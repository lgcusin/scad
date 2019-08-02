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
   
 ARCHIVO:     RecalificacionNotasForm.java	  
 DESCRIPCION: Bean de sesion que maneja la recalificación de notas por parte del Director de Carrera 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-08-2018		  	Daniel Albuja                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionDirCarrera;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.UserAuthenticator;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.auth.StaticUserAuthenticator;
import org.apache.commons.vfs.impl.DefaultFileSystemConfigBuilder;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteAprobacionSolicitudTerceraForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.IdentifidorCliente;

/**
 * Clase (session bean) RecalificacionNotasForm. 
 * Bean de sesion que maneja la recalificación de notas por parte del Director de Carrera 
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name = "recalificacionNotasForm")
@SessionScoped
public class RecalificacionNotasForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario astmfUsuario;
	//PARA BUSQUEDA
	private PeriodoAcademico rnfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> astmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico rnfPeriodoAcademicoActivo;
	private CarreraDto rnfCarreraDtoBuscar;
	private List<CarreraDto> rnfListCarreraDtoBusq;
	private EstudianteJdbcDto rnfEstudianteBuscar;
	private List<MateriaDto> rnfListMateriaBusq;
	private List<MateriaDto> rnfListaEstudiantesRecalificacion;
	private List<MateriaDto> rnfListaEstudiantesRecalificacionEdicion;
	private Carrera rnfCarreraVer;
	private Persona rnfPersonaSeleccionadaVer;
	private String nrctfRegCliente;
	
	
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD
	private List<SolicitudTerceraMatriculaDto> astmfListaSolicitudesDto;
	//VER MATERIAS
	private PeriodoAcademico astmfPeriodoSolicitudesVer;
	private FichaInscripcionDto astmfFichaInscripcion;
	private List<SolicitudTerceraMatriculaDto> astmfListaMateriasSolicitadasDto;
	private Integer astmfValidadorSeleccion;
	private Integer astmfClickGuardarResolver;
	private Integer astmfTipoCronograma;
	private Dependencia astmfDependencia;
	private CronogramaActividadJdbcDto astmfCronogramaActividad;
	private String astmfArchivoSelSt;
	private Boolean astmfDesactivaBotonGuardar;
	private String astmfObservacionFinal;
	
	//REPORTE
	private Integer astmfActivaReportePDF;
	private Boolean astmfDesactivaBotonReporte;
	
	private List<SolicitudTerceraMatriculaDto>  astmfListaMateriasReporte;
	private String astmfObservacionReporte;


	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar() {
	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB 
	PeriodoAcademicoServicio servAstmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servAstmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servAstmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servAstmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servAstmfPersonaServicio;
	@EJB 
	CarreraServicio servAstmfCarreraServicio;
	@EJB 
	DependenciaServicio servAstmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servAstmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servAstmfSolicitudTerceraMatriculaServicioServicioJdbc;
	@EJB 
	FichaInscripcionDtoServicioJdbc servAstmfFichaInscripcionDto;
	@EJB
	private MateriaDtoServicioJdbc servRnfMateriaDtoServicioJdbc;
	@EJB
	private MateriaServicio servRnfMateriaServicio;
	@EJB
	private MallaCurricularMateriaServicio servRnfMallaCurricularMateriaServicio;
	@EJB
	private MallaCurricularParaleloServicio servRnfMallaCurricularParaleloServicio;
	@EJB
	private RecordEstudianteDtoServicioJdbc servRnfRecordEstudianteDtoServicioJdbc;
	@EJB
	private CalificacionServicio servRnfCalificacionbServicio;
	@EJB
	private RecordEstudianteServicio servRnfRecordEstudianteServicio;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irAListarEstudiantes(Usuario usuario) {
		astmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		try {
			rnfPeriodoAcademicoActivo = servAstmfPeriodoAcademicoServicio.buscarXestadoSinExcepcion(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		   
		   if(rnfPeriodoAcademicoActivo!=null){
			   
		   }else{
			   rnfPeriodoAcademicoActivo = servAstmfPeriodoAcademicoServicio.buscarXestadoSinExcepcion(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			     
		   }
		    
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			rnfListCarreraDtoBusq = servAstmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(astmfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rnfPeriodoAcademicoActivo.getPracId());
			retorno = "irListarEstudiantesRecalificacion";
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(){
		rnfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		rnfListMateriaBusq = new ArrayList<MateriaDto>();
		rnfListaEstudiantesRecalificacion = new ArrayList<MateriaDto>();
		try {
			try {
				rnfPeriodoAcademicoActivo = servAstmfPeriodoAcademicoServicio.buscarXestadoSinExcepcion(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				rnfListMateriaBusq = servRnfMateriaDtoServicioJdbc.listarMateriasxCarreraPracEnCierre(rnfCarreraDtoBuscar.getCrrId());
			} catch (Exception e) {
				rnfPeriodoAcademicoActivo = servAstmfPeriodoAcademicoServicio.buscarXestadoSinExcepcion(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				rnfListMateriaBusq = servRnfMateriaDtoServicioJdbc.listarMateriasxCarrera(rnfCarreraDtoBuscar.getCrrId());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al buscar las asignaturas asignadas a la carrera.");
		} 
	}
	
	
	/**
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron tercera matrícula con los parámetros ingresados
	 */
	public void buscarEstudiantes(){
		try {
			
			rnfListaEstudiantesRecalificacion = servRnfMateriaDtoServicioJdbc.listarEstudiantesParaRecalificacion(rnfEstudianteBuscar.getPrsIdentificacion(),rnfListCarreraDtoBusq,rnfCarreraDtoBuscar.getCrrId(),rnfEstudianteBuscar.getMtrId());
			
			rnfListaEstudiantesRecalificacion = GeneralesUtilidades.quitarDuplicadosRecalificacion(rnfListaEstudiantesRecalificacion);
		} catch (MateriaDtoException e) {
		} catch (MateriaDtoNoEncontradoException e) {
		}
	}
	
	/**
	 * Método que permite ir a la visualización de las materias
	 * @param solicitudesEstudiante - solicitudesEstudiante objeto que se envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irEditarNotasEstudiante(MateriaDto  registro){
		
		String retorno =null;
		
		try {
			rnfCarreraVer= servAstmfCarreraServicio.buscarPorId(registro.getCrrId());
		} catch (CarreraNoEncontradoException e3) {
			    FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e3.getMessage());
		} catch (CarreraException e3) {
			    FacesUtil.limpiarMensaje(); 
			    FacesUtil.mensajeError(e3.getMessage());
		}
		
		try {
			rnfPersonaSeleccionadaVer= servAstmfPersonaServicio.buscarPorId(registro.getPrsId());
		} catch (PersonaNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		} catch (PersonaException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		
		try {
			if(rnfCarreraVer != null && rnfCarreraVer.getCrrTipo()==CarreraConstantes.TIPO_NIVELEACION_VALUE){
				rnfListaEstudiantesRecalificacionEdicion = servRnfMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoXcarreraFull(rnfPersonaSeleccionadaVer.getPrsId(), registro.getFcmtId(), rnfPeriodoAcademicoActivo.getPracId(), rnfCarreraVer.getCrrId());
			}if(rnfCarreraVer != null && rnfCarreraVer.getCrrTipo()==CarreraConstantes.TIPO_POSGRADO_VALUE){
				rnfListaEstudiantesRecalificacionEdicion = servRnfMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoXcarreraXPosgrado(rnfPersonaSeleccionadaVer.getPrsId(), registro.getFcmtId(), rnfPeriodoAcademicoActivo.getPracId());
			}else{
				rnfListaEstudiantesRecalificacionEdicion = servRnfMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoXcarrera(rnfPersonaSeleccionadaVer.getPrsId(), registro.getFcmtId(), rnfPeriodoAcademicoActivo.getPracId());
			}
			List<MateriaDto> listaModulosAgregar = new ArrayList<MateriaDto>(); 
			for (MateriaDto item : rnfListaEstudiantesRecalificacionEdicion) {
				List<Materia> listaMateriasModulos = null;
				try {
					listaMateriasModulos = new ArrayList<Materia>();
					listaMateriasModulos = servRnfMateriaServicio.listarTodosModulos(item.getMtrId());
					if(listaMateriasModulos.size()!=0){
						for (Materia materia : listaMateriasModulos) {
//							System.out.println(i);
//							System.out.println(materia.getMtrDescripcion());
							try {
								MallaCurricularMateria mlcrmtAux = servRnfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(materia.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								MallaCurricularParalelo mlcrprAux = servRnfMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), item.getPrlId());
								List<MateriaDto> listaModulos = servRnfMateriaDtoServicioJdbc.listarXmallaCurricularParalelo(mlcrprAux.getMlcrprId(),rnfPersonaSeleccionadaVer.getPrsId(),registro.getPracId());
								listaModulos.get(0).setMtrId(materia.getMtrId());
								listaModulos.get(0).setMtrDescripcion(listaModulos.get(0).getMtrDescripcion()+" - "+materia.getMtrDescripcion());
								if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()==-99){
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
								}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()>=28){
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()>8.8){
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
								}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()<8.8){
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
								listaModulosAgregar.addAll(listaModulos);
//								sumatoriaNotas = sumatoriaNotas.add(new BigDecimal(listaModulos.get(0).getClfNota1()).setScale(2, RoundingMode.CEILING));
//								sumatoriaAsistencia = sumatoriaAsistencia.add(new BigDecimal(listaModulos.get(0).getClfAsistencia1()).setScale(0, RoundingMode.CEILING));
//								sumatoriaAsistenciaDocente = sumatoriaAsistenciaDocente.add(new BigDecimal(listaModulos.get(0).getClfAsistenciaDocente1()).setScale(0, RoundingMode.CEILING));
							} catch (Exception e) {
								List<MateriaDto> listaModulos = new ArrayList<MateriaDto>();
								listaModulos.add(new MateriaDto());
								listaModulos.get(0).setMtrId(materia.getMtrId());
								listaModulos.get(0).setMtrDescripcion(item.getMtrDescripcion()+" - "+materia.getMtrDescripcion());
								listaModulos.get(0).setPrlDescripcion(item.getPrlDescripcion());
								listaModulos.get(0).setDtmtNumero(item.getDtmtNumero());
								listaModulos.get(0).setRcesEstado(item.getRcesEstado());
								listaModulos.get(0).setClfNotaFinalSemestre(item.getClfNotaFinalSemestre());
								listaModulos.get(0).setClfNota1(item.getClfNota1());
								listaModulos.get(0).setClfNota2(item.getClfNota2());
								listaModulos.get(0).setClfAsistencia1(item.getClfAsistencia1());
								listaModulos.get(0).setClfAsistencia2(item.getClfAsistencia2());
								listaModulos.get(0).setClfPromedioAsistencia(item.getClfPromedioAsistencia());
								listaModulos.get(0).setClfSumaP1P2(item.getClfSumaP1P2());
								try {
									if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()==-99){
										listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
									}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()>=28){
										listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
									}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()>8.8){
										listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
									}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()<8.8){
										listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								} catch (Exception e2) {
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
								}
								listaModulosAgregar.addAll(listaModulos);
							}
						}	
					}
				} catch (Exception e) {
				}
			}
			if(listaModulosAgregar.size()!=0){
				rnfListaEstudiantesRecalificacionEdicion.addAll(listaModulosAgregar);
				
			}
			rnfListaEstudiantesRecalificacion = new ArrayList<MateriaDto>();
			rnfListaEstudiantesRecalificacion.addAll(rnfListaEstudiantesRecalificacionEdicion);
			rnfListaEstudiantesRecalificacion= GeneralesUtilidades.quitarDuplicadosRecalificacion(rnfListaEstudiantesRecalificacion);
			rnfListaEstudiantesRecalificacionEdicion = GeneralesUtilidades.quitarDuplicadosRecalificacion(rnfListaEstudiantesRecalificacionEdicion);
			retorno="irVerNotasEstudiante";
		} catch (Exception e) {
		}
		return retorno;
	}
	
	public String irCancelar(){
		iniciarParametros();
		return "regresarListar";
	}
	
	/**
	 * Método que guarda la respuesta a la solicitud de tercera matricula en las materias solicitadas
	 * @return retorna - la navegación de la página listar estudiantes.
	 */
	public String guardar(){
		String retorno = null;
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			nrctfRegCliente  = idHostAux.concat(" "+ipLocalClienteAux);
			Date fechaActual = new Date();
			nrctfRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: RECALIFICACION");

			for (MateriaDto itemOriginal : rnfListaEstudiantesRecalificacionEdicion) {
				if(itemOriginal.getClfAsistencia1()>itemOriginal.getClfAsistenciaDocente1()){
					throw new Exception();
				}
				if(itemOriginal.getClfAsistencia2()!=null && itemOriginal.getClfAsistenciaDocente2()!=null){
						if(itemOriginal.getClfAsistencia2()>itemOriginal.getClfAsistenciaDocente2() ){
							throw new Exception();							
						}
				}
			}
			
			for (MateriaDto itemOriginal : rnfListaEstudiantesRecalificacionEdicion) {
				boolean op=true;
//				for (MateriaDto itemEditado : rnfListaEstudiantesRecalificacion) {
//					if(itemOriginal.getMtrId()==itemEditado.getMtrId()){
//						if(itemOriginal.getClfAsistencia1()!=itemEditado.getClfAsistencia1()
//								|| itemOriginal.getClfAsistencia2()!=itemEditado.getClfAsistencia2()
//								|| itemOriginal.getClfNota1()!=itemEditado.getClfNota1()
//								|| itemOriginal.getClfNota2()!=itemEditado.getClfNota2()
//								|| itemOriginal.getClfSupletorio()!=itemEditado.getClfSupletorio()
//								){
//							op= true;
//						}
//					}
//				}
				if(op){
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servRnfRecordEstudianteDtoServicioJdbc.buscarXRcesId(itemOriginal.getRcesId());
					EstudianteJdbcDto item = new EstudianteJdbcDto(); 
					item.setClfId(itemOriginal.getClfId());
					item.setRcesId(itemOriginal.getRcesId());
					item.setDtmtNumero(itemOriginal.getDtmtNumero());
					item.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					item.setClfAsistenciaEstudiante1(itemOriginal.getClfAsistencia1());
					item.setClfAsistenciaDocente1(itemOriginal.getClfAsistenciaDocente1());
					item.setClfNota1(new BigDecimal(itemOriginal.getClfNota1()));
					try {
						servRnfCalificacionbServicio.guardarEdicionNotasPrimerHemi(rcesAux, item, nrctfRegCliente+"RECALIFICACION");	
					} catch (Exception e) {
					}
					try {
						item.setClfNota2(new BigDecimal(itemOriginal.getClfNota2()));
						item.setClfAsistenciaEstudiante2(itemOriginal.getClfAsistencia2());
						item.setClfAsistenciaDocente2(itemOriginal.getClfAsistenciaDocente2());
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
						
							int promedioAsistencia = 0;
							promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
								item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
							if(promedioAsistencia == 1 || promedioAsistencia == 0){
							}else{// si el promedio de asistencia es menor a 80
								item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							}
							
					} catch (Exception e) {
					}
					try {
						item.setClfSupletorio(new BigDecimal(itemOriginal.getClfSupletorio()));	
						BigDecimal parametro2Aux  = BigDecimal.ZERO;
						parametro2Aux  = item.getClfSupletorio().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP);
						item.setClfParamRecuperacion2(parametro2Aux);
						BigDecimal parametro1Aux  = BigDecimal.ZERO;
						parametro1Aux = item.getClfSumaP1P2().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP);;
						item.setClfParamRecuperacion1(parametro1Aux);
						
						BigDecimal sumaParametros = BigDecimal.ZERO;
						sumaParametros = parametro2Aux.add(parametro1Aux);
//						item.setClfNotalFinalSemestre(sumaParametros.setScale(2, RoundingMode.HALF_UP));
//						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						int estadoRces = sumaParametros.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
						if(estadoRces == 1 || estadoRces ==0){
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
							item.setClfNotalFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP));
						}else{
							item.setClfNotalFinalSemestre(sumaParametros.setScale(2, RoundingMode.DOWN));
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
					} catch (Exception e) {
					}
					
					
					
					
					
					
						try {
							item.setClfNota2(new BigDecimal(itemOriginal.getClfNota2()));
							item.setClfAsistenciaEstudiante2(itemOriginal.getClfAsistencia2());
							item.setClfAsistenciaDocente2(itemOriginal.getClfAsistenciaDocente2());
							Materia mtrAux = servRnfMateriaServicio.buscarPorId(itemOriginal.getMtrId());
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);	
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
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() +  itemOriginal.getClfAsistenciaDocente2();
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
									item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
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
											if(promedioAsistencia == 1 || promedioAsistencia == 0 || 
													mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
												BigDecimal itemCost  = BigDecimal.ZERO;
												itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
												int comparador = 0;
												try {
													comparador = item.getClfParamRecuperacion2().compareTo(new BigDecimal(GeneralesConstantes.APP_ID_BASE));	
												} catch (Exception e) {
												}
												if(itemOriginal.getRcesIngresoNota3()!=0){
													item.setClfSupletorio(new BigDecimal(itemOriginal.getClfSupletorio()));
													item.setClfParamRecuperacion1(itemCost);
													item.setClfParamRecuperacion2(item.getClfSupletorio().multiply(new BigDecimal(1.2)).setScale(2, RoundingMode.HALF_UP));
													BigDecimal suma = item.getClfParamRecuperacion1().add(item.getClfParamRecuperacion2());
													comparador = suma.compareTo(new BigDecimal(27.5));
													if(comparador==0 || comparador ==1){
														item.setClfNotalFinalSemestre(suma.setScale(0, RoundingMode.HALF_UP));
														item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
													}else{
														item.setClfNotalFinalSemestre(suma.setScale(2, RoundingMode.HALF_UP));
														item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);	
													}
													
												}else{
													item.setClfParamRecuperacion1(itemCost);
													item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
													item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);	
												}
												
												
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
							try {
//								servRnfCalificacionbServicio.guardarEdicionNotasSegundoHemi(rcesAux, item, nrctfRegCliente);
								try {
									item.setClfSupletorio(new BigDecimal(itemOriginal.getClfSupletorio()));	
									BigDecimal parametro2Aux  = BigDecimal.ZERO;
									parametro2Aux  = item.getClfSupletorio().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP);
									item.setClfParamRecuperacion2(parametro2Aux);
									BigDecimal parametro1Aux  = BigDecimal.ZERO;
									parametro1Aux = item.getClfSumaP1P2().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP);;
									item.setClfParamRecuperacion1(parametro1Aux);
									
									BigDecimal sumaParametros = BigDecimal.ZERO;
									sumaParametros = parametro2Aux.add(parametro1Aux);
