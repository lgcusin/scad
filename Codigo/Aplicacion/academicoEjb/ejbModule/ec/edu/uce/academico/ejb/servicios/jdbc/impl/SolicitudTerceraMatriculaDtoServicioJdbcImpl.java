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
   
 ARCHIVO:     SolicitudTerceraMatriculaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla SolicitudTerceraMatricula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-02-2018     	Marcelo Quishpe				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB SolicitudTerceraMatriculaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla SolicitudTerceraMatriculaDto.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class SolicitudTerceraMatriculaDtoServicioJdbcImpl implements SolicitudTerceraMatriculaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de todas las solicitudes que pertenecen a cada periodo, carrera, buscar por apellido, identificacion y estado de la solictud.
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(int pracId, int crrId, String personaApellido, String personaIdentificacion, int estadoSltrmt) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
					
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" = ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_RCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);	
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		    sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
		
			int contador = 1;
			pstmt.setInt(contador, estadoSltrmt);
			contador++;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, pracId); //cargo el periodo academico
			}
				contador++;
				pstmt.setInt(contador, crrId); //cargo la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(rs));
			}
		} catch (SQLException e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.exception")));
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
	
			//throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**SIIUSAU
	 * Realiza la busqueda de todas las solicitudes que pertenecen a cada periodo, carrera, buscar por apellido, identificacion y estado de la solicitud  para Estudiantes SIIUSAU.
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitudSIIUSAU(int pracId, int crrId, String personaApellido, String personaIdentificacion, int estadoSltrmt) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
					
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO_REGISTRO);
			sbSql.append(" = ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		    sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 1;
			pstmt.setInt(contador, estadoSltrmt);
			contador++;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, pracId); //cargo el periodo academico
			}
				contador++;
				pstmt.setInt(contador, crrId); //cargo la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(rs));
			}
		} catch (SQLException e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.exception")));
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
	
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	/**SIIU
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SIIU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera, estado de la solicitud y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitud(int pracId, int crrId, int fcesId , int estadoSltrmt, int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);
			sbSql.append(" = ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_RCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);	
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);
			sbSql.append(" = ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, estadoSltrmt);
			pstmt.setInt(2, fcesId);
			pstmt.setInt(3, tipoSolicitud);
			int contador=3;
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			pstmt.setInt(++contador, pracId); //cargo el periodo academico
			}
			
			pstmt.setInt(++contador, crrId); //cargo la carrera
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitud(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**SIIUSAU
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SAU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera, estado de la solicitud y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(int pracId, int crrId, int fcesId , int estadoSltrmt, int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO_REGISTRO);
			sbSql.append(" = ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);
			sbSql.append(" = ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, estadoSltrmt);
			pstmt.setInt(2, fcesId);
			pstmt.setInt(3, tipoSolicitud);
			int contador=3;
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			pstmt.setInt(++contador, pracId); //cargo el periodo academico
			}
			
			pstmt.setInt(++contador, crrId); //cargo la carrera
						
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	/**
	 * Reimprimir solicitud tercera matricula
	 */
	public List<SolicitudTerceraMatriculaDto> listarxIdentificacionxCarrera(String identificacion, int crrId) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID); sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
					
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			System.out.println(identificacion);
			System.out.println(crrId);
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, crrId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXIdentificacionxCarrera(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	/**
	 * Reimprimir solicitud, apelaciones tercera matricula
	 */
	public List<SolicitudTerceraMatriculaDto> listarxIdentificacionxCarreraSolicitudApelacion(String identificacion, int crrId, int tipo) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID); sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" in (");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE);sbSql.append(",");
			sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE);sbSql.append(")");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO_REGISTRO);
			sbSql.append(" = ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
			
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
		
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, tipo);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXIdentificacionxCarrera(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	/**
	 * Reimprimir solicitud, apelaciones tercera matricula , Ver solicitudes y apelaciones anteriores
	 */
	public List<SolicitudTerceraMatriculaDto> listarxIdentificacionxCarreraSolicitudApelacionAnteriores(String identificacion, int crrId, int tipo) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID); sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" =  ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" not in (");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE);sbSql.append(",");
			sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE);sbSql.append(")");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO_REGISTRO);
			sbSql.append(" = ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
			
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
		
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, tipo);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXIdentificacionxCarrera(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**LISTA de estudiantes para reimpresion de Reporte de solicitudes aprobadas negadas
	 * Realiza la busqueda de los estudiantes con solicitudes por carrera, por apellido, identificacion y estado aprobadas-negadas .
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXPrimerApellidoXIdentidadXAprobasNegadas(int pracId, int crrId, String personaApellido, String personaIdentificacion) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
					
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" in  (");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE);
			sbSql.append(" , ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE);sbSql.append(")");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		    sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			
			contador++;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, pracId); //cargo el periodo academico
			}
				contador++;
				pstmt.setInt(contador, crrId); //cargo la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(rs));
			}
		} catch (SQLException e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.exception")));
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
	
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**Lista de materias para reimpresion de  reporte de solicitudes aprobadas y negadas .
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SAU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera y ficha estudiante tipo solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(int pracId, int crrId, int fcesId , int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION_FINAL);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
     		sbSql.append(" in  (");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE);
			sbSql.append(" , ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE);sbSql.append(")");
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);
			sbSql.append(" = ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, fcesId);
			pstmt.setInt(2, tipoSolicitud);
			int contador=2;
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			pstmt.setInt(++contador, pracId); //cargo el periodo academico
			}
			
			pstmt.setInt(++contador, crrId); //cargo la carrera
						
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
		return  retorno;
	}
	
	
	/**LISTA de estudiantes para reimpresion de Reporte de apelaciones aprobadas negadas
	 * Realiza la busqueda de los estudiantes con solicitudes por carrera, por apellido, identificacion y estado aprobadas-negadas .
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarApelacionesXPeriodoXCarreraXPrimerApellidoXIdentidadXAprobasNegadas(int pracId, int crrId, String personaApellido, String personaIdentificacion) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
					
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
//			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
		
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" in  (");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE);
			sbSql.append(" , ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE);sbSql.append(")");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" like ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		    sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			int contador = 0;
			
			contador++;
			pstmt.setString(contador, "%"+personaIdentificacion.toUpperCase().trim()+"%"); //cargo la identificacion de la persona
			contador++;
			pstmt.setString(contador, "%"+personaApellido.toUpperCase().trim()+"%"); //cargo el apellido de la persona
			if(pracId != GeneralesConstantes.APP_ID_BASE.intValue()){
				contador++;
				pstmt.setInt(contador, pracId); //cargo el periodo academico
			}
				contador++;
				pstmt.setInt(contador, crrId); //cargo la carrera
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(rs));
			}
		} catch (SQLException e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
		
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.exception")));
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
	
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.apellido.identidad.estadoSolicitud.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**Lista de materias para reimpresion de  reporte de apelación aprobadas y negadas .
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SAU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera y ficha estudiante tipo solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarApelacionesXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(int pracId, int crrId, int fcesId , int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION_FINAL);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
     		sbSql.append(" in  (");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE);
			sbSql.append(" , ");sbSql.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE);sbSql.append(")");
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);
			sbSql.append(" = ? ");
			
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, fcesId);
			pstmt.setInt(2, tipoSolicitud);
			int contador=2;
			if(pracId != GeneralesConstantes.APP_ID_BASE){ 
			pstmt.setInt(++contador, pracId); //cargo el periodo academico
			}
			
			pstmt.setInt(++contador, crrId); //cargo la carrera
						
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
		return  retorno;
	}
	
	
	
	/**MQ
	 * Lista las solicitudes de tercera matrícula por materia y ficha estudiante
	 * Realiza la busqueda de todas las solicitadas tercera matricula por fichaEstudiante y materia
	 * @param mtrId - id de la materia
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las solicitudes de tercera matrícula por materia y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarSolicitudesxFcesIdXMtrId(int mtrId,  int fcesId) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION_FINAL);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ? ");
	
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fcesId);
			pstmt.setInt(2, mtrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
		return  retorno;
	}
	
	
	/**MQ
	 * Lista las solicitudes de tercera matrícula por materia y ficha estudiante y estao del registro
	 * Realiza la busqueda de todas las solicitadas tercera matricula por fichaEstudiante y materia
	 * @param mtrId - id del periodo
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las solicitudes de tercera matrícula por materia y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarSolicitudesxFcesIdXMtrIdxEstadoRegistro(int mtrId,  int fcesId, int estadoRegistro) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION_FINAL);
			
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO_REGISTRO);
			sbSql.append(" = ? ");
	
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
					
			pstmt.setInt(1, fcesId);
			pstmt.setInt(2, mtrId);
			pstmt.setInt(3, estadoRegistro);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
		return  retorno;
	}
	
	
	/**MQ
	 * Lista las solicitudes de tercera matrícula por ficha estudiante
	 * Realiza la busqueda de todas las solicitadas tercera matricula por fichaEstudiante 
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las solicitudes de tercera matrícula por materia y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<SolicitudTerceraMatriculaDto> listarSolicitudesxFcesId(int fcesId) throws SolicitudTerceraMatriculaDtoException{
		List<SolicitudTerceraMatriculaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
		
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_DESCRIPCION);
			sbSql.append(" , csl.");sbSql.append(JdbcConstantes.CSL_CODIGO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ID);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD);
			sbSql.append(" , sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_OBSERVACION_FINAL);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CAUSAL);sbSql.append(" csl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			 
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_CSL_ID);
			sbSql.append(" = ");sbSql.append(" csl.");sbSql.append(JdbcConstantes.CSL_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
     		sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			
    	    sbSql.append(" ORDER BY ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fcesId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<SolicitudTerceraMatriculaDto>();
			while(rs.next()){
				retorno.add(listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(rs));
			}
		} catch (SQLException e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.sql.exception")));
		} catch (Exception e) {
			throw new SolicitudTerceraMatriculaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.exception")));
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
		//	throw new SolicitudTerceraMatriculaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudTerceraMatriculaDto.listar.por.periodo.carrera.fichaEstudiante.estadoSolicitud.no.result.exception")));
		}	
		return  retorno;
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
	private SolicitudTerceraMatriculaDto transformarResultSetADtolistarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(ResultSet rs) throws SQLException{
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
//		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
//		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		 
		return retorno;
	} 
	
	
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset 
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private SolicitudTerceraMatriculaDto transformarResultSetADtolistarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitud(ResultSet rs) throws SQLException{
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
		retorno.setCslId(rs.getInt(JdbcConstantes.CSL_ID));
		retorno.setCslDescripcion(rs.getString(JdbcConstantes.CSL_DESCRIPCION));
		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
		retorno.setSltrmtDocumentoSolicitud(rs.getString(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		 
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset 
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private SolicitudTerceraMatriculaDto transformarResultSetADtolistarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(ResultSet rs) throws SQLException{
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setCslId(rs.getInt(JdbcConstantes.CSL_ID));
		retorno.setCslDescripcion(rs.getString(JdbcConstantes.CSL_DESCRIPCION));
		retorno.setCslCodigo(rs.getString(JdbcConstantes.CSL_CODIGO));
		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
		retorno.setSltrmtDocumentoSolicitud(rs.getString(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		 
		return retorno;
	} 
	
	private SolicitudTerceraMatriculaDto transformarResultSetADtolistarXIdentificacionxCarrera(ResultSet rs) throws SQLException{
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		
		
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		retorno.setCslId(rs.getInt(JdbcConstantes.CSL_ID));
		retorno.setCslDescripcion(rs.getString(JdbcConstantes.CSL_DESCRIPCION));
		retorno.setCslCodigo(rs.getString(JdbcConstantes.CSL_CODIGO));
		
		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
		retorno.setSltrmtDocumentoSolicitud(rs.getString(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		 
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset 
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private SolicitudTerceraMatriculaDto listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(ResultSet rs) throws SQLException{
		SolicitudTerceraMatriculaDto retorno = new SolicitudTerceraMatriculaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setCslId(rs.getInt(JdbcConstantes.CSL_ID));
		retorno.setCslDescripcion(rs.getString(JdbcConstantes.CSL_DESCRIPCION));
		retorno.setCslCodigo(rs.getString(JdbcConstantes.CSL_CODIGO));
		retorno.setSltrmtId(rs.getInt(JdbcConstantes.SLTRMT_ID));
		retorno.setSltrmtDocumentoSolicitud(rs.getString(JdbcConstantes.SLTRMT_DOCUMENTO_SOLICITUD));
		retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));
		retorno.setSltrmtObservacionFinal(rs.getString(JdbcConstantes.SLTRMT_OBSERVACION_FINAL));
		 
		return retorno;
	} 
}
