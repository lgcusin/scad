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
   
 ARCHIVO:     TituloDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla TituloDto.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-NOV-2017		Marcelo Quishpe				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB TituloDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla TituloDto.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class TituloDtoServicioJdbcImpl implements TituloDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de Titulos académicos  tipo (secundario, 3erNivel, 4to Nivel)
	 * @param tipoId - id del tipo  a buscar
	 * @return Lista de todas los instituciones académicas por cantón y tipo
	 * @throws TituloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoNoEncontradoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<TituloDto> listarXTipo(int tipoId) throws TituloDtoException, TituloDtoNoEncontradoException{
		List<TituloDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_ESTADO);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
     		sbSql.append(" WHERE ");
     		sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_TIPO );
			sbSql.append(" = ? ");
    		sbSql.append(" ORDER BY ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoId);
            
			rs = pstmt.executeQuery();
			retorno = new ArrayList<TituloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXtipo(rs));
			}
		} catch (SQLException e) {
			throw new TituloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloDto.listar.por.tipo.sql.exception")));
		} catch (Exception e) {
			throw new TituloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloDto.listar.por.tipo..exception")));
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
			throw new TituloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloDto.listar.por.tipo.no.result.exception")));
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
	private TituloDto transformarResultSetADtoListarXtipo(ResultSet rs) throws SQLException{
		TituloDto retorno = new TituloDto();
			
		  retorno.setTtlId(rs.getInt(JdbcConstantes.TTL_ID));
		  retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		  retorno.setTtlEstado(rs.getInt(JdbcConstantes.TTL_ESTADO));
		  retorno.setTtlSexo(rs.getInt(JdbcConstantes.TTL_SEXO));
		  retorno.setTtlTipo(rs.getInt(JdbcConstantes.TTL_TIPO)); 
		  
		 
		  
		return retorno;
	} 
	
	
	
	
	
}
