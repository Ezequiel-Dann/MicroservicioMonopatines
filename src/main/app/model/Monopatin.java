package main.app.model;

import jakarta.persistence.*;

@Entity
public class Monopatin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private boolean isDisponible;
    @Column
    private boolean isEncendido;
    @Column
    private GPS gps;
    @Column
    @OneToMany
    private Parada parada;

    public Monopatin(boolean isDisponible, boolean isEncendido, GPS gps, Parada parada) {
        this.isDisponible = isDisponible;
        this.isEncendido = isEncendido;
        this.gps = gps;
        this.parada = parada;
    }

    public String getId() {
        return id;
    }

    public boolean isDisponible() {
        return isDisponible;
    }

    public boolean isEncendido() {
        return isEncendido;
    }

    public GPS getGps() {
        return gps;
    }

    public Parada getParada() {
        return parada;
    }

    public void setDisponible(boolean disponible) {
        isDisponible = disponible;
    }

    public void setEncendido(boolean encendido) {
        isEncendido = encendido;
    }

    public void setGps(GPS gps) {
        this.gps = gps;
    }

    public void setParada(Parada parada) {
        this.parada = parada;
    }
}
