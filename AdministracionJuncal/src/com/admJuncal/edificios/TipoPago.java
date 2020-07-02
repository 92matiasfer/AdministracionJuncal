package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoPago {
	
	private int id = 0;
	private String nombre = "";
	private String descripcion = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	public TipoPago() {
		super();
	}
	public TipoPago(int id, String nombre, String descripcion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	
	public static TipoPago obtenerTipoPago(int id, Connection conn) throws SQLException {
		TipoPago tipoPago = new TipoPago();
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		try {
			sql = "SELECT * FROM tipopago WHERE idTipoPago = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			res = ps.executeQuery();
			while(res.next()) {
				tipoPago.setId(res.getInt("idTipoPago"));
				tipoPago.setNombre(res.getString("nombre"));
				tipoPago.setDescripcion(res.getString("descripcion"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
		}
		return tipoPago;
	}
	

}
