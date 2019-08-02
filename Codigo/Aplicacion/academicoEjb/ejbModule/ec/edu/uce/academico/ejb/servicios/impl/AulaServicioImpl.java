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

 ARCHIVO:     AulaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Aula. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-06-2017           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.excepciones.AulaException;
import ec.edu.uce.academico.ejb.excepciones.AulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AulaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AulaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.AulaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HoraClaseAulaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.Edificio;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;


/**
 * Clase (Bean) AulaServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
  public class AulaServicioImpl  implements AulaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws AulaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws AulaNoEncontradoException - Excepción lanzada si no se encontró la entidad a editar
	 * @throws AulaException - Excepción general
	*/
	@Override
	public Boolean editar(AulaDto entidad, List<HoraClaseAula> listaHoraClaseAula) throws AulaValidacionException, AulaException {
		Boolean retorno = false;
			if( (entidad != null)&&(listaHoraClaseAula!=null)) {
		    	try{
				    	if(verificarDescripcion(entidad, GeneralesConstantes.APP_EDITAR)&&verificarCodigo(entidad, GeneralesConstantes.APP_EDITAR)){  // verificar que no repita la descripcion del aula
				    		    Aula aulaAux = em.find(Aula.class, entidad.getAlaId());
				    		    //CAMPOS A CAMBIARSE
				    		    aulaAux.setAlaCodigo(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getAlaCodigo()).toUpperCase());
				    		    aulaAux.setAlaDescripcion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getAlaDescripcion()).toUpperCase());
				    		    aulaAux.setAlaCapacidad(entidad.getAlaCapacidad());
				    		    aulaAux.setAlaPiso(entidad.getAlaPiso());
				    		    aulaAux.setAlaTipo(entidad.getAlaTipo());
				    		    aulaAux.setAlaEstado(entidad.getAlaEstado());
								//ACTIVO E INACTIVO HORA_CLASE_AULA DEPENDIENDO DEL ESTADO DEL AULA
								if(entidad.getAlaEstado()==AulaConstantes.ESTADO_ACTIVO_VALUE){
									for (HoraClaseAula horaClaseAula : listaHoraClaseAula) {
										HoraClaseAula horaClaseAulaAux=em.find(HoraClaseAula.class, horaClaseAula.getHoclalId());
										horaClaseAulaAux.setHoclalEstado(HoraClaseAulaConstantes.ESTADO_ACTIVO_VALUE); 
									} 
								}else if(entidad.getAlaEstado()==AulaConstantes.ESTADO_INACTIVO_VALUE){
									for (HoraClaseAula horaClaseAula : listaHoraClaseAula) {
										HoraClaseAula horaClaseAulaAux=em.find(HoraClaseAula.class, horaClaseAula.getHoclalId());
										horaClaseAulaAux.setHoclalEstado(HoraClaseAulaConstantes.ESTADO_INACTIVO_VALUE); 
									} 
									
								}
								retorno = true;
								
				    	    }else{
				    	    	throw new AulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.editar.descripcion.codigo.duplicado.exception")));
				    	    } 	
				      }catch (AulaValidacionException e) {
				    	  //throw new AulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.editar.descripcion.codigo.duplicado.exception")));
								throw new AulaValidacionException(e.getMessage());
				      }	catch (Exception e) {
					         throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.editar.exception")));
			         }    
			     }  else {
			         throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.editar.null.exception")));
		          }
		return retorno;
	}
	
	
	

	
	/**
	 * Añade un Aula en la BD
	 * @return Si se añadio o no el Aula
	 * @throws AulaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws AulaException - Excepción general
	 */
	@Override	
	public Aula anadir(AulaDto entidad , Edificio edificio , List<HoraClase> listaHorasClase ) throws AulaValidacionException, AulaException {
		Aula retorno = null;
		if ((entidad != null)&&(edificio!=null)) {
			try {
				if (verificarDescripcion(entidad, GeneralesConstantes.APP_NUEVO)&&verificarCodigo(entidad, GeneralesConstantes.APP_NUEVO)) {
					Aula AuxEntidad = new Aula();
		            // seteo los parametros de la entidad  paralelo JPA  con los parametros del objetoDto(AulaDto) enviado como parametro
					AuxEntidad.setAlaCodigo(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getAlaCodigo()).toUpperCase());
					AuxEntidad.setAlaDescripcion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getAlaDescripcion()).toUpperCase());
					AuxEntidad.setAlaCapacidad(entidad.getAlaCapacidad());
					AuxEntidad.setAlaEstado(entidad.getAlaEstado());
					AuxEntidad.setAlaTipo(entidad.getAlaTipo());
					AuxEntidad.setAlaPiso(entidad.getAlaPiso());
					//Objetos 
				      AuxEntidad.setAlaEdificio(edificio);
					em.persist(AuxEntidad);
					
					Aula aulaAux= em.find(Aula.class, AuxEntidad.getAlaId()) ;//busco el aula 
					for (HoraClase horaClase : listaHorasClase) {
						HoraClase horaClaseAux=em.find(HoraClase.class, horaClase.getHoclId());//busco objeto hora clase con id de las lista.
						HoraClaseAula horaClaseAulaAux = new HoraClaseAula();  //creo objeto nuevo
						horaClaseAulaAux.setHoclalAula(aulaAux);               //relacion con aula
						horaClaseAulaAux.setHoclalHoraClase(horaClaseAux);    //relacion con hora_clase
						horaClaseAulaAux.setHoclalEstado(HoraClaseAulaConstantes.ESTADO_ACTIVO_VALUE); 
						em.persist(horaClaseAulaAux);
					} 
					retorno = AuxEntidad;
				} else {
				throw new AulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.anadir.descripcion.codigo.duplicado.exception")));
				}
			} catch (AulaValidacionException e) {
				 throw new AulaValidacionException(e.getMessage());
			 } catch (Exception e) {
				throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.anadir.exception")));
			}
		} else {
			throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.anadir.null.exception")));
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Realiza verificaciones para determinar si existe una entidad con la misma descripcion
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @throws AulaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws AulaException  - Excepcion lanzada no se controla el error
	 */
	@SuppressWarnings("unchecked")
	private Boolean verificarDescripcion(AulaDto entidad, int tipo) throws AulaValidacionException, AulaException {
		Boolean retorno = false;
	List<Aula> aulaAux = null;
	try {
			//verifico que no exista otra entidad con la misma descripcion
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select ala from Aula ala ");
			sbsql.append(" where translate(ala.alaDescripcion,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:descripcion ");
		    sbsql.append(" and ala.alaEdificio.edfId =:edfId ");
            
			if(tipo == GeneralesConstantes.APP_EDITAR){
			sbsql.append(" and ala.alaId <>:identificador ");
			}
		    Query q = em.createQuery(sbsql.toString());
   	        q.setParameter("descripcion", entidad.getAlaDescripcion().toUpperCase());
   	        q.setParameter("edfId", entidad.getEdfId());
		    if(tipo == GeneralesConstantes.APP_EDITAR){
			q.setParameter("identificador", entidad.getAlaId());
		    }
		                    //    Aula	aulaAux = (Aula)q.getSingleResult(); 
		                 aulaAux =  q.getResultList();  //Se usa si no se controla con No result	
                      	  if (aulaAux.isEmpty()){ 
                     		retorno = true;
                  	    }
                      	  else{
                       		retorno = false;
                       	  }
	} catch (NoResultException e) {
		retorno = true;
	}catch (NonUniqueResultException e) {
		throw new AulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.verificar.descripcion.no.unico.exception")));
	}catch (Exception e) {
		throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.verificar.descripcion.validacion.exception")));
	}
	return retorno;
	}
	
	
	/**
	 * Realiza verificaciones para determinar si existe una entidad Paralelo con el mismo Codigo
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @throws AulaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws AulaException  - Excepcion lanzada no se controla el error
	 */
	@SuppressWarnings("unchecked")
	private Boolean verificarCodigo(AulaDto entidad, int tipo) throws AulaValidacionException, AulaException{
		Boolean retorno = false;
		List<Aula> aulaAux = null;
	try {
			//verifico que no exista otra entidad con la misma descripcion
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select ala from Aula ala ");
			sbsql.append(" where translate(ala.alaCodigo,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:codigo ");
		    sbsql.append(" and ala.alaEdificio.edfId =:edfId ");
		    if(tipo == GeneralesConstantes.APP_EDITAR){
			sbsql.append(" and ala.alaId <>:identificador ");
			    }
		    Query q = em.createQuery(sbsql.toString());
		    q.setMaxResults(1);
   	        q.setParameter("codigo", entidad.getAlaCodigo().toUpperCase());
            q.setParameter("edfId", entidad.getEdfId());
		    if(tipo == GeneralesConstantes.APP_EDITAR){
			q.setParameter("identificador", entidad.getAlaId());
		    }
		    aulaAux =  q.getResultList();
   //       Aula	aulaAux = (Aula)q.getSingleResult(); 
   	  if (aulaAux.isEmpty()){
  		retorno = true;
	    }
   	  else{
    		retorno = false;
    	  }	
 	 		
	} catch (NoResultException e) {
		retorno = true;
	}catch (NonUniqueResultException e) {
		//retorno= false;
		throw new AulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.verificar.codigo.no.unico.exception")));
	}
	
	 catch (Exception e) {
		throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.verificar.codigo.validacion.exception")));
		}
	return retorno;
	}
	
	
	
	/**
	 * Elimina un Aula
	 * @param idAula - id del aula que se quiere eliminar
	 * @param listaHoraClaseAula - Lista de hora clase aula que se quiere eliminar
	 * @return true en caso de que se complete la operacion, false caso contrario
	 * @throws AulaValidacionException - lanzada cuando el id del aula es nulo
	 * @throws AulaNoEncontradoException - lanzada cuando no se encuentra el aula con el id enviado
	 * @throws AulaException - lanzada cuando se deconoce el error al buscar el aula
	 */
	public boolean eliminarAula(Integer idAula, List<HoraClaseAula> listaHoraClaseAula) throws AulaValidacionException, AulaNoEncontradoException, AulaException {
	boolean retorno = false;
		Aula Aula = new Aula();
	if(idAula != null){
			try {
				for (HoraClaseAula horaClaseAula : listaHoraClaseAula) {
				HoraClaseAula horaClaseAulaAux=em.find(HoraClaseAula.class, horaClaseAula.getHoclalId());
				em.remove(horaClaseAulaAux);
			    }
				Aula = buscarPorId(idAula);
				em.remove(Aula);
			retorno = true;
		} catch (NoResultException e) {
				throw new AulaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.eliminar.por.id.noEncontrado.exception",idAula)));
			}catch (NonUniqueResultException e) {
			throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.eliminar.por.id.no.resultado.unico.exception",idAula)));
			} catch (Exception e) {
				throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.eliminar.por.id.general.exception")));
			}
		}else{
		throw new AulaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.eliminar.por.id.validacion.exception")));
		}
	return retorno;
	}
	
	
	
	
	/**
	 * Busca una entidad Aula por su id
	 * @param id - del Aula a a buscar
	 * @return Aula con el id solicitado
	 * @throws AulaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Aula con el id solicitado
	 * @throws AulaException - Excepcion general
	 */
	@Override
	public Aula buscarPorId(Integer id) throws AulaNoEncontradoException, AulaException {
		Aula retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Aula.class,  id);
			} catch (NoResultException e) {
				
				throw new AulaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.buscar.por.id.noEncontrado.exception",id)));
			}catch (NonUniqueResultException e) {
				
				
				throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.buscar.por.id.no.resultado.unico.exception",id)));
			} catch (Exception e) {
				
						
				throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.buscar.por.id.general.exception")));
			}
		}
		return retorno;
	}
	
	
	
}
	
	

