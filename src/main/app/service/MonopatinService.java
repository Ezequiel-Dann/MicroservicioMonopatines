package main.app.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;

import main.app.dto.GPSDTO;
import main.app.dto.MonopatinDTO;
import main.app.dto.ReporteOperacionDTO;
import main.app.model.Monopatin;
import main.app.repository.MonopatinRepository;
import main.app.utils.GenericObjectPatcher;

public class MonopatinService {
	@Autowired
	private final MonopatinRepository monopatinRepository;
	@Autowired
	private final RestTemplate restTemplate;
	
	@Value("${baseURLParada}")
	private final String baseURLParada;
	
	@Value("${baseURLLogMantenimiento}")
	private final String baseURLLogMantenimiento;

    
    public MonopatinService(RestTemplate restTemplate, MonopatinRepository monopatinRepository,String baseURLParada,String baseURLLogMantenimiento) {
        this.restTemplate = restTemplate;
        this.monopatinRepository = monopatinRepository;
        this.baseURLParada = baseURLParada;
        this.baseURLLogMantenimiento = baseURLLogMantenimiento;
    }
    

    public List<Monopatin> getAll() {
        return monopatinRepository.findAll();
    }


    public ResponseEntity<String> save(Monopatin monopatin) {
    	if(monopatin.getParada()!=null && 
    		!existeParada(monopatin.getId())) {
			return new ResponseEntity<String>("La parada no existe",HttpStatus.BAD_REQUEST);
		}
    	
    	try {
    		
    		monopatinRepository.save(monopatin);
    		return new ResponseEntity<String>("agregado", HttpStatus.CREATED);
    	}catch(IllegalArgumentException e) {
    		return  new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    	}
    }

    private boolean existeParada(Integer id) {
		return this.restTemplate.getForEntity(this.baseURLParada + "/" + id, Object.class).getStatusCode().is2xxSuccessful();
	}


	public ResponseEntity<Monopatin> findById(Integer id) {
    	Optional<Monopatin> monopatin = monopatinRepository.findById(id);
    	
    	if(monopatin.isPresent()) {
    		return new ResponseEntity<Monopatin>(monopatin.get(),HttpStatus.OK);
    	}
    	return new ResponseEntity<Monopatin>(HttpStatus.NOT_FOUND);

    }

    // Obtener monopatines disponibles en una parada espec�fica
    public List<Monopatin> getDisponiblesEnParada(Integer paradaId) {
        return monopatinRepository.findDisponiblesByParadaId(paradaId);
    }

    // Obtener monopatines en mantenimiento en una parada espec�fica
    public List<Monopatin> getEnMantenimientoEnParada(Integer paradaId) {
        return monopatinRepository.findEnMantenimientoByParadaId(paradaId);
    }

    // Actualizar la disponibilidad de un monopat�n
    public Monopatin actualizarDisponibilidad(Integer id, boolean disponible) {
        Optional<Monopatin> monopatinOpt = monopatinRepository.findById(id);
        if (monopatinOpt.isPresent()) {
            Monopatin monopatin = monopatinOpt.get();
            monopatin.setDisponible(disponible);
            return monopatinRepository.save(monopatin);
        } else {
            throw new IllegalArgumentException("Monopatin no encontrado.");
        }
    }

    // Iniciar o cortar un viaje
    public Monopatin iniciarOCortarViaje(Integer id, boolean encendido) {
        Optional<Monopatin> monopatinOpt = monopatinRepository.findById(id);
        if (monopatinOpt.isPresent()) {
            Monopatin monopatin = monopatinOpt.get();
            monopatin.setEncendido(encendido);
            monopatin.setDisponible(!encendido); // Si est� encendido, no est� disponible
            return monopatinRepository.save(monopatin);
        } else {
            throw new IllegalArgumentException("Monopatin no encontrado.");
        }
    }
    
    public List<Monopatin> getAllDisponibles() {
        return monopatinRepository.findAllDisponibles();
    }
    
  //TODO verificar el url
    /*
    public boolean verificarUbicacionEnParada(GPSDTO gpsMonopatin) {
        String url = "No se si esto esta bien";

        // Crear el objeto de solicitud con GPS del monopat�n y ID de parada
        VerificacionParadaDTO solicitud = new VerificacionParadaDTO(gpsMonopatin);
        
        // Enviar la solicitud al microservicio de Parada y obtener la respuesta
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, solicitud, Boolean.class);

        // Retornar el resultado de la verificaci�n 
        return response.getBody() != null && response.getBody();
    }*/


	public ResponseEntity<String> delete(Integer id) {
		this.monopatinRepository.deleteById(id);
		this.restTemplate.delete(baseURLParada + "/"+ id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}


	public ResponseEntity<ReporteOperacionDTO> getReporteOperacionYMantenimiento() {
		ReporteOperacionDTO reporte = this.monopatinRepository.getReporteOperacionYMantenimiento();
		return new ResponseEntity<ReporteOperacionDTO>(reporte,HttpStatus.OK);
	}


	public ResponseEntity<String> cambiarEstado(Integer id, Boolean mantenimiento, Boolean disponible, Boolean encendido) {
		try {
			Monopatin monopatin = this.monopatinRepository.findById(id).orElseThrow();
			if(mantenimiento!=null) {
				monopatin.setMantenimiento(mantenimiento);
				this.restTemplate.postForEntity(baseURLLogMantenimiento + "/", monopatin, null);
			}
			if(disponible!=null) {
				monopatin.setDisponible(false);
			}
			if(encendido!=null) {
				monopatin.setEncendido(false);
			}
			monopatinRepository.save(monopatin);
			return new ResponseEntity<String>(HttpStatus.OK);
			
		}catch(NoSuchElementException e) {
			return new ResponseEntity<String>("El monopatin no existe",HttpStatus.NOT_FOUND);
		}
		
	}


	public ResponseEntity<?> patch(Monopatin monopatinIncompleto, Integer id) {
		if((monopatinIncompleto.getId()!=null)) {
			return new ResponseEntity<String>("No se puede editar id",HttpStatus.BAD_REQUEST);
		}
		if(monopatinIncompleto.getParada()!=null && !existeParada(monopatinIncompleto.getId())) {
			return new ResponseEntity<String>("La parada no existe",HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			Monopatin monopatin = this.monopatinRepository.findById(id).orElseThrow();
			GenericObjectPatcher.patch(monopatinIncompleto, monopatin);
			monopatinRepository.save(monopatin);
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<String>("id Invalido",HttpStatus.NOT_FOUND);
		}
	}
	
	
}
