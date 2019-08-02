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

 ARCHIVO:     DetallePuestoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla DetallePuesto. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 26-SEPT-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;
import ec.edu.uce.academico.jpa.entidades.publico.RelacionLaboral;

/**
 * Clase (Bean)DetallePuestoServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class DetallePuestoServicioImpl implements DetallePuestoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@EJB
	private PeriodoAcademicoServicio servPeriodoAcademicoServicio;

	/**
	 * Busca una entidad DetallePuesto por su id
	 * @param id - de la DetallePuesto a buscar
	 * @return FichaEstudiante con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorId(Integer id) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = null;
		if (id != null) {
			try {
				retorno = em.find(DetallePuesto.class, id);
			} catch (NoResultException e) {
				throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return true si se guardo la entidad editada, false si no se guardo
	 * @throws DetallePuestoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws DetallePuestoException - Excepción general
	 * @author Arturo Villafuerte - ajvillafuerte
	*/
	@Override
	public Boolean editar(DetallePuesto entidad) throws DetallePuestoValidacionException , DetallePuestoException {
		Boolean retorno = false;
		if (entidad != null) {
			try {
					DetallePuesto cargaHorariaAux = em.find(DetallePuesto.class, entidad.getDtpsId());
					Puesto puesto = entidad.getDtpsPuesto();
					cargaHorariaAux.setDtpsPuesto(puesto);
					RelacionLaboral relacionLaboral = entidad.getDtpsRelacionLaboral();
					cargaHorariaAux.setDtpsRelacionLaboral(relacionLaboral);
					Carrera carrera = entidad.getDtpsCarrera();
					cargaHorariaAux.setDtpsCarrera(carrera);
					
					retorno = true;
			} catch (Exception e) {
				throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.editar.exception")));
			}
		} else {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.editar.null.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Busca una entidad DetallePuesto por su Prsid
	 * @param id - de la DetallePuesto a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcId(Integer fcdcId, Integer tipoCarrera) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select distinct dtps from DetallePuesto dtps, FichaDocente fcdc, Puesto pst, Carrera crr ");
			sbsql.append(" Where fcdc.fcdcId = dtps.dtpsFichaDocente.fcdcId ");
			sbsql.append(" and pst.pstId = dtps.dtpsPuesto.pstId ");
			sbsql.append(" and crr.crrId = dtps.dtpsCarrera.crrId ");
			sbsql.append(" and fcdc.fcdcId = :fcdcId ");
			sbsql.append(" and crr.crrTipo = :tipoCarrera ");
			sbsql.append(" and pst.pstId not in (10,11) ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
			retorno = null;
//			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad DetallePuesto por su Prsid
	 * @param id - de la DetallePuesto a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcIdVerifica(Integer fcdcId, Integer tipoCarrera) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select distinct dtps from DetallePuesto dtps, FichaDocente fcdc, Puesto pst, Carrera crr ");
			sbsql.append(" Where fcdc.fcdcId = dtps.dtpsFichaDocente.fcdcId ");
			sbsql.append(" and pst.pstId = dtps.dtpsPuesto.pstId ");
			sbsql.append(" and crr.crrId = dtps.dtpsCarrera.crrId ");
			sbsql.append(" and fcdc.fcdcId = :fcdcId ");
			sbsql.append(" and crr.crrTipo = :tipoCarrera ");
			sbsql.append(" and pst.pstId not in (10,11) ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
//			retorno = null;
			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente
	 * @param id - de la ficha docente a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcIdPostgradoUotro(Integer fcdcId, Integer crrId) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtps from DetallePuesto dtps ");
			sbsql.append(" Where dtps.dtpsFichaDocente.fcdcId = :fcdcId ");
			sbsql.append(" and dtps.dtpsPuesto.pstId = 10 ");
			sbsql.append(" and dtps.dtpsCarrera.crrId = :crrId ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("crrId", crrId);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente
	 * @param id - de la ficha docente a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorario(Integer fcdcId, Integer tipoCarrera) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtps from DetallePuesto dtps ");
			sbsql.append(" Where dtps.dtpsFichaDocente.fcdcId = :fcdcId ");
			sbsql.append(" and dtps.dtpsPuesto.pstId = 10 ");
			sbsql.append(" and dtps.dtpsCarrera.crrTipo = :tipoCarrera ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioSuficiencias(Integer fcdcId, Integer tipoCarrera, Integer dpnId) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtps from DetallePuesto dtps ");
			sbsql.append(" Where dtps.dtpsFichaDocente.fcdcId = :fcdcId ");
			sbsql.append(" and dtps.dtpsPuesto.pstId = 10 ");
			sbsql.append(" and dtps.dtpsCarrera.crrTipo = :tipoCarrera ");
			sbsql.append(" and dtps.dtpsCarrera.crrDependencia.dpnId = :dpnId ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			q.setParameter("dpnId", dpnId);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioSuficienciasAux(Integer fcdcId, Integer tipoCarrera, Integer dpnId) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtps from DetallePuesto dtps ");
			sbsql.append(" Where dtps.dtpsFichaDocente.fcdcId = :fcdcId ");
			sbsql.append(" and dtps.dtpsPuesto.pstId = 10 ");
			sbsql.append(" and dtps.dtpsCarrera.crrTipo = :tipoCarrera ");
			sbsql.append(" and dtps.dtpsCarrera.crrDependencia.dpnId = :dpnId ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			q.setParameter("dpnId", dpnId);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
			retorno = null;
//			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	
	public Boolean agregar(DetallePuesto entidad) throws DetallePuestoValidacionException , DetallePuestoException {
		Boolean retorno = false;
		PeriodoAcademico pracAuxActivo = new PeriodoAcademico();
		
		if (entidad != null) {
			try {
				
				DetallePuesto detallePuesto = new DetallePuesto();
				detallePuesto.setDtpsFichaDocente(entidad.getDtpsFichaDocente());
				detallePuesto.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
				detallePuesto.setDtpsCarrera(entidad.getDtpsCarrera());
				
				if(entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE || entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_SUFICIENCIA_VALUE || entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
					Puesto puestoAux = em.find(Puesto.class, 10);
					RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 2);
					detallePuesto.setDtpsRelacionLaboral(rllbAux);
					detallePuesto.setDtpsPuesto(puestoAux);
					
					detallePuesto.setDtpsFechaRegistro(new Timestamp((new Date()).getTime()));	
					detallePuesto.setDtpsUsuario(entidad.getDtpsUsuario());
					detallePuesto.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE);
					
					if(entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE){
						detallePuesto.setDtpsTipoCarrera(CarreraConstantes.TIPO_POSGRADO_VALUE);
					}else if(entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
						detallePuesto.setDtpsTipoCarrera(CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
						//periodo activo
						PeriodoAcademico periodoSuficiencia = new PeriodoAcademico();
						if(entidad.getDtpsCarrera().getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE){
							periodoSuficiencia = servPeriodoAcademicoServicio.buscarPeriodoXTipoXEstado(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
							if(periodoSuficiencia != null){
								detallePuesto.setDtpsPracId(periodoSuficiencia.getPracId());
							}
						}else if(entidad.getDtpsCarrera().getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE){
							periodoSuficiencia = servPeriodoAcademicoServicio.buscarPeriodoXTipoXEstado(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
							if(periodoSuficiencia != null){
								detallePuesto.setDtpsPracId(periodoSuficiencia.getPracId());
							}
						}else if(entidad.getDtpsCarrera().getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE){
							periodoSuficiencia = servPeriodoAcademicoServicio.buscarPeriodoXTipoXEstado(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
							if(periodoSuficiencia != null){
								detallePuesto.setDtpsPracId(periodoSuficiencia.getPracId());
							}
						}
					}else if(entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						detallePuesto.setDtpsTipoCarrera(CarreraConstantes.TIPO_NIVELEACION_VALUE);
						pracAuxActivo = servPeriodoAcademicoServicio.buscarPeriodo(CarreraConstantes.TIPO_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						detallePuesto.setDtpsPracId(pracAuxActivo.getPracId());
					}
					
					
					em.persist(detallePuesto);
					em.flush();
					retorno = true;
				}else if(entidad.getDtpsCarrera().getCrrTipo() == CarreraConstantes.TIPO_PREGRADO_VALUE){
					Puesto puestoAux = em.find(Puesto.class, 15);
					RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 1);
					detallePuesto.setDtpsRelacionLaboral(rllbAux);
					detallePuesto.setDtpsPuesto(puestoAux);
					
					//busco período activo
					pracAuxActivo = servPeriodoAcademicoServicio.buscarPeriodo(CarreraConstantes.TIPO_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					detallePuesto.setDtpsPracId(pracAuxActivo.getPracId());
					detallePuesto.setDtpsTipoCarrera(CarreraConstantes.TIPO_PREGRADO_VALUE);
					detallePuesto.setDtpsFechaRegistro(new Timestamp((new Date()).getTime()));
					detallePuesto.setDtpsUsuario(entidad.getDtpsUsuario());
					detallePuesto.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE);
					
					
					em.persist(detallePuesto);
					em.flush();
					retorno = true;
				}
				
				
//				DetallePuesto dtpsAux = new DetallePuesto();
//				dtpsAux.setDtpsFichaDocente(fcdcAux);
//				dtpsAux.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
//				dtpsAux.setDtpsCarrera(roflcrCarrera);
//				Puesto puestoAux = em.find(Puesto.class, 15);
//				RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 1);
//				dtpsAux.setDtpsRelacionLaboral(rllbAux);
//				dtpsAux.setDtpsPuesto(puestoAux);
//				em.persist(dtpsAux);
//				em.flush();
//				retorno = true;
//				
				
					retorno = true;
			} catch (Exception e) {
				throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.editar.exception")));
			}
		} else {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.editar.null.exception")));
		}
		return retorno;
	}
	
	
	public DetallePuesto buscarDetallePuesto(Integer personaId, Integer periodoId) throws DetallePuestoNoEncontradoException, DetallePuestoValidacionException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select dtps from DetallePuesto dtps, FichaDocente fcdc, Persona prs ");
		sbsql.append(" Where prs.prsId = fcdc.fcdcPersona.prsId ");
		sbsql.append(" and fcdc.fcdcId = dtps.dtpsFichaDocente.fcdcId ");
		sbsql.append(" and prs.prsId = :personaId ");
		sbsql.append(" and dtps.dtpsPracId = :periodoId ");
		
		try {
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("personaId", personaId);
			q.setParameter("periodoId", periodoId);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NonUniqueResultException e) {	
			throw new DetallePuestoValidacionException("Error tipo sql, comuníquese con el administrador del sistema.");
		} catch (NoResultException e) {
			throw new DetallePuestoNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		} catch (Exception e) {
			throw new DetallePuestoException("Error tipo sql, comuníquese con el administrador del sistema.");
		}
		return retorno;
	}
	
	public DetallePuesto guardar(DetallePuesto entidad) throws DetallePuestoValidacionException , DetallePuestoException {

		DetallePuesto retorno = null;

		try {

			DetallePuesto detallePuesto = new DetallePuesto();
			detallePuesto.setDtpsFichaDocente(entidad.getDtpsFichaDocente());
			detallePuesto.setDtpsPuesto(entidad.getDtpsPuesto());
			detallePuesto.setDtpsRelacionLaboral(entidad.getDtpsRelacionLaboral());
			detallePuesto.setDtpsCarrera(entidad.getDtpsCarrera());
			detallePuesto.setDtpsPracId(entidad.getDtpsPracId());
			detallePuesto.setDtpsEstado(entidad.getDtpsEstado());
			detallePuesto.setDtpsFechaRegistro(Timestamp.from(Instant.now()));
			detallePuesto.setDtpsUsuario(entidad.getDtpsUsuario());
			detallePuesto.setDtpsProcesoRegistro(entidad.getDtpsProcesoRegistro());
			detallePuesto.setDtpsTipoCarrera(entidad.getDtpsTipoCarrera());
			
			em.persist(detallePuesto);
			em.flush();
			
			retorno = detallePuesto;

		} catch (Exception e) {
			throw new DetallePuestoException("Error tipo sql al registrar el detalle del puesto en el período académico seleccionado.");
		}

		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public boolean desactivarDetallePuesto(int fichaDocente, int tipoCarrera) throws DetallePuestoValidacionException, DetallePuestoException {
		boolean retorno = false;
		
		try {
			List<DetallePuesto> listDetallePuesto = new ArrayList<>();
			Query sql = em.createNamedQuery("DetallePuesto.findPorFichaDocenteTipoCarrera");
			sql.setParameter("fcdcId", fichaDocente);
			sql.setParameter("tipoCarrera", tipoCarrera);
			listDetallePuesto = sql.getResultList();
			
			if (!listDetallePuesto.isEmpty()) {
				for (DetallePuesto item : listDetallePuesto) {
					item.setDtpsEstado(DetallePuestoConstantes.ESTADO_INACTIVO_VALUE);
					em.merge(item);
					em.flush();
				}
				
				retorno = true;
			}
			
		} catch (Exception e) {
			
		}
		
		return retorno;
	}

	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioPosgrado(Integer fcdcId, Integer tipoCarrera, Integer crrId) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtps from DetallePuesto dtps ");
			sbsql.append(" Where dtps.dtpsFichaDocente.fcdcId = :fcdcId ");
			sbsql.append(" and dtps.dtpsPuesto.pstId = 10 ");
			sbsql.append(" and dtps.dtpsCarrera.crrTipo = :tipoCarrera ");
			sbsql.append(" and dtps.dtpsCarrera.crrId = :crrId ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			q.setParameter("crrId", crrId);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}

	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	@Override
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioPosgradoAux(Integer fcdcId, Integer tipoCarrera, Integer crrId) throws DetallePuestoNoEncontradoException, DetallePuestoException {
		DetallePuesto retorno = new DetallePuesto();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtps from DetallePuesto dtps ");
			sbsql.append(" Where dtps.dtpsFichaDocente.fcdcId = :fcdcId ");
			sbsql.append(" and dtps.dtpsPuesto.pstId = 10 ");
			sbsql.append(" and dtps.dtpsCarrera.crrTipo = :tipoCarrera ");
			sbsql.append(" and dtps.dtpsCarrera.crrId = :crrId ");
			sbsql.append(" and dtps.dtpsEstado = ");sbsql.append(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcdcId", fcdcId);
			q.setParameter("tipoCarrera", tipoCarrera);
			q.setParameter("crrId", crrId);
			retorno = (DetallePuesto) q.getSingleResult();
		} catch (NoResultException e) {
//			throw new DetallePuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetallePuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetallePuesto.listar.todos.exception")));
		}
		return retorno;
	}

	
}
