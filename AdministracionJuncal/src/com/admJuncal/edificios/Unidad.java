package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.admJuncal.usuarios.Copropietario;

public class Unidad {

	private int id = 0;
	private int nroApartamento = 0;
	private int idEdificio = 0;
	private String tipoUnidad = "";
	private Copropietario copropietario = null;
	private double saldoAnterior = 0;
	private double coeficiente = 0;
	private boolean pagaCochera = false;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNroApartamento() {
		return nroApartamento;
	}
	public void setNroApartamento(int nroApartamento) {
		this.nroApartamento = nroApartamento;
	}
	public Copropietario getCopropietario() {
		return copropietario;
	}
	public void setCopropietario(Copropietario copropietario) {
		this.copropietario = copropietario;
	}
	public double getSaldoAnterior() {
		return saldoAnterior;
	}
	public void setSaldoAnterior(double saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	public double getCoeficiente() {
		return coeficiente;
	}
	public void setCoeficiente(double coeficiente) {
		this.coeficiente = coeficiente;
	}
	public boolean isPagaCochera() {
		return pagaCochera;
	}
	public void setPagaCochera(boolean pagaCochera) {
		this.pagaCochera = pagaCochera;
	}
	public int getIdEdificio() {
		return idEdificio;
	}
	public void setIdEdificio(int idEdificio) {
		this.idEdificio = idEdificio;
	}
	public String getTipoUnidad() {
		return tipoUnidad;
	}
	public void setTipoUnidad(String tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}
	
	public Unidad() {
		super();
	}	
	public Unidad(int id, int nroApartamento, int idEdificio, String tipoUnidad, Copropietario copropietario,
			double saldoAnterior, double coeficiente, boolean pagaCochera) {
		super();
		this.id = id;
		this.nroApartamento = nroApartamento;
		this.idEdificio = idEdificio;
		this.tipoUnidad = tipoUnidad;
		this.copropietario = copropietario;
		this.saldoAnterior = saldoAnterior;
		this.coeficiente = coeficiente;
		this.pagaCochera = pagaCochera;
	}
	
	public static ArrayList<Unidad> obtenerUnidades(int idEdificio, Connection conn){
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		ArrayList<Unidad> unidades = new ArrayList<Unidad>();
		try {
			sql = "SELECT * FROM unidad where idEdificio = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idEdificio);
			res = ps.executeQuery();
			while(res.next()) {
				Unidad u = new Unidad();
				u.setId(res.getInt("idUnidad"));
				u.setNroApartamento(res.getInt("apartamento"));
				u.setIdEdificio(res.getInt("idEdificio"));
				u.setTipoUnidad(res.getString("tipoUnidad"));
				unidades.add(u);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unidades;
	}
	
	public static JSONArray obtenerUnidadesJSON(int idEdificio, Connection conn) {
		JSONArray unidadesJSON = new JSONArray();
		ArrayList<Unidad> unidades = obtenerUnidades(idEdificio, conn);
		for(Unidad u:unidades) {
			unidadesJSON.add(u.toJSON());
		}
		return unidadesJSON;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("nroApartamento", this.getNroApartamento());
		json.put("edificio", this.getIdEdificio());
		json.put("tipoUnidad", this.getTipoUnidad());
		json.put("label", this.getNroApartamento() + this.getTipoUnidad()); //Para select de vue
		json.put("value", this.getId()); //Para select de vue
		return json;
	}
	
	
}
