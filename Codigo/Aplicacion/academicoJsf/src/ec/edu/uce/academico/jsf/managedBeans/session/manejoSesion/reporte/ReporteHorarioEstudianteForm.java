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
   
 ARCHIVO:     ReporteHorarioEstudianteForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para el reporte de horario por estudiante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
09-ABRIL-2018			Dennis Collaguazo 			      Emisión Inicial
25-JUN-2018		    	Freddy Guzmán 			      Actualizacion Horarios
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.MatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReporteHorarioEstudianteForm. Managed Bean que maneja
 * las peticiones para el reporte de horario por estudiante.
 * 
 * @author dcollaguazo v1.0
 * @author fgguzman v2.0
 * @version 2.0
 */
@ManagedBean(name = "reporteHorarioEstudianteForm")
@SessionScoped
public class ReporteHorarioEstudianteForm implements Serializable {

	private static final long serialVersionUID = -5422984994284974811L;
	public static final String PATH_GENERAL_REPORTE = "/academico/reportes/";
	public static final String PATH_GENERAL_IMG_PIE = "/academico/reportes/imagenes/plantillaPie.png";
	public static final String PATH_GENERAL_IMG_CABECERA = "/academico/reportes/imagenes/plantillaCabecera.png";
	public static final String GENERAL_NOMBRE_INSTITUCION = "UNIVERSIDAD \nCENTRAL \nDEL ECUADOR";
	public static final String GENERAL_TITULO_REPORTE_HORARIO_ACADEMICO = "HORARIO ACADÉMICO";
	public static final String GENERAL_PIE_PAGINA = "Copyright Universidad Central del Ecuador 2018";
	public static final String GENERAL_DOC_AUTOGENERADO = "Documento generado en siiu.uce.edu.ec el " +  GeneralesUtilidades.cambiarDateToStringFormatoFecha(Date.from(Instant.now()), "dd/MM/yyyy HH:mm:ss");
	
	
	// *******************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE *************************/
	// *******************************************************************/

	private Usuario rhefUsuario;
	private List<HorarioAcademicoDto> rhefListHorarioAcademico;
	private List<MatriculaDto> rhefListMatriculaDto;

	// PARA BUSQUEDA DE HORARIO ACADEMICO
	private EstudianteJdbcDto rhefEstudiante;
	private PeriodoAcademico rhefPeriodoAcademico;
	private Integer rhefCrrId;
	private List<CarreraDto> rhefListCarreraDtoBusq;
	private List<MateriaDto> rhefListMateriaDtoBusq;
	private List<FichaMatriculaDto> rhefListMatricula;
	private List<EstudianteJdbcDto> rhefListEstudiante;
	private List<PeriodoAcademico> rhefListPeriodoAcademico;
	private List<HorarioAcademicoDto> rhefListHorarioAcademicoBusq;
	private Integer rhefValidadorClic;
	private Integer rhefHabiltaBoton;

	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {
	}

	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB private HorarioAcademicoDtoServicioJdbc servRhefHorarioAcademicoDtoServicioJdbc;
	@EJB private PeriodoAcademicoServicio servRhefPeriodoAcademicoServicio;
	@EJB private DependenciaServicio servRhefDependenciaServicio;
	@EJB private CarreraServicio servRhefCarreraServicio;
	@EJB private FichaMatriculaDtoServicioJdbc servRhefFichaMatriculaDtoServicioJdbc;
	@EJB private EstudianteDtoServicioJdbc servRhefEstudianteDtoServicioJdbc;
	@EJB private MatriculaServicioJdbc servRhefMatriculaServicioJdbc;
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto; 


