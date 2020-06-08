package com.admJuncal.edificios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MesLiquidacion {

	private int id = 0;
	private String mes = "";
	private String año = "";
	private double total = 0;
	private String fechaPago = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAño() {
		return año;
	}
	public void setAño(String año) {
		this.año = año;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	
	
	public MesLiquidacion() {
		super();
	}
	public MesLiquidacion(int id, String mes, String año, double total, String fechaPago) {
		super();
		this.id = id;
		this.mes = mes;
		this.año = año;
		this.total = total;
		this.fechaPago = fechaPago;
	}
	
	public static ArrayList<MesLiquidacion> obtenerMesesLiquidacion(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet res = null;
		ArrayList<MesLiquidacion> meses = new ArrayList<MesLiquidacion>();
		try {
			String sql = "SELECT * FROM mesLiquidacion";
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while(res.next()) {
				MesLiquidacion ml = new MesLiquidacion();
				ml.setId(res.getInt("idMesLiquidacion"));
				ml.setMes(res.getString("mes"));
				ml.setAño(res.getString("año"));
				ml.setFechaPago(res.getString("fechaPago"));
				meses.add(ml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
		}
		return meses;
	}
	
	public static JSONArray obtenerMesesLiquidacionJSON(Connection conn) throws SQLException {
		JSONArray retorno = new JSONArray();
		ArrayList<MesLiquidacion> meses = obtenerMesesLiquidacion(conn);
		for(MesLiquidacion ml:meses) {
			retorno.add(ml.toJSON());
		}
		return retorno;
	}
	private Object toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("mes", this.getMes());
		json.put("año", this.getAño());
		json.put("fechaPago", this.getFechaPago());
		json.put("label", this.getMes() + ' ' + this.getAño());
		json.put("value", this.getId());
		return json;
	}
}
