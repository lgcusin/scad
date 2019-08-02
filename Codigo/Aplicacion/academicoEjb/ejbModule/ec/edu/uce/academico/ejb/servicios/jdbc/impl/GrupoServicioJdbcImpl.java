/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducciÃ³n o distribuciÃ³n no autorizada de este programa, 
 * o cualquier porciÃ³n de Ã©l, puede dar lugar a sanciones criminales y 
 * civiles severas, y serÃ¡n procesadas con el grado mÃ¡ximo contemplado 
 * por la ley.
  ************************************************************************* 
   
 ARCHIVO:     MatriculaServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de Matricula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
23Feb2018             Freddy Guzmán                       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.GrupoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.GrupoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoGrupoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoGrupo;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
/**
 * EJB GrupoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de MatriculaServicioJdbc.
 * @author fgguzman
 * @version 1.0
 */

@Stateless
public class GrupoServicioJdbcImpl implements GrupoServicioJdbc{
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public PersonaDto agregarCoordinador(PersonaDto coordinador, int periodo, int carrera, int grupo)throws GrupoNoEncontradoException  ,  GrupoException{
		PersonaDto retorno = null;

		try{

			List<RolFlujoGrupo> roflgrPorGrupoPeriodo = new ArrayList<>();
			Query sql3 = em.createNamedQuery("RolFlujoGrupo.findPorRolGrupoPeriodo");
			sql3.setParameter("rolId", RolConstantes.ROL_COORDINADORAREA_VALUE);
			sql3.setParameter("grpId", grupo);
			sql3.setParameter("pracId", periodo);
			roflgrPorGrupoPeriodo = sql3.getResultList();
			
			if (roflgrPorGrupoPeriodo != null && roflgrPorGrupoPeriodo.size() > 0) {
				for (RolFlujoGrupo item : roflgrPorGrupoPeriodo) {
				
					List<RolFlujoGrupo> roflgrOtroGrupo = new ArrayList<>();
					Query sql4 = em.createNamedQuery("RolFlujoGrupo.findPorUsroPeriodoNotInGrupo");
					sql4.setParameter("usroId", item.getRoflgrUsuarioRol().getUsroId());
					sql4.setParameter("grpId", grupo);
					sql4.setParameter("pracId", periodo);
					roflgrOtroGrupo = sql4.getResultList();
					
					if (roflgrOtroGrupo == null || roflgrOtroGrupo.size() <= 0) {
						UsuarioRol usrol = item.getRoflgrUsuarioRol();
						usrol.setUsroEstado(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
						em.merge(usrol);
						em.flush();
					}
					
					RolFlujoGrupo roflgrupo = item;
					roflgrupo.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_INACTIVO_VALUE);
					em.merge(roflgrupo);
					em.flush();
				}
			}
			
			
			List<UsuarioRol> rolesPorUsuario = new ArrayList<>();
			Query sql = em.createNamedQuery("UsuarioRol.findPorIdentificacionRol");
			sql.setParameter("usrIdentificacion", coordinador.getPrsIdentificacion());
			sql.setParameter("rolId", RolConstantes.ROL_COORDINADORAREA_VALUE);
			rolesPorUsuario = sql.getResultList();
			
			if (rolesPorUsuario != null && rolesPorUsuario.size() > 0) {
				UsuarioRol usro = rolesPorUsuario.get(rolesPorUsuario.size() -1);
				usro.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.merge(usro);
				em.flush();

				List<RolFlujoGrupo> gruposPorUsuarioRol = new ArrayList<>();
				Query sql2 = em.createNamedQuery("RolFlujoGrupo.findPorUsroGrupoPeriodo");
				sql2.setParameter("usroId", usro.getUsroId());
				sql2.setParameter("grpId", grupo);
				sql2.setParameter("pracId", periodo);
				gruposPorUsuarioRol = sql2.getResultList();

				if (gruposPorUsuarioRol != null && gruposPorUsuarioRol.size() > 0) { 
					for (RolFlujoGrupo item : gruposPorUsuarioRol) {
						if (item.getRoflgrPeriodoAcademico().getPracId() == periodo 
								&& item.getRoflgrGrupo().getGrpId() == grupo ) {
							item.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
							em.merge(item);
							em.flush();
						}
					}
				}else {
					Grupo grp = em.find(Grupo.class, grupo);
					PeriodoAcademico prac = em.find(PeriodoAcademico.class, periodo);
					
					RolFlujoGrupo roflgr = new RolFlujoGrupo();
					roflgr.setRoflgrGrupo(grp);
					roflgr.setRoflgrPeriodoAcademico(prac);
					roflgr.setRoflgrUsuarioRol(usro);		
					roflgr.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
					em.persist(roflgr);
					em.flush();
				}
				
			}else {
				
				Query sql1 = em.createNamedQuery("Usuario.findPorIdentificacion");
				sql1.setParameter("usrIdentificacion", coordinador.getPrsIdentificacion());
				Usuario user = (Usuario) sql1.getSingleResult();
				
				Rol rol = em.find(Rol.class, RolConstantes.ROL_COORDINADORAREA_VALUE);

				UsuarioRol usro = new UsuarioRol();
				usro.setUsroUsuario(user);
				usro.setUsroRol(rol);
				usro.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);

				em.persist(usro);
				em.flush();
				
				
				Grupo grp = em.find(Grupo.class, grupo);
				PeriodoAcademico prac = em.find(PeriodoAcademico.class, periodo);
				
				RolFlujoGrupo roflgr = new RolFlujoGrupo();
				roflgr.setRoflgrGrupo(grp);
				roflgr.setRoflgrPeriodoAcademico(prac);
				roflgr.setRoflgrUsuarioRol(usro);
				roflgr.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
				em.persist(roflgr);
				em.flush();
			}

		} catch (Exception e) {
			throw new GrupoException("Error de conexión, comuníquese con el administrador del sistema.");
		}

		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public PersonaDto agregarEvaluador(PersonaDto evaluador, int periodo, int carrera, int grupo)throws GrupoNoEncontradoException  ,  GrupoException{
		PersonaDto retorno = null;

		try{

			List<RolFlujoGrupo> roflgrPorGrupoPeriodo = new ArrayList<>();
			Query sql3 = em.createNamedQuery("RolFlujoGrupo.findPorRolGrupoPeriodo");
			sql3.setParameter("rolId", RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
			sql3.setParameter("grpId", grupo);
			sql3.setParameter("pracId", periodo);
			roflgrPorGrupoPeriodo = sql3.getResultList();
			
			
			if (roflgrPorGrupoPeriodo != null && roflgrPorGrupoPeriodo.size() > 0) {
				for (RolFlujoGrupo item : roflgrPorGrupoPeriodo) {
					List<RolFlujoGrupo> roflgrOtroGrupo = new ArrayList<>();
					Query sql4 = em.createNamedQuery("RolFlujoGrupo.findPorUsroPeriodoNotInGrupo");
					sql4.setParameter("usroId", item.getRoflgrUsuarioRol().getUsroId());
					sql4.setParameter("grpId", grupo);
					sql4.setParameter("pracId", periodo);
					roflgrOtroGrupo = sql4.getResultList();
					
					if (roflgrOtroGrupo == null || roflgrOtroGrupo.size() <= 0) {
						UsuarioRol usrol = item.getRoflgrUsuarioRol();
						usrol.setUsroEstado(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
						em.merge(usrol);
						em.flush();
					}
					
					
					RolFlujoGrupo roflgrupo = item;
					roflgrupo.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_INACTIVO_VALUE);
					em.merge(roflgrupo);
					em.flush();
				}
			}
			
			
			List<UsuarioRol> rolesPorUsuario = new ArrayList<>();
			Query sql = em.createNamedQuery("UsuarioRol.findPorIdentificacionRol");
			sql.setParameter("usrIdentificacion", evaluador.getPrsIdentificacion());
			sql.setParameter("rolId", RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
			rolesPorUsuario = sql.getResultList();
			
			if (rolesPorUsuario != null && rolesPorUsuario.size() > 0) {
				UsuarioRol usro = rolesPorUsuario.get(rolesPorUsuario.size() -1);
				usro.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				em.merge(usro);
				em.flush();

				List<RolFlujoGrupo> gruposPorUsuarioRol = new ArrayList<>();
				Query sql2 = em.createNamedQuery("RolFlujoGrupo.findPorUsroGrupoPeriodo");
				sql2.setParameter("usroId", usro.getUsroId());
				sql2.setParameter("grpId", grupo);
				sql2.setParameter("pracId", periodo);
				gruposPorUsuarioRol = sql2.getResultList();

				if (gruposPorUsuarioRol != null && gruposPorUsuarioRol.size() > 0) { 
					for (RolFlujoGrupo item : gruposPorUsuarioRol) {
						if (item.getRoflgrPeriodoAcademico().getPracId() == periodo 
								&& item.getRoflgrGrupo().getGrpId() == grupo ) {
							item.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
							em.merge(item);
							em.flush();
						}
					}
				}else {
					Grupo grp = em.find(Grupo.class, grupo);
					PeriodoAcademico prac = em.find(PeriodoAcademico.class, periodo);
					
					RolFlujoGrupo roflgr = new RolFlujoGrupo();
					roflgr.setRoflgrGrupo(grp);
					roflgr.setRoflgrPeriodoAcademico(prac);
					roflgr.setRoflgrUsuarioRol(usro);		
					roflgr.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
					em.persist(roflgr);
					em.flush();
				}
				
			}else {
				
				Query sql1 = em.createNamedQuery("Usuario.findPorIdentificacion");
				sql1.setParameter("usrIdentificacion", evaluador.getPrsIdentificacion());
				Usuario user = (Usuario) sql1.getSingleResult();
				
				Rol rol = em.find(Rol.class, RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);

				UsuarioRol usro = new UsuarioRol();
				usro.setUsroUsuario(user);
				usro.setUsroRol(rol);
				usro.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);

				em.persist(usro);
				em.flush();
				
				
				Grupo grp = em.find(Grupo.class, grupo);
				PeriodoAcademico prac = em.find(PeriodoAcademico.class, periodo);
				
				RolFlujoGrupo roflgr = new RolFlujoGrupo();
				roflgr.setRoflgrGrupo(grp);
				roflgr.setRoflgrPeriodoAcademico(prac);
				roflgr.setRoflgrUsuarioRol(usro);
				roflgr.setRoflgrEstado(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
				em.persist(roflgr);
				em.flush();
			}

		} catch (Exception e) {
			throw new GrupoException("Error de conexión, comuníquese con el administrador del sistema.");
		}

		return retorno;
	}
	
	
	public List<GrupoDto> buscarCoordinadoresPorGrupo(int carreraId, int periodoId) throws GrupoNoEncontradoException , GrupoValidacionException, GrupoException{
		List<GrupoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT GRP.GRP_ID, ");
		sql.append("   GRP.GRP_DESCRIPCION, ");
		sql.append("   GRP.GRP_ESTADO, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   (SELECT DISTINCT PRS.PRS_ID ");
		sql.append("   FROM USUARIO USR , ");
		sql.append("     USUARIO_ROL USRO , ");
		sql.append("     PERSONA PRS , ");
		sql.append("     ROL_FLUJO_GRUPO RLFLGR ");
		sql.append("   WHERE USRO.USR_ID = USR.USR_ID ");
		sql.append("   AND USR.PRS_ID    = PRS.PRS_ID ");
		sql.append("   AND USRO.USRO_ID  = RLFLGR.USRO_ID ");
		sql.append("   AND RLFLGR.GRP_ID = GRP.GRP_ID ");
		sql.append("   AND RLFLGR.PRAC_ID = ? ");
		sql.append("   AND USRO.ROL_ID   = ");sql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);
		sql.append("   AND USRO.USRO_ESTADO  = ");sql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sql.append("   AND RLFLGR.ROFLGR_ESTADO =  ");sql.append(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
		sql.append("   ) AS COORDINADOR_ID, ");
		sql.append("   (SELECT DISTINCT PRS.PRS_PRIMER_APELLIDO ");
		sql.append("     ||' ' ");
		sql.append("     ||PRS.PRS_SEGUNDO_APELLIDO ");
		sql.append("     ||' ' ");
		sql.append("     ||PRS.PRS_NOMBRES ");
		sql.append("   FROM USUARIO USR , ");
		sql.append("     USUARIO_ROL USRO , ");
		sql.append("     PERSONA PRS , ");
		sql.append("     ROL_FLUJO_GRUPO RLFLGR ");
		sql.append("   WHERE USRO.USR_ID = USR.USR_ID ");
		sql.append("   AND USR.PRS_ID    = PRS.PRS_ID ");
		sql.append("   AND USRO.USRO_ID  = RLFLGR.USRO_ID ");
		sql.append("   AND RLFLGR.GRP_ID = GRP.GRP_ID ");
		sql.append("   AND RLFLGR.PRAC_ID = ? ");
		sql.append("   AND USRO.ROL_ID   = ");sql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);
		sql.append("   AND USRO.USRO_ESTADO  = ");sql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sql.append("   AND RLFLGR.ROFLGR_ESTADO =  ");sql.append(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
		sql.append("   ) AS COORDINADOR, ");
		sql.append("   (SELECT DISTINCT PRS.PRS_ID ");
		sql.append("   FROM USUARIO USR , ");
		sql.append("     USUARIO_ROL USRO , ");
		sql.append("     PERSONA PRS , ");
		sql.append("     ROL_FLUJO_GRUPO RLFLGR ");
		sql.append("   WHERE USRO.USR_ID = USR.USR_ID ");
		sql.append("   AND USR.PRS_ID    = PRS.PRS_ID ");
		sql.append("   AND USRO.USRO_ID  = RLFLGR.USRO_ID ");
		sql.append("   AND RLFLGR.GRP_ID = GRP.GRP_ID ");
		sql.append("   AND RLFLGR.PRAC_ID = ? ");
		sql.append("   AND USRO.ROL_ID   = ");sql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
		sql.append("   AND USRO.USRO_ESTADO  = ");sql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sql.append("   AND RLFLGR.ROFLGR_ESTADO =  ");sql.append(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
		sql.append("   ) AS EVALUADOR_ID, ");
		sql.append("   (SELECT DISTINCT PRS.PRS_PRIMER_APELLIDO ");
		sql.append("     ||' ' ");
		sql.append("     ||PRS.PRS_SEGUNDO_APELLIDO ");
		sql.append("     ||' ' ");
		sql.append("     ||PRS.PRS_NOMBRES ");
		sql.append("   FROM USUARIO USR , ");
		sql.append("     USUARIO_ROL USRO , ");
		sql.append("     PERSONA PRS , ");
		sql.append("     ROL_FLUJO_GRUPO RLFLGR ");
		sql.append("   WHERE USRO.USR_ID = USR.USR_ID ");
		sql.append("   AND USR.PRS_ID    = PRS.PRS_ID ");
		sql.append("   AND USRO.USRO_ID  = RLFLGR.USRO_ID ");
		sql.append("   AND RLFLGR.GRP_ID = GRP.GRP_ID ");
		sql.append("   AND RLFLGR.PRAC_ID = ? ");
		sql.append("   AND USRO.ROL_ID   = ");sql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
		sql.append("   AND USRO.USRO_ESTADO  = ");sql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sql.append("   AND RLFLGR.ROFLGR_ESTADO =  ");sql.append(RolFlujoGrupoConstantes.ROFLGR_ESTADO_ACTIVO_VALUE);
		sql.append("   ) AS EVALUADOR ");
		sql.append(" FROM GRUPO GRP, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   CARRERA CRR ");
		sql.append(" WHERE GRP.GRP_CRR_ID  = CRR.CRR_ID ");
		sql.append(" AND MTR.GRP_ID = GRP.GRP_ID ");
		sql.append(" AND CRR.CRR_ID      = ? ");
		sql.append(" order by GRP.GRP_DESCRIPCION ");

		try{
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodoId);
			pstmt.setInt(2, periodoId);
			pstmt.setInt(3, periodoId);
			pstmt.setInt(4, periodoId);
			pstmt.setInt(5, carreraId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetAbuscarCoordinadoresPorGrupo(rs));
			}

		} catch (SQLException e) {
			throw new GrupoValidacionException("Error tipo sql, la subconsulta devuelve mas de un resultado.");
		} catch (Exception e) {
			throw new GrupoException("Error de conexión, comuníquese con el administrador del sistema.");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
			throw new GrupoNoEncontradoException("No se encontró grupos asigandos a las asignaturas de la carrera seleccionad.");
		}

		return retorno;

	}

	private GrupoDto transformarResultSetAbuscarCoordinadoresPorGrupo(ResultSet rs) throws SQLException{
		GrupoDto retorno = new GrupoDto();
		CarreraDto carrera = new CarreraDto();
		PersonaDto coordinador = new PersonaDto();
		PersonaDto evaluador = new PersonaDto();
		retorno.setGrpId(rs.getInt(1));
		retorno.setGrpDescripcion(rs.getString(2));
		retorno.setGrpEstado(rs.getInt(3));
		carrera.setCrrId(rs.getInt(4));
		carrera.setCrrDescripcion(rs.getString(5));
		coordinador.setPrsId(rs.getInt(6));
		coordinador.setPrsApellidosNombresEvaluacion(rs.getString(7));
		evaluador.setPrsId(rs.getInt(8));
		evaluador.setPrsApellidosNombresEvaluacion(rs.getString(9));
		retorno.setGrpCarreraDto(carrera);
		retorno.setGrpPersonaDtoCoordinador(coordinador);
		retorno.setGrpPersonaDtoEvaluador(evaluador);
		return retorno;
	}
}


