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
   
 ARCHIVO:     PlanificacionCronogramaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc del DTO PlanificacionCronograma.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
07-09-2017            Arturo Villafuerte                    Emisión Inicial
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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.ProcesoFlujoDto;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB PlanificacionCronogramaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc del DTO PlanificacionCronograma.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class PlanificacionCronogramaDtoServicioJdbcImpl implements PlanificacionCronogramaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Busca por id de proceso flujo para encontrar PlanificacionCronogramaDTO con todos los datos del cronograma
	 * @param idProcesoFlujo
	 * @return Dto PlanificacionCronogramaDto con la informacion del cronograma
	 * @throws PlanificacionCronogramaDtoNoEncontradoException PlanificacionCronogramaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PlanificacionCronogramaDtoException PlanificacionCronogramaDtoException excepción general lanzada
	 */
	public PlanificacionCronogramaDto buscarCronogramaXProcesoFlujo(int idProcesoFlujo) throws PlanificacionCronogramaDtoNoEncontradoException, PlanificacionCronogramaDtoException{
		
		PlanificacionCronogramaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder(); 
			
					sbSql.append("    SELECT DISTINCT ");
					sbSql.append(" PLCR.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
					sbSql.append(" , PLCR.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
					 
					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" CRN ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" CRPRFL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" PRFL ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" PLCR ");

					sbSql.append(" WHERE");
					
					sbSql.append(" CRPRFL.");sbSql.append(JdbcConstantes.CRN_ID); 
					sbSql.append(" = ");
					sbSql.append(" CRN.");sbSql.append(JdbcConstantes.CRN_ID); 
					sbSql.append(" AND");
					sbSql.append(" PLCR.");sbSql.append(JdbcConstantes.CRPRFL_ID); 
					sbSql.append(" = ");
					sbSql.append(" CRPRFL.");sbSql.append(JdbcConstantes.CRPRFL_ID);  
					sbSql.append(" AND");
					sbSql.append(" CRPRFL.");sbSql.append(JdbcConstantes.PRFL_ID); 
					sbSql.append(" = ");
					sbSql.append(" PRFL.");sbSql.append(JdbcConstantes.PRFL_ID); 
					sbSql.append(" AND");
					sbSql.append(" CRN.");sbSql.append(JdbcConstantes.CRN_ESTADO);  
					sbSql.append(" = ");
					sbSql.append(" 0 ");
					sbSql.append(" AND");
					sbSql.append(" PRFL.");sbSql.append(JdbcConstantes.PRFL_ESTADO); 
					sbSql.append(" = ");
					sbSql.append(" 0 ");
					sbSql.append(" AND");
					sbSql.append(" PLCR.");sbSql.append(JdbcConstantes.PLCR_ESTADO); 
					sbSql.append(" = ");
					sbSql.append(" 0 ");
					sbSql.append(" AND");
					sbSql.append(" PRFL.");sbSql.append(JdbcConstantes.PRFL_ID); 
					sbSql.append(" = ");
					sbSql.append(idProcesoFlujo);
					
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs = pstmt.executeQuery();
					if(rs.next()){
						retorno = transformarResultSetADto(rs);
					}else{
						retorno = null;
					}
			
			
		} catch (SQLException e) {  
			throw new PlanificacionCronogramaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.sql.exception")));
		} catch (Exception e) { 
			throw new PlanificacionCronogramaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.exception")));
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
			throw new PlanificacionCronogramaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.no.result.exception")));
		}
		return retorno;
	}
	
	/**MQ
	 * Busca PlanificacionCronogramaDTO  por procesoFlujo, tipoCronograma y periodo
	 * @param idPrfl- id del proceso flujo
	 * @param tipoCrn - tipo de cronograma
	 * @param idPeriodo -periodo academico
	 * @return Dto PlanificacionCronogramaDto con la informacion del cronograma
	 * @throws PlanificacionCronogramaDtoNoEncontradoException PlanificacionCronogramaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PlanificacionCronogramaDtoException PlanificacionCronogramaDtoException excepción general lanzada
	 */
	public PlanificacionCronogramaDto buscarXProcesoFlujoXTipoCrnXPeriodo(int idPrfl, int tipoCrn,int idPeriodo) throws PlanificacionCronogramaDtoNoEncontradoException, PlanificacionCronogramaDtoException{
		
		PlanificacionCronogramaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder(); 
		
			
					sbSql.append("SELECT DISTINCT ");
					sbSql.append(" PLCR.");sbSql.append(JdbcConstantes.PLCR_ID);
					sbSql.append(" , PLCR.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
					sbSql.append(" , PLCR.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
					sbSql.append(" , PLCR.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
					sbSql.append(" FROM ");
					                    sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");

					sbSql.append(" WHERE");
					
					sbSql.append(" plcr.");sbSql.append(JdbcConstantes.PLCR_CRPRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID); 
					sbSql.append(" AND");
					sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_PRFL_ID);sbSql.append(" = ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID); 
					sbSql.append(" AND");
					sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRPRFL_CRN_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_ID); 
					sbSql.append(" AND");
					sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_PRAC_ID);sbSql.append(" = ");	sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID); 
					sbSql.append(" AND");
					sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" =  ?");
					sbSql.append(" AND");
					sbSql.append(" crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" = ? ");
					sbSql.append(" AND");
					sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					pstmt.setInt(1, idPrfl);
					pstmt.setInt(2, tipoCrn);
					pstmt.setInt(3, idPeriodo);
					
					rs = pstmt.executeQuery();
					if(rs.next()){
						retorno = transformarResultSetADtoBuscarXProcesoFlujoXTipoCrnXPeriodo(rs);
					}else{
						retorno = null;
					}
			
		} catch (SQLException e) {  
			throw new PlanificacionCronogramaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.sql.exception")));
		} catch (Exception e) { 
			throw new PlanificacionCronogramaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.exception")));
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
			throw new PlanificacionCronogramaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.no.result.exception")));
		}
		return retorno;
	}
	
	
	public List<PlanificacionCronogramaDto> buscarCronogramaProcesos(Integer[] pracTipo, Integer[] crnTipo, Integer estado) throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException {
		List<PlanificacionCronogramaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		
		sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
		
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
		
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);

		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
		
		sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
		sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
		sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
		sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
		sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
		
		sbSql.append(" WHERE ");
		sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
		sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);sbSql.append(" in  ");sbSql.append(Arrays.toString(pracTipo).replace("[", "(").replace("]", ")"));	
		sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" in ");sbSql.append(Arrays.toString(crnTipo).replace("[", "(").replace("]", ")"));
		sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");sbSql.append(estado);

		sbSql.append(" ORDER BY ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);

		
		try {
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetProcesoPeriodoTipoADto(rs));
			}
			
		} catch (SQLException e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.sql.exception")));
		} catch (Exception e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.sql.exception")));
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
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	

	public 	List<PlanificacionCronogramaDto> buscarCronogramaProcesosPorPeriodo(int  pracId) throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException {
		List<PlanificacionCronogramaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		
		sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
		
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
		
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);

		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
		
		sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
		sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
		sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
		sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
		sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
		
		sbSql.append(" WHERE ");
		sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
		sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ?  ");	

		sbSql.append(" ORDER BY ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);

		
		try {
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, pracId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetProcesoPeriodoTipoADto(rs));
			}
			
		} catch (SQLException e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.sql.exception")));
		} catch (Exception e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.sql.exception")));
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
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades( "PlanificacionCronogramaDto.buscar.cronograma.proceso.flujo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	
	public 	PlanificacionCronogramaDto buscarPlanificacion(int  periodoId, int cronogramaTipo, int procesoId) throws PlanificacionCronogramaValidacionException, PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException {
		PlanificacionCronogramaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		
		sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_TIPO);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_INCIO);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_FECHA_FIN);
		sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
		
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_TIPO);
		sbSql.append(" ,crn.");sbSql.append(JdbcConstantes.CRN_DESCRIPCION);
		
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);
		sbSql.append(" ,prfl.");sbSql.append(JdbcConstantes.PRFL_ESTADO);

		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ID);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_ESTADO);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_INICIO);
		sbSql.append(" ,plcr.");sbSql.append(JdbcConstantes.PLCR_FECHA_FIN);
		
		sbSql.append(" ,crprfl. ");sbSql.append(JdbcConstantes.CRPRFL_ORDINAL);
		
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA);sbSql.append(" crn, ");
		sbSql.append(JdbcConstantes.TABLA_CRONOGRAMA_PROCESO_FLUJO);sbSql.append(" crprfl, ");
		sbSql.append(JdbcConstantes.TABLA_PROCESO_FLUJO);sbSql.append(" prfl, ");
		sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
		sbSql.append(JdbcConstantes.TABLA_PLANIFICACION_CRONOGRAMA);sbSql.append(" plcr ");
		
		sbSql.append(" WHERE ");
		sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ");sbSql.append(" crn.");sbSql.append(JdbcConstantes.PRAC_ID);
		sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.CRN_ID);
		sbSql.append(" AND prfl.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ");sbSql.append(" crprfl.");sbSql.append(JdbcConstantes.PRFL_ID);
		sbSql.append(" AND crprfl.");sbSql.append(JdbcConstantes.CRPRFL_ID);sbSql.append(" = ");sbSql.append(" plcr.");sbSql.append(JdbcConstantes.CRPRFL_ID);
		sbSql.append(" AND prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ?  ");
		sbSql.append(" AND crn.");sbSql.append(JdbcConstantes.CRN_TIPO);sbSql.append(" =  ? ");
		sbSql.append(" AND PRFL.");sbSql.append(JdbcConstantes.PRFL_ID);sbSql.append(" = ? ");

		sbSql.append(" ORDER BY ");sbSql.append(" prfl.");sbSql.append(JdbcConstantes.PRFL_DESCRIPCION);

		
		try {
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, cronogramaTipo);
			pstmt.setInt(3, procesoId);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno = transformarResultSetProcesoPeriodoTipoADto(rs);
			}
		
		} catch (NoResultException e) {
			throw new PlanificacionCronogramaNoEncontradoException("No se encontró el proceso solicitado.");
		} catch (NonUniqueResultException e) {
			throw new PlanificacionCronogramaValidacionException("Se encontró mas de un proceso vinculado al período académico solicitado.");
		} catch (Exception e) {
			throw new PlanificacionCronogramaException("Error tipo sql, comuníquese con el administrador del sistema.");
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

	/**
	 * MÃ©todo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parÃ¡metros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException ExcepciÃ³n de error en la consulta sql
	 */
	private PlanificacionCronogramaDto transformarResultSetADto(ResultSet rs) throws SQLException{
		PlanificacionCronogramaDto retorno = new PlanificacionCronogramaDto();
		
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));
	
		return retorno;
	}


	/**
	 * Metodo privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parametros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private PlanificacionCronogramaDto transformarResultSetADtoBuscarXProcesoFlujoXTipoCrnXPeriodo(ResultSet rs) throws SQLException{
		PlanificacionCronogramaDto retorno = new PlanificacionCronogramaDto();
		
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));
	
		return retorno;
	}
	
	
	private PlanificacionCronogramaDto transformarResultSetProcesoPeriodoTipoADto(ResultSet rs) throws SQLException {
		PlanificacionCronogramaDto retorno = new PlanificacionCronogramaDto();
		CronogramaDto cronograma = new CronogramaDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		ProcesoFlujoDto proceso = new ProcesoFlujoDto();
		
		periodo.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		periodo.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		periodo.setPracTipo(rs.getInt(JdbcConstantes.PRAC_TIPO));
		periodo.setPracFechaIncio(rs.getDate(JdbcConstantes.PRAC_FECHA_INCIO));
		periodo.setPracFechaFin(rs.getDate(JdbcConstantes.PRAC_FECHA_FIN));
		periodo.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));
		periodo.setCrprflOrdinal(rs.getInt(JdbcConstantes.CRPRFL_ORDINAL));
		cronograma.setCrnId(rs.getInt(JdbcConstantes.CRN_ID));
		cronograma.setCrnTipo(rs.getInt(JdbcConstantes.CRN_TIPO));
		cronograma.setCrnDescripcion(rs.getString(JdbcConstantes.CRN_DESCRIPCION));
		proceso.setPrflId(rs.getInt(JdbcConstantes.PRFL_ID));
		proceso.setPrflDescripcion(rs.getString(JdbcConstantes.PRFL_DESCRIPCION));
		proceso.setPrflEstado(rs.getInt(JdbcConstantes.PRFL_ESTADO));
		retorno.setPlcrId(rs.getInt(JdbcConstantes.PLCR_ID));
		retorno.setPlcrEstado(rs.getInt(JdbcConstantes.PLCR_ESTADO));
		retorno.setPlcrFechaInicio(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFin(rs.getTimestamp(JdbcConstantes.PLCR_FECHA_FIN));
		retorno.setPlcrFechaInicial(rs.getDate(JdbcConstantes.PLCR_FECHA_INICIO));
		retorno.setPlcrFechaFinal(rs.getDate(JdbcConstantes.PLCR_FECHA_FIN));
		retorno.setPlcrPeriodoAcademicoDto(periodo);
		retorno.setPlcrCronogramaDto(cronograma);
		retorno.setPlcrProcesoFlujoDto(proceso);
		
		return retorno;
	} 
}

