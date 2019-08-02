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
   
 ARCHIVO:     PersonaDatosDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc del DTO PersonaDatos.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
29-08-2017            Arturo Villafuerte                    Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
/**
 * EJB PersonaDatosDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc del DTO PersonaDatos.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class ContenidoEvaluacionDtoServicioJdbcImpl implements ContenidoEvaluacionDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	/**
	 * Enlista los contenidos de la evaluacion que se ha generado para el docente 
	 * @param idTipoEvaluacion - Id del tipo de evaluacion a buscar
	 * @param idEvaluacion - Id de la evaluacion a buscar
	 * @param idCrhr - Id de la carga horaria a buscar
	 * @param idEvaluador - Id usro del evaluador a buscar
	 * @param idEvaluado - Id usro del evaluado a buscar 
	 * @param idCarrera - Id de la carrera a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<ContenidoEvaluacionDto> listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(int idTipoEvaluacion, int idEvaluacion, int idCrhr, int idEvaluador, int idEvaluado, int idCarrera) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException{
		
		
		List<ContenidoEvaluacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
			sbSql.append("    SELECT DISTINCT ");
			
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.CNTN_ASEV_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_DESCRIPCION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_FECHA);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_SELECCION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_USUARIO);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_EVA_ID);
					
					sbSql.append(" , ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);
					
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_DESCRIPCION);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ESTADO);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_FECHA); 
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUM_MAX);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_OBLIGATORIEDAD);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_COMPONENTE); 
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_SELECCION);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_USUARIO);
					 
					sbSql.append(" FROM ");
				 
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUADOR_EVALUADO);sbSql.append(" EVEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_EVALUADOR);sbSql.append(" ASEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONTENIDO);sbSql.append(" CNT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUACION);sbSql.append(" EVL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TPCN_FUNCION_TPEV);sbSql.append(" TPCNFNTPEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_CONTENIDO);sbSql.append(" TPCN");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" =");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" AND"); 
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" =");
					sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" =");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					
					sbSql.append(" AND ");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ESTADO);
					sbSql.append("  NOT IN ( "+ContenidoConstantes.ESTADO_INACTIVO_VALUE+" ) ");
					
//				 	BUSCAME
//					sbSql.append(" AND ");
//					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.CNTN_ESTADO);
//					sbSql.append(" not in ( "+ContenidoConstantes.ESTADO_INACTIVO_VALUE+" ) ");
					
					if(idTipoEvaluacion != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");
						sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
						sbSql.append(" =");
						sbSql.append(" ?");

						sbSql.append(" AND");
						sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_EVALUADOR);
						sbSql.append(" =");
						sbSql.append(" ?");
					}
					
					sbSql.append(" AND");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" ?");
					
					//ESTUDIANTE
					if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_ESTUDIANTE_VALUE && idCrhr != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND");
						sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.CRHR_ID);
						sbSql.append(" =");
						sbSql.append(" ?");
					}
					
					//DIRECTIVO
					if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE && idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND");
						sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);
						sbSql.append(" =");
						sbSql.append(" ?"); 
					}
					 
					
					//PAR -> FGGUZMAN
					if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_VALUE && idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND");
						sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);
						sbSql.append(" =");
						sbSql.append(" ?"); 
					}
					
					
					sbSql.append(" ORDER BY TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
			 
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					if(idTipoEvaluacion != GeneralesConstantes.APP_ID_BASE){
						pstmt.setInt(1, idEvaluacion); 
						pstmt.setInt(2, idEvaluador); 
						pstmt.setInt(3, idEvaluado); 
					} else {  
						pstmt.setInt(1, idEvaluado); 
					} 
					
					//ESTUDIANTE
					if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_ESTUDIANTE_VALUE && idCrhr != GeneralesConstantes.APP_ID_BASE){
						pstmt.setInt(4, idCrhr); 
					}
					
					//DIRECTIVO
					if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE && idCarrera != GeneralesConstantes.APP_ID_BASE){
						pstmt.setInt(4, idCarrera); 
					}
					
					//PAR -> FGGUZMAN
					if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_VALUE && idCarrera != GeneralesConstantes.APP_ID_BASE){
						pstmt.setInt(4, idCarrera); 
					}

					rs = pstmt.executeQuery();
					retorno = new ArrayList<ContenidoEvaluacionDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.sql.exception")));
		} catch (Exception e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.exception")));
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
			throw new ContenidoEvaluacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Enlista los contenidos de la evaluacion que se ha generado para el docente 
	 * @param idAsignacionEvaluador - Id del asignacionEvaluador a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<ContenidoEvaluacionDto> listarXAsignacionEvaluador(int idAsignacionEvaluador) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException{
		
		
		List<ContenidoEvaluacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
			sbSql.append("    SELECT DISTINCT ");
			
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.CNTN_ASEV_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_DESCRIPCION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_FECHA);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_SELECCION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_USUARIO);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_EVA_ID);
					
					sbSql.append(" , ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);
					
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_DESCRIPCION);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ESTADO);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_FECHA); 
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUM_MAX);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_OBLIGATORIEDAD);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_COMPONENTE); 
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_SELECCION);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_USUARIO);
					 
					sbSql.append(" FROM ");
				 
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUADOR_EVALUADO);sbSql.append(" EVEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_EVALUADOR);sbSql.append(" ASEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONTENIDO);sbSql.append(" CNT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUACION);sbSql.append(" EVL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TPCN_FUNCION_TPEV);sbSql.append(" TPCNFNTPEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_CONTENIDO);sbSql.append(" TPCN");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" =");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" AND"); 
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" =");
					sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" =");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
				 
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" =");
					sbSql.append(" ?");
					 
					sbSql.append(" ORDER BY TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
			 
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
  
					pstmt.setInt(1, idAsignacionEvaluador); 					

					rs = pstmt.executeQuery();
					retorno = new ArrayList<ContenidoEvaluacionDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) {  
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.asiganacionevaluador.sql.exception")));
		} catch (Exception e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.asiganacionevaluador.exception")));
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
			throw new ContenidoEvaluacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.asiganacionevaluador.no.result.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Enlista los contenidos de la evaluacion que se ha generado para el docente 
	 * @param idEvaluado - Id usro del evaluado a buscar 
	 * @param idPrac - Id de periodo academico a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<ContenidoEvaluacionDto> listarEvaluacionIdXEvaluadoXPeriodo(int idEvaluado, int idPrac) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException{
		
		
		List<ContenidoEvaluacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
			sbSql.append("    SELECT DISTINCT ");
			 
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.CNTN_EVA_ID);
					sbSql.append(" , ASEV.");sbSql.append(JdbcConstantes.ASEV_ID); 
					sbSql.append(" , ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);
					 
					sbSql.append(" FROM ");
				 
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUADOR_EVALUADO);sbSql.append(" EVEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_EVALUADOR);sbSql.append(" ASEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONTENIDO);sbSql.append(" CNT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUACION);sbSql.append(" EVL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TPCN_FUNCION_TPEV);sbSql.append(" TPCNFNTPEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_CONTENIDO);sbSql.append(" TPCN");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" =");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" AND"); 
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" =");
					sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" =");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					 
					sbSql.append(" AND");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" ?");
					
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ESTADO);
					sbSql.append(" NOT IN ");
					sbSql.append(" ( 1 )");
					
					if(idPrac != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND");
						sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_PRAC_ID);
						sbSql.append(" =");
						sbSql.append(" ?");
					}
				   
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
  
					pstmt.setInt(1, idEvaluado); 
					
					if(idPrac != GeneralesConstantes.APP_ID_BASE){
						pstmt.setInt(2, idPrac);
					}

					rs = pstmt.executeQuery();
					retorno = new ArrayList<ContenidoEvaluacionDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoEvaluacion(rs));
					}

			
			
		} catch (SQLException e) {  
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.evaluacion.id.x.evaluado.x.periodo.sql.exception")));
		} catch (Exception e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.evaluacion.id.x.evaluado.x.periodo.exception")));
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
			throw new ContenidoEvaluacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.evaluacion.id.x.evaluado.x.periodo.no.result.exception")));
		}
		return retorno;
	}
	
   
	public Integer buscarEvaluacionDocente(String prsIdentificacion, int tipoEvaluacion ) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException{


		Integer retorno = new Integer(0);
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		List<ContenidoEvaluacionDto> evaluacion = new ArrayList<>();

		StringBuilder sbSql = new StringBuilder(); 
		sbSql.append("    SELECT DISTINCT ");

		sbSql.append(" CNT.");sbSql.append(JdbcConstantes.CNTN_ASEV_ID);
		sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_DESCRIPCION);
		sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_FECHA);
		sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_ID);
		sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_SELECCION);
		sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_USUARIO);
		sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_EVA_ID);

		sbSql.append(" , ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);

		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_DESCRIPCION);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ESTADO);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_FECHA); 
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUM_MAX);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_OBLIGATORIEDAD);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_COMPONENTE); 
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_SELECCION);
		sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_USUARIO);

		sbSql.append(" FROM ");

		sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUADOR_EVALUADO);sbSql.append(" EVEV ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_EVALUADOR);sbSql.append(" ASEV ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONTENIDO);sbSql.append(" CNT ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUACION);sbSql.append(" EVL ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TPCN_FUNCION_TPEV);sbSql.append(" TPCNFNTPEV ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_CONTENIDO);sbSql.append(" TPCN");

		sbSql.append(" WHERE");sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" =");sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
		sbSql.append(" AND");sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" =");sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
		sbSql.append(" AND");sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_ID);sbSql.append(" =");sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.EVEV_ID);
		sbSql.append(" AND");sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ID);sbSql.append(" =");sbSql.append(" CNT.");sbSql.append(JdbcConstantes.ASEV_ID);
		sbSql.append(" AND");sbSql.append(" CNT.");sbSql.append(JdbcConstantes.EVL_EVA_ID);sbSql.append(" =");sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
		sbSql.append(" AND");sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);sbSql.append(" =");sbSql.append(" CNT.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);				
		sbSql.append(" AND");sbSql.append(" TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);sbSql.append(" =");sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);

		sbSql.append(" AND ");sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ESTADO);sbSql.append("  NOT IN ( "+ContenidoConstantes.ESTADO_INACTIVO_VALUE+" ) ");
		sbSql.append(" AND ");sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);sbSql.append(" =");sbSql.append(" ?");
		sbSql.append(" AND");sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" =");sbSql.append(" ?");

		sbSql.append(" ORDER BY TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoEvaluacion); 
			pstmt.setString(2, prsIdentificacion); 
			
			rs = pstmt.executeQuery();

			while(rs.next()){
				evaluacion.add(transformarResultSetADto(rs));
			}

			if (evaluacion.size() > 0) {
				retorno = 1;
			}
		} catch (SQLException e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.sql.exception")));
		} catch (Exception e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.exception")));
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
	 * Metodo que devuelve la evaluacion del docente para la apelacion
	 * @param idPeriodo
	 * @param idTipoEvaluacion
	 * @param evaluado
	 * @param evaluador
	 * @return
	 * @throws ContenidoEvaluacionDtoNoEncontradoException
	 * @throws ContenidoEvaluacionDtoException
	 */
	
