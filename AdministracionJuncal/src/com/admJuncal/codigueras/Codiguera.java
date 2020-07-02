package com.admJuncal.codigueras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.admJuncal.edificios.Proveedor;
import com.admJuncal.transacciones.TipoTransaccion;

public class Codiguera {

	private static ArrayList<TipoTransaccion> tipoTransacciones = null;
	private static ArrayList<Proveedor> proveedores = null;
	
	public static ArrayList<TipoTransaccion> obtenerTiposTransacciones(Connection conn) throws SQLException{
		if(tipoTransacciones == null) {
			tipoTransacciones = new ArrayList<TipoTransaccion>();
			PreparedStatement ps = null;
			ResultSet res = null;
			String sql = "";
			try {
				sql = "SELECT * FROM tipotransaccion";
				ps = conn.prepareStatement(sql);
				res = ps.executeQuery();
				while(res.next()) {
					TipoTransaccion tipo = new TipoTransaccion();
					tipo.setId(res.getInt("idTipoTransaccion"));
					tipo.setDescripcion(res.getString("nombreTipoTransaccion"));
					tipoTransacciones.add(tipo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(ps != null) ps.close();
				if(res != null) res.close();
			}
		}
		return tipoTransacciones;
	}
	
	public static ArrayList<Proveedor> obtenerProveedores(Connection conn) throws SQLException{
		if(proveedores == null) {
			proveedores = new ArrayList<Proveedor>();
			PreparedStatement ps = null;
			ResultSet res = null;
			String sql = "";
			try {
				sql = "SELECT * FROM proveedor";
				ps = conn.prepareStatement(sql);
				res = ps.executeQuery();
				while(res.next()) {
					Proveedor proveedor = new Proveedor();
					proveedor.setId(res.getInt("idProveedor"));
					proveedor.setNombre(res.getString("nombreProvedor"));
					proveedores.add(proveedor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(ps != null) ps.close();
				if(res != null) res.close();
			}
		}
		return proveedores;
	}
}
