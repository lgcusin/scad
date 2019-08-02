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
   
 ARCHIVO:     FechaCreacionMallaCurricularValidador.java	  
 DESCRIPCION: Validator el cual valida que la fecha de creación de la carrera o programa no sea menor al 2016. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-ABRIL-2017 		  Gabriel Mafla                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.validadores;

import java.text.ParseException;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (Validator) FechaCreacionMallaCurricularValidador.
 * Validator el cual valida que la fecha de creación de la carrera o programa no sea menor al 2016.
 * @author ghmafla.
 * @version 1.0
 */
@FacesValidator("fechaCreacionMallaCurricularValidador")
public class FechaCreacionMallaCurricularValidador implements Validator {

	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		Date valorDate = null;
		
		valorDate = (Date)valor;
        try {
			if (valorDate.before(GeneralesUtilidades.fechaCadenaToDate("01-01-2016", "dd-MM-yyyy"))) {
				FacesMessage msg = new FacesMessage("Error","La fecha no debe ser menor al 01-06-2016");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
				}
		} catch (ParseException e) {
			FacesUtil.mensajeInfo("Debe ingresar correctamente la fecha");
		}
//        GeneralesConstantes.APP_MAX_FECHA_MINIMA_MALLA_CURRICULAR, "dd-MM-yyyy"
	}
}
