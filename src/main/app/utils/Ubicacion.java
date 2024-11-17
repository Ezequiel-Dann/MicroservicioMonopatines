package main.app.utils;

public class Ubicacion {
	private double longitud;
	private double latitud;
	
	
	
	public Ubicacion(double longitud, double latitud) {
		super();
		this.longitud = longitud;
		this.latitud = latitud;
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



	public boolean isInLocation(Ubicacion otraUbicacion) {
		return Math.abs(this.longitud - otraUbicacion.getLongitud()) <0.05 && Math.abs(this.latitud - otraUbicacion.getLatitud())<0.05;
	}
}
