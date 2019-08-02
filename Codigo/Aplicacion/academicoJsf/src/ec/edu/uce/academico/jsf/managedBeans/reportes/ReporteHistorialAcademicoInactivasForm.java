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
   
 ARCHIVO:     ReporteHistorialAcademicoInactivasForm.java	  
 DESCRIPCION: Bean de sesion que maneja los historiales academicos. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     	AUTOR          					COMENTARIOS
 06-08-2018			 		Freddy Guzman 					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import static ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades.cambiarBigDecimalToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteHistorialAcademicoInactivasForm. 
 * Bean de sesion que maneja los reportes del historial academico.
 * @author fgguzman.
 * @version 1.0
 */

public class ReporteHistorialAcademicoInactivasForm extends ReporteTemplateForm implements Serializable {
	
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
	public static void generarReporteSuficienciaCulturaFisica(Usuario usuario, EstudianteJdbcDto estudiante,   BigDecimal promedio, PersonaDto  director, PersonaDto secretario,  List<RecordEstudianteDto> recordAcademico ){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "RÉCORD ACADÉMICO";

		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmRrmParametros.put("estudiante", estudiante.getPrsNombres() + " "+ estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  estudiante.getPrsIdentificacion());
		frmRrmParametros.put("codigo_estudiantil",  String.valueOf(estudiante.getPrsId()));
		frmRrmParametros.put("facultad", recordAcademico.get(0).getRcesDependenciaDto().getDpnDescripcion());
		frmRrmParametros.put("carrera", recordAcademico.get(0).getRcesCarreraDto().getCrrDescripcion());
		frmRrmParametros.put("nick", usuario.getUsrNick());
		
		
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		if (director != null) {
			frmRrmParametros.put("director_carrera", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director_carrera", "Registre al Director de su Dependencia");
		}
		
		frmRrmParametros.put("promedio", cambiarBigDecimalToString(promedio,1));
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : recordAcademico) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("periodo", item.getRcesPeriodoAcademicoDto().getPracDescripcion());
			datoHorario.put("codigo_matricula", String.valueOf(item.getRcesFichaMatriculaDto().getFcmtId()));
			datoHorario.put("codigo_materia", item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("numero_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrCreditos()));
			datoHorario.put("nota1", cambiarBigDecimalToString(item.getRcesCalificacionDto().getClfNota1(),1));
			datoHorario.put("nota2", cambiarBigDecimalToString(item.getRcesCalificacionDto().getClfNota2(),1));
			datoHorario.put("recuperacion", cambiarBigDecimalToString(item.getRcesCalificacionDto().getClfSupletorio(),1));
			datoHorario.put("nota_final", cambiarBigDecimalToString(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("asistencia", cambiarBigDecimalToString(item.getRcesCalificacionDto().getClfPorcentajeAsistencia(),0));
			datoHorario.put("estado", item.getRcesMateriaDto().getMtrEstadoLabel());
			frmRrmCampos.add(datoHorario);
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte +"_"+ estudiante.getPrsIdentificacion());
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	} 
	
	
}
