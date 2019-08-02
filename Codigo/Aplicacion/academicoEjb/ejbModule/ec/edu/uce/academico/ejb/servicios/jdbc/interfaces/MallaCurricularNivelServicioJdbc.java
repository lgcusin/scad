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

 ARCHIVO:     MallaCurricularNivelServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla MallaCurricularNivel.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 06-ENE-2019			FREDDY GUZMÁN 						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelPosgradoDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;

/**
 * Interface MallaCurricularNivelServicioJdbc. Interface donde se registran los
 * metodos para el servicio jdbc de la tabla MallaCurricularNivel.
 * @author fgguzman
 * @version 1.0
 */
public interface MallaCurricularNivelServicioJdbc {

	/**
	 * Método que permite buscar la cantidad de creditos por nivel segun malla
	 * acurricular.
	 * @param crrId - id de la carrera.
	 * @return malla curricular por niveles.
	 * @throws MallaCurricularDtoException
	 * @throws MallaCurricularDtoNoEncontradoException
	 */
	List<MallaCurricularNivelDto> buscar(int crrId) throws MallaCurricularNivelException, MallaCurricularNivelNoEncontradoException;

	/**
	 * Método que permite listar la cantidad de creditos de toda la carrera segun malla.
	 * @author fgguzman
	 * @param idCarreras - id de las Carreras.
	 * @return total creditos por malla curricular. 
	 * @throws MallaCurricularNivelException
	 * @throws MallaCurricularNivelNoEncontradoException
	 */
	List<MallaCurricularNivelDto> buscarCreditosPorMalla(List<String> idCarreras) throws MallaCurricularNivelException, MallaCurricularNivelNoEncontradoException;

	List<MallaCurricularNivelPosgradoDto> buscarPosgrado(int crrId, int pracId) throws MallaCurricularNivelException, MallaCurricularNivelNoEncontradoException;


}
