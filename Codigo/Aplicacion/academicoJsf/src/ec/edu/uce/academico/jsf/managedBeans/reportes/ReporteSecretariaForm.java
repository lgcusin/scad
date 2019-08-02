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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;

/**
 * Clase (session bean) ReporteSecretariaForm. 
 * Bean de sesion que maneja los reportes de la secreataria.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "reporteSecretariaForm")
@SessionScoped
public class ReporteSecretariaForm implements Serializable {

	private static final long serialVersionUID = 6705724173087288262L;

	@EJB
	private MateriaServicio servMateriaServicio;
	
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	public static void generarReporteSecretaria(List<EstudianteJdbcDto> listaEstudiantes, int idParalelo){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteSecretaria";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			
			if(idParalelo != -99){
				frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			}else{
				frmRrmParametros.put("paralelo", "TODOS");
			}
			
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSecretaria/listadoEstudiantes");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			frmRrmCampos.add(datoEstudiantes);
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			cont ++;
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
	
	public static void generarReporteSecretariaEstudiante(List<EstudianteJdbcDto> listaEstudiantes, int idParalelo, int idMateria, Materia materia, int nivelacion){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		if(nivelacion == NivelConstantes.NIVEL_NIVELACION_VALUE){
			frmRrmNombreReporte = "reporteSecretariaNivelacion";
		}else{
			frmRrmNombreReporte = "reporteSecretaria";
		}
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			
			if(idParalelo != -99){
				frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			}else{
				frmRrmParametros.put("paralelo", "TODOS");
			}
			
			break;
		}
		
		if(idMateria != -99){
			frmRrmParametros.put("materia", materia.getMtrDescripcion());
		}else{
			frmRrmParametros.put("materia", "TODOS");
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSecretaria/listadoEstudiantes");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			datoEstudiantes.put("carreranivelacion", item.getFcinCarreraSiiuSt());
			frmRrmCampos.add(datoEstudiantes);
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			cont ++;
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
	
	
	public static void generarReporteSecretariaLibro(List<EstudianteJdbcDto> listaEstudiantes, int idParalelo){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteSecretariaLibro";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
//			frmRrmParametros.put("curso", item.getNvlDescripcion());
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_NIVELACION_VALUE){
				frmRrmParametros.put("curso", "NIVELACIÓN");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_PRIMERO_VALUE){
				frmRrmParametros.put("curso", "PRIMERO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_SEGUNDO_VALUE){
				frmRrmParametros.put("curso", "SEGUNDO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_TERCER_VALUE){
				frmRrmParametros.put("curso", "TERCERO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_CUARTO_VALUE){
				frmRrmParametros.put("curso", "CUARTO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_QUINTO_VALUE){
				frmRrmParametros.put("curso", "QUINTO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_SEXTO_VALUE){
				frmRrmParametros.put("curso", "SEXTO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_SEPTIMO_VALUE){
				frmRrmParametros.put("curso", "SÉPTIMO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_OCTAVO_VALUE){
				frmRrmParametros.put("curso", "OCTAVO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_NOVENO_VALUE){
				frmRrmParametros.put("curso", "NOVENO");
			}
			if(item.getFcmtNivelUbicacion() == NivelConstantes.NIVEL_DECIMO_VALUE){
				frmRrmParametros.put("curso", "DÉCIMO");
			}
			
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSecretaria/listadoEstudiantesLibro");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbMatricula = new StringBuilder();
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			if(item.getUbcPaisDescripcion() !=  null){
				datoEstudiantes.put("nacionalidad", item.getUbcPaisDescripcion());
			}else{
				datoEstudiantes.put("nacionalidad", "");
			}
			
			sbMatricula.append(item.getFcmtId());
			datoEstudiantes.put("matricula", sbMatricula.toString());
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			cont ++;
			frmRrmCampos.add(datoEstudiantes);
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
	
	
	public static void generarReporteEstadoMatricula(List<EstudianteJdbcDto> listaEstudiantes, int idParalelo, int idMateria, Materia materia, int nivelacion){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		if(nivelacion == NivelConstantes.NIVEL_NIVELACION_VALUE){
			frmRrmNombreReporte = "reporteSecretariaNivelacion";
		}else{
			frmRrmNombreReporte = "reporteSecretaria";
		}
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
//			frmRrmParametros.put("facultad", item.getDpnDescripcion());
//			frmRrmParametros.put("carrera", item.getCrrDescripcion());
//			frmRrmParametros.put("curso", item.getNvlDescripcion());
//			
//			if(idParalelo != -99){
//				frmRrmParametros.put("paralelo", item.getPrlDescripcion());
//			}else{
//				frmRrmParametros.put("paralelo", "TODOS");
//			}
			
			break;
		}
		
//		if(idMateria != -99){
//			frmRrmParametros.put("materia", materia.getMtrDescripcion());
//		}else{
//			frmRrmParametros.put("materia", "TODOS");
//		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSecretaria/matriculaEstado");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		int cont = 1;
		ListasCombosForm combos = new ListasCombosForm();
		for (EstudianteJdbcDto item : listaEstudiantes) {
			StringBuilder sbContador = new StringBuilder();
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("facultad", item.getDpnDescripcion());
			datoEstudiantes.put("carrera", item.getCrrDescripcion());
			datoEstudiantes.put("curso", item.getNvlDescripcion());
			datoEstudiantes.put("paralelo", item.getPrlDescripcion());
			datoEstudiantes.put("materia", item.getMtrDescripcion());
			datoEstudiantes.put("estado", combos.getListaEstadoRecorAcademico(item.getRcesEstado()));
			
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
			datoEstudiantes.put("carreranivelacion", item.getFcinCarreraSiiuSt());
			frmRrmCampos.add(datoEstudiantes);
			sbContador.append(cont);
			datoEstudiantes.put("numero", sbContador.toString());
			cont ++;
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
