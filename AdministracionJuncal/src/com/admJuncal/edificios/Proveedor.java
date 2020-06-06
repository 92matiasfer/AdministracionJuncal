package com.admJuncal.edificios;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.admJuncal.persistencia.BaseDatos;

public class Proveedor {

	private int id = 0;
	private String nombre = "";
	private String descripcion = "";
	private String rut = "";
	
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
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	
	
	public Proveedor() {
		super();
	}
	public Proveedor(int id, String nombre, String descripcion, String rut) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.rut = rut;
	}
	
	public static JSONArray obtenerProveedoresJSON(Connection conn) throws SQLException {
		JSONArray retorno = new JSONArray();
		ArrayList<Proveedor> proveedores = obtenerProveedores(conn);
		for(Proveedor p:proveedores) {
			retorno.add(p.toJSON());
		}
		return retorno;
	}
	
	
	public static ArrayList<Proveedor> obtenerProveedores(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet res = null;
		ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
		try {
			String sql = "SELECT * FROM proveedor";
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while(res.next()) {
				Proveedor p = new Proveedor();
				p.setId(res.getInt("idProveedor"));
				p.setNombre(res.getString("nombreProveedor"));
				p.setDescripcion(res.getString("descripcion"));
				p.setRut(res.getString("rut"));
				proveedores.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
		}
		return proveedores;
	}
	
	private Object toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("nombre", this.getNombre());
		json.put("descripcion", this.getDescripcion());
		json.put("rut", this.getRut());
		json.put("label", this.getNombre()); //Para select
		json.put("value", this.getId()); //Para select
		return json;
	}
	
	
}
