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
   
 ARCHIVO:     EncriptadorUtilidades.java	  
 DESCRIPCION: Clase encargada de manejar la encriptacion de datos 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.servicios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Clase  EncriptadorUtilidades.
 * Clase encargada de manejar la encriptacion de datos
 * @author dcollaguazo.
 * @version 1.0
 */
public class EncriptadorUtilidades {
	//algoritmos
	public static String MD2 = "MD2";
	public static String MD5 = "MD5";
	public static String SHA1 = "SHA-1";
	public static String SHA256 = "SHA-256";
	public static String SHA384 = "SHA-384";
	public static String SHA512 = "SHA-512";
	public static Integer CESAR = Integer.valueOf(7);
		
//	/***
//	 * Convierte un arreglo de bytes a String usando valores hexadecimales
//	 * @param digest - arreglo de bytes a convertir
//	 * @return String creado a partir del digest
//	 */
	private static String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1){
				hash += "0";
			}
			hash += Integer.toHexString(b);
		}
		return hash;
	}
		
//	/**
//	 * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
//	 * @param mensaje - texto a encriptar
//	 * @param algoritmo - algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
//	 * @return mensaje encriptado
//	 * @throws NoSuchAlgorithmException - error si el algoritmo indicado no existe
//	 */
	public static String resumirMensaje(String mensaje, String algoritmo)throws NoSuchAlgorithmException{
		mensaje = generarBaseCesar(mensaje);
		byte[] resumen = null;
		byte[] buffer = mensaje.getBytes();
		MessageDigest messageDigest = MessageDigest.getInstance(algoritmo);
		messageDigest.reset();
		messageDigest.update(buffer);
		resumen = messageDigest.digest();
		return toHexadecimal(resumen);
	}
	
//	/**
//	 * Encripta un menaje en base al numero cesar deseado
//	 * @param mensaje - texto a encriptar
//	 * @param cesar - numero cesar para la encriptacion
//	 * @return mensaje encriptado
//	 */
	public static String generarBaseCesar(String mensaje){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i< mensaje.length(); i++){
			int x = (int)mensaje.charAt(i);
			x = x+CESAR;
			char c = (char)x;
			sb.append(c);
		}
		return sb.toString();
	}
	
	
}
