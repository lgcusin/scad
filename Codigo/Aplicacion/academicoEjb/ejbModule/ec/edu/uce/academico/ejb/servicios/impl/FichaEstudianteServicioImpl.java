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

 ARCHIVO:     FichaEstudianteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla FichaEstudiante. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;

/**
 * Clase (Bean)FichaEstudianteServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class FichaEstudianteServicioImpl implements FichaEstudianteServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad FichaEstudiante por su id
	 * @param id - de la FichaEstudiante a buscar
	 * @return FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteException - Excepcion general
	 */
	@Override
	public FichaEstudiante buscarPorId(Integer id) throws FichaEstudianteNoEncontradoException, FichaEstudianteException {
		FichaEstudiante retorno = null;
		if (id != null) {
			try {
				retorno = em.find(FichaEstudiante.class, id);
			} catch (NoResultException e) {
				throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FichaEstudiante> listarTodos() throws FichaEstudianteNoEncontradoException , FichaEstudianteException {
		List<FichaEstudiante> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	//TODO: MODIFICAR EL COMENTARIO
	public FichaEstudiante buscarXpersonaId(int personaId) throws FichaEstudianteNoEncontradoException , FichaEstudianteException {
		FichaEstudiante retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces ");
			sbsql.append(" where fces.fcesPersona.prsId =:personaId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("personaId", personaId);
			retorno = (FichaEstudiante)q.getSingleResult();
		} catch (NoResultException e) {
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	public void cambiarNivelacionFichaEstudiante(int fcesId,FichaInscripcion fcin) throws FichaEstudianteNoEncontradoException , FichaEstudianteException {
		try {
			if(fcesId!=0&&fcin!=null){
				FichaEstudiante fcesaux = em.find(FichaEstudiante.class, fcesId);
				fcesaux.setFcesFichaInscripcion(fcin);
			}
		} catch (NoResultException e) {
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
	}
	public FichaEstudiante buscarPorFcinId(Integer fcinId) throws FichaEstudianteNoEncontradoException , FichaEstudianteException {
		FichaEstudiante retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces ");
			sbsql.append(" where fces.fcesFichaInscripcion.fcinId =:fcinId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcinId", fcinId.intValue());
			retorno = (FichaEstudiante)q.getSingleResult();
		} catch (NoResultException e) {
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	
	public FichaEstudiante buscarPorFcinIdNueva(Integer fcinId) throws  FichaEstudianteException {
		FichaEstudiante retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces ");
			sbsql.append(" where fces.fcesFichaInscripcion.fcinId =:fcinId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcinId", fcinId.intValue());
			retorno = (FichaEstudiante)q.getSingleResult();
		} catch (NoResultException e) {
			return null;
			//throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	@Override
	public void editarObservacionTerceraMatricula(Integer fcesId) throws FichaEstudianteNoEncontradoException , FichaEstudianteException {
		FichaEstudiante fcesAux = em.find(FichaEstudiante.class, fcesId);
		fcesAux.setFcesObservacion(FichaEstudianteConstantes.ESTADO_TERCERA_MATRICULA_REQUIERE_APROBACION_LABEL);
		em.merge(fcesAux);
		em.flush();
		
	}
	
	@Override
	public void habilitarObservacionTerceraMatricula(Integer fcesId) throws FichaEstudianteNoEncontradoException , FichaEstudianteException {
		FichaEstudiante fcesAux = em.find(FichaEstudiante.class, fcesId);
		fcesAux.setFcesObservacion(FichaEstudianteConstantes.ESTADO_TERCERA_MATRICULA_APROBADO_LABEL);
		em.merge(fcesAux);
		em.flush();
		
	}
	
	
	/**MQ
	 * Lista todas las entidades FichaEstudiante existentes en la BD por fcinId
	  * @param id - de la FichaInscripcion a buscar
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FichaEstudiante> listarPorFcinId(Integer fcinId) throws FichaEstudianteException {
		List<FichaEstudiante> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces ");
			sbsql.append(" where fces.fcesFichaInscripcion.fcinId =:fcinId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcinId", fcinId.intValue());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			retorno = null;
			//throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	public FichaEstudiante buscarFichaEstudiantePorFcinId(Integer fcinId) throws FichaEstudianteException {
		FichaEstudiante retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces ");
			sbsql.append(" where fces.fcesFichaInscripcion.fcinId =:fcinId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcinId", fcinId.intValue());
			retorno = (FichaEstudiante) q.getSingleResult();
		} catch (NoResultException e) {
			retorno = null;
			//throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.no.result.exception")));
		} catch (Exception e) {
			retorno = null;
//			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**MQ
	 * Editar FichaEstudiante existentes en la BD para cambiar campos de segunda carrera
	  * @param estudianteDto - estudiante a editar 
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */

	public Boolean editarFcesPorSegundaCarrera(EstudianteJdbcDto estudianteDto) {

		Boolean retorno = false;
		if (estudianteDto != null) {
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
        	fichaEstudiante = em.find(FichaEstudiante.class, estudianteDto.getFcesId()); // buscamos la ficha estudiante existente para editar

			if (estudianteDto.getFcesUnivEstudPrevId() != GeneralesConstantes.APP_ID_BASE) {
				fichaEstudiante.setFcesUnivEstudPrevId(estudianteDto.getFcesUnivEstudPrevId());
			}

			if (estudianteDto.getFcesTipoUnivEstudPrev() != GeneralesConstantes.APP_ID_BASE) {
				fichaEstudiante.setFcesTipoUnivEstudPrev(estudianteDto.getFcesTipoUnivEstudPrev());
			}
			if (estudianteDto.getFcesEstadoEstudPrev() != GeneralesConstantes.APP_ID_BASE) {
				fichaEstudiante.setFcesEstadoEstudPrev(estudianteDto.getFcesEstadoEstudPrev());
			}

			if (estudianteDto.getFcesTitEstudPrevId() != GeneralesConstantes.APP_ID_BASE) {
				fichaEstudiante.setFcesTitEstudPrevId(estudianteDto.getFcesTitEstudPrevId());
			}else{
				fichaEstudiante.setFcesTitEstudPrevId(null);
			}
				fichaEstudiante.setFcesRegTituloPrev(estudianteDto.getFcesRegTituloPrev());
			retorno = true;
		}
		
		return retorno;
	}
	
	@Override
	public void editarFcesPorRetorno(List<FichaInscripcionDto> registro) {
			for (FichaInscripcionDto fichaInscripcionDto : registro) {
				try {
					FichaEstudiante fcesAux = buscarFichaEstudiantePorFcinId(fichaInscripcionDto.getFcinId());	
					
					if(fcesAux!=null){
						FichaInscripcion fcinAntigua = em.find(FichaInscripcion.class, registro.get(registro.size()-1).getFcinId());
						fcinAntigua.setFcinEstado(0);
						fcesAux.setFcesFichaInscripcion(fcinAntigua);
						em.merge(fcesAux);
						em.flush();
						Query query = em.createQuery(
							      "DELETE FROM FichaInscripcion fcin WHERE fcin.fcinId = :fcin");
						query.setParameter("fcin", fichaInscripcionDto.getFcinId()).executeUpdate();
						em.merge(fcinAntigua);
						em.flush();	
						break;
					}else{
						Query query = em.createQuery(
							      "DELETE FROM FichaInscripcion fcin WHERE fcin.fcinId = :fcin");
						query.setParameter("fcin", fichaInscripcionDto.getFcinId()).executeUpdate();
					}
				} catch (Exception e) {
				}
			}
	}
	
}
