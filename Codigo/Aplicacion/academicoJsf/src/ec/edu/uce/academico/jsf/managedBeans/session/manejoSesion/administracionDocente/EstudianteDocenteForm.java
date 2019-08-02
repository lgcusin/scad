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
   
 ARCHIVO:     EstudianteDocenteForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes por docente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-MARZ-2017 			Dennis Collaguazo                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionDocente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteDocenteForm.
 * Managed Bean que administra los estudiantes para la visualización por docentes.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="estudianteDocenteForm")
@SessionScoped
public class EstudianteDocenteForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario edfUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto edfDocente;

	//PARA BUSQUEDA
	private EstudianteJdbcDto edfEstudianteBuscar;
	private List<CarreraDto> edfListCarreraBusq;
	private List<DocenteJdbcDto> edfListCarreraDocenteBusq;
	private List<DocenteJdbcDto> edfNivelDocenteDtoBusq;
	private List<NivelDto> edfListNivelBusq;
	private NivelDto edfNivelDtoBusq;
	private List<DocenteJdbcDto> edfListMateriaBusq;
	private MateriaDto edfMateriaBusq;
	private List<EstudianteJdbcDto> edfListEstudianteBusq;
	private List<PeriodoAcademicoDto> edfListPeriodoAcademicoBusq;
	private List<DocenteJdbcDto> edfListParaleloBusq;

	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto edfEstudianteVer;
	private UbicacionDto edfCantonResidenciaVer;
	private UbicacionDto edfPaisResidenciaVer;
	private UbicacionDto edfProvinciaResidenciaVer;
	
	//PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private String edfActivacion;

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
	private CarreraDtoServicioJdbc servEdfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servEdfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servEdfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servEdfEstudianteDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servEdfUbicacionDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servEdfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servEdfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servEdfParaleloDtoServicioJdbc;
	@EJB
	private MateriaServicio servEdfMateriaServicio;
	
	
	
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiante(Usuario usuario){
		edfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			//LISTO LOS PERIODOS ACADEMICOS
			//edfListPeriodoAcademicoBusq = servEdfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			edfListPeriodoAcademicoBusq = servEdfPeriodoAcademicoDtoServicioJdbc.listarTodos();
			edfListPeriodoAcademicoBusq = servEdfPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(GeneralesConstantes.APP_ID_BASE);
			retorno = "irListarEstudianteDocente";
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	/**
	 * Método que permite buscar la carrera por el periódo academico
	 * @param idPeriodo -  idPeriodo seleccionado para la busqueda
	 */
	public void llenarCarrera(int idPeriodo){
		idPeriodo = edfEstudianteBuscar.getPracId();
		edfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		edfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		edfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		edfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
//		edfListCarreraBusq = null;
//		edfListNivelBusq = null;
		edfNivelDocenteDtoBusq =  null;
		edfListParaleloBusq = null;	
		edfListMateriaBusq = null;
		edfListCarreraDocenteBusq = null;
		edfListEstudianteBusq = null;	
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				//edfListCarreraBusq = servEdfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(edfUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				List<DocenteJdbcDto> listCarreras = new ArrayList<>();
				listCarreras =servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(edfUsuario.getUsrIdentificacion(),idPeriodo);
				
				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(edfUsuario.getUsrIdentificacion(),idPeriodo);
				
				//listo carreras compartidas 
				List<DocenteJdbcDto> listCarrCompartida = new ArrayList<>();
				listCarrCompartida = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprId(listMlcrpr);
				
				edfListCarreraDocenteBusq = new ArrayList<>();
				
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto itemCarr : listCarreras) {
					edfListCarreraDocenteBusq.add(itemCarr);
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
							edfListCarreraDocenteBusq.add(item);
						}
					}	
				}
				Collections.sort(edfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
						return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
					}
				});
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.carrera.id.Periodo.validacion.exception")));
			}
