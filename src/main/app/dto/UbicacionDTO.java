package main.app.dto;

public class UbicacionDTO {

	private double latitud;
	private double longitud;
	
	public UbicacionDTO() {
		super();
	}
	
	public UbicacionDTO(double latitud, double longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
}
