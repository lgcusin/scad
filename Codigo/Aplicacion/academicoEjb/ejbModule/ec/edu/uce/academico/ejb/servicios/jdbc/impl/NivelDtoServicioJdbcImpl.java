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
   
 ARCHIVO:     NivelDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla nivel.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-MARZ-2017		Dennis Collaguazo				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB NivelDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla nivel.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class NivelDtoServicioJdbcImpl implements NivelDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un nivel por id
	 * @param nivelId - id del nivel
	 * @return Nivel con el id solicitado 
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public NivelDto buscarXId(int nivelId) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		NivelDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, nivelId); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetAlistarNivelXCarrera(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.id.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.id.no.result.exception",nivelId)));
		}	
		
		return retorno;
	}
	

	
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarTodos() throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.NVL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.todos.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que realiza la consulta de niveles por el id de la carrera
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @return - Retorna la lista de niveles que pertenecen al id de la carrera a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarNivelXCarrera(int idCarrera) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			
			sbSql.append(" GROUP BY "); sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, idCarrera); //cargo el id de la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarNivelXCarrera(rs));
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.carrera.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que realiza la consulta de niveles por periodo por carrera y detalle puesto docente
	 * @param idPeriodo - idPeriodo periodo academico a consultar
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarNivelXPeriodoXCarrera(int idPeriodo, int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");
			sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes. CRHR_DTPS_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
			sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, NivelConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado activo del nivel 
			pstmt.setInt(2, idPeriodo); //cargo el id del periodo academico
			pstmt.setInt(3, idCarrera); //cargo el id de la carrera
			pstmt.setInt(4, docente); //cargo el id del detalle puesto - del docente
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.no.result.exception")));
		}	
		return retorno;
	}

	
	/**
	 * Método que realiza la consulta de niveles por periodo por carrera y detalle puesto docente
	 * @param idPeriodo - idPeriodo periodo academico a consultar
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarNivelXPeriodoActivoXCarrera(int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");
			sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes. CRHR_DTPS_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
			sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, NivelConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado activo del nivel 
			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(3, docente); //cargo el id del detalle puesto - del docente
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que realiza la consulta de niveles por periodo por carrera y detalle puesto docente
	 * @param idPeriodo - idPeriodo periodo academico a consultar
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<NivelDto> listarNivelXPeriodoActivoXCarreraNivelacion(int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");
			sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes. CRHR_DTPS_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
			sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, NivelConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado activo del nivel 
			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(3, docente); //cargo el id del detalle puesto - del docente
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	

	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<NivelDto> listarTodosPosgrado() throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl LIMIT 4");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.NVL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.todos.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que realiza la consulta de niveles por el id de la carrera
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @return - Retorna la lista de niveles que pertenecen al id de la carrera a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<NivelDto> listarNivelXCarreraPosgrado(int idCarrera) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
			sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" GROUP BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera); //cargo el id de la carrera
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.carrera.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que realiza la consulta de niveles por periodo por carrera y detalle puesto docente
	 * @param idPeriodo - idPeriodo periodo academico a consultar
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<NivelDto> listarNivelXPeriodoXCarreraPosgrado(int idPeriodo, int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");
			sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes. CRHR_DTPS_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
			sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, NivelConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado activo del nivel 
			pstmt.setInt(2, idPeriodo); //cargo el id del periodo academico
			pstmt.setInt(3, idCarrera); //cargo el id de la carrera
			pstmt.setInt(4, docente); //cargo el id del detalle puesto - del docente
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.sql.exception")));
		} catch (Exception e) {
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.no.result.exception")));
		}	
		return retorno;
	}

	
	/**
	 * Método que realiza la consulta de niveles por periodo por carrera y detalle puesto docente
	 * @param idPeriodo - idPeriodo periodo academico a consultar
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<NivelDto> listarNivelXPeriodoActivoXCarreraPosgrado(int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException{
		List<NivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");
			sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes. CRHR_DTPS_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
			sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ESTADO);
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, NivelConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado activo del nivel 
			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(3, docente); //cargo el id del detalle puesto - del docente
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NivelDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new NivelDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.exception")));
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
			throw new NivelDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NivelDto.buscar.por.periodo.carrera.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private NivelDto transformarResultSetADto(ResultSet rs) throws SQLException {
		NivelDto retorno = new NivelDto();
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlEstado(rs.getInt(JdbcConstantes.NVL_ESTADO));
		return retorno;
	} 
	
	
	private NivelDto transformarResultSetAlistarNivelXCarrera(ResultSet rs) throws SQLException {
		NivelDto retorno = new NivelDto();
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		retorno.setNvlEstado(rs.getInt(JdbcConstantes.NVL_ESTADO));
		return retorno;
	} 
	
	
}
