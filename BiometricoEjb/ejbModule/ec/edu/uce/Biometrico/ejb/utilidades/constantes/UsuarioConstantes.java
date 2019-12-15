package ec.edu.uce.Biometrico.ejb.utilidades.constantes;

/**
 * Clase (constantes) UsuarioConstantes.
 * Clase que maneja las constantes de la entidad Usuario.
 * @author dcollaguazo.
 * @version 1.0
 */
public class UsuarioConstantes {
	
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	public static Integer LARGO_CLAVE = Integer.valueOf(8);
	public static final String REGEX_CLAVE = "[A-Za-z0-9.]*";
	
	//constantes para el tipo de recuperación de contraseña	
	public static final int TIPO_USUARIO_VALUE = Integer.valueOf(0);
	public static final String TIPO_USUARIO_LABEL = "POR USUARIO";
	public static final int TIPO_CORREO_VALUE = Integer.valueOf(1);	
	public static final String TIPO_CORREO_LABEL = "POR CORREO ELECTRÓNICO";
	
	//constantes para el tipo de usuario	
	public static final int ACTIVE_DIRECTORY_SI_VALUE = Integer.valueOf(0);
	public static final String ACTIVE_DIRECTORY_SI_LABEL = "SI";
	public static final int ACTIVE_DIRECTORY_NO_VALUE = Integer.valueOf(1);	
	public static final String ACTIVE_DIRECTORY_NO_LABEL = "NO";
}
