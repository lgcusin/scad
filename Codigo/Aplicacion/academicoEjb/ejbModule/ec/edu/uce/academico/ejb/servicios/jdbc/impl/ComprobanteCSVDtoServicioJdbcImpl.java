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
   
 ARCHIVO:     ComprobanteDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla Comprobante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-04-2017			Daniel Albuja					       Emisión Inicial
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
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.ComprobanteCSVDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobanteCSVDtoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobanteCSVDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobanteCSVDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * EJB ComprobanteDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Comprobante.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class ComprobanteCSVDtoServicioJdbcImpl implements ComprobanteCSVDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	@Override
	public List<ComprobanteCSVDto> buscarEmitidosGeneracionCSV()
			throws  ComprobanteCSVDtoNoEncontradoException , ComprobanteCSVDtoException {
		
		List<ComprobanteCSVDto> retorno = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		sbSql.append(" DISTINCT cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_NUM_COMPROBANTE);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_FACULTAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FECHA_EMISION);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FECHA_CADUCA);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ESTADO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ESPE_CODIGO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_MODALIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_MATR_TIPO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_APLICA_GRATUIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_PAI_CODIGO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_DESCRIPCION);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO);
		
		
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_CELULAR);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID_ARANCEL);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_CANTIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_VALOR_PAGADO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TIPO_UNIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_PROC_SAU);
		sbSql.append(" , ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
		sbSql.append(" , ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
		sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa, ");
		sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt, ");
		sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
		sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
		sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
		sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
		
		sbSql.append(" WHERE ");
		sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
		sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
		sbSql.append(" AND fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
		sbSql.append(" AND cmpa.");sbSql.append(JdbcConstantes.CMPA_ESTADO);sbSql.append(" = ");sbSql.append(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
		sbSql.append(" AND cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
		sbSql.append(" AND dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
		sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" AND cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);sbSql.append(" <> 0");
		sbSql.append(" AND (fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);sbSql.append(" = 11");
		sbSql.append(" OR fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);sbSql.append(" >= 100)");
		sbSql.append(" ORDER BY cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ComprobanteCSVDto>();
			while(rs.next()){
				retorno.add(transformarResultSet(rs));
			}
			if( rs != null){
				rs.close();
			}
			if (pstmt != null){
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			return retorno;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
			}
			throw new ComprobanteCSVDtoException("Error al consultar comprobantes");
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
	}
	
	
	@Override
	public ComprobanteCSVDto buscarComprobantePagoParaEditarReimpresion(String numComprobante)
			throws  ComprobanteCSVDtoNoEncontradoException , ComprobanteCSVDtoException {
		
		ComprobanteCSVDto retorno = new ComprobanteCSVDto();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		sbSql.append(" DISTINCT cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_NUM_COMPROBANTE);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_FACULTAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FECHA_EMISION);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FECHA_CADUCA);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ESTADO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ESPE_CODIGO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_MODALIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_MATR_TIPO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_APLICA_GRATUIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_PAI_CODIGO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_DESCRIPCION);
		sbSql.append(" , ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO);
		
		
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_CELULAR);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID_ARANCEL);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_CANTIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_VALOR_PAGADO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TIPO_UNIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_PROC_SAU);
		sbSql.append(" , ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
		
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
		sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa, ");
		sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt, ");
		sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
		sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
		sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
		sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
		
		sbSql.append(" WHERE ");
		sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
		sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
		sbSql.append(" AND fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
		sbSql.append(" AND cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
		sbSql.append(" AND dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
		sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
		
		sbSql.append(" AND cmpa.");sbSql.append(JdbcConstantes.CMPA_CODIGO);sbSql.append(" LIKE ? ");
		
		sbSql.append(" ORDER BY cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, numComprobante);
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno=transformarResultSet(rs);
			}
			if( rs != null){
				rs.close();
			}
			if (pstmt != null){
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			return retorno;
		} catch (SQLException e) {
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
			} catch (Exception e2) {
			}
			throw new ComprobanteCSVDtoException("Error al consultar comprobantes");
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
	}
	
	@Override
	public List<ComprobanteCSVDto> buscarComprobantesPagoParaEditarReimpresion(String numComprobante)
			throws  ComprobanteCSVDtoNoEncontradoException , ComprobanteCSVDtoException {
		
		List<ComprobanteCSVDto> retorno = new ArrayList<ComprobanteCSVDto>();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		sbSql.append(" DISTINCT cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_NUM_COMPROBANTE);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_FACULTAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FECHA_EMISION);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FECHA_CADUCA);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ESTADO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ESPE_CODIGO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_MODALIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_MATR_TIPO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_APLICA_GRATUIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_PAI_CODIGO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_DESCRIPCION);
		sbSql.append(" , ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SECTOR_DOMICILIO);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_CALLE_PRINCIPAL);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_CALLE_SECUNDARIA);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_NUMERO_CASA);
		sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO); sbSql.append(" IS NULL THEN ");sbSql.append("'NA'");
		sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO); 
		sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_REFERENCIA_DOMICILIO);
		
		
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_CELULAR);
		sbSql.append(" , ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID_ARANCEL);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_CANTIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_VALOR_PAGADO);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TIPO_UNIDAD);
		sbSql.append(" , ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_PROC_SAU);
		sbSql.append(" , ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
		
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
		sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa, ");
		sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin, ");
		sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
		sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
		sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
		
		sbSql.append(" WHERE ");
		sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
		sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
		sbSql.append(" AND fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
		sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" AND fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
		sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
		sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
		sbSql.append(" AND fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
		
		sbSql.append(" AND cmpa.");sbSql.append(JdbcConstantes.CMPA_NUM_COMPROBANTE);sbSql.append(" LIKE ? ");
		
		sbSql.append(" ORDER BY cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, numComprobante);
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSet(rs));
			}
			if( rs != null){
				rs.close();
			}
			if (pstmt != null){
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			return retorno;
		} catch (SQLException e) {
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
			} catch (Exception e2) {
			}
			throw new ComprobanteCSVDtoException("Error al consultar comprobantes");
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
	}
	
	public ComprobanteCSVDto transformarResultSet(ResultSet rs){
		ComprobanteCSVDto retorno = new ComprobanteCSVDto();
		try {
			retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
			retorno.setCmpaNumComprobante(rs.getString(JdbcConstantes.CMPA_NUM_COMPROBANTE));
			retorno.setCmpaTotalPago(rs.getBigDecimal(JdbcConstantes.CMPA_TOTAL_PAGO));
			retorno.setCmpaTotalFacultad(rs.getBigDecimal(JdbcConstantes.CMPA_TOTAL_FACULTAD));
			retorno.setCmpaFechaEmision(rs.getTimestamp(JdbcConstantes.CMPA_FECHA_EMISION));
			retorno.setCmpaFechaCaduca(rs.getTimestamp(JdbcConstantes.CMPA_FECHA_CADUCA));
			retorno.setCmpaEstado(rs.getInt(JdbcConstantes.CMPA_ESTADO));
			retorno.setCmpaEspeCodigo(rs.getInt(JdbcConstantes.CMPA_ESPE_CODIGO));
			retorno.setCmpaModalidad(rs.getInt(JdbcConstantes.CMPA_MODALIDAD));
			retorno.setCmpaMatrTipo(rs.getInt(JdbcConstantes.CMPA_MATR_TIPO));
			retorno.setCmpaAplicaGratuidad(rs.getInt(JdbcConstantes.CMPA_APLICA_GRATUIDAD));
			retorno.setCmpaPaiCodigo(rs.getInt(JdbcConstantes.CMPA_PAI_CODIGO));
			retorno.setCmpaDescripcion(rs.getString(JdbcConstantes.CMPA_DESCRIPCION));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			StringBuilder direccion = new StringBuilder();
			if(!rs.getString(JdbcConstantes.PRS_SECTOR_DOMICILIO).equals("NA")){
				direccion.append(rs.getString(JdbcConstantes.PRS_SECTOR_DOMICILIO));
				direccion.append(" ");
			}
			if(!rs.getString(JdbcConstantes.PRS_CALLE_PRINCIPAL).equals("NA")){
				direccion.append(rs.getString(JdbcConstantes.PRS_CALLE_PRINCIPAL));
				direccion.append(" ");
			}
			if(!rs.getString(JdbcConstantes.PRS_NUMERO_CASA).equals("NA")){
				direccion.append(rs.getString(JdbcConstantes.PRS_NUMERO_CASA));
				direccion.append(" ");
			}
			if(!rs.getString(JdbcConstantes.PRS_CALLE_SECUNDARIA).equals("NA")){
				direccion.append(rs.getString(JdbcConstantes.PRS_CALLE_SECUNDARIA));
				direccion.append(" ");
			}
			if(!rs.getString(JdbcConstantes.PRS_REFERENCIA_DOMICILIO).equals("NA")){
				direccion.append(rs.getString(JdbcConstantes.PRS_REFERENCIA_DOMICILIO));
				direccion.append(" ");
			}
			try {
				String direccion1 = GeneralesUtilidades.eliminarEnterYEspaciosEnBlanco(direccion.toString());
				if(direccion1.length()==0){
					retorno.setPrsDireccion("AVENIDA UNIVERSITARIA");	
				}else{
					retorno.setPrsDireccion(direccion.toString());	
				}
								
			} catch (Exception e) {
				retorno.setPrsDireccion("AVENIDA UNIVERSITARIA");
			}

			
			retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
			retorno.setPrsCelular(rs.getString(JdbcConstantes.PRS_CELULAR));
			retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL).toLowerCase());
			
			retorno.setCmpaIdArancel(rs.getInt(JdbcConstantes.CMPA_ID_ARANCEL));
			
			
			retorno.setCmpaCantidad(rs.getInt(JdbcConstantes.CMPA_CANTIDAD));
			retorno.setCmpaValorPagado(rs.getBigDecimal(JdbcConstantes.CMPA_VALOR_PAGADO));
			retorno.setCmpaTipoUnidad(rs.getInt(JdbcConstantes.CMPA_TIPO_UNIDAD));
			retorno.setCmpaProcSau(rs.getInt(JdbcConstantes.CMPA_PROC_SAU));
			retorno.setCmpaFichaMatricula(rs.getInt(JdbcConstantes.FCMT_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DETALLE));
		} catch (SQLException e) {
			System.out.println("ERROR : "+retorno.getPrsIdentificacion());
		}
		
		return retorno;
	}
	
	
}
