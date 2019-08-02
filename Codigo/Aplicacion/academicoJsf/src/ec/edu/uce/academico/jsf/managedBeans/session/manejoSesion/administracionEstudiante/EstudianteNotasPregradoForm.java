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
   
 ARCHIVO:     EstudianteNotasPregradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la consulta de notas del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-06-2017			 Marcelo Quishpe                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionEstudiante;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EstudianteNotasPregradoForm. 
 * Bean de sesion que maneja la consulta de notas del estudiante.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "estudianteNotasPregradoForm")
@SessionScoped
public class EstudianteNotasPregradoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario enpfUsuario;
	private PeriodoAcademico enpfPeriodoAcademicoBuscar;
	private CarreraDto enpfCarreraDtoBuscar;
	private List<PeriodoAcademico> enpfListPeriodoAcademico;
	private List<CarreraDto> enpfListCarreraDto;
	private List<MateriaDto> enpfListMateriaDto;	
	private List<FichaMatriculaDto> enpfListMatriculaDto;
	private Persona enpfEstudiante;
	private PeriodoAcademico enpfPeriodo;
	private Carrera enpfCarrera;
	private boolean enpRenderNumMatricula;
	
	private List<MateriaDto> enpfNotasSAu; 
	
	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {
	
	}
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB 
	FichaMatriculaDtoServicioJdbc servEnpfFichaMatriculaDto;
	@EJB 
	PeriodoAcademicoServicio servEnpfPeriodoAcademico;	
	@EJB 
	EstudianteDtoServicioJdbc  servEnpfEstudianteDto;
	@EJB 
	MateriaDtoServicioJdbc  servEnpfMateriaDto;
	@EJB 
	PersonaServicio  servPersonaServicio;
	@EJB 
	CarreraServicio  servCarreraServicio;
	@EJB
	MatriculaServicioJdbc servMatriculaDtoServicioJdbc; 
	@EJB
	MateriaServicio servEnpfMateria;
	@EJB
	MallaCurricularMateriaServicio servMallaCurricularMateriaServicio;
	@EJB
	MallaCurricularParaleloServicio servMallaCurricularParaleloServicio;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String irAlistarMatriculas(Usuario usuario) {
		try {
			
			enpfUsuario = usuario;

			enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			
			if(enpfListMatriculaDto != null || enpfListMatriculaDto.size() > 0){
				try {
					List<FichaMatriculaDto> recordSau = cargarRecordSAU(GeneralesConstantes.APP_ID_BASE, GeneralesConstantes.APP_ID_BASE);
					if(recordSau != null){
						enpfListMatriculaDto.addAll(recordSau);
					}	
				} catch (Exception e) {
				}
				

				enpfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
				enpfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);

				getEnpfListCarreraDto();
				for (FichaMatriculaDto itemMatricula : enpfListMatriculaDto) { 
					Boolean encontro = false;  
					for (CarreraDto itemCarrera : enpfListCarreraDto) {  
						if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ 
							encontro = true; 
						}
					} 
					if(encontro == false){ 
						CarreraDto carreraAgregar = new CarreraDto();
						carreraAgregar.setCrrId(itemMatricula.getCrrId());
						carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
						carreraAgregar.setCrrEspeCodigo(itemMatricula.getCrnEstado());
						enpfListCarreraDto.add(carreraAgregar);
					}
				}



				getEnpfListPeriodoAcademico();
				
				
				Collections.sort(enpfListMatriculaDto, new Comparator<FichaMatriculaDto>() {
					public int compare(FichaMatriculaDto obj1, FichaMatriculaDto obj2) {
						return new Integer(obj1.getPracId()).compareTo(new Integer(obj2.getPracId()));
					}
				});
				Collections.reverse(enpfListMatriculaDto);
				for (FichaMatriculaDto itemMatricula : enpfListMatriculaDto) { 
					Boolean encontro = false; 
					for (PeriodoAcademico itemPeriodoAcademico : enpfListPeriodoAcademico) { 
						if(itemMatricula.getPracId() == itemPeriodoAcademico.getPracId()){ 
							encontro = true; 
						}
					} 

					if(encontro == false){ 
						PeriodoAcademico periodoAgregar = new PeriodoAcademico();
						periodoAgregar.setPracId(itemMatricula.getPracId());  
						periodoAgregar.setPracDescripcion(itemMatricula.getPracDescripcion()); 
						enpfListPeriodoAcademico.add(periodoAgregar);
					}
				}
			}else{
				FacesUtil.mensajeError("Usted no posee notas para mostrar.");
				return null;
			}

			
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} 
		
		return "irListarMatriculaEstudiante";
	}


	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		try {

			enpfListMatriculaDto = new ArrayList<>();
			if(enpfCarreraDtoBuscar.getCrrId() != GeneralesConstantes.APP_ID_BASE){

				Carrera carrera = null;
				try{
					carrera = servCarreraServicio.buscarPorId(enpfCarreraDtoBuscar.getCrrId());
				} catch (CarreraNoEncontradoException e) {
					carrera = null;
				} catch (CarreraException e) {
					FacesUtil.mensajeError(e.getMessage());
				} 

				if(carrera != null){
					enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, enpfUsuario.getUsrPersona().getPrsIdentificacion(), enpfCarreraDtoBuscar.getCrrId());
					if(carrera.getCrrEspeCodigo() != null){
						List<FichaMatriculaDto> recordSau = cargarRecordSAU(carrera.getCrrEspeCodigo() , GeneralesConstantes.APP_ID_BASE);
						if(recordSau != null){
							enpfListMatriculaDto.addAll(recordSau);
						}
					}
				}else{
					List<FichaMatriculaDto> recordSau = cargarRecordSAU(enpfCarreraDtoBuscar.getCrrId() , GeneralesConstantes.APP_ID_BASE);
					if(recordSau != null){
						enpfListMatriculaDto.addAll(recordSau);
					}
				}
 
				if(enpfListMatriculaDto.size() == 0){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.buscar.matriculas.no.encontrado.exception")));
					enpfListMatriculaDto = null;
				}
			}else{
				enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, enpfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
				List<FichaMatriculaDto> recordSau = cargarRecordSAU(GeneralesConstantes.APP_ID_BASE, GeneralesConstantes.APP_ID_BASE);
				if(recordSau != null){
					enpfListMatriculaDto.addAll(recordSau);
				}
			}
			
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.buscar.ficha.matricula.exception")));
		}  
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar() {
		enpfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		enpfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		enpfListMatriculaDto = null;
		buscar();
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		enpfListMatriculaDto = null;
		enpfListPeriodoAcademico = null;
		enpfListCarreraDto = null;
		enpfPeriodoAcademicoBuscar = null;
		enpfCarreraDtoBuscar = null;
		enpfUsuario = null;
		enpfListMateriaDto = null;
		enpfCarrera = null;
		enpfEstudiante = null;
		enpfPeriodo = null;
		return "irInicio";
	}
	
	/**
	 * Visualiza las notas generada del estudiante
	 * @return  Navegacion a la pagina de notas.
	 */
	public String irVerNotas(FichaMatriculaDto fichaMatriculaDto){
		try {
			PeriodoAcademico pracAux = servEnpfPeriodoAcademico.buscarPorId(fichaMatriculaDto.getPracId());
			if(pracAux.getPracEstado()==PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE
					|| pracAux.getPracEstado()==PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE){
				try {
					Carrera carrera = servCarreraServicio.buscarPorId(fichaMatriculaDto.getCrrId());
					if(carrera.getCrrTipo()!= CarreraConstantes.TIPO_NIVELEACION_VALUE
							&& carrera.getCrrTipo()!= CarreraConstantes.TIPO_POSGRADO_VALUE){
						Persona prsAux = servPersonaServicio.buscarPersonaPorIdentificacion(enpfUsuario.getUsrIdentificacion());
						if(!(prsAux.getPrsFormularioSeguro()!=null)){
							FacesUtil.limpiarMensaje(); 
						    FacesUtil.mensajeError("Para visualizar las notas del Período Académico actual, es necesario que cargue el archivo de solicitud del seguro de vida, a través de la opción Estudiante - Información Personal.");
						    return null;
						}	
					}
				} catch (PersonaException e4) {
					FacesUtil.limpiarMensaje(); 
				    FacesUtil.mensajeError("Error al buscar la persona por identificación, por favor comuníquese con el administrador del sistema.");
					return null;
				} catch (CarreraNoEncontradoException e) {
					FacesUtil.limpiarMensaje(); 
				    FacesUtil.mensajeError("Error al buscar la carrera, por favor comuníquese con el administrador del sistema.");
					return null;
				} catch (CarreraException e) {
					FacesUtil.limpiarMensaje(); 
				    FacesUtil.mensajeError("Error al buscar la carrera, por favor comuníquese con el administrador del sistema.");
					return null;
				}
						
			}
		} catch (PeriodoAcademicoNoEncontradoException e5) {
			FacesUtil.limpiarMensaje(); 
		    FacesUtil.mensajeError("Error al buscar el período académico, por favor comuníquese con el administrador del sistema.");
			return null;
		} catch (PeriodoAcademicoException e5) {
			FacesUtil.limpiarMensaje(); 
		    FacesUtil.mensajeError("Error al buscar el período académico, por favor comuníquese con el administrador del sistema.");
			return null;
		}catch (Exception e5) {
		}
		
		
		enpRenderNumMatricula= true;
		try {
			enpfCarrera= servCarreraServicio.buscarPorId(fichaMatriculaDto.getCrrId());
		} catch (CarreraNoEncontradoException e3) {
			    FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e3.getMessage());
		} catch (CarreraException e3) {
			    FacesUtil.limpiarMensaje(); 
			    FacesUtil.mensajeError(e3.getMessage());
		}
		
		try {
			enpfPeriodo=servEnpfPeriodoAcademico.buscarPorId(fichaMatriculaDto.getPracId());
		} catch (PeriodoAcademicoNoEncontradoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		} catch (PeriodoAcademicoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		}
		
		try {
			enpfEstudiante= servPersonaServicio.buscarPorId(enpfUsuario.getUsrPersona().getPrsId());
		} catch (PersonaNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		} catch (PersonaException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		
		try {
			if(enpfCarrera != null && enpfCarrera.getCrrTipo()==CarreraConstantes.TIPO_NIVELEACION_VALUE){
				enpfListMateriaDto = servEnpfMateriaDto.listarXestudianteXmatriculaXperiodoXcarreraFull(enpfUsuario.getUsrPersona().getPrsId(), fichaMatriculaDto.getFcmtId(), fichaMatriculaDto.getPracId(), fichaMatriculaDto.getCrrId());
			}if(enpfCarrera != null && enpfCarrera.getCrrTipo()==CarreraConstantes.TIPO_POSGRADO_VALUE){
				enpfListMateriaDto = servEnpfMateriaDto.listarXestudianteXmatriculaXperiodoXcarreraXPosgrado(enpfUsuario.getUsrPersona().getPrsId(), fichaMatriculaDto.getFcmtId(), fichaMatriculaDto.getPracId());
			    enpRenderNumMatricula= false;
			}else{
				enpfListMateriaDto = servEnpfMateriaDto.listarXestudianteXmatriculaXperiodoXcarrera(enpfUsuario.getUsrPersona().getPrsId(), fichaMatriculaDto.getFcmtId(), fichaMatriculaDto.getPracId());
			}
			List<MateriaDto> listaModulosAgregar = new ArrayList<MateriaDto>(); 
			for (MateriaDto item : enpfListMateriaDto) {
				List<Materia> listaMateriasModulos = null;
				try {
					listaMateriasModulos = new ArrayList<Materia>();
					listaMateriasModulos = servEnpfMateria.listarTodosModulos(item.getMtrId());
//					int i = 1;
//					BigDecimal sumatoriaNotas = BigDecimal.ZERO;
//					BigDecimal sumatoriaAsistencia = BigDecimal.ZERO;
//					BigDecimal sumatoriaAsistenciaDocente = BigDecimal.ZERO;
					for (Materia materia : listaMateriasModulos) {
						try {
							MallaCurricularMateria mlcrmtAux = servMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(materia.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							MallaCurricularParalelo mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), item.getPrlId());
							List<MateriaDto> listaModulos = servEnpfMateriaDto.listarXmallaCurricularParalelo(mlcrprAux.getMlcrprId(),enpfUsuario.getUsrPersona().getPrsId(),fichaMatriculaDto.getPracId());
							listaModulos.get(0).setMtrId(materia.getMtrId());
							listaModulos.get(0).setMtrDescripcion(item.getMtrDescripcion()+" - "+materia.getMtrDescripcion());
							if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()==-99){
								listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
							}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()>=28){
								listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
							}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()>8.8){
								if(listaModulos.get(0).getClfSupletorio()!=null){
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}else{
									listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);	
								}
								
							}else if(listaModulos.get(0).getClfNotaFinalSemestre()!=null && listaModulos.get(0).getClfNotaFinalSemestre()<8.8){
								listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							}
							listaModulos.get(0).setDtmtNumero(item.getDtmtNumero());
							listaModulosAgregar.add(listaModulos.get(0));
//							sumatoriaNotas = sumatoriaNotas.add(new BigDecimal(listaModulos.get(0).getClfNota1()).setScale(2, RoundingMode.CEILING));
//							sumatoriaAsistencia = sumatoriaAsistencia.add(new BigDecimal(listaModulos.get(0).getClfAsistencia1()).setScale(0, RoundingMode.CEILING));
//							sumatoriaAsistenciaDocente = sumatoriaAsistenciaDocente.add(new BigDecimal(listaModulos.get(0).getClfAsistenciaDocente1()).setScale(0, RoundingMode.CEILING));
//							i++;
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
								e2.printStackTrace();
								listaModulos.get(0).setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
							}
							
							
							listaModulosAgregar.add(listaModulos.get(0));
						}
