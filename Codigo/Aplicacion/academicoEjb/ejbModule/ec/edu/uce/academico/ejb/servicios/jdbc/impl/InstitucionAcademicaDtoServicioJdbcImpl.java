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
   
 ARCHIVO:     InstitucionAcademicaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla InstitucionAcademicaDto.
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

import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB InstitucionAcademicaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla InstitucionAcademicaDto.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class InstitucionAcademicaDtoServicioJdbcImpl implements InstitucionAcademicaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de instituciones académicas por cantón,tipo y nivel
	 * @param cantonId - id del cantón  a buscar
	 * @param tipoId - id del tipo  a buscar
	 * @return Lista de todas los instituciones académicas por cantón y tipo
	 * @throws InstitucionAcademicaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXCantonXTipo(int cantonId, int tipoId) throws InstitucionAcademicaDtoException, InstitucionAcademicaDtoNoEncontradoException{
		List<InstitucionAcademicaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
					
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ");
			
     		sbSql.append(" WHERE ");
     		sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID );
			sbSql.append(" = ? ");
     		sbSql.append(" AND ");
     		sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" = ? ");		
			  sbSql.append(" ORDER BY ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, cantonId);
			pstmt.setInt(2, tipoId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<InstitucionAcademicaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXCantonXtipo(rs));
			}
		} catch (SQLException e) {
			throw new InstitucionAcademicaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaDto.listar.por.canton.tipo.sql.exception")));
		} catch (Exception e) {
			throw new InstitucionAcademicaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaDto.listar.por.canton.tipo.exception")));
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
			throw new InstitucionAcademicaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaDto.listar.por.canton.tipo.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**MQ
	 * Realiza la busqueda de instituciones académicas Nivel (Universidad, Colegio)
	 * @param nivelId - id del cantón  a buscar
	 * @return Lista de todas los instituciones académicas por cantón y tipo
	 * @throws InstitucionAcademicaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXNivel( int nivelId) throws InstitucionAcademicaDtoException, InstitucionAcademicaDtoNoEncontradoException{
		List<InstitucionAcademicaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
					
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ");
			
     		sbSql.append(" WHERE ");
     		sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" = ? ");		
			  sbSql.append(" ORDER BY ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, nivelId);
		
			rs = pstmt.executeQuery();
			retorno = new ArrayList<InstitucionAcademicaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXCantonXtipo(rs));
			}
		} catch (SQLException e) {
			throw new InstitucionAcademicaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaDto.listar.por.canton.tipo.sql.exception")));
		} catch (Exception e) {
			throw new InstitucionAcademicaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaDto.listar.por.canton.tipo.exception")));
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
			throw new InstitucionAcademicaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaDto.listar.por.canton.tipo.no.result.exception")));
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
	private InstitucionAcademicaDto transformarResultSetADtoListarXCantonXtipo(ResultSet rs) throws SQLException{
		InstitucionAcademicaDto retorno = new InstitucionAcademicaDto();
			
		  retorno.setInacId(rs.getInt(JdbcConstantes.INAC_ID));
		  retorno.setInacDescripcion(rs.getString(JdbcConstantes.INAC_DESCRIPCION));
		  retorno.setInacCodigoSniese(rs.getString(JdbcConstantes.INAC_CODIGO_SNIESE));
		  retorno.setInacNivel(rs.getInt(JdbcConstantes.INAC_NIVEL));
		  retorno.setInacTipo(rs.getInt(JdbcConstantes.INAC_TIPO)); 
		  retorno.setInacTipo(rs.getInt(JdbcConstantes.INAC_TIPO_SNIESE));
		 
		  
		return retorno;
	} 
	
	
	
	
	
}
