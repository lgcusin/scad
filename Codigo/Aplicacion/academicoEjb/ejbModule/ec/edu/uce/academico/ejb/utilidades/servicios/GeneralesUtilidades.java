/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     GeneralesUtilidades.java      
 DESCRIPCION: Clase encargada de realizar operaciones generalizadas para el sistema.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017            Roberto Cadena                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.servicios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;

/**
 * Clase GeneralesUtilidades.
 * Clase encargada de realizar operaciones generalizadas para el sistema.
 * @author rcadena
 * @version 1.0
 */
public class GeneralesUtilidades {
	
	/**
	 * Método que me permite eliminar espacios, tabuladores y retornos de una cadena.
	 * @param cadena - texto que vamos a quitar espacios.
	 * @return cadena sin espacios.
	 */
	public static String quitarEspaciosEnBlanco(String cadena) {
	return cadena!= null ? cadena.replaceAll("^\\s*","").replaceAll("\\s*$","").replaceAll("\\s", "").toUpperCase() : " " ;
	}
	
	/**
	 * Transforma un decimal como string que contenga una ","
	 * a un decimal como string que contenga ".".
	 * @param st string a transformar
	 * @return El nuevo string ya tranformado.
	 */
	public static String transforma(String st){
		StringBuffer stb = new StringBuffer("");
		for(int i=0; i<st.length();i++){
			if(st.charAt(i)==','){
				stb.append('.');
			}else{
				stb.append(st.charAt(i));
			}
		}
		return stb.toString();
	}
	