//						if(i==listaMateriasModulos.size()+1){
//							item.setClfAsistencia1((sumatoriaAsistencia.divide(new BigDecimal(i-1),0,RoundingMode.CEILING)).setScale(0, RoundingMode.CEILING).intValue());
//							item.setClfNota1((sumatoriaNotas.divide(new BigDecimal(i-1),2,RoundingMode.CEILING)).setScale(2, RoundingMode.CEILING).floatValue());
//							item.setClfAsistenciaDocente1((sumatoriaAsistenciaDocente.divide(new BigDecimal(i-1),0,RoundingMode.CEILING)).setScale(0, RoundingMode.CEILING).intValue());
////							System.out.println(item.getRcesId());
//						}
					}
					
				} catch (Exception e) {
				}
				
			}
			if(listaModulosAgregar.size()!=0){
				enpfListMateriaDto.addAll(listaModulosAgregar);
			}
//			Iterator itera = enpfListMateriaDto.iterator();
//			while(itera.hasNext()){
//				MateriaDto cad = (MateriaDto) itera.next();
//				if( cad.getDtmtNumero() == 0 ){
//					itera.remove();
//				}
//			}
			
//			Collections.sort(enpfListMateriaDto);
			
			
		} catch (MateriaDtoException e) {
		} catch (MateriaDtoNoEncontradoException e) {
		}
		
		List<MateriaDto> obtenerNotasSau = new ArrayList<>();
		List<MateriaDto> listaNotas = obtenerNotasSau(fichaMatriculaDto);
		
		if(listaNotas != null){
			obtenerNotasSau.addAll(listaNotas);
			enpfCarrera = new Carrera();
			enpfCarrera.setCrrDetalle(fichaMatriculaDto.getCrrDescripcion());
			enpfCarrera.setCrrDescripcion(fichaMatriculaDto.getCrrDescripcion());
			enpfPeriodo = new PeriodoAcademico();
			enpfPeriodo.setPracDescripcion(fichaMatriculaDto.getPracDescripcion());
		}
		
		if(obtenerNotasSau != null){
			if(enpfListMateriaDto == null){
				enpfListMateriaDto = new ArrayList<>();	
			}
			enpfListMateriaDto.addAll(obtenerNotasSau);
		}
		
		
