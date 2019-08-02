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
   
 ARCHIVO:     NotasPregradoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de notas. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-jun-2017 		  Marcelo Quishpe			          Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPosgradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB NotasPregradoDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de notas.
 * @author lmquishpei.
 * @version 1.0
 */

@Stateless
public class NotasPosgradoDtoServicioJdbcImpl implements NotasPosgradoDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
			
			/**
			 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
			 * @param idPeriodo - idPeriodo id del periodo academico a buscar
			 * @param idCarrera - idCarrera id de la carrera a buscar
			 * @param idNivel - idNivel id del nivel a buscar
			 * @param idParalelo - idParalelo id del paralelo a buscar
			 * @param idMateria - idMateria id de la materia a buscar
			 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
			 * @return - retorna una lista de estudiantes con los parametros ingresados
			 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
			 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
			 */
			public List<EstudianteJdbcDto> buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente( int idCarrera, int idNivel, int idParalelo, int idMateria , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException{
				List<EstudianteJdbcDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {

					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT");
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
								sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
								sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
					sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);					
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);					
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);						
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);					
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);					
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);				
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);					
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA2);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ID); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ID);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
					
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_TOTAL_ASISTENCIA1);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_TOTAL_ASISTENCIA2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL);

					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);

					sbSql.append(" FROM ");
					
					sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
					
					sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
					sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa, ");
					sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt, ");
					sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
					sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
					sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
					sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
					sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
					sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
					sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn, ");
					sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac, ");
					sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr, ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin, ");
					sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ");
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ON ");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
					
					sbSql.append(" where prs.prs_id=fces.prs_id and fces.fces_id=fcmt.fces_id and dtmt.mlcrpr_id=mlcrpr.mlcrpr_id and mlcrpr.mlcrmt_id=mlcrmt.mlcrmt_id"+
							" and mtr.mtr_id=mlcrmt.mtr_id and mlcrpr.prl_id=prl.prl_id and nvl.nvl_id=mlcrmt.nvl_id and prac.prac_id=prl.prac_id"+
							" and  mtr.crr_id=cncr.crr_id and fcin.fcin_id=fces.fcin_id and cmpa.fcmt_id=fcmt.fcmt_id and cmpa.cmpa_id=dtmt.cmpa_id"+
							" and fces.fces_id = rces.fces_id"+
							" and dpn.dpn_id = crr.dpn_id"+
							" and crr.crr_id = cncr.crr_id"+
							" and mlcrpr.mlcrpr_id = rces.mlcrpr_id ");
					sbSql.append(" AND ");

					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");

					sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(idRcesMlcrpr);
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
					sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
					sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
					sbSql.append(" ) ");
					
					
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ? ");
					}
					sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);sbSql.append(" = ");sbSql.append(FichaInscripcionConstantes.ACTIVO_VALUE);
					
					sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					
					//FIN QUERY
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					pstmt.setInt(1, idRcesMlcrpr); //cargo el id malla curricular paralelo : es el id de recod estudiante
					int contador = 1;
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						contador ++;
//						pstmt.setInt(contador, idPeriodo); //cargo el id del periodo academico
//					}
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idCarrera); //cargo el id de carrera
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idNivel); //cargo el id de nivel
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idParalelo); //cargo el id de paralelo
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idMateria); //cargo el id de la materia
					}
//					System.out.println(sbSql);
//					System.out.println(idRcesMlcrpr);
//					System.out.println(idCarrera);
//					System.out.println(idNivel);
//					System.out.println(idParalelo);
//					System.out.println(idMateria);
					rs = pstmt.executeQuery();
					retorno = new ArrayList<EstudianteJdbcDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoEstudiantesPeriodoCarreraNivelMateria(rs));
					}

				} catch (SQLException e) {
					e.printStackTrace();
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.sql.exception")));
				} catch (Exception e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.exception")));
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
					throw new EstudianteDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.no.result.exception")));
				}
				return retorno;
			}	

			
			
			/**
			 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
			 * @param idPeriodo - idPeriodo id del periodo academico a buscar
			 * @param idCarrera - idCarrera id de la carrera a buscar
			 * @param idNivel - idNivel id del nivel a buscar
			 * @param idParalelo - idParalelo id del paralelo a buscar
			 * @param idMateria - idMateria id de la materia a buscar
			 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
			 * @return - retorna una lista de estudiantes con los parametros ingresados
			 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
			 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
			 */
			public List<EstudianteJdbcDto> buscarEstudianteRecuperacionXperiodoXcarreraXnivelXparaleloXmateriaXdocente( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException{
				List<EstudianteJdbcDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {

					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT");
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
								sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
								sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
					sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);					
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);					
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);						
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);					
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);					
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);				
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);					
					sbSql.append(" ,dtps.");sbSql.append(JdbcConstantes.DTPS_ID);	
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA2);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_ID);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
					
