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

 ARCHIVO:     CarreraIntercambioServicioServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CarreraIntercambioServicio. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 05-Octubre-2018           Daniel Ortiz                       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaIntercambioServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.CarreraIntercambio;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.MateriaIntercambio;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;



/**
 * Clase (Bean)CarreraIntercambioServicioImpl.
 * Bean declarado como Stateless.
 * @author dortiz
 * @version 1.0
 */

@Stateless
public class MatriculaIntercambioServicioImpl implements MatriculaIntercambioServicio {
	
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/**
	 * Busca una entidad CarreraIntercambio por su id
	 * @param id - de la CarreraIntercambio  a buscar
	 * @return CarreraIntercambio con el id solicitado
	 * @throws CarreraIntercambioNoEncontradoException - Excepcion lanzada cuando no se encuentra una CarreraIntercambio con el id solicitado
	 * @throws CarreraIntercambioException - Excepcion general
	 */
	@Override
	public CarreraIntercambio buscarPorId(Integer id)
			throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException {
		CarreraIntercambio retorno=null;
		if (id != null) {
			try {
				retorno = em.find(CarreraIntercambio.class, id);
			} catch (NoResultException e) {
				throw new CarreraIntercambioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CarreraIntercambioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CarreraIntercambioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.buscar.por.id.exception")));
			}
		}
		return retorno;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CarreraIntercambio> listarTodos()
			throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException {
		List<CarreraIntercambio> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT c FROM CarreraIntercambio c ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraIntercambioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraIntercambioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.listar.todos.exception")));
		}
		return retorno;
	}

	@Override
	public Boolean editar(CarreraIntercambio entidad)
			throws CarreraIntercambioValidacionException, CarreraIntercambioException {
		Boolean retorno = false;
		if (entidad != null) {
			try {
			 
				    // BUSCO LA MATERIA A MODIFICAR
					CarreraIntercambio CarreraIntercambioAux = em.find(CarreraIntercambio.class, entidad.getCrinId());

					// CAMPOS A GUARDARSE
					CarreraIntercambioAux.setCrinCarrera(entidad.getCrinCarrera());
					CarreraIntercambioAux.setCrinFichaInscripcion(entidad.getCrinFichaInscripcion());
					CarreraIntercambioAux.setCrinAutorizacion(entidad.getCrinAutorizacion());
					CarreraIntercambioAux.setCrinUserRegistro(entidad.getCrinUserRegistro());
					CarreraIntercambioAux.setCrinFechaRegistro(entidad.getCrinFechaRegistro());
					CarreraIntercambioAux.setCrinObservacion(entidad.getCrinObservacion());
					CarreraIntercambioAux.setCrinEstado(entidad.getCrinEstado());
				
					retorno = true; // retorno verdadero si se realiza la actualizacion correctamente
				 
			} catch (Exception e) {
				throw new CarreraIntercambioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.editar.exception")));
			}
		} else {
			throw new CarreraIntercambioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.editar.null.exception")));
		}
		return retorno;
		
	}

	@SuppressWarnings("unchecked")
	public Boolean guardar(Map<CarreraDto, List<MateriaDto>> materiasPorCrr, List<FichaInscripcionDto> fichasInscripcion, Usuario usuario) throws CarreraIntercambioValidacionException, CarreraIntercambioException {
		Boolean retorno = null;
		
		try {
			
			for (FichaInscripcionDto item : fichasInscripcion) {
				List<CarreraIntercambio> carrerrasInt = new ArrayList<>();
				Query sql1 = em.createNamedQuery("CarreraIntercambio.findPorFichaInscripcion");
				sql1.setParameter("fcinId", item.getFcinId());
				carrerrasInt = sql1.getResultList();
				
				if (carrerrasInt.size() > 0) {
					for (CarreraIntercambio it1 : carrerrasInt) {
						List<MateriaIntercambio> materiasInt = new ArrayList<>();
						Query sql2 = em.createNamedQuery("MateriaIntercambio.findPorCarreraIntercambio");
						sql2.setParameter("crinId", it1.getCrinId());
						materiasInt = sql2.getResultList();
						
						if (materiasInt.size() > 0) {
							for (MateriaIntercambio it2 : materiasInt) {
								if (it2.getMtinEstado().equals(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE)) {
									em.remove(it2);									
								}
							}
						}
					}
					
					for (CarreraIntercambio it1 : carrerrasInt) {
						if (it1.getCrinEstado().equals(CarreraConstantes.CRR_ESTADO_ACTIVO_VALUE)) {
							em.remove(it1);									
						}
						
					}
				}
				
			}
			
			materiasPorCrr.forEach((k,v)->{
				
				CarreraIntercambio carreraIntercambio = new CarreraIntercambio();
				Carrera carrera = new Carrera();
				FichaInscripcion fichaInscripcion = new FichaInscripcion();
				
				carrera.setCrrId(k.getCrrId());
				fichaInscripcion.setFcinId(k.getFcinId());
				carreraIntercambio.setCrinCarrera(carrera);
				carreraIntercambio.setCrinFichaInscripcion(fichaInscripcion);
				carreraIntercambio.setCrinFechaRegistro(Timestamp.from(Instant.now()));
				carreraIntercambio.setCrinUserRegistro(usuario.getUsrNick());
				carreraIntercambio.setCrinEstado(k.getCrrEstado());
				//agrego crin
				em.persist(carreraIntercambio);
				em.flush();
				
				v.forEach(item -> {
					
					MateriaIntercambio materiaIntercambio = new MateriaIntercambio();
					Materia materia = new Materia();
					materia.setMtrId(item.getMtrId());
					materiaIntercambio.setMtinMateria(materia);
					materiaIntercambio.setMtinCarreraIntercambio(carreraIntercambio);
					materiaIntercambio.setMtinCodigo(item.getMtrCodigo());
					materiaIntercambio.setMtinDescripcion(item.getMtrDescripcion());
					materiaIntercambio.setMtinEstado(item.getMtrEstado());
					
					//agrego mtin
					em.persist(materiaIntercambio);
					em.flush();
					
				});
				
			});
			
			retorno = Boolean.TRUE;
		} catch (EntityNotFoundException e){
			throw new CarreraIntercambioValidacionException();
		} catch (Exception e) {
			throw new CarreraIntercambioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraIntercambio.anadir.exception")));
		}
		
		return retorno; 
	}

	
	public Boolean eliminar(MateriaDto entidad) throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException{
		return true;
	}
}