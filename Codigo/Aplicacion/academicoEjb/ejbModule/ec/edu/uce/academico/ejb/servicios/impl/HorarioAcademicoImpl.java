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

 ARCHIVO:     HorarioAcademicoImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla HorarioAcademico. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 18-SEP-2017           Marcelo Quishpe 						Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
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
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;

/**
 * Clase (Bean)HorarioAcademicoImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HorarioAcademicoImpl implements HorarioAcademicoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource 
	private SessionContext session;
	
	
	/**
	 * Lista todas los HorariosAcademicos existentes en la BD  con idHoraClaseAula
	 * @return lista de todas los HorariosAcademicos existentes en la BD con idHoraClaseAula
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List <HorarioAcademico> listarHorarioAcademicoXhoraClaseAulaId(int horaClaseAulaId) throws HorarioAcademicoNoEncontradoException , HorarioAcademicoException {
		List <HorarioAcademico> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select hrac from HorarioAcademico hrac ");
			sbsql.append(" Where hrac.hracHoraClaseAula.hoclalId =:horaClaseAulaId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("horaClaseAulaId", horaClaseAulaId);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new HorarioAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademico.listar.HorarioAcademico.por.hora.clase.aula.id.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			throw new HorarioAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HorarioAcademico.listar.HorarioAcademico.por.hora.clase.aula.id.no.result.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Método que sirve para editar el horario academico
	 * @param entidad - entidad, entidad horario academico ha ser editada
	 * @return, retorna verdadero si se ejecuto la edición
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean editar(HorarioAcademico entidad) throws HorarioAcademicoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			
			if(entidad != null){
				HorarioAcademico hracAux = em.find(HorarioAcademico.class, entidad.getHracId());
				if(hracAux.getHracId() != 0 || hracAux.getHracHoraClaseAula() != null || hracAux.getHracMallaCurricularParalelo() != null){
					hracAux.setHracEstado(entidad.getHracEstado());
					hracAux.setHracDia(entidad.getHracDia());
					hracAux.setHracHoraClaseAula(entidad.getHracHoraClaseAula());
					hracAux.setHracMallaCurricularParalelo(entidad.getHracMallaCurricularParalelo());
					session.getUserTransaction().commit();
					retorno = true;
				}
			}
			
		} catch (Exception e) {
			try {
				retorno = false;
				session.getUserTransaction().rollback();
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SystemException e1) {
				retorno = false;
				e1.printStackTrace();
			}
			throw new HorarioAcademicoException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para eliminar un horario academico
	 * @param hracId - hracId, id del horario academico a eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int hracId) throws HorarioAcademicoException, HorarioAcademicoValidacionException, HorarioAcademicoNoEncontradoException{
		boolean retorno = false;
			
			try {
				HorarioAcademico hracAux = em.find(HorarioAcademico.class, hracId);
				if (hracAux != null) {
					Query sql = em.createNamedQuery("HorarioAcademico.findHorariosPorMlcrprIdPrincipal");
					sql.setParameter("mlcrprIdPrincipal", hracAux.getHracMallaCurricularParalelo().getMlcrprId());
					List<HorarioAcademico> horariosPrincipal = sql.getResultList();	
					if (!horariosPrincipal.isEmpty()) {
						throw new HorarioAcademicoValidacionException("No se puede eliminar la Hora de Clase ya que el Horario se encuentra Compartido.");
					}else {
						try {
							session.getUserTransaction().begin();
							hracAux = em.find(HorarioAcademico.class, hracId);
							em.remove(hracAux);
							em.flush();	
							session.getUserTransaction().commit();
							retorno = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} 
			} catch (NoResultException e) {
				throw new HorarioAcademicoNoEncontradoException("No se encontró Horario Académico para eliminar");
			}
			
		
		return retorno;
	}
	
	/**
	 * Método que sirve para agregar un horario academico
	 * @param entidad - entidad, entidad horario academico
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean nuevo(HorarioAcademico entidad) throws HorarioAcademicoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			
			if(entidad != null){
				HorarioAcademico hracAux = new HorarioAcademico();
				hracAux.setHracDia(entidad.getHracDia());
				hracAux.setHracEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				hracAux.setHracHoraClaseAula(entidad.getHracHoraClaseAula());
				hracAux.setHracMallaCurricularParalelo(entidad.getHracMallaCurricularParalelo());
				em.persist(hracAux);
				em.flush();
				session.getUserTransaction().commit();
				retorno = true;
			}
		} catch (Exception e) {
			try {
				retorno = false;
				session.getUserTransaction().rollback();
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SystemException e1) {
				retorno = false;
				e1.printStackTrace();
			}
			throw new HorarioAcademicoException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que sirve para compartir un horario academico
	 * @param entidadAsignar - entidadAsignar, entidad horario academico a ser asignada
	 * @param listHorarioComp - listHorarioComp, lista de horarios academicos a compartir
	 * @param docente - docente, información docente a compartir
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean compartir(HorarioAcademicoDto entidadAsignar, List<HorarioAcademicoDto> listHorarioComp , DocenteJdbcDto docente) throws HorarioAcademicoException, Exception{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			if(entidadAsignar != null && listHorarioComp.size() > 0){
				//BUSCO LA MALLA CURRICULAR PARALELO QUE SE VA ASIGNAR
				MallaCurricularParalelo entidadMlcrprAsignar = new MallaCurricularParalelo();
				entidadMlcrprAsignar = em.find(MallaCurricularParalelo.class, entidadAsignar.getMlcrprId().intValue());
				if(entidadMlcrprAsignar != null){
					if(docente != null){
						//***********************************************************//
						//************** PROCESO PARA AGREGAR OTRO REGISTRO DE ******//
						//********************** HORARIO ACADEMICO ******************//
						//******************* DEL MISMO DOCENTE *********************//
						//						DetallePuesto dtpsBuscar = new DetallePuesto();
						//						dtpsBuscar = em.find(DetallePuesto.class, docente.getDtpsId());
						//						PeriodoAcademico pracBuscar = new PeriodoAcademico();
						//						pracBuscar = em.find(PeriodoAcademico.class, entidadAsignar.getPracId());
						//						if(dtpsBuscar != null && pracBuscar != null){
						//							CargaHoraria crHrAsignar = new CargaHoraria();
						//							crHrAsignar.setCrhrDetallePuesto(dtpsBuscar);
						//							crHrAsignar.setCrhrMallaCurricularParalelo(entidadMlcrprAsignar);
						//							crHrAsignar.setCrhrPeriodoAcademico(pracBuscar);
						//							crHrAsignar.setCrhrEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
						//							if(entidadAsignar.getMtrHoras() != 0 && entidadAsignar.getMtrHoras() != null){
						//								crHrAsignar.setCrhrNumHoras(entidadAsignar.getMtrHoras().intValue());
						//							}
						//							if(entidadAsignar.getMtrCreditos() != 0 && entidadAsignar.getMtrCreditos() != null){
						//								crHrAsignar.setCrhrNumHoras(entidadAsignar.getMtrCreditos().intValue());
						//							}
						//							crHrAsignar.setCrhrObservacion("HORAS CLASE");
						//							crHrAsignar.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
						//							em.persist(crHrAsignar);
						//							em.flush();
						//						}
						//***********************************************************//
						//***********************************************************//
						//					}
						for (HorarioAcademicoDto itemComp : listHorarioComp) {
							HorarioAcademico hracAuxComp = new HorarioAcademico();
							HorarioAcademico hracAuxAsig = new HorarioAcademico();
							//BUSCO EL HORARIO ACADEMICO A SER COMPARTIDO
							hracAuxComp = em.find(HorarioAcademico.class, itemComp.getHracId().intValue());
							if(hracAuxComp != null){
								//SETEO LOS VALORES DEL COMPARTIDO A UN NUEVO PARA LA ASIGNACIÓN
								hracAuxAsig.setHracDia(hracAuxComp.getHracDia());
								hracAuxAsig.setHracEstado(hracAuxComp.getHracEstado());
								hracAuxAsig.setHracHoraClaseAula(hracAuxComp.getHracHoraClaseAula());
								hracAuxAsig.setHracMallaCurricularParalelo(entidadMlcrprAsignar);
								//inicio compartido
								hracAuxAsig.setMlcrprIdComp(hracAuxComp.getHracMallaCurricularParalelo().getMlcrprId());
								//fin compartido
								em.persist(hracAuxAsig);
								em.flush();

							}
						}
						//inicio compartido
					}
					//fin compartido
				}
				session.getUserTransaction().commit();
				retorno = true;
			}
			
		} catch (Exception e) {
			try {
				e.getStackTrace();
				retorno = false;
				session.getUserTransaction().rollback();
				e.getStackTrace();
			} catch (IllegalStateException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				retorno = false;
				e1.printStackTrace();
			} catch (SystemException e1) {
				retorno = false;
				e1.printStackTrace();
			}
			throw new HorarioAcademicoException(e.getMessage());
		}
		return retorno;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public HorarioAcademico guardar(HorarioAcademico entidad) throws HorarioAcademicoException, HorarioAcademicoValidacionException{
		HorarioAcademico retorno = null;
		
		try {
			session.getUserTransaction().begin();
			if(entidad != null){
				
				try {
					Query sql = em.createNamedQuery("HorarioAcademico.findPorDiaHoInicioMlcrprId");
					sql.setParameter("dia", entidad.getHracDia());
					sql.setParameter("horaInicio", entidad.getHracHoraInicio());
					sql.setParameter("mlcrprId", entidad.getHracMallaCurricularParalelo().getMlcrprId());
					sql.getSingleResult();
				} catch (Exception e) {
					HorarioAcademico horario = new HorarioAcademico();
					horario.setHracDia(entidad.getHracDia());
					horario.setHracHoraInicio(entidad.getHracHoraInicio());
					horario.setHracHoraFin(entidad.getHracHoraFin());
					horario.setHracDescripcion(entidad.getHracDescripcion());
					horario.setHracEstado(entidad.getHracEstado());
					horario.setHracHoraClaseAula(entidad.getHracHoraClaseAula());
					horario.setHracMallaCurricularParalelo(entidad.getHracMallaCurricularParalelo());
					horario.setHracHoraTipo(entidad.getHracHoraTipo());

					em.persist(horario);
					em.flush();
					
					session.getUserTransaction().commit();
					retorno = horario;
				}
				
			}else{
				throw new HorarioAcademicoValidacionException("Error de validación, verificar que se encuentren todos los parámetros.");				
			}
			
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new HorarioAcademicoException(e.getMessage());
		}
		
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean compartir(int mlcrprIdPrincipal, int mlcrprIdCompartida) throws CargaHorariaNoEncontradoException, CargaHorariaValidacionException, CargaHorariaException, HorarioAcademicoNoEncontradoException, HorarioAcademicoException{
		boolean retorno = false;

		try {
			session.getUserTransaction().begin();
			
			List<HorarioAcademico> horariosPrincipal = new ArrayList<>();
			try{
				Query sql = em.createNamedQuery("HorarioAcademico.findPorMallaCurricularParaleloId");
				sql.setParameter("mlcrprId", mlcrprIdPrincipal);
				horariosPrincipal = sql.getResultList();	
			}catch (Exception e) {
				throw new HorarioAcademicoNoEncontradoException("No se encontró Horario Académico asignado a la Asignatura que desea Compartir.");
			}
			
			if (!horariosPrincipal.isEmpty()) {
				
				try {
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprIdCompartida);
					for (HorarioAcademico horaClase : horariosPrincipal) {
						HorarioAcademico horario = new HorarioAcademico();
						horario.setHracDia(horaClase.getHracDia());
						horario.setHracHoraInicio(horaClase.getHracHoraInicio());
						horario.setHracHoraFin(horaClase.getHracHoraFin());
						horario.setHracDescripcion(horaClase.getHracDescripcion());
						horario.setHracEstado(horaClase.getHracEstado());
						horario.setHracHoraClaseAula(horaClase.getHracHoraClaseAula());
						horario.setHracMallaCurricularParalelo(mallaCurricularParalelo);
						horario.setMlcrprIdComp(mlcrprIdPrincipal);
						horario.setHracHoraTipo(horaClase.getHracHoraTipo());

						em.persist(horario);
						em.flush();
					}
					
				} catch (Exception e) {
					throw new HorarioAcademicoNoEncontradoException("No se encontró Malla Curricular Paralelo a la Asignatura que desea Compartir.");
				}
				
			}
			
			
			try {
				Query q = em.createNamedQuery("CargaHoraria.findPorMallaCurricularParaleloId");
				q.setParameter("mlcrprId", mlcrprIdPrincipal);
				q.setParameter("crhrEstado", CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				q.setParameter("crhrEstadoEliminacion", CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
				CargaHoraria cahr = (CargaHoraria) q.getSingleResult();
				
				CargaHoraria cargaCompartida = new CargaHoraria();
				cargaCompartida.setCrhrObservacion(cahr.getCrhrObservacion());
				cargaCompartida.setCrhrNumHoras(cahr.getCrhrNumHoras());
				cargaCompartida.setCrhrEstado(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				cargaCompartida.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
				cargaCompartida.setCrhrDetallePuesto(cahr.getCrhrDetallePuesto());
				cargaCompartida.setCrhrMallaCurricularParalelo(new MallaCurricularParalelo(mlcrprIdCompartida));
				cargaCompartida.setCrhrMlcrprIdPrincipal(mlcrprIdPrincipal);
				cargaCompartida.setCrhrPeriodoAcademico(cahr.getCrhrPeriodoAcademico());
				
				em.persist(cargaCompartida);
				em.flush();
				
			} catch (NoResultException e) {
				throw new CargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.mlcrmt.periodo.paralelo.no.result.exception")));
			} catch (NonUniqueResultException e) {
				throw new CargaHorariaValidacionException("Se encontró mas de una Carga Horaria Activa con los parámetros de búsqueda.");
			} catch (Exception e) {
				throw new CargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CargaHoraria.buscar.mlcrmt.periodo.paralelo.exception")));
			}
			
			session.getUserTransaction().commit();
			retorno =  true;

		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new HorarioAcademicoException(e.getMessage());
		}
		return retorno;
	}
}
