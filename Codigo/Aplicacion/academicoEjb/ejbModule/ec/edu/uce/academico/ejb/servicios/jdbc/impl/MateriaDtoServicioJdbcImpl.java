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
   
 ARCHIVO:     MateriaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla MateriaDto.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-03-2017		David Arellano				       EmisiÃ³n Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MateriaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla MateriaDto.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class MateriaDtoServicioJdbcImpl implements MateriaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	private @EJB MateriaServicio servMateriaServicio;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	
	public MateriaDto buscarModularPorPeriodo(int modularId, int paraleloId) throws MateriaException, MateriaValidacionException, MateriaNoEncontradoException {
		MateriaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   MLCRMT.MLCRMT_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   MTR.MTR_SUB_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID ");
		sql.append(" FROM MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MALLA_CURRICULAR MLCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE MTR.MTR_ID     = MLCRMT.MTR_ID ");
		sql.append(" AND MLCR.MLCR_ID     = MLCRMT.MLCR_ID ");
		sql.append(" AND CRR.CRR_ID       = MLCR.CRR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append(" AND MLCRPR.PRL_ID    = PRL.PRL_ID ");
		sql.append(" AND PRAC.PRAC_ID     = PRL.PRAC_ID ");
		sql.append(" AND MTR.MTR_ID       = ? ");
		sql.append(" AND PRL.PRL_ID       = ? ");
		sql.append(" ORDER BY CRR.CRR_ID, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   MTR.MTR_CODIGO  ");


		try{

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, modularId);
			pstmt.setInt(2, paraleloId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				retorno = rsAbuscarModularPorParalelo(rs);
			}
		} catch (NonUniqueResultException e) {
			throw new MateriaValidacionException("Se encontró mas de un resultado con los parámetros ingresados.");
		} catch (NoResultException e) {
			throw new MateriaValidacionException("No se encontró resultados con los parámetros ingresados.");
		} catch (Exception e) {
			throw new MateriaException("Error tipo sql al buscar materia principal.");
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
	 * Realiza la busqueda de todas materias de una malla especifica de un nivel
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @param nivelId - id del nivel de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmallaXnivel(int mallaId, int nivelId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
//			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
//			sbSql.append(" not in ( ");sbSql.append(" 4,7 )");
//			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			
//			
//			System.out.println(sbSql);
//			System.out.println(mallaId);
//			System.out.println(nivelId);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, mallaId);
			pstmt.setInt(2, nivelId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXmallaXnivel(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas materias de una malla especifica de un nivel
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @param nivelId - id del nivel de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasMallaHastaNivel(int mallaId, int numeralId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
//			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
//			sbSql.append(" not in ( ");sbSql.append(" 4, 7 ) ");
//			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" <= ? ");
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			
			sbSql.append(" ORDER BY  ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" asc ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, mallaId);
			pstmt.setInt(2, numeralId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas materias de una carrera
	 * @return Lista todas materias de esa carrera
	 * @param crrId - id de la carrera a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmallaXnivelXpreXcoreq(Integer crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , prr.");sbSql.append(JdbcConstantes.PRR_MTR_ID);
			sbSql.append(" , prr.");sbSql.append(JdbcConstantes.PRR_MTR_PREREQUISITO_ID);
			sbSql.append(" , mtrprr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" , mtrcrq.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
				sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
				sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
				sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
				sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt  ON  ");
				sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
				sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl  ON  ");
				sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
				sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PREREQUISITO);sbSql.append(" prr ON  ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" prr.");sbSql.append(JdbcConstantes.PRR_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COREQUISITO);sbSql.append(" crq ON ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtrprr ON ");
				sbSql.append(" prr.");sbSql.append(JdbcConstantes.PRR_MTR_PREREQUISITO_ID);
				sbSql.append(" = ");sbSql.append(" mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtrcrq ON ");
				sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
				sbSql.append(" = ");sbSql.append(" mtrcrq.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			sbSql.append(" = ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" not in ( ");sbSql.append(" 4,7) ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtrcrq.");sbSql.append(JdbcConstantes.MTR_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			
			
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXmallaXnivelXpreXcoreq(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas materias de una carrera
	 * @return Lista todas materias de esa carrera
	 * @param crrId - id de la carrera a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasXpreXcoreq(Integer materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , prr.");sbSql.append(JdbcConstantes.PRR_MTR_ID);
			sbSql.append(" , prr.");sbSql.append(JdbcConstantes.PRR_MTR_PREREQUISITO_ID);
			sbSql.append(" , mtrprr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" , mtrcrq.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
				sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
				sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
				sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
				sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt  ON  ");
				sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
				sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl  ON  ");
				sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
				sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PREREQUISITO);sbSql.append(" prr ON  ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" prr.");sbSql.append(JdbcConstantes.PRR_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COREQUISITO);sbSql.append(" crq ON ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtrprr ON ");
				sbSql.append(" prr.");sbSql.append(JdbcConstantes.PRR_MTR_PREREQUISITO_ID);
				sbSql.append(" = ");sbSql.append(" mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtrcrq ON ");
				sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
				sbSql.append(" = ");sbSql.append(" mtrcrq.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);
			sbSql.append(" = ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtrcrq.");sbSql.append(JdbcConstantes.MTR_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXmallaXnivelXpreXcoreq(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas materias de una carrera
	 * @return Lista todas materias de esa carrera
	 * @param crrId - id de la carrera a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmateriasXlistPrerequisito(Integer crrId,List<RecordEstudianteDto> listaAprobadas) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , prr.");sbSql.append(JdbcConstantes.PRR_MTR_ID);
			sbSql.append(" , prr.");sbSql.append(JdbcConstantes.PRR_MTR_PREREQUISITO_ID);
			sbSql.append(" , mtrprr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" , crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
			sbSql.append(" , mtrcrq.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
				sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
				sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
				sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
				sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
				sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
				sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
				sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PREREQUISITO);sbSql.append(" pre ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" prr.");sbSql.append(JdbcConstantes.PRR_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COREQUISITO);sbSql.append(" crq ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtrprr ");
				sbSql.append(" prr.");sbSql.append(JdbcConstantes.PRR_MTR_PREREQUISITO_ID);
				sbSql.append(" = ");sbSql.append(" mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtrcrq ");
				sbSql.append(" crq.");sbSql.append(JdbcConstantes.CRQ_MTR_COREQUISITO_ID);
				sbSql.append(" = ");sbSql.append(" mtrcrq.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" in ( ");
			for (int i = 0; i < listaAprobadas.size(); i++) {
				sbSql.append(" ? ");
				if(i != listaAprobadas.size() -1) {
			        sbSql.append(","); 
			       }
			}
			sbSql.append(" ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtrprr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtrcrq.");sbSql.append(JdbcConstantes.MTR_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId);
			
			int contador = 1;
			
			for (RecordEstudianteDto item : listaAprobadas) {
				contador++;
				pstmt.setInt(contador, item.getMlcrmtMtrId()); //cargo las facultades ids
			}
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXmallaXnivelXpreXcoreq(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por nivel y por carrera para listar las materias
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXnivelXcarrera(int nivelId, int carreraId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			//sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			//sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, nivelId);
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.nivel.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.nivel.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.nivel.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXperiodoXcarreraXnivelXparaleloXdocente(int periodoId, int carreraId, int nivelId, int paraleloId, int docenteId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, paraleloId);
			pstmt.setInt(5, docenteId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAlterno(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
		}	
		return retorno;
	}

	
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXperiodoActivoXcarreraXnivelXparaleloXdocente(int carreraId, int nivelId, int docenteId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" distinct(");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ) ");
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in( ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, nivelId);
			pstmt.setInt(3, docenteId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXperiodoActivoXcarreraXnivelXparaleloXdocente(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * MÃ©todo que realiza la busqueda para listar las materias de los programas de posgrado
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarMateriasActivasPosgrado(int carreraId, int nivelId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" distinct(");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ) ");
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND (");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);
			sbSql.append(" = ");sbSql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
			sbSql.append(" OR");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);
			sbSql.append(" IS NULL) ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, nivelId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXperiodoActivoXcarreraXnivelXparaleloXdocente(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	public List<MateriaDto> listarXrecordXmatriculaXperiodo (int recordId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT MTR.MTR_ID , ");
		sbSql.append("   MTR.MTR_DESCRIPCION , ");
		sbSql.append("   MTR.TIMT_ID , ");
		sbSql.append("   RCES.RCES_ID , ");
		sbSql.append("   RCES.RCES_ESTADO , ");
		sbSql.append("   MLCRMT.MLCRMT_ID , ");
		sbSql.append("   PRL.PRL_ID , ");
		sbSql.append("   PRL.PRL_DESCRIPCION , ");
		sbSql.append("   PRS.PRS_ID , ");
		sbSql.append("   PRS.PRS_IDENTIFICACION , ");
		sbSql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sbSql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sbSql.append("   PRS.PRS_NOMBRES , ");
		sbSql.append("   PRS.PRS_MAIL_INSTITUCIONAL , ");
		sbSql.append("   FCES.FCES_ID , ");
		sbSql.append("   CRR.CRR_ID , ");
		sbSql.append("   PRAC.PRAC_ID , ");
		sbSql.append("   PRAC.PRAC_DESCRIPCION , ");
		sbSql.append("   PRAC.PRAC_ESTADO , ");
		sbSql.append("   FCMT.FCMT_ID , ");
		sbSql.append("   CAMD.MLCRPR_ID_MODULO ");
		sbSql.append(" FROM CALIFICACION_MODULO CAMD , ");
		sbSql.append("   RECORD_ESTUDIANTE RCES, ");
		sbSql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sbSql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sbSql.append("   MALLA_CURRICULAR MLCR, ");
		sbSql.append("   CARRERA CRR, ");
		sbSql.append("   MATERIA MTR, ");
		sbSql.append("   FICHA_ESTUDIANTE FCES, ");
		sbSql.append("   FICHA_MATRICULA FCMT, ");
		sbSql.append("   PERIODO_ACADEMICO PRAC, ");
		sbSql.append("   PERSONA PRS, ");
		sbSql.append("   PARALELO PRL ");
		sbSql.append(" WHERE CAMD.RCES_ID        = RCES.RCES_ID ");
		sbSql.append(" AND CAMD.MLCRPR_ID_MODULO = MLCRPR.MLCRPR_ID ");
		sbSql.append(" AND MLCRPR.MLCRMT_ID      =MLCRMT.MLCRMT_ID ");
		sbSql.append(" AND MTR.MTR_ID            = MLCRMT.MTR_ID ");
		sbSql.append(" AND FCES.FCES_ID          = RCES.FCES_ID ");
		sbSql.append(" AND FCES.FCES_ID          = FCMT.FCES_ID ");
		sbSql.append(" AND FCMT.FCMT_PRAC_ID     = PRAC.PRAC_ID ");
		sbSql.append(" AND FCES.PRS_ID           = PRS.PRS_ID ");
		sbSql.append(" AND MLCRPR.PRL_ID         = PRL.PRL_ID ");
		sbSql.append(" AND MLCR.MLCR_ID          = MLCRMT.MLCR_ID ");
		sbSql.append(" AND MLCR.CRR_ID           = CRR.CRR_ID ");
		sbSql.append(" AND RCES.RCES_ID          = ? ");
		sbSql.append(" AND FCMT.FCMT_ID     	 = ? ");
		sbSql.append(" AND FCMT.FCMT_PRAC_ID     = ? ");
		sbSql.append(" ORDER BY MTR.MTR_DESCRIPCION ");
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, recordId);
			pstmt.setInt(2, matriculaId);
			pstmt.setInt(3, periodoId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAMateriaModuloDto(rs));
			}
			
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoXcarrera(int personaId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, personaId);
			pstmt.setInt(2, matriculaId);
			pstmt.setInt(3, periodoId);
//			pstmt.setInt(4, carreraId);
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXestudianteXmatriculaXperiodo(rs));
			}
		} catch (SQLException e) {
			
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();	
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}

	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoEnCierreXcarreraXMateriaModular(String personaCedula, Integer rcesId,Integer pracId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append("  rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLMD_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
			
			
			
			
			sbSql.append(" FROM ");
			
			
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION_MODULO);sbSql.append(" clf  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces  ");
			sbSql.append(" WHERE "); 
			// Se puede usar hasta detalle puesto_id
            //sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);sbSql.append(" = ? ");

			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLMD_RCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");
			sbSql.append(rcesId);
			
			
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append( pracId );
//			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");
//			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLMD_NOTA1);sbSql.append(" is not null ");
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLMD_NOTA2);sbSql.append(" is not null ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetAEstudianteJdbcDtoModular(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
	
	@Override
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoEIdXcarreraXMateriaModular(String personaCedula, Integer rcesId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append("  rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLMD_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
			
			
			
			
			sbSql.append(" FROM ");
			
			
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION_MODULO);sbSql.append(" clf  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces  ");
			sbSql.append(" WHERE "); 
			// Se puede usar hasta detalle puesto_id
            //sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);sbSql.append(" = ? ");

			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLMD_RCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");
			sbSql.append(rcesId);
			
			
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" =150 ");
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLMD_NOTA1);sbSql.append(" is not null ");
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLMD_NOTA2);sbSql.append(" is not null ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetAEstudianteJdbcDtoModular(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	
	public List<MateriaDto> listadoEstudiantesRegistrosDuplicadosModulares() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT rces.rces_id,clmd_id, clmd_nota1, clmd_nota2, mlcrpr_id_modulo, rces_estado, clmd_asistencia1, clmd_asistencia2, clmd_nota_final_semestre ");	
			
			sbSql.append(" FROM calificacion_modulo clmd,record_estudiante rces, malla_curricular_paralelo mlcrpr, paralelo prl   ");
			sbSql.append(" WHERE  prl.prac_id =350 ");
			sbSql.append(" AND  clmd.rces_id =  rces.rces_id ");
			sbSql.append(" AND  clmd.mlcrpr_id_modulo =  mlcrpr.mlcrpr_id ");
			sbSql.append(" AND  prl.prl_id =  mlcrpr.prl_id ");
			sbSql.append(" AND  prl.prl_id =  mlcrpr.prl_id ");
			sbSql.append(" AND ROWNUM<400 ");
			sbSql.append(" and rces.rces_id in "); 
			sbSql.append("(select  rces_id from "); 
			sbSql.append("(select rces_id ,count(mlcrpr_id_modulo),mlcrpr_id_modulo ");
			sbSql.append("from calificacion_modulo ,malla_curricular_paralelo mlcrpr,paralelo prl ");
			sbSql.append("where mlcrpr_id_modulo=mlcrpr.mlcrpr_id and mlcrpr.prl_id=prl.prl_id and prl.prac_id=350 ");
			sbSql.append("group by rces_id, mlcrpr_id_modulo ");
			sbSql.append("having count(mlcrpr_id_modulo)>1)) "); 
			
			sbSql.append("  order by rces_id, mlcrpr_id_modulo ");
				
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
				retornoAux.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID_MODULO));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<MateriaDto> listadoEstudiantesRegistrosDuplicados() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT rces.rces_id,clf_id, clf_nota1, clf_nota2, rces.mlcrpr_id, rces.rces_estado, clf_asistencia1, clf_asistencia2, clf_nota_final_semestre ");	
			
			sbSql.append(" FROM calificacion clf,record_estudiante rces, malla_curricular_paralelo mlcrpr, paralelo prl   ");
			sbSql.append(" WHERE   ");
			sbSql.append("   clf.rces_id =  rces.rces_id ");
			sbSql.append(" AND  rces.mlcrpr_id =  mlcrpr.mlcrpr_id ");
			sbSql.append(" AND  prl.prl_id =  mlcrpr.prl_id ");
			sbSql.append(" and rces.rces_id in ( ");
			sbSql.append(" select rces_id from( ");
			sbSql.append(" select count(rces_id), rces_id");
			sbSql.append(" from calificacion "); 
			sbSql.append(" group by rces_id having count(rces_id) >1 )) ");
				
				
			sbSql.append(" order by rces.rces_id , clf.clf_id desc");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
				retornoAux.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
				retornoAux.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
	
	@Override
	public void eliminarDuplicados(int clfId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		Connection con = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" DELETE FROM PROCESO_CALIFICACION WHERE CLF_ID=");	
			
			sbSql.append(clfId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.executeUpdate();
			
			sbSql = new StringBuilder();
			sbSql.append(" DELETE FROM CALIFICACION WHERE CLF_ID=");
			sbSql.append(clfId);
			pstmt2 = con.prepareStatement(sbSql.toString());
			pstmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				}	
		}
		
	}
	
	
	
	@Override
	public List<MateriaDto> listadoCorrecionEstado() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT rces.rces_id,clf_id, clf_nota1, clf_nota2, rces.mlcrpr_id, rces_estado, clf_asistencia1, clf_asistencia2, clf_nota_final_semestre ");	
			
			sbSql.append(" FROM record_estudiante rces,   ");
			sbSql.append(" calificacion clf,   ");
			sbSql.append(" malla_curricular_paralelo mlcrpr,   ");
			sbSql.append(" paralelo prl   ");
			
			sbSql.append(" WHERE  prl.prac_id =350 ");  
			sbSql.append(" and rces.rces_id = clf.rces_id  ");
			sbSql.append(" and mlcrpr.mlcrpr_id = rces.mlcrpr_id  ");
			sbSql.append(" and prl.prl_id = mlcrpr.prl_id  ");
			sbSql.append(" and rces.rces_estado in(0,7)");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
				retornoAux.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
				retornoAux.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	@Override
	public List<MateriaDto> listadoEstudiantesRegistrosEditadosModulares() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select clmd_id,mlcrpr_id_modulo,clmd_nota1, clmd_nota2,clmd_suma_p1_p2, clmd_param_recuperacion1,clmd_supletorio, clmd_param_recuperacion2,clmd_nota_final_semestre");
			sbSql.append(" from calificacion_modulo ");
			sbSql.append(" where clmd_param_recuperacion1 is not null and  clmd_param_recuperacion2 is not null ");
			sbSql.append(" and clmd_nota_final_semestre=clmd_suma_p1_p2 ");
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setClfNota1(rs.getFloat(JdbcConstantes.CLMD_NOTA1));
				retornoAux.setClfNota2(rs.getFloat(JdbcConstantes.CLMD_NOTA2));
				retornoAux.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2));
				retornoAux.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1));
				retornoAux.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2));
				retornoAux.setClfSupletorio(rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO));
				retornoAux.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<MateriaDto> listadoEstudiantesSinNotaFinal() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select clmd_id,clmd_suma_p1_p2, clmd_asistencia_docente1, clmd_asistencia_docente2 ");
			sbSql.append(" from calificacion_modulo ");
			sbSql.append(" where clmd_nota_final_semestre is null and clmd_suma_p1_p2 is not null ");
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2));
				retornoAux.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1));
				retornoAux.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<MateriaDto> listadoEstudiantesSinNota1SinNota2() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select clmd_id");
			
			sbSql.append(" from calificacion_modulo ");
			sbSql.append(" where clmd_nota1 is null or clmd_nota2 is null ");
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<MateriaDto> listadoEstudiantesProblemasRecuperacion() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select clmd_id, clmd_suma_p1_p2");
			
			sbSql.append(" from calificacion_modulo ");
			sbSql.append(" where clmd_suma_p1_p2<27.5 and clmd_param_recuperacion1 is null ");
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<MateriaDto> listadoEstudiantesRecalculoRecuperacion() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select clmd_id, clmd_suma_p1_p2");
			
			sbSql.append(" from calificacion_modulo ");
			sbSql.append(" where clmd_param_recuperacion2 is not null and clmd_param_recuperacion1 is not null "
					+ " and clmd_supletorio is not null and clmd_suma_p1_p2 <27.5 and clmd_suma_p1_p2 >8.8");
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2));
				retornoAux.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1));
				retornoAux.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2));
				retornoAux.setClfSupletorio(rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO));
				
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listadoEstudiantesXmatriculaXperiodoEnCierreXcarreraXMateriaModular() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" select prs.prs_identificacion,mtr.mtr_id,mtr.mtr_descripcion, mtr1.mtr_id as modulo,mtr1.mtr_descripcion as modulodescripcion, clmd.clmd_id from materia mtr, malla_curricular_materia mlcrmt, malla_curricular_paralelo mlcrpr,");
//			sbSql.append("  record_estudiante rces, ficha_estudiante fces, persona prs, paralelo prl, periodo_academico prac, calificacion_modulo clmd, materia mtr1");
//			sbSql.append("  where prs.prs_id=fces.prs_id and fces.fces_id=rces.fces_id and mlcrpr.mlcrpr_Id=clmd.mlcrpr_id_modulo  and clmd.rces_id=rces.rces_id");
//			sbSql.append("  and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mtr.mtr_id=mlcrmt.mtr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id");
//			sbSql.append("  and prac.prac_estado =");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" and mtr1.mtr_id=mtr.mtr_sub_id ");
//			sbSql.append("  and prs.prs_identificacion in ('1727084996','1720621901')");	
//			sbSql.append("  order by prs.prs_identificacion , modulo");
			sbSql.append("  SELECT DISTINCT prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_CALIFICACION_MODULO);sbSql.append(" clf, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
//			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
//			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
//			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
//			
//					
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
//			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
//			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			
			
			sbSql.append(" WHERE ");	
//			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" =  ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" =  350 ");

			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
//			sbSql.append("  and rces.mlcrpr_id=");sbSql.append(22008);
			sbSql.append("  and prs.prs_identificacion in ('0401364922')");	
//			
						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
//				retornoAux.setMtrIdModular(rs.getInt("modulo"));
//				retornoAux.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
//				retornoAux.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
//				retornoAux.setMtrIdModularDescripcion(rs.getString("modulodescripcion"));
//				retornoAux.setClmdId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listadoEstudiantesXmatriculaXperiodoAnteriorXcarreraXMateriaModular() throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" select prs.prs_identificacion,mtr.mtr_id,mtr.mtr_descripcion, mtr1.mtr_id as modulo,mtr1.mtr_descripcion as modulodescripcion, clmd.clmd_id from materia mtr, malla_curricular_materia mlcrmt, malla_curricular_paralelo mlcrpr,");
//			sbSql.append("  record_estudiante rces, ficha_estudiante fces, persona prs, paralelo prl, periodo_academico prac, calificacion_modulo clmd, materia mtr1");
//			sbSql.append("  where prs.prs_id=fces.prs_id and fces.fces_id=rces.fces_id and mlcrpr.mlcrpr_Id=clmd.mlcrpr_id_modulo  and clmd.rces_id=rces.rces_id");
//			sbSql.append("  and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mtr.mtr_id=mlcrmt.mtr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id");
//			sbSql.append("  and prac.prac_estado =");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" and mtr1.mtr_id=mtr.mtr_sub_id ");
//			sbSql.append("  and prs.prs_identificacion in ('1727084996','1720621901')");	
//			sbSql.append("  order by prs.prs_identificacion , modulo");
			sbSql.append("  SELECT DISTINCT prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_CALIFICACION_MODULO);sbSql.append(" clf, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
//			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
//			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
//			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
//			
//					
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
//			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
//			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" =  150");

			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
//			sbSql.append("  and prs.prs_identificacion in ('1310232622')");	
//			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				MateriaDto retornoAux = new MateriaDto();
				retornoAux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
//				retornoAux.setMtrIdModular(rs.getInt("modulo"));
//				retornoAux.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
//				retornoAux.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
//				retornoAux.setMtrIdModularDescripcion(rs.getString("modulodescripcion"));
//				retornoAux.setClmdId(rs.getInt(JdbcConstantes.CLMD_ID));
				retornoAux.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
				retorno.add(retornoAux);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que realiza la busqueda de materias y notas por estudiante y carreras del director
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarMateriasNotasXPracActivoXCarreraXIdentificacion(String cedulaEstudiante, List<CarreraDto> carrerasDirector , int crrId,int mtrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			if(crrId!=GeneralesConstantes.APP_ID_BASE && crrId!=0){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" =  ");sbSql.append(crrId);				
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN (");

				for (int i = 0;i< carrerasDirector.size();i++) {
					sbSql.append(carrerasDirector.get(i).getCrrId());
					if(i!=carrerasDirector.size()-1){
						sbSql.append(" , ");		
					}
				}
				sbSql.append(" ) ");
			}

			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(",prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(",mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, cedulaEstudiante);
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXestudianteXmatriculaXperiodo(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que realiza la busqueda de materias y notas por estudiante y carreras del director
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarMateriasNotasXPracEnCierreXCarreraXIdentificacion(String cedulaEstudiante, List<CarreraDto> carrerasDirector , int crrId , int mtrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			
			if(crrId!=GeneralesConstantes.APP_ID_BASE && crrId!=0){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" =  ");sbSql.append(crrId);				
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN (");

				for (int i = 0;i< carrerasDirector.size();i++) {
					sbSql.append(carrerasDirector.get(i).getCrrId());
					if(i!=carrerasDirector.size()-1){
						sbSql.append(" , ");		
					}
				}
				sbSql.append(" ) ");
			}

			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(",prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(",mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, cedulaEstudiante);
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXestudianteXmatriculaXperiodo(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que realiza la busqueda de materias y notas por estudiante y carreras del director
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarEstudiantesParaRecalificacion(String cedulaEstudiante, List<CarreraDto> carrerasDirector , int crrId , int mtrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");	
			sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" LIKE  '%");sbSql.append(GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaEstudiante));sbSql.append("%'");
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" IN( ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ) ");
			
//			System.out.println(crrId);
			if(crrId!=GeneralesConstantes.APP_ID_BASE && crrId!=0){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" =  ");sbSql.append(crrId);				
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN (");

				for (int i = 0;i< carrerasDirector.size();i++) {
					sbSql.append(carrerasDirector.get(i).getCrrId());
					if(i!=carrerasDirector.size()-1){
						sbSql.append(" , ");		
					}
				}
				sbSql.append(" ) ");
			}
			if(mtrId!=GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" =  ");sbSql.append(mtrId);				
			}else{
				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" >  ");sbSql.append(mtrId);
			}
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" AND (clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);sbSql.append(" IS NOT NULL");
			sbSql.append(" OR clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);sbSql.append(" IS NOT NULL)");

			
			
			
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtoEstudiantes(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarXmallaCurricularParalelo(Integer mlcrprId, Integer prsId, Integer periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clmd.");sbSql.append(JdbcConstantes.CLMD_ID);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA1);	
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA2);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clmd.");sbSql.append(JdbcConstantes.CLMD_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			
			
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces, ");
			sbSql.append(JdbcConstantes.TABLA_CALIFICACION_MODULO);sbSql.append(" clmd, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
			
					
			sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" clmd.");sbSql.append(JdbcConstantes.MLCRPR_ID_MODULO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" =  ? ");
			
			sbSql.append(" and prs.prs_id=fces.prs_id and fces.fces_id=rces.fces_id and rces.rces_id=clmd.rces_id "+
					 " and clmd.mlcrpr_id_modulo= mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id "
					 + "and mlcrmt.mtr_id=mtr.mtr_id and mlcrpr.prl_id=prl.prl_id " 
					 + " and mlcr.mlcr_id=mlcrmt.mlcr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id=mtr.crr_id"
					 + " and prac.prac_id=prl.prac_id and fcmt.fces_id=fces.fces_id and cmpa.fcmt_id=fcmt.fcmt_id ");
			
//			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" =  ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" )  ");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			
//						System.out.println(sbSql);	
//						System.out.println(prsId);
//						System.out.println(mlcrprId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, prsId);
			pstmt.setInt(2, mlcrprId);
			pstmt.setInt(3, periodoId);
//			pstmt.setInt(4, carreraId);
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXestudianteXmatriculaXperiodoModulo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<EstudianteJdbcDto> listarNotasXmallaCurricularParalelo(Integer mlcrprId, Integer prsId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<EstudianteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");	
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clmd.");sbSql.append(JdbcConstantes.CLMD_ID);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA1);	
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA2);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clmd.");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clmd.");sbSql.append(JdbcConstantes.CLMD_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION_MODULO);sbSql.append(" clmd ");
			sbSql.append(" ON ");sbSql.append(" clmd.");sbSql.append(JdbcConstantes.CLMD_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" clmd.");sbSql.append(JdbcConstantes.MLCRPR_ID_MODULO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" =  ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, prsId);
			pstmt.setInt(2, mlcrprId);
//			pstmt.setInt(3, periodoId);
//			pstmt.setInt(4, carreraId);
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<EstudianteJdbcDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetAEstudianteJdbcDto(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**MQ
	 * Método que realiza la busqueda de materias ficha_matricula y por estudiante, para consulta de notas de posgrado-SIN NUMERO MATRICULA
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoXcarreraXPosgrado(int personaId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA

//			sbSql.append(", (SELECT ");			
//			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
//			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
//			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
//			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
//			sbSql.append(" WHERE ");	
//			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
//			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
//			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			//UTILIZO EL PERIODO ACADEMICO DE LA FICHA MATRICULA
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY ");
			sbSql.append("prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);sbSql.append(" , ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
										
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, personaId);
			pstmt.setInt(2, matriculaId);
			pstmt.setInt(3, periodoId);
//			pstmt.setInt(4, carreraId);
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXestudianteXmatriculaXperiodoXcarreraXPosgrado(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoXcarreraFull(int personaId, int matriculaId, int periodoId,int carreraId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" ON ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
//			sbSql.append(" and ");sbSql.append("rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);sbSql.append(" <> ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ORDER BY ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
//System.out.println(sbSql);
//System.out.println(personaId);
//System.out.println(matriculaId);
//System.out.println(periodoId);
//System.out.println(carreraId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, personaId);
			pstmt.setInt(2, matriculaId);
			pstmt.setInt(3, periodoId);
			pstmt.setInt(4, carreraId);
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXestudianteXmatriculaXperiodo(rs));
			}
		} catch (SQLException e) {
			
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarNotasEstudianteNivelacion(String identificacionPrs) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = (");
			sbSql.append(" SELECT PRAC_ID FROM PERIODO_ACADEMICO WHERE PRAC_ESTADO =  ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = (");
			sbSql.append(" SELECT PRAC_ID FROM PERIODO_ACADEMICO WHERE PRAC_ESTADO =  ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");

			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" LIKE ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" <> ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					
			sbSql.append(" ORDER BY ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacionPrs);
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtoListaEstudiantesNivelacionCorregido(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Método que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<EstudianteJdbcDto> listarCalificacionesEstudianteNivelacion(String identificacionPrs) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<EstudianteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
						
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" as crrIdArea");
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" as crrDescripcionArea");
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
//			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
//							
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
////			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
//			
//		
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
//			
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
//			
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
//			
//			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
//            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces, ");
			sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
//			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);sbSql.append(" = rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = 350 ");

			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
//			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
//			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" LIKE ? ");
//			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
//			sbSql.append(" <> ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);sbSql.append(" = rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ORDER BY ");
			sbSql.append("prs.prs_primer_apellido");
			sbSql.append(" , ");sbSql.append("prs.prs_segundo_apellido");
			sbSql.append(" , ");sbSql.append("mtr.mtr_descripcion");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+identificacionPrs+"%");
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<EstudianteJdbcDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtoPromoverNivelacion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas materias.
	 * @return Lista todas materias de de una facultad, carrera .
	 * @param dependenciaId - id de la dependencia de las materias a buscar.
	 * @param carreraId - id de la carrera de las materias a buscar.
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada.
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados.
	 */
	
	public List<MateriaDto> listarMateriasPdpn_IdPcrr_Id(int  dependenciaId , int carreraId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			//MATERIA
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO_SNIESE);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);
			
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_INTEGRADORA_HORAS);	
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_PRE_PROFESIONAL_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_RELACION_TRABAJO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
				
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_UNIDAD_MEDIDA);
			
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_PRACTICAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_PRACTICAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_HORAS_PRACTICAS);
			
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_TRAB_AUTONOMO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_TRAB_AUTONOMO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_HORAS_TRAB_AUTONOMO);
			
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_PRAC_SEMA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_PRAC_SEMA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_HORAS_PRAC_SEMA);
			
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_AUTONO_SEMA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_AUTONO_SEMA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_HORAS_AUTONO_SEMA);
			
			//DEPENDENCIA
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			//CARRERA
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			
			//NUCLEO PROBLEMICO CARRERA
			sbSql.append(" , ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_ID);
			//NUCLEO PROBLEMICO
			sbSql.append(" , ncpr.");sbSql.append(JdbcConstantes.NCPR_ID);
			sbSql.append(" , ncpr.");sbSql.append(JdbcConstantes.NCPR_DESCRIPCION);			
			//TIPO MATERIA
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			//CAMPO FORMACION
			sbSql.append(" , cmfr.");sbSql.append(JdbcConstantes.CMFR_ID);
			sbSql.append(" , cmfr.");sbSql.append(JdbcConstantes.CMFR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" ON ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" ON ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NUCLEO_PROBLEMICO_CARRERA);sbSql.append(" ncprcr ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_NCPRCR_ID);
			sbSql.append(" = ");sbSql.append(" ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NUCLEO_PROBLEMICO);sbSql.append(" ncpr ");
			sbSql.append(" ON ");sbSql.append(" ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_NCPR_ID);
			sbSql.append(" = ");sbSql.append(" ncpr.");sbSql.append(JdbcConstantes.NCPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" = ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CAMPO_FORMACION);sbSql.append(" cmfr ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CMFR_ID);
			sbSql.append(" = ");sbSql.append(" cmfr.");sbSql.append(JdbcConstantes.CMFR_ID);
			sbSql.append(" WHERE ");			
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){// Si  se a seleccionado una Dependencia, se incluye la busqueda por este filtro
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			sbSql.append(" ORDER BY ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
		    pstmt.setInt(1,dependenciaId );	
		    if(carreraId != GeneralesConstantes.APP_ID_BASE){
		    pstmt.setInt(2,carreraId );	
		    }			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXUsuarioXDependenciaXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.dependencia.carrera.sql.exception")));
			
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.dependencia.carrera.exception")));
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
			
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.dependencia.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de las sub materias de una materia padre
	 * @return Lista las sub materias de una materia padre
	 * @param materiaId - id de la materia padre
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateriaPadre(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" ON ");sbSql.append(" mtr. ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" = ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" WHERE ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXidMateriaPadre(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.subUnidades.por.materia.padre.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.subunidades.por.materia.padre.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.subunidades.buscar.por.materia.padre.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por descripcion y por carrera para listar las materias
	 * @param descripcion - descripcion nombre de la materia a buscar
	 * @param idCarrera - idCarrera id de la carrera seleccionada para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXDescripcionXcarrera(String descripcion, int idCarrera) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" on ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" on ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" = ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" on ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" WHERE ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" is null ");
			sbSql.append(" AND ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);sbSql.append(" not in ( ");
			sbSql.append(TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_VALUE);sbSql.append(" )");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" like ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera);
			pstmt.setString(2, "%"+descripcion.toUpperCase()+"%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXdescripcionXcarrera(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.descripcion.id.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.descripcion.id.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.descripcion.id.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por Malla, Nivel, DescripciÃ³n y tipo de busqueda(Prerrequisitos, Correquisitos)
	 * @param mallaCurricularId -idMalla a buscar
	 * @param nivelId - idNivel para la busqueda
	 * @param descripcion - descripcion para la busqueda
	 * * @param tipoBusqueda - tipoBusqueda para la busqueda (Prerrequisitos, Correquisitos)
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasPIdMlcr_PIdNvl_PDescripcion(int mallaCurricularId, int nivelId, String descripcion, int tipoBusqueda) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" on ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" on ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" on ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" on ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" = ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" WHERE ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL); 
			sbSql.append(" <> ");sbSql.append(NivelConstantes.NUMERAL_NIVELACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL); 
			
			if(tipoBusqueda == MateriaConstantes.MATERIA_PRERREQUISITO_VALUE){// Si se busca para agregar como prerrequisito se incluye la busqueda por este filtro
			sbSql.append(" < ? ");
			}
			
			if(tipoBusqueda == MateriaConstantes.MATERIA_CORREQUISITO_VALUE){// Si se busca para agregar como correquisito se incluye la busqueda por este filtro
				sbSql.append(" = ? ");
				}	
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" like ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mallaCurricularId);
			pstmt.setInt(2, nivelId);
			pstmt.setString(3, "%"+descripcion.toUpperCase()+"%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarMateriasPIdMlcr_PIdNvl(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.id.nivel.id.descripcion.tipo.busqueda.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.id.nivel.id.descripcion.tipo.busqueda.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.id.nivel.id.descripcion.tipo.busqueda.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de la materias por Id Materia en mallas curriculares
	 * @return TRUE- Si existe la materia en una malla curricular, FALSE- sino existe
	 * @param materiaId - id de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean  buscarMateriaDtoPMlcrmt(int materiaId) throws MateriaDtoException{
		Boolean retorno = true;
		List<MateriaDto> listMaterias= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			rs = pstmt.executeQuery();
			listMaterias = new ArrayList<MateriaDto>();
			while(rs.next()){
				listMaterias.add(transformarResultSetADtoBuscarMateriaDtoPMlcrmt(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.en.malla.curricular.materia.por.id.materia.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.en.malla.curricular.materia.por.id.materia.exception")));
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
		if(listMaterias == null || listMaterias.size()<=0){
			retorno=false;
			//NO REQUIERE MENSAJE DE EXCEPTION NO ENCONTRADO
			//throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.subunidades.buscar.por.materia.padre.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel para listar las materias
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlId(int pracId, int crrId, int nvlId, int prlId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, nvlId);
			pstmt.setInt(4, prlId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoPeriodoCarreraNivelParalelo(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel para listar las materias con modulares
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlIdModular(int pracId, int crrId, int nvlId, int prlId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			
			//para modulo sin modular
//			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
//			sbSql.append(" not in (");sbSql.append(TipoMateriaConstantes.TIPO_MODULAR_VALUE);sbSql.append(") ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, nvlId);
			pstmt.setInt(4, prlId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoPeriodoCarreraNivelParalelo(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel para listar las materias sin la materia a consultar
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @param mlcrprId - mlcrprId id del de la materia a ser excluida
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlIdSinMateria(int pracId, int crrId, int nvlId, int prlId, MateriaDto materiaDto) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");sbSql.append(" on ");
			
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" not in (");sbSql.append(materiaDto.getMlcrprId());sbSql.append(") ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ? ");
			
			
//			if(materiaDto.getMtrHoras() != 0 && materiaDto.getMtrHoras() != null){
//				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
//				sbSql.append(" = ? ");
//			}
//			if(materiaDto.getMtrCreditos() != 0 && materiaDto.getMtrCreditos() != null){
//				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
//				sbSql.append(" = ? ");
//			}
			
			sbSql.append(" AND ");sbSql.append(" ( mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" = ? ");
			sbSql.append(" OR ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" = ? )");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, nvlId);
			pstmt.setInt(4, prlId);
			
			if(materiaDto.getMtrHoras() != 0 && materiaDto.getMtrHoras() != null){
				pstmt.setInt(5, materiaDto.getMtrHoras());
				pstmt.setInt(6, materiaDto.getMtrHoras());
			}
			if(materiaDto.getMtrCreditos() != 0 && materiaDto.getMtrCreditos() != null){
				pstmt.setInt(5, materiaDto.getMtrCreditos());
				pstmt.setInt(6, materiaDto.getMtrCreditos());
			}
			
//			pstmt.setInt(5, materiaDto.getMtrHoras().intValue());
//			pstmt.setInt(6, materiaDto.getMtrCreditos().intValue());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoPeriodoCarreraNivelParalelo(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel para listar las materias sin la materia a consultar
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @param mlcrprId - mlcrprId id del de la materia a ser excluida
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlIdSinMateriaSinControlNumHorasCreditos(int pracId, int crrId, int nvlId, int prlId, MateriaDto materiaDto) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");sbSql.append(" on ");
			
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" not in (");sbSql.append(materiaDto.getMlcrprId());sbSql.append(") ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ? ");
			
			
//			if(materiaDto.getMtrHoras() != 0 && materiaDto.getMtrHoras() != null){
//				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
//				sbSql.append(" = ? ");
//			}
//			if(materiaDto.getMtrCreditos() != 0 && materiaDto.getMtrCreditos() != null){
//				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
//				sbSql.append(" = ? ");
//			}
			
//			sbSql.append(" AND ");sbSql.append(" ( mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
//			sbSql.append(" = ? ");
//			sbSql.append(" OR ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
//			sbSql.append(" = ? )");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, nvlId);
			pstmt.setInt(4, prlId);
			
//			if(materiaDto.getMtrHoras() != 0 && materiaDto.getMtrHoras() != null){
//				pstmt.setInt(5, materiaDto.getMtrHoras());
//				pstmt.setInt(6, materiaDto.getMtrHoras());
//			}
//			if(materiaDto.getMtrCreditos() != 0 && materiaDto.getMtrCreditos() != null){
//				pstmt.setInt(5, materiaDto.getMtrCreditos());
//				pstmt.setInt(6, materiaDto.getMtrCreditos());
//			}
			
//			pstmt.setInt(5, materiaDto.getMtrHoras().intValue());
//			pstmt.setInt(6, materiaDto.getMtrCreditos().intValue());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoPeriodoCarreraNivelParalelo(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.periodo.carrera.nivel.paralelo.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas materias de una malla especifica 
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmalla(int mallaId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" NOT IN (");sbSql.append(NivelConstantes.NIVEL_APROBACION_VALUE);sbSql.append(" )");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
							
			pstmt.setInt(1, mallaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAMateriaDto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**MQ:  28/03/2019
	 * Realiza la busqueda de todas materias de una malla especifica sin presentar materias hijas(modulos) para homologaciones
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmallaSinAsignaturasModulo(int mallaId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" NOT IN (");sbSql.append(NivelConstantes.NIVEL_APROBACION_VALUE);sbSql.append(" )");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" NOT IN (");sbSql.append(TipoMateriaConstantes.TIPO_MODULO_VALUE);sbSql.append(" )");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
							
			pstmt.setInt(1, mallaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAMateriaDto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracActivoXprcacEnCierre(String personaIdentificacion, int cncrID) throws MateriaDtoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			//Sql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_OBSERVACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			//Aumento JVROSALEs
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			//AumentoJVROSALES
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append("  in ( ");
			
			sbSql.append(" SELECT ");
			sbSql.append(" pracaux.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" pracaux ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in ( ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" )) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append("  not in ( ");
			
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr , ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr  ");
			sbSql.append(" WHERE ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_ID);sbSql.append(" = ? ");
			sbSql.append(" ) ");
			sbSql.append(" order by ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" ASC ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			pstmt.setInt(2, cncrID); //cargo la identificacion de la persona
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtobuscarRecordEstudiante(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<MateriaDto>();
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracActivoXprcac(String personaIdentificacion, int cncrID) throws MateriaDtoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			//Sql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_OBSERVACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			//Aumento JVROSALEs
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			//AumentoJVROSALES
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append("  in ( ");
			
			sbSql.append(" SELECT ");
			sbSql.append(" pracaux.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" pracaux ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in ( ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" )) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append("  not in ( ");
			
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr , ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr  ");
			sbSql.append(" WHERE ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_ID);sbSql.append(" = ? ");
			sbSql.append(" ) ");
			sbSql.append(" order by ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" ASC ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			pstmt.setInt(2, cncrID); //cargo la identificacion de la persona
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtobuscarRecordEstudiante(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<MateriaDto>();
		}	
		
		return retorno;
	}
	
	
	
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracHomologacion(String personaIdentificacion, int cncrID) throws MateriaDtoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" WHERE ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);			
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" = '");sbSql.append(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_LABEL);sbSql.append("' ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" order by ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" ASC ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion);
			pstmt.setInt(2, cncrID); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtobuscarRecordEstudianteHomologacion(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.record.estudiante.por.identificacion.por.perido.homologacion.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.record.estudiante.por.identificacion.por.perido.homologacion.exception")));
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
			retorno = new ArrayList<MateriaDto>();
		}	
		
		return retorno;
	}
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public MateriaDto buscarRecordEstudiante(String personaIdentificacion) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		MateriaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_OBSERVACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADtobuscarRecordEstudianteXprsIdentificacion(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
		
//		if(retorno == null ){
//			throw new MateriaDtoNoEncontradoException("No se encontrÃ³ el record estudiante del estudiante");
//		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas materias de una malla especifica de un nivel
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @param nivelId - id del nivel de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasxCarrera(int crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			//Datos de carrera
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			//Datos de materia
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			//Datos de nivel
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID); sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID); sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID); sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" AND ");
//			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.TIMT_ID); sbSql.append(" not in ( ");sbSql.append(" 4, 7 ) ");
			
			
			//AUMENTE JVROSALES PARA QUE NOS SALGA TODAS LAS MATERIAS
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			
			
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO); sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ? ");
			sbSql.append(" ORDER BY mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaCarrera(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas materias de una malla especifica de un nivel
	 * @author jdalbuja
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @param nivelId - id del nivel de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MateriaDto> listarMateriasxCarreraPracEnCierre(int crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			//Datos de carrera
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			//Datos de materia
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			//Datos de nivel
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID); sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID); sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID); sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" AND ");
//			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.TIMT_ID); sbSql.append(" not in ( ");sbSql.append(" 4, 7 ) ");
			
			
			//AUMENTE JVROSALES PARA QUE NOS SALGA TODAS LAS MATERIAS
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			
			
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO); sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ? ");
			sbSql.append(" ORDER BY mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaCarrera(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.malla.nivel.no.result.exception")));
		}	
		return retorno;
	}

	
	public List<MateriaDto> listarMateriasxCarreraFull(int crrId, Integer[] mtrEstados) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT DISTINCT ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
		sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
		sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_PRAC_SEMA);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_AUTONO_SEMA);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_INTEGRADORA_HORAS);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_PRE_PROFESIONAL_HORAS);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
		sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
		sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
		sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
		
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		
		sbSql.append(" WHERE ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCR_ID);
		sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID); sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID); sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID); sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO); sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO); sbSql.append(" = ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); sbSql.append(" not in (14)");
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO); sbSql.append(" in ");sbSql.append(Arrays.toString(mtrEstados).replace("[", "(").replace("]", ")"));
		sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ? ");
	
		try {
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaCarreraFull(rs));
			}
			
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.carrera.full.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.carrera.full.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.carrera.full.no.result.exception")));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionprcacEnCierre(String personaIdentificacion) throws MateriaDtoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append(" SELECT DISTINCT ");
		sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
		sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
		sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
		sbSql.append(" ,CASE ");
		sbSql.append("     WHEN rces.rces_estado =0 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =-1 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =1 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =2 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =3 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =4 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_MIGRADO_SAU_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =5 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_CONVALIDADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =6 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =7 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =8 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =9 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =10 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =11 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_SOLICITADO_LABEL);sbSql.append("'");
		sbSql.append("     WHEN rces.rces_estado =12 ");
		sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_LABEL);sbSql.append("'");
		sbSql.append("   END AS ESTADO ");
		sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
		sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
		sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
		sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
		sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);

		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
		//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
		//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");

		sbSql.append(" WHERE ");
		sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.PRS_ID);
		sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.FCES_ID);
		sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
		//			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
		//			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
		//			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		//			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCES_ID);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
		sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");

		sbSql.append(" order by ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" ASC ");

		
		try {
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtobuscarRecordEstudiantexIdentificacion(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<MateriaDto>();
		}	

		return retorno;
	}
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXcarrera(String personaIdentificacion, Integer crrId) throws MateriaDtoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" ,CASE ");
			sbSql.append("     WHEN rces.rces_estado =0 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =-1 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =1 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =2 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =3 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =4 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_MIGRADO_SAU_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =5 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_CONVALIDADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =6 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =7 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =8 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =9 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =10 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =11 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_SOLICITADO_LABEL);sbSql.append("'");
			sbSql.append("     WHEN rces.rces_estado =12 ");
			sbSql.append("     THEN '");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_LABEL);sbSql.append("'");
			sbSql.append("   END AS ESTADO ");
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
//			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
//			sbSql.append(" AND ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" order by ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" ASC ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			pstmt.setInt(2, crrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtobuscarRecordEstudiantexIdentificacion(rs));
			}
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<MateriaDto>();
		}	
		
		return retorno;
	}
	
	/**MQ
	 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado y carrera 
	 * @param prsId - prsId del estudiante a buscar
	 * @param dtmtNumMatriculaMateria - dtmtNumMatriculaMateria del estudiante a buscar
	 * @param rcesEstado - rcesEstado del estudiante a buscar
	 * @param crrId - fcesId del estudiante a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */

	   
	
		public List<MateriaDto> ListarMateriasXPrsIdXNumeroMatriculaXEstadoMateriaXCarrera(Integer prsId, Integer dtmtNumMatriculaMateria, int rcesEstado, int crrId) throws MateriaDtoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);			
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
		
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		//	sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI);sbSql.append(" = ");sbSql.append(RecordEstudianteConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_VALUE);
			
//			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" in ");
//			                      sbSql.append(" (SELECT materia FROM ");
//			                      sbSql.append(" (SELECT mtr2.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" as materia");sbSql.append(" , ");
//			                      sbSql.append(" COUNT ");sbSql.append(" (mtr2. ");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" ) ");sbSql.append(" as countmatricula");
//			                      sbSql.append(" FROM ");
//			        			  sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs2 ");
//			        			  sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces2 ");
//			        			  sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces2 ");
//			        			  sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr2 ");
//			        			  sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt2 ");
//			        			  sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr2 ");
//			        			  sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr2 ");
//			        			  sbSql.append(" WHERE ");
//			        			  sbSql.append(" prs2.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces2.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" rces2.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces2.");sbSql.append(JdbcConstantes.FCES_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" rces2.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr2.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" mlcrpr2.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt2.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" mlcrmt2.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr2.");sbSql.append(JdbcConstantes.MTR_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" mtr2.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr2.");sbSql.append(JdbcConstantes.CRR_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" rces2.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
//			        			  sbSql.append(" AND ");sbSql.append(" prs2.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			        			  sbSql.append(" AND ");sbSql.append(" crr2.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			        			  sbSql.append(" GROUP BY ");sbSql.append(" mtr2.");sbSql.append(JdbcConstantes.MTR_ID);
//			        			  sbSql.append(" ORDER BY ");sbSql.append(" mtr2.");sbSql.append(JdbcConstantes.MTR_ID);
//			        			  sbSql.append(" ) ");
//			        			  sbSql.append(" WHERE ");
//			        			  sbSql.append(" countmatricula ");sbSql.append(" = 2 "); //2 es que tengan solo hasta segunda matricula
//			        			  sbSql.append(" ) ");
			        			 			
			sbSql.append(" order by ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" ASC ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, prsId);
			pstmt.setInt(2, dtmtNumMatriculaMateria);
			pstmt.setInt(3, rcesEstado);
			pstmt.setInt(4, crrId);
			
//			System.out.println(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarMateriasXFcesIdXNumeroMatriculaXEstadoMateriaXCarrera(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.exception")));
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
			retorno = new ArrayList<MateriaDto>();
		}	
		
		return retorno;
	}
	
		/**MQ
		 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado, carrera  fichaInscripcion activa  en el periodo HOMOLOGACION
		 * @param prsId - prsId del estudiante a buscar
		 * @param dtmtNumMatriculaMateria - dtmtNumMatriculaMateria del estudiante a buscar
		 * @param rcesEstado - fcesId del estudiante a buscar
		 * @param crrId - crrId del estudiante a buscar
		 * @return Lista de MateriaDto que corresponde a la persona buscada
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 */
			public List<MateriaDto> ListarMateriasXPrsIdXNumeroMatriculaXEstadoMateriaXCarreraxPeriodoHomologacion(Integer prsId, Integer dtmtNumMatriculaMateria, Integer rcesEstado, Integer crrId) throws MateriaDtoException{
			List<MateriaDto> retorno = null;
			PreparedStatement pstmt = null;
			Connection con = null;
			ResultSet rs = null;
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT DISTINCT ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);			
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
				sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
				sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); 
				sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_ESTADO);
				sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
				sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
				sbSql.append(" FROM ");
				sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
				
				sbSql.append(" WHERE ");
				
				sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
				sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
				sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
							        			 			
				sbSql.append(" order by ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" ASC ");
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setInt(1, prsId);
				pstmt.setInt(2, dtmtNumMatriculaMateria);
				pstmt.setInt(3, rcesEstado);
				pstmt.setInt(4, crrId);
				
				
				
				rs = pstmt.executeQuery();
				retorno = new ArrayList<MateriaDto>();
				while(rs.next()){
					retorno.add(transformarResultSetADtoListarMateriasXFcesIdXNumeroMatriculaXEstadoMateriaXCarrera(rs));
				}
			} catch (SQLException e) {
				throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.sql.exception")));
			} catch (Exception e) {
				throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.exception")));
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
				retorno = new ArrayList<MateriaDto>();
			}	
			
			return retorno;
		}
		