//					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(" -99 ");
//					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
//					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
					
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_TOTAL_ASISTENCIA1);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_TOTAL_ASISTENCIA2);
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL);

					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					
					
					
					
					sbSql.append(" FROM ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ON ");
					sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ON ");
					sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ON ");
					sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ON ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ON ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ON ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);	
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ON ");
					sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
					sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ON ");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ON ");
					sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ON ");
					
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
									
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
					sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ON ");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
					

					sbSql.append(" WHERE "); 
					// Se puede usar hasta detalle puesto_id
                    //sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);sbSql.append(" = ? ");

					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ? ");
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//					}
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
					sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ? ");
					}

					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
					sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
					sbSql.append(" ) ");
					
					sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					//FIN QUERY
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					pstmt.setInt(1, idDocente); //cargo el id docente : es el id de ficha docente
					pstmt.setInt(2, idRcesMlcrpr); //cargo el id malla curricular paralelo : es el id de recod estudiante
					int contador = 2;
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						contador ++;
//						pstmt.setInt(contador, idPeriodo); //cargo el id del periodo academico
//					}
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idCarrera); //cargo el id de carrera
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idNivel); //cargo el id de nivel
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idParalelo); //cargo el id de paralelo
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idMateria); //cargo el id de la materia
					}

					rs = pstmt.executeQuery();
					retorno = new ArrayList<EstudianteJdbcDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoEstudiantesPeriodoCarreraNivelMateria(rs));
					}

				} catch (SQLException e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.sql.exception")));
				} catch (Exception e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.exception")));
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
					throw new EstudianteDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.no.result.exception")));
				}
				return retorno;
			}	

			
			/**
			 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
			 * @param idPeriodo - idPeriodo id del periodo academico a buscar
			 * @param idCarrera - idCarrera id de la carrera a buscar
			 * @param idNivel - idNivel id del nivel a buscar
			 * @param idParalelo - idParalelo id del paralelo a buscar
			 * @param idMateria - idMateria id de la materia a buscar
			 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
			 * @return - retorna una lista de estudiantes con los parametros ingresados
			 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
			 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
			 */
			public List<EstudianteJdbcDto> buscarEstudianteRecuperacionReporte( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException{
				List<EstudianteJdbcDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {

					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT");
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
								sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
								sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
					sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);					
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);					
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);						
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);					
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);					
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);				
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);					
					sbSql.append(" ,dtps.");sbSql.append(JdbcConstantes.DTPS_ID);	
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA2);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_ID);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
					
