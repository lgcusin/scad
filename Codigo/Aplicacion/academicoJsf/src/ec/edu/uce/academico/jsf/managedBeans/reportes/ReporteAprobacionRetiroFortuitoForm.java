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
   
 ARCHIVO:     ReporteAprobacionRetiroFortuitoForm.java	  
 DESCRIPCION: Bean de sesion que maneja el reporte  de aprobación/negación de la solicitud de retiro por situaciones fortuitas realizada por el Secretario Abogado de la Facultad..
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 7-01-2019			 Marcelo Quishpe                         Emisión Inicial
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
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteAprobacionRetiroFortuitoForm. 
 * Bean de sesion que maneja el reporte de aprobacion/negación de la solicitud de retiro por situaciones fortuitas realizada por el Secretario Abogado de la Facultad.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "reporteAprobacionRetiroFortuitoForm")
@SessionScoped
public class ReporteAprobacionRetiroFortuitoForm implements Serializable {

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
	
	public static void generarReporteAprobacionRetiroFortuito(List<EstudianteJdbcDto> listaMaterias, Carrera crr, Persona prs, PeriodoAcademico prac, Usuario usr, String observaciones){
		
		
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		List<Causal> listaCausalaux = new ArrayList<>();
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		//frmRrmNombreReporte = "solicitudResuelta";
		frmRrmNombreReporte=prs.getPrsNombres()+" "+prs.getPrsPrimerApellido().toUpperCase()+" "
				+(prs.getPrsSegundoApellido() == null?" ":prs.getPrsSegundoApellido());
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		
//		if(crr!=null){
//		frmRrmParametros.put("facultad", crr.getCrrDependencia().getDpnDescripcion());
//		}else{
//			frmRrmParametros.put("facultad", " ");
//		}
//		
//		if(crr!=null){
//			frmRrmParametros.put("carrera", crr.getCrrDescripcion());
//			}else{
//				frmRrmParametros.put("carrera", " ");
//			}
		String nombres= " ";
		
		if(prs!=null){
	       nombres=prs.getPrsNombres()+" "+prs.getPrsPrimerApellido().toUpperCase()+" "
				+(prs.getPrsSegundoApellido() == null?" ":prs.getPrsSegundoApellido());
		}
			
		frmRrmParametros.put("nombre",nombres );
		
		if(prs.getPrsIdentificacion()!=null){
			frmRrmParametros.put("identificacion", prs.getPrsIdentificacion());
			}else{
				frmRrmParametros.put("identificacion", " ");	
			}
		
		StringBuilder sbTextoInicial = new StringBuilder();
		sbTextoInicial.append("Señor(a)(ita) ");sbTextoInicial.append("\n");
		sbTextoInicial.append(nombres);sbTextoInicial.append("\n");
		sbTextoInicial.append("ESTUDIANTE DE LA CARRERA DE ");
		if(crr.getCrrDescripcion()!=null){
		sbTextoInicial.append(crr.getCrrDescripcion());sbTextoInicial.append("\n");
		}else{
			sbTextoInicial.append(" ");sbTextoInicial.append("\n");
		}
		
		sbTextoInicial.append("FACULTAD DE ");
		if(crr!=null){
			sbTextoInicial.append(crr.getCrrDependencia().getDpnDescripcion());sbTextoInicial.append("\n");
		}
		
		sbTextoInicial.append("Presente.-"); sbTextoInicial.append("\n\n");
		frmRrmParametros.put("textoInicial", sbTextoInicial.toString());			
		
		StringBuilder sbTexto = new StringBuilder();
	    sbTexto.append("Una vez analizada(s) y validada(s) la(s) solicitud(es) de retiro de asignatura(s) por situaciones fortuitas o de fuerza mayor y considerando la normativa vigente, "
				+ "a continuación se detalla el informe final de acuerdo a la resolución del Consejo Directivo de la Facultad:");
		frmRrmParametros.put("texto", sbTexto.toString());
		
		StringBuilder sbPeriodo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbAsignatura = new StringBuilder();
		StringBuilder sbHora = new StringBuilder();
		StringBuilder sbCausal = new StringBuilder();
		StringBuilder sbEvidencia = new StringBuilder();
		StringBuilder sbEstado = new StringBuilder();
		
		for (EstudianteJdbcDto item : listaMaterias) {
			if(item.getMtrDescripcion().length() <= 44){
				
				
				if(item.getPracDescripcion()!=null){
					sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n");
				}else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				}else{
					sbCodigo.append(" ");sbCodigo.append("\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
								
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n");
				}else{
					sbCausal.append(" ");sbCausal.append("\n\n");
				}
				
				if(item.getDtmtArchivoEstudiantes()!=null){
					sbEvidencia.append(item.getDtmtArchivoEstudiantes());sbEvidencia.append("\n\n");
				}else{
					sbEvidencia.append(" ");sbEvidencia.append("\n\n");
				}
				
				
				if(item.getEstadoSolicitudRetiro()!=null){
				     if(item.getEstadoSolicitudRetiro()==0){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);sbEstado.append("\n\n");
				    }
				    if(item.getEstadoSolicitudRetiro()==1){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);sbEstado.append("\n\n");
				    }
				
				}else{
					sbEstado.append(" ");sbEstado.append("\n\n");
				}
				
				
				
			}
			if(item.getMtrDescripcion().length() > 44 && item.getMtrDescripcion().length() <= 88){
				
				if(item.getPracDescripcion()!=null){
					sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n");
				}else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n");
				}
				
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				}else{
					sbCodigo.append(" ");sbCodigo.append("\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
								
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n");
				}else{
					sbCausal.append(" ");sbCausal.append("\n\n\n");
				}
				
				if(item.getDtmtArchivoEstudiantes()!=null){
					sbEvidencia.append(item.getDtmtArchivoEstudiantes());sbEvidencia.append("\n\n\n");
				}else{
					sbEvidencia.append(" ");sbEvidencia.append("\n\n\n");
				}
				
				
				
				if(item.getEstadoSolicitudRetiro()!=null){
				     if(item.getEstadoSolicitudRetiro()==0){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);sbEstado.append("\n\n\n");
				    }
				    if(item.getEstadoSolicitudRetiro()==1){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);sbEstado.append("\n\n\n");
				    }
				
				}else{
					sbEstado.append(" ");sbEstado.append("\n\n\n");
				}
				
				
			}
			if(item.getMtrDescripcion().length() > 88 && item.getMtrDescripcion().length() <= 132){
			
				if(item.getPracDescripcion()!=null){
					sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n");
				}else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n");
				}
				
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				}else{
					sbCodigo.append(" ");sbCodigo.append("\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
				
			
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n");
				}else{
					sbCausal.append(" ");sbCausal.append("\n\n\n\n");
				}
				
				if(item.getDtmtArchivoEstudiantes()!=null){
					sbEvidencia.append(item.getDtmtArchivoEstudiantes());sbEvidencia.append("\n\n\n\n");
				}else{
					sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n");
				}
				
				
				
				if(item.getEstadoSolicitudRetiro()!=null){
				     if(item.getEstadoSolicitudRetiro()==0){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);sbEstado.append("\n\n\n\n");
				    }
				    if(item.getEstadoSolicitudRetiro()==1){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);sbEstado.append("\n\n\n\n");
				    }
				
				}else{
					sbEstado.append(" ");sbEstado.append("\n\n\n\n");
				}
				
			}
			if(item.getMtrDescripcion().length() > 132 && item.getMtrDescripcion().length() <= 176){
				
				if(item.getPracDescripcion()!=null){
					sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n");
				}else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n");
				}
				
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				}else{
					sbCodigo.append(" ");sbCodigo.append("\n\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
				
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n\n");
				}else{
					sbCausal.append(" ");sbCausal.append("\n\n\n\n\n");
				}
				
				if(item.getDtmtArchivoEstudiantes()!=null){
					sbEvidencia.append(item.getDtmtArchivoEstudiantes());sbEvidencia.append("\n\n\n\n\n");
				}else{
					sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n\n");
				}
				
				
				if(item.getEstadoSolicitudRetiro()!=null){
				     if(item.getEstadoSolicitudRetiro()==0){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);sbEstado.append("\n\n\n\n\n");
				    }
				    if(item.getEstadoSolicitudRetiro()==1){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);sbEstado.append("\n\n\n\n\n");
				    }
				
				}else{
					sbEstado.append(" ");sbEstado.append("\n\n");
				}
				
			}
			if(item.getMtrDescripcion().length() > 176 && item.getMtrDescripcion().length() <= 220){
				
				if(item.getPracDescripcion()!=null){
					sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n");
				}else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				}else{
					sbCodigo.append(" ");sbCodigo.append("\n\n\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
				
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n\n\n");
				}else{
					sbCausal.append(" ");sbCausal.append("\n\n\n\n\n\n");
				}
				
				if(item.getDtmtArchivoEstudiantes()!=null){
					sbEvidencia.append(item.getDtmtArchivoEstudiantes());sbEvidencia.append("\n\n\n\n\n\n");
				}else{
					sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n\n\n");
				}
				
				
				
				if(item.getEstadoSolicitudRetiro()!=null){
				     if(item.getEstadoSolicitudRetiro()==0){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);sbEstado.append("\n\n\n\n\n\n");
				    }
				    if(item.getEstadoSolicitudRetiro()==1){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);sbEstado.append("\n\n\n\n\n\n");
				    }
				
				}else{
					sbEstado.append(" ");sbEstado.append("\n\n\n\n\n\n");
				}
				
			}
			if(item.getMtrDescripcion().length() > 220 && item.getMtrDescripcion().length() <= 264){
				
				if(item.getPracDescripcion()!=null){
					sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n\n");
				}else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n\n");
				}
				
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				}else{
					sbCodigo.append(" ");sbCodigo.append("\n\n\n\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
				
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n\n\n\n");
				}else{
					sbCausal.append(" ");sbCausal.append("\n\n\n\n\n\n\n");
				}
				
				if(item.getDtmtArchivoEstudiantes()!=null){
					sbEvidencia.append(item.getDtmtArchivoEstudiantes());sbEvidencia.append("\n\n\n\n\n\n\n");
				}else{
					sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n\n\n\n");
				}
				
				
				
				if(item.getEstadoSolicitudRetiro()!=null){
				     if(item.getEstadoSolicitudRetiro()==0){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);sbEstado.append("\n\n\n\n\n\n\n");
				    }
				    if(item.getEstadoSolicitudRetiro()==1){
					sbEstado.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);sbEstado.append("\n\n\n\n\n\n\n");
				    }
				
				}else{
					sbEstado.append(" ");sbEstado.append("\n\n\n\n\n\n\n");
				}
				
			}
			frmRrmParametros.put("periodo", sbPeriodo.toString());
			frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
			frmRrmParametros.put("asignatura", sbAsignatura.toString());
			frmRrmParametros.put("numero", sbHora.toString());
			frmRrmParametros.put("causal", sbCausal.toString());
			frmRrmParametros.put("evidencia", sbEvidencia.toString());
			frmRrmParametros.put("solicitudEstado", sbEstado.toString());
			
			String secreAbogado= null;
			
			if(usr!=null){
				secreAbogado = usr.getUsrPersona().getPrsNombres()+" "+usr.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
					+(usr.getUsrPersona().getPrsSegundoApellido() == null?" ":usr.getUsrPersona().getPrsSegundoApellido());
			}else{
				secreAbogado=" ";
			
			}
			frmRrmParametros.put("secreAbogado",secreAbogado);
			
			
			StringBuilder sbObservaciones = new StringBuilder();
			if(observaciones!=null){
				sbObservaciones.append("Observaciones: ");sbObservaciones.append("\n");
				sbObservaciones.append(observaciones);
				}else{
					sbObservaciones.append("Observaciones: ");sbObservaciones.append("\n");
					
				}
				frmRrmParametros.put("observaciones", sbObservaciones.toString());
			
			if( usr.getUsrNick()!=null){
				frmRrmParametros.put("nick", usr.getUsrNick());
				}else{
					frmRrmParametros.put("nick", " ");
				}
			
			Boolean encontrado = false;
			Causal objetoCausal= new Causal();
			for (Causal causal : listaCausalaux) {
				if(causal.getCslCodigo().equals(item.getCslCodigo())){
					encontrado = true;
					break;
				}
			}
			
			if(encontrado ==false){
				objetoCausal.setCslCodigo(item.getCslCodigo());
				objetoCausal.setCslDescripcion(item.getCslDescripcion());
				listaCausalaux.add(objetoCausal);
			}
			
		}
		StringBuilder sbCslCodigo= new StringBuilder();
		StringBuilder sbCslDescripcion= new StringBuilder();
		for (Causal causal2 : listaCausalaux) {
		
			if(causal2.getCslCodigo()!=null){
		    	sbCslCodigo.append(causal2.getCslCodigo());sbCslCodigo.append("\n");	
		    }else{
				sbCslCodigo.append(" ");sbCslCodigo.append("\n");
		    }
			
			if(causal2.getCslDescripcion()!=null){
				sbCslDescripcion.append(causal2.getCslDescripcion());sbCslDescripcion.append("\n");
			}else{
				sbCslDescripcion.append(" ");sbCslDescripcion.append("\n");
			}
			
			frmRrmParametros.put("cslCodigo", sbCslCodigo.toString());
			frmRrmParametros.put("cslDescripcion", sbCslDescripcion.toString());
		}
		
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteAprobacionRetiroFortuito");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoTercera = null;
		datoTercera = new HashMap<String, Object>();
		frmRrmCampos.add(datoTercera);
				
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
