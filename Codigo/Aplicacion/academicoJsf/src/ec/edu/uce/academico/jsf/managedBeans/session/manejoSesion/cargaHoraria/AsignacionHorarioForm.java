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
   
 ARCHIVO:      DireccionGestionAcademicaForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario Dieccion Academica y Gestion Academica. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 14-OCTUBRE-2018		 Freddy Guzman						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.HorarioFuncionDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleCargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioFuncionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFuncionCargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CargaHorariaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaEnum;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TiempoDedicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioFuncion;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteTemplateForm;
import ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial.CargaHorariaTemplate;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) AsignacionHorarioForm.java Bean de sesión que maneja los
 * atributos del formulario de Asignacion de Horarios.
 * 
 * @author fgguzman.
 * @version 1.0
 */

@SessionScoped
@ManagedBean(name = "asignacionHorarioForm")
public class AsignacionHorarioForm  extends ReporteTemplateForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;
	private final int CAHR_TIPO_HORARIO_CLASE = 1;
	private final int CAHR_TIPO_HORARIO_FUNCION = 2;

	public final int HOAC_TIPO_HORA_DOCENCIA_VALUE = 1;
	private final int HOAC_TIPO_HORA_PRACTICA_VALUE = 2;
	
	private final int TMDD_MAXIMO_HORAS_TIEMPO_COMPLETO = 40;
	private final int TMDD_MAXIMO_HORAS_MEDIO_TIEMPO = 20;
	private final int TMDD_MAXIMO_HORAS_TIEMPO_PARCIAL = 19;
	
	private final int MAXIMO_HORAS_DIARIO = 10;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario ahfUsuario;

	private Integer ahfActivarModal;
	private Integer ahfCarreraId;
	private Integer ahfDependenciaId;
	private Integer ahfPeriodoId;
	private Integer ahfCategoriaId;
	private Integer ahfActividadId;
	private Integer ahfMaximoHoras;
	private Integer ahfHorasAsignadas;
	
	private String ahfLinkReporte;
	private String ahfDiaLabel;
	private String ahfHoraLabel;
	private String ahfExtensionArchivo;
	private String ahfNombreArchivo;
	private String ahfTokenReporte;
	
	private PersonaDto ahfPersonaDto;
	private HoraClaseDto ahfSeleccionHoraClaseDto;
	
	private List<PersonaDto> ahfListPersonaDto;
	private List<Carrera> ahfListCarrera;
	private List<Dependencia> ahfListDependencia;
	private List<PeriodoAcademico> ahfListPeriodoAcademico;
	private List<HoraClaseDto> ahfListHoraClaseDto;
	
	private List<SelectItem> ahfListCategoriaCargaHoraria;
	private List<SelectItem> ahfListActividadCargaHoraria;
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB private DependenciaServicio servDependencia;
	@EJB private CarreraServicio servCarrera;
	@EJB private PersonaDatosDtoServicioJdbc servjdbcPersonaDatos;
	@EJB private PersonaServicio servPersona;
	@EJB private TipoFuncionCargaHorariaServicio servTipoFuncionCargaHoraria;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private PlanificacionCronogramaDtoServicioJdbc servJdbcPlanificacionCronogramaDto;
	@EJB private CargaHorariaServicio servCargaHoraria;
	@EJB private DetalleCargaHorariaServicio servDetalleCargaHoraria;
	@EJB private DocenteDatosDtoServicioJdbc servJdbcDocenteDatosDtox;
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private HorarioAcademicoServicio servHorarioAcademico;	
	@EJB private CargaHorariaServicioJdbc servJdbcCargaHorariaDto;
	@EJB private HoraClaseDtoServicioJdbc servHoraClaseDto;
	@EJB private HorarioFuncionServicio servHorarioFuncion;	


	
	// ****************************************************************/
	// ******************* METODOS DE NAVEGACION **********************/
	// ****************************************************************/

	public String irInicio() {

		ahfActivarModal = null;
		ahfCarreraId= null;
		ahfDependenciaId= null;
		ahfPeriodoId= null;
		ahfCategoriaId= null;
		ahfActividadId= null;
		
		ahfLinkReporte= null;
		ahfDiaLabel= null;
		ahfHoraLabel= null;
		
		ahfPersonaDto= null;
		ahfSeleccionHoraClaseDto= null;
		
		ahfListPersonaDto= null;
		ahfListCarrera= null;
		ahfListDependencia= null;
		ahfListPeriodoAcademico= null;
		ahfListHoraClaseDto= null;
		
		ahfListCategoriaCargaHoraria= null;
		ahfListActividadCargaHoraria= null;
		
		return "irInicio";
	}

	public String irAsignacionHorarios(Usuario usuario){
		ahfUsuario = usuario;
		ahfListPeriodoAcademico = new ArrayList<>();
		ahfListPeriodoAcademico.add(cargarPeriodoAcademico());
		ahfLinkReporte = "";
		limpiarFormDocentes();
		return "irBuscarDocentes";
	}
	
	
	public String irBuscarDocentes(){
		ahfPersonaDto = null;
		ahfListHoraClaseDto = null;
		ahfListCategoriaCargaHoraria = null;
		ahfListActividadCargaHoraria = null;
		return "irDocentes";
	}
	
	
	public String irVerHorarioDocente(PersonaDto docente) {
		List<HoraClaseDto> plantilla = cargarHorarioClasesTemplatePorAsignatura();
		if (!plantilla.isEmpty()) {
			ahfPersonaDto = docente;
			ahfPersonaDto.setPstDenominacion(CargaHorariaTemplate.establecerCategoria(ahfPersonaDto.getPstCategoria(), ahfPersonaDto.getPstNivelRangoGradual()));
			ahfListHoraClaseDto = plantilla;
			ahfHorasAsignadas = new Integer(0);
			
			establecerLimiteMaximoHoras();
			desactivarModalAsignacionHorario();
			
			recargarTemplate(docente);
			FacesUtil.limpiarMensaje();
			
			ahfListCategoriaCargaHoraria = getListCategoriaCargaHoraria();
			ahfListActividadCargaHoraria = null;
			
			return "irVerHorarioDocente";
		}else {
			FacesUtil.mensajeError("Error de conexión, comuníquese con el Administrador del Sistema.");
			return null;
		}
		
	}

	
	private void establecerLimiteMaximoHoras() {
		
		if (ahfPersonaDto.getTmddId() == TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE) {
			ahfMaximoHoras = TMDD_MAXIMO_HORAS_TIEMPO_COMPLETO;
		}else if (ahfPersonaDto.getTmddId() == TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE){
			ahfMaximoHoras = TMDD_MAXIMO_HORAS_MEDIO_TIEMPO;
		}else if (ahfPersonaDto.getTmddId() == TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE){
			ahfMaximoHoras = TMDD_MAXIMO_HORAS_TIEMPO_PARCIAL;
		}
		
	}

	// ****************************************************************/
	// ********************* METODOS GENERALES ************************/
	// ****************************************************************/

	public void limpiarFormDocentes() {
		ahfCarreraId= GeneralesConstantes.APP_ID_BASE;
		ahfDependenciaId= GeneralesConstantes.APP_ID_BASE;
		ahfPeriodoId= GeneralesConstantes.APP_ID_BASE;
		ahfListPersonaDto = null;
		verificarDatos();
	}
	
	
	public void buscarFacultades(){
		
		if (!ahfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			ahfListDependencia = cargarDependencias();
			Iterator<Dependencia> it = ahfListDependencia.iterator();
			while(it.hasNext()){
				Dependencia cad = (Dependencia) it.next();
				if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE
						|| cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE){
					it.remove();
				}
			}
		}else {
			FacesUtil.mensajeError("Seleccione un Periodo para continuar.");
		}
		
	}

	
	public void vaciarModalAsignacionHorario(){
		desactivarModalAsignacionHorario();
	}
	
	public void buscarActividades(){
		if (!ahfCategoriaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (ahfCategoriaId.equals(CargaHorariaEnum.CAT_DIRECCION_GESTION.getValue())) {
				ahfListActividadCargaHoraria = getListActividadesDireccionGestion();
			} else if (ahfCategoriaId.equals(CargaHorariaEnum.CAT_DOCENCIA.getValue())) {
				ahfListActividadCargaHoraria = getListActividadesDocencia();
			} else if (ahfCategoriaId.equals(CargaHorariaEnum.CAT_INVESTIGACION.getValue())) {
				ahfListActividadCargaHoraria = getListActividadesInvestigacion();
			} else {
				ahfListActividadCargaHoraria = null;
			}
		}else {
			FacesUtil.mensajeError("Seleccione una categoría para continuar.");
		}
	}
	
	public void asignarNuevaFuncion(HoraClaseDto item, int dia, String hora){
		
		desactivarModalAsignacionHorario();

		if (ahfHorasAsignadas.intValue() < ahfMaximoHoras.intValue() ) {
			
			if (!(verificarCupoMaximoDiario(dia) >= MAXIMO_HORAS_DIARIO)) {
				
				ahfCategoriaId = GeneralesConstantes.APP_ID_BASE;
				ahfActividadId = GeneralesConstantes.APP_ID_BASE;
				ahfHoraLabel = item.getHoclHoraInicio() + ":00 - " + item.getHoclHoraFin()+ ":00";
				ahfSeleccionHoraClaseDto = item;
				
				switch (dia) {
				case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_LUNES_LABEL;
					activarModalAsignacionHorario();
					break;
				case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_MARTES_LABEL;
					activarModalAsignacionHorario();
					break;
				case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL;
					activarModalAsignacionHorario();
					break;
				case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_JUEVES_LABEL;
					ahfHoraLabel = item.getHoclDescripcion();
					activarModalAsignacionHorario();
					break;
				case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_VIERNES_LABEL;
					activarModalAsignacionHorario();	
					break;
				case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_SABADO_LABEL;
					activarModalAsignacionHorario();
					break;
				case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
					ahfDiaLabel = HorarioAcademicoConstantes.DIA_DOMINGO_LABEL;
					activarModalAsignacionHorario();
					break;
				}
			}else {
				FacesUtil.mensajeInfo("No se puede agregar mas Actividades ya que exedería el límite diario.");
			}
			
		}else {
			FacesUtil.mensajeInfo("No se puede agregar mas Actividades ya que exedería el límite según su Tiempo de Dedicación.");
		}

	}
	

	private int verificarCupoMaximoDiario(int dia ) {
		int horasPorDia = 0;

		switch (dia) {
		case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclLunesHoraClaseDto().getHracId() != null && !item.getHoclLunesHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclMartesHoraClaseDto().getHracId() != null && !item.getHoclMartesHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}		
			break;
		case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclMiercolesHoraClaseDto().getHracId() != null && !item.getHoclMiercolesHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclJuevesHoraClaseDto().getHracId() != null && !item.getHoclJuevesHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclViernesHoraClaseDto().getHracId() != null && !item.getHoclViernesHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclSabadoHoraClaseDto().getHracId() != null && !item.getHoclSabadoHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}
			break;

		case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
			for (HoraClaseDto item : ahfListHoraClaseDto) {
				if (item.getHoclDomingoHoraClaseDto().getHracId() != null && !item.getHoclDomingoHoraClaseDto().getHracId().equals(0)) {
					horasPorDia = horasPorDia + 1;
				}
			}
			break;
		}

		return horasPorDia;
	}

	public void quitarHoraClase(HoraClaseDto horario, int dia, String hora){
		desactivarModalAsignacionHorario();

		if (horario.getHracHoraTipo() == null) {
			if (eliminarHorarioAcademico(horario)) {
				horario.setHoclDescripcion("");
				horario.setHoclCheckBox(Boolean.FALSE);
				recargarTemplate(ahfPersonaDto);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Horario de Actividad eliminado con éxito.");
			}	
		}else {
			FacesUtil.mensajeError("El Horario Académico debe ser modificado en la administracion de Horarios.");
		}
		
	}
	
	public Boolean verificarDatos(){
		if(ahfListPersonaDto != null && !ahfListPersonaDto.isEmpty()){
			ahfLinkReporte = "http://reportes.uce.edu.ec/Docentes/HorarioDocenteCarrera.aspx?idn="+ahfCarreraId+"&prd=2019-2019";
			return true;
		}
		return false;
	}
	
	public void buscarCarreras(){
		
		if (!ahfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ahfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				ahfListCarrera = cargarCarreras(ahfDependenciaId);
			}else {
				FacesUtil.mensajeError("Seleccione una Facultad para continuar.");	
			}
		}else {
			FacesUtil.mensajeError("Seleccione un Periodo para continuar.");
		}
		
	}
	
	public void verificarClickImprimir(){
		
		if (ahfListHoraClaseDto != null && !ahfListHoraClaseDto.isEmpty()) {
			activarModalImprimirReporte();
			ahfExtensionArchivo = "PDF";
			ahfNombreArchivo = "actividades";
			ahfTokenReporte = "CARGA_HORARIA_ACTIVIDADES";
			generarReporteHorarioDocente(ahfListHoraClaseDto, cargarDependencia(ahfDependenciaId), cargarCarrera(ahfCarreraId), cargarPeriodoAcademico(ahfPeriodoId));
		}else {
			FacesUtil.mensajeInfo("Usted no dispone de un horario cargado para generar el PDF.");
		}
		
	}
	