public List<ContenidoEvaluacionDto> listarXPeriodoXTipoEvaluacionXEvaluadorXEvaluado(int idPeriodo, int idTipoEvaluacion, int usroIdEvaluador,int usroIdEvaluado,int usrId) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException{
		
		
		List<ContenidoEvaluacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
			sbSql.append("    SELECT DISTINCT ");
			
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.CNTN_ASEV_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_DESCRIPCION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_FECHA);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_SELECCION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_USUARIO);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_EVA_ID);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_SELECCION_INICIAL);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_OFICIO_APELACION);
					sbSql.append(" , CNT.");sbSql.append(JdbcConstantes.CNTN_REGISTRO_APELACION);
					
					sbSql.append(" , ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);
					
					
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_DESCRIPCION);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ESTADO);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_FECHA); 
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUM_MAX);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_OBLIGATORIEDAD);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_COMPONENTE); 
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_TIPO_SELECCION);
					sbSql.append(" , TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_USUARIO);
					 
					sbSql.append(" FROM ");
				 
					sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUADOR_EVALUADO);sbSql.append(" EVEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_EVALUADOR);sbSql.append(" ASEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONTENIDO);sbSql.append(" CNT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_CONTENIDO);sbSql.append(" TPCN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EVALUACION);sbSql.append(" EVL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIPO_EVALUACION);sbSql.append(" TIEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TPCN_FUNCION_TPEV);sbSql.append(" TPCNFNTPEV ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" PRAC");
					
					
					sbSql.append(" WHERE");
					
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" =");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.EVEV_ID);
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.ASEV_ID);
					sbSql.append(" AND"); 
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" =");
					sbSql.append(" EVL.");sbSql.append(JdbcConstantes.EVL_EVA_ID);
					sbSql.append(" AND");
					sbSql.append(" TIEV.");sbSql.append(JdbcConstantes.TIEV_TPEV_ID);
					sbSql.append(" =");
					sbSql.append(" EVL.");sbSql.append(JdbcConstantes.TIEV_TPEV_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" =");
					sbSql.append(" CNT.");sbSql.append(JdbcConstantes.TPFNTP_TPCNFNTPEV_ID);
					sbSql.append(" AND");
					sbSql.append(" TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" =");
					sbSql.append(" TPCNFNTPEV.");sbSql.append(JdbcConstantes.TICN_TPCN_ID);
					sbSql.append(" AND");
					sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_ESTADO);
					sbSql.append("  NOT IN ( "+ContenidoConstantes.ESTADO_INACTIVO_VALUE+" ) ");		
					sbSql.append(" AND ");sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" =");sbSql.append(" ?");
					sbSql.append(" AND");sbSql.append(" TIEV.");sbSql.append(JdbcConstantes.TIEV_TPEV_ID);sbSql.append(" =");sbSql.append(" ?");
					sbSql.append(" AND");sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_EVALUADOR);sbSql.append(" = ? ");
					sbSql.append(" AND");sbSql.append(" EVEV.");sbSql.append(JdbcConstantes.EVEV_USRO_ID);sbSql.append(" = ? ");
					//sbSql.append(" AND");sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
					
					//sbSql.append(" SELECT ");sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_NICK);
					//sbSql.append(" FROM ");
					//sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS");
					//sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					//sbSql.append(" WHERE");
					//sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					//sbSql.append(" =");
					//sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					//sbSql.append(" AND");sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					//sbSql.append(" = ? ");sbSql.append(" )");