//					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(" -99 ");
//					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
//					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
					
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA1);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_TOTAL_ASISTENCIA1);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_TOTAL_ASISTENCIA2);
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					
//					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL);

					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					
					
					
					
					sbSql.append(" FROM ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ON ");
					sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ON ");
					sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ON ");
					sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ON ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ON ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ON ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);	
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ON ");
					sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
					sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ON ");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ON ");
					sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ON ");
					
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
									
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
					sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ON ");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
					

					sbSql.append(" WHERE "); 
					// Se puede usar hasta detalle puesto_id
                    //sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);sbSql.append(" = ? ");

					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ? ");
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//					}
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" = ");
					sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ? ");
					}

					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
					sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					sbSql.append(" ) ");
					
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);sbSql.append(" = ");
					sbSql.append(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
					
					sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					//FIN QUERY
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
				
					pstmt.setInt(1, idDocente); //cargo el id docente : es el id de ficha docente
					pstmt.setInt(2, idRcesMlcrpr); //cargo el id malla curricular paralelo : es el id de recod estudiante
					int contador = 2;
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						contador ++;
//						pstmt.setInt(contador, idPeriodo); //cargo el id del periodo academico
//					}
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idCarrera); //cargo el id de carrera
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idNivel); //cargo el id de nivel
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idParalelo); //cargo el id de paralelo
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idMateria); //cargo el id de la materia
					}

					rs = pstmt.executeQuery();
					retorno = new ArrayList<EstudianteJdbcDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoEstudiantesPeriodoCarreraNivelMateria(rs));
					}

				} catch (SQLException e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.sql.exception")));
				} catch (Exception e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.exception")));
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
					throw new EstudianteDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.no.result.exception")));
				}
				return retorno;
			}	
			
			
			
			/**
			 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
			 * @param idPeriodo - idPeriodo id del periodo academico a buscar
			 * @param idCarrera - idCarrera id de la carrera a buscar
			 * @param idNivel - idNivel id del nivel a buscar
			 * @param idParalelo - idParalelo id del paralelo a buscar
			 * @param idMateria - idMateria id de la materia a buscar
			 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
			 * @return - retorna una lista de estudiantes con los parametros ingresados
			 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
			 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
			 */
			public List<EstudianteJdbcDto> buscarEstudianteRectificacion( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException{
				List<EstudianteJdbcDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {

					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT");
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
								sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
								sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
					sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);					
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);					
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);						
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);					
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);					
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);				
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);					
					sbSql.append(" ,dtps.");sbSql.append(JdbcConstantes.DTPS_ID);	
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA2);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_ID);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL);

					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					
					sbSql.append(" FROM ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ON ");
					sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ON ");
					sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ON ");
					sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ON ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ON ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ON ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);	
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ON ");
					sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
					sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ON ");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ON ");
					sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ON ");
					
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
									
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
					sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ON ");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
					

					sbSql.append(" WHERE "); 
					// Se puede usar hasta detalle puesto_id
                    //sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);sbSql.append(" = ? ");

					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ? ");
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//					}
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" IN ( ");
					sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
					sbSql.append(" ) ");
					
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);sbSql.append(" in ( ");
					sbSql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					sbSql.append(" , ");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
					sbSql.append(" ) ");
					
					
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ? ");
					}

					
					sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);

					//FIN QUERY
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
				
					pstmt.setInt(1, idDocente); //cargo el id docente : es el id de ficha docente
					pstmt.setInt(2, idRcesMlcrpr); //cargo el id malla curricular paralelo : es el id de recod estudiante
					int contador = 2;
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						contador ++;
//						pstmt.setInt(contador, idPeriodo); //cargo el id del periodo academico
//					}
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idCarrera); //cargo el id de carrera
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idNivel); //cargo el id de nivel
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idParalelo); //cargo el id de paralelo
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idMateria); //cargo el id de la materia
					}

					rs = pstmt.executeQuery();
					retorno = new ArrayList<EstudianteJdbcDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoEstudiantesPeriodoCarreraNivelMateria(rs));
					}

				} catch (SQLException e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.sql.exception")));
				} catch (Exception e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.exception")));
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
					throw new EstudianteDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.no.result.exception")));
				}
				return retorno;
			}	
			
			
			/**
			 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
			 * @param idPeriodo - idPeriodo id del periodo academico a buscar
			 * @param idCarrera - idCarrera id de la carrera a buscar
			 * @param idNivel - idNivel id del nivel a buscar
			 * @param idParalelo - idParalelo id del paralelo a buscar
			 * @param idMateria - idMateria id de la materia a buscar
			 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
			 * @return - retorna una lista de estudiantes con los parametros ingresados
			 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
			 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
			 */
			public List<EstudianteJdbcDto> buscarEstudianteRectificacionRecuperacion( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException{
				List<EstudianteJdbcDto> retorno = null;
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;
				try {

					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT");
					sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
								sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
								sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
					sbSql.append(" ,cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_ID);
					sbSql.append(" ,dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
					sbSql.append(" ,prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);					
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
					sbSql.append(" ,fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);					
					sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);						
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_ID);
					sbSql.append(" ,nvl.");sbSql.append(JdbcConstantes.NVL_DESCRIPCION);					
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_ID);
					sbSql.append(" ,prl.");sbSql.append(JdbcConstantes.PRL_DESCRIPCION);					
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_ID);				
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_CODIGO);
					sbSql.append(" ,mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);					
					sbSql.append(" ,dtps.");sbSql.append(JdbcConstantes.DTPS_ID);	
					sbSql.append(" ,dtmt.");sbSql.append(JdbcConstantes.DTMT_NUMERO);
					sbSql.append(" ,mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ID);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_ESTADO);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA2);
					sbSql.append(" ,rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_ID);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
					
					sbSql.append(" ,clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PROMEDIO_NOTAS);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_ASISTENCIA_TOTAL);

					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION2);
					
					sbSql.append(" , CASE WHEN clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
					sbSql.append(" ELSE clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
					
					sbSql.append(" FROM ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FCIN_ID);sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ON ");
					sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" = ");sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_FCMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt ON ");
					sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" = ");sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_CMPA_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr ON ");
					sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.DTMT_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl ON ");
					sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_PRL_ID);			
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt ON ");
					sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_MLCRMT_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl ON ");
					sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_NVL_ID);sbSql.append(" = ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);	
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ON ");
					sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_CNCR_ID);sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
					sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR);sbSql.append(" mlcr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MALLA_PERIODO);sbSql.append(" mlpr ON ");
					sbSql.append(" mlcr.");sbSql.append(JdbcConstantes.MLCR_ID);sbSql.append(" = ");sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_MLCR_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ON ");
					sbSql.append(" mlpr.");sbSql.append(JdbcConstantes.MLPR_PRAC_ID);sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ON ");
					sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);sbSql.append(" = ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARGA_HORARIA);sbSql.append(" crhr ON ");
					sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_MLCRPR_ID);sbSql.append(" = ");sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_DETALLE_PUESTO);sbSql.append(" dtps ON ");
					
					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_ID);sbSql.append(" = ");sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);
									
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr ON ");
					sbSql.append(" mtr.");sbSql.append(JdbcConstantes.MTR_ID);sbSql.append(" = ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);

					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_RECORD_ESTUDIANTE);sbSql.append(" rces ON ");
					sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" = ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_FCES_ID);
					
					sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CALIFICACION);sbSql.append(" clf ON ");
					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_ID);sbSql.append(" = ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_RCES_ID);
					

					sbSql.append(" WHERE "); 
					// Se puede usar hasta detalle puesto_id
                    //sbSql.append(" crhr.");sbSql.append(JdbcConstantes.CRHR_DTPS_ID);sbSql.append(" = ? ");

					sbSql.append(" dtps.");sbSql.append(JdbcConstantes.DTPS_FCDC_ID);sbSql.append(" = ? ");
					sbSql.append(" AND ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_MLCRPR_ID);sbSql.append(" = ? ");
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);sbSql.append(" = ? ");
//					}
					
					sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);sbSql.append(" In (");
					sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" , ");sbSql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					sbSql.append(" ) ");
					
					
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" = ? ");
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRL_ID);sbSql.append(" = ? ");
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						sbSql.append(" AND ");sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_MTR_ID);sbSql.append(" = ? ");
					}

					sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_PARAM_RECUPERACION1);sbSql.append(" is not null  ");
					sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);sbSql.append(" >=  ");
					sbSql.append(CalificacionConstantes.PORCENTAJE_ASISTENCIA_APROBACION_VALUE);
