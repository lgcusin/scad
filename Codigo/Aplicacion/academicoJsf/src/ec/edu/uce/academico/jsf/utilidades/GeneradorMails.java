package ec.edu.uce.academico.jsf.utilidades;


import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.ui.velocity.VelocityEngineUtils;

import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
import ec.edu.uce.envioMail.productor.ProductorMailJson;

public class GeneradorMails {
	
	private VelocityEngine motorVelocity;
	
	public GeneradorMails() {
		motorVelocity = new VelocityEngine();
		try {
			motorVelocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			motorVelocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			motorVelocity.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	public String generadorMailString(Map<String, Object> parametros, String pathTemplate){
		StringBuffer sbHtmlMsg = new StringBuffer(); 
		sbHtmlMsg.append(VelocityEngineUtils.mergeTemplateIntoString(motorVelocity, "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-registro.vm", "UTF-8", parametros));
		return sbHtmlMsg.toString();	
	}
	
	public String generarMailJson(List<String> correosTo, List<String> correosCc, List<String> correosBcc, 
								  String mailBase, String asunto, String usuarioWS, String claveWS,
								  Map<String, Object> parametros, String pathTemplate, boolean esFirmado) throws ValidacionMailException{
		//generacion del mensaje con plantilla velocity
		StringBuffer sbHtmlMsg = new StringBuffer(); 
		sbHtmlMsg.append(VelocityEngineUtils.mergeTemplateIntoString(motorVelocity, pathTemplate, "UTF-8", parametros));
		if(esFirmado){
			sbHtmlMsg.append(VelocityEngineUtils.mergeTemplateIntoString(motorVelocity, "/ec/edu/uce/academico/jsf/velocity/plantillas/template-pie-firma.vm", "UTF-8", parametros));
		}
		ProductorMailJson pmail = new ProductorMailJson(correosTo, correosCc, correosBcc, mailBase,asunto, sbHtmlMsg.toString(), usuarioWS, claveWS);
		return pmail.generarMail();
	}
	
	
}
