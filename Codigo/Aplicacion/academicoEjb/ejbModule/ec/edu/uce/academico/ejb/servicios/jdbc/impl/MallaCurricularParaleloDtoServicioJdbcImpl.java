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
   
 ARCHIVO:     MallaCurricularParaleloDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularParalelo
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-AGOSTO-2017		Daniel Albuja				       Emisión Inicial
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
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MallaCurricularParaleloDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricularParalelo.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class MallaCurricularParaleloDtoServicioJdbcImpl implements MallaCurricularParaleloDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularParaleloDto> listarMallasXParaleloXMateria(int paraleloId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException{
		List<MallaCurricularParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ?");
				
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, paraleloId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoReserva(rs));
			}
		} catch (SQLException e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",paraleloId)));
		} catch (Exception e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", paraleloId)));
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
			throw new MallaCurricularParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularParaleloDto> listarMallasXCarrera(int crrId, List<CarreraDto> listCarreras) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException{
		List<MallaCurricularParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			if(crrId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listCarreras.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			}
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(crrId == GeneralesConstantes.APP_ID_BASE){ // caso para setear la lista de carreras
				for (CarreraDto item : listCarreras) {
					contador++;
					pstmt.setInt(contador, item.getCrrId()); //cargo las carreras ids
				}
			}else{//caso para setear una sola carrrera
				contador++;
				pstmt.setInt(contador, crrId); //cargo el id de carrera
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception")));
		} catch (Exception e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception")));
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
			throw new MallaCurricularParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MallaCurricularParaleloDto buscarCupoMlcrprXMateriaXParalelo(Integer paraleloId, Integer materiaId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException{
		MallaCurricularParaleloDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		if(paraleloId!=null&&materiaId!=null){}
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" WHERE ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ?");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ?");
				
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, paraleloId);
			pstmt.setInt(2, materiaId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",paraleloId)));
		} catch (Exception e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", paraleloId)));
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
		
		if(retorno == null){
			throw new MallaCurricularParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@SuppressWarnings("unused")
	public List<MallaCurricularParaleloDto> buscarMatriculadosMlcrprXMateriaXParalelo(Integer paraleloId, Integer materiaId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException{
		List<MallaCurricularParaleloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		if(paraleloId!=null&&materiaId!=null){}
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);
			
			sbSql.append(" AND dtmt.");sbSql.append(JdbcConstantes.DTMT_ESTADO);sbSql.append(" = ");sbSql.append(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ?");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ?");
				
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, paraleloId);
			pstmt.setInt(2, materiaId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularParaleloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",paraleloId)));
		} catch (Exception e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", paraleloId)));
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
		
		if(retorno == null){
			throw new MallaCurricularParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MallaCurricularParaleloDto buscarCupoMlcrprXMateriaXParaleloAlterno(Integer paraleloId, Integer materiaId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException{
		MallaCurricularParaleloDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		if(paraleloId!=null&&materiaId!=null){}
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); sbSql.append(" IS NULL THEN "); sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS); sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			
			sbSql.append(" , CASE WHEN mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO); sbSql.append(" IS NULL THEN "); sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO); sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ?");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ?");
				
			sbSql.append(" ORDER BY ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, paraleloId);
			pstmt.setInt(2, materiaId);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADtoAlterno(rs);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",paraleloId)));
		} catch (Exception e) {
			throw new MallaCurricularParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", paraleloId)));
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
		
		if(retorno == null){
			throw new MallaCurricularParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		
		return retorno;
	}

	
	
	@Override
	public Integer buscarPorMtrIdPrsIdentificacion(Integer materiaId,String cedula, Integer nivelId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
//			select * from  malla_curricular_paralelo mlcrpr
//			, malla_curricular_materia mlcrmt, materia mtr , paralelo prl where
//			mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id
//			and mtr.mtr_id in(
//			select mtr_sub_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr
//			, malla_curricular_materia mlcrmt, materia mtr
//			where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id
//			and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id
//			and mtr.mtr_id=6883 and prs.prs_identificacion = '1704417730')
//			and mlcrmt.nvl_id=6 and prl.prl_id = mlcrpr.prl_id
//			and prl.prl_id in (
//			select prl.prl_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr
//			, malla_curricular_materia mlcrmt, materia mtr, paralelo prl
//			where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id
//			and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id
//			and mtr.mtr_id=6883 and prs.prs_identificacion = '1704417730' and mlcrpr.prl_id=prl.prl_id
//			)
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select mlcrpr.mlcrpr_id  from  malla_curricular_paralelo mlcrpr ");
			sbSql.append(", malla_curricular_materia mlcrmt, materia mtr , paralelo prl where ");
			sbSql.append("mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id ");
			sbSql.append("and mtr.mtr_id in( ");
			sbSql.append("select mtr_sub_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr ");
			sbSql.append(", malla_curricular_materia mlcrmt, materia mtr ");
			sbSql.append("where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id ");
			sbSql.append("and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id ");
			sbSql.append("and mtr.mtr_id= ? and prs.prs_identificacion like ?) ");
			sbSql.append("and mlcrmt.nvl_id= ? and prl.prl_id = mlcrpr.prl_id ");
			sbSql.append("and prl.prl_id in ( ");
			sbSql.append("select prl.prl_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr ");																									
			sbSql.append(", malla_curricular_materia mlcrmt, materia mtr, paralelo prl ");
			sbSql.append("where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id ");
			sbSql.append("and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id ");
			sbSql.append("and mtr.mtr_id= ? and prs.prs_identificacion like ? and mlcrpr.prl_id=prl.prl_id and mlcrmt.nvl_id= ? ");
			sbSql.append(") ");
			
//			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			
//			
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
//			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
//			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
//			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
//			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" in (");
//			sbSql.append(" SELECT ");
//			
//			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
//			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
//			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
//			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
//			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
//			
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
//			sbSql.append(" AND nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? )");
//			
//			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" in (");
//			sbSql.append(" SELECT ");
//			
//			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
//			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
//			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
//			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
//			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
//			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
//			sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
//			
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
//			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
//			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
//			sbSql.append(" AND nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? )");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setString(2, cedula);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, materiaId);
			pstmt.setString(5, cedula);
			pstmt.setInt(6, nivelId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = rs.getInt(JdbcConstantes.MLCRPR_ID);
			}else{
				retorno = null;
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.no.result.exception")));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.exception")));
		}finally {
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
	
	@Override
	public Integer buscarPorMtrIdPrsIdentificacionPorPrlId(Integer materiaId,String cedula, Integer nivelId,Integer paraleloId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select mlcrpr.mlcrpr_id  from  malla_curricular_paralelo mlcrpr ");
			sbSql.append(", malla_curricular_materia mlcrmt, materia mtr , paralelo prl where ");
			sbSql.append("mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id ");
			sbSql.append("and mtr.mtr_id in( ");
			sbSql.append("select mtr_sub_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr ");
			sbSql.append(", malla_curricular_materia mlcrmt, materia mtr ");
			sbSql.append("where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id ");
			sbSql.append("and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id ");
			sbSql.append("and mtr.mtr_id= ? and prs.prs_identificacion like ?) ");
			sbSql.append("and mlcrmt.nvl_id= ? and prl.prl_id = mlcrpr.prl_id ");
			sbSql.append("and prl.prl_id in ( ");
			sbSql.append("select prl.prl_id from persona prs, ficha_docente fcdc,detalle_puesto dtps, carga_horaria crhr, malla_curricular_paralelo mlcrpr ");																									
			sbSql.append(", malla_curricular_materia mlcrmt, materia mtr, paralelo prl ");
			sbSql.append("where prs.prs_id=fcdc.prs_id and fcdc.fcdc_id=dtps.fcdc_id and dtps.dtps_id=crhr.dtps_id ");
			sbSql.append("and crhr.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id ");
			sbSql.append("and mtr.mtr_id= ? and prs.prs_identificacion like ? and mlcrpr.prl_id=prl.prl_id and mlcrmt.nvl_id= ? ");
			sbSql.append(") and prl.prl_id = ?");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, materiaId);
			pstmt.setString(2, cedula);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, materiaId);
			pstmt.setString(5, cedula);
			pstmt.setInt(6, nivelId);
			pstmt.setInt(7, paraleloId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = rs.getInt(JdbcConstantes.MLCRPR_ID);
			}else{
				retorno = null;
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.no.result.exception")));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularParaleloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.paralelo.exception")));
		}finally {
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
	
	@Override
	public Integer buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(Integer mlcrprId)  {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" select distinct mlcrpr_id  from  horario_academico ");
			sbSql.append("where mlcrpr_id_comp = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mlcrprId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = rs.getInt(JdbcConstantes.MLCRPR_ID);
			}else{
				retorno = null;
			}
		} catch (NoResultException e) {
			retorno = GeneralesConstantes.APP_ID_BASE;
		}catch (Exception e) {
			retorno = GeneralesConstantes.APP_ID_BASE;
		}finally {
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
	
	@Override
	public void modificarCupoMallaCurricularParalelo(Integer mlcrprId, Integer tipoOperacion)  {
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			if(tipoOperacion==0){
				sbSql.append(" update malla_curricular_paralelo set mlcrpr_cupo=mlcrpr_cupo-1");
				sbSql.append("where mlcrpr_id = ? ");	
			}else{
				sbSql.append(" update malla_curricular_paralelo set mlcrpr_cupo=mlcrpr_cupo11");
				sbSql.append("where mlcrpr_id = ? ");
			}			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mlcrprId);
			pstmt.executeUpdate();
		} catch (NoResultException e) {
		}catch (Exception e) {
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				}	
		}
	}
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public MallaCurricularParaleloDto buscarMallaCurricularParaleloPorId(Integer mlcrprId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException{
		MallaCurricularParaleloDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		if(mlcrprId!=null){}
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" WHERE ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
				
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mlcrprId);
			rs = pstmt.executeQuery();
			retorno = new MallaCurricularParaleloDto();
			if(rs.next()){
				retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
				retorno.setMlcrprParaleloId(rs.getInt(JdbcConstantes.MLCRPR_PRL_ID));
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
		
		if(retorno == null){
			throw new MallaCurricularParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
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
	private MallaCurricularParaleloDto transformarResultSetADto(ResultSet rs) throws SQLException{
		MallaCurricularParaleloDto retorno = new MallaCurricularParaleloDto();
			
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		
			
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			
			retorno.setMlcrprMallaCurricularMateriaId(rs.getInt(JdbcConstantes.MLCRPR_MLCRMT_ID));
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			if(rs.getString(JdbcConstantes.MTR_CODIGO) != null){
				retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			}else{
				retorno.setMtrCodigo("");
			}
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
			
			retorno.setMlcrprParaleloId(rs.getInt(JdbcConstantes.MLCRPR_PRL_ID));
			retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		return retorno;
	} 
	
	private MallaCurricularParaleloDto transformarResultSetADtoAlterno(ResultSet rs) throws SQLException{
		MallaCurricularParaleloDto retorno = new MallaCurricularParaleloDto();
			
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		
			int value = 0;
			value = rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS);
			if(value == GeneralesConstantes.APP_ID_BASE){
				retorno.setMlcrprInscritos(0);
			}else{
				retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			}
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			
			retorno.setMlcrprMallaCurricularMateriaId(rs.getInt(JdbcConstantes.MLCRPR_MLCRMT_ID));
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMlcrprParaleloId(rs.getInt(JdbcConstantes.MLCRPR_PRL_ID));
			
		return retorno;
	} 
	
	private MallaCurricularParaleloDto transformarResultSetADtoReserva(ResultSet rs) throws SQLException{
		MallaCurricularParaleloDto retorno = new MallaCurricularParaleloDto();
			
			retorno.setMlcrprCupo(rs.getInt(JdbcConstantes.MLCRPR_CUPO));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		
			
			retorno.setMlcrprInscritos(rs.getInt(JdbcConstantes.MLCRPR_INSCRITOS));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			
			retorno.setMlcrprMallaCurricularMateriaId(rs.getInt(JdbcConstantes.MLCRPR_MLCRMT_ID));
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			if(rs.getString(JdbcConstantes.MTR_CODIGO) != null){
				retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			}else{
				retorno.setMtrCodigo("");
			}
			retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
			retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
			
			retorno.setMlcrprParaleloId(rs.getInt(JdbcConstantes.MLCRPR_PRL_ID));
			retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
			retorno.setMlcrprReservaRepetidos(rs.getInt(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS));
		return retorno;
	} 
	
	
	
	@Override
	public List<DocenteJdbcDto> listarRegistroNotasDocente(int carreraId,int pracId, String identificacion) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(pracId);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
//			sbSql.append(" = ");sbSql.append(crrId);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_CUPO);
			sbSql.append(" - (");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_INSCRITOS);
			sbSql.append(" + ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_RESERVA_REPETIDOS);
			sbSql.append(" )) <> 0 ");
			sbSql.append(" ORDER BY ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			pstmt.setInt(1, MateriaId);
//			rs = pstmt.executeQuery();
//			retorno = new ArrayList<ParaleloDto>();
//			while(rs.next()){
//				retorno.add(transformarresultSetAParaleloDto(rs));
//			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.exception")));
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
			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.por.materia.no.result.exception")));
		}	
		return retorno;
	}
	
	
}
