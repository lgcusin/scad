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
   
 ARCHIVO:     MallaCurricularDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricular.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-ABRIL-2017		Gabriel Mafla				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MallaCurricularDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mallaCurricular.
 * @author ghmafla
 * @version 1.0
 */
@Stateless
public class MallaCurricularDtoServicioJdbcImpl implements MallaCurricularDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularDto> listarMallasXCarrera(int carreraId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
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
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FECHA_CREACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_RESOLUCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_DESCRIPCION);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ESTADO);
			
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_INICIO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_FIN);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_HORAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_MATERIAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_FORMACION_MALLA);sbSql.append(" tifrml, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);sbSql.append(" = ");sbSql.append(" tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_JERARQUIA);sbSql.append(" = ");sbSql.append(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);
//			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);sbSql.append(" = ");sbSql.append(MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);sbSql.append(" = ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			if(carreraId == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" >= ?");
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			}
				
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId);
					
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",carreraId)));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", carreraId)));
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
	 * Realiza la busqueda de una malla por codigo en la aplicacion
	 * @return Malla por codigo de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public boolean validarMallaCurricularXCodigo (MallaCurricularDto entidad,int tipo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		boolean retorno = false;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append("  mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_INICIO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_FIN);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_HORAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_MATERIAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);
						
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" UPPER(mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);sbSql.append(") = ? ");
			
			if(tipo == GeneralesConstantes.APP_EDITAR){
				sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ?");
			}
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, entidad.getMlcrCodigo().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR){
				pstmt.setInt(2, entidad.getMlcrId());
			}
					
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = true;
			}else{
				retorno = false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.buscar.malla.curricular.codigo.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.buscar.malla.curricular.codigo.exception")));
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
		
		
//		if(retorno == null ){
//			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.buscar.malla.curricular.codigo.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de una malla por codigo en la aplicacion
	 * @return Malla por codigo de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public boolean validarMallaCurricularXNombre (MallaCurricularDto entidad,int tipo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		boolean retorno = false;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			
			sbSql.append("  mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_INICIO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_FIN);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_HORAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_MATERIAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);
						
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" UPPER(mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);sbSql.append(") = ? ");
			
			if(tipo == GeneralesConstantes.APP_EDITAR){
				sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ?");
			}
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, entidad.getMlcrDescripcion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR){
				pstmt.setInt(2, entidad.getMlcrId());
			}
					
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = true;
			}else{
				retorno = false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			//TODO
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.buscar.malla.curricular.codigo.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.buscar.malla.curricular.codigo.exception")));
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
		
		
//		if(retorno == null ){
//			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.buscar.malla.curricular.codigo.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de mallas curriculares buscado por id de carrera por vigencia y por estado
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param vigencia - vigencia malla curricurar vigencia
	 * @param estado - estado de la malla curricular
	 * @return Lista todas las mallas a buscar por los parametros
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularDto> listarMallasXCarreraXVigenciaXEstadoXestadoPeriodo(int idCarrera, int vigencia, int estado, int estadoPeriodo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
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
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FECHA_CREACION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_RESOLUCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_DESCRIPCION);
			sbSql.append(" , tifrml.");sbSql.append(JdbcConstantes.TIFRML_ESTADO);
			
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CODIGO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_INICIO);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_FECHA_FIN);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_HORAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TOTAL_MATERIAS);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" , mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_FORMACION_MALLA);sbSql.append(" tifrml, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_TIFRML_ID);sbSql.append(" = ");sbSql.append(" tifrml.");sbSql.append(JdbcConstantes.TIFRML_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_JERARQUIA);sbSql.append(" = ");sbSql.append(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLCR_ID);

			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_VIGENTE);sbSql.append(" = ?");
			sbSql.append(" AND mlcr.");sbSql.append(JdbcConstantes.MLCR_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ?");
			sbSql.append(" AND mlpr.");sbSql.append(JdbcConstantes.MLPR_ESTADO);sbSql.append(" = ");sbSql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			
			sbSql.append(" ORDER BY ");
			sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera);
			pstmt.setInt(2, vigencia);
			pstmt.setInt(3, estado);
			pstmt.setInt(4, estadoPeriodo);	
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MallaCurricularDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.vigencia.estado.sql.exception")));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.vigencia.estado.exception")));
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
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.vigencia.estado.no.result.exception")));
		}	
		return retorno;
	}
	
	public List<MallaCurricularDto> buscarMallaCurricular(int dependenciaId, int carreraId, Integer[] tiposCarrera,Integer[] estadosMalla) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");
		sql.append(" dpn.");sql.append(JdbcConstantes.DPN_ID);
		sql.append(" , dpn.");sql.append(JdbcConstantes.DPN_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_COD_SNIESE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DETALLE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_FECHA_CREACION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_RESOLUCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_PROCESO);
		sql.append(" , tifrml.");sql.append(JdbcConstantes.TIFRML_ID);
		sql.append(" , tifrml.");sql.append(JdbcConstantes.TIFRML_DESCRIPCION);
		sql.append(" , tifrml.");sql.append(JdbcConstantes.TIFRML_ESTADO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_ID);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_CODIGO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_DESCRIPCION);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_ESTADO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_FECHA_INICIO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_FECHA_FIN);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TOTAL_HORAS);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TOTAL_MATERIAS);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_VIGENTE);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
		
		sql.append(" FROM ");
		sql.append(JdbcConstantes.TABLA_DEPENDENCIA);sql.append(" dpn, ");
		sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr, ");
		sql.append(JdbcConstantes.TABLA_TIPO_FORMACION_MALLA);sql.append(" tifrml, ");
		sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sql.append(" mlcr, ");
		sql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sql.append(" prac, ");
		sql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sql.append(" mlpr ");
		
		sql.append(" WHERE ");sql.append(" dpn.");sql.append(JdbcConstantes.DPN_ID);sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_DPN_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_CRR_ID);sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_TIFRML_ID);sql.append(" = ");sql.append(" tifrml.");sql.append(JdbcConstantes.TIFRML_ID);
		sql.append(" AND dpn.");sql.append(JdbcConstantes.DPN_JERARQUIA);sql.append(" = ");sql.append(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
		sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ID);sql.append(" = ");sql.append(" mlpr.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ID);sql.append(" = ");sql.append(" mlpr.");sql.append(JdbcConstantes.MLCR_ID);
		sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ESTADO);sql.append(" = ");sql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND crr.");sql.append(JdbcConstantes.CRR_TIPO);sql.append(" in ");sql.append(Arrays.toString(tiposCarrera).replace("[", "(").replace("]", ")"));
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ESTADO);sql.append(" in ");sql.append(Arrays.toString(estadosMalla).replace("[", "(").replace("]", ")"));

		if(GeneralesConstantes.APP_ID_BASE.equals(carreraId)){
			sql.append(" AND dpn.");sql.append(JdbcConstantes.DPN_ID);sql.append(" = " + dependenciaId);
		}else{
			sql.append(" AND crr.");sql.append(JdbcConstantes.CRR_ID);sql.append(" = " + carreraId);
		}
			
		sql.append(" ORDER BY ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarMallaCurricular(rs));
			}
			
		} catch (SQLException e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",carreraId)));
		} catch (Exception e) {
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", carreraId)));
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
		
		if(retorno.isEmpty()){
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		
		return retorno;
	}	
	
	@Override
	public List<MallaCurricularDto> buscarMallaCurricularPosgrado(int dependenciaId, int carreraId, Integer tipoCarrera,Integer estadoMalla) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");
		sql.append(" dpn.");sql.append(JdbcConstantes.DPN_ID);
		sql.append(" , dpn.");sql.append(JdbcConstantes.DPN_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_COD_SNIESE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DETALLE);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_FECHA_CREACION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_RESOLUCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_PROCESO);
		sql.append(" , tifrml.");sql.append(JdbcConstantes.TIFRML_ID);
		sql.append(" , tifrml.");sql.append(JdbcConstantes.TIFRML_DESCRIPCION);
		sql.append(" , tifrml.");sql.append(JdbcConstantes.TIFRML_ESTADO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_ID);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_CODIGO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_DESCRIPCION);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_ESTADO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_FECHA_INICIO);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_FECHA_FIN);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TOTAL_HORAS);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TOTAL_MATERIAS);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_VIGENTE);
		sql.append(" , mlcr.");sql.append(JdbcConstantes.MLCR_TIPO_APROBACION);
		
		sql.append(" FROM ");
		sql.append(JdbcConstantes.TABLA_DEPENDENCIA);sql.append(" dpn, ");
		sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr, ");
		sql.append(JdbcConstantes.TABLA_TIPO_FORMACION_MALLA);sql.append(" tifrml, ");
		sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sql.append(" mlcr, ");
		sql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sql.append(" prac, ");
		sql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sql.append(" mlpr ");
		
		sql.append(" WHERE ");sql.append(" dpn.");sql.append(JdbcConstantes.DPN_ID);sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_DPN_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_CRR_ID);sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_TIFRML_ID);sql.append(" = ");sql.append(" tifrml.");sql.append(JdbcConstantes.TIFRML_ID);
		sql.append(" AND dpn.");sql.append(JdbcConstantes.DPN_JERARQUIA);sql.append(" = ");sql.append(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
		sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ID);sql.append(" = ");sql.append(" mlpr.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ID);sql.append(" = ");sql.append(" mlpr.");sql.append(JdbcConstantes.MLCR_ID);
		sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ESTADO);sql.append(" = ");sql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND crr.");sql.append(JdbcConstantes.CRR_TIPO);sql.append(" = ");sql.append(tipoCarrera);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ESTADO);sql.append(" = ");sql.append(estadoMalla);

		if(GeneralesConstantes.APP_ID_BASE.equals(carreraId)){
			sql.append(" AND dpn.");sql.append(JdbcConstantes.DPN_ID);sql.append(" = " + dependenciaId);
		}else{
			sql.append(" AND crr.");sql.append(JdbcConstantes.CRR_ID);sql.append(" = " + carreraId);
		}
			
		sql.append(" ORDER BY ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarMallaCurricular(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.sql.exception",carreraId)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallaCurricularDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.exception", carreraId)));
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
		
		if(retorno.isEmpty()){
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		
		return retorno;
	}	
	@Override
	public List<MallaCurricularDto> buscarCarreraDependenciaPorPeriodoAcademico(int pracId) {
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");
		sql.append(" dpn.");sql.append(JdbcConstantes.DPN_ID);
		sql.append(" , dpn.");sql.append(JdbcConstantes.DPN_DESCRIPCION);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" , crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		sql.append(" FROM ");
		sql.append(JdbcConstantes.TABLA_DEPENDENCIA);sql.append(" dpn, ");
		sql.append(JdbcConstantes.TABLA_CARRERA);sql.append(" crr, ");
		sql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sql.append(" mlcr, ");
		sql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sql.append(" prac, ");
		sql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sql.append(" mlpr ");
		
		sql.append(" WHERE ");sql.append(" dpn.");sql.append(JdbcConstantes.DPN_ID);sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_DPN_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_CRR_ID);sql.append(" = ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_ID);
		sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ID);sql.append(" = ");sql.append(" mlpr.");sql.append(JdbcConstantes.PRAC_ID);
		sql.append(" AND mlcr.");sql.append(JdbcConstantes.MLCR_ID);sql.append(" = ");sql.append(" mlpr.");sql.append(JdbcConstantes.MLCR_ID);

			sql.append(" AND prac.");sql.append(JdbcConstantes.PRAC_ID);sql.append(" = " + pracId);
			
		sql.append(" ORDER BY ");sql.append(" crr.");sql.append(JdbcConstantes.CRR_DESCRIPCION);
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarMallaCurricularPosgrado(rs));
			}
			
		} catch (SQLException e) {
		} catch (Exception e) {
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
	
	public List<MallaCurricularDto> buscarMallaCurricularPorNivel(int mlcrId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MallaCurricularDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarMallaCurricularPorNivel(rs));
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
		
		if(retorno.isEmpty()){
			throw new MallaCurricularDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularDto.listar.mallas.carrera.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<MateriaDto> buscarMaterias(int mlcrId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException{
		List<MateriaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT mtr.mtr_id, ");
		sql.append("   mtr.mtr_codigo, ");
		sql.append("   mtr.mtr_descripcion, ");
		sql.append("   mtr.timt_id, ");
		sql.append("   crr.crr_id id_carrera, ");
		sql.append("   crr.crr_descripcion, ");
		sql.append("   nvl.nvl_id, ");
		sql.append("   nvl.nvl_descripcion, ");
		sql.append("   CASE ");
		sql.append("     WHEN mtr.mtr_creditos IS NULL ");
		sql.append("     THEN mtr.mtr_horas ");
		sql.append("     ELSE mtr.mtr_creditos ");
		sql.append("   END numero_creditos ");
		sql.append(" FROM materia mtr, ");
		sql.append("   carrera crr, ");
		sql.append("   malla_curricular_materia mlcrmt, ");
		sql.append("   nivel NVL, ");
		sql.append("   malla_curricular mlcr, ");
		sql.append("   malla_periodo mlpr, ");
		sql.append("   periodo_academico prac ");
		sql.append(" WHERE mlcrmt.mtr_id = mtr.mtr_id ");
		sql.append(" AND mlcr.mlcr_id    = mlcrmt.mlcr_id ");
		sql.append(" AND mlcr.crr_id     = crr.crr_id ");
		sql.append(" AND mlpr.mlcr_id    = mlcr.mlcr_id ");
		sql.append(" AND mlpr.prac_id    = prac.prac_id ");
		sql.append(" AND mlcrmt.NVL_ID   = nvl.NVL_ID ");
		sql.append(" AND prac.prac_estado =  " + PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND mtr.mtr_estado =  " + MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
		sql.append(" AND mlcr.mlcr_id = ?");
		sql.append(" ORDER BY nvl.nvl_id ");

		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mlcrId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarMaterias(rs));
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
		
		if(retorno.isEmpty()){
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
			retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
			retorno.setCrrFechaCreacion(rs.getDate(JdbcConstantes.CRR_FECHA_CREACION));
			retorno.setCrrResolucion(rs.getString(JdbcConstantes.CRR_RESOLUCION));
			retorno.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));
			
			retorno.setTpfrmlId(rs.getInt(JdbcConstantes.TIFRML_ID));
			retorno.setTpfrmlDescripcion(rs.getString(JdbcConstantes.TIFRML_DESCRIPCION));
			retorno.setTpfrmlEstado(rs.getInt(JdbcConstantes.TIFRML_ESTADO));
			
			retorno.setMlcrId(rs.getInt(JdbcConstantes.MLCR_ID));
			retorno.setMlcrCodigo(rs.getString(JdbcConstantes.MLCR_CODIGO));
			retorno.setMlcrDescripcion(rs.getString(JdbcConstantes.MLCR_DESCRIPCION));
			retorno.setMlcrEstado(rs.getInt(JdbcConstantes.MLCR_ESTADO));
			retorno.setMlcrFechaInicio(rs.getDate(JdbcConstantes.MLCR_FECHA_INICIO));
			retorno.setMlcrFechaFin(rs.getDate(JdbcConstantes.MLCR_FECHA_FIN));
			retorno.setMlcrTotalHoras(rs.getInt(JdbcConstantes.MLCR_TOTAL_HORAS));
			retorno.setMlcrTotalMaterias(rs.getInt(JdbcConstantes.MLCR_TOTAL_MATERIAS));
			retorno.setMlcrTipoOrgAprendizaje(rs.getInt(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE));
			retorno.setMlcrVigencia(rs.getInt(JdbcConstantes.MLCR_VIGENTE));
			retorno.setMlcrTipoAprobacion(rs.getInt(JdbcConstantes.MLCR_TIPO_APROBACION));
			retorno.setMlcrCrrId(rs.getInt(JdbcConstantes.MLCR_CRR_ID));
			retorno.setMlcrTpfrmlId(rs.getInt(JdbcConstantes.MLCR_TIFRML_ID));
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			
		return retorno;
	} 
	
	
	private MallaCurricularDto transformarResultSetAbuscarMallaCurricular(ResultSet rs) throws SQLException{
		MallaCurricularDto retorno = new MallaCurricularDto();

		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));

		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrFechaCreacion(rs.getDate(JdbcConstantes.CRR_FECHA_CREACION));
		retorno.setCrrResolucion(rs.getString(JdbcConstantes.CRR_RESOLUCION));
		retorno.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));

		retorno.setTpfrmlId(rs.getInt(JdbcConstantes.TIFRML_ID));
		retorno.setTpfrmlDescripcion(rs.getString(JdbcConstantes.TIFRML_DESCRIPCION));
		retorno.setTpfrmlEstado(rs.getInt(JdbcConstantes.TIFRML_ESTADO));

		retorno.setMlcrId(rs.getInt(JdbcConstantes.MLCR_ID));
		retorno.setMlcrCodigo(rs.getString(JdbcConstantes.MLCR_CODIGO));
		retorno.setMlcrDescripcion(rs.getString(JdbcConstantes.MLCR_DESCRIPCION));
		retorno.setMlcrEstado(rs.getInt(JdbcConstantes.MLCR_ESTADO));
		retorno.setMlcrFechaInicio(rs.getDate(JdbcConstantes.MLCR_FECHA_INICIO));
		retorno.setMlcrFechaFin(rs.getDate(JdbcConstantes.MLCR_FECHA_FIN));
		retorno.setMlcrTotalHoras(rs.getInt(JdbcConstantes.MLCR_TOTAL_HORAS));
		retorno.setMlcrTotalMaterias(rs.getInt(JdbcConstantes.MLCR_TOTAL_MATERIAS));
		retorno.setMlcrTipoOrgAprendizaje(rs.getInt(JdbcConstantes.MLCR_TIPO_ORG_APRENDIZAJE));
		retorno.setMlcrVigencia(rs.getInt(JdbcConstantes.MLCR_VIGENTE));
		retorno.setMlcrTipoAprobacion(rs.getInt(JdbcConstantes.MLCR_TIPO_APROBACION));

		return retorno;
	}
	
	private MallaCurricularDto transformarResultSetAbuscarMallaCurricularPosgrado(ResultSet rs) throws SQLException{
		MallaCurricularDto retorno = new MallaCurricularDto();

		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));

		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));

		return retorno;
	}
	
	private MallaCurricularDto transformarResultSetAbuscarMallaCurricularPorNivel(ResultSet rs) throws SQLException {
		return null;
	}
	
	private MateriaDto transformarResultSetAbuscarMaterias(ResultSet rs) throws SQLException{
		MateriaDto retorno = new MateriaDto();
		retorno.setMtrId(rs.getInt(1));
		retorno.setMtrCodigo(rs.getString(2));
		retorno.setMtrDescripcion(rs.getString(3));
		retorno.setTpmtId(rs.getInt(4));
		retorno.setCrrId(rs.getInt(5));
		retorno.setCrrDescripcion(rs.getString(6));
		retorno.setNvlId(rs.getInt(7));
		retorno.setNvlDescripcion(rs.getString(8));
		retorno.setMtrCreditos(rs.getInt(9));
		return retorno;
	}
}
