package com.admJuncal.transacciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.admJuncal.edificios.Edificio;
import com.admJuncal.edificios.MesLiquidacion;
import com.admJuncal.edificios.Proveedor;
import com.admJuncal.edificios.Unidad;

public class Transaccion {
	
	private Edificio edificio = null;
	private Unidad unidad = null;
	private double monto = 0;
	private TipoTransaccion tipoTranssacion = null;
	private MesLiquidacion mesLiquidacion = null;
	private String fecha = "";
	private Proveedor proveedor = null;
	private String nroFactura = "";
	private String observaciones = "";
	
	public Edificio getEdificio() {
		return edificio;
	}
	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}
	public Unidad getUnidad() {
		return unidad;
	}
	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
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
	public MesLiquidacion getMesLiquidacion() {
		return mesLiquidacion;
	}
	public void setMesLiquidacion(MesLiquidacion mesLiquidacion) {
		this.mesLiquidacion = mesLiquidacion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	public String getNroFactura() {
		return nroFactura;
	}
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public Transaccion() {
		super();
	}
	public Transaccion(Edificio edificio, Unidad unidad, double monto, TipoTransaccion tipoTranssacion,
			MesLiquidacion mesLiquidacion, String fecha, Proveedor proveedor, String nroFactura, String observaciones) {
		super();
		this.edificio = edificio;
		this.unidad = unidad;
		this.monto = monto;
		this.tipoTranssacion = tipoTranssacion;
		this.mesLiquidacion = mesLiquidacion;
		this.fecha = fecha;
		this.proveedor = proveedor;
		this.nroFactura = nroFactura;
		this.observaciones = observaciones;
	}
	
	public static void guardar(int idEdificio, int idUnidad, int idProveedor, double monto, String fecha, int mesLiquidacion, String nroFactura, String observacion, int tipoTransaccion, Connection conn) throws SQLException {
		PreparedStatement ps = null;
		String sql = "";
		try {
			//Pagos hechos por copropietarios
			if(tipoTransaccion == 1) {
				sql = "INSERT INTO transaccion(idEdificio, idUnidad, fecha, monto, idMesLiquidacion, nroFactura, observacion, idTipoTransaccion) VALUES(?, ?, ? ,?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, idEdificio);
				ps.setInt(2, idUnidad);
				ps.setString(3, fecha);
				ps.setDouble(4, monto);
				ps.setInt(5, mesLiquidacion);
				ps.setString(6, nroFactura);
				ps.setString(7, observacion);
				ps.setInt(8, tipoTransaccion);
				ps.executeUpdate();
//			Pagos realizados a proveedores
			} else if(tipoTransaccion == 2) {
				sql = "INSERT INTO transaccion(idEdificio, fecha, monto, idProveedor, idMesLiquidacion, nroFactura, observacion, idTipoTransaccion) VALUES(?, ?, ? ,?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, idEdificio);
				ps.setString(2, fecha);
				ps.setDouble(3, monto);
				ps.setInt(4, idProveedor);
				ps.setInt(5, mesLiquidacion);
				ps.setString(6, nroFactura);
				ps.setString(7, observacion);
				ps.setInt(8, tipoTransaccion);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
		}
	}
	
	private static ArrayList<Transaccion> obtenerTransacciones(Connection conn){
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();
		try {
			sql = "SELECT * FROM transaccion";
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while(res.next()) {
				Transaccion t = new Transaccion();
				t.setEdificio(Edificio.obtenerEdificio(res.getInt("idEdificio"), conn));
				int unidad = res.getInt("idUnidad");
				if(unidad > 0) {
					t.setUnidad(Unidad.obtenerUnidad(unidad, conn));
				}
				t.setMonto(res.getDouble("monto"));
				t.setObservaciones(res.getString("observacion"));
				t.setFecha(res.getString("fecha"));
				t.setNroFactura(res.getString("nroFactura"));
				t.setTipoTranssacion(TipoTransaccion.obtenerTipoTransaccion(res.getInt("idTipoTransaccion"), conn));
				transacciones.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transacciones;
	}
	
	
	public static JSONArray obtenerTransaccionesJSON(Connection conn) {
		JSONArray transaccionesJSON = new JSONArray();
		ArrayList<Transaccion> transacciones = obtenerTransacciones(conn);
		for(Transaccion t: transacciones) {
			transaccionesJSON.add(t.toJSON());
		}
		return transaccionesJSON;
	}
	
	public static JSONArray obtenerTransaccionesJSON(int edificio,  int unidad,int tipoTransaccion, int proveedor, String fechaInicio, String fechaFin, Connection conn) {
		JSONArray transaccionesJSON = new JSONArray();
		ArrayList<Transaccion> transacciones = obtenerTransacciones(edificio, unidad, tipoTransaccion, proveedor,fechaInicio, fechaFin, conn);
		for(Transaccion t: transacciones) {
			transaccionesJSON.add(t.toJSON());
		}
		return transaccionesJSON;
	}
	
	private static ArrayList<Transaccion> obtenerTransacciones(int idEdificio, int unidad, int tipoTransaccion, int idProveedor, String fechaInicio, String fechaFin, Connection conn){
		PreparedStatement ps = null;
		ResultSet res = null;
		String sql = "";
		int contador = 1; //Se inicializa en la cantidad minima de clausuras que se agregaralan en la consulta.
		ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();
		try {
			sql = "SELECT * FROM transaccion WHERE idtipotransaccion = ?";
			if(idEdificio > 0 || idProveedor > 0 || unidad > 0 || fechaInicio.length() > 0 || fechaFin.length() > 0) {
				if(idEdificio > 0) {
					sql += " AND idEdificio = ? ";
				}
				if(idProveedor > 0 ){
					sql += " AND idProveedor = ? ";
				}
				if(unidad > 0 ){
					sql += " AND idUnidad = ? ";
				}
				if(fechaInicio.length() > 0){
					sql += " AND fecha >= ?";
				}
				if(fechaFin.length() > 0){
					sql += " AND fecha <= ?";
				}
			}
			ps = conn.prepareStatement(sql);
			ps.setInt(contador, tipoTransaccion);
			contador++;
			if(idEdificio > 0){
				ps.setInt(contador, idEdificio);
				contador++;
			}
			if(idProveedor > 0){
				ps.setInt(contador, idProveedor);
				contador++;
			}
			if(unidad > 0){
				ps.setInt(contador, unidad);
				contador++;
			}
			if(fechaInicio.length() > 0){
				ps.setString(contador, fechaInicio);
				contador++;
			}
			if(fechaFin.length() > 0){
				ps.setString(contador, fechaFin);
			}
			res = ps.executeQuery();
			while(res.next()) {
				Transaccion t = new Transaccion();
				t.setEdificio(Edificio.obtenerEdificio(res.getInt("idEdificio"), conn));
				int unidadAux = res.getInt("idUnidad");
				if(unidadAux > 0) {
					t.setUnidad(Unidad.obtenerUnidad(unidadAux, conn));
				}
				int provAux = res.getInt("idProveedor");
				if(provAux > 0) {
					t.setProveedor(Proveedor.obtenerProveedor(provAux, conn));
				} 
				t.setMonto(res.getDouble("monto"));
				t.setObservaciones(res.getString("observacion"));
				t.setFecha(res.getString("fecha"));
				t.setNroFactura(res.getString("nroFactura"));
				t.setTipoTranssacion(TipoTransaccion.obtenerTipoTransaccion(res.getInt("idTipoTransaccion"), conn));
				transacciones.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transacciones;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("edificio", this.getEdificio().toJSON());
		if(this.getUnidad() != null) {
			json.put("idUnidad", this.getUnidad().toJSON());
		}
		if(this.getProveedor() != null) {
			json.put("proveedor", this.getProveedor().toJSON());
		}
		json.put("fecha", this.getFecha());
		json.put("monto", this.getMonto());
		json.put("nroFactura", this.getNroFactura());
		json.put("tipoTrnasaccion", this.getTipoTranssacion().getDescripcion());
		return json;
	}
	
	
}
