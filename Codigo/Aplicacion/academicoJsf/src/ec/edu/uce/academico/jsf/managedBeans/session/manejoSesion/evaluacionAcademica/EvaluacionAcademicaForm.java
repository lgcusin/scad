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
   
 ARCHIVO:     NotasRectificacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja el ingreso de las notas  
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-11-2017 			Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPregradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteNotasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.IdentifidorCliente;

/**
 * Clase (managed bean) NotaAtrasadaForm.
 * Managed Bean que administra el ingreso de las notas 
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="evaluacionAcademicaForm")
@SessionScoped
public class EvaluacionAcademicaForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario eafUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto nrctfDocente;
	private PersonaDto nrctfDirCarrera;

	//PARA BUSQUEDA
	private EstudianteJdbcDto eafEstudianteBuscar;
	private List<CarreraDto> nrctfListCarreraBusq;
	private List<NivelDto> nrctfListNivelBusq;
	private List<MateriaDto> nrctfListMateriaBusq;
	private List<EstudianteJdbcDto> eafListEstudianteBusq;
	private List<EstudianteJdbcDto> nrctfListEstudianteEditar;
	private List<ParaleloDto> eafListParaleloBusq;
	private ParaleloDto eafParaleloDtoEditar;
	private List<RolFlujoCarrera> nrctfListRolFlujoCarreraBusq;
	private CronogramaActividadJdbcDto eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico;
	private CronogramaActividadJdbcDto eafCronogramaActividadJdbcDtoBuscar;
	private CronogramaActividadJdbcDto eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	private CronogramaActividadJdbcDto nrctfFechaPracCierreFinPresentePeriodoAcademico;
	private CronogramaActividadJdbcDto nrctfFechaPracCierreInicioSigPeriodoAcademico;
	private Dependencia eafDependenciaBuscar;
	private List<DocenteJdbcDto> nrctfListCarreraDocenteBusq;
	private List<DocenteJdbcDto> nrctfListNivelxCarreraDocenteBusq;
	private List<DocenteJdbcDto> nrctfListMateriasxCarreraDocenteBusq;
	private boolean activador;
	private List<PeriodoAcademico> nrctfListPeriodoAcademico;
	private PeriodoAcademicoDto nrctfPeriodoAcademicoBusq;
	
	//PARA GUARDAR LA ASISTENCIA DEL DOCENTE DEL FORM
	private Integer eafAsistenciaDocente;
	//PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private Integer nrctfValidadorClic;
	private String nrctfEstado;
	private Integer nrctfTipoRectificacion;
	private Integer eafEtapaProceso;
	
	//PARA GUARDAR LOS REGISTROS DE LA SESION DEL CLIENTE HOSTNAME, IPPIBLICA, IPPRIVADA
	private String eafRegCliente;
	//private String thisIpAddress;
	//campos para el envio de la notificacion del ingreso final de notas al mail del docente	
	private String nrctfNomCarrera;
	private String nrctfNomMateria;
	private String nrctfNomParalelo;
	private String nrctfNomNivel;
	private String nrctfNomNotaRectificacion;
	
	
	private Integer nrctfTipoNota;
	//PARA CARGA DE ARCHIVO
	private String nrctfNombreArchivoAuxiliar;
	private String nrctfNombreArchivoSubido;
	private Integer eafValidarArchivo;
	private InputStream eafArchivo;
	@SuppressWarnings("unused")
	private Materia mtrModulo;
	
	private String eafNombre;
	
	private int eafActivadorReporte;
	
	private Boolean eafActivar;

	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	private CarreraDtoServicioJdbc servNrctfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servNrctfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servNrctfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servNrctfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servNrctfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servNrctfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servNrctfParaleloDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servNrctfNotasPregradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servNrctfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNrctfMateriaDto;
	@EJB
	private CalificacionServicio servCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servNpfRolFlujoCarreraServicio;
	@EJB
	private UsuarioRolServicio servNpfUsuarioRolServicio;
	@EJB
	private DependenciaServicio servDependenciaServicio;
	@EJB
	private CronogramaActividadDtoServicioJdbc servCronogramaActividadDtoServicioJdbcServicio;
	@EJB
	private PersonaDtoServicioJdbc servNrctfPersonaDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoServicio servNrctfPeriodoAcademicoServicio;
	@EJB
	private MateriaServicio servNpfMateriaServicio;
	@EJB
	private MallaCurricularParaleloDtoServicioJdbc servNpfMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	private MallaCurricularParaleloServicio servNpfMallaCurricularParaleloServicio;
	@EJB
	private MallaCurricularMateriaServicio servNpfMallaCurricularMateriaServicio;
	@EJB
	private CarreraServicio servNpfCarreraServicio;
	@EJB
	private NivelServicio servNivelServicio;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irListarParalelosXMateriaXCarreraArchivo(Usuario usuario){
		eafUsuario = usuario;
		String retorno = null;
		eafActivadorReporte = 0;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			
			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(eafUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			
//			nrctfListRolFlujoCarreraBusq = servNpfRolFlujoCarreraServicio.buscarPorIdUsuario(nrctfUsuario);
			
			iniciarParametros();
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
//			nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc.buscarCarrerasDocente(eafUsuario.getUsrIdentificacion());
			eafListParaleloBusq = null;
			
			//busco el periodo academico
			nrctfPeriodoAcademicoBusq =  servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			
			/*********************************************************************/
			/*********** PARA ARREGLAR *******************************************/
			/*********************************************************************/
			//BUSCO EL DOCENTE PARA LAS MATERIAS
//			nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());
//			//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//			//busco compartidas
//			List<ParaleloDto> listCompartida = new ArrayList<>();
//			listCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
//
//			//busco las no compartidas
//			List<ParaleloDto> listNoCompartida = new ArrayList<>();
//			listNoCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
//			
//			//asignación a una sola lista
//			eafListParaleloBusq = new ArrayList<>();
//			for (ParaleloDto item : listCompartida) {
//				eafListParaleloBusq.add(item);
//			}
//			for (ParaleloDto item : listNoCompartida) {
//				eafListParaleloBusq.add(item);
//			}
			/*********************************************************************/
			/*********************************************************************/
			
			if(nrctfPeriodoAcademicoBusq.getPracId() != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				List<DocenteJdbcDto> listCarreras = new ArrayList<>();
				listCarreras =servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo carreras compartidas 
				List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
				listCarrCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);

				nrctfListCarreraDocenteBusq = new ArrayList<>();
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto itemCarr : listCarreras) {
					nrctfListCarreraDocenteBusq.add(itemCarr);
				}
				if(listCarrCompartida.size() > 0){
					//asigno carreras compartidas
					boolean igual = false;
					for (DocenteJdbcDto item : listCarrCompartida) {
						igual = false;
						for (DocenteJdbcDto itemAux : listCarreras) {
							if(item.getCrrId() == itemAux.getCrrId()){
								igual = true;
							}
						}
						if(igual){
							continue;
						}else{
							nrctfListCarreraDocenteBusq.add(item);
						}
					}	
				}
				Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
						return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
					}
				});
				retorno = "irListarParalelosXMateriaXCarreraArchivo";
				
			}
