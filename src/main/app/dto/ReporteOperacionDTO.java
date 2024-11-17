package main.app.dto;

public class ReporteOperacionDTO {
	private Integer operacion;
	private Integer mantenimiento;
	
	
	
	public ReporteOperacionDTO() {
		super();
	}


	public ReporteOperacionDTO(Integer operacion, Integer mantenimiento) {
		super();
		this.operacion = operacion;
		this.mantenimiento = mantenimiento;
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
