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
   
 ARCHIVO:     RecordEstudianteDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla RecordEstudianteDto.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-03-2017			David Arellano					       Emisión Inicial
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.FichaEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB RecordEstudianteDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla RecordEstudianteDto.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class RecordEstudianteDtoServicioJdbcImpl implements RecordEstudianteDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	public List<MateriaDto> buscarRecordEstudiantePorIdentificacionPeriodoActivo(String identificacion) throws RecordEstudianteDtoNoEncontradoException, RecordEstudianteDtoException{
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		List<MateriaDto> retorno = null;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ");
			sql.append(" fcmt.FCMT_PRAC_ID ");
			sql.append(" ,PRAC.PRAC_DESCRIPCION ");
			sql.append(" ,CRR.CRR_ID ");
			sql.append(" ,CRR.CRR_DESCRIPCION ");
			sql.append(" ,fcmt.FCMT_FECHA_MATRICULA ");
			sql.append(" ,mtr.MTR_ID ");
			sql.append(" ,mtr.MTR_DESCRIPCION ");
			sql.append(" ,dtmt.DTMT_ID ");
			sql.append(" ,dtmt.DTMT_NUMERO ");
			sql.append(" ,rces.RCES_ID ");
			sql.append(" ,rces.MLCRPR_ID ");
			sql.append(" ,rces.RCES_ESTADO  ");
			sql.append(" ,prs.PRS_ID ");
			sql.append(" ,prs.PRS_IDENTIFICACION ");
			sql.append(" ,PRS.PRS_PRIMER_APELLIDO ");
			sql.append(" ,PRS.PRS_SEGUNDO_APELLIDO ");
			sql.append(" ,PRS.PRS_NOMBRES ");
			sql.append(" from  ");
			sql.append(" PERSONA PRS ");
			sql.append(" , USUARIO USR ");
			sql.append(" , FICHA_ESTUDIANTE FCES ");
			sql.append(" , RECORD_ESTUDIANTE RCES ");
			sql.append(" , MALLA_CURRICULAR_PARALELO MLCRPR ");
			sql.append(" , FICHA_MATRICULA fcmt ");
			sql.append(" , PERIODO_ACADEMICO PRAC ");
			sql.append(" , COMPROBANTE_PAGO CMPG ");
			sql.append(" , DETALLE_MATRICULA DTMT ");
			sql.append(" , FICHA_INSCRIPCION fcin ");
			sql.append(" , CONFIGURACION_CARRERA CNCR ");
			sql.append(" , CARRERA CRR ");
			sql.append(" , DEPENDENCIA DPN ");
			sql.append(" , MALLA_CURRICULAR_MATERIA MLCRMT ");
			sql.append(" , MATERIA MTR ");
			sql.append(" , NIVEL NVL ");
			sql.append(" , PARALELO PRL ");
			sql.append(" WHERE fCES.FCES_ID = fcmt.FCES_ID ");
			sql.append(" AND fcmt.FCMT_ID = CMPG.FCMT_ID ");
			sql.append(" AND fcmt.FCMT_PRAC_ID = PRAC.PRAC_ID ");
			sql.append(" AND CMPG.CMPA_ID = DTMT.CMPA_ID ");
			sql.append(" AND PRS.PRS_ID = FCES.PRS_ID ");
			sql.append(" AND USR.PRS_ID = PRS.PRS_ID ");
			sql.append(" AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID  ");
			sql.append(" AND MLCRPR.MLCRPR_ID = DTMT.MLCRPR_ID ");
			sql.append(" AND MLCRPR.PRL_ID = PRL.PRL_ID  ");
			sql.append(" AND MLCRMT.NVL_ID = NVL.NVL_ID ");
			sql.append(" AND fCES.FCES_ID = RCES.FCES_ID ");
			sql.append(" AND fcin.FCIN_ID = FCES.FCIN_ID ");
			sql.append(" AND fcin.CNCR_ID = CNCR.CNCR_ID ");
			sql.append(" AND CRR.CRR_ID = CNCR.CRR_ID ");
			sql.append(" AND CRR.DPN_ID = DPN.DPN_ID ");
			sql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID  ");
			sql.append(" AND MTR.MTR_ID = MLCRMT.MTR_ID  ");
			sql.append(" AND prs.PRS_IDENTIFICACION = ? ");
			sql.append(" AND fcmt.FCMT_PRAC_ID =  ");
			sql.append(" (SELECT PRAC_ID FROM PERIODO_ACADEMICO WHERE PRAC_ESTADO in (0) AND PRAC_TIPO = 0) ");
			sql.append(" order by MLCRMT.NVL_ID ASC ");


			con = ds.getConnection();

			if (con != null) {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, GeneralesUtilidades.quitarEspaciosEnBlanco(identificacion).toUpperCase());
				rs = pstmt.executeQuery();

				retorno = new ArrayList<>();
				while (rs.next()) {
					retorno.add(transformarResultSetAMateriaDto(rs));
				}
			}
			

		} catch (Exception e) {
			retorno = null;
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
				 e.printStackTrace();
			}
		}
		
		if (retorno.size() == 0) {
			retorno = null;
			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception")));
		}
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacion(String personaIdentificacion) throws RecordEstudianteDtoException{
		List<RecordEstudianteDto> retorno = null;
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
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<RecordEstudianteDto>();
		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona y la carrera seleccionada menos las asignaturas tomadas en el periodo activo
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - Id de la carrera a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXcarrera(String personaIdentificacion, int carreraId) throws RecordEstudianteDtoException{
		List<RecordEstudianteDto> retorno = null;
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
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
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
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" not in ( ");
			
			sbSql.append(" SELECT ");
			sbSql.append(" pracaux.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" pracaux ");
			sbSql.append(" WHERE ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<RecordEstudianteDto>();
		}	
		
		return retorno;
	}
	
	
	/**
	 * MQ:
	 * Realiza la busqueda del record academico por la identificacion de la persona y la carrera seleccionada Asisgnaturas en todos los estados.
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - Id de la carrera a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXcarreraTodas(String personaIdentificacion, int carreraId) throws RecordEstudianteDtoException{
		List<RecordEstudianteDto> retorno = null;
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
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);	
	
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
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
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" not in ");sbSql.append(" (");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE); ;sbSql.append(")");
						
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<RecordEstudianteDto>();
		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona y la carrera seleccionada
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - Id de la carrera a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXpracActivoXprcacEnCierre(String personaIdentificacion) throws RecordEstudianteDtoException{
		List<RecordEstudianteDto> retorno = null;
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
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" ,mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
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
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" <> ");sbSql.append(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
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
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);
			sbSql.append(" )) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
//			System.out.println(sbSql);
//			System.out.println(personaIdentificacion);
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<RecordEstudianteDto>();
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona y el estado del record
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoid - id del estado a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXestado(String personaIdentificacion, int estadoid) throws RecordEstudianteDtoException{
		List<RecordEstudianteDto> retorno = null;
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
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoid);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatriculaDto.buscar.por.periodo.por.identificacion.exception")));
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
			retorno = new ArrayList<RecordEstudianteDto>();
		}	
		
		return retorno;
	}

	
	
	/**
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public RecordEstudianteDto buscarXFcesIdXMlcrprXPracActivo(int fcesId, int mlcrprId) throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException{
		RecordEstudianteDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
					
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ROWNUM = 1 ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, fcesId); //cargo el id de la ficha_estudiante
			pstmt.setInt(2, mlcrprId); //cargo el id de malla_curricular_paralelo
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarXFcesIdXMlcrprXPracActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception",fcesId,mlcrprId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public RecordEstudianteDto buscarXRcesId(int rcesId) throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException{
		RecordEstudianteDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
					
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ROWNUM = 1 ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, rcesId); //cargo el id de la ficha_estudiante
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarXFcesIdXMlcrprXPracActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public RecordEstudianteDto buscarXFcesIdXMlcrprXPracActivoCambio(int fcesId, int mlcrprId) throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException{
		RecordEstudianteDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
					
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, fcesId); //cargo el id de la ficha_estudiante
			pstmt.setInt(2, mlcrprId); //cargo el id de malla_curricular_paralelo
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarXFcesIdXMlcrprXPracActivo(rs);
			}else{
				retorno = null;
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
		} catch (SQLException e) {
			
			//TODO GABRIEL HACER EL MENSAJE
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
		
//		if(retorno == null){
//			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception",fcesId,mlcrprId)));
//		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public RecordEstudianteDto buscarXFcesIdXMlcrprXPracEnCierreCambio(int fcesId, int mlcrprId) throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException{
		RecordEstudianteDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
			
					
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, fcesId); //cargo el id de la ficha_estudiante
			pstmt.setInt(2, mlcrprId); //cargo el id de malla_curricular_paralelo
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarXFcesIdXMlcrprXPracActivo(rs);
			}else{
				retorno = null;
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
		} catch (SQLException e) {
			
			//TODO GABRIEL HACER EL MENSAJE
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			//TODO GABRIEL HACER EL MENSAJE
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
		
//		if(retorno == null){
//			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception",fcesId,mlcrprId)));
//		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<RecordEstudianteDto> listarRecordEstudianteRecuperacion() {
		List<RecordEstudianteDto> retorno = new ArrayList<RecordEstudianteDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_ID);
			sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			
					
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" clf.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" in ( ");
			
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" = ");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
//			System.out.println(sbSql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoRecordCalificacion(rs));
			}
		} catch (NoResultException e) {
		} catch (SQLException e) {
		} catch (Exception e) {
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
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private RecordEstudianteDto transformarResultSetADto(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setRcesObservacion(rs.getString(JdbcConstantes.RCES_OBSERVACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setMlcrmtMallaCurricularMateria(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrmtMtrId(rs.getInt(JdbcConstantes.MLCRMT_MTR_ID));
		try {
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		} catch (Exception e) {
		}
		return retorno;
	}
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private RecordEstudianteDto transformarResultSetADtobuscarXidentificacionXcarreraTodas(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		retorno.setRcesObservacion(rs.getString(JdbcConstantes.RCES_OBSERVACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setMlcrmtMallaCurricularMateria(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMlcrmtMtrId(rs.getInt(JdbcConstantes.MLCRMT_MTR_ID));
	
		return retorno;
	}
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private RecordEstudianteDto transformarResultSetADtoBuscarXFcesIdXMlcrprXPracActivo(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(JdbcConstantes.MLCRPR_ID)); 
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
		return retorno;
	}

	
	private MateriaDto transformarResultSetAMateriaDto(ResultSet rs) throws SQLException {
		MateriaDto retorno = new MateriaDto();
		PersonaDto persona = new PersonaDto();
		
		retorno.setPracId(rs.getInt(1));
		retorno.setPracDescripcion(rs.getString(2));
		retorno.setCrrId(rs.getInt(3));
		retorno.setCrrDescripcion(rs.getString(4));
		retorno.setFcmtFechaMatricula(rs.getDate(5));
		retorno.setMtrId(rs.getInt(6));
		retorno.setMtrDescripcion(rs.getString(7));
		retorno.setDtmtId(rs.getInt(8));
		retorno.setDtmtNumeroMatricula(rs.getString(9));
		retorno.setRcesId(rs.getInt(10));
		retorno.setMlcrprId(rs.getInt(11));
		retorno.setRcesEstado(rs.getInt(12));
		persona.setPrsId(rs.getInt(13));
		persona.setPrsIdentificacion(rs.getString(14));
		persona.setPrsPrimerApellido(rs.getString(15));
		persona.setPrsSegundoApellido(rs.getString(16));
		persona.setPrsNombres(rs.getString(17));
		retorno.setMtrPersonaDto(persona);
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetADtoRecordCalificacion(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
		retorno.setClfParamRecuperacion1(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION1));
		retorno.setClfParamRecuperacion2(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION2));
		return retorno;
	}
	
	
	@Override
	public List<FichaEstudianteDto> buscarRecordEstudianteDetalleMatricula(String prsIdentificacion, Integer pracId)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException {
		List<FichaEstudianteDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(pracId);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, prsIdentificacion);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaEstudianteDto>();
			while(rs.next()){
				FichaEstudianteDto fcesAgregar = new FichaEstudianteDto();
				fcesAgregar.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
				fcesAgregar.setRcesMlcrprId(rs.getInt(JdbcConstantes.RCES_MLCRPR_ID));
				fcesAgregar.setFcesFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
				fcesAgregar.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
				
				retorno.add(fcesAgregar);
			}
			
		}catch(SQLException e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.sql.exception")));
		}catch(Exception e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.exception")));
		}finally{
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
			throw new  FichaEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.no.result.exception")));
		}
		
		return retorno;
		
		
	}
	@Override
	public Integer buscarDetalleMatricula(Integer mlcrprId, Integer fcesId, Integer pracId)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException {
		 Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try{
			con=ds.getConnection();
					StringBuilder sbSql1 = new StringBuilder();
					sbSql1.append(" SELECT ");
					sbSql1.append(" dtmt.");sbSql1.append(JdbcConstantes.DTMT_ID);
					sbSql1.append(" FROM ");sbSql1.append(JdbcConstantes.TABLA_PERSONA);sbSql1.append(" prs ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql1.append(" fces ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql1.append(" fcmt ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql1.append(" cmpa ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql1.append(" dtmt ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql1.append(" mlcrpr ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_PARALELO);sbSql1.append(" prl ");
					sbSql1.append(" , ");sbSql1.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql1.append(" prac ");
					sbSql1.append(" WHERE ");
					sbSql1.append(" fces.");sbSql1.append(JdbcConstantes.FCES_PRS_ID);
					sbSql1.append(" = prs.");sbSql1.append(JdbcConstantes.PRS_ID);
					sbSql1.append(" AND fces.");sbSql1.append(JdbcConstantes.FCES_ID);
					sbSql1.append(" = fcmt.");sbSql1.append(JdbcConstantes.FCMT_FCES_ID);
					sbSql1.append(" AND fcmt.");sbSql1.append(JdbcConstantes.FCMT_ID);
					sbSql1.append(" = cmpa.");sbSql1.append(JdbcConstantes.CMPA_FCMT_ID);
					sbSql1.append(" AND cmpa.");sbSql1.append(JdbcConstantes.CMPA_ID);
					sbSql1.append(" = dtmt.");sbSql1.append(JdbcConstantes.DTMT_CMPA_ID);
					sbSql1.append(" AND dtmt.");sbSql1.append(JdbcConstantes.DTMT_MLCRPR_ID);
					sbSql1.append(" = mlcrpr.");sbSql1.append(JdbcConstantes.MLCRPR_ID);
					sbSql1.append(" AND mlcrpr.");sbSql1.append(JdbcConstantes.MLCRPR_PRL_ID);
					sbSql1.append(" = prl.");sbSql1.append(JdbcConstantes.PRL_ID);
					sbSql1.append(" AND prac.");sbSql1.append(JdbcConstantes.PRAC_ID);
					sbSql1.append(" = ");sbSql1.append(pracId);
					sbSql1.append(" AND prl.");sbSql1.append(JdbcConstantes.PRL_PRAC_ID);
					sbSql1.append(" = prac.");sbSql1.append(JdbcConstantes.PRAC_ID);
					sbSql1.append(" AND mlcrpr.");sbSql1.append(JdbcConstantes.MLCRPR_ID);
					sbSql1.append(" = ");sbSql1.append(mlcrprId);
					sbSql1.append(" AND fces.");sbSql1.append(JdbcConstantes.FCES_ID);
					sbSql1.append(" =  ");sbSql1.append(fcesId);
					pstmt = con.prepareStatement(sbSql1.toString());
					rs = pstmt.executeQuery();
					while(rs.next()){
						retorno=(rs.getInt(JdbcConstantes.DTMT_ID));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					try {
						if(rs!=null){
							rs.close();
						}
						if(pstmt!=null){
							pstmt.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (Exception e2) {
					}
					
				}
				
			return retorno;
		
	}
	
	@Override
	public void actualizarRecordEstudianteDetalleMatricula(Integer rcesId, Integer dtmtId, Integer mlcrprIdNuevo)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		Connection con = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" UPDATE ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);
			sbSql.append(" SET ");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mlcrprIdNuevo);
			pstmt.setInt(2, rcesId);
			pstmt.executeUpdate();
			sbSql = new StringBuilder();
			sbSql.append(" UPDATE ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);
			sbSql.append(" SET ");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.DTMT_ID);
			sbSql.append(" = ? ");
			pstmt1 = con.prepareStatement(sbSql.toString());
			pstmt1.setInt(1, mlcrprIdNuevo);
			pstmt1.setInt(2, dtmtId);
			pstmt1.executeUpdate();
		}catch(SQLException e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.sql.exception")));
		}catch(Exception e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.exception")));
		}finally{
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				}	
		}
		
		
		
	}
	
	@Override
	public void eliminarRecordEstudianteDetalleMatricula(Integer rcesId, Integer dtmtId)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		Connection con = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" DELETE FROM ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, rcesId);
			pstmt.executeUpdate();
			sbSql = new StringBuilder();
			sbSql.append(" DELETE FROM ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.DTMT_ID);
			sbSql.append(" = ? ");
			pstmt1 = con.prepareStatement(sbSql.toString());
			pstmt1.setInt(1, dtmtId);
			pstmt1.executeUpdate();
		}catch(SQLException e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.sql.exception")));
		}catch(Exception e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.exception")));
		}finally{
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				}	
		}
		
		
		
	}
	
	@Override
	public void insertarRecordEstudianteDetalleMatricula(Integer rcesId, Integer dtmtId, Integer mlcrprId, Integer numMatricula)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		Connection con = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" INSERT INTO ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);
			sbSql.append(" (RCES_ID,FCES_ID,MLCRPR_ID,RCES_ESTADO) ");
			sbSql.append(" VALUES ((SELECT MAX(RCES_ID)+1 FROM RECORD_ESTUDIANTE),(SELECT FCES_ID FROM RECORD_ESTUDIANTE WHERE RCES_ID=");
			sbSql.append(rcesId);sbSql.append("),");sbSql.append(mlcrprId);sbSql.append(",0)");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.executeUpdate();
			
			sbSql = new StringBuilder();
			sbSql.append(" INSERT INTO ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);
			sbSql.append(" (DTMT_ID,CMPA_ID,MLCRPR_ID,DTMT_NUMERO, DTMT_ESTADO) ");
			sbSql.append(" VALUES ((SELECT MAX(DTMT_ID)+1 FROM DETALLE_MATRICULA),(SELECT CMPA_ID FROM DETALLE_MATRICULA WHERE DTMT_ID=");
			sbSql.append(dtmtId);sbSql.append("),");sbSql.append(mlcrprId);sbSql.append(",");sbSql.append(numMatricula);sbSql.append(",0)");
			pstmt1 = con.prepareStatement(sbSql.toString());
			pstmt1.executeUpdate();
			
		}catch(SQLException e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.sql.exception")));
		}catch(Exception e){
			throw new FichaEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteDtoServicioJdbc.buscarXPersonaid.FichaEstudiante.exception")));
		}finally{
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				}	
		}
		
		
		
	}
}
