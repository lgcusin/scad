package ec.edu.uce.Biometrico.jsf.spring.excepciones;

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
