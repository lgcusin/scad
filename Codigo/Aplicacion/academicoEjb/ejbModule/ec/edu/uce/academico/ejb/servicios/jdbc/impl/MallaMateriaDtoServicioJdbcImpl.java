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
   
 ARCHIVO:     MallaMateriaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularMateria.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
07-AGOS-2017		Dennis Collaguazo				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MallaMateriaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularMateria.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class MallaMateriaDtoServicioJdbcImpl implements MallaMateriaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas
	 * @param idMalla - idMalla id de la malla curricular 
	 * @param estadoMalla - estadoMalla , estado de la malla curricular
	 * @param estadoMallaMateria - estadoMallaMateria estado de la malla curricular materia
	 * @param estadoMateria - estadoMateria estado de la materia
	 * @param idNivel - idNivel id del nivel
	 * @param estadoPeriodo - estado periodo
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularDto> listarMallasMateriasXIdMallaXEstadoPeriodo(int idMalla, int estadoMalla, int estadoMallaMateria, int estadoMateria, int idNivel, int estadoPeriodo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_DESCRIPCION);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ESTADO);
			
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , unfr.");sbSql.append(JdbcConstantes.UNFR_ID);
			sbSql.append(" , unfr.");sbSql.append(JdbcConstantes.UNFR_DESCRIPCION);
			
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_FORMACION_MALLA);sbSql.append(" tifrml, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			sbSql.append(JdbcConstantes.TABLA_UNIDAD_FORMACION);sbSql.append(" unfr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);sbSql.append(" = ");sbSql.append(" tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_JERARQUIA);sbSql.append(" = ");sbSql.append(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
			
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND unfr.");sbSql.append(JdbcConstantes.UNFR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_UNFR_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND timt.");sbSql.append(JdbcConstantes.TIMT_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);sbSql.append(" not in ( ");sbSql.append(TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);sbSql.append(" = ? ");
			//sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" AND nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);sbSql.append(" =  ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
//			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" , unfr.");sbSql.append(JdbcConstantes.UNFR_DESCRIPCION);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idMalla);
			pstmt.setInt(2, estadoMalla);
			//pstmt.setInt(3, estadoMallaMateria);
			pstmt.setInt(3, estadoMateria);
			pstmt.setInt(4, idNivel);
			pstmt.setInt(5, estadoPeriodo);
					
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.sql.exception")));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.exception")));
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
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas
	 * @param idMallaMateria - idMallaMateria id de la malla curricular materia
	 * @return retorna la entidad malla curricular materia por id de malla curricular materia a buscar
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MallaCurricularDto buscarXId(int idMallaMateria) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		MallaCurricularDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_DESCRIPCION);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ESTADO);
			
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			
			sbSql.append(" , unfr.");sbSql.append(JdbcConstantes.UNFR_ID);
			sbSql.append(" , unfr.");sbSql.append(JdbcConstantes.UNFR_DESCRIPCION);
			
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_ID);
			sbSql.append(" , timt.");sbSql.append(JdbcConstantes.TIMT_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_FORMACION_MALLA);sbSql.append(" tifrml, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr, ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
			sbSql.append(JdbcConstantes.TABLA_UNIDAD_FORMACION);sbSql.append(" unfr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_MATERIA);sbSql.append(" timt ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);sbSql.append(" = ");sbSql.append(" tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_JERARQUIA);sbSql.append(" = ");sbSql.append(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
			
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" AND unfr.");sbSql.append(JdbcConstantes.UNFR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_UNFR_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND timt.");sbSql.append(JdbcConstantes.TIMT_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);sbSql.append(" not in ( ");sbSql.append(TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , unfr.");sbSql.append(JdbcConstantes.UNFR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idMallaMateria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.buscar.por.id.malla.materia.sql.exception")));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.buscar.por.id.malla.materia.exception")));
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
		
		if(retorno == null ){
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.buscar.por.id.malla.materia.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas por nivel y carrera
	 * @return Lista todas las mallas curricularers materia por nivel y carrera
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MallaCurricularDto> listarMallasMateriasXNivelXCarrera(int idCarrera, int idNivel) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);sbSql.append(" = ");sbSql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			sbSql.append(" AND nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idNivel);
			pstmt.setInt(2, idCarrera);
					
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMalla(rs));
			}
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception")));
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
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas por nivel y carrera
	 * @return Lista todas las mallas curricularers materia por nivel y carrera
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<MallaCurricularDto> listarMallasMateriasXCarrera(int idCarrera) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");
			sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera);
					
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMallaXNIvel(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception")));
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
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
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
	private MallaCurricularDto transformarResultSetADto(ResultSet rs) throws SQLException{
		MallaCurricularDto retorno = new MallaCurricularDto();
			
			retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
			retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			
			retorno.setTpfrmlId(rs.getInt(JdbcConstantes.TIFRML_ID));
			retorno.setTpfrmlDescripcion(rs.getString(JdbcConstantes.TIFRML_DESCRIPCION));
			retorno.setTpfrmlEstado(rs.getInt(JdbcConstantes.TIFRML_ESTADO));
			
			retorno.setMlcrId(rs.getInt(JdbcConstantes.MLCR_ID));
			retorno.setMlcrCodigo(rs.getString(JdbcConstantes.MLCR_CODIGO));
			retorno.setMlcrDescripcion(rs.getString(JdbcConstantes.MLCR_DESCRIPCION));
			retorno.setMlcrEstado(rs.getInt(JdbcConstantes.MLCR_ESTADO));
			
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
			
			retorno.setUnfrId(rs.getInt(JdbcConstantes.UNFR_ID));
			retorno.setUnfrDescripcion(rs.getString(JdbcConstantes.UNFR_DESCRIPCION));
			
			retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
			retorno.setMlcrmtEstado(rs.getInt(JdbcConstantes.MLCRMT_ESTADO));
			
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
			
			retorno.setTimtId(rs.getInt(JdbcConstantes.TIMT_ID));
			retorno.setTimtDescripcion(rs.getString(JdbcConstantes.TIMT_DESCRIPCION));
			
		return retorno;
	}

	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset de búsqueda del método listarMallasMateriasXPeriodoXCarrera
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private MallaCurricularDto transformarResultSetADtoMalla(ResultSet rs) throws SQLException{
		MallaCurricularDto retorno = new MallaCurricularDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setMtrAsignada(false);
		return retorno;
	}
	
	private MallaCurricularDto transformarResultSetADtoMallaXNIvel(ResultSet rs) throws SQLException{
		MallaCurricularDto retorno = new MallaCurricularDto();
		
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setMtrAsignada(false);
		return retorno;
	}
}
