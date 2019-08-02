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
   
 ARCHIVO:     DocenteDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc del DTO de docente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-ABRIL-2017 		  Dennis Collaguazo			          Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * EJB DocenteDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc del DTO de docente.
 * @author dcollaguazo.
 * @version 1.0
 */
@Stateless
public class DocenteDtoServicioJdbcImpl implements DocenteDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPeriodo(int idUsuario, int idCarrera, int docente, int idPeriodoAcademico) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ? ");		
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, DetallePuestoConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado del detalle puesto del docente
			pstmt.setInt(2, idPeriodoAcademico); //cargo el id del periodo académico
			pstmt.setInt(3, idCarrera); //cargo el id de la carrera
			pstmt.setInt(4, docente); //cargo el tipo docente
			pstmt.setInt(5, idUsuario); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",idCarrera)));
		}	

		return retorno;
	}

	@Override
	public DocenteJdbcDto buscarDatosDocenteXUsuario(int idUsuario) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ROWNUM<2");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(idUsuario);
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoDatos(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
		}	

		return retorno;
	}
	

	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPeriodoActivo(int idUsuario, int idCarrera, int docente) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");	

			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
//			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, DetallePuestoConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado del detalle puesto del docente
//			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(2, docente); //cargo el tipo docente
			pstmt.setInt(3, idUsuario); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoPrActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",idCarrera)));
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocente(String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" (SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
		
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocentePeriodosAnteriores(String identificacionId, Integer pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" (SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, identificacionId);
			pstmt.setInt(2, pracId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocentePeriodosInactivos(String identificacionId, Integer pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" SELECT ");
//			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
//			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
//			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND ");sbSql.append(" dtps.dtps_prac_id");
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ");sbSql.append(pracId);
//			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDirectorPosgrado(String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" (SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setString(1, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	/**
	 * Método que busca los niveles del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesDocente(int crrId, String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl1.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl1.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl1 ");
			sbSql.append(" WHERE ");sbSql.append(" nvl1.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" IN ");
			sbSql.append(" (SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" ? ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId);
			pstmt.setString(2, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	/**
	 * Método que busca los niveles del programa de posgrado por perído activo
	 * @param crrId - id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarNivelesProgramaPosgrado(int crrId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ");
			sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException("No existen niveles para el programa seleccionado");
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las masterias del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - id de la carrera del docente da clase a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarMateriasDocente(int crrId, int nvlId, String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr1.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr1 ");
			sbSql.append(" WHERE ");sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" IN ");
			sbSql.append(" (SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" ) ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" ? ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			sbSql.append(" ORDER BY mtr1.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, crrId);
			pstmt.setInt(2, nvlId);
			pstmt.setString(3, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarMateriasDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo y materia
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<DocenteJdbcDto> listarHorarioPpracIdPprlIdPmtrId(int pracId, int prlId, int mtrId) throws DocenteDtoException  {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_ID);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			
			//lo comentado sirve para consultar el docente con el horario academico 
			
//			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
//			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
//			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
//			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
//			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
//			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, prlId);
			pstmt.setInt(3, mtrId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.exception")));
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
//			throw new DocenteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.no.result.exception")));
//		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo , materia y malla curricular paralelo
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @param mlcrprId - mlcrprId id de la malla curricular paralelo
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<DocenteJdbcDto> listarHorarioXpracIdXprlIdXmtrIdDocente(int pracId, int prlId, int mtrId, int mlcrprId) throws DocenteDtoException  {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_ID);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");sbSql.append(" left join ");
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");sbSql.append(" on ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			
			//lo comentado sirve para consultar el docente con el horario academico 
			
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
//			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
//			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
//			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
//			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			if(mlcrprId != GeneralesConstantes.APP_ID_BASE && mlcrprId != 0){
				sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
				sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
				sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			}
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			if(mlcrprId != GeneralesConstantes.APP_ID_BASE && mlcrprId != 0){
				pstmt.setInt(1, mlcrprId);
			}else{
				pstmt.setInt(1, pracId);
				pstmt.setInt(2, prlId);
				pstmt.setInt(3, mtrId);
			}
			
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.exception")));
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
//			throw new DocenteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo , materia y malla curricular paralelo
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @param mlcrprId - mlcrprId id de la malla curricular paralelo
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<DocenteJdbcDto> listarHorarioXpracIdXprlIdXmtrIdDocenteCompratida(int pracId, int prlId, int mtrId, int mlcrprId) throws DocenteDtoException  {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_ID);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");sbSql.append(" left join ");
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");sbSql.append(" on ");
			
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			
			//lo comentado sirve para consultar el docente con el horario academico 
			
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
//			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
//			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
//			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
//			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			if(mlcrprId != GeneralesConstantes.APP_ID_BASE && mlcrprId != 0){
				sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
				sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
				sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			}
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			if(mlcrprId != GeneralesConstantes.APP_ID_BASE && mlcrprId != 0){
				pstmt.setInt(1, mlcrprId);
			}else{
				pstmt.setInt(1, pracId);
				pstmt.setInt(2, prlId);
				pstmt.setInt(3, mtrId);
			}
			
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.exception")));
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
//			throw new DocenteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo y materia
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public DocenteJdbcDto docenteXpracIdXprlIdXmtrId(int pracId, int prlId, int mtrId) throws DocenteDtoException  {
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_ID);
			
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			
			//lo comentado sirve para consultar el docente con el horario academico 
			
//			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal, ");
//			sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl, ");
//			sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
//			sbSql.append(" AND hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
//			sbSql.append(" AND hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
//			sbSql.append(" AND ala.");sbSql.append(JdbcConstantes.ALA_ID);sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
//			sbSql.append(" AND hrac.");sbSql.append(JdbcConstantes.HRAC_ESTADO);sbSql.append(" = ");sbSql.append(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, prlId);
			pstmt.setInt(3, mtrId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.sql.exception")));
		} catch (Exception e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.listar.horario.periodo.paralelo.materia.exception")));
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
//			throw new DocenteDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaMateriaDto.listar.mallas.materias.idMalla.estados.mallas.materia.no.result.exception")));
//		}	
		return retorno;
	}
	
	
	/**
	 * Método que lista los docentes por carrera por apellido paterno o por identificación
	 * @param crrId - crrId id de la carrera para buscar
	 * @param prsPrimerApellido - prsPrimerApellido apellido paterno a buscar
	 * @param prsIdentificacion - prsIdentificacion número de identificación a buscar
	 * @param rolTipo - rolTipo tipo de rol que realiza la búsqueda entre el rol de secretarias
	 * @return - retorna la lista de docentes
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> listarPcrrIdPprsPrimerApellidoPprsIdentificacionPTipoRol(int crrId, String prsPrimerApellido, String prsIdentificacion, Integer rolTipo) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" like ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" like ? ");
			if(crrId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
				sbSql.append(" = ? ");
			}
			if(rolTipo == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" = ? ");
			}else if(rolTipo == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				sbSql.append(" AND (");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" = ? ");
				sbSql.append(" or ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
				sbSql.append(" = ? )");
			}
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			int cont = 0;
			pstmt.setInt(++cont, DetallePuestoConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado del detalle puesto del docente
			pstmt.setInt(++cont, 1); //cargo 1: tipo puesto docente
			pstmt.setString(++cont, "%"+prsPrimerApellido+"%"); 
			pstmt.setString(++cont, "%"+prsIdentificacion+"%"); 
			if(crrId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++cont, crrId); //cargo el id de la carrera
			}
			if(rolTipo == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				pstmt.setInt(++cont, CarreraConstantes.TIPO_POSGRADO_VALUE); //tipo de carrera
			}else if(rolTipo == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				pstmt.setInt(++cont, CarreraConstantes.TIPO_PREGRADO_VALUE); //tipo de carrera
				pstmt.setInt(++cont, CarreraConstantes.TIPO_NIVELEACION_VALUE); //tipo de carrera
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentes(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.carrea.apellido.identificacion.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.carrea.apellido.identificacions.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.carrea.apellido.identificacion.no.result.exception")));
		}	
		return retorno;
	}
	
	
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPrac(String identificacionId , int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" (SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ?");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, pracId);
			pstmt.setString(2, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracActivoCierre(String identificacionId ) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" (SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (0,2)");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setString(1, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracComp(String identificacionId , int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ?");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setInt(1, pracId);
			pstmt.setString(2, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteComp(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracRep(String identificacionId , int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setString(2, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracRepActivoCierre(String identificacionId ) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in(0,2) ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
//			pstmt.setInt(1, pracId);
			pstmt.setString(1, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPraXListMlcrprId(List<DocenteJdbcDto> listaMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);		
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			//verifico si selecciono todas las carreras o una sola
			if(listaMlcrpr.size() > 0){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);sbSql.append(" in ( ");
				for (int i = 0; i < listaMlcrpr.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaMlcrpr.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(listaMlcrpr.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listaMlcrpr) {
					contador++;
					pstmt.setInt(contador, item.getMlcrprId()); //cargo las carreras ids
				}
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPraXListMlcrprIdPrinci(List<DocenteJdbcDto> listaMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);		
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			//verifico si selecciono todas las carreras o una sola
			if(listaMlcrpr.size() > 0){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);sbSql.append(" in ( ");
				for (int i = 0; i < listaMlcrpr.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaMlcrpr.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(listaMlcrpr.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listaMlcrpr) {
					contador++;
					pstmt.setInt(contador, item.getMlcrprId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep1(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPraXListMlcrprIdPrinciNotas(List<DocenteJdbcDto> listaMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
//			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);		
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			//verifico si selecciono todas las carreras o una sola
			if(listaMlcrpr.size() > 0){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);sbSql.append(" in ( ");
				for (int i = 0; i < listaMlcrpr.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaMlcrpr.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 0;
			if(listaMlcrpr.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listaMlcrpr) {
					contador++;
					pstmt.setInt(contador, item.getMlcrprId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep2(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<DocenteJdbcDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_SUB_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");sbSql.append(" hrac.mlcrpr_id = mlcrpr.mlcrpr_id "
								 + "and prl.prl_id = mlcrpr.prl_id "
								+ " and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
								+" and mtr.mtr_id = mlcrmt.mtr_id "
								+" and nvl.nvl_id = mlcrmt.nvl_id "
								+" and prac.prac_id = prl.prac_id "
								+" and crr.crr_id = mtr.crr_id "
								+" and dpn.dpn_id = crr.dpn_id ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(" , ");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ");sbSql.append(ParaleloConstantes.ESTADO_ACTIVO_VALUE);

				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
				sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			sbSql.append(" in ( ");
			
			sbSql.append(" select hrac.mlcrpr_id from persona prs ,ficha_docente fcdc ,detalle_puesto dtps ,carga_horaria crhr, usuario usr"
					+" ,malla_curricular_paralelo mlcrpr ,horario_academico hrac ,paralelo prl ,malla_curricular_materia mlcrmt ,materia mtr "
					+" ,nivel nvl ,carrera crr where prs.prs_id = fcdc.prs_id and fcdc.fcdc_id = dtps.fcdc_id and dtps.dtps_id = crhr.dtps_id "
					+" and crhr.mlcrpr_id = mlcrpr.mlcrpr_id and hrac.mlcrpr_id = mlcrpr.mlcrpr_id and prl.prl_id = mlcrpr.prl_id and mlcrmt.mlcrmt_id = mlcrpr.mlcrmt_id "
					+" and mtr.mtr_id = mlcrmt.mtr_id and nvl.nvl_id = mlcrmt.nvl_id and crr.crr_id = mtr.crr_id and prs.prs_id=usr.prs_id");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ) ");
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			System.out.println(sbSql);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(materiaId);
//			System.out.println(docente);
				pstmt.setInt(1, carreraId);
				pstmt.setInt(2, nivelId);
				pstmt.setInt(3, materiaId);
			pstmt.setInt(4, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(rs));
			}
		} catch (SQLException e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.sql.exception")));
		} catch (Exception e) {
			throw new ParaleloDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.exception")));
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
//			throw new ParaleloDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ParaleloDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarCarrerasXMlcrprId(int idMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, idMlcrpr); //cargo las carreras ids
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarCarreraDocente(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesMlcrprId(int mlcrprId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);		
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, mlcrprId); //cargo las carreras ids
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep1(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception")));
//		}	

		return retorno;
	}
	
	
	
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPrac(int idUsuario, int idCarrera, int docente,int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");	

			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
//			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, DetallePuestoConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado del detalle puesto del docente
//			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(3, docente); //cargo el tipo docente
			pstmt.setInt(4, idUsuario); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoPrActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",idCarrera)));
		}	

		return retorno;
	}
	
	
	
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public DocenteJdbcDto buscarDocenteInactivoXUsuarioXCarreraXPrac(int idUsuario, int idCarrera, int docente,int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");	

			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
//			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
//			sbSql.append(" = ");sbSql.append(idCarrera);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, DetallePuestoConstantes.ESTADO_INACTIVO_VALUE); //cargo el estado del detalle puesto del docente
//			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(3, docente); //cargo el tipo docente
			pstmt.setInt(4, idUsuario); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoPrActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",idCarrera)));
		}	

		return retorno;
	}
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPracXTipoCarrera(int idUsuario, int idCarrera, int docente,int pracId, int tipoCarrera) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT distinct ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");	

			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
//			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" = ? ");	
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
//			System.out.println(sbSql);
//			System.out.println(pracId);
//			System.out.println(docente);
//			System.out.println(idUsuario);
//			System.out.println(tipoCarrera);
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, DetallePuestoConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado del detalle puesto del docente
//			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(3, docente); //cargo el tipo docente
			pstmt.setInt(4, idUsuario); //cargo el id del usuario
			pstmt.setInt(5, tipoCarrera); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoPrActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",idCarrera)));
		}	

		return retorno;
	}
	
	
	@Override
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPracAnterior(int idUsuario, int idCarrera, int docente,int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		DocenteJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");	

//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
//			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
//			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TIPS_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY  ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,  ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
//			pstmt.setInt(2, DetallePuestoConstantes.ESTADO_ACTIVO_VALUE); //cargo el estado del detalle puesto del docente
//			pstmt.setInt(2, idCarrera); //cargo el id de la carrera
			pstmt.setInt(2, docente); //cargo el tipo docente
			pstmt.setInt(3, idUsuario); //cargo el id del usuario
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoPrActivo(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",idCarrera)));
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca los niveles del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesDocenteXPrac(int crrId, String identificacionId, int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl1.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl1.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl1 ");
			sbSql.append(" WHERE ");sbSql.append(" nvl1.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" IN ");
			sbSql.append(" (SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ?");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" ? ");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setInt(2, crrId);
			pstmt.setString(3, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca los niveles del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesDocenteXPracComp(List<DocenteJdbcDto> listCarreras, String identificacionId, int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" nvl1.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl1.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl1 ");
			sbSql.append(" WHERE ");sbSql.append(" nvl1.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" IN ");
			sbSql.append(" (SELECT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ?");
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" = ");sbSql.append(" ? ");
			
			//verifico si selecciono todas las carreras o una sola
			if(listCarreras.size() > 0){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listCarreras.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}
			
			
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setString(2, identificacionId);
			
			int contador = 2;
			if(listCarreras.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listCarreras) {
					contador++;
					pstmt.setInt(contador, item.getCrrId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.exception")));
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
//			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.usuario.carrera.docente.periodo.academico.no.result.exception",identificacionId)));
//		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente por perido academico activo no cierre
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public List<DocenteJdbcDto> buscarCarrerasXIdentificacionXPeriodoActivo(String identificacionId, int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ");
			
			sbSql.append(" (SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" = ? ");
			sbSql.append(" ) ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, pracId);
			pstmt.setString(2, identificacionId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocente(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.exception")));
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
			throw new DetallePuestoDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.no.result.exception",identificacionId)));
		}	

		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXcarreraXnivelXdocente(int periodoId, int carreraId, int nivelId, int docente) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, periodoId);
			pstmt.setInt(3, carreraId);
			pstmt.setInt(4, nivelId);
			pstmt.setInt(5, docente);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoParalelo(rs));//transformarResultSetADtoXPeriodoActivoXCarreraXNivel
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.exception")));
		}  finally {
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXcarreraXnivelXdocenteComp(int periodoId, List<DocenteJdbcDto> listCarreras, List<DocenteJdbcDto> listNiveles, int docente) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			
			sbSql.append(" WHERE ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
//			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
//			sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
//			sbSql.append(" = ? ");
			
			if(listCarreras.size() > 0){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listCarreras.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}
			
			if(listNiveles.size() > 0){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listNiveles.size(); i++) {
					sbSql.append(" ? ");
					if(i != listNiveles.size() -1) {
			         sbSql.append(","); 
			        }
				}
				sbSql.append(" ) ");
			}
			
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" ORDER BY ");
			sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, ParaleloConstantes.ESTADO_ACTIVO_VALUE);
			pstmt.setInt(2, periodoId);
			pstmt.setInt(3, docente);
			
			int contador = 3;
			if(listCarreras.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listCarreras) {
					contador++;
					pstmt.setInt(contador, item.getCrrId()); //cargo las carreras ids
				}
			}
			if(listNiveles.size() > 0){ // caso para setear la lista de carreras
				for (DocenteJdbcDto item : listNiveles) {
					contador++;
					pstmt.setInt(contador, item.getNvlId()); //cargo las carreras ids
				}
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoParalelo(rs));//transformarResultSetADtoXPeriodoActivoXCarreraXNivel
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteDto.buscar.por.identificacion.periodo.activo.exception")));
		}  finally {
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXcarreraXnivelXparaleloXdocente(int periodoId, int carreraId, int nivelId, int paraleloId, int docenteId) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, paraleloId);
			pstmt.setInt(5, docenteId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateria(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXPeriodoXcarreraXnivelXdocente(int periodoId, int carreraId, int nivelId, int docenteId) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
//			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
//			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ? ");
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
//			System.out.println(sbSql);
//			System.out.println(periodoId);
//			System.out.println(carreraId);
//			System.out.println(nivelId);
//			System.out.println(docenteId);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, carreraId);
			pstmt.setInt(3, nivelId);
			pstmt.setInt(4, docenteId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaNotas(rs));
			}
		} catch (SQLException e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXdocente(int periodoId, int docenteId) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
			
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MTR_CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MLCR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);
			sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ? ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, docenteId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateria(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	
	//BUSCAR DOCENTES POR MATERIAS 
	
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	//public List<DocenteJdbcDto>  listarXDocentesXMateria(int pracId, int dpnId, int crrId , String usrIdentificacion,Usuario usuario) throws DetallePuestoDtoJdbcException {
		public List<DocenteJdbcDto>  listarXDocentesXMateria(int pracId, String usrIdentificacion,Usuario usuario) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);	
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" , ( SELECT ");
			sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr1, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt1, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1 ");
			sbSql.append(" WHERE ");sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt1.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt1.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" ) AS materiacompartida ");
			
			sbSql.append(" , ( SELECT ");
			sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl1, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1 ");
			sbSql.append(" WHERE ");sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" ) AS paralelocompartido ");
			
			sbSql.append(" , ( SELECT ");
			sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl1, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1 ");
			sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" ) AS carreracompartida ");
            
            sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
			sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
            sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
            sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
            sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
            sbSql.append(" on ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
		
			sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
					
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" =  ");sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);
			
//			if (crrId!=GeneralesConstantes.APP_ID_BASE) {
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//				sbSql.append(" = ? ");
//			}
//			else {
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//				sbSql.append(" in( ");
//				sbSql.append(" Select crr.crr_id from carrera crr,rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");
//				sbSql.append(" where crr.crr_id = roflcr.crr_id AND roflcr.usro_id= usro.usro_id ");
//				sbSql.append(" and usro.usr_id = usr.usr_id ");
//				sbSql.append(" and usr.usr_id = ");
//				sbSql.append(usuario.getUsrId());
//				sbSql.append(" and usro.rol_id = ");sbSql.append(RolConstantes.ROL_SECRECARRERA_VALUE);
//			}
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" like ? ");
			sbSql.append(" ORDER BY "); 
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 1;  
			
//			if (crrId!=GeneralesConstantes.APP_ID_BASE) {
//				pstmt.setInt(contador, crrId);
//				contador= contador +1; 
//			}
			
			pstmt.setInt(contador, pracId);
			pstmt.setString(++contador, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(usrIdentificacion)+"%");
			
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarDocentesXMateria(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	//-------
	
	
	
	//BUSCAR DOCENTES POR MATERIAS POR CARRERAS
	
	
		/**
		 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
		 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
		 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
		 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
		 */
		@Override
			public List<DocenteJdbcDto>  listarXDocentesXMateriaXCarreraXMateria(int pracId, int dpnId, int crrId, Usuario usuario, int tipoUsuario ) throws DetallePuestoDtoJdbcException {
			List<DocenteJdbcDto> retorno = null;
			PreparedStatement pstmt = null;
			Connection con = null;
			ResultSet rs = null;
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");
				sbSql.append(" DISTINCT ");
				sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
				sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_CODIGO);
				sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
				sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
				sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);	
				sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
				
				sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
				sbSql.append(" , hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
				
				sbSql.append(" , ( SELECT ");
				sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr1, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt1, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1 ");
				sbSql.append(" WHERE ");sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_ID);
				sbSql.append(" = ");sbSql.append(" mlcrmt1.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrmt1.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" ) AS materiacompartida ");
				
				sbSql.append(" , ( SELECT ");
				sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl1, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1 ");
				sbSql.append(" WHERE ");sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" ) AS paralelocompartido ");
				
				sbSql.append(" , ( SELECT ");
				sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr1, ");
				sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl1, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1 ");
				sbSql.append(" WHERE ");sbSql.append(" crr1.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ");sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
				sbSql.append(" AND ");sbSql.append(" prl1.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
				sbSql.append(" AND ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);sbSql.append(" ) AS carreracompartida ");
				
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
				sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
				sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol, ");
				sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
				sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
				sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
				sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
				sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
				sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
	            sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
	            sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
	            sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
	            sbSql.append(" on ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
	            
				sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
				
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
				
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
				
				sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
				sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
				
				sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
				sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
				
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
				
				sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
				sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				
				sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
				sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
				
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
				
				sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
				
				sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
				
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
						
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" =  ");sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);
				
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
				if (crrId != GeneralesConstantes.APP_ID_BASE) {
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
				}else {
					if(tipoUsuario != RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
						sbSql.append(" in( ");
						sbSql.append(" Select crr.crr_id from carrera crr,rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");
						sbSql.append(" where crr.crr_id = roflcr.crr_id AND roflcr.usro_id= usro.usro_id ");
						sbSql.append(" and usro.usr_id = usr.usr_id ");
						sbSql.append(" and usr.usr_id = ");sbSql.append(usuario.getUsrId());
//						sbSql.append(" and usro.rol_id = ");sbSql.append(RolConstantes.ROL_SECRECARRERA_VALUE);
						sbSql.append(" and usro.rol_id = ");sbSql.append(tipoUsuario);
						sbSql.append(")");
					}
				}
				
				
				

				sbSql.append(" ORDER BY "); 
				sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
				sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				
				int contador = 0;  
				pstmt.setInt(++contador, pracId);
				pstmt.setInt(++contador, dpnId);
				if (crrId != GeneralesConstantes.APP_ID_BASE) {
					pstmt.setInt(++contador, crrId);
				}
				


				
				rs = pstmt.executeQuery();
				retorno = new ArrayList<DocenteJdbcDto>();
				while(rs.next()){
					retorno.add(transformarResultSetADtoBuscarDocentesXMateria(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
			} catch (Exception e) {
				e.printStackTrace();
				throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
		
		//BUSCAR CARGA HORARIA DEL DOCENTE
		
		
				@Override
				public List<DocenteJdbcDto>  listarXPeriodoAcademicoXDependenciaXCarrera(int pracId, int dpnId, int crrId, Usuario usuario, int tipoUsuario ) throws DetallePuestoDtoJdbcException {
				List<DocenteJdbcDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" DISTINCT ");
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
					sbSql.append(" , rllb.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
					sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
					sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);	
					sbSql.append(" , pst.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					sbSql.append(" , tmdd.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_NUM_HORAS);
					
					sbSql.append(" , ( SELECT ");
					sbSql.append("   SUM ");
					sbSql.append(" (crhr1.");sbSql.append(JdbcConstantes.CRHR_NUM_HORAS);sbSql.append(")");
					sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr1, ");
					sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt1, ");
					sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr1, ");
					sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps1, ");
					sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr1 ");
					sbSql.append(" WHERE ");
					sbSql.append(" dtps1.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr1.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND mtr1.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt1.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" AND mlcrmt1.");sbSql.append(JdbcConstantes.MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrpr1.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND mlcrpr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);sbSql.append(" = ");sbSql.append(" crhr1.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" dtps1.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mtr1.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" AND crhr1.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND crhr1.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");sbSql.append(") AS ImparticionesClases");
					sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
					sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol, ");
					sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
					sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
					sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
					sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
					sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
					sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
					sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
		            sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
		            sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
		            sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
		            sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst,");
		            sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" tmdd, ");
		            sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" rllb ");
					sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
					
					sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
					
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
					
					sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
					
					sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
					
					sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
					
					sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
					sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					
					sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
					sbSql.append(" = ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
					
					sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
					
					sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
					
					sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
					
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
					
					sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					
					
					sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.PST_ID);
					
					sbSql.append(" AND ");sbSql.append(" tmdd.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" = ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.TMDD_ID);
					
					sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" = ");sbSql.append(" rllb.");sbSql.append(JdbcConstantes.RLLB_ID);
					
					sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =  ");sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);
					
					sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
					sbSql.append(" =  ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
					
					
					sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
					if (crrId != GeneralesConstantes.APP_ID_BASE) {
						sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
					}else {
						if(tipoUsuario != RolConstantes.ROL_SOPORTE_VALUE.intValue()){
							sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
							sbSql.append(" in( ");
							sbSql.append(" Select crr.crr_id from carrera crr,rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");
							sbSql.append(" where crr.crr_id = roflcr.crr_id AND roflcr.usro_id= usro.usro_id ");
							sbSql.append(" and usro.usr_id = usr.usr_id ");
							sbSql.append(" and usr.usr_id = ");sbSql.append(usuario.getUsrId());
							sbSql.append(" and usro.rol_id = ");sbSql.append(RolConstantes.ROL_DIRCARRERA_VALUE);
							sbSql.append(")");
						}
					}
					sbSql.append(" GROUP BY "); 
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);	
					sbSql.append(" , rllb.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);	
					sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
					sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
					sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);	
					sbSql.append(" , pst.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					sbSql.append(" , tmdd.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_NUM_HORAS);
					sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);

					sbSql.append(" ORDER BY "); 
					sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					
					pstmt.setInt(1, pracId);
					pstmt.setInt(2, pracId);
					pstmt.setInt(3, dpnId);
					
					if (crrId != GeneralesConstantes.APP_ID_BASE) {
						pstmt.setInt(4, crrId);
					}
					
					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<DocenteJdbcDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoCargaHorariaDocente(rs));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
				} catch (Exception e) {
					e.printStackTrace();
					throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
			
	
	/**
	 * Método que busca docentes de autoevaluación
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @return - retorna la lista de docentes autoevaluados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
		public List<DocenteJdbcDto>  listarXDocentesAutoevaluacionXPeriodo(int pracId) throws DetallePuestoDtoJdbcException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crhr.");sbSql.append(JdbcConstantes.CRHR_NUM_HORAS);
			sbSql.append(" , asev.");sbSql.append(JdbcConstantes.ASEV_CRHR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");sbSql.append(" left join ");
			sbSql.append(JdbcConstantes.TABLA_ASIGNACION_EVALUADOR);sbSql.append(" asev ");sbSql.append(" on ");
			sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ID);sbSql.append(" = ");sbSql.append(" asev.");sbSql.append(JdbcConstantes.ASEV_CRHR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_CONTENIDO);sbSql.append(" cnt ");sbSql.append(" on ");
			sbSql.append(" asev.");sbSql.append(JdbcConstantes.ASEV_ID);sbSql.append(" = ");sbSql.append(" cnt.");sbSql.append(JdbcConstantes.CNTN_ASEV_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY "); 
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 1;  
			pstmt.setInt(contador, CarreraConstantes.TIPO_PREGRADO_VALUE);
			pstmt.setInt(++ contador, pracId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentesEvaluados(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
//		}	
		return retorno;
	}
	
	/**
	 * Método que busca docentes con materia y núero de estudiantes por paralelo
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param dpnId - dpnId id de la facultad seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionado para la busqueda
	 * @return - retorna la lista de docentes con materia y núero de estudiantes por paralelo
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
		public List<DocenteJdbcDto>  listarDocentesMateriaNumMatriculadosXPeriodoXFacultadXCarrera(int pracId, int dpnId, int crrId) throws DocenteDtoException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_TIMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , hrac1.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , ( ");
			
			sbSql.append(" select distinct (select count(mlcrpr2.mlcrpr_id) from malla_curricular_paralelo mlcrpr2, detalle_matricula dtmt2 where mlcrpr2.mlcrpr_id = dtmt2.mlcrpr_id and  mlcrpr2.mlcrpr_id = mlcrpr1.mlcrpr_id) as matriculados"
					+ " from persona prs1 , ficha_estudiante fces1 , ficha_matricula fcmt1 , record_estudiante rces1 , malla_curricular_paralelo mlcrpr1 , paralelo prl1 , carrera crr1"
					+ ", dependencia dpn1 where prs1.prs_id = fces1.prs_id and fces1.fces_id = rces1.fces_id and fces1.fces_id = fcmt1.fces_id and rces1.mlcrpr_id = mlcrpr1.mlcrpr_id"
					+ " and prl1.prl_id = mlcrpr1.prl_id and crr1.crr_id = prl1.crr_id and dpn1.dpn_id = crr1.dpn_id and rces1.rces_estado not in (-1,3,10,12) "
					+ " and mlcrpr1.mlcrpr_id =  mlcrpr.mlcrpr_id ");
			sbSql.append(" and crr1.crr_tipo = 0 and fcmt1.fcmt_estado = 0 ");
			sbSql.append(" and fcmt1.fcmt_prac_id = "+pracId);
			sbSql.append(" and prl1.prac_id = "+pracId);
			sbSql.append("group by mlcrpr1.mlcrpr_id ) as matriculados ");
			
			sbSql.append(" , ( ");	
			
			sbSql.append(" select distinct (select count(mlcrpr2.mlcrpr_id) from malla_curricular_paralelo mlcrpr2, detalle_matricula dtmt2 where mlcrpr2.mlcrpr_id = dtmt2.mlcrpr_id and  mlcrpr2.mlcrpr_id = mlcrpr1.mlcrpr_id) as matriculados"
					+ " from persona prs1 , ficha_estudiante fces1 , ficha_matricula fcmt1, record_estudiante rces1, malla_curricular_paralelo mlcrpr1, paralelo prl1, carrera crr1"
					+ ", dependencia dpn1 where prs1.prs_id = fces1.prs_id and fces1.fces_id = rces1.fces_id and fces1.fces_id = fcmt1.fces_id and rces1.mlcrpr_id = mlcrpr1.mlcrpr_id"
					+ " and prl1.prl_id = mlcrpr1.prl_id and crr1.crr_id = prl1.crr_id and dpn1.dpn_id = crr1.dpn_id and rces1.rces_estado not in (-1,3,10,12)"
					+ " and mlcrpr1.mlcrpr_id =  hrac1.mlcrpr_id "
					+ " and crr1.crr_tipo = 0 and fcmt1.fcmt_estado = 0 ");
			sbSql.append(" and fcmt1.fcmt_prac_id = "+pracId);
			sbSql.append(" and prl1.prac_id = "+pracId);
			sbSql.append(" group by mlcrpr1.mlcrpr_id ) as matriculadoscompartidos ");
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps, ");
			sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr, ");
			sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac, ");
			sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr left join ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac1 on ");
			sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac1.");sbSql.append(JdbcConstantes.MLCRPR_ID_COMP);
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);
			sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = 0 ");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" = 0 ");
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
			if(crrId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			
			sbSql.append(" ORDER BY "); 
			sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contador = 1;  
			pstmt.setInt(contador, pracId);
			pstmt.setInt(++ contador, pracId);
			pstmt.setInt(++ contador, dpnId);
			if(crrId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++ contador, crrId);
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentesNumeroMatriculados(rs));
			}
		} catch (SQLException e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
//			throw new DocenteJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.no.result.exception")));
//		}	
		return retorno;
	}

	/**
	 * Método que busca docentes con el detalle puesto (categoria tiemo de dedicación y carrera)
	 * @param prsId - prsId id de la persona seleccionado para la busqueda
	 * @return - retorna la lista de docentes con materia y núero de estudiantes por paralelo
	 * @throws DocenteDtoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<DocenteJdbcDto>  listarDocentesConDetallePuestoXPersona(int prsId) throws DocenteDtoException {
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" , rllb.");sbSql.append(JdbcConstantes.RLLB_ID);
			sbSql.append(" , rllb.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
			sbSql.append(" , tmdd.");sbSql.append(JdbcConstantes.TMDD_ID);
			sbSql.append(" , tmdd.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_ESTADO);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_PRAC_ID);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_TIPO_CARRERA);
			sbSql.append(" , pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" , pst.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);

			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" pst, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
			sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" rllb, ");
			sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" tmdd, ");
			sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" on ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" AND ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_PST_ID);
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" rllb.");sbSql.append(JdbcConstantes.RLLB_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_RLLB_ID);
			sbSql.append(" AND ");sbSql.append(" tmdd.");sbSql.append(JdbcConstantes.TMDD_ID);
			sbSql.append(" = ");sbSql.append(" pst.");sbSql.append(JdbcConstantes.PST_TMDD_ID);

			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ? ");

			sbSql.append(" ORDER BY "); 
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
			sbSql.append(" , dtps.");sbSql.append(JdbcConstantes.DTPS_PRAC_ID);sbSql.append(" desc");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			int contador = 1;  
			pstmt.setInt(contador, prsId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentesDetallePuesto(rs));
			}
		} catch (SQLException e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
		} catch (Exception e) {
			throw new DocenteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
	
	
	@Override
	public List<DocenteJdbcDto>  listarDocenciaXParalelo(int pracId, int dpnId, int crrId, Usuario usuario, int tipoUsuario ) throws DetallePuestoDtoJdbcException {
	List<DocenteJdbcDto> retorno = null;
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	try {
		StringBuilder sql = new StringBuilder();
		sql.append(" select facultad, carrera,materia,paralelo,count(cedula) as total from (select distinct ");
		sql.append(" dpn.dpn_descripcion facultad, crr.crr_descripcion carrera, mtr.mtr_descripcion materia, prl.prl_descripcion paralelo ");
		sql.append(" , rces.rces_estado, fcmt.fcmt_nivel_ubicacion, prs.prs_identificacion cedula, prs.prs_primer_apellido ");
		sql.append(" , prs.prs_segundo_apellido, prs.prs_nombres, dpn.dpn_id, crr.crr_id, fcmt.fcmt_prac_id ");
		sql.append(" from persona prs ");
		sql.append(" , ficha_estudiante fces, ficha_matricula fcmt, record_estudiante rces, malla_curricular_paralelo mlcrpr ");
		sql.append(" , malla_curricular_materia mlcrmt, materia mtr, paralelo prl, carrera crr ");
		sql.append(" , dependencia dpn, periodo_academico prac, detalle_matricula dtmt, comprobante_pago cmp ");
		sql.append(" where prs.prs_id = fces.prs_id ");
		sql.append(" and fces.fces_id = rces.fces_id ");
		sql.append(" and fces.fces_id = fcmt.fces_id" );
		sql.append(" and rces.mlcrpr_id = mlcrpr.mlcrpr_id ");
		sql.append(" and prl.prl_id = mlcrpr.prl_id ");
		sql.append(" and crr.crr_id = prl.crr_id ");
		sql.append(" and dpn.dpn_id = crr.dpn_id ");
		sql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id ");
		sql.append(" and mlcrmt.mtr_id=mtr.mtr_id ");
		sql.append(" and prl.prac_id= prac.prac_id ");
		sql.append(" and dtmt.mlcrpr_id= mlcrpr.mlcrpr_id ");
		sql.append(" and cmp.fcmt_id=fcmt.fcmt_id ");
		sql.append(" and crr.crr_tipo = 0 ");
		sql.append(" and fcmt.fcmt_estado = 0 ");
		sql.append(" and rces.rces_estado not in(-1) ");
		sql.append(" and prl.prac_id = ? ");
		sql.append(" and fcmt.fcmt_prac_id = ? ");
		sql.append(" and dpn.dpn_id= ? ");
		if (crrId != GeneralesConstantes.APP_ID_BASE) {
			sql.append(" and crr.crr_id= ? ) ");
		}else {
			if(tipoUsuario != RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				sql.append(" and crr.crr_id " );
				sql.append(" in( ");
				sql.append(" Select crr.crr_id from carrera crr,rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");
				sql.append(" where crr.crr_id = roflcr.crr_id AND roflcr.usro_id= usro.usro_id ");
				sql.append(" and usro.usr_id = usr.usr_id ");
				sql.append(" and usr.usr_id = ");
				sql.append(usuario.getUsrId());
				sql.append(" and usro.rol_id = ");
				sql.append(RolConstantes.ROL_DIRCARRERA_VALUE);
				sql.append(")");
				sql.append(")");
			}
		}
		
		sql.append(" group by facultad, carrera,materia,paralelo ");
		sql.append(" order by 1,2,3,4 ");


		
		con = ds.getConnection();
		pstmt = con.prepareStatement(sql.toString());
		
		pstmt.setInt(1, pracId);
		pstmt.setInt(2, pracId);
		pstmt.setInt(3, dpnId);
	
		if (crrId != GeneralesConstantes.APP_ID_BASE) {
			pstmt.setInt(4, crrId);
		}
		
		
		rs = pstmt.executeQuery();
		retorno = new ArrayList<DocenteJdbcDto>();
		while(rs.next()){
			retorno.add(transformarResultSetADtolistarDocenteXParalelo(rs));
		}
	} catch (SQLException e) {
		e.printStackTrace();
		throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.sql.exception")));
	} catch (Exception e) {
		e.printStackTrace();
		throw new DetallePuestoDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.periodo.carrera.nivel.paralelo.docente.exception")));
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
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setCrhrMlcrprId(rs.getInt(JdbcConstantes.CRHR_MLCRPR_ID));
		retorno.setCrhrDtpsId(rs.getInt(JdbcConstantes.CRHR_DTPS_ID));
		retorno.setCrhrId(rs.getInt(JdbcConstantes.CRHR_ID));
		return retorno;
	} 
	
	
	private DocenteJdbcDto transformarResultSetADtoDatos(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
		return retorno;
	} 
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADtoPrActivo(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setCrhrMlcrprId(rs.getInt(JdbcConstantes.CRHR_MLCRPR_ID));
		return retorno;
	} 
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADtoBuscarCarreraDocente(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoBuscarCarreraDocenteComp(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoBuscarCarreraDocenteRep(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		try {
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));	
		} catch (Exception e) {
		}
		
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoBuscarCarreraDocenteReporte(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		try {
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));	
		} catch (Exception e) {
		}
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoBuscarCarreraDocenteRep1(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		return retorno;
	} 
	
	private DocenteJdbcDto transformarResultSetADtoBuscarCarreraDocenteRep2(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
//		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
//		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADtoBuscarNivelesDocente(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		return retorno;
	} 
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADtoMateria(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		return retorno;
	} 
	
	private DocenteJdbcDto transformarResultSetADtoMateriaNotas(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));
		retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));
		retorno.setMlcrmtId(rs.getInt(JdbcConstantes.MLCRMT_ID));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		retorno.setMtrHoras(rs.getInt(JdbcConstantes.MTR_HORAS));
		retorno.setMtrCreditos(rs.getInt(JdbcConstantes.MTR_CREDITOS));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
//		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
//		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		return retorno;
	} 
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADtoParalelo(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		return retorno;
	} 
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private DocenteJdbcDto transformarResultSetADtoBuscarMateriasDocente(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoDocentes(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION DOCENTES **************************** *
	 * ********************************************************************************* */
	

	private DocenteJdbcDto transformarResultSetADtoBuscarDocentesXMateria(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));		
	    retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
	    retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
	    retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
	    retorno.setHracMlcrprId(rs.getInt(JdbcConstantes.HRAC_MLCRPR_ID));
	    retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
	    retorno.setCrrDescripcionComp(rs.getString("carreracompartida"));
	    retorno.setMtrDescripcionComp(rs.getString("materiacompartida"));
	    retorno.setPrlDescripcionComp(rs.getString("paralelocompartido"));
		return retorno;
	}
	
	
	
	private DocenteJdbcDto transformarResultSetADtoDocentesEvaluados(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrhrNumHoras(rs.getInt(JdbcConstantes.CRHR_NUM_HORAS));		
	    retorno.setAsevCrhrId(rs.getInt(JdbcConstantes.ASEV_CRHR_ID));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoDocentesNumeroMatriculados(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));		
	    retorno.setMtrTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
	    retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
	    retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
	    retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.HRAC_MLCRPR_ID));
	    retorno.setMtrId(rs.getInt(JdbcConstantes.DPN_ID));
	    retorno.setMtrId(rs.getInt(JdbcConstantes.CRR_ID));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoCargaHorariaDocente(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
		 retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		 retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		 retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		 retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
	    retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
	    retorno.setCrhrNumHoras(rs.getInt(JdbcConstantes.CRHR_NUM_HORAS));
	    retorno.setCrhrDescripcionSuma(rs.getBigDecimal("ImparticionesClases"));
		return retorno;
	}
	private DocenteJdbcDto transformarResultSetADtoXPeriodoActivoXCarreraXNivelComp(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_CODIGO));
		retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		retorno.setMtrTimtId(rs.getInt(JdbcConstantes.MTR_TIMT_ID));