//									item.setClfNotalFinalSemestre(sumaParametros.setScale(2, RoundingMode.HALF_UP));
//									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									int estadoRces = sumaParametros.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
									if(estadoRces == 1 || estadoRces ==0){
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
										rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
										item.setClfNotalFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP));
									}else{
										item.setClfNotalFinalSemestre(sumaParametros.setScale(2, RoundingMode.DOWN));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
										rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								try {
									int promedioAsistencia = 0;
									promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
										// si el promedio de asistencia es mayor o igual a 80
									if(promedioAsistencia == 1 || promedioAsistencia == 0){
									}else{// si el promedio de asistencia es menor a 80
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								
								servRnfRecordEstudianteServicio.actualizaEstadoRces(item.getRcesId(), item.getRcesEstado());
									if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
										servRnfCalificacionbServicio.verificarModulos(rcesAux, item, nrctfRegCliente);
									}
							} catch (Exception e) {
							}
						} catch (Exception e) {
						}
						try {
							Materia mtrAux = servRnfMateriaServicio.buscarPorId(itemOriginal.getMtrId());
							item.setClfSupletorio(new BigDecimal(itemOriginal.getClfSupletorio()));	
							servRnfCalificacionbServicio.guardarNotasPregradoRecuperacion(rcesAux, item, nrctfRegCliente);
							if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
								servRnfCalificacionbServicio.verificarModulos(rcesAux, item, nrctfRegCliente);
							}
						} catch (Exception e) {
						}
				}
				//******************************************************************************
			//************************* ACA INICIA EL ENVIO DE MAIL CON ADJUNTO ************
			//******************************************************************************
			
