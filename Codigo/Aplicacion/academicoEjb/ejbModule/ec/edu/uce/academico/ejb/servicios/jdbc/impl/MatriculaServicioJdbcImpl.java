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
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
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
import ec.edu.uce.academico.ejb.dtos.MatriculaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
/**
 * EJB MatriculaServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de MatriculaServicioJdbc.
 * @author fgguzman
 * @version 1.0
 */

@Stateless
public class MatriculaServicioJdbcImpl implements MatriculaServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@Resource(lookup=GeneralesConstantes.APP_DATA_SOURCE_SAU)
	private DataSource dsSau;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public	List<MatriculaDto> buscarEstudiantesPregrado() throws MatriculaNoEncontradoException, MatriculaException{

		List<MatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;


		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT FCIN.FCIN_ID, ");
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_INSCRIPCION FCIN ");
		sql.append(" WHERE PRS.PRS_ID        = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID          = USRO.USR_ID ");
		sql.append(" AND USRO.USRO_ID        = FCIN.USRO_ID ");
		sql.append(" AND USRO.ROL_ID         =  " + RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND FCIN.FCIN_ESTADO    =  " + FichaInscripcionConstantes.ACTIVO_VALUE);
		sql.append(" AND FCIN.FCIN_TIPO NOT IN (6,4,3,2) ");

		try {

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarEstudiantesPregrado(rs));
			}
			
		} catch (Exception e) {
			throw new MatriculaException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MatriculaNoEncontradoException("No se encontró resultados para los parámetros ingresados.");				
		}
		
		return retorno;
	}
	
	
	public	List<MatriculaDto> buscarEstudianteMatriculado(String prsIdentificacion, int fcinId, int periodoId) throws MatriculaNoEncontradoException, MatriculaException{

		List<MatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;


		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT FCIN.FCIN_ID, ");
		sql.append("   FCMT.FCMT_PRAC_ID , ");
		sql.append("   PRAC.PRAC_DESCRIPCION , ");
		sql.append("   CRR.CRR_ID , ");
		sql.append("   CRR.CRR_DESCRIPCION , ");
		sql.append("   PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES ");
		sql.append(" FROM PERSONA PRS , ");
		sql.append("   USUARIO USR , ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_ESTUDIANTE FCES , ");
		sql.append("   RECORD_ESTUDIANTE RCES , ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR , ");
		sql.append("   FICHA_MATRICULA FCMT , ");
		sql.append("   PERIODO_ACADEMICO PRAC , ");
		sql.append("   COMPROBANTE_PAGO CMPG , ");
		sql.append("   DETALLE_MATRICULA DTMT , ");
		sql.append("   FICHA_INSCRIPCION FCIN , ");
		sql.append("   CONFIGURACION_CARRERA CNCR , ");
		sql.append("   CARRERA CRR ");
		sql.append(" WHERE FCES.FCES_ID             = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_ID               = CMPG.FCMT_ID ");
		sql.append(" AND FCMT.FCMT_PRAC_ID          = PRAC.PRAC_ID ");
		sql.append(" AND CMPG.CMPA_ID               = DTMT.CMPA_ID ");
		sql.append(" AND PRS.PRS_ID                 = FCES.PRS_ID ");
		sql.append(" AND USR.PRS_ID                 = PRS.PRS_ID ");
		sql.append(" AND RCES.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID           = DTMT.MLCRPR_ID ");
		sql.append(" AND FCES.FCES_ID               = RCES.FCES_ID ");
		sql.append(" AND FCIN.FCIN_ID               = FCES.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID               = CNCR.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID                 = CNCR.CRR_ID ");
		sql.append(" AND USR.USR_ID                 = USRO.USR_ID ");
		sql.append(" AND FCIN.FCIN_ESTADO           =  " + FichaInscripcionConstantes.ACTIVO_VALUE);
		sql.append(" AND FCMT.FCMT_NIVEL_UBICACION <>  " + NivelConstantes.NIVEL_NIVELACION_VALUE);
		sql.append(" AND USRO.ROL_ID                =  " + RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND PRS.PRS_IDENTIFICACION     = ? ");
		sql.append(" AND FCIN.FCIN_ID               = ? ");
		sql.append(" AND FCMT.FCMT_PRAC_ID          = ? ");
		

		try {

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, prsIdentificacion);
			pstmt.setInt(2, fcinId);
			pstmt.setInt(3, periodoId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarEstudiantesInscritos(rs));
			}
			
		} catch (Exception e) {
			throw new MatriculaException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MatriculaNoEncontradoException("No se encontró resultados para los parámetros ingresados.");				
		}
		
		return retorno;
	}
	
	public	List<MatriculaDto> buscarEstudiantesInscritos(int periodoId) throws MatriculaNoEncontradoException, MatriculaException{

		List<MatriculaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;


		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT FCIN.FCIN_ID, ");
		sql.append("   FCMT.FCMT_PRAC_ID , ");
		sql.append("   PRAC.PRAC_DESCRIPCION , ");
		sql.append("   CRR.CRR_ID , ");
		sql.append("   CRR.CRR_DESCRIPCION , ");
		sql.append("   PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES ");
		sql.append(" FROM PERSONA PRS , ");
		sql.append("   USUARIO USR , ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_ESTUDIANTE FCES , ");
		sql.append("   RECORD_ESTUDIANTE RCES , ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR , ");
		sql.append("   FICHA_MATRICULA FCMT , ");
		sql.append("   PERIODO_ACADEMICO PRAC , ");
		sql.append("   COMPROBANTE_PAGO CMPG , ");
		sql.append("   DETALLE_MATRICULA DTMT , ");
		sql.append("   FICHA_INSCRIPCION FCIN , ");
		sql.append("   CONFIGURACION_CARRERA CNCR , ");
		sql.append("   CARRERA CRR ");
		sql.append(" WHERE FCES.FCES_ID             = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_ID               = CMPG.FCMT_ID ");
		sql.append(" AND FCMT.FCMT_PRAC_ID          = PRAC.PRAC_ID ");
		sql.append(" AND CMPG.CMPA_ID               = DTMT.CMPA_ID ");
		sql.append(" AND PRS.PRS_ID                 = FCES.PRS_ID ");
		sql.append(" AND USR.PRS_ID                 = PRS.PRS_ID ");
		sql.append(" AND RCES.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID           = DTMT.MLCRPR_ID ");
		sql.append(" AND FCES.FCES_ID               = RCES.FCES_ID ");
		sql.append(" AND FCIN.FCIN_ID               = FCES.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID               = CNCR.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID                 = CNCR.CRR_ID ");
		sql.append(" AND USR.USR_ID                 = USRO.USR_ID ");
		sql.append(" AND RCES.RCES_ESTADO           =  " + RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
		sql.append(" AND FCMT.FCMT_NIVEL_UBICACION <>  " + NivelConstantes.NIVEL_NIVELACION_VALUE);
		sql.append(" AND USRO.ROL_ID                =  " + RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND FCMT.FCMT_PRAC_ID          = ? ");
		

		try {

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarEstudiantesInscritos(rs));
			}
			
		} catch (Exception e) {
			throw new MatriculaException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MatriculaNoEncontradoException("No se encontró resultados para los parámetros ingresados.");				
		}
		
		return retorno;
	}

	/**
	 * Método que permite buscar la cantidad de cupos y matriculados en una Materia y Paralelo X.
	 * @author fgguzman // -- v1
	 * @author Arturo Villafuerte - ajvilafuerte // -- v2
	 * @param mallaCurricularParaleloId - parametro con la malla curricular paralelo ID.
	 * @param paraleloId - parametro con el paralelo. 
	 * @return cantidad de cupos disponibles.
	 * @throws MateriaException
	 * @throws MateriaNoEncontradoException
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloException
	 */
	public List<Integer> buscarCuposAndMatriculados(int mallaCurricularMateriaId, int paraleloId, int periodoId) throws ParaleloNoEncontradoException, ParaleloException {
		List<Integer> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM ( ");
		sql.append(" SELECT MLCRPR.MLCRPR_CUPO CUPOS ");
		sql.append(" FROM MALLA_CURRICULAR_PARALELO MLCRPR, PARALELO PRL, MALLA_CURRICULAR_MATERIA MLCRMT, MATERIA MTR, PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE MLCRPR.PRL_ID = PRL.PRL_ID  "); 
		sql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID  ");
		sql.append(" AND MLCRMT.MTR_ID = MTR.MTR_ID ");
		sql.append(" AND PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append(" AND MLCRPR.MLCRPR_CUPO IS NOT NULL ");
		sql.append(" AND MLCRMT.MLCRMT_ID = ");
		sql.append( mallaCurricularMateriaId );
		sql.append(" AND PRL.PRL_ID = ");
		sql.append( paraleloId );
		sql.append(" AND PRAC.PRAC_ID = ");
		sql.append( periodoId );
		sql.append(" ), ( ");
		sql.append(" SELECT COUNT(DTMT.DTMT_ID) MATRICULADOS  ");
		sql.append(" FROM DETALLE_MATRICULA DTMT, MALLA_CURRICULAR_PARALELO MLCRPR, PARALELO PRL, MALLA_CURRICULAR_MATERIA MLCRMT, PERIODO_ACADEMICO PRAC  ");
		sql.append(" WHERE DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID = PRL.PRL_ID  ");
		sql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID  ");
		sql.append(" AND PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append(" AND MLCRPR.MLCRPR_CUPO IS NOT NULL ");
		sql.append(" AND MLCRMT.MLCRMT_ID = ");
		sql.append( mallaCurricularMateriaId );
		sql.append(" AND PRL.PRL_ID = ");
		sql.append( paraleloId );
		sql.append(" AND DTMT.DTMT_ESTADO = 0 ");
		sql.append(" AND PRAC.PRAC_ID = ");
		sql.append( periodoId );
		sql.append(" ) ");
		
		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rs.getInt(1));
				retorno.add(rs.getInt(2));
			}
			
		} catch (Exception e) {
			retorno = null;
			e.printStackTrace();
			throw new ParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MatriculaServicioJdbcImpl.buscar.record.academico.sau.error.tipo.sql")));
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
			throw new ParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MatriculaServicioJdbcImpl.buscar.record.academico.sau.no.result.exception")));
		}
		return retorno;
	}
 

	public List<RecordEstudianteSAUDto> buscarRecordAcademicoSAU(String identificacion, int espeCodigo,  int periodoId)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteSAUDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		
		
		sql.append("  select DISTINCT carrera, codigo_materia,estado, numero_matricula, periodo,semestre from (SELECT DISTINCT f.nombre facultad,ee.espe_codigo carrera,CASE mtr.modalidad  ");
		sql.append(" WHEN 1 THEN 'PRESENCIAL'  ");
		sql.append(" WHEN 2 THEN 'DISTANCIA'  ");
		sql.append(" END AS modalidad,mtr.pkv_folio AS Folio,mtr.pkv_folio_indice AS indice,mat.mat_curso AS semestre,P .grupo AS periodo,e.est_codigo as codigo_estudiantil,e.est_cedula cedula,e.est_apellido_paterno || ' ' || e.est_apellido_materno || ' ' ||e.est_nombres AS nombres,mat.mat_codigo_uce AS codigo_materia,mat.mat_nombre AS materia,rm.rmat_nota_uno AS Nota_1,rm.rmat_nota_dos AS Nota_2,rm.rmat_nota_tres AS Recuperacion,rm.nota_final as nota_final,rm.asistencia_final AS Asistencia,rm.rmat_aprobado estado,mat.mat_numero_creditos as Numero_creditos,rm.numero_matricula as numero_matricula,ee.nombre_director as director_carrera, fa.nombre_secretario_abogado as secretario_abogado,mtr.matr_codigo as codigo_matricula,(select avg(rm2.nota_final/2)  ");
		sql.append(" FROM registro_materias rm2  ");
		sql.append(" INNER JOIN materias mat2 ON rm2.mat_codigo = mat2.mat_codigo  ");
		sql.append(" INNER JOIN matriculas mtr2 ON rm2.per_codigo = mtr2.per_codigo AND rm2.est_codigo = mtr2.est_codigo  ");
		sql.append(" INNER JOIN periodos P2 ON mtr2.per_codigo = P2 .per_codigo INNER JOIN estudiantes e2 ON mtr2.est_codigo = e2.est_codigo INNER JOIN especialidades_escuela ee2 ON mtr2.espe_codigo = ee2.espe_codigo  ");
		sql.append(" INNER JOIN escuelas s2 ON ee2.esc_codigo = s2.esc_codigo AND s2.esc_codigo = s2.esc_codigo  ");
		sql.append(" INNER JOIN facultad f2 ON s2.id_facultad = f2. ID  ");
		sql.append(" INNER JOIN facultad_autoridades fa2 ON fa2.facultad_id =f2.id  ");
		sql.append(" WHERE f2. ID = f. ID and ee2.espe_codigo = ee.espe_codigo  ");
		sql.append(" AND s2.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165)  ");
		sql.append(" and ee.espe_codigo not in (63,64,65,66) ");
		sql.append(" AND mtr2.matr_estado > 1  ");
		sql.append(" AND rm2.rmat_aprobado IN (3,10)  ");
		sql.append(" and mat2.mat_curso > 0  ");
		sql.append(" and e2.est_cedula = e.est_cedula  ");
		sql.append(" )promedio_general,(SELECT DISTINCT sum(mat3.mat_numero_creditos) FROM registro_materias rm3 INNER JOIN materias mat3 ON rm3.mat_codigo = mat3.mat_codigo INNER JOIN matriculas mtr3 ON rm3.per_codigo = mtr3.per_codigo AND rm3.est_codigo = mtr3.est_codigo INNER JOIN periodos P3 ON mtr3.per_codigo = P3 .per_codigo INNER JOIN estudiantes e3 ON mtr3.est_codigo = e3.est_codigo INNER JOIN especialidades_escuela ee3 ON mtr3.espe_codigo = ee3.espe_codigo INNER JOIN escuelas s3 ON ee3.esc_codigo = s3.esc_codigo AND s3.esc_codigo = s3.esc_codigo INNER JOIN facultad f3 ON s3.id_facultad = f3. ID INNER JOIN facultad_autoridades fa3 ON fa3.facultad_id =f3.id WHERE s3.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr3.matr_estado > 1 and mat3.mat_curso > 0 AND rm3.rmat_aprobado IN (3,10) and f3. ID = f. ID and ee3.espe_codigo = ee.espe_codigo AND e3.est_cedula = e.est_cedula )creditos_totales,(SELECT DISTINCT sum(mat4.mat_numero_creditos) as Numero_creditos FROM registro_materias rm4 INNER JOIN materias mat4 ON rm4.mat_codigo = mat4.mat_codigo INNER JOIN matriculas mtr4 ON rm4.per_codigo = mtr4.per_codigo AND rm4.est_codigo = mtr4.est_codigo INNER JOIN periodos P4 ON mtr4.per_codigo = P4 .per_codigo INNER JOIN estudiantes e4 ON mtr4.est_codigo = e4.est_codigo INNER JOIN especialidades_escuela ee4 ON mtr4.espe_codigo = ee4.espe_codigo INNER JOIN escuelas s4 ON ee4.esc_codigo = s4.esc_codigo AND s4.esc_codigo = s4.esc_codigo INNER JOIN facultad f4 ON s4.id_facultad = f4. ID INNER JOIN facultad_autoridades fa4 ON fa4.facultad_id =f4.id WHERE f4. ID = f. ID and ee4.espe_codigo = ee.espe_codigo AND s4.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr4.matr_estado > 1 AND p4.per_descripcion in ('MIGRACION') AND rm4.rmat_aprobado IN (3) and e4.est_cedula = e.est_cedula )Migracion,(SELECT sum(mcc5.numero_creditos) FROM malla_curricular_creditos mcc5  LEFT JOIN malla_curricular mc5 ON mc5.id= mcc5.malla_curricular_id  LEFT JOIN escuelas esc5 ON esc5.esc_codigo = mc5.esc_codigo LEFT JOIN especialidades_escuela espe5 ON espe5.espe_codigo = mc5.espe_codigo WHERE mc5.fecha_termina IS NULL and espe5.espe_codigo = ee.espe_codigo) Creditos_carrera,'' as elaborado_por FROM registro_materias rm INNER JOIN materias mat ON rm.mat_codigo = mat.mat_codigo INNER JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo INNER JOIN periodos P ON mtr.per_codigo = P .per_codigo INNER JOIN estudiantes e ON mtr.est_codigo = e.est_codigo INNER JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo INNER JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND s.esc_codigo = s.esc_codigo INNER JOIN facultad f ON s.id_facultad = f. ID INNER JOIN facultad_autoridades fa ON fa.facultad_id =f.id inner JOIN malla_curricular mc ON ee.espe_codigo = mc.espe_codigo and mat.malla_curricular_id =mc.id  ");
		sql.append(" INNER JOIN malla_curricular_creditos mcc on mc.id= mcc.malla_curricular_id WHERE ");
		sql.append("  ee.espe_codigo = "+espeCodigo +" AND s.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr.matr_estado > 1 AND rm.rmat_aprobado IN (3,4,10) and mat.mat_estado = 1 and mat.mat_curso > 0  ");
		sql.append(" and e.est_cedula = '"+identificacion.toUpperCase() +"' and p.grupo<= "+periodoId+" GROUP BY P.grupo,f.nombre,ee.espe_nombre,f.id,ee.espe_codigo,mtr.modalidad ,mat.mat_curso,nombres,mat.mat_codigo_uce,mat.mat_nombre,rm.rmat_aprobado,mtr.pkv_folio,mtr.pkv_folio_indice,rm.rmat_nota_uno,rm.rmat_nota_dos,rm.rmat_nota_tres,rm.nota_final,rm.asistencia_final,mat.mat_numero_creditos,rm.numero_matricula,e.est_codigo,e.est_cedula,ee.nombre_director,fa.nombre_secretario_abogado,mtr.matr_codigo ORDER BY nombres, P .grupo,mat.mat_codigo_uce,e.est_cedula, mat.mat_nombre)certificado group by facultad, carrera,modalidad,Folio,indice, semestre, periodo,codigo_estudiantil, cedula, nombres,codigo_materia, materia, Nota_1,Nota_2,Recuperacion, nota_final, Asistencia,estado, Numero_creditos, numero_matricula,director_carrera,secretario_abogado,codigo_matricula,Creditos_carrera,creditos_totales,promedio_general ,migracion,elaborado_por  ");
		sql.append(" union all  ");
		sql.append(" select DISTINCT carrera, codigo_materia,estado, numero_matricula, periodo,semestre from (SELECT DISTINCT f.nombre facultad,ee.espe_codigo carrera,CASE mtr.modalidad  ");
		sql.append(" WHEN 1 THEN 'PRESENCIAL'  ");
		sql.append(" WHEN 2 THEN 'DISTANCIA'  ");
		sql.append(" END AS modalidad,mtr.pkv_folio AS Folio,mtr.pkv_folio_indice AS indice,mat.mat_curso AS semestre,P .grupo AS periodo,e.est_codigo as codigo_estudiantil,e.est_cedula cedula,e.est_apellido_paterno || ' ' || e.est_apellido_materno || ' ' ||e.est_nombres AS nombres,mat.mat_codigo_uce AS codigo_materia,mat.mat_nombre AS materia,rm.rmat_nota_uno AS Nota_1,rm.rmat_nota_dos AS Nota_2,rm.rmat_nota_tres AS Recuperacion,rm.nota_final as nota_final,rm.asistencia_final AS Asistencia,rm.rmat_aprobado estado,mat.mat_numero_creditos as Numero_creditos,rm.numero_matricula as numero_matricula,ee.nombre_director as director_carrera, fa.nombre_secretario_abogado as secretario_abogado,mtr.matr_codigo as codigo_matricula,(select avg(rm2.nota_final/2)  ");
		sql.append(" FROM registro_materias rm2  ");
		sql.append(" INNER JOIN materias mat2 ON rm2.mat_codigo = mat2.mat_codigo  ");
		sql.append(" INNER JOIN matriculas mtr2 ON rm2.per_codigo = mtr2.per_codigo AND rm2.est_codigo = mtr2.est_codigo  ");
		sql.append(" INNER JOIN periodos P2 ON mtr2.per_codigo = P2 .per_codigo INNER JOIN estudiantes e2 ON mtr2.est_codigo = e2.est_codigo INNER JOIN especialidades_escuela ee2 ON mtr2.espe_codigo = ee2.espe_codigo  ");
		sql.append(" INNER JOIN escuelas s2 ON ee2.esc_codigo = s2.esc_codigo AND s2.esc_codigo = s2.esc_codigo  ");
		sql.append(" INNER JOIN facultad f2 ON s2.id_facultad = f2. ID  ");
		sql.append(" INNER JOIN facultad_autoridades fa2 ON fa2.facultad_id =f2.id  ");
		sql.append(" WHERE f2. ID = f. ID and ee2.espe_codigo = ee.espe_codigo  ");
		sql.append(" AND s2.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165)  ");
		sql.append(" and ee.espe_codigo not in (63,64,65,66) ");
		sql.append(" AND mtr2.matr_estado > 1  ");
		sql.append(" AND rm2.rmat_aprobado IN (3,10)  ");
		sql.append(" and mat2.mat_curso > 0  ");
		sql.append(" and e2.est_cedula = e.est_cedula  ");
		sql.append(" )promedio_general,(SELECT DISTINCT sum(mat3.mat_numero_creditos) FROM registro_materias rm3 INNER JOIN materias mat3 ON rm3.mat_codigo = mat3.mat_codigo INNER JOIN matriculas mtr3 ON rm3.per_codigo = mtr3.per_codigo AND rm3.est_codigo = mtr3.est_codigo INNER JOIN periodos P3 ON mtr3.per_codigo = P3 .per_codigo INNER JOIN estudiantes e3 ON mtr3.est_codigo = e3.est_codigo INNER JOIN especialidades_escuela ee3 ON mtr3.espe_codigo = ee3.espe_codigo INNER JOIN escuelas s3 ON ee3.esc_codigo = s3.esc_codigo AND s3.esc_codigo = s3.esc_codigo INNER JOIN facultad f3 ON s3.id_facultad = f3. ID INNER JOIN facultad_autoridades fa3 ON fa3.facultad_id =f3.id WHERE s3.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr3.matr_estado > 1 and mat3.mat_curso > 0 AND rm3.rmat_aprobado IN (3,10) and f3. ID = f. ID and ee3.espe_codigo = ee.espe_codigo AND e3.est_cedula = e.est_cedula )creditos_totales,(SELECT DISTINCT sum(mat4.mat_numero_creditos) as Numero_creditos FROM registro_materias rm4 INNER JOIN materias mat4 ON rm4.mat_codigo = mat4.mat_codigo INNER JOIN matriculas mtr4 ON rm4.per_codigo = mtr4.per_codigo AND rm4.est_codigo = mtr4.est_codigo INNER JOIN periodos P4 ON mtr4.per_codigo = P4 .per_codigo INNER JOIN estudiantes e4 ON mtr4.est_codigo = e4.est_codigo INNER JOIN especialidades_escuela ee4 ON mtr4.espe_codigo = ee4.espe_codigo INNER JOIN escuelas s4 ON ee4.esc_codigo = s4.esc_codigo AND s4.esc_codigo = s4.esc_codigo INNER JOIN facultad f4 ON s4.id_facultad = f4. ID INNER JOIN facultad_autoridades fa4 ON fa4.facultad_id =f4.id WHERE f4. ID = f. ID and ee4.espe_codigo = ee.espe_codigo AND s4.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr4.matr_estado > 1 AND p4.per_descripcion in ('MIGRACION') AND rm4.rmat_aprobado IN (3) and e4.est_cedula = e.est_cedula )Migracion,(SELECT sum(mcc5.numero_creditos) FROM malla_curricular_creditos mcc5  LEFT JOIN malla_curricular mc5 ON mc5.id= mcc5.malla_curricular_id  LEFT JOIN escuelas esc5 ON esc5.esc_codigo = mc5.esc_codigo LEFT JOIN especialidades_escuela espe5 ON espe5.espe_codigo = mc5.espe_codigo WHERE mc5.fecha_termina IS NULL and espe5.espe_codigo = ee.espe_codigo) Creditos_carrera,'' as elaborado_por FROM registro_materias rm INNER JOIN materias mat ON rm.mat_codigo = mat.mat_codigo INNER JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo INNER JOIN periodos P ON mtr.per_codigo = P .per_codigo INNER JOIN estudiantes e ON mtr.est_codigo = e.est_codigo INNER JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo INNER JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND s.esc_codigo = s.esc_codigo INNER JOIN facultad f ON s.id_facultad = f. ID INNER JOIN facultad_autoridades fa ON fa.facultad_id =f.id inner JOIN malla_curricular mc ON ee.espe_codigo = mc.espe_codigo and mat.malla_curricular_id =mc.id  ");
		sql.append(" INNER JOIN malla_curricular_creditos mcc on mc.id= mcc.malla_curricular_id WHERE ");
		sql.append("  ee.espe_codigo = "+espeCodigo+" AND s.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr.matr_estado > 1 and mat.mat_estado = 1 AND rm.rmat_aprobado IN (3,4,10) and mat.mat_curso > 0  ");
		sql.append(" and e.est_cedula = '"+identificacion.toUpperCase()+"' and p.per_descripcion ilike 'conv%' GROUP BY P.grupo,f.nombre,ee.espe_nombre,f.id,ee.espe_codigo,mtr.modalidad ,mat.mat_curso,nombres,mat.mat_codigo_uce,mat.mat_nombre,rm.rmat_aprobado,mtr.pkv_folio,mtr.pkv_folio_indice,rm.rmat_nota_uno,rm.rmat_nota_dos,rm.rmat_nota_tres,rm.nota_final,rm.asistencia_final,mat.mat_numero_creditos,rm.numero_matricula,e.est_codigo,e.est_cedula,ee.nombre_director,fa.nombre_secretario_abogado,mtr.matr_codigo ORDER BY nombres, P .grupo,mat.mat_codigo_uce,e.est_cedula, mat.mat_nombre)certificado group by facultad, carrera,modalidad,Folio,indice, semestre, periodo,codigo_estudiantil, cedula, nombres,codigo_materia, materia, Nota_1,Nota_2,Recuperacion, nota_final, Asistencia,estado, Numero_creditos, numero_matricula,director_carrera,secretario_abogado,codigo_matricula,Creditos_carrera,creditos_totales,promedio_general ,migracion,elaborado_por  ");
		sql.append(" order by codigo_materia  ");
		
		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSauDto(rs));
			}
			
		} catch (Exception e) {
			retorno = null; 
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
				e.printStackTrace();
			}
		}

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
		}

		return retorno;
	}

	/**
	 * Busca las del sistema SAU 
	 * @author Arturo Villafuerte - ajvilafuerte
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carreraId - id de la carrera.
	 * @param periodoId - id del periodo.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	public List<RecordEstudianteSAUDto> buscarNotasSAU(String identificacion, int espeCodigo,  int periodoId)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteSAUDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  ");
		sql.append(" DISTINCT p.per_descripcion as periodo,  ");
		sql.append(" p.grupo grupo, ");
		sql.append(" ee.espe_codigo, ");
		sql.append(" ee.espe_nombre as carrera, ");
		sql.append(" e.est_cedula as cedula, ");
		sql.append(" e.est_apellido_paterno ||' '|| e.est_apellido_materno ||' '||e.est_nombres as nombres,  ");
		sql.append(" mtr.matr_curso as semestre, ");
		sql.append(" rm.numero_matricula NO_matricula, ");
		sql.append(" mat.mat_estado,  ");
		sql.append(" mat.mat_codigo_uce as codigo_materia, ");
		sql.append(" mat.mat_nombre as materia,  ");
		sql.append(" rm.rmat_nota_uno as nota_1, ");
		sql.append(" rm.rmat_asistencia_uno asistencia_1, ");
		sql.append(" rm.rmat_nota_dos as nota_2, ");
		sql.append(" rm.rmat_asistencia_dos asistencia_2, ");
		sql.append(" rm.rmat_nota_tres as recuperacion, ");
		sql.append(" rm.nota_final as nota_final, ");
		sql.append(" rm.asistencia_final asistencia_final,  ");
		sql.append(" mat.mat_numero_creditos, ");
		sql.append(" CASE rm.rmat_aprobado ");
		sql.append(" WHEN -1 THEN 'ANULADO' ");
		sql.append(" WHEN 0 THEN '??0' ");
		sql.append(" WHEN 1 THEN 'INSCRITO' ");
		sql.append(" WHEN 2 THEN 'MATRICULADO' ");
		sql.append(" WHEN 3 THEN 'APROBADO' ");
		sql.append(" WHEN 4 THEN 'REPROBADO' ");
		sql.append(" WHEN 5 THEN 'NINGUNA_NOTA' ");
		sql.append(" WHEN 6 THEN 'SUSPENSO' ");
		sql.append(" WHEN 7 THEN 'PERDIDO_POR_ASISTENCIA' ");
		sql.append(" WHEN 10 THEN 'CONVALIDADO' ");
		sql.append(" ELSE 'OTHER' END as estado, ");
//		sql.append(" hp.descripcion as paralelo, ");
		sql.append(" rm.rmat_asistencia_total_uno, ");
		sql.append(" rm.rmat_asistencia_total_dos ");
		
		sql.append(" FROM ");
		sql.append(" registro_materias rm ");
		sql.append(" INNER JOIN materias mat ON rm.mat_codigo = mat.mat_codigo ");
		sql.append(" INNER JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo ");
		sql.append(" INNER JOIN periodos p ON mtr.per_codigo = p.per_codigo ");
		sql.append(" INNER JOIN estudiantes e ON mtr.est_codigo = e.est_codigo ");
		sql.append(" INNER JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo ");
		sql.append(" INNER JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND s.esc_codigo = s.esc_codigo ");
		sql.append(" INNER JOIN facultad f ON s.id_facultad = f.id ");
		sql.append(" INNER JOIN hor_materia hm ON mat.mat_codigo = hm.id_materia ");
//		sql.append(" INNER JOIN hor_paralelo hp ON hm.id_paralelo = hp.id AND hp.id_periodo = p.per_codigo  ");
		sql.append(" WHERE ");
		sql.append(" e.est_cedula = '"+identificacion+"' ");
		
		if(espeCodigo != GeneralesConstantes.APP_ID_BASE){
			sql.append(" AND ee.espe_codigo = "+espeCodigo+" ");
		}
		sql.append(" AND p.grupo <= "+SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE+" ");
		if(periodoId != GeneralesConstantes.APP_ID_BASE){
			sql.append(" AND p.grupo = "+periodoId+" ");
		}
		sql.append(" AND mtr.matr_curso > 0 ");
		sql.append(" AND mtr.matr_estado > 0 ");
		sql.append(" and mat.mat_estado = 1 ");
		sql.append(" and ee.espe_codigo not in (63,64,65,66) ");
		sql.append(" GROUP BY f.nombre,ee.espe_nombre, mtr.matr_curso, e.est_cedula,e.est_apellido_paterno,e.est_apellido_materno, ");
		sql.append(" e.est_nombres,rm.numero_matricula,f.id,mat.mat_codigo_uce,rm.rmat_nota_uno,rm.rmat_asistencia_uno, ");
		sql.append(" rm.rmat_asistencia_dos,rm.rmat_nota_dos,rm.rmat_nota_tres,rm.nota_final,p.per_descripcion, p.grupo,mat.mat_nombre, ");
		sql.append(" rm.rmat_aprobado,rm.asistencia_final, mat.mat_numero_creditos , ee.espe_codigo, codigo_materia,mat.mat_estado,rm.rmat_asistencia_total_uno,rm.rmat_asistencia_total_dos ");
		sql.append(" ORDER BY 2 DESC; ");
		
		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSauDtoFull(rs));
			}
			
		} catch (Exception e) {
			retorno = null;
			e.printStackTrace();
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
		}

		return retorno;
	}
	
	
	/**
	 * Método que permite buscar la cantidad de cupos y matriculados en una Materia y Paralelo X.
	 * @author fgguzman // -- v1
	 * @author Arturo Villafuerte - ajvilafuerte // -- v2
	 * @param mallaCurricularParaleloId - parametro con la malla curricular paralelo ID.
	 * @param paraleloId - parametro con el paralelo. 
	 * @return cantidad de cupos disponibles.
	 * @throws MateriaException
	 * @throws MateriaNoEncontradoException
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloException
	 */
	public ComprobantePago buscarComprobantePago(String identificacion, int carreraid,  int periodoId) throws ParaleloNoEncontradoException, ParaleloException {
		ComprobantePago retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CMPG.CMPA_FECHA_EMISION, "); 
		sql.append(" CMPG.CMPA_FECHA_CADUCA,  ");
		sql.append(" CMPG.CMPA_NUM_COMPROBANTE,  ");
		sql.append(" CMPG.CMPA_TOTAL_PAGO   ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append(" FICHA_ESTUDIANTE FCES, ");
		sql.append(" FICHA_MATRICULA FCMT, ");
		sql.append(" FICHA_INSCRIPCION FCIN, ");
		sql.append(" CONFIGURACION_CARRERA CNCR, ");
		sql.append(" COMPROBANTE_PAGO CMPG ");
		sql.append(" WHERE PRS.PRS_ID = FCES.PRS_ID ");
		sql.append(" AND FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_ID = CMPG.FCMT_ID ");
		sql.append(" AND FCES.FCIN_ID = FCIN.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID = CNCR.CNCR_ID ");
		sql.append(" AND CNCR.CRR_ID = "+carreraid+" ");
//		sql.append(" AND CMPG.CMPA_ESTADO IN (2,3,7,6) ");
		sql.append(" AND FCMT.FCMT_PRAC_ID = "+periodoId+" ");
		sql.append(" AND PRS.PRS_IDENTIFICACION = '"+identificacion.toUpperCase()+"'");
		

		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ComprobantePago();
			while (rs.next()) {
				retorno = transformarResultSetComprobantePago(rs);
			}
			
			
		} catch (Exception e) {
			retorno = null;
			throw new ParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MatriculaServicioJdbcImpl.buscar.record.academico.sau.error.tipo.sql")));
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
 
		

		return retorno;
	}
	 
	public List<RecordEstudianteDto> buscarRecordAcademicoSIIU(String identificacion, int carreraId, int periodoInicioId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" rces.");sql.append(JdbcConstantes.RCES_ID);
		sql.append(" ,rces.");sql.append(JdbcConstantes.RCES_MLCRPR_ID);
		sql.append(" ,rces.");sql.append(JdbcConstantes.RCES_ESTADO);
		sql.append(" ,rces.");sql.append(JdbcConstantes.RCES_OBSERVACION);
		sql.append(" ,fces.");sql.append(JdbcConstantes.FCES_ID);
		sql.append(" ,prs.");sql.append(JdbcConstantes.PRS_ID);
		sql.append(" ,prs.");sql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sql.append(" ,mlcrmt.");sql.append(JdbcConstantes.MLCRMT_ID);
		sql.append(" ,mlcrmt.");sql.append(JdbcConstantes.MLCRMT_MTR_ID);
		sql.append(" FROM ");sql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sql.append(" rces ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sql.append(" fces ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_PERSONA);sql.append(" prs ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sql.append(" mlcrpr ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sql.append(" mlcrmt ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sql.append(" mlcr ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_PARALELO);sql.append(" prl ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sql.append(" prac ");
		sql.append(" WHERE ");sql.append(" rces.");sql.append(JdbcConstantes.RCES_FCES_ID);
		sql.append(" = ");sql.append(" fces.");sql.append(JdbcConstantes.FCES_ID);
		sql.append(" AND ");sql.append(" fces.");sql.append(JdbcConstantes.FCES_PRS_ID);
		sql.append(" = ");sql.append(" prs.");sql.append(JdbcConstantes.PRS_ID);
		sql.append(" AND ");sql.append(" rces.");sql.append(JdbcConstantes.RCES_MLCRPR_ID);
		sql.append(" = ");sql.append(" mlcrpr.");sql.append(JdbcConstantes.MLCRPR_ID);
		sql.append(" AND ");sql.append(" mlcrpr.");sql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
		sql.append(" = ");sql.append(" mlcrmt.");sql.append(JdbcConstantes.MLCRMT_ID);
		sql.append(" AND ");sql.append(" mlcrmt.");sql.append(JdbcConstantes.MLCRMT_MLCR_ID);
		sql.append(" = ");sql.append(" mlcr.");sql.append(JdbcConstantes.MLCR_ID);
		sql.append(" AND ");sql.append(" mlcr.");sql.append(JdbcConstantes.MLCR_CRR_ID);
		sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" AND ");sql.append(" mlcrpr.");sql.append(JdbcConstantes.MLCRPR_PRL_ID);
		sql.append(" = ");sql.append(" prl.");sql.append(JdbcConstantes.PRL_ID);
		sql.append(" AND ");sql.append(" prac.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" = ");sql.append(" prl.");sql.append(JdbcConstantes.PRL_PRAC_ID); 
		
		sql.append(" AND ");sql.append(" prs.");sql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sql.append(" = ? ");
		sql.append(" AND ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" = ? ");
		sql.append(" AND ");sql.append(" prac.");sql.append(JdbcConstantes.PRAC_TIPO);
		sql.append(" = ");sql.append(" ? ");
		
		if(periodoInicioId != GeneralesConstantes.APP_ID_BASE){ 
			sql.append(" AND ");sql.append(" prac.");sql.append(JdbcConstantes.PRAC_ID);
			sql.append(" >=  ");sql.append(" ? ");
		}

		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			
			if(periodoInicioId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(4, periodoInicioId);
			}
			
			rs = pstmt.executeQuery();
			

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoDto(rs));
			}

		} catch (Exception e) {
			retorno = null;
			e.printStackTrace();
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
		}

		return retorno;
	}
	
	public	List<MatriculaDto> buscarMatriculasActivas(String prsIdentificacion, List<String> estados, List<String> pracTipos) throws MatriculaNoEncontradoException, MatriculaException{

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		List<MatriculaDto> retorno = null;

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
			sql.append(" AND fcmt.FCMT_PRAC_ID in  ");
			sql.append(" (SELECT PRAC_ID FROM PERIODO_ACADEMICO WHERE PRAC_ESTADO in ( ");
			sql.append(estados.toString().replace("[", "").replace("]", ""));
			sql.append(" ) AND PRAC_TIPO in ( ");
			sql.append(pracTipos.toString().replace("[", "").replace("]", ""));
			sql.append(" )) ");
			sql.append(" order by MLCRMT.NVL_ID ASC ");


			con = dsSiiu.getConnection();

			if (con != null) {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, prsIdentificacion.toUpperCase());
				rs = pstmt.executeQuery();

				retorno = new ArrayList<>();
				while (rs.next()) {
					retorno.add(transformarResulSetAMatriculaDto(rs));
				}
			}
			

		} catch (Exception e) {
			throw new MatriculaException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MatriculaNoEncontradoException("No se encontró resultados para los parámetros ingresados.");				
		}
		
		return retorno;
	}
	
	

	/**
	 * Método que permite recuperar el record academico del estudiante del SIIU.
	 * @author fgguzman 
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carrerasId - id de las carreras.
	 * @param pracTipo - tipo de periodo academico.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	public List<RecordEstudianteDto> buscarRecordEstudianteSIIU(String identificacion, List<String> carrerasId, int pracTipo) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT prac.PRAC_ID, ");
		sql.append("   prac.prac_descripcion, ");
		sql.append("   dpn.dpn_descripcion, ");
		sql.append("   crr.CRR_DESCRIPCION, ");
		sql.append("   prs.prs_identificacion, ");
		sql.append("   prs.prs_primer_apellido  || ' ' || prs_segundo_apellido  || ' '  || prs.prs_nombres ESTUDIANTE, ");
		sql.append("   prs.PRS_SEXO, ");
		sql.append("   mlcrmt.NVL_ID, ");
		sql.append("   mtr.mtr_codigo, ");
		sql.append("   mtr.mtr_descripcion, ");
		sql.append("   dtmt.dtmt_numero numero_matricula, ");
		sql.append("   clf.clf_nota1, ");
		sql.append("   clf.clf_nota2, ");
		sql.append("   clf.clf_supletorio, ");
		sql.append("   clf.clf_nota_final_semestre,   ");
		sql.append("   clf.clf_promedio_asistencia, ");
		sql.append("   clf.clf_asistencia1, ");
		sql.append("   clf.clf_asistencia2, ");
		sql.append("   clf.clf_asistencia_docente1, ");
		sql.append("   clf.clf_asistencia_docente2, ");
		sql.append("   CASE ");
		sql.append("     WHEN rces.rces_estado =0 ");
		sql.append("     THEN 'MATRICULADO' ");
		sql.append("     WHEN rces.rces_estado =1 ");
		sql.append("     THEN 'APROBADO' ");
		sql.append("     WHEN rces.rces_estado =2 ");
		sql.append("     THEN 'REPROBADO' ");
		sql.append("     WHEN rces.rces_estado =3 ");
		sql.append("     THEN 'RETIRO APROBADO' ");
		sql.append("     WHEN rces.rces_estado =4 ");
		sql.append("     THEN 'MIGRADO SAU' ");
		sql.append("     WHEN rces.rces_estado =5 ");
		sql.append("     THEN 'ESTADO CONVALIDADO' ");
		sql.append("     WHEN rces.rces_estado =6 ");
		sql.append("     THEN 'RETIRO SOLICITADO' ");
		sql.append("     WHEN rces.rces_estado =7 ");
		sql.append("     THEN 'RECUPERACION' ");
		sql.append("     WHEN rces.rces_estado =8 ");
		sql.append("     THEN 'HOMOLOGACION' ");
		sql.append("     WHEN rces.rces_estado =9 ");
		sql.append("     THEN 'RETIRO SOLICITADO A CONSEJO DIRECTIVO' ");
		sql.append("     WHEN rces.rces_estado =10 ");
		sql.append("     THEN 'ANULACIÓN MATRÍCULA' ");
		sql.append("     WHEN rces.rces_estado =11 ");
		sql.append("     THEN 'RETIRO SOLICITADO POR SITUACIONES FORTUITAS O FUERZA MAYOR' ");
		sql.append("     WHEN rces.rces_estado =12 ");
		sql.append("     THEN 'RETIRO POR SITUACIONES FORTUITAS O FUERZA MAYOR' ");
		sql.append("   END AS RCES_MATERIA_ESTADO, ");
		sql.append("   fcmt.FCMT_PRAC_ID, ");
		sql.append("   crr.CRR_ID   ");
		sql.append(" FROM ficha_estudiante fces ");
		sql.append(" LEFT JOIN ficha_inscripcion fcin ON fces.fcin_id = fcin.fcin_id ");
		sql.append(" LEFT JOIN ficha_matricula fcmt ON fces.fces_id = fcmt.fces_id ");
		sql.append(" LEFT JOIN comprobante_pago cmpa ON fcmt.fcmt_id = cmpa.fcmt_id ");
		sql.append(" LEFT JOIN detalle_matricula dtmt ON cmpa.cmpa_id = dtmt.cmpa_id ");
		sql.append(" LEFT JOIN malla_curricular_paralelo mlcrpr ON dtmt.mlcrpr_id = mlcrpr.mlcrpr_id ");
		sql.append(" LEFT JOIN paralelo prl ON prl.prl_id = mlcrpr.prl_id ");
		sql.append(" LEFT JOIN malla_curricular_materia mlcrmt ON mlcrpr.mlcrmt_id = mlcrmt.mlcrmt_id ");
		sql.append(" LEFT JOIN nivel NVL ON mlcrmt.nvl_id = nvl.nvl_id ");
		sql.append(" LEFT JOIN persona prs ON fces.prs_id = prs.prs_id ");
		sql.append(" LEFT JOIN configuracion_carrera cncr ON fcin.cncr_id = cncr.cncr_id ");
		sql.append(" LEFT JOIN carrera crr ON cncr.crr_id = crr.crr_id ");
		sql.append(" LEFT JOIN malla_curricular mlcr ON mlcr.crr_id = crr.crr_id ");
		sql.append(" LEFT JOIN periodo_academico prac ON prl.prac_id = prac.prac_id ");
		sql.append(" LEFT JOIN dependencia dpn ON crr.dpn_id = dpn.dpn_id ");
		sql.append(" LEFT JOIN materia mtr ON mtr.mtr_id = mlcrmt.mtr_id ");
		sql.append(" LEFT JOIN record_estudiante rces ON fces.fces_id = rces.fces_id AND rces.mlcrpr_id = mlcrpr.mlcrpr_id  ");
		sql.append(" LEFT JOIN calificacion clf ON rces.rces_id = clf.rces_id ");
		sql.append(" LEFT JOIN modalidad mdl ON mdl.mdl_id = cncr.mdl_id ");
		sql.append(" WHERE mtr.mtr_estado =  ");sql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
		sql.append(" AND ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);sql.append(" in (  ");sql.append(carrerasId.toString().replace("[", "").replace("]", ""));sql.append(" )  ");
		sql.append(" AND ");sql.append(" prs.");sql.append(JdbcConstantes.PRS_IDENTIFICACION);sql.append(" = ? ");
//		sql.append(" AND ");sql.append(" prac.");sql.append(JdbcConstantes.PRAC_TIPO);sql.append(" = ");sql.append(" ? ");
		sql.append(" GROUP BY  prac.PRAC_ID, ");
		sql.append("   prac.prac_descripcion, ");
		sql.append("   dpn.dpn_descripcion, ");
		sql.append("   crr.CRR_DESCRIPCION, ");
		sql.append("   prs.prs_identificacion, ");
		sql.append("   prs.prs_primer_apellido, ");
		sql.append("   prs_segundo_apellido, ");
		sql.append("   prs.prs_nombres, ");
		sql.append("   prs.PRS_SEXO, ");
		sql.append("   mlcrmt.NVL_ID, ");
		sql.append("   mtr.mtr_codigo, ");
		sql.append("   mtr.mtr_descripcion, ");
		sql.append("   dtmt.dtmt_numero, ");
		sql.append("   clf.clf_nota1, ");
		sql.append("   clf.clf_nota2, ");
		sql.append("   clf.clf_supletorio, ");
		sql.append("   clf.clf_nota_final_semestre,   ");
		sql.append("   clf.clf_promedio_asistencia, ");
		sql.append("   clf.clf_asistencia1, ");
		sql.append("   clf.clf_asistencia2, ");
		sql.append("   clf.clf_asistencia_docente1, ");
		sql.append("   clf.clf_asistencia_docente2, ");
		sql.append("   rces.rces_estado,   ");
		sql.append("   fcmt.FCMT_PRAC_ID,   ");
		sql.append("   crr.CRR_ID   ");
		sql.append(" ORDER BY prac.PRAC_ID  ");
		
		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
//			pstmt.setInt(2, pracTipo);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSuficienciaCulturaFisica(rs, RecordEstudianteConstantes.RCES_ORIGEN_SIIU));
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
			throw new RecordEstudianteNoEncontradoException("El estudiante aun no dispone de record academico en el SIIU.");
		}

		return retorno;
	}

	
	
	public List<RecordEstudianteDto> buscarRecordAcademicoSuficienciaCulturaFisicaSAU(String identificacion)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT DISTINCT p.grupo AS grupo, ");
		sql.append("  p.per_descripcion as periodo, ");
		sql.append("  f.nombre as facultad, ");
		sql.append("  ee.espe_nombre as carrera, ");
		sql.append("  e.est_cedula as cedula, ");
		sql.append("  e.est_apellido_paterno ||' '|| e.est_apellido_materno ||' '||e.est_nombres as estudiante, ");
		sql.append("  E.EST_SEXO AS SEXO, ");
		sql.append("  mat.mat_curso as semestre, ");
		sql.append("  mat.mat_codigo_uce as codigo_materia, ");
		sql.append("  mat.mat_nombre as materia, ");
		sql.append("  mtr.matr_numero as numero_matricula,  ");
		sql.append("  rm.rmat_nota_uno as nota_1, ");
		sql.append("  rm.rmat_nota_dos as nota_2, ");
		sql.append("  rm.rmat_nota_tres as recuperacion, ");
		sql.append("  rm.nota_final as nota_final, ");
		sql.append("  rm.asistencia_final as PORCENTAJE_ASISTENCIA,  ");
		sql.append("  rm.rmat_asistencia_uno, ");
		sql.append("  rm.rmat_asistencia_dos, ");
		sql.append("  rm.rmat_asistencia_total_uno, ");
		sql.append("  rm.rmat_asistencia_total_dos,");
		sql.append("  CASE rm.rmat_aprobado WHEN -1 THEN 'ANULADO' WHEN 0 THEN '??0' WHEN 1 THEN 'INSCRITO' WHEN 2 THEN 'MATRICULADO' WHEN 3 THEN 'APROBADO' WHEN 4 THEN 'REPROBADO' WHEN 5 THEN   'NINGUNA_NOTA' WHEN 6 THEN 'SUSPENSO' WHEN 7 THEN 'PERDIDO_POR_ASISTENCIA' WHEN 10 THEN 'CONVALIDADO' ELSE 'OTHER' END as estado, ");
		sql.append("  mtr.matr_codigo,");
		sql.append("  ee.espe_codigo");
		sql.append("  FROM registro_materias rm ");
		sql.append("  INNER JOIN materias mat ON rm.mat_codigo = mat.mat_codigo ");
		sql.append("  INNER JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo ");
		sql.append("  INNER JOIN periodos p ON mtr.per_codigo = p.per_codigo ");
		sql.append("  INNER JOIN estudiantes e ON mtr.est_codigo = e.est_codigo ");
		sql.append("  INNER JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo ");
		sql.append("  INNER JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND s.esc_codigo = s.esc_codigo ");
		sql.append("  INNER JOIN facultad f ON s.id_facultad = f.id ");
		sql.append("  WHERE f.id = 20 ");
		sql.append("  and e.est_cedula = '"+identificacion+"' ");
		sql.append("  and ee.espe_codigo IN (170,2268190,2268191,2268192,2268193,2268194,2268195,2268196,2268198,2268199,2570291) ");
		sql.append("  AND rm.rmat_aprobado > 1  ");
		sql.append("  GROUP BY mtr.matr_codigo,ee.espe_codigo, f.nombre,ee.espe_nombre, mtr.matr_numero,rm.asistencia_final,mat.mat_curso,e.est_cedula,e.est_apellido_paterno,e.est_apellido_materno,e.est_nombres, f.id,mat.mat_codigo_uce,rm.rmat_nota_uno,rm.rmat_nota_dos,rm.rmat_nota_tres,rm.nota_final,p.per_descripcion,mat.mat_nombre,rm.rmat_aprobado,E.EST_SEXO,p.grupo,rm.rmat_asistencia_uno, rm.rmat_asistencia_dos, rm.rmat_asistencia_total_uno, rm.rmat_asistencia_total_dos ");
		sql.append("  ORDER BY 1 ");
		
		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSuficienciaCulturaFisica(rs,RecordEstudianteConstantes.RCES_ORIGEN_SAU));
			}
			
		} catch (Exception e) {
			retorno = null; 
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
				e.printStackTrace();
			}
		}

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
		}

		return retorno;
	}
	
	public List<RecordEstudianteDto> buscarRecordEstudianteSIIU(String identificacion, Integer[] estadosMateria) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT dpn.dpn_descripcion facultad, ");
		sql.append("   NVL(SUBSTR(crr.crr_detalle, 0, INSTR(crr.crr_detalle, '(') - 1), crr.crr_detalle) carrera, ");
		sql.append("   crr.crr_id, ");
		sql.append("   mdl.mdl_descripcion MODALIDAD, ");
		sql.append("   0 folio, ");
		sql.append("   0 indice, ");
		sql.append("   CASE fcmt.fcmt_nivel_ubicacion ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'PRIMERO' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'SEGUNDO' ");
		sql.append("     WHEN 3 ");
		sql.append("     THEN 'TERCERO' ");
		sql.append("     WHEN 4 ");
		sql.append("     THEN 'CUARTO' ");
		sql.append("     WHEN 5 ");
		sql.append("     THEN 'QUINTO' ");
		sql.append("     WHEN 6 ");
		sql.append("     THEN 'SEXTO' ");
		sql.append("     WHEN 7 ");
		sql.append("     THEN 'SEPTIMO' ");
		sql.append("     WHEN 8 ");
		sql.append("     THEN 'OCTAVO' ");
		sql.append("     WHEN 9 ");
		sql.append("     THEN 'NOVENO' ");
		sql.append("     WHEN 10 ");
		sql.append("     THEN 'DECIMO' ");
		sql.append("     WHEN 11 ");
		sql.append("     THEN 'NIVELACION' ");
		sql.append("     WHEN 12 ");
		sql.append("     THEN 'DECIMO PRIMERO' ");
		sql.append("   END semestre, ");
		sql.append("   fcmt.fcmt_nivel_ubicacion, ");
		sql.append("   prac.prac_id, ");
		sql.append("   prac.prac_descripcion periodo, ");
		sql.append("   prs.prs_id codigo_estudiantil, ");
		sql.append("   prs.prs_identificacion cedula, ");
		sql.append("   prs.prs_primer_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs_segundo_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs.prs_nombres nombres, ");
		sql.append("   mtr.mtr_id materia_id, ");
		sql.append("   mtr.mtr_codigo codigo_materia, ");
		sql.append("   mtr.mtr_descripcion materia, ");
		sql.append("   clf.clf_nota1 Nota_1, ");
		sql.append("   clf.clf_nota2 Nota_2, ");
		sql.append("   clf.clf_supletorio Recuperacion, ");
		sql.append("   clf.clf_nota_final_semestre nota_final, ");
		sql.append("   (SELECT DISTINCT CAST(NVL((clf.clf_nota1 + clf.clf_nota2) / 2, 0) AS DECIMAL(5, 2)) ");
		sql.append("   FROM ficha_estudiante fces1 ");
		sql.append("   LEFT JOIN ficha_inscripcion fcin1 ");
		sql.append("   ON fces1.fcin_id = fcin1.fcin_id ");
		sql.append("   LEFT JOIN ficha_matricula fcmt1 ");
		sql.append("   ON fces1.fces_id = fcmt1.fces_id ");
		sql.append("   LEFT JOIN comprobante_pago cmpa1 ");
		sql.append("   ON fcmt1.fcmt_id = cmpa1.fcmt_id ");
		sql.append("   LEFT JOIN detalle_matricula dtmt1 ");
		sql.append("   ON cmpa1.cmpa_id = dtmt1.cmpa_id ");
		sql.append("   LEFT JOIN malla_curricular_paralelo mlcrpr1 ");
		sql.append("   ON dtmt1.mlcrpr_id = mlcrpr1.mlcrpr_id ");
		sql.append("   LEFT JOIN paralelo prl1 ");
		sql.append("   ON prl1.prl_id = mlcrpr1.prl_id ");
		sql.append("   LEFT JOIN persona prs1 ");
		sql.append("   ON fces1.prs_id = prs1.prs_id ");
		sql.append("   LEFT JOIN configuracion_carrera cncr1 ");
		sql.append("   ON fcin1.cncr_id = cncr1.cncr_id ");
		sql.append("   LEFT JOIN carrera crr1 ");
		sql.append("   ON cncr1.crr_id = crr1.crr_id ");
		sql.append("   LEFT JOIN periodo_academico prac1 ");
		sql.append("   ON prl1.prac_id   = prac1.prac_id ");
		sql.append("   AND fcin1.prac_id = prac1.prac_id ");
		sql.append("   LEFT JOIN dependencia dpn1 ");
		sql.append("   ON crr1.dpn_id = dpn1.dpn_id ");
		sql.append("   LEFT JOIN record_estudiante rces1 ");
		sql.append("   ON fces1.fces_id    = rces1.fces_id ");
		sql.append("   AND rces1.mlcrpr_id = mlcrpr1.mlcrpr_id ");
		sql.append("   LEFT JOIN calificacion clf1 ");
		sql.append("   ON rces1.rces_id              = clf1.rces_id ");
		sql.append("   WHERE prs1.prs_identificacion = prs.prs_identificacion ");
		sql.append("   AND rces1.rces_estado        IN (1, 2, 8) ");
		sql.append("   AND cncr1.crr_id              = cncr.crr_id ");
		sql.append("   ) promedio, ");
		sql.append("   clf.clf_promedio_asistencia asistencia, ");
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
		sql.append("   CASE ");
		sql.append("     WHEN mtr.mtr_horas IS NULL ");
		sql.append("     THEN mtr.mtr_creditos ");
		sql.append("     ELSE mtr.mtr_horas ");
		sql.append("   END numero_creditos, ");
		sql.append("   dtmt.dtmt_numero numero_matricula, ");
		sql.append("   fcmt.fcmt_id codigo_matricula, ");
		sql.append("   (SELECT ");
		sql.append("     CASE ");
		sql.append("       WHEN SUM(mtr1.mtr_creditos) IS NULL ");
		sql.append("       THEN SUM(mtr1.mtr_horas) ");
		sql.append("       ELSE SUM(mtr1.mtr_creditos) ");
		sql.append("     END ");
		sql.append("   FROM malla_curricular mlcr1, ");
		sql.append("     malla_curricular_materia mlcrmt1, ");
		sql.append("     materia mtr1, ");
		sql.append("     carrera crr1, ");
		sql.append("     dependencia dpn1 ");
		sql.append("   WHERE mlcr1.mlcr_id = mlcrmt1.mlcr_id ");
		sql.append("   AND mlcrmt1.mtr_id  = mtr1.mtr_id ");
		sql.append("   AND mlcr1.crr_id    = crr1.crr_id ");
		sql.append("   AND crr1.dpn_id     = dpn1.dpn_id ");
		sql.append("   AND mtr1.mtr_estado = 0 ");
		sql.append("   AND MTR1.timt_id   IN (2, 3, 9, 14) ");
		sql.append("   AND crr1.crr_id     = crr.crr_id ");
		sql.append("   ) CREDITOS_MALLA, ");
		sql.append("   ( SELECT DISTINCT CAST(AVG(clf1.clf_nota_final_semestre)/2 AS DECIMAL(5,2)) ");
		sql.append("   FROM ficha_estudiante fces1 ");
		sql.append("   LEFT JOIN ficha_inscripcion fcin1 ");
		sql.append("   ON fces1.fcin_id = fcin1.fcin_id ");
		sql.append("   LEFT JOIN ficha_matricula fcmt1 ");
		sql.append("   ON fces1.fces_id = fcmt1.fces_id ");
		sql.append("   LEFT JOIN comprobante_pago cmpa1 ");
		sql.append("   ON fcmt1.fcmt_id = cmpa1.fcmt_id ");
		sql.append("   LEFT JOIN detalle_matricula dtmt1 ");
		sql.append("   ON cmpa1.cmpa_id = dtmt1.cmpa_id ");
		sql.append("   LEFT JOIN malla_curricular_paralelo mlcrpr1 ");
		sql.append("   ON dtmt1.mlcrpr_id = mlcrpr1.mlcrpr_id ");
		sql.append("   LEFT JOIN paralelo prl1 ");
		sql.append("   ON prl1.prl_id = mlcrpr1.prl_id ");
		sql.append("   LEFT JOIN persona prs1 ");
		sql.append("   ON fces1.prs_id = prs1.prs_id ");
		sql.append("   LEFT JOIN configuracion_carrera cncr1 ");
		sql.append("   ON fcin1.cncr_id = cncr1.cncr_id ");
		sql.append("   LEFT JOIN carrera crr1 ");
		sql.append("   ON cncr1.crr_id = crr1.crr_id ");
		sql.append("   LEFT JOIN periodo_academico prac1 ");
		sql.append("   ON prl1.prac_id   = prac1.prac_id ");
		sql.append("   AND fcin1.prac_id = prac1.prac_id ");
		sql.append("   LEFT JOIN dependencia dpn1 ");
		sql.append("   ON crr1.dpn_id = dpn1.dpn_id ");
		sql.append("   LEFT JOIN record_estudiante rces1 ");
		sql.append("   ON fces1.fces_id    = rces1.fces_id ");
		sql.append("   AND rces1.mlcrpr_id = mlcrpr1.mlcrpr_id ");
		sql.append("   LEFT JOIN calificacion clf1 ");
		sql.append("   ON rces1.rces_id              = clf1.rces_id ");
		sql.append("   WHERE prs1.prs_identificacion = prs.prs_identificacion ");
		sql.append("   AND rces1.rces_estado        IN (1) ");
		sql.append("   AND cncr1.crr_id              = cncr.crr_id ");
		sql.append("   )promedio_general, ");
		sql.append("   clf.clf_asistencia1, ");
		sql.append("   clf.clf_asistencia2, ");
		sql.append("   clf.clf_asistencia_docente1, ");
		sql.append("   clf.clf_asistencia_docente2, ");
		sql.append("   CASE fcmt.fcmt_tipo ");
		sql.append("     WHEN 0 ");
		sql.append("     THEN 'ORDINARIA' ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'EXTRAORDINARIA' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'ESPECIAL' ");
		sql.append("   END TIPO_MATRICULA , ");
		sql.append("   clf.clf_suma_p1_p2 suma_h1_h2, ");
		sql.append("   clf.clf_param_recuperacion1, ");
		sql.append("   clf.clf_param_recuperacion2, ");
		sql.append("   CASE nvl.nvl_numeral ");
		sql.append("     WHEN 1 ");
		sql.append("     THEN 'PRIMERO' ");
		sql.append("     WHEN 2 ");
		sql.append("     THEN 'SEGUNDO' ");
		sql.append("     WHEN 3 ");
		sql.append("     THEN 'TERCERO' ");
		sql.append("     WHEN 4 ");
		sql.append("     THEN 'CUARTO' ");
		sql.append("     WHEN 5 ");
		sql.append("     THEN 'QUINTO' ");
		sql.append("     WHEN 6 ");
		sql.append("     THEN 'SEXTO' ");
		sql.append("     WHEN 7 ");
		sql.append("     THEN 'SEPTIMO' ");
		sql.append("     WHEN 8 ");
		sql.append("     THEN 'OCTAVO' ");
		sql.append("     WHEN 9 ");
		sql.append("     THEN 'NOVENO' ");
		sql.append("     WHEN 10 ");
		sql.append("     THEN 'DECIMO' ");
		sql.append("     WHEN 11 ");
		sql.append("     THEN 'NIVELACION' ");
		sql.append("     WHEN 12 ");
		sql.append("     THEN 'DECIMO PRIMERO' ");
		sql.append("   END nivel, ");
		sql.append("   rces.rces_estado, ");
		sql.append("   nvl.nvl_numeral ");
		sql.append(" FROM ficha_estudiante fces ");
		sql.append(" LEFT JOIN ficha_inscripcion fcin ");
		sql.append(" ON fces.fcin_id = fcin.fcin_id ");
		sql.append(" LEFT JOIN ficha_matricula fcmt ");
		sql.append(" ON fces.fces_id = fcmt.fces_id ");
		sql.append(" LEFT JOIN comprobante_pago cmpa ");
		sql.append(" ON fcmt.fcmt_id = cmpa.fcmt_id ");
		sql.append(" LEFT JOIN detalle_matricula dtmt ");
		sql.append(" ON cmpa.cmpa_id = dtmt.cmpa_id ");
		sql.append(" LEFT JOIN malla_curricular_paralelo mlcrpr ");
		sql.append(" ON dtmt.mlcrpr_id = mlcrpr.mlcrpr_id ");
		sql.append(" LEFT JOIN paralelo prl ");
		sql.append(" ON prl.prl_id = mlcrpr.prl_id ");
		sql.append(" LEFT JOIN malla_curricular_materia mlcrmt ");
		sql.append(" ON mlcrpr.mlcrmt_id = mlcrmt.mlcrmt_id ");
		sql.append(" LEFT JOIN nivel NVL ");
		sql.append(" ON mlcrmt.nvl_id = nvl.nvl_id ");
		sql.append(" LEFT JOIN persona prs ");
		sql.append(" ON fces.prs_id = prs.prs_id ");
		sql.append(" LEFT JOIN configuracion_carrera cncr ");
		sql.append(" ON fcin.cncr_id = cncr.cncr_id ");
		sql.append(" LEFT JOIN carrera crr ");
		sql.append(" ON cncr.crr_id = crr.crr_id ");
		sql.append(" LEFT JOIN malla_curricular mlcr ");
		sql.append(" ON mlcr.crr_id = crr.crr_id ");
		sql.append(" LEFT JOIN periodo_academico prac ");
		sql.append(" ON prl.prac_id = prac.prac_id ");
		sql.append(" LEFT JOIN dependencia dpn ");
		sql.append(" ON crr.dpn_id = dpn.dpn_id ");
		sql.append(" LEFT JOIN materia mtr ");
		sql.append(" ON mtr.mtr_id = mlcrmt.mtr_id ");
		sql.append(" LEFT JOIN record_estudiante rces ");
		sql.append(" ON fces.fces_id    = rces.fces_id ");
		sql.append(" AND rces.mlcrpr_id = mlcrpr.mlcrpr_id ");
		sql.append(" LEFT JOIN calificacion clf ");
		sql.append(" ON rces.rces_id = clf.rces_id ");
		sql.append(" LEFT JOIN modalidad mdl ");
		sql.append(" ON mdl.mdl_id                = cncr.mdl_id ");
		sql.append(" WHERE prs.prs_identificacion = '"+identificacion.toUpperCase()+"'  ");
		sql.append(" AND mtr.mtr_estado           IN ");sql.append(Arrays.toString(estadosMateria).replace("[", "(").replace("]", ")"));
		sql.append(" GROUP BY dpn.dpn_descripcion, ");
		sql.append("   crr.crr_detalle, ");
		sql.append("   mdl.mdl_descripcion, ");
		sql.append("   fcmt.fcmt_nivel_ubicacion, ");
		sql.append("   prac.prac_id, ");
		sql.append("   prac.prac_descripcion, ");
		sql.append("   prs.prs_id, ");
		sql.append("   prs.prs_identificacion, ");
		sql.append("   prs.prs_primer_apellido, ");
		sql.append("   prs_segundo_apellido, ");
		sql.append("   prs.prs_nombres, ");
		sql.append("   mtr.mtr_codigo, ");
		sql.append("   mtr.mtr_descripcion, ");
		sql.append("   clf.clf_nota1, ");
		sql.append("   clf.clf_nota2, ");
		sql.append("   clf.clf_supletorio, ");
		sql.append("   clf.clf_nota_final_semestre, ");
		sql.append("   rces.rces_estado, ");
		sql.append("   mtr.mtr_creditos, ");
		sql.append("   dtmt.dtmt_numero, ");
		sql.append("   clf.clf_asistencia_total, ");
		sql.append("   crr.crr_id, ");
		sql.append("   dpn.dpn_id, ");
		sql.append("   prac.prac_id, ");
		sql.append("   cncr.crr_id, ");
		sql.append("   fcmt.fcmt_id, ");
		sql.append("   mtr.mtr_horas, ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   clf.clf_asistencia1, ");
		sql.append("   clf.clf_asistencia2, ");
		sql.append("   clf.clf_asistencia_docente1, ");
		sql.append("   clf.clf_asistencia_docente2, ");
		sql.append("   clf.clf_promedio_asistencia, ");
		sql.append("   fcmt.fcmt_tipo, ");
		sql.append("   clf.clf_suma_p1_p2, ");
		sql.append("   clf.clf_param_recuperacion1, ");
		sql.append("   clf.clf_param_recuperacion2, ");
		sql.append("   nvl.nvl_numeral, ");
		sql.append("   mtr.mtr_id ");
		sql.append(" ORDER BY prac.prac_descripcion ");

		
		try{
			
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoDto(rs, RecordEstudianteConstantes.RCES_ORIGEN_SIIU));
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
	
	
	
	
	public List<RecordEstudianteDto> buscarRecordAcademicoSAU(String identificacion, Integer[] estadosMateria)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT facultad, ");
		sql.append("   carrera, ");
		sql.append("   CODIGO_CARRERA, ");
		sql.append("   modalidad, ");
		sql.append("   Folio, ");
		sql.append("   indice, ");
		sql.append("   semestre, ");
		sql.append("   NUMERAL, ");
		sql.append("   periodo_id, ");
		sql.append("   periodo, ");
		sql.append("   codigo_estudiantil, ");
		sql.append("   cedula, ");
		sql.append("   nombres, ");
		sql.append("   materia_id, ");
		sql.append("   codigo_materia, ");
		sql.append("   materia, ");
		sql.append("   Nota_1, ");
		sql.append("   Nota_2, ");
		sql.append("   Recuperacion, ");
		sql.append("   nota_final, ");
		sql.append("   AVG(nota_final / 2) AS promedio, ");
		sql.append("   Asistencia, ");
		sql.append("   estado, ");
		sql.append("   Numero_creditos, ");
		sql.append("   numero_matricula, ");
		sql.append("   codigo_matricula, ");
		sql.append("   Creditos_carrera, ");
		sql.append("   promedio_general, ");
		sql.append("   asist_1, ");
		sql.append("   asist_2, ");
		sql.append("   asist_total_1, ");
		sql.append("   asist_total_2, ");
		sql.append("   tipo_matricula, ");
		sql.append("   suma_h1_h2, ");
		sql.append("   recup40, ");
		sql.append("   recup60, ");
		sql.append("   nivel, ");
		sql.append("   estado_value, ");
		sql.append("   nivel_value ");
		sql.append(" FROM ");
		sql.append("   (SELECT DISTINCT f.nombre facultad, ");
		sql.append("     ee.espe_nombre carrera, ");
		sql.append("     ee.espe_codigo CODIGO_CARRERA, ");
		sql.append("     CASE mtr.modalidad ");
		sql.append("       WHEN 1 ");
		sql.append("       THEN 'PRESENCIAL' ");
		sql.append("       WHEN 2 ");
		sql.append("       THEN 'DISTANCIA' ");
		sql.append("     END                  AS modalidad, ");
		sql.append("     mtr.pkv_folio        AS Folio, ");
		sql.append("     mtr.pkv_folio_indice AS indice, ");
		sql.append("     CASE mtr.matr_curso ");
		sql.append("       WHEN 1 ");
		sql.append("       THEN 'PRIMERO' ");
		sql.append("       WHEN 2 ");
		sql.append("       THEN 'SEGUNDO' ");
		sql.append("       WHEN 3 ");
		sql.append("       THEN 'TERCERO' ");
		sql.append("       WHEN 4 ");
		sql.append("       THEN 'CUARTO' ");
		sql.append("       WHEN 5 ");
		sql.append("       THEN 'QUINTO' ");
		sql.append("       WHEN 6 ");
		sql.append("       THEN 'SEXTO' ");
		sql.append("       WHEN 7 ");
		sql.append("       THEN 'SEPTIMO' ");
		sql.append("       WHEN 8 ");
		sql.append("       THEN 'OCTAVO' ");
		sql.append("       WHEN 9 ");
		sql.append("       THEN 'NOVENO' ");
		sql.append("       WHEN 10 ");
		sql.append("       THEN 'DECIMO' ");
		sql.append("       WHEN 11 ");
		sql.append("       THEN 'DECIMO PRIMERO' ");
		sql.append("     END semestre, ");
		sql.append("     mtr.matr_curso     AS NUMERAL, ");
		sql.append("     p.grupo AS periodo_id, ");
		sql.append("     P .per_descripcion AS periodo, ");
		sql.append("     e.est_codigo       AS codigo_estudiantil, ");
		sql.append("     e.est_cedula cedula, ");
		sql.append("     e.est_apellido_paterno ");
		sql.append("     || ' ' ");
		sql.append("     || e.est_apellido_materno ");
		sql.append("     || ' ' ");
		sql.append("     || e.est_nombres    AS nombres, ");
		sql.append("     mat.mat_codigo      AS materia_id, ");
		sql.append("     mat.mat_codigo_uce  AS codigo_materia, ");
		sql.append("     mat.mat_nombre      AS materia, ");
		sql.append("     rm.rmat_nota_uno    AS Nota_1, ");
		sql.append("     rm.rmat_nota_dos    AS Nota_2, ");
		sql.append("     rm.rmat_nota_tres   AS Recuperacion, ");
		sql.append("     rm.nota_final       AS nota_final, ");
		sql.append("     rm.asistencia_final AS Asistencia, ");
		sql.append("     CASE rm.rmat_aprobado ");
		sql.append("       WHEN -1 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_ANULADO_LABEL);sql.append("'");
		sql.append("       WHEN 0 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL);sql.append("'");
		sql.append("       WHEN 1 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_INSCRITO_LABEL);sql.append("'");
		sql.append("       WHEN 2 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_MATRICULDO_LABEL);sql.append("'");
		sql.append("       WHEN 3 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_APROBADO_LABEL);sql.append("'");
		sql.append("       WHEN 4 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_REPROBADO_LABEL);sql.append("'");
		sql.append("       WHEN 5 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_NINGUNA_NOTA_LABEL);sql.append("'");
		sql.append("       WHEN 6 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_SUSPENSO_LABEL);sql.append("'");
		sql.append("       WHEN 7 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_PERDIDO_POR_ASISTENCIA_LABEL);sql.append("'");
		sql.append("       WHEN 10 ");sql.append("       THEN '");sql.append(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL);sql.append("'");
		sql.append("       ELSE '");sql.append(SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL);sql.append("'");
		sql.append("     END                          AS estado, ");
		sql.append("     mat.mat_numero_creditos AS Numero_creditos, ");
		sql.append("     rm.numero_matricula     AS numero_matricula, ");
		sql.append("     mtr.matr_codigo         AS codigo_matricula, ");
		sql.append("     (SELECT DISTINCT SUM(mat4.mat_numero_creditos) AS Numero_creditos ");
		sql.append("     FROM registro_materias rm4 ");
		sql.append("     INNER JOIN materias mat4 ");
		sql.append("     ON rm4.mat_codigo = mat4.mat_codigo ");
		sql.append("     INNER JOIN matriculas mtr4 ");
		sql.append("     ON rm4.per_codigo  = mtr4.per_codigo ");
		sql.append("     AND rm4.est_codigo = mtr4.est_codigo ");
		sql.append("     INNER JOIN periodos P4 ");
		sql.append("     ON mtr4.per_codigo = P4 .per_codigo ");
		sql.append("     INNER JOIN estudiantes e4 ");
		sql.append("     ON mtr4.est_codigo = e4.est_codigo ");
		sql.append("     INNER JOIN especialidades_escuela ee4 ");
		sql.append("     ON mtr4.espe_codigo = ee4.espe_codigo ");
		sql.append("     INNER JOIN escuelas s4 ");
		sql.append("     ON ee4.esc_codigo = s4.esc_codigo ");
		sql.append("     AND s4.esc_codigo = s4.esc_codigo ");
		sql.append("     INNER JOIN facultad f4 ");
		sql.append("     ON s4.id_facultad = f4. ID ");
		sql.append("     INNER JOIN facultad_autoridades fa4 ");
		sql.append("     ON fa4.facultad_id      = f4.id ");
		sql.append("     WHERE f4. ID            = f. ID ");
		sql.append("     AND ee4.espe_codigo     = ee.espe_codigo ");
		sql.append("     AND s4.esc_codigo NOT  IN (-1, 5, 6, 60, 980, 165) ");
		sql.append("     AND mtr4.matr_estado    > 1 ");
		sql.append("     AND p4.per_descripcion IN ('MIGRACION') ");
		sql.append("     AND rm4.rmat_aprobado  IN (3) ");
		sql.append("     AND e4.est_cedula       = e.est_cedula ");
		sql.append("     ) Migracion, ");
		sql.append("     (SELECT SUM(mcc5.numero_creditos) ");
		sql.append("     FROM malla_curricular_creditos mcc5 ");
		sql.append("     LEFT JOIN malla_curricular mc5 ");
		sql.append("     ON mc5.id = mcc5.malla_curricular_id ");
		sql.append("     LEFT JOIN escuelas esc5 ");
		sql.append("     ON esc5.esc_codigo = mc5.esc_codigo ");
		sql.append("     LEFT JOIN especialidades_escuela espe5 ");
		sql.append("     ON espe5.espe_codigo     = mc5.espe_codigo ");
		sql.append("     WHERE mc5.fecha_termina IS NULL ");
		sql.append("     AND espe5.espe_codigo    = ee.espe_codigo ");
		sql.append("     ) Creditos_carrera, ");
		sql.append("     (SELECT AVG(rm2.nota_final/2) ");
		sql.append("     FROM registro_materias rm2 ");
		sql.append("     INNER JOIN materias mat2 ");
		sql.append("     ON rm2.mat_codigo = mat2.mat_codigo ");
		sql.append("     INNER JOIN matriculas mtr2 ");
		sql.append("     ON rm2.per_codigo  = mtr2.per_codigo ");
		sql.append("     AND rm2.est_codigo = mtr2.est_codigo ");
		sql.append("     INNER JOIN periodos P2 ");
		sql.append("     ON mtr2.per_codigo = P2 .per_codigo ");
		sql.append("     INNER JOIN estudiantes e2 ");
		sql.append("     ON mtr2.est_codigo = e2.est_codigo ");
		sql.append("     INNER JOIN especialidades_escuela ee2 ");
		sql.append("     ON mtr2.espe_codigo = ee2.espe_codigo ");
		sql.append("     INNER JOIN escuelas s2 ");
		sql.append("     ON ee2.esc_codigo = s2.esc_codigo ");
		sql.append("     AND s2.esc_codigo = s2.esc_codigo ");
		sql.append("     INNER JOIN facultad f2 ");
		sql.append("     ON s2.id_facultad = f2. ID ");
		sql.append("     INNER JOIN facultad_autoridades fa2 ");
		sql.append("     ON fa2.facultad_id     = f2.id ");
		sql.append("     WHERE f2. ID           = f. ID ");
		sql.append("     AND ee2.espe_codigo    = ee.espe_codigo ");
		sql.append("     AND s2.esc_codigo NOT IN (-1, 5, 6, 80, 60, 980, 165) ");
		sql.append("     AND mtr2.matr_estado   > 1 ");
		sql.append("     AND rm2.rmat_aprobado IN (3,10) ");
		sql.append("     AND mat2.mat_curso     > 0 ");
		sql.append("     AND mat2.mat_estado    = 1 ");
		sql.append("     AND e2.est_cedula      = e.est_cedula ");
		sql.append("     )promedio_general, ");
		sql.append("     rm.rmat_asistencia_uno asist_1, ");
		sql.append("     rm.rmat_asistencia_dos asist_2, ");
		sql.append("     rm.rmat_asistencia_total_uno asist_total_1, ");
		sql.append("     rm.rmat_asistencia_total_dos asist_total_2, ");
		sql.append("     CASE ");
		sql.append("       WHEN mtr.matr_tipo = 1 ");
		sql.append("       THEN 'ORDINARIA' ");
		sql.append("       WHEN mtr.matr_tipo = 2 ");
		sql.append("       THEN 'EXTRAORDINARIA' ");
		sql.append("       WHEN mtr.matr_tipo = 3 ");
		sql.append("       THEN 'ESPECIAL' ");
		sql.append("     END tipo_matricula, ");
		sql.append("     0 suma_h1_h2, ");
		sql.append("     0 recup40, ");
		sql.append("     0 recup60, ");
		sql.append("     CASE mat.mat_curso ");
		sql.append("       WHEN 1 ");
		sql.append("       THEN 'PRIMERO' ");
		sql.append("       WHEN 2 ");
		sql.append("       THEN 'SEGUNDO' ");
		sql.append("       WHEN 3 ");
		sql.append("       THEN 'TERCERO' ");
		sql.append("       WHEN 4 ");
		sql.append("       THEN 'CUARTO' ");
		sql.append("       WHEN 5 ");
		sql.append("       THEN 'QUINTO' ");
		sql.append("       WHEN 6 ");
		sql.append("       THEN 'SEXTO' ");
		sql.append("       WHEN 7 ");
		sql.append("       THEN 'SEPTIMO' ");
		sql.append("       WHEN 8 ");
		sql.append("       THEN 'OCTAVO' ");
		sql.append("       WHEN 9 ");
		sql.append("       THEN 'NOVENO' ");
		sql.append("       WHEN 10 ");
		sql.append("       THEN 'DECIMO' ");
		sql.append("       WHEN 11 ");
		sql.append("       THEN 'DECIMO PRIMERO' ");
		sql.append("     END nivel, ");
		sql.append("     rm.rmat_aprobado estado_value, ");
		sql.append("     mat.mat_curso nivel_value ");
		sql.append("   FROM registro_materias rm ");
		sql.append("   INNER JOIN materias mat ");
		sql.append("   ON rm.mat_codigo = mat.mat_codigo ");
		sql.append("   INNER JOIN matriculas mtr ");
		sql.append("   ON rm.per_codigo  = mtr.per_codigo ");
		sql.append("   AND rm.est_codigo = mtr.est_codigo ");
		sql.append("   INNER JOIN periodos P ");
		sql.append("   ON mtr.per_codigo = P.per_codigo ");
		sql.append("   INNER JOIN estudiantes e ");
		sql.append("   ON mtr.est_codigo = e.est_codigo ");
		sql.append("   INNER JOIN especialidades_escuela ee ");
		sql.append("   ON mtr.espe_codigo = ee.espe_codigo ");
		sql.append("   INNER JOIN escuelas s ");
		sql.append("   ON ee.esc_codigo = s.esc_codigo ");
		sql.append("   AND s.esc_codigo = s.esc_codigo ");
		sql.append("   INNER JOIN facultad f ");
		sql.append("   ON s.id_facultad = f. ID ");
		sql.append("   INNER JOIN facultad_autoridades fa ");
		sql.append("   ON fa.facultad_id = f.id ");
		sql.append("   INNER JOIN malla_curricular mc ");
		sql.append("   ON ee.espe_codigo           = mc.espe_codigo ");
		sql.append("   AND mat.malla_curricular_id = mc.id ");
		sql.append("   INNER JOIN malla_curricular_creditos mcc ");
		sql.append("   ON mc.id                = mcc.malla_curricular_id ");
		sql.append("   WHERE s.esc_codigo NOT IN (-1, 5, 6, 60, 980, 165) ");
		sql.append("   AND mtr.matr_estado     > 1 ");
		sql.append("   AND mat.mat_curso       > 0 ");
		sql.append("   AND mat.mat_estado in ");sql.append(Arrays.toString(estadosMateria).replace("[", "(").replace("]", ")"));
		sql.append("   AND e.est_cedula        = '"+identificacion+"' ");
		sql.append("   GROUP BY P.per_descripcion, ");
		sql.append("     f.nombre, ");
		sql.append("     ee.espe_nombre, ");
		sql.append("     f.id, ");
		sql.append("     ee.espe_codigo, ");
		sql.append("     mtr.modalidad, ");
		sql.append("     mtr.matr_curso, ");
		sql.append("     nombres, ");
		sql.append("     mat.mat_codigo_uce, ");
		sql.append("     mat.mat_nombre, ");
		sql.append("     rm.rmat_aprobado, ");
		sql.append("     mtr.pkv_folio, ");
		sql.append("     mtr.pkv_folio_indice, ");
		sql.append("     rm.rmat_nota_uno, ");
		sql.append("     rm.rmat_nota_dos, ");
		sql.append("     rm.rmat_nota_tres, ");
		sql.append("     rm.nota_final, ");
		sql.append("     rm.asistencia_final, ");
		sql.append("     mat.mat_numero_creditos, ");
		sql.append("     rm.numero_matricula, ");
		sql.append("     e.est_codigo, ");
		sql.append("     e.est_cedula, ");
		sql.append("     mtr.matr_codigo, ");
		sql.append("     p.grupo, ");
		sql.append("     rm.rmat_asistencia_uno , ");
		sql.append("     rm.rmat_asistencia_dos, ");
		sql.append("     rm.rmat_asistencia_total_uno, ");
		sql.append("     rm.rmat_asistencia_total_dos, ");
		sql.append("     mtr.matr_tipo, ");
		sql.append("     mat.mat_curso, ");
		sql.append("     mat.mat_codigo ");
		sql.append("   ORDER BY nombres, ");
		sql.append("     P .per_descripcion, ");
		sql.append("     mat.mat_codigo_uce, ");
		sql.append("     e.est_cedula, ");
		sql.append("     mat.mat_nombre, ");
		sql.append("     mat.mat_codigo ");
		sql.append("   ) certificado ");
		sql.append(" GROUP BY facultad, ");
		sql.append("   carrera, ");
		sql.append("   modalidad, ");
		sql.append("   Folio, ");
		sql.append("   indice, ");
		sql.append("   semestre, ");
		sql.append("   periodo_id, ");
		sql.append("   periodo, ");
		sql.append("   codigo_estudiantil, ");
		sql.append("   cedula, ");
		sql.append("   nombres, ");
		sql.append("   codigo_materia, ");
		sql.append("   materia, ");
		sql.append("   Nota_1, ");
		sql.append("   Nota_2, ");
		sql.append("   Recuperacion, ");
		sql.append("   nota_final, ");
		sql.append("   Asistencia, ");
		sql.append("   estado, ");
		sql.append("   Numero_creditos, ");
		sql.append("   NUMERAL, ");
		sql.append("   CODIGO_CARRERA, ");
		sql.append("   numero_matricula, ");
		sql.append("   codigo_matricula, ");
		sql.append("   Creditos_carrera, ");
		sql.append("   migracion, ");
		sql.append("   promedio_general, ");
		sql.append("   asist_1, ");
		sql.append("   asist_2, ");
		sql.append("   asist_total_1, ");
		sql.append("   asist_total_2, ");
		sql.append("   tipo_matricula, ");
		sql.append("   suma_h1_h2, ");
		sql.append("   recup40, ");
		sql.append("   recup60, ");
		sql.append("   nivel, ");
		sql.append("   estado_value, ");
		sql.append("   nivel_value, ");
		sql.append("   materia_id ");
		
		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAhistorialRecordAcademicoDto(rs, RecordEstudianteConstantes.RCES_ORIGEN_SAU));
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

	
	public List<RecordEstudianteDto> buscarEstudiantesMatriculados(int paraleloId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
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
		sql.append("   FCMT.FCMT_FECHA_MATRICULA, ");
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
		sql.append(" WHERE MLCRPR.MLCRPR_ID = ? ");
		sql.append(" ORDER BY  CRR.CRR_TIPO,  PRAC.PRAC_ID, CRR.CRR_DESCRIPCION, NVL.NVL_NUMERAL, MTR.MTR_CODIGO ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, paraleloId);
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
		if (!retorno.isEmpty()) {
			// setear atributos propios del SIIU
			for (RecordEstudianteDto item : retorno) {
				asignarEstadoAsignaturaEnRecord(item);
				asignarNivelMatricula(item);
				asignarTipoMatriculacion(item);
			}
		}else {
			throw new RecordEstudianteNoEncontradoException();
		}

		return retorno;
	}
	
	
	
	/**
	 * 22 mar 2019
	 * Método que permite buscar El record del estudiante en el SAU, incluyendo materia anuladas la matricula, para utilizar en movilidad estudiantil.
	 * @author lmquishpei
	 * @param identificacion - identificación del estudiante. 
	 * @param espeCodigo - codigo de carrera. 
	 * @param periodoId - hasta el periodo . 
	 * @return record de estudiante en SAU.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	public List<RecordEstudianteSAUDto> buscarRecordAcademicoSAUconAnulados(String identificacion, int espeCodigo,  int periodoId)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteSAUDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		
		
		sql.append("  select DISTINCT carrera, codigo_materia,estado, numero_matricula, periodo,semestre from (SELECT DISTINCT f.nombre facultad,ee.espe_codigo carrera,CASE mtr.modalidad  ");
		sql.append(" WHEN 1 THEN 'PRESENCIAL'  ");
		sql.append(" WHEN 2 THEN 'DISTANCIA'  ");
		sql.append(" END AS modalidad,mtr.pkv_folio AS Folio,mtr.pkv_folio_indice AS indice,mat.mat_curso AS semestre,P .grupo AS periodo,e.est_codigo as codigo_estudiantil,e.est_cedula cedula,e.est_apellido_paterno || ' ' || e.est_apellido_materno || ' ' ||e.est_nombres AS nombres,mat.mat_codigo_uce AS codigo_materia,mat.mat_nombre AS materia,rm.rmat_nota_uno AS Nota_1,rm.rmat_nota_dos AS Nota_2,rm.rmat_nota_tres AS Recuperacion,rm.nota_final as nota_final,rm.asistencia_final AS Asistencia,rm.rmat_aprobado estado,mat.mat_numero_creditos as Numero_creditos,rm.numero_matricula as numero_matricula,ee.nombre_director as director_carrera, fa.nombre_secretario_abogado as secretario_abogado,mtr.matr_codigo as codigo_matricula,(select avg(rm2.nota_final/2)  ");
		sql.append(" FROM registro_materias rm2  ");
		sql.append(" INNER JOIN materias mat2 ON rm2.mat_codigo = mat2.mat_codigo  ");
		sql.append(" INNER JOIN matriculas mtr2 ON rm2.per_codigo = mtr2.per_codigo AND rm2.est_codigo = mtr2.est_codigo  ");
		sql.append(" INNER JOIN periodos P2 ON mtr2.per_codigo = P2 .per_codigo INNER JOIN estudiantes e2 ON mtr2.est_codigo = e2.est_codigo INNER JOIN especialidades_escuela ee2 ON mtr2.espe_codigo = ee2.espe_codigo  ");
		sql.append(" INNER JOIN escuelas s2 ON ee2.esc_codigo = s2.esc_codigo AND s2.esc_codigo = s2.esc_codigo  ");
		sql.append(" INNER JOIN facultad f2 ON s2.id_facultad = f2. ID  ");
		sql.append(" INNER JOIN facultad_autoridades fa2 ON fa2.facultad_id =f2.id  ");
		sql.append(" WHERE f2. ID = f. ID and ee2.espe_codigo = ee.espe_codigo  ");
		sql.append(" AND s2.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165)  ");
		sql.append(" and ee.espe_codigo not in (63,64,65,66) ");
		sql.append(" AND mtr2.matr_estado > 1  ");
		sql.append(" AND rm2.rmat_aprobado IN (3,10)  ");
		sql.append(" and mat2.mat_curso > 0  ");
		sql.append(" and e2.est_cedula = e.est_cedula  ");
		sql.append(" )promedio_general,(SELECT DISTINCT sum(mat3.mat_numero_creditos) FROM registro_materias rm3 INNER JOIN materias mat3 ON rm3.mat_codigo = mat3.mat_codigo INNER JOIN matriculas mtr3 ON rm3.per_codigo = mtr3.per_codigo AND rm3.est_codigo = mtr3.est_codigo INNER JOIN periodos P3 ON mtr3.per_codigo = P3 .per_codigo INNER JOIN estudiantes e3 ON mtr3.est_codigo = e3.est_codigo INNER JOIN especialidades_escuela ee3 ON mtr3.espe_codigo = ee3.espe_codigo INNER JOIN escuelas s3 ON ee3.esc_codigo = s3.esc_codigo AND s3.esc_codigo = s3.esc_codigo INNER JOIN facultad f3 ON s3.id_facultad = f3. ID INNER JOIN facultad_autoridades fa3 ON fa3.facultad_id =f3.id WHERE s3.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr3.matr_estado > 1 and mat3.mat_curso > 0 AND rm3.rmat_aprobado IN (3,10) and f3. ID = f. ID and ee3.espe_codigo = ee.espe_codigo AND e3.est_cedula = e.est_cedula )creditos_totales,(SELECT DISTINCT sum(mat4.mat_numero_creditos) as Numero_creditos FROM registro_materias rm4 INNER JOIN materias mat4 ON rm4.mat_codigo = mat4.mat_codigo INNER JOIN matriculas mtr4 ON rm4.per_codigo = mtr4.per_codigo AND rm4.est_codigo = mtr4.est_codigo INNER JOIN periodos P4 ON mtr4.per_codigo = P4 .per_codigo INNER JOIN estudiantes e4 ON mtr4.est_codigo = e4.est_codigo INNER JOIN especialidades_escuela ee4 ON mtr4.espe_codigo = ee4.espe_codigo INNER JOIN escuelas s4 ON ee4.esc_codigo = s4.esc_codigo AND s4.esc_codigo = s4.esc_codigo INNER JOIN facultad f4 ON s4.id_facultad = f4. ID INNER JOIN facultad_autoridades fa4 ON fa4.facultad_id =f4.id WHERE f4. ID = f. ID and ee4.espe_codigo = ee.espe_codigo AND s4.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr4.matr_estado > 1 AND p4.per_descripcion in ('MIGRACION') AND rm4.rmat_aprobado IN (3) and e4.est_cedula = e.est_cedula )Migracion,(SELECT sum(mcc5.numero_creditos) FROM malla_curricular_creditos mcc5  LEFT JOIN malla_curricular mc5 ON mc5.id= mcc5.malla_curricular_id  LEFT JOIN escuelas esc5 ON esc5.esc_codigo = mc5.esc_codigo LEFT JOIN especialidades_escuela espe5 ON espe5.espe_codigo = mc5.espe_codigo WHERE mc5.fecha_termina IS NULL and espe5.espe_codigo = ee.espe_codigo) Creditos_carrera,'' as elaborado_por FROM registro_materias rm INNER JOIN materias mat ON rm.mat_codigo = mat.mat_codigo INNER JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo INNER JOIN periodos P ON mtr.per_codigo = P .per_codigo INNER JOIN estudiantes e ON mtr.est_codigo = e.est_codigo INNER JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo INNER JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND s.esc_codigo = s.esc_codigo INNER JOIN facultad f ON s.id_facultad = f. ID INNER JOIN facultad_autoridades fa ON fa.facultad_id =f.id inner JOIN malla_curricular mc ON ee.espe_codigo = mc.espe_codigo and mat.malla_curricular_id =mc.id  ");
		sql.append(" INNER JOIN malla_curricular_creditos mcc on mc.id= mcc.malla_curricular_id WHERE ");
		sql.append("  ee.espe_codigo = "+espeCodigo +" AND s.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr.matr_estado > 1 AND rm.rmat_aprobado IN (3,4,10) and mat.mat_estado = 1 and mat.mat_curso > 0  ");
		sql.append(" and e.est_cedula = '"+identificacion.toUpperCase() +"' and p.grupo<= "+periodoId+" GROUP BY P.grupo,f.nombre,ee.espe_nombre,f.id,ee.espe_codigo,mtr.modalidad ,mat.mat_curso,nombres,mat.mat_codigo_uce,mat.mat_nombre,rm.rmat_aprobado,mtr.pkv_folio,mtr.pkv_folio_indice,rm.rmat_nota_uno,rm.rmat_nota_dos,rm.rmat_nota_tres,rm.nota_final,rm.asistencia_final,mat.mat_numero_creditos,rm.numero_matricula,e.est_codigo,e.est_cedula,ee.nombre_director,fa.nombre_secretario_abogado,mtr.matr_codigo ORDER BY nombres, P .grupo,mat.mat_codigo_uce,e.est_cedula, mat.mat_nombre)certificado group by facultad, carrera,modalidad,Folio,indice, semestre, periodo,codigo_estudiantil, cedula, nombres,codigo_materia, materia, Nota_1,Nota_2,Recuperacion, nota_final, Asistencia,estado, Numero_creditos, numero_matricula,director_carrera,secretario_abogado,codigo_matricula,Creditos_carrera,creditos_totales,promedio_general ,migracion,elaborado_por  ");
		sql.append(" union all  ");
		sql.append(" select DISTINCT carrera, codigo_materia,estado, numero_matricula, periodo,semestre from (SELECT DISTINCT f.nombre facultad,ee.espe_codigo carrera,CASE mtr.modalidad  ");
		sql.append(" WHEN 1 THEN 'PRESENCIAL'  ");
		sql.append(" WHEN 2 THEN 'DISTANCIA'  ");
		sql.append(" END AS modalidad,mtr.pkv_folio AS Folio,mtr.pkv_folio_indice AS indice,mat.mat_curso AS semestre,P .grupo AS periodo,e.est_codigo as codigo_estudiantil,e.est_cedula cedula,e.est_apellido_paterno || ' ' || e.est_apellido_materno || ' ' ||e.est_nombres AS nombres,mat.mat_codigo_uce AS codigo_materia,mat.mat_nombre AS materia,rm.rmat_nota_uno AS Nota_1,rm.rmat_nota_dos AS Nota_2,rm.rmat_nota_tres AS Recuperacion,rm.nota_final as nota_final,rm.asistencia_final AS Asistencia,rm.rmat_aprobado estado,mat.mat_numero_creditos as Numero_creditos,rm.numero_matricula as numero_matricula,ee.nombre_director as director_carrera, fa.nombre_secretario_abogado as secretario_abogado,mtr.matr_codigo as codigo_matricula,(select avg(rm2.nota_final/2)  ");
		sql.append(" FROM registro_materias rm2  ");
		sql.append(" INNER JOIN materias mat2 ON rm2.mat_codigo = mat2.mat_codigo  ");
		sql.append(" INNER JOIN matriculas mtr2 ON rm2.per_codigo = mtr2.per_codigo AND rm2.est_codigo = mtr2.est_codigo  ");
		sql.append(" INNER JOIN periodos P2 ON mtr2.per_codigo = P2 .per_codigo INNER JOIN estudiantes e2 ON mtr2.est_codigo = e2.est_codigo INNER JOIN especialidades_escuela ee2 ON mtr2.espe_codigo = ee2.espe_codigo  ");
		sql.append(" INNER JOIN escuelas s2 ON ee2.esc_codigo = s2.esc_codigo AND s2.esc_codigo = s2.esc_codigo  ");
		sql.append(" INNER JOIN facultad f2 ON s2.id_facultad = f2. ID  ");
		sql.append(" INNER JOIN facultad_autoridades fa2 ON fa2.facultad_id =f2.id  ");
		sql.append(" WHERE f2. ID = f. ID and ee2.espe_codigo = ee.espe_codigo  ");
		sql.append(" AND s2.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165)  ");
		sql.append(" and ee.espe_codigo not in (63,64,65,66) ");
		sql.append(" AND mtr2.matr_estado > 1  ");
		sql.append(" AND rm2.rmat_aprobado IN (3,10)  ");
		sql.append(" and mat2.mat_curso > 0  ");
		sql.append(" and e2.est_cedula = e.est_cedula  ");
		sql.append(" )promedio_general,(SELECT DISTINCT sum(mat3.mat_numero_creditos) FROM registro_materias rm3 INNER JOIN materias mat3 ON rm3.mat_codigo = mat3.mat_codigo INNER JOIN matriculas mtr3 ON rm3.per_codigo = mtr3.per_codigo AND rm3.est_codigo = mtr3.est_codigo INNER JOIN periodos P3 ON mtr3.per_codigo = P3 .per_codigo INNER JOIN estudiantes e3 ON mtr3.est_codigo = e3.est_codigo INNER JOIN especialidades_escuela ee3 ON mtr3.espe_codigo = ee3.espe_codigo INNER JOIN escuelas s3 ON ee3.esc_codigo = s3.esc_codigo AND s3.esc_codigo = s3.esc_codigo INNER JOIN facultad f3 ON s3.id_facultad = f3. ID INNER JOIN facultad_autoridades fa3 ON fa3.facultad_id =f3.id WHERE s3.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr3.matr_estado > 1 and mat3.mat_curso > 0 AND rm3.rmat_aprobado IN (3,10) and f3. ID = f. ID and ee3.espe_codigo = ee.espe_codigo AND e3.est_cedula = e.est_cedula )creditos_totales,(SELECT DISTINCT sum(mat4.mat_numero_creditos) as Numero_creditos FROM registro_materias rm4 INNER JOIN materias mat4 ON rm4.mat_codigo = mat4.mat_codigo INNER JOIN matriculas mtr4 ON rm4.per_codigo = mtr4.per_codigo AND rm4.est_codigo = mtr4.est_codigo INNER JOIN periodos P4 ON mtr4.per_codigo = P4 .per_codigo INNER JOIN estudiantes e4 ON mtr4.est_codigo = e4.est_codigo INNER JOIN especialidades_escuela ee4 ON mtr4.espe_codigo = ee4.espe_codigo INNER JOIN escuelas s4 ON ee4.esc_codigo = s4.esc_codigo AND s4.esc_codigo = s4.esc_codigo INNER JOIN facultad f4 ON s4.id_facultad = f4. ID INNER JOIN facultad_autoridades fa4 ON fa4.facultad_id =f4.id WHERE f4. ID = f. ID and ee4.espe_codigo = ee.espe_codigo AND s4.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) AND mtr4.matr_estado > 1 AND p4.per_descripcion in ('MIGRACION') AND rm4.rmat_aprobado IN (3) and e4.est_cedula = e.est_cedula )Migracion,(SELECT sum(mcc5.numero_creditos) FROM malla_curricular_creditos mcc5  LEFT JOIN malla_curricular mc5 ON mc5.id= mcc5.malla_curricular_id  LEFT JOIN escuelas esc5 ON esc5.esc_codigo = mc5.esc_codigo LEFT JOIN especialidades_escuela espe5 ON espe5.espe_codigo = mc5.espe_codigo WHERE mc5.fecha_termina IS NULL and espe5.espe_codigo = ee.espe_codigo) Creditos_carrera,'' as elaborado_por FROM registro_materias rm INNER JOIN materias mat ON rm.mat_codigo = mat.mat_codigo INNER JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo INNER JOIN periodos P ON mtr.per_codigo = P .per_codigo INNER JOIN estudiantes e ON mtr.est_codigo = e.est_codigo INNER JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo INNER JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND s.esc_codigo = s.esc_codigo INNER JOIN facultad f ON s.id_facultad = f. ID INNER JOIN facultad_autoridades fa ON fa.facultad_id =f.id inner JOIN malla_curricular mc ON ee.espe_codigo = mc.espe_codigo and mat.malla_curricular_id =mc.id  ");
		sql.append(" INNER JOIN malla_curricular_creditos mcc on mc.id= mcc.malla_curricular_id WHERE ");
		sql.append("  ee.espe_codigo = "+espeCodigo+" AND s.esc_codigo NOT IN (- 1, 5, 6, 80, 60, 79, 980, 165) and mat.mat_estado = 1 AND rm.rmat_aprobado IN (3,4,10,-1) and mat.mat_curso > 0  ");
		sql.append(" and e.est_cedula = '"+identificacion.toUpperCase()+"' GROUP BY P.grupo,f.nombre,ee.espe_nombre,f.id,ee.espe_codigo,mtr.modalidad ,mat.mat_curso,nombres,mat.mat_codigo_uce,mat.mat_nombre,rm.rmat_aprobado,mtr.pkv_folio,mtr.pkv_folio_indice,rm.rmat_nota_uno,rm.rmat_nota_dos,rm.rmat_nota_tres,rm.nota_final,rm.asistencia_final,mat.mat_numero_creditos,rm.numero_matricula,e.est_codigo,e.est_cedula,ee.nombre_director,fa.nombre_secretario_abogado,mtr.matr_codigo ORDER BY nombres, P .grupo,mat.mat_codigo_uce,e.est_cedula, mat.mat_nombre)certificado group by facultad, carrera,modalidad,Folio,indice, semestre, periodo,codigo_estudiantil, cedula, nombres,codigo_materia, materia, Nota_1,Nota_2,Recuperacion, nota_final, Asistencia,estado, Numero_creditos, numero_matricula,director_carrera,secretario_abogado,codigo_matricula,Creditos_carrera,creditos_totales,promedio_general ,migracion,elaborado_por  ");
		sql.append(" order by codigo_materia  ");
		
		try{
			con = dsSau.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
//System.out.println(sql.toString());
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSauDto(rs));
			}
			
		} catch (Exception e) {
			retorno = null; 
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
				e.printStackTrace();
			}
		}

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
		}

		return retorno;
	}

	

	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	
	private RecordEstudianteSAUDto transformarResultSetArecordAcademicoSauDto(ResultSet rs) throws SQLException{
		RecordEstudianteSAUDto retorno = new RecordEstudianteSAUDto();
		retorno.setCarreraId(rs.getInt(1));
		retorno.setCodigoMateria(rs.getString(2));
		retorno.setEstado(rs.getInt(3));
		retorno.setNumeroMtricula(rs.getInt(4));
		retorno.setPeriodoAcademico(rs.getInt(5));
		retorno.setSemestre(rs.getInt(6));
		return retorno;
	}
	
	
	private RecordEstudianteSAUDto transformarResultSetArecordAcademicoSauDtoFull(ResultSet rs) throws SQLException{
		RecordEstudianteSAUDto retorno = new RecordEstudianteSAUDto();
		
		retorno.setPracDescripcion(rs.getString(1));
		retorno.setPracGrupo(rs.getInt(2));
		retorno.setEspeCodigo(rs.getInt(3));
		retorno.setEspeDescripcion(rs.getString(4));
		retorno.setPrsIdentificacion(rs.getString(5));
		retorno.setPrsNombresApellidos(rs.getString(6));
		retorno.setNumSemestre(rs.getInt(7));
		retorno.setNumeroMtricula(rs.getInt(8));
		retorno.setMtrEstado(rs.getInt(9));
		retorno.setMtrCodigo(rs.getString(10));
		retorno.setMtrDescripcion(rs.getString(11));
		retorno.setNota1(rs.getBigDecimal(12));
		retorno.setAsistencia1(rs.getInt(13));
		retorno.setNota2(rs.getBigDecimal(14));
		retorno.setAsistencia2(rs.getInt(15));
		retorno.setNotaRecuperacion(rs.getBigDecimal(16));
		retorno.setNotaFinal(rs.getBigDecimal(17));
		retorno.setAsistenciaFinal(rs.getInt(18));
		retorno.setMtrCreditos(rs.getInt(19));
		retorno.setMtrEstadoDescripcion(rs.getString(20));
		retorno.setPrlDescripcion("PARALELO");
		retorno.setAsistenciaTotal1(rs.getInt(21));
		retorno.setAsistenciaTotal2(rs.getInt(22));
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetArecordAcademicoSuficienciaCulturaFisica(ResultSet rs, int origen) throws SQLException {
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PeriodoAcademicoDto  periodoAcademico = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		MateriaDto materia = new MateriaDto();
		NivelDto nivel = new NivelDto();
		PersonaDto estudiante = new PersonaDto();
		CalificacionDto calificacion = new CalificacionDto();
		FichaMatriculaDto matricula = new FichaMatriculaDto();
		
		periodoAcademico.setPracId(rs.getInt(1));
		periodoAcademico.setPracDescripcion(rs.getString(2));
		dependencia.setDpnDescripcion(rs.getString(3));
		carrera.setCrrDescripcion(rs.getString(4));
		estudiante.setPrsIdentificacion(rs.getString(5));
		estudiante.setPrsNombres(rs.getString(6));
		estudiante.setPrsSexo(rs.getInt(7));
		nivel.setNvlNumeral(rs.getInt(8));
		materia.setMtrCodigo(rs.getString(9));
		materia.setMtrDescripcion(rs.getString(10));
		materia.setNumMatricula(rs.getInt(11));
		calificacion.setClfNota1(rs.getBigDecimal(12));
		calificacion.setClfNota2(rs.getBigDecimal(13));
		calificacion.setClfSupletorio(rs.getBigDecimal(14));
		calificacion.setClfNotaFinalSemestre(rs.getBigDecimal(15));
		calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(16));
		calificacion.setClfAsistencia1(rs.getBigDecimal(17));
		calificacion.setClfAsistencia2(rs.getBigDecimal(18));
		calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(19));
		calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(20));
		materia.setMtrEstadoLabel(rs.getString(21));
		matricula.setPracId(rs.getInt(22));
		
		if (origen == RecordEstudianteConstantes.RCES_ORIGEN_SAU) {
			carrera.setCrrEspeCodigo(rs.getInt(23));
			retorno.setRcesOrigen(RecordEstudianteConstantes.RCES_ORIGEN_SAU);
		}else {
			carrera.setCrrId(rs.getInt(23));
			retorno.setRcesOrigen(RecordEstudianteConstantes.RCES_ORIGEN_SIIU);
		}
		

		retorno.setRcesPracId(periodoAcademico.getPracId());
		retorno.setRcesPeriodoAcademicoDto(periodoAcademico);
		retorno.setRcesDependenciaDto(dependencia);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesMateriaDto(materia);
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesNivelDto(nivel);
		retorno.setRcesCalificacionDto(calificacion);
		retorno.setRcesFichaMatriculaDto(matricula);
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetArecordAcademicoDto(ResultSet rs) throws SQLException{
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
	

	private ComprobantePago transformarResultSetComprobantePago(ResultSet rs) throws SQLException{
		ComprobantePago retorno = new ComprobantePago();
		retorno.setCmpaFechaEmision(rs.getTimestamp(1));
		retorno.setCmpaFechaCaduca(rs.getTimestamp(2));
		retorno.setCmpaNumComprobante(rs.getString(3));
		retorno.setCmpaTotalPago(rs.getBigDecimal(4));
		return retorno;
	}

	
	private MatriculaDto rsAbuscarEstudiantesInscritos(ResultSet rs) throws SQLException {
		MatriculaDto retorno = new MatriculaDto();
		PersonaDto persona = new PersonaDto();
		retorno.setFcinId(rs.getInt(1));
		retorno.setPracId(rs.getInt(2));
		retorno.setPracDescripcion(rs.getString(3));
		retorno.setCrrId(rs.getInt(4));
		retorno.setCrrDescripcion(rs.getString(5));
		persona.setPrsId(rs.getInt(6));
		persona.setPrsIdentificacion(rs.getString(7));
		persona.setPrsPrimerApellido(rs.getString(8));
		persona.setPrsSegundoApellido(rs.getString(9));
		persona.setPrsNombres(rs.getString(10));
		retorno.setMtrPersonaDto(persona);
		return retorno;
	}
	
	
	
	private MatriculaDto rsAbuscarEstudiantesPregrado(ResultSet rs) throws SQLException {
		MatriculaDto retorno = new MatriculaDto();
		PersonaDto persona = new PersonaDto();
		retorno.setFcinId(rs.getInt(1));
		persona.setPrsId(rs.getInt(2));
		persona.setPrsIdentificacion(rs.getString(3));
		persona.setPrsPrimerApellido(rs.getString(4));
		persona.setPrsSegundoApellido(rs.getString(5));
		persona.setPrsNombres(rs.getString(6));
		retorno.setMtrPersonaDto(persona);
		return retorno;
	}
	
	private MatriculaDto transformarResulSetAMatriculaDto(ResultSet rs) throws SQLException {
		MatriculaDto retorno = new MatriculaDto();
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


	private RecordEstudianteDto transformarResultSetAhistorialRecordAcademicoDto(ResultSet rs, int origen) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PeriodoAcademicoDto  periodoAcademico = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		ModalidadDto modalidad = new ModalidadDto();
		MateriaDto materia = new MateriaDto();
		NivelDto nivel = new NivelDto();
		CalificacionDto calificacion = new CalificacionDto();
		PersonaDto estudiante = new PersonaDto();
		FichaMatriculaDto fichaMatricula = new FichaMatriculaDto();
		MallaCurricularDto mallaCurricular = new MallaCurricularDto();
		
		dependencia.setDpnDescripcion(rs.getString(1));
		carrera.setCrrDescripcion(rs.getString(2));
		carrera.setCrrId(rs.getInt(3));
		carrera.setCrrAcceso(false);//acceso jaja
		modalidad.setMdlDescripcion(rs.getString(4));
		retorno.setRcesFolio(rs.getInt(5));
		retorno.setRcesIndice(rs.getInt(6));
		fichaMatricula.setNvlDescripcion(rs.getString(7));
		fichaMatricula.setNvlId(rs.getInt(8));
		periodoAcademico.setPracId(rs.getInt(9));
		periodoAcademico.setPracDescripcion(rs.getString(10));
		estudiante.setPrsId(rs.getInt(11));
		estudiante.setPrsIdentificacion(rs.getString(12));
		estudiante.setPrsNombres(rs.getString(13));
		materia.setMtrId(rs.getInt(14));
		materia.setMtrCodigo(rs.getString(15));
		materia.setMtrDescripcion(rs.getString(16));
		calificacion.setClfNota1(rs.getBigDecimal(17));
		calificacion.setClfNota2(rs.getBigDecimal(18));
		calificacion.setClfSupletorio(rs.getBigDecimal(19));
		calificacion.setClfNotaFinalSemestre(rs.getBigDecimal(20));
		calificacion.setClfPromedioNotas(rs.getBigDecimal(21));
		calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(22));
		materia.setMtrEstadoLabel(rs.getString(23));
		materia.setMtrCreditos(rs.getInt(24));
		materia.setNumMatricula(rs.getInt(25));
		fichaMatricula.setFcmtId(rs.getInt(26));
		mallaCurricular.setMlcrTotalCreditos(rs.getInt(27));
		retorno.setRcesPromedioGeneral(rs.getBigDecimal(28));
		calificacion.setClfAsistencia1(rs.getBigDecimal(29));
		calificacion.setClfAsistencia2(rs.getBigDecimal(30));
		calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(31));
		calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(32));
		fichaMatricula.setFcmtTipoMatriculaLabel(rs.getString(33));
		calificacion.setClfSumaNotas(rs.getBigDecimal(34));
		calificacion.setClfParamRecuperacion1(rs.getBigDecimal(35));
		calificacion.setClfParamRecuperacion2(rs.getBigDecimal(36));
		nivel.setNvlDescripcion(rs.getString(37));
		materia.setMtrEstado(rs.getInt(38));
		nivel.setNvlNumeral(rs.getInt(39));
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
		retorno.setRcesPracDescripcion(periodoAcademico.getPracDescripcion());
		retorno.setMtrCodigo(materia.getMtrCodigo());
		retorno.setRcesOrigen(origen);
		retorno.setRcesPracId(periodoAcademico.getPracId());
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
		carrera.setCrrAcceso(false);

		estudiante.setPrsId(rs.getInt(9));
		estudiante.setPrsIdentificacion(rs.getString(10));
		estudiante.setPrsPrimerApellido(rs.getString(11));
		estudiante.setPrsSegundoApellido(rs.getString(12));
		estudiante.setPrsNombres(rs.getString(13));
		
		fichaMatricula.setFcmtId(rs.getInt(14));
		fichaMatricula.setFcmtFechaMatricula(rs.getTimestamp(15));
		fichaMatricula.setFcmtNivelUbicacion(rs.getInt(16));
		fichaMatricula.setFcmtTipo(rs.getInt(17)); //-> corresponde a ordinaria / extra / especial
		modalidad.setMdlDescripcion(rs.getString(18));
		fichaMatricula.setPracId(rs.getInt(19));
		
		materia.setMtrId(rs.getInt(20));
		materia.setMtrCodigo(rs.getString(21));
		materia.setMtrDescripcion(rs.getString(22));
		materia.setMtrCreditos(rs.getInt(23));
		materia.setMtrHoras(rs.getInt(24));
		materia.setNumMatricula(rs.getInt(25));
		
		nivel.setNvlId(rs.getInt(26));
		nivel.setNvlNumeral(rs.getInt(27));
		nivel.setNvlDescripcion(rs.getString(28));
		
		retorno.setRcesOrigen(origen);
		retorno.setRcesId(rs.getInt(29));
		retorno.setRcesEstado(rs.getInt(30));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(31));
		
		paralelo.setPrlId(rs.getInt(32));
		paralelo.setPrlCodigo(rs.getString(33));
		paralelo.setPracDescripcion(rs.getString(34));
		
		calificacion.setClfId(rs.getInt(35));
		calificacion.setClfNota1(rs.getBigDecimal(36));
		calificacion.setClfNota2(rs.getBigDecimal(37));
		calificacion.setClfSupletorio(rs.getBigDecimal(38));
		calificacion.setClfSumaNotas(rs.getBigDecimal(39));
		calificacion.setClfParamRecuperacion1(rs.getBigDecimal(40));
		calificacion.setClfParamRecuperacion2(rs.getBigDecimal(41));
		calificacion.setClfPromedioNotas(rs.getBigDecimal(42));
		calificacion.setClfAsistencia1(rs.getBigDecimal(43));
		calificacion.setClfAsistencia2(rs.getBigDecimal(44));
		calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(45));
		calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(46));
		calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(47)); 
		
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
	

	
	
	
	
	
	
	
	
	private String cargarEstadoRecordEstudiantil(int rcesEstado, int origen) {
		String retorno = "";
				
		if (RecordEstudianteConstantes.RCES_ORIGEN_SAU == origen) {
			switch (rcesEstado) {
			case SAUConstantes.RCES_MATERIA_ANULADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_ANULADO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_DESCONOCIDO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL;			
				break;
			case SAUConstantes.RCES_MATERIA_INSCRITO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_INSCRITO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_MATRICULDO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_MATRICULDO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_APROBADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_APROBADO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_REPROBADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_REPROBADO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_NINGUNA_NOTA_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_NINGUNA_NOTA_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_SUSPENSO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_SUSPENSO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_PERDIDO_POR_ASISTENCIA_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_PERDIDO_POR_ASISTENCIA_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_CONVALIDADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL;
				break;
			}
		}else {
			
			if(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_APROBADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_APROBADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE.equals(rcesEstado)){
				retorno = RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL;
			}else{
				retorno = SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL;			
			}
			
		}
		
		return retorno;
	}
	
	
	private void asignarEstadoAsignaturaEnRecord(RecordEstudianteDto item) {
		item.setRcesEstadoLabel(cargarEstadoRecordEstudiantil(item.getRcesEstado(), item.getRcesOrigen()));
		item.getRcesMateriaDto().setMtrEstadoLabel(item.getRcesEstadoLabel());
	}


	private void asignarNivelMatricula(RecordEstudianteDto item) {
		String retorno = "";
		
		if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_APROBACION_VALUE)){
			retorno = NivelConstantes.NIVEL_APROBACION_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_NIVELACION_VALUE)) {
			retorno = NivelConstantes.NIVEL_NIVELACION_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_PRIMERO_VALUE)) {
			retorno = NivelConstantes.NIVEL_PRIMERO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_SEGUNDO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEGUNDO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_TERCER_VALUE)) {
			retorno = NivelConstantes.NIVEL_TERCER_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_CUARTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_CUARTO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_QUINTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_QUINTO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_SEXTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEXTO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_SEPTIMO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEPTIMO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_OCTAVO_VALUE)) {
			retorno = NivelConstantes.NIVEL_OCTAVO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_NOVENO_VALUE)) {
			retorno = NivelConstantes.NIVEL_NOVENO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_DECIMO_VALUE)) {
			retorno = NivelConstantes.NIVEL_DECIMO_LABEL;
		}
		
		item.getRcesFichaMatriculaDto().setNvlDescripcion(retorno);
	}
	
	private void asignarTipoMatriculacion(RecordEstudianteDto item) {
		if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SAU)) {
			if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(1)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(2)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(3)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_LABEL);	
			}else {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel("DESCONOCIDO");
			}
		}else {
			if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_VALUE)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_VALUE)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_LABEL);	
			}else {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel("DESCONOCIDO");
			}
		}
	}
}

