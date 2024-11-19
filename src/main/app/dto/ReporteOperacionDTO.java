package main.app.dto;

import java.io.Serializable;

public class ReporteOperacionDTO implements Serializable{
	private Integer operacion;
	private Integer mantenimiento;
	
	
	
	public ReporteOperacionDTO() {
		super();
	}


	public ReporteOperacionDTO(Long operacion, Long mantenimiento) {
		super();
		this.operacion = operacion.intValue();
		this.mantenimiento = mantenimiento.intValue();
	}


	public Integer getOperacion() {
		return operacion;
	}


	public void setOperacion(Integer operacion) {
		this.operacion = operacion;
	}


	public Integer getMantenimiento() {
		return mantenimiento;
	}


	public void setMantenimiento(Integer mantenimiento) {
		this.mantenimiento = mantenimiento;
	}
	
	
}
