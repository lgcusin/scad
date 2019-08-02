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

 ARCHIVO:     PrerrequisitoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones de la entidad Prerrequisito. 
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
import ec.edu.uce.academico.ejb.excepciones.PrerequisitoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PrerrequisitoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Prerequisito;
/**
 * Clase (Bean)PrerrequisitoServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class PrerrequisitoServicioImpl implements PrerrequisitoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource
	private SessionContext session;
	
	
	
	/**
	 * Método que sirve para agregar un nuevos prerequisito a una materia
	 * @param lisPrerrequisitos - lisPrerrequisitos, Lista de prerrequisitos a ser agregados.
	 * @return, retorna verdadero si se ejecuto la transacción añadir.
	 * @throws Exception - Exception excepción general
	 * @throws PrerequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean anadir(MateriaDto materiaPrincipal, List<MateriaDto> listPrerrequisitos) throws PrerequisitoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			int estado=0;
			Materia materiaAux= em.find(Materia.class, materiaPrincipal.getMtrId()) ;//busco materia principal
			for (MateriaDto materiaPrereq : listPrerrequisitos) {
				Materia materiaAuxPrereq=em.find(Materia.class, materiaPrereq.getMtrId());//busco materia prerequisito
				
				Prerequisito prerrequisitoAux = new Prerequisito();  //creo objeto prerrequisito a guardar
				prerrequisitoAux.setPrrEstado(estado);
				prerrequisitoAux.setPrrMateria(materiaAux);    //relacion a materia principal
				prerrequisitoAux.setPrrMateriaPrerequisito(materiaAuxPrereq);  //relacion a materia prerrequisito
				em.persist(prerrequisitoAux);
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
			throw new PrerequisitoException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para eliminar prerrequsitos de una materia
	*  @param materiaPrincipal - materiaPrincipal, entidad materia ha ser eliminados los prerrequisitos
	 * @param lisPrerrequisitosEliminar - lisPrerrequisitos, Lista de prerrequisitos a ser eliminados.
	 * @return, retorna verdadero si se ejecuto la transacción eliminar.
	 * @throws Exception - Exception excepción general
	 * @throws PrerequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(List<MateriaDto> listPrerrequisitosEliminar) throws PrerequisitoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			for (MateriaDto materiaPrereq : listPrerrequisitosEliminar) {
				Prerequisito materiaAuxPrereq=em.find(Prerequisito.class, materiaPrereq.getPrrId());
				em.remove(materiaAuxPrereq);
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
			throw new PrerequisitoException(e.getMessage());
		}
		return retorno;
	}
	

	
	/**
	 * Método que sirve para agregar un nuevos prerequisito a una materia
	 * @param lisPrerrequisitos - lisPrerrequisitos, Lista de prerrequisitos a ser agregados.
	 * @return, retorna verdadero si se ejecuto la transacción añadir.
	 * @throws Exception - Exception excepción general
	 * @throws PrerequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean guardar(List<MateriaDto> listPrerrequisitos) throws PrerequisitoException, Exception{
		boolean retorno = false;
		Prerequisito prerrequisito= null;
		try {
			session.getUserTransaction().begin();
			
			for (MateriaDto materiaPrereq : listPrerrequisitos) {			
				if(materiaPrereq.getPrrId()!=0){
					 prerrequisito = em.find(Prerequisito.class, materiaPrereq.getPrrId());
					if (prerrequisito != null) {
						prerrequisito.setPrrEstado(materiaPrereq.getPrrEstado());
		        	 }
				  }else {
					prerrequisito = new Prerequisito();
					Materia materiaPrincipal= em.find(Materia.class, materiaPrereq.getPrrMtrId());
					Materia materiaPrerrequisito= em.find(Materia.class, materiaPrereq.getPrrMtrPrrId());
					prerrequisito.setPrrEstado(materiaPrereq.getPrrEstado());
	        		prerrequisito.setPrrMateria(materiaPrincipal);
	        		prerrequisito.setPrrMateriaPrerequisito(materiaPrerrequisito);
	        		em.persist(prerrequisito);
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
			throw new PrerequisitoException(e.getMessage());
		}
		return retorno;
	}
	
}
