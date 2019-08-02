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

 ARCHIVO:     DetalleCargaHorariaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla DetalleCargaHoraria. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 08-09-2017           Arturo Villafuerte                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleCargaHorariaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleCargaHoraria;

/**
 * Clase (Bean)DetalleCargaHorariaServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class DetalleCargaHorariaServicioImpl implements  DetalleCargaHorariaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	
	
	/**
	 * Busca una entidad DetalleCargaHoraria por su id
	 * @param id - de la DetalleCargaHoraria a buscar
	 * @return DetalleCargaHoraria con el id solicitado
	 * @throws DetalleCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleCargaHoraria con el id solicitado
	 * @throws DetalleCargaHorariaException - Excepcion general
	 */
	@Override
	public DetalleCargaHoraria buscarPorId(Integer id) throws DetalleCargaHorariaNoEncontradoException, DetalleCargaHorariaException {
		DetalleCargaHoraria retorno = null;
		if (id != null) {
			try {
				retorno = em.find(DetalleCargaHoraria.class, id);
			} catch (NoResultException e) {
				throw new DetalleCargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades DetalleCargaHoraria existentes en la BD
	 * @return lista de todas las entidades DetalleCargaHoraria existentes en la BD
	 * @throws DetalleCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleCargaHoraria 
	 * @throws DetalleCargaHorariaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalleCargaHoraria> listarTodos() throws DetalleCargaHorariaNoEncontradoException , DetalleCargaHorariaException {
		List<DetalleCargaHoraria> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtcrhr from DetalleCargaHoraria dtcrhr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new DetalleCargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Añade una carga horaria en la BD
	 * @return Si se añadio o no la carga horaria
	 * @throws DetalleCargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws DetalleCargaHorariaException - Excepción general
	 */
	@Override
	public DetalleCargaHoraria anadir(DetalleCargaHoraria entidad) throws DetalleCargaHorariaValidacionException, DetalleCargaHorariaException {
		DetalleCargaHoraria retorno = null;
		 
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.anadir.exception")));
			}
		} else {
			throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.anadir.null.exception")));
		}
		return retorno;
	}
	
	/**
	 * Edita un detalle carga horaria en la BD
	 * @return Si se Edito o no el detalle carga horaria
	 * @throws DetalleCargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws DetalleCargaHorariaException - Excepción general
	 */
	@Override
	public Boolean editar(DetalleCargaHoraria entidad) throws DetalleCargaHorariaValidacionException, DetalleCargaHorariaException {
		Boolean verificar = false;
		
		DetalleCargaHoraria retorno = null;
		try {
		if (entidad != null) {
				retorno = buscarPorId(entidad.getDtcrhrId());
				if (retorno != null) {
					
				retorno.setDtcrhrArea(entidad.getDtcrhrArea());
				retorno.setDtcrhrAsignaturaClinicas(entidad.getDtcrhrAsignaturaClinicas());
				retorno.setDtcrhrCargaHoraria(entidad.getDtcrhrCargaHoraria());
				retorno.setDtcrhrCarreraId(entidad.getDtcrhrCarreraId());
				retorno.setDtcrhrEstado(entidad.getDtcrhrEstado());
				retorno.setDtcrhrEstadoEliminacion(entidad.getDtcrhrEstadoEliminacion());
				retorno.setDtcrhrFechaInicio(entidad.getDtcrhrFechaInicio());
				retorno.setDtcrhrFechaFin(entidad.getDtcrhrFechaFin());
				retorno.setDtcrhrFuncion(entidad.getDtcrhrFuncion());
				retorno.setDtcrhrNombreProyecto(entidad.getDtcrhrNombreProyecto());
				retorno.setDtcrhrNumAlumnosClinicas(entidad.getDtcrhrNumAlumnosClinicas());
				retorno.setDtcrhrNumAlumnosTitulacion(entidad.getDtcrhrNumAlumnosTitulacion());
				retorno.setDtcrhrNumHoras(entidad.getDtcrhrNumHoras());
				retorno.setDtcrhrNumProyecto(entidad.getDtcrhrNumProyecto());
				retorno.setDtcrhrSubarea(entidad.getDtcrhrSubarea());
				retorno.setDtcrhrTituloDoctorado(entidad.getDtcrhrTituloDoctorado());
				retorno.setDtcrhrUnidadAcademica(entidad.getDtcrhrUnidadAcademica());
				retorno.setDtcrhrUniversidadDoctorado(entidad.getDtcrhrUniversidadDoctorado());
				retorno.setDtcrhrNivelProyecto1(entidad.getDtcrhrNivelProyecto1());
				retorno.setDtcrhrNivelProyecto2(entidad.getDtcrhrNivelProyecto2());
				retorno.setDtcrhrNivelProyecto3(entidad.getDtcrhrNivelProyecto3());
				retorno.setDtcrhrNivelProyecto4(entidad.getDtcrhrNivelProyecto4());
				retorno.setDtcrhrNivelProyecto5(entidad.getDtcrhrNivelProyecto5());
				retorno.setDtcrhrNivelProyecto6(entidad.getDtcrhrNivelProyecto6());
				retorno.setDtcrhrNivelProyecto7(entidad.getDtcrhrNivelProyecto7());
				retorno.setDtcrhrNivelProyecto8(entidad.getDtcrhrNivelProyecto8());
				retorno.setDtcrhrProyecto1(entidad.getDtcrhrProyecto1());
				retorno.setDtcrhrProyecto2(entidad.getDtcrhrProyecto2());
				retorno.setDtcrhrProyecto3(entidad.getDtcrhrProyecto3());
				retorno.setDtcrhrProyecto4(entidad.getDtcrhrProyecto4());
				retorno.setDtcrhrProyecto5(entidad.getDtcrhrProyecto5());
				retorno.setDtcrhrProyecto6(entidad.getDtcrhrProyecto6());
				retorno.setDtcrhrProyecto7(entidad.getDtcrhrProyecto7());
				retorno.setDtcrhrProyecto8(entidad.getDtcrhrProyecto8());
				retorno.setDtcrhrProyecto1Horas(entidad.getDtcrhrProyecto1Horas());
				retorno.setDtcrhrProyecto2Horas(entidad.getDtcrhrProyecto2Horas());
				retorno.setDtcrhrProyecto3Horas(entidad.getDtcrhrProyecto3Horas());
				retorno.setDtcrhrProyecto4Horas(entidad.getDtcrhrProyecto4Horas());
				retorno.setDtcrhrProyecto5Horas(entidad.getDtcrhrProyecto5Horas());
				retorno.setDtcrhrProyecto6Horas(entidad.getDtcrhrProyecto6Horas());
				retorno.setDtcrhrProyecto7Horas(entidad.getDtcrhrProyecto7Horas());
				retorno.setDtcrhrProyecto8Horas(entidad.getDtcrhrProyecto8Horas());
				
				verificar= true;
				}
			}
		}catch (Exception e) { 
				throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.editar.exception")));
		}
		
		return verificar;
	}
	
	/**
	 * Lista todas las entidades DetalleCargaHoraria existentes en la BD por carga horaria
	 * @param crhrId Id de carga horaria a buscar 
	 * @return lista de todas las entidades DetalleCargaHoraria existentes en la BD
	 * @throws DetalleCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleCargaHoraria 
	 * @throws DetalleCargaHorariaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalleCargaHoraria> listaPorCargaHoraria(int crhrId) throws DetalleCargaHorariaNoEncontradoException , DetalleCargaHorariaException {
		List<DetalleCargaHoraria> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtcrhr from DetalleCargaHoraria dtcrhr where dtcrhrCargaHoraria.crhrId =:crhrId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crhrId", crhrId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new DetalleCargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.por.carga.horaria.no.result.exception")));
		} catch (Exception e) {
			throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.por.carga.horaria.exception")));
		}
		return retorno;
	}
	
	public Boolean eliminar(DetalleCargaHoraria entidad) throws DetalleCargaHorariaValidacionException, DetalleCargaHorariaException {
		Boolean verificar = false;
		
		try{
			DetalleCargaHoraria detalle = em.find(DetalleCargaHoraria.class, entidad.getDtcrhrId());
			if (detalle != null) {
				em.remove(detalle);
				em.flush();
				verificar = true;
			}else{
				throw new DetalleCargaHorariaValidacionException("No se encontró el Detalle Carga Horaria."); 
			}
		}catch (Exception e) { 
		  throw new DetalleCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleCargaHoraria.editar.exception")));
		}
		
		return verificar;
	}
	
} 