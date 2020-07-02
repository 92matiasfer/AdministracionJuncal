package com.admJuncal.servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.admJuncal.edificios.Edificio;
import com.admJuncal.edificios.MesLiquidacion;
import com.admJuncal.edificios.Proveedor;
import com.admJuncal.persistencia.BaseDatos;
import com.admJuncal.transacciones.Transaccion;
import com.admJuncal.usuarios.Usuario;
import com.admJuncal.utilidades.FechaUtils;

import org.json.simple.JSONArray;


/**
 * Servlet implementation class AppServicio
 */
@WebServlet("/AppServicio")
public class AppServicio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppServicio() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void seandBack(String texto, HttpServletResponse response) {
    	try {
			response.setContentType("aplication/json");
    		PrintWriter out = response.getWriter();
    		out.print(texto);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		String comando = request.getParameter("comando");
		try {
			if(comando.contains("iniciosistema")) { 
				inicioSistema(request, response);
			} else if(comando.contains("login")) {
				login(request, response);
			} else if(comando.contains("obteneredificio")) {
				obtenerEdificio(request, response);
			} else if(comando.contains("nuevatransaccion")) { 
				nuevaTransaccion(request, response);
			} else if(comando.contains("generarliquidacion")) {
				generarLiquidacion(request, response);
			} else if(comando.contains("obtenertransaccion")) {
				obtenerTransacciones(request, response);    //*****************
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void inicioSistema(HttpServletRequest request, HttpServletResponse response) {
		int status = 0;
		String mensaje = "";
		System.out.println("inicio");
		JSONObject json = new JSONObject();
		JSONArray edificiosJSON = new JSONArray();
		JSONArray proveedoresJSON = new JSONArray();
		JSONArray mesesLiquidacionJSON = new JSONArray();
		JSONArray transaccionesJSON = new JSONArray();
		try {
			edificiosJSON = obtenerEdificiosJSON();
			proveedoresJSON = obtenerProveedoresJSON();
			mesesLiquidacionJSON = obtenerMesesLiquidacion();
			transaccionesJSON = obtenerTransaccionesJSON();
		} catch (Exception e) {
			status = 200;
			mensaje = "Ha ocurrido un error al intentar iniciar el sistema";
			e.printStackTrace();
		}
		json.put("status", status);
		json.put("mensaje", mensaje);
		json.put("transacciones", transaccionesJSON);
		json.put("edificios", edificiosJSON);
		json.put("proveedores", proveedoresJSON);
		json.put("meses", mesesLiquidacionJSON);
		this.seandBack(json.toJSONString(), response);
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int status = 0;
		String mensaje = "";
		Connection conn = null;
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		JSONArray proveedoresJSON = new JSONArray();
		JSONArray edificiosJSON = new JSONArray();
		JSONObject retorno = new JSONObject();
		try {
			conn = BaseDatos.getConnection();
			boolean isLoged = Usuario.login(userName, password);
			if(!isLoged) {
				status = 400;
				mensaje = "usuario incorrecto";
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}
		retorno.put("status", status);
		retorno.put("mensaje", mensaje);
		this.seandBack(retorno.toJSONString(), response);
		
	}

	private JSONArray obtenerEdificiosJSON() {
		Connection conn = null;
		JSONArray retorno = new JSONArray();
		try {
			conn = BaseDatos.getConnection();
			retorno =  Edificio.obtenerEdificiosJSON(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	private JSONArray obtenerProveedoresJSON() {
		Connection conn = null;
		JSONArray retorno = new JSONArray();
		try {
			conn = BaseDatos.getConnection();
			retorno =  Proveedor.obtenerProveedoresJSON(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	private void obtenerEdificio(HttpServletRequest request, HttpServletResponse response) {
		int status = 0;
		int idEdificio = Integer.parseInt(request.getParameter("idEdificio"));
		String mensaje = "";
		String fechaActual = "";
		JSONObject retorno = new JSONObject();
		Edificio edificio = new Edificio();
		Connection conn = null;
		try {
			conn = BaseDatos.getConnection();
			edificio = Edificio.obtenerEdificio(idEdificio, conn);
			fechaActual = FechaUtils.otenerFechaActual();
		} catch (Exception e) {
			e.printStackTrace();
		}
		retorno.put("status", status);
		retorno.put("mensaje", mensaje);
		retorno.put("edificio", edificio.toJSON());
		retorno.put("fechaActual", fechaActual);
		this.seandBack(retorno.toJSONString(), response);
	}
	
	private JSONArray obtenerMesesLiquidacion() {
		Connection conn = null;
		JSONArray retorno = new JSONArray();
		try {
			conn = BaseDatos.getConnection();
			retorno =  MesLiquidacion.obtenerMesesLiquidacionJSON(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	

	private void nuevaTransaccion(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int status = 0;
		int idUnidad = 0;
		int idProveedor = 0;
		int tipoTransaccion = Integer.parseInt(request.getParameter("tipoTransaccion"));
		if(tipoTransaccion == 1) {
			idUnidad = Integer.parseInt(request.getParameter("unidad"));
		} else if(tipoTransaccion == 2) {
			idProveedor = Integer.parseInt(request.getParameter("proveedor"));
		}
		int idEdificio = Integer.parseInt(request.getParameter("edificio"));
		int mesLiquidacion = Integer.parseInt(request.getParameter("mesLiquidacion"));
		double monto = Double.parseDouble(request.getParameter("monto"));
		String nroFactura = request.getParameter("nroFactura");
		String observacion = request.getParameter("observacion");
		String fecha = request.getParameter("fecha");
		String mensaje = "";
		JSONObject retorno = new JSONObject();
		Connection conn = null;
		try {
			conn = BaseDatos.getConnection();
			Transaccion.guardar(idEdificio, idUnidad, idProveedor, monto, fecha, mesLiquidacion, nroFactura, observacion, tipoTransaccion, conn);
			mensaje = "Se ha registrado con exito la transaccion";
		} catch (Exception e) {
			status = 100;
			mensaje = "Ha ocurrido un error al intentar hacer la transaccion";
			e.printStackTrace();
		} finally {
			if(conn != null) conn.close();
		}
		retorno.put("status", status);
		retorno.put("mensaje", mensaje);
		this.seandBack(retorno.toJSONString(), response);
	}
	
	private void generarLiquidacion(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int status = 0;
		String mensaje = "";
		JSONObject retorno = new JSONObject();
		int mesLiquidacion = Integer.parseInt(request.getParameter("mesLiquidacion"));
		int idEdificio = Integer.parseInt(request.getParameter("edificio"));
		Edificio edificio = new Edificio();
		Connection conn = null;
		try {
			conn = BaseDatos.getConnection();
			edificio = Edificio.obtenerEdificio(idEdificio, conn);
			edificio.calcularLiquidacion(mesLiquidacion, "2020", conn);
			mensaje = "Se ha generado con exito la liquidacion";
		} catch (Exception e) {
			status = 100;
			mensaje = "Lo sentimos, ha ocurrido un error al intentar generar la liquidacion";
			e.printStackTrace();
		} finally {
			if(conn != null) conn.close();
		}
		retorno.put("status", status);
		retorno.put("mensaje", mensaje);
		this.seandBack(retorno.toJSONString(), response);
	}
	
	private void obtenerTransacciones(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int status = 0;
		String mensaje = "";
		JSONObject retorno = new JSONObject();
		int edificio = (request.getParameter("idEdificio") == null) ? 0 : Integer.parseInt(request.getParameter("idEdificio"));
		int tipoTransaccion = Integer.parseInt(request.getParameter("tipoTransaccion"));
		int proveedor = (request.getParameter("proveedor") == null) ? 0 : Integer.parseInt(request.getParameter("proveedor"));
		int unidad = (request.getParameter("unidad") == null) ? 0 : Integer.parseInt(request.getParameter("unidad"));
		String fechaInicio = (request.getParameter("fechaInicio") == null) ? "" :request.getParameter("fechaInicio");
		String fechaFin =  (request.getParameter("fechaFin") == null) ? "" :request.getParameter("fechaFin");
		JSONArray transaccionesJSON = new JSONArray();
		try {
			transaccionesJSON = obtenerTransaccionesJSON(edificio, unidad, tipoTransaccion, proveedor, fechaInicio, fechaFin);
		} catch (Exception e) {
			status = 100;
			mensaje = "Ha ocurrido un error al intentar hacer la transaccion";
			e.printStackTrace();
		}
		retorno.put("status", status);
		retorno.put("mensaje", mensaje);
		retorno.put("transacciones", transaccionesJSON);
		this.seandBack(retorno.toJSONString(), response);
		
	}
	
	private JSONArray obtenerTransaccionesJSON(int edificio, int unidad, int tipoTransaccion, int proveedor, String fechaInicio, String fechaFin) {
		Connection conn = null;
		JSONArray retorno = new JSONArray();
		try {
			conn = BaseDatos.getConnection();
			retorno =  Transaccion.obtenerTransaccionesJSON(edificio, unidad, tipoTransaccion, proveedor, fechaInicio, fechaFin, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	private JSONArray obtenerTransaccionesJSON() {
		Connection conn = null;
		JSONArray retorno = new JSONArray();
		try {
			conn = BaseDatos.getConnection();
			retorno =  Transaccion.obtenerTransaccionesJSON(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
}
