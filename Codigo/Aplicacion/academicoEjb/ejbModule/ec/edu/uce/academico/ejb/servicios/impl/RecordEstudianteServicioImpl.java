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

 ARCHIVO:     RecordEstudianteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla RecordEstudiante. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-OCT-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;

/**
 * Clase (Bean)RecordEstudiante.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class RecordEstudianteServicioImpl implements RecordEstudianteServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	
	/**
	 * Busca una entidad RecordEstudiante por su id
	 * @param id - de la RecordEstudiante a buscar
	 * @return RecordEstudiante con el id solicitado
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una RecordEstudiante con el id solicitado
	 * @throws RecordEstudianteException - Excepcion general
	 */
	@Override
	public RecordEstudiante buscarPorId(Integer id) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		RecordEstudiante retorno = null;
		if (id != null) {
			try {
				retorno = em.find(RecordEstudiante.class, id);
			} catch (NoResultException e) {
				throw new RecordEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	@Override
	public RecordEstudiante buscarPorIdSingle(Integer id) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		RecordEstudiante retorno = null;
		if (id != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select rces from RecordEstudiante rces WHERE ROWNUM = 1");
				sbsql.append(" and rcesId= :id");
				Query q = em.createQuery(sbsql.toString());
				 q.setParameter("id", id);
				retorno = (RecordEstudiante) q.getSingleResult();
			} catch (NoResultException e) {
				throw new RecordEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas las entidades RecordEstudiante existentes en la BD
	 * @return lista de todas las entidades RecordEstudiante existentes en la BD
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una RecordEstudiante 
	 * @throws RecordEstudianteException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecordEstudiante> listarTodos() throws RecordEstudianteNoEncontradoException , RecordEstudianteException {
		List<RecordEstudiante> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rces from RecordEstudiante rces ");
//			sbsql.append(" where dtmt.crrTipo =:tipoRecordEstudiante ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new RecordEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.todos.exception")));
		}
		return retorno;
	}


	/**MQ
	 * Lista todas las entidades RecordEstudiante existentes en la BD, por fichaEstudiante
	 * @return lista de todas las entidades RecordEstudiante existentes en la BD, por fichaEstudiante
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra record estudiante
	 * @throws RecordEstudianteException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecordEstudiante> listarXfcesId(Integer fcesId) throws  RecordEstudianteException {
		List<RecordEstudiante> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rces from RecordEstudiante rces ");
			sbsql.append(" where rces.rcesFichaEstudiante.fcesId =:fcesId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("fcesId", fcesId);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.por.ficha.estudiante.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			retorno = null;
			//throw new RecordEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.por.ficha.estudiante.no.encontrado.exception")));
		}
		
		return retorno;
	}	
	
	/**@author Daniel
	 * Lista todas las entidades RecordEstudiante existentes en la BD del nivel más alto, por prsIdentificacion 
	 * @return Lista todas las entidades RecordEstudiante existentes en la BD del nivel más alto del estudiante
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra record estudiante
	 * @throws RecordEstudianteException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecordEstudiante> buscarEstadoMateriasActualesPosgrado(String prsIdentificacion, Integer nivelId) throws RecordEstudianteNoEncontradoException  {
		List<RecordEstudiante> retorno = null;
		try {
			retorno = new ArrayList<RecordEstudiante>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rces from RecordEstudiante rces, FichaEstudiante fces, Persona prs, MallaCurricularParalelo mlcrpr,"
					+ " MallaCurricularMateria mlcrmt ");
			sbsql.append(" where rces.rcesFichaEstudiante.fcesId = fces.fcesId ");
			sbsql.append(" and fces.fcesPersona.prsId = prs.prsId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
//			sbsql.append(" and mlcrmt.mlcrmtNivel.nvlId = nvl.nvlId ");
			sbsql.append(" and prs.prsIdentificacion  =:prsIdentificacion ");
//			sbsql.append(" and nvl.nvlId  =:nivelId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("prsIdentificacion", prsIdentificacion);
//			q.setParameter("nivelId", nivelId);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new RecordEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.por.ficha.estudiante.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			retorno = null;
			//throw new RecordEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.por.ficha.estudiante.no.encontrado.exception")));
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void registrarNuevoNivelPosgrado(Integer fcesId,Integer nivelId, Integer pracId)   {
		try {
			List<MallaCurricularParalelo> listaMallaCurriculares= new ArrayList<MallaCurricularParalelo>();
			MallaPeriodo mlprNivelActual = new MallaPeriodo();
			// Busco la MallaPeriodo para buscar el siguiente paralelo a matricular
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlpr from RecordEstudiante rces, FichaEstudiante fces, MallaCurricularParalelo mlcrpr,"
					+ " MallaCurricularMateria mlcrmt, Nivel nvl , PeriodoAcademico prac, MallaCurricular mlcr, MallaPeriodo mlpr");
			sbsql.append(" where rces.rcesFichaEstudiante.fcesId = fces.fcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrmt.mlcrmtNivel.nvlId = nvl.nvlId ");
			sbsql.append(" and mlcrmt.mlcrmtMallaCurricular.mlcrId = mlcr.mlcrId ");
			sbsql.append(" and mlpr.mlprMallaCurricular.mlcrId  = mlcr.mlcrId");
			sbsql.append(" and mlpr.mlprPeriodoAcademico.pracId  = prac.pracId");
			sbsql.append(" and fces.fcesId.fcesId  = :fcesId");
			sbsql.append(" and mlpr.mlprPeriodoAcademico.pracId  = :pracId");
			sbsql.append(" and nvl.nvlId  =:nivelId ");
			Query q = em.createQuery(sbsql.toString()).setMaxResults(1);
			q.setParameter("fcesId", fcesId);
			q.setParameter("pracId", pracId);
			q.setParameter("nivelId", nivelId);
			mlprNivelActual = (MallaPeriodo) q.getSingleResult();
			// Busco las mallaCurricularesParalelo del nivel siguiente
			sbsql = new StringBuffer();
			sbsql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr,"
					+ " MallaCurricularMateria mlcrmt, Nivel nvl , PeriodoAcademico prac, MallaCurricular mlcr, MallaPeriodo mlpr, Paralelo prl");
			sbsql.append(" where mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrmt.mlcrmtNivel.nvlId = nvl.nvlId ");
			sbsql.append(" and mlcrmt.mlcrmtMallaCurricular.mlcrId = mlcr.mlcrId ");
			sbsql.append(" and mlpr.mlprMallaCurricular.mlcrId  = mlcr.mlcrId");
			sbsql.append(" and mlpr.mlprPeriodoAcademico.pracId  = prac.pracId");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId  = prl.prlId");
			sbsql.append(" and mlpr.mlprId  = :mlprId");
			sbsql.append(" and nvl.nvlId  =:nivelId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId  =:pracId ");
			q = em.createQuery(sbsql.toString());
			q.setParameter("mlprId", mlprNivelActual.getMlprId());
			q.setParameter("nivelId", nivelId+1);
			q.setParameter("pracId", pracId);
			listaMallaCurriculares = q.getResultList();
			
			ComprobantePago cmpaAux = new ComprobantePago();
			//Busco el comprobante de pago asignado al posgrado
			sbsql = new StringBuffer();
			sbsql.append(" Select cmpa from DetalleMatricula dtmt, FichaEstudiante fces, MallaCurricularParalelo mlcrpr,"
					+ " MallaCurricularMateria mlcrmt, Nivel nvl , PeriodoAcademico prac, MallaCurricular mlcr, MallaPeriodo mlpr, FichaMatricula fcmt, ComprobantePago cmpa");
			sbsql.append(" where fcmt.fcmtFichaEstudiante.fcesId = fces.fcesId ");
			sbsql.append(" and dtmt.dtmtMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and dtmt.dtmtComprobantePago.cmpaId = cmpa.cmpaId ");
			sbsql.append(" and cmpa.cmpaFichaMatricula.fcmtId = fcmt.fcmtId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrmt.mlcrmtNivel.nvlId = nvl.nvlId ");
			sbsql.append(" and mlcrmt.mlcrmtMallaCurricular.mlcrId = mlcr.mlcrId ");
			sbsql.append(" and mlpr.mlprMallaCurricular.mlcrId  = mlcr.mlcrId");
			sbsql.append(" and mlpr.mlprPeriodoAcademico.pracId  = prac.pracId");
			sbsql.append(" and fces.fcesId.fcesId  = :fcesId");
			sbsql.append(" and mlpr.mlprPeriodoAcademico.pracId  = :pracId");
			sbsql.append(" and nvl.nvlId  =:nivelId ");
			q = em.createQuery(sbsql.toString()).setMaxResults(1);
			q.setParameter("fcesId", fcesId);
			q.setParameter("pracId", pracId);
			q.setParameter("nivelId", nivelId);
			cmpaAux = (ComprobantePago) q.getSingleResult();
			FichaEstudiante fcesAux = new FichaEstudiante();
			fcesAux= em.find(FichaEstudiante.class, fcesId);
			//Creo los nuevos registros del siguiente nivel
			for (MallaCurricularParalelo item : listaMallaCurriculares) {
				RecordEstudiante rcesAux = new RecordEstudiante();
				rcesAux.setRcesFichaEstudiante(fcesAux);
				rcesAux.setRcesMallaCurricularParalelo(item);
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
				em.persist(rcesAux);
				em.flush();
				DetalleMatricula dtmtAux = new DetalleMatricula();
				dtmtAux.setDtmtComprobantePago(cmpaAux);
				dtmtAux.setDtmtMallaCurricularParalelo(item);
				dtmtAux.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
				dtmtAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(dtmtAux);
				em.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**@author Daniel
	 * Actualiza todos los recordEstudiante a Matriculado por fichaEstudiante y MallaCurricularParalelo
	 * @return 
	 */
	@Override
	public void actualizaMatriculadoPorFcesIdPorMlCrPrId(Integer fcesId, Integer mlcrprId) throws  RecordEstudianteException {
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Update RecordEstudiante set rcesEstado = ");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbsql.append(" where rcesFichaEstudiante.fcesId =:fcesId ");
			sbsql.append(" and rcesMallaCurricularParalelo.mlcrprId =:mlcrprId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcesId", fcesId);
			q.setParameter("mlcrprId", mlcrprId);
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.por.ficha.estudiante.exception")));
		}
	}
	
	@Override
	public void actualizaEstadoRces(Integer rcesId, Integer estado) throws  RecordEstudianteException {
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Update RecordEstudiante set rcesEstado = ");sbsql.append(estado);
			sbsql.append(" where rcesId =:rcesId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("rcesId", rcesId);
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudiante.listar.por.ficha.estudiante.exception")));
		}
	}
	
}
