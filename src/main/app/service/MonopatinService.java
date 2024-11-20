package main.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.ParameterizedTypeReference;

import main.app.dto.LogMantenimientoDTO;
import main.app.dto.MonopatinDTO;
import main.app.dto.ReporteKilometrosMonopatinDTO;
import main.app.dto.ReporteOperacionDTO;
import main.app.dto.ReporteTiempoMonopatinDTO;
import main.app.dto.ReporteViajesMonopatinDTO;
import main.app.dto.UbicacionDTO;
import main.app.model.Monopatin;
import main.app.repository.MonopatinRepository;
import main.app.security.TokenHandler;
import main.app.utils.GenericObjectPatcher;

@Service
public class MonopatinService {
	@Autowired
	private final MonopatinRepository monopatinRepository;
	@Autowired
	private final RestTemplate restTemplate;
	@Autowired
	private final TokenHandler tokenHandler;
	
	@Value("${baseURLParada}")
	private String baseURLParada;
	
	@Value("${baseURLLogMantenimiento}")
	private String baseURLLogMantenimiento;
	
	@Value("${baseURLViajes}")
	private String baseURLViajes;

    
    public MonopatinService(RestTemplate restTemplate, MonopatinRepository monopatinRepository,TokenHandler tokenHandler) {
        this.restTemplate = restTemplate;
        this.monopatinRepository = monopatinRepository;
		this.tokenHandler = tokenHandler;
    }
    

    public List<Monopatin> getAll() {
        return monopatinRepository.findAll();
    }


    public ResponseEntity<String> save(Monopatin monopatin) {
    	if(monopatin.getIdParada()!=null && 
    		!existeParada(monopatin.getIdParada())) {
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
    	try {
    		HttpHeaders header = tokenHandler.createMicroserviceHeader();
    		HttpEntity<String> entity = new HttpEntity<String>(header);
    		
    		return this.restTemplate.exchange(this.baseURLParada + "/" + id,HttpMethod.GET,entity, Object.class).getStatusCode().is2xxSuccessful();
    	}catch(HttpClientErrorException.NotFound e) {
    		return false;
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
    
    public ResponseEntity<String> estacionar(Integer idMonopatin) {
    	Optional<Monopatin> optionalMonopatin = this.monopatinRepository.findById(idMonopatin);
    	System.out.println(1);
    	System.out.println(tokenHandler.getToken());
    	try {
    		Monopatin monopatin = optionalMonopatin.orElseThrow();
    		String url = this.baseURLParada + "/estacionarMonopatin";
    		
    		
    		// Enviar la solicitud al microservicio de Parada y obtener la respuesta
    		HttpHeaders header = tokenHandler.createMicroserviceHeader();
    		HttpEntity<Monopatin> entity = new HttpEntity<Monopatin>(monopatin,header);
    		
    		ResponseEntity<Integer> response = restTemplate.postForEntity(url, entity, Integer.class);
    		if(response.getStatusCode().is2xxSuccessful()) {
    			monopatin.setDisponible(true);
    			monopatin.setEncendido(false);
    			monopatin.setIdParada(response.getBody());
    			this.monopatinRepository.save(monopatin);
    			return new ResponseEntity<String>("Estacionado",HttpStatus.OK);
    		}else {
    			return new ResponseEntity<String>("El monopatin no se encuentra en una parada",HttpStatus.CONFLICT);
    		}
    	}catch(NoSuchElementException e) {
    		return new ResponseEntity<String>("El monopatin no existe",HttpStatus.NOT_FOUND);
    	}
    }


	public ResponseEntity<String> delete(Integer id) {
		
		HttpHeaders header = tokenHandler.createMicroserviceHeader();
		HttpEntity<Integer> entity = new HttpEntity<Integer>(id,header);
		this.restTemplate.exchange(baseURLParada + "/borrarMonopatin/"+ id,HttpMethod.DELETE, entity, String.class);
		
		this.monopatinRepository.deleteById(id);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}


	public ResponseEntity<ReporteOperacionDTO> getReporteOperacionYMantenimiento() {
		ReporteOperacionDTO reporte = this.monopatinRepository.getReporteOperacionYMantenimiento();
		return new ResponseEntity<ReporteOperacionDTO>(reporte,HttpStatus.OK);
	}

/*
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
		
	}*/


	public ResponseEntity<?> patch(Monopatin monopatinIncompleto, Integer id) {
		if((monopatinIncompleto.getIdMonopatin()!=null && monopatinIncompleto.getIdMonopatin()!=id)) {
			return new ResponseEntity<String>("No se puede editar id",HttpStatus.BAD_REQUEST);
		}
		System.out.println(monopatinIncompleto);
		if(monopatinIncompleto.getIdParada()!=null && !existeParada(monopatinIncompleto.getIdParada())) {
			return new ResponseEntity<String>("La parada no existe",HttpStatus.BAD_REQUEST);
		}
		
		try {
			Monopatin monopatin = this.monopatinRepository.findById(id).orElseThrow();
			
			//mantenimiento anterior
			boolean mantenimientoAnterior = monopatin.isMantenimiento();
			GenericObjectPatcher.patch(monopatinIncompleto, monopatin);
			monopatinRepository.save(monopatin);
			
			String reporte = monopatin.isMantenimiento() ? "Entro en mantenimiento" : "Termino el mantenimiento";
			LogMantenimientoDTO log = new LogMantenimientoDTO(LocalDateTime.now(),id,reporte);
			System.out.println(this.baseURLLogMantenimiento + "/");
			System.out.println(log);
			if(mantenimientoAnterior != monopatin.isMantenimiento()) {
				HttpHeaders header = tokenHandler.createMicroserviceHeader();
				HttpEntity<LogMantenimientoDTO> entity = new HttpEntity<LogMantenimientoDTO>(log,header);
				this.restTemplate.postForObject(this.baseURLLogMantenimiento + "/", entity, Void.class);
			}
			return new ResponseEntity<String>("Modificado",HttpStatus.OK);
		}catch(NoSuchElementException e ) {
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
		
		//TODO connect to viajes
		
		HttpHeaders header = tokenHandler.createMicroserviceHeader();
		HttpEntity<String> entity = new HttpEntity<String>(header);
		
		ParameterizedTypeReference<List<T>> type = new ParameterizedTypeReference<List<T>>(){};
		List<T> reporte = this.restTemplate.exchange(url,HttpMethod.GET,entity,type).getBody();
		
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
		
		//TODO connect to viajes
		String url = this.baseURLViajes + "/reporteKilometrosMonopatin?pausas=" + conPausa;
		
		return new ResponseEntity<List<ReporteKilometrosMonopatinDTO>>(this.<ReporteKilometrosMonopatinDTO>getReporte(url),HttpStatus.OK);
	}


	public ResponseEntity<String> updateMonopatin(Integer idMonopatin, Monopatin monopatinNuevo) {
		
		//TODO borrar
		Optional<Monopatin> optionalMonopatin = this.monopatinRepository.findById(idMonopatin);
		try {
			Monopatin monopatin = optionalMonopatin.orElseThrow();
			monopatinNuevo.setIdMonopatin(idMonopatin);
			GenericObjectPatcher.patch(monopatinNuevo, monopatin);
			this.monopatinRepository.save(monopatin);
			return new ResponseEntity<String>("Actualizado", HttpStatus.OK); 
		}catch(NoSuchElementException e) {
			return new ResponseEntity<String>("El Monopatin no existe", HttpStatus.NOT_FOUND); 
		}
		
	}
	
}
