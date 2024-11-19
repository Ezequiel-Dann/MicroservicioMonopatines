package main.app.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
public class Monopatin implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMonopatin;

    @Column
    private boolean disponible;
    @Column
    private boolean encendido;
    @Column
    private Double longitud;
    @Column
    private Double latitud;
    @Column
    private Integer idParada;
    @Column 
    private boolean mantenimiento;
    
    public Monopatin() {
    	
    }

    public Monopatin(Integer id, boolean disponible, boolean encendido, Double longitud, Double latitud,
			Integer parada,boolean mantenimiento) {
		super();
		this.idMonopatin = id;
		this.disponible = disponible;
		this.encendido = encendido;
		this.longitud = longitud;
		this.latitud = latitud;
		this.idParada = parada;
		this.mantenimiento = mantenimiento;
	}

	public Integer getIdMonopatin() {
		return idMonopatin;
	}

	public void setIdMonopatin(Integer idMonopatin) {
		this.idMonopatin = idMonopatin;
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

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Integer getIdParada() {
		return idParada;
	}

	public void setIdParada(Integer idParada) {
		this.idParada = idParada;
	}

	public boolean isMantenimiento() {
		return mantenimiento;
	}

	public void setMantenimiento(boolean mantenimiento) {
		this.mantenimiento = mantenimiento;
	}

	@Override
	public String toString() {
		return "Monopatin [idMonopatin=" + idMonopatin + ", disponible=" + disponible + ", encendido=" + encendido
				+ ", longitud=" + longitud + ", latitud=" + latitud + ", idParada=" + idParada + ", mantenimiento="
				+ mantenimiento + "]";
	}



    
}
