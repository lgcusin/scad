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

 ARCHIVO:     MatriculaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones del proceso de matriculación. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 27-03-2017          David Arellano                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import javax.transaction.SystemException;

import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.RegistroDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RegistroAutomaticoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoGratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

/**
 * Clase (Bean)MatriculaServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class RegistroAutomaticoServicioImpl implements RegistroAutomaticoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@Resource
	private SessionContext session;
	
	@EJB
	RolServicio servRasRol;
	
	@EJB
	PeriodoAcademicoServicio servRasPeriodoAcademico;
	@EJB 
	ConfiguracionCarreraServicio  servRasConfiguracionCarrera;
	@EJB
	PersonaServicio servPersona;
	@EJB
	UsuarioServicio servUsuario;
	@EJB
	UsuarioRolServicio servUsuarioRol;
	@EJB
	FichaInscripcionDtoServicioJdbc servFichaInscripcionDto;
	@EJB
	MatriculaServicioJdbc servRecordEstudianteSAU; 
	@EJB
	CarreraServicio servCarrera;
	@EJB
	PlanificacionCronogramaServicio servPlanificacionCronograma;
	@EJB
	FichaEstudianteServicio servFichaEstudiante;

	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la matricula de un estudiante 
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void generarRegistro(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
		String cedulaError=null;
		try {
			session.getUserTransaction().begin();
			
			//busco el período académico activo
			PeriodoAcademico pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			//fecha actual
			Timestamp fechaHoy = new Timestamp(new Date().getTime());
			
			int contador = 0;
			for (RegistroDto itemRegistro : listRegistroDto) {
				//*************************************************************
				//********************* VALIDACIONES **************************
				//*************************************************************
				if(itemRegistro.getRgdNuevo() == GeneralesConstantes.APP_NUEVO){
					//creo el objeto para instertar la persona
					Persona persona = new Persona();
					persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
					persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
					persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
					//validacion de constraint de identificacion
					if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
//						System.out.println(itemRegistro.getPrsIdentificacion());
						//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
						ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getRgdArea());
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcion = new FichaInscripcion();
						fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
						fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
						fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
						fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
						fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
						fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
						//seteo los datos dependiendo si es un estudiante nuevo o un estudiante viejo
							fichaInscripcion.setFcinObservacion("NUEVO CUPO - NIVELACION");
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
						fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
						fichaInscripcion.setFcinCarrera(itemRegistro.getFcinCarrera());
						fichaInscripcion.setFcinCncrArea(itemRegistro.getRgdArea());
						fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());
						//busco el rol de estudiante 
						Rol rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD);
						Usuario usr = new Usuario();
						try {
							usr = servUsuario.buscarUsuarioPorIdentificacion(itemRegistro.getPrsIdentificacion());
						} catch (Exception e) {
							persona = servPersona.buscarPersonaPorIdentificacion(itemRegistro.getPrsIdentificacion());
							usr.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
							usr.setUsrFechaCreacion(fechaHoy);
							usr.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
							usr.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
							usr.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
							String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
							usr.setUsrNick(partes[0]);
							usr.setUsrPersona(persona);
							em.persist(usr);
						}
						
						UsuarioRol usuarioRol = new UsuarioRol();
						try {
							usuarioRol = servUsuarioRol.buscarPorIdentificacionPorRol(itemRegistro.getPrsIdentificacion(),RolConstantes.ROL_ESTUD_VALUE);
							if(usuarioRol!=null){
								usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
								em.merge(usuarioRol);
								List<FichaInscripcionDto> fcinDto = new ArrayList<FichaInscripcionDto>();
								
								fcinDto = servFichaInscripcionDto.buscarFichasInscripcionRolEstudianteTipoNivelacion(usuarioRol.getUsroId(), FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
								for (FichaInscripcionDto fichaInscripcionDto : fcinDto) {
									FichaInscripcion fcin = new FichaInscripcion();
									fcin = em.find(FichaInscripcion.class, fichaInscripcionDto.getFcinId());
									fcin.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									em.merge(fcin);
								}
							}else{
								usuarioRol.setUsroRol(rolEstudiante);
								usuarioRol.setUsroUsuario(usr);
								usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
								em.persist(usuarioRol);
							}
						} catch (Exception e) {
							usuarioRol.setUsroRol(rolEstudiante);
							usuarioRol.setUsroUsuario(usr);
							usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
							em.persist(usuarioRol);
						}
						
						fichaInscripcion.setFcinUsuarioRol(usuarioRol);
						em.persist(fichaInscripcion);
//						cedulaError = persona.getPrsIdentificacion();
//						throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.validar.constraint.identificador")));
					}else{
						persona.setPrsSexo(itemRegistro.getPrsSexo());
						persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
						persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
						persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
						persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
						persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
						persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
						//validacion de constraint de mail personal
						if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
							cedulaError = persona.getPrsIdentificacion();
							throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.validar.constraint.mail")));
						}	
						
						//creo el objeto para insertar el usuario
						Usuario usuario = new Usuario();
						usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
						usuario.setUsrFechaCreacion(fechaHoy);
						usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
						usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
						usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
						//validacion de constraint de identificacion de usuario
						if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
							cedulaError = usuario.getUsrIdentificacion();
							throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.identificador")));
						}
						String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
						usuario.setUsrNick(partes[0]);
						//validacion de constraint de nick
						if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
							cedulaError = usuario.getUsrIdentificacion();
							throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.nick")));
						}
						
						//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
						ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getRgdArea());
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcion = new FichaInscripcion();
						fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
						fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
						fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
						fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
						fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
						fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
						//seteo los datos dependiendo si es un estudiante nuevo o un estudiante viejo
						if(itemRegistro.getRgdNuevo() == GeneralesConstantes.APP_NUEVO){
							fichaInscripcion.setFcinObservacion("NUEVO - NIVELACIÓN");
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
						}else{
							fichaInscripcion.setFcinObservacion("NO NUEVO - NIVELACIÓN");
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
						}
						fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
						fichaInscripcion.setFcinCarrera(itemRegistro.getFcinCarrera());
						fichaInscripcion.setFcinCncrArea(itemRegistro.getRgdArea());
						fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());
						//busco el rol de estudiante 
						Rol rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD);
						
						
						//*************************************************************
						//************************** INSERCIONES **********************
						//*************************************************************
						em.persist(persona);
						
						usuario.setUsrPersona(persona);
						em.persist(usuario);
						
						UsuarioRol usuarioRol = new UsuarioRol();
						usuarioRol.setUsroRol(rolEstudiante);
						usuarioRol.setUsroUsuario(usuario);
						usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
						em.persist(usuarioRol);
						
						fichaInscripcion.setFcinUsuarioRol(usuarioRol);
						em.persist(fichaInscripcion);
					}
					
				}else{
					//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
					ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getRgdArea());
					//creo el objeto para insertar la ficha inscripcion
					FichaInscripcion fichaInscripcion = new FichaInscripcion();
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
					fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
					fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
					fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
					fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
					//seteo los datos dependiendo si es un estudiante nuevo o un estudiante viejo
						fichaInscripcion.setFcinObservacion("NUEVO CUPO - NIVELACION");
						fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
					fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
					fichaInscripcion.setFcinCarrera(itemRegistro.getFcinCarrera());
					fichaInscripcion.setFcinCncrArea(itemRegistro.getRgdArea());
					fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());
					//busco el rol de estudiante 
					Rol rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD);
					Usuario usr = servUsuario.buscarUsuarioPorIdentificacion(itemRegistro.getPrsIdentificacion());
					UsuarioRol usuarioRol = new UsuarioRol();
					try {
						usuarioRol = servUsuarioRol.buscarPorIdentificacionPorRol(itemRegistro.getPrsIdentificacion(),RolConstantes.ROL_ESTUD_VALUE);
						if(usuarioRol!=null){
							usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
							em.merge(usuarioRol);
//							List<FichaInscripcionDto> fcinDto = new ArrayList<FichaInscripcionDto>();
//							
//							fcinDto = servFichaInscripcionDto.buscarFichasInscripcionRolEstudianteTipoNivelacion(usuarioRol.getUsroId(), FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE);
//							for (FichaInscripcionDto fichaInscripcionDto : fcinDto) {
//								FichaInscripcion fcin = new FichaInscripcion();
//								fcin = em.find(FichaInscripcion.class, fichaInscripcionDto.getFcinId());
//								fcin.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
//								em.merge(fcin);
//							}
						}else{
							usuarioRol = new UsuarioRol();
							usuarioRol.setUsroRol(rolEstudiante);
							usuarioRol.setUsroUsuario(usr);
							usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
							em.persist(usuarioRol);
						}
					} catch (Exception e) {
						usuarioRol = new UsuarioRol();
						usuarioRol.setUsroRol(rolEstudiante);
						usuarioRol.setUsroUsuario(usr);
						usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
						em.persist(usuarioRol);
					}
					
					fichaInscripcion.setFcinUsuarioRol(usuarioRol);
					em.persist(fichaInscripcion);
				}
				contador++;
				
				if(contador == 30 || contador==listRegistroDto.size()){
					em.flush();
					em.clear();
					contador = 0;
				}
			}
			session.getUserTransaction().commit();
			
		} catch (PersonaValidacionException e) {
			e.printStackTrace();
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException("Error al validar la persona, datos duplicados "+cedulaError);
		} catch (UsuarioValidacionException e) {
			e.printStackTrace();
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException("Error al validar el usuario, datos duplicados "+cedulaError);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoException("Error al insertar en la BD , intente nuevamente");
		}		
	}
	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la matricula de un estudiante 
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void generarRegistroPosgrado(List<RegistroDto> listRegistroDto , Integer pracId) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
		try {
			session.getUserTransaction().begin();
			
			//busco el período académico activo
			PeriodoAcademico pracActivo= em.find(PeriodoAcademico.class, pracId);
			//fecha actual
			Timestamp fechaHoy = new Timestamp(new Date().getTime());
			
			int contador = 0;
			for (RegistroDto itemRegistro : listRegistroDto) {
				
				//*************************************************************
				//********************* VALIDACIONES **************************
				//*************************************************************
				
				//creo el objeto para instertar la persona
				Persona persona = new Persona();
				persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
				persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					//TODO: MENSAJE DAVID 
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.validar.constraint.identificador")));
				}
				persona.setPrsSexo(itemRegistro.getPrsSexo());
				persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
				persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
				persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
				//validacion de constraint de mail personal
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					//TODO: MENSAJE DAVID
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.validar.constraint.mail")));
				}	
				
				//creo el objeto para insertar el usuario
				Usuario usuario = new Usuario();
				usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
				usuario.setUsrFechaCreacion(fechaHoy);
				usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
				usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
				usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion de usuario
				if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					//TODO: MENSJE DAVID
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.identificador")));
				}
				String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
				usuario.setUsrNick(partes[0]);
				//validacion de constraint de nick
				if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					//TODO: MENSJE DAVID
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.nick")));
				}
				////////////////////////////// TODO MODIFICAR PARA CONFIGURACION CARRERA POSGRADO
				//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
				ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
				//creo el objeto para insertar la ficha inscripcion
				FichaInscripcion fichaInscripcion = new FichaInscripcion();
				fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
//				fichaInscripcion.setFcinConfiguracionCarrera(null);
				fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
				fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
				fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE);
				fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
				//seteo los datos dependiendo si es un estudiante nuevo o un estudiante viejo
				if(itemRegistro.getRgdNuevo() == GeneralesConstantes.APP_NUEVO){
					fichaInscripcion.setFcinObservacion("NUEVO - POSGRADO");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
				}else{
					fichaInscripcion.setFcinObservacion("NO NUEVO - POSGRADO");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
				}
				fichaInscripcion.setFcinNotaEnes(new Float(0)); //MQ 17 jul 2019  antes Int
				fichaInscripcion.setFcinCarrera(itemRegistro.getFcinCarrera());
				
				//busco el rol de estudiante 
				Rol rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUDIANTEPOSGRADO);
				
				
				//*************************************************************
				//************************** INSERCIONES **********************
				//*************************************************************
				em.persist(persona);
				
				usuario.setUsrPersona(persona);
				em.persist(usuario);
				
				UsuarioRol usuarioRol = new UsuarioRol();
				usuarioRol.setUsroRol(rolEstudiante);
				usuarioRol.setUsroUsuario(usuario);
				usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(usuarioRol);
				
				fichaInscripcion.setFcinUsuarioRol(usuarioRol);
				em.persist(fichaInscripcion);
				
				contador++;
				
				if(contador == 30){
					em.flush();
					em.clear();
					contador = 0;
				}
				
			}
			
			session.getUserTransaction().commit();
			
		} catch (PersonaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			 TODO Auto-generated catch block
//			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.nick")));
			throw new RegistroAutomaticoValidacionException("Error al validar la persona, datos duplicados");
		} catch (UsuarioValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
//			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.nick")));
			throw new RegistroAutomaticoValidacionException("Error al validar el usuario, datos duplicados");
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			// TODO: handle exception
			throw new RegistroAutomaticoException("Error al insertar en la BD , intente nuevamente");
		}		
	}
	

	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la ficha de inscripcion de un estudiante a homologar desde cero  o crea solo ficha inscripcion si es cambio de carrera.
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarRegistroHomologacion(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
	
		String retorno = null;
		
		try {
			session.getUserTransaction().begin();
			
			
			int contador = 0;
			for (RegistroDto itemRegistro : listRegistroDto) {
				
				//*************************************************************
				//********************* VALIDACIONES **************************
				//*************************************************************
				
				
			//CASOS DE PREGRADO
				//Busco si existe la persona
				Persona AuxPersona= new Persona();
				AuxPersona= servPersona.buscarPersonaPorIdentificacion(itemRegistro.getPrsIdentificacion());				
				if(AuxPersona==null){
					//busco el período académico activo
					PeriodoAcademico pracActivo= new PeriodoAcademico();
					pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//creo el objeto para instertar la persona
					Persona persona = new Persona();
					//creo el objeto para insertar el usuario
					Usuario usuario = new Usuario();
					Rol rolEstudiante= new Rol();
					//creo el objeto para insertar la ficha inscripcion
					FichaInscripcion fichaInscripcion = new FichaInscripcion();
					
				persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
				persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de persona ya existe.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
					
					//throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.identificador.persona")));
				}
				persona.setPrsSexo(itemRegistro.getPrsSexo());
				persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
				persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
				persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
				//validacion de constraint de mail personal
				
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El mail personal se encuentra ya registrado.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					//throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.mail.persona")));
				
				}	
				
				usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
				usuario.setUsrFechaCreacion(fechaHoy);
				usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
				usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
				usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion de usuario
				if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de usuario ya existe.");
					retorno= retornoAux.toString()  ;
                  //   throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.identificador.usuario")));
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				}
				String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
				usuario.setUsrNick(partes[0]);
				//validacion de constraint de nick
				if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El nick de usuario ya existe.");
					retorno= retornoAux.toString()  ;
				//	throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.nick.usuario")));
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
				
				//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
			   //	ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
				
				ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
				if(configuracionCarrera==null){
				ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
				if(configuracionCarrera2!=null){
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
					}else{
						retorno= null;
						StringBuilder retornoAux= new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
						retorno= retornoAux.toString()  ;
						throw new PersonaValidacionException(" ");
						
					}
				}else{
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
				}
				
				
				/*
				 * AÑADIDO    13/03/2018**********************************
				 */
