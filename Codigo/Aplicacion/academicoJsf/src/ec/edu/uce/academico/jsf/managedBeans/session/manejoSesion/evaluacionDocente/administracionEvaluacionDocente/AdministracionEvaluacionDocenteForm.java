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

 ARCHIVO:     AdministracionEvaluacionDocenteForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de administracion de grupos y coordinadores de area. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR                             COMENTARIOS
24-NOVIEMBRE-2018		 FREDDY GUZMAN				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionDocente.administracionEvaluacionDocente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.dtos.GrupoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.GrupoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.GrupoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GrupoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionDirectivoForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del Directivo.
 * 
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "administracionEvaluacionDocenteForm")
@SessionScoped
public class AdministracionEvaluacionDocenteForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;
	protected static final int FIND_BY_IDENTIFICACION = 0;
	protected static final int FIND_BY_PRIMER_APELLIDO = 1;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario aefUsuario;
	private Grupo aefGrupo;
	private GrupoDto aefGrupoDto;
	private Evaluacion aefEvaluacion;

	private Integer aefDependenciaId;
	private Integer aefCarreraId;
	private Integer aefGrupoId;
	private Integer	aefPeriodoId;
	private String aefPrimerApellido;
	private String aefIdentificacion;
	private Integer aefTipoBusqueda; 
	
	private List<Dependencia> aefListDependencia;
	private List<Carrera> aefListCarrera;
	private List<Grupo> aefListGrupo;
	private List<GrupoDto> aefListGrupoDto;
	private List<PeriodoAcademico> aefListPeriodoAcademico;
	private List<MateriaDto> aefListMateria;
	private List<PersonaDto> aefListPersonaDto;
	private List<SelectItem> aefListEstadoGrupo;
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB private DependenciaServicio servDependencia;
	@EJB private CarreraServicio servCarrera;
	@EJB private GrupoServicio servGrupo;
	@EJB private MateriaServicio servMateria;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private EvaluacionServicio servEvaluacionServicio;
	
	@EJB private GrupoServicioJdbc servJdbcGrupo;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;

	// ****************************************************************/
	// ******************* METODOS DE NAVEGACIÓN **********************/
	// ****************************************************************/
	
	public String irAgregarCoordinador(GrupoDto item){
		try {
			aefGrupoDto = item;
			aefListPersonaDto = servJdbcPersonaDto.buscarDocentesPorGrupo(aefPeriodoId, aefCarreraId, item.getGrpId());
			return "irAgregarCoordinador";
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Docentes con Carga Horaria Activa.");
			return null;
		}
		
	}
	
	
	public String irAgregarEvaluador(GrupoDto item){
		try {
			aefGrupoDto = item;
			aefListPersonaDto = servJdbcPersonaDto.buscarDocentesPorGrupo(aefPeriodoId, aefCarreraId, item.getGrpId());
			return "irAgregarCoordinador";
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Docentes con Carga Horaria activa.");
			return null;
		}
		
	}
	
	
	public void irGuardarAsignacionGrupos(){
		if (aefListMateria != null && aefListMateria.size() >0) {

			boolean actualizacion = true;
			for (MateriaDto item : aefListMateria) {
				if (item.getMtrId() != 0) {
					try {

						Materia materia = new Materia();
						materia.setMtrId(item.getMtrId());

						Grupo grupo = new Grupo();
						grupo.setGrpId(item.getGrpId());

						materia.setMtrGrupo(grupo);

						servMateria.editarMateriaPorGrupo(materia);
					} catch (MateriaException e) {
						actualizacion = false;
						FacesUtil.mensajeError("Error al actualizar la asignatura " + item.getMtrDescripcion());
					}
				}
			}

			if (actualizacion) {
				limpiarFormListarMaterias();
				FacesUtil.mensajeInfo("Actualización exitosa. ");
			}
		}
	}

	public String irListarCoordinadoresPorGrupo(Usuario usuario) {
		aefUsuario = usuario;
		
		try {
			aefPeriodoId = GeneralesConstantes.APP_ID_BASE;
			aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
			aefCarreraId = GeneralesConstantes.APP_ID_BASE;
			aefTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
			aefListGrupoDto = null;
			
			aefListPeriodoAcademico = servPeriodoAcademico.buscarPeriodosEvaluacionDesempeño();
			aefListDependencia = servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			return "irListarCoordinadoresPorGrupo";
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	public String irListarAsignaturasPorCarrera(Usuario usuario) {
		aefUsuario = usuario;
		
		try {
			aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
			aefCarreraId = GeneralesConstantes.APP_ID_BASE;
			aefListDependencia = servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			return "irListarAsignaturasPorCarrera";
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}
	
	
	public String irActualizarGrupo() {
		
		if (aefGrupo.getGrpDescripcion().length() > 0) {
			if (aefGrupo.getGrpEstado().intValue() != GeneralesConstantes.APP_ID_BASE) {
				try {
					aefGrupo.setGrpDescripcion(aefGrupo.getGrpDescripcion().toUpperCase());
					boolean retorno = servGrupo.editar(aefGrupo);
					if (retorno) {
						limpiarFormListarGrupos();	
						FacesUtil.mensajeInfo("El Grupo fue actualizado con éxito.");
					}
					
					return "irListarGrupoConocimiento";
				} catch (GrupoException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				}				
			}else {
				FacesUtil.mensajeError("Seleccione un estado al Grupo para continuar con el registro.");
				return null;
			}
		}else {
			FacesUtil.mensajeError("Ingrese un nombre al Grupo para continuar con el registro.");
			return null;
		}
	}
	
	public String irEditarGrupoConocimiento(Grupo item) {
		aefGrupo = item;
		aefListEstadoGrupo = cargarEstadosGrupo();
		return "irEditarGrupoConocimiento";
	}
	
	public String irRegistrarGrupo(){
		if (aefDependenciaId != GeneralesConstantes.APP_ID_BASE) {
			if (aefCarreraId != GeneralesConstantes.APP_ID_BASE) {
				if (aefGrupo.getGrpDescripcion().length() > 0) {
					aefGrupo.setGrpCarrera(new Carrera(aefCarreraId));
					aefGrupo.setGrpEstado(GrupoConstantes.ESTADO_GRUPO_ACTIVO_VALUE);
					try {
						boolean retorno = servGrupo.crear(aefGrupo);
						if (retorno) {
							limpiarFormListarGrupos();	
							FacesUtil.mensajeInfo("El nuevo Grupo fué registrado con éxito.");
						}
						return "irListarGrupoConocimiento";
					} catch (GrupoException e) {
						FacesUtil.mensajeError(e.getMessage());
						return null;
					}
				}else {
					FacesUtil.mensajeError("Ingrese un nombre al Grupo para continuar con el registro.");
					return null;
				}
			}else {
				FacesUtil.mensajeError("Seleccione una Carrera para continuar con el registro.");
				return null;
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar con el registro.");
			return null;
		}
		
	}
	
	public String irNuevoGrupoConocimiento(){
		iniciarFormNuevoGrupo();
		return "irNuevoGrupoConocimiento";
	}
	
	
	public String irListarAreasConocimiento(Usuario usuario) {
		aefUsuario = usuario;
		
		try {
			aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
			aefCarreraId = GeneralesConstantes.APP_ID_BASE;
			aefListDependencia = servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			
			return "irListarAreasConocimiento";
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
		
	}

	public String irListarGrupos(){
		limpiarFormListarGrupos();
		return "irListarGrupoConocimiento";
	}
	
	public String irInicio() {
		aefGrupo = null;
		aefDependenciaId= null;
		aefCarreraId = null;
		aefGrupoId = null;
		aefListDependencia = null;
		aefListCarrera = null;
		aefListGrupo = null;
		aefListMateria = null;
		aefTipoBusqueda = null;
		return "irInicio";
	}
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	public void busquedaPorIdentificacion(){
		
		if (aefIdentificacion.length() > 0) {
			aefPrimerApellido = new String();
			aefTipoBusqueda = FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (aefPrimerApellido.length() > 0) {
			aefIdentificacion = new String();
			aefTipoBusqueda = FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	
	
	public void agregarNuevoGrupo(){
		if (aefDependenciaId != GeneralesConstantes.APP_ID_BASE) {
			if (aefCarreraId != GeneralesConstantes.APP_ID_BASE) {
				if (aefGrupo.getGrpDescripcion().length() > 0) {
					aefGrupo.setGrpCarrera(new Carrera(aefCarreraId));
					aefGrupo.setGrpEstado(GrupoConstantes.ESTADO_GRUPO_ACTIVO_VALUE);
					aefGrupo.setGrpDescripcion(aefGrupo.getGrpDescripcion().toUpperCase());
					
					if (!verificarSiExisteEsaArea()) {
						try {
							boolean retorno = servGrupo.crear(aefGrupo);
							if (retorno) {
								aefGrupo = new Grupo();	
								aefListGrupo = cargarGruposPorCarreras();
								FacesUtil.mensajeInfo("La nueva Área fué registrada con éxito.");
							}
						} catch (GrupoException e) {
							FacesUtil.mensajeError(e.getMessage());
						}
					}else {
						FacesUtil.mensajeInfo("El Área que intenta registrar ya existe.");
					}
					
				}else {
					FacesUtil.mensajeError("Ingrese un nombre al Área para continuar con el registro.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una Carrera para continuar con el registro.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar con el registro.");
		}
	}
	
	private boolean verificarSiExisteEsaArea() {
		List<Grupo> auxListGrupo = cargarGruposPorCarreras();
		if (auxListGrupo != null && auxListGrupo.size() >0) {
			for (Grupo item : auxListGrupo) {
				if (GeneralesUtilidades.quitarEspaciosEnBlanco(item.getGrpDescripcion()).toUpperCase().equals(GeneralesUtilidades.quitarEspaciosEnBlanco(aefGrupo.getGrpDescripcion()).toUpperCase())) {
					return true;
				}
			}
		}
		
		return false;
	}



	private void iniciarFormNuevoGrupo(){
		aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
		aefCarreraId = GeneralesConstantes.APP_ID_BASE;
		aefGrupoId = GeneralesConstantes.APP_ID_BASE;
		aefListCarrera = null;
		aefGrupo = new Grupo();
	}
	
	public void limpiarFormListarGrupos(){
		aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
		aefCarreraId = GeneralesConstantes.APP_ID_BASE;
		aefGrupoId = GeneralesConstantes.APP_ID_BASE;
		aefListCarrera = null;
		aefListGrupo = null;
	}
	
	public void limpiarFormListarCoordinadores(){
		aefPeriodoId = GeneralesConstantes.APP_ID_BASE;
		aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
		aefCarreraId = GeneralesConstantes.APP_ID_BASE;
		aefListCarrera = null;
		aefListGrupoDto = null;
	}
	
	public void limpiarFormListarMaterias(){
		aefDependenciaId = GeneralesConstantes.APP_ID_BASE;
		aefCarreraId = GeneralesConstantes.APP_ID_BASE;
		aefGrupoId = GeneralesConstantes.APP_ID_BASE;
		aefListCarrera = null;
		aefListGrupo = null;
		aefListMateria = null;
	}
	
	
	public void buscarCarreras(){
		if (aefDependenciaId != GeneralesConstantes.APP_ID_BASE) {
			aefListCarrera = cargarCarrerasPorDependencia();
		}else {
			aefListCarrera = null;
			FacesUtil.mensajeError("Seleccione una Facultad para continuar con la búsqueda.");
		}
	}
	
	public void buscarGrupos(){
		if (aefDependenciaId != GeneralesConstantes.APP_ID_BASE) {
			if (aefCarreraId != GeneralesConstantes.APP_ID_BASE) {
				aefListGrupo = cargarGruposPorCarreras();
			}else {
				aefListGrupo = null;
				FacesUtil.mensajeError("Seleccione una Carrera para continuar con la búsqueda.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar con la búsqueda.");
		}
	}
	
	public void buscarCoordinadores(){
		if (aefPeriodoId != GeneralesConstantes.APP_ID_BASE) {
			if (aefDependenciaId != GeneralesConstantes.APP_ID_BASE) {
				if (aefCarreraId != GeneralesConstantes.APP_ID_BASE) {
					aefListGrupoDto = cargarCoordinadoresPorGrupo();
				}else {
					aefListGrupoDto = null;
					FacesUtil.mensajeError("Seleccione una Carrera para continuar con la búsqueda.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una Facultad para continuar con la búsqueda.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un Período para continuar con la búsqueda.");
		}
	}
	
	private List<GrupoDto> cargarCoordinadoresPorGrupo() {
		List<GrupoDto> retorno = null;

		try {
			retorno = servJdbcGrupo.buscarCoordinadoresPorGrupo(aefCarreraId, aefPeriodoId);
			if (retorno != null && retorno.size() > 0) {
				retorno.sort(Comparator.comparing(GrupoDto::getGrpDescripcion));
			}
		} catch (GrupoValidacionException e) {
			FacesUtil.mensajeError("Se encontró mas de un coordinador asignados al mismo grupo.");
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}



	public void buscarMaterias(){
		if (aefDependenciaId != GeneralesConstantes.APP_ID_BASE) {
			if (aefCarreraId != GeneralesConstantes.APP_ID_BASE) {
				List<GrupoDto> grupos = cargarGruposDtoporCarreras();
				if (grupos != null && grupos.size() > 0) {
					List<Materia> auxListMateria = cargarMateriasPorCarreras();
					if (auxListMateria != null && auxListMateria.size() >0) {
						List<MateriaDto> materias = new ArrayList<>();
						MateriaDto materiaDto = null;
						for (Materia item : auxListMateria) {
							materiaDto = new MateriaDto();
							materiaDto.setMtrId(item.getMtrId());
							materiaDto.setMtrCodigo(item.getMtrCodigo()!= null ? item.getMtrCodigo() : "");
							materiaDto.setMtrDescripcion(item.getMtrDescripcion());
							materiaDto.setGrpId(item.getMtrGrupo() != null ? item.getMtrGrupo().getGrpId(): GeneralesConstantes.APP_ID_BASE);
							materiaDto.setMtrListGrupoDto(grupos);
							materias.add(materiaDto);
						}
						
						materias.sort(Comparator.comparing(MateriaDto::getMtrDescripcion));	
						
						aefListMateria = materias;
					}
					
				}else {
					aefListMateria = null;
					FacesUtil.mensajeError("Debe agregar grupos para continuar con la asignación de materias.");
				}
			}else {
				aefListMateria = null;
				FacesUtil.mensajeError("Seleccione una Carrera para continuar con la búsqueda.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar con la búsqueda.");
		}
	}
	
	public String etiquetarEstadosGrupo(int estado){
		String retorno = "";
		switch (estado) {
		case 0:
			retorno= GrupoConstantes.ESTADO_GRUPO_ACTIVO_LABEL;
			break;

		case 1:
			retorno = GrupoConstantes.ESTADO_GRUPO_INACTIVO_LABEL;
			break;
		}

		return  retorno;
	}
	
	public void busquedaAvanzadaDocentes() {
		String param = null;
		
		if (!aefTipoBusqueda.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			if (aefTipoBusqueda.intValue() == FIND_BY_IDENTIFICACION) {
				param = aefIdentificacion;
			}else {
				param = aefPrimerApellido;
			}

			List<PersonaDto>  docentes = cargarDocentesPorFacultad(param, aefDependenciaId, aefTipoBusqueda);
			if (docentes != null && docentes.size() > 0) {
				aefListPersonaDto = docentes;
			}else {
				aefListPersonaDto = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del docente para continuar con la búsqueda.");
		}
	}

	/**
	 * Método que permite cargar docentes en funcion a la facultad.
	 */
	private List<PersonaDto> cargarDocentesPorFacultad(String param, int dependencia , int tipoBusqueda) {
		List<PersonaDto> retorno = null;
		
		try {
			retorno = servJdbcPersonaDto.buscarDocentesPorDependencia(aefPeriodoId, dependencia, param, tipoBusqueda);
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Docentes con Carga Horaria Activa.");
		}
		
		return retorno;
	}

	/**
	 * Método que permite cargar docentes en funcion a los grupos y carreras.
	 */
	private List<PersonaDto> cargarDocentesPorGruposCarrera() {
		List<PersonaDto> retorno = null;
		
		try {
			retorno = servJdbcPersonaDto.buscarDocentesPorGrupo(aefPeriodoId, aefCarreraId, aefGrupoDto.getGrpId());
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Docentes con Carga Horaria Activa.");
		}
		
		return retorno;
	}


	public void registrarCoordinadorElegido(PersonaDto item){

		try {
			
			if (verificarPlanificacionActiva()) {
				if (aefGrupoDto.getGrpPersonaDtoEvaluador() != null && aefGrupoDto.getGrpPersonaDtoEvaluador().getPrsId() != item.getPrsId()) {//el coordinador es diferente al seleccionado asigno.
					servJdbcGrupo.agregarCoordinador(item, aefPeriodoId, aefCarreraId, aefGrupoDto.getGrpId());
					aefListGrupoDto = null;
					aefListGrupoDto = cargarCoordinadoresPorGrupo();
				}else {
					FacesUtil.mensajeError("El docente que quiere asignar se encuentra registrado como Evaluador al Par. Seleccione otra persona...");
				}
			}
			
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public void registrarEvaluadorElegido(PersonaDto item){

		try {
			
			if (verificarPlanificacionActiva()) {
				if (aefGrupoDto.getGrpPersonaDtoCoordinador() != null && aefGrupoDto.getGrpPersonaDtoCoordinador().getPrsId() != item.getPrsId()) {//el coordinador es diferente al seleccionado asigno.
					servJdbcGrupo.agregarEvaluador(item, aefPeriodoId, aefCarreraId, aefGrupoDto.getGrpId());
					aefListGrupoDto = null;
					aefListGrupoDto = cargarCoordinadoresPorGrupo();
				}else {
					FacesUtil.mensajeError("El docente que quiere asignar se encuentra registrado como Coordinador. Seleccione otra persona...");
				}
			}
			
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	private boolean verificarPlanificacionActiva() {
		try{  
			aefEvaluacion = servEvaluacionServicio.buscarActivoXTipo(TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_VALUE);
			if (aefEvaluacion.getEvPeriodoAcademico().getPracId() == aefPeriodoId.intValue()) {
				return true;	
			}else {
				FacesUtil.mensajeError("No es posible realizar asignaciones en un Período de Evaluación Inactivo.");
				return false;
			}
			
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.verificar.acceso.evaluacion.no.activa.validacion.exception")));
			return false;
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
			return false;
		} catch (EvaluacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
			return false;
		}    
	}



	public void limpiarFormBusquedaCoord(){
		aefIdentificacion = new String();
		aefPrimerApellido = new String();
		aefTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		aefListPersonaDto = null; 
		aefListPersonaDto = cargarDocentesPorGruposCarrera();
	}
	
	public void vaciarFormBusquedaCoord(){
		aefIdentificacion = new String();
		aefPrimerApellido = new String();
		aefTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		aefListPersonaDto = null; 
	}
	
	private List<SelectItem> cargarEstadosGrupo(){
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(GrupoConstantes.ESTADO_GRUPO_ACTIVO_VALUE, GrupoConstantes.ESTADO_GRUPO_ACTIVO_LABEL));
		retorno.add(new SelectItem(GrupoConstantes.ESTADO_GRUPO_INACTIVO_VALUE, GrupoConstantes.ESTADO_GRUPO_INACTIVO_LABEL));
		return retorno;
	}
	
	
	private List<Materia> cargarMateriasPorCarreras(){
		List<Materia> retorno = null;

		try {
			retorno = servMateria.buscarMateriasPorCarrera(aefCarreraId);
		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró materias vinculadas a la carrera solicitada.");
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	private List<GrupoDto> cargarGruposDtoporCarreras(){
		List<GrupoDto> retorno = null;

		try {
			List<Grupo> grupos = servGrupo.buscarPorCarrera(aefCarreraId);
			if (grupos != null && grupos.size() >0) {
				List<GrupoDto> gruposDto = new ArrayList<>();
				for (Grupo item : grupos) {
					GrupoDto grupo = new GrupoDto();
					grupo.setGrpId(item.getGrpId());
					grupo.setGrpDescripcion(item.getGrpDescripcion());
					gruposDto.add(grupo);
				}
				gruposDto.sort(Comparator.comparing(GrupoDto::getGrpDescripcion));
				retorno = gruposDto;
			}
			
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró grupos vinculados a la carrera solicitada.");
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<Grupo> cargarGruposPorCarreras(){
		List<Grupo> retorno = null;

		try {
			retorno = servGrupo.buscarPorCarrera(aefCarreraId);
			if (retorno != null && retorno.size() > 0) {
				retorno.sort(Comparator.comparing(Grupo::getGrpDescripcion));
			}else {
				FacesUtil.mensajeError("No se encontró grupos vinculados a la carrera solicitada.");
			}
					
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró grupos vinculados a la carrera solicitada.");
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	private List<Carrera> cargarCarrerasPorDependencia(){
		List<Carrera> retorno = null;
		
		try {
			retorno = servCarrera.buscarCarreras(aefDependenciaId, CarreraConstantes.TIPO_PREGRADO_VALUE);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró carreras con los parámetros ingresados.");
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	// ****************************************************************/
	// *********** METODOS PARA ENCAPSULAMIENTO  **********************/
	// ****************************************************************/

	
	public Usuario getAefUsuario() {
		return aefUsuario;
	}

	public void setAefUsuario(Usuario aefUsuario) {
		this.aefUsuario = aefUsuario;
	}

	public Grupo getAefGrupo() {
		return aefGrupo;
	}

	public void setAefGrupo(Grupo aefGrupo) {
		this.aefGrupo = aefGrupo;
	}

	public Integer getAefDependenciaId() {
		return aefDependenciaId;
	}

	public void setAefDependenciaId(Integer aefDependenciaId) {
		this.aefDependenciaId = aefDependenciaId;
	}

	public Integer getAefCarreraId() {
		return aefCarreraId;
	}

	public void setAefCarreraId(Integer aefCarreraId) {
		this.aefCarreraId = aefCarreraId;
	}

	public Integer getAefGrupoId() {
		return aefGrupoId;
	}

	public void setAefGrupoId(Integer aefGrupoId) {
		this.aefGrupoId = aefGrupoId;
	}

	public List<Dependencia> getAefListDependencia() {
		return aefListDependencia;
	}

	public void setAefListDependencia(List<Dependencia> aefListDependencia) {
		this.aefListDependencia = aefListDependencia;
	}

	public List<Carrera> getAefListCarrera() {
		return aefListCarrera;
	}

	public void setAefListCarrera(List<Carrera> aefListCarrera) {
		this.aefListCarrera = aefListCarrera;
	}

	public List<Grupo> getAefListGrupo() {
		return aefListGrupo;
	}

	public void setAefListGrupo(List<Grupo> aefListGrupo) {
		this.aefListGrupo = aefListGrupo;
	}

	public List<SelectItem> getAefListEstadoGrupo() {
		return aefListEstadoGrupo;
	}

	public void setAefListEstadoGrupo(List<SelectItem> aefListEstadoGrupo) {
		this.aefListEstadoGrupo = aefListEstadoGrupo;
	}

	public List<MateriaDto> getAefListMateria() {
		return aefListMateria;
	}

	public void setAefListMateria(List<MateriaDto> aefListMateria) {
		this.aefListMateria = aefListMateria;
	}



	public List<GrupoDto> getAefListGrupoDto() {
		return aefListGrupoDto;
	}



	public void setAefListGrupoDto(List<GrupoDto> aefListGrupoDto) {
		this.aefListGrupoDto = aefListGrupoDto;
	}



	public List<PeriodoAcademico> getAefListPeriodoAcademico() {
		return aefListPeriodoAcademico;
	}



	public void setAefListPeriodoAcademico(List<PeriodoAcademico> aefListPeriodoAcademico) {
		this.aefListPeriodoAcademico = aefListPeriodoAcademico;
	}



	public Integer getAefPeriodoId() {
		return aefPeriodoId;
	}



	public void setAefPeriodoId(Integer aefPeriodoId) {
		this.aefPeriodoId = aefPeriodoId;
	}



	public String getAefPrimerApellido() {
		return aefPrimerApellido;
	}



	public void setAefPrimerApellido(String aefPrimerApellido) {
		this.aefPrimerApellido = aefPrimerApellido;
	}



	public String getAefIdentificacion() {
		return aefIdentificacion;
	}



	public void setAefIdentificacion(String aefIdentificacion) {
		this.aefIdentificacion = aefIdentificacion;
	}



	public Integer getAefTipoBusqueda() {
		return aefTipoBusqueda;
	}



	public void setAefTipoBusqueda(Integer aefTipoBusqueda) {
		this.aefTipoBusqueda = aefTipoBusqueda;
	}



	public List<PersonaDto> getAefListPersonaDto() {
		return aefListPersonaDto;
	}



	public void setAefListPersonaDto(List<PersonaDto> aefListPersonaDto) {
		this.aefListPersonaDto = aefListPersonaDto;
	}



	public GrupoDto getAefGrupoDto() {
		return aefGrupoDto;
	}



	public void setAefGrupoDto(GrupoDto aefGrupoDto) {
		this.aefGrupoDto = aefGrupoDto;
	}


	
}