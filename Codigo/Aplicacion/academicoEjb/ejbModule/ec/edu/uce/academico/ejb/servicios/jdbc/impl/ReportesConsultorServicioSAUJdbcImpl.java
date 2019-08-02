package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReportesConsultorServicioSAUJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;




@Stateless
public class ReportesConsultorServicioSAUJdbcImpl implements ReportesConsultorServicioSAUJdbc {
	//SAU
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_SAU)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Lista los estudiantes matriculados por Facultad, Carrera y Periodo Academico.
	 * @return Lista de los estudiantes matriculados.
	 * @throws PersonaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PersonaDto> listarEstudiantesMatriculadosXFacultadXCarreraXPeriodoAcademico(int dpn, int crr, int prac)
			throws PersonaDtoException, PersonaDtoNoEncontradoException {
		List<PersonaDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		int numeral = 0;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" p.");sbSql.append("PER_DESCRIPCION");
			sbSql.append(" , f.");sbSql.append("NOMBRE");
			sbSql.append(" , ee.");sbSql.append("ESPE_NOMBRE");
			sbSql.append(" , e.");sbSql.append("EST_CEDULA");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_PATERNO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_MATERNO");
			sbSql.append(" , e.");sbSql.append("EST_NOMBRES");
			sbSql.append(" , mtr.");sbSql.append("MATR_CURSO");
			sbSql.append(" , e.");sbSql.append("MAIL");
			sbSql.append(" , e.");sbSql.append("MAIL:INSTITUCIONAL");
			sbSql.append(" FROM ");
			sbSql.append("REGISTRO_MATERIAS");sbSql.append(" rm ");
			sbSql.append(" LEFT JOIN ");sbSql.append("MATERIAS");sbSql.append(" mat ON ");
			sbSql.append(" rm.");sbSql.append("MAT_CODIGO");sbSql.append(" = ");sbSql.append(" mat.");sbSql.append("MAT_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("MATRICULAS");sbSql.append(" mtr ON ");
			sbSql.append(" rm.");sbSql.append("PER_CODIGO");sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append("PER_CODIGO");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("EST_CODIGO");sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append("EST_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("PERIODOS");sbSql.append(" p ON ");
			sbSql.append(" mtr.");sbSql.append("PER_CODIGO");sbSql.append(" = ");sbSql.append(" p.");sbSql.append("PER_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESPECIALIDADES_ESCUELA");sbSql.append(" ee ON ");
			sbSql.append(" mtr.");sbSql.append("ESPE_CODIGO");sbSql.append(" = ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESCUELAS");sbSql.append(" s ON ");
			sbSql.append(" ee.");sbSql.append("ESC_CODIGO");sbSql.append(" = ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" AND ");sbSql.append(" p.");sbSql.append("ESC_CODIGO");sbSql.append(" = ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("FACULTAD");sbSql.append(" f ON ");
			sbSql.append(" s.");sbSql.append("ID_FACULTAD");sbSql.append(" = ");sbSql.append(" f.");sbSql.append("ID");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESTUDIANTES");sbSql.append(" e ON ");
			sbSql.append(" mtr.");sbSql.append("EST_CODIGO");sbSql.append(" = ");sbSql.append(" e.");sbSql.append("EST_CODIGO");
			sbSql.append(" WHERE ");
			sbSql.append(" f.");sbSql.append("ID");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" NOT IN ");sbSql.append("(-1, 5, 6, 60, 980, 1655, 79)");
			sbSql.append(" AND ");sbSql.append(" p.");sbSql.append("GRUPO");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append("MATR_CURSO");
			sbSql.append(" > 0 ");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append("MATR_ESTADO");
			sbSql.append(" > 1 ");
			sbSql.append(" ORDER BY ");sbSql.append("APELLIDO_PATERNO");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, dpn);
			pstmt.setInt(2, crr);
			pstmt.setInt(3, prac);
			rs = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.exception")));
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
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.no.result.exception")));
		}
			
		return retorno;
	}

	public List<SolicitudTerceraMatriculaDto> listarEstudiantesTercerasMatriculasXCarreraXPeriodoAcademico(int dpn, int crr,Timestamp fechaini,Timestamp fechafin) throws SolicitudTerceraMatriculaDtoException, SolicitudTerceraMatriculaDtoNoEncontradoException {
		List<SolicitudTerceraMatriculaDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		int numeral = 0;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" f.");sbSql.append("NOMBRE");
			sbSql.append(" , ee.");sbSql.append("ESPE_NOMBRE");
			sbSql.append(" , e.");sbSql.append("EST_CEDULA");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_PATERNO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_MATERNO");
			sbSql.append(" , e.");sbSql.append("EST_NOMBRES");
			sbSql.append(" , mat.");sbSql.append("MAT_NOMBRE");
			sbSql.append(" , rm.");sbSql.append("NUMERO_MATRICULA");
			sbSql.append(" , eeat.");sbSql.append("OBSERVACION");
			sbSql.append(" , eeat.");sbSql.append("FECHA_CREACION");
			sbSql.append(" , eeat.");sbSql.append("FECHA_APROBACION");
			sbSql.append(" , CASE ");sbSql.append("eeat.");sbSql.append("ESTADO");
			sbSql.append(" WHEN ");sbSql.append(" '1' ");sbSql.append(" THEN ");sbSql.append(" 'APROBADO' ");
			sbSql.append(" WHEN ");sbSql.append(" '2' ");sbSql.append(" THEN ");sbSql.append(" 'NEGADO' ");
			sbSql.append(" ELSE ");sbSql.append(" 'EN PROCESO' ");sbSql.append(" END ");
			sbSql.append(" , CASE ");sbSql.append("eeat.");sbSql.append("INSTANCIA");
			sbSql.append(" WHEN ");sbSql.append(" '1' ");sbSql.append(" THEN ");sbSql.append(" 'DIRECTOR' ");
			sbSql.append(" WHEN ");sbSql.append(" '2' ");sbSql.append(" THEN ");sbSql.append(" 'DECANO' ");
			sbSql.append(" WHEN ");sbSql.append(" '3' ");sbSql.append(" THEN ");sbSql.append(" 'CONSEJO DIRECTIVO' ");sbSql.append(" END ");
			sbSql.append(" , eeat.");sbSql.append("OBSERVACION_INSTANCIA");
			sbSql.append(" FROM ");
			sbSql.append("MATERIAS");sbSql.append(" mat ");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESPECIALIDADES_ESCUELA");sbSql.append(" ee ON ");
			sbSql.append(" mat.");sbSql.append("ESPE_CODIGO");sbSql.append(" = ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESCUELAS");sbSql.append(" s ON ");
			sbSql.append(" ee.");sbSql.append("ESC_CODIGO");sbSql.append(" = ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" AND ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");sbSql.append(" = ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("FACULTAD");sbSql.append(" f ON ");
			sbSql.append(" s.");sbSql.append("ID_FACULTAD");sbSql.append(" = ");sbSql.append(" f.");sbSql.append("ID");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESTUDIANTES_ESCUELA_AUTORIZACIONES_TERCERAS");sbSql.append(" eeat ON ");
			sbSql.append(" mat.");sbSql.append("MAT_CODIGO");sbSql.append(" = ");sbSql.append(" eeat.");sbSql.append("MATERIA_ID");
			sbSql.append(" LEFT JOIN ");sbSql.append("ESTUDIANTES");sbSql.append(" e ON ");
			sbSql.append(" e.");sbSql.append("EST_CODIGO");sbSql.append(" = ");sbSql.append(" eeat.");sbSql.append("EST_CODIGO");
			sbSql.append(" LEFT JOIN ");sbSql.append("REGISTRO_MATERIAS");sbSql.append(" rm ON ");
			sbSql.append(" eeat.");sbSql.append("EST_CODIGO");sbSql.append(" = ");sbSql.append(" rm.");sbSql.append("EST_CODIGO");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("MAT_CODIGO");sbSql.append(" = ");sbSql.append(" eeat.");sbSql.append("MATERIA_ID");
			sbSql.append(" , dblink('dbname=academico_wf ','select distinct ahp.start_user_id_,aht.proc_inst_id_,aht.proc_def_id_,aht.start_time_,aht.end_time_,aht.assignee_ from  act_hi_taskinst aht LEFT JOIN act_hi_procinst ahp on ahp.proc_inst_id_ = aht.proc_inst_id_')As p(start_user_id_ varchar,proc_inst_id_ varchar,proc_def_id_  varchar,start_time_ timestamp,end_time_ timestamp,assignee_ varchar)");
			sbSql.append(" WHERE ");
			sbSql.append(" f.");sbSql.append("ID");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" NOT IN ");sbSql.append("(-1, 5, 6, 60, 980, 1655, 79)");
			sbSql.append(" AND ");sbSql.append(" e.");sbSql.append("EST_CEDULA");
			sbSql.append(" = ");sbSql.append(" p.");sbSql.append("START_USER_ID_");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("NUMERO_MATRICULA");
			sbSql.append(" IN ");sbSql.append(" (2,3) ");
			sbSql.append(" AND ");sbSql.append(" eeat.");sbSql.append("ESTADO");
			sbSql.append(" IN ");sbSql.append(" (0,1,2) ");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("RMAT_APROBADO");
			sbSql.append(" NOT IN ");sbSql.append(" (2) ");
			sbSql.append(" AND ");sbSql.append(" eeat.");sbSql.append("MATERIA_ID");
			sbSql.append(" = ");sbSql.append(" mat.");sbSql.append("MAT_CODIGO");
			sbSql.append(" AND ");sbSql.append(" eeat.");sbSql.append("FECHA_CREACION");
			sbSql.append(" BETWEEN ");sbSql.append(" ? ");sbSql.append("AND");sbSql.append(" ? ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" e.");sbSql.append("EST_APELLIDO_PATERNO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_MATERNO");
			sbSql.append(" , e.");sbSql.append("EST_NOMBRES");
			sbSql.append(" , mat.");sbSql.append("MAT_NOMBRE");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, dpn);
			pstmt.setInt(2, crr);
			pstmt.setTimestamp(3, fechaini);
			pstmt.setTimestamp(4, fechafin);
			rs = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
			throw new  SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new  SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.exception")));
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
			throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.no.result.exception")));
		}
			
		return retorno;
	}

	
	public List<RecordEstudianteDto> listarNotasEstudiantesXCarreraXPeriodoAcademico(int dpn,int crr, int prac)
			throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException {
		List<RecordEstudianteDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		int numeral = 0;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" e.");sbSql.append("EST_CEDULA");
			sbSql.append(" , p.");sbSql.append("PER_DESCRIPCION");
			sbSql.append(" , ee.");sbSql.append("ESPE_NOMBRE");
			sbSql.append(" , mtr.");sbSql.append("MATR_CURSO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_PATERNO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_MATERNO");
			sbSql.append(" , e.");sbSql.append("EST_NOMBRES");
			sbSql.append(" , mat.");sbSql.append("MAT_CODIGO_UCE");
			sbSql.append(" , mat.");sbSql.append("MAT_NOMBRE");
			sbSql.append(" , mtr.");sbSql.append("MATR_NUMERO");
			sbSql.append(" , rm.");sbSql.append("RMAT_NOTA_UNO");
			sbSql.append(" , rm.");sbSql.append("RMAT_NOTA_DOS");
			sbSql.append(" , rm.");sbSql.append("NOTA_FINAL");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_UNO");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_TOTAL_UNO");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_DOS");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_TOTAL_DOS");
			sbSql.append(" , rm.");sbSql.append("ASISTENCIA_FINAL");
			sbSql.append(" , sum(mat.MAT_NUMERO_CREDITOS)");
			sbSql.append(" , CASE ");sbSql.append("rm.");sbSql.append("RMAT_APROBADO");
			sbSql.append(" WHEN ");sbSql.append(" -1 ");sbSql.append(" THEN ");sbSql.append(" 'ANULADO' ");
			sbSql.append(" WHEN ");sbSql.append(" 0 ");sbSql.append(" THEN ");sbSql.append(" '??0' ");
			sbSql.append(" WHEN ");sbSql.append(" 1 ");sbSql.append(" THEN ");sbSql.append(" 'INSCRITO' ");
			sbSql.append(" WHEN ");sbSql.append(" 2 ");sbSql.append(" THEN ");sbSql.append(" 'MATRICULADO' ");
			sbSql.append(" WHEN ");sbSql.append(" 3 ");sbSql.append(" THEN ");sbSql.append(" 'APROBADO' ");
			sbSql.append(" WHEN ");sbSql.append(" 4 ");sbSql.append(" THEN ");sbSql.append(" 'REPROBADO' ");
			sbSql.append(" WHEN ");sbSql.append(" 5 ");sbSql.append(" THEN ");sbSql.append(" 'NINGUNA_NOTA' ");
			sbSql.append(" WHEN ");sbSql.append(" 6 ");sbSql.append(" THEN ");sbSql.append(" 'SUSPENSO' ");
			sbSql.append(" WHEN ");sbSql.append(" 7 ");sbSql.append(" THEN ");sbSql.append(" 'PERDIDO_POR_ASISTENCIA' ");
			sbSql.append(" WHEN ");sbSql.append(" 10 ");sbSql.append(" THEN ");sbSql.append(" 'CONVALIDADO' ");
			sbSql.append(" ELSE ");sbSql.append(" 'OTHER' ");sbSql.append(" END ");
			sbSql.append(" , hp.");sbSql.append("DESCRIPCION");
			sbSql.append(" , hp.");sbSql.append("NUMERO");
			sbSql.append(" FROM ");
			sbSql.append("REGISTRO_MATERIAS");sbSql.append(" rm ");
			sbSql.append(" INNER JOIN ");sbSql.append("MATERIAS");sbSql.append(" mat ON ");
			sbSql.append(" rm.");sbSql.append("MAT_CODIGO");sbSql.append(" = ");sbSql.append(" mat.");sbSql.append("MAT_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("MATRICULAS");sbSql.append(" mtr ON ");
			sbSql.append(" rm.");sbSql.append("PER_CODIGO");sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append("PER_CODIGO");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("EST_CODIGO");sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append("EST_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("ESPECIALIDADES_ESCUELA");sbSql.append(" ee ON ");
			sbSql.append(" mtr.");sbSql.append("ESPE_CODIGO");sbSql.append(" = ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" AND ");sbSql.append(" mat.");sbSql.append("ESPE_CODIGO");sbSql.append(" = ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("ESCUELAS");sbSql.append(" s ON ");
			sbSql.append(" ee.");sbSql.append("ESC_CODIGO");sbSql.append(" = ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("FACULTAD");sbSql.append(" f ON ");
			sbSql.append(" s.");sbSql.append("ID_FACULTAD");sbSql.append(" = ");sbSql.append(" f.");sbSql.append("ID");
			sbSql.append(" INNER JOIN ");sbSql.append("PERIODOS");sbSql.append(" p ON ");
			sbSql.append(" mtr.");sbSql.append("PER_CODIGO");sbSql.append(" = ");sbSql.append(" p.");sbSql.append("PER_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("ESTUDIANTES");sbSql.append(" e ON ");
			sbSql.append(" mtr.");sbSql.append("EST_CODIGO");sbSql.append(" = ");sbSql.append(" e.");sbSql.append("EST_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("HOR_MATERIA");sbSql.append(" hm ON ");
			sbSql.append(" mat.");sbSql.append("MAT_CODIGO");sbSql.append(" = ");sbSql.append(" hm.");sbSql.append("ID_MATERIA");
			sbSql.append(" INNER JOIN ");sbSql.append("HOR_PARALELO");sbSql.append(" hp ON ");
			sbSql.append(" hm.");sbSql.append("ID_PARALELO");sbSql.append(" = ");sbSql.append(" hp.");sbSql.append("ID");
			sbSql.append(" AND ");sbSql.append(" hp.");sbSql.append("ESPE_CODIGO");sbSql.append(" = ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" AND ");sbSql.append(" hp.");sbSql.append("ID_PERIODO");sbSql.append(" = ");sbSql.append(" p.");sbSql.append("PER_CODIGO");
			sbSql.append(" INNER JOIN ");sbSql.append("PRF_PROFESOR");sbSql.append(" prf ON ");
			sbSql.append(" hm.");sbSql.append("ID_PROFESOR");sbSql.append(" = ");sbSql.append(" prf.");sbSql.append("PRO_CODIGO");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("RMAT_PARALELO");sbSql.append(" = ");sbSql.append(" hp.");sbSql.append("NUMERO");
			sbSql.append(" AND ");sbSql.append(" rm.");sbSql.append("PER_CODIGO");sbSql.append(" = ");sbSql.append(" hp.");sbSql.append("ID_PERIODO");
			sbSql.append(" WHERE ");
			sbSql.append(" f.");sbSql.append("ID");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" ee.");sbSql.append("ESPE_CODIGO");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" p.");sbSql.append("GRUPO");
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" s.");sbSql.append("ESC_CODIGO");
			sbSql.append(" NOT IN ");sbSql.append("(-1, 5, 6, 60, 980, 1655, 79)");
			sbSql.append(" GROUP BY ");
			sbSql.append(" e.");sbSql.append("EST_CEDULA");
			sbSql.append(" , p.");sbSql.append("PER_DESCRIPCION");
			sbSql.append(" , ee.");sbSql.append("ESPE_NOMBRE");
			sbSql.append(" , mtr.");sbSql.append("MATR_CURSO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_PATERNO");
			sbSql.append(" , e.");sbSql.append("EST_APELLIDO_MATERNO");
			sbSql.append(" , e.");sbSql.append("EST_NOMBRES");
			sbSql.append(" , mat.");sbSql.append("MAT_CODIGO_UCE");
			sbSql.append(" , mat.");sbSql.append("MAT_NOMBRE");	
			sbSql.append(" , mtr.");sbSql.append("MATR_NUMERO");
			sbSql.append(" , rm.");sbSql.append("RMAT_NOTA_UNO");
			sbSql.append(" , rm.");sbSql.append("RMAT_NOTA_DOS"); 
			sbSql.append(" , rm.");sbSql.append("NOTA_FINAL");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_UNO");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_TOTAL_UNO");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_DOS");
			sbSql.append(" , rm.");sbSql.append("RMAT_ASISTENCIA_TOTAL_DOS");
			sbSql.append(" , rm.");sbSql.append("ASISTENCIA_FINAL");
			sbSql.append(" , hp.");sbSql.append("DESCRIPCION");
			sbSql.append(" , hp.");sbSql.append("NUMERO");
			sbSql.append(" , rm.");sbSql.append("RMAT_APROBADO");
			sbSql.append(" ORDER BY ");sbSql.append("APELLIDO_PATERNO");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, dpn);
			pstmt.setInt(2, crr);
			pstmt.setInt(3, prac);
			rs = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.exception")));
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
			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorSAUJdbc.listar.matriculados.no.result.exception")));
		}
			
		return retorno;
		
	}

	
	public List<RecordEstudianteDto> generarRecordAcademicoXCarreraXCedula(int crr, String cedula)
			throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException {

		
		
		
		return null;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private PersonaDto transformarResultSetADto(ResultSet rs,int numeral)throws SQLException {
		PersonaDto retorno = new PersonaDto();
		retorno.setNumeral(numeral);
		retorno.setPracDescripcion(rs.getString("PER_DESCRIPCION"));
		retorno.setDpnDescripcion(rs.getString("NOMBRE"));
		retorno.setCrrDescripcion(rs.getString("ESPE_NOMBRE"));
		retorno.setPrsIdentificacion(rs.getString("EST_CEDULA"));
		retorno.setPrsPrimerApellido(rs.getString("EST_APELLIDO_PATERNO"));
		retorno.setPrsSegundoApellido(rs.getString("EST_APELLIDO_MATERNO"));
		retorno.setPrsNombres(rs.getString("EST_NOMBRES"));
		retorno.setNvlDescripcion(rs.getString("MATR_CURSO"));
		retorno.setPrsMailPersonal(rs.getString("MAIL"));
		retorno.setPrsMailInstitucional(rs.getString("MAIL:INSTITUCIONAL"));
		
		return retorno;
	}
	
	private SolicitudTerceraMatriculaDto transformarResultSetADto2(ResultSet rs,int numeral)throws SQLException {
		
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		
		retorno.setNumeral(numeral);
		retorno.setDpnDescripcion(rs.getString("NOMBRE"));
		retorno.setCrrDescripcion(rs.getString("ESPE_NOMBRE"));
		retorno.setPrsIdentificacion(rs.getString("EST_CEDULA"));
		retorno.setPrsPrimerApellido(rs.getString("EST_APELLIDO_PATERNO"));
		retorno.setPrsSegundoApellido(rs.getString("EST_APELLIDO_MATERNO"));
		retorno.setPrsNombres(rs.getString("EST_NOMBRES"));
		retorno.setMtrDescripcion(rs.getString("MAT_NOMBRE"));
		retorno.setNvlDescripcion(rs.getString("NUMERO_MATRICULA"));
		retorno.setSltrmtEstado(rs.getInt("ESTADO"));
		retorno.setSltrmtFechaSolicitud(rs.getTimestamp("FECHA_CREACION"));
		retorno.setSltrmtFechaVerificacion(rs.getTimestamp("FECHA_APROBACION"));
		//Falta Autorizado por...
		retorno.setSltrmtObservacion(rs.getString("OBSERVACION"));
		retorno.setSltrmtObservacionFinal(rs.getString("OBSERVACION_INSTANCIA"));
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetADto3(ResultSet rs)throws SQLException {
		
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		
		retorno.setPrsIdentificacion(rs.getString("EST_CEDULA"));
		retorno.setRcesPracDescripcion(rs.getString("PER_DESCRIPCION"));
		retorno.setCrrDescripcion(rs.getString("ESPE_NOMBRE"));
		retorno.setPrsPrimerApellido(rs.getString("EST_APELLIDO_PATERNO"));
		retorno.setPrsSegundoApellido(rs.getString("EST_APELLIDO_MATERNO"));
		retorno.setPrsNombres(rs.getString("EST_NOMBRES"));
		retorno.setNvlDescripcion(rs.getString("MATR_CURSO"));
		retorno.setMtrCodigo(rs.getString("MAT_CODIGO_UCE"));
		retorno.setMtrDescripcion(rs.getString("MAT_NOMBRE"));
		retorno.setClfNota1(rs.getBigDecimal("RMAT_NOTA_UNO"));
		retorno.setClfAsistencia1(rs.getInt("RMAT_ASISTENCIA_TOTAL_UNO"));
		retorno.setClfNota2(rs.getBigDecimal("RMAT_NOTA_DOS"));
		retorno.setClfAsistencia2(rs.getInt("RMAT_ASISTENCIA_TOTAL_DOS"));
		retorno.setClfNotaFinal(rs.getBigDecimal("NOTA_FINAL"));
		retorno.setClfAsistenciaFinal(rs.getInt("ASISTENCIA_FINAL"));
		retorno.setClfEstado(rs.getInt("RMAT_APROBADO"));
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetADto4(ResultSet rs)throws SQLException {
		
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		
//		retorno.setPrlPeriodoAcademicoId(rs.getInt());
//		retorno.setRcesId(rs.getInt());
//		retorno.setMtrDescripcion(rs.getString());
//		retorno.setClfNota1(rs.getBigDecimal());
//		retorno.setClfAsistencia1(rs.getInt());
//		retorno.setClfNota2(rs.getBigDecimal());
//		retorno.setClfAsistencia2(rs.getInt());
//		retorno.setClfNotaFinal(rs.getBigDecimal());
//		retorno.setClfAsistenciaFinal(rs.getInt());
//		retorno.setClfEstado(rs.getInt());
//		
		return retorno;
	}
	
	
	
	
	
	

}
