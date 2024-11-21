package main.app.dto;

import java.io.Serializable;
public class PostMonopatinDTO implements Serializable{

    private boolean disponible;
    private boolean encendido;
    private boolean mantenimiento;
    private double longitud;
    private double latitud;
    
    public PostMonopatinDTO() {
    	
    }
    
    

	public PostMonopatinDTO(boolean disponible, boolean encendido, boolean mantenimiento, double longitud,
			double latitud) {
		super();
		this.disponible = disponible;
		this.encendido = encendido;
		this.mantenimiento = mantenimiento;
		this.longitud = longitud;
		this.latitud = latitud;
	}



	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public boolean isEncendido() {
		return encendido;
	}

	public void setEncendido(boolean encendido) {
		this.encendido = encendido;
	}

	public boolean isMantenimiento() {
		return mantenimiento;
	}

	public void setMantenimiento(boolean mantenimiento) {
		this.mantenimiento = mantenimiento;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	

}