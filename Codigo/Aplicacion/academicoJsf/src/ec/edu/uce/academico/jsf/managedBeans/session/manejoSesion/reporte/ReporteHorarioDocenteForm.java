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
   
 ARCHIVO:     ReporteHorarioDocenteForm.java	  
 DESCRIPCION: Bean de sesion que maneja el horario del docente 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-ABRIL-2017 			Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReporteHorarioDocenteForm.
 * Managed Bean que administra los que maneja el horario del docente
 * @author v1-dcollaguazo.
 * @author v2-fgguzman
 * @version 2.0
 */

@ManagedBean(name="reporteHorarioDocenteForm")
@SessionScoped
public class ReporteHorarioDocenteForm implements Serializable{

	private static final long serialVersionUID = 6593041410113173885L;
	public static final String PATH_GENERAL_REPORTE = "/academico/reportes/";
	public static final String PATH_GENERAL_IMG_PIE = "/academico/reportes/imagenes/plantillaPie.png";
	public static final String PATH_GENERAL_IMG_CABECERA = "/academico/reportes/imagenes/plantillaCabecera.png";
	public static final String GENERAL_NOMBRE_INSTITUCION = "UNIVERSIDAD \nCENTRAL \nDEL ECUADOR";
	public static final String GENERAL_TITULO_REPORTE_HORARIO_ACADEMICO = "HORARIO ACADÉMICO";
	public static final String GENERAL_PIE_PAGINA = "Copyright Universidad Central del Ecuador 2018";
	public static final String GENERAL_DOC_AUTOGENERADO = "Documento generado en siiu.uce.edu.ec el " +  GeneralesUtilidades.cambiarDateToStringFormatoFecha(Date.from(Instant.now()), "dd/MM/yyyy HH:mm:ss");
	

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario rhdfUsuario;
	private DocenteJdbcDto rhdfDocenteDto;
	private List<HorarioAcademicoDto> rhdfListPeriodoAcademicoDto;
	private List<HorarioAcademicoDto> rhdfListHorarioAcademicoDto;
	private List<HorarioAcademicoDto> rhdfListHorarioAcademicoDtoCarga;
	private Integer rhdfValidadorClic;
	private Integer rhdfPeriodoId;
	
	
	
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB private CarreraDtoServicioJdbc servRhdfCarreraDtoServicioJdbc;
	@EJB private NivelDtoServicioJdbc servRhdfNivelDtoServicioJdbc;
	@EJB private MateriaDtoServicioJdbc servRhdfMateriaDtoServicioJdbc;
	@EJB private EstudianteDtoServicioJdbc servRhdfEstudianteDtoServicioJdbc;
	@EJB private UbicacionDtoServicioJdbc servRhdfUbicacionDtoServicioJdbc;
	@EJB private PeriodoAcademicoDtoServicioJdbc servRhdfPeriodoAcademicoDtoServicioJdbc;
	@EJB private DocenteDtoServicioJdbc servRhdfDocenteDtoServicioJdbc;
	@EJB private ParaleloDtoServicioJdbc servRhdfParaleloDtoServicioJdbc;
	@EJB private HorarioAcademicoDtoServicioJdbc servRhdfHorarioAcademicoDtoServicioJdbc;
	@EJB private PeriodoAcademicoServicio servRhdfPeriodoAcademicoServicio;
	@EJB private DependenciaServicio servRhdfDependenciaServicio;
	@EJB private CarreraServicio servRhdfCarreraServicio;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/

	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irHorarioAcademico(Usuario usuario){
		rhdfUsuario = usuario;
		
		try {
			List<HorarioAcademicoDto> horarioDocente = servRhdfHorarioAcademicoDtoServicioJdbc.buscarHorarioDocente(usuario.getUsrIdentificacion());
			
			if (horarioDocente != null && horarioDocente.size()> 0) {
				List<HorarioAcademicoDto> periodosAcademicos = new ArrayList<>();
				Map<Integer, HorarioAcademicoDto> mapHorarioDocente =  new HashMap<Integer, HorarioAcademicoDto>();
				
				for (HorarioAcademicoDto horario : horarioDocente) {
					mapHorarioDocente.put(horario.getPracId(), horario);
				}
				for (Entry<Integer, HorarioAcademicoDto> medico : mapHorarioDocente.entrySet()) {
					periodosAcademicos.add(medico.getValue());
				}
				
				periodosAcademicos.sort(Comparator.comparing(HorarioAcademicoDto::getPracId));

				rhdfListHorarioAcademicoDtoCarga = horarioDocente;
				rhdfListPeriodoAcademicoDto = periodosAcademicos;
				rhdfPeriodoId = GeneralesConstantes.APP_ID_BASE;
				rhdfListHorarioAcademicoDto = null;
				return "irReporteHorarioDocente";	
			}else {
				return null;	
			}
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;	
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;	
		}
		
	}
	

	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		rhdfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		rhdfListHorarioAcademicoDto = null;
		rhdfListHorarioAcademicoDtoCarga = null;
		rhdfListPeriodoAcademicoDto = null;
		desactivarModalReporte();
		return "irInicio";
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		rhdfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		rhdfListHorarioAcademicoDto = null;
		desactivarModalReporte();
	}
	
	/**
	 * Método que genera el reporte
	 */
	public void generarReporteHorarioDocente(List<HorarioAcademicoDto> listaHorarioParalelo){
		
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
		frmRrmParametros.put("nombres", rhdfUsuario.getUsrPersona().getPrsNombres() + " "+ rhdfUsuario.getUsrPersona().getPrsPrimerApellido() + " "+ rhdfUsuario.getUsrPersona().getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  rhdfUsuario.getUsrIdentificacion());
		frmRrmParametros.put("facultad", listaHorarioParalelo.get(0).getDpnDescripcion());
		frmRrmParametros.put("carrera", listaHorarioParalelo.get(0).getCrrDescripcion());
		frmRrmParametros.put("nick", rhdfUsuario.getUsrNick());

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
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir(){
		
		if (rhdfListHorarioAcademicoDto != null && rhdfListHorarioAcademicoDto.size() > 0 ) {
			activarModalReporte();
			generarReporteHorarioDocente(rhdfListHorarioAcademicoDto);
		}else {
			FacesUtil.mensajeInfo("Usted no dispone de un horario cargado para generar el PDF.");
		}
	}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void activarModalReporte() {
		rhdfValidadorClic = 1;
	}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void desactivarModalReporte() {
		rhdfValidadorClic = 0;
	}
	
	
	public void buscarHorarioDocente(Integer periodoId){
		
		if (periodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			try {
				List<HorarioAcademicoDto> horarioAcademico = servRhdfHorarioAcademicoDtoServicioJdbc.buscarHorarioDocente(rhdfUsuario.getUsrIdentificacion());
				List<HorarioAcademicoDto> horarios =null;
				
				if (horarioAcademico != null && horarioAcademico.size() > 0) {
					horarios = new ArrayList<>();
 					for (HorarioAcademicoDto it : horarioAcademico) {
						if (it.getPracId() == periodoId.intValue()) {
							horarios.add(it);
						}
					}
				} else {
					FacesUtil.mensajeInfo("Usted no tiene registrado un horario en este período académico.");
				}
				
				if (horarios != null && horarios.size() > 0) {
					rhdfListHorarioAcademicoDto = llenarTablaHorario(horarios);
				} else {
					FacesUtil.mensajeInfo("Seleccione un período académico para continuar.");
				}


			} catch (HorarioAcademicoDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (HorarioAcademicoDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		} else {
			FacesUtil.mensajeInfo("Seleccione un Período académico para continuar.");
		}


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
					horarioAcademicoAux.setMtrDscLunes(horario.getMtrDescripcion()+" - PARALELO: "+horario.getPrlDescripcion());
					break;
				case 1:
					horarioAcademicoAux.setMtrDscMartes(horario.getMtrDescripcion()+" - PARALELO: "+horario.getPrlDescripcion());
					break;
				case 2:
					horarioAcademicoAux.setMtrDscMiercoles(horario.getMtrDescripcion()+" - PARALELO: "+horario.getPrlDescripcion());
					break;
				case 3:
					horarioAcademicoAux.setMtrDscJueves(horario.getMtrDescripcion()+" - PARALELO: "+horario.getPrlDescripcion());
					break;
				case 4:
					horarioAcademicoAux.setMtrDscViernes(horario.getMtrDescripcion()+" - PARALELO: "+horario.getPrlDescripcion());
					break;
				case 5:
					horarioAcademicoAux.setMtrDscSabado(horario.getMtrDescripcion()+" - PARALELO: "+horario.getPrlDescripcion());
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

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	public Usuario getRhdfUsuario() {
		return rhdfUsuario;
	}


	public void setRhdfUsuario(Usuario rhdfUsuario) {
		this.rhdfUsuario = rhdfUsuario;
	}


	public DocenteJdbcDto getRhdfDocenteDto() {
		return rhdfDocenteDto;
	}


	public void setRhdfDocenteDto(DocenteJdbcDto rhdfDocenteDto) {
		this.rhdfDocenteDto = rhdfDocenteDto;
	}


	public List<HorarioAcademicoDto> getRhdfListPeriodoAcademicoDto() {
		return rhdfListPeriodoAcademicoDto;
	}


	public void setRhdfListPeriodoAcademicoDto(List<HorarioAcademicoDto> rhdfListPeriodoAcademicoDto) {
		this.rhdfListPeriodoAcademicoDto = rhdfListPeriodoAcademicoDto;
	}


	public List<HorarioAcademicoDto> getRhdfListHorarioAcademicoDto() {
		return rhdfListHorarioAcademicoDto;
	}


	public void setRhdfListHorarioAcademicoDto(List<HorarioAcademicoDto> rhdfListHorarioAcademicoDto) {
		this.rhdfListHorarioAcademicoDto = rhdfListHorarioAcademicoDto;
	}


	public Integer getRhdfValidadorClic() {
		return rhdfValidadorClic;
	}


	public void setRhdfValidadorClic(Integer rhdfValidadorClic) {
		this.rhdfValidadorClic = rhdfValidadorClic;
	}


	public Integer getRhdfPeriodoId() {
		return rhdfPeriodoId;
	}


	public void setRhdfPeriodoId(Integer rhdfPeriodoId) {
		this.rhdfPeriodoId = rhdfPeriodoId;
	}


	public List<HorarioAcademicoDto> getRhdfListHorarioAcademicoDtoCarga() {
		return rhdfListHorarioAcademicoDtoCarga;
	}


	public void setRhdfListHorarioAcademicoDtoCarga(List<HorarioAcademicoDto> rhdfListHorarioAcademicoDtoCarga) {
		this.rhdfListHorarioAcademicoDtoCarga = rhdfListHorarioAcademicoDtoCarga;
	}

	
	
}
