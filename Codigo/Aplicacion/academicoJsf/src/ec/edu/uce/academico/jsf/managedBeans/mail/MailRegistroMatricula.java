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
   
 ARCHIVO:     MailRegistroMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-03-2018			 Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.mail;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Clase (session bean) MailRegistroMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author ajvillafuerte.
 * @version 1.0
 */ 


public class MailRegistroMatricula implements Serializable {
	
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
	
	
	
	//------------------------------------------------------CONTENIDO MAIL-----------------------------------------------------------------
	
 /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
 * 
 * @param aux true -> funcionario ----- false -> administrador de nómina
 * @param fecha mes y año que corresponde el rol
 * @param cadena nombre del funcionario
 * @return cadena con el mail
 */

  public static StringBuilder generarMailRegistroMatricula(String fecha,String nombres, String carrera ,String nivel, String facultad, String gratuidad, String valorPagar){
	  
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div style=\"text-align: center;\">");
		sb.append("<span style=\"font-size:18px;\"><strong><span style=\"font-size:20px;\">UNIVERSIDAD CENTRAL DEL ECUADOR</span></strong></span><br />");
		sb.append("<br />");
		sb.append("<strong><span style=\"font-size: 16px;\"><span style=\"font-size:18px;\">Sistema Integral de Informaci&oacute;n Universitaria</span></span></strong><br />");
		sb.append("<br />");
		sb.append("<span style=\"font-size:18px;\"><strong>"+facultad+"<br />");
		sb.append(carrera+"</strong></span><br />");
		sb.append("&nbsp;</div>");
		sb.append("<div><strong>Estimado/a. "+nombres+".</strong></div>");
		sb.append("<div style=\"text-align: center;\"><br /> &nbsp;</div>");
		sb.append("<div>");
		sb.append("Su matr&#237;cula en el m&#243;dulo Acad&#233;mico del Sistema Integral de Informaci&#243;n Universitaria &#45; SIIU se ha generado con &#233;xito y con el siguiente detalle:<br />");
		sb.append("</div>");
		sb.append("<br/>");
		sb.append("<div style=\"text-align: center;\">");
		sb.append("<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px;\">");
		sb.append("<tbody>");
		sb.append("<tr>");
  		sb.append("<td><strong>NIVEL<strong></td>");
  		sb.append("<td>");
  		sb.append(""+nivel+"</td>");
  		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td><strong>GRATUIDAD</strong></td>");
		sb.append("<td>");
		sb.append(""+gratuidad+"</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td><strong>VALOR A PAGAR</strong></td>");
		sb.append("<td>");
		sb.append(""+valorPagar+"</td>");
		sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		sb.append("<br/>");
		sb.append("<div><strong>Adjunto al presente se servir&#225; encontrar su Registro de Matr&#237;cula.</strong><br /> </div>");
		sb.append("<br/>");
		sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
		sb.append("</p></strong>");
		sb.append("<br/>");
		sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
		sb.append("<br/>Universidad Central del Ecuador.");
		sb.append("<br/>");
		sb.append("<br/>");
		
		sb.append("------------------------------------------------------------------------------------------------------------------");
		sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
		sb.append("<tbody>");
		sb.append("<tr style=\"min-height:10.1pt\">");
		sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
		sb.append("<br/><br/>");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
		sb.append("</p>");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
		sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
		sb.append("</p>");
		sb.append("</td>");
		sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
		sb.append("</p>");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
		sb.append("</p>");
		sb.append("</td>");
		sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
		sb.append("<br/>  <br/>");
		sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
		sb.append("</p>");
		sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
		sb.append("</p>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("------------------------------------------------------------------------------------------------------------------");
		sb.append("</body>");
		sb.append("</html>");
		return sb;
	}
	
  
  /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
   * 
   * @param aux true -> funcionario ----- false -> administrador de nómina
   * @param fecha mes y año que corresponde el rol
   * @param cadena nombre del funcionario
   * @return cadena con el mail
   */

