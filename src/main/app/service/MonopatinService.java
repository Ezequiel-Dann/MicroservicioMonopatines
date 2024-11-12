package main.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import main.app.repository.MonopatinRepository;

public class MonopatinService {
	private final MonopatinRepository monopatinRepository;

    @Autowired
    public MonopatinService(MonopatinRepository monopatinRepository) {
        this.monopatinRepository = monopatinRepository;
    }

    // Obtener todos los monopatines
    public List<Monopatin> getAll() {
        return monopatinRepository.findAll();
    }

    // Guardar un nuevo monopat�n
    public Monopatin save(Monopatin monopatin) {
        return monopatinRepository.save(monopatin);
    }

    // Buscar monopat�n por ID �nico
    public Optional<Monopatin> findById(UUID id) {
        return monopatinRepository.findById(id);
    }

    // Obtener monopatines disponibles en una parada espec�fica
    public List<Monopatin> getDisponiblesEnParada(Long paradaId) {
        return monopatinRepository.findDisponiblesByParadaId(paradaId);
    }

    // Obtener monopatines en mantenimiento en una parada espec�fica
    public List<Monopatin> getEnMantenimientoEnParada(Long paradaId) {
        return monopatinRepository.findEnMantenimientoByParadaId(paradaId);
    }

    // Actualizar la disponibilidad de un monopat�n
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
            monopatin.setDisponible(!encendido); // Si est� encendido, no est� disponible
            return monopatinRepository.save(monopatin);
        } else {
            throw new IllegalArgumentException("Monopatin no encontrado.");
        }
    }
}
