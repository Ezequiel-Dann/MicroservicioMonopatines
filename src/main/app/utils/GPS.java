package main.app.utils;

public class GPS {
	private double longitud;
	private double latitud;
	
	
	
	public GPS(double longitud, double latitud) {
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



	public boolean isInLocation(double longitud,double latitud) {
		return Math.abs(this.longitud - longitud) <0.05 && Math.abs(this.latitud - latitud)<0.05;
	}
}
