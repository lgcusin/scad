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
   
 ARCHIVO:    RegistroManualHomologacionForm.java	  
 DESCRIPCION: Managed Bean que maneja el registro manual de homologación de un estudiante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
15-OCT-2018			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.registro;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NotaCorteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RegistroAutomaticoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RegistroHomologacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.NotaCorte;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) RegistroManualHomologacionForm. Managed Bean que maneja el
 * registro  manual de cambios, reingresos , reinicios, segunda carrera, reingrasos cambios de carrera por rediseño.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "registroManualHomologacionForm")
@SessionScoped
public class RegistroManualHomologacionForm extends HistorialAcademicoForm implements Serializable  {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	//BUSCAR ESTUDIANTE
	private Usuario rmhfUsuario;
	private Integer tipoUsuario;
	private PersonaDto rmhfPersonaDtoBuscar; // Guardar la cedula
	private CarreraDto rmhfCarreraDtoBuscar; // Guardar la carrera
	private Persona rmhfPersonaVer;  //Objeto persona para visualizar información del estudiante
	private List<CarreraDto> rmhfListCarreraDto; // Combo a seleccionar carreras: CARRERAS A LAS QUE TIENE ACCESO EL USUARIO
	private List<RegistroHomologacionDto> rmhfListFichasInscripcion; // Listado de personas
	private Integer rmhfValidadorClic;  //habilita modales
	private Integer rmhfTipoIngresoFcinNueva;
	private RegistroHomologacionDto rmhfPersonaDto;
	private PeriodoAcademico rmhfPeriodoAcademicoActivo;
	private PeriodoAcademico rmhfPeriodoAcademicoCierre;
	private UsuarioRol rmhfUsuarioRol;
	private Rol rmhfRol;
	private Boolean rmhfDisabledAgregar; //deshabilita botón Agregar
	private Boolean rmhfDisabledNuevo;  //deshabilita boton Nuevo
	private Boolean rmhfRenderPanelInfo; //presenta o oculta
	private Boolean rmhfRenderPanelResultado;
	private Boolean rmhfRenderCarreras;  
	private List<CarreraDto> rmhfListCarreraDtoSau; //Lista de carreras SAU
	private Boolean rmhfRenderCarrerasSau; //presenta lista de carreras en las que estuvo registrado el estudiante en el sistema SAU
	private String rmhfNombrePersonaSau;
	private String rmhfIdentificacionPersonaSau;
	private Boolean rmhfChkNotaEnes;  //Check  habilita o deshabilita ingreso de nota
	private Boolean rmhfDisabledAplicaEnes;
	private Boolean rmhfDisabledNotaEnes; //habilita check y campo Nota Enes
	private NotaCorte rmhfNotaCorte;
		
	
	//MODAL AGREGAR FICHA INSCRIPCION
	private List<Carrera> rmhfListaCarrera;  //Lista de carreras validas DE ACUERDO A LA DEPENDENCIA SELECCIONADA
	private RegistroHomologacionDto rmhfPersonaDtoAgregarRegistro;
	private List<Dependencia> rmhfListaDependencia;
	private String rmhfDetalleTipoIngreso;
	private Boolean rmhfDisabledGuardarAgregar;
	
	//CREAR ESTUDIANTE NUEVO
	private RegistroHomologacionDto rmhfPersonaDtoNuevo;
	private Integer rmhfValidadorNuevoClic; 
	
	//ElIMINAR FICHA INSCRIPCION
	private RegistroHomologacionDto rmhfPersonaDtoEliminar;
	private FichaEstudiante rmhfFichaEstudianteExistente;
	private Integer rmhfValidadorEliminarClic;
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {

	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	// Servicios para la busqueda
	@EJB
	PersonaServicio servRmhfPersona;
	@EJB
	RegistroHomologacionDtoServicioJdbc servRmhfRegistroHomologacionDto;
	@EJB
	CarreraDtoServicioJdbc servRmhfCarreraDto;
	@EJB
	PeriodoAcademicoServicio servRmhfPeriodoAcademico;
	@EJB
	UsuarioRolServicio servRmhfUsuarioRol;
	@EJB
	RolServicio servRmhfRol;
	@EJB
	FichaInscripcionDtoServicioJdbc servRmhfFichaInscripcionDto;
	@EJB
	RegistroAutomaticoServicio servRmhfRegistroAutomatico;
	@EJB
	DependenciaServicio servRmhfDependencia;
	@EJB
	CarreraServicio servRmhfCarrera;
	@EJB
	RecordEstudianteDtoServicioJdbc servRmhfRecordEstudianteSIIU;
	@EJB
	MatriculaServicioJdbc servRmhfRecordEstudianteSAU;
	@EJB 
	ConfiguracionCarreraServicio servRmhfConfiguracionCarrera;
	@EJB
	FichaMatriculaDtoServicioJdbc servRmhfFichaMatriculaJdbc;
	@EJB
	FichaEstudianteServicio servRmhfFichaEstudiante;
	@EJB
	NotaCorteServicio servRmhfNotaCorte;
	@EJB
	CronogramaDtoServicioJdbc servRmhfCronogramaJdbc;
	

	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instanciados al
	 * iniciar la funcionalidad
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		return "irInicio";
	}

	/**
	 * Método que permite ir a buscar el estudiante para registrar la movilidad
	 * @param usuario   - el usuario que ingresa a la opción de registrar movilidad estudiantil
	 * @return Navegacion a la pagina xhtml de busqueda del estudiante para registrar la movilidad estudiantil.
	 */
	public String irRegistroManualHomologacion(Usuario usuario) {
		rmhfUsuario = usuario;
		iniciarParametros();
		return "irRegistroManualHomologacion";
	}


	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
		try {
            //Inicio las listas
			rmhfListFichasInscripcion = new ArrayList<>();
			rmhfListCarreraDto = null;
			// Inicio los objetos
			rmhfPersonaDtoBuscar = new PersonaDto();
			rmhfPersonaDtoBuscar.setPrsIdentificacion("");
			rmhfCarreraDtoBuscar = new CarreraDto();
			// seteo la carrera para que busque por todas
			rmhfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			rmhfPersonaVer= new Persona();
			rmhfTipoIngresoFcinNueva = GeneralesConstantes.APP_ID_BASE;
	        rmhfValidadorClic = 0;
	        rmhfValidadorNuevoClic=0;
			rmhfDisabledAgregar= true; //disabled
			rmhfDisabledNuevo = true;  //disables
			rmhfRenderPanelInfo= false; // rendered
			rmhfRenderPanelResultado= false;  //rendered
			 rmhfRenderCarrerasSau= false;
			// Campos modal Agregar
			 rmhfListaCarrera= new ArrayList<>();  //Lista de carreras validas pregrado
			 rmhfPersonaDtoAgregarRegistro = new RegistroHomologacionDto();
			 rmhfListaDependencia= new ArrayList<>();
			 //Campos nuevo estudiante
			 rmhfPersonaDtoNuevo= new RegistroHomologacionDto();
			 rmhfRenderCarreras = false;
			 rmhfRol= null;
			 rmhfUsuarioRol= null;
			 rmhfDisabledNotaEnes = true;
			 rmhfDisabledAplicaEnes = true;
			 rmhfChkNotaEnes = false;
			
			 
			 List<Rol>listaRolesUsuarioAux = new ArrayList<>();
			
        // busco el periodo academico activo --
		rmhfPeriodoAcademicoActivo = servRmhfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		
		// Busco el periodo academico en cierre
		rmhfPeriodoAcademicoCierre = servRmhfPeriodoAcademico.buscarPeriodoEnCierre();
		
		//BUSCO LA LISTA DE ROLES DEL USUARIO
		listaRolesUsuarioAux=servRmhfRol.listarRolesXUsrId(rmhfUsuario.getUsrId());	
		
	      if(listaRolesUsuarioAux!=null){
			boolean rolEncontrado= false;
		  //BUSCO EL ROL DE SECRETARIA  DE CARRERA O SOPORTE
			for (Rol rol : listaRolesUsuarioAux) {
			    if(rol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE
			    	||	rol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE
			    	){
						rmhfRol=servRmhfRol.buscarPorId(rol.getRolId());
				rolEncontrado = true;
				break;
			   }
	    	}
		
			if(rolEncontrado){
				rmhfUsuarioRol= servRmhfUsuarioRol.buscarXUsuarioXrol(rmhfUsuario.getUsrId(), rmhfRol.getRolId());
				if(rmhfRol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE){
					//Deshabilito  combo de carreras
					rmhfRenderCarreras= false;
					tipoUsuario= RolConstantes.ROL_SOPORTE_VALUE; 
					
					
					
				}else{ //SECRETARIA DE CARRERA
					
					rmhfRenderCarreras= true;
					//Lista de carreras a las que tiene acceso la secretaria
					rmhfListCarreraDto = servRmhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(rmhfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE,
                            RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rmhfPeriodoAcademicoActivo.getPracId());
					tipoUsuario= RolConstantes.ROL_SECRECARRERA_VALUE; 
				}
				
				
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.iniciar.parametros.rol.no.encontrado.validacion.exception")));
			}
		
	      }else{
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.iniciar.parametros.usuario.sin.roles.validacion.exception")));
				
		   }		
			
	
		} catch (PeriodoAcademicoNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.iniciar.parametros.periodo.academico.no.econtrado.exception")));
	
		
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}catch (RolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.buscar.rol.no.encontrado.exception")));
    	} catch (RolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.buscar.rol.exception")));
       } catch (CarreraDtoJdbcException e) {
    	   FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  catch (UsuarioRolException e) {
     	   FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		   } catch (UsuarioRolNoEncontradoException e) {
			   FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
		   }

	}

