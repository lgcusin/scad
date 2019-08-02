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
   
 ARCHIVO:     EstudianteDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc del DTO de estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 31-MAY-2018 		  Fatima Tobar			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.dtos.ReportesGeneralesDto;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReporteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;

/**
 * EJB EstudianteDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc del DTO de estudiante.
 * @author fktobar.
 * @version 1.0
 */
@Stateless
public class ReportesGeneralesDtoServicioJdbcImpl implements ReporteDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@Resource(lookup=GeneralesConstantes.APP_DATA_SOURCE_SAU)
	DataSource dsSAU;
	
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	/* ********Consultar Total de Matriculados Niveles****** */
	
	@Override
	public List<ReportesGeneralesDto> buscarTotalMatriculadosxCarreraXNivel(int idPeriodo, int idCarrera,int idNiveles, List<Dependencia> listaDependencia) throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException {

		List<ReportesGeneralesDto> retorno=new ArrayList<ReportesGeneralesDto>();
		PreparedStatement pstmt = null; //forma de comunicarse mediante una sentencia hacia la base de datos
		Connection con = null; // la conexión a la base de datos 
		ResultSet rs = null; // objeto donde se guarda los resultados de la consulta
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(", count(*) AS total ");
			if (idNiveles!=GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(",  nivel_descripcion, nivel_id ");
			}
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
			sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpg, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin, ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			if (idNiveles!=GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(", (SELECT ");
				sbSql.append(JdbcConstantes.NVL_DESCRIPCION);sbSql.append(" nivel_descripcion ,");
				sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" nivel_id FROM ");
				sbSql.append(JdbcConstantes.TABLA_NIVEL);
				sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.NVL_ID);sbSql.append(" =? ) ");
			}
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = fcmt.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" = cmpg.");sbSql.append(JdbcConstantes.FCMT_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" AND ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");
			sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = dpn.");
			sbSql.append(JdbcConstantes.DPN_ID);
			
			
			if (idPeriodo==GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
				sbSql.append(" > ");sbSql.append(" ? ");
			}else {
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
				sbSql.append(" = ");sbSql.append(" ? ");
			}
