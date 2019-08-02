package ec.edu.uce.academico.jsf.spring.springSecurity;

import org.springframework.security.core.GrantedAuthority;

public class ProveedorPermisos implements GrantedAuthority{

	private static final long serialVersionUID = -6835653812274402660L;
	
	private String rol;

	public ProveedorPermisos(String rol) {
		this.rol = rol;
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + rol;
	}
}