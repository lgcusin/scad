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
   
 ARCHIVO:     ReporteHomologacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja el reporte de la solicitud de tercera matrícula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-08-2018			 Marcelo Quishpe                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.math.BigDecimal;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (session bean) ReporteHomologacionForm. 
 * Bean de sesion que maneja el reporte de homologación del estudiante realizado por la carrera.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "reporteHomologacionForm")
@SessionScoped
public class ReporteHomologacionForm implements Serializable {

	private static final long serialVersionUID = 6705724173087288262L;

	@PostConstruct
	public void inicializar(){
	}
	
	public static void generarReporteHomologacion( List<MateriaDto> ListHomologadosDto, Usuario usr, PersonaDto estudiante){
		try{	
			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			frmRrmNombreReporte = "vistaPreviaHomologacion";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			String nombres= null;
			if(estudiante!=null){
			nombres=estudiante.getPrsNombres()+" "+estudiante.getPrsPrimerApellido().toUpperCase()+" "+(estudiante.getPrsSegundoApellido() == null?" ":estudiante.getPrsSegundoApellido());
			}else{
				nombres=" ";
			}
			if(estudiante.getCrrDescripcion()!=null){
				frmRrmParametros.put("carrera", estudiante.getCrrDescripcion());
				}else{
					frmRrmParametros.put("carrera", " ");	
				}
			
			frmRrmParametros.put("estudiante",nombres );
			if(estudiante.getPrsIdentificacion()!=null){
				frmRrmParametros.put("identificacion", estudiante.getPrsIdentificacion());
				}else{
					frmRrmParametros.put("identificacion"," ");
				}
			
			if(usr.getUsrNick()!=null){
				frmRrmParametros.put("nick", usr.getUsrNick());
			}else{
				frmRrmParametros.put("nick", " ");
			}
			
			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteHomologacion");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
			frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
			
			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoRecord = null;
			for (MateriaDto it : ListHomologadosDto) {
				datoRecord = new HashMap<>();
				datoRecord.put("cod_asignatura", it.getMtrCodigo());
				datoRecord.put("asignatura", it.getMtrDescripcion());
				datoRecord.put("nota1", getBigDecimal(it.getNotaUno(), 1));
				datoRecord.put("nota2", getBigDecimal(it.getNotaDos(), 1));
				datoRecord.put("notaFinal", getBigDecimal(it.getNotaSuma(), 1));
				datoRecord.put("num_matricula", String.valueOf(it.getNumMatricula()));
				
				if(it.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE){
					datoRecord.put("estado", RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL);
				}else if(it.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
					datoRecord.put("estado", RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL);
				}
				
				frmRrmCampos.add(datoRecord);
			}
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		   }catch (Exception e) {
		   }
		
		} 
	



/**
 * Método que da formato de dos decimales a un BigDecimal.
 * @param param - bigdecimal
 * @param simbolo - adiciona simbolo.
 * @return valor en formato string.
 */
public static String getBigDecimal(BigDecimal param, int simbolo){
	
	if (param != null && param.intValue() != 0) {
		if (simbolo == 0) {
			return String.format("%.2f", param) + " %";	
		}else if(param.intValue() == -1 ){
			return "";
		}else if(simbolo == 1 ){
			return String.format("%.2f", param);
		}else if (simbolo == 2) {
			return String.valueOf(param.intValue());
		}
	}
	
	return "";
}

}
