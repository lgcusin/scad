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
   
 ARCHIVO:     EditarHomologacionForm.java	  
 DESCRIPCION: Managed Bean que maneja edición del registro de las materias homologadas de los estudiantes.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
09-NOV-2018			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.homologacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoSistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RegistroHomologacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoSistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.SistemaCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSistemaCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EditarHomologacionForm. Managed Bean que maneja la edición del 
 * registro de las materias homologadas del estudiante.
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "editarHomologacionForm")
@SessionScoped
public class EditarHomologacionForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// Objetos
	// VISTA BUSCAR ESTUDIANTE--------
	private Usuario ehfUsuario;
	private PersonaDto ehfPersonaDtoBuscar; // Guardar la cedula
	private CarreraDto ehfCarreraDtoBuscar; // Guardar la carrera
	private List<RegistroHomologacionDto> ehfListaRegistroEstudiante;  //Lista de registros de homologación del estudiante
	private List<Rol> ehfListaRolesUsuario;
	private Boolean ehfRenderListCarreras ;
	private List<PeriodoAcademico> ehfListaPracActivoPosgrado;
	private PeriodoAcademico ehfPeriodoAcademicoIdioma;
	private PeriodoAcademico ehfPeriodoAcademicoCulturaFisica;
	
	//VISTA VER HOMOLOGACION---
	private RegistroHomologacionDto ehfRegistroPersonaVer;
	private List<MateriaDto> ehfListaMateriasHomologadasVer;
	private boolean ehfDeshabilitaOpcionesEditar; // editar
	private boolean ehfDeshabilitaOpcionEliminar;
	private boolean ehfDeshabilitaOpcionAgregar;
	
	//VISTA AGREGAR HOMOLOGACION
	private RegistroHomologacionDto ehfPersonaDtoAgregar;
	private Integer ehfValidadorClic;
	private ParaleloDto ehfParaleloHomologacion;// paralelo unico de homologación por carrera.
	private PeriodoAcademico ehfPeriodoAcademico;
	private MallaCurricular ehfMallaCurricular;
	private TipoSistemaCalificacion ehfTipoSistemaCalificacion;
	private SistemaCalificacion ehfSistemaCalificacion;
	private PeriodoAcademico ehfPeriodoAcademicoActivo;
	private PeriodoAcademico ehfPeriodoAcademicoInscripcion;
	private UsuarioRol ehfUsuarioRol;
	private Rol ehfRol;
	
	// Listas de Objetos
	private List<CarreraDto> ehfListCarreraDto; // Combo a seleccionar carreras
	private List<MateriaDto> ehfListMateriaDto;  //Lista de materias de la malla curricular.
	private List<ParaleloDto> ehfListParaleloDto; 
	private List<MateriaDto> ehfListHomologadosDto;//Lista de materias homologadas a guardar en BDD
	private List<MateriaDto> ehfListRecordTotal;
	
	//VISTA MODAL  EDITAR	
	private  Integer ehfValidadorClicEditar;
	private MateriaDto ehfMateriaDtoEditar;
	private boolean ehfDeshabilitaGuardarEdicion;
	
	//VISTA MODAL  ELIMINAR
	private MateriaDto ehfMateriaDtoEliminar;
	private  Integer ehfValidadorClicEliminar;
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {

	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	// Para las busquedas

	@EJB
	RegistroHomologacionDtoServicioJdbc servEhfRegistroHomologacionDto;
	@EJB
	RolServicio servEhfRol;
	@EJB
	PeriodoAcademicoServicio servEhfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servEhfCarreraDto;
	@EJB
	PersonaServicio servEhfPersona;
	@EJB
	MateriaDtoServicioJdbc servEhfMateriaDto;
	@EJB
	MallaCurricularServicio servEhfMallaCurricular;
	@EJB
	ParaleloDtoServicioJdbc servEhfParaleloDto;
	@EJB
	TipoSistemaCalificacionServicio servEhfTipoSistemaCalificacion;
	@EJB
	SistemaCalificacionServicio servEhfSistemaCalificacion;
	@EJB
	UsuarioRolServicio servEhfUsuarioRol;
	@EJB
	MatriculaServicio servEhfMatriculaServicio;
	@EJB
    CarreraServicio servEhfCarreraServicio;
	@EJB
	MateriaServicio servEhfMateriaServicio;
	@EJB
	RecordEstudianteDtoServicioJdbc servEhfRecordEstudianteSIIU;
	@EJB
	DetalleMatriculaServicio servEhfDetalleMatricula;
	@EJB
	ComprobantePagoServicio servEhfComprobantePago;	


	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	

	/**
	 * Método que permite ir a buscar el estudiante por identificación y carrera
	 * @param usuario   - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml buscar estudiantes a editar homologación
	 */

	public String irABuscarEstudiante(Usuario usuario) {
		ehfUsuario = usuario;
		iniciarParametros();
		return "irABuscarEstudiante";
	}

	/**
	 * Método para limpiar los parámetros de busqueda ingresados 
	 */
	public void limpiar() {
		iniciarParametros();
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instanciados al
	 * iniciar la funcionalidad
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		limpiar();
		FacesUtil.limpiarMensaje();
		return "irInicio";
	}
	
	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
		
		// Inicio los objetos    
		    ehfRenderListCarreras = false;
		    ehfPersonaDtoBuscar = new PersonaDto();
			ehfPersonaDtoBuscar.setPrsIdentificacion("");
			ehfCarreraDtoBuscar = new CarreraDto();
			ehfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
			ehfPeriodoAcademicoActivo= null;
			ehfPeriodoAcademicoIdioma= null;
			ehfPeriodoAcademicoCulturaFisica= null;
			ehfListaRolesUsuario= null;
			ehfRol= null;
			ehfListCarreraDto = new ArrayList<>();
			ehfValidadorClic = new Integer(0);
			ehfListaRegistroEstudiante=null;
			
			// seteo la carrera para que busque por todas
			//ehfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		    ehfPeriodoAcademicoInscripcion= null;
			List<CarreraDto> listCarrerasDtoAuxIdioma= new ArrayList<>();
			List<CarreraDto> listCarrerasDtoAuxCulturaFisica= new ArrayList<>();
			List<CarreraDto> listCarrerasDtoAuxPosgrado= new ArrayList<>();
			
			// busco el periodo academico activo pregrado
			try {
					ehfPeriodoAcademicoActivo = servEhfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					//BUSCO LA LISTA DE ROLES DEL USUARIO
					ehfListaRolesUsuario=servEhfRol.listarRolesXUsrId(ehfUsuario.getUsrId());	
			
			boolean rolValido = false;
			if(ehfListaRolesUsuario!=null){
			
			  //BUSCO EL ROL DE SECRETARIA DE CARRERA
				for (Rol rol : ehfListaRolesUsuario) {
				if(rol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE 
						|| rol.getRolId()==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE 
						|| rol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE 
					    || rol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE ){
									
						ehfRol=servEhfRol.buscarPorId(rol.getRolId());
				    	rolValido= true;
			          break;
						
				}
				
			  }
				
			}
			
			
			if(rolValido){ //SOLO ROLES: SECRECARRERA, SECRESUFICIENCIA, SECREPOSGRADO, SOPORTE
				
				if(ehfRol.getRolId()== RolConstantes.ROL_SECRECARRERA_VALUE ){ //CARRERAS DE PREGRADO
					
						ehfRenderListCarreras= true;  //Presento combo de listas de carreras	
						ehfListCarreraDto = servEhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(ehfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE,
								                                                                        RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, ehfPeriodoAcademicoActivo.getPracId());
						
			   }else if(ehfRol.getRolId()==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE){  //SUFICIENCIAS
				   
				   ehfRenderListCarreras= true;  //Presento combo de listas de carreras	
				   
					// busco el periodo academico activo Idioma
					ehfPeriodoAcademicoIdioma= servEhfPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				
					// busco los periodo academico activo Cultura Física
					ehfPeriodoAcademicoCulturaFisica=servEhfPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
				 //Busco carreras 
					if(ehfPeriodoAcademicoIdioma!=null){
						//Busco carreras En Suficiencia idiomas
						listCarrerasDtoAuxIdioma = servEhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(
						ehfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE,
						RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, ehfPeriodoAcademicoIdioma.getPracId());
					}
			
					if((listCarrerasDtoAuxIdioma!=null)&&(listCarrerasDtoAuxIdioma.size()>0)){
						ehfListCarreraDto.addAll(listCarrerasDtoAuxIdioma); //Agrego las carreras de Suficiencia idiomas
					}
			
					if(ehfPeriodoAcademicoCulturaFisica!=null){
						//Busco carreras En Suficiencia Cultura física
						listCarrerasDtoAuxCulturaFisica = servEhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(
						ehfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE,
						RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, ehfPeriodoAcademicoCulturaFisica.getPracId());
					}
			
					if((listCarrerasDtoAuxCulturaFisica!=null)&&(listCarrerasDtoAuxCulturaFisica.size()>0)){
						ehfListCarreraDto.addAll(listCarrerasDtoAuxCulturaFisica);  //Agrego las carreras de suficiencia CCFF
					}
				   
				   
			   }else if(ehfRol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE){ //POSGRADO
					
				   ehfRenderListCarreras= true;		//Presento combo de carreras		   
				   
				   // busco los periodo academico activo posgrado
					ehfListaPracActivoPosgrado = servEhfPeriodoAcademico.buscarXestadolistPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
						
					//Busco carreras si el usuario es secreposgrado
			
					if((ehfListaPracActivoPosgrado!=null)&&(ehfListaPracActivoPosgrado.size()>0)){
					listCarrerasDtoAuxPosgrado = servEhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXListaPrac(
							ehfUsuario.getUsrId(), RolConstantes.ROL_SECREPOSGRADO_VALUE,
							RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, ehfListaPracActivoPosgrado);
				  }
				
					if((listCarrerasDtoAuxPosgrado!=null)&&(listCarrerasDtoAuxPosgrado.size()>0)){
						ehfListCarreraDto.addAll(listCarrerasDtoAuxPosgrado); //agrego las carreras de posgrado
					}
				   
			   }else{ //TIENE ROL DE  SOPORTE
				   ehfListCarreraDto= new ArrayList<>(); //No importan las carreras, soporte debe tener acceso a todas las carreras
				   
				   ehfListCarreraDto = servEhfCarreraDto.listarCarrerasTodas();
				   
				   ehfRenderListCarreras= false;	 //Oculto lista de carreras	 
				   
			   }
			
			}
		
			} catch (PeriodoAcademicoNoEncontradoException e) {
				//Nunca puede saltar esta exception, servicio no la llama				
				
			} catch (PeriodoAcademicoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}catch (RolNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
			 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.iniciar.parametros.rol.no.encontrado.exception")));
			 
			}catch (RolException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} 
		
	}	
	
	/**
	 * Método para buscar los estudiantes con los parámetros ingresados en los filtros de busqueda
	 */
	public void buscar() {
		// buscar los estudiantes por carrera y descripción
		ehfListaRegistroEstudiante = new ArrayList<>();
		List<RegistroHomologacionDto> listaRegistrosHomologacion = new ArrayList<>();
		try {
			
			if (!ehfPersonaDtoBuscar.getPrsIdentificacion().trim().isEmpty()) {

				if (ehfRol.getRolId() == RolConstantes.ROL_SOPORTE_VALUE) {
                    //Realiza la busqueda en todas la carreras de la universidad
					//ehfListaRegistroEstudiante = servEhfRegistroHomologacionDto.listarXEstudiantesXcarreraXIdentificacion(ehfCarreraDtoBuscar.getCrrIdentificador(),ehfPersonaDtoBuscar.getPrsIdentificacion());
					listaRegistrosHomologacion = servEhfRegistroHomologacionDto.listarXEstudiantesXListaCarreraXIdentificacion(ehfListCarreraDto,ehfCarreraDtoBuscar.getCrrIdentificador(),ehfPersonaDtoBuscar.getPrsIdentificacion());
		               
					if(listaRegistrosHomologacion.size()>0){
            	   
						for (RegistroHomologacionDto registroHomologacionDto : listaRegistrosHomologacion) {
							ComprobantePago cmpaAux= servEhfComprobantePago.buscarPorFcmt(registroHomologacionDto.getFcmtId()); //por cada fcmt solo un comprobante
							List <DetalleMatricula> listDtmtAux=servEhfDetalleMatricula.listarXCmpaId(cmpaAux.getCmpaId());
							
							if(listDtmtAux!=null){
								ehfListaRegistroEstudiante.add(registroHomologacionDto);
								
							}
						}
            	   
                    }
					
					if(ehfListaRegistroEstudiante.size()>1){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("El estudiante presenta mas de un registro de homologación con asignaturas.");
						
					}
					
					
					
				} else if (ehfRol.getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE
						|| ehfRol.getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE) {
					
					//Si no selecciona la carrera busca en TODAS a las que tiene acceso la secre de carrera, suficiencia o posgrados
					listaRegistrosHomologacion = servEhfRegistroHomologacionDto.listarXEstudiantesXListaCarreraXIdentificacion(ehfListCarreraDto,ehfCarreraDtoBuscar.getCrrIdentificador(),ehfPersonaDtoBuscar.getPrsIdentificacion());
               
					if(listaRegistrosHomologacion.size()>0){
            	   
						for (RegistroHomologacionDto registroHomologacionDto : listaRegistrosHomologacion) {
							ComprobantePago cmpaAux= servEhfComprobantePago.buscarPorFcmt(registroHomologacionDto.getFcmtId()); //por cada fcmt solo un comprobante
							List <DetalleMatricula> listDtmtAux=servEhfDetalleMatricula.listarXCmpaId(cmpaAux.getCmpaId());
							
							if(listDtmtAux!=null){
								ehfListaRegistroEstudiante.add(registroHomologacionDto);
								
							}
						}
            	   
                    }
					
					if(ehfListaRegistroEstudiante.size()>1){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("El estudiante presenta mas de un registro de homologación con asignaturas.");
						
					}
					
					
					
					
				} else if(ehfRol.getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE){
					//Ver si es necesario desarrollar, falta crear consulta  por paralelo HISTORICO
					
					FacesUtil.limpiarMensaje();
					//No existe la  edicion de histórico de posgrados.
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.buscar.rol.secretaria.posgrado.validacion.exception")));
					
				}else {
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.buscar.rol.validacion.exception")));
					
				}

			} else {
				FacesUtil.limpiarMensaje();
			    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.buscar.identificacion.validacion.exception")));

			}
			
		} catch (RegistroHomologacionDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (RegistroHomologacionDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ComprobantePagoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ComprobantePagoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetalleMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que permite ver el registro de homologación del estudiante seleccionado
	 * @return navegación a la pagina para ver las homologaciones del estudiante
	 */
	
	public String irVerHomologacion(RegistroHomologacionDto registroHomologacion){
		String retorno= null;
		ehfRegistroPersonaVer = new RegistroHomologacionDto();
		ehfRegistroPersonaVer=registroHomologacion;
		ehfDeshabilitaOpcionesEditar = false;
		ehfDeshabilitaOpcionEliminar = false;
		ehfDeshabilitaOpcionAgregar = false;
		ehfValidadorClic= 0;
		ehfValidadorClicEditar= 0;
		ehfValidadorClicEliminar= 0;
		ehfTipoSistemaCalificacion= null;
		ehfSistemaCalificacion= null;
		ehfPeriodoAcademico= null;
		ehfRol= null;
		ehfUsuarioRol= null;
		ehfListaRolesUsuario = null;
		
	try {
	
		
		//BUSCO LA LISTA DE ROLES DEL USUARIO
		ehfListaRolesUsuario=servEhfRol.listarRolesXUsrId(ehfUsuario.getUsrId());	
		
		if(ehfListaRolesUsuario!=null){
			boolean rolEncontrado= false;
		  //BUSCO EL ROL DE SECRETARIA DE CARRERA
			for (Rol rol : ehfListaRolesUsuario) {
			if(rol.getRolId()==RolConstantes.ROL_SOPORTE_VALUE 
					||rol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE 
					|| rol.getRolId()==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE 
					|| rol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE){
				
				try {
					ehfRol=servEhfRol.buscarPorId(rol.getRolId());
				} catch (RolNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.rol.no.encontrado.exception")));
					return null;
				} catch (RolException e) {
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.rol.exception")));
					return null;
				}
				rolEncontrado = true;
				break;
			}
		}
		
		
		if(!rolEncontrado){
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.rol.validacion.exception")));
			return null;
		  }
		
		}else{
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.rol.no.encontrado.validacion.exception")));
			return null;
			
		}
		
		//Busco el usuario rol del usuario
		ehfUsuarioRol= servEhfUsuarioRol.buscarXUsuarioXrol(ehfUsuario.getUsrId(), ehfRol.getRolId());
		
		if(registroHomologacion!=null){
			
			// Busqueda del período academico HOMOLOGACION
			ehfPeriodoAcademico = servEhfPeriodoAcademico.buscarPDescripcion(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_LABEL);
			// buscar el tipo de sistema de calificaciones por la descripcion :"PREGRADO"
			ehfTipoSistemaCalificacion = servEhfTipoSistemaCalificacion.buscarXDescripcion(TipoSistemaCalificacionConstantes.SISTEMA_CALIFICACION_PREGRADO_LABEL);
			
			// buscar el sistema de calificaciones por el periodo academiico y por el tipo sistema de calificaciones
			ehfSistemaCalificacion = servEhfSistemaCalificacion.listarSistemaCalificacionXPracXtissclXEstado(ehfPeriodoAcademico.getPracId(), ehfTipoSistemaCalificacion.getTissclId(),	SistemaCalificacionConstantes.INACTIVO_VALUE);
			
			
		if(ehfRegistroPersonaVer.getFcinTipo()==FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE){
				//ehfDeshabilitaOpcionesEditar = false;// MQ: se cambia esto por pedido de producción  20 jun 2019
				//ehfDeshabilitaOpcionEliminar = false;// MQ: se cambia esto por pedido de producción  20 jun 2019
				
				ehfDeshabilitaOpcionesEditar = false;
				ehfDeshabilitaOpcionAgregar = false;
				ehfDeshabilitaOpcionEliminar = false;
			
	//		if (ehfRegistroPersonaVer.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE) {  //No es necsario pues se presenta solo fcmt con detalles de matricula
						
				ehfPeriodoAcademicoInscripcion = servEhfPeriodoAcademico.buscarPorId(ehfRegistroPersonaVer.getFcinPeriodoAcademico());
				
				ehfListaMateriasHomologadasVer = servEhfMateriaDto.listarHomologacionesXestudianteXmatriculaXperiodoXcarrera(
										ehfRegistroPersonaVer.getPrsId(), ehfRegistroPersonaVer.getFcmtId(),ehfRegistroPersonaVer.getFcmtPracId());
						
						if(ehfListaMateriasHomologadasVer.size()<=1){
							ehfDeshabilitaOpcionEliminar = true;
						}
						
				retorno= "irVerHomologacion";
		  
//		     }else{
//			  FacesUtil.limpiarMensaje();
//			    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.estado.homologado.validacion.exception")));
//			
//		    }
		
			
		}else if(ehfRegistroPersonaVer.getFcinTipo()==FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE){
		
			ehfDeshabilitaOpcionesEditar = true;//No se puede realizar edicion
			ehfDeshabilitaOpcionEliminar = true;
			ehfDeshabilitaOpcionAgregar = true;
			
			if (ehfRegistroPersonaVer.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE) {
				ehfPeriodoAcademicoInscripcion = servEhfPeriodoAcademico.buscarPorId(ehfRegistroPersonaVer.getFcinPeriodoAcademico());
				
				ehfListaMateriasHomologadasVer = servEhfMateriaDto.listarHomologacionesXestudianteXmatriculaXperiodoXcarrera(
										ehfRegistroPersonaVer.getPrsId(), ehfRegistroPersonaVer.getFcmtId(),ehfRegistroPersonaVer.getFcmtPracId());
						
						if(ehfListaMateriasHomologadasVer.size()<=1){
							ehfDeshabilitaOpcionEliminar = true;
						}
						
				retorno= "irVerHomologacion"; //Se debe cambiar la vista
		  
		     }else{
			  FacesUtil.limpiarMensaje();
			    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.estado.sin.homologar.validacion.exception")));
			
		    }
		
			
			
		}else if (ehfRegistroPersonaVer.getFcinTipo()==FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE){
			ehfDeshabilitaOpcionesEditar = true; //No se puede realizar edicion
			ehfDeshabilitaOpcionEliminar = true;
			ehfDeshabilitaOpcionAgregar = true;
			
		}else{
			ehfDeshabilitaOpcionesEditar = true; //No se puede realizar edicion
			ehfDeshabilitaOpcionEliminar = true;
			ehfDeshabilitaOpcionAgregar = true;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.tipo.inscrito.validacion.exception")));
			
		}
			
		 }else{
			 FacesUtil.limpiarMensaje();
			 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.error.validacion.exception")));
		 }
		
	  } catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.periodo.no.encontrado.exception")));
	  } catch (PeriodoAcademicoException e) {
		  FacesUtil.limpiarMensaje(); 
		  FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.periodo.exception")));
	  } catch (MateriaDtoException e) {
		FacesUtil.limpiarMensaje();
		  FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.materiaDto.exception")));
	} catch (MateriaDtoNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
	    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.materiaDto.no.encontrado.exception")));
	} catch (TipoSistemaCalificacionNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
	   FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.TipoSistemaCalificacion.no.encontrado.exception")));
		return null;
	} catch (TipoSistemaCalificacionException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(e.getMessage());
		return null;
	} catch (SistemaCalificacionNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.sistemaCalificacion.no.encontrado.exception")));
		return null;
	} catch (SistemaCalificacionException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(e.getMessage());
	} catch (UsuarioRolException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.usuario.rol.exception")));
		return null;
	} catch (UsuarioRolNoEncontradoException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irVerHomologacion.usuario.rol.no.encontrado.exception")));
		return null;
	
	}
		
		return  retorno;
	}

	
	
	/**
	 * Método para inicar el registro de agregar asignaturas a homologar del estudiante seleccionado
	 * @return navegación a la pagina de agregar las homologaciones del estudiante
	 */
	public String irAgregarHomologacion(RegistroHomologacionDto registroPersonaVer) {
		//VARIABLES
		ehfPersonaDtoAgregar = new RegistroHomologacionDto();
		ehfPersonaDtoAgregar=registroPersonaVer;
		
		ehfValidadorClic = 0;
		  
//	    //LISTAS
	    ehfListMateriaDto= new ArrayList<>();
	 	ehfListHomologadosDto = new ArrayList<MateriaDto>(); //nuevas asignaturas homologadas
	 	ehfListRecordTotal = new ArrayList<>();
		
		//LISTAS AUXILIARES
		List<MateriaDto> listMateriasMallaAux = new ArrayList<MateriaDto>();
		List<RecordEstudianteDto> recordEstudianteSIIUAux = new ArrayList<>();
		//List<RecordEstudianteSAUDto> recordEstudianteSauAux = new ArrayList<>();		    				
		//OBJETOS AUXILIARES
		Carrera carreraAux = new Carrera(); 
		try {
				
			// Busqueda del objeto periodo academico de la fichaInscripcion del estudiante, por el id_prac de la fcin
			if(ehfPersonaDtoAgregar.getFcinPeriodoAcademico()!=null){
			ehfPeriodoAcademicoInscripcion= servEhfPeriodoAcademico.buscarPorId(ehfPersonaDtoAgregar.getFcinPeriodoAcademico());
			}
			
		 	//BUSCO LA CARRERA CON crr_id de la ficha Inscripcion
            carreraAux = servEhfCarreraServicio.buscarPorId(ehfPersonaDtoAgregar.getCrrId());
			
			// Buscar la malla curricular vigente y activa de la carrera
			ehfMallaCurricular = servEhfMallaCurricular.buscarXcarreraXvigenciaXestado(ehfPersonaDtoAgregar.getCrrId(),MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			
			//Busco materias de toda la malla
		 //	listMateriasMallaAux =   servEhfMateriaDto.listarXmalla(ehfMallaCurricular.getMlcrId());
			   
			//habilitar al finalizar periodo de editar homologaciones, no se debe poder homologar materias hijas
			  listMateriasMallaAux =   servEhfMateriaDto.listarXmallaSinAsignaturasModulo(ehfMallaCurricular.getMlcrId());
			
			
			//CARGO PARARLELO HOMOLOGACION A TODAS LAS MATERIAS DE LA MALLA
			for (MateriaDto itmMateria : listMateriasMallaAux) {
				//BUSCO EL PARALELO HOMOLOGACiÓN  o PARALELO HISTORICO  SEGUN EL CASO
				if(carreraAux.getCrrTipo()==CarreraConstantes.TIPO_PREGRADO_VALUE){
				ehfParaleloHomologacion = servEhfParaleloDto.buscarXmallaMateriaXperiodoXDescripcion(
						itmMateria.getMlcrmtId(), PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE,ParaleloConstantes.PARALELO_HOMOLOGACION_LABEL);
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.tipo.carrera.validacion.exception")));
					return null;
					
				}
				itmMateria.setPrlId(ehfParaleloHomologacion.getPrlId()); //Se agrega a cada materia el paralelo homologación
				itmMateria.setPrlDescripcion(ehfParaleloHomologacion.getPrlDescripcion());
				itmMateria.setMlcrprId(ehfParaleloHomologacion.getMlcrprId());
				itmMateria.setEsHomologado(false);
				// seteo el numero de matricula a primera
				itmMateria.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
				// asignar valores por defecto de las notas
				itmMateria.setNotaUno(null);
				itmMateria.setNotaDos(null);
				itmMateria.setNotaTres(null);
				itmMateria.setClfPromedioAsistencia(null);
				itmMateria.setNotaSuma(null);
				itmMateria.setAprobado(false);
				itmMateria.setMtrCmbEstado(true);
				itmMateria.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
			}

		        
			
			       //BUSCO RECORD EN SIIU, EN CARRERA A LA QUE INGRESA  MENOS LAS DEL PERIODO ACTIVO
		           // recordEstudianteSIIUAux=servEhfRecordEstudianteSIIU.buscarXidentificacionXcarrera(ehfPersonaDtoAgregar.getPrsIdentificacion(), carreraAux.getCrrId());
		
			//BUSCAMOS todos los RECORD DEL ESTUDIANTE EN EL siiu
			recordEstudianteSIIUAux=servEhfRecordEstudianteSIIU.buscarXidentificacionXcarreraTodas(ehfPersonaDtoAgregar.getPrsIdentificacion(), carreraAux.getCrrId());
			
		    //AGREGO LA LISTA DE RECORD SIIU A RECORD TOTAL
			if((recordEstudianteSIIUAux!=null)&&(recordEstudianteSIIUAux.size()>0))	{	
		    	for (RecordEstudianteDto recordEstudianteSiiu : recordEstudianteSIIUAux) {
			//	if (recordEstudianteSiiu.getRcesEstado()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
					MateriaDto materiaDtoAux= new MateriaDto();
					 Materia materiaAux = new Materia();
					 try {
						materiaAux= servEhfMateriaServicio.buscarPorId(recordEstudianteSiiu.getMlcrmtMtrId()) ;
					} catch (MateriaNoEncontradoException e) {
						materiaAux= null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.materia.no.encontrado.exception")));
					} catch (MateriaException e) {
						materiaAux= null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.materia.exception")));
					}
					
					 if(materiaAux!=null){
					 materiaDtoAux.setMtrId(materiaAux.getMtrId());
					 materiaDtoAux.setMtrCodigo(materiaAux.getMtrCodigo());
					 materiaDtoAux.setMtrDescripcion(materiaAux.getMtrDescripcion());
				     ehfListRecordTotal.add(materiaDtoAux);
					 }
			//	}
				
			   } //fin for
			
		    }	
			
			// AGREGO  a la lista de materias para homologar las que no esten tomdas(EN EL RECORD)
			boolean encontrado=false; 
			for (MateriaDto MateriaDtoMalla: listMateriasMallaAux) {
				for (MateriaDto MateriaTomada : ehfListRecordTotal) {
					
					if(MateriaTomada.getMtrId()==MateriaDtoMalla.getMtrId()){
						encontrado= true;
						break;
					}
					
				}
				
				if(encontrado==false){
					ehfListMateriaDto.add(MateriaDtoMalla); //lista de materias que se pueden agregar a las homologaciones
				}
				encontrado=false;
			}
			

		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.periodo.academico.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {    
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.malla.curricular.no.encontrado.exception")));
			return null;
		} catch (MallaCurricularException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
     		FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.materiaDto.no.encontrado.exception")));
			return null;
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.paralelo.no.encontrado.exception")));
			return null;
		}    catch (RecordEstudianteDtoException e)	{
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}  catch (CarreraNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} catch (CarreraException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} 
		
		
	
	 
   if(ehfPersonaDtoAgregar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_VALUE){
	    FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.tipo.ingreso.posgrado.validacion.exception")));
	    // return "irRegistrarPosgrado"; //vista para registrar historial de posgrado.
		return null;
     }else  if(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
    	
    	FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.tipo.ingreso.idiomas.validacion.exception")));
 		 //return "irAgregarUbicacion"; //vista para registrar ubicación del nivel de ingles.
 		return null;
    	 
     }else if((ehfPersonaDtoAgregar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE)){
    	 FacesUtil.limpiarMensaje();
 		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.tipo.ingreso.culturafisica.validacion.exception")));
  		// return "irRegistrarSufCulturaFisica"; 
    	 return null;
     
     
     } else if((ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE) 
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)
    		 ||(ehfPersonaDtoAgregar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE)
    		 ){
		
    	 return "irAgregarHomologacion"; // vista para registrar homologaciones
		
            }else{
         	FacesUtil.limpiarMensaje();
	    	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.tipo.ingreso.validacion.exception")));
	    	return null;
           }
     
	    
	}

	/**
	 * Método para crear listado de asignaturas en las que el estudiante aun no se matrícula.
	 * @return listado de asignaturas en las que el estudiante aun no se matrícula.
	 */
	
	public List<MateriaDto> generaListaAsignaturasNoTomadas(){
		List<MateriaDto> listaRetorno = new ArrayList<>();
		ehfListRecordTotal = new ArrayList<>();
		
		  //LISTAS
	    
		ehfListHomologadosDto = new ArrayList<MateriaDto>();
		
		//LISTAS AUXILIARES
		List<MateriaDto> listMateriasMallaAux = new ArrayList<MateriaDto>();
		List<RecordEstudianteDto> recordEstudianteSIIUAux = new ArrayList<>();
		//List<RecordEstudianteSAUDto> recordEstudianteSauAux = new ArrayList<>();		    				
		//OBJETOS AUXILIARES
		Carrera carreraAux = new Carrera(); 
		
		try {
			
			// Busqueda del objeto periodo academico de la fichaInscripcion del estudiante, por el id_prac de la fcin
			if(ehfPersonaDtoAgregar.getFcinPeriodoAcademico()!=null){
			ehfPeriodoAcademicoInscripcion= servEhfPeriodoAcademico.buscarPorId(ehfPersonaDtoAgregar.getFcinPeriodoAcademico());
			}
			
		 	//BUSCO LA CARRERA CON crr_id de la ficha Inscripcion
            carreraAux = servEhfCarreraServicio.buscarPorId(ehfPersonaDtoAgregar.getCrrId());
			
			// Buscar la malla curricular vigente y activa de la carrera
			ehfMallaCurricular = servEhfMallaCurricular.buscarXcarreraXvigenciaXestado(ehfPersonaDtoAgregar.getCrrId(),MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			
			//Busco materias de toda la malla
		 //	listMateriasMallaAux =   servEhfMateriaDto.listarXmalla(ehfMallaCurricular.getMlcrId());
			   
			//habilitar al finalizar periodo de editar homologaciones, no se debe poder homologar materias hijas
			  listMateriasMallaAux =   servEhfMateriaDto.listarXmallaSinAsignaturasModulo(ehfMallaCurricular.getMlcrId());
			
			
			//CARGO PARARLELO HOMOLOGACION A TODAS LAS MATERIAS DE LA MALLA
			for (MateriaDto itmMateria : listMateriasMallaAux) {
				//BUSCO EL PARALELO HOMOLOGACiÓN  o PARALELO HISTORICO  SEGUN EL CASO
				if(carreraAux.getCrrTipo()==CarreraConstantes.TIPO_PREGRADO_VALUE){
				ehfParaleloHomologacion = servEhfParaleloDto.buscarXmallaMateriaXperiodoXDescripcion(
						itmMateria.getMlcrmtId(), PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE,ParaleloConstantes.PARALELO_HOMOLOGACION_LABEL);
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.tipo.carrera.validacion.exception")));
					return null;
					
				}
				itmMateria.setPrlId(ehfParaleloHomologacion.getPrlId()); //Se agrega a cada materia el paralelo homologación
				itmMateria.setPrlDescripcion(ehfParaleloHomologacion.getPrlDescripcion());
				itmMateria.setMlcrprId(ehfParaleloHomologacion.getMlcrprId());
				itmMateria.setEsHomologado(false);
				// seteo el numero de matricula a primera
				itmMateria.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
				// asignar valores por defecto de las notas
				itmMateria.setNotaUno(null);
				itmMateria.setNotaDos(null);
				itmMateria.setNotaTres(null);
				itmMateria.setClfPromedioAsistencia(null);
				itmMateria.setNotaSuma(null);
				itmMateria.setAprobado(false);
				itmMateria.setMtrCmbEstado(true);
				itmMateria.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
			}

		        
			
			       //BUSCO RECORD EN SIIU, EN CARRERA A LA QUE INGRESA  MENOS LAS DEL PERIODO ACTIVO
		           // recordEstudianteSIIUAux=servEhfRecordEstudianteSIIU.buscarXidentificacionXcarrera(ehfPersonaDtoAgregar.getPrsIdentificacion(), carreraAux.getCrrId());
		
			//BUSCAMOS TODOS LOS RECORD DEL ESTUDIANTE EN EL siiu
			recordEstudianteSIIUAux=servEhfRecordEstudianteSIIU.buscarXidentificacionXcarreraTodas(ehfPersonaDtoAgregar.getPrsIdentificacion(), carreraAux.getCrrId());
			
		    //AGREGO LA LISTA DE RECORD SIIU A RECORD TOTAL
			if((recordEstudianteSIIUAux!=null)&&(recordEstudianteSIIUAux.size()>0))	{	
		    	for (RecordEstudianteDto recordEstudianteSiiu : recordEstudianteSIIUAux) {
			//	if (recordEstudianteSiiu.getRcesEstado()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
					MateriaDto materiaDtoAux= new MateriaDto();
					 Materia materiaAux = new Materia();
					 try {
						materiaAux= servEhfMateriaServicio.buscarPorId(recordEstudianteSiiu.getMlcrmtMtrId()) ;
					} catch (MateriaNoEncontradoException e) {
						materiaAux= null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.materia.no.encontrado.exception")));
					} catch (MateriaException e) {
						materiaAux= null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.materia.exception")));
					}
					
					 if(materiaAux!=null){
					 materiaDtoAux.setMtrId(materiaAux.getMtrId());
					 materiaDtoAux.setMtrCodigo(materiaAux.getMtrCodigo());
					 materiaDtoAux.setMtrDescripcion(materiaAux.getMtrDescripcion());
				     ehfListRecordTotal.add(materiaDtoAux);
					 }
			//	}
				
			   } //fin for
			
		    }	
			
			// AGREGO  a la lista de materias para homologar las que no esten tomdas(EN EL RECORD)
			boolean encontrado=false; 
			for (MateriaDto MateriaDtoMalla: listMateriasMallaAux) {
				for (MateriaDto MateriaTomada : ehfListRecordTotal) {
					
					if(MateriaTomada.getMtrId()==MateriaDtoMalla.getMtrId()){
						encontrado= true;
						break;
					}
					
				}
				
				if(encontrado==false){
					listaRetorno.add(MateriaDtoMalla); //lista de materias que se pueden agregar a las homologaciones
				}
				encontrado=false;
			}
			

		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.periodo.academico.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {    
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.malla.curricular.no.encontrado.exception")));
			return null;
		} catch (MallaCurricularException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
     		FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.materiaDto.no.encontrado.exception")));
			return null;
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irAgregarHomologacion.paralelo.no.encontrado.exception")));
			return null;
		}    catch (RecordEstudianteDtoException e)	{
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}  catch (CarreraNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} catch (CarreraException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} 
		
		return listaRetorno;
	}
	
	/**
	 * Método para verificar si la asignatura indicada presenta una matricula posterior a la homologación.
	 * @return true/false luego de  verificar si la asignatura indicada presenta una matricula posterior a la homologación..
	 */
	
	public boolean asignaturaEnRecord(MateriaDto materiaEditada){
		boolean retorno = false;
		
		List<RecordEstudianteDto> recordEstudianteSIIUAux = new ArrayList<>();
		List<RecordEstudianteDto> recordEstudianteSinHomologadas = new ArrayList<>(); //Asignaturas homologadas como reprobadas en las que el estudiantes se matricula nuevamente
		
		try {
				
			recordEstudianteSIIUAux=servEhfRecordEstudianteSIIU.buscarXidentificacionXcarreraTodas(ehfRegistroPersonaVer.getPrsIdentificacion(), ehfRegistroPersonaVer.getCrrId());
		
		
			for (RecordEstudianteDto recordEstudianteDto : recordEstudianteSIIUAux) {
		     	for (MateriaDto materiaDto : ehfListaMateriasHomologadasVer) {
					//Si una asignatura en el record que fue homologada consta con otra matricula añado a la lista
                   if((recordEstudianteDto.getMtrId()==materiaDto.getMtrId())&&(recordEstudianteDto.getRcesMallaCurricularParalelo()!=materiaDto.getMlcrprId()))	{
                	   
                	   recordEstudianteSinHomologadas.add(recordEstudianteDto);//Solo asignaturas homologadas como reprobadas y que se tomaron nuevamente
                   }
				
		
			    }
		
		    }
			
			
			if(recordEstudianteSinHomologadas.size()>0){
			for (RecordEstudianteDto recordEstudianteDto : recordEstudianteSinHomologadas) {
				//verifico si la asignatura a editar esta en la lista de asignaturas tomadas en nueva matricula
				if(materiaEditada.getMtrId()==recordEstudianteDto.getMtrId()){
					retorno= true;
					break;
				}
				
			}
			
			}
	
		
		} catch (RecordEstudianteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
			
		return retorno;
			
		
	}
	
	
	
	/**
	 * Método para guardar asignaturas homologadas nuevas del estudiante en BDD.
	 * @return navegación a la pagina xhtml. ver registro homologación del estudiante.
	 */
	public String guardarAgregarHomologacion() {
		String retorno= null;
			ehfValidadorClic = new Integer(0);
			try {
				
				if(servEhfMatriculaServicio.generarAgregarEnMatriculaHomologacion(ehfListHomologadosDto, ehfPersonaDtoAgregar, ehfUsuarioRol)){
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.guardarAgregarHomologacion.exito")));
				
				retorno=irVerHomologacion(ehfRegistroPersonaVer);
				
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.guardarAgregarHomologacion.sin.exito")));
				
				}
				
			} catch (MatriculaValidacionException e) {
				ehfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			} catch (MatriculaException e) {
				ehfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			}
			
			return  retorno;
	}


	/**
	 * Método para ir al modal de edicion de notas de la materias homologadas del estudiante.
	 * @return void.
	 */
	
	public void irModalEditarHomologacion(MateriaDto materiaEditar){
		
		ehfMateriaDtoEditar= new MateriaDto();
		ehfMateriaDtoEditar = materiaEditar;
		ehfDeshabilitaGuardarEdicion= false;
		boolean existeRecordAsignatura =false;
		
		existeRecordAsignatura =asignaturaEnRecord(materiaEditar);
		
		
	if(!existeRecordAsignatura){
		//INICIALIZO UNO A UNO LOS CAMPOS, PUES CAMBIA LOS NOMBRES DEL OBJETO DE BDD Y NECESITO EJECUTAR METODOS CON ESTOS NOMBRES
		ehfMateriaDtoEditar.setNumMatricula(materiaEditar.getDtmtNumero());
		ehfMateriaDtoEditar.setEstadoHomologacion(materiaEditar.getRcesEstado());
		ehfMateriaDtoEditar.setNotaUno(BigDecimal.valueOf(materiaEditar.getClfNota1()).setScale(2, RoundingMode.HALF_DOWN)) ; 
		ehfMateriaDtoEditar.setNotaDos(BigDecimal.valueOf(materiaEditar.getClfNota2()).setScale(2, RoundingMode.HALF_DOWN)) ; 
		ehfMateriaDtoEditar.setNotaSuma(BigDecimal.valueOf(materiaEditar.getClfNotaFinalSemestre()).setScale(2, RoundingMode.HALF_DOWN));	
		
		if(materiaEditar.getRcesEstado()==RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE){
		//ehfMateriaDtoEditar.setMtrCmbEstado(true); //deshabilito combo numero matricula  MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
			ehfMateriaDtoEditar.setMtrCmbEstado(false);
		ehfMateriaDtoEditar.setAprobado(true);
		}else if(materiaEditar.getRcesEstado()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
			ehfMateriaDtoEditar.setMtrCmbEstado(false);  //habilito  combo numero de matricula
			ehfMateriaDtoEditar.setAprobado(false);
		}else{
			ehfMateriaDtoEditar.setMtrCmbEstado(false);	 //habilito  combo numero de matricula
			ehfMateriaDtoEditar.setAprobado(false);
		}
		
       // ehfMateriaDtoEditar.setNotaUno(new BigDecimal(Float.toString(materiaEditar.getClfNota2())));// TAMBIEN SIRVE DE ESTA FORMA, no  llena de decimales cuanto termina en decimales impares
		//System.out.println("Op3  "+ehfMateriaDtoEditar.getNotaUno());
		
	ehfValidadorClicEditar = 1;  //Habilita modal Editar
	
	}else{
		
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("El registro de matrícula de la asignatura es posterior a la homologación, no es posible la edición");
		
		
	}
	
	
	}
	
	
	
	/**
	 * Método para guardar la edicion de notas de la materias homologadas del estudiante en BDD.
	 * @return navegación a la pagina xhtml. ver registro homologación.
	 */
	public String guardarEditarHomologacion(MateriaDto materiaEditada) {
		String retorno = null;
		ehfDeshabilitaGuardarEdicion= false;
		
		if(materiaEditada.getNumMatricula()!=GeneralesConstantes.APP_ID_BASE){
			
			
			if(validarCasosDeUsoEditar(materiaEditada)){
				
				
				try {
					//Busco el detalle de matricula a editar
					DetalleMatricula detalleAux = servEhfDetalleMatricula.buscarXCmpaIdxMlcrprId(materiaEditada.getCmpaId(), materiaEditada.getMlcrprId());
					materiaEditada.setDtmtId(detalleAux.getDtmtId());// Guardo el Id de dtmt  en el objeto para enviar a editar
					
					if(servEhfMatriculaServicio.generarEditarEnMatriculaHomologacion(materiaEditada,  ehfUsuarioRol)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("mensaje","Registro actualizado con éxito");
						ehfDeshabilitaGuardarEdicion= true;
						ehfValidadorClicEditar = 0;
					
					}else{
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("mensaje","Se produjo un error al editar el registro, no se actualizo el registro.");
						
					}
				}  catch (MatriculaException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("mensaje","Se produjo un error al guardar la información editada, no se actualizo el registro.");
					
				} catch (DetalleMatriculaException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("mensaje","Error desconocido al buscar detalle matricula por comprobante y malla curricular paralelo.");
					
				} catch (DetalleMatriculaNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("mensaje","No se encontró detalle matrícula al buscar por comprobante y malla curricular paralelo.");
					
				}
			
				}
			
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje","Ingrese el número de matrícula");
			
		}
		
		return retorno;
	}
	
	
	/**
	 * Método para ir al modal eliminar  un registro de homologación
	 * 
	 */
	
public void irModalEliminarHomologacion(MateriaDto materiaEliminar){
		
		ehfMateriaDtoEliminar= new MateriaDto();
		ehfMateriaDtoEliminar = materiaEliminar;

        boolean existeRecordAsignatura =false;
	existeRecordAsignatura =asignaturaEnRecord(materiaEliminar);
	
	if(!existeRecordAsignatura)	{
		try {
			//Busco el detalle de matricula a Eliminar
			DetalleMatricula detalleAux;
			detalleAux = servEhfDetalleMatricula.buscarXCmpaIdxMlcrprId(materiaEliminar.getCmpaId(), materiaEliminar.getMlcrprId());
			materiaEliminar.setDtmtId(detalleAux.getDtmtId());// Guardo el Id de dtmt  en el objeto para enviar a editar
		
			
			ehfValidadorClicEliminar = 1;  //Habilita modal Eliminar
		
		
		}catch (DetalleMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irModalEliminarHomologacion.detalle.matricula.exception")));
			
		} catch (DetalleMatriculaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.irModalEliminarHomologacion.detalle.matricula.no.encontrado.exception")));
			
		}
		
    }else{
	
	FacesUtil.limpiarMensaje();
	FacesUtil.mensajeError("El registro de matrícula de la asignatura es posterior a la homologación, no es posible la eliminación");
	
	
}
		
		
	}


/**
 * Método para  eliminar  un registro de homologación en la BDD.
 * @return navegación a la pagina xhtml. ver registro homologación.
 */

public String EliminarRegistroEnHomologacion(){
	String retorno = null;
	
	//Se debe verificar que no tenga otra matricula con la misma asignatura... para permitir eliminar.
	ehfValidadorClicEliminar = 0;
	
	try {
		
		if(servEhfMatriculaServicio.generarEliminarEnMatriculaHomologacion(ehfMateriaDtoEliminar,  ehfUsuarioRol)){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.EliminarHomologacion.con.exito")));
		
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.EliminarHomologacion.sin.exito")));
			
		}
	}  catch (MatriculaException e) {
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.EliminarHomologacion.matricula.exception")));
		
	} 
	
	retorno= irVerHomologacion(ehfRegistroPersonaVer);
	
	return retorno;
	
}


	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	
	/**
	 * Verifica información al hacer click en el botón guardar agregar.
	 */
	public void verificarClickAgregar() {
		//OBJETOS
		//LISTAS
		ehfListHomologadosDto = new ArrayList<>();
	    //VARIABLE
		ehfValidadorClic = 0;
		//AUXILIARES

					for (MateriaDto it : ehfListMateriaDto) {
						if (it.getEsHomologado()==true) {
							ehfListHomologadosDto.add(it); //agrego las materias selecionadas y con notas a la lista de materias homologadas
						}
					}


					//Verifico que exista alguna materia homologada para permitir guardar.
					if (ehfListHomologadosDto.size() <= 0) {
						ehfValidadorClic = 0;  //inactivo modal guardar
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.verificarClickAgregar.homologacion.vacia.validacion.exception")));
					} else {
						
						//Valido el numero de matricula lleno
                      boolean validacionNumMatricula = false;
						for (MateriaDto it : ehfListHomologadosDto) {
							if (it.getNumMatricula()!=GeneralesConstantes.APP_ID_BASE) {
								validacionNumMatricula= true;
							}else {
								validacionNumMatricula= false;
								break;
							}
						}
						
						//Valido   las notas ingresadas
						boolean validacionFinal = false;
						for (MateriaDto it1 : ehfListHomologadosDto) {
							if (vaidarCasosDeUso(it1)) {//verifico nuevamente que todos las materia seleccionadas tengan notas.
							   validacionFinal= true;
							}else {
								 validacionFinal= false;
								break;
							}
						}
						
						
					if(validacionNumMatricula)	{
						if(validacionFinal){
						  //TODO:
					     // llamarReporte();
					     ehfValidadorClic = 1;  //activo modal guardar agregar
						} else{
						   ehfValidadorClic = 0;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.verificarClickAgregar.notas.validacion.final.exception")));
					   }
					}else{
						ehfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.verificarClickAgregar.numero.matricula.validacion.exception")));
					}
						
						
				}

	}

	
	/**
	 * Permite validar los que la información ingresada cumpla con los parametros establecidos al agregar.
	 */
	private boolean vaidarCasosDeUso(MateriaDto it) {
		boolean caso = true;
	//	if (it.getNotaUno() == null || it.getNotaUno().equals(new BigDecimal(0))) {
		if (it.getNotaUno() == null ) {
			caso = false;
			ehfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.vaidarCasosDeUso.nota1.validacion.exception")));
		}

//		if (it.getNotaDos()== null || it.getNotaDos().equals(new BigDecimal(0))) {
	   if (it.getNotaDos()== null) {
			caso = false;
			ehfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.vaidarCasosDeUso.nota2.validacion.exception")));
		}

		
		return caso;
	}
	
	
	
	/**
	 * calcula el promedio de las dos notas al agregar
	 */
	public void calcularPromedio(MateriaDto materiaDto) {

		if (vaidarCasosDeUso(materiaDto)) {
			BigDecimal notaSuma = materiaDto.getNotaUno().add(materiaDto.getNotaDos());
						
			BigDecimal valor = new BigDecimal(27.5);
			BigDecimal sumaredond = new BigDecimal(0);
			
			if(notaSuma.compareTo(valor)>=0){
				sumaredond=	notaSuma.setScale(0, RoundingMode.HALF_UP);	
			}else{
				sumaredond=notaSuma;
			}
						
			materiaDto.setNotaSuma(sumaredond);
			
			int result = sumaredond.compareTo(new BigDecimal(ehfSistemaCalificacion.getSsclNotaMinimaAprobacion().toString()));
			
			if (result >= 0) {
				// aprobado por notas
				// materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula   MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
				materiaDto.setMtrCmbEstado(Boolean.FALSE);
				materiaDto.setAprobado(true); // seteo el aprobado de la materia
				//materiaDto.setNumMatricula(0);
				//materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);// Constante para num matricula  MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
				//estado de homologacion varia si es suficiencia de cultura fisica.
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE);
					
			} else {
				// reprobado por notas
				materiaDto.setMtrCmbEstado(Boolean.FALSE); //habilita vista de combo NumMatricula
				materiaDto.setAprobado(false); // seteo el reprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				//materiaDto.setNumMatricula(0);
				//materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);  //MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
				
			}
			
		}
		
	}
	
	
	/**
	 * método que setear campos  al checkear es homologado en cada fila
	 */

	public void setearCampos(MateriaDto materia) {
  
		if (materia.getEsHomologado()) {
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EditarHomologacion.setearCampos.notas.validacion.exception")));
		}else {
			materia.setNotaUno(null);
			materia.setNotaDos(null);
			materia.setNotaSuma(null);
			materia.setClfPromedioAsistencia(null);
			materia.setMtrCmbEstado(Boolean.TRUE);
			materia.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
			materia.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
			
		}
	
	}
	
//	/**
//		 * llama a generar el reporte PDF
//		 */
//		public void llamarReporte(){
//			
//			ReporteHomologacionForm.generarReporteHomologacion(ehfListHomologadosDto, ehfUsuario, ehfPersonaDtoAgregar );
//			ehfActivadorReporte = 1;
//							
//		}
		
	
	
	
	/**
	 * Permite validar los que la información ingresada cumpla con los parametros establecidos al editar.
	 */
	private boolean validarCasosDeUsoEditar(MateriaDto it) {
		boolean caso = true;
	//	if (it.getNotaUno() == null || it.getNotaUno().equals(new BigDecimal(0))) {
		if (it.getNotaUno() == null ) {
			caso = false;
			ehfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje","Ingrese valores  en el Campo Nota 1.");
		}

//		if (it.getNotaDos()== null || it.getNotaDos().equals(new BigDecimal(0))) {
	   if (it.getNotaDos()== null) {
			caso = false;
			ehfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje","Ingrese valores en el Campo Nota 2.");
		}

		
		return caso;
	}
	
	
	
	/**
	 * calcula el promedio de las dos notas al editar, los mensajes de error deben aparecer en el modal.
	 */
	public void calcularPromedioEditar(MateriaDto materiaDto) {

		if (validarCasosDeUsoEditar(materiaDto)) {
			BigDecimal notaSuma = materiaDto.getNotaUno().add(materiaDto.getNotaDos());
						
			BigDecimal valor = new BigDecimal(27.5);
			BigDecimal sumaredond = new BigDecimal(0);
			
			if(notaSuma.compareTo(valor)>=0){
				sumaredond=	notaSuma.setScale(0, RoundingMode.HALF_UP);	
			}else{
				sumaredond=notaSuma;
			}
						
			materiaDto.setNotaSuma(sumaredond);
			
			int result = sumaredond.compareTo(new BigDecimal(ehfSistemaCalificacion.getSsclNotaMinimaAprobacion().toString()));
			
			if (result >= 0) {
				// aprobado por notas
				//materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula  MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
				materiaDto.setMtrCmbEstado(Boolean.FALSE); //habilita vista de combo NumMatricula
				materiaDto.setAprobado(true); // seteo el aprobado de la materia
				//materiaDto.setNumMatricula(0);
				//materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);// Constante para num matricula MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
				//estado de homologacion varia si es suficiencia de cultura fisica.
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE);
					
			} else {
				// reprobado por notas
				materiaDto.setMtrCmbEstado(Boolean.FALSE); //habilita vista de combo NumMatricula
				materiaDto.setAprobado(false); // seteo el reprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				//materiaDto.setNumMatricula(0);
			//	materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); MQ: Cambio por pedido de producción, 20 jun 2019, se comenta
				
			}
			
		}
		
	}
		
		/**
		 * Método regresar a buscar estudiante homologado luego de ver la información de un estudiante
		 * @return navegación a la pagina de buscar estudiante 
		 */
		public String regresarVer() {
			ehfRegistroPersonaVer= null;
			iniciarParametros();
			return "irABuscarEstudiante";
		}
		
		
		/**
		 * Método cancelar el agregar homologaciones
		 * @return navegación a la pagina de ver estudiantes a editar homologación.
		 */
		public String cancelarAgregar() {
			String retorno = null;	
			//iniciamos la lista de materias de la malla.
			ehfListRecordTotal = null;
			//VOLVEMOS A BUSCAR MATERIAS HOMOLOGADAS
			retorno=irVerHomologacion(ehfRegistroPersonaVer);
			return  retorno;
		}

		/**
		 * Método para desactivar modal agregar
		 * 
		 */ 
		public void desactivaModalAgregar(){
			ehfValidadorClic = 0;
		}
		
		
		/**
		 * Método para desactivar modal editar
		 * 
		 */ 
		public String desactivaModalEditar(){
			String retorno= null;
			ehfValidadorClicEditar = 0;
			retorno = irVerHomologacion(ehfRegistroPersonaVer); //Actualizo la ver Homologacion
			return retorno;
		}
	
		
		/**
		 * Método para desactivar modal eliminar
		 * 
		 */ 
		public void desactivaModalEliminar(){
			ehfValidadorClicEliminar = 0;
		}
		
		
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getEhfUsuario() {
		return ehfUsuario;
	}

	public void setEhfUsuario(Usuario ehfUsuario) {
		this.ehfUsuario = ehfUsuario;
	}


	public PersonaDto getEhfPersonaDtoBuscar() {
		return ehfPersonaDtoBuscar;
	}

	public void setEhfPersonaDtoBuscar(PersonaDto ehfPersonaDtoBuscar) {
		this.ehfPersonaDtoBuscar = ehfPersonaDtoBuscar;
	}

	public List<CarreraDto> getEhfListCarreraDto() {
		ehfListCarreraDto = ehfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : ehfListCarreraDto;
		return ehfListCarreraDto;
	}

	public void setEhfListCarreraDto(List<CarreraDto> ehfListCarreraDto) {
		this.ehfListCarreraDto = ehfListCarreraDto;
	}

	public CarreraDto getEhfCarreraDtoBuscar() {
		return ehfCarreraDtoBuscar;
	}

	public void setEhfCarreraDtoBuscar(CarreraDto ehfCarreraDtoBuscar) {
		this.ehfCarreraDtoBuscar = ehfCarreraDtoBuscar;
	}

	public PeriodoAcademico getEhfPeriodoAcademico() {
		return ehfPeriodoAcademico;
	}

	public void setEhfPeriodoAcademico(PeriodoAcademico ehfPeriodoAcademico) {
		this.ehfPeriodoAcademico = ehfPeriodoAcademico;
	}

	public MallaCurricular getEhfMallaCurricular() {
		return ehfMallaCurricular;
	}

	public void setEhfMallaCurricular(MallaCurricular ehfMallaCurricular) {
		this.ehfMallaCurricular = ehfMallaCurricular;
	}

	public List<MateriaDto> getEhfListMateriaDto() {
		ehfListMateriaDto = ehfListMateriaDto == null ? (new ArrayList<MateriaDto>()) : ehfListMateriaDto;
		return ehfListMateriaDto;
	}

	public void setEhfListMateriaDto(List<MateriaDto> ehfListMateriaDto) {
		this.ehfListMateriaDto = ehfListMateriaDto;
	}

	public List<ParaleloDto> getEhfListParaleloDto() {
		ehfListParaleloDto = ehfListParaleloDto == null ? (new ArrayList<ParaleloDto>()) : ehfListParaleloDto;
		return ehfListParaleloDto;
	}

	public void setEhfListParaleloDto(List<ParaleloDto> ehfListParaleloDto) {
		this.ehfListParaleloDto = ehfListParaleloDto;
	}

	public TipoSistemaCalificacion getEhfTipoSistemaCalificacion() {
		return ehfTipoSistemaCalificacion;
	}

	public void setEhfTipoSistemaCalificacion(TipoSistemaCalificacion ehfTipoSistemaCalificacion) {
		this.ehfTipoSistemaCalificacion = ehfTipoSistemaCalificacion;
	}

	public SistemaCalificacion getEhfSistemaCalificacion() {
		return ehfSistemaCalificacion;
	}

	public void setEhfSistemaCalificacion(SistemaCalificacion ehfSistemaCalificacion) {
		this.ehfSistemaCalificacion = ehfSistemaCalificacion;
	}


	public PeriodoAcademico getEhfPeriodoAcademicoActivo() {
		return ehfPeriodoAcademicoActivo;
	}

	public void setEhfPeriodoAcademicoActivo(PeriodoAcademico ehfPeriodoAcademicoActivo) {
		this.ehfPeriodoAcademicoActivo = ehfPeriodoAcademicoActivo;
	}

	public Integer getEhfValidadorClic() {
		return ehfValidadorClic;
	}

	public void setEhfValidadorClic(Integer ehfValidadorClic) {
		this.ehfValidadorClic = ehfValidadorClic;
	}


	public ParaleloDto getEhfParaleloHomologacion() {
		return ehfParaleloHomologacion;
	}

	public void setEhfParaleloHomologacion(ParaleloDto ehfParaleloHomologacion) {
		this.ehfParaleloHomologacion = ehfParaleloHomologacion;
	}

	public List<MateriaDto> getEhfListHomologadosDto() {
		ehfListHomologadosDto = ehfListHomologadosDto == null ? (new ArrayList<MateriaDto>()) : ehfListHomologadosDto;

		return ehfListHomologadosDto;
	}

	public void setEhfListHomologadosDto(List<MateriaDto> ehfListHomologadosDto) {
		this.ehfListHomologadosDto = ehfListHomologadosDto;
	}


	public List<MateriaDto> getEhfListRecordTotal() {
		ehfListRecordTotal = ehfListRecordTotal == null ? (new ArrayList<MateriaDto>()) : ehfListRecordTotal;
		return ehfListRecordTotal;
	}

	public void setEhfListRecordTotal(List<MateriaDto> ehfListRecordTotal) {
		this.ehfListRecordTotal = ehfListRecordTotal;
	}

	public PeriodoAcademico getEhfPeriodoAcademicoInscripcion() {
		return ehfPeriodoAcademicoInscripcion;
	}

	public void setEhfPeriodoAcademicoInscripcion(PeriodoAcademico ehfPeriodoAcademicoInscripcion) {
		this.ehfPeriodoAcademicoInscripcion = ehfPeriodoAcademicoInscripcion;
	}

	public UsuarioRol getEhfUsuarioRol() {
		return ehfUsuarioRol;
	}

	public void setEhfUsuarioRol(UsuarioRol ehfUsuarioRol) {
		this.ehfUsuarioRol = ehfUsuarioRol;
	}

	public List<Rol> getEhfListaRolesUsuario() {
		ehfListaRolesUsuario = ehfListaRolesUsuario == null ? (new ArrayList<Rol>()) : ehfListaRolesUsuario;
		return ehfListaRolesUsuario;
	}

	public void setEhfListaRolesUsuario(List<Rol> ehfListaRolesUsuario) {
		this.ehfListaRolesUsuario = ehfListaRolesUsuario;
	}

	public Rol getEhfRol() {
		return ehfRol;
	}

	public void setEhfRol(Rol ehfRol) {
		this.ehfRol = ehfRol;
	}



	public PeriodoAcademico getEhfPeriodoAcademicoIdioma() {
		return ehfPeriodoAcademicoIdioma;
	}

	public void setEhfPeriodoAcademicoIdioma(PeriodoAcademico ehfPeriodoAcademicoIdioma) {
		this.ehfPeriodoAcademicoIdioma = ehfPeriodoAcademicoIdioma;
	}


	public PeriodoAcademico getEhfPeriodoAcademicoCulturaFisica() {
		return ehfPeriodoAcademicoCulturaFisica;
	}

	public void setEhfPeriodoAcademicoCulturaFisica(PeriodoAcademico ehfPeriodoAcademicoCulturaFisica) {
		this.ehfPeriodoAcademicoCulturaFisica = ehfPeriodoAcademicoCulturaFisica;
	}

	public List<RegistroHomologacionDto> getEhfListaRegistroEstudiante() {
		ehfListaRegistroEstudiante = ehfListaRegistroEstudiante == null ? (new ArrayList<RegistroHomologacionDto>()) : ehfListaRegistroEstudiante;
		return ehfListaRegistroEstudiante;
	}

	public void setEhfListaRegistroEstudiante(List<RegistroHomologacionDto> ehfListaRegistroEstudiante) {
		this.ehfListaRegistroEstudiante = ehfListaRegistroEstudiante;
	}

	public Boolean getEhfRenderListCarreras() {
		return ehfRenderListCarreras;
	}

	public void setEhfRenderListCarreras(Boolean ehfRenderListCarreras) {
		this.ehfRenderListCarreras = ehfRenderListCarreras;
	}

	public RegistroHomologacionDto getEhfRegistroPersonaVer() {
		return ehfRegistroPersonaVer;
	}

	public void setEhfRegistroPersonaVer(RegistroHomologacionDto ehfRegistroPersonaVer) {
		this.ehfRegistroPersonaVer = ehfRegistroPersonaVer;
	}

	public List<MateriaDto> getEhfListaMateriasHomologadasVer() {
		ehfListaMateriasHomologadasVer = ehfListaMateriasHomologadasVer == null ? (new ArrayList<MateriaDto>()) : ehfListaMateriasHomologadasVer;
		return ehfListaMateriasHomologadasVer;
	}

	public void setEhfListaMateriasHomologadasVer(List<MateriaDto> ehfListaMateriasHomologadasVer) {
		this.ehfListaMateriasHomologadasVer = ehfListaMateriasHomologadasVer;
	}

	public RegistroHomologacionDto getEhfPersonaDtoAgregar() {
		return ehfPersonaDtoAgregar;
	}

	public void setEhfPersonaDtoAgregar(RegistroHomologacionDto ehfPersonaDtoAgregar) {
		this.ehfPersonaDtoAgregar = ehfPersonaDtoAgregar;
	}

	public boolean isEhfDeshabilitaOpcionesEditar() {
		return ehfDeshabilitaOpcionesEditar;
	}

	public void setEhfDeshabilitaOpcionesEditar(boolean ehfDeshabilitaOpcionesEditar) {
		this.ehfDeshabilitaOpcionesEditar = ehfDeshabilitaOpcionesEditar;
	}

	public Integer getEhfValidadorClicEditar() {
		return ehfValidadorClicEditar;
	}

	public void setEhfValidadorClicEditar(Integer ehfValidadorClicEditar) {
		this.ehfValidadorClicEditar = ehfValidadorClicEditar;
	}

	public MateriaDto getEhfMateriaDtoEditar() {
		return ehfMateriaDtoEditar;
	}

	public void setEhfMateriaDtoEditar(MateriaDto ehfMateriaDtoEditar) {
		this.ehfMateriaDtoEditar = ehfMateriaDtoEditar;
	}

	public boolean isEhfDeshabilitaGuardarEdicion() {
		return ehfDeshabilitaGuardarEdicion;
	}

	public void setEhfDeshabilitaGuardarEdicion(boolean ehfDeshabilitaGuardarEdicion) {
		this.ehfDeshabilitaGuardarEdicion = ehfDeshabilitaGuardarEdicion;
	}

	public MateriaDto getEhfMateriaDtoEliminar() {
		return ehfMateriaDtoEliminar;
	}

	public void setEhfMateriaDtoEliminar(MateriaDto ehfMateriaDtoEliminar) {
		this.ehfMateriaDtoEliminar = ehfMateriaDtoEliminar;
	}

	public Integer getEhfValidadorClicEliminar() {
		return ehfValidadorClicEliminar;
	}

	public void setEhfValidadorClicEliminar(Integer ehfValidadorClicEliminar) {
		this.ehfValidadorClicEliminar = ehfValidadorClicEliminar;
	}

	public boolean isEhfDeshabilitaOpcionEliminar() {
		return ehfDeshabilitaOpcionEliminar;
	}

	public void setEhfDeshabilitaOpcionEliminar(boolean ehfDeshabilitaOpcionEliminar) {
		this.ehfDeshabilitaOpcionEliminar = ehfDeshabilitaOpcionEliminar;
	}

	public List<PeriodoAcademico> getEhfListaPracActivoPosgrado() {
		ehfListaPracActivoPosgrado = ehfListaPracActivoPosgrado == null ? (new ArrayList<PeriodoAcademico>()) : ehfListaPracActivoPosgrado;
		return ehfListaPracActivoPosgrado;
	}

	public void setEhfListaPracActivoPosgrado(List<PeriodoAcademico> ehfListaPracActivoPosgrado) {
		this.ehfListaPracActivoPosgrado = ehfListaPracActivoPosgrado;
	}

	public boolean isEhfDeshabilitaOpcionAgregar() {
		return ehfDeshabilitaOpcionAgregar;
	}

	public void setEhfDeshabilitaOpcionAgregar(boolean ehfDeshabilitaOpcionAgregar) {
		this.ehfDeshabilitaOpcionAgregar = ehfDeshabilitaOpcionAgregar;
	}


	
	
	
}
