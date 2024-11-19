package main.app.dto;

import java.io.Serializable;

public class ReporteKilometrosMonopatinDTO extends MonopatinDTO implements Serializable{
	
	private double kilometros;

	public ReporteKilometrosMonopatinDTO() {
		super();
	}

	public ReporteKilometrosMonopatinDTO(Integer id, boolean isDisponible, boolean isEncendido, double longitud,
			double latitud, boolean mantenimiento, Integer parada,double kilometros) {
		super(id, isDisponible, isEncendido, longitud, latitud, mantenimiento, parada);
		this.setKilometros(kilometros);
	}

	public double getKilometros() {
		return kilometros;
	}

	public void setKilometros(double kilometros) {
		this.kilometros = kilometros;
	}
	
	
	
}
