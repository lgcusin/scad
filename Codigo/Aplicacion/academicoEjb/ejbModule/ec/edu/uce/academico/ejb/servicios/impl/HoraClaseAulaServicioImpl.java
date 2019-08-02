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

 ARCHIVO:     HoraClaseAulaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla HoraClaseAula. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 05-07-2017           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseAulaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HoraClaseAulaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;



/**
 * Clase (Bean) HoraClaseAulaServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */



@Stateless
  public class HoraClaseAulaServicioImpl  implements HoraClaseAulaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	

	/**
	 * Busca si existe al meos una  entidad HoraClaseAula por  id del Aula para eliminar sino existe la relacion
	 * @param idAula -id del aula del HoraClaseAula a a buscar
	 * @return true si encuentra por lo menos un HoraClaseAula con idAula o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	@Override
	public boolean buscarPorIdAula(Integer idAula)  {
		boolean retorno=false;
		if (idAula != null) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select hoclal from HoraClaseAula hoclal ");
				sbsql.append(" where hoclal.hoclalAula.alaId =:alaId ");
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("alaId", idAula);
			    q.getSingleResult(); 
				retorno = true;
			} catch (NoResultException e) {
				retorno = false; // retorna falso si no existe alguna Hora_clase_aula relacionada con el aula
				return retorno;
			}catch (NonUniqueResultException e) {
				retorno = true; // retorna verdader si  existe varios Hora_clase_aula  relacionados con el aula
				return retorno;
			} catch (Exception e) {
				retorno = true; // retorna verdader si  existe error Hora_clase_aula  relacionados con el aula
				return retorno;
			} 
		}
		return retorno;
	}
	
	
	/**
	 * Busca Lista de  HoraClaseAula por  id del Aula 
	 * @param idAula - id del Aula a a buscar
	 * @return Lista de  HoraClaseAula con idAula. 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HoraClaseAula> ListarHoraClaseAulaPIdAla(Integer idAula) throws  HoraClaseAulaException, HoraClaseAulaNoEncontradoException  {
		List<HoraClaseAula> retorno=null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select hoclal from HoraClaseAula hoclal ");
			sbsql.append(" where hoclal.hoclalAula.alaId =:alaId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("alaId", idAula);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new HoraClaseAulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.listar.HoraClaseAula.por.Aula.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			throw new HoraClaseAulaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.listar.HoraClaseAula.por.Aula.no.result.exception")));
		}
		return retorno;
	}
	
	

	/**
	 * Añade  horaClase por cada aula en la BD
	 * @return Si se añadio o no correctamente toda la lista de horas clase a el aula
	 * @throws HoraClaseAulaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws HoraClaseAulaException - Excepción general
	 */
	@Override	
	public boolean grabar(Aula aula , List<HoraClase> listaHorasClase  ) throws  HoraClaseAulaException {
		Boolean retorno = false;
		if ((aula != null)&&(listaHorasClase!=null)) {
			try {
				Aula aulaAux= em.find(Aula.class, aula.getAlaId()) ;//busco el aula 
					for (HoraClase horaClase : listaHorasClase) {
					HoraClase horaClaseAux=em.find(HoraClase.class, horaClase.getHoclId());//busco objeto hora clase con id de las lista.
					HoraClaseAula horaClaseAulaAux = new HoraClaseAula();  //creo objeto nuevo
					horaClaseAulaAux.setHoclalAula(aulaAux);               //relacion con aula
					horaClaseAulaAux.setHoclalHoraClase(horaClaseAux);    //relacion con hora_clase
					horaClaseAulaAux.setHoclalEstado(HoraClaseAulaConstantes.ESTADO_ACTIVO_VALUE); 
					em.persist(horaClaseAulaAux);
					em.flush();
					retorno = true;
				} 
			}  catch (Exception e) {
				 
				throw new HoraClaseAulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.grabar.exception")));
			}
		} else {
			throw new HoraClaseAulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.grabar.null.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca si existe al meos una  entidad HoraClaseAula por  id del Aula y el id de hora clase 
	 * @param alaId - alaId id del aula a buscar
	 * @param hoclId - hoclId id del hora clase a buscar
	 * @return retorna la entidad hora clase aula buscada por los parametros ingresados. 
	 * @throws HoraClaseAulaNoEncontradoException  - excepción lanzada cuando no se encuentra hora clase aula
	 * @throws HoraClaseAulaValidacionException - cuando existen mas de un registro
	 * @throws HoraClaseAulaException - excepción generan lanzada
	 */
	@Override
	public HoraClaseAula buscarPalaIdPhoclId(Integer alaId, Integer hoclId) throws HoraClaseAulaNoEncontradoException, HoraClaseAulaValidacionException, HoraClaseAulaException  {
		HoraClaseAula retorno = null;
		if (alaId != null || hoclId != null) { //verifico que el id del aula y del hora clase no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select hoclal from HoraClaseAula hoclal ");
				sbsql.append(" where hoclal.hoclalAula.alaId =:alaId ");
				sbsql.append(" and hoclal.hoclalHoraClase.hoclId =:hoclId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("alaId", alaId);
				q.setParameter("hoclId", hoclId);
				retorno = (HoraClaseAula) q.getSingleResult(); 
			} catch (NoResultException e) {
				throw new HoraClaseAulaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.buscar.por.aula.horaClase.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new HoraClaseAulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.buscar.por.aula.horaClase.no.unique.exception")));
			} catch (Exception e) {
				throw new HoraClaseAulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseAula.buscar.por.aula.horaClase.exception")));
			} 
		}
		return retorno;
	}

	
}
	
	

