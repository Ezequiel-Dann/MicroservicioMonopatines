package main.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Brief: Manejo de las paradas donde tendremos su ubicacion en el mapa mediante GPS y los monopatines
 * @Params
 * GPS: Se utiliza para manejar la ubicacion de la parada
 * monopatines: La lista de los monopatines en esa parada
 */
@Entity
public class Parada {

    @Column
    private GPS ubicacion;
    @Column
    private List<Monopatin> monopatines;

    public Parada(GPS ubicacion) {
        this.ubicacion = ubicacion;
        this.monopatines = new ArrayList<Monopatin>();
    }

    public GPS getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GPS ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void addMonopatin(Monopatin M) {
        monopatines.add(M);
    }

    public boolean verificaParadaValida(Monopatin M) {
        if (M.getGps().equals(this.ubicacion))
            return true;
        else
            return false;
    }
    /// Devuelve todos los monopatines **disponibles** de la parada
    public List<Monopatin> getDisponibles() {
        ArrayList<Monopatin> aux = new ArrayList<>();
        for(Monopatin m:monopatines) {
            if(m.isDisponible())
                aux.add(m);
        }
        return aux;
    }


}
