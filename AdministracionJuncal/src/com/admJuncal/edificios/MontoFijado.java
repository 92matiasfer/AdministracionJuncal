package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MontoFijado {
	
	private int idEdificio = 0;
	private String anio = "";
	private double montoTotal = 0;
	private double montoGastosComunes = 0;
	private double montoFondoReserva = 0;
	private String ultimaActualizacion = "";
	
	public int getIdEdificio() {
		return idEdificio;
	}
	public void setIdEdificio(int idEdificio) {
		this.idEdificio = idEdificio;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}
	public double getMontoGastosComunes() {
		return montoGastosComunes;
	}
	public void setMontoGastosComunes(double montoGastosComunes) {
		this.montoGastosComunes = montoGastosComunes;
	}
	public double getMontoFondoReserva() {
		return montoFondoReserva;
	}
	public void setMontoFondoReserva(double montoFondoReserva) {
		this.montoFondoReserva = montoFondoReserva;
	}
	public String getUltimaActualizacion() {
		return ultimaActualizacion;
	}
	public void setUltimaActualizacion(String ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}
	
	public MontoFijado() {
		super();
	}
	public MontoFijado(int idEdificio, String anio, double montoTotal, double montoGastosComunes,
			double montoFondoReserva, String ultimaActualizacion) {
		super();
		this.idEdificio = idEdificio;
		this.anio = anio;
		this.montoTotal = montoTotal;
		this.montoGastosComunes = montoGastosComunes;
		this.montoFondoReserva = montoFondoReserva;
		this.ultimaActualizacion = ultimaActualizacion;
	}
	
	public static MontoFijado obtenerMontoFijado(int idEdificio, String anio, Connection conn) throws SQLException {
		MontoFijado montoFijado = new MontoFijado();
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		try {
			sql = "SELECT * FROM montofijados WHERE idEdificio = ? AND anio = ? ORDER BY ultimaActualizacion DESC";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idEdificio);
			ps.setString(2, anio);
			res = ps.executeQuery();
			while(res.next()) {
				montoFijado.setIdEdificio(res.getInt("idEdificio"));
				montoFijado.setAnio(res.getString("anio"));
				montoFijado.setMontoFondoReserva(res.getDouble("montoFondoReserva"));
				montoFijado.setMontoGastosComunes(res.getDouble("montoGastosComunes"));
				montoFijado.setUltimaActualizacion(res.getString("ultimaActualizacion"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
		}
		return montoFijado;
	} 
	

}
