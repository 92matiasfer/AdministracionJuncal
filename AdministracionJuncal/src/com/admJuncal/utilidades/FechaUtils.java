package com.admJuncal.utilidades;

import java.util.Date;

public class FechaUtils {

	public static String otenerFechaActual() {
		Date fecha = new Date();
		String retorno = "";
		retorno = fecha.getYear() + "-" + (fecha.getMonth() + 1) + "-" + fecha.getDate();
		return retorno;
	}

}