public void generarReporteHorarioDocente(List<HoraClaseDto> horario, Dependencia dependencia, Carrera carrera, PeriodoAcademico periodo){
		
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
		frmRrmParametros.put("encabezado_reporte", frmRrmNombreReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmRrmParametros.put("periodo", periodo.getPracDescripcion());
		frmRrmParametros.put("docente", ahfPersonaDto.getPrsNombres() + " "+ ahfPersonaDto.getPrsPrimerApellido() + " "+ ahfPersonaDto.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  ahfPersonaDto.getPrsIdentificacion());
		frmRrmParametros.put("facultad", dependencia.getDpnDescripcion());
//		frmRrmParametros.put("carrera", carrera.getCrrDescripcion());
		frmRrmParametros.put("relacion_laboral",  ahfPersonaDto.getRllbDescripcion());
		frmRrmParametros.put("categoria", ahfPersonaDto.getPstDenominacion());
		frmRrmParametros.put("tiempo_dedicacion", ahfPersonaDto.getTmddDescripcion());
		
		frmRrmParametros.put("cargo_label_1", "DOCENTE");
		frmRrmParametros.put("cargo_value_1", "-");
		frmRrmParametros.put("cargo_label_2", "DIRECTOR(A) DE CARRERA");
		frmRrmParametros.put("cargo_value_2", "");
		frmRrmParametros.put("nick", ahfUsuario.getUsrNick());
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		for (HoraClaseDto item : horario) {
			datoHorario = new HashMap<String, Object>();
			datoHorario.put("hora", item.getHoclDescripcion());
			datoHorario.put("lunes", item.getHoclLunesHoraClaseDto().getHoclDescripcion());
			datoHorario.put("martes", item.getHoclMartesHoraClaseDto().getHoclDescripcion());
			datoHorario.put("miercoles", item.getHoclMiercolesHoraClaseDto().getHoclDescripcion());
			datoHorario.put("jueves", item.getHoclJuevesHoraClaseDto().getHoclDescripcion());
			datoHorario.put("viernes", item.getHoclViernesHoraClaseDto().getHoclDescripcion());
			datoHorario.put("sabado", item.getHoclSabadoHoraClaseDto().getHoclDescripcion());
			datoHorario.put("domingo", item.getHoclDomingoHoraClaseDto().getHoclDescripcion());
			frmRrmCampos.add(datoHorario);
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte +"_"+ ahfPersonaDto.getPrsIdentificacion());
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	}

	public void asignarHorario(){
		if (!ahfCategoriaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ahfActividadId.equals(GeneralesConstantes.APP_ID_BASE)) {
				
				HorarioFuncion horario = new HorarioFuncion();
				horario.setHrfnHoraClase(new HoraClase(ahfSeleccionHoraClaseDto.getHoclId()));
				horario.setHrfnDia(ahfSeleccionHoraClaseDto.getHoclDiaValue());
				horario.setHrfnHoraInicio(Integer.valueOf(ahfSeleccionHoraClaseDto.getHoclHoraInicio()));
				horario.setHrfnHoraFin(Integer.valueOf(ahfSeleccionHoraClaseDto.getHoclHoraFin()));
				horario.setHrfnDescripcion(ahfSeleccionHoraClaseDto.getHoclDescripcion());
				horario.setHrfnFuncion(ahfCategoriaId);
				horario.setHrfnActividad(ahfActividadId);
				horario.setHrfnDetallePuesto(ahfPersonaDto.getDtpsId());
				horario.setHrfnPeriodoAcademico(ahfPeriodoId);
				horario.setHrfnEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);

				try {
					servHorarioFuncion.guardar(horario);
					recargarTemplate(ahfPersonaDto);
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Registro agregado con éxito.");
				} catch (HorarioFuncionException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (HorarioFuncionValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
			}else {
				FacesUtil.mensajeInfo("Seleccione una Actividad para continuar.");
			}
		}else {
			FacesUtil.mensajeInfo("Seleccione una Categoría para continuar.");
		}
	}
	
	private PeriodoAcademico cargarPeriodoAcademico(int periodoId){
		PeriodoAcademico retorno = null;

		try {
			retorno = servPeriodoAcademico.buscarPorId(periodoId);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	
	private Carrera cargarCarrera(int carreraId){
		Carrera retorno = null;

		try {
			retorno = servCarrera.buscarPorId(carreraId);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());

		}

		return retorno;
	}
	
	private Dependencia cargarDependencia(int dependenciaId){
		Dependencia retorno = null;
		
		try {
			retorno = servDependencia.buscarPorId(dependenciaId);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	

	private void recargarTemplate(PersonaDto docente) {
		ahfHorasAsignadas = 0;
		
		List<HoraClaseDto> clases = cargarHorarioAsignadoPorClases(docente);
		if (!clases.isEmpty()) {
			establecerActividadesCargaHoraria(ahfListHoraClaseDto, clases, CAHR_TIPO_HORARIO_CLASE);
		}
		
		List<HoraClaseDto> funciones = cargarHorarioAsignadoPorFunciones(docente);
		if (!funciones.isEmpty()) {
			establecerActividadesCargaHoraria(ahfListHoraClaseDto, funciones, CAHR_TIPO_HORARIO_FUNCION);
		}
		
	}

	private List<HoraClaseDto> cargarHorarioAsignadoPorFunciones(PersonaDto docente) {
		List<HoraClaseDto> retorno = new ArrayList<>();
		try {
			retorno = servHoraClaseDto.buscarHorarioFunciones(docente, ahfPeriodoId);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}

	private List<HoraClaseDto> cargarHorarioAsignadoPorClases(PersonaDto docente) {
		List<HoraClaseDto> retorno = new ArrayList<>();
		try {
			retorno = servHoraClaseDto.buscarHorarioAcademico(docente, ahfPeriodoId);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	
	
	
	private List<HoraClaseDto> establecerActividadesCargaHoraria(List<HoraClaseDto> plantilla, List<HoraClaseDto> horario, int tipoCargaHoraria) {

		for (HoraClaseDto itemHorario: horario) {

			switch (itemHorario.getHracDia().intValue()) {

			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:

				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclLunesHoraClaseDto(), itemHorario, tipoCargaHoraria);
					
					}
				}

				break;

			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:

				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclMartesHoraClaseDto(), itemHorario, tipoCargaHoraria);

					}
				}				
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				
				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclMiercolesHoraClaseDto(), itemHorario, tipoCargaHoraria);

					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclJuevesHoraClaseDto(), itemHorario, tipoCargaHoraria);

					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclViernesHoraClaseDto(), itemHorario, tipoCargaHoraria);

					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclSabadoHoraClaseDto(), itemHorario, tipoCargaHoraria);

					}
				}
				break;
				
			case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
				
				for (HoraClaseDto itemPlantilla : plantilla) {
					
					if (itemPlantilla.getHoclHoraInicio().equals(itemHorario.getHoclHoraInicio())) {
						
						establecerValores(itemPlantilla.getHoclDomingoHoraClaseDto(), itemHorario, tipoCargaHoraria);

					}
				}
				break;
			}

		}

		return plantilla;
	}
	
	
	private void establecerValores(HoraClaseDto itemPlantilla, HoraClaseDto itemHorario, int tipoCargaHoraria) {
		
		ahfHorasAsignadas = ahfHorasAsignadas + 1; 
		itemPlantilla.setHoclCheckBox(Boolean.TRUE);

		if (tipoCargaHoraria == CAHR_TIPO_HORARIO_CLASE) {
			itemPlantilla.setHoclDisableCheck(Boolean.TRUE);
			itemPlantilla.setAlaCodigo(itemHorario.getAlaCodigo());
			itemPlantilla.setAlaDescripcion(itemHorario.getAlaDescripcion());
			itemPlantilla.setHracId(itemHorario.getHracId());
			itemPlantilla.setHoclMateriaDto(itemHorario.getHoclMateriaDto());
			itemPlantilla.setHracHoraTipo(itemHorario.getHracHoraTipo());

			if (itemHorario.getHracHoraTipo().equals(HOAC_TIPO_HORA_PRACTICA_VALUE)) {
				itemPlantilla.setHoclDescripcion(CargaHorariaEnum.SUB_DOC_PRACTICA_EXPERIMENTACION.getLabel());
			}else {
				itemPlantilla.setHoclDescripcion(CargaHorariaEnum.SUB_DOC_IMPARTICION_CLASES.getLabel());	
			}
			
		}else if (tipoCargaHoraria == CAHR_TIPO_HORARIO_FUNCION) {
			itemPlantilla.setHoclDescripcion(establecerFuncion(itemHorario.getHoclHorarioFuncionDto()));
			itemPlantilla.setHracId(itemHorario.getHracId());
		}
		
	}

	private String establecerFuncion(HorarioFuncionDto horarioFuncion) {
		
		if (horarioFuncion.getHrfnFuncion().equals(CargaHorariaEnum.CAT_DOCENCIA.getValue())) {
			if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_IMPARTICION_CLASES.getValue())) {
				return CargaHorariaEnum.SUB_DOC_IMPARTICION_CLASES.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_PRACTICA_EXPERIMENTACION.getValue())) {
				return CargaHorariaEnum.SUB_DOC_PRACTICA_EXPERIMENTACION.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_PREPARACION_CLASES.getValue())) {
				return CargaHorariaEnum.SUB_DOC_PREPARACION_CLASES.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_PREPARACION_EXAMENES.getValue())) {
				return CargaHorariaEnum.SUB_DOC_PREPARACION_EXAMENES.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_TUTORIAS.getValue())) {
				return CargaHorariaEnum.SUB_DOC_TUTORIAS.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_VINCULACION.getValue())) {
				return CargaHorariaEnum.SUB_DOC_VINCULACION.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DOC_TITULACION.getValue())) {
				return CargaHorariaEnum.SUB_DOC_TITULACION.getLabel();
			}else {
				return "";
			}
		} else if (horarioFuncion.getHrfnFuncion().equals(CargaHorariaEnum.CAT_DIRECCION_GESTION.getValue())){
			if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DG_COORDINADOR.getValue())) {
				return CargaHorariaEnum.SUB_DG_COORDINADOR.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DG_DIRECCION.getValue())) {
				return CargaHorariaEnum.SUB_DG_DIRECCION.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DG_GESTION.getValue())) {
				return CargaHorariaEnum.SUB_DG_GESTION.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_DG_REPRESENTANTE.getValue())) {
				return CargaHorariaEnum.SUB_DG_REPRESENTANTE.getLabel();
			}else {
				return "";
			}
		} else if (horarioFuncion.getHrfnFuncion().equals(CargaHorariaEnum.CAT_INVESTIGACION.getValue())){
			if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_INV_ACTIVIDADES.getValue())) {
				return CargaHorariaEnum.SUB_INV_ACTIVIDADES.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_INV_AVANZADA.getValue())) {
				return CargaHorariaEnum.SUB_INV_AVANZADA.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_INV_BASICA.getValue())) {
				return CargaHorariaEnum.SUB_INV_BASICA.getLabel();
			} else if (horarioFuncion.getHrfnActividad().equals(CargaHorariaEnum.SUB_INV_PROYECTOS.getValue())) {
				return CargaHorariaEnum.SUB_INV_PROYECTOS.getLabel();
			}else {
				return "";
			}
		}else {
			return "";
		}
		
	}

	
	
	

	/**
	 * Genera items para el combo categoria carga horaria.
	 * @return items para el combo categoria carga horaria.
	 */
	private List<SelectItem> getListCategoriaCargaHoraria()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(CargaHorariaEnum.CAT_DIRECCION_GESTION.getValue(), CargaHorariaEnum.CAT_DIRECCION_GESTION.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.CAT_DOCENCIA.getValue(), CargaHorariaEnum.CAT_DOCENCIA.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.CAT_INVESTIGACION.getValue(), CargaHorariaEnum.CAT_INVESTIGACION.getLabel()));
		return retorno;
		
	}
	
	/**
	 * Genera items para el combo sub categoria carga horaria - direccion y gestion.
	 * @return items para el combo sub categoria carga horaria - direccion y gestion.
	 */
	private List<SelectItem> getListActividadesDireccionGestion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DG_DIRECCION.getValue(), CargaHorariaEnum.SUB_DG_DIRECCION.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DG_GESTION.getValue(), CargaHorariaEnum.SUB_DG_GESTION.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DG_REPRESENTANTE.getValue(), CargaHorariaEnum.SUB_DG_REPRESENTANTE.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DG_COORDINADOR.getValue(), CargaHorariaEnum.SUB_DG_COORDINADOR.getLabel()));
		return retorno;
	}
	
	/**
	 * Genera items para el combo sub categoria carga horaria - docencia.
	 * @return items para el combo sub categoria carga horaria - docencia.
	 */
	private List<SelectItem> getListActividadesDocencia()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_IMPARTICION_CLASES.getValue(), CargaHorariaEnum.SUB_DOC_IMPARTICION_CLASES.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_PREPARACION_CLASES.getValue(), CargaHorariaEnum.SUB_DOC_PREPARACION_CLASES.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_PREPARACION_EXAMENES.getValue(), CargaHorariaEnum.SUB_DOC_PREPARACION_EXAMENES.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_TUTORIAS.getValue(), CargaHorariaEnum.SUB_DOC_TUTORIAS.getLabel()));
