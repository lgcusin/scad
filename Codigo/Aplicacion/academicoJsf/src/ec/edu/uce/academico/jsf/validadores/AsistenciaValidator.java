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
   
 ARCHIVO:     asistenciaValidator.java	  
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



/**
 * Clase (Validator) asistenciaValidator.
 * Validator el cual verifica que ela asistencia ingresada no sea mayor que la asistencia del docente 
 * @author ghmafla.
 * @version 1.0
 */


@FacesValidator("asistenciaValidator")
public class AsistenciaValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		
		Integer asistenciaEstudiante = (Integer) valor;
		String asistenciaDocente = fc.getExternalContext().getRequestParameterMap().get("frmEditarNotasPregrado:txtAsistenciaDocente");
		
		try{
			Integer aDocente = Integer.valueOf(asistenciaDocente);
			if(asistenciaEstudiante>aDocente){
				FacesMessage msg = new FacesMessage("Error","La asitencia no puede ser mayor a la asistencia del docente");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}catch(NumberFormatException e){
			FacesMessage msg = new FacesMessage("Error","Debe ingresar la asistencia del docente en el hemisemestre");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
		
}
