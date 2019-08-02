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
   
 ARCHIVO:     ComprobantePagoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-08-2018			Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.CalificacionDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaInformaticaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB ComprobantePagoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Comprobante.
 * @author fgguzman
 * @version 1.0
 */
@Stateless
public class SuficienciaInformaticaServicioJdbcImpl implements SuficienciaInformaticaServicioJdbc {
	
//	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_SUFICIENCIA_INFORMATICA)
	private DataSource dsSuficienciaInformatica;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSiiu;
	
	public List<RecordEstudianteDto> buscarHistorialMatriculasPresencial(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {
		List<RecordEstudianteDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct est.CEDULA_EST,  ");
		sql.append(" est.APELLIDO_EST, ");
		sql.append(" est.NOMBRE_EST, ");
		sql.append(" hrr.horario_horario,  ");
		sql.append(" prd.FECHA_INICIO, ");
		sql.append(" prd.FECHA_FINAL, ");
		sql.append(" nt.NOTA_FINAL, ");
		sql.append(" nt.ESTADO_NOTA ");
		sql.append(" from nota nt, cursoxhorarios cuhr, estudiante est , curso crs, horarios hrr , tipo_curso tpcr, periodo prd ");
		sql.append(" where nt.id_cursoxhorario = cuhr.id_cursoxhorario  ");
		sql.append(" and cuhr.id_est = est.id_est ");
		sql.append(" and cuhr.id_curso = crs.id_curso ");
		sql.append(" and cuhr.id_horario = hrr.id_horario ");
		sql.append(" and tpcr.id_tipocurso = crs.id_tipocurso  ");
		sql.append(" and prd.id_periodo = cuhr.id_periodo ");
		sql.append(" and est.cedula_est = ? ");

		try {
			con = dsSuficienciaInformatica.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarHistorialMatriculasPresencial(rs));
			}

		} catch (Exception e) {
			throw new RecordEstudianteException("Error de conexión, comuníquese con el administrador.");
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
			throw new RecordEstudianteNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}