	// ********************************************************************/
	// ************* METODOS GENERALES DE LA CLASE ************************/
	// ********************************************************************/
	/**
	 * Dirige la navegación hacia la página de listar
	 * @param usuario - usuario login
	 * @return pagina listar
	 */
	public String irHorarioAcademico(Usuario usuario) {
		rhefUsuario = usuario;

		try {
			List<String> estado = new ArrayList<>();
			estado.add(String.valueOf(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE));

			List<String> tipo = new ArrayList<>();
			tipo.add(String.valueOf(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE));
			tipo.add(String.valueOf(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE));
			tipo.add(String.valueOf(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE));

			List<MatriculaDto> matriculas = servRhefMatriculaServicioJdbc.buscarMatriculasActivas(usuario.getUsrIdentificacion(), estado, tipo);
			if (matriculas != null && matriculas.size() > 0) {
				rhefListMatriculaDto = new ArrayList<>();
				rhefCrrId = GeneralesConstantes.APP_ID_BASE;
				Map<Integer, MatriculaDto> mapMatriculas = new HashMap<Integer, MatriculaDto>();

				for (MatriculaDto it : matriculas) {
					mapMatriculas.put(it.getCrrId(), it);
				}

				for (Entry<Integer, MatriculaDto> matricula : mapMatriculas.entrySet()) {
					rhefListMatriculaDto.add(matricula.getValue());
				}

			}

			desactivarModalReporte();
		} catch (MatriculaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return "irReporteHorarioEstudiante";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		rhefListHorarioAcademico = null;
		rhefCrrId = null;
		rhefListMatriculaDto = null;
		rhefValidadorClic = null;
		return "irInicio";
	}

	/**
	 * Navega hacia la pagina del listar las materias del paralelo
	 * @return Navegacion a la pagina del listar las materias del paralelo.
	 */
	public String irListarHorarioAcademico() {
		String retorno = null;
		retorno = "irHorarioAcademico";
		return retorno;
	}

	/**
	 * Método que limpia los parametros de busqueda
	 */
	public void limpiar() {
		rhefListHorarioAcademico = null;
		rhefCrrId = GeneralesConstantes.APP_ID_BASE;
		desactivarModalReporte();
	}

	// ********************************************************************/
	// *********************** METODOS GENERALES *************************/
	// ********************************************************************/


	/**
	 * Método que busca materias del periodo , carrera , nivel y paralelo
	 */
	public void buscarHorario() {

		if (rhefCrrId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			MatriculaDto matricula = null;

			for (MatriculaDto it : rhefListMatriculaDto) {
				if (it.getCrrId().intValue() == rhefCrrId.intValue()) {
					matricula = it;
					break;
				}
			}

			if (matricula != null) {

				List<HorarioAcademicoDto> horarioAcademico = cargarHorarioClases(rhefCrrId, matricula.getPracId(), rhefUsuario.getUsrIdentificacion());
				if (!horarioAcademico.isEmpty()) {

					List<HorarioAcademicoDto> horarioModulos = new ArrayList<>();
					for (HorarioAcademicoDto item : horarioAcademico) {

						if (item.getMtrTimtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {

							List<MateriaDto> modulos = cargarModulosPorModular(item.getMtrId());
							if (!modulos.isEmpty()) {
								for (MateriaDto modulo : modulos) {
									horarioModulos.addAll(cargarHorarioPorModulo(item.getPrlId(), modulo.getMlcrmtId(), item.getPracId()));
								}
							}

						}

					}
					
					Iterator<HorarioAcademicoDto> iterHorario = horarioAcademico.iterator();
					while (iterHorario.hasNext()) {
						HorarioAcademicoDto item = (HorarioAcademicoDto) iterHorario.next();
						if (item.getMtrTimtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
							iterHorario.remove();
						}
					}
					
					horarioAcademico.addAll(horarioModulos);
					
					if (!horarioAcademico.isEmpty()) {
						rhefListHorarioAcademico = llenarTablaHorario(horarioAcademico);	
					}
					
				} else {
					FacesUtil.mensajeInfo("Usted no tiene registrada una matrícula en esta carrera.");
				}
			}

		} else {
			FacesUtil.mensajeInfo("Seleccione una Carrera para continuar.");
		}

	}
	

	private List<HorarioAcademicoDto> cargarHorarioPorModulo(int prlId, int mlcrmtId, Integer pracId) {
		List<HorarioAcademicoDto> retorno = new ArrayList<>(); 
		
		try {
			retorno = servRhefHorarioAcademicoDtoServicioJdbc.buscarHorarioModulo(prlId, mlcrmtId, pracId);
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	private List<MateriaDto> cargarModulosPorModular(Integer mtrId) {
		List<MateriaDto> retorno = new ArrayList<>();
		try {
			retorno = servJdbcMallaCurricularMateriaDto.buscarModulos(mtrId);
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}

	private List<HorarioAcademicoDto> cargarHorarioClases(Integer carreraId, Integer periodoId, String identificacion) {
		List<HorarioAcademicoDto> retorno = new ArrayList<>();
		
		try {
			retorno = servRhefHorarioAcademicoDtoServicioJdbc.buscarHorarioEstudiante(carreraId, periodoId, identificacion);
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	private List<HorarioAcademicoDto> llenarTablaHorario(List<HorarioAcademicoDto> materiasPorEstudiante) {
		List<HorarioAcademicoDto> horarioAcademico = new ArrayList<>();
		Map<LocalTime, List<HorarioAcademicoDto>> horarioMateria2 = materiasPorEstudiante.stream().collect(Collectors.groupingBy(HorarioAcademicoDto::getHoclHoInicio));

		horarioMateria2.forEach((k, v) -> {
			HorarioAcademicoDto horarioAcademicoAux = new HorarioAcademicoDto();
			v.forEach(horario -> {
				horarioAcademicoAux.setHoclHoInicio(k);
				horarioAcademicoAux.setMtrDscHora(k + " " + k.plusHours(1));
				horarioAcademicoAux.setDpnDescripcion(v.get(0) == null ? "":v.get(0).getDpnDescripcion());
				horarioAcademicoAux.setCrrDescripcion(v.get(0) == null ? "":v.get(0).getCrrDescripcion());
				horarioAcademicoAux.setPracDescripcion(v.get(0) == null ? "":v.get(0).getPracDescripcion());
				switch (horario.getHracDia()) {
				case 0:
					horarioAcademicoAux.setMtrDscLunes(horario.getMtrDescripcion()+"\nPARALELO: "+horario.getPrlCodigo()+"\nAULA: "+horario.getAlaCodigo());
					break;
				case 1:
					horarioAcademicoAux.setMtrDscMartes(horario.getMtrDescripcion()+"\nPARALELO: "+horario.getPrlCodigo()+"\nAULA: "+horario.getAlaCodigo());
					break;
				case 2:
					horarioAcademicoAux.setMtrDscMiercoles(horario.getMtrDescripcion()+"\nPARALELO: "+horario.getPrlCodigo()+"\nAULA: "+horario.getAlaCodigo());
					break;
				case 3:
					horarioAcademicoAux.setMtrDscJueves(horario.getMtrDescripcion()+"\nPARALELO: "+horario.getPrlCodigo()+"\nAULA: "+horario.getAlaCodigo());
					break;
				case 4:
					horarioAcademicoAux.setMtrDscViernes(horario.getMtrDescripcion()+"\nPARALELO: "+horario.getPrlCodigo()+"\nAULA: "+horario.getAlaCodigo());
					break;
				case 5:
					horarioAcademicoAux.setMtrDscSabado(horario.getMtrDescripcion()+"\nPARALELO: "+horario.getPrlCodigo()+"\nAULA: "+horario.getAlaCodigo());
					break;
				default:
					break;
				}
			});
			horarioAcademico.add(horarioAcademicoAux);
		});
		horarioAcademico.sort(Comparator.comparing(HorarioAcademicoDto::getHoclHoInicio));
		return horarioAcademico;
	}

	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en
	 * pantalla del pdf
	 */
	public void verificarClickImprimir() {

		if (rhefListHorarioAcademico != null && rhefListHorarioAcademico.size() > 0) {
			activarModalReporte();
			generarReporteHorarioParalelo(rhefListHorarioAcademico);
		}else {
			FacesUtil.mensajeInfo("Usted no dispone de un horario cargado para generar el PDF.");
		}
		
	}
	

	/**
	 * Método que genera el reporte
	 */
	public void generarReporteHorarioParalelo(List<HorarioAcademicoDto> listaHorarioParalelo) {

		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "HORARIO ACADÉMICO";

		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_HORARIO_ACADEMICO);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO " + listaHorarioParalelo.get(0).getPracDescripcion());
		frmRrmParametros.put("nombres", rhefUsuario.getUsrPersona().getPrsNombres() + " "+ rhefUsuario.getUsrPersona().getPrsPrimerApellido() + " "+ rhefUsuario.getUsrPersona().getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  rhefUsuario.getUsrIdentificacion());
		frmRrmParametros.put("facultad", listaHorarioParalelo.get(0).getDpnDescripcion());
		frmRrmParametros.put("carrera", listaHorarioParalelo.get(0).getCrrDescripcion());
		frmRrmParametros.put("nick", rhefUsuario.getUsrNick());

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (HorarioAcademicoDto item : listaHorarioParalelo) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("hora", item.getMtrDscHora() == null ? "":capitalizarOracion(item.getMtrDscHora()));
			datoHorario.put("lunes", item.getMtrDscLunes() == null ? "":capitalizarOracion(item.getMtrDscLunes()));
			datoHorario.put("martes", item.getMtrDscMartes() == null ? "":capitalizarOracion(item.getMtrDscMartes()));
			datoHorario.put("miercoles", item.getMtrDscMiercoles() == null ? "":capitalizarOracion(item.getMtrDscMiercoles()));
			datoHorario.put("jueves", item.getMtrDscJueves() == null ? "":capitalizarOracion(item.getMtrDscJueves()));
			datoHorario.put("viernes", item.getMtrDscViernes() == null ? "":capitalizarOracion(item.getMtrDscViernes()));
			datoHorario.put("sabado", item.getMtrDscSabado() == null ? "":capitalizarOracion(item.getMtrDscSabado()));
			frmRrmCampos.add(datoHorario);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRrmParametros);

	}
	
	/**
	 * Método que permite poner la primera letra en Mayusculas
	 * @param param - nombre de la materia.
	 * @return Oracion capitalizada.
	 */
	public static String capitalizarOracion(String param) {
		  if (param == null || param.isEmpty()) {
		    return "";            
		  } else {
		    return param.substring(0, 1).toUpperCase() + param.substring(1).toUpperCase();
		  }
		}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void activarModalReporte() {
		rhefValidadorClic = 1;
	}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void desactivarModalReporte() {
		rhefValidadorClic = 0;
	}

	// HABILITA SEGÚN SEA EL CASO EN CABECERA DEL XHTML DE HORAS O CREDITOS
	/**
	 * Cabecera de la columna que varia entre creditos y horas
	 */
	public String cabeceraCreditosHoras(Integer creditos, Integer horas) {
		if (creditos != null && creditos != 0) {
			return "Créditos";
		}
		if (horas != null && horas != 0) {
			return "Horas";
		}
		return "Horas - Créditos N/D";
	}

	// HABILITA EL TIPO DE ASIGNATURA - CREDITOS U HORAS
	/**
	 * Modalidad de la asignatura entre creditos y horas
	 */
	public Integer modalidadCreditosHoras(Integer creditos, Integer horas) {
		if (creditos != null && creditos != 0) {
			return 0;
		}
		if (horas != null && horas != 0) {
			return 1;
		}
		return GeneralesConstantes.APP_ID_BASE;
	}

	
	/**
	 * Método que permite convertir a un String - formato "Quito, 10 de Enero de 2018" tomando una
	 * fecha tipo java.Util.Date.
	 * @author FREDDY - fgguzman
	 * @param java.Util.Date - parametro de entrada.
	 * @return String - representa la fecha en formato "10 de Enero de 2018".
	 */
	public static String cambiarDateToStringFormatoCaberaDocumento(Date date) {
		DateFormat formateador = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("es", "EC"));
		return formateador.format(date);
	}
	
	
	
	// ********************************************************************/
	// *********************** METODOS GET Y SET **************************/
	// ********************************************************************/
	public Usuario getRhefUsuario() {
		return rhefUsuario;
	}

	public void setRhefUsuario(Usuario rhefUsuario) {
		this.rhefUsuario = rhefUsuario;
	}

	public PeriodoAcademico getRhefPeriodoAcademico() {
		return rhefPeriodoAcademico;
	}

	public void setRhefPeriodoAcademico(PeriodoAcademico rhefPeriodoAcademico) {
		this.rhefPeriodoAcademico = rhefPeriodoAcademico;
	}

	public Integer getRhefCrrId() {
		return rhefCrrId;
	}

	public void setRhefCrrId(Integer rhefCrrId) {
		this.rhefCrrId = rhefCrrId;
	}

	public List<CarreraDto> getRhefListCarreraDtoBusq() {
		rhefListCarreraDtoBusq = rhefListCarreraDtoBusq == null ? (new ArrayList<CarreraDto>())
				: rhefListCarreraDtoBusq;
		return rhefListCarreraDtoBusq;
	}

	public void setRhefListCarreraDtoBusq(List<CarreraDto> rhefListCarreraDtoBusq) {
		this.rhefListCarreraDtoBusq = rhefListCarreraDtoBusq;
	}

	public List<MateriaDto> getRhefListMateriaDtoBusq() {
		rhefListMateriaDtoBusq = rhefListMateriaDtoBusq == null ? (new ArrayList<MateriaDto>())
				: rhefListMateriaDtoBusq;
		return rhefListMateriaDtoBusq;
	}

	public void setRhefListMateriaDtoBusq(List<MateriaDto> rhefListMateriaDtoBusq) {
		this.rhefListMateriaDtoBusq = rhefListMateriaDtoBusq;
	}

	public List<HorarioAcademicoDto> getRhefListHorarioAcademicoBusq() {
		rhefListHorarioAcademicoBusq = rhefListHorarioAcademicoBusq == null ? (new ArrayList<HorarioAcademicoDto>())
				: rhefListHorarioAcademicoBusq;
		return rhefListHorarioAcademicoBusq;
	}

	public void setRhefListHorarioAcademicoBusq(List<HorarioAcademicoDto> rhefListHorarioAcademicoBusq) {
		this.rhefListHorarioAcademicoBusq = rhefListHorarioAcademicoBusq;
	}

	public Integer getRhefValidadorClic() {
		return rhefValidadorClic;
	}

	public void setRhefValidadorClic(Integer rhefValidadorClic) {
		this.rhefValidadorClic = rhefValidadorClic;
	}

	public EstudianteJdbcDto getRhefEstudiante() {
		return rhefEstudiante;
	}

	public void setRhefEstudiante(EstudianteJdbcDto rhefEstudiante) {
		this.rhefEstudiante = rhefEstudiante;
	}

	public List<FichaMatriculaDto> getRhefListMatricula() {
		return rhefListMatricula;
	}

	public void setRhefListMatricula(List<FichaMatriculaDto> rhefListMatricula) {
		this.rhefListMatricula = rhefListMatricula;
	}

	public List<EstudianteJdbcDto> getRhefListEstudiante() {
		rhefListEstudiante = rhefListEstudiante == null ? (new ArrayList<EstudianteJdbcDto>()) : rhefListEstudiante;
		return rhefListEstudiante;
	}

	public void setRhefListEstudiante(List<EstudianteJdbcDto> rhefListEstudiante) {
		this.rhefListEstudiante = rhefListEstudiante;
	}

	public List<PeriodoAcademico> getRhefListPeriodoAcademico() {
		rhefListPeriodoAcademico = rhefListPeriodoAcademico == null ? (new ArrayList<PeriodoAcademico>())
				: rhefListPeriodoAcademico;
		return rhefListPeriodoAcademico;
	}

	public void setRhefListPeriodoAcademico(List<PeriodoAcademico> rhefListPeriodoAcademico) {
		this.rhefListPeriodoAcademico = rhefListPeriodoAcademico;
	}

	public Integer getRhefHabiltaBoton() {
		return rhefHabiltaBoton;
	}

	public void setRhefHabiltaBoton(Integer rhefHabiltaBoton) {
		this.rhefHabiltaBoton = rhefHabiltaBoton;
	}

	public List<HorarioAcademicoDto> getRhefListHorarioAcademico() {
		return rhefListHorarioAcademico;
	}

	public void setRhefListHorarioAcademico(List<HorarioAcademicoDto> rhefListHorarioAcademico) {
		this.rhefListHorarioAcademico = rhefListHorarioAcademico;
	}

	public List<MatriculaDto> getRhefListMatriculaDto() {
		return rhefListMatriculaDto;
	}

	public void setRhefListMatriculaDto(List<MatriculaDto> rhefListMatriculaDto) {
		this.rhefListMatriculaDto = rhefListMatriculaDto;
	}

}
