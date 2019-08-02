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
   
 ARCHIVO:     AutenticacionGeneralException.java      
 DESCRIPCION: Clase encargada de gestionar las excepciones generales en la autenticacion. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017             Dennis Collaguazo				Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.spring.excepciones;

/**
 * Clase (Exception)AutenticacionGeneralException.
 * Clase encargada de gestionar las excepciones  generales en la autenticacion. 
 * @author dcollaguazo
 * @version 1.0
 */
public class AutenticacionGeneralException extends Exception {

	private static final long serialVersionUID = -4461258073598707463L;

	/**
	 * Contructor nulo de la clase
	 */
	public AutenticacionGeneralException() {
		super();
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 */
	public AutenticacionGeneralException(String message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje en tiempo de ejecucion
	 */
	public AutenticacionGeneralException(Throwable message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 * @param cause - mensaje en tiempo de ejecucion
	 */
	public AutenticacionGeneralException(String message, Throwable cause) {
		super(message, cause);
	}

}
