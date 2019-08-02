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

 ARCHIVO:     DetalleMatriculaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla DetalleMatricula. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-OCT-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.DetalleMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;

/**
 * Clase (Bean)DetalleMatricula.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class DetalleMatriculaServicioImpl implements DetalleMatriculaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad DetalleMatricula por su id
	 * @param id - de la DetalleMatricula a buscar
	 * @return DetalleMatricula con el id solicitado
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula con el id solicitado
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	@Override
	public DetalleMatricula buscarPorId(Integer id) throws DetalleMatriculaNoEncontradoException, DetalleMatriculaException {
		DetalleMatricula retorno = null;
		if (id != null) {
			try {
				retorno = em.find(DetalleMatricula.class, id);
			} catch (NoResultException e) {
				throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades DetalleMatricula existentes en la BD
	 * @return lista de todas las entidades DetalleMatricula existentes en la BD
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula 
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalleMatricula> listarTodos() throws DetalleMatriculaNoEncontradoException , DetalleMatriculaException {
		List<DetalleMatricula> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from DetalleMatricula crr ");
			sbsql.append(" where crr.crrTipo =:tipoDetalleMatricula ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.listar.todos.exception")));
		}
		return retorno;
	}

	public boolean editar(DetalleMatriculaDto detalleVigente,  RecordEstudianteDto recordVigente, int mlcrprIdNuevo) throws DetalleMatriculaNoEncontradoException, DetalleMatriculaException, RecordEstudianteNoEncontradoException, RecordEstudianteException, MallaCurricularParaleloDtoNoEncontradoException, MallaCurricularParaleloDtoException{
		boolean retorno = false;
		
		boolean isMlcrprCorrecto = false;
		boolean isDtmtCorrecto = false;
		boolean isRcesCorrecto = false;

		MallaCurricularParalelo mlcrprNueva = null;
		
		try {

			mlcrprNueva = em.find(MallaCurricularParalelo.class, mlcrprIdNuevo);
			mlcrprNueva.setMlcrprInscritos(mlcrprNueva.getMlcrprInscritos().intValue() + 1);
			em.merge(mlcrprNueva);
			em.flush();
			
			MallaCurricularParalelo mlcrprVigente = em.find(MallaCurricularParalelo.class, detalleVigente.getDtmtMallaCurricularParaleloDto().getMlcrprId());
			mlcrprVigente.setMlcrprInscritos(mlcrprVigente.getMlcrprInscritos().intValue() - 1);
			em.merge(mlcrprVigente);
			em.flush();
			
			isMlcrprCorrecto = true;
		} catch (NoResultException e) {
			throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}


		try {

			DetalleMatricula dtmtVigente = em.find(DetalleMatricula.class, detalleVigente.getDtmtId());
			dtmtVigente.setDtmtMallaCurricularParalelo(mlcrprNueva);
			em.merge(dtmtVigente);
			em.flush();
			
			isDtmtCorrecto = true;
		} catch (NoResultException e) {
			throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}


		try {

			RecordEstudiante rcesVigente = em.find(RecordEstudiante.class, recordVigente.getRcesId());
			rcesVigente.setRcesMallaCurricularParalelo(mlcrprNueva);
			em.merge(rcesVigente);
			em.flush();

			isRcesCorrecto = true;
		} catch (NoResultException e) {
			throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}
		
		if (isMlcrprCorrecto && isDtmtCorrecto && isRcesCorrecto) {
			retorno = true;
		}

		return retorno;
	}
	
	/**
	 * Método que elimina el detalle matricula 
	 * 
	 * @throws DetalleMatriculaNoEncontradoException 
	 * @throws DetalleMatriculaException 
	 */
	public boolean eliminarFull(int dtmtId, int cmpaId, int rcesId, int mlcrprId, List<MateriaDto> matriculaFinal, Gratuidad gratuidad, int tipoGratuidadId, int rcesEstado, int fcmtId, Nivel nivel) throws DetalleMatriculaNoEncontradoException, DetalleMatriculaException{
		boolean retorno = false;
		try {
			if(dtmtId != 0 && cmpaId != 0 && rcesId != 0 ){

				BigDecimal valorTotal = BigDecimal.ZERO;
				for(MateriaDto item: matriculaFinal){
					valorTotal = valorTotal.add(item.getValorMatricula());
					
					DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId());
					dtmtAux.setDtmtValorPorMateria(item.getValorMatricula());
					dtmtAux.setDtmtValorParcial(item.getValorMatricula());
					em.merge(dtmtAux);

					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId());
					rcesAux.setRcesEstado(rcesEstado);
					em.merge(rcesAux);

				}


				DetalleMatricula dtmtEliminaAux = em.find(DetalleMatricula.class, dtmtId);
				em.remove(dtmtEliminaAux);

				ComprobantePago cmpaAux = em.find(ComprobantePago.class, cmpaId);
				cmpaAux.setCmpaNumComprobante(null);
				cmpaAux.setCmpaFechaEmision(null);
				cmpaAux.setCmpaFechaCaduca(null);
				cmpaAux.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EDITADO_VALUE);
				cmpaAux.setCmpaTotalPago(valorTotal);
				em.merge(cmpaAux); 

				FichaMatricula fcmtAux = em.find(FichaMatricula.class, fcmtId);
				fcmtAux.setFcmtNivelUbicacion(nivel.getNvlId());
				fcmtAux.setFcmtValorTotal(valorTotal);
				em.merge(fcmtAux);

				Gratuidad gratuidadAux = em.find(Gratuidad.class, gratuidad.getGrtId());
				gratuidadAux.setGrtTipoGratuidad(new TipoGratuidad(tipoGratuidadId));
				em.merge(gratuidadAux);


				RecordEstudiante rcesEliminarAux = em.find(RecordEstudiante.class, rcesId);
				em.remove(rcesEliminarAux);


				MallaCurricularParalelo mlcrprActualizaAux = em.find(MallaCurricularParalelo.class, mlcrprId);
				if(mlcrprActualizaAux.getMlcrprInscritos() != null || mlcrprActualizaAux.getMlcrprInscritos().intValue() > 0){
					mlcrprActualizaAux.setMlcrprInscritos(mlcrprActualizaAux.getMlcrprInscritos() - 1);
				}else{
					mlcrprActualizaAux.setMlcrprInscritos(Integer.valueOf(0));
				}
				em.merge(mlcrprActualizaAux);

				retorno = true;
			}
		} catch (NoResultException e) {
			throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.id.malla.materia.exception")));
		}
		return retorno;
	}
	
	
	/**MQ
	 * Lista todas las entidades DetalleMatricula existentes en la BD, por comprobante de pago
	 * @return lista de todas las entidades DetalleMatricula existentes en la BD, por comprobante de pago
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula 
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalleMatricula> listarXCmpaId(Integer cmpaId) throws  DetalleMatriculaException {
		List<DetalleMatricula> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtmt from DetalleMatricula dtmt ");
			sbsql.append(" where dtmt.dtmtComprobantePago.cmpaId =:cmpaId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("cmpaId", cmpaId);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.listar.por.comprobante.pago.exception")));
		}
		if (retorno.size() == Integer.valueOf(0)) {
			retorno = null;
			//throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.listar.por.comprobante.pago.no.encontrado.exception")));
		}
		
		return retorno;
	}
	
	
	/**MQ
	 * Lista todas la entidad DetalleMatricula existentes en la BD, por comprobante de pago y malla curricular paralelo
	 * @return la entidad DetalleMatricula existentes en la BD, por comprobante de pago y malla curricular paralelo
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula 
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	@Override
	public DetalleMatricula buscarXCmpaIdxMlcrprId(Integer cmpaId, Integer mlcrprId) throws  DetalleMatriculaException, DetalleMatriculaNoEncontradoException {
	  DetalleMatricula retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dtmt from DetalleMatricula dtmt ");
			sbsql.append(" where dtmt.dtmtComprobantePago.cmpaId =:cmpaId ");
			sbsql.append(" and dtmt.dtmtMallaCurricularParalelo.mlcrprId =:mlcrprId ");
			Query q = em.createQuery(sbsql.toString());
			  q.setParameter("cmpaId", cmpaId);
			  q.setParameter("mlcrprId", mlcrprId);
			
			  retorno = (DetalleMatricula) q.getSingleResult();
		
		} catch (NoResultException e) {
			throw new DetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.cmpa.por.mlcrpr.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.cmpa.por.mlcrpr.non.unique.result.exception")));
		} catch (Exception e) {
			throw new DetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DetalleMatricula.buscar.por.cmpa.por.mlcrpr.exception")));
		}
		
		return retorno;
	}

}