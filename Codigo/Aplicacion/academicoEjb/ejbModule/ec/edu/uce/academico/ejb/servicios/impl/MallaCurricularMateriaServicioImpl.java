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

 ARCHIVO:     MallaCurricularMateriaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones de la entidad MallaCurricularMateria. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 08-AGOS-2017          Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

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
import javax.persistence.Query;
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.UnidadFormacion;

/**
 * Clase (Bean)MallaCurricularMateriaServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MallaCurricularMateriaServicioImpl implements MallaCurricularMateriaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource
	private SessionContext session;
	
	/**
	 * Método que sirve para editar la malla curricular materia
	 * @param entidad - entidad, entidad malla curricular materia ha ser editada
	 * @return, retorna verdadero si se ejecuto la edición
	 * @throws Exception - Exception excepción general
	 * @throws MallaCurricularMateriaException - MallaCurricularMateriaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean editar(MallaCurricularMateria entidad) throws MallaCurricularMateriaException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			
			if(entidad != null){
				MallaCurricularMateria mlcrmtAux = em.find(MallaCurricularMateria.class, entidad.getMlcrmtId());
				if(mlcrmtAux.getMlcrmtId() != 0){
					mlcrmtAux.setMlcrmtEstado(entidad.getMlcrmtEstado());
					mlcrmtAux.setMlcrmtUnidadFormacion(entidad.getMlcrmtUnidadFormacion());
					mlcrmtAux.setMlcrmtNivel(entidad.getMlcrmtNivel());
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
			throw new MallaCurricularMateriaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para agregar una nueva malla curricular materia
	 * @param idUnidadFormacion - idUnidadFormacion, entidad malla curricular materia ha ser editada
	 * @param idNivel - idNivel entidad, entidad malla curricular materia ha ser editada
	 * @param idMallaCurricular - idMallaCurricular entidad, entidad malla curricular materia ha ser editada
	 * @param listMateria - listMateria entidad, entidad malla curricular materia ha ser editada
	 * @param estado - estado entidad, entidad malla curricular materia ha ser editada
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws MallaCurricularMateriaException - MallaCurricularMateriaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean nuevo(int idUnidadFormacion, int idNivel, int idMallaCurricular, List<MateriaDto> listMateria, int estado) throws MallaCurricularMateriaException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			UnidadFormacion unfrAux = em.find(UnidadFormacion.class, idUnidadFormacion);
			Nivel nvlAux = em.find(Nivel.class, idNivel);
			MallaCurricular mlcrAux = em.find(MallaCurricular.class, idMallaCurricular);
			
			for (MateriaDto item : listMateria) {
				Materia mtrAux = em.find(Materia.class, item.getMtrId());
				if(unfrAux.getUnfrId() != 0 && nvlAux.getNvlId() != 0 && mlcrAux.getMlcrId() != 0 && mtrAux.getMtrId() != 0){
					MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
					mlcrmtAux.setMlcrmtUnidadFormacion(unfrAux);
					mlcrmtAux.setMlcrmtNivel(nvlAux);
					mlcrmtAux.setMlcrmtMallaCurricular(mlcrAux);
					mlcrmtAux.setMlcrmtMateria(mtrAux);
					mlcrmtAux.setMlcrmtEstado(estado);
					em.persist(mlcrmtAux);
					em.flush();
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
			throw new MallaCurricularMateriaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para eliminar una malla curricular materia
	 * @param idMallaMateria - idMallaMateria, id de la malla curricular materia a eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws MallaCurricularMateriaException - MallaCurricularMateriaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int idMallaMateria) throws MallaCurricularMateriaException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
			mlcrmtAux = em.find(MallaCurricularMateria.class, idMallaMateria);
			em.remove(mlcrmtAux);
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
	
	/**
	 * Busca una entidad MallaCurricularMateria por su id
	 * @param id - de la MallaCurricularMateria a buscar
	 * @return MallaCurricularMateria con el id solicitado
	 * @throws MallaCurricularMateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una malla curricular paralelo con el id solicitado
	 * @throws MallaCurricularMateriaException - Excepcion general
	 */
	@Override
	public MallaCurricularMateria buscarPorId(Integer prlId) throws MallaCurricularMateriaNoEncontradoException, MallaCurricularMateriaException {
		MallaCurricularMateria retorno = null;
		if (prlId != null ) {
			try {
				retorno = em.find(MallaCurricularMateria.class, prlId);
			} catch (NoResultException e) {
				throw new MallaCurricularMateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.no.result.exception",prlId)));
			}catch (NonUniqueResultException e) {
				throw new MallaCurricularMateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.non.unique.result.exception",prlId)));
			} catch (Exception e) {
				throw new MallaCurricularMateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	
		@Override
		public MallaCurricularMateria buscarPorMtrIdPorEstado(Integer mtrId, Integer estado) throws MallaCurricularMateriaNoEncontradoException, MallaCurricularMateriaException {
			MallaCurricularMateria retorno = null;
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcrmt from MallaCurricularMateria mlcrmt ");
			sbsql.append(" Where mlcrmt.mlcrmtMateria.mtrId = :mtrId ");
			sbsql.append(" and mlcrmt.mlcrmtEstado = :estado ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("mtrId", mtrId);
			q.setParameter("estado", estado);
			retorno = (MallaCurricularMateria) q.getSingleResult();
			return retorno;
		}
	
}