	/**
	 * Método para buscar las personas con los parámetros ingresados en los
	 * filtros de busqueda
	 */
	public void buscar() {
		// buscar el estudiante homologado por carrera y descripción
		rmhfListFichasInscripcion = null;
		rmhfDisabledAgregar= true;
		rmhfDisabledNuevo = true;
		rmhfRenderPanelInfo= false; // render
		rmhfRenderPanelResultado= false;  //render
		rmhfPersonaVer= new Persona();
		rmhfListFichasInscripcion= new ArrayList<>();
		rmhfValidadorClic = 0;
	    rmhfValidadorNuevoClic= 0; 
		rmhfValidadorEliminarClic = 0;
		rmhfDisabledGuardarAgregar = false;
		rmhfRenderCarrerasSau = false;
		
		try {
		
		if((rmhfPersonaDtoBuscar.getPrsIdentificacion()!=null)&& (rmhfPersonaDtoBuscar.getPrsIdentificacion().trim().length()>0)){
			rmhfListCarreraDtoSau= new ArrayList<>();
			List<RecordEstudianteDto> historialSauEstudiante=cargarHistorialAcademicoSAIUHomologado(rmhfPersonaDtoBuscar.getPrsIdentificacion());
		if(historialSauEstudiante!=null){
			for (RecordEstudianteDto recordEstudianteDto : historialSauEstudiante) {
				
				boolean encontrado = false;
				for (CarreraDto itemDto : rmhfListCarreraDtoSau) {
					if(recordEstudianteDto.getRcesCarreraDto().getCrrId()==itemDto.getCrrId()){
						encontrado= true;
						break;
					}
					
				}
				
				if(!encontrado){
					if(recordEstudianteDto.getRcesOrigen()==RecordEstudianteConstantes.RCES_ORIGEN_SAU){
					rmhfListCarreraDtoSau.add(recordEstudianteDto.getRcesCarreraDto());
					}
				}
				
			}
			
			if(rmhfListCarreraDtoSau.size()>0){
				
				rmhfNombrePersonaSau=historialSauEstudiante.get(0).getRcesEstudianteDto().getPrsNombres();
				rmhfIdentificacionPersonaSau=historialSauEstudiante.get(0).getRcesEstudianteDto().getPrsIdentificacion();
				rmhfRenderCarrerasSau =true;
				
			}else{
				rmhfRenderCarrerasSau = false;
				
			}
			
			
			
		}else{
			rmhfRenderCarrerasSau = false;
			
		}
			
							//Busco la persona por identificación
							rmhfPersonaVer= servRmhfPersona.buscarPorIdentificacion(rmhfPersonaDtoBuscar.getPrsIdentificacion().trim());
					
							// 1.-PRESENTO FICHAS INSCRIPCION DEL ESTUDIANTE SOLO DE LAS CARRERAS  A LAS QUE EL USER TIENE ACCESO
//				if(rmhfRol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE)	{
//						
//							//Busco lista de fcin por identificación
//							rmhfListFichasInscripcion = servRmhfRegistroHomologacionDto.listarXEstudianteXIdentificacion(rmhfPersonaDtoBuscar.getPrsIdentificacion().trim());
//							
//                  }else{ //Secretaria de carrera
//							
//							rmhfListFichasInscripcion= servRmhfRegistroHomologacionDto.listarXEstudiantesXListaCarreraXIdentificacion(rmhfListCarreraDto, rmhfCarreraDtoBuscar.getCrrId(), rmhfPersonaDtoBuscar.getPrsIdentificacion().trim());
//							
//				}
							
							//2.- PRESNTO TODAS LAS FICHAS INSCRIPCION DEL ESTUDIANTE
							
							
					rmhfListFichasInscripcion = servRmhfRegistroHomologacionDto.listarXEstudianteXIdentificacion(rmhfPersonaDtoBuscar.getPrsIdentificacion().trim());
							
					
					if(rmhfRol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE)	{
												
						for (RegistroHomologacionDto registroDto : rmhfListFichasInscripcion) {
							// La ficha inscripción tiene que esta inactiva y vigente para permitir realizar un reingreso 
							
							
							
							    if((registroDto.getFcinVigente()==FichaInscripcionConstantes.VIGENTE_VALUE)&&(registroDto.getFcinEstado()==FichaInscripcionConstantes.INACTIVO_VALUE)){
								   registroDto.setDeshabilitaReingreso(false); //se habilita la opción reingreso
							      }else{
								registroDto.setDeshabilitaReingreso(true);  //se deshabilita la opción reingreso
							   }
							
							 //***********BUSCAR FICHA MATRICULA EN PERIODO ANTERIOR O ACTIVO
//							   List<FichaMatriculaDto> listaFichaMatriculaAux = new ArrayList<>();
//							   if(rmhfPeriodoAcademicoCierre!= null){ //Si existe periodo en cierre buscamos en periodo en cierre
//								
//								 listaFichaMatriculaAux =servRmhfFichaMatriculaJdbc.listarXPeriodoXidentificacionXcarreraNull(rmhfPeriodoAcademicoCierre.getPracId(), rmhfPersonaVer.getPrsIdentificacion(), registroDto.getCrrId());
//								  
//							  }else{ //buscamos en periodo activo
//								  
//									 listaFichaMatriculaAux =servRmhfFichaMatriculaJdbc.listarXPeriodoXidentificacionXcarreraNull(rmhfPeriodoAcademicoActivo.getPracId(), rmhfPersonaVer.getPrsIdentificacion(), registroDto.getCrrId());
//
//								  
//							  }
						
//							   //Si NO existe matricula en periodo en cierrer o activo 
//							 if(((listaFichaMatriculaAux== null)||(listaFichaMatriculaAux.size()<=0))&&(registroDto.getFcinVigente()==FichaInscripcionConstantes.VIGENTE_VALUE)){
//									registroDto.setDeshabilitaReingreso(false); //habilito opcion de reingreso
//									
//								}else{//Si existe matricula en periodo activo o en cierre
//									
//									registroDto.setDeshabilitaReingreso(true);	 //deshabilito opcion de reingreso
//								}
							  //******************** 
						
						}
						
						
					}else{//Secretaria de carrera
						
						//BUSCO LAS FICHAS INSCRIPCION DEL ESTUDIANTE QUE ESTAN EN LAS CARRERAS A LAS QUE TIENE ACCESO LA SECRETARIA, PUES SOLO PUEDE ACTIVAR REINGRESO DE CARRERAS QUE ESTA AUTORIZADA
						for (RegistroHomologacionDto registroDto : rmhfListFichasInscripcion) {
							// La ficha inscripción tiene que esta inactiva y vigente para permitir realizar un reingreso 
							boolean encontrado = false;
							for (CarreraDto carreraDto : rmhfListCarreraDto) {
								 if(carreraDto.getCrrId()==registroDto.getCrrId()){
									 encontrado = true;
									 break;
								 }else{
									encontrado= false;
								 }
							}
							
							if(encontrado){//Solo si encontro una carrera que la secretaria tiene permiso procedo
							if((registroDto.getFcinVigente()==FichaInscripcionConstantes.VIGENTE_VALUE)&&(registroDto.getFcinEstado()==FichaInscripcionConstantes.INACTIVO_VALUE)){
								   registroDto.setDeshabilitaReingreso(false); //se habilita la opción reingreso
							      }else{
								registroDto.setDeshabilitaReingreso(true);  //se deshabilita la opción reingreso
							   }
							
							if(registroDto.getFcinTipo()==FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE){
								   registroDto.setDeshabilitaEliminar(false); //se habilita la opción eliminar
							      }else{
								registroDto.setDeshabilitaEliminar(true);  //se deshabilita la opción eliminar
							   }
							
							
							
							//***********BUSCAR FICHA MATRICULA EN PERIODO ANTERIOR O ACTIVO
//							   List<FichaMatriculaDto> listaFichaMatriculaAux = new ArrayList<>();
//							   if(rmhfPeriodoAcademicoCierre!= null){ //Si existe periodo en cierre buscamos en periodo en cierre
//								
//								 listaFichaMatriculaAux =servRmhfFichaMatriculaJdbc.listarXPeriodoXidentificacionXcarreraNull(rmhfPeriodoAcademicoCierre.getPracId(), rmhfPersonaVer.getPrsIdentificacion(), registroDto.getCrrId());
//								  
//							  }else{ //buscamos en periodo activo
//								  
//									 listaFichaMatriculaAux =servRmhfFichaMatriculaJdbc.listarXPeriodoXidentificacionXcarreraNull(rmhfPeriodoAcademicoActivo.getPracId(), rmhfPersonaVer.getPrsIdentificacion(), registroDto.getCrrId());
//
//								  
//							  }
								
							   //Si NO existe matricula en periodo en cierrer o activo 
//							 if(((listaFichaMatriculaAux== null)||(listaFichaMatriculaAux.size()<=0))&&(registroDto.getFcinVigente()==FichaInscripcionConstantes.VIGENTE_VALUE)){
//									registroDto.setDeshabilitaReingreso(false); //habilito opcion de reingreso
//									
//								}else{//Si existe matricula en periodo activo o en cierre
//									
//									registroDto.setDeshabilitaReingreso(true);	 //deshabilito opcion de reingreso
//								}
							  //******************** 
							
							}else{
								registroDto.setDeshabilitaReingreso(true);  //se deshabilita la opción reingreso pues no tiene permisos sobre la carrera para permitir reingreso
								registroDto.setDeshabilitaEliminar(true);  //se deshabilita la opción eliminar
							}
							
							    
						   }
						
						
					}
						
						rmhfRenderPanelInfo= true; // render- se presenta el panel de información personal
						rmhfRenderPanelResultado= true;  //render   - se presenta el panel de fichas inscripción
						rmhfDisabledAgregar=false; //Se habilita el botón agregar
						
						
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.buscar.validacion.identificacion")));
				}
						
	
			} catch (RegistroHomologacionDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (RegistroHomologacionDtoNoEncontradoException e) {
				rmhfRenderPanelInfo= true;       // render
				rmhfRenderPanelResultado= true;  //render 
				rmhfDisabledAgregar=false;       //Se habilita el botón agregar sino encuentra fichas inscripción
				FacesUtil.limpiarMensaje();
				//FacesUtil.mensajeError(e.getMessage());
				FacesUtil.mensajeInfo("El estudiante no cuenta con registro de carrera alguna en el sistema académico SIIU.");
							
			} catch (PersonaNoEncontradoException e) {
				rmhfDisabledNuevo = false;  //Se habilita el botón nuevo si no existe la persona
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.buscar.persona.no.encontrado.exception")));
			
			} catch (PersonaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}  
			
	}
	
	
	/**
	 * Método para verificar información antes de generar la ficha inscripción de tipoIngreso reingreso
	 * @return null
	 * @param personaDto.- objeto con información para verificar la información antes de realizar el guardado
	 */
	public String irVerificaReingreso(RegistroHomologacionDto personaDto) {
			
		rmhfPersonaDto=personaDto; //Persona seleccionada para crear reingreso
		desactivaModalReingreso();  //Inicializa validador modal en desactivado
		rmhfTipoIngresoFcinNueva= GeneralesConstantes.APP_ID_BASE;
	
		
			 try { 
			
			//Busco ficha inscripcion creada en este periodo en la carrera  si existe no vuelvo a crear
			FichaInscripcionDto auxFichaInscripcionPeriodoActivo= new FichaInscripcionDto();
			auxFichaInscripcionPeriodoActivo=servRmhfFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraxPeriodo(rmhfPersonaDto.getPrsIdentificacion(), rmhfPersonaDto.getFcinCarreraSiiu(), rmhfPeriodoAcademicoActivo.getPracId());
			
    		  if(auxFichaInscripcionPeriodoActivo==null){
    			  rmhfTipoIngresoFcinNueva = FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE;  
    		     activaModalReingreso();  // Se habilita modal crear reingreso
    		  }else{
    			  FacesUtil.limpiarMensaje();
  				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificaReingreso.validacion.fichaInscripcion.exception")));
    		  }
		
    		  
			 }  catch (FichaInscripcionDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificaReingreso.fichaInscripcion.exception")));
			}
    	 
		return null;
	
	}
	
	
	/**
	 * Método para verificar información antes de eliminar la ficha inscripcion con estado PENDEIENTE HOMOLOGAR
	 * @return null
	 * @param personaDto.- objeto con información para verificar la información antes de realizar la eliminación
	 */
	public String irVerificaEliminar(RegistroHomologacionDto personaDto) {
			
		rmhfPersonaDtoEliminar=personaDto; //Persona seleccionada para crear reingreso
		boolean listoEliminar= false;
		
		try {	
			
		if(rmhfPersonaDtoEliminar.getFcinTipo()==FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE){
			rmhfFichaEstudianteExistente = servRmhfFichaEstudiante.buscarPorFcinIdNueva(rmhfPersonaDtoEliminar.getFcinId());
			
			if(rmhfFichaEstudianteExistente==null){ //NO EXISTE FICHA ESTUDIANTE
				listoEliminar= true;
			
			}else{
				if((rmhfPersonaDtoEliminar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE) 
						//||(rmhfPersonaDtoEliminar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE) 
					||	(rmhfPersonaDtoEliminar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)){
					
					listoEliminar= true;
					
				}else{
					//OTROS TIPOS DE INGRESO NO DEBEN TENER FICHA ESTUDIANTE, PUES (C.CARRERA, C.UNIVERSIDAD, SEG. CARRERA, C. CARRERA O REING. POR REDISEÑO)  DEBEN SER NUEVOS ESTUDIANTES A CARRERAS.
					 listoEliminar= false;
				}
				
			}
			
			
			if(listoEliminar){
				//llamar a eliminar solo si no tiene fces o tiene fces y son reingresos o reinicios y aun no se homologa
				rmhfValidadorEliminarClic = 1;
			}else{
				FacesUtil.limpiarMensaje();
  				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificaEliminar.ficha.estudiante.existente.validacion.exception")));
			}
			
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificaEliminar.tipo.pendiente.homologar.validacion.exception")));
			
		}
		
		} catch (FichaEstudianteException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificaEliminar.ficha.estudiante.exception")));
		} 
		    	 
		return null;
	
	}
	
	/**
	 * Método para eliminar la ficha inscripción de la BDD.
	 */
	public void eliminarFichaInscripcion(){
		
		rmhfValidadorEliminarClic = 0;
			try {
				servRmhfRegistroAutomatico.eliminarFichaInscripcionHomologación(rmhfPersonaDtoEliminar,  rmhfFichaEstudianteExistente);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.eliminarFichaInscripcion.con.exito")));
			
			} catch (RegistroAutomaticoValidacionException e) {
				FacesUtil.limpiarMensaje();
				//FacesUtil.mensajeError(e.getMessage());
				FacesUtil.mensajeError("No es posible eliminar el registro pendiente de homologar.");
			} catch (RegistroAutomaticoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		
			buscar(); //Regresa a actualizar la información de la vista
		
	}
	
	
	
	/**
	 * Método para inicar el registro de la ficha inscripcion reingreso del estudiante en BDD.
	 * @return navegación a la pagina xhtml. Listar ficha inscripcion del estudiante.
	 */
	public void crearRegistroReingreso() {
			desactivaModalReingreso(); //Desactiva el modal de reingreso
			
		try {
			servRmhfRegistroAutomatico.generarRegistroManual(rmhfPersonaDto, rmhfTipoIngresoFcinNueva);

					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.crearRegistroReingreso.exito")));
					
		} catch (RegistroAutomaticoValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			
		} catch (RegistroAutomaticoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			
		}
		
		buscar(); //Regresa a actualizar la información de la vista.
	}

	
	/**
	 * Método que activa el modal para agregar la ficha inscripcion nueva del estudiante.
	 * @param persona Información de la persona para agregar la ficha inscripción.
	 */
	
	public void habilitaModalAgregar(Persona persona)  {
		
		rmhfListaDependencia = new ArrayList<>();
		rmhfListaCarrera= new ArrayList<>();
		rmhfPersonaDtoAgregarRegistro = new RegistroHomologacionDto();
		rmhfPersonaDtoAgregarRegistro.setPrsId(persona.getPrsId());
		rmhfPersonaDtoAgregarRegistro.setPrsIdentificacion(persona.getPrsIdentificacion());
		rmhfPersonaDtoAgregarRegistro.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoAgregarRegistro.setCrrDependencia(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoAgregarRegistro.setCrrId(GeneralesConstantes.APP_ID_BASE);
		rmhfDetalleTipoIngreso= new String(" ") ;
		rmhfDisabledNotaEnes = true;
		llenarDependencia();
		rmhfValidadorClic = 2;	 //Activa modal agregar nueva ficha a un estudiante existente
		
	}
	
	
    /**
	 * Método para grabar la nueva fichaInscripcion del estudiante  que ya existe en BDD.
	 * @param registroPersona Información para generar la ficha inscripción a grabar
	 */
	public void agregarFichaInscripcion(RegistroHomologacionDto registroPersona) {
	
		//rmhfValidadorClic = 0; //desactivo modal
		Carrera carreraAux= null;
		Integer valAux = GeneralesConstantes.APP_ID_BASE;//Verificar nota de corte
		rmhfDisabledGuardarAgregar = false;  // Por defecto esta habilitado el boton guardar nueva ficha inscripción
		List<RecordEstudianteSAUDto> recordEstudianteSauAux = new ArrayList<>();
		boolean existeInscripcionPeriodo = false;
	    boolean sinRegistroSau = false;
	    boolean tieneCambioCarrera = false;
	    boolean tieneCambioUniversidad = false;
		boolean tieneRecordParaReingreso = false;
		
		try {
			
		if(registroPersona.getFcinTipoIngreso()!=GeneralesConstantes.APP_ID_BASE){ //Debe seleccionar el tipo de ingreso
			if(registroPersona.getFcinCarreraSiiu()!=GeneralesConstantes.APP_ID_BASE){//Debe seleccionar la carrera
			
				//VERIFICO QUE EL ESTUDIANTE NO TENGA UNA FICHA INSCRIPCION EN EL PERIODO ACTIVO
				if((rmhfListFichasInscripcion!=null)&&(rmhfListFichasInscripcion.size()>0)){
					for (RegistroHomologacionDto fichaInscripcion : rmhfListFichasInscripcion) {
						
						if(fichaInscripcion.getPracId()!=rmhfPeriodoAcademicoActivo.getPracId()){
							
							existeInscripcionPeriodo = false;
						}else{
							existeInscripcionPeriodo = true;
							break;
						}	
						
					}
				 }
				
				
				//VERIFICO QUE NO EXISTA YA UNA FICHA INSCRIPCION EN LA CARRERA EN EL SIIU, CAULQUIER TIPO DE INGRESO (C.CARRERA, C. UNIVERSIDAD, C.CARRERA o REING. CON REDISEÑO, SEG.CARRERA)
				 boolean encontrado = false;
			if((rmhfListFichasInscripcion!=null)&&(rmhfListFichasInscripcion.size()>0)){
				for (RegistroHomologacionDto fichaInscripcion : rmhfListFichasInscripcion) {
					
					if(fichaInscripcion.getCrrId()!=registroPersona.getFcinCarreraSiiu()){
						
						 encontrado = false;
					}else{
						encontrado = true;
						break;
					}	
					
				}
			 }	
		
				if(!encontrado)	{ //Si NO encontro regitros de inscipcion en el siiu
					//VERIFICO QUE NO EXISTA YA UNA FICHA INSCRIPCION EN LA CARRERA EN EL SAU, SI ES CAMBIO DE CARRERA, SEGUNDA CARRERA, CAMBIO UNIVERSIDAD
					
				if(registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE
						||registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE
						||registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){	
					
					carreraAux = servRmhfCarrera.buscarPorId(registroPersona.getFcinCarreraSiiu());
							   
			          //BUSCO RECORD EN SAU, EN LA CARRERA A LA QUE REINGRESA
			           if(carreraAux.getCrrEspeCodigo()!=null){
			          //   recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAU(registroPersona.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
				
			        	 //Se cambia para que se muestren tambien materias que anulo el estudiante, casos de haber anulado en primer nivel aparecen como sin record antes  
			        	   recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAUconAnulados(registroPersona.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
 
			           
			           }
			           
			           if((recordEstudianteSauAux==null)||(recordEstudianteSauAux.size()<=0)){
			        	   sinRegistroSau =true;
			        	   
			           }else{
			        	   sinRegistroSau = false;
			        	   
			           }
			           
					
				   }else{
					
					   sinRegistroSau =true;
					
				    }
				
				}else{
					sinRegistroSau =true;
					
				}
				
				
				//VERIFICO QUE NO TENGA MAS CAMBIOS DE CARRERA EN CASO DE SER CAMBIO DE CARRERA
			
			if (registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
				
				if((rmhfListFichasInscripcion!=null)&&(rmhfListFichasInscripcion.size()>0)){
					for (RegistroHomologacionDto fichaInscripcion : rmhfListFichasInscripcion) {
						
						if(fichaInscripcion.getFcinTipoIngreso()!=FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
							
							tieneCambioCarrera = false;
						}else{
							tieneCambioCarrera = true;
							break;
						}
						
					}
				 }
				
			}
				
			//VERIFICO QUE NO TENGA MAS CAMBIOS DE UUNIVERSIDAD EN CASO DE SER CAMBIO DE UNIVERSIDAD
              if (registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
				
				if((rmhfListFichasInscripcion!=null)&&(rmhfListFichasInscripcion.size()>0)){
					for (RegistroHomologacionDto fichaInscripcion : rmhfListFichasInscripcion) {
						
						if(fichaInscripcion.getFcinTipoIngreso()!=FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
							
							tieneCambioUniversidad = false;
						}else{
							tieneCambioUniversidad = true;
							break;
						}
						
					}
				 }
				
			}
				
				//SOLO INGRESA POR AGREGAR FCIN SI NO TIENE YA FCIN EN SIIU, CASO CONTRARIO VERIFICO QUE TENGA RECORD EN SAU
				if((registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
						||(registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)){	
					carreraAux = servRmhfCarrera.buscarPorId(registroPersona.getFcinCarreraSiiu());
						
					   //BUSCO RECORD EN SIIU, EN CARRERA A LA QUE INGRESA
			          //   recordEstudianteSIIUAux=servRmhfRecordEstudianteSIIU.buscarXidentificacionXcarrera(personaNueva.getPrsIdentificacion(), carreraAux.getCrrId());

			        //BUSCO RECORD EN SAU, EN LA CARRERA A LA QUE REINGRESA
			        if(carreraAux.getCrrEspeCodigo()!=null){
			        //    recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAU(registroPersona.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
						 recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAUconAnulados(registroPersona.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
  			        
			        }
			        
			        if((recordEstudianteSauAux!=null) && (recordEstudianteSauAux.size()>0)){
			        	  tieneRecordParaReingreso = true;
			            }else{
			            	tieneRecordParaReingreso = false;
			          }
					
					}else{//Si son otros tipo de ingreso esta validación no se debe tomar en cuenta
						tieneRecordParaReingreso = true;
						
					}
				
				
				//VALIDAR SI SUPERA NOTA ENES
				boolean validaNotaEnes = false;
				if(registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE
						||registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE
						||registroPersona.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){		
				
					if(registroPersona.getFcinNotaEnes()!= null && registroPersona.getFcinNotaEnes()>=0 ){	
				    valAux=verificarNotaCorte(registroPersona.getFcinCarreraSiiu(),registroPersona.getFcinNotaEnes()); //validar
					
				     if(valAux==0){
					    validaNotaEnes = true;
						
					    registroPersona.setFcinNotaCorteId(rmhfNotaCorte.getNocrId()); //Se añade el Id de la nota e corte
				      }
				     
					}else{
						
						valAux=3; //el valor de nota enes no puede esta vacio
					}
					
				}else{
					
					 validaNotaEnes = true; //no necesita nota enes, para otros tipos de ingreso
				}
				
				
				//VALIDAR QUE NO TENGA MATRICULA ACTUALMENTE.
				
				boolean existeMatriculaActual = false;
				
					Date fechaActual = new Date(); //obtener fecha actual;
					List<CronogramaDto> listaCronogramaMatriculas = new ArrayList<>();
					
					
					listaCronogramaMatriculas=servRmhfCronogramaJdbc.listarFechasCronogramaXTipoCronogramaXPeriodo(CronogramaConstantes.TIPO_ACADEMICO_VALUE,rmhfPeriodoAcademicoActivo.getPracId());
					
					boolean habilitadoCronogramaMatricula= false;
					//VALIDAR SI ESTAMOS DENTRO DEL PERIODO DE MATRICULA
					for (CronogramaDto cronogramaDto : listaCronogramaMatriculas) {
						 if(cronogramaDto.getPrflId()==ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE
							||cronogramaDto.getPrflId()==ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE ){
						  if(cronogramaDto.getPlcrFechaInicial() != null && cronogramaDto.getPlcrFechaFinal()!= null)	{			
					    	if (GeneralesUtilidades.verificarEntreFechas(cronogramaDto.getPlcrFechaInicial(),cronogramaDto.getPlcrFechaFinal(), fechaActual)) {
						      habilitadoCronogramaMatricula = true;
							   break;
							}	
						  }
					    	
					    }
					}  //fin for
					
					
					if(habilitadoCronogramaMatricula){
						
						
						//Buscar si el estudiante no se matriculó en este periodo.
						List<FichaMatriculaDto> listFichaMatricula  = new ArrayList<>();
						
						listFichaMatricula=servRmhfFichaMatriculaJdbc.buscarFichaMatriculaXPeriodoXIdentificacion(rmhfPeriodoAcademicoActivo.getPracId(), registroPersona.getPrsIdentificacion());
						
						if(listFichaMatricula.size()>0){
							existeMatriculaActual= true;
							
						}
						
					}
				
				
			//Todas la validaciones	
	if(!existeInscripcionPeriodo){
		if(sinRegistroSau){
		if(!encontrado){	
			if(tieneRecordParaReingreso){	
				if(!tieneCambioUniversidad){
				      if(!tieneCambioCarrera){
				    	  if(validaNotaEnes){
				    	
				    		  if(!existeMatriculaActual){
			
		                   	servRmhfRegistroAutomatico.generarRegistroManual(registroPersona, registroPersona.getFcinTipoIngreso());
				         	FacesUtil.limpiarMensaje();
					        FacesUtil.mensajeInfo("mensaje","Registro creado con éxito");
					         rmhfValidadorClic=0;
					         rmhfDisabledGuardarAgregar = true; //Si se graba con exito deshabilito boton grabar
					         
					         // iniciarParametros();
					         //CancelarAgregarRegistro();
				              } else{
				            	  FacesUtil.limpiarMensaje();
						 	      FacesUtil.mensajeError("mensaje","El estudiante presenta matrícula en el período actual, no es posible registrar la movilidad"); 
				            			            					    			  
				    		  }
				            } else{
					        	 
				            	 if(valAux==1){
						    		 	FacesUtil.limpiarMensaje();
						 	            FacesUtil.mensajeError("mensaje","La nota enes es menor a la nota de corte para la carrera indicada.");
						    		 }else if(valAux==2 ){
						    			 FacesUtil.limpiarMensaje();
							 	         FacesUtil.mensajeError("mensaje","No existe nota de corte en la carrera seleccionada, comuniquese con la Dirección General Académica");
						    			 
						    		 }else if(valAux==3 ){
						    			 FacesUtil.limpiarMensaje();
							 	         FacesUtil.mensajeError("mensaje","Debe ingresar el valor de la nota enes");
						    			 
						    		 }else{
						    			 FacesUtil.limpiarMensaje();
							 	         FacesUtil.mensajeError("mensaje","Error desconocido al validar la nota enes con la nota de corte");
						    			 
						    		 }
				        	 
					         }
					         
			               } else{ //tieneCambioCarrera
				               FacesUtil.limpiarMensaje();
				               FacesUtil.mensajeError("mensaje","El estudiante ya cuenta con un cambio de carrera previo");
				   
			                   }
			 
		          	}else{  //tieneCambio de Universidad
				
		          		 FacesUtil.limpiarMensaje();
			             FacesUtil.mensajeError("mensaje","El estudiante ya cuenta con un cambio de universidad previo");
		          		
			          }
				
			    }else{ //tieneRecordParaReingreso
				
			    	FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("mensaje","El estudiante no tiene record en la carrera para reingreso o reinicio");
					
			   }
			
			}else{ //encontrado
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje","El estudiante se encuentra ya inscrito en el SIIU en la carrera indicada");
			     }
			
		}else{
			FacesUtil.limpiarMensaje();
	        FacesUtil.mensajeError("mensaje","El estudiante cuenta con un registro en el SAU en la carrera indicada");
			
		}
		
		     }else{ //existeInscripcionPeriodo
        	 FacesUtil.limpiarMensaje();
	         FacesUtil.mensajeError("mensaje","El estudiante cuenta con un registro de movilidad en este periodo");
	
          }	
			
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje","Debe seleccionar la carrera a agregar");
				
			}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje","Debe seleccionar el tipo de ingreso");
				
			}
			
			
			
		} catch (RegistroAutomaticoValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (RegistroAutomaticoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (RecordEstudianteNoEncontradoException e) {
			//e.printStackTrace();
			//CONSULTA A SAU NO LANZA EXCEPTION
		} catch (RecordEstudianteException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje"," No se encontró cronograma de pregrado, al buscar por período activo");
	
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		}
		
	}
	
	
	/**
	 * Método para cancelar el agregar una ficha inscripcion
	 */
	
	public  void CancelarAgregarRegistro(){
		rmhfValidadorClic = 0;
		// Campos modal Agregar
		rmhfListaCarrera = new ArrayList<>(); // Lista de carreras validas pregrado
		rmhfPersonaDtoAgregarRegistro = new RegistroHomologacionDto();
		rmhfListaDependencia = new ArrayList<>();

		rmhfPersonaDtoAgregarRegistro.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoAgregarRegistro.setCrrDependencia(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoAgregarRegistro.setCrrId(GeneralesConstantes.APP_ID_BASE);
		 rmhfDisabledNotaEnes = true;
		 rmhfDisabledAplicaEnes = true;
		 rmhfChkNotaEnes = false;
		 
		rmhfDetalleTipoIngreso = new String(" ");
		
		buscar();
		 
	}
	
	
	/**
	 * Método para ir a crear nuevo estudiante, en caso de que no exista en el SIIU.
	 * @return Navegación a la pagina xhtml de crear nuevo estudiante.
	 */
	
	public String irCrearNuevoEstudiante(String prsIdentificacion)  {
		rmhfListaDependencia = new ArrayList<>();
		rmhfListaCarrera= new ArrayList<>();
		rmhfPersonaDtoNuevo = new RegistroHomologacionDto();
		rmhfPersonaDtoNuevo.setPrsTipoIdentificacion(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoNuevo.setPrsIdentificacion(prsIdentificacion);
		rmhfPersonaDtoNuevo.setPrsSexo(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoNuevo.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoNuevo.setCrrDependencia(GeneralesConstantes.APP_ID_BASE);
		rmhfPersonaDtoNuevo.setFcinCarreraSiiu(GeneralesConstantes.APP_ID_BASE);
		rmhfDetalleTipoIngreso= new String(" ") ;
		 rmhfDisabledNotaEnes = true;
		 rmhfDisabledAplicaEnes = true;
		 rmhfChkNotaEnes = false;

		llenarDependencia();	//lleno el combo de dependencias
		return "irAcrearNuevoEstudiante";
	}
	
	
	/**
	 * Método para llenar el texto de detalle de tipo de inscripcion para nuevo estudiante
	 */
	public void llenarDetalleTipoIngresoNuevo(Integer tipoIngreso){
		rmhfPersonaDtoNuevo.setCrrDependencia(GeneralesConstantes.APP_ID_BASE); //seteo la lista de dependecias
		rmhfPersonaDtoNuevo.setFcinCarreraSiiu(GeneralesConstantes.APP_ID_BASE); //Seteo la lista de carreras
		rmhfDetalleTipoIngreso= new String(" ") ;
		rmhfDetalleTipoIngreso = devuelveTipoIngreso(tipoIngreso); //llamo el método que me devuelve la descripción del tipo de ingreso
	
		
		//HABILITAR CUANDO REGRESE DE VACA
		if((tipoIngreso==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
				|| (tipoIngreso==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE)
				|| (tipoIngreso==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)){
			//Debe ingresar nota enes
			
			//rmhfDisabledAplicaEnes = false;
			rmhfDisabledAplicaEnes = true; //se deahabilita la opcion de poder  quitar el aplica nota enes, 16 jul 2019
			rmhfChkNotaEnes= true;
			rmhfDisabledNotaEnes = false;
		
		}else{
			rmhfDisabledAplicaEnes = true;
			rmhfChkNotaEnes = false;
			rmhfDisabledNotaEnes = true;
			
		}
			
			
	}
	
	public void aplicaNotaEnes(Boolean estadoChk){
		if(estadoChk==true){
			rmhfDisabledNotaEnes =false;
			
		}else{
			rmhfDisabledNotaEnes =true;
			
		}
		
	}
	
	/**
	 * Método que verifica la información ingresada antes de mostrar el modal guardar
	 * @param personaNueva información ingresada en el formulario
	 */
	
public void irVerificarNuevoEstudiante(RegistroHomologacionDto personaNueva)  {
		desactivaModalcrearNuevo(); // por defecto modal deshabilitado
		// reglas para permitit guardar
		Persona personaAux = null;
		Carrera carreraAux = null;
		Integer valAux = GeneralesConstantes.APP_ID_BASE; //1
		boolean validaReingreso = false;
		List<RecordEstudianteSAUDto> recordEstudianteSauAux = new ArrayList<>();	
	
	try {
		
		personaAux = servRmhfPersona.buscarPersonaPorIdentificacion(personaNueva.getPrsIdentificacion());
		
		//VERIFICIO QUE NO EXISTE LA PERSONA POR IDENTIFICACION, PARA CREAR NUEVO ESTUDIANTE EN EL SIIU, por ende es la primera ficha inscripcion del estudiante, si se le ocurre cambiarme el num de cedula
		if(personaAux== null){
		if(personaNueva.getPrsTipoIdentificacion()!=GeneralesConstantes.APP_ID_BASE){
		if(personaNueva.getPrsSexo()!=GeneralesConstantes.APP_ID_BASE)	{
		if(personaNueva.getFcinTipoIngreso()!=GeneralesConstantes.APP_ID_BASE)	{	
		
		
		 if(personaNueva.getFcinCarreraSiiu()!=GeneralesConstantes.APP_ID_BASE){
			
			//VERIFICO QUE SI EXISTA RECORD DEL ESTUDIANTE EN LA CARRERA A LA QUE REINGRESA O REINICIA
				
			if((personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
				||(personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)){	
				carreraAux = servRmhfCarrera.buscarPorId(personaNueva.getFcinCarreraSiiu());
				 //BUSCO RECORD EN SIIU, EN CARRERA A LA QUE INGRESA, NO APLICA  PUES REINGRESO DE ALGUIEN QUE YA ESTA EN EL SIIU  DEBE HACERLO EN LA VENTANA PREVIA.
	                   //   recordEstudianteSIIUAux=servRmhfRecordEstudianteSIIU.buscarXidentificacionXcarrera(personaNueva.getPrsIdentificacion(), carreraAux.getCrrId());
	
	                //BUSCO RECORD EN SAU, EN LA CARRERA A LA QUE REINGRESA
	               if(carreraAux.getCrrEspeCodigo()!=null){
	               //     recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAU(personaNueva.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
	  				
	            	   recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAUconAnulados(personaNueva.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);

	               
	               }
	        
                       if((recordEstudianteSauAux!=null) && (recordEstudianteSauAux.size()>0)){
	                         validaReingreso = true;
                         }else{
	                        validaReingreso = false;
                           }
			
			}else{//Si son otros tipo de ingreso esta validación no se debe tomar en cuenta
				 validaReingreso = true;
				
			}
			
			//VERIFICO QUE NO EXISTA YA UNA FICHA INSCRIPCION EN LA CARRERA EN EL SAU, SI ES CAMBIO DE CARRERA, SEGUNDA CARRERA, CAMBIO UNIVERSIDAD: REDISEÑO ESTA CUBIERTO EN EL SIIU
			boolean sinRegistroSau = false;		
			
		if(personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE
				||personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE
				||personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){	
			
			              carreraAux = servRmhfCarrera.buscarPorId(personaNueva.getFcinCarreraSiiu());
					   
	                //BUSCO RECORD EN SAU, EN LA CARRERA A LA QUE REINGRESA
	                if(carreraAux.getCrrEspeCodigo()!=null){
	               //       recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAU(personaNueva.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
	   				 recordEstudianteSauAux=servRmhfRecordEstudianteSAU.buscarRecordAcademicoSAUconAnulados(personaNueva.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
      
	                
	                }
	           
	                 if((recordEstudianteSauAux==null)||(recordEstudianteSauAux.size()<=0)){
	        	                sinRegistroSau =true;
	        	   
	                    }else{
	        	              sinRegistroSau = false;
	        	   
	                  }
	           
			
		}else{
			
			sinRegistroSau =true;
			
		}
	
		//VALIDAR SI SUPERA NOTA ENES, CAMBIO DE CARRERA, CAMBIO UNIVERSIDAD, SEGUNDA CARRERA 
		boolean validaNotaEnes = false;
		if(personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE
				||personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE
				||personaNueva.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){		
			
		    valAux=verificarNotaCorte(personaNueva.getFcinCarreraSiiu(),personaNueva.getFcinNotaEnes()); //validar
			
		     if(valAux==0){
			    validaNotaEnes = true;
				
			    rmhfPersonaDtoNuevo.setFcinNotaCorteId(rmhfNotaCorte.getNocrId()); //Se añade el Id de la nota de corte
		      }
			
		}else{
			
			 validaNotaEnes = true; //no necesita nota enes, para otros tipos de ingreso, reingreso
		}
		
		
		if(rmhfChkNotaEnes ==true){
			rmhfPersonaDtoNuevo.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE);
			
		}else{
			
			rmhfPersonaDtoNuevo.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE);
		}
		
		
		
		//VALIDAR QUE NO TENGA MATRICULA ACTUALMENTE.
		
		boolean existeMatriculaActual = false;
		
			Date fechaActual = new Date(); //obtener fecha actual;
			List<CronogramaDto> listaCronogramaMatriculas = new ArrayList<>();
			
			
			listaCronogramaMatriculas=servRmhfCronogramaJdbc.listarFechasCronogramaXTipoCronogramaXPeriodo(CronogramaConstantes.TIPO_ACADEMICO_VALUE,rmhfPeriodoAcademicoActivo.getPracId());
			
			boolean habilitadoCronogramaMatricula= false;
			//VALIDAR SI ESTAMOS DENTRO DEL PERIODO DE MATRICULA
			for (CronogramaDto cronogramaDto : listaCronogramaMatriculas) {
				 if(cronogramaDto.getPrflId()==ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE
					||cronogramaDto.getPrflId()==ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE ){
				  if(cronogramaDto.getPlcrFechaInicial() != null && cronogramaDto.getPlcrFechaFinal()!= null)	{			
			    	if (GeneralesUtilidades.verificarEntreFechas(cronogramaDto.getPlcrFechaInicial(),cronogramaDto.getPlcrFechaFinal(), fechaActual)) {
				      habilitadoCronogramaMatricula = true;
					   break;
					}	
				  }
			    	
			    }
			}  //fin for
			
			
			if(habilitadoCronogramaMatricula){
				
				
				//Buscar si el estudiante no se matriculó en este periodo.
				List<FichaMatriculaDto> listFichaMatricula  = new ArrayList<>();
				
				listFichaMatricula=servRmhfFichaMatriculaJdbc.buscarFichaMatriculaXPeriodoXIdentificacion(rmhfPeriodoAcademicoActivo.getPracId(), rmhfPersonaDtoNuevo.getPrsIdentificacion());
				
				if(listFichaMatricula.size()>0){
					existeMatriculaActual= true;
					
				}
				
			}
			
		
		
		//VALIDO TODOS LOS CASOS
		 if(sinRegistroSau){
			
		     if(validaReingreso){
		    	 
		    	 if(validaNotaEnes){
		    		 
		    		 if(!existeMatriculaActual){ //No debe existir matricula en período actual.
		    		 
		             activaModalcrearNuevo();  //Activo modal para poder grabar
		    		
		    		 }else{
		    			 
		    			 FacesUtil.limpiarMensaje();
			 	         FacesUtil.mensajeError("El estudiante presenta matrícula en el período actual, no es posible registrar la movilidad");
		    
		    			 
		    		 }
		    		 
		    	 }else {
		    		 
		    		 if(valAux==1){//la nota enes es menor a nota de corte
		    		 	FacesUtil.limpiarMensaje();
		 	            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.notaEnes.exception")));
		    		 }else if(valAux==2 ){//No existe nota de corte
		    			 FacesUtil.limpiarMensaje();
			 	         FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.notaEnes.no.encontrado.exception")));
		    		 }else{//Error desconocido
		    			 FacesUtil.limpiarMensaje(); 
			 	         FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.error.exception")));
		    		 }
		    		 
		    	 }
		             
		       	}else{
		        	FacesUtil.limpiarMensaje();
	 	            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.record.para.reingreso.exception")));
                      }
			 
		     
		     
		     
		   }else{     	
			   FacesUtil.limpiarMensaje();
            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.registro.sau.exception")));
	       	}
			
		
	     }else{ //CarreraSiiu
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.carreraId.exception")));
			 }
	    }else{ //Tipo Ingreso
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.tipoIngreso.exception")));
		    }
	  }else{//PrsSexo
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.sexo.exception")));
		}
	
	}else{ //TipoIdentificacion
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.tipoIdentificacion.exception")));
		}
			
   }else{
		  FacesUtil.limpiarMensaje();
		  FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.validacion.existe.persona.exception")));
		  
  }
	
	} catch (PersonaException e) {
		  FacesUtil.limpiarMensaje();
		  FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.persona.exception")));
		
	} catch (CarreraNoEncontradoException e) {
		//Carrera a Ingresar
		 FacesUtil.limpiarMensaje();
		 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.carrera.por.fcinCarreraSiiu.no.encontrada.exception")));
		
	} catch (CarreraException e) {
		 FacesUtil.limpiarMensaje();
		 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.carrera.por.FcinCarreraSiiu.exception")));
	
		//Carrera a Ingresar
	} catch (RecordEstudianteNoEncontradoException e) {
			//No devuelve exception  en consulta de  record sau
	} catch (RecordEstudianteException e) {
		//error en record Sau
		 FacesUtil.limpiarMensaje();
		 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.recordEstudiante.exception")));
	
	} catch (CronogramaDtoJdbcException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(e.getMessage());
		
	} catch (CronogramaDtoJdbcNoEncontradoException e) {
		 FacesUtil.limpiarMensaje();
		// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.irVerificarNuevoEstudiante.recordEstudiante.exception")));
		 FacesUtil.mensajeError("No se encontró cronograma de pregrado, al buscar por período activo");
		 
	} catch (FichaMatriculaException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(e.getMessage());
	}
		
	}
	
/**
 * 	Método que permite guardar la información del estudiante nuevo en la BDD
 * @param personaNueva Información del formulario para realizar el guardado en BDD
 * @return Navegación a la página xhtml para buscar nuevamente el estudiante
 */
	
public String guardarNuevoEstudiante(RegistroHomologacionDto personaNueva)  {

	String retorno = null;
	
	desactivaModalcrearNuevo();
	try {
		servRmhfRegistroAutomatico.generarRegistroManual(personaNueva, personaNueva.getFcinTipoIngreso()); //Es el mismo registro de cuando se agrega ficha inscripción
		FacesUtil.limpiarMensaje();
		 FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.guardarNuevoEstudiante.exito")));
		 iniciarParametros();
		retorno="irAlistarEstudianteHomologacion";
		
	} catch (RegistroAutomaticoValidacionException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(e.getMessage());
	} catch (RegistroAutomaticoException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(e.getMessage());
	}
	return  retorno;
	
}

/**
 * Metodo para llenar el combo de dependencias
 */

public void llenarDependencia(){
	try {
		
	    	rmhfListaDependencia = new ArrayList<>();
		if(rmhfRol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE){
			
			rmhfListaDependencia= servRmhfDependencia.listarFacultadesxUsuario(rmhfUsuario.getUsrId());
			
		}else if(rmhfRol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE){
		  //LISTA TODAS DEPENDENCIAS DE PREGRADO
		
			rmhfListaDependencia= servRmhfDependencia.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
		}else {
			// Si ingresa persona con otro rol
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.llenarDependencia.rol.validacion.exception")));
			
		}
	
	} catch (DependenciaNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.llenarDependencia.no.encontrado.exception")));

	}	
	
}

/**
 * Método para llenar lista carrreras  en modal agregar, de acuerdo a la dependencia seleccionada
 * @param dependenciaId.- id de la dependencia para buscar carreras
 */
public  void llenarCarreras(Integer dependenciaId){
	
	rmhfListaCarrera= new ArrayList<>();
	
	try {
		
		if(dependenciaId!=GeneralesConstantes.APP_ID_BASE){
			List<Carrera> listCarrerasAux=servRmhfCarrera.listarCarrerasXFacultad(dependenciaId);
			
			if(rmhfRol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE){
				
			for(Carrera carrerasFacultad :listCarrerasAux ){
				boolean encontrado = false;
				for (CarreraDto carreraDto : rmhfListCarreraDto) {
					 if(carreraDto.getCrrId()==carrerasFacultad.getCrrId()){
						 encontrado = true;
						 break;
					 }else{
						encontrado= false;
					 }
				}
				
				if(encontrado){//Solo si encontro una carrera que la secretaria tiene permiso procedo a agregar en la lista a mostrar
					rmhfListaCarrera.add(carrerasFacultad);
				
				}
			}
			
			
			}else  if(rmhfRol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE){
				
				rmhfListaCarrera.addAll(listCarrerasAux); //Para soporte debe tener acceso a todas la carreras de la facultad seleccionada
				
			}else{
				
				// Si ingresa persona con otro rol
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.llenarDependencia.rol.validacion.exception")));
				
			}
		
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.llenarCarreras.validacion.dependenciaId.exception")));
		}
	
	} catch (CarreraNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.llenarCarreras.carrera.no.encontrada.exception")));
	} catch (CarreraException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroManualHomologacion.llenarCarreras.carrera.exception")));
	}
	
}


/**
 * Método para llenar el texto de detalle de tipo de inscripcion al agregar una ficha inscripción
 * @param tipoIngreso.- Tipo de ingreso para presentar la descripción del tipo de ingreso.
 */
public void llenarDetalleTipoIngreso(Integer tipoIngreso){
	rmhfPersonaDtoAgregarRegistro.setCrrDependencia(GeneralesConstantes.APP_ID_BASE); //seteo la lista de dependecias
	rmhfPersonaDtoAgregarRegistro.setFcinCarreraSiiu(GeneralesConstantes.APP_ID_BASE); //Seteo la lista de carreras
	
	rmhfDetalleTipoIngreso= new String(" ") ;
	rmhfDetalleTipoIngreso = devuelveTipoIngreso(tipoIngreso); //devuelve el tipo de ingreso(String) de acuerdo a lo seleccionado
	
	if((tipoIngreso==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
			|| (tipoIngreso==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE)
			|| (tipoIngreso==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)){
		//Debe ingresar nota enes
		rmhfDisabledAplicaEnes = true;
		rmhfChkNotaEnes= true;
		rmhfDisabledNotaEnes = false;
	
	}else{
		rmhfDisabledAplicaEnes = true;
		rmhfChkNotaEnes = false;
		rmhfDisabledNotaEnes = true;
		
	}
		
	
}

/**
 * Método que devuelve el detalle del tipo de ingreso seleccionado
 * @return detalle del tipo de ingreso.
 * @param tipoIngreso.- tipo de ingreso a buscar detalle.
 */
public String devuelveTipoIngreso(Integer tipoIngreso){
	String retorno = " ";
	//MQ: 26 jul 2019 Pide Producción quitar el texto del tipo de ingreso por cambio de normativa, se desactiva en el Jsf  con rendered
	
	if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
		retorno ="Es el proceso que se aplica para estudiantes que han dejado sus estudios por al menos un período académico y que no supera los cinco años. "
				+ "En este caso los estudiantes continúan con la malla anterior y el record que mantenían al momento de su retiro."
				+ "-Se validará que el estudiante tenga historial académico en la universidad. "
				+ "-El sistema NO permitirá ingresar homologaciones";

	}else if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
		retorno ="Es un proceso que se aplica para estudiantes que desean cambiarse de Carrera en la misma Universidad, ellos deben cumplir los requisitos de "
				+ "acuerdo con la disposición general Vigésima, literal a) “Un estudiante podrá cambiarse de una carrera a otra en la misma IES pública, por una sola ocasión, "
				+ "una vez que haya cursado al menos un período académico ordinario y haya aprobado todas las asignaturas, cursos o sus equivalentes del plan de estudios del "
				+ "correspondiente período, de las cuales al menos una pueda ser homologada, en la carrera receptora”; y tener el puntaje requerido en la nota ENES para el "
				+ "ingreso a la Carrera .-Se validará que el estudiante tenga historial académico en la Universidad. -La homologación es obligatoria en al menos una asignatura."
				+ "-En caso de no haber homologación no se podrá registrar el cambio de carrera del estudiante. -Se deberá seleccionar la carrera de la cual proviene el estudiante obligatoriamente.";

    }else if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
    	retorno ="Es un proceso  que se aplica para estudiantes que desean ingresar a la Institución y proceden de otra Universidad, para realizar este proceso se "
    			+ "considera la disposición general Vigésima, literal b) “Un estudiante podrá cambiarse de carrera a otra  de diferente IES pública, por una sola ocasión, una vez "
    			+ "que haya cursado al menos dos períodos académicos y haya aprobado asignaturas cursos o sus equivalentes, que puedan ser homologadas”; y el literal c) "
    			+ "“Un estudiantes podrá cambiarse de una carrera de una IES particular a una IES pública, siempre que rinda el Examen Nacional de Nivelación y Admisión y obtenga "
    			+ "el puntaje requerido para la carrera receptora”. -La homologación es obligatoria en al menos una asignatura. -En caso de no haber homologación no se podrá "
    			+ "registrar el cambio de universidad del estudiante. -Se deberá seleccionar la Universidad de la cual proviene el estudiante, en caso de no existir en el Sistema escoger OTRA.";
    	
	}else if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
		retorno ="Es un proceso  que se aplica para estudiantes que han dejado sus estudios por al menos cinco años y no supera los diez años, los estudiantes solicitan continuar su Carrera. "
				+ "-Se deberá ingresar el año en que abandonó la carrera. --El sistema NO permitirá ingresar homologaciones. En este caso los estudiantes continúan con la malla anterior y "
				+ "el record que mantenían al momento de su retiro";
	
	}else if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){	
		retorno ="Es un proceso que se aplica para estudiantes nuevos que toman una segunda carrera dentro de la Universidad Central. Las personas que deseen cursar una segunda "
				+ "carrera a la vez o iniciar una nueva carrera (tras haber culminado una), siempre y cuando el cupo haya sido obtenido después de la implementación del Sistema Nacional de Nivelación"
				+ " y Admisión (18 de febrero de 2012), deberán ingresar a través del Sistema Nacional de Nivelación y Admisión. Esta segunda carrera no será gratuita. Los aspirantes que obtuvieron un cupo "
				+ "en una Institución de Educación Superior antes del 18 de febrero de 2012, y deseen continuar o iniciar sus estudios, deberán gestionar la disponibilidad y obtención de un cupo en la misma carrera.";
	
	}else if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
		retorno ="CAMBIO DE MALLA";
	
	}else if(tipoIngreso == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
		retorno ="Es un proceso que se aplica para estudiantes que la carrera  determina que debe cambiarse de una carrera de diseño a rediseño, se incluyen: reingresos con rediseño y "
				+ "reinicios con rediseño. -Se validará que el estudiante tenga historial académico en la Universidad de la carrera de diseño. -La homologación es opcional, depende del análisis realizado al "
				+ "historial del estudiante en la Facultad. "
				+ "-Se deberá seleccionar la carrera de la cual proviene el estudiante obligatoriamente.";
		
	}
	
	return retorno;
	
}
	
public Integer verificarNotaCorte (Integer carreraId, float notaIngresada ){
	 PeriodoAcademico periodoAcademicoCorteAux =null ;
	 rmhfNotaCorte = null;
	 
	Integer retorno = GeneralesConstantes.APP_ID_BASE; //Por defecto devuelvo -99  error
	
	 try {
		periodoAcademicoCorteAux = servRmhfPeriodoAcademico.buscarPeriodoEnCierre();
	
	if(periodoAcademicoCorteAux==null){// Sino existe periodo en cierre, busco periodo activo
		periodoAcademicoCorteAux = servRmhfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
	}
	
	rmhfNotaCorte= servRmhfNotaCorte.buscarActivoXCrrXPrac(carreraId, periodoAcademicoCorteAux.getPracId());
	
	if (rmhfNotaCorte!=null){
	  BigDecimal notaCorteAux = new BigDecimal(rmhfNotaCorte.getNocrNota());
	  BigDecimal notaIngresadaAux = new BigDecimal(notaIngresada);
	
	   if(notaIngresadaAux.compareTo(notaCorteAux)>=0){
	     	retorno=0; //Si es mayor o igual devuelvo cero
		
	    }else{
	    	retorno =1; //Es menor
	    }
	
	}else{
		
		retorno=2; //No existe nota de corte en la carrera.
	}
	   
	   
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
          catch (PeriodoAcademicoException e) {
        	  FacesUtil.limpiarMensaje();
  			FacesUtil.mensajeError(e.getMessage());
		} catch (NotaCorteException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
      }
	
		return retorno;	
	
}


	/**
	 * Método para cancelar la creación de nuevo estudiante
	 */
    public  String CancelarNuevo(){
		iniciarParametros();
    	return  "irAlistarEstudianteHomologacion";
	}
	
    /**
     * Método para limpiar los campos de la pagina de búsqueda.
     */
	
	public void limpiar(){
		iniciarParametros();
		
	}

	/**
	 *Método que permite desactivar el modal reingreso
	 */
	public void desactivaModalReingreso(){
			rmhfValidadorClic = 0;
	}
	
	/**
	 *Método que permite activar el modal reingreso
	 */
	public void activaModalReingreso(){
		rmhfValidadorClic = 1;
    }	
	
	/**
	 *Método que permite desactivar el modal crear nuevo estudiante
	 */
	public void desactivaModalcrearNuevo(){
		rmhfValidadorNuevoClic = 0;
    }

	/**
	 *Método que permite activar el modal crear nuevo estudiante
	 */
    public void activaModalcrearNuevo(){
	rmhfValidadorNuevoClic = 1;
   }	
	
    /**
	 *Método que permite desactivar el modal eliminar ficha inscripción
	 */
	public void desactivaModalEliminar(){
			rmhfValidadorEliminarClic = 0;
	}
	
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getRmhfUsuario() {
		return rmhfUsuario;
	}

	public void setRmhfUsuario(Usuario rmhfUsuario) {
		this.rmhfUsuario = rmhfUsuario;
	}

	public PersonaDto getRmhfPersonaDtoBuscar() {
		return rmhfPersonaDtoBuscar;
	}

	public void setRmhfPersonaDtoBuscar(PersonaDto rmhfPersonaDtoBuscar) {
		this.rmhfPersonaDtoBuscar = rmhfPersonaDtoBuscar;
	}

	public List<CarreraDto> getRmhfListCarreraDto() {
		rmhfListCarreraDto = rmhfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : rmhfListCarreraDto;
		return rmhfListCarreraDto;
	}

	public void setRmhfListCarreraDto(List<CarreraDto> rmhfListCarreraDto) {
		this.rmhfListCarreraDto = rmhfListCarreraDto;
	}

	public CarreraDto getRmhfCarreraDtoBuscar() {
		return rmhfCarreraDtoBuscar;
	}

	public void setRmhfCarreraDtoBuscar(CarreraDto rmhfCarreraDtoBuscar) {
		this.rmhfCarreraDtoBuscar = rmhfCarreraDtoBuscar;
	}

	public Integer getRmhfValidadorClic() {
		return rmhfValidadorClic;
	}

	public void setRmhfValidadorClic(Integer rmhfValidadorClic) {
		this.rmhfValidadorClic = rmhfValidadorClic;
	}

	public List<RegistroHomologacionDto> getRmhfListFichasInscripcion() {
		rmhfListFichasInscripcion = rmhfListFichasInscripcion == null ? (new ArrayList<RegistroHomologacionDto>()) : rmhfListFichasInscripcion;
	
		return rmhfListFichasInscripcion;
	}

	public void setRmhfListFichasInscripcion(List<RegistroHomologacionDto> rmhfListFichasInscripcion) {
		this.rmhfListFichasInscripcion = rmhfListFichasInscripcion;
	}

	public PeriodoAcademico getRmhfPeriodoAcademicoActivo() {
		return rmhfPeriodoAcademicoActivo;
	}

	public void setRmhfPeriodoAcademicoActivo(PeriodoAcademico rmhfPeriodoAcademicoActivo) {
		this.rmhfPeriodoAcademicoActivo = rmhfPeriodoAcademicoActivo;
	}

	public UsuarioRol getRmhfUsuarioRol() {
		return rmhfUsuarioRol;
	}

	public void setRmhfUsuarioRol(UsuarioRol rmhfUsuarioRol) {
		this.rmhfUsuarioRol = rmhfUsuarioRol;
	}

	public Rol getRmhfRol() {
		return rmhfRol;
	}

	public void setRmhfRol(Rol rmhfRol) {
		this.rmhfRol = rmhfRol;
	}

	public RegistroHomologacionDto getRmhfPersonaDto() {
		return rmhfPersonaDto;
	}

	public void setRmhfPersonaDto(RegistroHomologacionDto rmhfPersonaDto) {
		this.rmhfPersonaDto = rmhfPersonaDto;
	}

	
	public Persona getRmhfPersonaVer() {
		return rmhfPersonaVer;
	}

	public void setRmhfPersonaVer(Persona rmhfPersonaVer) {
		this.rmhfPersonaVer = rmhfPersonaVer;
	}

	public Integer getRmhfTipoIngresoFcinNueva() {
		return rmhfTipoIngresoFcinNueva;
	}

	public void setRmhfTipoIngresoFcinNueva(Integer rmhfTipoIngresoFcinNueva) {
		this.rmhfTipoIngresoFcinNueva = rmhfTipoIngresoFcinNueva;
	}

	
	public Boolean getRmhfDisabledAgregar() {
		return rmhfDisabledAgregar;
	}

	public void setRmhfDisabledAgregar(Boolean rmhfDisabledAgregar) {
		this.rmhfDisabledAgregar = rmhfDisabledAgregar;
	}

	

	public Boolean getRmhfDisabledNuevo() {
		return rmhfDisabledNuevo;
	}

	public void setRmhfDisabledNuevo(Boolean rmhfDisabledNuevo) {
		this.rmhfDisabledNuevo = rmhfDisabledNuevo;
	}

	public Boolean getRmhfRenderPanelInfo() {
		return rmhfRenderPanelInfo;
	}

	public void setRmhfRenderPanelInfo(Boolean rmhfRenderPanelInfo) {
		this.rmhfRenderPanelInfo = rmhfRenderPanelInfo;
	}

	public Boolean getRmhfRenderPanelResultado() {
		return rmhfRenderPanelResultado;
	}

	public void setRmhfRenderPanelResultado(Boolean rmhfRenderPanelResultado) {
		this.rmhfRenderPanelResultado = rmhfRenderPanelResultado;
	}

	public List<Carrera> getRmhfListaCarrera() {
		rmhfListaCarrera = rmhfListaCarrera == null ? (new ArrayList<Carrera>()) : rmhfListaCarrera;
		return rmhfListaCarrera;
	}

	public void setRmhfListaCarrera(List<Carrera> rmhfListaCarrera) {
		this.rmhfListaCarrera = rmhfListaCarrera;
	}

	public RegistroHomologacionDto getRmhfPersonaDtoAgregarRegistro() {
		return rmhfPersonaDtoAgregarRegistro;
	}

	public void setRmhfPersonaDtoAgregarRegistro(RegistroHomologacionDto rmhfPersonaDtoAgregarRegistro) {
		this.rmhfPersonaDtoAgregarRegistro = rmhfPersonaDtoAgregarRegistro;
	}

	public List<Dependencia> getRmhfListaDependencia() {
		rmhfListaDependencia = rmhfListaDependencia == null ? (new ArrayList<Dependencia>()) : rmhfListaDependencia;
		return rmhfListaDependencia;
	}

	public void setRmhfListaDependencia(List<Dependencia> rmhfListaDependencia) {
		this.rmhfListaDependencia = rmhfListaDependencia;
	}

	public String getRmhfDetalleTipoIngreso() {
		return rmhfDetalleTipoIngreso;
	}

	public void setRmhfDetalleTipoIngreso(String rmhfDetalleTipoIngreso) {
		this.rmhfDetalleTipoIngreso = rmhfDetalleTipoIngreso;
	}

	public RegistroHomologacionDto getRmhfPersonaDtoNuevo() {
		return rmhfPersonaDtoNuevo;
	}

	public void setRmhfPersonaDtoNuevo(RegistroHomologacionDto rmhfPersonaDtoNuevo) {
		this.rmhfPersonaDtoNuevo = rmhfPersonaDtoNuevo;
	}

	public Integer getRmhfValidadorNuevoClic() {
		return rmhfValidadorNuevoClic;
	}

	public void setRmhfValidadorNuevoClic(Integer rmhfValidadorNuevoClic) {
		this.rmhfValidadorNuevoClic = rmhfValidadorNuevoClic;
	}

	public Boolean getRmhfDisabledGuardarAgregar() {
		return rmhfDisabledGuardarAgregar;
	}

	public void setRmhfDisabledGuardarAgregar(Boolean rmhfDisabledGuardarAgregar) {
		this.rmhfDisabledGuardarAgregar = rmhfDisabledGuardarAgregar;
	}

	public Boolean getRmhfRenderCarreras() {
		return rmhfRenderCarreras;
	}

	public void setRmhfRenderCarreras(Boolean rmhfRenderCarreras) {
		this.rmhfRenderCarreras = rmhfRenderCarreras;
	}

	public PeriodoAcademico getRmhfPeriodoAcademicoCierre() {
		return rmhfPeriodoAcademicoCierre;
	}

	public void setRmhfPeriodoAcademicoCierre(PeriodoAcademico rmhfPeriodoAcademicoCierre) {
		this.rmhfPeriodoAcademicoCierre = rmhfPeriodoAcademicoCierre;
	}

	public RegistroHomologacionDto getRmhfPersonaDtoEliminar() {
		return rmhfPersonaDtoEliminar;
	}

	public void setRmhfPersonaDtoEliminar(RegistroHomologacionDto rmhfPersonaDtoEliminar) {
		this.rmhfPersonaDtoEliminar = rmhfPersonaDtoEliminar;
	}

	public FichaEstudiante getRmhfFichaEstudianteExistente() {
		return rmhfFichaEstudianteExistente;
	}

	public void setRmhfFichaEstudianteExistente(FichaEstudiante rmhfFichaEstudianteExistente) {
		this.rmhfFichaEstudianteExistente = rmhfFichaEstudianteExistente;
	}

	public Integer getRmhfValidadorEliminarClic() {
		return rmhfValidadorEliminarClic;
	}

	public void setRmhfValidadorEliminarClic(Integer rmhfValidadorEliminarClic) {
		this.rmhfValidadorEliminarClic = rmhfValidadorEliminarClic;
	}

	public Integer getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(Integer tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	
	public List<CarreraDto> getRmhfListCarreraDtoSau() {
		rmhfListCarreraDtoSau = rmhfListCarreraDtoSau == null ? (new ArrayList<CarreraDto>()) : rmhfListCarreraDtoSau;
		return rmhfListCarreraDtoSau;
	}

	public void setRmhfListCarreraDtoSau(List<CarreraDto> rmhfListCarreraDtoSau) {
		this.rmhfListCarreraDtoSau = rmhfListCarreraDtoSau;
	}

	public Boolean getRmhfRenderCarrerasSau() {
		return rmhfRenderCarrerasSau;
	}

	public void setRmhfRenderCarrerasSau(Boolean rmhfRenderCarrerasSau) {
		this.rmhfRenderCarrerasSau = rmhfRenderCarrerasSau;
	}

	public String getRmhfNombrePersonaSau() {
		return rmhfNombrePersonaSau;
	}

	public void setRmhfNombrePersonaSau(String rmhfNombrePersonaSau) {
		this.rmhfNombrePersonaSau = rmhfNombrePersonaSau;
	}

	public String getRmhfIdentificacionPersonaSau() {
		return rmhfIdentificacionPersonaSau;
	}

	public void setRmhfIdentificacionPersonaSau(String rmhfIdentificacionPersonaSau) {
		this.rmhfIdentificacionPersonaSau = rmhfIdentificacionPersonaSau;
	}

	public Boolean getRmhfChkNotaEnes() {
		return rmhfChkNotaEnes;
	}

	public void setRmhfChkNotaEnes(Boolean rmhfChkNotaEnes) {
		this.rmhfChkNotaEnes = rmhfChkNotaEnes;
	}

	public Boolean getRmhfDisabledNotaEnes() {
		return rmhfDisabledNotaEnes;
	}

	public void setRmhfDisabledNotaEnes(Boolean rmhfDisabledNotaEnes) {
		this.rmhfDisabledNotaEnes = rmhfDisabledNotaEnes;
	}

	public Boolean getRmhfDisabledAplicaEnes() {
		return rmhfDisabledAplicaEnes;
	}

	public void setRmhfDisabledAplicaEnes(Boolean rmhfDisabledAplicaEnes) {
		this.rmhfDisabledAplicaEnes = rmhfDisabledAplicaEnes;
	}

	public NotaCorte getRmhfNotaCorte() {
		return rmhfNotaCorte;
	}

	public void setRmhfNotaCorte(NotaCorte rmhfNotaCorte) {
		this.rmhfNotaCorte = rmhfNotaCorte;
	}

		
	
	
}
