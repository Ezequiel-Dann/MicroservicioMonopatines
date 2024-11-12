package main.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.DTO.VerificacionParadaDTO;

import main.app.dto.GPSDTO;
import main.app.model.Monopatin;
import main.app.repository.MonopatinRepository;

public class MonopatinService {
	private final MonopatinRepository monopatinRepository;
	private final RestTemplate restTemplate;

    @Autowired
    public MonopatinService(RestTemplate restTemplate, MonopatinRepository monopatinRepository) {
        this.restTemplate = restTemplate;
        this.monopatinRepository = monopatinRepository;
    }
    

    public List<Monopatin> getAll() {
        return monopatinRepository.findAll();
    }


    public Monopatin save(Monopatin monopatin) {
        return monopatinRepository.save(monopatin);
    }

    // TODO No se si esta bien 
    public Optional<Monopatin> findById(UUID id) {
        return monopatinRepository.findById(id);
    }

    // Obtener monopatines disponibles en una parada específica
    public List<Monopatin> getDisponiblesEnParada(Long paradaId) {
        return monopatinRepository.findDisponiblesByParadaId(paradaId);
    }

    // Obtener monopatines en mantenimiento en una parada específica
    public List<Monopatin> getEnMantenimientoEnParada(Long paradaId) {
        return monopatinRepository.findEnMantenimientoByParadaId(paradaId);
    }

    // Actualizar la disponibilidad de un monopatín
    public Monopatin actualizarDisponibilidad(UUID id, boolean disponible) {
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
    public Monopatin iniciarOCortarViaje(UUID id, boolean encendido) {
        Optional<Monopatin> monopatinOpt = monopatinRepository.findById(id);
        if (monopatinOpt.isPresent()) {
            Monopatin monopatin = monopatinOpt.get();
            monopatin.setEncendido(encendido);
            monopatin.setDisponible(!encendido); // Si está encendido, no está disponible
            return monopatinRepository.save(monopatin);
        } else {
            throw new IllegalArgumentException("Monopatin no encontrado.");
        }
    }
    public List<Monopatin> getAllDisponibles() {
        return monopatinRepository.findAllDisponibles();
    }
  //TODO verificar el url
    public boolean verificarUbicacionEnParada(GPSDTO gpsMonopatin) {
        String url = "No se si esto esta bien";

        // Crear el objeto de solicitud con GPS del monopatín y ID de parada
        VerificacionParadaDTO solicitud = new VerificacionParadaDTO(gpsMonopatin);
        
        // Enviar la solicitud al microservicio de Parada y obtener la respuesta
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, solicitud, Boolean.class);

        // Retornar el resultado de la verificación 
        return response.getBody() != null && response.getBody();
    }
}
