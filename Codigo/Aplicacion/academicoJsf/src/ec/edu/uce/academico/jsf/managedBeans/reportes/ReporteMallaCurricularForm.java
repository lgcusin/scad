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
   
 ARCHIVO:     ReporteMallaCurricularForm.java	  
 DESCRIPCION: Bean de sesion que maneja procesos relacionados con la malla curricular. 
 *************************************************************************
                           	MODIFICACIONES
                            
FECHA         		    AUTOR          					COMENTARIOS
07-ENE-2019		 		Freddy Guzmán					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteMallaCurricularForm. 
 * Bean de sesion que maneja procesos relacionados con la malla curricular.
 * @author fgguzman.
 * @version 1.0
 */
public class ReporteMallaCurricularForm extends ReporteTemplateForm implements Serializable {
	
	private static final long serialVersionUID = 903755549202583562L;


	public static void generarReporteCertificadoNotas(Usuario usuario, CarreraDto carrera, MallaCurricularDto mallaCurricular, List<MateriaDto> materias){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "MALLA CURRICULAR";
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmRrmParametros.put("malla_curricular", mallaCurricular.getMlcrDescripcion());
		frmRrmParametros.put("periodo", mallaCurricular.getPracDescripcion());
		frmRrmParametros.put("facultad", carrera.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDescripcion());
		frmRrmParametros.put("total_asignaturas", String.valueOf(mallaCurricular.getMlcrTotalMaterias()!=null?mallaCurricular.getMlcrTotalMaterias():"0"));
		frmRrmParametros.put("total_creditos", String.valueOf(mallaCurricular.getMlcrTotalHoras()!=null?mallaCurricular.getMlcrTotalHoras():"0"));
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (MateriaDto item : materias) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("semestre",item.getNvlDescripcion());
			datoHorario.put("cod_materia", item.getMtrCodigo());
			datoHorario.put("asignatura", item.getMtrDescripcion());
			datoHorario.put("creditos", String.valueOf(item.getMtrCreditos()));
			datoHorario.put("requisitos", item.getMtrPrerequisito());
			datoHorario.put("correquisitos", item.getMtrCorrequisito());
			frmRrmCampos.add(datoHorario);
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	} 
	
}