//					sbSql.append(" AND ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);sbSql.append(" BETWEEN  ");
//					sbSql.append(CalificacionConstantes.NOTA_MINIMA_INGRESAR_RECUPERACION_MATERIA_VALUE);sbSql.append(" AND ");
//					sbSql.append(CalificacionConstantes.NOTA_MAXIMA_INGRESAR_RECUPERACION_MATERIA_VALUE);
					
//					sbSql.append(" AND ( ");sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);sbSql.append(" is null or ");
//					sbSql.append(" clf.");sbSql.append(JdbcConstantes.CLF_SUPLETORIO);sbSql.append(" is not null ) ");
					
//					sbSql.append(" AND ( ");sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);sbSql.append(" is null or ");
//					sbSql.append(" rces.");sbSql.append(JdbcConstantes.RCES_INGRESO_NOTA3);sbSql.append(" =  ");
//					sbSql.append(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
//					sbSql.append(" ) ");
					
					sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
					sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
					//FIN QUERY
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					pstmt.setInt(1, idDocente); //cargo el id docente : es el id de ficha docente
					pstmt.setInt(2, idRcesMlcrpr); //cargo el id malla curricular paralelo : es el id de recod estudiante
					int contador = 2;
//					if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
//						contador ++;
//						pstmt.setInt(contador, idPeriodo); //cargo el id del periodo academico
//					}
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idCarrera); //cargo el id de carrera
					}
					if(idNivel != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idNivel); //cargo el id de nivel
					}
					if(idParalelo != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idParalelo); //cargo el id de paralelo
					}
					if(idMateria != GeneralesConstantes.APP_ID_BASE){
						contador ++;
						pstmt.setInt(contador, idMateria); //cargo el id de la materia
					}

					rs = pstmt.executeQuery();
					retorno = new ArrayList<EstudianteJdbcDto>();
					while(rs.next()){
						retorno.add(transformarResultSetADtoEstudiantesPeriodoCarreraNivelMateria(rs));
					}

				} catch (SQLException e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.sql.exception")));
				} catch (Exception e) {
					throw new EstudianteDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.exception")));
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
					throw new EstudianteDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotasPregaradoDtoServicioJdbc.buscar.por.periodo.carrera.nivel.paralelo.materia.docente.no.result.exception")));
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
		private EstudianteJdbcDto transformarResultSetADtoEstudiantesPeriodoCarreraNivelMateria(ResultSet rs) throws SQLException{
			EstudianteJdbcDto retorno = new EstudianteJdbcDto();
			
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));			
			retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
			retorno.setCncrId(rs.getInt(JdbcConstantes.CRR_ID));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));			
			retorno.setDpnId(rs.getInt(JdbcConstantes.DPN_ID));
			retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));			
			retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
			retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));
			retorno.setPracEstado(rs.getInt(JdbcConstantes.PRAC_ESTADO));			
			retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));
			retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));
			retorno.setFcinTipo(rs.getInt(JdbcConstantes.FCIN_TIPO));			
			retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));			
			retorno.setNvlId(rs.getInt(JdbcConstantes.NVL_ID));			
			retorno.setNvlDescripcion(rs.getString(JdbcConstantes.NVL_DESCRIPCION));			
			retorno.setPrlId(rs.getInt(JdbcConstantes.PRL_ID));
			retorno.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));			
			retorno.setMtrId(rs.getInt(JdbcConstantes.MTR_ID));
			retorno.setMtrCodigo(rs.getString(JdbcConstantes.MTR_CODIGO));
			retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));			
			retorno.setDtmtNumero(rs.getInt(JdbcConstantes.DTMT_NUMERO));
			retorno.setMlcrprId(rs.getInt(JdbcConstantes.MLCRPR_ID));
			retorno.setRcesId(rs.getInt(JdbcConstantes.RCES_ID));
			retorno.setRcesEstado(rs.getInt(JdbcConstantes.RCES_ESTADO));
			retorno.setRcesIngersoNota(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA));
			retorno.setRcesIngersoNota2(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA2));
			retorno.setRcesIngersoNota3(rs.getInt(JdbcConstantes.RCES_INGRESO_NOTA3));
			retorno.setClfId(rs.getInt(JdbcConstantes.CLF_ID));
			
			int asistencia1Aux=0;
			asistencia1Aux =  rs.getInt(JdbcConstantes.CLF_ASISTENCIA1);
			if(asistencia1Aux!=-99){
				retorno.setClfAsistenciaEstudiante1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA1));
			}else{
				retorno.setClfAsistenciaEstudiante1(null);
			}
			
			int asistencia2Aux = rs.getInt(JdbcConstantes.CLF_ASISTENCIA2);
			if(asistencia2Aux != -99){
				retorno.setClfAsistenciaEstudiante2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
			}else{
				retorno.setClfAsistenciaEstudiante2(null);
			}
			
			
