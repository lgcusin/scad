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
   
 ARCHIVO:     FichaMatriculaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla FichaMatricula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-03-2017			David Arellano					       Emisión Inicial
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
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * EJB FichaMatriculaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla FichaMatricula.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class FichaMatriculaDtoServicioJdbcImpl implements FichaMatriculaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	@Override
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraXsuficiencia(int periodoId,	String personaIdentificacion, int carreraId) throws FichaMatriculaException {
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT   ");
			sql.append("  DISTINCT   ");
			sql.append("  fcmt.fcmt_id  ,fcmt.fcmt_nivel_ubicacion  ,fcmt.fcmt_estado ,fcmt.fcmt_fecha_convalidacion   ");
			sql.append("  ,fcmt.fcmt_tipo ,fcmt.fcmt_modalidad  ,fcmt.fcmt_valor_total  ,fcmt.fcmt_fecha_matricula  ");
			sql.append("  ,plcr.plcr_id ,fces.fces_id  ");
			sql.append("  ,prac.prac_id ,prac.prac_descripcion  ,prac.prac_fecha_incio  ");
			sql.append("  ,prs.prs_id ,prs.prs_identificacion ,prs.prs_primer_apellido ,prs.prs_segundo_apellido ,prs.prs_nombres, prs.prs_mail_institucional ");
			sql.append("  ,fcin.fcin_id ,fcin.usro_id ,fcin.fcin_cncr_area ,fcin.fcin_carrera_siiu ,fcin.cncr_id  ");
			sql.append("  ,cncr.cncr_id  ");
			sql.append("  ,crr.crr_id ,crr.crr_detalle  ,crr.crr_descripcion  ,crr.crr_espe_codigo ,crr.dpn_id , crr.crr_tipo ");
			sql.append("  FROM ficha_matricula fcmt  ");
			sql.append("  , ficha_estudiante fces   ");
			sql.append("  , ficha_inscripcion fcin  ");
			sql.append("  , configuracion_carrera cncr  ");
			sql.append("   , carrera crr  ");
			sql.append("  , persona prs  ");
			sql.append("  , planificacion_cronograma plcr  ");
			sql.append("  , cronograma_proceso_flujo crprfl  ");
			sql.append("  , cronograma crn  ");
			sql.append("  , periodo_academico prac   ");
			sql.append("  WHERE  fcmt.fces_id =  fces.fces_id  ");
			sql.append("  AND  fces.fcin_id =  fcin.fcin_id ");
			sql.append("  AND  fcin.cncr_id =  cncr.cncr_id ");
			sql.append("  AND  cncr.crr_id =  crr.crr_id  ");
			sql.append("  AND  fces.prs_id =  prs.prs_id ");
			sql.append("  AND  fcmt.plcr_id =  plcr.plcr_id ");
			sql.append("  AND  plcr.crprfl_id =  crprfl.crprfl_id  ");
			sql.append("  AND  crprfl.crn_id =  crn.crn_id ");
			sql.append("  AND  fcmt.fcmt_prac_id =  prac.prac_id ");
			sql.append("  AND  prs.prs_identificacion = ?   ");

			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sql.append("  AND  fcmt.fcmt_prac_id = ?   ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sql.append("  AND  crr.crr_id = ? ");
			}
			
			sql.append(" ORDER BY ");sql.append(" fcmt.");sql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarrera(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
//			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
//			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
//			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); 
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); 
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
			retorno = null;
			throw new FichaMatriculaException("El estudiante solicitado aún no registra matrícula en el período académico actual.");
		}
		
		return retorno;
	}
	
	
	
	/**MQ:  7 feb 2019
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraParaTercerasMatriculas(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
//			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			//sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); 
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); 
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
			retorno = null;
		}
		
		return retorno;
	}
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraTodos(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ( ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" ) ");
			
			
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
//			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			//sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND ( ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
				sbSql.append(" =  ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				//para idiomas
//				sbSql.append(" OR  ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
//				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				sbSql.append(" ) ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
//			System.out.println("matri"+sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de las matriculas de estudiantes en pregrado y suficiencia en informática 
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarMatriculaPregradoYSufInformaticaXPeriodoXidentificacionXcarrera(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ( ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" ) ");
			
			
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
//			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			//sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" not in ( ");
			sbSql.append(" SELECT crr2.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" from carrera crr2 where crr2.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" in ( ");
			sbSql.append(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE);sbSql.append(" , ");sbSql.append(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" crr2.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_SUFICIENCIA_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona

			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	
	
	/**MQ: 17 ene 2019
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraTodosPregrado(int periodoId, String personaIdentificacion, int carreraId, List<PeriodoAcademico> listaPeriodo) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
//			sbSql.append(" AND ( ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			sbSql.append(" ) ");
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
//			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			//sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}else{
				
				 // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaPeriodo.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaPeriodo.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
				
				//sbSql.append(" AND ( ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
			//	sbSql.append(" =  ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
//				sbSql.append(" OR  ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
//				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
			//	sbSql.append(" ) ");
//				sbSql.append(" AND ( ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//				sbSql.append(" OR ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//				sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//				sbSql.append(" ) ");
				
			}
				
			
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			//System.out.println(sbSql.toString());
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
//		System.out.println("matri"+sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
					
			
			Integer contador = 1;			
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++contador, carreraId); //cargo la carrera
			}
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++contador, periodoId); //cargo el periodo academico
			}else{
				for (PeriodoAcademico item : listaPeriodo) {
							pstmt.setInt(++contador, item.getPracId()); //cargo la carrera
				}
				
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraNivelacion(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			//sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNivelacion(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	public List<FichaMatriculaDto> buscarFichaMatriculaXPeriodoXIdentificacion(int periodoId, String personaIdentificacion) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
				sbSql.append(" = ? ");
			}
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); //cargo el periodo academico
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				FichaMatriculaDto fcmtAux = new FichaMatriculaDto();
				fcmtAux.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
				retorno.add(fcmtAux);
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona para posgrado
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	@Override
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraPosgrado(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
//			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
//			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
//			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
//			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
//			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
//			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
//			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoPosgrado(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico activo, identifiacion de la persona, por apellido, por carrera
	 * @param periodoId - periodoId  String del período académico a buscar
	 * @param listaCarreras - listaCarreras lista de carreras a buscar, si se selecciona todas
	 * @param carreraId - carreraId id de la carrera a buscar
	 * @param personaIdentificacion - personaIdentificacion número de identificación de la persona a buscar
	 * @param personaApellido - personaApellido apellido paterno de la persona a buscar
	 * @return retorna la lista de ficha matriculas - lista de estudiantes matriculados
	 * @throws FichaMatriculaException - FichaMatriculaException excepción general lanzada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXapellidoXidentificacionXcarrera(int periodoId, List<CarreraDto> listaCarreras, int carreraId, String personaIdentificacion, String personaApellido) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_CAMBIO);
			sbSql.append(" = ");sbSql.append(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_SOLICITUD);
			sbSql.append(" = ");sbSql.append(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE);
			sbSql.append(" AND ( ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" not in ");sbSql.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);
			sbSql.append(" or ");;sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" is null ) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
				
			if(carreraId == GeneralesConstantes.APP_ID_BASE){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaCarreras.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}else{//caso para una sola carrera
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			int contador = 1;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, periodoId); //cargo el periodo academico
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE.intValue()){
				for (CarreraDto item : listaCarreras) {
					contador++;
					pstmt.setInt(contador, item.getCrrId()); //cargo la carrera
				}
			}else{
				contador++;
				pstmt.setInt(contador, carreraId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.apellido.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.apellido.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico activo, identifiacion de la persona, por apellido, por carrera
	 * @param periodoId - periodoId  String del período académico a buscar
	 * @param listaCarreras - listaCarreras lista de carreras a buscar, si se selecciona todas
	 * @param carreraId - carreraId id de la carrera a buscar
	 * @param personaIdentificacion - personaIdentificacion número de identificación de la persona a buscar
	 * @param personaApellido - personaApellido apellido paterno de la persona a buscar
	 * @return retorna la lista de ficha matriculas - lista de estudiantes matriculados
	 * @throws FichaMatriculaException - FichaMatriculaException excepción general lanzada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoActivoEnCierreXapellidoXidentificacionXcarrera(int periodoId, List<CarreraDto> listaCarreras, int carreraId, String personaIdentificacion, String personaApellido) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_CAMBIO);
			sbSql.append(" = ");sbSql.append(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_SOLICITUD);
			sbSql.append(" = ");sbSql.append(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE);
			sbSql.append(" AND ( ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" not in ");sbSql.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);
			sbSql.append(" or ");;sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" is null ) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND (prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				sbSql.append(" OR prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaCarreras.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}else{//caso para una sola carrera
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			int contador = 1;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, periodoId); //cargo el periodo academico
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE.intValue()){
				for (CarreraDto item : listaCarreras) {
					contador++;
					pstmt.setInt(contador, item.getCrrId()); //cargo la carrera
				}
			}else{
				contador++;
				pstmt.setInt(contador, carreraId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.apellido.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.apellido.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarFcmtPosgradoPpracPprsIdentificacionPcrrId(int pracId, String prsIdentificacion, int crrId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(crrId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, prsIdentificacion); //cargo la identificacion de la persona
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, pracId); //cargo el periodo academico
			}
			if(crrId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, crrId); //cargo la carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula del estudiante  del periodo activo, carreras de pregrado, periodo de tipo pregrado por usuarioId y por la carrera 
	 * @param usuarioId - Usuario a buscar
	 * @param carreraID - Carrera a buscar
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	@Override
	public List<FichaMatriculaDto> ListarXidPersonaXcarrera(int personaId, int carreraId, int periodoId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT distinct ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE "); sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND "); sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND "); sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND "); sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND "); sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND "); sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND "); sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND "); sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND "); sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND "); sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND "); sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND "); sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);

			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, personaId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, periodoId);
			rs = pstmt.executeQuery();
			
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetListarXidPersonaXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula del estudiante  del periodo activo, carreras de pregrado, periodo de tipo pregrado por usuarioId y por la carrera 
	 * @param usuarioId - Usuario a buscar
	 * @param carreraID - Carrera a buscar
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	@Override
	public List<FichaMatriculaDto> ListarXidPersonaXcarreraXPracEnCierre(int personaId, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT distinct ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);sbSql.append(" , ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);sbSql.append(" , ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE);sbSql.append(" ) ");
			
//			sbSql.append(" AND ");
//			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
//			sbSql.append(" AND ");
//			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, personaId); //cargo la id Usuario
			pstmt.setInt(2, carreraId); //cargo la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetListarXidUsuarioXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula del estudiante  del periodo activo, carreras de pregrado, periodo de tipo pregrado por usuarioId y por la carrera 
	 * @param usuarioId - Usuario a buscar
	 * @param carreraID - Carrera a buscar
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	@Override
	public List<FichaMatriculaDto> ListarXidPersonaXcarreraXtipo(int personaId, int carreraId, int tipoPeriodo) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT distinct ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_ID);
			sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);sbSql.append(" , ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);sbSql.append(" , ");
			sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE);sbSql.append(" ) ");
			
//			sbSql.append(" AND ");
//			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND ");
//			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
//			sbSql.append(" AND ");
//			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, personaId); //cargo la id Usuario
			pstmt.setInt(2, carreraId); //cargo la carrera
			pstmt.setInt(3, tipoPeriodo); //cargo el tipo de periodo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetListarXidUsuarioXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	
	/**MQ
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarrera(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);			
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);sbSql.append(" DESC");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			
			int cont=1;
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, carreraId); //cargo la carrera
			}
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
		
			while(rs.next()){
				retorno.add(transformarResultSetListarXPeriodoXidentificacionXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	
	
	
	/**MQ
	 * Realiza la busqueda de un FichaMatricula de posgrado por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarreraPosgrado(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);			
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" =  ");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			
			int cont=1;
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, carreraId); //cargo la carrera
			}
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
		
			while(rs.next()){
				retorno.add(transformarResultSetListarXPeriodoXidentificacionXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarreraFull(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA); 
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);			
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); 
			
			int cont=1;
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, periodoId); 
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, carreraId); 
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
		
			while(rs.next()){
				retorno.add(transformarResultSetListarXPeriodoXidentificacionXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @param estado - estado del detalle matricula
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada 
	 */
	public List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarreraXEstado(int periodoId, String personaIdentificacion, int carreraId, int estado) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
