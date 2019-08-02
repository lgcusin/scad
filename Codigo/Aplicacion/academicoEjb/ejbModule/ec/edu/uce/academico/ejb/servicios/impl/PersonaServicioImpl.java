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

 ARCHIVO:     PersonaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Persona. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
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
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ReferenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Etnia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.InstitucionAcademica;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Referencia;
import ec.edu.uce.academico.jpa.entidades.publico.Titulo;
import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)PersonaServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)  
public class PersonaServicioImpl implements PersonaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource ds;

	@EJB private UsuarioServicio servUsuario;
	@EJB private UsuarioRolServicio sevrUsuarioRol;
	
	@Resource 
	private SessionContext session;  
	/**
	 * Busca una entidad Persona por su id
	 * @param id - de la Persona a buscar
	 * @return Persona con el id solicitado
	 * @throws PersonaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Persona con el id solicitado
	 * @throws PersonaException - Excepcion general
	 */
	@Override
	public Persona buscarPorId(Integer id) throws PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Persona.class, id);
			} catch (NoResultException e) {
				throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.exception")));
			}
		}else{
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.no.result.exception",id)));
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Persona existentes en la BD
	 * @return lista de todas las entidades Persona existentes en la BD
	 * @throws PersonaException - PersonaException Excepcion general
	 * @throws PersonaNoEncontradoException - PersonaNoEncontradoException Excepcion lanzada cuando no hay personas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Persona> listarTodos() throws PersonaException, PersonaNoEncontradoException {
		List<Persona> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prs from Persona prs ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.no.result.exception")));
		}catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.exception")));
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
	public Persona buscarPorIdentificacion(String identificacion) throws PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prs from Persona prs ");
			sbsql.append(" where prs.prsIdentificacion =:identificacion");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("identificacion", identificacion);
			retorno = (Persona) q.getSingleResult();
		} catch (NoResultException e) {
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.no.result.exception")));
		}catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.exception")));
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
	public Persona buscarPersonaPorIdentificacion(String identificacion) throws   PersonaException {
		Persona retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prs from Persona prs ");
			sbsql.append(" where prs.prsIdentificacion =:identificacion");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("identificacion", identificacion);
			retorno = (Persona) q.getSingleResult();
		} catch (NoResultException e) {
			//throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.no.result.exception")));
			retorno = null;
		}catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.exception")));
		}
		return retorno;

	}
	
	
	
	
	
	/**
	 * Edita todos los atributos de la entidad persona, ficha estudiante por parte de la secretaria de carrera
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaException - Excepción general
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean actualizarEstudianteXSecretaria(EstudianteJdbcDto entidad, UbicacionDto cantonNacimiento, UbicacionDto parroquiaResidencia ) throws PersonaException {
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			Persona persona = em.find(Persona.class, entidad.getPrsId());
			// CAMPOS A GUARDARSE
			persona.setPrsNombres(GeneralesUtilidades.eliminarTildes(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsNombres()).toUpperCase()));
			persona.setPrsPrimerApellido(GeneralesUtilidades.eliminarTildes(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsPrimerApellido()).toUpperCase()));
			if(persona.getPrsPrimerApellido().length()==0){
				throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.actualizar.estudiante.apellido.null.exception")));
			}
			persona.setPrsSegundoApellido(entidad.getPrsSegundoApellido()!=null?(GeneralesUtilidades.eliminarTildes(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsSegundoApellido()).toUpperCase())): null);
			persona.setPrsFechaNacimiento(entidad.getPrsFechaNacimiento());
			
			Etnia etnia = em.find(Etnia.class, entidad.getEtnId());
			persona.setPrsEtnia(etnia);
			
			persona.setPrsSexo(entidad.getPrsSexo());
			if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
				persona.setPrsSexoSniese(PersonaConstantes.SEXO_HOMBRE_SNIESE_VALUE);
			}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
				persona.setPrsSexoSniese(PersonaConstantes.SEXO_MUJER_SNIESE_VALUE);
			}
			
			if(entidad.getPrsTipoIdentificacion()==PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE);
			}else if(entidad.getPrsTipoIdentificacion()==PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_SNIESE_VALUE);
			}
			
			persona.setPrsTelefono(entidad.getPrsTelefono());
			persona.setPrsCelular(entidad.getPrsCelular());
			//persona.setPrsMailInstitucional(entidad.getPrsMailInstitucional());
//			persona.setPrsMailPersonal(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsMailPersonal()));
			persona.setPrsMailPersonal(entidad.getPrsMailPersonal());
		    persona.setPrsEstadoCivil(entidad.getPrsEstadoCivil());
		    Ubicacion lugarNacimiento= em.find(Ubicacion.class, cantonNacimiento.getUbcId());
		    persona.setPrsUbicacionNacimiento(lugarNacimiento);
			persona.setPrsDiscapacidad(entidad.getPrsDiscapacidad());
			persona.setPrsCarnetConadis(entidad.getPrsCarnetConadis());
			persona.setPrsTipoDiscapacidad(entidad.getPrsTipoDiscapacidad());
			persona.setPrsPorceDiscapacidad(entidad.getPrsPorceDiscapacidad());
			persona.setPrsNumCarnetConadis(entidad.getPrsNumCarnetConadis());
			Ubicacion lugarResidencia= em.find(Ubicacion.class, parroquiaResidencia.getUbcId());
		    persona.setPrsUbicacionResidencia(lugarResidencia);
		    persona.setPrsCallePrincipal(entidad.getPrsCallePrincipal());
		    persona.setPrsCalleSecundaria(entidad.getPrsCalleSecundaria());
		    persona.setPrsNumeroCasa(entidad.getPrsNumeroCasa());
		    persona.setPrsSectorDomicilio(entidad.getPrsSectorDomicilio());
		    persona.setPrsReferenciaDomicilio(entidad.getPrsReferenciaDomicilio());
			persona.setPrsFechaActualizacionDatos(new Timestamp(new Date().getTime()));
		    
			//em.persist(persona);
			
		    InstitucionAcademica institucionAcademica = em.find(InstitucionAcademica.class, entidad.getFcesColegioId());
		    Titulo tituloBachiller = em.find(Titulo.class, entidad.getTtlId());
		    
		    FichaEstudiante fichaEstudiante = em.find(FichaEstudiante.class, entidad.getFcesId());
		   // fichaEstudiante.setFcesInstitucionAcademica(institucionAcademica);
		    fichaEstudiante.setFcesInacTtlId(entidad.getTtlId());
		    fichaEstudiante.setFcesNotaGradoSecundaria(entidad.getFcesNotaGradoSecundaria());
		    fichaEstudiante.setFcesUbcColegio(entidad.getFcesUbcColegio());  //Puede ser Pais o canton.
		    fichaEstudiante.setFcesFechaInicio(entidad.getFcesFechaInicio());
		   
		   //Guardado de campos adicionales
		    if(tituloBachiller!=null){
		    fichaEstudiante.setFcesTituloBachiller(tituloBachiller.getTtlDescripcion());
		    }else{
		      fichaEstudiante.setFcesTituloBachiller(null);
		    }
		   
		    if(institucionAcademica!=null){
		    	fichaEstudiante.setFcesColegioId(entidad.getFcesColegioId());
		    	  fichaEstudiante.setFcesTipoColegio(institucionAcademica.getInacTipo());
		    }else{
		    	 fichaEstudiante.setFcesColegioId(null);
				 fichaEstudiante.setFcesTipoColegio(null);
		    }
		   // Se guarda fecha actual de actualizacion de la ficha estudiante
		    fichaEstudiante.setFcesFechaActualizacionDatos(new Timestamp(new Date().getTime()));
//				em.persist(fichaEstudiante);
//				em.flush();
			session.getUserTransaction().commit();
			retorno = true;
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
			throw new PersonaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**
	 * Edita todos los atributos de la entidad persona por parte del estudiante
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaException - Excepción general
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean actualizarEstudianteXEstudiante(EstudianteJdbcDto entidad, UbicacionDto parroquiaResidencia, List<Referencia> listaReferenciaEditar, List<Referencia> listaReferenciaVer ) throws PersonaException {
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			Persona persona = em.find(Persona.class, entidad.getPrsId());
			Etnia etnia = em.find(Etnia.class, entidad.getEtnId());
			// CAMPOS A GUARDARSE
			persona.setPrsTelefono(entidad.getPrsTelefono());
			persona.setPrsCelular(entidad.getPrsCelular());
//			persona.setPrsMailPersonal(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsMailPersonal()));
			persona.setPrsMailPersonal(entidad.getPrsMailPersonal());
			Ubicacion lugarResidencia= em.find(Ubicacion.class, parroquiaResidencia.getUbcId());
		    persona.setPrsUbicacionResidencia(lugarResidencia);
		    persona.setPrsCallePrincipal(entidad.getPrsCallePrincipal());
		    persona.setPrsCalleSecundaria(entidad.getPrsCalleSecundaria());
		    persona.setPrsNumeroCasa(entidad.getPrsNumeroCasa());
		    persona.setPrsSectorDomicilio(entidad.getPrsSectorDomicilio());
		    persona.setPrsReferenciaDomicilio(entidad.getPrsReferenciaDomicilio());
		    persona.setPrsEtnia(etnia);
		    persona.setPrsSexo(entidad.getPrsSexo());
		    persona.setPrsEstadoCivil(entidad.getPrsEstadoCivil());
		    persona.setPrsFechaActualizacionDatos(new Timestamp(new Date().getTime()));
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
//	        String dateInString = "05-nov-2018";
	        Date date = formatter.parse(GeneralesConstantes.APP_FECHA_VINCULACION_SEGURO_VIDA_SUCRE);
	        Timestamp ts=new Timestamp(date.getTime());
			persona.setPrsFechaVinculacionSeguro(ts);
			persona.setPrsFechaRegistroSeguro(new Timestamp(new Date().getTime()));
			persona.setPrsEstadoSeguro(PersonaConstantes.ESTADO_SEGURO_ACTIVO_VALUE);
			em.persist(persona);
			if(listaReferenciaVer.size() <= 0){
				for (Referencia item : listaReferenciaEditar) {
					Referencia refAux = new Referencia();
					refAux.setRfrCedula(item.getRfrCedula().toUpperCase().trim());
					refAux.setRfrNombre(item.getRfrNombre().toUpperCase().trim());
					refAux.setRfrDireccion(item.getRfrDireccion().toUpperCase().trim());
					refAux.setRfrParentesco(item.getRfrParentesco());
					refAux.setRfrPorcentaje(item.getRfrPorcentaje());
					refAux.setRfrEstado(ReferenciaConstantes.ESTADO_ACTIVO_VALUE);
					refAux.setRfrTipo(ReferenciaConstantes.TIPO_BENEFICIARIO_SEGURO_VIDA_VALUE);
					refAux.setRfrPersona(persona);
					em.persist(refAux);
				}
			}else {
				for (Referencia itemVer : listaReferenciaVer) {
					boolean encontrado = false;
					for (Referencia item : listaReferenciaEditar) {
						if(itemVer.getRfrId() == item.getRfrId()){
							Referencia referenciaEdit = em.find(Referencia.class, itemVer.getRfrId());
							referenciaEdit.setRfrCedula(item.getRfrCedula().toUpperCase().trim());
							referenciaEdit.setRfrNombre(item.getRfrNombre().toUpperCase().trim());
							referenciaEdit.setRfrDireccion(item.getRfrDireccion().toUpperCase().trim());
							referenciaEdit.setRfrParentesco(item.getRfrParentesco());
							referenciaEdit.setRfrPorcentaje(item.getRfrPorcentaje());
							referenciaEdit.setRfrEstado(ReferenciaConstantes.ESTADO_ACTIVO_VALUE);
							referenciaEdit.setRfrTipo(ReferenciaConstantes.TIPO_BENEFICIARIO_SEGURO_VIDA_VALUE);
							referenciaEdit.setRfrPersona(persona);
							em.persist(referenciaEdit);
							encontrado = true;
						}
					}
					if(!encontrado){
						Referencia referenciaInact = em.find(Referencia.class, itemVer.getRfrId());
						referenciaInact.setRfrEstado(ReferenciaConstantes.ESTADO_INACTIVO_VALUE);
						em.persist(referenciaInact);
					}
				}
				for (Referencia item : listaReferenciaEditar) {
					boolean encontrado = false;
					for (Referencia itemVer : listaReferenciaVer) {
						if(itemVer.getRfrId() == item.getRfrId()){
							encontrado = true;
						}
					}
					if(!encontrado){
						Referencia refAux = new Referencia();
						refAux.setRfrCedula(item.getRfrCedula().toUpperCase().trim());
						refAux.setRfrNombre(item.getRfrNombre().toUpperCase().trim());
						refAux.setRfrDireccion(item.getRfrDireccion().toUpperCase().trim());
						refAux.setRfrParentesco(item.getRfrParentesco());
						refAux.setRfrPorcentaje(item.getRfrPorcentaje());
						refAux.setRfrEstado(ReferenciaConstantes.ESTADO_ACTIVO_VALUE);
						refAux.setRfrTipo(ReferenciaConstantes.TIPO_BENEFICIARIO_SEGURO_VIDA_VALUE);
						refAux.setRfrPersona(persona);
						em.persist(refAux);
					}
				}
			}
		   	em.flush();
			session.getUserTransaction().commit();
			retorno = true;
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
			throw new PersonaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Edita el atributos de la entidad persona el campo prs_fecha_formulario_seguro
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaException - Excepción general
	*/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean actualizarFormularioSeguroPersona(EstudianteJdbcDto entidad) throws PersonaException {
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			Persona persona = em.find(Persona.class, entidad.getPrsId());
			// CAMPOS A GUARDARSE
			persona.setPrsFormularioSeguro(entidad.getPrsIdentificacion());
			persona.setPrsFechaFormularioSeguro(new Timestamp(new Date().getTime()));
			em.persist(persona);
		   	em.flush();
			session.getUserTransaction().commit();
			retorno = true;
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
			throw new PersonaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PersonaNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws PersonaException - Excepcion general
	 */
	@Override
	public Persona editarXId(UsuarioRolJdbcDto entidad) throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getPrsId());
				if (retorno != null) {
					String identificacionAux = null;
					identificacionAux = retorno.getPrsIdentificacion();
					session.getUserTransaction().begin();
					retorno.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE);
					}
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_SNIESE_VALUE);
					}
					retorno.setPrsIdentificacion(entidad.getPrsIdentificacion().toUpperCase());
					retorno.setPrsPrimerApellido(entidad.getPrsPrimerApellido().replaceAll(" +", " ").trim().toUpperCase());
					if(entidad.getPrsSegundoApellido() != null && entidad.getPrsSegundoApellido() != "" && !entidad.getPrsSegundoApellido().equals(null)){
						retorno.setPrsSegundoApellido(entidad.getPrsSegundoApellido().replaceAll(" +", " ").trim().toUpperCase());
					}
					retorno.setPrsNombres(entidad.getPrsNombres().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsMailPersonal(entidad.getPrsMailPersonal().trim());
					retorno.setPrsMailInstitucional(entidad.getPrsMailInstitucional().replaceAll(" +", " ").trim());
					Usuario usuario = new Usuario();
					if(identificacionAux.equals(retorno.getPrsIdentificacion())){
						usuario = servUsuario.buscarUsuarioPorIdentificacion(retorno.getPrsIdentificacion());
					}else{
						usuario = servUsuario.buscarUsuarioPorIdentificacion(identificacionAux);
					}
					if(usuario != null){
						Usuario usuarioAux = new Usuario();
						usuarioAux = em.find(Usuario.class, usuario.getUsrId());
						usuarioAux.setUsrIdentificacion(entidad.getPrsIdentificacion().toUpperCase());
						String[] parts = retorno.getPrsMailInstitucional().split("@");
						usuarioAux.setUsrNick(parts[0]);
						if(entidad.getUsrEstado() == UsuarioConstantes.ESTADO_ACTIVO_VALUE){
							usuarioAux.setUsrEstado(entidad.getUsrEstado());
							em.merge(usuarioAux);
						}
						if(entidad.getUsrEstado() == UsuarioConstantes.ESTADO_INACTIVO_VALUE){
							
							PreparedStatement pstmt = null;
							PreparedStatement pstmt1 = null;
							Connection con = null;
							ResultSet rs = null;
							
							try{
								StringBuilder sbsql = new StringBuilder();
								sbsql.append(" Update Usuario set usrEstado = ");sbsql.append(UsuarioConstantes.ESTADO_INACTIVO_VALUE);
								sbsql.append(" where usrId = :usrId ");
								
								Query q = em.createQuery(sbsql.toString());
								q.setParameter("usrId", usuarioAux.getUsrId());
								q.executeUpdate();
								
								con = ds.getConnection();
								sbsql = new StringBuilder();
								sbsql.append(" Update rol_flujo_carrera set roflcr_estado = ");sbsql.append(RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE);
								sbsql.append(" where roflcr_id in ( ");
								sbsql.append(" select roflcr_id from usuario usr, usuario_rol usro, rol_flujo_carrera roflcr ");
								sbsql.append(" where usr.usr_id=usro.usr_id ");
								sbsql.append(" and usro.usro_id=roflcr.usro_id ");
								sbsql.append(" and usr.usr_id = ");sbsql.append(usuarioAux.getUsrId());sbsql.append(" )");
								pstmt = con.prepareStatement(sbsql.toString());
								pstmt.executeUpdate();
								
								sbsql = new StringBuilder();
								sbsql.append(" Update usuario_rol set usro_estado = ");sbsql.append(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
								sbsql.append(" where usro_id in ( ");
								sbsql.append(" select usro_id from usuario usr, usuario_rol usro ");
								sbsql.append(" where usr.usr_id=usro.usr_id ");
								sbsql.append(" and usr.usr_id = ");sbsql.append(usuarioAux.getUsrId());sbsql.append(" )");
								pstmt1 = con.prepareStatement(sbsql.toString());
								pstmt1.executeUpdate();
							}catch (Exception e) {
							}finally {
								try {
									if (rs != null) {
										rs.close();
									}
									if (pstmt != null) {
										pstmt.close();
									}
									if (pstmt1 != null) {
										pstmt1.close();
									}
									if (con != null) {
										con.close();
									}
								} catch (SQLException e) {
									}	
							}
						}
					}
					em.merge(retorno);
			   		em.flush();
					session.getUserTransaction().commit();
				}
			}
		} catch (PersonaNoEncontradoException e) {
			throw e;
		}  catch (PersonaException e) {
			throw e;
		} catch (SecurityException e) {
		} catch (IllegalStateException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (SystemException e) {
		} catch (NotSupportedException e) {
		} catch (UsuarioNoEncontradoException e) {
		} catch (UsuarioException e) {
		} 
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> listarPorIdentificacion(String identificacion) throws PersonaNoEncontradoException, PersonaException{
		List<Persona> retorno = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" Select prs from Persona prs ");
			sql.append(" where prs.prsIdentificacion like :identificacion");
			
			Query q = em.createQuery(sql.toString());
			q.setParameter("identificacion", identificacion.toUpperCase() + "%");
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.no.result.exception")));
		}catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.exception")));
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public List<Persona> listarPorApellidoPaterno(String apellido) throws PersonaNoEncontradoException, PersonaException{
		List<Persona> retorno = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" Select prs from Persona prs ");
			sql.append(" where prs.prsPrimerApellido like :apellido");
			
			Query q = em.createQuery(sql.toString());
			q.setParameter("apellido", apellido.toUpperCase() + "%");
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.no.result.exception")));
		}catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.listar.todos.exception")));
		}
		return retorno;
	}
	
	@Override
	public DatabaseMetaData retornarDs(){
		DatabaseMetaData md = null;
		try {
			md = ds.getConnection().getMetaData();
		} catch (SQLException e) {
		}
		return md;
	}
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
	
	
}
