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
   
 ARCHIVO:     HoraClaseDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla HoraClase.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-SEPT-2017		Dennis Collaguazo				       Emisión Inicial
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
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.HorarioFuncionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HoraClaseConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB HoraClaseDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla HoraClase.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class HoraClaseDtoServicioJdbcImpl implements HoraClaseDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de todas las hora clase disponible por aula y por día
	 * @param alaId - alaId id del aula 
	 * @param hracDia - hracDia dia que se quiere buscar
	 * @param pracId - pracId id del periodo academico a buscar la disponibilidad
	 * @param listaHorasND - listaHorasND lista de horas clase que estan utilizadas
	 * @return Lista de todas las hora clase que se necesita que esten disponibles para la asignación 
	 * @throws HoraClaseDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HoraClaseDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<HoraClaseDto> listarPalaIdPhracDiaPpracIdPlistHocl(int alaId, int hracDia, int pracId, List<HorarioAcademicoDto> listaHorasND) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl ");
			
     		sbSql.append(" WHERE ");
     		
     		if(listaHorasND.size() > 0){
     		
     			sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" not in ( ");
				
				sbSql.append(" SELECT DISTINCT ");
				sbSql.append(" hocl1.");sbSql.append(JdbcConstantes.HOCL_ID);
				
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl1 ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
				
	     		sbSql.append(" WHERE ");sbSql.append(" hocl1.");sbSql.append(JdbcConstantes.HOCL_ID);
	     		sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
	     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);
	     		sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
	     		sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
	     		sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
	     		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
	     		sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
	     		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
	     		sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
	     		
	//     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
	//     		sbSql.append(" = ? ");
	     		sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
	     		sbSql.append(" = ? ");
	     		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
	     		sbSql.append(" = ? ");
	     		
	//     		if(listaHorasND.size() >= 1 ){
	//     			sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);sbSql.append(" in ( ");
	//				for (int i = 0; i < listaHorasND.size(); i++) {
	//					sbSql.append(" ? ");
	//					if(i != listaHorasND.size() -1) {
	//			         sbSql.append(","); 
	//			        }
	//				}
	//				sbSql.append(" ) ");
	//     		}
	     		
	     		if(listaHorasND.size() >= 1 ){
	     			sbSql.append(" AND ");sbSql.append(" hocl1.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" in ( ");
					for (int i = 0; i < listaHorasND.size(); i++) {
						sbSql.append(" ? ");
						if(i != listaHorasND.size() -1) {
				         sbSql.append(","); 
				        }
					}
					sbSql.append(" ) ");
	     		}
	     		
	     		sbSql.append(" ) ");
	
	     		sbSql.append(" AND ");
     		
     		}
     		sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);

     		sbSql.append(" not in ( ");
     		
     		
     		sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" hocl2.");sbSql.append(JdbcConstantes.HOCL_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac2 ");
			
     		sbSql.append(" WHERE ");sbSql.append(" hocl2.");sbSql.append(JdbcConstantes.HOCL_ID);
     		sbSql.append(" = ");sbSql.append(" hoclal2.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
     		sbSql.append(" AND ");sbSql.append(" hoclal2.");sbSql.append(JdbcConstantes.HOCLAL_ID);
     		sbSql.append(" = ");sbSql.append(" hrac2.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
     		sbSql.append(" AND ");sbSql.append(" mlcrpr2.");sbSql.append(JdbcConstantes.MLCRPR_ID);
     		sbSql.append(" = ");sbSql.append(" hrac2.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
     		sbSql.append(" AND ");sbSql.append(" prl2.");sbSql.append(JdbcConstantes.PRL_ID);
     		sbSql.append(" = ");sbSql.append(" mlcrpr2.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
     		sbSql.append(" AND ");sbSql.append(" prac2.");sbSql.append(JdbcConstantes.PRAC_ID);
     		sbSql.append(" = ");sbSql.append(" prl2.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
     		
//     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
//     		sbSql.append(" = ? ");
     		sbSql.append(" AND ");sbSql.append(" hrac2.");sbSql.append(JdbcConstantes.HRAC_DIA);
     		sbSql.append(" = ? ");
     		sbSql.append(" AND ");sbSql.append(" prac2.");sbSql.append(JdbcConstantes.PRAC_ID);
     		sbSql.append(" = ? ");
     		sbSql.append(" AND ");sbSql.append(" hoclal2.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
     		sbSql.append(" = ? ");
     		
     		
     		sbSql.append(" ) ");
			
			sbSql.append(" ORDER BY ");sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			pstmt.setInt(1, alaId);
			int contador = 0;
			if(listaHorasND.size() > 0){
				contador = contador + 1;
				pstmt.setInt(contador, hracDia);
				contador = contador + 1;
				pstmt.setInt(contador, pracId);
				
//				int contador = 2;
	//			if(listaHorasND.size() >= 1){ // caso para setear la lista de hora clase 
	//				for (HorarioAcademicoDto item : listaHorasND) {
	//					contador = contador + 1;
	//					pstmt.setInt(contador, item.getAlaId()); //cargo los id de hora clase
	//				}
	//			}
				if(listaHorasND.size() >= 1){ // caso para setear la lista de hora clase 
					for (HorarioAcademicoDto item : listaHorasND) {
						contador = contador + 1;
						pstmt.setInt(contador, item.getHoclId()); //cargo los id de hora clase
					}
				}
			}
			contador = contador + 1;
			pstmt.setInt(contador, hracDia);
			contador = contador + 1;
			pstmt.setInt(contador, pracId);
			contador = contador + 1;
			pstmt.setInt(contador, alaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		return  retorno;
	}
	
	public List<HoraClaseDto> listarPalaIdPhracDiaPpracIdPlistHoclNuevo(int alaId, int hracDia, int pracId, List<HorarioAcademicoDto> listaHorasND) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl ");
			
     		sbSql.append(" WHERE ");
     		
     		if(listaHorasND.size() > 0){
     		
     			sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" not in ( ");
				
				sbSql.append(" SELECT DISTINCT ");
				sbSql.append(" hocl1.");sbSql.append(JdbcConstantes.HOCL_ID);
				
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl1 ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
				
	     		sbSql.append(" WHERE ");sbSql.append(" hocl1.");sbSql.append(JdbcConstantes.HOCL_ID);
	     		sbSql.append(" = ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
	     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ID);
	     		sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
	     		sbSql.append(" AND ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
	     		sbSql.append(" = ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
	     		sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);
	     		sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
	     		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
	     		sbSql.append(" = ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
	     		
	//     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
	//     		sbSql.append(" = ? ");
	     		sbSql.append(" AND ");sbSql.append(" hrac.");sbSql.append(JdbcConstantes.HRAC_DIA);
	     		sbSql.append(" = ? ");
	     		sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
	     		sbSql.append(" = ? ");
	     		
	     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
	     		sbSql.append(" in ( ? ");
	     		List<HorarioAcademicoDto> listaHorasNDNew = new ArrayList<HorarioAcademicoDto>();
     			List<HorarioAcademicoDto> listaHorasNDNewAula = new ArrayList<HorarioAcademicoDto>();
	     		
	     		if(listaHorasND.size() >= 1 ){
	     			
	     			
	     			
					for (HorarioAcademicoDto item : listaHorasND) {
						if(item.getHracDia() == hracDia){
							listaHorasNDNew.add(item);
						}
						if(item.getAlaId() != alaId){
							listaHorasNDNewAula.add(item);
						}
					}
					
					if(listaHorasNDNewAula.size() >= 1 ){
						sbSql.append(",");
		     			for (int i = 0; i < listaHorasNDNewAula.size(); i++) {
							sbSql.append(" ? ");
							if(i != listaHorasNDNewAula.size() -1) {
					         sbSql.append(","); 
					        }
						}
						
						sbSql.append(" ) ");
					}else{
						sbSql.append(" ) ");
					}
					
					if(listaHorasNDNew.size() >= 1 ){
						sbSql.append(" AND ");sbSql.append(" hocl1.");sbSql.append(JdbcConstantes.HOCL_ID);sbSql.append(" in ( ");
						
		     			for (int i = 0; i < listaHorasNDNew.size(); i++) {
							sbSql.append(" ? ");
							if(i != listaHorasNDNew.size() -1) {
					         sbSql.append(","); 
					        }
						}
						
						sbSql.append(" ) ");
					}
	     			
	     		}
	     		
//	     		if(listaHorasNDNewAula.size() >= 1 ){
//	     			sbSql.append(" ) ");
//	     		}
	     		
	     		sbSql.append(" ) ");
	
	     		sbSql.append(" AND ");
     		
     		}
     		sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);

     		sbSql.append(" not in ( ");
     		
     		
     		sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" hocl2.");sbSql.append(JdbcConstantes.HOCL_ID);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE_AULA);sbSql.append(" hoclal2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_HORARIO_ACADEMICO);sbSql.append(" hrac2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl2 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac2 ");
			
     		sbSql.append(" WHERE ");sbSql.append(" hocl2.");sbSql.append(JdbcConstantes.HOCL_ID);
     		sbSql.append(" = ");sbSql.append(" hoclal2.");sbSql.append(JdbcConstantes.HOCLAL_HOCL_ID);
     		sbSql.append(" AND ");sbSql.append(" hoclal2.");sbSql.append(JdbcConstantes.HOCLAL_ID);
     		sbSql.append(" = ");sbSql.append(" hrac2.");sbSql.append(JdbcConstantes.HRAC_HOCLAL_ID);
     		sbSql.append(" AND ");sbSql.append(" mlcrpr2.");sbSql.append(JdbcConstantes.MLCRPR_ID);
     		sbSql.append(" = ");sbSql.append(" hrac2.");sbSql.append(JdbcConstantes.HRAC_MLCRPR_ID);
     		sbSql.append(" AND ");sbSql.append(" prl2.");sbSql.append(JdbcConstantes.PRL_ID);
     		sbSql.append(" = ");sbSql.append(" mlcrpr2.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);
     		sbSql.append(" AND ");sbSql.append(" prac2.");sbSql.append(JdbcConstantes.PRAC_ID);
     		sbSql.append(" = ");sbSql.append(" prl2.");sbSql.append(JdbcConstantes.PRL_PRAC_ID);
     		
//     		sbSql.append(" AND ");sbSql.append(" hoclal.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
//     		sbSql.append(" = ? ");
     		sbSql.append(" AND ");sbSql.append(" hrac2.");sbSql.append(JdbcConstantes.HRAC_DIA);
     		sbSql.append(" = ? ");
     		sbSql.append(" AND ");sbSql.append(" prac2.");sbSql.append(JdbcConstantes.PRAC_ID);
     		sbSql.append(" = ? ");
     		sbSql.append(" AND ");sbSql.append(" hoclal2.");sbSql.append(JdbcConstantes.HOCLAL_ALA_ID);
     		sbSql.append(" = ? ");
     		
     		
     		sbSql.append(" ) ");
			
			sbSql.append(" ORDER BY ");sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			pstmt.setInt(1, alaId);
			int contador = 0;
			if(listaHorasND.size() > 0){
				contador = contador + 1;
				pstmt.setInt(contador, hracDia);
				contador = contador + 1;
				pstmt.setInt(contador, pracId);
				contador = contador + 1;
				pstmt.setInt(contador, alaId);
				
				if(listaHorasND.size() >= 1){ // caso para setear la lista de hora clase 
					List<HorarioAcademicoDto> listaHorasNDNew = new ArrayList<HorarioAcademicoDto>();
					List<HorarioAcademicoDto> listaHorasNDNewAula = new ArrayList<HorarioAcademicoDto>();
					
					for (HorarioAcademicoDto item : listaHorasND) {
						if(item.getHracDia() == hracDia){
							listaHorasNDNew.add(item);
						}
						if(item.getAlaId() != alaId){
							listaHorasNDNewAula.add(item);
						}
					}
					if(listaHorasNDNewAula.size() >= 1 ){
						for (HorarioAcademicoDto item : listaHorasNDNewAula) {
							contador = contador + 1;
							pstmt.setInt(contador, item.getAlaId()); //cargo los id de hora clase
						}
					}
					
					if(listaHorasNDNew.size() >= 1 ){
						for (HorarioAcademicoDto item : listaHorasNDNew) {
							contador = contador + 1;
							pstmt.setInt(contador, item.getHoclId()); //cargo los id de hora clase
						}
					}
					
				}
			}
			contador = contador + 1;
			pstmt.setInt(contador, hracDia);
			contador = contador + 1;
			pstmt.setInt(contador, pracId);
			contador = contador + 1;
			pstmt.setInt(contador, alaId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		return  retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las hora clase 
	 * @return Lista de todas las hora clase 
	 * @throws HoraClaseDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HoraClaseDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<HoraClaseDto> listarTodos() throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_DESCRIPCION);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_INICIO);
			sbSql.append(" , hocl.");sbSql.append(JdbcConstantes.HOCL_HORA_FIN);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_HORA_CLASE);sbSql.append(" hocl ");
			
			sbSql.append(" WHERE ");sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ESTADO);sbSql.append(" = ");sbSql.append(HoraClaseConstantes.ESTADO_ACTIVO_VALUE);
			
			sbSql.append(" ORDER BY ");sbSql.append(" hocl.");sbSql.append(JdbcConstantes.HOCL_ID);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		return  retorno;
	}
	
	public List<HoraClaseDto> listarTemplateHorarioClases() throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT  ");sql.append(" HOCL.");sql.append(JdbcConstantes.HOCL_DESCRIPCION);
		sql.append(" , HOCL.");sql.append(JdbcConstantes.HOCL_HORA_INICIO);
		sql.append(" , HOCL.");sql.append(JdbcConstantes.HOCL_HORA_FIN);
		sql.append(" , HOCL.");sql.append(JdbcConstantes.HOCL_ID);
		sql.append(" FROM HORA_CLASE HOCL, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU ");
		sql.append(" WHERE HOCL.HOCL_ID = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID     = HOCLAU.ALA_ID ");
		sql.append(" AND AUL.ALA_ID     = 1 ");
		sql.append(" ORDER BY HOCL.HOCL_HORA_INICIO ");

		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAlistarTemplateHorarioClases(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	@Override
	public List<HoraClaseDto> buscarHorarioAcademico(int mlcrprId) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   HOAC.HRAC_HORA_TIPO, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL ");
		sql.append(" WHERE HOAC.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID    = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID      = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID   = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID     = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID       = HOCLAU.ALA_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID      = ? ");
		sql.append(" ORDER BY HOCL.HOCL_HORA_INICIO ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mlcrprId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarHorarioAcademico(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	@Override
	public List<HoraClaseDto> buscarHorarioAcademico(PersonaDto docente , int periodoId) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   Crr.Crr_Id, ");
		sql.append("   Crr.Crr_Descripcion, ");
		sql.append("   Mtr.Mtr_Id, ");
		sql.append("   Mtr.Mtr_Codigo, ");
		sql.append("   Mtr.Mtr_Descripcion, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   HOAC.HRAC_HORA_TIPO ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MALLA_CURRICULAR MLCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL, ");
		sql.append("   PERSONA PRS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   CARGA_HORARIA CAHR ");
		sql.append(" WHERE HOAC.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID               = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID                 = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID                   = HOCLAU.ALA_ID ");
		sql.append(" AND PRS.PRS_ID                   = Fcdc.Prs_Id ");
		sql.append(" AND Fcdc.Fcdc_Id                 = Dtps.Fcdc_Id ");
		sql.append(" AND Dtps.Dtps_Id                 = Cahr.Dtps_Id ");
		sql.append(" AND Cahr.Mlcrpr_Id               = Hoac.Mlcrpr_Id ");
		sql.append(" AND Mlcrmt.Mlcrmt_Id             = Mlcrpr.Mlcrmt_Id ");
		sql.append(" AND Mtr.Mtr_Id                   = Mlcrmt.Mtr_Id ");
		sql.append(" AND Mlcrmt.Mlcr_Id               = Mlcr.Mlcr_Id ");
		sql.append(" AND Mlcr.Crr_Id                  = Crr.Crr_Id ");
		sql.append(" AND Hoac.Mlcrpr_Id_Comp         IS NULL ");
		sql.append(" AND Cahr.Crhr_Estado             = 0 ");
		sql.append(" AND Cahr.Crhr_Estado_Eliminacion = 1 ");
		sql.append(" AND Prs.Prs_Identificacion       = ? ");
		sql.append(" AND Prac.Prac_Id                 = ? ");
		sql.append(" ORDER BY HOAC.HRAC_DIA, HOCL.HOCL_HORA_INICIO ");


		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, docente.getPrsIdentificacion());
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarHorarioAcademicoPorDocente(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	public List<HoraClaseDto> buscarHorarioFunciones(PersonaDto docente, int periodoId) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   HRFN.HRFN_ID, ");
		sql.append("   HRFN.HRFN_DIA, ");
		sql.append("   HRFN.HRFN_HORA_INICIO, ");
		sql.append("   HRFN.HRFN_HORA_FIN, ");
		sql.append("   HRFN.HRFN_DESCRIPCION, ");
		sql.append("   HRFN.HRFN_FUNCION, ");
		sql.append("   HRFN.HRFN_ACTIVIDAD, ");
		sql.append("   HRFN.HRFN_ESTADO, ");
		sql.append("   HRFN.HRFN_DTPS_ID, ");
		sql.append("   HRFN.HOCL_ID, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN ");
		sql.append(" FROM HORARIO_FUNCION HRFN, ");
		sql.append("   HORA_CLASE HOCL, ");
		sql.append("   PERSONA PRS, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE PRS.PRS_ID           = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID           = DTPS.FCDC_ID ");
		sql.append(" AND DTPS.DTPS_ID           = HRFN.HRFN_DTPS_ID ");
		sql.append(" AND PRAC.PRAC_ID           = HRFN.HRFN_PRAC_ID ");
		sql.append(" AND HOCL.HOCL_ID           = HRFN.HOCL_ID ");
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" AND PRAC.PRAC_ID           = ? ");
		sql.append(" ORDER BY HRFN.HRFN_DIA,  HRFN.HRFN_HORA_INICIO");


		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, docente.getPrsIdentificacion());
			pstmt.setInt(2, periodoId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarHorarioAcademicoFuncionPorDocente(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	public List<HoraClaseDto> buscarCargaHoraria(int periodoId, int paraleloId, int materiaId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   CAHR.CRHR_ID, ");
		sql.append("   CAHR.CRHR_OBSERVACION, ");
		sql.append("   CAHR.CRHR_NUM_HORAS, ");
		sql.append("   CAHR.MLCRPR_ID, ");
		sql.append("   CAHR.MLCRPR_ID_PRINCIPAL, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   CARGA_HORARIA CAHR, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   PERSONA PRS ");
		sql.append(" WHERE MLCRPR.MLCRPR_ID           = CAHR.MLCRPR_ID ");
		sql.append(" AND DTPS.DTPS_ID                 = CAHR.DTPS_ID ");
		sql.append(" AND FCDC.FCDC_ID                 = DTPS.FCDC_ID ");
		sql.append(" AND PRS.PRS_ID                   = FCDC.PRS_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID             = MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID                = MTR.MTR_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND CAHR.CRHR_ESTADO             =  "+CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION =  "+CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND PRL.PRAC_ID                  = ? ");
		sql.append(" AND PRL.PRL_ID                   = ? ");
		sql.append(" AND MTR.MTR_ID                   = ? ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, paraleloId);
			pstmt.setInt(3, materiaId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarCargaHorariaPorPeriodoParaleloMateria(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	
	public List<HoraClaseDto> buscarCargaHorariaPeriodosActivos(int docenteId, int pracEstado)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException{
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL, ");
		sql.append("   CARGA_HORARIA CAHR, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   PERSONA PRS ");
		sql.append(" WHERE HOAC.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID               = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID                 = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID                   = HOCLAU.ALA_ID ");
		sql.append(" AND CAHR.MLCRPR_ID               = MLCRPR.MLCRPR_ID ");
		sql.append(" AND CAHR.DTPS_ID                 = DTPS.DTPS_ID ");
		sql.append(" AND DTPS.FCDC_ID                 = FCDC.FCDC_ID ");
		sql.append(" AND FCDC.PRS_ID                  = PRS.PRS_ID ");
		sql.append(" AND CAHR.CRHR_ESTADO             =  "+CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION =  "+CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND PRS.PRS_ID					  = ? ");
		sql.append(" AND PRAC.PRAC_ESTADO             = ? ");
		sql.append(" ORDER BY HOAC.HRAC_DIA ,  HOCL.HOCL_HORA_INICIO ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, docenteId);
			pstmt.setInt(2, pracEstado);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarCargaHoraria(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	public List<HoraClaseDto> buscarCargaHoraria(int docenteId, int peridoId) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL, ");
		sql.append("   CARGA_HORARIA CAHR, ");
		sql.append("   DETALLE_PUESTO DTPS, ");
		sql.append("   FICHA_DOCENTE FCDC, ");
		sql.append("   PERSONA PRS ");
		sql.append(" WHERE HOAC.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID               = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID                 = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID                   = HOCLAU.ALA_ID ");
		sql.append(" AND CAHR.MLCRPR_ID               = MLCRPR.MLCRPR_ID ");
		sql.append(" AND CAHR.DTPS_ID                 = DTPS.DTPS_ID ");
		sql.append(" AND DTPS.FCDC_ID                 = FCDC.FCDC_ID ");
		sql.append(" AND FCDC.PRS_ID                  = PRS.PRS_ID ");
		sql.append(" AND CAHR.CRHR_ESTADO             =  "+CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND CAHR.CRHR_ESTADO_ELIMINACION =  "+CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append(" AND PRL.PRAC_ID                  = ? ");
		sql.append(" AND PRS.PRS_ID					  = ? ");
		sql.append(" ORDER BY HOAC.HRAC_DIA ,  HOCL.HOCL_HORA_INICIO ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, peridoId);
			pstmt.setInt(2, docenteId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarCargaHoraria(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	
	public List<HoraClaseDto> buscarHorarioAcademicoPorPeriodoParaleloAsignatura(int periodoId,	int paraleloId, int materiaId) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   HOAC.HRAC_HORA_TIPO, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL ");
		sql.append(" WHERE HOAC.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID    = PRL.PRL_ID ");
		sql.append(" AND MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID    = MTR.MTR_ID ");
		sql.append(" AND PRL.PRAC_ID      = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID   = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID     = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID       = HOCLAU.ALA_ID ");
		sql.append(" AND PRL.PRAC_ID      = ? ");
		sql.append(" AND PRL.PRL_ID       = ? ");
		sql.append(" AND MTR.MTR_ID       = ? ");
		sql.append(" ORDER BY HOCL.HOCL_HORA_INICIO ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, paraleloId);
			pstmt.setInt(3, materiaId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarHorarioAcademico(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	public List<HoraClaseDto> buscarDisponibilidad(int aulaId, int diaId,  String horaId, int pracEstado) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL ");
		sql.append(" WHERE HOAC.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID               = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID                 = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID                   = HOCLAU.ALA_ID ");
		sql.append(" AND AUL.ALA_ID 				  = ? ");
		sql.append(" AND HOAC.HRAC_DIA 				  = ? ");
		sql.append(" AND PRAC.PRAC_ESTADO             = ? ");
		sql.append(" AND HOCL.HOCL_HORA_INICIO 	      = ? ");
		sql.append(" ORDER BY HOCL.HOCL_HORA_INICIO ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, aulaId);
			pstmt.setInt(2, diaId);
			pstmt.setInt(3, pracEstado);
			pstmt.setString(4, horaId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarDisponibilidad(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	
	public List<HoraClaseDto> buscarDisponibilidad(int aulaId, String diaId,  String horaId, int pracEstado) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException {
		List<HoraClaseDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   PRL.PRL_ESTADO, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD, ");
		sql.append("   HOAC.HRAC_ID, ");
		sql.append("   HOAC.HRAC_DIA, ");
		sql.append("   HOAC.MLCRPR_ID, ");
		sql.append("   HOAC.MLCRPR_ID_COMP, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
		sql.append(" FROM HORARIO_ACADEMICO HOAC, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   PARALELO PRL, ");
		sql.append("   HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL ");
		sql.append(" WHERE HOAC.MLCRPR_ID             = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append(" AND PRL.PRAC_ID                  = PRAC.PRAC_ID ");
		sql.append(" AND HOAC.HOCLAL_ID               = HOCLAU.HOCLAL_ID ");
		sql.append(" AND HOCL.HOCL_ID                 = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID                   = HOCLAU.ALA_ID ");
		sql.append(" AND AUL.ALA_ID 				  = ? ");
		sql.append(" AND PRL.PRL_CODIGO				  = ? ");
		sql.append(" AND PRAC.PRAC_ESTADO             = ? ");
		sql.append(" AND HOCL.HOCL_HORA_INICIO 	      = ? ");
		sql.append(" ORDER BY HOCL.HOCL_HORA_INICIO ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, aulaId);
			pstmt.setString(2, diaId);
			pstmt.setInt(3, pracEstado);
			pstmt.setString(4, horaId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<HoraClaseDto>();
			while(rs.next()){
				retorno.add(rsAbuscarDisponibilidad(rs));
			}
		} catch (SQLException e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.sql.exception")));
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
		}	
		
		return  retorno;
	}
	
	public HoraClaseDto buscarHoraClaseDto(int aulaId, String horaInicio)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException, HoraClaseDtoValidacionException {
		HoraClaseDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT HOCLAU.HOCLAL_ID, ");
		sql.append("   HOCLAU.HOCLAL_ESTADO, ");
		sql.append("   HOCL.HOCL_ID, ");
		sql.append("   HOCL.HOCL_DESCRIPCION, ");
		sql.append("   HOCL.HOCL_HORA_INICIO, ");
		sql.append("   HOCL.HOCL_HORA_FIN, ");
		sql.append("   AUL.ALA_ID, ");
		sql.append("   AUL.ALA_CODIGO, ");
		sql.append("   AUL.ALA_DESCRIPCION, ");
		sql.append("   AUL.ALA_CAPACIDAD ");
		sql.append(" FROM HORA_CLASE_AULA HOCLAU, ");
		sql.append("   AULA AUL, ");
		sql.append("   HORA_CLASE HOCL ");
		sql.append(" WHERE HOCL.HOCL_ID        = HOCLAU.HOCL_ID ");
		sql.append(" AND AUL.ALA_ID            = HOCLAU.ALA_ID ");
		sql.append(" AND AUL.ALA_ID            = ? ");
		sql.append(" AND HOCL.HOCL_HORA_INICIO = ? ");
		sql.append(" AND HOCLAU.HOCLAL_ESTADO  = ? ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, aulaId);
			pstmt.setString(2, horaInicio);
			pstmt.setInt(3, HoraClaseConstantes.ESTADO_ACTIVO_VALUE);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno = rsAbuscarHoraClaseDto(rs);
			}
			
			
		} catch (NonUniqueResultException e) {
			throw new HoraClaseDtoValidacionException("Se encontró mas de un Horario de Clases con los parámetros ingresados.");
		} catch (Exception e) {
			throw new HoraClaseDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.exception")));
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
		
		if (retorno == null) {
			throw new HoraClaseDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClaseDto.buscar.por.aula.horaDia.periodo.listaHorasND.no.result.exception")));
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
	private HoraClaseDto transformarResultSetADto(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		retorno.setHoclId(rs.getInt(JdbcConstantes.HOCL_ID));
		retorno.setHoclDescripcion(rs.getString(JdbcConstantes.HOCL_DESCRIPCION));
		retorno.setHoclHoraInicio(rs.getString(JdbcConstantes.HOCL_HORA_INICIO));
		retorno.setHoclHoraFin(rs.getString(JdbcConstantes.HOCL_HORA_FIN));
		return retorno;
	}

	private HoraClaseDto rsAlistarTemplateHorarioClases(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		retorno.setHoclDescripcion(rs.getString(JdbcConstantes.HOCL_DESCRIPCION));
		retorno.setHoclHoraInicio(rs.getString(JdbcConstantes.HOCL_HORA_INICIO));
		retorno.setHoclHoraFin(rs.getString(JdbcConstantes.HOCL_HORA_FIN));
		retorno.setHoclId(rs.getInt(JdbcConstantes.HOCL_ID));
		return retorno;
	}

	private HoraClaseDto rsAbuscarDisponibilidad(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		
		ParaleloDto paralelo = new ParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		paralelo.setPrlId(rs.getInt(1));
		paralelo.setPrlCodigo(rs.getString(2));
		paralelo.setPrlDescripcion(rs.getString(3));
		paralelo.setPrlEstado(rs.getInt(4));
		
		retorno.setHoclDescripcion(rs.getString(5));
		retorno.setHoclHoraInicio(rs.getString(6));
		retorno.setHoclHoraFin(rs.getString(7));
		retorno.setAlaId(rs.getInt(8));
		retorno.setAlaCodigo(rs.getString(9));
		retorno.setAlaDescripcion(rs.getString(10));
		retorno.setAlaCapacidad(rs.getString(11));
		retorno.setHracId(rs.getInt(12));
		retorno.setHracDia(rs.getInt(13));
		retorno.setHracMlcrprId(rs.getInt(14));
		retorno.setHracMlcrprIdComp(rs.getInt(15));
		periodo.setPracId(rs.getInt(16));
		periodo.setPracDescripcion(rs.getString(17));
	
		retorno.setHracParaleloDto(paralelo);
		retorno.setHracPeriodoAcademicoDto(periodo);
		
		return retorno;
	}
	
	private HoraClaseDto rsAbuscarCargaHorariaPorPeriodoParaleloMateria(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		
		PersonaDto docente = new PersonaDto();
		CargaHorariaDto carga = new CargaHorariaDto();
		MateriaDto materia = new MateriaDto();
		ParaleloDto paralelo = new ParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();

		
		docente.setPrsId(rs.getInt(1));
		docente.setPrsIdentificacion(rs.getString(2));
		docente.setPrsPrimerApellido(rs.getString(3));
		docente.setPrsSegundoApellido(rs.getString(4));
		docente.setPrsNombres(rs.getString(5));
		
		carga.setCahrId(rs.getInt(6));
		carga.setCahrObservacion(rs.getString(7));
		carga.setCahrNumHoras(rs.getInt(8));
		carga.setCahrMlcrprId(rs.getInt(9));
		carga.setCahrMlcrprIdPrincipal(rs.getInt(10));
		
		materia.setMtrId(rs.getInt(11));
		materia.setMtrDescripcion(rs.getString(12));
		
		paralelo.setPrlId(rs.getInt(13));
		paralelo.setPrlCodigo(rs.getString(14));
		paralelo.setPrlEstado(rs.getInt(15));
		
		periodo.setPracId(rs.getInt(16));
		periodo.setPracDescripcion(rs.getString(17));
		
		retorno.setHoclPersonaDto(docente);
		retorno.setHoclCargaHorariaDto(carga);
		retorno.setHoclMateriaDto(materia);
		retorno.setHracParaleloDto(paralelo);
		retorno.setHracPeriodoAcademicoDto(periodo);
		
		return retorno;
	}
	private HoraClaseDto rsAbuscarCargaHoraria(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		
		PersonaDto docente = new PersonaDto();
		ParaleloDto paralelo = new ParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		docente.setPrsId(rs.getInt(1));
		docente.setPrsIdentificacion(rs.getString(2));
		docente.setPrsPrimerApellido(rs.getString(3));
		docente.setPrsSegundoApellido(rs.getString(4));
		docente.setPrsNombres(rs.getString(5));
		
		paralelo.setPrlId(rs.getInt(6));
		paralelo.setPrlCodigo(rs.getString(7));
		paralelo.setPrlDescripcion(rs.getString(8));
		paralelo.setPrlEstado(rs.getInt(9));
		
		retorno.setHoclDescripcion(rs.getString(10));
		retorno.setHoclHoraInicio(rs.getString(11));
		retorno.setHoclHoraFin(rs.getString(12));
		retorno.setAlaId(rs.getInt(13));
		retorno.setAlaCodigo(rs.getString(14));
		retorno.setAlaDescripcion(rs.getString(15));
		retorno.setAlaCapacidad(rs.getString(16));
		retorno.setHracId(rs.getInt(17));
		retorno.setHracDia(rs.getInt(18));
		retorno.setHracMlcrprId(rs.getInt(19));
		retorno.setHracMlcrprIdComp(rs.getInt(20));
		periodo.setPracId(rs.getInt(21));
		periodo.setPracDescripcion(rs.getString(22));
	
		retorno.setHoclPersonaDto(docente);
		retorno.setHracParaleloDto(paralelo);
		retorno.setHracPeriodoAcademicoDto(periodo);
		
		return retorno;
	}
	
	private HoraClaseDto rsAbuscarHorarioAcademico(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		ParaleloDto paralelo = new ParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		paralelo.setPrlId(rs.getInt(1));
		paralelo.setPrlCodigo(rs.getString(2));
		paralelo.setPrlEstado(rs.getInt(3));
		retorno.setHoclDescripcion(rs.getString(4));
		retorno.setHoclHoraInicio(rs.getString(5));
		retorno.setHoclHoraFin(rs.getString(6));
		retorno.setAlaId(rs.getInt(7));
		retorno.setAlaCodigo(rs.getString(8));
		retorno.setAlaDescripcion(rs.getString(9));
		retorno.setAlaCapacidad(rs.getString(10));
		retorno.setHracId(rs.getInt(11));
		retorno.setHracDia(rs.getInt(12));
		retorno.setHracMlcrprId(rs.getInt(13));
		retorno.setHracMlcrprIdComp(rs.getInt(14));
		retorno.setHracHoraTipo(rs.getInt(15));
		periodo.setPracId(rs.getInt(16));
		periodo.setPracDescripcion(rs.getString(17));
	
		retorno.setHracParaleloDto(paralelo);
		retorno.setHracPeriodoAcademicoDto(periodo);
		
		return retorno;
	}

	private HoraClaseDto rsAbuscarHoraClaseDto(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		
		retorno.setHoclauId(rs.getInt(1));
		retorno.setHoclauEstado(rs.getInt(2));
		retorno.setHoclId(rs.getInt(3));
		retorno.setHoclDescripcion(rs.getString(4));
		retorno.setHoclHoraInicio(rs.getString(5));
		retorno.setHoclHoraFin(rs.getString(6));
		retorno.setAlaId(rs.getInt(7));
		retorno.setAlaCodigo(rs.getString(8));
		retorno.setAlaDescripcion(rs.getString(9));
		retorno.setAlaCapacidad(rs.getString(10));
		
		return retorno;
	}	

	private HoraClaseDto rsAbuscarHorarioAcademicoPorDocente(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		
		
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		CarreraDto carrera = new CarreraDto();
		MateriaDto materia  = new MateriaDto();
		ParaleloDto paralelo = new ParaleloDto();
		
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		
		carrera.setCrrId(rs.getInt(3));
		carrera.setCrrDescripcion(rs.getString(4));

		materia.setMtrId(rs.getInt(5));
		materia.setMtrCodigo(rs.getString(6));
		materia.setMtrDescripcion(rs.getString(7));
		
		paralelo.setPrlId(rs.getInt(8));
		paralelo.setPrlCodigo(rs.getString(9));
		paralelo.setPrlEstado(rs.getInt(10));
		
		retorno.setHracId(rs.getInt(11));
		retorno.setHracDia(rs.getInt(12));
		
		retorno.setHoclDescripcion(rs.getString(13));
		retorno.setHoclHoraInicio(rs.getString(14));
		retorno.setHoclHoraFin(rs.getString(15));
		
		retorno.setHracMlcrprId(rs.getInt(16));
		retorno.setHracMlcrprIdComp(rs.getInt(17));
		retorno.setHracHoraTipo(rs.getInt(18));
		
		retorno.setHracPeriodoAcademicoDto(periodo);
		retorno.setHoclCarreraDto(carrera);
		retorno.setHoclMateriaDto(materia);
		retorno.setHracParaleloDto(paralelo);
		
		return retorno;
	}
	
	private HoraClaseDto rsAbuscarHorarioAcademicoFuncionPorDocente(ResultSet rs) throws SQLException{
		HoraClaseDto retorno = new HoraClaseDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		HorarioFuncionDto horario = new HorarioFuncionDto();
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		
		retorno.setHracId(rs.getInt(3));
		retorno.setHracDia(rs.getInt(4));
		
		horario.setHrfnHoraInicio(rs.getInt(5));
		horario.setHrfnHoraFin(rs.getInt(6));
		horario.setHrfnDescripcion(rs.getString(7));
		horario.setHrfnFuncion(rs.getInt(8));
		horario.setHrfnActividad(rs.getInt(9));
		horario.setHrfnEstado(rs.getInt(10));
		horario.setHrfnDetallePuesto(rs.getInt(11));
		
		retorno.setHoclId(rs.getInt(12));	
		retorno.setHoclDescripcion(rs.getString(13));
		retorno.setHoclHoraInicio(rs.getString(14));
		retorno.setHoclHoraFin(rs.getString(15));
		
		retorno.setHoclHorarioFuncionDto(horario);
		retorno.setHracPeriodoAcademicoDto(periodo);
		
		return retorno;
	}
}
