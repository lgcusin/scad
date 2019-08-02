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
 31-AGOSTO-2017 		Vinicio Rosales                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionDocente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
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

/**
 * Clase (managed bean) ReporteNotasDocenteForm.
 * Managed Bean que administra la consulta de notas de los hemisemestres, recuperación y notas finales
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name="reporteNotasDocenteForm")
@SessionScoped
public class ReporteNotasDocenteForm implements Serializable{
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
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irListarParalelosXMateriaXCarrera(Usuario usuario){
		rndActivarReporte = 0;
		arndfUsuario = usuario;
		String retorno = null;
		try {
			/*********************************************************************/
			/*********** CON PANEL DE BÚSQUEDA ***********************************/
			/*********************************************************************/
			//INICIO PARAMETROS DE BUSQUEDA
			UsuarioRol usro = servRndfUsuarioRolServicio.buscarXUsuarioXrol(arndfUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			
			arndfListRolFlujoCarreraBusq = servRndfRolFlujoCarreraServicio.buscarPorIdUsuario(arndfUsuario);
			
			iniciarParametros();
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			arndfListCarreraDocenteBusq = servRndfDocenteDtoServicioJdbc.buscarCarrerasDocente(arndfUsuario.getUsrIdentificacion());
			arndfListParaleloBusq = null;
			
			/*********************************************************************/
			/*********************************************************************/

			/*********************************************************************/
			/*********** SIN PANEL DE BUSQUEDA ***********************************/
			/*********************************************************************/
			List<ParaleloDto> listNoCompartida = new ArrayList<>();
			arndfListParaleloBusq = new ArrayList<ParaleloDto>();
			//busco el periodo academico
			arndfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
			
			List<PeriodoAcademico> listaPeriodos = new ArrayList<PeriodoAcademico>();
			PeriodoAcademico prac = null;
//			try {
//				prac = servRndfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);	
//			} catch (Exception e) {
//			}
//			if(prac!=null){
//				listaPeriodos.add(prac);
//				PeriodoAcademico pracCulturaFisica = new PeriodoAcademico();
//				pracCulturaFisica.setPracId(430);
//				listaPeriodos.add(pracCulturaFisica);
//			}else{
				try {
					prac = servRndfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					listaPeriodos.add(prac);
					PeriodoAcademico pracCulturaFisica = new PeriodoAcademico();
					pracCulturaFisica.setPracId(730);
					listaPeriodos.add(pracCulturaFisica);
				} catch (Exception e) {

				}
//			}
					for (PeriodoAcademico periodoAcademico : listaPeriodos) {
						for (DocenteJdbcDto item : arndfListCarreraDocenteBusq) {
							listNoCompartida = servRndfParaleloDtoServicioJdbc.listarParalelosXcarreraXnivelXdocenteXmateriaNoCompXPracId(item.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfUsuario.getUsrId(), arndfEstudianteBuscar.getMtrId(),periodoAcademico.getPracId());
							List<ParaleloDto> listCompartida = new ArrayList<>();
							try {
								listCompartida =servRndfParaleloDtoServicioJdbc.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaCompXpracId(item.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfUsuario.getUsrId(), arndfEstudianteBuscar.getMtrId(),periodoAcademico.getPracId());
								if(listCompartida.size()!=0){

									for (ParaleloDto item2 : listCompartida) {
										arndfListParaleloBusq.add(item2);
									}	
								}
							} catch (Exception e) {
							}
							//asignación a una sola lista
							for (ParaleloDto item2 : listNoCompartida) {
								arndfListParaleloBusq.add(item2);
							}
						}
					}
			
			
			
			
			Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
				public int compare(ParaleloDto obj1, ParaleloDto obj2) {
					return new String(obj1.getMtrDescripcion()).compareTo(new String(obj2.getMtrDescripcion()));
				}
			});	
			
			/*********************************************************************/
			/*********************************************************************/
			retorno = "irVerReporteNotas";
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.irListarParalelosXMateriaXCarrera.DetallePuestoDtoJdbcException")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			try {
				iniciarParametros();
				//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
				arndfListCarreraDocenteBusq = servRndfDocenteDtoServicioJdbc.buscarCarrerasDocente(arndfUsuario.getUsrIdentificacion());
				arndfListParaleloBusq = null;
				
				/*********************************************************************/
				/*********************************************************************/

				/*********************************************************************/
				/*********** SIN PANEL DE BUSQUEDA ***********************************/
				/*********************************************************************/
				//busco el periodo academico
				arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), arndfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoAcademicoBusq.getPracId());
				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
				//busco compartidas
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
				/*********************************************************************/
				/*********************************************************************/
				retorno = "irVerReporteNotas";
			} catch (Exception e2) {
				e2.printStackTrace();
				try {
					arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					//BUSCO EL DOCENTE PARA LAS MATERIAS
					arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), arndfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoAcademicoBusq.getPracId());
					//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
					//busco compartidas
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
					return "irVerReporteNotasIdiomas";
					
				} catch (Exception e1) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.irListarParalelosXMateriaXCarrera.DetallePuestoDtoJdbcNoEncontradoException")));
				}
			}
			
			
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	public String irListarParalelosXMateriaXCarreraPeriodosAnteriores(Usuario usuario){
		rndActivarReporte = 0;
		arndfUsuario = usuario;
		String retorno = null;
		try {
			/*********************************************************************/
			/*********** CON PANEL DE BÚSQUEDA ***********************************/
			/*********************************************************************/
			//INICIO PARAMETROS DE BUSQUEDA
			UsuarioRol usro = servRndfUsuarioRolServicio.buscarXUsuarioXrol(arndfUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			
			arndfListRolFlujoCarreraBusq = servRndfRolFlujoCarreraServicio.buscarPorIdUsuario(arndfUsuario);
			
			iniciarParametros();
			
			
			
			arndfListParaleloBusq = null;
			try {
				arndfListPeriodoAcademicoBusq = servRndfPeriodoAcademicoServicio.buscarXestadolist(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);
				 ListIterator<PeriodoAcademico> litr = arndfListPeriodoAcademicoBusq.listIterator();
				    while (litr.hasNext()) {
				    	PeriodoAcademico element = litr.next();
				      if(element.getPracDescripcion().equals("HOMOLOGACION")){
				    	  litr.remove();
				      }
				    }
			} catch (PeriodoAcademicoNoEncontradoException e1) {
			} catch (PeriodoAcademicoException e1) {
			}
//			/*********************************************************************/
//			/*********************************************************************/
//
//			/*********************************************************************/
//			/*********** SIN PANEL DE BUSQUEDA ***********************************/
//			/*********************************************************************/
//			//busco el periodo academico
//			arndfPeriodoAcademicoBusq = servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			List<ParaleloDto> listNoCompartida = new ArrayList<>();
//			List<ParaleloDto> listCompartida = new ArrayList<>();
//			if(arndfPeriodoAcademicoBusq!=null){
//				//BUSCO EL DOCENTE PARA LAS MATERIAS
//				arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), arndfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoAcademicoBusq.getPracId());
//				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//				//busco compartidas
//				
//				listCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoEnCierreXcarreraXnivelXdocenteCompartida(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//				//busco las no compartidas
//				listNoCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoEnCierreXcarreraXnivelXdocenteNoComp(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//			}else{
//				arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//				//BUSCO EL DOCENTE PARA LAS MATERIAS
//				arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), arndfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoAcademicoBusq.getPracId());
//				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//				//busco compartidas
//				listCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//				//busco las no compartidas
//				
//				listNoCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//			}
//			
//			
//			
//			
//			
//			//asignación a una sola lista
//			arndfListParaleloBusq = new ArrayList<>();
//			for (ParaleloDto item : listCompartida) {
//				arndfListParaleloBusq.add(item);
//			}
//			for (ParaleloDto item : listNoCompartida) {
//				arndfListParaleloBusq.add(item);
//			}
//			
//			Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
//				public int compare(ParaleloDto obj1, ParaleloDto obj2) {
//					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
//				}
//			});
//			Iterator<ParaleloDto> it = arndfListParaleloBusq.iterator();
//			while(it.hasNext()){
//				ParaleloDto aux = it.next();
//				for (ParaleloDto item : arndfListParaleloBusq) {
//					Materia mtrAux;
//					try {
//						mtrAux = servRndfMateriaDto.buscarPorId(item.getMtrId());
//						
//						if(aux.getMtrId()==mtrAux.getMtrMateria().getMtrId()){
//							it.remove();
//							break;
//						}
//					} catch (Exception e) {
//					}
//				}	
//			}
//			
//			/*********************************************************************/
//			/*********************************************************************/
			retorno = "irVerReporteNotasPeriodosAnteriores";
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		
		}
//			try {
//				iniciarParametros();
//				//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
				
//				arndfListParaleloBusq = null;
//				
//				/*********************************************************************/
//				/*********************************************************************/
//
//				/*********************************************************************/
//				/*********** SIN PANEL DE BUSQUEDA ***********************************/
//				/*********************************************************************/
//				//busco el periodo academico
//				arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//				
//				//BUSCO EL DOCENTE PARA LAS MATERIAS
//				arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), arndfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoAcademicoBusq.getPracId());
//				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//				//busco compartidas
//				List<ParaleloDto> listCompartida = new ArrayList<>();
//				listCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//				//busco las no compartidas
//				List<ParaleloDto> listNoCompartida = new ArrayList<>();
//				listNoCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//				//asignación a una sola lista
//				arndfListParaleloBusq = new ArrayList<>();
//				for (ParaleloDto item : listCompartida) {
//					arndfListParaleloBusq.add(item);
//				}
//				for (ParaleloDto item : listNoCompartida) {
//					arndfListParaleloBusq.add(item);
//				}
//				
//				Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
//					public int compare(ParaleloDto obj1, ParaleloDto obj2) {
//						return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
//					}
//				});
//				/*********************************************************************/
//				/*********************************************************************/
//				retorno = "irVerReporteNotas";
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				try {
//					arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//					//BUSCO EL DOCENTE PARA LAS MATERIAS
//					arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), arndfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, arndfPeriodoAcademicoBusq.getPracId());
//					//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
//					//busco compartidas
//					List<ParaleloDto> listCompartida = new ArrayList<>();
//					listCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteCompartida(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//					//busco las no compartidas
//					List<ParaleloDto> listNoCompartida = new ArrayList<>();
//					listNoCompartida = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocenteNoComp(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
//
//					//asignación a una sola lista
//					arndfListParaleloBusq = new ArrayList<>();
//					for (ParaleloDto item : listCompartida) {
//						arndfListParaleloBusq.add(item);
//					}
//					for (ParaleloDto item : listNoCompartida) {
//						arndfListParaleloBusq.add(item);
//					}
//					
//					Collections.sort(arndfListParaleloBusq, new Comparator<ParaleloDto>() {
//						public int compare(ParaleloDto obj1, ParaleloDto obj2) {
//							return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
//						}
//					});
//					return "irVerReporteNotasIdiomas";
//					
//				} catch (Exception e1) {
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.irListarParalelosXMateriaXCarrera.DetallePuestoDtoJdbcNoEncontradoException")));
//				}
//			}
//			
//			
			
			
			
//		} catch (PeriodoAcademicoDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (ParaleloDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (ParaleloDtoNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} 
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
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * @param idCarrera - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera){
		arndfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		arndfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		arndfListParaleloBusq = null;
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodoActivo(arndfUsuario.getUsrId(), idCarrera, TipoPuestoConstantes.TIPO_DOCENTE_VALUE);
				//LISTO LOS NIVELES
				arndfListNivelxCarreraDocenteBusq = servRndfDocenteDtoServicioJdbc.buscarNivelesDocente(idCarrera, arndfUsuario.getUsrIdentificacion());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.llenarNivel.exception")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.llenarNivel.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idNivel, int carreraId){
		carreraId = arndfEstudianteBuscar.getCrrId();
		idNivel = arndfEstudianteBuscar.getNvlId();
		arndfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		arndfListParaleloBusq = null;
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				arndfListMateriasxCarreraDocenteBusq =servRndfDocenteDtoServicioJdbc.buscarMateriasDocente(carreraId, idNivel, arndfUsuario.getUsrIdentificacion());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.llenarMateria.exception")));
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.llenarMateria.no.encontrado.exception")));
		}
	}
	
	public void llenarMaterias(){
		try {
			List<DocenteJdbcDto> listCarrNoCompartida = new ArrayList<>();
//			listCarrNoCompartida = servRndfDocenteDtoServicioJdbc.buscarCarrerasDocentePeriodosAnteriores(arndfUsuario.getUsrIdentificacion(),arndfPeriodoId);
			
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			
			listCarrNoCompartida = servRndfDocenteDtoServicioJdbc.buscarCarrerasDocentePeriodosInactivos(arndfUsuario.getUsrIdentificacion(),arndfPeriodoId);
			arndfListCarreraDocenteBusq = new ArrayList<>();
			//asigno carreras con detalle puesto
			for (DocenteJdbcDto itemCarr : listCarrNoCompartida) {
				arndfListCarreraDocenteBusq.add(itemCarr);
			}
			
			//listo carreras compartidas 
			List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
			try {
				listCarrCompartida = servRndfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listCarrNoCompartida);

				
				if(listCarrCompartida.size() > 0){
					//asigno carreras compartidas
					boolean igual = false;
					for (DocenteJdbcDto item : listCarrCompartida) {
						igual = false;
						for (DocenteJdbcDto itemAux : listCarrNoCompartida) {
							if(item.getCrrId() == itemAux.getCrrId()){
								igual = true;
							}
						}
						if(igual){
							continue;
						}else{
							arndfListCarreraDocenteBusq.add(item);
						}
					}	
				}
			} catch (Exception e) {
			}
			
			Collections.sort(arndfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
				public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
				}
			});
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al buscar las materias asignadas al docente.");
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al buscar las materias asignadas al docente.");
		}catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al buscar las materias asignadas al docente.");
		}
	}
	
	
	
	
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		arndfListParaleloBusq = null;
		try {
			if(arndfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			}else if(arndfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else if(arndfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			}else{
				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
				arndfListParaleloBusq = servRndfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocente(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), arndfDocente.getDtpsId(), arndfEstudianteBuscar.getMtrId());
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.buscar.exception")));
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.Notas.Docente.buscar.no.encontrado.exception")));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Metodo que llama los datos para la genracion del reporte
	 */
	public void reporteNotas(){
		
	}
	
	/**
	 * Método que no nos permite generar el reporte de las notas del primer hemisemestre
	 * @param prl - Objeto para la busqueda 
	 */
	public void reporte1Hemi(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto();
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = null;
		arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>(); 
		
		boolean modulo = false;
		try {
			arndfPeriodoAcademicoBusq =  servRndfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);

			arndfDocente = servRndfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(arndfUsuario.getUsrId(), 
					arndfEstudianteBuscar.getCrrId(), 
					TipoPuestoConstantes.TIPO_DOCENTE_VALUE, 
					arndfPeriodoAcademicoBusq.getPracId());
			//ver si es modular
			Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
			if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
					
				
				Integer mlcrprAuxId = servRndfMallaCurricularParaleloDtoServicioJdbc
						.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
								arndfUsuario.getUsrIdentificacion(),
								arndfParaleloDtoEditar.getMlcrmtNvlId(),
								arndfParaleloDtoEditar.getPrlId());
				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
				mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
				mtrModulo = mtrAux;
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				listaprueba = servRndfNotasPregradoDtoServicioJdbc
						.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(),
								mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
								arndfDocente.getFcdcId(), mlcrprAux.getMlcrprId(), arndfParaleloDtoEditar.getMlcrprId());
				arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
				List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
				for (EstudianteJdbcDto item : listaprueba) {
					item.setMateriaModulo(true);
					// En caso de que no existe el resultado de búsqueda
					if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
						item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
						item.setClmdNota1(null);
						item.setClmdAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
						item.setClmdAsistenciaEstudiante1(null);
						item.setClmdNota1(null);
						npfListEstudianteBusqPrueba.add(item);
					} else {
						// En caso de que sean la misma malla_curricula_paralelo
						if (item.getMlcrprIdModulo() == arndfParaleloDtoEditar.getMlcrprId().intValue()) {
							npfListEstudianteBusqPrueba.add(item);
						} else {
							item.setClmdId(GeneralesConstantes.APP_ID_BASE);
							item.setClmdNota1(null);
							item.setClmdAsistenciaEstudiante1(null);
							item.setClmdAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
							item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
							npfListEstudianteBusqPrueba.add(item);
						}
					}
				}
				for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
					for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

						if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
								.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
								&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba.get(j)
										.getClfId())) {
							npfListEstudianteBusqPrueba.remove(j);
							j--;
						}
					}
				}

				for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
					boolean op = true;
					for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
						if (i != j) {
							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
								if (npfListEstudianteBusqPrueba.get(i).getClfId() == GeneralesConstantes.APP_ID_BASE) {
									op = false;
								}
							}
						}
					}
					if (op) {
						arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
					}
				}
				
				//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotasModular(arndfListEstudianteBusq, arndfUsuario, arndfParaleloDtoEditar.getMtrDescripcion(), arndfParaleloDtoEditar.getDpnDescripcion(), arndfParaleloDtoEditar.getCrrDescripcion());
				rndActivarReporte = 1;

				//				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
				//						arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
				//						arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
				//				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
				modulo = true;
			}
			if(!modulo){
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
//				if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
//				}else{// Compartida o dependiente de otra
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
//				}
//				for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
//					item.setCrrId(arndfParaleloDtoEditar.getCrrId());
//				}
				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
						arndfParaleloDtoEditar.getPrlId(), 
						arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
				rndActivarReporte = 1;
			}
			//iniciar parametros originales
			//			iniciarListaParalelos();
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que no nos permite generar el reporte de las notas del primer hemisemestre
	 * @param prl - Objeto para la busqueda 
	 */
	public void reporte1HemiAnterior(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto();
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = null;
		arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>(); 
		boolean modulo = false;
		try {
			//ver si es modular
			Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
			if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servRndfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							arndfDocente.getPrsIdentificacion(),arndfParaleloDtoEditar.getMlcrmtNvlId(),arndfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModularAnterior(
							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),arndfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),arndfParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota2(null);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
			            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
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
								mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloIdAnterior(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
										item.setClfNota2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfNota2(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
						            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
					}
				}
					
	      
				
				
				//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotasModular(arndfListEstudianteBusq, arndfUsuario, arndfParaleloDtoEditar.getMtrDescripcion(), arndfParaleloDtoEditar.getDpnDescripcion(), arndfParaleloDtoEditar.getCrrDescripcion());
				rndActivarReporte = 1;

				//				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
				//						arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
				//						arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
				//				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
				modulo = true;
			}
			if(!modulo){
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
//				if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteAnterior(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
//				}else{// Compartida o dependiente de otra
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaAnterior(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
//				}
//				for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
//					item.setCrrId(arndfParaleloDtoEditar.getCrrId());
//				}
				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParaleloPeriodoInactivo(
						arndfParaleloDtoEditar.getPrlId(), 
						arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
				rndActivarReporte = 1;
			}
			//iniciar parametros originales
			//			iniciarListaParalelos();
		} catch (EstudianteDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Método que no nos permite generar el reporte de las notas del segundo hemisemestre
	 * @param prl - Objeto para la busqueda 
	 */
	public void reporte2Hemi(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto();
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = null;
		List<EstudianteJdbcDto> arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		boolean modulo = false;
		try {
			//ver si es modular
			Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
			if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
				Integer mlcrprAuxId = servRndfMallaCurricularParaleloDtoServicioJdbc
						.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
								arndfUsuario.getUsrIdentificacion(),
								arndfParaleloDtoEditar.getMlcrmtNvlId(),
								arndfParaleloDtoEditar.getPrlId());
				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
				mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
				mtrModulo = mtrAux;
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				listaprueba = servRndfNotasPregradoDtoServicioJdbc
						.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(),
								mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
								arndfDocente.getFcdcId(), mlcrprAux.getMlcrprId(), arndfParaleloDtoEditar.getMlcrprId());
				arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
				List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
				for (EstudianteJdbcDto item : listaprueba) {
					item.setMateriaModulo(true);
					// En caso de que no existe el resultado de búsqueda
					if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
						item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
						item.setClmdNota1(null);
						item.setClmdAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
						item.setClmdAsistenciaEstudiante1(null);
						item.setClmdNota1(null);
						npfListEstudianteBusqPrueba.add(item);
					} else {
						// En caso de que sean la misma malla_curricula_paralelo
						if (item.getMlcrprIdModulo() == arndfParaleloDtoEditar.getMlcrprId().intValue()) {
							npfListEstudianteBusqPrueba.add(item);
						} else {
							item.setClmdId(GeneralesConstantes.APP_ID_BASE);
							item.setClmdNota1(null);
							item.setClmdAsistenciaEstudiante1(null);
							item.setClmdAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
							item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
							npfListEstudianteBusqPrueba.add(item);
						}
					}
				}
				for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
					for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

						if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
								.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
								&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba.get(j)
										.getClfId())) {
							npfListEstudianteBusqPrueba.remove(j);
							j--;
						}
					}
				}

				for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
					boolean op = true;
					for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
						if (i != j) {
							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
								if (npfListEstudianteBusqPrueba.get(i).getClfId() == GeneralesConstantes.APP_ID_BASE) {
									op = false;
								}
							}
						}
					}
					if (op) {
						arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
					}
				}
				
				
				//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotasModular2Hemi(arndfListEstudianteBusq, arndfUsuario, arndfParaleloDtoEditar.getMtrDescripcion(), arndfParaleloDtoEditar.getDpnDescripcion(), arndfParaleloDtoEditar.getCrrDescripcion());
				rndActivarReporte = 2;

				modulo = true;
			}
			if(!modulo){
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
//				if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
//				}else{// Compartida o dependiente de otra
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
//				}
//				for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
//					item.setCrrId(arndfParaleloDtoEditar.getCrrId());
//				}
				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
						arndfParaleloDtoEditar.getPrlId(), 
						arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotasSegundoHemisemestre(arndfListEstudianteBusq, arndfUsuario);
				rndActivarReporte = 2;
			}
			//iniciar parametros originales
			//			iniciarListaParalelos();
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
	 
		} catch (Exception ex) {
		}
	}
	
	public void reporte2HemiAnterior(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto();
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = null;
		List<EstudianteJdbcDto> arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		boolean modulo = false;
		try {
			//ver si es modular
			Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
			if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){

				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servRndfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							arndfDocente.getPrsIdentificacion(),arndfParaleloDtoEditar.getMlcrmtNvlId(),arndfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModularAnterior(
							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),arndfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),arndfParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota2(null);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
			            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
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
								mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloIdAnterior(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
										item.setClfNota2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfNota2(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
						            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
					}
				}
				
				//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotasModular2Hemi(arndfListEstudianteBusq, arndfUsuario, arndfParaleloDtoEditar.getMtrDescripcion(), arndfParaleloDtoEditar.getDpnDescripcion(), arndfParaleloDtoEditar.getCrrDescripcion());
				rndActivarReporte = 2;

				//				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
				//						arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
				//						arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
				//				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
				modulo = true;
			}
			if(!modulo){
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
//				if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteAnterior(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
//				}else{// Compartida o dependiente de otra
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaAnterior(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
//				}
//				for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
//					item.setCrrId(arndfParaleloDtoEditar.getCrrId());
//				}
				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParaleloPeriodoInactivo(
						arndfParaleloDtoEditar.getPrlId(), 
						arndfParaleloDtoEditar.getMlcrprId());
				ReporteNotasForm.generarReporteNotasSegundoHemisemestre(arndfListEstudianteBusq, arndfUsuario);
				rndActivarReporte = 2;
			}
			//iniciar parametros originales
			//			iniciarListaParalelos();
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
	 
		} catch (Exception ex) {
		}
	}
	
	/**
	 * Método que no nos permite generar el reporte de las notas de recuperación
	 * @param prl - Objeto para la busqueda 
	 */
	public void reporteRecuperacion(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto(); 
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		boolean modulo = false;
		try {
			//ver si es modular
			Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
			if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){

				Integer mlcrprAuxId = servRndfMallaCurricularParaleloDtoServicioJdbc
						.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
								arndfUsuario.getUsrIdentificacion(),
								arndfParaleloDtoEditar.getMlcrmtNvlId(),
								arndfParaleloDtoEditar.getPrlId());
				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
				mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
				mtrModulo = mtrAux;
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				listaprueba = servRndfNotasPregradoDtoServicioJdbc
						.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(),
								mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
								arndfDocente.getFcdcId(), mlcrprAux.getMlcrprId(), arndfParaleloDtoEditar.getMlcrprId());
				arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
				List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
				for (EstudianteJdbcDto item : listaprueba) {
					item.setMateriaModulo(true);
					// En caso de que no existe el resultado de búsqueda
					if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
						item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
						item.setClmdNota1(null);
						item.setClmdAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
						item.setClmdAsistenciaEstudiante1(null);
						item.setClmdNota1(null);
						npfListEstudianteBusqPrueba.add(item);
					} else {
						// En caso de que sean la misma malla_curricula_paralelo
						if (item.getMlcrprIdModulo() == arndfParaleloDtoEditar.getMlcrprId().intValue()) {
							npfListEstudianteBusqPrueba.add(item);
						} else {
							item.setClmdId(GeneralesConstantes.APP_ID_BASE);
							item.setClmdNota1(null);
							item.setClmdAsistenciaEstudiante1(null);
							item.setClmdAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
							item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
							npfListEstudianteBusqPrueba.add(item);
						}
					}
				}
				for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
					for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

						if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
								.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
								&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba.get(j)
										.getClfId())) {
							npfListEstudianteBusqPrueba.remove(j);
							j--;
						}
					}
				}

				for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
					boolean op = true;
					for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
						if (i != j) {
							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
								if (npfListEstudianteBusqPrueba.get(i).getClfId() == GeneralesConstantes.APP_ID_BASE) {
									op = false;
								}
							}
						}
					}
					if (op) {
						arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
					}
				}
				
				//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
				Iterator<EstudianteJdbcDto> it = arndfListEstudianteBusq.iterator();
				while (it.hasNext()){
					EstudianteJdbcDto aux = new EstudianteJdbcDto();
					aux= it.next();
					try {
						int comparadorMayor = aux.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
						int comparadorMenor = aux.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
						if(comparadorMayor==-1 && (comparadorMenor==0 || comparadorMenor==1)){

						}else{
							it.remove();
						}
	
					} catch (Exception e) {
						it.remove();
					}
									}
				if(arndfListEstudianteBusq.size()!=0){
					ReporteNotasForm.generarReporteNotasModular2Hemi(arndfListEstudianteBusq, arndfUsuario, arndfParaleloDtoEditar.getMtrDescripcion(), arndfParaleloDtoEditar.getDpnDescripcion(), arndfParaleloDtoEditar.getCrrDescripcion());
					rndActivarReporte = 3;
	
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No existen estudiantes que se encuentren en recuperación.");
				}
				
				modulo = true;
			}
			if(!modulo){
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
						arndfParaleloDtoEditar.getPrlId(), 
						arndfParaleloDtoEditar.getMlcrprId());
				Iterator<EstudianteJdbcDto> it = arndfListEstudianteBusq.iterator();
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
				ReporteNotasForm.generarReporteNotasRecuperacion(arndfListEstudianteBusq, arndfUsuario);
				rndActivarReporte = 3;
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e1) {
		} catch (Exception e1) {
		}
	}
	
	public void reporteRecuperacionAnterior(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto(); 
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		boolean modulo = false;
		try {
			//ver si es modular
			Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
			if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){

				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
				
				try {
					Integer mlcrprAuxId =  servRndfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
							arndfDocente.getPrsIdentificacion(),arndfParaleloDtoEditar.getMlcrmtNvlId(),arndfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					
					listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModularAnterior(
							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
							mtrAux.getMtrMateria().getMtrId(),arndfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),arndfParaleloDtoEditar.getMlcrprId());
					
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
							item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						}else{
							//En caso de que sean la misma malla_curricula_paralelo
							if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
								npfListEstudianteBusqPrueba.add(item);
							}else{
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota2(null);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
			            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
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
								mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;
								
								listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloIdAnterior(mlcrprAux.getMlcrprId());
								
								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de búsqueda
									if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
										item.setClfNota2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfNota2(null);
										npfListEstudianteBusqPrueba.add(item);
									}else{
										//En caso de que sean la misma malla_curricula_paralelo
										if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
											npfListEstudianteBusqPrueba.add(item);
										}else{
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
						            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						            }
						        }
							} catch (Exception ex) {
							}
								
				 
					} catch (Exception ex) {
					}
				}
				
				//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
				Iterator<EstudianteJdbcDto> it = arndfListEstudianteBusq.iterator();
				while (it.hasNext()){
					EstudianteJdbcDto aux = new EstudianteJdbcDto();
					aux= it.next();
					try {
						int comparadorMayor = aux.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
						int comparadorMenor = aux.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
						if(comparadorMayor==-1 && (comparadorMenor==0 || comparadorMenor==1)){

						}else{
							it.remove();
						}
	
					} catch (Exception e) {
						it.remove();
					}
									}
				if(arndfListEstudianteBusq.size()!=0){
					ReporteNotasForm.generarReporteNotasModular2Hemi(arndfListEstudianteBusq, arndfUsuario, arndfParaleloDtoEditar.getMtrDescripcion(), arndfParaleloDtoEditar.getDpnDescripcion(), arndfParaleloDtoEditar.getCrrDescripcion());
					rndActivarReporte = 3;
	
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No existen estudiantes que se encuentren en recuperación.");
				}
				
				
				//				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
				//						arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
				//						arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
				//				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
				modulo = true;
			}
			if(!modulo){
				//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
//				if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteAnterior(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
//				}else{// Compartida o dependiente de otra
//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaAnterior(
//							arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//							arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
//				}
//				for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
//					item.setCrrId(arndfParaleloDtoEditar.getCrrId());
//				}
				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParaleloPeriodoInactivo(
						arndfParaleloDtoEditar.getPrlId(), 
						arndfParaleloDtoEditar.getMlcrprId());
				Iterator<EstudianteJdbcDto> it = arndfListEstudianteBusq.iterator();
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
				ReporteNotasForm.generarReporteNotasRecuperacion(arndfListEstudianteBusq, arndfUsuario);
				rndActivarReporte = 3;
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e1) {
		} catch (MateriaException e1) {
		}
	}
	
	/**
	 * Método que no nos permite generar el reporte de las notas del primer hemisemestre, segundo hemisemestre, recuperación, y notas finales
	 * @param prl - Objeto para la busqueda 
	 */
	public void reporteNotasFinales(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto(); 
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		boolean modulo = false;
		try {
				//ver si es modular
				Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
				if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
					List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
					
					try {
						Integer mlcrprAuxId =  servRndfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
								arndfDocente.getPrsIdentificacion(),arndfParaleloDtoEditar.getMlcrmtNvlId(),arndfParaleloDtoEditar.getPrlId());
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
						mtrModulo = mtrAux;
						
						listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
								mtrAux.getMtrMateria().getMtrId(),arndfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),arndfParaleloDtoEditar.getMlcrprId());
						
						List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
						for (EstudianteJdbcDto item : listaprueba) {
							item.setMateriaModulo(true);
							// En caso de que no existe el resultado de búsqueda
							if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
								item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
								item.setClfNota2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfNota2(null);
								npfListEstudianteBusqPrueba.add(item);
							}else{
								//En caso de que sean la misma malla_curricula_paralelo
								if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
									npfListEstudianteBusqPrueba.add(item);
								}else{
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
				            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
				            }
				        }
					} catch (Exception e) {
						 try {
								 listaprueba = new ArrayList<EstudianteJdbcDto>();
								try {
//									Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//											nrctfParaleloDtoEditar.getMlcrprId());
//									System.out.println(mlcrprAuxId);
									MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
									mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
									MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
									mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
									mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
									mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
									mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
									mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
									mtrModulo = mtrAux;
									
									listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
									
									List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
									for (EstudianteJdbcDto item : listaprueba) {
										item.setMateriaModulo(true);
										// En caso de que no existe el resultado de búsqueda
										if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
											item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
											item.setClfNota2(null);
											item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
											item.setClfAsistenciaEstudiante2(null);
											item.setClfNota2(null);
											npfListEstudianteBusqPrueba.add(item);
										}else{
											//En caso de que sean la misma malla_curricula_paralelo
											if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
												npfListEstudianteBusqPrueba.add(item);
											}else{
													item.setClfId(GeneralesConstantes.APP_ID_BASE);
													item.setClfNota2(null);
													item.setClfAsistenciaEstudiante2(null);
													item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
													item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
							            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
							            }
							        }
								} catch (Exception ex) {
								}
									
					 
						} catch (Exception ex) {
						}
					}
					//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
					ReporteNotasForm.generarReporteNotasTotalesModular(arndfListEstudianteBusq, arndfUsuario);
					rndActivarReporte = 4;	

					//				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
					//						arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
					//						arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
					//				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
					modulo = true;
				}
				if(!modulo){
					//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
//					if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
//						arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
//								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//								arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
//					}else{// Compartida o dependiente de otra
//						arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
//								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
//								arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
//					}
//					for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
//						item.setCrrId(arndfParaleloDtoEditar.getCrrId());
//					}
					
					//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
							arndfParaleloDtoEditar.getPrlId(), 
							arndfParaleloDtoEditar.getMlcrprId());
					
					ReporteNotasForm.generarReporteNotasTotales(arndfListEstudianteBusq, arndfUsuario);
					rndActivarReporte = 4;
				}
				//iniciar parametros originales
				//			iniciarListaParalelos();
			} catch (EstudianteDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EstudianteDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MateriaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MateriaException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (NullPointerException e) {
				try {
					 List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
					try {
//						Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//								nrctfParaleloDtoEditar.getMlcrprId());
//						System.out.println(mlcrprAuxId);
						Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
						MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
						mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
						mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
						mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
						mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
						mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
						
						listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
						List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
						for (EstudianteJdbcDto item : listaprueba) {
							item.setMateriaModulo(true);
							// En caso de que no existe el resultado de búsqueda
							if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
								item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
								item.setClfNota1(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfNota2(null);
								npfListEstudianteBusqPrueba.add(item);
							}else{
								//En caso de que sean la misma malla_curricula_paralelo
								if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
									npfListEstudianteBusqPrueba.add(item);
								}else{
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
						arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
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
				            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
				            }
				        }
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
						item.setCrrDescripcion(arndfParaleloDtoEditar.getCrrDescripcion());
						item.setDpnDescripcion(arndfParaleloDtoEditar.getDpnDescripcion());
					}
					ReporteNotasForm.generarReporteNotasTotalesModular(arndfListEstudianteBusq, arndfUsuario);
					rndActivarReporte = 4;	
		 
			} catch (Exception ex) {
			}
			} 

		for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
			item.setCrrDescripcion(arndfParaleloDtoEditar.getCrrDescripcion());
			item.setDpnDescripcion(arndfParaleloDtoEditar.getDpnDescripcion());
		}
		ReporteNotasForm.generarReporteNotasTotales(arndfListEstudianteBusq, arndfUsuario);
		rndActivarReporte = 4;
	}
	
	
	public void reporteNotasFinalesAnterior(ParaleloDto prl){
		arndfParaleloDtoEditar = new ParaleloDto(); 
		arndfParaleloDtoEditar = prl;
		arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		boolean modulo = false;
		try {
				//ver si es modular
				Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
				if(prl.getMtrTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
					List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
					
					try {
						Integer mlcrprAuxId =  servRndfMallaCurricularParaleloDtoServicioJdbc.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(), 
								arndfDocente.getPrsIdentificacion(),arndfParaleloDtoEditar.getMlcrmtNvlId(),arndfParaleloDtoEditar.getPrlId());
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
						mtrModulo = mtrAux;
						
						listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModularAnterior(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), mlcrprAux.getMlcrprParalelo().getPrlId(), 
								mtrAux.getMtrMateria().getMtrId(),arndfDocente.getFcdcId(),mlcrprAux.getMlcrprId(),arndfParaleloDtoEditar.getMlcrprId());
						
						List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
						for (EstudianteJdbcDto item : listaprueba) {
							item.setMateriaModulo(true);
							// En caso de que no existe el resultado de búsqueda
							if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
								item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
								item.setClfNota2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfNota2(null);
								npfListEstudianteBusqPrueba.add(item);
							}else{
								//En caso de que sean la misma malla_curricula_paralelo
								if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
									npfListEstudianteBusqPrueba.add(item);
								}else{
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
				            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
				            }
				        }
					} catch (Exception e) {
						 try {
								 listaprueba = new ArrayList<EstudianteJdbcDto>();
								try {
//									Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//											nrctfParaleloDtoEditar.getMlcrprId());
//									System.out.println(mlcrprAuxId);
									MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
									mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
									MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
									mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
									mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
									mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
									mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
									mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
									mtrModulo = mtrAux;
									
									listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloIdAnterior(mlcrprAux.getMlcrprId());
									
									List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
									for (EstudianteJdbcDto item : listaprueba) {
										item.setMateriaModulo(true);
										// En caso de que no existe el resultado de búsqueda
										if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
											item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
											item.setClfNota2(null);
											item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
											item.setClfAsistenciaEstudiante2(null);
											item.setClfNota2(null);
											npfListEstudianteBusqPrueba.add(item);
										}else{
											//En caso de que sean la misma malla_curricula_paralelo
											if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
												npfListEstudianteBusqPrueba.add(item);
											}else{
													item.setClfId(GeneralesConstantes.APP_ID_BASE);
													item.setClfNota2(null);
													item.setClfAsistenciaEstudiante2(null);
													item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
													item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
							            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
							            }
							        }
								} catch (Exception ex) {
								}
									
					 
						} catch (Exception ex) {
						}
					}
					//					arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaModular(arndfEstudianteBuscar.getCrrId(), arndfEstudianteBuscar.getNvlId(), GeneralesConstantes.APP_ID_BASE, materiaModular, GeneralesConstantes.APP_ID_BASE, arndfParaleloDtoEditar.getMlcrprId());
					ReporteNotasForm.generarReporteNotasTotalesModular(arndfListEstudianteBusq, arndfUsuario);
					rndActivarReporte = 4;	

					//				arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
					//						arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
					//						arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
					//				ReporteNotasForm.generarReporteNotas(arndfListEstudianteBusq, arndfUsuario);
					modulo = true;
				}
				if(!modulo){
					//IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES SI COMPARTEN O NO COMPARTEN
					if(arndfParaleloDtoEditar.getHracMlcrprIdComp() == null || arndfParaleloDtoEditar.getHracMlcrprIdComp().intValue() == 0){// No comparte o no tiene compartidas con nadie
						arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteAnterior(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
								arndfParaleloDtoEditar.getMtrId(),arndfDocente.getFcdcId(),arndfParaleloDtoEditar.getMlcrprId());
					}else{// Compartida o dependiente de otra
						arndfListEstudianteBusq = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaAnterior(
								arndfParaleloDtoEditar.getCrrId(), arndfParaleloDtoEditar.getMlcrmtNvlId(), arndfParaleloDtoEditar.getPrlId(), 
								arndfParaleloDtoEditar.getMtrId(),GeneralesConstantes.APP_ID_BASE,arndfParaleloDtoEditar.getMlcrprId());
					}
					for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
						item.setCrrId(arndfParaleloDtoEditar.getCrrId());
					}
					ReporteNotasForm.generarReporteNotasTotales(arndfListEstudianteBusq, arndfUsuario);
					rndActivarReporte = 4;
				}
				//iniciar parametros originales
				//			iniciarListaParalelos();
			} catch (EstudianteDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EstudianteDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MateriaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MateriaException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (NullPointerException e) {
				try {
					 List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
					try {
//						Integer mlcrprAuxId =  servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
//								nrctfParaleloDtoEditar.getMlcrprId());
//						System.out.println(mlcrprAuxId);
						Materia mtrAux = servRndfMateriaDto.buscarPorId(arndfParaleloDtoEditar.getMtrId());
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorId(arndfParaleloDtoEditar.getMlcrprId());
						MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
						mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
						mtrAux = servRndfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
						mtrAux = servRndfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
						mlcrmtAux = servRndfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
						mlcrprAux = servRndfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(),arndfParaleloDtoEditar.getPrlId());
						
						listaprueba = servRndfNotasPregradoDtoServicioJdbc.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());
						List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
						for (EstudianteJdbcDto item : listaprueba) {
							item.setMateriaModulo(true);
							// En caso de que no existe el resultado de búsqueda
							if(item.getMlcrprIdModulo()==GeneralesConstantes.APP_ID_BASE ){
								item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
								item.setClfNota1(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfNota2(null);
								npfListEstudianteBusqPrueba.add(item);
							}else{
								//En caso de que sean la misma malla_curricula_paralelo
								if(item.getMlcrprIdModulo()==arndfParaleloDtoEditar.getMlcrprId().intValue()){
									npfListEstudianteBusqPrueba.add(item);
								}else{
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(arndfParaleloDtoEditar.getMlcrprId());
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
						arndfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
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
				            	arndfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
				            }
				        }
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
						item.setCrrDescripcion(arndfParaleloDtoEditar.getCrrDescripcion());
						item.setDpnDescripcion(arndfParaleloDtoEditar.getDpnDescripcion());
					}
					ReporteNotasForm.generarReporteNotasTotalesModular(arndfListEstudianteBusq, arndfUsuario);
					rndActivarReporte = 4;	
		 
			} catch (Exception ex) {
			}
			} 

		for (EstudianteJdbcDto item : arndfListEstudianteBusq) {
			item.setCrrDescripcion(arndfParaleloDtoEditar.getCrrDescripcion());
			item.setDpnDescripcion(arndfParaleloDtoEditar.getDpnDescripcion());
		}
		ReporteNotasForm.generarReporteNotasTotales(arndfListEstudianteBusq, arndfUsuario);
		rndActivarReporte = 4;
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
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
	
	
	
	
}