//para controlar el acceso 

					if(idTipoEvaluacion==TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE){
					sbSql.append(" AND");sbSql.append(" ASEV.");sbSql.append(JdbcConstantes.ASEV_EVALUADOR_CRR_ID);sbSql.append(" IN (");
					sbSql.append(" SELECT ");sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" FROM ");
					sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" ROFLCR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" WHERE");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");sbSql.append(RolConstantes.ROL_DIRCARRERA_VALUE);
					sbSql.append(" AND");sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
					sbSql.append(" AND");sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
					sbSql.append(" =");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" )");
					}
					sbSql.append(" ORDER BY TPCN.");sbSql.append(JdbcConstantes.TICN_TPCN_NUMERAL);
			 
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
						pstmt.setInt(1, idPeriodo); 
						pstmt.setInt(2, idTipoEvaluacion); 
						pstmt.setInt(3, usroIdEvaluador); 
						pstmt.setInt(4, usroIdEvaluado); 
						if(idTipoEvaluacion==TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE){
							pstmt.setInt(5, usrId);
						}
						
					rs = pstmt.executeQuery();
					retorno = new ArrayList<ContenidoEvaluacionDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoApelacion(rs));
					}
			
			
			
		} catch (SQLException e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.sql.exception")));
		} catch (Exception e) { 
			throw new ContenidoEvaluacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.x.tipoevaluacion.x.crhr.evaluador.evaluado.carrera.evaluacion.exception")));
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
			throw new ContenidoEvaluacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "ContenidoEvaluacionDto.listar.evaluacion.x.periodo.tpev.evaluado.evaluador.no.encontrado.exception")));
		}
		return retorno;
	}
	
    
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	/**
	 * Metodo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parametros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private ContenidoEvaluacionDto transformarResultSetADtoEvaluacion(ResultSet rs) throws SQLException{
		ContenidoEvaluacionDto retorno = new ContenidoEvaluacionDto(); 
		retorno.setEvaId(rs.getInt(JdbcConstantes.CNTN_EVA_ID)); 
		
		retorno.setAsevId(rs.getInt(JdbcConstantes.ASEV_ID));
		retorno.setAsevCrr(rs.getInt(JdbcConstantes.ASEV_EVALUADOR_CRR_ID));
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	/**
	 * Metodo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parametros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private ContenidoEvaluacionDto transformarResultSetADto(ResultSet rs) throws SQLException{
		ContenidoEvaluacionDto retorno = new ContenidoEvaluacionDto();
		
		retorno.setAsevId(rs.getInt(JdbcConstantes.CNTN_ASEV_ID));
		retorno.setCntDescripcion(rs.getString(JdbcConstantes.CNTN_DESCRIPCION));
		retorno.setCntFecha(rs.getTimestamp(JdbcConstantes.CNTN_FECHA));
		retorno.setCntId(rs.getInt(JdbcConstantes.CNTN_ID));
		retorno.setCntSeleccion(rs.getInt(JdbcConstantes.CNTN_SELECCION));
		retorno.setCntUsuario(rs.getString(JdbcConstantes.CNTN_USUARIO));
		retorno.setEvaId(rs.getInt(JdbcConstantes.CNTN_EVA_ID));
		
		
		retorno.setAsevCrr(rs.getInt(JdbcConstantes.ASEV_EVALUADOR_CRR_ID));
		  
		retorno.setTpcnDescripcion(rs.getString(JdbcConstantes.TICN_TPCN_DESCRIPCION));
		retorno.setTpcnEstado(rs.getInt(JdbcConstantes.TICN_TPCN_ESTADO));
		retorno.setTpcnFecha(rs.getTimestamp(JdbcConstantes.TICN_TPCN_FECHA));
		retorno.setTpcnId(rs.getInt(JdbcConstantes.TICN_TPCN_ID));
		retorno.setTpcnNumeral(rs.getInt(JdbcConstantes.TICN_TPCN_NUMERAL));
		retorno.setTpcnNumMax(rs.getInt(JdbcConstantes.TICN_TPCN_NUM_MAX));
		retorno.setTpcnObligatoriedad(rs.getInt(JdbcConstantes.TICN_TPCN_OBLIGATORIEDAD));
		retorno.setTpcnTipo(rs.getInt(JdbcConstantes.TICN_TPCN_TIPO));
		retorno.setTpcnTipoComponente(rs.getInt(JdbcConstantes.TICN_TPCN_TIPO_COMPONENTE)); 
		retorno.setTpcnTipoSeleccion(rs.getInt(JdbcConstantes.TICN_TPCN_TIPO_SELECCION));
		retorno.setTpcnUsuario(rs.getString(JdbcConstantes.TICN_TPCN_USUARIO));
		   
		return retorno;
	}

	/**
	 * Metodo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parametros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private ContenidoEvaluacionDto transformarResultSetADtoApelacion(ResultSet rs) throws SQLException{
		ContenidoEvaluacionDto retorno = new ContenidoEvaluacionDto();
		
		retorno.setAsevId(rs.getInt(JdbcConstantes.CNTN_ASEV_ID));
		retorno.setCntDescripcion(rs.getString(JdbcConstantes.CNTN_DESCRIPCION));
		retorno.setCntFecha(rs.getTimestamp(JdbcConstantes.CNTN_FECHA));
		retorno.setCntId(rs.getInt(JdbcConstantes.CNTN_ID));
		retorno.setCntSeleccion(rs.getInt(JdbcConstantes.CNTN_SELECCION));
		retorno.setCntUsuario(rs.getString(JdbcConstantes.CNTN_USUARIO));
		retorno.setEvaId(rs.getInt(JdbcConstantes.CNTN_EVA_ID));
		retorno.setCntOficioApelacion(rs.getString(JdbcConstantes.CNTN_OFICIO_APELACION));
		retorno.setCntSeleccionInicial(rs.getInt(JdbcConstantes.CNTN_SELECCION_INICIAL));
		retorno.setCntRegistroApelacion(rs.getTimestamp(JdbcConstantes.CNTN_REGISTRO_APELACION));
		
		retorno.setAsevCrr(rs.getInt(JdbcConstantes.ASEV_EVALUADOR_CRR_ID));
		  
		retorno.setTpcnDescripcion(rs.getString(JdbcConstantes.TICN_TPCN_DESCRIPCION));
		retorno.setTpcnEstado(rs.getInt(JdbcConstantes.TICN_TPCN_ESTADO));
		retorno.setTpcnFecha(rs.getTimestamp(JdbcConstantes.TICN_TPCN_FECHA));
		retorno.setTpcnId(rs.getInt(JdbcConstantes.TICN_TPCN_ID));
		retorno.setTpcnNumeral(rs.getInt(JdbcConstantes.TICN_TPCN_NUMERAL));
		retorno.setTpcnNumMax(rs.getInt(JdbcConstantes.TICN_TPCN_NUM_MAX));
		retorno.setTpcnObligatoriedad(rs.getInt(JdbcConstantes.TICN_TPCN_OBLIGATORIEDAD));
		retorno.setTpcnTipo(rs.getInt(JdbcConstantes.TICN_TPCN_TIPO));
		retorno.setTpcnTipoComponente(rs.getInt(JdbcConstantes.TICN_TPCN_TIPO_COMPONENTE)); 
		retorno.setTpcnTipoSeleccion(rs.getInt(JdbcConstantes.TICN_TPCN_TIPO_SELECCION));
		retorno.setTpcnUsuario(rs.getString(JdbcConstantes.TICN_TPCN_USUARIO));
		   
		return retorno;
	}
	

}

