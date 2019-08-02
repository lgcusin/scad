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
   
 ARCHIVO:     ReporteRegistroMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja los certificados de las suficiencias. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     	AUTOR          					COMENTARIOS
 06-08-2018			 		Freddy Guzman 					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import static ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades.cambiarDateToStringFormatoCaberaDocumento;

import java.io.Serializable;
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
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteRegistroMatriculaForm. 
 * Bean de sesion que maneja los certificados de las suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

public class ReporteCertificadoSuficienciasForm extends ReporteTemplateForm implements Serializable {
	
	private static final long serialVersionUID = -6595466142462136234L;

	/**
	 * Método que permite generar el certificado  de aprobacion de la suficiencia en Cultura Física.
	 * @param usuario
	 * @param estudiante
	 * @param facultad
	 * @param decano
	 * @param secretario
	 * @param recordAcademico
	 */
	public static void generarReporteSuficienciaCulturaFisica(Usuario usuario, Persona estudiante,   CarreraDto facultad, PersonaDto  decano, PersonaDto secretario,  List<RecordEstudianteDto> recordAcademico ){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CULTURA FÍSICA";
		
		StringBuilder contenido = new StringBuilder();
		contenido.append( setearGenero(secretario, 1)+" de la Facultad de Cultura Física, de la Universidad Central del Ecuador,\n\n");
		contenido.append("CERTIFICA: que revisados los archivos que reposan en la Secretaría del Departamento de Actividad Física, "+setearGenero(estudiante, 1)+" estudiante ");
		contenido.append(estudiante.getPrsNombres() +" "+ estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido());
		contenido.append(" con identificación número ");contenido.append(estudiante.getPrsIdentificacion());
		contenido.append(" APROBÓ la Suficiencia en Cultura Física.");
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		frmRrmParametros.put("fecha", "Quito, "+cambiarDateToStringFormatoCaberaDocumento(Date.from(Instant.now())));
		frmRrmParametros.put("decano_label", setearGenero(decano, 3));
		frmRrmParametros.put("secretario_label", setearGenero(secretario, 2));
		frmRrmParametros.put("contenido",contenido.toString());
		
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		if (decano != null) {
			frmRrmParametros.put("decano", decano.getPrsNombres() +" "+ decano.getPrsPrimerApellido() + " "+ decano.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("decano", "Registre al Decano de su Dependencia");
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
	
	/**
	 * Método que permite generar el certificado de la suficiencia en Idiomas.
	 * @param usuario
	 * @param estudiante
	 * @param facultad
	 * @param director
	 * @param secretario
	 * @param recordAcademico
	 */
	public static void generarReporteSuficienciaIdiomas(Usuario usuario, Persona estudiante,   CarreraDto facultad, PersonaDto  director, PersonaDto secretario,  List<RecordEstudianteDto> recordAcademico ){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "INSTITUTO ACADÉMICO DE IDIOMAS";
		
		StringBuilder contenido = new StringBuilder();
		contenido.append("El INSTITUTO ACADÉMICO DE IDIOMAS, de la Universidad Central del Ecuador,\n\n");
		contenido.append("CERTIFICA: que de acuerdo a la información que registra el Sistema Integral de Información Universitaria - Académico, "+setearGenero(estudiante, 1)+" estudiante ");
		contenido.append(estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido() + " " + estudiante.getPrsNombres());
		contenido.append(" con identificación número ");contenido.append(estudiante.getPrsIdentificacion());
		contenido.append(" APROBÓ la Suficiencia en el " + facultad.getCrrDescripcion() + ", requisito previo a la titulación de grado.");
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		frmRrmParametros.put("fecha", "Quito, "+cambiarDateToStringFormatoCaberaDocumento(Date.from(Instant.now())));
		frmRrmParametros.put("director_label", "DIRECTOR (E)");
		frmRrmParametros.put("contenido",contenido.toString());
		
		if (director != null) {
			frmRrmParametros.put("director", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director", "Registre al Director de la Suficiencia");
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
	
	public static void generarReporteEstudiantesAprodados(List<PersonaDto> estudiantes){
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
	
}
