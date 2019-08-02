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
   
 ARCHIVO:     MallaPeriodoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaPeriodo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 31-AGOS-2017		Vinicio Rosales				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MallaPeriodoDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaPeriodoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MallaPeriodoDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaPeriodo.
 * @author jvrosales
 * @version 1.0
 */
@Stateless
public class MallaPeriodoDtoServicioJdbcImpl implements MallaPeriodoDtoServicioJdbc{
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	/**
	 * Realiza la busqueda de todas las mallas_periodo
	 * @return Lista todas las mallas_periodo por carrera de la aplicacion
	 * @throws MallaPeridoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaPeriodoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 * @throws MallaCurricularDtoException 
	 */
	public List<MallaPeriodoDto> listarMallasPeriodo() throws MallaPeriodoDtoException, MallaPeriodoDtoNoEncontradoException{
		List<MallaPeriodoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ID);
			sbSql.append(" , mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);sbSql.append(" = ");sbSql.append(MallaCurricularConstantes.VIGENTE_MALLA_SI_VALUE);

			sbSql.append(" ORDER BY ");
			sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
					
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaPeriodoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new MallaPeriodoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodoDto.listar.mallasPeriodo.sql.exception")));
		} catch (Exception e) {
			throw new MallaPeriodoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodoDto.listar.mallasPeriodo.exception")));
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
			throw new MallaPeriodoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodoDto.listar.mallasPeriodo.no.result.exception")));
		}	
		return retorno;
	}

	
	/**
	 * Realiza la busqueda de todas las mallas_periodo
	 * @return Lista todas las mallas_periodo por carrera de la aplicacion
	 * @throws MallaPeridoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaPeriodoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 * @throws MallaCurricularDtoException 
	 */
	public List<MallaPeriodoDto> listarMallasPeriodoXPeriodo(int pracId) throws MallaPeriodoDtoException, MallaPeriodoDtoNoEncontradoException{
		List<MallaPeriodoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ID);
			sbSql.append(" , mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);sbSql.append(" = ");sbSql.append(MallaCurricularConstantes.VIGENTE_MALLA_SI_VALUE);
			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);sbSql.append(" = ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);

			sbSql.append(" ORDER BY ");
			sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaPeriodoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new MallaPeriodoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodoDto.listar.mallasPeriodo.sql.exception")));
		} catch (Exception e) {
			throw new MallaPeriodoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodoDto.listar.mallasPeriodo.exception")));
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
			throw new MallaPeriodoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodoDto.listar.mallasPeriodo.no.result.exception")));
		}	
		return retorno;
	}

	public List<MallaPeriodoDto>  buscarMallasPorTipoPeriodo(int pracTipo, int mlprEstado) throws MallaPeriodoException, MallaPeriodoNoEncontradoException{
		List<MallaPeriodoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT MLPR.");sql.append(JdbcConstantes.MLPR_ID);
		sql.append(" ,MLPR.");sql.append(JdbcConstantes.MLPR_ESTADO);
		sql.append(" ,MLPR.");sql.append(JdbcConstantes.MLPR_PRAC_ID);
		sql.append(" ,MLPR.");sql.append(JdbcConstantes.MLPR_MLCR_ID);
		sql.append(" ,PRAC.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" ,PRAC.");sql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sql.append(" ,PRAC.");sql.append(JdbcConstantes.PRAC_TIPO);
		sql.append(" ,PRAC.");sql.append(JdbcConstantes.PRAC_ESTADO);
		sql.append(" ,MLCR.");sql.append(JdbcConstantes.MLCR_ID);
		sql.append(" ,MLCR.");sql.append(JdbcConstantes.MLCR_DESCRIPCION);
		sql.append(" ,CRR.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" ,CRR.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" FROM MALLA_PERIODO MLPR,  ");
		sql.append(" MALLA_CURRICULAR MLCR, ");
		sql.append(" CARRERA CRR, ");
		sql.append(" PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE MLPR.MLCR_ID = MLCR.MLCR_ID ");
		sql.append(" AND MLCR.CRR_ID = CRR.CRR_ID ");
		sql.append(" AND MLPR.PRAC_ID = PRAC.PRAC_ID ");
		sql.append(" AND PRAC.PRAC_TIPO = ? ");
		sql.append(" AND MLPR.MLPR_ESTADO = ? ");
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, pracTipo);
			pstmt.setInt(2, mlprEstado);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAMallaPeriodoDto(rs));
			}
		
		} catch (SQLException e) {
			throw new MallaPeriodoException("Error de validación en la cosulta, comuníquese con el administrador.");
		} catch (Exception e) {
			throw new MallaPeriodoException("Error de conexión, comuníquese con el administrador.");
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
		
		if(retorno.size()<=0){
			throw new MallaPeriodoNoEncontradoException("No se encontró resultados con los parámetros solicitados.");
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
	private MallaPeriodoDto transformarResultSetADto(ResultSet rs) throws SQLException{
		MallaPeriodoDto retorno = new MallaPeriodoDto();
			
		retorno.setMlprId(rs.getInt(JdbcConstantes.MLPR_ID));	
		retorno.setMlprMlcrId(rs.getInt(JdbcConstantes.MLCR_ID));
		retorno.setMlprPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setMlprEstado(rs.getInt(JdbcConstantes.MLPR_ESTADO));
		
		return retorno;
	} 
	
	
	private MallaPeriodoDto transformarResultSetAMallaPeriodoDto(ResultSet rs) throws SQLException{
		MallaPeriodoDto retorno = new MallaPeriodoDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		CarreraDto carrera = new CarreraDto();
		MallaCurricularDto mallaCurricular = new MallaCurricularDto();
		retorno.setMlprId(rs.getInt(JdbcConstantes.MLPR_ID));	
		retorno.setMlprEstado(rs.getInt(JdbcConstantes.MLPR_ESTADO));
		retorno.setMlprMlcrId(rs.getInt(JdbcConstantes.MLPR_MLCR_ID));
		retorno.setMlprPracId(rs.getInt(JdbcConstantes.MLPR_PRAC_ID));
		periodo.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		periodo.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		periodo.setPracTipo(rs.getInt(JdbcConstantes.PRAC_TIPO));
		periodo.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
		mallaCurricular.setMlcrId(rs.getInt(JdbcConstantes.MLCR_ID));
		mallaCurricular.setMlcrDescripcion(rs.getString(JdbcConstantes.MLCR_DESCRIPCION));
		mallaCurricular.setMlcrCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		carrera.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		carrera.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMlprPeriodoAcademicoDto(periodo);
		retorno.setMlprCarreraDto(carrera);
		retorno.setMlprMallaCurricularDto(mallaCurricular);
		return retorno;
	} 
	
}
