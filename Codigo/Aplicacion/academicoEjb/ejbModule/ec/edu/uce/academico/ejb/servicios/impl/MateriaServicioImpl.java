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

 ARCHIVO:     MateriaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Materia. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CampoFormacion;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemicoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.TipoMateria;

/**
 * Clase (Bean)MateriaServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class MateriaServicioImpl implements MateriaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Materia por su id
	 * @param id - de la Materia a buscar
	 * @return Materia con el id solicitado
	 * @throws MateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Materia con el id solicitado
	 * @throws MateriaException - Excepcion general
	 */
	@Override
	public Materia buscarPorId(Integer id) throws MateriaNoEncontradoException, MateriaException {
		Materia retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Materia.class, id);
			} catch (NoResultException e) {
				throw new MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Materia existentes en la BD
	 * @return lista de todas las entidades Materia existentes en la BD
	 * @throws MateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Materia 
	 * @throws MateriaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarTodos() throws MateriaNoEncontradoException , MateriaException {
		List<Materia> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas las entidades Materia existentes en la BD que sean modulares
	 * @return lista de todas las entidades Materia existentes en la BD
	 * @throws MateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Materia 
	 * @throws MateriaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarTodosModularesXFacultad(Integer fclId) throws MateriaNoEncontradoException , MateriaException {
		List<Materia> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" select mtr from Materia mtr, MallaCurricular mlcr , Carrera crr , MallaCurricularMateria mlcrmt, Dependencia dpn ");
			sbsql.append(" where mtr.mtrId in(");
			sbsql.append("select mtra.mtrMateria.mtrId from Materia mtra where mtra.mtrMateria.mtrId is not null)");
			sbsql.append(" and mtr.mtrId=mlcrmt.mlcrmtMateria.mtrId and mlcrmt.mlcrmtMallaCurricular.mlcrId=mlcr.mlcrId ");
			sbsql.append(" and mlcr.mlcrCarrera.crrId=crr.crrId and crr.crrDependencia.dpnId=dpn.dpnId and dpn.dpnId  = :fclId");
			sbsql.append(" order by mtr.mtrDescripcion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fclId", fclId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws MateriaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException - Excepción general
	*/
	@Override
	public Boolean editar(MateriaDto entidad) throws MateriaValidacionException , MateriaException {
		Boolean retorno = false;
		if (entidad != null) {
			try {
				// verificar que no repita la descripcion y el código
				if (verificarDescripcion(entidad, GeneralesConstantes.APP_EDITAR)&& verificarCodigo(entidad, GeneralesConstantes.APP_EDITAR)) {
					// BUSCO LA MATERIA A MODIFICAR
					Materia materiaAux = em.find(Materia.class, entidad.getMtrId());
					// BUSCO OBJETOS DE RELACIONES
					TipoMateria tipoMateriaAux = em.find(TipoMateria.class, entidad.getTpmtId());
					CampoFormacion campoFormacionAux = em.find(CampoFormacion.class, entidad.getCmfrId());
					NucleoProblemicoCarrera nucleoProblemicoCarreraAux = em.find(NucleoProblemicoCarrera.class,	entidad.getNcprCrrId());
					// CAMPOS A GUARDARSE
					materiaAux.setMtrCodigo(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getMtrCodigo()).toUpperCase());
					materiaAux.setMtrDescripcion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getMtrDescripcion()).toUpperCase());
					materiaAux.setMtrEstado(entidad.getMtrEstado());
					materiaAux.setMtrHoras(entidad.getMtrHoras());
					   if(entidad.getMtrCreditos()==null){
						//materiaAux.setMtrCreditos(new BigDecimal(entidad.getMtrCreditos()));
							BigDecimal aux= null;
							materiaAux.setMtrCreditos(aux);
						}else{
							materiaAux.setMtrCreditos(new BigDecimal(entidad.getMtrCreditos()));	
							
						}
					materiaAux.setMtrHorasCien(entidad.getMtrHorasCien());
					materiaAux.setMtrUnidadMedida(entidad.getMtrUnidadMedida());
					materiaAux.setMtrRelacionTrabajo(entidad.getMtrRelacionTrabajo());
					materiaAux.setMtrHorasPracticas(new BigDecimal(entidad.getMtrHorasPracticas()));
					materiaAux.setMtrHorasTrabAutonomo(entidad.getMtrHorasTrabAutonomo());
					materiaAux.setMtrHorasPracSema(entidad.getMtrHorasPracSema());
					materiaAux.setMtrHorasAutonoSema(entidad.getMtrHorasAutonoSema());
					
					// RELACIONES A GUARDARSE
					materiaAux.setMtrTipoMateria(tipoMateriaAux);
					materiaAux.setmtrCampoFormacion(campoFormacionAux);
					materiaAux.setMtrNucleoProblemicoCarrera(nucleoProblemicoCarreraAux);

					// GUARDO EN CADA SUBMATERIAS LA MATERIA PADRE
//					if (subMateria != null) {
//						for (MateriaDto item : subMateria) {
//							Materia mtrAux = em.find(Materia.class, item.getMtrId());
//							if (mtrAux.getMtrId() != 0) {
//								mtrAux.setMtrMateria(materiaAux);
//							}
//						}
//					}
//					// ELIMINO DE LAS SUBMATERIAS LA MATERIA PADRE
//					if (subMateriaEliminar != null) {
//						for (MateriaDto item : subMateriaEliminar) {
//							Materia mtrAux = em.find(Materia.class, item.getMtrId());
//							if (mtrAux.getMtrId() != 0) {
//								mtrAux.setMtrMateria(null);
//							}
//						}
//					}
					retorno = true; // retorno verdadero si se realiza la actualizacion correctamente
				} else {
					throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.editar.descripcion.codigo.duplicado.exception")));
				}
			} catch (MateriaValidacionException e) {
				throw new MateriaValidacionException(e.getMessage());
			} catch (Exception e) {
				throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.editar.exception")));
			}
		} else {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.editar.null.exception")));
		}
		return retorno;
	}
	
	
	
	/**
	 * Añade una materia en la BD
	 * @return Si se añadio o no la materia
	 * @throws MateriaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws MateriaException - Excepción general
	 */
	@Override	
	public boolean anadir(MateriaDto entidad) throws MateriaValidacionException, MateriaException {
		Boolean retorno = false;
		if (entidad != null) {
			try {
				if (verificarDescripcion(entidad, GeneralesConstantes.APP_NUEVO)
						&& verificarCodigo(entidad, GeneralesConstantes.APP_NUEVO)) {
					// BUSCO LA MATERIA A MODIFICAR
					Materia materiaAux = new Materia();
					// BUSCO OBJETOS DE RELACIONES
					Carrera carreraAux = em.find(Carrera.class, entidad.getCrrId());
					TipoMateria tipoMateriaAux = em.find(TipoMateria.class, entidad.getTpmtId());
					CampoFormacion campoFormacionAux = em.find(CampoFormacion.class, entidad.getCmfrId());
					
					/// CAMPOS A GUARDARSE
					materiaAux.setMtrCodigo(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getMtrCodigo()).toUpperCase());
					materiaAux.setMtrDescripcion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getMtrDescripcion()).toUpperCase());
					materiaAux.setMtrEstado(entidad.getMtrEstado());
					materiaAux.setMtrHoras(entidad.getMtrHoras());
					if(entidad.getMtrCreditos()==null){
					//materiaAux.setMtrCreditos(new BigDecimal(entidad.getMtrCreditos()));
						BigDecimal aux= null;
						materiaAux.setMtrCreditos(aux);
					}else{
						materiaAux.setMtrCreditos(new BigDecimal(entidad.getMtrCreditos()));	
						
					}
						
					materiaAux.setMtrHorasCien(entidad.getMtrHorasCien());
					materiaAux.setMtrUnidadMedida(entidad.getMtrUnidadMedida());
					materiaAux.setMtrRelacionTrabajo(entidad.getMtrRelacionTrabajo());
					materiaAux.setMtrHorasPracticas(new BigDecimal(entidad.getMtrHorasPracticas()));
					materiaAux.setMtrHorasTrabAutonomo(entidad.getMtrHorasTrabAutonomo());
					materiaAux.setMtrHorasPracSema(entidad.getMtrHorasPracSema());
					materiaAux.setMtrHorasAutonoSema(entidad.getMtrHorasAutonoSema());
					
					// RELACIONES A GUARDARSE
					materiaAux.setMtrTipoMateria(tipoMateriaAux);
					materiaAux.setmtrCampoFormacion(campoFormacionAux);
					if(entidad.getNcprCrrId() != null){
						NucleoProblemicoCarrera nucleoProblemicoCarreraAux = em.find(NucleoProblemicoCarrera.class,entidad.getNcprCrrId());
						materiaAux.setMtrNucleoProblemicoCarrera(nucleoProblemicoCarreraAux);
					}
					
					materiaAux.setMtrCarrera(carreraAux);
					// GUARDO EN CADA SUBMATERIAS LA MATERIA PADRE
					em.persist(materiaAux);
					retorno = true;
				} else {
					throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.anadir.descripcion.codigo.duplicado.exception")));
				}
			} catch (MateriaValidacionException e) {
				throw new MateriaValidacionException(e.getMessage());
			} catch (Exception e) {
				throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.anadir.exception")));
			}
		} else {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.anadir.null.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Realiza verificaciones para determinar si existe una entidad Materia con la misma descripcion
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @throws MateriaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException  - Excepcion lanzada no se controla el error
	 */
	@SuppressWarnings("unused")
	private Boolean verificarDescripcion(MateriaDto entidad, int tipo)
			throws MateriaValidacionException, MateriaException {
		Boolean retorno = false;
		try {
			// verifico que no exista otra entidad con la misma descripción
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			sbsql.append(" where translate(mtr.mtrDescripcion,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:descripcion ");
			sbsql.append(" and mtr.mtrCarrera.crrId =:crrId ");
			if (tipo == GeneralesConstantes.APP_EDITAR) {
				sbsql.append(" and mtr.mtrId <>:identificador ");
			}
			Query q = em.createQuery(sbsql.toString());
			q.setMaxResults(1);
			q.setParameter("descripcion", entidad.getMtrDescripcion().toUpperCase());
			q.setParameter("crrId", entidad.getCrrId());
			if (tipo == GeneralesConstantes.APP_EDITAR) {
				q.setParameter("identificador", entidad.getMtrId());
			}
			Materia materiaAux = (Materia) q.getSingleResult();
		} catch (NoResultException e) {
			retorno = true;
		} catch (NonUniqueResultException e) {
			throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.descripcion.no.unico.exception")));
		} catch (Exception e) {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.descripcion.validacion.exception")));

		}

		return retorno;
	}
	
	
	
	/**
	 * Realiza verificaciones para determinar si existe una entidad Materia con el mismo Codigo
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @throws MateriaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException  - Excepcion lanzada no se controla el error
	 */
	@SuppressWarnings("unused")
	private Boolean verificarCodigo(MateriaDto entidad, int tipo) throws MateriaValidacionException, MateriaException {
		Boolean retorno = false;
		try {
			// verifico que no exista otra entidad con la mismo Código
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			sbsql.append(" where translate(mtr.mtrCodigo,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:codigo ");
			sbsql.append(" and mtr.mtrCarrera.crrId =:crrId ");
			if (tipo == GeneralesConstantes.APP_EDITAR) {
				sbsql.append(" and mtr.mtrId <>:identificador ");
			}
			Query q = em.createQuery(sbsql.toString());
			q.setMaxResults(1);
			q.setParameter("codigo", entidad.getMtrCodigo().toUpperCase());
			q.setParameter("crrId", entidad.getCrrId());
			if (tipo == GeneralesConstantes.APP_EDITAR) {
				q.setParameter("identificador", entidad.getMtrId());
			}
			Materia materiaAux = (Materia) q.getSingleResult();
		} catch (NoResultException e) {
			retorno = true;
		} catch (NonUniqueResultException e) {
			throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.no.unico.exception")));
		}
		catch (Exception e) {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.validacion.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca la materia por codigo y carrera
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param codigo - codigo de materia a buscar
	 * @param carrera - id de carrera SAU a buscar 
	 * @throws MateriaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException  - Excepcion lanzada no se controla el error
	 */
	public Materia buscarXCodigoXEspeCodigo(String codigo, int carrera) throws MateriaValidacionException, MateriaException {
		
		Materia materiaAux = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			sbsql.append(" where translate(mtr.mtrCodigo,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:codigo ");
			sbsql.append(" and mtr.mtrCarrera.crrEspeCodigo =:crrId ");
			sbsql.append(" and mtr.mtrEstado =:estado ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setMaxResults(1); 
			q.setParameter("codigo", codigo.toUpperCase().replace(" ", ""));
			q.setParameter("crrId", carrera);
			q.setParameter("estado", MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);

			materiaAux = (Materia) q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.no.unico.exception")));
		} catch (Exception e) {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.validacion.exception")));
		}
		return materiaAux;
	}
	
	
	/**
	 * Busca la materia por codigo y carrera
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param codigo - codigo de materia a buscar
	 * @param carrera - id de carrera SIIU a buscar 
	 * @throws MateriaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException  - Excepcion lanzada no se controla el error
	 */
	public Materia buscarXCodigoXCarrera(String codigo, int carrera) throws MateriaValidacionException, MateriaException {
		
		Materia materiaAux = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			sbsql.append(" where translate(mtr.mtrCodigo,'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ','aeiouAEIOUaeiouAEIOU') =:codigo ");
			sbsql.append(" and mtr.mtrCarrera.crrId =:crrId ");
			sbsql.append(" and mtr.mtrEstado =:estado ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setMaxResults(1); 
			q.setParameter("codigo", codigo.toUpperCase().replace(" ", ""));
			q.setParameter("crrId", carrera);
			q.setParameter("estado", MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);

			materiaAux = (Materia) q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new MateriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.no.unico.exception")));
		} catch (Exception e) {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.verificar.codigo.validacion.exception")));
		}
		return materiaAux;
	}
	
	
	/**
	 * Lista todas las entidades Materia existentes en la BD
	 * @return lista de todas las entidades Materia existentes en la BD
	 * @throws MateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Materia 
	 * @throws MateriaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarTodosModulos(Integer mtrId) throws MateriaNoEncontradoException , MateriaException {
		List<Materia> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			sbsql.append(" where mtr.mtrMateria.mtrId = :mtrId ");
			sbsql.append(" and mtrEstado = ");sbsql.append(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("mtrId", mtrId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.exception")));
		}
		return retorno;
	}
	
	@Override
	public Materia buscarMateriaXMlCrPrPeriodoPregradoActivo(Integer mlcrpr) throws MateriaNoEncontradoException , MateriaException {
		Materia retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr, MallaCurricularMateria mlcrmt, MallaCurricularParalelo mlcrpr, Paralelo prl, PeriodoAcademico prac ");
			sbsql.append(" where mtr.mtrId = mlcrmt.mlcrmtMateria.mtrId ");
			sbsql.append(" and mlcrmt.mlcrmtId = mlcrpr.mlcrprMallaCurricularMateria.mlcrmtId ");
			sbsql.append(" and mlcrpr.mlcrprParalelo.prlId = prl.prlId ");
			sbsql.append(" and prl.prlPeriodoAcademico.pracId =  prac.pracId");
			sbsql.append(" and (prac.pracEstado = ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			sbsql.append(" or prac.pracEstado = ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);sbsql.append(" ) ");
			sbsql.append(" and prac.pracTipo = ");sbsql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			sbsql.append(" and mlcrpr.mlcrprId = :mlcrpr ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("mlcrpr", mlcrpr);
			retorno = (Materia) q.getSingleResult();
		} catch (NoResultException e) {
			throw new MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	public boolean editarMateriaPorGrupo(Materia entidad) throws  MateriaException {
		boolean retorno = false;
		
			try {
				Materia materia = em.find(Materia.class, entidad.getMtrId());
				Grupo grupo = em.find(Grupo.class, entidad.getMtrGrupo().getGrpId());
				materia.setMtrGrupo(grupo);
				
				em.merge(materia);
				em.flush();
				retorno = true;
			} catch (Exception e) {
				throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.editar.exception")));
			}
			
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public List<Materia> buscarMateriasPorCarrera(int carreraId) throws MateriaNoEncontradoException , MateriaException {
		List<Materia> retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mtr from Materia mtr ");
			sbsql.append(" where mtr.mtrCarrera.crrId = :carreraId ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("carreraId", carreraId);
			retorno = q.getResultList();
			
		} catch (NoResultException e) {
			throw new MateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new MateriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.listar.todos.exception")));
		}
		
		return retorno;
	}
	
	
}






