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

 ARCHIVO:     MallaCurricularParaleloServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla MallaCurricularParalelo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 14-AGOS-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;

/**
 * Clase (Bean)MallaCurricularParaleloServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class MallaCurricularParaleloServicioImpl implements MallaCurricularParaleloServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca la entidades MallaCurricularParalelo por id de la malla curricular materia
	 * @param idMallaMateria - idMallaMateria id de la malla curricular materia
	 * @return verdadero si encuentra falso caso contrario
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricularParalelo 
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	@Override
	public boolean buscarXMallaMateria(int idMallaMateria) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		boolean retorno = false;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr where ");
			sbsql.append(" mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId =:idMallaMateria ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("idMallaMateria", idMallaMateria);
			q.getSingleResult();
			retorno = true;
		} catch (NoResultException e) {
			retorno = false;
		}catch (NonUniqueResultException e) {
			retorno = true;
		} catch (Exception e) {
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.exception")));
		}
		return retorno;
	}
	
	/**
	 * Eliminaa la entidad MallaCurricularParalelo por id y la elimina
	 * @param idMallaCurricularParalelo - idMallaMateria id de la malla curricular paralelo
	 * @return verdadero si encuentra falso caso contrario
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricularParalelo 
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	@Override
	public boolean eliminarPmlcrprId(int idMallaCurricularParalelo) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		boolean retorno = false;
		try {
			MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, idMallaCurricularParalelo);
			if(mlcrprAux != null){
				em.remove(mlcrprAux);
				em.flush();
				retorno = true;
			}else{
				retorno = false;
			}
		} catch (NoResultException e) {
			retorno = false;
		} catch (Exception e) {
			e.printStackTrace();
//			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.eliminar.por.id.malla.curricular.paralelo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca la entidades MallaCurricularParalelo por id de la malla curricular materia
	 * @param idMallaMateria - idMallaMateria id de la malla curricular materia
	 * @return verdadero si encuentra falso caso contrario
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricularParalelo 
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MallaCurricularParalelo> buscarXParaleloId(int idParalelo) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		List<MallaCurricularParalelo> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr where ");
			sbsql.append(" mlcrpr.mlcrprParalelo.prlId =:idParalelo ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("idParalelo", idParalelo);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.paralelo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca la entidades MallaCurricularParalelo por id de la malla curricular materia
	 * @param idMallaMateria - idMallaMateria id de la malla curricular materia
	 * @return verdadero si encuentra falso caso contrario
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricularParalelo 
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	@Override
	public MallaCurricularParalelo buscarXParaleloIdXMateriaId(Integer mtrId, Integer prlPracId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr , MallaCurricularMateria mlcrmt where ");
			sbsql.append(" mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrmt.mlcrmtMateria.mtrId = :mtrId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlPeriodoAcademico.pracId = :prlPracId");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("mtrId", mtrId);
			q.setParameter("prlPracId", prlPracId);
			retorno = (MallaCurricularParalelo) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.materia.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad MallaCurricularParalelo por su id
	 * @param id - de la malla curricular paralelo a buscar
	 * @return MallaCurricularParalelo con el id solicitado
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una malla curricular paralelo con el id solicitado
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	
	public MallaCurricularParalelo buscarPorId(Integer prlId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;
		if (prlId != null ) {
			try {
				retorno = em.find(MallaCurricularParalelo.class, prlId);
			} catch (NoResultException e) {
				throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.no.result.exception",prlId)));
			}catch (NonUniqueResultException e) {
				throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.non.unique.result.exception",prlId)));
			} catch (Exception e) {
				throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public MallaCurricularParalelo buscarPorMlcrprIdPorMtrId(Integer prlId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;
		if (prlId != null ) {
			try {
				retorno = em.find(MallaCurricularParalelo.class, prlId);
			} catch (NoResultException e) {
				throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.no.result.exception",prlId)));
			}catch (NonUniqueResultException e) {
				throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.non.unique.result.exception",prlId)));
			} catch (Exception e) {
				throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public MallaCurricularParalelo buscarPorMlcrmtPorPrlId(Integer mlcrmt,Integer prlId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr  where ");
			sbsql.append(" mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId =:mlcrmt ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId =:prlId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("mlcrmt", mlcrmt);
			q.setParameter("prlId", prlId);
			retorno = (MallaCurricularParalelo) q.getSingleResult();
		} catch (NoResultException e) {
			throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.no.result.exception")));
		}catch (Exception e) {
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.exception")));
		}
		return retorno;
	}

	@Override
	public void editarCupoPorMlcrprId(Integer mlcrprId, Integer nuevoCupo) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException {
		try {
			MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, mlcrprId);
			mlcrprAux.setMlcrprCupo(nuevoCupo);
			em.merge(mlcrprAux);
			em.flush();
		} catch (NoResultException e) {
			 throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.exception")));
		}catch (Exception e) {
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.exception")));
		}
		
	}
	
	@Override
	public void editarCupoPorMlcrprIdReserva(Integer mlcrprId, Integer nuevoCupo)
			throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException {
		try {
			MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, mlcrprId);
			mlcrprAux.setMlcrprReservaRepetidos(nuevoCupo);
			em.merge(mlcrprAux);
			em.flush();
		} catch (NoResultException e) {
			 throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.exception")));
		}catch (Exception e) {
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.exception")));
		}
		
	}
	
	@Override
	public MallaCurricularParalelo buscarPorMtrIdPrsIdentificacion(Integer materiaId,String cedula, Integer nivelId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;
//		PreparedStatement pstmt = null;
//		Connection con = null;
//		ResultSet rs = null;
		try {
//			select * from  malla_curricular_paralelo mlcrpr
//			, malla_curricular_materia mlcrmt, materia mtr , paralelo prl where
//			mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id
//			and mtr.mtr_id in(
//			select mtr_sub_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr
//			, malla_curricular_materia mlcrmt, materia mtr
//			where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id
//			and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id
//			and mtr.mtr_id=6883 and prs.prs_identificacion = '1704417730')
//			and mlcrmt.nvl_id=6 and prl.prl_id = mlcrpr.prl_id
//			and prl.prl_id in (
//			select prl.prl_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr
//			, malla_curricular_materia mlcrmt, materia mtr, paralelo prl
//			where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id
//			and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id
//			and mtr.mtr_id=6883 and prs.prs_identificacion = '1704417730' and mlcrpr.prl_id=prl.prl_id
//			)
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" in (");
sbSql.append(" SELECT ");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ?");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ?");
			
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr, MallaCurricularMateria mlcrmt, Materia mtr, Paralelo prl ");
			sbsql.append(" where mlcrmt.mlcrmtMateria.mtrId = mtr.mtrId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			
			sbsql.append(" and mtr.mtrId in ");
			sbsql.append(" (Select mtr.mtrMateria.mtrId from MallaCurricularParalelo mlcrpr, MallaCurricularMateria mlcrmt, Materia mtr,"
					+ " CargaHoraria crhr, DetallePuesto dtps, FichaDocente fcdc, Persona prs  where ");
			sbsql.append(" mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mtr.mtrId ");
			sbsql.append(" and crhr.crhrMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and crhr.crhrDetallePuesto.dtpsId = dtps.dtpsId ");
			sbsql.append(" and dtps.dtpsFichaDocente.fcdcId = fcdc.fcdcId ");
			sbsql.append(" and fcdc.fcdcPersona.prsId = prs.prsId ");
			sbsql.append(" and mlcrmt.mlcrmtNivel.nvlId = :nivelId ");
			sbsql.append(" and prs.prsIdentificacion = :cedula ");
			sbsql.append(" and mtr.mtrId = :materiaId ) ");
			sbsql.append(" and prl.prlId in  ");
			sbsql.append(" (Select prl.prlId from MallaCurricularParalelo mlcrpr, MallaCurricularMateria mlcrmt, Materia mtr,"
					+ " CargaHoraria crhr, DetallePuesto dtps, FichaDocente fcdc, Persona prs, Paralelo prl  where ");
			sbsql.append(" mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mtr.mtrId ");
			sbsql.append(" and crhr.crhrMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and crhr.crhrDetallePuesto.dtpsId = dtps.dtpsId ");
			sbsql.append(" and dtps.dtpsFichaDocente.fcdcId = fcdc.fcdcId ");
			sbsql.append(" and fcdc.fcdcPersona.prsId = prs.prsId ");
			sbsql.append(" and mlcrmt.mlcrmtNivel.nvlId = :nivelId1 ");
			sbsql.append(" and prs.prsIdentificacion = :cedula1 ");
			sbsql.append(" and mtr.mtrId = :materiaId1  ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ) ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("materiaId", materiaId);
			q.setParameter("cedula", cedula);
			q.setParameter("nivelId", nivelId);
			q.setParameter("materiaId1", materiaId);
			q.setParameter("cedula1", cedula);
			q.setParameter("nivelId1", nivelId);
			retorno = (MallaCurricularParalelo) q.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.no.result.exception")));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.exception")));
		}
		return retorno;
	}	
	
	
	public MallaCurricularParalelo actualizarParaleloCupo(MallaCurricularParalelo mlcrpr) throws MallaCurricularParaleloValidacionException, MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;

		if (mlcrpr.getMlcrprId() != 0 ) {
			try {
				MallaCurricularParalelo malla = em.find(MallaCurricularParalelo.class, mlcrpr.getMlcrprId());
				malla.setMlcrprCupo(mlcrpr.getMlcrprCupo());
				retorno = em.merge(malla);
				em.flush();
				
				Paralelo paralelo = em.find(Paralelo.class, malla.getMlcrprParalelo().getPrlId());
				paralelo.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				em.merge(paralelo);
				em.flush();
				
			} catch (NoResultException e) {
				throw new MallaCurricularParaleloNoEncontradoException("");
			}catch (NonUniqueResultException e) {
				throw new MallaCurricularParaleloException("Se encontró mas de un paralelo vinculado a la Materia.");
			} catch (Exception e) {
				throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.exception")));
			}
		}else{
			throw new MallaCurricularParaleloValidacionException("");
		}

		return retorno;
	}

	public  MallaCurricularParalelo buscarPorParalelo(int paraleloId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloValidacionException, MallaCurricularParaleloException {
		MallaCurricularParalelo retorno = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" Select mlcrpr from MallaCurricularParalelo mlcrpr where ");
		sql.append(" mlcrpr.mlcrprParalelo.prlId = :paraleloId ");
		
		try {
			
			Query q = em.createQuery(sql.toString());
			q.setParameter("paraleloId", paraleloId);
			retorno = (MallaCurricularParalelo)q.getSingleResult();
		
		} catch (NoResultException e) {
			throw new MallaCurricularParaleloNoEncontradoException("No se encontró malla curricular paralelo según parametro ingresado.");
		} catch (NonUniqueResultException e) {
			throw new MallaCurricularParaleloException("Se encontró mas de un objeto malla curricular paralelo según parametro ingresado.");
		} catch (Exception e) {
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.paralelo.exception")));
		}
		
		return retorno;
	}
}
