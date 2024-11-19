package main.app.dto;

import java.io.Serializable;
import java.time.Duration;

import main.app.model.Monopatin;

public class ReporteTiempoMonopatinDTO extends MonopatinDTO implements Serializable{


    
    private Duration tiempo;

    public ReporteTiempoMonopatinDTO() {
	}
    
	public ReporteTiempoMonopatinDTO(Integer id, boolean isDisponible, boolean isEncendido, double longitud,
			double latitud, Integer parada, boolean mantenimiento, Duration tiempo) {
		super(id,isDisponible,isEncendido,longitud,latitud,mantenimiento,parada);
		this.tiempo = tiempo;
	}
    
	public Duration getTiempo() {
		return tiempo;
	}

	public void setTiempo(Duration tiempo) {
		this.tiempo = tiempo;
	}    
}
