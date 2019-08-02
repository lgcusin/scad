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

 ARCHIVO:     ParaleloServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Paralelo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 28-04-2017           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Clase (Bean)ParaleloServicioImpl. Bean declarado como Stateless.
 * 
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class ParaleloServicioImpl implements ParaleloServicio {

	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto;

	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad  - entidad a editar
	 * @return la entidad editada
	 * @throws ParaleloValidacionException - Excepción lanzada en el caso de que no finalizo todas las  validaciones
	 * @throws ParaleloNoEncontradoException  - Excepción lanzada si no se encontró la entidad a editar
	 * @throws ParaleloException - Excepción general
	 */
	@Override
	public Boolean editar(ParaleloDto entidad) throws ParaleloValidacionException, ParaleloException {
		Boolean retorno = false;
		if (entidad != null) {
			try {
				if (verificarDescripcion(entidad, GeneralesConstantes.APP_EDITAR)
						&& verificarCodigo(entidad, GeneralesConstantes.APP_EDITAR)) { // verificar
																						// que
																						// no
																						// repita
																						// la
																						// descripcion
																						// del
																						// paralelo
					Paralelo paraleloAux = em.find(Paralelo.class, entidad.getPrlId());
					paraleloAux.setPrlCupo(null);
					paraleloAux.setPrlCodigo(
							GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrlCodigo()).toUpperCase());
					paraleloAux.setPrlDescripcion(
							GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrlDescripcion()).toUpperCase());
					paraleloAux.setPrlEstado(entidad.getPrlEstado());
					retorno = true;
				} else {

					throw new ParaleloValidacionException(MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.duplicado.exception")));
				}

			} catch (ParaleloValidacionException e) {
				throw new ParaleloValidacionException(e.getMessage());
			} catch (Exception e) {
				// e.printStackTrace();
				throw new ParaleloException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.exception")));
			}
		} else {

			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.null.exception")));

		}
		return retorno;
	}

	/**
	 * Edita los atributos enviados como parámetros de la entidad indicada
	 * 
	 * @param prlId,
	 *            cupo, descripcion, codigo, estado
	 * @return boolean, si se editó o no el paralelo
	 * @throws ParaleloValidacionException
	 *             - Excepción lanzada en el caso de que no finalizo todas las
	 *             validaciones
	 * @throws ParaleloNoEncontradoException
	 *             - Excepción lanzada si no se encontró la entidad a editar
	 * @throws ParaleloException
	 *             - Excepción general
	 */
	@Override
	public Boolean editarXParametros(int prlId, Integer cupoNuevo, String descripcionNuevo, String codigoNuevo,
			Integer estadoNuevo) throws ParaleloValidacionException, ParaleloNoEncontradoException, ParaleloException {
		Boolean retorno = false;
		Paralelo paraleloAux;
		if ((prlId != GeneralesConstantes.APP_CERO_VALUE) && (prlId != GeneralesConstantes.APP_ID_BASE)) {
			try {
				paraleloAux = em.find(Paralelo.class, prlId);
				paraleloAux.setPrlCupo(cupoNuevo);
				paraleloAux.setPrlCodigo(codigoNuevo);
				paraleloAux.setPrlDescripcion(descripcionNuevo);
				paraleloAux.setPrlEstado(estadoNuevo);
				retorno = true;
			} catch (Exception e) {
				throw new ParaleloException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.null.exception")));
		}
		return retorno;
	}

	/**
	 * Añade un Paralelo en la BD
	 * 
	 * @return Si se añadio o no el paralelo
	 * @throws ParaleloValidacionException
	 *             - Excepción lanzada en el caso de que no finalizó todas las
	 *             validaciones
	 * @throws ParaleloException
	 *             - Excepción general
	 */
	@Override
	public boolean anadir(ParaleloDto entidad, PeriodoAcademico periodo, Carrera carrera)
			throws ParaleloValidacionException, ParaleloException {
		Boolean retorno = false;
		if (entidad != null) {
			try {

				// entidad.setPracId(periodo.getPracId());
				if (verificarDescripcion(entidad, GeneralesConstantes.APP_NUEVO)
						&& verificarCodigo(entidad, GeneralesConstantes.APP_NUEVO)) {
					Paralelo AuxEntidad = new Paralelo();

					// seteo los parametros de la entidad paralelo JPA con los
					// parametros del objetoDto(ParaleloDto) enviado como
					// parametro
					AuxEntidad.setPrlCodigo(
							GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrlCodigo()).toUpperCase());
					AuxEntidad.setPrlDescripcion(
							GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrlDescripcion()).toUpperCase());
					AuxEntidad.setPrlCupo(entidad.getPrlCupo());
					AuxEntidad.setPrlEstado(entidad.getPrlEstado());
					// Objetos Periodo y carrera
					AuxEntidad.setPrlPeriodoAcademico(periodo);
					AuxEntidad.setPrlCarrera(carrera);
					em.persist(AuxEntidad);
					retorno = true;
				} else {
					throw new ParaleloValidacionException(MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.duplicado.exception")));
				}
			} catch (ParaleloValidacionException e) {
				throw new ParaleloValidacionException(e.getMessage());
			} catch (Exception e) {
				throw new ParaleloException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.null.exception")));
		}
		return retorno;
	}

	/**
	 * Añade un Paralelo y sus mallas curriculares en la BD
	 * 
	 * @return Si se añadio o no el paralelo
	 * @throws ParaleloValidacionException
	 *             - Excepción lanzada en el caso de que no finalizó todas las
	 *             validaciones
	 * @throws ParaleloException
	 *             - Excepción general
	 */
	@Override
	public boolean anadirParaleloMlCrPr(ParaleloDto entidad, List<MallaCurricularDto> listaMallaCurricular)
			throws ParaleloValidacionException, ParaleloException {
		Boolean retorno = false;
		if (entidad != null && listaMallaCurricular.size() != 0) {
			try {
				Carrera crrAux = em.find(Carrera.class, entidad.getCrrId());
				PeriodoAcademico pracAux = em.find(PeriodoAcademico.class, entidad.getPracId());
				Paralelo prlAux = new Paralelo();
				prlAux.setPrlCarrera(crrAux);
				prlAux.setPrlPeriodoAcademico(pracAux);
				prlAux.setPrlCodigo(entidad.getPrlCodigo());
				prlAux.setPrlDescripcion(entidad.getPrlCodigo());
				prlAux.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(prlAux);
				em.flush();

				for (MallaCurricularDto item : listaMallaCurricular) {
					if (item.isMtrAsignada()) {
						System.out.println(item.getMlcrmtId());
						MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
						mlcrmtAux = em.find(MallaCurricularMateria.class, item.getMlcrmtId());
						// Materia mtrAux = em.find(Materia.class,
						// mlcrmtAux.getMlcrmtMateria().getMtrId());
						// List<Materia> listaMaterias = null;
						// if(item.getTimtId()==TipoMateriaConstantes.TIPO_MODULAR_VALUE){
						// listaMaterias = new ArrayList<Materia>();
						// listaMaterias=listarTodosModulos(mtrAux.getMtrId());
						// for (Materia materia : listaMaterias) {
						// MallaCurricularParalelo mlcrprAux = new
						// MallaCurricularParalelo();
						// StringBuffer sbsql = new StringBuffer();
						// sbsql.append(" Select mlcrmt from
						// MallaCurricularMateria mlcrmt ");
						// sbsql.append(" where mlcrmt.mlcrmtMateria.mtrId =
						// ");sbsql.append(materia.getMtrId());
						// sbsql.append(" and mlcrmtEstado =
						// ");sbsql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
						// Query q = em.createQuery(sbsql.toString());
						// MallaCurricularMateria mlcrmtAux1 =
						// (MallaCurricularMateria) q.getSingleResult();
						//
						// mlcrprAux.setMlcrprParalelo(prlAux);
						// mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmtAux1);
						// mlcrprAux.setMlcrprCupo(item.getMlcrprCupo());
						// mlcrprAux.setMlcrprInscritos(0);
						// if(item.getMlcrprReservaRepetidos()!=null){
						// mlcrprAux.setMlcrprReservaRepetidos(item.getMlcrprReservaRepetidos());
						// }else{
						// mlcrprAux.setMlcrprReservaRepetidos(0);
						// }
						// em.persist(mlcrprAux);
						// em.flush();
						// System.out.println("creada la materia
						// "+materia.getMtrDescripcion());
						// }
						// }
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux.setMlcrprParalelo(prlAux);
						mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmtAux);
						mlcrprAux.setMlcrprCupo(item.getMlcrprCupo());
						mlcrprAux.setMlcrprInscritos(0);
						if (item.getMlcrprReservaRepetidos() != null) {
							mlcrprAux.setMlcrprReservaRepetidos(item.getMlcrprReservaRepetidos());
						} else {
							mlcrprAux.setMlcrprReservaRepetidos(0);
						}
						em.persist(mlcrprAux);
						em.flush();

					}
				}
				retorno = true;
			} catch (Exception e) {
				throw new ParaleloException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.null.exception")));
		}
		return retorno;
	}

	/**
	 * Añade un Paralelo y sus mallas curriculares en la BD
	 * 
	 * @return Si se añadio o no el paralelo
	 * @throws ParaleloValidacionException
	 *             - Excepción lanzada en el caso de que no finalizó todas las
	 *             validaciones
	 * @throws ParaleloException
	 *             - Excepción general
	 */
	@Override
	public boolean anadirParaleloMlCrPrNivelacion(ParaleloDto entidad, List<MallaCurricularDto> listaMallaCurricular)
			throws ParaleloValidacionException, ParaleloException {
		Boolean retorno = false;
		if (entidad != null && listaMallaCurricular.size() != 0) {
			try {
				Carrera crrAux = em.find(Carrera.class, entidad.getMlcrprNivelacionCrrId());
				PeriodoAcademico pracAux = em.find(PeriodoAcademico.class, entidad.getPracId());
				Paralelo prlAux = new Paralelo();
				prlAux.setPrlCarrera(crrAux);
				prlAux.setPrlPeriodoAcademico(pracAux);
				prlAux.setPrlCodigo(entidad.getPrlCodigo());
				prlAux.setPrlDescripcion(entidad.getPrlCodigo());
				prlAux.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(prlAux);
				em.flush();
				for (MallaCurricularDto item : listaMallaCurricular) {
					if (item.isMtrAsignada()) {
						MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
						mlcrmtAux = em.find(MallaCurricularMateria.class, item.getMlcrmtId());
						// Materia mtrAux = em.find(Materia.class,
						// mlcrmtAux.getMlcrmtMateria().getMtrId());
						// List<Materia> listaMaterias = null;
						// if(item.getTimtId()==TipoMateriaConstantes.TIPO_MODULAR_VALUE){
						// listaMaterias = new ArrayList<Materia>();
						// listaMaterias=listarTodosModulos(mtrAux.getMtrId());
						// for (Materia materia : listaMaterias) {
						// MallaCurricularParalelo mlcrprAux = new
						// MallaCurricularParalelo();
						// StringBuffer sbsql = new StringBuffer();
						// sbsql.append(" Select mlcrmt from
						// MallaCurricularMateria mlcrmt ");
						// sbsql.append(" where mlcrmt.mlcrmtMateria.mtrId =
						// ");sbsql.append(materia.getMtrId());
						// sbsql.append(" and mlcrmtEstado =
						// ");sbsql.append(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
						// Query q = em.createQuery(sbsql.toString());
						// MallaCurricularMateria mlcrmtAux1 =
						// (MallaCurricularMateria) q.getSingleResult();
						//
						// mlcrprAux.setMlcrprParalelo(prlAux);
						// mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmtAux1);
						// mlcrprAux.setMlcrprCupo(item.getMlcrprCupo());
						// mlcrprAux.setMlcrprInscritos(0);
						// if(item.getMlcrprReservaRepetidos()!=null){
						// mlcrprAux.setMlcrprReservaRepetidos(item.getMlcrprReservaRepetidos());
						// }else{
						// mlcrprAux.setMlcrprReservaRepetidos(0);
						// }
						// em.persist(mlcrprAux);
						// em.flush();
						// System.out.println("creada la materia
						// "+materia.getMtrDescripcion());
						// }
						// }
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux.setMlcrprParalelo(prlAux);
						mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmtAux);
						mlcrprAux.setMlcrprCupo(item.getMlcrprCupo());
						mlcrprAux.setMlcrprInscritos(0);
						if (item.getMlcrprReservaRepetidos() != null) {
							mlcrprAux.setMlcrprReservaRepetidos(item.getMlcrprReservaRepetidos());
						} else {
							mlcrprAux.setMlcrprReservaRepetidos(0);
						}
						mlcrprAux.setMlcrprNivelacionCrrId(entidad.getCrrId());
						em.persist(mlcrprAux);
						em.flush();

					}
				}
				retorno = true;
			} catch (Exception e) {
				throw new ParaleloException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.null.exception")));
		}
		return retorno;
	}

	// @SuppressWarnings("unchecked")
	// private List<Materia> listarTodosModulos(Integer mtrId) throws
	// MateriaNoEncontradoException , MateriaException {
	// List<Materia> retorno = null;
	// try {
	// StringBuffer sbsql = new StringBuffer();
	// sbsql.append(" Select mtr from Materia mtr ");
	// sbsql.append(" where mtr.mtrMateria.mtrId = :mtrId ");
	// sbsql.append(" and mtrEstado =
	// ");sbsql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
	// Query q = em.createQuery(sbsql.toString());
	// q.setParameter("mtrId", mtrId);
	// retorno = q.getResultList();
	// } catch (NoResultException e) {
	// throw new
	// MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new
	// MensajeGeneradorUtilidades("Materia.listar.todos.no.result.exception")));
	// } catch (Exception e) {
	// throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new
	// MensajeGeneradorUtilidades("Materia.listar.todos.exception")));
	// }
	// return retorno;
	// }

	/**
	 * Añade mallas curriculares asociadas al paraleloen la BD
	 * 
	 * @return Si se añadio o no el paralelo
	 * @throws ParaleloValidacionException
	 *             - Excepción lanzada en el caso de que no finalizó todas las
	 *             validaciones
	 * @throws ParaleloException
	 *             - Excepción general
	 */
	@Override
	public boolean anadirMallasAParalelo(ParaleloDto entidad, List<MallaCurricularDto> listaMallaCurricular)
			throws ParaleloValidacionException, ParaleloException {
		Boolean retorno = false;
		if (entidad != null && listaMallaCurricular.size() != 0) {
			try {
				Paralelo prlAux = em.find(Paralelo.class, entidad.getPrlId());
				for (MallaCurricularDto item : listaMallaCurricular) {
					if (item.isMtrAsignada()) {
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux.setMlcrprParalelo(prlAux);
						MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
						mlcrmtAux = em.find(MallaCurricularMateria.class, item.getMlcrmtId());
						mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmtAux);
						mlcrprAux.setMlcrprCupo(item.getMlcrprCupo());
						mlcrprAux.setMlcrprReservaRepetidos(item.getMlcrprReservaRepetidos());
						mlcrprAux.setMlcrprInscritos(0);
						mlcrprAux.setMlcrprNivelacionCrrId(prlAux.getPrlMallaCurricularParalelos().get(0).getMlcrprNivelacionCrrId());
 						em.persist(mlcrprAux);
						em.flush();
					}
				}
				retorno = true;
			} catch (Exception e) {
				throw new ParaleloException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.null.exception")));
		}
		return retorno;
	}

	/**
	 * Realiza verificaciones para determinar si existe una entidad con la misma
	 * descripcion
	 * 
	 * @param entidad
	 *            - entidad a validar antes de gestionar su edicion o insersion
	 * @throws ParaleloValidacionException
	 *             - Excepcion lanzada en el caso de que no finalizo todas las
	 *             validaciones
	 * @throws ParaleloException
	 *             - Excepcion lanzada no se controla el error
	 */
	@SuppressWarnings("unused")
	private Boolean verificarDescripcion(ParaleloDto entidad, int tipo) throws ParaleloValidacionException {
		Boolean retorno = false;
		// List<Paralelo> paraleloAux = new ArrayList<Paralelo>();

		try {
			// verifico que no exista otra entidad con la misma descripcion
			StringBuffer sbsql = new StringBuffer();

			sbsql.append(" Select prl from Paralelo prl ");
			sbsql.append(
					" where translate(prl.prlDescripcion,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:descripcion ");
			sbsql.append(" and prl.prlCarrera.crrId =:crrId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId =:pracId ");

			if (tipo == GeneralesConstantes.APP_EDITAR) {
				sbsql.append(" and prl.prlId <>:identificador ");
			}

			sbsql.append(" Order by prl.prlDescripcion ");

			Query q = em.createQuery(sbsql.toString());

			q.setMaxResults(1);
			q.setParameter("descripcion", entidad.getPrlDescripcion().toUpperCase());

			q.setParameter("crrId", entidad.getCrrId());
			q.setParameter("pracId", entidad.getPracId());

			if (tipo == GeneralesConstantes.APP_EDITAR) {
				q.setParameter("identificador", entidad.getPrlId());
			}

			// paraleloAux = q.getResultList();

			Paralelo paraleloAux = (Paralelo) q.getSingleResult();

			// if (paraleloAux.isEmpty()){
			// retorno = true;
			// }
			// else{
			// retorno = false;
			// }

		} catch (NoResultException e) {
			retorno = true;
		} catch (Exception e) {
			// TODO: Editar mensaje
			throw new ParaleloValidacionException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.verificar.descripcion.validacion.exception")));

		}

		return retorno;
	}

	/**
	 * Realiza verificaciones para determinar si existe una entidad Paralelo con
	 * el mismo Codigo
	 * 
	 * @param entidad
	 *            - entidad a validar antes de gestionar su edicion o insersion
	 * @throws ParaleloValidacionException
	 *             - Excepcion lanzada en el caso de que no finalizo todas las
	 *             validaciones
	 * @throws ParaleloException
	 *             - Excepcion lanzada no se controla el error
	 */
	@SuppressWarnings("unused")
	private Boolean verificarCodigo(ParaleloDto entidad, int tipo) throws ParaleloValidacionException {
		Boolean retorno = false;
		// List<Paralelo> paraleloAux = new ArrayList<Paralelo>();

		try {
			// verifico que no exista otra entidad con la misma descripcion
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prl from Paralelo prl ");
			sbsql.append(" where translate(prl.prlCodigo,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:codigo ");
			sbsql.append(" and prl.prlCarrera.crrId =:crrId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId =:pracId ");
			if (tipo == GeneralesConstantes.APP_EDITAR) {
				sbsql.append(" and prl.prlId <>:identificador ");
			}
			sbsql.append(" Order by prl.prlCodigo ");
			Query q = em.createQuery(sbsql.toString());
			q.setMaxResults(1);
			q.setParameter("codigo", entidad.getPrlCodigo().toUpperCase());
			q.setParameter("crrId", entidad.getCrrId());
			q.setParameter("pracId", entidad.getPracId());
			if (tipo == GeneralesConstantes.APP_EDITAR) {
				q.setParameter("identificador", entidad.getPrlId());
			}

			// paraleloAux = q.getResultList();
			Paralelo paraleloAux = (Paralelo) q.getSingleResult();

			// if (paraleloAux.isEmpty()){
			// retorno = true;
			// }
			// else{
			// retorno = false;
			// }

		} catch (NoResultException e) {
			retorno = true;
		} catch (Exception e) {
			// TODO: craer Paralelo.verificar.codigo.validacion.exception en el
			// mensajes.properties
			throw new ParaleloValidacionException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.verificar.codigo.validacion.exception")));

		}

		return retorno;
	}

	/**
	 * Permite desactivar el paralelo que se seleccionó
	 * 
	 * @param parametros
	 *            a editar
	 * @return True o False si fue eliminada la entidad paralelo existente en la
	 *         BD
	 * @throws ParaleloException
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloValidacionException
	 */
	@Override
	public Boolean desactivarPprlId(Integer prlId) throws ParaleloNoEncontradoException, ParaleloException {
		Boolean retorno = false;
		if (prlId != null) {
			try {
				Paralelo prlAux = em.find(Paralelo.class, prlId);
				prlAux.setPrlEstado(ParaleloConstantes.ESTADO_INACTIVO_VALUE);
				retorno = true;
			} catch (Exception e) {
				throw new ParaleloNoEncontradoException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.buscar.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.null.exception")));
		}
		return retorno;
	}

	/**
	 * Permite activar el paralelo que se seleccionó
	 * 
	 * @param parametros
	 *            a editar
	 * @return True o False si fue eliminada la entidad paralelo existente en la
	 *         BD
	 * @throws ParaleloException
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloValidacionException
	 */
	@Override
	public Boolean activarPprlId(Integer prlId) throws ParaleloNoEncontradoException, ParaleloException {
		Boolean retorno = false;
		if (prlId != null) {
			try {
				Paralelo prlAux = em.find(Paralelo.class, prlId);
				prlAux.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
				retorno = true;
			} catch (Exception e) {
				throw new ParaleloNoEncontradoException(
						MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.buscar.exception")));
			}
		} else {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.null.exception")));
		}
		return retorno;
	}

	/**
	 * Busca una entidad Paralelo por su id
	 * 
	 * @param id
	 *            - del Edificio a buscar
	 * @return Paralelo con el id solicitado
	 * @throws ParaleloNoEncontradoException
	 *             - Excepcion lanzada cuando no se encuentra un Edificio con el
	 *             id solicitado
	 * @throws ParaleloException
	 *             - Excepcion general
	 */

	@Override
	public Paralelo buscarPorId(Integer id) throws ParaleloNoEncontradoException, ParaleloException {
		Paralelo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Paralelo.class, id);
			} catch (NoResultException e) {
				// TODO
				throw new ParaleloNoEncontradoException(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("Paralelo.buscar.por.id.no.result.exception", id)));
			} catch (NonUniqueResultException e) {
				// TODO
				throw new ParaleloException(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Paralelo.buscar.por.id.non.unique.result.exception", id)));
			} catch (Exception e) {
				// TODO
				throw new ParaleloException(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("Paralelo.buscar.por.id.exception", id)));
			}
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public boolean eliminarMateriaDelParalelo(MateriaDto materiaDto, int paraleloId) throws ParaleloException {
		Boolean retorno = false;

		try {

			if (materiaDto.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
				List<MallaCurricularParalelo> modular = new ArrayList<>();
				Query sql = em.createNamedQuery("MallaCurricularParalelo.findPorMlcrmtIdParaleloId");
				sql.setParameter("mlcrmtId", materiaDto.getMlcrmtId());
				sql.setParameter("paraleloId", paraleloId);
			
				modular = sql.getResultList();
				if (!modular.isEmpty()) {
					for (MallaCurricularParalelo item : modular) {
						em.remove(item);
						em.flush();
					}
				}
				
				
				List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(materiaDto.getMtrId());
				if (!modulos.isEmpty()) {
					for (MateriaDto modulo : modulos) {
						List<MallaCurricularParalelo> mlcrprList = new ArrayList<>();
						Query sql1 = em.createNamedQuery("MallaCurricularParalelo.findPorMlcrmtIdParaleloId");
						sql1.setParameter("mlcrmtId", modulo.getMlcrmtId());
						sql1.setParameter("paraleloId", paraleloId);
					
						mlcrprList = sql1.getResultList();
						if (!mlcrprList.isEmpty()) {
							for (MallaCurricularParalelo item : mlcrprList) {
								em.remove(item);
								em.flush();
							}
						}
						
					}
					
				}
				

			}else {
				List<MallaCurricularParalelo> mlcrprList = new ArrayList<>();
				Query sql = em.createNamedQuery("MallaCurricularParalelo.findPorMlcrmtIdParaleloId");
				sql.setParameter("mlcrmtId", materiaDto.getMlcrmtId());
				sql.setParameter("paraleloId", paraleloId);
				mlcrprList = sql.getResultList();
				
				if (!mlcrprList.isEmpty()) {
					for (MallaCurricularParalelo item : mlcrprList) {
						em.remove(item);
						em.flush();
					}
				}
				
			}

			retorno = true;

		} catch (Exception e) {
			throw new ParaleloException("Error tipo SQL, verique los parámetros de entrada.");
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public boolean agregarMateriaAparalelo(ParaleloDto paraleloDto, MateriaDto materiaDto) throws ParaleloException {
		Boolean retorno = false;

		try {

			Paralelo paralelo = em.find(Paralelo.class, paraleloDto.getPrlId());

			MallaCurricularMateria mallaCurricularMateria = new MallaCurricularMateria();
			MallaCurricularParalelo mallaCurricularParalelo = new MallaCurricularParalelo();

			if (materiaDto.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
				mallaCurricularMateria = em.find(MallaCurricularMateria.class, materiaDto.getMlcrmtId());
				mallaCurricularParalelo.setMlcrprParalelo(paralelo);
				mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
				mallaCurricularParalelo.setMlcrprCupo(0);
				mallaCurricularParalelo.setMlcrprInscritos(0);
				mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());
				em.persist(mallaCurricularParalelo);
				em.flush();

				List<Materia> modulos = new ArrayList<>();
				Query sql = em.createNamedQuery("Materia.findModulosPorMateriaId");
				sql.setParameter("materiaId", materiaDto.getMtrId());
				modulos = sql.getResultList();

				for (Materia modulo : modulos) {
					mallaCurricularMateria = new MallaCurricularMateria();
					Query sql1 = em.createNamedQuery("MallaCurricularMateria.findPorMateriaId");
					sql1.setParameter("materiaId", modulo.getMtrId());
					mallaCurricularMateria = (MallaCurricularMateria) sql1.getSingleResult();

					mallaCurricularParalelo = new MallaCurricularParalelo();
					mallaCurricularParalelo.setMlcrprParalelo(paralelo);
					mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
					mallaCurricularParalelo.setMlcrprCupo(0);
					mallaCurricularParalelo.setMlcrprInscritos(0);
					mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());

					em.persist(mallaCurricularParalelo);
					em.flush();
				}

			}else {
				mallaCurricularMateria = em.find(MallaCurricularMateria.class, materiaDto.getMlcrmtId());
				mallaCurricularParalelo = new MallaCurricularParalelo();
				mallaCurricularParalelo.setMlcrprParalelo(paralelo);
				mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
				mallaCurricularParalelo.setMlcrprCupo(0);
				mallaCurricularParalelo.setMlcrprInscritos(0);
				mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());

				em.persist(mallaCurricularParalelo);
				em.flush();
			}

			retorno = true;

		} catch (Exception e) {
			throw new ParaleloException(
					MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
		}
		
		return retorno;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean crearParalelo(ParaleloDto paraleloDto, List<MateriaDto> materias) throws ParaleloException {
		Boolean retorno = false;

		try {

			Carrera carrera = em.find(Carrera.class, paraleloDto.getCrrId());
			PeriodoAcademico periodo = em.find(PeriodoAcademico.class, paraleloDto.getPracId());

			Paralelo paralelo = new Paralelo();
			paralelo.setPrlCarrera(carrera);
			paralelo.setPrlPeriodoAcademico(periodo);
			paralelo.setPrlCodigo(paraleloDto.getPrlCodigo());
			paralelo.setPrlDescripcion(paraleloDto.getPrlDescripcion());
			paralelo.setPrlFecha(paraleloDto.getPrlFecha());
			paralelo.setPrlInicioClase(paraleloDto.getPrlInicioClase());
			paralelo.setPrlFinClase(paraleloDto.getPrlFinClase());
			paralelo.setPrlEstado(ParaleloConstantes.ESTADO_INACTIVO_VALUE);
			em.persist(paralelo);
			em.flush();

			for (MateriaDto item : materias) {
				MallaCurricularMateria mallaCurricularMateria = new MallaCurricularMateria();
				MallaCurricularParalelo mallaCurricularParalelo = new MallaCurricularParalelo();
				
				if (item.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
					mallaCurricularMateria = em.find(MallaCurricularMateria.class, item.getMlcrmtId());
					mallaCurricularParalelo.setMlcrprParalelo(paralelo);
					mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
					mallaCurricularParalelo.setMlcrprCupo(0);
					mallaCurricularParalelo.setMlcrprInscritos(0);
					mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());
					em.persist(mallaCurricularParalelo);
					em.flush();
					
					List<Materia> modulos = new ArrayList<>();
					Query sql = em.createNamedQuery("Materia.findModulosPorMateriaId");
					sql.setParameter("materiaId", item.getMtrId());
					modulos = sql.getResultList();
					
					for (Materia modulo : modulos) {
						mallaCurricularMateria = new MallaCurricularMateria();
						Query sql1 = em.createNamedQuery("MallaCurricularMateria.findPorMateriaId");
						sql1.setParameter("materiaId", modulo.getMtrId());
						mallaCurricularMateria = (MallaCurricularMateria) sql1.getSingleResult();
						
						mallaCurricularParalelo = new MallaCurricularParalelo();
						mallaCurricularParalelo.setMlcrprParalelo(paralelo);
						mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
						mallaCurricularParalelo.setMlcrprCupo(0);
						mallaCurricularParalelo.setMlcrprInscritos(0);
						mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());

						em.persist(mallaCurricularParalelo);
						em.flush();
					}
					
				}else {
					mallaCurricularMateria = em.find(MallaCurricularMateria.class, item.getMlcrmtId());
					mallaCurricularParalelo = new MallaCurricularParalelo();
					mallaCurricularParalelo.setMlcrprParalelo(paralelo);
					mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
					mallaCurricularParalelo.setMlcrprCupo(0);
					mallaCurricularParalelo.setMlcrprInscritos(0);
					mallaCurricularParalelo.setMlcrprNivelacionCrrId(paraleloDto.getMlcrprNivelacionCrrId());
					mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());

					em.persist(mallaCurricularParalelo);
					em.flush();
				}
				
			}

			retorno = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ParaleloException(
					MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	public boolean editarParalelo(ParaleloDto entidad) throws ParaleloNoEncontradoException, ParaleloException {
		Boolean retorno = false;
		try {
			Paralelo paralelo = em.find(Paralelo.class, entidad.getPrlId());
			paralelo.setPrlEstado(entidad.getPrlEstado());
			em.merge(paralelo);
			em.flush();

			List<MallaCurricularParalelo> mallaCurPar = new ArrayList<>();
			Query sql = em.createNamedQuery("MallaCurricularParalelo.findPorParalelo");
			sql.setParameter("prlId", entidad.getPrlId());
			mallaCurPar = sql.getResultList();

			if (mallaCurPar != null && mallaCurPar.size() >0) {
				for (MallaCurricularParalelo item : mallaCurPar) {
					MallaCurricularParalelo malla = em.find(MallaCurricularParalelo.class, item.getMlcrprId());
					malla.setMlcrprModalidad(entidad.getMlcrprModalidad());
					em.merge(malla);
					em.flush();
				}
			}

			retorno = true;
		} catch (NoResultException e) {
			throw new ParaleloNoEncontradoException(
					MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.buscar.exception")));
		} catch (Exception e) {
			throw new ParaleloException(MensajeGeneradorUtilidades
					.getMsj(new MensajeGeneradorUtilidades("Paralelo.editar.null.exception")));
		}

		return retorno;
	}
	
	public boolean crearParaleloExoneracion(ParaleloDto paraleloDto, MateriaDto materiaDto, List<HoraClaseDto> horarios, PersonaDto docente) throws ParaleloException {
		boolean retorno = false;

		try {

			Carrera carrera = em.find(Carrera.class, paraleloDto.getCrrId());
			PeriodoAcademico periodo = em.find(PeriodoAcademico.class, paraleloDto.getPracId());

			Paralelo paralelo = new Paralelo();
			paralelo.setPrlCarrera(carrera);
			paralelo.setPrlPeriodoAcademico(periodo);
			paralelo.setPrlCodigo(paraleloDto.getPrlCodigo());
			paralelo.setPrlDescripcion(paraleloDto.getPrlDescripcion());
			paralelo.setPrlEstado(paraleloDto.getPrlEstado());
			paralelo.setPrlFecha(paraleloDto.getPrlFecha());
			em.persist(paralelo);
			em.flush();

			MallaCurricularMateria mallaCurricularMateria = new MallaCurricularMateria();
			MallaCurricularParalelo mallaCurricularParalelo = new MallaCurricularParalelo();
			mallaCurricularMateria = em.find(MallaCurricularMateria.class, materiaDto.getMlcrmtId());
			mallaCurricularParalelo = new MallaCurricularParalelo();
			mallaCurricularParalelo.setMlcrprParalelo(paralelo);
			mallaCurricularParalelo.setMlcrprMallaCurricularMateria(mallaCurricularMateria);
			mallaCurricularParalelo.setMlcrprInscritos(0);
			mallaCurricularParalelo.setMlcrprCupo(paraleloDto.getPrlCupo());
			mallaCurricularParalelo.setMlcrprModalidad(paraleloDto.getMlcrprModalidad());
			em.persist(mallaCurricularParalelo);
			em.flush();

			for (HoraClaseDto entidad : horarios) {
				try {

					Query sql = em.createNamedQuery("HorarioAcademico.findPorDiaHoInicioMlcrprId");
					sql.setParameter("dia", entidad.getHracDia());
					sql.setParameter("horaInicio", entidad.getHoclHoraInicio());
					sql.setParameter("mlcrprId", mallaCurricularParalelo.getMlcrprId());
					sql.getSingleResult();
				} catch (Exception e) {
					HorarioAcademico horario = new HorarioAcademico();
					horario.setHracDia(entidad.getHoclDiaValue());
					horario.setHracHoraInicio(Integer.valueOf(entidad.getHoclHoraInicio()));
					horario.setHracHoraFin(Integer.valueOf(entidad.getHoclHoraFin()));
					horario.setHracDescripcion(entidad.getHoclDescripcion());
					horario.setHracEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					horario.setHracHoraClaseAula(new HoraClaseAula(entidad.getHoclauId()));
					horario.setHracMallaCurricularParalelo(mallaCurricularParalelo);
					horario.setHracHoraTipo(GeneralesConstantes.APP_ID_BASE);
					em.persist(horario);
					em.flush();
				}
			}

			DetallePuesto dtps = em.find(DetallePuesto.class, docente.getDtpsId());

			CargaHoraria carga = new CargaHoraria();
			carga.setCrhrObservacion("HORAS CLASE");
			carga.setCrhrNumHoras(materiaDto.getMtrCreditos());
			carga.setCrhrEstado(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			carga.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
			carga.setCrhrDetallePuesto(dtps);
			carga.setCrhrMallaCurricularParalelo(mallaCurricularParalelo);
			carga.setCrhrPeriodoAcademico(periodo);

			em.persist(carga);
			em.flush();

			retorno = true;

		} catch (Exception e) {
			throw new ParaleloException(
					MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.añadir.exception")));
		}

		return retorno;
	}

}
