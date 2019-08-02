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
   
 ARCHIVO:     ReporteServlet.java	  
 DESCRIPCION: Clase encargada de generar el reporte de JasperReport. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 14-NOV-2016			 Vinicio Rosales  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.servlets;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
/**
 * Clase (servlet) ReporteServlet.
 * Clase encargada de generar el reporte de JasperReport.
 * @author jvrosales.
 * @version 1.0
 */
public class ReporteServlet extends HttpServlet{

	private static final long serialVersionUID = -4029426518014683903L;

	public void init(ServletConfig conf) throws ServletException{
		super.init(conf);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//parametros del doGet
		final String nombreJasper = request.getParameter("nombreJasper");
		final String formato = request.getParameter("formato");
		final String tipo = request.getParameter("tipo");  
		
		//parametros de sesion
		String nombreReporte=null;
		List<Map<String, Object>> campos = null;
		Map<String, Object> parametros = null;
		
		//directorio especifico del jasper
		String directorioBase = null;
		//directorio general de los jaspers
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(getServletConfig().getServletContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/");
		//directorio completo del jasper
		StringBuilder pathDeReporte = new StringBuilder();
		
		//**************************************************************************//		
		//********************* VERIFICACION DEL TIPO *****************************//
		//*************************************************************************//
		//Reporte de errores de carga
		
		if (tipo.equals("SUFICIENCIA_INFORMATICA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "suficiencias/informatica/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("SUFICIENCIAS_CERTIFICADOS_RECORD")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "suficiencias/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("PREGRADO_ESTUDIANTES_SUFICIENCIAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "pregrado/estudiantes/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("PREGRADO_CERTIFICADOS_RECORD")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "pregrado/certificados/record/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("PREGRADO_DOCENTES")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "pregrado/docentes/calificacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("NIVELACION_CERTIFICADOS_RECORD")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "nivelacion/certificados/record/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("MATRICULA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "generacionMatricula/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if(tipo.equals("SECRETARIA")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSecretaria/listadoEstudiantes/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if(tipo.equals("DOCENTE")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if(tipo.equals("CERTIFICADO_MATRICULA")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoMatricula/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if(tipo.equals("REPORTE_NOTAS")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/primerHemi/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("REPORTE_NOTAS_SEGUNDO_HEMI")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/segundoHemi/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		
		else if(tipo.equals("REPORTE_RECUPERACION")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/recuperacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("REPORTE_RECTIFICACION_PRIMER_HEMI")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/rectificacion/primerHemi/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("REPORTE_RECTIFICACION_SEGUNDO_HEMI")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/rectificacion/segundoHemi/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("REPORTE_RECTIFICACION_RECUPERACION")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/rectificacion/recuperacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("REPORTE_LIBROS")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSecretaria/listadoEstudiantesLibro/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("REPORTE_NOTAS_FINALES")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/notasFinales/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if (tipo.equals("MATRICULAPOSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "generacionMatriculaPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("MALLA_CURRICULAR_MATERIAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "mallaCurricular/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADO_NOTAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoNotas/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADO_NOTAS_POSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoNotasPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADO_RECORD_ACADEMICO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoRecordAcademico/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADO_RECORD_ACADEMICO_APROBADAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoRecordAcademico/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADO_RECORD_ACADEMICO_INACTIVAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoRecordAcademico/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADO_SUFICIENCIA_CULTURA_FISICA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "certificados/suficiencias/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if (tipo.equals("CERTIFICADO_SUFICIENCIA_IDIOMAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "certificados/suficiencias/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		} else if (tipo.equals("CERTIFICADO_SUFICIENCIA_INFORMATICA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "certificados/suficiencias/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if (tipo.equals("CERTIFICADOS_MATRICULA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "certificados/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CERTIFICADOS_NOTAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "certificados/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("CARGA_HORARIA_DOCENTE")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "cargaHoraria/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("CARGA_HORARIA_ACTIVIDADES")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "cargaHoraria/horario/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("ORDENCOBRO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "matriculasNivelacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("FORMATONOTAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "formatoNotas/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}
		else if (tipo.equals("ORDENCOBROPOSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "matriculaPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if(tipo.equals("AUTOEVALUACION")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "evaluacionDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if(tipo.equals("EVALUACION_DIRECTIVO")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "evaluacionDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if(tipo.equals("EVALUACION_PAR_ACADEMICO")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "evaluacionDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("GENERAR_MATRICULA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "matricula/registro/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_SOLICITUD_TERCERA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSolicitudTercera/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("GENERAR_ORDEN_COBRO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "matricula/comprobante/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");		
		}else if (tipo.equals("REPORTE_APROBACION_SOLICITUD_TERCERA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteAprobacionTercera/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_NOTAS_POSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/notasPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_APELACION_TERCERA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteApelacionTercera/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("REPORTE_APROBACION_APELACION_TERCERA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteAprobarApelacionTercera/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_HORARIO_PARALELO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "horario/paralelo/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_HORARIO_PARALELO_XLS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "horario/paralelo/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_PARALELO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "paralelo/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_PARALELO_XLS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "paralelo/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_HORARIO_ESTUDIANTE")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "horario/estudiante/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_HORARIO_DOCENTE")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "horario/docente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("MATRICULA_POSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteMatriculadosPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if(tipo.equals("REPORTE_MATRICULADOS")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reportesConsultor/Matriculados/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if(tipo.equals("REPORTE_TERCERAS_MATRICULAS")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reportesConsultor/Solicitudes/TercerasMatriculas/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if(tipo.equals("REPORTE_NOTAS_ESTUDIANTES")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reportesConsultor/NotasEstudiantes/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("REPORTE_NOTAS_IDIOMAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNotas/notasIdiomas/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("FORMATONOTAS_IDIOMAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "formatoNotas/ListaEstudianteNotasIdiomas/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_HOMOLOGACION")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteHomologacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		else if (tipo.equals("MATERIAXDOCENTE")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSecretaria/listadoMateriasXDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}	else if (tipo.equals("CARGAHORARIAXDOCENTE")) {
				nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
				campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
				parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
				directorioBase = "reporteDocente/";
				request.getSession().removeAttribute("frmCargaNombreReporte");
				request.getSession().removeAttribute("frmCargaCampos");
				request.getSession().removeAttribute("frmCargaParametros");
		}	else if (tipo.equals("REPORTEDOCENCIAPORPARALELO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}	else if (tipo.equals("TITULACIONEXAMENCOMPLEXIVO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteTitulacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}	else if (tipo.equals("TITULACIONPROYECTO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteTitulacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("REPORTE_USUARIOS_XLS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "soporte/administracionUsuario/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("AUTOEVALUACION_GENERAL")) {
				nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
				campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
				parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
				directorioBase = "evaluacionDocente/";
				request.getSession().removeAttribute("frmCargaNombreReporte");
				request.getSession().removeAttribute("frmCargaCampos");
				request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("AUTOEVALUACION_GENERAL_XLS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "evaluacionDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
	
		}else if (tipo.equals("EVALUACION_GENERAL")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "evaluacionDocente/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("ESTUDIANTESxNIVEL")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "estudiantes/listaestudiantesmatriculados/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("SOLICITUD_RETIRO_FORTUITO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSolicitudRetiroFortuito/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("APROBACION_RETIRO_FORTUITO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteAprobacionRetiroFortuito/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("ANULACION_MATRICULA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteAnulacionMatricula/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("REPORTE_SEGURO_VIDA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSeguroVida/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("REPORTE_RETIRO_XLS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteRetiro/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("CERTIFICADO_RECORD_ACADEMICO_POSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoRecordPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_POSGRADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteEstadoPosgrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("REPORTE_TERCERAS_MATICULAS_XLS")) { 
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCompletoTercerasMatriculas/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		}else if (tipo.equals("ESTADOS_APROBADOS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteNivelacion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("ESTADO_MATRICULA")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSecretaria/matriculaEstado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("SUFICIENCIA_IDIOMAS_APROBADOS_XLS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteSecretaria/suficiencias/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("SUF_INF_ASISTENCIA_INTENSIVO_REGULAR")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "suficiencias/informatica/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if (tipo.equals("SUF_INF_ASISTENCIA_EXONERACION")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "suficiencias/informatica/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
			
		}else if (tipo.equals("SUF_INF_PRUEBAS_INT_REG")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "suficiencias/informatica/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
			
		}else if (tipo.equals("SUF_INF_CALIFICACIONES_INT_REG")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "suficiencias/informatica/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		
		}else if (tipo.equals("PADRON_DOCENTE")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "padrones/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
			
		}else if (tipo.equals("PADRON_ESTUDIANTE")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "padrones/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
			
		}else if (tipo.equals("CARGA_HORARIA_GENERAL")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "cargaHoraria/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");	
		
		}
		else if (tipo.equals("HISTORIAL_ACADEMICO_CONSOLIDADO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCertificadoRecordAcademico/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		} else if (tipo.equals("ESTADISTICAS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "directorCarrera/pasoDeNotasDocentes/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		
		}else if(tipo.equals("otro reporte")){
			
		}
		//defino el path completo del jasper
		pathDeReporte.append(pathGeneralReportes);
		pathDeReporte.append(directorioBase);
		pathDeReporte.append(nombreJasper);
		pathDeReporte.append(".jasper");
		
		if(campos!=null){
			//genero el datasource de jasper a partir de nuestro datasource
			JRDataSource dataSource = new JRBeanCollectionDataSource(campos);
			//verifico el formato
			try {
				switch (formato) {
					case "PDF":
						exportarPDF(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "XLS":
						exportarExcel(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "CSV":
						exportarCsv(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "HTML":
						exportarHtml(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "DOCX":
						exportarDocx(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "XLSX":
						exportarExcelXLSX(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "PDF_INLINE":
						exportarPdfInline(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "PDF_TABLE":
						exportarPDF(generarJasperPrint(pathDeReporte.toString(),parametros), nombreReporte, response);
						break;
					default:
						break;
				}
			} catch (JRException e) {
				e.printStackTrace();
//				System.out.println("*********************************************************************************");
//				System.out.println("********************* ERROR EN LA GENERACION DEL REPORTE ************************");
//				System.out.println("*********************************************************************************");
//				e.printStackTrace();
//				System.out.println("*********************************************************************************");
			}catch (IOException e) {
				e.printStackTrace();
//				System.out.println("*********************************************************************************");
//				System.out.println("********************* ERROR EN LA GENERACION DEL REPORTE ************************");
//				System.out.println("*********************************************************************************");
//				e.printStackTrace();
//				System.out.println("*********************************************************************************");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new RuntimeException("No es posible usar doPost");
	}

	/**
	 * genera el jasperprint a partir del path del jasper, el datasource y los parametros
	 * @param reportPath - path del jasper
	 * @param parametros - parametros para el reporte
	 * @param dataSource - datasource para el reporte
	 * @return - jasperprint respectivo 
	 * @throws JRException
	 */
	private JasperPrint generarJasperPrint(String reportPath, Map<String, Object> parametros, JRDataSource dataSource) throws JRException{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(reportPath));
		return JasperFillManager.fillReport(jasperReport, parametros, dataSource);
	}
	
	/**
	 * genera el jasperprint a partir del path del jasper, el datasource y los parametros
	 * @author fgguzman
	 * @param reportPath - path del jasper
	 * @param parametros - parametros para el reporte
	 * @param dataSource - datasource para el reporte
	 * @return - jasperprint respectivo 
	 * @throws JRException
	 */
	private JasperPrint generarJasperPrint(String reportPath, Map<String, Object> parametros) throws JRException{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(reportPath));
		return JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	}
	
	
	/**
	 * Genera el reporte en archivo PDF 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarPDF(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/pdf");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".pdf\"");
		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		exporter.exportReport();
	}
	
	
	/**
	 * Genera el reporte en archivo PDF 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarPdfInline(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/pdf");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "inline; filename=\""+ nombreReporte +".pdf\"");
		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo XLS 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarExcel(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/vnd.ms-excel");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".xls\"");
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		//configuracion del excel 
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);//una pagina por hoja
		configuration.setDetectCellType(true);//detectar el tipo dato de la celda
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo XLS 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarExcelXLSX(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/vnd.ms-excel");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".xlsx\"");
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		//configuracion del excel 
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(false);//una pagina por hoja
		configuration.setDetectCellType(true);//detectar el tipo dato de la celda
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo CSV 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarCsv(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/CSV");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".csv\"");
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo HTML 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarHtml(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		HtmlExporter exporterHTML = new HtmlExporter();
		SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
		exporterHTML.setExporterInput(exporterInput);
		SimpleHtmlExporterOutput exporterOutput;
		exporterOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
		exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
		exporterHTML.setExporterOutput(exporterOutput);
	    SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
		reportExportConfiguration.setWhitePageBackground(false);
		reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
		exporterHTML.setConfiguration(reportExportConfiguration);
		exporterHTML.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo DOCX 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarDocx(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".docx\"");
		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
	}
		
	/**
	 * Compila un jrxml, para esto es necesario que en el bin del servidor esten todas las librerias de jasper
	 * @param pathJrxml - directorio absoluto del archivo jrxml
	 * @return - el compilado jasper del jrxml indicado
	 */
	@SuppressWarnings("unused")
	private JasperReport compilarJrxml(String pathJrxml){
		JasperReport jasperReport = null;
		try {
			jasperReport = JasperCompileManager.compileReport(pathJrxml);
		} catch (JRException e) {
			jasperReport = null;
		}
		return jasperReport;
	}
	
	/**
	 * Transforma un jason en un datasource para el reporte jasper
	 * @param jsonData - cadena del json
	 * @return - JRDatasource
	 */
	public JRDataSource jsonToJRDatasource(String jsonData) {
		JRDataSource dataSource = null;
		if ("null".equals(jsonData) || jsonData == null || "".equals(jsonData)) {
			dataSource = new JREmptyDataSource();
			return dataSource;
		}
		InputStream jsonInputStream = null;
		try {
			// Convert the jsonData string to inputStream
			jsonInputStream = IOUtils.toInputStream(jsonData, "UTF-8");
			// selectExpression is based on the jsonData that your string contains
			dataSource = new JsonDataSource(jsonInputStream, "data");
		} catch (IOException ex) {
		} catch (JRException e) {
		}
		if (dataSource == null) {
			dataSource = new JREmptyDataSource();
		}
		return dataSource;
	}
	
}
