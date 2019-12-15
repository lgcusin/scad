package ec.edu.uce.Biometrico.jsf.spring.excepciones;

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
