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
   
 ARCHIVO:     HorarioAcademicoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla HorarioAcademicoDto.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
21-SEPT-2017		Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB HorarioAcademicoDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla HorarioAcademicoDto.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class HorarioAcademicoDtoServicioJdbcImpl implements HorarioAcademicoDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public List<HorarioAcademicoDto> buscarHorarioModulo(int paraleloId, int mlcrmtId, int periodoId)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException  {
		List<HorarioAcademicoDto> retorno = null;
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
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION , ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   HRAC.HRAC_DIA, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   ALA.ALA_ID, ");
		sql.append("   ALA.ALA_CODIGO, ");
		sql.append("   ALA.ALA_DESCRIPCION ");
		sql.append(" FROM MATERIA MTR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PARALELO PRL , ");
		sql.append("   CARRERA CRR , ");
		sql.append("   DEPENDENCIA DPN, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   HORARIO_ACADEMICO HRAC, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   HORA_CLASE HOCL, ");
		sql.append("   AULA ALA ");
		sql.append(" WHERE MTR.MTR_ID     = MLCRMT.MTR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append(" AND PRL.PRL_ID       = MLCRPR.PRL_ID ");
		sql.append(" AND CRR.CRR_ID       = PRL.CRR_ID ");
		sql.append(" AND DPN.DPN_ID       = CRR.DPN_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID = HRAC.MLCRPR_ID ");
		sql.append(" AND HOCLAU.HOCLAL_ID = HRAC.HOCLAL_ID ");
		sql.append(" AND ALA.ALA_ID       = HOCLAU.ALA_ID ");
		sql.append(" AND HOCL.HOCL_ID     = HOCLAU.HOCL_ID ");
		sql.append(" AND PRAC.PRAC_ID     = ? ");
		sql.append(" AND MLCRMT.MLCRMT_ID = ? ");
		sql.append(" AND PRL.PRL_ID       = ? ");
		sql.append(" ORDER BY HRAC.HRAC_DIA,  HOCL.HOCL_HORA_INICIO ");

		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, mlcrmtId);
			pstmt.setInt(3, paraleloId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAhorarioAcademicoModulo(rs));
			}
			
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
			throw new HorarioAcademicoDtoNoEncontradoException("No hay resultados, intente con otros parámetros de búsqueda.");
		}	
		
		return retorno;
	}

	public List<HorarioAcademicoDto> buscarHorarioDocente(String identificacion) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT DISTINCT prac.prac_descripcion periodo, ");
			sql.append("   dpn.dpn_descripcion FACULTAD, ");
			sql.append("   NVL(SUBSTR(crr.crr_detalle, 0, INSTR(crr.crr_detalle, '(')-1), crr.crr_detalle) CARRERA, ");
			sql.append("   prs.prs_identificacion CEDULA_DOCENTE, ");
			sql.append("   prs.prs_primer_apellido ||' ' ||prs.prs_segundo_apellido || ' ' ||prs.prs_nombres DOCENTE, ");
			sql.append("   mtr.mtr_id CODIGO_MATERIA, ");
			sql.append("   mtr.mtr_descripcion MATERIA, ");
			sql.append("   hrac.hrac_dia, ");
			sql.append("   nvl.nvl_numeral CURSO, ");
			sql.append("   prl.prl_descripcion PARALELO, ");
			sql.append("   SUBSTR(hrcl.hocl_descripcion,1,5) hora_inicio, ");
			sql.append("   SUBSTR(hrcl.hocl_descripcion,8,6) hora_fin, ");
			sql.append("   prac.prac_id, ");
			sql.append("   ala.ala_descripcion ");
			sql.append(" FROM persona prs, ");
			sql.append("   ficha_docente fcdc, ");
			sql.append("   detalle_puesto dtps, ");
			sql.append("   carga_horaria crhr, ");
			sql.append("   periodo_academico prac, ");
			sql.append("   tiempo_dedicacion tmdd, ");
			sql.append("   puesto pst, ");
			sql.append("   malla_curricular_paralelo mlcrpr, ");
			sql.append("   paralelo prl, ");
			sql.append("   malla_curricular_materia mlcrmt, ");
			sql.append("   materia mtr, ");
			sql.append("   carrera crr, ");
			sql.append("   dependencia dpn, ");
			sql.append("   horario_academico hrac, ");
			sql.append("   hora_clase hrcl, ");
			sql.append("   hora_clase_aula hrcla, ");
			sql.append("   nivel NVL, ");
			sql.append("   aula ala ");
			sql.append(" WHERE prs.prs_id     = fcdc.prs_id ");
			sql.append(" AND dtps.fcdc_id     = fcdc.fcdc_id ");
			sql.append(" AND crhr.dtps_id     = dtps.dtps_id ");
			sql.append(" AND crhr.prac_id     = prac.prac_id ");
			sql.append(" AND dtps.pst_id      = pst.pst_id ");
			sql.append(" AND pst.tmdd_id      = tmdd.tmdd_id ");
			sql.append(" AND crhr.mlcrpr_id   = mlcrpr.mlcrpr_id ");
			sql.append(" AND mlcrpr.prl_id    = prl.prl_id ");
			sql.append(" AND mlcrpr.mlcrmt_id = mlcrmt.mlcrmt_id ");
			sql.append(" AND mlcrmt.mtr_id    = mtr.mtr_id ");
			sql.append(" AND mtr.crr_id       = crr.crr_id ");
			sql.append(" AND crr.dpn_id       = dpn.dpn_id ");
			sql.append(" AND mlcrpr.mlcrpr_id = hrac.mlcrpr_id ");
			sql.append(" AND hrac.hoclal_id   = hrcla.hoclal_id ");
			sql.append(" AND hrcla.hocl_id    = hrcl.hocl_id ");
			sql.append(" AND nvl.nvl_id       = mlcrmt.nvl_id ");
			sql.append(" AND ala.ala_id       = hrcla.ala_id ");
			sql.append(" AND crhr.crhr_estado = 0 ");
			sql.append(" AND crhr_estado_eliminacion = 1 ");
			sql.append(" AND prs.PRS_IDENTIFICACION = ? ");
			sql.append(" GROUP BY prac.prac_descripcion , ");
			sql.append("   dpn.dpn_descripcion , ");
			sql.append("   crr.crr_detalle, ");
			sql.append("   prs.prs_identificacion , ");
			sql.append("   prs.prs_primer_apellido , ");
			sql.append("   prs.prs_segundo_apellido, ");
			sql.append("   prs.prs_nombres , ");
			sql.append("   tmdd.tmdd_descripcion, ");
			sql.append("   pst.pst_denominacion, ");
			sql.append("   pst.pst_nivel_rango_gradual, ");
			sql.append("   mtr.mtr_id , ");
			sql.append("   mtr.mtr_descripcion , ");
			sql.append("   mtr.mtr_creditos, ");
			sql.append("   prl.prl_descripcion, ");
			sql.append("   hrac.hrac_descripcion, ");
			sql.append("   hrcl.hocl_descripcion, ");
			sql.append("   hrac.hrac_dia, ");
			sql.append("   nvl.nvl_numeral, ");
			sql.append("   prl.prl_descripcion, ");
			sql.append("   prac.prac_id, ");
			sql.append("   ala.ala_descripcion ");
			sql.append("   ORDER BY 1 ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion.toUpperCase());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAhorarioAcademicoDocenteDto(rs));
			}
			
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
			throw new HorarioAcademicoDtoNoEncontradoException("No hay resultados, intente con otros parámetros de búsqueda.");
		}	
		
		return retorno;
	}	
	
	public List<HorarioAcademicoDto> buscarHorarioEstudiante(int crrId, int pracId, String identificacion) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
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
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION , ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   HRAC.HRAC_DIA, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   ALA.ALA_ID, ");
		sql.append("   ALA.ALA_CODIGO, ");
		sql.append("   ALA.ALA_DESCRIPCION ");
		sql.append(" FROM PERSONA PRS  ");
		sql.append(" INNER JOIN USUARIO USR ON USR.PRS_ID = PRS.PRS_ID ");
		sql.append(" INNER JOIN USUARIO_ROL USRO ON USRO.USR_ID = USR.USR_ID ");
		sql.append(" INNER JOIN FICHA_INSCRIPCION FCIN ON FCIN.USRO_ID = USRO.USRO_ID ");
		sql.append(" INNER JOIN FICHA_ESTUDIANTE FCES ON FCES.PRS_ID   = PRS.PRS_ID AND FCES.FCIN_ID = FCIN.FCIN_ID ");
		sql.append(" INNER JOIN RECORD_ESTUDIANTE RCES ON RCES.FCES_ID = FCES.FCES_ID ");
		sql.append(" INNER JOIN FICHA_MATRICULA FCMT ON FCMT.FCES_ID = FCES.FCES_ID ");
		sql.append(" INNER JOIN COMPROBANTE_PAGO CMPG ON FCMT.FCMT_ID = CMPG.FCMT_ID ");
		sql.append(" INNER JOIN DETALLE_MATRICULA DTMT ON CMPG.CMPA_ID = DTMT.CMPA_ID ");
		sql.append(" INNER JOIN CONFIGURACION_CARRERA CNCR ON CNCR.CNCR_ID = FCIN.CNCR_ID ");
		sql.append(" INNER JOIN CARRERA CRR ON CRR.CRR_ID = CNCR.CRR_ID ");
		sql.append(" INNER JOIN DEPENDENCIA DPN ON DPN.DPN_ID = CRR.DPN_ID ");
		sql.append(" INNER JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON MLCRPR.MLCRPR_ID  = RCES.MLCRPR_ID AND MLCRPR.MLCRPR_ID = DTMT.MLCRPR_ID ");
		sql.append(" INNER JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append(" INNER JOIN MATERIA MTR ON MLCRMT.MTR_ID = MTR.MTR_ID AND MTR.CRR_ID   = CRR.CRR_ID ");
		sql.append(" INNER JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = FCMT.FCMT_PRAC_ID ");
		sql.append(" LEFT JOIN HORARIO_ACADEMICO HRAC ON HRAC.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" LEFT JOIN HORA_CLASE_AULA HOCLAU ON HOCLAU.HOCLAL_ID = HRAC.HOCLAL_ID ");
		sql.append(" LEFT JOIN HORA_CLASE HOCL ON HOCL.HOCL_ID = HOCLAU.HOCL_ID ");
		sql.append(" LEFT JOIN AULA ALA ON ALA.ALA_ID = HOCLAU.ALA_ID ");
		sql.append(" LEFT JOIN PARALELO PRL ON PRL.PRL_ID              = MLCRPR.PRL_ID ");
		sql.append(" WHERE CRR.CRR_ID           = ? ");
		sql.append(" AND FCMT.FCMT_PRAC_ID      = ? ");
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" ORDER BY HRAC.HRAC_DIA, HOCL.HOCL_HORA_INICIO ");

		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, crrId);
			pstmt.setInt(2, pracId);
			pstmt.setString(3, identificacion);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAhorarioAcademicoEstudianteDto(rs));
			}
			
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
			throw new HorarioAcademicoDtoNoEncontradoException("No hay resultados, intente con otros parámetros de búsqueda.");
		}	
		
		return retorno;
	}	
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioPpracIdPprlIdPmtrId(int pracId, int prlId, int mtrId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, prlId);
			pstmt.setInt(3, mtrId);
			
		
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXprlId(int pracId, int prlId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, prlId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoCupo(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXListMlcrpr(int pracId, List<EstudianteJdbcDto> listaMaterias) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			
			if(listaMaterias.size() > 0){
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaMaterias.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaMaterias.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");
			}
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			
			int contador = 1;
			if(listaMaterias.size() > 0){ // caso para setear la lista de carreras
				for (EstudianteJdbcDto item : listaMaterias) {
					contador++;
					pstmt.setInt(contador, item.getMlcrprId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoCupo(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXListMlcrprComp(int pracId, List<DocenteJdbcDto> listaMaterias) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			
			if(listaMaterias.size() > 0){
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaMaterias.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaMaterias.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");
			}
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			
			int contador = 1;
			if(listaMaterias.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listaMaterias) {
					contador++;
					pstmt.setInt(contador, item.getMlcrprId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoComp(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXcrrId(int pracId, int crrId, List<CarreraDto> listCarreras, List<MallaCurricularParaleloDto> listaMallaCurricularParalelo) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			retorno = new ArrayList<HorarioAcademicoDto>();
			for (MallaCurricularParaleloDto item : listaMallaCurricularParalelo) {
				
				StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT * FROM ( ");
			
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
//			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);sbSql.append(" not in ( ");sbSql.append(TipoMateriaConstantes.TIPO_MODULO_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
			
//			if(crrId == GeneralesConstantes.APP_ID_BASE){
//				sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" in ( ");
//				for (int i = 0; i < listCarreras.size(); i++) {
//					sbSql.append(" ? ");
//					if(i != listCarreras.size() -1) {
//			         sbSql.append(","); 
//			        }
//				}
//				sbSql.append(" ) ");
//			}else{
//				sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
//			}
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			
			sbSql.append(" ), ( ");
			
			sbSql.append(" SELECT COUNT ( ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" ) ");sbSql.append(" as matriculados");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" WHERE ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
//			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" = ");sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			
			sbSql.append(" AND rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" not in ( ");sbSql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
									   sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE);
									   sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE);
									   sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);sbSql.append(" not in ( ");sbSql.append(TipoMateriaConstantes.TIPO_MODULO_VALUE);sbSql.append(" ) ");
									   
//			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ) ");
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
//			pstmt.setInt(1, pracId);
//
//			int contador = 1;
//			if(crrId == GeneralesConstantes.APP_ID_BASE){ // caso para setear la lista de carreras
//				for (CarreraDto item : listCarreras) {
//					contador++;
//					pstmt.setInt(contador, item.getCrrId()); //cargo las carreras ids
//				}
//			}else{//caso para setear una sola carrrera
//				contador++;
//				pstmt.setInt(contador, crrId); //cargo el id de carrera
//			}
			
			pstmt.setInt(1, item.getMlcrprId()); //cargo el id de carrera
			pstmt.setInt(2, item.getMlcrprId()); //cargo el id de carrera
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtoRep(rs));
			}
		}
			
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXprlIdXmlcrprId(int pracId, int mlcrprId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, mlcrprId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas los horarios academicos
	 * @param pracId - pracId id de la malla curricular 
	 * @param dtpsId - dtpsId id del detalle puesto del docente
	 * @return Lista todas las mallas curricularers materia por periodo, detalle puesto
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXPracIdXDtpsId(int pracId, int dtpsId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn , ");
			
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, dtpsId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoNoEncontradoException("No se encontro registros");
//		}	
		return retorno;
	}
	
	
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados por paralelo Id
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId , id de la materia
	 * @return Lista de paralelos encontrados
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioPprlIdPMtrId( int prlId, int mtrId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, prlId);
			pstmt.setInt(1, mtrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarlistarHorarioPprlIdResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.por.prlId.mtrId.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.por.prlId.mtrId.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
			throw new HorarioAcademicoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.por.prlId.mtrId.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @param hracDia - hracDia dia en el que hay que consultar
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioPpracIdPprlIdPmtrIdPhracDia(int pracId, int prlId, int mtrId, int hracDia) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_NUMERAL);
			
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CUPO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_INICIO);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_HORA_FIN);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala, ");
			
			sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);

			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND edf.");sbSql.append(JdbcConstantes.EDF_ID);sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_EDF_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_DPN_ID);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
			sbSql.append(" ,hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" ,ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, prlId);
//			pstmt.setInt(3, mtrId);
			pstmt.setInt(3, hracDia);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			e.getStackTrace();
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.horadia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.horadia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
//			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.horadia.no.result.exception")));
		}	
		return retorno;
	}
	

	
	/**
	 * Listar horarios por parametros de busqueda
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param prlId .- Paralelo id a buscar
	 * @param mlcrmtId .- MallaCurricularMateria Id a buscar
	 * @param pracId .- PeriodoAcademico id a buscar  
	 * @return Lista de horarios
	 * @throws HorarioAcademicoDtoException
	 * @throws HorarioAcademicoDtoNoEncontradoException
	 */
	public List<HorarioAcademicoDto> buscarHorarioFull(int prlId, int mlcrmtId, int pracId)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" ");
			sql.append(" SELECT PRL.PRL_ID, PRAC.PRAC_ID, MLCRMT.MTR_ID, MLCRMT.MLCRMT_ID, HRAC.HRAC_DIA, ");
			sql.append(" substr(hrcl.hocl_descripcion,1,5) HOCL_HORA_INICIO, substr(hrcl.hocl_descripcion,8,6) HOCL_HORA_FIN  ");
			sql.append(" FROM MALLA_CURRICULAR_MATERIA MLCRMT, ");
			sql.append(" MALLA_CURRICULAR_PARALELO MLCRPR, ");
			sql.append(" PARALELO PRL, ");
			sql.append(" HORARIO_ACADEMICO HRAC, ");
			sql.append(" PERIODO_ACADEMICO PRAC, ");
			sql.append(" HORA_CLASE_AULA HOCLAL, ");
			sql.append(" HORA_CLASE HRCL ");
			sql.append(" WHERE MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
			sql.append(" AND MLCRPR.MLCRPR_ID = HRAC.MLCRPR_ID ");
			sql.append(" AND MLCRPR.PRL_ID = PRL.PRL_ID ");
			sql.append(" AND PRAC.PRAC_ID = PRL.PRAC_ID ");
			sql.append(" AND HRAC.HOCLAL_ID = HOCLAL.HOCLAL_ID ");
			sql.append(" AND HRCL.HOCL_ID = HOCLAL.HOCL_ID ");
			sql.append(" AND PRAC.PRAC_ID = ? ");
			sql.append(" AND MLCRMT.MLCRMT_ID = ? "); 
			sql.append(" AND PRL.PRL_ID = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, mlcrmtId);
			pstmt.setInt(3, prlId);
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HorarioAcademicoDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetAhorarioAcademicoDtoFull(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
			throw new HorarioAcademicoDtoNoEncontradoException("No hay resultados, intente con diferentes parámetros de búsqueda.");
		}	
		
		return retorno;
	}	
	
	
	public List<HorarioAcademicoDto> buscarParaleloCompartido(int mlcrprId)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException {
		List<HorarioAcademicoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  ");
		sql.append(" MLCRMT.MLCRMT_ID, ");
		sql.append(" MLCRMT.MLCR_ID, ");
		sql.append(" MLCRMT.MTR_ID, ");
		sql.append(" MLCRMT.MLCRMT_ESTADO, ");
		sql.append(" MLCRPR.MLCRPR_ID, ");
		sql.append(" MLCRPR.PRL_ID, ");
		sql.append(" HOAC.HRAC_ID, ");
		sql.append(" HOAC.MLCRPR_ID_COMP ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT ");
		sql.append(" WHERE HOAC.MLCRPR_ID  = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID  = MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRPR. MLCRPR_ID = ? ");
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mlcrprId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HorarioAcademicoDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetAHorarioAcademicoDto(rs));
			}
			
		} catch (SQLException e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new HorarioAcademicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademicoDto.listar.horario.por.periodo.paralelo.materia.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
			throw new HorarioAcademicoDtoNoEncontradoException("No hay resultados, intente con diferentes parámetros de búsqueda.");
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
	private HorarioAcademicoDto transformarResultSetADto(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));

			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			
//			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
//			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
//			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			
			retorno.setAlaId(rs.getInt(JdbcConstantes.ALA_ID));
			retorno.setAlaCodigo(rs.getString(JdbcConstantes.ALA_CODIGO));
			retorno.setAlaDescripcion(rs.getString(JdbcConstantes.ALA_DESCRIPCION));
			retorno.setAlaCapacidad(rs.getString(JdbcConstantes.ALA_CAPACIDAD));
			
			retorno.setHoclId(rs.getInt(JdbcConstantes.HOCL_ID));
			retorno.setHoclDescripcion(rs.getString(JdbcConstantes.HOCL_DESCRIPCION));
			retorno.setHoclHoraInicio(rs.getString(JdbcConstantes.HOCL_HORA_INICIO));
			retorno.setHoclHoraFin(rs.getString(JdbcConstantes.HOCL_HORA_FIN));
			retorno.setHoclHoclalId(rs.getInt(JdbcConstantes.HOCLAL_HOCL_ID));
			
			retorno.setHracHoclalId(rs.getInt(JdbcConstantes.HRAC_HOCLAL_ID));
			
			retorno.setHracId(rs.getInt(JdbcConstantes.HRAC_ID));
			retorno.setHracDia(rs.getInt(JdbcConstantes.HRAC_DIA));
			retorno.setHracFechaInicio(rs.getString(JdbcConstantes.HRAC_HORA_INICIO));
			retorno.setHracFechaFin(rs.getString(JdbcConstantes.HRAC_HORA_FIN));
			retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
			
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			
			retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
			retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
			
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
	private HorarioAcademicoDto transformarlistarHorarioPprlIdResultSetADto(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			retorno.setAlaId(rs.getInt(JdbcConstantes.ALA_ID));
			retorno.setAlaCodigo(rs.getString(JdbcConstantes.ALA_CODIGO));
			retorno.setAlaDescripcion(rs.getString(JdbcConstantes.ALA_DESCRIPCION));
			retorno.setAlaCapacidad(rs.getString(JdbcConstantes.ALA_CAPACIDAD));
			retorno.setHoclId(rs.getInt(JdbcConstantes.HOCL_ID));
			retorno.setHoclDescripcion(rs.getString(JdbcConstantes.HOCL_DESCRIPCION));
			retorno.setHoclHoraInicio(rs.getString(JdbcConstantes.HOCL_HORA_INICIO));
			retorno.setHoclHoraFin(rs.getString(JdbcConstantes.HOCL_HORA_FIN));
			retorno.setHracId(rs.getInt(JdbcConstantes.HRAC_ID));
			retorno.setHracDia(rs.getInt(JdbcConstantes.HRAC_DIA));
			retorno.setHracFechaInicio(rs.getString(JdbcConstantes.HRAC_HORA_INICIO));
			retorno.setHracFechaFin(rs.getString(JdbcConstantes.HRAC_HORA_FIN));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			
			
		return retorno;
	}
	

	
	/**
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param rs ResultSet
	 * @return Entidad
	 * @throws SQLException
	 */
	private HorarioAcademicoDto transformarResultSetAhorarioAcademicoDtoFull(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		
		retorno.setPrlId(rs.getInt("PRL_ID"));
		retorno.setMtrId(rs.getInt("MTR_ID"));
		retorno.setMlcrmtId(rs.getInt("MLCRMT_ID"));
		retorno.setPracId(rs.getInt("PRAC_ID"));
		retorno.setHoclHoraInicio(rs.getString("HOCL_HORA_INICIO"));
		retorno.setHoclHoraFin(rs.getString("HOCL_HORA_FIN"));
		retorno.setHracDia(rs.getInt("HRAC_DIA"));
		retorno.setHoclHoInicio(LocalTime.parse(retorno.getHoclHoraInicio().replace(" ", ""),  DateTimeFormatter.ofPattern("HH:mm")));
		retorno.setHoclHoFin(LocalTime.parse(retorno.getHoclHoraFin().replace(" ", ""),  DateTimeFormatter.ofPattern("HH:mm")));
		
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private HorarioAcademicoDto transformarResultSetADtoCupo(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));

			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			
//			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
//			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
//			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			
			retorno.setAlaId(rs.getInt(JdbcConstantes.ALA_ID));
			retorno.setAlaCodigo(rs.getString(JdbcConstantes.ALA_CODIGO));
			retorno.setAlaDescripcion(rs.getString(JdbcConstantes.ALA_DESCRIPCION));
			retorno.setAlaCapacidad(rs.getString(JdbcConstantes.ALA_CAPACIDAD));
			
			retorno.setHoclId(rs.getInt(JdbcConstantes.HOCL_ID));
			retorno.setHoclDescripcion(rs.getString(JdbcConstantes.HOCL_DESCRIPCION));
			retorno.setHoclHoraInicio(rs.getString(JdbcConstantes.HOCL_HORA_INICIO));
			retorno.setHoclHoraFin(rs.getString(JdbcConstantes.HOCL_HORA_FIN));
			retorno.setHoclHoclalId(rs.getInt(JdbcConstantes.HOCLAL_HOCL_ID));
			
			retorno.setHracHoclalId(rs.getInt(JdbcConstantes.HRAC_HOCLAL_ID));
			
			retorno.setHracId(rs.getInt(JdbcConstantes.HRAC_ID));
			retorno.setHracDia(rs.getInt(JdbcConstantes.HRAC_DIA));
			retorno.setHracFechaInicio(rs.getString(JdbcConstantes.HRAC_HORA_INICIO));
			retorno.setHracFechaFin(rs.getString(JdbcConstantes.HRAC_HORA_FIN));
			retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
			
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			
			retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
			retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
			
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
	private HorarioAcademicoDto transformarResultSetADtoComp(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));

			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			
