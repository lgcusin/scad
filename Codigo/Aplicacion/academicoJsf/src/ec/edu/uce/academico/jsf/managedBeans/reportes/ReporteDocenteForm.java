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
   
 ARCHIVO:     ReporteDocenteForm.java	  
 DESCRIPCION: Bean de sesion que maneja el reportes de listado de estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 28-04-2017			 Vinicio Rosales                         Emisión Inicial
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;

/**
 * Clase (session bean) ReporteDocenteForm. 
 * Bean de sesion que maneja los reportes de los docentes.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "reporteDocenteForm")
@SessionScoped
public class ReporteDocenteForm implements Serializable {

	private static final long serialVersionUID = 6705724173087288262L;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	public static void generarReporteDocente(List<EstudianteJdbcDto> listaEstudiantes){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteDocente";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			frmRrmParametros.put("periodo", item.getPracDescripcion());
			frmRrmParametros.put("facultad", item.getDpnDescripcion());
			frmRrmParametros.put("carrera", item.getCrrDescripcion());
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			frmRrmParametros.put("paralelo", item.getPrlDescripcion());
			frmRrmParametros.put("materia", item.getMtrDescripcion());
			break;
		}
		
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteDocente");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		
		for (EstudianteJdbcDto item : listaEstudiantes) {
			datoEstudiantes = new HashMap<String, Object>();
			StringBuilder sbApellidos = new StringBuilder();
			datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
			sbApellidos.append(item.getPrsPrimerApellido());
			sbApellidos.append(" ");
			sbApellidos.append(item.getPrsSegundoApellido());
			datoEstudiantes.put("apellidos", sbApellidos.toString());
//			datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
//			datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("nombres", item.getPrsNombres());
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
	
	
	
	
	
}