//			try {
//				nrctfPeriodoAcademicoBusq =  servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//				if(nrctfPeriodoAcademicoBusq.getPracId() != GeneralesConstantes.APP_ID_BASE){
//					//LISTO LAS CARRERAS
//					List<DocenteJdbcDto> listCarreras = new ArrayList<>();
//					listCarreras =servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());
//					//listo mallas curriculares 
//					List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
//					listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());
//
//					//listo carreras compartidas 
//					List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
//					listCarrCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);
//					//asigno carreras con detalle puesto
//					for (DocenteJdbcDto itemCarr : listCarreras) {
//						nrctfListCarreraDocenteBusq.add(itemCarr);
//					}
//					if(listCarrCompartida.size() > 0){
//						//asigno carreras compartidas
//						boolean igual = false;
//						for (DocenteJdbcDto item : listCarrCompartida) {
//							igual = false;
//							for (DocenteJdbcDto itemAux : listCarreras) {
//								if(item.getCrrId() == itemAux.getCrrId()){
//									igual = true;
//								}
//							}
//							if(igual){
//								continue;
//							}else{
//								nrctfListCarreraDocenteBusq.add(item);
//							}
//						}	
//					}
//					Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
//						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
//							return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
//						}
//					});
//				}
//				return "irListarParalelosXMateriaXCarreraArchivoIdiomas";
//				
//			} catch (Exception e1) {
//			}
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
//		} catch (RolFlujoCarreraNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (RolFlujoCarreraException e) {
//			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irListarParalelosXMateriaXCarrera.DetallePuestoDtoJdbcException")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			
			try {
				iniciarParametros();
				//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
//				nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc.buscarCarrerasDocente(eafUsuario.getUsrIdentificacion());
				eafListParaleloBusq = null;
				
				//busco el periodo academico
				nrctfPeriodoAcademicoBusq =  servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				
				
				/*********************************************************************/
				/*********** PARA ARREGLAR *******************************************/
				/*********************************************************************/
				//BUSCO EL DOCENTE PARA LAS MATERIAS
//				nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());
//				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//				//busco compartidas
//				List<ParaleloDto> listCompartida = new ArrayList<>();
//				listCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
	//
//				//busco las no compartidas
//				List<ParaleloDto> listNoCompartida = new ArrayList<>();
//				listNoCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
//				
//				//asignación a una sola lista
//				eafListParaleloBusq = new ArrayList<>();
//				for (ParaleloDto item : listCompartida) {
//					eafListParaleloBusq.add(item);
//				}
//				for (ParaleloDto item : listNoCompartida) {
//					eafListParaleloBusq.add(item);
//				}
				/*********************************************************************/
				/*********************************************************************/
				
				if(nrctfPeriodoAcademicoBusq.getPracId() != GeneralesConstantes.APP_ID_BASE){
					//LISTO LAS CARRERAS
					List<DocenteJdbcDto> listCarreras = new ArrayList<>();
					listCarreras =servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

					//listo mallas curriculares 
					List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
					listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

					//listo carreras compartidas 
					List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
					listCarrCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);

					nrctfListCarreraDocenteBusq = new ArrayList<>();
					//asigno carreras con detalle puesto
					for (DocenteJdbcDto itemCarr : listCarreras) {
						nrctfListCarreraDocenteBusq.add(itemCarr);
					}
					if(listCarrCompartida.size() > 0){
						//asigno carreras compartidas
						boolean igual = false;
						for (DocenteJdbcDto item : listCarrCompartida) {
							igual = false;
							for (DocenteJdbcDto itemAux : listCarreras) {
								if(item.getCrrId() == itemAux.getCrrId()){
									igual = true;
								}
							}
							if(igual){
								continue;
							}else{
								nrctfListCarreraDocenteBusq.add(item);
							}
						}	
					}
					Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
						}
					});
					retorno = "irListarParalelosXMateriaXCarreraArchivo";
					
				}
			} catch (Exception e2) {
				//busco el periodo academico
				try {
					nrctfPeriodoAcademicoBusq =  servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					if(nrctfPeriodoAcademicoBusq.getPracId() != GeneralesConstantes.APP_ID_BASE){
						//LISTO LAS CARRERAS
						List<DocenteJdbcDto> listCarreras = new ArrayList<>();
						listCarreras =servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());
						//listo mallas curriculares 
						List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
						listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

						//listo carreras compartidas 
						List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
						listCarrCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);

						nrctfListCarreraDocenteBusq = new ArrayList<>();
						//asigno carreras con detalle puesto
						for (DocenteJdbcDto itemCarr : listCarreras) {
							nrctfListCarreraDocenteBusq.add(itemCarr);
						}
						if(listCarrCompartida.size() > 0){
							//asigno carreras compartidas
							boolean igual = false;
							for (DocenteJdbcDto item : listCarrCompartida) {
								igual = false;
								for (DocenteJdbcDto itemAux : listCarreras) {
									if(item.getCrrId() == itemAux.getCrrId()){
										igual = true;
									}
								}
								if(igual){
									continue;
								}else{
									nrctfListCarreraDocenteBusq.add(item);
								}
							}	
						}
						Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
							public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
								return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
							}
						});
					}
					return "irListarParalelosXMateriaXCarreraArchivoIdiomas";
					
				} catch (Exception e1) {
				}
				
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irListarParalelosXMateriaXCarrera.DetallePuestoDtoJdbcNoEncontradoException")));
			}
			
			
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	public String irListarParalelosXMateriaXCarreraArchivoRectificacion(Usuario usuario){
		eafUsuario = usuario;
		String retorno = null;
		eafActivadorReporte = 0;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			
			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(eafUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			iniciarParametros();
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE

			eafListParaleloBusq = null;
			
			//busco el periodo academico
			nrctfPeriodoAcademicoBusq =  servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
		
			/*********************************************************************/
			/*********************************************************************/
			
			if(nrctfPeriodoAcademicoBusq.getPracId() != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				List<DocenteJdbcDto> listCarreras = new ArrayList<>();
				listCarreras =servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo carreras compartidas 
				List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
				listCarrCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);

				nrctfListCarreraDocenteBusq = new ArrayList<>();
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto itemCarr : listCarreras) {
					nrctfListCarreraDocenteBusq.add(itemCarr);
				}
				if(listCarrCompartida.size() > 0){
					//asigno carreras compartidas
					boolean igual = false;
					for (DocenteJdbcDto item : listCarrCompartida) {
						igual = false;
						for (DocenteJdbcDto itemAux : listCarreras) {
							if(item.getCrrId() == itemAux.getCrrId()){
								igual = true;
							}
						}
						if(igual){
							continue;
						}else{
							nrctfListCarreraDocenteBusq.add(item);
						}
					}	
				}
				Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
						return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
					}
				});
				retorno = "irListarParalelosXMateriaXCarreraArchivoRectificacion";
				
			}

		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irListarParalelosXMateriaXCarrera.DetallePuestoDtoJdbcException")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			
			try {
				iniciarParametros();
				//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
//				nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc.buscarCarrerasDocente(eafUsuario.getUsrIdentificacion());
				eafListParaleloBusq = null;
				
				//busco el periodo academico
				nrctfPeriodoAcademicoBusq =  servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				
				
				
				/*********************************************************************/
				/*********************************************************************/
				
				if(nrctfPeriodoAcademicoBusq.getPracId() != GeneralesConstantes.APP_ID_BASE){
					//LISTO LAS CARRERAS
					List<DocenteJdbcDto> listCarreras = new ArrayList<>();
					listCarreras =servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

					//listo mallas curriculares 
					List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
					listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

					//listo carreras compartidas 
					List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
					listCarrCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);

					nrctfListCarreraDocenteBusq = new ArrayList<>();
					//asigno carreras con detalle puesto
					for (DocenteJdbcDto itemCarr : listCarreras) {
						nrctfListCarreraDocenteBusq.add(itemCarr);
					}
					if(listCarrCompartida.size() > 0){
						//asigno carreras compartidas
						boolean igual = false;
						for (DocenteJdbcDto item : listCarrCompartida) {
							igual = false;
							for (DocenteJdbcDto itemAux : listCarreras) {
								if(item.getCrrId() == itemAux.getCrrId()){
									igual = true;
								}
							}
							if(igual){
								continue;
							}else{
								nrctfListCarreraDocenteBusq.add(item);
							}
						}	
					}
					Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
						}
					});
					retorno = "irListarParalelosXMateriaXCarreraArchivoRectificacion";
					
				}
			} catch (Exception e2) {
			}
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		iniciarParametros();
		return "irInicio";
	}
	
	
	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * @param idCarrera - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera){
//		idCarrera = nrctfEstudianteBuscar.getCrrId();
		eafEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		eafEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		eafListParaleloBusq = null;
		
		Carrera carreraConsulta = new Carrera();
		try {
			carreraConsulta = servNpfCarreraServicio.buscarPorId(idCarrera);
		} catch (CarreraNoEncontradoException e1) {
		} catch (CarreraException e1) {
		}
		
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
//				//BUSCO EL DOCENTE PARA LAS MATERIAS
//				nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodoActivo(eafUsuario.getUsrId(), idCarrera, TipoPuestoConstantes.TIPO_DOCENTE_VALUE);
//				//LISTO LOS NIVELES
//				nrctfListNivelxCarreraDocenteBusq = servDocenteDtoServicioJdbc.buscarNivelesDocente(idCarrera, eafUsuario.getUsrIdentificacion());
				
				//BUSCO EL DOCENTE PARA LAS MATERIAS
//				nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());
//				nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPracXTipoCarrera(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId(), carreraConsulta.getCrrTipo());
				
				nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPracXTipoCarrera(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId(), CarreraConstantes.TIPO_PREGRADO_VALUE);
				if(nrctfDocente == null){
					nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPracXTipoCarrera(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId(), CarreraConstantes.TIPO_NIVELEACION_VALUE);
					if(nrctfDocente == null){
						nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPracXTipoCarrera(eafUsuario.getUsrId(), eafEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId(), CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
					}
				}
				
				//LISTO LOS NIVELES
				List<DocenteJdbcDto> listNiveles = new ArrayList<>();
				listNiveles = servNrctfDocenteDtoServicioJdbc.buscarNivelesDocenteXPrac(eafEstudianteBuscar.getCrrId(),eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo niveles compartidas 
				List<DocenteJdbcDto> listNivelCompartida = new ArrayList<>();
				listNivelCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprIdPrinciNotas(listMlcrpr);

				nrctfListNivelxCarreraDocenteBusq = new ArrayList<>();
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto item : listNiveles) {
					nrctfListNivelxCarreraDocenteBusq.add(item);
				}
				if(listNivelCompartida.size() > 0){
					//asigno carreras compartidas
					boolean igual = false;
					for (DocenteJdbcDto item : listNivelCompartida) {
						igual = false;
						for (DocenteJdbcDto itemAux : listNiveles) {
							if(item.getNvlId() == itemAux.getNvlId()){
								igual = true;
							}
						}
						if(igual){
							continue;
						}else{
							if(eafEstudianteBuscar.getCrrId() == item.getCrrId()){
								nrctfListNivelxCarreraDocenteBusq.add(item);
							}
						}
					}	
				}
				Collections.sort(nrctfListNivelxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
						return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
					}
				});
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.llenarNivel.exception")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.llenarNivel.no.encontrado.exception")));
		}
	}
	
	
	/**
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idNivel, int carreraId){
		carreraId = eafEstudianteBuscar.getCrrId();
		idNivel = eafEstudianteBuscar.getNvlId();
		eafEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		eafListParaleloBusq = null;
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
//				nrctfListMateriaBusq = servNrctfMateriaDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXparaleloXdocente( nrctfEstudianteBuscar.getCrrId(), idNivel , nrctfDocente.getDtpsId());
//				nrctfListMateriasxCarreraDocenteBusq =servDocenteDtoServicioJdbc.buscarMateriasDocente(carreraId, idNivel, eafUsuario.getUsrIdentificacion());
				
				//LISTO LAS MATERIAS
				List<DocenteJdbcDto> listMaterias = new ArrayList<>();
				listMaterias = servNrctfDocenteDtoServicioJdbc.listarXPeriodoXcarreraXnivelXdocente(nrctfPeriodoAcademicoBusq.getPracId(), eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId());

				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(eafUsuario.getUsrIdentificacion(),nrctfPeriodoAcademicoBusq.getPracId());

				//listo materias compartidas 
				List<DocenteJdbcDto> listMateriasCompartida = new ArrayList<>();
				listMateriasCompartida = servNrctfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprIdPrinciNotas(listMlcrpr);

				nrctfListMateriasxCarreraDocenteBusq = new ArrayList<DocenteJdbcDto>();
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto item : listMaterias) {
					nrctfListMateriasxCarreraDocenteBusq.add(item);
				}
				if(listMateriasCompartida.size() > 0){
					//asigno carreras compartidas
					boolean igual = false;
					for (DocenteJdbcDto item : listMateriasCompartida) {
						igual = false;
						for (DocenteJdbcDto itemAux : listMaterias) {
							if(item.getMtrId() == itemAux.getMtrId()){
								igual = true;
							}
						}
						if(igual){
							continue;
						}else{
							if(eafEstudianteBuscar.getCrrId() == item.getCrrId() && eafEstudianteBuscar.getNvlId() == item.getNvlId()){
								nrctfListMateriasxCarreraDocenteBusq.add(item);
							}
						}
					}	
				}
				Collections.sort(nrctfListMateriasxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
						return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
					}
				});
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.llenarMateria.exception")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.llenarMateria.no.encontrado.exception")));
		}
	}
	
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas1(ParaleloDto prl){
		eafActivadorReporte=0;
		eafNombre = "1er HEMISEMESTRE";
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			eafRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS 1");
			StringBuilder sb = new StringBuilder();
			sb.append(eafRegCliente);
			sb.append("|");
			
			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");	
				} catch (Exception e) {
				}
			}
			eafRegCliente = sb.toString();

			eafDependenciaBuscar =  servDependenciaServicio.buscarFacultadXcrrId(eafEstudianteBuscar.getCrrId());
			PeriodoAcademico pracCierre = null;
//			try {
//				pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			
//			if(pracCierre!=null){
//				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
//				}
//				else{
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
//				}
//			}else{
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
				}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ENFERMERIA_REDISENO_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION1_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION2_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION3_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION4_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION5_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION6_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION7_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION8_VALUE
//								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION9_VALUE
				){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
				}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE, ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasPeriodoActivoPorProceso(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
				}
				
//			}
			
			
//			nrctfDependenciaBuscar =  servNrctfDependenciaServicio.buscarPorId(nrctfEstudianteBuscar.getCrrId());
//			if(nrctfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}else{
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}
			
			if(eafUsuario.getUsrNick().equals("fesempertegui")){
				
			}else{
				Carrera npfCarrera = new Carrera();
				try {
					npfCarrera = servNpfCarreraServicio.buscarPorId(eafEstudianteBuscar.getCrrId());
				} catch (Exception e) {
				}
				
				if((npfCarrera.getCrrId()==CarreraConstantes.CARRERA_CONTABILIDAD_DISTANCIA_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_EMPRESAS_DISTANCIA_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_PUBLICA_DISTANCIA_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_LIC_CONTABILIDAD_DISTANCIA_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_LIC_EMPRESAS_DISTANCIA_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_LIC_PUBLICA_DISTANCIA_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_CONTABILIDAD_DISTANCIA_R__VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_EMPRESAS_DISTANCIA_R_VALUE
						||npfCarrera.getCrrId()==CarreraConstantes.CARRERA_PUBLICA_DISTANCIA_R_VALUE
						)
						&& npfCarrera.getCrrTipo()==CarreraConstantes.TIPO_PREGRADO_VALUE){
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					try {
						Date date = dateFormat.parse("10/06/2019");
						long time = date.getTime();
						Timestamp myDate = new Timestamp(new Date().getTime());
						if (myDate.after(new Timestamp(time))) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas");
							return null;
						}
					} catch (Exception e) {
					}
				}else{
					Date myDate = new Date(new Date().getTime());
					try {
						Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio().getTime());
						Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
						if(myDate.after(maxDate)){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
							return null;
						}
					} catch (Exception e) {
					}
					
					Date inicioIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaInicio().getTime());
					if(myDate.before(inicioIngresoNota)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("No es posible ingresar notas antes de la fecha establecida en el cronograma.");
						return null;
					}
					
					Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaFin().getTime());
					if(myDate.after(finIngresoNota)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("No es posible ingresar notas después de la fecha establecida en el cronograma.");
						return null;
					}
				}
				
			}
			Materia mtrAux = servNrctfMateriaDto.buscarPorId(eafParaleloDtoEditar.getMtrId());
			if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servNpfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							nrctfDocente.getPrsIdentificacion(),eafParaleloDtoEditar.getMlcrmtNvlId(),eafParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),nrctfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),eafParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
							item.setClfNota1(null);
							item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante1(null);
							item.setClfNota1(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota1(null);
									item.setClfAsistenciaEstudiante1(null);
									item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
									npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
			            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
			            	
			                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
			                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
			                	npfListEstudianteBusqPrueba.remove(j);
			                    j--;
			                }
			            }
			        }
			
			       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
			    	   boolean op = true;
			            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
			            	if(i!=j){
			            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
				                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
					                	op=false;
				                	}
				                }	
			            	}
			            }
			            if(op){
			            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
			            }
			        }
				} catch (Exception e) {
					 try {
							 listaprueba = new ArrayList<EstudianteJdbcDto>();
							try {
//								Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//										nrctfParaleloDtoEditar.getMlcrprId());
//								System.out.println(mlcrprAuxId);
								MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(eafParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),eafParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
										item.setClfNota1(null);
										item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante1(null);
										item.setClfNota1(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota1(null);
												item.setClfAsistenciaEstudiante1(null);
												item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
										}
									}
								}
								for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
						            	
						                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
						                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
						                	npfListEstudianteBusqPrueba.remove(j);
						                    j--;
						                }
						            }
						        }
						
						       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
						    	   boolean op = true;
						            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
						            	if(i!=j){
						            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
							                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
								                	op=false;
							                	}
							                }	
						            	}
						            }
						            if(op){
						            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
					
	      
	}else{
//				eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
				if(eafParaleloDtoEditar.getHracMlcrprIdComp() == null){// No comparte o no tiene compartidas con nadie
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				}else{// Compartida o dependiente de otra
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,eafParaleloDtoEditar.getMlcrprId());
				}
			
			}
			
			
//			nrctfListEstudianteEditar = new ArrayList<EstudianteJdbcDto>();
//			nrctfListEstudianteEditar = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacion(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
//			
//			nrctfDirCarrera = new PersonaDto();
//			nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, eafParaleloDtoEditar.getCrrId());
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				
				if(item.getClfNota1()!=null ){
					String nota = item.getClfNota1().toString();
					int puntoDecimalUbc = nota.indexOf('.');
					int totalDecimales = nota.length() - puntoDecimalUbc -1;
					if(puntoDecimalUbc==-1){
						item.setClfNota1String(nota+",00");
					}else if (totalDecimales==1){
						item.setClfNota1String(nota.replace(".", ",")+"0");
					}else{
						item.setClfNota1String(nota.replace(".", ","));
					}
				}
			}
			
			nrctfTipoRectificacion = new Integer(1);
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente1() != null)){
					eafAsistenciaDocente = null;
				}else{
					eafAsistenciaDocente = item.getClfAsistenciaDocente1();
					break;
				}
			}
			eafListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(eafListEstudianteBusq);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.dependencia.no.encontrado.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));

		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		} 
		cargarReporte();
		if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
			return "irCargarArchivoNotasIdiomas";
		}else{
			return "irCargarArchivoNotas";	
		}
		
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas2(ParaleloDto prl){
		eafNombre = "2do HEMISEMESTRE";
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			eafDependenciaBuscar = new Dependencia();
			eafDependenciaBuscar =  servDependenciaServicio.buscarFacultadXcrrId(eafEstudianteBuscar.getCrrId());
			
			
			
			
			if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
				eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
			}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
//									|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ENFERMERIA_REDISENO_VALUE
					){
				eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
			}
			else{
				eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
			}
			eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = eafCronogramaActividadJdbcDtoBuscar;
			if(eafUsuario.getUsrNick().equals("fesempertegui")){
				
			}else{
				Timestamp myDate = new Timestamp(new Date().getTime());
				
				if(myDate.after(eafCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está fuera de las fechas establecidas");
					return null;
				}
				
				if(myDate.before(eafCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está antes de las fechas establecidas");
					return null;
				}
			}
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			
//			for (int i = 0; i < datosCliente.size(); i++) {
//			}
			
			
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			eafRegCliente  = "ID SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|" + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			eafRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 2");
			Materia mtrAux = servNrctfMateriaDto.buscarPorId(eafParaleloDtoEditar.getMtrId());
			if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servNpfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							nrctfDocente.getPrsIdentificacion(),eafParaleloDtoEditar.getMlcrmtNvlId(),eafParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),nrctfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),eafParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota2(null);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
									npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
			            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
			            	
			                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
			                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
			                	npfListEstudianteBusqPrueba.remove(j);
			                    j--;
			                }
			            }
			        }
			
			       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
			    	   boolean op = true;
			            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
			            	if(i!=j){
			            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
				                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
					                	op=false;
				                	}
				                }	
			            	}
			            }
			            if(op){
			            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
			            }
			        }
				} catch (Exception e) {
					 try {
							 listaprueba = new ArrayList<EstudianteJdbcDto>();
							try {
//								Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//										nrctfParaleloDtoEditar.getMlcrprId());
//								System.out.println(mlcrprAuxId);
								MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(eafParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),eafParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
										item.setClfNota2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfNota2(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
										}
									}
								}
								for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
						            	
						                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
						                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
						                	npfListEstudianteBusqPrueba.remove(j);
						                    j--;
						                }
						            }
						        }
						
						       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
						    	   boolean op = true;
						            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
						            	if(i!=j){
						            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
							                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
								                	op=false;
							                	}
							                }	
						            	}
						            }
						            if(op){
						            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
					
	      
			}else{
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
				if(eafParaleloDtoEditar.getHracMlcrprIdComp() == null){// No comparte o no tiene compartidas con nadie
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				}else{// Compartida o dependiente de otra
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,eafParaleloDtoEditar.getMlcrprId());
				}
			}
			
			eafEstudianteBuscar.setMtrId(eafParaleloDtoEditar.getMtrId());
//			System.out.println(eafParaleloDtoEditar.getMtrId());
		for (EstudianteJdbcDto item : eafListEstudianteBusq) {
			try {
				eafAsistenciaDocente=item.getClfAsistenciaDocente2();
				break;
			} catch (Exception e) {
			}
		}
		
		for (EstudianteJdbcDto item : eafListEstudianteBusq) {
			
			if(item.getClfNota2()!=null ){
				String nota = item.getClfNota2().toString();
				int puntoDecimalUbc = nota.indexOf('.');
				int totalDecimales = nota.length() - puntoDecimalUbc -1;
				if(puntoDecimalUbc==-1){
					item.setClfNota2String(nota+",00");
				}else if (totalDecimales==1){
					item.setClfNota2String(nota.replace(".", ",")+"0");
				}else{
					item.setClfNota2String(nota.replace(".", ","));
				}
			}
		}
		
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if((item.getClfAsistenciaDocente2() != null)){
						eafAsistenciaDocente = item.getClfAsistenciaDocente2();
						break;
				}else{
					eafAsistenciaDocente = null;
				}
				
			}
			eafListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(eafListEstudianteBusq);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.dependencia.no.encontrado.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));
//		} catch (PersonaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaDtoNoEncontradoException e) {
//			FacesUtil.mensajeError("No se encontró al Director de carrera para la notificación del ingreso de notas.");
		} catch (MateriaNoEncontradoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MateriaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		cargarReporte2();
		return "irCargarArchivoNotas2";
	}
	
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas1Rectificacion(ParaleloDto prl){
		eafActivadorReporte=0;
		eafNombre = "1er HEMISEMESTRE";
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			eafRegCliente  = "ID SERVIDOR:".concat(idHostAux).concat("|IP CLIENTE:"+ipLocalClienteAux.concat("|USUARIO:" + datosCliente.get(4).concat("|IP PROXY:"+datosCliente.get(6))));
			Date fechaActual = new Date();
			eafRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 1 RECTIFICACION");
			eafDependenciaBuscar =  servDependenciaServicio.buscarFacultadXcrrId(eafEstudianteBuscar.getCrrId());
//			PeriodoAcademico pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
			
			PeriodoAcademico pracCierre=null;
			try {
				pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
			} catch (PeriodoAcademicoNoEncontradoException e1) {
			
			}
			if(pracCierre!=null){
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else{
					if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
							){
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
					}else{
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);	
					}
				}
			}else{
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else {
					if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
							){
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
					}else{
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);						
					}

				}
				
			}
			if(eafUsuario.getUsrNick().equals("fesempertegui")){
				
			}else{
				Date myDate = new Date(new Date().getTime());
				Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaInicio().getTime());
				Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
				Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin().getTime());
				if(myDate.after(maxDate)){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
					return null;
				}
				
				if(myDate.before(finIngresoNota)){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está antes de las fechas establecidas.");
					return null;
				}
			}				
//			if(pracCierre!=null){
//				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
//				}
//				else{
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
//				}
//			}else{
//				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
//						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_TERAPIA_OCUPACIONAL_REDISENO_VALUE
//						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ATENCION_PREHOSPITALARIA_REDISENO_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ENFERMERIA_REDISENO_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION1_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION2_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION3_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION4_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION5_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION6_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION7_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION8_VALUE
////								|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_ADMINITRACION9_VALUE
//				){
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
//				}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
//						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
//						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
//						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
//						|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE, ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);
//				}else{
//					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				}
//				
//			}
			
			
//			nrctfDependenciaBuscar =  servNrctfDependenciaServicio.buscarPorId(nrctfEstudianteBuscar.getCrrId());
//			if(nrctfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}else{
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}
//			Date myDate = new Date(new Date().getTime());
//			try {
//				Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio().getTime());
//				Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
//				if(myDate.after(maxDate)){
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
//					return null;
//				}
//			} catch (Exception e) {
//			}
//			
//			Date inicioIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaInicio().getTime());
//			if(myDate.before(inicioIngresoNota)){
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No es posible ingresar notas antes de la fecha establecida en el cronograma.");
//				return null;
//			}
//			
//			Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaFin().getTime());
//			if(myDate.after(finIngresoNota)){
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No es posible ingresar notas después de la fecha establecida en el cronograma.");
//				return null;
//			}
			Materia mtrAux = servNrctfMateriaDto.buscarPorId(eafParaleloDtoEditar.getMtrId());
			if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servNpfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							nrctfDocente.getPrsIdentificacion(),eafParaleloDtoEditar.getMlcrmtNvlId(),eafParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),nrctfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),eafParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
							item.setClfNota1(null);
							item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante1(null);
							item.setClfNota1(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota1(null);
									item.setClfAsistenciaEstudiante1(null);
									item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
									npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
			            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
			            	
			                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
			                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
			                	npfListEstudianteBusqPrueba.remove(j);
			                    j--;
			                }
			            }
			        }
			
			       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
			    	   boolean op = true;
			            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
			            	if(i!=j){
			            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
				                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
					                	op=false;
				                	}
				                }	
			            	}
			            }
			            if(op){
			            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
			            }
			        }
				} catch (Exception e) {
					 try {
							 listaprueba = new ArrayList<EstudianteJdbcDto>();
							try {
//								Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//										nrctfParaleloDtoEditar.getMlcrprId());
//								System.out.println(mlcrprAuxId);
								MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(eafParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),eafParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
										item.setClfNota1(null);
										item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante1(null);
										item.setClfNota1(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota1(null);
												item.setClfAsistenciaEstudiante1(null);
												item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
										}
									}
								}
								for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
						            	
						                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
						                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
						                	npfListEstudianteBusqPrueba.remove(j);
						                    j--;
						                }
						            }
						        }
						
						       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
						    	   boolean op = true;
						            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
						            	if(i!=j){
						            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
							                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
								                	op=false;
							                	}
							                }	
						            	}
						            }
						            if(op){
						            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
						
					}
				}
					
	      
	}else{
//				eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
				if(eafParaleloDtoEditar.getHracMlcrprIdComp() == null){// No comparte o no tiene compartidas con nadie
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				}else{// Compartida o dependiente de otra
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,eafParaleloDtoEditar.getMlcrprId());
				}
			
			}
			
			
//			nrctfListEstudianteEditar = new ArrayList<EstudianteJdbcDto>();
//			nrctfListEstudianteEditar = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacion(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
//			
//			nrctfDirCarrera = new PersonaDto();
//			nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, eafParaleloDtoEditar.getCrrId());
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				
				if(item.getClfNota1()!=null ){
					String nota = item.getClfNota1().toString();
					int puntoDecimalUbc = nota.indexOf('.');
					int totalDecimales = nota.length() - puntoDecimalUbc -1;
					if(puntoDecimalUbc==-1){
						item.setClfNota1String(nota+",00");
					}else if (totalDecimales==1){
						item.setClfNota1String(nota.replace(".", ",")+"0");
					}else{
						item.setClfNota1String(nota.replace(".", ","));
					}
				}
			}
			
			nrctfTipoRectificacion = new Integer(1);
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente1() != null)){
					eafAsistenciaDocente = null;
				}else{
					eafAsistenciaDocente = item.getClfAsistenciaDocente1();
					break;
				}
			}
			eafListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(eafListEstudianteBusq);
			cargarReporte();
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.dependencia.no.encontrado.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));
//		} catch (PersonaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaDtoNoEncontradoException e) {
//			FacesUtil.mensajeError("No se encontró al Director de carrera para la notificación del ingreso de notas.");
			
			
		
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		} 

		if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Esta opción solo está habilitada para registro de notas de carrera.");
			return null;
		}else{
			return "irCargarArchivoNotasRectificacion";	
		}
		
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas2Rectificacion(ParaleloDto prl){
		eafNombre = "2do HEMISEMESTRE";
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			eafDependenciaBuscar = new Dependencia();
			eafDependenciaBuscar =  servDependenciaServicio.buscarFacultadXcrrId(eafEstudianteBuscar.getCrrId());
		
			
			PeriodoAcademico pracCierre=null;
			try {
				pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
			} catch (PeriodoAcademicoNoEncontradoException e1) {
				
			}
			if(pracCierre!=null){
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else{
					if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
							){
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
					}else{
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);	
					}
				}
			}else{
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else {
					if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
							){
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
					}else{
						eafCronogramaActividadJdbcDtoBuscar = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
						eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);						
					}

				}
				
			}
			if(eafUsuario.getUsrNick().equals("fesempertegui")){
				
			}else{
				Date myDate = new Date(new Date().getTime());
				Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaInicio().getTime());
				Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
				Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin().getTime());
				if(myDate.after(maxDate)){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
					return null;
				}
				
				if(myDate.before(finIngresoNota)){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está antes de las fechas establecidas.");
					return null;
				}
			}				
			

			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			
//			for (int i = 0; i < datosCliente.size(); i++) {
//			}
			
			
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			eafRegCliente  = "ID SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|" + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			eafRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 2 RECTIFICACION");
			Materia mtrAux = servNrctfMateriaDto.buscarPorId(eafParaleloDtoEditar.getMtrId());
			if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servNpfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							nrctfDocente.getPrsIdentificacion(),eafParaleloDtoEditar.getMlcrmtNvlId(),eafParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),nrctfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),eafParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota2(null);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
									npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
			            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
			            	
			                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
			                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
			                	npfListEstudianteBusqPrueba.remove(j);
			                    j--;
			                }
			            }
			        }
			
			       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
			    	   boolean op = true;
			            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
			            	if(i!=j){
			            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
				                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
					                	op=false;
				                	}
				                }	
			            	}
			            }
			            if(op){
			            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
			            }
			        }
				} catch (Exception e) {
					 try {
							 listaprueba = new ArrayList<EstudianteJdbcDto>();
							try {
//								Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//										nrctfParaleloDtoEditar.getMlcrprId());
//								System.out.println(mlcrprAuxId);
								MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(eafParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),eafParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
										item.setClfNota2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfNota2(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
										}
									}
								}
								for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
						            	
						                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
						                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
						                	npfListEstudianteBusqPrueba.remove(j);
						                    j--;
						                }
						            }
						        }
						
						       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
						    	   boolean op = true;
						            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
						            	if(i!=j){
						            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
							                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
								                	op=false;
							                	}
							                }	
						            	}
						            }
						            if(op){
						            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
					
	      
			}else{
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
				if(eafParaleloDtoEditar.getHracMlcrprIdComp() == null){// No comparte o no tiene compartidas con nadie
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				}else{// Compartida o dependiente de otra
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,eafParaleloDtoEditar.getMlcrprId());
				}
			}
			
			eafEstudianteBuscar.setMtrId(eafParaleloDtoEditar.getMtrId());
//			System.out.println(eafParaleloDtoEditar.getMtrId());
		for (EstudianteJdbcDto item : eafListEstudianteBusq) {
			try {
				eafAsistenciaDocente=item.getClfAsistenciaDocente2();
				break;
			} catch (Exception e) {
			}
		}
		
		for (EstudianteJdbcDto item : eafListEstudianteBusq) {
			
			if(item.getClfNota2()!=null ){
				String nota = item.getClfNota2().toString();
				int puntoDecimalUbc = nota.indexOf('.');
				int totalDecimales = nota.length() - puntoDecimalUbc -1;
				if(puntoDecimalUbc==-1){
					item.setClfNota2String(nota+",00");
				}else if (totalDecimales==1){
					item.setClfNota2String(nota.replace(".", ",")+"0");
				}else{
					item.setClfNota2String(nota.replace(".", ","));
				}
			}
		}
		
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if((item.getClfAsistenciaDocente2() != null)){
						eafAsistenciaDocente = item.getClfAsistenciaDocente2();
						break;
				}else{
					eafAsistenciaDocente = null;
				}
				
			}
			eafListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(eafListEstudianteBusq);
			cargarReporte2();
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.dependencia.no.encontrado.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));
//		} catch (PersonaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaDtoNoEncontradoException e) {
//			FacesUtil.mensajeError("No se encontró al Director de carrera para la notificación del ingreso de notas.");
		} catch (Exception e1) {
		e1.printStackTrace();
		} 
		return "irCargarArchivoNotas2Rectificar";
	}
	
	
	public void cargarReporte(){
			
			 List<Map<String, Object>> frmCrpCampos = null;
			 Map<String, Object> frmCrpParametros = null;
			 String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DEL REPORTEO  ****************//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "LISTADO"+"_"+eafParaleloDtoEditar.getMtrDescripcion()+"_"+eafParaleloDtoEditar.getPrlDescripcion();
			java.util.Date date= new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha",fecha);
			frmCrpParametros.put("periodo",eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPracDescripcion());
			frmCrpParametros.put("facultad",eafDependenciaBuscar.getDpnDescripcion());
			frmCrpParametros.put("carrera",eafParaleloDtoEditar.getCrrDescripcion());
			frmCrpParametros.put("materia",eafParaleloDtoEditar.getMtrDescripcion());
			frmCrpParametros.put("paralelo",eafParaleloDtoEditar.getPrlDescripcion());
			if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
					|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
					|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
					|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
					|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
				frmCrpParametros.put("parcial", " ");
			}else{
				frmCrpParametros.put("parcial", eafNombre);	
			}
			
			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			int cont = 1;
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				StringBuilder sbNumero = new StringBuilder();
				dato = new HashMap<String, Object>();
				dato.put("identificacion", item.getPrsIdentificacion());
				dato.put("apellido_paterno", item.getPrsPrimerApellido());
				dato.put("apellido_materno", item.getPrsSegundoApellido());
				dato.put("nombres", item.getPrsNombres());
				sbNumero.append(cont);
				dato.put("numero", sbNumero.toString());
				if(item.getClfNota1()!=null){
					if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
						dato.put("nota", item.getClfNota1().toString());
					}else{
							dato.put("nota", item.getClfNota1String().toString());
					}
						
				}else{
					dato.put("nota", "--");
				}
				if(item.getClfAsistenciaEstudiante1()!=null){
					dato.put("asistencia",item.getClfAsistenciaEstudiante1().toString());	
				}else{
					dato.put("asistencia","--");
				}
				
				cont ++;
				
				frmCrpCampos.add(dato);
			}
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
			httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
			httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
			// ******************FIN DE GENERACION DE REPORTE ************//
			 eafActivadorReporte = 1;
	}
	
	public void cargarReporte2(){
		
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL REPORTEO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		frmCrpNombreReporte = "LISTADO"+"_"+eafParaleloDtoEditar.getMtrDescripcion()+"_"+eafParaleloDtoEditar.getPrlDescripcion();
		java.util.Date date= new java.util.Date();
		frmCrpParametros = new HashMap<String, Object>();
		String fecha = new Timestamp(date.getTime()).toString();
		frmCrpParametros.put("fecha",fecha);
		frmCrpParametros.put("periodo",eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPracDescripcion());
		frmCrpParametros.put("facultad",eafDependenciaBuscar.getDpnDescripcion());
		frmCrpParametros.put("carrera",eafParaleloDtoEditar.getCrrDescripcion());
		frmCrpParametros.put("materia",eafParaleloDtoEditar.getMtrDescripcion());
		frmCrpParametros.put("paralelo",eafParaleloDtoEditar.getPrlDescripcion());
		if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
			frmCrpParametros.put("parcial", " ");
		}else{
			frmCrpParametros.put("parcial", eafNombre);	
		}
		
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		int cont = 1;
		for (EstudianteJdbcDto item : eafListEstudianteBusq) {
			StringBuilder sbNumero = new StringBuilder();
			dato = new HashMap<String, Object>();
			dato.put("identificacion", item.getPrsIdentificacion());
			dato.put("apellido_paterno", item.getPrsPrimerApellido());
			dato.put("apellido_materno", item.getPrsSegundoApellido());
			dato.put("nombres", item.getPrsNombres());
			sbNumero.append(cont);
			dato.put("numero", sbNumero.toString());
			if(item.getClfNota2()!=null){
						dato.put("nota", item.getClfNota2String().toString());
			}else{
				dato.put("nota", "--");
			}
			if(item.getClfAsistenciaEstudiante2()!=null){
				dato.put("asistencia",item.getClfAsistenciaEstudiante2().toString());	
			}else{
				dato.put("asistencia","--");
			}
			
			cont ++;
			
			frmCrpCampos.add(dato);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		// ******************FIN DE GENERACION DE REPORTE ************//
		 eafActivadorReporte = 1;
}
	
	public void finReporte(){
		activador= true;
		eafActivadorReporte=0;
		
	}
	
	public String cargarArchivo(FileUploadEvent event) {
		if (eafAsistenciaDocente != null) {
			try {
				if (event != null) {
					eafArchivo = event.getFile().getInputstream();
					eafEtapaProceso = 0;
					try {
						// creo el libro excel del archivo
						Workbook libro = new XSSFWorkbook(eafArchivo);
						
						Sheet hoja = libro.getSheetAt(0); // selecciono la hoja
															// número 1
						Row filaActual; // creo el objeto para la fila que va a
										// ir recorriendo
						Cell celdaActual; // creo el objeto para la celda que va
											// a ir recorriendo
						Iterator<Row> itFilas = hoja.rowIterator();
						for (int i = 0; i < 9; i++) {
							itFilas.next();
						}
						boolean op = true;
						recFilas:while (itFilas.hasNext()) { // recorro las filas
							filaActual = itFilas.next();
							Iterator<Cell> itCeldas = filaActual.cellIterator();
							boolean opNota = false;
							boolean opAsistencia = false;
							String cedulaEstudiante = null;
							BigDecimal nota1 = new BigDecimal(0);
							BigDecimal asistencia1 = new BigDecimal(0);
							while (itCeldas.hasNext()) { // recorro las celdas
															// de esa fila
								celdaActual = itCeldas.next(); // guardo la
																// celda en una
																// variable
								
								if (celdaActual.getColumnIndex() == 2) {
									try {
										cedulaEstudiante = celdaActual.getStringCellValue();
									} catch (Exception e) {
									}
								} else if (celdaActual.getColumnIndex() == 6) {
									if(validador(celdaActual)){
										try {
											String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(String.valueOf(celdaActual.getNumericCellValue()));
											valorSt=valorSt.replace(",", ".");
//										     Float.parseFloat(valorSt);
											nota1 = new BigDecimal(valorSt);
											opNota = true;
//											System.out.println(nota1);
										} catch (Exception e) {
											try {
												String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(celdaActual.getStringCellValue());
												valorSt=valorSt.replace(",", ".");
//											     Float.parseFloat(valorSt);
												nota1 = new BigDecimal(valorSt);
												opNota = true;
											} catch (Exception e2) {
												opNota = false;
											}
										}
									}else{
										op = false;
										eafEtapaProceso = 0;
										break recFilas;
									}
								} else if (celdaActual.getColumnIndex() == 7) {
									if(validadorAsistencia(celdaActual,eafAsistenciaDocente)){
										try {
											asistencia1 = new BigDecimal(celdaActual.getNumericCellValue());
//											System.out.println(asistencia1);
											opAsistencia=true;
										} catch (Exception e) {
											try {
												asistencia1 = new BigDecimal(celdaActual.getStringCellValue());	
												opAsistencia=true;
												
											} catch (Exception e1) {
												opAsistencia=false;
											}
										}
									}else{
										op = false;
										eafEtapaProceso = 0;
										break recFilas;
									}
								}
								eafEtapaProceso = 1;
							}
							if(opNota && opAsistencia){
								for (EstudianteJdbcDto item : eafListEstudianteBusq) {
									if(item.getPrsIdentificacion().equals(cedulaEstudiante)){
										item.setClfNota1(nota1);
										item.setClfAsistenciaEstudiante1(asistencia1.intValue());
										item.setClfAsistenciaDocente1(eafAsistenciaDocente);
											
										String nota = item.getClfNota1().toString();
										int puntoDecimalUbc = nota.indexOf('.');
										int totalDecimales = nota.length() - puntoDecimalUbc -1;
										if(puntoDecimalUbc==-1){
											item.setClfNota1String(nota+",00");
										}else if (totalDecimales==1){
											item.setClfNota1String(nota.replace(".", ",")+"0");
										}else{
											item.setClfNota1String(nota.replace(".", ","));
										}
									
												
		
//										servCalificacionServicio.guardarNotasPregradoPrimerHemi(recordEstudianteDto, eafListEstudianteBusq, regCliente);
//										BigDecimal componente = asistencia1.multiply(new BigDecimal(100));
//										BigDecimal Componente2 = componente.divide(new BigDecimal(eafAsistenciaDocente),2,RoundingMode.HALF_UP);
//										item.setClfTotalAsistencia1(Componente2);
									}
								}	
							}
						}
						if(op){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName().toUpperCase()
									+ " cargado correctamente, por favor proceda con el guardado de datos.");	
						}
					} catch (IOException e) {
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					} catch (EncryptedDocumentException e) {
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					} catch (Exception e) {
						e.printStackTrace();
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(e.getMessage());
					}finally {
						try {
							if (eafArchivo != null) {
								eafArchivo.close();
							}
						} catch (IOException e) {
						}
					}

				} else {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Archivo erróneo");
				}
			} catch (IOException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al intentar cargar el archivo, por favor intente nuevamente");
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("La asistencia del docente es requerida para la carga del archivo.");
		}
		return null;
	}
	
	public String cargarArchivo2(FileUploadEvent event) {
		if (eafAsistenciaDocente != null) {
			try {
				if (event != null) {
					eafArchivo = event.getFile().getInputstream();
					eafEtapaProceso = 0;
					try {
						// creo el libro excel del archivo
						Workbook libro = new XSSFWorkbook(eafArchivo);
						
						Sheet hoja = libro.getSheetAt(0); // selecciono la hoja
															// número 1
						Row filaActual; // creo el objeto para la fila que va a
										// ir recorriendo
						Cell celdaActual; // creo el objeto para la celda que va
											// a ir recorriendo
						Iterator<Row> itFilas = hoja.rowIterator();
						for (int i = 0; i < 9; i++) {
							itFilas.next();
						}
						boolean op = true;
						recFilas:while (itFilas.hasNext()) { // recorro las filas
							filaActual = itFilas.next();
							Iterator<Cell> itCeldas = filaActual.cellIterator();
							boolean opNota = false;
							boolean opAsistencia = false;
							String cedulaEstudiante = null;
							BigDecimal nota2 = new BigDecimal(0);
							BigDecimal asistencia2 = new BigDecimal(0);
							while (itCeldas.hasNext()) { // recorro las celdas
															// de esa fila
								celdaActual = itCeldas.next(); // guardo la
																// celda en una
																// variable
								
								if (celdaActual.getColumnIndex() == 2) {
									try {
										cedulaEstudiante = celdaActual.getStringCellValue();
									} catch (Exception e) {
									}
								} else if (celdaActual.getColumnIndex() == 6) {
									if(validador(celdaActual)){
										try {
											String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(String.valueOf(celdaActual.getNumericCellValue()));
											valorSt=valorSt.replace(",", ".");
//										     Float.parseFloat(valorSt);
											nota2 = new BigDecimal(valorSt);
											opNota = true;
//											System.out.println(nota2);
										} catch (Exception e) {
											try {
												String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(celdaActual.getStringCellValue());
												valorSt=valorSt.replace(",", ".");
//											     Float.parseFloat(valorSt);
												nota2 = new BigDecimal(valorSt);
												opNota = true;
											} catch (Exception e2) {
												opNota = false;
											}
										}
									}else{
										op = false;
										eafEtapaProceso = 0;
										break recFilas;
									}
								} else if (celdaActual.getColumnIndex() == 7) {
									if(validadorAsistencia(celdaActual,eafAsistenciaDocente)){
										try {
											asistencia2 = new BigDecimal(celdaActual.getNumericCellValue());
//											System.out.println(asistencia2);
											opAsistencia=true;
										} catch (Exception e) {
											try {
												asistencia2 = new BigDecimal(celdaActual.getStringCellValue());	
												opAsistencia=true;
												
											} catch (Exception e1) {
												opAsistencia=false;
											}
										}
									}else{
										op = false;
										eafEtapaProceso = 0;
										break recFilas;
									}
								}
								eafEtapaProceso = 1;
							}
							if(opNota && opAsistencia){
								for (EstudianteJdbcDto item : eafListEstudianteBusq) {
									if(item.getPrsIdentificacion().equals(cedulaEstudiante)){
										item.setClfNota2(nota2);
										item.setClfAsistenciaEstudiante2(asistencia2.intValue());
										item.setClfAsistenciaDocente2(eafAsistenciaDocente);
											
										String nota = item.getClfNota2().toString();
										int puntoDecimalUbc = nota.indexOf('.');
										int totalDecimales = nota.length() - puntoDecimalUbc -1;
										if(puntoDecimalUbc==-1){
											item.setClfNota1String(nota+",00");
										}else if (totalDecimales==1){
											item.setClfNota2String(nota.replace(".", ",")+"0");
										}else{
											item.setClfNota2String(nota.replace(".", ","));
										}
									
												
		
//										servCalificacionServicio.guardarNotasPregradoPrimerHemi(recordEstudianteDto, eafListEstudianteBusq, regCliente);
//										BigDecimal componente = asistencia1.multiply(new BigDecimal(100));
//										BigDecimal Componente2 = componente.divide(new BigDecimal(eafAsistenciaDocente),2,RoundingMode.HALF_UP);
//										item.setClfTotalAsistencia1(Componente2);
									}
								}	
							}
						}
						if(op){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName().toUpperCase()
									+ " cargado correctamente, por favor proceda con el guardado de datos.");	
						}
					} catch (IOException e) {
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					} catch (EncryptedDocumentException e) {
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					} catch (Exception e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					}finally {
						try {
							if (eafArchivo != null) {
								eafArchivo.close();
							}
						} catch (IOException e) {
						}
					}

				} else {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Archivo erróneo");
				}
			} catch (IOException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al intentar cargar el archivo, por favor intente nuevamente");
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("La asistencia del docente es requerida para la carga del archivo.");
		}
		return null;
	}
	
	public String cargarArchivoIdiomas(FileUploadEvent event) {
		if (eafAsistenciaDocente != null) {
			try {
				if (event != null) {
					eafArchivo = event.getFile().getInputstream();
					eafEtapaProceso = 0;
					try {
						// creo el libro excel del archivo
						Workbook libro = new XSSFWorkbook(eafArchivo);
						
						Sheet hoja = libro.getSheetAt(0); // selecciono la hoja
															// número 1
						Row filaActual; // creo el objeto para la fila que va a
										// ir recorriendo
						Cell celdaActual; // creo el objeto para la celda que va
											// a ir recorriendo
						Iterator<Row> itFilas = hoja.rowIterator();
						for (int i = 0; i < 9; i++) {
							itFilas.next();
						}
						boolean op = true;
						recFilas:while (itFilas.hasNext()) { // recorro las filas
							filaActual = itFilas.next();
							Iterator<Cell> itCeldas = filaActual.cellIterator();
							boolean opNota = false;
							boolean opAsistencia = false;
							String cedulaEstudiante = null;
							BigDecimal nota1 = new BigDecimal(0);
							BigDecimal asistencia1 = new BigDecimal(0);
							while (itCeldas.hasNext()) { // recorro las celdas
															// de esa fila
								celdaActual = itCeldas.next(); // guardo la
																// celda en una
																// variable
								
								if (celdaActual.getColumnIndex() == 2) {
									try {
										cedulaEstudiante = celdaActual.getStringCellValue();
									} catch (Exception e) {
									}
								} else if (celdaActual.getColumnIndex() == 6) {
									if(validadorIdiomas(celdaActual)){
										try {
											String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(String.valueOf(celdaActual.getNumericCellValue()));
											valorSt=valorSt.replace(",", ".");
//										     Float.parseFloat(valorSt);
											nota1 = new BigDecimal(valorSt);
											opNota = true;
										} catch (Exception e) {
											try {
												String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(celdaActual.getStringCellValue());
												valorSt=valorSt.replace(",", ".");
//											     Float.parseFloat(valorSt);
												nota1 = new BigDecimal(valorSt);
												opNota = true;
											} catch (Exception e2) {
												opNota = false;
											}
										}
									}else{
										op = false;
										eafEtapaProceso = 0;
										break recFilas;
									}
								} else if (celdaActual.getColumnIndex() == 7) {
									if(validadorAsistencia(celdaActual,eafAsistenciaDocente)){
										try {
											asistencia1 = new BigDecimal(celdaActual.getNumericCellValue());
											opAsistencia=true;
										} catch (Exception e) {
											try {
												asistencia1 = new BigDecimal(celdaActual.getStringCellValue());	
												opAsistencia=true;
												
											} catch (Exception e1) {
												opAsistencia=false;
											}
										}
									}else{
										op = false;
										eafEtapaProceso = 0;
										break recFilas;
									}
								}
								eafEtapaProceso = 1;
							}
							if(opNota && opAsistencia){
								for (EstudianteJdbcDto item : eafListEstudianteBusq) {
									if(item.getPrsIdentificacion().equals(cedulaEstudiante)){
										item.setClfNota1(nota1);
										item.setClfAsistenciaEstudiante1(asistencia1.intValue());
										item.setClfAsistenciaDocente1(eafAsistenciaDocente);
									}
								}	
							}
						}
						if(op){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName().toUpperCase()
									+ " cargado correctamente, para finalizar el proceso de click en el botón Guardar.");	
						}
					} catch (IOException e) {
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					} catch (EncryptedDocumentException e) {
						FacesUtil.mensajeError(
								"Error al validar el archivo, por favor revise el archivo de carga e intente más tarde");
					} catch (Exception e) {
						e.printStackTrace();
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(e.getMessage());
					}finally {
						try {
							if (eafArchivo != null) {
								eafArchivo.close();
							}
						} catch (IOException e) {
						}
					}

				} else {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Archivo erróneo");
				}
			} catch (IOException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al intentar cargar el archivo, por favor intente nuevamente");
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("La asistencia del docente es requerida para la carga del archivo.");
		}
		return null;
	}

	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		eafListParaleloBusq = null;
		try {
			if(eafEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			}else if(eafEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else if(eafEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			}else{
//				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//				eafListParaleloBusq = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocente(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
				
				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
				//busco compartidas
				//busco las no compartidas
				PeriodoAcademico pracCierre = null;
//				try {
//					pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();				
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				List<ParaleloDto> listNoCompartida = new ArrayList<>();

//				if(pracCierre!=null){
//					
//					listNoCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoEnCierreXcarreraXnivelXdocenteNoComp(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
//					
//
////				listNoCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
////				listNoCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteNoComp(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
//
//
//					eafListParaleloBusq = new ArrayList<>();
//					List<ParaleloDto> listCompartida = new ArrayList<>();
//					try {
//						
//						listCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoEnCierreXcarreraXnivelXdocenteCompartida(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());										
//						if(listCompartida.size()!=0){
//							for (ParaleloDto item : listCompartida) {
//								eafListParaleloBusq.add(item);
//							}	
//						}
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}else{
					
					listNoCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());
					

					eafListParaleloBusq = new ArrayList<>();
					List<ParaleloDto> listCompartida = new ArrayList<>();
					try {
						
						listCompartida = servNrctfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(eafEstudianteBuscar.getCrrId(), eafEstudianteBuscar.getNvlId(), nrctfDocente.getDtpsId(), eafEstudianteBuscar.getMtrId());										
						if(listCompartida.size()!=0){
							for (ParaleloDto item : listCompartida) {
								eafListParaleloBusq.add(item);
							}	
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
//				}
				


				//asignación a una sola lista


				for (ParaleloDto item : listNoCompartida) {
					eafListParaleloBusq.add(item);
				}
				
				Collections.sort(eafListParaleloBusq, new Comparator<ParaleloDto>() {
					public int compare(ParaleloDto obj1, ParaleloDto obj2) {
						return new Integer(obj1.getPrlId()).compareTo(new Integer(obj2.getPrlId()));
					}
				});
				
			}
		} catch (ParaleloDtoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.buscar.exception")));
		} catch (ParaleloDtoNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.buscar.no.encontrado.exception")));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que llama los datos para la genracion del reporte
	 */
	public void reporteNotas(){
		
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	// Suma los días recibidos a la fecha  
	public Date sumarRestarDiasFecha(Date fecha, int dias){
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
	    calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o restar en caso de días<0
	    return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas primer parcial
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificar1Parcial(ParaleloDto prl){
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			eafRegCliente  = "ID SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|" + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			eafRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 1 RECTIFICAR");
			eafDependenciaBuscar =  servDependenciaServicio.buscarPorId(eafEstudianteBuscar.getCrrId());
			PeriodoAcademico pracCierre = null;
			try {
				pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();				
			} catch (Exception e) {
				// TODO: handle exception
			}

			if(pracCierre!=null){
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}
			}else{
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}
				
			}
			
			
//			nrctfDependenciaBuscar =  servNrctfDependenciaServicio.buscarPorId(nrctfEstudianteBuscar.getCrrId());
//			if(nrctfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}else{
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}
			Date myDate = new Date(new Date().getTime());
			Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio().getTime());
			Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
			Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaFin().getTime());
			
			if(myDate.after(maxDate)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			
			if(myDate.before(finIngresoNota)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta antes de las fechas establecidas");
				return null;
			}
			
			eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacion(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());

			nrctfListEstudianteEditar = new ArrayList<EstudianteJdbcDto>();
			nrctfListEstudianteEditar = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacion(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
			
			nrctfDirCarrera = new PersonaDto();
			nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, eafParaleloDtoEditar.getCrrId());
			
			
			nrctfTipoRectificacion = new Integer(1);
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente1() != null)){
					eafAsistenciaDocente = null;
				}else{
					eafAsistenciaDocente = item.getClfAsistenciaDocente1();
					break;
				}
				
			}
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.dependencia.no.encontrado.exception")));
		} catch (DependenciaException e){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.dependencia.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar1Parcial.PersonaDto.no.encontrado.exception")));
		} 
		return "irRectificar1Parcial";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas segundo parcial
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificar2Parcial(ParaleloDto prl){
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			eafRegCliente  = "ID SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|" + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			eafRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 2 RECTIFICAR");
			eafDependenciaBuscar =  servDependenciaServicio.buscarPorId(eafEstudianteBuscar.getCrrId());
			PeriodoAcademico pracCierre = null;
			try {
				pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();				
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(pracCierre!=null){
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}
			}else{
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}
				
			}
			
			
