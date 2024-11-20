package main.app.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogMantenimientoDTO implements Serializable{
	private LocalDateTime fecha;
	private Integer idMonopatin;
	private String reporte;
	
	public LogMantenimientoDTO() {
	}
	
	public LogMantenimientoDTO(LocalDateTime fecha, Integer idMonopatin, String reporte) {
		super();
		this.fecha = fecha;
		this.idMonopatin = idMonopatin;
		this.reporte = reporte;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Integer getIdMonopatin() {
		return idMonopatin;
	}

	public void setIdMonopatin(Integer idMonopatin) {
		this.idMonopatin = idMonopatin;
	}

	public String getReporte() {
		return reporte;
	}

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
	
	
}
