package com.admJuncal.transacciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaccion {
	
	private int idEdificio = 0;
	private int idUnidad = 0;
	private double monto = 0;
	private TipoTransaccion tipoTranssacion;
	private String fecha = "";
	private String proveedor = "";
	private String nroFactura = "";
	
	public int getIdEdificio() {
		return idEdificio;
	}
	public void setIdEdificio(int idEdificio) {
		this.idEdificio = idEdificio;
	}
	public int getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(int idUnidad) {
		this.idUnidad = idUnidad;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public TipoTransaccion getTipoTranssacion() {
		return tipoTranssacion;
	}
	public void setTipoTranssacion(TipoTransaccion tipoTranssacion) {
		this.tipoTranssacion = tipoTranssacion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getNroFactura() {
		return nroFactura;
	}
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}
	
	
	public Transaccion() {
		super();
	}
	public Transaccion(int idEdificio, int idUnidad, double monto, TipoTransaccion tipoTranssacion, String fecha,
			String proveedor, String nroFactura) {
		super();
		this.idEdificio = idEdificio;
		this.idUnidad = idUnidad;
		this.monto = monto;
		this.tipoTranssacion = tipoTranssacion;
		this.fecha = fecha;
		this.proveedor = proveedor;
		this.nroFactura = nroFactura;
	}
	public static void guardar(int idEdificio, int idUnidad, double monto, String fecha, Connection conn) throws SQLException {
		PreparedStatement ps = null;
		String sql = "";
		try {
			sql = "INSERT INTO transaccion(idEdificio, idUnidad, fecha, monto) VALUES(?, ?, ? ,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idEdificio);
			ps.setInt(2, idUnidad);
			ps.setString(3, fecha);
			ps.setDouble(4, monto);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
		}
	}
	
	
	
	
}
