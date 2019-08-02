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
 06-03-2018			 Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (session bean) ReporteRegistroMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "registroMatriculaForm")
@SessionScoped
public class ReporteRegistroMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = -6595466142462136234L;
	public static final String PATH_GENERAL_REPORTE = "/academico/reportes/";
	public static final String PATH_GENERAL_IMG_PIE = "/academico/reportes/imagenes/plantillaPie.png";
	public static final String PATH_GENERAL_IMG_CABECERA = "/academico/reportes/imagenes/plantillaCabecera.png";
	public static final String GENERAL_NOMBRE_INSTITUCION = "UNIVERSIDAD \nCENTRAL \nDEL ECUADOR";
	public static final String GENERAL_TITULO_REPORTE_REGISTRO_MATRICULA = "REGISTRO DE MATRÍCULA";
	public static final String GENERAL_TITULO_REPORTE_ORDEN_COBRO = "ORDEN DE COBRO";
	public static final String GENERAL_PIE_PAGINA = "Copyright Universidad Central del Ecuador 2018";
	public static final String PATH_GENERAL_IMAGEN_QR = "/academico/reportes/imagenes/codigoQR.png";
	public static final String GENERAL_DOC_AUTOGENERADO = "Documento generado en siiu.uce.edu.ec el " +  cambiarDateToStringFormatoFecha(Date.from(Instant.now()), "dd/MM/yyyy HH:mm:ss");
	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	public static void generarReporteRegistroMatricula(List<EstudianteJdbcDto> listaRegistroMatricula, String nick, CarreraDto carrera, String pracDescripcion){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteGeneracionMatricula";
		frmRrmParametros = new HashMap<String, Object>();
		StringBuilder sbNombres = new StringBuilder();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		
		for (EstudianteJdbcDto item : listaRegistroMatricula) {
//			frmRrmParametros.put("primer_apellido", item.getPrsPrimerApellido());
//			frmRrmParametros.put("segundo_apellido", item.getPrsSegundoApellido());
//			frmRrmParametros.put("nombres", item.getPrsNombres());
			
//			frmRrmParametros.put("curso", "1");
			frmRrmParametros.put("curso", item.getNvlDescripcion());
			break;
		}
		
		
		frmRrmParametros.put("periodo", pracDescripcion);
		sbNombres.append(carrera.getPrsNombres());sbNombres.append(" ");
		sbNombres.append(carrera.getPrsPrimerApellido());sbNombres.append(" ");
		sbNombres.append(carrera.getPrsSegundoApellido());sbNombres.append(" ");
		frmRrmParametros.put("identificacion", carrera.getPrsIdentificacion());
		frmRrmParametros.put("facultad", carrera.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDescripcion());
		
		
				
		frmRrmParametros.put("nombres", sbNombres.toString());
//		StringBuilder sbContador = new StringBuilder();
		StringBuilder sbMaterias = new StringBuilder();
		StringBuilder sbParalelo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbMatricula = new StringBuilder();
		StringBuilder sbhoras = new StringBuilder();
		StringBuilder sbCosto = new StringBuilder();
		for (EstudianteJdbcDto item : listaRegistroMatricula) {
			
			
			if(item.getMtrDescripcion().length() <= 80){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==0){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n");
				sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n");
				sbCosto.append(item.getDtmtValorPorMateria());sbCosto.append("\n\n");
			}
			if(item.getMtrDescripcion().length() > 80 && item.getMtrDescripcion().length() <= 160){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==0){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n");
				sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n");
				sbCosto.append(item.getDtmtValorPorMateria());sbCosto.append("\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 160 && item.getMtrDescripcion().length() <= 240){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==0){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n");
				sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n");
				sbCosto.append(item.getDtmtValorPorMateria());sbCosto.append("\n\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 240 && item.getMtrDescripcion().length() <= 320){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==0){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n");
				sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n");
				sbCosto.append(item.getDtmtValorPorMateria());sbCosto.append("\n\n\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 320 && item.getMtrDescripcion().length() <= 400){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==0){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n");
				sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n\n");
				sbCosto.append(item.getDtmtValorPorMateria());sbCosto.append("\n\n\n\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 400 && item.getMtrDescripcion().length() <= 480){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==0){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n\n");
				sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n\n\n");
				sbCosto.append(item.getDtmtValorPorMateria());sbCosto.append("\n\n\n\n\n\n\n");
			}
			
			
		}
		frmRrmParametros.put("materias", sbMaterias.toString());
		frmRrmParametros.put("paralelo", sbParalelo.toString());
		frmRrmParametros.put("codigo", sbCodigo.toString());
		try {
			frmRrmParametros.put("matricula", sbMatricula.toString());
			frmRrmParametros.put("horas", sbhoras.toString());
			frmRrmParametros.put("costo", sbCosto.toString());	
		} catch (Exception e) {
		}
		
		frmRrmParametros.put("nick", nick);
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/generacionMatricula");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmRrmParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
	} 

	/**



	Map<String, Object> frmRrmParametros = null;
	List<Map<String, Object>> frmCrpCampos = null;

	
	StringBuilder pathGeneralReportes = new StringBuilder();
	pathGeneralReportes.append("");

	frmRrmParametros = new HashMap<String, Object>();
	String frmNombreReporte = "reporteRegistroMatricula.jasper";
			
	StringBuilder contenido = new StringBuilder();
	
	frmRrmParametros = new HashMap<String, Object>();
	frmRrmParametros = new HashMap<String, Object>();
	
	frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMAGEN_CABECERA);
	frmRrmParametros.put("imagenPie", pathGeneralReportes + PATH_GENERAL_IMAGEN_PIE);
	frmRrmParametros.put("encabezado_institucion", GENERAL_INSTITUCION );
	frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE);

	frmRrmParametros.put("carrera_label", "CARRERA");
	frmRrmParametros.put("regimen", "Créditos");
	
	frmRrmParametros.put("periodo", "PERIODO ACADÉMICO 2017 - 2018");
	frmRrmParametros.put("facultad", "FACULTAD DE CIENCIAS FÍSICAS Y MATEMÁTICA");
	frmRrmParametros.put("carrera", "INGENIERÍA INFORMATICA");
	frmRrmParametros.put("nombres", "FREDDY GEOVANNY GUZMÁN ALARCÓN");
	frmRrmParametros.put("nivel", "NOVENO");
	frmRrmParametros.put("gratuidad", "SI");
	frmRrmParametros.put("identificacion", "1720088010");
	frmRrmParametros.put("nick", "fgguzman");
	frmRrmParametros.put("leyenda", nombreLeyenda);
	frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
	
	
	

	frmCrpCampos = new ArrayList<Map<String, Object>>();
	frmCrpCampos.add(new HashMap<String, Object>());

	Reporte reporte = new Reporte("Reporte", 280, 10, 850, 750);
	reporte.cargarReporte(PATH_GENERAL_REPORTE + "/reporteRegistroMatricula.jasper", frmRrmParametros,	new JRBeanCollectionDataSource(frmCrpCampos));

*/
	
	
	//------------------------------------------------------GENERACION DE REPORTES VISUALIZABLES-----------------------------------------------------------------
	
	/**
	 * Generacion de reporte Registro de Matricula
	 * @author Arturo Villafuerte - ajvillafuerte //--v1
	 * @author Freddy Guzman - fgguzman //--v2
	 * @author Arturo Villafuerte - ajvillafuerte //--v3
	 **/
	public static void generarReporteRegistroMatriculaFull(List<EstudianteJdbcDto> listaRegistroMatricula, String nick, CarreraDto carrera, String pracDescripcion, String nivel, String gratuidad){
		
		StringBuilder sbNombres = new StringBuilder(); 
		sbNombres.append(carrera.getPrsNombres());sbNombres.append(" ");
		sbNombres.append(carrera.getPrsPrimerApellido());sbNombres.append(" ");
		sbNombres.append(carrera.getPrsSegundoApellido());sbNombres.append(" ");
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
 
		String frmRrmNombreReporte = null;
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;

		frmRrmNombreReporte = "REGISTRO DE MATRÍCULA";
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_REGISTRO_MATRICULA);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO "+pracDescripcion.replace("-", " - "));
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("identificacion", carrera.getPrsIdentificacion());
		frmRrmParametros.put("facultad", carrera.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDetalle());
		frmRrmParametros.put("regimen", cabeceraCreditosHoras(listaRegistroMatricula.get(listaRegistroMatricula.size()-1).getMtrCreditos(), listaRegistroMatricula.get(listaRegistroMatricula.size()-1).getMtrHoras()));
		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("carrera_label", "CARRERA");
			frmRrmParametros.put("gratuidad", gratuidad);
		}else{
			frmRrmParametros.put("carrera_label", "ÁREA DE CONOCIMIENTO");
			frmRrmParametros.put("gratuidad", "N/A");
		}
		
		frmRrmParametros.put("nombres", sbNombres.toString());
		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("nivel", nivel);
		}else{
			frmRrmParametros.put("nivel", "N/A");
		}
			
		frmRrmParametros.put("nick", nick);
		 
		StringBuilder sbItem = new StringBuilder();
		StringBuilder sbMaterias = new StringBuilder();
		StringBuilder sbParalelo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbMatricula = new StringBuilder();
		StringBuilder sbhoras = new StringBuilder();
		StringBuilder sbCosto = new StringBuilder();
		
		
		int contador = 0;
		
		for (EstudianteJdbcDto item : listaRegistroMatricula) {
			
			if(item.getMtrDescripcion().length() <= 80){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+(item.getMtrHorasPAE()!=null?item.getMtrHorasPAE():0));sbhoras.append("\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()!=null?item.getDtmtValorPorMateria():BigDecimal.ZERO));sbCosto.append("\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 80 && item.getMtrDescripcion().length() <= 160){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+(item.getMtrHorasPAE()!=null?item.getMtrHorasPAE():0));sbhoras.append("\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()!=null?item.getDtmtValorPorMateria():BigDecimal.ZERO));sbCosto.append("\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 160 && item.getMtrDescripcion().length() <= 240){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+(item.getMtrHorasPAE()!=null?item.getMtrHorasPAE():0));sbhoras.append("\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()!=null?item.getDtmtValorPorMateria():BigDecimal.ZERO));sbCosto.append("\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 240 && item.getMtrDescripcion().length() <= 320){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+(item.getMtrHorasPAE()!=null?item.getMtrHorasPAE():0));sbhoras.append("\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 320 && item.getMtrDescripcion().length() <= 400){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+(item.getMtrHorasPAE()!=null?item.getMtrHorasPAE():0));sbhoras.append("\n\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 400 && item.getMtrDescripcion().length() <= 480){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n\n");

				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+(item.getMtrHorasPAE()!=null?item.getMtrHorasPAE():0));sbhoras.append("\n\n\n\n\n\n\n");
				} 

				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n\n\n\n");
				
			}


		}
		
		frmRrmParametros.put("materias", sbMaterias.toString());
		frmRrmParametros.put("paralelo", sbParalelo.toString());
		frmRrmParametros.put("codigo", sbCodigo.toString());
		frmRrmParametros.put("item", sbItem.toString());
		frmRrmParametros.put("matricula", sbMatricula.toString()); 
		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("horas", sbhoras.toString());
			frmRrmParametros.put("costo", sbCosto.toString());	
		}else{
			frmRrmParametros.put("horas", "N/A");
			frmRrmParametros.put("costo", "N/A");
		}
		
		frmRrmCampos = new ArrayList<Map<String, Object>>(); 
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		 
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		 
	} 
	
	public static void generarReporteRegistroMatriculaNivelacion(String carreraSiiu, List<EstudianteJdbcDto> listaRegistroMatricula, String nick, CarreraDto carrera, String pracDescripcion, String nivel, String gratuidad){
		
		StringBuilder sbNombres = new StringBuilder(); 
		sbNombres.append(carrera.getPrsNombres());sbNombres.append(" ");
		sbNombres.append(carrera.getPrsPrimerApellido());sbNombres.append(" ");
		sbNombres.append(carrera.getPrsSegundoApellido());sbNombres.append(" ");
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
 
		String frmRrmNombreReporte = null;
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;

		frmRrmNombreReporte = "REGISTRO DE MATRÍCULA";
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_REGISTRO_MATRICULA);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO "+pracDescripcion.replace("-", " - "));
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("identificacion", carrera.getPrsIdentificacion());
		frmRrmParametros.put("facultad", carrera.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDetalle());
		frmRrmParametros.put("regimen", cabeceraCreditosHoras(listaRegistroMatricula.get(listaRegistroMatricula.size()-1).getMtrCreditos(), listaRegistroMatricula.get(listaRegistroMatricula.size()-1).getMtrHoras()));
		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("carrera_label", "CARRERA");
			frmRrmParametros.put("gratuidad", gratuidad);
		}else{
			frmRrmParametros.put("carrera_label", "ÁREA DE CONOCIMIENTO");
			frmRrmParametros.put("gratuidad", "N/A");
		}
		frmRrmParametros.put("area", carreraSiiu);
		
		frmRrmParametros.put("nombres", sbNombres.toString());
		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("nivel", nivel);
		}else{
			frmRrmParametros.put("nivel", "N/A");
		}
			
		frmRrmParametros.put("nick", nick);
		 
		StringBuilder sbItem = new StringBuilder();
		StringBuilder sbMaterias = new StringBuilder();
		StringBuilder sbParalelo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbMatricula = new StringBuilder();
		StringBuilder sbhoras = new StringBuilder();
		StringBuilder sbCosto = new StringBuilder();
		
		
		int contador = 0;
		
		for (EstudianteJdbcDto item : listaRegistroMatricula) {
			
			if(item.getMtrDescripcion().length() <= 80){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 80 && item.getMtrDescripcion().length() <= 160){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 160 && item.getMtrDescripcion().length() <= 240){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 240 && item.getMtrDescripcion().length() <= 320){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 320 && item.getMtrDescripcion().length() <= 400){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 400 && item.getMtrDescripcion().length() <= 480){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n\n");

				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n\n\n");
				} 

				sbCosto.append("$"+String.format("%.2f", item.getDtmtValorPorMateria()));sbCosto.append("\n\n\n\n\n\n\n");
				
			}


		}
		
		frmRrmParametros.put("materias", sbMaterias.toString());
		frmRrmParametros.put("paralelo", sbParalelo.toString());
		frmRrmParametros.put("codigo", sbCodigo.toString());
		frmRrmParametros.put("item", sbItem.toString());
		frmRrmParametros.put("matricula", sbMatricula.toString()); 
