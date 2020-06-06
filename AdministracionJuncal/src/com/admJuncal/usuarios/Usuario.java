package com.admJuncal.usuarios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.admJuncal.persistencia.BaseDatos;
import com.mysql.jdbc.Connection;

public class Usuario {

	private String nombre = "";
	private String password = "";
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Usuario() {
		super();
	}
	public Usuario(String nombre, String password) {
		super();
		this.nombre = nombre;
		this.password = password;
	}
	
	public static boolean login(String nombre, String password) throws SQLException {
		boolean ret = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try {
			con = BaseDatos.getConnection();
			String sql = "SELECT * FROM usuario WHERE usuario = ? AND password = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, nombre);
			ps.setString(2, password);
			res = ps.executeQuery();
			if(res.next()) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) ps.close();
			if(res != null) res.close();
			if(con != null) con.close();
		}
		return ret;
	}
}