//			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
//			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
//			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			
			retorno.setAlaId(rs.getInt(JdbcConstantes.ALA_ID));
			retorno.setAlaCodigo(rs.getString(JdbcConstantes.ALA_CODIGO));
			retorno.setAlaDescripcion(rs.getString(JdbcConstantes.ALA_DESCRIPCION));
			retorno.setAlaCapacidad(rs.getString(JdbcConstantes.ALA_CAPACIDAD));
			
			retorno.setHoclId(rs.getInt(JdbcConstantes.HOCL_ID));
			retorno.setHoclDescripcion(rs.getString(JdbcConstantes.HOCL_DESCRIPCION));
			retorno.setHoclHoraInicio(rs.getString(JdbcConstantes.HOCL_HORA_INICIO));
			retorno.setHoclHoraFin(rs.getString(JdbcConstantes.HOCL_HORA_FIN));
			retorno.setHoclHoclalId(rs.getInt(JdbcConstantes.HOCLAL_HOCL_ID));
			
			retorno.setHracHoclalId(rs.getInt(JdbcConstantes.HRAC_HOCLAL_ID));
			
			retorno.setHracId(rs.getInt(JdbcConstantes.HRAC_ID));
			retorno.setHracDia(rs.getInt(JdbcConstantes.HRAC_DIA));
			retorno.setHracFechaInicio(rs.getString(JdbcConstantes.HRAC_HORA_INICIO));
			retorno.setHracFechaFin(rs.getString(JdbcConstantes.HRAC_HORA_FIN));
			retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
			
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			
			retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
			retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
			
			retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
			
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			
		return retorno;
	}
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private HorarioAcademicoDto transformarResultSetADtoRep(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));

			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
			retorno.setPrlCupo(rs.getInt(JdbcConstantes.PRL_CUPO));
			
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			
			retorno.setAlaId(rs.getInt(JdbcConstantes.ALA_ID));
			retorno.setAlaCodigo(rs.getString(JdbcConstantes.ALA_CODIGO));
			retorno.setAlaDescripcion(rs.getString(JdbcConstantes.ALA_DESCRIPCION));
			retorno.setAlaCapacidad(rs.getString(JdbcConstantes.ALA_CAPACIDAD));
			
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			
			retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
			retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
			
			retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
			
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			
			retorno.setRcesNumeroMatriculados(rs.getInt("matriculados"));
			
			
		return retorno;
	}

	
	private HorarioAcademicoDto transformarResultSetAhorarioAcademicoEstudianteDto(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		retorno.setPracId(rs.getInt(1));
		retorno.setPracDescripcion(rs.getString(2));
		retorno.setDpnId(rs.getInt(3));
		retorno.setDpnDescripcion(rs.getString(4));
		retorno.setCrrId(rs.getInt(5));
		retorno.setCrrDescripcion(rs.getString(6));
		retorno.setPrsId(rs.getInt(7));
		retorno.setPrsIdentificacion(rs.getString(8));
		retorno.setPrsPrimerApellido(rs.getString(9));
		retorno.setPrsSegundoApellido(rs.getString(10));
		retorno.setPrsNombres(rs.getString(11));
		retorno.setMtrId(rs.getInt(12));
		retorno.setMtrCodigo(rs.getString(13));
		retorno.setMtrDescripcion(rs.getString(14));
		retorno.setMtrTimtId(rs.getInt(15));
		retorno.setPrlId(rs.getInt(16));
		retorno.setPrlCodigo(rs.getString(17));
		retorno.setHracDia(rs.getInt(18));
//		retorno.setHoclHoInicio(LocalTime.parse(String.valueOf(rs.getInt(19)),  DateTimeFormatter.ofPattern("HH:mm")));
		retorno.setHoclHoInicio(LocalTime.of(rs.getInt(19), 0));
		retorno.setHoclHoFin(LocalTime.of(rs.getInt(20), 0));
		retorno.setHoclDescripcion(rs.getString(21));
		retorno.setAlaId(rs.getInt(22));
		retorno.setAlaCodigo(rs.getString(23));
		retorno.setAlaDescripcion(rs.getString(24));
		return retorno;
	}
	
	private HorarioAcademicoDto transformarResultSetAhorarioAcademicoDocenteDto(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		PersonaDto docente = new PersonaDto();
		retorno.setPracDescripcion(rs.getString(1));
		retorno.setDpnDescripcion(rs.getString(2));
		retorno.setCrrDescripcion(rs.getString(3));
		docente.setPrsIdentificacion(rs.getString(4));
		docente.setPrsNombres(rs.getString(5));
		retorno.setHracPersonaDto(docente);
		retorno.setMtrCodigo(rs.getString(6));
		retorno.setMtrDescripcion(rs.getString(7));
		retorno.setHracDia(rs.getInt(8));
		retorno.setPrlCodigo(rs.getString(9));
		retorno.setPrlDescripcion(rs.getString(10));
		retorno.setHoclHoInicio(LocalTime.parse(rs.getString(11).trim(),  DateTimeFormatter.ofPattern("HH:mm")));
		retorno.setHoclHoFin(LocalTime.parse(rs.getString(12).trim(),  DateTimeFormatter.ofPattern("HH:mm")));
		retorno.setPracId(rs.getInt(13));
		retorno.setAlaDescripcion(rs.getString(14));
		return retorno;
	}
	
	private HorarioAcademicoDto transformarResultSetAHorarioAcademicoDto(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		retorno.setMlcrmtId(rs.getInt(1));
		retorno.setMlcrId(rs.getInt(2));
		retorno.setMtrId(rs.getInt(3));
		retorno.setMlcrmtEstado(rs.getInt(4));
		retorno.setMlcrprId(rs.getInt(5));
		retorno.setPrlId(rs.getInt(6));
		retorno.setHracId(rs.getInt(7));
		retorno.setHracMlcrprIdComp(rs.getInt(8));
		return retorno;
	}
	
	private HorarioAcademicoDto transformarResultSetAhorarioAcademicoModulo(ResultSet rs) throws SQLException{
		HorarioAcademicoDto retorno = new HorarioAcademicoDto();
		retorno.setPracId(rs.getInt(1));
		retorno.setPracDescripcion(rs.getString(2));
		retorno.setDpnId(rs.getInt(3));
		retorno.setDpnDescripcion(rs.getString(4));
		retorno.setCrrId(rs.getInt(5));
		retorno.setCrrDescripcion(rs.getString(6));
		retorno.setMtrId(rs.getInt(7));
		retorno.setMtrCodigo(rs.getString(8));
		retorno.setMtrDescripcion(rs.getString(9));
		retorno.setMtrTimtId(rs.getInt(10));
		retorno.setPrlId(rs.getInt(11));
		retorno.setPrlCodigo(rs.getString(12));
		retorno.setHracDia(rs.getInt(13));
		retorno.setHoclHoInicio(LocalTime.of(rs.getInt(14), 0));
		retorno.setHoclHoFin(LocalTime.of(rs.getInt(15), 0));
		retorno.setHoclDescripcion(rs.getString(16));
		retorno.setAlaId(rs.getInt(17));
		retorno.setAlaCodigo(rs.getString(18));
		retorno.setAlaDescripcion(rs.getString(19));
		return retorno;
	}
}