//		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//			frmRrmParametros.put("horas", sbhoras.toString());
//			frmRrmParametros.put("costo", sbCosto.toString());	
//		}else{
//			frmRrmParametros.put("horas", "N/A");
//			frmRrmParametros.put("costo", "N/A");
//		}
		
		frmRrmCampos = new ArrayList<Map<String, Object>>(); 
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		 
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		 
	} 

	/**
	 * Generacion de reporte Orden de Cobro.
	 * @author Freddy Guzman - fgguzman  // v1
	 * @author Arturo Villafuerte - a  // v1
	 * @param listaRegistroMatricula
	 * @param nick
	 * @param carrera
	 * @param pracDescripcion
	 * @param nivel
	 * @param gratuidad
	 * @param comprobante
	 */
	public static void generarReporteOrdenCobroFull(List<EstudianteJdbcDto> listaRegistroMatricula, Usuario usuario, CarreraDto carrera, String pracDescripcion, String nivel, String gratuidad, ComprobantePago comprobante){
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		
		StringBuilder indicaciones = new StringBuilder();
//		indicaciones.append("1. Las órdenes de cobro generadas hasta las 15H00 serán canceladas en SERVIPAGOS al día siguiente.");
//		indicaciones.append("\n2. Las órdenes de cobro generadas pasado las 15H00 serán canceladas en SERVIPAGOS al segundo día.");
//		indicaciones.append("\n3. El servicio bancario será pagado directamente en SERVIPAGOS.");
//		indicaciones.append("\n4. En el caso que el valor a pagar no sea el correcto, acércarse a la Secretaría de la Carrera.");
//		indicaciones.append("\n5. Si se ha caducado la orden de cobro, no tendrá derecho a reclamos posteriores y deberá matricularse\n    en el período  extraordinario, pagando el arancel adicional.");
//		indicaciones.append("\n6. Cualquier alteración y/o enmienda al presente documento, lo invalida.");

		indicaciones.append("1. Las órdenes de cobro deberán ser impresas y luego canceladas al día siguiente de la matrícula, \n    a partir de las 9H00; y la legalización se efectuará automáticamente al día siguiente del pago.");
		indicaciones.append("\n2. En el caso que el valor a pagar no sea el correcto, acércarse a la Secretaría de la Carrera.");
		indicaciones.append("\n3. Si no esta conforme con la matrícula generada, acercarse a la Secretaría de la Carrera inmediatamente\n    SIN EFECTUAR EL PAGO.");
		indicaciones.append("\n4. Si ha dejado caducar la órden de cobro, deberá acercarse a las ventanillas de Tesorería de la Dirección\n    General Financiera a cancelar el valor de la matrícula, y para legalizar la matrícula deberá acercarse a la \n    DTIC con el RECIBO.");
		indicaciones.append("\n5. Cualquier alteración y/o enmienda al presente documento, lo invalida.");

		StringBuilder nota = new StringBuilder();
		nota.append("LA FACTURA ELECTRÓNICA SERÁ ENVIADA AL CORREO INSTITUCIONAL, UNA VEZ VERIFICADO EL PAGO CORRESPONDIENTE.");
		nota.append("\nSi no recibe el documento, consultarlo en http://facturacion.uce.edu.ec/scgate");
		
		
		
		StringBuilder perosnaFQDN = new StringBuilder(); 
		perosnaFQDN.append(carrera.getPrsNombres());perosnaFQDN.append(" ");
		perosnaFQDN.append(carrera.getPrsPrimerApellido());perosnaFQDN.append(" ");
		perosnaFQDN.append(carrera.getPrsSegundoApellido());perosnaFQDN.append(" ");

		String frmRrmNombreReporte = null;
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;

		frmRrmNombreReporte = "ORDEN DE COBRO";
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("img_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("img_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("txt_cabecera_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("txt_cabecera_reporte", GENERAL_TITULO_REPORTE_ORDEN_COBRO);
		frmRrmParametros.put("txt_pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("txt_usuario", usuario.getUsrNick());
		frmRrmParametros.put("txt_autogenerado_fecha", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("dependencia", "FACULTAD DE "+carrera.getDpnDescripcion()+"\n"+ " CARRERA DE " + carrera.getCrrDescripcion());
//		frmRrmParametros.put("facultad", carrera.getDpnDescripcion());
//		frmRrmParametros.put("carrera", carrera.getCrrDetalle());
		frmRrmParametros.put("apellidos_nombres", perosnaFQDN.toString());
		frmRrmParametros.put("numero_comprobante", comprobante.getCmpaNumComprobante());
		frmRrmParametros.put("numero_comprobante_descripcion", comprobante.getCmpaNumComprobante() != null ? comprobante.getCmpaNumComprobante() : "");
		
		if(carrera.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("dependencia_categoria", "CARRERA");
		}else{
			frmRrmParametros.put("dependencia_categoria", "ÁREA DE CONOCIMIENTO");
		}
		
		frmRrmParametros.put("nivel", nivel);
		frmRrmParametros.put("gratuidad", gratuidad);
		frmRrmParametros.put("identificacion", carrera.getPrsIdentificacion());
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO " + pracDescripcion.replace("-", " - "));
		
  
		
		if(usuario.getUsrPersona().getPrsSectorDomicilio() == null 
				&& usuario.getUsrPersona().getPrsCallePrincipal() == null  
				&& usuario.getUsrPersona().getPrsCalleSecundaria() == null
				&& usuario.getUsrPersona().getPrsNumeroCasa() == null ){
			frmRrmParametros.put("direccion","");
		}else{
			frmRrmParametros.put("direccion", usuario.getUsrPersona().getPrsSectorDomicilio() != null ? usuario.getUsrPersona().getPrsSectorDomicilio() : "" 
					+usuario.getUsrPersona().getPrsCallePrincipal() != null ?  ", "+usuario.getUsrPersona().getPrsCallePrincipal() : "" 
					+usuario.getUsrPersona().getPrsCalleSecundaria() != null ? " Y "+usuario.getUsrPersona().getPrsCalleSecundaria() : ""
					+usuario.getUsrPersona().getPrsNumeroCasa() != null ? " ("+usuario.getUsrPersona().getPrsNumeroCasa()+")." : ""
					);
		}
		
		
		frmRrmParametros.put("telefono", usuario.getUsrPersona().getPrsCelular() != null ? usuario.getUsrPersona().getPrsCelular() : "");
		frmRrmParametros.put("email", usuario.getUsrPersona().getPrsMailInstitucional());
		frmRrmParametros.put("emision", comprobante.getCmpaFechaEmision() != null ? cambiarTimestampToString(comprobante.getCmpaFechaEmision(), "yyyy-MM-dd"):"");
		frmRrmParametros.put("caducidad", comprobante.getCmpaFechaCaduca() != null ? cambiarTimestampToString(comprobante.getCmpaFechaCaduca(), "yyyy-MM-dd"): "");
		frmRrmParametros.put("indicacion", indicaciones.toString());
		frmRrmParametros.put("nota", nota.toString());		
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		
		StringBuilder detalleItem = new StringBuilder();
		StringBuilder horasCreditosItem = new StringBuilder();
		StringBuilder valorItem = new StringBuilder();
		StringBuilder item = new StringBuilder();
		
		List<MateriaDto> detalle = cargarDetalle(listaRegistroMatricula, gratuidad);
		int contador = 0;
		BigDecimal valorTotal = BigDecimal.ZERO;
		Integer horasCreditosTotal = 0;
		
		for(MateriaDto items: detalle){
			contador=contador+1;
			item.append(contador+"\n");
			detalleItem.append(items.getMtrDescripcion()+"\n");
			horasCreditosItem.append(items.getMtrCreditos()+"\n");
			valorItem.append("$"+String.format("%.2f", items.getValorMatricula())+"\n");
			horasCreditosTotal = horasCreditosTotal+items.getMtrCreditos();
			valorTotal = valorTotal.add(items.getValorMatricula());
		}
		
		item.append("\n\n");
		detalleItem.append("\nTOTAL\n");
		horasCreditosItem.append("\n"+horasCreditosTotal+"\n");
		valorItem.append("\n$"+String.format("%.2f", valorTotal)+"\n");
		
		frmRrmParametros.put("item", item.toString());
		frmRrmParametros.put("materias", detalleItem.toString()); 
		frmRrmParametros.put("horas", horasCreditosItem.toString());
		frmRrmParametros.put("costo", valorItem.toString());	 
		frmRrmParametros.put("codigo_qr", codigoQR(pracDescripcion.replace("-", " - "), carrera.getDpnDescripcion(), carrera.getCrrDescripcion(), 
				perosnaFQDN.toString(), comprobante.getCmpaNumComprobante(), comprobante.getCmpaFechaEmision() != null ? cambiarTimestampToString(comprobante.getCmpaFechaEmision(), "yyyy-MM-dd"):"", 
				comprobante.getCmpaFechaCaduca() != null ? cambiarTimestampToString(comprobante.getCmpaFechaCaduca(), "yyyy-MM-dd"): "", nivel, gratuidad, valorTotal));
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	} 
	
	
	
	//------------------------------------------------------GENERACION BYTEARRAY REPORTES ENVIO MAIL-----------------------------------------------------------------
	
	/**
	 * Genera byteArray de reporte para mail Registro matricula
	 */
	public static byte[] generarReporteRegistroMatriculaMail(List<MateriaDto> listMateriaDto, Nivel nivel, PeriodoAcademico periodo, FichaInscripcionDto fichaInscripcion, Usuario usuario, Dependencia dependencia, Carrera carrera, String gratuidad){
		
		StringBuilder sbNombres = new StringBuilder(); 
		sbNombres.append(usuario.getUsrPersona().getPrsNombres());sbNombres.append(" ");
		sbNombres.append(usuario.getUsrPersona().getPrsPrimerApellido());sbNombres.append(" ");
		sbNombres.append(usuario.getUsrPersona().getPrsSegundoApellido());sbNombres.append(" ");

		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		JasperReport jasperReport = null;
		JasperPrint jasperPrint; 
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
 
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_REGISTRO_MATRICULA);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO "+ periodo.getPracDescripcion().replace("-", " - "));
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("identificacion", fichaInscripcion.getPrsIdentificacion());
		frmRrmParametros.put("facultad", dependencia.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDetalle());
		frmRrmParametros.put("regimen", cabeceraCreditosHoras(listMateriaDto.get(listMateriaDto.size()-1).getMtrCreditos(), listMateriaDto.get(listMateriaDto.size()-1).getMtrHoras()));
		if(dependencia.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("carrera_label", "CARRERA");
		}else{
			frmRrmParametros.put("carrera_label", "ÁREA DE CONOCIMIENTO");
		}
		frmRrmParametros.put("gratuidad", gratuidad);
		frmRrmParametros.put("nombres", sbNombres.toString());
		frmRrmParametros.put("nivel", nivel.getNvlDescripcion());
		frmRrmParametros.put("nick", usuario.getUsrNick());
		 
		StringBuilder sbItem = new StringBuilder();
		StringBuilder sbMaterias = new StringBuilder();
		StringBuilder sbParalelo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbMatricula = new StringBuilder();
		StringBuilder sbhoras = new StringBuilder();
		StringBuilder sbCosto = new StringBuilder();
		
		int contador = 0;
		
		for (MateriaDto item : listMateriaDto) {
			
			if(item.getMtrDescripcion().length() <= 80){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+item.getMtrHorasPracSema());sbhoras.append("\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 80 && item.getMtrDescripcion().length() <= 160){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+item.getMtrHorasPracSema());sbhoras.append("\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 160 && item.getMtrDescripcion().length() <= 240){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+item.getMtrHorasPracSema());sbhoras.append("\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 240 && item.getMtrDescripcion().length() <= 320){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+item.getMtrHorasPracSema());sbhoras.append("\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 320 && item.getMtrDescripcion().length() <= 400){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n");
				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+item.getMtrHorasPracSema());sbhoras.append("\n\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 400 && item.getMtrDescripcion().length() <= 480){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n\n");

				if(item.getMtrCreditos() != 0){
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n\n");
				}
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras()+item.getMtrHorasPracSema());sbhoras.append("\n\n\n\n\n\n\n");
				} 

				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n\n\n\n");
				
			}


		}
		
		frmRrmParametros.put("materias", sbMaterias.toString());
		frmRrmParametros.put("paralelo", sbParalelo.toString());
		frmRrmParametros.put("codigo", sbCodigo.toString());
		frmRrmParametros.put("item", sbItem.toString());
		frmRrmParametros.put("matricula", sbMatricula.toString()); 
		frmRrmParametros.put("horas", sbhoras.toString());
		frmRrmParametros.put("costo", sbCosto.toString());	
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		
		byte[] arreglo = null;
		
			try {
				pathGeneralReportes.append("/academico/reportes/archivosJasper/matricula/registro/");
				jasperReport = (JasperReport) JRLoader.loadObject(new File(pathGeneralReportes.append("/reporteRegistroMatricula.jasper").toString()));
				jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
				arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
				return arreglo;
				
			} catch (JRException e) {
			}
		
			return null;
	}
	
	
	/**
	 * Genera byteArray de reporte para mail Registro matricula
	 */
	public static byte[] generarReporteOrdenCobroMail(List<MateriaDto> listMateriaDto, Nivel nivel, ComprobantePago numeroComprobante, PeriodoAcademico periodo, FichaInscripcionDto fichaInscripcion, Usuario usuario, Dependencia dependencia, Carrera carrera, String gratuidad){
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		
		StringBuilder indicaciones = new StringBuilder();
		indicaciones.append("1. Las órdenes de cobro generadas hasta las 15H00 serán canceladas en SERVIPAGOS al día siguiente.");
		indicaciones.append("\n2. Las órdenes de cobro generadas pasado las 15H00 serán canceladas en SERVIPAGOS al segundo día.");
		indicaciones.append("\n3. El servicio bancario será pagado directamente en SERVIPAGOS.");
		indicaciones.append("\n4. En el caso que el valor a pagar no sea el correcto, acércarse a la Secretaría de la Carrera.");
		indicaciones.append("\n5. Si se ha caducado la orden de cobro, no tendrá derecho a reclamos posteriores y deberá matricularse\n    en el período  extraordinario, pagando el arancel adicional.");
		indicaciones.append("\n6. Cualquier alteración y/o enmienda al presente documento, lo invalida.");

		StringBuilder nota = new StringBuilder();
		nota.append("LA FACTURA ELECTRÓNICA SERÁ ENVIADA AL CORREO INSTITUCIONAL, UNA VEZ VERIFICADO EL PAGO CORRESPONDIENTE.");
		nota.append("\nSi no recibe el documento, consultarlo en http://facturacion.uce.edu.ec/scgate");
		 
		StringBuilder perosnaFQDN = new StringBuilder(); 
		perosnaFQDN.append(usuario.getUsrPersona().getPrsNombres());perosnaFQDN.append(" ");
		perosnaFQDN.append(usuario.getUsrPersona().getPrsPrimerApellido());perosnaFQDN.append(" ");
		perosnaFQDN.append(usuario.getUsrPersona().getPrsSegundoApellido());perosnaFQDN.append(" ");

		JasperReport jasperReport = null;
		JasperPrint jasperPrint;  
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
  
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("img_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("img_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("txt_cabecera_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("txt_cabecera_reporte", GENERAL_TITULO_REPORTE_ORDEN_COBRO);
		frmRrmParametros.put("txt_pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("txt_usuario", usuario.getUsrNick());
		frmRrmParametros.put("txt_autogenerado_fecha", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("facultad", dependencia.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDescripcion());
		frmRrmParametros.put("apellidos_nombres", perosnaFQDN.toString());
		frmRrmParametros.put("numero_comprobante", numeroComprobante.getCmpaNumComprobante());
		frmRrmParametros.put("numero_comprobante_descripcion", numeroComprobante.getCmpaNumComprobante() != null ? numeroComprobante.getCmpaNumComprobante() : "");
		
		if(dependencia.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
			frmRrmParametros.put("dependencia_categoria", "CARRERA");
		}else{
			frmRrmParametros.put("dependencia_categoria", "ÁREA DE CONOCIMIENTO");
		}
		
		frmRrmParametros.put("nivel", nivel.getNvlDescripcion());
		frmRrmParametros.put("gratuidad", gratuidad);
		frmRrmParametros.put("identificacion", fichaInscripcion.getPrsIdentificacion());
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO " + periodo.getPracDescripcion().replace("-", " - "));
		
		if(usuario.getUsrPersona().getPrsSectorDomicilio() != null 
				&& usuario.getUsrPersona().getPrsCallePrincipal() != null  
				&& usuario.getUsrPersona().getPrsCalleSecundaria() != null
				&& usuario.getUsrPersona().getPrsNumeroCasa() != null ){
			frmRrmParametros.put("direccion","");
		}else{
			frmRrmParametros.put("direccion", usuario.getUsrPersona().getPrsSectorDomicilio() != null ? usuario.getUsrPersona().getPrsSectorDomicilio()+", " : "" 
					+usuario.getUsrPersona().getPrsCallePrincipal() != null ?  usuario.getUsrPersona().getPrsCallePrincipal()+" Y " : "" 
					+usuario.getUsrPersona().getPrsCalleSecundaria() != null ? usuario.getUsrPersona().getPrsCalleSecundaria()+" (" : ""
					+usuario.getUsrPersona().getPrsNumeroCasa() != null ? usuario.getUsrPersona().getPrsNumeroCasa()+")." : ""
					);
		}
		frmRrmParametros.put("telefono", usuario.getUsrPersona().getPrsCelular() != null ? usuario.getUsrPersona().getPrsCelular() : "");
		
		frmRrmParametros.put("email", usuario.getUsrPersona().getPrsMailInstitucional());
		frmRrmParametros.put("emision", numeroComprobante.getCmpaFechaEmision() != null ? cambiarTimestampToString(numeroComprobante.getCmpaFechaEmision(), "yyyy-MM-dd"):"");
		frmRrmParametros.put("caducidad", numeroComprobante.getCmpaFechaCaduca() != null ? cambiarTimestampToString(numeroComprobante.getCmpaFechaCaduca(), "yyyy-MM-dd"): "");
		frmRrmParametros.put("indicacion", indicaciones.toString());
		frmRrmParametros.put("nota", nota.toString());		
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		
		StringBuilder detalleItem = new StringBuilder();
		StringBuilder horasCreditosItem = new StringBuilder();
		StringBuilder valorItem = new StringBuilder();
		StringBuilder item = new StringBuilder();
		
		List<MateriaDto> detalle = cargarDetalleMail(listMateriaDto, gratuidad);
		int contador = 0;
		BigDecimal valorTotal = BigDecimal.ZERO;
		Integer horasCreditosTotal = 0;
		
		for(MateriaDto items: detalle){
			contador=contador+1;
			item.append(contador+"\n");
			detalleItem.append(items.getMtrDescripcion()+"\n");
			horasCreditosItem.append(items.getMtrCreditos()+"\n");
			valorItem.append("$"+String.format("%.2f", items.getValorMatricula())+"\n");
			horasCreditosTotal = horasCreditosTotal+items.getMtrCreditos();
			valorTotal = valorTotal.add(items.getValorMatricula());
		}
		
		item.append("\n\n");
		detalleItem.append("\nTOTAL\n");
		horasCreditosItem.append("\n"+horasCreditosTotal+"\n");
		valorItem.append("\n$"+String.format("%.2f", valorTotal)+"\n");
		
		frmRrmParametros.put("item", item.toString());
		frmRrmParametros.put("materias", detalleItem.toString()); 
		frmRrmParametros.put("horas", horasCreditosItem.toString());
		frmRrmParametros.put("costo", valorItem.toString());	
		
		frmRrmParametros.put("codigo_qr", codigoQR(periodo.getPracDescripcion().replace("-", " - "), dependencia.getDpnDescripcion(), carrera.getCrrDescripcion(), 
				perosnaFQDN.toString(), numeroComprobante.getCmpaNumComprobante(), 
				numeroComprobante.getCmpaFechaEmision() != null ? cambiarTimestampToString(numeroComprobante.getCmpaFechaEmision(), "yyyy-MM-dd"):"", 
				numeroComprobante.getCmpaFechaCaduca() != null ? cambiarTimestampToString(numeroComprobante.getCmpaFechaCaduca(), "yyyy-MM-dd"): "", 
						nivel.getNvlDescripcion(), gratuidad, valorTotal));
		
		byte[] arreglo = null;
		
			try {
				pathGeneralReportes.append("/academico/reportes/archivosJasper/matricula/comprobante/");
				jasperReport = (JasperReport) JRLoader.loadObject(new File(pathGeneralReportes.append("/reporteOrdenCobro.jasper").toString()));
				jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
				arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
				return arreglo;
				
			} catch (JRException e) {
			}
		
			return null;
	}
	
	
	
	/**
	 * Genera byteArray de reporte para mail Registro matricula
	 */
	public static byte[] generarReporteRegistroMatriculaIntercambioMail(List<MateriaDto> listMateriaDto, Nivel nivel, PeriodoAcademico periodo, FichaInscripcionDto fichaInscripcion, Usuario usuario, Dependencia dependencia, Carrera carrera, String gratuidad){
		
		StringBuilder sbNombres = new StringBuilder(); 
		sbNombres.append(usuario.getUsrPersona().getPrsNombres());sbNombres.append(" ");
		sbNombres.append(usuario.getUsrPersona().getPrsPrimerApellido());sbNombres.append(" ");
		sbNombres.append(usuario.getUsrPersona().getPrsSegundoApellido());sbNombres.append(" ");

		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		JasperReport jasperReport = null;
		JasperPrint jasperPrint; 
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
 
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_REGISTRO_MATRICULA);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO "+ periodo.getPracDescripcion().replace("-", " - "));
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		frmRrmParametros.put("identificacion", fichaInscripcion.getPrsIdentificacion());
		frmRrmParametros.put("facultad", dependencia.getDpnDescripcion());
		frmRrmParametros.put("carrera", carrera.getCrrDetalle());
		frmRrmParametros.put("regimen", "CARRERA");
		frmRrmParametros.put("gratuidad", gratuidad);
		frmRrmParametros.put("nombres", sbNombres.toString());
		frmRrmParametros.put("nivel", nivel.getNvlDescripcion());
		frmRrmParametros.put("nick", usuario.getUsrNick());
		 
		StringBuilder sbItem = new StringBuilder();
		StringBuilder sbMaterias = new StringBuilder();
		StringBuilder sbParalelo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbMatricula = new StringBuilder();
		StringBuilder sbhoras = new StringBuilder();
		StringBuilder sbCosto = new StringBuilder();
		
		int contador = 0;
		
		for (MateriaDto item : listMateriaDto) {
			
			if(item.getMtrDescripcion().length() <= 80){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n");
				if(item.getMtrHorasPorSemana() != 0){
					sbhoras.append(item.getMtrHorasPorSemana());sbhoras.append("\n\n");
				}
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 80 && item.getMtrDescripcion().length() <= 160){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n");
				if(item.getMtrHorasPorSemana() != 0){
					sbhoras.append(item.getMtrHorasPorSemana());sbhoras.append("\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 160 && item.getMtrDescripcion().length() <= 240){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n");
				if(item.getMtrHorasPorSemana() != 0){
					sbhoras.append(item.getMtrHorasPorSemana());sbhoras.append("\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 240 && item.getMtrDescripcion().length() <= 320){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n");
				if(item.getMtrHorasPorSemana() != 0){
					sbhoras.append(item.getMtrHorasPorSemana());sbhoras.append("\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 320 && item.getMtrDescripcion().length() <= 400){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n");
				if(item.getMtrHorasPorSemana() != 0){
					sbhoras.append(item.getMtrHorasPorSemana());sbhoras.append("\n\n\n\n\n\n");
				} 
				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n\n\n");
				
			}
			
			if(item.getMtrDescripcion().length() > 400 && item.getMtrDescripcion().length() <= 480){
				sbItem.append(String.valueOf(contador += 1));sbItem.append("\n\n\n\n\n\n\n");
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n\n\n\n\n\n");
				if(item.getNumMatricula()==1){
					sbMatricula.append(DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==2){
					sbMatricula.append(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getNumMatricula()==3){
					sbMatricula.append(DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL);sbMatricula.append("\n\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n\n");
				if(item.getMtrHorasPorSemana() != 0){
					sbhoras.append(item.getMtrHorasPorSemana());sbhoras.append("\n\n\n\n\n\n\n");
				} 

				sbCosto.append("$"+String.format("%.2f", item.getValorMatricula()));sbCosto.append("\n\n\n\n\n\n\n");
				
			}


		}
		
		frmRrmParametros.put("materias", sbMaterias.toString());
		frmRrmParametros.put("paralelo", sbParalelo.toString());
		frmRrmParametros.put("codigo", sbCodigo.toString());
		frmRrmParametros.put("item", sbItem.toString());
		frmRrmParametros.put("matricula", sbMatricula.toString()); 
		frmRrmParametros.put("horas", sbhoras.toString());
		frmRrmParametros.put("costo", sbCosto.toString());	
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
		
		byte[] arreglo = null;
		
			try {
				pathGeneralReportes.append("/academico/reportes/archivosJasper/matricula/registro/");
				jasperReport = (JasperReport) JRLoader.loadObject(new File(pathGeneralReportes.append("/reporteRegistroMatricula.jasper").toString()));
				jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
				arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
				return arreglo;
				
			} catch (JRException e) {
			}
		
			return null;
	}
	
	//------------------------------------------------------CARGA DE DATOS-----------------------------------------------------------------
	
	
	
	/**
	 * Cabecera de la columna que varia entre creditos y horas 
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	private static String cabeceraCreditosHoras(Integer creditos, Integer horas){
		
		if(horas != null && horas != 0){
			return "Horas";
		}
		if(creditos != null && creditos != 0){
			return "Créditos";
		}
		return "Error en el regimen.";
	}
	
	/**
	 * Carga detalle del reporte orden de cobro 
	 * @param lista
	 * @param gratuidad
	 * @return
	 */
	private static List<MateriaDto> cargarDetalle(List<EstudianteJdbcDto> lista, String gratuidad){
		List<MateriaDto> retorno = null;

		BigDecimal primeraValor = BigDecimal.ZERO;
		Integer primeraCreditosHoras = 0;
		BigDecimal segundaValor = BigDecimal.ZERO;
		Integer segundaCreditosHoras = 0;
		BigDecimal terceraValor = BigDecimal.ZERO;
		Integer terceraCreditosHoras = 0;
		
		for(EstudianteJdbcDto item : lista){
			if(item.getDtmtNumero() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
				primeraValor = primeraValor.add(item.getDtmtValorPorMateria() != null? item.getDtmtValorPorMateria(): BigDecimal.ZERO);
				if(item.getMtrCreditos() != 0 ){
					primeraCreditosHoras = primeraCreditosHoras+item.getMtrCreditos();
				}
				if(item.getMtrHorasCien() != 0){
					primeraCreditosHoras = primeraCreditosHoras+item.getMtrHorasCien();
				}
			}
			if(item.getDtmtNumero() == SAUConstantes.SEGUNDA_MATRICULA_VALUE){
				segundaValor = segundaValor.add(item.getDtmtValorPorMateria() != null? item.getDtmtValorPorMateria(): BigDecimal.ZERO);
				if(item.getMtrCreditos() != 0 ){
					segundaCreditosHoras = segundaCreditosHoras+item.getMtrCreditos();
				}
				if(item.getMtrHorasCien() != 0){
					segundaCreditosHoras = segundaCreditosHoras+item.getMtrHorasCien();
				}
			}
			if(item.getDtmtNumero() == SAUConstantes.TERCERA_MATRICULA_VALUE){
				terceraValor = terceraValor.add(item.getDtmtValorPorMateria() != null? item.getDtmtValorPorMateria(): BigDecimal.ZERO);
				if(item.getMtrCreditos() != 0 ){
					terceraCreditosHoras = terceraCreditosHoras+item.getMtrCreditos();
				}
				if(item.getMtrHorasCien() != 0){
					terceraCreditosHoras = terceraCreditosHoras+item.getMtrHorasCien();
				}
			}
		}
		
		retorno = new ArrayList<>();
		
		if(primeraValor != BigDecimal.ZERO || primeraCreditosHoras != 0){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion("PRIMERA MATRICULA");
			detalle.setValorMatricula(primeraValor);
			detalle.setMtrCreditos(primeraCreditosHoras);
			retorno.add(detalle);
		}
		if(segundaValor != BigDecimal.ZERO || segundaCreditosHoras != 0){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion("SEGUNDA MATRICULA");
			detalle.setValorMatricula(segundaValor);
			detalle.setMtrCreditos(segundaCreditosHoras);
			retorno.add(detalle);
		}
		if(terceraValor != BigDecimal.ZERO || terceraCreditosHoras != 0){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion("TERCERA MATRICULA");
			detalle.setValorMatricula(terceraValor);
			detalle.setMtrCreditos(terceraCreditosHoras);
			retorno.add(detalle);
		}
		
//		if(primeraValor == BigDecimal.ZERO && segundaValor == BigDecimal.ZERO && terceraValor == BigDecimal.ZERO){
//			MateriaDto detalle = new MateriaDto();
//			detalle.setMtrDescripcion(gratuidad);
//			detalle.setValorMatricula(BigDecimal.ZERO);
//			detalle.setMtrCreditos(0);
//			retorno.add(detalle);
//		}
		
		return retorno;
	}
	
	/**
	  * Carga detalle del reporte orden de cobro 
	  * @param lista
	  * @param gratuidad
	  * @return
	  */
	private static List<MateriaDto> cargarDetalleMail(List<MateriaDto> lista, String gratuidad){
		List<MateriaDto> retorno = null;
		
		BigDecimal primeraValor = BigDecimal.ZERO;
		Integer primeraCreditosHoras = 0;
		BigDecimal segundaValor = BigDecimal.ZERO;
		Integer segundaCreditosHoras = 0;
		BigDecimal terceraValor = BigDecimal.ZERO;
		Integer terceraCreditosHoras = 0;
		
		for(MateriaDto item : lista){
			if(item.getNumMatricula() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
				primeraValor = primeraValor.add(item.getValorMatricula());
				if(item.getMtrCreditos() != 0 ){
					primeraCreditosHoras = primeraCreditosHoras+item.getMtrCreditos();
				}
				if(item.getMtrHorasCien() != 0){
					primeraCreditosHoras = primeraCreditosHoras+item.getMtrHorasCien();
				}
			}
			if(item.getNumMatricula() == SAUConstantes.SEGUNDA_MATRICULA_VALUE){
				segundaValor = segundaValor.add(item.getValorMatricula());
				if(item.getMtrCreditos() != 0 ){
					segundaCreditosHoras = segundaCreditosHoras+item.getMtrCreditos();
				}
				if(item.getMtrHorasCien() != 0){
					segundaCreditosHoras = segundaCreditosHoras+item.getMtrHorasCien();
				}
			}
			if(item.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE){
				terceraValor = terceraValor.add(item.getValorMatricula());
				if(item.getMtrCreditos() != 0 ){
					terceraCreditosHoras = terceraCreditosHoras+item.getMtrCreditos();
				}
				if(item.getMtrHorasCien() != 0){
					terceraCreditosHoras = terceraCreditosHoras+item.getMtrHorasCien();
				}
			}
		}
		
		retorno = new ArrayList<>();
		
		if(primeraValor != BigDecimal.ZERO){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion("PRIMERA MATRICULA");
			detalle.setValorMatricula(primeraValor);
			detalle.setMtrCreditos(primeraCreditosHoras);
			retorno.add(detalle);
		}
		if(segundaValor != BigDecimal.ZERO){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion("SEGUNA MATRICULA");
			detalle.setValorMatricula(segundaValor);
			detalle.setMtrCreditos(segundaCreditosHoras);
			retorno.add(detalle);
		}
		if(terceraValor != BigDecimal.ZERO){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion("TERCERA MATRICULA");
			detalle.setValorMatricula(terceraValor);
			detalle.setMtrCreditos(terceraCreditosHoras);
			retorno.add(detalle);
		}
		
		if(primeraValor == BigDecimal.ZERO && segundaValor == BigDecimal.ZERO && terceraValor == BigDecimal.ZERO){
			MateriaDto detalle = new MateriaDto();
			detalle.setMtrDescripcion(gratuidad);
			detalle.setValorMatricula(BigDecimal.ZERO);
			detalle.setMtrCreditos(0);
			retorno.add(detalle);
		}
		
		return retorno;
	}
	
	/**
	 * Crea texto de qr
	 * @param periodo
	 * @param facultad
	 * @param carrera
	 * @param nombres
	 * @param numComprobante
	 * @param fechaEmision
	 * @param fechaCaduca
	 * @param semestre
	 * @param gratuidad
	 * @param valorTotal
	 * @return texto qr
	 */
	public static String codigoQR(String periodo, String facultad, String carrera, String nombres,
			String numComprobante, String fechaEmision, String fechaCaduca, String semestre,
			String gratuidad, BigDecimal valorTotal){
		
		String numComp = numComprobante != null ? "No. Comprobante: "+numComprobante+"\n":"\n";
		String valor = String.format("%.2f", valorTotal);
		return "UNIVERSIDAD CENTRAL DEL ECUADOR \n"+
		periodo+"\n"+
		facultad+"\n"+
		carrera+"\n"+
		nombres+"\n"+
		numComp
		+"Emisión: "+fechaEmision+"\n"+
		"Caducidad: "+fechaCaduca+"\n"+
		"Semestre: "+semestre+"\n"+
		"Gratuidad: "+gratuidad+"\n"+
		"Valor a Pagar: $"+valor+"\n"+
		"http://siiu.uce.edu.ec";
	}
	
	
	//------------------------------------------------------METODOS GENERALES-----------------------------------------------------------------
	
	/**
	 * Método que permite convertir un objeto java.util.Date a un String, el formato de salida es el parametro que se ingrese.
	 * @author FREDDY - fgguzman 
	 * @param date - java.util.Date
	 * @param formato - "Ejm. dd/MM/yyyy HH:mm:ss"
	 * @return cadena de texto con fecha en formato solicitado.
	 */
	public static String cambiarDateToStringFormatoFecha(Date date, String formato) {
		SimpleDateFormat formateador = new SimpleDateFormat(formato, new Locale("es", "EC"));
	return formateador.format(date);
	}
	
	/**
	 * Método que permite convertir a un String - formato "Quito, 10 de Enero de 2018" tomando una
	 * fecha tipo java.Util.Date.
	 * @author FREDDY - fgguzman
	 * @param java.Util.Date - parametro de entrada.
	 * @return String - representa la fecha en formato "10 de Enero de 2018".
	 */
	public static String cambiarDateToStringFormatoCaberaDocumento(Date date) {
		DateFormat formateador = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("es", "EC"));
		return formateador.format(date);
	}
	

	/**
	 * Transforma las fechas de timestamp string con formato
	 * @param tmFecha - fecha como timestamp
	 * @return Fecha como date trasformado a estring con formato
	 * @throws ParseException
	 */
	public static String cambiarTimestampToString(Timestamp tmFecha, String parametro) {
		SimpleDateFormat sdf = new SimpleDateFormat(parametro);
		Date fecha;
		try {
			if(tmFecha != null){
				fecha = sdf.parse(tmFecha.toString());
				return sdf.format(fecha);
			}
		} catch (ParseException e) {
			return "Error al calcular la fecha.";
		}
		return null;
	}
	
	/**
	 * Método que permite devolver una fecha en formato java.util.Date.
	 * @author FREDDY - fgguzman
	 * @param fecha - fecha inicio.
	 * @param dias - cantidad de dias adicionales
	 * @return fecha + nDias
	 */
	public static java.util.Date getFechaIncrementada(java.util.Date fecha, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        cal.add(Calendar.DATE, dias);
        return new java.util.Date(cal.getTimeInMillis());
    }
	
	
}