//			try{
//				Connection connection = null;
//				Session session = null;
//				MessageProducer producer = null;
//				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",GeneralesConstantes.APP_NIO_ACTIVEMQ);
//				connection = connectionFactory.createConnection();
//				connection.start();
//				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//				Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
//				// Creamos un productor
//				producer = session.createProducer(destino);
//				
//				JasperReport jasperReport = null;
//				JasperPrint jasperPrint;
//				StringBuilder pathGeneralReportes = new StringBuilder();
//				pathGeneralReportes.append(FacesContext.getCurrentInstance()
//						.getExternalContext().getRealPath("/"));
//				
//				//*********************************************************//
//				//******PARAMETROS PARA RECTIFICACION 1ER HEMISEMESTR******//
//				//*********************************************************//
//				if(nrctfTipoRectificacion==1){
//					pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/primerHemi");
//					jasperReport = (JasperReport) JRLoader.loadObject(new File(
//							(pathGeneralReportes.toString() + "/reporteNota1HemiRectificacion.jasper")));
//				}
//				
//				if(nrctfTipoRectificacion==2){
//					pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/segundoHemi");
//					jasperReport = (JasperReport) JRLoader.loadObject(new File(
//							(pathGeneralReportes.toString() + "/reporteNota2Hemi2Rectificacion.jasper")));
//				}
//				
//				if(nrctfTipoRectificacion==3){
//					pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/recuperacion");
//					jasperReport = (JasperReport) JRLoader.loadObject(new File(
//							(pathGeneralReportes.toString() + "/reporteNotaRecuperacionRectificacion.jasper")));
//				}
//				
//				
//				List<Map<String, Object>> frmAdjuntoCampos = null;
//				Map<String, Object> frmAdjuntoParametros = null;
////				String facultadMail = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
////				String carreraMail = GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
//				
//				frmAdjuntoParametros = new HashMap<String, Object>();
//				SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
//				String fecha = formato.format(new Date());
//				frmAdjuntoParametros.put("fecha",fecha);
//				StringBuilder sbAsistenciaDocente = new StringBuilder();
//				frmAdjuntoParametros.put("nick", nrctfUsuario.getUsrNick());
//				frmAdjuntoParametros.put("docente", nrctfDocente.getPrsNombres()+" "+nrctfDocente.getPrsPrimerApellido()+" "+nrctfDocente.getPrsSegundoApellido());
//				
//				Carrera crrAux = new Carrera();
//				try {
//					crrAux = servNrctfCarreraServicio.buscarPorId(nrctfParaleloDtoEditar.getCrrId());
//					nrctfNomCarrera =  crrAux.getCrrDescripcion().toString();
//				} catch (CarreraNoEncontradoException | CarreraException e) {
//				}
//				for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
//					if(mtrAux.getMtrTipoMateria().getTimtId()==TipoMateriaConstantes.TIPO_MODULO_VALUE){
//						nrctfNomMateria = itemAux.getMtrDescripcion().toString()+" - "+mtrAux.getMtrDescripcion();
//					}else{
//						nrctfNomMateria = itemAux.getMtrDescripcion().toString();
//					}
//					nrctfNomParalelo = itemAux.getPrlDescripcion().toString();
//					nrctfNomNivel = itemAux.getNvlDescripcion().toString();
//					if(nrctfTipoRectificacion==1){
//						nrctfNomNotaRectificacion =("PRIMER HEMISEMESTRE"); 
//					}
//					if(nrctfTipoRectificacion==2){
//						nrctfNomNotaRectificacion =("SEGUNDO HEMISEMESTRE");
//					}
//					if(nrctfTipoRectificacion==3){
//						nrctfNomNotaRectificacion =("RECUPERACIÓN");
//					}
//					
//					frmAdjuntoParametros.put("periodo", itemAux.getPracDescripcion());
//					frmAdjuntoParametros.put("facultad", crrAux.getCrrDependencia().getDpnDescripcion());
//					frmAdjuntoParametros.put("carrera", crrAux.getCrrDetalle());
//					frmAdjuntoParametros.put("curso", itemAux.getNvlDescripcion());
//					frmAdjuntoParametros.put("paralelo", itemAux.getPrlDescripcion());
//					frmAdjuntoParametros.put("materia", itemAux.getMtrDescripcion());
//					
//					sbAsistenciaDocente.append(itemAux.getClfAsistenciaDocente1());
//					frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());	
//					break;
//				}
//				frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
//				frmAdjuntoParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
//				
//				frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
//				Map<String, Object> datoEstudiantes = null;
//				
//				//*********************************************************//
//				//******PARAMETROS PARA RECTIFICACION 1ER HEMISEMESTR******//
//				//*********************************************************//
//				int cont1 = 1;
//				if(nrctfTipoRectificacion==1){
//					for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
//						if(itemAux.getClfNota1()!=null ){
//							String nota = itemAux.getClfNota1().toString();
//							int puntoDecimalUbc = nota.indexOf('.');
//							int totalDecimales = nota.length() - puntoDecimalUbc -1;
//							if(puntoDecimalUbc==-1){
//								itemAux.setClfNota1String(nota+".00");
//							}else if (totalDecimales==1){
//								itemAux.setClfNota1String(nota+"0");
//							}else{
//								itemAux.setClfNota1String(nota);
//							}
//						}
//						StringBuilder sbNota1 = new StringBuilder();
//						StringBuilder sbAsistencia1 = new StringBuilder();
//						StringBuilder sbContador1 = new StringBuilder();
//						datoEstudiantes = new HashMap<String, Object>();
//						datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
//						datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
//						datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
//						datoEstudiantes.put("nombres", itemAux.getPrsNombres());
//						if(itemAux.getClfNota1() != null){
//							sbNota1.append(itemAux.getClfNota1String());
//							datoEstudiantes.put("nota1", sbNota1.toString());
//						}else{
//							sbNota1.append("-");
//							datoEstudiantes.put("nota1", sbNota1.toString());
//						}
//						if(itemAux.getClfAsistenciaEstudiante1() != null){
//							sbAsistencia1.append(itemAux.getClfAsistenciaEstudiante1());
//							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//						}else{
//							sbAsistencia1.append("-");
//							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//						}
//						sbContador1.append(cont1);
//						datoEstudiantes.put("numero", sbContador1.toString());
//						
//						frmAdjuntoCampos.add(datoEstudiantes);
//						cont1 = cont1 + 1;
//					}
//				}
//				//*********************************************************//
//				//******PARAMETROS PARA RECTIFICACION 2do HEMISEMESTR******//
//				//*********************************************************//
//				int cont2 = 1;
//				if(nrctfTipoRectificacion==2){
//					for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
//						StringBuilder sbNota2 = new StringBuilder();
//						StringBuilder sbAsistencia2 = new StringBuilder();
//						StringBuilder sbContador2 = new StringBuilder();
//						datoEstudiantes = new HashMap<String, Object>();
//						datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
//						datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
//						datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
//						datoEstudiantes.put("nombres", itemAux.getPrsNombres());
//						if(itemAux.getClfNota1() != null){
//							sbNota2.append(itemAux.getClfNota2());
//							datoEstudiantes.put("nota1", sbNota2.toString());
//						}else{
//							sbNota2.append("-");
//							datoEstudiantes.put("nota1", sbNota2.toString());
//						}
//						if(itemAux.getClfAsistenciaEstudiante2() != null){
//							sbAsistencia2.append(itemAux.getClfAsistenciaEstudiante2());
//							datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
//						}else{
//							sbAsistencia2.append("-");
//							datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
//						}
//						sbContador2.append(cont2);
//						datoEstudiantes.put("numero", sbContador2.toString());
//						
//						frmAdjuntoCampos.add(datoEstudiantes);
//						cont2 = cont2 +1;
//					}
//				}
//				//*********************************************************//
//				//******PARAMETROS PARA RECTIFICACION 2do HEMISEMESTR******//
//				//*********************************************************//
//				int cont3 = 1;
//				if(nrctfTipoRectificacion==3){
//					for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
//						StringBuilder sbNota3 = new StringBuilder();
//						StringBuilder sbContador3 = new StringBuilder();
//						datoEstudiantes = new HashMap<String, Object>();
//						datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
//						datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
//						datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
//						datoEstudiantes.put("nombres", itemAux.getPrsNombres());
//						if(itemAux.getClfNota1() != null){
//							sbNota3.append(itemAux.getClfNota2());
//							datoEstudiantes.put("nota1", sbNota3.toString());
//						}else{
//							sbNota3.append("-");
//							datoEstudiantes.put("nota1", sbNota3.toString());
//						}
//						sbContador3.append(cont3);
//						datoEstudiantes.put("numero", sbContador3.toString());
//						
//						frmAdjuntoCampos.add(datoEstudiantes);
//						cont3 = cont3 +1;
//					}
//				}
//				
//				
//				
//				JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);
//				
//				
//				jasperPrint = JasperFillManager.fillReport(jasperReport,
//					frmAdjuntoParametros, dataSource);
//				
//				
//				byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
////				AdjuntoDto adjuntoDto = new AdjuntoDto();
////				adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
////				adjuntoDto.setAdjunto(arreglo);
//				
//				//lista de correos a los que se enviara el mail
//				List<String> correosTo = new ArrayList<>();
//				correosTo.add(nrctfDocente.getPrsMailInstitucional());
//				
//				//lista de correos copia a los que se enviara el mail al director de carrera
////				List<String> correosCc = new ArrayList<>();
////				correosCc.add(nrctfDirCarrera.getPrsMailInstitucional());
//				
//				//path de la plantilla del mail
//				ProductorMailJson pmail = null;
//				StringBuilder sbCorreo= new StringBuilder();
//				//path de la plantilla del mail para el director de carrera 
//				ProductorMailJson pmailDirCarrera = null;
//				StringBuilder sbCorreoDirCarrera= new StringBuilder();
//				
//				
//				sbCorreo= GeneralesUtilidades.generarAsuntoRectificacionNotas(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
//						nrctfDocente.getPrsPrimerApellido()+" "+nrctfDocente.getPrsSegundoApellido()+" "+nrctfDocente.getPrsNombres() , GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera) , nrctfNomMateria , nrctfNomParalelo ,nrctfNomNotaRectificacion);
//				pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_REGISTRO_NOTAS,
//						sbCorreo.toString()
//						  , "admin", "dt1c201s", true, arreglo, "RegistroNotas."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
//				String jsonSt = pmail.generarMail();
//				Gson json = new Gson();
//				MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
//			// 	Iniciamos el envío de mensajes
//				ObjectMessage message = session.createObjectMessage(mailDto);
//				producer.send(message);
//				
//				
//				sbCorreoDirCarrera= GeneralesUtilidades.generarAsuntoRectificacionNotasDirCarrera((GeneralesUtilidades.generaStringParaCorreo(fecha.toString())),
//						nrctfDirCarrera.getPrsPrimerApellido()+" "+nrctfDirCarrera.getPrsSegundoApellido()+" "+nrctfDirCarrera.getPrsNombres() , 
//						nrctfDocente.getPrsPrimerApellido()+" "+nrctfDocente.getPrsSegundoApellido()+" "+nrctfDocente.getPrsNombres() ,
//						GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera) , 
//						nrctfNomMateria , 
//						nrctfNomNivel,
//						nrctfNomParalelo,
//						nrctfNomNotaRectificacion);
//				pmailDirCarrera = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_REGISTRO_NOTAS,
//						sbCorreoDirCarrera.toString()
//						  , "admin", "dt1c201s", true, arreglo, "RegistroNotas."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
//				String jsonStDirCarrera = pmailDirCarrera.generarMail();
//				Gson jsonDirCarrera = new Gson();
//				MailDto mailDtoDirCarrera = jsonDirCarrera.fromJson(jsonStDirCarrera, MailDto.class);
//			// 	Iniciamos el envío de mensajes
//				ObjectMessage messageDirCarrera = session.createObjectMessage(mailDtoDirCarrera);
//				producer.send(messageDirCarrera);
//				
//				
//				
//				}  catch (JMSException e) {
//				} catch (JRException e) {
//				} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
//				}
//			
//			//******************************************************************************
//			//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//			//
//					
			}
			retorno="regresarListar";
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Se han guardado correctamente las notas editadas");
			
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al guardar las notas del estudiante.");
		}
		
		
		
		iniciarParametros();
		return retorno;
	}
	
	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		 BigDecimal itemCost  = BigDecimal.ZERO;
	     itemCost  = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje)).divide(new BigDecimal(asitenciaDoc), 0, RoundingMode.HALF_UP);
	     return itemCost;
	}
	
	/**
	 * Método que llama al generar el reporte
	 * 
	 */
	
	public void llamarReporte(){
		//Activo el pdf
	   
		//GENERAR REPORTE
		 ReporteAprobacionSolicitudTerceraForm.generarReporteAprobacionTercera(astmfListaMateriasReporte, astmfDependencia, rnfCarreraVer, rnfPersonaSeleccionadaVer, rnfPeriodoAcademicoActivo, astmfUsuario,astmfObservacionReporte );
		 astmfActivaReportePDF=1;
	
	}
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros
	 */
	public void iniciarParametros(){
		//INSTANCIO EL PERIODO ACADÉMICO
		rnfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		rnfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		rnfEstudianteBuscar = new EstudianteJdbcDto();
		astmfListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rnfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rnfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		rnfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		rnfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		astmfValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		astmfClickGuardarResolver=0;
		astmfActivaReportePDF=0;
		astmfDesactivaBotonReporte= Boolean.TRUE;
		astmfDesactivaBotonGuardar=Boolean.FALSE;
		astmfObservacionFinal= null;
		astmfObservacionReporte= null;
		rnfListaEstudiantesRecalificacion = new ArrayList<MateriaDto>();
	}
	
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		rnfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		rnfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rnfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rnfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		astmfListaSolicitudesDto = null;
				
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		limpiar();
		return "irInicio";
	}
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton guardar  para verificar que cumpla las reglas antes de presentar el modal guardar
	 * @return retora null para para cualquier cosa 
	 */
	public String controlarClickGuardarResolver(){
		Boolean selecionadosTodos=false;
		Boolean dentroCronograma= false;
		astmfClickGuardarResolver = 0;
		//VERICO QUE EXISTAN MATERIAS 
		if(astmfListaMateriasSolicitadasDto.size() >= 1){
			for (SolicitudTerceraMatriculaDto materia : astmfListaMateriasSolicitadasDto) {
				if(materia.getRespuestaSolicitud()!=GeneralesConstantes.APP_ID_BASE){
					selecionadosTodos=true;
				}else{
					selecionadosTodos=false;
					break;					
				}
			}	
			
			
			
				
			//VERIFICO QUE ESTE ACTIVO EL CRONOGRAMA DE TERCERA MATRICULA
			dentroCronograma=verificarCronogramaSolicitarTerceraMatricula();
			if(dentroCronograma==true){
			    if(selecionadosTodos==true){
			    	
			    	
			   // 	||(GeneralesUtilidades.eliminarEspaciosEnBlanco(astmfObservacionFinal).length()!=0)
			    	if((getAstmfObservacionFinal() != null) && (getAstmfObservacionFinal().length() >=1)){
				       astmfClickGuardarResolver = 1;
			    	}else{
			    	astmfClickGuardarResolver = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Debe ingresar la observación");
			    	}
		     	}else{
				astmfClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.seleccionar.todos.validacion.exception")));
			     }
			}
		}else{
			astmfClickGuardarResolver = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.no.existe.materias.validacion.exception")));
		}
		return null;
	}

	
	public void desactivaModalGuardar(){
		astmfClickGuardarResolver = 0;
	}
	
	/**
	 * Verifica que el proceso de solicitar tercera matricula exista y se encuentre activo en la fecha actual
	 * @return retora True, si esta el proceso dentro de las fechas y false si no esta dentro de las fechas del cronograma
	 */
	public Boolean verificarCronogramaSolicitarTerceraMatricula() {
		Date fechaActual = new Date();
		Boolean retorno = false;
		// DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		try {
			astmfDependencia = servAstmfDependencialServicio.buscarFacultadXcrrId(rnfCarreraVer.getCrrId());
		} catch (DependenciaNoEncontradoException e1) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		if (astmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			astmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			astmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			astmfCronogramaActividad = servAstmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(rnfCarreraVer.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, astmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_VALIDACION_SOLICITUDES_TERCERA_MATRICULA_VALUE,
							ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.encontrado.exception")));
		}
		if (astmfCronogramaActividad != null) {
			if ((astmfCronogramaActividad.getPlcrFechaInicio() != null)
					&& (astmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(astmfCronogramaActividad.getPlcrFechaInicio(),
						astmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
					retorno = true;
				} else {
					retorno = false;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.habilitado.exception")));
				}
			} else {
				retorno = false;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.sin.fechas.exception")));
			}
		} else {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.encontrado.exception")));
		}
		return retorno;
	}
	
	
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(SolicitudTerceraMatriculaDto materiaSeleccionada){
		astmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(astmfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getSltrmtDocumentoSolicitud();
			try{
				File f=new File(GeneralesConstantes.APP_PATH_ARCHIVOS_TEMPORAL+materiaSeleccionada.getSltrmtDocumentoSolicitud());
				if(f.exists())
				{
				    f.delete();
				}
				f.createNewFile(); 
				FileObject destn = (FileObject) VFS.getManager().resolveFile(f.getAbsolutePath());
				UserAuthenticator auth = new StaticUserAuthenticator("", "produ", "12345.a");
				FileSystemOptions opts = new FileSystemOptions();

				DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
				FileObject fo = (FileObject) VFS.getManager().resolveFile(astmfArchivoSelSt,opts);
				destn.copyFrom( fo,Selectors.SELECT_SELF);
				 destn.close();
				return new DefaultStreamedContent((InputStream) destn,GeneralesUtilidades.obtenerContentType(astmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				astmfArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.no.encontrado.exception")));
			return null;
		}
		return null;
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas 
	 * @param materiaDeshabilitar - materiaDeshabilitar entidad seleccionada para deshabilitar el link
	 */
	public void deshabilitar(SolicitudTerceraMatriculaDto materiaDeshabilitar){
		for (SolicitudTerceraMatriculaDto item : astmfListaMateriasSolicitadasDto) {
			if(materiaDeshabilitar.getRcesId() == item.getRcesId()){
				item.setVisualizador(true);
				break;
			}
		}
	}
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public Usuario getAstmfUsuario() {
		return astmfUsuario;
	}

	public void setAstmfUsuario(Usuario astmfUsuario) {
		this.astmfUsuario = astmfUsuario;
	}
	


	


	public List<SolicitudTerceraMatriculaDto> getAstmfListaSolicitudesDto() {
		astmfListaSolicitudesDto = astmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):astmfListaSolicitudesDto;
		return astmfListaSolicitudesDto;
	}

	public void setAstmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> astmfListaSolicitudesDto) {
		this.astmfListaSolicitudesDto = astmfListaSolicitudesDto;
	}

	public PeriodoAcademico getAstmfPeriodoSolicitudesVer() {
		return astmfPeriodoSolicitudesVer;
	}

	public void setAstmfPeriodoSolicitudesVer(PeriodoAcademico astmfPeriodoSolicitudesVer) {
		this.astmfPeriodoSolicitudesVer = astmfPeriodoSolicitudesVer;
	}

	public List<SolicitudTerceraMatriculaDto> getAstmfListaMateriasSolicitadasDto() {
		astmfListaMateriasSolicitadasDto = astmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):astmfListaMateriasSolicitadasDto;
		return astmfListaMateriasSolicitadasDto;
	}

	public void setAstmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> astmfListaMateriasSolicitadasDto) {
		this.astmfListaMateriasSolicitadasDto = astmfListaMateriasSolicitadasDto;
	}

	public Integer getAstmfValidadorSeleccion() {
		return astmfValidadorSeleccion;
	}

	public void setAstmfValidadorSeleccion(Integer astmfValidadorSeleccion) {
		this.astmfValidadorSeleccion = astmfValidadorSeleccion;
	}

	public Integer getAstmfClickGuardarResolver() {
		return astmfClickGuardarResolver;
	}

	
	public void setAstmfClickGuardarResolver(Integer astmfClickGuardarResolver) {
		this.astmfClickGuardarResolver = astmfClickGuardarResolver;
	}
	

	
	public Integer getAstmfActivaReportePDF() {
		return astmfActivaReportePDF;
	}

	public void setAstmfActivaReportePDF(Integer astmfActivaReportePDF) {
		this.astmfActivaReportePDF = astmfActivaReportePDF;
	}

	public List<PeriodoAcademico> getAstmfListaPeriodoAcademicoBusq() {
		astmfListaPeriodoAcademicoBusq = astmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):astmfListaPeriodoAcademicoBusq;
		return astmfListaPeriodoAcademicoBusq;
	}

	public void setAstmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> astmfListaPeriodoAcademicoBusq) {
		this.astmfListaPeriodoAcademicoBusq = astmfListaPeriodoAcademicoBusq;
	}

	public Integer getAstmfTipoCronograma() {
		return astmfTipoCronograma;
	}

	public void setAstmfTipoCronograma(Integer astmfTipoCronograma) {
		this.astmfTipoCronograma = astmfTipoCronograma;
	}

	public Dependencia getAstmfDependencia() {
		return astmfDependencia;
	}

	public void setAstmfDependencia(Dependencia astmfDependencia) {
		this.astmfDependencia = astmfDependencia;
	}

	public CronogramaActividadJdbcDto getAstmfCronogramaActividad() {
		return astmfCronogramaActividad;
	}

	public void setAstmfCronogramaActividad(CronogramaActividadJdbcDto astmfCronogramaActividad) {
		this.astmfCronogramaActividad = astmfCronogramaActividad;
	}


	public String getAstmfArchivoSelSt() {
		return astmfArchivoSelSt;
	}

	public void setAstmfArchivoSelSt(String astmfArchivoSelSt) {
		this.astmfArchivoSelSt = astmfArchivoSelSt;
	}

	public FichaInscripcionDto getAstmfFichaInscripcion() {
		return astmfFichaInscripcion;
	}

	public void setAstmfFichaInscripcion(FichaInscripcionDto astmfFichaInscripcion) {
		this.astmfFichaInscripcion = astmfFichaInscripcion;
	}

	public List<SolicitudTerceraMatriculaDto> getAstmfListaMateriasReporte() {
		astmfListaMateriasReporte = astmfListaMateriasReporte==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):astmfListaMateriasReporte;
		return astmfListaMateriasReporte;
	}

	public void setAstmfListaMateriasReporte(List<SolicitudTerceraMatriculaDto> astmfListaMateriasReporte) {
		this.astmfListaMateriasReporte = astmfListaMateriasReporte;
	}

	public Boolean getAstmfDesactivaBotonReporte() {
		return astmfDesactivaBotonReporte;
	}

	public void setAstmfDesactivaBotonReporte(Boolean astmfDesactivaBotonReporte) {
		this.astmfDesactivaBotonReporte = astmfDesactivaBotonReporte;
	}

	public Boolean getAstmfDesactivaBotonGuardar() {
		return astmfDesactivaBotonGuardar;
	}

	public void setAstmfDesactivaBotonGuardar(Boolean astmfDesactivaBotonGuardar) {
		this.astmfDesactivaBotonGuardar = astmfDesactivaBotonGuardar;
	}

	public String getAstmfObservacionFinal() {
		return astmfObservacionFinal;
	}

	public void setAstmfObservacionFinal(String astmfObservacionFinal) {
		this.astmfObservacionFinal = astmfObservacionFinal;
	}

	public String getAstmfObservacionReporte() {
		return astmfObservacionReporte;
	}

	public void setAstmfObservacionReporte(String astmfObservacionReporte) {
		this.astmfObservacionReporte = astmfObservacionReporte;
	}
