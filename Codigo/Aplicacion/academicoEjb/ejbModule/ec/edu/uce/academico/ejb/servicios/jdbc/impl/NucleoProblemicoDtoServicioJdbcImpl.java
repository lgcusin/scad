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
   
 ARCHIVO:     NucleoProblemicoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc de la tabla Núcleo Problémico.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-Agosto-2017		Marcelo Quishpe				       Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.NucleoProblemicoDto;
import ec.edu.uce.academico.ejb.excepciones.NucleoProblemicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.NucleoProblemicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NucleoProblemicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB NucleoProblemicoDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Núcleo Problémico.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class NucleoProblemicoDtoServicioJdbcImpl implements NucleoProblemicoDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de todas los núcleos problemicos que pertenecen a la carrera indicada
	 * @param carreraId - id de la carrera  a utilizar como filtro
	 * @return Lista de núcleos problemicos pertenecientes a la carrera
	 * @throws NucleoProblemicoDtoException- Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NucleoProblemicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<NucleoProblemicoDto> listarXcarrera(int carreraId) throws NucleoProblemicoDtoException, NucleoProblemicoDtoNoEncontradoException{
		List<NucleoProblemicoDto> retorno= null;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" ncpr.");sbSql.append(JdbcConstantes.NCPR_ID);
			sbSql.append(" , ncpr.");sbSql.append(JdbcConstantes.NCPR_DESCRIPCION);
			sbSql.append(" , ncpr.");sbSql.append(JdbcConstantes.NCPR_ESTADO);
			sbSql.append(" , ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_ID);
			sbSql.append(" , ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_ESTADO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_NUCLEO_PROBLEMICO);sbSql.append(" ncpr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_NUCLEO_PROBLEMICO_CARRERA);sbSql.append(" ncprcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
     		sbSql.append(" WHERE ");sbSql.append(" ncpr.");sbSql.append(JdbcConstantes.NCPR_ID);
			sbSql.append(" = ");sbSql.append(" ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_NCPR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" ncprcr.");sbSql.append(JdbcConstantes.NCPRCR_CRR_ID);
			sbSql.append(" = ? ");		
		    sbSql.append(" ORDER BY ");sbSql.append(" ncpr.");sbSql.append(JdbcConstantes.NCPR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<NucleoProblemicoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXCarrera(rs));
			}
		} catch (SQLException e) {
			throw new NucleoProblemicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NucleoProblemicoDto.buscar.por.carrera.sql.exception")));
		} catch (Exception e) {
			throw new NucleoProblemicoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NucleoProblemicoDto.buscar.por.carrera.exception")));
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
			throw new NucleoProblemicoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NucleoProblemicoDto.buscar.por.carrera.no.result.exception")));
		}	
		return  retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset XCarrera
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private NucleoProblemicoDto transformarResultSetADtoXCarrera(ResultSet rs) throws SQLException{
		NucleoProblemicoDto retorno = new NucleoProblemicoDto();
		 retorno.setNcprId(rs.getInt(JdbcConstantes.NCPR_ID));
		  retorno.setNcprDescripcion(rs.getString(JdbcConstantes.NCPR_DESCRIPCION));
		  retorno.setNcprEstado(rs.getInt(JdbcConstantes.NCPR_ESTADO));
		  retorno.setNcprcrId(rs.getInt(JdbcConstantes.NCPRCR_ID));
		  retorno.setNcprcrEstado(rs.getInt(JdbcConstantes.NCPRCR_ESTADO));
		  retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		  retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		  retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		return retorno;
	} 
	
	
	
	
	
}
