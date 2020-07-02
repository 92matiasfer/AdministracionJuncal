package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private TipoPago tipoPagoGC = null;
	private TipoPago tipoPagoFR = null;
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
	public TipoPago getTipoPagoGC() {
		return tipoPagoGC;
	}
	public void setTipoPagoGC(TipoPago tipoPagoGC) {
		this.tipoPagoGC = tipoPagoGC;
	}
	public TipoPago getTipoPagoFR() {
		return tipoPagoFR;
	}
	public void setTipoPagoFR(TipoPago tipoPagoFR) {
		this.tipoPagoFR = tipoPagoFR;
	}
	
	public Edificio() {
		super();
	}
	public Edificio(int id, String nombre, String direccion, double montoAnualTotal, double recargo,
			TipoPago tipoPagoGC, TipoPago tipoPagoFR, ArrayList<Unidad> unidades,
			ArrayList<Notificacion> notificaciones) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.montoAnualTotal = montoAnualTotal;
		this.recargo = recargo;
		this.tipoPagoGC = tipoPagoGC;
		this.tipoPagoFR = tipoPagoFR;
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
				e.setTipoPagoFR(TipoPago.obtenerTipoPago(res.getInt("tipoPagoFR"), conn));
				e.setTipoPagoGC(TipoPago.obtenerTipoPago(res.getInt("tipoPagoGC"), conn));
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
	
	
	public void calcularLiquidacion(int mesLiquidacion, String anio, Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		double montoFR = 0;
		double montoGC = 0;
		double montoFRAnterior = 0;
		double montoGCAnterior = 0;
		ArrayList<Unidad> unidades = new ArrayList<Unidad>();
		try {
			conn.setAutoCommit(false);
			
//			Obtengo los saldos del mes anterior
			sql = "SELECT saldoAnteriorGC, saldoAnteriorFR FROM mesliquidacion WHERE idMesLiquidacion = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, mesLiquidacion);
			res = ps.executeQuery();
			while(res.next()) {
				montoFRAnterior = res.getDouble("saldoAnteriorFR");
				montoGCAnterior = res.getDouble("saldoAnteriorGC");
			}
			
			unidades = Unidad.obtenerUnidades(this.getId(), conn);
			if(this.getTipoPagoFR().getId() == 1) {
				sql = "SELECT montoFondoReserva FROM montosfijados WHERE idEdificio = ? AND anio = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, this.getId());
				ps.setString(2, anio);
				res = ps.executeQuery();
				while(res.next()) {
					montoFR = res.getDouble("montoFondoReserva");
				}
				for(Unidad u:unidades) {
					if( u.getHablitadoPago() == 1 && (u.getTipoUnidad().equalsIgnoreCase("P") || u.getTipoUnidad().equalsIgnoreCase("A"))) {
						sql = "SELECT * FROM unidadmesliquidacion WHERE idUnidad = ? AND idMesLiquidacion = ?";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, u.getId());
						ps.setInt(2, mesLiquidacion);
						res = ps.executeQuery();
						if(res.next()) {
							sql = "UPDATE unidadmesliquidacion SET montoFondoReserva = ? WHERE idUnidad = ? AND idMesLiquidacion = ?";
							ps = conn.prepareStatement(sql);
							double montoFRUnidad = montoFR * u.getCoeficiente();
							ps.setDouble(1, montoFRUnidad);
							ps.setInt(2, u.getId());
							ps.setInt(3, mesLiquidacion);
							ps.executeUpdate();
						} else {
							sql = "INSERT INTO unidadmesliquidacion(idUnidad, idMesLiquidacion, montoFondoReserva) VALUES (?, ?, ?)";
							ps = conn.prepareStatement(sql);
							ps.setInt(1, u.getId());
							ps.setInt(2, mesLiquidacion);
							double montoFRUnidad = montoFR * u.getCoeficiente();
							ps.setDouble(3, montoFRUnidad);
							ps.executeUpdate();
						}
					}
				}
			}
			if(this.getTipoPagoGC().getId() == 2) {
				sql = "SELECT SUM(monto) AS totalGC FROM transaccion WHERE idEdificio = ? AND idTipoTransaccion = ? AND idMesLiquidacion = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, this.getId());
				ps.setInt(2, 2);
				ps.setInt(3, mesLiquidacion);
				res = ps.executeQuery();
				while(res.next()) {
					montoGC = res.getDouble("totalGC");
				}
				for(Unidad u:unidades) {
					if(u.getHablitadoPago() == 1 && (u.getTipoUnidad().equalsIgnoreCase("I") || u.getTipoUnidad().equalsIgnoreCase("A"))) {
						sql = "SELECT * FROM unidadmesliquidacion WHERE idUnidad = ? AND idMesLiquidacion = ?";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, u.getId());
						ps.setInt(2, mesLiquidacion);
						res = ps.executeQuery();
						if(res.next()) {
							sql = "UPDATE unidadmesliquidacion SET montoGastosComunes = ? WHERE idUnidad = ? AND idMesLiquidacion = ?";
							ps = conn.prepareStatement(sql);
							double montoGCUnidad = montoGC * u.getCoeficiente();
							ps.setDouble(1, montoGCUnidad);
							ps.setInt(2, u.getId());
							ps.setInt(3, mesLiquidacion);
							ps.executeUpdate();
						} else {
							sql = "INSERT INTO unidadmesliquidacion (idUnidad, idMesLiquidacion, montoGastosComunes) VALUES (?, ?, ?)";
							ps = conn.prepareStatement(sql);
							ps.setInt(1, u.getId());
							ps.setInt(2, mesLiquidacion);
							double montoGCUnidad = montoGC * u.getCoeficiente();
							ps.setDouble(3, montoGCUnidad);
							ps.executeUpdate();
						}
					}
				}
			}
			
			conn.commit();
		} catch (Exception e) {
			if(conn != null) conn.rollback();
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
			conn.setAutoCommit(true);
		}
	}
	
	
	public Object toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("nombre", this.getNombre());
		json.put("direccion", this.getDireccion());
		json.put("unidades", this.getUnidadesJSON());
		json.put("label", this.getNombre());
		json.put("value", this.getId());
		return json;
	}
	
}