	/**
	 * Valida el identificador ya sea RUC o CI.
	 * @param numero string que representa al identificador a ser verificado.
	 * @return true, si la validacion es correcta, caso contrario retorna false.
	 */
	public static boolean validarDocumento(String numero) {
		int[] digitos = new int[10];
		int sumaPar = 0;
		int sumaImpar = 0;
		int digitoVerificador;
		try {
			for (int i = 0; i < digitos.length; i++) {
				digitos[i] = Integer.parseInt("" + numero.charAt(i));
			}
			// Los primeros dos digitos corresponden al codigo de la provincia
			if ((Integer.parseInt("" + digitos[0] + "" + digitos[1])) <= 0
					|| (Integer.parseInt("" + digitos[0] + "" + digitos[1])) > 24) {
				return false;
			}
			// El tercer digito es:
			// 9 para sociedades privadas y extranjeros
			// 6 para sociedades publicas
			// menor que 6 (0,1,2,3,4,5) para personas naturales
			if (digitos[2] == 7 || digitos[2] == 8) {
				return false;
			}
			// Solo para personas naturales (modulo 10)
			if (digitos[2] < 6) {
				for (int i = 0; i < digitos.length - 1; i++) {
					if (((i + 1) % 2) == 0) {// par
						sumaPar += digitos[i];
					} else {// impar
						sumaImpar += digitos[i] * 2 > 9 ? (digitos[i] * 2) - 9 : digitos[i] * 2;
					}
				}
				digitoVerificador = (sumaPar + sumaImpar) % 10;
				digitoVerificador = digitoVerificador == 0 ? 0
						: 10 - digitoVerificador;
				if (digitoVerificador == digitos[9]) {
					// El ruc de las empresas del sector publico terminan con
					// 0001, 0002, etc
					if (numero.length() > 10) {
						if ((numero.substring(10, numero.length() - 1).equals("00"))
								&& (Integer.parseInt("" + numero.charAt(12)) >= 1)) {
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
				} else {
					return false;
				}
			}

			// Solo para sociedades publicas (modulo 11)
			// Aqui el digito verficador esta en la posicion 9, en las otras 2
			// en la pos. 10
			if (digitos[2] == 6) {
				digitoVerificador = digitos[0] * 3 + digitos[1] * 2
						+ digitos[2] * 7 + digitos[3] * 6 + digitos[4] * 5
						+ digitos[5] * 4 + digitos[6] * 3 + digitos[7] * 2;
				digitoVerificador = digitoVerificador % 11;
				digitoVerificador = digitoVerificador == 0 ? 0
						: 11 - digitoVerificador;
				if (digitoVerificador == digitos[8]) {
					// El ruc de las empresas del sector publico terminan con
					// 0001, 0002, etc
					if ((numero.substring(9, numero.length() - 1).equals("000"))
							&& (Integer.parseInt("" + numero.charAt(12)) >= 1)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}

			/* Solo para entidades privadas (modulo 11) */
			if (digitos[2] == 9) {
				digitoVerificador = digitos[0] * 4 + digitos[1] * 3
						+ digitos[2] * 2 + digitos[3] * 7 + digitos[4] * 6
						+ digitos[5] * 5 + digitos[6] * 4 + digitos[7] * 3
						+ digitos[8] * 2;
				digitoVerificador = digitoVerificador % 11;
				digitoVerificador = digitoVerificador == 0 ? 0
						: 11 - digitoVerificador;
				if (digitoVerificador == digitos[9]) {
					// El ruc de las empresas del sector publico terminan con
					// 0001, 0002, etc
					if ((numero.substring(10, numero.length() - 1).equals("00"))
							&& (Integer.parseInt("" + numero.charAt(12)) >= 1)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	
	
	/**
	 * Completa la cadena recibida con el caracter comodin a la izq de la cadena original.
	 * @param cadena, string a ser completado
	 * @param numDigitos, numero de digitos que deberia tener el string al finalizar la tarea
	 * @param comodin, string con el que se completara la cadena original
	 * @return El string completado.
	 */
	public static String completarCadenaIzq(String cadena, int numDigitos, String comodin){
		int digFaltante;
		StringBuffer stb = new StringBuffer();
		if(cadena.length() < numDigitos){
			digFaltante = numDigitos - cadena.length();
			for(int i=0; i<digFaltante ; i++){
				stb.append(comodin);
			}
		}
		stb.append(cadena);
		return stb.toString();
	}
	
	/**
	 * Completa la cadena recibida con el caracter comodin a la der de la cadena original.
	 * @param cadena, string a ser completado
	 * @param numDigitos, numero de digitos que deberia tener el string al finalizar la tarea
	 * @param comodin, string con el que se completara la cadena original
	 * @return El string completado.
	 */
	public static String completarCadenaDer(String cadena, int numDigitos, String comodin){
		int digFaltante;
		StringBuffer stb = new StringBuffer();
		stb.append(cadena);
		if(cadena.length() < numDigitos){
			digFaltante = numDigitos - cadena.length();
			for(int i=0; i<digFaltante ; i++){
				stb.append(comodin);
			}
		}
		
		return stb.toString();
	}
	
	/**
	 * Delimita la cadena recibida a numero de digitos y finaliza con el separador Final indicado.
	 * @param cadena, string a ser delimitado
	 * @param numDigitos, numero de digitos que debe tener el string al finalizar la tarea
	 * @param separadorFinal, string con el que se completara la cadena original
	 * @return El string delimitado.
	 */
	public static String delimitarCadena(String cadena, int numDigitos, String separadorFinal) {
		if(separadorFinal == null){
			separadorFinal = "";
		}
		if(cadena!=null){
			if(cadena.length() < numDigitos){
				return cadena;
			}else{
				cadena = cadena.substring(0, (numDigitos-separadorFinal.length()));
				return cadena+separadorFinal;
			}
		}else{
			return cadena;
		}
	}

	/**
	 * Verifica la edad minima de una persona (configurada en la constante)
	 * @param fechaNacimiento - fecha de nacimeinto 
	 * @return true- si la edad es correcta, false, de lo contrario
	 */
	public static boolean verificarEdad(Date fechaNacimiento){
		final long EDAD_MINIMA = 17;
	    return   calcularAnios(fechaNacimiento)  < EDAD_MINIMA? false: true;
	}
	
	/**
	 * Cualcual cuantos anios tiene a partir de la fecha de nacimiento
	 * @param fechaNacimiento - fecha de nacimeinto 
	 * @return numero de años
	 */
	public static int calcularAnios(Date fechaNacimiento){
		Calendar fechaNacimientoCl = Calendar. getInstance();
		fechaNacimientoCl.setTime(fechaNacimiento);
		Calendar fechaActual = Calendar.getInstance();
		Date fechActual = new Date();
		fechaActual.setTime(fechActual);
		int anio = fechaActual.get(Calendar.YEAR) - fechaNacimientoCl.get(Calendar.YEAR);
		int mes = fechaActual.get(Calendar.MONTH) - fechaNacimientoCl.get(Calendar.MONTH);
		int dia = fechaActual.get(Calendar.DATE) - fechaNacimientoCl.get(Calendar.DATE);
		if (mes < 0 || (mes == 0 && dia < 0)) {
			anio--;
		}

		return anio;
	}
	
	/**
	 * Transformar String a Util.Date dependiendo del formato
	 * @param fecha - fecha como cadena
	 * @param formato - formato de fecha
	 * @return fecha como objeto Util.Date
	 * @throws ParseException - excepcion en la transformacion
	 */
	public static Date fechaCadenaToDate(String fecha, String formatoFecha) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		sdf.setLenient(false);
        return sdf.parse(fecha);
	}
	
	/**
	 * Transformar String sin separador a Util.Date dependiendo del formato de la fecha inicial
	 * @param fecha - fecha como cadena sin separador / o - y en el orden del formato ejemplo 20131231 para el formato yyyy-MM-dd
	 * @param formato - formato de fecha ejemplo yyyy-MM-dd
	 * @param separador - separador del formato ejemplo -
	 * @return fecha como objeto Util.Date
	 */
	public static Date fechaCadenaSinSepToDate(String fecha, String formatoFecha, String separador) throws ParseException{
		int contador = 0;
		fecha = fecha.toString().replace("-", "");
		fecha = fecha.toString().replace("/", "").trim();
		String formatoSplit[] = formatoFecha.split(separador);
		StringBuffer sb = new StringBuffer("");
		for (String item : formatoSplit) {
			sb.append(fecha.substring(contador, (contador + item.length())));
			sb.append(separador);
			contador = contador + item.length();
		}
		fecha = sb.toString();
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		sdf.setLenient(false);
        return sdf.parse(fecha);
	}
	
	/**
	 * Retorna la fecha del primer dia del mes en curso(de la fecha del servidor)
	 * @return fecha del primer dia del mes en curso
	 */
	public static Date getPrimerDiaDelMesActual() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMinimum(Calendar.DAY_OF_MONTH),
				cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE),
				cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * Retorna la fecha del ultimo dia del mes en curso(de la fecha del servidor)
	 * @return fecha del ultimo dia del mes en curso
	 */
	public static Date getUltimoDiaDelMesActual() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}
	
	/**
	 * Retorna la fecha del primer dia del mes de la fecha del parametro
	 * @param fecha - fecha de la que se calculara el primer dia de su mes.
	 * @return fecha del primer dia del mes en curso
	 */
	public static Date getPrimerDiaDelMes(Date fecha) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMinimum(Calendar.DAY_OF_MONTH),
				cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE),
				cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * Retorna la fecha del ultimo dia del mes de la fecha del parametro.
	 * @param fecha - fecha de la que se calculara el ultimo dia de su mes.
	 * @return fecha del ultimo dia del mes en curso
	 */
	public static Date getUltimoDiaDelMes(Date fecha) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * Elimina una palabra del texto original, luego lo corta al tamaï¿½o indicado.
	 * @param texto - texto original que se va a cortar
	 * @param palabra - palabra que se eliminarï¿½ del texto original
	 * @param tamanio - tamanio final que debe tener el texto al final del proceso
	 * @return cadena del texto cortado. 
	 */
	public static String cortarPalabra(String texto, String palabra, int tamanio) {
		return (texto.replace(palabra, "").substring(0, tamanio)) ;
	}
	
	/**
	 * Eliminar los saltos de linea de una cadena.
	 * @param texto - texto original que se va a cortar
	 * @return cadena del texto resultante. 
	 */
	public static String eliminarSaltos(String texto, String remplazo) {
		return (texto.replaceAll("[\n\r]",remplazo)) ;
	}
	
	/**
	 * Compara las fecha de un timestamp(fechaHora)
	 * @param fechaTSIni - fecha hora inicial
	 * @param fechaTSFin - fecha hora final
	 * @return -1 si fechaTSIni es menor que fechaTSFin, 0 si son iguales, 1 si fechaTSIni es mayor que fechaTSFin
	 */
	public static int compararTimeStamp(Timestamp fechaTSIni, Timestamp fechaTSFin){
		Date fechaIni = null;
		Date fechaFin = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaIni = sdf.parse(sdf.format(fechaTSIni.getTime()));
			fechaFin = sdf.parse(sdf.format(fechaTSFin.getTime()));
		} catch (ParseException e) {
		}
		
		return fechaIni.compareTo(fechaFin);
	}
	
    /**
	 * Transforma las fechas de servipagos a timestamp
	 * @param stFecha - fecha como string
	 * @param stHora - hora como string
	 * @return Fecha y hora como timestamp
     * @throws ParseException 
	 */
	public static Timestamp fechaHoraStringATimeStamp(String stFecha, String stHora) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date fecha = sdf.parse(stFecha+stHora);
    	Timestamp time = new Timestamp(fecha.getTime());
		return time;
	}

	 public static boolean isValidEmailAddress(String email) {
		 if(email==null || email.length()==0){
			 return false;
		 }else{
			 String PATRON_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			 Pattern pattern = Pattern.compile(PATRON_EMAIL);
		     Matcher matcher = pattern.matcher(email);
		     return matcher.matches();
		 }
	 }
	 
	 
	 /**
	  * Extrae los ultimos caracteres de un string
	  * @param cadena - cadena de la que se extrae los caracteres
	  * @param numCaracteres - numero de caracteres a extraer
	  * @return - ultimos caracteres de la cadena
	  */
	 public static String extraeUltimosCaracteres(String cadena, int numCaracteres ){
		 if(cadena==null){
			 return "";
		 }
		 if(cadena.length()<=numCaracteres){
			 return cadena;
		 }
		 return cadena.substring((cadena.length()-4), cadena.length());
	 }
	 
	 
	 /**
	 * Calcula la cantidad de dias entre do fechas
	 * @param fechaIni - fecha inicial para el calculo 
	 * @param fechaFin - fecha final para el calculo
	 * @return numero de dias de diferencia.
	 */
	public static int calcularDiferenciFechas(Date fechaIni, Date fechaFin){
		final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
		double diferencia= ( fechaFin.getTime() - fechaIni.getTime() )/ MILLSECS_PER_DAY;
		return (int)diferencia;
	}
	
    /**
	 * Calcula la cantidad de dias de forma absoluta entre dos fechas
	 * @param fechaIni - fecha inicial para el calculo 
	 * @param fechaFin - fecha final para el calculo
	 * @return numero de dias de diferencia.
	 */
	public static int calcularDiferenciFechasEnDiasAbsolutos(Date fechaIni, Date fechaFin){
		final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(fechaIni);
		calIni.set(Calendar.HOUR, 0);
		calIni.set(Calendar.MINUTE, 0);
		calIni.set(Calendar.SECOND, 0);
		calIni.set(Calendar.MILLISECOND, 0);
		Calendar calFin = Calendar.getInstance();
		calFin.setTime(fechaFin);
		calFin.set(Calendar.HOUR, 0);
		calFin.set(Calendar.MINUTE, 0);
		calFin.set(Calendar.SECOND, 0);
		calFin.set(Calendar.MILLISECOND, 0);
		double diferencia= ( calFin.getTimeInMillis() - calIni.getTimeInMillis() )/ MILLSECS_PER_DAY;
		return (int)diferencia;
	}
	
	
	public static String stacktraceToString(Exception e){
//		StackTraceElement[] stack = e.getStackTrace();
//	    String exception = "";
//	    for (StackTraceElement s : stack) {
//	        exception = exception + s.toString() + "\n\t\t";
//	    }
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
		
	}
	
	/**
	 * Verifica si la fecha esta entre dos fechas 
	 * @param fechaIni - fecha inicial para el calculo 
	 * @param fechaFin - fecha final para el calculo
	 * @param fechaVerif - fecha que se necesita verificar
	 * @return true si esta entre flase caso contrario
	 */
	public static boolean verificarEntreFechas(Date fechaIni, Date fechaFin, Date fechaVerif){
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(fechaFin);
		calendario.add(Calendar.HOUR, 3);
		fechaFin = calendario.getTime();
		
		return fechaVerif.after(fechaIni) && fechaVerif.before(fechaFin);
	}

	
	/**
	 *  Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static BigDecimal aproximarDosDecimales(BigDecimal decimal){
		double redondeado = redondeoBasico(decimal.doubleValue(), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return new BigDecimal(cortado);
	}
	
	/**
	 * Aproxima un double a dos decimales 
	 * @param double que se quiere aproximar
	 * @return double aproximado a dos decimales
	 */
	public static double aproximarDosDecimales(double decimal){
		double redondeado = redondeoBasico(decimal, 2);
		String cortado = cortarDecimales(redondeado, 2);
		return Double.parseDouble(cortado);
	}
	
	/**
	 * Aproxima un String a dos decimales 
	 * @param String que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static BigDecimal aproximarDosDecimales(String decimal){
		double redondeado = redondeoBasico(Double.parseDouble(decimal), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return new BigDecimal(cortado);
	}
	
	/**
	 * Aproxima un String a dos decimales 
	 * @param String que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static double aproximarDosDecimalesDouble(String decimal){
		double redondeado = redondeoBasico(Double.parseDouble(decimal), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return Double.parseDouble(cortado);
	}
	
	/**
	 * Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return String aproximado a dos decimales
	 */
	public static String aproximarDosDecimalesStr(BigDecimal decimal){
		double redondeado = redondeoBasico(decimal.doubleValue(), 2);
		return cortarDecimales(redondeado, 2);
	}
	
	/**
	 *  Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return double aproximado a dos decimales
	 */
	public static double aproximarDosDecimalesDouble(BigDecimal decimal){
		double redondeado = redondeoBasico(decimal.doubleValue(), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return Double.parseDouble(cortado);
	}
	
	/**
	 * Aproxima un double a dos decimales 
	 * @param double que se quiere aproximar
	 * @return String aproximado a dos decimales
	 */
	public static String aproximarDosDecimalesStr(double decimal){
		double redondeado = redondeoBasico(decimal, 2);
		return cortarDecimales(redondeado, 2);
	}
	
	/**
	 *  Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static BigDecimal aproximarDosDecimalesBigdc(double decimal){
		double redondeado = redondeoBasico(decimal, 2);
		String cortado = cortarDecimales(redondeado, 2);
		return new BigDecimal(cortado);
	}
	
	public static double redondeoBasico(double numero, int numDecimales){
		double potenciador = Math.pow(10.0, numDecimales);
		return (double) Math.round(numero * potenciador) / potenciador;
	}
	
	/**
	 * Aproxima un decimal a dos decimales ".".
	 * @param decimal double a aproximar
	 * @return El nuevo BigDecimal.
	 */
	public static String cortarDecimales(double decimal, int numDecimales){
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat formateador = new DecimalFormat("####.##",simbolos);
		formateador.setMaximumFractionDigits(numDecimales);
		formateador.setMinimumFractionDigits(numDecimales);
		return formateador.format(decimal);
	}
	
	public static String eliminarEspaciosEnBlanco(String cadena){
		return cadena.replaceAll(" +", " ").trim();
	}
	
	public static String eliminarEnterYEspaciosEnBlanco(String cadena){
		return cadena.replaceAll("[\n\r]", " ").replaceAll(" +", " ").trim().toUpperCase();
	}
	
	/**
	 * Genera el numero de tramite
	 * @param trmId - id del registro de tramite
	 * @param fecha - fecha de creacion del tramite
	 * @return
	 */
	public static String generarNumeroTramite(int trmId, Timestamp fecha){
		StringBuilder sb = new StringBuilder();
		sb.append("TRM_");
		sb.append(trmId);
		sb.append("_");
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
		sb.append(sdf.format(fecha.getTime()));
		return sb.toString();
	}
	
	
//	/**
//	* Genera la lista de items para los estado de tramite de titulo por ejecucion proceso
//	* @return lista de items para los estado de tramite de titulo
//	*/
//	public static List<Integer> listarEstadosProcesoTramiteXProcesoRol(int procesoRol)	{
//		List<Integer> retorno = new ArrayList<Integer>();
//		
//		if(procesoRol == TramiteTituloConstantes.PROCESO_ROL_SECRETARIA_VALUE){
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_ENVIO_SECRETARIA_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_VALIDACION_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_ACEPTADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_RECHAZADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_GENERAR_CSV_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_VERIFICAR_CARGA_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_VERIFICADO_OK_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_VERIFICADO_ERROR_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SENESCYT_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_TITULO_GENERADO_VALUE);
//		}
//		
//		if(procesoRol == TramiteTituloConstantes.PROCESO_ROL_SECRET_ABG_VALUE){
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_ENVIO_SECRETARIA_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_VALIDACION_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_ACEPTADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_RECHAZADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_GENERAR_CSV_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_VERIFICAR_CARGA_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_VERIFICADO_OK_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_VERIFICADO_ERROR_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SENESCYT_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_TITULO_GENERADO_VALUE);
//		}
//		
//		if(procesoRol == TramiteTituloConstantes.PROCESO_ROL_OUDE_VALUE){
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_ENVIO_SECRETARIA_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_VALIDACION_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_ACEPTADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_RECHAZADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SECR_ABG_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_GENERAR_CSV_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_VERIFICAR_CARGA_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_VERIFICADO_OK_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_VERIFICADO_ERROR_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SENESCYT_VALUE);
//			retorno.add(TramiteTituloConstantes.ESTADO_TITULO_GENERADO_VALUE);
//		}
//		return retorno;
//	}
	
	public static String[] separarNombres(String nombres){
		String[] listaArticulos = {"el", "la", "de", "los", "las","del"};
		
		String[] retorno = new String[2];
		if(nombres == null ){
			return null;
		}
		String asd = nombres.replaceAll(" +", " ").trim();
		
		String[] aux = asd.split("\\s+");
		if(aux.length == 1){
			retorno[0] = aux[0];
			retorno[1] = null;
		}else{
			int contNombres = 0;
			
			StringBuilder sb = new StringBuilder();
			boolean esArticulo = false;
			for (String item : aux) {
				if(contNombres == 0){
					//verifico si es articulo
					if(item.length() <=3){
						esArticulo = false;
						for (String itemArt: listaArticulos) {
							if(item.equalsIgnoreCase(itemArt)){//es articulo
								esArticulo = true;
								break;
							}
						}
						
						if(!esArticulo){
							sb.append(item);
							sb.append(" ");
							contNombres++;
							retorno[0]= sb.toString().replaceAll(" +", " ").trim();
							sb = new StringBuilder();
						}else{
							sb.append(item);
							sb.append(" ");
						}
					}else{
						sb.append(item);
						sb.append(" ");
						retorno[0]= sb.toString().replaceAll(" +", " ").trim();
						contNombres++;
						sb = new StringBuilder();
					}
				}else{
					sb.append(item);
					sb.append(" ");
				}
			}
			retorno[1]= sb.toString().replaceAll(" +", " ").trim();
		}
		return retorno;
	}
	
	/**
	* Genera el año a partir de una fecha
	* @return año de la fecha generada
	*/
	public static int obtenerAnio(Date fecha){
		if (null == fecha){
			return 0;
		}
		else{
			String formato="yyyy";
			SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			return Integer.parseInt(dateFormat.format(fecha));

		}

	}
	
	/**
	 * Método que permite copiar el archivo de reonsideración
	 * @param rutaOrigen - ruta en la que se encuentra el archivo de errores
	 * @param rutaDestino - ruta en la que se copiará el archivo de errores
	 * @throws IOException - excepción que no se pudo copiar el archivo de errores
	 */
	public static void copiarArchivo(InputStream rutaOrigen,String rutaDestino){
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[2048];
		try {
			while ((nRead = rutaOrigen.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			byte[] bytesArchivo = buffer.toByteArray();
			rutaOrigen.close();
			OutputStream output = new FileOutputStream(new File(rutaDestino));
			output.write(bytesArchivo);
	        if(output != null){
	        	output.close();
	        }
		} catch (Exception e) {
		}
	}
	
	/**
	 * Método que obtiene la extención del archivo de reconsideración del postulante o estudiante
	 * @param fileName - fileName nombre del archivo de reconsideración del postulante o estudiante
	 * @return retorno extención del archivo de reconsideración del postulante o estudiante
	 */
	public static String obtenerExtension(String fileName) {
		String retorno = ".upload";
		if (fileName != null) {
			StringTokenizer st = new StringTokenizer(fileName, ".");
			while (st.hasMoreTokens()) {
				retorno = st.nextToken();
			}
		}
		return retorno;
	}
	
	/**
	 * Obtiene la extension del archivo
	 * @param fileName nombre del archivo que se quiere buscar la extension
	 * @return la extension del archivo 
	 */
	public static String obtenerExtensionDescarga(String fileName){
		String retorno = ".download";
		if(fileName != null){
			StringTokenizer st = new StringTokenizer(fileName,".");
			while(st.hasMoreTokens()){
				retorno = st.nextToken();
			}
		}
		return retorno;
	}
	
	/**
	 * Método que permite obtener el ContentType de un archivo   
	 * @param rutaArchivoConExtension - ruta donde se encuentra el archivo.
	 * @return retorna la cadena del ContentType
	 */
	public static String obtenerContentType(String rutaArchivoConExtension)
	{
		String retorno = "";
		try
		{
			retorno = Files.probeContentType(Paths.get("",rutaArchivoConExtension));
		}
		catch(IOException ioe)
		{
			retorno = "";
		}
		return retorno;
	}
	
	
	/**
	 * Obtiene el tipo de reporte que será generado
	 * para la impresión del titulo académico.
	 * @param facultad - descripcion de la facultad o sede
	 * @param titulo - descripcion del titulo a obtener
	 * @return tipo de reporte que se debe generar.
	 *  1.- sede titulo pequeño
	 *  2.- sede titulo grande
	 *  3.- sin impreso facultad titulo pequeño facultad letra 10
	 *  4.- sin impreso facultad titulo pequeño facultad letra 12
	 *  5.- sin impreso facultad titulo grande facultad letra 10
	 *  6.- sin impreso facultad titulo grande facultad letra 12
	 *  7.- impreso filosofia titulo grande
	 */
	public static int obtenerTipoImpresion(String facultad, String titulo){
		int retorno = 0;
		String[] parts = facultad.split(" ");
		String parte1 = parts[0].toUpperCase(); // primera parte del split donde encontraré si es sede
		if(parte1.equals("SEDE")){
			if(titulo.length() > 51){
				retorno = 2;
			}else{
				retorno = 1;
			}
		}else if(!parte1.equals("FILOSOFÍA,")){
				if(titulo.length() >51){ // 51 valor para 2 lineas en el título
					if(facultad.length() > 32){ //  >32 valor para determinar si el length de la facultad el grande
						if(facultad.length() <= 46){ // hasta una sola linea en la facultad
							retorno = 5;
						}else{
							retorno = 9; //mas de una linea en la facultad
						}
						
					}else{ // <=32 valor para determinar si el length de la facultad el pequeño
						retorno = 6;
					}
				}else{ // 1 sola linea en el título
					if(facultad.length() > 32){
						if(facultad.length() <= 46){  // hasta una sola linea en la facultad
							retorno = 3;
						}else{
							retorno = 10; //mas de una linea en la facultad
						}
					}else{
						retorno = 4;
					}
				}
			}else{
				retorno = 8; // se sabe que es filosofia 
			}

		return retorno;
	}
	
	
	/**
	 * Transforma las fechas de timestamp string con formato
	 * @param tmFecha - fecha como timestamp
	 * @return Fecha como date trasformado a estring con formato
	 * @throws ParseException 
	 */
	public static String fechaFormatoTimeStamp(Timestamp tmFecha) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fecha = sdf.parse(tmFecha.toString());
		return sdf.format(fecha);
	}
	
	/**
	 * Genera el string con codigos de caracteres especiales para poner en el correo electronico
	 * @param cadena - String cadena que se requiere transformar
	 * @return
	 */
	public static String generaStringConTildes(String cadena){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<cadena.length();i++){
			switch (cadena.charAt(i)) {
			case 'Ñ':
				sb.append("&#209;");
				break;
			case 'Á':
				sb.append("&#193;");
				break;
			case 'É':
				sb.append("&#201;");
				break;
			case 'Í':
				sb.append("&#205;");
				break;
			case 'Ó':
				sb.append("&#211;");
				break;
			case 'Ú':
				sb.append("&#218;");
				break;

			case 'á':
				sb.append("&#225;");
				break;	
			case 'é':
				sb.append("&#233;");
				break;
			case 'í':
				sb.append("&#237;");
				break;
			case 'ó':
				sb.append("&#243;");
				break;
			case 'ú':
				sb.append("&#250;");
				break;

			default:
				sb.append(cadena.charAt(i));
				break;
			}
		}
		return sb.toString();
	}
	
	
	  /**
	   * Metodo que validar un dato segun un tipo de validación
	   * @param dato - string del dato a validar
	   * @param tipoValidacion - int para el tipo de validación a efectuar
	   * @return Boolean - true si pasó la validación, false caso contrario
	   */
	  public static Boolean validarXtipoDato(int tipoValidacion, String dato, Integer tipoId) {
		  Boolean retorno = false;
		  switch (tipoValidacion) {
			case 1: //validacion de cedula 
				if(tipoId.equals(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE)){
					retorno = validarDocumento(dato);
				}else{
					Pattern patron = Pattern.compile("[A-Za-z0-9]{1,}");
					Matcher matcher = patron.matcher(dato);
					if(matcher.matches()){
						retorno = true;
					}else{
						retorno = false;
					}
				}
				break;
			case 2: //validacion de string 
				Pattern patron = Pattern.compile("[A-Za-zñÑáéíóúÁÉÍÓüÜ]{1,}");
				String [] partes = dato.split("\\s");
				for (String item : partes) {
					Matcher matcher = patron.matcher(item);
					if(matcher.matches()){
						retorno = true;
					}
				}
				break;
			case 3: //validacion de email
				retorno = isValidEmailAddress(dato);
				break;
			case 4: //validacion numero
				try {
					Integer.parseInt(dato);
					retorno = true;
				} catch (NumberFormatException e) {
					retorno = false;
				}
				break; 
			case 5: //validacion fecha
				try {
					LocalDate.parse(dato);
					retorno  = true;
				} catch (DateTimeParseException e) {
					retorno = false;
				}
				
				break;
			case 6: //validacion decimal float:MQ
				try {
					Float.parseFloat(dato);
					retorno = true;
				} catch (NumberFormatException e) {
					retorno = false;
				}
				break; 
				
			default:
				break;
		}
		  return retorno;
	  }
	  
	  /**
	   * Metodo que obtiene la letra que corresponde a un numero de columna
	   * @param numColumna - numero de columna a obtener su letra
	   * @return String - string de la letra que corresponde a la columna enviada
	   */
	  public static String obtenerLetraColumna(int numColumna) {
		  Map<Integer, String> celdas = new HashMap<>();
		  celdas.put(0, "A");
		  celdas.put(1, "B");
		  celdas.put(2, "C");
		  celdas.put(3, "D");
		  celdas.put(4, "E");
		  celdas.put(5, "F");
		  celdas.put(6, "G");
		  celdas.put(7, "H");
		  celdas.put(8, "I");
		  celdas.put(9, "J");
		  celdas.put(10, "K");
		  celdas.put(11, "L");
		  celdas.put(12, "M");
		  celdas.put(13, "N");
		  celdas.put(14, "O");
		  celdas.put(15, "P");
		  celdas.put(16, "Q");
		  celdas.put(17, "R");
		  celdas.put(18, "S");
		  celdas.put(19, "T");
		  celdas.put(20, "U");
		  celdas.put(21, "V");
		  celdas.put(22, "W");
		  celdas.put(23, "X");
		  celdas.put(24, "Y");
		  celdas.put(25, "Z");
		  return celdas.get(numColumna);
	  }
	  
	  
	  /**
		 * Genera el string con codigos de caracteres especiales para poner en el correo electronico
		 * @param cadena - String cadena que se requiere transformar
		 * @return
		 */
		public static String generaStringParaCorreo(String cadena){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<cadena.length();i++){
				switch (cadena.charAt(i)) {
				case 'Ñ':
					sb.append("&#209;");
					break;
				case 'Á':
					sb.append("&#193;");
					break;
				case 'É':
					sb.append("&#201;");
					break;
				case 'Í':
					sb.append("&#205;");
					break;
				case 'Ó':
					sb.append("&#211;");
					break;
				case 'Ú':
					sb.append("&#218;");
					break;
					
				case 'á':
					sb.append("&#225;");
					break;	
				case 'é':
					sb.append("&#233;");
					break;
				case 'í':
					sb.append("&#237;");
					break;
				case 'ó':
					sb.append("&#243;");
					break;
				case 'ú':
					sb.append("&#250;");
					break;

				default:
					sb.append(cadena.charAt(i));
					break;
				}
			}
			return sb.toString();
		}
		
