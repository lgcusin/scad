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
   
 ARCHIVO:     CronogramaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla Cronograma.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-DICI-2017		Vinicio Rosales				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB CronogramaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Cronograma.
 * @author jvrosales
 * @version 1.0
 */
@Stateless
public class CronogramaDtoServicioJdbcImpl implements CronogramaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	
	
	/**
	 * Realiza la busqueda de los procesos con sus respectivo periodo academico
	 * @return Lista los procesos del cronograma
	 * @throws CronogramaDtoJdbcException 
	 * @throws CronogramaDtoJdbcNoEncontradoException 
	 * @throws CronogramaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CronogramaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CronogramaDto> listarProcesosXPeriodoxTipo(int pracId, int crnTipo) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException {
		List<CronogramaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);

			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			
			sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
//			System.out.println(sbSql);
//			System.out.println(pracId);
//			System.out.println(crnTipo);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, crnTipo);
					
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CronogramaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetProcesoPeriodoTipoADto(rs));
			}
		} catch (SQLException e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.procesos.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new CronogramaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la búsqueda de la planificacion de cronograma para ingresar las fechas
	 * @param pracId - id del periodo acedemico
	 * @param cnrTipo - tipo de cronograma nivelacion, pregrado
	 * @param plcrId - id de la planificaicon a buscar 
	 * @return la planificacion del cronograma
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	
	public CronogramaDto buscarPlanificacionCronogramaXPeriodoXTipoXPlanificacion(int pracId, int cnrTipo, int plcrId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException{
		CronogramaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND plcr.");sbSql.append(JdbcConstantes.PLCR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
					
			con = ds.getConnection();
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, cnrTipo);
			pstmt.setInt(3, plcrId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetProcesoPeriodoTipoPlanificacionADto(rs);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.buscar.planificacion.sql.exception")));
		}catch (Exception e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.buscar.planificacion.exception")));
		}finally {
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
			throw new CronogramaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.buscar.planificacion.no.result.exception")));
		}
		
				
		return retorno;
		
	}
	
	
	/**
	 * Realiza la búsqueda de cronograma por los parametros de busqueda
	 * @author Arturo Villafuerte - ajvillafuerte 
	 * @param prflId - id del proceso flujo
	 * @param tiapId - tipo de tipoApertura
	 * @param nvapNumeral - numeral en nivel apertira por semestre 
	 * @return fechas de el cronograma
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	public CronogramaDto buscarCronogramaXTipoAperturaXNivelAperturaXprocesoFlujo(int prflId, int tiapId, int nvapNumeral, int nvapCrrId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException{
		CronogramaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT TPNVAP.TINVAP_FECHA_FIN, TPNVAP.TINVAP_FECHA_INICIO, NVAP.NVAP_DESCRIPCION ");
			sbSql.append(" FROM PROCESO_FLUJO PRFL, ");
			sbSql.append(" CRONOGRAMA_PROCESO_FLUJO CRPRFL, ");
			sbSql.append(" PLANIFICACION_CRONOGRAMA PLCR, ");
			sbSql.append(" TIPO_NIVEL_APERTURA TPNVAP, ");
			sbSql.append(" TIPO_APERTURA TPAP, ");
			sbSql.append(" NIVEL_APERTURA NVAP ");
			sbSql.append(" WHERE PRFL.PRFL_ID = CRPRFL.PRFL_ID ");
			sbSql.append(" AND PLCR.CRPRFL_ID = CRPRFL.CRPRFL_ID ");
			sbSql.append(" AND TPNVAP.PLCR_ID = PLCR.PLCR_ID ");
			sbSql.append(" AND TPNVAP.NVAP_ID = NVAP.NVAP_ID ");
			sbSql.append(" AND TPNVAP.TIAP_ID = TPAP.TIAP_ID ");
			sbSql.append(" AND PRFL.PRFL_ESTADO = 0 ");
			sbSql.append(" AND PRFL.PRFL_ID = ? ");
			sbSql.append(" AND TPAP.TIAP_ID = ? ");
			sbSql.append(" AND TPNVAP.TINVAP_ESTADO = 0 ");
			sbSql.append(" AND NVAP.NVAP_NUMERAL = ? ");
			sbSql.append(" AND NVAP.CRR_ID = ? ");
					
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, prflId);
			pstmt.setInt(2, tiapId);
			pstmt.setInt(3, nvapNumeral);
			pstmt.setInt(4, nvapCrrId);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetProcesoPeriodoTipoPlanificacionADtoFull(rs);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.buscar.planificacion.sql.exception")));
		}catch (Exception e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.buscar.planificacion.exception")));
		}finally {
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
			throw new CronogramaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.buscar.planificacion.no.result.exception")));
		}
		
				
		return retorno;
		
	}
	
	public List<CronogramaDto> buscarProcesoFlujo(int crnTipo, Integer[] pracEstado) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException{
		List<CronogramaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
		sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");sbSql.append(Arrays.toString(pracEstado).replace("[", "").replace("]", ""));sbSql.append(" ) ");
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ");sbSql.append(crnTipo);
		sbSql.append(" ORDER BY ");	sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		
		try {
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetACronogramaDto(rs));
			}
			
		} catch (SQLException e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.procesos.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			retorno = null ;
			throw new CronogramaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.no.result.exception")));
		}	
		
		return retorno;	
	}
	
	public CronogramaDto buscarTipoMatriculaGeneral(int crnTipo, int pracEstado, int prflId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException{
		CronogramaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
		sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(pracEstado);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ");sbSql.append(crnTipo);
			sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(prflId);
		sbSql.append(" ORDER BY ");	sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		
		try {
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno = transformarResultSetProcesoPeriodoTipoADto(rs);
			}
			
		} catch (SQLException e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.procesos.exception")));
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
			throw new CronogramaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.no.result.exception")));
		}	
		
		return retorno;	
	}
	
	
	
	
	
	/**
	 * Realiza la búsqueda de cronograma por los parametros de busqueda
	 * @author MQ:
	 * @param prflId - id del proceso flujo
	 * @param crnTipo - tipo de cronograma
	 * @param pracId - id del periodo académico 
	 * @return cronograma con fechas
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	
	public List<CronogramaDto> listarFechasCronogramaXTipoCronogramaXPeriodo(int crnTipo, int pracId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException{
		List<CronogramaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
			sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
			sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
			sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
			sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
		sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
			sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(pracId);
			sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ");sbSql.append(crnTipo);
		
		sbSql.append(" ORDER BY ");	sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetACronogramaDto(rs));
			}
			
		} catch (SQLException e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.sql.exception")));
		} catch (Exception e) {
			throw new CronogramaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.procesos.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			retorno = null ;
			throw new CronogramaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaDto.listar.procesos.no.result.exception")));
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
	private CronogramaDto transformarResultSetProcesoPeriodoTipoADto(ResultSet rs) throws SQLException{
		CronogramaDto retorno = new CronogramaDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrnId(rs.getInt(JdbcConstantes.CRN_ID));
		retorno.setCrnTipo(rs.getInt(JdbcConstantes.CRN_TIPO));
		retorno.setCrnDescripcion(rs.getString(JdbcConstantes.CRN_DESCRIPCION));
		retorno.setPrflId(rs.getInt(JdbcConstantes.PRFL_ID));
		retorno.setPrflDescripcion(rs.getString(JdbcConstantes.PRFL_DESCRIPCION));
		retorno.setPrflEstado(rs.getInt(JdbcConstantes.PRFL_ESTADO));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));
		retorno.setCrprflOrdinal(rs.getInt(JdbcConstantes.CRPRFL_ORDINAL));
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CronogramaDto transformarResultSetACronogramaDto(ResultSet rs) throws SQLException{
		CronogramaDto retorno = new CronogramaDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrnId(rs.getInt(JdbcConstantes.CRN_ID));
		retorno.setCrnTipo(rs.getInt(JdbcConstantes.CRN_TIPO));
		retorno.setCrnDescripcion(rs.getString(JdbcConstantes.CRN_DESCRIPCION));
		retorno.setPrflId(rs.getInt(JdbcConstantes.PRFL_ID));
		retorno.setPrflDescripcion(rs.getString(JdbcConstantes.PRFL_DESCRIPCION));
		retorno.setPrflEstado(rs.getInt(JdbcConstantes.PRFL_ESTADO));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		retorno.setPlcrFechaInicial(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFinal(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));		
		retorno.setCrprflOrdinal(rs.getInt(JdbcConstantes.CRPRFL_ORDINAL));
		return retorno;
	} 
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CronogramaDto transformarResultSetProcesoPeriodoTipoPlanificacionADto(ResultSet rs) throws SQLException{
		CronogramaDto retorno = new CronogramaDto();
		
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrnId(rs.getInt(JdbcConstantes.CRN_ID));
		retorno.setCrnTipo(rs.getInt(JdbcConstantes.CRN_TIPO));
		retorno.setCrnDescripcion(rs.getString(JdbcConstantes.CRN_DESCRIPCION));
		retorno.setPrflId(rs.getInt(JdbcConstantes.PRFL_ID));
		retorno.setPrflDescripcion(rs.getString(JdbcConstantes.PRFL_DESCRIPCION));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));
//		retorno.setPlcrStrFechaInicio(rs.getString("fecha_inicio"));
//		retorno.setPlcrStrFechaFin(rs.getString("fecha_fin"));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CronogramaDto transformarResultSetProcesoPeriodoTipoPlanificacionADtoFull(ResultSet rs) throws SQLException{
		CronogramaDto retorno = new CronogramaDto();
		
		retorno.setCrnDescripcion(rs.getString(JdbcConstantes.NVAP_DESCRIPCION));
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.TINVAP_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.TINVAP_FECHA_FIN));
		
		return retorno;
	} 
	
}
