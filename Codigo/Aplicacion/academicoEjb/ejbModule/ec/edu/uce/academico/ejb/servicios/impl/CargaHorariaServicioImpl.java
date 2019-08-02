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

 ARCHIVO:     CargaHorariaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CargaHoraria. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-09-2017           Arturo Villafuerte                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Clase (Bean)CargaHorariaServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless 
public class CargaHorariaServicioImpl implements  CargaHorariaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	 
	/**
	 * Busca una entidad CargaHoraria por su id
	 * @author Arturo VIllafuerte
	 * @param id - de la CargaHoraria a buscar
	 * @return CargaHoraria con el id solicitado
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria con el id solicitado
	 * @throws CargaHorariaException - Excepcion general
	 */
	@Override
	public CargaHoraria buscarPorId(Integer id) throws CargaHorariaNoEncontradoException, CargaHorariaException {
		CargaHoraria retorno = null;
		if (id != null) {
			try {
				retorno = em.find(CargaHoraria.class, id);
			} catch (NoResultException e) {
				throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD
	 * @author Arturo VIllafuerte
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CargaHoraria> listarTodos() throws CargaHorariaNoEncontradoException , CargaHorariaException {
		List<CargaHoraria> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crhr from CargaHoraria crhr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @author Arturo VIllafuerte
	 * @param entidad - entidad a editar
	 * @return true si se guardo la entidad editada, false si no se guardo
	 * @throws CargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws CargaHorariaException - Excepción general
	*/
	@Override
	public Boolean editar(CargaHoraria entidad) throws CargaHorariaValidacionException , CargaHorariaException {
		Boolean retorno = false;
		if (entidad != null) {
			try {
			 
				    // BUSCO LA MATERIA A MODIFICAR
					CargaHoraria cargaHorariaAux = em.find(CargaHoraria.class, entidad.getCrhrId());

					// CAMPOS A GUARDARSE
					cargaHorariaAux.setCrhrDetallePuesto(entidad.getCrhrDetallePuesto());
					cargaHorariaAux.setCrhrEstado(entidad.getCrhrEstado());
					cargaHorariaAux.setCrhrEstadoEliminacion(entidad.getCrhrEstadoEliminacion());
					cargaHorariaAux.setCrhrMallaCurricularParalelo(entidad.getCrhrMallaCurricularParalelo());
					cargaHorariaAux.setCrhrNumHoras(entidad.getCrhrNumHoras());
					cargaHorariaAux.setCrhrObservacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getCrhrObservacion()).toUpperCase());
					cargaHorariaAux.setCrhrPeriodoAcademico(entidad.getCrhrPeriodoAcademico());
					cargaHorariaAux.setCrhrTipoFuncionCargaHoraria(entidad.getCrhrTipoFuncionCargaHoraria());

					retorno = true; // retorno verdadero si se realiza la actualizacion correctamente
				 
			} catch (Exception e) {
				throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.editar.exception")));
			}
		} else {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.editar.null.exception")));
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public boolean editarPorHorarioAcademico(CargaHoraria entidad) throws CargaHorariaValidacionException , CargaHorariaNoEncontradoException, CargaHorariaException {
		boolean retorno = false;
		
		try {
			CargaHoraria cargaHorariaAux = em.find(CargaHoraria.class, entidad.getCrhrId());
			if (cargaHorariaAux != null) {
				
				Query q = em.createNamedQuery("CargaHoraria.findCargaHorariaPorMlcrprIdPrincipal");
				q.setParameter("mlcrprIdPrincipal", cargaHorariaAux.getCrhrMallaCurricularParalelo().getMlcrprId());
				q.setParameter("crhrEstado", CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				q.setParameter("crhrEstadoEliminacion", CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
				
				List<CargaHoraria> cargasHoraria = q.getResultList();	
				if (!cargasHoraria.isEmpty()) {
					throw new CargaHorariaValidacionException("No se puede eliminar el Docente ya que se registra Horario Compartido.");
				}else {
					try {
						cargaHorariaAux.setCrhrDetallePuesto(entidad.getCrhrDetallePuesto());
						cargaHorariaAux.setCrhrEstado(entidad.getCrhrEstado());
						cargaHorariaAux.setCrhrEstadoEliminacion(entidad.getCrhrEstadoEliminacion());
						cargaHorariaAux.setCrhrMallaCurricularParalelo(entidad.getCrhrMallaCurricularParalelo());
						cargaHorariaAux.setCrhrNumHoras(entidad.getCrhrNumHoras());
						cargaHorariaAux.setCrhrObservacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getCrhrObservacion()).toUpperCase());
						cargaHorariaAux.setCrhrPeriodoAcademico(entidad.getCrhrPeriodoAcademico());
						cargaHorariaAux.setCrhrTipoFuncionCargaHoraria(entidad.getCrhrTipoFuncionCargaHoraria());
						retorno = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException("No se encontró Carga Horaria para eliminar");
		}
		
		return retorno;
	}
	
	/**
	 * Añade una carga horaria en la BD
	 * @author Arturo VIllafuerte
	 * @return Si se añadio o no la carga horaria
	 * @throws CargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws CargaHorariaException - Excepción general
	 */
	@Override
	public CargaHoraria anadir(CargaHoraria entidad) throws CargaHorariaValidacionException, CargaHorariaException {
		CargaHoraria retorno = null;
		 
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.anadir.exception")));
			}
		} else {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.anadir.null.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD por detalle puesto
	 * @author Arturo VIllafuerte
	 * @param dtpsId Id del detalle puesto a ser buscado
	 * @param tfcrhrId Id de tipo funcion carga horaria a buscar
	 * @param pracId Id del periodo en el cual se va a realizar carga horaria
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CargaHoraria> buscarPorDetallePuesto(int dtpsId, int tfcrhrId, int pracId) throws CargaHorariaNoEncontradoException , CargaHorariaException {
		List<CargaHoraria> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crhr from CargaHoraria crhr where crhrDetallePuesto.dtpsId =:dtpsId");
			sbsql.append(" and crhrTipoFuncionCargaHoraria.tifncrhrId =:tfcrhrId");
			sbsql.append(" and crhrPeriodoAcademico.pracId =:pracId");
			sbsql.append(" and crhrEstadoEliminacion =:estadoNoEliminado");
			sbsql.append(" and crhrEstado =:estado");
			
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("dtpsId", dtpsId);
			q.setParameter("tfcrhrId", tfcrhrId);
			q.setParameter("pracId", pracId);
			q.setParameter("estadoNoEliminado", CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
			q.setParameter("estado", CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.detalle.puesto.no.result.exception")));
		} catch (Exception e) {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.detalle.puesto.exception")));
		}
		return retorno;
	}
	
	
	public CargaHoraria buscarCahrPorDetallePuesto(int dtpsId, int tfcrhrId, int pracId) throws CargaHorariaNoEncontradoException, CargaHorariaException, CargaHorariaValidacionException {
		CargaHoraria retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crhr from CargaHoraria crhr where crhrDetallePuesto.dtpsId =:dtpsId");
			sbsql.append(" and crhrTipoFuncionCargaHoraria.tifncrhrId =:tfcrhrId");
			sbsql.append(" and crhrPeriodoAcademico.pracId =:pracId");
			sbsql.append(" and crhrEstadoEliminacion =:estadoNoEliminado");
			sbsql.append(" and crhrEstado =:estado");
			
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("dtpsId", dtpsId);
			q.setParameter("tfcrhrId", tfcrhrId);
			q.setParameter("pracId", pracId);
			q.setParameter("estadoNoEliminado", CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
			q.setParameter("estado", CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			retorno = (CargaHoraria)q.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new CargaHorariaValidacionException("Se encontró mas de un resultado.");
		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.detalle.puesto.no.result.exception")));
		} catch (Exception e) {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.detalle.puesto.exception")));
		}
		return retorno;
	}
	
	//--v2
	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD por tipo de funcion carga horaria
	 * @author Arturo VIllafuerte
	 * @param tfcrhrId Id de tipo funcion carga horaria a buscar
	 * @param pracId Id del periodo en el cual se va a realizar carga horaria
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CargaHoraria> buscarPorTipFncCrHr(int tfcrhrId, int pracId) throws CargaHorariaNoEncontradoException , CargaHorariaException {
		List<CargaHoraria> retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crhr from CargaHoraria crhr where ");
			sbsql.append(" crhrTipoFuncionCargaHoraria.tifncrhrId =:tfcrhrId");
			sbsql.append(" and crhrPeriodoAcademico.pracId =:pracId");
			sbsql.append(" and crhrEstadoEliminacion =:estadoNoEliminado");
			sbsql.append(" and crhrEstado =:estado");
			
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("tfcrhrId", tfcrhrId);
			q.setParameter("pracId", pracId);
			q.setParameter("estadoNoEliminado", CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
			q.setParameter("estado", CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.tip.fnc.cr.hr.no.result.exception")));
		} catch (Exception e) {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.tip.fnc.cr.hr.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD por malla curricular materia
	 * @author Arturo VIllafuerte
	 * @param prlId Id de paralelo a buscar
	 * @param pracId peridodo academico a buscar
	 * @param mlcrprId Id de malla curricular paralelo a buscar 
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */ 
	@Override
	public CargaHoraria buscarPorMlCrMtXPeriodoXParalelo(int mlcrmtId, int pracId, int prlId) throws CargaHorariaNoEncontradoException , CargaHorariaException {
		CargaHoraria retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crhr from CargaHoraria crhr where ");
			sbsql.append(" crhr.crhrMallaCurricularParalelo.mlcrprMallaCurricularMateria.mlcrmtId =:mlcrmtId");
			sbsql.append(" and crhr.crhrMallaCurricularParalelo.mlcrprParalelo.prlId =:prlId");
			sbsql.append(" and crhr.crhrPeriodoAcademico.pracId =:pracId");
			sbsql.append(" and crhrEstado =:estado");
			
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("mlcrmtId", mlcrmtId);
			q.setParameter("prlId", prlId);  
			q.setParameter("pracId", pracId);  
			q.setParameter("estado", CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			retorno = (CargaHoraria) q.getSingleResult();
		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.mlcrmt.periodo.paralelo.no.result.exception")));
		} catch (Exception e) {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.mlcrmt.periodo.paralelo.exception")));
		}
		return retorno;
	}
	
	
		
	/**
	 * Método que sirve para agregar una nueva carga horaria
	 * @param dtps - dtps, entidad detalle puesto
	 * @param mlcrpr - mlcrpr entidad malla curricular paralelo
	 * @param prac - prac entidad, periodo academico
	 * @param numHoras - numHoras número de horas de la materia
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws CargaHorariaException - CargaHorariaException, excepción genearal.
	 */
	public boolean nuevo(DetallePuesto dtps, MallaCurricularParalelo mlcrpr, PeriodoAcademico prac, Integer numHoras, Integer numCreditos) throws CargaHorariaException, Exception{
		boolean retorno = false;
		try {
			if(dtps != null || mlcrpr != null || prac != null || numHoras != null){
				CargaHoraria crhrAux = new CargaHoraria();
				crhrAux.setCrhrDetallePuesto(dtps);
				crhrAux.setCrhrMallaCurricularParalelo(mlcrpr);
				crhrAux.setCrhrPeriodoAcademico(prac);
				crhrAux.setCrhrEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
				if(numHoras != 0 && numHoras != null){
					crhrAux.setCrhrNumHoras(numHoras.intValue());
				}
				if(numCreditos != 0 && numCreditos != null){
					crhrAux.setCrhrNumHoras(numCreditos.intValue());
				}
				crhrAux.setCrhrObservacion("HORAS CLASE");
				crhrAux.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
				em.persist(crhrAux);
				em.flush();
				retorno = true;
			}
		} catch (Exception e) {
			try {
				retorno = false;
				
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			}
			throw new CargaHorariaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para agregar una nueva carga horaria
	 * @param dtps - dtps, entidad detalle puesto
	 * @param mlcrpr - mlcrpr entidad malla curricular paralelo
	 * @param prac - prac entidad, periodo academico
	 * @param numHoras - numHoras número de horas de la materia
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws CargaHorariaException - CargaHorariaException, excepción genearal.
	 */
	public boolean editarDocente(Integer crhrId, Integer dtpsId, Integer mlcrprId, Integer pracId, Integer numHoras, Integer numCreditos) throws CargaHorariaException, Exception{
		boolean retorno = false;
		try {
			if(crhrId != null || dtpsId != null ){
				//busco la carga horaria
				CargaHoraria crhrAux = em.find(CargaHoraria.class, crhrId.intValue());
				//cambio de estado a la carga horaria para que quede registrado
				crhrAux.setCrhrEstado(CargaHorariaConstantes.ESTADO_INACTIVO_VALUE);
				crhrAux.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_ACTIVO_VALUE);
				
				//busco el detalle puesto del docente
				DetallePuesto dtpsAux = em.find(DetallePuesto.class, dtpsId.intValue());
				//busco la malla curricular paralelo
				MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, mlcrprId.intValue());
				//busco el periodo academico
				PeriodoAcademico pracAux = em.find(PeriodoAcademico.class, pracId.intValue());
				
				
				//creo una nueva carga horaria
				CargaHoraria crhrAgregar = new CargaHoraria();
				crhrAgregar.setCrhrDetallePuesto(dtpsAux);
				crhrAgregar.setCrhrMallaCurricularParalelo(mlcrprAux);
				crhrAgregar.setCrhrPeriodoAcademico(pracAux);
				crhrAgregar.setCrhrEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
				if(numHoras != 0 && numHoras != null){
					crhrAgregar.setCrhrNumHoras(numHoras.intValue());
				}
				if(numCreditos != 0 && numCreditos != null){
					crhrAgregar.setCrhrNumHoras(numCreditos.intValue());
				}
				crhrAgregar.setCrhrObservacion("HORAS CLASE");
				crhrAgregar.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
				em.persist(crhrAux);
				em.persist(crhrAgregar);
				em.flush();
				retorno = true;
			}
		} catch (Exception e) {
			e.getStackTrace();
			try {
				retorno = false;
				
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} 
			throw new CargaHorariaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para eliminar un docente de la carga horaria
	 * @param crhrId - crhrId, id de la carga horaria a eliminar
	 * @param listaEliminarMlcrprDocente - listaEliminarMlcrprDocente, lista de los horarios academicos a ser eliminados el docente
	 * @param mlcrprIdPadre - mlcrprIdPadre, id de la mallacurricular que se encuentra en mlcrprIdComp
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws CargaHorariaException - CargaHorariaException, excepción genearal.
	 */
	public boolean eliminarDocente(int crhrId, List<HorarioAcademicoDto> listaEliminarMlcrprDocente, int mlcrprIdPadre) throws CargaHorariaException, Exception{
		boolean retorno = false;
		try {
			if(mlcrprIdPadre == 0){//si es un docente que no tiene asigando como horario compartido
				CargaHoraria crhrAux = new CargaHoraria();
				crhrAux = em.find(CargaHoraria.class, crhrId);
				crhrAux.setCrhrEstado(CargaHorariaConstantes.ESTADO_INACTIVO_VALUE);
				crhrAux.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_ACTIVO_VALUE);
				if(crhrAux.getCrhrMallaCurricularParalelo() != null){
					crhrAux.setCrhrObservacion("DA CLASES - ELIMINADO - MLCRPRID: "+crhrAux.getCrhrMallaCurricularParalelo().getMlcrprId());
					crhrAux.setCrhrMallaCurricularParalelo(null);
				}
				if(listaEliminarMlcrprDocente.size() > 0){ // si tiene una lista de horarios academicos
					for (HorarioAcademicoDto itemElimminar : listaEliminarMlcrprDocente) { //elimina los horarios
						HorarioAcademico hracAux = new HorarioAcademico();
						hracAux = em.find(HorarioAcademico.class, itemElimminar.getHracId());
						em.remove(hracAux);
					}
				}
				em.persist(crhrAux);
				em.flush();
			}else {// cuando un docene tiene asiganado como horario compartido y es eliminado este compartido 
				if(listaEliminarMlcrprDocente.size() > 0){//si tiene una lista de horarios academicos
					for (HorarioAcademicoDto itemElimminar : listaEliminarMlcrprDocente) {
						HorarioAcademico hracAux = new HorarioAcademico();
						hracAux = em.find(HorarioAcademico.class, itemElimminar.getHracId());
						em.remove(hracAux);
					}
				}
			}
			retorno = true;
		} catch (Exception e) {
			try {
				retorno = false;
				
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} 
			throw new CargaHorariaException(e.getMessage());
		}
		return retorno;
	}
	
	public CargaHoraria buscarPorMallaCurricularParalelo(int mlcrprId, int cahrEstado) throws CargaHorariaNoEncontradoException , CargaHorariaValidacionException, CargaHorariaException{
		CargaHoraria retorno = null;
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select crhr from CargaHoraria crhr where ");
		sbsql.append(" crhr.crhrMallaCurricularParalelo.mlcrprId = :mlcrprId");
		sbsql.append(" and crhrEstado = :estado");
		
		try {
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("mlcrprId", mlcrprId);
			q.setParameter("estado", cahrEstado);
			retorno = (CargaHoraria) q.getSingleResult();
		} catch (NoResultException e) {
			throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.mlcrmt.periodo.paralelo.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new CargaHorariaValidacionException();
		} catch (Exception e) {
			throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.mlcrmt.periodo.paralelo.exception")));
		}
		return retorno;		
	}
}






