package ec.edu.uce.Biometrico.jsf.springSecurity;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DetalleUsuario implements UserDetails {
	private static final long serialVersionUID = -7604616693479737220L;
	private String usuario;
	private String password;
	private List<ProveedorPermisos> permisos;

	public DetalleUsuario(String usuario, String password, List<ProveedorPermisos> permisos) {
		this.usuario = usuario;
		this.password = password;
		this.permisos = permisos;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permisos;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return usuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}