//		/**
//		 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un persona, carrera, estado de la solicitud y periodo 
//		 * @param prsId - id de la persona
//		 * @param crrId - id de la carrera
//		 * @param estadoSolicitud- busca por el estado de la solicitud
//		 * @param pracId - id del periodo
//		 * @return Lista de las materias solicitadas tercera matricula de un persona, carrera, estado de la solicitud y periodo
//		 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
//		 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
//		 */
//		public List<MateriaDto> listarXprsIdXCarreraXEstadoXpracId(int prsId, int crrId , int estadoSolicitud, int pracId) throws MateriaDtoException{
//			List<MateriaDto> retorno= null;
//			PreparedStatement pstmt = null;
//			Connection con = null;
//			ResultSet rs = null;
//			try {
//				StringBuilder sbSql = new StringBuilder();
//				sbSql.append(" SELECT DISTINCT");
//				sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//				sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
//				sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
//				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
//				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//				sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
//				sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
//				sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
//				sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
//				sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
//				sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI);
//				sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
//				sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
//				sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
//				sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
//				
//				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
//				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//				 
//				sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//				sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
//				sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
//				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
//				sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
//				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
//				sbSql.append(" = ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_RCES_ID);
//				sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
//				sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//				sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
//				sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
//				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
//				sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);			
//				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
//				sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
//				sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);	
//				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
//				sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//	     		
//				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//				sbSql.append(" = ? ");
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//				sbSql.append(" = ? ");
//				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI);
//				sbSql.append(" =  ?");
//				if(pracId != GeneralesConstantes.APP_ID_BASE){ 
//					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//					sbSql.append(" = ? ");
//					}
//	    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
//				 
//				con = ds.getConnection();
//				pstmt = con.prepareStatement(sbSql.toString());
//				pstmt.setInt(1, prsId);
//				pstmt.setInt(2, crrId); //cargo la carrera
//				pstmt.setInt(3, estadoSolicitud);
//				int contador=3;
//				if(pracId != GeneralesConstantes.APP_ID_BASE){ 
//				pstmt.setInt(++contador, pracId); //cargo el periodo academico
//				}
//				
//				
//				rs = pstmt.executeQuery();
//				retorno = new ArrayList<MateriaDto>();
//				while(rs.next()){
//					retorno.add(transformarResultSetADtoListarXprsIdXCarreraXEstadoXpracId(rs));
//				}
//			} catch (SQLException e) {
//				throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
//			} catch (Exception e) {
//				throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
//			} finally {
//				try {
//					if (rs != null) {
//						rs.close();
//					}
//					if (pstmt != null) {
//						pstmt.close();
//					}
//					if (con != null) {
//						con.close();
//					}
//				} catch (SQLException e) {
//					}	
//			}
//			
//			if(retorno == null || retorno.size()<=0){
//				retorno = new ArrayList<MateriaDto>();
//				
//			}	
//		
//			
//			return  retorno;
//		}	
//		
		
		/**
		 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado y carrera: Apelacion tercera matricula
		 * @param prsId - fcesId del estudiante a buscar
		 * @param crrId - fcesId del estudiante a buscar
		 * @param rcesEstado - fcesId del estudiante a buscar
		 * @return Lista de MateriaDto que corresponde a la persona buscada
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 */
			public List<MateriaDto> ListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitud(Integer prsId, Integer crrId, int sltrmtEstadoSolicitud) throws MateriaDtoException{
			List<MateriaDto> retorno = null;
			PreparedStatement pstmt = null;
			Connection con = null;
			ResultSet rs = null;
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT DISTINCT ");
				sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
				sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
				sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
				sbSql.append(" ,sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
				sbSql.append(" ,sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			
				sbSql.append(" FROM ");
				sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
				
				sbSql.append(" WHERE ");
				sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
				sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
				sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_RCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
				//sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);sbSql.append(" = ? ");
				sbSql.append(" order by ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" ASC ");
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setInt(1, prsId);
				pstmt.setInt(2, crrId);
				pstmt.setInt(3, sltrmtEstadoSolicitud);
				rs = pstmt.executeQuery();
				retorno = new ArrayList<MateriaDto>();
				while(rs.next()){
					retorno.add(transformarResultSetADtoListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitud(rs));
				}
			} catch (SQLException e) {
				throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.sql.exception")));
			} catch (Exception e) {
				throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.exception")));
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
				retorno = new ArrayList<MateriaDto>();
			}	
			
			return retorno;
		}
		
			
			/**SIIUSAU
			 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado y carrera: Apelacion tercera matricula
			 * @param prsId - fcesId del estudiante a buscar
			 * @param crrId - fcesId del estudiante a buscar
			 * @param rcesEstado - fcesId del estudiante a buscar
			 * @return Lista de MateriaDto que corresponde a la persona buscada
			 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
			 */

			public List<MateriaDto> ListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitudSIIUSAU(Integer prsId, Integer crrId, int sltrmtEstadoSolicitud) throws MateriaDtoException{
				List<MateriaDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT ");
					sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" ,sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
					sbSql.append(" ,sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
					sbSql.append(" FROM ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
					sbSql.append(" WHERE ");
					
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
					sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);sbSql.append(" = ");sbSql.append(" Fces.");sbSql.append(JdbcConstantes.FCES_ID);
					sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO_REGISTRO);
					sbSql.append(" = ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);sbSql.append(" = ? ");
					sbSql.append(" order by ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);sbSql.append(" ASC ");
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					pstmt.setInt(1, prsId);
					pstmt.setInt(2, crrId);
					pstmt.setInt(3, sltrmtEstadoSolicitud);
					rs = pstmt.executeQuery();
					retorno = new ArrayList<MateriaDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitudSIIUSAU(rs));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.sql.exception")));
				} catch (Exception e) {
					e.printStackTrace();
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.listar.materias.por.persona.numero.matricula.estado.materia.carrera.exception")));
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
					retorno = new ArrayList<MateriaDto>();
				}	
				
				return retorno;
			}
			/**
			 * Realiza la busqueda del record academico por la identificacion 
			 * @author Arturo Villafuerte - ajvillafuerte
			 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
			 * @return Lista de MateriaDto que corresponde a la persona buscada
			 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
			 */
			public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracActivoXprcacFull(String personaIdentificacion, int cncrID, int periodoInicioId) throws MateriaDtoException{
				List<MateriaDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT distinct");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); 
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					
					sbSql.append(" from PERSONA PRS ");
					sbSql.append(" , USUARIO USR ");
					sbSql.append(" , FICHA_ESTUDIANTE FCES ");
					sbSql.append(" , RECORD_ESTUDIANTE RCES ");
					sbSql.append(" , MALLA_CURRICULAR_PARALELO MLCRPR ");
					sbSql.append(" , FICHA_MATRICULA fcmt ");
					sbSql.append(" , COMPROBANTE_PAGO CMPG ");
					sbSql.append(" , DETALLE_MATRICULA DTMT ");
					sbSql.append(" , FICHA_INSCRIPCION FCIN ");
					sbSql.append(" , CONFIGURACION_CARRERA CNCR ");
					sbSql.append(" , CARRERA CRR ");
					sbSql.append(" , DEPENDENCIA DPN ");
					sbSql.append(" ,MALLA_CURRICULAR_MATERIA MLCRMT ");
					sbSql.append(" , MATERIA MTR ");
					sbSql.append(" ,GRATUIDAD GRT ");
					sbSql.append(" , NIVEL NVL ");
					sbSql.append(" WHERE fCES.FCES_ID = fcmt.FCES_ID "); 
					sbSql.append(" AND fcmt.FCMT_ID = CMPG.FCMT_ID  ");
					sbSql.append(" AND CMPG.CMPA_ID = DTMT.CMPA_ID  ");
					sbSql.append(" AND PRS.PRS_ID = FCES.PRS_ID  ");
					sbSql.append(" AND USR.PRS_ID = PRS.PRS_ID  ");
					sbSql.append(" AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID "); 
					sbSql.append(" AND MLCRPR.MLCRPR_ID = DTMT.MLCRPR_ID  ");
					sbSql.append(" AND fCES.FCES_ID = RCES.FCES_ID  ");
					sbSql.append(" AND fcin.FCIN_ID = FCES.FCIN_ID  ");
					sbSql.append(" AND fcin.CNCR_ID = CNCR.CNCR_ID  ");
					sbSql.append(" AND CRR.CRR_ID = CNCR.CRR_ID  ");
					sbSql.append(" AND CRR.DPN_ID = DPN.DPN_ID  ");
					sbSql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID "); 
					sbSql.append(" AND NVL.NVL_ID = MLCRMT.NVL_ID ");
					sbSql.append(" AND MTR.MTR_ID = MLCRMT.MTR_ID  ");
					sbSql.append(" AND GRT.FCES_ID = FCES.FCES_ID "); 
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" not in (11) "); 
					
//					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
//					sbSql.append("  not in ( ");
//					sbSql.append(" SELECT ");
//					sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
//					sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
//					sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr , ");
//					sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr  ");
//					sbSql.append(" WHERE ");
//					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
//					sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
//					sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_ID);sbSql.append(" = ? ");
//					sbSql.append(" ) ");
					
					if(periodoInicioId != GeneralesConstantes.APP_ID_BASE){ 
						sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
						if(periodoInicioId != PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE){ 
							sbSql.append(" >=  ");sbSql.append(" ? ");
						}else{
							sbSql.append(" =  ");sbSql.append(" ? ");
						}
					}
					
					sbSql.append(" order by ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" ASC ");
					
//					System.out.println("SIIU - "+sbSql.toString()+" - "+personaIdentificacion+" - "+cncrID+" - "+periodoInicioId);
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());

					pstmt.setString(1, personaIdentificacion);
//					pstmt.setInt(2, cncrID);
					
					if(periodoInicioId != GeneralesConstantes.APP_ID_BASE){
						pstmt.setInt(2, periodoInicioId);
					}
					rs = pstmt.executeQuery();
					retorno = new ArrayList<MateriaDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtobuscarRecordEstudiante(rs));
					}
				} catch (SQLException e) {
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
				} catch (Exception e) {
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
					retorno = new ArrayList<MateriaDto>();
				}	
				
				return retorno;
			}
			
			
			/**
			 * Realiza la busqueda del record academico por la identificacion 
			 * @author Arturo Villafuerte - ajvillafuerte
			 * @param prsIdentificacion - String de la indentificaciÃ³n de la persona a buscar
			 * @return Lista de MateriaDto que corresponde a la persona buscada
			 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
			 */
			public List<MateriaDto> buscarRecordEstudianteAModificarXidentificacionXpracActivoXprcacFull(String prsIdentificacion, int periodoId, int carreaId) throws MateriaDtoException{
				List<MateriaDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); 
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_HORAS_CIEN);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					sbSql.append(" ,dtmt.");sbSql.append("DTMT_VALOR_POR_MATERIA");
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
					
					sbSql.append(" from PERSONA PRS ");
					sbSql.append(" , USUARIO USR ");
					sbSql.append(" , FICHA_ESTUDIANTE FCES ");
					sbSql.append(" , RECORD_ESTUDIANTE RCES ");
					sbSql.append(" , MALLA_CURRICULAR_PARALELO MLCRPR ");
					sbSql.append(" , FICHA_MATRICULA fcmt ");
					sbSql.append(" , COMPROBANTE_PAGO CMPG ");
					sbSql.append(" , DETALLE_MATRICULA DTMT ");
					sbSql.append(" , FICHA_INSCRIPCION fcin ");
					sbSql.append(" , CONFIGURACION_CARRERA CNCR ");
					sbSql.append(" , CARRERA CRR ");
					sbSql.append(" , DEPENDENCIA DPN ");
					sbSql.append(" , MALLA_CURRICULAR_MATERIA MLCRMT ");
					sbSql.append(" , MATERIA MTR ");
					sbSql.append(" , NIVEL NVL ");
					sbSql.append(" , PARALELO PRL ");
					sbSql.append(" WHERE fCES.FCES_ID = fcmt.FCES_ID"); 
					sbSql.append(" AND fcmt.FCMT_ID = CMPG.FCMT_ID ");
					sbSql.append(" AND CMPG.CMPA_ID = DTMT.CMPA_ID ");
					sbSql.append(" AND PRS.PRS_ID = FCES.PRS_ID ");
					sbSql.append(" AND USR.PRS_ID = PRS.PRS_ID ");
					sbSql.append(" AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID "); 
					sbSql.append(" AND MLCRPR.MLCRPR_ID = DTMT.MLCRPR_ID ");
					sbSql.append(" AND MLCRPR.PRL_ID = PRL.PRL_ID "); 
					sbSql.append(" AND MLCRMT.NVL_ID = NVL.NVL_ID ");
					sbSql.append(" AND fCES.FCES_ID = RCES.FCES_ID ");
					sbSql.append(" AND fcin.FCIN_ID = FCES.FCIN_ID ");
					sbSql.append(" AND fcin.CNCR_ID = CNCR.CNCR_ID ");
					sbSql.append(" AND CRR.CRR_ID = CNCR.CRR_ID ");
					sbSql.append(" AND CRR.DPN_ID = DPN.DPN_ID ");
					sbSql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID "); 
					sbSql.append(" AND MTR.MTR_ID = MLCRMT.MTR_ID "); 
					
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? "); 
					sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" =  ? "); 
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
	
					sbSql.append(" order by ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" ASC ");
					
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());

					pstmt.setString(1, prsIdentificacion);
					pstmt.setInt(2, periodoId);
					pstmt.setInt(3, carreaId);
					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<MateriaDto>();
					
					
					while(rs.next()){
						retorno.add(transformarResultSetADtobuscarRecordEstudianteFull(rs));
					}
				} catch (SQLException e) {
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
				} catch (Exception e) {
					throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
					retorno = new ArrayList<MateriaDto>();
				}	
				
				return retorno;
			}
			
	public List<MateriaDto> buscarRecordEstudiante(String prsIdentificacion, List<String> rcesEstado, int pracTipo, int pracEstado)throws MateriaDtoException, MateriaDtoNoEncontradoException {
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
				sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
				sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
				sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
				sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
				sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" FCMT.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" AND ");sbSql.append(" FCMT.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" RCES.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" IN (  ");sbSql.append( rcesEstado.toString().replace("[", "").replace("]", ""));sbSql.append("  ) ");
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ( ");
				sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);
					sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ? ");
				sbSql.append(" ) ");
				sbSql.append(" order by ");sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" ASC ");			
				

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, prsIdentificacion.toUpperCase());
			pstmt.setInt(2, pracEstado);
			pstmt.setInt(3, pracTipo);
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarRecordEstudiante(rs));
			}
			
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = null;
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.nivel.carrera.no.result.exception")));
		}	
		
		return retorno;
	}		

	
	
	public List<MateriaDto> buscarRecordEstudiante(String identificacion, int crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct");
				sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO); 
				sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
				sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
				sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" from PERSONA PRS ");
				sbSql.append(" , USUARIO USR ");
				sbSql.append(" , FICHA_ESTUDIANTE FCES ");
				sbSql.append(" , RECORD_ESTUDIANTE RCES ");
				sbSql.append(" , MALLA_CURRICULAR_PARALELO MLCRPR ");
				sbSql.append(" , FICHA_MATRICULA fcmt ");
				sbSql.append(" , COMPROBANTE_PAGO CMPG ");
				sbSql.append(" , DETALLE_MATRICULA DTMT ");
				sbSql.append(" , FICHA_INSCRIPCION FCIN ");
				sbSql.append(" , CONFIGURACION_CARRERA CNCR ");
				sbSql.append(" , CARRERA CRR ");
				sbSql.append(" , DEPENDENCIA DPN ");
				sbSql.append(" , MALLA_CURRICULAR_MATERIA MLCRMT ");
				sbSql.append(" , MATERIA MTR ");
				sbSql.append(" , GRATUIDAD GRT ");
				sbSql.append(" , NIVEL NVL ");
			sbSql.append(" WHERE fCES.FCES_ID = fcmt.FCES_ID "); 
				sbSql.append(" AND fcmt.FCMT_ID = CMPG.FCMT_ID  ");
				sbSql.append(" AND CMPG.CMPA_ID = DTMT.CMPA_ID  ");
				sbSql.append(" AND PRS.PRS_ID = FCES.PRS_ID  ");
				sbSql.append(" AND USR.PRS_ID = PRS.PRS_ID  ");
				sbSql.append(" AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID "); 
				sbSql.append(" AND MLCRPR.MLCRPR_ID = DTMT.MLCRPR_ID  ");
				sbSql.append(" AND fCES.FCES_ID = RCES.FCES_ID  ");
				sbSql.append(" AND fcin.FCIN_ID = FCES.FCIN_ID  ");
				sbSql.append(" AND fcin.CNCR_ID = CNCR.CNCR_ID  ");
				sbSql.append(" AND CRR.CRR_ID = CNCR.CRR_ID  ");
				sbSql.append(" AND CRR.DPN_ID = DPN.DPN_ID  ");
				sbSql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID "); 
				sbSql.append(" AND NVL.NVL_ID = MLCRMT.NVL_ID ");
				sbSql.append(" AND MTR.MTR_ID = MLCRMT.MTR_ID  ");
				sbSql.append(" AND GRT.FCES_ID = FCES.FCES_ID "); 
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" order by ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" ASC ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion.toUpperCase());
			pstmt.setInt(2, crrId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtobuscarRecordEstudiante(rs));
			}
			
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = null;
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.nivel.carrera.no.result.exception")));
		}	
		
		return retorno;
	}	
	
	
	public List<MateriaDto> buscarEstudianteMatriculadoXPeriodoXDependencia(int idPeriodo, int idDependencia, int tipoCarrera, int idCarrera) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct");
				sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
				sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
				sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
				sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
				sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" from "
					+ " persona prs"
					+ " , ficha_estudiante fces"
					+ " , ficha_matricula fcmt"
					+ " , record_estudiante rces"
					+ " , malla_curricular_paralelo mlcrpr"
					+ " , paralelo prl"
					+ " , carrera crr"
					+ " , dependencia dpn "
					+ " , malla_curricular_materia mlcrmt"
					+ " , materia mtr"
					+ " , detalle_matricula dtmt"
					+ " , comprobante_pago cmpa");
			sbSql.append(" where prs.prs_id = fces.prs_id"
					+ " and fces.fces_id = rces.fces_id"
					+ " and fces.fces_id = fcmt.fces_id"
					+ " and rces.mlcrpr_id = mlcrpr.mlcrpr_id"
					+ " and prl.prl_id = mlcrpr.prl_id"
					+ " and crr.crr_id = prl.crr_id"
					+ " and dpn.dpn_id = crr.dpn_id"
					+ " and mtr.mtr_id = mlcrmt.mtr_id"
					+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id"
					+ " and fcmt.fcmt_id = cmpa.fcmt_id"
					+ " and cmpa.cmpa_id = dtmt.cmpa_id"
					+ " and mlcrpr.mlcrpr_id = dtmt.mlcrpr_id");
				
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO);sbSql.append(" = ? ");
				if(idCarrera != GeneralesConstantes.APP_ID_BASE){
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
				}
				sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");
				
				
			sbSql.append(" order by dpn.dpn_id"
					+ " , crr.crr_id"
					+ " , prs.prs_primer_apellido"
					+ " , prs.prs_segundo_apellido"
					+ " , prs.prs_nombres"
					+ " , mtr.mtr_descripcion");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoCarrera);
			pstmt.setInt(2, idPeriodo);
			pstmt.setInt(3, idPeriodo);
			pstmt.setInt(4, idDependencia);
			pstmt.setInt(5, FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(6, DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(7, idCarrera);
			}
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMatriculado(rs));
			}
			
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = null;
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.nivel.carrera.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public PersonaDto buscarDocentesAsignadosCHyCompartidosXPeriodoXDependencia(int idPeriodo, int mlcrprId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct");
				sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" from "
					+ " persona prs"
					+ " , ficha_docente fcdc"
					+ " , detalle_puesto dtps"
					+ " , paralelo prl"
					+ " , carrera crr"
					+ " , dependencia dpn "
					+ " , carga_horaria crhr"
					+ " , horario_academico hrac"
					+ " , malla_curricular_paralelo mlcrpr left join horario_academico hrac1 on mlcrpr.mlcrpr_id = hrac1.mlcrpr_id_comp ");
			sbSql.append(" where prs.prs_id = fcdc.prs_id"
					+ " and fcdc.fcdc_id = dtps.fcdc_id"
					+ " and dtps.dtps_id = crhr.dtps_id"
					+ " and crr.crr_id = prl.crr_id"
					+ " and prl.prl_id = mlcrpr.prl_id"
					+ " and mlcrpr.mlcrpr_id = crhr.mlcrpr_id"
					+ " and dpn.dpn_id = crr.dpn_id"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id");
				
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
				
			sbSql.append(" order by dpn.dpn_id"
					+ " , crr.crr_id"
					+ " , prs.prs_primer_apellido"
					+ " , prs.prs_segundo_apellido"
					+ " , prs.prs_nombres");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, CarreraConstantes.TIPO_PREGRADO_VALUE);
			pstmt.setInt(3, idPeriodo);
			pstmt.setInt(4, mlcrprId);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno = transformarResultSetADtoDocentesAsigandosCHyComparidos(rs);
			}
			
		} catch (SQLException e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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

	public List<MateriaDto> buscarMateriasPorCarreraNivel(int carreraId, int nivelId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");sql.append(" mlcrmt.");sql.append(JdbcConstantes.MLCRMT_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_CODIGO);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_ID);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_DESCRIPCION);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_SUB_ID); 
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_TIMT_ID);
		
		sql.append(" FROM ");sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr, ");
		sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sql.append(" mlcrmt, ");
		sql.append(JdbcConstantes.TABLA_MATERIA);sql.append(" mtr, ");
		sql.append(JdbcConstantes.TABLA_NIVEL);sql.append(" nvl ");
		
		sql.append(" WHERE ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);sql.append(" = ");sql.append(" mtr.");sql.append(JdbcConstantes.MTR_CRR_ID);
		sql.append(" AND mlcrmt.");sql.append(JdbcConstantes.MLCRMT_MTR_ID);sql.append(" = ");sql.append(" mtr.");sql.append(JdbcConstantes.MTR_ID);
		sql.append(" AND mlcrmt.");sql.append(JdbcConstantes.MLCRMT_NVL_ID);sql.append(" = ");sql.append(" nvl.");sql.append(JdbcConstantes.NVL_ID);
		sql.append(" AND mtr.");sql.append(JdbcConstantes.MTR_ESTADO);sql.append(" = ");sql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
		sql.append(" AND mlcrmt.");sql.append(JdbcConstantes.MLCRMT_ESTADO);sql.append(" = ");sql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
		sql.append(" AND mtr.");sql.append(JdbcConstantes.MTR_TIMT_ID);sql.append(" in ( ");sql.append(TipoMateriaConstantes.TIPO_ASIGNATURA_VALUE);sql.append(" , ");sql.append(TipoMateriaConstantes.TIPO_MODULAR_VALUE);sql.append(" ) ");
		sql.append(" AND nvl.");sql.append(JdbcConstantes.NVL_ID);sql.append(" = ");sql.append(" ? ");
		sql.append(" AND crr.");sql.append(JdbcConstantes.CRR_ID);sql.append(" = ? ");
		
		sql.append(" ORDER BY ");sql.append(" mtr.");sql.append(JdbcConstantes.MTR_DESCRIPCION);sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_SUB_ID);
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, nivelId);
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaPorCarreraNivel(rs));
			}
			
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception")));
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
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		
		return retorno;
	}
			
	
	public List<MateriaDto> buscarMateriasPorParalelo(int paraleloId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRL.PRL_ID, ");
		sql.append("   PRL.PRAC_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   MLCRPR.MLCRPR_CUPO, ");
		sql.append("   MLCRPR.MLCRPR_INSCRITOS, ");
		sql.append("   MLCRPR.MLCRPR_MODALIDAD, ");
		sql.append("   MLCRPR.MLCRMT_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.TIMT_ID");
		sql.append(" FROM MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE MLCRPR.PRL_ID  = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID      = PRAC.PRAC_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID    = MTR.MTR_ID ");
		sql.append(" AND PRL.PRL_ID       = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, paraleloId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAMateriasPorParaleloDto(rs));
			}
			
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public MateriaDto buscarMateriaPorParaleloMateria(int paraleloId, int materiaId) throws ParaleloDtoValidacionException, ParaleloDtoException, ParaleloDtoNoEncontradoException{
		MateriaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRL.PRL_ID, ");
		sql.append("   PRL.PRAC_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   MLCRPR.MLCRPR_CUPO, ");
		sql.append("   MLCRPR.MLCRPR_INSCRITOS, ");
		sql.append("   MLCRPR.MLCRPR_MODALIDAD, ");
		sql.append("   MLCRPR.MLCRMT_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.TIMT_ID");
		sql.append(" FROM MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE MLCRPR.PRL_ID  = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID      = PRAC.PRAC_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID    = MTR.MTR_ID ");
		sql.append(" AND MLCRMT.MTR_ID    = ? ");
		sql.append(" AND PRL.PRL_ID       = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setInt(2, paraleloId);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno = transformarResultSetAMateriasPorParaleloDto(rs);
			}
			
		} catch (SQLException e) {
			throw new ParaleloDtoException(e.getMessage());	
		} catch (NonUniqueResultException e) {
			throw new ParaleloDtoValidacionException("Se encontró mas de una materia con los parámetros solicitados.");
		}catch (NoResultException e) {
			throw new ParaleloDtoNoEncontradoException("No se encontró resultados con los parámetros solicitados.");
		} catch (Exception e) {
			throw new ParaleloDtoException(e.getMessage());
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
	 * MQ:
	 * Metodo que realiza la busqueda de materias homologadas con notas por  personaId ficha_matricula y periodo
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.
	  * @param periodoId - periodoId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarHomologacionesXestudianteXmatriculaXperiodoXcarrera(int personaId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
									
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
			sbSql.append(" , CASE WHEN rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
						
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ID);
			sbSql.append(" , cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);	
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
							
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUMA_P1_P2);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			
		
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);
			
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			
			sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
            sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			
			
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
            sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
		  // SELECT ANIDADO PARA CONSULTAR EL NUMERO DE MATRICULA DE DETALLE MATRICULA
			sbSql.append(", (SELECT ");			
			sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmtAux ");			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrprAux ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpaAux ");
			sbSql.append(" WHERE ");	
			sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrprAux.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dtmtAux.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" = ");sbSql.append(" cmpaAux.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(") as ");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			
			//   FIN SELECT ANIDADO
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" ON ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" ON ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" ON ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" ON ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" ON ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ON ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" ON ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" ON ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" ON ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
					
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" ON ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			
			sbSql.append(" WHERE ");	
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in (-1,3,10,12) ");//sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" ORDER BY ");sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
//						System.out.println(sbSql);	
//						System.out.println(personaId);
//						System.out.println(matriculaId);
//						System.out.println(periodoId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, personaId);
			pstmt.setInt(2, matriculaId);
			pstmt.setInt(3, periodoId);
