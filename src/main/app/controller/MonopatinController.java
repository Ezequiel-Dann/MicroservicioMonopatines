package main.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.app.dto.MonopatinDTO;
import main.app.dto.ReporteOperacionDTO;
import main.app.model.Monopatin;
import main.app.service.MonopatinService;

@RestController
@RequestMapping("/monopatines")
public class MonopatinController {

	@Autowired
	private final MonopatinService monopatinService;
	
	
	
	public MonopatinController(MonopatinService monopatinService) {
		super();
		this.monopatinService = monopatinService;
	}

	@GetMapping("/")
	public ResponseEntity<Iterable<Monopatin>> getAll() {
		Iterable<Monopatin> monopatines = monopatinService.getAll();
		return new ResponseEntity<Iterable<Monopatin>>(monopatines,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<String> save(@RequestBody Monopatin monopatin) {
		return monopatinService.save(monopatin);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Monopatin> findById(@PathVariable Integer id){
		return monopatinService.findById(id);
	}
	
	@GetMapping("/disponibles")
	public List<Monopatin> getAllDisponibles(){
		return this.monopatinService.getAllDisponibles();
	}
	
	@GetMapping("/reporteOperacion")
	public ResponseEntity<ReporteOperacionDTO> getReporteOperacionYMantenimiento(){
		return this.monopatinService.getReporteOperacionYMantenimiento();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		return monopatinService.delete(id);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<String> cambiarEstado(@RequestParam (required=false) Boolean mantenimiento,
												@RequestParam (required=false) Boolean disponible,
												@RequestParam (required=false) Boolean encendido,
												@PathVariable Integer id){
		return monopatinService.cambiarEstado(id,mantenimiento,disponible,encendido);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> patch(@RequestBody Monopatin monopatin,@PathVariable Integer id){
		return monopatinService.patch(monopatin,id);
	}
	
	/*TODO
	Como administrador quiero consultar los monopatines con más de X viajes en un cierto año
	
	 Como encargado de mantenimiento quiero poder generar un reporte de uso de monopatines por
	kilómetros para establecer si un monopatín requiere de mantenimiento. Este reporte debe poder
	configurarse para incluir (o no) los tiempos de pausa.
	*/
}
