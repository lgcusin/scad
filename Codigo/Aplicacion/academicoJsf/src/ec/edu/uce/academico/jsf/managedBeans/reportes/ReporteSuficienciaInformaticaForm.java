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
   
 ARCHIVO:     ReporteSuficienciaInformatciaForm.java	  
 DESCRIPCION: Bean de sesion que maneja los certificados de las suficiencias. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     	AUTOR          					COMENTARIOS
 26-04-2019			 		Freddy Guzman 					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import static ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades.cambiarDateToStringFormatoCaberaDocumento;

import java.io.Serializable;
import java.math.BigDecimal;
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

import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;

/**
 * Clase (session bean) ReporteSuficienciaInformaticaForm. 
 * Bean de sesion que maneja los certificados de las suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

public class ReporteSuficienciaInformaticaForm extends ReporteTemplateForm implements Serializable {
	private static final long serialVersionUID = -5299619416089446836L;

	public static void generarCalificacionEstudiantesExoneracion(Usuario usuario, List<PersonaDto> estudiantes){
		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "RESULTADO EXONERACIONES";
		
		frmParametros = new HashMap<String, Object>();
		frmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmParametros.put("encabezado_reporte", encabezadoReporte);
		frmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmParametros.put("nick", usuario.getUsrNick());
		frmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmParametros.put("periodo", estudiantes.get(estudiantes.size()-1).getPrsPeriodoAcademicoDto().getPracDescripcion());
		
		frmParametros.put("director", "ING. KARINA SERRANO");
//		if (director != null) {
//			frmParametros.put("director", director.getPrsApellidosNombres());
//		}

	
		frmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		for (PersonaDto item : estudiantes) {
			dato = new HashMap<String, Object>();			
			dato.put("paralelo",item.getPrsParaleloDto().getPrlCodigo());
			dato.put("estudiante", item.getPrsApellidosNombres());
			dato.put("identificacion", item.getPrsIdentificacion());
			dato.put("nota_final", getStringToBigDecimal(item.getPrsCalificacionDto().getClfNotaFinalSemestre(),1));
			dato.put("asistencia", getStringToBigDecimal(item.getPrsCalificacionDto().getClfPorcentajeAsistencia(),0));
			dato.put("estado", ListasCombosForm.getListaEstadoRecorAcademico(item.getPrsRecordEstudianteDto().getRcesEstado()));
			frmCampos.add(dato);
		}
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	}
	
	public static void generarEstudiantesMatriculados(Usuario usuario, PersonaDto director, PersonaDto docente, List<PersonaDto> estudiantes, ParaleloDto paralelo){

		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "ASISTENCIA";
		
		frmParametros = new HashMap<String, Object>();
		frmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmParametros.put("encabezado_reporte", encabezadoReporte);
		frmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmParametros.put("nick", usuario.getUsrNick());
		frmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmParametros.put("docente", docente.getPrsApellidosNombres());
		frmParametros.put("periodo", paralelo.getPrlPeriodoAcademicoDto().getPracDescripcion());
		frmParametros.put("paralelo", paralelo.getPrlDescripcion());
		frmParametros.put("inicio_clase", paralelo.getPrlInicioClase().toString());
		frmParametros.put("fin_clase", paralelo.getPrlFinClase().toString());
		
		frmParametros.put("director_carrera_label", "DIRECTOR(A) DE CARRERA");
		frmParametros.put("director_carrera_value", "Director de Carrera no registrado.");
		if (director != null) {
			frmParametros.put("director_carrera_value", director.getPrsApellidosNombres());
		}
		
		frmParametros.put("docente_label", setearGenero(docente, 5));
		frmParametros.put("docente_value", "Docente no registrado.");
		if (docente != null) {
			frmParametros.put("docente_value", docente.getPrsApellidosNombres());
		}

		Integer contador = new Integer(0);
		frmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		for (PersonaDto item : estudiantes) {
			dato = new HashMap<String, Object>();			
			dato.put("item",(contador = contador + 1).toString());
			dato.put("estudiante", item.getPrsApellidosNombres());
			dato.put("identificacion", item.getPrsIdentificacion());
			dato.put("estado", ListasCombosForm.getListaEstadoRecorAcademico(item.getPrsRecordEstudianteDto().getRcesEstado()));
			frmCampos.add(dato);
		}
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	} 
	
	public static void generarEstudiantesExoneracion(Usuario usuario, PersonaDto director,  List<PersonaDto> estudiantes){

		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "ASISTENCIA EXONERACIONES";
		
		frmParametros = new HashMap<String, Object>();
		frmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmParametros.put("encabezado_reporte", encabezadoReporte);
		frmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmParametros.put("nick", usuario.getUsrNick());
		frmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmParametros.put("periodo", estudiantes.get(estudiantes.size()-1).getPrsPeriodoAcademicoDto().getPracDescripcion());
		
		frmParametros.put("director", "Registre al Director de Carrera.");
		if (director != null) {
			frmParametros.put("director", director.getPrsApellidosNombres());
		}

		frmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		for (PersonaDto item : estudiantes) {
			dato = new HashMap<String, Object>();
			dato.put("paralelo", item.getPrsParaleloDto().getPrlCodigo());
			dato.put("estudiante", item.getPrsApellidosNombres());
			dato.put("identificacion", item.getPrsIdentificacion());
			dato.put("estado", ListasCombosForm.getListaEstadoRecorAcademico(item.getPrsRecordEstudianteDto().getRcesEstado()));
			frmCampos.add(dato);
		}
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	} 
	
	public static void generarTemplatePruebas(Usuario usuario, PersonaDto docente, List<PersonaDto> estudiantes, ParaleloDto paralelo){

		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "EVALUACIÓN";
		
		frmParametros = new HashMap<String, Object>();
		frmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmParametros.put("encabezado_reporte", encabezadoReporte);
		frmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmParametros.put("nick", usuario.getUsrNick());
		frmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmParametros.put("docente", docente.getPrsApellidosNombres());
		frmParametros.put("periodo", paralelo.getPrlPeriodoAcademicoDto().getPracDescripcion());
		frmParametros.put("paralelo", paralelo.getPrlDescripcion());
		frmParametros.put("fecha", paralelo.getPrlInicioClase().toString() +" - "+ paralelo.getPrlFinClase().toString());
		frmParametros.put("instructor", setearGenero(docente, 5));

		Integer contador = new Integer(0);
		frmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		for (PersonaDto item : estudiantes) {
			dato = new HashMap<String, Object>();			
			dato.put("item",(contador = contador + 1).toString());
			dato.put("estudiante", item.getPrsApellidosNombres());
			dato.put("identificacion", item.getPrsIdentificacion());
			frmCampos.add(dato);
		}
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	} 
	
	
	public static void generarCalificacionEstudiantes(Usuario usuario, PersonaDto docente, List<PersonaDto> estudiantes, ParaleloDto paralelo){
		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CALIFICACIONES";
		
		frmParametros = new HashMap<String, Object>();
		frmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmParametros.put("encabezado_reporte", encabezadoReporte);
		frmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmParametros.put("nick", usuario.getUsrNick());
		frmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmParametros.put("docente", docente.getPrsApellidosNombres());
		frmParametros.put("periodo", paralelo.getPrlPeriodoAcademicoDto().getPracDescripcion());
		frmParametros.put("paralelo", paralelo.getPrlDescripcion());
		frmParametros.put("fecha_inicio", paralelo.getPrlInicioClase().toString());
		frmParametros.put("fecha_fin", paralelo.getPrlFinClase().toString());
		
		if (docente != null) {
			frmParametros.put("instructor", docente.getPrsApellidosNombres());
		}

		Integer contador = new Integer(0);
		frmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		for (PersonaDto item : estudiantes) {
			dato = new HashMap<String, Object>();			
			dato.put("item",(contador = contador + 1).toString());
			dato.put("estudiante", item.getPrsApellidosNombres());
			dato.put("identificacion", item.getPrsIdentificacion());
			dato.put("nota", getStringToBigDecimal(item.getPrsCalificacionDto().getClfNotaFinalSemestre(),1));
			dato.put("asistencia", getStringToBigDecimal(item.getPrsCalificacionDto().getClfPorcentajeAsistencia(),0));
			dato.put("estado", ListasCombosForm.getListaEstadoRecorAcademico(item.getPrsRecordEstudianteDto().getRcesEstado()));
			frmCampos.add(dato);
		}
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte+"_"+paralelo.getPrlCodigo());
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	} 
	
	
	
	
	
	public static void generarCertificadoAprobacion(Usuario usuario, Persona estudiante,  PersonaDto secretario, RecordEstudianteDto aprobacion){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "HERRAMIENTAS INFORMÁTICAS";
		
		StringBuilder contenido = new StringBuilder();
		contenido.append("El suscrito SECRETARIO ABOGADO de la Facultad de Ingeniería, Ciencias Físicas y Matemática de la Universidad Central del Ecuador,\n\n");
		contenido.append("CERTIFICA: que revisados los archivos que reposan en la Secretaría de la Carrera de Ingeniería Informática, "+setearGenero(estudiante, 1)+" estudiante ");
		contenido.append(estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido()+ " " + estudiante.getPrsNombres());
		contenido.append(" con identificación número ");contenido.append(estudiante.getPrsIdentificacion());
		contenido.append(" APROBÓ " + setearTipoAprobación(aprobacion) + " de la Suficiencia en Herramientas Informáticas con la nota " + setearNota(aprobacion)+ ", asistencia del " + setearAsistencia(aprobacion)+".");
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		frmRrmParametros.put("fecha", "Quito, "+cambiarDateToStringFormatoCaberaDocumento(Date.from(Instant.now())));
		frmRrmParametros.put("contenido",contenido.toString());
		
		frmRrmParametros.put("secretario_label", "Registre al Secretario Abogado.");
		if (secretario != null) {
			frmRrmParametros.put("secretario_label", setearGenero(secretario, 2));
			frmRrmParametros.put("secretario_value", secretario.getPrsApellidosNombres());
		}

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoRecord = null;
		datoRecord = new HashMap<String, Object>();			
		frmRrmCampos.add(datoRecord);
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte +"_"+ estudiante.getPrsIdentificacion());
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	} 
	
	
	


	public static void generarEstudiantesAprodados(List<PersonaDto> estudiantes){
		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "SUFICIENCIA IDIOMAS - APROBADOS";
		
		frmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
		String fecha = formato.format(GeneralesUtilidades.getFechaActualSistema());
		frmParametros.put("fecha",fecha);

		frmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		
		for (PersonaDto item : estudiantes) {
			dato = new HashMap<String, Object>();			
			dato.put("carrera",item.getCrrDescripcion());
			dato.put("idioma", item.getCrrDetalle());
			dato.put("identificacion", item.getPrsIdentificacion());
			dato.put("estudiante", item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " " + item.getPrsNombres() );
			dato.put("email", item.getPrsMailInstitucional());
			dato.put("estado", item.getFcinObservacion());
			frmCampos.add(dato);
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	}
	
	
	private static String setearTipoAprobación(RecordEstudianteDto aprobacion) {
		String retorno = "el curso ";
		
		if (aprobacion.getRcesPeriodoAcademicoDto().getPracTipo().equals(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE)) {
			retorno = "el exámen ";
		}
		
		return retorno;
	}
	
	

	private static String setearNota(RecordEstudianteDto aprobacion) {
		return  getStringToBigDecimal(aprobacion.getRcesCalificacionDto().getClfNotaFinalSemestre(), new BigDecimal("10"), 0);
	}

	private static String setearAsistencia(RecordEstudianteDto aprobacion) {
		return  getStringToBigDecimal(aprobacion.getRcesCalificacionDto().getClfPorcentajeAsistencia(), 0);
	}
}