//			pstmt.setInt(4, carreraId);
//				System.out.println(sbSql);
//				System.out.println(personaId);
//				System.out.println(matriculaId);
//				System.out.println(periodoId);
			
			
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<MateriaDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarHomologacionesXestudianteXmatriculaXperiodo(rs));
			}
		} catch (SQLException e) {
			
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();	
			throw new MateriaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.exception")));
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
			throw new MateriaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MateriaDto.buscar.por.persona.ficha.matricula.periodo.carrera.no.result.exception")));
		}	
		return retorno;
	}

	public List<MateriaDto> buscarMateriasDocentePorParalelo(int paraleloId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append( " SELECT DISTINCT PRS.PRS_ID, ");
		sql.append( "   PRS.PRS_IDENTIFICACION, ");
		sql.append( "   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append( "   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append( "   PRS.PRS_NOMBRES, ");
		sql.append( "   PRL.PRL_ID, ");
		sql.append( "   PRL.PRL_CODIGO, ");
		sql.append( "   PRL.PRL_DESCRIPCION, ");
		sql.append( "   PRL.PRL_ESTADO, ");
		sql.append( "   PRAC.PRAC_ID, ");
		sql.append( "   PRAC.PRAC_DESCRIPCION, ");
		sql.append( "   MLCRPR.MLCRPR_ID, ");
		sql.append( "   MLCRPR.MLCRPR_CUPO, ");
		sql.append( "   MLCRPR.MLCRPR_INSCRITOS, ");
		sql.append( "   MLCRPR.MLCRPR_MODALIDAD, ");
		sql.append( "   MLCRPR.MLCRMT_ID, ");
		sql.append( "   MTR.MTR_ID, ");
		sql.append( "   MTR.MTR_CODIGO, ");
		sql.append( "   MTR.MTR_DESCRIPCION, ");
		sql.append( "   MTR.MTR_HORAS, ");
		sql.append( "   MTR.MTR_CREDITOS, ");
		sql.append( "   MTR.MTR_HORAS_PRAC_SEMA, ");
		sql.append( "   NVL.NVL_ID, ");
		sql.append( "   NVL.NVL_NUMERAL, ");
		sql.append( "   NVL.NVL_DESCRIPCION, ");
		sql.append( "   CRR.CRR_ID, ");
		sql.append( "   CRR.CRR_DESCRIPCION, ");
		sql.append( "   CRR.CRR_PROCESO, ");
		sql.append( "   CAHR.CRHR_ID, ");
		sql.append( "   CAHR.MLCRPR_ID, ");
		sql.append( "   CAHR.MLCRPR_ID_PRINCIPAL, ");
		sql.append( "   MTR.TIMT_ID, ");
		sql.append( "   MTR.MTR_SUB_ID ");
		sql.append( " FROM MALLA_CURRICULAR_PARALELO MLCRPR ");
		sql.append( " LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID ");
		sql.append( " LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append( " LEFT JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append( " LEFT JOIN MATERIA MTR ON MLCRMT.MTR_ID = MTR.MTR_ID ");
		sql.append( " LEFT JOIN CARGA_HORARIA CAHR ON CAHR.MLCRPR_ID = MLCRPR.MLCRPR_ID AND CAHR.CRHR_ESTADO = "+CargaHorariaConstantes.ESTADO_ACTIVO_VALUE+" AND CAHR.CRHR_ESTADO_ELIMINACION = " +CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append( " LEFT JOIN DETALLE_PUESTO DTPS ON CAHR.DTPS_ID = DTPS.DTPS_ID ");
		sql.append( " LEFT JOIN FICHA_DOCENTE FCDC ON DTPS.FCDC_ID = FCDC.FCDC_ID ");
		sql.append( " LEFT JOIN PERSONA PRS ON FCDC.PRS_ID     = PRS.PRS_ID ");
		sql.append( " LEFT JOIN NIVEL NVL ON NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append( " LEFT JOIN MALLA_CURRICULAR MLCR ON MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append( " LEFT JOIN CARRERA CRR ON CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append( " WHERE  ");
		sql.append( " MLCRPR.PRL_ID  = ? ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, paraleloId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarMateriasDocentePorParalelo(rs));
			}
			
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
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
	private MateriaDto transformarResultSetAMateriaDto(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		return retorno;
		
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADto(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(GeneralesConstantes.APP_ID_BASE);
		return retorno;
		
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoAlterno(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(GeneralesConstantes.APP_ID_BASE);
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
		return retorno;
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoListarXperiodoActivoXcarreraXnivelXparaleloXdocente(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
		return retorno;
	} 
	
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset del metodo listarXestudianteXcarreraXperiodoXmatricula
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 * 
	 * 
	 */
	private MateriaDto transformarResultSetADtolistarXestudianteXmatriculaXperiodo(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		float notaAux1, ingresoNota;
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrTpmtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
			//retorno.setRcesIngresoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
			
			ingresoNota= rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA); //Se captura el valor de Ingreso de nota en record estudiante
			
			if(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA3)!=GeneralesConstantes.APP_ID_BASE){
				retorno.setRcesIngresoNota3(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA3)); //Se captura el valor de Ingreso de nota en record estudiante	
			}else{
				retorno.setRcesIngresoNota3(null);
			}
			
			
			if(ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE || ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){// verifico el valor de ingresoNota 
				retorno.setClfNota1(null);
				retorno.setClfAsistencia1(null);
				retorno.setClfNota2(null);
				retorno.setClfAsistencia2(null);
				retorno.setClfPromedioAsistencia(null);
				retorno.setClfPromedioNotas(null);
				retorno.setClfParamRecuperacion1(null);	
				retorno.setClfSupletorio(null);
				retorno.setClfParamRecuperacion2(null);
				retorno.setClfNotaFinalSemestre(null);
				

			}else{ // si Ingreso nota es NULL(-99)  o tiene gurdado final ( 2 )
				
				
				notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA1);	//capturo el valor de la nota		
				if(notaAux1==GeneralesConstantes.APP_ID_BASE){		// si es null(-99)		
				retorno.setClfNota1(null);						// guardo null en el campo para que no se visualice ningun valor
				}else{				
				retorno.setClfNota1(rs.getFloat(JdbcConstantes.CLF_NOTA1)); // si existe un valor guradado lo guarda en el campo
				}
				
			
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA1);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia1(null);							
				  }else{				
				  retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
				  }
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfNota2(null);							
				  }else{				
				  retorno.setClfNota2(rs.getFloat(JdbcConstantes.CLF_NOTA2));
				  }
				
				
				  notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia2(null);							
				  }else{				
				  retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
				  }
				  
				  notaAux1=rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioAsistencia(null);							
					}else{				
					retorno.setClfPromedioAsistencia(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
					}
					
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PROMEDIO_NOTAS);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfPromedioNotas(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_NOTAS));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_SUMA_P1_P2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLF_SUMA_P1_P2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion1(null);							
					}else{				
					retorno.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION1));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_SUPLETORIO);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfSupletorio(null);							
					}else{				
					retorno.setClfSupletorio(rs.getFloat(JdbcConstantes.CLF_SUPLETORIO));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion2(null);							
					}else{				
					retorno.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfNotaFinalSemestre(null);							
					}else{				
					retorno.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaTotalDoc(null);							
					}else{				
					retorno.setClfAsistenciaTotalDoc(rs.getFloat(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente1(null);							
					}else{				
					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente2(null);							
					}else{				
					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2));
					}
				
					
					
				
			}
		
			
			retorno.setClfEstado(rs.getInt(JdbcConstantes.CLF_ESTADO));
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));

			retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
			
		return retorno;
	} 
	
	
	private MateriaDto transformarResultSetADtoEstudiantes(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
			
		return retorno;
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset del metodo listarXestudianteXcarreraXperiodoXmatricula
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 * 
	 * 
	 */
	private MateriaDto transformarResultSetADtolistarXestudianteXmatriculaXperiodoModulo(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		float notaAux1, ingresoNota;
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
			//retorno.setRcesIngresoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			
			
			ingresoNota= rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA); //Se captura el valor de Ingreso de nota en record estudiante
			
			
			
			
			if(ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE || ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){// verifico el valor de ingresoNota 
				retorno.setClfNota1(null);
				retorno.setClfAsistencia1(null);
				retorno.setClfNota2(null);
				retorno.setClfAsistencia2(null);
				retorno.setClfPromedioAsistencia(null);
				retorno.setClfPromedioNotas(null);
				retorno.setClfParamRecuperacion1(null);	
				retorno.setClfSupletorio(null);
				retorno.setClfParamRecuperacion2(null);
				retorno.setClfNotaFinalSemestre(null);
				

			}else{ // si Ingreso nota es NULL(-99)  o tiene gurdado final ( 2 )
				
				
				notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA1);	//capturo el valor de la nota		
				if(notaAux1==GeneralesConstantes.APP_ID_BASE){		// si es null(-99)		
				retorno.setClfNota1(null);						// guardo null en el campo para que no se visualice ningun valor
				}else{				
				retorno.setClfNota1(rs.getFloat(JdbcConstantes.CLMD_NOTA1)); // si existe un valor guradado lo guarda en el campo
				}
				
			
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA1);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia1(null);							
				  }else{				
				  retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA1));
				  }
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfNota2(null);							
				  }else{				
				  retorno.setClfNota2(rs.getFloat(JdbcConstantes.CLMD_NOTA2));
				  }
				
				
				  notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia2(null);							
				  }else{				
				  retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA2));
				  }
				  
				  notaAux1=rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioAsistencia(null);							
					}else{				
					retorno.setClfPromedioAsistencia(rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA));
					}
					
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_NOTAS);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfPromedioNotas(rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_NOTAS));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion1(null);							
					}else{				
					retorno.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfSupletorio(null);							
					}else{				
					retorno.setClfSupletorio(rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion2(null);							
					}else{				
					retorno.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
						retorno.setClfNotaFinalSemestre(null);							
					}else{				
						retorno.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaTotalDoc(null);							
					}else{				
					retorno.setClfAsistenciaTotalDoc(rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente1(null);							
					}else{				
					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente2(null);							
					}else{				
					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2));
					}
				
					
					
				
			}
		
			
			retorno.setClfEstado(rs.getInt(JdbcConstantes.CLMD_ESTADO));
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));

			retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
			
		return retorno;
	} 
	
	private EstudianteJdbcDto transformarResultSetAEstudianteJdbcDto(ResultSet rs) throws SQLException{
		EstudianteJdbcDto retorno = new EstudianteJdbcDto();
		
		float notaAux1, ingresoNota;
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
			//retorno.setRcesIngresoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			
			
			ingresoNota= rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA); //Se captura el valor de Ingreso de nota en record estudiante
			
			
			
			
			if(ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE || ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){// verifico el valor de ingresoNota 
				retorno.setClfNota1(null);
				retorno.setClfAsistenciaEstudiante1(null);
				retorno.setClfNota2(null);
				retorno.setClfAsistenciaEstudiante2(null);
				retorno.setClfPromedioAsistencia(null);
				retorno.setClfPromedioNotas(null);
				retorno.setClfParamRecuperacion1(null);	
				retorno.setClfSupletorio(null);
				retorno.setClfParamRecuperacion2(null);
				retorno.setClfNotalFinalSemestre(null);
				

			}else{ // si Ingreso nota es NULL(-99)  o tiene gurdado final ( 2 )
				
				
				notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA1);	//capturo el valor de la nota		
				if(notaAux1==GeneralesConstantes.APP_ID_BASE){		// si es null(-99)		
				retorno.setClfNota1(null);						// guardo null en el campo para que no se visualice ningun valor
				}else{				
				retorno.setClfNota1(rs.getBigDecimal(JdbcConstantes.CLMD_NOTA1)); // si existe un valor guradado lo guarda en el campo
				}
				
			
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA1);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistenciaEstudiante1(null);							
				  }else{				
				  retorno.setClfAsistenciaEstudiante1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA1));
				  }
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfNota2(null);							
				  }else{				
				  retorno.setClfNota2(rs.getBigDecimal(JdbcConstantes.CLMD_NOTA2));
				  }
				
				
				  notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistenciaEstudiante2(null);							
				  }else{				
				  retorno.setClfAsistenciaEstudiante2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA2));
				  }
				  
				  notaAux1=rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioAsistencia(null);							
					}else{				
					retorno.setClfPromedioAsistencia(rs.getBigDecimal(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA));
					}
					
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_NOTAS);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfPromedioNotas(rs.getBigDecimal(JdbcConstantes.CLMD_PROMEDIO_NOTAS));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfSumaP1P2(rs.getBigDecimal(JdbcConstantes.CLMD_SUMA_P1_P2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion1(null);							
					}else{				
					retorno.setClfParamRecuperacion1(rs.getBigDecimal(JdbcConstantes.CLMD_PARAM_RECUPERACION1));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfSupletorio(null);							
					}else{				
					retorno.setClfSupletorio(rs.getBigDecimal(JdbcConstantes.CLMD_SUPLETORIO));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion2(null);							
					}else{				
					retorno.setClfParamRecuperacion2(rs.getBigDecimal(JdbcConstantes.CLMD_PARAM_RECUPERACION2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfNotalFinalSemestre(null);							
					}else{				
					retorno.setClfNotalFinalSemestre(rs.getBigDecimal(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaTotalDoc(null);							
					}else{				
					retorno.setClfAsistenciaTotalDoc(rs.getBigDecimal(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente1(null);							
					}else{				
					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente2(null);							
					}else{				
					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2));
					}
				
					
					
				
			}
		
			
			retorno.setClfEstado(rs.getBigDecimal(JdbcConstantes.CLMD_ESTADO));
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));

			retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
			
		return retorno;
	} 
	
	private MateriaDto transformarResultSetAEstudianteJdbcDtoModular(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		float ingresoNota;
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
//			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
//			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
//			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLMD_ID));
			//retorno.setRcesIngresoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			
			ingresoNota= rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA); //Se captura el valor de Ingreso de nota en record estudiante
			
			
			
			
			if(ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE || ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){// verifico el valor de ingresoNota 
				retorno.setClfNota1(null);
				retorno.setClfAsistencia1(null);
				retorno.setClfNota2(null);
				retorno.setClfAsistencia2(null);
				retorno.setClfPromedioAsistencia(null);
				retorno.setClfPromedioNotas(null);
				retorno.setClfParamRecuperacion1(null);	
				retorno.setClfSupletorio(null);
				retorno.setClfParamRecuperacion2(null);
				retorno.setClfNotaFinalSemestre(null);
				retorno.setClfAsistenciaTotal(null);

			}else{ // si Ingreso nota es NULL(-99)  o tiene gurdado final ( 2 )
				
				
//				notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA1);	//capturo el valor de la nota		
//				if(notaAux1==GeneralesConstantes.APP_ID_BASE){		// si es null(-99)		
//				retorno.setClfNota1(null);						// guardo null en el campo para que no se visualice ningun valor
//				}else{				
				retorno.setClfNota1(rs.getFloat(JdbcConstantes.CLMD_NOTA1)); // si existe un valor guradado lo guarda en el campo
//				}
				
			
				
//				 notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA1);			
//				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//				  retorno.setClfAsistencia1(null);							
//				  }else{				
				  retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA1));
