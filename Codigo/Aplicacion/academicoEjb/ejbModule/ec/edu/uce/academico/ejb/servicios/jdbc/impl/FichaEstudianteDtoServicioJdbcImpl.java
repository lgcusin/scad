package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.FichaEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

public class FichaEstudianteDtoServicioJdbcImpl implements FichaEstudianteDtoServicioJdbc {
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	public List<FichaEstudianteDto> buscarXId(int fcesId)
			throws FichaEstudianteDtoException, FichaEstudianteDtoNoEncontradoException {
		List<FichaEstudianteDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION); 
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ESTADO_UNIVERSITARIO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ESTADO_MATRICULA);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_OBSERVACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fcesId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXid.FichaEstudiante.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXid.FichaEstudiante.exception")));
		}finally{
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
			throw new  FichaEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXid.FichaEstudiante.no.result.exception")));
		}
		
		return retorno;
	}
	
	public List<FichaEstudianteDto> buscarXPersonaId(int fcesPersonaId)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException {
		List<FichaEstudianteDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION); 
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ESTADO_UNIVERSITARIO);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ESTADO_MATRICULA);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_OBSERVACION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fcesPersonaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		}catch(SQLException e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.sql.exception")));
		}catch(Exception e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.exception")));
		}finally{
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
			throw new  FichaEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.no.result.exception")));
		}
		
		return retorno;
		
		
	}
	
	
	
	
	
	private FichaEstudianteDto transformarResultSetADto(ResultSet rs)throws SQLException {
		FichaEstudianteDto retorno = new FichaEstudianteDto();
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesFechaInicio(rs.getTimestamp(JdbcConstantes.FCES_FECHA_INICIO));
		retorno.setFcesFechaEgresamiento(rs.getTimestamp(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		retorno.setFcesFechaActaGrado(rs.getTimestamp(JdbcConstantes.FCES_FECHA_ACTA_GRADO));
		retorno.setFcesNumActaGrado(rs.getString(JdbcConstantes.FCES_NUM_ACTA_GRADO));
		retorno.setFcesFechaRefrendacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_REFRENDACION));
		retorno.setFcesNumRefrendacion(rs.getString(JdbcConstantes.FCES_NUM_REFRENDACION));
		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		retorno.setFcesTipoDuracRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
		retorno.setFcesNotaPromAcumulado(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO));
		retorno.setFcesNotaTrabTitulacion(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION));
		retorno.setFcesLinkTesis(rs.getString(JdbcConstantes.FCES_LINK_TESIS));
		retorno.setFcesRecEstudPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
		retorno.setFcesRecEstudPrevSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		retorno.setFcesPrsId(rs.getInt(JdbcConstantes.FCES_PRS_ID));
		retorno.setFcesInstitucionAcademicaId(rs.getInt(JdbcConstantes.FCES_INAC_ID));
		retorno.setFcesFcinId(rs.getInt(JdbcConstantes.FCES_FCIN_ID));
		retorno.setFcesEstadoUniversitario(rs.getInt(JdbcConstantes.FCES_ESTADO_UNIVERSITARIO));
		retorno.setFcesEstadoMatricula(rs.getInt(JdbcConstantes.FCES_ESTADO_MATRICULA));
		retorno.setFcesObservacion(rs.getString(JdbcConstantes.FCES_OBSERVACION));
		retorno.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
		return retorno;
	}
	
}
