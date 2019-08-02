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
   
 ARCHIVO:     MailValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto que se ingreso sea una direcciónde correo válida. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-DIC-2017 		  Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.validadores;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Clase (Validator) NotaValidator.
 * Validator el cual verifica que e texto que se ingreso sea una dirección de correo válida.
 * @author dalbuja.
 * @version 1.0
 */

@FacesValidator("mailValidator")
public class MailValidator implements Validator {

	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		String valorString = null;
		if(valor instanceof String){
			valorString = (String)valor;
			if(valorString==null || valorString.length()==0){
				FacesMessage msg = new FacesMessage("Error","No contiene caracteres");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			 }else{
				 String PATRON_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
				 Pattern pattern = Pattern.compile(PATRON_EMAIL);
				 Matcher matcher = pattern.matcher(valorString);
				 if(!matcher.matches()){
					 FacesMessage msg = new FacesMessage("Error","Debe ser una dirección de correo electrónica válida");
					 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					 throw new ValidatorException(msg);
				 }
			 }
		}else{
			FacesMessage msg = new FacesMessage("Error","Debe ser una cadena");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
	

	
}