	public List<RecordEstudianteDto> buscarHistorialMatriculasExoneracion(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {

		List<RecordEstudianteDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" select  ");
		sql.append(" est.CEDULA_EST,  ");
		sql.append(" est.APELLIDO_EST, ");
		sql.append(" est.NOMBRE_EST, ");
		sql.append(" exo.FECHA_PRSUFI, ");
		sql.append(" exo.ESTADO_PRSUFI, ");
		sql.append(" exo.NOTA_PRSUFI, ");
		sql.append(" exo.RESPONSABLE_PRSUFI ");
		sql.append(" from estudiante est, pruebasufi exo ");
		sql.append(" where est.id_est = exo.id_est ");
		sql.append(" and  CEDULA_EST = ? ");

		try {
			con = dsSuficienciaInformatica.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarHistorialMatriculasExoneracion(rs));
			}

		} catch (Exception e) {
			throw new RecordEstudianteException("Error de conexión, comuníquese con el administrador.");
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
			throw new RecordEstudianteNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}

		return retorno;
	}
	

	@Override
	public List<FichaMatriculaDto> buscarMatriculasActivas(String identificacion, Integer[] pracTipo) throws FichaMatriculaNoEncontradoException, FichaMatriculaException {

		List<FichaMatriculaDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT FCMT.FCMT_ID , ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION ,  ");
		sql.append("   FCMT.FCMT_TIPO , ");
		sql.append("   FCMT.FCMT_MODALIDAD , ");
		sql.append("   FCMT.FCMT_VALOR_TOTAL , ");
		sql.append("   FCMT.FCMT_FECHA_MATRICULA , ");
		sql.append("   FCMT.FCMT_ESTADO , ");
		sql.append("   FCES.FCES_ID , ");
		sql.append("   PRAC.PRAC_ID , ");
		sql.append("   PRAC.PRAC_TIPO, ");
		sql.append("   PRAC.PRAC_DESCRIPCION , ");
		sql.append("   PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO , ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   CRR.CRR_ID , ");
		sql.append("   CRR.CRR_DESCRIPCION , ");
		sql.append("   CRR.CRR_TIPO, ");
		sql.append("   CRR.DPN_ID  ");
		sql.append(" FROM FICHA_MATRICULA FCMT , ");
		sql.append("   FICHA_ESTUDIANTE FCES , ");
		sql.append("   FICHA_INSCRIPCION FCIN , ");
		sql.append("   CONFIGURACION_CARRERA CNCR , ");
		sql.append("   CARRERA CRR , ");
		sql.append("   PERSONA PRS , ");
		sql.append("   PERIODO_ACADEMICO PRAC ");
		sql.append(" WHERE FCMT.FCES_ID         = FCES.FCES_ID ");
		sql.append(" AND FCES.FCIN_ID           = FCIN.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID           = CNCR.CNCR_ID ");
		sql.append(" AND CNCR.CRR_ID            = CRR.CRR_ID ");
		sql.append(" AND FCES.PRS_ID            = PRS.PRS_ID ");
		sql.append(" AND FCMT.FCMT_PRAC_ID      = PRAC.PRAC_ID ");
		sql.append(" AND PRAC.PRAC_ESTADO       =  " + PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" AND FCMT.FCMT_ESTADO       =  " + FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE); // DESACTIVAR PARA EXONERACIONES
		sql.append(" AND  prac.prac_tipo in "+Arrays.toString(pracTipo).replace("[", "(").replace("]", ")"));
		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" ORDER BY FCMT.FCMT_NIVEL_UBICACION ");

		try {
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarMatriculasActivas(rs));
			}

		} catch (Exception e) {
			throw new FichaMatriculaException("Error de conexión, comuníquese con el administrador.");
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
			throw new FichaMatriculaNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}
		return retorno;
	}


	@Override
	public List<RecordEstudianteDto> buscarMatriculaActivaPregrado(String identificacion, int periodo) throws FichaMatriculaNoEncontradoException, FichaMatriculaException {

		List<RecordEstudianteDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT DPN.DPN_ID, ");
		sql.append("   DPN.DPN_DESCRIPCION , ");
		sql.append("   CRR.CRR_ID, ");
		sql.append("   CRR.CRR_DESCRIPCION , ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   FCMT.FCMT_ID , ");
		sql.append("   FCMT.FCMT_NIVEL_UBICACION, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION , ");
		sql.append("   PRS.PRS_ID , ");
		sql.append("   PRS.PRS_IDENTIFICACION , ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
		sql.append("   PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   NVL.NVL_ID, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MTR.MTR_CREDITOS, ");
		sql.append("   MTR.MTR_HORAS, ");
		sql.append("   MTR.TIMT_ID, ");
		sql.append("   MLCRMT.MLCRMT_ID, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   DTMT.DTMT_NUMERO, ");
		sql.append("   CNCR.CNCR_ID, ");
		sql.append("   CNCR.MDL_ID ");
		sql.append(" FROM FICHA_ESTUDIANTE FCES INNER JOIN FICHA_INSCRIPCION FCIN  ON FCES.FCIN_ID = FCIN.FCIN_ID ");
		sql.append(" INNER JOIN FICHA_MATRICULA FCMT ON FCES.FCES_ID = FCMT.FCES_ID ");
		sql.append(" INNER JOIN COMPROBANTE_PAGO CMPA ON FCMT.FCMT_ID = CMPA.FCMT_ID ");
		sql.append(" INNER JOIN DETALLE_MATRICULA DTMT  ON CMPA.CMPA_ID = DTMT.CMPA_ID ");
		sql.append(" LEFT JOIN MALLA_CURRICULAR_PARALELO MLCRPR ON DTMT.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" LEFT JOIN PARALELO PRL ON PRL.PRL_ID = MLCRPR.PRL_ID ");
		sql.append(" LEFT JOIN MALLA_CURRICULAR_MATERIA MLCRMT  ON MLCRPR.MLCRMT_ID = MLCRMT.MLCRMT_ID ");
		sql.append(" LEFT JOIN NIVEL NVL ON MLCRMT.NVL_ID = NVL.NVL_ID ");
		sql.append(" LEFT JOIN PERSONA PRS  ON FCES.PRS_ID = PRS.PRS_ID ");
		sql.append(" LEFT JOIN CONFIGURACION_CARRERA CNCR  ON FCIN.CNCR_ID = CNCR.CNCR_ID ");
		sql.append(" LEFT JOIN CARRERA CRR ON CNCR.CRR_ID = CRR.CRR_ID ");
		sql.append(" LEFT JOIN MALLA_CURRICULAR MLCR  ON MLCR.CRR_ID = CRR.CRR_ID ");
		sql.append(" LEFT JOIN PERIODO_ACADEMICO PRAC ON PRL.PRAC_ID = PRAC.PRAC_ID ");
		sql.append(" LEFT JOIN DEPENDENCIA DPN  ON CRR.DPN_ID = DPN.DPN_ID ");
		sql.append(" LEFT JOIN MATERIA MTR ON MTR.MTR_ID = MLCRMT.MTR_ID ");
		sql.append(" INNER JOIN RECORD_ESTUDIANTE RCES  ON FCES.FCES_ID = RCES.FCES_ID   AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
		sql.append(" LEFT JOIN MODALIDAD MDL ON MDL.MDL_ID  = CNCR.MDL_ID ");
		sql.append(" WHERE PRS.PRS_IDENTIFICACION = ? ");
		sql.append(" AND FCMT.FCMT_PRAC_ID        = ? ");
		sql.append(" AND FCMT.FCMT_PRAC_ID NOT IN  (" + PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE+")");
		sql.append(" AND RCES.RCES_ESTADO NOT IN ("+RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE+" , "+RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE+" ) ");
		sql.append(" AND CRR.CRR_TIPO            =  " + CarreraConstantes.TIPO_PREGRADO_VALUE);

		try {
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			pstmt.setInt(2, periodo);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarMatriculaActivaPregrado(rs));
			}

		} catch (Exception e) {
			throw new FichaMatriculaException("Error de conexión, comuníquese con el administrador.");
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
			throw new FichaMatriculaNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}
		
		return retorno;
	}

	/**
	 * Método que permite recuperar el historial academico de la suficiencia en el SIIU 
	 */
	public List<RecordEstudianteDto> buscarHistorialMatriculasSuficiencia(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException {

		List<RecordEstudianteDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT PRS.PRS_ID, ");
		sql.append("   PRS.PRS_IDENTIFICACION, ");
		sql.append("   PRS.PRS_PRIMER_APELLIDO , ");
		sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
		sql.append("   PRS.PRS_NOMBRES, ");
		sql.append("   PRL.PRL_ID, ");
		sql.append("   PRL.PRL_CODIGO, ");
		sql.append("   PRL.PRL_DESCRIPCION, ");
		sql.append("   MTR.MTR_ID, ");
		sql.append("   MTR.MTR_CODIGO, ");
		sql.append("   MTR.MTR_DESCRIPCION, ");
		sql.append("   MLCRPR.MLCRPR_ID, ");
		sql.append("   MLCRPR.MLCRPR_CUPO, ");
		sql.append("   MLCRPR.MLCRPR_INSCRITOS, ");
		sql.append("   MLCRPR.MLCRPR_MODALIDAD, ");
		sql.append("   MLCRPR.MLCRMT_ID, ");
		sql.append("   RCES.RCES_ID, ");
		sql.append("   RCES.RCES_ESTADO, ");
		sql.append("   PRAC.PRAC_ID, ");
		sql.append("   PRAC.PRAC_DESCRIPCION ");
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
		sql.append(" WHERE FCES.FCES_ID         = FCMT.FCES_ID ");
		sql.append(" AND FCMT.FCMT_ID           = CMPG.FCMT_ID ");
		sql.append(" AND CMPG.CMPA_ID           = DTMT.CMPA_ID ");
		sql.append(" AND PRS.PRS_ID             = FCES.PRS_ID ");
		sql.append(" AND USR.PRS_ID             = PRS.PRS_ID ");
		sql.append(" AND RCES.MLCRPR_ID         = MLCRPR.MLCRPR_ID ");
		sql.append(" AND MLCRPR.MLCRPR_ID       = DTMT.MLCRPR_ID ");
		sql.append(" AND MLCRMT.MLCRMT_ID       = MLCRPR.MLCRMT_ID ");
		sql.append(" AND MLCRMT.MTR_ID          = MTR.MTR_ID ");
		sql.append(" AND MTR.CRR_ID             = CRR.CRR_ID ");
		sql.append(" AND FCES.FCES_ID           = RCES.FCES_ID ");
		sql.append(" AND FCIN.FCIN_ID           = FCES.FCIN_ID ");
		sql.append(" AND FCIN.CNCR_ID           = CNCR.CNCR_ID ");
		sql.append(" AND CRR.CRR_ID             = CNCR.CRR_ID ");
		sql.append(" AND CRR.DPN_ID             = DPN.DPN_ID ");
		sql.append(" AND PRAC.PRAC_ID           = FCMT.FCMT_PRAC_ID ");
		sql.append(" AND DPN.DPN_ID             = DPN.DPN_ID ");
		sql.append(" AND NVL.NVL_ID             = MLCRMT.NVL_ID ");
		sql.append(" AND MLCRPR.PRL_ID          = PRL.PRL_ID ");
		sql.append(" AND PRAC.PRAC_TIPO IN ("+PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE+" , " +PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE+" , "+PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE+" ) ");

		sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
		
		
		try {
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();

			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAbuscarHistorialMatriculasSuficiencia(rs));
			}

		} catch (Exception e) {
			throw new RecordEstudianteException("Error de conexión, comuníquese con el administrador.");
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
			throw new RecordEstudianteNoEncontradoException("No se encontró resultados con los parámetros ingresados.");
		}
		
		return retorno;
	}

	public List<PersonaDto> buscarEstudiantesPorParametro(String param , int tipo) throws PersonaException, PersonaNoEncontradoException{
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
		sql.append("   FCMT.FCMT_VALOR_TOTAL,  ");
		sql.append("   CMPA.CMPA_ID, ");
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
		sql.append(" WHERE PRAC.PRAC_TIPO in (" + PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE+" , " + PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE+" , "+PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE +")");
		sql.append(" AND PRAC.PRAC_ESTADO = " + PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);

		if (tipo == GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO) {
			sql.append(" AND PRS.PRS_PRIMER_APELLIDO LIKE ? ");
		}else {
			sql.append(" AND PRS.PRS_IDENTIFICACION LIKE ? ");
		}
		
		sql.append(" ORDER BY PRS.PRS_PRIMER_APELLIDO, PRS.PRS_SEGUNDO_APELLIDO, PRS.PRS_NOMBRES ");

		try {
			
			con = dsSiiu.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, param.toUpperCase()+"%");
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
	
	
	private RecordEstudianteDto transformarResultSetAbuscarHistorialMatriculasSuficiencia(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PersonaDto estudiante = new PersonaDto();
		ParaleloDto paralelo = new ParaleloDto();
		MateriaDto materia = new MateriaDto();
		MallaCurricularParaleloDto malla = new MallaCurricularParaleloDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		estudiante.setPrsId(rs.getInt(1));
		estudiante.setPrsIdentificacion(rs.getString(2));
		estudiante.setPrsPrimerApellido(rs.getString(3));
		estudiante.setPrsSegundoApellido(rs.getString(4));
		estudiante.setPrsNombres(rs.getString(5));
		
		paralelo.setPrlId(rs.getInt(6));
		paralelo.setPrlCodigo(rs.getString(7));
		paralelo.setPrlDescripcion(rs.getString(8));
		
		materia.setMtrId(rs.getInt(9));
		materia.setMtrCodigo(rs.getString(10));
		materia.setMtrDescripcion(rs.getString(11));
		
		malla.setMlcrprId(rs.getInt(12));
		malla.setMlcrprCupo(rs.getInt(13));
		malla.setMlcrprInscritos(rs.getInt(14));
		malla.setMlcrprModalidad(rs.getInt(15));
		malla.setMlcrprMallaCurricularMateriaId(rs.getInt(16));
		
		retorno.setRcesId(rs.getInt(17));
		retorno.setRcesEstado(rs.getInt(18));
		
		periodo.setPracId(rs.getInt(19));
		periodo.setPracDescripcion(rs.getString(20));
		retorno.setRcesModalidadDto(new ModalidadDto(malla.getMlcrprModalidad()));
		
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesParaleloDto(paralelo);
		retorno.setRcesMateriaDto(materia);
		retorno.setRcesMallaCurricularParaleloDto(malla);
		retorno.setRcesPeriodoAcademicoDto(periodo);
		
		return retorno;
	}
	
	private RecordEstudianteDto transformarResultSetAbuscarHistorialMatriculasPresencial(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PersonaDto estudiante = new PersonaDto();
		HoraClaseDto horario = new HoraClaseDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		
		estudiante.setPrsIdentificacion(rs.getString(1));
		estudiante.setPrsApellidosNombres(rs.getString(2)+" " + rs.getString(3));
		horario.setHoclDescripcion(rs.getString(4));
		periodo.setPracFechaIncio(rs.getDate(5));
		periodo.setPracFechaFin(rs.getDate(6));
		retorno.setClfNotaFinal(BigDecimal.valueOf(Double.valueOf(rs.getString(7).replace(",", "."))));
		retorno.setRcesEstadoLabel(rs.getString(8));
		
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesHoraClaseDto(horario);
		retorno.setRcesPeriodoAcademicoDto(periodo);
		retorno.setRcesModalidadDto(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE));
		return retorno;
	}
	

	private RecordEstudianteDto transformarResultSetAbuscarHistorialMatriculasExoneracion(ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		PersonaDto estudiante = new PersonaDto();
		PersonaDto docente = new PersonaDto();
		estudiante.setPrsIdentificacion(rs.getString(1));
		estudiante.setPrsApellidosNombres(rs.getString(2)+" " + rs.getString(3));
		retorno.setRcesFechaPrueba(rs.getDate(4));
		retorno.setRcesEstadoLabel(rs.getString(5));
		retorno.setClfNotaFinal(BigDecimal.valueOf(Double.valueOf(rs.getString(6).replace(",", "."))));
		docente.setPrsApellidosNombres(rs.getString(7));
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesDocente(docente);
		retorno.setRcesModalidadDto(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE));
		return retorno;
	}


	private FichaMatriculaDto rsAbuscarMatriculasActivas(ResultSet rs) throws SQLException{
		FichaMatriculaDto retorno = new FichaMatriculaDto();
		retorno.setFcmtId(rs.getInt(1));
		retorno.setFcmtNivelUbicacion(rs.getInt(2));
		retorno.setFcmtTipo(rs.getInt(3));
		retorno.setFcmtModalidad(rs.getInt(4));
		retorno.setFcmtValorTotal(rs.getBigDecimal(5));
		retorno.setFcmtFechaMatricula(rs.getTimestamp(6));
		retorno.setFcmtEstado(rs.getInt(7));
		retorno.setFcesId(rs.getInt(8));
		retorno.setPracId(rs.getInt(9));
		retorno.setPracTipo(rs.getInt(10));
		retorno.setPracDescripcion(rs.getString(11));
		retorno.setPrsId(rs.getInt(12));
		retorno.setPrsIdentificacion(rs.getString(13));
		retorno.setPrsPrimerApellido(rs.getString(14));
		retorno.setPrsSegundoApellido(rs.getString(15));
		retorno.setPrsNombres(rs.getString(16));
		retorno.setCrrId(rs.getInt(17));
		retorno.setCrrDescripcion(rs.getString(18));
		retorno.setCrrTipo(rs.getInt(19));
		retorno.setDpnId(rs.getInt(20));
		return retorno;
	}
	
	private RecordEstudianteDto rsAbuscarMatriculaActivaPregrado (ResultSet rs) throws SQLException{
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		DependenciaDto dependencia = new DependenciaDto();
		CarreraDto carrera = new CarreraDto();
		ParaleloDto paralelo = new ParaleloDto();
		FichaMatriculaDto matricula = new FichaMatriculaDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		PersonaDto persona = new PersonaDto();
		MateriaDto materia = new MateriaDto();
		
		dependencia.setDpnId(rs.getInt(1));
		dependencia.setDpnDescripcion(rs.getString(2));
		carrera.setCrrId(rs.getInt(3));
		carrera.setCrrDescripcion(rs.getString(4));
		paralelo.setPrlId(rs.getInt(5));
		paralelo.setPrlCodigo(rs.getString(6));
		paralelo.setPrlDescripcion(rs.getString(7));
		matricula.setFcmtId(rs.getInt(8));
		matricula.setFcmtNivelUbicacion(rs.getInt(9));
		periodo.setPracId(rs.getInt(10));
		periodo.setPracDescripcion(rs.getString(11));
		persona.setPrsId(rs.getInt(12));
		persona.setPrsIdentificacion(rs.getString(13));
		persona.setPrsPrimerApellido(rs.getString(14));
		persona.setPrsSegundoApellido(rs.getString(15));
		persona.setPrsNombres(rs.getString(16));
		materia.setNvlId(rs.getInt(17));
		materia.setMtrId(rs.getInt(18));
		materia.setMtrCodigo(rs.getString(19));
		materia.setMtrDescripcion(rs.getString(20));
		materia.setMtrCreditos(rs.getInt(21));
		materia.setMtrHoras(rs.getInt(22));
		materia.setMtrTpmtId(rs.getInt(23));
		retorno.setMlcrmtMtrId(rs.getInt(24));
		retorno.setRcesId(rs.getInt(25));
		retorno.setRcesEstado(rs.getInt(26));
		retorno.setDtmtNumMatricula(rs.getInt(27));
		matricula.setCncrId(rs.getInt(28));
		matricula.setCncrModalidad(rs.getInt(29));
		
		retorno.setRcesDependenciaDto(dependencia);
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesParaleloDto(paralelo);
		retorno.setRcesFichaMatriculaDto(matricula);
		retorno.setRcesPeriodoAcademicoDto(periodo);
		retorno.setRcesEstudianteDto(persona);
		retorno.setRcesMateriaDto(materia);
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
		fichaMatricula.setFcmtValorTotal(rs.getBigDecimal(16));
		fichaMatricula.setCmpaId(rs.getInt(17)); 
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

}

