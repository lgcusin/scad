/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
  ************************************************************************* 
   
 ARCHIVO:     CargaHorariaServicioJdbcImpl.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 18-12-2018			Freddy Guzmán						Emisión Inicial
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
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CargaHorariaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;

/**
 * EJB CargaHorariaServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Comprobante.
 * @author fgguzman
 * @version 1.0
 */
@Stateless
public class CargaHorariaServicioJdbcImpl implements CargaHorariaServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_SAU)
	private DataSource dsSAU;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSIIU;
	
	public List<PersonaDto> buscarDocentes(int dependenciaId, int carreraId) throws PersonaNoEncontradoException, PersonaException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT prs.PRS_ID, ");
		sql.append("   prs.prs_identificacion , ");
		sql.append("   prs.prs_primer_apellido, ");
		sql.append("   prs.prs_segundo_apellido, ");
		sql.append("   prs.prs_nombres, ");
		sql.append("   prs.prs_sexo, ");
		sql.append("   prs.prs_mail_institucional, ");
		sql.append("   tmdd.tmdd_id, ");
		sql.append("   tmdd.tmdd_descripcion, ");
		sql.append("   pst.pst_denominacion, ");
		sql.append("   pst.pst_nivel_rango_gradual, ");
		sql.append("   pst.pst_categoria_docente, ");
		sql.append("   rbl.rllb_id, ");
		sql.append("   rbl.rllb_descripcion, ");
		sql.append("   dtpt.dtps_id ");
		sql.append(" FROM persona prs, ");
		sql.append("   usuario usr, ");
		sql.append("   usuario_rol usro, ");
		sql.append("   rol rol, ");
		sql.append("   ficha_docente fcdc, ");
		sql.append("   detalle_puesto dtpt, ");
		sql.append("   relacion_laboral rbl, ");
		sql.append("   puesto pst, ");
		sql.append("   tiempo_dedicacion tmdd, ");
		sql.append("   Carrera crr, ");
		sql.append("   Dependencia dpn ");
		sql.append(" WHERE fcdc.prs_id    = prs.prs_id ");
		sql.append(" AND prs.prs_id       = usr.prs_id ");
		sql.append(" AND usr.usr_id       = usro.usr_id ");
		sql.append(" AND usro.rol_id      = rol.rol_id ");
		sql.append(" AND fcdc.fcdc_id     = dtpt.fcdc_id ");
		sql.append(" AND rbl.rllb_id      = dtpt.rllb_id ");
		sql.append(" AND dtpt.pst_id      = pst.pst_id ");
		sql.append(" AND pst.tmdd_id      = tmdd.tmdd_id ");
		sql.append(" AND Dtpt.Crr_Id      = Crr.Crr_Id ");
		sql.append(" AND Crr.Dpn_Id       = Dpn.Dpn_Id ");
		sql.append(" AND Usr.Usr_Estado   = " + UsuarioConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND Usro.Usro_Estado = " + UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND Usro.Rol_Id      = " +  RolConstantes.ROL_DOCENTE_VALUE);
		sql.append(" AND Dpn.Dpn_Id       = ? ");
		
		if (carreraId != GeneralesConstantes.APP_ID_BASE) {
			sql.append(" AND Crr.Crr_Id       = ? ");
		}
		
		try {
			
			con = dsSIIU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, dependenciaId);
			
			if (carreraId != GeneralesConstantes.APP_ID_BASE) {
				pstmt.setInt(2, carreraId);
			}
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while (rs.next()){
				retorno.add(rsAbuscarInformacionDocente(rs));
			}
			
		} catch (SQLException e) {
			throw new PersonaException("Error tipo sql al buscar Docentes, comuniquése con el administrador.");
		} catch (Exception e) {
			throw new PersonaException("Error al buscar Docentes, comuniquése con el administrador.");
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
		
		if(retorno.isEmpty()){
			throw new PersonaNoEncontradoException("No se encontró Docentes con los parámetros solicitados.");
		}	
		
		return retorno;
	}
	
	public PersonaDto buscarInformacionDocente(String identificacion) throws PersonaNoEncontradoException, PersonaException{
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT prs1.PRS_ID, ");
		sql.append("   prs1.prs_identificacion , ");
		sql.append("   prs1.prs_primer_apellido, ");
		sql.append("   prs1.prs_segundo_apellido, ");
		sql.append("   prs1.prs_nombres, ");
		sql.append("   prs1.prs_sexo, ");
		sql.append("   prs1.prs_mail_institucional, ");
		sql.append("   tmdd1.tmdd_id, ");
		sql.append("   tmdd1.tmdd_descripcion, ");
		sql.append("   pst1.pst_denominacion, ");
		sql.append("   pst1.pst_nivel_rango_gradual, ");
		sql.append("   pst1.pst_categoria_docente, ");
		sql.append("   rbl1.rllb_id, ");
		sql.append("   rbl1.rllb_descripcion, ");
		sql.append("   dtpt1.dtps_id ");
		sql.append(" FROM persona prs1, ");
		sql.append("   usuario usr1, ");
		sql.append("   usuario_rol usro1, ");
		sql.append("   rol rol1, ");
		sql.append("   ficha_docente fcdc1, ");
		sql.append("   detalle_puesto dtpt1, ");
		sql.append("   relacion_laboral rbl1, ");
		sql.append("   puesto pst1, ");
		sql.append("   tiempo_dedicacion tmdd1 ");
		sql.append(" WHERE fcdc1.prs_id          = prs1.prs_id ");
		sql.append(" AND prs1.prs_id             = usr1.prs_id ");
		sql.append(" AND usr1.usr_id             = usro1.usr_id ");
		sql.append(" AND usro1.rol_id            = rol1.rol_id ");
		sql.append(" AND fcdc1.fcdc_id           = dtpt1.fcdc_id ");
		sql.append(" AND rbl1.rllb_id            = dtpt1.rllb_id ");
		sql.append(" AND dtpt1.pst_id            = pst1.pst_id ");
		sql.append(" AND pst1.tmdd_id            = tmdd1.tmdd_id ");
		sql.append(" AND dtpt1.DTPS_TIPO_CARRERA = " + CarreraConstantes.TIPO_PREGRADO_VALUE);
		sql.append(" AND dtpt1.dtps_estado       = " +  DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND prs1.prs_identificacion = ? ");
		
		try {
			con = dsSIIU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion.toUpperCase());
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				retorno = rsAbuscarInformacionDocente(rs);
			}
			
		} catch (SQLException e) {
			throw new PersonaException("Error tipo sql al buscar al Docente, comuniquése con el administrador.");
		} catch (Exception e) {
			throw new PersonaException("Error al buscar al Docente, comuniquése con el administrador.");
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
		
		if(retorno == null){
			throw new PersonaNoEncontradoException("No se encontró al Docente solicitado.");
		}	
		
		return retorno;
	}
	
	public List<CargaHorariaDto> buscarImparticionClasesPorDocente(String identificacion, int periodo) throws CargaHorariaNoEncontradoException, CargaHorariaException {
		List<CargaHorariaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ID_PERIODO, ");
		sql.append("   PERIODO, ");
		sql.append("   ID_FACULTAD, ");
		sql.append("   FACULTAD, ");
		sql.append("   ID_CARRERA, ");
		sql.append("   CARRERA, ");
		sql.append("   PROCESO_CARRERA, ");
		sql.append("   ID_PERSONA, ");
		sql.append("   CEDULA, ");
		sql.append("   APELLIDOS_NOMBRES_DOCENTE, ");
		sql.append("   ID_MATERIA, ");
		sql.append("   TIPO_MATERIA, ");
		sql.append("   MATERIA, ");
		sql.append("   MATERIA_PRINCIPAL, ");
		sql.append("   MLCRPR_COMPARTIDO, ");
		sql.append("   MLCRPR, ");
		sql.append("   NVL(SUM(HORAS_SEMANA), 0) HORAS_CLASE_SEMANA, ");
		sql.append("   NVL(SUM(HORAS_SEMANA_PAE), 0) HORAS_CLASE_PAE_SEMANA, ");
		sql.append("   MATRICULADOS, ");
		sql.append("   ID_PARALELO, ");
		sql.append("   PARALELO ");
		sql.append(" FROM ");
		sql.append("   (SELECT PRAC.PRAC_ID ID_PERIODO, ");
		sql.append("     PRAC.PRAC_DESCRIPCION PERIODO, ");
		sql.append("     DPN.DPN_ID ID_FACULTAD, ");
		sql.append("     DPN.DPN_DESCRIPCION FACULTAD, ");
		sql.append("     CRR.CRR_ID ID_CARRERA, ");
		sql.append("     CRR.CRR_DESCRIPCION CARRERA, ");
		sql.append("     CRR.CRR_PROCESO PROCESO_CARRERA, ");
		sql.append("     PRS.PRS_ID ID_PERSONA, ");
		sql.append("     PRS.PRS_IDENTIFICACION CEDULA, ");
		sql.append("     PRS.PRS_PRIMER_APELLIDO ");
		sql.append("     || ' ' ");
		sql.append("     || PRS.PRS_SEGUNDO_APELLIDO ");
		sql.append("     || ' ' ");
		sql.append("     || PRS.PRS_NOMBRES APELLIDOS_NOMBRES_DOCENTE, ");
		sql.append("     MTR.MTR_ID ID_MATERIA, ");
		sql.append("     MTR.TIMT_ID TIPO_MATERIA, ");
		sql.append("     MTR.MTR_DESCRIPCION MATERIA, ");
		sql.append("     MTR.MTR_SUB_ID MATERIA_PRINCIPAL, ");
		sql.append("     HRAC.MLCRPR_ID_COMP MLCRPR_COMPARTIDO, ");
		sql.append("     HRAC.MLCRPR_ID MLCRPR, ");
		sql.append("     COUNT(MTR.MTR_DESCRIPCION) MATERIAS, ");
		sql.append("     CASE WHEN (HRAC.HRAC_HORA_TIPO = -99 OR  HRAC.HRAC_HORA_TIPO = 1 OR  HRAC.HRAC_HORA_TIPO IS NULL) THEN ");
		sql.append("     	SUM(HRCL.HOCL_HORA_FIN - HRCL.HOCL_HORA_INICIO)  END  HORAS_SEMANA, ");
		sql.append("     CASE WHEN (HRAC.HRAC_HORA_TIPO = 2) THEN ");
		sql.append("     	SUM(HRCL.HOCL_HORA_FIN - HRCL.HOCL_HORA_INICIO)  END HORAS_SEMANA_PAE, ");
		sql.append("     PRL.PRL_ID ID_PARALELO, ");
		sql.append("     PRL.PRL_DESCRIPCION PARALELO, ");
		sql.append("     (SELECT DISTINCT COUNT(PRS1.PRS_IDENTIFICACION) MATRICULADOS ");
		sql.append("     FROM PERSONA PRS1, ");
		sql.append("       USUARIO USR1, ");
		sql.append("       FICHA_ESTUDIANTE FCES1, ");
		sql.append("       RECORD_ESTUDIANTE RCES1, ");
		sql.append("       MALLA_CURRICULAR_PARALELO MLCRPR1, ");
		sql.append("       MALLA_CURRICULAR_MATERIA MLCRMT1, ");
		sql.append("       MATERIA MTR1, ");
		sql.append("       FICHA_MATRICULA FCMT1, ");
		sql.append("       COMPROBANTE_PAGO CMPG1, ");
		sql.append("       DETALLE_MATRICULA DTMT1, ");
		sql.append("       FICHA_INSCRIPCION FCIN1, ");
		sql.append("       CONFIGURACION_CARRERA CNCR1, ");
		sql.append("       CARRERA CRR1, ");
		sql.append("       DEPENDENCIA DPN1, ");
		sql.append("       PERIODO_ACADEMICO PRAC1 ");
		sql.append("     WHERE FCES1.FCES_ID    = FCMT1.FCES_ID ");
		sql.append("     AND FCMT1.FCMT_ID      = CMPG1.FCMT_ID ");
		sql.append("     AND CMPG1.CMPA_ID      = DTMT1.CMPA_ID ");
		sql.append("     AND PRS1.PRS_ID        = FCES1.PRS_ID ");
		sql.append("     AND USR1.PRS_ID        = PRS1.PRS_ID ");
		sql.append("     AND RCES1.MLCRPR_ID    = MLCRPR1.MLCRPR_ID ");
		sql.append("     AND MLCRPR1.MLCRPR_ID  = DTMT1.MLCRPR_ID ");
		sql.append("     AND MLCRMT1.MLCRMT_ID  = MLCRPR1.MLCRMT_ID ");
		sql.append("     AND MLCRMT1.MTR_ID     = MTR1.MTR_ID ");
		sql.append("     AND MTR1.CRR_ID        = CRR1.CRR_ID ");
		sql.append("     AND FCES1.FCES_ID      = RCES1.FCES_ID ");
		sql.append("     AND FCIN1.FCIN_ID      = FCES1.FCIN_ID ");
		sql.append("     AND FCIN1.CNCR_ID      = CNCR1.CNCR_ID ");
		sql.append("     AND CRR1.CRR_ID        = CNCR1.CRR_ID ");
		sql.append("     AND CRR1.DPN_ID        = DPN1.DPN_ID ");
		sql.append("     AND PRAC1.PRAC_ID      = FCMT1.FCMT_PRAC_ID ");
		sql.append("     AND DPN1.DPN_ID        = DPN1.DPN_ID ");
		sql.append("     AND FCMT1.FCMT_PRAC_ID = PRAC.PRAC_ID ");
		sql.append("     AND CRR1.CRR_ID        = CRR.CRR_ID ");
		sql.append("     AND MTR1.MTR_CODIGO    = MTR1.MTR_CODIGO ");
		sql.append("     AND MLCRPR1.MLCRPR_ID  = MLCRPR.MLCRPR_ID ");
		sql.append("     ) MATRICULADOS ");
		sql.append("   FROM PERSONA PRS, ");
		sql.append("     FICHA_DOCENTE FCDC, ");
		sql.append("     DETALLE_PUESTO DTPS, ");
		sql.append("     CARGA_HORARIA CRHR, ");
		sql.append("     PERIODO_ACADEMICO PRAC, ");
		sql.append("     TIEMPO_DEDICACION TMDD, ");
		sql.append("     PUESTO PST, ");
		sql.append("     RELACION_LABORAL RBL, ");
		sql.append("     MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("     PARALELO PRL, ");
		sql.append("     MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("     MATERIA MTR, ");
		sql.append("     CARRERA CRR, ");
		sql.append("     DEPENDENCIA DPN, ");
		sql.append("     HORARIO_ACADEMICO HRAC, ");
		sql.append("     HORA_CLASE HRCL, ");
		sql.append("     HORA_CLASE_AULA HRCLA, ");
		sql.append("     NIVEL NVL ");
		sql.append("   WHERE PRS.PRS_ID                 = FCDC.PRS_ID ");
		sql.append("   AND DTPS.FCDC_ID                 = FCDC.FCDC_ID ");
		sql.append("   AND CRHR.DTPS_ID                 = DTPS.DTPS_ID ");
		sql.append("   AND CRHR.PRAC_ID                 = PRAC.PRAC_ID ");
		sql.append("   AND DTPS.PST_ID                  = PST.PST_ID ");
		sql.append("   AND PST.TMDD_ID                  = TMDD.TMDD_ID ");
		sql.append("   AND CRHR.MLCRPR_ID               = MLCRPR.MLCRPR_ID ");
		sql.append("   AND MLCRPR.PRL_ID                = PRL.PRL_ID ");
		sql.append("   AND MLCRPR.MLCRMT_ID             = MLCRMT.MLCRMT_ID ");
		sql.append("   AND MLCRMT.MTR_ID                = MTR.MTR_ID ");
		sql.append("   AND RBL.RLLB_ID                  = DTPS.RLLB_ID ");
		sql.append("   AND MTR.CRR_ID                   = CRR.CRR_ID ");
		sql.append("   AND CRR.DPN_ID                   = DPN.DPN_ID ");
		sql.append("   AND MLCRPR.MLCRPR_ID             = HRAC.MLCRPR_ID ");
		sql.append("   AND HRAC.HOCLAL_ID               = HRCLA.HOCLAL_ID ");
		sql.append("   AND HRCLA.HOCL_ID                = HRCL.HOCL_ID ");
		sql.append("   AND NVL.NVL_ID                   = MLCRMT.NVL_ID ");
		sql.append("   AND CRHR.CRHR_ESTADO             =  " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append("   AND CRHR.CRHR_ESTADO_ELIMINACION =  " + CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		sql.append("   AND PRS.PRS_IDENTIFICACION       = ? ");
		sql.append("   AND PRAC.PRAC_ID             = ? ");
		sql.append("   GROUP BY PRAC.PRAC_DESCRIPCION, ");
		sql.append("     DPN.DPN_DESCRIPCION, ");
		sql.append("     CRR.CRR_DESCRIPCION, ");
		sql.append("     PRS.PRS_IDENTIFICACION, ");
		sql.append("     PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("     PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("     PRS.PRS_NOMBRES, ");
		sql.append("     TMDD.TMDD_DESCRIPCION, ");
		sql.append("     PST.PST_DENOMINACION, ");
		sql.append("     PST.PST_NIVEL_RANGO_GRADUAL, ");
		sql.append("     MTR.MTR_ID, ");
		sql.append("     MTR.MTR_DESCRIPCION, ");
		sql.append("     MTR.MTR_CREDITOS, ");
		sql.append("     PRL.PRL_ID, ");
		sql.append("     PRL.PRL_DESCRIPCION, ");
		sql.append("     HRAC.HRAC_DESCRIPCION, ");
		sql.append("     RBL.RLLB_DESCRIPCION, ");
		sql.append("     HRCL.HOCL_DESCRIPCION, ");
		sql.append("     HRAC.HRAC_DIA, ");
		sql.append("     NVL.NVL_NUMERAL, ");
		sql.append("     PRL.PRL_DESCRIPCION, ");
		sql.append("     HRCL.HOCL_HORA_FIN, ");
		sql.append("     HRCL.HOCL_HORA_INICIO, ");
		sql.append("     PRAC.PRAC_ID, ");
		sql.append("     DPN.DPN_ID, ");
		sql.append("     CRR.CRR_ID, ");
		sql.append("     MLCRPR.MLCRPR_ID, ");
		sql.append("     MTR.MTR_HORAS, ");
		sql.append("     MTR.MTR_HORAS_PRACTICAS, ");
		sql.append("     PRS.PRS_ID, ");
		sql.append("     MTR.MTR_ID, ");
		sql.append("     MTR.TIMT_ID, ");
		sql.append("     HRAC.MLCRPR_ID_COMP, ");
		sql.append("     HRAC.MLCRPR_ID , ");
		sql.append("     MTR.MTR_SUB_ID, ");
		sql.append("     CRR.CRR_PROCESO, ");
		sql.append("     HRAC.HRAC_HORA_TIPO ");
		sql.append("   ORDER BY 5 ");
		sql.append("   ) TEST ");
		sql.append(" GROUP BY ID_PERIODO, ");
		sql.append("   PERIODO, ");
		sql.append("   ID_FACULTAD, ");
		sql.append("   FACULTAD, ");
		sql.append("   ID_CARRERA, ");
		sql.append("   CARRERA, ");
		sql.append("   PROCESO_CARRERA, ");
		sql.append("   ID_PERSONA, ");
		sql.append("   CEDULA, ");
		sql.append("   APELLIDOS_NOMBRES_DOCENTE, ");
		sql.append("   MATERIA, ");
		sql.append("   ID_PARALELO, ");
		sql.append("   PARALELO, ");
		sql.append("   MATRICULADOS, ");
		sql.append("   ID_MATERIA, ");
		sql.append("   TIPO_MATERIA, ");
		sql.append("   MATERIA_PRINCIPAL, ");
		sql.append("   MLCRPR_COMPARTIDO, ");
		sql.append("   MLCRPR ");
		sql.append(" ORDER BY MATERIA  ");

		
		try {
			con = dsSIIU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, periodo);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarImparticionClasesPorDocente(rs));
			}
			
		} catch (Exception e) {
			throw new CargaHorariaException("Error de conexión, comuníquese con el administrador.");
		}finally {
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
		
		if (retorno.isEmpty()) {
			throw new CargaHorariaNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
		}
		
		return retorno;
	}


	public List<CargaHorariaDto> buscarFuncionesPorDocente(String identificacion, int periodo) throws CargaHorariaNoEncontradoException, CargaHorariaException {
		List<CargaHorariaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT prac1.PRAC_ID, ");
		sql.append("   prac1.prac_descripcion, ");
		sql.append("   dpn1.DPN_ID, ");
		sql.append("   dpn1.dpn_descripcion, ");
		sql.append("   crr1.CRR_ID, ");
		sql.append("   crr1.CRR_DESCRIPCION, ");
		sql.append("   prs1.prs_id , ");
		sql.append("   prs1.prs_identificacion , ");
		sql.append("   prs1.prs_primer_apellido ");
		sql.append("   || ' ' ");
		sql.append("   || prs1.prs_segundo_apellido ");
		sql.append("   ||' ' ");
		sql.append("   || prs1.prs_nombres prs_apellidos_nombres, ");
		sql.append("   tmdd1.tmdd_descripcion tmdd_descripcion, ");
		sql.append("   CASE ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'AUXILIAR' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '1') ");
		sql.append("     THEN 'AUXILIAR NIVEL 1' ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'AUXILIAR' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '2') ");
		sql.append("     THEN 'AUXILIAR NIVEL 2' ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'AGREGADO' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '1') ");
		sql.append("     THEN 'AGREGADO NIVEL 1' ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'AGREGADO' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '2') ");
		sql.append("     THEN 'AGREGADO NIVEL 2' ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'AGREGADO' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '3') ");
		sql.append("     THEN 'AGREGADO NIVEL 3' ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'PRINCIPAL' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '1') ");
		sql.append("     THEN 'PRINCIPAL NIVEL 1' ");
		sql.append("     WHEN (pst1.pst_denominacion      = 'PRINCIPAL' ");
		sql.append("     AND pst1.pst_nivel_rango_gradual = '2') ");
		sql.append("     THEN 'PRINCIPAL NIVEL 2' ");
		sql.append("     WHEN (pst1.pst_denominacion = 'OCASIONAL') ");
		sql.append("     THEN 'OCASIONAL' ");
		sql.append("   END CATEGORIA, ");
		sql.append("   rbl1.rllb_descripcion, ");
		sql.append("   tpcghr1.ticrhr_id, ");
		sql.append("   tpcghr1.ticrhr_descripcion, ");
		sql.append("   fncrhr1.fncrhr_id, ");
		sql.append("   fncrhr1.fncrhr_descripcion, ");
		sql.append("   cghr1.crhr_id, ");
		sql.append("   cghr1.crhr_num_horas, ");
		sql.append("   cghr1.crhr_estado, ");
		sql.append("   cghr1.crhr_estado_eliminacion ");
		sql.append(" FROM persona prs1, ");
		sql.append("   usuario usr1, ");
		sql.append("   usuario_rol usro1, ");
		sql.append("   rol rol1, ");
		sql.append("   ficha_docente fcdc1, ");
		sql.append("   detalle_puesto dtpt1, ");
		sql.append("   carga_horaria cghr1, ");
		sql.append("   funcion_carga_horaria fncrhr1, ");
		sql.append("   tipo_funcion_carga_horaria tpfncghr1, ");
		sql.append("   tipo_carga_horaria tpcghr1, ");
		sql.append("   periodo_academico prac1, ");
		sql.append("   carrera crr1, ");
		sql.append("   dependencia dpn1, ");
		sql.append("   relacion_laboral rbl1, ");
		sql.append("   puesto pst1, ");
		sql.append("   tiempo_dedicacion tmdd1 ");
		sql.append(" WHERE fcdc1.prs_id                = prs1.prs_id ");
		sql.append(" AND prs1.prs_id                   = usr1.prs_id ");
		sql.append(" AND usr1.usr_id                   = usro1.usr_id ");
		sql.append(" AND usro1.rol_id                  = rol1.rol_id ");
		sql.append(" AND fcdc1.fcdc_id                 = dtpt1.fcdc_id ");
		sql.append(" AND dtpt1.dtps_id                 = cghr1.dtps_id ");
		sql.append(" AND cghr1.tifncrhr_id             = tpfncghr1.tifncrhr_id ");
		sql.append(" AND tpfncghr1.ticrhr_id           = tpcghr1.ticrhr_id ");
		sql.append(" AND tpfncghr1.fncrhr_id           = fncrhr1.fncrhr_id ");
		sql.append(" AND crr1.crr_id                   = dtpt1.crr_id ");
		sql.append(" AND dpn1.dpn_id                   = crr1.dpn_id ");
		sql.append(" AND rbl1.rllb_id                  = dtpt1.rllb_id ");
		sql.append(" AND dtpt1.pst_id                  = pst1.pst_id ");
		sql.append(" AND pst1.tmdd_id                  = tmdd1.tmdd_id ");
		sql.append(" AND cghr1.prac_id                 = prac1.prac_id ");
		sql.append(" AND cghr1.tifncrhr_id            IS NOT NULL ");
//		sql.append(" AND dtpt1.dtps_estado             = 0 ");
//		sql.append(" AND fcdc1.fcdc_estado             = 0 ");
		sql.append(" AND cghr1.crhr_estado             = 0 ");
		sql.append(" AND cghr1.crhr_estado_eliminacion = 1 ");
		sql.append(" AND prac1.prac_id                 = ? ");
		sql.append(" AND prs1.prs_identificacion       = ? ");
		sql.append(" ORDER BY 13 ");

		try {
			con = dsSIIU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodo);
			pstmt.setString(2, identificacion);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarFuncionesPorDocente(rs));
			}
			
		} catch (Exception e) {
			throw new CargaHorariaException("Error de conexión, comuníquese con el administrador.");
		}finally {
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
		
		if (retorno.isEmpty()) {
			throw new CargaHorariaNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
		}
		
		return retorno;
	}
	
	public List<CargaHorariaDto> buscarFuncionesPorPeriodo(int periodo) throws CargaHorariaNoEncontradoException, CargaHorariaException {
		List<CargaHorariaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT TPFNCRHR.TICRHR_ID, ");
		sql.append("   TPCRHR.TICRHR_DESCRIPCION ");
		sql.append(" FROM CARGA_HORARIA CRHR, ");
		sql.append("   FUNCION_CARGA_HORARIA FNCRHR, ");
		sql.append("   TIPO_FUNCION_CARGA_HORARIA TPFNCRHR, ");
		sql.append("   TIPO_CARGA_HORARIA TPCRHR, ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE CRHR.TIFNCRHR_ID = TPFNCRHR.TIFNCRHR_ID ");
		sql.append(" AND FNCRHR.FNCRHR_ID   = TPFNCRHR.FNCRHR_ID ");
		sql.append(" AND PRAC.PRAC_ID       = CRHR.PRAC_ID ");
		sql.append(" AND TPCRHR.TICRHR_ID   = TPFNCRHR.TICRHR_ID ");
		sql.append(" AND PRAC.PRAC_ID       =  ? ");

		
		try {
			con = dsSIIU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodo);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarFuncionesPorPeriodo(rs));
			}
			
		} catch (Exception e) {
			throw new CargaHorariaException("Error de conexión, comuníquese con el administrador.");
		}finally {
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
			throw new CargaHorariaNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
		}
		
		return retorno;
	}
	
	private CargaHorariaDto rsAbuscarImparticionClasesPorDocente(ResultSet rs) throws SQLException{
		CargaHorariaDto retorno = new CargaHorariaDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		PersonaDto persona = new PersonaDto();
		MateriaDto materia = new MateriaDto();
		ParaleloDto paralelo = new ParaleloDto();
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		
		dependencia.setDpnId(rs.getInt(3));
		dependencia.setDpnDescripcion(rs.getString(4));
		
		carrera.setCrrId(rs.getInt(5));
		carrera.setCrrDescripcion(rs.getString(6));
		carrera.setCrrProceso(rs.getInt(7));
		
		persona.setPrsId(rs.getInt(8));
		persona.setPrsIdentificacion(rs.getString(9));
		persona.setPrsApellidosNombres(rs.getString(10));
		
		materia.setMtrId(rs.getInt(11));
		materia.setTpmtId(rs.getInt(12));
		materia.setMtrDescripcion(rs.getString(13));
		materia.setMtrSubId(rs.getInt(14));
		
		materia.setHracMlcrprIdComp(rs.getInt(15));
		materia.setHracMlcrprId(rs.getInt(16));
		materia.setMtrHorasPorSemana(rs.getInt(17));
		materia.setMtrHorasPorSemanaPAE(rs.getInt(18));
		paralelo.setMlcrprMatriculados(rs.getInt(19));
		paralelo.setPrlId(rs.getInt(20));
		paralelo.setPrlDescripcion(rs.getString(21));
		
		retorno.setCahrPeriodoAcademicoDto(periodo);
		retorno.setCarhDependenciaDto(dependencia);
		retorno.setCahrCarreraDto(carrera);
		retorno.setCahrPersonaDto(persona);
		retorno.setCahrMateriaDto(materia);
		retorno.setCahrParaleloDto(paralelo);
		retorno.setCahrNumHoras(materia.getMtrHorasPorSemana()+ (materia.getMtrHorasPorSemanaPAE()!=null?materia.getMtrHorasPorSemanaPAE():0));
		return retorno;
	}
	
	private CargaHorariaDto rsAbuscarFuncionesPorDocente(ResultSet rs) throws SQLException{
		CargaHorariaDto retorno = new CargaHorariaDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		PersonaDto persona = new PersonaDto();
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		dependencia.setDpnId(rs.getInt(3));
		dependencia.setDpnDescripcion(rs.getString(4));
		carrera.setCrrId(rs.getInt(5));
		carrera.setCrrDescripcion(rs.getString(6));
		persona.setPrsId(rs.getInt(7));
		persona.setPrsIdentificacion(rs.getString(8));
		persona.setPrsApellidosNombres(rs.getString(9));
		retorno.setTmddDescripcion(rs.getString(10));
		retorno.setTmddCategoria(rs.getString(11));
		retorno.setRllbDescripcion(rs.getString(12));
		retorno.setTicahrId(rs.getInt(13));
		retorno.setTicahrDescripcion(rs.getString(14));
		retorno.setFncahrId(rs.getInt(15));
		retorno.setFncahrDescripcion(rs.getString(16));
		retorno.setCahrId(rs.getInt(17));
		retorno.setCahrNumHoras(rs.getInt(18));
		retorno.setCahrEstado(rs.getInt(19));
		retorno.setCahrEstadoEliminacion(rs.getInt(20));
		
		retorno.setCahrPeriodoAcademicoDto(periodo);
		retorno.setCarhDependenciaDto(dependencia);
		retorno.setCahrCarreraDto(carrera);
		retorno.setCahrPersonaDto(persona);
		return retorno;
	}
	
	private CargaHorariaDto rsAbuscarFuncionesPorPeriodo(ResultSet rs) throws SQLException{
		CargaHorariaDto retorno = new CargaHorariaDto();
		retorno.setTicahrId(rs.getInt(1));
		retorno.setTicahrDescripcion(rs.getString(2));
		return retorno;
	}
	
	private PersonaDto rsAbuscarInformacionDocente(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setPrsId(rs.getInt(1));
		retorno.setPrsIdentificacion(rs.getString(2));
		retorno.setPrsPrimerApellido(rs.getString(3));
		retorno.setPrsSegundoApellido(rs.getString(4));
		retorno.setPrsNombres(rs.getString(5));
		retorno.setPrsSexo(rs.getInt(6));
		retorno.setPrsMailInstitucional(rs.getString(7));
		retorno.setTmddId(rs.getInt(8));
		retorno.setTmddDescripcion(rs.getString(9));
		retorno.setPstDenominacion(rs.getString(10));
		retorno.setPstNivelRangoGradual(rs.getInt(11));
		retorno.setPstCategoria(rs.getInt(12));
		retorno.setRllbId(rs.getInt(13));
		retorno.setRllbDescripcion(rs.getString(14));
		retorno.setDtpsId(rs.getInt(15));
		return retorno;
	} 
}

