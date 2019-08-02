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
   
 ARCHIVO:     MatriculaServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de Matricula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
23Feb2018             Freddy Guzmán                       Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.FichaEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.GratuidadServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
/**
 * EJB MatriculaServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de MatriculaServicioJdbc.
 * @author fgguzman
 * @version 1.0
 */

@Stateless
public class GratuidadServicioJdbcImpl implements GratuidadServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@Resource(lookup=GeneralesConstantes.APP_DATA_SOURCE_SAU)
	private DataSource dsSau;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	public List<RecordEstudianteSAUDto> buscarRecordSAU(String prsIdentificacion) throws GratuidadNoEncontradoException, GratuidadException {
		List<RecordEstudianteSAUDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT per_descripcion AS PERIODO,  ");
		sql.append("   nombre,  ");
		sql.append("   espe_nombre CARRERA,  ");
		sql.append("   est_cedula CEDULA,  ");
		sql.append("   est_apellido_paterno APELLIDO_PATERNO,  ");
		sql.append("   est_apellido_materno APELLIDO_MATERNO,  ");
		sql.append("   est_nombres NOMBRES,  ");
		sql.append("   malla MALLA,  ");
		sql.append("   COALESCE (total_creditos_reprobados,0) AS total_creditos_reprobados,  ");
		sql.append("   COALESCE (creditos_malla, 0) AS creditos_malla,  ");
		sql.append("   (COALESCE (total_creditos_reprobados,0) * 100 / (COALESCE(creditos_malla, 0)) ) AS porcentaje,  ");
		sql.append("   CASE  ");
		sql.append("     WHEN ( COALESCE (total_creditos_reprobados, 0 ) * 100 / (COALESCE(creditos_malla, 0)) ) :: DECIMAL >= 30  ");
		sql.append("     THEN 'PIERDE GRATUIDAD'  ");
		sql.append("     ELSE 'MANTIENE GRATUIDAD'  ");
		sql.append("   END AS Observacion  ");
		sql.append(" FROM  ");
		sql.append("   (SELECT DISTINCT p1.per_descripcion,  ");
		sql.append("     f1.nombre,  ");
		sql.append("     ee1.espe_nombre,  ");
		sql.append("     e1.est_cedula,  ");
		sql.append("     TRIM (e1.est_apellido_paterno) AS est_apellido_paterno,  ");
		sql.append("     TRIM (e1.est_apellido_materno) AS est_apellido_materno,  ");
		sql.append("     TRIM (e1.est_nombres) AS est_nombres,  ");
		sql.append("     mtr1.matr_curso,  ");
		sql.append("     (SELECT * FROM obtener_malla (f1. ID,ee1.espe_codigo,p1.grupo,e1.est_cedula)) malla,  ");
		sql.append("     (SELECT DISTINCT SUM (mat2.mat_numero_creditos) FROM registro_materias rm2  ");
		sql.append("     LEFT JOIN materias mat2 ON rm2.mat_codigo = mat2.mat_codigo  ");
		sql.append("     LEFT JOIN matriculas mtr2 ON rm2.per_codigo  = mtr2.per_codigo  ");
		sql.append("     AND rm2.est_codigo = mtr2.est_codigo LEFT JOIN especialidades_escuela ee2   ON mtr2.espe_codigo  = ee2.espe_codigo  AND mat2.espe_codigo = ee2.espe_codigo  ");
		sql.append("     LEFT JOIN escuelas s2  ON ee2.esc_codigo = s2.esc_codigo LEFT JOIN facultad f2  ");
		sql.append("     ON s2.id_facultad = f2. ID LEFT JOIN periodos p2  ");
		sql.append("     ON mtr2.per_codigo = p2.per_codigo LEFT JOIN estudiantes e2  ");
		sql.append("     ON mtr2.est_codigo = e2.est_codigo  ");
		sql.append("     WHERE f2. ID = f1. ID  ");
		sql.append("     AND p2.grupo  <= (p1.grupo)  ");
		sql.append("     AND rm2.rmat_aprobado IN (4)  ");
		sql.append("     AND mtr2.matr_curso > 0  ");
		sql.append("     AND ee2.espe_codigo = ee1.espe_codigo  ");
		sql.append("     AND e2.est_cedula = e1.est_cedula  ");
		sql.append("     AND mat2.malla_curricular_id =  (SELECT *FROM obtener_malla (f1. ID,ee1.espe_codigo,p1.grupo,e1.est_cedula))  ");
		sql.append("     ) AS total_creditos_reprobados,  ");
		sql.append("     (SELECT SUM (mcc.numero_creditos)  ");
		sql.append("     FROM malla_curricular_creditos mcc  ");
		sql.append("     LEFT JOIN malla_curricular mc  ");
		sql.append("     ON mc. ID = mcc.malla_curricular_id  ");
		sql.append("     LEFT JOIN escuelas esc  ");
		sql.append("     ON esc.esc_codigo = mc.esc_codigo  ");
		sql.append("     LEFT JOIN especialidades_escuela espe  ");
		sql.append("     ON espe.espe_codigo           = mc.espe_codigo  ");
		sql.append("     WHERE mcc.malla_curricular_id = (SELECT * FROM obtener_malla (f1. ID,ee1.espe_codigo,p1.grupo, e1.est_cedula ))) :: DECIMAL AS creditos_malla  ");
		sql.append("   FROM registro_materias rm1  ");
		sql.append("   LEFT JOIN materias mat1  ");
		sql.append("   ON rm1.mat_codigo = mat1.mat_codigo  ");
		sql.append("   LEFT JOIN matriculas mtr1  ");
		sql.append("   ON rm1.per_codigo  = mtr1.per_codigo  ");
		sql.append("   AND rm1.est_codigo = mtr1.est_codigo  ");
		sql.append("   LEFT JOIN periodos p1  ");
		sql.append("   ON mtr1.per_codigo = p1.per_codigo  ");
		sql.append("   LEFT JOIN especialidades_escuela ee1  ");
		sql.append("   ON mtr1.espe_codigo  = ee1.espe_codigo  ");
		sql.append("   AND mat1.espe_codigo = ee1.espe_codigo  ");
		sql.append("   LEFT JOIN escuelas s1  ");
		sql.append("   ON ee1.esc_codigo = s1.esc_codigo  ");
		sql.append("   LEFT JOIN facultad f1  ");
		sql.append("   ON s1.id_facultad = f1. ID  ");
		sql.append("   LEFT JOIN estudiantes e1  ");
		sql.append("   ON mtr1.est_codigo     = e1.est_codigo  ");
		sql.append("   WHERE f1.id NOT       IN (21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,37,38,41,42,43,43,45,46)  ");
		sql.append("   AND s1.esc_codigo NOT IN (- 1, 5, 6, 60, 980, 1655, 79)  ");
		sql.append("   AND mtr1.matr_curso    > 0  ");
		sql.append("   AND mtr1.matr_estado   > 1  ");
		sql.append("   AND e1.est_cedula      = '"+prsIdentificacion.toUpperCase()+"'  ");
		sql.append("   AND p1.per_descripcion NOT ilike 'con%'  ");
		sql.append("   ORDER BY p1.per_descripcion  ");
		sql.append("   ) AS matriculados  ");
		sql.append(" ORDER BY periodo   ");
		
		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSauDto(rs));
			}
			
		} catch (Exception e) {
			throw new GratuidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudianteSAUDto.buscar.record.academico.sau.error.tipo.sql")));
		} finally {
			try {
				if (rs != null) {
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

		if (retorno == null || retorno.size() <= 0) {
			throw new GratuidadNoEncontradoException("No se ha encontrado Record Estudiantil para la identificación ingresada.");
		}

		return retorno;
	}

	public boolean buscarSegundaCarrera(String prsIdentificacion) throws GratuidadNoEncontradoException, GratuidadException{
		Boolean retorno = Boolean.FALSE;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PRS.PRS_ID  ");
		sql.append(" ,PRS.PRS_IDENTIFICACION  ");
		sql.append(" ,PRS.PRS_PRIMER_APELLIDO  ");
		sql.append(" ,PRS.PRS_SEGUNDO_APELLIDO  ");
		sql.append(" ,PRS.PRS_NOMBRES  ");
		sql.append(" ,CRR.CRR_ID  ");
		sql.append(" ,CRR.CRR_DESCRIPCION  ");
		sql.append(" ,CRR.CRR_DETALLE  ");
		sql.append(" ,FCIN.FCIN_ID  ");
		sql.append(" ,FCIN.FCIN_TIPO_INGRESO  ");
		sql.append(" ,FCES.FCES_ID  ");
		sql.append(" ,FCES.FCES_UNIV_ESTUD_PREV_ID  ");
		sql.append(" FROM FICHA_INSCRIPCION FCIN,   ");
		sql.append(" FICHA_ESTUDIANTE FCES,   ");
		sql.append(" USUARIO_ROL USRO,   ");
		sql.append(" USUARIO USR,  ");
		sql.append(" PERSONA PRS,  ");
		sql.append(" CARRERA CRR,  ");
		sql.append(" CONFIGURACION_CARRERA CNCR  ");
		sql.append(" WHERE FCIN.FCIN_ID = FCES.FCIN_ID  ");
		sql.append(" AND PRS.PRS_ID = USR.PRS_ID  ");
		sql.append(" AND USRO.USRO_ID = FCIN.USRO_ID  ");
		sql.append(" AND USR.USR_ID = USRO.USR_ID  ");
		sql.append(" AND CNCR.CNCR_ID = FCIN.CNCR_ID  ");
		sql.append(" AND CRR.CRR_ID = CNCR.CRR_ID  ");
		sql.append(" AND FCES.FCES_TIPO_UNIV_ESTUD_PREV = ");sql.append(FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE);
		sql.append(" AND PRS.PRS_IDENTIFICACION = ?  ");

		List<RecordEstudianteDto> record = null;
		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, prsIdentificacion.toUpperCase());
			rs = pstmt.executeQuery();

			record = new ArrayList<>();
			while (rs.next()) {
				record.add(transformarResultSetArecordEstudianteDto(rs));
			}
			
		} catch (Exception e) {
			throw new GratuidadException("Error al buscar perdida de gratuidad por segunda carrera despues de titulado.");
		} finally {
			try {
				if (rs != null) {
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

		if (record.isEmpty()) {
			throw new GratuidadNoEncontradoException("El estudiante no tiene registro de segunda carrera despues de haber culminado una carreara.");
		}else {
			retorno = Boolean.TRUE; 
		}

		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	
	private RecordEstudianteSAUDto transformarResultSetArecordAcademicoSauDto(ResultSet rs) throws SQLException{
		RecordEstudianteSAUDto retorno = new RecordEstudianteSAUDto();
		PeriodoAcademicoDto  periodoAcademico = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		MallaCurricularDto malla = new MallaCurricularDto();
		PersonaDto estudiante = new PersonaDto();
		periodoAcademico.setPracDescripcion(rs.getString(1));
		dependencia.setDpnDescripcion(rs.getString(2));
		carrera.setCrrDescripcion(rs.getString(3));
		estudiante.setPrsIdentificacion(rs.getString(4));
		estudiante.setPrsPrimerApellido(rs.getString(5));
		estudiante.setPrsSegundoApellido(rs.getString(6));
		estudiante.setPrsNombres(rs.getString(7));
		malla.setMlcrId(rs.getInt(8));
		malla.setMlcrCreditosReprobados(rs.getInt(9));
		malla.setMlcrTotalCreditos(rs.getInt(10));
		malla.setMlcrPorcentajeReprobado(rs.getBigDecimal(11));
		malla.setMlcrObservacion(rs.getString(12));
		retorno.setRcesMallaCurricularDto(malla);
		retorno.setRcesPeriodoAcademicoDto(periodoAcademico);
		retorno.setRcesDependenciaDto(dependencia);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesEstudiante(estudiante);
		return retorno;
	}
	
	

	private RecordEstudianteDto transformarResultSetArecordEstudianteDto(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PersonaDto estudiante = new PersonaDto();
		CarreraDto carrera = new CarreraDto();
		FichaInscripcionDto fichaInscripcion = new FichaInscripcionDto();
		FichaEstudianteDto fichaEstudiante = new FichaEstudianteDto();
		estudiante.setPrsId(rs.getInt(1));
		estudiante.setPrsIdentificacion(rs.getString(2));
		estudiante.setPrsPrimerApellido(rs.getString(3));
		estudiante.setPrsSegundoApellido(rs.getString(4));
		estudiante.setPrsNombres(rs.getString(5));
		carrera.setCrrId(rs.getInt(6));
		carrera.setCrrDescripcion(rs.getString(7));
		carrera.setCrrDetalle(rs.getString(8));
		fichaInscripcion.setFcinId(rs.getInt(9));
		fichaInscripcion.setFcinTipoIngreso(rs.getInt(10));
		fichaEstudiante.setFcesId(rs.getInt(11));
		fichaEstudiante.setFcesInstitucionAcademicaId(rs.getInt(12));
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesFichaInscripcionDto(fichaInscripcion);
		retorno.setRcesFichaEstudianteDto(fichaEstudiante);
		return retorno;
	}
	
}

