package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.admJuncal.usuarios.Copropietario;

public class Unidad {

	private int id = 0;
	private String nroApartamento = "";
	private int idEdificio = 0;
	private String tipoUnidad = "";
	private Copropietario copropietario = null;
	private double saldoAnterior = 0;
	private double coeficiente = 0;
	private boolean pagaCochera = false;
	private int hablitadoPago = 0;
	private double montoGC = 0;
	private double montoFR = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNroApartamento() {
		return nroApartamento;
	}
	public void setNroApartamento(String nroApartamento) {
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
	public int getHablitadoPago() {
		return hablitadoPago;
	}
	public void setHablitadoPago(int hablitadoPago) {
		this.hablitadoPago = hablitadoPago;
	}
	public double getMontoGC() {
		return montoGC;
	}
	public void setMontoGC(double montoGC) {
		this.montoGC = montoGC;
	}
	public double getMontoFR() {
		return montoFR;
	}
	public void setMontoFR(double montoFR) {
		this.montoFR = montoFR;
	}
	
	public Unidad() {
		super();
	}	
	public Unidad(int id, String nroApartamento, int idEdificio, String tipoUnidad, Copropietario copropietario,
			double saldoAnterior, double coeficiente, boolean pagaCochera, int hablitadoPago, double montoGC,
			double montoFR) {
		super();
		this.id = id;
		this.nroApartamento = nroApartamento;
		this.idEdificio = idEdificio;
		this.tipoUnidad = tipoUnidad;
		this.copropietario = copropietario;
		this.saldoAnterior = saldoAnterior;
		this.coeficiente = coeficiente;
		this.pagaCochera = pagaCochera;
		this.hablitadoPago = hablitadoPago;
		this.montoGC = montoGC;
		this.montoFR = montoFR;
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
				u.setNroApartamento(res.getString("apartamento"));
				u.setIdEdificio(res.getInt("idEdificio"));
				u.setTipoUnidad(res.getString("tipoUnidad"));
				u.setCoeficiente(res.getDouble("coeficiente"));
				u.setHablitadoPago(res.getInt("habilitadoPago"));
				u.cargarMontosAPagar(conn);
				unidades.add(u);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unidades;
	}
	
	private void cargarMontosAPagar(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		try {
			sql = "SELECT montoFondoReserva, montoGastosComunes FROM unidadmesliquidacion WHERE idUnidad = ? AND idMesLiquidacion = (SELECT MAX(idMesLiquidacion) FROM unidadmesliquidacion WHERE idUnidad = ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, this.getId());
			ps.setInt(2, this.getId());
			res = ps.executeQuery();
			while(res.next()) {
				this.setMontoFR(res.getDouble("montoFondoReserva"));
				this.setMontoGC(res.getDouble("montoGastosComunes"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
		}
		
	}
	public static JSONArray obtenerUnidadesJSON(int idEdificio, Connection conn) {
		JSONArray unidadesJSON = new JSONArray();
		ArrayList<Unidad> unidades = obtenerUnidades(idEdificio, conn);
		for(Unidad u:unidades) {
			unidadesJSON.add(u.toJSON());
		}
		return unidadesJSON;
	}
	
	public static Unidad obtenerUnidad(int id, Connection conn) throws SQLException {
		Unidad unidad = new Unidad();
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		try {
			sql = "SELECT * FROM unidad WHERE idUnidad = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			res = ps.executeQuery();
			while(res.next()) {
				unidad.setId(res.getInt("idUnidad"));
				unidad.setNroApartamento(res.getString("apartamento") + res.getString("tipoUnidad"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
		}
		return unidad;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("nroApartamento", this.getNroApartamento());
		json.put("edificio", this.getIdEdificio());
		json.put("tipoUnidad", this.getTipoUnidad());
		json.put("coeficiente", this.getCoeficiente());
		json.put("montoGC", this.getMontoGC());
		json.put("montoFR", this.getMontoFR());
		json.put("label", this.getNroApartamento() + " " + this.getTipoUnidad()); //Para select de vue
		json.put("value", this.getId()); //Para select de vue
		return json;
	}
	
	
	
}
