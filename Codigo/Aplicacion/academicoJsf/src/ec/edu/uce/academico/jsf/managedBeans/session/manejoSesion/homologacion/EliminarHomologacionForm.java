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
   
 ARCHIVO:    EliminarHomologacionForm.java	  
 DESCRIPCION: Managed Bean que maneja eliminacion del registro de homologación de un estudiante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
16-ABR-2018			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.homologacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.GratuidadServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RegistroHomologacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) RegistroHomologacionForm. Managed Bean que maneja el
 * registro de las materias homologadas de los estudiantes.
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "eliminarHomologacionForm")
@SessionScoped
public class EliminarHomologacionForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// Objetos
	// PANTALLA LISTAR ESTUDIANTES
	private Usuario erhfUsuario;
	private PersonaDto erhfPersonaDtoBuscar; // Guardar la cedula
	private CarreraDto erhfCarreraDtoBuscar; // Guardar la carrera
	
	private List<CarreraDto> erhfListCarreraDto; // Combo a seleccionar carreras
	private List<RegistroHomologacionDto> erhfListPersonas; // Listado de personas
	private List<FichaMatricula> erhfListaFichaMatricula;
	private RegistroHomologacionDto erhfPersonaDto;
	private Integer erhfValidadorClic;
	private  PeriodoAcademico erhfPeriodoAcademicoActivo;
	private ComprobantePago erhfComprobantePagoHomologacion;  //Comprobante de pago de homologacion
	private Gratuidad erhfGratuidadHomologacion;
	private FichaMatricula erhfFcmtHomologacion; //fichaMatricula Homologacion
	private UsuarioRol erhfUsuarioRol;
	private Rol erhfRol;
	private List<DetalleMatricula> erhfListaDetMatriculaHomologacion;  //Lista de Detalle matricula homologacion
	private List<RecordEstudiante> erhfListaRecordHomologacion;  //Record Estudiante homologacion
	private List<Calificacion> erhfListaCalificacionHomologacion; //Calificaciones de homologación
	private List<SolicitudTerceraMatriculaDto> erhListaSolicitudesTerceraMatricula;
	
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {

	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	// Para la busqueda
	@EJB
	RegistroHomologacionDtoServicioJdbc servErhfRegistroHomologacionDto;
	@EJB
	CarreraDtoServicioJdbc servErhfCarreraDto;
	@EJB
	PeriodoAcademicoServicio servErhfPeriodoAcademico;
	@EJB
	FichaMatriculaDtoServicioJdbc servErhfFichaMatriculaDto;
	@EJB
	FichaMatriculaServicio servErhfFichaMatricula;
	@EJB
	ComprobantePagoServicio servErhfComprobantePago;
	@EJB
	DetalleMatriculaServicio servErhfDetalleMatricula;
	@EJB
	RecordEstudianteServicio servErhfRecordEstudiante;
	@EJB
	GratuidadServicio servErhfGratuidad;
	@EJB
	CalificacionServicio servErhfCalificacion;
	@EJB
	MatriculaServicio servErhfMatriculaServicio;
	@EJB
	UsuarioRolServicio servErhfUsuarioRol;
	@EJB
	RolServicio servErhfRol;
	@EJB
	SolicitudTerceraMatriculaDtoServicioJdbc servErhfSolicitudTerceraMatriculaDto;

	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instanciados al
	 * iniciar la funcionalidad
	 * 
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		return "irInicio";
	}

	/**
	 * Método que permite ir a buscar el estudiante
	 * @param usuario   - el usuario que ingresa a la opción
	 * @return Navegacion a la pagina xhtml de busqueda del estudiante para eliminar hoologación.
	 */

	public String irAlistarEstudEliminarHomologacion(Usuario usuario) {
		erhfUsuario = usuario;
		iniciarParametros();
		return "irAlistarEstEliminarHomologacion";
	}


	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
//		try {
            //Inicio las listas
			erhfListPersonas = new ArrayList<>();
			erhfListCarreraDto = null;
			// Inicio los objetos
			erhfPersonaDtoBuscar = new PersonaDto();
			erhfPersonaDtoBuscar.setPrsIdentificacion("");
			erhfCarreraDtoBuscar = new CarreraDto();
			// seteo la carrera para que busque por todas
			erhfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			// busco el periodo academico activo --solo se usa si le damos la opción de borrar a las secre de carrera solo de pregrado
