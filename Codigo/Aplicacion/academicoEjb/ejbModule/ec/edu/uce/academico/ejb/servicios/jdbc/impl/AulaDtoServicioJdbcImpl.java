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
   
 ARCHIVO:     AulaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla Aula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-05-2017		Marcelo Quishpe				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB AulaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Aula.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class AulaDtoServicioJdbcImpl implements AulaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<AulaDto> listarXUsuarioXDependenciaXEdificio(int usuarioId, int dependenciaId, int edificioId) throws AulaDtoException, AulaDtoNoEncontradoException{
		List<AulaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_PISO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_TIPO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ESTADO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
     		sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.EDF_ID);			
     		sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");		
			  if(dependenciaId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
					sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
		     		sbSql.append(" = ? ");
				}
			  if(edificioId != GeneralesConstantes.APP_ID_BASE){// Si se a seleccioando un edificio, , se incluye la busqueda por este filtro 
				  sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
		     		sbSql.append(" = ? ");
				}
			  sbSql.append(" ORDER BY ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			 sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			 sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
             Integer aux =1;
			if(dependenciaId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
				pstmt.setInt(++aux, dependenciaId);
			}
			if(edificioId != GeneralesConstantes.APP_ID_BASE){  // Si se a seleccioando un edificio, , se incluye la busqueda por este filtro
				pstmt.setInt(++aux, edificioId);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<AulaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuarioXdependenciaXEdificio(rs));
			}
		} catch (SQLException e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.edificio.sql.exception")));
		} catch (Exception e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.edificio.exception")));
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
			throw new AulaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.edificio.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<AulaDto> listarXUsuarioXDependenciaXEdificioXHoraXDia(int usuarioId, int dependenciaId, int edificioId, int horaId, int diaId) throws AulaDtoException, AulaDtoNoEncontradoException{
		List<AulaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_PISO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_TIPO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ESTADO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
			
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
			
			
     		sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.EDF_ID);	
			
			sbSql.append(" AND ");sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
			sbSql.append(" AND ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
			sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);
			sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
			
     		sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");		
			if(dependenciaId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
				sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
				sbSql.append(" = ? ");
			}
			if(edificioId != GeneralesConstantes.APP_ID_BASE){// Si se a seleccioando un edificio, , se incluye la busqueda por este filtro 
				sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
				sbSql.append(" = ? ");
			}
			if(horaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
				sbSql.append(" = ? ");
			}
			if(diaId != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
				sbSql.append(" = ? ");
			}

			  sbSql.append(" ORDER BY ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			 sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			 sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usuarioId);
             Integer aux =1;
			if(dependenciaId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
				pstmt.setInt(++aux, dependenciaId);
			}
			if(edificioId != GeneralesConstantes.APP_ID_BASE){  // Si se a seleccioando un edificio, , se incluye la busqueda por este filtro
				pstmt.setInt(++aux, edificioId);
			}
			
			if(horaId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, horaId);
			}
			if(diaId != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++aux, diaId);
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<AulaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuarioXdependenciaXEdificio(rs));
			}
		} catch (SQLException e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.edificio.sql.exception")));
		} catch (Exception e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.edificio.exception")));
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
			throw new AulaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.edificio.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de  aulas que pertenecen a  edificio 
	 * @param edificioId - id del edificio  a buscar
	 * @return verdero si tiene falso caso contrario
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public boolean buscarPIdEdificio(int edificioId) throws AulaDtoException, AulaDtoNoEncontradoException{
		boolean retorno= false;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_PISO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_TIPO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ESTADO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
     		sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.EDF_ID);			
     		
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
//			sbSql.append(" = ? ");	
//			
//			  if(dependenciaId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
//					sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
//		     		sbSql.append(" = ? ");
//				}
//			  
			  
			  if(edificioId != GeneralesConstantes.APP_ID_BASE){// Si se a seleccioando un edificio, , se incluye la busqueda por este filtro 
				  sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
		     		sbSql.append(" = ? ");
				}
			  sbSql.append(" ORDER BY ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			 sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			 sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			pstmt.setInt(1, usuarioId);
//             Integer aux =1;
//			if(dependenciaId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
//				pstmt.setInt(++aux, dependenciaId);
//			}
			if(edificioId != GeneralesConstantes.APP_ID_BASE){  // Si se a seleccioando un edificio, , se incluye la busqueda por este filtro
				pstmt.setInt(1, edificioId);
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = true;
			}else{
				retorno = false;
			}
			
		} catch (SQLException e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.edificio.sql.exception")));
		} catch (Exception e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.edificio.exception")));
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
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<AulaDto> listarPusrIdPdpnIdP(int usrId, int dpnId) throws AulaDtoException, AulaDtoNoEncontradoException{
		List<AulaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_ID);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CODIGO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_PISO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_TIPO);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_CAPACIDAD);
			sbSql.append(" , ala.");sbSql.append(JdbcConstantes.ALA_ESTADO);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" , edf.");sbSql.append(JdbcConstantes.EDF_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" rlflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_EDIFICIO);sbSql.append(" edf ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_AULA);sbSql.append(" ala ");
     		sbSql.append(" WHERE ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);			
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" rlflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);			
			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" = ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND ");sbSql.append(" edf.");sbSql.append(JdbcConstantes.EDF_ID);
			sbSql.append(" = ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.EDF_ID);			
     		sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ? ");		
			  if(dpnId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
					sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
		     		sbSql.append(" = ? ");
				}
			sbSql.append(" ORDER BY ");sbSql.append(" ala.");sbSql.append(JdbcConstantes.ALA_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, usrId);
			if(dpnId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
				pstmt.setInt(2, dpnId);
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<AulaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXUsuarioXdependenciaXEdificio(rs));
			}
		} catch (SQLException e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.sql.exception")));
		} catch (Exception e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.exception")));
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
			throw new AulaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	public List<AulaDto> buscarAulasPorMateria(int prlId, int mlcrmtId) throws AulaDtoException, AulaDtoNoEncontradoException{
		List<AulaDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT EDF.EDF_ID, ");
		sql.append("   EDF.EDF_CODIGO, ");
		sql.append("   EDF.EDF_DESCRIPCION, ");
		sql.append("   EDF.EDF_ESTADO, ");
		sql.append("   EDF.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   AUL.ALA_TIPO, ");
		sql.append("   AUL.ALA_ESTADO, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.HRAC_ESTADO, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   MLCRPR.MLCRMT_ID ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   HORA_CLASE HOCL, ");
		sql.append("   AULA AUL, ");
		sql.append("   DEPENDENCIA DPN, ");
		sql.append("   EDIFICIO EDF, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE HOAC.HOCLAL_ID = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCLAU.HOCL_ID   = HOCL.HOCL_ID ");
		sql.append(" AND HOCLAU.ALA_ID    = AUL.ALA_ID ");
		sql.append(" AND HOAC.MLCRPR_ID   = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID    = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID      = PRAC.PRAC_ID ");
		sql.append(" AND AUL.EDF_ID       = EDF.EDF_ID ");
		sql.append(" AND DPN.DPN_ID       = EDF.DPN_ID ");
		sql.append(" AND PRL.PRL_ID       = ? ");
		sql.append(" AND MLCRPR.MLCRMT_ID = ? ");
		sql.append(" ORDER BY AUL.ALA_CAPACIDAD ");

		try {
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, prlId);
			pstmt.setInt(2, mlcrmtId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<AulaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarAulasPorMateria(rs));
			}
			
		} catch (SQLException e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.sql.exception")));
		} catch (Exception e) {
			throw new AulaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.exception")));
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
			throw new AulaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AulaDto.buscar.por.usuario.dependencia.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset XUsuarioXdependenciaXEdificio
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private AulaDto transformarResultSetADtoXUsuarioXdependenciaXEdificio(ResultSet rs) throws SQLException{
		AulaDto retorno = new AulaDto();
		  retorno.setAlaId(rs.getInt(JdbcConstantes.ALA_ID));
		  retorno.setAlaCodigo(rs.getString(JdbcConstantes.ALA_CODIGO));
		  retorno.setAlaDescripcion(rs.getString(JdbcConstantes.ALA_DESCRIPCION));
		  retorno.setAlaPiso(rs.getInt(JdbcConstantes.ALA_PISO));
		  retorno.setAlaTipo(rs.getInt(JdbcConstantes.ALA_TIPO)); 
		  retorno.setAlaEstado(rs.getInt(JdbcConstantes.ALA_ESTADO));
		  retorno.setAlaCapacidad(rs.getInt(JdbcConstantes.ALA_CAPACIDAD));
		  retorno.setEdfId(rs.getInt(JdbcConstantes.EDF_ID));
		  retorno.setEdfDescripcion(rs.getString(JdbcConstantes.EDF_DESCRIPCION));
		  retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
		  retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		return retorno;
	} 
	
	private AulaDto transformarResultSetAbuscarAulasPorMateria(ResultSet rs) throws SQLException{
		AulaDto retorno = new AulaDto();
		retorno.setEdfId(rs.getInt(1));
		retorno.setEdfCodigo(rs.getString(2));
		retorno.setEdfDescripcion(rs.getString(3));
		retorno.setEdfEstado(rs.getInt(4));
		retorno.setDpnId(rs.getInt(5));
		retorno.setDpnDescripcion(rs.getString(6));
		retorno.setAlaId(rs.getInt(7));
		retorno.setAlaCodigo(rs.getString(8));
		retorno.setAlaCapacidad(rs.getInt(9));
		retorno.setAlaTipo(rs.getInt(10));
		retorno.setAlaEstado(rs.getInt(11));
		retorno.setPrlId(rs.getInt(12));
		retorno.setPrlCodigo(rs.getString(13));
		retorno.setPrlEstado(rs.getInt(14));
		retorno.setHracId(rs.getInt(15));
		retorno.setHracDia(rs.getInt(16));
		retorno.setHracEstado(rs.getInt(17));
		retorno.setMlcrprId(rs.getInt(18));
		retorno.setMlcrprMallaCurricularMateriaId(rs.getInt(19));
		return retorno;
	}

	
	
	
	
	
}
