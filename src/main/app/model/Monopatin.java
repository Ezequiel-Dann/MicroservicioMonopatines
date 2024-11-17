package main.app.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
public class Monopatin implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private boolean isDisponible;
    @Column
    private boolean isEncendido;
    @Column
    private double longitud;
    @Column
    private double latitud;
    @Column
    private Integer parada;
    @Column 
    private boolean mantenimiento;
    
    public Monopatin() {
    	
    }

    public Monopatin(Integer id, boolean isDisponible, boolean isEncendido, double longitud, double latitud,
			Integer parada,boolean mantenimiento) {
		super();
		this.id = id;
		this.isDisponible = isDisponible;
		this.isEncendido = isEncendido;
		this.longitud = longitud;
		this.latitud = latitud;
		this.parada = parada;
		this.mantenimiento = mantenimiento;
	}

    
    
	public boolean isMantenimiento() {
		return mantenimiento;
	}

	public void setMantenimiento(boolean mantenimiento) {
		this.mantenimiento = mantenimiento;
	}

	public Integer getParada() {
		return parada;
	}

	public void setParada(Integer parada) {
		this.parada = parada;
	}

	public Integer getId() {
        return id;
    }

    public boolean isDisponible() {
        return isDisponible;
    }

    public boolean isEncendido() {
        return isEncendido;
    }


    public void setDisponible(boolean disponible) {
        isDisponible = disponible;
    }

    public void setEncendido(boolean encendido) {
        isEncendido = encendido;
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