//			erhfPeriodoAcademicoActivo = servErhfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			// lleno las carreras --solo se usa si le damos la opción de borrar a las secre de carrera
//			erhfListCarreraDto = servErhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPregrado(
//					erhfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE,
//					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, erhfPeriodoAcademicoActivo.getPracId());
			
			erhfValidadorClic = new Integer(0);
//		} catch (CarreraDtoJdbcException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (CarreraDtoJdbcNoEncontradoException e) {
////			FacesUtil.limpiarMensaje();
////			FacesUtil.mensajeError(e.getMessage());
//		} catch (PeriodoAcademicoNoEncontradoException e) {
//			//			FacesUtil.limpiarMensaje();
//		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.iniciar.parametros.periodoActivo.no.encontrado.exception")));
//
//			FacesUtil.mensajeError("No se encontró periodo académico activo y de pregrado ");
//		
//		} catch (PeriodoAcademicoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}

	}

	/**
	 * Método para buscar las personas con los parámetros ingresados en los
	 * filtros de busqueda
	 */
	public void buscar() {
		// buscar el estudiante homologado por carrera y descripción
			erhfListPersonas = null;
			try {
				
				if(erhfPersonaDtoBuscar.getPrsIdentificacion()!=null){
				erhfListPersonas = servErhfRegistroHomologacionDto.listarXEstudiantesXcarreraXIdentificacion(erhfCarreraDtoBuscar.getCrrId(), erhfPersonaDtoBuscar.getPrsIdentificacion());
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.buscar.sin.identificacion.validacion.exception")));
				}
				
			} catch (RegistroHomologacionDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (RegistroHomologacionDtoNoEncontradoException e) {
				//e.printStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		
	}
	
	/**
	 * Método para verificar información antes de eliminar homologación
	 * 
	 */
	public String irVerificaEliminarHomologacion(RegistroHomologacionDto personaDto) {
			
		erhfPersonaDto=personaDto;
		List<FichaMatricula> listaFcmtHomologacionAux= new ArrayList<>();
		erhfListaFichaMatricula= new ArrayList<>(); 
		erhfComprobantePagoHomologacion= null;
		erhfGratuidadHomologacion=null;
		erhfListaDetMatriculaHomologacion = null; //se setea en null
		erhfListaRecordHomologacion=null; //se setea en null
		erhfListaCalificacionHomologacion= new ArrayList<>(); //se setea en null
		erhfRol= null;
		erhfUsuarioRol= null;
		erhfFcmtHomologacion= null;
		erhListaSolicitudesTerceraMatricula =new ArrayList<>();
		
		 List<Rol>listaRolesUsuarioAux = new ArrayList<>();
    	   try {
    		 
    			//BUSCO LA LISTA DE ROLES DEL USUARIO
    			listaRolesUsuarioAux=servErhfRol.listarRolesXUsrId(erhfUsuario.getUsrId());	
    			if(listaRolesUsuarioAux!=null){
    				boolean rolEncontrado= false;
    			  //BUSCO EL ROL DE SECRETARIA DE CARRERA
    				for (Rol rol : listaRolesUsuarioAux) {
    				if(rol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE){
    					
    					try {
    						erhfRol=servErhfRol.buscarPorId(rol.getRolId());
    					} catch (RolNoEncontradoException e) {
    						FacesUtil.limpiarMensaje();
    						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.rol.no.encontrado.exception")));
    						return null;
    					} catch (RolException e) {
    						FacesUtil.limpiarMensaje();
    						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.rol.exception")));
    						return null;
    					}
    					rolEncontrado = true;
    					break;
    				}
    			}
    			
    			
    			if(!rolEncontrado){
    				FacesUtil.limpiarMensaje();
    				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.rol.no.encontrado.validacion.exception")));
    				return null;
    			  }
    			
    			}else{
    				FacesUtil.limpiarMensaje();
    				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.ningun.rol.validacion.exception")));
    				return null;
    				
    			}
    		   
    		//Usuario Rol de persona que realiza el proceso   
   		   erhfUsuarioRol= servErhfUsuarioRol.buscarXUsuarioXrol(erhfUsuario.getUsrId(), erhfRol.getRolId());
    		   
    		// erhfListaFichaMatriculaDto= servErhfFichaMatriculaDto.ListarTodasXidPersonaXcarrera(erhfPersonaDto.getPrsId(), erhfPersonaDto.getCrrId());
    		   //LISTA DE FICHAS MATRICULAS POR FICHA ESTUDIANTE, PUEDEN EXISTIR VARIAS
    	erhfListaFichaMatricula= servErhfFichaMatricula.ListarFichaMatriculaXfcesId(erhfPersonaDto.getFcesId());  
    	
		Boolean encontrado = false;
		for (FichaMatricula fichaMatricula : erhfListaFichaMatricula) {
			if(fichaMatricula.getFcmtPracId()!= PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE){
				encontrado= true;
				break;
			}
				
			
		}
		
		if(encontrado==false){
			
			//BUSCO LA FICHA MATRICULA HOMOLOG
			for (FichaMatricula fichaMatricula : erhfListaFichaMatricula) {
				if(fichaMatricula.getFcmtPracId()== PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE){
					listaFcmtHomologacionAux.add(fichaMatricula);
				}
			}
			
		if((listaFcmtHomologacionAux== null)||(listaFcmtHomologacionAux.size()<=0)){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("El estudiante no tiene fcmt homologacion");	
			return null;
		  }else	if(listaFcmtHomologacionAux.size()==1)	{
			  
			//Verifico que no haya realizado solicitudes de tercera matrícula  
		  erhListaSolicitudesTerceraMatricula=servErhfSolicitudTerceraMatriculaDto.listarSolicitudesxFcesId(erhfPersonaDto.getFcesId());
				 
		if((erhListaSolicitudesTerceraMatricula==null)||(erhListaSolicitudesTerceraMatricula.size()<=0)){
			  
			  erhfFcmtHomologacion = listaFcmtHomologacionAux.get(0);
			  //BUSCO COMPROBANTE HOMOLOGACION POR FCMT HOMOLOG
			  erhfComprobantePagoHomologacion= servErhfComprobantePago.buscarPorFcmt(erhfFcmtHomologacion.getFcmtId());
			 //BUSCO GRATUIDAD POR FICHA MATRIUCULA HOMOLOG
			erhfGratuidadHomologacion=servErhfGratuidad.buscarGratuidadXFichaMatricula(erhfFcmtHomologacion.getFcmtId());
			//BUSCO EL DETALLE MATRICULA POR EL COMPROBANTE DE PAGO HOMOLOG, puede ser null
			erhfListaDetMatriculaHomologacion = servErhfDetalleMatricula.listarXCmpaId(erhfComprobantePagoHomologacion.getCmpaId());
			//BUSCO LOS RECORD ESTUDIANTE POR  FICHA ESTUDIANTE, puede ser null
			erhfListaRecordHomologacion=servErhfRecordEstudiante.listarXfcesId(erhfPersonaDto.getFcesId());
			//NO ES NECESARIO BORRAR SOLICITUDES PUES SE MANTIENE LA FICHA ESTUDIANTE
			
			
		
			//CREO LISTA DE CALIFICACIONES, puede ser null
		if((erhfListaRecordHomologacion!=null)&&(erhfListaRecordHomologacion.size())>0){
			for (RecordEstudiante itemRecord : erhfListaRecordHomologacion) {
				Calificacion calificacionAux = null;
				calificacionAux= servErhfCalificacion.buscarPorRces(itemRecord.getRcesId());
				
				if(calificacionAux!=null){
					erhfListaCalificacionHomologacion.add(calificacionAux);
				}
							
			}
			
			
			
		}else{
			
			erhfListaCalificacionHomologacion = null;
		}
			
//       	if(personaDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
			
			erhfValidadorClic = 1;  // Se habilita modal borrar

//		    }else{
//		    	erhfValidadorClic = 0;
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("Al momento solo se puede eliminar homologaciones manuales realizadas por idiomas");
//		    	
//		    }
			
		   }else{
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.tercera.matricula.realizada.validacion.exception")));

			}
			
			
		   }else{
			   erhfValidadorClic = 0;
			   FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.varias.fcmt.homologacion.validacion.exception")));
		   }
			
			
			
		}else{
			erhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.estudiante.matriculado.validacion.exception")));
		}
		
		
    	} catch (FichaMatriculaException e) {
   			e.printStackTrace();
   			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
  		} 
   			catch (FichaMatriculaNoEncontradoException e) {
			//e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.fcmt.no.encontrado.exception")));
		} 
    	   catch (ComprobantePagoNoEncontradoException e) {
    		FacesUtil.limpiarMensaje();
      		FacesUtil.mensajeError(e.getMessage());
		} catch (ComprobantePagoException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		}  catch (DetalleMatriculaException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (GratuidadNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (GratuidadException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (CalificacionException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
   			FacesUtil.mensajeError(e.getMessage());
		} catch (SolicitudTerceraMatriculaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.irVerificar.eliminar.homologacion.tercera.matricula.exception")));
		} 
		return null;
	
	}

	/**
	 * Método para inicar el registro del notas de la materias homologadas del estudiante en BDD.
	 * @return navegación a la pagina xhtml. Listar estudiantes a homologar.
	 */
	public String eliminarHomologacion() {
	
			erhfValidadorClic = new Integer(0);
			
			try {
		if(servErhfMatriculaServicio.eliminarHomologacion(erhfPersonaDto.getFcesId(),erhfPersonaDto.getFcinId(), erhfFcmtHomologacion.getFcmtId(),erhfGratuidadHomologacion.getGrtId(),erhfComprobantePagoHomologacion.getCmpaId(), erhfListaDetMatriculaHomologacion, erhfListaRecordHomologacion, erhfListaCalificacionHomologacion, erhfUsuarioRol)){
	
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.eliminar.homologacion.exito")));
				}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.eliminar.homologacion.sin.exito")));
				
				}
				
			} catch (MatriculaException e) {
				// validador click en 0
				erhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EliminarHomologacion.eliminar.homologacion.exception")));
			}
			iniciarParametros();
		return "irAlistarEstudianteHomologacion";
	}


	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	public void limpiar(){
		iniciarParametros();
		
	}

	
		public void desactivaModalEliminar(){
			erhfValidadorClic = 0;
		}
		
	
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getErhfUsuario() {
		return erhfUsuario;
	}

	public void setErhfUsuario(Usuario erhfUsuario) {
		this.erhfUsuario = erhfUsuario;
	}

	public PersonaDto getErhfPersonaDtoBuscar() {
		return erhfPersonaDtoBuscar;
	}

	public void setErhfPersonaDtoBuscar(PersonaDto erhfPersonaDtoBuscar) {
		this.erhfPersonaDtoBuscar = erhfPersonaDtoBuscar;
	}

	public List<CarreraDto> getErhfListCarreraDto() {
		erhfListCarreraDto = erhfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : erhfListCarreraDto;
		return erhfListCarreraDto;
	}

	public void setErhfListCarreraDto(List<CarreraDto> erhfListCarreraDto) {
		this.erhfListCarreraDto = erhfListCarreraDto;
	}

	public CarreraDto getErhfCarreraDtoBuscar() {
		return erhfCarreraDtoBuscar;
	}

	public void setErhfCarreraDtoBuscar(CarreraDto erhfCarreraDtoBuscar) {
		this.erhfCarreraDtoBuscar = erhfCarreraDtoBuscar;
	}

	public Integer getErhfValidadorClic() {
		return erhfValidadorClic;
	}

	public void setErhfValidadorClic(Integer erhfValidadorClic) {
		this.erhfValidadorClic = erhfValidadorClic;
	}

	public List<RegistroHomologacionDto> getErhfListPersonas() {
		erhfListPersonas = erhfListPersonas == null ? (new ArrayList<RegistroHomologacionDto>()) : erhfListPersonas;
		return erhfListPersonas;
	}

	public void setErhfListPersonas(List<RegistroHomologacionDto> erhfListPersonas) {
		this.erhfListPersonas = erhfListPersonas;
	}

	
	public List<FichaMatricula> getErhfListaFichaMatricula() {
		erhfListaFichaMatricula = erhfListaFichaMatricula == null ? (new ArrayList<FichaMatricula>()) : erhfListaFichaMatricula;
		return erhfListaFichaMatricula;
	}

	public void setErhfListaFichaMatricula(List<FichaMatricula> erhfListaFichaMatricula) {
		this.erhfListaFichaMatricula = erhfListaFichaMatricula;
	}

	public PeriodoAcademico getErhfPeriodoAcademicoActivo() {
		return erhfPeriodoAcademicoActivo;
	}

	public void setErhfPeriodoAcademicoActivo(PeriodoAcademico erhfPeriodoAcademicoActivo) {
		this.erhfPeriodoAcademicoActivo = erhfPeriodoAcademicoActivo;
	}

	public ComprobantePago getErhfComprobantePagoHomologacion() {
		return erhfComprobantePagoHomologacion;
	}

	public void setErhfComprobantePagoHomologacion(ComprobantePago erhfComprobantePagoHomologacion) {
		this.erhfComprobantePagoHomologacion = erhfComprobantePagoHomologacion;
	}

	public List<RecordEstudiante> getErhfListaRecordHomologacion() {
		erhfListaRecordHomologacion = erhfListaRecordHomologacion == null ? (new ArrayList<RecordEstudiante>()) : erhfListaRecordHomologacion;	
		return erhfListaRecordHomologacion;
	}

	public void setErhfListaRecordHomologacion(List<RecordEstudiante> erhfListaRecordHomologacion) {
		this.erhfListaRecordHomologacion = erhfListaRecordHomologacion;
	}

	public List<DetalleMatricula> getErhfListaDetMatriculaHomologacion() {
		erhfListaDetMatriculaHomologacion = erhfListaDetMatriculaHomologacion == null ? (new ArrayList<DetalleMatricula>()) : erhfListaDetMatriculaHomologacion;	
		return erhfListaDetMatriculaHomologacion;
	}

	public void setErhfListaDetMatriculaHomologacion(List<DetalleMatricula> erhfListaDetMatriculaHomologacion) {
		this.erhfListaDetMatriculaHomologacion = erhfListaDetMatriculaHomologacion;
	}

	public Gratuidad getErhfGratuidadHomologacion() {
		return erhfGratuidadHomologacion;
	}

	public void setErhfGratuidadHomologacion(Gratuidad erhfGratuidadHomologacion) {
		this.erhfGratuidadHomologacion = erhfGratuidadHomologacion;
	}

	public List<Calificacion> getErhfListaCalificacionHomologacion() {
		erhfListaCalificacionHomologacion = erhfListaCalificacionHomologacion == null ? (new ArrayList<Calificacion>()) : erhfListaCalificacionHomologacion;
		return erhfListaCalificacionHomologacion;
	}

	public void setErhfListaCalificacionHomologacion(List<Calificacion> erhfListaCalificacionHomologacion) {
		this.erhfListaCalificacionHomologacion = erhfListaCalificacionHomologacion;
	}

	public FichaMatricula getErhfFcmtHomologacion() {
		return erhfFcmtHomologacion;
	}

	public void setErhfFcmtHomologacion(FichaMatricula erhfFcmtHomologacion) {
		this.erhfFcmtHomologacion = erhfFcmtHomologacion;
	}

	public UsuarioRol getErhfUsuarioRol() {
		return erhfUsuarioRol;
	}

	public void setErhfUsuarioRol(UsuarioRol erhfUsuarioRol) {
		this.erhfUsuarioRol = erhfUsuarioRol;
	}

	public Rol getErhfRol() {
		return erhfRol;
	}

	public void setErhfRol(Rol erhfRol) {
		this.erhfRol = erhfRol;
	}

	public RegistroHomologacionDto getErhfPersonaDto() {
		return erhfPersonaDto;
	}

	public void setErhfPersonaDto(RegistroHomologacionDto erhfPersonaDto) {
		this.erhfPersonaDto = erhfPersonaDto;
	}

	public List<SolicitudTerceraMatriculaDto> getErhListaSolicitudesTerceraMatricula() {
		erhListaSolicitudesTerceraMatricula = erhListaSolicitudesTerceraMatricula == null ? (new ArrayList<SolicitudTerceraMatriculaDto>()) : erhListaSolicitudesTerceraMatricula;
		return erhListaSolicitudesTerceraMatricula;
	}

	public void setErhListaSolicitudesTerceraMatricula(
			List<SolicitudTerceraMatriculaDto> erhListaSolicitudesTerceraMatricula) {
		this.erhListaSolicitudesTerceraMatricula = erhListaSolicitudesTerceraMatricula;
	}

	
}
