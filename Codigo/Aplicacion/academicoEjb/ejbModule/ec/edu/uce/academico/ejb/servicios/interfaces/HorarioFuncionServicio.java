/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     HorarioFuncionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla HorarioFuncion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 15-ABRIL-2019                Freddy Guzmán                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioFuncion;

/**
 * Interface HorarioFuncionServicio
 * Interfase que define las operaciones sobre la tabla HorarioFuncion.
 * @author fgguzman
 * @version 1.0
 */
public interface HorarioFuncionServicio {

	
	/**
	 * Método que permite guardar en la tabla Horario Funcion.
	 * @author fgguzman
	 * @param entidad - HorarioAcademico
	 * @return HorarioAcademico
	 * @throws HorarioAcademicoException
	 * @throws HorarioAcademicoValidacionException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	HorarioFuncion guardar(HorarioFuncion entidad) throws HorarioFuncionException,HorarioFuncionValidacionException;
	
	/**
	 * Método que sirve para eliminar un horario funcion
	 * @param hracId - hracId, id del horario academico a eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int horarioId) throws HorarioFuncionException, HorarioFuncionValidacionException, HorarioFuncionNoEncontradoException;

}