//			if (idDependencia==GeneralesConstantes.APP_ID_BASE) {
//			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
//			sbSql.append(" > ");sbSql.append(" ? ");
//		}
//		
//		else {
//			sbSql.append(" AND ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_ID);
//			sbSql.append(" = ");sbSql.append(" ? ");
//		}
			
			if (idCarrera==GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" > ");sbSql.append(" ? ");
			}else {
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = ");sbSql.append(" ? ");
			}
			
			if (idNiveles!=GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(" AND fcmt. ");sbSql.append(JdbcConstantes.FCMT_ID);sbSql.append(" in ");
				
				sbSql.append(" (SELECT DISTINCT");
				sbSql.append(" cmpa. ");sbSql.append(JdbcConstantes.FCMT_ID);
				sbSql.append(" FROM ");
				sbSql.append(JdbcConstantes.TABLA_DETALLE_MATRICULA);sbSql.append(" dtmt, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_PARALELO);sbSql.append(" mlcrpr, ");
				sbSql.append(JdbcConstantes.TABLA_MALLA_CURRICULAR_MATERIA);sbSql.append(" mlcrmt, ");
				sbSql.append(JdbcConstantes.TABLA_NIVEL);sbSql.append(" nvl, ");
				sbSql.append(JdbcConstantes.TABLA_PARALELO);sbSql.append(" prl, ");
				sbSql.append(JdbcConstantes.TABLA_COMPROBANTE_PAGO);sbSql.append(" cmpa ");
				sbSql.append(" WHERE");
				sbSql.append(" dtmt.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" = mlcrpr.");sbSql.append(JdbcConstantes.MLCRPR_ID);
				sbSql.append(" AND ");
				sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" = mlcrmt.");sbSql.append(JdbcConstantes.MLCRMT_ID);
				sbSql.append(" AND ");
				sbSql.append(" mlcrpr.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" = prl.");sbSql.append(JdbcConstantes.PRL_ID);
				sbSql.append(" AND ");
				sbSql.append(" mlcrmt.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" = nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" AND ");
				sbSql.append(" nvl.");sbSql.append(JdbcConstantes.NVL_ID);
				sbSql.append(" = ");sbSql.append(" ? ");
				sbSql.append(" AND ");
				sbSql.append(" prl.");sbSql.append(JdbcConstantes.PRAC_ID);
				sbSql.append(" = ");sbSql.append(" ? ");
				sbSql.append(" AND ");
				sbSql.append(" cmpa.");sbSql.append(JdbcConstantes.CMPA_ID);
				sbSql.append(" = dtmt.");sbSql.append(JdbcConstantes.CMPA_ID);sbSql.append(" ) ");	
				sbSql.append(" AND (");
				
				for (int i=0;i<listaDependencia.size();i++) {
					
					sbSql.append("dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(listaDependencia.get(i).getDpnId());
					if(i<listaDependencia.size()-1) {
						sbSql.append(" OR ");
					}else {
						sbSql.append(" ) ");
					}
				}
			}
			
			sbSql.append(" GROUP BY ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			if (idNiveles!=GeneralesConstantes.APP_ID_BASE) {
			sbSql.append(" ,nivel_descripcion");
			sbSql.append(" ,nivel_id");
			}
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			System.out.println(sbSql.toString());
			System.out.println("idPeriodo"+idPeriodo+"idDependencia"+"idCarrera"+idCarrera+"idNiveles"+idNiveles);
			
			if (idNiveles!=GeneralesConstantes.APP_ID_BASE) {
				pstmt.setInt(1, idNiveles); //cargo el id de nivel
				/*pstmt.setInt(2, idDependencia); //cargo el id de nivel*/
				pstmt.setInt(2, idPeriodo); //cargo el id de nivel
				pstmt.setInt(3, idCarrera); //cargo el id de nivel
				pstmt.setInt(4, idNiveles); //cargo el id de nivel
				pstmt.setInt(5, idPeriodo); //cargo el id de nivel
			}else {
				pstmt.setInt(1, idNiveles); //cargo el id de nivel
				/*pstmt.setInt(2, idDependencia); //cargo el id de nivel*/
				pstmt.setInt(2, idPeriodo); //cargo el id de nivel
				pstmt.setInt(3, idCarrera); //cargo el id de nivel
			}

			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs,idNiveles));
			}
			
				
		} catch (NoResultException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			// TODO: handle finally clause
		}
		return retorno;
	} 
				
	

		
	
	/* ******* Consultar Total de Solicitudes terceras matriculas******** */

	
	@Override
	public List<ReportesGeneralesDto> buscarTotalSolicitudesTerceras(int idPeriodo, int idEstado , List<Dependencia> listaDependencia ) throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException {
		List<ReportesGeneralesDto> retorno=new ArrayList<ReportesGeneralesDto>();
		PreparedStatement pstmt = null; //forma de comunicarse mediante una sentencia hacia la base de datos
		Connection con = null; // la conexión a la base de datos 
		ResultSet rs = null; // objeto donde se guarda los resultados de la consulta
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(", prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(", prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(", sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_TIPO);
			sbSql.append(", sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
			sbSql.append(", crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(", mtr.");sbSql.append(JdbcConstantes.MTR_DESCRIPCION);
			sbSql.append(", dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_SOLICITUD_TERCERA_MATRICULA);sbSql.append(" sltrmt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_INSCRIPCION);sbSql.append(" fcin, ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_MATRICULA);sbSql.append(" fcmt, ");
			sbSql.append(JdbcConstantes.TABLA_MATERIA);sbSql.append(" mtr, ");
			sbSql.append(JdbcConstantes.TABLA_DEPENDENCIA);sbSql.append(" dpn ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" = fcin.");sbSql.append(JdbcConstantes.FCIN_ID);
			sbSql.append(" AND ");
			sbSql.append(" fcin.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND ");
			sbSql.append("cncr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);	
			sbSql.append(" AND ");
			sbSql.append("fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = fcmt.");sbSql.append(JdbcConstantes.FCMT_FCES_ID);
			sbSql.append(" AND ");
			sbSql.append("mtr.");sbSql.append(JdbcConstantes.MTR_ID);
			sbSql.append(" = sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_MTR_ID);
			sbSql.append(" AND ");
			sbSql.append("crr.");sbSql.append(JdbcConstantes.CRR_DPN_ID);
			sbSql.append(" = dpn.");sbSql.append(JdbcConstantes.DPN_ID);
			if (idPeriodo== GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
				sbSql.append(" > ");sbSql.append(" ? ");
			}else {
				sbSql.append(" AND ");sbSql.append(" fcmt.");sbSql.append(JdbcConstantes.FCMT_PRAC_ID);
				sbSql.append(" = ");sbSql.append(" ? ");
			}
				sbSql.append(" AND ");sbSql.append(" sltrmt.");sbSql.append(JdbcConstantes.SLTRMT_ESTADO);
				sbSql.append(" = ");sbSql.append(" ? ");
				
				sbSql.append(" AND (");
			for (int i=0;i<listaDependencia.size();i++) {
				
				sbSql.append("dpn.");sbSql.append(JdbcConstantes.DPN_ID);sbSql.append(" = ");sbSql.append(listaDependencia.get(i).getDpnId());
				if(i<listaDependencia.size()-1) {
					sbSql.append(" OR ");
				}else {
					sbSql.append(" ) ");
				}
			} 
			sbSql.append(" ORDER BY ");sbSql.append(" dpn.");sbSql.append(JdbcConstantes.DPN_DESCRIPCION);
			sbSql.append(" ,");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			System.out.println(sbSql.toString());
			System.out.println("idPeriodo"+idPeriodo+"idEstado"+idEstado);
				pstmt.setInt(1, idPeriodo); //cargo el id de nivel
				pstmt.setInt(2, idEstado); //cargo el id de nivel
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoSolicitudes(rs));
			}
			
			
		} catch (NoResultException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			// TODO: handle finally clause
		}
		return retorno;
	} 
	
@Override
	public List<ReportesGeneralesDto> buscarRecordEstudianteSAU(int facultad, int especodigo, int periodoId, String identificacion, int matriculaCurso, int matriculaEstado)	throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException {
		List<ReportesGeneralesDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT DISTINCT  ");
		sql.append(" e.est_codigo as codigo_estudiante, e.est_cedula as cedula, trim(e.est_apellido_paterno) AS apellido_paterno,  ");
		sql.append(" trim(e.est_apellido_materno) AS apellido_materno, trim(e.est_nombres) AS nombres, f.nombre as facultad,  ");
		sql.append(" ee.espe_nombre as carrera,  mtr.matr_curso as semestre,p.per_descripcion as periodo  ");
		sql.append(" FROM registro_materias rm  ");
		sql.append(" LEFT JOIN materias mat ON rm.mat_codigo = mat.mat_codigo  ");
		sql.append(" LEFT JOIN matriculas mtr ON rm.per_codigo = mtr.per_codigo AND rm.est_codigo = mtr.est_codigo  ");
		sql.append(" LEFT JOIN periodos p ON mtr.per_codigo=p.per_codigo  ");
		sql.append(" LEFT JOIN especialidades_escuela ee ON mtr.espe_codigo = ee.espe_codigo  ");
		sql.append(" LEFT JOIN escuelas s ON ee.esc_codigo = s.esc_codigo AND p.esc_codigo = s.esc_codigo ");
		sql.append(" LEFT JOIN facultad f ON s.id_facultad = f.id  ");
		sql.append(" LEFT JOIN estudiantes e ON mtr.est_codigo = e.est_codigo  ");
		sql.append(" INNER JOIN hor_materia hm ON mat.mat_codigo = hm.id_materia  ");
		sql.append(" INNER JOIN prf_profesor prf ON hm.id_profesor = prf.pro_codigo ");
		sql.append(" INNER JOIN hor_paralelo hp ON hm.id_paralelo = hp.id AND  hp.espe_codigo = ee.espe_codigo AND hp.id_periodo = p.per_codigo ");
		sql.append(" AND rm.rmat_paralelo = hp.numero AND rm.per_codigo = hp.id_periodo  ");
		sql.append(" WHERE   ");
		sql.append(" f.id = ?  ");
		sql.append(" and ee.espe_codigo =? ");
		sql.append(" AND s.esc_codigo NOT IN (-1, 5, 6, 60, 980, 1655, 79) ");
		sql.append("  AND p.per_codigo =?  ");
		if(identificacion!=null) {
			sql.append(" AND prf.pro_cedula like '%");sql.append(GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion));sql.append("%'");
		}
		if(matriculaCurso==GeneralesConstantes.APP_ID_BASE) {
			sql.append(" AND mtr.matr_curso > ?  ");
		}else {
			sql.append(" AND mtr.matr_curso = ?  ");
		}
		if(matriculaEstado==GeneralesConstantes.APP_ID_BASE) {
			sql.append(" AND mtr.matr_estado > ?  ");
		}else {
			sql.append(" AND mtr.matr_estado = ?  ");
		}
		sql.append(" ORDER BY f.nombre, ee.espe_nombre, mtr.matr_curso, apellido_paterno, apellido_materno, nombres  ");
		try{
			con = dsSAU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
//			System.out.println(sql.toString());
//			System.out.println("facultas"+facultad+"especodigo"+especodigo+"periodoId"+periodoId+"identificacion"+identificacion+"matriculaCurso"+matriculaCurso+"matriculaEstado"+matriculaEstado);
			pstmt.setInt(1, facultad); //cargo el id de nivel
			pstmt.setInt(2, especodigo); //cargo el id de nivel
			pstmt.setInt(3, periodoId); //cargo el id de nivel
			pstmt.setInt(4, matriculaCurso); //cargo el id de nivel
			pstmt.setInt(5, matriculaEstado); //cargo el id de nivel
			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSauDto(rs));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			retorno = null; 
			throw new ReportesGeneralesDtoException("No se encontró resultados.");
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

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
		}

		return retorno;
	}
	
	@Override
	public Integer buscarCrrIdporEspeCodigoSAU(int espe_codigo)	throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
	
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT esc_codigo from especialidades_escuela where espe_codigo=  ");
		sql.append(espe_codigo);
		try {
			con = dsSAU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retorno = rs.getInt("esc_codigo");
			}
		}catch (Exception e) {
			e.printStackTrace();
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
			} catch (Exception e2) {
			}
		}
			
		return retorno;
	}

	
	@Override
	public List<ReportesGeneralesDto> buscarRecordEstudianteSIIU(int facultad, int codigocarrera, int periodoId, String identificacion)	throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException {
		List<ReportesGeneralesDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT DISTINCT  ");
		sql.append(" prs.prs_identificacion,fcmt_nivel_ubicacion,prac.prac_descripcion ");
		sql.append(" FROM PERSONA prs,FICHA_ESTUDIANTE fces,FICHA_MATRICULA fcmt,RECORD_ESTUDIANTE rces, MALLA_CURRICULAR_PARALELO mlcrpr,  ");
		sql.append(" MALLA_CURRICULAR_MATERIA mlcrmt,MATERIA mtr,CARRERA crr, DEPENDENCIA dpn , PERIODO_ACADEMICO prac  ");
		sql.append(" WHERE  ");
		sql.append(" prs.PRS_ID = fces.PRS_ID  ");
		sql.append(" AND fces.FCES_ID = fcmt.FCES_ID  ");
		sql.append(" AND fces.FCES_ID = rces.FCES_ID  ");
		sql.append(" AND mlcrpr.MLCRPR_ID = rces.MLCRPR_ID  ");
		sql.append(" AND mlcrmt.MLCRMT_ID = mlcrpr.MLCRMT_ID ");
		sql.append(" AND mtr.MTR_ID = mlcrmt.MTR_ID  ");
		sql.append(" AND crr.CRR_ID = mtr.CRR_ID  ");
		sql.append(" AND dpn.DPN_ID = crr.DPN_ID  ");
		sql.append(" and prac.prac_id=fcmt.FCMT_PRAC_ID ");
		sql.append(" AND fcmt.FCMT_PRAC_ID =?   ");
		sql.append("AND dpn.DPN_ID = ? ");
		sql.append("And  crr.CRR_ID = ?");
	if(identificacion!=null) {
			sql.append(" AND prf.prs_identificacion like '%");sql.append(GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion));sql.append("%'");
		}