//		} catch (CarreraDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (CarreraDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.docente.llenar.carrera.id.Periodo.validacion.no.encontrado.exception")));
//		} 
	} catch (DetallePuestoDtoJdbcException e) {
		FacesUtil.mensajeError(e.getMessage());
	} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.docente.llenar.carrera.id.Periodo.validacion.no.encontrado.exception")));
	}
	}
	
	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * @param idCarrera - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idMlcrpr){
//		idCarrera = edfEstudianteBuscar.getCrrId();
		try {
			//buscar id carrera
//			edfEstudianteBuscar.setCrrId((servEdfDocenteDtoServicioJdbc.buscarCarrerasXMlcrprId(idMlcrpr)).getCrrId());
			edfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
			edfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
			edfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//		edfListNivelBusq = null;
			edfNivelDocenteDtoBusq =  null;
			edfListParaleloBusq = null;
			edfListMateriaBusq = null;
			edfListEstudianteBusq = null;

			if(idMlcrpr != GeneralesConstantes.APP_ID_BASE){
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				//edfDocente = servEdfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodo(edfUsuario.getUsrId(), edfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, edfEstudianteBuscar.getPracId());
				edfDocente = servEdfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(edfUsuario.getUsrId(), edfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, edfEstudianteBuscar.getPracId());
				//LISTO LOS NIVELES
				//edfListNivelBusq = servEdfNivelDtoServicioJdbc.listarNivelXPeriodoXCarrera(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfDocente.getDtpsId());
				List<DocenteJdbcDto> listNiveles = new ArrayList<>();
				listNiveles = servEdfDocenteDtoServicioJdbc.buscarNivelesDocenteXPrac(edfEstudianteBuscar.getCrrId(),edfUsuario.getUsrIdentificacion(),edfEstudianteBuscar.getPracId());
//				edfNivelDocenteDtoBusq = servEdfDocenteDtoServicioJdbc.buscarNivelesMlcrprId(idMlcrpr);
				
				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(edfUsuario.getUsrIdentificacion(),edfEstudianteBuscar.getPracId());
				
				//listo niveles compartidas 
				List<DocenteJdbcDto> listNivelCompartida = new ArrayList<>();
				listNivelCompartida = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprIdPrinci(listMlcrpr);
				
				edfNivelDocenteDtoBusq = new ArrayList<>();
				
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto item : listNiveles) {
					edfNivelDocenteDtoBusq.add(item);
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
							if(edfEstudianteBuscar.getCrrId() == item.getCrrId()){
								edfNivelDocenteDtoBusq.add(item);
							}
						}
//						if(edfEstudianteBuscar.getCrrId() == item.getCrrId()){
//							edfNivelDocenteDtoBusq.add(item);
//						}
					}	
				}
				Collections.sort(edfNivelDocenteDtoBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
						return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
					}
				});
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
			}
			//} catch (NivelDtoJdbcException e) {
			//	FacesUtil.mensajeError(e.getMessage());
			//} catch (NivelDtoJdbcNoEncontradoException e) {
			//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
		} catch (DetallePuestoDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.detalle.puesto.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que permite buscar la lista de paralelos por el id del nivel
	 * @param idNivel - idNivel seleccionado para la busqueda
	 */
	public void llenarParalelo(int idNivel){
		idNivel = edfEstudianteBuscar.getNvlId();
		edfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		edfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		edfListParaleloBusq = null;
		edfListMateriaBusq = null;
		edfListEstudianteBusq = null;
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS PARALELOS
				List<DocenteJdbcDto> listParalelos = new ArrayList<>();
//				edfListParaleloBusq = servEdfParaleloDtoServicioJdbc.listarXperiodoXcarreraXnivelXdocente(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfDocente.getDtpsId());
				listParalelos = servEdfDocenteDtoServicioJdbc.listarXperiodoXcarreraXnivelXdocente(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfDocente.getDtpsId());
				
				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(edfUsuario.getUsrIdentificacion(),edfEstudianteBuscar.getPracId());
				
				//listo paralelos compartidas 
				List<DocenteJdbcDto> listParaleloCompartida = new ArrayList<>();
				listParaleloCompartida = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprIdPrinci(listMlcrpr);
				
				edfListParaleloBusq = new ArrayList<>();
				
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto item : listParalelos) {
					edfListParaleloBusq.add(item);
				}
				
				if(listParaleloCompartida.size() > 0){
					//asigno carreras compartidas
					boolean igual = false;
					for (DocenteJdbcDto item : listParaleloCompartida) {
						igual = false;
						for (DocenteJdbcDto itemAux : listParalelos) {
							if(item.getPrlId() == itemAux.getPrlId()){
								igual = true;
							}
						}
						if(igual){
							continue;
						}else{
							if(edfEstudianteBuscar.getCrrId() == item.getCrrId() && edfEstudianteBuscar.getNvlId() == item.getNvlId()){
								edfListParaleloBusq.add(item);
							}
						}
//						if(edfEstudianteBuscar.getCrrId() == item.getCrrId()){
//							edfNivelDocenteDtoBusq.add(item);
//						}
					}	
				}
				Collections.sort(edfListParaleloBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
						return obj1.getPrlDescripcion().compareTo(obj2.getPrlDescripcion());
					}
				});
				for (DocenteJdbcDto item : edfListParaleloBusq) {
					System.out.println("paralelo id "+item.getPrlId());
					System.out.println("paralelo descripcion "+item.getPrlDescripcion());
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idParalelo){
		idParalelo = edfEstudianteBuscar.getPrlId();
		edfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		edfListMateriaBusq = null;
		edfListEstudianteBusq = null;
		try {
			if(idParalelo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				List<DocenteJdbcDto> listMaterias = new ArrayList<>();
//				edfListMateriaBusq = servEdfDocenteDtoServicioJdbc.listarXperiodoXcarreraXnivelXparaleloXdocente(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfEstudianteBuscar.getPrlId(), edfDocente.getDtpsId());
				listMaterias = servEdfDocenteDtoServicioJdbc.listarXperiodoXcarreraXnivelXparaleloXdocente(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfEstudianteBuscar.getPrlId(), edfDocente.getDtpsId());
			
				//listo mallas curriculares 
				List<DocenteJdbcDto> listMlcrpr = new ArrayList<>();
				listMlcrpr = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPracRep(edfUsuario.getUsrIdentificacion(),edfEstudianteBuscar.getPracId());
				
				//listo materias compartidas 
				List<DocenteJdbcDto> listMateriasCompartida = new ArrayList<>();
				listMateriasCompartida = servEdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPraXListMlcrprIdPrinci(listMlcrpr);
				
				edfListMateriaBusq = new ArrayList<>();
				
				//asigno carreras con detalle puesto
				for (DocenteJdbcDto item : listMaterias) {
					edfListMateriaBusq.add(item);
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
							if(edfEstudianteBuscar.getCrrId() == item.getCrrId() && edfEstudianteBuscar.getPrlId() == item.getPrlId()){
								edfListMateriaBusq.add(item);
							}
						}
//						if(edfEstudianteBuscar.getCrrId() == item.getCrrId()){
//							edfNivelDocenteDtoBusq.add(item);
//						}
					}	
				}
				Collections.sort(edfListMateriaBusq, new Comparator<DocenteJdbcDto>() {
					public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
						return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
					}
				});
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que limpia la lista de resultado luego de cambiar el foco
	 */
	public void limpiarListaResultados(){
		edfListEstudianteBusq = null;
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
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		//LISTO LOS PERIODOS ACADEMICOS
		try {
//			edfListPeriodoAcademicoBusq = servEdfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			edfListPeriodoAcademicoBusq = servEdfPeriodoAcademicoDtoServicioJdbc.listarTodos();
			edfListPeriodoAcademicoBusq = servEdfPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(GeneralesConstantes.APP_ID_BASE);
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.limpiar.periodo.academico.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		edfListEstudianteBusq = null;
		ReporteDocenteForm reporteDocente = new ReporteDocenteForm();
		try {
//			if(edfEstudianteBuscar.getPracId() != GeneralesConstantes.APP_ID_BASE 
//					&& edfEstudianteBuscar.getCrrId() != GeneralesConstantes.APP_ID_BASE 
//					&& edfEstudianteBuscar.getNvlId() != GeneralesConstantes.APP_ID_BASE 
//					&& edfEstudianteBuscar.getPrlId() != GeneralesConstantes.APP_ID_BASE
//					&& edfEstudianteBuscar.getMtrId() != GeneralesConstantes.APP_ID_BASE){
//				edfActivacion = "false";
//				
//				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				edfListEstudianteBusq =  servEdfEstudianteDtoServicioJdbc.buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacion(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfEstudianteBuscar.getPrlId(), edfEstudianteBuscar.getMtrId(), edfDocente.getDtpsId(), edfEstudianteBuscar.getPrsPrimerApellido(), edfEstudianteBuscar.getPrsIdentificacion());
//				//GENERA EL REPORTE
//				reporteDocente.generarReporteDocente(edfListEstudianteBusq);
//			}else{
//				limpiar();
//				FacesUtil.mensajeError("Debe seleccionar los párametros de búsqueda");
//			}
			
			
			if(edfEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.periodo.validacion.exception")));
			}else if(edfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			}else if(edfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else if(edfEstudianteBuscar.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.paralelo.validacion.exception")));
			}else if(edfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			}else{
				edfActivacion = "false";
				//BUSCO LA CARREA CON EL SUB ID
				Materia materia = new Materia();
				materia = servEdfMateriaServicio.buscarPorId(edfEstudianteBuscar.getMtrId());
				int mtrId = GeneralesConstantes.APP_ID_BASE;
				if(materia.getMtrMateria() != null){
					mtrId = materia.getMtrMateria().getMtrId();
				}else{
					mtrId = materia.getMtrId();
				}
				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				edfListEstudianteBusq =  servEdfEstudianteDtoServicioJdbc.buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacionPagados(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfEstudianteBuscar.getPrlId(), edfEstudianteBuscar.getMtrId(), edfDocente.getDtpsId(), edfEstudianteBuscar.getPrsPrimerApellido(), edfEstudianteBuscar.getPrsIdentificacion());
//				edfListEstudianteBusq =  servEdfEstudianteDtoServicioJdbc.buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacionPagadosAlterno(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfEstudianteBuscar.getPrlId(), edfEstudianteBuscar.getMtrId(), edfDocente.getDtpsId(), edfEstudianteBuscar.getPrsPrimerApellido(), edfEstudianteBuscar.getPrsIdentificacion());
				edfListEstudianteBusq =  servEdfEstudianteDtoServicioJdbc.buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacionPagadosNoRetirados(edfEstudianteBuscar.getPracId(), edfEstudianteBuscar.getCrrId(), edfEstudianteBuscar.getNvlId(), edfEstudianteBuscar.getPrlId(), mtrId, edfDocente.getDtpsId(), edfEstudianteBuscar.getPrsPrimerApellido(), edfEstudianteBuscar.getPrsIdentificacion());
				//GENERA EL REPORTE
				reporteDocente.generarReporteDocente(edfListEstudianteBusq);
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.no.encontrado.exception")));
		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Redirecciona a la pagina de ver datos completos del estudiante
	 * @return Navegacion a la página de visualización de datos del estudiante.
	 */
	public String verDatosEstudiante(EstudianteJdbcDto estudiante) {
		String retorno = null;
		try {
			//DATOS DE ESTUDIANTE
			edfEstudianteVer = servEdfEstudianteDtoServicioJdbc.buscarEstudianteXId(estudiante.getPrsId());
			if(edfEstudianteVer != null){
				//CANTON DE RESIDENCIA
				edfCantonResidenciaVer = servEdfUbicacionDtoServicioJdbc.buscarXId(edfEstudianteVer.getUbcCantonId());
				UbicacionDto ubicacionAux =  null;
				try {
					ubicacionAux =  servEdfUbicacionDtoServicioJdbc.buscarPadreXId(edfEstudianteVer.getUbcCantonId());
				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					ubicacionAux =  null;
				}
				//verfifica si es pais o canton
				if(ubicacionAux == null){//No es ecuador
					edfPaisResidenciaVer = edfCantonResidenciaVer;
					edfProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					edfProvinciaResidenciaVer.setUbcDescripcion("N/A");
					edfCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					edfCantonResidenciaVer.setUbcDescripcion("N/A");
				}else{
					edfProvinciaResidenciaVer = servEdfUbicacionDtoServicioJdbc.buscarPadreXId(edfCantonResidenciaVer.getUbcId());
					edfPaisResidenciaVer = servEdfUbicacionDtoServicioJdbc.buscarPadreXId(edfProvinciaResidenciaVer.getUbcId());
				}
			}else{
				FacesUtil.mensajeError("No se encontró la información del estudiante");
			}
			retorno = "irVerDatosEstudiante";
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.no.encontrado.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.ubicacion.no.encontrado.exception")));
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.ubicacion.exception")));
		}
		return retorno;
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		edfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO ID
		edfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		edfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL ID
		edfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO ID
		edfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA MATERIA ID
		edfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		edfEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		edfEstudianteBuscar.setPrsPrimerApellido("");
		//ANULO LA LISTA DE PERIODOS ACADEMICOS
		edfListPeriodoAcademicoBusq = null;
		//ANULO LA LISTA DE CARRERAS
//		edfListCarreraBusq = null;
		edfListCarreraDocenteBusq = null;
		//ANULO LA LISTA DE NIVEL
//		edfListNivelBusq = null;
		edfNivelDocenteDtoBusq = null;
		//ANULO LA LISTA DE PARALELOS
		edfListParaleloBusq = null;
		//ANULO LA LISTA DE MATERIAS
		edfListMateriaBusq = null;
		//ANULO LA LISTA DE ESTUDIANTES
		edfListEstudianteBusq = null;
		//INICIALIZO LA VARIABLE CON VERDADERO
		
		edfActivacion = "true";
	}
	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada(){
		edfActivacion = "true";
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getEdfUsuario() {
		return edfUsuario;
	}
	public void setEdfUsuario(Usuario edfUsuario) {
		this.edfUsuario = edfUsuario;
	}
	public DocenteJdbcDto getEdfDocente() {
		return edfDocente;
	}
	public void setEdfDocente(DocenteJdbcDto edfDocente) {
		this.edfDocente = edfDocente;
	}
	public EstudianteJdbcDto getEdfEstudianteBuscar() {
		return edfEstudianteBuscar;
	}
	public void setEdfEstudianteBuscar(EstudianteJdbcDto edfEstudianteBuscar) {
		this.edfEstudianteBuscar = edfEstudianteBuscar;
	}
	public List<CarreraDto> getEdfListCarreraBusq() {
		edfListCarreraBusq = edfListCarreraBusq==null?(new ArrayList<CarreraDto>()):edfListCarreraBusq;
		return edfListCarreraBusq;
	}
	public void setEdfListCarreraBusq(List<CarreraDto> edfListCarreraBusq) {
		this.edfListCarreraBusq = edfListCarreraBusq;
	}
	public List<NivelDto> getEdfListNivelBusq() {
		edfListNivelBusq = edfListNivelBusq==null?(new ArrayList<NivelDto>()):edfListNivelBusq;
		return edfListNivelBusq;
	}
	public void setEdfListNivelBusq(List<NivelDto> edfListNivelBusq) {
		this.edfListNivelBusq = edfListNivelBusq;
	}
	public NivelDto getEdfNivelDtoBusq() {
		return edfNivelDtoBusq;
	}
	public void setEdfNivelDtoBusq(NivelDto edfNivelDtoBusq) {
		this.edfNivelDtoBusq = edfNivelDtoBusq;
	}
	public List<DocenteJdbcDto> getEdfListMateriaBusq() {
		edfListMateriaBusq = edfListMateriaBusq==null?(new ArrayList<DocenteJdbcDto>()):edfListMateriaBusq;
		return edfListMateriaBusq;
	}
	public void setEdfListMateriaBusq(List<DocenteJdbcDto> edfListMateriaBusq) {
		this.edfListMateriaBusq = edfListMateriaBusq;
	}
	public MateriaDto getEdfMateriaBusq() {
		return edfMateriaBusq;
	}
	public void setEdfMateriaBusq(MateriaDto edfMateriaBusq) {
		this.edfMateriaBusq = edfMateriaBusq;
	}
	public List<EstudianteJdbcDto> getEdfListEstudianteBusq() {
		edfListEstudianteBusq = edfListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):edfListEstudianteBusq;
		return edfListEstudianteBusq;
	}
	public void setEdfListEstudianteBusq(List<EstudianteJdbcDto> edfListEstudianteBusq) {
		this.edfListEstudianteBusq = edfListEstudianteBusq;
	}
	public List<PeriodoAcademicoDto> getEdfListPeriodoAcademicoBusq() {
		edfListPeriodoAcademicoBusq = edfListPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademicoDto>()):edfListPeriodoAcademicoBusq;
		return edfListPeriodoAcademicoBusq;
	}
	public void setEdfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> edfListPeriodoAcademicoBusq) {
		this.edfListPeriodoAcademicoBusq = edfListPeriodoAcademicoBusq;
	}
	public List<DocenteJdbcDto> getEdfListParaleloBusq() {
		edfListParaleloBusq = edfListParaleloBusq==null?(new ArrayList<DocenteJdbcDto>()):edfListParaleloBusq;
		return edfListParaleloBusq;
	}
	public void setEdfListParaleloBusq(List<DocenteJdbcDto> edfListParaleloBusq) {
		this.edfListParaleloBusq = edfListParaleloBusq;
	}
	public EstudianteJdbcDto getEdfEstudianteVer() {
		return edfEstudianteVer;
	}
	public void setEdfEstudianteVer(EstudianteJdbcDto edfEstudianteVer) {
		this.edfEstudianteVer = edfEstudianteVer;
	}
	public UbicacionDto getEdfCantonResidenciaVer() {
		return edfCantonResidenciaVer;
	}
	public void setEdfCantonResidenciaVer(UbicacionDto edfCantonResidenciaVer) {
		this.edfCantonResidenciaVer = edfCantonResidenciaVer;
	}
	public UbicacionDto getEdfPaisResidenciaVer() {
		return edfPaisResidenciaVer;
	}
	public void setEdfPaisResidenciaVer(UbicacionDto edfPaisResidenciaVer) {
		this.edfPaisResidenciaVer = edfPaisResidenciaVer;
	}
	public UbicacionDto getEdfProvinciaResidenciaVer() {
		return edfProvinciaResidenciaVer;
	}
	public void setEdfProvinciaResidenciaVer(UbicacionDto edfProvinciaResidenciaVer) {
		this.edfProvinciaResidenciaVer = edfProvinciaResidenciaVer;
	}
	public String getEdfActivacion() {
		return edfActivacion;
	}
	public void setEdfActivacion(String edfActivacion) {
		this.edfActivacion = edfActivacion;
	}

	public List<DocenteJdbcDto> getEdfListCarreraDocenteBusq() {
		edfListCarreraDocenteBusq = edfListCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):edfListCarreraDocenteBusq;
		return edfListCarreraDocenteBusq;
	}

	public void setEdfListCarreraDocenteBusq(List<DocenteJdbcDto> edfListCarreraDocenteBusq) {
		this.edfListCarreraDocenteBusq = edfListCarreraDocenteBusq;
	}

	public List<DocenteJdbcDto> getEdfNivelDocenteDtoBusq() {
		edfNivelDocenteDtoBusq = edfNivelDocenteDtoBusq==null?(new ArrayList<DocenteJdbcDto>()):edfNivelDocenteDtoBusq;
		return edfNivelDocenteDtoBusq;
	}

	public void setEdfNivelDocenteDtoBusq(List<DocenteJdbcDto> edfNivelDocenteDtoBusq) {
		this.edfNivelDocenteDtoBusq = edfNivelDocenteDtoBusq;
	}
	
	
	
}
