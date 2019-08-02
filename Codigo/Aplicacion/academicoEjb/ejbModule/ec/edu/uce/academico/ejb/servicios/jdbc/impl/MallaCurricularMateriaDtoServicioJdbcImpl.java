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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;

/**
 * EJB MallaCurricularNivelServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularNivel.
 * @author fgguzman
 * @version 1.0
 */
@Stateless
public class MallaCurricularMateriaDtoServicioJdbcImpl implements MallaCurricularMateriaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	
	public List<MateriaDto> buscarModulos(int modularId) throws MallaCurricularMateriaException, MallaCurricularMateriaValidacionException, MallaCurricularMateriaNoEncontradoException {
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   MLCRMT.MLCRMT_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   MTR.MTR_SUB_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_ESTADO ");
		sql.append(" FROM MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MALLA_CURRICULAR MLCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   MATERIA MTR ");
		sql.append(" WHERE MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append(" AND MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append(" AND CRR.CRR_ID   = MLCR.CRR_ID ");
		sql.append(" AND MTR.MTR_SUB_ID = ? ");
		sql.append(" ORDER BY CRR.CRR_ID, MTR.TIMT_ID, MTR.MTR_CODIGO ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, modularId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarModulos(rs));
			}

		} catch (Exception e) {
			throw new MallaCurricularMateriaValidacionException("Error tipo sql al buscar Módulos.");
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
			throw new MallaCurricularMateriaNoEncontradoException("No se encontró resultados con el parámetro ingresado.");
		}

		return retorno;
	}
	
	public MateriaDto buscarModularPorParalelo(int materiaId, int paraleloId) throws MallaCurricularMateriaException, MallaCurricularMateriaValidacionException, MallaCurricularMateriaNoEncontradoException {
		MateriaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   MLCRMT.MLCRMT_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   MTR.MTR_SUB_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID ");
		sql.append(" FROM MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MALLA_CURRICULAR MLCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE MTR.MTR_ID     = MLCRMT.MTR_ID ");
		sql.append(" AND MLCR.MLCR_ID     = MLCRMT.MLCR_ID ");
		sql.append(" AND CRR.CRR_ID       = MLCR.CRR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append(" AND MLCRPR.PRL_ID    = PRL.PRL_ID ");
		sql.append(" AND PRAC.PRAC_ID     = PRL.PRAC_ID ");
		sql.append(" AND MTR.MTR_ID       = ? ");
		sql.append(" AND PRl.PRl_ID       = ? ");
		sql.append(" ORDER BY CRR.CRR_ID, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   MTR.MTR_CODIGO  ");


		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setInt(2, paraleloId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				retorno = rsAbuscarModularPorParalelo(rs);
			}
		} catch (NonUniqueResultException e) {
			throw new MallaCurricularMateriaValidacionException("Se encontró mas de un resultado con los parámetros ingresados.");
		} catch (NoResultException e) {
			throw new MallaCurricularMateriaValidacionException("No se encontró resultados con el parámetro ingresado.");
		} catch (Exception e) {
			throw new MallaCurricularMateriaException("Error tipo sql al buscar Módulos.");
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
	
	public Integer contarEstudiantesMatriculados(int mlcrprId) throws MallaCurricularMateriaException, MallaCurricularMateriaValidacionException, MallaCurricularMateriaNoEncontradoException {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT COUNT(PRS1.PRS_IDENTIFICACION) MATRICULADOS ");
		sql.append(" FROM PERSONA PRS1, ");
		sql.append("   USUARIO USR1, ");
		sql.append("   FICHA_ESTUDIANTE FCES1, ");
		sql.append("   RECORD_ESTUDIANTE RCES1, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR1, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT1, ");
		sql.append("   MATERIA MTR1, ");
		sql.append("   FICHA_MATRICULA FCMT1, ");
		sql.append("   COMPROBANTE_PAGO CMPG1, ");
		sql.append("   DETALLE_MATRICULA DTMT1, ");
		sql.append("   FICHA_INSCRIPCION FCIN1, ");
		sql.append("   CONFIGURACION_CARRERA CNCR1, ");
		sql.append("   CARRERA CRR1, ");
		sql.append("   DEPENDENCIA DPN1, ");
		sql.append("   PERIODO_ACADEMICO PRAC1 ");
		sql.append(" WHERE FCES1.FCES_ID        = FCMT1.FCES_ID ");
		sql.append(" AND FCMT1.FCMT_ID          = CMPG1.FCMT_ID ");
		sql.append(" AND CMPG1.CMPA_ID          = DTMT1.CMPA_ID ");
		sql.append(" AND PRS1.PRS_ID            = FCES1.PRS_ID ");
		sql.append(" AND USR1.PRS_ID            = PRS1.PRS_ID ");
		sql.append(" AND RCES1.MLCRPR_ID        = MLCRPR1.MLCRPR_ID ");
		sql.append(" AND MLCRPR1.MLCRPR_ID      = DTMT1.MLCRPR_ID ");
		sql.append(" AND MLCRMT1.MLCRMT_ID      = MLCRPR1.MLCRMT_ID ");
		sql.append(" AND MLCRMT1.MTR_ID         = MTR1.MTR_ID ");
		sql.append(" AND MTR1.CRR_ID            = CRR1.CRR_ID ");
		sql.append(" AND FCES1.FCES_ID          = RCES1.FCES_ID ");
		sql.append(" AND FCIN1.FCIN_ID          = FCES1.FCIN_ID ");
		sql.append(" AND FCIN1.CNCR_ID          = CNCR1.CNCR_ID ");
		sql.append(" AND CRR1.CRR_ID            = CNCR1.CRR_ID ");
		sql.append(" AND CRR1.DPN_ID            = DPN1.DPN_ID ");
		sql.append(" AND PRAC1.PRAC_ID          = FCMT1.FCMT_PRAC_ID ");
		sql.append(" AND DPN1.DPN_ID            = DPN1.DPN_ID ");
		sql.append(" AND MTR1.MTR_CODIGO        = MTR1.MTR_CODIGO ");
		sql.append(" AND RCES1.RCES_ESTADO NOT IN ("+RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE+") ");
		sql.append(" AND MLCRPR1.MLCRPR_ID      = ? ");

		try{

			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mlcrprId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				retorno = rs.getInt(1);
			}
		} catch (NoResultException e) {
			throw new MallaCurricularMateriaValidacionException("No se encontró resultados con el parámetro ingresado.");
		} catch (Exception e) {
			throw new MallaCurricularMateriaException("Error tipo sql al buscar matriculados.");
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

	private MateriaDto rsAbuscarModulos(ResultSet rs) throws SQLException {
		MateriaDto retorno = new MateriaDto();
		retorno.setCrrId(rs.getInt(1));
		retorno.setCrrDescripcion(rs.getString(2));
		retorno.setMlcrmtId(rs.getInt(3));
		retorno.setMtrId(rs.getInt(4));
		retorno.setMtrTpmtId(rs.getInt(5));
		retorno.setMtrSubId(rs.getInt(6));
		retorno.setMtrCodigo(rs.getString(7));
		retorno.setMtrDescripcion(rs.getString(8));
		retorno.setMtrEstado(rs.getInt(9));
		return retorno;
	}
	
	private MateriaDto rsAbuscarModularPorParalelo(ResultSet rs) throws SQLException {
		MateriaDto retorno = new MateriaDto();
		retorno.setCrrId(rs.getInt(1));
		retorno.setCrrDescripcion(rs.getString(2));
		retorno.setMlcrmtId(rs.getInt(3));
		retorno.setMtrId(rs.getInt(4));
		retorno.setMtrTpmtId(rs.getInt(5));
		retorno.setMtrSubId(rs.getInt(6));
		retorno.setMtrCodigo(rs.getString(7));
		retorno.setMtrDescripcion(rs.getString(8));
		retorno.setMtrEstado(rs.getInt(9));
		retorno.setMlcrprId(rs.getInt(10));
		return retorno;
	}

	
}
