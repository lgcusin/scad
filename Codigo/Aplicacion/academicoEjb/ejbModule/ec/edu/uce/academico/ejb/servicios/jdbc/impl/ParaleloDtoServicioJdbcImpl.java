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
   
 ARCHIVO:     ParaleloDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla Paralelo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-03-2017		David Arellano				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB ParaleloDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Paralelo.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class ParaleloDtoServicioJdbcImpl implements ParaleloDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public List<ParaleloDto> buscarParalelosPorDocenteNivelPeriodoCarrera(String identificacion, int nivelAcademico, int periodo, int carrera)throws ParaleloNoEncontradoException, ParaleloValidacionException, ParaleloException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   PRL.PRL_FECHA, ");
		sql.append("   PRL.PRL_INICIO_CLASE, ");
		sql.append("   PRL.PRL_FIN_CLASE, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_SUB_ID, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   NVL.NVL_ID, ");
		sql.append("   NVL.NVL_DESCRIPCION, ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   CARGA_HORARIA CAHR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN, ");
		sql.append("   NIVEL NVL ");
		sql.append(" WHERE FCDC.PRS_ID                		= PRS.PRS_ID ");
		sql.append(" AND DTPS.FCDC_ID               		= FCDC.FCDC_ID ");
		sql.append(" AND CAHR.DTPS_ID                 		= DTPS.DTPS_ID ");
		sql.append(" AND CAHR.MLCRPR_ID               		= MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID                		= PRL.PRL_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID            		= MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID                		= MTR.MTR_ID ");
		sql.append(" AND PRL.PRAC_ID                  		= PRAC.PRAC_ID ");
		sql.append(" AND CRR.CRR_ID                   		= MTR.CRR_ID ");
		sql.append(" AND CRR.DPN_ID                   		= DPN.DPN_ID ");
		sql.append(" AND MLCRMT.NVL_ID                		= NVL.NVL_ID ");
		sql.append(" AND CAHR.CRHR_ESTADO       			= " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION       = " + CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND CRR.CRR_TIPO           = ? ");
		sql.append(" AND PRL.PRAC_ID            = ?  ");
		sql.append(" AND CRR.CRR_ID             = ? ");
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" ORDER BY  MTR.MTR_DESCRIPCION, PRL.PRL_CODIGO ");


		try {

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, nivelAcademico);
			pstmt.setInt(2, periodo);
			pstmt.setInt(3, carrera);
			pstmt.setString(4, identificacion);
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarParalelosPorDocenteNivelPeriodoCarrera(rs));				
			}

		} catch (SQLException e) {
			throw new ParaleloValidacionException("Error tipo sql, comuníquese con el administrador del sistema.");
		} catch (Exception e) {
			throw new ParaleloException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new ParaleloNoEncontradoException("No se encontró paralelos vinculadas al docente.");
		}	

		return retorno;
	}
	
	public List<RecordEstudianteDto> buscarMatriculados(int periodoId, int carreraId, int paraleloId, int mlcrmtId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {

		List<RecordEstudianteDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   MLCRPR.MLCRPR_CUPO, ");
		sql.append("   MLCRPR.MLCRPR_INSCRITOS, ");
		sql.append("   MLCRPR.MLCRPR_MODALIDAD, ");
		sql.append("   MLCRPR.MLCRMT_ID, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   FICHA_INSCRIPCION FCIN, ");
		sql.append("   FICHA_ESTUDIANTE FCES, ");
		sql.append("   RECORD_ESTUDIANTE RCES, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   FICHA_MATRICULA FCMT, ");
		sql.append("   COMPROBANTE_PAGO CMPG, ");
		sql.append("   DETALLE_MATRICULA DTMT, ");
		sql.append("   CONFIGURACION_CARRERA CNCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   NIVEL NVL, ");
		sql.append("   PARALELO PRL ");
		sql.append(" WHERE FCES.FCES_ID         = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_ID           = CMPG.FCMT_ID ");
		sql.append(" AND CMPG.CMPA_ID           = DTMT.CMPA_ID ");
		sql.append(" AND PRS.PRS_ID             = FCES.PRS_ID ");
		sql.append(" AND USR.PRS_ID             = PRS.PRS_ID ");
		sql.append(" AND RCES.MLCRPR_ID         = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID       = DTMT.MLCRPR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID       = MLCRPR.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID          = MTR.MTR_ID ");
		sql.append(" AND MTR.CRR_ID             = CRR.CRR_ID ");
		sql.append(" AND FCES.FCES_ID           = RCES.FCES_ID ");
		sql.append(" AND FCIN.FCIN_ID           = FCES.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID           = CNCR.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID             = CNCR.CRR_ID ");
		sql.append(" AND CRR.DPN_ID             = DPN.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID           = FCMT.FCMT_PRAC_ID ");
		sql.append(" AND DPN.DPN_ID             = DPN.DPN_ID ");
		sql.append(" AND NVL.NVL_ID             = MLCRMT.NVL_ID ");
		sql.append(" AND MLCRPR.PRL_ID          = PRL.PRL_ID ");
		sql.append(" AND FCMT.FCMT_PRAC_ID      = ? ");
		sql.append(" AND CRR.CRR_ID             = ? ");
		sql.append(" AND PRL.PRL_ID             = ? ");
		sql.append(" AND MLCRMT.MLCRMT_ID       = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, paraleloId);
			pstmt.setInt(4, mlcrmtId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarMatriculados(rs));
			}

		} catch (Exception e) {
			throw new RecordEstudianteException("Error de conexión, comuníquese con el administrador.");
		}finally {
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

		if (retorno.isEmpty()) {
			throw new RecordEstudianteNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}
		
		return retorno;
	}

	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por período
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXmallaMateriaXperiodo(int mallaMateriaId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mallaMateriaId);
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXmallaMateriaXperiodo(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por período
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXmallaMateriaXperiodoNivelacion(int mallaMateriaId, int periodoId, int crrId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID);
			sbSql.append(" = ");sbSql.append(crrId);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			System.out.println(sbSql);
			System.out.println(mallaMateriaId);
			System.out.println(periodoId);
			
			pstmt.setInt(1, mallaMateriaId);
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXmallaMateriaXperiodo(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	
	@Override
	public List<ParaleloDto> listarXmallaMateriaXperiodoNivelacionParaEdicion(int mallaMateriaId, int periodoId, int crrId, int areaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(areaId);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" - (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" + ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" )) <> 0 ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID);
			sbSql.append(" = ");sbSql.append(crrId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mallaMateriaId);
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			rs.next();
				retorno.add(transformarResultSetADtolistarXmallaMateriaXperiodo(rs));
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<ParaleloDto> listarXmallaMateriaXperiodoNivelacionParaEdicionFull(int materiaId, int periodoId, int crrId, int areaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(materiaId);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(areaId);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" - (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" + ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" )) <> 0 ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID);
			sbSql.append(" = ");sbSql.append(crrId);
			System.out.println(sbSql);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			rs.next();
				ParaleloDto aux = new ParaleloDto();
				aux.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
				aux.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
				retorno.add(aux);
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada carrera por período
	 * @param usuarioId - id del usuario
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera, ese período y usuario
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXusuarioXcarreraXperiodo(int usuarioId, int carreraId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			
			
			 
			 
			sbSql.append(" SELECT ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
		
			if(periodoId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
				
			}
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
			
			Integer aux =1;
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, carreraId);
			}			
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, periodoId);
			}
			
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXusuarioXcarreraXperiodo(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * 
	 * Método que busca el paralelo por carrera, por nivel.
	 * @param idPeriodo - id del periodo académico actual o vigente
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @return Lista de paralelos que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws ParaleloDtoException - EstudianteDtoJdbcException excepción general
	 * @throws ParaleloDtoNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra paralelos con los parametros ingresados
	 */
	public List<ParaleloDto> listarParaleloXPeriodoCarreraNivel(int idPeriodo, int idCarrera, int idNivel) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(", ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
										
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
					
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			    
			    sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
				
				sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
							
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idPeriodo); //cargo el id del periodo academico
			pstmt.setInt(2, idCarrera); //cargo el id carrera
			pstmt.setInt(3, idNivel); //cargo el id nivel
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXCarreraXNivel(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	
	
	
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoXcarreraXnivelXdocente(int periodoId, int carreraId, int nivelId, int docente) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, periodoId);
			pstmt.setInt(3, carreraId);
			pstmt.setInt(4, nivelId);
			pstmt.setInt(5, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXCarreraXNivel(rs));//transformarResultSetADtoXPeriodoActivoXCarreraXNivel
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
		
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocente( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");

			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, docente);
			pstmt.setInt(5, materiaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivel(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(docente);
//			System.out.println(materiaId); 
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			int contador = 1;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, docente);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @author Daniel
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteXmateriaNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
//			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
//			sbSql.append(" in (");sbSql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ? ");
				if(nivelId!=GeneralesConstantes.APP_ID_BASE){
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
					sbSql.append(" = ? ");
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ? ");
				}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(docente);
//			System.out.println(nivelId);
//			System.out.println(materiaId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, docente);

			if (nivelId != GeneralesConstantes.APP_ID_BASE) {
				pstmt.setInt(3, nivelId);
				pstmt.setInt(4, materiaId);
			}

			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @author Daniel
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteInactivoXmateriaNoComp( int carreraId, int nivelId, int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(pracId);
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
//			sbSql.append(" in (");sbSql.append(DetallePuestoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ? ");
				if(nivelId!=GeneralesConstantes.APP_ID_BASE){
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
					sbSql.append(" = ? ");
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ? ");
				}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, docente);

			if (nivelId != GeneralesConstantes.APP_ID_BASE) {
				pstmt.setInt(3, nivelId);
				pstmt.setInt(4, materiaId);
			}

			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @author Daniel
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarParalelosTotalesXcarreraXPeriodoNoComp( int carreraId, String identificacionDocente, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
//			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
//			sbSql.append(" in (");sbSql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" LIKE ? ");
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" = ? ");
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			pstmt.setInt(1, carreraId);
			pstmt.setString(2, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacionDocente)+"%");

			pstmt.setInt(3, periodoId);
				
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	
	@Override
	public List<ParaleloDto> listarParalelosXProgramaXnivelXdocenteXmateriaNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			
//			
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = 1 ");
			
//			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
//			sbSql.append(" =  ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ? ");
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ? ");
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, docente);

			if (nivelId != GeneralesConstantes.APP_ID_BASE) {
//				pstmt.setInt(3, nivelId);
				pstmt.setInt(3, materiaId);
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<ParaleloDto> listarParalelosXProgramaXnivelXdocenteXmateriaNoCompEnLineaX350( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			
//			
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = 0 ");
			
//			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
//			sbSql.append(" =  ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ? ");
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ? ");
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, docente);

			if (nivelId != GeneralesConstantes.APP_ID_BASE) {
//				pstmt.setInt(3, nivelId);
				pstmt.setInt(3, materiaId);
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @author Daniel
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteXmateriaNoCompXPracId( int carreraId, int nivelId, int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			
//			
//			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
//			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(pracId);
			
//			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
//			sbSql.append(" =  ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ? ");
				if(nivelId!=GeneralesConstantes.APP_ID_BASE){
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
					sbSql.append(" = ? ");
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ? ");
				}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
				pstmt.setInt(1, carreraId);
				pstmt.setInt(2, docente);
				
				if(nivelId!=GeneralesConstantes.APP_ID_BASE){
					pstmt.setInt(3, nivelId);
					pstmt.setInt(4, materiaId);
				}
				
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @author Daniel
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteXmateriaNoCompParaReporte( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" =  ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ? ");
				if(nivelId!=GeneralesConstantes.APP_ID_BASE){
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
					sbSql.append(" = ? ");
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ? ");
				}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
				pstmt.setInt(1, carreraId);
				pstmt.setInt(2, docente);
				
				if(nivelId!=GeneralesConstantes.APP_ID_BASE){
					pstmt.setInt(3, nivelId);
					pstmt.setInt(4, materiaId);
				}
				
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelCompParaReporte(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(docente);
//			System.out.println(materiaId); 
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			int contador = 1;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, docente);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteNoCompAnterior( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(docente);
//			System.out.println(materiaId); 
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			int contador = 1;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, docente);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoEnCierreXcarreraXnivelXdocenteNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(docente);
//			System.out.println(materiaId);
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			int contador = 1;
			 
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, docente);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoEnCierreXcarreraXnivelXdocenteNoCompXCedula( int carreraId, int nivelId, int docente, int materiaId, String identificacion) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" is null ");
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
//			if(docente != GeneralesConstantes.APP_ID_BASE){
//				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//				sbSql.append(" = ? ");
//			}
			
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			if(identificacion != null){
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" = ? ");
			}
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(docente);
//			System.out.println(materiaId);
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			int contador = 1;
			 
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
//			if(docente != GeneralesConstantes.APP_ID_BASE){
//				contador ++;
//				pstmt.setInt(contador, docente);
//			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			pstmt.setString(contador+1, identificacion);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" (select distinct mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr,"
+" periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt," 
+" carrera crr WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (0,2)"
+" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ?" 
+" and hrac_estado=0  and crhr_estado=0 and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and" 
+" mtr.crr_id=crr.crr_id) )");
//			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(materiaId);
//			System.out.println(docente);
				pstmt.setInt(1, carreraId);
				pstmt.setInt(2, nivelId);
				pstmt.setInt(3, materiaId);
			pstmt.setInt(4, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	
	@Override
	public List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteInactivoXMateriaComp( int carreraId, int nivelId, int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and prac.prac_id =  "+pracId
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" (select distinct mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr,"
+" periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt," 
+" carrera crr WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (1)"
+" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ?" 
+" and hrac_estado=0  and crhr_estado=0 and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and" 
+" mtr.crr_id=crr.crr_id) )");
//			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(materiaId);
//			System.out.println(docente);
				pstmt.setInt(1, carreraId);
				pstmt.setInt(2, nivelId);
				pstmt.setInt(3, materiaId);
			pstmt.setInt(4, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarParaleloReporteNotasDocenteXCrrXPracXIdentificacionComp( int carreraId, String identificacion, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" (select distinct mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr,"
+" periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt,persona prs," 
+" carrera crr WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (0,2)"
+" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and prs_identificacion = ?" 
+" and prs.prs_id=usr.prs_id and hrac_estado=0 and dtps_estado=0 and crhr_estado=0 and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and" 
+" mtr.crr_id=crr.crr_id and prac.prac_id=?) )");
//			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(identificacion);
//			System.out.println(pracId);
//			System.out.println(docente);
				pstmt.setInt(1, carreraId);
				pstmt.setString(2, identificacion);
				pstmt.setInt(3, pracId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaCompXpracId( int carreraId, int nivelId, int docente, int materiaId , int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" =");
			sbSql.append(pracId);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" (select distinct mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr,"
					+" periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt," 
					+" carrera crr WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ?" 
							+" and hrac_estado=0 and crhr_estado=0 and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and" 
							+" mtr.crr_id=crr.crr_id) )");
//			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(materiaId);
//			System.out.println(docente);
				pstmt.setInt(1, carreraId);
				pstmt.setInt(2, nivelId);
				pstmt.setInt(3, materiaId);
			pstmt.setInt(4, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteNoCompModular( int carreraId, int nivelId, int docente, int materiaId, String paralelo) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
//			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
//			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
//			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" is null ");
			
//			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);sbSql.append(" = ? ");
			
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			int contador = 1;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(docente != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, docente);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			contador ++;
			pstmt.setString(contador, paralelo);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelCompModular(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteCompartida( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" select hrac.mlcrpr_id from persona prs ,ficha_docente fcdc ,detalle_puesto dtps ,carga_horaria crhr"
					+" ,malla_curricular_paralelo mlcrpr ,horario_academico hrac ,paralelo prl ,malla_curricular_materia mlcrmt ,materia mtr "
					+" ,nivel nvl ,carrera crr where prs.prs_id = fcdc.prs_id and fcdc.fcdc_id = dtps.fcdc_id and dtps.dtps_id = crhr.dtps_id "
					+" and crhr.mlcrpr_id = mlcrpr.mlcrpr_id and hrac.mlcrpr_id = mlcrpr.mlcrpr_id and prl.prl_id = mlcrpr.prl_id and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
					+" and mtr.mtr_id = mlcrmt.mtr_id and nvl.nvl_id = mlcrmt.nvl_id and crr.crr_id = mtr.crr_id ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			contador ++;			
			pstmt.setInt(contador, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
		
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteCompartidaAnterior( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" select hrac.mlcrpr_id from persona prs ,ficha_docente fcdc ,detalle_puesto dtps ,carga_horaria crhr"
					+" ,malla_curricular_paralelo mlcrpr ,horario_academico hrac ,paralelo prl ,malla_curricular_materia mlcrmt ,materia mtr "
					+" ,nivel nvl ,carrera crr where prs.prs_id = fcdc.prs_id and fcdc.fcdc_id = dtps.fcdc_id and dtps.dtps_id = crhr.dtps_id "
					+" and crhr.mlcrpr_id = mlcrpr.mlcrpr_id and hrac.mlcrpr_id = mlcrpr.mlcrpr_id and prl.prl_id = mlcrpr.prl_id and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
					+" and mtr.mtr_id = mlcrmt.mtr_id and nvl.nvl_id = mlcrmt.nvl_id and crr.crr_id = mtr.crr_id ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			contador ++;			
			pstmt.setInt(contador, docente);
//			System.out.println(sbSql);
//			System.out.println(docente);
//			System.out.println(carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoEnCierreXcarreraXnivelXdocenteCompartida( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" select hrac.mlcrpr_id from persona prs ,ficha_docente fcdc ,detalle_puesto dtps ,carga_horaria crhr"
					+" ,malla_curricular_paralelo mlcrpr ,horario_academico hrac ,paralelo prl ,malla_curricular_materia mlcrmt ,materia mtr "
					+" ,nivel nvl ,carrera crr where prs.prs_id = fcdc.prs_id and fcdc.fcdc_id = dtps.fcdc_id and dtps.dtps_id = crhr.dtps_id "
					+" and crhr.mlcrpr_id = mlcrpr.mlcrpr_id and hrac.mlcrpr_id = mlcrpr.mlcrpr_id and prl.prl_id = mlcrpr.prl_id and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
					+" and mtr.mtr_id = mlcrmt.mtr_id and nvl.nvl_id = mlcrmt.nvl_id and crr.crr_id = mtr.crr_id ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, carreraId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, nivelId);
			}
			if(materiaId != GeneralesConstantes.APP_ID_BASE){
				contador ++;
				pstmt.setInt(contador, materiaId);
			}
			contador ++;			
			pstmt.setInt(contador, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocentePagados( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");

			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, docente);
			pstmt.setInt(5, materiaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivel(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel para posgrado
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelPosgrado( int carreraId, int nivelId, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" <> 63 ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");

			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, materiaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelPosgrado(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada carrera por período y nivel
	 * @param usuarioId - id del usuario
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * * @param nivelId - id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera, ese período y usuario
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXusuarioXcarreraXperiodoXNivel(int usuarioId, int carreraId, int periodoId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			
			
			 
			 
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
//			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
//			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
//			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
//			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
//			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
//			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					
//			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
//			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);

			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
		
			if(periodoId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
				
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" = ? ");
				
			}
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
			
			Integer aux =1;
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, carreraId);
			}			
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, periodoId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, nivelId);
			}
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXusuarioXCarreraXPeriodoXNivel(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada carrera por período y nivel
	 * @param usuarioId - id del usuario
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * * @param nivelId - id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera, ese período y usuario
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXusuarioXcarreraXperiodoXNivelXArea(int usuarioId, int carreraId, int periodoId, int nivelId, int areaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			
			
			 
			 
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
//			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
//			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
//			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
//			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
//			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
//			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					
//			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
//			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);

			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
		
			if(periodoId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
				
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" = ? ");
				
			}
			if(areaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID);
				sbSql.append(" = ? ");
				
			}
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
			
			Integer aux =1;
			
			if(carreraId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, carreraId);
			}			
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, periodoId);
			}
			if(nivelId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, nivelId);
			}
			if(areaId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, areaId);
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXusuarioXCarreraXPeriodoXNivel(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por período
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param nivelId - nivelId id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXNivelCarreraXperiodo(int carreraId, int periodoId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" , ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAParaleloDto(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los paralelos de cada materia activos e inactivos para hacer la cuenta de paralelos
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param nivelId - nivelId id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarXNivelCarreraXperiodoNumMaxParalelo(int carreraId, int periodoId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" ON ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" ON ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
//			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" like ? ");
//			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
//			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" , ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setString(3, "%"+nivelId+"-%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAParaleloDto(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por período
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param nivelId - nivelId id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> listarMayorParaleloXNivelCarreraXperiodo(int carreraId, int periodoId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT MAX( ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" ), prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" , ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAParaleloDto(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por estado y malla_curricular
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public ParaleloDto listarXMallaCurricularXCarreraXEstadoActivo(int carreraId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		ParaleloDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId);
			rs = pstmt.executeQuery();
			retorno = new ParaleloDto();
			while(rs.next()){
				retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
				retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
				retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
				retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
				retorno.setCrrId(rs.getInt(JdbcConstantes.PRL_CRR_ID));
				retorno.setPracId(rs.getInt(JdbcConstantes.PRL_PRAC_ID));
				retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
	 * 
	 * Método que busca el paralelo por carrera, por nivel para postgrado
	 * @param idPeriodo - id del periodo académico actual o vigente
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @return Lista de paralelos que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws ParaleloDtoException - EstudianteDtoJdbcException excepción general
	 * @throws ParaleloDtoNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra paralelos con los parametros ingresados
	 */
	public List<ParaleloDto> listarParaleloXPeriodoCarreraNivelPostgrado(int idPeriodo, int idCarrera, int idNivel) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(", ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
										
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			    
			    sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
				
				sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
							
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idPeriodo); //cargo el id del periodo academico
			pstmt.setInt(2, idCarrera); //cargo el id carrera
			pstmt.setInt(3, idNivel); //cargo el id nivel
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXCarreraXNivel(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * 
	 * Método que busca el paralelo por carrera, por nivel para postgrado sin exception no encontrado
	 * @param idPeriodo - id del periodo académico actual o vigente
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @return Lista de paralelos que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws ParaleloDtoException - EstudianteDtoJdbcException excepción general
	 * @throws ParaleloDtoNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra paralelos con los parametros ingresados
	 */
	public List<ParaleloDto> listarParaleloXPeriodoCarreraNivelPostgradoNuevo(int idPeriodo, int idCarrera, int idNivel) throws ParaleloDtoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(", ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
										
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			    
			    sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
				
				sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
							
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idPeriodo); //cargo el id del periodo academico
			pstmt.setInt(2, idCarrera); //cargo el id carrera
			pstmt.setInt(3, idNivel); //cargo el id nivel
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXCarreraXNivel(rs));
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
		if(retorno == null || retorno.size()<=0){
		//	throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del paralelo que pertenecen homologacion de la carrera
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param descripcion- la descripcion del paralelo
	 * @return  paralelo  que cumple con la descripcion.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public ParaleloDto buscarXmallaMateriaXperiodoXDescripcion(int mallaMateriaId, int periodoId, String descripcion) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		ParaleloDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mallaMateriaId);
			pstmt.setInt(2, periodoId);
			pstmt.setString(3, descripcion);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.malla.materia.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda del paralelo por Materia Id
	 * @param MateriaId - id de la materia 
	 * @return  paralelo  que cumple con la busqueda.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> ListarXMateriaId(int MateriaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
//			sbSql.append(" not in ( ");sbSql.append(" 2,4 )");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" <> ");sbSql.append("'");sbSql.append(ParaleloConstantes.PARALELO_HOMOLOGACION_LABEL);sbSql.append("'");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, MateriaId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarresultSetAParaleloDto(rs));
			}
			
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<ParaleloDto> ListarXMateriaIdNivelacion(int MateriaId,int pracId, int crrId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(pracId);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ");sbSql.append(crrId);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" - (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" + ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" )) <> 0 ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, MateriaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarresultSetAParaleloDto(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.no.result.exception")));
		}	
		return retorno;
	}
	
	
	public List<ParaleloDto> buscarParlelosPorMateriaPeriodo(int materiaId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
		sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
		
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
		
		sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);sbSql.append(" <> ");sbSql.append("'");sbSql.append(ParaleloConstantes.PARALELO_HOMOLOGACION_LABEL);sbSql.append("'");
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ? ");
		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");

		sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarListarXMateriaIdResultSetADto(rs));
			}
			
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.sql.exception")));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del paralelo por Materia Id
	 * @param MateriaId - id de la materia 
	 * @return  paralelo  que cumple con la busqueda.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> ListarXMateriaIdXDisponibilidadCupo(int MateriaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" ( mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" > ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			
			sbSql.append(" OR ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" is null ) ");
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, MateriaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarListarXMateriaIdResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			
			
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.sql.exception")));
		} catch (Exception e) {
			
			
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.exception")));
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
//			
//			
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.no.result.exception")));
//		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda del paralelo por Materia Id
	 * @param MateriaId - id de la materia 
	 * @return  paralelo  que cumple con la busqueda.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<ParaleloDto> ListarXMateriaIdXDisponibilidadCupoEnCierre(int MateriaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" ( mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" > ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			
			sbSql.append(" OR ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" is null ) ");
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, MateriaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarListarXMateriaIdResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			
			
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.sql.exception")));
		} catch (Exception e) {
			
			
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.exception")));
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
//			
//			
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.no.result.exception")));
//		}	
		return retorno;
	}
	
	public List<ParaleloDto> ListarXMateriaIdXDisponibilidadCupoXTipoPeriodo(int materiaId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append(" SELECT ");
		sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
		sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
		sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID); sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID); sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID); sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO); sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" ( mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);sbSql.append(" > ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);sbSql.append(" OR ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);sbSql.append(" is null ) ");
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ? ");
		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
		sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarListarXMateriaIdResultSetADto(rs));
			}

		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.disponibilidad.no.result.exception")));
		}	
		return retorno;
	}
	
	
	public List<ParaleloDto> buscarParalelos(int periodoId, Date fecha) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT DISTINCT PRAC.PRAC_ID , ");
			sql.append("   PRAC.PRAC_DESCRIPCION , ");
			sql.append("   CRR.CRR_ID , ");
			sql.append("   CRR.CRR_DESCRIPCION , ");
			sql.append("   PRL.PRL_ID , ");
			sql.append("   PRL.PRL_CODIGO , ");
			sql.append("   PRL.PRL_DESCRIPCION , ");
			sql.append("   PRL.PRL_FECHA , ");
			sql.append("   PRL.PRL_ESTADO , ");
			sql.append("   ALA.ALA_ID, ");
			sql.append("   ALA.ALA_CODIGO, ");
			sql.append("   ALA.ALA_DESCRIPCION, ");
			sql.append("   NVL.NVL_ID , ");
			sql.append("   NVL.NVL_DESCRIPCION , ");
			sql.append("   MLCRPR.MLCRPR_ID, ");
			sql.append("   MLCRPR.MLCRPR_CUPO, ");
			sql.append("   MLCRPR.MLCRPR_INSCRITOS, ");
			sql.append("   MLCRPR.MLCRPR_MODALIDAD, ");
			sql.append("   MLCRPR.MLCRMT_ID, ");
			sql.append("   PRS.PRS_ID, ");
			sql.append("   PRS.PRS_IDENTIFICACION, ");
			sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
			sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
			sql.append("   PRS.PRS_NOMBRES, ");
			sql.append("   DTPS.DTPS_ID, ");
			sql.append("   CAHR.CRHR_ID, ");
			sql.append("   CAHR.CRHR_NUM_HORAS ");
			sql.append(" FROM CARRERA CRR , ");
			sql.append("   PARALELO PRL , ");
			sql.append("   PERIODO_ACADEMICO PRAC , ");
			sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR , ");
			sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT , ");
			sql.append("   MALLA_CURRICULAR MLCR , ");
			sql.append("   MALLA_PERIODO MLPR , ");
			sql.append("   NIVEL NVL, ");
			sql.append("   HORARIO_ACADEMICO HOAC, ");
			sql.append("   HORA_CLASE_AULA HOCLAU, ");
			sql.append("   AULA ALA, ");
			sql.append("   CARGA_HORARIA CAHR, ");
			sql.append("   DETALLE_PUESTO DTPS, ");
			sql.append("   FICHA_DOCENTE FCDC, ");
			sql.append("   PERSONA PRS ");
			sql.append(" WHERE CRR.CRR_ID                 = PRL.CRR_ID ");
			sql.append(" AND MLCRPR.MLCRPR_ID             = CAHR.MLCRPR_ID ");
			sql.append(" AND DTPS.DTPS_ID                 = CAHR.DTPS_ID ");
			sql.append(" AND FCDC.FCDC_ID                 = DTPS.FCDC_ID ");
			sql.append(" AND PRS.PRS_ID                   = FCDC.PRS_ID ");
			sql.append(" AND HOAC.HOCLAL_ID               = HOCLAU.HOCLAL_ID ");
			sql.append(" AND HOCLAU.ALA_ID                = ALA.ALA_ID ");
			sql.append(" AND PRAC.PRAC_ID                 = PRL.PRAC_ID ");
			sql.append(" AND PRL.PRL_ID                   = MLCRPR.PRL_ID ");
			sql.append(" AND MLCRMT.MLCRMT_ID             = MLCRPR.MLCRMT_ID ");
			sql.append(" AND NVL.NVL_ID                   = MLCRMT.NVL_ID ");
			sql.append(" AND MLCR.MLCR_ID                 = MLCRMT.MLCR_ID ");
			sql.append(" AND MLCR.MLCR_ID                 = MLCRMT.MLCR_ID ");
			sql.append(" AND MLCR.MLCR_ID                 = MLPR.MLCR_ID ");
			sql.append(" AND PRAC.PRAC_ID                 = MLPR.PRAC_ID ");
			sql.append(" AND MLCRPR.MLCRPR_ID             = HOAC.MLCRPR_ID ");
			sql.append(" AND CAHR.CRHR_ESTADO             = " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION = " + CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
			sql.append(" AND PRAC.PRAC_ID                 = ? ");
			sql.append(" AND PRL.PRL_FECHA               >= ? ");


			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setDate(2, new java.sql.Date(fecha.getTime()));
			rs = pstmt.executeQuery();

			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(buscarParalelosExoneracoiones(rs));
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
	
	public 	List<ParaleloDto> buscarParalelosPorAreaCarrera(int periodoId,int areaId, int carreraId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID);

			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
		    sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		    sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, areaId);
			pstmt.setInt(3, carreraId);
			pstmt.setInt(4, nivelId); 
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarParalelosPorAreaCarrera(rs));
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
	
	public List<ParaleloDto> buscarParalelosPorDocente(int carreraId, int materiaId, String identificacion) throws ParaleloNoEncontradoException, ParaleloException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   PRL.PRL_FECHA, ");
		sql.append("   PRL.PRL_INICIO_CLASE, ");
		sql.append("   PRL.PRL_FIN_CLASE, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   PRAC.PRAC_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   CARGA_HORARIA CAHR, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   CARRERA CRR ");
		sql.append(" WHERE PRS.PRS_ID           = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID           = DTPS.FCDC_ID ");
		sql.append(" AND DTPS.DTPS_ID           = CAHR.DTPS_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID       = CAHR.MLCRPR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID       = MLCRPR.MLCRMT_ID ");
		sql.append(" AND MTR.MTR_ID             = MLCRMT.MTR_ID ");
		sql.append(" AND PRL.PRL_ID             = MLCRPR.PRL_ID ");
		sql.append(" AND PRAC.PRAC_ID           = PRL.PRAC_ID ");
		sql.append(" AND CRR.CRR_ID             = PRL.CRR_ID ");
		sql.append(" AND cahr.crhr_estado            			 =  " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND cahr.crhr_estado_eliminacion            =  " + CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND PRAC.PRAC_ESTADO      IN ( "+PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE+" , "+ PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE+" )");
		sql.append(" AND CRR.CRR_ID             = ? ");
		sql.append(" AND MTR.MTR_ID             = ? ");
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" ORDER BY  PRAC.PRAC_DESCRIPCION, PRAC.PRAC_ESTADO, PRL.PRL_CODIGO ");
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, materiaId);
			pstmt.setString(3, identificacion); 
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsBuscarParalelosPorDocente(rs));
			}
			
		} catch (SQLException e) {
			throw new ParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.exception")));
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
			throw new ParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	public List<ParaleloDto> buscarParalelos(int periodoId, int carreraId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);

			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
		    sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId); 
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAparaleloPorCarreraNivel(rs));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	public List<ParaleloDto> buscarParalelos(int periodoId, int carreraId, int nivelId, int modalidadId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
		
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
		    sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);sbSql.append(" = ? ");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId); 
			pstmt.setInt(4, modalidadId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<ParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAparaleloDto(rs));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.carrera.nivel.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	public List<ParaleloDto> buscarParalelosPorModalidad(int modalidad, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<ParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT "); sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
		sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
		sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
		sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD);
		sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
		sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
		sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID); sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID); sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
		
		sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO); sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" <> ");sbSql.append(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);

		
		sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID); sbSql.append(" = ? ");
		sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MODALIDAD); sbSql.append(" = ? ");

		
		if (modalidad == ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE) {
			sbSql.append(" AND PRL.PRL_FECHA               >= ? ");
			
		}
		
		sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setInt(2, modalidad);
			
			if (modalidad == ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE) {
				Date fechaCursos = GeneralesUtilidades.getPrimerDiaDeLaSemana(GeneralesUtilidades.getFechaMasDiasIncremento(GeneralesUtilidades.getFechaActualSistema(), 7));
				pstmt.setDate(3, new java.sql.Date(fechaCursos.getTime()));
			}
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarParalelosPorModalidad(rs));
			}

		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.no.result.exception")));
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
	private ParaleloDto transformarResultSetADtolistarXmallaMateriaXperiodo(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRPR_MLCRMT_ID));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			retorno.setMlcrprReservaRepetidos(rs.getInt(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS));
		return retorno;
	} 

	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private ParaleloDto transformarResultSetADto(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRPR_MLCRMT_ID));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		return retorno;
	} 

	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private ParaleloDto transformarResultSetAParaleloDto(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
			retorno.setPrlCarrera(rs.getInt(JdbcConstantes.PRL_CRR_ID));
			retorno.setPrlPeriodoAcademico(rs.getInt(JdbcConstantes.PRL_PRAC_ID));
		return retorno;
	} 
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset listarXcarreraXperiodo
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private ParaleloDto transformarResultSetADtoXusuarioXcarreraXperiodo(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		
		  retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		  retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		  retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		  retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		  retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		  retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
	      retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		  retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
		  retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
		  
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset listarXcarreraXperiodoXnivel
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private ParaleloDto transformarResultSetADtoXusuarioXCarreraXPeriodoXNivel(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		
		  retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		  retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		  retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		  retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		  retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		  retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		  retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
	      retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		  retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
		  retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
		  retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		  retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		return retorno;
	}
	
	private ParaleloDto transformarResultSetADtoXCarreraXNivel(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
	return retorno;
	}
	
	
	
	private ParaleloDto transformarResultSetADtoXPeriodoActivoXCarreraXNivel(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMlcrmtNvlId(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
	return retorno;
	}
	
	private ParaleloDto transformarResultSetADtoXPeriodoActivoXCarreraXNivelPosgrado(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMlcrmtNvlId(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
	return retorno;
	}
	
	private ParaleloDto transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setMlcrmtNvlId(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		try {
			retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));	
		} catch (Exception e) {
		}
		try {
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));	
		} catch (Exception e) {
		}
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		try {
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerAPellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		} catch (Exception e) {
		}
		try {
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return retorno;
	}
	
	
	private ParaleloDto transformarResultSetADtoXPeriodoActivoXCarreraXNivelCompParaReporte(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setMlcrmtNvlId(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
		return retorno;
	}
	
	private ParaleloDto transformarResultSetADtoXPeriodoActivoXCarreraXNivelCompModular(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setMlcrmtNvlId(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
//		retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
		return retorno;
	}
	
	
	private ParaleloDto transformarListarXMateriaIdResultSetADto(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
		retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
	}
	
	private ParaleloDto transformarresultSetAParaleloDto(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
		retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMlcrprModalidad(rs.getInt(JdbcConstantes.MLCRPR_MODALIDAD));
		return retorno;
	}

	private ParaleloDto rsAbuscarParalelosPorModalidad(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
		retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		retorno.setMlcrprModalidad(rs.getInt(JdbcConstantes.MLCRPR_MODALIDAD));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
	}
	
	private ParaleloDto transformarResultSetAparaleloDto(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrprModalidad(rs.getInt(JdbcConstantes.MLCRPR_MODALIDAD));
		retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
		retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		return retorno;
	}
	
	private ParaleloDto buscarParalelosExoneracoiones(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		CarreraDto carrera = new CarreraDto();
		NivelDto nivel = new NivelDto();
		MallaCurricularParaleloDto malla = new MallaCurricularParaleloDto();
		PersonaDto docente = new PersonaDto();
		CargaHorariaDto carga  = new CargaHorariaDto();
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		
		carrera.setCrrId(rs.getInt(3));
		carrera.setCrrDescripcion(rs.getString(4));
		
		retorno.setPrlId(rs.getInt(5));
		retorno.setPrlCodigo(rs.getString(6));
		retorno.setPrlDescripcion(rs.getString(7));
		retorno.setPrlFecha(rs.getTimestamp(8));
		retorno.setPrlEstado(rs.getInt(9));
		retorno.setAlaId(rs.getInt(10));
		retorno.setAlaCodigo(rs.getString(11));
		retorno.setAlaDescripcion(rs.getString(12));
		
		nivel.setNvlId(rs.getInt(13));
		nivel.setNvlDescripcion(rs.getString(14));
		
		malla.setMlcrprId(rs.getInt(15));
		malla.setMlcrprCupo(rs.getInt(16));
		malla.setMlcrprInscritos(rs.getInt(17));
		malla.setMlcrprModalidad(rs.getInt(18));
		malla.setMlcrprMallaCurricularMateriaId(rs.getInt(19));

		docente.setPrsId(rs.getInt(20));
		docente.setPrsIdentificacion(rs.getString(21));
		docente.setPrsPrimerApellido(rs.getString(22));
		docente.setPrsSegundoApellido(rs.getString(23));
		docente.setPrsNombres(rs.getString(24));
		
		carga.setDtpsId(rs.getInt(25));
		carga.setCahrId(rs.getInt(26));
		carga.setCahrNumHoras(rs.getInt(27)); 
		
		retorno.setPrlPeriodoAcademicoDto(periodo);
		retorno.setPrlCarreraDto(carrera);
		retorno.setPrlNivelDto(nivel);
		retorno.setPrlMallaCurricularParaleloDto(malla);
		retorno.setPrlPersonaDto(docente);
		retorno.setPrlCargaHorariaDto(carga);
		
		return retorno;
	}
	
	private ParaleloDto transformarResultSetAparaleloPorCarreraNivel(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrprModalidad(rs.getInt(JdbcConstantes.MLCRPR_MODALIDAD));

		return retorno;
	}
	
	private RecordEstudianteDto rsAbuscarMatriculados (ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PersonaDto estudiante = new PersonaDto();
		ParaleloDto paralelo = new ParaleloDto();
		MateriaDto materia = new MateriaDto();
		MallaCurricularParaleloDto malla = new MallaCurricularParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		estudiante.setPrsId(rs.getInt(1));
		estudiante.setPrsIdentificacion(rs.getString(2));
		estudiante.setPrsPrimerApellido(rs.getString(3));
		estudiante.setPrsSegundoApellido(rs.getString(4));
		estudiante.setPrsNombres(rs.getString(5));
		
		paralelo.setPrlId(rs.getInt(6));
		paralelo.setPrlCodigo(rs.getString(7));
		paralelo.setPrlDescripcion(rs.getString(8));
		
		materia.setMtrId(rs.getInt(9));
		materia.setMtrCodigo(rs.getString(10));
		materia.setMtrDescripcion(rs.getString(11));
		
		malla.setMlcrprId(rs.getInt(12));
		malla.setMlcrprCupo(rs.getInt(13));
		malla.setMlcrprInscritos(rs.getInt(14));
		malla.setMlcrprModalidad(rs.getInt(15));
		malla.setMlcrprMallaCurricularMateriaId(rs.getInt(16));
		
		retorno.setRcesId(rs.getInt(17));
		retorno.setRcesEstadoLabel(rs.getString(18));
		
		periodo.setPracId(rs.getInt(19));
		periodo.setPracDescripcion(rs.getString(20));
		retorno.setRcesModalidadDto(new ModalidadDto(malla.getMlcrprModalidad()));
		
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesParaleloDto(paralelo);
		retorno.setRcesMateriaDto(materia);
		retorno.setRcesMallaCurricularParaleloDto(malla);
		retorno.setRcesPeriodoAcademicoDto(periodo);
		
		return retorno;
	}

	private ParaleloDto transformarResultSetAbuscarParalelosPorAreaCarrera(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPrlEstado(rs.getInt(JdbcConstantes.PRL_ESTADO));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrprNivelacionCrrId(rs.getInt(JdbcConstantes.MLCRPR_NIVELACION_CRR_ID));

		return retorno;
	}
	
	private ParaleloDto rsBuscarParalelosPorDocente(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		PersonaDto docente = new PersonaDto();
		MateriaDto materia = new MateriaDto();
		CarreraDto carrera = new CarreraDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		docente.setPrsId(rs.getInt(1));
		docente.setPrsIdentificacion(rs.getString(2));
		docente.setPrsPrimerApellido(rs.getString(3));
		docente.setPrsSegundoApellido(rs.getString(4));
		docente.setPrsNombres(rs.getString(5));
		
		retorno.setPrlId(rs.getInt(6));
		retorno.setPrlCodigo(rs.getString(7));
		retorno.setPrlDescripcion(rs.getString(8));
		retorno.setPrlFecha(rs.getTimestamp(9));
		retorno.setPrlInicioClase(rs.getTimestamp(10));
		retorno.setPrlFinClase(rs.getTimestamp(11));
		
		materia.setMtrId(rs.getInt(12));
		materia.setMtrCodigo(rs.getString(13));
		materia.setMtrDescripcion(rs.getString(14));
		
		carrera.setCrrId(rs.getInt(15));
		carrera.setCrrDescripcion(rs.getString(16));
		
		periodo.setPracId(rs.getInt(17));
		periodo.setPracDescripcion(rs.getString(18));
		periodo.setPracEstado(rs.getInt(19));
		
		retorno.setMlcrprId(rs.getInt(20));
		
		retorno.setPrlPersonaDto(docente);
		retorno.setPrlMateriaDto(materia);
		retorno.setPrlCarreraDto(carrera);
		retorno.setPrlPeriodoAcademicoDto(periodo);
		return retorno;
	}
	

	private ParaleloDto rsAbuscarParalelosPorDocenteNivelPeriodoCarrera(ResultSet rs) throws SQLException{
		ParaleloDto retorno = new ParaleloDto();
		PersonaDto docente = new PersonaDto();
		MateriaDto materia = new MateriaDto();
		CarreraDto carrera = new CarreraDto();
		DependenciaDto dependencia = new DependenciaDto();
		NivelDto nivel = new NivelDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		docente.setPrsId(rs.getInt(1));
		docente.setPrsIdentificacion(rs.getString(2));
		docente.setPrsPrimerApellido(rs.getString(3));
		docente.setPrsSegundoApellido(rs.getString(4));
		docente.setPrsNombres(rs.getString(5));
		
		retorno.setPrlId(rs.getInt(6));
		retorno.setPrlCodigo(rs.getString(7));
		retorno.setPrlDescripcion(rs.getString(8));
		retorno.setPrlFecha(rs.getTimestamp(9));
		retorno.setPrlInicioClase(rs.getTimestamp(10));
		retorno.setPrlFinClase(rs.getTimestamp(11));
		
		retorno.setMlcrprId(rs.getInt(12));

		
		materia.setMtrId(rs.getInt(13));
		materia.setMtrCodigo(rs.getString(14));
		materia.setMtrDescripcion(rs.getString(15));
		materia.setMtrSubId(rs.getInt(16));
		materia.setMtrTpmtId(rs.getInt(17));
		
		nivel.setNvlId(rs.getInt(18));
		nivel.setNvlDescripcion(rs.getString(19));
		nivel.setNvlNumeral(rs.getInt(20));
		
		carrera.setCrrId(rs.getInt(21));
		carrera.setCrrDescripcion(rs.getString(22));
		carrera.setCrrTipo(rs.getInt(23));
		
		dependencia.setDpnId(rs.getInt(24));
		dependencia.setDpnDescripcion(rs.getString(25));
		
		periodo.setPracId(rs.getInt(26));
		periodo.setPracDescripcion(rs.getString(27));
		
		retorno.setPrlPersonaDto(docente);
		retorno.setPrlMateriaDto(materia);
		retorno.setPrlCarreraDto(carrera);
		retorno.setPrlDependenciaDto(dependencia);
		retorno.setPrlNivelDto(nivel);
		retorno.setPrlPeriodoAcademicoDto(periodo);
		return retorno;
	}
	
}
