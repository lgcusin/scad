package ec.edu.uce.academico.jsf.spring.springSecurity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import ec.edu.uce.academico.ejb.excepciones.UsuarioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.EncriptadorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.spring.excepciones.AutenticacionGeneralException;
import ec.edu.uce.academico.jsf.spring.excepciones.UsuarioInactivoException;


@Named("proveedorAutenticacion")
public class ProveedorAutenticacion implements AuthenticationProvider, Serializable{
	private static final long serialVersionUID = 3572187630900863258L;

	private UsuarioServicio srvUsuarioEjb;
	private UsuarioRolServicio srvUsuarioRolEjb;
	private String mensaje;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		try {
			
			String username = authentication.getName();
			String password = (String) authentication.getCredentials();
			
			//consulta usuario
			Usuario usu = srvUsuarioEjb.buscarPorNick(username);
			

			// verifica que el estado del usario este activo // todo esto para el caso de cambio de usuarios.
			//0: activo
			//1: inactivo
			if(usu.getUsrEstado() == 0){
				//verifico si el usuario es o no del active directory
				if(usu.getUsrActiveDirectory().intValue() == UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE){
//					ConexionLdap ldap = new ConexionLdap("10.20.1.5", "389", "DC=uce,DC=edu,DC=ec");

					ConexionLdap ldap = new ConexionLdap("uce.edu.ec", "389", "DC=uce,DC=edu,DC=ec");
					if(!ldap.verificarLoginUsuario(username, password) || password.length() == 0 ){
						mensaje = "Usuario y/o contraseña mal ingresados";
						throw new AutenticacionGeneralException("");
					}
				}else{//si es del active directory
					//verificacion de usuario y password
					if(username.equalsIgnoreCase(usu.getUsrNick())){
						String passEncript = null;
						try {
							passEncript = EncriptadorUtilidades.resumirMensaje(password, EncriptadorUtilidades.MD5);

						} catch (NoSuchAlgorithmException e) {
							mensaje = "Usuario y/o contraseña mal ingresados .";
							throw new AutenticacionGeneralException("");
						}
						if(!passEncript.equals(usu.getUsrPassword())){
							mensaje = "Usuario y/o contraseña mal ingresados.";
							throw new AutenticacionGeneralException("");
						}
					}else{
						mensaje = "Usuario y/o contraseña mal ingresados..";
						throw new AutenticacionGeneralException("");
					}
				}

				//**************************************************************
				//**************** ASIGNACION DE PERMISOS **********************
				//**************************************************************
				//definicion de lista de permisos
				List<ProveedorPermisos> permisos = new ArrayList<ProveedorPermisos>();

				//consulta de roles
				List<UsuarioRol> usuRoles = srvUsuarioRolEjb.buscarRolesActivoXUsuario(usu.getUsrId());
				for (UsuarioRol item : usuRoles) {
					permisos.add(new ProveedorPermisos(item.getUsroRol().getRolDescripcion()));
				}
				
				DetalleUsuario usuario = new DetalleUsuario(usu, permisos);
				return new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), permisos);
			}else{
				mensaje = "Usuario inactivo";
				throw new UsuarioInactivoException("Usuario inactivo");
			}
		} catch (UsuarioNoEncontradoException e1) {
			mensaje = "Usuario y/o contraseña mal ingresados";
			throw new BadCredentialsException("");
		} catch (UsuarioException e1) {
			mensaje = "Usuario y/o contraseña mal ingresados.";
			throw new BadCredentialsException("");
		} catch (UsuarioInactivoException e) {
			throw new BadCredentialsException("");	
		} catch (AutenticacionGeneralException e) {
			throw new BadCredentialsException("");	
		} catch (Exception e1) {
			e1.printStackTrace();
			mensaje = "Usuario y/o contraseña mal ingresados...";
			throw new BadCredentialsException("");
		} 

		
	}

	@Override
	public boolean supports(Class<?> arg0){
		return true;
	}
	public UsuarioServicio getSrvUsuarioEjb() {
		return srvUsuarioEjb;
	}
	public void setSrvUsuarioEjb(UsuarioServicio srvUsuarioEjb) {
		this.srvUsuarioEjb = srvUsuarioEjb;
	}
	public UsuarioRolServicio getSrvUsuarioRolEjb() {
		return srvUsuarioRolEjb;
	}
	public void setSrvUsuarioRolEjb(UsuarioRolServicio srvUsuarioRolEjb) {
		this.srvUsuarioRolEjb = srvUsuarioRolEjb;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
