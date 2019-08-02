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
   
 ARCHIVO:     UsuarioInactivoException.java      
 DESCRIPCION: Clase encargada de gestionar las excepciones de usuario inactivo. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017             Dennis Collaguazo				Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.spring.excepciones;

/**
 * Clase (Exception)UsuarioInactivoException.
 * Clase encargada de gestionar las excepciones  de usuario inactivo. 
 * @author dcollaguazo
 * @version 1.0
 */
public class UsuarioInactivoException extends Exception {
	private static final long serialVersionUID = -800530230844770228L;

	/**
	 * Contructor nulo de la clase
	 */
	public UsuarioInactivoException() {
		super();
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 */
	public UsuarioInactivoException(String message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje en tiempo de ejecucion
	 */
	public UsuarioInactivoException(Throwable message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 * @param cause - mensaje en tiempo de ejecucion
	 */
	public UsuarioInactivoException(String message, Throwable cause) {
		super(message, cause);
	}

}