//		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_PRACTICA_EXPERIMENTACION.getValue(), CargaHorariaEnum.SUB_DOC_PRACTICA_EXPERIMENTACION.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_VINCULACION.getValue(), CargaHorariaEnum.SUB_DOC_VINCULACION.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_DOC_TITULACION.getValue(), CargaHorariaEnum.SUB_DOC_TITULACION.getLabel()));
		return retorno;
		
	}
	
	/**
	 * Genera items para el combo sub categoria carga horaria - investigacion.
	 * @return items para el combo sub categoria carga horaria - investigacion.
	 */
	private List<SelectItem> getListActividadesInvestigacion(){
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_INV_AVANZADA.getValue(), CargaHorariaEnum.SUB_INV_AVANZADA.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_INV_BASICA.getValue(), CargaHorariaEnum.SUB_INV_BASICA.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_INV_ACTIVIDADES.getValue(), CargaHorariaEnum.SUB_INV_ACTIVIDADES.getLabel()));
		retorno.add(new SelectItem(CargaHorariaEnum.SUB_INV_PROYECTOS.getValue(), CargaHorariaEnum.SUB_INV_PROYECTOS.getLabel()));
		return retorno;
		
	}
	
	private List<HoraClaseDto> cargarHorarioClasesTemplatePorAsignatura(){
		
		List<HoraClaseDto>  retorno = new ArrayList<>();
		
		 try {
			retorno = servHoraClaseDto.listarTemplateHorarioClases();
			if (!retorno.isEmpty()) {
				for (int i = 0; i < HorarioAcademicoConstantes.DIA_DOMINGO_VALUE+1; i++) {
					for (int j = 0; j < retorno.size(); j++) {
						switch (i) {
						case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
							HoraClaseDto horarioL = new HoraClaseDto(i,j, 0);
							horarioL.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioL.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioL.setHoclId(retorno.get(j).getHoclId());
							horarioL.setHoclDiaValue(HorarioAcademicoConstantes.DIA_LUNES_VALUE);
							horarioL.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclLunesHoraClaseDto(horarioL);
							break;
						case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
							HoraClaseDto horarioM = new HoraClaseDto(i,j, 0);
							horarioM.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioM.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioM.setHoclId(retorno.get(j).getHoclId());
							horarioM.setHoclDiaValue(HorarioAcademicoConstantes.DIA_MARTES_VALUE);
							horarioM.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclMartesHoraClaseDto(horarioM);
							break;
						case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
							HoraClaseDto horarioX = new HoraClaseDto(i,j, 0);
							horarioX.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioX.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioX.setHoclId(retorno.get(j).getHoclId());
							horarioX.setHoclDiaValue(HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE);
							horarioX.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclMiercolesHoraClaseDto(horarioX);
							break;
						case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
							HoraClaseDto horarioJ = new HoraClaseDto(i,j, 0);
							horarioJ.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioJ.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioJ.setHoclId(retorno.get(j).getHoclId());
							horarioJ.setHoclDiaValue(HorarioAcademicoConstantes.DIA_JUEVES_VALUE);
							horarioJ.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclJuevesHoraClaseDto(horarioJ);
							break;
						case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
							HoraClaseDto horarioV = new HoraClaseDto(i,j, 0);
							horarioV.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioV.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioV.setHoclId(retorno.get(j).getHoclId());
							horarioV.setHoclDiaValue(HorarioAcademicoConstantes.DIA_VIERNES_VALUE);
							horarioV.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclViernesHoraClaseDto(horarioV);
							break;
						case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
							HoraClaseDto horarioS = new HoraClaseDto(i,j, 0);
							horarioS.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioS.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioS.setHoclId(retorno.get(j).getHoclId());
							horarioS.setHoclDiaValue(HorarioAcademicoConstantes.DIA_SABADO_VALUE);
							horarioS.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclSabadoHoraClaseDto(horarioS);
							break;
						case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
							HoraClaseDto horarioD = new HoraClaseDto(i,j, 0);
							horarioD.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioD.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioD.setHoclId(retorno.get(j).getHoclId());
							horarioD.setHoclDiaValue(HorarioAcademicoConstantes.DIA_DOMINGO_VALUE);
							horarioD.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclDomingoHoraClaseDto(horarioD);
							break;
						}
					}
				}
			}
			
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError("Error de conexión, comuníquese con el Administrador del Sistema.");
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Error de conexión, comuníquese con el Administrador del Sistema.");
		}

		return retorno;
	}
	private List<Dependencia> cargarDependencias(){
		List<Dependencia> retorno = new ArrayList<>();
		
		try {
			retorno = servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	
		return retorno;
	}
	
	private PeriodoAcademico cargarPeriodoAcademico(){
		
		try {
			return servPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			return null;
		} catch (PeriodoAcademicoException e) {
			return null;
		}
	}
	
	
	
	
	private boolean eliminarHorarioAcademico(HoraClaseDto horario) {
		boolean retorno = false;

		try {
			boolean eliminado = servHorarioFuncion.eliminar(horario.getHracId());
			
			if (eliminado) {
				retorno = true;
			}
			
		} catch (HorarioFuncionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioFuncionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioFuncionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	private void desactivarModalAsignacionHorario() {
		ahfActivarModal = 0;
	}

	private void activarModalAsignacionHorario() {
		ahfActivarModal = 1;
	}
	
	private void activarModalImprimirReporte() {
		ahfActivarModal = 2;
	}
	
	public void buscarDocentes() {
		if (!ahfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ahfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				ahfListPersonaDto = cargarDocentes(ahfDependenciaId, ahfCarreraId);
				ahfListPersonaDto.sort(Comparator.comparing(PersonaDto::getPrsPrimerApellido).thenComparing(Comparator.comparing(PersonaDto::getPrsSegundoApellido)));
				verificarDatos();
			}else {
				FacesUtil.mensajeError("Seleccione una Facultad para continuar.");	
			}
		}else {
			FacesUtil.mensajeError("Seleccione un Periodo para continuar.");
		}
	}

	private List<PersonaDto> cargarDocentes(int dependenciaId, int carreraId) {
		List<PersonaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcCargaHorariaDto.buscarDocentes(dependenciaId, carreraId);
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	private List<Carrera> cargarCarreras(Integer dependenciaId) {
		List<Carrera> retorno = new ArrayList<>();
		
		try {
			retorno = servCarrera.listarCarrerasXFacultad(dependenciaId);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	// ****************************************************************/
	// **************** METODOS DE ENCAPSULAMIENTO ********************/
	// ****************************************************************/
	public Usuario getAhfUsuario() {
		return ahfUsuario;
	}

	public void setAhfUsuario(Usuario ahfUsuario) {
		this.ahfUsuario = ahfUsuario;
	}

	public List<PersonaDto> getAhfListPersonaDto() {
		return ahfListPersonaDto;
	}

	public void setAhfListPersonaDto(List<PersonaDto> ahfListPersonaDto) {
		this.ahfListPersonaDto = ahfListPersonaDto;
	}

	public Integer getAhfCarreraId() {
		return ahfCarreraId;
	}

	public void setAhfCarreraId(Integer ahfCarreraId) {
		this.ahfCarreraId = ahfCarreraId;
	}

	public Integer getAhfDependenciaId() {
		return ahfDependenciaId;
	}

	public void setAhfDependenciaId(Integer ahfDependenciaId) {
		this.ahfDependenciaId = ahfDependenciaId;
	}

	public List<Carrera> getAhfListCarrera() {
		return ahfListCarrera;
	}

	public void setAhfListCarrera(List<Carrera> ahfListCarrera) {
		this.ahfListCarrera = ahfListCarrera;
	}

	public List<Dependencia> getAhfListDependencia() {
		return ahfListDependencia;
	}

	public void setAhfListDependencia(List<Dependencia> ahfListDependencia) {
		this.ahfListDependencia = ahfListDependencia;
	}

	public Integer getAhfPeriodoId() {
		return ahfPeriodoId;
	}

	public void setAhfPeriodoId(Integer ahfPeriodoId) {
		this.ahfPeriodoId = ahfPeriodoId;
	}

	public List<PeriodoAcademico> getAhfListPeriodoAcademico() {
		return ahfListPeriodoAcademico;
	}

	public void setAhfListPeriodoAcademico(List<PeriodoAcademico> ahfListPeriodoAcademico) {
		this.ahfListPeriodoAcademico = ahfListPeriodoAcademico;
	}

	public PersonaDto getAhfPersonaDto() {
		return ahfPersonaDto;
	}

	public void setAhfPersonaDto(PersonaDto ahfPersonaDto) {
		this.ahfPersonaDto = ahfPersonaDto;
	}

	public Integer getAhfActivarModal() {
		return ahfActivarModal;
	}

	public void setAhfActivarModal(Integer ahfActivarModal) {
		this.ahfActivarModal = ahfActivarModal;
	}

	public List<HoraClaseDto> getAhfListHoraClaseDto() {
		return ahfListHoraClaseDto;
	}

	public void setAhfListHoraClaseDto(List<HoraClaseDto> ahfListHoraClaseDto) {
		this.ahfListHoraClaseDto = ahfListHoraClaseDto;
	}

	public Integer getAhfCategoriaId() {
		return ahfCategoriaId;
	}

	public void setAhfCategoriaId(Integer ahfCategoriaId) {
		this.ahfCategoriaId = ahfCategoriaId;
	}

	public Integer getAhfActividadId() {
		return ahfActividadId;
	}

	public void setAhfActividadId(Integer ahfActividadId) {
		this.ahfActividadId = ahfActividadId;
	}

	public List<SelectItem> getAhfListCategoriaCargaHoraria() {
		return ahfListCategoriaCargaHoraria;
	}

	public void setAhfListCategoriaCargaHoraria(List<SelectItem> ahfListCategoriaCargaHoraria) {
		this.ahfListCategoriaCargaHoraria = ahfListCategoriaCargaHoraria;
	}

	public List<SelectItem> getAhfListActividadCargaHoraria() {
		return ahfListActividadCargaHoraria;
	}

	public void setAhfListActividadCargaHoraria(List<SelectItem> ahfListActividadCargaHoraria) {
		this.ahfListActividadCargaHoraria = ahfListActividadCargaHoraria;
	}

	public String getAhfLinkReporte() {
		return ahfLinkReporte;
	}

	public void setAhfLinkReporte(String ahfLinkReporte) {
		this.ahfLinkReporte = ahfLinkReporte;
	}

	public String getAhfDiaLabel() {
		return ahfDiaLabel;
	}

	public void setAhfDiaLabel(String ahfDiaLabel) {
		this.ahfDiaLabel = ahfDiaLabel;
	}

	public String getAhfHoraLabel() {
		return ahfHoraLabel;
	}

	public void setAhfHoraLabel(String ahfHoraLabel) {
		this.ahfHoraLabel = ahfHoraLabel;
	}

	public HoraClaseDto getAhfSeleccionHoraClaseDto() {
		return ahfSeleccionHoraClaseDto;
	}

	public void setAhfSeleccionHoraClaseDto(HoraClaseDto ahfSeleccionHoraClaseDto) {
		this.ahfSeleccionHoraClaseDto = ahfSeleccionHoraClaseDto;
	}

	public String getAhfExtensionArchivo() {
		return ahfExtensionArchivo;
	}

	public void setAhfExtensionArchivo(String ahfExtensionArchivo) {
		this.ahfExtensionArchivo = ahfExtensionArchivo;
	}

	public String getAhfNombreArchivo() {
		return ahfNombreArchivo;
	}

	public void setAhfNombreArchivo(String ahfNombreArchivo) {
		this.ahfNombreArchivo = ahfNombreArchivo;
	}

	public String getAhfTokenReporte() {
		return ahfTokenReporte;
	}

	public void setAhfTokenReporte(String ahfTokenReporte) {
		this.ahfTokenReporte = ahfTokenReporte;
	}

	public Integer getAhfMaximoHoras() {
		return ahfMaximoHoras;
	}

	public void setAhfMaximoHoras(Integer ahfMaximoHoras) {
		this.ahfMaximoHoras = ahfMaximoHoras;
	}

	public Integer getAhfHorasAsignadas() {
		return ahfHorasAsignadas;
	}

	public void setAhfHorasAsignadas(Integer ahfHorasAsignadas) {
		this.ahfHorasAsignadas = ahfHorasAsignadas;
	}

	
}