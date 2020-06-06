package com.admJuncal.persistencia;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class BaseDatos {

	private static final String url = "jdbc:mysql://localhost:3306/AdministracionJuncal";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "root";
	private static final String password = "root";
	
	/**
	 * Retorna la conexion a la base de datos.
	 * ¡¡ IMPORTANTE !! Cerrar conexion cuando no se use mas.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection() throws ClassNotFoundException {
		Connection con = null;
		
		try {
			// Carga la clase del driver
	        Class.forName(driver);
	        
	     // Iniciamos conexion
	        con = (Connection) DriverManager.getConnection(url, userName, password);
	        System.out.println("Conexion existosa");
		} catch (Exception e) {
			System.out.println("Error al conectar");
			e.printStackTrace();;
		}
		return con;
	}
	
}
