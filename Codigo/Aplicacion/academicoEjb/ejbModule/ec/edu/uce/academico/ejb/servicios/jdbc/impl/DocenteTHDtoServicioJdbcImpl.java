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

 ARCHIVO:     DocenteTHDtoServicioJdbcImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la base de talento humano, docente 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 28/05/2019           Dennis Collaguazo	                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.DocenteTHJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.DocenteTHDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteTHDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteTHDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * Clase (Bean)DocenteTHDtoServicioJdbcImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class DocenteTHDtoServicioJdbcImpl implements DocenteTHDtoServicioJdbc{

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_TALENTO_HUMANO)
	private DataSource dsTH;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Método que busca el docente por cédula en la base de talento humano
	 * @param cedula - cedula del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado en la base de talento humano
	 * @throws DocenteTHDtoNoEncontradoException - DocenteTHDtoNoEncontradoException lanzada cuando no se encuentra docentes
	 * @throws DocenteTHDtoException - DocenteTHDtoException excepción general lanzada 
	 */
	public DocenteTHJdbcDto buscarDocenteTHXCedula(String cedula) throws DocenteTHDtoNoEncontradoException, DocenteTHDtoException {
		DocenteTHJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct ");
			sbSql.append(" prs.prs_identificacion, pst.pst_id, pst.pst_denominacion, ctrn.ctrn_id,rngr.rngr_id, rngr.rngr_descripcion, cttm.cttm_id ");
			sbSql.append(" , ctdc.ctdc_id, ctdc.ctdc_descripcion, tmdd.tmdd_id, tmdd.tmdd_descripcion, dpn.dpn_id, dpn.dpn_descripcion, rllb.rllb_id, rllb.rllb_descripcion ");
			sbSql.append(" , crr.crr_id, pst.pst_tipo_puesto ");
			sbSql.append(" from CLASIFICADOR_PRODUCCION.persona prs, CLASIFICADOR_PRODUCCION.ficha_empleado fcem, CLASIFICADOR_PRODUCCION.detalle_puesto dtps, CLASIFICADOR_PRODUCCION.categoria_rango ctrn, CLASIFICADOR_PRODUCCION.rango_gradual rngr, CLASIFICADOR_PRODUCCION.categoria_tiempo cttm ");
			sbSql.append(" , CLASIFICADOR_PRODUCCION.categoria_docente ctdc, CLASIFICADOR_PRODUCCION.tiempo_dedicacion tmdd, CLASIFICADOR_PRODUCCION.puesto pst, CLASIFICADOR_PRODUCCION.dependencia dpn, CLASIFICADOR_PRODUCCION.relacion_laboral rllb, CLASIFICADOR_PRODUCCION.carrera crr ");
			sbSql.append(" where prs.prs_id = fcem.prs_id ");
			sbSql.append(" and fcem.fcem_id = dtps.fcem_id ");
			sbSql.append(" and pst.pst_id = dtps.pst_id ");
			sbSql.append(" and dpn.dpn_id = dtps.dpn_id ");
			sbSql.append(" and rllb.rllb_id = dtps.rllb_id ");
			sbSql.append(" and ctrn.ctrn_id = pst.ctrn_id ");
			sbSql.append(" and rngr.rngr_id = ctrn.rngr_id ");
			sbSql.append(" and cttm.cttm_id = ctrn.cttm_id ");
			sbSql.append(" and ctdc.ctdc_id = cttm.ctdc_id ");
			sbSql.append(" and tmdd.tmdd_id = cttm.tmdd_id ");
			sbSql.append(" and crr.crr_id = dtps.crr_id ");
			sbSql.append(" and dtps.dtps_estado = 0 ");
			sbSql.append(" and prs.prs_identificacion ");sbSql.append(" = ? ");
			sbSql.append(" order by dpn.dpn_descripcion ");
			
			con = dsTH.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, cedula.toUpperCase().trim().toString()); //cargo la cédula del docente a consultar
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocenteTHDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteTHDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DocenteTHDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteTHDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DocenteTHDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteTHDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
		}	

		return retorno;
	}

	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteTHJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		DocenteTHJdbcDto retorno = new DocenteTHJdbcDto();
		
		retorno.setPrsIdentificacion(rs.getString(1));
		retorno.setPstId(rs.getInt(2));
		retorno.setPstDenominacion(rs.getString(3));
		retorno.setCtrnId(rs.getInt(4));
		retorno.setRngrId(rs.getInt(5));
		retorno.setRngrDescripcion(rs.getString(6));
		retorno.setCttmId(rs.getInt(7));
		retorno.setCtdcId(rs.getInt(8));
		retorno.setCtdcDescripcion(rs.getString(9));
		retorno.setTmddId(rs.getInt(10));
		retorno.setTmddDescripcion(rs.getString(11));
		retorno.setDpnId(rs.getInt(12));
		retorno.setDpnDescripcion(rs.getString(13));
		retorno.setRllbId(rs.getInt(14));
		retorno.setRllbDescripcion(rs.getString(15));
		retorno.setCrrId(rs.getInt(16));
		retorno.setPstTipoPuesto(rs.getInt(17));
		return retorno;
	} 
	
	
}
