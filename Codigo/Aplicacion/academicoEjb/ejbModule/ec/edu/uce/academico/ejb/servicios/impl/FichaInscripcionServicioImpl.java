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

 ARCHIVO:     FichaEstudianteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla FichaEstudiante. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
/**
 * Clase (Bean)FichaInscripcionServicioImpl.
 * Bean declarado como Stateless.
 * @author ghmafla
 * @version 1.0
 */

@Stateless
public class FichaInscripcionServicioImpl implements FichaInscripcionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@EJB	private ConfiguracionCarreraServicio servConfiguracionCarreraServicio;
	@EJB	private PeriodoAcademicoServicio servPeriodoAcademicoServicio;
	@EJB	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB	private FichaEstudianteServicio sevFichaEstudianteServicio;
	@EJB	private CarreraServicio sevCarreraServicio;
	
	@Override
	public void pormoverNivelacionGrado(List<Persona> listFcin)	throws FichaEstudianteNoEncontradoException {
		if(listFcin.size() > 0)	{
			for (Persona item : listFcin) {
				try {
					//Hasta decidir si van a rediseño o no
						ConfiguracionCarrera cncrAux = new ConfiguracionCarrera();
						PeriodoAcademico pracAux = new PeriodoAcademico();
						FichaInscripcion retorno = new FichaInscripcion();
						FichaInscripcion fcinDesactivar = buscarFichaInscripcionNivelacion(item.getPrsIdentificacion());
						try {
							FichaInscripcion fcinAux = new FichaInscripcion();
							fcinAux = em.find(FichaInscripcion.class, fcinDesactivar.getFcinId());
							fcinAux.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
							em.merge(fcinAux);
							em.flush();	
						} catch (Exception e) {
							e.printStackTrace();
						}
						UsuarioRol usroAux = new UsuarioRol();
						try {
							StringBuffer sbsql = new StringBuffer();
							sbsql.append(" select usro from UsuarioRol usro");
							sbsql.append(" where usro.usroRol.rolId = ");
							sbsql.append(RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
							sbsql.append(" and usro.usroUsuario.usrIdentificacion = :cedula");
							Query q = em.createQuery(sbsql.toString());
							q.setParameter("cedula", item.getPrsIdentificacion());
							usroAux  = (UsuarioRol) q.getSingleResult();
							usroAux.getUsroId();
							retorno.setFcinUsuarioRol(usroAux);
							pracAux =   servPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
							
							
						} catch (Exception e) {
							usroAux = em.find(UsuarioRol.class, fcinDesactivar.getFcinUsuarioRol().getUsroId());
							usroAux.setUsroRol(em.find(Rol.class, RolConstantes.ROL_ESTUD_PREGRADO_VALUE));
							em.merge(usroAux);
							em.flush();	
							pracAux =   servPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
							usroAux =  servUsuarioRolServicio.buscarPorId(fcinDesactivar.getFcinUsuarioRol().getUsroId());
							retorno.setFcinUsuarioRol(usroAux);

						}
						
						
						
//						cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrIdXsexoTtl(item.getFcinCarreraSiiu(),item.getPrsSexo());
//						Carrera auxCarrera = new Carrera();
						if(fcinDesactivar.getFcinCarreraSiiu()!=GeneralesConstantes.APP_ID_BASE){
							cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrIdXsexoTtl(fcinDesactivar.getFcinCarreraSiiu(),0);	
						}
//						else{
//							
//							try {
//								auxCarrera = sevCarreraServicio.buscarCarreraXEspeCodigo(fcinDesactivar.getFcinCarrera());	
//							} catch (Exception e) {
//								continue;
//							}
//							
//							switch (auxCarrera.getCrrId()) {
//							case 33:
//							case 77:
//								auxCarrera = em.find(Carrera.class, 167);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(167);
//									
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(167);
//								}
//								break;
//							case 34:
//							case 78:
//								auxCarrera = em.find(Carrera.class, 168);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(168);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(168);
//								}
//								break;
//							case 35:
//								auxCarrera = em.find(Carrera.class, 180);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(180);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(180);
//								}
//								break;
//							case 47:
//							case 79:
//								auxCarrera = em.find(Carrera.class, 166);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(166);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(166);
//								}
//								break;
//							case 49:
//								auxCarrera = em.find(Carrera.class, 170);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(170);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(170);
//								}
//								break;
//							case 50:
//								auxCarrera = em.find(Carrera.class, 171);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(171);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(171);
//								}
//								break;
//							case 51:
//								auxCarrera = em.find(Carrera.class, 169);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(169);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(169);
//								}
//								break;
//							case 62:
//								auxCarrera = em.find(Carrera.class, 175);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(175);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(175);
//								}
//								break;
//							case 105:
//								auxCarrera = em.find(Carrera.class, 174);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(174);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(174);
//								}
//								break;
//							case 103:
//								auxCarrera = em.find(Carrera.class, 161);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(161);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(161);
//								}
//								break;
//							case 37:
//								auxCarrera = em.find(Carrera.class, 162);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(162);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(162);
//								}
//								break;
//							case 55:
//								auxCarrera = em.find(Carrera.class, 163);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(163);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(163);
//								}
//								break;
//							case 69:
//								auxCarrera = em.find(Carrera.class, 164);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(164);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(164);
//								}
//								break;
//							case 70:
//							case 80:
//								auxCarrera = em.find(Carrera.class, 165);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(165);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(165);
//								}
//								break;
//							case 63:
//								auxCarrera = em.find(Carrera.class, 148);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(148);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(148);
//								}
//								break;
//							case 57:
//								auxCarrera = em.find(Carrera.class, 159);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(159);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(159);
//								}
//								break;
//							case 76:
//								auxCarrera = em.find(Carrera.class, 212);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(212);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(212);
//								}
//								break;
//							case 82:
//								auxCarrera = em.find(Carrera.class, 157);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(157);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(157);
//								}
//								break;
//							case 84:
//								auxCarrera = em.find(Carrera.class, 158);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(158);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(158);
//								}
//								break;
//							case 94:
//								auxCarrera = em.find(Carrera.class, 211);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(211);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(211);
//								}
//								break;
//							case 87:
//								auxCarrera = em.find(Carrera.class, 172);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(172);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(172);
//								}
//								break;
//							case 92:
//								auxCarrera = em.find(Carrera.class, 179);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(179);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(179);
//								}
//								break;
//							case 66:
//								auxCarrera = em.find(Carrera.class, 149);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(149);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(149);
//								}
//								break;
//							case 67:
//								auxCarrera = em.find(Carrera.class, 150);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(150);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(150);
//								}
//								break;
//							case 72:
//								auxCarrera = em.find(Carrera.class, 151);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(151);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(151);
//								}
//								break;
//							case 91:
//							case 38:
//							case 93:
//								auxCarrera = em.find(Carrera.class, 135);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(135);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(135);
//								}
//								break;
//							case 142:
//								auxCarrera = em.find(Carrera.class, 11);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(11);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(11);
//								}
//								break;	
//							case 83:
//								auxCarrera = em.find(Carrera.class, 10);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(10);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(10);
//								}
//								break;
//							case 104:
//								auxCarrera = em.find(Carrera.class, 143);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(143);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(143);
//								}
//								break;
//							case 102:
//								auxCarrera = em.find(Carrera.class, 138);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(138);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(138);
//								}
//								break;
//							case 95:
//								auxCarrera = em.find(Carrera.class, 213);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(213);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(213);
//								}
//								break;
//							case 86:
//								auxCarrera = em.find(Carrera.class, 178);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(178);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(178);
//								}
//								break;
//							case 46:
//								auxCarrera = em.find(Carrera.class, 156);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(156);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(156);
//								}
//								break;
//							case 41:
//								auxCarrera = em.find(Carrera.class, 217);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(217);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(217);
//								}
//								break;
//							case 73:
//								auxCarrera = em.find(Carrera.class, 181);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(181);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(181);
//								}
//								break;
//							case 71:
//								auxCarrera = em.find(Carrera.class, 208);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(208);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(208);
//								}
//								break;
//							case 65:
//								auxCarrera = em.find(Carrera.class, 209);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(209);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(209);
//								}
//								break;
//							case 48:
//								auxCarrera = em.find(Carrera.class, 176);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(176);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(176);
//								}
//								break;
//							case 64:
//								auxCarrera = em.find(Carrera.class, 210);
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(210);
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(210);
//								}
//								break;
//							default:
//								auxCarrera = em.find(Carrera.class, auxCarrera.getCrrId());
//								try {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrr(auxCarrera.getCrrId());
//								} catch (Exception e) {
//									cncrAux = 	servConfiguracionCarreraServicio.buscarXcrrXSexoHombre(auxCarrera.getCrrId());
//								}
//								
//								break;
//							}
//						}
						
						Timestamp fechaHoy = new Timestamp(new Date().getTime());
						retorno.setFcinFechaInscripcion(fechaHoy);
						retorno.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
						retorno.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
						retorno.setFcinObservacion("APROBADO NIVELACION");
						retorno.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
						retorno.setFcinCarreraSiiu(fcinDesactivar.getFcinCarreraSiiu());
//						retorno.setFcinEncuesta(FichaInscripcionConstantes.NO_ENCUESTA_LLENA_VALUE);
						retorno.setFcinEncuesta(FichaInscripcionConstantes.SI_ENCUESTA_LLENA_VALUE);
						retorno.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
						retorno.setFcinCarrera(fcinDesactivar.getFcinCarreraSiiu());
						retorno.setFcinNotaEnes(fcinDesactivar.getFcinNotaEnes());
//						retorno.setFcinCarreraSiiu(auxCarrera.getCrrId());
						retorno.setFcinCncrArea(cncrAux.getCncrId());
						retorno.setFcinFcinNivelacion(fcinDesactivar.getFcinId());
						retorno.setFcinConfiguracionCarrera(cncrAux);
						retorno.setFcinPeriodoAcademico(pracAux);
						retorno.setFcinVigente(0);
						
						em.persist(retorno);
						em.flush();
						StringBuffer sbsql = new StringBuffer();
						sbsql.append(" select fces from FichaEstudiante fces");
						sbsql.append(" where fces.fcesFichaInscripcion.fcinId = ");
						sbsql.append(fcinDesactivar.getFcinId());
						Query q = em.createQuery(sbsql.toString());
						FichaEstudiante aux = (FichaEstudiante) q.getSingleResult();
						FichaEstudiante fcesaux = em.find(FichaEstudiante.class,aux.getFcesId());
						fcesaux.setFcesFichaInscripcion(retorno);
						em.merge(fcesaux);
						em.flush();
//						sevFichaEstudianteServicio.cambiarNivelacionFichaEstudiante(item.getFcesId(), retorno);
//						em.persist(retorno);
//						em.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void desactivarRegistrosUsuarioAprobadosSiac(List<EstudianteJdbcDto> listFcin) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException {
			try {
				if(listFcin.size() > 0)	{
					for (EstudianteJdbcDto item : listFcin) {
	
						StringBuffer sbsql = new StringBuffer();
						sbsql.append(" update FichaInscripcion set fcinEstado = ");
						sbsql.append(FichaInscripcionConstantes.INACTIVO_VALUE);
						sbsql.append(" , fcinObservacion = '");
						sbsql.append("MIGRADO A SAU");
						sbsql.append("' where fcinId = ");
						sbsql.append(item.getFcinId());
						Query q1 = em.createQuery(sbsql.toString());
						q1.executeUpdate();
						// StringBuffer sbsql2 = new StringBuffer();
						// sbsql2.append(" update Usuario set usrEstado = ");
						// sbsql2.append(UsuarioConstantes.ESTADO_INACTIVO_VALUE);
						// sbsql2.append(" , usrEstSesion = ");
						// sbsql2.append(UsuarioConstantes.ESTADO_INACTIVO_VALUE);
						// sbsql2.append(" , usrActiveDirectory = ");
						// sbsql2.append(UsuarioConstantes.ACTIVE_DIRECTORY_NO_VALUE);
						// sbsql2.append(" where usrIdentificacion = ");
						// sbsql2.append(item.getPrsIdentificacion());
						// Query q2 = em.createQuery(sbsql2.toString());
						// q2.executeUpdate();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public FichaInscripcion buscarFichaInscripcionNivelacion(String cedula) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException {
		try {

					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" select fcin from FichaInscripcion fcin, FichaEstudiante fces, FichaMatricula fcmt, Persona prs ");
					sbsql.append(" where fcin.fcinId = fces.fcesFichaInscripcion.fcinId");
					sbsql.append(" and prs.prsId = fces.fcesPersona.prsId");
					sbsql.append(" and fces.fcesId = fcmt.fcmtFichaEstudiante.fcesId");
					sbsql.append(" and fcmt.fcmtPracId=350");
					sbsql.append(" and prs.prsIdentificacion =:cedula");
					Query q = em.createQuery(sbsql.toString());
					q.setParameter("cedula", cedula);
					FichaInscripcion retorno = (FichaInscripcion) q.getSingleResult();
					return retorno;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}

	
	
	@Override
	public void modificarMatriculaPendientePasoNotas(Integer fcinId) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException {
		try {
			FichaInscripcion fcinAux = new FichaInscripcion();
			fcinAux = em.find(FichaInscripcion.class, fcinId);
			fcinAux.setFcinMatriculado(FichaInscripcionConstantes.MATRICULA_PENDIENTE_PASO_NOTAS_VALUE);
			em.merge(fcinAux);
			em.flush();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void desactivarFichaInscripcion(Integer fcinId) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException {
		try {
			FichaInscripcion fcinAux = new FichaInscripcion();
			fcinAux = em.find(FichaInscripcion.class, fcinId);
			fcinAux.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
			em.merge(fcinAux);
			em.flush();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void desactivarFichaInscripcionPosgradoXFcesId(Integer fcesId) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException {
		try {
			FichaEstudiante fces = new FichaEstudiante();
			fces = em.find(FichaEstudiante.class, fcesId);
			FichaInscripcion fcinAux = new FichaInscripcion();
			fcinAux = em.find(FichaInscripcion.class, fces.getFcesFichaInscripcion().getFcinId());
			fcinAux.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
			fcinAux.setFcinObservacion("NUEVO - POSGRADO "+" - DESACTIVADO REPROBADO +2 MATERIAS");
			em.merge(fcinAux);
			em.flush();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void activarMatriculaReprobadosNivelacion(List<EstudianteJdbcDto> listFcin) throws FichaEstudianteNoEncontradoException {
		try {
			if(listFcin.size() > 0)	{
				for (EstudianteJdbcDto item : listFcin) {
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" update FichaInscripcion set fcinMatriculado = ");
					sbsql.append(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
					sbsql.append(" where fcinId = ");
					sbsql.append(item.getFcinId());
					Query q1 = em.createQuery(sbsql.toString());
					q1.executeUpdate();
					// StringBuffer sbsql2 = new StringBuffer();
					// sbsql2.append(" update Usuario set usrEstado = ");
					// sbsql2.append(UsuarioConstantes.ESTADO_INACTIVO_VALUE);
					// sbsql2.append(" , usrEstSesion = ");
					// sbsql2.append(UsuarioConstantes.ESTADO_INACTIVO_VALUE);
					// sbsql2.append(" , usrActiveDirectory = ");
					// sbsql2.append(UsuarioConstantes.ACTIVE_DIRECTORY_NO_VALUE);
					// sbsql2.append(" where usrIdentificacion = ");
					// sbsql2.append(item.getPrsIdentificacion());
					// Query q2 = em.createQuery(sbsql2.toString());
					// q2.executeUpdate();
				}
			}
		} catch (Exception e) {
		}
		
		
	
}

	@Override
	public FichaInscripcion buscarXId(Integer fcinId) {
		FichaInscripcion retorno = new FichaInscripcion();
		retorno = em.find(FichaInscripcion.class, fcinId);
		return retorno;
	}
	
	public FichaInscripcion updateEstadoFichaInscripcion(int fcinId, int estado)throws  FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException {
		FichaInscripcion retorno = null;

		try {
			retorno = em.find(FichaInscripcion.class, fcinId);

			if (retorno == null) {
				throw new FichaInscripcionDtoNoEncontradoException("La ficha inscripción que busca no se encuentra registrado en la base de datos.");
			}

			retorno.setFcinEstado(estado);

			em.merge(retorno);
			em.flush();
		} catch (Exception e) {
			throw new FichaInscripcionDtoException("Error de conexión, comuníquese con el administrador del sistema.");
		}

		return retorno;
	}
	
	/**
	 * Método que permite crear una ficha inscripcion en la suficiencias informatica y cultura fisica.
	 * @param identificacion - cedula o pasaporte
	 * @param configuracionCarrera - id de la cncr
	 * @param rol - pregrado o postgrado
	 * @throws FichaEstudianteNoEncontradoException
	 */
	public void crearFichaInscripcionSuficiencia(String identificacion, Integer configuracionCarrera, Integer rol, PeriodoAcademico periodo) throws FichaInscripcionDtoValidacionException, FichaEstudianteNoEncontradoException, FichaInscripcionDtoException , UsuarioRolNoEncontradoException, UsuarioRolValidacionException{
		
		
		try{
			Query q = em.createNamedQuery("UsuarioRol.findPorIdentificacionRol");
			q.setParameter("usrIdentificacion", identificacion);
			q.setParameter("rolId", rol);
			UsuarioRol usro = (UsuarioRol)q.getSingleResult();
			
			try{
				buscarFichaInscripcionSuficiencia(identificacion, configuracionCarrera);
				throw new  FichaInscripcionDtoException("El estudiante que intenta registrar ya cuenta con una inscripción en la Suficiencia.");
			} catch (FichaInscripcionDtoNoEncontradoException e) {
				FichaInscripcion fcin = new FichaInscripcion();
				fcin.setFcinPeriodoAcademico(periodo);
				fcin.setFcinConfiguracionCarrera(new ConfiguracionCarrera(configuracionCarrera));
				fcin.setFcinUsuarioRol(usro);
				
				fcin.setFcinFechaInscripcion(Timestamp.from(Instant.now()));
				fcin.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
				fcin.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fcin.setFcinObservacion("INSCRIPCIÓN SUFICIENCIA " + periodo.getPracDescripcion());
				fcin.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE);
				fcin.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
				fcin.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);
				fcin.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_ACTIVO_VALUE);
				fcin.setFcinVigente(0);
				
				em.persist(fcin);
				em.flush();
				
				FichaEstudiante fces = new FichaEstudiante();
				fces.setFcesFichaInscripcion(fcin);
				fces.setFcesPersona(new Persona(usro.getUsroUsuario().getUsrPersona().getPrsId()));
				em.persist(fces);
				em.flush();

			} catch (FichaInscripcionDtoValidacionException e) {
				throw new  FichaInscripcionDtoValidacionException("Error tipo sql, comuníquese con el administrador del sistema.");
			}
		} catch (NoResultException e) {
			throw new  UsuarioRolNoEncontradoException("Registre el rol correspondiente al estudiente antes de registrar la suficiencia.");
		} catch (NonUniqueResultException e) {
			throw new  UsuarioRolValidacionException("Error tipo sql, comuníquese con el administrador del sistema.");
		}
			
		
	}
	
	private FichaInscripcion buscarFichaInscripcionSuficiencia(String identificacion , Integer configuracionCarrera) throws FichaInscripcionDtoNoEncontradoException, FichaInscripcionDtoValidacionException, FichaInscripcionDtoException{

		StringBuffer sql = new StringBuffer();
		sql.append(" select fcin from FichaInscripcion fcin, FichaEstudiante fces,  Persona prs ");
		sql.append(" where fcin.fcinId = fces.fcesFichaInscripcion.fcinId");
		sql.append(" and prs.prsId = fces.fcesPersona.prsId");
		sql.append(" and prs.prsIdentificacion = :identificacion");
		sql.append(" and fcin.fcinConfiguracionCarrera.cncrId = :configuracion");
		
		try {
			Query q = em.createQuery(sql.toString());
			q.setParameter("identificacion", identificacion);
			q.setParameter("configuracion", configuracionCarrera);
			return (FichaInscripcion) q.getSingleResult();
		} catch (NoResultException e) {
			throw new  FichaInscripcionDtoNoEncontradoException();
		} catch (NonUniqueResultException e) {
			throw new  FichaInscripcionDtoValidacionException();
		}
	}
}
