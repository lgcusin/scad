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
   
 ARCHIVO:     AdministracionEvaluacionEstudianteForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración del reporte.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
7-NOV-2017			Vinicio Rosales                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionEvaluacionEstudianteForm.
 * Managed Bean que maneja las peticiones para la administración de la generacion del reporte.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name="administracionEvaluacionEstudianteForm")
@SessionScoped
public class AdministracionEvaluacionEstudiante implements Serializable{

	private static final long serialVersionUID = -5706899311603575398L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	//para buscar
	private PeriodoAcademico aeefPeriodoAcademicoBuscar;
	private Dependencia aeefFacultadBuscar;
	private Carrera aeefCarreraBuscar;
	private Usuario aeefUsuarioBuscar;
	
	private List<PeriodoAcademico> aeefListPeriodoAcademico;
	private List<Dependencia> aeefListDependencia;
	private List<Carrera> aeefListCarrera;
	private List<MateriaDto> aeefListMateria;
	private List<MateriaDto> aeefListDocentes;
	
	private Integer aeefValidadorClic;
	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private DependenciaServicio servDependencia;
	@EJB private CarreraServicio servCarrera;
	@EJB private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB private HorarioAcademicoDtoServicioJdbc servJdbcHorarioAcademicoDto;
	@EJB private CargaHorariaServicio servEefCargaHorariaServicio; 
	@EJB private PersonaDatosDtoServicioJdbc servEefPersonaDatosServicioJdbc;
	@EJB private UsuarioRolServicio servEefUsuarioRolServicio;
	@EJB private ContenidoEvaluacionDtoServicioJdbc servEefContenidoEvaluacionDtoJdbcServicio;
	@EJB private EvaluacionServicio servEefEvaluacionServicio;
	@EJB private DocenteDtoServicioJdbc servEdfDocentesServicio;


		
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	/**
	 * Incicializa las variables para la funcionalidad de administración de la generacion del reporte
	 * @return navegacion al xhtml de listar Autoevaluacion
	 */
	public String irAdministracionEvaluacionEstudiante(Usuario usuario) {
		try {
			aeefUsuarioBuscar = usuario;
			iniciarParametros();
			aeefListPeriodoAcademico = servPeriodoAcademico.buscarPracEstadoEvaluacionXestadoPracXtipoPrac(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irListarEvaluacionEstudiante";
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio
	 */
	public void iniciarParametros(){
		//iniciar el objeto de búsqueda
		aeefPeriodoAcademicoBuscar = new PeriodoAcademico();
		aeefFacultadBuscar = new Dependencia();
		aeefCarreraBuscar = new Carrera();
		aeefUsuarioBuscar = new Usuario();
		aeefListMateria = new ArrayList<MateriaDto>();
		aeefListCarrera = null;
		bloquearModal();
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio
	 */
	public String irInicio(){
		limpiar();
		return "irInicio"; 	
	}
	
	
	/**
	 * Método para limpiar los parámtros de busqueda ingresados
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	/**
	 * Método para buscar la entidad deseada con los parámetros de búsqueda ingresados
	 */
	@SuppressWarnings("rawtypes")
	public void buscar(PeriodoAcademico periodoAcademico, Dependencia dpn, Carrera crr){
		try {
			if(periodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}else if(dpn.getDpnId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe seleccionar la Facultad");
			}else{
				//lista de docentes con materia, mlcrpr y número de estudiantes matriculados en mlcrpr
				List<DocenteJdbcDto> docentesList = new ArrayList<>();
				docentesList = servEdfDocentesServicio.listarDocentesMateriaNumMatriculadosXPeriodoXFacultadXCarrera(periodoAcademico.getPracId(), dpn.getDpnId(), aeefCarreraBuscar.getCrrId());
				
				 List<MateriaDto>  auxListMateriaDto = servJdbcMateriaDto.buscarEstudianteMatriculadoXPeriodoXDependencia(periodoAcademico.getPracId(), dpn.getDpnId(), CarreraConstantes.TIPO_PREGRADO_VALUE, crr.getCrrId());
                 List<MateriaDto> materias =  new ArrayList<>();
 					
					if (auxListMateriaDto != null && auxListMateriaDto.size()> 0) {
						for (MateriaDto item : auxListMateriaDto) {
							if (item.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
								materias.addAll(cargarModulos(item));
							}
						}	
					}
					
					auxListMateriaDto.addAll(materias);
					Iterator iter = auxListMateriaDto.iterator();
					while(iter.hasNext()){
						final MateriaDto item = (MateriaDto) iter.next();
						if (item.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
							iter.remove();
						}
					}
					for (MateriaDto item : auxListMateriaDto) {
						List<HorarioAcademicoDto> horarios = cargarParaleloCompartido(item.getMlcrprId());
						if (horarios != null && horarios.size() >0) {
							List<HorarioAcademicoDto> horariosPadre = cargarParaleloCompartido(horarios.get(0).getHracMlcrprIdComp());
							if (horariosPadre != null && horariosPadre.size() > 0) {
								item.setMlcrprId(horariosPadre.get(0).getMlcrprId());
								item.setMlcrmtId(horariosPadre.get(0).getMlcrmtId());
								item.setPrlId(horariosPadre.get(0).getPrlId());
							}
						}
					}
					
					for (MateriaDto item : auxListMateriaDto) {
						item.setMtrPersonaDto(cargarDocentePorMallaCurricularParalelo(periodoAcademico.getPracId(), item.getMlcrprId()));
						item.setMtrEstadoLabel(verificarEvaluacion(item, periodoAcademico.getPracId()));
					}
					
					auxListMateriaDto.sort(Comparator.comparing(MateriaDto::getPrsIdentificacion).thenComparing(Comparator.comparing(MateriaDto::getMtrDescripcion)));
				 aeefListMateria = auxListMateriaDto;
			}
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DocenteDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	@SuppressWarnings("null")
	public String verificarEvaluacion(MateriaDto materia, int pracId){
		String verificar = "NO EVALUADO";
		try {
			
			CargaHoraria crhrSeleccion = servEefCargaHorariaServicio.buscarPorMlCrMtXPeriodoXParalelo(materia.getMlcrmtId(), pracId, materia.getPrlId());
			PersonaDatosDto prsDtoSeleccion = servEefPersonaDatosServicioJdbc.buscarXDetallePuesto(crhrSeleccion.getCrhrDetallePuesto().getDtpsId());
			UsuarioRol usroEvaluado = servEefUsuarioRolServicio.buscarXPersonaXrol(prsDtoSeleccion.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			UsuarioRol usroEvaluador = servEefUsuarioRolServicio.buscarXPersonaXrol(materia.getPrsId(),RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
			Evaluacion evaluacion = servEefEvaluacionServicio.buscarEvaluacion(pracId, TipoEvaluacionConstantes.EVALUACION_ESTUDIANTE_VALUE);
			
			try{
				List<ContenidoEvaluacionDto> contenido = servEefContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), crhrSeleccion.getCrhrId(), usroEvaluador.getUsroId(), usroEvaluado.getUsroId(), GeneralesConstantes.APP_ID_BASE );
				if(contenido != null || contenido.size() > 0){
					verificar = "EVALUADO";
				}

			} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			} catch (ContenidoEvaluacionDtoException e) {
			}
		
		} catch (UsuarioRolException e) {
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (CargaHorariaNoEncontradoException e) {
		} catch (CargaHorariaException e) {
		} catch (PersonaDatosDtoNoEncontradoException e) {
		} catch (PersonaDatosDtoException e) {
		} catch (EvaluacionNoEncontradoException e) {
		} catch (EvaluacionException e) {
		}
		
		return verificar;
	}
	
	
	
	private PersonaDto cargarDocentePorMallaCurricularParalelo(int periodo, int mlcrprId){
		try {
			return servJdbcMateriaDto.buscarDocentesAsignadosCHyCompartidosXPeriodoXDependencia(periodo, mlcrprId);
		} catch (MateriaDtoException e) {
			return null;
		} catch (MateriaDtoNoEncontradoException e) {
			return null;
		} 
	}
	
	private List<HorarioAcademicoDto> cargarParaleloCompartido(int mlcrprId){
		List<HorarioAcademicoDto> retorno = null;

		try {
			retorno = servJdbcHorarioAcademicoDto.buscarParaleloCompartido(mlcrprId);
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		FacesUtil.limpiarMensaje();	

		return retorno;

	}
	
	private List<MateriaDto> cargarModulos(MateriaDto recordId) {
		try {
			List<MateriaDto> retorno = servJdbcMateriaDto.listarXrecordXmatriculaXperiodo(recordId.getRcesId(), recordId.getFcmtId(), recordId.getPracId());
			if (retorno != null && retorno.size() > 0) {
				for (MateriaDto item : retorno) {
					item.setMtrDescripcion(recordId.getMtrDescripcion() + " - " + item.getMtrDescripcion());
				}
			}

			return retorno;
		} catch (MateriaDtoException e) {
			return null;
		} catch (MateriaDtoNoEncontradoException e) {
			return null;
		}
	}

	
	
	/**
	 * Método para cancelar la edicion o creacion de un periodo academico
	 * @return navegacion al xhtml de listar periodo academico
	 */
	public String cancelar(){
		limpiar();
		return "cancelar"; 	
	}
	
	public void cambiarPeriodo(){
		try {
			if(aeefPeriodoAcademicoBuscar.getPracId() != GeneralesConstantes.APP_ID_BASE){
				aeefListDependencia = servDependencia.listarFacultadesActivasEvaluacionXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE, aeefPeriodoAcademicoBuscar.getPracId());
			}else{
				aeefListDependencia = null;
				aeefListCarrera = null;
				aeefListMateria = null;
				aeefFacultadBuscar.setDpnId(GeneralesConstantes.APP_ID_BASE);
				aeefCarreraBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
		}
	}
	
	/**
	 * Método para llenar la lista de carreras
	 * @param dpnId la facultad seleccionada en el combo facultad
	 */
	public void llenarCarrera(int dnpId) {
		aeefListCarrera = new ArrayList<Carrera>();
		try {
			if (dnpId != GeneralesConstantes.APP_ID_BASE) {
				aeefListCarrera = servCarrera.listarCarrerasActivasEvaluacionXTipoCarrera(dnpId, CarreraConstantes.TIPO_PREGRADO_VALUE, aeefPeriodoAcademicoBuscar.getPracId());
			}else{
				aeefListCarrera = null;
				aeefListMateria = null;
				aeefCarreraBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
				FacesUtil.mensajeError("Debe seleccionar la Facultad");
			}
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.no.encontrado.exception")));
		}
	}
	
	public void cambiarCarrera(){
		aeefListMateria = new ArrayList<>();
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla
	 */
	public void verificarClickImprimir(){
		if(aeefListMateria.size() > 0 && aeefListMateria != null){
			PersonaDto directora = new PersonaDto();
			directora.setPrsPrimerApellido("Carrillo");
			directora.setPrsSegundoApellido("Chico");
			directora.setPrsNombres("María Elena");
			directora.setPrsCargo("DIRECTORA DE TECNOLOGIAS");
			ReporteEvaluacionDocenteForm.generarReporteGeneralEvaluacionXls(aeefListMateria, aeefUsuarioBuscar, directora, "100", "100");
			habilitaModalImpresion();
		}else{
			FacesUtil.mensajeError("No existe información para presentar");
			bloquearModal();
		}
	}
	
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloquearModal(){
		aeefValidadorClic = 0;
	}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		aeefValidadorClic = 1;
	}
	
	public PeriodoAcademico getAeefPeriodoAcademicoBuscar() {
		return aeefPeriodoAcademicoBuscar;
	}

	public void setAeefPeriodoAcademicoBuscar(PeriodoAcademico aeefPeriodoAcademicoBuscar) {
		this.aeefPeriodoAcademicoBuscar = aeefPeriodoAcademicoBuscar;
	}

	public Dependencia getAeefFacultadBuscar() {
		return aeefFacultadBuscar;
	}

	public void setAeefFacultadBuscar(Dependencia aeefFacultadBuscar) {
		this.aeefFacultadBuscar = aeefFacultadBuscar;
	}

	public Carrera getAeefCarreraBuscar() {
		return aeefCarreraBuscar;
	}

	public void setAeefCarreraBuscar(Carrera aeefCarreraBuscar) {
		this.aeefCarreraBuscar = aeefCarreraBuscar;
	}

	public Usuario getAeefUsuarioBuscar() {
		return aeefUsuarioBuscar;
	}

	public void setAeefUsuarioBuscar(Usuario aeefUsuarioBuscar) {
		this.aeefUsuarioBuscar = aeefUsuarioBuscar;
	}

	public List<PeriodoAcademico> getAeefListPeriodoAcademico() {
		return aeefListPeriodoAcademico;
	}

	public void setAeefListPeriodoAcademico(List<PeriodoAcademico> aeefListPeriodoAcademico) {
		this.aeefListPeriodoAcademico = aeefListPeriodoAcademico;
	}

	public List<Dependencia> getAeefListDependencia() {
		return aeefListDependencia;
	}

	public void setAeefListDependencia(List<Dependencia> aeefListDependencia) {
		this.aeefListDependencia = aeefListDependencia;
	}

	public List<Carrera> getAeefListCarrera() {
		return aeefListCarrera;
	}

	public void setAeefListCarrera(List<Carrera> aeefListCarrera) {
		this.aeefListCarrera = aeefListCarrera;
	}

	public List<MateriaDto> getAeefListMateria() {
		return aeefListMateria;
	}

	public void setAeefListMateria(List<MateriaDto> aeefListMateria) {
		this.aeefListMateria = aeefListMateria;
	}

	public Integer getAeefValidadorClic() {
		return aeefValidadorClic;
	}

	public void setAeefValidadorClic(Integer aeefValidadorClic) {
		this.aeefValidadorClic = aeefValidadorClic;
	}

	public List<MateriaDto> getAeefListDocentes() {
		return aeefListDocentes;
	}

	public void setAeefListDocentes(List<MateriaDto> aeefListDocentes) {
		this.aeefListDocentes = aeefListDocentes;
	}
	

	
}

