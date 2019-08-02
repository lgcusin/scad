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
   
 ARCHIVO:     ReporteNotasDocenteForm.java	  
 DESCRIPCION: Bean de sesion que maneja la consulta de notas de los hemisemestres, recuperación y notas finales. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 31-MAYO-2019 		Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionDirectorCarrera;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstadisticasDocenteNotasDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReporteNotasDocenteForm.
 * Managed Bean que administra la consulta de notas de los hemisemestres, recuperación y notas finales
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="reporteDocenteNotasForm")
@SessionScoped
public class ReporteDocenteNotasForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario arndfUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto arndfDocente;

	//PARA BUSQUEDA
	private EstudianteJdbcDto arndfEstudianteBuscar;
	private List<CarreraDto> arndfListCarreraBusq;
	private List<NivelDto> arndfListNivelBusq;
	private List<MateriaDto> arndfListMateriaBusq;
	private List<EstudianteJdbcDto> arndfListEstudianteBusq;
	private List<EstudianteJdbcDto> arndfListEstudianteEditar;
	private List<ParaleloDto> arndfListParaleloBusq;
	private ParaleloDto arndfParaleloDtoEditar;
	private List<RolFlujoCarrera> arndfListRolFlujoCarreraBusq;
	private Dependencia arndfDependenciaBuscar;
	private List<DocenteJdbcDto> arndfListCarreraDocenteBusq;
	private List<DocenteJdbcDto> arndfListNivelxCarreraDocenteBusq;
	private List<DocenteJdbcDto> arndfListMateriasxCarreraDocenteBusq;
	private PeriodoAcademicoDto arndfPeriodoAcademicoBusq;
	private int rndActivarReporte;
	private Materia mtrModulo;
	private List<PeriodoAcademico> arndfListPeriodoAcademicoBusq;
	private Integer arndfPeriodoId;
	private Integer arndfCarreraId;
	private List<Carrera> rdfListaCarrerasDirector;
	private DocenteJdbcDto rdfDocenteBuscar;
	private EstadisticasDocenteNotasDto rndfEstadisticas;
	private List<EstadisticasDocenteNotasDto> listaEstadisticas;
	private String chartData;
	private String chartData2;
	private String chartDataAxisX;
	private Boolean activadorGrafico;
	private Boolean activadorGrafico2;
	private Boolean activadorDescargar;
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
	private CarreraDtoServicioJdbc servRndfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servRndfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servRndfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servRndfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servRndfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servRndfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servRndfParaleloDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servRndfNotasPregradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servRndfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servRndfMateriaDto;
	@EJB
	private CalificacionServicio servRndfCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servRndfRolFlujoCarreraServicio;
	@EJB
	private UsuarioRolServicio servRndfUsuarioRolServicio;
	@EJB
	private DependenciaServicio servRndfDependenciaServicio;
	@EJB
	private CronogramaActividadDtoServicioJdbc servRndfCronogramaActividadDtoServicioJdbcServicio;
	@EJB
	private PersonaDtoServicioJdbc servRndfPersonaDtoServicioJdbc;
	@EJB
	private MallaCurricularParaleloDtoServicioJdbc servRndfMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	private MallaCurricularParaleloServicio servRndfMallaCurricularParaleloServicio;
	@EJB
	private MallaCurricularMateriaServicio servRndfMallaCurricularMateriaServicio;
	@EJB
	private PeriodoAcademicoServicio servRndfPeriodoAcademicoServicio;	
	@EJB
	private CarreraServicio servRndfCarreraServicio;
	@EJB
	private PersonaServicio servRndfPersonaServicio;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	
	
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irReportePasoNotas(Usuario usuario){
		rndActivarReporte = 0;
		arndfUsuario = usuario;
		String retorno = null;
		try {
			/*********************************************************************/
			/*********** CON PANEL DE BÚSQUEDA ***********************************/
			/*********************************************************************/
			//INICIO PARAMETROS DE BUSQUEDA
			UsuarioRol usro = null;
			try {
				usro = servRndfUsuarioRolServicio.buscarXUsuarioXrolActivo(arndfUsuario.getUsrId(), RolConstantes.ROL_DECANO_VALUE);	
			} catch (Exception e) {
				try {
					usro = servRndfUsuarioRolServicio.buscarXUsuarioXrolActivo(arndfUsuario.getUsrId(), RolConstantes.ROL_SUBDECANO_VALUE);	
				} catch (Exception e2) {
					try {
						usro = servRndfUsuarioRolServicio.buscarXUsuarioXrolActivo(arndfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE);	
					} catch (Exception e3) {
						try {
							usro = servRndfUsuarioRolServicio.buscarXUsuarioXrolActivo(arndfUsuario.getUsrId(), RolConstantes.ROL_SECRENIVELACION_VALUE);
						} catch (Exception e4) {
							throw new UsuarioRolNoEncontradoException();
						}
						
					}
				}
				
			}
			
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			arndfListRolFlujoCarreraBusq = servRndfRolFlujoCarreraServicio.buscarXUsuarioXRoles(arndfUsuario.getUsrId(), new Integer[]{RolConstantes.ROL_DIRCARRERA_VALUE,RolConstantes.ROL_DECANO_VALUE,RolConstantes.ROL_SUBDECANO_VALUE });
			
			iniciarParametros();
			for (RolFlujoCarrera item : arndfListRolFlujoCarreraBusq) {
				try {
					rdfListaCarrerasDirector.add(servRndfCarreraServicio.buscarPorId(item.getRoflcrCarrera().getCrrId()));	
				} catch (Exception e) {
				}
			}
			
			 List<Carrera> newList = rdfListaCarrerasDirector.stream() 
                     .distinct() 
                     .collect(Collectors.toList()); 
			 rdfListaCarrerasDirector = newList;
			 
			 Iterator<Carrera> it = rdfListaCarrerasDirector.iterator();
				while(it.hasNext()){
					Carrera crr = it.next();
					Integer contador=0;
					for (Carrera item : rdfListaCarrerasDirector) {
						if(crr.getCrrId()==item.getCrrId()){
							contador++;
						}
					}
					if(contador>1){
						it.remove();
					}
				}
			 
			try {
				List<String> estados = new ArrayList<>();
				estados.add(String.valueOf(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE));
				estados.add(String.valueOf(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE));
				arndfListPeriodoAcademicoBusq = servRndfPeriodoAcademicoServicio.buscarPeriodos(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, estados);
				arndfListPeriodoAcademicoBusq.add(servRndfPeriodoAcademicoServicio.buscarPorId(350));
			} catch (Exception e) {
			}
			retorno = "irReporteNotasDocenteDirector";
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
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
		arndfCarreraId = GeneralesConstantes.APP_ID_BASE;
		arndfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		return "irInicio";
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		arndfListParaleloBusq = null;
		try {
			if(rdfDocenteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscar(rdfDocenteBuscar.getPracId());
				
				arndfListParaleloBusq = new ArrayList<>();
				
				
				for (Carrera crrBuscar : rdfListaCarrerasDirector) {
					List<ParaleloDto> listNoCompartida = new ArrayList<>();
					listNoCompartida = servRndfParaleloDtoServicioJdbc.listarParalelosTotalesXcarreraXPeriodoNoComp(
							crrBuscar.getCrrId(), rdfDocenteBuscar.getPrsIdentificacion(), rdfDocenteBuscar.getPracId());
//					// asignación a una sola lista
					for (ParaleloDto item : listNoCompartida) {
						arndfListParaleloBusq.add(item);
					}
				}
				Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
					public int compare(ParaleloDto obj1, ParaleloDto obj2) {
						return new String(obj1.getCrrDescripcion()).compareTo(new String(obj2.getCrrDescripcion()));
					}
				});
			}else if(rdfDocenteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else{
				arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscar(rdfDocenteBuscar.getPracId());
				List<ParaleloDto> listNoCompartida = new ArrayList<>();
				arndfListParaleloBusq = new ArrayList<>();
				listNoCompartida = servRndfParaleloDtoServicioJdbc.listarParalelosTotalesXcarreraXPeriodoNoComp(
						rdfDocenteBuscar.getCrrId(), rdfDocenteBuscar.getPrsIdentificacion(), rdfDocenteBuscar.getPracId());
//				// asignación a una sola lista
				for (ParaleloDto item : listNoCompartida) {
					arndfListParaleloBusq.add(item);
				}
//				Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
//					public int compare(ParaleloDto obj1, ParaleloDto obj2) {
//						return new String(obj1.getMtrDescripcion()).compareTo(new String(obj2.getMtrDescripcion()));
//					}
//				});
			}
			generarReporte();
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.buscar.exception")));
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.buscar.no.encontrado.exception")));
		} catch (PeriodoAcademicoDtoJdbcException e) {
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
		}
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarParalelos(){
		try {
			//BUSCO EL DOCENTE PARA LAS MATERIAS
			arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPracAnterior(arndfUsuario.getUsrId(), arndfCarreraId, TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoId);
			arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscar(arndfPeriodoId);
			List<ParaleloDto> listCompartida = new ArrayList<>();
			listCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartidaAnterior(arndfCarreraId, -99, arndfDocente.getDtpsId(), -99);

			//busco las no compartidas
			List<ParaleloDto> listNoCompartida = new ArrayList<>();
			listNoCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoCompAnterior(arndfCarreraId, -99, arndfDocente.getDtpsId(), -99);

		//asignación a una sola lista
		arndfListParaleloBusq = new ArrayList<>();
		for (ParaleloDto item : listCompartida) {
			arndfListParaleloBusq.add(item);
		}
		for (ParaleloDto item : listNoCompartida) {
			arndfListParaleloBusq.add(item);
		}
		
		Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
			public int compare(ParaleloDto obj1, ParaleloDto obj2) {
				return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
			}
		});
		Iterator<ParaleloDto> it = arndfListParaleloBusq.iterator();
		while(it.hasNext()){
			ParaleloDto aux = it.next();
			for (ParaleloDto item : arndfListParaleloBusq) {
				Materia mtrAux;
				try {
					mtrAux = servRndfMateriaDto.buscarPorId(item.getMtrId());
					
					if(aux.getMtrId()==mtrAux.getMtrMateria().getMtrId()){
						it.remove();
						break;
					}
				} catch (Exception e) {
				}
			}	
		}
		generarReporte();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String irListar(){
		return "irListarEstadisticaDocente";
	}
	
	public String generarReporte(){
		try {
			listaEstadisticas = new ArrayList<EstadisticasDocenteNotasDto>();
			for (ParaleloDto itemParalelo : arndfListParaleloBusq) {
				arndfParaleloDtoEditar = itemParalelo;
				rndfEstadisticas = new EstadisticasDocenteNotasDto();
				Integer numEstudiantesMatriculados=0;
				Integer numEstudiantesInscritos=0;
				Integer numEstudianteConNota1=0;
				Integer numEstudiantesSinNota1=0;
				BigDecimal desviacionEstandar1=BigDecimal.ZERO;
				BigDecimal media1=BigDecimal.ZERO;
				BigDecimal coeficiente1=BigDecimal.ZERO;
				BigDecimal coeficiente2=BigDecimal.ZERO;
				BigDecimal sumatoriaNotas1=BigDecimal.ZERO;
				Integer numEstudianteConNota2=0;
				Integer numEstudiantesSinNota2=0;
				BigDecimal desviacionEstandar2=BigDecimal.ZERO;
				BigDecimal media2=BigDecimal.ZERO;
				BigDecimal sumatoriaNotas2=BigDecimal.ZERO;
				Integer totalNotas1=0;
				Integer totalNotas2=0;
				List<EstudianteJdbcDto> listaNotas = null;
				
				listaNotas = new ArrayList<EstudianteJdbcDto>();
				try {
					listaNotas	= servRndfNotasPregradoDtoServicioJdbc.buscarEstudiantesEstadisticasPasoNotas(arndfParaleloDtoEditar.getMlcrprId());
				} catch (EstudianteDtoJdbcException e) {
				} catch (EstudianteDtoJdbcNoEncontradoException e) {
				}
					List<Double> lista1 = new ArrayList<Double>();
					List<Double> lista2 = new ArrayList<Double>();
					for (EstudianteJdbcDto item : listaNotas) {
						if(item.getRcesEstado()==RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE){
							numEstudiantesInscritos++;
						}else{
							numEstudiantesMatriculados++;
							if(item.getClfId()==GeneralesConstantes.APP_ID_BASE){
								numEstudiantesSinNota1++;
							}else{
								if(!(item.getClfNota1()!=null)){
									numEstudiantesSinNota1++;
								}else{
									numEstudianteConNota1++;
									sumatoriaNotas1=sumatoriaNotas1.add(item.getClfNota1());
									totalNotas1++;
									lista1.add(item.getClfNota1().doubleValue());
								}
								if(!(item.getClfNota2()!=null)){
									numEstudiantesSinNota2++;
								}else{
									numEstudianteConNota2++;
									sumatoriaNotas2=sumatoriaNotas2.add(item.getClfNota2());
									totalNotas2++;
									lista2.add(item.getClfNota2().doubleValue());
								}
							}	
						}
					}
					try {
						media1=sumatoriaNotas1.divide(new BigDecimal(totalNotas1),2,RoundingMode.HALF_UP);
						desviacionEstandar1 = new BigDecimal(calcularSD(lista1)).setScale(2, RoundingMode.HALF_UP);
						coeficiente1=desviacionEstandar1.divide(media1.abs(),2,RoundingMode.HALF_UP);
					} catch (Exception e) {
					}
					try {
						media2=sumatoriaNotas2.divide(new BigDecimal(totalNotas2),2,RoundingMode.HALF_UP);
						desviacionEstandar2 = new BigDecimal(calcularSD(lista2)).setScale(2, RoundingMode.HALF_UP);
						coeficiente2=desviacionEstandar2.divide(media2.abs(),2,RoundingMode.HALF_UP);
					} catch (Exception e) {
					}
					rndfEstadisticas.setDesviacionEstandar1(desviacionEstandar1);
					rndfEstadisticas.setDesviacionEstandar2(desviacionEstandar2);
					rndfEstadisticas.setMedia1(media1);
					rndfEstadisticas.setMedia2(media2);
					rndfEstadisticas.setCoeficiente1(coeficiente1);
					rndfEstadisticas.setCoeficiente2(coeficiente2);
					rndfEstadisticas.setNumEstudianteConNota1(numEstudianteConNota1);
					rndfEstadisticas.setNumEstudianteConNota2(numEstudianteConNota2);
					rndfEstadisticas.setNumEstudiantesSinNota1(numEstudiantesSinNota1);
					rndfEstadisticas.setNumEstudiantesSinNota2(numEstudiantesSinNota2);
					rndfEstadisticas.setNumEstudiantesMatriculados(numEstudiantesMatriculados);
					rndfEstadisticas.setNumEstudiantesInscritos(numEstudiantesInscritos);
					rndfEstadisticas.setPrlCodigo(arndfParaleloDtoEditar.getPrlCodigo());
					rndfEstadisticas.setMtrDescripcion(arndfParaleloDtoEditar.getMtrDescripcion());
					rndfEstadisticas.setPrsNombres(arndfParaleloDtoEditar.getPrsNombres());
					rndfEstadisticas.setPrsPrimerApellido(arndfParaleloDtoEditar.getPrsPrimerAPellido());
					rndfEstadisticas.setPrsSegundoApellido(arndfParaleloDtoEditar.getPrsSegundoApellido());
					rndfEstadisticas.setCrrDescripcion(arndfParaleloDtoEditar.getCrrDescripcion());
					rndfEstadisticas.setDpnDescripcion(arndfParaleloDtoEditar.getDpnDescripcion());
					rndfEstadisticas.setMtrDescripcion(arndfParaleloDtoEditar.getMtrDescripcion());
					rndfEstadisticas.setPracDescripcion(arndfParaleloDtoEditar.getPracDescripcion());
					listaEstadisticas.add(rndfEstadisticas);
			}
			
			generarReporteFacultad(listaEstadisticas);
			activadorDescargar=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return "irInicio";
	}
	
	
	/**
	* Genera el reporte de registrados y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return void
	*/
	public static void generarReporteFacultad(List<EstadisticasDocenteNotasDto> listado){
		try {
			 List<Map<String, Object>> frmCrpCampos = null;
			 Map<String, Object> frmCrpParametros = null;
			 String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "reporteEstadisticoNotas";
//			java.util.Date date= new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
//			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("periodo",listado.get(0).getPracDescripcion());
			frmCrpParametros.put("facultad",listado.get(0).getDpnDescripcion());
			
			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			Integer numero=1;
			for (EstadisticasDocenteNotasDto item : listado) {
				dato = new HashMap<String, Object>();
				dato.put("numero",numero.toString());
				dato.put("carrera", item.getCrrDescripcion());
				dato.put("asignatura", item.getMtrDescripcion());
				try {
					dato.put("apellidos", item.getPrsPrimerApellido().concat(" ").concat(item.getPrsSegundoApellido()).concat(" ").concat(item.getPrsNombres()));
				} catch (Exception e) {
					dato.put("apellidos", item.getPrsPrimerApellido().concat(" ").concat(item.getPrsNombres()));
				}
				
				dato.put("paralelo", item.getPrlCodigo());
				dato.put("inscritos", item.getNumEstudiantesInscritos().toString());
				dato.put("matriculados", item.getNumEstudiantesMatriculados().toString());
				dato.put("media1", item.getMedia1().toString());
				dato.put("desviacion1", item.getDesviacionEstandar1().toString());
				dato.put("coeficiente1", item.getCoeficiente1().toString());
				dato.put("media2", item.getMedia2().toString());
				dato.put("desviacion2", item.getDesviacionEstandar2().toString());
				dato.put("coeficiente2", item.getCoeficiente2().toString());
				numero++;
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	} 
	
	/**
	 * Metodo que llama los datos para la genracion del reporte
	 */
	public String reporteNotas(ParaleloDto paralelo){
		String retorno = null;
		arndfParaleloDtoEditar = paralelo;
		rndfEstadisticas = new EstadisticasDocenteNotasDto();
		Integer numEstudiantesMatriculados=0;
		Integer numEstudiantesInscritos=0;
		Integer numEstudianteConNota1=0;
		Integer numEstudiantesSinNota1=0;
		BigDecimal desviacionEstandar1=BigDecimal.ZERO;
		BigDecimal media1=BigDecimal.ZERO;
		BigDecimal sumatoriaNotas1=BigDecimal.ZERO;
		Integer numEstudianteConNota2=0;
		Integer numEstudiantesSinNota2=0;
		BigDecimal desviacionEstandar2=BigDecimal.ZERO;
		BigDecimal media2=BigDecimal.ZERO;
		BigDecimal sumatoriaNotas2=BigDecimal.ZERO;
		Integer totalNotas1=0;
		Integer totalNotas2=0;
		List<EstudianteJdbcDto> listaNotas = null;
		listaEstadisticas = new ArrayList<EstadisticasDocenteNotasDto>();
		listaNotas = new ArrayList<EstudianteJdbcDto>();
		try {
			listaNotas	= servRndfNotasPregradoDtoServicioJdbc.buscarEstudiantesEstadisticasPasoNotas(arndfParaleloDtoEditar.getMlcrprId());
		} catch (EstudianteDtoJdbcException e) {
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
		}
		if(listaNotas.size()==0){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen registros con los parámetros seleccionados.");
			retorno=null;
		}else{
			List<Double> lista1 = new ArrayList<Double>();
			List<Double> lista2 = new ArrayList<Double>();
			for (EstudianteJdbcDto item : listaNotas) {
				if(item.getRcesEstado()==RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE){
					numEstudiantesInscritos++;
				}else{
					numEstudiantesMatriculados++;
					if(item.getClfId()==GeneralesConstantes.APP_ID_BASE ){
						numEstudiantesSinNota1++;
					}else{
						if(!(item.getClfNota1()!=null)){
							numEstudiantesSinNota1++;
						}else{
							numEstudianteConNota1++;
							sumatoriaNotas1=sumatoriaNotas1.add(item.getClfNota1());
							totalNotas1++;
							lista1.add(item.getClfNota1().doubleValue());
						}
						if(!(item.getClfNota2()!=null)){
							numEstudiantesSinNota2++;
						}else{
							numEstudianteConNota2++;
							sumatoriaNotas2=sumatoriaNotas2.add(item.getClfNota2());
							totalNotas2++;
							lista2.add(item.getClfNota2().doubleValue());
						}
					}	
				}
			}
			try {
				media1=sumatoriaNotas1.divide(new BigDecimal(totalNotas1),2,RoundingMode.HALF_UP);
				desviacionEstandar1 = new BigDecimal(calcularSD(lista1)).setScale(2, RoundingMode.HALF_UP);
			} catch (Exception e) {
			}
			try {
				media2=sumatoriaNotas2.divide(new BigDecimal(totalNotas2),2,RoundingMode.HALF_UP);
				desviacionEstandar2 = new BigDecimal(calcularSD(lista2)).setScale(2, RoundingMode.HALF_UP);
			} catch (Exception e) {
			}
			rndfEstadisticas.setDesviacionEstandar1(desviacionEstandar1);
			rndfEstadisticas.setDesviacionEstandar2(desviacionEstandar2);
			rndfEstadisticas.setMedia1(media1);
			rndfEstadisticas.setMedia2(media2);
			rndfEstadisticas.setNumEstudianteConNota1(numEstudianteConNota1);
			rndfEstadisticas.setNumEstudianteConNota2(numEstudianteConNota2);
			rndfEstadisticas.setNumEstudiantesSinNota1(numEstudiantesSinNota1);
			rndfEstadisticas.setNumEstudiantesSinNota2(numEstudiantesSinNota2);
			rndfEstadisticas.setNumEstudiantesMatriculados(numEstudiantesMatriculados);
			rndfEstadisticas.setNumEstudiantesInscritos(numEstudiantesInscritos);
			rndfEstadisticas.setPrlCodigo(arndfParaleloDtoEditar.getPrlCodigo());
			rndfEstadisticas.setMtrDescripcion(arndfParaleloDtoEditar.getMtrDescripcion());
			listaEstadisticas.add(rndfEstadisticas);
			
			if(media1.floatValue()==0 || desviacionEstandar1.floatValue()==0){
				activadorGrafico=false;
			}else{
				activadorGrafico=true;
				List<Float> listaX = new ArrayList<Float>();
				List<Float> listaXFinal = new ArrayList<Float>();
				float contador = media1.floatValue();
				while (contador>0){
					listaX.add(contador);
					contador-=desviacionEstandar1.floatValue();
				}
				listaX.add(Float.valueOf(0));
				ListIterator<Float> listIterator = listaX.listIterator(listaX.size());
				while (listIterator.hasPrevious()) {
					listaXFinal.add(listIterator.previous());
				}
				contador = media1.floatValue();
				contador+=desviacionEstandar1.floatValue();
				while (contador<20){
					
					listaXFinal.add(contador);
					contador+=desviacionEstandar1.floatValue();
				}
				listaXFinal.add(Float.valueOf(20));
				StringBuilder stringBuilder = new StringBuilder();
				for (Float item : listaXFinal) {
					stringBuilder.append("[");
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					stringBuilder.append("'"+df.format(item)+"', ");
					stringBuilder.append(getY(item));
					stringBuilder.append("],");
				}
				chartData = stringBuilder.toString().substring(0, stringBuilder.toString().length()-1);
				
			}
			if(media2.floatValue()==0 || desviacionEstandar2.floatValue()==0){
				activadorGrafico2=false;
			}else{
				activadorGrafico2=true;
				List<Float> listaX = new ArrayList<Float>();
				List<Float> listaXFinal = new ArrayList<Float>();
				float contador = media2.floatValue();
				while (contador>0){
					listaX.add(contador);
					contador-=desviacionEstandar2.floatValue();
				}
				listaX.add(Float.valueOf(0));
				ListIterator<Float> listIterator = listaX.listIterator(listaX.size());
				while (listIterator.hasPrevious()) {
					listaXFinal.add(listIterator.previous());
				}
				contador = media2.floatValue();
				contador+=desviacionEstandar2.floatValue();
				while (contador<20){
					
					listaXFinal.add(contador);
					contador+=desviacionEstandar2.floatValue();
				}
				listaXFinal.add(Float.valueOf(20));
				StringBuilder stringBuilder = new StringBuilder();
				for (Float item : listaXFinal) {
					stringBuilder.append("[");
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					stringBuilder.append("'"+df.format(item)+"', ");
					stringBuilder.append(getY(item));
					stringBuilder.append("],");
				}
				chartData2 = stringBuilder.toString().substring(0, stringBuilder.toString().length()-1);
			}
			retorno="irVerEstadisticasDocente";
		}
		
		return retorno;
	}
	
	
	private double calcularSD(List<Double> numArray){
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }
	
    private double getY(double x) { 
    	 Double media = rndfEstadisticas.getMedia1().doubleValue();
    	 Double varianza = (rndfEstadisticas.getDesviacionEstandar1().multiply(rndfEstadisticas.getDesviacionEstandar1()).doubleValue());
    	 Double ds1 = rndfEstadisticas.getDesviacionEstandar1().doubleValue();
        return Math.pow(Math.exp(-(((x - media) * (x - media)) / ((2 * varianza)))), 1 / (ds1 * Math.sqrt(2 * Math.PI))); 
 
    } 
    
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		listaEstadisticas = new ArrayList<EstadisticasDocenteNotasDto>();
		rndfEstadisticas = new EstadisticasDocenteNotasDto();
		
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por carrera nivel y materia 
	 * @return Xhtml listar
	 */
	public String irCancelar(){
		arndfParaleloDtoEditar = null;
		arndfListEstudianteBusq = null;
		arndfListEstudianteEditar = null;
		return "irCancelar";
	}
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		arndfEstudianteBuscar = null;
		arndfListNivelBusq = null;
		arndfListMateriaBusq= null;
		arndfListMateriaBusq= null;
		arndfListNivelxCarreraDocenteBusq = null;
		arndfListMateriasxCarreraDocenteBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		arndfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO ID
		arndfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		arndfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL ID
		arndfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA MATERIA ID
		arndfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		//ANULO LA LISTA DE PARALELOS
		arndfListParaleloBusq = null;
		rdfListaCarrerasDirector = new ArrayList<Carrera>();
		rdfDocenteBuscar = new DocenteJdbcDto();
		arndfListPeriodoAcademicoBusq = new ArrayList<PeriodoAcademico>();
		activadorGrafico=false;
		activadorDescargar=false;
	}
	
	/**
	 * Inicializar la lista de los paralelos consultados
	 */
	public void iniciarListaParalelos(){
		try {
			iniciarParametros();
			List<ParaleloDto> listCompartida = new ArrayList<>();
			listCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
			//busco las no compartidas
			List<ParaleloDto> listNoCompartida = new ArrayList<>();
			listNoCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());

			//asignación a una sola lista
			arndfListParaleloBusq = new ArrayList<>();
			for (ParaleloDto item : listCompartida) {
				arndfListParaleloBusq.add(item);
			}
			for (ParaleloDto item : listNoCompartida) {
				arndfListParaleloBusq.add(item);
			}
			
			Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
				public int compare(ParaleloDto obj1, ParaleloDto obj2) {
					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
				}
			});
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada(){
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getArndfUsuario() {
		return arndfUsuario;
	}
	public void setArndfUsuario(Usuario arndfUsuario) {
		this.arndfUsuario = arndfUsuario;
	}
	public DocenteJdbcDto getArndfDocente() {
		return arndfDocente;
	}
	public void setArndfDocente(DocenteJdbcDto arndfDocente) {
		this.arndfDocente = arndfDocente;
	}
	public EstudianteJdbcDto getArndfEstudianteBuscar() {
		return arndfEstudianteBuscar;
	}
	public void setArndfEstudianteBuscar(EstudianteJdbcDto arndfEstudianteBuscar) {
		this.arndfEstudianteBuscar = arndfEstudianteBuscar;
	}
	public List<CarreraDto> getArndfListCarreraBusq() {
		return arndfListCarreraBusq;
	}
	public void setArndfListCarreraBusq(List<CarreraDto> arndfListCarreraBusq) {
		this.arndfListCarreraBusq = arndfListCarreraBusq;
	}
	public List<NivelDto> getArndfListNivelBusq() {
		arndfListNivelBusq = arndfListNivelBusq==null?(new ArrayList<NivelDto>()):arndfListNivelBusq;
		return arndfListNivelBusq;
	}
	public void setArndfListNivelBusq(List<NivelDto> arndfListNivelBusq) {
		this.arndfListNivelBusq = arndfListNivelBusq;
	}
	public List<MateriaDto> getArndfListMateriaBusq() {
		arndfListMateriaBusq = arndfListMateriaBusq==null?(new ArrayList<MateriaDto>()):arndfListMateriaBusq;
		return arndfListMateriaBusq;
	}
	public void setArndfListMateriaBusq(List<MateriaDto> arndfListMateriaBusq) {
		this.arndfListMateriaBusq = arndfListMateriaBusq;
	}
	public List<EstudianteJdbcDto> getArndfListEstudianteBusq() {
		arndfListEstudianteBusq = arndfListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):arndfListEstudianteBusq;
		return arndfListEstudianteBusq;
	}
	public void setArndfListEstudianteBusq(List<EstudianteJdbcDto> arndfListEstudianteBusq) {
		this.arndfListEstudianteBusq = arndfListEstudianteBusq;
	}
	public List<EstudianteJdbcDto> getArndfListEstudianteEditar() {
		arndfListEstudianteEditar = arndfListEstudianteEditar==null?(new ArrayList<EstudianteJdbcDto>()):arndfListEstudianteEditar;
		return arndfListEstudianteEditar;
	}
	public void setArndfListEstudianteEditar(List<EstudianteJdbcDto> arndfListEstudianteEditar) {
		this.arndfListEstudianteEditar = arndfListEstudianteEditar;
	}
	public List<ParaleloDto> getArndfListParaleloBusq() {
		arndfListParaleloBusq = arndfListParaleloBusq==null?(new ArrayList<ParaleloDto>()):arndfListParaleloBusq;
		return arndfListParaleloBusq;
	}
	public void setArndfListParaleloBusq(List<ParaleloDto> arndfListParaleloBusq) {
		this.arndfListParaleloBusq = arndfListParaleloBusq;
	}
	public ParaleloDto getArndfParaleloDtoEditar() {
		return arndfParaleloDtoEditar;
	}
	public void setArndfParaleloDtoEditar(ParaleloDto arndfParaleloDtoEditar) {
		this.arndfParaleloDtoEditar = arndfParaleloDtoEditar;
	}
	public List<RolFlujoCarrera> getArndfListRolFlujoCarreraBusq() {
		arndfListRolFlujoCarreraBusq = arndfListRolFlujoCarreraBusq==null?(new ArrayList<RolFlujoCarrera>()):arndfListRolFlujoCarreraBusq;
		return arndfListRolFlujoCarreraBusq;
	}
	public void setArndfListRolFlujoCarreraBusq(List<RolFlujoCarrera> arndfListRolFlujoCarreraBusq) {
		this.arndfListRolFlujoCarreraBusq = arndfListRolFlujoCarreraBusq;
	}
	public Dependencia getArndfDependenciaBuscar() {
		return arndfDependenciaBuscar;
	}
	public void setArndfDependenciaBuscar(Dependencia arndfDependenciaBuscar) {
		this.arndfDependenciaBuscar = arndfDependenciaBuscar;
	}
	public List<DocenteJdbcDto> getArndfListCarreraDocenteBusq() {
		arndfListCarreraDocenteBusq = arndfListCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):arndfListCarreraDocenteBusq;
		return arndfListCarreraDocenteBusq;
	}
	public void setArndfListCarreraDocenteBusq(List<DocenteJdbcDto> arndfListCarreraDocenteBusq) {
		this.arndfListCarreraDocenteBusq = arndfListCarreraDocenteBusq;
	}
	public List<DocenteJdbcDto> getArndfListNivelxCarreraDocenteBusq() {
		arndfListNivelxCarreraDocenteBusq = arndfListNivelxCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):arndfListNivelxCarreraDocenteBusq;
		return arndfListNivelxCarreraDocenteBusq;
	}
	public void setArndfListNivelxCarreraDocenteBusq(List<DocenteJdbcDto> arndfListNivelxCarreraDocenteBusq) {
		this.arndfListNivelxCarreraDocenteBusq = arndfListNivelxCarreraDocenteBusq;
	}
	public List<DocenteJdbcDto> getArndfListMateriasxCarreraDocenteBusq() {
		arndfListMateriasxCarreraDocenteBusq = arndfListMateriasxCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):arndfListMateriasxCarreraDocenteBusq;
		return arndfListMateriasxCarreraDocenteBusq;
	}
	public void setArndfListMateriasxCarreraDocenteBusq(List<DocenteJdbcDto> arndfListMateriasxCarreraDocenteBusq) {
		this.arndfListMateriasxCarreraDocenteBusq = arndfListMateriasxCarreraDocenteBusq;
	}
	public int getRndActivarReporte() {
		return rndActivarReporte;
	}
	public void setRndActivarReporte(int rndActivarReporte) {
		this.rndActivarReporte = rndActivarReporte;
	}
	public PeriodoAcademicoDto getArndfPeriodoAcademicoBusq() {
		return arndfPeriodoAcademicoBusq;
	}
	public void setArndfPeriodoAcademicoBusq(PeriodoAcademicoDto arndfPeriodoAcademicoBusq) {
		this.arndfPeriodoAcademicoBusq = arndfPeriodoAcademicoBusq;
	}
	public Materia getMtrModulo() {
		return mtrModulo;
	}
	public void setMtrModulo(Materia mtrModulo) {
		this.mtrModulo = mtrModulo;
	}

	public List<PeriodoAcademico> getArndfListPeriodoAcademicoBusq() {
		return arndfListPeriodoAcademicoBusq;
	}

	public void setArndfListPeriodoAcademicoBusq(List<PeriodoAcademico> arndfListPeriodoAcademicoBusq) {
		this.arndfListPeriodoAcademicoBusq = arndfListPeriodoAcademicoBusq;
	}

	public Integer getArndfPeriodoId() {
		return arndfPeriodoId;
	}

	public void setArndfPeriodoId(Integer arndfPeriodoId) {
		this.arndfPeriodoId = arndfPeriodoId;
	}

	public Integer getArndfCarreraId() {
		return arndfCarreraId;
	}

	public void setArndfCarreraId(Integer arndfCarreraId) {
		this.arndfCarreraId = arndfCarreraId;
	}

	public List<Carrera> getRdfListaCarrerasDirector() {
		return rdfListaCarrerasDirector;
	}

	public void setRdfListaCarrerasDirector(List<Carrera> rdfListaCarrerasDirector) {
		this.rdfListaCarrerasDirector = rdfListaCarrerasDirector;
	}

	public DocenteJdbcDto getRdfDocenteBuscar() {
		return rdfDocenteBuscar;
	}

	public void setRdfDocenteBuscar(DocenteJdbcDto rdfDocenteBuscar) {
		this.rdfDocenteBuscar = rdfDocenteBuscar;
	}




	public EstadisticasDocenteNotasDto getRndfEstadisticas() {
		return rndfEstadisticas;
	}




	public void setRndfEstadisticas(EstadisticasDocenteNotasDto rndfEstadisticas) {
		this.rndfEstadisticas = rndfEstadisticas;
	}

	public List<EstadisticasDocenteNotasDto> getListaEstadisticas() {
		return listaEstadisticas;
	}

	public void setListaEstadisticas(List<EstadisticasDocenteNotasDto> listaEstadisticas) {
		this.listaEstadisticas = listaEstadisticas;
	}

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public String getChartDataAxisX() {
		return chartDataAxisX;
	}

	public void setChartDataAxisX(String chartDataAxisX) {
		this.chartDataAxisX = chartDataAxisX;
	}

	public Boolean getActivadorGrafico() {
		return activadorGrafico;
	}

	public void setActivadorGrafico(Boolean activadorGrafico) {
		this.activadorGrafico = activadorGrafico;
	}

	public Boolean getActivadorGrafico2() {
		return activadorGrafico2;
	}

	public void setActivadorGrafico2(Boolean activadorGrafico2) {
		this.activadorGrafico2 = activadorGrafico2;
	}

	public Boolean getActivadorDescargar() {
		return activadorDescargar;
	}

	public void setActivadorDescargar(Boolean activadorDescargar) {
		this.activadorDescargar = activadorDescargar;
	}

	public String getChartData2() {
		return chartData2;
	}

	public void setChartData2(String chartData2) {
		this.chartData2 = chartData2;
	}
	
	
	
	
}
