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
   
 ARCHIVO:     FichaInscripcionDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla FichaInscripcion.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-03-2017			David Arellano					       EmisiÃ³n Inicial
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
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;

/**
 * EJB FichaInscripcionDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla FichaInscripcion.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class FichaInscripcionDtoServicioJdbcImpl implements FichaInscripcionDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@EJB
	private CarreraServicio servCarreraServicio;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarXidentificacionXestado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" =  ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoId); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		}	
		
		return retorno;
	}
	
	
	
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona , el estado de la ficha inscripcion y el rol de estudiante posgrado
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @param rolId - id del rol de posgrado
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarEstudiantePosgradoPidentificacionPfcinEstado(String prsIdentificacion, int fcinEstado) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" = ");sbSql.append(RolConstantes.ROL_ESTUDIANTEPOSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" =  ");sbSql.append(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, prsIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, fcinEstado); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoPosgrado(rs));
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",prsIdentificacion)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFcinXidentificacionXestado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoId); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarFcinPregradoXidentificacionXestado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" in (");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_VALUE);
			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoId); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		}	
		
		return retorno;
	}

	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<FichaInscripcionDto> buscarXidentificacionXestadoXMatriculado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoId); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<FichaInscripcionDto> buscarXidentificacionXestadoXMatriculadoPosgrado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoId); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFcinXidentificacionXcarrera(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, crrId);
		
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarFcinXidentificacionXcarrera(rs);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		
		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXidentificacionXcarrera(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, crrId);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarFcinXidentificacionXcarrera(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null){
//			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		
//		}	
		
		return retorno;
	}
	
	@Override
	public FichaInscripcionDto buscarFichaInscripcionNivelacionXidentificacion(String personaIdentificacion) throws FichaInscripcionDtoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);sbSql.append(" =  ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" =  ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarFcinXidentificacionXcarrera(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null){
//			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		
//		}	
		
		return retorno;
	}
	
	/**MQ
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera  ficha inscripcion activa
	 * @param personaIdentificacion - String de la indentificacion de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXidentificacionXcarreraNueva(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" =  ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			
			pstmt.setInt(2, crrId);
			
//		  System.out.println(sbSql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarFcinXidentificacionXcarreraNueva(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null){
//			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		
//		}	
		
		return retorno;
	}
	
	@Override
	public void correcionFichaErrorCargaNivelacion() throws FichaInscripcionDtoException{
		PreparedStatement pstmt = null;
		Connection con = null;
		PreparedStatement pstmt1 = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" update ficha_inscripcion set cncr_id=33,fcin_cncr_area=21 where fcin_id in(653383,	653397,	653411,	653423,	653458,	655041,"	
+"655042,	655047,	655055,	655063,	655069,	655078,	655083,	655087,	655096,	655106,	655109,	655114,	653513,	653524,	653536,	653540,"	
+"653543,	653545,	655963,	655967,	655972,	655974,	655978,	655979,	655980,	655984,	655999,	656017,	656021,	656024,	656028,	656031,"	
+"656032,	656034,	656040,	652883,	652888,	652896,	652898,	652903,	652905,	652909,	652914,	652921,	652924,	652926,	652927,	652928,"	
+"652934,	652940,	652962,	652965,	653581,	653585,	653589,	653593,	653602,	653622,	653641,	653643,	653654,	656062,	656064,	656074,"	
+"656075,	656076,	656080,	656081,	656089,	656094,	656096,	656099,	656102,	656103,	656110,	656117,	656119,	656120,	656122,	656123,"	
+"656133,	652983,	652987,	652989,	653001,	653003,	653008,	653018,	653021,	653030,	653045,	653048,	653054,	653058,	653061,	653664,"	
+"653684,	653685,	653687,	653688,	653694,	653698,	653702,	653703,	653710,	653719,	653742,	653747,	653748,	657278,	657287,	657288,"	
+"657293,	657312,	657325,	657335,	657339,	657349,	657352,	657353,	653072,	653075,	653077,	653089,	653116,	653133,	653138,	653148,"	
+"653150,	653154,	653789,	653791,	653793,	653802,	653824,	653825,	653837,	653843,	653844,	653848,	657369,	657384,	657391,	657394,"	
+"657407,	657421,	657430,	657431,	657432,	657438,	657439,	657452,	657456,	657458,	657460,	653173,	653187,	653188,	653189,	653192,"	
+"653195,	653197,	653200,	653202,	653225,	653228,	653229,	653234,	653237,	653240,	653248,	653927,	653933,	653949,	653954,	653958,"	
+"653967,	653970,	653971,	653972,	653984,	653988,	653994,	657474,	657478,	657479,	657485,	657487,	652380,	652391,	652393,	652394,"	
+"652396,	652412,	652421,	652432,	652438,	652446,	652448,	652449,	652455,	652456,	652459,	654008,	654011,	654014,	654021,	654027,"	
+"654035,	654042,	654044,	654049,	654058,	654060,	654062,	654069,	654080,	652485,	652496,	652497,	652499,	652502,	652520,	652521,"	
+"652527,	652558,	652561,	652564,	654094,	654116,	654121,	654155,	654165,	654173,	652571,	652573,	652578,	652597,	652614,	652621,"	
+"652632,	652650,	652651,	654209,	654245,	654246,	654253,	654260,	654262,	654263,	654267,	654280,	652666,	652671,	652692,	652696,"	
+"652700,	652702,	652704,	652706,	652708,	652709,	652711,	652715,	654288,	654291,	654293,	654304,	654324,	654327,	654333,	654339,	"
+"654358,	654366,	654367,	652757,	652766,	652779,	652786,	652789,	652793,	652796,	652798,	652802,	652803,	652810,	652815,	652817,"	
+"652832,	652833,	654383,	654409,	654411,	654418,	654423,	655432,	655433,	655436,	655438,	655446,	655453,	652849,	652855,	652864,"	
+"652866,	652869,	652876,	653260,	653266,	653272,	653282,	653289,	653290,	653294,	653309,	655485,	655486,	655487,	655506,	655524,"	
+"655547,	655566,	655568,	653325,	653332,	653343,	653344,	653355,	653377,	655142,	655150,	655165,	655169,	655170,	655572,	655598,"	
+"655600,	655607,	655609,	655625,	655635,	655638,	655644,	655656,	655658,	654644,	654669,	654688,	654689,	654692,	654711,	654715,"	
+"654734,	655117,	655121,	655126,	655132,	655175,	655211,	655215,	655217,	655222,	655226,	655237,	655242,	655243,	655667,	655672,"	
+"655675,	655679,	655688,	655698,	655707,	655711,	655742,	655744,	655754,	654752,	654753,	654779,	654821,	655256,	655272,	655282,"	
+"655287,	655298,	655307,	655320,	655766,	655770,	655773,	655779,	655783,	655793,	655794,	655799,	655810,	655814,	655815,	655817,"	
+"655835,	655845,	655856,	654836,	654854,	654857,	654859,	654880,	654885,	654886,	654903,	654917,	655350,	655351,	655375,	655392,"	
+"655394,	655396,	655397,	655403,	655408,	655414,	655866,	655876,	655877,	655885,	655891,	655905,	655908,	655921,	655923,	655925,	"
+"655926,	655941,	655942,	655945,	654949,	654958,	654959,	654971,	654979,	655004,	655005,	656297,	656317,	656329,	656333,	656354,"	
+"656359,	656363,	656366,	656388,	656390,	656401,	656413,	656417,	656424,	656450,	656456,	656485,	656486,	656494,	656502,	656511,"	
+"656538,	656540,	656551,	656552,	656570,	656574,	656576,	656582,	656600,	656615,	656620,	656634,	656646,	656650,	656653,	656655,"	
+"656668,	656670,	656779,	656788,	656791,	656823,	656839,	656842,	656843,	656850,	656853,	656953,	656969,	656984,	656997,	657007,"	
+"657015,	657018,	657027,	656676,	656684,	656722,	656726,	656737,	656757,	656760,	656766,	656880,	656903,	656906,	656907,	656919,"	
+"656932,	656940,	656942,	656949,	657059,	657069,	657075,	657076,	657079,	657099,	657100,	657121,	657129,	657134,	651895,	651898,	"
+"651904,	651907,	651912,	651913,	651927,	651935,	651942,	651957,	656152,	656156,	656165,	656173,	656193,	656203,	656206,	656211,"	
+"656214,	656215,	656232,	651968,	651969,	651974,	651976,	651981,	651987,	652004,	652018,	652020,	652042,	652053,	652058,	652069,	"
+"652073,	652078,	652086,	652094,	652100,	652143,	652152,	652154,	652155,	652165,	652167,	652170,	652193,	652199,	652203,	652210,"	
+"652212,	652220,	652226,	652227,	652230,	652237,	652238,	652244,	652248,	652261,	652262,	652265,	652274,	652275,	652278,	652282,"	
+"652283,	652288,	652289,	652301,	652305,	652315,	652326,	652327,	652334,	652346,	652369,	652372,	652375,	652378,	656245,	656246,"	
+"656253,	656258,	656259,	656262,	656278,	656286)");
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.executeUpdate();
			
//			StringBuilder sbSql1 = new StringBuilder();
//			sbSql.append(" update ficha_inscripcion set cncr_id=509,  fcin_cncr_area=228 where fcin_id in("
//					+" select fcin_id from ficha_inscripcion where fcin_carrera_siiu=183 and prac_id=630 and fcin_observacion like '%NIVELAC%' and fcin_tipo=3)");
//			
//			pstmt1 = con.prepareStatement(sbSql1.toString());
//			pstmt1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			try {
//				if (pstmt1 != null) {
//					pstmt1.close();
//				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e1) {
				}
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
		} finally {
			try {
				if (pstmt1 != null) {
					pstmt1.close();
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
		
	}
	
	
	@Override
	public void actualizarCarreraAreaXFcinId(Integer fcinId, Integer crrId, Integer areaId) throws FichaInscripcionDtoException{
		PreparedStatement pstmt = null;
		Connection con = null;
		PreparedStatement pstmt1 = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" update ficha_inscripcion set cncr_id=(select MAX(cncr_id) from configuracion_carrera where crr_id=");
			sbSql.append(areaId);
			sbSql.append("),fcin_carrera_siiu=");sbSql.append(crrId);sbSql.append(" ,fcin_cncr_area=");sbSql.append(areaId);
			sbSql.append(" where fcin_id=");sbSql.append(fcinId);
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e1) {
				}
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
		} finally {
			try {
				if (pstmt1 != null) {
					pstmt1.close();
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
		
	}
	
	/**MQ
	 * Realiza la busqueda de una Lista de FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
     * @param estadoId - id estao  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaInscripcionDto> buscarXidentificacionXcarreraXEstado(String personaIdentificacion, int crrId, int estadoId) throws FichaInscripcionDtoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
						
			sbSql.append(" , CASE WHEN fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);				
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			if(crrId!=GeneralesConstantes.APP_ID_BASE){
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			if(estadoId!=GeneralesConstantes.APP_ID_BASE){
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" = ? ");
			}			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			if(crrId!=GeneralesConstantes.APP_ID_BASE){
			pstmt.setInt(2, crrId);
			}
			if(estadoId!=GeneralesConstantes.APP_ID_BASE){
			pstmt.setInt(3, estadoId); 
			}
			
			
			
			//System.out.println(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				//retorno.add(transformarResultSetADto(rs));
				retorno.add(transformarResultSetADtoBuscarFcinXidentificacionXcarreraNueva(rs));
				
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null || retorno.size()<=0){
//			//throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		}	
//		
		return retorno;
	}
	
	
	
	
	/**MQ
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXidentificacionXcarreraxPeriodo(String personaIdentificacion, int crrId, int pracId) throws FichaInscripcionDtoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			
			
						
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, crrId);
			pstmt.setInt(3, pracId);
		  
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarFcinXidentificacionXcarreraNueva(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null){
//			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		
//		}	
		
		return retorno;
	}
	
	/**MQ
	 * Realiza la busqueda de un FichaInscripcionDto con FcinId Mayor  por la identifiacion de la persona , la carrera
	 * @param personaIdentificacion - String de la indentificacion de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXIdentificacionXCarreraXIdMayor(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException{
		FichaInscripcionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
	
		try {
			StringBuilder sbSql = new StringBuilder();
						
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" WHERE ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);sbSql.append(" = ");
			sbSql.append("( SELECT  MAX ( ");
			sbSql.append(" fcin1.");sbSql.append(JdbcConstantes.FCIN_ID);sbSql.append(" )");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
		
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" fcin1.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" fcin1.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" ) ");
			
			
//				System.out.println(sbSql.toString());		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, crrId);
				  
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarFichaInscripcionXIdentificacionXCarreraXIdMayor(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null){
//			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		
//		}	
		
		return retorno;
	}
	
	
	//--v2
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param prsIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarFichaInscripcionPorCarrera(String prsIdentificacion, int carreraId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_RETIRO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_REINICIO_ORIGEN);
			sbSql.append(" ,case when fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);sbSql.append(" is null then -99 else fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO); sbSql.append(" end ");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" ,case when fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);sbSql.append(" is null then -99 else fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);sbSql.append(" end ");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" ,fces. ");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
		sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
		sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
		sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
		sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
		sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
		sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
		
//		sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
//		sbSql.append(" in (");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
//			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
//			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_VALUE);
//			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE);
//			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE);
//			sbSql.append(" , ");sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
//		sbSql.append(" ) ");
//		sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append("FCIN_ESTADO_RETIRO");sbSql.append(" not in ( "+FichaInscripcionConstantes.ESTADO_RETIRO_ACTIVO_VALUE+" ) ");
		
		sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" =  "+ FichaInscripcionConstantes.ESTADO_RETIRO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
		sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, prsIdentificacion);
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoFull(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.no.encontrado.exception", prsIdentificacion)));
		}	
		
		return retorno;
	}
	
	public List<FichaInscripcionDto> buscarFichasInscripcion(String prsIdentificacion, int crrTipo) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();

		sbSql.append(" SELECT DISTINCT");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_RETIRO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_REINICIO_ORIGEN);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" ,fces. ");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
			sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
		sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, prsIdentificacion);
			pstmt.setInt(2, crrTipo);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoFull(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException("Usted no dispone de una ficha inscripción activa.");
		}	

		return retorno;
	}

	
	public List<FichaInscripcionDto> buscarFichasInscripcion(String identificacion, Integer[] estados) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();

			sbSql.append(" SELECT DISTINCT");
				sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_RETIRO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_REINICIO_ORIGEN);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
				sbSql.append(" ,fces. ");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
				sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
				sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
				sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
				sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" in  ");sbSql.append(Arrays.toString(estados).replace("[", "(").replace("]", ")"));
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion);

			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoFull(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException("Usted no dispone de una ficha inscripción activa.");
		}	

		return retorno;
	}

	public List<FichaInscripcionDto> buscarFichasInscripcion(Integer[] estados, Integer[] tiposIngreso, int pracId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();

			sbSql.append(" SELECT DISTINCT");
				sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
				sbSql.append(" ,fcin.");sbSql.append("FCIN_ESTADO_RETIRO");
				sbSql.append(" ,fcin.");sbSql.append("FCIN_REINICIO_ORIGEN");
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
				sbSql.append(" ,fces. ");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
				sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
				sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
				sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
				sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" in  ");sbSql.append(Arrays.toString(estados).replace("[", "(").replace("]", ")"));
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);sbSql.append(" in  ");sbSql.append(Arrays.toString(tiposIngreso).replace("[", "(").replace("]", ")"));
				
				if (pracId != GeneralesConstantes.APP_ID_BASE) {
					sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);sbSql.append(" =  ");sbSql.append(pracId);	
				}
				
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoFull(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException("Usted no dispone de una ficha inscripción activa.");
		}	

		return retorno;
	}
	
	public List<FichaInscripcionDto> buscarFichasInscripcion(String param, Integer[] estados, Integer[] tiposIngreso, int tipoBusqueda, int pracId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();

			sbSql.append(" SELECT DISTINCT");
				sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
				sbSql.append(" ,fcin.");sbSql.append("FCIN_ESTADO_RETIRO");
				sbSql.append(" ,fcin.");sbSql.append("FCIN_REINICIO_ORIGEN");
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
				sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
				sbSql.append(" ,fces. ");sbSql.append(JdbcConstantes.FCES_ID);
				sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
				sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_PROCESO);
				sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
				sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
				sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
				sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" in  ");sbSql.append(Arrays.toString(estados).replace("[", "(").replace("]", ")"));
				sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);sbSql.append(" in  ");sbSql.append(Arrays.toString(tiposIngreso).replace("[", "(").replace("]", ")"));

				if (tipoBusqueda == 0) {
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");	
				}else {
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" LIKE ? ");
				}
				
				if (pracId != GeneralesConstantes.APP_ID_BASE) {
					sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);sbSql.append(" =  ");sbSql.append(pracId);	
				}
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, param + "%");

			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoFull(rs));
			}
			
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.pregrado.full.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException("Usted no dispone de una ficha inscripción activa.");
		}	

		return retorno;
	}

	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificaciÃ³n de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> listarFcinXIdentificacionXEstado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ESPE_CODIGO);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" WHERE ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			pstmt.setString(1, personaIdentificacion); //cargo la identificacion de la persona
			pstmt.setInt(2, estadoId); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarFcinXIdentificacionXEstado(rs));
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
		}	
		
		return retorno;
	}
	
	
	
	
	/**MQ
	 * Realiza la busqueda de una Lista de FichaInscripcionDto por nota de corteId
     * @param notaCorteID - id nota de corte  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la nota de corte
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaInscripcionDto> listarXnotaCorte(int notaCorteId) throws FichaInscripcionDtoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_NOTA_CORTE_ID);sbSql.append(" = ?");
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, notaCorteId); //cargo la identificacion de la persona
						
		//	System.out.println(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListarXNotaCorte(rs));
				
			}
		} catch (SQLException e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
		
//		if(retorno == null || retorno.size()<=0){
//			//throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception",personaIdentificacion)));
//		}	
//		
		return retorno;
	}
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADto(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		//retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		return retorno;
	}

	
	/**
	 * Método privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtoNivelacion(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		//retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtoPosgrado(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPracId(rs.getInt(JdbcConstantes.FCIN_PRAC_ID));
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtoBuscarFcinXidentificacionXcarrera(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
				
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		try {
			retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return retorno;

	}

	
	/**
	 * Metodo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtoBuscarFcinXidentificacionXcarreraNueva(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
				
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinTipoIngreso(rs.getInt(JdbcConstantes.FCIN_TIPO_INGRESO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinPeriodoAcademico(rs.getInt(JdbcConstantes.FCIN_PRAC_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		return retorno;

	}
	
	/**
	 * Metodo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtoListarXNotaCorte(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
				
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));		
		return retorno;

	}
	
	/**
	 * Metodo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtoBuscarFichaInscripcionXIdentificacionXCarreraXIdMayor(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
				
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		
		return retorno;

	}
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset al Dto
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
private FichaInscripcionDto transformarResultSetADtoFull(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinEstadoRetiro(rs.getInt("FCIN_ESTADO_RETIRO"));
		retorno.setFcinReinicioOrigen(rs.getInt("FCIN_REINICIO_ORIGEN"));
		retorno.setFcinEstadoIngreso(rs.getInt(JdbcConstantes.FCIN_ESTADO_INGRESO));
		retorno.setFcinTipoIngreso(rs.getInt(JdbcConstantes.FCIN_TIPO_INGRESO));
		retorno.setPracId(rs.getInt(JdbcConstantes.FCIN_PRAC_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCncrModalidad(rs.getInt(JdbcConstantes.CNCR_MDL_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setCrrProceso(rs.getInt(JdbcConstantes.CRR_PROCESO));
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		return retorno;
	}
	
	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset al Dto
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private FichaInscripcionDto transformarResultSetADtolistarFcinXIdentificacionXEstado(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));
		retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinCncrId(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setFcinCarrera(rs.getInt(JdbcConstantes.FCIN_CARRERA));
		retorno.setFcinCncrArea(rs.getInt(JdbcConstantes.FCIN_CNCR_AREA));
		retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.FCIN_CARRERA_SIIU));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_CNCR_ID));
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrEspeCodigo(rs.getInt(JdbcConstantes.CRR_ESPE_CODIGO));
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		return retorno;
	}
	
	
	@Override
	public List<FichaInscripcionDto> listarFcinXRetornoNivelacion() throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" IN( ");
			sbSql.append(" select usro_id from ficha_inscripcion fcin, ficha_estudiante fces where fcin_observacion='APROBADO NIVELACION'" );
			sbSql.append(" and prac_id=350 and fces.fcin_id=fcin.fcin_id) ");
			sbSql.append(" and prac_id<>1  ");
			sbSql.append(" order by usro_id,prac_id desc ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarFcinRetorno(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception")));
		}	
		
		return retorno;
	}
	
	
	@Override
	public List<FichaInscripcionDto> listarFcinParaCorrecionCncr() throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException{
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , ");
			sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);
			sbSql.append("  where fcin_observacion='APROBADO NIVELACION'" );
			sbSql.append(" and prac_id=350 ");
			sbSql.append(" and fcin_matriculado=1 ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarFcinRetorno(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
			throw new FichaInscripcionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.no.encontrado.exception")));
		}	
		
		return retorno;
	}
	
	private FichaInscripcionDto transformarResultSetADtolistarFcinRetorno(ResultSet rs) throws SQLException{
		
		FichaInscripcionDto retorno = new FichaInscripcionDto();
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		return retorno;
	}
	
	
	@Override
	public void corregirCncrEnFcin(FichaInscripcionDto item ) {
				try {
					FichaInscripcion fcinAux=em.find(FichaInscripcion.class, item.getFcinId());
					ConfiguracionCarrera cncrAux = new ConfiguracionCarrera();
					Query query = em.createQuery(
						      "SELECT cncr FROM ConfiguracionCarrera cncr WHERE cncr.cncrCarrera.crrId = :crr");
							cncrAux = (ConfiguracionCarrera) query.setParameter("crr", fcinAux.getFcinCarreraSiiu()).setMaxResults(1).getSingleResult();
							if(cncrAux!=null){
								fcinAux.setFcinConfiguracionCarrera(cncrAux);
								em.merge(fcinAux);
								em.flush();	
							}else{
								try {
									Carrera auxCarrera = servCarreraServicio.buscarCarreraXEspeCodigo(item.getFcinCarrera());
									 query = em.createQuery(
										      "SELECT cncr FROM ConfiguracionCarrera cncr WHERE cncr.cncrCarrera.crrId = :crr");
										      System.out.println(item.getCrrId());
											cncrAux = (ConfiguracionCarrera) query.setParameter("crr", auxCarrera.getCrrId()).setMaxResults(1).getSingleResult();
											fcinAux.setFcinConfiguracionCarrera(cncrAux);
											em.merge(fcinAux);
											em.flush();	
								} catch (Exception e) {
									// TODO: handle exception
								}
								
							}
							
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	}




	@Override
	public List<FichaInscripcionDto> buscarFichasInscripcionRolEstudianteTipoNivelacion(Integer UsuarioRol, Integer nivelacion)
			throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException {
		List<FichaInscripcionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CARRERA_SIIU);
			sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_AREA);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" WHERE ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(UsuarioRol);
//			System.out.println(nivelacion);
			pstmt.setInt(1, UsuarioRol); //cargo la identificacion de la persona
			pstmt.setInt(2, nivelacion); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FichaInscripcionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNivelacion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new FichaInscripcionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaInscripcionDto.buscar.por.identificacion.por.estado.exception")));
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
}
