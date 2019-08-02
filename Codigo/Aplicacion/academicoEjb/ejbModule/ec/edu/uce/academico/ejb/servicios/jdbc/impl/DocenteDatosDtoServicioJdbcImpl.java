/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducciÃ³n o distribuciÃ³n no autorizada de este programa, 
 * o cualquier porciÃ³n de Ã©l, puede dar lugar a sanciones criminales y 
 * civiles severas, y serÃ¡n procesadas con el grado mÃ¡ximo contemplado 
 * por la ley.
  ************************************************************************* 
   
 ARCHIVO:     DocenteDatosDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc del DTO PersonaDatos.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
29-08-2017            Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.DocenteDatosDto;
import ec.edu.uce.academico.ejb.excepciones.DocenteDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
/**
 * EJB DocenteDatosDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc del DTO PersonaDatos.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class DocenteDatosDtoServicioJdbcImpl implements DocenteDatosDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param dcnId ide del docente a buscar
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws DocenteDatosDtoNoEncontradoException DocenteDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws DocenteDatosDtoException DocenteDatosDtoException excepción general lanzada
	 */
	public DocenteDatosDto buscarPdcnIdProlId(int dcnId, int rolId) throws DocenteDatosDtoNoEncontradoException, DocenteDatosDtoException{
		
		DocenteDatosDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			  
			StringBuilder sbSql = new StringBuilder(); 
			sbSql.append("SELECT DISTINCT "); 
			sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" , USR.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" , ROL.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" , ROL.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" , CRR.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , CRR.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			  
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" RLFLCR "); 
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" =");
			sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND");
			sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" =");
			sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND");
			sbSql.append(" RLFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" =");
			sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND");
			sbSql.append(" RLFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" =");
			sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND");
			sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND");
			sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, dcnId);
			pstmt.setInt(2, rolId);
			 
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		
		} catch (SQLException e) {  
			throw new DocenteDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "DocenteDatosDto.buscar.dcnid.rolid.sql.exception")));
		} catch (Exception e) { 
			throw new DocenteDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "DocenteDatosDto.buscar.dcnid.rolid.exception")));
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
		
		if (retorno == null ) { 
			throw new DocenteDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "DocenteDatosDto.buscar.dcnid.rolid.no.result.exception")));
		}
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private DocenteDatosDto transformarResultSetADto(ResultSet rs) throws SQLException{
		DocenteDatosDto retorno = new DocenteDatosDto();
		
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
	
		return retorno;
	}


}