//			sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
//			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);			
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO);
			sbSql.append(" = ? ");
			 
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estado); //estado de detalle matricula
			
			int cont=2;
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(++cont, carreraId); //cargo la carrera
			}
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
		
			while(rs.next()){
				retorno.add(transformarResultSetListarXPeriodoXidentificacionXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.por.estado.exception")));
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
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @author Arturo Villafuerte - ajvillafuerte 
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraFull(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,tpgr.");sbSql.append(JdbcConstantes.TIGR_DESCRIPCION);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_GRATUIDAD);sbSql.append(" grt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_GRATUIDAD);sbSql.append(" tpgr ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" grt.");sbSql.append(JdbcConstantes.GRT_FCMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" grt.");sbSql.append(JdbcConstantes.GRT_TIGR_ID);
			sbSql.append(" = ");sbSql.append(" tpgr.");sbSql.append(JdbcConstantes.TIGR_ID);
			
//			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);sbSql.append("=");
//			sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			int contador = 1;
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador= contador+1;
				pstmt.setInt(contador, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador= contador+1;
				pstmt.setInt(contador, carreraId); //cargo la carrera
			}
//			System.out.println(sbSql);
//			System.out.println(personaIdentificacion);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoFull(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraFullNivelacion(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			
			
			
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);sbSql.append("=");
			sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			int contador = 1;
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador= contador+1;
				pstmt.setInt(contador, periodoId); //cargo el periodo academico
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador= contador+1;
				pstmt.setInt(contador, carreraId); //cargo la carrera
			}
//			System.out.println(sbSql);
//			System.out.println(personaIdentificacion);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNivelacionFull(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	
	/**MQ
	 * Realiza la busqueda de las FichasMatricula del estudiante con detalle matrículas y comprobante , carreras de pregrado, periodo de tipo pregrado por personaId y por la carrera 
	 * @param personaId - Usuario a buscar
	 * @param carreraID - Carrera a buscar
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	@Override
	public List<FichaMatriculaDto> ListarTodasXidPersonaXcarrera(int personaId, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);

			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");
			sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");
//			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
//			sbSql.append(" AND ");
//			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ");sbSql.append(CarreraConstantes.TIPO_PREGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			System.out.println(sbSql.toString());
			pstmt.setInt(1, personaId); //cargo la id Usuario
			pstmt.setInt(2, carreraId); //cargo la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetListarTodasXidPersonaXcarrera(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
	
	
	public FichaMatriculaDto buscarFichaMatriculaDto(int fcmtId) throws FichaMatriculaException{
		FichaMatriculaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);			
			sbSql.append(" ,cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
    		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);	sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);	sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fcmtId); //estado de detalle matricula
			rs = pstmt.executeQuery();

			while(rs.next()){
				retorno=transformarResultSetListarXPeriodoXidentificacionXcarrera(rs);
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.por.estado.exception")));
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
	
	public List<FichaMatriculaDto> buscarFichasMatricula(String prsIdentificacion, int periodoId) throws FichaMatriculaNoEncontradoException, FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES NOMBRES, ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_ESTUDIANTE FCES, ");
		sql.append("   FICHA_INSCRIPCION FCIN, ");
		sql.append("   FICHA_MATRICULA FCMT, ");
		sql.append("   CONFIGURACION_CARRERA CNCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   RECORD_ESTUDIANTE RCES ");
		sql.append(" WHERE PRS.PRS_ID               = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID                 = USRO.USR_ID ");
		sql.append(" AND FCES.PRS_ID                = PRS.PRS_ID ");
		sql.append(" AND FCIN.FCIN_ID               = FCES.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID               = CNCR.CNCR_ID ");
		sql.append(" AND CNCR.CRR_ID                  = CRR.CRR_ID ");
		sql.append(" AND CRR.DPN_ID                 = DPN.DPN_ID ");
		sql.append(" AND FCES.FCES_ID               = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_PRAC_ID          =PRAC.PRAC_ID ");
		sql.append(" AND RCES.FCES_ID               = FCES.FCES_ID ");
		sql.append(" AND USRO.ROL_ID                = ");sql.append(RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND RCES.RCES_ESTADO           = ");sql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
		sql.append(" AND FCMT.FCMT_NIVEL_UBICACION <> ");sql.append(NivelConstantes.NIVEL_NIVELACION_VALUE);
		sql.append(" AND PRS.PRS_IDENTIFICACION     = ? ");
		sql.append(" AND FCMT.FCMT_PRAC_ID          = ? ");
		sql.append(" ORDER BY FCMT.FCMT_NIVEL_UBICACION ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, prsIdentificacion);
			pstmt.setInt(2, periodoId); 
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAFichaMatriculaDto(rs));
			}
			
		} catch (SQLException e) {
			retorno = null;
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
		
		if (retorno == null || retorno.size() == 0) {
			retorno = null;
			throw new FichaMatriculaNoEncontradoException("El estudinate solicitado no registra matrícula en el período académico actual.");
		}
		
		return retorno;
	}
	
	
	
	/**MQ
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona, devuelve null sino existe
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> listarXPeriodoXidentificacionXcarreraNull(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
//			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncrarea ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
//			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
//			sbSql.append(" = ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			//sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			//sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion);
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(2, periodoId); 
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setInt(3, carreraId); 
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.por.carrera.exception")));
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
			retorno = null;
			//throw new FichaMatriculaException("El estudinate solicitado aún no registra matrícula en el período académico actual.");
		}
		
		return retorno;
	}
	
	
	
	/**
	 * MQ: 12 dic 2008
	 *  Realiza la busqueda de un FichaMatricula por período académico activo, identifiacion de la persona, por apellido, por carrera de los estudiantes 
	 * que han solicitado retiro fortuito.
	 * @param periodoId - periodoId  String del período académico a buscar
	 * @param listaCarreras - listaCarreras lista de carreras a buscar, si se selecciona todas
	 * @param carreraId - carreraId id de la carrera a buscar
	 * @param personaIdentificacion - personaIdentificacion número de identificación de la persona a buscar
	 * @param personaApellido - personaApellido apellido paterno de la persona a buscar
	 * @return retorna la lista de ficha matriculas - lista de estudiantes matriculados
	 * @throws FichaMatriculaException - FichaMatriculaException excepción general lanzada
	 */
	public List<FichaMatriculaDto> buscarSolicitudRetiroFortuitoXPeriodoXapellidoXidentificacionXcarrera(int periodoId, List<CarreraDto> listaCarreras, int carreraId, String personaIdentificacion, String personaApellido, Integer estadoSolicitud) throws FichaMatriculaException{
		List<FichaMatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_NIVEL_UBICACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_ESTADO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_CONVALIDACION);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_TIPO);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_MODALIDAD);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_VALOR_TOTAL);
			sbSql.append(" ,fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" WHERE ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PLCR_ID);
			sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
			sbSql.append(" AND ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);
			sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);
			sbSql.append(" AND ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);
			sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID);
			sbSql.append(" AND ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
//			sbSql.append(" AND ( ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" not in ");sbSql.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);
//			sbSql.append(" or ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" is null ) ");
			
			sbSql.append(" AND ( ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" not in ");
			sbSql.append(" ( ");sbSql.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);sbSql.append(" , ");
			sbSql.append(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);sbSql.append(" ) ");
			sbSql.append(" or ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_RESPUESTA);sbSql.append(" is null ) ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO_SOLICITUD);sbSql.append(" = ?");
			//sbSql.append(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE);
			
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaCarreras.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}else{//caso para una sola carrera
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			
			//System.out.println(sbSql.toString());
			
			int contador = 1;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			
			contador++;
			pstmt.setInt(contador, estadoSolicitud); //cargo el apellido de la persona
			
			if(periodoId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, periodoId); //cargo el periodo academico
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE.intValue()){
				for (CarreraDto item : listaCarreras) {
					contador++;
					pstmt.setInt(contador, item.getCrrId()); //cargo la carrera
				}
			}else{
				contador++;
				pstmt.setInt(contador, carreraId); //cargo la carrera
			}
			
		
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.apellido.por.identificacion.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.apellido.por.identificacion.por.carrera.exception")));
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
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto con varias entidades
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaMatriculaDto transformarResultSetADto(ResultSet rs) throws SQLException{
		java.sql.Date fecha = null;
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
		if(fecha != null ){
			retorno.setPracFechaIncio(new Date(fecha.getTime()));
		}
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinUsuarioRol(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
		
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinCncr(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
		retorno.setCrrDependencia(rs.getInt(JdbcConstantes.CRR_DPN_ID));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		return retorno;
	}
	
//	private FichaMatriculaDto transformarResultSetADtoAlterno(ResultSet rs) throws SQLException{
//		java.sql.Date fecha = null;
//		FichaMatriculaDto retorno = new FichaMatriculaDto();
//		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
//		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
//		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
//		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
//		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
//		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
//		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
//		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
//		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
//		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
//		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
//		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
//		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
//		if(fecha != null ){
//			retorno.setPracFechaIncio(new Date(fecha.getTime()));
//		}
//		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
//		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
//		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
//		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
//		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
//		retorno.setFcinUsuarioRol(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
//		
//		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
//		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
//		retorno.setFcinCncr(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
//		
//		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
//		retorno.setCrrDependencia(rs.getInt(JdbcConstantes.CRR_DPN_ID));
//		retorno.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
//		return retorno;
//	}
	
	private FichaMatriculaDto transformarResultSetADtoNivelacion(ResultSet rs) throws SQLException{
		java.sql.Date fecha = null;
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
		if(fecha != null ){
			retorno.setPracFechaIncio(new Date(fecha.getTime()));
		}
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinUsuarioRol(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
		
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinCncr(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
		retorno.setCrrDependencia(rs.getInt(JdbcConstantes.CRR_DPN_ID));
		
		retorno.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto con varias entidades
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaMatriculaDto transformarResultSetADtoPosgrado(ResultSet rs) throws SQLException{

		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinUsuarioRol(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDependencia(rs.getInt(JdbcConstantes.CRR_DPN_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto con varias entidades
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaMatriculaDto transformarResultSetListarXidUsuarioXcarrera(ResultSet rs) throws SQLException{
	
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_ID));
		retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
		retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
	
		return retorno;
	}
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto con varias entidades
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaMatriculaDto transformarResultSetListarXPeriodoXidentificacionXcarrera(ResultSet rs) throws SQLException{
		java.sql.Date fecha = null;
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
//		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
//		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
		if(fecha != null ){
			retorno.setPracFechaIncio(new Date(fecha.getTime()));
		}
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
	
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto con varias entidades
	 * @author Arturo Villafuerte - ajvillafuerte 
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaMatriculaDto transformarResultSetADtoFull(ResultSet rs) throws SQLException{
		java.sql.Date fecha = null;
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
		retorno.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setTpgrDescripcion(rs.getString(JdbcConstantes.TIGR_DESCRIPCION));

		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
		if(fecha != null ){
			retorno.setPracFechaIncio(new Date(fecha.getTime()));
		}
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinUsuarioRol(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
		
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinCncr(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
		retorno.setCrrDependencia(rs.getInt(JdbcConstantes.CRR_DPN_ID));
		return retorno;
	}
	
	private FichaMatriculaDto transformarResultSetADtoNivelacionFull(ResultSet rs) throws SQLException{
		java.sql.Date fecha = null;
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtNivelUbicacion(rs.getInt(JdbcConstantes.FCMT_NIVEL_UBICACION));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setFcmtFechaConvalidacion(rs.getDate(JdbcConstantes.FCMT_FECHA_CONVALIDACION));
		retorno.setFcmtTipo(rs.getInt(JdbcConstantes.FCMT_TIPO));
		retorno.setFcmtModalidad(rs.getInt(JdbcConstantes.FCMT_MODALIDAD));
		retorno.setFcmtValorTotal(rs.getBigDecimal(JdbcConstantes.FCMT_VALOR_TOTAL));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));

		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		fecha = rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO);
		if(fecha != null ){
			retorno.setPracFechaIncio(new Date(fecha.getTime()));
		}
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinUsuarioRol(rs.getInt(JdbcConstantes.FCIN_USRO_ID));
		
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinCncr(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
		retorno.setCrrDependencia(rs.getInt(JdbcConstantes.CRR_DPN_ID));
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto con varias entidades
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaMatriculaDto transformarResultSetListarTodasXidPersonaXcarrera(ResultSet rs) throws SQLException{
	
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
		
		return retorno;
	}

	private FichaMatriculaDto transformarResultSetAFichaMatriculaDto (ResultSet rs) throws SQLException{
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setPracId(rs.getInt(1));
		retorno.setPracDescripcion(rs.getString(2));
		retorno.setDpnId(rs.getInt(3));
		retorno.setDpnDescripcion(rs.getString(4));
		retorno.setCrrId(rs.getInt(5));
		retorno.setCrrDescripcion(rs.getString(6));
		retorno.setPrsIdentificacion(rs.getString(7));
		retorno.setPrsPrimerApellido(rs.getString(8));
		retorno.setPrsSegundoApellido(rs.getString(9));
		retorno.setPrsNombres(rs.getString(10));
		retorno.setFcmtNivelUbicacion(rs.getInt(11));
		return retorno;
	}

	
	private FichaMatriculaDto transformarResultSetListarXidPersonaXcarrera (ResultSet rs) throws SQLException{
		
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));
		retorno.setFcmtEstado(rs.getInt(JdbcConstantes.FCMT_ESTADO));
		retorno.setDtmtId(rs.getInt(JdbcConstantes.DTMT_ID));
		retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
		retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlCodigo(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));
		retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesEstadoValue(rs.getInt(JdbcConstantes.RCES_ESTADO));
	
		return retorno;
	}
	
}
