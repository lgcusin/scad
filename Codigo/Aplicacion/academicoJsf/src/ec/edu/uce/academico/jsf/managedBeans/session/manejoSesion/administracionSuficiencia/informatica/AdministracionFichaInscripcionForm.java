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
   
 ARCHIVO:     AdministracionFichaInscripcionForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-MAY-2019 			 Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSuficiencia.informatica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionFichaInscripcionForm.
 * Managed Bean que administra las inscripciones de estudiantes a la suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

@SessionScoped
@ManagedBean(name="administracionFichaInscripcionForm")
public class AdministracionFichaInscripcionForm extends HistorialAcademicoForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	//****************************************************************/
	//************************ VARIABLES *****************************/
	//****************************************************************/
	private Usuario afifUsuario;
	private EstudianteJdbcDto afifEstudianteJdbcDto;
	private CarreraDto afifCarreraDto;

	private Integer afifTipoBusqueda;
	private Integer afifActivarReporte;
	private Integer afifCarreraId;
	private String afifIdentificacion;
	private String afifPrimerApellido;
	private String afifNombreArchivo ;
	private String afifNombreReporte;
	
	private List<Carrera> afifListCarrera;
	private List<EstudianteJdbcDto> afifListEstudianteJdbcDto;
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB private MatriculaServicioJdbc servJdbcMatricula;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private UsuarioRolServicio servUsuarioRol;	
	@EJB private CarreraServicio servCarrera;
	@EJB private UsuarioServicio servUsuarioServicio;
	@EJB private FichaInscripcionServicio servFichaInscripcionServicio;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;	

	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	

	public String irListarEstudiantes(Usuario usuario){
		String retorno = null;
		
		try {

			afifUsuario = usuario;
			afifListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXUsuarioXRoles(afifUsuario.getUsrId(), new Integer[]{RolConstantes.ROL_ADMINSUFINFORMATICA_VALUE ,RolConstantes.ROL_SECRESUFICIENCIAS_VALUE});
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				afifListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}

			iniciarParametrosFormEstudiantes();
			retorno = "irBuscarEstudiantesEgresados";
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	public String irInicio(){
		vaciarParametrosFormEstudiantes();
		vaciarParametrosFormCarreras();
		vaciarParametrosFormPeriodos();
		return "irInicio";
	}
	
	public static final int SUFICIENCIA_INFORMATICA_CNCR_ID = 526;

	public String irRegistrarInscripcion(EstudianteJdbcDto estudiante){
		String retorno = null;

		try {
			PeriodoAcademico periodo = servPeriodoAcademico.buscarPeriodo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			if (periodo != null) {
				
				try {
					servFichaInscripcionServicio.crearFichaInscripcionSuficiencia(estudiante.getPrsIdentificacion(), SUFICIENCIA_INFORMATICA_CNCR_ID, RolConstantes.ROL_ESTUD_PREGRADO_VALUE, periodo);
					FacesUtil.mensajeInfo("Inscripción registrada con éxito.");
					retorno = "irInicio";
				} catch (FichaInscripcionDtoValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FichaEstudianteNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FichaInscripcionDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (UsuarioRolNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (UsuarioRolValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
			}

		} catch (PeriodoAcademicoNoEncontradoException e1) {
			FacesUtil.mensajeError("No se encontró un período académico en modalidad Regulares.");
		} catch (PeriodoAcademicoValidacionException e1) {
			FacesUtil.mensajeError("Se encontró mas de un período académico en modalidad Regulares.");
		} catch (PeriodoAcademicoException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}

		return retorno;
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	private void iniciarParametrosFormEstudiantes(){
		afifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		afifTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		afifIdentificacion = new String();
		afifPrimerApellido = new String();
		afifListEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormEstudiantes(){
		afifActivarReporte = null;
		afifTipoBusqueda = null;
		afifIdentificacion = null;
		afifPrimerApellido = null;
		afifListEstudianteJdbcDto = null;
	}
	
	public void limpiarParametrosFormEstudiantes(){
		afifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		afifTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		afifIdentificacion = new String();
		afifPrimerApellido = new String();
		afifListEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormCarreras(){
		afifEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormPeriodos(){
		afifEstudianteJdbcDto = null;
		afifActivarReporte = null;
	}
	
	/**
	 * Método que permite realizar la búsqueda de estudiantes.
	 */
	public void buscarEstudiante(){
		
		if (afifTipoBusqueda.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			String param = null;
			if (afifTipoBusqueda == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = afifIdentificacion;
			}else {
				param = afifPrimerApellido;
			}
			
			List<EstudianteJdbcDto>  estudiantes = cargarEstudianteEgresadoSAIU(param, Arrays.asList(GeneralesConstantes.APP_ID_BASE.toString()), afifTipoBusqueda);
			if (estudiantes != null && estudiantes.size() > 0) {
				afifListEstudianteJdbcDto = estudiantes;
			}else {
				afifListEstudianteJdbcDto = null;
				FacesUtil.mensajeInfo("Registre al estudiante en el SIIU y vuelva a este menú a relizar la inscripción.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar con la búsqueda.");
		}

	}
	
	
	public void busquedaPorIdentificacion(){
		
		if (afifIdentificacion.length() > 0) {
			afifPrimerApellido = new String();
			afifTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (afifPrimerApellido.length() > 0) {
			afifIdentificacion = new String();
			afifTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	



	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getAfifUsuario() {
		return afifUsuario;
	}

	public void setAfifUsuario(Usuario afifUsuario) {
		this.afifUsuario = afifUsuario;
	}

	public EstudianteJdbcDto getAfifEstudianteJdbcDto() {
		return afifEstudianteJdbcDto;
	}

	public void setAfifEstudianteJdbcDto(EstudianteJdbcDto afifEstudianteJdbcDto) {
		this.afifEstudianteJdbcDto = afifEstudianteJdbcDto;
	}

	public CarreraDto getAfifCarreraDto() {
		return afifCarreraDto;
	}

	public void setAfifCarreraDto(CarreraDto afifCarreraDto) {
		this.afifCarreraDto = afifCarreraDto;
	}

	public Integer getAfifTipoBusqueda() {
		return afifTipoBusqueda;
	}

	public void setAfifTipoBusqueda(Integer afifTipoBusqueda) {
		this.afifTipoBusqueda = afifTipoBusqueda;
	}

	public Integer getAfifActivarReporte() {
		return afifActivarReporte;
	}

	public void setAfifActivarReporte(Integer afifActivarReporte) {
		this.afifActivarReporte = afifActivarReporte;
	}

	public Integer getAfifCarreraId() {
		return afifCarreraId;
	}

	public void setAfifCarreraId(Integer afifCarreraId) {
		this.afifCarreraId = afifCarreraId;
	}

	public String getAfifIdentificacion() {
		return afifIdentificacion;
	}

	public void setAfifIdentificacion(String afifIdentificacion) {
		this.afifIdentificacion = afifIdentificacion;
	}

	public String getAfifPrimerApellido() {
		return afifPrimerApellido;
	}

	public void setAfifPrimerApellido(String afifPrimerApellido) {
		this.afifPrimerApellido = afifPrimerApellido;
	}

	public String getAfifNombreArchivo() {
		return afifNombreArchivo;
	}

	public void setAfifNombreArchivo(String afifNombreArchivo) {
		this.afifNombreArchivo = afifNombreArchivo;
	}

	public String getAfifNombreReporte() {
		return afifNombreReporte;
	}

	public void setAfifNombreReporte(String afifNombreReporte) {
		this.afifNombreReporte = afifNombreReporte;
	}

	public List<Carrera> getAfifListCarrera() {
		return afifListCarrera;
	}

	public void setAfifListCarrera(List<Carrera> afifListCarrera) {
		this.afifListCarrera = afifListCarrera;
	}

	public List<EstudianteJdbcDto> getAfifListEstudianteJdbcDto() {
		return afifListEstudianteJdbcDto;
	}

	public void setAfifListEstudianteJdbcDto(List<EstudianteJdbcDto> afifListEstudianteJdbcDto) {
		this.afifListEstudianteJdbcDto = afifListEstudianteJdbcDto;
	}

	


}