//		retorno.setMtrSubId(rs.getInt(JdbcConstantes.MTR_SUB_ID));
		retorno.setNvlId(rs.getInt(JdbcConstantes.MLCRMT_NVL_ID));
		retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
		retorno.setHracMlcrprIdComp(rs.getInt(JdbcConstantes.MLCRPR_ID_COMP));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		return retorno;
	}
	
	
	private DocenteJdbcDto transformarResultSetADtolistarDocenteXParalelo(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno=new DocenteJdbcDto();
		
		
		retorno.setDpnDescripcion(rs.getString(1));
		retorno.setCrrDescripcion(rs.getString(2));
		retorno.setMtrDescripcion(rs.getString(3));
		retorno.setPrlDescripcion(rs.getString(4));
		retorno.setNumeroMatriculados(rs.getInt(5));
		return retorno;
	}
	
	private DocenteJdbcDto transformarResultSetADtoDocentesDetallePuesto(ResultSet rs) throws SQLException{
		DocenteJdbcDto retorno = new DocenteJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
		retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setDtpsEstado(rs.getInt(JdbcConstantes.DTPS_ESTADO));
		retorno.setDtpsPracId(rs.getInt(JdbcConstantes.DTPS_PRAC_ID));
		retorno.setDtpsTipoCarrera(rs.getInt(JdbcConstantes.DTPS_TIPO_CARRERA));
		retorno.setPstId(rs.getInt(JdbcConstantes.PST_ID));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		return retorno;
	} 
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Método que busca las carreras del docente por estado del period académico
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param pracEstado - Estado del perído académico enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracEstado(Integer identificacionId , Integer pracEstado) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado =");
			sbSql.append(pracEstado);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado =");
			sbSql.append(pracEstado);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id_comp and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado = ");
			sbSql.append(pracEstado);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente de períodos Activos o en Cierre
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracEstadoActivoCierre(Integer identificacionId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras del docente de períodos Activos o en Cierre
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasEnLineaDocente2018_2019(Integer identificacionId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and crr.crr_id in(225,226)"
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_Id=350");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and crr.crr_id in(225,226)");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracIdMedicinaAnterior(Integer identificacionId, Integer pracId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_prac_id=prac.prac_id");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id =");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_prac_id=prac.prac_id");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras del docente de períodos Activos o en Cierre
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarProgramasDocente(Integer identificacionId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id ");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ "  and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append("  and dtps_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " and crr.crr_tipo=");
			
			sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
//			sbSql.append(" and prac.prac_id not in()");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca las carreras del docente de períodos específicos
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracId(Integer identificacionId , Integer pracId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id= ");
			
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente de períodos específicos
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteInactivoXPracId(Integer identificacionId , Integer pracId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id= ");
			
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteRep(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca los niveles del docente de períodos Activos o en Cierre por Carrera
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - Id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarNivelesDocenteXCarrera(Integer identificacionId, Integer crrId ) throws NivelDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			
			sbSql.append("  UNION "
			+" SELECT DISTINCT  nvl.nvl_id , nvl.nvl_descripcion from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_estado in (0,2) and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0" 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (0,2) and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			
			sbSql.append("  UNION ");
					sbSql.append(" SELECT DISTINCT ");
					sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
					sbSql.append(" from  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
							+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
							+ " WHERE "
							+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_tipo in (1)");
					sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
							+ "  and usr_id = "); 
					sbSql.append(identificacionId);
					sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
					sbSql.append("and crhr_estado=0 "
							+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
							+ " ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new NivelDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	@Override
	public List<DocenteJdbcDto> buscarNivelesDocenteEnLineaX350(Integer identificacionId, Integer crrId ) throws NivelDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350 ");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			
			sbSql.append("  UNION "
			+" SELECT DISTINCT  nvl.nvl_id , nvl.nvl_descripcion from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_id=350 and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 " 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350 and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			
			sbSql.append("  UNION ");
					sbSql.append(" SELECT DISTINCT ");
					sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
					sbSql.append(" from  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
							+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
							+ " WHERE "
							+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_tipo in (1)");
					sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
							+ "  and usr_id = "); 
					sbSql.append(identificacionId);
					sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
					sbSql.append("   and crhr_estado=0 "
							+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
							+ " ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new NivelDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	
	@Override
	public List<DocenteJdbcDto> buscarNivelesDocenteInactivoXCarrera(Integer identificacionId, Integer crrId ) throws NivelDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(")");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			
			sbSql.append("  UNION "
			+" SELECT DISTINCT  nvl.nvl_id , nvl.nvl_descripcion from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_estado in (1) and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1" 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (1) and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			
			sbSql.append("  UNION ");
					sbSql.append(" SELECT DISTINCT ");
					sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
					sbSql.append(" from  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
							+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
							+ " WHERE "
							+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_tipo in (1)");
					sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
							+ "  and usr_id = "); 
					sbSql.append(identificacionId);
					sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
					sbSql.append("  and dtps_estado=1 and crhr_estado=0 "
							+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
							+ " ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new NivelDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca los niveles del docente de períodos específicos
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - Id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarNivelesDocenteXCarreraXPracId(Integer identificacionId, Integer crrId, Integer pracId ) throws NivelDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			
			sbSql.append("  UNION "
			+" SELECT DISTINCT  nvl.nvl_id , nvl.nvl_descripcion from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId); 
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0" 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new NivelDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	
	/**
	 * Método que busca los niveles del docente de períodos específicos
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - Id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarNivelesDocenteInactivoXCarreraXPracId(Integer identificacionId, Integer crrId, Integer pracId ) throws NivelDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			
			sbSql.append("  UNION "
			+" SELECT DISTINCT  nvl.nvl_id , nvl.nvl_descripcion from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId); 
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 " 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarNivelesDocente(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new NivelDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca masterias del docente de períodos Activos o en Cierre por Carrera y por nivel
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - Id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarMateriasDocenteXCarreraXNivel(Integer identificacionId, Integer crrId , Integer nvlId) throws MateriaDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");
			sbSql.append(nvlId);
			sbSql.append(" UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);
			sbSql.append(" and nvl.nvl_id =");
			sbSql.append(nvlId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			sbSql.append("  from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_estado in (0,2) and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 " 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (0,2) and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); 
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MTR_HORAS);sbSql.append(", ");
			sbSql.append(" CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); 
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_tipo in (1)");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ "  and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");sbSql.append(nvlId);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaNotas(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new MateriaDtoNoEncontradoException();
		}	

		return retorno;
	}
	@Override
	public List<DocenteJdbcDto> buscarMateriasDocenteXCarreraXNivelEnLineaX350(Integer identificacionId, Integer crrId , Integer nvlId) throws MateriaDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");
			sbSql.append(nvlId);
			sbSql.append(" UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0  and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);
			sbSql.append(" and nvl.nvl_id =");
			sbSql.append(nvlId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			sbSql.append("  from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_id=350 and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 " 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=350 and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); 
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MTR_HORAS);sbSql.append(", ");
			sbSql.append(" CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); 
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_tipo in (1)");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ "  and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append("  and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");sbSql.append(nvlId);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaNotas(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new MateriaDtoNoEncontradoException();
		}	

		return retorno;
	}
	
	@Override
	public List<DocenteJdbcDto> buscarMateriasDocenteInactivoXCarreraXNivel(Integer identificacionId, Integer crrId , Integer nvlId) throws MateriaDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");
			sbSql.append(nvlId);
			sbSql.append(" UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);
			sbSql.append(" and nvl.nvl_id =");
			sbSql.append(nvlId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);sbSql.append(")");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			sbSql.append("  from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_estado in (1) and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=1" 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (1) and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_HORAS); 
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MTR_HORAS);sbSql.append(", ");
			sbSql.append(" CASE WHEN mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS); 
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM  malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_tipo in (1)");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ "  and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append("  and dtps_estado=1 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");sbSql.append(nvlId);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaNotas(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new MateriaDtoNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca masterias del docente de períodos Activos o en Cierre por Carrera y por nivel
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - Id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarMateriasDocenteXCarreraXNivelXPracId(Integer identificacionId, Integer crrId , Integer nvlId , Integer pracId) throws MateriaDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");
			sbSql.append(nvlId);
			sbSql.append(" UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);
			sbSql.append(" and nvl.nvl_id =");
			sbSql.append(nvlId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			sbSql.append("  from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0" 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaNotas(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new MateriaDtoNoEncontradoException();
		}	

		return retorno;
	}
	
	
	@Override
	public List<DocenteJdbcDto> buscarMateriasDocenteInactivoXCarreraXNivelXPracId(Integer identificacionId, Integer crrId , Integer nvlId , Integer pracId) throws MateriaDtoNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append(" and hrac_estado=0 and   crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and nvl.nvl_id =");
			sbSql.append(nvlId);
			sbSql.append(" UNION");
				
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and   crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id"
					+ " and crr.crr_id=");
			sbSql.append(crrId);
			sbSql.append(" and nvl.nvl_id =");
			sbSql.append(nvlId);sbSql.append(")"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id");
			sbSql.append(" and crr.crr_id =");sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			
			sbSql.append("  UNION ");
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_ID);
			sbSql.append(" , nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_HORAS);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_CREDITOS);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			sbSql.append("  from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr, nivel nvl where mlcrpr_id_comp in" 

			+" (SELECT DISTINCT  mlcrpr.mlcrpr_id from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,"
			+" carga_horaria crhr, periodo_academico prac  , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr," 
			+" malla_curricular_materia mlcrmt, carrera crr, nivel nvl WHERE  mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id" 
			+" and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and" 
			+" fcdc.prs_id=usr.prs_id and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 " 
			+" and crhr_estado=0"  
			+" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id)"
			+" and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_id=");
			sbSql.append(pracId);
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id and nvl.nvl_id=mlcrmt.nvl_id and crr.crr_id =");
			sbSql.append(crrId);
			sbSql.append( " and nvl.nvl_id =");
			sbSql.append(nvlId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoMateriaNotas(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new MateriaDtoNoEncontradoException();
		}	

		return retorno;
	}
	
	/**
	 * Método que busca las carreras del docente de períodos Activos o en Cierre
	 * @author Daniel
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	@Override
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracEstadoActivoCierreParaReporte(Integer identificacionId ) throws CarreraDtoJdbcNoEncontradoException{
		List<DocenteJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = "); 
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0 "
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id "
					+ " UNION");
			
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);
			sbSql.append(" , prl.");sbSql.append(JdbcConstantes.PRL_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" , mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
			sbSql.append(" , mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(" from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl, periodo_academico prac, materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " where mlcrpr_id_comp in (select distinct mlcrpr.mlcrpr_id"
					+ " from horario_academico hrac , malla_curricular_paralelo mlcrpr, paralelo prl ,carga_horaria crhr, periodo_academico prac "
					+ " , detalle_puesto dtps, ficha_docente fcdc, usuario usr , materia mtr, malla_curricular_materia mlcrmt, carrera crr"
					+ " WHERE "
					+ " mlcrpr.mlcrpr_id = crhr.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and  prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and dtps.dtps_id=crhr.dtps_id and dtps.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=usr.prs_id"
					+ " and mlcrpr.mlcrpr_id=hrac.mlcrpr_id and usr_id = ");
			sbSql.append(identificacionId);
			sbSql.append(" and hrac_estado=0 and dtps_estado=0 and crhr_estado=0"
					+ " and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id)"
					+ " and mlcrpr.mlcrpr_id = hrac.mlcrpr_id and prl.prl_id=mlcrpr.prl_id and prl.prac_id=prac.prac_id and prac.prac_estado in (");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);sbSql.append(",");
			sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbSql.append(")");
			sbSql.append(" and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id and mlcrmt.mtr_id=mtr.mtr_id and mtr.crr_id=crr.crr_id");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarCarreraDocenteReporte(rs));
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
		if(retorno == null || retorno.size()<=0){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	

		return retorno;
	}
	
	
	
	
	
}
