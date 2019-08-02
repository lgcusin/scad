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

 ARCHIVO:     EdificioServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Edificio. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-JUN-2017           Marcelo Quishpe 						Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.excepciones.EdificioException;
import ec.edu.uce.academico.ejb.excepciones.EdificioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EdificioServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Edificio;

/**
 * Clase (Bean)EdificioServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class EdificioServicioImpl implements EdificioServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource
	private SessionContext session;

	/**
	 * Busca una entidad Edificio por su id
	 * @param id - del Edificio a buscar
	 * @return Edificio con el id solicitado
	 * @throws EdificioNoEncontradoException - Excepcion lanzada cuando no se encuentra un Edificio con el id solicitado
	 * @throws EdificioException - Excepcion general
	 */
	
	
	@Override
	public Edificio buscarPorId(Integer id) throws EdificioNoEncontradoException, EdificioException {
		Edificio retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Edificio.class, id);
			} catch (NoResultException e) {
				throw new EdificioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edificio.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new EdificioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edificio.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new EdificioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edificio.buscar.por.id.exception",id)));
			}
		}
		return retorno;
	}
	
	
	/**
	 * Método que sirve para editar edificio
	 * @param entidad - entidad, entidad edificio ha ser editada
	 * @return, retorna verdadero si se ejecuto la edición
	 * @throws Exception - Exception excepción general
	 * @throws EdificioException - EdificioException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean editar(Edificio entidad) throws EdificioException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			
			if(entidad != null){
				Edificio edificioAux = em.find(Edificio.class, entidad.getEdfId());
				if(edificioAux.getEdfId() != 0){
//					edificioAux.setEdfCodigo(entidad.getEdfCodigo());
					edificioAux.setEdfDescripcion(entidad.getEdfDescripcion());
//					edificioAux.setEdfLocalizacion(entidad.getEdfLocalizacion());
					edificioAux.setEdfEstado(entidad.getEdfEstado());
					edificioAux.setEdfDependencia(entidad.getEdfDependencia());
				}
			}
			session.getUserTransaction().commit();
			retorno = true;
		} catch (Exception e) {
			try {
				retorno = false;
				session.getUserTransaction().rollback();
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SystemException e1) {
				retorno = false;
				e1.printStackTrace();
			}
			throw new EdificioException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para agregar un nuevo edificio
	 * @param entidad - entidad entidad, que tiene la informacion de todo el edificio
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws EdificioException - EdificioException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean nuevo(Edificio entidad) throws EdificioException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			Edificio edificioAux = new Edificio();
//			edificioAux.setEdfCodigo(entidad.getEdfCodigo().trim().toUpperCase());
			edificioAux.setEdfDescripcion(entidad.getEdfDescripcion().trim().toUpperCase());
//			edificioAux.setEdfLocalizacion(entidad.getEdfLocalizacion());
			edificioAux.setEdfEstado(entidad.getEdfEstado());
			edificioAux.setEdfDependencia(entidad.getEdfDependencia());
			em.persist(edificioAux);
			em.flush();
			session.getUserTransaction().commit();
			retorno = true;
		} catch (Exception e) {
			try {
				retorno = false;
				session.getUserTransaction().rollback();
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
			} catch (SecurityException e1) {
				retorno = false;
			} catch (SystemException e1) {
				retorno = false;
			}
			throw new EdificioException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para eliminar un edificio
	 * @param idEdificio - idEdificio, id del edificio eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws EdificioException - EdificioException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int idEdificio) throws EdificioException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			Edificio edificioAux = new Edificio();
			edificioAux = em.find(Edificio.class, idEdificio);
			em.remove(edificioAux);
			em.flush();
			session.getUserTransaction().commit();
			retorno = true;
		} catch (Exception e) {
			try {
				retorno = false;
				session.getUserTransaction().rollback();
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SystemException e1) {
				retorno = false;
				e1.printStackTrace();
			}
			throw new MallaCurricularMateriaException(e.getMessage());
		}
		return retorno;
	}
	

//	/**
//	 * Lista todas los edificios existentes en la BD
//	 * @return lista de todas los edificios existentes en la BD
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Edificio> listarTodos() throws EdificioNoEncontradoException{
//		List<Edificio> retorno = null;
//		StringBuffer sbsql = new StringBuffer();
//		sbsql.append(" Select edf from Edificio dpn ");
//		Query q = em.createQuery(sbsql.toString());
//		retorno = q.getResultList();
//		if(retorno.size()<=0){
//			throw new EdificioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edificio.buscar.todos.no.result.exception")));
//		}
//		return retorno;
//
//	}
	
	

}
