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
   
 ARCHIVO:     AdministracionPeriodoAcademicoForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de la tabla PeriodoAcademico.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
13-JUL-2017			Vinicio Rosales                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.MallaPeriodoDto;
import ec.edu.uce.academico.ejb.dtos.MatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaPeriodoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaPeriodoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionPeriodoAcademicoForm. Managed Bean que
 * maneja las peticiones para la administración de la tabla PeriodoAcademico.
 * 
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "administracionPeriodoAcademicoForm")
@SessionScoped
public class AdministracionPeriodoAcademicoForm implements Serializable {

	private static final long serialVersionUID = -5706899311603575398L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// para buscar
	private PeriodoAcademico apafPeriodoAcademicoBuscar;
	// para nuevo
	private PeriodoAcademico apafPeriodoAcademicoEditar;
	private Integer apafValidadorClick;
	// para listar búsqueda
	private List<PeriodoAcademico> apafListPeriodoAcademico;
	// para la paginacion
	private String apafPaginacionSt;
	// para eliminar
	private PeriodoAcademico apafConocimientoEliminar;
	// para retornar el estado
	private String estado;
	// para capturar el usuario dga, posgrado
	private Usuario apafUsuarioBuscar;
	// para diferencia el tipo de usuario
	private Integer apafTipoUsuario;
	// para diferencia el tipo de carrera/programa
	private String apafTipoCarrera;
	// para listar las mallas_periodo
	private List<MallaPeriodoDto> apafListMallaPeriodo;
	// para guardar las mallas vigentes
	private MallaPeriodo apafMallaPeriodoEditar;

	private PeriodoAcademico apafPrac;
	// para listar los procesos del período
	private List<ProcesoFlujo> apafListProcesoFlujo;
	private List<PeriodoAcademicoDto> apafListPeriodoAcademicoGuardar;
	private List<PeriodoAcademicoDto> apafListPeriodoAcademicoGuardarNivelacion;
	private List<MatriculaDto> apafListEstudiantesInscritosMatriculaDto;
	private boolean apafHabilitarCronograma;
	private PeriodoAcademicoDto apafPeriodoAcademicoNuevo;
	private String apafTipoPeriodoAcademico;

	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	@EJB	private PeriodoAcademicoServicio servApafPeriodoAcademico;
	@EJB	private UsuarioRolServicio servApafUsuarioRolServicio;
	@EJB	private MallaPeriodoDtoServicioJdbc servApafMallaPeriodoDtoServicioJdbc;
	@EJB	private MallaPeriodoServicio servApafMallaPeriodoServicio;
	@EJB	private ProcesoFlujoServicio servProcesoFlujoServicio;
	@EJB	private MatriculaServicioJdbc servJdbcMatricula;
	@EJB	private FichaInscripcionServicio servFichaInscripcion;

	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/

