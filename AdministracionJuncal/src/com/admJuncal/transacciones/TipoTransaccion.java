package com.admJuncal.transacciones;

import java.sql.Connection;
import java.sql.SQLException;

import com.admJuncal.codigueras.Codiguera;

public class TipoTransaccion {

	private int id = 0;
	private String descripcion = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public TipoTransaccion() {
		super();
	}
	public TipoTransaccion(int id, String descripcion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
	}
	
	public static TipoTransaccion obtenerTipoTransaccion(int id, Connection conn) throws SQLException {
		TipoTransaccion  tipo = new TipoTransaccion();
		for(TipoTransaccion tp:Codiguera.obtenerTiposTransacciones(conn)) {
			if(tp.getId() == id) {
				tipo = tp;
				break;
			}
		}
		return tipo;
	}
	
		
}