//		List<MateriaDto> listaAux = new ArrayList<MateriaDto>();
//		listaAux.addAll(enpfListMateriaDto);
//		Iterator itera = enpfListMateriaDto.iterator();
//		while(itera.hasNext()){
//			MateriaDto cad = (MateriaDto) itera.next();
//			if( cad.getDtmtNumero() == 0 ){
//				itera.remove();
//			}
//		}


		Collections.sort(enpfListMateriaDto, new Comparator<MateriaDto>() {
			public int compare(MateriaDto obj1, MateriaDto obj2) {
				return new String(obj1.getMtrDescripcion()).compareTo(new String(obj2.getMtrDescripcion()));
			}
		});
		
		
		if(fichaMatriculaDto.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
				|| fichaMatriculaDto.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| fichaMatriculaDto.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| fichaMatriculaDto.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| fichaMatriculaDto.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
			return "irVerNotasIdiomas";
		}else{
			return "irVerNotas";	
		}
		
	}
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String regresarMatricula(){
		enpfListMateriaDto = null;
		return "regresarListarMatricula";
	}

	
	/**
	 * No tocar hps
	 * @author Arturo Villafuerte - ajvillafuerte
	 **/
	@SuppressWarnings("rawtypes")
	public List<FichaMatriculaDto> cargarRecordSAU(int espeCodigo, int pracGrupo){
	
		List<FichaMatriculaDto> retorno = null;
		List<MateriaDto> notasSau = null; 
		
		try {
			
			List<RecordEstudianteSAUDto> record = servMatriculaDtoServicioJdbc.buscarNotasSAU(enpfUsuario.getUsrPersona().getPrsIdentificacion(), espeCodigo, pracGrupo);
			retorno = new ArrayList<>();
			notasSau = new ArrayList<>();
			
			if(record != null){
				for(RecordEstudianteSAUDto item: record){

					Carrera carrera =null;
					try{
						carrera = servCarreraServicio.buscarCarreraXEspeCodigo(item.getEspeCodigo());
					} catch (CarreraException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (CarreraNoEncontradoException e) {
						carrera = null;
					}

					FichaMatriculaDto ficha = new FichaMatriculaDto();
					if(carrera != null){
						ficha.setCrrId(carrera.getCrrId());
						ficha.setCrrDescripcion(carrera.getCrrDescripcion());
					}else{
						ficha.setCrrId(item.getEspeCodigo());
						ficha.setCrrDescripcion(item.getEspeDescripcion());
					}
					ficha.setPracId(item.getPracGrupo());
					ficha.setPracDescripcion(item.getPracDescripcion());
					ficha.setFcmtNivelUbicacion(item.getNumSemestre());
					
					
					MateriaDto materiaNotas = new MateriaDto();
				
					materiaNotas.setPracId(item.getPracGrupo());
					
					if(carrera != null){
						materiaNotas.setCrrId(carrera.getCrrId()); 
					}else{
						materiaNotas.setCrrId(item.getEspeCodigo()); 
					}
					materiaNotas.setPrlDescripcion(item.getPrlDescripcion() != null ? item.getPrlDescripcion():"Paralelo Desconocida");
					materiaNotas.setMtrCodigo(item.getMtrCodigo() != null ? item.getMtrCodigo():"Código Desconocida");
					materiaNotas.setMtrDescripcion(item.getMtrDescripcion() != null ? item.getMtrDescripcion():"Asignatura Desconocida");
					materiaNotas.setDtmtNumero(item.getNumeroMtricula()  != null  ?  item.getNumeroMtricula() != -1 ? item.getNumeroMtricula():GeneralesConstantes.APP_ID_BASE : null);
					materiaNotas.setClfNota1(item.getNota1() != null ? item.getNota1().intValue() != -1 ? item.getNota1().floatValue():null:null);
					
					materiaNotas.setClfAsistencia1(item.getAsistencia1() != null ? item.getAsistencia1().intValue() != -1 ? item.getAsistencia1():null:null);
					materiaNotas.setClfAsistenciaDocente1(item.getAsistenciaTotal1() != null ? item.getAsistenciaTotal1() != -1 ? item.getAsistenciaTotal1():null:null);
					materiaNotas.setClfNota2(item.getNota2() != null ? item.getNota2().intValue() != -1 ? item.getNota2().floatValue():null:null);
					materiaNotas.setClfAsistencia2(item.getAsistencia2() != null ? item.getAsistencia2().intValue() != -1 ? item.getAsistencia2():null:null);
					materiaNotas.setClfAsistenciaDocente2(item.getAsistenciaTotal2() != null ? item.getAsistenciaTotal2().intValue() != -1 ? item.getAsistenciaTotal2():null:null);
					
					Float asistencia1 = item.getAsistencia1() != null ? item.getAsistencia1().intValue() != -1 ? item.getAsistencia1():new Float(0):new Float(0);
					Float asistencia2 = item.getAsistencia2() != null ? item.getAsistencia2().intValue() != -1 ? item.getAsistencia2():new Float(0):new Float(0);
					Float asistenciaTotal1 = item.getAsistenciaTotal1() != null ? item.getAsistenciaTotal1() != -1 ? item.getAsistenciaTotal1().floatValue():new Float(0):new Float(0);
					Float asistenciaTotal2 = item.getAsistenciaTotal2() != null ? item.getAsistenciaTotal2() != -1 ? item.getAsistenciaTotal2().floatValue():new Float(0):new Float(0);
					Float promedioAsistencia = ((asistencia1+asistencia2)/(asistenciaTotal1+asistenciaTotal2)*100);
					if((asistencia1+asistencia2) == new Float(0) && (asistenciaTotal1+asistenciaTotal2) == new Float(0)){
						promedioAsistencia = new Float(0);
					} 
					
					materiaNotas.setClfPromedioAsistencia(promedioAsistencia);
					
					Float nota1 = item.getNota1() != null ? item.getNota1().intValue() != -1 ? item.getNota1().floatValue():new Float(0):new Float(0);
					Float nota2 = item.getNota2() != null ? item.getNota2().intValue() != -1 ? item.getNota2().floatValue():new Float(0):new Float(0);
					Float promedioNotas = nota1+nota2;
					
					materiaNotas.setClfSumaP1P2(promedioNotas);
					materiaNotas.setClfSupletorio(item.getNotaRecuperacion() != null ? item.getNotaRecuperacion().intValue() != -1 ? item.getNotaRecuperacion().floatValue():null:null);
					materiaNotas.setClfNotaFinalSemestre(item.getNotaFinal() != null ? item.getNotaFinal().intValue() != -1 ? item.getNotaFinal().floatValue():0:0);
					materiaNotas.setCmfrDescripcion(item.getMtrEstadoDescripcion());
					
					notasSau.add(materiaNotas);  
					
					retorno.add(ficha);
				}
				
				enpfNotasSAu = new ArrayList<>();
				enpfNotasSAu.addAll(notasSau);

				List<FichaMatriculaDto> recordAux = new ArrayList<>();
				recordAux.addAll(retorno);
				for(FichaMatriculaDto item: recordAux){
					int count = 1;
					Iterator itera = retorno.iterator();
					while(itera.hasNext()){
						FichaMatriculaDto cad = (FichaMatriculaDto) itera.next();
						if( cad.getPracId() == item.getPracId() && cad.getCrrId() == item.getCrrId()){
							if(count>1){
								itera.remove();
							}
							count = count+1;
						}
					}	
				}
			}
			
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	public List<MateriaDto> obtenerNotasSau(FichaMatriculaDto fichaMatriculaDto){
		
		List<MateriaDto> retorno = null;
		
		Carrera carrera =null;
		try{
			carrera = servCarreraServicio.buscarCarreraXEspeCodigo(fichaMatriculaDto.getCrrId());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			carrera = null;
		}
		
		retorno = new ArrayList<>();
		
		if(carrera != null){
			if(enpfNotasSAu != null){
				for(MateriaDto item: enpfNotasSAu){
					if(item.getPracId() == fichaMatriculaDto.getPracId() && item.getCrrId() == carrera.getCrrId()){
						retorno.add(item);
					}
					if(item.getPracId() == fichaMatriculaDto.getPracId() && item.getCrrId() == fichaMatriculaDto.getCrrId()){
						retorno.add(item);
					}
				}
			}
		}else{
			if(enpfNotasSAu != null){
				for(MateriaDto item: enpfNotasSAu){
					if(item.getPracId() == fichaMatriculaDto.getPracId() && item.getCrrId() == fichaMatriculaDto.getCrrId()){
						retorno.add(item);
					}
				}
			}
		}
		
		return retorno;
	}
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	public PeriodoAcademico getEnpfPeriodoAcademicoBuscar() {
		return enpfPeriodoAcademicoBuscar;
	}

	public void setEnpfPeriodoAcademicoBuscar(PeriodoAcademico enpfPeriodoAcademicoBuscar) {
		this.enpfPeriodoAcademicoBuscar = enpfPeriodoAcademicoBuscar;
	}

	public CarreraDto getEnpfCarreraDtoBuscar() {
		return enpfCarreraDtoBuscar;
	}

	public void setEnpfCarreraDtoBuscar(CarreraDto enpfCarreraDtoBuscar) {
		this.enpfCarreraDtoBuscar = enpfCarreraDtoBuscar;
	}

	public List<PeriodoAcademico> getEnpfListPeriodoAcademico() {
		enpfListPeriodoAcademico = enpfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):enpfListPeriodoAcademico;
		return enpfListPeriodoAcademico;
	}

	public void setEnpfListPeriodoAcademico(List<PeriodoAcademico> enpfListPeriodoAcademico) {
		this.enpfListPeriodoAcademico = enpfListPeriodoAcademico;
	}

	public List<CarreraDto> getEnpfListCarreraDto() {
		enpfListCarreraDto = enpfListCarreraDto==null?(new ArrayList<CarreraDto>()):enpfListCarreraDto;
		return enpfListCarreraDto;
	}

	public void setEnpfListCarreraDto(List<CarreraDto> enpfListCarreraDto) {
		this.enpfListCarreraDto = enpfListCarreraDto;
	}

	public List<FichaMatriculaDto> getEnpfListMatriculaDto() {
		enpfListMatriculaDto = enpfListMatriculaDto==null?(new ArrayList<FichaMatriculaDto>()):enpfListMatriculaDto;
		return enpfListMatriculaDto;
	}

	public void setEnpfListMatriculaDto(List<FichaMatriculaDto> enpfListMatriculaDto) {
		this.enpfListMatriculaDto = enpfListMatriculaDto;
	}

	public Usuario getEnpfUsuario() {
		return enpfUsuario;
	}

	public void setEnpfUsuario(Usuario enpfUsuario) {
		this.enpfUsuario = enpfUsuario;
	}

	public List<MateriaDto> getEnpfListMateriaDto() {
		enpfListMateriaDto = enpfListMateriaDto==null?(new ArrayList<MateriaDto>()):enpfListMateriaDto;
		return enpfListMateriaDto;
	}

	public void setEnpfListMateriaDto(List<MateriaDto> enpfListMateriaDto) {
		this.enpfListMateriaDto = enpfListMateriaDto;
	}
	
	public PeriodoAcademicoServicio getServEnpfPeriodoAcademico() {
		return servEnpfPeriodoAcademico;
	}

	public Persona getEnpfEstudiante() {
		return enpfEstudiante;
	}

	public void setEnpfEstudiante(Persona enpfEstudiante) {
		this.enpfEstudiante = enpfEstudiante;
	}

	public PeriodoAcademico getEnpfPeriodo() {
		return enpfPeriodo;
	}

	public void setEnpfPeriodo(PeriodoAcademico enpfPeriodo) {
		this.enpfPeriodo = enpfPeriodo;
	}

	public Carrera getEnpfCarrera() {
		return enpfCarrera;
	}

	public void setEnpfCarrera(Carrera enpfCarrera) {
		this.enpfCarrera = enpfCarrera;
	}


	public boolean isEnpRenderNumMatricula() {
		return enpRenderNumMatricula;
	}


	public void setEnpRenderNumMatricula(boolean enpRenderNumMatricula) {
		this.enpRenderNumMatricula = enpRenderNumMatricula;
	}
	
}