//			nrctfDependenciaBuscar =  servNrctfDependenciaServicio.buscarPorId(nrctfEstudianteBuscar.getCrrId());
//			if(nrctfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}else{
//				nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
//				nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECTIFICACION_VALUE);
//			}
			Date myDate = new Date(new Date().getTime());
			Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio().getTime());
			Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
			Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaFin().getTime());
			
			if(myDate.after(maxDate)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			
			if(myDate.before(finIngresoNota)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta antes de las fechas establecidas");
				return null;
			}
			
			eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacion(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
			nrctfListEstudianteEditar = new ArrayList<EstudianteJdbcDto>();
			nrctfListEstudianteEditar =  servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacion(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
			
			nrctfDirCarrera = new PersonaDto();
			nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, eafParaleloDtoEditar.getCrrId());
			
			
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente2() != null)){
					eafAsistenciaDocente = null;
				}else{
					eafAsistenciaDocente = item.getClfAsistenciaDocente2();
					break;
				}
			}
			nrctfTipoRectificacion = new Integer(2);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.dependencia.no.encontrado.exception")));
		} catch (DependenciaException e){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.dependencia.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificar2Parcial.PersonaDto.no.encontrado.exception")));
		} 
		return "irRectificar2Parcial";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas segundo parcial
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificarRecuperacion(ParaleloDto prl){
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		eafDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			eafRegCliente  = "ID SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|" + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			eafRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS RECUPERACION RECTIFICAR");
			eafDependenciaBuscar =  servDependenciaServicio.buscarPorId(eafEstudianteBuscar.getCrrId());
			PeriodoAcademico pracCierre = null;
			try {
				pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();				
			} catch (Exception e) {
				
			}

			if(pracCierre!=null){
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
						
						){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}
					
			}else{
				if(eafDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico  = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_VALUE || eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
						){
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}else{
					eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				}
				
			}
			
			Date myDate = new Date(new Date().getTime());
			Date siguientePracDate = new Date(eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio().getTime());
			Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
			Date finIngresoNota = new Date(eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico.getPlcrFechaFin().getTime());
			
			if(myDate.after(maxDate)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Está fuera de las fechas establecidas");
				return null;
			}
			
			if(myDate.before(finIngresoNota)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Está antes de las fechas establecidas");
				return null;
			}
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(eafParaleloDtoEditar.getMtrId());
			if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El proceso de recuperación para materias modulares no se encuentra disponible en estos momentos.");
				return null;
