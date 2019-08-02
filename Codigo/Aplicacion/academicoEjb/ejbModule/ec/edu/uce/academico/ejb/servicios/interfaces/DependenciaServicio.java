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

 ARCHIVO:     DependenciaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Dependencia.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-abril-2017            Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;

/**
 * Interface DependenciaServicio
 * Interfase que define las operaciones sobre la tabla Dependencia.
 * @author ghmafla
 * @version 1.0
 */
public interface DependenciaServicio {
	
	/**
	 * Busca una entidad Dependencia por su id
	 * @param id - del Dependencia a buscar
	 * @return Dependencia con el id solicitado
	 * @throws DependenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Dependencia con el id solicitado
	 * @throws DependenciaException - Excepcion general
	 */
	public Dependencia buscarPorId(Integer id) throws DependenciaNoEncontradoException, DependenciaException;
	
	/**
	 * Lista todas las dependencias existentes en la BD
	 * @return lista de todas las dependencias existentes en la BD
	 */
	public List<Dependencia> listarTodos() throws DependenciaNoEncontradoException;
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	public List<Dependencia> listarFacultadesActivas(int jerarquiaId) throws DependenciaNoEncontradoException;

	/**
	 * Lista la facultad de acuerdo a la carrera enviada
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	public Dependencia buscarFacultadXcrrId(int crrId) throws DependenciaNoEncontradoException;
	
	public List<Dependencia> listarFacultadesxUsuario(int usrId) throws DependenciaNoEncontradoException;
	
	public List<Dependencia> listarFacultadesxUsuarioXDependencia(int usrId, int dpnId) throws DependenciaNoEncontradoException;

	Dependencia buscarDependenciaXcrrId(int crrId) throws DependenciaNoEncontradoException;
	
	/**
	 * Lista la facultad de acuerdo a la carrera enviada, sin excepciones
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	public Dependencia buscarDependenciaXcrrIdSinException(int crrId);
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 */
	public List<Dependencia> listarFacultadesActivasXTipoCarrera(int jerarquiaId, int tipoCarrera) throws DependenciaNoEncontradoException;
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 */
	public List<Dependencia> listarFacultadesActivasEvaluacionXTipoCarrera(int jerarquiaId, int tipoCarrera, int periodoId) throws DependenciaNoEncontradoException;

	public List<Dependencia> listarFacultadesActivasPosgrado(int jerarquiaId) throws DependenciaNoEncontradoException;
	
	/**
	 * Método que permite recuperar las dependencias por usuario y tipo de carrera.
	 * @author fgguzman
	 * @param usuarioId - id del usuario
	 * @param tipoCrrId -tipo de la carrera
	 * @return dependencias
	 * @throws DependenciaNoEncontradoException
	 */
	List<Dependencia> buscarDependenciasPorCrrTipoUsrRol(int usuarioId, int tipoCrrId, int rolId) throws DependenciaNoEncontradoException;

	/**
	 * Método que permite buscar dependencias por jeraquia y tipo de carrera.
	 * @author fgguzman
	 * @param jerarquia 
	 * @param tipoCarrera
	 * @return dependencias.
	 * @throws DependenciaNoEncontradoException
	 * @throws DependenciaException
	 */
	List<Dependencia> buscarDependencias(int jerarquia, int tipoCarrera) throws DependenciaNoEncontradoException, DependenciaException;
}