	/**
	 * Incicializa las variables para la funcionalidad de administración de la
	 * tabla Periodo Academico
	 * @return navegacion al xhtml de listar Periodo Academico
	 */
	public String irAdministracionPeriodoAcademico(Usuario usuario) {
		apafUsuarioBuscar = usuario;

		try {
			List<UsuarioRol> usro = servApafUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDGA_VALUE.intValue()
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
					apafTipoUsuario = RolConstantes.ROL_ADMINDGA_VALUE.intValue();
					apafTipoCarrera = "carreras";
					apafTipoPeriodoAcademico="Período Académico:";
				} else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
					apafTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
					apafTipoCarrera = "programas";
					apafTipoPeriodoAcademico="Cohorte:";
				}
			}
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
		}

		iniciarParametros();
		return "irAdministracionPeriodoAcademico";
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al
	 * iniciar la funcionalidad
	 * 
	 * @return navegacion al inicio
	 */
	public void iniciarParametros() {
		// iniciar el objeto de búsqueda
		apafPeriodoAcademicoBuscar = new PeriodoAcademico();
		// inicio la lista de peridodos academicos
		if (apafTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()
				|| apafTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
			apafListPeriodoAcademico = servApafPeriodoAcademico.listarTodos();
		} else if (apafTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
			apafListPeriodoAcademico = servApafPeriodoAcademico.listarTodosPosgradoDesc();
		}

		// inicio el string de paginacion
		apafPaginacionSt = new String();
		// inicio el validador del click en 0
		apafValidadorClick = 0;
		apafHabilitarCronograma = false;
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al
	 * iniciar la funcionalidad
	 * 
	 * @return navegacion al inicio
	 */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}

	/**
	 * Método para limpiar los parámtros de busqueda ingresados
	 */
	public void limpiar() {
		iniciarParametros();
	}

	/**
	 * Método para buscar la entidad deseada con los parámetros de búsqueda
	 * ingresados
	 */
	public void buscar(PeriodoAcademico periodoAcademico) {
		// busca el periodo academico con la descripcion
		try {
			if (apafTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()
					|| apafTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
				apafListPeriodoAcademico = servApafPeriodoAcademico.buscarXestadolist(periodoAcademico.getPracEstado());
			} else if (apafTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
				apafListPeriodoAcademico = servApafPeriodoAcademico
						.buscarXestadolistPosgrado(periodoAcademico.getPracEstado());
			}

		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"AdministracionPeriodoAcademico.buscar.periodoAcademico.exception")));
		}
	}

	/**
	 * Método para ir a la funcionalidad de editar un perido academico
	 * 
	 * @return navagación al xhtml editarPeriodoAcademico
	 */
	public String irEditarPeriodoAcademico(PeriodoAcademico periodoAcademico) {
		// instancio el objeto de periodo academico para la edicion del mismo
		apafPeriodoAcademicoEditar = periodoAcademico;
		// genero string para paginacion de editar
		apafPaginacionSt = "Editar";
		if (apafTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()
				|| apafTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
			return "irEditarPeriodoAcademico";
		} else if (apafTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
			return "irEditarPeriodoAcademicoPosgrado";
		}
		return null;
	}

	/**
	 * Método para ir a generar el nuevo periodo academico
	 * @return navagación al xhtml listarPeriodoAcademico
	 * @throws MallaPeriodoDtoNoEncontradoException 
	 * @throws MallaPeriodoDtoException 
	 */
	public String irNuevoPeriodoAcademico() throws MallaPeriodoDtoException, MallaPeriodoDtoNoEncontradoException{
		
		try {
			apafPeriodoAcademicoEditar = new PeriodoAcademico();//instancio el objeto de periodo academico para la creacion del mismo
			apafPeriodoAcademicoEditar.setPracEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			apafPaginacionSt = "Nuevo";// genero string para paginacion de editar
			
			PeriodoAcademico periodo = servApafPeriodoAcademico.buscarPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			if (periodo != null) {
				apafListEstudiantesInscritosMatriculaDto = cargarEstudiantesInscritosPeriodoVigente(periodo);
			}
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError("Verifique que el último Período Académico se encuentre activo.");
			return null;
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.mensajeError("Se encontró mas de un Período Académico activo.");
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
		return "irNuevoPeriodoAcademico";
	}

	private List<MatriculaDto> cargarEstudiantesInscritosPeriodoVigente(PeriodoAcademico periodo) {
		List<MatriculaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcMatricula.buscarEstudiantesInscritos(periodo.getPracId());
		} catch (MatriculaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró estudiantes inscritos en el periodo " + periodo.getPracDescripcion() + "." );
		} catch (MatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	/**
	 * Método para devolver el estado del periodo academico
	 */
	public String estadoPrac(int estadoPrac) {
		if (estadoPrac == 0) {
			estado = "ACTIVO";
		}
		if (estadoPrac == 1) {
			estado = "INACTIVO";
		}
		if (estadoPrac == 2) {
			estado = "EN CIERRE";
		}
		return estado;

	}

	/**
	 * Método para crear un nuevo periodo academico
	 * 
	 * @return navagación al xhtml listarPeriodoAcademico
	 * @throws MallaPeriodoDtoNoEncontradoException
	 * @throws MallaPeriodoDtoException
	 * @throws MallaPeriodoException
	 * @throws MallaPeriodoValidacionException
	 */
	public String crearPeriodoAcademico() throws MallaPeriodoDtoException, MallaPeriodoDtoNoEncontradoException,			MallaPeriodoValidacionException, MallaPeriodoException {

		try {
			apafPeriodoAcademicoEditar.setPracDescripcion(GeneralesUtilidades.eliminarEnterYEspaciosEnBlanco(apafPeriodoAcademicoEditar.getPracDescripcion()));
			try {
				List<UsuarioRol> usro = servApafUsuarioRolServicio.buscarXUsuario(apafUsuarioBuscar.getUsrId());
				for (UsuarioRol item : usro) {
					if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDGA_VALUE.intValue()
							|| item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
						apafPeriodoAcademicoEditar.setPracTipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
					} else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
						apafPeriodoAcademicoEditar.setPracTipo(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
					}
				}
			} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			}
			servApafPeriodoAcademico.anadir(apafPeriodoAcademicoEditar, apafTipoUsuario, apafListPeriodoAcademicoGuardar, apafListPeriodoAcademicoGuardarNivelacion);
			apafPrac = servApafPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			apafListMallaPeriodo = servApafMallaPeriodoDtoServicioJdbc.listarMallasPeriodoXPeriodo(apafPrac.getPracId());
		
			for (MallaPeriodoDto item : apafListMallaPeriodo) {
				try {
					servApafMallaPeriodoServicio.actualizaMallaPeriodo(apafPrac.getPracId());
				} catch (MallaPeriodoNoEncontradoException e) {
				}
				
				servApafMallaPeriodoServicio.anadir(new MallaPeriodo(new MallaCurricular(item.getMlprMlcrId()),
						new PeriodoAcademico(apafPeriodoAcademicoEditar.getPracId()),
						MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE));
			}

			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionPeriodoAcademico.crear.nuevo.periodoAcademico.exitoso")));
//			cambiarEstadoFichaInscripcionEstudiantesInscritosPeriodoEnCierre();
//			cambiarEstadoFichaInscripcionEstudiantesNoMatriculadosPeriodoEnCierre(periodo);

			iniciarParametros();
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarParametros();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarParametros();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "irAdministracionPeriodoAcademico";
	}

	private void cambiarEstadoFichaInscripcionEstudiantesNoMatriculadosPeriodoEnCierre(PeriodoAcademico periodo) {
		try {
			List<MatriculaDto> estudiantes = servJdbcMatricula.buscarEstudiantesPregrado();
			if (!estudiantes.isEmpty()) {
				for (MatriculaDto inscrito : estudiantes) {

					try{
						servJdbcMatricula.buscarEstudianteMatriculado(inscrito.getMtrPersonaDto().getPrsIdentificacion(), inscrito.getFcinId(), periodo.getPracId());
					} catch (MatriculaNoEncontradoException e) {
						// System.out.println(inscrito.getMtrPersonaDto().getPrsIdentificacion());
//						try {
//							servFichaInscripcion.desactivarFichaInscripcion(inscrito.getFcinId());
//						} catch (FichaEstudianteNoEncontradoException e1) {
//							FacesUtil.mensajeError(e1.getMessage());
//						} catch (FichaEstudianteException e1) {
//							FacesUtil.mensajeError(e1.getMessage());
//						} catch (FichaEstudianteValidacionException e1) {
//							FacesUtil.mensajeError(e1.getMessage());
//						}
					} catch (MatriculaException e) {
						FacesUtil.mensajeError(e.getMessage());
					}
				}

			}
		} catch (MatriculaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró estudiantes inscritos en Pregrado.");
		} catch (MatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
	}

	private void cambiarEstadoFichaInscripcionEstudiantesInscritosPeriodoEnCierre() {
		if (!apafListEstudiantesInscritosMatriculaDto.isEmpty()) {
			for (MatriculaDto matricula : apafListEstudiantesInscritosMatriculaDto) {
				try {
					servFichaInscripcion.desactivarFichaInscripcion(matricula.getFcinId());
				} catch (FichaEstudianteNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FichaEstudianteException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FichaEstudianteValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
			}
		}
	}

	/**
	 * Método para editar un periodo academico
	 * 
	 * @return navagación al xhtml listarPeriodoAcademico
	 */
	public String editarPeriodoAcademico() {
		try {
			apafPeriodoAcademicoEditar.setPracDescripcion(GeneralesUtilidades
					.eliminarEnterYEspaciosEnBlanco(apafPeriodoAcademicoEditar.getPracDescripcion()));
			if (apafTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()
					|| apafTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
				servApafPeriodoAcademico.editar(apafPeriodoAcademicoEditar);
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
						"AdministracionPeriodoAcademico.editar.periodoAcademico.exitoso")));
			} else if (apafTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
				servApafPeriodoAcademico.editarPosgrado(apafPeriodoAcademicoEditar);
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("AdministracionPeriodoAcademico.editar.cohorte.exitoso")));
			}

			iniciarParametros();
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarParametros();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarParametros();
		}

		return "irAdministracionPeriodoAcademico";
	}

	/**
	 * Método para cancelar la edicion o creacion de un periodo academico
	 * 
	 * @return navegacion al xhtml de listar periodo academico
	 */
	public String cancelar() {
		limpiar();
		return "cancelar";
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	/**
	 * verifica que haga click en el boton nuevo periodo academico
	 */
	public String habilitarCreacionCronograma() {
		apafValidadorClick = 1;
		return null;
	}

	/**
	 * verifica que haga click en el boton nuevo periodo academico
	 */
	public String verificarClickNuevoPeriodoAcademico() {
		if (apafPeriodoAcademicoEditar.getPracEstado().intValue() == PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE) {
			FacesUtil.mensajeError("No puede crear período académico con estado inactivo");
		} else {
			List<PeriodoAcademico> listaPeriodoTodosC = new ArrayList<>();
			int tieneEstadoCierre = 0;
			listaPeriodoTodosC = servApafPeriodoAcademico.listarTodos();
			for (PeriodoAcademico item : listaPeriodoTodosC) {
				if (item.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE) {
					tieneEstadoCierre = 1;
				}
			}
			if (apafPeriodoAcademicoEditar.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE
					&& tieneEstadoCierre == 1) {
				FacesUtil.mensajeError(
						"Existe un periodo académico en proceso de cierre, no puede crear un periodo académico ");
			} else if (apafPeriodoAcademicoEditar.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE
					&& tieneEstadoCierre == 1) {
				FacesUtil.mensajeError(
						"Existe un periodo académico en proceso de cierre, no puede crear un periodo académico ");
			} else {
				if (!apafHabilitarCronograma) {
					apafListProcesoFlujo = new ArrayList<ProcesoFlujo>();
					try {
						apafListProcesoFlujo = servProcesoFlujoServicio.listarTodos();
						apafListPeriodoAcademicoGuardar = new ArrayList<PeriodoAcademicoDto>();
						apafListPeriodoAcademicoGuardarNivelacion = new ArrayList<PeriodoAcademicoDto>();
						for (ProcesoFlujo item : apafListProcesoFlujo) {
							if (item.getPrflId() == 6
									|| item.getPrflId() == 2
									|| item.getPrflId() == 5
									|| item.getPrflId() == 3
									|| item.getPrflId() == 4
									|| item.getPrflId() == 7
									|| item.getPrflId() == 8
									|| item.getPrflId() == 10
									|| item.getPrflId() == 12
									|| item.getPrflId() == 13
									|| item.getPrflId() == 14
									|| item.getPrflId() == 15
									|| item.getPrflId() == 16
									|| item.getPrflId() == 17
									|| item.getPrflId() == 18
									|| item.getPrflId() == 19
									|| item.getPrflId() == 22) {
								PeriodoAcademicoDto pracAux = new PeriodoAcademicoDto();
								pracAux.setPrflId(item.getPrflId());
								pracAux.setPrflDescripcion(item.getPrflDescripcion());
								apafListPeriodoAcademicoGuardar.add(pracAux);
								apafListPeriodoAcademicoGuardarNivelacion.add(pracAux);
							}
						}
					} catch (ProcesoFlujoNoEncontradoException | ProcesoFlujoException e) {
					}
					apafHabilitarCronograma = true;
				} else {
					boolean op = false;
					for (PeriodoAcademicoDto item : apafListPeriodoAcademicoGuardar) {
						if (item.getPlcrFechaInicio() != null && item.getPlcrFechaFin() != null) {
							// Están las fechas requeridas, no hace falta ningún
							// proceso
						} else {
							op = true;
						}
					}
					if (op) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Por favor, llene todos lo campos correspondientes a fechas.");
					} else {
						apafValidadorClick = 1;
					}
				}
			}
		}
		return null;
	}

	/**
	 * verifica que haga click en el boton editar periodo academico
	 */
	public String verificarClickEditarPeriodoAcademico() {
		apafValidadorClick = 0;
		List<PeriodoAcademico> listaPeriodoTodos = new ArrayList<>();
		int tieneEstadoActivo = 0;
		int tieneEstadoCierre = 0;
		listaPeriodoTodos = servApafPeriodoAcademico.listarTodos();
		for (PeriodoAcademico item : listaPeriodoTodos) {
			if (item.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE) {
				tieneEstadoActivo = 1;
			}
			if (item.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE) {
				tieneEstadoCierre = 1;
			}
		}
		if (apafPeriodoAcademicoEditar.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE
				&& tieneEstadoActivo == 1) {
			FacesUtil.mensajeError("Ya existe un periodo académico con el estado activo");
		} else if (apafPeriodoAcademicoEditar.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE
				&& tieneEstadoCierre == 1) {
			FacesUtil.mensajeError("Ya existe un periodo académico con el estado en cierre");
		} else {
			apafValidadorClick = 2;
		}
		return null;
	}

	public String verificarClickEditarCohorte() {
		apafValidadorClick = 2;
		return null;
	}

	/**
	 * setea el verificador del click a 0 para nuevas validaciones
	 */
	public void setearVerificadorClick() {
		apafValidadorClick = 0;
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	public PeriodoAcademico getApafPeriodoAcademicoBuscar() {
		return apafPeriodoAcademicoBuscar;
	}

	public void setApafPeriodoAcademicoBuscar(PeriodoAcademico apafPeriodoAcademicoBuscar) {
		this.apafPeriodoAcademicoBuscar = apafPeriodoAcademicoBuscar;
	}

	public PeriodoAcademico getApafPeriodoAcademicoEditar() {
		return apafPeriodoAcademicoEditar;
	}

	public void setApafPeriodoAcademicoEditar(PeriodoAcademico apafPeriodoAcademicoEditar) {
		this.apafPeriodoAcademicoEditar = apafPeriodoAcademicoEditar;
	}

	public Integer getApafValidadorClick() {
		return apafValidadorClick;
	}

	public void setApafValidadorClick(Integer apafValidadorClick) {
		this.apafValidadorClick = apafValidadorClick;
	}

	public List<PeriodoAcademico> getApafListPeriodoAcademico() {
		apafListPeriodoAcademico = apafListPeriodoAcademico == null ? (new ArrayList<PeriodoAcademico>())
				: apafListPeriodoAcademico;
		return apafListPeriodoAcademico;
	}

	public void setApafListPeriodoAcademico(List<PeriodoAcademico> apafListPeriodoAcademico) {
		this.apafListPeriodoAcademico = apafListPeriodoAcademico;
	}

	public String getApafPaginacionSt() {
		return apafPaginacionSt;
	}

	public void setApafPaginacionSt(String apafPaginacionSt) {
		this.apafPaginacionSt = apafPaginacionSt;
	}

	public PeriodoAcademico getApafConocimientoEliminar() {
		return apafConocimientoEliminar;
	}

	public void setApafConocimientoEliminar(PeriodoAcademico apafConocimientoEliminar) {
		this.apafConocimientoEliminar = apafConocimientoEliminar;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Usuario getApafUsuarioBuscar() {
		return apafUsuarioBuscar;
	}

	public void setApafUsuarioBuscar(Usuario apafUsuarioBuscar) {
		this.apafUsuarioBuscar = apafUsuarioBuscar;
	}

	public Integer getApafTipoUsuario() {
		return apafTipoUsuario;
	}

	public void setApafTipoUsuario(Integer apafTipoUsuario) {
		this.apafTipoUsuario = apafTipoUsuario;
	}

	public String getApafTipoCarrera() {
		return apafTipoCarrera;
	}

	public void setApafTipoCarrera(String apafTipoCarrera) {
		this.apafTipoCarrera = apafTipoCarrera;
	}

	public List<MallaPeriodoDto> getApafListMallaPeriodo() {
		return apafListMallaPeriodo;
	}

	public void setApafListMallaPeriodo(List<MallaPeriodoDto> apafListMallaPeriodo) {
		this.apafListMallaPeriodo = apafListMallaPeriodo;
	}

	public MallaPeriodo getApafMallaPeriodoEditar() {
		return apafMallaPeriodoEditar;
	}

	public void setApafMallaPeriodoEditar(MallaPeriodo apafMallaPeriodoEditar) {
		this.apafMallaPeriodoEditar = apafMallaPeriodoEditar;
	}

	public boolean isApafHabilitarCronograma() {
		return apafHabilitarCronograma;
	}

	public void setApafHabilitarCronograma(boolean apafHabilitarCronograma) {
		this.apafHabilitarCronograma = apafHabilitarCronograma;
	}

	public List<ProcesoFlujo> getApafListProcesoFlujo() {
		return apafListProcesoFlujo;
	}

	public void setApafListProcesoFlujo(List<ProcesoFlujo> apafListProcesoFlujo) {
		this.apafListProcesoFlujo = apafListProcesoFlujo;
	}

	public PeriodoAcademicoDto getApafPeriodoAcademicoNuevo() {
		return apafPeriodoAcademicoNuevo;
	}

	public void setApafPeriodoAcademicoNuevo(PeriodoAcademicoDto apafPeriodoAcademicoNuevo) {
		this.apafPeriodoAcademicoNuevo = apafPeriodoAcademicoNuevo;
	}

	public List<PeriodoAcademicoDto> getApafListPeriodoAcademicoGuardar() {
		return apafListPeriodoAcademicoGuardar;
	}

	public void setApafListPeriodoAcademicoGuardar(List<PeriodoAcademicoDto> apafListPeriodoAcademicoGuardar) {
		this.apafListPeriodoAcademicoGuardar = apafListPeriodoAcademicoGuardar;
	}

	public List<PeriodoAcademicoDto> getApafListPeriodoAcademicoGuardarNivelacion() {
		return apafListPeriodoAcademicoGuardarNivelacion;
	}

	public void setApafListPeriodoAcademicoGuardarNivelacion(
			List<PeriodoAcademicoDto> apafListPeriodoAcademicoGuardarNivelacion) {
		this.apafListPeriodoAcademicoGuardarNivelacion = apafListPeriodoAcademicoGuardarNivelacion;
	}

	public PeriodoAcademico getApafPrac() {
		return apafPrac;
	}

	public void setApafPrac(PeriodoAcademico apafPrac) {
		this.apafPrac = apafPrac;
	}

	public List<MatriculaDto> getApafListEstudiantesInscritosMatriculaDto() {
		return apafListEstudiantesInscritosMatriculaDto;
	}

	public void setApafListEstudiantesInscritosMatriculaDto(List<MatriculaDto> apafListEstudiantesInscritosMatriculaDto) {
		this.apafListEstudiantesInscritosMatriculaDto = apafListEstudiantesInscritosMatriculaDto;
	}

	public String getApafTipoPeriodoAcademico() {
		return apafTipoPeriodoAcademico;
	}

	public void setApafTipoPeriodoAcademico(String apafTipoPeriodoAcademico) {
		this.apafTipoPeriodoAcademico = apafTipoPeriodoAcademico;
	}

	

}
