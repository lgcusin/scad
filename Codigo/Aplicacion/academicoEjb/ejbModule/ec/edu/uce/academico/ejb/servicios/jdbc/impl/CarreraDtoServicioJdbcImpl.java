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
   
 ARCHIVO:     CarreraDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla carrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-MARZ-2017		Dennis Collaguazo				       Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;

/**
 * EJB CarreraDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla carrera.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class CarreraDtoServicioJdbcImpl implements CarreraDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	
	public List<CarreraDto> buscarCarrerasPorDocenteNivelAcademicoPeriodoAcademico(String identificacion, int nivelAcademico, int periodo) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException,  PeriodoAcademicoNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_TIPO ");
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
		sql.append(" WHERE FCDC.PRS_ID                		= PRS.PRS_ID ");
		sql.append(" AND DTPS.FCDC_ID                 		= FCDC.FCDC_ID ");
		sql.append(" AND CAHR.DTPS_ID                 		= DTPS.DTPS_ID ");
		sql.append(" AND CAHR.MLCRPR_ID               		= MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID             		= MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID                		= MTR.MTR_ID ");
		sql.append(" AND MTR.CRR_ID                   		= CRR.CRR_ID ");
		sql.append(" AND PRL.PRAC_ID                  		= PRAC.PRAC_ID ");
		sql.append(" AND MLCRPR.PRL_ID                		= PRL.PRL_ID ");
		sql.append(" AND CAHR.CRHR_ESTADO       			= " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION       = " + CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND CRR.CRR_TIPO           = ? ");
		sql.append(" AND PRL.PRAC_ID            = ?  ");
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" ORDER BY  CRR.CRR_DESCRIPCION ");


		try {

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, nivelAcademico);
			pstmt.setInt(2, periodo);
			pstmt.setString(3, identificacion);
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarCarrerasPorDocenteNivelAcademicoPeriodoAcademico(rs));				
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
			throw new PeriodoAcademicoNoEncontradoException("No se encontró carreras vinculadas al docente.");
		}	

		return retorno;
	}
	
	
	
	public CarreraDto buscarXId(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		CarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO_EVALUACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_REAJUSTE_MATRICULA);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); 
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetACarreraDto(rs,1);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception",carreraId)));
		}	
		return retorno;
	}
	
	

	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarTodos() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarTodosRediseno() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" LIKE ? ");
//			sbSql.append(" OR ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" IN (184))");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%(R)%"); //cargo el id de la carrera padre
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion, sin no encontrado exception
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarCarrerasTodas() throws CarreraDtoJdbcException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
		//	throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	public List<CarreraDto> buscarCarrerasPorArea(int areaId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DETALLE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_TIPO);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_PROCESO);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_COD_SNIESE);
		sql.append(" FROM ");sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr, ");sql.append(JdbcConstantes.TABLA_CARRERA_AREA);sql.append(" crar ");
		sql.append(" WHERE ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID); sql.append(" = crar.");sql.append(JdbcConstantes.CRAR_CRR_ID);
		sql.append(" AND crar.");sql.append(JdbcConstantes.CRAR_AREA_ID);sql.append(" = ? ");
		sql.append(" ORDER BY ");sql.append(JdbcConstantes.CRR_DESCRIPCION);

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, areaId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}

		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	

		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las áreas por carrera de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarAreasXCrrId(Integer crrId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA_AREA);sbSql.append(" crar ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = crar.");sbSql.append(JdbcConstantes.CRAR_AREA_ID);
			sbSql.append(" AND crar.");sbSql.append(JdbcConstantes.CRAR_CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId); //cargo el id de la carrera padre
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion por la facultad
	 * @return Lista todas las carreras de la aplicacion por la facultad solicitada
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXfacultad(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, facultadId); //cargo el id de la facultad
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.exception")));
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
			pstmt = null;
			con = null;
			rs = null;
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
				sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setInt(1, facultadId); //cargo el id de la facultad
				rs = pstmt.executeQuery();
				retorno = new ArrayList<CarreraDto>();
				while(rs.next()){
					retorno.add(transformarResultSetADto(rs));
				}
			} catch (SQLException e) {
				throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.sql.exception")));
			} catch (Exception e) {
				throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.exception")));
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
				throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.no.result.exception")));
			}
		}
		return retorno;
	}
	
	
	@Override
	public List<CarreraDto> listarXfacultades(List<Dependencia> listaDependencia) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE (");
			for (int i=0;i<listaDependencia.size();i++) {
				
				sbSql.append("crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(listaDependencia.get(i).getDpnId());
				if(i<listaDependencia.size()-1) {
					sbSql.append(" OR ");
				}else {
					sbSql.append(" ) ");
				}
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.exception")));
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
		return retorno;
	}
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFl(int idUsuario, String descRol, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.estado.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.no.result.exception",idUsuario)));
		}	
		return retorno;
	}
	
	
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXDependencia(int idUsuario, int descRol, int estadoRolFlujo, int idDependencia) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
//									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idDependencia); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.estado.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.no.result.exception",idUsuario)));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras a las que el estudiante esta matriculado
	 * @return Lista todas las carreras a las que el estudiante se inscribió
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXRolFlujoCarrera(int usuarioRolId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FECHA_CREACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_RESOLUCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioRolId); //cargo el id del usuario
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoConDependencia(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.bucar.rol.flujo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @return Lista de las carreras por id de usuario y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXPeriodoAcademico(int idUsuario,  int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
										sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = prl.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			                         sbSql.append(" GROUP BY "); //agrupa por carrera, para que no se repita la carrera
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			                       sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(idUsuario);
//			System.out.println(rolId);
			
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoEnCierre(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									
									if(idPeriodoAcademico == GeneralesConstantes.APP_ID_BASE){
										sbSql.append(" AND (prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
										sbSql.append(" OR prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
									}else{
										sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									}
									
//									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(idUsuario);
//			System.out.println(rolId);
			
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			if(idPeriodoAcademico != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoXFacultad(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico, List<DependenciaDto> listDependencia) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
								   sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn on crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
								   sbSql.append(" = dpn.");sbSql.append(JdbcConstantes.DPN_ID);
								   
			sbSql.append(" WHERE ");
//									sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID); sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
//									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ? ");
									
									if(listDependencia.size() > 0){
											sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" in ( ");
											for (int i = 0; i < listDependencia.size(); i++) {
												sbSql.append(" ? ");
												if(i != listDependencia.size() -1) {
										         sbSql.append(","); 
										        }
											}
											sbSql.append(" ) ");
									}
									
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			
			int contador = 4;
			
			if(listDependencia.size() > 0){
				for (DependenciaDto item : listDependencia) {
					contador++;
					pstmt.setInt(contador, item.getDpnId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivo(int idUsuario, String descRol, int estadoRolFlujo, List<RolFlujoCarrera> listCarreras) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
									sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" in ( ");
									for (int i = 0; i < listCarreras.size(); i++) {
										sbSql.append(listCarreras.get(i).getRoflcrCarrera().getCrrId());
										if(i==listCarreras.size()-1){
											sbSql.append(" ) ");
										}else{
											sbSql.append(" , ");
										}
									}
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras a las cuales tiene acceso el Usuario y por la Facultad:
	 * @param usuarioId - id del usuario , id de la facultad
	 * @return Lista de Carreras  por el usuario que accede y la dependencia seleccionada 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXUsuarioXDependencia(int usuarioId, int dependenciaId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno= null;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
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
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
			pstmt.setInt(2, dependenciaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuarioXDependencia(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.no.result.exception")));
		}	
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrr(int idUsuario, int rolId, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			pstmt = null;
			con = null;
			rs = null;
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
									   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
									   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
									   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
									   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
				sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
											sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
											sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
										sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
											sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
										sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
											sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
										sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
										sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
										sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
										sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
										sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
				sbSql.append(" GROUP BY ");
										sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
										sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
										sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
										sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setInt(1, idUsuario); //cargo el id del usuario
				pstmt.setInt(2, rolId); //cargo la descripcion del rol
				pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
				rs = pstmt.executeQuery();
				retorno = new ArrayList<CarreraDto>();
				while(rs.next()){
					retorno.add(transformarResultSetADto(rs));
				}
			} catch (SQLException e) {
				throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
			} catch (Exception e) {
				throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
				throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
			}	
		}
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrXTipo(int idUsuario, int rolId, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrXDependencia(int idUsuario, int rolId, int estadoRolFlujo, int dpnId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, dpnId); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public CarreraDto buscarXIdPosgrado(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		CarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo el id de la carrera
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception",carreraId)));
		}	
		return retorno;
	}
	
	
	@Override
	public List<CarreraDto> buscarXPracIdPosgrado(int pracId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<CarreraDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId); //cargo el id de la carrera
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException("No se encontraron carreras en la cohorte seleccionada.");
		}	
		return retorno;
	}
	
	@Override
	public List<CarreraDto> buscarTodasPosgrado() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<CarreraDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException("No se encontraron programas en la cohorte seleccionada.");
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarTodosPosgrado() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion por la facultad
	 * @return Lista todas las carreras de la aplicacion por la facultad solicitada
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXfacultadPosgrado(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, facultadId); //cargo el id de la facultad
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlPosgrado(int idUsuario, String descRol, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.estado.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.no.result.exception",idUsuario)));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras a las que el estudiante esta matriculado
	 * @return Lista todas las carreras a las que el estudiante se inscribió
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXRolFlujoCarreraPosgrado(int usuarioRolId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FECHA_CREACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_RESOLUCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioRolId); //cargo el id del usuario
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoConDependencia(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.bucar.rol.flujo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @return Lista de las carreras por id de usuario y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXPeriodoAcademicoPosgrado(int idUsuario,  int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
										sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = prl.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			                         sbSql.append(" GROUP BY "); //agrupa por carrera, para que no se repita la carrera
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			                       sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(int idUsuario, String descRol, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoPosgrado(int idUsuario, String descRol, int estadoRolFlujo, List<RolFlujoCarrera> listCarreras) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
									sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" in ( ");
									for (int i = 0; i < listCarreras.size(); i++) {
										sbSql.append(listCarreras.get(i).getRoflcrCarrera().getCrrId());
										if(i==listCarreras.size()-1){
											sbSql.append(" ) ");
										}else{
											sbSql.append(" , ");
										}
									}
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras a las cuales tiene acceso el Usuario y por la Facultad: 12-jul-2016
	 * @param usuarioId - id del usuario , id de la facultad
	 * @return Lista de Carreras  por el usuario que accede y la dependencia seleccionada 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXUsuarioXDependenciaPosgrado(int usuarioId, int dependenciaId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno= null;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
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
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
			pstmt.setInt(2, dependenciaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuarioXDependencia(rs));
			}
		} catch (SQLException e) {
			//TODO: FALTA MENSAJE
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.sql.exception")));
		} catch (Exception e) {
			//TODO: FALTA MENSAJE
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.exception")));
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
			//TODO: FALTA MENSAJE
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.no.result.exception")));
		}	
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrPosgrado(int idUsuario, int rolId, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public CarreraDto buscarXIdNivelacion(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		CarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo el id de la carrera
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception",carreraId)));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarTodosNivelacion() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion por la facultad
	 * @return Lista todas las carreras de la aplicacion por la facultad solicitada
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXfacultadNivelacion(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, facultadId); //cargo el id de la facultad
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlNivelacion(int idUsuario, String descRol, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.estado.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.no.result.exception",idUsuario)));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras a las que el estudiante esta matriculado
	 * @return Lista todas las carreras a las que el estudiante se inscribió
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXRolFlujoCarreraNivelacion(int usuarioRolId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FECHA_CREACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_RESOLUCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioRolId); //cargo el id del usuario
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoConDependencia(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.bucar.rol.flujo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @return Lista de las carreras por id de usuario y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXPeriodoAcademicoNivelacion(int idUsuario,  int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
										sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = prl.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			                         sbSql.append(" GROUP BY "); //agrupa por carrera, para que no se repita la carrera
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			                       sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoNivelacion(int idUsuario, String descRol, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoNivelacion(int idUsuario, String descRol, int estadoRolFlujo, List<RolFlujoCarrera> listCarreras) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
									sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" in ( ");
									for (int i = 0; i < listCarreras.size(); i++) {
										sbSql.append(listCarreras.get(i).getRoflcrCarrera().getCrrId());
										if(i==listCarreras.size()-1){
											sbSql.append(" ) ");
										}else{
											sbSql.append(" , ");
										}
									}
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.lista.carreras.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras a las cuales tiene acceso el Usuario y por la Facultad: 12-jul-2016
	 * @param usuarioId - id del usuario , id de la facultad
	 * @return Lista de Carreras  por el usuario que accede y la dependencia seleccionada 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXUsuarioXDependenciaNivelacion(int usuarioId, int dependenciaId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno= null;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
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
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
			pstmt.setInt(2, dependenciaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuarioXDependencia(rs));
			}
		} catch (SQLException e) {
			//TODO: FALTA MENSAJE
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.sql.exception")));
		} catch (Exception e) {
			//TODO: FALTA MENSAJE
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.exception")));
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
			//TODO: FALTA MENSAJE
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.por.usuario.dependencia.no.result.exception")));
		}	
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrNivelacion(int idUsuario, int rolId, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrNivelacionRediseno(int idUsuario, int rolId, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_NIVELEACION_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las materias por perido_academico, tipo de carrera, dependencia
	 * @param pracId - id del perido_academico
	 * @param tipoCrr - tipo de carrera 0 grado 1 posgrado
	 * @param dpnId -  id de la facultad
	 * @return Lista de materias
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<CarreraDto> listarMateriasXPracIdXtipoCrrXDpnId(Integer pracId,Integer tipoCrr,Integer dpnId,Integer crrId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
								   sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
								   sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
								   sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
								   sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ON ");
								   sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
								   sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_ID);
								   
			sbSql.append(" WHERE ");
									sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");	
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ? ");
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId); //cargo el id del perido_academico
			pstmt.setInt(2, tipoCrr); //cargo el id de tipode carrera
			pstmt.setInt(3, dpnId); //cargo la facultad
			pstmt.setInt(4, crrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarMateriasXPracIdXtipoCrrXDpnId(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las materias por perido_academico, tipo de carrera, dependencia
	 * @param pracId - id del perido_academico
	 * @param tipoCrr - tipo de carrera 0 grado 1 posgrado
	 * @param dpnId -  id de la facultad
	 * @return Lista de materias
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<CarreraDto> listarMateriasCarrera() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
								   sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
								   sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
								   sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
								   sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ON ");
								   sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
								   
								   sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
								   sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_ID);
								   
			sbSql.append(" WHERE ");
									sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
									sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");
									sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" =  ");
									sbSql.append(DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarMateriasXPracIdXtipoCrrXDpnId(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPregrado(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
									sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de un estudiante por su identificacion y su carrera por id
	 * @param carreraId - id del carrera
	 * @param identificacion - id del identificaion de la persona
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public CarreraDto buscarCarreraXIdentificacionXcrrId(String identificacion,int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		CarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); //cargo el id de la carrerapstmt.setInt(1, carreraId); //cargo el id de la carrera
			pstmt.setInt(2, carreraId); //cargo el id de la carrera
			
			
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoCarreraxIdentificacionxcarreraId(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception",carreraId)));
		}	
		return retorno;
	}
	
	
	
	
	/**MQ:
	 * Realiza la busqueda de todas las carreras de la aplicacion por carrera Proceso
	 * @return Lista todas las carreras de la aplicacion por carrera Proceso
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarTodosXCrrProceso(int crrProceso) throws CarreraDtoJdbcException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" = ?");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrProceso);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			//throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
			
		}	
		return retorno;
	}
	
	/**MQ
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico Pregrado
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPrac(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
									
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			pstmt.setInt(4, idPeriodoAcademico); //cargo el id del periodo academico
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
		//	throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**MQ
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y lista de periodos Posgrado
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @param listaPeriodosActivos - lista de periodos activos de posgrado
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y lista de periodos activos
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada

	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXListaPrac(int idUsuario, Integer rolId, int estadoRolFlujo, List<PeriodoAcademico> listaPeriodosActivos) throws CarreraDtoJdbcException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr , ");
								   sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr , ");
								   sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);
										sbSql.append(" = mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);	
									sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
										sbSql.append(" = mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);	
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
									
									
									sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" in ( ");
									for (int i = 0; i < listaPeriodosActivos.size(); i++) {
										sbSql.append(listaPeriodosActivos.get(i).getPracId());
										if(i==listaPeriodosActivos.size()-1){
											sbSql.append(" ) ");
										}else{
											sbSql.append(" , ");
										}
									}
									
									
									
			sbSql.append(" GROUP BY ");
									sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
									sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setInt(2, rolId); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.exception")));
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
		//	throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.usuario.rol.rolFlujo.periodo.Academico.no.result.exception")));
		}	
		return retorno;
	}
	
	
	public List<CarreraDto> buscarCarreraDependencia(int crrTipo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_REAJUSTE_MATRICULA);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" =  ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrTipo);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetACarreraDto(rs,1));
			}
			
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	public List<CarreraDto> buscarCarreras(int usroId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FECHA_CREACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_RESOLUCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usroId); 
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetACarreraDto(rs, 2));
			}
			
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.bucar.rol.flujo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.rol.flujo.carrera.todos.no.result.exception")));
		}	
		
		return retorno;
	}

	/**
	 * Método que busca las carreras de docentes asignados carga horaria
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @return - retorna las carreras de docentes asignados carga horaria
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto>  listarXDocentesHorasClaseXPeriodo(int pracId) throws DetallePuestoDtoJdbcException {
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");

			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY "); 
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			int contador = 1;  
			pstmt.setInt(contador, CarreraConstantes.TIPO_PREGRADO_VALUE);
			pstmt.setInt(++ contador, pracId);

			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentesHorasClase(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
		//			throw new CarreraDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
		//		}	
		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras de docentes asignados carga horaria
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @return - retorna las carreras de docentes asignados carga horaria
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto>  listarXDocentesHorasClaseXPeriodoXDependencia(int pracId, int dpnId) throws DetallePuestoDtoJdbcException {
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");

			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY "); 
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			int contador = 1;  
			pstmt.setInt(contador, CarreraConstantes.TIPO_PREGRADO_VALUE);
			pstmt.setInt(++ contador, pracId);
			pstmt.setInt(++ contador, dpnId);

			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
		//			throw new CarreraDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
		//		}	
		return retorno;
	}
	
	public List<CarreraDto> buscarCarrerasPorTipoFacultad(int facultadId, int carreraTipo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");
		sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_COD_SNIESE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DETALLE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_TIPO);
		sql.append(" FROM ");sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr ");
		sql.append(" WHERE ");sql.append(" crr.");sql.append(JdbcConstantes.DPN_ID);sql.append(" = ? ");
		sql.append(" AND ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_TIPO);sql.append(" = ? ");
		sql.append(" ORDER BY ");sql.append(JdbcConstantes.CRR_DESCRIPCION);

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, facultadId); 
			pstmt.setInt(2, carreraTipo);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarCarrerasPorTipoFacultad(rs));
			}

		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.facultad.no.result.exception")));
		}
		
		return retorno;
	}
	
	
	/**
	 * MQ: 11 jul 2019
	 * Realiza la busqueda de todas las carreras activas de rediseño
	 * @return Lista todas las carreras activas de rediseño
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarCarreraActivasRediseno() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
							
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" INNER JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" WHERE ");		
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE);
						
			sbSql.append(" ORDER BY ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);  sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			con = ds.getConnection();
			
			//System.out.println(sbSql.toString());
			pstmt = con.prepareStatement(sbSql.toString());

			rs = pstmt.executeQuery();

			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarCarreraNotaCorteXPeriodo(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDtoJdbc.lista.carrera.notaCorte.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDtoJdbc.lista.carrera.notaCorte.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDtoJdbc.lista.carrera.notaCorte.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * MQ: 17 jul 2019
	 * Realiza la busqueda de todas las carreras de pregrado
	 * @return Lista todas las carreras activas de rediseño
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarCarrerasPregrado() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
							
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" INNER JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" WHERE ");		
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
									
			sbSql.append(" ORDER BY ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);sbSql.append(", crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION); 
			con = ds.getConnection();
			
			//System.out.println(sbSql.toString());
			pstmt = con.prepareStatement(sbSql.toString());

			rs = pstmt.executeQuery();

			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarCarreraNotaCorteXPeriodo(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDtoJdbc.lista.carrera.notaCorte.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDtoJdbc.lista.carrera.notaCorte.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDtoJdbc.lista.carrera.notaCorte.no.result.exception")));
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
	private CarreraDto transformarResultSetADto(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		return retorno;
	} 

	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CarreraDto transformarResultSetADtoConDependencia(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		return retorno;
	} 

	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CarreraDto transformarResultSetADtoXUsuarioXDependencia(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CarreraDto transformarResultSetADtolistarMateriasXPracIdXtipoCrrXDpnId(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private CarreraDto transformarResultSetADtoCarreraxIdentificacionxcarreraId(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));

		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		
		retorno.setFcinPracId(rs.getInt(JdbcConstantes.FCIN_PRAC_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));

		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		return retorno;
		
	}
	
	private CarreraDto transformarResultSetACarreraDto(ResultSet rs, int atributos) throws SQLException {
		CarreraDto retorno = new CarreraDto();
		
		switch (atributos) {
		case 1:
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
			retorno.setCrrReajusteMatricula(rs.getInt(JdbcConstantes.CRR_REAJUSTE_MATRICULA));
			retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
			retorno.setCrrDpnId(rs.getInt(JdbcConstantes.CRR_DPN_ID));
			try {
				retorno.setCrrTipoEvaluacion(rs.getInt(JdbcConstantes.CRR_TIPO_EVALUACION));	
			} catch (Exception e) {
			}
			break;
		case 2:
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			retorno.setCrrFechaCreacion(rs.getDate(JdbcConstantes.CRR_FECHA_CREACION));
			retorno.setCrrResolucion(rs.getString(JdbcConstantes.CRR_RESOLUCION));
			retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
			retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
			break;
		}
		
		return retorno;		
	}
	
	private CarreraDto transformarResultSetADtoDocentesHorasClase(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsEmailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DETALLE));
		return retorno;
	}
	
	private CarreraDto transformarResultSetAbuscarCarrerasPorTipoFacultad(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		return retorno;
	} 
	
	private CarreraDto rsAbuscarCarrerasPorDocenteNivelAcademicoPeriodoAcademico(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		PersonaDto persona = new PersonaDto();
		
		persona.setPracId(rs.getInt(1));
		persona.setPrsIdentificacion(rs.getString(2));
		persona.setPrsPrimerApellido(rs.getString(3));
		persona.setPrsSegundoApellido(rs.getString(4));
		persona.setPrsNombres(rs.getString(5));
		retorno.setCrrPersonaDto(persona);
		
		retorno.setCrrId(rs.getInt(6));
		retorno.setCrrDescripcion(rs.getString(7));
		retorno.setCrrTipo(rs.getInt(8));
		
		return retorno;
	} 
	
	/**
	 * MQ: 11 jul 2019
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	
	private CarreraDto transformarResultSetADtoListarCarreraNotaCorteXPeriodo(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
	
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));		
	
		return retorno;
	} 
}
