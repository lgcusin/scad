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

 ARCHIVO:     PlanificacionCronogramaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla PlanificacionCronograma. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-03-2017           David Arellano                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.MallaPeriodoDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Cronograma;
import ec.edu.uce.academico.jpa.entidades.publico.CronogramaProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;

/**
 * Clase (Bean)PlanificacionCronogramaServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class PlanificacionCronogramaServicioImpl implements PlanificacionCronogramaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad PlanificacionCronograma por su id
	 * @param id - deL PlanificacionCronograma a buscar
	 * @return PlanificacionCronograma con el id solicitado
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una PlanificacionCronograma con el id solicitado
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public PlanificacionCronograma buscarPorId(Integer id) throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException {
		PlanificacionCronograma retorno = null;
		if (id != null) {
			try {
				retorno = em.find(PlanificacionCronograma.class, id);
			} catch (NoResultException e) {
				throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<PlanificacionCronograma> listarTodos() throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException {
		List<PlanificacionCronograma> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select plcr from PlanificacionCronograma plcr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por el cronogama proceso flujo
	 * @param cronogramaProcesoFlujoId - id del cronograma proceso flujo a buscar
	 * @return la entidad Planificacion cronograma según los parametros enviados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una PlanificacionCronograma
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public PlanificacionCronograma buscarXCronogramaProcesoFlujo(int cronogramaProcesoFlujoId) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException {
		PlanificacionCronograma retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select plcr from PlanificacionCronograma plcr ");
			sbsql.append(" Where plcr.plcrCronogramaProcesoFlujo.crprflId =:cronogramaProcesoFlujoId");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("cronogramaProcesoFlujoId", cronogramaProcesoFlujoId);
			retorno = (PlanificacionCronograma)q.getSingleResult();
		} catch (NoResultException e) {
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.no.result.exception")));
		} catch (Exception e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por estado del cronograma, por estado del período académico y el proceso flujo
	 * @param estadoCronograma - estado del cronograma que desea listar
	 * @param estadoPeriodoAcademico - estado del período académico que se requiere listar
	 * @param listProcesoFlujo - lista de Integer con los proceso flujo requeridos para listar
	 * @param tipoCronograma - Integer del tipo de cronograma a buscar
	 * @return Lista de entidades planificacion cronograma con los parámetros ingresados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra las entidades por los parametros enviados
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<PlanificacionCronograma> buscarXestadoCrnXestadoPracXprocesoFlujo(int estadoCronograma, int estadoPeriodoAcademico, List<Integer> listProcesoFlujo, Integer tipoCronograma) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException {
		List<PlanificacionCronograma> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT plcr , prfl from Cronograma crn, ProcesoFlujo prfl, CronogramaProcesoFlujo crprfl, PlanificacionCronograma plcr, PeriodoAcademico prac ");
			sbsql.append(" WHERE crprfl.crprProcesoFlujo.prflId  = prfl.prflId ");
			sbsql.append(" AND crprfl.crprCronograma.crnId  = crn.crnId ");
			sbsql.append(" AND plcr.plcrCronogramaProcesoFlujo.crprflId  = crprfl.crprflId ");
			sbsql.append(" AND crn.crnPeriodoAcademico.pracId  = prac.pracId ");
			sbsql.append(" AND crn.crnEstado  = :crnEstado");
			sbsql.append(" AND crn.crnTipo  = :crnTipo");
			sbsql.append(" AND prac.pracEstado  = :pracEstado ");
			sbsql.append(" AND prfl.prflId  IN ( :listProcesoFlujo ) ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crnEstado", estadoCronograma);
			q.setParameter("crnTipo", tipoCronograma);
			q.setParameter("pracEstado", estadoPeriodoAcademico);
			q.setParameter("listProcesoFlujo", listProcesoFlujo);
			retorno = q.getResultList();
			
		} catch (NoResultException e) {
			//TODO: HACER EL MENSAJE DAVID
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.no.result.exception")));
		} catch (Exception e) {
			//TODO: HACER EL MENSAJE DAVID
			e.printStackTrace();
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por estado del cronograma, por estado del período académico y el proceso flujo
	 * @param estadoCronograma - estado del cronograma que desea listar
	 * @param estadoPeriodoAcademico - estado del período académico que se requiere listar
	 * @param listProcesoFlujo - lista de Integer con los proceso flujo requeridos para listar
	 * @param tipoCronograma - Integer del tipo de cronograma a buscar
	 * @return Lista de entidades planificacion cronograma con los parámetros ingresados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra las entidades por los parametros enviados
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<PlanificacionCronograma> buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(int periodoId, List<Integer> listProcesoFlujo, Integer tipoCronograma) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException {
		List<PlanificacionCronograma> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT plcr from Cronograma crn, ProcesoFlujo prfl, CronogramaProcesoFlujo crprfl, PlanificacionCronograma plcr, PeriodoAcademico prac ");
			sbsql.append(" WHERE crprfl.crprProcesoFlujo.prflId  = prfl.prflId ");
			sbsql.append(" AND crprfl.crprCronograma.crnId  = crn.crnId ");
			sbsql.append(" AND plcr.plcrCronogramaProcesoFlujo.crprflId  = crprfl.crprflId ");
			sbsql.append(" AND crn.crnPeriodoAcademico.pracId  = prac.pracId ");
			sbsql.append(" AND prac.pracId  = :periodoId ");
			sbsql.append(" AND crn.crnTipo  = :crnTipo");
			sbsql.append(" AND prfl.prflId  IN ( :listProcesoFlujo ) ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crnTipo", tipoCronograma);
			q.setParameter("periodoId", periodoId);
			q.setParameter("listProcesoFlujo", listProcesoFlujo);
			retorno = q.getResultList();
			
		} 
		catch (NoResultException e) {
			//TODO: HACER EL MENSAJE DAVID
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.no.result.exception")));
		} catch (Exception e) {
			//TODO: HACER EL MENSAJE DAVID
			e.printStackTrace();
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.exception")));
		}
		
		if(retorno==null|| retorno.size()==0){
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por posgrado por estado del cronograma, por estado del período académico y el proceso flujo
	 * @param crrId - id del programa de posgrado
	 * @param crnEstado - estado del cronograma que desea listar
	 * @param crnTipo - Integer del tipo de cronograma a buscar
	 * @param pracEstado - estado del período académico que se requiere listar
	 * @param pracTipo - estado del período académico de posgrado por programa que se requiere listar
	 * @param listProcesoFlujo - lista de Integer con los proceso flujo requeridos para listar
	 * @return Lista de entidades planificacion cronograma con los parámetros ingresados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra las entidades por los parametros enviados
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<PlanificacionCronograma> buscarPlcrPosgradoXestadoCrnXestadoPracXprocesoFlujo(int crrId,int crnEstado,Integer crnTipo, int pracEstado,int pracTipo, List<Integer> listProcesoFlujo ) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException {
		List<PlanificacionCronograma> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT plcr from Cronograma crn, ProcesoFlujo prfl, CronogramaProcesoFlujo crprfl, PlanificacionCronograma plcr, PeriodoAcademico prac ");
			sbsql.append(" , MallaPeriodo mlpr , MallaCurricular mlcr  ");
			sbsql.append(" WHERE crprfl.crprProcesoFlujo.prflId  = prfl.prflId ");
			sbsql.append(" AND crprfl.crprCronograma.crnId  = crn.crnId ");
			sbsql.append(" AND plcr.plcrCronogramaProcesoFlujo.crprflId  = crprfl.crprflId ");
			sbsql.append(" AND crn.crnPeriodoAcademico.pracId  = prac.pracId ");
			sbsql.append(" AND prac.pracId = mlpr.mlprPeriodoAcademico.pracId ");
			sbsql.append(" AND mlpr.mlprMallaCurricular.mlcrId = mlcr.mlcrId ");
			sbsql.append(" AND mlcr.mlcrCarrera.crrId = :crrId ");
			sbsql.append(" AND crn.crnEstado  = :crnEstado");
			sbsql.append(" AND crn.crnTipo  = :crnTipo");
			sbsql.append(" AND prac.pracEstado  = :pracEstado ");
			sbsql.append(" AND prac.pracTipo  = :pracTipo ");
			sbsql.append(" AND prfl.prflId  IN ( :listProcesoFlujo ) ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("crnEstado", crnEstado);
			q.setParameter("crnTipo", crnTipo);
			q.setParameter("pracEstado", pracEstado);
			q.setParameter("pracTipo", pracTipo);
			q.setParameter("listProcesoFlujo", listProcesoFlujo);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			//TODO: HACER EL MENSAJE DAVID
			e.printStackTrace();
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.no.result.exception")));
		} catch (Exception e) {
			//TODO: HACER EL MENSAJE DAVID
			e.printStackTrace();
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.exception")));
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlanificacionCronograma> buscarPlcrPosgradoXestadoCrnXestadoPracXprocesoFlujoXPracId(int crrId,int crnEstado,Integer crnTipo, int pracEstado,int pracTipo, List<Integer> listProcesoFlujo, Integer pracId ) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException {
		List<PlanificacionCronograma> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT plcr from Cronograma crn, ProcesoFlujo prfl, CronogramaProcesoFlujo crprfl, PlanificacionCronograma plcr, PeriodoAcademico prac ");
			sbsql.append(" , MallaPeriodo mlpr , MallaCurricular mlcr  ");
			sbsql.append(" WHERE crprfl.crprProcesoFlujo.prflId  = prfl.prflId ");
			sbsql.append(" AND crprfl.crprCronograma.crnId  = crn.crnId ");
			sbsql.append(" AND plcr.plcrCronogramaProcesoFlujo.crprflId  = crprfl.crprflId ");
			sbsql.append(" AND crn.crnPeriodoAcademico.pracId  = prac.pracId ");
			sbsql.append(" AND prac.pracId = mlpr.mlprPeriodoAcademico.pracId ");
			sbsql.append(" AND prac.pracId = :pracId ");
			sbsql.append(" AND mlpr.mlprMallaCurricular.mlcrId = mlcr.mlcrId ");
			sbsql.append(" AND mlcr.mlcrCarrera.crrId = :crrId ");
			sbsql.append(" AND crn.crnEstado  = :crnEstado");
			sbsql.append(" AND crn.crnTipo  = :crnTipo");
			sbsql.append(" AND prac.pracEstado  = :pracEstado ");
			sbsql.append(" AND prac.pracTipo  = :pracTipo ");
			sbsql.append(" AND prfl.prflId  IN (  ");
			
			sbsql.append( ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);sbsql.append(", ");
			sbsql.append( ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);sbsql.append(", ");
			sbsql.append( ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);sbsql.append(") ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("crnEstado", crnEstado);
			q.setParameter("crnTipo", crnTipo);
			q.setParameter("pracEstado", pracEstado);
			q.setParameter("pracTipo", pracTipo);
			q.setParameter("pracId", pracId);
//			System.out.println(sbsql);
//			System.out.println(pracId);
//			System.out.println(crrId);
//			System.out.println(crnEstado);
//			System.out.println(pracEstado);
//			System.out.println(pracTipo);
//			System.out.println(listProcesoFlujo);
			
			retorno = q.getResultList();
		} catch (NoResultException e) {
			//TODO: HACER EL MENSAJE DAVID
			e.printStackTrace();
			throw new PlanificacionCronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.no.result.exception")));
		} catch (Exception e) {
			//TODO: HACER EL MENSAJE DAVID
			e.printStackTrace();
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.buscar.por.cronogramaProcesoFlujo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PlanificacionCronogramaException 
	 * @throws CronogramaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws CronogramaException - Excepción general
	 */
	public Boolean editarCronograma(CronogramaDto entidad) throws PeriodoAcademicoValidacionException, PeriodoAcademicoException, PlanificacionCronogramaException{
		Boolean retorno = false;
		if(entidad != null){
						
			try{
				PlanificacionCronograma plcrAux = em.find(PlanificacionCronograma.class, entidad.getPlcrId());
					
					plcrAux.setPlcrEstado(entidad.getPlcrEstado());
					
					Calendar cal = Calendar.getInstance();
					@SuppressWarnings("unused")
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				        try {
				            cal.setTime(entidad.getPlcrFechaInicio());
				        }catch (Exception e) {
						}
					cal.set(Calendar.MILLISECOND, 0);
					plcrAux.setPlcrFechaInicio(new java.sql.Timestamp(cal.getTimeInMillis()));
					cal = Calendar.getInstance();
					try {
			            cal.setTime(entidad.getPlcrFechaFin());
			        }catch (Exception e) {
					}
					cal.set(Calendar.MILLISECOND, 0);
					plcrAux.setPlcrFechaFin(new java.sql.Timestamp(cal.getTimeInMillis()));
					
					retorno = true;
				
			}catch(Exception e){
				throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.editar.exception")));
			}
		}else{
			
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.editar.null.exception")));
		}
		return retorno;
	}
	
	
	public Boolean crearNuevaPlanificacion(PlanificacionCronogramaDto grupo, List<PlanificacionCronogramaDto> cronogramas, List<MallaPeriodoDto> mallas) throws PlanificacionCronogramaException, PlanificacionCronogramaValidacionException{
		
		Boolean retorno = false;

		try{
			// DESACTIVAR PERIODO ANTERIOR
			try {
				PeriodoAcademico periodoAnterior = new PeriodoAcademico();
				Query sql = em.createNamedQuery("PeriodoAcademico.findPorTipoEstado");
				sql.setParameter("pracTipo", grupo.getPlcrPeriodoAcademicoDto().getPracTipo());
				sql.setParameter("pracEstado", PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				periodoAnterior = (PeriodoAcademico)sql.getSingleResult();
				periodoAnterior.setPracEstado(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);
				em.merge(periodoAnterior);
				em.flush();
				
				// DESACTIVAR MALLA PERIODO ANTERIOR
				if (mallas != null && mallas.size() > 0) {
					for (MallaPeriodoDto item : mallas) {
						MallaPeriodo mallaAnterior = new MallaPeriodo();
						mallaAnterior = em.find(MallaPeriodo.class, item.getMlprId());
						mallaAnterior.setMlprEstado(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_INACTIVO_VALUE);
						em.merge(mallaAnterior);
						em.flush();						
					}
				}
				
			} catch (Exception e) {
				
			}

			// CREAR NUEVO PERIODO ACADEMICO
			PeriodoAcademico periodo = new PeriodoAcademico();
			periodo.setPracDescripcion(grupo.getPlcrPeriodoAcademicoDto().getPracDescripcion());
			periodo.setPracFechaIncio(grupo.getPlcrPeriodoAcademicoDto().getPracFechaIncio());
			periodo.setPracFechaFin(grupo.getPlcrPeriodoAcademicoDto().getPracFechaFin());
			periodo.setPracTipo(grupo.getPlcrPeriodoAcademicoDto().getPracTipo());
			periodo.setPracEstado(grupo.getPlcrPeriodoAcademicoDto().getPracEstado());
			
			em.persist(periodo);
			em.flush();
			
			// CREAR NUEVO CRONOGRAMA
			Cronograma cronograma = new Cronograma();
			cronograma.setCrnDescripcion(grupo.getPlcrCronogramaDto().getCrnDescripcion());
			cronograma.setCrnEstado(grupo.getPlcrCronogramaDto().getCrnEstado());
			cronograma.setCrnTipo(grupo.getPlcrCronogramaDto().getCrnTipo());
			cronograma.setCrnPeriodoAcademico(periodo);
			
			em.persist(cronograma);
			em.flush();
			
			int numeral = 1;
			// CREAR NUEVA PLANIFICACION CRONOGRAMA
			for (PlanificacionCronogramaDto item : cronogramas) {
				//CREAR EL CRONOGRAMA PROCESO FLUJO
				CronogramaProcesoFlujo asignacionCronogramaProceso = new CronogramaProcesoFlujo();
				ProcesoFlujo proceso = em.find(ProcesoFlujo.class, item.getPlcrProcesoFlujoDto().getPrflId());
				asignacionCronogramaProceso.setCrprflOrdinal(numeral++);
				asignacionCronogramaProceso.setCrprCronograma(cronograma);
				asignacionCronogramaProceso.setCrprProcesoFlujo(proceso);
				em.persist(asignacionCronogramaProceso);
				em.flush();
				
				// CREAR LA PLANIFICACION CRONOGRAMA
				PlanificacionCronograma asignacionPlanificacion = new PlanificacionCronograma();
				asignacionPlanificacion.setPlcrCronogramaProcesoFlujo(asignacionCronogramaProceso);
				asignacionPlanificacion.setPlcrEstado(item.getPlcrEstado());
				asignacionPlanificacion.setPlcrFechaInicio(new Timestamp(item.getPlcrFechaInicial().getTime()));
				asignacionPlanificacion.setPlcrFechaFin(new Timestamp(item.getPlcrFechaFinal().getTime()));
				em.persist(asignacionPlanificacion);
				em.flush();
			}


			// CREAR NUEVA MALLA PERIODO
			if (mallas != null && mallas.size() >0) {
				
				for (MallaPeriodoDto item : mallas) {
					MallaPeriodo mallaPeriodo = new MallaPeriodo();
					MallaCurricular mallaCurricular = new MallaCurricular();

					Query sql = em.createNamedQuery("MallaCurricular.findPorCarreraEstado");
					sql.setParameter("crrId", item.getMlprMallaCurricularDto().getMlcrCrrId());
					sql.setParameter("mlcrEstado", MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE);
					mallaCurricular = (MallaCurricular)sql.getSingleResult();

					mallaPeriodo.setMlprPeriodoAcademico(periodo);
					mallaPeriodo.setMlprMallaCurricular(mallaCurricular);
					mallaPeriodo.setMlprEstado(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
					em.persist(mallaPeriodo);
					em.flush();
				}
			}
			
			retorno = true;

		}catch(Exception e){
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.añadir.exception")));
		}
		
		return retorno;
	}
	
	public Boolean editarPlanificacion(PlanificacionCronogramaDto proceso) throws PlanificacionCronogramaException, PlanificacionCronogramaValidacionException{
		Boolean retorno = false;
		try {
			PeriodoAcademico periodo = em.find(PeriodoAcademico.class, proceso.getPlcrPeriodoAcademicoDto().getPracId());
			periodo.setPracFechaIncio(proceso.getPlcrPeriodoAcademicoDto().getPracFechaIncio());
			periodo.setPracFechaFin(proceso.getPlcrPeriodoAcademicoDto().getPracFechaFin());
			em.merge(periodo);
			em.flush();
			PlanificacionCronograma planificacion = em.find(PlanificacionCronograma.class, proceso.getPlcrId());
			planificacion.setPlcrFechaInicio(new Timestamp(proceso.getPlcrFechaInicial().getTime()));
			planificacion.setPlcrFechaFin(new Timestamp(proceso.getPlcrFechaFinal().getTime()));
			em.merge(planificacion);
			em.flush();
			retorno = true;
		} catch (Exception e) {
			throw new PlanificacionCronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PlanificacionCronograma.editar.exception")));
		}
		return retorno;
	}

}
