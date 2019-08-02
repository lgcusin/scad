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
   
 ARCHIVO:     MallaCurricularNivelServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularNivel.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-ENE-2019			Freddy Guzmán 						Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelPosgradoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularNivelServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;

/**
 * EJB MallaCurricularNivelServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularNivel.
 * @author fgguzman
 * @version 1.0
 */
@Stateless
public class MallaCurricularNivelServicioJdbcImpl implements MallaCurricularNivelServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	public List<MallaCurricularNivelDto> buscar(int crrId) throws MallaCurricularNivelException, MallaCurricularNivelNoEncontradoException {
		List<MallaCurricularNivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT malla_id, ");
		sql.append("   nombre_malla, ");
		sql.append("   estado_malla, ");
		sql.append("   total_creditos, ");
		sql.append("   total_materias, ");
		sql.append("   id_carrera, ");
		sql.append("   carrera, ");
		sql.append("   semestre, ");
		sql.append("   nivel_descripcion, ");
		sql.append("   nvl_numeral, ");
		sql.append("   creditos, ");
		sql.append("   SUM(creditos) over(order by semestre) total ");
		sql.append(" FROM ");
		sql.append("   ( SELECT DISTINCT malla_id, ");
		sql.append("     nombre_malla, ");
		sql.append("     estado_malla, ");
		sql.append("     total_creditos, ");
		sql.append("     total_materias, ");
		sql.append("     id_carrera, ");
		sql.append("     carrera, ");
		sql.append("     semestre, ");
		sql.append("     nivel_descripcion, ");
		sql.append("     nvl_numeral, ");
		sql.append("     SUM(numero_creditos) creditos ");
		sql.append("   FROM ");
		sql.append("     ( SELECT DISTINCT mlcr.mlcr_id malla_id, ");
		sql.append("       mlcr.mlcr_descripcion nombre_malla, ");
		sql.append("       mlcr.mlcr_estado estado_malla, ");
		sql.append("       mlcr.mlcr_total_horas total_creditos, ");
		sql.append("       mlcr.mlcr_total_materias total_materias, ");
		sql.append("       crr.crr_id id_carrera, ");
		sql.append("       crr.crr_descripcion carrera, ");
		sql.append("       mtr.mtr_codigo materia_codigo, ");
		sql.append("       mtr.mtr_descripcion, ");
		sql.append("       mlcrmt.nvl_id semestre, ");
		sql.append("       nvl.nvl_descripcion nivel_descripcion, ");
		sql.append("       nvl.nvl_numeral nvl_numeral, ");
		sql.append("       CASE ");
		sql.append("         WHEN mtr.mtr_creditos IS NULL ");
		sql.append("         THEN mtr.mtr_horas ");
		sql.append("         ELSE mtr.mtr_creditos ");
		sql.append("       END numero_creditos, ");
		sql.append("       mtr.mtr_estado ");
		sql.append("     FROM materia mtr, ");
		sql.append("       carrera crr, ");
		sql.append("       malla_curricular_materia mlcrmt, ");
		sql.append("       malla_curricular mlcr, ");
		sql.append("       nivel NVL, ");
		sql.append("       malla_periodo mlpr, ");
		sql.append("       periodo_academico prac ");
		sql.append("     WHERE mlcrmt.mtr_id  = mtr.mtr_id ");
		sql.append("     AND mlcr.mlcr_id     = mlcrmt.mlcr_id ");
		sql.append("     AND mlcr.crr_id      = crr.crr_id ");
		sql.append("     AND mlcrmt.nvl_id    = nvl.nvl_id ");
		sql.append("     AND mlpr.mlcr_id     = mlcr.mlcr_id ");
		sql.append("     AND prac.prac_id     = mlpr.prac_id ");
		sql.append("     AND crr.crr_proceso IN (0,2) ");
		sql.append("     AND mtr.timt_id     IN (");sql.append(TipoMateriaConstantes.TIPO_MODULAR_VALUE );sql.append("  , ");	sql.append(TipoMateriaConstantes.TIPO_ASIGNATURA_VALUE+"  ) ");
		sql.append("     AND mtr.mtr_estado   =  "+MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
		sql.append("     AND prac.prac_estado =  "+PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append("     AND crr.CRR_ID = ? ");
		sql.append("     ORDER BY mtr.mtr_codigo ");
		sql.append("     ) test ");
		sql.append("   GROUP BY malla_id, ");
		sql.append("     nombre_malla, ");
		sql.append("     estado_malla, ");
		sql.append("     total_creditos, ");
		sql.append("     total_materias, ");
		sql.append("     id_carrera, ");
		sql.append("     carrera, ");
		sql.append("     semestre, ");
		sql.append("     nivel_descripcion, ");
		sql.append("     nvl_numeral ");
		sql.append("   )test  ");


		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAbuscar(rs));
			}

		} catch (Exception e) {
			throw new MallaCurricularNivelException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MallaCurricularNivelNoEncontradoException("No se encontró Asignaturas vinculadas a la Carrera seleccionada.");
		}

		return retorno;
	}
	
	public List<MallaCurricularNivelDto> buscarCreditosPorMalla(List<String> idCarreras) throws MallaCurricularNivelException, MallaCurricularNivelNoEncontradoException {
		List<MallaCurricularNivelDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CRR_ID, ");
		sql.append("   CRR_DESCRIPCION, ");
		sql.append("   CRR_TIPO, ");
		sql.append("   CRR_PROCESO, ");
		sql.append("   MLCR_ID, ");
		sql.append("   MLCR_DESCRIPCION, ");
		sql.append("   MLCR_TOTAL_HORAS, ");
		sql.append("   SUM(MLCRNV_CREDITOS) AS MLCRNV_CREDITOS, ");
		sql.append("   NVL_ID ");
		sql.append(" FROM ");
		sql.append("   (SELECT CRR.CRR_ID CRR_ID, ");
		sql.append("     CRR.CRR_DESCRIPCION CRR_DESCRIPCION, ");
		sql.append("     CRR.CRR_TIPO CRR_TIPO, ");
		sql.append("     CRR.CRR_PROCESO CRR_PROCESO, ");
		sql.append("     MLCR.MLCR_ID MLCR_ID, ");
		sql.append("     MLCR.MLCR_DESCRIPCION MLCR_DESCRIPCION, ");
		sql.append("     MLCR.MLCR_TOTAL_HORAS MLCR_TOTAL_HORAS, ");
		sql.append("     MLCR.MLCR_TOTAL_MATERIAS MLCR_TOTAL_MATERIAS, ");
		sql.append("     MLCRNV.MLCRNV_ID MLCRNV_ID, ");
		sql.append("     MLCRNV.MLCRNV_CREDITOS_ACUMULADO MLCRNV_CREDITOS_ACUMULADO, ");
		sql.append("     MLCRNV.MLCRNV_CREDITOS MLCRNV_CREDITOS, ");
		sql.append("     MLCRNV.NVL_ID NVL_ID ");
		sql.append("   FROM MALLA_CURRICULAR MLCR, ");
		sql.append("     CARRERA CRR, ");
		sql.append("     MALLA_CURRICULAR_NIVEL MLCRNV ");
		sql.append("   WHERE CRR.CRR_ID     = MLCR.CRR_ID ");
		sql.append("   AND MLCR.MLCR_ID     = MLCRNV.MLCR_ID ");
		sql.append("   AND MLCR.MLCR_ESTADO = 0 ");
		sql.append("   AND CRR.CRR_ID      IN " + idCarreras.toString().replace("[", "(").replace("]", ")"));
		sql.append("   AND CRR.CRR_PROCESO IN ("+CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE+" , " +CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE+" ) ");
		sql.append("   AND CRR.CRR_TIPO     =  " + CarreraConstantes.TIPO_PREGRADO_VALUE);
		sql.append("   ORDER BY CRR.CRR_ID ");
		sql.append("   ) TEST ");
		sql.append(" GROUP BY CRR_ID, ");
		sql.append("   CRR_TIPO, ");
		sql.append("   CRR_PROCESO, ");
		sql.append("   MLCR_ID, ");
		sql.append("   CRR_DESCRIPCION, ");
		sql.append("   MLCR_DESCRIPCION, ");
		sql.append("   MLCR_TOTAL_HORAS, ");
		sql.append("   NVL_ID ");
		sql.append("   ORDER BY NVL_ID ");

		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAbuscarCreditosPorMalla(rs));
			}

		} catch (Exception e) {
			throw new MallaCurricularNivelException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MallaCurricularNivelNoEncontradoException("No se encontró resultados en Malla Curricular Nivel.");
		}

		return retorno;
	}
	
	/**
	 * @author Daniel
	 * Método que permite buscar la malla de un programa de posgrado
	 * acurricular.
	 * @param crrId - id de la carrera.
	 * @param estadoMalla - estado  de la malla.
	 * @return malla curricular por niveles.
	 * @throws MallaCurricularDtoException
	 * @throws MallaCurricularDtoNoEncontradoException
	 */
	@Override
	public List<MallaCurricularNivelPosgradoDto> buscarPosgrado(int crrId, int pracId) throws MallaCurricularNivelException, MallaCurricularNivelNoEncontradoException {
		List<MallaCurricularNivelPosgradoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  mtr.");
		sql.append(JdbcConstantes.MTR_DESCRIPCION);
		sql.append(" ,  mtr.");sql.append(JdbcConstantes.MTR_ID);
		sql.append(" ,  nvl.");sql.append(JdbcConstantes.NVL_ID);
		sql.append(" ,  mtr.");sql.append(JdbcConstantes.MTR_CODIGO);
		sql.append(" ,  prac.");sql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sql.append("   FROM ");
		sql.append(JdbcConstantes.TABLA_MATERIA);sql.append(" mtr ");
		sql.append(",");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sql.append(" mlcrmt ");
		sql.append(",");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sql.append(" mlcr ");
		sql.append(",");sql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sql.append(" mlpr ");
		sql.append(",");sql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sql.append(" prac ");
		sql.append(" WHERE mtr.");sql.append(JdbcConstantes.MTR_ID);sql.append(" = mlcrmt.");sql.append(JdbcConstantes.MLCRMT_MTR_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ID);sql.append(" = mlcrmt.");sql.append(JdbcConstantes.MLCRMT_MLCR_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ID);sql.append(" = mlpr.");sql.append(JdbcConstantes.MLPR_MLCR_ID);
		sql.append(" AND mlpr.");sql.append(JdbcConstantes.MLPR_PRAC_ID);sql.append(" = prac.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ID);sql.append(" = ? ");
		sql.append(" AND mtr.");sql.append(JdbcConstantes.MTR_CRR_ID);sql.append(" = ? ");
		sql.append(" ORDER BY mlcrmt.");sql.append(JdbcConstantes.MLCRMT_NVL_ID);
		


		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, crrId);
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAbuscarPosgrado(rs));
			}

		} catch (Exception e) {
			throw new MallaCurricularNivelException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new MallaCurricularNivelNoEncontradoException("No se encontró Asignaturas vinculadas al programa seleccionado.");
		}

		return retorno;
	}

	
	
	
	private MallaCurricularNivelDto transformarResultSetAbuscarCreditosPorMalla(ResultSet rs) throws SQLException{
		MallaCurricularNivelDto retorno = new MallaCurricularNivelDto();
		MallaCurricularDto mallaCurricular = new MallaCurricularDto();
		CarreraDto carrera = new CarreraDto();
		
		carrera.setCrrId(rs.getInt(1));
		carrera.setCrrDescripcion(rs.getString(2));
		carrera.setCrrTipo(rs.getInt(3));
		carrera.setCrrProceso(rs.getInt(4));
		
		mallaCurricular.setMlcrId(rs.getInt(5));
		mallaCurricular.setMlcrDescripcion(rs.getString(6));
		mallaCurricular.setMlcrTotalCreditos(rs.getInt(7));
		retorno.setMlcrnvHorasAcumulado(rs.getInt(8));
		retorno.setMlcrnvNvlId(rs.getInt(9));
		
		retorno.setMlcrnvMlcrId(mallaCurricular.getMlcrId());
		retorno.setMlcrnvMallaCurricularDto(mallaCurricular);
		retorno.setMlcrnvCarreraDto(carrera);
		return retorno;
	}
	
	
	private MallaCurricularNivelDto transformarResultSetAbuscar(ResultSet rs) throws SQLException{
		MallaCurricularNivelDto retorno = new MallaCurricularNivelDto();
		MallaCurricularDto mallaCurricular = new MallaCurricularDto();
		CarreraDto carrera = new CarreraDto();
		NivelDto nivel = new NivelDto();
		mallaCurricular.setMlcrId(rs.getInt(1));
		mallaCurricular.setMlcrDescripcion(rs.getString(2));
		mallaCurricular.setMlcrEstado(rs.getInt(3));
		mallaCurricular.setMlcrTotalCreditos(rs.getInt(4));
		mallaCurricular.setMlcrTotalMaterias(rs.getInt(5));
		carrera.setCrrId(rs.getInt(6));
		carrera.setCrrDescripcion(rs.getString(7));
		nivel.setNvlId(rs.getInt(8));
		nivel.setNvlDescripcion(rs.getString(9));
		nivel.setNvlNumeral(rs.getInt(10));
		retorno.setMlcrnvHoras(rs.getInt(11));
		retorno.setMlcrnvHorasAcumulado(rs.getInt(12));
		retorno.setMlcrnvNvlId(nivel.getNvlId());
		retorno.setMlcrnvMlcrId(mallaCurricular.getMlcrId());
		retorno.setMlcrnvMallaCurricularDto(mallaCurricular);
		retorno.setMlcrnvCarreraDto(carrera);
		retorno.setMlcrnvNivelDto(nivel);
		return retorno;
	}
	
	private MallaCurricularNivelPosgradoDto transformarResultSetAbuscarPosgrado(ResultSet rs) throws SQLException{
		MallaCurricularNivelPosgradoDto retorno = new MallaCurricularNivelPosgradoDto();
		MallaCurricularDto mallaCurricular = new MallaCurricularDto();
		PeriodoAcademicoDto periodoAcademico = new PeriodoAcademicoDto();
		NivelDto nivel = new NivelDto();
		MateriaDto materia = new MateriaDto();
		materia.setMtrDescripcion(rs.getString(1));
		materia.setMtrId(rs.getInt(2));
		materia.setMtrCodigo(rs.getString(4));
		nivel.setNvlId(rs.getInt(3));
		periodoAcademico.setPracDescripcion(rs.getString(5));
		retorno.setMlcrnvMallaCurricularDto(mallaCurricular);
		retorno.setMlcrnvMateriaDto(materia);
		retorno.setMlcrnvNivelDto(nivel);
		retorno.setMlcrnvPeriodoAcademicoDto(periodoAcademico);
		return retorno;
	}
	
}
