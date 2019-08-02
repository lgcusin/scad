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
import java.util.Arrays;
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
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HistorialAcademicoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
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
public class HistorialAcademicoJdbcImpl implements HistorialAcademicoServicioJdbc {

	@Resource(mappedName = GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@Resource(lookup = GeneralesConstantes.APP_DATA_SOURCE_SAU)
	private DataSource dsSau;
	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@EJB private MateriaServicio servMateriaServicio;


	
	public List<RecordEstudianteDto> buscarHistorialAcademicoSAUHomologado(String identificacion, Integer[] mtrEstados) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT p.grupo prac_id, ");
		sql.append("   p.per_descripcion prac_descripcion, ");
		sql.append("   f.id dpn_id, ");
		sql.append("   f.nombre dpn_descripcion, ");
		sql.append("   ee.espe_codigo crr_id, ");
		sql.append("   ee.espe_nombre crr_descripcion, ");
		sql.append("   -99 crr_tipo, ");
		sql.append("   -99 crr_proceso, ");
		sql.append("   -99 crr_espe_codigo, ");
		sql.append("   e.est_codigo prs_id, ");
		sql.append("   e.est_cedula prs_identificacion, ");
		sql.append("   e.est_apellido_paterno prs_primer_apellido, ");
		sql.append("   e.est_apellido_materno prs_segundo_apellido, ");
		sql.append("   e.est_nombres prs_nombres, ");
		sql.append("   mtr.matr_codigo fcmt_id, ");
		sql.append("   mtr.matr_curso fcmt_nivel_ubicacion, ");
		sql.append("   mtr.matr_tipo fcmt_tipo, ");
		sql.append("   CASE mtr.modalidad ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'PRESENCIAL' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'DISTANCIA' ");
		sql.append("   END mdl_descripcion, ");
		sql.append("   p.per_codigo fcmt_prac_id, ");
		sql.append("   mat.mat_codigo mtr_id, ");
		sql.append("   mat.mat_codigo_uce mtr_codigo, ");
		sql.append("   mat.mat_nombre mtr_descripcion, ");
		sql.append("   mat.mat_numero_creditos mtr_creditos, ");
		sql.append("   -99 mtr_horas, ");
		sql.append("   -99 mtr_horas_prac_sema, ");
		sql.append("   rm.numero_matricula dtmt_numero, ");
		sql.append("   -99 nvl_id, ");
		sql.append("   mat.mat_curso nvl_numeral, ");
		sql.append("   '' nvl_descrpcion, ");
		sql.append("   rm.rmat_codigo rces_id, ");
		sql.append("   rm.rmat_aprobado rces_estado, ");
		sql.append("   -99 mlcrpr_id, ");
		sql.append("   -99 prl_id, ");
		sql.append("   rm.rmat_paralelo prl_codigo, ");
		sql.append("   hp.descripcion prl_descripcion, ");
		sql.append("   -99 clf_id, ");
		sql.append("   rm.rmat_nota_uno clf_nota_1, ");
		sql.append("   rm.rmat_nota_dos clf_nota_2, ");
		sql.append("   rm.rmat_nota_tres clf_supletorio, ");
		sql.append("   -99 clf_suma_p1_p2, ");
		sql.append("   -99 clf_param_recuperacion1, ");
		sql.append("   -99 clf_param_recuperacion2, ");
		sql.append("   rm.nota_final clf_nota_final_semestre, ");
		sql.append("   rmat_asistencia_uno clf_asistencia1, ");
		sql.append("   rmat_asistencia_dos clf_asistencia2, ");
		sql.append("   rmat_asistencia_total_uno clf_asistencia_docente1, ");
		sql.append("   rmat_asistencia_total_dos clf_asistencia_docente2, ");
		sql.append("   rm.asistencia_final clf_promedio_asistencia ");
		sql.append(" FROM registro_materias rm ");
		sql.append(" INNER JOIN materias mat ");
		sql.append(" ON rm.mat_codigo = mat.mat_codigo ");
		sql.append(" INNER JOIN matriculas mtr ");
		sql.append(" ON rm.per_codigo  = mtr.per_codigo ");
		sql.append(" AND rm.est_codigo = mtr.est_codigo ");
		sql.append(" INNER JOIN periodos P ");
		sql.append(" ON mtr.per_codigo = P.per_codigo ");
		sql.append(" INNER JOIN estudiantes e ");
		sql.append(" ON mtr.est_codigo = e.est_codigo ");
		sql.append(" INNER JOIN especialidades_escuela ee ");
		sql.append(" ON mtr.espe_codigo = ee.espe_codigo ");
		sql.append(" INNER JOIN escuelas s ");
		sql.append(" ON ee.esc_codigo = s.esc_codigo ");
		sql.append(" AND s.esc_codigo = s.esc_codigo ");
		sql.append(" INNER JOIN facultad f ");
		sql.append(" ON s.id_facultad = f. ID ");
		sql.append(" INNER JOIN facultad_autoridades fa ");
		sql.append(" ON fa.facultad_id = f.id ");
		sql.append(" INNER JOIN malla_curricular mc ");
		sql.append(" ON ee.espe_codigo           = mc.espe_codigo ");
		sql.append(" AND mat.malla_curricular_id = mc.id ");
		sql.append(" INNER JOIN malla_curricular_creditos mcc ");
		sql.append(" ON mc.id = mcc.malla_curricular_id ");
		sql.append(" LEFT JOIN hor_materia hm ");
		sql.append(" ON rm.hormateria_id= hm.id ");
		sql.append(" LEFT JOIN hor_paralelo hp ");
		sql.append(" ON hm.id_paralelo       = hp.id ");
		sql.append(" WHERE s.esc_codigo NOT IN (-1, 5, 6, 60, 980, 165) ");
		sql.append(" AND mtr.matr_estado     > 1 ");
		sql.append(" AND mat.mat_curso       > 0 ");
		sql.append(" AND mat.mat_estado     IN (1) ");
		sql.append(" AND e.est_cedula        = '"+identificacion+"' ");
		sql.append(" AND mat.mat_estado in ");sql.append(Arrays.toString(mtrEstados).replace("[", "(").replace("]", ")"));
		sql.append(" GROUP BY P.per_descripcion, ");
		sql.append("   f.nombre, ");
		sql.append("   ee.espe_nombre, ");
		sql.append("   f.id, ");
		sql.append("   ee.espe_codigo, ");
		sql.append("   mtr.modalidad, ");
		sql.append("   mtr.matr_curso, ");
		sql.append("   prs_nombres, ");
		sql.append("   mat.mat_codigo_uce, ");
		sql.append("   mat.mat_nombre, ");
		sql.append("   rm.rmat_aprobado, ");
		sql.append("   mtr.pkv_folio, ");
		sql.append("   mtr.pkv_folio_indice, ");
		sql.append("   rm.rmat_nota_uno, ");
		sql.append("   rm.rmat_nota_dos, ");
		sql.append("   rm.rmat_nota_tres, ");
		sql.append("   rm.nota_final, ");
		sql.append("   rm.asistencia_final, ");
		sql.append("   mat.mat_numero_creditos, ");
		sql.append("   rm.numero_matricula, ");
		sql.append("   e.est_codigo, ");
		sql.append("   e.est_cedula, ");
		sql.append("   e.est_apellido_paterno, ");
		sql.append("   e.est_apellido_materno, ");
		sql.append("   mtr.matr_codigo, ");
		sql.append("   p.grupo, ");
		sql.append("   rm.rmat_asistencia_uno , ");
		sql.append("   rm.rmat_asistencia_dos, ");
		sql.append("   rm.rmat_asistencia_total_uno, ");
		sql.append("   rm.rmat_asistencia_total_dos, ");
		sql.append("   mtr.matr_tipo, ");
		sql.append("   mat.mat_curso, ");
		sql.append("   mat.mat_codigo, ");
		sql.append("   rm.rmat_paralelo, ");
		sql.append("   hp.descripcion, ");
		sql.append("   p.per_codigo, ");
		sql.append("   p.per_descripcion, ");
		sql.append("   f.id, ");
		sql.append("   rm.rmat_codigo ");
		sql.append(" ORDER BY prs_nombres, ");
		sql.append("   prac_descripcion, ");
		sql.append("   mat.mat_codigo_uce, ");
		sql.append("   e.est_cedula, ");
		sql.append("   mat.mat_nombre, ");
		sql.append("   mat.mat_codigo  ");

		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoHomologadoDto(rs, RecordEstudianteConstantes.RCES_ORIGEN_SAU));
			}
			
		} catch (Exception e) {
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudianteSAUDto.buscar.record.academico.sau.error.tipo.sql")));
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
		
		if (retorno.isEmpty()) {
			throw new RecordEstudianteNoEncontradoException();
		}
		
		return retorno;
	}
	




	@Override
	public List<RecordEstudianteDto> buscarHistorialMatriculaSAU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ID, ");
		sql.append("   FACULTAD, ");
		sql.append("   CODIGO_CARRERA, ");
		sql.append("   CARRERA, ");
		sql.append("   MODALIDAD, ");
		sql.append("   SEMESTRE_ET, ");
		sql.append("   SEMESTRE, ");
		sql.append("   PARALELO, ");
		sql.append("   DESCRPCION_PARALELO, ");
		sql.append("   CODIGO_MATRICULA, ");
		sql.append("   PERIODO_ID, ");
		sql.append("   PERIODO, ");
		sql.append("   CODIGO_ESTUDIANTIL, ");
		sql.append("   CEDULA, ");
		sql.append("   NOMBRES, ");
		sql.append("   MATERIA_ID, ");
		sql.append("   CODIGO_MATERIA, ");
		sql.append("   MATERIA, ");
		sql.append("   creditos, ");
		sql.append("   RCES_ESTADO, ");
		sql.append("   ESTADO, ");
		sql.append("   NUMERO_MATRICULA, ");
		sql.append("   TIPO_MATRICULA ");
		sql.append(" FROM ");
		sql.append("   (SELECT DISTINCT F.ID, ");
		sql.append("     F.NOMBRE FACULTAD, ");
		sql.append("     EE.ESPE_CODIGO CODIGO_CARRERA, ");
		sql.append("     EE.ESPE_NOMBRE CARRERA, ");
		sql.append("     CASE MTR.MODALIDAD ");
		sql.append("       WHEN 1 ");
		sql.append("       THEN 'PRESENCIAL' ");
		sql.append("       WHEN 2 ");
		sql.append("       THEN 'DISTANCIA' ");
		sql.append("     END AS MODALIDAD, ");
		sql.append("     MTR.MATR_CURSO SEMESTRE_ET, ");
		sql.append("     CASE MTR.MATR_CURSO ");
		sql.append("       WHEN 1 ");
		sql.append("       THEN 'PRIMER NIVEL' ");
		sql.append("       WHEN 2 ");
		sql.append("       THEN 'SEGUNDO NIVEL' ");
		sql.append("       WHEN 3 ");
		sql.append("       THEN 'TERCER NIVEL' ");
		sql.append("       WHEN 4 ");
		sql.append("       THEN 'CUARTO NIVEL' ");
		sql.append("       WHEN 5 ");
		sql.append("       THEN 'QUINTO NIVEL' ");
		sql.append("       WHEN 6 ");
		sql.append("       THEN 'SEXTO NIVEL' ");
		sql.append("       WHEN 7 ");
		sql.append("       THEN 'SÉPTIMO NIVEL' ");
		sql.append("       WHEN 8 ");
		sql.append("       THEN 'OCTAVO NIVEL' ");
		sql.append("       WHEN 9 ");
		sql.append("       THEN 'NOVENO NIVEL' ");
		sql.append("       WHEN 10 ");
		sql.append("       THEN 'DÉCIMO NIVEL' ");
		sql.append("       WHEN 11 ");
		sql.append("       THEN 'DÉCIMO PRIMER NIVEL' ");
		sql.append("     END SEMESTRE, ");
		sql.append("     rm.rmat_paralelo paralelo, ");
		sql.append("     hp.descripcion DESCRPCION_PARALELO, ");
		sql.append("     MTR.MATR_CODIGO   AS CODIGO_MATRICULA, ");
		sql.append("     P.GRUPO           AS PERIODO_ID, ");
		sql.append("     P.PER_DESCRIPCION AS PERIODO, ");
		sql.append("     E.EST_CODIGO      AS CODIGO_ESTUDIANTIL, ");
		sql.append("     E.EST_CEDULA CEDULA, ");
		sql.append("     E.EST_APELLIDO_PATERNO ");
		sql.append("     || ' ' ");
		sql.append("     || E.EST_APELLIDO_MATERNO ");
		sql.append("     || ' ' ");
		sql.append("     || E.EST_NOMBRES   AS NOMBRES, ");
		sql.append("     MAT.MAT_CODIGO     AS MATERIA_ID, ");
		sql.append("     MAT.MAT_CODIGO_UCE AS CODIGO_MATERIA, ");
		sql.append("     MAT.MAT_NOMBRE     AS MATERIA, ");
		sql.append("     mat.mat_numero_creditos creditos, ");
		sql.append("     RM.RMAT_APROBADO AS RCES_ESTADO, ");
		sql.append("     CASE RM.RMAT_APROBADO ");
		sql.append("       WHEN -1 ");
		sql.append("       THEN 'ANULADO' ");
		sql.append("       WHEN 0 ");
		sql.append("       THEN 'DESCONOCIDO' ");
		sql.append("       WHEN 1 ");
		sql.append("       THEN 'INSCRITO' ");
		sql.append("       WHEN 2 ");
		sql.append("       THEN 'MATRÍCULADO' ");
		sql.append("       WHEN 3 ");
		sql.append("       THEN 'APROBADO' ");
		sql.append("       WHEN 4 ");
		sql.append("       THEN 'REPROBADO' ");
		sql.append("       WHEN 5 ");
		sql.append("       THEN 'NO REGISTRA NOTA' ");
		sql.append("       WHEN 6 ");
		sql.append("       THEN 'SUSPENSO' ");
		sql.append("       WHEN 7 ");
		sql.append("       THEN 'PÉRDIDO POR ASISTENCIA' ");
		sql.append("       WHEN 10 ");
		sql.append("       THEN 'CONVALIDADO' ");
		sql.append("       ELSE 'DESCONOCIDO' ");
		sql.append("     END                 AS ESTADO, ");
		sql.append("     RM.NUMERO_MATRICULA AS NUMERO_MATRICULA, ");
		sql.append("     CASE ");
		sql.append("       WHEN MTR.MATR_TIPO = 1 ");
		sql.append("       THEN 'ORDINARIA' ");
		sql.append("       WHEN MTR.MATR_TIPO = 2 ");
		sql.append("       THEN 'EXTRAORDINARIA' ");
		sql.append("       WHEN MTR.MATR_TIPO = 3 ");
		sql.append("       THEN 'ESPECIAL' ");
		sql.append("     END TIPO_MATRICULA ");
		sql.append("   FROM REGISTRO_MATERIAS RM ");
		sql.append("   INNER JOIN MATERIAS MAT ");
		sql.append("   ON RM.MAT_CODIGO = MAT.MAT_CODIGO ");
		sql.append("   INNER JOIN MATRICULAS MTR ");
		sql.append("   ON RM.PER_CODIGO  = MTR.PER_CODIGO ");
		sql.append("   AND RM.EST_CODIGO = MTR.EST_CODIGO ");
		sql.append("   INNER JOIN PERIODOS P ");
		sql.append("   ON MTR.PER_CODIGO = P.PER_CODIGO ");
		sql.append("   INNER JOIN ESTUDIANTES E ");
		sql.append("   ON MTR.EST_CODIGO = E.EST_CODIGO ");
		sql.append("   INNER JOIN ESPECIALIDADES_ESCUELA EE ");
		sql.append("   ON MTR.ESPE_CODIGO = EE.ESPE_CODIGO ");
		sql.append("   INNER JOIN ESCUELAS S ");
		sql.append("   ON EE.ESC_CODIGO = S.ESC_CODIGO ");
		sql.append("   AND S.ESC_CODIGO = S.ESC_CODIGO ");
		sql.append("   INNER JOIN FACULTAD F ");
		sql.append("   ON S.ID_FACULTAD = F. ID ");
		sql.append("   INNER JOIN FACULTAD_AUTORIDADES FA ");
		sql.append("   ON FA.FACULTAD_ID = F.ID ");
		sql.append("   LEFT JOIN hor_materia hm ");
		sql.append("   ON rm.hormateria_id= hm.id ");
		sql.append("   LEFT JOIN hor_paralelo hp ");
		sql.append("   ON hm.id_paralelo       = hp.id ");
		sql.append("   WHERE S.ESC_CODIGO NOT IN (-1, 5, 6, 60, 980, 165) ");
		sql.append("   AND MTR.MATR_ESTADO     > 1 ");
		sql.append("   AND MAT.MAT_CURSO       > 0 ");
		sql.append("   AND MAT.MAT_ESTADO     IN (1) ");
		sql.append("   AND E.EST_CEDULA        = '"+identificacion+"' ");
		sql.append("   GROUP BY P.PER_DESCRIPCION, ");
		sql.append("     F.NOMBRE, ");
		sql.append("     EE.ESPE_NOMBRE, ");
		sql.append("     F.ID, ");
		sql.append("     EE.ESPE_CODIGO, ");
		sql.append("     MTR.MODALIDAD, ");
		sql.append("     MTR.MATR_CURSO, ");
		sql.append("     NOMBRES, ");
		sql.append("     MAT.MAT_CODIGO_UCE, ");
		sql.append("     MAT.MAT_NOMBRE, ");
		sql.append("     RM.RMAT_APROBADO, ");
		sql.append("     RM.NUMERO_MATRICULA, ");
		sql.append("     E.EST_CODIGO, ");
		sql.append("     E.EST_CEDULA, ");
		sql.append("     MTR.MATR_CODIGO, ");
		sql.append("     P.GRUPO, ");
		sql.append("     MTR.MATR_TIPO, ");
		sql.append("     MAT.MAT_CURSO, ");
		sql.append("     MAT.MAT_CODIGO, ");
		sql.append("     hp.descripcion, ");
		sql.append("     rm.rmat_paralelo, ");
		sql.append("     mat.mat_numero_creditos ");
		sql.append("   ORDER BY NOMBRES, ");
		sql.append("     P.PER_DESCRIPCION, ");
		sql.append("     MAT.MAT_CODIGO_UCE, ");
		sql.append("     E.EST_CEDULA, ");
		sql.append("     MAT.MAT_NOMBRE, ");
		sql.append("     MAT.MAT_CODIGO ");
		sql.append("   ) CERTIFICADO ");
		sql.append(" GROUP BY ID, ");
		sql.append("   FACULTAD, ");
		sql.append("   CARRERA, ");
		sql.append("   MODALIDAD, ");
		sql.append("   SEMESTRE, ");
		sql.append("   SEMESTRE_ET, ");
		sql.append("   PERIODO_ID, ");
		sql.append("   PERIODO, ");
		sql.append("   CODIGO_ESTUDIANTIL, ");
		sql.append("   CEDULA, ");
		sql.append("   NOMBRES, ");
		sql.append("   CODIGO_MATERIA, ");
		sql.append("   MATERIA, ");
		sql.append("   ESTADO, ");
		sql.append("   CODIGO_CARRERA, ");
		sql.append("   NUMERO_MATRICULA, ");
		sql.append("   CODIGO_MATRICULA, ");
		sql.append("   TIPO_MATRICULA, ");
		sql.append("   MATERIA_ID, ");
		sql.append("   RCES_ESTADO, ");
		sql.append("   PARALELO, ");
		sql.append("   DESCRPCION_PARALELO, ");
		sql.append("   creditos ");
		sql.append(" ORDER BY CARRERA, ");
		sql.append("   PERIODO ");


		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarRShistorialMatriculaDto(rs, RecordEstudianteConstantes.RCES_ORIGEN_SAU));
			}
			
		} catch (Exception e) {
			throw new RecordEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecordEstudianteSAUDto.buscar.record.academico.sau.error.tipo.sql")));
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
			throw new RecordEstudianteNoEncontradoException();
		}
		
		return retorno;
	}
	

	public List<RecordEstudianteDto> buscarHistorialAcademicoSIIUHomologado(String identificacion, Integer[] mtrEstados) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
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
		sql.append("   CRR.CRR_ESPE_CODIGO, ");
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
		sql.append("   MTR.MTR_HORAS_PRAC_SEMA, ");
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
		sql.append(" WHERE PRS.PRS_IDENTIFICACION = '"+identificacion.toUpperCase()+"'  ");
		sql.append(" AND mtr.mtr_estado           IN ");sql.append(Arrays.toString(mtrEstados).replace("[", "(").replace("]", ")"));
		sql.append(" ORDER BY  CRR.CRR_TIPO,  PRAC.PRAC_ID, CRR.CRR_DESCRIPCION, NVL.NVL_NUMERAL, MTR.MTR_CODIGO ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoHomologadoDto(rs, RecordEstudianteConstantes.RCES_ORIGEN_SIIU));
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
	
	
	

	@Override
	public List<RecordEstudianteDto> buscarHistorialAcademicoSIIUPosgrado(String identificacion, Integer[] mtrEstados) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT dpn.dpn_descripcion , ");
		sql.append("   crr_descripcion, ");
		sql.append("   crr.crr_id, ");
		sql.append("   prac.prac_id, ");
		sql.append("   prac.prac_descripcion, ");
		sql.append("   prs.prs_id, ");
		sql.append("   prs.prs_identificacion , ");
		sql.append("   prs.prs_primer_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs_segundo_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs.prs_nombres nombres, ");
		sql.append("   mtr.mtr_id , ");
		sql.append("   mtr.mtr_codigo , ");
		sql.append("   mtr.mtr_descripcion , ");
		sql.append("   mtr.mtr_codigo , ");
		sql.append("   clf.clf_nota1 , ");
		sql.append("   clf.clf_nota2 , ");
		sql.append("   clf.clf_supletorio , ");
		sql.append("   clf.clf_nota_final_semestre , ");
		sql.append("   clf_asistencia1, ");
		sql.append("   clf_asistencia2, ");
		sql.append("   clf_asistencia_docente1, ");
		sql.append("   clf_asistencia_docente2, ");
		sql.append("   clf_param_recuperacion1, ");
		sql.append("   clf_param_recuperacion2, ");
		sql.append("   clf_suma_p1_p2, ");
		sql.append("   clf.clf_promedio_asistencia , ");
		sql.append("   mlcrmt.nvl_id, ");
		sql.append("   CASE ");
		sql.append("     WHEN rces.rces_estado =0 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =-1 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =1 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =2 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =3 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =4 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_MIGRADO_SAU_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =5 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_CONVALIDADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =6 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =7 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =8 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =9 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =10 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =11 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_SOLICITADO_LABEL);sql.append("'");
		sql.append("     WHEN rces.rces_estado =12 ");
		sql.append("     THEN '");sql.append(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_LABEL);sql.append("'");
		sql.append("   END AS ESTADO, ");
		sql.append("   crr.crr_tipo ");
		
		sql.append("   from persona prs, ficha_estudiante fces, record_estudiante rces, calificacion clf,periodo_academico prac,"
		+" malla_curricular_paralelo mlcrpr, malla_curricular_materia mlcrmt, materia mtr, paralelo prl, dependencia dpn, carrera crr"
		+" where prs.prs_id=fces.prs_id and fces.fces_id=rces.fces_id and rces.rces_id=clf.rces_id and rces.mlcrpr_id=mlcrpr.mlcrpr_id and prac.prac_id=prl.prac_id and"
		+" mlcrpr.prl_id=prl.prl_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and dpn.dpn_id=crr.dpn_id and crr.crr_id=mtr.crr_id ");		

		sql.append(" AND prs.prs_identificacion = '"+identificacion.toUpperCase()+"'  ");
		sql.append(" ORDER BY mlcrmt.nvl_id, mtr.mtr_descripcion ");
		
		try{
			
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoDtoPosgrado(rs, RecordEstudianteConstantes.RCES_ORIGEN_SIIU));
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

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
			throw new RecordEstudianteNoEncontradoException();
		}

		return retorno;
	}
	
	@Override
	public List<RecordEstudianteDto> buscarHistorialMatriculaSIIU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT dpn.dpn_id, ");
		sql.append("   dpn.dpn_descripcion , ");
		sql.append("   crr.crr_id, ");
		sql.append("   crr.crr_descripcion , ");
		sql.append("   mdl.mdl_descripcion , ");
		sql.append("   fcmt.fcmt_nivel_ubicacion, ");
		sql.append("   CASE fcmt.fcmt_nivel_ubicacion ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'PRIMER NIVEL' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'SEGUNDO NIVEL' ");
		sql.append("     WHEN 3 ");
		sql.append("     THEN 'TERCER NIVEL' ");
		sql.append("     WHEN 4 ");
		sql.append("     THEN 'CUARTO NIVEL' ");
		sql.append("     WHEN 5 ");
		sql.append("     THEN 'QUINTO NIVEL' ");
		sql.append("     WHEN 6 ");
		sql.append("     THEN 'SEXTO NIVEL' ");
		sql.append("     WHEN 7 ");
		sql.append("     THEN 'SÉPTIMO NIVEL' ");
		sql.append("     WHEN 8 ");
		sql.append("     THEN 'OCTAVO NIVEL' ");
		sql.append("     WHEN 9 ");
		sql.append("     THEN 'NOVENO NIVEL' ");
		sql.append("     WHEN 10 ");
		sql.append("     THEN 'DÉCIMO NIVEL' ");
		sql.append("     WHEN 11 ");
		sql.append("     THEN 'NIVELACIÓN' ");
		sql.append("     WHEN 12 ");
		sql.append("     THEN 'DÉCIMO PRIMER NIVEL' ");
		sql.append("     WHEN 13 ");
		sql.append("     THEN 'CURSO DE APROBACIÓN' ");
		sql.append("   END semestre, ");
		sql.append("   prl.PRL_CODIGO, ");
		sql.append("   prl.PRL_DESCRIPCION, ");
		sql.append("   fcmt.fcmt_id , ");
		sql.append("   prac.prac_id, ");
		sql.append("   prac.prac_descripcion , ");
		sql.append("   prs.prs_id , ");
		sql.append("   prs.prs_identificacion , ");
		sql.append("   prs.prs_primer_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs_segundo_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs.prs_nombres estudiante, ");
		sql.append("   mtr.mtr_id materia_id, ");
		sql.append("   mtr.mtr_codigo codigo_materia, ");
		sql.append("   mtr.mtr_descripcion materia, ");
		sql.append("   case when mtr.mtr_creditos is null ");
		sql.append("   then mtr.mtr_horas  ");
		sql.append("   else mtr.mtr_creditos  ");
		sql.append("   end horas,  ");
		sql.append("   rces.rces_estado, ");
		sql.append("   CASE  rces.rces_estado ");
		sql.append("     WHEN -1 ");
		sql.append("     THEN 'INSCRITO' ");
		sql.append("     WHEN 0 ");
		sql.append("     THEN 'MATRICULADO' ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'APROBADO' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'REPROBADO' ");
		sql.append("     WHEN 3 ");
		sql.append("     THEN 'RETIRO APROBADO' ");
		sql.append("   END ESTADO, ");
		sql.append("   dtmt.dtmt_numero numero_matricula, ");
		sql.append("   CASE fcmt.fcmt_tipo ");
		sql.append("     WHEN 0 ");
		sql.append("     THEN 'ORDINARIA' ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'EXTRAORDINARIA' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'ESPECIAL' ");
		sql.append("   END TIPO_MATRICULA ");
		sql.append(" FROM ficha_estudiante fces ");
		sql.append(" INNER JOIN ficha_inscripcion fcin ");
		sql.append(" ON fces.fcin_id = fcin.fcin_id ");
		sql.append(" INNER JOIN ficha_matricula fcmt ");
		sql.append(" ON fces.fces_id = fcmt.fces_id ");
		sql.append(" INNER JOIN comprobante_pago cmpa ");
		sql.append(" ON fcmt.fcmt_id = cmpa.fcmt_id ");
		sql.append(" INNER JOIN detalle_matricula dtmt ");
		sql.append(" ON cmpa.cmpa_id = dtmt.cmpa_id ");
		sql.append(" INNER JOIN malla_curricular_paralelo mlcrpr ");
		sql.append(" ON dtmt.mlcrpr_id = mlcrpr.mlcrpr_id ");
		sql.append(" INNER JOIN paralelo prl ");
		sql.append(" ON prl.prl_id = mlcrpr.prl_id ");
		sql.append(" INNER JOIN malla_curricular_materia mlcrmt ");
		sql.append(" ON mlcrpr.mlcrmt_id = mlcrmt.mlcrmt_id ");
		sql.append(" INNER JOIN nivel NVL ");
		sql.append(" ON mlcrmt.nvl_id = nvl.nvl_id ");
		sql.append(" INNER JOIN persona prs ");
		sql.append(" ON fces.prs_id = prs.prs_id ");
		sql.append(" INNER JOIN configuracion_carrera cncr ");
		sql.append(" ON fcin.cncr_id = cncr.cncr_id ");
		sql.append(" INNER JOIN carrera crr ");
		sql.append(" ON cncr.crr_id = crr.crr_id ");
		sql.append(" INNER JOIN malla_curricular mlcr ");
		sql.append(" ON mlcr.crr_id = crr.crr_id ");
		sql.append(" INNER JOIN periodo_academico prac ");
		sql.append(" ON prl.prac_id = prac.prac_id ");
		sql.append(" INNER JOIN dependencia dpn ");
		sql.append(" ON crr.dpn_id = dpn.dpn_id ");
		sql.append(" INNER JOIN materia mtr ");
		sql.append(" ON mtr.mtr_id = mlcrmt.mtr_id ");
		sql.append(" INNER JOIN record_estudiante rces ");
		sql.append(" ON fces.fces_id    = rces.fces_id ");
		sql.append(" AND rces.mlcrpr_id = mlcrpr.mlcrpr_id ");
		sql.append(" INNER JOIN modalidad mdl ");
		sql.append(" ON mdl.mdl_id                = cncr.mdl_id ");
		sql.append(" WHERE prs.prs_identificacion = '"+identificacion+"' ");
		sql.append(" AND fcmt.FCMT_PRAC_ID NOT IN 63 ");
		sql.append(" GROUP BY dpn.dpn_descripcion, ");
		sql.append("   crr.crr_descripcion, ");
		sql.append("   mdl.mdl_descripcion, ");
		sql.append("   fcmt.fcmt_nivel_ubicacion, ");
		sql.append("   prac.prac_descripcion, ");
		sql.append("   prs.prs_id, ");
		sql.append("   prs.prs_identificacion, ");
		sql.append("   prs.prs_primer_apellido, ");
		sql.append("   prs_segundo_apellido, ");
		sql.append("   prs.prs_nombres, ");
		sql.append("   mtr.mtr_codigo, ");
		sql.append("   mtr.mtr_descripcion, ");
		sql.append("   rces.rces_estado, ");
		sql.append("   dtmt.dtmt_numero, ");
		sql.append("   crr.crr_id, ");
		sql.append("   dpn.dpn_id, ");
		sql.append("   prac.prac_id, ");
		sql.append("   cncr.crr_id, ");
		sql.append("   fcmt.fcmt_id, ");
		sql.append("   mtr.mtr_horas, ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   fcmt.fcmt_tipo, ");
		sql.append("   nvl.nvl_numeral, ");
		sql.append("   mtr.mtr_id, ");
		sql.append("   prl.PRL_CODIGO, ");
		sql.append("   prl.PRL_DESCRIPCION, ");
		sql.append("   mtr.mtr_creditos, ");
		sql.append("   mtr.MTR_HORAS ");
		sql.append(" ORDER BY crr.CRR_DESCRIPCION, ");
		sql.append("   prac.prac_descripcion ");

		try{
			
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarRShistorialMatriculaDto(rs, RecordEstudianteConstantes.RCES_ORIGEN_SIIU));
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

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
			throw new RecordEstudianteNoEncontradoException();
		}

		return retorno;
	}

	public List<RecordEstudianteDto> buscarHistorialAcademicoIdiomasSIIU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException{
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
		sql.append(" WHERE PRS.PRS_IDENTIFICACION = '"+identificacion.toUpperCase()+"'  ");
		sql.append(" AND CRR.CRR_TIPO = " + CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
		sql.append(" AND DPN.DPN_ID = " + DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE);
		sql.append(" ORDER BY  CRR.CRR_TIPO,  PRAC.PRAC_ID, CRR.CRR_DESCRIPCION, NVL.NVL_NUMERAL, MTR.MTR_CODIGO ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
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
	
	
	private RecordEstudianteDto transformarResultSetAhistorialRecordAcademicoHomologadoDto(ResultSet rs, int origen) throws SQLException {
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
		carrera.setCrrEspeCodigo(rs.getInt(9));
		carrera.setCrrAcceso(false);

		estudiante.setPrsId(rs.getInt(10));
		estudiante.setPrsIdentificacion(rs.getString(11));
		estudiante.setPrsPrimerApellido(rs.getString(12));
		estudiante.setPrsSegundoApellido(rs.getString(13));
		estudiante.setPrsNombres(rs.getString(14));
		
		fichaMatricula.setFcmtId(rs.getInt(15));
		fichaMatricula.setFcmtNivelUbicacion(rs.getInt(16));
		fichaMatricula.setFcmtTipo(rs.getInt(17)); //-> corresponde a ordinaria / extra / especial
		modalidad.setMdlDescripcion(rs.getString(18));
		fichaMatricula.setPracId(rs.getInt(19));
		
		materia.setMtrId(rs.getInt(20));
		materia.setMtrCodigo(rs.getString(21));
		materia.setMtrDescripcion(rs.getString(22));
		materia.setMtrCreditos(rs.getInt(23));
		materia.setMtrHoras(rs.getInt(24));
		materia.setMtrHorasPorSemanaPAE(rs.getInt(25));
		materia.setNumMatricula(rs.getInt(26));
		
		nivel.setNvlId(rs.getInt(27));
		nivel.setNvlNumeral(rs.getInt(28));
		nivel.setNvlDescripcion(rs.getString(29));
		
		retorno.setRcesOrigen(origen);
		retorno.setRcesId(rs.getInt(30));
		retorno.setRcesEstado(rs.getInt(31));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(32));
		
		paralelo.setPrlId(rs.getInt(33));
		paralelo.setPrlCodigo(rs.getString(34));
		paralelo.setPracDescripcion(rs.getString(35));
		
		calificacion.setClfId(rs.getInt(36));
		calificacion.setClfNota1(rs.getBigDecimal(37));
		calificacion.setClfNota2(rs.getBigDecimal(38));
		calificacion.setClfSupletorio(rs.getBigDecimal(39));
		calificacion.setClfSumaNotas(rs.getBigDecimal(40));
		calificacion.setClfParamRecuperacion1(rs.getBigDecimal(41));
		calificacion.setClfParamRecuperacion2(rs.getBigDecimal(42));
		calificacion.setClfNotaFinalSemestre(rs.getBigDecimal(43));
		calificacion.setClfAsistencia1(rs.getBigDecimal(44));
		calificacion.setClfAsistencia2(rs.getBigDecimal(45));
		calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(46));
		calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(47));
		calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(48)); 
		
		//objetos seteados para las administraciones antiguas