//				Integer mlcrprAuxId =  servNpfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), nrctfDocente.getPrsIdentificacion(),eafEstudianteBuscar.getNvlId(),eafParaleloDtoEditar.getPrlId());
//				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
//				mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
//				mtrModulo = mtrAux;
//				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
//				listaprueba = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
//						eafParaleloDtoEditar.getCrrId(), eafEstudianteBuscar.getNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
//						mtrAux.getMtrMateria().getMtrId(),nrctfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),eafParaleloDtoEditar.getMlcrprId());
//				eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
//				List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
//				for (EstudianteJdbcDto item : listaprueba) {
//					item.setMateriaModulo(true);
//					// En caso de que no existe el resultado de búsqueda
//					if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
//						item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
//						item.setClfNota2(null);
//						item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
//						item.setClfAsistenciaEstudiante2(null);
//						item.setClfNota2(null);
//						npfListEstudianteBusqPrueba.add(item);
//					}else{
//						//En caso de que sean la misma malla_curricula_paralelo
//						if(item.getMlcrprIdModulo()==eafParaleloDtoEditar.getMlcrprId().intValue()){
//							npfListEstudianteBusqPrueba.add(item);
//						}else{
////							//En caso de no haber duplicados
////							if(!op){
//								item.setClfId(GeneralesConstantes.APP_ID_BASE);
//								item.setClfNota2(null);
//								item.setClfAsistenciaEstudiante2(null);
//								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
//								item.setMlcrprIdModulo(eafParaleloDtoEditar.getMlcrprId());
//								npfListEstudianteBusqPrueba.add(item);
////							}
//						}
//					}
//				}
//				for(int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
//		            for(int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {
//		            	
//		                if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
//		                		&& (npfListEstudianteBusqPrueba.get(i).getClfId()==npfListEstudianteBusqPrueba.get(j).getClfId())){
//		                	npfListEstudianteBusqPrueba.remove(j);
//		                    j--;
//		                }
//		            }
//		        }
//				
//			       for(int i =0; i<npfListEstudianteBusqPrueba.size();i++){
//			    	   boolean op = true;
//			            for (int j=0; j<npfListEstudianteBusqPrueba.size();j++){
//			            	if(i!=j){
//			            		if(npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())){
//				                	if(npfListEstudianteBusqPrueba.get(i).getClfId()==GeneralesConstantes.APP_ID_BASE){
//					                	op=false;
//				                	}
//				                }	
//			            	}
//			            }
//			            if(op){
//			            	eafListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
//			            }
//			        }
			}else{
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
				if(eafParaleloDtoEditar.getHracMlcrprIdComp() == null){// No comparte o no tiene compartidas con nadie
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
				}else{// Compartida o dependiente de otra
					eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
							eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), 
							eafParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,eafParaleloDtoEditar.getMlcrprId());
				}
			}
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				if(item.getClfAsistenciaDocente2() == GeneralesConstantes.APP_ID_BASE){
						eafAsistenciaDocente = null;
				}else{
					eafAsistenciaDocente = item.getClfAsistenciaDocente2();
					break;
				}
			}
			Iterator<EstudianteJdbcDto> it = eafListEstudianteBusq.iterator();
			while (it.hasNext()){
				EstudianteJdbcDto aux = new EstudianteJdbcDto();
				aux= it.next();
				if(aux.getRcesEstado() != RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE){
						if (aux.getRcesIngersoNota3()!=RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
							it.remove();		
						}
				}else{
					it.remove();
				}
			}
			
			nrctfDirCarrera = new PersonaDto();
			nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, eafParaleloDtoEditar.getCrrId());
			
			nrctfTipoRectificacion = new Integer(3);
			if(eafListEstudianteBusq.isEmpty()){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen estudiantes en el paralelo para rectificar notas de recuperación");
				return null;
			}
		} catch (DependenciaNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.dependencia.no.encontrado.exception")));
		} catch (DependenciaException e){
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.dependencia.exception")));
		} catch (CronogramaActividadDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.EstudianteDtoJdbcc.no.encontrado.exception")));
		} catch (PersonaDtoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.PersonaDto.no.encontrado.exception")));
	
		} catch (MateriaNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MateriaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "irRectificarRecuperacion";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por carrera nivel y materia 
	 * @return Xhtml listar
	 */
	public String irCancelar(){
		eafParaleloDtoEditar = null;
		eafListEstudianteBusq = null;
		nrctfListEstudianteEditar = null;
		nrctfDirCarrera = null;
		nrctfValidadorClic = new Integer(0);
		nrctfTipoRectificacion = new Integer(0);
		eafEtapaProceso = 0;
		iniciarParametros();
		return "irCancelar";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por carrera nivel y materia con todos los datos de busqueda
	 * @return Xhtml listar
	 */
	public String irListarParalelos(){
//		eafParaleloDtoEditar = null;
		eafListEstudianteBusq = null;
//		nrctfListEstudianteEditar = null;
		nrctfDirCarrera = null;
		nrctfValidadorClic = new Integer(0);
		nrctfTipoRectificacion = new Integer(0);
		eafEtapaProceso = 0;
//		iniciarParametros();
		activador=false;
		return "irListarParalelos";
	}
	
	
	/*
	 * Metodo que permite generar el reporte
	 */
	public void generarReporte(ParaleloDto prl){
		
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
			
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("static-access")
	public String irVerNotas(ParaleloDto prl){
		ReporteNotasForm reporte = new ReporteNotasForm();
		eafParaleloDtoEditar = new ParaleloDto(); 
		eafParaleloDtoEditar = prl;
		eafListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			eafListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(eafParaleloDtoEditar.getCrrId(), eafParaleloDtoEditar.getMlcrmtNvlId(), eafParaleloDtoEditar.getPrlId(), eafParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),eafParaleloDtoEditar.getMlcrprId());
			
			activacion();
			reporte.generarReporteNotas(eafListEstudianteBusq, eafUsuario);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas";
	}
	
	
	/**
	 * Método calcula el porcentaje de asistencia
	 * @param porcentaje .- valor del porsentaje del calculo
	 * @param asitenciaEst.- valor de la asistencia de estudiante
	 * @param asitenciaDoc.- valor de la asistencia del docente
	 * @return.- valor del porsentaje en bigdecimal con dos decimales sin redondeo
	 */
	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		 BigDecimal itemCost  = BigDecimal.ZERO;
	     itemCost  = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje)).divide(new BigDecimal(asitenciaDoc), 0, RoundingMode.HALF_UP);
	     return itemCost;
	}
	
	
	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinalRecuperacion(){
//			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
//				if(item.getClfSupletorio()!=null){
//					if(!validador(item.getClfSupletorio())){
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
//						nrctfValidadorClic = 0;
//						return null;
//					}
//				}else{
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" debe ingresar la nota de recuperación");
//					nrctfValidadorClic = 0;
//					return null;
//				}
//			}
		nrctfValidadorClic = 2;
		return null;
	}

		/**
		 * verifica que haga click en el boton guardar final la nota
		 */
		public String verificarClicGuardadoFinal(){
			
			Boolean verificarGuardar = false;
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				RecordEstudianteDto rcesAux = new RecordEstudianteDto();
				try {
					if(item.getClfNota1()!=null){
					rcesAux = servNrctfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getRcesId());
					
					if(eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
							|| eafEstudianteBuscar.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
						try {
							//calculo la suma de na nota1 + nota2 con redondeo de una cifra decimal
							BigDecimal sumaParciales = BigDecimal.ZERO;
							sumaParciales = item.getClfNota1();
							item.setClfSumaP1P2(sumaParciales);
							
							//calculo de la suma de asistencia del estudiante de los dos parciales
							int sumaAsistenciaParciales= 0;
							sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1();
							item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));
							
							//calculo de la suma de la asistencia del docente de los dos parciales
							int sumaAsistenciaDoc = 0;
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() ;
							item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));
							
							//calcula el promedio de la asistencia del estudiante
							item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, item.getClfAsistenciaTotal().intValue(), item.getClfAsistenciaTotalDoc().intValue()));
							
							int com = item.getClfSumaP1P2().compareTo(new BigDecimal(14));
							//si la suma de los parciales es mayor o igual a 14
							if(com == 1 || com == 0){
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									// si el promedio de asistencia es mayor o igual a 80
								if(promedioAsistencia == 1 || promedioAsistencia == 0){
									//calcula la nota final del semestre y el estado a aprobado
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								}else{// si el promedio de asistencia es menor a 80
									item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							}else{
								item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.DOWN));
								item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							}
						} catch (Exception e) {
						}
					}
					verificarGuardar = servCalificacionServicio.guardarEdicionNotasPrimerHemi(rcesAux, item , eafRegCliente);
