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
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioFuncionValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioFuncionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioFuncion;

/**
 * Clase (Bean)HorarioAcademicoImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HorarioFuncionImpl implements HorarioFuncionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource 
	private SessionContext session;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public HorarioFuncion guardar(HorarioFuncion entidad) throws HorarioFuncionException,HorarioFuncionValidacionException{
		HorarioFuncion retorno = null;
		
		try {
			
			if(entidad != null){
				
				try {
					Query sql = em.createNamedQuery("HorarioFuncion.findPorDiaHoraInicioDtpsId");
					sql.setParameter("dia", entidad.getHrfnDia());
					sql.setParameter("horaInicio", entidad.getHrfnHoraInicio());
					sql.setParameter("dtpsId", entidad.getHrfnDetallePuesto());
					sql.getSingleResult();
				} catch (Exception e) {
					session.getUserTransaction().begin();
					
					HorarioFuncion horario = new HorarioFuncion();
					horario.setHrfnDia(entidad.getHrfnDia());
					horario.setHrfnHoraInicio(entidad.getHrfnHoraInicio());
					horario.setHrfnHoraFin(entidad.getHrfnHoraFin());
					horario.setHrfnDescripcion(entidad.getHrfnDescripcion());
					horario.setHrfnEstado(entidad.getHrfnEstado());
					horario.setHrfnHoraClase(entidad.getHrfnHoraClase());
					horario.setHrfnFuncion(entidad.getHrfnFuncion());
					horario.setHrfnActividad(entidad.getHrfnActividad());
					horario.setHrfnDetallePuesto(entidad.getHrfnDetallePuesto());
					horario.setHrfnPeriodoAcademico(entidad.getHrfnPeriodoAcademico());

					em.persist(horario);
					em.flush();
					
					session.getUserTransaction().commit();
					retorno = horario;
				}
//				
			}else{
				throw new HorarioFuncionValidacionException("Error de validación, verificar que se encuentren todos los parámetros.");				
			}
			
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new HorarioFuncionException(e.getMessage());
		}
		
		return retorno;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int horarioId) throws HorarioFuncionException, HorarioFuncionValidacionException, HorarioFuncionNoEncontradoException{
		boolean retorno = false;

		HorarioFuncion hrfn = new HorarioFuncion();
		try {
			hrfn = em.find(HorarioFuncion.class, horarioId);
			if (hrfn != null) {
				try {
					session.getUserTransaction().begin();
					em.remove(em.contains(hrfn) ? hrfn : em.merge(hrfn));
					em.flush();	
					session.getUserTransaction().commit();
					retorno = true;
				} catch (Exception e) {
					throw new HorarioFuncionValidacionException("Error tipo SQL para eliminar Horario Función.");
				}
			} 
		} catch (NoResultException e) {
			throw new HorarioFuncionNoEncontradoException("No se encontró Horario Función para eliminar");
		}


		return retorno;
	}
}
