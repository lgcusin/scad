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
   
 ARCHIVO:     SesionUsuarioForm.java	  
 DESCRIPCION: Clase encargada de obtener usuario y roles a traves del facesUtils. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.GrantedAuthority;

import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.spring.springSecurity.ProveedorPermisos;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (manageBean) SesionUsuarioForm.
 * Clase encargada de obtener usuario y roles a traves del facesUtils.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="sesionUsuarioForm")
@SessionScoped
public class SesionUsuarioForm implements Serializable{
	private static final long serialVersionUID = 2300187948955776410L;
	
	private Usuario frmSuUsuario;
	
	@EJB
	private FichaInscripcionDtoServicioJdbc servJdbcSuFichaInscripcionDto;
	
	/**
	 * Retorna el usuario (entity bean) logueado
	 * @return el usuario (entity bean) logueado
	 */
	public Usuario getFrmSuUsuario() {
		frmSuUsuario = FacesUtil.obtenerUsuario();
		return frmSuUsuario;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMINISTRADOR, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINISTRADOR, false de lo contrario
	 */
	public boolean isAdministrador(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMIN)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ESTUDIANTE, false de lo contrario
	 * @return true si el usuario tiene el rol ESTUDIANTE, false de lo contrario
	 */
	public boolean isEstudiante(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ESTUD)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Retorna true si el usuario tiene el rol ESTUDIANTE, false de lo contrario
	 * @return true si el usuario tiene el rol ESTUDIANTE, false de lo contrario
	 */
	public boolean isEstudiantePregrado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ESTUD_PREGRADO)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol SOPORTE, false de lo contrario
	 * @return true si el usuario tiene el rol SOPORTE, false de lo contrario
	 */
	public boolean isSoporte(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SOPORTE)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMINFACULTAD, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINFACULTAD, false de lo contrario
	 */
	public boolean isAdminFacultad(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINFACULTAD)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DIRCARRERA, false de lo contrario
	 * @return true si el usuario tiene el rol DIRCARRERA, false de lo contrario
	 */
	public boolean isDirCarrera(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRCARRERA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DOCENTE, false de lo contrario
	 * @return true si el usuario tiene el rol DOCENTE, false de lo contrario
	 */
	public boolean isDocente(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DOCENTE)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna true si el usuario tiene el rol DIRRRHH, false de lo contrario
	 * @return true si el usuario tiene el rol DIRRRHH, false de lo contrario
	 */
	public boolean isDirRRHH(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRRRHH)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol SECRECARRERA, false de lo contrario
	 * @return true si el usuario tiene el rol SECRECARRERA, false de lo contrario
	 */
	public boolean isSecreCarrera(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SECRECARRERA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMINNIVELACION, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINNIVELACION, false de lo contrario
	 */
	public boolean isAdminNivelacion(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINNIVELACION)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol USUARIONIVELACION, false de lo contrario
	 * @return true si el usuario tiene el rol USUARIONIVELACION, false de lo contrario
	 */
	public boolean isUsuarioNivelacion(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_USUARIONIVELACION)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMINDGA, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINDGA, false de lo contrario
	 */
	public boolean isAdminDga(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINDGA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMINDPP, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINDPP, false de lo contrario
	 */
	public boolean isAdminDpp(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINDPP)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol SECREPOSGRADO, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINDPP, false de lo contrario
	 */
	public boolean isSecrePosgrado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SECREPOSGRADO)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ESTUDIANTEPOSGRADO, false de lo contrario
	 * @return true si el usuario tiene el rol ESTUDIANTEPOSGRADO, false de lo contrario
	 */
	public boolean isEstudiantePosgrado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ESTUDIANTEPOSGRADO)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DIRECTOR DE ASEGURAMIENTO DE CALIDAD, false de lo contrario
	 * @return true si el usuario tiene el rol DIRECTOR DE ASEGURAMIENTO DE CALIDAD, false de lo contrario
	 */
	public boolean isDirectorAseguramientoCalidad(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DIRECTOR DE VINCULACION CON LA SOCIEDAD, false de lo contrario
	 * @return true si el usuario tiene el rol DIRECTOR DE VINCULACION CON LA SOCIEDAD, false de lo contrario
	 */
	public boolean isDirectorVinculacionSociedad(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRVINCULACIONSOCIEDAD)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol COORDINADOR DE INVESTIGACION, false de lo contrario
	 * @return true si el usuario tiene el rol COORDINADOR DE INVESTIGACION, false de lo contrario
	 */
	public boolean isCoordinadorInvestigacion(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_COORDINVESTIGACION)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol PRESIDENTE DEL COMITÉ DE ETICA, false de lo contrario
	 * @return true si el usuario tiene el rol PRESIDENTE DEL COMITÉ DE ETICA, false de lo contrario
	 */
	public boolean isPresComiteEtica(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_PRESCOMITEETICA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol COORDINADOR DE GESTION, false de lo contrario
	 * @return true si el usuario tiene el rol COORDINADOR DE GESTION, false de lo contrario
	 */
	public boolean isCoordinadorGestion(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_COORDGESTION)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol COORDINADOR DE ASIGNACION DE CARGA HORARIA, false de lo contrario
	 * @return true si el usuario tiene el rol COORDINADOR DE ASIGNACION DE CARGA HORARIA, false de lo contrario
	 */
	public boolean isCoordinadorCargaHoraria(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DIRECTOR DE INVESTIGACION., false de lo contrario
	 * @return true si el usuario tiene el rol DIRECTOR DE INVESTIGACION., false de lo contrario
	 */
	public boolean isDirectorInvestigacion(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRINVESTIGACION)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DIRECTOR DE INVESTIGACION., false de lo contrario
	 * @return true si el usuario tiene el rol DIRECTOR DE INVESTIGACION., false de lo contrario
	 */
	public boolean isDirectorPosgrado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRPOSGRADO)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ACTUALIZACION DOCENTE., false de lo contrario
	 * @return true si el usuario tiene el rol ACTUALIZACION DOCENTE., false de lo contrario
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public boolean isActualizacionDocente(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ACTUALIZACIONDOCENTE)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol EVALUADOR DE DIRECTIVOS., false de lo contrario
	 * @return true si el usuario tiene el rol EVALUADOR DE DIRECTIVOS., false de lo contrario
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public boolean isEvaluadorDirectivos(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_EVALUADORDIRECTIVOS)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Retorna true si el usuario tiene el rol DIRECTOR DE CARRERA DE POSGRADOS., false de lo contrario
	 * @return true si el usuario tiene el rol 	/**
	 * Retorna true si el usuario tiene el rol ACTUALIZACION DOCENTE., false de lo contrario
	 * @return true si el usuario tiene el rol ACTUALIZACION DOCENTE., false de lo contrario
	 */
	public boolean isDirCarreraPosgrado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DIRCARRERAPOSGRADO)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol EVALUADOR DE PARES ACADEMICOS., false de lo contrario
	 * @return true si el usuario tiene el rol EVALUADOR DE PARES ACADEMICOS., false de lo contrario
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public boolean isEvaluadorParesAcademicos(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_EVALUADORPARESACADEMICOS)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Retorna true si el usuario tiene el rol COORDINADOR DE AREA., false de lo contrario
	 * @return true si el usuario tiene el rol COORDINADOR DE AREA., false de lo contrario
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public boolean isCoordinadorArea(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_COORDINADORAREA)){
				return true;
			}
		}
		return false;
	}
	 
	
	/**
	 * Retorna true si el usuario tiene el rol SECRETARIA DEL SECRETARIO ABOGADO, false de lo contrario
	 * @return true si el usuario tiene el rol SECRETARIA DEL SECRETARIO ABOGADO, false de lo contrario
	 */
	public boolean isSecreSecreAbogado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SECRESECREABOGADO)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Retorna true si el usuario tiene el rol SECRETARIO ABOGADO, false de lo contrario
	 * @return true si el usuario tiene el rol SECRETARIO ABOGADO, false de lo contrario
	 */
	public boolean isSecreAbogado(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SECREABOGADO)){
				return true;
			}
		}
	return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol SECRETARIO SUFICIENCIA, false de lo contrario
	 * @return true si el usuario tiene el rol SECRETARIO SUFICIENCIA, false de lo contrario
	 */
	public boolean isSecretarioSuficiencia(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SECRESUFICIENCIAS)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMIN SUFICIENCIA INFORMATICA, false de lo contrario
	 * @return true si el usuario tiene el rol ADMIN SUFICIENCIA INFORMATICA, false de lo contrario
	 */
	public boolean isAdminSuficienciaInformatica(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINSUFINFORMATICA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMIN SUFICIENCIA IDIOMAS, false de lo contrario
	 * @return true si el usuario tiene el rol ADMIN SUFICIENCIA IDIOMAS, false de lo contrario
	 */
	public boolean isAdminSuficienciaIdiomas(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINSUFIDIOMAS)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol ADMIN SUFICIENCIA AFR, false de lo contrario
	 * @return true si el usuario tiene el rol ADMIN SUFICIENCIA AFR, false de lo contrario
	 */
	public boolean isAdminSuficienciaActividadFisica(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINSUFACTIVIDADFISICA)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol SECRETARIO NIVELACION, false de lo contrario
	 * @return true si el usuario tiene el rol SECRETARIO NIVELACION, false de lo contrario
	 */
	public boolean isSecretarioNivelacion(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SECRENIVELACION)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol DECANO, false de lo contrario
	 * @return true si el usuario tiene el rol DECANO, false de lo contrario
	 */
	public boolean isDecano(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_DECANO)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Retorna true si el usuario tiene el rol SUBDECANO, false de lo contrario
	 * @return true si el usuario tiene el rol SUBDECANO, false de lo contrario
	 */
	public boolean isSubDecano(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_SUBDECANO)){
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * Retorna true si el usuario tiene el rol CONSULTOR REPORTES, false de lo contrario
	 * @return true si el usuario tiene el rol CONSULTOR REPORTES, false de lo contrario
	 */
	public boolean isConsultorReportes(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_CONSULTOREPORTES)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol INFORMATICO FACULTAD, false de lo contrario
	 * @return true si el usuario tiene el rol INFORMATICO FACULTAD, false de lo contrario
	 */
	public boolean isInformaticoFacultad(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_INFORMATICOFACULTAD)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol MOVILIDAD ESTUDIANTIL, false de lo contrario
	 * @return true si el usuario tiene el rol MOVILIDAD ESTUDIANTIL, false de lo contrario
	 */
	public boolean isMovilidadEstudiantil(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_MOVILIDADESTUDIANTIL)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Retorna true si el usuario tiene ficha inscripcion activa en nivelacion, carrera, suficiencia,....
	 * @return true si el usuario tiene ficha inscripcion activa en nivelacion, carrera, suficiencia,....
	 */
	public Boolean[] verProcesoMatricula(){
		Boolean retorno[] = {false,false,false, false};

		try {
			List<FichaInscripcionDto> fichas = servJdbcSuFichaInscripcionDto.listarFcinXIdentificacionXEstado(getFrmSuUsuario().getUsrIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
			
			for (FichaInscripcionDto it : fichas) {
				if(it.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE){
					retorno [0] = true;
				}
				
				if(it.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE
						|| it.getFcinTipo().intValue() ==  FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_VALUE
						|| it.getFcinTipo().intValue() ==  FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE){
					retorno [1] = true;
				}
				
				if(it.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE){
					retorno [2] = true;
				}
				
				if(it.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE){
					retorno [3] = true;
				}
				
			}
			
		} catch (FichaInscripcionDtoException | FichaInscripcionDtoNoEncontradoException e) {
			return retorno;
		}

		
		return retorno;
	}
}
