package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.admJuncal.notificaciones.Notificacion;


public class Edificio {

	private int id = 0;
	private String nombre = "";
	private String direccion = "";
	private double montoAnualTotal = 0;
	private double recargo = 0;
	private TipoMonto tipoMonto;
	private ArrayList<Unidad> unidades = new ArrayList<Unidad>();
	private ArrayList<Notificacion> notificaciones = new ArrayList<Notificacion>();
	
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
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public double getMontoAnualTotal() {
		return montoAnualTotal;
	}
	public void setMontoAnualTotal(double montoAnualTotal) {
		this.montoAnualTotal = montoAnualTotal;
	}
	public double getRecargo() {
		return recargo;
	}
	public void setRecargo(double recargo) {
		this.recargo = recargo;
	}
	public TipoMonto getTipoMonto() {
		return tipoMonto;
	}
	public void setTipoMonto(TipoMonto tipoMonto) {
		this.tipoMonto = tipoMonto;
	}
	public ArrayList<Unidad> getUnidades() {
		return unidades;
	}
	public void setUnidades(ArrayList<Unidad> unidades) {
		this.unidades = unidades;
	}
	public ArrayList<Notificacion> getNotificaciones() {
		return notificaciones;
	}
	public void setNotificaciones(ArrayList<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}
	
	public Edificio() {
		super();
	}
	public Edificio(int id, String nombre, String direccion, double montoAnualTotal, double recargo, TipoMonto tipoMonto,
			ArrayList<Unidad> unidades, ArrayList<Notificacion> notificaciones) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.montoAnualTotal = montoAnualTotal;
		this.recargo = recargo;
		this.tipoMonto = tipoMonto;
		this.unidades = unidades;
		this.notificaciones = notificaciones;
	}
	
	public static JSONArray obtenerEdificiosJSON(Connection conn) {
		JSONArray edificiosJSON = new JSONArray();
		ArrayList<Edificio> edificios = obtenerEdificios(conn);
		for(Edificio e:edificios) {
			edificiosJSON.add(e.toJSON());
		}
		return edificiosJSON;
	}
	

	private static ArrayList<Edificio> obtenerEdificios(Connection conn){
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		ArrayList<Edificio> edificios = new ArrayList<Edificio>();
		try {
			sql = "SELECT * FROM edificio ORDER BY nombreEdificio";
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while(res.next()) {
				Edificio e = new Edificio();
				e.setId(res.getInt("idEdificio"));
				e.setNombre(res.getString("nombreEdificio"));
				e.setDireccion(res.getString("direccionEdificio"));
				edificios.add(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return edificios;
	}
	
	public Object toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("nombre", this.getNombre());
		json.put("direccion", this.getDireccion());
		json.put("unidades", this.getUnidadesJSON());
		return json;
	}
	
	public static Edificio obtenerEdificio(int idEdificio, Connection conn) {
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		Edificio e = new Edificio();
		try {
			sql = "SELECT * FROM edificio where idEdificio = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idEdificio);
			res = ps.executeQuery();
			while(res.next()) {
				e.setId(res.getInt("idEdificio"));
				e.setNombre(res.getString("nombreEdificio"));
				e.setDireccion(res.getString("direccionEdificio"));
				e.setUnidades(Unidad.obtenerUnidades(idEdificio, conn));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return e;
	}
	
	private JSONArray getUnidadesJSON() {
		JSONArray unidadesJSON = new JSONArray();
		for(Unidad u: this.getUnidades()) {
			unidadesJSON.add(u.toJSON());
		}
		return unidadesJSON;
	}
	
}
