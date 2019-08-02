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
   
 ARCHIVO:     ReporteEvaluacionDocenteForm.java	  
 DESCRIPCION: Bean de sesion que maneja el certificado de ReporteEvaluacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-02-2018			 Freddy Guzman                          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) ReporteEvaluacionDocenteForm. 
 * Bean de sesion que maneja los reportes del proceso evaluación docente.
 * @author fgguzman.
 * @version 1.0
 */


public class ReporteEvaluacionDocenteForm extends ReporteTemplateForm implements Serializable { 
	
	private static final long serialVersionUID = 903755549202583562L;
	
	/**
	 * Genera el reporte de autoevaluacion
	 * @param listaContenidos - lista de contenidos a incluir en el reporte
	 * @param persona - datos de la persona a incluir
	 * @param evaluacion - datos de la evaluacion a incluir
	 **/
	public static void generarReporteAutoevaluacion(List<ContenidoEvaluacionDto> listaContenidos, PersonaDatosDto persona, Evaluacion evaluacion){
		Map<String, Object> frmCrpParametros = null;
		List<Map<String, Object>> frmCrpCampos = null;
		Map<String, Object> dato = null;
		String frmRrmNombreReporte = null;

		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/evaluacionDocente");
		
		frmRrmNombreReporte = "autoEvaluacion.jasper";
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes +"/plantillaCabecera.png");
		frmCrpParametros.put("fecha", GeneralesUtilidades.cambiarDateToStringFormatoCaberaDocumento(GeneralesUtilidades.getFechaActualSistema()));
		frmCrpParametros.put("titulo", evaluacion.getEvaDescripcion());
		frmCrpParametros.put("docente", persona.getPrsPrimerApellido()+" "+persona.getPrsSegundoApellido()+" "+persona.getPrsNombres());
		frmCrpParametros.put("identificacion", persona.getPrsIdentificacion());
		frmCrpParametros.put("nick", persona.getPrsMailInstitucional());
		frmCrpParametros.put("encabezado_reporte", "EVALUACIÓN AL DESEMPEÑO DOCENTE");
		frmCrpParametros.put("encabezado_institucion", ReporteTemplateForm.GENERAL_NOMBRE_INSTITUCION);
		frmCrpParametros.put("imagenPie", pathGeneralReportes +"/plantillaPie.png");
		frmCrpParametros.put("pie_pagina",ReporteTemplateForm.GENERAL_PIE_PAGINA);
		frmCrpParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));

		
		frmCrpCampos = new ArrayList<Map<String, Object>>();

		for (ContenidoEvaluacionDto item : listaContenidos) {
			dato = new HashMap<String, Object>();
			dato.put("item", item.getTpcnNumeral().toString());
			dato.put("indicador", item.getTpcnDescripcion().toString());
			dato.put("valoracion", calcularComponenteEtiqueta(item.getCntSeleccion(), item.getTpcnTipoSeleccion()) );
			frmCrpCampos.add(dato);
		}
		
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		
		// ******************FIN DE GENERACION DE REPORTE ************//
	}
	
	
	/**
	 * Genera el reporte de evaluacion de directivo
	 * @param listaContenidos - lista de contenidos a incluir en el reporte
	 * @param persona - datos de la persona a incluir
	 * @param evaluacion - datos de la evaluacion a incluir
	 * @param carrera - datos de la carrera a incluir 
	 * @author Arturo Vilafuerte - ajvillafuerte
	 **/
	public static void generarReporteDirectivo(List<ContenidoEvaluacionDto> listaContenidos, PersonaDatosDto persona, Evaluacion evaluacion, Carrera carrera){
		Map<String, Object> frmCrpParametros = null;
		List<Map<String, Object>> frmCrpCampos = null;
		Map<String, Object> dato = null;
		String frmRrmNombreReporte = null;

		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/evaluacionDocente");
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
	
		frmRrmNombreReporte = "evaluacionDirectivo.jasper";
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes +"/plantillaCabecera.png");
		frmCrpParametros.put("fecha", fechaActual.toString());
		frmCrpParametros.put("titulo", evaluacion.getEvaDescripcion());
		frmCrpParametros.put("docente", persona.getPrsPrimerApellido()+" "+persona.getPrsSegundoApellido()+" "+persona.getPrsNombres());
		frmCrpParametros.put("identificacion", persona.getPrsIdentificacion());
		frmCrpParametros.put("nick", persona.getPrsMailInstitucional());
		frmCrpParametros.put("facultad", carrera.getCrrDependencia().getDpnDescripcion());
		frmCrpParametros.put("carrera", carrera.getCrrDescripcion());
		frmCrpParametros.put("encabezado_reporte", "EVALUACIÓN AL DESEMPEÑO DOCENTE");
		frmCrpParametros.put("imagenPie", pathGeneralReportes +"/plantillaPie.png");
		
		frmCrpCampos = new ArrayList<Map<String, Object>>();

		for (ContenidoEvaluacionDto item : listaContenidos) {
			dato = new HashMap<String, Object>();
			dato.put("item", item.getTpcnNumeral().toString());
			dato.put("indicador", item.getTpcnDescripcion().toString());
			dato.put("valoracion", calcularComponenteEtiqueta(item.getCntSeleccion(), item.getTpcnTipoSeleccion()) );
			frmCrpCampos.add(dato);
		}
		
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		
		// ******************FIN DE GENERACION DE REPORTE ************//
	}
	
	/**
	 * Genera el reporte de evaluacion de par academico
	 * @param listaContenidos - lista de contenidos a incluir en el reporte
	 * @param persona - datos de la persona a incluir
	 * @param evaluacion - datos de la evaluacion a incluir
	 * @param area - datos de la carrera a incluir 
	 * @author Arturo Vilafuerte - ajvillafuerte
	 **/
	public static void generarReporteParAcademico(List<ContenidoEvaluacionDto> listaContenidos, PersonaDatosDto persona, Evaluacion evaluacion, Grupo area){
		Map<String, Object> frmCrpParametros = null;
		List<Map<String, Object>> frmCrpCampos = null;
		Map<String, Object> dato = null;
		String frmRrmNombreReporte = null;

		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/evaluacionDocente");
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
	
		frmRrmNombreReporte = "autoEvaluacion.jasper";
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes +"/plantillaCabecera.png");
		frmCrpParametros.put("fecha", fechaActual.toString());
		frmCrpParametros.put("titulo", evaluacion.getEvaDescripcion());
		frmCrpParametros.put("docente", persona.getPrsPrimerApellido()+" "+persona.getPrsSegundoApellido()+" "+persona.getPrsNombres());
		frmCrpParametros.put("identificacion", persona.getPrsIdentificacion());
		frmCrpParametros.put("nick", persona.getPrsMailInstitucional());
		frmCrpParametros.put("area_conocimiento", area.getGrpDescripcion());
		frmCrpParametros.put("encabezado_reporte", "EVALUACIÓN AL DESEMPEÑO DOCENTE");
		frmCrpParametros.put("imagenPie", pathGeneralReportes +"/plantillaPie.png");
		
		frmCrpCampos = new ArrayList<Map<String, Object>>();

		for (ContenidoEvaluacionDto item : listaContenidos) {
			dato = new HashMap<String, Object>();
			dato.put("item", item.getTpcnNumeral().toString());
			dato.put("indicador", item.getTpcnDescripcion().toString());
			dato.put("valoracion", calcularComponenteEtiqueta(item.getCntSeleccion(), item.getTpcnTipoSeleccion()) );
			frmCrpCampos.add(dato);
		}
		
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		
		// ******************FIN DE GENERACION DE REPORTE ************//
	}
	
	
	public static void generarReporteGeneralAutoevaluacion(List<CarreraDto> carreras, Usuario usuario, PersonaDto director, String evaluados, String noEvaluados){
		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "AUTOEVALUACIÓN";
		
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("fecha", "Quito, "+GeneralesUtilidades.cambiarDateToStringFormatoCaberaDocumento(Date.from(Instant.now())));
		
		frmRrmParametros.put("director", "Ing. "+ director.getPrsNombres() +" "+ director.getPrsPrimerApellido()+" " + director.getPrsSegundoApellido());
		frmRrmParametros.put("cargo", director.getPrsCargo());
		frmRrmParametros.put("total_evaluados", evaluados);
		frmRrmParametros.put("total_no_evaluados", noEvaluados);
		frmRrmParametros.put("titulo", "AUTOEVALUACIÓN 2018 - 2018");

	
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> registros = null;
		registros = new HashMap<String, Object>();			
		
		int count = 0;
		for (CarreraDto item : carreras) {
			registros = new HashMap<String, Object>();
			count = count +1;
			registros.put("estado", count);
			registros.put("facultad", item.getDpnDescripcion());
			registros.put("carrera", item.getCrrDescripcion());
			registros.put("evaluados", item.getCrrEvaluado());
			registros.put("no_evaluados", item.getCrrNoEvaluado());
			frmRrmCampos.add(registros);
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		
		// ******************FIN DE GENERACION DE REPORTE ************//
	}
	
	/**
	 * Método que genera el reporte
	 */
	public static void generarReporteGeneralAutoevaluacionXls(List<CarreraDto> listaUsuario, Usuario usuario, PersonaDto director, String evaluados, String noEvaluados){
		try {
			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE AUTOEVALUACIÓN *************//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "generalAutoEvaluacionXLS";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("evaluacion","AUTOEVALUACION 2018 - 2018");
			frmRrmParametros.put("fecha",fecha);

			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoUsuario = null;

			for (CarreraDto item : listaUsuario) {
				datoUsuario = new HashMap<String, Object>();
				datoUsuario.put("identificacion", item.getPrsIdentificacion());
				datoUsuario.put("apellido1", item.getPrsPrimerApellido() +" "+ item.getPrsSegundoApellido());
				datoUsuario.put("nombre", item.getPrsNombres());
				datoUsuario.put("nick", item.getPrsEmailInstitucional());
				datoUsuario.put("dependencia", item.getDpnDescripcion());
				datoUsuario.put("carrera", item.getCrrDescripcion());
				if(item.getCrrEstado() == 0){
					datoUsuario.put("estado", "NO EVALUADO");
				}
				if(item.getCrrEstado() == 1){
					datoUsuario.put("estado", "EVALUADO");
				}
				frmRrmCampos.add(datoUsuario);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/evaluacionDocente");

			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	}
	
	/**
	 * Método que genera el reporte
	 */
	public static void generarReporteGeneralEvaluacionXls(List<MateriaDto> listaUsuario, Usuario usuario, PersonaDto director, String evaluados, String noEvaluados){
		try {
			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE AUTOEVALUACIÓN *************//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "generalEvaluacion";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("evaluacion","EVALUACION 2018 - 2018");
			frmRrmParametros.put("fecha",fecha);

			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoUsuario = null;

			for (MateriaDto item : listaUsuario) {
				datoUsuario = new HashMap<String, Object>();
				datoUsuario.put("identificacion", item.getPrsIdentificacion());
				datoUsuario.put("apellido1", item.getPrsPrimerApellido() +" "+ item.getPrsSegundoApellido() +" "+  item.getPrsNombres());
				datoUsuario.put("nombre",item.getMtrPersonaDto().getPrsPrimerApellido()+" "+ item.getMtrPersonaDto().getPrsSegundoApellido() +" "+  item.getMtrPersonaDto().getPrsNombres());
				datoUsuario.put("nick", item.getPrsMailInstitucional());
				datoUsuario.put("dependencia", item.getDpnDescripcion()!=null?item.getDpnDescripcion() :"");
				datoUsuario.put("carrera", item.getCrrDescripcion()!=null?item.getCrrDescripcion() :"");
				datoUsuario.put("estado", item.getMtrEstadoLabel());
				frmRrmCampos.add(datoUsuario);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/evaluacionDocente");

			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	}
	

	/**
	 * Transforma el id de estado a descripcion
	 **/
	public static String calcularComponenteEtiqueta(Integer num, Integer tipoSeleccion){
		String lista = null;

		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_UNICO_VALUE){
			switch (num) {
			case 0:
				lista = TipoContenidoConstantes.VALORACION_SI_LABEL;
				break;
			case 1:
				lista = TipoContenidoConstantes.VALORACION_NO_LABEL;
				break;
			default:
				lista = TipoContenidoConstantes.VALORACION_SIN_ETIQUETA_LABEL;
				break;
			}
		}

		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_VALUE){

			switch (num) {
			case 0:
				lista = TipoContenidoConstantes.VALORACION_NUNCA_LABEL;
				break;
			case 1:
				lista = TipoContenidoConstantes.VALORACION_A_VECES_LABEL;
				break;
			case 2:
				lista = TipoContenidoConstantes.VALORACION_CASI_SIEMPRE_LABEL;
				break;
			case 3:
				lista = TipoContenidoConstantes.VALORACION_SIEMPRE_LABEL;
				break;
			default:
				lista = TipoContenidoConstantes.VALORACION_SIN_ETIQUETA_LABEL;
				break;
			}
		}

		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_TRES_VALUE){
			switch (num) {
			case 0:
				lista = TipoContenidoConstantes.VALORACION_SI_LABEL;
				break;
			case 1:
				lista = TipoContenidoConstantes.VALORACION_NO_LABEL;
				break;
			case 3:
				lista = TipoContenidoConstantes.VALORACION_NO_APLICA_LABEL;
				break;
			default:
				lista = TipoContenidoConstantes.VALORACION_SIN_ETIQUETA_LABEL;
				break;
			}
		}
		return lista;
	}
	
	
	
}
