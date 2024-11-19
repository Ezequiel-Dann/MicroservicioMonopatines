package main.app.dto;

import java.io.Serializable;

import main.app.model.Monopatin;

public class ReporteViajesMonopatinDTO extends MonopatinDTO implements Serializable{

    
    private Integer cantViajes;

    public ReporteViajesMonopatinDTO() {
	}
    
	public ReporteViajesMonopatinDTO(Integer id, boolean isDisponible, boolean isEncendido, double longitud,
			double latitud, Integer parada, boolean mantenimiento, Integer cantViajes) {
		super(id,isDisponible,isEncendido,longitud,latitud,mantenimiento,parada);
		
		this.cantViajes = cantViajes;
	}

	public Integer getCantViajes() {
		return cantViajes;
	}

	public void setCantViajes(Integer cantViajes) {
		this.cantViajes = cantViajes;
	}
    
}
