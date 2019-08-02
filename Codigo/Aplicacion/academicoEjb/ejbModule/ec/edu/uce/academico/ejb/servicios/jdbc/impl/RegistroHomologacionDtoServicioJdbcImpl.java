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
   
 ARCHIVO:     RegistroHomologacionDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los métodos para el servicio jdbc del registro de homologacion.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-ABR-2018		Marcelo Quishpe				       Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RegistroHomologacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB AulaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Aula.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class RegistroHomologacionDtoServicioJdbcImpl implements RegistroHomologacionDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/**
	 * Realiza la busqueda de los estudiantes que se le ha realizado homologación Estudiantes con ROL de pregrado. (pregrado y suficiencias)
	 * @param crrId - id de la carrera por la cual se va buscar 
	 * @param identificacionId - identificacion del estudiante a buscar
	 * @return Lista de los estudiantes  homologados en la carrera
	 * @throws RegistroHomologacionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistroHomologacionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RegistroHomologacionDto> listarXEstudiantesXcarreraXIdentificacion(int crrId, String identificacion) throws RegistroHomologacionDtoException, RegistroHomologacionDtoNoEncontradoException{
		List<RegistroHomologacionDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" , usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);	
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_VIGENTE);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" , cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" , cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" , grt.");sbSql.append(JdbcConstantes.GRT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_GRATUIDAD);sbSql.append(" grt ");
     		sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);	
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" grt.");sbSql.append(JdbcConstantes.GRT_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
			
			  
			  if(crrId != GeneralesConstantes.APP_ID_BASE){// Si se a seleccioando un edificio, , se incluye la busqueda por este filtro 
				  sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" = ? ");	
				}
			  
			  sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" like ? ");
				
			  sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			  
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
		
             Integer aux =0;
			if(crrId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
				pstmt.setInt(++aux, crrId);
			}
					
			pstmt.setString(++aux, "%"+identificacion.toUpperCase()+"%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RegistroHomologacionDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXEstudiantesXcarreraXIdentificacion(rs));
			}
		} catch (SQLException e) {
			
				throw new RegistroHomologacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.carrera.identificacion.sql.exception")));
		} catch (Exception e) {
		
			throw new RegistroHomologacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.carrera.identificacion.exception")));
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
			throw new RegistroHomologacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.carrera.identificacion.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	/**
	 * Realiza la busqueda del estudiantes por el id identificación para listar las carreras en las que esta inscrito
	 * @param identificacionId - identificacion del estudiante a buscar
	 * @return lista de estudiante con carreras inscrito
	 * @throws RegistroHomologacionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistroHomologacionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RegistroHomologacionDto> listarXEstudianteXIdentificacion(String identificacion) throws RegistroHomologacionDtoException, RegistroHomologacionDtoNoEncontradoException{
		List<RegistroHomologacionDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);	
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_NOTA_ENES);
			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" , usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);	
			//sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_FCIN_ANTERIOR_ID);
			sbSql.append(" , CASE WHEN fcin.");sbSql.append(JdbcConstantes.FCIN_FCIN_ANTERIOR_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fcin.");sbSql.append(JdbcConstantes.FCIN_FCIN_ANTERIOR_ID); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCIN_FCIN_ANTERIOR_ID);
			
			
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			
			//sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			
			sbSql.append(" , CASE WHEN fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCIN_TIPO);
			
			//sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" , CASE WHEN fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			
		
			//sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_VIGENTE);
			
			sbSql.append(" , CASE WHEN fcin.");sbSql.append(JdbcConstantes.FCIN_VIGENTE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fcin.");sbSql.append(JdbcConstantes.FCIN_VIGENTE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCIN_VIGENTE);
			
			sbSql.append(" , cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);	
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			
//			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" = ");sbSql.append(RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" in ( " );sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
			sbSql.append(" ,");	sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
			sbSql.append(" ,");	sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_VALUE);
			sbSql.append(" ,");	sbSql.append(FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE);
			sbSql.append(" ) " );
					
		    sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" like ? ");
				
			  sbSql.append(" ORDER BY ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			  
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
				
			pstmt.setString(1, "%"+identificacion.toUpperCase()+"%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RegistroHomologacionDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXEstudianteXIdentificacion(rs));
			}
		} catch (SQLException e) {
			
				throw new RegistroHomologacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.identificacion.sql.exception")));
		} catch (Exception e) {
		
			throw new RegistroHomologacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.identificacion.exception")));
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
			throw new RegistroHomologacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.identificacion.no.result.exception")));
		}	
	
		
		return  retorno;
	}
	
	
	
	
	
	
	/**MQ
	 * Realiza la busqueda de los estudiantes que se le ha realizado homologación con lista de carreras, si selecciona todas o consulta por carrera SOLO PREGRADO(usa periodo homologación)
	 * @param crrId - id de la carrera por la cual se va buscar 
	 * @param identificacionId - identificacion del estudiante a buscar
	 * @return Lista de los estudiantes  homologados en la carrera
	 * @throws RegistroHomologacionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistroHomologacionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RegistroHomologacionDto> listarXEstudiantesXListaCarreraXIdentificacion(List<CarreraDto> listCarreraDto, int crrId, String identificacion) throws RegistroHomologacionDtoException, RegistroHomologacionDtoNoEncontradoException{
		List<RegistroHomologacionDto> retorno= null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" , usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);	
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_VIGENTE);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" , cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" , fcmt.");sbSql.append(JdbcConstantes.FCMT_FECHA_MATRICULA);
			sbSql.append(" , cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
			sbSql.append(" , grt.");sbSql.append(JdbcConstantes.GRT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_GRATUIDAD);sbSql.append(" grt ");
     		sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);	
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = ");sbSql.append(" grt.");sbSql.append(JdbcConstantes.GRT_FCMT_ID);
			sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
			sbSql.append(" = ");sbSql.append(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
			
			  
			  if(crrId != GeneralesConstantes.APP_ID_BASE){// Si se a seleccioando un edificio, , se incluye la busqueda por este filtro 
				  sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" = ? ");	
				}else{
					sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
					
					for (int i = 0; i < listCarreraDto.size(); i++) {
						sbSql.append(" ? ");
						if(i != listCarreraDto.size() -1) {
							sbSql.append(","); 
						}
					}
					sbSql.append(" ) ");
				}
			  
			  sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" like ? ");
				
			  sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			//System.out.println(sbSql.toString());
		
             Integer aux =0;
			if(crrId != GeneralesConstantes.APP_ID_BASE){  // Si  se a seleccionado una Dependecia, se incluye la busqueda por este filtro
				pstmt.setInt(++aux, crrId);
			}else{
				for (CarreraDto item : listCarreraDto) {
					aux++;
					pstmt.setInt(aux, item.getCrrId());
				}
				
			}
			
			pstmt.setString(++aux, "%"+identificacion.toUpperCase()+"%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<RegistroHomologacionDto>();
			
			while(rs.next()){
				retorno.add(transformarResultSetADtolistarXEstudiantesXcarreraXIdentificacion(rs));
			}
		} catch (SQLException e) {
			
				throw new RegistroHomologacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.carrera.identificacion.sql.exception")));
		} catch (Exception e) {
		
			throw new RegistroHomologacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.carrera.identificacion.exception")));
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
			throw new RegistroHomologacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacionDto.buscar.por.carrera.identificacion.no.result.exception")));
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
	private RegistroHomologacionDto transformarResultSetADtolistarXEstudiantesXcarreraXIdentificacion(ResultSet rs) throws SQLException{
		RegistroHomologacionDto retorno = new RegistroHomologacionDto();
    	  retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		  retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		  retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		  retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		  retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES)); 
		  retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		  retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		  retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		  retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		  retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		  retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		  retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		  retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		  retorno.setFcinVigente(rs.getInt(JdbcConstantes.FCIN_VIGENTE));
		  retorno.setFcinTipoIngreso(rs.getInt(JdbcConstantes.FCIN_TIPO_INGRESO));
		  retorno.setFcinEstadoIngreso(rs.getInt(JdbcConstantes.FCIN_ESTADO_INGRESO));
		  retorno.setFcinPeriodoAcademico(rs.getInt(JdbcConstantes.FCIN_PRAC_ID));
		  retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		  retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		  retorno.setFcmtId(rs.getInt(JdbcConstantes.FCMT_ID));
		  retorno.setFcmtPracId(rs.getInt(JdbcConstantes.FCMT_PRAC_ID));
		  retorno.setFcmtFechaMatricula(rs.getTimestamp(JdbcConstantes.FCMT_FECHA_MATRICULA));		  
		  retorno.setCmpaId(rs.getInt(JdbcConstantes.CMPA_ID));
		  retorno.setGrtId(rs.getInt(JdbcConstantes.GRT_ID));
		
		  
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset XUsuarioXdependenciaXEdificio
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private RegistroHomologacionDto transformarResultSetADtolistarXEstudianteXIdentificacion(ResultSet rs) throws SQLException{
		RegistroHomologacionDto retorno = new RegistroHomologacionDto();
		
    	  retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		  retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		  retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		  retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		  retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		  retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		  retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		  retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		  retorno.setFcinNotaEnes(rs.getFloat(JdbcConstantes.FCIN_NOTA_ENES));
		//coloco idCarrera en el campo CarreraSiiu pues no todas las fichas inscripcion tienen lleno el campo
		  retorno.setFcinCarreraSiiu(rs.getInt(JdbcConstantes.CRR_ID));  
		  retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		  retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		  retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		  retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		  retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		  retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
		  retorno.setFcinFcinAnteriorId(rs.getInt(JdbcConstantes.FCIN_FCIN_ANTERIOR_ID));
		  retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		  
		  retorno.setFcinTipoIngreso(rs.getInt(JdbcConstantes.FCIN_TIPO_INGRESO));
		  retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));
		  retorno.setFcinVigente(rs.getInt(JdbcConstantes.FCIN_VIGENTE));
		  
		  retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		  retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		  retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
		  
		return retorno;
	} 
	
	
	
}
