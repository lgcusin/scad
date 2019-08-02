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

 ARCHIVO:     ComprobantePagoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Comprobantepago. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 02-09-2017           Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.ComprobanteCSVDto;
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;

/**
 * Clase (Bean)ComprobantePagoServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class ComprobantePagoServicioImpl implements ComprobantePagoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	
	@Override
	public ComprobantePago editarEstadoMigrado(ComprobanteCSVDto entidad)
			throws ComprobantePagoValidacionException, ComprobantePagoNoEncontradoException, ComprobantePagoException {
		ComprobantePago retorno = new ComprobantePago();
		if(entidad != null)	{
			ComprobantePago cmpaAux = em.find(ComprobantePago.class, entidad.getCmpaId());
			cmpaAux.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ENVIADO_VALUE);
			em.merge(cmpaAux);
		}
		return retorno;
	}

	@Override
	public ComprobantePago buscarXId(Integer id) throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		ComprobantePago retorno = null;
		if (id != null) {
			try {
				retorno = em.find(ComprobantePago.class, id);
			} catch (NoResultException e) {
				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	@Override
	public ComprobantePago generarComprobanteMedicina(Integer id) throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		ComprobantePago retorno = null;
		if (id != null) {
			try {
				retorno = em.find(ComprobantePago.class, id);
				retorno.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
				Date fechaCaducidadPago = new Date(retorno.getCmpaFechaCaduca().getTime());
				retorno.setCmpaFechaCaducidad(fechaCaducidadPago);
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Timestamp((new Date()).getTime()));
				retorno.setCmpaFechaEmision(new Timestamp(cal.getTime().getTime()));
				Timestamp ts = new Timestamp(cal.getTime().getTime());
				cal.add(Calendar.DAY_OF_WEEK, 5);					
				ts = new Timestamp(cal.getTime().getTime());
				retorno.setCmpaFechaCaduca(ts);
				em.merge(retorno);
				em.flush();
			
			} catch (NoResultException e) {
				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public ComprobantePagoDto buscarXNumComprobantePago(String numComprobantePago) throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		ComprobantePagoDto retorno = null;
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT CMP_ID FROM COMPROBANTE_PAGO WHERE CMP_NUM_COMPROBANTE = '");sql.append(numComprobantePago);
			sql.append("'");
			
			
			con = ds.getConnection();
				pstmt = con.prepareStatement(sql.toString());
				
			rs = pstmt.executeQuery();
			retorno = new ComprobantePagoDto();
			while (rs.next()) {
				retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
			}
			
		} catch (Exception e) {
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		
//		if (numComprobantePago != null) {
//			try {
//					StringBuffer sbsql = new StringBuffer();
//					sbsql.append(" Select cmpa from ComprobantePago cmpa ");
//					sbsql.append(" where cmpa.cmpaNumComprobante = :numComprobantePago");
//					Query q = em.createQuery(sbsql.toString());
//					q.setParameter("numComprobantePago", numComprobantePago);
//					retorno = (ComprobantePago) q.getSingleResult();
//			} catch (NoResultException e) {
//				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception",numComprobantePago)));
//			}catch (NonUniqueResultException e) {
//				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception",numComprobantePago)));
//			} catch (Exception e) {
//				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
//			}
//		}
		return retorno;
	}
	
	@Override
	public Integer buscarMaxId() throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		Integer retorno = null;
			try {
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" Select max(cmpa.cmpaNumCompSecuencial) from ComprobantePago cmpa ");
					Query q = em.createQuery(sbsql.toString());
					retorno =  (Integer)q.getSingleResult();
			} catch (NoResultException e) {
				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
			}
		return retorno;
	}
	
	@Override
	public boolean editarMontoPagarXid(Integer cmpaId, Integer tipoFacturacion) throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		boolean retorno = false;
		if (cmpaId != null && tipoFacturacion!=null ) {
			try {
				
					ComprobantePago cmpaAux = em.find(ComprobantePago.class, cmpaId);
//					int comparador = ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE.compareTo(new BigDecimal(tipoFacturacion));
//					if(comparador==0){
//						cmpaAux.setCmpaTotalPago(new BigDecimal(140));
//						cmpaAux.setCmpaValorPagado(new BigDecimal(140));
//						cmpaAux.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_SEGUNDA_MATRICULA_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
//					}else if (comparador==1){
//						cmpaAux.setCmpaTotalPago(new BigDecimal(0));
//						cmpaAux.setCmpaValorPagado(new BigDecimal(0));
//						cmpaAux.setCmpaIdArancel(null);
//					}else{
//						cmpaAux.setCmpaTotalPago(new BigDecimal(240));
//						cmpaAux.setCmpaValorPagado(new BigDecimal(240));
//						cmpaAux.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_TERCERA_MATRICULA_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
//					}
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Timestamp((new Date()).getTime()));
					cmpaAux.setCmpaFechaEmision(new Timestamp(cal.getTime().getTime()));
					cal.add(Calendar.DAY_OF_WEEK, 4);	
					Timestamp ts = new Timestamp(cal.getTime().getTime());
					cmpaAux.setCmpaFechaCaduca(ts);
					cmpaAux.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
					
					em.merge(cmpaAux);
					em.flush();
					retorno = true;
			} catch (NoResultException e) {
				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception",cmpaId)));
			}catch (NonUniqueResultException e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception",cmpaId)));
			} catch (Exception e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public String editarNumComprobanteXCmpaId(Integer cmpaId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		String retorno = null;
		if (cmpaId != null  ) {
			try {
					ComprobantePago cmpaAux = em.find(ComprobantePago.class, cmpaId);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Timestamp((new Date()).getTime()));
					cal.add(Calendar.DAY_OF_WEEK, 4);	
					Timestamp ts = new Timestamp(cal.getTime().getTime());
					StringBuilder sbComprobanteAux = new StringBuilder();
					sbComprobanteAux.append(cmpaAux.getCmpaNumComprobante().substring(0,3));
					Integer secuencial=new Integer(0);
					try {
						StringBuilder sbSql = new StringBuilder();
						sbSql.append(" SELECT max(cmpaNumCompSecuencial) from ComprobantePago ");
						Query q = em.createQuery(sbSql.toString());
						secuencial = (Integer)q.getSingleResult();
						if(secuencial != null){
							secuencial++;	
						}else{
							secuencial=1;
						}
					}catch (Exception e) {
					}
					String secuencialString = secuencial.toString();
					String auxNumComprobante= GeneralesUtilidades.completarCadenaDer(sbComprobanteAux.toString(), 
							(ComprobantePagoConstantes.NUM_MAX_CARACTERES_COMPROBANTE-secuencialString.length()), "0");
					StringBuilder sbComprobante = new StringBuilder();
					sbComprobante.append(auxNumComprobante);
					sbComprobante.append(secuencialString);
					
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" UPDATE ComprobantePago SET ");
					sbsql.append(" cmpaNumCompSecuencial=");sbsql.append(secuencial);
					sbsql.append(" ,cmpaCodigo='");sbsql.append(sbComprobante.toString());
					sbsql.append("' ,cmpaNumComprobante='");sbsql.append(sbComprobante.toString());
					if(cmpaAux.getCmpaDescripcion().equals("MATRICULA NIVELACIÓN NIVELACION")){
						sbsql.append("' ,cmpaEstado=");sbsql.append(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
					}else{
						sbsql.append("' ,cmpaEstado=");sbsql.append(ComprobantePagoConstantes.ESTADO_EDITADO_VALUE);	
					}
					
					sbsql.append(" ,cmpaFechaEmision= :fechaEmision");
					sbsql.append(" ,cmpaFechaCaduca= :fechaCaduca");
					sbsql.append(" where cmpaId = :cmpaId");
					Query q = em.createQuery(sbsql.toString());
					cal = Calendar.getInstance();
					q.setParameter("fechaEmision", new Timestamp(cal.getTime().getTime()));
					q.setParameter("fechaCaduca", ts);
					q.setParameter("cmpaId", cmpaId);
					q.executeUpdate();
					retorno = sbComprobante.toString();
			} catch (NoResultException e) {
				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception",cmpaId)));
			}catch (NonUniqueResultException e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception",cmpaId)));
			} catch (Exception e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public String editarNumComprobanteXCmpaNumComprobante(String cmpaNumComprobante,Integer cmpaId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		String retorno = null;
		if (cmpaNumComprobante != null  ) {
			try {
					ComprobantePago cmpaAux = em.find(ComprobantePago.class, cmpaId);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Timestamp((new Date()).getTime()));
					cal.add(Calendar.DAY_OF_WEEK, 4);	
					Timestamp ts = new Timestamp(cal.getTime().getTime());
					StringBuilder sbComprobanteAux = new StringBuilder();
					sbComprobanteAux.append(cmpaAux.getCmpaNumComprobante().substring(0,3));
					Integer secuencial=new Integer(0);
					try {
						StringBuilder sbSql = new StringBuilder();
						sbSql.append(" SELECT max(cmpaNumCompSecuencial) from ComprobantePago ");
						Query q = em.createQuery(sbSql.toString());
						secuencial = (Integer)q.getSingleResult();
						if(secuencial != null){
							secuencial++;	
						}else{
							secuencial=1;
						}
					}catch (Exception e) {
					}
					String secuencialString = secuencial.toString();
					String auxNumComprobante= GeneralesUtilidades.completarCadenaDer(sbComprobanteAux.toString(), 
							(ComprobantePagoConstantes.NUM_MAX_CARACTERES_COMPROBANTE-secuencialString.length()), "0");
					StringBuilder sbComprobante = new StringBuilder();
					sbComprobante.append(auxNumComprobante);
					sbComprobante.append(secuencialString);
					
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" UPDATE ComprobantePago SET ");
					sbsql.append(" cmpaNumCompSecuencial=");sbsql.append(secuencial);
					sbsql.append(" ,cmpaCodigo='");sbsql.append(sbComprobante.toString());
					sbsql.append("' ,cmpaNumComprobante='");sbsql.append(sbComprobante.toString());
					if(cmpaAux.getCmpaDescripcion().equals("MATRICULA NIVELACIÓN NIVELACION")
							||cmpaAux.getCmpaDescripcion().equals("MATRICULA NIVELACIÓN")){
						sbsql.append("' ,cmpaEstado=");sbsql.append(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
					}else{
						int i = cmpaAux.getCmpaTotalPago().compareTo(new BigDecimal(1500));
						if(i==0 || i == 1){
							sbsql.append("' ,cmpaEstado=");sbsql.append(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
						}else{
							sbsql.append("' ,cmpaEstado=");sbsql.append(ComprobantePagoConstantes.ESTADO_EDITADO_VALUE);	
						}
					}
					sbsql.append(" ,cmpaFechaEmision= :fechaEmision");
					sbsql.append(" ,cmpaFechaCaduca= :fechaCaduca");
					sbsql.append(" where cmpaNumComprobante = :cmpaNumComprobante");
					Query q = em.createQuery(sbsql.toString());
					cal = Calendar.getInstance();
					q.setParameter("fechaEmision", new Timestamp(cal.getTime().getTime()));
					q.setParameter("fechaCaduca", ts);
					q.setParameter("cmpaNumComprobante", cmpaNumComprobante);
					q.executeUpdate();
					retorno = sbComprobante.toString();
			} catch (NoResultException e) {
				throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.no.result.exception",cmpaId)));
			}catch (NonUniqueResultException e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.non.unique.result.exception",cmpaId)));
			} catch (Exception e) {
				throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**MQ
	 * Busca Lista de  comprobantes por Id De FichaMatricula
	 * @param fcmtId - id de la ficha matricula a buscar
	 * @return Lista de  Comprobantes de pago. 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComprobantePago> ListarPorFcmt(Integer fcmtId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException  {
		List<ComprobantePago> retorno=null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cmpa from ComprobantePago cmpa ");
			sbsql.append(" where cmpa.cmpaFichaMatricula.fcmtId =:fcmtId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("fcmtId", fcmtId);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.listar.por.id.ficha.matricula.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.listar.por.id.ficha.matricula.no.encontrado.exception")));
		}
		return retorno;
	}
	
	/**MQ
	 * Busca un comprobante por Id De FichaMatricula
	 * @param fcmtId - id de la ficha matricula a buscar
	 * @return Comprobante de pago. 
	 * 
	 */
	@Override
	public ComprobantePago buscarPorFcmt(Integer fcmtId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException  {
		ComprobantePago retorno=null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cmpa from ComprobantePago cmpa ");
			sbsql.append(" where cmpa.cmpaFichaMatricula.fcmtId =:fcmtId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("fcmtId", fcmtId);
			  retorno = (ComprobantePago) q.getSingleResult();
		} catch (NoResultException e) {
			throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.ficha.matricula.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.ficha.matricula.non.unique.result.exception")));
		} catch (Exception e) {
			throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.buscar.por.id.ficha.matricula.exception")));
		}
		
		return retorno;
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComprobantePago> buscarCmpaPendientePagoNivelacionPosgrado()
			throws ComprobantePagoNoEncontradoException, ComprobantePagoException {
		List<ComprobantePago> retorno=null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cmpa from ComprobantePago cmpa, FichaMatricula fcmt, FichaEstudiante fces, RecordEstudiante rces ");
			sbsql.append(" where cmpa.cmpaFichaMatricula.fcmtId = fcmt.fcmtId ");
			sbsql.append(" and fcmt.fcmtFichaEstudiante.fcesId = fces.fcesId ");
			sbsql.append(" and fces.fcesId = rces.rcesFichaEstudiante.fcesId ");
			sbsql.append(" and rces.rcesEstado = ");sbsql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
			sbsql.append(" and cmpa.cmpaNumComprobante is not null ");
			sbsql.append(" and cmpa.cmpaEstado = ");sbsql.append(ComprobantePagoConstantes.ESTADO_ENVIADO_VALUE);
			sbsql.append(" and fcmt.fcmtNivelUbicacion in( ");sbsql.append(FichaMatriculaConstantes.NIVELACION_VALUE);
			sbsql.append(" , ");sbsql.append(FichaMatriculaConstantes.POSGRADO_PRIMER_NIVEL_VALUE);sbsql.append(" ) ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new ComprobantePagoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.listar.por.id.ficha.matricula.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			throw new ComprobantePagoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ComprobantePago.listar.por.id.ficha.matricula.no.encontrado.exception")));
		}
		return retorno;
	}
	
	public boolean actualizarComprobantePago(ComprobantePagoDto entidad)throws ComprobantePagoValidacionException, ComprobantePagoNoEncontradoException {
		boolean retorno = false;
		
		try {
			ComprobantePago comprobante = em.find(ComprobantePago.class, entidad.getCmpaId());
			comprobante.setCmpaNumComprobante(entidad.getCmpaNumero());
			comprobante.setCmpaFechaEmision(entidad.getCmpaFechaEmision());
			comprobante.setCmpaFechaCaduca(entidad.getCmpaFechaCaducidad());
			comprobante.setCmpaFechaPago(entidad.getCmpaFechaPago());
			comprobante.setCmpaFechaEnvio(GeneralesUtilidades.getFechaActualSistemaTimestamp());//legalizacion
			comprobante.setCmpaValorPagado(entidad.getCmpaTotalAPagar());
			comprobante.setCmpaTotalPago(entidad.getCmpaTotalAPagar());
			comprobante.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PAGADO_VALUE);
			em.merge(comprobante);
			em.flush();
			retorno = true;
		} catch (NoResultException e) {
			throw new ComprobantePagoNoEncontradoException("No se encontró comprobante de pago con el id solicidado.");
		} catch (IllegalArgumentException e) {
			throw new ComprobantePagoValidacionException("Error tipo sql al actualizar el comprobante de pago.");
		}
		
		return retorno;
	}
	
}
