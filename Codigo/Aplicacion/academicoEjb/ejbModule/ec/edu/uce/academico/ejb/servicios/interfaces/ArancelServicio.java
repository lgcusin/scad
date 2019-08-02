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

 ARCHIVO:     ArancelServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Arancel.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.ArancelException;
import ec.edu.uce.academico.ejb.excepciones.ArancelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ArancelValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;

/**
 * Interface ArancelServicio
 * Interfase que define las operaciones sobre la tabla Arancel.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface ArancelServicio {

	/**
	 * Busca una entidad Arancel por su id
	 * @param id - deL Arancel a buscar
	 * @return Arancel con el id solicitado
	 * @throws ArancelNoEncontradoException - Excepcion lanzada cuando no se encuentra una Arancel con el id solicitado
	 * @throws ArancelException - Excepcion general
	 */
	public Arancel buscarPorId(Integer id) throws ArancelNoEncontradoException, ArancelException;

	/**
	 * Lista todas las entidades Arancel existentes en la BD
	 * @return lista de todas las entidades Arancel existentes en la BD
	 * @throws ArancelNoEncontradoException - Excepcion lanzada cuando no se encuentra una Arancel 
	 * @throws ArancelException - Excepcion general
	 */
	public List<Arancel> listarXGratuidadXTipoMatriculaXModalidadXTipoArancel(int tipoGratuidadId, int tipoMatricula, int modalidadId, int tipoArancel) throws ArancelNoEncontradoException , ArancelException;
		 
	/**
	 * Anade una Arancel en la BD
	 * @param entidad .- Entidad a ingresar
	 * @return Si se añadio o no la Arancel
	 * @throws ArancelValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws ArancelException - Excepción general
	 */
	public Arancel anadir(Arancel entidad) throws ArancelValidacionException, ArancelException;

}