    public static StringBuilder generarMailOrdenCobro(String fecha,String nombres, String carrera ,String nivel, String facultad, String gratuidad, String valorPagar){
  	  
  		StringBuilder sb = new StringBuilder();
  		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
  		sb.append("<html>");
  		sb.append("<head>");
  		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
  		sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
  		sb.append("</head>");
  		sb.append("<body>");
  		sb.append("<div style=\"text-align: center;\">");
  		sb.append("<span style=\"font-size:18px;\"><strong><span style=\"font-size:20px;\">UNIVERSIDAD CENTRAL DEL ECUADOR</span></strong></span><br />");
  		sb.append("<br />");
  		sb.append("<strong><span style=\"font-size: 16px;\"><span style=\"font-size:18px;\">Sistema Integral de Informaci&oacute;n Universitaria</span></span></strong><br />");
  		sb.append("<br />");
  		sb.append("<span style=\"font-size:18px;\"><strong>"+facultad+"<br />");
  		sb.append(carrera+"</strong></span><br />");
  		sb.append("&nbsp;</div>");
  		sb.append("<div>");
  		sb.append("<strong>Estimado/a. "+nombres+".</strong></div>");
  		sb.append("<div style=\"text-align: center;\">");
  		sb.append("<br /> &nbsp;</div>");
  		sb.append("<div>");
  		sb.append("Se ha generado su matr&iacute;cula con &eacute;xito.&nbsp; Adjunto al presente se encuentra su Orden de Cobro.<br />");
  		sb.append("&nbsp;</div>");
  		sb.append("<div style=\"text-align: center;\">");
  		sb.append("<div> &nbsp;</div>");
  		sb.append("<div style=\"text-align: left;\"> &nbsp;</div>");
  		sb.append("<div>");
  		sb.append("&nbsp;</div>");
  		sb.append("<div style=\"text-align: right;\">");
  		sb.append("&nbsp;</div>");
  		sb.append("<div style=\"text-align: left;\">");
  		sb.append("&nbsp;</div>");
  		sb.append("<div style=\"text-align: right;\">");
  		sb.append("&nbsp;</div>");
  		sb.append("<div>");
  		sb.append("&nbsp;</div>");
  		sb.append("<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px;\">");
  		sb.append("<tbody>");
  		sb.append("<tr>");
  		sb.append("<td>");
  		sb.append("NIVEL</td>");
  		sb.append("<td>");
  		sb.append(""+nivel+"</td>");
  		sb.append("</tr>");
  		sb.append("<tr>");
  		sb.append("<td>");
  		sb.append("GRATUIDAD</td>");
  		sb.append("<td>");
  		sb.append(""+gratuidad+"</td>");
  		sb.append("</tr>");
  		sb.append("<tr>");
  		sb.append("<td>");
  		sb.append("VALOR A PAGAR</td>");
  		sb.append("<td>");
  		sb.append(""+valorPagar+"</td>");
  		sb.append("</tr>");
  		sb.append("</tbody>");
  		sb.append("</table>");
  		sb.append("<p style=\"text-align: left;\">");
  		sb.append("&nbsp;</p>");
  		sb.append("</div>");
  		sb.append("<div>");
  		sb.append("<br />");
  		sb.append("<br />");
  		sb.append("&nbsp;</div>");
  		sb.append("<div style=\"text-align: center;\">");
  		sb.append("&nbsp;</div>");


  		sb.append("<br/>");
  		sb.append("<br/>");
  		sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
  		sb.append("</p></strong>");
  		sb.append("<br/>");
  		sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
  		sb.append("<br/>Universidad Central del Ecuador.");
  		sb.append("<br/>");
  		sb.append("<br/>");
  		
  		sb.append("------------------------------------------------------------------------------------------------------------------");
  		sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
  		sb.append("<tbody>");
  		sb.append("<tr style=\"min-height:10.1pt\">");
  		sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
  		sb.append("<br/><br/>");
  		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
  		sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
  		sb.append("</p>");
  		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
  		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
  		sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
  		sb.append("</p>");
  		sb.append("</td>");
  		sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
  		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
  		sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
  		sb.append("</p>");
  		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
  		sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
  		sb.append("</p>");
  		sb.append("</td>");
  		sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
  		sb.append("<br/>  <br/>");
  		sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
  		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
  		sb.append("</p>");
  		sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
  		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
  		sb.append("</p>");
  		sb.append("</td>");
  		sb.append("</tr>");
  		sb.append("</tbody>");
  		sb.append("</table>");
  		sb.append("------------------------------------------------------------------------------------------------------------------");
  		sb.append("</body>");
  		sb.append("</html>");
  		return sb;
  	}
	
