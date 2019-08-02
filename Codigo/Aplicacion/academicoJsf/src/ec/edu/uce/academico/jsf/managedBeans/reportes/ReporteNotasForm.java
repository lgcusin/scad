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
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 03-04-2017			 Vinicio Rosales                         Emisión Inicial
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
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteNotasForm. 
 * Bean de sesion que maneja los reportes de del ingreso de notas del docente.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "reporteNotasForm")
@SessionScoped
public class ReporteNotasForm implements Serializable {
	
	private static final long serialVersionUID = -8433854820036679007L;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	public static void generarReporteNotas(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		if(listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE)	{
			frmRrmNombreReporte = "reporteNotasIdiomas";
		}else{
			frmRrmNombreReporte = "reporteNota1Hemi";	
		}
		
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			if(item.getClfAsistenciaDocente1() != null){
				sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
			
			break;
		}
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		if(listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE)	{
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasIdiomas");
		}else{
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");	
		}
		
		
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		BigDecimal cont1 = BigDecimal.ZERO;
		BigDecimal suma = BigDecimal.ZERO;
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbAsistencia1 = new StringBuilder();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getClfNota1() != null){
				BigDecimal nota1 = item.getClfNota1().setScale(2,RoundingMode.HALF_EVEN);
				sbNota1.append(nota1);
				datoEstudiantes.put("nota1", sbNota1.toString());
				cont1 = cont1.add(new BigDecimal(1));
				suma = suma.add(nota1);
				
			}else{
				sbNota1.append("-");
				datoEstudiantes.put("nota1", sbNota1.toString());
			}
			if(item.getClfAsistenciaEstudiante1() != null){
				if(listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
						|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
						|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
						|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
						|| listaEstudiantes.get(0).getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE)	{
					sbAsistencia1.append(item.getClfPromedioAsistencia());
				}else{
					sbAsistencia1.append(item.getClfAsistenciaEstudiante1());
				}
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else{
				sbAsistencia1.append("-");
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			if(item.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE 
					|| item.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
					|| item.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
					|| item.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
					|| item.getCrrId()== CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE)	{
				switch (item.getRcesEstado()) {
				case 0:
					datoEstudiantes.put("estado", "MATR.");
					break;
				case 1:
					datoEstudiantes.put("estado", "APRO.");
					break;
				case 2:
					datoEstudiantes.put("estado", "REPR.");
					break;
				default:
					break;
				}
				
			}
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont + 1;
		}
		
		StringBuilder sbPromedio = new StringBuilder();
		if(cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO){
			BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
			if(promedio != null){
				sbPromedio.append(promedio);
				frmRrmParametros.put("promedio", sbPromedio.toString());
			}else{
				sbPromedio.append("-");
				frmRrmParametros.put("promedio", sbPromedio.toString());
			}
		}else{
			sbPromedio.append("-");
			frmRrmParametros.put("promedio", sbPromedio.toString());
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
	
	
	public static void generarReporteNotasModular(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente, String materia, String facultad, String carrera){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNota1Hemi";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", facultad);
			frmRrmParametros.put("carrera", carrera);
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", materia);
			if(item.getClmdAsistenciaDocente1() != null){
				sbAsistenciaDocente.append(item.getClmdAsistenciaDocente1());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		BigDecimal cont1 = BigDecimal.ZERO;
		BigDecimal suma = BigDecimal.ZERO;
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbAsistencia1 = new StringBuilder();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getClmdNota1() != null){
				BigDecimal nota1 = item.getClmdNota1().setScale(2,RoundingMode.HALF_EVEN);
				sbNota1.append(nota1);
				datoEstudiantes.put("nota1", sbNota1.toString());
				cont1 = cont1.add(new BigDecimal(1));
				suma = suma.add(nota1);
				System.out.println(nota1);
			}else{
				sbNota1.append("-");
				datoEstudiantes.put("nota1", sbNota1.toString());
			}
			if(item.getClmdAsistenciaEstudiante1() != null){
				sbAsistencia1.append(item.getClmdAsistenciaEstudiante1());
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}else{
				sbAsistencia1.append("-");
				datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
			}
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont + 1;
		}
		
		StringBuilder sbPromedio = new StringBuilder();
		if(cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO){
			BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
			if(promedio != null){
				sbPromedio.append(promedio);
				frmRrmParametros.put("promedio", sbPromedio.toString());
			}else{
				sbPromedio.append("-");
				frmRrmParametros.put("promedio", sbPromedio.toString());
			}
		}else{
			sbPromedio.append("-");
			frmRrmParametros.put("promedio", sbPromedio.toString());
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
	
	
	public static void generarReporteNotasModular2Hemi(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente, String materia, String facultad, String carrera){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNota2Hemi";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", facultad);
			frmRrmParametros.put("carrera", carrera);
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", materia);
			if(item.getClmdAsistenciaDocente2() != null){
				sbAsistenciaDocente.append(item.getClmdAsistenciaDocente2());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		BigDecimal cont1 = BigDecimal.ZERO;
		BigDecimal suma = BigDecimal.ZERO;
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getClmdNota2() != null){
				BigDecimal nota2 = item.getClmdNota2().setScale(2,RoundingMode.HALF_EVEN);
				sbNota2.append(nota2);
				datoEstudiantes.put("nota1", sbNota2.toString());
				cont1 = cont1.add(new BigDecimal(1));
				suma = suma.add(nota2);
				
			}else{
				sbNota2.append("-");
				datoEstudiantes.put("nota1", sbNota2.toString());
			}
			if(item.getClmdAsistenciaEstudiante1() != null){
				sbAsistencia2.append(item.getClmdAsistenciaEstudiante2());
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}else{
				sbAsistencia2.append("-");
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont + 1;
		}
		
		StringBuilder sbPromedio = new StringBuilder();
		if(cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO){
			BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
			if(promedio != null){
				sbPromedio.append(promedio);
				frmRrmParametros.put("promedio", sbPromedio.toString());
			}else{
				sbPromedio.append("-");
				frmRrmParametros.put("promedio", sbPromedio.toString());
			}
		}else{
			sbPromedio.append("-");
			frmRrmParametros.put("promedio", sbPromedio.toString());
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
	
	
	public static void generarReporteNotasSegundoHemisemestre(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNota2Hemi";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			if(item.getClfAsistenciaDocente2() != null){
				sbAsistenciaDocente.append(item.getClfAsistenciaDocente2());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/segundoHemi");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		BigDecimal cont1 = BigDecimal.ZERO;
		BigDecimal suma = BigDecimal.ZERO;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getClfNota2() != null){
				BigDecimal nota2 = item.getClfNota2().setScale(2,RoundingMode.HALF_EVEN);
				sbNota2.append(nota2);
				datoEstudiantes.put("nota1", sbNota2.toString());
				cont1 = cont1.add(new BigDecimal(1));
				suma = suma.add(nota2);
			}else{
				sbNota2.append("-");
				datoEstudiantes.put("nota1", sbNota2.toString());
			}
			if(item.getClfAsistenciaEstudiante2() != null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante2());
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}else{
				sbAsistencia2.append("-");
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont + 1;
		}
		
		StringBuilder sbPromedio = new StringBuilder();
		if(cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO){
			BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
			if(promedio != null){
				sbPromedio.append(promedio);
			}else{
				sbPromedio.append("-");
			}
		}else{
			sbPromedio.append("-");
		}
		frmRrmParametros.put("promedio", sbPromedio.toString());
		
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
	
	
	public static void generarReporteNotasPosgrado(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNotaPosgrado";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			if(item.getClfAsistenciaDocente2() != null){
				sbAsistenciaDocente.append(item.getClfAsistenciaDocente2());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasPosgrado");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		BigDecimal cont1 = BigDecimal.ZERO;
		BigDecimal suma = BigDecimal.ZERO;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getClfNota2() != null){
				BigDecimal nota2 = item.getClfNota2().setScale(2,RoundingMode.HALF_UP);
				sbNota2.append(nota2);
				datoEstudiantes.put("nota1", sbNota2.toString());
				cont1 = cont1.add(new BigDecimal(1));
				suma = suma.add(nota2);
			}else if(item.getClfNota1() != null){
				BigDecimal nota1 = item.getClfNota1().setScale(2,RoundingMode.HALF_UP);
				sbNota2.append(nota1);
				datoEstudiantes.put("nota1", sbNota2.toString());
				cont1 = cont1.add(new BigDecimal(1));
				suma = suma.add(nota1);
			}else{
				sbNota2.append("-");
				datoEstudiantes.put("nota1", sbNota2.toString());
			}
			if(item.getClfAsistenciaEstudiante2() != null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante2());
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}else if(item.getClfAsistenciaEstudiante1() != null){
				sbAsistencia2.append(item.getClfAsistenciaEstudiante1());
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}else{
				sbAsistencia2.append("-");
				datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
			}
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			
			frmRrmCampos.add(datoEstudiantes);
			cont = cont + 1;
		}
		
//		StringBuilder sbPromedio = new StringBuilder();
//		if(cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO){
//			BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
//			if(promedio != null){
//				sbPromedio.append(promedio);
//				frmRrmParametros.put("promedio", sbPromedio.toString());
//			}else{
//				sbPromedio.append("-");
//				frmRrmParametros.put("promedio", sbPromedio.toString());
//			}
//		}else{
//			sbPromedio.append("-");
//			frmRrmParametros.put("promedio", sbPromedio.toString());
//		}
		
		
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

