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
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaIntercambioServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
/**
 * EJB MatriculaServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de MatriculaServicioJdbc.
 * @author fgguzman
 * @version 1.0
 */

@Stateless
public class MatriculaIntercambioServicioJdbcImpl implements MatriculaIntercambioServicioJdbc{
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	public List<MateriaDto> buscarMateriasIscripcion(List<FichaInscripcionDto> fichasInscripcion) throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_PROCESO);
		sql.append(" , crin.");sql.append(JdbcConstantes.CRIN_ESTADO);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_ID);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_CODIGO);
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_DESCRIPCION);
		sql.append(" , CASE");
		sql.append("  WHEN MTR.");sql.append(JdbcConstantes.MTR_HORAS);sql.append(" IS NULL");sql.append(" THEN MTR.");sql.append(JdbcConstantes.MTR_CREDITOS);
		sql.append("  ELSE");sql.append(" MTR.");sql.append(JdbcConstantes.MTR_HORAS);
		sql.append("  END AS ");sql.append("MTR_HORAS_POR_SEMANA");
		sql.append(" , mtr.");sql.append(JdbcConstantes.MTR_HORAS_CIEN);
		sql.append(" , mtin.");sql.append(JdbcConstantes.MTIN_ESTADO);
		sql.append(" , mlcrmt.");sql.append(JdbcConstantes.MLCRMT_ID);
		sql.append(" , nvl.");sql.append(JdbcConstantes.NVL_ID);
		sql.append(" , nvl.");sql.append(JdbcConstantes.NVL_DESCRIPCION);
		sql.append(" , nvl.");sql.append(JdbcConstantes.NVL_NUMERAL);
		sql.append(" , fcin.");sql.append(JdbcConstantes.FCIN_ID);
		sql.append(" FROM ");sql.append(JdbcConstantes.TABLA_CARRERA_INTERCAMBIO);sql.append(" CRIN ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MATERIA_INTERCAMBIO);sql.append(" MTIN ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sql.append(" FCIN ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" CRR ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MATERIA);sql.append(" MTR ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sql.append(" MLCR ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sql.append(" MLPR ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sql.append(" MLCRMT ");
		sql.append(" , ");sql.append(JdbcConstantes.TABLA_NIVEL);sql.append(" NVL ");
		sql.append(" WHERE CRIN.FCIN_ID = FCIN.FCIN_ID ");
		sql.append(" AND CRIN.CRR_ID = CRR.CRR_ID ");
		sql.append(" AND CRIN.CRIN_ID = MTIN.CRIN_ID ");
		sql.append(" AND MTIN.MTR_ID = MTR.MTR_ID ");
		sql.append(" AND MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append(" AND NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append(" AND CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append(" AND MLCR.MLCR_ID = MLPR.MLCR_ID ");
		sql.append(" AND MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append(" AND ");sql.append(" FCIN.");sql.append(JdbcConstantes.FCIN_TIPO_INGRESO);sql.append(" = ");sql.append(FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE);
		sql.append(" AND ");sql.append(" MLPR.");sql.append(JdbcConstantes.MLPR_ESTADO); sql.append(" = ");sql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
		
		List<String> fichas = new ArrayList<>();
		for (FichaInscripcionDto item : fichasInscripcion) {
			fichas.add(String.valueOf(item.getFcinId()));
		}
		
		sql.append(" AND ");sql.append(" FCIN.");sql.append(JdbcConstantes.FCIN_ID);sql.append(" in ");sql.append(fichas.toString().replace("[","(").replace("]",")"));


		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAmateriaDto(rs));
			}

		} catch (Exception e) {
			throw new CarreraIntercambioException("Error de conexión, comuníquese con el administrador del sistema.");
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
			throw new CarreraIntercambioNoEncontradoException("No se encontró materias asigandas al estudiante seleccionado.");
		}

		return retorno;

	}

	private MateriaDto transformarResultSetAmateriaDto(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		CarreraDto carrera = new CarreraDto();
		FichaInscripcionDto fichaInscripcion = new FichaInscripcionDto();
		carrera.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		carrera.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		carrera.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));
		carrera.setCrrEstado(rs.getInt(JdbcConstantes.CRIN_ESTADO));
		retorno.setCrrId(carrera.getCrrId());
		retorno.setCrrDescripcion(carrera.getCrrDescripcion());
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrHorasPorSemana(rs.getInt("MTR_HORAS_POR_SEMANA"));
		retorno.setMtrHorasCien(rs.getInt(JdbcConstantes.MTR_HORAS_CIEN));
		retorno.setMtrEstado(rs.getInt(JdbcConstantes.MTIN_ESTADO));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setNvlNumeral(rs.getInt(JdbcConstantes.NVL_NUMERAL));
		fichaInscripcion.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		carrera.setFcinId(fichaInscripcion.getFcinId());
		retorno.setFcinId(fichaInscripcion.getFcinId());
		retorno.setMtrFichaInscripcionDto(fichaInscripcion);
		retorno.setMtrCarreraDto(carrera);		
		return retorno;
	}
}


