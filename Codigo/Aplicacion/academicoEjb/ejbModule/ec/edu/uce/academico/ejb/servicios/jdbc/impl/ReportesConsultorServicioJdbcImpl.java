package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReportesConsultorServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

@Stateless
public class ReportesConsultorServicioJdbcImpl implements ReportesConsultorServicioJdbc {
	//SIIU
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
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
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
			sbSql.append(" AND ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, dpn);
			pstmt.setInt(2, crr);
			pstmt.setInt(3, prac);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PersonaDto>();
			while(rs.next()){
				numeral++;
				retorno.add(transformarResultSetADto(rs,numeral));
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.matriculados.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.matriculados.exception")));
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
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.matriculados.no.result.exception")));
		}
			
		return retorno;
	}
	/**
	 * Lista los estudiantes con solicitudes de terceras matriculas por Facultad, Carrera y Periodo Academico.
	 * @return Lista de los estudiantes con terceras matriculas.
	 * @throws PersonaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarEstudiantesTercerasMatriculasXCarreraXPeriodoAcademico(int crr,int prac)
			throws SolicitudTerceraMatriculaDtoException,SolicitudTerceraMatriculaDtoNoEncontradoException{
		List<SolicitudTerceraMatriculaDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		int numeral = 0;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FECHA_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FECHA_VERIF_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION_FINAL);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");
			sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, prac);
			pstmt.setInt(2, crr);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				numeral++;
				retorno.add(transformarResultSetADto2(rs,numeral));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.tercerasmatriculas.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.tercerasmatriculas.exception")));
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
			throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.tercerasmatriculas.no.result.exception")));
		}
		
		
		return retorno;
	}
	
	/**
	 * Lista las notas de los estudiantes por Carrera y Periodo Academico.
	 * @return Lista de las notas de los estudiantes.
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RecordEstudianteDto> listarNotasEstudiantesXCarreraXPeriodoAcademico(int crr,int prac) 
			throws RecordEstudianteDtoException,RecordEstudianteDtoNoEncontradoException{
		List<RecordEstudianteDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, prac);
			pstmt.setInt(2, crr);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto3(rs));
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.notasEstudiantes.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.notasEstudiantes.exception")));
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
			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.listar.notasEstudiantes.no.result.exception")));
		}
		
		return retorno;
	}
	/**
	 * Genera el record academico con las notas del estudiante por su Carrera y Cedula.
	 * @return Lista de las notas de los estudiantes.
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RecordEstudianteDto> generarRecordAcademicoXCarreraXCedula(int crr,String cedula) 
			throws RecordEstudianteDtoException,RecordEstudianteDtoNoEncontradoException{
		List<RecordEstudianteDto>retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			sbSql.append(" , clf.");sbSql.append(JdbcConstantes.CLF_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crr);
			pstmt.setString(2,cedula);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RecordEstudianteDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto4(rs));
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.generar.RecordEstudiante.sql.exception")));
		}catch(Exception e){
			e.printStackTrace();
			throw new RecordEstudianteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.generar.RecordEstudiante.exception")));
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
			throw new RecordEstudianteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReportesConsultorJdbc.generar.RecordEstudiante.no.result.exception")));
		}
		
		return retorno;
	}

	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private PersonaDto transformarResultSetADto(ResultSet rs,int numeral)throws SQLException {
		PersonaDto retorno = new PersonaDto();
		retorno.setNumeral(numeral);
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		
		return retorno;
	}
	
	private SolicitudTerceraMatriculaDto transformarResultSetADto2(ResultSet rs,int numeral)throws SQLException {
		
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		
		retorno.setNumeral(numeral);
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.SLTRMT_MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setSltrmtTipo(rs.getInt(JdbcConstantes.SLTRMT_TIPO));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		retorno.setSltrmtFechaSolicitud(rs.getTimestamp(JdbcConstantes.SLTRMT_FECHA_SOLICITUD));
		retorno.setSltrmtFechaVerificacion(rs.getTimestamp(JdbcConstantes.SLTRMT_FECHA_VERIF_SOLICITUD));
		retorno.setSltrmtObservacion(rs.getString(JdbcConstantes.SLTRMT_OBSERVACION));
		retorno.setSltrmtObservacionFinal(rs.getString(JdbcConstantes.SLTRMT_OBSERVACION_FINAL));
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetADto3(ResultSet rs)throws SQLException {
		
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setClfNota1(rs.getBigDecimal(JdbcConstantes.CLF_NOTA1));
		retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
		retorno.setClfNota2(rs.getBigDecimal(JdbcConstantes.CLF_NOTA2));
		retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
		retorno.setClfNotaFinal(rs.getBigDecimal(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
		retorno.setClfAsistenciaFinal(rs.getInt(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
		retorno.setClfEstado(rs.getInt(JdbcConstantes.CLF_ESTADO));
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetADto4(ResultSet rs)throws SQLException {
		
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		
		retorno.setPrlPeriodoAcademicoId(rs.getInt(JdbcConstantes.PRL_PRAC_ID));
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setClfNota1(rs.getBigDecimal(JdbcConstantes.CLF_NOTA1));
		retorno.setClfAsistencia1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
		retorno.setClfNota2(rs.getBigDecimal(JdbcConstantes.CLF_NOTA2));
		retorno.setClfAsistencia2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
		retorno.setClfNotaFinal(rs.getBigDecimal(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
		retorno.setClfAsistenciaFinal(rs.getInt(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
		retorno.setClfEstado(rs.getInt(JdbcConstantes.CLF_ESTADO));
		
		return retorno;
	}

}
