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

 ARCHIVO:    NotaCorteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla NotaCorte. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 11-07-2019           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteException;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.NotaCorteServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HistorialNotaCorteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NotaCorteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.HistorialNotaCorte;
import ec.edu.uce.academico.jpa.entidades.publico.NotaCorte;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;


/**
 * Clase (Bean) NotaCorteServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
  public class NotaCorteServicioImpl  implements NotaCorteServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/**
	 * Busca una entidad Nota corte por su id
	 * @param id - del Nota corte a a buscar
	 * @return Nota corte con el id solicitado
	 * @throws NotaCorteNoEncontradoException - Excepcion lanzada cuando no se encuentra una Nota corte con el id solicitado
	 * @throws NotaCorteException - Excepcion general
	 */
	@Override
	public NotaCorte buscarPorId(Integer id) throws NotaCorteNoEncontradoException, NotaCorteException {
		NotaCorte retorno = null;
		if (id != null) {
			try {
				retorno = em.find(NotaCorte.class,  id);
			} catch (NoResultException e) {
				
				throw new NotaCorteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.buscar.por.id.noEncontrado.exception",id)));
			}catch (NonUniqueResultException e) {
				
				throw new NotaCorteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.buscar.por.id.no.resultado.unico.exception",id)));
			} catch (Exception e) {
				
						
				throw new NotaCorteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.buscar.por.id.general.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Buscar Notas corte activas por Carrera y Periodo(corte)
	 * @param idCrr - idCarrrera de las Nota corte a buscar
	 * @param idPrac - idPeriodoAcademico de las Nota corte a buscar
	 * @return  Nota corte con los parametro indicados
	 * @throws NotaCorteException - Excepcion general
	 */
	@Override
	public NotaCorte buscarActivoXCrrXPrac(Integer idCrr, Integer idPrac ) throws  NotaCorteException {
		NotaCorte retorno = null;
		
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select nocr from NotaCorte nocr ");
				sbsql.append(" Where nocr.nocrEstado =:estadoActivo ");
				sbsql.append(" and nocr.nocrPracId =:idPrac ");
				sbsql.append(" and nocr.nocrCarrera.crrId =:idCrr ");
			    	Query q = em.createQuery(sbsql.toString());
				q.setParameter("estadoActivo", NotaCorteConstantes.ESTADO_ACTIVO_VALUE);
				q.setParameter("idPrac", idPrac);
				q.setParameter("idCrr", idCrr);
				retorno = (NotaCorte)q.getSingleResult();
				
			} catch (NoResultException e) {
				//throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("NotaCorte.buscar.activo.por.carrera.por.periodoo.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new NotaCorteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.buscar.activo.por.carrera.por.periodo.non.unique.result.exception")));
			} catch (Exception e) {
				throw new NotaCorteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.buscar.activo.por.carrera.por.periodo.exception")));
			}
		
		return retorno;
		
		
	}
	
	
	/**
	 * listar Notas corte activas en periodo distinto Periodo(corte)
	 * @param idPrac - idPeriodoAcademico de las Nota corte a buscar
	 */
	@SuppressWarnings("unchecked")
	public List<NotaCorte> listarActivoXPrac(Integer idPrac){
		List<NotaCorte> retorno = null;
		
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select nocr from NotaCorte nocr ");
				sbsql.append(" Where nocr.nocrEstado =:estadoActivo ");
				sbsql.append(" and nocr.nocrPracId <>:idPrac ");
			    	Query q = em.createQuery(sbsql.toString());
				q.setParameter("estadoActivo", NotaCorteConstantes.ESTADO_ACTIVO_VALUE);
				q.setParameter("idPrac", idPrac);
				retorno = q.getResultList();
				
			} catch (NoResultException e) {
				
			}catch (Exception e) {
				//	throw new NotaCorteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.listar.activos.diferente.periodo.exception")));
			}
		return retorno;
		
		
	}
	
	
	/**
	 * Añadir o editar nota de corte de una carrera en la BD
	 * @return true o False - Si se completa correctamente el guardado en BDD
	 * @throws NotaCorteValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws NotaCorteException - Excepción general
	 */
	@Override	
	public boolean editarGuardarNotaCorte(CarreraDto carreraNotaCorte, Integer NocrPracId, Usuario usuario ) throws  NotaCorteException {
		boolean retorno = false;
	
		if (carreraNotaCorte != null) {
			try {
				
					
					//Genero el historial de nota corte agregue o edite
					HistorialNotaCorte historialNotaCorteAux = new HistorialNotaCorte();
								
					NotaCorte notaCorteExisteAux = buscarActivoXCrrXPrac(carreraNotaCorte.getCrrId(), NocrPracId);
					
					if(notaCorteExisteAux!=null){//Existe notaCorte activa  se debe editar
						float notaAnterior =notaCorteExisteAux.getNocrNota(); //guardo la nota anterior que existe en la BDD
						
						if(notaAnterior!=carreraNotaCorte.getNocrNota()){ //solo se guarda si se cambio el valor de la nota
							notaCorteExisteAux.setNocrNota(carreraNotaCorte.getNocrNota());
						em.merge(notaCorteExisteAux);
						
						//Se crea registro historico editar
						historialNotaCorteAux.setHsnocrNotaCorte(notaCorteExisteAux);
						historialNotaCorteAux.setHsnocrNotaAnterior(notaAnterior);
						historialNotaCorteAux.setHsnocrTipoProceso(HistorialNotaCorteConstantes.TIPO_PROCESO_EDITAR_VALUE);
						historialNotaCorteAux.setHsnocrNotaAnterior(notaAnterior);
						historialNotaCorteAux.setHsnocrFechaRegistro(new Timestamp(new Date().getTime()));
						historialNotaCorteAux.setHsnocrUsrId(usuario.getUsrId());
						historialNotaCorteAux.setHsnocrUsrNick(usuario.getUsrNick());
						em.persist(historialNotaCorteAux);
						}
						
					}else{//Se crea nueva nota corte
						NotaCorte notaCorteNuevaAux = new NotaCorte();
						if(carreraNotaCorte.getNocrNota()!=null){
						Carrera carreraAux = em.find(Carrera.class, carreraNotaCorte.getCrrId());
						notaCorteNuevaAux.setNocrNota(carreraNotaCorte.getNocrNota());
						notaCorteNuevaAux.setNocrCarrera(carreraAux);
						notaCorteNuevaAux.setNocrEstado(NotaCorteConstantes.ESTADO_ACTIVO_VALUE);
						notaCorteNuevaAux.setNocrPracId(NocrPracId);
						em.persist(notaCorteNuevaAux);
						
						//Se crea registro historico de nuevo registro
						historialNotaCorteAux.setHsnocrNotaCorte(notaCorteNuevaAux);
						historialNotaCorteAux.setHsnocrTipoProceso(HistorialNotaCorteConstantes.TIPO_PROCESO_CREAR_VALUE);
						historialNotaCorteAux.setHsnocrFechaRegistro(new Timestamp(new Date().getTime()));
						historialNotaCorteAux.setHsnocrUsrId(usuario.getUsrId());
						historialNotaCorteAux.setHsnocrUsrNick(usuario.getUsrNick());
						em.persist(historialNotaCorteAux);
					
						}
						
					}
					            
				
					retorno = true;
				
			}  catch (Exception e) {
				//throw new AulaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aula.anadir.exception")));
				
				retorno = false;
			}
		} 
		return retorno;
	}
	
	
	
	
	
	
	@Override	
	public void desactivarNotaCorteAnteriores(List<NotaCorte> listaNotaCorte ) throws  NotaCorteException{
		try{
		for (NotaCorte notaCorte : listaNotaCorte) {
			NotaCorte notaCorteAux=em.find(NotaCorte.class, notaCorte.getNocrId());
			notaCorteAux.setNocrEstado(NotaCorteConstantes.ESTADO_INACTIVO_VALUE);
			em.merge(notaCorteAux);
			
		}
		
		}catch(Exception e){
			throw new NotaCorteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nota.corte.desactivar.anteriores.exception")));
			
		}
		
	}
	
}
	
	