	public static void generarReporteNotasRecuperacion(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNotaRecuperacion";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			if(item.getClfAsistenciaDocente1() != null){
				sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota3 = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getClfSupletorio() != null){
				BigDecimal nota3 = item.getClfSupletorio().setScale(2,RoundingMode.HALF_EVEN);
				sbNota3.append(nota3);
				datoEstudiantes.put("nota1", sbNota3.toString());
			}else{
				sbNota3.append("-");
				datoEstudiantes.put("nota1", sbNota3.toString());
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
	
	public static void generarReporteNotasTotales(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNotasTotales";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			if(item.getClfAsistenciaDocente1() != null){
				sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasFinales");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbNota3 = new StringBuilder();
			StringBuilder sbNotaTotal = new StringBuilder();
			StringBuilder sbAsistencia1 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbAsistenciaTotal = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
//			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres());
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
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL);
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
	
	public static void generarReporteNotasTotalesModular(List<EstudianteJdbcDto> listaEstudiantes, Usuario docente){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteNotasTotales";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		StringBuilder sbAsistenciaDocente = new StringBuilder();
		frmRrmParametros.put("nick", docente.getUsrNick());
		frmRrmParametros.put("docente", docente.getUsrPersona().getPrsNombres()+" "+docente.getUsrPersona().getPrsPrimerApellido()+" "+docente.getUsrPersona().getPrsSegundoApellido());
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			if(item.getClfAsistenciaDocente1() != null){
				sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}else{
				sbAsistenciaDocente.append("-");
				frmRrmParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
			}
				
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasFinales");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbNota1 = new StringBuilder();
			StringBuilder sbNota2 = new StringBuilder();
			StringBuilder sbNota3 = new StringBuilder();
			StringBuilder sbNotaTotal = new StringBuilder();
			StringBuilder sbAsistencia1 = new StringBuilder();
			StringBuilder sbAsistencia2 = new StringBuilder();
			StringBuilder sbAsistenciaTotal = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
//			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres());
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
			try {
				int com = item.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
				if(com == 1 || com == 0){
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				}else{
						int minNota = item.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
						//si la suma de los parciales el menor a 27.5 y es mayor o igual a 8.8
						if(minNota==0 || minNota==1){
							if(item.getDtmtNumero()==3){
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							}else{
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
							}
								
						}else{
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
				}	
			} catch (Exception e) {
				item.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
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
			if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE){
				datoEstudiantes.put("estado", RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL);
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
