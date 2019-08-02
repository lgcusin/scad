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
   
 ARCHIVO:     CorequisitoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla CorequisitoDto.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-07-2017		Marcelo Quishpe				       Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CorequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB CorequisitoDtoServicioJdbcImpl. Clase donde se implementan los metodos
 * para el servicio jdbc de la tabla CorequisitoDto.
 * 
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class CorequisitoDtoServicioJdbcImpl implements CorequisitoDtoServicioJdbc {

	@Resource(mappedName = GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/*
	 * ********************************************************************************* 
	 * ************************** METODOS DE CONSULTA ********************************** 
	 * *********************************************************************************
	 */

	/**
	 * Realiza la busqueda de las materias correquisitos de una materia
	 * @return Lista las materias correquisitos de una materia
	 * @param materiaId - id de la materia que se desea tener los correquisitos
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException- Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateria(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException {
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_ESTADO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_COREQUISITO);
			sbSql.append(" crq ");sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" ON ");sbSql.append(" crq. ");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" ON ");sbSql.append(" mtr. ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);sbSql.append(" = ");sbSql.append(" timt.");
			sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" crq.");
			sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");
			sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while (rs.next()) {
				retorno.add(transformarResultSetADtoListarXidMateria(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Correquisito.buscar.por.id.materia.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Correquisito.buscar.por.id.materia.exception")));
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

		if (retorno == null || retorno.size() <= 0) {
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Correquisito.buscar.por.id.materia.no.result.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de las materias correquisitos de una materia
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @return Lista las materias correquisitos de una materia
	 * @param materiaId - id de la materia que se desea tener los correquisitos
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException- Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateriaFull(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException {
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_ESTADO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_COREQUISITO);
			sbSql.append(" crq ");sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" ON ");sbSql.append(" crq. ");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" ON ");sbSql.append(" mtr. ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);sbSql.append(" = ");sbSql.append(" timt.");
			sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" crq.");
			sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");
			sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while (rs.next()) {
				retorno.add(transformarResultSetADtoListarXidMateriaFull(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Correquisito.buscar.por.id.materia.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Correquisito.buscar.por.id.materia.exception")));
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

		if (retorno == null || retorno.size() <= 0) {
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Correquisito.buscar.por.id.materia.no.result.exception")));
		}
		return retorno;
	}

	/*
	 * ********************************************************************************* 
	 * ************************** METODOS DE TRANSFORMACION **************************** 
	 * *********************************************************************************
	 */

	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs- rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException- SQLException Excepción de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoListarXidMateria(ResultSet rs) throws SQLException {
		MateriaDto retorno = new MateriaDto();
		retorno.setCrqId(rs.getInt(JdbcConstantes.CRQ_ID));
		retorno.setCrqMtrId(rs.getInt(JdbcConstantes.CRQ_MTR_ID));
		retorno.setCrqMtrCorequisitoId(rs.getInt(JdbcConstantes.CRQ_MTR_COREQUISITO_ID));
		retorno.setCrqEstado(rs.getInt(JdbcConstantes.CRQ_ESTADO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param rs- rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException- SQLException Excepción de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoListarXidMateriaFull(ResultSet rs) throws SQLException {
		MateriaDto retorno = new MateriaDto();
		retorno.setCrqId(rs.getInt(JdbcConstantes.CRQ_ID));
		retorno.setCrqMtrId(rs.getInt(JdbcConstantes.CRQ_MTR_ID));
		retorno.setCrqMtrCorequisitoId(rs.getInt(JdbcConstantes.CRQ_MTR_COREQUISITO_ID));
		retorno.setCrqEstado(rs.getInt(JdbcConstantes.CRQ_ESTADO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
		return retorno;
	}

}
