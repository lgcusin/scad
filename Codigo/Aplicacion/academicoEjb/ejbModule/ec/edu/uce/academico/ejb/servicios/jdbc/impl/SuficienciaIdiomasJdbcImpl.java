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
   
 ARCHIVO:     HistorialAcademicoJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de HistorialAcademico.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
30Nov2018             Freddy Guzmán                       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CalificacionDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaIdiomasServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB HistorialAcademicoJdbcImpl. Clase donde se implementan los metodos para
 * el servicio jdbc de MatriculaServicioJdbc.
 * 
 * @author fgguzman
 * @version 1.0
 */

@Stateless
public class SuficienciaIdiomasJdbcImpl implements SuficienciaIdiomasServicioJdbc {

	@Resource(mappedName = GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@Resource(lookup = GeneralesConstantes.APP_DATA_SOURCE_SAU)
	private DataSource dsSau;
	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;

	public List<RecordEstudianteDto> buscarAprobacionIdiomasSIIU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException{
		List<RecordEstudianteDto> retorno = null;
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
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.CRR_PROCESO, ");
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   FCMT.FCMT_ID, ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION, ");
		sql.append("   FCMT.FCMT_TIPO, ");
		sql.append("   MDL.MDL_DESCRIPCION, ");
		sql.append("   FCMT.FCMT_PRAC_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_CREDITOS, ");
		sql.append("   MTR.MTR_HORAS, ");
		sql.append("   DTMT.DTMT_NUMERO, ");
		sql.append("   NVL.NVL_ID,  ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   NVL.NVL_DESCRIPCION, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   Prl.Prl_Descripcion, ");
		sql.append("   CLF.CLF_ID, ");
		sql.append("   CLF.CLF_NOTA1, ");
		sql.append("   CLF.CLF_NOTA2, ");
		sql.append("   CLF.CLF_SUPLETORIO, ");
		sql.append("   CLF.CLF_SUMA_P1_P2, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION1, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION2, ");
		sql.append("   CLF.CLF_NOTA_FINAL_SEMESTRE, ");
		sql.append("   CLF.CLF_ASISTENCIA1, ");
		sql.append("   CLF.CLF_ASISTENCIA2, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE1, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE2, ");
		sql.append("   CLF.CLF_PROMEDIO_ASISTENCIA   ");
		sql.append(" FROM PERSONA PRS INNER JOIN USUARIO USR ON PRS.PRS_ID = USR.PRS_ID ");
		sql.append("                  INNER JOIN USUARIO_ROL USRO ON USR.USR_ID = USRO.USR_ID ");
		sql.append("                  INNER JOIN FICHA_INSCRIPCION FCIN ON USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append("                  INNER JOIN FICHA_ESTUDIANTE FCES ON FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append("                  INNER JOIN FICHA_MATRICULA FCMT ON FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append("                  INNER JOIN COMPROBANTE_PAGO CMPA ON FCMT.FCMT_ID = CMPA.FCMT_ID ");
		sql.append("                  INNER JOIN DETALLE_MATRICULA DTMT ON CMPA.CMPA_ID = DTMT.CMPA_ID ");
		sql.append("                  INNER JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  INNER JOIN RECORD_ESTUDIANTE RCES ON FCES.FCES_ID = RCES.FCES_ID  AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR MLCR ON MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append("                  LEFT JOIN CARRERA CRR ON CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append("                  LEFT JOIN DEPENDENCIA DPN ON DPN.DPN_ID = CRR.DPN_ID ");
		sql.append("                  LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append("                  LEFT JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append("                  LEFT JOIN NIVEL NVL ON NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append("                  LEFT JOIN CONFIGURACION_CARRERA CNCR ON CNCR.CRR_ID = CRR.CRR_ID ");
		sql.append("                  LEFT JOIN CALIFICACION CLF ON RCES.RCES_ID = CLF.RCES_ID ");
		sql.append("                  LEFT JOIN MODALIDAD MDL ON MDL.MDL_ID  = CNCR.MDL_ID ");
		sql.append("                  LEFT JOIN MATERIA MTR ON MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append(" WHERE CRR.CRR_TIPO = " + CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
		sql.append(" AND NVL.NVL_NUMERAL IN (" + NivelConstantes.NIVEL_CUARTO_VALUE + "," + NivelConstantes.NUMERAL_APROBACION_VALUE + ")") ;
		sql.append(" AND RCES.RCES_ESTADO IN (" + RecordEstudianteConstantes.ESTADO_APROBADO_VALUE + "," + RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE + ")");
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" ORDER BY  CRR.CRR_TIPO,  PRAC.PRAC_ID, CRR.CRR_DESCRIPCION, NVL.NVL_NUMERAL, MTR.MTR_CODIGO ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoIdiomas(rs));
			}

		} catch (Exception e) {
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MatriculaServicioJdbcImpl.buscar.record.academico.sau.error.tipo.sql")));
		} finally {
			try {
				if (rs != null) {
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

		if (retorno.isEmpty()) {
			throw new RecordEstudianteNoEncontradoException();
		}

		return retorno;
	}
	
	public List<RecordEstudianteDto> buscarEstudiantesAprobadosIdiomasSIIU(int periodoId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException{
		List<RecordEstudianteDto> retorno = null;
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
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.CRR_PROCESO, ");
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   FCMT.FCMT_ID, ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION, ");
		sql.append("   FCMT.FCMT_TIPO, ");
		sql.append("   MDL.MDL_DESCRIPCION, ");
		sql.append("   FCMT.FCMT_PRAC_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_CREDITOS, ");
		sql.append("   MTR.MTR_HORAS, ");
		sql.append("   DTMT.DTMT_NUMERO, ");
		sql.append("   NVL.NVL_ID,  ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   NVL.NVL_DESCRIPCION, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   Prl.Prl_Descripcion, ");
		sql.append("   CLF.CLF_ID, ");
		sql.append("   CLF.CLF_NOTA1, ");
		sql.append("   CLF.CLF_NOTA2, ");
		sql.append("   CLF.CLF_SUPLETORIO, ");
		sql.append("   CLF.CLF_SUMA_P1_P2, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION1, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION2, ");
		sql.append("   CLF.CLF_NOTA_FINAL_SEMESTRE, ");
		sql.append("   CLF.CLF_ASISTENCIA1, ");
		sql.append("   CLF.CLF_ASISTENCIA2, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE1, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE2, ");
		sql.append("   CLF.CLF_PROMEDIO_ASISTENCIA   ");
		sql.append(" FROM PERSONA PRS INNER JOIN USUARIO USR ON PRS.PRS_ID = USR.PRS_ID ");
		sql.append("                  INNER JOIN USUARIO_ROL USRO ON USR.USR_ID = USRO.USR_ID ");
		sql.append("                  INNER JOIN FICHA_INSCRIPCION FCIN ON USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append("                  INNER JOIN FICHA_ESTUDIANTE FCES ON FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append("                  INNER JOIN FICHA_MATRICULA FCMT ON FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append("                  INNER JOIN COMPROBANTE_PAGO CMPA ON FCMT.FCMT_ID = CMPA.FCMT_ID ");
		sql.append("                  INNER JOIN DETALLE_MATRICULA DTMT ON CMPA.CMPA_ID = DTMT.CMPA_ID ");
		sql.append("                  INNER JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  INNER JOIN RECORD_ESTUDIANTE RCES ON FCES.FCES_ID = RCES.FCES_ID  AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR MLCR ON MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append("                  LEFT JOIN CARRERA CRR ON CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append("                  LEFT JOIN DEPENDENCIA DPN ON DPN.DPN_ID = CRR.DPN_ID ");
		sql.append("                  LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append("                  LEFT JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append("                  LEFT JOIN NIVEL NVL ON NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append("                  LEFT JOIN CONFIGURACION_CARRERA CNCR ON CNCR.CRR_ID = CRR.CRR_ID ");
		sql.append("                  LEFT JOIN CALIFICACION CLF ON RCES.RCES_ID = CLF.RCES_ID ");
		sql.append("                  LEFT JOIN MODALIDAD MDL ON MDL.MDL_ID  = CNCR.MDL_ID ");
		sql.append("                  LEFT JOIN MATERIA MTR ON MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append(" WHERE CRR.CRR_TIPO = " + CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
		sql.append(" AND NVL.NVL_NUMERAL IN (" + NivelConstantes.NIVEL_CUARTO_VALUE + "," + NivelConstantes.NUMERAL_APROBACION_VALUE + ")") ;
		sql.append(" AND RCES.RCES_ESTADO IN (" + RecordEstudianteConstantes.ESTADO_APROBADO_VALUE + "," + RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE + ")");
		sql.append(" AND PRAC.PRAC_ID = ? ");
		sql.append(" ORDER BY  CRR.CRR_TIPO,  PRAC.PRAC_ID, CRR.CRR_DESCRIPCION, NVL.NVL_NUMERAL, MTR.MTR_CODIGO ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoIdiomas(rs));
			}

		} catch (Exception e) {
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MatriculaServicioJdbcImpl.buscar.record.academico.sau.error.tipo.sql")));
		} finally {
			try {
				if (rs != null) {
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

		if (retorno.isEmpty()) {
			throw new RecordEstudianteNoEncontradoException();
		}

		return retorno;
	}
	
	public List<PersonaDto> cargarEstudiantesAprobadosIdiomasPorCarrera(int periodoId, int carreraId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
	
		List<PersonaDto> retorno = new ArrayList<>();
		
		List<RecordEstudianteDto> aprobados = buscarEstudiantesAprobadosIdiomasSIIU(periodoId);
		for (RecordEstudianteDto item : aprobados) {
			
			PersonaDto estudiante = new PersonaDto();
			estudiante.setCrrDetalle(item.getRcesCarreraDto().getCrrDescripcion());
			estudiante.setPrsIdentificacion(item.getRcesEstudianteDto().getPrsIdentificacion());
			estudiante.setPrsPrimerApellido(item.getRcesEstudianteDto().getPrsPrimerApellido());
			estudiante.setPrsSegundoApellido(item.getRcesEstudianteDto().getPrsSegundoApellido());
			estudiante.setPrsNombres(item.getRcesEstudianteDto().getPrsNombres());
			estudiante.setFcinObservacion(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);
			
			try {
				List<PersonaDto> fichas = servJdbcPersonaDto.buscarEstudiantePorIdentificacionCarrera(item.getRcesEstudianteDto().getPrsIdentificacion(), carreraId);
				estudiante.setCrrDescripcion(fichas.get(fichas.size()-1).getCrrDescripcion());
				estudiante.setPrsMailInstitucional(fichas.get(fichas.size()-1).getPrsMailInstitucional());
				retorno.add(estudiante);
			} catch (Exception e) {
				try {
					servJdbcPersonaDto.buscarEstudiantePorIdentificacion(item.getRcesEstudianteDto().getPrsIdentificacion());
				} catch (Exception e1) {
					estudiante.setCrrDescripcion("EGRESADO");
					estudiante.setPrsMailInstitucional("");
					retorno.add(estudiante);
				}
			}
			
		}
		
		return retorno;
	}

	private RecordEstudianteDto transformarResultSetAhistorialRecordAcademicoIdiomas(ResultSet rs) throws SQLException {
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		PersonaDto estudiante = new PersonaDto();
		FichaMatriculaDto fichaMatricula = new FichaMatriculaDto();
		ModalidadDto modalidad = new ModalidadDto();
		MateriaDto materia = new MateriaDto();
		NivelDto nivel = new NivelDto();
		ParaleloDto paralelo = new ParaleloDto();
		CalificacionDto calificacion = new CalificacionDto();
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		
		dependencia.setDpnId(rs.getInt(3));
		dependencia.setDpnDescripcion(rs.getString(4));

		carrera.setCrrId(rs.getInt(5));
		carrera.setCrrDescripcion(rs.getString(6));
		carrera.setCrrTipo(rs.getInt(7));
		carrera.setCrrProceso(rs.getInt(8));
		carrera.setCrrAcceso(false);

		estudiante.setPrsId(rs.getInt(9));
		estudiante.setPrsIdentificacion(rs.getString(10));
		estudiante.setPrsPrimerApellido(rs.getString(11));
		estudiante.setPrsSegundoApellido(rs.getString(12));
		estudiante.setPrsNombres(rs.getString(13));
		
		fichaMatricula.setFcmtId(rs.getInt(14));
		fichaMatricula.setFcmtNivelUbicacion(rs.getInt(15));
		fichaMatricula.setFcmtTipo(rs.getInt(16)); 
		modalidad.setMdlDescripcion(rs.getString(17));
		fichaMatricula.setPracId(rs.getInt(18));
		
		materia.setMtrId(rs.getInt(19));
		materia.setMtrCodigo(rs.getString(20));
		materia.setMtrDescripcion(rs.getString(21));
		materia.setMtrCreditos(rs.getInt(22));
		materia.setMtrHoras(rs.getInt(23));
		materia.setNumMatricula(rs.getInt(24));
		
		nivel.setNvlId(rs.getInt(25));
		nivel.setNvlNumeral(rs.getInt(26));
		nivel.setNvlDescripcion(rs.getString(27));
		
		retorno.setRcesId(rs.getInt(28));
		retorno.setRcesEstado(rs.getInt(29));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(30));
		
		paralelo.setPrlId(rs.getInt(31));
		paralelo.setPrlCodigo(rs.getString(32));
		paralelo.setPracDescripcion(rs.getString(33));
		
		calificacion.setClfId(rs.getInt(34));
		calificacion.setClfNota1(rs.getBigDecimal(35));
		calificacion.setClfNota2(rs.getBigDecimal(36));
		calificacion.setClfSupletorio(rs.getBigDecimal(37));
		calificacion.setClfSumaNotas(rs.getBigDecimal(38));
		calificacion.setClfParamRecuperacion1(rs.getBigDecimal(39));
		calificacion.setClfParamRecuperacion2(rs.getBigDecimal(40));
		calificacion.setClfPromedioNotas(rs.getBigDecimal(41));
		calificacion.setClfAsistencia1(rs.getBigDecimal(42));
		calificacion.setClfAsistencia2(rs.getBigDecimal(43));
		calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(44));
		calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(45));
		calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(46)); 
		
		retorno.setRcesPracId(periodo.getPracId());
		retorno.setRcesPracDescripcion(periodo.getPracDescripcion());
		retorno.setMtrCodigo(materia.getMtrCodigo());
		fichaMatricula.setPrlCodigo(paralelo.getPrlCodigo());
		fichaMatricula.setPrlDescripcion(paralelo.getPrlDescripcion());
		materia.setMtrEstado(retorno.getRcesEstado()); // estado del record

		retorno.setRcesPeriodoAcademicoDto(periodo);
		retorno.setRcesDependenciaDto(dependencia);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesFichaMatriculaDto(fichaMatricula);
		retorno.setRcesModalidadDto(modalidad);
		retorno.setRcesMateriaDto(materia);
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesNivelDto(nivel);
		retorno.setRcesParaleloDto(paralelo);
		retorno.setRcesCalificacionDto(calificacion);
		return retorno;
	}


	
	
}