		 /** Método que devuelve el contenido de un Mail correspondiente al docente que registró las notas
			 * 
			 * @return cadena con el mail
			 */
			  public static StringBuilder generarAsunto(String fecha,String nombres, String carrera ,  String materia,  String paralelo){
					StringBuilder sb = new StringBuilder();
					sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
					sb.append("<html>");
					sb.append("<head>");
					sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
					sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
					sb.append("</head>");
					sb.append("<body>");
					sb.append("<p><strong>Estimado(a). Docente.-");
					for(int i=0;i<nombres.length();i++){
						if (nombres.charAt(i)=='Ñ'){
							sb.append("&#209;");
						}else{
							sb.append(nombres.charAt(i));
						}
					}
					sb.append(".-</strong></p>");
					sb.append("<p><strong>Carrera/&#225;rea: </strong>");sb.append(carrera);sb.append("</p>");
					sb.append("<p><strong>Asignatura: </strong>");sb.append(materia);sb.append("</p>");
					sb.append("<p><strong>Paralelo: </strong>");sb.append(paralelo);sb.append("</p>");
					sb.append("<br/>");
					sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("Se informa que Usted ha guardado de manera exitosa las notas del hemisemestre. Adjunto encontrar&#225; el archivo PDF con las notas ingresadas.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("Se recuerda que puede verificarlas ingresando al sistema en la opci&#243;n <strong>notas - ingreso de notas - ver. </strong>");
					sb.append("<br/>");
					sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
					sb.append("</p></strong>");
					sb.append("<br/>");
					sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
					sb.append("<br/>Universidad Central del Ecuador.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("------------------------------------------------------------------------------------------------------------------");
					sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
					sb.append("<tbody>");
					sb.append("<tr style=\"min-height:10.1pt\">");
					sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
					sb.append("<br/><br/>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
					sb.append("</p>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
					sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
					sb.append("</p>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
					sb.append("<br/>  <br/>");
					sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
					sb.append("</p>");
					sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</tbody>");
					sb.append("</table>");
					sb.append("------------------------------------------------------------------------------------------------------------------");
					sb.append("</body>");
					sb.append("</html>");
					return sb;
				}
			  
			  /** Método que devuelve el contenido de un Mail correspondiente al docente que registró las notas
				 * 
				 * @return cadena con el mail
				 */
				  public static StringBuilder generarAsuntoIdiomas(String fecha,String nombres, String carrera ,  String materia,  String paralelo){
						StringBuilder sb = new StringBuilder();
						sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
						sb.append("<html>");
						sb.append("<head>");
						sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
						sb.append("</head>");
						sb.append("<body>");
						sb.append("<p><strong>Estimado(a). Docente ");
						for(int i=0;i<nombres.length();i++){
							if (nombres.charAt(i)=='Ñ'){
								sb.append("&#209;");
							}else{
								sb.append(nombres.charAt(i));
							}
						}
						sb.append(".-</strong></p>");
						sb.append("<p><strong>Carrera/&#225;rea: </strong>");sb.append(carrera);sb.append("</p>");
						sb.append("<p><strong>Asignatura: </strong>");sb.append(materia);sb.append("</p>");
						sb.append("<p><strong>Paralelo: </strong>");sb.append(paralelo);sb.append("</p>");
						sb.append("<br/>");
						sb.append("Bienvenido al m&#243;dulo Acad&#233;mico del Sistema Integral de Informaci&#243;n Universitaria - SIIU.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Se informa que Usted ha guardado de manera exitosa las notas de la suficiencia en el Idioma extranjero. Adjunto encontrar&#225; el archivo PDF con las notas ingresadas.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Se recuerda que puede verificarlas ingresando al sistema en la opci&#243;n <strong>Reportes - Notas </strong>");
						sb.append("<br/>");
						sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
						sb.append("</p></strong>");
						sb.append("<br/>");
						sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
						sb.append("<br/>Universidad Central del Ecuador.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
						sb.append("<tbody>");
						sb.append("<tr style=\"min-height:10.1pt\">");
						sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/><br/>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
						sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/>  <br/>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
						sb.append("</p>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("</tr>");
						sb.append("</tbody>");
						sb.append("</table>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("</body>");
						sb.append("</html>");
						return sb;
					}
			  
			  
			  /** Método que devuelve el contenido de un Mail correspondiente a la solicitud de tercera matrícula realizada por el estudiante
				 * 
				 * @param fecha mes y año que corresponde el rol
				 * @param cadena nombre estudiante
				 * @param carerra carrera del estudiantes
				 * @return cadena con el mail
				 */
				  public static StringBuilder generarAsuntoSolicitudTercera(String fecha,String nombres, String carrera){
						StringBuilder sb = new StringBuilder();
						sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
						sb.append("<html>");
						sb.append("<head>");
						sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
						sb.append("</head>");
						sb.append("<body>");
						sb.append("<p><strong>Estimado(a). ");
						for(int i=0;i<nombres.length();i++){
							if (nombres.charAt(i)=='Ñ'){
								sb.append("&#209;");
							}else{
								sb.append(nombres.charAt(i));
							}
						}
						sb.append(".-</strong></p>");
						sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
						sb.append("<br/>");
						sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Se informa que Usted ha guardado de manera exitosa la solicitud de tercera matr&#237;cula. Adjunto encontrar&#225; el archivo PDF con la solicitud ingresada.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Cabe recalcar que Usted deber&#225; presentar el documento adjunto, debidamente firmado junto a la evidencia en la secretar&#237;a de su carrera para continuar con el proceso.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
						sb.append("</p></strong>");
						sb.append("<br/>");
						sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
						sb.append("<br/>Universidad Central del Ecuador.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
						sb.append("<tbody>");
						sb.append("<tr style=\"min-height:10.1pt\">");
						sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/><br/>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
						sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/>  <br/>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
						sb.append("</p>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("</tr>");
						sb.append("</tbody>");
						sb.append("</table>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("</body>");
						sb.append("</html>");
						return sb;
					}
				  
				  
				  
				  
				  /** Método que devuelve el contenido de un Mail correspondiente a la aprobacion  del director de carrera de la solicitud de tercera matriucla
					 * 
					 * @param fecha mes y año que corresponde el rol
					 * @param cadena nombre del estudiante
					 * @param carerra carrera del estudiantes
					 * @return cadena con el mail
					 */                       
					  public static StringBuilder generarAsuntoAprobacionTercera(String fecha,String nombres, String carrera){
							StringBuilder sb = new StringBuilder();
							sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
							sb.append("<html>");
							sb.append("<head>");
							sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
							sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
							sb.append("</head>");
							sb.append("<body>");
							sb.append("<p><strong>Estimado(a). ");
							for(int i=0;i<nombres.length();i++){
								if (nombres.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(nombres.charAt(i));
								}
							}
							sb.append(".-</strong></p>");
							sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
							sb.append("<br/>");
							sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("Adjunto al presente se servir&#225; encontrar la respuesta a la solicitud de tercera matr&#237;cula.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
							sb.append("</p></strong>");
							sb.append("<br/>");
							sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
							sb.append("<br/>Universidad Central del Ecuador.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
							sb.append("<tbody>");
							sb.append("<tr style=\"min-height:10.1pt\">");
							sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/><br/>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
							sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/>  <br/>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
							sb.append("</p>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</tbody>");
							sb.append("</table>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("</body>");
							sb.append("</html>");
							return sb;
						}
					  
					  
					  
				  /** Método que devuelve el contenido de un Mail correspondiente a la verificación de la secretaria de carrera al recibir los documentos de solicitud
					 * 
					 * @param fecha mes y año que corresponde el rol
					 * @param  estudiante, cadena nombre del estudiante
					 * @param director, cadena nombre del director de carrera
					 * @param carerra carrera del estudiantes
					 * @return cadena con el mail
					 */
					  public static StringBuilder generarAsuntoAprobarSolicitudTercera(String fecha,String estudiante, String secretaria, String carrera){
							StringBuilder sb = new StringBuilder();
							sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
							sb.append("<html>");
							sb.append("<head>");
							sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
							sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
							sb.append("</head>");
							sb.append("<body>");
							sb.append("<p><strong>Estimado(a). ");
							for(int i=0;i<estudiante.length();i++){
								if (estudiante.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(estudiante.charAt(i));
								}
							}
							sb.append(".-</strong></p>");
							sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
							sb.append("<br/>");
							sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("Se informa que sus documentos f&#237sicos han sido recibidos por el usuario:");
							sb.append("<br/>");
							sb.append(secretaria);
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
							sb.append("</p></strong>");
							sb.append("<br/>");
							sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
							sb.append("<br/>Universidad Central del Ecuador.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
							sb.append("<tbody>");
							sb.append("<tr style=\"min-height:10.1pt\">");
							sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/><br/>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
							sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/>  <br/>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
							sb.append("</p>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</tbody>");
							sb.append("</table>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("</body>");
							sb.append("</html>");
							return sb;
						}
			  
				  
					  
					  
					  
					  /** Método que devuelve el contenido de un Mail correspondiente a la apelación de tercera matrícula realizada por el estudiante
						 * 
						 * @param fecha mes y año que corresponde el rol
						 * @param cadena nombre estudiante
						 * @param carerra carrera del estudiantes
						 * @return cadena con el mail
						 */
						  public static StringBuilder generarAsuntoApelacionTercera(String fecha,String nombres, String carrera){
								StringBuilder sb = new StringBuilder();
								sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
								sb.append("<html>");
								sb.append("<head>");
								sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
								sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
								sb.append("</head>");
								sb.append("<body>");
								sb.append("<p><strong>Estimado(a). ");
								for(int i=0;i<nombres.length();i++){
									if (nombres.charAt(i)=='Ñ'){
										sb.append("&#209;");
									}else{
										sb.append(nombres.charAt(i));
									}
								}
								sb.append(".-</strong></p>");
								sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
								sb.append("<br/>");
								sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("Se informa que Usted ha guardado de manera exitosa la solicitud de apelaci&#243;n de tercera matr&#237;cula. Adjunto encontrar&#225; el archivo PDF con la solicitud ingresada.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("Cabe recalcar que Usted deber&#225; presentar el documento adjunto, debidamente firmado junto a la evidencia en la secretar&#237;a de su carrera para continuar con el proceso.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
								sb.append("</p></strong>");
								sb.append("<br/>");
								sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
								sb.append("<br/>Universidad Central del Ecuador.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("------------------------------------------------------------------------------------------------------------------");
								sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
								sb.append("<tbody>");
								sb.append("<tr style=\"min-height:10.1pt\">");
								sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
								sb.append("<br/><br/>");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
								sb.append("</p>");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
								sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
								sb.append("</p>");
								sb.append("</td>");
								sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
								sb.append("</p>");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
								sb.append("</p>");
								sb.append("</td>");
								sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
								sb.append("<br/>  <br/>");
								sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
								sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
								sb.append("</p>");
								sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
								sb.append("</p>");
								sb.append("</td>");
								sb.append("</tr>");
								sb.append("</tbody>");
								sb.append("</table>");
								sb.append("------------------------------------------------------------------------------------------------------------------");
								sb.append("</body>");
								sb.append("</html>");
								return sb;
							}
					  
				
						  
						  /** Método que devuelve el contenido de un Mail correspondiente a la verificación de la secretaria de carrera al recibir los documentos de solicitud
							 * 
							 * @param fecha mes y año que corresponde el rol
							 * @param  estudiante, cadena nombre del estudiante
							 * @param director, cadena nombre del director de carrera
							 * @param carerra carrera del estudiantes
							 * @return cadena con el mail
							 */
							  public static StringBuilder generarAsuntoVerificarApelacionTercera(String fecha,String estudiante, String secretaria, String carrera){
									StringBuilder sb = new StringBuilder();
									sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
									sb.append("<html>");
									sb.append("<head>");
									sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
									sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
									sb.append("</head>");
									sb.append("<body>");
									sb.append("<p><strong>Estimado(a). ");
									for(int i=0;i<estudiante.length();i++){
										if (estudiante.charAt(i)=='Ñ'){
											sb.append("&#209;");
										}else{
											sb.append(estudiante.charAt(i));
										}
									}
									sb.append(".-</strong></p>");
									sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
									sb.append("<br/>");
									sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("Se informa que sus documentos f&#237sicos han sido recibidos por el usuario:");
									sb.append("<br/>");
									sb.append(secretaria);
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
									sb.append("</p></strong>");
									sb.append("<br/>");
									sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
									sb.append("<br/>Universidad Central del Ecuador.");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("------------------------------------------------------------------------------------------------------------------");
									sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
									sb.append("<tbody>");
									sb.append("<tr style=\"min-height:10.1pt\">");
									sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
									sb.append("<br/><br/>");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
									sb.append("</p>");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
									sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
									sb.append("</p>");
									sb.append("</td>");
									sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
									sb.append("</p>");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
									sb.append("</p>");
									sb.append("</td>");
									sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
									sb.append("<br/>  <br/>");
									sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
									sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
									sb.append("</p>");
									sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
									sb.append("</p>");
									sb.append("</td>");
									sb.append("</tr>");
									sb.append("</tbody>");
									sb.append("</table>");
									sb.append("------------------------------------------------------------------------------------------------------------------");
									sb.append("</body>");
									sb.append("</html>");
									return sb;
								}
					  
				  
			  /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
				 * 
				 * @param aux true -> funcionario ----- false -> administrador de nómina
				 * @param fecha mes y año que corresponde el rol
				 * @param cadena nombre del funcionario
				 * @return cadena con el mail
				 */
			  
				  public static StringBuilder generarAsuntoPosgrado(String fecha,String nombres, String carrera , String nivel, String paralelo, String facultad){
						StringBuilder sb = new StringBuilder();
						sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
						sb.append("<html>");
						sb.append("<head>");
						sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
						sb.append("</head>");
						sb.append("<body>");
						sb.append("<p><strong>Estimado(a).-");
						for(int i=0;i<nombres.length();i++){
							if (nombres.charAt(i)=='Ñ'){
								sb.append("&#209;");
							}else{
								sb.append(nombres.charAt(i));
							}
						}
						sb.append(".-</strong></p>");
						sb.append("<br/>");
						sb.append("Bienvenido/a al Sistema Integral de Informaci&#243;n Universitaria.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Se informa que usted ha generado su matr&#237;cula en el posgrado de la Facultad de ");sb.append(facultad);sb.append(".");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Datos de la matr&#237;cula:");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("<table>");
						sb.append("<tr align='left'>");
						sb.append("<th>PROGRAMA: </th>");
						sb.append("<td>");sb.append(carrera);sb.append("</td>");
						sb.append("</tr>");
						sb.append("<tr align='left'>");
						sb.append("<th>NIVEL: </th>");
						sb.append("<td>");sb.append(nivel);sb.append("</td>");
						sb.append("</tr>");
						sb.append("<tr align='left'>");
						sb.append("<th>PARALELO: </th>");
						sb.append("<td>");sb.append(paralelo);sb.append("</td>");
						sb.append("</tr>");
						sb.append("</table>");
						sb.append("<br/>");
						
						sb.append("<br/>");
						sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
						sb.append("</p></strong>");
						sb.append("<br/>");
						sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
						sb.append("<br/>Universidad Central del Ecuador.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
						sb.append("<tbody>");
						sb.append("<tr style=\"min-height:10.1pt\">");
						sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/><br/>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
						sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/>  <br/>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
						sb.append("</p>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("</tr>");
						sb.append("</tbody>");
						sb.append("</table>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("</body>");
						sb.append("</html>");
						return sb;
					}
			  
				  
					  public static StringBuilder generarAsuntoNivelacion(String fecha,String nombres, String carrera ,  
							  String nivel, String paralelo, String facultad, String carrerasiiu){
							StringBuilder sb = new StringBuilder();
							sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
							sb.append("<html>");
							sb.append("<head>");
							sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
							sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
							sb.append("</head>");
							sb.append("<body>");
							sb.append("<p><strong>Estimado(a).-");
							for(int i=0;i<nombres.length();i++){
								if (nombres.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(nombres.charAt(i));
								}
							}
							sb.append(".-</strong></p>");
							sb.append("<br/>");
							sb.append("Bienvenido/a al Sistema Integral de Informaci&#243;n Universitaria.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("Usted ha registrado una matr&#237;cula en el sistema, por favor ingrese a la aplicaci&#243n para verificar los datos.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("Datos de la matr&#237;cula:");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("<table>");
							sb.append("<tr align='left'>");
							sb.append("<th>FACULTAD: </th>");
							sb.append("<td>");sb.append(facultad);sb.append("</td>");
							sb.append("</tr>");
							sb.append("<tr align='left'>");
							sb.append("<th>CARRERA: </th>");
							sb.append("<td>");sb.append(carrerasiiu);sb.append("</td>");
							sb.append("</tr>");
							sb.append("<tr align='left'>");
							sb.append("<th>AREA: </th>");
							sb.append("<td>");sb.append(carrera);sb.append("</td>");
							sb.append("</tr>");
							sb.append("<tr align='left'>");
							sb.append("<th>NIVEL: </th>");
							sb.append("<td>");sb.append(nivel);sb.append("</td>");
							sb.append("</tr>");
							sb.append("</table>");
							sb.append("<br/>");
							
							sb.append("<br/>");
							sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
							sb.append("</p></strong>");
							sb.append("<br/>");
							sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
							sb.append("<br/>Universidad Central del Ecuador.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
							sb.append("<tbody>");
							sb.append("<tr style=\"min-height:10.1pt\">");
							sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/><br/>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
							sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/>  <br/>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
							sb.append("</p>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</tbody>");
							sb.append("</table>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("</body>");
							sb.append("</html>");
							return sb;
						}
					  public static StringBuilder generarAsuntoNuevaFactura(String fecha,String nombres, String carrera ,  
							  String nivel){
							StringBuilder sb = new StringBuilder();
							sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
							sb.append("<html>");
							sb.append("<head>");
							sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
							sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
							sb.append("</head>");
							sb.append("<body>");
							sb.append("<p><strong>Estimado(a).-");
							for(int i=0;i<nombres.length();i++){
								if (nombres.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(nombres.charAt(i));
								}
							}
							sb.append(".-</strong></p>");
							sb.append("<br/>");
							sb.append("Bienvenido/a al Sistema Integral de Informaci&#243;n Universitaria.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("Usted ha generado un nuevo comprobante de pago, por favor ac&#233;rquese a SERVIPAGOS para cancelarlo a partir del d&#237;a siguiente de su emisi&#243;n.");
							sb.append("<br/>");
							sb.append("<br/>");
							
							sb.append("<br/>");
							sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
							sb.append("</p></strong>");
							sb.append("<br/>");
							sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
							sb.append("<br/>Universidad Central del Ecuador.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
							sb.append("<tbody>");
							sb.append("<tr style=\"min-height:10.1pt\">");
							sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/><br/>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
							sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/>  <br/>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
							sb.append("</p>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</tbody>");
							sb.append("</table>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("</body>");
							sb.append("</html>");
							return sb;
						}		  
			     /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
				 * 
				 * @param aux true -> funcionario ----- false -> administrador de nómina
				 * @param fecha mes y año que corresponde el rol
				 * @param cadena nombre del funcionario
				 * @return cadena con el mail
				 */
				  public static StringBuilder generarAsuntoRectificacionNotas(String fecha,String nombres, String carrera ,  String materia,  String paralelo, String notaRectificacion){
						StringBuilder sb = new StringBuilder();
						sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
						sb.append("<html>");
						sb.append("<head>");
						sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
						sb.append("</head>");
						sb.append("<body>");
						sb.append("<p><strong>Estimado(a). Docente.-");
						for(int i=0;i<nombres.length();i++){
							if (nombres.charAt(i)=='Ñ'){
								sb.append("&#209;");
							}else{
								sb.append(nombres.charAt(i));
							}
						}
						sb.append(".-</strong></p>");
						sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
						sb.append("<p><strong>Asignatura: </strong>");sb.append(materia);sb.append("</p>");
						sb.append("<p><strong>Paralelo: </strong>");sb.append(paralelo);sb.append("</p>");
						sb.append("<p><strong>Nota: </strong>");sb.append(notaRectificacion);sb.append("</p>");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("Se informa que usted ha guardado de manera exitosa y definitiva la rectificaci&#243;n de las notas y/o asistencias. Adjunto encontrar&#225; el archivo PDF.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("La rectificaci&#243;n de notas y/o asistencias puede verificarlas ingresando al m&#243;dulo acad&#233;mico del Sistema Integral de Informaci&#243;n Universitaria a trav&#233;s de la opci&#243;n <strong>Reportes - Notas </strong>");
						sb.append("<br/>");
						sb.append("Fecha de rectificaci&#243;n: ");sb.append(fecha);
						sb.append("</p></strong>");
						sb.append("<br/>");
						sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
						sb.append("<br/>Universidad Central del Ecuador.");
						sb.append("<br/>");
						sb.append("<br/>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
						sb.append("<tbody>");
						sb.append("<tr style=\"min-height:10.1pt\">");
						sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/><br/>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
						sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
						sb.append("</p>");
						sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						sb.append("<br/>  <br/>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
						sb.append("</p>");
						sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
						sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
						sb.append("</p>");
						sb.append("</td>");
						sb.append("</tr>");
						sb.append("</tbody>");
						sb.append("</table>");
						sb.append("------------------------------------------------------------------------------------------------------------------");
						sb.append("</body>");
						sb.append("</html>");
						return sb;
					}
				  
				  /** Método que devuelve el contenido de un Mail para el director de carrera con asunto rectificacion de notas 
					 * 
					 * @param aux true -> funcionario ----- false -> administrador de nómina
					 * @param fecha mes y año que corresponde el rol
					 * @param cadena nombre del funcionario
					 * @return cadena con el mail
					 */
					  public static StringBuilder generarAsuntoRectificacionNotasDirCarrera(String fecha,String nombresDicCarrera, String nombreDocente, String carrera ,  String materia,  String nivel,String paralelo ,String notaRectificacion){
							StringBuilder sb = new StringBuilder();
							sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
							sb.append("<html>");
							sb.append("<head>");
							sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
							sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
							sb.append("</head>");
							sb.append("<body>");
							sb.append("<p><strong>Estimado(a). Docente.-");
							for(int i=0;i<nombresDicCarrera.length();i++){
								if (nombresDicCarrera.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(nombresDicCarrera.charAt(i));
								}
							}
							sb.append(".-</strong></p>");
							sb.append("<br/>");
							sb.append("Director(a) de la Carrera/&#193;rea: ");sb.append("<p><strong>");sb.append(carrera);sb.append("</strong></p>");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("Se informa que el Docente: "); sb.append("<p><strong>");
							for(int i=0;i<nombreDocente.length();i++){
								if (nombreDocente.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(nombreDocente.charAt(i));
								}
							}
							sb.append("</strong></p>");
							sb.append(" Ha guardado de manera exitosa y definitiva la rectificaci&#243;n de las notas y/o asistencias de acuerdo al siguiente detalle:");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("<p><strong>Asignatura: </strong>");sb.append(materia);sb.append("</p>");
							sb.append("<p><strong>Nivel: </strong>");sb.append(nivel);sb.append("</p>");
							sb.append("<p><strong>Paralelo: </strong>");sb.append(paralelo);sb.append("</p>");
							sb.append("<p><strong>Nota rectificada: </strong>");sb.append(notaRectificacion);sb.append("</p>");
							sb.append("Adjunto encontrar&#225; el archivo  de la hoja de calificaciones respectivas en formato PDF.");
							sb.append("<br/>");
							sb.append("<p><strong>Fecha de rectificaci&#243;n: </strong>");sb.append(fecha);
							sb.append("</p></strong>");
							sb.append("<br/>");
							sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
							sb.append("<br/>Universidad Central del Ecuador.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
							sb.append("<tbody>");
							sb.append("<tr style=\"min-height:10.1pt\">");
							sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/><br/>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
							sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/>  <br/>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
							sb.append("</p>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</tbody>");
							sb.append("</table>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("</body>");
							sb.append("</html>");
							return sb;
						}
				  
				  /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
					 * 
					 * @param aux true -> funcionario ----- false -> administrador de nómina
					 * @param fecha mes y año que corresponde el rol
					 * @param cadena nombre del funcionario
					 * @return cadena con el mail
					 */
					  public static StringBuilder generarAsuntoCSVComprobantePago(String fecha,String nombres){
							StringBuilder sb = new StringBuilder();
							sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
							sb.append("<html>");
							sb.append("<head>");
							sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
							sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
							sb.append("</head>");
							sb.append("<body>");
							sb.append("<p><strong>Estimado(a). ");
							for(int i=0;i<nombres.length();i++){
								if (nombres.charAt(i)=='Ñ'){
									sb.append("&#209;");
								}else{
									sb.append(nombres.charAt(i));
								}
							}
							sb.append(".-</strong></p>");
							
							sb.append("El presente correo contiene el archivo CSV generado de la aplicaci&#243;n Sistema Integral de Informaci&#243;n Universitaria para realizar la carga a Servipagos");
							sb.append("<br/>");
							sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
							sb.append("</p></strong>");
							sb.append("<br/>");
							sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
							sb.append("<br/>Universidad Central del Ecuador.");
							sb.append("<br/>");
							sb.append("<br/>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
							sb.append("<tbody>");
							sb.append("<tr style=\"min-height:10.1pt\">");
							sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/><br/>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
							sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
							sb.append("</p>");
							sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
							sb.append("<br/>  <br/>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
							sb.append("</p>");
							sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
							sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
							sb.append("</p>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</tbody>");
							sb.append("</table>");
							sb.append("------------------------------------------------------------------------------------------------------------------");
							sb.append("</body>");
							sb.append("</html>");
							return sb;
						}
					  
	

					  
					  /** Método que devuelve el contenido de un Mail correspondiente a la solicitud de retiro de matrícula realizada por el estudiante
					   * 
					   * @param fecha mes y año que corresponde el rol
					   * @param cadena nombre estudiante
					   * @param carerra carrera del estudiantes
					   * @return cadena con el mail
					   */
					  public static StringBuilder generarAsuntoSolicitudRetiroMatricula(String fecha,String nombres, String carrera, List<EstudianteJdbcDto> listaMaterias){
						  StringBuilder sb = new StringBuilder();
						  sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
						  sb.append("<html>");
						  sb.append("<head>");
						  sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						  sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
						  sb.append("</head>");
						  sb.append("<body>");
						  sb.append("<p><strong>Estimado(a). ");
						  for(int i=0;i<nombres.length();i++){
							  if (nombres.charAt(i)=='Ñ'){
								  sb.append("&#209;");
							  }else{
								  sb.append(nombres.charAt(i));
							  }
						  }
						  sb.append(".-</strong></p>");
						  sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
						  sb.append("<br/>");
						  sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("Se informa que Usted ha generado de manera exitosa la solicitud de retiro de matr&#237;cula. Adjunto encontrar&#225; el archivo PDF con la solicitud ingresada.");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("Lista de asignaturas solicitadas para retiro de matr&#237;cula");
						  sb.append("<br/>");
						  for (EstudianteJdbcDto item : listaMaterias) {
							  sb.append("<br/>");
							  sb.append("C&#243;digo : ");sb.append(item.getMtrCodigo());
							  sb.append(" , Asignatura : ");sb.append(GeneralesUtilidades.generaStringConTildes(item.getMtrDescripcion()));
							  if(item.getDtmtEstadoRespuesta() == DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE){
								  sb.append(" , Estado : ");sb.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL); 
							  }
							  if(item.getDtmtEstadoRespuesta() == DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE){
								  sb.append(" , Estado : ");sb.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL); 
							  }
						  }
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
						  sb.append("</p></strong>");
						  sb.append("<br/>");
						  sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
						  sb.append("<br/>Universidad Central del Ecuador.");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("------------------------------------------------------------------------------------------------------------------");
						  sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
						  sb.append("<tbody>");
						  sb.append("<tr style=\"min-height:10.1pt\">");
						  sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						  sb.append("<br/><br/>");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
						  sb.append("</p>");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
						  sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
						  sb.append("</p>");
						  sb.append("</td>");
						  sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
						  sb.append("</p>");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
						  sb.append("</p>");
						  sb.append("</td>");
						  sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						  sb.append("<br/>  <br/>");
						  sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
						  sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
						  sb.append("</p>");
						  sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
						  sb.append("</p>");
						  sb.append("</td>");
						  sb.append("</tr>");
						  sb.append("</tbody>");
						  sb.append("</table>");
						  sb.append("------------------------------------------------------------------------------------------------------------------");
						  sb.append("</body>");
						  sb.append("</html>");
						  return sb;
					  }
					  
					  /** Método que devuelve el contenido de un Mail correspondiente a la solicitud de retiro de matrícula realizada por el estudiante
					   * 
					   * @param fecha mes y año que corresponde el rol
					   * @param cadena nombre estudiante
					   * @param carerra carrera del estudiantes
					   * @return cadena con el mail
					   */
					  public static StringBuilder generarAsuntoRespuestaRetiroMatricula(String fecha,String nombres, String carrera, List<EstudianteJdbcDto> listaMaterias){
						  StringBuilder sb = new StringBuilder();
						  sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
						  sb.append("<html>");
						  sb.append("<head>");
						  sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						  sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
						  sb.append("</head>");
						  sb.append("<body>");
						  sb.append("<p><strong>Estimado(a). ");
						  for(int i=0;i<nombres.length();i++){
							  if (nombres.charAt(i)=='Ñ'){
								  sb.append("&#209;");
							  }else{
								  sb.append(nombres.charAt(i));
							  }
						  }
						  sb.append(".-</strong></p>");
						  sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
						  sb.append("<br/>");
						  sb.append("Bienvenido al Sistema Integral de Informaci&#243;n Universitaria.");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("Por favor revisar en el sistema la respuesta a la solicitud de retiro de asignatura:");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("Lista de asignaturas solicitadas para el retiro de matr&#237;cula");
						  sb.append("<br/>");
						  for (EstudianteJdbcDto item : listaMaterias) {
							  sb.append("<br/>");
							  sb.append("C&#243;digo : ");sb.append(item.getMtrCodigo());
							  sb.append(" , Asignatura : ");sb.append(GeneralesUtilidades.generaStringConTildes(item.getMtrDescripcion()));
							  if(item.getDtmtEstadoRespuesta().intValue() == DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE){
								  sb.append(" , Estado : ");sb.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL); 
							  }
							  if(item.getDtmtEstadoRespuesta().intValue() == DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE){
								  sb.append(" , Estado : ");sb.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL); 
							  }
						  }
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
						  sb.append("</p></strong>");
						  sb.append("<br/>");
						  sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
						  sb.append("<br/>Universidad Central del Ecuador.");
						  sb.append("<br/>");
						  sb.append("<br/>");
						  sb.append("------------------------------------------------------------------------------------------------------------------");
						  sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
						  sb.append("<tbody>");
						  sb.append("<tr style=\"min-height:10.1pt\">");
						  sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						  sb.append("<br/><br/>");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
						  sb.append("</p>");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
						  sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
						  sb.append("</p>");
						  sb.append("</td>");
						  sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
						  sb.append("</p>");
						  sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
						  sb.append("</p>");
						  sb.append("</td>");
						  sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
						  sb.append("<br/>  <br/>");
						  sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
						  sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
						  sb.append("</p>");
						  sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
						  sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
						  sb.append("</p>");
						  sb.append("</td>");
						  sb.append("</tr>");
						  sb.append("</tbody>");
						  sb.append("</table>");
						  sb.append("------------------------------------------------------------------------------------------------------------------");
						  sb.append("</body>");
						  sb.append("</html>");
						  return sb;
					  }
					  
					  /** Método que devuelve el contenido de un Mail correspondiente a la solicitud de retiro por situaciones fortuitas por el estudiante
						 * 
						 * @param fecha mes y año que corresponde el rol
						 * @param cadena nombre estudiante
						 * @param carerra carrera del estudiantes
						 * @return cadena con el mail
						 */
						  public static StringBuilder generarAsuntoSolicitudRetiroFortuito(String fecha,String nombres, String carrera){
								StringBuilder sb = new StringBuilder();
								sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
								sb.append("<html>");
								sb.append("<head>");
								sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
								sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
								sb.append("</head>");
								sb.append("<body>");
								sb.append("<p><strong>Estimado(a). ");
								for(int i=0;i<nombres.length();i++){
									if (nombres.charAt(i)=='Ñ'){
										sb.append("&#209;");
									}else{
										sb.append(nombres.charAt(i));
									}
								}
								sb.append(".-</strong></p>");
								sb.append("<p><strong>Carrera/area: </strong>");sb.append(carrera);sb.append("</p>");
								sb.append("<br/>");
								sb.append("Bienvenido al m&#243;dulo Acad&#233;mico del Sistema Integral de Informaci&#243;n Universitaria.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("Se informa que Usted ha guardado de manera exitosa la solicitud de retiro por situaciones fortuitas. Adjunto encontrar&#225; el archivo PDF de la solicitud generada.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("Para continuar con el proceso deber&#225; imprimir la solicitud generada, firmar y entregar inmediatamente en la Secretar&#237;a de la Carrera  adjuntando las evidencias que justifican la causal del pedido.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
								sb.append("</p></strong>");
								sb.append("<br/>");
								sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
								sb.append("<br/>Universidad Central del Ecuador.");
								sb.append("<br/>");
								sb.append("<br/>");
								sb.append("------------------------------------------------------------------------------------------------------------------");
								sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
								sb.append("<tbody>");
								sb.append("<tr style=\"min-height:10.1pt\">");
								sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
								sb.append("<br/><br/>");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
								sb.append("</p>");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
								sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
								sb.append("</p>");
								sb.append("</td>");
								sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
								sb.append("</p>");
								sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
								sb.append("</p>");
								sb.append("</td>");
								sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
								sb.append("<br/>  <br/>");
								sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
								sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
								sb.append("</p>");
								sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
								sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
								sb.append("</p>");
								sb.append("</td>");
								sb.append("</tr>");
								sb.append("</tbody>");
								sb.append("</table>");
								sb.append("------------------------------------------------------------------------------------------------------------------");
								sb.append("</body>");
								sb.append("</html>");
								return sb;
							}
						  
						  
						  /** Método que devuelve el contenido de un Mail correspondiente a la aprobacion  del secretario Abogado de la solicitud de retiro fortuito
							 * 
							 * @param fecha mes y año que corresponde el rol
							 * @param cadena nombre del estudiante
							 * @param carerra carrera del estudiantes
							 * @return cadena con el mail
							 */                       
							  public static StringBuilder generarAsuntoAprobacionRetiroFortuito(String fecha,String nombres, String carrera){
									StringBuilder sb = new StringBuilder();
									sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
									sb.append("<html>");
									sb.append("<head>");
									sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
									sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
									sb.append("</head>");
									sb.append("<body>");
									sb.append("<p><strong>Estimado(a). ");
									for(int i=0;i<nombres.length();i++){
										if (nombres.charAt(i)=='Ñ'){
											sb.append("&#209;");
										}else{
											sb.append(nombres.charAt(i));
										}
									}
									sb.append(".-</strong></p>");
									sb.append("<p><strong>Carrera/área: </strong>");sb.append(carrera);sb.append("</p>");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("Adjunto al presente se servir&#225; encontrar la respuesta a la solicitud de retiro por situaciones fortuitas o de fuerza mayor.");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
									sb.append("</p></strong>");
									sb.append("<br/>");
									sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
									sb.append("<br/>Universidad Central del Ecuador.");
									sb.append("<br/>");
									sb.append("<br/>");
									sb.append("------------------------------------------------------------------------------------------------------------------");
									sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
									sb.append("<tbody>");
									sb.append("<tr style=\"min-height:10.1pt\">");
									sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
									sb.append("<br/><br/>");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
									sb.append("</p>");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
									sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
									sb.append("</p>");
									sb.append("</td>");
									sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
									sb.append("</p>");
									sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
									sb.append("</p>");
									sb.append("</td>");
									sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
									sb.append("<br/>  <br/>");
									sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
									sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
									sb.append("</p>");
									sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
									sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
									sb.append("</p>");
									sb.append("</td>");
									sb.append("</tr>");
									sb.append("</tbody>");
									sb.append("</table>");
									sb.append("------------------------------------------------------------------------------------------------------------------");
									sb.append("</body>");
									sb.append("</html>");
									return sb;
								}						  
					  
							  
							  /** Método que devuelve el contenido de un Mail correspondiente a la aprobacion  del secretario Abogado de la solicitud de retiro fortuito
								 * 
								 * @param fecha mes y año que corresponde el rol
								 * @param cadena nombre del estudiante
								 * @param carerra carrera del estudiantes
								 * @return cadena con el mail
								 */                       
								  public static StringBuilder generarAsuntoAnulacionMatricula(String fecha,String nombres, String carrera){
										StringBuilder sb = new StringBuilder();
										sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
										sb.append("<html>");
										sb.append("<head>");
										sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
										sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
										sb.append("</head>");
										sb.append("<body>");
										sb.append("<p><strong>Estimado(a). ");
										for(int i=0;i<nombres.length();i++){
											if (nombres.charAt(i)=='Ñ'){
												sb.append("&#209;");
											}else{
												sb.append(nombres.charAt(i));
											}
										}
										sb.append(".-</strong></p>");
										sb.append("<p><strong>Carrera: </strong>");sb.append(carrera);sb.append("</p>");
										sb.append("<br/>");
										sb.append("<br/>");
										sb.append("<br/>");
										sb.append("Adjunto al presente se servir&#225; encontrar el informe de anulaci&#243;n de matr&#237;cula.");
										sb.append("<br/>");
										sb.append("<br/>");
										sb.append("<br/>");
										sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
										sb.append("</p></strong>");
										sb.append("<br/>");
										sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
										sb.append("<br/>Universidad Central del Ecuador.");
										sb.append("<br/>");
										sb.append("<br/>");
										sb.append("------------------------------------------------------------------------------------------------------------------");
										sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
										sb.append("<tbody>");
										sb.append("<tr style=\"min-height:10.1pt\">");
										sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
										sb.append("<br/><br/>");
										sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
										sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
										sb.append("</p>");
										sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
										sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
										sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
										sb.append("</p>");
										sb.append("</td>");
										sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
										sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
										sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
										sb.append("</p>");
										sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
										sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
										sb.append("</p>");
										sb.append("</td>");
										sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
										sb.append("<br/>  <br/>");
										sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
										sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
										sb.append("</p>");
										sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
										sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
										sb.append("</p>");
										sb.append("</td>");
										sb.append("</tr>");
										sb.append("</tbody>");
										sb.append("</table>");
										sb.append("------------------------------------------------------------------------------------------------------------------");
										sb.append("</body>");
										sb.append("</html>");
										return sb;
									}						  
	
	 /**
		 * Elimina tildes de una cadena
		 * @param campoEditar - cadena a editar 
		 * @return boolean.
		 */
		public static String eliminarTildes(String campoEditar) {
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<campoEditar.length();i++){
				switch (campoEditar.charAt(i)) {
				case 'Á':
					sb.append("A");
					break;
				case 'É':
					sb.append("E");
					break;
				case 'Í':
					sb.append("I");
					break;
				case 'Ó':
					sb.append("O");
					break;
				case 'Ú':
					sb.append("U");
					break;
				default:
					sb.append(campoEditar.charAt(i));
					break;
				}
			}
			return sb.toString();
		}
		
		
		/**
		 * Método que permite convertir un objeto java.util.Date a un String, el formato de salida es el parametro que se ingrese.
		 * @author FREDDY - fgguzman 
		 * @param date - java.util.Date
		 * @param formato - "Ejm. dd/MM/yyyy HH:mm:ss"
		 * @return cadena de texto con fecha en formato solicitado.
		 */
		public static String cambiarDateToStringFormatoFecha(Date date, String formato) {
			if (date!= null) {
				SimpleDateFormat formateador = new SimpleDateFormat(formato, new Locale("es", "EC"));
				return formateador.format(date);	
			}else {
				return " - ";
			}
			
		}
	
		/**
		 * Método que da formato a un BigDecimal y retorna un String.
		 * @author FREDDY - fgguzman 
		 * @param param - bigdecimal
		 * @param simbolo - adiciona simbolo.
		 * @return valor en formato string.
		 */
		public static String cambiarBigDecimalToString(BigDecimal param, int simbolo){
			
			if (param != null && param.intValue() != 0) {
				if (simbolo == 0) {
					return String.format("%.2f", param) + " %";	
				}else if(param.intValue() == -1 ){
					return "";
				}else if(simbolo == 1 ){
					return String.format("%.2f", param);
				}else if (simbolo == 2) {
					return String.valueOf(param.intValue());
				}
			}
			
			return "";
		}
				
		/**
		 * Método que permite convertir a un String - formato "Quito, 10 de Enero de 2018" tomando una
		 * fecha tipo java.Util.Date.
		 * @author fgguzman 
		 * @param java.Util.Date - parametro de entrada.
		 * @return String - representa la fecha en formato "10 de Enero de 2018".
		 */
		public static String cambiarDateToStringFormatoCaberaDocumento(Date date) {
			DateFormat formateador = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("es", "EC"));
			return formateador.format(date);
		}
		
		
		/**
		 * Metodo que convierte timestamp en String.
		 * @param timestamp - fecha
		 * @param entrada - formato
		 * @return Java.Util.Date
		 */
		public static String cambiarTimestampToString(Timestamp timestamp, String entrada) {
			String retorno = "";
			SimpleDateFormat sdf = new SimpleDateFormat(entrada);
			Date fecha = null;
			
			try {
				fecha = sdf.parse(timestamp.toString());
				retorno = sdf.format(fecha);
			} catch (ParseException e) {
			}
			
			return retorno;
		}
		
		/**
		 * Método que obtiene la fecha del sistema
		 * @author fgguzman
		 * @return Date - fecha del servidor.
		 */
		public static Date getFechaActualSistema(){
			return new Date(System.currentTimeMillis());
		}
		
		/**
		 * Método que obtiene la fecha del sistema
		 * @author fgguzman
		 * @return Timestamp -fecha del servidor.
		 */
		public static Timestamp getFechaActualSistemaTimestamp(){
			return  new Timestamp(System.currentTimeMillis());
		}
		
		/**
		 * Método que permite obtener una fecha con incremento de dias.
		 * @author fgguzman
		 * @param fecha - fecha que requiere.
		 * @param incremento - dias de incremento
		 * @return fecha nueva
		 */
		public static Date getFechaMasDiasIncremento(Date fecha, int incremento) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.DATE, incremento);
			return calendar.getTime();
		}
		
		/**
		 * Metodo que permite obtener el dia lunes de la semana a la que corresponde la fecha.
		 * @author fgguzman
		 * @param fecha - fecha
		 * @return fecha correspondiente al lunes.
		 */
		public static Date getPrimerDiaDeLaSemana(Date fecha) {
			GregorianCalendar calendar = new GregorianCalendar();
			
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(fecha);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.set(Calendar.AM_PM, Calendar.AM);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			return calendar.getTime();
		}
		
		
		
		/**
		 * Método que permite eliminar de una lista los items duplicados usando su identificación.
		 * @author Daniel
		 * @param ArrayList<EstudianteJdbcDto> list - Lista a eliminar
		 * @return ArrayList<EstudianteJdbcDto> nuevaLista Lista limpia
		 */
	    public static ArrayList<EstudianteJdbcDto> quitarDuplicados(List<EstudianteJdbcDto> listaInicial) 
	    { 
	        ArrayList<EstudianteJdbcDto> nuevaLista = new ArrayList<EstudianteJdbcDto>(); 
	        for (EstudianteJdbcDto item : listaInicial) { 
	        	try {
	        		boolean op=true;
	        		for (EstudianteJdbcDto itemNuevo : nuevaLista) {
	        			 if (itemNuevo.getPrsIdentificacion().equals(item.getPrsIdentificacion())) { 
	     	            	op=false;
	     	            } 
	        		}
	        		if(op){
	        			nuevaLista.add(item);	
	        		}
				} catch (Exception e) {
					nuevaLista.add(item);
				}
	        } 
	        return nuevaLista; 
	    } 
	    
	    /**
		 * Método que permite eliminar de una lista los items duplicados usando su identificación.
		 * @author Daniel
		 * @param ArrayList<EstudianteJdbcDto> list - Lista a eliminar
		 * @return ArrayList<EstudianteJdbcDto> nuevaLista Lista limpia
		 */
	    public static ArrayList<MateriaDto> quitarDuplicadosRecalificacion(List<MateriaDto> listaInicial) 
	    { 
	        ArrayList<MateriaDto> nuevaLista = new ArrayList<MateriaDto>(); 
	        for (MateriaDto item : listaInicial) { 
	        	try {
	        		boolean op=true;
	        		for (MateriaDto itemNuevo : nuevaLista) {
	        			 if (itemNuevo.getRcesId()==(item.getRcesId())) { 
	     	            	op=false;
	     	            } 
	        		}
	        		if(op){
	        			nuevaLista.add(item);	
	        		}
				} catch (Exception e) {
					nuevaLista.add(item);
				}
	        } 
	        return nuevaLista; 
	    } 
	    
}

