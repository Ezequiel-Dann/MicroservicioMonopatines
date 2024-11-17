package main.app.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;

import main.app.dto.GPSDTO;
import main.app.dto.ReporteOperacionDTO;
import main.app.model.Monopatin;
import main.app.repository.MonopatinRepository;

public class MonopatinService {
	@Autowired
	private final MonopatinRepository monopatinRepository;
	@Autowired
	private final RestTemplate restTemplate;

    
    public MonopatinService(RestTemplate restTemplate, MonopatinRepository monopatinRepository) {
        this.restTemplate = restTemplate;
        this.monopatinRepository = monopatinRepository;
    }
    

    public List<Monopatin> getAll() {
        return monopatinRepository.findAll();
    }


    public ResponseEntity<String> save(Monopatin monopatin) {
    	try {
    		monopatinRepository.save(monopatin);
    		return new ResponseEntity<String>("agregado", HttpStatus.CREATED);
    	}catch(IllegalArgumentException e) {
    		return  new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    	}
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
	
	
}