//					try {
//						verificarClicGuardadoFinal2();						
//					} catch (Exception e) {
//						// TODO: handle exception
//					}

					
					
					eafEtapaProceso = 0;

				}	
				} catch (SQLIntegrityConstraintViolationException  | ConstraintViolationException | PersistenceException e) {
					FacesUtil.mensajeError("Ocurrió un error al guardar las notas, por favor revise los listados de notas.");
				}catch (RecordEstudianteDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (RecordEstudianteDtoNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (CalificacionValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (CalificacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
			}
//			
			if(verificarGuardar){
				nrctfValidadorClic = 2;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.verificarGuardar.exitoso")));
				

//				// ******************************************************************************
//				// ************************* ACA INICIA EL ENVIO DE MAIL CON
//				// ADJUNTO ************
//				// ******************************************************************************
//
//				try {
//					Connection connection = null;
//					Session session = null;
//					MessageProducer producer = null;
//					ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
//							GeneralesConstantes.APP_NIO_ACTIVEMQ);
//					connection = connectionFactory.createConnection();
//					connection.start();
//					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//					Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
//					// Creamos un productor
//					producer = session.createProducer(destino);
//
//					JasperReport jasperReport = null;
//					JasperPrint jasperPrint;
//					StringBuilder pathGeneralReportes = new StringBuilder();
//					pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
//					Carrera crrAux = servNpfCarreraServicio.buscarPorId(eafEstudianteBuscar.getCrrId());
//					if (eafEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
//							|| eafEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
//							|| eafEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
//							|| eafEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
//							|| eafEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
//						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasIdiomas");
//						jasperReport = (JasperReport) JRLoader
//								.loadObject(new File((pathGeneralReportes.toString() + "/reporteNotasIdiomas.jasper")));
//					} else if(crrAux.getCrrTipo()==CarreraConstantes.TIPO_POSGRADO_VALUE){
//						pathGeneralReportes.append(FacesContext.getCurrentInstance()
//								.getExternalContext().getRealPath("/"));
//						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasPosgrado");
//						jasperReport = (JasperReport) JRLoader.loadObject(new File(
//								(pathGeneralReportes.toString() + "/reporteNotaPosgrado.jasper")));
//					}else {
//						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
//						jasperReport = (JasperReport) JRLoader
//								.loadObject(new File((pathGeneralReportes.toString() + "/reporteNota1Hemi.jasper")));
//					}
//
//					List<Map<String, Object>> frmAdjuntoCampos = null;
//					Map<String, Object> frmAdjuntoParametros = null;
//					// String facultadMail =
//					// GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
//					// String carreraMail =
//					// GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
//
//					frmAdjuntoParametros = new HashMap<String, Object>();
//					SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss",
//							new Locale("es", "ES"));
//					String fecha = formato.format(new Date());
//					frmAdjuntoParametros.put("fecha", fecha);
//					frmAdjuntoParametros.put("docente", nrctfDocente.getPrsNombres() + " "
//							+ nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido());
//					frmAdjuntoParametros.put("nick", eafUsuario.getUsrNick());
//					StringBuilder sbAsistenciaDocente = new StringBuilder();
//					Materia mtr = servNpfMateriaServicio.buscarPorId(eafEstudianteBuscar.getMtrId());
//					for (EstudianteJdbcDto item : eafListEstudianteBusq) {
//						try {
//							mtrAux = servNpfMateriaServicio.buscarPorId(mtrModulo.getMtrId());
//							frmAdjuntoParametros.put("materia", 
//									item.getMtrDescripcion() + " - " + mtrAux.getMtrDescripcion());
//							Carrera crrAux1 = servNpfCarreraServicio.buscarPorId(npfParaleloDtoEditar.getCrrId());
//							Dependencia fclAux = servDependenciaServicio
//									.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
//							Nivel nvlAux = servNivelServicio.buscarPorId(npfEstudianteBuscar.getNvlId());
//							item.setCrrDescripcion(crrAux1.getCrrDescripcion());
//							item.setCrrDetalle(crrAux1.getCrrDetalle());
//							item.setNvlDescripcion(nvlAux.getNvlDescripcion());
//							item.setDpnDescripcion(fclAux.getDpnDescripcion());
//						} catch (MateriaNoEncontradoException | MateriaException e) {
//							frmAdjuntoParametros.put("materia",	item.getMtrDescripcion());
//						} catch (NivelNoEncontradoException e) {
//						} catch (NivelException e) {
//						} catch (CarreraNoEncontradoException e) {
//						} catch (CarreraException e) {
//						} catch (DependenciaNoEncontradoException e) {
//						} catch (Exception e) {
//							frmAdjuntoParametros.put("materia",
//									item.getMtrDescripcion());
//						}
//						npfNomCarrera = item.getCrrDescripcion().toString();
//						npfNomMateria = item.getMtrDescripcion().toString();
//						npfNomParalelo = item.getPrlDescripcion().toString();
//						frmAdjuntoParametros.put("periodo", item.getPracDescripcion());
//						frmAdjuntoParametros.put("facultad", item.getDpnDescripcion());
//						frmAdjuntoParametros.put("carrera", item.getCrrDetalle());
//						frmAdjuntoParametros.put("curso", item.getNvlDescripcion());
//						frmAdjuntoParametros.put("paralelo", item.getPrlDescripcion());
//
//						sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
//						frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
//						break;
//					}
//					frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes + "/cabeceraNotas.png");
//					frmAdjuntoParametros.put("imagenPie", pathGeneralReportes + "/pieNotas.png");
//
//					frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
//					Map<String, Object> datoEstudiantes = null;
//
//					int cont = 1;
//					for (EstudianteJdbcDto item : eafListEstudianteBusq) {
//						StringBuilder sbNota1 = new StringBuilder();
//						StringBuilder sbAsistencia1 = new StringBuilder();
//						StringBuilder sbContador = new StringBuilder();
//						datoEstudiantes = new HashMap<String, Object>();
//						datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
//						datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//						datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
//						datoEstudiantes.put("nombres", item.getPrsNombres());
//						if (item.getClfNota1() != null) {
//							sbNota1.append(item.getClfNota1());
//							datoEstudiantes.put("nota1", sbNota1.toString());
//						} else {
//							sbNota1.append("-");
//							datoEstudiantes.put("nota1", sbNota1.toString());
//						}
//						if (item.getClfAsistenciaEstudiante1() != null) {
//							if (crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
//								sbAsistencia1.append(item.getClfPromedioAsistencia().setScale(0, RoundingMode.DOWN));
//								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//								if (item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) {
//									datoEstudiantes.put("estado", "APROB.");
//								} else {
//									datoEstudiantes.put("estado", "REPROB.");
//								}
//							} else {
//								sbAsistencia1.append(item.getClfAsistenciaEstudiante1());
//								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//							}
//
//						} else {
//							sbAsistencia1.append("-");
//							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//							if (crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
//									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
//								datoEstudiantes.put("estado", "-");
//							}
//						}
//						sbContador.append(cont);
//						datoEstudiantes.put("numero", sbContador.toString());
//
//						frmAdjuntoCampos.add(datoEstudiantes);
//						cont = cont + 1;
//					}
//
//					StringBuilder sbPromedio = new StringBuilder();
//					if (cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO) {
//						BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
//						if (promedio != null) {
//							sbPromedio.append(promedio);
//							frmAdjuntoParametros.put("promedio", sbPromedio.toString());
//						} else {
//							sbPromedio.append("-");
//							frmAdjuntoParametros.put("promedio", sbPromedio.toString());
//						}
//					} else {
//						sbPromedio.append("-");
//						frmAdjuntoParametros.put("promedio", sbPromedio.toString());
//					}
//					JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);
//
//					jasperPrint = JasperFillManager.fillReport(jasperReport, frmAdjuntoParametros, dataSource);
//
//					byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
//
//					// lista de correos a los que se enviara el mail
//					List<String> correosTo = new ArrayList<>();
//					correosTo.add(nrctfDocente.getPrsMailInstitucional());
//					// path de la plantilla del mail
//					ProductorMailJson pmail = null;
//					StringBuilder sbCorreo = new StringBuilder();
//
//					if (crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
//							|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
//							|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
//							|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
//							|| crrAux.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
//						sbCorreo = GeneralesUtilidades.generarAsuntoIdiomas(
//								GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
//								nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido() + " "
//										+ nrctfDocente.getPrsNombres(),
//								GeneralesUtilidades.generaStringConTildes(crrAux.getCrrDescripcion()),
//								GeneralesUtilidades.generaStringConTildes(npfNomMateria), npfNomParalelo);
//					} else {
//						sbCorreo = GeneralesUtilidades.generarAsunto(
//								GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
//								nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido() + " "
//										+ nrctfDocente.getPrsNombres(),
//								GeneralesUtilidades.generaStringConTildes(crrAux.getCrrDescripcion()),
//								GeneralesUtilidades.generaStringConTildes(npfNomMateria), npfNomParalelo);
//					}
//
//					pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
//							GeneralesConstantes.APP_REGISTRO_NOTAS, sbCorreo.toString(), "admin", "dt1c201s", true,
//							arreglo, "RegistroNotas." + MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF);
//					String jsonSt = pmail.generarMail();
//					Gson json = new Gson();
//					MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
//					// Iniciamos el envío de mensajes
//					ObjectMessage message = session.createObjectMessage(mailDto);
//					producer.send(message);
//				} catch (Exception e) {
//				}

				// ******************************************************************************
				// *********************** ACA FINALIZA EL ENVIO DE MAIL
				// ************************
				// ******************************************************************************
				
				
				return null;
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.verificarGuardar.error")));
				return null;
			}
		}
		

		
		/**
		 * verifica que haga click en el boton guardar final la nota
		 */
		public String verificarClicGuardadoFinal2(){
			
			Boolean verificarGuardar = false;
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				RecordEstudianteDto rcesAux = new RecordEstudianteDto();
				try {
					rcesAux = servNrctfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getRcesId());
					
					if(item.getClfNota2()!=null && item.getClfAsistenciaEstudiante2()!=null){
						try {
							//calculo la suma de na nota1 + nota2 con redondeo de una cifra decimal
							BigDecimal sumaParciales = BigDecimal.ZERO;
							sumaParciales = item.getClfNota1().setScale(2, RoundingMode.DOWN).add(item.getClfNota2().setScale(2, RoundingMode.DOWN));
							item.setClfSumaP1P2(sumaParciales);
							
							//calculo de la suma de asistencia del estudiante de los dos parciales
							int sumaAsistenciaParciales= 0;
							sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1()+item.getClfAsistenciaEstudiante2();
							item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));
							
							//calculo de la suma de la asistencia del docente de los dos parciales
							int sumaAsistenciaDoc = 0;
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() +  item.getClfAsistenciaDocente2();
							item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));
							
							//calcula el promedio de la asistencia del estudiante
							item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, item.getClfAsistenciaTotal().intValue(), item.getClfAsistenciaTotalDoc().intValue()));
							
							int com = item.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
							//si la suma de los parciales es mayor o igual a 27.5
							if(com == 1 || com == 0){
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									// si el promedio de asistencia es mayor o igual a 80
								if(promedioAsistencia == 1 || promedioAsistencia == 0){
									//calcula la nota final del semestre y el estado a aprobado
									item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								}else{// si el promedio de asistencia es menor a 80
									item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							}else{
//								if(item.getDtmtNumero()==3){
//									item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
//									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//								}else{
									int minNota = item.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
									//si la suma de los parciales el menor a 27.5 y es mayor o igual a 8.8
									if(minNota==0 || minNota==1){
										int promedioAsistencia = 0;
										promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
											if(promedioAsistencia == 1 || promedioAsistencia == 0){
												BigDecimal itemCost  = BigDecimal.ZERO;
												itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
												item.setClfParamRecuperacion1(itemCost);
												item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
												item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
											}else{
												BigDecimal itemCost  = BigDecimal.ZERO;
												itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
												item.setClfParamRecuperacion1(itemCost);
												item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
												item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
											}
									}else{
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
//								}
								
							}
						} catch (Exception e) {
						}
						
						try {
							verificarGuardar = servCalificacionServicio.guardarEdicionNotasSegundoHemi(rcesAux, item, eafRegCliente);
						} catch (CalificacionValidacionException e) {
						} catch (CalificacionException e) {
						}	
					}else{
						try {
							//calculo la suma de na nota1 + nota2 con redondeo de una cifra decimal
							item.setClfNota2(BigDecimal.ZERO);
							item.setClfAsistenciaEstudiante2(Integer.valueOf(0));
							BigDecimal sumaParciales = BigDecimal.ZERO;
							sumaParciales = item.getClfNota1().setScale(2, RoundingMode.DOWN).add(item.getClfNota2().setScale(2, RoundingMode.DOWN));
							item.setClfSumaP1P2(sumaParciales);
							
							//calculo de la suma de asistencia del estudiante de los dos parciales
							int sumaAsistenciaParciales= 0;
							sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1()+item.getClfAsistenciaEstudiante2();
							item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));
							
							//calculo de la suma de la asistencia del docente de los dos parciales
							int sumaAsistenciaDoc = 0;
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() +  item.getClfAsistenciaDocente2();
							item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));
							
							//calcula el promedio de la asistencia del estudiante
							item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, item.getClfAsistenciaTotal().intValue(), item.getClfAsistenciaTotalDoc().intValue()));
							
							int com = item.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
							//si la suma de los parciales es mayor o igual a 27.5
							if(com == 1 || com == 0){
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									// si el promedio de asistencia es mayor o igual a 80
								if(promedioAsistencia == 1 || promedioAsistencia == 0){
									//calcula la nota final del semestre y el estado a aprobado
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								}else{// si el promedio de asistencia es menor a 80
									item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							}else{
								if(item.getDtmtNumero()==3){
									item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}else{
									int minNota = item.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
									//si la suma de los parciales el menor a 27.5 y es mayor o igual a 8.8
									if(minNota==0 || minNota==1){
										int promedioAsistencia = 0;
										promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
											if(promedioAsistencia == 1 || promedioAsistencia == 0){
												BigDecimal itemCost  = BigDecimal.ZERO;
												itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
												item.setClfParamRecuperacion1(itemCost);
												item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
												item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
											}else{
												BigDecimal itemCost  = BigDecimal.ZERO;
												itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
												item.setClfParamRecuperacion1(itemCost);
												item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
												item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
											}
									}else{
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								}
								
							}
						} catch (Exception e) {
						}
						
						try {
							verificarGuardar = servCalificacionServicio.guardarEdicionNotasSegundoHemi(rcesAux, item, eafRegCliente);
						} catch (CalificacionValidacionException e) {
						} catch (CalificacionException e) {
						} catch (Exception e) {
						}
					}
					eafEtapaProceso = 0;
					
				} catch (RecordEstudianteDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (RecordEstudianteDtoNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
			}
//			
			if(verificarGuardar){
				nrctfValidadorClic = 2;
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.verificarGuardar.exitoso")));
				
				
				return null;
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.verificarGuardar.error")));
				return null;
			}
		}
		
		/**
		 * Metodo que guarda de forma definitiva los registros editados de la lista de paralelos
		 * @return XHTML listar paralelos
		 */
		public String guardar(){
//			try {
//				if(eafAsistenciaDocente!=null){
//					for (EstudianteJdbcDto item : eafListEstudianteBusq) {
//						item.setClfAsistenciaDocente1(eafAsistenciaDocente);
//						RecordEstudianteDto rcesAux = new RecordEstudianteDto();
//						rcesAux = servRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
////						if(item.getClfNota1()!=null||item.getClfAsistenciaEstudiante1()!=null){
////							RecordEstudianteDto rcesAux = new RecordEstudianteDto();
////							rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
////							servNpfCalificacionServicio.guardarNotasPregradoPrimerHemi(rcesAux, item , npfRegCliente);
////							npfValidadorClic=0;
////						}
//						if(item.getClfNota1()==null ^ item.getClfAsistenciaEstudiante1()==null){
//							FacesUtil.limpiarMensaje();
//							FacesUtil.mensajeError("EL registro por estudiante debe tener la nota y asistencia del primer hemisemestre");
//							npfValidadorClic=0;
//							return null;
//						}
//						
//						servCalificacionServicio.guardarNotasPregradoPrimerHemi(rcesAux, item, eafRegCliente);
//						npfValidadorClic=0;
//						
//					}
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeInfo("Ingreso de notas exitoso");
//					
//					
//					//******************************************************************************
//					//************************* ACA INICIA EL ENVIO DE MAIL CON ADJUNTO ************
//					//******************************************************************************
//					
//					try{
//						Connection connection = null;
//						Session session = null;
//						MessageProducer producer = null;
//						ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",GeneralesConstantes.APP_NIO_ACTIVEMQ);
//						connection = connectionFactory.createConnection();
//						connection.start();
//						session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//						Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
//						// Creamos un productor
//						producer = session.createProducer(destino);
//						
//						JasperReport jasperReport = null;
//						JasperPrint jasperPrint;
//						StringBuilder pathGeneralReportes = new StringBuilder();
//						pathGeneralReportes.append(FacesContext.getCurrentInstance()
//								.getExternalContext().getRealPath("/"));
//						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
//						jasperReport = (JasperReport) JRLoader.loadObject(new File(
//								(pathGeneralReportes.toString() + "/reporteNota1Hemi.jasper")));
//						List<Map<String, Object>> frmAdjuntoCampos = null;
//						Map<String, Object> frmAdjuntoParametros = null;
////						String facultadMail = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
////						String carreraMail = GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
//						
//						
//						
//						frmAdjuntoParametros = new HashMap<String, Object>();
//						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
//						String fecha = formato.format(new Date());
//						frmAdjuntoParametros.put("fecha",fecha);
//						frmAdjuntoParametros.put("docente", nrctfDocente.getPrsNombres()+" "+nrctfDocente.getPrsPrimerApellido()+" "+npfDocente.getPrsSegundoApellido());
//						frmAdjuntoParametros.put("nick", eafUsuario.getUsrNick());
//						StringBuilder sbAsistenciaDocente = new StringBuilder();
//						for (EstudianteJdbcDto item : npfListEstudianteBusq) {
//							npfNomCarrera =  item.getCrrDescripcion().toString();
//							npfNomMateria = item.getMtrDescripcion().toString();
//							npfNomParalelo = item.getPrlDescripcion().toString();;
//							frmAdjuntoParametros.put("periodo", item.getPracDescripcion());
//							frmAdjuntoParametros.put("facultad", item.getDpnDescripcion());
//							frmAdjuntoParametros.put("carrera", item.getCrrDetalle());
//							frmAdjuntoParametros.put("curso", item.getNvlDescripcion());
//							frmAdjuntoParametros.put("paralelo", item.getPrlDescripcion());
//							frmAdjuntoParametros.put("materia", item.getMtrDescripcion());
//							sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
//							frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());	
//							break;
//						}
//						frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
//						frmAdjuntoParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
//						
//						frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
//						Map<String, Object> datoEstudiantes = null;
//						
//						int cont = 1;
//						for (EstudianteJdbcDto item : npfListEstudianteBusq) {
//							StringBuilder sbNota1 = new StringBuilder();
//							StringBuilder sbAsistencia1 = new StringBuilder();
//							StringBuilder sbContador = new StringBuilder();
//							datoEstudiantes = new HashMap<String, Object>();
//							datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
//							datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//							datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
//							datoEstudiantes.put("nombres", item.getPrsNombres());
//							if(item.getClfNota1() != null){
//								sbNota1.append(item.getClfNota1());
//								datoEstudiantes.put("nota1", sbNota1.toString());
//							}else{
//								sbNota1.append("-");
//								datoEstudiantes.put("nota1", sbNota1.toString());
//							}
//							if(item.getClfAsistenciaEstudiante1() != null){
//								sbAsistencia1.append(item.getClfAsistenciaEstudiante1());
//								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//							}else{
//								sbAsistencia1.append("-");
//								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//							}
//							sbContador.append(cont);
//							datoEstudiantes.put("numero", sbContador.toString());
//							
//							frmAdjuntoCampos.add(datoEstudiantes);
//							cont = cont +1;
//						}
//						
//						JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);
//						
//						
//						jasperPrint = JasperFillManager.fillReport(jasperReport,
//							frmAdjuntoParametros, dataSource);
//						
//						
//						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
//		//				AdjuntoDto adjuntoDto = new AdjuntoDto();
//		//				adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
//		//				adjuntoDto.setAdjunto(arreglo);
//						
//						//lista de correos a los que se enviara el mail
//						List<String> correosTo = new ArrayList<>();
//						correosTo.add(npfDocente.getPrsMailInstitucional());
//						//path de la plantilla del mail
//						ProductorMailJson pmail = null;
//						StringBuilder sbCorreo= new StringBuilder();
//						sbCorreo= GeneralesUtilidades.generarAsunto(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
//								npfDocente.getPrsPrimerApellido()+" "+npfDocente.getPrsSegundoApellido()+" "+npfDocente.getPrsNombres() , npfNomCarrera , npfNomMateria , npfNomParalelo);
//						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_REGISTRO_NOTAS,
//								sbCorreo.toString()
//								  , "admin", "dt1c201s", true, arreglo, "RegistroNotas."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
//						String jsonSt = pmail.generarMail();
//						Gson json = new Gson();
//						MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
//					// 	Iniciamos el envío de mensajes
//						ObjectMessage message = session.createObjectMessage(mailDto);
//						producer.send(message);
//				}  catch (JMSException e) {
//				} catch (JRException e) {
//				} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
//				}
//					
//					//******************************************************************************
//					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//					//******************************************************************************
//				}else{
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
//					return null;
//				}
//			} catch (RecordEstudianteDtoException e) {
//				 FacesUtil.mensajeError(e.getMessage());
//				 
//			} catch (RecordEstudianteDtoNoEncontradoException e) {
//				FacesUtil.mensajeError(e.getMessage());
//				 
//			}
			return "irCancelar";
		}	
	
	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista de paralelos
	 * @return XHTML listar paralelos
	 */
	public String guardarRecuperacion(){
		try {
			for (EstudianteJdbcDto item : eafListEstudianteBusq) {
				RecordEstudianteDto rcesAux = new RecordEstudianteDto();
				rcesAux = servNrctfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
				if(item.getClfSupletorio()!=null){
					
					BigDecimal parametro2Aux  = BigDecimal.ZERO;
					parametro2Aux  = item.getClfSupletorio().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).divide(new BigDecimal(CalificacionConstantes.NOTA_MAX_PARCIAL_VALUE), 2, RoundingMode.DOWN);
					item.setClfParamRecuperacion2(parametro2Aux);
						
					BigDecimal sumaParametros = BigDecimal.ZERO;
					sumaParametros = item.getClfParamRecuperacion1().setScale(2, RoundingMode.DOWN).add(item.getClfParamRecuperacion2().setScale(2, RoundingMode.DOWN));
					item.setClfNotalFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP));
					
					int estadoRces = item.getClfNotalFinalSemestre().compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
					if(estadoRces == 1 || estadoRces ==0){
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
					}else{
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					}
					servCalificacionServicio.guardarNotasRectificacionRecuperacion(rcesAux, item , eafRegCliente);
					nrctfValidadorClic=0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Ingreso de notas exitoso");
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Debe ingresar la nota de recuperación de todos los estudiantes");
					return null;
				}
			}

		
		} catch (RecordEstudianteDtoException e) {
			 FacesUtil.mensajeError(e.getMessage());
			 
		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		}
		return "irListarParalelos";
	}	
	
	public boolean activacion(){
		boolean retorno = false;
		
		for (EstudianteJdbcDto item : eafListEstudianteBusq) {
			int aux = item.getRcesIngersoNota2();
			if(aux == 2){
				retorno = true;
				break;
			}
			
		}
		return retorno;
	}
		
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	
	
	
	
	public Boolean validadorAsistencia(Cell celdaActual,Integer asistenciaDocente)  {
		Boolean retorno = false;
		try{
			Double d = new Double(celdaActual.getNumericCellValue());
			int valor =  d.intValue();
			if(valor>asistenciaDocente){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La asistencia no puede ser mayor a la asistencia del docente : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
				return false;
			}
			if(valor<0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La asistencia no puede ser un valor negativo : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
				return false;
			}
		}catch(Exception e){
			if(celdaActual.getStringCellValue().equals("--")){
				return true;
			}else{
				try{
					Double d = new Double(celdaActual.getStringCellValue());
					int valor =  d.intValue();
					if(valor>asistenciaDocente){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("La asistencia no puede ser mayor a la asistencia del docente : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
					if(valor<0){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("La asistencia no puede ser un valor negativo : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
				}catch(Exception e1){
					if(celdaActual.getStringCellValue().equals("--")){
						return true;
					}else{
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Solo números enteros de la asistencia : Columna "
														+ (GeneralesUtilidades
																.obtenerLetraColumna(celdaActual.getColumnIndex()))
														+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
				}
			}
		}
		retorno = true;
		return retorno;
	}
	
	public Boolean validadorIdiomas(Cell celdaActual)  {
		Boolean retorno = false;
		try{
			Double d = new Double(celdaActual.getNumericCellValue());
			int valor =  d.intValue();
			if((d-valor)!=0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La nota debe ser registrada en números enteros : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
				return false;
			}
			if(valor>20){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La nota no puede ser mayor 20 puntos : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
				return false;
			}
			if(valor<0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La nota no puede ser un valor negativo : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
				return false;
			}
		}catch(Exception e){
			if(celdaActual.getStringCellValue().equals("--")){
				return true;
			}else{
				try{
					Double d = new Double(celdaActual.getStringCellValue());
					
					int valor =  d.intValue();
					if((d-valor)!=0){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("La nota debe ser registrada en números enteros : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
					if(valor>20){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("La nota no puede ser mayor a 20 puntos : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
					if(valor<0){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("La nota no puede ser un valor negativo : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
				}catch(Exception e1){
					if(celdaActual.getStringCellValue().equals("--")){
						return true;
					}else{
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Solo números enteros son permitidos : Columna "
														+ (GeneralesUtilidades
																.obtenerLetraColumna(celdaActual.getColumnIndex()))
														+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					}
				}
			}
		}
		retorno = true;
		return retorno;
	}
	
	public Boolean validador(Cell celdaActual)  {
		Boolean retorno = false;
		
		try {
			String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(String.valueOf(celdaActual.getNumericCellValue()));
			valorSt=valorSt.replace(",", ".");
		     Float.parseFloat(valorSt);
		     int puntoDecimalUbc = valorSt.indexOf('.');
		     if(puntoDecimalUbc==0){
		    	FacesUtil.limpiarMensaje();
		    	FacesUtil.mensajeError("No se permiten números sin parte entera : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
				return false;
		     }
		     int totalDecimales = valorSt.length() - puntoDecimalUbc - 1;
		     if(puntoDecimalUbc==-1){
		    	 if(Float.parseFloat(valorSt)>20){
		    		 FacesUtil.limpiarMensaje();
		    		 FacesUtil.mensajeError("Calificación máximo sobre 20 puntos : Columna "
								+ (GeneralesUtilidades
										.obtenerLetraColumna(celdaActual.getColumnIndex()))
								+ " fila: " + (celdaActual.getRowIndex() + 1));
		    		 return false;
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("No se permiten números negativos : Columna "
								+ (GeneralesUtilidades
										.obtenerLetraColumna(celdaActual.getColumnIndex()))
								+ " fila: " + (celdaActual.getRowIndex() + 1));
					 return false;
				 } 
		     }else{
		    	 if(totalDecimales>2){
		    		 FacesUtil.limpiarMensaje();
		    		 FacesUtil.mensajeError("Sólo permite máximo 2 números decimales : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
		    		 return false;
				 }else if(Float.parseFloat(valorSt)>20){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("Calificación máximo sobre 20 puntos : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
					 return false;
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("No se permiten números negativos : Columna "
											+ (GeneralesUtilidades
													.obtenerLetraColumna(celdaActual.getColumnIndex()))
											+ " fila: " + (celdaActual.getRowIndex() + 1));
					 return false;
				 } 
		     }
		}catch (Exception ex) {
			if(celdaActual.getStringCellValue().equals("--")){
				return true;
			}else{
				try {
					String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(celdaActual.getStringCellValue());
					valorSt=valorSt.replace(",", ".");
				     Float.parseFloat(valorSt);
				     int puntoDecimalUbc = valorSt.indexOf('.');
				     if(puntoDecimalUbc==0){
				    	FacesUtil.limpiarMensaje();
				    	FacesUtil.mensajeError("No se permiten números sin parte entera : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
				     }
				     int totalDecimales = valorSt.length() - puntoDecimalUbc - 1;
				     if(puntoDecimalUbc==-1){
				    	 if(Float.parseFloat(valorSt)>20){
				    		 FacesUtil.limpiarMensaje();
				    		 FacesUtil.mensajeError("Calificación máximo sobre 20 puntos : Columna "
										+ (GeneralesUtilidades
												.obtenerLetraColumna(celdaActual.getColumnIndex()))
										+ " fila: " + (celdaActual.getRowIndex() + 1));
				    		 return false;
						 }else if(Float.parseFloat(valorSt)<0){
							 FacesUtil.limpiarMensaje();
							 FacesUtil.mensajeError("No se permiten números negativos : Columna "
										+ (GeneralesUtilidades
												.obtenerLetraColumna(celdaActual.getColumnIndex()))
										+ " fila: " + (celdaActual.getRowIndex() + 1));
							 return false;
						 } 
				     }else{
				    	 if(totalDecimales>2){
				    		 FacesUtil.limpiarMensaje();
				    		 FacesUtil.mensajeError("Sólo permite máximo 2 números decimales : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
				    		 return false;
						 }else if(Float.parseFloat(valorSt)>20){
							 FacesUtil.limpiarMensaje();
							 FacesUtil.mensajeError("Calificación máximo sobre 20 puntos : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
							 return false;
						 }else if(Float.parseFloat(valorSt)<0){
							 FacesUtil.limpiarMensaje();
							 FacesUtil.mensajeError("No se permiten números negativos : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
							 return false;
						 } 
				     }
				}
				catch (Exception ex1) {
					if(celdaActual.getStringCellValue().equals("--")){
						return true;
					}else{
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Sólo números y signos decimales : Columna "
													+ (GeneralesUtilidades
															.obtenerLetraColumna(celdaActual.getColumnIndex()))
													+ " fila: " + (celdaActual.getRowIndex() + 1));
						return false;
					 }
				}
			 }
		}
		retorno  = true;
		return retorno;
	}
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		eafEtapaProceso=0;
		eafEstudianteBuscar = null;
//		nrctfListCarreraBusq = null;
		nrctfListNivelBusq = null;
		nrctfListMateriaBusq= null;
		nrctfListNivelxCarreraDocenteBusq = null;
		nrctfListMateriasxCarreraDocenteBusq = null;
		eafActivadorReporte=0;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		eafEstudianteBuscar = new EstudianteJdbcDto();
//		//INICIALIZO EL PERIODO ACADEMICO ID
		eafEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO LA CARRERA ID
		eafEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO EL NIVEL ID
		eafEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO EL PARALELO ID
//		nrctfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO LA MATERIA ID
		eafEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
//		//ANULO LA LISTA DE NIVEL
//		nrctfListNivelBusq = null;
		//ANULO LA LISTA DE PARALELOS
		eafListParaleloBusq = null;
//		//ANULO LA LISTA DE MATERIAS
//		nrctfListMateriaBusq = null;
		eafActivar = false;
		activador= false;
	}
	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada(){
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getEafUsuario() {
		return eafUsuario;
	}
	public void setEafUsuario(Usuario eafUsuario) {
		this.eafUsuario = eafUsuario;
	}
	public DocenteJdbcDto getNrctfDocente() {
		return nrctfDocente;
	}
	public void setNrctfDocente(DocenteJdbcDto nrctfDocente) {
		this.nrctfDocente = nrctfDocente;
	}
	public EstudianteJdbcDto getEafEstudianteBuscar() {
		return eafEstudianteBuscar;
	}
	public void setEafEstudianteBuscar(EstudianteJdbcDto eafEstudianteBuscar) {
		this.eafEstudianteBuscar = eafEstudianteBuscar;
	}
	public List<CarreraDto> getNrctfListCarreraBusq() {
		nrctfListCarreraBusq = nrctfListCarreraBusq==null?(new ArrayList<CarreraDto>()):nrctfListCarreraBusq;
		return nrctfListCarreraBusq;
	}
	public void setNrctfListCarreraBusq(List<CarreraDto> nrctfListCarreraBusq) {
		this.nrctfListCarreraBusq = nrctfListCarreraBusq;
	}
	public List<NivelDto> getNrctfListNivelBusq() {
		nrctfListNivelBusq = nrctfListNivelBusq==null?(new ArrayList<NivelDto>()):nrctfListNivelBusq;
		return nrctfListNivelBusq;
	}
	public void setNrctfListNivelBusq(List<NivelDto> nrctfListNivelBusq) {
		this.nrctfListNivelBusq = nrctfListNivelBusq;
	}
	public List<MateriaDto> getNrctfListMateriaBusq() {
		nrctfListMateriaBusq = nrctfListMateriaBusq==null?(new ArrayList<MateriaDto>()):nrctfListMateriaBusq;
		return nrctfListMateriaBusq;
	}
	public void setNrctfListMateriaBusq(List<MateriaDto> nrctfListMateriaBusq) {
		this.nrctfListMateriaBusq = nrctfListMateriaBusq;
	}
	public List<EstudianteJdbcDto> getEafListEstudianteBusq() {
		eafListEstudianteBusq = eafListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):eafListEstudianteBusq;
		return eafListEstudianteBusq;
	}
	public void setEafListEstudianteBusq(List<EstudianteJdbcDto> eafListEstudianteBusq) {
		this.eafListEstudianteBusq = eafListEstudianteBusq;
	}
	public List<ParaleloDto> getEafListParaleloBusq() {
		eafListParaleloBusq = eafListParaleloBusq==null?(new ArrayList<ParaleloDto>()):eafListParaleloBusq;
		return eafListParaleloBusq;
	}
	public void setEafListParaleloBusq(List<ParaleloDto> eafListParaleloBusq) {
		this.eafListParaleloBusq = eafListParaleloBusq;
	}

	public ParaleloDto getEafParaleloDtoEditar() {
		return eafParaleloDtoEditar;
	}

	public void setEafParaleloDtoEditar(ParaleloDto eafParaleloDtoEditar) {
		nrctfListMateriaBusq = nrctfListMateriaBusq==null?(new ArrayList<MateriaDto>()):nrctfListMateriaBusq;
		this.eafParaleloDtoEditar = eafParaleloDtoEditar;
	}

	public Integer getEafAsistenciaDocente() {
		return eafAsistenciaDocente;
	}


	public void setEafAsistenciaDocente(Integer eafAsistenciaDocente) {
		this.eafAsistenciaDocente = eafAsistenciaDocente;
	}


	public Integer getNrctfValidadorClic() {
		return nrctfValidadorClic;
	}


	public void setNrctfValidadorClic(Integer nrctfValidadorClic) {
		this.nrctfValidadorClic = nrctfValidadorClic;
	}

//	private void setIpAdd() {
//        try {
//            InetAddress thisIp = InetAddress.getLocalHost();
//            thisIpAddress = thisIp.getHostAddress().toString();
//            
//            thisIp = InetAddress.getLocalHost();
//
//    		NetworkInterface network = NetworkInterface.getByInetAddress(thisIp);
//
//    		byte[] mac = network.getHardwareAddress();
//
//    		System.out.print("Current MAC address : ");
//
//    		StringBuilder sb = new StringBuilder();
//    		for (int i = 0; i < mac.length; i++) {
//    			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//    		}
//        } catch (Exception e) {
//        }
//    }

//    protected String getIpAddress() {
//        setIpAdd();
//        return thisIpAddress;
//    }


	public String getEafRegCliente() {
		return eafRegCliente;
	}
	public void setEafRegCliente(String eafRegCliente) {
		this.eafRegCliente = eafRegCliente;
	}


	public String getNrctfEstado() {
		return nrctfEstado;
	}
	public void setNrctfEstado(String nrctfEstado) {
		this.nrctfEstado = nrctfEstado;
	}

	public String getNrctfNomCarrera() {
		return nrctfNomCarrera;
	}


	public void setNrctfNomCarrera(String nrctfNomCarrera) {
		this.nrctfNomCarrera = nrctfNomCarrera;
	}


	public String getNrctfNomMateria() {
		return nrctfNomMateria;
	}


	public void setNrctfNomMateria(String nrctfNomMateria) {
		this.nrctfNomMateria = nrctfNomMateria;
	}


	public String getNrctfNomParalelo() {
		return nrctfNomParalelo;
	}


	public void setNrctfNomParalelo(String nrctfNomParalelo) {
		this.nrctfNomParalelo = nrctfNomParalelo;
	}


	public List<RolFlujoCarrera> getNrctfListRolFlujoCarreraBusq() {
		nrctfListRolFlujoCarreraBusq = nrctfListRolFlujoCarreraBusq==null?(new ArrayList<RolFlujoCarrera>()):nrctfListRolFlujoCarreraBusq;
		return nrctfListRolFlujoCarreraBusq;
	}


	public void setNrctfListRolFlujoCarreraBusq(List<RolFlujoCarrera> nrctfListRolFlujoCarreraBusq) {
		this.nrctfListRolFlujoCarreraBusq = nrctfListRolFlujoCarreraBusq;
	}


	public Dependencia getEafDependenciaBuscar() {
		return eafDependenciaBuscar;
	}


	public void setEafDependenciaBuscar(Dependencia eafDependenciaBuscar) {
		this.eafDependenciaBuscar = eafDependenciaBuscar;
	}


	public Integer getNrctfTipoNota() {
		return nrctfTipoNota;
	}


	public void setNrctfTipoNota(Integer nrctfTipoNota) {
		this.nrctfTipoNota = nrctfTipoNota;
	}


	public String getNrctfNombreArchivoAuxiliar() {
		return nrctfNombreArchivoAuxiliar;
	}


	public void setNrctfNombreArchivoAuxiliar(String nrctfNombreArchivoAuxiliar) {
		this.nrctfNombreArchivoAuxiliar = nrctfNombreArchivoAuxiliar;
	}


	public String getNrctfNombreArchivoSubido() {
		return nrctfNombreArchivoSubido;
	}


	public void setNrctfNombreArchivoSubido(String nrctfNombreArchivoSubido) {
		this.nrctfNombreArchivoSubido = nrctfNombreArchivoSubido;
	}


	public CronogramaActividadJdbcDto getEafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico() {
		return eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico;
	}


	public void setEafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico(
			CronogramaActividadJdbcDto eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico) {
		this.eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = eafCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico;
	}


	public CronogramaActividadJdbcDto getEafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico() {
		return eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	}


	public void setEafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico(
			CronogramaActividadJdbcDto eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico) {
		this.eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = eafCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	}


	public Integer getNrctfTipoRectificacion() {
		return nrctfTipoRectificacion;
	}


	public void setNrctfTipoRectificacion(Integer nrctfTipoRectificacion) {
		this.nrctfTipoRectificacion = nrctfTipoRectificacion;
	}


	public List<EstudianteJdbcDto> getNrctfListEstudianteEditar() {
		nrctfListEstudianteEditar = nrctfListEstudianteEditar==null?(new ArrayList<EstudianteJdbcDto>()):nrctfListEstudianteEditar;
		return nrctfListEstudianteEditar;
	}


	public void setNrctfListEstudianteEditar(List<EstudianteJdbcDto> nrctfListEstudianteEditar) {
		this.nrctfListEstudianteEditar = nrctfListEstudianteEditar;
	}


	public PersonaDto getNrctfDirCarrera() {
		return nrctfDirCarrera;
	}


	public void setNrctfDirCarrera(PersonaDto nrctfDirCarrera) {
		this.nrctfDirCarrera = nrctfDirCarrera;
	}


	public String getNrctfNomNivel() {
		return nrctfNomNivel;
	}


	public void setNrctfNomNivel(String nrctfNomNivel) {
		this.nrctfNomNivel = nrctfNomNivel;
	}


	public String getNrctfNomNotaRectificacion() {
		return nrctfNomNotaRectificacion;
	}


	public void setNrctfNomNotaRectificacion(String nrctfNomNotaRectificacion) {
		this.nrctfNomNotaRectificacion = nrctfNomNotaRectificacion;
	}


	public List<DocenteJdbcDto> getNrctfListCarreraDocenteBusq() {
		nrctfListCarreraDocenteBusq = nrctfListCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):nrctfListCarreraDocenteBusq;
		return nrctfListCarreraDocenteBusq;
	}


	public void setNrctfListCarreraDocenteBusq(List<DocenteJdbcDto> nrctfListCarreraDocenteBusq) {
		this.nrctfListCarreraDocenteBusq = nrctfListCarreraDocenteBusq;
	}


	public List<DocenteJdbcDto> getNrctfListNivelxCarreraDocenteBusq() {
		nrctfListNivelxCarreraDocenteBusq = nrctfListNivelxCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):nrctfListNivelxCarreraDocenteBusq;
		return nrctfListNivelxCarreraDocenteBusq;
	}


	public void setNrctfListNivelxCarreraDocenteBusq(List<DocenteJdbcDto> nrctfListNivelxCarreraDocenteBusq) {
		this.nrctfListNivelxCarreraDocenteBusq = nrctfListNivelxCarreraDocenteBusq;
	}


	public List<DocenteJdbcDto> getNrctfListMateriasxCarreraDocenteBusq() {
		nrctfListMateriasxCarreraDocenteBusq = nrctfListMateriasxCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):nrctfListMateriasxCarreraDocenteBusq;
		return nrctfListMateriasxCarreraDocenteBusq;
	}


	public void setNrctfListMateriasxCarreraDocenteBusq(List<DocenteJdbcDto> nrctfListMateriasxCarreraDocenteBusq) {
		this.nrctfListMateriasxCarreraDocenteBusq = nrctfListMateriasxCarreraDocenteBusq;
	}


	public CronogramaActividadJdbcDto getNrctfFechaPracCierreFinPresentePeriodoAcademico() {
		return nrctfFechaPracCierreFinPresentePeriodoAcademico;
	}


	public void setNrctfFechaPracCierreFinPresentePeriodoAcademico(
			CronogramaActividadJdbcDto nrctfFechaPracCierreFinPresentePeriodoAcademico) {
		this.nrctfFechaPracCierreFinPresentePeriodoAcademico = nrctfFechaPracCierreFinPresentePeriodoAcademico;
	}


	public CronogramaActividadJdbcDto getNrctfFechaPracCierreInicioSigPeriodoAcademico() {
		return nrctfFechaPracCierreInicioSigPeriodoAcademico;
	}


	public void setNrctfFechaPracCierreInicioSigPeriodoAcademico(
			CronogramaActividadJdbcDto nrctfFechaPracCierreInicioSigPeriodoAcademico) {
		this.nrctfFechaPracCierreInicioSigPeriodoAcademico = nrctfFechaPracCierreInicioSigPeriodoAcademico;
	}

	public List<PeriodoAcademico> getNrctfListPeriodoAcademico() {
		nrctfListPeriodoAcademico = nrctfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):nrctfListPeriodoAcademico;
		return nrctfListPeriodoAcademico;
	}
	public PeriodoAcademicoDto getNrctfPeriodoAcademicoBusq() {
		return nrctfPeriodoAcademicoBusq;
	}
	public void setNrctfPeriodoAcademicoBusq(PeriodoAcademicoDto nrctfPeriodoAcademicoBusq) {
		this.nrctfPeriodoAcademicoBusq = nrctfPeriodoAcademicoBusq;
	}

	public void setNrctfListPeriodoAcademico(List<PeriodoAcademico> nrctfListPeriodoAcademico) {
		this.nrctfListPeriodoAcademico = nrctfListPeriodoAcademico;
	}

	public String getEafNombre() {
		return eafNombre;
	}

	public void setEafNombre(String eafNombre) {
		this.eafNombre = eafNombre;
	}

	public Integer getEafEtapaProceso() {
		return eafEtapaProceso;
	}

	public void setEafEtapaProceso(Integer eafEtapaProceso) {
		this.eafEtapaProceso = eafEtapaProceso;
	}

	public Integer getEafValidarArchivo() {
		return eafValidarArchivo;
	}

	public void setEafValidarArchivo(Integer eafValidarArchivo) {
		this.eafValidarArchivo = eafValidarArchivo;
	}

	public InputStream getEafArchivo() {
		return eafArchivo;
	}

	public void setEafArchivo(InputStream eafArchivo) {
		this.eafArchivo = eafArchivo;
	}

	public int getEafActivadorReporte() {
		return eafActivadorReporte;
	}

	public void setEafActivadorReporte(int eafActivadorReporte) {
		this.eafActivadorReporte = eafActivadorReporte;
	}

	public Boolean getEafActivar() {
		return eafActivar;
	}

	public void setEafActivar(Boolean eafActivar) {
		this.eafActivar = eafActivar;
	}



	public boolean isActivador() {
		return activador;
	}



	public void setActivador(boolean activador) {
		this.activador = activador;
	}



	public CronogramaActividadJdbcDto getEafCronogramaActividadJdbcDtoBuscar() {
		return eafCronogramaActividadJdbcDtoBuscar;
	}



	public void setEafCronogramaActividadJdbcDtoBuscar(CronogramaActividadJdbcDto eafCronogramaActividadJdbcDtoBuscar) {
		this.eafCronogramaActividadJdbcDtoBuscar = eafCronogramaActividadJdbcDtoBuscar;
	}
	
	
	
}

