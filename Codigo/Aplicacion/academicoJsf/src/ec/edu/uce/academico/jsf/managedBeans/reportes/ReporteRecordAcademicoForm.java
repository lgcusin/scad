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
   
 ARCHIVO:     ReporteRecordAcademicoForm.java	  
 DESCRIPCION: Bean de sesion que maneja el certificado de notas del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 08-SEPT-2017			 Vinicio Rosales                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;

/**
 * Clase (session bean) ReporteRecordAcademicoForm. 
 * Bean de sesion que maneja el certificado de notas del estudiante.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "registroRecordAcademicoForm")
@SessionScoped
public class ReporteRecordAcademicoForm implements Serializable {
	
	private static final long serialVersionUID = 903755549202583562L;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	public static void generarReporteRecordAcademico(List<EstudianteJdbcDto> listaRecordAcademico, String nick, List<EstudianteJdbcDto> prome, Carrera crr, PersonaDto dir, PersonaDto prs){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteRecordAcademico";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		frmRrmParametros.put("nick", nick);
		for (EstudianteJdbcDto item : listaRecordAcademico) {
			StringBuilder sbCodigoEstudiante = new StringBuilder();
			StringBuilder sbFichaMatricula = new StringBuilder();
			frmRrmParametros.put("nombres", item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			frmRrmParametros.put("identificacion", item.getPrsIdentificacion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			sbCodigoEstudiante.append(item.getPrsId());
			frmRrmParametros.put("codigo_estudiante", sbCodigoEstudiante.toString());
//			frmRrmParametros.put("periodo", item.getPracDescripcion());
			sbFichaMatricula.append(item.getFcmtId());
			frmRrmParametros.put("ficha", sbFichaMatricula.toString());
			break;
		}
		
		//Para agregar el secretario abogado
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
		
		//Para agregar el director de carrera
		StringBuilder sbDirectorCarrera = new StringBuilder();
		if(dir != null){
			if(dir.getPrsSegundoApellido() != null){
				sbDirectorCarrera.append(dir.getPrsNombres() + " "+dir.getPrsPrimerApellido() + " "+dir.getPrsSegundoApellido());
			}else{
				sbDirectorCarrera.append(dir.getPrsNombres() + " "+dir.getPrsPrimerApellido());
			}
			
			frmRrmParametros.put("director_carrera", sbDirectorCarrera.toString());
		}else{
			frmRrmParametros.put("director_carrera", " ");
		}
		
		//El promedio del estudiante
		if(prome != null){
			for (EstudianteJdbcDto item : prome) {
				StringBuilder promedio = new StringBuilder();
				if(item.getProme() != null){
					promedio.append(item.getProme().setScale(2,RoundingMode.HALF_EVEN));
					frmRrmParametros.put("promedio", promedio.toString());
				}else{
					frmRrmParametros.put("promedio", "-");
				}
			}
		}else{
			frmRrmParametros.put("promedio", "-");
		}
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteCertificadoRecordAcademico");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraRecordAcademico.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieCertificado.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaRecordAcademico) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbNota3 = new StringBuilder();
			StringBuilder sbNotaTotal = new StringBuilder();
//			StringBuilder sbAsistencia1 = new StringBuilder();
//			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbAsistenciaTotal = new StringBuilder();
			StringBuilder sbHoras = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbContador = new StringBuilder();
			StringBuilder sbPeriodoMatricula = new StringBuilder();
			datoEstudiantes.put("codigo", item.getMtrCodigo());
			datoEstudiantes.put("materia", item.getMtrDescripcion());
			sbPeriodoMatricula.append(item.getPracDescripcion()+" "+"COD. MAT. "+item.getFcmtId());
			datoEstudiantes.put("periodo", sbPeriodoMatricula.toString());
//			sbMatricula.append(item.getDtmtNumero());
//			datoEstudiantes.put("matricula", sbMatricula.toString());
			if(item.getMtrHoras() != GeneralesConstantes.APP_ID_BASE){
				sbHoras.append(item.getMtrHoras());
				datoEstudiantes.put("horas", sbHoras.toString());
			}else{
				datoEstudiantes.put("horas", "--");
			}
			
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
//			if(item.getClfAsistenciaEstudiante1() != null && item.getClfAsistenciaDocente1() != null){
//				sbAsistencia1.append(item.getClfAsistenciaEstudiante1()+"/"+item.getClfAsistenciaDocente1());
//				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//			}else if(item.getClfAsistenciaEstudiante1() == null && item.getClfAsistenciaDocente1() == null){
//				sbAsistencia1.append("-/-");
//				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//			}else if(item.getClfAsistenciaEstudiante1() != null && item.getClfAsistenciaDocente1() == null){
//				sbAsistencia1.append(item.getClfAsistenciaEstudiante1()+"/"+"-");
//				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//			}else if(item.getClfAsistenciaEstudiante1() == null && item.getClfAsistenciaDocente1() != null){
//				sbAsistencia1.append("-"+"/"+item.getClfAsistenciaDocente1());
//				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
//			}
			//Asistencia y Nota 2do Hemisemestre
			if(item.getClfNota2() != null){
				BigDecimal nota2 = item.getClfNota2().setScale(2,RoundingMode.HALF_EVEN);
				sbNota2.append(nota2);
				datoEstudiantes.put("nota2", sbNota2.toString());
			}else{
				sbNota2.append("-");
				datoEstudiantes.put("nota2", sbNota2.toString());
			}
//			if(item.getClfAsistenciaEstudiante2() != null && item.getClfAsistenciaDocente2() != null){
//				sbAsistencia2.append(item.getClfAsistenciaEstudiante2()+"/"+item.getClfAsistenciaDocente2());
//				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
//			}else if(item.getClfAsistenciaEstudiante2() == null && item.getClfAsistenciaDocente2() == null){
//				sbAsistencia2.append("-/-");
//				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
//			}else if(item.getClfAsistenciaEstudiante2() != null && item.getClfAsistenciaDocente2() == null){
//				sbAsistencia2.append(item.getClfAsistenciaEstudiante2()+"/"+"-");
//				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
//			}else if(item.getClfAsistenciaEstudiante2() == null && item.getClfAsistenciaDocente2() != null){
//				sbAsistencia2.append("-"+"/"+item.getClfAsistenciaDocente2());
//				datoEstudiantes.put("asistencia2", sbAsistencia2.toString());
//			}
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
				sbAsistenciaTotal.append(item.getClfPromedioAsistencia());
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
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL);
			}
			
			if(item.getDtmtNumero() == DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE){
				datoEstudiantes.put("matricula", DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);
			} else if(item.getDtmtNumero() == DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE){
				datoEstudiantes.put("matricula", DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);
			} else if(item.getDtmtNumero() == DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE){
				datoEstudiantes.put("matricula", DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);
			}else{
				datoEstudiantes.put("matricula", "--");
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
	
	
	
	
	
}
