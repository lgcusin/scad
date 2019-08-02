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

 ARCHIVO:     DependenciaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla DEpendencia. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-Abril-2017           Gabriel Mafla 						Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;

/**
 * Clase (Bean)DependenciaServicioImpl.
 * Bean declarado como Stateless.
 * @author ghmafla
 * @version 1.0
 */

@Stateless
public class DependenciaServicioImpl implements DependenciaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Dependencia por su id
	 * @param id - del Dependencia a buscar
	 * @return Dependencia con el id solicitado
	 * @throws DependenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Dependencia con el id solicitado
	 * @throws DependenciaException - Excepcion general
	 */
	@Override
	public Dependencia buscarPorId(Integer id) throws DependenciaNoEncontradoException, DependenciaException {
		Dependencia retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Dependencia.class, id);
			} catch (NoResultException e) {
				throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new DependenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new DependenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.por.id.exception",id)));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las dependencias existentes en la BD
	 * @return lista de todas las dependencias existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<Dependencia> listarTodos() throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select dpn from Dependencia dpn ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.todos.no.result.exception")));
		}
		return retorno;

	}
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<Dependencia> listarFacultadesActivas(int jerarquiaId) throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select dpn from Dependencia dpn ");
		sbsql.append(" Where dpn.dpnJerarquia =:jerarquiaId");
		sbsql.append(" and  dpn.dpnEstado =:activo");
		sbsql.append(" order by dpnDescripcion");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("jerarquiaId", jerarquiaId);
		q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD
	 * @author Daniel
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Dependencia> listarFacultadesActivasPosgrado(int jerarquiaId) throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct dpn from Dependencia dpn, Carrera crr ");
		sbsql.append(" Where dpn.dpnJerarquia =:jerarquiaId");
		sbsql.append(" and  dpn.dpnEstado =:activo");
		sbsql.append(" and  crr.crrDependencia.dpnId = dpn.dpnId");
		sbsql.append(" and  crr.crrTipo = ");sbsql.append(CarreraConstantes.TIPO_POSGRADO_VALUE);
		sbsql.append(" order by dpn.dpnDescripcion");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("jerarquiaId", jerarquiaId);
		q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}
	
	@SuppressWarnings("unchecked")
	public List<Dependencia> buscarDependenciasPorCrrTipoUsrRol(int usuarioId, int tipoCrrId, int rolId) throws DependenciaNoEncontradoException{

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct dpn ");
		sbsql.append(" from Usuario usr, UsuarioRol usro, Rol rol, RolFlujoCarrera roflcr, Carrera crr, Dependencia dpn ");
		sbsql.append(" where usro.usroUsuario.usrId = usr.usrId ");
		sbsql.append(" and usro.usroRol.rolId = rol.rolId ");
		sbsql.append(" and roflcr.roflcrUsuarioRol.usroId = usro.usroId ");
		sbsql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
		sbsql.append(" and crr.crrDependencia.dpnId = dpn.dpnId ");
		sbsql.append(" and usr.usrId  = :usuarioId ");
		sbsql.append(" and crr.crrTipo  = :tipoCrrId");
		sbsql.append(" and usro.usroRol.rolId = :rolId");

		List<Dependencia> retorno = new ArrayList<>();
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("usuarioId", usuarioId);
		q.setParameter("tipoCrrId", tipoCrrId);
		q.setParameter("rolId", rolId);
		retorno = q.getResultList();

		if(retorno.isEmpty()){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}

		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Dependencia> listarFacultadesxUsuario(int usrId) throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct dpn ");
		sbsql.append(" from Usuario usr, UsuarioRol usro, Rol rol, RolFlujoCarrera roflcr, Carrera crr, Dependencia dpn ");
		sbsql.append(" where usro.usroUsuario.usrId = usr.usrId ");
		sbsql.append(" and usro.usroRol.rolId = rol.rolId ");
		sbsql.append(" and roflcr.roflcrUsuarioRol.usroId = usro.usroId ");
		sbsql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
		sbsql.append(" and crr.crrDependencia.dpnId = dpn.dpnId ");
		sbsql.append(" and usr.usrId  = :usrId ");
		sbsql.append(" and dpn.dpnEstado  = :activo ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("usrId", usrId);
		q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}
	
	@SuppressWarnings("unchecked")
	public List<Dependencia> listarFacultadesxUsuarioXDependencia(int usrId, int dpnId) throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct dpn ");
		sbsql.append(" from Usuario usr, UsuarioRol usro, Rol rol, RolFlujoCarrera roflcr, Carrera crr, Dependencia dpn ");
		sbsql.append(" where usro.usroUsuario.usrId = usr.usrId ");
		sbsql.append(" and usro.usroRol.rolId = rol.rolId ");
		sbsql.append(" and roflcr.roflcrUsuarioRol.usroId = usro.usroId ");
		sbsql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
		sbsql.append(" and crr.crrDependencia.dpnId = dpn.dpnId ");
		sbsql.append(" and usr.usrId  = :usrId ");
		sbsql.append(" and dpn.dpnEstado  = :activo ");
		sbsql.append(" and dpn.dpnId  = :dpnId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("usrId", usrId);
		q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
		q.setParameter("dpnId", dpnId);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}
	

	/**
	 * Lista la facultad de acuerdo a la carrera enviada
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	@Override
	public Dependencia buscarFacultadXcrrId(int crrId) throws DependenciaNoEncontradoException{
		Dependencia retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dpn from Dependencia dpn, Carrera crr ");
			sbsql.append(" join fetch dpn.dpnCarreras crr ");
			sbsql.append(" Where crr.crrId =:crrId");
			sbsql.append(" and  dpn.dpnEstado =:activo");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
			retorno = (Dependencia) q.getSingleResult();	
		} catch (Exception e) {
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultad.carrera.no.result.exception")));
		}
		
		return retorno;

	}
	
	/**
	 * Lista la facultad de acuerdo a la carrera enviada
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	@Override
	public Dependencia buscarDependenciaXcrrId(int crrId) throws DependenciaNoEncontradoException{
		Dependencia retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dpn from Dependencia dpn , Carrera crr ");
			sbsql.append(" Where crr.crrDependencia.dpnId = dpn.dpnId");
			sbsql.append(" and  dpn.dpnEstado =:activo");
			sbsql.append(" and  crr.crrId =:crrId");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
			retorno = (Dependencia) q.getSingleResult();	
		} catch (Exception e) {
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultad.carrera.no.result.exception")));
		}
		
		return retorno;

	}
	
	/**
	 * Lista la facultad de acuerdo a la carrera enviada
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	@Override
	public Dependencia buscarDependenciaXcrrIdSinException(int crrId){
		Dependencia retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select dpn from Dependencia dpn , Carrera crr ");
			sbsql.append(" Where crr.crrDependencia.dpnId = dpn.dpnId");
			sbsql.append(" and  dpn.dpnEstado =:activo");
			sbsql.append(" and  crr.crrId =:crrId");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
			retorno = (Dependencia) q.getSingleResult();	
		} catch (Exception e) {
		 //	throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultad.carrera.no.result.exception")));
		 return retorno;
		}
		
		return retorno;

	}
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 */
	@SuppressWarnings("unchecked")
	public List<Dependencia> listarFacultadesActivasXTipoCarrera(int jerarquiaId, int tipoCarrera) throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct dpn from Dependencia dpn, Carrera crr ");
		sbsql.append(" Where dpn.dpnId = crr.crrDependencia.dpnId ");
		sbsql.append(" and dpn.dpnJerarquia =:jerarquiaId");
		sbsql.append(" and  dpn.dpnEstado =:activo");
		sbsql.append(" and  crr.crrTipo =:tipoCarrera");
		sbsql.append(" order by dpn.dpnDescripcion");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("jerarquiaId", jerarquiaId);
		q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
		q.setParameter("tipoCarrera", tipoCarrera);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 */
	@SuppressWarnings("unchecked")
	public List<Dependencia> listarFacultadesActivasEvaluacionXTipoCarrera(int jerarquiaId, int tipoCarrera, int periodoId) throws DependenciaNoEncontradoException{
		List<Dependencia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct dpn from Dependencia dpn, Carrera crr, Paralelo prl, MallaCurricularParalelo mlcrpr, CargaHoraria crhr ");
		sbsql.append(" Where dpn.dpnId = crr.crrDependencia.dpnId ");
		sbsql.append(" and crr.crrId = prl.prlCarrera.crrId ");
		sbsql.append(" and prl.prlId = mlcrpr.mlcrprParalelo.prlId ");
		sbsql.append(" and mlcrpr.mlcrprId = crhr.crhrMallaCurricularParalelo.mlcrprId");
		sbsql.append(" and dpn.dpnJerarquia =:jerarquiaId");
		sbsql.append(" and  dpn.dpnEstado =:activo");
		sbsql.append(" and  crr.crrTipo =:tipoCarrera");
		sbsql.append(" and  prl.prlPeriodoAcademico.pracId =:periodoId");
		sbsql.append(" order by dpn.dpnDescripcion");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("jerarquiaId", jerarquiaId);
		q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
		q.setParameter("tipoCarrera", tipoCarrera);
		q.setParameter("periodoId", periodoId);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}
	
	@SuppressWarnings("unchecked")
	public List<Dependencia> buscarDependencias(int jerarquia, int tipoCarrera) throws DependenciaNoEncontradoException, DependenciaException{
		List<Dependencia> retorno = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append(" Select distinct dpn from Dependencia dpn, Carrera crr ");
		sql.append(" Where crr.crrDependencia.dpnId = dpn.dpnId ");
		sql.append(" and dpn.dpnJerarquia = :jerarquia ");
		sql.append(" and  crr.crrTipo = :tipoCarrera ");
		sql.append(" order by dpn.dpnDescripcion");
		
		try {
			Query q = em.createQuery(sql.toString());
			q.setParameter("jerarquia", jerarquia);
			q.setParameter("tipoCarrera", tipoCarrera);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new DependenciaException("Error al buscar dependencias, comuníquese con el administrador del sistema.");
		}
		
		if(retorno.isEmpty()){
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.facultades.activas.no.result.exception")));
		}
		
		return retorno;

	}
	
}
