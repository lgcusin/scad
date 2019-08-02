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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (session bean) ReporteProblemasGeneralesForm. 
 * Bean de sesion que maneja los errores que podrian sucitar en algun proceso.
 * @author fgguzman.
 * @version 1.0
 */

public class ReporteProblemasGeneralesForm extends ReporteTemplateForm implements Serializable {
	
	private static final long serialVersionUID = -6595466142462136234L;

	public static byte[] generarReporteLegalizarMatriculas(Usuario ejecutor, List<ComprobantePagoDto> comprobantes){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		JasperReport jasperReport = null;
		JasperPrint jasperPrint; 
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "LEGALIZAR MATRÍCULA";
		
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", ejecutor.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmRrmParametros.put("encargado", "fgguzman");
		frmRrmParametros.put("encargado_label", "DTIC. ASISTENTE DE TECNOLOGÍAS");

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoRecord = null;
		datoRecord = new HashMap<String, Object>();			
		frmRrmCampos.add(datoRecord);
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		for (ComprobantePagoDto item : comprobantes) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("identificacion", item.getCmpaEstudianteDto().getPrsIdentificacion());
			datoHorario.put("estudiante", item.getCmpaEstudianteDto().getPrsPrimerApellido() + " " + item.getCmpaEstudianteDto().getPrsSegundoApellido() + " " + item.getCmpaEstudianteDto().getPrsNombres()); 
			datoHorario.put("comprobante", item.getCmpaNumero());
			frmRrmCampos.add(datoHorario);
		}

//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//		HttpSession httpSession = request.getSession(false);
//		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
//		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
//		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
//		
		byte[] arreglo = null;
		
		try {
			
			JRDataSource campos = new JRBeanCollectionDataSource(frmRrmCampos);
			pathGeneralReportes.append("/academico/reportes/archivosJasper/soporte/comprobantes/");
			jasperReport = (JasperReport) JRLoader.loadObject(new File(pathGeneralReportes.append("/ReporteMitigacionErrores.jasper").toString()));
			jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, campos);
			arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
			return arreglo;
			
		} catch (JRException e) {
		}
	
		return null;
	} 
	
}
