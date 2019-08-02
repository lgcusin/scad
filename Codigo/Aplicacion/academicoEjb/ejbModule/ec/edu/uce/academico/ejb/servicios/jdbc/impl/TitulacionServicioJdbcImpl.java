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
   
 ARCHIVO:     ComprobantePagoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-08-2018			Freddy Guzmán						Emisión Inicial
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
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.TitulacionDto;
import ec.edu.uce.academico.ejb.excepciones.TitulacionException;
import ec.edu.uce.academico.ejb.excepciones.TitulacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TitulacionServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;

/**
 * EJB ComprobantePagoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Comprobante.
 * @author fgguzman
 * @version 1.0
 */
@Stateless
public class TitulacionServicioJdbcImpl implements TitulacionServicioJdbc {
	
//	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_TITULACION)
	private DataSource dssTitulacion;
	

	public List<TitulacionDto> listarConvocatoria() throws TitulacionNoEncontradoException,TitulacionException{

		List<TitulacionDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select cnv.cnv_id, cnv.cnv_descripcion ");
		sql.append(" from convocatoria cnv ");
		try {
			con = dssTitulacion.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarConvocatoria(rs));
			}

		} catch (Exception e) {
			throw new TitulacionException("Error de conexión, comuníquese con el administrador.");
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

		if (retorno.isEmpty()) {
			throw new TitulacionNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}
	
	
	
	public List<TitulacionDto> listarTodasFacultades() throws TitulacionNoEncontradoException,TitulacionException{

		List<TitulacionDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select fcl.fcl_id, fcl.fcl_descripcion ");
		sql.append(" from facultad fcl ");
		try {
			con = dssTitulacion.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarTodasFacultades(rs));
			}

		} catch (Exception e) {
			throw new TitulacionException("Error de conexión, comuníquese con el administrador.");
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

		if (retorno.isEmpty()) {
			throw new TitulacionNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}
	
	
	public List<TitulacionDto> listarFacultadesxUsuario(int usrId) throws TitulacionNoEncontradoException,TitulacionException{

		List<TitulacionDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();

		sql.append(" select fcl.fcl_id, fcl.fcl_descripcion ");
		sql.append(" from usuario usr,usuario_rol usro,rol_flujo_carrera rlflcrr ,carrera crr,facultad fcl ");
		sql.append(" where usr.usr_id=usro.usr_id  ");
		sql.append(" and rlflcrr.usro_id=usro.usro_id ");
		sql.append(" and crr.crr_id=rlflcrr.crr_id ");
		sql.append(" and crr.fcl_id=fcl.fcl_id ");
		sql.append(" and usr.usr_id= ? ");
		try {
			con = dssTitulacion.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, usrId);
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarTodasFacultades(rs));
			}

		} catch (Exception e) {
			throw new TitulacionException("Error de conexión, comuníquese con el administrador.");
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

		if (retorno.isEmpty()) {
			throw new TitulacionNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}
	
	public List<TitulacionDto> listarTodasCarreraXFacultad(int fclId) throws TitulacionNoEncontradoException,TitulacionException{

		List<TitulacionDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select crr.crr_id, crr.crr_descripcion ");
		sql.append(" from carrera crr ");
		sql.append(" where crr.fcl_id = ? ");
		try {
			con = dssTitulacion.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, fclId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarTodasCarreraXFacultad(rs));
			}

		} catch (Exception e) {
			throw new TitulacionException("Error de conexión, comuníquese con el administrador.");
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

		if (retorno.isEmpty()) {
			throw new TitulacionNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}
	
	/*
	 * Metodo que retorna la lista el numero de titulados en examen complexivo
	 * tanto de inscritosaprobados(con y sin gracia)y reprobados  
	 * 
	 */
	public List<TitulacionDto> ListarTitulacionExamenComplexivo(int convId, int fclId, int crrId) throws TitulacionNoEncontradoException, TitulacionException {
		List<TitulacionDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct (select count(*) from tramite_titulo trtt, ficha_estudiante fces, mecanismo_titulacion_carrera mcttcr, mecanismo_titulacion mctt , "); 
		sql.append(" carrera crr,facultad fcl,convocatoria cnv where trtt.trtt_id=fces.trtt_id ");
		sql.append(" and fces.mcttcr_id=mcttcr.mcttcr_id ");
		sql.append(" and mcttcr.mctt_id=mctt.mctt_id  ");
		sql.append(" and trtt.trtt_carrera_id=crr.crr_id  ");
		sql.append(" and trtt.cnv_id= cnv.cnv_id  ");
		sql.append(" and crr.fcl_id=fcl.fcl_id ");
		sql.append(" and trtt_estado_tramite=0  ");
		sql.append(" and trtt_estado_proceso>=3  ");
		sql.append(" and mctt.mctt_id=20 ");
		sql.append(" and fcl.fcl_id= ? ");
		if(crrId!=-99){
			sql.append(" and crr.crr_id= ? ");
		}			
		sql.append(" and cnv.cnv_id= ? )as inscritos, ");

		sql.append(" (select count(*) from tramite_titulo trtt, ficha_estudiante fces, mecanismo_titulacion_carrera mcttcr, ");
		sql.append(" mecanismo_titulacion mctt ,carrera crr, asentamiento_nota asno, proceso_tramite prtr,facultad fcl,convocatoria cnv ");
		sql.append(" where trtt.trtt_id=fces.trtt_id  ");
		sql.append(" and fces.mcttcr_id=mcttcr.mcttcr_id  ");
		sql.append(" and mcttcr.mctt_id=mctt.mctt_id  ");
		sql.append(" and trtt.cnv_id= cnv.cnv_id  ");
		sql.append(" and crr.fcl_id=fcl.fcl_id ");
		sql.append(" and trtt.trtt_carrera_id=crr.crr_id  ");
		sql.append(" and prtr.prtr_id=asno.prtr_id  ");
		sql.append(" and trtt.trtt_id=prtr.trtt_id  ");
		sql.append(" and trtt_estado_tramite=0  ");
		sql.append(" and trtt_estado_proceso>=100 ");
		sql.append(" and asno_cmp_gracia_final is  null ");
		sql.append(" and mctt.mctt_id=20  ");
		sql.append(" and fcl.fcl_id= ? ");
		if(crrId!=-99){
			sql.append(" and crr.crr_id= ? ");
		}	
		sql.append(" and cnv.cnv_id= ? ) as aprobados_sin_gracia, ");

		sql.append(" (select  count(*) from tramite_titulo trtt, ficha_estudiante fces, mecanismo_titulacion_carrera mcttcr,mecanismo_titulacion mctt , ");
		sql.append(" carrera crr, asentamiento_nota asno, proceso_tramite prtr,facultad fcl,convocatoria cnv ");
		sql.append(" where trtt.trtt_id=fces.trtt_id  ");
		sql.append(" and fces.mcttcr_id=mcttcr.mcttcr_id  ");
		sql.append(" and mcttcr.mctt_id=mctt.mctt_id  ");
		sql.append(" and trtt.cnv_id= cnv.cnv_id  ");
		sql.append(" and crr.fcl_id=fcl.fcl_id ");
		sql.append(" and trtt.trtt_carrera_id=crr.crr_id  ");
		sql.append(" and prtr.prtr_id=asno.prtr_id  ");
		sql.append(" and trtt.trtt_id=prtr.trtt_id  ");
		sql.append(" and mctt.mctt_id=20  ");
		sql.append(" and trtt_estado_tramite=0  ");
		sql.append(" and trtt_estado_proceso>=100 ");
		sql.append(" and asno_cmp_gracia_final is  not null ");
		sql.append(" and fcl.fcl_id= ? ");
		if(crrId!=-99){
			sql.append(" and crr.crr_id= ? ");
		}	
		sql.append(" and cnv.cnv_id= ? ) as aprobados_con_gracia, ");

		sql.append(" (select count(*) from tramite_titulo trtt, ficha_estudiante fces, mecanismo_titulacion_carrera mcttcr, mecanismo_titulacion mctt , ");
		sql.append(" carrera crr,facultad fcl,convocatoria cnv where trtt.trtt_id=fces.trtt_id  ");
		sql.append(" and fces.mcttcr_id=mcttcr.mcttcr_id  ");
		sql.append(" and mcttcr.mctt_id=mctt.mctt_id  ");
		sql.append(" and trtt.trtt_carrera_id=crr.crr_id  ");
		sql.append(" and trtt.cnv_id= cnv.cnv_id  ");
		sql.append(" and crr.fcl_id=fcl.fcl_id ");
		sql.append(" and mctt.mctt_id=20 ");
		sql.append(" and trtt_estado_proceso in(11,14) ");
		sql.append(" and fcl.fcl_id= ? ");
		if(crrId!=-99){
			sql.append(" and crr.crr_id= ? ");
		}	
		sql.append(" and cnv.cnv_id= ? ) as reprobados ");


		try {
			con = dssTitulacion.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
			if(crrId!=-99){
				pstmt.setInt(1, fclId);
				pstmt.setInt(2, crrId);
				pstmt.setInt(3, convId);
				pstmt.setInt(4, fclId);
				pstmt.setInt(5, crrId);
				pstmt.setInt(6, convId);
				pstmt.setInt(7, fclId);
				pstmt.setInt(8, crrId);
				pstmt.setInt(9, convId);
				pstmt.setInt(10, fclId);
				pstmt.setInt(11, crrId);
				pstmt.setInt(12, convId);
			}else{
				pstmt.setInt(1, fclId);
				pstmt.setInt(2, convId);
				pstmt.setInt(3, fclId);
				pstmt.setInt(4, convId);
				pstmt.setInt(5, fclId);
				pstmt.setInt(6, convId);
				pstmt.setInt(7, fclId);
				pstmt.setInt(8, convId);
			}
		
			
			
			
			
			
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarTitulacionExamenComplexivo(rs));
			}

		} catch (Exception e) {
			throw new TitulacionException("Error de conexión, comuníquese con el administrador.");
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

		if (retorno.isEmpty()) {
			throw new TitulacionNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}

	
	public List<TitulacionDto> ListarTitulacionProyectoTitulacion(int convId, int fclId, int crrId) throws TitulacionNoEncontradoException, TitulacionException {
		List<TitulacionDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select cnv.cnv_descripcion,prs.prs_primer_apellido ||' '|| prs.prs_segundo_apellido || ' ' ||  prs.prs_nombres as nombres, ");
		sql.append(" astt.astt_tema_trabajo,astt.astt_tutor,asno.asno_trb_titulacion_final, ");
		sql.append(" fces.fces_num_acta_grado,fces_fecha_acta_grado ");
		sql.append(" from tramite_titulo trtt, ficha_estudiante fces, ");
		sql.append(" mecanismo_titulacion_carrera mcttcr,mecanismo_titulacion mctt, ");
		sql.append(" carrera crr, asentamiento_nota asno, asignacion_titulacion astt, ");
		sql.append(" proceso_tramite prtr,facultad fcl, ");
		sql.append(" convocatoria cnv,persona prs,tramite_titulo trtt1,proceso_tramite prtr1 ");
		sql.append(" where trtt.trtt_id=fces.trtt_id ");
		sql.append(" and fces.mcttcr_id=mcttcr.mcttcr_id ");
		sql.append(" and fces.prs_id=prs.prs_id ");
		sql.append(" and mcttcr.mctt_id=mctt.mctt_id ");
		sql.append(" and trtt.cnv_id= cnv.cnv_id ");
		sql.append(" and crr.fcl_id=fcl.fcl_id ");
		sql.append(" and trtt.trtt_carrera_id=crr.crr_id ");
		sql.append(" and prtr.prtr_id=asno.prtr_id ");
		sql.append(" and trtt.trtt_id=prtr.trtt_id ");
		sql.append(" and trtt.trtt_id=trtt1.trtt_id "); 
		sql.append(" and trtt1.trtt_id=prtr1.trtt_id ");
		sql.append(" and astt.prtr_id=prtr1.prtr_id ");
		sql.append(" and trtt.trtt_estado_tramite=0 " ); 
		sql.append(" and trtt.trtt_estado_proceso>=100 " );
		sql.append(" and astt_tema_trabajo is not null ");
		sql.append(" and mctt.mctt_id!=20 ");
		sql.append(" and fcl.fcl_id= ? ");
		if(crrId!=-99){
			sql.append(" and crr.crr_id= ? ");
		}
		
		sql.append(" and cnv.cnv_id= ? ");
		sql.append(" order by crr.crr_descripcion, prs.prs_primer_apellido, prs.prs_segundo_apellido ");


		try {
			con = dssTitulacion.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
			if(crrId!=-99){
				pstmt.setInt(1, fclId);
				pstmt.setInt(2, crrId);
				pstmt.setInt(3, convId);
				
			}else{
				pstmt.setInt(1, fclId);
				pstmt.setInt(2, convId);
				
			}
		
			
			
			
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAlistarProyectoTitulacion(rs));
			}

		} catch (Exception e) {
			throw new TitulacionException("Error de conexión, comuníquese con el administrador.");
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

		if (retorno.isEmpty()) {
			throw new TitulacionNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}

	
	
	
	private TitulacionDto transformarResultSetAlistarTitulacionExamenComplexivo(ResultSet rs) throws SQLException{
		TitulacionDto titulacion=new TitulacionDto();
		
		
		titulacion.setNumeroInscritos(rs.getBigDecimal(1));
		titulacion.setNumeroAprobadosGracia(rs.getBigDecimal(2));
		titulacion.setNumeroAprobadosSinGracia(rs.getBigDecimal(3));
		titulacion.setNumeroReprobados(rs.getBigDecimal(4));
		return titulacion;
	}
	
	private TitulacionDto transformarResultSetAlistarConvocatoria(ResultSet rs) throws SQLException{
		TitulacionDto titulacion=new TitulacionDto();
		
		
		titulacion.setCnvId(rs.getInt(1));
		titulacion.setCnvDescripcion(rs.getString(2));
		
		return titulacion;
	}
	
	private TitulacionDto transformarResultSetAlistarTodasFacultades(ResultSet rs) throws SQLException{
		TitulacionDto titulacion=new TitulacionDto();
		
		
		titulacion.setFclId(rs.getInt(1));
		titulacion.setFclDescripcion(rs.getString(2));
		
		return titulacion;
	}
	
	private TitulacionDto transformarResultSetAlistarTodasCarreraXFacultad(ResultSet rs) throws SQLException{
		TitulacionDto titulacion=new TitulacionDto();
		
		
		titulacion.setCrrId(rs.getInt(1));
		titulacion.setCrrDescripcion(rs.getString(2));
		
		return titulacion;
	}
	
	private TitulacionDto transformarResultSetAlistarProyectoTitulacion(ResultSet rs) throws SQLException{
		TitulacionDto titulacion=new TitulacionDto();
		
		
		titulacion.setCnvDescripcion(rs.getString(1));
		titulacion.setNombres(rs.getString(2));
		titulacion.setAsttTemaTrabajo(rs.getString(3));
		titulacion.setAsttTutor(rs.getString(4));
		titulacion.setAsnoNotaFinal(rs.getBigDecimal(5));
		titulacion.setFcesNumeroActa(rs.getString(6));
		titulacion.setFcesFechaActaGrado(rs.getDate(7));
		return titulacion;
	}
	
	
}
	