//		materia.setMtrEstadoLabel(rs.getString(23));// setear afuera
//		fichaMatricula.setNvlDescripcion(rs.getString(7)); -> setear arriba
//		fichaMatricula.setFcmtTipoMatriculaLabel(rs.getString(33));//-> corresponde a ordinaria / extra / especial -> setear arriba
//		retorno.setRcesPromedioGeneral(rs.getBigDecimal(28));// no util
		
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
	
	private RecordEstudianteDto transformarResultSetAhistorialRecordAcademicoDtoPosgrado(ResultSet rs, int origen) throws SQLException {
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PeriodoAcademicoDto periodoAcademico = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		ModalidadDto modalidad = new ModalidadDto();
		MateriaDto materia = new MateriaDto();
		NivelDto nivel = new NivelDto();
		CalificacionDto calificacion = new CalificacionDto();
		PersonaDto estudiante = new PersonaDto();
		FichaMatriculaDto fichaMatricula = new FichaMatriculaDto();
		MallaCurricularDto mallaCurricular = new MallaCurricularDto();

		dependencia.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		carrera.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		carrera.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		
		if(rs.getString(JdbcConstantes.NVL_ID)!=null){
			fichaMatricula.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));	
		}
		
		periodoAcademico.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		periodoAcademico.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		estudiante.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		estudiante.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		estudiante.setPrsNombres(rs.getString("nombres"));
		materia.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		materia.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		materia.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		materia.setNumMatricula(1);
		calificacion.setClfNota1(rs.getBigDecimal(JdbcConstantes.CLF_NOTA1));
		if(rs.getString(JdbcConstantes.CLF_NOTA2)!=null){
			calificacion.setClfNota2(rs.getBigDecimal(JdbcConstantes.CLF_NOTA2));
		}
		
		if(rs.getString(JdbcConstantes.CLF_SUPLETORIO)!=null){
			calificacion.setClfSupletorio(rs.getBigDecimal(JdbcConstantes.CLF_SUPLETORIO));
		}
		
		if(rs.getString(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE)!=null){
			calificacion.setClfNotaFinalSemestre(rs.getBigDecimal(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
		}
		
		if(rs.getString(JdbcConstantes.CLF_SUMA_P1_P2)!=null){
			calificacion.setClfPromedioNotas(rs.getBigDecimal(JdbcConstantes.CLF_SUMA_P1_P2));
		}
		
		if(rs.getString(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA)!=null){
			calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
		}
		
		if(rs.getString("estado")!=null){
			materia.setMtrEstadoLabel(rs.getString("estado"));
		}
		
		
		
		if(rs.getString(JdbcConstantes.CLF_ASISTENCIA1)!=null){
			calificacion.setClfAsistencia1(rs.getBigDecimal(JdbcConstantes.CLF_ASISTENCIA1));
		}
		
		if(rs.getString(JdbcConstantes.CLF_ASISTENCIA2)!=null){
			calificacion.setClfAsistencia2(rs.getBigDecimal(JdbcConstantes.CLF_ASISTENCIA2));
		}
		
		if(rs.getString(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1)!=null){
			calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
		}
		
		if(rs.getString(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2)!=null){
			calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2));
		}
		
		
		if(rs.getString(JdbcConstantes.CLF_SUMA_P1_P2)!=null){
			calificacion.setClfSumaNotas(rs.getBigDecimal(JdbcConstantes.CLF_SUMA_P1_P2));	
		}
		
		if(rs.getString(JdbcConstantes.CLF_PARAM_RECUPERACION1)!=null){
			calificacion.setClfParamRecuperacion1(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION1));
		}
		
		if(rs.getString(JdbcConstantes.CLF_PARAM_RECUPERACION2)!=null){
			calificacion.setClfParamRecuperacion2(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION2));
		}
		nivel.setNvlNumeral(GeneralesConstantes.APP_ID_BASE);
		
		carrera.setCrrTipo(rs.getInt("crr_tipo"));
		retorno.setRcesOrigen(origen);
		retorno.setRcesPeriodoAcademicoDto(periodoAcademico);
		retorno.setRcesDependenciaDto(dependencia);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesMateriaDto(materia);
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesModalidadDto(modalidad);
		retorno.setRcesNivelDto(nivel);
		retorno.setRcesCalificacionDto(calificacion);
		retorno.setRcesFichaMatriculaDto(fichaMatricula);
		retorno.setRcesMallaCurricularDto(mallaCurricular);
		retorno.setRcesPracId(periodoAcademico.getPracId());
		retorno.setRcesPracDescripcion(periodoAcademico.getPracDescripcion());
		retorno.setMtrCodigo(materia.getMtrCodigo());
		return retorno;
	}
	
	private RecordEstudianteDto transformarRShistorialMatriculaDto(ResultSet rs, int origen) throws SQLException {
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		FichaMatriculaDto fichaMatricula = new FichaMatriculaDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		PersonaDto estudiante = new PersonaDto();
		MateriaDto materia = new MateriaDto();
		
		dependencia.setDpnId(rs.getInt(1));
		dependencia.setDpnDescripcion(rs.getString(2));
		carrera.setCrrId(rs.getInt(3));
		carrera.setCrrDescripcion(rs.getString(4));
		carrera.setCrrAcceso(false);
		fichaMatricula.setFcmtModalidadLabel(rs.getString(5));
		fichaMatricula.setNvlId(rs.getInt(6));
		fichaMatricula.setNvlDescripcion(rs.getString(7));
		fichaMatricula.setPrlCodigo(rs.getString(8));
		fichaMatricula.setPrlDescripcion(rs.getString(9));
		fichaMatricula.setFcmtId(rs.getInt(10));
		periodo.setPracId(rs.getInt(11));
		periodo.setPracDescripcion(rs.getString(12));
		estudiante.setPrsId(rs.getInt(13));
		estudiante.setPrsIdentificacion(rs.getString(14));
		estudiante.setPrsApellidosNombres(rs.getString(15));
		materia.setMtrId(rs.getInt(16));
		materia.setMtrCodigo(rs.getString(17));
		materia.setMtrDescripcion(rs.getString(18));
		materia.setMtrHorasPorSemana(rs.getInt(19));
		materia.setMtrEstado(rs.getInt(20));
		materia.setMtrEstadoLabel(rs.getString(21));
		retorno.setDtmtNumMatricula(rs.getInt(22));
		fichaMatricula.setFcmtTipoMatriculaLabel(rs.getString(23));
		retorno.setRcesOrigen(origen);
		retorno.setRcesDependenciaDto(dependencia);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesFichaMatriculaDto(fichaMatricula);
		retorno.setRcesPeriodoAcademicoDto(periodo);
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesMateriaDto(materia);
		retorno.setRcesPracId(periodo.getPracId());
		retorno.setRcesPracDescripcion(periodo.getPracDescripcion());
		retorno.setMtrCodigo(materia.getMtrCodigo());
		
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
		fichaMatricula.setFcmtTipo(rs.getInt(16)); //-> corresponde a ordinaria / extra / especial
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