//			retorno.setClfAsistenciaEstudiante2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA2));
			
			
//			int asistenciaDocAux=0;
//			asistenciaDocAux =  rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
//			if(asistenciaDocAux!=-99){
//				retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
//			}else{
//				retorno.setClfAsistenciaDocente1(null);
//			}
			
			int asistenciaDocente1 = rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1);
			if(asistenciaDocente1 != -99){
				retorno.setClfAsistenciaDocente1(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE1));
			}else{
				retorno.setClfAsistenciaDocente1(null);
			}
			
			int asistenciaDocente2 = rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2);
			if(asistenciaDocente2 != -99){
				retorno.setClfAsistenciaDocente2(rs.getInt(JdbcConstantes.CLF_ASISTENCIA_DOCENTE2));
			}else{
				retorno.setClfAsistenciaDocente2(null);
			}
			
			int nota1 = rs.getInt(JdbcConstantes.CLF_NOTA1);
			if(nota1 != -99){
				retorno.setClfNota1(rs.getBigDecimal(JdbcConstantes.CLF_NOTA1));
			}else{
				retorno.setClfNota1(null);
			}
			
			int nota2 = rs.getInt(JdbcConstantes.CLF_NOTA2);
			if(nota2 != -99){
				retorno.setClfNota2(rs.getBigDecimal(JdbcConstantes.CLF_NOTA2));
			}else{
				retorno.setClfNota2(null);
			}
			
			int promedioAsistenciaAux = rs.getInt(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA);
			if(promedioAsistenciaAux != -99){
				retorno.setClfPromedioAsistencia(rs.getBigDecimal(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
			}else{
				retorno.setClfPromedioAsistencia(null);
			}
			
			
			int proemdioNotasAux = rs.getInt(JdbcConstantes.CLF_PROMEDIO_NOTAS);
			if(proemdioNotasAux != -99){
				retorno.setClfPromedioNotas(rs.getBigDecimal(JdbcConstantes.CLF_PROMEDIO_NOTAS));
			}else{
				retorno.setClfPromedioNotas(null);
			}
			
			int supletorioAux = rs.getInt(JdbcConstantes.CLF_SUPLETORIO);
			if(supletorioAux != -99){
				retorno.setClfSupletorio(rs.getBigDecimal(JdbcConstantes.CLF_SUPLETORIO));
			}else{
				retorno.setClfSupletorio(null);
			}
			
			int totalAsistenciaAux = rs.getInt(JdbcConstantes.CLF_ASISTENCIA_TOTAL);
			if(totalAsistenciaAux != -99){
				retorno.setClfAsistenciaTotal(rs.getBigDecimal(JdbcConstantes.CLF_ASISTENCIA_TOTAL));
			}else{
				retorno.setClfAsistenciaTotal(null);
			}
			
			
			int paramRecuperacion1Aux = rs.getInt(JdbcConstantes.CLF_PARAM_RECUPERACION1);
			if(paramRecuperacion1Aux != -99){
				retorno.setClfParamRecuperacion1(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION1));
			}else{
				retorno.setClfParamRecuperacion1(null);
			}
			
			int paramRecuperacion2Aux = rs.getInt(JdbcConstantes.CLF_PARAM_RECUPERACION2);
			if(paramRecuperacion2Aux != -99){
				retorno.setClfParamRecuperacion2(rs.getBigDecimal(JdbcConstantes.CLF_PARAM_RECUPERACION2));
			}else{
				retorno.setClfParamRecuperacion2(null);
			}
			
			int notaFinalSemestreAux = rs.getInt(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE);
			if(notaFinalSemestreAux != -99){
				retorno.setClfNotalFinalSemestre(rs.getBigDecimal(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
			}else{
				retorno.setClfNotalFinalSemestre(null);
			}
			return retorno;
		}
		
}
