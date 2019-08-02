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
   
 ARCHIVO:     CedulaValidator.java	  
 DESCRIPCION: Validator el cual valida la identificacion 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-MAY-2016 		  Dennis Collaguazo                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.validadores;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * Clase (Validator) CedulaValidator.
 * Validator el cual valida la identificacion
 * @author dcollaguazo.
 * @version 1.0
 */

@FacesValidator("cedulaValidator")
public class CedulaValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		Integer tipoId = (Integer)comp.getAttributes().get("tipoIdent");
		String valorSt = (String)valor;
		if(tipoId == GeneralesConstantes.APP_ID_BASE.intValue()){
			FacesMessage msg = new FacesMessage("Error","Debe seleccionar el tipo de identificación");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}else if(tipoId == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
			if(!GeneralesUtilidades.validarDocumento(valorSt)){
				FacesMessage msg = new FacesMessage("Error","Cédula erronea");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}
}
