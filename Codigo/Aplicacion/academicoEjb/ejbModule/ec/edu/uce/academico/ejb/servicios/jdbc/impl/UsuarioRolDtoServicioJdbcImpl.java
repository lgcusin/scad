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
   
 ARCHIVO:     UsuarioRolDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla usuario_rol.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-FEBRERO-2016		Daniel Albuja					       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB UsuarioRolDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla ubicacion.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class UsuarioRolDtoServicioJdbcImpl implements UsuarioRolDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	@Override
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarrea(
			String identificacion,  int rolId, String primerApellido)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
//			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
//			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
//			//**** CAMPOS DE ROL ****/
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			

			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append( " = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" and usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append( " = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			if(identificacion!=null){
				sbSql.append( " AND " );
				//identificacion
				sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%'");
			}
			
			if(primerApellido!=null){
				sbSql.append(" AND UPPER(prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(") like  '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(primerApellido.toUpperCase())+"%'");
			}
			
			if(rolId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
				sbSql.append(rolId);
			}
			
//			if(rolId == GeneralesConstantes.APP_ID_BASE.intValue() || rolId == 0){
//				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" > ");
////				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
////				sbSql.append(" not in (");
////				sbSql.append(RolConstantes.ROL_SOPORTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRRRHH_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_ACTUALIZACIONDOCENTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDGESTION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_PRESCOMITEETICA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
////				sbSql.append(" ) ");
//			}else{
//				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
//			}
//			sbSql.append(rolId);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,  prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);

//			sbSql.append(" )  ORDER BY  ");
//			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	public List<UsuarioRolJdbcDto> buscarXIdentificacion(String identificacion)throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
//			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
			sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			

			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append( " = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" and usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append( " = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" and rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append( " = usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			if(identificacion!=null){
				sbSql.append( " AND " );
				//identificacion
				sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%'");
			}
			
//			if(rolId == GeneralesConstantes.APP_ID_BASE.intValue() || rolId == 0){
//				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" > ");
////				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
////				sbSql.append(" not in (");
////				sbSql.append(RolConstantes.ROL_SOPORTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRRRHH_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_ACTUALIZACIONDOCENTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDGESTION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_PRESCOMITEETICA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
////				sbSql.append(" ) ");
//			}else{
//				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
//			}
//			sbSql.append(rolId);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,  prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);

//			sbSql.append(" )  ORDER BY  ");
//			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoIdentificacion(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarreaAlterno(String identificacion,  int rolId, String primerApellido) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
//			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
//			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
//			//**** CAMPOS DE ROL ****/
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
//			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			

			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append( " = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
//			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append( " = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			if(identificacion!=null){
				sbSql.append( " AND " );
				//identificacion
				sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%'");
			}
			
			if(primerApellido!=null){
				sbSql.append(" AND UPPER(prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(") like  '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(primerApellido.toUpperCase())+"%'");
			}
			
//			if(rolId != GeneralesConstantes.APP_ID_BASE){
//				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
//				sbSql.append(rolId);
//			}
			
//			if(rolId == GeneralesConstantes.APP_ID_BASE.intValue() || rolId == 0){
//				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" > ");
////				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
////				sbSql.append(" not in (");
////				sbSql.append(RolConstantes.ROL_SOPORTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRRRHH_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_ACTUALIZACIONDOCENTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDGESTION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_PRESCOMITEETICA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
////				sbSql.append(" ) ");
//			}else{
//				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
//			}
//			sbSql.append(rolId);
			
			sbSql.append(" GROUP BY ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,  prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);

//			sbSql.append(" )  ORDER BY  ");
//			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}

	@Override
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarreaConRol(
			String identificacion,  int rolId, String primerApellido)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
//			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
//			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
//			//**** CAMPOS DE ROL ****/
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
//			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
//			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
			sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			

			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append( " = usr." );sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append( " AND " );sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append( " = usro." );sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append( " AND " );sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append( " = rol." );sbSql.append(JdbcConstantes.ROL_ID);
			if(identificacion!=null){
				sbSql.append( " AND " );
				//identificacion
				sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%'");
			}
			
			if(primerApellido!=null){
				sbSql.append(" AND UPPER(prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(") like  '");
				sbSql.append("%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(primerApellido.toUpperCase())+"%'");
			}
			if(rolId == GeneralesConstantes.APP_ID_BASE.intValue() || rolId == 0){
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" > ");
////				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
////				sbSql.append(" not in (");
////				sbSql.append(RolConstantes.ROL_SOPORTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRRRHH_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_ACTUALIZACIONDOCENTE_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDGESTION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_COORDINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_DIRINVESTIGACION_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_PRESCOMITEETICA_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);sbSql.append(",");
////				sbSql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
////				sbSql.append(" ) ");
			}else{
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
			}
			sbSql.append(rolId);
			
			sbSql.append(" GROUP BY ");
			
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,  prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);

//			System.out.println(sbSql);
//			sbSql.append(" )  ORDER BY  ");
//			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraXRol(
			int rolId, int facultadId, int carreraId)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
//			sbSql.append("crrfcl");sbSql.append(",");
//			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(",");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" WHERE ");
			
				sbSql.append("  rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" = ?");
			
			//facultad
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" ? ");
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, rolId); 
			pstmt.setInt(2, facultadId);
			pstmt.setInt(3, carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNuevoUsuario(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UsuarioRolJdbcDto buscarUsuarioXCarreraXRol(int rolId, int carreraId) throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException {
		UsuarioRolJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);sbSql.append(",");
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
//			sbSql.append("crrfcl");sbSql.append(",");
//			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(",");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_NICK);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM  ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" WHERE ");
			sbSql.append("  rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, rolId); 
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoUsuario(rs);
			}else{
				retorno = null;
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
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
	
	}
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraTodas(String identificacion ,
			int rolId, int facultadId, int carreraId)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
//			sbSql.append("crrfcl");sbSql.append(",");
//			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(",");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr  ");
						
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl  ");
			

			sbSql.append(" WHERE ");
//			
				sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);
//			//facultad
//				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" ? ");
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
//			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  "); //sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNuevoUsuario(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraXRolXEstado(int facultadId, int carreraId, int postgradoId, int rolId, int estadoRoflcr) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
//			sbSql.append("crrfcl");sbSql.append(",");
//			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(",");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_NICK);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DETALLE);sbSql.append(",");
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM  ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl  ");

			sbSql.append(" WHERE ");
//			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);
			
			if(postgradoId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_TIPO);sbSql.append(" not in (");sbSql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);sbSql.append(" ) ");
			}
			
			if(rolId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" not in (");sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);sbSql.append(" ) ");
			}
			
			if(facultadId != GeneralesConstantes.APP_ID_BASE && postgradoId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE && postgradoId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(postgradoId != GeneralesConstantes.APP_ID_BASE && carreraId == GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(rolId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(estadoRoflcr != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE && postgradoId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ? ");sbSql.append(" , ");sbSql.append(" ? ) ");
			}
			
			
//			//facultad
//				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" ? ");
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
//			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  "); //sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
//			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			int contador = 0;

			if(facultadId != GeneralesConstantes.APP_ID_BASE && postgradoId == GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, facultadId);
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE && postgradoId == GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, carreraId);
			}
			if(postgradoId != GeneralesConstantes.APP_ID_BASE && carreraId == GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, postgradoId);
			}
			if(rolId != GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, rolId);
			}
			if(estadoRoflcr != GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, estadoRoflcr);
			}
			if(carreraId != GeneralesConstantes.APP_ID_BASE && postgradoId != GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, carreraId);
				contador = contador + 1;
				pstmt.setInt(contador, postgradoId);
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoUsuarios(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/**
	 * Realiza la busqueda del rol que no tienen rol flujo carrera
	 * @param rolId - rolId id del rol
	 * @return datos generales de usuario con el rol consultado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<UsuarioRolJdbcDto> buscarUsuarioXrolIdXestado(int rolId, int estadoUsro) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_NICK);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DETALLE);
			sbSql.append(" FROM  ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro  ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol  ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			if(rolId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(estadoUsro != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" ORDER BY  "); 
			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			int contador = 0;
			if(rolId != GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, rolId);
			}
			if(estadoUsro != GeneralesConstantes.APP_ID_BASE){
				contador = contador + 1;
				pstmt.setInt(contador, estadoUsro);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoSNroflcr(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<UsuarioRolJdbcDto> buscarRolesUsuarioTodosActivos(String identificacion) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
//			sbSql.append("crrfcl");sbSql.append(",");
//			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(",");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr  ");
						
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr  ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl  ");
			

			sbSql.append(" WHERE ");
//			
				sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);
//			//facultad
//				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(" ? ");
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  "); //sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNuevoUsuario(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public List<UsuarioRolJdbcDto> buscarCarrerasXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") = ? ");
//			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" in (");
//			sbSql.append(RolConstantes.ROL_SECRESECREABOGADO_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_SECRECARRERA_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_ADMINDGA_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_ADMINNIVELACION_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_ADMINFACULTAD_VALUE);sbSql.append(")");
			sbSql.append(" ORDER BY  fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListaCarrerasXidentificacion(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	@Override
	public List<UsuarioRolJdbcDto> buscarRolesXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		System.out.println(identificacion);
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
		
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") = ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" not in (");
			sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_DIRCARRERA_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_SECRECARRERA_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_SECREABOGADO_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_DECANO_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_SUBDECANO_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_ADMINFACULTAD_VALUE);sbSql.append(")");
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			System.out.println(sbSql);
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				
				retorno.add(transformarResultSetADtoRoles(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	@Override
	public List<UsuarioRolJdbcDto> buscarCarrerasXIdentificacionEstudiantes(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ON ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
//			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" in (");
//			sbSql.append(RolConstantes.ROL_SECRESECREABOGADO_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_SECRECARRERA_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_ADMINDGA_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_ADMINNIVELACION_VALUE);sbSql.append(",");
//			sbSql.append(RolConstantes.ROL_ADMINFACULTAD_VALUE);sbSql.append(")");
			sbSql.append(" ORDER BY  fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoEstudiantesUsuarios(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	@Override
	public List<UsuarioRolJdbcDto> buscarUsuariosActivosxCarreraXIdentificacion(
			String identificacion, int rolId , int crrId)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.DPN_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" ORDER BY  fcl.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			
			
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt( 2, rolId ); 
			pstmt.setInt( 3 , crrId );
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListaCarrerasXidentificacion(rs));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	
	public UsuarioRolJdbcDto buscarUsuariosXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		UsuarioRolJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_NICK);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" NOT IN ( ");
			sbSql.append(RolConstantes.ROL_SOPORTE_VALUE);sbSql.append(" , ");
			sbSql.append(RolConstantes.ROL_DIRPOSGRADO_VALUE);sbSql.append(" ) ");
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion.toUpperCase()); 
			
			rs = pstmt.executeQuery();
			retorno = new UsuarioRolJdbcDto();
			while(rs.next()){
				retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
				retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
				retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
				retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
		
	}
	
	@Override
	public UsuarioRolJdbcDto buscarUsuariosXIdentificacionAgregarCarrera(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		UsuarioRolJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_NICK);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" NOT IN ( ");
			sbSql.append(RolConstantes.ROL_SOPORTE_VALUE);sbSql.append(" , ");
			sbSql.append(RolConstantes.ROL_DIRPOSGRADO_VALUE);sbSql.append(" ) ");
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion.toUpperCase()); 
			rs = pstmt.executeQuery();
			retorno = new UsuarioRolJdbcDto();
			while(rs.next()){
				retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
				retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
				retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
				retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
				retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			}
		} catch (Exception e) {
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
		
	}
	
	
	@Override
	public List<UsuarioRolJdbcDto> buscarRolesEvaluacionDocenteXIdentificacion(String identificacion,int tipoEvalId)throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		System.out.println(identificacion);
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
		
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			if(tipoEvalId==TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE){
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append("  in (");
				sbSql.append(RolConstantes.ROL_DIRCARRERA_VALUE);sbSql.append(",");
				sbSql.append(RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);sbSql.append(" )");
			}else{
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append("  in (");
				sbSql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbSql.append(",");
				sbSql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);sbSql.append(" )");
			}
			
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			System.out.println(sbSql);
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			 //cargo la identificacion
		
				pstmt.setString(1, identificacion);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				
				retorno.add(transformarResultSetADtoRolesApelacionEvaluador(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	/**
	 * Metodo que devuelve el rol del evaluado en relacion al rol del evaluador
	 * @param identificacion
	 * @param rolEvaluador
	 * @return
	 * @throws UsuarioRolJdbcDtoException
	 * @throws UsuarioRolJdbcDtoNoEncontradoException
	 */
	
	@Override
	public List<UsuarioRolJdbcDto> buscarRolesEvaluacionDocenteEvaluadoXIdentificacion(String identificacion,int rolEvaluador)throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		System.out.println(identificacion);
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
		
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			//**** CAMPOS DE ROL FLUJO CARRERA ****/

			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			
			//**** CAMPOS DE CARRERA ****/

			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" WHERE ");
			//identificacion
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			if(rolEvaluador==RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE){
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append("  in (");
				sbSql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbSql.append(" )");
			}else{
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append("  in (");
				sbSql.append(RolConstantes.ROL_DOCENTE_VALUE);sbSql.append(" )");
			}
			
			sbSql.append(" ORDER BY  prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			System.out.println(sbSql);
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			 //cargo la identificacion
		
				pstmt.setString(1, identificacion);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				
				retorno.add(transformarResultSetADtoRolesApelacionEvaluado(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private UsuarioRolJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
//		retorno.setPrsTipoIdentificacionSniese(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
//		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
//		retorno.setPrsEtnId(rs.getInt("prsEtn"));
//		retorno.setPrsUbcId(rs.getInt("prsUbc"));
		
		//**** CAMPOS DE ETNIA ****/
//		retorno.setEtnId(rs.getInt(JdbcConstantes.ETN_ID));
//		retorno.setEtnDescripcion(rs.getString(JdbcConstantes.ETN_DESCRIPCION));
//		retorno.setEtnCodigoSniese(rs.getString(JdbcConstantes.ETN_CODIGO_SNIESE));
		
		//**** CAMPOS DE UBICACION ****/
//		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
//		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
//		retorno.setUbcJerarquia(rs.getInt(JdbcConstantes.UBC_JERARQUIA));
//		retorno.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
//		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrIdentificacion(rs.getString(JdbcConstantes.USR_IDENTIFICACION));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setUsrFechaCreacion(rs.getTimestamp(JdbcConstantes.USR_FECHA_CREACION));
		retorno.setUsrFechaCaducidad(rs.getDate(JdbcConstantes.USR_FECHA_CADUCIDAD));
		retorno.setUsrFechaCadPass(rs.getDate(JdbcConstantes.USR_FECHA_CAD_PASS));
		retorno.setUsrEstado(rs.getInt(JdbcConstantes.USR_ESTADO));
		retorno.setUsrEstSesion(rs.getInt(JdbcConstantes.USR_EST_SESION));
		retorno.setUsrActiveDirectory(rs.getInt(JdbcConstantes.USR_ACTIVE_DIRECTORY));
//		//**** CAMPOS DE USUARIO_ROL ****//
//		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
//		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
//		//**** CAMPOS DE ROL ****//
//		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
//		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
//		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL ****//
//		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
//		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		
		//**** CAMPOS DE CARRERA ****//
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
//		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
//		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
//		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoIdentificacion(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrIdentificacion(rs.getString(JdbcConstantes.USR_IDENTIFICACION));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setUsrFechaCreacion(rs.getTimestamp(JdbcConstantes.USR_FECHA_CREACION));
		retorno.setUsrFechaCaducidad(rs.getDate(JdbcConstantes.USR_FECHA_CADUCIDAD));
		retorno.setUsrFechaCadPass(rs.getDate(JdbcConstantes.USR_FECHA_CAD_PASS));
		retorno.setUsrEstado(rs.getInt(JdbcConstantes.USR_ESTADO));
		retorno.setUsrEstSesion(rs.getInt(JdbcConstantes.USR_EST_SESION));
		retorno.setUsrActiveDirectory(rs.getInt(JdbcConstantes.USR_ACTIVE_DIRECTORY));
//		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		
		return retorno;
	}
	
	
	private UsuarioRolJdbcDto transformarResultSetADtoListaCarrerasXidentificacion(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrIdentificacion(rs.getString(JdbcConstantes.USR_IDENTIFICACION));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setUsrFechaCreacion(rs.getTimestamp(JdbcConstantes.USR_FECHA_CREACION));
		retorno.setUsrFechaCaducidad(rs.getDate(JdbcConstantes.USR_FECHA_CADUCIDAD));
		retorno.setUsrFechaCadPass(rs.getDate(JdbcConstantes.USR_FECHA_CAD_PASS));
		retorno.setUsrEstado(rs.getInt(JdbcConstantes.USR_ESTADO));
		retorno.setUsrEstSesion(rs.getInt(JdbcConstantes.USR_EST_SESION));
		retorno.setUsrActiveDirectory(rs.getInt(JdbcConstantes.USR_ACTIVE_DIRECTORY));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL ****//
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoRoles(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoEstudiantesUsuarios(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrIdentificacion(rs.getString(JdbcConstantes.USR_IDENTIFICACION));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setUsrFechaCreacion(rs.getTimestamp(JdbcConstantes.USR_FECHA_CREACION));
		retorno.setUsrFechaCaducidad(rs.getDate(JdbcConstantes.USR_FECHA_CADUCIDAD));
		retorno.setUsrFechaCadPass(rs.getDate(JdbcConstantes.USR_FECHA_CAD_PASS));
		retorno.setUsrEstado(rs.getInt(JdbcConstantes.USR_ESTADO));
		retorno.setUsrEstSesion(rs.getInt(JdbcConstantes.USR_EST_SESION));
		retorno.setUsrActiveDirectory(rs.getInt(JdbcConstantes.USR_ACTIVE_DIRECTORY));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoNuevoUsuario(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL ****//
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoUsuario(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL_FLUJO_CARRERA ****//
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		//**** CAMPOS DE USUARIO ****/
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoUsuarios(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		//**** CAMPOS DE PERSONA ****/
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		retorno.setRolDetalle(rs.getString(JdbcConstantes.ROL_DETALLE));
		//**** CAMPOS DE ROL FLUJO CARRERA ****//
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.DPN_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoSNroflcr(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		//**** CAMPOS DE PERSONA ****/
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		retorno.setRolDetalle(rs.getString(JdbcConstantes.ROL_DETALLE));
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoRolesApelacionEvaluador(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE PERSONA ****//
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoRolesApelacionEvaluado(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE PERSONA ****//
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		//**** CAMPOS DE ROL FLUJO CARRERA ****/
		retorno.setRolId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_CRR_ID));
		
		
		//**** CAMPOS DE GRUPO o carrera  ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));

		return retorno;
	}
}