//				  }
				
//				 notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA2);			
//				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//				  retorno.setClfNota2(null);							
//				  }else{				
				  retorno.setClfNota2(rs.getFloat(JdbcConstantes.CLMD_NOTA2));
//				  }
				
				
//				  notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA2);			
//				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//				  retorno.setClfAsistencia2(null);							
//				  }else{				
				  retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA2));
//				  }
				  
//				  notaAux1=rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfPromedioAsistencia(null);							
//					}else{				
					retorno.setClfPromedioAsistencia(rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_ASISTENCIA));
//					}
//					
//					
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_NOTAS);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfPromedioNotas(null);							
//					}else{				
					retorno.setClfPromedioNotas(rs.getFloat(JdbcConstantes.CLMD_PROMEDIO_NOTAS));
//					}
//					
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfPromedioNotas(null);							
//					}else{				
					retorno.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLMD_SUMA_P1_P2));
//					}
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfParamRecuperacion1(null);							
//					}else{				
					retorno.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION1));
//					}
//					
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfSupletorio(null);							
//					}else{				
					retorno.setClfSupletorio(rs.getFloat(JdbcConstantes.CLMD_SUPLETORIO));
//					}
//					
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfParamRecuperacion2(null);							
//					}else{				
					retorno.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLMD_PARAM_RECUPERACION2));
