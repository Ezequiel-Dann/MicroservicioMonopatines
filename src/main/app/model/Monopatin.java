package main.app.model;

import jakarta.persistence.*;

@Entity
public class Monopatin {
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
