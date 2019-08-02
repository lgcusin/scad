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

 ARCHIVO:     RolFlujoCarreraServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Rol_Flujo_Carrera. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 18-JULIO-2017           Gabriel Mafla                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.DocenteTHJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;
import ec.edu.uce.academico.jpa.entidades.publico.RelacionLaboral;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

/**
 * Clase (Bean)RolFlujoCarreraServicioImpl.
 * Bean declarado como Stateless.
 * @author ghmafla
 * @version 1.0
 */

@Stateless
public class RolFlujoCarreraServicioImpl implements RolFlujoCarreraServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@EJB
	private FichaDocenteServicio servFichaDocenteServicio;
	@EJB
	private DetallePuestoServicio servDetallePuestoServicio;
	@EJB
	private PeriodoAcademicoServicio servPeriodoAcademicoServicio; 
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	@Override
	public RolFlujoCarrera buscarPorId(Integer id) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException {
		RolFlujoCarrera retorno = null;
		if (id != null) {
			try {
				retorno = em.find(RolFlujoCarrera.class, id);
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades RolFlujoCarrera existentes en la BD
	 * @return lista de todas las entidades RolFlujoCarrera existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> listarTodos() throws RolFlujoCarreraNoEncontradoException{
		List<RolFlujoCarrera> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	

	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@Override
	public RolFlujoCarrera buscarPorCarrera(Integer carrera, Integer usuarioId, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		RolFlujoCarrera retorno = null;
		if(carrera != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrCarrera.crrId = :carrera");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", carrera );
				q.setParameter("rolId", rolId );
				q.setParameter("usuarioId", usuarioId );
				retorno = (RolFlujoCarrera)q.getSingleResult();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.no.result.exception",carrera)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.non.unique.result.exception",carrera)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.exception")));
			}
		}
		return retorno;
	}
	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@Override
	public RolFlujoCarrera buscarPorCarreraXRol(Integer carrera, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		RolFlujoCarrera retorno = null;
		if(carrera != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrCarrera.crrId = :carrera");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", carrera );
				q.setParameter("rolId", rolId );
				retorno = (RolFlujoCarrera)q.getSingleResult();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.no.result.exception",carrera)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.non.unique.result.exception",carrera)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.exception")));
			}
		}
		return retorno;
	}
	
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de usuario
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> buscarPorIdUsuario(Usuario usuario) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		List<RolFlujoCarrera> retorno = null;
		if(usuario != null){
			try{
				retorno=new ArrayList<RolFlujoCarrera>();
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("usuarioId", usuario.getUsrId() );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.IdUsuario.no.result.exception",usuario.getUsrId())));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.IdUsuario.non.unique.result.exception",usuario.getUsrId())));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.IdUsuario.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad RolFlujoCarrera por idUsuario y idRol 
	 * @param usuarioId - de Usuario a buscar
	 * @param rolId - de Rol a buscar
	 * @return RolFlujoCarrera solicitado
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> buscarXRolXUsuarioId(Integer usuarioId, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		List<RolFlujoCarrera> retorno = null;
		if(usuarioId != null && rolId != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				sbsql.append(" and roflcr.roflcrEstado = :roflcrEstado ");
				Query q = em.createQuery(sbsql.toString()); 
				q.setParameter("rolId", rolId );
				q.setParameter("usuarioId", usuarioId );
				q.setParameter("roflcrEstado", RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.no.result.exception",usuarioId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.exception")));
			}
		}
		return retorno;
	}
	
	
	/**
	 * Desactiva el rol_flujo_carrera de acuerdo al roflcr_id
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	@Override
	public boolean desactivarRolFlujoCarreraXUsuarioRol(Integer roflcrId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException{
		boolean retorno = false;
		if(roflcrId != 0){
			try{
				RolFlujoCarrera roflcrAux = em.find(RolFlujoCarrera.class, roflcrId);
				if(roflcrAux.getRoflcrEstado()==RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE){
					roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				}else{
					roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE);	
				}
				//merge si no exite crea  si existe actualiza
				em.merge(roflcrAux);
				// vaciar entitymanager
				em.flush();
				retorno=true;
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.no.result.exception",roflcrId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	
	/**
	 * Activa el rol_flujo_carrera de acuerdo al roflcr_id
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	@Override
	public boolean activarRolFlujoCarreraXUsuarioRol(Integer roflcrId, Integer rolId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException,
		RolFlujoCarreraValidacionException{
		boolean retorno = false;
		if(roflcrId != 0){
			RolFlujoCarrera roflcrActivo = null;
			try{
				RolFlujoCarrera roflcrAux = em.find(RolFlujoCarrera.class, roflcrId);
				
				try {
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
					sbsql.append(" where roflcr.roflcrCarrera.crrId = :crrId");
					sbsql.append(" and roflcr.roflcrEstado = ");sbsql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId");
					
					Query q = em.createQuery(sbsql.toString());
					q.setParameter("crrId", roflcrAux.getRoflcrCarrera().getCrrId() );
					q.setParameter("rolId", rolId );
					roflcrActivo = (RolFlujoCarrera) q.getSingleResult();
					if(roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick()!=null){
						
						throw new RolFlujoCarreraValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.usuario.activo.por.carrera",roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick())));
					}
				} catch (NoResultException e) {
					//En caso de no existir usuario en la carrera seleccionada con el mismo rol
					roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					em.merge(roflcrAux);
					// vaciar entitymanager
					em.flush();
				}
				retorno=true;
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.no.result.exception",roflcrId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.usuario.activo.por.carrera",roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick())));
			}
		}
		return retorno;
	}

	@Override
	public boolean desactivarRolFlujoCarrerasXListaUsuarioRol(Integer usroId)
			throws RolFlujoCarreraException,
			RolFlujoCarreraNoEncontradoException {
		boolean retorno = false;
		if(usroId != 0){
				try {
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
					sbsql.append(" where roflcr.roflcrUsuarioRol.usroId = :usroId");
					sbsql.append(" and roflcr.roflcrEstado = ");sbsql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					Query q = em.createQuery(sbsql.toString());
					q.setParameter("usroId", usroId );
					@SuppressWarnings("unchecked")
					List<RolFlujoCarrera> listaRolFlujoCarrera = q.getResultList();
					if(listaRolFlujoCarrera==null){
					}else{
						for (RolFlujoCarrera item : listaRolFlujoCarrera) {
							RolFlujoCarrera aux = em.find(RolFlujoCarrera.class, item.getRoflcrId());
							aux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE);
							em.merge(aux);
							// vaciar entitymanager
							em.flush();
						}
					}
				} catch (NoResultException e) {
				}
				retorno=true;
		}
		return retorno;
	}
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws UsuarioValidacionException 
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	@SuppressWarnings("unchecked")
	public boolean anadirUsuarioRolFlujoCarrera(UsuarioCreacionDto entidad, Integer rolId, Integer carreraId, Integer dpnId, Integer tipoCarrera, Usuario usuarioRegistro, DocenteTHJdbcDto docenteTH, PeriodoAcademicoDto periodoPosgrado, Puesto puesto, Integer relacionLaboral) throws PersonaValidacionException, PersonaException, UsuarioValidacionException{
		boolean retorno = false;
			try {
				//*************************************************************
				//********************* VALIDACIONES **************************
				//*************************************************************
				PeriodoAcademico pracAuxActivo = new PeriodoAcademico();
				if(tipoCarrera == CarreraConstantes.TIPO_PREGRADO_VALUE || tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
					//busco período activo
					pracAuxActivo = servPeriodoAcademicoServicio.buscarPeriodo(CarreraConstantes.TIPO_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				}
				
				//creacion del objeto usuario_rol
				Rol rol = em.find(Rol.class, rolId);
				//*************************************************************
				//************************** INSERCIONES **********************
				//*************************************************************
				//*********      Insercion de usuariorol
				//asignacion del usuario al usuario_rol
					UsuarioRol usuarioRol = new UsuarioRol();
					try {
						StringBuilder sbSql1 = new StringBuilder();
						sbSql1.append(" Select usro from UsuarioRol usro where ");
						sbSql1.append(" usro.usroUsuario.usrId = :usrId ");
						sbSql1.append(" and usro.usroRol.rolId = :rolId ");
						Query q = em.createQuery(sbSql1.toString());
						q.setParameter("usrId",entidad.getUsrId());
						q.setParameter("rolId",rolId);
						usuarioRol = (UsuarioRol) q.getSingleResult();
					} catch (NoResultException e) {
						usuarioRol = new UsuarioRol();
						usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
						usuarioRol.setUsroRol(rol);
						Usuario usuario = em.find(Usuario.class, entidad.getUsrId());
						usuarioRol.setUsroUsuario(usuario);
						em.persist(usuarioRol);
						em.flush();	
					}
					
					List<Carrera> listaCarrerasExistentes = new ArrayList<Carrera>();
					StringBuilder sbSqlCr = new StringBuilder();
					sbSqlCr.append(" Select roflcr from RolFlujoCarrera roflcr where ");
					sbSqlCr.append(" roflcr.roflcrCarrera.crrId = :carreraId ");
					sbSqlCr.append(" and roflcr.roflcrUsuarioRol.usroId = :usroId ");
					Query qCrr = em.createQuery(sbSqlCr.toString());
					qCrr.setParameter("carreraId",carreraId);
					qCrr.setParameter("usroId",usuarioRol.getUsroId());
					listaCarrerasExistentes = qCrr.getResultList();
					int carreraExistente = 0;
					for (Carrera item : listaCarrerasExistentes) {
						if(item.getCrrId() != 0){
							carreraExistente = item.getCrrId();
						}
					}
					
				if(rolId==RolConstantes.ROL_ADMINFACULTAD_VALUE
						||rolId==RolConstantes.ROL_ADMINNIVELACION_VALUE
						||rolId==RolConstantes.ROL_SECREABOGADO_VALUE
						||rolId==RolConstantes.ROL_SECRESECREABOGADO_VALUE
						||rolId==RolConstantes.ROL_DECANO_VALUE
						||rolId==RolConstantes.ROL_SUBDECANO_VALUE
						||rolId==RolConstantes.ROL_CONSULTOREPORTES_VALUE
						||rolId==RolConstantes.ROL_INFORMATICOFACULTAD_VALUE
						||rolId==RolConstantes.ROL_DIRPOSGRADO_VALUE) {
//						||rolId==RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE) {
					
					
					List<RolFlujoCarrera> listaRoflcrExistentes = new ArrayList<RolFlujoCarrera>();
					StringBuilder sbSqlCrAux = new StringBuilder();
					sbSqlCrAux.append(" Select roflcr from RolFlujoCarrera roflcr, Carrera crr where ");
					sbSqlCrAux.append(" crr.crrId = roflcr.roflcrCarrera.crrId ");
					sbSqlCrAux.append(" and roflcr.roflcrCarrera.crrDependencia.dpnId = :dpnId ");
					sbSqlCrAux.append(" and roflcr.roflcrUsuarioRol.usroId = :usroId ");
					Query qCrrAux = em.createQuery(sbSqlCrAux.toString());
					qCrrAux.setParameter("dpnId",dpnId);
					qCrrAux.setParameter("usroId",usuarioRol.getUsroId());
					listaRoflcrExistentes = qCrrAux.getResultList();
					
//					int carreraExistente = 0;
//					for (Carrera item : listaCarrerasExistentes) {
//						if(item.getCrrId() != 0){
//							carreraExistente = item.getCrrId();
//						}
//					}
					
					List<Carrera> listaCarreras = new ArrayList<Carrera>();
					
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" Select crr from Carrera crr where ");
					sbSql.append(" crr.crrDependencia.dpnId = :dpnId ");
					
					Query q = em.createQuery(sbSql.toString());
					q.setParameter("dpnId",dpnId);
					listaCarreras = q.getResultList();
					boolean asignar = true;
					List<Carrera> carreraAsignar = new ArrayList<>();
					for (Carrera carreraAux : listaCarreras) {
						asignar = true;
						for (RolFlujoCarrera roflcrAuxAux : listaRoflcrExistentes) {
							if(carreraAux.getCrrId() == roflcrAuxAux.getRoflcrCarrera().getCrrId()){
								asignar = false;
							}
						}
						if(asignar){
							System.out.println(carreraAux.getCrrDescripcion());
							carreraAsignar.add(carreraAux);
						}
					}
					
//					for (Carrera item : listaCarreras) {
//						RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
//						Carrera roflcrCarrera = em.find(Carrera.class, item.getCrrId());
//						roflcrAux.setRoflcrCarrera(roflcrCarrera);
//						roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
//						roflcrAux.setRoflcrUsuarioRol(usuarioRol);
//						em.persist(roflcrAux);
//						em.flush();
//					}
					
					for (Carrera item : carreraAsignar) {
						RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
						Carrera roflcrCarrera = em.find(Carrera.class, item.getCrrId());
						roflcrAux.setRoflcrCarrera(roflcrCarrera);
						roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
						roflcrAux.setRoflcrUsuarioRol(usuarioRol);
						em.persist(roflcrAux);
						em.flush();
					}
					
					retorno =true;
				}else if(rolId==RolConstantes.ROL_SECRECARRERA_VALUE
						||rolId==RolConstantes.ROL_DOCENTE_VALUE
						||rolId==RolConstantes.ROL_DIRCARRERA_VALUE
						||rolId==RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE
						||rolId==RolConstantes.ROL_SECREPOSGRADO_VALUE
						||rolId==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE //){
						||rolId==RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE) {
					RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
					Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
					if(carreraExistente != carreraId){
						roflcrAux.setRoflcrCarrera(roflcrCarrera);
						roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
						roflcrAux.setRoflcrUsuarioRol(usuarioRol);
						em.persist(roflcrAux);
						em.flush();
						if(rolId==RolConstantes.ROL_DOCENTE_VALUE){
							FichaDocente fcdcAux = null;
							Persona prsAux = em.find(Persona.class, entidad.getPrsId());
							try {
								fcdcAux = servFichaDocenteServicio.buscarPorPrsId(entidad.getPrsId());	
								if(fcdcAux != null){
									DetallePuesto dtpsAux = null;
									try {
										if(tipoCarrera == CarreraConstantes.TIPO_POSGRADO_VALUE || tipoCarrera == CarreraConstantes.TIPO_SUFICIENCIA_VALUE || tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
											if(tipoCarrera == CarreraConstantes.TIPO_POSGRADO_VALUE){
												dtpsAux = servDetallePuestoServicio.buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioPosgrado(fcdcAux.getFcdcId(),tipoCarrera, roflcrCarrera.getCrrId());
												retorno = true;
											}else if(tipoCarrera == CarreraConstantes.TIPO_SUFICIENCIA_VALUE || tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
//												dtpsAux = servDetallePuestoServicio.buscarPorFcdcIdPostgradoUotro(fcdcAux.getFcdcId(),roflcrCarrera.getCrrId());
//												dtpsAux = servDetallePuestoServicio.buscarPorFcdcIdXIdCarreraXTipoCarreraHonorario(fcdcAux.getFcdcId(),tipoCarrera);
												dtpsAux = servDetallePuestoServicio.buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioSuficiencias(fcdcAux.getFcdcId(),tipoCarrera, roflcrCarrera.getCrrDependencia().getDpnId());
												retorno = true;
											}

										}else if(tipoCarrera == CarreraConstantes.TIPO_PREGRADO_VALUE){
//											dtpsAux = servDetallePuestoServicio.buscarPorFcdcId(fcdcAux.getFcdcId(), tipoCarrera);
											dtpsAux = servDetallePuestoServicio.buscarPorFcdcIdVerifica(fcdcAux.getFcdcId(), tipoCarrera);
											retorno = true;
										}
									} catch (DetallePuestoNoEncontradoException e) {
										if(tipoCarrera == CarreraConstantes.TIPO_POSGRADO_VALUE || tipoCarrera == CarreraConstantes.TIPO_SUFICIENCIA_VALUE || tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
											dtpsAux = new DetallePuesto();
											dtpsAux.setDtpsFichaDocente(fcdcAux);
											dtpsAux.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
																						
											dtpsAux.setDtpsPracId(pracAuxActivo.getPracId());
											if(tipoCarrera == CarreraConstantes.TIPO_POSGRADO_VALUE){
												dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_POSGRADO_VALUE);
												dtpsAux.setDtpsPracId(periodoPosgrado.getPracId());
												
											}
											if(tipoCarrera == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
												dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
												//periodo activo
												PeriodoAcademico periodoSuficiencia = new PeriodoAcademico();
												if(roflcrCarrera.getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE){
													periodoSuficiencia = servPeriodoAcademicoServicio.buscarPeriodoXTipoXEstado(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
													if(periodoSuficiencia != null){
														dtpsAux.setDtpsPracId(periodoSuficiencia.getPracId());
													}
												}else if(roflcrCarrera.getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE){
													periodoSuficiencia = servPeriodoAcademicoServicio.buscarPeriodoXTipoXEstado(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
													if(periodoSuficiencia != null){
														dtpsAux.setDtpsPracId(periodoSuficiencia.getPracId());
													}
												}else if(roflcrCarrera.getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE){
													periodoSuficiencia = servPeriodoAcademicoServicio.buscarPeriodoXTipoXEstado(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
													if(periodoSuficiencia != null){
														dtpsAux.setDtpsPracId(periodoSuficiencia.getPracId());
													}
												}
											}
											if(tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
												dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_NIVELEACION_VALUE);
												
											}
											dtpsAux.setDtpsFechaRegistro(new Timestamp((new Date()).getTime()));
											dtpsAux.setDtpsUsuario(usuarioRegistro.getUsrNick().toString());
											dtpsAux.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE);
											
//											dtpsAux.setDtpsPracId(pracAuxActivo.getPracId());
											
											//aquí agregar la categoria y tiempo de dedicacion de la base de talento humano
//											docenteTH
											
											
											Puesto puestoAux = em.find(Puesto.class, 10);
											RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 2);
											dtpsAux.setDtpsRelacionLaboral(rllbAux);
											dtpsAux.setDtpsPuesto(puestoAux);
											dtpsAux.setDtpsCarrera(roflcrCarrera);
											em.persist(dtpsAux);
											em.flush();
											retorno = true;
										}else if(tipoCarrera == CarreraConstantes.TIPO_PREGRADO_VALUE){
											dtpsAux = new DetallePuesto();
											dtpsAux.setDtpsFichaDocente(fcdcAux);
											dtpsAux.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
											
											//aquí agregar la categoria y tiempo de dedicacion de la base de talento humano
//											docenteTH
											
//											Puesto puestoAux = em.find(Puesto.class, 15);
//											RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 1);
//											dtpsAux.setDtpsRelacionLaboral(rllbAux);
//											dtpsAux.setDtpsPuesto(puestoAux);
//											dtpsAux.setDtpsRelacionLaboral(rllbAux);
//											dtpsAux.setDtpsPuesto(puestoAux);
//											dtpsAux.setDtpsCarrera(roflcrCarrera);

											
											RelacionLaboral rllbAux = em.find(RelacionLaboral.class, relacionLaboral);
											dtpsAux.setDtpsRelacionLaboral(rllbAux);
											dtpsAux.setDtpsPuesto(puesto);
											dtpsAux.setDtpsCarrera(roflcrCarrera);
											
											dtpsAux.setDtpsPracId(pracAuxActivo.getPracId());
											dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_PREGRADO_VALUE);
											dtpsAux.setDtpsFechaRegistro(new Timestamp((new Date()).getTime()));
											dtpsAux.setDtpsUsuario(usuarioRegistro.getUsrNick().toString());
											dtpsAux.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE);
											
											em.persist(dtpsAux);
											em.flush();
											retorno = true;
										}
									}
								}else{
									retorno = false;
								}
								
							} catch (FichaDocenteNoEncontradoException e) {
								fcdcAux = new FichaDocente();
								fcdcAux.setFcdcPersona(prsAux);
								fcdcAux.setFcdcEstado(0);
								em.persist(fcdcAux);
								em.flush();
								if(tipoCarrera == CarreraConstantes.TIPO_POSGRADO_VALUE || tipoCarrera == CarreraConstantes.TIPO_SUFICIENCIA_VALUE || tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
									DetallePuesto dtpsAux = new DetallePuesto();
									dtpsAux.setDtpsFichaDocente(fcdcAux);
									dtpsAux.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
									dtpsAux.setDtpsCarrera(roflcrCarrera);
									
									dtpsAux.setDtpsPracId(pracAuxActivo.getPracId());
									if(tipoCarrera == CarreraConstantes.TIPO_POSGRADO_VALUE){
										dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_POSGRADO_VALUE);
									}
									if(tipoCarrera == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
										dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
									}
									if(tipoCarrera == CarreraConstantes.TIPO_NIVELEACION_VALUE){
										dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_NIVELEACION_VALUE);
									}
									dtpsAux.setDtpsFechaRegistro(new Timestamp((new Date()).getTime()));
									dtpsAux.setDtpsUsuario(usuarioRegistro.getUsrNick().toString());
									dtpsAux.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE);
																		
									//aquí agregar la categoria y tiempo de dedicacion de la base de talento humano
//									docenteTH
									
									Puesto puestoAux = em.find(Puesto.class, 10);
									RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 2);
									dtpsAux.setDtpsRelacionLaboral(rllbAux);
									dtpsAux.setDtpsPuesto(puestoAux);
									em.persist(dtpsAux);
									em.flush();
									retorno = true;
								}else if(tipoCarrera == CarreraConstantes.TIPO_PREGRADO_VALUE){
									DetallePuesto dtpsAux = new DetallePuesto();
									dtpsAux.setDtpsFichaDocente(fcdcAux);
									dtpsAux.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
									dtpsAux.setDtpsCarrera(roflcrCarrera);
									
									//aquí agregar la categoria y tiempo de dedicacion de la base de talento humano
//									docenteTH
									
//									Puesto puestoAux = em.find(Puesto.class, 15);
//									RelacionLaboral rllbAux = em.find(RelacionLaboral.class, 1);
//									dtpsAux.setDtpsRelacionLaboral(rllbAux);
//									dtpsAux.setDtpsPuesto(puestoAux);
									
									RelacionLaboral rllbAux = em.find(RelacionLaboral.class, relacionLaboral);
									dtpsAux.setDtpsRelacionLaboral(rllbAux);
									dtpsAux.setDtpsPuesto(puesto);
									dtpsAux.setDtpsCarrera(roflcrCarrera);
									
									dtpsAux.setDtpsPracId(pracAuxActivo.getPracId());
									dtpsAux.setDtpsTipoCarrera(CarreraConstantes.TIPO_PREGRADO_VALUE);
									dtpsAux.setDtpsFechaRegistro(new Timestamp((new Date()).getTime()));
									dtpsAux.setDtpsUsuario(usuarioRegistro.getUsrNick().toString());
									dtpsAux.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE);
									
									em.persist(dtpsAux);
									em.flush();
									retorno = true;
								}
							}
							
						}else{
							retorno =true;	
						}
					}else{
						retorno = false;
					}
				}else{
					retorno =true;
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	

	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean anadirRolFlujoCarrera(UsuarioRolJdbcDto entidad,Integer rolId, Integer carreraId) throws PersonaValidacionException,PersonaException{
		boolean retorno = false;
		try {
			RolFlujoCarrera roflcrAux = buscarPorCarrera(carreraId, entidad.getUsrId(), rolId);
			throw new PersistenceException("El usuario ya existe en la carrera seleccionada");
		} catch (RolFlujoCarreraNoEncontradoException | RolFlujoCarreraException e) {
			try {
				Usuario usrAux = em.find(Usuario.class, entidad.getUsrId());
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" Select usro from UsuarioRol usro where ");
				sbSql.append(" usro.usroRol.rolId= :rolId ");
				sbSql.append(" and usro.usroUsuario.usrIdentificacion= :identificacion ");
				Query q = em.createQuery(sbSql.toString());
				q.setParameter("rolId",rolId);
				q.setParameter("identificacion",usrAux.getUsrIdentificacion());
				
				UsuarioRol usroAux = (UsuarioRol)q.getSingleResult();
				RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
				Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
				roflcrAux.setRoflcrCarrera(roflcrCarrera);
				roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				roflcrAux.setRoflcrUsuarioRol(usroAux);
				em.persist(roflcrAux);
				em.flush();
				retorno=true;
			} catch (NoResultException e2) {
				UsuarioRol usroCreacion = new UsuarioRol();
				Rol rolAux = em.find(Rol.class, rolId);
				Usuario usrAux = em.find(Usuario.class, entidad.getUsrId());
				usroCreacion.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				usroCreacion.setUsroRol(rolAux);
				usroCreacion.setUsroUsuario(usrAux);
				em.persist(usroCreacion);
				em.flush();
				RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
				Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
				roflcrAux.setRoflcrCarrera(roflcrCarrera);
				roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				roflcrAux.setRoflcrUsuarioRol(usroCreacion);
				em.persist(roflcrAux);
				retorno=true;
				em.flush();
			}
		}
		return retorno;
	}
	
	/**
	 * Metodo que busca las carreras asignadas a un usuario
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> buscarRolFlujoCarreraXUsuario(int  usrId){
		List<Carrera> retorno = null;
		try{
			retorno = new ArrayList<Carrera>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select crr from Carrera crr, Usuario usr, UsuarioRol usro, RolFlujoCarrera roflcr where ");
			sbSql.append(" usr.usrId = usro.usroUsuario.usrId ");
			sbSql.append(" and usro.usroId = roflcr.roflcrUsuarioRol.usroId ");
			sbSql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
			sbSql.append(" and usr.usrId = :id ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("id",usrId);
			retorno = q.getResultList();
		}catch(NoResultException nre){
		}catch(Exception nre){
			
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que busca las carreras asignadas a un usuario
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> buscarCarrerasXUsuarioPosgrado(int  usrId){
		List<Carrera> retorno = null;
		try{
			retorno = new ArrayList<Carrera>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select crr from Carrera crr, Usuario usr, UsuarioRol usro, RolFlujoCarrera roflcr where ");
			sbSql.append(" usr.usrId = usro.usroUsuario.usrId ");
			sbSql.append(" and usro.usroId = roflcr.roflcrUsuarioRol.usroId ");
			sbSql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
			sbSql.append(" and usr.usrId = :id ");
			sbSql.append(" and usro.usroRol.rolId = ");sbSql.append(RolConstantes.ROL_SECREPOSGRADO_VALUE);
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("id",usrId);
			retorno = q.getResultList();
		}catch(NoResultException nre){
		}catch(Exception nre){
			
		}
		
		return retorno;
	}
	
	
	/**
	 * Busca una entidad RolFlujoCarrera por idUsuario, idRol y su dependencia 
	 * @param usuarioId - de Usuario a buscar
	 * @param rolId - de Rol a buscar
	 * @return RolFlujoCarrera solicitado
	 * @author Daniel Albuja Daniel ALbuja
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> buscarXRolXUsuarioIdXDependencia(Integer usuarioId, Integer rolId, Integer dpnId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		List<RolFlujoCarrera> retorno = null;
		if(usuarioId != null && rolId != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				sbsql.append(" and roflcr.roflcrCarrera.crrDependencia.dpnId = :dependenciaId ");
				Query q = em.createQuery(sbsql.toString()); 
				q.setParameter("rolId", rolId );
				q.setParameter("usuarioId", usuarioId );
				q.setParameter("dependenciaId", dpnId );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.no.result.exception",usuarioId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.exception")));
			}
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<RolFlujoCarrera> buscarXUsuarioXRoles(Integer usuarioId, Integer[] rolesId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		List<RolFlujoCarrera> retorno = null;
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where  roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				sbsql.append(" and roflcr.roflcrEstado = :roflcrEstado ");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId in ");sbsql.append(Arrays.toString(rolesId).replace("[", "(").replace("]",")"));
				Query q = em.createQuery(sbsql.toString()); 
				q.setParameter("usuarioId", usuarioId );
				q.setParameter("roflcrEstado", RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.no.result.exception",usuarioId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.exception")));
			}
		return retorno;
	}
	
}
