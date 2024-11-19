package main.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.ParameterizedTypeReference;

import main.app.dto.LogMantenimientoDTO;
import main.app.dto.MonopatinDTO;
import main.app.dto.ReporteKilometrosMonopatinDTO;
import main.app.dto.ReporteOperacionDTO;
import main.app.dto.ReporteTiempoMonopatinDTO;
import main.app.dto.ReporteViajesMonopatinDTO;
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
	
	@Value("${baseURLViajes")
	private final String baseURLViajes;
	
	

    
    public MonopatinService(RestTemplate restTemplate, MonopatinRepository monopatinRepository,String baseURLParada,String baseURLLogMantenimiento, String baseURLViajes) {
        this.restTemplate = restTemplate;
        this.monopatinRepository = monopatinRepository;
        this.baseURLParada = baseURLParada;
        this.baseURLLogMantenimiento = baseURLLogMantenimiento;
        this.baseURLViajes = baseURLViajes;
    }
    

    public List<Monopatin> getAll() {
        return monopatinRepository.findAll();
    }


    public ResponseEntity<String> save(Monopatin monopatin) {
    	if(monopatin.getParada()!=null && 
    		!existeParada(monopatin.getIdMonopatin())) {
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
		if((monopatinIncompleto.getIdMonopatin()!=null && monopatinIncompleto.getIdMonopatin()!=id)) {
			return new ResponseEntity<String>("No se puede editar id",HttpStatus.BAD_REQUEST);
		}
		if(monopatinIncompleto.getParada()!=null && !existeParada(monopatinIncompleto.getIdMonopatin())) {
			return new ResponseEntity<String>("La parada no existe",HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			Monopatin monopatin = this.monopatinRepository.findById(id).orElseThrow();
			boolean mantenimiento = monopatin.isMantenimiento();
			GenericObjectPatcher.patch(monopatinIncompleto, monopatin);
			monopatinRepository.save(monopatin);
			
			if(mantenimiento != monopatin.isMantenimiento()) {
				String reporte = monopatin.isMantenimiento() ? "Entro en mantenimiento" : "Termino el mantenimiento";
				LogMantenimientoDTO log = new LogMantenimientoDTO(LocalDate.now(),id,reporte);
				this.restTemplate.postForObject(this.baseURLLogMantenimiento + "/", log, null);
			}
			return new ResponseEntity<String>("Modificado",HttpStatus.OK);
		}catch(NoSuchElementException | IllegalArgumentException e ) {
			return new ResponseEntity<String>("El Monopatin no existe", HttpStatus.NOT_FOUND);
		}
	}


	public ResponseEntity<List<ReporteViajesMonopatinDTO>> reporteViajes(Integer maxViajes) {
		
		String url = this.baseURLViajes + "/cantViajesMonopatin";
		if(maxViajes!=null) {
			url += "/maxViajes=" + maxViajes;
		}
		return new ResponseEntity<List<ReporteViajesMonopatinDTO>>(this.<ReporteViajesMonopatinDTO>getReporte(url),HttpStatus.OK);
	}
	
	public ResponseEntity<List<ReporteTiempoMonopatinDTO>> reportePorTiempo(boolean incluirPausas) {
		
		String url = this.baseURLViajes + "/reporteMonopatin?pausas=" + incluirPausas;
		
		return new ResponseEntity<List<ReporteTiempoMonopatinDTO>>(this.<ReporteTiempoMonopatinDTO>getReporte(url),HttpStatus.OK);
	}
	
	private <T extends MonopatinDTO> List<T> getReporte(String url) {
		ParameterizedTypeReference<List<T>> type = new ParameterizedTypeReference<List<T>>(){};
		List<T> reporte = this.restTemplate.exchange(url,HttpMethod.GET,null,type).getBody();
		
		//to map para acceder en tiempo constante cuando se busca la informacion de un monopatin especifico para completar el dto
		Map<Integer,Monopatin> mapaMonopatines =this.monopatinRepository.findAll().stream().collect(Collectors.toMap(Monopatin::getIdMonopatin, Function.identity()));
		
		for (T dto : reporte) {
			dto.completarInfo(mapaMonopatines.get(dto.getId()));
			
			//no se si anda
			//GenericObjectPatcher.patch(mapaMonopatines.get(dto.getId()), dto);
			
		}
		
		return reporte;
		
	}


	public ResponseEntity<List<ReporteKilometrosMonopatinDTO>> reportePorKilometros(boolean conPausa) {
		String url = this.baseURLViajes + "/reporteKilometrosMonopatin?pausas=" + conPausa;
		
		return new ResponseEntity<List<ReporteKilometrosMonopatinDTO>>(this.<ReporteKilometrosMonopatinDTO>getReporte(url),HttpStatus.OK);
	}
	
}
