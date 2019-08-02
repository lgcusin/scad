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

 ARCHIVO:     CorrequisitoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones de la entidad Correquisito. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-AGOS-2017          Marcelo Quishpe                   Emisión Inicial
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
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CorequisitoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CorrequisitoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Corequisito;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;

/**
 * Clase (Bean)CorrequisitoServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CorrequisitoServicioImpl implements CorrequisitoServicio{

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
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//	public boolean editar(MallaCurricularMateria entidad) throws MallaCurricularMateriaException, Exception{
//		boolean retorno = false;
//		try {
//			session.getUserTransaction().begin();
//			
//			if(entidad != null){
//				MallaCurricularMateria mlcrmtAux = em.find(MallaCurricularMateria.class, entidad.getMlcrmtId());
//				if(mlcrmtAux.getMlcrmtId() != 0){
//					mlcrmtAux.setMlcrmtEstado(entidad.getMlcrmtEstado());
//					mlcrmtAux.setMlcrmtUnidadFormacion(entidad.getMlcrmtUnidadFormacion());
//					mlcrmtAux.setMlcrmtNivel(entidad.getMlcrmtNivel());
//				}
//			}
//			session.getUserTransaction().commit();
//			retorno = true;
//		} catch (Exception e) {
//			try {
//				retorno = false;
//				session.getUserTransaction().rollback();
//				e.getStackTrace();
//			} catch (IllegalStateException e1) {
//				retorno = false;
//				e1.printStackTrace();
//			} catch (SecurityException e1) {
//				retorno = false;
//				e1.printStackTrace();
//			} catch (SystemException e1) {
//				retorno = false;
//				e1.printStackTrace();
//			}
//			throw new MallaCurricularMateriaException(e.getMessage());
//		}
//		return retorno;
//	}
	
	/**
	 * Método que sirve para agregar un nuevos Correquisito a una materia
	 * @param materiaPrincipal - materiaPrincipal, entidad materia ha ser agregados los Correquisitos
	 * @param lisCorrequisitos - lisCorrequisitos, Lista de prerrequisitos a ser agregados.
	 * @return, retorna verdadero si se ejecuto la transacción añadir.
	 * @throws Exception - Exception excepción general
	 * @throws CorequisitoException - CorequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean anadir(MateriaDto materiaPrincipal, List<MateriaDto> listCorrequisitos) throws CorequisitoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			int estado=0;
			Materia materiaAux= em.find(Materia.class, materiaPrincipal.getMtrId()) ;
			for (MateriaDto materiaCoreq : listCorrequisitos) {
				Materia materiaAuxCoreq=em.find(Materia.class, materiaCoreq.getMtrId());
				Corequisito CorrequisitoAux = new Corequisito();

				CorrequisitoAux.setCrqEstado(estado);
				CorrequisitoAux.setCrqMateria(materiaAux);
				CorrequisitoAux.setCrqMateriaCorequisito(materiaAuxCoreq);
				em.persist(CorrequisitoAux);
				em.flush();
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
			throw new CorequisitoException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para eliminar Correqusitos de una materia
	 * @param lisPrerrequisitosEliminar - lisPrerrequisitos, Lista de prerrequisitos a ser eliminados.
	 * @return, retorna verdadero si se ejecuto la transacción eliminar.
	 * @throws Exception - Exception excepción general
	 * @throws CorequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(List<MateriaDto> listCorrequisitosEliminar) throws CorequisitoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			for (MateriaDto materiaCoreq : listCorrequisitosEliminar) {
				Corequisito materiaAuxCoreq=em.find(Corequisito.class, materiaCoreq.getCrqId());
				em.remove(materiaAuxCoreq);
				em.flush();
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
			throw new CorequisitoException(e.getMessage());
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Método que sirve para guardar correquisitos de una materia
	 * @param materiaCorequisito -  entidad materia ha ser creada o editada 
	 * @return Correquisito agregado o editado
	 * @throws Exception - Exception excepción general
	 * @throws CorequisitoException - CorequisitoException, excepción general.
	 */
	

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean guardar(List<MateriaDto> listCorrequisitos) throws CorequisitoException, Exception{
		boolean retorno = false;
		Corequisito correquisito= null;
		try {
			session.getUserTransaction().begin();
			
		
			for (MateriaDto materiaCoreq : listCorrequisitos) {
				
				if(materiaCoreq.getCrqId()!=0){
				correquisito = em.find(Corequisito.class, materiaCoreq.getCrqId());
				if (correquisito != null) {
					correquisito.setCrqEstado(materiaCoreq.getCrqEstado());
	        	   }
			     }else {
				correquisito = new Corequisito();
				Materia materiaPrincipal= em.find(Materia.class, materiaCoreq.getCrqMtrId());
				Materia materiaCorrequisito= em.find(Materia.class, materiaCoreq.getCrqMtrCorequisitoId());
				correquisito.setCrqEstado(materiaCoreq.getCrqEstado());
        		correquisito.setCrqMateria(materiaPrincipal);
        		correquisito.setCrqMateriaCorequisito(materiaCorrequisito);
        		em.persist(correquisito);
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
			throw new CorequisitoException(e.getMessage());
		}
		return retorno;
	}
	
}
