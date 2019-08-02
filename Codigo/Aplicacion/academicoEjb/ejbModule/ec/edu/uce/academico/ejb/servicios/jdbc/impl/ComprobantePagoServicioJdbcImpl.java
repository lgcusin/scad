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
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.dtos.ArancelDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.dtos.DetalleMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteValidacionException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobantePagoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * EJB ComprobantePagoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla Comprobante.
 * @author fgguzman
 * @version 1.0
 */
@Stateless
public class ComprobantePagoServicioJdbcImpl implements ComprobantePagoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_SYSREC)
	private DataSource dsSysRec;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	private DataSource dsSIIU;
	
	public List<ComprobantePagoDto> buscarComprobantesDePago(String identificacion, String fechaInicioBusqueda) throws ComprobantePagoException, ComprobantePagoNoEncontradoException {
		List<ComprobantePagoDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cbdc.cab_num_doc, ");
		sql.append("   cbdc.cab_fecha_emi, ");
		sql.append("   cbdc.cab_fecha_cad_cp, ");
		sql.append("   cbdc.cab_fecha_pago_cp, ");
		sql.append("   cbdc.estdoc_sec, ");
		sql.append("   esdc.estdoc_nombre, ");
		sql.append("   cl.clie_nombres, ");
		sql.append("   cl.clie_ape_paterno, ");
		sql.append("   cl.clie_ape_materno, ");
		sql.append("   cl.clie_identificador, ");
		sql.append("   cl.clie_email, ");
		sql.append("   cl.clie_direccion, ");
		sql.append("   cl.clie_telefono, ");
		sql.append("   cl.clie_celular, ");
		sql.append("   cbdc.cab_total_pagar ");
		sql.append(" FROM cabecera_documento cbdc, ");
		sql.append("   cliente cl, ");
		sql.append("   estado_documento esdc ");
		sql.append(" WHERE cbdc.clie_sec = cl.clie_sec ");
		sql.append(" AND cbdc.estdoc_sec = esdc.estdoc_sec ");
		sql.append(" and cbdc.cab_fecha_emi >= to_timestamp('"+fechaInicioBusqueda+"', 'YYYY-MM-DD') ");
		sql.append(" AND cl.clie_identificador ilike '"+identificacion+"' ");
		sql.append(" ORDER BY 3 desc  ");
		try {
			con = dsSysRec.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAcomprobanteDto(rs));
			}
			
		} catch (Exception e) {
			throw new ComprobantePagoException("Error de conexión, comuníquese con el administrador.");
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
			throw new ComprobantePagoNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
		}
		
		return retorno;
	}

	public ComprobantePagoDto buscarComprobante (String numero) throws ComprobantePagoException, ComprobantePagoNoEncontradoException, ComprobantePagoValidacionException{
		ComprobantePagoDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CMPA_NUM_COMPROBANTE ");
		sql.append("   ,CMPA_FECHA_EMISION ");
		sql.append("   ,CMPA_FECHA_CADUCA ");
		sql.append("   ,CMPA_FECHA_PAGO ");
		sql.append("   ,CMPA_ESTADO_PAGO ");
		sql.append(" FROM COMPROBANTE_PAGO  ");
		sql.append(" WHERE CMPA_NUM_COMPROBANTE = ? and CMPA_ESTADO_PAGO is null ");
		
		
		try {
			
			con = dsSIIU.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, numero);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno = transformarResultSetAcomprobantePagoDto(rs);
			}
			
		} catch (NoResultException e) {
			throw new ComprobantePagoNoEncontradoException("No se encontró el comprobante solicitado.");
		} catch (NonUniqueResultException e) {
			throw new ComprobantePagoValidacionException("Se encontró mas de un resultado con el número ingresado.");
		} catch (Exception e) {
			throw new ComprobantePagoException("Error de conexión, comuníquese con el administrador.");
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
		
		return retorno;
	}
	
	public List<ComprobantePagoDto> buscarComprobantesDePagoFecha(String fechaInicioBusqueda) throws ComprobantePagoException, ComprobantePagoNoEncontradoException {
		List<ComprobantePagoDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cab.cab_num_doc, ");
		sql.append("   cab.cab_fecha_emi, ");
		sql.append("   cab.cab_fecha_cad_cp, ");
		sql.append("   cab.cab_fecha_pago_cp, ");
		sql.append("   cab.estdoc_sec, ");
		sql.append("   estdoc.estdoc_nombre, ");
		sql.append("   clie.clie_nombres, ");
		sql.append("   clie.clie_ape_paterno, ");
		sql.append("   clie.clie_ape_materno, ");
		sql.append("   clie.clie_identificador, ");
		sql.append("   clie.clie_email, ");
		sql.append("   clie.clie_direccion, ");
		sql.append("   clie.clie_telefono, ");
		sql.append("   clie.clie_celular, ");
		sql.append("   cab.cab_total_pagar ");
		sql.append(" FROM ");
		sql.append(" cabecera_documento cab, cliente clie, estado_documento estdoc, sistema_origen sori ");
		sql.append(" WHERE ");
		sql.append(" clie.clie_sec = cab.clie_sec ");
		sql.append(" AND estdoc.estdoc_sec = cab.estdoc_sec ");
		sql.append(" AND cab.sori_sec = sori.sori_sec ");
		sql.append(" AND cab.estdoc_sec in (5) ");
		sql.append(" AND cab.sori_sec = 5 ");
		sql.append(" AND cab.cab_fecha_pago_cp between '"+fechaInicioBusqueda+" 00:00:00'::timestamp and '"+fechaInicioBusqueda+"  23:59:59'::timestamp ");
		
		try {
			con = dsSysRec.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(transformarResultSetAcomprobanteDtoSysRec(rs));
			}
			
		} catch (Exception e) {
			throw new ComprobantePagoException("Error de conexión, comuníquese con el administrador.");
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
			throw new ComprobantePagoNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
		}
		
		return retorno;
		
	}
	
  
   public BigDecimal buscarValorAPagarMatricula(String identificacion, int carreraId, int periodoId ) throws ComprobantePagoNoEncontradoException, ComprobantePagoException{
	   PreparedStatement pstmt = null;
	   Connection con = null;
	   ResultSet rs = null;
	   BigDecimal retorno = null;
	   
	   StringBuilder sql = new StringBuilder();
	   sql.append(" SELECT SUM (DTMT.DTMT_VALOR_POR_MATERIA) ");
	   sql.append(" from PERSONA PRS, USUARIO USR, ");
	   sql.append(" FICHA_ESTUDIANTE FCES, ");
	   sql.append(" RECORD_ESTUDIANTE RCES, ");
	   sql.append(" MALLA_CURRICULAR_PARALELO MLCRPR, ");
	   sql.append(" FICHA_MATRICULA fcmt, ");
	   sql.append(" COMPROBANTE_PAGO CMPG,  ");
	   sql.append(" DETALLE_MATRICULA DTMT, ");
	   sql.append(" FICHA_INSCRIPCION fcin, ");
	   sql.append(" CONFIGURACION_CARRERA CNCR, CARRERA CRR, DEPENDENCIA DPN ");
	   sql.append(" WHERE fCES.FCES_ID = fcmt.FCES_ID ");
	   sql.append(" AND fcmt.FCMT_ID = CMPG.FCMT_ID ");
	   sql.append(" AND CMPG.CMPA_ID = DTMT.CMPA_ID ");
	   sql.append(" AND PRS.PRS_ID = FCES.PRS_ID ");
	   sql.append(" AND USR.PRS_ID = PRS.PRS_ID ");
	   sql.append(" AND RCES.MLCRPR_ID = MLCRPR.MLCRPR_ID ");
	   sql.append(" AND MLCRPR.MLCRPR_ID = DTMT.MLCRPR_ID ");
	   sql.append(" AND fCES.FCES_ID = RCES.FCES_ID ");
	   sql.append(" AND fcin.FCIN_ID = FCES.FCIN_ID ");
	   sql.append(" AND fcin.CNCR_ID = CNCR.CNCR_ID ");
	   sql.append(" AND CRR.CRR_ID = CNCR.CRR_ID ");
	   sql.append(" AND CRR.DPN_ID = DPN.DPN_ID ");
	   sql.append(" AND CRR.CRR_ID = ? ");
	   sql.append(" AND fcmt.FCMT_PRAC_ID = ? ");
	   sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");
	   
	   try {

		   con = dsSIIU.getConnection();
		   pstmt = con.prepareStatement(sql.toString());
		   pstmt.setInt(1, carreraId);
		   pstmt.setInt(2, periodoId);
		   pstmt.setString(3, identificacion.toUpperCase());
		   rs = pstmt.executeQuery();

		   while (rs.next()){
			   retorno = rs.getBigDecimal(1);			
		   }
		   

	   } catch (Exception e) {
		   throw new ComprobantePagoException("Error de conexión, comuníquese con el administrador.");
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
	   
	   return retorno;

   }
   

   
   public List<RecordEstudianteDto> buscarMatricula(String identificacion, int carreraId, int periodoId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException{
	   PreparedStatement pstmt = null;
	   Connection con = null;
	   ResultSet rs = null;
	   List<RecordEstudianteDto> retorno = null;
	   
	   StringBuilder sql = new StringBuilder();
	   sql.append(" SELECT DISTINCT CRR.CRR_ID, ");
	   sql.append("   CRR.CRR_DESCRIPCION, ");
	   sql.append("   PRS.PRS_ID, ");
	   sql.append("   PRS.PRS_IDENTIFICACION, ");
	   sql.append("   PRS.PRS_PRIMER_APELLIDO, ");
	   sql.append("   PRS.PRS_SEGUNDO_APELLIDO, ");
	   sql.append("   PRS.PRS_NOMBRES, ");
	   sql.append("   FCMT.FCMT_ID, ");
	   sql.append("   FCMT.FCMT_FECHA_MATRICULA, ");
	   sql.append("   FCMT.FCMT_NIVEL_UBICACION, ");
	   sql.append("   FCMT.FCMT_PRAC_ID, ");
	   sql.append("   CMPG.CMPA_ID, ");
	   sql.append("   CMPG.CMPA_NUM_COMPROBANTE, ");
	   sql.append("   DTMT.DTMT_ID, ");
	   sql.append("   DTMT.DTMT_VALOR_POR_MATERIA, ");
	   sql.append("   RCES.RCES_ID, ");
	   sql.append("   RCES.RCES_ESTADO, ");
	   sql.append("   MLCRPR.MLCRPR_ID ");
	   sql.append(" FROM PERSONA PRS , ");
	   sql.append("   USUARIO USR , ");
	   sql.append("   FICHA_ESTUDIANTE FCES , ");
	   sql.append("   RECORD_ESTUDIANTE RCES , ");
	   sql.append("   MALLA_CURRICULAR_PARALELO MLCRPR , ");
	   sql.append("   FICHA_MATRICULA FCMT , ");
	   sql.append("   COMPROBANTE_PAGO CMPG , ");
	   sql.append("   DETALLE_MATRICULA DTMT , ");
	   sql.append("   FICHA_INSCRIPCION FCIN , ");
	   sql.append("   CONFIGURACION_CARRERA CNCR , ");
	   sql.append("   CARRERA CRR , ");
	   sql.append("   DEPENDENCIA DPN , ");
	   sql.append("   MALLA_CURRICULAR_MATERIA MLCRMT , ");
	   sql.append("   MATERIA MTR ");
	   sql.append(" WHERE FCES.FCES_ID         = FCMT.FCES_ID ");
	   sql.append(" AND FCMT.FCMT_ID           = CMPG.FCMT_ID ");
	   sql.append(" AND CMPG.CMPA_ID           = DTMT.CMPA_ID ");
	   sql.append(" AND PRS.PRS_ID             = FCES.PRS_ID ");
	   sql.append(" AND USR.PRS_ID             = PRS.PRS_ID ");
	   sql.append(" AND RCES.MLCRPR_ID         = MLCRPR.MLCRPR_ID ");
	   sql.append(" AND MLCRPR.MLCRPR_ID       = DTMT.MLCRPR_ID ");
	   sql.append(" AND FCES.FCES_ID           = RCES.FCES_ID ");
	   sql.append(" AND FCIN.FCIN_ID           = FCES.FCIN_ID ");
	   sql.append(" AND FCIN.CNCR_ID           = CNCR.CNCR_ID ");
	   sql.append(" AND CRR.CRR_ID             = CNCR.CRR_ID ");
	   sql.append(" AND CRR.DPN_ID             = DPN.DPN_ID ");
	   sql.append(" AND MLCRPR.MLCRMT_ID       = MLCRMT.MLCRMT_ID ");
	   sql.append(" AND MTR.MTR_ID             = MLCRMT.MTR_ID ");
	   sql.append(" AND FCMT.FCMT_PRAC_ID      = ? ");
	   sql.append(" AND CRR.CRR_ID             = ? ");
	   sql.append(" AND PRS.PRS_IDENTIFICACION = ? ");

	   
	   try {
		   retorno = new ArrayList<>();
		   
		   con = dsSIIU.getConnection();
		   pstmt = con.prepareStatement(sql.toString());
		   pstmt.setInt(1, periodoId);
		   pstmt.setInt(2, carreraId);
		   pstmt.setString(3, identificacion);
		   
		   rs = pstmt.executeQuery();
		   while (rs.next()){
			   retorno.add(rsAbuscarMatricula(rs));			
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
   
   public Integer legalizarMatricula(String identificacion, int carreraId, int periodoId) throws RecordEstudianteValidacionException, RecordEstudianteException{
	   PreparedStatement pstmt = null;
	   Connection con = null;
	   ResultSet rs = null;
	   Integer retorno = null;
	   
	   StringBuilder sql = new StringBuilder();
	   sql.append(" UPDATE Record_Estudiante SET Rces_Estado = " + RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
	   sql.append(" WHERE Rces_Id  IN ( ");
	   sql.append("   SELECT DISTINCT Rces.Rces_Id ");
	   sql.append("   FROM Persona Prs , ");
	   sql.append("     Usuario Usr , ");
	   sql.append("     Ficha_Estudiante Fces , ");
	   sql.append("     Record_Estudiante Rces , ");
	   sql.append("     Malla_Curricular_Paralelo Mlcrpr , ");
	   sql.append("     Ficha_Matricula Fcmt , ");
	   sql.append("     Comprobante_Pago Cmpg , ");
	   sql.append("     Detalle_Matricula Dtmt , ");
	   sql.append("     Ficha_Inscripcion Fcin , ");
	   sql.append("     Configuracion_Carrera Cncr , ");
	   sql.append("     Carrera Crr , ");
	   sql.append("     Dependencia Dpn , ");
	   sql.append("     Malla_Curricular_Materia Mlcrmt , ");
	   sql.append("     Materia Mtr ");
	   sql.append("   WHERE Fces.Fces_Id         = Fcmt.Fces_Id ");
	   sql.append("   AND Fcmt.Fcmt_Id           = Cmpg.Fcmt_Id ");
	   sql.append("   AND Cmpg.Cmpa_Id           = Dtmt.Cmpa_Id ");
	   sql.append("   AND Prs.Prs_Id             = Fces.Prs_Id ");
	   sql.append("   AND Usr.Prs_Id             = Prs.Prs_Id ");
	   sql.append("   AND Rces.Mlcrpr_Id         = Mlcrpr.Mlcrpr_Id ");
	   sql.append("   AND Mlcrpr.Mlcrpr_Id       = Dtmt.Mlcrpr_Id ");
	   sql.append("   AND Fces.Fces_Id           = Rces.Fces_Id ");
	   sql.append("   AND Fcin.Fcin_Id           = Fces.Fcin_Id ");
	   sql.append("   AND Fcin.Cncr_Id           = Cncr.Cncr_Id ");
	   sql.append("   AND Crr.Crr_Id             = Cncr.Crr_Id ");
	   sql.append("   AND Crr.Dpn_Id             = Dpn.Dpn_Id ");
	   sql.append("   AND Mlcrpr.Mlcrmt_Id       = Mlcrmt.Mlcrmt_Id ");
	   sql.append("   AND Mtr.Mtr_Id             = Mlcrmt.Mtr_Id ");
	   sql.append("   AND Rces.Rces_Estado       = " + RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
	   sql.append("   AND Fcmt.Fcmt_Prac_Id      = ? ");
	   sql.append("   AND Crr.Crr_Id             = ? ");
	   sql.append("   AND Prs.Prs_Identificacion = ? ) ");


	   try {
		   con = dsSIIU.getConnection();
		   pstmt = con.prepareStatement(sql.toString());
		   pstmt.setInt(1, periodoId);
		   pstmt.setInt(2, carreraId);
		   pstmt.setString(3, identificacion);
		   retorno = pstmt.executeUpdate();
		   
	   } catch (SQLException e) {
		   throw new RecordEstudianteValidacionException("Error de conexión, comuníquese con el administrador.");
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

	   return retorno;
   }
   
   public Integer legalizarMatricula(String identificacion, String numeroComprobante) throws RecordEstudianteValidacionException, RecordEstudianteException{
	   PreparedStatement pstmt = null;
	   Connection con = null;
	   ResultSet rs = null;
	   Integer retorno = null;
	   
	   StringBuilder sql = new StringBuilder();
	   sql.append(" UPDATE Record_Estudiante SET Rces_Estado = " + RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
	   sql.append(" WHERE Rces_Id  IN ( ");
	   sql.append("   SELECT DISTINCT Rces.Rces_Id ");
	   sql.append("   FROM Persona Prs , ");
	   sql.append("     Usuario Usr , ");
	   sql.append("     Ficha_Estudiante Fces , ");
	   sql.append("     Record_Estudiante Rces , ");
	   sql.append("     Malla_Curricular_Paralelo Mlcrpr , ");
	   sql.append("     Ficha_Matricula Fcmt , ");
	   sql.append("     Comprobante_Pago Cmpg , ");
	   sql.append("     Detalle_Matricula Dtmt , ");
	   sql.append("     Ficha_Inscripcion Fcin , ");
	   sql.append("     Configuracion_Carrera Cncr , ");
	   sql.append("     Carrera Crr , ");
	   sql.append("     Dependencia Dpn , ");
	   sql.append("     Malla_Curricular_Materia Mlcrmt , ");
	   sql.append("     Materia Mtr ");
	   sql.append("   WHERE Fces.Fces_Id         = Fcmt.Fces_Id ");
	   sql.append("   AND Fcmt.Fcmt_Id           = Cmpg.Fcmt_Id ");
	   sql.append("   AND Cmpg.Cmpa_Id           = Dtmt.Cmpa_Id ");
	   sql.append("   AND Prs.Prs_Id             = Fces.Prs_Id ");
	   sql.append("   AND Usr.Prs_Id             = Prs.Prs_Id ");
	   sql.append("   AND Rces.Mlcrpr_Id         = Mlcrpr.Mlcrpr_Id ");
	   sql.append("   AND Mlcrpr.Mlcrpr_Id       = Dtmt.Mlcrpr_Id ");
	   sql.append("   AND Fces.Fces_Id           = Rces.Fces_Id ");
	   sql.append("   AND Fcin.Fcin_Id           = Fces.Fcin_Id ");
	   sql.append("   AND Fcin.Cncr_Id           = Cncr.Cncr_Id ");
	   sql.append("   AND Crr.Crr_Id             = Cncr.Crr_Id ");
	   sql.append("   AND Crr.Dpn_Id             = Dpn.Dpn_Id ");
	   sql.append("   AND Mlcrpr.Mlcrmt_Id       = Mlcrmt.Mlcrmt_Id ");
	   sql.append("   AND Mtr.Mtr_Id             = Mlcrmt.Mtr_Id ");
	   sql.append("   AND Rces.Rces_Estado       = " + RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
	   sql.append("   AND Prs.Prs_Identificacion = ?  ");
	   sql.append("   AND Cmpg.CMPA_NUM_COMPROBANTE      = ? )");

	   try {
		   con = dsSIIU.getConnection();
		   pstmt = con.prepareStatement(sql.toString());
		   pstmt.setString(1, identificacion);
		   pstmt.setString(2, numeroComprobante);
		   retorno = pstmt.executeUpdate();
		   
	   } catch (SQLException e) {
		   throw new RecordEstudianteValidacionException("Error de conexión, comuníquese con el administrador.");
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

	   return retorno;
   }
   
   public boolean actualizarComprobateMatricula(String identificacion, ComprobantePagoDto comprobante) throws RecordEstudianteValidacionException, RecordEstudianteException{
	   PreparedStatement pstmt = null;
	   Connection con = null;
	   ResultSet rs = null;
	   boolean retorno = false;
	   
	   StringBuilder sql = new StringBuilder();
	   sql.append(" UPDATE  comprobante_pago set cmpa_estado_pago = " + ComprobantePagoConstantes.ESTADO_PAGADO_VALUE);
	   sql.append(" , CMPA_FECHA_EMISION = to_timestamp('"+GeneralesUtilidades.cambiarDateToStringFormatoFecha(comprobante.getCmpaFechaEmision(), "dd/MM/yyyy")+"', 'dd/MM/yyyy') ");
	   sql.append(" , CMPA_FECHA_CADUCA = to_timestamp('"+GeneralesUtilidades.cambiarDateToStringFormatoFecha(comprobante.getCmpaFechaCaducidad(), "dd/MM/yyyy")+"', 'dd/MM/yyyy') ");
	   sql.append(" , CMPA_FECHA_PAGO = to_timestamp('"+GeneralesUtilidades.cambiarDateToStringFormatoFecha(comprobante.getCmpaFechaPago(), "dd/MM/yyyy")+"', 'dd/MM/yyyy') ");
	   sql.append(" , CMPA_FECHA_ENVIO = to_timestamp('"+GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistemaTimestamp(), "dd/MM/yyyy")+"', 'dd/MM/yyyy') ");//legalizacion
	   sql.append(" , CMPA_VALOR_PAGADO = "+GeneralesUtilidades.cambiarBigDecimalToString(comprobante.getCmpaTotalAPagar(), 1).replace(",", "."));
	   sql.append(" , CMPA_TOTAL_PAGO = "+GeneralesUtilidades.cambiarBigDecimalToString(comprobante.getCmpaTotalAPagar(), 1).replace(",", "."));
	   sql.append(" WHERE CMPA_ID = ( ");
	   sql.append("   SELECT DISTINCT Cmpg.CMPA_ID ");
	   sql.append("   FROM Persona Prs , ");
	   sql.append("     Usuario Usr , ");
	   sql.append("     Ficha_Estudiante Fces , ");
	   sql.append("     Record_Estudiante Rces , ");
	   sql.append("     Malla_Curricular_Paralelo Mlcrpr , ");
	   sql.append("     Ficha_Matricula Fcmt , ");
	   sql.append("     Comprobante_Pago Cmpg , ");
	   sql.append("     Detalle_Matricula Dtmt , ");
	   sql.append("     Ficha_Inscripcion Fcin , ");
	   sql.append("     Configuracion_Carrera Cncr , ");
	   sql.append("     Carrera Crr , ");
	   sql.append("     Dependencia Dpn , ");
	   sql.append("     Malla_Curricular_Materia Mlcrmt , ");
	   sql.append("     Materia Mtr ");
	   sql.append("   WHERE Fces.Fces_Id         = Fcmt.Fces_Id ");
	   sql.append("   AND Fcmt.Fcmt_Id           = Cmpg.Fcmt_Id ");
	   sql.append("   AND Cmpg.Cmpa_Id           = Dtmt.Cmpa_Id ");
	   sql.append("   AND Prs.Prs_Id             = Fces.Prs_Id ");
	   sql.append("   AND Usr.Prs_Id             = Prs.Prs_Id ");
	   sql.append("   AND Rces.Mlcrpr_Id         = Mlcrpr.Mlcrpr_Id ");
	   sql.append("   AND Mlcrpr.Mlcrpr_Id       = Dtmt.Mlcrpr_Id ");
	   sql.append("   AND Fces.Fces_Id           = Rces.Fces_Id ");
	   sql.append("   AND Fcin.Fcin_Id           = Fces.Fcin_Id ");
	   sql.append("   AND Fcin.Cncr_Id           = Cncr.Cncr_Id ");
	   sql.append("   AND Crr.Crr_Id             = Cncr.Crr_Id ");
	   sql.append("   AND Crr.Dpn_Id             = Dpn.Dpn_Id ");
	   sql.append("   AND Mlcrpr.Mlcrmt_Id       = Mlcrmt.Mlcrmt_Id ");
	   sql.append("   AND Mtr.Mtr_Id             = Mlcrmt.Mtr_Id ");
//	   sql.append("   AND Rces.Rces_Estado       = " + RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
	   sql.append("   AND Prs.Prs_Identificacion = ?  ");
	   sql.append("   AND Cmpg.CMPA_NUM_COMPROBANTE      = ? )");

	   try {
		   con = dsSIIU.getConnection();
		   pstmt = con.prepareStatement(sql.toString());
		   pstmt.setString(1, identificacion);
		   pstmt.setString(2, comprobante.getCmpaNumero());
		   pstmt.executeUpdate();
		   
		   retorno = true;
	   } catch (SQLException e) {
		   throw new RecordEstudianteValidacionException("Error de conexión, comuníquese con el administrador.");
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

	   return retorno;
   }

@Override
   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
   public List<ComprobantePagoDto> buscarComprobantesDePagoPagados(String num_comprobante) throws ComprobantePagoException, ComprobantePagoNoEncontradoException {
		List<ComprobantePagoDto> retorno = null;

		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cab_num_doc ");
	
		sql.append(" FROM cabecera_documento  ");
		sql.append(" WHERE cab_num_doc = ? ");
		sql.append(" AND estdoc_sec = 5");
		try {
			con = dsSysRec.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, num_comprobante);
			rs = pstmt.executeQuery();
			
			retorno = new ArrayList<>();
			while(rs.next()){
				ComprobantePagoDto cmpaAux = new ComprobantePagoDto();
				cmpaAux.setCmpaNumero(rs.getString("cab_num_doc"));
				retorno.add(cmpaAux);
			}
			
		} catch (Exception e) {
			throw new ComprobantePagoException("Error de conexión, comuníquese con el administrador.");
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
		
//		if (retorno == null || retorno.size() <= 0) {
//			retorno = null;
//			throw new ComprobantePagoNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
//		}
		
		return retorno;
	}
   
	
	public List<ComprobantePagoDto> buscarComprobantesDePagoInformatica(String identificacion, String fechaInicioBusqueda) throws ComprobantePagoException, ComprobantePagoNoEncontradoException {
		List<ComprobantePagoDto> retorno = null;
	
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
	
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cbdc.cab_num_doc, ");
		sql.append("   cbdc.cab_fecha_emi, ");
		sql.append("   cbdc.cab_fecha_cad_cp, ");
		sql.append("   cbdc.cab_fecha_pago_cp, ");
		sql.append("   cbdc.estdoc_sec, ");
		sql.append("   esdc.estdoc_nombre, ");
		sql.append("   cl.clie_nombres, ");
		sql.append("   cl.clie_ape_paterno, ");
		sql.append("   cl.clie_ape_materno, ");
		sql.append("   cl.clie_identificador, ");
		sql.append("   cl.clie_email, ");
		sql.append("   cl.clie_direccion, ");
		sql.append("   cl.clie_telefono, ");
		sql.append("   cl.clie_celular, ");
		sql.append("   cbdc.cab_total_pagar , ");
		sql.append("   ara.ara_descripcion ");
		sql.append(" FROM cliente cl, ");
		sql.append("   cabecera_documento cbdc, ");
		sql.append("   detalle_documento dtdc, ");
		sql.append("   arancel ara, ");
		sql.append("   estado_documento esdc ");
		sql.append(" WHERE cl.clie_sec   = cbdc.clie_sec ");
		sql.append(" AND cbdc.cab_sec    = dtdc.cab_sec ");
		sql.append(" AND ara.ara_sec     = dtdc.ara_sec ");
		sql.append(" AND cbdc.estdoc_sec = esdc.estdoc_sec ");
		sql.append(" AND ara.ara_sec    IN ( 5301,141,4166,142,4135,4165,5300,5260) ");
		sql.append(" AND cbdc.cab_fecha_emi >= to_timestamp('"+fechaInicioBusqueda+"', 'YYYY-MM-DD') ");
		sql.append(" AND cl.clie_identificador ilike '"+identificacion+"' ");
		sql.append(" ORDER BY 3 desc  ");
		try {
			con = dsSysRec.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
	
			retorno = new ArrayList<>();
			while(rs.next()){
				retorno.add(rsAbuscarComprobantesDePagoInformatica(rs));
			}
	
		} catch (Exception e) {
			throw new ComprobantePagoException("Error de conexión, comuníquese con el administrador.");
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
			throw new ComprobantePagoNoEncontradoException("No se encontró comprobantes con los parámetros ingresados.");
		}
	
		return retorno;
	}

	private ComprobantePagoDto transformarResultSetAcomprobanteDto(ResultSet rs) throws SQLException{
		ComprobantePagoDto retorno = new ComprobantePagoDto();
		PersonaDto beneficiario =  new PersonaDto();
		retorno.setCmpaNumero(rs.getString(1));
		retorno.setCmpaFechaEmision(rs.getTimestamp(2));
		retorno.setCmpaFechaCaducidad(rs.getTimestamp(3));
		retorno.setCmpaFechaPago(rs.getTimestamp(4));
		retorno.setCmpaEstadoValue(rs.getInt(5));
		retorno.setCmpaEstadoLabel(rs.getString(6));
		beneficiario.setPrsNombres(rs.getString(7));
		beneficiario.setPrsPrimerApellido(rs.getString(8));
		beneficiario.setPrsSegundoApellido(rs.getString(9));
		beneficiario.setPrsIdentificacion(rs.getString(10));
		beneficiario.setPrsMailPersonal(rs.getString(11));
		beneficiario.setPrsDireccionDomicilio(rs.getString(12));
		beneficiario.setPrsTelefono(rs.getString(13));
		beneficiario.setPrsTelefonoMovil(rs.getString(14));
		retorno.setCmpaTotalAPagar(rs.getBigDecimal(15));
		retorno.setCmpaEstudianteDto(beneficiario);
		return retorno;
	}
	
	private ComprobantePagoDto transformarResultSetAcomprobanteDtoSysRec(ResultSet rs) throws SQLException{
		ComprobantePagoDto retorno = new ComprobantePagoDto();
		PersonaDto beneficiario =  new PersonaDto();
		retorno.setCmpaNumero(rs.getString(1));
		retorno.setCmpaFechaEmision(rs.getTimestamp(2));
		retorno.setCmpaFechaCaducidad(rs.getTimestamp(3));
		retorno.setCmpaFechaPago(rs.getTimestamp(4));
		retorno.setCmpaEstadoValue(rs.getInt(5));
		retorno.setCmpaEstadoLabel(rs.getString(6));
		beneficiario.setPrsNombres(rs.getString(7));
		beneficiario.setPrsPrimerApellido(rs.getString(8));
		beneficiario.setPrsSegundoApellido(rs.getString(9));
		beneficiario.setPrsIdentificacion(rs.getString(10));
		beneficiario.setPrsMailPersonal(rs.getString(11));
		beneficiario.setPrsDireccionDomicilio(rs.getString(12));
		beneficiario.setPrsTelefono(rs.getString(13));
		beneficiario.setPrsTelefonoMovil(rs.getString(14));
		retorno.setCmpaTotalAPagar(rs.getBigDecimal(15));
		retorno.setCmpaEstudianteDto(beneficiario);
		return retorno;
	}

	private ComprobantePagoDto transformarResultSetAcomprobantePagoDto(ResultSet rs) throws SQLException{
		ComprobantePagoDto retorno = new ComprobantePagoDto();
		retorno.setCmpaNumero(rs.getString(1));
		retorno.setCmpaFechaEmision(rs.getTimestamp(2));
		retorno.setCmpaFechaCaducidad(rs.getTimestamp(3));
		retorno.setCmpaFechaPago(rs.getTimestamp(4));
		retorno.setCmpaEstadoValue(rs.getInt(5));
		return retorno;
	}
	

	   private RecordEstudianteDto rsAbuscarMatricula(ResultSet rs) throws SQLException {
		RecordEstudianteDto retorno = new RecordEstudianteDto();
		CarreraDto carrera = new CarreraDto();
		PersonaDto estudiante = new PersonaDto();
		FichaMatriculaDto fichaMatricula = new FichaMatriculaDto();
		ComprobantePagoDto comprobante = new ComprobantePagoDto();
		DetalleMatriculaDto detalle = new DetalleMatriculaDto();
		
		carrera.setCrrId(rs.getInt(1));
		carrera.setCrrDescripcion(rs.getString(2));
		estudiante.setPrsId(rs.getInt(3));
		estudiante.setPrsIdentificacion(rs.getString(4));
		estudiante.setPrsPrimerApellido(rs.getString(5));
		estudiante.setPrsSegundoApellido(rs.getString(6));
		estudiante.setPrsNombres(rs.getString(7));
		fichaMatricula.setFcmtId(rs.getInt(8));
		fichaMatricula.setFcmtFechaMatricula(rs.getTimestamp(9));
		fichaMatricula.setFcmtNivelUbicacion(rs.getInt(10));
		fichaMatricula.setPracId(rs.getInt(11));
		comprobante.setCmpaId(rs.getInt(12));
		comprobante.setCmpaNumero(rs.getString(13));
		detalle.setDtmtId(rs.getInt(14));
		detalle.setDtmtValorPorMateria(rs.getBigDecimal(15));
		retorno.setRcesId(rs.getInt(16));
		retorno.setRcesEstado(rs.getInt(17));
		retorno.setRcesMallaCurricularParalelo(rs.getInt(18));
		
		retorno.setRcesCarreraDto(carrera);
		retorno.setRcesEstudianteDto(estudiante);
		retorno.setRcesFichaMatriculaDto(fichaMatricula);
		retorno.setRcesComprobantePagoDto(comprobante);
		retorno.setRcesDetalleMatriculaDto(detalle);
		
		return retorno;
	}
	   
	  private ComprobantePagoDto rsAbuscarComprobantesDePagoInformatica(ResultSet rs) throws SQLException{
			ComprobantePagoDto retorno = new ComprobantePagoDto();
			PersonaDto beneficiario =  new PersonaDto();
			ArancelDto arancel = new ArancelDto();
			
			retorno.setCmpaNumero(rs.getString(1));
			retorno.setCmpaFechaEmision(rs.getTimestamp(2));
			retorno.setCmpaFechaCaducidad(rs.getTimestamp(3));
			retorno.setCmpaFechaPago(rs.getTimestamp(4));
			retorno.setCmpaEstadoValue(rs.getInt(5));
			retorno.setCmpaEstadoLabel(rs.getString(6));
			beneficiario.setPrsNombres(rs.getString(7));
			beneficiario.setPrsPrimerApellido(rs.getString(8));
			beneficiario.setPrsSegundoApellido(rs.getString(9));
			beneficiario.setPrsIdentificacion(rs.getString(10));
			beneficiario.setPrsMailPersonal(rs.getString(11));
			beneficiario.setPrsDireccionDomicilio(rs.getString(12));
			beneficiario.setPrsTelefono(rs.getString(13));
			beneficiario.setPrsTelefonoMovil(rs.getString(14));
			retorno.setCmpaTotalAPagar(rs.getBigDecimal(15));
			arancel.setArnDescripcion(rs.getString(16));
			
			retorno.setCmpaEstudianteDto(beneficiario);
			retorno.setCmpaArancelDto(arancel);
			return retorno;
		}
}