    public static StringBuilder generarMailCorreccionesAtiempoLegalizarMatricula(String fecha,String nombres, String dependencia){
  	  
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div style=\"text-align: center;\">");
		sb.append("<span style=\"font-size:18px;\"><strong><span style=\"font-size:20px;\">UNIVERSIDAD CENTRAL DEL ECUADOR</span></strong></span><br />");
		sb.append("<br />");
		sb.append("<strong><span style=\"font-size: 16px;\"><span style=\"font-size:18px;\">Sistema Integral de Informaci&oacute;n Universitaria</span></span></strong><br />");
		sb.append("<br />");
		sb.append("<span style=\"font-size:18px;\"><strong><br />");
		sb.append(dependencia+"</strong></span><br />");
		sb.append("&nbsp;</div>");
		sb.append("<div>");
		sb.append("<strong>Estimado/a. "+nombres+".</strong></div>");
		sb.append("<div style=\"text-align: center;\">");
		sb.append("<br /> &nbsp;</div>");
		sb.append("<div>");
		sb.append("Se ha notificado errores al legalizar las matriculas.&nbsp; Adjunto el listado de los implicados.<br />");
		sb.append("&nbsp;</div>");
		sb.append("<div style=\"text-align: center;\">");
		sb.append("<div> &nbsp;</div>");
		sb.append("<div style=\"text-align: left;\"> &nbsp;</div>");
		sb.append("<div>");
		sb.append("&nbsp;</div>");
		sb.append("<div style=\"text-align: right;\">");
		sb.append("&nbsp;</div>");
		sb.append("<div style=\"text-align: left;\">");
		sb.append("&nbsp;</div>");
		sb.append("<div style=\"text-align: right;\">");
		sb.append("&nbsp;</div>");
		sb.append("<div>");
		sb.append("&nbsp;</div>");
		sb.append("<p style=\"text-align: left;\">");
		sb.append("&nbsp;</p>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<br />");
		sb.append("<br />");
		sb.append("&nbsp;</div>");
		sb.append("<div style=\"text-align: center;\">");
		sb.append("&nbsp;</div>");
		sb.append("<br/>");
		sb.append("<br/>");
		
		sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
		sb.append("</p></strong>");
		sb.append("<br/>");
		sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
		sb.append("<br/>Universidad Central del Ecuador.");
		sb.append("<br/>");
		sb.append("<br/>");
		
		sb.append("------------------------------------------------------------------------------------------------------------------");
		sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
		sb.append("<tbody>");
		sb.append("<tr style=\"min-height:10.1pt\">");
		sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
		sb.append("<br/><br/>");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema Integral de Informaci&#243;n Universitaria<br></span></b>");
		sb.append("</p>");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
		sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
		sb.append("</p>");
		sb.append("</td>");
		sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
		sb.append("</p>");
		sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
		sb.append("</p>");
		sb.append("</td>");
		sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
		sb.append("<br/>  <br/>");
		sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
		sb.append("</p>");
		sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
		sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
		sb.append("</p>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("------------------------------------------------------------------------------------------------------------------");
		sb.append("</body>");
		sb.append("</html>");
		return sb;
	}
    
	//------------------------------------------------------CARGA DE DATOS-----------------------------------------------------------------
	
	 
	
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
			fecha = sdf.parse(tmFecha.toString());
		} catch (ParseException e) {
			return "Error al calcular la fecha.";
		}
		return sdf.format(fecha);
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