//				
//				Carrera carreraSiiuAux= new Carrera();
//				carreraSiiuAux= servCarrera.buscarPorId(itemRegistro.getFcinCarreraSiiu());
//				
											
				if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
						
							fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO REINGRESO");
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
							fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
							fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if (itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIO CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIO UNIVERSIDAD");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO REINICIO CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					fichaInscripcion.setFcinReinicioOrigen(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE);
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIO MALLA CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO SEGUNDA CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIOS POR REDISEÑO");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO UBICACIÓN IDIOMAS");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO INTERCAMBIO");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE); //ESTUDIANTE CON FCIN ACTIVO
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);  // NO SE HOMOLOGA PASA DIRECTO.
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
				
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO SUFICIENCIA CULTURA FISICA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO INTENSIVO SUFICIENCIA CULTURA FISICA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
				}
				else{
					//ESTUDIANTE SIN TIPO DE INGRESO 
					retorno = null;
					StringBuilder retornoAux = new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-Estudiante sin tipo de ingreso valido");
					retorno = retornoAux.toString();
					throw new PersonaValidacionException(" ");
					
				}
				
				//GUARDAR PERIODO ACADEMICO DE ACUERDO AL TIPO DE PERIODO, PREGRADO, IDIOMAS, CULTURA FISICA, INTENSIVO CULTURA FISICA
				
			    if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
					//BUSCO PERIODO ACADEMICO DE IDIOMAS
					PeriodoAcademico pracIdiomasActivo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					if(pracIdiomasActivo!=null){
					fichaInscripcion.setFcinPeriodoAcademico(pracIdiomasActivo);
					}else{
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe periodo academico activo de tipo idiomas");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
					}
					
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE){
					//BUSCO PERIODO ACADEMICO DE SUFICIENCIA_CULTURA_FISICA
					PeriodoAcademico pracCulturaFisicaActivo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					if(pracCulturaFisicaActivo!=null){
					fichaInscripcion.setFcinPeriodoAcademico(pracCulturaFisicaActivo);
					}else{
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe periodo academico activo de tipo suficiencia cultura física");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
					}
					
			    
				}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE){
					//BUSCO PERIODO ACADEMICO DE SUFICIENCIA_CULTURA_FISICA
					PeriodoAcademico pracIntensivoCulturaFisicaActivo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					if(pracIntensivoCulturaFisicaActivo!=null){
					fichaInscripcion.setFcinPeriodoAcademico(pracIntensivoCulturaFisicaActivo);
					}else{
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe periodo academico activo de tipo intensivo suficiencia cultura física");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
					}
					
			    
				}else{//PERIODO ACADÉMICO  PREGRADO
				fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
				}
			    
				fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
				fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
				fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
				fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
				fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
				fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
				fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
				 
				//busco el rol de estudiante 
			
					 rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD_PREGRADO);
				
				//*************************************************************
				//************************** INSERCIONES **********************
				//*************************************************************
				em.persist(persona);
				usuario.setUsrPersona(persona);
				em.persist(usuario);
				UsuarioRol usuarioRol = new UsuarioRol();
				usuarioRol.setUsroRol(rolEstudiante);
				usuarioRol.setUsroUsuario(usuario);
				usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(usuarioRol);
				
				fichaInscripcion.setFcinUsuarioRol(usuarioRol);
				em.persist(fichaInscripcion);
				
			
				contador++;
				   if(contador == 30){
				    	em.flush();
				     	em.clear();
					   contador = 0;
			    	}
				//FIN CREACION SINO EXISTE PERSONA
				   
				   
				} else{ //Si Existe persona en el SIIU
					
					//PARA CASOS DE IDIOMAS Y SUFICIENCIAS CULTURA FISICA ESTE PROCESO ES EL CORRECTO
					if((itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE)
						||(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
						||(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE)) 	{
					
						//BUSCO PERIODO ACTIVO DE IDIOMAS, SUFICIENCIAS CULTURA FISICA
						PeriodoAcademico pracActivoPorTipo= new PeriodoAcademico();
						if (itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
						pracActivoPorTipo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						
						  if(pracActivoPorTipo==null){
							retorno = null;
							StringBuilder retornoAux = new StringBuilder();
							retornoAux.append(itemRegistro.getPrsIdentificacion());
							retornoAux.append("-No existe periodo academico activo de idiomas");
							retorno = retornoAux.toString();
							throw new PersonaValidacionException(" ");
						  }
						
						
						
						}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE){
							pracActivoPorTipo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);	
							 if(pracActivoPorTipo==null){
									retorno = null;
									StringBuilder retornoAux = new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-No existe periodo academico activo de suficiencia cultura física");
									retorno = retornoAux.toString();
									throw new PersonaValidacionException(" ");
								  }
						
						}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE){
							pracActivoPorTipo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);	
							if(pracActivoPorTipo==null){
								retorno = null;
								StringBuilder retornoAux = new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-No existe periodo academico activo de intensivo suficiencia cultura física");
								retorno = retornoAux.toString();
								throw new PersonaValidacionException(" ");
							  }
						}
						
						//FECHA ACTUAL
						Timestamp fechaHoy = new Timestamp(new Date().getTime());
						//busco todas las fichas inscripción en la carrera
						List<FichaInscripcionDto> auxListaFichaInscripcion = new ArrayList<>();
						auxListaFichaInscripcion= servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE)	;
						
						if(auxListaFichaInscripcion==null|| auxListaFichaInscripcion.size()<=0)	{// NO TIENE FICHA INSCRIPCION ALGUNA
							//creo el objeto para insertar la ficha inscripcion
							FichaInscripcion fichaInscripcion = new FichaInscripcion();
							
							Usuario usuarioAux = new Usuario();
						try{//BUSCO USUARIO DE LA PERSONA	
							usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
						}catch (UsuarioNoEncontradoException e){
							retorno = null;
							StringBuilder retornoAux = new StringBuilder();
							retornoAux.append(itemRegistro.getPrsIdentificacion());
							retornoAux.append("-Persona existente no tiene usuario creado");
							retorno = retornoAux.toString();
							throw new PersonaValidacionException(" ");
							
						}catch (UsuarioException e){
							retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Error desconocido al buscar usuario");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");	 							
						}
							
							
							 UsuarioRol	usuarioRolAux = new UsuarioRol();
							if(usuarioAux!=null){
								try{//BUSCO USUARIO ROL DE PREGRADO
								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
								}catch ( UsuarioRolNoEncontradoException e){
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
																									
								}catch ( UsuarioRolException e){
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
								} 
								 
							if(usuarioRolAux!=null){
							UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
							   //BUSCO CONFIGURACION CARRERA CON ID_CARRERA Y SEXO
								ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
								
								if(configuracionCarrera==null){//Si no se encontro cncr, se buscar sin sexo_id
								ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
								if(configuracionCarrera2!=null){
								fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
								}else{
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-No existe registro de configuración carrera por id Carrera_siiu, comuniquese con el administrador del sistema");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
									
								}
								
								}else{
								fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
								}
															
								if (itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
										fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION UBICACIÓN IDIOMAS");
									}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE){
											fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION SUFICIENCIA CULTURA FISICA");
									}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE){
											fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION INTENSIVO SUFICIENCIA CULTURA FISICA");
									}
																
								
								fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
							
								fichaInscripcion.setFcinPeriodoAcademico(pracActivoPorTipo);
								
								fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
								fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
								fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
								fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
								fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
								fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
								fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
								fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
								fichaInscripcion.setFcinUsuarioRol(usuarioRolPersis);
								
								em.persist(fichaInscripcion);
						            	  
									
									contador++;
									if(contador == 30){
										em.flush();
										em.clear();
										contador = 0;
									}
								
							  }
					
						   }
							
						}	else if(auxListaFichaInscripcion.size()>=1){// TIENE UNA O MAS FICHAS INSCRIPCION, suficiencia no puede tener fcin anterior
							
							
							retorno= null;
							StringBuilder retornoAux= new StringBuilder();
							retornoAux.append(itemRegistro.getPrsIdentificacion());
							retornoAux.append("-El estudiante ya presenta registro de inscripción en la suficiencia, comuniquese con el administrador del sistema");
							retorno= retornoAux.toString()  ;
							throw new PersonaValidacionException(" ");
								
							
							
					   }
				
						
					}else{ //OTROS TIPOS DE INGRESO DISTINTO A IDIOMAS Y SUFICIENCIAS CULTURA FISICA
					//busco el período académico activo
					PeriodoAcademico pracActivo= new PeriodoAcademico();
					   pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
				
					//Busco ficha inscripcion creada en este periodo en la carrera
					FichaInscripcionDto auxFichaInscripcionPeriodoActivo= new FichaInscripcionDto();
					auxFichaInscripcionPeriodoActivo=servFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraxPeriodo(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), pracActivo.getPracId());
					
					
					if	(auxFichaInscripcionPeriodoActivo==null){  //NO Tiene ficha  inscripcion creada en periodo actual en la carrera a ingresar
						
						//Busco si existe una ficha inscripcion ACTIVA en la carrera indicada 
						FichaInscripcionDto auxFichaInscripcion= new FichaInscripcionDto();
						auxFichaInscripcion=servFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraNueva(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu());
						
						if(auxFichaInscripcion==null){  //NO TIENE FICHA INSCRIPCION ACTIVA en la carrera--CREO NUEVA FICHA INSCRIPCION
						
								//creo el objeto para insertar la ficha inscripcion
							FichaInscripcion fichaInscripcionNueva = new FichaInscripcion();
							
							Usuario usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
							 UsuarioRol	usuarioRolAux = new UsuarioRol();
							if(usuarioAux!=null){
								try{
								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
								}catch ( UsuarioRolNoEncontradoException e){
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
									
								}catch ( UsuarioRolException e){
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
								} 
								 
							if(usuarioRolAux!=null){
							UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
							
							//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
							   //	ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
									
								   //Carrera 
								ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
								
								if(configuracionCarrera==null){
								ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
								
								if(configuracionCarrera2!=null){
									fichaInscripcionNueva.setFcinConfiguracionCarrera(configuracionCarrera2);
									}else{
										retorno= null;
										StringBuilder retornoAux= new StringBuilder();
										retornoAux.append(itemRegistro.getPrsIdentificacion());
										retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
										retorno= retornoAux.toString()  ;
										throw new PersonaValidacionException(" ");
										
									}
								
								
								}else{
								fichaInscripcionNueva.setFcinConfiguracionCarrera(configuracionCarrera);
								}
								
							
								
								/*
								 * AÑADIDO    13/03/2018**********************************
								 */
								
//								Carrera carreraSiiuAux= servCarrera.buscarPorId(itemRegistro.getFcinCarreraSiiu());
//								List<RecordEstudianteSAUDto> recordEstudianteSau= servRecordEstudianteSAU.buscarRecordAcademicoSAU(itemRegistro.getPrsIdentificacion(), carreraSiiuAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);

								if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
											fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVA F_INSCRIPCION REINGRESO");
											fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
											fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
											fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
									
								}else if (itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVA F_INSCRIPCION CAMBIO CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
									
								}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVA F_INSCRIPCION CAMBIO UNIVERSIDAD");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
																
								}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO  F_INSCRIPCION REINICIO CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
									fichaInscripcionNueva.setFcinReinicioOrigen(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE);
									
								}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION CAMBIO MALLA CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
									
								}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION SEGUNDA CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
									
								}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION CAMBIOS POR REDISEÑO");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
									
								}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION INTERCAMBIO");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE); 
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);  
									fichaInscripcionNueva.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								
								}else{
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION SIN TIPO DE INGRESO");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
									
								}
								
					
								fichaInscripcionNueva.setFcinPeriodoAcademico(pracActivo);
								fichaInscripcionNueva.setFcinFechaInscripcion(fechaHoy);
								fichaInscripcionNueva.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
								fichaInscripcionNueva.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
								fichaInscripcionNueva.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
								fichaInscripcionNueva.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
								fichaInscripcionNueva.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
								fichaInscripcionNueva.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
								fichaInscripcionNueva.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
								 
								//*************************************************************
								//************************** INSERCIONES **********************
								//*************************************************************
								
								fichaInscripcionNueva.setFcinUsuarioRol(usuarioRolPersis);
									em.persist(fichaInscripcionNueva);
						            	 
						            	  List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
								        	 //Se busca lista las fichas inscripcion Anteriores en la carrera  para buscar la ficha estudiante
											  auxListaFichaInscripcion=servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
								        	//Realizo los siguiente solo si existe al menos una ficha inscripcion anterior											  
									if((auxListaFichaInscripcion!=null)&&(auxListaFichaInscripcion.size()>0))	{	  
											//********************************	NEW	 casos con varias fichas estudiante  ***********************
												List<FichaEstudiante> auxListaFinalFichasEstudiante = new ArrayList<>();
															
												 
												  //BUSCO FICHAS ESTUDIANTE POR TODAS LA FICHAS_INSCRIPCION  inactivas en la carrera
												for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
													List<FichaEstudiante> auxListaFichasEstudiante2 = new ArrayList<>();
													auxListaFichasEstudiante2= servFichaEstudiante.listarPorFcinId(fichaInscripcionDto.getFcinId());
			                                      if	((auxListaFichasEstudiante2!=null)&&(auxListaFichasEstudiante2.size()>0)){
			                                      	
			                                      	for (FichaEstudiante fichaEstudiante : auxListaFichasEstudiante2) {
			                                      		auxListaFinalFichasEstudiante.add(fichaEstudiante);
														}
			                                      	
			                                      }
												}
									        
												if((auxListaFinalFichasEstudiante!=null)&&(auxListaFinalFichasEstudiante.size()>0)){  // Existe alguna fces
												if(auxListaFinalFichasEstudiante.size()==1){ // Si existe una ficha estudiante en la carrera con alguna de las fichas inscripcion
														FichaEstudiante fichaEstudianteExistente = new FichaEstudiante();
														fichaEstudianteExistente= em.find(FichaEstudiante.class, auxListaFinalFichasEstudiante.get(0).getFcesId()); // buscamos la ficha estudiante existente para editar
														      
														if(fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId()!=fichaInscripcionNueva.getFcinId()){
														         Integer  auxFcinAnterior =null;
														           auxFcinAnterior=fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId();//guardamos el id de fcin anterior guardada en la fces
														          FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId()); //buscamos la fcin anterior
																   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);  //inactivamos la fcin anterior
																   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
														           fichaEstudianteExistente.setFcesFichaInscripcion(fichaInscripcionNueva); //guardamos la fcin nueva en la ficha estudiante
														           FichaInscripcion fichaInscripcionNuevaAux= em.find(FichaInscripcion.class,fichaInscripcionNueva.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
														           fichaInscripcionNuevaAux.setFcinFcinAnteriorId(auxFcinAnterior); //guardamos el la fcin nueva el idFcin anterior.
														           
														}
														
													}else if(auxListaFinalFichasEstudiante.size()>1){  //VARIAS FICHAS ESTUDIANTE EN LA CARRERA 
														
														retorno= null;
														StringBuilder retornoAux= new StringBuilder();
														retornoAux.append(itemRegistro.getPrsIdentificacion());
														retornoAux.append("-Persona presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema");
														retorno= retornoAux.toString()  ;
														throw new PersonaValidacionException(" ");
														
													   }
												}else{ //NO TIENE NINGUNA FICHA ESTUDIANTE
													//Busco la ficha_iscripcion  con mator Id
													FichaInscripcionDto auxFichaInscripcionInactivaMayor=servFichaInscripcionDto.buscarFichaInscripcionXIdentificacionXCarreraXIdMayor(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu());
													
													if(auxFichaInscripcionInactivaMayor.getFcinId()!=fichaInscripcionNueva.getFcinId()){
															   Integer  auxFcinAnterior =null;
													           auxFcinAnterior=auxFichaInscripcionInactivaMayor.getFcinId();//guardamos el id de fcin anterior
													           FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,auxFichaInscripcionInactivaMayor.getFcinId());//buscamos el objeto fcin anterior
															   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE); //inactivamos el fcin anterior
															   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
													           FichaInscripcion fichaInscripcionNuevaAux= em.find(FichaInscripcion.class,fichaInscripcionNueva.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
													           fichaInscripcionNuevaAux.setFcinFcinAnteriorId(auxFcinAnterior); 
													}
													
												}	  
																								  
													//************************************	  ***************************
											  
										
												
									}			
												
						            	  
						       //       }  //  FIN PARA EL RESTO DE TIPOS DE INGRESO: CAMBIOS, REINGRESOS
									contador++;
									if(contador == 30){
										em.flush();
										em.clear();
										contador = 0;
									}
									// }else{ //SI TIENE FICHA INSCRIPCIONINACTIVA Y NINGUNA ACTIVA
									// retorno= itemRegistro.getPrsIdentificacion();
									// throw new PersonaValidacionException("Persona no tiene ficha inscripcion activa, 1 o varias inactivas");
									// }
									
							}
					
						}else {

							retorno = null;
							StringBuilder retornoAux = new StringBuilder();
							retornoAux.append(itemRegistro.getPrsIdentificacion());
							retornoAux.append("-Persona existente no tiene usuario creado");
							retorno = retornoAux.toString();
							throw new PersonaValidacionException(" ");

						}
									
				   }else if(auxFichaInscripcion!=null){ //SI TIENE FICHA INSCRIPCION ACTIVA.
						
						FichaInscripcion fcinExistente=em.find(FichaInscripcion.class,auxFichaInscripcion.getFcinId());
						fcinExistente.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
						fcinExistente.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
						
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcionNuevo = new FichaInscripcion();
 						
						Usuario usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
						UsuarioRol	usuarioRolAux= new UsuarioRol();
						if(usuarioAux!=null){
							try{
								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
							}catch ( UsuarioRolNoEncontradoException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
								
							}catch ( UsuarioRolException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
							} 
						if(usuarioRolAux!=null){
						
						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
						
						//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
						   //	ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
								
							   //Carrera 
							ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
							
							if(configuracionCarrera==null){
							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
							
							if(configuracionCarrera2!=null){
								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera2);
								}else{
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
									
								}
												
							
							}else{
								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera);
							}
							
						
							if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE REINGRESO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								
							}else if (itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE CAMBIO CARRERA  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								
							}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE CAMBIO UNIVERSIDAD  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
																						
							}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE REINICIO CARRERA  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								fichaInscripcionNuevo.setFcinReinicioOrigen(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE);
								
							}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE  CAMBIO MALLA  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								
							}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE  SEGUNDA CARRERA SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								
							}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE CAMBIOS POR REDISEÑO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
								
							}else if(itemRegistro.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE NUEVO INTERCAMBIO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);   
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE); 
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
							
							}else{
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE SIN TIPO DE INGRESO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
								
							}
							
						
							fichaInscripcionNuevo.setFcinPeriodoAcademico(pracActivo);
							fichaInscripcionNuevo.setFcinFechaInscripcion(fechaHoy);
							fichaInscripcionNuevo.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
							fichaInscripcionNuevo.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
							fichaInscripcionNuevo.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
							fichaInscripcionNuevo.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // para carreras SiiU 
							fichaInscripcionNuevo.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
							fichaInscripcionNuevo.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
							fichaInscripcionNuevo.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE); 
													
							fichaInscripcionNuevo.setFcinUsuarioRol(usuarioRolPersis);
								em.persist(fichaInscripcionNuevo);
						            	 
					            	  List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
							        	 //Se busca lista las fichas inscripcion en la carrera  para buscar la ficha estudiante
										  auxListaFichaInscripcion=servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
							        	
																			 
										//  Si Existe  al menos una ficha inscripcon
								 if((auxListaFichaInscripcion!=null)&&(auxListaFichaInscripcion.size()>0)) {
									
											  
									//********************************	NEW	 casos con varias fichas estudiante  ***********************
									List<FichaEstudiante> auxListaFinalFichasEstudiante = new ArrayList<>();
												
									 
									  //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION en la carrera
									for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
										List<FichaEstudiante> auxListaFichasEstudiante2 = new ArrayList<>();
										auxListaFichasEstudiante2= servFichaEstudiante.listarPorFcinId(fichaInscripcionDto.getFcinId());
                                      if	((auxListaFichasEstudiante2!=null)&&(auxListaFichasEstudiante2.size()>0)){
                                      	
                                      	for (FichaEstudiante fichaEstudiante : auxListaFichasEstudiante2) {
                                      		auxListaFinalFichasEstudiante.add(fichaEstudiante);
											}
                                      	
                                      }
									}
						        
									if((auxListaFinalFichasEstudiante!=null)&&(auxListaFinalFichasEstudiante.size()>0)){  // Existe alguna fces
									if(auxListaFinalFichasEstudiante.size()==1){ // Ya existe una ficha estudiante en la carrera
											FichaEstudiante fichaEstudianteExistente = new FichaEstudiante();
											fichaEstudianteExistente= em.find(FichaEstudiante.class, auxListaFinalFichasEstudiante.get(0).getFcesId()); // buscamos la ficha estudiante existente para editar
											         
											if(fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId()!=fichaInscripcionNuevo.getFcinId()){
											           Integer  auxFcinAnterior =null;
											           auxFcinAnterior=fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId();//guardamos el id de fcin anterior
											           FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId());
													   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
													   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
											           fichaEstudianteExistente.setFcesFichaInscripcion(fichaInscripcionNuevo); //guardamos la fcin nueva en la ficha estudiante
											           FichaInscripcion fichaInscripcionNueva= em.find(FichaInscripcion.class,fichaInscripcionNuevo.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
											           fichaInscripcionNueva.setFcinFcinAnteriorId(auxFcinAnterior); //guardamos el la fcin nueva el idFcin anterior.
											          
											}
											           
										}else if(auxListaFinalFichasEstudiante.size()>1){  //VARIAS FICHAS ESTUDIANTE EN LA CARRERA 
											
											retorno= null;
											StringBuilder retornoAux= new StringBuilder();
											retornoAux.append(itemRegistro.getPrsIdentificacion());
											retornoAux.append("-Persona presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema");
											retorno= retornoAux.toString()  ;
											throw new PersonaValidacionException(" ");
											
										   }
									}else{ //NO TIENE NINGUNA FICHA ESTUDIANTE
												  
									    	if(fcinExistente.getFcinId()!=fichaInscripcionNuevo.getFcinId()){
										           Integer  auxFcinAnterior =null;
										           auxFcinAnterior=fcinExistente.getFcinId();//guardamos el id de fcin anterior
										           FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,fcinExistente.getFcinId());//buscamos el objeto fcin anterior
												   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE); //inactivamos el fcin anterior
												   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
												   FichaInscripcion fichaInscripcionNueva= em.find(FichaInscripcion.class,fichaInscripcionNuevo.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
										           fichaInscripcionNueva.setFcinFcinAnteriorId(auxFcinAnterior); 
										    }  
									}	  
										
							  }
					            	  
				//	       }  //  FIN PARA EL RESTO DE TIPOS DE INGRESO: CAMBIOS, REINGRESOS
								
								
								
								contador++;
								if(contador == 30){
									em.flush();
									em.clear();
									contador = 0;
								}
											
								} 
								
							} else {

								retorno = null;
								StringBuilder retornoAux = new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario creado");
								retorno = retornoAux.toString();
								throw new PersonaValidacionException(" ");

							}

						}

					} else {
						// ESTUDIANTE YA INGRESADO EN ESTE PERIODO POR HOMOLOGACION

						// retorno= itemRegistro.getPrsIdentificacion();
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Persona registrada en este periodo académico por homologación");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");

					}

				}
			
			}
				
								
				
			}
			
			session.getUserTransaction().commit();
			
			retorno=null;
		} catch (PersonaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.persona", retorno)));
     	    //throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(retorno)));
     	    
		} catch (UsuarioValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		//	throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario")));
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario", retorno)));
			
		}
		catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
				
				
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.exception")));
		}	
		
		
		return retorno;
	}
	
	
	
	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintIdenfificadorPersona(Persona entidad, int tipo){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsIdentificacion)= :identificacion ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId != :personaId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",entidad.getPrsIdentificacion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintMailPersona(Persona entidad, int tipo){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsMailPersonal)= :mailPersonal ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId != :personaId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("mailPersonal",entidad.getPrsMailPersonal().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintMailPersonaXIdentificacion(Persona entidad, String cedula){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsMailPersonal) = :mailPersonal ");
			sbSql.append(" and p.prsIdentificacion = :cedula ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("mailPersonal",entidad.getPrsMailPersonal().toUpperCase());
			q.setParameter("cedula",cedula);
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintIdenficadorUsuario(Usuario entidad, int tipo){
		boolean retorno = false;
		Usuario usuAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select u from Usuario u where ");
			sbSql.append(" upper(u.usrIdentificacion)= :identificacion ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND u.usrId != :usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",entidad.getUsrIdentificacion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getUsrId());
			}
			usuAux = (Usuario)q.getSingleResult();
		}catch(NoResultException nre){
			usuAux = null;
		}catch(NonUniqueResultException nure){
			usuAux = new Usuario();
		}
		
		if(usuAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	
	/**
	 * Metodo que verifica la existencia de un nick en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintNickUsuario(Usuario entidad, int tipo){
		boolean retorno = false;
		Usuario usuAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select u from Usuario u where ");
			sbSql.append(" upper(u.usrNick)= :nickName ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND u.usrId != :usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("nickName",entidad.getUsrNick().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getUsrId());
			}
			usuAux = (Usuario)q.getSingleResult();
		}catch(NoResultException nre){
			usuAux = null;
		}catch(NonUniqueResultException nure){
			usuAux = new Usuario(-99);
		}
		
		if(usuAux==null){
			retorno = true;
		}
		return retorno;
	}
	
	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la ficha de inscripcion de un estudiante nuevo directo a carrera
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarRegistroPregrado(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
		String retorno = null;
		
		
		try {
			session.getUserTransaction().begin();
			
			
			int contador = 0;
			for (RegistroDto itemRegistro : listRegistroDto) {
	
				//Busco si existe la persona
				Persona AuxPersona= new Persona();
				AuxPersona= servPersona.buscarPersonaPorIdentificacion(itemRegistro.getPrsIdentificacion());				
				if(AuxPersona==null){
					//busco el período académico activo
					PeriodoAcademico pracActivo= new PeriodoAcademico();
					pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//creo el objeto para instertar la persona
					Persona persona = new Persona();
					//creo el objeto para insertar el usuario
					Usuario usuario = new Usuario();
					Rol rolEstudiante= new Rol();
					//creo el objeto para insertar la ficha inscripcion
					FichaInscripcion fichaInscripcion = new FichaInscripcion();
					
				persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
				persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de persona ya existe.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
				persona.setPrsSexo(itemRegistro.getPrsSexo());
				persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
				persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
				persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
				//validacion de constraint de mail personal
				
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El mail personal se encuentra ya registrado.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				
				}	
				
				usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
				usuario.setUsrFechaCreacion(fechaHoy);
				usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
				usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
				usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion de usuario
				if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de usuario ya existe.");
					retorno= retornoAux.toString()  ;
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				}
				String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
				usuario.setUsrNick(partes[0]);
				//validacion de constraint de nick
				if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El nick de usuario ya existe.");
					retorno= retornoAux.toString()  ;
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
						
				ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
				if(configuracionCarrera==null){
				ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarreraSiiu());
				if(configuracionCarrera2!=null){
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
					}else{
						retorno= null;
						StringBuilder retornoAux= new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
						retorno= retornoAux.toString()  ;
						throw new PersonaValidacionException(" ");
						
					}
				}else{
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
				}
				
				fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE); //Ficha inscripción activa por ser DIRECTO A CARRERA
				fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE); //Tipo ficha inscripcion normal
				
				fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE INSCRITO DIRECTO A CARRERA CON AUTORIZACIÓN");
				fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
			   //PERIODO ACADÉMICO  PREGRADO
				fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
			
				fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
				fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
				fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
				fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
				fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
				fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
				fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.VIGENTE_VALUE);
				fichaInscripcion.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE);  //MQ: 18 jul 2019
				fichaInscripcion.setFcinNotaCorteId(itemRegistro.getFcinNotaCorteId());  //MQ: 18 jul 2019
				 
				//busco el rol de estudiante PREGRADO
				 rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD_PREGRADO);
				
				 
				
				//*************************************************************
				//************************** INSERCIONES **********************
				//*************************************************************
				em.persist(persona);
				usuario.setUsrPersona(persona);
				em.persist(usuario);
				UsuarioRol usuarioRol = new UsuarioRol();
				usuarioRol.setUsroRol(rolEstudiante);
				usuarioRol.setUsroUsuario(usuario);
				usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(usuarioRol);
				
				fichaInscripcion.setFcinUsuarioRol(usuarioRol);
				em.persist(fichaInscripcion);
				
				//Creo Ficha Estudiante
				 FichaEstudiante fichaEstudiante = new FichaEstudiante();
				 fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				 fichaEstudiante.setFcesPersona(persona);
				 fichaEstudiante.setFcesFichaInscripcion(fichaInscripcion);
				 em.persist(fichaEstudiante);
				
				
				contador++;
				   if(contador == 30){
				    	em.flush();
				     	em.clear();
					   contador = 0;
			    	}
				//FIN CREACION SINO EXISTE PERSONA
				   
				   
				} else{ 
									
					//BUSCO PERIODO ACTIVO
					PeriodoAcademico pracActivo= new PeriodoAcademico();
					pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					//FECHA ACTUAL
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//busco todas las fichas inscripción en la carrera
					List<FichaInscripcionDto> auxListaFichaInscripcion = new ArrayList<>();
					auxListaFichaInscripcion= servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE)	;
					
					if(auxListaFichaInscripcion==null|| auxListaFichaInscripcion.size()<=0)	{// NO TIENE FICHA INSCRIPCION ALGUNA EN LA CARRERA
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcion = new FichaInscripcion();
						
						Usuario usuarioAux = new Usuario();
					try{//BUSCO USUARIO DE LA PERSONA	
						usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
					}catch (UsuarioNoEncontradoException e){
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Persona existente no tiene usuario creado");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
						
					}catch (UsuarioException e){
						retorno = null;
					StringBuilder retornoAux = new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-Error desconocido al buscar usuario");
					retorno = retornoAux.toString();
					throw new PersonaValidacionException(" ");	 							
					}
						
						
						 UsuarioRol	usuarioRolAux = new UsuarioRol();
						if(usuarioAux!=null){
							try{//BUSCO USUARIO ROL DE PREGRADO
							usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
							}catch ( UsuarioRolNoEncontradoException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
																								
							}catch ( UsuarioRolException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
							} 
							 
						if(usuarioRolAux!=null){
						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
						   //BUSCO CONFIGURACION CARRERA CON ID_CARRERA Y SEXO
							ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
							
							if(configuracionCarrera==null){//Si no se encontro cncr, se buscar sin sexo_id
							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
							if(configuracionCarrera2!=null){
							fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
							}else{
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
								
							}
							
							}else{
							fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
							}
												
							
							fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE INSCRITO DIRECTO A CARRERA CON AUTORIZACIÓN-NO NUEVO EN SIIU ");
							
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
							fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
							fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
						
							fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
							
							fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
							fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
							fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
							fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
							fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
							fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
							fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
							fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.VIGENTE_VALUE);
							fichaInscripcion.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE);  //MQ: 18 jul 2019
							fichaInscripcion.setFcinNotaCorteId(itemRegistro.getFcinNotaCorteId());  //MQ: 18 jul 2019
							
							fichaInscripcion.setFcinUsuarioRol(usuarioRolPersis);
							
							em.persist(fichaInscripcion);
					            
							//Creo Ficha Estudiante
							 FichaEstudiante fichaEstudiante = new FichaEstudiante();
							 fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
							 fichaEstudiante.setFcesPersona(AuxPersona);
							 fichaEstudiante.setFcesFichaInscripcion(fichaInscripcion);
							 em.persist(fichaEstudiante);
							
								
								contador++;
								if(contador == 30){
									em.flush();
									em.clear();
									contador = 0;
								}
							
						  }
				
					   }
						
					}	else if(auxListaFichaInscripcion.size()>=1){// TIENE UNA O MAS FICHAS INSCRIPCION EN LA CARRERA
						
						
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-El estudiante ya se encuentra inscrito en la carrera");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
						
//						//BUSCO FICHA INSCRIPCION  CON MAYOR ID EN LA CARRERA, de todas las fcin
//						 FichaInscripcionDto fcinExistenteDtoAux= new FichaInscripcionDto(); 
//						   try {
//							fcinExistenteDtoAux = servFichaInscripcionDto.buscarFichaInscripcionXIdentificacionXCarreraXIdMayor(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu());
//						   } catch (FichaInscripcionDtoException e) {
//							retorno = null;
//							StringBuilder retornoAux = new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Error desconocido al buscar ficha_inscripcion Id Mayor, comuniquese con el administrador del sistema");
//							retorno = retornoAux.toString();
//							throw new PersonaValidacionException(" ");
//					    	}
//						
//						//Busco alguna ficha inscripcion creada en este periodo
//						FichaInscripcionDto auxFichaInscripcionPeriodoActivo= new FichaInscripcionDto();
//						auxFichaInscripcionPeriodoActivo=servFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraxPeriodo(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), pracActivo.getPracId());
//						
//						
//			if((auxFichaInscripcionPeriodoActivo==null)){ //NO TIENE FCIN EN ESTE PERIODO
//					
//						//creo el objeto para insertar la ficha inscripcion
//						FichaInscripcion fichaInscripcionNuevo = new FichaInscripcion();
//						Usuario usuarioAux = new Usuario();
// 						try{
//						 usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
// 						}catch (UsuarioNoEncontradoException e){
//								retorno = null;
//								StringBuilder retornoAux = new StringBuilder();
//								retornoAux.append(itemRegistro.getPrsIdentificacion());
//								retornoAux.append("-Persona existente no tiene usuario creado");
//								retorno = retornoAux.toString();
//								throw new PersonaValidacionException(" ");
// 							
// 						}catch (UsuarioException e){
// 							retorno = null;
//							StringBuilder retornoAux = new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Error desconocido al buscar usuario");
//							retorno = retornoAux.toString();
//							throw new PersonaValidacionException(" ");	 							
// 						}
// 						
//						
//						UsuarioRol	usuarioRolAux= new UsuarioRol();
//												
//						if(usuarioAux!=null){
//							try{
//								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
//							}catch ( UsuarioRolNoEncontradoException e){
//								retorno= null;
//								StringBuilder retornoAux= new StringBuilder();
//								retornoAux.append(itemRegistro.getPrsIdentificacion());
//								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
//								retorno= retornoAux.toString()  ;
//								throw new PersonaValidacionException(" ");
//								
//							}catch ( UsuarioRolException e){
//								retorno= null;
//								StringBuilder retornoAux= new StringBuilder();
//								retornoAux.append(itemRegistro.getPrsIdentificacion());
//								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
//								retorno= retornoAux.toString()  ;
//								throw new PersonaValidacionException(" ");
//							} 
//						if(usuarioRolAux!=null){
//						
//						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
//						
//				
//							   //Carrera 
//							ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
//							
//							if(configuracionCarrera==null){
//							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
//							
//							if(configuracionCarrera2!=null){
//								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera2);
//								}else{
//									retorno= null;
//									StringBuilder retornoAux= new StringBuilder();
//									retornoAux.append(itemRegistro.getPrsIdentificacion());
//									retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
//									retorno= retornoAux.toString()  ;
//									throw new PersonaValidacionException(" ");
//									
//								}
//							
//							}else{
//								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera);
//							}
//							
//								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE INSCRITO DIRECTO A CARRERA CON AUTORIZACIÓN-NUEVA FCIN EN SIIU");
//								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
//								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
//								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
//								
//								
//							fichaInscripcionNuevo.setFcinPeriodoAcademico(pracActivo);
//							fichaInscripcionNuevo.setFcinFechaInscripcion(fechaHoy);
//							fichaInscripcionNuevo.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
//							fichaInscripcionNuevo.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
//							fichaInscripcionNuevo.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
//							fichaInscripcionNuevo.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // para carreras SiiU 
//							fichaInscripcionNuevo.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
//							fichaInscripcionNuevo.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
//							 
//							//*************************************************************
//							//************************** INSERCIONES **********************
//							//*************************************************************
//							
//							fichaInscripcionNuevo.setFcinUsuarioRol(usuarioRolPersis);
//								em.persist(fichaInscripcionNuevo);
//							List<FichaEstudiante> auxListaFinalFichasEstudiante = new ArrayList<>();
//										  //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION en la carrera
//										for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
//											List<FichaEstudiante> auxListaFichasEstudiante = new ArrayList<>();
//											auxListaFichasEstudiante= servFichaEstudiante.listarPorFcinId(fichaInscripcionDto.getFcinId());
//                                            if	((auxListaFichasEstudiante!=null)&&(auxListaFichasEstudiante.size()>0)){
//                                            	
//                                            	for (FichaEstudiante fichaEstudiante : auxListaFichasEstudiante) {
//                                            		auxListaFinalFichasEstudiante.add(fichaEstudiante);
//												}
//                                            	
//                                            }
//										}
//							        
//										if((auxListaFinalFichasEstudiante!=null)&&(auxListaFinalFichasEstudiante.size()>0)){  // Existe alguna fces
//										if(auxListaFinalFichasEstudiante.size()==1){ // Ya existe una ficha estudiante en la carrera
//											retorno= null;
//											StringBuilder retornoAux= new StringBuilder();
//											retornoAux.append(itemRegistro.getPrsIdentificacion());
//											retornoAux.append("-Persona presenta varias una ficha estudiante en la carrera, comuniquese con el administrador del sistema");
//											retorno= retornoAux.toString()  ;
//											throw new PersonaValidacionException(" ");
//												
//											}else if(auxListaFinalFichasEstudiante.size()>1){  //VARIAS FICHAS ESTUDIANTE EN LA CARRERA 
//												
//												retorno= null;
//												StringBuilder retornoAux= new StringBuilder();
//												retornoAux.append(itemRegistro.getPrsIdentificacion());
//												retornoAux.append("-Persona presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema");
//												retorno= retornoAux.toString()  ;
//												throw new PersonaValidacionException(" ");
//												
//											   }
//										}else{ //NO TIENE NINGUNA FICHA ESTUDIANTE, pero si tiene fcin inscripcion anterior  en otro periodo
//													   Integer  auxFcinAnterior =null;
//											           auxFcinAnterior=fcinExistenteDtoAux.getFcinId();//guardamos el id de fcin anterior
//											           FichaInscripcion fcinExistente=em.find(FichaInscripcion.class,fcinExistenteDtoAux.getFcinId());
//													   fcinExistente.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
//											           FichaInscripcion fichaInscripcionNueva= em.find(FichaInscripcion.class,fichaInscripcionNuevo.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
//											           fichaInscripcionNueva.setFcinFcinAnteriorId(auxFcinAnterior); 
//											           
//											         //Creo Ficha Estudiante
//														FichaEstudiante fichaEstudiante = new FichaEstudiante();
//														 fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
//														 fichaEstudiante.setFcesPersona(AuxPersona);
//														 fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionNueva);
//														 em.persist(fichaEstudiante);
//												   
//										}
//								
//								contador++;
//								if(contador == 30){
//									em.flush();
//									em.clear();
//									contador = 0;
//								}
//											
//								} 
//								
//							} 
//					   }else{
//						   
//						   retorno = null;
//							StringBuilder retornoAux = new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Persona se encuentra ya registrada en este período académico.");
//							retorno = retornoAux.toString();
//							throw new PersonaValidacionException(" ");
//							
//						}
						
				   }
					
				}
				
			}
			
		session.getUserTransaction().commit();

			retorno=null;
		} catch (PersonaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.persona", retorno)));
     	    //throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(retorno)));
     	    
		} catch (UsuarioValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		//	throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario")));
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario", retorno)));
			
		}
		catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.exception")));
		}	
		
		return retorno;
	
	}
	
	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion  nota del examen de ubicación
	 * para generar la ficha de inscripcion de un estudiante nuevo a idiomas de a acuerdo a la nota del examen de ubicación
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarRegistroUbicacionIdiomas(List<RegistroDto> listRegistroDto, PlanificacionCronograma planificacionCronograma) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
		String retorno = null;
		
		
		try {
			session.getUserTransaction().begin();
			
			
			int contador = 0;
			for (RegistroDto itemRegistro : listRegistroDto) {
	
				//Busco si existe la persona
				Persona AuxPersona= new Persona();
				AuxPersona= servPersona.buscarPersonaPorIdentificacion(itemRegistro.getPrsIdentificacion());				
				if(AuxPersona==null){
					//busco el período académico activo
					
					
					PeriodoAcademico pracIdiomasActivo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
				
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//creo el objeto para instertar la persona
					Persona persona = new Persona();
					//creo el objeto para insertar el usuario
					Usuario usuario = new Usuario();
					Rol rolEstudiante= new Rol();
					//creo el objeto para insertar la ficha inscripcion
					FichaInscripcion fichaInscripcion = new FichaInscripcion();
					
				persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
				persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de persona ya existe.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
				persona.setPrsSexo(itemRegistro.getPrsSexo());
				persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
				persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
				persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
				//validacion de constraint de mail personal
				
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El mail personal se encuentra ya registrado.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				
				}	
				
				usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
				usuario.setUsrFechaCreacion(fechaHoy);
				usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
				usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
				usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion de usuario
				if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de usuario ya existe.");
					retorno= retornoAux.toString()  ;
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				}
				String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
				usuario.setUsrNick(partes[0]);
				//validacion de constraint de nick
				if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El nick de usuario ya existe.");
					retorno= retornoAux.toString()  ;
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
						
				ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
				if(configuracionCarrera==null){
				ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarreraSiiu());
				if(configuracionCarrera2!=null){
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
					}else{
						retorno= null;
						StringBuilder retornoAux= new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
						retorno= retornoAux.toString()  ;
						throw new PersonaValidacionException(" ");
						
					}
				}else{
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
				}
				
				fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE); //Ficha inscripción activa por ser DIRECTO A CARRERA de IDIOMA
				fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE); //Tipo ficha inscripcion normal
				
				fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE INSCRITO POR PRUEBA DE UBICACION IDIOMAS");
				fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
					
			   //PERIODO ACADÉMICO  IDIOMAS
				if(pracIdiomasActivo!=null){
					fichaInscripcion.setFcinPeriodoAcademico(pracIdiomasActivo);
					}else{
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe periodo académico activo de tipo idiomas");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
					}
			
				fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
				fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
				//fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
				fichaInscripcion.setFcinNotaUbicacion(  new Float(itemRegistro.getFcinNotaUbicacion().toString())); //Guardo nota de prueba de Ubicacion
				fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // MARCELO:para carreras SiiU tambien, si ingresan a idioma por prueba de ubicación
				fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
				fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
				fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
				//busco el rol de estudiante PREGRADO
				 rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD_PREGRADO);
				
			
				em.persist(persona);
				usuario.setUsrPersona(persona);
				em.persist(usuario);
				UsuarioRol usuarioRol = new UsuarioRol();
				usuarioRol.setUsroRol(rolEstudiante);
				usuarioRol.setUsroUsuario(usuario);
				usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(usuarioRol);
				
				fichaInscripcion.setFcinUsuarioRol(usuarioRol);
				em.persist(fichaInscripcion);
				
				//Creo Ficha Estudiante
				 FichaEstudiante fichaEstudiante = new FichaEstudiante();
				 fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				 fichaEstudiante.setFcesPersona(persona);
				 fichaEstudiante.setFcesFichaInscripcion(fichaInscripcion);
				 em.persist(fichaEstudiante);
				
					if(itemRegistro.getListaMaterias()!=null){ 
					//creacion de la ficha matricula
						FichaMatricula fichaMatricula = new FichaMatricula();
						fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
						//Verificar nivel
						fichaMatricula.setFcmtNivelUbicacion(itemRegistro.getFcmtNivelUbicacion());
						
						fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
						fichaMatricula.setFcmtTipo(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
			            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
						fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
									
						fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
						
						//OJO:  BUSCAR  MQ  9 julio 2018
						fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
												
						fichaMatricula.setFcmtPracId(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
						
						ConfiguracionCarrera configuracionCarreraAux=	em.find(ConfiguracionCarrera.class, fichaInscripcion.getFcinConfiguracionCarrera().getCncrId());
						
						if(configuracionCarreraAux.getCncrModalidad()!=null){
						fichaMatricula.setFcmtModalidad(configuracionCarreraAux.getCncrModalidad().getMdlId());
						}
									
					    em.persist(fichaMatricula);
							
						//creo comprobante pago 
						//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
						ComprobantePago comprobantePago = new ComprobantePago();
						comprobantePago.setCmpaFichaMatricula(fichaMatricula);
						comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
						comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
						comprobantePago.setCmpaTipo(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE); //según el tipo de matricula
						int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
						if(o==0){
							comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
						}
					    em.persist(comprobantePago);
						
						//creo un detalle_matricula y un record_estudiante por materia 
						List<MateriaDto> listaMateriasAux = new ArrayList<>();
						listaMateriasAux.addAll(itemRegistro.getListaMaterias());
							
						for (MateriaDto itemMaterias : listaMateriasAux) {

							
							//BUSCO MALLA_CURRICULAR_PARALELO POR EL ID DE MLCRPR QUE VIENE EN CADA MATERIA 
							MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
							
							//detalle matricula
							DetalleMatricula detalleMatricula = new DetalleMatricula();
							detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
							detalleMatricula.setDtmtComprobantePago(comprobantePago);
							detalleMatricula.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); // Si existe materia se aprueba con primera en Idiomas
							detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
							detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
							int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
							if(r==0){
								//arancel nulo y seteo el valor parcial en cero
								detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
							}else{
								//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
							}
						   	em.persist(detalleMatricula);
								
							//record
							RecordEstudiante recordEstudiante = new RecordEstudiante();
							recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
							recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
							recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);//estado aprobado en caso de Idiomas
							recordEstudiante.setRcesModoAprobacion(RecordEstudianteConstantes.MODO_APROBACION_PRUEBA_UBICACION_VALUE);// modo que se aprueba Idiomas
						 	em.persist(recordEstudiante);
								
							//registro de notas
//							Calificacion calificacion = new Calificacion();
//							calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
//							calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
//							calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
//							calificacion.setRecordEstudiante(recordEstudiante);
//						 	em.persist(calificacion);
						}
							
						//creo la gratuidad
						TipoGratuidad tipoGratuidad = new TipoGratuidad();
						tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
						
						Gratuidad gratuidad = new Gratuidad();
						gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
						gratuidad.setGrtFichaEstudiante(fichaEstudiante);
						gratuidad.setGrtTipoGratuidad(tipoGratuidad);
						gratuidad.setGrtFichaMatricula(fichaMatricula);
					  	em.persist(gratuidad);
					  	
					  	//creo Control del proceso
//					  	ControlProceso controlProceso = new ControlProceso();
//					  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
//					  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
//					  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
//					  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
//					  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
//					  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
//					  	StringBuilder detalleAux= new StringBuilder();
//					  	detalleAux.append(JdbcConstantes.FCIN_ID);
//					  	detalleAux.append(":");
//					  	detalleAux.append(fichaInscripcionAux.getFcinId());
//					  	detalleAux.append(";");
//					  	detalleAux.append(JdbcConstantes.FCES_ID);
//					  	detalleAux.append(":");
//					  	detalleAux.append(fichaEstudiante.getFcesId());
//					  	detalleAux.append(";");
//					  	detalleAux.append(JdbcConstantes.FCMT_ID);
//					  	detalleAux.append(":");
//					  	detalleAux.append(fichaMatricula.getFcmtId());
//					  	detalleAux.append(";");
//					  	detalleAux.append(JdbcConstantes.CMPA_ID);
//					  	detalleAux.append(":");
//					  	detalleAux.append(comprobantePago.getCmpaId());
//					  	detalleAux.append(";");
//					  	detalleAux.append(JdbcConstantes.GRT_ID);
//					  	detalleAux.append(":");
//					  	detalleAux.append(gratuidad.getGrtId());
//							
//					  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
//					  	
//						StringBuilder observacionAux= new StringBuilder();
//						observacionAux.append("actualizar-");
//						observacionAux.append(JdbcConstantes.FCIN_ID);
//						observacionAux.append("-");
//						observacionAux.append(fichaInscripcionAux.getFcinId());
//						observacionAux.append("; crear-");
//						observacionAux.append(JdbcConstantes.FCES_ID);
//						observacionAux.append("-");
//						observacionAux.append(fichaEstudiante.getFcesId());
//						observacionAux.append("; crear-");
//						observacionAux.append(JdbcConstantes.FCMT_ID);
//						observacionAux.append("-");
//						observacionAux.append(fichaMatricula.getFcmtId());
//						observacionAux.append("; crear-");
//						observacionAux.append(JdbcConstantes.CMPA_ID);
//						observacionAux.append("-");
//						observacionAux.append(comprobantePago.getCmpaId());
//						observacionAux.append("; crear-");
//						observacionAux.append(JdbcConstantes.GRT_ID);
//						observacionAux.append("-");
//						observacionAux.append(gratuidad.getGrtId());
//					  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
//					  	em.persist(controlProceso);
					}//FIN SI EXISTE LISTA DE MATERIAS
					
				contador++;
				   if(contador == 30){
				    	em.flush();
				     	em.clear();
					   contador = 0;
			    	}
				//FIN CREACION SINO EXISTE PERSONA
				   
				} else{ //Si EXISTE PERSONA
									
					//BUSCO PERIODO ACTIVO DE IDIOMAS
					PeriodoAcademico pracIdiomasActivo= servRasPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					//FECHA ACTUAL
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//busco todas las fichas inscripción en la carrera
					List<FichaInscripcionDto> auxListaFichaInscripcion = new ArrayList<>();
					auxListaFichaInscripcion= servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE)	;
					
					if(auxListaFichaInscripcion==null|| auxListaFichaInscripcion.size()<=0)	{// NO TIENE FICHA INSCRIPCION ALGUNA
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcion = new FichaInscripcion();
						
						Usuario usuarioAux = new Usuario();
					try{//BUSCO USUARIO DE LA PERSONA	
						usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
					}catch (UsuarioNoEncontradoException e){
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Persona existente no tiene usuario creado");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
						
					}catch (UsuarioException e){
						retorno = null;
					StringBuilder retornoAux = new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-Error desconocido al buscar usuario");
					retorno = retornoAux.toString();
					throw new PersonaValidacionException(" ");	 							
					}
						
						
						 UsuarioRol	usuarioRolAux = new UsuarioRol();
						if(usuarioAux!=null){
							try{//BUSCO USUARIO ROL DE PREGRADO
							usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
							}catch ( UsuarioRolNoEncontradoException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
																								
							}catch ( UsuarioRolException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
							} 
							 
						if(usuarioRolAux!=null){
						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
						   //BUSCO CONFIGURACION CARRERA CON ID_CARRERA Y SEXO
							ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
							
							if(configuracionCarrera==null){//Si no se encontro cncr, se buscar sin sexo_id
							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
							if(configuracionCarrera2!=null){
							fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
							}else{
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
								
							}
							
							}else{
							fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
							}
							
							fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE INSCRITO POR PRUEBA DE UBICACION-NO NUEVO EN SIIU ");
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
							fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE);
							fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
							
							
							   //PERIODO ACADÉMICO  IDIOMAS
								if(pracIdiomasActivo!=null){
									fichaInscripcion.setFcinPeriodoAcademico(pracIdiomasActivo);
									}else{
										retorno = null;
										StringBuilder retornoAux = new StringBuilder();
										retornoAux.append(itemRegistro.getPrsIdentificacion());
										retornoAux.append("-No existe periodo académico activo de tipo idiomas");
										retorno = retornoAux.toString();
										throw new PersonaValidacionException(" ");
									}
							
							fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
							fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
							fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
							//fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
							fichaInscripcion.setFcinNotaUbicacion(  new Float(itemRegistro.getFcinNotaUbicacion().toString())); //Guardo nota de prueba de Ubicacion
							fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
							fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
							fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
							fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
							
							
							fichaInscripcion.setFcinUsuarioRol(usuarioRolPersis);
							em.persist(fichaInscripcion);
					            
							//Creo Ficha Estudiante
							 FichaEstudiante fichaEstudiante = new FichaEstudiante();
							 fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
							 fichaEstudiante.setFcesPersona(AuxPersona);
							 fichaEstudiante.setFcesFichaInscripcion(fichaInscripcion);
							 em.persist(fichaEstudiante);
							 
							 if(itemRegistro.getListaMaterias()!=null){ 
							 
								//creacion de la ficha matricula
									FichaMatricula fichaMatricula = new FichaMatricula();
									fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
									//Verificar nivel
									fichaMatricula.setFcmtNivelUbicacion(itemRegistro.getFcmtNivelUbicacion());
									
									fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
									fichaMatricula.setFcmtTipo(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
						            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
									fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
												
									fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
									
									//OJO:  BUSCAR  MQ  9 julio 2018
									fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
															
									fichaMatricula.setFcmtPracId(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
									
									ConfiguracionCarrera configuracionCarreraAux=	em.find(ConfiguracionCarrera.class, fichaInscripcion.getFcinConfiguracionCarrera().getCncrId());
									
									if(configuracionCarreraAux.getCncrModalidad()!=null){
									fichaMatricula.setFcmtModalidad(configuracionCarreraAux.getCncrModalidad().getMdlId());
									}
												
								    em.persist(fichaMatricula);
										
									//creo comprobante pago 
									//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
									ComprobantePago comprobantePago = new ComprobantePago();
									comprobantePago.setCmpaFichaMatricula(fichaMatricula);
									comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
									comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
									comprobantePago.setCmpaTipo(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE); //según el tipo de matricula
									int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
									if(o==0){
										comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
									}
								    em.persist(comprobantePago);
									
									//creo un detalle_matricula y un record_estudiante por materia 
										
									
							          
										List<MateriaDto> listaMateriasAux = new ArrayList<>();
										listaMateriasAux.addAll(itemRegistro.getListaMaterias());
										
									for (MateriaDto itemMaterias : listaMateriasAux) {

										
										//BUSCO MALLA_CURRICULAR_PARALELO POR EL ID DE MLCRPR QUE VIENE EN CADA MATERIA 
										MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
										
										//detalle matricula
										DetalleMatricula detalleMatricula = new DetalleMatricula();
										detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
										detalleMatricula.setDtmtComprobantePago(comprobantePago);
										detalleMatricula.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); // Si existe materia se aprueba con primera en Idiomas
										detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
										detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
										int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
										if(r==0){
											//arancel nulo y seteo el valor parcial en cero
											detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
										}else{
											//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
										}
									   	em.persist(detalleMatricula);
											
										//record
										RecordEstudiante recordEstudiante = new RecordEstudiante();
										recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
										recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
										recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);//estado aprobado en caso de Idiomas
										recordEstudiante.setRcesModoAprobacion(RecordEstudianteConstantes.MODO_APROBACION_PRUEBA_UBICACION_VALUE);// modo que se aprueba Idiomas
									 	em.persist(recordEstudiante);
											
										//registro de notas
//										Calificacion calificacion = new Calificacion();
//										calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
//										calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
//										calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
//										calificacion.setRecordEstudiante(recordEstudiante);
//									 	em.persist(calificacion);
									}
										
									//creo la gratuidad
									TipoGratuidad tipoGratuidad = new TipoGratuidad();
									tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
									
									Gratuidad gratuidad = new Gratuidad();
									gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
									gratuidad.setGrtFichaEstudiante(fichaEstudiante);
									gratuidad.setGrtTipoGratuidad(tipoGratuidad);
									gratuidad.setGrtFichaMatricula(fichaMatricula);
								  	em.persist(gratuidad);
								  	
								  	//creo Control del proceso
//								  	ControlProceso controlProceso = new ControlProceso();
//								  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
//								  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
//								  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
//								  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
//								  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
//								  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
//								  	StringBuilder detalleAux= new StringBuilder();
//								  	detalleAux.append(JdbcConstantes.FCIN_ID);
//								  	detalleAux.append(":");
//								  	detalleAux.append(fichaInscripcionAux.getFcinId());
//								  	detalleAux.append(";");
//								  	detalleAux.append(JdbcConstantes.FCES_ID);
//								  	detalleAux.append(":");
//								  	detalleAux.append(fichaEstudiante.getFcesId());
//								  	detalleAux.append(";");
//								  	detalleAux.append(JdbcConstantes.FCMT_ID);
//								  	detalleAux.append(":");
//								  	detalleAux.append(fichaMatricula.getFcmtId());
//								  	detalleAux.append(";");
//								  	detalleAux.append(JdbcConstantes.CMPA_ID);
//								  	detalleAux.append(":");
//								  	detalleAux.append(comprobantePago.getCmpaId());
//								  	detalleAux.append(";");
//								  	detalleAux.append(JdbcConstantes.GRT_ID);
//								  	detalleAux.append(":");
//								  	detalleAux.append(gratuidad.getGrtId());
//										
//								  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
//								  	
//									StringBuilder observacionAux= new StringBuilder();
//									observacionAux.append("actualizar-");
//									observacionAux.append(JdbcConstantes.FCIN_ID);
//									observacionAux.append("-");
//									observacionAux.append(fichaInscripcionAux.getFcinId());
//									observacionAux.append("; crear-");
//									observacionAux.append(JdbcConstantes.FCES_ID);
//									observacionAux.append("-");
//									observacionAux.append(fichaEstudiante.getFcesId());
//									observacionAux.append("; crear-");
//									observacionAux.append(JdbcConstantes.FCMT_ID);
//									observacionAux.append("-");
//									observacionAux.append(fichaMatricula.getFcmtId());
//									observacionAux.append("; crear-");
//									observacionAux.append(JdbcConstantes.CMPA_ID);
//									observacionAux.append("-");
//									observacionAux.append(comprobantePago.getCmpaId());
//									observacionAux.append("; crear-");
//									observacionAux.append(JdbcConstantes.GRT_ID);
//									observacionAux.append("-");
//									observacionAux.append(gratuidad.getGrtId());
//								  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
//								  	em.persist(controlProceso);
							 
							 }//  FIN  SI EXISTE LISTA DE MATERIAS
							 
							 
							 
							 
								
								contador++;
								if(contador == 30){
									em.flush();
									em.clear();
									contador = 0;
								}
							
						  }
				
					   }
						
					}	else if(auxListaFichaInscripcion.size()>=1){
						
						//OPCION1: ERROR UN ESTUDIANTE NO PUEDE INGRESAR POR EXAMEN DE UBICACION SI YA ESTA EN UN IDIOMA
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Error el estudiante ya se encuentra inscrito en el idioma, no debe ingresar por prueba de ubicación.");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");
						
						
						// OPSCION2: TIENE UNA O MAS FICHAS INSCRIPCION
						//BUSCO FICHA INSCRIPCION  CON MAYOR ID EN LA CARRERA, de todas las fcin
//						 FichaInscripcionDto fcinExistenteDtoAux= new FichaInscripcionDto(); 
//						   try {
//							fcinExistenteDtoAux = servFichaInscripcionDto.buscarFichaInscripcionXIdentificacionXCarreraXIdMayor(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu());
//						   } catch (FichaInscripcionDtoException e) {
//							retorno = null;
//							StringBuilder retornoAux = new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Error desconocido al buscar ficha_inscripcion Id Mayor, comuniquese con el administrador del sistema");
//							retorno = retornoAux.toString();
//							throw new PersonaValidacionException(" ");
//					    	}
//						
//						//Busco alguna ficha inscripcion creada en este periodo
//						FichaInscripcionDto auxFichaInscripcionPeriodoActivo= new FichaInscripcionDto();
//						auxFichaInscripcionPeriodoActivo=servFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraxPeriodo(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), pracIdiomasActivo.getPracId());
//						
//						
//			if((auxFichaInscripcionPeriodoActivo==null)){ //NO TIENE FCIN EN ESTE PERIODO
//					
//						//creo el objeto para insertar la ficha inscripcion
//						FichaInscripcion fichaInscripcionNuevo = new FichaInscripcion();
//						Usuario usuarioAux = new Usuario();
// 						try{
//						 usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
// 						}catch (UsuarioNoEncontradoException e){
//								retorno = null;
//								StringBuilder retornoAux = new StringBuilder();
//								retornoAux.append(itemRegistro.getPrsIdentificacion());
//								retornoAux.append("-Persona existente no tiene usuario creado");
//								retorno = retornoAux.toString();
//								throw new PersonaValidacionException(" ");
// 							
// 						}catch (UsuarioException e){
// 							retorno = null;
//							StringBuilder retornoAux = new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Error desconocido al buscar usuario");
//							retorno = retornoAux.toString();
//							throw new PersonaValidacionException(" ");	 							
// 						}
// 						
//						
//						UsuarioRol	usuarioRolAux= new UsuarioRol();
//												
//						if(usuarioAux!=null){
//							try{
//								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
//							}catch ( UsuarioRolNoEncontradoException e){
//								retorno= null;
//								StringBuilder retornoAux= new StringBuilder();
//								retornoAux.append(itemRegistro.getPrsIdentificacion());
//								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
//								retorno= retornoAux.toString()  ;
//								throw new PersonaValidacionException(" ");
//								
//							}catch ( UsuarioRolException e){
//								retorno= null;
//								StringBuilder retornoAux= new StringBuilder();
//								retornoAux.append(itemRegistro.getPrsIdentificacion());
//								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
//								retorno= retornoAux.toString()  ;
//								throw new PersonaValidacionException(" ");
//							} 
//						if(usuarioRolAux!=null){
//						
//						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
//						
//				
//							   //Carrera 
//							ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
//							
//							if(configuracionCarrera==null){
//							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
//							
//							if(configuracionCarrera2!=null){
//								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera2);
//								}else{
//									retorno= null;
//									StringBuilder retornoAux= new StringBuilder();
//									retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
//									retorno= retornoAux.toString()  ;
//									throw new PersonaValidacionException(" ");
//									
//								}
//							
//							}else{
//								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera);
//							}
//							
//								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE INSCRITO DIRECTO A CARRERA CON AUTORIZACIÓN-NUEVA FCIN EN SIIU");
//								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
//								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
//								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
//								
//								
//							fichaInscripcionNuevo.setFcinPeriodoAcademico(pracIdiomasActivo);
//							fichaInscripcionNuevo.setFcinFechaInscripcion(fechaHoy);
//							fichaInscripcionNuevo.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
//							fichaInscripcionNuevo.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
//							fichaInscripcionNuevo.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
//							fichaInscripcionNuevo.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // para carreras SiiU 
//							fichaInscripcionNuevo.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
//							fichaInscripcionNuevo.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
//							 
//							//*************************************************************
//							//************************** INSERCIONES **********************
//							//*************************************************************
//							
//							fichaInscripcionNuevo.setFcinUsuarioRol(usuarioRolPersis);
//								em.persist(fichaInscripcionNuevo);
//							List<FichaEstudiante> auxListaFinalFichasEstudiante = new ArrayList<>();
//										  //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION en la carrera
//										for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
//											List<FichaEstudiante> auxListaFichasEstudiante = new ArrayList<>();
//											auxListaFichasEstudiante= servFichaEstudiante.listarPorFcinId(fichaInscripcionDto.getFcinId());
//                                            if	((auxListaFichasEstudiante!=null)&&(auxListaFichasEstudiante.size()>0)){
//                                            	
//                                            	for (FichaEstudiante fichaEstudiante : auxListaFichasEstudiante) {
//                                            		auxListaFinalFichasEstudiante.add(fichaEstudiante);
//												}
//                                            	
//                                            }
//										}
//							        
//										if((auxListaFinalFichasEstudiante!=null)&&(auxListaFinalFichasEstudiante.size()>0)){  // Existe alguna fces
//										if(auxListaFinalFichasEstudiante.size()==1){ // Ya existe una ficha estudiante en la carrera
//											retorno= null;
//											StringBuilder retornoAux= new StringBuilder();
//											retornoAux.append(itemRegistro.getPrsIdentificacion());
//											retornoAux.append("-Persona presenta varias una ficha estudiante en la carrera, comuniquese con el administrador del sistema");
//											retorno= retornoAux.toString()  ;
//											throw new PersonaValidacionException(" ");
//												
//											}else if(auxListaFinalFichasEstudiante.size()>1){  //VARIAS FICHAS ESTUDIANTE EN LA CARRERA 
//												
//												retorno= null;
//												StringBuilder retornoAux= new StringBuilder();
//												retornoAux.append(itemRegistro.getPrsIdentificacion());
//												retornoAux.append("-Persona presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema");
//												retorno= retornoAux.toString()  ;
//												throw new PersonaValidacionException(" ");
//												
//											   }
//										}else{ //NO TIENE NINGUNA FICHA ESTUDIANTE, pero si tiene fcin inscripcion anterior  en otro periodo
//													   Integer  auxFcinAnterior =null;
//											           auxFcinAnterior=fcinExistenteDtoAux.getFcinId();//guardamos el id de fcin anterior
//											           FichaInscripcion fcinExistente=em.find(FichaInscripcion.class,fcinExistenteDtoAux.getFcinId());
//													   fcinExistente.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
//											           FichaInscripcion fichaInscripcionNueva= em.find(FichaInscripcion.class,fichaInscripcionNuevo.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
//											           fichaInscripcionNueva.setFcinFcinAnteriorId(auxFcinAnterior); 
//											           
//											         //Creo Ficha Estudiante
//														FichaEstudiante fichaEstudiante = new FichaEstudiante();
//														 fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
//														 fichaEstudiante.setFcesPersona(AuxPersona);
//														 fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionNueva);
//														 em.persist(fichaEstudiante);
//												   
//										}
//								
//								contador++;
//								if(contador == 30){
//									em.flush();
//									em.clear();
//									contador = 0;
//								}
//											
//								} 
//								
//							} 
//					   }else{
//						   
//						   retorno = null;
//							StringBuilder retornoAux = new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Persona se encuentra ya registrada en este período académico.");
//							retorno = retornoAux.toString();
//							throw new PersonaValidacionException(" ");
//							
//						}
						
				   }
					
				}
				
			}
			
		session.getUserTransaction().commit();
		//	session.getUserTransaction().rollback();
			retorno=null;
		} catch (PersonaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.persona", retorno)));
     	    //throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(retorno)));
     	    
		} catch (UsuarioValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		//	throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario")));
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario", retorno)));
			
		}
		catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.exception")));
		}	
		
		return retorno;
	
	}
	
	
	
	/**MQ:
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion  POSGRADO
	 * para generar la ficha de inscripcion de un estudiante a homologar desde cero  o crea solo ficha inscripcion si es cambio de carrera.
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarRegistroHomologacionPosgrado(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
	
		String retorno = null;
		
		try {
			session.getUserTransaction().begin();
			
			
			int contador = 0;
			for (RegistroDto itemRegistro : listRegistroDto) {
				
				//*************************************************************
				//********************* VALIDACIONES **************************
				//*************************************************************
				
				
		//ES INGRESO A POSGRADO, REGISTRO AUTOMATICO SIN PERIODO ACADEMICO

				Persona AuxPersona= new Persona();
				AuxPersona= servPersona.buscarPersonaPorIdentificacion(itemRegistro.getPrsIdentificacion());				
				if(AuxPersona==null){
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//creo el objeto para instertar la persona
					Persona persona = new Persona();
					//creo el objeto para insertar el usuario
					Usuario usuario = new Usuario();
					Rol rolEstudiante= new Rol();
					//creo el objeto para insertar la ficha inscripcion
					FichaInscripcion fichaInscripcion = new FichaInscripcion();
					
				persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
				persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de persona ya existe.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
					
					//throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.identificador.persona")));
				}
				persona.setPrsSexo(itemRegistro.getPrsSexo());
				persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
				persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
				persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
				//validacion de constraint de mail personal
				
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El mail personal se encuentra ya registrado.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					//throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.mail.persona")));
				
				}	
				
				usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
				usuario.setUsrFechaCreacion(fechaHoy);
				usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
				usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
				usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion de usuario
				if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de usuario ya existe.");
					retorno= retornoAux.toString()  ;
                  //   throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.identificador.usuario")));
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				}
				String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
				usuario.setUsrNick(partes[0]);
				//validacion de constraint de nick
				if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El nick de usuario ya existe.");
					retorno= retornoAux.toString()  ;
				//	throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.nick.usuario")));
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
				
				//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
			   //	ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
				
				ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
				if(configuracionCarrera==null){
				ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
				if(configuracionCarrera2!=null){
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
					}else{
						retorno= null;
						StringBuilder retornoAux= new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
						retorno= retornoAux.toString()  ;
						throw new PersonaValidacionException(" ");
						
					}
			
			
				}else{
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
				}
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO INGRESO A POSGRADO POR REGISTRO HISTORICO");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
				
			//	fichaInscripcion.setFcinPeriodoAcademico(pracActivo);  // No se ingresa el periodo pues tiene cohortes
				fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
				fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
				fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
				fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
				fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
				fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
				fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
				 
				//busco el rol de estudiante 
			
					 rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUDIANTEPOSGRADO);
				
				//*************************************************************
				//************************** INSERCIONES **********************
				//*************************************************************
				em.persist(persona);
				usuario.setUsrPersona(persona);
				em.persist(usuario);
				UsuarioRol usuarioRol = new UsuarioRol();
				usuarioRol.setUsroRol(rolEstudiante);
				usuarioRol.setUsroUsuario(usuario);
				usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(usuarioRol);
				
				fichaInscripcion.setFcinUsuarioRol(usuarioRol);
				em.persist(fichaInscripcion);
    				
				contador++;
				   if(contador == 30){
				    	em.flush();
				     	em.clear();
					   contador = 0;
			    	}
				//FIN CREACION SINO EXISTE PERSONA POSGRADO
				   
				   
				 } else{//SI EXISTE PERSONA  EN LA BDD
					 
					   List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>(); 
					  auxListaFichaInscripcion=servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
                   //NO DEBE EXISTIR FICHA INSCRIPCION EN EL PROGRAMA
					  if((auxListaFichaInscripcion==null)||(auxListaFichaInscripcion.size()==0)){
						Timestamp fechaHoy = new Timestamp(new Date().getTime());
									
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcionNuevo = new FichaInscripcion();
						Usuario usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
						
						UsuarioRol	usuarioRolAux = new UsuarioRol();
						if(usuarioAux!=null){
						try{	
							
						usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUDIANTEPOSGRADO_VALUE);
						}catch ( UsuarioRolNoEncontradoException e){
//							retorno= null;
//							StringBuilder retornoAux= new StringBuilder();
//							retornoAux.append(itemRegistro.getPrsIdentificacion());
//							retornoAux.append("-Persona existente no tiene usuario_rol de tipo posgrado creado");
//							retorno= retornoAux.toString()  ;
//							throw new PersonaValidacionException(" ");
							//busco el rol de estudiante 
							Rol rolEstudianteAux= new Rol();
							rolEstudianteAux = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUDIANTEPOSGRADO);	
							usuarioRolAux.setUsroRol(rolEstudianteAux);
							usuarioRolAux.setUsroUsuario(usuarioAux);
							usuarioRolAux.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
							em.persist(usuarioRolAux);
							
						}catch ( UsuarioRolException e){
							retorno= null;
							StringBuilder retornoAux= new StringBuilder();
							retornoAux.append(itemRegistro.getPrsIdentificacion());
							retornoAux.append("-Error descoconcido al buscar UsuarioRol de tipo posgrado");
							retorno= retornoAux.toString()  ;
							throw new PersonaValidacionException(" ");
						} 	 
							 
							 
							 
						if(usuarioRolAux!=null){
						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
							ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
							
							if(configuracionCarrera==null){
							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
														
							if(configuracionCarrera2!=null){
								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera2);
								}else{
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
									
								}
							
							
							
							}else{
								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera);
							}
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION INGRESO A POSGRADO POR REGISTRO HISTORICO");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(itemRegistro.getFcinTipoIngreso());
							
					
							fichaInscripcionNuevo.setFcinFechaInscripcion(fechaHoy);
							fichaInscripcionNuevo.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
							fichaInscripcionNuevo.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
							fichaInscripcionNuevo.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
							fichaInscripcionNuevo.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // para carreras SiiU 
							fichaInscripcionNuevo.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
							fichaInscripcionNuevo.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
							fichaInscripcionNuevo.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
							fichaInscripcionNuevo.setFcinUsuarioRol(usuarioRolPersis);
							
								em.persist(fichaInscripcionNuevo);
						
													            	 
							
								contador++;
								if(contador == 30){
									em.flush();
									em.clear();
									contador = 0;
								}
											
								} 
								
							} else {

								retorno = null;
								StringBuilder retornoAux = new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario creado");
								retorno = retornoAux.toString();
								throw new PersonaValidacionException(" ");

							}
					 
					//else de if Tiene ficha inscripcion	
				 } else{
					 retorno= null;
						StringBuilder retornoAux= new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Persona presenta ficha inscripcion en posgrado, comuniquese con el administrador del sistema");
						retorno= retornoAux.toString()  ;
						throw new PersonaValidacionException(" ");
					 
					
				 }
						
						
				 } //FIN ELSE POSGRADOS
					 
			}
			
			session.getUserTransaction().commit();
			retorno=null;
		} catch (PersonaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.persona", retorno)));
     	    //throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(retorno)));
     	    
		} catch (UsuarioValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		//	throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario")));
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario", retorno)));
			
		}
		catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
				
				
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.exception")));
		}	
		
		
		return retorno;
	}
	
	
	/**
	 * Crea usuario, persona, usuario_rol,ficha_inscripcion si el estudiante no existe en el SIIU,
	 * crea solo ficha inscripcion como reingreso si el estudiante ya existe en el SIIU.
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarRegistroManual(RegistroHomologacionDto itemRegistro, Integer tipoIngresoNuevaFcin) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
	
		String retorno = null;
		
		try {
			session.getUserTransaction().begin();
						
							
				//*************************************************************
				//********************* VALIDACIONES **************************
				//*************************************************************
				
			    //BUSCO SI EXISTE LA PERSONA
				Persona AuxPersona= new Persona();
				AuxPersona= servPersona.buscarPersonaPorIdentificacion(itemRegistro.getPrsIdentificacion());				
				
				if(AuxPersona==null){ //No existe la persona
					//busco el período académico activo
					PeriodoAcademico pracActivo= new PeriodoAcademico();
					pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
					//creo el objeto para instertar la persona
					Persona persona = new Persona();
					//creo el objeto para insertar el usuario
					Usuario usuario = new Usuario();
					Rol rolEstudiante= new Rol();
					//creo el objeto para insertar la ficha inscripcion
					FichaInscripcion fichaInscripcion = new FichaInscripcion();
					
				persona.setPrsTipoIdentificacion(itemRegistro.getPrsTipoIdentificacion());
				persona.setPrsTipoIdentificacionSniese(PersonaConstantes.traerTipoIdEsnieseXTipoIdUce(itemRegistro.getPrsTipoIdentificacion()));
				persona.setPrsIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de persona ya existe.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					//throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.identificador.persona")));
				}
				persona.setPrsSexo(itemRegistro.getPrsSexo());
				persona.setPrsSexoSniese(PersonaConstantes.traerSexoEsnieseXSexoUce(itemRegistro.getPrsSexo()));
				persona.setPrsPrimerApellido(itemRegistro.getPrsPrimerApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsSegundoApellido(itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim().equals("NULO")?null:itemRegistro.getPrsSegundoApellido().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsNombres(itemRegistro.getPrsNombres().toUpperCase().replaceAll(" +", " ").trim());
				persona.setPrsMailInstitucional(itemRegistro.getPrsMailInstitucional().replaceAll(" +", " ").trim());
				persona.setPrsMailPersonal(itemRegistro.getPrsMailPersonal().replaceAll(" +", " ").trim());
				
				//validacion de constraint de mail personal
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El mail personal se encuentra ya registrado.");
					retorno= retornoAux.toString()  ;
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					//throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.mail.persona")));
				
				}	
				
				usuario.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
				usuario.setUsrFechaCreacion(fechaHoy);
				usuario.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
				usuario.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
				usuario.setUsrIdentificacion(itemRegistro.getPrsIdentificacion().toUpperCase());
				
				//validacion de constraint de identificacion de usuario
				if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El identificador de usuario ya existe.");
					retorno= retornoAux.toString()  ;
                  //   throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.identificador.usuario")));
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
				}
				String partes[] = itemRegistro.getPrsMailInstitucional().split("@");
				usuario.setUsrNick(partes[0]);
				
				//validacion de constraint de nick
				if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
					
					retorno= null;
					StringBuilder retornoAux= new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-El nick de usuario ya existe.");
					retorno= retornoAux.toString()  ;
				//	throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.nick.usuario")));
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(" ")));
					
				}
				
				//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
			   //	ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
				ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
				if(configuracionCarrera==null){
				ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
				if(configuracionCarrera2!=null){
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera2);
					}else{
						retorno= null;
						StringBuilder retornoAux= new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
						retorno= retornoAux.toString()  ;
						throw new PersonaValidacionException(" ");
						
					}
				}else{
					fichaInscripcion.setFcinConfiguracionCarrera(configuracionCarrera);
				}
				
				
				/*
				 * AÑADIDO    13/03/2018**********************************
				 */
//				
//				Carrera carreraSiiuAux= new Carrera();
//				carreraSiiuAux= servCarrera.buscarPorId(itemRegistro.getFcinCarreraSiiu());
//				
							
				if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
						
							fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO REINGRESO");
							fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
							fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
							fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					
				}else if (tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIO CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					
					
				}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIO UNIVERSIDAD");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					
				}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO REINICIO CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					fichaInscripcion.setFcinReinicioOrigen(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE);
					
				}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIO MALLA CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					
				}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO SEGUNDA CARRERA");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					
				}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
					
					fichaInscripcion.setFcinObservacionIngreso("ESTUDIANTE NUEVO CAMBIOS POR REDISEÑO");
					fichaInscripcion.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
					fichaInscripcion.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
					fichaInscripcion.setFcinTipoIngreso(tipoIngresoNuevaFcin);
					
				}else{
					//ESTUDIANTE SIN TIPO DE INGRESO 
					retorno = null;
					StringBuilder retornoAux = new StringBuilder();
					retornoAux.append(itemRegistro.getPrsIdentificacion());
					retornoAux.append("-Estudiante sin tipo de ingreso valido");
					retorno = retornoAux.toString();
					throw new PersonaValidacionException(" ");
					
				}
				
				//GUARDAR PERIODO ACADEMICO DE ACUERDO AL TIPO DE PERIODO, PREGRADO, IDIOMAS, CULTURA FISICA, INTENSIVO CULTURA FISICA
				
			//PERIODO ACADÉMICO  PREGRADO
				fichaInscripcion.setFcinPeriodoAcademico(pracActivo);
				fichaInscripcion.setFcinFechaInscripcion(fechaHoy);
				fichaInscripcion.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fichaInscripcion.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
				fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
				fichaInscripcion.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
				fichaInscripcion.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
				fichaInscripcion.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
				fichaInscripcion.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
				fichaInscripcion.setFcinObservacion("REGISTRO CREADO POR OPCIÓN DE MOVILIDAD ESTUDIANTIL MANUAL");
				
				if(itemRegistro.getFcinAplicaNotaEnes()!=null){
				if(itemRegistro.getFcinAplicaNotaEnes()==FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE){
					fichaInscripcion.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE);
					fichaInscripcion.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
					fichaInscripcion.setFcinNotaCorteId(itemRegistro.getFcinNotaCorteId());
					
				}else if(itemRegistro.getFcinAplicaNotaEnes()==FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE){
					fichaInscripcion.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE);
								
				}
				}
				
				//busco el rol de estudiante 
			
					 rolEstudiante = servRasRol.buscarRolXDescripcion(RolConstantes.ROL_BD_ESTUD_PREGRADO);
				
				//*************************************************************
				//************************** INSERCIONES **********************
				//*************************************************************
				em.persist(persona);
				usuario.setUsrPersona(persona);
				em.persist(usuario);
				UsuarioRol usuarioRol = new UsuarioRol();
				usuarioRol.setUsroRol(rolEstudiante);
				usuarioRol.setUsroUsuario(usuario);
				usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.persist(usuarioRol);
				
				fichaInscripcion.setFcinUsuarioRol(usuarioRol);
				em.persist(fichaInscripcion);
							
				//FIN CREACION SINO EXISTE PERSONA
				   
				   
				} else{ //Si Existe persona en el SIIU
					
					//busco el período académico activo
					PeriodoAcademico pracActivo= new PeriodoAcademico();
					   pracActivo= servRasPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
					
					//fecha actual
					Timestamp fechaHoy = new Timestamp(new Date().getTime());
				
					//Busco ficha inscripcion creada en este periodo en la carrera  si existe no vuelvo a crear
					FichaInscripcionDto auxFichaInscripcionPeriodoActivo= new FichaInscripcionDto();
					auxFichaInscripcionPeriodoActivo=servFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraxPeriodo(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), pracActivo.getPracId());
					
					
					if	(auxFichaInscripcionPeriodoActivo==null){  //NO Tiene ficha  inscripcion creada en periodo actual en la carrera a ingresar
						
						//Busco si existe una ficha inscripcion ACTIVA en la carrera indicada 
						FichaInscripcionDto auxFichaInscripcion= new FichaInscripcionDto();
						auxFichaInscripcion=servFichaInscripcionDto.buscarFichaInscripcionXidentificacionXcarreraNueva(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu());
						
						if(auxFichaInscripcion==null){  //NO TIENE FICHA INSCRIPCION ACTIVA en la carrera--CREO NUEVA FICHA INSCRIPCION
						
								//creo el objeto para insertar la ficha inscripcion nueva
							FichaInscripcion fichaInscripcionNueva = new FichaInscripcion();
							
							Usuario usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
							 UsuarioRol	usuarioRolAux = new UsuarioRol();
							if(usuarioAux!=null){
								try{
								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
								}catch ( UsuarioRolNoEncontradoException e){
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
									
								}catch ( UsuarioRolException e){
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
								}  
								 
							if(usuarioRolAux!=null){
							UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
							
							//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
							   //	ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarXcrr(itemRegistro.getFcinCarrera());
									
								   //Carrera 
								ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
								
								if(configuracionCarrera==null){
								ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
								
								if(configuracionCarrera2!=null){
									fichaInscripcionNueva.setFcinConfiguracionCarrera(configuracionCarrera2);
									}else{
										retorno= null;
										StringBuilder retornoAux= new StringBuilder();
										retornoAux.append(itemRegistro.getPrsIdentificacion());
										retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
										retorno= retornoAux.toString()  ;
										throw new PersonaValidacionException(" ");
										
									}
								
								
								}else{
								fichaInscripcionNueva.setFcinConfiguracionCarrera(configuracionCarrera);
								}
								
							
								
								/*
								 * AÑADIDO    13/03/2018**********************************
								 */
								
//								Carrera carreraSiiuAux= servCarrera.buscarPorId(itemRegistro.getFcinCarreraSiiu());
//								List<RecordEstudianteSAUDto> recordEstudianteSau= servRecordEstudianteSAU.buscarRecordAcademicoSAU(itemRegistro.getPrsIdentificacion(), carreraSiiuAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);

								if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
											fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVA F_INSCRIPCION REINGRESO");
											fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
											fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
											fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
									
								}else if (tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVA F_INSCRIPCION CAMBIO CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
									
								}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVA F_INSCRIPCION CAMBIO UNIVERSIDAD");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
																
								}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO  F_INSCRIPCION REINICIO CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
									fichaInscripcionNueva.setFcinReinicioOrigen(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE);
									
								}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION CAMBIO MALLA CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
									
								}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION SEGUNDA CARRERA");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
									
								}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
									
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION CAMBIOS POR REDISEÑO");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(tipoIngresoNuevaFcin);
									
								}else{
									fichaInscripcionNueva.setFcinObservacionIngreso("ESTUDIANTE NUEVO F_INSCRIPCION SIN TIPO DE INGRESO");
									fichaInscripcionNueva.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
									fichaInscripcionNueva.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
									fichaInscripcionNueva.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
									
								}
								
					
								fichaInscripcionNueva.setFcinPeriodoAcademico(pracActivo);
								fichaInscripcionNueva.setFcinFechaInscripcion(fechaHoy);
								fichaInscripcionNueva.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
								fichaInscripcionNueva.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
								fichaInscripcionNueva.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
								fichaInscripcionNueva.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  //nivelación y  MARCELO:para carreras SiiU tambien, si ingresan directo a carrera
								fichaInscripcionNueva.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
								fichaInscripcionNueva.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
								fichaInscripcionNueva.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
								fichaInscripcionNueva.setFcinObservacion("REGISTRO CREADO POR OPCIÓN DE MOVILIDAD ESTUDIANTIL MANUAL");
								if(itemRegistro.getFcinAplicaNotaEnes()!=null){
								if(itemRegistro.getFcinAplicaNotaEnes()==FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE){
									fichaInscripcionNueva.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE);
									fichaInscripcionNueva.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
									fichaInscripcionNueva.setFcinNotaCorteId(itemRegistro.getFcinNotaCorteId());
									
								}else if(itemRegistro.getFcinAplicaNotaEnes()==FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE){
									fichaInscripcionNueva.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE);
												
								}
								
								}
								
								fichaInscripcionNueva.setFcinUsuarioRol(usuarioRolPersis);
								em.persist(fichaInscripcionNueva);
						            	 
						            List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
								        	 //Se busca lista las fichas inscripcion Anteriores en la carrera  para buscar la ficha estudiante
											  auxListaFichaInscripcion=servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
								        	//Realizo los siguiente solo si existe al menos una ficha inscripcion anterior											  
									if((auxListaFichaInscripcion!=null)&&(auxListaFichaInscripcion.size()>0))	{	  
											//********************************	NEW	 casos con varias fichas estudiante  ***********************
												List<FichaEstudiante> auxListaFinalFichasEstudiante = new ArrayList<>();
															
												 
												  //BUSCO FICHAS ESTUDIANTE POR TODAS LA FICHAS_INSCRIPCION  inactivas en la carrera
												for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
													List<FichaEstudiante> auxListaFichasEstudiante2 = new ArrayList<>();
													auxListaFichasEstudiante2= servFichaEstudiante.listarPorFcinId(fichaInscripcionDto.getFcinId());
			                                      if	((auxListaFichasEstudiante2!=null)&&(auxListaFichasEstudiante2.size()>0)){
			                                      	
			                                      	for (FichaEstudiante fichaEstudiante : auxListaFichasEstudiante2) {
			                                      		auxListaFinalFichasEstudiante.add(fichaEstudiante);
														}
			                                      	
			                                      }
												}
									        
												if((auxListaFinalFichasEstudiante!=null)&&(auxListaFinalFichasEstudiante.size()>0)){  // Existe alguna fces
												if(auxListaFinalFichasEstudiante.size()==1){ // Si existe una ficha estudiante en la carrera con alguna de las fichas inscripcion
														FichaEstudiante fichaEstudianteExistente = new FichaEstudiante();
														fichaEstudianteExistente= em.find(FichaEstudiante.class, auxListaFinalFichasEstudiante.get(0).getFcesId()); // buscamos la ficha estudiante existente para editar
														      
														if(fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId()!=fichaInscripcionNueva.getFcinId()){
														         Integer  auxFcinAnterior =null;
														           auxFcinAnterior=fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId();//guardamos el id de fcin anterior guardada en la fces
														          FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId()); //buscamos la fcin anterior
																   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);  //inactivamos la fcin anterior
																   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
														           fichaEstudianteExistente.setFcesFichaInscripcion(fichaInscripcionNueva); //guardamos la fcin nueva en la ficha estudiante
														           FichaInscripcion fichaInscripcionNuevaAux= em.find(FichaInscripcion.class,fichaInscripcionNueva.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
														           fichaInscripcionNuevaAux.setFcinFcinAnteriorId(auxFcinAnterior); //guardamos el la fcin nueva el idFcin anterior.
														           
														}
														
													}else if(auxListaFinalFichasEstudiante.size()>1){  //VARIAS FICHAS ESTUDIANTE EN LA CARRERA 
														
														retorno= null;
														StringBuilder retornoAux= new StringBuilder();
														retornoAux.append(itemRegistro.getPrsIdentificacion());
														retornoAux.append("-Persona presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema");
														retorno= retornoAux.toString()  ;
														throw new PersonaValidacionException(" ");
														
													   }
												}else{ //NO TIENE NINGUNA FICHA ESTUDIANTE
													//Busco la ficha_iscripcion  con mator Id
													FichaInscripcionDto auxFichaInscripcionInactivaMayor=servFichaInscripcionDto.buscarFichaInscripcionXIdentificacionXCarreraXIdMayor(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu());
													
													if(auxFichaInscripcionInactivaMayor.getFcinId()!=fichaInscripcionNueva.getFcinId()){
															   Integer  auxFcinAnterior =null;
													           auxFcinAnterior=auxFichaInscripcionInactivaMayor.getFcinId();//guardamos el id de fcin anterior
													           FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,auxFichaInscripcionInactivaMayor.getFcinId());//buscamos el objeto fcin anterior
															   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE); //inactivamos el fcin anterior
															   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
													           FichaInscripcion fichaInscripcionNuevaAux= em.find(FichaInscripcion.class,fichaInscripcionNueva.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
													           fichaInscripcionNuevaAux.setFcinFcinAnteriorId(auxFcinAnterior); 
													           
													}
													
												}	  
																								  
													//************************************	  ***************************
											  
										
												
									}			
												
						            	  
						       //       }  //  FIN PARA EL RESTO DE TIPOS DE INGRESO: CAMBIOS, REINGRESOS
									
									// }else{ //SI TIENE FICHA INSCRIPCIONINACTIVA Y NINGUNA ACTIVA
									// retorno= itemRegistro.getPrsIdentificacion();
									// throw new PersonaValidacionException("Persona no tiene ficha inscripcion activa, 1 o varias inactivas");
									// }
									
							}
					
						}else {

							retorno = null;
							StringBuilder retornoAux = new StringBuilder();
							retornoAux.append(itemRegistro.getPrsIdentificacion());
							retornoAux.append("-Persona existente no tiene usuario creado");
							retorno = retornoAux.toString();
							throw new PersonaValidacionException(" ");

						}
									
				   }else if(auxFichaInscripcion!=null){ //SI TIENE FICHA INSCRIPCION ACTIVA.
						
						FichaInscripcion fcinExistente=em.find(FichaInscripcion.class,auxFichaInscripcion.getFcinId());
						fcinExistente.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
						//fcinExistente.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
						
						//creo el objeto para insertar la ficha inscripcion
						FichaInscripcion fichaInscripcionNuevo = new FichaInscripcion();
 						
						Usuario usuarioAux=servUsuario.buscarUsuarioPorIdentificacion(AuxPersona.getPrsIdentificacion());
						UsuarioRol	usuarioRolAux= new UsuarioRol();
						if(usuarioAux!=null){
							try{
								usuarioRolAux= servUsuarioRol.buscarXUsuarioXrol(usuarioAux.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
							}catch ( UsuarioRolNoEncontradoException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
								
							}catch ( UsuarioRolException e){
								retorno= null;
								StringBuilder retornoAux= new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Error desconocido al buscar usuario_rol de tipo pregrado creado");
								retorno= retornoAux.toString()  ;
								throw new PersonaValidacionException(" ");
							} 
						if(usuarioRolAux!=null){
						
						UsuarioRol usuarioRolPersis= em.find(UsuarioRol.class, usuarioRolAux.getUsroId());
						
						//busco la configuracion carrera a la que esta entrando  por carrera id y tipo formacion
						 ConfiguracionCarrera configuracionCarrera =  servRasConfiguracionCarrera.buscarCncrXcrrIdXsexoTtl(itemRegistro.getFcinCarreraSiiu(),PersonaConstantes.SEXO_HOMBRE_VALUE);
							
							if(configuracionCarrera==null){
							ConfiguracionCarrera configuracionCarrera2=	servRasConfiguracionCarrera.buscarXcrrIdNuevo(itemRegistro.getFcinCarreraSiiu());
							
							if(configuracionCarrera2!=null){
								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera2);
								}else{
									retorno= null;
									StringBuilder retornoAux= new StringBuilder();
									retornoAux.append(itemRegistro.getPrsIdentificacion());
									retornoAux.append("-No existe registro de configuración carrera, comuniquese con el administrador del sistema");
									retorno= retornoAux.toString()  ;
									throw new PersonaValidacionException(" ");
									
								}
												
							
							}else{
								fichaInscripcionNuevo.setFcinConfiguracionCarrera(configuracionCarrera);
							}
							
						
							if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE REINGRESO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
								
							}else if (tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE CAMBIO CARRERA  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
								
							}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE CAMBIO UNIVERSIDAD  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
																						
							}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE REINICIO CARRERA  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
								fichaInscripcionNuevo.setFcinReinicioOrigen(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE);
								
							}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE  CAMBIO MALLA  SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
								
							}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE  SEGUNDA CARRERA SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
								
							}else if(tipoIngresoNuevaFcin==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
								
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE CAMBIOS POR REDISEÑO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(tipoIngresoNuevaFcin);
								
							}else{
								fichaInscripcionNuevo.setFcinObservacionIngreso("ESTUDIANTE SIN TIPO DE INGRESO SE CREA NUEVA FCIN EN LA CARRERA E INACTIVA FCIN");
								fichaInscripcionNuevo.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
								fichaInscripcionNuevo.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
								fichaInscripcionNuevo.setFcinTipoIngreso(GeneralesConstantes.APP_ID_BASE);
								
							}
							
							fichaInscripcionNuevo.setFcinPeriodoAcademico(pracActivo);
							fichaInscripcionNuevo.setFcinFechaInscripcion(fechaHoy);
							fichaInscripcionNuevo.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
							fichaInscripcionNuevo.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
							fichaInscripcionNuevo.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
							fichaInscripcionNuevo.setFcinCarreraSiiu(itemRegistro.getFcinCarreraSiiu());  // para carreras SiiU 
							fichaInscripcionNuevo.setFcinCarrera(GeneralesConstantes.APP_ID_BASE); 
							fichaInscripcionNuevo.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
							fichaInscripcionNuevo.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
							fichaInscripcionNuevo.setFcinObservacion("REGISTRO CREADO POR OPCIÓN DE MOVILIDAD ESTUDIANTIL MANUAL");
							
							if(itemRegistro.getFcinAplicaNotaEnes()!=null){
							if(itemRegistro.getFcinAplicaNotaEnes()==FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE){
								fichaInscripcionNuevo.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_SI_APLICA_NOTA_ENES_VALUE);
								fichaInscripcionNuevo.setFcinNotaEnes(itemRegistro.getFcinNotaEnes());
								fichaInscripcionNuevo.setFcinNotaCorteId(itemRegistro.getFcinNotaCorteId());
								
							}else if(itemRegistro.getFcinAplicaNotaEnes()==FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE){
								fichaInscripcionNuevo.setFcinAplicaNotaEnes(FichaInscripcionConstantes.TIPO_NO_APLICA_NOTA_ENES_VALUE);
											
							}
							
							}
							fichaInscripcionNuevo.setFcinUsuarioRol(usuarioRolPersis);
							
							em.persist(fichaInscripcionNuevo);
						            	 
					            	  List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
							        	 //Se busca lista las fichas inscripcion en la carrera  para buscar la ficha estudiante
										  auxListaFichaInscripcion=servFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(itemRegistro.getPrsIdentificacion(), itemRegistro.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
							        	
																			 
										//  Si Existe  al menos una ficha inscripcon
								 if((auxListaFichaInscripcion!=null)&&(auxListaFichaInscripcion.size()>0)) {
									
											  
									//********************************	NEW	 casos con varias fichas estudiante  ***********************
									List<FichaEstudiante> auxListaFinalFichasEstudiante = new ArrayList<>();
												
									 
									  //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION en la carrera
									for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
										List<FichaEstudiante> auxListaFichasEstudiante2 = new ArrayList<>();
										auxListaFichasEstudiante2= servFichaEstudiante.listarPorFcinId(fichaInscripcionDto.getFcinId());
                                      if	((auxListaFichasEstudiante2!=null)&&(auxListaFichasEstudiante2.size()>0)){
                                      	
                                      	for (FichaEstudiante fichaEstudiante : auxListaFichasEstudiante2) {
                                      		auxListaFinalFichasEstudiante.add(fichaEstudiante);
											}
                                      	
                                      }
									}
						        
									if((auxListaFinalFichasEstudiante!=null)&&(auxListaFinalFichasEstudiante.size()>0)){  // Existe alguna fces
									if(auxListaFinalFichasEstudiante.size()==1){ // Ya existe una ficha estudiante en la carrera
											FichaEstudiante fichaEstudianteExistente = new FichaEstudiante();
											fichaEstudianteExistente= em.find(FichaEstudiante.class, auxListaFinalFichasEstudiante.get(0).getFcesId()); // buscamos la ficha estudiante existente para editar
											         
											if(fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId()!=fichaInscripcionNuevo.getFcinId()){
											           Integer  auxFcinAnterior =null;
											           auxFcinAnterior=fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId();//guardamos el id de fcin anterior
											           FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,fichaEstudianteExistente.getFcesFichaInscripcion().getFcinId());
													   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
													   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
											           fichaEstudianteExistente.setFcesFichaInscripcion(fichaInscripcionNuevo); //guardamos la fcin nueva en la ficha estudiante
											           FichaInscripcion fichaInscripcionNueva= em.find(FichaInscripcion.class,fichaInscripcionNuevo.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
											           fichaInscripcionNueva.setFcinFcinAnteriorId(auxFcinAnterior); //guardamos el la fcin nueva el idFcin anterior.
											           
											}
											           
										}else if(auxListaFinalFichasEstudiante.size()>1){  //VARIAS FICHAS ESTUDIANTE EN LA CARRERA 
											
											retorno= null;
											StringBuilder retornoAux= new StringBuilder();
											retornoAux.append(itemRegistro.getPrsIdentificacion());
											retornoAux.append("-Persona presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema");
											retorno= retornoAux.toString()  ;
											throw new PersonaValidacionException(" ");
											
										   }
									}else{ //NO TIENE NINGUNA FICHA ESTUDIANTE
												  
									    	if(fcinExistente.getFcinId()!=fichaInscripcionNuevo.getFcinId()){
										           Integer  auxFcinAnterior =null;
										           auxFcinAnterior=fcinExistente.getFcinId();//guardamos el id de fcin anterior
										           FichaInscripcion fcinExistente2=em.find(FichaInscripcion.class,fcinExistente.getFcinId());//buscamos el objeto fcin anterior
												   fcinExistente2.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE); //inactivamos el fcin anterior
												   fcinExistente2.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
												   FichaInscripcion fichaInscripcionNueva= em.find(FichaInscripcion.class,fichaInscripcionNuevo.getFcinId());  //buscamos la ficha inscripcion nueva para editar.
										           fichaInscripcionNueva.setFcinFcinAnteriorId(auxFcinAnterior); 
										          
										    }  
									}	  
										
							  }
					            	  
				//	       }  //  FIN PARA EL RESTO DE TIPOS DE INGRESO: CAMBIOS, REINGRESOS
								
								
								
							
											
							} 
								
							} else {

								retorno = null;
								StringBuilder retornoAux = new StringBuilder();
								retornoAux.append(itemRegistro.getPrsIdentificacion());
								retornoAux.append("-Persona existente no tiene usuario creado");
								retorno = retornoAux.toString();
								throw new PersonaValidacionException(" ");

							}

						}

					} else {
						// ESTUDIANTE YA INGRESADO EN ESTE PERIODO POR HOMOLOGACION

						// retorno= itemRegistro.getPrsIdentificacion();
						retorno = null;
						StringBuilder retornoAux = new StringBuilder();
						retornoAux.append(itemRegistro.getPrsIdentificacion());
						retornoAux.append("-Persona registrada en este periodo académico por homologación");
						retorno = retornoAux.toString();
						throw new PersonaValidacionException(" ");

					}

				
			
			}
			
			session.getUserTransaction().commit();
		
			retorno = null;
			
		} catch (PersonaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.persona", retorno)));
     	    //throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(retorno)));
     	    
		} catch (UsuarioValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		//	throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario")));
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.constraint.usuario", retorno)));
			
		}
		catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
				
				
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new RegistroAutomaticoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.homologacion.validar.exception")));
		}	
		
		
		return retorno;
		
	}
	/**MQ:
	 * Elimina ficha inscripcion sino tiene ficha estudiante o es un reingreso o reinicio se vuelve a colocar la ficha inscripcion anterior en la ficha Estudiante
	 * @param registroHomologacion - registro con la ficha inscripcion a eliminar.
	 * @param fichaEstudianteExistente - ficha estudiante existente.
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String eliminarFichaInscripcionHomologación(RegistroHomologacionDto registroHomologacionDto, FichaEstudiante fichaEstudianteExistente) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException{
		String retorno = null;
		try{
			session.getUserTransaction().begin();	
			
		FichaInscripcion  fichaInscripcionEliminar = new FichaInscripcion();		
		fichaInscripcionEliminar = em.find(FichaInscripcion.class, registroHomologacionDto.getFcinId());
		
		if(fichaEstudianteExistente== null) {
		em.remove(fichaInscripcionEliminar);
		
		}else{
		//Solo en caso de reingreso o rediseño
			
			if(registroHomologacionDto.getFcinFcinAnteriorId()!=GeneralesConstantes.APP_ID_BASE){ //Si existe ficha inscripcion anterior
			FichaInscripcion  fichaInscripcionAnterior = new FichaInscripcion();				
			fichaInscripcionAnterior = em.find(FichaInscripcion.class, registroHomologacionDto.getFcinFcinAnteriorId());  //busco ficha inscripcion anterior
			
			fichaInscripcionAnterior.setFcinVigente(FichaInscripcionConstantes.VIGENTE_VALUE); //activo la fcin anterior
			FichaEstudiante fichaEstudiante = em.find(FichaEstudiante.class, fichaEstudianteExistente.getFcesId()); //busco ficha estudiante
			fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAnterior); //coloco nuevamente la ficha inscripcion anterior
			}else{
				//sino existe ficha inscripcion anterior y existe ficha estudiante
				FichaEstudiante fichaEstudiante = em.find(FichaEstudiante.class, fichaEstudianteExistente.getFcesId());
				em.remove(fichaEstudiante); //elimina ficha estudiante
				
			}
			em.remove(fichaInscripcionEliminar);
					
			
		}
		
		session.getUserTransaction().commit();
		
	} catch (Exception e) {
		try {
			e.printStackTrace();
			session.getUserTransaction().rollback();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		throw new RegistroAutomaticoValidacionException(e.getMessage());
	} 
		
		
		
		return retorno;
		
	}
	
	
}
