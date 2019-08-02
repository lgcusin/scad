/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     CalificacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Calificacion. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 12-JUN-2017           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ec.edu.uce.academico.ejb.dtos.CalificacionNivelacionDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.CalificacionModulo;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;

/**
 * Clase (Bean)CalificacionServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CalificacionServicioImpl implements CalificacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	@Resource
	private SessionContext session;

	@Resource
    private UserTransaction userTransaction;
	
	@EJB
	private MateriaServicio srvMateriaServicio;
	@EJB
	private RecordEstudianteServicio srvRecordEstudianteServicio;
	/**
	 * Busca una entidad Calificación por su id
	 * @param id - de la Calificación a buscar
	 * @return Calificación con el id solicitado
	 * @throws CalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Calificación con el id solicitado
	 * @throws CalificacionException - Excepcion general
	 */
	@Override
	public Calificacion buscarPorId(Integer id) throws CalificacionNoEncontradoException, CalificacionException {
		Calificacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Calificacion.class, id);
			} catch (NoResultException e) {
				throw new CalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	@Override
	public CalificacionModulo buscarClmdPorId(Integer id) throws CalificacionNoEncontradoException, CalificacionException {
		CalificacionModulo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(CalificacionModulo.class, id);
			} catch (NoResultException e) {
				throw new CalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Calificación existentes en la BD
	 * @return lista de todas las entidades Calificación existentes en la BD
	 * @throws CalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Calificacion 
	 * @throws CalificacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> listarTodos() throws CalificacionNoEncontradoException , CalificacionException {
		List<Calificacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.listar.todos.exception")));
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CalificacionModulo> listarTodosModularesCorreccion() throws CalificacionNoEncontradoException , CalificacionException {
		List<CalificacionModulo> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clmd from CalificacionModulo clmd, MallaCurricularParalelo mlcrpr, Paralelo prl, PeriodoAcademico prac ");
			sbsql.append(" where clmd.mlcrprIdModulo = mlcrpr.mlcrpId and prl.prlId=mlcrpr.mlcrprParalelo.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId and prac.pracEstado=");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbsql.append(" and clmd.clmdParamRecuperacion1 is null ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasPregradoPrimerHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente){
		//registro de notas
		//busco la record academico
		try {
			session.getUserTransaction().begin();
			 if(estudiante.getRcesIngersoNota2()==null||estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE){
					if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota()==null){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						Calificacion calificacion = new Calificacion();
						if(estudiante.getClfNota1()!=null){
							calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						if(estudiante.getClfAsistenciaDocente1()!=null){
							calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
						calificacion.setRecordEstudiante(rcesAux);
						em.persist(calificacion);

						ProcesoCalificacion prclf = new ProcesoCalificacion();
						prclf.setPrclFecha(new Timestamp(new Date().getTime()));
						prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
						prclf.setPrclObservacion(regCliente);
						prclf.setPrclCalificacion(calificacion);
						prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
						prclf.setPrclAsistencia1(calificacion.getClfAsistencia1());
						prclf.setPrclNota1(calificacion.getClfNota1());
						em.persist(prclf);
					}
					
					if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
						if(estudiante.getClfNota1()!=null){
							clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						if(estudiante.getClfAsistenciaDocente1()!=null){
							clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
						
						ProcesoCalificacion prclf = new ProcesoCalificacion();
						prclf.setPrclFecha(new Timestamp(new Date().getTime()));
						prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
						prclf.setPrclObservacion(regCliente);
						prclf.setPrclCalificacion(clfAux);
						prclf.setPrclAsistenciaDocente1(clfAux.getClfAsistenciaDocente1());
						prclf.setPrclAsistencia1(clfAux.getClfAsistencia1());
						prclf.setPrclNota1(clfAux.getClfNota1());
						em.persist(prclf);
					}
				 }
				 ///////////////////////
				 if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE || estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
					 if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota()==null){
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
							rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							
							Calificacion calificacion = em.find(Calificacion.class, estudiante.getClfId());
							if(estudiante.getClfNota1()!=null){
								calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante1()!=null){
								calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
							}
							if(estudiante.getClfAsistenciaDocente1()!=null){
								calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
							}
							
							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFecha(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
							prclf.setPrclObservacion(regCliente);
							prclf.setPrclCalificacion(calificacion);
							prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
							prclf.setPrclAsistencia1(calificacion.getClfAsistencia1());
							prclf.setPrclNota1(calificacion.getClfNota1());
							em.persist(prclf);
						}
						if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
							rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
							if(estudiante.getClfNota1()!=null){
								clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante1()!=null){
								clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
							}
							if(estudiante.getClfAsistenciaDocente1()!=null){
								clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
							}
							
							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFecha(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
							prclf.setPrclObservacion(regCliente);
							prclf.setPrclCalificacion(clfAux);
							prclf.setPrclAsistenciaDocente1(clfAux.getClfAsistenciaDocente1());
							prclf.setPrclAsistencia1(clfAux.getClfAsistencia1());
							prclf.setPrclNota1(clfAux.getClfNota1());
							em.persist(prclf);
						}
				 }
				FichaInscripcion fcinAux = new FichaInscripcion();
				fcinAux = em.find(FichaInscripcion.class, estudiante.getFcinId());
				fcinAux.setFcinMatriculado(0);
				em.persist(fcinAux);
				session.getUserTransaction().commit();
			
			
		} catch (IllegalStateException e) {
		} catch (NotSupportedException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		}
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasPregradoSegundoHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente){
		//registro de notas
		//busco la record academico
		
		try {
			session.getUserTransaction().begin();
			 if(estudiante.getRcesIngersoNota()==null||estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE){
					if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota2()==null){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						rcesAux.setRcesEstado(estudiante.getRcesEstado());
						
						Calificacion calificacion = new Calificacion();
						if(estudiante.getClfNota2()!=null){
							calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
						calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion1()!=null){
							calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						
						calificacion.setRecordEstudiante(rcesAux);
						em.persist(calificacion);

						ProcesoCalificacion prclf = new ProcesoCalificacion();
						prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
						prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
						prclf.setPrclObservacion2(regCliente);
						prclf.setPrclCalificacion(calificacion);
						prclf.setPrclAsistenciaDocente2(calificacion.getClfAsistenciaDocente2());
						
						if(estudiante.getClfNota2()!=null){
							prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
						prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaDocente2()));
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion1()!=null){
							prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						em.persist(prclf);
					}
					
					if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE
							||estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						rcesAux.setRcesEstado(estudiante.getRcesEstado());
						Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
						if(estudiante.getClfNota2()!=null){
							clfAux.setClfNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							clfAux.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							clfAux.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							clfAux.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
							clfAux.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							clfAux.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							clfAux.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							clfAux.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion1()!=null){
							clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						
						ProcesoCalificacion prclf = new ProcesoCalificacion();
						prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
						prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
						prclf.setPrclObservacion2(regCliente);
						prclf.setPrclCalificacion(clfAux);
						prclf.setPrclAsistenciaDocente2(clfAux.getClfAsistenciaDocente2());
						
						if(estudiante.getClfNota2()!=null){
							prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
							prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaDocente2()));
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion1()!=null){
							prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						em.persist(prclf);
					}
				 }
				 ///////////////////////
				 if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE || estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
					 if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota2()==null){
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
							rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							rcesAux.setRcesEstado(estudiante.getRcesEstado());
							Calificacion calificacion = em.find(Calificacion.class, estudiante.getClfId());
							if(estudiante.getClfNota2()!=null){
								calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante2()!=null){
								calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
							}
							
							if(estudiante.getClfAsistenciaTotal()!=null){
								calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
							}
							
							if(estudiante.getClfPromedioAsistencia()!=null){
								calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
							}
							
							if(estudiante.getClfAsistenciaDocente2()!=null){
								calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
							}
							
							if(estudiante.getClfAsistenciaTotalDoc()!=null){
								calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(estudiante.getClfSumaP1P2()!=null){
								calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
							}
							
							if(estudiante.getClfPromedioNotas()!=null){
								calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
							}
							
							if(estudiante.getClfParamRecuperacion1()!=null){
								calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
							}
							
							if(estudiante.getClfNotalFinalSemestre()!=null){
								calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
							}
							
							
							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
							prclf.setPrclObservacion2(regCliente);
							prclf.setPrclCalificacion(calificacion);
							prclf.setPrclAsistenciaDocente2(calificacion.getClfAsistenciaDocente2());
							
							if(estudiante.getClfNota2()!=null){
								prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante2()!=null){
								prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
							}
							
							if(estudiante.getClfAsistenciaTotal()!=null){
								prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
							}
							
							if(estudiante.getClfPromedioAsistencia()!=null){
								prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
							}
							
							if(estudiante.getClfAsistenciaDocente2()!=null){
								prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaDocente2()));
							}
							
							if(estudiante.getClfAsistenciaTotalDoc()!=null){
								prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(estudiante.getClfSumaP1P2()!=null){
								prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
							}
							
							if(estudiante.getClfPromedioNotas()!=null){
								prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
							}
							
							if(estudiante.getClfParamRecuperacion1()!=null){
								prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
							}
							
							if(estudiante.getClfNotalFinalSemestre()!=null){
								prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
							}
							
							em.persist(prclf);
						}
						if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE
								||estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE
								){
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
							rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							rcesAux.setRcesEstado(estudiante.getRcesEstado());
							Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
							if(estudiante.getClfNota2()!=null){
								clfAux.setClfNota2(estudiante.getClfNota2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante2()!=null){
								clfAux.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
							}
							
							if(estudiante.getClfAsistenciaTotal()!=null){
								clfAux.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
							}
							
							if(estudiante.getClfPromedioAsistencia()!=null){
								clfAux.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
							}
							
							if(estudiante.getClfAsistenciaDocente2()!=null){
								clfAux.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
							}
							
							if(estudiante.getClfAsistenciaTotalDoc()!=null){
								clfAux.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(estudiante.getClfSumaP1P2()!=null){
								clfAux.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
							}
							
							if(estudiante.getClfPromedioNotas()!=null){
								clfAux.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
							}
							
							if(estudiante.getClfParamRecuperacion1()!=null){
								clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
							}
							
							if(estudiante.getClfNotalFinalSemestre()!=null){
								clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
							}
							
							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
							prclf.setPrclObservacion2(regCliente);
							prclf.setPrclCalificacion(clfAux);
							prclf.setPrclAsistenciaDocente2(clfAux.getClfAsistenciaDocente2());
							if(estudiante.getClfNota2()!=null){
								prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante2()!=null){
								prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
							}
							
							if(estudiante.getClfAsistenciaTotal()!=null){
								prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
							}
							
							if(estudiante.getClfPromedioAsistencia()!=null){
								prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
							}
							
							if(estudiante.getClfAsistenciaDocente2()!=null){
								prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaDocente2()));
							}
							
							if(estudiante.getClfAsistenciaTotalDoc()!=null){
								prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(estudiante.getClfSumaP1P2()!=null){
								prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
							}
							
							if(estudiante.getClfPromedioNotas()!=null){
								prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
							}
							
							if(estudiante.getClfParamRecuperacion1()!=null){
								prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
							}
							
							if(estudiante.getClfNotalFinalSemestre()!=null){
								prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
							}
							em.persist(prclf);
						}
				 }
				FichaInscripcion fcinAux = new FichaInscripcion();
				fcinAux = em.find(FichaInscripcion.class, estudiante.getFcinId());
				fcinAux.setFcinMatriculado(0);
				em.persist(fcinAux); 
				session.getUserTransaction().commit();
		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		}
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasPosgrado(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente){
		try {
			session.getUserTransaction().begin();
			RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
			rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
			rcesAux.setRcesEstado(estudiante.getRcesEstado());

			Calificacion clfAux = new Calificacion();

			try {
				if(estudiante.getClfId()!=0){
					clfAux = em.find(Calificacion.class, estudiante.getClfId());
					if (clfAux != null) {

					} else {
						clfAux = new Calificacion();
					}
	
				}else{
					clfAux = new Calificacion();
				}
				
			} catch (Exception e) {
				clfAux = new Calificacion();
			}
			if (estudiante.getClfNota1() != null) {
				clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
			}

			if (estudiante.getClfAsistenciaEstudiante1() != null) {
				clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
			}

			clfAux.setClfNota2(null);
			clfAux.setClfAsistenciaDocente2(null);
			clfAux.setClfAsistencia2(null);
			if (estudiante.getClfAsistenciaTotal() != null) {
				clfAux.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
			}

			if (estudiante.getClfPromedioAsistencia() != null) {
				clfAux.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
			}

			if (estudiante.getClfAsistenciaDocente1() != null) {
				clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
			}

			if (estudiante.getClfAsistenciaTotalDoc() != null) {
				clfAux.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
			}

			if (estudiante.getClfSumaP1P2() != null) {
				clfAux.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
			}

			if (estudiante.getClfPromedioNotas() != null) {
				clfAux.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
			}

			if (estudiante.getClfParamRecuperacion1() != null) {
				clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
			}

			if (estudiante.getClfNotalFinalSemestre() != null) {
				clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
			}
			
			
			ProcesoCalificacion prclf = new ProcesoCalificacion();
			prclf.setPrclFecha(new Timestamp(new Date().getTime()));
			prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
			prclf.setPrclObservacion(regCliente);
			prclf.setPrclCalificacion(clfAux);
			prclf.setPrclAsistenciaDocente1(clfAux.getClfAsistenciaDocente1());
			if (estudiante.getClfNota1() != null) {
				prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
			}

			if (estudiante.getClfAsistenciaEstudiante1() != null) {
				prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
			}

			if (estudiante.getClfAsistenciaTotal() != null) {
				prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
			}

			if (estudiante.getClfPromedioAsistencia() != null) {
				prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
			}

			if (estudiante.getClfAsistenciaDocente1() != null) {
				prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaDocente1()));
			}

			if (estudiante.getClfAsistenciaTotalDoc() != null) {
				prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
			}

			if (estudiante.getClfSumaP1P2() != null) {
				prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
			}

			if (estudiante.getClfPromedioNotas() != null) {
				prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
			}

			if (estudiante.getClfParamRecuperacion1() != null) {
				prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
			}

			if (estudiante.getClfNotalFinalSemestre() != null) {
				prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
			}
			
			

			session.getUserTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardoTemporalNotasPregradoPrimerHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante , String regCliente)  throws CalificacionValidacionException, CalificacionException{
		//registro de notas
		//busco la record academico
		try {
			session.getUserTransaction().begin();
			 if(estudiante.getRcesIngersoNota2()==null||estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE){
				if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota()==null){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);

						Calificacion calificacion = new Calificacion();
						if(estudiante.getClfNota1()!=null){
							calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						if(estudiante.getClfAsistenciaDocente1()!=null){
							calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
						calificacion.setRecordEstudiante(rcesAux);
						em.persist(calificacion);
				}
				
				if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){
					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
					rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
					Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
						
					if(estudiante.getClfNota1()!=null){
						clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
					}
						
					if(estudiante.getClfAsistenciaEstudiante1()!=null){
						clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
					}
					if(estudiante.getClfAsistenciaDocente1()!=null){
						clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
					}
				}
			 }
			 ///////////////////////
			 if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE || estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
				 if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota()==null){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
						Calificacion calificacion = em.find(Calificacion.class, estudiante.getClfId());
						
						if(estudiante.getClfNota1()!=null){
							calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						if(estudiante.getClfAsistenciaDocente1()!=null){
							calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
						
					}
					if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
						
						Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
						
						if(estudiante.getClfNota1()!=null){
							clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						if(estudiante.getClfAsistenciaDocente1()!=null){
							clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
					}
			 }
			 
			session.getUserTransaction().commit();
		} catch (Exception e) {
				try {
					session.getUserTransaction().rollback();
				} catch (IllegalStateException e1) {
				} catch (SecurityException e1) {
				} catch (SystemException e1) {
				}
 		  throw new CalificacionException(e.getMessage());
		} 
	}
		
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardoTemporalNotasPregradoSegundoHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante , String regCliente)  throws CalificacionValidacionException, CalificacionException{
		//registro de notas
		//busco la record academico
		try {
			
			session.getUserTransaction().begin();
			 if(estudiante.getRcesIngersoNota()==null||estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE){
				if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota2()==null){
					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
					rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
					Calificacion calificacion = new Calificacion();
					if(estudiante.getClfNota2()!=null){
						calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
					}
					
					if(estudiante.getClfAsistenciaEstudiante2()!=null){
						calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
					}
					calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
					
					calificacion.setRecordEstudiante(rcesAux);
					em.persist(calificacion);
					
				}
				if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){
					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
					rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
					Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
					if(estudiante.getClfNota2()!=null){
						clfAux.setClfNota2(estudiante.getClfNota2().floatValue());
					}
					
					if(estudiante.getClfAsistenciaEstudiante2()!=null){
						clfAux.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
					}
						clfAux.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
				}
				
			 }
			
			 ///////////////////////
			 if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE || estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
				 if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota2()==null){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
						
						Calificacion calificacion = em.find(Calificacion.class, estudiante.getClfId());
						if(estudiante.getClfNota2()!=null){
							calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
							calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
						
					}
					if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE);
						Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
						if(estudiante.getClfNota2()!=null){
							clfAux.setClfNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							clfAux.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
							clfAux.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
						
					}
				 
			 }	
			session.getUserTransaction().commit();
		} catch (Exception e) {
				try {
					session.getUserTransaction().rollback();
				} catch (IllegalStateException e1) {
				} catch (SecurityException e1) {
				} catch (SystemException e1) {
				}
 		  throw new CalificacionException(e.getMessage());
		} 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasPregradoRecuperacion(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente){
		//registro de notas
		//busco la record academico
		
		try {
			session.getUserTransaction().begin();
			if(estudiante.isMateriaModulo()){
				try {
					Materia mtrAux = srvMateriaServicio.buscarMateriaXMlCrPrPeriodoPregradoActivo(estudiante.getMlcrprIdModulo());
					Materia MateriaPadre =  srvMateriaServicio.buscarPorId(mtrAux.getMtrMateria().getMtrId());
					List<Materia> listaModulos = new ArrayList<>();
					listaModulos = srvMateriaServicio.listarTodosModulos(MateriaPadre.getMtrId());
					RecordEstudiante rcesAuxPadre= srvRecordEstudianteServicio.buscarPorId(recordEstudianteDto.getRcesId());
					CalificacionModulo calificacionModulo = new CalificacionModulo();
					calificacionModulo = em.find(CalificacionModulo.class, estudiante.getClfId());
					
					calificacionModulo.setClmdSupletorio(estudiante.getClfSupletorio().floatValue());
					BigDecimal parametro2Aux  = BigDecimal.ZERO;
					parametro2Aux  = estudiante.getClfSupletorio().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP);
					calificacionModulo.setClmdParamRecuperacion2(parametro2Aux.floatValue());
					BigDecimal parametro1Aux  = BigDecimal.ZERO;
					parametro1Aux = estudiante.getClmdSumaP1P2().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP);;
					calificacionModulo.setClmdParamRecuperacion1(parametro1Aux.floatValue());
					
					BigDecimal sumaParametros = BigDecimal.ZERO;
					sumaParametros = parametro1Aux.add(parametro2Aux);
//					estudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					int comparador = sumaParametros.compareTo(new BigDecimal(27.5));
					if(comparador==-1){
						calificacionModulo.setClmdNotaFinalSemestre(sumaParametros.floatValue());						
					}else{
						calificacionModulo.setClmdNotaFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP).floatValue());
					}

					em.persist(calificacionModulo);
				}catch (Exception e) {
						// TODO: handle exception
				}
					
			}else{
				RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
				rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
				rcesAux.setRcesIngresoNota3(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
				rcesAux.setRcesEstado(estudiante.getRcesEstado());
				Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
				if(estudiante.getClfSupletorio()!=null){
					clfAux.setClfSupletorio(new Float(estudiante.getClfSupletorio().floatValue()));
				}
				if(estudiante.getClfParamRecuperacion1()!=null){
					clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
				}
				if(estudiante.getClfParamRecuperacion2()!=null){
					clfAux.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
				}
				if(estudiante.getClfNotalFinalSemestre()!=null){
					clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());;
				}
				em.persist(clfAux);
				ProcesoCalificacion prclf = new ProcesoCalificacion();
				prclf.setPrclFechaRecuperacion(new Timestamp(new Date().getTime()));
				prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_3_RECUPERACION_VALUE);
				prclf.setPrclObservacion3(regCliente);
				prclf.setPrclCalificacion(clfAux);
				prclf.setPrclSupletorio(clfAux.getClfSupletorio());
				//TODO completar auditoria
				em.persist(prclf);
				em.persist(rcesAux);
			}
			try {
				FichaInscripcion fcinAux = new FichaInscripcion();
				fcinAux = em.find(FichaInscripcion.class, estudiante.getFcinId());
				fcinAux.setFcinMatriculado(0);
				em.persist(fcinAux);
			} catch (Exception e) {
			}
			
			session.getUserTransaction().commit();
		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		}
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void verificarModulos(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException{
		try {
			session.getUserTransaction().begin();
			Materia mtrAux = srvMateriaServicio.buscarMateriaXMlCrPrPeriodoPregradoActivo(estudiante.getMlcrprIdModulo());
			Materia MateriaPadre =  srvMateriaServicio.buscarPorId(mtrAux.getMtrMateria().getMtrId());
			List<Materia> listaModulos = new ArrayList<>();
			listaModulos = srvMateriaServicio.listarTodosModulos(MateriaPadre.getMtrId());
//			System.out.println(MateriaPadre.getMtrId());
			RecordEstudiante rcesAuxPadre= srvRecordEstudianteServicio.buscarPorId(recordEstudianteDto.getRcesId());
//			System.out.println(recordEstudianteDto.getRcesId());
			List<CalificacionModulo> listaClmd = new ArrayList<CalificacionModulo>();
			listaClmd = buscarClMdPorRces(rcesAuxPadre.getRcesId());
			
			if(listaModulos.size()==listaClmd.size()){
				BigDecimal asistenciaPromedio = BigDecimal.ZERO;
				BigDecimal asistenciaDocente = BigDecimal.ZERO;
				BigDecimal asistenciaSuma = BigDecimal.ZERO;
				BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
				BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
				BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
				BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
				BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
				BigDecimal notEstudiante2 = BigDecimal.ZERO;
				BigDecimal notEstudiante1 = BigDecimal.ZERO;
				BigDecimal notaSuma = BigDecimal.ZERO;
				BigDecimal notaPromedio = BigDecimal.ZERO;
				int ubicacion = 0;
				int ubicacionRecuperacion = 0;
				int contador = 0;
				boolean opAsistencia = false;
				boolean opNota = false;
				boolean opSupletorio = false;
				boolean opModulosCompletos = true;
				Float asistenciaMenor = Float.valueOf(100);
				Float notaMenor = Float.valueOf(100);
				for (CalificacionModulo item : listaClmd) {
					if(item.getClmdNotaFinalSemestre()!=null || item.getClmdNotaFinalSemestre()!=GeneralesConstantes.APP_ID_BASE.floatValue()){
						int comparadorNota = (new BigDecimal(item.getClmdNotaFinalSemestre())).compareTo(new BigDecimal(28));
						if(comparadorNota == -1){
							opNota = true;
							if(item.getClmdNotaFinalSemestre()<notaMenor){
								notaMenor = item.getClmdNotaFinalSemestre();
								ubicacion = contador;
							}		
						}
						if(!opNota){
							notaSuma=notaSuma.add(new BigDecimal(item.getClmdNotaFinalSemestre()).setScale(2, RoundingMode.DOWN));
//							notEstudiante1=notEstudiante1.add(new BigDecimal(item.getClmdNota1()));
//							notEstudiante2=notEstudiante2.add(new BigDecimal(item.getClmdNota2()));
//							rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						}
						contador++;
					}else{
						opModulosCompletos=false;
						break;
					}
					
				}
				if(opModulosCompletos){
					if(opNota){
						rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						em.merge(rcesAuxPadre);
						Calificacion calificacion = new Calificacion();
						boolean op = false;
						try {
							try {
								calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
								op= true;
							} catch (CalificacionNoEncontradoException e) {
								
							}

							
							if(listaClmd.get(ubicacion).getClmdNota1()!=null){
								calificacion.setClfNota1(listaClmd.get(ubicacion).getClmdNota1().floatValue());
							}
							
							if(listaClmd.get(ubicacion).getClmdAsistencia1()!=null){
								calificacion.setClfAsistencia1(new Float(listaClmd.get(ubicacion).getClmdAsistencia1()));
							}
							
							if(listaClmd.get(ubicacion).getClmdNota2()!=null){
								calificacion.setClfNota2(listaClmd.get(ubicacion).getClmdNota2().floatValue());
							}
							
							if(listaClmd.get(ubicacion).getClmdAsistencia2()!=null){
								calificacion.setClfAsistencia2(new Float(listaClmd.get(ubicacion).getClmdAsistencia2()));
							}
							
							if(listaClmd.get(ubicacion).getClmdAsistenciaDocente1()!=null){
								calificacion.setClfAsistenciaDocente1(new Float(listaClmd.get(ubicacion).getClmdAsistenciaDocente1()));
							}
							
							if(listaClmd.get(ubicacion).getClmdAsistenciaDocente2()!=null){
								calificacion.setClfAsistenciaDocente2(new Float(listaClmd.get(ubicacion).getClmdAsistenciaDocente2()));
							}
							
							
							if(listaClmd.get(ubicacion).getClmdAsistenciaTotalDoc()!=null){
								calificacion.setClfAsistenciaTotalDoc(listaClmd.get(ubicacion).getClmdAsistenciaTotalDoc().floatValue());
							}
							
							if(listaClmd.get(ubicacion).getClmdAsistenciaTotal()!=null){
								calificacion.setClfAsistenciaTotal(listaClmd.get(ubicacion).getClmdAsistenciaTotal().floatValue());
							}
							
								if(listaClmd.get(ubicacion).getClmdPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(listaClmd.get(ubicacion).getClmdPromedioAsistencia().floatValue());
								}	
							
							if(listaClmd.get(ubicacion).getClmdSumaP1P2()!=null){
								calificacion.setClfSumaP1P2(listaClmd.get(ubicacion).getClmdSumaP1P2().floatValue());
							}
							if(listaClmd.get(ubicacion).getClmdPromedioNotas()!=null){
								calificacion.setClfPromedioNotas(listaClmd.get(ubicacion).getClmdPromedioNotas().floatValue());
							}
							if(listaClmd.get(ubicacion).getClmdSupletorio()!=null){
								calificacion.setClfSupletorio(listaClmd.get(ubicacion).getClmdSupletorio().floatValue());
							}
							if(listaClmd.get(ubicacion).getClmdParamRecuperacion1()!=null){
								calificacion.setClfParamRecuperacion1(listaClmd.get(ubicacion).getClmdParamRecuperacion1().floatValue());
							}
							if(listaClmd.get(ubicacion).getClmdParamRecuperacion2()!=null){
								calificacion.setClfParamRecuperacion2(listaClmd.get(ubicacion).getClmdParamRecuperacion2().floatValue());
							}
							
							calificacion.setClfNotaFinalSemestre(listaClmd.get(ubicacion).getClmdNotaFinalSemestre().floatValue());	
							calificacion.setRecordEstudiante(rcesAuxPadre);
							if(op){
								em.merge(calificacion);
							}else{
								em.persist(calificacion);	
							}

							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_3_RECUPERACION_VALUE);
							prclf.setPrclObservacion2(regCliente);
							prclf.setPrclCalificacion(calificacion);
							prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
							
							if(calificacion.getClfNota1()!=null){
								prclf.setPrclNota1(calificacion.getClfNota1().floatValue());
							}
							
							if(calificacion.getClfAsistencia1()!=null){
								prclf.setPrclAsistencia1(new Float(calificacion.getClfAsistencia1()));
							}
							
							if(calificacion.getClfNota2()!=null){
								prclf.setPrclNota2(calificacion.getClfNota2().floatValue());
							}
							
							if(calificacion.getClfAsistencia2()!=null){
								prclf.setPrclAsistencia2(new Float(calificacion.getClfAsistencia2()));
							}
							
							if(calificacion.getClfAsistenciaTotal()!=null){
								prclf.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal().floatValue());
							}
							
							if(calificacion.getClfPromedioAsistencia()!=null){
								prclf.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia().floatValue());
							}
							
							
							if(calificacion.getClfAsistenciaDocente1()!=null){
								prclf.setPrclAsistencia1(calificacion.getClfAsistenciaDocente1().floatValue());
							}
							if(calificacion.getClfAsistenciaDocente2()!=null){
								prclf.setPrclAsistencia2(calificacion.getClfAsistenciaDocente2().floatValue());
							}
							
							if(calificacion.getClfAsistenciaTotalDoc()!=null){
								prclf.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(calificacion.getClfSumaP1P2()!=null){
								prclf.setPrclSumaP1P2(calificacion.getClfSumaP1P2().floatValue());
							}
							
							if(calificacion.getClfPromedioNotas()!=null){
								prclf.setPrclPromedioNotas(calificacion.getClfPromedioNotas().floatValue());
							}
							
							if(calificacion.getClfSupletorio()!=null){
								prclf.setPrclSupletorio(calificacion.getClfSupletorio().floatValue());
							}
							
							
							if(calificacion.getClfParamRecuperacion1()!=null){
								prclf.setPrclParamRecuperacion1(calificacion.getClfParamRecuperacion1().floatValue());
							}
							
							if(calificacion.getClfParamRecuperacion2()!=null){
								prclf.setPrclParamRecuperacion2(calificacion.getClfParamRecuperacion2().floatValue());
							}
							
							if(calificacion.getClfNotaFinalSemestre()!=null){
								prclf.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre().floatValue());
							}
							em.persist(prclf);
						}catch (Exception e) {
						}
					}else{
						rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						em.merge(rcesAuxPadre);
						Calificacion calificacion = new Calificacion();
						boolean op = false;
						try {
							try {
								calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
								op= true;
							} catch (CalificacionNoEncontradoException e) {
								
							}
							
							
								calificacion.setClfNota1(notEstudiante1.divide(new BigDecimal(contador),2,RoundingMode.DOWN).floatValue());
							
								calificacion.setClfAsistencia1(asistenciaEstudiante1.setScale(2,RoundingMode.DOWN).floatValue());
							
								calificacion.setClfNota2(notEstudiante2.divide(new BigDecimal(contador),2,RoundingMode.DOWN).floatValue());
							
								calificacion.setClfAsistencia2(asistenciaEstudiante2.setScale(2,RoundingMode.DOWN).floatValue());
							
								calificacion.setClfAsistenciaDocente1(asistenciaDocente1.setScale(2,RoundingMode.DOWN).floatValue());
							
								calificacion.setClfAsistenciaDocente2(asistenciaEstudiante2.setScale(2,RoundingMode.DOWN).floatValue());
							
								calificacion.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
							
								calificacion.setClfAsistenciaTotal(asistenciaSuma.floatValue());
							Float porcentaje = asistenciaSuma.floatValue()*(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE)/(asistenciaSumaDocente.floatValue());
								calificacion.setClfPromedioAsistencia(porcentaje);
							
							BigDecimal promedio = notaSuma.divide(new BigDecimal(contador),2, RoundingMode.DOWN);
								calificacion.setClfPromedioNotas(promedio.floatValue());
//								calificacion.setClfSumaP1P2(promedio.floatValue());
								int comp = promedio.compareTo(new BigDecimal(27.5));
								if(comp==0 || comp == 1){
									calificacion.setClfNotaFinalSemestre(promedio.setScale(0, RoundingMode.HALF_UP).floatValue());
								}else{
									calificacion.setClfNotaFinalSemestre(promedio.floatValue());	
								}
							calificacion.setRecordEstudiante(rcesAuxPadre);
							if(op){
								em.merge(calificacion);
							}else{
								em.persist(calificacion);	
							}

							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_3_RECUPERACION_VALUE);
							prclf.setPrclObservacion2(regCliente);
							prclf.setPrclCalificacion(calificacion);
							prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
							
							if(calificacion.getClfNota1()!=null){
								prclf.setPrclNota1(calificacion.getClfNota1().floatValue());
							}
							
							if(calificacion.getClfAsistencia1()!=null){
								prclf.setPrclAsistencia1(new Float(calificacion.getClfAsistencia1()));
							}
							
							if(calificacion.getClfNota2()!=null){
								prclf.setPrclNota2(calificacion.getClfNota2().floatValue());
							}
							
							if(calificacion.getClfAsistencia2()!=null){
								prclf.setPrclAsistencia2(new Float(calificacion.getClfAsistencia2()));
							}
							
							if(calificacion.getClfAsistenciaTotal()!=null){
								prclf.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal().floatValue());
							}
							
							if(calificacion.getClfPromedioAsistencia()!=null){
								prclf.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia().floatValue());
							}
							
							
							if(calificacion.getClfAsistenciaDocente1()!=null){
								prclf.setPrclAsistencia1(calificacion.getClfAsistenciaDocente1().floatValue());
							}
							if(calificacion.getClfAsistenciaDocente2()!=null){
								prclf.setPrclAsistencia2(calificacion.getClfAsistenciaDocente2().floatValue());
							}
							
							if(calificacion.getClfAsistenciaTotalDoc()!=null){
								prclf.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(calificacion.getClfSumaP1P2()!=null){
								prclf.setPrclSumaP1P2(calificacion.getClfSumaP1P2().floatValue());
							}
							
							if(calificacion.getClfPromedioNotas()!=null){
								prclf.setPrclPromedioNotas(calificacion.getClfPromedioNotas().floatValue());
							}
							
							if(calificacion.getClfSupletorio()!=null){
								prclf.setPrclSupletorio(calificacion.getClfSupletorio().floatValue());
							}
							
							
							if(calificacion.getClfParamRecuperacion1()!=null){
								prclf.setPrclParamRecuperacion1(calificacion.getClfParamRecuperacion1().floatValue());
							}
							
							if(calificacion.getClfParamRecuperacion2()!=null){
								prclf.setPrclParamRecuperacion2(calificacion.getClfParamRecuperacion2().floatValue());
							}
							
							if(calificacion.getClfNotaFinalSemestre()!=null){
								prclf.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre().floatValue());
							}
							em.persist(prclf);
						}catch (Exception e) {
						}
					}	
				}
				
			}
			session.getUserTransaction().commit();
		} catch (Exception e) {
			
		}
			
			
		
	}
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasRectificacion(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException{
		//registro de notas
		//busco la record academico
		try {
			session.getUserTransaction().begin();
			 if(estudiante.getRcesIngersoNota()==null||estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE){
					if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota2()==null){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						
						rcesAux.setRcesEstado(estudiante.getRcesEstado());
						Calificacion calificacion = new Calificacion();
						if(estudiante.getClfNota1()!=null){
							calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						
						if(estudiante.getClfNota2()!=null){
							calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaDocente1()!=null){
							calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
						
						if(estudiante.getClfAsistenciaDocente2()!=null){
							calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
						}
						
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						int supletorioAux = estudiante.getClfSupletorio().compareTo(new BigDecimal(-99));
						if(supletorioAux==0){
							calificacion.setClfSupletorio(null);
						}else{
							calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
						}
						
						int paramRecuperacion1Aux = estudiante.getClfParamRecuperacion1().compareTo(new BigDecimal(-99));
						if(paramRecuperacion1Aux==0){
							calificacion.setClfParamRecuperacion1(null);
						}else{
							calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						int paramRecuperacion2Aux = estudiante.getClfParamRecuperacion2().compareTo(new BigDecimal(-99));
						if(paramRecuperacion2Aux==0){
							calificacion.setClfParamRecuperacion2(null);
						}else{
							calificacion.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						
						calificacion.setRecordEstudiante(rcesAux);
						em.persist(calificacion);

						ProcesoCalificacion prclf = new ProcesoCalificacion();
						prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
						prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
						prclf.setPrclObservacion2(regCliente);
						prclf.setPrclCalificacion(calificacion);
						prclf.setPrclAsistenciaDocente2(calificacion.getClfAsistenciaDocente2());
						
						if(estudiante.getClfNota1()!=null){
							prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						
						if(estudiante.getClfNota2()!=null){
							prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
						
						if(estudiante.getClfAsistenciaDocente1()!=null){
							prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
						}
						if(estudiante.getClfAsistenciaDocente2()!=null){
							prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						if(estudiante.getClfSupletorio()!=null){
							prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
						}
						
						
						if(estudiante.getClfParamRecuperacion1()!=null){
							prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion2()!=null){
							prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						em.persist(prclf);
					}
//					
					if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE||estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
						rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
						rcesAux.setRcesEstado(estudiante.getRcesEstado());
						Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
						if(estudiante.getClfNota1()!=null){
							clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						
						if(estudiante.getClfNota2()!=null){
							clfAux.setClfNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							clfAux.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						
						if(estudiante.getClfAsistenciaDocente1()!=null){
							clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
						}
						
						if(estudiante.getClfAsistenciaDocente2()!=null){
							clfAux.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
						}
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							clfAux.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							clfAux.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							clfAux.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							clfAux.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							clfAux.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						int supletorioAux = estudiante.getClfSupletorio().compareTo(new BigDecimal(-99));
						if(supletorioAux==0){
							clfAux.setClfSupletorio(null);
						}else{
							clfAux.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
						}
						
						int paramRecuperacion1Aux = estudiante.getClfParamRecuperacion1().compareTo(new BigDecimal(-99));
						if(paramRecuperacion1Aux==0){
							clfAux.setClfParamRecuperacion1(null);
						}else{
							clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						int paramRecuperacion2Aux = estudiante.getClfParamRecuperacion2().compareTo(new BigDecimal(-99));
						if(paramRecuperacion2Aux==0){
							clfAux.setClfParamRecuperacion2(null);
						}else{
							clfAux.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						
						ProcesoCalificacion prclf = new ProcesoCalificacion();
						prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
						prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
						prclf.setPrclObservacion2(regCliente);
						prclf.setPrclCalificacion(clfAux);
						prclf.setPrclAsistenciaDocente2(clfAux.getClfAsistenciaDocente2());
						
						if(estudiante.getClfNota1()!=null){
							prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante1()!=null){
							prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
						}
						
						if(estudiante.getClfNota2()!=null){
							prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaEstudiante2()!=null){
							prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
						}
						
						if(estudiante.getClfAsistenciaTotal()!=null){
							prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
						}
						
						if(estudiante.getClfPromedioAsistencia()!=null){
							prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
						}
						
						if(estudiante.getClfAsistenciaDocente1()!=null){
							prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
						}
						
						if(estudiante.getClfAsistenciaDocente2()!=null){
							prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
						}
						
						if(estudiante.getClfAsistenciaTotalDoc()!=null){
							prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
						}
						
						if(estudiante.getClfSumaP1P2()!=null){
							prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
						}
						
						if(estudiante.getClfPromedioNotas()!=null){
							prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
						}
						
						if(estudiante.getClfSupletorio()!=null){
							prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion1()!=null){
							prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
						}
						
						if(estudiante.getClfParamRecuperacion2()!=null){
							prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
						}
						
						if(estudiante.getClfNotalFinalSemestre()!=null){
							prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						}
						em.persist(prclf);
					}
				 }
				 ///////////////////////
				 if(estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE || estudiante.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
					 if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_NO_INGRESADA_VALUE||estudiante.getRcesIngersoNota2()==null){
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
							rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							rcesAux.setRcesEstado(estudiante.getRcesEstado());
							
							try {
								Calificacion calificacion = null;
								List<Calificacion> listaCalificacion = new ArrayList<Calificacion>();
								StringBuffer sbsql = new StringBuffer();
								sbsql.append(" Select clf from Calificacion clf ");
								sbsql.append(" where clf.recordEstudiante.rcesMallaCurricularParalelo.mlcrprId =:mlcrprId ");
								sbsql.append(" and clf.recordEstudiante.rcesFichaEstudiante.fcesId =:fcesId ");
								sbsql.append(" order by clf.clfId asc ");
								Query q = em.createQuery(sbsql.toString());
								q.setParameter("mlcrprId", estudiante.getMlcrprId());
								q.setParameter("fcesId", estudiante.getFcesId());
								listaCalificacion = q.getResultList();
								if(listaCalificacion.size()==0){
									throw new NoResultException(); 
								}else if(listaCalificacion.size()>1){
									calificacion = listaCalificacion.get(0);
								}else{
									calificacion = em.find(Calificacion.class, estudiante.getClfId());	
								}
							
								if(estudiante.getClfNota1()!=null){
									calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								int supletorioAux = estudiante.getClfSupletorio().compareTo(new BigDecimal(-99));
								if(supletorioAux==0){
									calificacion.setClfSupletorio(null);
								}else{
									calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								int paramRecuperacion1Aux = estudiante.getClfParamRecuperacion1().compareTo(new BigDecimal(-99));
								if(paramRecuperacion1Aux==0){
									calificacion.setClfParamRecuperacion1(null);
								}else{
									calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								int paramRecuperacion2Aux = estudiante.getClfParamRecuperacion2().compareTo(new BigDecimal(-99));
								if(paramRecuperacion2Aux==0){
									calificacion.setClfParamRecuperacion2(null);
								}else{
									calificacion.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								
								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion2(regCliente);
								prclf.setPrclCalificacion(calificacion);
								prclf.setPrclAsistenciaDocente2(calificacion.getClfAsistenciaDocente2());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.merge(calificacion);
								em.persist(prclf);
							} catch (NullPointerException e) {
								try {
									Calificacion calificacion = null;
									List<Calificacion> listaCalificacion = new ArrayList<Calificacion>();
									StringBuffer sbsql = new StringBuffer();
									sbsql.append(" Select clf from Calificacion clf ");
									sbsql.append(" where clf.recordEstudiante.rcesMallaCurricularParalelo.mlcrprId =:mlcrprId ");
									sbsql.append(" and clf.recordEstudiante.rcesFichaEstudiante.fcesId =:fcesId ");
									sbsql.append(" order by clf.clfId asc ");
									Query q = em.createQuery(sbsql.toString());
									q.setParameter("mlcrprId", estudiante.getMlcrprId());
									q.setParameter("fcesId", estudiante.getFcesId());
									listaCalificacion = q.getResultList();
									if(listaCalificacion.size()==0){
										throw new NoResultException(); 
									}else if(listaCalificacion.size()>1){
										calificacion = listaCalificacion.get(0);
									}else{
										calificacion = em.find(Calificacion.class, estudiante.getClfId());	
									}
									if(estudiante.getClfNota1()!=null){
										calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante1()!=null){
										calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
									}
									
									if(estudiante.getClfNota2()!=null){
										calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante2()!=null){
										calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
									}
									
									if(estudiante.getClfAsistenciaDocente1()!=null){
										calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
									}
									
									if(estudiante.getClfAsistenciaDocente2()!=null){
										calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
									}
									
									
									if(estudiante.getClfAsistenciaTotalDoc()!=null){
										calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
									}
									
									if(estudiante.getClfAsistenciaTotal()!=null){
										calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
									}
									
									if(estudiante.getClfPromedioAsistencia()!=null){
										calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
									}
									
									if(estudiante.getClfSumaP1P2()!=null){
										calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
									}
									
									if(estudiante.getClfPromedioNotas()!=null){
										calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
									}
									
									int supletorioAux = estudiante.getClfSupletorio().compareTo(new BigDecimal(-99));
									if(supletorioAux==0){
										calificacion.setClfSupletorio(null);
									}else{
										calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
									}
									
									int paramRecuperacion1Aux = estudiante.getClfParamRecuperacion1().compareTo(new BigDecimal(-99));
									if(paramRecuperacion1Aux==0){
										calificacion.setClfParamRecuperacion1(null);
									}else{
										calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
									}
									
									int paramRecuperacion2Aux = estudiante.getClfParamRecuperacion2().compareTo(new BigDecimal(-99));
									if(paramRecuperacion2Aux==0){
										calificacion.setClfParamRecuperacion2(null);
									}else{
										calificacion.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
									}
									
									if(estudiante.getClfNotalFinalSemestre()!=null){
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
									}
									
									ProcesoCalificacion prclf = new ProcesoCalificacion();
									prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
									prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
									prclf.setPrclObservacion2(regCliente);
									prclf.setPrclCalificacion(calificacion);
									prclf.setPrclAsistenciaDocente2(calificacion.getClfAsistenciaDocente2());
									
									if(estudiante.getClfNota1()!=null){
										prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante1()!=null){
										prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
									}
									
									if(estudiante.getClfNota2()!=null){
										prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante2()!=null){
										prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
									}
									
									if(estudiante.getClfAsistenciaTotal()!=null){
										prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
									}
									
									if(estudiante.getClfPromedioAsistencia()!=null){
										prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
									}
									
									if(estudiante.getClfAsistenciaDocente1()!=null){
										prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
									}
									
									if(estudiante.getClfAsistenciaDocente2()!=null){
										prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
									}
									
									if(estudiante.getClfAsistenciaTotalDoc()!=null){
										prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
									}
									
									if(estudiante.getClfSumaP1P2()!=null){
										prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
									}
									
									if(estudiante.getClfPromedioNotas()!=null){
										prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
									}
									
									if(estudiante.getClfSupletorio()!=null){
										prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
									}
									
									if(estudiante.getClfParamRecuperacion1()!=null){
										prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
									}
									
									if(estudiante.getClfParamRecuperacion2()!=null){
										prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
									}
									
									if(estudiante.getClfNotalFinalSemestre()!=null){
										prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
									}
									
									em.persist(prclf);
									calificacion.setRecordEstudiante(rcesAux);
									em.merge(calificacion);
								} catch (NoResultException e2) {
									Calificacion calificacion = new Calificacion();
									if(estudiante.getClfNota1()!=null){
										calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante1()!=null){
										calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
									}
									
									if(estudiante.getClfNota2()!=null){
										calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante2()!=null){
										calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
									}
									
									if(estudiante.getClfAsistenciaDocente1()!=null){
										calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
									}
									
									if(estudiante.getClfAsistenciaDocente2()!=null){
										calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
									}
									
									
									if(estudiante.getClfAsistenciaTotalDoc()!=null){
										calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
									}
									
									if(estudiante.getClfAsistenciaTotal()!=null){
										calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
									}
									
									if(estudiante.getClfPromedioAsistencia()!=null){
										calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
									}
									
									if(estudiante.getClfSumaP1P2()!=null){
										calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
									}
									
									if(estudiante.getClfPromedioNotas()!=null){
										calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
									}
									
									int supletorioAux = estudiante.getClfSupletorio().compareTo(new BigDecimal(-99));
									if(supletorioAux==0){
										calificacion.setClfSupletorio(null);
									}else{
										calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
									}
									
									int paramRecuperacion1Aux = estudiante.getClfParamRecuperacion1().compareTo(new BigDecimal(-99));
									if(paramRecuperacion1Aux==0){
										calificacion.setClfParamRecuperacion1(null);
									}else{
										calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
									}
									
									int paramRecuperacion2Aux = estudiante.getClfParamRecuperacion2().compareTo(new BigDecimal(-99));
									if(paramRecuperacion2Aux==0){
										calificacion.setClfParamRecuperacion2(null);
									}else{
										calificacion.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
									}
									
									if(estudiante.getClfNotalFinalSemestre()!=null){
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
									}
									
									ProcesoCalificacion prclf = new ProcesoCalificacion();
									prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
									prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
									prclf.setPrclObservacion2(regCliente);
									prclf.setPrclCalificacion(calificacion);
									prclf.setPrclAsistenciaDocente2(calificacion.getClfAsistenciaDocente2());
									
									if(estudiante.getClfNota1()!=null){
										prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante1()!=null){
										prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
									}
									
									if(estudiante.getClfNota2()!=null){
										prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
									}
									
									if(estudiante.getClfAsistenciaEstudiante2()!=null){
										prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
									}
									
									if(estudiante.getClfAsistenciaTotal()!=null){
										prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
									}
									
									if(estudiante.getClfPromedioAsistencia()!=null){
										prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
									}
									
									if(estudiante.getClfAsistenciaDocente1()!=null){
										prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
									}
									
									if(estudiante.getClfAsistenciaDocente2()!=null){
										prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
									}
									
									if(estudiante.getClfAsistenciaTotalDoc()!=null){
										prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
									}
									
									if(estudiante.getClfSumaP1P2()!=null){
										prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
									}
									
									if(estudiante.getClfPromedioNotas()!=null){
										prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
									}
									
									if(estudiante.getClfSupletorio()!=null){
										prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
									}
									
									if(estudiante.getClfParamRecuperacion1()!=null){
										prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
									}
									
									if(estudiante.getClfParamRecuperacion2()!=null){
										prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
									}
									
									if(estudiante.getClfNotalFinalSemestre()!=null){
										prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
									}
									
									em.persist(prclf);
									calificacion.setRecordEstudiante(rcesAux);
									em.persist(calificacion);
								}
								
							}
							
						}
						if(estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE||estudiante.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
							rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
							rcesAux.setRcesEstado(estudiante.getRcesEstado());
							Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
							
							if(estudiante.getClfNota1()!=null){
								clfAux.setClfNota1(estudiante.getClfNota1().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante1()!=null){
								clfAux.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
							}
							
							if(estudiante.getClfNota2()!=null){
								clfAux.setClfNota2(estudiante.getClfNota2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante2()!=null){
								clfAux.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
							}
							
							if(estudiante.getClfAsistenciaDocente1()!=null){
								clfAux.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
							}
							
							if(estudiante.getClfAsistenciaDocente2()!=null){
								clfAux.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
							}
							
							
							if(estudiante.getClfAsistenciaTotalDoc()!=null){
								clfAux.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(estudiante.getClfAsistenciaTotal()!=null){
								clfAux.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
							}
							
							if(estudiante.getClfPromedioAsistencia()!=null){
								clfAux.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
							}
							
							if(estudiante.getClfSumaP1P2()!=null){
								clfAux.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
							}
							
							if(estudiante.getClfPromedioNotas()!=null){
								clfAux.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
							}
							
							
							int supletorioAux = estudiante.getClfSupletorio().compareTo(new BigDecimal(-99));
							if(supletorioAux==0){
								clfAux.setClfSupletorio(null);
							}else{
								clfAux.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
							}
							
							
							int paramRecuperacion1Aux = estudiante.getClfParamRecuperacion1().compareTo(new BigDecimal(-99));
							if(paramRecuperacion1Aux==0){
								clfAux.setClfParamRecuperacion1(null);
							}else{
								clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
							}
							
							int paramRecuperacion2Aux = estudiante.getClfParamRecuperacion2().compareTo(new BigDecimal(-99));
							if(paramRecuperacion2Aux==0){
								clfAux.setClfParamRecuperacion2(null);
							}else{
								clfAux.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
							}
							
							if(estudiante.getClfNotalFinalSemestre()!=null){
								clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
							}
							
							ProcesoCalificacion prclf = new ProcesoCalificacion();
							prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
							prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
							prclf.setPrclObservacion2(regCliente);
							prclf.setPrclCalificacion(clfAux);
							prclf.setPrclAsistenciaDocente2(clfAux.getClfAsistenciaDocente2());
							
							if(estudiante.getClfNota1()!=null){
								prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante1()!=null){
								prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
							}
							
							if(estudiante.getClfNota2()!=null){
								prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaEstudiante2()!=null){
								prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
							}
							
							if(estudiante.getClfAsistenciaTotal()!=null){
								prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
							}
							
							if(estudiante.getClfPromedioAsistencia()!=null){
								prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
							}
							
							if(estudiante.getClfAsistenciaDocente1()!=null){
								prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
							}
							
							if(estudiante.getClfAsistenciaDocente2()!=null){
								prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
							}
							
							if(estudiante.getClfAsistenciaTotalDoc()!=null){
								prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
							}
							
							if(estudiante.getClfSumaP1P2()!=null){
								prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
							}
							
							if(estudiante.getClfPromedioNotas()!=null){
								prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
							}
							
							if(estudiante.getClfSupletorio()!=null){
								prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
							}
							
							if(estudiante.getClfParamRecuperacion1()!=null){
								prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
							}
							
							if(estudiante.getClfParamRecuperacion2()!=null){
								prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
							}
							
							if(estudiante.getClfNotalFinalSemestre()!=null){
								prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
							}
							
							em.persist(prclf);
						}
				 }
				 
				session.getUserTransaction().commit();
		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		} catch (Exception e1) {
		}
		
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean guardarEdicionNotasPrimerHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException,SQLIntegrityConstraintViolationException{
		//registro de notas
		//busco la record academico
		boolean verificar = false;
		try {
			session.getUserTransaction().begin();
			RecordEstudiante rcesAux = em.find(RecordEstudiante.class, estudiante.getRcesId());
						
			rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
			
			rcesAux.setRcesEstado(estudiante.getRcesEstado());
			
						if(estudiante.isMateriaModulo()){
							CalificacionModulo calificacionModulo = new CalificacionModulo();
								
							try {
									StringBuffer sbsql = new StringBuffer();
									sbsql.append(" Select clmd from CalificacionModulo clmd ");
									sbsql.append(" where clmd.mlcrprIdModulo =:id ");
									sbsql.append(" and clmd.recordEstudiante.rcesId =:rcesId ");
									Query q = em.createQuery(sbsql.toString());
									  q.setParameter("id", estudiante.getMlcrprIdModulo());
									  q.setParameter("rcesId", estudiante.getRcesId());
									  calificacionModulo = (CalificacionModulo) q.getSingleResult();
//								calificacionModulo = em.find(CalificacionModulo.class, estudiante.getClfId());
								if(estudiante.getClfNota1()!=null){
									calificacionModulo.setClmdNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacionModulo.setClmdAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacionModulo.setClmdNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacionModulo.setClmdAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacionModulo.setClmdAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacionModulo.setClmdAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacionModulo.setClmdAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacionModulo.setClmdAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacionModulo.setClmdPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacionModulo.setClmdSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacionModulo.setClmdPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfSupletorio()!=null){
									calificacionModulo.setClmdSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								if(estudiante.getClfParamRecuperacion1()!=null){
									calificacionModulo.setClmdParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								if(estudiante.getClfParamRecuperacion2()!=null){
									calificacionModulo.setClmdParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
									
								}
								calificacionModulo.setRecordEstudiante(rcesAux);
								calificacionModulo.setMlcrprIdModulo(estudiante.getMlcrprIdModulo());
								em.merge(calificacionModulo);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFecha(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion(regCliente);
								prclf.setPrclCalificacionModulo(calificacionModulo);
								prclf.setPrclAsistenciaDocente1(calificacionModulo.getClmdAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
								
							} catch (NoResultException | NullPointerException e) {
								calificacionModulo = new CalificacionModulo();
								if(estudiante.getClfNota1()!=null){
									calificacionModulo.setClmdNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacionModulo.setClmdAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacionModulo.setClmdNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacionModulo.setClmdAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacionModulo.setClmdAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacionModulo.setClmdAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacionModulo.setClmdAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacionModulo.setClmdAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacionModulo.setClmdPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacionModulo.setClmdSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacionModulo.setClmdPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
								}
								calificacionModulo.setRecordEstudiante(rcesAux);
								calificacionModulo.setMlcrprIdModulo(estudiante.getMlcrprIdModulo());
								em.persist(calificacionModulo);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFecha(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion(regCliente);
								prclf.setPrclCalificacionModulo(calificacionModulo);
								prclf.setPrclAsistenciaDocente1(calificacionModulo.getClmdAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
							}			
						}else{
							Calificacion calificacion = new Calificacion();
							try {
								calificacion = em.find(Calificacion.class, estudiante.getClfId());	
								if(estudiante.getClfNota1()!=null){
									calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								if(estudiante.getClfNota2()!=null){
									calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfSupletorio()!=null){
									calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								if(estudiante.getClfParamRecuperacion1()!=null){
									calificacion.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								if(estudiante.getClfParamRecuperacion2()!=null){
									calificacion.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.UP).floatValue());
										rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
										em.merge(rcesAux);
									}else{
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
								}
								calificacion.setRecordEstudiante(rcesAux);
								em.merge(calificacion);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFecha(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion(regCliente);
								prclf.setPrclCalificacion(calificacion);
								prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
								
							} catch (NoResultException | NullPointerException e) {
								calificacion = new Calificacion();
								if(estudiante.getClfNota1()!=null){
									calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
								}
								calificacion.setRecordEstudiante(rcesAux);
								em.persist(calificacion);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFecha(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion(regCliente);
								prclf.setPrclCalificacion(calificacion);
								prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
				}
			}
			em.merge(rcesAux);
			session.getUserTransaction().commit();
			verificar = true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return verificar;
	}
	
	
	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean guardarEdicionNotasPosgrado(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException{
		//registro de notas
		//busco el record academico
		boolean verificar = false;
		try {
			session.getUserTransaction().begin();
			RecordEstudiante rcesAux = em.find(RecordEstudiante.class, estudiante.getRcesId());

			rcesAux.setRcesIngresoNota(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);

			rcesAux.setRcesEstado(estudiante.getRcesEstado());

			Calificacion calificacion = new Calificacion();
			ProcesoCalificacion prclf = new ProcesoCalificacion();
			prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
			prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_POSGRADO_VALUE);
			prclf.setPrclObservacion2(regCliente);
			try {
				calificacion = em.find(Calificacion.class, estudiante.getClfId());
				if (estudiante.getClfNota1() != null) {
					calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
					prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
				}

				if (estudiante.getClfAsistenciaEstudiante1() != null) {
					calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
					prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
				}

				if (estudiante.getClfAsistenciaDocente1() != null) {
					calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
					prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
				}

				if (estudiante.getClfAsistenciaTotalDoc() != null) {
					calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
					prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
				}

				if (estudiante.getClfAsistenciaTotal() != null) {
					calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
					prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
				}

				if (estudiante.getClfPromedioAsistencia() != null) {
					calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
					prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
				}

				if (estudiante.getClfSumaP1P2() != null) {
					calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
					prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
				}
				if (estudiante.getClfPromedioNotas() != null) {
					calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
					prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
				}
				if (estudiante.getClfSupletorio() != null) {
					calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
					prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
				}
				if (estudiante.getClfNotalFinalSemestre() != null) {
					int comp;
					if(estudiante.getCrrTipoEvaluacion()==CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_VALUE){
						comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(16));	
					}else{
						comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(7));
					}
					
					if (comp == 0 || comp == 1) {
						calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						if(rcesAux.getRcesEstado()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);	
						}else{
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
						em.merge(rcesAux);
					} else {
						calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						if(rcesAux.getRcesEstado()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);	
						}else{
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
					}
				}
				calificacion.setRecordEstudiante(rcesAux);
				em.merge(calificacion);

				prclf.setPrclCalificacion(calificacion);
				
				em.persist(prclf);

			} catch (NoResultException | NullPointerException e) {
				calificacion = new Calificacion();
				if (estudiante.getClfNota1() != null) {
					calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
					prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
				}

				if (estudiante.getClfAsistenciaEstudiante1() != null) {
					calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
					prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
				}

				if (estudiante.getClfAsistenciaDocente1() != null) {
					calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
					prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
				}

				if (estudiante.getClfAsistenciaTotalDoc() != null) {
					calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
					prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
				}

				if (estudiante.getClfAsistenciaTotal() != null) {
					calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
					prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
				}

				if (estudiante.getClfPromedioAsistencia() != null) {
					calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
					prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
				}

				if (estudiante.getClfSumaP1P2() != null) {
					calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
					prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
				}
				if (estudiante.getClfPromedioNotas() != null) {
					calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
					prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
				}
				if (estudiante.getClfSupletorio() != null) {
					calificacion.setClfSupletorio(estudiante.getClfSupletorio().floatValue());
					prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
				}
				if (estudiante.getClfNotalFinalSemestre() != null) {
					int comp;
					if(estudiante.getCrrTipoEvaluacion()==CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_VALUE){
						comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(14));	
					}else{
						comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(7));
					}
					
					if (comp == 0 || comp == 1) {
						calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						em.merge(rcesAux);
					} else {
						calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
						rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
					}
				}
				calificacion.setRecordEstudiante(rcesAux);
				em.persist(calificacion);

				prclf.setPrclCalificacion(calificacion);
				
				em.persist(prclf);
			}
			em.merge(rcesAux);
			session.getUserTransaction().commit();
			verificar = true;

		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		}
		return verificar;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean guardarEdicionNotasSegundoHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException{
		//registro de notas
		//busco la record academico
		boolean verificar = false;
		try {
			session.getUserTransaction().begin();
			RecordEstudiante rcesAux = em.find(RecordEstudiante.class, estudiante.getRcesId());
			rcesAux.setRcesEstado(estudiante.getRcesEstado());
			rcesAux.setRcesIngresoNota2(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
			
						if(estudiante.isMateriaModulo()){
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
							CalificacionModulo calificacionModulo = new CalificacionModulo();
								
							try {
								StringBuffer sbsql = new StringBuffer();
								sbsql.append(" Select clmd from CalificacionModulo clmd ");
								sbsql.append(" where clmd.mlcrprIdModulo =:id ");
								sbsql.append(" and clmd.recordEstudiante.rcesId =:rcesId ");
								Query q = em.createQuery(sbsql.toString());
								  q.setParameter("id", estudiante.getMlcrprIdModulo());
								  q.setParameter("rcesId", estudiante.getRcesId());
								  calificacionModulo = (CalificacionModulo) q.getSingleResult();
//								calificacionModulo = em.find(CalificacionModulo.class, estudiante.getClfId());
								if(estudiante.getClfNota1()!=null){
									calificacionModulo.setClmdNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacionModulo.setClmdAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacionModulo.setClmdNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacionModulo.setClmdAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacionModulo.setClmdAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacionModulo.setClmdAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacionModulo.setClmdAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacionModulo.setClmdAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacionModulo.setClmdPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacionModulo.setClmdSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacionModulo.setClmdPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
									
								}
								if(calificacionModulo.getClmdParamRecuperacion1()!=null){
									calificacionModulo.setClmdParamRecuperacion1(calificacionModulo.getClmdSumaP1P2());
									
									
									BigDecimal parametro2Aux  = BigDecimal.ZERO;
									parametro2Aux  = new BigDecimal(calificacionModulo.getClmdSupletorio()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP);
									calificacionModulo.setClmdParamRecuperacion2(parametro2Aux.floatValue());
									BigDecimal parametro1Aux  = BigDecimal.ZERO;
									parametro1Aux = new BigDecimal(calificacionModulo.getClmdSumaP1P2()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP);;
									calificacionModulo.setClmdParamRecuperacion1(parametro1Aux.floatValue());
									
									BigDecimal sumaParametros = BigDecimal.ZERO;
									sumaParametros = new BigDecimal(calificacionModulo.getClmdParamRecuperacion1()).setScale(2, RoundingMode.DOWN).add(new BigDecimal(calificacionModulo.getClmdParamRecuperacion2()).setScale(2, RoundingMode.DOWN));
									calificacionModulo.setClmdNotaFinalSemestre(sumaParametros.floatValue());
									int estadoRces = new BigDecimal(calificacionModulo.getClmdNotaFinalSemestre()).compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
									if(estadoRces == 1 || estadoRces ==0){
										calificacionModulo.setClmdNotaFinalSemestre(sumaParametros.floatValue());
									}else{
										calificacionModulo.setClmdNotaFinalSemestre(sumaParametros.floatValue());
									}
									
								}
								calificacionModulo.setRecordEstudiante(rcesAux);
								calificacionModulo.setMlcrprIdModulo(estudiante.getMlcrprIdModulo());
								em.persist(calificacionModulo);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion2(regCliente);
								prclf.setPrclCalificacionModulo(calificacionModulo);
								prclf.setPrclAsistenciaDocente1(calificacionModulo.getClmdAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
								
							} catch (NoResultException | NullPointerException e) {
								calificacionModulo = new CalificacionModulo();
								if(estudiante.getClfNota1()!=null){
									calificacionModulo.setClmdNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacionModulo.setClmdAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacionModulo.setClmdNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacionModulo.setClmdAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacionModulo.setClmdAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacionModulo.setClmdAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacionModulo.setClmdAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacionModulo.setClmdAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacionModulo.setClmdPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacionModulo.setClmdSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacionModulo.setClmdPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacionModulo.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
								}
								calificacionModulo.setRecordEstudiante(rcesAux);
								calificacionModulo.setMlcrprIdModulo(estudiante.getMlcrprIdModulo());
								em.persist(calificacionModulo);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion2(regCliente);
								prclf.setPrclCalificacionModulo(calificacionModulo);
								prclf.setPrclAsistenciaDocente1(calificacionModulo.getClmdAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
							}	

				try {
					Materia mtrAux = srvMateriaServicio.buscarMateriaXMlCrPrPeriodoPregradoActivo(estudiante.getMlcrprIdModulo());
					Materia MateriaPadre =  srvMateriaServicio.buscarPorId(mtrAux.getMtrMateria().getMtrId());
					List<Materia> listaModulos = new ArrayList<>();
					listaModulos = srvMateriaServicio.listarTodosModulos(MateriaPadre.getMtrId());
					RecordEstudiante rcesAuxPadre= srvRecordEstudianteServicio.buscarPorIdSingle(rcesAux.getRcesId());
					List<CalificacionModulo> listaClmd = new ArrayList<CalificacionModulo>();
					listaClmd = buscarClMdPorRces(rcesAuxPadre.getRcesId());
					if(listaModulos.size()==listaClmd.size()){
						BigDecimal asistenciaPromedio = BigDecimal.ZERO;
						BigDecimal asistenciaDocente = BigDecimal.ZERO;
						BigDecimal asistenciaSuma = BigDecimal.ZERO;
						BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
						BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
						BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
						BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
						BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
						BigDecimal notEstudiante2 = BigDecimal.ZERO;
						BigDecimal notEstudiante1 = BigDecimal.ZERO;
						BigDecimal notaSuma = BigDecimal.ZERO;
						BigDecimal notaPromedio = BigDecimal.ZERO;
						int ubicacion = 0;
						int ubicacionRecuperacion = 0;
						int contador = 0;
						boolean opAsistencia = false;
						boolean opNota = false;
						boolean opSupletorio = false;
						boolean opModulosCompletos = true;
						Float asistenciaMenor = Float.valueOf(100);
						Float notaMenor = Float.valueOf(100);
						lazo:for (CalificacionModulo item : listaClmd) {
							if(item.getClmdSumaP1P2()!=null || item.getClmdSumaP1P2()!=GeneralesConstantes.APP_ID_BASE.floatValue()){
								int comparadorNota = (new BigDecimal(item.getClmdSumaP1P2()).setScale(2, RoundingMode.DOWN)).compareTo(new BigDecimal(8.8));
								if(comparadorNota>=0){
									comparadorNota = (new BigDecimal(item.getClmdSumaP1P2()).setScale(2, RoundingMode.DOWN)).compareTo(new BigDecimal(27.5));
									if(comparadorNota==-1){
//										if(estudiante.getDtmtNumero()==3){
//											opModulosCompletos=false;
//											rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);	
//											notaMenor = item.getClmdSumaP1P2();
//											ubicacionRecuperacion = contador;
//											break lazo;
//										}else{
											rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);	
//										}
										
										opSupletorio = true;
										if(item.getClmdPromedioAsistencia()<asistenciaMenor){
											asistenciaMenor = item.getClmdPromedioAsistencia();
											ubicacionRecuperacion = contador;
										}
										if(comparadorNota == -1){
											if(item.getClmdSumaP1P2()<notaMenor){
												notaMenor = item.getClmdSumaP1P2();
												ubicacionRecuperacion = contador;
											}		
										}
									}else{
										notaSuma=notaSuma.add(new BigDecimal(item.getClmdSumaP1P2()).setScale(2, RoundingMode.DOWN));
										asistenciaSuma=asistenciaSuma.add(new BigDecimal(item.getClmdAsistenciaTotal()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
										asistenciaSumaDocente =asistenciaSumaDocente.add(new BigDecimal(item.getClmdAsistenciaTotalDoc()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
										asistenciaDocente1=asistenciaDocente1.add(new BigDecimal(item.getClmdAsistenciaDocente1()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
										asistenciaDocente2=asistenciaDocente2.add(new BigDecimal(item.getClmdAsistenciaDocente2()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
										asistenciaEstudiante1=asistenciaEstudiante1.add(new BigDecimal(item.getClmdAsistencia1()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
										asistenciaEstudiante2=asistenciaEstudiante2.add(new BigDecimal(item.getClmdAsistencia2()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
										notEstudiante1=notEstudiante1.add(new BigDecimal(item.getClmdNota1()));
										notEstudiante2=notEstudiante2.add(new BigDecimal(item.getClmdNota2()));
									}
								}else{
									opNota=true;
									rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									if(item.getClmdPromedioAsistencia()<asistenciaMenor){
										asistenciaMenor = item.getClmdPromedioAsistencia();
										ubicacionRecuperacion = contador;
									}
									if(item.getClmdSumaP1P2()<notaMenor){
										notaMenor = item.getClmdSumaP1P2();
										ubicacionRecuperacion = contador;
									}
								}
								contador++;
								
							}else{
								opModulosCompletos=false;
								break;
							}
							
						}
						asistenciaPromedio=asistenciaPromedio.add(asistenciaSuma.divide(asistenciaSumaDocente,2,RoundingMode.DOWN).multiply(new BigDecimal(100)));
						if(opModulosCompletos){
							if(!opNota && !opSupletorio){
								int comparadorAsistencia = asistenciaPromedio.compareTo(new BigDecimal(80));
								Calificacion calificacion = new Calificacion();
								boolean op = false;
								if(comparadorAsistencia==1 || comparadorAsistencia==0){
									rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
									try {
										calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
									} catch (CalificacionNoEncontradoException e) {
										
									}
									op= false;
								}else{
									rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									try {
										calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
									} catch (CalificacionNoEncontradoException e) {
										
									}
									op= true;
								}
								em.merge(rcesAuxPadre);
								
								if(op){
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdNota1()!=null){
										calificacion.setClfNota1(listaClmd.get(ubicacionRecuperacion).getClmdNota1().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia1()!=null){
										calificacion.setClfAsistencia1(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia1()));
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdNota2()!=null){
										calificacion.setClfNota2(listaClmd.get(ubicacionRecuperacion).getClmdNota2().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia2()!=null){
										calificacion.setClfAsistencia2(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia2()));
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente1()!=null){
										calificacion.setClfAsistenciaDocente1(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente1()));
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente2()!=null){
										calificacion.setClfAsistenciaDocente2(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente2()));
									}
									
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotalDoc()!=null){
										calificacion.setClfAsistenciaTotalDoc(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotalDoc().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotal()!=null){
										calificacion.setClfAsistenciaTotal(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotal().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdPromedioAsistencia()!=null){
										calificacion.setClfPromedioAsistencia(listaClmd.get(ubicacionRecuperacion).getClmdPromedioAsistencia().floatValue());
									}	
									
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdSumaP1P2()!=null){
										calificacion.setClfSumaP1P2(listaClmd.get(ubicacionRecuperacion).getClmdSumaP1P2().floatValue());
									}
									if(listaClmd.get(ubicacionRecuperacion).getClmdPromedioNotas()!=null){
										calificacion.setClfPromedioNotas(listaClmd.get(ubicacionRecuperacion).getClmdPromedioNotas().floatValue());
									}
									if(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre()!=null){
										int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
										if(comp==0 || comp == 1){
											calificacion.setClfNotaFinalSemestre(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre().floatValue());	
										}else{
											calificacion.setClfNotaFinalSemestre(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre().floatValue());	
										}
									}
									calificacion.setRecordEstudiante(rcesAux);
									em.persist(calificacion);	
									

									ProcesoCalificacion prclf = new ProcesoCalificacion();
									prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
									prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
									prclf.setPrclObservacion2(regCliente);
									prclf.setPrclCalificacion(calificacion);
									prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
									
									if(calificacion.getClfNota1()!=null){
										prclf.setPrclNota1(calificacion.getClfNota1().floatValue());
									}
									
									if(calificacion.getClfAsistencia1()!=null){
										prclf.setPrclAsistencia1(new Float(calificacion.getClfAsistencia1()));
									}
									
									if(calificacion.getClfNota2()!=null){
										prclf.setPrclNota2(calificacion.getClfNota2().floatValue());
									}
									
									if(calificacion.getClfAsistencia2()!=null){
										prclf.setPrclAsistencia2(new Float(calificacion.getClfAsistencia2()));
									}
									
									if(calificacion.getClfAsistenciaTotal()!=null){
										prclf.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal().floatValue());
									}
									
									if(calificacion.getClfPromedioAsistencia()!=null){
										prclf.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia().floatValue());
									}
									
									
									if(calificacion.getClfAsistenciaDocente1()!=null){
										prclf.setPrclAsistencia1(calificacion.getClfAsistenciaDocente1().floatValue());
									}
									if(calificacion.getClfAsistenciaDocente2()!=null){
										prclf.setPrclAsistencia2(calificacion.getClfAsistenciaDocente2().floatValue());
									}
									
									if(calificacion.getClfAsistenciaTotalDoc()!=null){
										prclf.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc().floatValue());
									}
									
									if(calificacion.getClfSumaP1P2()!=null){
										prclf.setPrclSumaP1P2(calificacion.getClfSumaP1P2().floatValue());
									}
									
									if(calificacion.getClfPromedioNotas()!=null){
										prclf.setPrclPromedioNotas(calificacion.getClfPromedioNotas().floatValue());
									}
									
									if(calificacion.getClfSupletorio()!=null){
										prclf.setPrclSupletorio(calificacion.getClfSupletorio().floatValue());
									}
									
									
									if(calificacion.getClfParamRecuperacion1()!=null){
										prclf.setPrclParamRecuperacion1(calificacion.getClfParamRecuperacion1().floatValue());
									}
									
									if(calificacion.getClfParamRecuperacion2()!=null){
										prclf.setPrclParamRecuperacion2(calificacion.getClfParamRecuperacion2().floatValue());
									}
									
									if(calificacion.getClfNotaFinalSemestre()!=null){
										prclf.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre().floatValue());
									}
									em.persist(prclf);
								}else{
									try {
										try {
											calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
										} catch (CalificacionNoEncontradoException e) {
											
										}
										
										
											calificacion.setClfNota1(notEstudiante1.divide(new BigDecimal(contador),2,RoundingMode.DOWN).floatValue());
										
											calificacion.setClfAsistencia1(asistenciaEstudiante1.setScale(2,RoundingMode.DOWN).floatValue());
										
											calificacion.setClfNota2(notEstudiante2.divide(new BigDecimal(contador),2,RoundingMode.DOWN).floatValue());
										
											calificacion.setClfAsistencia2(asistenciaEstudiante2.setScale(2,RoundingMode.DOWN).floatValue());
										
											calificacion.setClfAsistenciaDocente1(asistenciaDocente1.setScale(2,RoundingMode.DOWN).floatValue());
										
											calificacion.setClfAsistenciaDocente2(asistenciaEstudiante2.setScale(2,RoundingMode.DOWN).floatValue());
										
											calificacion.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
										
											calificacion.setClfAsistenciaTotal(asistenciaSuma.floatValue());
										Float porcentaje = asistenciaSuma.floatValue()*(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE)/(asistenciaSumaDocente.floatValue());
											calificacion.setClfPromedioAsistencia(porcentaje);
										
										BigDecimal promedio = notaSuma.divide(new BigDecimal(contador),2, RoundingMode.DOWN);
											calificacion.setClfPromedioNotas(promedio.floatValue());
											calificacion.setClfSumaP1P2(promedio.floatValue());
											int comp = promedio.compareTo(new BigDecimal(27.5));
											if(comp==0 || comp == 1){
												calificacion.setClfNotaFinalSemestre(promedio.setScale(0, RoundingMode.HALF_UP).floatValue());
											}else{
												calificacion.setClfNotaFinalSemestre(promedio.floatValue());	
											}
										calificacion.setRecordEstudiante(rcesAux);
											em.persist(calificacion);	
										

										ProcesoCalificacion prclf = new ProcesoCalificacion();
										prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
										prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
										prclf.setPrclObservacion2(regCliente);
										prclf.setPrclCalificacion(calificacion);
										prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
										
										if(calificacion.getClfNota1()!=null){
											prclf.setPrclNota1(calificacion.getClfNota1().floatValue());
										}
										
										if(calificacion.getClfAsistencia1()!=null){
											prclf.setPrclAsistencia1(new Float(calificacion.getClfAsistencia1()));
										}
										
										if(calificacion.getClfNota2()!=null){
											prclf.setPrclNota2(calificacion.getClfNota2().floatValue());
										}
										
										if(calificacion.getClfAsistencia2()!=null){
											prclf.setPrclAsistencia2(new Float(calificacion.getClfAsistencia2()));
										}
										
										if(calificacion.getClfAsistenciaTotal()!=null){
											prclf.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal().floatValue());
										}
										
										if(calificacion.getClfPromedioAsistencia()!=null){
											prclf.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia().floatValue());
										}
										
										
										if(calificacion.getClfAsistenciaDocente1()!=null){
											prclf.setPrclAsistencia1(calificacion.getClfAsistenciaDocente1().floatValue());
										}
										if(calificacion.getClfAsistenciaDocente2()!=null){
											prclf.setPrclAsistencia2(calificacion.getClfAsistenciaDocente2().floatValue());
										}
										
										if(calificacion.getClfAsistenciaTotalDoc()!=null){
											prclf.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc().floatValue());
										}
										
										if(calificacion.getClfSumaP1P2()!=null){
											prclf.setPrclSumaP1P2(calificacion.getClfSumaP1P2().floatValue());
										}
										
										if(calificacion.getClfPromedioNotas()!=null){
											prclf.setPrclPromedioNotas(calificacion.getClfPromedioNotas().floatValue());
										}
										
										if(calificacion.getClfSupletorio()!=null){
											prclf.setPrclSupletorio(calificacion.getClfSupletorio().floatValue());
										}
										
										
										if(calificacion.getClfParamRecuperacion1()!=null){
											prclf.setPrclParamRecuperacion1(calificacion.getClfParamRecuperacion1().floatValue());
										}
										
										if(calificacion.getClfParamRecuperacion2()!=null){
											prclf.setPrclParamRecuperacion2(calificacion.getClfParamRecuperacion2().floatValue());
										}
										
										if(calificacion.getClfNotaFinalSemestre()!=null){
											prclf.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre().floatValue());
										}
										em.persist(prclf);
									}catch (Exception e) {
									}
								}
							}else {
								em.merge(rcesAuxPadre);
								Calificacion calificacion = new Calificacion();
								boolean op1 = false;
								try {
									try {
										calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
										op1= true;
									} catch (CalificacionNoEncontradoException e) {
										
									}

									
									if(listaClmd.get(ubicacionRecuperacion).getClmdNota1()!=null){
										calificacion.setClfNota1(listaClmd.get(ubicacionRecuperacion).getClmdNota1().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia1()!=null){
										calificacion.setClfAsistencia1(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia1()));
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdNota2()!=null){
										calificacion.setClfNota2(listaClmd.get(ubicacionRecuperacion).getClmdNota2().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia2()!=null){
										calificacion.setClfAsistencia2(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia2()));
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente1()!=null){
										calificacion.setClfAsistenciaDocente1(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente1()));
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente2()!=null){
										calificacion.setClfAsistenciaDocente2(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente2()));
									}
									
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotalDoc()!=null){
										calificacion.setClfAsistenciaTotalDoc(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotalDoc().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotal()!=null){
										calificacion.setClfAsistenciaTotal(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotal().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdPromedioAsistencia()!=null){
										calificacion.setClfPromedioAsistencia(listaClmd.get(ubicacionRecuperacion).getClmdPromedioAsistencia().floatValue());
									}
									
									if(listaClmd.get(ubicacionRecuperacion).getClmdSumaP1P2()!=null){
										calificacion.setClfSumaP1P2(listaClmd.get(ubicacionRecuperacion).getClmdSumaP1P2().floatValue());
									}
									if(listaClmd.get(ubicacionRecuperacion).getClmdPromedioNotas()!=null){
										calificacion.setClfPromedioNotas(listaClmd.get(ubicacionRecuperacion).getClmdPromedioNotas().floatValue());
									}
									if(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre()!=null){
										calificacion.setClfNotaFinalSemestre(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre().floatValue());	
									}
									calificacion.setRecordEstudiante(rcesAux);
									if(op1){
										em.merge(calificacion);
									}else{
										em.persist(calificacion);	
									}
									

									ProcesoCalificacion prclf = new ProcesoCalificacion();
									prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
									prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
									prclf.setPrclObservacion2(regCliente);
									prclf.setPrclCalificacion(calificacion);
									prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
									
									if(calificacion.getClfNota1()!=null){
										prclf.setPrclNota1(calificacion.getClfNota1().floatValue());
									}
									
									if(calificacion.getClfAsistencia1()!=null){
										prclf.setPrclAsistencia1(new Float(calificacion.getClfAsistencia1()));
									}
									
									if(calificacion.getClfNota2()!=null){
										prclf.setPrclNota2(calificacion.getClfNota2().floatValue());
									}
									
									if(calificacion.getClfAsistencia2()!=null){
										prclf.setPrclAsistencia2(new Float(calificacion.getClfAsistencia2()));
									}
									
									if(calificacion.getClfAsistenciaTotal()!=null){
										prclf.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal().floatValue());
									}
									
									if(calificacion.getClfPromedioAsistencia()!=null){
										prclf.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia().floatValue());
									}
									
									
									if(calificacion.getClfAsistenciaDocente1()!=null){
										prclf.setPrclAsistencia1(calificacion.getClfAsistenciaDocente1().floatValue());
									}
									if(calificacion.getClfAsistenciaDocente2()!=null){
										prclf.setPrclAsistencia2(calificacion.getClfAsistenciaDocente2().floatValue());
									}
									
									if(calificacion.getClfAsistenciaTotalDoc()!=null){
										prclf.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc().floatValue());
									}
									
									if(calificacion.getClfSumaP1P2()!=null){
										prclf.setPrclSumaP1P2(calificacion.getClfSumaP1P2().floatValue());
									}
									
									if(calificacion.getClfPromedioNotas()!=null){
										prclf.setPrclPromedioNotas(calificacion.getClfPromedioNotas().floatValue());
									}
									
									if(calificacion.getClfSupletorio()!=null){
										prclf.setPrclSupletorio(calificacion.getClfSupletorio().floatValue());
									}
									
									
									if(calificacion.getClfParamRecuperacion1()!=null){
										prclf.setPrclParamRecuperacion1(calificacion.getClfParamRecuperacion1().floatValue());
									}
									
									if(calificacion.getClfParamRecuperacion2()!=null){
										prclf.setPrclParamRecuperacion2(calificacion.getClfParamRecuperacion2().floatValue());
									}
									
									if(calificacion.getClfNotaFinalSemestre()!=null){
										prclf.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre().floatValue());
									}
									em.persist(prclf);
								}catch (Exception e) {
								}							
							}	
						}
						if(rcesAuxPadre.getRcesEstado()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
							em.merge(rcesAuxPadre);
							Calificacion calificacion = new Calificacion();
							boolean op1 = false;
							try {
								try {
									calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
									op1= true;
								} catch (CalificacionNoEncontradoException e) {
									
								}

								
								if(listaClmd.get(ubicacionRecuperacion).getClmdNota1()!=null){
									calificacion.setClfNota1(listaClmd.get(ubicacionRecuperacion).getClmdNota1().floatValue());
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia1()!=null){
									calificacion.setClfAsistencia1(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia1()));
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdNota2()!=null){
									calificacion.setClfNota2(listaClmd.get(ubicacionRecuperacion).getClmdNota2().floatValue());
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia2()!=null){
									calificacion.setClfAsistencia2(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistencia2()));
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente1()!=null){
									calificacion.setClfAsistenciaDocente1(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente1()));
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente2()!=null){
									calificacion.setClfAsistenciaDocente2(new Float(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaDocente2()));
								}
								
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotalDoc()!=null){
									calificacion.setClfAsistenciaTotalDoc(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotalDoc().floatValue());
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotal()!=null){
									calificacion.setClfAsistenciaTotal(listaClmd.get(ubicacionRecuperacion).getClmdAsistenciaTotal().floatValue());
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(listaClmd.get(ubicacionRecuperacion).getClmdPromedioAsistencia().floatValue());
								}
								
								if(listaClmd.get(ubicacionRecuperacion).getClmdSumaP1P2()!=null){
									calificacion.setClfSumaP1P2(listaClmd.get(ubicacionRecuperacion).getClmdSumaP1P2().floatValue());
								}
								if(listaClmd.get(ubicacionRecuperacion).getClmdPromedioNotas()!=null){
									calificacion.setClfPromedioNotas(listaClmd.get(ubicacionRecuperacion).getClmdPromedioNotas().floatValue());
								}
								if(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre()!=null){
									calificacion.setClfNotaFinalSemestre(listaClmd.get(ubicacionRecuperacion).getClmdNotaFinalSemestre().floatValue());	
								}
								calificacion.setRecordEstudiante(rcesAux);
								if(op1){
									em.merge(calificacion);
								}else{
									em.persist(calificacion);	
								}
								

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion2(regCliente);
								prclf.setPrclCalificacion(calificacion);
								prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
								
								if(calificacion.getClfNota1()!=null){
									prclf.setPrclNota1(calificacion.getClfNota1().floatValue());
								}
								
								if(calificacion.getClfAsistencia1()!=null){
									prclf.setPrclAsistencia1(new Float(calificacion.getClfAsistencia1()));
								}
								
								if(calificacion.getClfNota2()!=null){
									prclf.setPrclNota2(calificacion.getClfNota2().floatValue());
								}
								
								if(calificacion.getClfAsistencia2()!=null){
									prclf.setPrclAsistencia2(new Float(calificacion.getClfAsistencia2()));
								}
								
								if(calificacion.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal().floatValue());
								}
								
								if(calificacion.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(calificacion.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(calificacion.getClfAsistenciaDocente1().floatValue());
								}
								if(calificacion.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(calificacion.getClfAsistenciaDocente2().floatValue());
								}
								
								if(calificacion.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(calificacion.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(calificacion.getClfSumaP1P2().floatValue());
								}
								
								if(calificacion.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(calificacion.getClfPromedioNotas().floatValue());
								}
								
								if(calificacion.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(calificacion.getClfSupletorio().floatValue());
								}
								
								
								if(calificacion.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(calificacion.getClfParamRecuperacion1().floatValue());
								}
								
								if(calificacion.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(calificacion.getClfParamRecuperacion2().floatValue());
								}
								
								if(calificacion.getClfNotaFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre().floatValue());
								}
								em.persist(prclf);
							}catch (Exception e) {
							}	
						}
					}
					
				} catch (Exception e) {
				}
								
			}else{
							rcesAux.setRcesEstado(estudiante.getRcesEstado());
							em.persist(rcesAux);
							Calificacion calificacion = new Calificacion();
							try {
//								List<Calificacion> listaCalificacion = new ArrayList<Calificacion>();
//								StringBuffer sbsql = new StringBuffer();
//								sbsql.append(" Select clf from Calificacion clf ");
//								sbsql.append(" where clf.recordEstudiante.rcesMallaCurricularParalelo.mlcrprId =:mlcrprId ");
//								sbsql.append(" and clf.recordEstudiante.rcesFichaEstudiante.fcesId =:fcesId ");
//								sbsql.append(" order by clf.clfId asc ");
//								Query q = em.createQuery(sbsql.toString());
//								q.setParameter("mlcrprId", estudiante.getMlcrprId());
//								q.setParameter("fcesId", estudiante.getFcesId());
//								listaCalificacion = q.getResultList();
//								if(listaCalificacion.size()==0){
//									throw new NoResultException(); 
//								}else if(listaCalificacion.size()>1){
//									calificacion = listaCalificacion.get(0);
//								}else{
									calificacion = em.find(Calificacion.class, estudiante.getClfId());	
//								}
								if(estudiante.getClfNota1()!=null){
									calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
								}
								if(calificacion.getClfParamRecuperacion1()!=null){
									calificacion.setClfParamRecuperacion1(calificacion.getClfSumaP1P2());
									
									
									BigDecimal parametro2Aux  = BigDecimal.ZERO;
									parametro2Aux  = new BigDecimal(calificacion.getClfSupletorio()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP);
									calificacion.setClfParamRecuperacion2(parametro2Aux.floatValue());
									BigDecimal parametro1Aux  = BigDecimal.ZERO;
									parametro1Aux = new BigDecimal(calificacion.getClfSumaP1P2()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP);;
									calificacion.setClfParamRecuperacion1(parametro1Aux.floatValue());
									
									BigDecimal sumaParametros = BigDecimal.ZERO;
									sumaParametros = new BigDecimal(calificacion.getClfParamRecuperacion1()).setScale(2, RoundingMode.DOWN).add(new BigDecimal(calificacion.getClfParamRecuperacion2()).setScale(2, RoundingMode.DOWN));
									calificacion.setClfNotaFinalSemestre(sumaParametros.floatValue());
									int estadoRces = new BigDecimal(calificacion.getClfNotaFinalSemestre()).compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
									if(estadoRces == 1 || estadoRces ==0){
										calificacion.setClfNotaFinalSemestre(sumaParametros.floatValue());
										rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
									}else{
										calificacion.setClfNotaFinalSemestre(sumaParametros.floatValue());
										rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
									
								}
								calificacion.setRecordEstudiante(rcesAux);
								em.merge(calificacion);

								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion2(regCliente);
								prclf.setPrclCalificacion(calificacion);
								prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
								
							} catch (NoResultException | NullPointerException e) {
								calificacion = new Calificacion();
								if(estudiante.getClfNota1()!=null){
									calificacion.setClfNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									calificacion.setClfAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									calificacion.setClfNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									calificacion.setClfAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									calificacion.setClfAsistenciaDocente1(new Float(estudiante.getClfAsistenciaDocente1()));
								}
								
								if(estudiante.getClfAsistenciaDocente2()!=null){
									calificacion.setClfAsistenciaDocente2(new Float(estudiante.getClfAsistenciaDocente2()));
								}
								
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									calificacion.setClfAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									calificacion.setClfAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									calificacion.setClfPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									calificacion.setClfSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								if(estudiante.getClfPromedioNotas()!=null){
									calificacion.setClfPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								if(estudiante.getClfNotalFinalSemestre()!=null){
									int comp = estudiante.getClfNotalFinalSemestre().compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().setScale(0, RoundingMode.HALF_EVEN).floatValue());	
									}else{
										calificacion.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());	
									}
								}
								calificacion.setRecordEstudiante(rcesAux);
								em.persist(calificacion);
//
								ProcesoCalificacion prclf = new ProcesoCalificacion();
								prclf.setPrclFechaNota2(new Timestamp(new Date().getTime()));
								prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE);
								prclf.setPrclObservacion2(regCliente);
								prclf.setPrclCalificacion(calificacion);
								prclf.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
								
								if(estudiante.getClfNota1()!=null){
									prclf.setPrclNota1(estudiante.getClfNota1().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante1()!=null){
									prclf.setPrclAsistencia1(new Float(estudiante.getClfAsistenciaEstudiante1()));
								}
								
								if(estudiante.getClfNota2()!=null){
									prclf.setPrclNota2(estudiante.getClfNota2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaEstudiante2()!=null){
									prclf.setPrclAsistencia2(new Float(estudiante.getClfAsistenciaEstudiante2()));
								}
								
								if(estudiante.getClfAsistenciaTotal()!=null){
									prclf.setPrclAsistenciaTotal(estudiante.getClfAsistenciaTotal().floatValue());
								}
								
								if(estudiante.getClfPromedioAsistencia()!=null){
									prclf.setPrclPromedioAsistencia(estudiante.getClfPromedioAsistencia().floatValue());
								}
								
								
								if(estudiante.getClfAsistenciaDocente1()!=null){
									prclf.setPrclAsistencia1(estudiante.getClfAsistenciaDocente1().floatValue());
								}
								if(estudiante.getClfAsistenciaDocente2()!=null){
									prclf.setPrclAsistencia2(estudiante.getClfAsistenciaDocente2().floatValue());
								}
								
								if(estudiante.getClfAsistenciaTotalDoc()!=null){
									prclf.setPrclAsistenciaTotalDoc(estudiante.getClfAsistenciaTotalDoc().floatValue());
								}
								
								if(estudiante.getClfSumaP1P2()!=null){
									prclf.setPrclSumaP1P2(estudiante.getClfSumaP1P2().floatValue());
								}
								
								if(estudiante.getClfPromedioNotas()!=null){
									prclf.setPrclPromedioNotas(estudiante.getClfPromedioNotas().floatValue());
								}
								
								if(estudiante.getClfSupletorio()!=null){
									prclf.setPrclSupletorio(estudiante.getClfSupletorio().floatValue());
								}
								
								
								if(estudiante.getClfParamRecuperacion1()!=null){
									prclf.setPrclParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
								}
								
								if(estudiante.getClfParamRecuperacion2()!=null){
									prclf.setPrclParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
								}
								
								if(estudiante.getClfNotalFinalSemestre()!=null){
									prclf.setPrclNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());
								}
								em.persist(prclf);
							}catch (Exception e) {
							}
				 }
				
				session.getUserTransaction().commit();
				verificar = true;
				
		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		}
		return verificar;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasRectificacionRecuperacion(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente){
		try {
			session.getUserTransaction().begin();
				RecordEstudiante rcesAux = em.find(RecordEstudiante.class, recordEstudianteDto.getRcesId());
				rcesAux.setRcesIngresoNota3(RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE);
				rcesAux.setRcesEstado(estudiante.getRcesEstado());
				
				Calificacion clfAux = em.find(Calificacion.class, estudiante.getClfId());
				if(estudiante.getClfSupletorio()!=null){
					clfAux.setClfSupletorio(new Float(estudiante.getClfSupletorio().floatValue()));
				}
				
				if(estudiante.getClfParamRecuperacion1()!=null){
					clfAux.setClfParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
				}
				if(estudiante.getClfParamRecuperacion2()!=null){
					clfAux.setClfParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
				}
				
				if(estudiante.getClfNotalFinalSemestre()!=null){
					clfAux.setClfNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());;
				}
				
				ProcesoCalificacion prclf = new ProcesoCalificacion();
				prclf.setPrclFechaRecuperacion(new Timestamp(new Date().getTime()));
				prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_3_RECUPERACION_VALUE);
				prclf.setPrclObservacion3(regCliente);
				prclf.setPrclCalificacion(clfAux);
				prclf.setPrclSupletorio(clfAux.getClfSupletorio());
				//TODO completar auditoria
				em.merge(clfAux);
				em.persist(prclf);
				em.merge(rcesAux);
			session.getUserTransaction().commit();
		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		}
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarNotasRectificacionRecuperacionModular(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente){
		try {
			session.getUserTransaction().begin();
				CalificacionModulo clfAux = new CalificacionModulo();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clmd from CalificacionModulo clmd ");
			sbsql.append(" where clmd.mlcrprIdModulo =:id ");
			sbsql.append(" and clmd.recordEstudiante.rcesId =:rcesId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("id", estudiante.getMlcrprIdModulo());
			  q.setParameter("rcesId", estudiante.getRcesId());
			  clfAux = (CalificacionModulo) q.getSingleResult();
				if(estudiante.getClfSupletorio()!=null){
					clfAux.setClmdSupletorio(new Float(estudiante.getClfSupletorio().floatValue()));
				}
				
				if(estudiante.getClfParamRecuperacion1()!=null){
					clfAux.setClmdParamRecuperacion1(estudiante.getClfParamRecuperacion1().floatValue());
				}
				if(estudiante.getClfParamRecuperacion2()!=null){
					clfAux.setClmdParamRecuperacion2(estudiante.getClfParamRecuperacion2().floatValue());
				}
				
				if(estudiante.getClfNotalFinalSemestre()!=null){
					clfAux.setClmdNotaFinalSemestre(estudiante.getClfNotalFinalSemestre().floatValue());;
				}
				
				ProcesoCalificacion prclf = new ProcesoCalificacion();
				prclf.setPrclFechaRecuperacion(new Timestamp(new Date().getTime()));
				prclf.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_3_RECUPERACION_VALUE);
				prclf.setPrclObservacion3(regCliente);
				prclf.setPrclCalificacionModulo(clfAux);
				prclf.setPrclSupletorio(clfAux.getClmdSupletorio());
				//TODO completar auditoria
				em.merge(clfAux);
				em.persist(prclf);
				
				
			
				
			session.getUserTransaction().commit();
		} catch (IllegalStateException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (NotSupportedException e1) {
		}
		
	}
	
	
	/**MQ
	 * Busca un calificacion por Id de recordEstudiante
	 * @param rcesId - id de la recordestudiante a buscar
	 * @return Calificacion. 
	 * 
	 */
	@Override
	public Calificacion buscarPorRces(Integer rcestId) throws CalificacionException  {
		Calificacion retorno=null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf ");
			sbsql.append(" where clf.recordEstudiante.rcesId =:rcestId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("rcestId", rcestId);
			  retorno = (Calificacion) q.getSingleResult();
		} catch (NoResultException e) {
		
		}catch (NonUniqueResultException e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.non.unique.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<CalificacionModulo> buscarClMdPorRces(Integer rcesId) throws CalificacionException  {
		 List<CalificacionModulo> retorno=null;
		try {
			retorno = new ArrayList<CalificacionModulo>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clmd from CalificacionModulo clmd ");
			sbsql.append(" where clmd.recordEstudiante.rcesId =:rcesId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("rcesId", rcesId);
			retorno =  q.getResultList();
		} catch (NoResultException e) {
		}catch (NonUniqueResultException e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.non.unique.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		
		return retorno;
		
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarCorreccion(Calificacion clfAux) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			Calificacion calificacion = null;
			try {
				calificacion = buscarPorRcesId(clfAux.getRecordEstudiante().getRcesId());
				calificacion.setClfAsistencia1(clfAux.getClfAsistencia1());
				calificacion.setClfAsistencia2(clfAux.getClfAsistencia2());
				calificacion.setClfNota1(clfAux.getClfNota1());
				calificacion.setClfNota2(clfAux.getClfNota2());
				calificacion.setClfAsistenciaDocente1(clfAux.getClfAsistenciaDocente1());
				calificacion.setClfAsistenciaDocente2(clfAux.getClfAsistenciaDocente2());
				calificacion.setClfSumaP1P2(clfAux.getClfNotaFinalSemestre());
				if(clfAux.getClfNotaFinalSemestre()>=27.5){
					calificacion.setClfNotaFinalSemestre(new BigDecimal(clfAux.getClfNotaFinalSemestre()).setScale(0, RoundingMode.HALF_UP).floatValue());
				}else{
					calificacion.setClfNotaFinalSemestre(clfAux.getClfNotaFinalSemestre());	
				}
				calificacion.setClfPromedioAsistencia(clfAux.getClfAsistenciaTotal()/clfAux.getClfAsistenciaTotalDoc()*100);
				em.merge(calificacion);
			} catch (CalificacionNoEncontradoException e) {
				calificacion = new Calificacion();
				calificacion = new Calificacion();
				calificacion.setClfAsistencia1(clfAux.getClfAsistencia1());
				calificacion.setClfAsistencia2(clfAux.getClfAsistencia2());
				calificacion.setClfNota1(clfAux.getClfNota1());
				calificacion.setClfNota2(clfAux.getClfNota2());
				calificacion.setClfAsistenciaDocente1(clfAux.getClfAsistenciaDocente1());
				calificacion.setClfAsistenciaDocente2(clfAux.getClfAsistenciaDocente2());
				calificacion.setClfSumaP1P2(clfAux.getClfNotaFinalSemestre());
				if(clfAux.getClfNotaFinalSemestre()>27.5){
					calificacion.setClfNotaFinalSemestre(new BigDecimal(clfAux.getClfNotaFinalSemestre()).setScale(0, RoundingMode.HALF_UP).floatValue());	
				}else{
					calificacion.setClfNotaFinalSemestre(clfAux.getClfNotaFinalSemestre());	
				}
				calificacion.setClfPromedioAsistencia(clfAux.getClfAsistenciaTotal()/clfAux.getClfAsistenciaTotalDoc()*100);
				calificacion.setRecordEstudiante(clfAux.getRecordEstudiante());
				em.persist(calificacion);
			}
			
			RecordEstudiante rcesAux = em.find(RecordEstudiante.class, clfAux.getRecordEstudiante().getRcesId());
			if(calificacion.getClfNotaFinalSemestre()>27.5){
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			}else{
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			}
			if(calificacion.getClfPromedioAsistencia()>=80){
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			}else{
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			}
			em.merge(rcesAux);
			session.getUserTransaction().commit();
		} catch (NoResultException e) {
			throw new CalificacionNoEncontradoException();
		}catch (NonUniqueResultException e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.non.unique.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		
		
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarActualizarModulares(MateriaDto clfAux) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			CalificacionModulo clmdAux = null;
			try {
				clmdAux = em.find(CalificacionModulo.class, clfAux.getClfId());
				clmdAux.setClmdNotaFinalSemestre(clfAux.getClfNotaFinalSemestre());
				try {
					clmdAux.setClmdAsistencia1(clfAux.getClfAsistencia1().floatValue());
					clmdAux.setClmdAsistencia2(clfAux.getClfAsistencia2().floatValue());
					clmdAux.setClmdAsistenciaTotal(clfAux.getClfAsistenciaTotal().floatValue());
					clmdAux.setClmdPromedioAsistencia(clfAux.getClfPromedioAsistencia().floatValue());
					clmdAux.setClmdAsistenciaTotal(clfAux.getClfAsistenciaTotal().floatValue());
				} catch (Exception e) {
				}
				try {
					clmdAux.setClmdParamRecuperacion1(clfAux.getClfParamRecuperacion1().floatValue());
					clmdAux.setClmdParamRecuperacion2(clfAux.getClfParamRecuperacion2().floatValue());
					clmdAux.setClmdSupletorio(clfAux.getClfSupletorio());
				} catch (Exception e) {
					// TODO: handle exception
				}
				em.merge(clmdAux);
			} catch (Exception e) {
			}
			
			session.getUserTransaction().commit();
		} catch (NoResultException e) {
			throw new CalificacionNoEncontradoException();
		}catch (NonUniqueResultException e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.non.unique.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		
		
		
	}
	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarActualizarModulares(CalificacionModulo clfAux) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			CalificacionModulo clmdAux = null;
			try {
				clmdAux = em.find(CalificacionModulo.class, clfAux.getClmdId());
				clmdAux.setClmdNotaFinalSemestre(clfAux.getClmdNotaFinalSemestre());
				clmdAux.setClmdSumaP1P2(clfAux.getClmdNotaFinalSemestre());
				em.merge(clmdAux);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			session.getUserTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		
		
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarActualizarModularesConValorNull(MateriaDto clfAux) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			CalificacionModulo clmdAux = null;
			try {
				clmdAux = em.find(CalificacionModulo.class, clfAux.getClfId());
				if(clmdAux.getClmdNota1()!=null){
					clmdAux.setClmdNota2((float)0);
					clmdAux.setClmdAsistencia2((float)0);
				}else{
					clmdAux.setClmdNota1((float)0);
					clmdAux.setClmdAsistencia1((float)0);
				}
				BigDecimal sumaParciales = BigDecimal.ZERO;
				sumaParciales = (new BigDecimal(clmdAux.getClmdNota1()).setScale(2, RoundingMode.DOWN).add((new BigDecimal(clmdAux.getClmdNota2()).setScale(2, RoundingMode.DOWN))));
				clmdAux.setClmdSumaP1P2(sumaParciales.floatValue());
				clmdAux.setClmdPromedioNotas(sumaParciales.floatValue());
				//calculo de la suma de asistencia del estudiante de los dos parciales
				BigDecimal sumaAsistencia = BigDecimal.ZERO;
				sumaAsistencia = (new BigDecimal(clmdAux.getClmdAsistencia1()).setScale(2, RoundingMode.DOWN).add((new BigDecimal(clmdAux.getClmdAsistencia2()).setScale(2, RoundingMode.DOWN))));
				clmdAux.setClmdAsistenciaTotal(sumaAsistencia.floatValue());
				
				
				clmdAux.setClmdPromedioAsistencia(calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, clmdAux.getClmdAsistenciaTotal().intValue(), clmdAux.getClmdAsistenciaTotalDoc().intValue()).floatValue());
				
				int com = (new BigDecimal(clmdAux.getClmdSumaP1P2()).compareTo(new BigDecimal(27.5)));
				//si la suma de los parciales es mayor o igual a 27.5
				if(com == 1 || com == 0){
					int promedioAsistencia = 0;
					promedioAsistencia = (new BigDecimal(clmdAux.getClmdPromedioAsistencia()).compareTo(new BigDecimal(80)));
						// si el promedio de asistencia es mayor o igual a 80
					if(promedioAsistencia == 1 || promedioAsistencia == 0){
						//calcula la nota final del semestre y el estado a aprobado
						clmdAux.setClmdPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP).floatValue());
						clmdAux.setClmdNotaFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
					}else{// si el promedio de asistencia es menor a 80
						clmdAux.setClmdPromedioNotas(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
						clmdAux.setClmdNotaFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
					}
				}
				
				em.merge(clmdAux);
			} catch (Exception e) {
			}
			
			session.getUserTransaction().commit();
		} catch (NoResultException e) {
			throw new CalificacionNoEncontradoException();
		}catch (NonUniqueResultException e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.non.unique.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		
		
		
	}
	
	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		 BigDecimal itemCost  = BigDecimal.ZERO;
	     itemCost  = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje)).divide(new BigDecimal(asitenciaDoc), 0, RoundingMode.HALF_UP);
	     return itemCost;
	}
	
	
	@Override
	public Calificacion buscarPorRcesId(Integer rcesId) throws CalificacionException, CalificacionNoEncontradoException  {
		 Calificacion retorno=null;
		try {
			retorno = new Calificacion();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf ");
			sbsql.append(" where clf.recordEstudiante.rcesId =:rcesId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("rcesId", rcesId);
			  retorno =  (Calificacion) q.getSingleResult();
		} catch (NoResultException e) {
			throw new CalificacionNoEncontradoException();
		}catch (NonUniqueResultException e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.non.unique.result.exception")));
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
		
		return retorno;
		
		
	}
	
	
	@Override
	public void eliminarCalificacionModulo(Integer clmdId) throws CalificacionException  {
		try {
//			session.getUserTransaction().begin();
			PreparedStatement pstmt = null;
			Connection con = null;
			con=ds.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("insert into carrera values((select max(crr_id)+1 from carrera),12, 'MAESTRIA EN MEJORAMIENTO DE PROCESOS, MENCION SISTEMAS DE GESTION',null,'MAESTRIA EN MEJORAMIENTO DE PROCESOS, MENCION SISTEMAS DE GESTION',TO_DATE('13/04/2019','DD/MM/YYYY'),null,1,50,8442,2587,1,null,1,5434)");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.executeUpdate();
//			StringBuffer sbsql = new StringBuffer();
//			sbsql.append(" Delete from ProcesoCalificacion prcl ");
//			sbsql.append(" where prcl.prclCalificacionModulo.clmdId =:clmdId ");
//			Query q = em.createQuery(sbsql.toString());
//			  q.setParameter("clmdId", clmdId).executeUpdate();
//			  sbsql = new StringBuffer();
//			sbsql.append(" Delete from CalificacionModulo clmd ");
//			sbsql.append(" where clmd.clmdId =:clmdId ");
//			q = em.createQuery(sbsql.toString());
//			  q.setParameter("clmdId", clmdId).executeUpdate();
//			  session.getUserTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void eliminarCalificacion(Integer clfId) throws CalificacionException  {
		try {
			session.getUserTransaction().begin();
			Calificacion clf = new Calificacion();
			clf= em.find(Calificacion.class, clfId);
			
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prcl from ProcesoCalificacion prcl ");
			sbsql.append(" where prcl.prclCalificacion.clfId =:clfId ");
			Query q = em.createQuery(sbsql.toString());
			List<ProcesoCalificacion> prcl = (List<ProcesoCalificacion>) q.setParameter("clfId", clfId).getResultList();
			for (ProcesoCalificacion procesoCalificacion : prcl) {
				em.remove(procesoCalificacion);
			}
			 em.remove(clf);
			  session.getUserTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
	}
	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void eliminarCalificacionXId() throws CalificacionException  {
		try {
			

			session.getUserTransaction().begin();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" update Calificacion set clfNota1=11.25,clfAsistencia1=7 , clfAsistenciaDocente1=7 where clfId=558501 ");
			Query q = em.createQuery(sbsql.toString());
			q.executeUpdate();
			  sbsql = new StringBuffer();
			sbsql.append(" delete from ProcesoCalificacion where clfId in(608931,558202) ");
			q = em.createQuery(sbsql.toString());
			q.executeUpdate();
			 sbsql = new StringBuffer();
				sbsql.append(" delete from Calificacion where clfId in(608931,558202) ");
				q = em.createQuery(sbsql.toString());
				q.executeUpdate();
			  session.getUserTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void correcionNivelacionError() throws CalificacionException  {
		try {
			session.getUserTransaction().begin();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" delete from CarreraArea where crar_id=45 ");
			Query q = em.createQuery(sbsql.toString());
			  q.executeUpdate();
			  session.getUserTransaction().commit();
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
	}
	
	
	/** @author Daniel
	 *  @param MateriaDto item 
	 *  @exception GeneralException
	 *  @return void
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarRecalculoModular(MateriaDto item) throws CalificacionException  {
		try {
			session.getUserTransaction().begin();
			RecordEstudiante rcesAux = new RecordEstudiante();
			rcesAux = em.find(RecordEstudiante.class, item.getRcesId());
			Calificacion clf = buscarPorRcesId(rcesAux.getRcesId());
			clf.setClfAsistencia1(item.getClfAsistencia1().floatValue());
			clf.setClfAsistencia2(item.getClfAsistencia2().floatValue());
			clf.setClfAsistenciaDocente1(item.getClfAsistenciaDocente1().floatValue());
			clf.setClfAsistenciaDocente2(item.getClfAsistenciaDocente2().floatValue());
			clf.setClfPromedioAsistencia(item.getClfPromedioAsistencia().floatValue());
			clf.setClfNota1(item.getClfNota1().floatValue());
			clf.setClfNota2(item.getClfNota2().floatValue());
			clf.setClfNotaFinalSemestre(item.getClfNotaFinalSemestre().floatValue());
			clf.setClfSumaP1P2(item.getClfSumaP1P2().floatValue());
			em.merge(clf);
			rcesAux.setRcesEstado(item.getRcesEstado());
			em.merge(rcesAux);
			session.getUserTransaction().commit();
		} catch (Exception e) {
			throw new CalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Calificacion.buscar.por.id.record.estudiante.matricula.exception")));
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> buscarCalificacionCorrecionPorPrac()  {
		List<Calificacion> retorno = null;
		try {
			retorno = new ArrayList<Calificacion>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr, Paralelo prl , PeriodoAcademico prac");
			sbsql.append(" where clf.recordEstudiante.rcesId = rces.rcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId ");
			sbsql.append(" and prac.pracId = 150");
			sbsql.append(" and rces.rcesEstado in (");sbsql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
			sbsql.append(" )");
			sbsql.append(" and (clf.clfEstado <>-1)");
			sbsql.append(" and clf.clfNota1 is not null  and clf.clfNota2 is not null");
			sbsql.append(" and clf.clfAsistenciaDocente1 is not null  and clf.clfAsistenciaDocente2 is not null");
//			sbsql.append(" and rces.rcesId=602446");
			Query q = em.createQuery(sbsql.toString()).setMaxResults(500);
			retorno =  q.getResultList();
		} catch (Exception e) {
			retorno = null;
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> buscarCalificacionCorrecionNull()  {
		List<Calificacion> retorno = null;
		try {
			retorno = new ArrayList<Calificacion>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr, Paralelo prl , PeriodoAcademico prac, MallaCurricularMateria mlcrmt, Materia mtr");
			sbsql.append(" where clf.recordEstudiante.rcesId = rces.rcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId ");
			sbsql.append(" and mlcrmt.mlcrmtId = mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId ");
			sbsql.append(" and mtr.mtrId = mlcrmt.mlcrmtMateria.mtrId ");
			sbsql.append(" and mtr.mtrMateria.mtrId is null ");
//			sbsql.append(" and prac.pracId = 150");
			sbsql.append(" and mlcrpr.mlcrprId = 31521");
//			sbsql.append(" and prac.pracEstado =  ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbsql.append(" and rces.rcesEstado in (");
//			sbsql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
//			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
//			sbsql.append(" ,");
			sbsql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
			sbsql.append(" )");
//			sbsql.append(" and (clf.clfEstado is null)");
			sbsql.append(" and clf.clfNota1 is not null  and clf.clfNota2 is not null");
			sbsql.append(" and clf.clfAsistenciaDocente1 is not null  and clf.clfAsistenciaDocente2 is not null");
//			sbsql.append(" and clf.clfId=257611");
			Query q = em.createQuery(sbsql.toString()).setMaxResults(500);
			retorno =  q.getResultList();
		} catch (Exception e) {
			retorno = null;
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> buscarCalificacionCorrecion()  {
		List<Calificacion> retorno = null;
		try {
			retorno = new ArrayList<Calificacion>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr, Paralelo prl , PeriodoAcademico prac, MallaCurricularMateria mlcrmt, Materia mtr");
			sbsql.append(" where clf.recordEstudiante.rcesId = rces.rcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId ");
			sbsql.append(" and mlcrmt.mlcrmtId = mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId ");
			sbsql.append(" and mtr.mtrId = mlcrmt.mlcrmtMateria.mtrId ");
			sbsql.append(" and mtr.mtrMateria.mtrId is null ");
			sbsql.append(" and prac.pracId = 150");
//			sbsql.append(" and prac.pracEstado =  ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			sbsql.append(" and rces.rcesEstado in (");//sbsql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			//sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
			//sbsql.append(" ,");
			sbsql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			//sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
			sbsql.append(" )");
			sbsql.append(" and (clf.clfEstado <>-1)");
			sbsql.append(" and clf.clfNota1 is not null  and clf.clfNota2 is not null");
			sbsql.append(" and clf.clfAsistenciaDocente1 is not null  and clf.clfAsistenciaDocente2 is not null");
//			sbsql.append(" and clf.clfId=257611");
//			System.out.println(sbsql);
			Query q = em.createQuery(sbsql.toString()).setMaxResults(500);
			retorno =  q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			retorno = null;
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> buscarCalificacionCorrecionIdiomas()  {
		List<Calificacion> retorno = null;
		try {
			retorno = new ArrayList<Calificacion>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr, Paralelo prl ");
			sbsql.append(" where clf.recordEstudiante.rcesId = rces.rcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId in(490)");
//			sbsql.append(" and prac.pracEstado =  ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			sbsql.append(" and rces.rcesEstado in (");//sbsql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
//			//sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
//			//sbsql.append(" ,");
//			sbsql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//			//sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
//			sbsql.append(" )");
			sbsql.append(" and clf_supletorio is not null");
			sbsql.append(" and clf.clfNota1 is not null ");
			sbsql.append(" and clf.clfAsistenciaDocente1 is not null  ");
//			sbsql.append(" and clf.clfId=257611");
//			System.out.println(sbsql);
			Query q = em.createQuery(sbsql.toString()).setMaxResults(500);
			retorno =  q.getResultList();
		} catch (Exception e) {
			retorno = null;
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> buscarCalificacionCorrecionCulturaFisica()  {
		List<Calificacion> retorno = null;
		try {
			retorno = new ArrayList<Calificacion>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr, Paralelo prl ");
			sbsql.append(" where clf.recordEstudiante.rcesId = rces.rcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId in(210)");
//			sbsql.append(" and prac.pracEstado =  ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			sbsql.append(" and rces.rcesEstado in (");//sbsql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
//			//sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
//			//sbsql.append(" ,");
//			sbsql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//			//sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
//			sbsql.append(" )");
//			sbsql.append(" and clf_supletorio is not null");
			sbsql.append(" and clf.clfNota1 is not null ");
			sbsql.append(" and clf.clfAsistenciaDocente1 is not null  ");
			sbsql.append(" and clf.clfNota2 is not null ");
			sbsql.append(" and clf.clfAsistenciaDocente2 is not null  ");
//			sbsql.append(" and clf.clfId=257611");
//			System.out.println(sbsql);
			Query q = em.createQuery(sbsql.toString()).setMaxResults(500);
			retorno =  q.getResultList();
		} catch (Exception e) {
			retorno = null;
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Calificacion> buscarCalificacionCorrecionIndividual()  {
		List<Calificacion> retorno = null;
		try {
			retorno = new ArrayList<Calificacion>();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select clf from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr, Paralelo prl , PeriodoAcademico prac"
					+ ", MallaCurricularMateria mlcrmt, Materia mtr, Carrera crr");
			sbsql.append(" where clf.recordEstudiante.rcesId = rces.rcesId ");
			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
			sbsql.append(" and mlcrmt.mlcrmtMateria.mtrId = mtr.mtrId ");
			sbsql.append(" and mtr.mtrCarrera.crrId = crr.crrId ");
			sbsql.append(" and (crr.crrId =225 or crr.crrId=226) ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = 350 ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId = prac.pracId ");
//			sbsql.append(" and prac.pracEstado =  ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			sbsql.append(" and rces.rcesEstado in (");sbsql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
//			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
//			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//			sbsql.append(" ,");sbsql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
//			sbsql.append(" )");
//			sbsql.append(" and clf.clfEstado is null");
//			sbsql.append(" and clf.clfNota1 is not null  and clf.clfNota2 is not null");
//			sbsql.append(" and clf.clfParamRecuperacion1 is not null ");
//			sbsql.append(" and clf.clfId in(");
//			sbsql.append(" 888107,884619,888351)");
//					",618257,618267,618326,618357,618360,618728,618979,618981,618983,618985,618991,618993,619003,619015,619027,619029"+
//					",619037,619039,619673,619689,619691,619697,619853,619875,619881,619883,619893,619895,619897,620001,620003,620009"+
//					",620011,620013,620015,620017,620021,620023,620612,620616,620620,620622,620624,620628,620630,620632,620634,620636"+
//					",620642,620644,620652,620654,620656,620658,620660,620662,620664,620666,620670,620672,620680,620682,620684,620690"+
//					",620874,620878,620902,620904,620906,620908,620910,620928,620930,620934,620942,620954,620962,620983,620987,620991"+
//					",620993,620997,621003 ");
//			sbsql.append(" )");
//			sbsql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId =22139 ");
//			System.out.println(sbsql);
			Query q = em.createQuery(sbsql.toString());
			retorno =  q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			retorno = null;
		}
		return retorno;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarCorreccionFull(Integer clfId) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			Calificacion calificacion = null;
				calificacion = buscarPorId(clfId);
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select rces from RecordEstudiante rces ");
				sbsql.append(" where rces.rcesId =:rcesId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("rcesId", calificacion.getRecordEstudiante().getRcesId());
				RecordEstudiante rcesAux = (RecordEstudiante) q.getSingleResult();
				BigDecimal asistenciaSumaP1P2 = BigDecimal.ZERO;
				BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
				BigDecimal notEstudiante2 = new BigDecimal(calificacion.getClfNota2());
				BigDecimal notEstudiante1 = new BigDecimal(calificacion.getClfNota1());
				BigDecimal notaSuma = BigDecimal.ZERO;
				BigDecimal promedioAsistencia = BigDecimal.ZERO;
				
				notaSuma = notEstudiante1.setScale(2, RoundingMode.HALF_UP).add(notEstudiante2.setScale(2, RoundingMode.HALF_UP));
				asistenciaSumaP1P2 = new BigDecimal(calificacion.getClfAsistencia1()).add(new BigDecimal(calificacion.getClfAsistencia2()));
				asistenciaSumaDocente = new BigDecimal(calificacion.getClfAsistenciaDocente1()).add(new BigDecimal(calificacion.getClfAsistenciaDocente2()));
				promedioAsistencia=calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, asistenciaSumaP1P2.intValue(), asistenciaSumaDocente.intValue());
				
				calificacion.setClfSumaP1P2(notaSuma.floatValue());
				calificacion.setClfPromedioAsistencia(promedioAsistencia.floatValue());
				calificacion.setClfAsistenciaTotal(asistenciaSumaP1P2.floatValue());
				calificacion.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
				int com = notaSuma.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
				int comparador = promedioAsistencia.compareTo(new BigDecimal(80));
				// si el promedio de asistencia es mayor o igual a 80
				if(comparador == 1 || comparador == 0){
					if(com == 1 || com == 0){
						calificacion.setClfPromedioAsistencia(promedioAsistencia.setScale(0, RoundingMode.HALF_UP).floatValue());
						calificacion.setClfNotaFinalSemestre((notaSuma.setScale(0, RoundingMode.HALF_UP)).floatValue());
						rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						calificacion.setClfSupletorio(null);
						calificacion.setClfParamRecuperacion1(null);
						calificacion.setClfParamRecuperacion2(null);
					}else{
						int minNota = notaSuma.compareTo(new BigDecimal(8.8));
						if(minNota==-1){
							calificacion.setClfNotaFinalSemestre((notaSuma.setScale(2, RoundingMode.HALF_UP)).floatValue());
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							calificacion.setClfSupletorio(null);
							calificacion.setClfParamRecuperacion1(null);
							calificacion.setClfParamRecuperacion2(null);
						}else{
							try {
								if(calificacion.getClfParamRecuperacion1()>0){
										BigDecimal parametro1Aux  = BigDecimal.ZERO;
										parametro1Aux  = notaSuma.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.HALF_UP);
										calificacion.setClfParamRecuperacion1(parametro1Aux.floatValue());
										BigDecimal parametro2Aux  = BigDecimal.ZERO;
										parametro2Aux  = new BigDecimal(calificacion.getClfSupletorio()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2,RoundingMode.HALF_UP);
										calificacion.setClfParamRecuperacion2(parametro2Aux.floatValue());
											
										BigDecimal sumaParametros = BigDecimal.ZERO;
										sumaParametros = parametro1Aux.setScale(2, RoundingMode.DOWN).add(parametro2Aux.setScale(2, RoundingMode.HALF_UP));
										calificacion.setClfNotaFinalSemestre(sumaParametros.floatValue());
										
										int estadoRces = (new BigDecimal(calificacion.getClfNotaFinalSemestre())).compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
										if(estadoRces == 1 || estadoRces ==0){
											calificacion.setClfNotaFinalSemestre((sumaParametros.setScale(0, RoundingMode.HALF_UP)).floatValue());
											calificacion.setClfPromedioAsistencia(promedioAsistencia.setScale(0, RoundingMode.HALF_UP).floatValue());
											rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
										}else{
											rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
										}	
									
								}
							} catch (Exception e) {
									calificacion.setClfSupletorio(null);
									calificacion.setClfParamRecuperacion1(null);
									calificacion.setClfParamRecuperacion2(null);
									calificacion.setClfPromedioAsistencia(promedioAsistencia.setScale(0, RoundingMode.HALF_UP).floatValue());
									calificacion.setClfNotaFinalSemestre(notaSuma.setScale(2, RoundingMode.HALF_UP).floatValue());
									rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
							}
						}
					}
					
				}else{
					calificacion.setClfPromedioNotas((notaSuma.setScale(2, RoundingMode.HALF_UP)).floatValue());
					calificacion.setClfNotaFinalSemestre((notaSuma.setScale(0, RoundingMode.HALF_UP)).floatValue());
					rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					calificacion.setClfSupletorio(null);
					calificacion.setClfParamRecuperacion1(null);
					calificacion.setClfParamRecuperacion2(null);
				}
			
				calificacion.setClfEstado(-9);
				em.merge(calificacion);
				em.merge(rcesAux);				
			session.getUserTransaction().commit();
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (Exception e2) {
			}
			try {
				session.getUserTransaction().begin();
				Calificacion calificacion = null;
				calificacion = buscarPorId(clfId);
				calificacion.setClfEstado(-9);
				em.merge(calificacion);
				session.getUserTransaction().commit();
			} catch (Exception e1) {
			}
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarCorreccionFullIdiomas(Integer clfId) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			Calificacion calificacion = null;
			calificacion = buscarPorId(clfId);
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rces from RecordEstudiante rces ");
			sbsql.append(" where rces.rcesId =:rcesId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("rcesId", calificacion.getRecordEstudiante().getRcesId());
			RecordEstudiante rcesAux = (RecordEstudiante) q.getSingleResult();
			calificacion.setClfNotaFinalSemestre(
					(new BigDecimal(calificacion.getClfNota1()).setScale(0, RoundingMode.HALF_UP)).floatValue());

			int comparador = new BigDecimal(calificacion.getClfNota1()).compareTo(new BigDecimal(14));
			// si el promedio de asistencia es mayor o igual a 80
			if (calificacion.getClfPromedioAsistencia() < 80) {
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			} else {
				if (comparador == 1 || comparador == 0) {
					rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				} else {
					rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					if (calificacion.getClfSupletorio() != null) {
						if (calificacion.getClfSupletorio() >= 14) {
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);

						} else {
							rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
						calificacion.setClfNotaFinalSemestre(calificacion.getClfSupletorio());
					} else {
						calificacion.setClfNotaFinalSemestre(
								(new BigDecimal(calificacion.getClfNota1()).setScale(0, RoundingMode.HALF_UP))
										.floatValue());
					}
				}
			}
			calificacion.setClfEstado(-99);
			em.merge(calificacion);
			em.merge(rcesAux);
			session.getUserTransaction().commit();
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (Exception e2) {
			}
			try {
				session.getUserTransaction().begin();
				Calificacion calificacion = null;
				calificacion = buscarPorId(clfId);
				calificacion.setClfEstado(-99);
				em.merge(calificacion);
				session.getUserTransaction().commit();
			} catch (Exception e1) {
			}
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarCorreccionFullCulturaFisica(Integer clfId) throws CalificacionException, CalificacionNoEncontradoException  {
		
		try {
			session.getUserTransaction().begin();
			Calificacion calificacion = null;
			calificacion = buscarPorId(clfId);
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rces from RecordEstudiante rces ");
			sbsql.append(" where rces.rcesId =:rcesId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("rcesId", calificacion.getRecordEstudiante().getRcesId());
			RecordEstudiante rcesAux = (RecordEstudiante) q.getSingleResult();
			BigDecimal suma = (new BigDecimal(calificacion.getClfNota1()).setScale(0, RoundingMode.HALF_UP)).add(new BigDecimal(calificacion.getClfNota2()).setScale(0, RoundingMode.HALF_UP));
			calificacion.setClfNotaFinalSemestre(suma.floatValue());

			int comparador = new BigDecimal(calificacion.getClfNotaFinalSemestre()).compareTo(new BigDecimal(28));
			// si el promedio de asistencia es mayor o igual a 80
			if (calificacion.getClfPromedioAsistencia() < 80) {
				rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			} else {
				if (comparador == 1 || comparador == 0) {
					rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				} else {
					rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					
				}
			}
			calificacion.setClfEstado(-99);
			em.merge(calificacion);
			em.merge(rcesAux);
			session.getUserTransaction().commit();
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (Exception e2) {
			}
			try {
				session.getUserTransaction().begin();
				Calificacion calificacion = null;
				calificacion = buscarPorId(clfId);
				calificacion.setClfEstado(-99);
				em.merge(calificacion);
				session.getUserTransaction().commit();
			} catch (Exception e1) {
			}
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarCorreccionModularesFull(List<MateriaDto> lista) throws CalificacionException, CalificacionNoEncontradoException  {
		try {
			session.getUserTransaction().begin();
			List<Materia> listaModulos = new ArrayList<>();
			listaModulos = srvMateriaServicio.listarTodosModulos(lista.get(0).getMtrId());
			RecordEstudiante rcesAuxPadre= srvRecordEstudianteServicio.buscarPorId(lista.get(0).getRcesId());
			List<CalificacionModulo> listaClmd = new ArrayList<CalificacionModulo>();
			for (MateriaDto item : lista) {
				listaClmd.add(buscarClmdPorId(item.getClfId()));
			}
			try {
				if(listaModulos.size()==listaClmd.size()){
					System.out.println("modulos");
					BigDecimal asistenciaPromedio = BigDecimal.ZERO;
					BigDecimal asistenciaDocente = BigDecimal.ZERO;
					BigDecimal asistenciaSuma = BigDecimal.ZERO;
					BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
					BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
					BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
					BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
					BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
					BigDecimal notEstudiante2 = BigDecimal.ZERO;
					BigDecimal notEstudiante1 = BigDecimal.ZERO;
					BigDecimal notaSuma = BigDecimal.ZERO;
					BigDecimal notaSumaP1P2 = BigDecimal.ZERO;
					BigDecimal notaPromedio = BigDecimal.ZERO;
					int ubicacion = 0;
					int ubicacionRecuperacion = 0;
					int contador = 0;
					boolean opAsistencia = false;
					boolean opNota = false;
					boolean opSupletorio = false;
					boolean opModulosCompletos = true;
					Float asistenciaMenor = Float.valueOf(100);
					Float notaMenor = Float.valueOf(100);
					for (CalificacionModulo item : listaClmd) {
						if(item.getClmdNotaFinalSemestre()!=null || item.getClmdNotaFinalSemestre()!=GeneralesConstantes.APP_ID_BASE.floatValue()){
							notaSuma=notaSuma.add(new BigDecimal(item.getClmdNotaFinalSemestre()).setScale(2, RoundingMode.DOWN));
							notEstudiante1=notEstudiante1.add(new BigDecimal(item.getClmdNota1()).setScale(2, RoundingMode.DOWN));
							notEstudiante2=notEstudiante2.add(new BigDecimal(item.getClmdNota2()).setScale(2, RoundingMode.DOWN));
							
							asistenciaDocente1=asistenciaDocente1.add(new BigDecimal(item.getClmdAsistenciaDocente1()).setScale(2, RoundingMode.DOWN));
							asistenciaDocente2=asistenciaDocente2.add(new BigDecimal(item.getClmdAsistenciaDocente2()).setScale(2, RoundingMode.DOWN));
							asistenciaEstudiante1=asistenciaEstudiante1.add(new BigDecimal(item.getClmdAsistencia1()).setScale(2, RoundingMode.DOWN));
							asistenciaEstudiante2=asistenciaEstudiante2.add(new BigDecimal(item.getClmdAsistencia2()).setScale(2, RoundingMode.DOWN));
							asistenciaSumaDocente=asistenciaSumaDocente.add(new BigDecimal(item.getClmdAsistenciaTotalDoc()).setScale(2, RoundingMode.DOWN));
							asistenciaSuma=asistenciaSuma.add(asistenciaEstudiante1.add(asistenciaEstudiante2));
							notaMenor = item.getClmdNotaFinalSemestre();
							if(item.getClmdNotaFinalSemestre()<notaMenor){
								notaMenor = item.getClmdNotaFinalSemestre();
							}	
						}		
							
					}
							rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
							em.merge(rcesAuxPadre);
							Calificacion calificacion = new Calificacion();
							boolean op = false;
							try {
								try {
									calificacion = buscarPorRcesId(rcesAuxPadre.getRcesId());
									op= true;
								} catch (CalificacionNoEncontradoException e) {
									
								}
								
								
									calificacion.setClfNota1(notEstudiante1.divide(new BigDecimal(listaClmd.size()),2,RoundingMode.DOWN).floatValue());
								
									calificacion.setClfAsistencia1(asistenciaEstudiante1.setScale(2,RoundingMode.DOWN).floatValue());
								
									calificacion.setClfNota2(notEstudiante2.divide(new BigDecimal(listaClmd.size()),2,RoundingMode.DOWN).floatValue());
								
									calificacion.setClfAsistencia2(asistenciaEstudiante2.setScale(2,RoundingMode.DOWN).floatValue());
								
									calificacion.setClfAsistenciaDocente1(asistenciaDocente1.setScale(2,RoundingMode.DOWN).floatValue());
								
									calificacion.setClfAsistenciaDocente2(asistenciaDocente2.setScale(2,RoundingMode.DOWN).floatValue());
								
									calificacion.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
								
									
									asistenciaSuma=asistenciaEstudiante1.add(asistenciaEstudiante2);
									asistenciaSumaDocente=asistenciaDocente1.add(asistenciaDocente2);
									calificacion.setClfAsistenciaTotal(asistenciaSuma.floatValue());
								BigDecimal porcentaje = asistenciaSuma.multiply(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE).divide(asistenciaSumaDocente,2,RoundingMode.HALF_UP));
									calificacion.setClfPromedioAsistencia(porcentaje.floatValue());
								
								BigDecimal promedio = notaSuma.divide(new BigDecimal(listaClmd.size()),2, RoundingMode.DOWN);
									calificacion.setClfPromedioNotas(promedio.floatValue());
//									calificacion.setClfSumaP1P2(promedio.floatValue());
									int comp = promedio.compareTo(new BigDecimal(27.5));
									if(comp==0 || comp == 1){
										calificacion.setClfNotaFinalSemestre(promedio.setScale(0, RoundingMode.HALF_UP).floatValue());
										calificacion.setClfSupletorio(null);
										calificacion.setClfParamRecuperacion1(null);
										calificacion.setClfParamRecuperacion2(null);
										rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
									}else{
										calificacion.setClfNotaFinalSemestre(promedio.floatValue());	
										calificacion.setClfSupletorio(null);
										calificacion.setClfParamRecuperacion1(null);
										calificacion.setClfParamRecuperacion2(null);
										rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
									comp = porcentaje.compareTo(new BigDecimal(80));
									if(comp<0){
										rcesAuxPadre.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
									
									calificacion.setClfSumaP1P2(promedio.floatValue());
								calificacion.setRecordEstudiante(rcesAuxPadre);
								if(op){
									em.merge(calificacion);
								}else{
									em.persist(calificacion);	
								}

							}catch (Exception e) {
								e.printStackTrace();
							}
//						}	
					}
					
				session.getUserTransaction().commit();	
			} catch (Exception e) {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			}
//			listaClmd = buscarClMdPorRces(rcesAuxPadre.getRcesId());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				session.getUserTransaction().rollback();
			} catch (Exception e2) {
			}
		}
		
	}
	
	@Override
	public List<CalificacionNivelacionDto> buscarCalificacionNivelacion(String identificacion){
		List<CalificacionNivelacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			retorno = new ArrayList<CalificacionNivelacionDto>();
			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" Select clf, mtr, prl from Calificacion clf, RecordEstudiante rces, MallaCurricularParalelo mlcrpr"
//					+ ",MallaCurricularMateria mlcrmt, Materia mtr, FichaEstudiante fces, Persona prs, Paralelo prl where ");
//			sbSql.append(" clf.recordEstudiante.rcesId = rces.rcesId ");
//			sbSql.append(" and rces.rcesFichaEstudiante.fcesId = fces.fcesId ");
//			sbSql.append(" and fces.fcesPersona.prsId = prs.prsId ");
//			sbSql.append(" and rces.rcesMallaCurricularParalelo.mlcrprId = mlcrpr.mlcrprId ");
//			sbSql.append(" and mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId = mlcrmt.mlcrmtId ");
//			sbSql.append(" and mlcrmt.mlcrmtMateria.mtrId = mtr.mtrId ");
//			sbSql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
//			sbSql.append(" and prl.prlPeriodoAcademico.pracId = 350 ");
//			if(GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion) !=null && identificacion.length()!=0){
//				sbSql.append(" and prs.prsIdentificacion = :identificacion ");	
//			}
			
//			sbSql.append(" order by mtr.mtrDescripcion");
			sbSql.append("SELECT  distinct mtr.mtr_descripcion, clf.clf_nota_final_semestre, clf.clf_promedio_asistencia, prl_descripcion"); 
							sbSql.append(" FROM materia mtr");  
							sbSql.append(" LEFT JOIN malla_curricular_materia mlcrmt  ON  mtr.mtr_id =  mlcrmt.mtr_id LEFT JOIN malla_curricular_paralelo mlcrpr  ON  mlcrmt.mlcrmt_id =  mlcrpr.mlcrmt_id LEFT JOIN paralelo prl  ON  prl.prl_id =  mlcrpr.prl_id LEFT JOIN record_estudiante rces  ON  rces.mlcrpr_id =  mlcrpr.mlcrpr_id"); 
							sbSql.append(" LEFT JOIN calificacion clf  ON  clf.rces_id =  rces.rces_id LEFT JOIN ficha_estudiante fces  ON  fces.fces_id =  rces.fces_id LEFT JOIN ficha_inscripcion fcin  ON  fces.fcin_id =  fcin.fcin_id ");
							sbSql.append(" LEFT JOIN persona prs  ON  prs.prs_id =  fces.prs_id LEFT JOIN malla_curricular mlcr  ON  mlcr.mlcr_id =  mlcrmt.mlcr_id LEFT JOIN carrera crr  ON  crr.crr_id =  mlcr.crr_id LEFT JOIN periodo_academico prac  ON  prac.prac_id =  prl.prac_id"); 
							sbSql.append(" LEFT JOIN ficha_matricula fcmt  ON  fcmt.fces_id =  fces.fces_id LEFT JOIN nivel nvl  ON  nvl.nvl_id =  mlcrmt.nvl_id LEFT JOIN comprobante_pago cmpa  ON  cmpa.fcmt_id =  fcmt.fcmt_id ");
							sbSql.append(" WHERE  prac.prac_id=350");
							sbSql.append(" AND  prs.prs_identificacion = ?");
							sbSql.append(" order by mtr.mtr_descripcion");
							
							con = ds.getConnection();
							pstmt = con.prepareStatement(sbSql.toString());
							pstmt.setString(1, identificacion);
							rs = pstmt.executeQuery();
			
							while(rs.next()){
								 Calificacion c = new Calificacion();
								 Materia m = new Materia();
								 Paralelo p = new Paralelo();
								 c.setClfNotaFinalSemestre(rs.getFloat(JdbcConstantes.CLF_NOTA_FINAL_SEMESTRE));
								 c.setClfPromedioAsistencia(rs.getFloat(JdbcConstantes.CLF_PROMEDIO_ASISTENCIA));
								 m.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
								 p.setPrlDescripcion(rs.getString(JdbcConstantes.PRL_DESCRIPCION));
								 CalificacionNivelacionDto auxiliar = new CalificacionNivelacionDto();
								 auxiliar.setClfAux(c);
								 auxiliar.setMtrAux(m);
								 auxiliar.setPrl(p);
								 retorno.add(auxiliar);
							}
		}catch (Exception e) {
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
	
	
	public boolean registrarCalificacion(PersonaDto estudiante, String cliente){
		boolean retorno = false;

		try {
			session.getUserTransaction().begin();
			RecordEstudiante record = em.find(RecordEstudiante.class, estudiante.getPrsRecordEstudianteDto().getRcesId());
			record.setRcesEstado(estudiante.getPrsRecordEstudianteDto().getRcesEstado());
			em.merge(record);
			
			Calificacion calificacion = null;
			ProcesoCalificacion proceso = null;
			try {
				
				Query q = em.createNamedQuery("Calificacion.findPorRecordEstudiante");
				q.setParameter("recordId", record.getRcesId());
				calificacion = (Calificacion) q.getSingleResult();

				calificacion.setClfNota1(estudiante.getPrsCalificacionDto().getClfNota1().floatValue());
				calificacion.setClfSumaP1P2(estudiante.getPrsCalificacionDto().getClfNota1().floatValue());
				calificacion.setClfNotaFinalSemestre(estudiante.getPrsCalificacionDto().getClfNotaFinalSemestre().floatValue());
				
				calificacion.setClfAsistencia1(estudiante.getPrsCalificacionDto().getClfAsistencia1().floatValue());
				calificacion.setClfAsistenciaTotal(estudiante.getPrsCalificacionDto().getClfAsistencia1().floatValue());
				calificacion.setClfPromedioAsistencia(estudiante.getPrsCalificacionDto().getClfPorcentajeAsistencia().floatValue());
				
				calificacion.setClfAsistenciaDocente1(estudiante.getPrsCalificacionDto().getClfAsistenciaDocente1().floatValue());
				calificacion.setClfAsistenciaTotalDoc(estudiante.getPrsCalificacionDto().getClfAsistenciaDocente1().floatValue());
				em.merge(calificacion);
				
				proceso = new ProcesoCalificacion();
				proceso.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_CORRECION_VALUE);

			} catch (Exception e) {
				
				calificacion = new Calificacion();
				calificacion.setClfNota1(estudiante.getPrsCalificacionDto().getClfNota1().floatValue());
				calificacion.setClfSumaP1P2(estudiante.getPrsCalificacionDto().getClfNota1().floatValue());
				calificacion.setClfNotaFinalSemestre(estudiante.getPrsCalificacionDto().getClfNotaFinalSemestre().floatValue());
				
				calificacion.setClfAsistencia1(estudiante.getPrsCalificacionDto().getClfAsistencia1().floatValue());
				calificacion.setClfAsistenciaTotal(estudiante.getPrsCalificacionDto().getClfAsistencia1().floatValue());
				calificacion.setClfPromedioAsistencia(estudiante.getPrsCalificacionDto().getClfPorcentajeAsistencia().floatValue());
				
				calificacion.setClfAsistenciaDocente1(estudiante.getPrsCalificacionDto().getClfAsistenciaDocente1().floatValue());
				calificacion.setClfAsistenciaTotalDoc(estudiante.getPrsCalificacionDto().getClfAsistenciaDocente1().floatValue());
				calificacion.setRecordEstudiante(record);
				em.persist(calificacion);
				
				proceso = new ProcesoCalificacion();
				proceso.setPrclTipoProceso(ProcesoCalificacionConstantes.ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE);

			}
			
			
			proceso.setPrclFecha(new Timestamp(System.currentTimeMillis()));
			proceso.setPrclObservacion(cliente);
			proceso.setPrclCalificacion(calificacion);
			
			proceso.setPrclNota1(calificacion.getClfNota1());
			proceso.setPrclSumaP1P2(calificacion.getClfSumaP1P2());
			proceso.setPrclNotaFinalSemestre(calificacion.getClfNotaFinalSemestre());
			
			proceso.setPrclAsistencia1(calificacion.getClfAsistencia1());
			proceso.setPrclAsistenciaTotal(calificacion.getClfAsistenciaTotal());
			proceso.setPrclPromedioAsistencia(calificacion.getClfPromedioAsistencia());
			
			proceso.setPrclAsistenciaDocente1(calificacion.getClfAsistenciaDocente1());
			proceso.setPrclAsistenciaTotalDoc(calificacion.getClfAsistenciaTotalDoc());

			em.persist(proceso);
			
			FichaMatricula matricula = em.find(FichaMatricula.class, estudiante.getPrsFichaMatriculaDto().getFcmtId());
			if (record.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE) && estudiante.getPrsFichaMatriculaDto().getPracId() == PeriodoAcademicoConstantes.PRAC_PERIODO_SUFICIENCIA_INFORMATICA_EXONERACIONES_VALUE) {
				matricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_INACTIVO_VALUE);
				em.merge(matricula);
				em.flush();
			}else if (record.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) && matricula.getFcmtEstado().equals(FichaMatriculaConstantes.ESTADO_INACTIVO_VALUE) && matricula.getFcmtPracId() == PeriodoAcademicoConstantes.PRAC_PERIODO_SUFICIENCIA_INFORMATICA_EXONERACIONES_VALUE) {
				matricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				em.merge(matricula);
				em.flush();
			}
			
			session.getUserTransaction().commit();
			retorno = true;
			
		} catch (IllegalStateException e) {
		} catch (NotSupportedException e) {
		} catch (SystemException e) {
		} catch (SecurityException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		}
		
		return retorno;
	}
	
} 
	
	
