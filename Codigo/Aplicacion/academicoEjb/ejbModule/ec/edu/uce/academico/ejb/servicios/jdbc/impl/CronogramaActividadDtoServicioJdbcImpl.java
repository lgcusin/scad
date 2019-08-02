/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 ************************************************************************* 

 ARCHIVO:     CronogramaActividadDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc del dto CronogramaActividadesDTO.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 20-06-2017		   Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB CronogramaActividadDtoServicioJdbcImpl.
 * Clase donde se implementan los métodos para el servicio jdbc del dto CronogramaActividadDTO.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class CronogramaActividadDtoServicioJdbcImpl implements CronogramaActividadDtoServicioJdbc {

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Metodo que realiza la busqueda de las actividades del proceso flujo
	 * @param idCrr - idCrr id de la carrera que pertenece el consultado
	 * @param estadoPrac - estadoPrac estado del periodo académico del que pertenece el consultado
	 * @param tipoCrn - tipoCrn tipo cronograma si es para 1:nivelación ó 0:carrera o academico 
	 * @param idPrfl - idPrfl id del proceso flujo al que se quiere consultar
	 * @param estadoPrfl - estadoPrfl estado del proceso flujo del que se quiere consultar
	 * @return Retorna la entidad CronogramaActividadesJdbcDto que ha sido consultado con los parametros requirientes
	 * @throws CronogramaActividadDtoJdbcException  - CronogramaActividadDtoJdbcException excepción general
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException  - CronogramaActividadDtoJdbcNoEncontradoException excepción cuando no se encuentra cronogramaactividad por los parametros ingresados
	 */
	public CronogramaActividadJdbcDto listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(int idCrr, int estadoPrac, int tipoCrn, int idPrfl, int estadoPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
						sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
						sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" = ? ");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCrr);
			pstmt.setInt(2, estadoPrac);
			pstmt.setInt(3, tipoCrn);
			pstmt.setInt(4, idPrfl);
			pstmt.setInt(5, estadoPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.actividad.dto.dbc.buscar.por.carrera.estadoPrac.tipoCrn.idPrfl.estadoPrfl.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.actividad.dto.dbc.buscar.por.carrera.estadoPrac.tipoCrn.idPrfl.estadoPrfl.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.actividad.dto.dbc.buscar.por.carrera.estadoPrac.tipoCrn.idPrfl.estadoPrfl.no.result.exception")));
		}
		return retorno;
	}
	
	
	
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" ,  ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" )  ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
//			System.out.println(sbSql);
//			System.out.println(tipoCrn);
//			System.out.println(idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
				throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));	
			
		}
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasPeriodoActivoPorProceso(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" )  ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
				throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));	
			
		}
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasxPracIdXtipoFlujo(int pracId, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" ,  ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" )  ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			
//			System.out.println(sbSql);
//			System.out.println(idPrfl);
//			System.out.println(pracId);
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
				throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));	
			
		}
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" )  ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
				throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));	
			
		}
		return retorno;
	}
	
	
	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" )  ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoSchedule(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" )  ");
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}

	/**
	 * Buscar CronogramaActividadJdbcDto por los campos mencionados 
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param tipoCrn .- TipoCronograma a buscar
	 * @param idPrfl .- ProcesoFLujo a buscar
	 * @return CronogramaActividadJdbcDto encontrado
	 * @throws CronogramaActividadDtoJdbcException
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException
	 */
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" )  ");
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
			sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" = ");
			sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}

	

	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarRangoFechasNotasSuficienciaCulturaFisica(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" =  ");
						sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, tipoCrn);
			pstmt.setInt(2, idPrfl);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	
	@Override
	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechas(String date) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ");
						sbSql.append(CronogramaConstantes.TIPO_ACADEMICO_VALUE);
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" in (");
						sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
						sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
						sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
						sbSql.append(" ) ");
						sbSql.append(" AND ");sbSql.append(" to_date( ");sbSql.append(" ? ");sbSql.append(" , 'dd/MM/YYYY' ) ");
						sbSql.append(" BETWEEN TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");
						sbSql.append(" AND ");sbSql.append(" TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	
	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechasFull(String date, int crnTipo) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
				sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
				sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
				sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
				sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");	sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");	sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" in (  ");sbSql.append(" ? ");sbSql.append(" ) ");
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" in (");
							sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
							sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
							sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
							sbSql.append(" ) ");
						sbSql.append(" AND ");sbSql.append(" to_date( ");sbSql.append(" ? ");sbSql.append(" , 'dd/MM/YYYY' ) ");
						sbSql.append(" BETWEEN TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");
						sbSql.append(" AND ");sbSql.append(" TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crnTipo);
			pstmt.setString(2, date);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}
			
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaActividadJdbcDto.buscar.por.fechas.full.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaActividadJdbcDto.buscar.por.fechas.full.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaActividadJdbcDto.buscar.por.fechas.full.exception")));
		}
		return retorno;
	}
	
	
	
	@Override
	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechasNivelacion(String date) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ");
						sbSql.append(CronogramaConstantes.TIPO_NIVELACION_VALUE);
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" in (");
						sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
						sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
						sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
						sbSql.append(" ) ");
						sbSql.append(" AND ");sbSql.append(" to_date( ");sbSql.append(" ? ");sbSql.append(" , 'dd/MM/YYYY' ) ");
						sbSql.append(" BETWEEN TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");
						sbSql.append(" AND ");sbSql.append(" TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechasNivelacionCursoVerano(String date) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");
		    sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");
						sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");
						sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");
						sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
						sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");
						sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ");
						sbSql.append(CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_VALUE);
						sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" in (");
						sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
						sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
						sbSql.append(" , ");sbSql.append(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
						sbSql.append(" ) ");
						sbSql.append(" AND ");sbSql.append(" to_date( ");sbSql.append(" ? ");sbSql.append(" , 'dd/MM/YYYY' ) ");
						sbSql.append(" BETWEEN TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");
						sbSql.append(" AND ");sbSql.append(" TO_DATE(TO_CHAR( ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
						sbSql.append(" , 'dd/MM/YYYY' ), 'dd/MM/YYYY')  ");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	public CronogramaActividadJdbcDto buscarCronograma(int prflId, int crnTipo) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in( ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" )  ");
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);sbSql.append(" = ");sbSql.append(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);sbSql.append(" = ");sbSql.append(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crnTipo);
			pstmt.setInt(2, prflId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}
			
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		
		return retorno;
	}
	
	@Override
	public CronogramaActividadJdbcDto buscarCronogramaPorPlanificacionCronograma(int plcrId) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
			CronogramaActividadJdbcDto retorno= null;
			PreparedStatement pstmt = null;
			Connection con = null;
			ResultSet rs = null;
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
				sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
				sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
				sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
				sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
				sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
				
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
				
				sbSql.append(" WHERE ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);sbSql.append(" = ? ");

				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setInt(1, plcrId);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
				}
				
			} catch (SQLException e) {
				throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
			} catch (Exception e) {
				throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
			
			if(retorno == null){
				throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
			}
			
			return retorno;
	} 
	
	public CronogramaActividadJdbcDto buscarCronogramaPorPeriodo(int procesoId, int cronogramaTipo, int periodoId) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
		sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
		sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
		sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
		sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
		sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
		sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
		sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
		sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
		sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
		
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
		sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
		sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
		
		sbSql.append(" WHERE ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
		sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");

		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, procesoId);
			pstmt.setInt(2, cronogramaTipo);
			pstmt.setInt(3, periodoId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				retorno = transformarResultSetADtobuscarRangoFechasNotas(rs);
			}
			
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		
		return retorno;
	}
	
	public CronogramaActividadJdbcDto buscarCronogramaPorProcesoPeriodo(int procesoId, int periodoId) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException {
		CronogramaActividadJdbcDto retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" , crn.");sbSql.append(JdbcConstantes.CRN_ESTADO);	
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" , prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" , plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ON ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ON ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, procesoId);
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				retorno = rsAbuscarCronogramaPorProcesoPeriodo(rs);
			}
			
		} catch (SQLException e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaActividadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.estudiante.exception")));
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
		
		if(retorno == null){
			throw new CronogramaActividadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.no.result.exception")));
		}
		
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CronogramaActividadJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		java.sql.Timestamp fecha = null;
		CronogramaActividadJdbcDto retorno = new CronogramaActividadJdbcDto();
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		fecha = rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO);
		if(fecha != null ){
			retorno.setPlcrFechaInicio(fecha);
		}
		fecha = rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN);
		if(fecha != null ){
			retorno.setPlcrFechaFin(fecha);
		}
		return retorno;
	} 
	
	
	private CronogramaActividadJdbcDto rsAbuscarCronogramaPorProcesoPeriodo(ResultSet rs) throws SQLException{
		CronogramaActividadJdbcDto retorno = new CronogramaActividadJdbcDto();
		
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPracFechaInicio(rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO));
		retorno.setPracFechaFin(rs.getDate(JdbcConstantes.PRAC_FECHA_FIN));
		retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
		retorno.setCrnId(rs.getInt(JdbcConstantes.CRN_ID));
		retorno.setCrnDescripcion(rs.getString(JdbcConstantes.CRN_DESCRIPCION));
		retorno.setCrnTipo(rs.getInt(JdbcConstantes.CRN_TIPO));
		retorno.setCrnEstado(rs.getInt(JdbcConstantes.CRN_ESTADO));
		retorno.setPrlfId(rs.getInt(JdbcConstantes.PRFL_ID));
		retorno.setPrlfDescripcion(rs.getString(JdbcConstantes.PRFL_DESCRIPCION));
		retorno.setPrlfEstado(rs.getInt(JdbcConstantes.PRFL_ESTADO));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));
		
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CronogramaActividadJdbcDto transformarResultSetADtobuscarRangoFechasNotas(ResultSet rs) throws SQLException{
		java.sql.Timestamp fecha = null;
		CronogramaActividadJdbcDto retorno = new CronogramaActividadJdbcDto();
		
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPracFechaInicio(rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO));
		retorno.setPracFechaFin(rs.getDate(JdbcConstantes.PRAC_FECHA_FIN));
		retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
		retorno.setCrnId(rs.getInt(JdbcConstantes.CRN_ID));
		retorno.setCrnDescripcion(rs.getString(JdbcConstantes.CRN_DESCRIPCION));
		retorno.setCrnTipo(rs.getInt(JdbcConstantes.CRN_TIPO));
		retorno.setCrnEstado(rs.getInt(JdbcConstantes.CRN_ESTADO));
		retorno.setPrlfId(rs.getInt(JdbcConstantes.PRFL_ID));
		retorno.setPrlfDescripcion(rs.getString(JdbcConstantes.PRFL_DESCRIPCION));
		retorno.setPrlfEstado(rs.getInt(JdbcConstantes.PRFL_ESTADO));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		
		fecha = rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO);
		
		if(fecha != null ){
			retorno.setPlcrFechaInicio(fecha);
		}
		
		fecha = rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN);
		
		if(fecha != null ){
			retorno.setPlcrFechaFin(fecha);
		}
		
		return retorno;
	}



	
}