//		if(matriculaCurso==GeneralesConstantes.APP_ID_BASE) {
//			sql.append(" AND mtr.matr_curso > ?  ");
//		}else {
//			sql.append(" AND mtr.matr_curso = ?  ");
//		}
//		if(matriculaEstado==GeneralesConstantes.APP_ID_BASE) {
//			sql.append(" AND mtr.matr_estado > ?  ");
//		}else {
//			sql.append(" AND mtr.matr_estado = ?  ");
//		}
		sql.append(" ORDER BY dpn.dpn_descripcion, crr.crr_descripcion, prs.primer_apellido, prs.nombres  ");
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			
//			System.out.println(sql.toString());
//			System.out.println("facultas"+facultad+"especodigo"+especodigo+"periodoId"+periodoId+"identificacion"+identificacion+"matriculaCurso"+matriculaCurso+"matriculaEstado"+matriculaEstado);
			pstmt.setInt(1, periodoId); //cargo el id de nivel
			pstmt.setInt(2, facultad); //cargo el id de nivel
			pstmt.setInt(3, codigocarrera); //cargo el id de nivel
//			pstmt.setInt(4, matriculaCurso); //cargo el id de nivel
//			pstmt.setInt(5, matriculaEstado); //cargo el id de nivel
			rs = pstmt.executeQuery();
			retorno = new ArrayList<>();
			while (rs.next()) {
				retorno.add(transformarResultSetArecordAcademicoSIIUDto(rs));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			retorno = null; 
			throw new ReportesGeneralesDtoException("No se encontró resultados.");
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

		if (retorno == null || retorno.size() <= 0) {
			retorno = null;
		}

		return retorno;
	}
	
	
	
	@Override
	public Integer buscarDpnIdporCarreraidSAU(int crr_id)	throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
	
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT id_facultad from escuelas where esc_codigo=  ");
		sql.append(crr_id);
		try {
			con = dsSAU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retorno = rs.getInt("id_facultad");
			}
		}catch (Exception e) {
			e.printStackTrace();
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
			} catch (Exception e2) {
			}
		}
			
		return retorno;
	}


	
		/* ********************************************************************************* *
		 * ************************** METODOS DE TRANSFORMACION **************************** *
		 * ********************************************************************************* */
	
	//Metodo Total matriculados por niveles 

	private ReportesGeneralesDto transformarResultSetADto(ResultSet rs,int nivel) throws SQLException{
				int valAux = 0;
				java.sql.Date fecha = null;
				@SuppressWarnings("unused")
				Timestamp fechaTime = null;
				ReportesGeneralesDto retorno = new ReportesGeneralesDto();
				if (nivel != GeneralesConstantes.APP_ID_BASE) {
					retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
					retorno.setRcesNumeroMatriculados(rs.getInt("total"));
						retorno.setNvlId(rs.getInt("nivel_id"));
						retorno.setNvlDescripcion(rs.getString("nivel_descripcion"));
										
				}
				
				return retorno;
			} 
			

	//Metodo Resultados terceras matriculas
	
			private ReportesGeneralesDto transformarResultSetADtoSolicitudes(ResultSet rs) throws SQLException{
				
				ReportesGeneralesDto retorno = new ReportesGeneralesDto();
				retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				retorno.setSltrmtTipo(rs.getInt(JdbcConstantes.SLTRMT_TIPO));
				retorno.setSltrmtEstado(rs.getInt(JdbcConstantes.SLTRMT_ESTADO));				
				retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				retorno.setDpnDescripcion(rs.getString(JdbcConstantes.DPN_DESCRIPCION));
				retorno.setMtrDescripcion(rs.getString(JdbcConstantes.MTR_DESCRIPCION));
				return retorno;

				
			}
			
			
			//Metodo Consulta Historial de Estudiante SAU
			
			private ReportesGeneralesDto transformarResultSetArecordAcademicoSauDto(ResultSet rs) throws SQLException{
				
				ReportesGeneralesDto retorno = new ReportesGeneralesDto();
				retorno.setPrsIdentificacion(rs.getString("cedula"));
				retorno.setPrsPrimerApellido(rs.getString("apellido_paterno"));
				retorno.setPrsSegundoApellido(rs.getString("apellido_materno"));
				retorno.setPrsNombres(rs.getString("nombres"));			
				retorno.setDpnDescripcion(rs.getString("facultad"));
				retorno.setCrrDescripcion(rs.getString("carrera"));
				retorno.setNvlDescripcion(rs.getString("semestre"));
				retorno.setPracDescripcion(rs.getString("periodo"));
				
				return retorno;

				
			}
			
			
			
			private ReportesGeneralesDto transformarResultSetArecordAcademicoSIIUDto(ResultSet rs) throws SQLException{
				
				ReportesGeneralesDto retorno = new ReportesGeneralesDto();
				retorno.setPrsIdentificacion(rs.getString("cedula"));
				retorno.setPrsPrimerApellido(rs.getString("apellido_paterno"));
				retorno.setPrsSegundoApellido(rs.getString("apellido_materno"));
				retorno.setPrsNombres(rs.getString("nombres"));			
				retorno.setDpnDescripcion(rs.getString("facultad"));
				retorno.setCrrDescripcion(rs.getString("carrera"));
				retorno.setNvlDescripcion(rs.getString("semestre"));
				retorno.setPracDescripcion(rs.getString("periodo"));
				
				return retorno;

				
			}

}

