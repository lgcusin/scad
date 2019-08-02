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
   
 ARCHIVO:     PersonaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla Persona.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 25-05-2017		Marcelo Quishpe				       Emisión Inicial
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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CalificacionDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
/**
 * EJB PersonaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Persona.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class PersonaDtoServicioJdbcImpl implements PersonaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	public List<PersonaDto> buscarEstudiantesRegistrados(int periodoId, int dependenciaId) throws PersonaNoEncontradoException, PersonaException {
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES , ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL , ");
		sql.append("   DPN.DPN_ID , ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_PROCESO, ");
		sql.append("   NVL.NVL_NUMERAL ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   FICHA_INSCRIPCION FCIN, ");
		sql.append("   FICHA_ESTUDIANTE FCES, ");
		sql.append("   RECORD_ESTUDIANTE RCES, ");
		sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR, ");
		sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT, ");
		sql.append("   MATERIA MTR, ");
		sql.append("   FICHA_MATRICULA FCMT, ");
		sql.append("   COMPROBANTE_PAGO CMPG, ");
		sql.append("   DETALLE_MATRICULA DTMT, ");
		sql.append("   CONFIGURACION_CARRERA CNCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN, ");
		sql.append("   PERIODO_ACADEMICO PRAC, ");
		sql.append("   NIVEL NVL, ");
		sql.append("   PARALELO PRL ");
		sql.append(" WHERE FCES.FCES_ID    = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_ID      = CMPG.FCMT_ID ");
		sql.append(" AND CMPG.CMPA_ID      = DTMT.CMPA_ID ");
		sql.append(" AND PRS.PRS_ID        = FCES.PRS_ID ");
		sql.append(" AND USR.PRS_ID        = PRS.PRS_ID ");
		sql.append(" AND RCES.MLCRPR_ID    = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID  = DTMT.MLCRPR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID  = MLCRPR.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID     = MTR.MTR_ID ");
		sql.append(" AND MTR.CRR_ID        = CRR.CRR_ID ");
		sql.append(" AND FCES.FCES_ID      = RCES.FCES_ID ");
		sql.append(" AND FCIN.FCIN_ID      = FCES.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID      = CNCR.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID        = CNCR.CRR_ID ");
		sql.append(" AND CRR.DPN_ID        = DPN.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID      = FCMT.FCMT_PRAC_ID ");
		sql.append(" AND DPN.DPN_ID        = DPN.DPN_ID ");
		sql.append(" AND NVL.NVL_ID        = FCMT.FCMT_NIVEL_UBICACION ");
		sql.append(" AND MLCRPR.PRL_ID     = PRL.PRL_ID ");
		sql.append(" AND FCMT.FCMT_PRAC_ID = ? ");
		
		if (dependenciaId != GeneralesConstantes.APP_ID_BASE) {
			sql.append(" AND DPN.DPN_ID        = ? ");	
		}else {
			sql.append(" AND DPN.DPN_ID        > ? ");
		}
		
		sql.append(" AND CRR.Crr_Tipo      =  " + CarreraConstantes.TIPO_PREGRADO_VALUE);
		sql.append(" ORDER BY DPN.DPN_DESCRIPCION, CRR.CRR_DESCRIPCION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES ");

		
		try{

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1 ,  periodoId);
			pstmt.setInt(2 ,  dependenciaId);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(rsAbuscarEstudiantesRegistrados(rs));
			}

		} catch (Exception e) {
			throw new PersonaException("Error de comunicación. Exception general");
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
				e.printStackTrace();
			}
		}
		
		if (retorno.isEmpty()) {
			throw new PersonaNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}
	
	public List<PersonaDto> buscarEstudiantesPorModuloParalelo(int mallaCurricularParaleloId) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.CRR_PROCESO, ");
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   FCMT.FCMT_ID, ");
		sql.append("   Fcmt.Fcmt_Fecha_Matricula, ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION, ");
		sql.append("   FCMT.FCMT_TIPO, ");
		sql.append("   MDL.MDL_DESCRIPCION, ");
		sql.append("   FCMT.FCMT_PRAC_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_CREDITOS, ");
		sql.append("   MTR.MTR_HORAS, ");
		sql.append("   DTMT.DTMT_NUMERO, ");
		sql.append("   NVL.NVL_ID, ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   NVL.NVL_DESCRIPCION, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   Prl.Prl_Descripcion, ");
		sql.append("   CLMD.CLMD_ID, ");
		sql.append("   CLMD.CLMD_NOTA1, ");
		sql.append("   CLMD.CLMD_NOTA2, ");
		sql.append("   CLMD.CLMD_SUPLETORIO, ");
		sql.append("   CLMD.CLMD_SUMA_P1_P2, ");
		sql.append("   CLMD.CLMD_PARAM_RECUPERACION1, ");
		sql.append("   CLMD.CLMD_PARAM_RECUPERACION2, ");
		sql.append("   CLMD.CLMD_NOTA_FINAL_SEMESTRE, ");
		sql.append("   CLMD.CLMD_ASISTENCIA1, ");
		sql.append("   CLMD.CLMD_ASISTENCIA2, ");
		sql.append("   CLMD.CLMD_ASISTENCIA_DOCENTE1, ");
		sql.append("   CLMD.CLMD_ASISTENCIA_DOCENTE2, ");
		sql.append("   CLMD.CLMD_PROMEDIO_ASISTENCIA ");
		sql.append(" FROM PERSONA PRS  ");
		sql.append(" INNER JOIN USUARIO USR ON PRS.PRS_ID = USR.PRS_ID ");
		sql.append(" INNER JOIN USUARIO_ROL USRO ON USR.USR_ID = USRO.USR_ID ");
		sql.append(" INNER JOIN FICHA_INSCRIPCION FCIN ON USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append(" INNER JOIN FICHA_ESTUDIANTE FCES ON FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append(" INNER JOIN FICHA_MATRICULA FCMT ON FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append(" INNER JOIN COMPROBANTE_PAGO CMPA ON FCMT.FCMT_ID = CMPA.FCMT_ID ");
		sql.append(" INNER JOIN DETALLE_MATRICULA DTMT ON CMPA.CMPA_ID = DTMT.CMPA_ID ");
		sql.append(" INNER JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" INNER JOIN RECORD_ESTUDIANTE RCES ON FCES.FCES_ID    = RCES.FCES_ID AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" LEFT JOIN CALIFICACION_MODULO CLMD ON RCES.RCES_ID    = CLMD.RCES_ID ");
		sql.append(" LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append(" LEFT JOIN MALLA_CURRICULAR MLCR ON MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append(" LEFT JOIN CARRERA CRR ON CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append(" LEFT JOIN DEPENDENCIA DPN ON DPN.DPN_ID = CRR.DPN_ID ");
		sql.append(" LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append(" LEFT JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append(" LEFT JOIN NIVEL NVL ON NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append(" LEFT JOIN CONFIGURACION_CARRERA CNCR ON CNCR.CRR_ID = CRR.CRR_ID ");
		sql.append(" LEFT JOIN MODALIDAD MDL ON MDL.MDL_ID = CNCR.MDL_ID ");
		sql.append(" LEFT JOIN MATERIA MTR ON MTR.MTR_ID = MLCRMT.MTR_ID  ");
		sql.append(" WHERE CLMD.MLCRPR_ID_MODULO = ? ");
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO,  PRS.PRS_SEGUNDO_APELLIDO,  PRS.PRS_NOMBRES  ");


		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mallaCurricularParaleloId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarEstudiantesPorAsignaturaParalelo(rs));
			}
			
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}	
		
		return retorno;
	}

	/**
	 * Método que lista los estudiantes que esten listos para el proceso de registro historico
	 * @param listaCarreras - listaCarreras lista de carreras que esta asignado
	 * @param idCarrera - idCarrera id de la carrera seleccionada 
	 * @param rol - rol rol del usuario que va ha consultar 
	 * @param tipoInscripcion - tipoInscripcion tipo de la inscripción
	 * @param estadoInscripcion - estadoInscripcion estado de la inscripción en este caso activa
	 * @param estadoPeriodo - estadoPeriodo estado del periodo a buscar
	 * @param identificacion - identificacion numero de identificación del estudiante a buscar
	 * @return retorna una lista de estudiantes para cargar el historico de matrícula
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public List<PersonaDto> listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(List<CarreraDto> listaCarreras, Integer idCarrera, int rol, int tipoInscripcion, int estadoInscripcion, int estadoPeriodo, String identificacion) throws PersonaDtoException, PersonaDtoNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
	
		
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION_INGRESO);
			sbSql.append(" , cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_ID);
   		    sbSql.append(" , prac.");sbSql.append(JdbcConstantes.PRAC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
        	sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERIODO_ACADEMICO);sbSql.append(" prac ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.USRO_ID);
            sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.PRAC_ID);
    		sbSql.append(" = ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ID);
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);			
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);	
			
			//para las carreras
			if(idCarrera == GeneralesConstantes.APP_ID_BASE){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				for (int i = 0; i < listaCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaCarreras.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");
			}else{//caso para una sola carrera
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			
//			if(!idCarrera.equals(GeneralesConstantes.APP_ID_BASE)){
//				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//				sbSql.append(" = ? ");
//			}
			
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" like ? ");
			if(rol!= GeneralesConstantes.APP_ID_BASE){
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" prac.");sbSql.append(JdbcConstantes.PRAC_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			System.out.println(sbSql.toString());
			int contAux = 0; 
			
			//para las carreras
			if(idCarrera == GeneralesConstantes.APP_ID_BASE){ // caso para setear la lista de carreras
				for (CarreraDto item : listaCarreras) {
					contAux++;
					pstmt.setInt(contAux, item.getCrrId());
				}
			}else{//caso para setear una sola carrrera
				pstmt.setInt(1, idCarrera); 
				contAux++;
			}
			
//			if(!idCarrera.equals(GeneralesConstantes.APP_ID_BASE)){
//				pstmt.setInt(++contAux, idCarrera);
//			}
			
			pstmt.setString(++contAux, "%"+identificacion.toUpperCase()+"%");
			if(rol!= GeneralesConstantes.APP_ID_BASE){
			pstmt.setInt(++contAux, rol);
			}
			pstmt.setInt(++contAux, tipoInscripcion);
			pstmt.setInt(++contAux, estadoPeriodo);
			pstmt.setInt(++contAux, estadoInscripcion);
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PersonaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			
		throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	public PersonaDto buscarPersonaPorIdentificacionRol(int rolId, String identificacion) throws PersonaException, PersonaNoEncontradoException, PersonaValidacionException{
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT DISTINCT");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
		sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
		sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
		sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
		sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
		sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_NICK);
		sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
		sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
		sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.PRS_ID);
		sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
		sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
		sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
		sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);	
		sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);	
		sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ?");
		sbSql.append(" AND ");sbSql.append(" PRS.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, rolId);
			pstmt.setString(2, identificacion); 
			
			rs = pstmt.executeQuery();

			if(rs.next()){
				retorno = rsAbuscarPersonaPorIdentificacionRol(rs);
			}else{
				retorno = null;
			}
			
		} catch (NoResultException e) {
			throw new PersonaNoEncontradoException("No se encontró resultados con los parámetros ingresados");
		} catch (NonUniqueResultException e) {
			throw new PersonaValidacionException("Se encontró mas de un resultado con los parámetros ingresados.");
		} catch (Exception e) {
			throw new PersonaException("Se encontró mas de un resultado con los parámetros ingresados.");
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
		
		return retorno;
	}
	
	/**
	 * Método que busca a la persona con el rol director de carrera de estado activo
	 * @param rolDirCarrera .- id del rol a buscar
	 * @param crrId .- id de la carrera a buscar
	 * @return .- una persona con los parametros de busqueda ingresados
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public PersonaDto buscarPersonaProlDirCarreraPcrrId(int rolDirCarrera, int crrId) throws PersonaDtoException, PersonaDtoNoEncontradoException{
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);	
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);	
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, rolDirCarrera);
			pstmt.setInt(2, crrId); 
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarPersonaProlDirCarreraPcrrId(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception",rolDirCarrera, crrId)));
		}	
		return retorno;
	}
	
	
	/**
	 * Método que busca a la persona con el rol y  facultad
	 * @param rol .- id del rol a buscar
	 * @param fclId .- id de la facultad a buscar
	 * @param estadoRol.- id de la carrera a buscar
	 * @return .- una persona con los parametros de busqueda ingresados
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public PersonaDto buscarPersonaXRolIdXFclId(int rolId, int fclId) throws PersonaDtoException, PersonaDtoNoEncontradoException{
    	PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" , dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ? ");
			sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fclId);
			pstmt.setInt(2, rolId);
			 
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarPersonaXRolIdXFclId(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.por.rol.facultad.sql.exception")));
		} catch (Exception e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.por.rol.facultad.exception")));
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
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.por.rol.facultad.no.encontrado.exception")));
		}	
		return retorno;
	}
	
	
	
	
	/**
	 * Método que busca a la persona con el rol director de carrera por carrera
	 * @param crrId .- crrId  a buscar
	 * @return .- una persona con los parametros de busqueda ingresados
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public PersonaDto buscarDirectorCarreraxidCarrera(int crrId) throws PersonaDtoException, PersonaDtoNoEncontradoException{
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" , usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(RolConstantes.ROL_DIRCARRERA_VALUE);
			sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, crrId); 
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retorno = transformarResultSetADtoBuscarDirectorxidCarrera(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception", crrId)));
		}	
		return retorno;
	}
	
	
	/**MQ
	 * Método que lista los estudiantes que esten listos para el proceso de registro historico
	 * @param listaCarreras - listaCarreras lista de carreras que esta asignado
	 * @param idCarrera - idCarrera id de la carrera seleccionada 
	 * @param rol - rol rol del usuario que va ha consultar 
	 * @param tipoInscripcion - tipoInscripcion tipo de la inscripción
	 * @param estadoInscripcion - estadoInscripcion estado de la inscripción en este caso activa
	 * @param identificacion - identificacion numero de identificación del estudiante a buscar
	 * @return retorna una lista de estudiantes para cargar el historico de matrícula
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public List<PersonaDto> listarXCarreraXRolXTipoInscripcionXestadoInscripcion(List<CarreraDto> listaCarreras, Integer idCarrera, int rol, int tipoInscripcion, int estadoInscripcion, String identificacion) throws PersonaDtoException, PersonaDtoNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
	
		
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_ENCUESTA);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_FECHA_INSCRIPCION);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_MATRICULADO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_OBSERVACION_INGRESO);
			sbSql.append(" , fcin.");sbSql.append(JdbcConstantes.FCIN_PRAC_ID);
			sbSql.append(" , cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" = ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.USRO_ID);
            sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" = ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);			
			sbSql.append(" AND ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);	
			
			//para las carreras
			if(idCarrera == GeneralesConstantes.APP_ID_BASE){ // caso para todas las carreras
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" in ( ");
				
				for (int i = 0; i < listaCarreras.size(); i++) {
					sbSql.append(" ? ");
					if(i != listaCarreras.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");
			}else{//caso para una sola carrera
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			
		
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" like ? ");
			if(rol!= GeneralesConstantes.APP_ID_BASE){
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" = ? ");
			}
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ESTADO);
			sbSql.append(" = ? ");
			sbSql.append(" AND ");sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_TIPO_INGRESO);
			sbSql.append(" is not null ");
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			
			int contAux = 0; 
			
			//para las carreras
			if(idCarrera == GeneralesConstantes.APP_ID_BASE){ // caso para setear la lista de carreras
				for (CarreraDto item : listaCarreras) {
					contAux++;
					pstmt.setInt(contAux, item.getCrrId());
				}
				
			}else{//caso para setear una sola carrrera
				pstmt.setInt(1, idCarrera); 
				contAux++;
			}
			
			pstmt.setString(++contAux, "%"+identificacion.toUpperCase()+"%");
			if(rol!= GeneralesConstantes.APP_ID_BASE){
			pstmt.setInt(++contAux, rol);
			}
			pstmt.setInt(++contAux, tipoInscripcion);
			pstmt.setInt(++contAux, estadoInscripcion);
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PersonaDto>();
			while(rs.next()){
				retorno.add(listarXCarreraXRolXTipoInscripcionXestadoInscripcion(rs));
			}
		} catch (SQLException e) {
			
		throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new PersonaDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		return retorno;
	}
	
	public List<PersonaDto> buscarDocentesPorGrupo(int periodo, int carrera, int grupo) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");
		
		sql.append(" PRS.");sql.append(JdbcConstantes.PRS_ID);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_NOMBRES);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
		sql.append(" , TMDD.");sql.append(JdbcConstantes.TMDD_ID);
		sql.append(" , TMDD.");sql.append(JdbcConstantes.TMDD_DESCRIPCION);
		sql.append(" , RLLB.");sql.append(JdbcConstantes.RLLB_ID);
		sql.append(" , RLLB.");sql.append(JdbcConstantes.RLLB_DESCRIPCION);
		sql.append(" , DTPS.");sql.append(JdbcConstantes.DTPS_ID);
		sql.append(" , PST.");sql.append(JdbcConstantes.PST_DENOMINACION);
		sql.append(" FROM usuario USR , ");
		sql.append("   usuario_rol USRO , ");
		sql.append("   rol ROL , ");
		sql.append("   persona PRS , ");
		sql.append("   ficha_docente FCDC , ");
		sql.append("   detalle_puesto DTPS , ");
		sql.append("   relacion_laboral RLLB , ");
		sql.append("   puesto PST , ");
		sql.append("   tiempo_dedicacion TMDD , ");
		sql.append("   carga_horaria CRHR , ");
		sql.append("   malla_curricular_paralelo MLCRPR , ");
		sql.append("   malla_curricular_materia MLCRMT , ");
		sql.append("   materia MTR , ");
		sql.append("   malla_curricular MLCR , ");
		sql.append("   carrera CRR , ");
		sql.append("   dependencia DPN , ");
		sql.append("   periodo_academico PRAC , ");
		sql.append("   paralelo PRL ");
		sql.append(" WHERE USRO.usr_id    = USR.usr_id ");
		sql.append(" AND USRO.rol_id      = ROL.rol_id ");
		sql.append(" AND USR.prs_id       = PRS.prs_id ");
		sql.append(" AND FCDC.prs_id      = PRS.prs_id ");
		sql.append(" AND DTPS.fcdc_id     = FCDC.fcdc_id ");
		sql.append(" AND DTPS.pst_id      = PST.pst_id ");
		sql.append(" AND DTPS.rllb_id     = RLLB.rllb_id ");
		sql.append(" AND PST.tmdd_id      = TMDD.tmdd_id ");
		sql.append(" AND CRHR.dtps_id     = DTPS.dtps_id ");
		sql.append(" AND CRHR.mlcrpr_id   = MLCRPR.mlcrpr_id ");
		sql.append(" AND MLCRPR.mlcrmt_id = MLCRMT.mlcrmt_id ");
		sql.append(" AND MTR.mtr_id       = MLCRMT.mtr_id ");
		sql.append(" AND MLCRMT.mlcr_id   = MLCR.mlcr_id ");
		sql.append(" AND CRR.dpn_id       = DPN.dpn_id ");
		sql.append(" AND PRAC.prac_id     = PRL.prac_id ");
		sql.append(" AND MLCRPR.prl_id    = PRL.prl_id ");
		sql.append(" AND crhr.crhr_estado = " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND PRAC.prac_id     = ? ");
		sql.append(" AND MLCR.CRR_ID      = ? ");
		sql.append(" AND MTR.GRP_ID       = ?");
		sql.append(" ORDER BY 3  ");

		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodo);
			pstmt.setInt(2, carrera);
			pstmt.setInt(3, grupo);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarRSbuscarDocentesPorGrupo(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PersonaDto> buscarDocentesPorDependencia(int periodo, int dependencia, String param, int tipoBusqueda) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ");
		
		sql.append(" PRS.");sql.append(JdbcConstantes.PRS_ID);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_IDENTIFICACION);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_NOMBRES);
		sql.append(" , PRS.");sql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
		sql.append(" , TMDD.");sql.append(JdbcConstantes.TMDD_ID);
		sql.append(" , TMDD.");sql.append(JdbcConstantes.TMDD_DESCRIPCION);
		sql.append(" , RLLB.");sql.append(JdbcConstantes.RLLB_ID);
		sql.append(" , RLLB.");sql.append(JdbcConstantes.RLLB_DESCRIPCION);
		sql.append(" , DTPS.");sql.append(JdbcConstantes.DTPS_ID);
		sql.append(" , PST.");sql.append(JdbcConstantes.PST_DENOMINACION);
		sql.append(" FROM usuario USR , ");
		sql.append("   usuario_rol USRO , ");
		sql.append("   rol ROL , ");
		sql.append("   persona PRS , ");
		sql.append("   ficha_docente FCDC , ");
		sql.append("   detalle_puesto DTPS , ");
		sql.append("   relacion_laboral RLLB , ");
		sql.append("   puesto PST , ");
		sql.append("   tiempo_dedicacion TMDD , ");
		sql.append("   carga_horaria CRHR , ");
		sql.append("   malla_curricular_paralelo MLCRPR , ");
		sql.append("   malla_curricular_materia MLCRMT , ");
		sql.append("   materia MTR , ");
		sql.append("   malla_curricular MLCR , ");
		sql.append("   carrera CRR , ");
		sql.append("   dependencia DPN , ");
		sql.append("   periodo_academico PRAC , ");
		sql.append("   paralelo PRL ");
		sql.append(" WHERE USRO.usr_id    = USR.usr_id ");
		sql.append(" AND USRO.rol_id      = ROL.rol_id ");
		sql.append(" AND USR.prs_id       = PRS.prs_id ");
		sql.append(" AND FCDC.prs_id      = PRS.prs_id ");
		sql.append(" AND DTPS.fcdc_id     = FCDC.fcdc_id ");
		sql.append(" AND DTPS.pst_id      = PST.pst_id ");
		sql.append(" AND DTPS.rllb_id     = RLLB.rllb_id ");
		sql.append(" AND PST.tmdd_id      = TMDD.tmdd_id ");
		sql.append(" AND CRHR.dtps_id     = DTPS.dtps_id ");
		sql.append(" AND CRHR.mlcrpr_id   = MLCRPR.mlcrpr_id ");
		sql.append(" AND MLCRPR.mlcrmt_id = MLCRMT.mlcrmt_id ");
		sql.append(" AND MTR.mtr_id       = MLCRMT.mtr_id ");
		sql.append(" AND MLCRMT.mlcr_id   = MLCR.mlcr_id ");
		sql.append(" AND CRR.dpn_id       = DPN.dpn_id ");
		sql.append(" AND PRAC.prac_id     = PRL.prac_id ");
		sql.append(" AND MLCRPR.prl_id    = PRL.prl_id ");
		sql.append(" AND MLCR.CRR_ID      = CRR.CRR_ID");
		sql.append(" AND crhr.crhr_estado = " + CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND PRAC.prac_id     = ? ");
		sql.append(" AND DPN.DPN_ID       = ? ");
		
		if (tipoBusqueda == 0) {
			sql.append(" AND PRS.PRS_IDENTIFICACION LIKE ? ");	
		}else {
			sql.append(" AND PRS.PRS_PRIMER_APELLIDO LIKE ? ");	
		}
		
		sql.append(" ORDER BY 3  ");

		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, periodo);
			pstmt.setInt(2, dependencia);
			pstmt.setString(3, param.toUpperCase() + "%");
			
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarRSbuscarDocentesPorDependencia(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
		
		if(retorno == null || retorno.size()<=0){
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	// TODO: tipo carrera
	public List<PersonaDto> buscarDocentesPorCarrera(int crrTipo, int tipoBusqueda, String param) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES , ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL, ");
		sql.append("   FCDC.FCDC_ID , ");
		sql.append("   DTPS.DTPS_ID ");
		sql.append(" FROM USUARIO USR , ");
		sql.append("   PERSONA PRS , ");
		sql.append("   FICHA_DOCENTE FCDC , ");
		sql.append("   DETALLE_PUESTO DTPS , ");
		sql.append("   PUESTO PST , ");
		sql.append("   CARRERA CRR ");
		sql.append(" WHERE PRS.PRS_ID     = USR.PRS_ID ");
		sql.append(" AND PRS.PRS_ID       = FCDC.PRS_ID ");
		sql.append(" AND FCDC.FCDC_ID     = DTPS.FCDC_ID ");
		sql.append(" AND PST.PST_ID       = DTPS.PST_ID ");
		sql.append(" AND CRR.CRR_ID       = DTPS.CRR_ID ");
		sql.append(" AND DTPS.DTPS_ESTADO =  "+DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);// no importa el estado
		// traer solamente el max() y con ese seguir el proceso
		sql.append(" AND PST.TIPS_ID      =  "+TipoPuestoConstantes.TIPO_DOCENTE_VALUE);
		sql.append(" AND CRR.CRR_TIPO = ? ");
		
		if (tipoBusqueda == GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO) {
			sql.append("  AND PRS.PRS_PRIMER_APELLIDO LIKE ? ");
		}else {
			sql.append("  AND PRS.PRS_IDENTIFICACION LIKE ? ");
		}
		
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO ");

		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, crrTipo);
			pstmt.setString(2, param.toUpperCase() + "%");
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarRSbuscarDocentesPorCarrera(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PersonaDto> buscarEstudiantePorIdentificacion(String identificacion) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_INSCRIPCION FCIN, ");
		sql.append("   FICHA_ESTUDIANTE FCES, ");
		sql.append("   CONFIGURACION_CARRERA CNCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN ");
		sql.append(" WHERE PRS.PRS_ID = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID   = USRO.USR_ID ");
		sql.append(" AND USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append(" AND FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append(" AND CNCR.CNCR_ID = FCIN.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID   = CNCR.CRR_ID ");
		sql.append(" AND DPN.DPN_ID   = CRR.DPN_ID ");
		sql.append(" AND CRR.CRR_TIPO = " + CarreraConstantes.TIPO_PREGRADO_VALUE);
		sql.append(" AND USRO.ROL_ID  = " + RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND PRS.PRS_IDENTIFICACION  = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarRSbuscarEstudiantesPorCarrera(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PersonaDto> buscarEstudiantesPorCarrera(int carreraId) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_INSCRIPCION FCIN, ");
		sql.append("   FICHA_ESTUDIANTE FCES, ");
		sql.append("   FICHA_MATRICULA FCMT, ");
		sql.append("   CONFIGURACION_CARRERA CNCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN ");
		sql.append(" WHERE PRS.PRS_ID = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID   = USRO.USR_ID ");
		sql.append(" AND USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append(" AND FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append(" AND FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append(" AND CNCR.CNCR_ID = FCIN.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID   = CNCR.CRR_ID ");
		sql.append(" AND DPN.DPN_ID   = CRR.DPN_ID ");
		sql.append(" AND USRO.ROL_ID  = " + RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND CRR.CRR_ID   = ? ");
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, carreraId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarRSbuscarEstudiantesPorCarrera(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PersonaDto> buscarEstudiantePorIdentificacionCarrera(String identificacion, int carreraId) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRS.PRS_MAIL_INSTITUCIONAL, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION ");
		sql.append(" FROM PERSONA PRS, ");
		sql.append("   USUARIO USR, ");
		sql.append("   USUARIO_ROL USRO, ");
		sql.append("   FICHA_INSCRIPCION FCIN, ");
		sql.append("   FICHA_ESTUDIANTE FCES, ");
		sql.append("   FICHA_MATRICULA FCMT, ");
		sql.append("   CONFIGURACION_CARRERA CNCR, ");
		sql.append("   CARRERA CRR, ");
		sql.append("   DEPENDENCIA DPN ");
		sql.append(" WHERE PRS.PRS_ID = USR.PRS_ID ");
		sql.append(" AND USR.USR_ID   = USRO.USR_ID ");
		sql.append(" AND USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append(" AND FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append(" AND FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append(" AND CNCR.CNCR_ID = FCIN.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID   = CNCR.CRR_ID ");
		sql.append(" AND DPN.DPN_ID   = CRR.DPN_ID ");
		sql.append(" AND PRS.PRS_IDENTIFICACION  = ? ");
		sql.append(" AND CRR.CRR_ID   =  ? ");
		sql.append(" AND USRO.ROL_ID  = " + RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
		sql.append(" AND FCIN.FCIN_ESTADO = " + FichaInscripcionConstantes.ESTADO_RETIRO_ACTIVO_VALUE);
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, carreraId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarRSbuscarEstudiantesPorCarrera(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.no.result.exception")));
		}	
		
		return retorno;
	}
	
	public List<PersonaDto> buscarEstudiantesPorAsignaturaParalelo(int mallaCurricularParaleloId) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.CRR_PROCESO, ");
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   FCMT.FCMT_ID, ");
		sql.append("   Fcmt.Fcmt_Fecha_Matricula, ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION,  ");
		sql.append("   FCMT.FCMT_TIPO, ");
		sql.append("   MDL.MDL_DESCRIPCION, ");
		sql.append("   FCMT.FCMT_PRAC_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_CREDITOS, ");
		sql.append("   MTR.MTR_HORAS, ");
		sql.append("   DTMT.DTMT_NUMERO, ");
		sql.append("   NVL.NVL_ID,  ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   NVL.NVL_DESCRIPCION, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   Prl.Prl_Descripcion, ");
		sql.append("   CLF.CLF_ID, ");
		sql.append("   CLF.CLF_NOTA1, ");
		sql.append("   CLF.CLF_NOTA2, ");
		sql.append("   CLF.CLF_SUPLETORIO, ");
		sql.append("   CLF.CLF_SUMA_P1_P2, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION1, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION2, ");
		sql.append("   CLF.CLF_NOTA_FINAL_SEMESTRE, ");
		sql.append("   CLF.CLF_ASISTENCIA1, ");
		sql.append("   CLF.CLF_ASISTENCIA2, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE1, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE2, ");
		sql.append("   CLF.CLF_PROMEDIO_ASISTENCIA   ");
		sql.append(" FROM PERSONA PRS INNER JOIN USUARIO USR ON PRS.PRS_ID = USR.PRS_ID ");
		sql.append("                  INNER JOIN USUARIO_ROL USRO ON USR.USR_ID = USRO.USR_ID ");
		sql.append("                  INNER JOIN FICHA_INSCRIPCION FCIN ON USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append("                  INNER JOIN FICHA_ESTUDIANTE FCES ON FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append("                  INNER JOIN FICHA_MATRICULA FCMT ON FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append("                  INNER JOIN COMPROBANTE_PAGO CMPA ON FCMT.FCMT_ID = CMPA.FCMT_ID ");
		sql.append("                  INNER JOIN DETALLE_MATRICULA DTMT ON CMPA.CMPA_ID = DTMT.CMPA_ID ");
		sql.append("                  INNER JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  INNER JOIN RECORD_ESTUDIANTE RCES ON FCES.FCES_ID = RCES.FCES_ID  AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR MLCR ON MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append("                  LEFT JOIN CARRERA CRR ON CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append("                  LEFT JOIN DEPENDENCIA DPN ON DPN.DPN_ID = CRR.DPN_ID ");
		sql.append("                  LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append("                  LEFT JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append("                  LEFT JOIN NIVEL NVL ON NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append("                  LEFT JOIN CONFIGURACION_CARRERA CNCR ON CNCR.CRR_ID = CRR.CRR_ID ");
		sql.append("                  LEFT JOIN MODALIDAD MDL ON MDL.MDL_ID  = CNCR.MDL_ID ");
		sql.append("                  LEFT JOIN MATERIA MTR ON MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append("                  LEFT JOIN CALIFICACION CLF ON RCES.RCES_ID = CLF.RCES_ID ");
		sql.append(" WHERE MLCRPR.MLCRPR_ID = ? ");
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO, PRS.PRS_SEGUNDO_APELLIDO, PRS.PRS_NOMBRES ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mallaCurricularParaleloId);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarEstudiantesPorAsignaturaParalelo(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}	
		
		return retorno;
	}
	
	public List<PersonaDto> buscarEstudiantesPorFechaExoneracion(String fecha) throws PersonaException, PersonaNoEncontradoException{
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION, ");
		sql.append("   DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION, ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION, ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.CRR_PROCESO, ");
		sql.append("   PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   FCMT.FCMT_ID, ");
		sql.append("   Fcmt.Fcmt_Fecha_Matricula, ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION,  ");
		sql.append("   FCMT.FCMT_TIPO, ");
		sql.append("   MDL.MDL_DESCRIPCION, ");
		sql.append("   FCMT.FCMT_PRAC_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_CREDITOS, ");
		sql.append("   MTR.MTR_HORAS, ");
		sql.append("   DTMT.DTMT_NUMERO, ");
		sql.append("   NVL.NVL_ID,  ");
		sql.append("   NVL.NVL_NUMERAL, ");
		sql.append("   NVL.NVL_DESCRIPCION, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   Prl.Prl_Descripcion, ");
		sql.append("   CLF.CLF_ID, ");
		sql.append("   CLF.CLF_NOTA1, ");
		sql.append("   CLF.CLF_NOTA2, ");
		sql.append("   CLF.CLF_SUPLETORIO, ");
		sql.append("   CLF.CLF_SUMA_P1_P2, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION1, ");
		sql.append("   CLF.CLF_PARAM_RECUPERACION2, ");
		sql.append("   CLF.CLF_NOTA_FINAL_SEMESTRE, ");
		sql.append("   CLF.CLF_ASISTENCIA1, ");
		sql.append("   CLF.CLF_ASISTENCIA2, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE1, ");
		sql.append("   CLF.CLF_ASISTENCIA_DOCENTE2, ");
		sql.append("   CLF.CLF_PROMEDIO_ASISTENCIA   ");
		sql.append(" FROM PERSONA PRS INNER JOIN USUARIO USR ON PRS.PRS_ID = USR.PRS_ID ");
		sql.append("                  INNER JOIN USUARIO_ROL USRO ON USR.USR_ID = USRO.USR_ID ");
		sql.append("                  INNER JOIN FICHA_INSCRIPCION FCIN ON USRO.USRO_ID = FCIN.USRO_ID ");
		sql.append("                  INNER JOIN FICHA_ESTUDIANTE FCES ON FCIN.FCIN_ID = FCES.FCIN_ID ");
		sql.append("                  INNER JOIN FICHA_MATRICULA FCMT ON FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append("                  INNER JOIN COMPROBANTE_PAGO CMPA ON FCMT.FCMT_ID = CMPA.FCMT_ID ");
		sql.append("                  INNER JOIN DETALLE_MATRICULA DTMT ON CMPA.CMPA_ID = DTMT.CMPA_ID ");
		sql.append("                  INNER JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  INNER JOIN RECORD_ESTUDIANTE RCES ON FCES.FCES_ID = RCES.FCES_ID  AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT ON MLCRMT.MLCRMT_ID = MLCRPR.MLCRMT_ID ");
		sql.append("                  LEFT JOIN MALLA_CURRICULAR MLCR ON MLCR.MLCR_ID = MLCRMT.MLCR_ID ");
		sql.append("                  LEFT JOIN CARRERA CRR ON CRR.CRR_ID = MLCR.CRR_ID ");
		sql.append("                  LEFT JOIN DEPENDENCIA DPN ON DPN.DPN_ID = CRR.DPN_ID ");
		sql.append("                  LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append("                  LEFT JOIN PERIODO_ACADEMICO PRAC ON PRAC.PRAC_ID = PRL.PRAC_ID ");
		sql.append("                  LEFT JOIN NIVEL NVL ON NVL.NVL_ID = MLCRMT.NVL_ID ");
		sql.append("                  LEFT JOIN CONFIGURACION_CARRERA CNCR ON CNCR.CRR_ID = CRR.CRR_ID ");
		sql.append("                  LEFT JOIN MODALIDAD MDL ON MDL.MDL_ID  = CNCR.MDL_ID ");
		sql.append("                  LEFT JOIN MATERIA MTR ON MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append("                  LEFT JOIN CALIFICACION CLF ON RCES.RCES_ID = CLF.RCES_ID ");
		sql.append(" WHERE PRL.PRL_CODIGO LIKE ? ");
		sql.append(" ORDER BY PRL.PRL_CODIGO, PRS.PRS_PRIMER_APELLIDO, PRS.PRS_SEGUNDO_APELLIDO, PRS.PRS_NOMBRES ");

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, fecha.toUpperCase() + "%");
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarEstudiantesPorAsignaturaParalelo(rs));
			}
		} catch (SQLException e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.sql.exception")));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaDto.buscar.carrera.rol.tipo.inscripcion.estado.periodo.exception")));
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
			throw new PersonaNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}	
		
		return retorno;
	}
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	private PersonaDto transformarRSbuscarEstudiantesPorCarrera(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setPrsId(rs.getInt(1));
		retorno.setPrsIdentificacion(rs.getString(2));
		retorno.setPrsPrimerApellido(rs.getString(3));
		retorno.setPrsSegundoApellido(rs.getString(4));
		retorno.setPrsNombres(rs.getString(5));
		retorno.setPrsMailInstitucional(rs.getString(6));
		retorno.setCrrId(rs.getInt(7));
		retorno.setCrrDescripcion(rs.getString(8));
		retorno.setDpnId(rs.getInt(9));
		retorno.setDpnDescripcion(rs.getString(10));
		return retorno;
	}

	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private PersonaDto transformarResultSetADto(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));			
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinTipoIngreso(rs.getInt(JdbcConstantes.FCIN_TIPO_INGRESO));
		retorno.setFcinObservacionIngreso(rs.getString(JdbcConstantes.FCIN_OBSERVACION_INGRESO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));			
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));				
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));			
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setPracId(rs.getInt(JdbcConstantes.PRAC_ID));
		retorno.setPracDescripcion(rs.getString(JdbcConstantes.PRAC_DESCRIPCION));

		return retorno;
	} 

	
	/**
	 * Método privado que sirve para trasformar los datos del resulset de metodo BuscarPersonaProlDirCarreraPcrrId
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private PersonaDto transformarResultSetADtoBuscarPersonaProlDirCarreraPcrrId(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		
		return retorno;
	} 
	
	
	private PersonaDto rsAbuscarPersonaPorIdentificacionRol(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		
		return retorno;
	} 
	
	private PersonaDto transformarResultSetADtoBuscarDirectorxidCarrera(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		
		return retorno;
	} 
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset de metodo BuscarPersonaProlDirCarreraPcrrId
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private PersonaDto transformarResultSetADtoBuscarPersonaXRolIdXFclId(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
		return retorno;
	} 
	
	
	/**
	 * Método privado que sirve para trasformar los datos del resulset
	 * @param rs - rs parámetros de ingreso
	 * @return retorna el dto seteado con los datos correctos
	 * @throws SQLException - SQLException Excepción de error en la consulta sql
	 */
	private PersonaDto listarXCarreraXRolXTipoInscripcionXestadoInscripcion(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFcinId(rs.getInt(JdbcConstantes.FCIN_ID));			
		retorno.setFcinEstado(rs.getInt(JdbcConstantes.FCIN_ESTADO));
		retorno.setFcinTipoIngreso(rs.getInt(JdbcConstantes.FCIN_TIPO_INGRESO));
		retorno.setFcinObservacionIngreso(rs.getString(JdbcConstantes.FCIN_OBSERVACION_INGRESO));
		retorno.setFcinEncuesta(rs.getInt(JdbcConstantes.FCIN_ENCUESTA));
		retorno.setFcinFechaInscripcion(rs.getTimestamp(JdbcConstantes.FCIN_FECHA_INSCRIPCION));			
		retorno.setFcinMatriculado(rs.getInt(JdbcConstantes.FCIN_MATRICULADO));				
		retorno.setFcinObservacion(rs.getString(JdbcConstantes.FCIN_OBSERVACION));	
		retorno.setFcinPeriodoAcademico(rs.getInt(JdbcConstantes.FCIN_PRAC_ID));	
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
	
		return retorno;
	} 

	private PersonaDto transformarRSbuscarDocentesPorCarrera(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setPrsId(rs.getInt(1));
		retorno.setPrsIdentificacion(rs.getString(2));
		retorno.setPrsPrimerApellido(rs.getString(3));
		retorno.setPrsSegundoApellido(rs.getString(4));
		retorno.setPrsNombres(rs.getString(5));
		retorno.setPrsMailInstitucional(rs.getString(6));
		retorno.setFcdcId(rs.getInt(7));
		retorno.setDtpsId(rs.getInt(8));
		return retorno;
	}
	
	private PersonaDto transformarRSbuscarDocentesPorGrupo(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
		
		return retorno;
	} 
	
	private PersonaDto transformarRSbuscarDocentesPorDependencia(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTmddId(rs.getInt(JdbcConstantes.TMDD_ID));
		retorno.setTmddDescripcion(rs.getString(JdbcConstantes.TMDD_DESCRIPCION));
		retorno.setRllbId(rs.getInt(JdbcConstantes.RLLB_ID));
		retorno.setRllbDescripcion(rs.getString(JdbcConstantes.RLLB_DESCRIPCION));
		retorno.setDtpsId(rs.getInt(JdbcConstantes.DTPS_ID));
		retorno.setPstDenominacion(rs.getString(JdbcConstantes.PST_DENOMINACION));
		
		return retorno;
	} 
	
	
	private PersonaDto rsAbuscarEstudiantesPorAsignaturaParalelo(ResultSet rs) throws SQLException {
		PersonaDto retorno = new PersonaDto();
		
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		FichaMatriculaDto fichaMatricula = new FichaMatriculaDto();
		ModalidadDto modalidad = new ModalidadDto();
		MateriaDto materia = new MateriaDto();
		NivelDto nivel = new NivelDto();
		ParaleloDto paralelo = new ParaleloDto();
		RecordEstudianteDto record = new RecordEstudianteDto();
		CalificacionDto calificacion = new CalificacionDto();
		
		periodo.setPracId(rs.getInt(1));
		periodo.setPracDescripcion(rs.getString(2));
		
		dependencia.setDpnId(rs.getInt(3));
		dependencia.setDpnDescripcion(rs.getString(4));

		carrera.setCrrId(rs.getInt(5));
		carrera.setCrrDescripcion(rs.getString(6));
		carrera.setCrrTipo(rs.getInt(7));
		carrera.setCrrProceso(rs.getInt(8));

		retorno.setPrsId(rs.getInt(9));
		retorno.setPrsIdentificacion(rs.getString(10));
		retorno.setPrsPrimerApellido(rs.getString(11));
		retorno.setPrsSegundoApellido(rs.getString(12));
		retorno.setPrsNombres(rs.getString(13));
		
		fichaMatricula.setFcmtId(rs.getInt(14));
		fichaMatricula.setFcmtFechaMatricula(rs.getTimestamp(15));
		fichaMatricula.setFcmtNivelUbicacion(rs.getInt(16));
		fichaMatricula.setFcmtTipo(rs.getInt(17)); 
		modalidad.setMdlDescripcion(rs.getString(18));
		fichaMatricula.setPracId(rs.getInt(19));
		
		materia.setMtrId(rs.getInt(20));
		materia.setMtrCodigo(rs.getString(21));
		materia.setMtrDescripcion(rs.getString(22));
		materia.setMtrCreditos(rs.getInt(23));
		materia.setMtrHoras(rs.getInt(24));
		materia.setNumMatricula(rs.getInt(25));
		
		nivel.setNvlId(rs.getInt(26));
		nivel.setNvlNumeral(rs.getInt(27));
		nivel.setNvlDescripcion(rs.getString(28));
		
		record.setRcesId(rs.getInt(29));
		record.setRcesEstado(rs.getInt(30));
		record.setRcesMallaCurricularParalelo(rs.getInt(31));
		
		paralelo.setPrlId(rs.getInt(32));
		paralelo.setPrlCodigo(rs.getString(33));
		paralelo.setPracDescripcion(rs.getString(34));
		
		calificacion.setClfIsDisable(Boolean.FALSE);
		calificacion.setClfId(rs.getInt(35));
		calificacion.setClfNota1(rs.getBigDecimal(36));
		calificacion.setClfNota2(rs.getBigDecimal(37));
		calificacion.setClfSupletorio(rs.getBigDecimal(38));
		calificacion.setClfSumaNotas(rs.getBigDecimal(39));
		calificacion.setClfParamRecuperacion1(rs.getBigDecimal(40));
		calificacion.setClfParamRecuperacion2(rs.getBigDecimal(41));
		calificacion.setClfNotaFinalSemestre(rs.getBigDecimal(42));
		calificacion.setClfAsistencia1(rs.getBigDecimal(43));
		calificacion.setClfAsistencia2(rs.getBigDecimal(44));
		calificacion.setClfAsistenciaDocente1(rs.getBigDecimal(45));
		calificacion.setClfAsistenciaDocente2(rs.getBigDecimal(46));
		calificacion.setClfPorcentajeAsistencia(rs.getBigDecimal(47)); 
		

		retorno.setPrsPeriodoAcademicoDto(periodo);
		retorno.setPrsDependenciaDto(dependencia);
		retorno.setPrsCarreraDto(carrera);
		retorno.setPrsFichaMatriculaDto(fichaMatricula);
		retorno.setPrsModalidadDto(modalidad);
		retorno.setPrsMateriaDto(materia);
		retorno.setPrsNivelDto(nivel);
		retorno.setPrsParaleloDto(paralelo);
		retorno.setPrsRecordEstudianteDto(record);
		retorno.setPrsCalificacionDto(calificacion);
		return retorno;
	}
	
	private PersonaDto rsAbuscarEstudiantesRegistrados(ResultSet rs) throws SQLException {
		PersonaDto retorno = new PersonaDto();
		NivelDto nivel = new NivelDto();
		retorno.setPrsId(rs.getInt(1));
		retorno.setPrsIdentificacion(rs.getString(2));
		retorno.setPrsPrimerApellido(rs.getString(3));
		retorno.setPrsSegundoApellido(rs.getString(4));
		retorno.setPrsNombres(rs.getString(5));
		retorno.setPrsMailInstitucional(rs.getString(6));
		retorno.setDpnId(rs.getInt(7));
		retorno.setDpnDescripcion(rs.getString(8));
		retorno.setCrrId(rs.getInt(9));
		retorno.setCrrDescripcion(rs.getString(10));
		retorno.setCrrProceso(rs.getInt(11));
		nivel.setNvlNumeral(rs.getInt(12));
		retorno.setPrsNivelDto(nivel);
		return retorno;
	}
	
}

