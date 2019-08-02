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
   
 ARCHIVO:    DependenciaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla Dependencia.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-05-2017		Marcelo Quishpe				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EdificioDto;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.EdificioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB EdificioDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla EdificioDto.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class EdificioDtoServicioJdbcImpl implements EdificioDtoServicioJdbc{
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	/**
	 * Realiza la busqueda de los edificios por la dependencia
	 * @param usuarioId - id de la dependencia
	 * @return Lista de Edificios  por dependencia 
	 * @throws EdificioDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw EdificioDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<EdificioDto> listarXDependencia(int dependenciaId, List<DependenciaDto> listDependencia) throws EdificioDtoException, EdificioDtoNoEncontradoException{
		List<EdificioDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_CODIGO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ESTADO);
			
			sbSql.append(" , CASE WHEN edf.");sbSql.append(JdbcConstantes.EDF_LOCALIZACION); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" ELSE edf.");sbSql.append(JdbcConstantes.EDF_LOCALIZACION); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.EDF_LOCALIZACION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);

			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			
			if(dependenciaId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listDependencia.size(); i++) {
					sbSql.append(" ? ");
					if(i != listDependencia.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}else{
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
			}
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			
			if(dependenciaId == GeneralesConstantes.APP_ID_BASE){ // caso para setear la lista de facultades
				for (DependenciaDto item : listDependencia) {
					contador++;
					pstmt.setInt(contador, item.getDpnId()); //cargo las facultades ids
				}
			}else{//caso para setear una sola facultades
				contador++;
				pstmt.setInt(contador, dependenciaId); //cargo el id de facultades
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EdificioDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXDependencia(rs));
			}
		} catch (SQLException e) {
			throw new EdificioDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.lista.sql.exception")));
		} catch (Exception e) {
			throw new EdificioDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.lista.exception")));
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
			throw new EdificioDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.lista.no.result.exception")));
		}	
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de los edificios por la dependencia Id
	 * @param dependenciaId - id de la dependencia
	 * @throws EdificioDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw EdificioDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<EdificioDto> listarEdificiosPdpnId(int dependenciaId) throws EdificioDtoException, EdificioDtoNoEncontradoException{
		List<EdificioDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_CODIGO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");	
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, dependenciaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EdificioDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarEdificiosPdpnId(rs));
			}
		} catch (SQLException e) {
			throw new EdificioDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.sql.exception")));
		} catch (Exception e) {
			throw new EdificioDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.exception")));
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
			throw new EdificioDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.no.result.exception")));
		}	


		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de los edificios por la dependencia
	 * @param usuarioId - id de la dependencia
	 * @param listDependencia - listDependencia de dependencias
	 * @return Lista de Edificios  por dependencia 
	 * @throws EdificioDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw EdificioDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<EdificioDto> listarXDependenciaXEstadoActivo(int dependenciaId, List<DependenciaDto> listDependencia) throws EdificioDtoException, EdificioDtoNoEncontradoException{
		List<EdificioDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_CODIGO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ESTADO);
			
			sbSql.append(" , CASE WHEN edf.");sbSql.append(JdbcConstantes.EDF_LOCALIZACION); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" ELSE edf.");sbSql.append(JdbcConstantes.EDF_LOCALIZACION); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.EDF_LOCALIZACION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);

			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ESTADO);
			sbSql.append(" = ");sbSql.append(EdificioConstantes.ESTADO_ACTIVO_VALUE);
			
			if(dependenciaId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listDependencia.size(); i++) {
					sbSql.append(" ? ");
					if(i != listDependencia.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}else{
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
			}
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			
			if(dependenciaId == GeneralesConstantes.APP_ID_BASE){ // caso para setear la lista de facultades
				for (DependenciaDto item : listDependencia) {
					contador++;
					pstmt.setInt(contador, item.getDpnId()); //cargo las facultades ids
				}
			}else{//caso para setear una sola facultades
				contador++;
				pstmt.setInt(contador, dependenciaId); //cargo el id de facultades
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EdificioDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXDependencia(rs));
			}
		} catch (SQLException e) {
			throw new EdificioDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.lista.sql.exception")));
		} catch (Exception e) {
			throw new EdificioDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.lista.exception")));
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
			throw new EdificioDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EdificioDto.buscar.por.dependencia.lista.no.result.exception")));
		}	
		return  retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset XDependencia
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private EdificioDto transformarResultSetADtoXDependencia(ResultSet rs) throws SQLException{
		int valAux = 0;
		EdificioDto retorno = new EdificioDto();
		retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
		retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
		retorno.setEdfEstado(rs.getInt(JdbcConstantes.EDF_ESTADO));
		retorno.setEdfCodigo(rs.getString(JdbcConstantes.EDF_CODIGO));
		valAux = rs.getInt(JdbcConstantes.EDF_LOCALIZACION);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setEdfLocalizacion(valAux);
			retorno.setEdfLocalizacionSt(Integer.toString(valAux));
		}else{
			retorno.setEdfLocalizacion(null);
			retorno.setEdfLocalizacionSt("");
		}
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset XDependencia
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private EdificioDto transformarResultSetADtoListarEdificiosPdpnId(ResultSet rs) throws SQLException{
		
		EdificioDto retorno = new EdificioDto();
		retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
		retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
		retorno.setEdfEstado(rs.getInt(JdbcConstantes.EDF_ESTADO));
		retorno.setEdfCodigo(rs.getString(JdbcConstantes.EDF_CODIGO));
		return retorno;
	} 
	
	
	
}