//					}
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfNotaFinalSemestre(null);							
//					}else{				
					retorno.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLMD_NOTA_FINAL_SEMESTRE));
//					}
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfAsistenciaTotalDoc(null);							
//					}else{				
					retorno.setClfAsistenciaTotalDoc(rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL_DOC));
//					}
//					
//					notaAux1=rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfAsistenciaTotal(null);							
//					}else{				
					retorno.setClfAsistenciaTotal(rs.getFloat(JdbcConstantes.CLMD_ASISTENCIA_TOTAL));
//					}
//					
//					notaAux1=rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfAsistenciaDocente1(null);							
//					}else{				
					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE1));
//					}
//					
//					notaAux1=rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfAsistenciaDocente2(null);							
//					}else{				
					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLMD_ASISTENCIA_DOCENTE2));
//					}
				
					
					
				
			}

			
		return retorno;
	} 
	
	/**MQ
	 * Método privado que sirve para trasformar los datos del resulset del metodo ListarXestudianteXmatriculaXperiodoXcarreraXPosgrado
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 * 
	 * 
	 */
	private MateriaDto transformarResultSetADtoListarXestudianteXmatriculaXperiodoXcarreraXPosgrado(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		float notaAux1, ingresoNota;
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			//retorno.setRcesIngresoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			
			
			ingresoNota= rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA); //Se captura el valor de Ingreso de nota en record estudiante
			
			if(ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE || ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){// verifico el valor de ingresoNota 
				retorno.setClfNota1(null);
				retorno.setClfAsistencia1(null);
				retorno.setClfNota2(null);
				retorno.setClfAsistencia2(null);
				retorno.setClfPromedioAsistencia(null);
				retorno.setClfPromedioNotas(null);
				retorno.setClfParamRecuperacion1(null);	
				retorno.setClfSupletorio(null);
				retorno.setClfParamRecuperacion2(null);
				retorno.setClfNotaFinalSemestre(null);
				

			}else{ // si Ingreso nota es NULL(-99)  o tiene gurdado final ( 2 )
				
				
				notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA1);	//capturo el valor de la nota		
				if(notaAux1==GeneralesConstantes.APP_ID_BASE){		// si es null(-99)		
				retorno.setClfNota1(null);						// guardo null en el campo para que no se visualice ningun valor
				}else{				
				retorno.setClfNota1(rs.getFloat(JdbcConstantes.CLF_NOTA1)); // si existe un valor guradado lo guarda en el campo
				}
				
			
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA1);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia1(null);							
				  }else{				
				  retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
				  }
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfNota2(null);							
				  }else{				
				  retorno.setClfNota2(rs.getFloat(JdbcConstantes.CLF_NOTA2));
				  }
				
				
				  notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia2(null);							
				  }else{				
				  retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
				  }
				  
				  notaAux1=rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioAsistencia(null);							
					}else{				
					retorno.setClfPromedioAsistencia(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
					}
					
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PROMEDIO_NOTAS);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfPromedioNotas(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_NOTAS));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_SUMA_P1_P2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLF_SUMA_P1_P2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion1(null);							
					}else{				
					retorno.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION1));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_SUPLETORIO);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfSupletorio(null);							
					}else{				
					retorno.setClfSupletorio(rs.getFloat(JdbcConstantes.CLF_SUPLETORIO));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion2(null);							
					}else{				
					retorno.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfNotaFinalSemestre(null);							
					}else{				
					retorno.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaTotalDoc(null);							
					}else{				
					retorno.setClfAsistenciaTotalDoc(rs.getFloat(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente1(null);							
					}else{				
					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente2(null);							
					}else{				
					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2));
					}
				
					
					
				
			}
			
			retorno.setClfEstado(rs.getInt(JdbcConstantes.CLF_ESTADO));
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		
		return retorno;
	} 
	
	
	
	
	
	private MateriaDto transformarResultSetAMateriasPorParaleloDto(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setPrlId(rs.getInt(1));
		retorno.setPracId(rs.getInt(2));
		retorno.setPrlCodigo(rs.getString(3));
		retorno.setPrlEstado(rs.getInt(4));
		retorno.setMlcrprId(rs.getInt(5));
		retorno.setMlcrprCupo(rs.getInt(6));
		retorno.setMlcrprInscritos(rs.getInt(7));
		retorno.setMlcrprModalidad(rs.getInt(8));
		retorno.setMlcrmtId(rs.getInt(9));
		retorno.setMtrId(rs.getInt(10));
		retorno.setMtrCodigo(rs.getString(11));
		retorno.setMtrDescripcion(rs.getString(12));
		retorno.setMtrTpmtId(rs.getInt(13));
		return retorno;
	}
	
	
			
	private MateriaDto transformarResultSetADtolistarXUsuarioXDependenciaXcarrera(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		float materiaAux1;	
		    retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrCodigoSniese(rs.getString(JdbcConstantes.MTR_CODIGO_SNIESE));
			retorno.setMtrEstado(rs.getInt(JdbcConstantes.MTR_ESTADO));
			materiaAux1=rs.getInt(JdbcConstantes.MTR_HORAS);			
			if(materiaAux1==GeneralesConstantes.APP_ID_BASE){				
			retorno.setMtrHoras(null);							
			}else{				
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			}
			materiaAux1=rs.getInt(JdbcConstantes.MTR_CREDITOS);			
			if(materiaAux1==GeneralesConstantes.APP_ID_BASE){				
			retorno.setMtrCreditos(null);							
			}else{				
			retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
			}
			retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
			retorno.setMtrIntegradoraHoras(rs.getInt(JdbcConstantes.MTR_INTEGRADORA_HORAS));
			retorno.setMtrPreProfesionalHoras(rs.getInt(JdbcConstantes.MTR_PRE_PROFESIONAL_HORAS));
			retorno.setMtrRelacionTrabajo(rs.getInt(JdbcConstantes.MTR_RELACION_TRABAJO));
			retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
			retorno.setMtrUnidadMedida(rs.getInt(JdbcConstantes.MTR_UNIDAD_MEDIDA));
			
			
			materiaAux1=rs.getInt(JdbcConstantes.MTR_HORAS_PRACTICAS);			
			if(materiaAux1==GeneralesConstantes.APP_ID_BASE){				
			retorno.setMtrHorasPracticas(null);							
			}else{				
			retorno.setMtrHorasPracticas(rs.getInt(JdbcConstantes.MTR_HORAS_PRACTICAS));
			}
			
			materiaAux1=rs.getInt(JdbcConstantes.MTR_HORAS_TRAB_AUTONOMO);			
			if(materiaAux1==GeneralesConstantes.APP_ID_BASE){				
			retorno.setMtrHorasTrabAutonomo(null);							
			}else{				
			retorno.setMtrHorasTrabAutonomo(rs.getBigDecimal(JdbcConstantes.MTR_HORAS_TRAB_AUTONOMO));
			}
			
			materiaAux1=rs.getInt(JdbcConstantes.MTR_HORAS_PRAC_SEMA);			
			if(materiaAux1==GeneralesConstantes.APP_ID_BASE){				
			retorno.setMtrHorasPracSema(null);							
			}else{				
			retorno.setMtrHorasPracSema(rs.getInt(JdbcConstantes.MTR_HORAS_PRAC_SEMA));
			}
			
			materiaAux1=rs.getInt(JdbcConstantes.MTR_HORAS_AUTONO_SEMA);			
			if(materiaAux1==GeneralesConstantes.APP_ID_BASE){				
			retorno.setMtrHorasAutonoSema(null);							
			}else{				
			retorno.setMtrHorasAutonoSema(rs.getBigDecimal(JdbcConstantes.MTR_HORAS_AUTONO_SEMA));
			}
			
			retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
			
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			retorno.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));
			
    		retorno.setNcprCrrId(rs.getInt(JdbcConstantes.NCPRCR_ID));
			
			retorno.setNcprId(rs.getInt(JdbcConstantes.NCPR_ID));
			retorno.setNcprDescripcion(rs.getString(JdbcConstantes.NCPR_DESCRIPCION));
			
			retorno.setTpmtId(rs.getInt(JdbcConstantes.TIMT_ID));
			retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
			
			retorno.setCmfrId(rs.getInt(JdbcConstantes.CMFR_ID));
			retorno.setCmfrDescripcion(rs.getString(JdbcConstantes.CMFR_DESCRIPCION));
			
		return retorno;
	} 
	
	
	private MateriaDto transformarResultSetADtoXidMateriaPadre(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
			retorno.setTpmtId(rs.getInt(JdbcConstantes.TIMT_ID));
			retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION))
			
			;
			
			
		return retorno;
	} 

	
	private MateriaDto transformarResultSetADtoXdescripcionXcarrera(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrEstado(rs.getInt(JdbcConstantes.MTR_ESTADO));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setTpmtId(rs.getInt(JdbcConstantes.TIMT_ID));
		retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
		return retorno;
	} 
	
	
	
	private MateriaDto transformarResultSetADtolistarMateriasPIdMlcr_PIdNvl(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrEstado(rs.getInt(JdbcConstantes.MTR_ESTADO));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrmtEstado(rs.getInt(JdbcConstantes.MLCRMT_ESTADO));
		retorno.setMlcrId(rs.getInt(JdbcConstantes.MLCR_ID));
		retorno.setMlcrCodigo(rs.getString(JdbcConstantes.MLCR_CODIGO));
		retorno.setMlcrCrrId(rs.getInt(JdbcConstantes.MLCR_CRR_ID));
		retorno.setMlcrDescripcion(rs.getString(JdbcConstantes.MLCR_DESCRIPCION));
		retorno.setMlcrEstado(rs.getInt(JdbcConstantes.MLCR_ESTADO));
		retorno.setMlcrVigencia(rs.getInt(JdbcConstantes.MLCR_VIGENTE));
		retorno.setMlcrTipoAprobacion(rs.getInt(JdbcConstantes.MLCR_TIPO_APROBACION));
		retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
		
		return retorno;
	} 

	
