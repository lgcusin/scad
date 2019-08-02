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

 ARCHIVO:     UsuarioServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Usuario. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)UsuarioServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class UsuarioServicioImpl implements UsuarioServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Usuario por su id
	 * @param id - del Usuario a buscar
	 * @return Usuario con el id solicitado
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada cuando no se encuentra un Usuario con el id solicitado
	 * @throws UsuarioException - Excepcion general
	 */
	@Override
	public Usuario buscarPorId(Integer id) throws UsuarioNoEncontradoException, UsuarioException {
		Usuario retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Usuario.class, id);
			} catch (NoResultException e) {
				throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Usuario existentes en la BD
	 * @return lista de todas las entidades Usuario existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> listarTodos() {
		List<Usuario> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select usr from Usuario usr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;

	}

	/**
	 * Busca a un usuario por su nick
	 * @param nickName - nick con el que se va a buscar
	 * @return usuario con el nick solicitado
	 * @throws UsuarioNoEncontradoException - UsuarioNoEncontradoException Excepcion lanzada cuando no encuentra usuario por los parametros ingresados
	 * @throws UsuarioException - UsuarioException Excepción general
	 */
	@Override
	public Usuario buscarPorNick(String nickName) throws UsuarioNoEncontradoException, UsuarioException {
		Usuario retorno = null;
		try{
			if(nickName != null){
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" Select usr from Usuario usr where ");
				sbSql.append(" upper(usr.usrNick)=upper(:nickName) ");

				Query q = em.createQuery(sbSql.toString());
				q.setParameter("nickName",nickName);
				retorno = (Usuario)q.getSingleResult();
			}
		}catch(NoResultException nre){
			throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.nickName.no.result.exception", nickName)));
		}catch(NonUniqueResultException nre){
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.nickName.non.unique.result.exception",nickName)));
		}catch (Exception nre) {
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.nickName.exception")));
		}
		return retorno;
	}
   

	/**
	 * Lista todas las entidades Persona existentes en la BD de acuerdo a su identificación
	 * @return busca las entidades Persona existentes en la BD
	 * @throws PersonaException - PersonaException Excepcion general
	 * @throws PersonaNoEncontradoException - PersonaNoEncontradoException Excepcion lanzada cuando no hay personas
	 */
	@Override
	public Usuario buscarUsuarioPorIdentificacion(String identificacion) throws UsuarioNoEncontradoException, UsuarioException {
		Usuario retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select usr from Usuario usr ");
			sbsql.append(" where usr.usrIdentificacion =:identificacion");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("identificacion", identificacion);
			retorno = (Usuario) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.no.result.exception")));
		}catch (Exception e) {
			throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.exception")));
		}
		return retorno;

	}
	
	
	
	@Override
	public boolean crearPersonaUsuario(UsuarioCreacionDto entidad) {
		boolean retorno = false;
		try {
			Usuario usrAux = new Usuario();
			Persona prsAux = new Persona();
			prsAux.setPrsMailPersonal(entidad.getPrsMailPersonal().toLowerCase());
			prsAux.setPrsMailInstitucional(entidad.getPrsMailInstitucional().toLowerCase());
			prsAux.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
			prsAux.setPrsIdentificacion(entidad.getPrsIdentificacion());
			prsAux.setPrsPrimerApellido(entidad.getPrsPrimerApellido().toUpperCase());
			prsAux.setPrsSegundoApellido(entidad.getPrsSegundoApellido().toUpperCase());
			prsAux.setPrsNombres(entidad.getPrsNombres().toUpperCase());
			prsAux.setPrsSexo(entidad.getPrsSexo());
			usrAux.setUsrIdentificacion(entidad.getPrsIdentificacion());
			usrAux.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
			usrAux.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			usrAux.setUsrEstSesion(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
			usrAux.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
			usrAux.setUsrFechaCreacion(new Timestamp((new Date()).getTime()));
			Date fechaActual = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(fechaActual);
			c.add(Calendar.YEAR, 1);
			Date fechaCaducidad = new Date();
			fechaCaducidad = c.getTime();
			usrAux.setUsrFechaCaducidad(fechaCaducidad);
			usrAux.setUsrFechaCadPass(fechaCaducidad);
			int indice = entidad.getPrsMailInstitucional().indexOf("@");
			String usrNick = entidad.getPrsMailInstitucional().substring(0,indice);
			usrAux.setUsrNick(usrNick);
			prsAux.setPrsEtnia(null);
			prsAux.setPrsFechaNacimiento(null);
			prsAux.setPrsTelefono(null);
			if(entidad.getPrsTipoIdentificacion()==PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
				Ubicacion ubc = em.find(Ubicacion.class, UbicacionConstantes.ECUADOR_VALUE);
				prsAux.setPrsUbicacionNacimiento(ubc);
			}else{
				prsAux.setPrsUbicacionNacimiento(null);	
			}
			
			prsAux.setPrsTelefono(null);
			em.persist(prsAux);
			em.flush();
			usrAux.setUsrPersona(prsAux);
			em.persist(usrAux);
			em.flush();
			retorno = true;
		} catch (Exception e) {
		}
		return retorno;
	}
	
}
