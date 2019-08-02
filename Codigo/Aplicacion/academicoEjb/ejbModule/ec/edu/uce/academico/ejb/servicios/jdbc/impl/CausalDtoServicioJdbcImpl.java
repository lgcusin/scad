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
   
 ARCHIVO:     CausalDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla Causal.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-01-2019		Marcelo Quishpe				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.CausalDto;
import ec.edu.uce.academico.ejb.excepciones.CausalDtoException;
import ec.edu.uce.academico.ejb.excepciones.CausalDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CausalDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB CausalDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Causal.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class CausalDtoServicioJdbcImpl implements CausalDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de todas las causales por tipo de causal
	 * @param causalId - id del usuario 
	 * @return Lista de todas las causales por tipo de causal
	 * @throws CausalDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CausalDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CausalDto> listarXidTipoCausal(int tipoCausalId) throws CausalDtoException, CausalDtoNoEncontradoException{
		List<CausalDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ESTADO);
			sbSql.append(" , tics.");sbSql.append(JdbcConstantes.TICS_ID);
			sbSql.append(" , tics.");sbSql.append(JdbcConstantes.TICS_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_CAUSAL);sbSql.append(" tics ");
			
     		sbSql.append(" WHERE ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_TICS_ID);
			sbSql.append(" = ");sbSql.append(" tics.");sbSql.append(JdbcConstantes.TICS_ID);
     		sbSql.append(" AND ");sbSql.append(" tics.");sbSql.append(JdbcConstantes.TICS_ID);
			sbSql.append(" = ? ");		
			 
			sbSql.append(" ORDER BY ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoCausalId);
            			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CausalDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXListarXidTipoCausal(rs));
			}
		} catch (SQLException e) {
			
			throw new CausalDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDto.buscar.por.tipoCausal.sql.exception")));
		} catch (Exception e) {
			
			throw new CausalDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDto.buscar.por.tipoCausal.exception")));
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
			
			throw new CausalDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDto.buscar.por.tipoCausal.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset XUsuarioXdependenciaXEdificio
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CausalDto transformarResultSetADtoXListarXidTipoCausal(ResultSet rs) throws SQLException{
		CausalDto retorno = new CausalDto();
				
		retorno.setCslId(rs.getInt(JdbcConstantes.CSL_ID));
		retorno.setCslCodigo(rs.getString(JdbcConstantes.CSL_CODIGO));
		retorno.setCslDescripcion(rs.getString(JdbcConstantes.CSL_DESCRIPCION));
		retorno.setCslEstado(rs.getInt(JdbcConstantes.CSL_ESTADO));
		retorno.setTicsId(rs.getInt(JdbcConstantes.TICS_ID));
		retorno.setTicsDescripcion(rs.getString(JdbcConstantes.TICS_DESCRIPCION));

		return retorno;
	} 
	
	
	
	
	
	
	
}