///////////////////////////////////////////////////////
	public PeriodoAcademico getRnfPeriodoAcademicoBuscar() {
		return rnfPeriodoAcademicoBuscar;
	}

	public void setRnfPeriodoAcademicoBuscar(PeriodoAcademico rnfPeriodoAcademicoBuscar) {
		this.rnfPeriodoAcademicoBuscar = rnfPeriodoAcademicoBuscar;
	}

	public List<CarreraDto> getRnfListCarreraDtoBusq() {
		return rnfListCarreraDtoBusq;
	}

	public void setRnfListCarreraDtoBusq(List<CarreraDto> rnfListCarreraDtoBusq) {
		this.rnfListCarreraDtoBusq = rnfListCarreraDtoBusq;
	}

	public PeriodoAcademico getRnfPeriodoAcademicoActivo() {
		return rnfPeriodoAcademicoActivo;
	}

	public void setRnfPeriodoAcademicoActivo(PeriodoAcademico rnfPeriodoAcademicoActivo) {
		this.rnfPeriodoAcademicoActivo = rnfPeriodoAcademicoActivo;
	}

	public CarreraDto getRnfCarreraDtoBuscar() {
		return rnfCarreraDtoBuscar;
	}

	public void setRnfCarreraDtoBuscar(CarreraDto rnfCarreraDtoBuscar) {
		this.rnfCarreraDtoBuscar = rnfCarreraDtoBuscar;
	}

	public EstudianteJdbcDto getRnfEstudianteBuscar() {
		return rnfEstudianteBuscar;
	}

	public void setRnfEstudianteBuscar(EstudianteJdbcDto rnfEstudianteBuscar) {
		this.rnfEstudianteBuscar = rnfEstudianteBuscar;
	}

	public List<MateriaDto> getRnfListMateriaBusq() {
		return rnfListMateriaBusq;
	}

	public void setRnfListMateriaBusq(List<MateriaDto> rnfListMateriaBusq) {
		this.rnfListMateriaBusq = rnfListMateriaBusq;
	}

	public List<MateriaDto> getRnfListaEstudiantesRecalificacion() {
		return rnfListaEstudiantesRecalificacion;
	}

	public void setRnfListaEstudiantesRecalificacion(List<MateriaDto> rnfListaEstudiantesRecalificacion) {
		this.rnfListaEstudiantesRecalificacion = rnfListaEstudiantesRecalificacion;
	}

	public Carrera getRnfCarreraVer() {
		return rnfCarreraVer;
	}

	public void setRnfCarreraVer(Carrera rnfCarreraVer) {
		this.rnfCarreraVer = rnfCarreraVer;
	}

	public Persona getRnfPersonaSeleccionadaVer() {
		return rnfPersonaSeleccionadaVer;
	}

	public void setRnfPersonaSeleccionadaVer(Persona rnfPersonaSeleccionadaVer) {
		this.rnfPersonaSeleccionadaVer = rnfPersonaSeleccionadaVer;
	}

	public List<MateriaDto> getRnfListaEstudiantesRecalificacionEdicion() {
		return rnfListaEstudiantesRecalificacionEdicion;
	}

	public void setRnfListaEstudiantesRecalificacionEdicion(List<MateriaDto> rnfListaEstudiantesRecalificacionEdicion) {
		this.rnfListaEstudiantesRecalificacionEdicion = rnfListaEstudiantesRecalificacionEdicion;
	}

	public String getNrctfRegCliente() {
		return nrctfRegCliente;
	}

	public void setNrctfRegCliente(String nrctfRegCliente) {
		this.nrctfRegCliente = nrctfRegCliente;
	}

		
	
	

}
