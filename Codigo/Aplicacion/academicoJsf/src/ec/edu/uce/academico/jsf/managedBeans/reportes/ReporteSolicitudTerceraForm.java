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
   
 ARCHIVO:     ReporteSolicitudTerceraForm.java	  
 DESCRIPCION: Bean de sesion que maneja el reporte de la solicitud de tercera matrícula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-03-2018			 Marcelo Quishpe                         Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteSolicitudTerceraForm. 
 * Bean de sesion que maneja aneja el reporte de la solicitud de tercera matrícula del estudiante..
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "reporteSolicitudTerceraForm")
@SessionScoped
public class ReporteSolicitudTerceraForm implements Serializable {

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
	
	public static void generarReporteSolicitudTercera(List<MateriaDto> listaMaterias, Dependencia dpn, Carrera crr, Persona prs, PeriodoAcademico prac, Usuario usr, PersonaDto directorCarrera){
		
	try{	
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		List<Causal> listaCausalaux = new ArrayList<>();
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "solicitudTerceraMatricula";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		//frmRrmParametros.put("facultad", dpn.getDpnDescripcion());
		
//		if(crr.getCrrDescripcion()!=null){
//			frmRrmParametros.put("carrera", crr.getCrrDescripcion());
//			}else{
//				frmRrmParametros.put("carrera", " ");	
//			}
		
		String nombres= null;
		if(prs!=null){
		nombres=prs.getPrsNombres()+" "+prs.getPrsPrimerApellido().toUpperCase()+" "
				+(prs.getPrsSegundoApellido() == null?" ":prs.getPrsSegundoApellido());
		}else{
			nombres=" ";
		}
		
		//	frmRrmParametros.put("nombre",nombres );
				
//		if(prs.getPrsIdentificacion()!=null){
//			frmRrmParametros.put("identificacion", prs.getPrsIdentificacion());
//			}else{
//				frmRrmParametros.put("identificacion"," ");
//			}
		
		String dirCarrera = null;
		if(directorCarrera!=null){
		 dirCarrera = directorCarrera.getPrsNombres()+" "+directorCarrera.getPrsPrimerApellido().toUpperCase()+" "
				+(directorCarrera.getPrsSegundoApellido() == null?" ":directorCarrera.getPrsSegundoApellido());
		}else{
			dirCarrera= " ";
		}
		
		
		
		StringBuilder sbTextoInicial = new StringBuilder();
		sbTextoInicial.append("Señor(a)");sbTextoInicial.append("\n");
		sbTextoInicial.append(dirCarrera);sbTextoInicial.append("\n");
		sbTextoInicial.append("DIRECTOR(A) DE LA CARRERA DE ");
		if(crr.getCrrDescripcion()!=null){
			sbTextoInicial.append(crr.getCrrDescripcion());sbTextoInicial.append("\n");
		}else{
			sbTextoInicial.append(" ");sbTextoInicial.append("\n");
		}
		sbTextoInicial.append("FACULTAD DE ");
		if(dpn.getDpnDescripcion()!=null){
			sbTextoInicial.append(dpn.getDpnDescripcion());sbTextoInicial.append("\n");
		}else{
			sbTextoInicial.append(" ");sbTextoInicial.append("\n");
		}
		sbTextoInicial.append("Presente.- ");sbTextoInicial.append("\n\n");
		sbTextoInicial.append("Señor(a) Director(a)");sbTextoInicial.append("\n");
		
		frmRrmParametros.put("textoInicial", sbTextoInicial.toString());
		
		
		
		StringBuilder sbTexto = new StringBuilder();
		sbTexto.append("Yo, "); 
		sbTexto.append(nombres);
		sbTexto.append(" con identificación No. ");
		sbTexto.append(prs.getPrsIdentificacion());
		sbTexto.append(" estudiante de la Carrera de ");
		if(crr.getCrrDescripcion()!=null){
			sbTexto.append(crr.getCrrDescripcion());
			}else{
				sbTexto.append(" ");
			}
		sbTexto.append(" de la Facultad de ");
		if(dpn.getDpnDescripcion()!=null){
			sbTexto.append(dpn.getDpnDescripcion());
			}else{
				sbTexto.append(" ");
			}
	
		sbTexto.append(" ,solicito a usted muy comedidamente me conceda la autorización respectiva para realizar la Tercera Matrícula en la(s) siguiente(s) asignatura(s): ");
		
		frmRrmParametros.put("texto", sbTexto.toString());
		
		
		
		
		StringBuilder sbPeriodo = new StringBuilder();
		StringBuilder sbCodigo = new StringBuilder();
		StringBuilder sbAsignatura = new StringBuilder();
		StringBuilder sbHora = new StringBuilder();
		StringBuilder sbCausal = new StringBuilder();
		StringBuilder sbEvidencia = new StringBuilder();
		for (MateriaDto item : listaMaterias) {
			if(item.getMtrDescripcion().length() <= 44){
				
				if(prac.getPracDescripcion()!=null){
					sbPeriodo.append(prac.getPracDescripcion());sbPeriodo.append("\n\n");
			   }else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
				}else{
					sbCodigo.append("");sbCodigo.append("\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
				if(item.getMtrHoras() != null){
					sbHora.append(item.getMtrHoras());sbHora.append("\n\n");
				}else{
					sbHora.append("");sbHora.append("\n\n");
				}
				
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n");
					}else{
					 sbCausal.append(" ");sbCausal.append("\n\n");
					}
					
					if(item.getRutaPdf()!=null){
					sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n");
					}else{
						sbEvidencia.append(" ");sbEvidencia.append("\n\n");
					}
			}
			if(item.getMtrDescripcion().length() > 44 && item.getMtrDescripcion().length() <= 88){
							
				if(prac.getPracDescripcion()!=null){
					sbPeriodo.append(prac.getPracDescripcion());sbPeriodo.append("\n\n\n");
			   }else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
				}else{
					sbCodigo.append("");sbCodigo.append("\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
				if(item.getMtrHoras() != null){
					sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n");
				}else{
					sbHora.append("");sbHora.append("\n\n\n");
				}
				
				if(item.getCslCodigo()!=null){
					sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n");
					}else{
					 sbCausal.append(" ");sbCausal.append("\n\n\n");
					}
					
					if(item.getRutaPdf()!=null){
					sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n\n");
					}else{
						sbEvidencia.append(" ");sbEvidencia.append("\n\n\n");
					}
			}
			if(item.getMtrDescripcion().length() > 88 && item.getMtrDescripcion().length() <= 132){
							
				if(prac.getPracDescripcion()!=null){
					sbPeriodo.append(prac.getPracDescripcion());sbPeriodo.append("\n\n\n\n");
			   }else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n");
				}
				
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
				}else{
					sbCodigo.append("");sbCodigo.append("\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
				if(item.getMtrHoras() != null){
					sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n");
				}else{
					sbHora.append("");sbHora.append("\n\n\n\n");
				}
			
				 if(item.getCslCodigo()!=null){
						sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n");
						}else{
						 sbCausal.append(" ");sbCausal.append("\n\n\n\n");
						}
						
						if(item.getRutaPdf()!=null){
						sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n\n\n");
						}else{
							sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n");
						}
			}
			if(item.getMtrDescripcion().length() > 132 && item.getMtrDescripcion().length() <= 176){
			
				
				if(prac.getPracDescripcion()!=null){
					sbPeriodo.append(prac.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n");
			   }else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
				}else{
					sbCodigo.append("");sbCodigo.append("\n\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
				if(item.getMtrHoras() != null){
					sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n");
				}else{
					sbHora.append("");sbHora.append("\n\n\n\n\n");
				}
				
				if (item.getCslCodigo() != null) {
					sbCausal.append(item.getCslCodigo());
					sbCausal.append("\n\n\n\n\n");
				} else {
					sbCausal.append(" ");
					sbCausal.append("\n\n\n\n\n");
				}

				if (item.getRutaPdf() != null) {
					sbEvidencia.append(item.getRutaPdf());
					sbEvidencia.append("\n\n\n\n\n");
				} else {
					sbEvidencia.append(" ");
					sbEvidencia.append("\n\n\n\n\n");
				}
				
			}
			if(item.getMtrDescripcion().length() > 176 && item.getMtrDescripcion().length() <= 220){
			
				if(prac.getPracDescripcion()!=null){
					sbPeriodo.append(prac.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n");
			   }else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
				}else{
					sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
				if(item.getMtrHoras() != null){
					sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n\n");
				}else{
					sbHora.append("");sbHora.append("\n\n\n\n\n\n");
				}
				
				if (item.getCslCodigo() != null) {
					sbCausal.append(item.getCslCodigo());
					sbCausal.append("\n\n\n\n\n\n");
				} else {
					sbCausal.append(" ");
					sbCausal.append("\n\n\n\n\n\n");
				}

				if (item.getRutaPdf() != null) {
					sbEvidencia.append(item.getRutaPdf());
					sbEvidencia.append("\n\n\n\n\n\n");
				} else {
					sbEvidencia.append(" ");
					sbEvidencia.append("\n\n\n\n\n\n");
				}
				
			}
			if(item.getMtrDescripcion().length() > 220 && item.getMtrDescripcion().length() <= 264){
			
				if(prac.getPracDescripcion()!=null){
					sbPeriodo.append(prac.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n\n");
			   }else{
					sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n\n");
				}
				
				if(item.getMtrCodigo() != null){
					sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
				}else{
					sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n\n");
				}
				sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
				if(item.getMtrHoras() != null){
					sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n\n\n");
				}else{
					sbHora.append("");sbHora.append("\n\n\n\n\n\n\n");
				}
				if (item.getCslCodigo() != null) {
					sbCausal.append(item.getCslCodigo());
					sbCausal.append("\n\n\n\n\n\n\n");
				} else {
					sbCausal.append(" ");
					sbCausal.append("\n\n\n\n\n\n\n");
				}

				if (item.getRutaPdf() != null) {
					sbEvidencia.append(item.getRutaPdf());
					sbEvidencia.append("\n\n\n\n\n\n\n");
				} else {
					sbEvidencia.append(" ");
					sbEvidencia.append("\n\n\n\n\n\n\n");
				}
			}
			frmRrmParametros.put("periodo", sbPeriodo.toString());
			frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
			frmRrmParametros.put("asignatura", sbAsignatura.toString());
			frmRrmParametros.put("numero", sbHora.toString());
			frmRrmParametros.put("causal", sbCausal.toString());
			frmRrmParametros.put("evidencia", sbEvidencia.toString());
			
			if(prs.getPrsSegundoApellido() != null){
				frmRrmParametros.put("estudiante", prs.getPrsNombres()+" "+prs.getPrsPrimerApellido()+" "+prs.getPrsSegundoApellido());
			}else{
				frmRrmParametros.put("estudiante", prs.getPrsNombres()+" "+prs.getPrsPrimerApellido()+" "+prs.getPrsSegundoApellido());
				
			}
			
			if(usr.getUsrNick()!=null){
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
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSolicitudTercera");
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
		
	   }catch (Exception e) {
	   }
	
	
	} 
	
	/**
	 * Reimpresion de Solicitud
	 * 
	 */
	
	public static void generarReporteReimpresion(List<SolicitudTerceraMatriculaDto> listaSolicitudes, PersonaDto directorCarrera, Usuario usr){
		
		try{	
			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "solicitudTerceraMatricula";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
			//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			String nombres = null;
			String ci = null;
			String carrera = null;
			String dependencia = null;
			//Datos del Estudiante
			for (SolicitudTerceraMatriculaDto item : listaSolicitudes) {
				nombres=item.getPrsNombres()+" "+item.getPrsPrimerApellido().toUpperCase()+" "
						+(item.getPrsSegundoApellido() == null?" ":item.getPrsSegundoApellido());
				ci = item.getPrsIdentificacion();
				carrera = item.getCrrDescripcion();
				dependencia = item.getDpnDescripcion();
				break;
			}
			
			//Datos del Director de Carrera
			String dirCarrera = directorCarrera.getPrsNombres()+" "+directorCarrera.getPrsPrimerApellido().toUpperCase()+" "
					+(directorCarrera.getPrsSegundoApellido() == null?" ":directorCarrera.getPrsSegundoApellido());
			
			StringBuilder sbTextoInicial = new StringBuilder();
			sbTextoInicial.append("Señor(a)");sbTextoInicial.append("\n");
			sbTextoInicial.append(dirCarrera);sbTextoInicial.append("\n");
			sbTextoInicial.append("DIRECTOR(A) DE LA CARRERA DE ");
			for (SolicitudTerceraMatriculaDto item : listaSolicitudes) {
				if(item.getCrrDescripcion()!=null){
					sbTextoInicial.append(item.getCrrDescripcion());sbTextoInicial.append("\n");
				}else{
					sbTextoInicial.append(" ");sbTextoInicial.append("\n");
				}
				sbTextoInicial.append("FACULTAD DE ");
				if(item.getDpnDescripcion()!=null){
					sbTextoInicial.append(item.getDpnDescripcion());sbTextoInicial.append("\n");
				}else{
					sbTextoInicial.append(" ");sbTextoInicial.append("\n");
				}
				break;
			}
			sbTextoInicial.append("Presente.- ");sbTextoInicial.append("\n\n");
			sbTextoInicial.append("Señor(a) Director(a)");sbTextoInicial.append("\n");
			
			frmRrmParametros.put("textoInicial", sbTextoInicial.toString());
			
			
			
			StringBuilder sbTexto = new StringBuilder();
			sbTexto.append("Yo, "); 
			sbTexto.append(nombres);
			sbTexto.append(" con identificación No. ");
			sbTexto.append(ci);
			sbTexto.append(" estudiante de la Carrera de ");
			if(carrera != null){
				sbTexto.append(carrera);
				}else{
					sbTexto.append(" ");
				}
			sbTexto.append(" de la Facultad de ");
			if(dependencia != null){
				sbTexto.append(dependencia);
				}else{
					sbTexto.append(" ");
				}
		
			sbTexto.append(" ,solicito a usted muy comedidamente me conceda la autorización respectiva para realizar la Tercera Matrícula en la(s) siguiente(s) asignatura(s): ");
			
			frmRrmParametros.put("texto", sbTexto.toString());
			
			
			
			
			StringBuilder sbPeriodo = new StringBuilder();
			StringBuilder sbCodigo = new StringBuilder();
			StringBuilder sbAsignatura = new StringBuilder();
			StringBuilder sbHora = new StringBuilder();
			StringBuilder sbCausal = new StringBuilder();
			StringBuilder sbEvidencia = new StringBuilder();
			for (SolicitudTerceraMatriculaDto item : listaSolicitudes) {
				if(item.getMtrDescripcion().length() <= 44){
					
					if(item.getPracDescripcion()!=null){
						sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n");
				   }else{
						sbPeriodo.append(" ");sbPeriodo.append("\n\n");
					}
					
					if(item.getMtrCodigo() != null){
						sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
					}else{
						sbCodigo.append("");sbCodigo.append("\n\n");
					}
					sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
					if(item.getMtrHoras() != 0){
						sbHora.append(item.getMtrHoras());sbHora.append("\n\n");
					}else{
						sbHora.append("");sbHora.append("\n\n");
					}
					
					if(item.getCslCodigo()!=null){
						sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n");
						}else{
						 sbCausal.append(" ");sbCausal.append("\n\n");
						}
						
						if(item.getSltrmtDocumentoSolicitud() != null){
						sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n");
						}else{
							sbEvidencia.append(" ");sbEvidencia.append("\n\n");
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
						sbCodigo.append("");sbCodigo.append("\n\n\n");
					}
					sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
					if(item.getMtrHoras() != 0){
						sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n");
					}else{
						sbHora.append("");sbHora.append("\n\n\n");
					}
					
					if(item.getCslCodigo()!=null){
						sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n");
						}else{
						 sbCausal.append(" ");sbCausal.append("\n\n\n");
						}
						
						if(item.getSltrmtDocumentoSolicitud()!=null){
						sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n");
						}else{
							sbEvidencia.append(" ");sbEvidencia.append("\n\n\n");
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
						sbCodigo.append("");sbCodigo.append("\n\n\n\n");
					}
					sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
					if(item.getMtrHoras() != 0){
						sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n");
					}else{
						sbHora.append("");sbHora.append("\n\n\n\n");
					}
				
					 if(item.getCslCodigo()!=null){
							sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n");
							}else{
							 sbCausal.append(" ");sbCausal.append("\n\n\n\n");
							}
							
							if(item.getSltrmtDocumentoSolicitud()!=null){
							sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n\n");
							}else{
								sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n");
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
						sbCodigo.append("");sbCodigo.append("\n\n\n\n\n");
					}
					sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
					if(item.getMtrHoras() != 0){
						sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n");
					}else{
						sbHora.append("");sbHora.append("\n\n\n\n\n");
					}
					
					if (item.getCslCodigo() != null) {
						sbCausal.append(item.getCslCodigo());
						sbCausal.append("\n\n\n\n\n");
					} else {
						sbCausal.append(" ");
						sbCausal.append("\n\n\n\n\n");
					}

					if (item.getSltrmtDocumentoSolicitud() != null) {
						sbEvidencia.append(item.getSltrmtDocumentoSolicitud());
						sbEvidencia.append("\n\n\n\n\n");
					} else {
						sbEvidencia.append(" ");
						sbEvidencia.append("\n\n\n\n\n");
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
						sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n");
					}
					sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
					if(item.getMtrHoras() != 0){
						sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n\n");
					}else{
						sbHora.append("");sbHora.append("\n\n\n\n\n\n");
					}
					
					if (item.getCslCodigo() != null) {
						sbCausal.append(item.getCslCodigo());
						sbCausal.append("\n\n\n\n\n\n");
					} else {
						sbCausal.append(" ");
						sbCausal.append("\n\n\n\n\n\n");
					}

					if (item.getSltrmtDocumentoSolicitud() != null) {
						sbEvidencia.append(item.getSltrmtDocumentoSolicitud());
						sbEvidencia.append("\n\n\n\n\n\n");
					} else {
						sbEvidencia.append(" ");
						sbEvidencia.append("\n\n\n\n\n\n");
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
						sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n\n");
					}
					sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
					if(item.getMtrHoras() != 0){
						sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n\n\n");
					}else{
						sbHora.append("");sbHora.append("\n\n\n\n\n\n\n");
					}
					if (item.getCslCodigo() != null) {
						sbCausal.append(item.getCslCodigo());
						sbCausal.append("\n\n\n\n\n\n\n");
					} else {
						sbCausal.append(" ");
						sbCausal.append("\n\n\n\n\n\n\n");
					}

					if (item.getSltrmtDocumentoSolicitud() != null) {
						sbEvidencia.append(item.getSltrmtDocumentoSolicitud());
						sbEvidencia.append("\n\n\n\n\n\n\n");
					} else {
						sbEvidencia.append(" ");
						sbEvidencia.append("\n\n\n\n\n\n\n");
					}
				}
			}	
				frmRrmParametros.put("periodo", sbPeriodo.toString());
				frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
				frmRrmParametros.put("asignatura", sbAsignatura.toString());
				frmRrmParametros.put("numero", sbHora.toString());
				frmRrmParametros.put("causal", sbCausal.toString());
				frmRrmParametros.put("evidencia", sbEvidencia.toString());
				
			
			for (SolicitudTerceraMatriculaDto item : listaSolicitudes) {
				if(item.getPrsSegundoApellido() != null){
					frmRrmParametros.put("estudiante", item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
				}else{
					frmRrmParametros.put("estudiante", item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
					
				}
				break;
			}
			
			if(usr.getUsrNick()!=null){
				frmRrmParametros.put("nick", usr.getUsrNick());
			}else{
				frmRrmParametros.put("nick", " ");
			}
			
			StringBuilder sbCslCodigo= new StringBuilder();
			StringBuilder sbCslDescripcion= new StringBuilder();
			
			for (SolicitudTerceraMatriculaDto item : listaSolicitudes) {
				if(item.getCslCodigo()!=null){
			    	sbCslCodigo.append(item.getCslCodigo());sbCslCodigo.append("\n");	
			    }else{
					sbCslCodigo.append(" ");sbCslCodigo.append("\n");
			    }
				
				if(item.getCslDescripcion()!=null){
					sbCslDescripcion.append(item.getCslDescripcion());sbCslDescripcion.append("\n");
				}else{
					sbCslDescripcion.append(" ");sbCslDescripcion.append("\n");
					
				}
			frmRrmParametros.put("cslCodigo", sbCslCodigo.toString());
			frmRrmParametros.put("cslDescripcion", sbCslDescripcion.toString());
			}
					 
				
			
			
			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSolicitudTercera");
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
			
		   }catch (Exception e) {
		   }
		
		
		}
	
	
}
