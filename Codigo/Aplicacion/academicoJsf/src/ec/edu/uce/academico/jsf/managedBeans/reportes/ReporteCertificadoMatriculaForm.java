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
   
 ARCHIVO:     ReporteCertificadoMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja el certificado de matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 28-06-2017			 Vinicio Rosales                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import static ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades.cambiarDateToStringFormatoCaberaDocumento;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;

/**
 * Clase (session bean) ReporteCertificadoMatriculaForm. 
 * Bean de sesion que maneja el certificado de matricula del estudiante.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name = "registroCertificadoMatriculaForm")
@SessionScoped
public class ReporteCertificadoMatriculaForm extends ReporteTemplateForm implements Serializable {
	
	private static final long serialVersionUID = 903755549202583562L;

	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	public static void generarReporteCertificadoMatricula(List<EstudianteJdbcDto> listaRegistroMatricula, String nick, EstudianteJdbcDto estudiante, Carrera crr, PersonaDto prs){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteCertificadoMatricula_"+estudiante.getPrsPrimerApellido();
		frmRrmParametros = new HashMap<String, Object>();
		StringBuilder sbNombres = new StringBuilder();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
		SimpleDateFormat formatoFe = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		
		if(prs != null){
			if(prs.getPrsPrimerApellido() != null){
				frmRrmParametros.put("nombre_secretario", prs.getPrsNombres()+" "+prs.getPrsPrimerApellido()+" "+prs.getPrsSegundoApellido());
			}else{
				frmRrmParametros.put("nombre_secretario", prs.getPrsNombres()+" "+prs.getPrsSegundoApellido());
			}
			
		}else{
			frmRrmParametros.put("nombre_secretario", " ");
		}
		
		
		
//		
//		for (EstudianteJdbcDto item : listaRegistroMatricula) {
////			frmRrmParametros.put("periodo", item.getPracDescripcion());
////			sbNombres.append(item.getPrsPrimerApellido());sbNombres.append(" ");
////			sbNombres.append(item.getPrsSegundoApellido());sbNombres.append(" ");
////			sbNombres.append(item.getPrsNombres());sbNombres.append(" ");
////			frmRrmParametros.put("primer_apellido", item.getPrsPrimerApellido());
////			frmRrmParametros.put("segundo_apellido", item.getPrsSegundoApellido());
////			frmRrmParametros.put("nombres", item.getPrsNombres());
////			frmRrmParametros.put("identificacion", item.getPrsIdentificacion());
//			frmRrmParametros.put("facultad", item.getDpnDescripcion());
//			frmRrmParametros.put("carrera", item.getCrrDetalle());
////			frmRrmParametros.put("curso", "1");
//			frmRrmParametros.put("curso", item.getNvlDescripcion());
//			break;
//		}
		StringBuilder sbTexto = new StringBuilder();
		StringBuilder sbFacultad = new StringBuilder();
		StringBuilder sbCarrera = new StringBuilder();
		for (EstudianteJdbcDto item : listaRegistroMatricula) {
			String fecha1 = formatoFe.format(item.getFcmtFechaMatricula());
			sbTexto.append("El(La) Sr.(ta) ");
			sbTexto.append(estudiante.getPrsNombres());sbTexto.append(" ");
			sbTexto.append(estudiante.getPrsPrimerApellido());sbTexto.append(" ");sbTexto.append(estudiante.getPrsSegundoApellido());sbTexto.append(" ");
			sbTexto.append("con identificación No. ");sbTexto.append(estudiante.getPrsIdentificacion());sbTexto.append(" ");sbTexto.append("se matriculó en ");
			
//			if(item.getNvlId() == 1){
//				String ext = item.getNvlDescripcion().substring(0,item.getNvlDescripcion().length()-1);
//				sbTexto.append(ext);
//			}else{
//				sbTexto.append(item.getFcmtNivelUbicacion());
//			}
			
			ListasCombosForm listas = new ListasCombosForm();
			String nivelSt = null;
			nivelSt = listas.getListaNivelFichaMatricula(item.getFcmtNivelUbicacion());
			if(item.getFcmtNivelUbicacion() == 1 || item.getFcmtNivelUbicacion() == 3){
				String ext = nivelSt.substring(0,nivelSt.length()-1);
				sbTexto.append(ext);
			}else{
				sbTexto.append(nivelSt);
			}
			
			if(item.getNvlId()!= 11){
				sbTexto.append(" NIVEL ");	
			}else{
				sbTexto.append(" ");
			}
			sbTexto.append("de la carrera de ");sbTexto.append(item.getCrrDescripcion());
			sbTexto.append(", período académico ");sbTexto.append(item.getPracDescripcion()); sbTexto.append(" con fecha ");sbTexto.append(fecha1);
			sbTexto.append(", con matrícula No. ");sbTexto.append(item.getFcmtId());sbTexto.append(" en el módulo Académico del Sistema Integral de Información Universitaria - SIIU en las siguientes asignaturas:");
			sbTexto.append("\n\n");
			
			
			break;
		}
		
		sbFacultad.append(" FACULTAD DE ");sbFacultad.append(estudiante.getDpnDescripcion());
		sbCarrera.append("CARRERA DE ");sbCarrera.append(estudiante.getCrrDescripcion());
		frmRrmParametros.put("texto", sbTexto.toString());
		frmRrmParametros.put("facultad", sbFacultad.toString());
		frmRrmParametros.put("carrera", sbCarrera.toString());
		frmRrmParametros.put("nombres", sbNombres.toString());
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
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n");
				}else{
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n");
				}
				sbCosto.append("0.00");sbCosto.append("\n\n");
			}
			if(item.getMtrDescripcion().length() > 80 && item.getMtrDescripcion().length() <= 160){
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
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n");
					
				}else{
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n");
				}
				sbCosto.append("0.00");sbCosto.append("\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 160 && item.getMtrDescripcion().length() <= 240){
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
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n");
				}else{
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n");
				}
				sbCosto.append("0.00");sbCosto.append("\n\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 240 && item.getMtrDescripcion().length() <= 320){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
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
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n");
				}else{
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n");
				}
				sbCosto.append("0.00");sbCosto.append("\n\n\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 320 && item.getMtrDescripcion().length() <= 400){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
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
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n\n");
				}else{
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n");
				}
				sbCosto.append("0.00");sbCosto.append("\n\n\n\n\n\n");
			}
			if(item.getMtrDescripcion().length() > 400 && item.getMtrDescripcion().length() <= 480){
				sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				sbMaterias.append(item.getMtrDescripcion());sbMaterias.append("\n\n");
				if(item.getDtmtNumero()==1){
					sbMatricula.append("1");sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==2){
					sbMatricula.append("2");sbMatricula.append("\n\n\n\n\n\n\n");
				}
				if(item.getDtmtNumero()==3){
					sbMatricula.append("3");sbMatricula.append("\n\n\n\n\n\n\n");
				}
				sbParalelo.append(item.getPrlDescripcion());sbParalelo.append("\n\n\n\n\n\n\n");
				if(item.getMtrHoras() != 0){
					sbhoras.append(item.getMtrHoras());sbhoras.append("\n\n\n\n\n\n\n");
				}else{
					sbhoras.append(item.getMtrCreditos());sbhoras.append("\n\n\n\n\n\n\n");
				}
				sbCosto.append("0.00");sbCosto.append("\n\n\n\n\n\n\n");
			}
			
			
		}
		for (EstudianteJdbcDto item : listaRegistroMatricula) {
			if(item.getMtrHoras() != 0){
				frmRrmParametros.put("etiqueta", "Horas");
			}else{
				frmRrmParametros.put("etiqueta", "Créditos");
			}
			break;
		}
		frmRrmParametros.put("materias", sbMaterias.toString());
		frmRrmParametros.put("paralelo", sbParalelo.toString());
		frmRrmParametros.put("codigo", sbCodigo.toString());
		frmRrmParametros.put("matricula", sbMatricula.toString());
		frmRrmParametros.put("horas", sbhoras.toString());
//		frmRrmParametros.put("costo", sbCosto.toString());
		frmRrmParametros.put("nick", nick);
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteCertificadoMatricula");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraCertificado.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieCertificado.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoMatriculas = null;
		datoMatriculas = new HashMap<String, Object>();
		frmRrmCampos.add(datoMatriculas);
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

	public static void generarReporteCretificadoMatricula(Usuario usuario, EstudianteJdbcDto estudiante, FichaMatriculaDto matricula, CarreraDto facultad, PersonaDto  secretario, List<RecordEstudianteDto> asignaturas){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CERTIFICADO DE MATRÍCULA";
		
		StringBuilder contenido = new StringBuilder();
		
		/**
		contenido.append(setearGenero(estudiante, 3)+ estudiante.getPrsNombres() +" "+ estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido() + ",");
		contenido.append(" con identificación N°." + estudiante.getPrsIdentificacion()+" se matriculó en el nivel " + matricula.getNvlDescripcion()+ " de la carrera de " + facultad.getCrrDescripcion()+ " facultad de ");
		contenido.append(facultad.getDpnDescripcion()+ ", modalidad " + matricula.getFcmtModalidadLabel() + "; período académico "+ matricula.getPracDescripcion() +", ");
		contenido.append(" matrícula asignada N°. "+matricula.getFcmtId() + " registrada en el Sistema Académico de la Universidad Central del Ecuador.");
		*/
		contenido.append(setearGenero(estudiante, 3)+ estudiante.getPrsPrimerApellido() + " "+ estudiante.getPrsSegundoApellido()+ " " +estudiante.getPrsNombres());
		contenido.append(" con identificación N°. " + estudiante.getPrsIdentificacion()+" se matriculó en el Sistema Académico de la Institución, en " + matricula.getNvlDescripcion() + " de la Carrera de " + facultad.getCrrDescripcion());
		contenido.append(", período académico "+matricula.getPracDescripcion());
		contenido.append(", con matrícula N°. ");contenido.append(matricula.getFcmtId());contenido.append(", en las siguientes asignaturas:");
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		frmRrmParametros.put("fecha", "Quito, "+cambiarDateToStringFormatoCaberaDocumento(GeneralesUtilidades.getFechaActualSistema()));
		frmRrmParametros.put("facultad", "FACULTAD DE "+facultad.getDpnDescripcion());
		frmRrmParametros.put("carrera", "CARRERA DE "+facultad.getCrrDescripcion());
		
//		if (facultad.getDpnId() == DependenciaConstantes.DPN_MEDICINA_VETERINARIA_ZOOTECNIA_VALUE) {
//			frmRrmParametros.put("facultad","FACULTAD DE "+facultad.getDpnDescripcion());
//			frmRrmParametros.put("carrera","CARRERA DE "+facultad.getCrrDescripcion());
//		}
		
		frmRrmParametros.put("certificado", encabezadoReporte);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		

		frmRrmParametros.put("contenido",contenido.toString());
		
		if (secretario != null) {
			frmRrmParametros.put("cargo_value", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
			frmRrmParametros.put("cargo_label", setearGenero(secretario, 2));
		}else {
			frmRrmParametros.put("cargo_value", "Registre al Secretario de su Dependencia");
			frmRrmParametros.put("cargo_label", "");
		}
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : asignaturas) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("cod_materia",item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("num_matricula", String.valueOf(item.getDtmtNumMatricula()));
			datoHorario.put("horas",  String.valueOf(item.getRcesMateriaDto().getMtrHorasPorSemana()));
			datoHorario.put("paralelo", item.getRcesFichaMatriculaDto().getPrlCodigo());
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
