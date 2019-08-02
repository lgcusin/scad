package ec.edu.uce.academico.jsf.spring.springSecurity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

public class DetalleUsuario implements UserDetails{
	private static final long serialVersionUID = -7604616693479737220L;
	private Usuario usuario;
	private List<ProveedorPermisos> permisos;
	
	public DetalleUsuario(Usuario usuario, List<ProveedorPermisos> permisos) {
		this.usuario = usuario;
		this.permisos = permisos;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return permisos;
	}

	@Override
	public String getPassword()	{
		return usuario.getUsrPassword();
	}

	@Override
	public String getUsername(){
		return usuario.getUsrNick();
	}

	@Override
	public boolean isAccountNonExpired(){
		return true;
	}

	@Override
	public boolean isAccountNonLocked(){
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired(){
		return true;
	}

	@Override
	public boolean isEnabled(){
		return true;
	}

	public Usuario getUsuario() {
		return usuario;
	}
}
