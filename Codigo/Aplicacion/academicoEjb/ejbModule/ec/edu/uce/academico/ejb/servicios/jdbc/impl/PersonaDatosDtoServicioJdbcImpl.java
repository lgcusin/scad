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

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoGrupoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
/**
 * EJB PersonaDatosDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc del DTO PersonaDatos.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class PersonaDatosDtoServicioJdbcImpl implements PersonaDatosDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */

	public List<PersonaDatosDto> buscarDocentesPorCarreraPeriodo(int carreraId, int periodoId) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{

		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder(); 
		sql.append(" SELECT DISTINCT PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES , ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL , ");
		sql.append("   TMDD.TMDD_ID , ");
		sql.append("   TMDD.TMDD_DESCRIPCION , ");
		sql.append("   RLLB.RLLB_ID , ");
		sql.append("   RLLB.RLLB_DESCRIPCION , ");
		sql.append("   DTPS.DTPS_ID , ");
		sql.append("   PST.PST_DENOMINACION ");
		sql.append(" FROM DEPENDENCIA DPN , ");
		sql.append("   CARRERA CRR , ");
		sql.append("   USUARIO_ROL USRO , ");
		sql.append("   USUARIO USR , ");
		sql.append("   PERSONA PRS , ");
		sql.append("   FICHA_DOCENTE FCDC , ");
		sql.append("   DETALLE_PUESTO DTPS , ");
		sql.append("   RELACION_LABORAL RLLB , ");
		sql.append("   PUESTO PST , ");
		sql.append("   TIEMPO_DEDICACION TMDD, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   ROL_FLUJO_CARRERA ROFLCR ");
		sql.append(" WHERE PRS.PRS_ID      = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID        = USRO.USR_ID ");
		sql.append(" AND PRS.PRS_ID        = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID      = DTPS.FCDC_ID ");
		sql.append(" AND PST.PST_ID        = DTPS.PST_ID ");
		sql.append(" AND RLLB.RLLB_ID      = DTPS.RLLB_ID ");
		sql.append(" AND TMDD.TMDD_ID      = PST.TMDD_ID ");
		sql.append(" AND CRR.CRR_ID        = ROFLCR.CRR_ID ");
		sql.append(" AND DPN.DPN_ID        = CRR.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID      = DTPS.DTPS_PRAC_ID ");
		sql.append(" AND USRO.USRO_ID      = ROFLCR.USRO_ID ");
		sql.append(" AND USRO.ROL_ID       =  " + RolConstantes.ROL_DOCENTE_VALUE);
		sql.append(" AND CRR.CRR_id        = ? ");
		sql.append(" AND DTPS.DTPS_PRAC_ID = ? ");
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO, PRS.PRS_SEGUNDO_APELLIDO, PRS.PRS_NOMBRES ");

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, carreraId);
			pstmt.setInt(2, periodoId);

			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}

		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.no.result.exception")));
		}
		
		return retorno;
	}
	
	
	public List<PersonaDatosDto> buscarDocentesPorFacultadPeriodo(int dependenciaId, int periodoId) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{

		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder(); 
		sql.append(" SELECT DISTINCT PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES , ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL , ");
		sql.append("   TMDD.TMDD_ID , ");
		sql.append("   TMDD.TMDD_DESCRIPCION , ");
		sql.append("   RLLB.RLLB_ID , ");
		sql.append("   RLLB.RLLB_DESCRIPCION , ");
		sql.append("   DTPS.DTPS_ID , ");
		sql.append("   PST.PST_NIVEL_RANGO_GRADUAL, ");
		sql.append("   PST.PST_CATEGORIA_DOCENTE, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM DEPENDENCIA DPN , ");
		sql.append("   CARRERA CRR , ");
		sql.append("   USUARIO_ROL USRO , ");
		sql.append("   USUARIO USR , ");
		sql.append("   PERSONA PRS , ");
		sql.append("   FICHA_DOCENTE FCDC , ");
		sql.append("   DETALLE_PUESTO DTPS , ");
		sql.append("   RELACION_LABORAL RLLB , ");
		sql.append("   PUESTO PST , ");
		sql.append("   TIEMPO_DEDICACION TMDD, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE PRS.PRS_ID      = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID        = USRO.USR_ID ");
		sql.append(" AND PRS.PRS_ID        = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID      = DTPS.FCDC_ID ");
		sql.append(" AND PST.PST_ID        = DTPS.PST_ID ");
		sql.append(" AND RLLB.RLLB_ID      = DTPS.RLLB_ID ");
		sql.append(" AND TMDD.TMDD_ID      = PST.TMDD_ID ");
		sql.append(" AND CRR.CRR_ID        = DTPS.CRR_ID ");
		sql.append(" AND DPN.DPN_ID        = CRR.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID      = DTPS.DTPS_PRAC_ID ");
		sql.append(" AND USRO.ROL_ID       =  " + RolConstantes.ROL_DOCENTE_VALUE);
		sql.append(" AND CRR.CRR_TIPO      =  " + CarreraConstantes.TIPO_PREGRADO_VALUE);
		sql.append(" AND DPN.dpn_id     = ? ");
		sql.append(" AND DTPS.DTPS_PRAC_ID = ? ");
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO, PRS.PRS_SEGUNDO_APELLIDO, PRS.PRS_NOMBRES ");

		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, dependenciaId);
			pstmt.setInt(2, periodoId);

			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarDocentesPorFacultadPeriodo(rs));
			}

		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.no.result.exception")));
		}
		
		return retorno;
	}
	
	
	public List<PersonaDatosDto> buscarDocentesPorFacultadPeriodoMultipleNivel(String param, int tipoBusqueda)  throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{

		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder(); 
		sql.append(" SELECT DISTINCT PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES , ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL , ");
		sql.append("   TMDD.TMDD_ID , ");
		sql.append("   TMDD.TMDD_DESCRIPCION , ");
		sql.append("   RLLB.RLLB_ID , ");
		sql.append("   RLLB.RLLB_DESCRIPCION , ");
		sql.append("   DTPS.DTPS_ID , ");
		sql.append("   PST.PST_CATEGORIA_DOCENTE, ");
		sql.append("   PST.PST_NIVEL_RANGO_GRADUAL, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM DEPENDENCIA DPN , ");
		sql.append("   CARRERA CRR , ");
		sql.append("   USUARIO_ROL USRO , ");
		sql.append("   USUARIO USR , ");
		sql.append("   PERSONA PRS , ");
		sql.append("   FICHA_DOCENTE FCDC , ");
		sql.append("   DETALLE_PUESTO DTPS , ");
		sql.append("   RELACION_LABORAL RLLB , ");
		sql.append("   PUESTO PST , ");
		sql.append("   TIEMPO_DEDICACION TMDD, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE PRS.PRS_ID           = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID             = USRO.USR_ID ");
		sql.append(" AND PRS.PRS_ID             = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID           = DTPS.FCDC_ID ");
		sql.append(" AND PST.PST_ID             = DTPS.PST_ID ");
		sql.append(" AND RLLB.RLLB_ID           = DTPS.RLLB_ID ");
		sql.append(" AND TMDD.TMDD_ID           = PST.TMDD_ID ");
		sql.append(" AND CRR.CRR_ID             = DTPS.CRR_ID ");
		sql.append(" AND DPN.DPN_ID             = CRR.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID           = DTPS.DTPS_PRAC_ID ");
		sql.append(" AND USRO.ROL_ID       		=  " + RolConstantes.ROL_DOCENTE_VALUE);
		
		if (tipoBusqueda == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
			sql.append(" AND PRS.PRS_IDENTIFICACION        = ? ");
		}else {
			sql.append(" AND PRS.PRS_PRIMER_APELLIDO       = ? ");
		}
		
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   PRAC.PRAC_ID ");
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, param.toUpperCase());

			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarDocentesPorFacultadPeriodoMultipleNivel(rs));
			}

		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.facultad.carrera.no.result.exception")));
		}
		
		return retorno;
	}
	
	public PersonaDatosDto buscarPorId(int personaId , int periodoId) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		PersonaDatosDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder(); 
		sql.append(" SELECT DISTINCT PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES , ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL , ");
		sql.append("   TMDD.TMDD_ID , ");
		sql.append("   TMDD.TMDD_DESCRIPCION , ");
		sql.append("   RLLB.RLLB_ID , ");
		sql.append("   RLLB.RLLB_DESCRIPCION , ");
		sql.append("   DTPS.DTPS_ID , ");
		sql.append("   PST.PST_DENOMINACION ");
		sql.append(" FROM DEPENDENCIA DPN , ");
		sql.append("   CARRERA CRR , ");
		sql.append("   USUARIO_ROL USRO , ");
		sql.append("   USUARIO USR , ");
		sql.append("   PERSONA PRS , ");
		sql.append("   FICHA_DOCENTE FCDC , ");
		sql.append("   DETALLE_PUESTO DTPS , ");
		sql.append("   RELACION_LABORAL RLLB , ");
		sql.append("   PUESTO PST , ");
		sql.append("   TIEMPO_DEDICACION TMDD, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE PRS.PRS_ID      = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID        = USRO.USR_ID ");
		sql.append(" AND PRS.PRS_ID        = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID      = DTPS.FCDC_ID ");
		sql.append(" AND PST.PST_ID        = DTPS.PST_ID ");
		sql.append(" AND RLLB.RLLB_ID      = DTPS.RLLB_ID ");
		sql.append(" AND TMDD.TMDD_ID      = PST.TMDD_ID ");
		sql.append(" AND CRR.CRR_ID        = DTPS.CRR_ID ");
		sql.append(" AND DPN.DPN_ID        = CRR.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID      = DTPS.DTPS_PRAC_ID ");
		sql.append(" AND USRO.ROL_ID       =  " + RolConstantes.ROL_DOCENTE_VALUE);
		sql.append(" AND prs.prs_id     = ? ");
		if(periodoId != GeneralesConstantes.APP_ID_BASE)
		sql.append(" AND DTPS.DTPS_PRAC_ID = ? ");
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO, PRS.PRS_SEGUNDO_APELLIDO, PRS.PRS_NOMBRES ");
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, personaId);
			if(periodoId != GeneralesConstantes.APP_ID_BASE)
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}

		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.exception")));
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
		
		if (retorno == null ) { 
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param idDocente ide del docente a buscar
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarPorIdEntidad(PersonaDatosDto docente) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		PersonaDatosDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder(); 
			sbSql.append("SELECT DISTINCT ");
					sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM (");
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_TIPO);
					sbSql.append(" =");
					sbSql.append(docente.getDtpsCrrTipo());
					 
					
					if(docente.getPrsId() != GeneralesConstantes.APP_ID_BASE){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
						sbSql.append(" = ");
						sbSql.append(" ?"); 
					}  
					
					sbSql.append(" UNION ALL");

					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" ROFLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID); 
					sbSql.append(" AND");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_TIPO);
					sbSql.append(" =");
					sbSql.append(docente.getDtpsCrrTipo());

					
					if(docente.getPrsId() != GeneralesConstantes.APP_ID_BASE){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
						sbSql.append(" = ");
						sbSql.append(" ?"); 
					} 
					
					sbSql.append(" ) "); 
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					pstmt.setInt(1, docente.getPrsId());
					pstmt.setInt(2, docente.getPrsId());
					rs = pstmt.executeQuery();
					if(rs.next()){
						retorno = transformarResultSetADto(rs);
					}else{
						retorno = null;
					}
			 
		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.exception")));
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
		
		if (retorno == null ) { 
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param idDocente ide del docente a buscar
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarPorIdPorTipoEntidad(PersonaDatosDto docente) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		PersonaDatosDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder(); 
			sbSql.append("SELECT DISTINCT ");
					sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM (");
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_CRR_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_TIPO);
					sbSql.append(" =");
					sbSql.append(docente.getDtpsCrrTipo());
					 
					
					if(docente.getPrsId() != GeneralesConstantes.APP_ID_BASE){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
						sbSql.append(" = ");
						sbSql.append(" ?"); 
					}  
					
					sbSql.append(" UNION ALL");

					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" ROFLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID); 
					sbSql.append(" AND");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_CRR_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_TIPO);
					sbSql.append(" =");
					sbSql.append(docente.getDtpsCrrTipo());

					
					if(docente.getPrsId() != GeneralesConstantes.APP_ID_BASE){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
						sbSql.append(" = ");
						sbSql.append(" ?"); 
					} 
					
					sbSql.append(" ) "); 
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					pstmt.setInt(1, docente.getPrsId());
					pstmt.setInt(2, docente.getPrsId());
					rs = pstmt.executeQuery();
					if(rs.next()){
						retorno = transformarResultSetADto(rs);
					}else{
						retorno = null;
					}
			 
		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.exception")));
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
		
		if (retorno == null ) { 
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.por.id.no.result.exception")));
		}
		return retorno;
	}
	
	/**
     * Método que busca un docente por identificacion
     * @param identificacion -identificacion del postulante que se requiere buscar.
     * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
     */
    public List<PersonaDatosDto> buscarDocenteXIdentificacionXApellido(String identificacion, String apellido) throws PersonaDatosDtoException,PersonaDatosDtoNoEncontradoException{
        
    	List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try { 
			
			StringBuilder sbSql = new StringBuilder(); 
			sbSql.append("SELECT DISTINCT ");
					sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PST_DENOMINACION);
				    
					sbSql.append(" FROM (");
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID); 
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(RolConstantes.ROL_DOCENTE_VALUE); 
					
					if(identificacion != null && identificacion.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					}
					
					if(apellido != null && apellido.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					} 
					
					sbSql.append(" AND USR.");sbSql.append(JdbcConstantes.USR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);	
					
					sbSql.append(" UNION ALL");

					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" ROFLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID); 
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(RolConstantes.ROL_DOCENTE_VALUE); 
					
					if(identificacion != null && identificacion.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					}
					
					if(apellido != null && apellido.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					}
					
					sbSql.append(" AND USR.");sbSql.append(JdbcConstantes.USR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);	
					sbSql.append(" AND ROFLCR.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);	
					
					sbSql.append(" ) ");
					sbSql.append(" ORDER BY 3 ");
			
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					if(identificacion == null || identificacion.length()==0){  
						pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%")); 
						pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%")); 
					}
					
					if(apellido == null || apellido.length()==0){   
						pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%")); 
						pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%"));
					}
					
					if((apellido != null && apellido.length()>0) && (identificacion != null && identificacion.length()>0)){  
						pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%"));
						pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%"));
						pstmt.setString(3, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%"));
						pstmt.setString(4, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%"));
					}
					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<PersonaDatosDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.docente.identificar.apellido.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.docente.identificar.apellido.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.docente.identificar.apellido.no.result.exception")));
		}
		return retorno;
	}
    
    /**
     * Método que busca un docente por identificacion, dependiendo del tipo de carrera
     * @param identificacion -identificacion del postulante que se requiere buscar.
     * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
     */
    public List<PersonaDatosDto> buscarDocenteTipoCarreraXIdentificacionXApellido(String identificacion, String apellido) throws PersonaDatosDtoException,PersonaDatosDtoNoEncontradoException{
        
    	List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try { 
			
			StringBuilder sbSql = new StringBuilder(); 
			sbSql.append("SELECT DISTINCT ");
					sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.CRR_TIPO);
				    
					sbSql.append(" FROM (");
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_CRR_ID); 
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(RolConstantes.ROL_DOCENTE_VALUE); 
					
					if(identificacion != null && identificacion.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					}
					
					if(apellido != null && apellido.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					} 
					
					sbSql.append(" AND USR.");sbSql.append(JdbcConstantes.USR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);	
					
					sbSql.append(" UNION ALL");

					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" ROFLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD");
					
					sbSql.append(" WHERE");
					
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" AND");
					sbSql.append(" ROFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID); 
					sbSql.append(" AND");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_CRR_ID); 
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(RolConstantes.ROL_DOCENTE_VALUE); 
					
					if(identificacion != null && identificacion.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					}
					
					if(apellido != null && apellido.length()>0){  
						sbSql.append(" AND");
						sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
						sbSql.append(" LIKE ");
						sbSql.append(" ? ");
					}
					
					sbSql.append(" AND USR.");sbSql.append(JdbcConstantes.USR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);	
					sbSql.append(" AND ROFLCR.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);	
					
					sbSql.append(" ) ");
					sbSql.append(" ORDER BY 3 ");
			
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					if(identificacion == null || identificacion.length()==0){  
						pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%")); 
						pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%")); 
					}
					
					if(apellido == null || apellido.length()==0){   
						pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%")); 
						pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%"));
					}
					
					if((apellido != null && apellido.length()>0) && (identificacion != null && identificacion.length()>0)){  
						pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%"));
						pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%"));
						pstmt.setString(3, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase()+"%"));
						pstmt.setString(4, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase()+"%"));
					}
					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<PersonaDatosDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoTipoCarrera(rs));
					}
			
			
			
		} catch (SQLException e) {  
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.docente.identificar.apellido.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.docente.identificar.apellido.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.docente.identificar.apellido.no.result.exception")));
		}
		return retorno;
	}
    
    /**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera o enlista todos los docentes 
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idCarrera - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXCarreraConCargaAcademica(int idCarrera, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		
		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" PRAC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" PRL ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRAC_ID);
					
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRL_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(" "+UsuarioRolConstantes.ESTADO_ACTIVO_VALUE); 
					
					sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
					sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
					 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" = ");
					sbSql.append(" ?");  

					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" = ");
					sbSql.append(" ?");  
					
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(" ?"); 
					
					sbSql.append(" ORDER BY 3 ");

					con = ds.getConnection(); 
					pstmt = con.prepareStatement(sbSql.toString()); 

					pstmt.setInt(1, idCarrera);
					pstmt.setInt(2, idPeriodo);
					pstmt.setInt(3, idRol); 

					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<PersonaDatosDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) {   
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.carrera.con.carga.academica.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.carrera.con.carga.academica.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.carrera.con.carga.academica.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera o enlista todos los directivos  
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idCarrera - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXCarreraConCargaAcademicaDirectivo(int idCarrera, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		
		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" RLFLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" PRAC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" PRL ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRAC_ID);
					
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRL_ID);
					
//					sbSql.append(" AND");
//					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.CRR_ID);
//					sbSql.append(" =");
//					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
//										
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(" =");
					sbSql.append(" RLFLCR.");sbSql.append(JdbcConstantes.USRO_ID);
					
					
					sbSql.append(" AND");
					sbSql.append(" RLFLCR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" RLFLCR.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
					sbSql.append(" = ");
					sbSql.append(" "+UsuarioRolConstantes.ESTADO_ACTIVO_VALUE); 
					
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(" "+UsuarioRolConstantes.ESTADO_ACTIVO_VALUE); 
					 
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" = ");
					sbSql.append(" ?");  
					
					sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
					sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);

					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" = ");
					sbSql.append(" ?");  
					
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(" ?"); 
					
					sbSql.append(" ORDER BY 3 ");

					con = ds.getConnection(); 
					pstmt = con.prepareStatement(sbSql.toString()); 

					pstmt.setInt(1, idCarrera);
					pstmt.setInt(2, idPeriodo);
					pstmt.setInt(3, idRol); 

					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<PersonaDatosDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) {   
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.carrera.con.carga.academica.directivo.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.carrera.con.carga.academica.directivo.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.carrera.con.carga.academica.directivo.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera/area o enlista todos los docentes 
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idArea - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXAreaConCargaAcademica(int idArea, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		
		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" MTR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" PRAC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" PRL ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MTR.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRAC_ID);
					
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRL_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);
					sbSql.append(" = ");
					sbSql.append(" "+UsuarioRolConstantes.ESTADO_ACTIVO_VALUE); 
					 
					sbSql.append(" AND");
					sbSql.append(" MTR.");sbSql.append("GRP_ID");
					sbSql.append(" = ");
					sbSql.append(" ?");  
					
					sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
					sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);

					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" = ");
					sbSql.append(" ?");  
					
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(" ?"); 
					
					sbSql.append(" ORDER BY 3 ");
 
					con = ds.getConnection(); 
					pstmt = con.prepareStatement(sbSql.toString()); 

					pstmt.setInt(1, idArea);
					pstmt.setInt(2, idPeriodo);
					pstmt.setInt(3, idRol); 

					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<PersonaDatosDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) {   
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.area.con.carga.academica.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.area.con.carga.academica.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.area.con.carga.academica.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera/area o enlista todos los docentes pares academicos
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idArea - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXAreaConCargaAcademicaParAcademico(int idArea, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		
		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" MTR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" PRAC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" PRL ");
					sbSql.append(" , ");sbSql.append("ROL_FLUJO_GRUPO");sbSql.append(" RLFLGR ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MTR.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MTR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRAC_ID);
					
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRL_ID);
					
//					sbSql.append(" AND");
//					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.CRR_ID);
//					sbSql.append(" =");
//					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					
					sbSql.append(" AND");sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" =");sbSql.append(" RLFLGR.");sbSql.append(JdbcConstantes.USRO_ID);
					
					sbSql.append(" AND");sbSql.append(" RLFLGR.");sbSql.append("GRP_ID");sbSql.append(" =");sbSql.append(" MTR.");sbSql.append("GRP_ID");
					sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND");sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(" "+UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND");sbSql.append(" RLFLGR.");sbSql.append("ROFLGR_ESTADO");sbSql.append(" = ");sbSql.append(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
					sbSql.append(" AND");sbSql.append(" MTR.");sbSql.append("GRP_ID");sbSql.append(" = ");sbSql.append(" ?");  
					sbSql.append(" AND");sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" ?");  
					sbSql.append(" AND");sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(" ?"); 
					
					sbSql.append(" ORDER BY 3 ");
 
					con = ds.getConnection(); 
					pstmt = con.prepareStatement(sbSql.toString()); 

					pstmt.setInt(1, idArea);
					pstmt.setInt(2, idPeriodo);
					pstmt.setInt(3, idRol); 

					
					rs = pstmt.executeQuery();
					retorno = new ArrayList<PersonaDatosDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADto(rs));
					}
			
			
			
		} catch (SQLException e) {   
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.area.con.carga.academica.par.academico.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.area.con.carga.academica.par.academico.exception")));
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
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.listar.area.con.carga.academica.par.academico.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca docente con detalle puesto 
	 * @param idDetallePuesto - Id de detalle puesto a buscar  
	 * @return docente con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarXDetallePuesto(int idDetallePuesto) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException{
		
		
		PersonaDatosDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder(); 
			 
					
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
					sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
					
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" USR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" USRO ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" ROL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" PRS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" FCDC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" DTPS ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" CRHR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" MLCRPR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" MLCRMT ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" MLCR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" CRR ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" DPN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" PRAC ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" PRL ");
					
					sbSql.append(" WHERE ");
					
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" =");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(" AND");
					sbSql.append(" USRO.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" =");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" AND");
					sbSql.append(" USR.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" =");
					sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" =");
					sbSql.append(" FCDC.");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" =");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
					sbSql.append(" AND");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" =");
					sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
					sbSql.append(" AND");
					sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" =");
					sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" =");
					sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
					sbSql.append(" AND");
					sbSql.append(" CRHR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" =");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" AND");
					sbSql.append(" MLCRMT.");sbSql.append(JdbcConstantes.MLCR_ID);
					sbSql.append(" =");
					sbSql.append(" MLCR.");sbSql.append(JdbcConstantes.MLCR_ID);
					
					sbSql.append(" AND");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" =");
					sbSql.append(" DPN.");sbSql.append(JdbcConstantes.DPN_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRAC.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRAC_ID);
					
					sbSql.append(" AND");
					sbSql.append(" MLCRPR.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" =");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.PRL_ID);
					
					sbSql.append(" AND");
					sbSql.append(" PRL.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" =");
					sbSql.append(" CRR.");sbSql.append(JdbcConstantes.CRR_ID);
					 
					sbSql.append(" AND");
					sbSql.append(" ROL.");sbSql.append(JdbcConstantes.ROL_ID);
					sbSql.append(" = ");
					sbSql.append(RolConstantes.ROL_DOCENTE_VALUE); 
					  
						sbSql.append(" AND");
						sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
						sbSql.append(" = ");
						sbSql.append(" ?");  
						
				
					sbSql.append(" ORDER BY 3 ");

					con = ds.getConnection();
					
					pstmt = con.prepareStatement(sbSql.toString()); 
					
						pstmt.setInt(1, idDetallePuesto); 
					
					
					rs = pstmt.executeQuery();
					retorno = new PersonaDatosDto();
					while(rs.next()){
						retorno = transformarResultSetADto(rs);
					}
			
			
			
		} catch (SQLException e) {   
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.detalle.puesto.sql.exception")));
		} catch (Exception e) { 
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.detalle.puesto.exception")));
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
		 
		if (retorno == null ) { 
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PersonaDatosDto.buscar.detalle.puesto.no.result.exception")));
		}
		return retorno;
	}
    	
	
	
	/**
	 * Método que realiza la busqueda de los docentes que estan asignados a un area
	 * @param areaId - Id de la materia a buscar
	 * @param periodoId - Id del periodo a buscar
	 * @return - retorna la lista de persona datos dto que estan asignados al area 
	 * @throws PersonaDatosDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDatosDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public List<PersonaDatosDto> listarXAreaXPeriodo(int areaId, int periodoId) throws PersonaDatosDtoException, PersonaDatosDtoNoEncontradoException{
		List<PersonaDatosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			
			sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , PRS.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
			sbSql.append(" , TMDD.");sbSql.append(JdbcConstantes.TMDD_DESCRIPCION);
			sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
			sbSql.append(" , RLLB.");sbSql.append(JdbcConstantes.RLLB_DESCRIPCION);
			sbSql.append(" , DTPS.");sbSql.append(JdbcConstantes.DTPS_ID);
			sbSql.append(" , PST.");sbSql.append(JdbcConstantes.PST_DENOMINACION);
//			sbSql.append(" , MTR.");sbSql.append(JdbcConstantes.MTR_CODIGO);
//			sbSql.append(" , MTR.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_RELACION_LABORAL);sbSql.append(" RLLB ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PUESTO);sbSql.append(" PST ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TIEMPO_DEDICACION);sbSql.append(" TMDD ");			
			
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
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
			sbSql.append(" = ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);
			
			sbSql.append(" AND");sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.PST_ID);
			sbSql.append(" =");sbSql.append(" PST.");sbSql.append(JdbcConstantes.PST_ID);
			
			sbSql.append(" AND");sbSql.append(" DTPS.");sbSql.append(JdbcConstantes.RLLB_ID);
			sbSql.append(" =");sbSql.append(" RLLB.");sbSql.append(JdbcConstantes.RLLB_ID);
			
			sbSql.append(" AND");sbSql.append(" PST.");sbSql.append(JdbcConstantes.TMDD_ID);
			sbSql.append(" =");sbSql.append(" TMDD.");sbSql.append(JdbcConstantes.TMDD_ID);
			
			sbSql.append(" AND ");sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);
			sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			
			sbSql.append(" AND ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
//			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
//			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ESTADO);
			sbSql.append(" = ");sbSql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_ESTADO);
			sbSql.append(" = ");sbSql.append(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" mtr.");sbSql.append("GRP_ID");
			sbSql.append(" = ? "); 
			
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setInt(1, areaId);
			pstmt.setInt(2, periodoId); 
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PersonaDatosDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoConMaterias(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDatosDto.x.area.x.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaDatosDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDatosDto.x.area.x.periodo.exception")));
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
			
			throw new PersonaDatosDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDatosDto.x.area.x.periodo.no.result.exception")));
		}	
		return retorno;
	}
   
    
    
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private PersonaDatosDto transformarResultSetADto(ResultSet rs) throws SQLException{
		PersonaDatosDto retorno = new PersonaDatosDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
	
		return retorno;
	}

	private PersonaDatosDto transformarResultSetADtoConMaterias(ResultSet rs) throws SQLException{
		PersonaDatosDto retorno = new PersonaDatosDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
//		retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));cc 
//		retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
		return retorno;
	}
	
	private PersonaDatosDto transformarResultSetADtoTipoCarrera(ResultSet rs) throws SQLException{
		PersonaDatosDto retorno = new PersonaDatosDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
		retorno.setDtpsCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
	
		return retorno;
	}
	
	private PersonaDatosDto rsAbuscarDocentesPorFacultadPeriodo(ResultSet rs) throws SQLException{
		PersonaDatosDto retorno = new PersonaDatosDto();
		CarreraDto carrera = new CarreraDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		
		retorno.setPstNivelRangoGradual(rs.getInt(JdbcConstantes.PST_NIVEL_RANGO_GRADUAL));
		retorno.setPstCategoria(rs.getInt(JdbcConstantes.PST_CATEGORIA_DOCENTE));
		
		carrera.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		carrera.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		
		periodo.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		periodo.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		
		retorno.setPrsCarreraDto(carrera);
		retorno.setPrsPeriodoAcademicoDto(periodo);
		retorno.setDtpsCrrDescripcion(carrera.getCrrDescripcion());
		
		return retorno;
	}
	

	private PersonaDatosDto rsAbuscarDocentesPorFacultadPeriodoMultipleNivel(ResultSet rs) throws SQLException{
		PersonaDatosDto retorno = new PersonaDatosDto();
		CarreraDto carrera  = new CarreraDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setPstCategoria(rs.getInt(JdbcConstantes.PST_CATEGORIA_DOCENTE));
		retorno.setPstNivelRangoGradual(rs.getInt(JdbcConstantes.PST_NIVEL_RANGO_GRADUAL));
		
		carrera.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		carrera.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		carrera.setCrrTipo(rs.getInt(JdbcConstantes.CRR_TIPO));
		retorno.setPrsCarreraDto(carrera);
		
		periodo.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		periodo.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		retorno.setPrsPeriodoAcademicoDto(periodo);
		
		return retorno;
	}
}

