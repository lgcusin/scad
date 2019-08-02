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
   
 ARCHIVO:     PeriodoAcademicoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla periodo academico.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 22-MARZ-2017		Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;

/**
 * EJB PeriodoAcademicoDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla periodo academico.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class PeriodoAcademicoDtoServicioJdbcImpl implements PeriodoAcademicoDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	@Resource(lookup=GeneralesConstantes.APP_DATA_SOURCE_SAU)
	private DataSource dsSau;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un periodo academico por id
	 * @param periodoId - id del periodo academico
	 * @return Periodo academico con el id solicitado 
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public PeriodoAcademicoDto buscarXId(int periodoId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarTodos() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<PeriodoAcademicoDto> listarTodosSAU() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" per_descripcion");
			sbSql.append(" , per_codigo");
			sbSql.append(" , per_estado");
			sbSql.append(" , inicio_clases");
			sbSql.append(" , fin_clases");
			sbSql.append(" , grupo");
			sbSql.append(" FROM periodos");
			sbSql.append(" where per_estado = 0");
			sbSql.append(" ORDER BY per_descripcion");
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoSAU(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<PeriodoAcademicoDto> listarTodosSAUxEscCodigoxEspeCodigo(int esccodigo, int especodigo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" per_descripcion");
			sbSql.append(" , per_codigo");
			sbSql.append(" , per_estado");
			sbSql.append(" , inicio_clases");
			sbSql.append(" , fin_clases");
			sbSql.append(" , grupo");
			sbSql.append(" FROM periodos");
			sbSql.append(" where per_estado = 0");
			sbSql.append(" AND esc_codigo in (");
			sbSql.append(" select escuela_id from ins_carreras ");
			sbSql.append(" where escuela_id=");sbSql.append(esccodigo);
			sbSql.append(" and especialidad_escuela_id=");sbSql.append(especodigo);
			sbSql.append(") ORDER BY per_descripcion");
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoSAU(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion por tipo de periodo
	 * @return Lista todos los periodos academicos de la aplicacion por tipo de periodo
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarTodosXTipo(int tipoPeriodo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			if(tipoPeriodo != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
				sbSql.append(" = ? ");
			}else{
				sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				sbSql.append(" or prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				sbSql.append(" or prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			if(tipoPeriodo != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(1, tipoPeriodo); //estado del periodo academico
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por el estado solicitado
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarXEstado(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado); //estado del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarXEstado(int estadoActivo, int estadoIniciado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" (prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? )");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estadoActivo); //estado del periodo academico
			pstmt.setInt(2, estadoIniciado);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarXEstadoXTipo(int estadoActivo, int estadoIniciado, int tipoPeriodo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" (prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? )");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estadoActivo); //estado del periodo academico
			pstmt.setInt(2, estadoIniciado);
			pstmt.setInt(3, tipoPeriodo);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado de matriculado y que tenga un comprobante de pago con valor diferente a cero
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarPeriodosMatriculadoConComprobantePago(String cedula) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
			sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);
			sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_NUM_COMPROBANTE);
			sbSql.append("  LIKE '014%' ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cedula del postulante
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoConComprobantePago(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado de matriculado y que tenga un comprobante de pago con valor diferente a cero
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarPeriodosMatriculadoConComprobantePagoPosgrado(String cedula) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
			sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);
			sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_TOTAL_PAGO);
			sbSql.append(" > 1000 ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cedula del postulante
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoConComprobantePago(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public PeriodoAcademicoDto buscarXIdPosgrado(int periodoId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	@Override
	public PeriodoAcademicoDto buscarXEstadoPregrado(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado); //cargo el estado del periodo academico
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	@Override
	public PeriodoAcademicoDto buscarXEstadoSuficienciaCulturaFisica(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado); //cargo el estado del periodo academico
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	@Override
	public PeriodoAcademicoDto buscarXEstadoIdiomas(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado); //cargo el estado del periodo academico
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	@Override
	public PeriodoAcademicoDto buscarXEstadoCulturaFisica(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado); //cargo el estado del periodo academico
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarTodosPosgrado() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * @author Daniel
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion asociados a la facultad
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarTodosPosgradoPorDependencia(Integer dpnId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" IN(");sbSql.append(dpnId);sbSql.append(" )");
			
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			sbSql.append("=");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion asociados a la carrera
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarPeriodoPosgradoXCarrera(Integer crrId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" IN(");sbSql.append(crrId);sbSql.append(" )");
			
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			sbSql.append("=");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
		
//		if(retorno == null || retorno.size()<=0){
//			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
//		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarTodosXCarrera(List<Carrera> listaCarreras) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" IN(");
			for (int i = 0; i < listaCarreras.size(); i++) {
				sbSql.append(listaCarreras.get(i).getCrrId());
				if(i==listaCarreras.size()-1){
					sbSql.append(" )");		
				}else{
					sbSql.append(" ,");
				}
			}
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			sbSql.append("=");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por el estado solicitado
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarXEstadoPosgrado(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado); //estado del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el usuario
	 * @return Lista todos los periodos academicos de la aplicacion por el estado solicitado
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<PeriodoAcademicoDto> listarXUsuarioPosgrado(Integer idUsuario) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			
			sbSql.append(" and ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			
			sbSql.append(" and ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			
			sbSql.append(" and ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			
			sbSql.append(" and ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);

			sbSql.append(" and ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			
			sbSql.append(" and ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			
			sbSql.append(" and ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" and ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //id del usuario
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarXEstadoPosgrado(int estadoActivo, int estadoIniciado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" (prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? )");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estadoActivo); //estado del periodo academico
			pstmt.setInt(2, estadoIniciado);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PeriodoAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.listar.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	public List<PeriodoAcademicoDto> buscar(Integer estados[], Integer tipos[]) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		List<PeriodoAcademicoDto> retorno = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in ( ");
					sbSql.append(Arrays.toString(estados).replace("[", "").replace("]", ""));
					sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" in ( ");
					sbSql.append(Arrays.toString(tipos).replace("[", "").replace("]", ""));
					sbSql.append(" ) ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetADto(rs));				
			}

		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
		
		if(retorno == null || retorno.size() <= 0){
			retorno = null;
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public PeriodoAcademicoDto  buscar(int estado, int tipo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, estado);
			pstmt.setInt(2, tipo);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				retorno = transformarResultSetADto(rs);				
			}

		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PeriodoAcademicoDto> buscarPeriodosPorDocenteNivelAcademico(String identificacion, int nivelAcademico) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException,  PeriodoAcademicoNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   PRAC.PRAC_TIPO, ");
		sql.append("   PRAC.PRAC_FECHA_INCIO, ");
		sql.append("   PRAC.PRAC_FECHA_FIN ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   CARGA_HORARIA CAHR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   CARRERA CRR ");
		sql.append(" WHERE FCDC.PRS_ID                = PRS.PRS_ID ");
		sql.append(" AND DTPS.FCDC_ID                 = FCDC.FCDC_ID ");
		sql.append(" AND CAHR.DTPS_ID                 = DTPS.DTPS_ID ");
		sql.append(" AND CAHR.MLCRPR_ID               = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID             = MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID                = MTR.MTR_ID ");
		sql.append(" AND MTR.CRR_ID                   = CRR.CRR_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND CAHR.CRHR_ESTADO       			= " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION       = " + CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND PRS.PRS_IDENTIFICACION 			=  ?  ");
		sql.append(" AND CRR.CRR_TIPO           			=  ? ");
		sql.append(" ORDER BY  PRAC.PRAC_DESCRIPCION DESC ");


		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, nivelAcademico);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarPeriodosPorDocenteNivelAcademico(rs));				
			}

		} catch (SQLException e) {
			throw new PeriodoAcademicoValidacionException("Error tipo sql, comuníquese con el administrador del sistema.");
		} catch (Exception e) {
			throw new PeriodoAcademicoException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new PeriodoAcademicoNoEncontradoException("No se encontró períodos académicos vinculados al docente.");
		}	

		return retorno;
	}

	
	public List<PeriodoAcademicoDto>  buscarPeriodosCargaHoraria() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = " +PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE );
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" <> " +PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE );

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetADto(rs));				
			}

		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	public PeriodoAcademicoDto buscar(int pracId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		PeriodoAcademicoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId); 
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				retorno = transformarResultSetADto(rs);				
			}
			
		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PeriodoAcademicoDto> buscarPeriodos(int usrId, int rolId, int pracEstado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException{
		List<PeriodoAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");sql.append(" prac.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" , prac.");sql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sql.append(" , prac.");sql.append(JdbcConstantes.PRAC_ESTADO);
		sql.append(" , prac.");sql.append(JdbcConstantes.PRAC_FECHA_INCIO);
		sql.append(" , prac.");sql.append(JdbcConstantes.PRAC_FECHA_FIN);
		sql.append(" , prac.");sql.append(JdbcConstantes.PRAC_TIPO);
		sql.append(" FROM USUARIO usr , ");
		sql.append("   USUARIO_ROL usro, ");
		sql.append("   ROL_FLUJO_CARRERA roflcr, ");
		sql.append("   MALLA_CURRICULAR mlcr, ");
		sql.append("   MALLA_PERIODO mlpr, ");
		sql.append("   PERIODO_ACADEMICO prac ");
		sql.append(" WHERE usr.USR_ID     = usro.USR_ID ");
		sql.append(" AND usro.USRO_ID     = roflcr.USRO_ID ");
		sql.append(" AND roflcr.CRR_ID    = mlcr.CRR_ID ");
		sql.append(" AND mlcr.MLCR_ID     = mlpr.MLCR_ID ");
		sql.append(" AND mlpr.PRAC_ID     = prac.PRAC_ID ");
		sql.append(" AND usr.USR_ID       = ? ");
		sql.append(" AND usro.ROL_ID      = ? ");
		sql.append(" AND prac.PRAC_ESTADO = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, usrId);
			pstmt.setInt(2, rolId);
			pstmt.setInt(3, pracEstado);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetADto(rs));				
			}

		} catch (SQLException e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.exception")));
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
		
		if(retorno == null || retorno.size()<= 0){
			throw new PeriodoAcademicoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademicoDtoJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private PeriodoAcademicoDto rsAbuscarPeriodosPorDocenteNivelAcademico(ResultSet rs) throws SQLException{
		PeriodoAcademicoDto retorno = new PeriodoAcademicoDto();
		PersonaDto persona = new PersonaDto();
		
		persona.setPrsId(rs.getInt(1));
		persona.setPrsIdentificacion(rs.getString(2));
		persona.setPrsPrimerApellido(rs.getString(3));
		persona.setPrsSegundoApellido(rs.getString(4));
		persona.setPrsNombres(rs.getString(5));
		retorno.setPracPersonaDto(persona);
		
		retorno.setPracId(rs.getInt(6));
		retorno.setPracDescripcion(rs.getString(7));
		retorno.setPracTipo(rs.getInt(8));
		retorno.setPracFechaIncio(rs.getDate(9));
		retorno.setPracFechaFin(rs.getDate(10));
		return retorno;
	} 
	
	private PeriodoAcademicoDto transformarResultSetADto(ResultSet rs) throws SQLException{
		PeriodoAcademicoDto retorno = new PeriodoAcademicoDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
		retorno.setPracFechaIncio(rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO) == null ? null:rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO));
		retorno.setPracFechaFin(rs.getDate(JdbcConstantes.PRAC_FECHA_FIN) == null ? null:rs.getDate(JdbcConstantes.PRAC_FECHA_FIN));
		retorno.setPracTipo(rs.getInt(JdbcConstantes.PRAC_TIPO));
		return retorno;
	} 
	
	private PeriodoAcademicoDto transformarResultSetADtoSAU(ResultSet rs) throws SQLException{
		PeriodoAcademicoDto retorno = new PeriodoAcademicoDto();
		retorno.setPracId(rs.getInt("per_codigo"));
		retorno.setPracDescripcion(rs.getString("per_descripcion")+" - SAU");
		retorno.setPracEstado(rs.getInt("per_estado"));
		retorno.setPracFechaIncio(rs.getDate("inicio_clases"));
		retorno.setPracFechaFin(rs.getDate("fin_clases"));
		retorno.setPracTipo(rs.getInt("grupo"));
		return retorno;
	} 
	
	private PeriodoAcademicoDto transformarResultSetADtoConComprobantePago(ResultSet rs) throws SQLException{
		java.sql.Date fecha = null;
		PeriodoAcademicoDto retorno = new PeriodoAcademicoDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
		if(fecha != null ){
			retorno.setPracFechaIncio(new Date(fecha.getTime()));
		}
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_FIN);
		if(fecha != null ){
			retorno.setPracFechaFin(new Date(fecha.getTime()));
		}
		retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
		return retorno;
	} 
}
