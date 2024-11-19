package main.app.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class LogMantenimientoDTO implements Serializable{
	private LocalDate fecha;
	private Integer idMonopatin;
	private String reporte;
	
	public LogMantenimientoDTO() {
	}
	
	public LogMantenimientoDTO(LocalDate fecha, Integer idMonopatin, String reporte) {
		super();
		this.fecha = fecha;
		this.idMonopatin = idMonopatin;
		this.reporte = reporte;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
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
