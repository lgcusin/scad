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
   
 ARCHIVO:     ReporteCertificadoNotasForm.java	  
 DESCRIPCION: Bean de sesion que maneja el certificado de notas del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-SEPT-2017			 Vinicio Rosales                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import static ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades.cambiarDateToStringFormatoCaberaDocumento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteCertificadoNotasForm. 
 * Bean de sesion que maneja el certificado de notas del estudiante.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "registroCertificadoNotasForm")
@SessionScoped
public class ReporteCertificadoNotasForm extends ReporteTemplateForm implements Serializable {
	
	private static final long serialVersionUID = 903755549202583562L;


	public static void generarReporteCertificadoNotas(Usuario usuario, EstudianteJdbcDto estudiante, FichaMatriculaDto matricula, CarreraDto facultad, PersonaDto  secretario, List<RecordEstudianteDto> asignaturas){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CERTIFICADO DE NOTAS";
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		frmRrmParametros.put("fecha", "Quito, "+cambiarDateToStringFormatoCaberaDocumento(Date.from(Instant.now())));
		
		frmRrmParametros.put("estudiante", estudiante.getPrsNombres() +" "+ estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion", estudiante.getPrsIdentificacion());
		frmRrmParametros.put("facultad", facultad.getDpnDescripcion());
		frmRrmParametros.put("carrera", facultad.getCrrDescripcion());
		frmRrmParametros.put("periodo", matricula.getPracDescripcion());
		frmRrmParametros.put("cod_matricula", String.valueOf(matricula.getFcmtId()));
		frmRrmParametros.put("cod_estudiante", String.valueOf(estudiante.getPrsId()));
		
		String[] nivel = matricula.getNvlDescripcion().split("\\s+");
		frmRrmParametros.put("nivel", nivel[0].toString());
		
		
		if (secretario != null) {
			frmRrmParametros.put("cargo_value", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
			frmRrmParametros.put("cargo_label", setearGenero(secretario, 2));
		}else {
			frmRrmParametros.put("cargo_value", "Registre al Secretario de su Dependencia");
			frmRrmParametros.put("cargo_label", "");
		}
		
		frmRrmParametros.put("promedio", "");

		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : asignaturas) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("cod_materia",item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("asignatura", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("num_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			datoHorario.put("nota1", getStringToBigDecimal(item.getRcesCalificacionDto().getClfNota1(),1));
			datoHorario.put("nota2", getStringToBigDecimal(item.getRcesCalificacionDto().getClfNota2(),1));
			datoHorario.put("recuperacion", getStringToBigDecimal(item.getRcesCalificacionDto().getClfSupletorio(),1));
			datoHorario.put("nota_final", getStringToBigDecimal(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("asistencia1", getStringToBigDecimal(item.getRcesCalificacionDto().getClfAsistencia1(), item.getRcesCalificacionDto().getClfAsistenciaDocente1(), 0));
			datoHorario.put("asistencia2", getStringToBigDecimal(item.getRcesCalificacionDto().getClfAsistencia2(), item.getRcesCalificacionDto().getClfAsistenciaDocente2(), 0));
			datoHorario.put("asistencia_final", getStringToBigDecimal(item.getRcesCalificacionDto().getClfPorcentajeAsistencia(),0));
			datoHorario.put("estado_matricula", String.valueOf(item.getRcesMateriaDto().getMtrEstadoLabel()));
			frmRrmCampos.add(datoHorario);
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte +"_"+ estudiante.getPrsIdentificacion());
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	} 
	
	public static void generarReporteCertificadoNotas(List<EstudianteJdbcDto> listaRegistroNotas, String nick, Carrera crr, PersonaDto prs){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteCertificadoNotas";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		frmRrmParametros.put("nick", nick);
		for (EstudianteJdbcDto item : listaRegistroNotas) {
			StringBuilder sbCodigoEstudiante = new StringBuilder();
			StringBuilder sbNivelUbicacion = new StringBuilder();
			frmRrmParametros.put("nombres", item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			frmRrmParametros.put("identificacion", item.getPrsIdentificacion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
//			frmRrmParametros.put("curso", item.getNvlDescripcion());
			sbNivelUbicacion.append(item.getFcmtNivelUbicacion());
			frmRrmParametros.put("curso", sbNivelUbicacion.toString());
			sbCodigoEstudiante.append(item.getPrsId());
			frmRrmParametros.put("codigo_estudiante", sbCodigoEstudiante.toString());
			
			frmRrmParametros.put("periodo", item.getPracDescripcion());
				
			break;
		}
		
//		if(crr != null ){
//			if(crr.getCrrDependencia().getDpnId() == 15 ){
//				frmRrmParametros.put("secretario_abogado", "FREDDY VINICIO ZUMARRAGA FONSECA");
//			}
//			if(crr.getCrrDependencia().getDpnId() == 14 ){
//				frmRrmParametros.put("secretario_abogado", "LOURDES LUCIA MANOSALVAS VINUEZA");
//			}
//			if(crr.getCrrDependencia().getDpnId() == 26 ){
//				frmRrmParametros.put("secretario_abogado", "GABRIELA SOLEDAD PAREDES ALDAS");
//			}
//		}else{
//			frmRrmParametros.put("secretario_abogado", " ");
//		}
		
		if(prs != null){
			if(prs.getPrsPrimerApellido() != null){
				frmRrmParametros.put("secretario_abogado", prs.getPrsNombres()+" "+prs.getPrsPrimerApellido()+" "+prs.getPrsSegundoApellido());
			}else{
				frmRrmParametros.put("secretario_abogado", prs.getPrsNombres()+" "+prs.getPrsSegundoApellido());
			}
		}else{
			frmRrmParametros.put("secretario_abogado", " ");
		}
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteCertificadoNotas");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraCertificadoNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieCertificado.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaRegistroNotas) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbNota3 = new StringBuilder();
			StringBuilder sbNotaTotal = new StringBuilder();
			StringBuilder sbAsistencia1 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbAsistenciaTotal = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes.put("codigo", item.getMtrCodigo());
			datoEstudiantes.put("materia", item.getMtrDescripcion());
//			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			//Asistencia y Nota 1er Hemisemestre
			if(item.getClfNota1() != null){
				BigDecimal nota1 = item.getClfNota1().setScale(2,RoundingMode.HALF_EVEN);
				sbNota1.append(nota1);
				datoEstudiantes.put("nota1", sbNota1.toString());
			}else{
				sbNota1.append("-");
				datoEstudiantes.put("nota1", sbNota1.toString());
			}
			if(item.getClfAsistenciaEstudiante1() != null && item.getClfAsistenciaDocente1() != null){
				sbAsistencia1.append(item.getClfAsistenciaEstudiante1()+"/"+item.getClfAsistenciaDocente1());
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else if(item.getClfAsistenciaEstudiante1() == null && item.getClfAsistenciaDocente1() == null){
				sbAsistencia1.append("-/-");
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else if(item.getClfAsistenciaEstudiante1() != null && item.getClfAsistenciaDocente1() == null){
				sbAsistencia1.append(item.getClfAsistenciaEstudiante1()+"/"+"-");
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else if(item.getClfAsistenciaEstudiante1() == null && item.getClfAsistenciaDocente1() != null){
				sbAsistencia1.append("-"+"/"+item.getClfAsistenciaDocente1());
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}
			//Asistencia y Nota 2do Hemisemestre
			if(item.getClfNota2() != null){
				BigDecimal nota2 = item.getClfNota2().setScale(2,RoundingMode.HALF_EVEN);
				sbNota2.append(nota2);
				datoEstudiantes.put("nota2", sbNota2.toString());
			}else{
				sbNota2.append("-");
				datoEstudiantes.put("nota2", sbNota2.toString());
			}
			if(item.getClfAsistenciaEstudiante2() != null && item.getClfAsistenciaDocente2() != null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante2()+"/"+item.getClfAsistenciaDocente2());
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante2() == null && item.getClfAsistenciaDocente2() == null){
				sbAsistencia2.append("-/-");
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante2() != null && item.getClfAsistenciaDocente2() == null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante2()+"/"+"-");
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante2() == null && item.getClfAsistenciaDocente2() != null){
				sbAsistencia2.append("-"+"/"+item.getClfAsistenciaDocente2());
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}
			//Nota del supletorio
			if(item.getClfSupletorio() != null){
				BigDecimal nota3 = item.getClfSupletorio().setScale(2,RoundingMode.HALF_EVEN);
				sbNota3.append(nota3);
				datoEstudiantes.put("nota3", sbNota3.toString());
			}else{
				sbNota3.append("-");
				datoEstudiantes.put("nota3", sbNota3.toString());
			}
			//Asistencia y Nota Final
			if(item.getClfPromedioAsistencia() != null){
				BigDecimal asistencia_final = item.getClfPromedioAsistencia().setScale(2, RoundingMode.HALF_UP);
				sbAsistenciaTotal.append(asistencia_final);
				datoEstudiantes.put("asistencia_final", sbAsistenciaTotal.toString());
			}else{
				sbAsistenciaTotal.append("-");
				datoEstudiantes.put("asistencia_final", sbAsistenciaTotal.toString());
			}
			if(item.getClfNotalFinalSemestre() != null){
				BigDecimal nota_final = item.getClfNotalFinalSemestre().setScale(2, RoundingMode.HALF_EVEN);
				sbNotaTotal.append(nota_final);
				datoEstudiantes.put("nota_final", sbNotaTotal.toString());
			}else{
				sbNotaTotal.append("-");
				datoEstudiantes.put("nota_final", sbNotaTotal.toString());
			}
			//Estado del estudiante
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_LABEL);
			}
			
			
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont +1;
		}
		
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		
		
		
		// ******************FIN DE GENERACION DE REPORTE ************//
	} 
	
	public static void generarReporteCertificadoNotasPosgrado(List<EstudianteJdbcDto> listaRegistroNotas, String nick, Carrera crr, PersonaDto prs){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteCertificadoNotas";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		frmRrmParametros.put("nick", nick);
		for (EstudianteJdbcDto item : listaRegistroNotas) {
			StringBuilder sbCodigoEstudiante = new StringBuilder();
			StringBuilder sbNivelUbicacion = new StringBuilder();
			frmRrmParametros.put("nombres", item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			frmRrmParametros.put("identificacion", item.getPrsIdentificacion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
//			frmRrmParametros.put("curso", item.getNvlDescripcion());
			sbNivelUbicacion.append(item.getFcmtNivelUbicacion());
			frmRrmParametros.put("curso", "CUARTO NIVEL");
			sbCodigoEstudiante.append(item.getPrsId());
			frmRrmParametros.put("codigo_estudiante", sbCodigoEstudiante.toString());
			
			frmRrmParametros.put("periodo", item.getPracDescripcion());
				
			break;
		}
		
//		if(crr != null ){
//			if(crr.getCrrDependencia().getDpnId() == 15 ){
//				frmRrmParametros.put("secretario_abogado", "FREDDY VINICIO ZUMARRAGA FONSECA");
//			}
//			if(crr.getCrrDependencia().getDpnId() == 14 ){
//				frmRrmParametros.put("secretario_abogado", "LOURDES LUCIA MANOSALVAS VINUEZA");
//			}
//			if(crr.getCrrDependencia().getDpnId() == 26 ){
//				frmRrmParametros.put("secretario_abogado", "GABRIELA SOLEDAD PAREDES ALDAS");
//			}
//		}else{
//			frmRrmParametros.put("secretario_abogado", " ");
//		}
		
		if(prs != null){
			if(prs.getPrsPrimerApellido() != null){
				frmRrmParametros.put("secretario_abogado", prs.getPrsNombres()+" "+prs.getPrsPrimerApellido()+" "+prs.getPrsSegundoApellido());
			}else{
				frmRrmParametros.put("secretario_abogado", prs.getPrsNombres()+" "+prs.getPrsSegundoApellido());
			}
		}else{
			frmRrmParametros.put("secretario_abogado", " ");
		}
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteCertificadoNotas");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraCertificadoNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieCertificado.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaRegistroNotas) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbNota3 = new StringBuilder();
			StringBuilder sbNotaTotal = new StringBuilder();
			StringBuilder sbAsistencia1 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbAsistenciaTotal = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes.put("codigo", item.getMtrCodigo());
			datoEstudiantes.put("materia", item.getMtrDescripcion());
//			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			//Asistencia y Nota 1er Hemisemestre
			if(item.getClfNota1() != null){
				BigDecimal nota1 = item.getClfNota1().setScale(2,RoundingMode.HALF_EVEN);
				sbNota1.append(nota1);
				datoEstudiantes.put("nota1", sbNota1.toString());
			}else{
				sbNota1.append("-");
				datoEstudiantes.put("nota1", sbNota1.toString());
			}
			if(item.getClfAsistenciaEstudiante1() != null && item.getClfAsistenciaDocente1() != null){
				sbAsistencia1.append(item.getClfAsistenciaEstudiante1()+"/"+item.getClfAsistenciaDocente1());
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else if(item.getClfAsistenciaEstudiante1() == null && item.getClfAsistenciaDocente1() == null){
				sbAsistencia1.append("-/-");
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else if(item.getClfAsistenciaEstudiante1() != null && item.getClfAsistenciaDocente1() == null){
				sbAsistencia1.append(item.getClfAsistenciaEstudiante1()+"/"+"-");
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else if(item.getClfAsistenciaEstudiante1() == null && item.getClfAsistenciaDocente1() != null){
				sbAsistencia1.append("-"+"/"+item.getClfAsistenciaDocente1());
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}
			//Asistencia y Nota 2do Hemisemestre
			if(item.getClfNota2() != null){
				BigDecimal nota2 = item.getClfNota2().setScale(2,RoundingMode.HALF_EVEN);
				sbNota2.append(nota2);
				datoEstudiantes.put("nota2", sbNota2.toString());
			}else{
				sbNota2.append("-");
				datoEstudiantes.put("nota2", sbNota2.toString());
			}
			if(item.getClfAsistenciaEstudiante2() != null && item.getClfAsistenciaDocente2() != null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante2()+"/"+item.getClfAsistenciaDocente2());
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante2() == null && item.getClfAsistenciaDocente2() == null){
				sbAsistencia2.append("-/-");
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante2() != null && item.getClfAsistenciaDocente2() == null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante2()+"/"+"-");
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante2() == null && item.getClfAsistenciaDocente2() != null){
				sbAsistencia2.append("-"+"/"+item.getClfAsistenciaDocente2());
				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
			}
			//Nota del supletorio
			if(item.getClfSupletorio() != null){
				BigDecimal nota3 = item.getClfSupletorio().setScale(2,RoundingMode.HALF_EVEN);
				sbNota3.append(nota3);
				datoEstudiantes.put("nota3", sbNota3.toString());
			}else{
				sbNota3.append("-");
				datoEstudiantes.put("nota3", sbNota3.toString());
			}
			//Asistencia y Nota Final
			if(item.getClfPromedioAsistencia() != null){
				BigDecimal asistencia_final = item.getClfPromedioAsistencia().setScale(2, RoundingMode.HALF_UP);
				sbAsistenciaTotal.append(asistencia_final);
				datoEstudiantes.put("asistencia_final", sbAsistenciaTotal.toString());
			}else{
				sbAsistenciaTotal.append("-");
				datoEstudiantes.put("asistencia_final", sbAsistenciaTotal.toString());
			}
			if(item.getClfNotalFinalSemestre() != null){
				BigDecimal nota_final = item.getClfNotalFinalSemestre().setScale(2, RoundingMode.HALF_EVEN);
				sbNotaTotal.append(nota_final);
				datoEstudiantes.put("nota_final", sbNotaTotal.toString());
			}else{
				sbNotaTotal.append("-");
				datoEstudiantes.put("nota_final", sbNotaTotal.toString());
			}
			//Estado del estudiante
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL);
			}
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_LABEL);
			}
			
			
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont +1;
		}
		
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		
	} 
	
	
	
	
}
