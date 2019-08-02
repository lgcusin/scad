/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     ContenidoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ContenidoServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.excepciones.ContenidoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ContenidoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.ContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)ContenidoServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class ContenidoServicioImpl implements ContenidoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Contenido por su id
	 * @param id - deL Contenido a buscar
	 * @return Contenido con el id solicitado
	 * @throws ContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Contenido con el id solicitado
	 * @throws ContenidoException - Excepcion general
	 */
	@Override
	public Contenido buscarPorId(Integer id) throws ContenidoNoEncontradoException, ContenidoException {
		Contenido retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Contenido.class, id);
			} catch (NoResultException e) {
				throw new ContenidoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Contenido existentes en la BD
	 * @return lista de todas las entidades Contenido existentes en la BD
	 * @throws ContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Contenido
	 * @throws ContenidoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Contenido> listarTodos() throws ContenidoNoEncontradoException , ContenidoException {
		List<Contenido> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpcn from Contenido tpcn order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ContenidoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.listar.todos.exception")));
		}
		return retorno;
	}

  	/**
	 * Lista de entidades Contenido existentes en la BD por tipo de evalucion
	 * @param idTipoContenido .- id del tipo de Contenido
	 * @return Lista de entidades Contenido existentes en la BD
	 * @throws ContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Contenido
	 * @throws ContenidoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Contenido> listarTodosXTipo(int idTipoContenido) throws ContenidoNoEncontradoException , ContenidoException {
		List<Contenido> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpcn from Contenido tpcn where ");
			sbsql.append(" tpcn.tpcnTipo =:idTipoContenido ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("idTipoContenido", idTipoContenido);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ContenidoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.listar.todos.x.tipo.no.result.exception")));
		} catch (Exception e) {
			throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.listar.todos.x.tipo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Añade una Contenido en la BD
	 * @return Si se añadio o no la Contenido
	 * @throws ContenidoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws ContenidoException - Excepción general
	 */
	@Override
	public Contenido anadir(Contenido entidad) throws ContenidoValidacionException, ContenidoException {
		Contenido retorno = null;
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.anadir.exception")));
			}
		} else {
			throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.anadir.null.exception")));
		}
		return retorno;
	} 

	
	
	public Boolean anadirApelacion(ContenidoEvaluacionDto entidad,Usuario usuario,Integer seleccionAux) throws ContenidoException{
		boolean retorno=false;
		if(entidad!=null)
		{
			try {
				
				Contenido contenidoAux=em.find(Contenido.class, entidad.getCntId());
				contenidoAux.setCntSeleccion(entidad.getCntSeleccion());
				if(contenidoAux.getCntSeleccionInicial()==null){
					contenidoAux.setCntSeleccionInicial(seleccionAux);		
				}
				
				contenidoAux.setCntUsuarioApelacion(usuario.getUsrNick());
				contenidoAux.setCntEstadoApelacion(ContenidoConstantes.ESTADO_APELACION_EJECUTADA_VALUE);
				contenidoAux.setCntRegistroApelacion(new Timestamp(System.currentTimeMillis()));
				contenidoAux.setCntOficioApelacion(entidad.getCntOficioApelacion());
				retorno=true;
				
			} catch (Exception e) { 
				throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.anadir.exception")));
			}
		} else {
			throw new ContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Contenido.anadir.null.exception")));
		}
		
		return retorno;
		
	}
}