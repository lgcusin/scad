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

 ARCHIVO:     MallaCurricularServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla MallaCurricular. 
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

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacionMalla;

/**
 * Clase (Bean)MallaCurricularServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class MallaCurricularServicioImpl implements MallaCurricularServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad MallaCurricular por su id
	 * @param id - de la MallaCurricular a buscar
	 * @return MallaCurricular con el id solicitado
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular con el id solicitado
	 * @throws MallaCurricularException - Excepcion general
	 */
	@Override
	public MallaCurricular buscarPorId(Integer id) throws MallaCurricularNoEncontradoException, MallaCurricularException {
		MallaCurricular retorno = null;
		if (id != null) {
			try {
				retorno = em.find(MallaCurricular.class, id);
			} catch (NoResultException e) {
				throw new MallaCurricularNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades MallaCurricular existentes en la BD
	 * @return lista de todas las entidades MallaCurricular existentes en la BD
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
	 * @throws MallaCurricularException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MallaCurricular> listarTodos() throws MallaCurricularNoEncontradoException , MallaCurricularException {
		List<MallaCurricular> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcr from MallaCurricular mlcr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new MallaCurricularNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades MallaCurricular por carrera por vigencia por estado
	 * @return lista de todas las entidades MallaCurricular por carrera por vigencia por estado
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
	 * @throws MallaCurricularException - Excepcion general
	 */
	@Override
	public MallaCurricular buscarXcarreraXvigenciaXestado(int crrId, int vigencia, int estado) throws MallaCurricularNoEncontradoException , MallaCurricularException {
		MallaCurricular retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcr from MallaCurricular mlcr where ");
			sbsql.append(" mlcr.mlcrCarrera.crrId =:crrId ");
			sbsql.append(" and mlcr.mlcrVigente =:vigencia ");
			sbsql.append(" and mlcr.mlcrEstado =:estado ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("vigencia", vigencia);
			q.setParameter("estado", estado);
			retorno = (MallaCurricular)q.getSingleResult();
		} catch (NoResultException e) {
			throw new MallaCurricularNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.carrera.por.vigencia.por.estado.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.carrera.por.vigencia.por.estado.non.unique.result.exception")));
		} catch (Exception e) {
			throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.carrera.por.vigencia.por.estado.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas las entidades MallaCurricular por codigo de malla
	 * @return lista de todas las entidades MallaCurricular por carrera por codigo de malla
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
	 * @throws MallaCurricularException - Excepcion general
	 */
	@Override
	public MallaCurricular buscarXcodigoMalla(String codigoMalla) throws MallaCurricularNoEncontradoException , MallaCurricularException {
		MallaCurricular retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcr from MallaCurricular mlcr where ");
			sbsql.append(" mlcr.mlcrCodigo =:codigoMalla ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("codigoMalla", codigoMalla);
			retorno = (MallaCurricular)q.getSingleResult();
		} catch (NoResultException e) {
			throw new MallaCurricularNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.codigo.malla.no.result.exception",codigoMalla)));
		}catch (NonUniqueResultException e) {
			throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.codigo.malla.non.unique.result.exception",codigoMalla)));
		} catch (Exception e) {
			throw new MallaCurricularException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.codigo.malla.exception")));
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad MallaCurricular
	 * @param entidad - entidad MallaCurricular a editar
	 * @return null si no se encuentró la entidad a editar, la entidad MallaCurricular editada de lo contrario
	 * @throws MallaCurricularException - excepcion de validacion de edicion
	 * @throws MallaCurricularNoEncontradoException - excepcion de validacion de edicion
	 */
	public MallaCurricular editar(MallaCurricularDto entidad, MallaPeriodo mlpr) throws MallaCurricularException , MallaCurricularNoEncontradoException{
		MallaCurricular retorno = null;
		
		if(entidad != null)	{
				try{
					Carrera crrAux = new Carrera();
					crrAux.setCrrId(entidad.getCrrId());
					TipoFormacionMalla tifrmlAux  = new TipoFormacionMalla();
					tifrmlAux.setTifrmlId(entidad.getTpfrmlId());
					MallaCurricular mlcrAux = em.find(MallaCurricular.class, entidad.getMlcrId());
					MallaPeriodo mlprAux = em.find(MallaPeriodo.class, mlpr.getMlprId());
					PeriodoAcademico pracAux = em.find(PeriodoAcademico.class, entidad.getPracId());
					
					mlcrAux.setMlcrCodigo(entidad.getMlcrCodigo().toUpperCase().replaceAll(" +", " ").trim());
					mlcrAux.setMlcrDescripcion(entidad.getMlcrDescripcion().toUpperCase().replaceAll(" +", " ").trim());
					mlcrAux.setMlcrEstado(entidad.getMlcrEstado());
					mlcrAux.setMlcrFechaInicio(entidad.getMlcrFechaInicio());
					mlcrAux.setMlcrFechaFin(entidad.getMlcrFechaFin());
					mlcrAux.setMlcrTotalHoras(entidad.getMlcrTotalHoras());
					mlcrAux.setMlcrTotalMaterias(entidad.getMlcrTotalMaterias());
					mlcrAux.setMlcrTipoOrgAprendizaje(entidad.getMlcrTipoOrgAprendizaje());
					mlcrAux.setMlcrVigente(entidad.getMlcrVigencia());
					mlcrAux.setMlcrTipoAprobacion(entidad.getMlcrTipoAprobacion());
					mlcrAux.setMlcrCarrera(crrAux);
					mlcrAux.setMlcrTipoFormacionMalla(tifrmlAux);
					
//					MallaPeriodo mlprAux = new MallaPeriodo();
					mlprAux.setMlprMallaCurricular(mlcrAux);
					mlprAux.setMlprPeriodoAcademico(pracAux);
					mlprAux.setMlprEstado(entidad.getMlcrEstado());
					
				} catch (NoResultException e) {
					throw new MallaCurricularNoEncontradoException("aaaaaaaaaa");
				}catch (NonUniqueResultException e){
					throw new MallaCurricularException("xxxxxxxx");
				}catch (Exception e) {
					throw new MallaCurricularException("Error desconocido");
				}
		}			
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad MallaCurricular
	 * @param entidad - entidad MallaCurricular a editar
	 * @return null si no se encuentró la entidad a editar, la entidad MallaCurricular editada de lo contrario
	 * @throws MallaCurricularException - excepcion de validacion de edicion
	 * @throws MallaCurricularNoEncontradoException - excepcion de validacion de edicion
	 */
	public MallaCurricular anadir(MallaCurricularDto entidad) throws MallaCurricularException , MallaCurricularNoEncontradoException{
		MallaCurricular retorno = null;

		try{
			PeriodoAcademico periodo = em.find(PeriodoAcademico.class, entidad.getPracId());
			Carrera carrera = em.find(Carrera.class, entidad.getCrrId());
			TipoFormacionMalla tipoFormacion  = em.find(TipoFormacionMalla.class, entidad.getTpfrmlId());
			
			MallaCurricular mallaCurricular = new MallaCurricular();
			mallaCurricular.setMlcrCodigo(entidad.getMlcrCodigo().toUpperCase().replaceAll(" +", " ").trim());
			mallaCurricular.setMlcrDescripcion(entidad.getMlcrDescripcion().toUpperCase().replaceAll(" +", " ").trim());
			mallaCurricular.setMlcrFechaInicio(entidad.getMlcrFechaInicio());
			mallaCurricular.setMlcrFechaFin(entidad.getMlcrFechaFin());
			mallaCurricular.setMlcrTotalHoras(entidad.getMlcrTotalCreditos());
			mallaCurricular.setMlcrTotalMaterias(entidad.getMlcrTotalMaterias());
			mallaCurricular.setMlcrTipoOrgAprendizaje(entidad.getMlcrTipoOrgAprendizaje());
			mallaCurricular.setMlcrVigente(entidad.getMlcrVigencia());
			mallaCurricular.setMlcrTipoAprobacion(entidad.getMlcrTipoAprobacion());
			mallaCurricular.setMlcrEstado(MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			mallaCurricular.setMlcrCarrera(carrera);
			mallaCurricular.setMlcrTipoFormacionMalla(tipoFormacion);
			
			em.persist(mallaCurricular);
			em.flush();
			
			//Para guardar la informacion en la tabla malla periodo
			MallaPeriodo mallaPeriodo = new MallaPeriodo();
			mallaPeriodo.setMlprEstado(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			mallaPeriodo.setMlprPeriodoAcademico(periodo);
			mallaPeriodo.setMlprMallaCurricular(mallaCurricular);
			
			em.persist(mallaPeriodo);
			em.flush();
		} catch (NoResultException e) {
			throw new MallaCurricularNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.id.no.result.exception")));
		}catch (NonUniqueResultException e){
			throw new MallaCurricularException("Error al guardar malla curricular, no unique exception.");
		}catch (Exception e) {
			throw new MallaCurricularException("Error desconocido al guardar malla curricular.");
		}
		
		return retorno;
	}
	
	
	public MallaCurricular editar(MallaCurricularDto entidad) throws MallaCurricularException , MallaCurricularNoEncontradoException{

		MallaCurricular retorno = null;

		try{

			MallaCurricular mallaCurricular = em.find(MallaCurricular.class, entidad.getMlcrId());
			mallaCurricular.setMlcrCodigo(entidad.getMlcrCodigo().toUpperCase().replaceAll(" +", " ").trim());
			mallaCurricular.setMlcrDescripcion(entidad.getMlcrDescripcion().toUpperCase().replaceAll(" +", " ").trim());
			mallaCurricular.setMlcrEstado(entidad.getMlcrEstado());
			mallaCurricular.setMlcrFechaInicio(entidad.getMlcrFechaInicio());
			mallaCurricular.setMlcrFechaFin(entidad.getMlcrFechaFin());
			mallaCurricular.setMlcrTotalHoras(entidad.getMlcrTotalCreditos());
			mallaCurricular.setMlcrTotalMaterias(entidad.getMlcrTotalMaterias());
			mallaCurricular.setMlcrTipoOrgAprendizaje(entidad.getMlcrTipoOrgAprendizaje());
			mallaCurricular.setMlcrVigente(entidad.getMlcrVigencia());
			mallaCurricular.setMlcrTipoAprobacion(entidad.getMlcrTipoAprobacion());

		} catch (NoResultException e) {
			throw new MallaCurricularNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricular.buscar.por.id.no.result.exception")));
		}catch (NonUniqueResultException e){
			throw new MallaCurricularException("Error al guardar malla curricular, no unique exception.");
		}catch (Exception e) {
			throw new MallaCurricularException("Error desconocido al guardar malla curricular.");
		}
		
		return retorno;
	}
	
	
	
}
