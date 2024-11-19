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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import main.app.dto.MonopatinDTO;
import main.app.dto.ReporteKilometrosMonopatinDTO;
import main.app.dto.ReporteOperacionDTO;
import main.app.dto.ReporteTiempoMonopatinDTO;
import main.app.dto.ReporteViajesMonopatinDTO;
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
	
	@Operation(summary = "Obtener todos los monopatines", description = "Recupera todos los monopatines registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Monopatines obtenidos exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Monopatin.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/")
    public ResponseEntity<Iterable<Monopatin>> getAll() {
        Iterable<Monopatin> monopatines = monopatinService.getAll();
        return new ResponseEntity<>(monopatines, HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo monopatín", description = "Guarda un nuevo monopatín en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Monopatín creado exitosamente", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta - entrada no válida")
    })
    @PostMapping("/")
    public ResponseEntity<String> save(@RequestBody Monopatin monopatin) {
        return monopatinService.save(monopatin);
    }

    @Operation(summary = "Buscar monopatín por ID", description = "Recupera un monopatín usando su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Monopatín encontrado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Monopatin.class))),
        @ApiResponse(responseCode = "404", description = "Monopatín no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Monopatin> findById(@Parameter(description = "ID del monopatín") @PathVariable Integer id) {
        return monopatinService.findById(id);
    }

    @Operation(summary = "Obtener todos los monopatines disponibles", description = "Recupera los monopatines que están disponibles para ser usados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Monopatines disponibles obtenidos exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Monopatin.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/disponibles")
    public List<Monopatin> getAllDisponibles() {
        return this.monopatinService.getAllDisponibles();
    }

    @Operation(summary = "Obtener reporte de operación y mantenimiento", description = "Recupera un reporte de la operación y el mantenimiento de los monopatines.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte de operación y mantenimiento obtenido exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteOperacionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reporteOperacion")
    public ResponseEntity<ReporteOperacionDTO> getReporteOperacionYMantenimiento() {
        return this.monopatinService.getReporteOperacionYMantenimiento();
    }

    @Operation(summary = "Eliminar un monopatín por ID", description = "Elimina un monopatín del sistema usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Monopatín eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Monopatín no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(description = "ID del monopatín a eliminar") @PathVariable Integer id) {
        return monopatinService.delete(id);
    }

    /*
    @Operation(summary = "Actualizar estado del monopatín", description = "Permite cambiar el estado de un monopatín (por ejemplo: en mantenimiento, disponible, encendido).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del monopatín actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> cambiarEstado(
        @RequestParam(required = false) Boolean mantenimiento,
        @RequestParam(required = false) Boolean disponible,
        @RequestParam(required = false) Boolean encendido,
        @Parameter(description = "ID del monopatín") @PathVariable Integer id) {
        return monopatinService.cambiarEstado(id, mantenimiento, disponible, encendido);
    }
     */
    @Operation(summary = "Actualizar parcialmente un monopatín", description = "Permite actualizar parcialmente los campos de un monopatín.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Monopatín actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta"),
        @ApiResponse(responseCode = "404", description = "Monopatin no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@RequestBody Monopatin monopatin, @PathVariable Integer id) {
        return monopatinService.patch(monopatin, id);
    }

    @Operation(summary = "Generar reporte de viajes", description = "Recupera un reporte con información sobre los viajes realizados por los monopatines.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte de viajes obtenido exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteViajesMonopatinDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reporteViajes")
    public ResponseEntity<List<ReporteViajesMonopatinDTO>> reporteViajes(@RequestParam(required = false) Integer MaxViajes) {
        return monopatinService.reporteViajes(MaxViajes);
    }
    
    
    @Operation(summary = "Generar reporte de tiempo de uso", description = "Recupera un reporte con el tiempo de uso de los monopatines.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte de tiempo de uso obtenido exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteTiempoMonopatinDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reporteTiempo")
    public ResponseEntity<List<ReporteTiempoMonopatinDTO>> reportePorTiempo(@RequestParam(required = false) boolean conPausa) {
        return monopatinService.reportePorTiempo(conPausa);
    }

    @Operation(summary = "Generar reporte de kilómetros", description = "Recupera un reporte con los kilómetros recorridos por los monopatines.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte de kilómetros obtenido exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteKilometrosMonopatinDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reporteKilometros")
    public ResponseEntity<List<ReporteKilometrosMonopatinDTO>> reportePorKilometros(@RequestParam(required = false) boolean conPausa) {
        return monopatinService.reportePorKilometros(conPausa);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMonopatin (@PathVariable Integer idMonopatin,@RequestBody Monopatin monopatin){
    	return this.monopatinService.updateMonopatin(idMonopatin,monopatin);
    }
    
    @PutMapping("/{id}/estacionar")
    public ResponseEntity<String> estacionar (@PathVariable Integer idMonopatin){
    	return this.monopatinService.estacionar(idMonopatin);
    }
}