//	private MateriaDto transformarResultSetADtoListaEstudiantesNivelacion(ResultSet rs) throws SQLException{
//		MateriaDto retorno = new MateriaDto();
//		
//			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
//			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
//			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
//			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
//			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
//			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
//			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
//			
//			retorno.setClfEstado(rs.getInt(JdbcConstantes.CLF_ESTADO));
//			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
//			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
//			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
//			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
//			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
//			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
//			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
//			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
//			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
//			retorno.setFcinUsroId(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
//
//		return retorno;
//	} 
	
	
	private MateriaDto transformarResultSetADtoListaEstudiantesNivelacionCorregido(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcinUsroId(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
			retorno.setFcinUsroId(rs.getInt(JdbcConstantes.FCIN_ID));
		return retorno;
	} 

	private MateriaDto transformarResultSetADtoBuscarMateriaDtoPMlcrmt(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMlcrDescripcion(rs.getString(JdbcConstantes.MLCR_DESCRIPCION));
			return retorno;
	} 
	

	
	private MateriaDto transformarResultSetADtoPeriodoCarreraNivelParalelo(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			if(rs.getInt(JdbcConstantes.TIMT_ID) == TipoMateriaConstantes.TIPO_MODULO_VALUE){
				retorno.setMtrDescripcion(consultaSubMateria(rs.getInt(JdbcConstantes.MTR_SUB_ID))+" - "+rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			}else{
				retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			}
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
			retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrDpnId(rs.getInt(JdbcConstantes.CRR_DPN_ID));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setCrrDpnId(rs.getInt(JdbcConstantes.CRR_DPN_ID));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			retorno.setTpmtId(rs.getInt(JdbcConstantes.TIMT_ID));
			retorno.setTpmtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
			
		return retorno;
	} 
	

	private String consultaSubMateria(Integer idSubMateria){
		String retorno = null;
		try {
			if(idSubMateria.intValue() != 0){
				retorno = (servMateriaServicio.buscarPorId(idSubMateria)).getMtrDescripcion();
			}else{
				retorno = "";
			}
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		}
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtolistarXmallaXnivelXpreXcoreq(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		
		retorno.setPrrMtrId(rs.getInt(JdbcConstantes.PRR_MTR_ID));
		retorno.setPrrMtrPrrId(rs.getInt(JdbcConstantes.PRR_MTR_PREREQUISITO_ID));
		retorno.setPrrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		
		retorno.setCrqMtrId(rs.getInt(JdbcConstantes.CRQ_MTR_ID));
		retorno.setCrqMtrCorequisitoId(rs.getInt(JdbcConstantes.CRQ_MTR_COREQUISITO_ID));
		retorno.setCrqDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
		
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtobuscarRecordEstudiante(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrmtMateria(rs.getInt(JdbcConstantes.MLCRMT_MTR_ID));
		retorno.setMlcrmtNivel(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		
		retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		
				
		return retorno;
	}
	
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtobuscarRecordEstudianteFull(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrmtMateria(rs.getInt(JdbcConstantes.MLCRMT_MTR_ID));
		retorno.setMlcrmtNivel(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
		retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
		retorno.setTpmtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrCmbEstado(Boolean.TRUE);
		retorno.setValorMatricula(retorno.getValorMatricula() == null?BigDecimal.ZERO:retorno.getValorMatricula());
		
		retorno.setNumMatricula(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setValorMatricula(rs.getBigDecimal("DTMT_VALOR_POR_MATERIA"));
		
		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_ID));
		
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
				
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtolistarXmallaXnivel(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		return retorno;
	} 
	
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtobuscarRecordEstudianteXprsIdentificacion(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoMateriaCarrera(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		
		return retorno;
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoMateriaCarreraFull(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrHorasPracSema(rs.getInt(JdbcConstantes.MTR_HORAS_PRAC_SEMA));
		retorno.setMtrHorasAutonoSema(rs.getBigDecimal(JdbcConstantes.MTR_HORAS_AUTONO_SEMA));
		
		//TODO : VERIFICAR EN GRATUIDADES Y PERDIDAS
		if (retorno.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
			retorno.setMtrHorasPorSemana(retorno.getMtrHoras() + retorno.getMtrHorasPracSema());
		}else {
			retorno.setMtrHorasPorSemana(retorno.getMtrCreditos());
		}
		
		retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
		retorno.setMtrIntegradoraHoras(rs.getInt(JdbcConstantes.MTR_INTEGRADORA_HORAS));
		retorno.setMtrPreProfesionalHoras(rs.getInt(JdbcConstantes.MTR_PRE_PROFESIONAL_HORAS));
		retorno.setTpmtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrTpmtId(retorno.getTpmtId());
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		
		return retorno;
	} 
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtobuscarRecordEstudiantexIdentificacion(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setMtrEstadoLabel(rs.getString("ESTADO"));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoBuscarRecordEstudiante(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtobuscarRecordEstudianteHomologacion(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrmtMateria(rs.getInt(JdbcConstantes.MLCRMT_MTR_ID));
		retorno.setMlcrmtNivel(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		return retorno;
	}
	
	

	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoListarMateriasXFcesIdXNumeroMatriculaXEstadoMateriaXCarrera(ResultSet rs) throws SQLException{
		int valAux=0;
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		
		valAux = rs.getInt(JdbcConstantes.RCES_ESTADO);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setRcesEstado(valAux);
		}
		
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_ID));
		retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		return retorno;
	}
	
//	/**
//	 * MÃ©todo privado que sirve para trasformar los datos del resulset 
//	 * @param rs - rs parÃ¡metros de ingreso
//	 * @return retorna el dto seteado con los datos correctos
//	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
//	 */
//	private MateriaDto transformarResultSetADtoListarXprsIdXCarreraXEstadoXpracId(ResultSet rs) throws SQLException{
//		MateriaDto retorno = new MateriaDto();
//		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
//		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
//		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
//		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
//		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
//		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
//		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
//		retorno.setRcesEstadoTerceraMatri(rs.getInt(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI));
//		retorno.setCslId(rs.getInt(JdbcConstantes.CSL_ID));
//		retorno.setCslDescripcion(rs.getString(JdbcConstantes.CSL_DESCRIPCION));
//		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
//		retorno.setSltrmtDocumentoSolicitud(rs.getString(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD));
//		 
//		return retorno;
//	} 
	
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitud(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
	//	retorno.setRcesEstadoTerceraMatri(rs.getInt(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_ID));
		retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		return retorno;
	}
	
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private MateriaDto transformarResultSetADtoListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitudSIIUSAU(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
//		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
//		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
//		retorno.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
	//	retorno.setRcesEstadoTerceraMatri(rs.getInt(JdbcConstantes.RCES_ESTADO_TERCERA_MATRI));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
//		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_ID));
//		retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		return retorno;
	}

	private MateriaDto transformarResultSetAMateriaModuloDto(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(1));
		retorno.setMtrDescripcion(rs.getString(2));
		retorno.setMtrTpmtId(rs.getInt(3));
		retorno.setRcesId(rs.getInt(4));
		retorno.setRcesEstado(rs.getInt(5));			
		retorno.setMlcrmtId(rs.getInt(6));
		retorno.setPrlId(rs.getInt(7));
		retorno.setPrlDescripcion(rs.getString(8));
		retorno.setPrsId(rs.getInt(9));
		retorno.setPrsIdentificacion(rs.getString(10));
		retorno.setPrsPrimerApellido(rs.getString(11));
		retorno.setPrsSegundoApellido(rs.getString(12));
		retorno.setPrsNombres(rs.getString(13));
		retorno.setPrsMailInstitucional(rs.getString(14));
		retorno.setFcesId(rs.getInt(15));
		retorno.setCrrId(rs.getInt(16));
		retorno.setPracId(rs.getInt(17));
		retorno.setPracDescripcion(rs.getString(18));
		retorno.setPracEstado(rs.getInt(19));
		retorno.setFcmtId(rs.getInt(20));
		retorno.setMlcrprId(rs.getInt(21));
		return retorno;
		
	} 
	
	private MateriaDto transformarResultSetADtoMatriculado(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrTpmtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.FCMT_PRAC_ID));
		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		return retorno;
	} 
	
	private PersonaDto transformarResultSetADtoDocentesAsigandosCHyComparidos(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		return retorno;
	} 
	
	
	private MateriaDto transformarResultSetADtoMateriaPorCarreraNivel(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setMtrTpmtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrCmbEstado(Boolean.FALSE);
		return retorno;
	}
	
	
	
	/**
	 * MQ:
	 * Método privado que sirve para trasformar los datos del resulset del metodo listarHomologacionesXestudianteXcarreraXperiodoXmatricula
	 * @param rs - rs parametros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepcion de error en la consulta sql
	 * 
	 * 
	 */
	private MateriaDto transformarResultSetADtolistarHomologacionesXestudianteXmatriculaXperiodo(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		float notaAux1, ingresoNota;
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrTpmtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
			retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
			//retorno.setRcesIngresoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
			
			ingresoNota= rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA); //Se captura el valor de Ingreso de nota en record estudiante
			
			if(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA3)!=GeneralesConstantes.APP_ID_BASE){
				retorno.setRcesIngresoNota3(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA3)); //Se captura el valor de Ingreso de nota en record estudiante	
			}else{
				retorno.setRcesIngresoNota3(null);
			}
			
			
			if(ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE || ingresoNota == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){// verifico el valor de ingresoNota 
				retorno.setClfNota1(null);
				retorno.setClfAsistencia1(null);
				retorno.setClfNota2(null);
				retorno.setClfAsistencia2(null);
				retorno.setClfPromedioAsistencia(null);
				retorno.setClfPromedioNotas(null);
				retorno.setClfParamRecuperacion1(null);	
				retorno.setClfSupletorio(null);
				retorno.setClfParamRecuperacion2(null);
				retorno.setClfNotaFinalSemestre(null);
				

			}else{ // si Ingreso nota es NULL(-99)  o tiene gurdado final ( 2 )
				
				
				notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA1);	//capturo el valor de la nota		
				if(notaAux1==GeneralesConstantes.APP_ID_BASE){		// si es null(-99)		
				retorno.setClfNota1(null);						// guardo null en el campo para que no se visualice ningun valor
				}else{				
				retorno.setClfNota1(rs.getFloat(JdbcConstantes.CLF_NOTA1)); // si existe un valor guradado lo guarda en el campo
				}
				
			
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA1);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia1(null);							
				  }else{				
				  retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
				  }
				
				 notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfNota2(null);							
				  }else{				
				  retorno.setClfNota2(rs.getFloat(JdbcConstantes.CLF_NOTA2));
				  }
				
				
				  notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA2);			
				  if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
				  retorno.setClfAsistencia2(null);							
				  }else{				
				  retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
				  }
				  
				  notaAux1=rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioAsistencia(null);							
					}else{				
					retorno.setClfPromedioAsistencia(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
					}
					
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PROMEDIO_NOTAS);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfPromedioNotas(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_NOTAS));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_SUMA_P1_P2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfPromedioNotas(null);							
					}else{				
					retorno.setClfSumaP1P2(rs.getFloat(JdbcConstantes.CLF_SUMA_P1_P2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion1(null);							
					}else{				
					retorno.setClfParamRecuperacion1(rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION1));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_SUPLETORIO);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfSupletorio(null);							
					}else{				
					retorno.setClfSupletorio(rs.getFloat(JdbcConstantes.CLF_SUPLETORIO));
					}
					
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfParamRecuperacion2(null);							
					}else{				
					retorno.setClfParamRecuperacion2(rs.getFloat(JdbcConstantes.CLF_PARAM_RECUPERACION2));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfNotaFinalSemestre(null);							
					}else{				
					retorno.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
					}
					
					notaAux1=rs.getFloat(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaTotalDoc(null);							
					}else{				
					retorno.setClfAsistenciaTotalDoc(rs.getFloat(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente1(null);							
					}else{				
					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
					}
					
					notaAux1=rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);			
					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
					retorno.setClfAsistenciaDocente2(null);							
					}else{				
					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2));
					}
				
					
					
				
			}
		
			
			retorno.setClfEstado(rs.getInt(JdbcConstantes.CLF_ESTADO));
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
			retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));

			retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
			
		return retorno;
	} 
	
	
	private MateriaDto transformarResultSetAbuscarMateriasDocentePorParalelo(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		
		retorno.setPrsId(rs.getInt(1));
		retorno.setPrsIdentificacion(rs.getString(2));
		retorno.setPrsPrimerApellido(rs.getString(3));
		retorno.setPrsSegundoApellido(rs.getString(4));
		retorno.setPrsNombres(rs.getString(5));
		
		retorno.setPrlId(rs.getInt(6));
		retorno.setPrlCodigo(rs.getString(7));
		retorno.setPrlDescripcion(rs.getString(8));
		retorno.setPrlEstado(rs.getInt(9));
		
		retorno.setPracId(rs.getInt(10));
		retorno.setPracDescripcion(rs.getString(11));

		retorno.setMlcrprId(rs.getInt(12));
		retorno.setMlcrprCupo(rs.getInt(13));
		retorno.setMlcrprInscritos(rs.getInt(14));
		retorno.setMlcrprModalidad(rs.getInt(15));
		retorno.setMlcrmtId(rs.getInt(16));
		retorno.setMtrId(rs.getInt(17));
		retorno.setMtrCodigo(rs.getString(18));
		retorno.setMtrDescripcion(rs.getString(19));
		retorno.setMtrHoras(rs.getInt(20));
		retorno.setMtrCreditos(rs.getInt(21));
		retorno.setMtrHorasPracticas(rs.getInt(22));
		
		retorno.setNvlId(rs.getInt(23));
		retorno.setNvlNumeral(rs.getInt(24));
		retorno.setNvlDescripcion(rs.getString(25));

		retorno.setCrrId(rs.getInt(26));
		retorno.setCrrDescripcion(rs.getString(27));
		retorno.setCrrProceso(rs.getInt(28));
		
		retorno.setCahrId(rs.getInt(29));
		retorno.setCahrMlcrprId(rs.getInt(30));
		retorno.setCahrMlcrprIdPrincipal(rs.getInt(31));
		
		retorno.setMtrTpmtId(rs.getInt(32));
		retorno.setMtrSubId(rs.getInt(33));
		return retorno;
	}
	
	private EstudianteJdbcDto transformarResultSetADtoPromoverNivelacion(ResultSet rs) throws SQLException{
		EstudianteJdbcDto retorno = new EstudianteJdbcDto();
		
//		float notaAux1;
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));			
//			retorno.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			
				
				
//				retorno.setClfNota1(rs.getBigDecimal(JdbcConstantes.CLF_NOTA1)); // si existe un valor guradado lo guarda en el campo
//				
//			
//				
//				  retorno.setClfAsistenciaEstudiante1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
//				
//				  retorno.setClfNota2(rs.getBigDecimal(JdbcConstantes.CLF_NOTA2));
//				
//				
//				  retorno.setClfAsistenciaEstudiante2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
				  
