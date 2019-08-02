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
   
 ARCHIVO:     SinCaracteresEspecialesSoloNumerosFloatValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto contenga números y no tenga caracteres epeciales  
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-06-2017 		  Gabriel Mafla                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.validadores;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (Validator) SinCaracteresEspecialesSoloNumerosFloatValidator.
 * Validator el cual verifica que el texto contenga números decimales y no tenga caracteres epeciales 
 * @author ghmafla.
 * @version 1.0
 */


@FacesValidator("sinCaracteresEspecialesSoloNumerosFloatValidator")
public class SinCaracteresEspecialesSoloNumerosFloatValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		String valorSt = null;
		valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(valor.toString());
		valorSt=valorSt.replace(",", ".");
		try {
		     Float.parseFloat(valorSt);
		     int puntoDecimalUbc = valorSt.indexOf('.');
		     if(puntoDecimalUbc==0){
		    	 
		    	FacesUtil.limpiarMensaje();
		    	FacesMessage msg = new FacesMessage("Error","No se permiten números sin parte entera");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
		     }
		     int totalDecimales = valorSt.length() - puntoDecimalUbc - 1;
		     if(puntoDecimalUbc==-1){
		    	 if(Float.parseFloat(valorSt)>20){
		    		 FacesUtil.limpiarMensaje();
				   	 FacesMessage msg = new FacesMessage("Error","Calificación máximo sobre 20 puntos");
					 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					 
					 throw new ValidatorException(msg);
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesMessage msg = new FacesMessage("Error","No se permiten números negativos");
					 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					 throw new ValidatorException(msg);
				 } 
		     }else{
		    	 if(totalDecimales>2){
		    		 FacesUtil.limpiarMensaje();
		    		 FacesMessage msg = new FacesMessage("Error","Sólo permite máximo 2 números decimales");
					 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					 throw new ValidatorException(msg); 
				 }else if(Float.parseFloat(valorSt)>20){
					 FacesUtil.limpiarMensaje();
					 FacesMessage msg = new FacesMessage("Error","Calificación máximo sobre 20 puntos");
					 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					 throw new ValidatorException(msg);
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesMessage msg = new FacesMessage("Error","No se permiten números negativos");
					 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					 throw new ValidatorException(msg);
				 } 
		     }
		}
		catch (NumberFormatException ex) {
			FacesUtil.limpiarMensaje();
			FacesMessage msg = new FacesMessage("Error","Sólo números y signos decimales");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
	}
