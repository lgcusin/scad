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
 ARCHIVO:     FichaEstudianteDtoNoEncontradoException.java	  
 DESCRIPCION: Clase de excepcion lanzada cuando no se encuentra la entidad FichaEstudianteDto.
 *************************************************************************
                           	MODIFICACIONES
 FECHA         		     AUTOR          					COMENTARIOS
 11-07-2018			Daniel Ortiz				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.excepciones;
/**
 * Clase (Exception)FichaEstudianteDtoNoEncontradoException.
 * Clase de excepcion lanzada cuando no se encuentra la entidad FichaEstudianteDto.
 * @author dortiz
 * @version 1.0
 */
public class FichaEstudianteDtoNoEncontradoException extends Exception{
	private static final long serialVersionUID = -1786245988139158303L;

	/**
	 * Constructor por defecto de la excepcion.
	 */
	public FichaEstudianteDtoNoEncontradoException() {
		super();
	}

	/**
	 * Constructor de la excepcion el cual acepta un mensaje de error como parametro del
	 * constructor.
	 * @param message El mensaje de error.
	 */
	public FichaEstudianteDtoNoEncontradoException(String message) {
		super(message);
	}

	/**
	 * Constructor de la excepcion el cual acepta un mensaje de error y un objecto de tipo Throwable
	 * que representa la causa de esta excepcion.
	 * @param message El mensaje de error.
	 * @param cause La causa de la excepcion.
	 * 
	 * @see java.lang.Throwable
	 */
	public FichaEstudianteDtoNoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor de la excepcion el cual acepta la causa de la excepcion.
	 * @param cause La causa de la excepcion.
	 * 
	 * @see java.lang.Throwable
	 */
	public FichaEstudianteDtoNoEncontradoException(Throwable cause) {
		super(cause);
	}
}