//					retorno.setClfPromedioAsistencia(rs.getBigDecimal(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
					
					
					
//					retorno.setClfPromedioNotas(rs.getBigDecimal(JdbcConstantes.CLF_PROMEDIO_NOTAS));
//					
//					retorno.setClfSumaP1P2(rs.getBigDecimal(JdbcConstantes.CLF_SUMA_P1_P2));
//					
//					retorno.setClfParamRecuperacion1(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION1));
//					
//					
//					retorno.setClfSupletorio(rs.getBigDecimal(JdbcConstantes.CLF_SUPLETORIO));
//					
//					
//					retorno.setClfParamRecuperacion2(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION2));
					
//					notaAux1=rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);			
//					if(notaAux1==GeneralesConstantes.APP_ID_BASE){				
//					retorno.setClfNotaFinalSemestre(null);							
//					}else{				
//					retorno.setClfNotalFinalSemestre(rs.getBigDecimal(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
//					}
					
//					retorno.setClfAsistenciaTotalDoc(rs.getBigDecimal(JdbcConstantes.CLF_ASISTENCIA_TOTAL_DOC));
//					
//					retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
//					
//					retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2));
					
					
				
			
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrIdArea(rs.getInt("crrIdArea"));
			retorno.setCrrDescripcionArea(rs.getString("crrDescripcionArea"));
			retorno.setFcinUsroId(rs.getInt(JdbcConstantes.USRO_ID));
			retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		return retorno;
	} 
	
	private MateriaDto rsAbuscarModularPorParalelo(ResultSet rs) throws SQLException {
		MateriaDto retorno = new MateriaDto();
		retorno.setCrrId(rs.getInt(1));
		retorno.setCrrDescripcion(rs.getString(2));
		retorno.setMlcrmtId(rs.getInt(3));
		retorno.setMtrId(rs.getInt(4));
		retorno.setMtrTpmtId(rs.getInt(5));
		retorno.setMtrSubId(rs.getInt(6));
		retorno.setMtrCodigo(rs.getString(7));
		retorno.setMtrDescripcion(rs.getString(8));
		retorno.setMtrEstado(rs.getInt(9));
		retorno.setMlcrprId(rs.getInt(10));
		return retorno;
	}

	
}
