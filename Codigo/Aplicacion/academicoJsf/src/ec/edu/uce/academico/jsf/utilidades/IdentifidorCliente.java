/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     IdentifidorCliente.java	  
 DESCRIPCION: Clase encargada de obtener ip publicas y privadas que consume los servicios del servidor. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-JUNIO-2017		 Gabriel Mafla 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Clase IdentifidorCliente.
 * Clase encargada de obtener ip publicas y privadas que consume los servicios del servidor. 
 * @author ghmafla.
 * @version 1.0
 */
public class IdentifidorCliente{
	
	public static String getClientIp(HttpServletRequest request) {

		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}
	
	
	public static Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {

		Map<String, String> result = new HashMap<>();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			result.put(key, value);
		}

		return result;
	}

	  /**
		 * Método que devuelve una lista con las identificaciones del cliente que
		 * hace peticiones al servidor
		 * 
		 * @param
		 * @return List<String>
		 */
		public static List<String> obtenerDatosCliente() {
			List<String> retorno = new ArrayList<String>();
//			FacesContext context = FacesContext.getCurrentInstance();
//			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//			String ipAddress = request.getRemoteAddr();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
//			if (ipAddress == null) {
//			    ipAddress = request.getRemoteAddr();
//			}
			
			if (ipAddress != null) {
		        // cares only about the first IP if there is a list
		        ipAddress = ipAddress.replaceFirst(",.*", "");
		    } else {
		        ipAddress = request.getRemoteAddr();
		    }
			retorno.add("IP del cliente:" + ipAddress);
			try {
				String ipLocal = request.getLocalAddr();
				retorno.add("IP del servidor:" + ipLocal);
			} catch (Exception e) {
				retorno.add("IP del servidor:N/A");
			}
			try {
				String puertoRemoto = String.valueOf(request.getRemotePort());
				retorno.add("Puerto:" + puertoRemoto);
			} catch (Exception e) {
				retorno.add("Puerto:N/A");
			}
			
			try {
				String usuarioRemoto = request.getRemoteUser();
				retorno.add("Usuario que ingresa:" + usuarioRemoto);
			} catch (Exception e) {
				retorno.add("Usuario que ingresa:N/A");
			}
//			try {
//				retorno.add("Cabecera:" + obtenerHeadersEnMap(request));
//			} catch (Exception e) {
//				retorno.add("Cabecera:N/A");
//			}
			try {
				retorno.add("IP pública:" + obtnerIpPublica());
//				System.out.println(obtnerIpPublica());
			} catch (Exception e1) {
				retorno.add("IP pública:N/A");
			}
			
//			for (Cookie item : request.getCookies()) {
//				"ID del servidor:"
//				retorno.add(item.getValue());
//				System.out.println(item.getValue());
//			}
			
//			System.out.println(ipAddress);
			
//			System.out.println(ipLocal);
			
//			System.out.println(puertoRemoto);
			
//			System.out.println(usuarioRemoto);
			
//			System.out.println(obtenerHeadersEnMap(request));
			
			return retorno;
		}

		public static String obtnerIpPublica() throws Exception {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
				String ip = in.readLine();
				return ip;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
		}

		public static Map<String, String> obtenerHeadersEnMap(HttpServletRequest request) {
			Map<String, String> resultado = new HashMap<>();
			@SuppressWarnings("rawtypes")
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String clave = (String) headerNames.nextElement();
				String valor = request.getHeader(clave);
				resultado.put(clave, valor);
			}
			return resultado;
		}
}
