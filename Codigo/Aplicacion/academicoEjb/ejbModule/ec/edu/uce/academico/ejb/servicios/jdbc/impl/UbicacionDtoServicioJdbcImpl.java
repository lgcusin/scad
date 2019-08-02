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
   
 ARCHIVO:     UbicacionDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla ubicacion.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-MARZ-2017		Dennis Collaguazo 			       Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB UbicacionDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla ubicacion.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class UbicacionDtoServicioJdbcImpl implements UbicacionDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un ubicacion por id
	 * @param ubicacionId - id del ubicacion
	 * @return Ubicacion con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		UbicacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
					sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
					sbSql.append(" ubcPrv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception",ubicacionId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los ubicaciones de la aplicacion
	 * @return Lista todos las ubicaciones de la aplicacion
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarTodos() throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPrv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" ORDER BY ");sbSql.append("ubc."); sbSql.append(JdbcConstantes.UBC_JERARQUIA);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	
	/**
	 * Realiza la busqueda de todas las ubicaciones por carrera de la aplicacion
	 * @return Lista todas las ubicaciones de la aplicacion
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarUbicacionesXCarrera(int carreraId, List<CarreraDto> lstCarreras) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		List<Integer> listaCarreras = new ArrayList<Integer>();
		if(carreraId == GeneralesConstantes.APP_ID_BASE){
			for (CarreraDto item : lstCarreras) {
				listaCarreras.add(item.getCrrId());
			}
		}else{
				listaCarreras.add(carreraId);
		}
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" distinct (");  sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(") , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");sbSql.append(" ubcPrv.");
			sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");sbSql.append(" ON ");
			sbSql.append("cncr.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" = ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			
			sbSql.append(" WHERE ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN (");
			for( int i = 0; i< listaCarreras.size(); i++){
				sbSql.append(" ? ");
				if(i != listaCarreras.size() -1) {
					sbSql.append(","); 
					}
			}
			sbSql.append(" ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			for (Integer item : listaCarreras) {
				contador++;
				pstmt.setInt(contador, item); //cargo las carreras ids
			}
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.carrea.exeption")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.carrea.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un ubicacion de pais por id
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarPaisXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		UbicacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPdr.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
				sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
				sbSql.append(" ubcPdr.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion ");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPdr ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPdr.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.pais.por.id.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.pais.por.id.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.pais.por.id.no.result.exception",ubicacionId)));
		}	
		
		return retorno;
	}

	
	
	/**
	 * Realiza la busqueda de una ubicacion de provincia por id de pais
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaProvinciaXPais(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPadre ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPadre.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_PROVINCIA_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.provincia.por.pais.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.provincia.por.pais.exeption")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.provincia.por.pais.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de una ubicacion de canton por id de provincia 
	 * @param ubicacionId - id del ubicacion de provincia 
	 * @return Ubicacion canton con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaCatonXProvincia(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion ");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPadre ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPadre.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.provincia.canton.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.provincia.canton.exeption")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.provincia.canton.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de las ubicaciones por carrera, vigencia, modalidad, titulo, tipo sede, tipo formacion
	 * @param tiseId - id de tipo sede
	 * @param tifrId - id de tipo de formacion 
	 * @param crrId - id de carrera
	 * @param vgnId - id de vigencia
	 * @param mdlId - id de modalidad
	 * @param ttlId - id de titulo
	 * @return Ubicaciones con los id solicitados 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaUbicacionXTiseXTifrXCrrXVgnXMdlXXttl(int tiseId, int tifrId, int crrId, int vgnId, int mdlId, int ttlId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" distinct (");  sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(") , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");sbSql.append(" ubcPrv.");
			sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");sbSql.append(" ON ");
			sbSql.append("cncr.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" = ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_VGN_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TISE_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TIFR_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId); //cargo el id de la ubicacion
			pstmt.setInt(2, vgnId); //cargo el id de la vigencia
			pstmt.setInt(3, mdlId); //cargo el id de la modalidad
			pstmt.setInt(4, ttlId); //cargo el id del titulo
			pstmt.setInt(5, tiseId); //cargo el id del tipo de sede
			pstmt.setInt(6, tifrId); //cargo el id del tipo de formacion
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.configuracion.carrea.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.configuracion.carrera.exeption")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.configuracion.carrea.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un ubicacion de pais por id
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarPadreXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		UbicacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_SUB_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc1 , ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc2 ");
			sbSql.append(" WHERE ");
			sbSql.append("ubc1.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubc2.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" AND ubc1.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoSoloUbicacion(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sub.id.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sub.id.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sub.id.no.result.exception",ubicacionId)));
		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de las ubicaciones por jerarquia
	 * @return Lista todos las ubicaciones de la aplicacion por jerarquia
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarXjerarquia(int idJerarquia) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPrv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc."); sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ubc."); sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idJerarquia); //cargo el id de la jerarquia
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.jerarquia.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.jerarquia.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.jerarquia.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de una ubicacion de parroquia por id del canton
	 * @param ubicacionId - id del ubicacion del canton
	 * @return Ubicacion parroquia con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaParroquiasXCanton(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion ");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPadre ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);sbSql.append(" = ");sbSql.append("ubcPadre.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_SUB_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.parroquias.por.canton.sql.exception")));
		} catch (Exception e) {
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.parroquias.por.canton.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.parroquias.por.canton.no.result.exception")));
		}	
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private UbicacionDto transformarResultSetADto(ResultSet rs) throws SQLException{
		UbicacionDto retorno = new UbicacionDto();
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		retorno.setUbcJerarquia(rs.getInt(JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		retorno.setUbcPadre(rs.getInt(JdbcConstantes.UBC_SUB_ID));
		retorno.setUbcPadreDescripcion(rs.getString("padre_descripcion"));
		return retorno;
	}
	
	private UbicacionDto transformarResultSetADtoSoloUbicacion(ResultSet rs) throws SQLException{
		UbicacionDto retorno = new UbicacionDto();
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		retorno.setUbcJerarquia(rs.getInt(JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		retorno.setUbcPadre(rs.getInt(JdbcConstantes.UBC_SUB_ID));
		return retorno;
	}
	
	
}
