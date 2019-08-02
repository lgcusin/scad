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
 19-05-2017		Marcelo Quishpe				       Emisión Inicial
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
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB DependenciaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla DependenciaDto.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class DependenciaDtoServicioJdbcImpl implements DependenciaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de las dependencias a las cuales tiene acceso el Usuario
	 * @param usuarioId - id del usuario 
	 * @return Lista de Dependencias  por el usuario que accede 
	 * @throws DependenciaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw DependenciaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DependenciaDto> listarXUsuario(int usuarioId) throws DependenciaDtoException, DependenciaDtoNoEncontradoException{
		List<DependenciaDto> retorno= null;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
				
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ESTADO);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_CAMPUS);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_JERARQUIA);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);	
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");			
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);

			rs = pstmt.executeQuery();
			retorno = new ArrayList<DependenciaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuario(rs));
			}
		} catch (SQLException e) {
			throw new DependenciaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DependenciaDto.buscar.por.usuario.sql.exception")));
		} catch (Exception e) {
			throw new DependenciaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DependenciaDto.buscar.por.usuario.exception")));
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
			throw new DependenciaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DependenciaDto.buscar.por.usuario.no.result.exception")));
		}	
		return  retorno;
	}

	
	public List<DependenciaDto> buscarDependencias(int usuarioId, int rolId) throws DependenciaDtoException, DependenciaDtoNoEncontradoException{
		List<DependenciaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   DPN.DPN_ESTADO ");
		sql.append(" FROM ROL_FLUJO_CARRERA ROFLCR, ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   USUARIO USR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN ");
		sql.append(" WHERE DPN.DPN_ID         = CRR.DPN_ID ");
		sql.append(" AND CRR.CRR_ID           = ROFLCR.CRR_ID ");
		sql.append(" AND ROFLCR.USRO_ID       = USRO.USRO_ID ");
		sql.append(" AND USR.USR_ID           = USRO.USR_ID ");
		sql.append(" AND USRO.USRO_ESTADO     =  " + UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND ROFLCR.ROFLCR_ESTADO =  " + RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND USRO.ROL_ID          = ? ");
		sql.append(" AND USR.USR_ID           = ? ");

		try {
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, rolId);
			pstmt.setInt(2, usuarioId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<DependenciaDto>();
			while(rs.next()){
				retorno.add(rsAbuscarDependencias(rs));
			}
			
		} catch (SQLException e) {
			throw new DependenciaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DependenciaDto.buscar.por.usuario.sql.exception")));
		} catch (Exception e) {
			throw new DependenciaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DependenciaDto.buscar.por.usuario.exception")));
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
		
		if(retorno.isEmpty()){
			throw new DependenciaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DependenciaDto.buscar.por.usuario.no.result.exception")));
		}	
		return  retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	

	/**
	 * Método privado que sirve para trasformar los datos del resulset XUsuario
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private DependenciaDto transformarResultSetADtoXUsuario(ResultSet rs) throws SQLException{
		DependenciaDto retorno = new DependenciaDto();
		  retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		  retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		  retorno.setDpnEstado(rs.getInt(JdbcConstantes.DPN_ESTADO));
		  retorno.setDpnCampus(rs.getInt(JdbcConstantes.DPN_CAMPUS));
		  retorno.setDpnJerarquia(rs.getInt(JdbcConstantes.DPN_JERARQUIA)); 
		 
		return retorno;
	} 
	
	private DependenciaDto rsAbuscarDependencias(ResultSet rs) throws SQLException{
		DependenciaDto retorno = new DependenciaDto();
		  retorno.setDpnId(rs.getInt(1));
		  retorno.setDpnDescripcion(rs.getString(2));
		  retorno.setDpnEstado(rs.getInt(3));
		return retorno;
	}

	
}
