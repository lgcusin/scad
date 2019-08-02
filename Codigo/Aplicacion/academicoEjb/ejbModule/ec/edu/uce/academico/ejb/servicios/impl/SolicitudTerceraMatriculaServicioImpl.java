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

 ARCHIVO:     SolicitudTerceraMatriculaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla SolicitudTerceraMatricula. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 31-ENE-2018           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
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

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.SolicitudTerceraMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)SolicitudTerceraMatriculaServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)  
public class SolicitudTerceraMatriculaServicioImpl implements SolicitudTerceraMatriculaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	
	@Resource  
	private SessionContext session;  
	/**
	 * Busca una entidad SolicitudTerceraMatricula por su id
	 * @param id - de la SolicitudTerceraMatricula a buscar
	 * @return SolicitudTerceraMatricula con el id solicitado
	 * @throws SolicitudTerceraMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Persona con el id solicitado
	 * @throws SolicitudTerceraMatriculaException - Excepcion general
	 */
	@Override
	public SolicitudTerceraMatricula buscarPorId(Integer id) throws SolicitudTerceraMatriculaNoEncontradoException, SolicitudTerceraMatriculaException {
		SolicitudTerceraMatricula retorno = null;
		if (id != null) {
			try {
				retorno = em.find(SolicitudTerceraMatricula.class, id);
			} catch (NoResultException e) {
				throw new SolicitudTerceraMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new SolicitudTerceraMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new SolicitudTerceraMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.exception")));
			}
		}else{
			throw new SolicitudTerceraMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.no.result.exception",id)));
		}
		return retorno;
	}
	
	/**
	 * Busca si existe al menos una  entidad Sltrmt por  id del rces 
	 * @param idRces -id del Rces a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idRecordEstudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	@Override
	public boolean buscarPorRcesId(Integer rcesId)  {
		boolean retorno=false;
		if (rcesId != null) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select sltrmt from SolicitudTerceraMatricula sltrmt ");
				sbsql.append(" where sltrmt.sltrmtRecordEstudiante.rcesId =:rcesId ");
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("rcesId", rcesId);
			    q.getSingleResult(); 
				retorno = true;
			} catch (NoResultException e) {
				retorno = false; // retorna falso si no existe alguna sltrmtr relacionada con record
				return retorno;
			}catch (NonUniqueResultException e) {
				retorno = true; // retorna verdader si  existe varios sltrmt  relacionados con record
				return retorno;
			} catch (Exception e) {
				retorno = true; // retorna verdader si  existe error
				return retorno;
			} 
		}
		return retorno;
	}
	
	/**
	 * Busca si existe al menos una  entidad Sltrmt por  SubId Sltrmtr, se ha realizado una apelacion de una solicitud
	 * @param idSltrmt -id del solicitudTerceraMatricula a buscar
	 * @return true si encuentra por lo menos una Sltrmt con idSltrmt o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	@Override
	public boolean buscarPorSltrmtId(Integer sltrmtId)  {
		boolean retorno=false;
		if (sltrmtId != null) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select sltrmt from SolicitudTerceraMatricula sltrmt ");
				sbsql.append(" where sltrmt.sltrmtSubId.sltrmtId =:sltrmtId ");
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("sltrmtId", sltrmtId);
			    q.getSingleResult(); 
				retorno = true;
			} catch (NoResultException e) {
				retorno = false; // retorna falso si no existe alguna sltrmtr relacionada con record
				return retorno;
			}catch (NonUniqueResultException e) {
				retorno = true; // retorna verdader si  existe varios sltrmt  relacionados con record
				return retorno;
			} catch (Exception e) {
				retorno = true; // retorna verdader si  existe error
				return retorno;
			} 
		}
		return retorno;
	}
	
	
	/**MQ
	 *  Busca si existe al menos una  entidad Sltrmt por  id del materia y la id de fichaEstudiante
	 * @param mtrId -id de la Materia a buscar
	 * @param fcesId -id de la FichaEstudiante a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idMateria y id Ficha estudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	@Override
	public boolean buscarXMtrIdXFcesId(Integer mtrId, Integer fcesId)  {
		boolean retorno=false;
		if ((mtrId != null) && (fcesId!=null)) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select sltrmt from SolicitudTerceraMatricula sltrmt ");
				sbsql.append(" where ");
				sbsql.append(" sltrmt.sltrmtMtrId =:mtrId ");
				sbsql.append(" and  sltrmt.sltrmtFcesId =:fcesId ");
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("mtrId", mtrId);
			    q.setParameter("fcesId", fcesId);
			    q.getSingleResult(); 
				retorno = true;
			} catch (NoResultException e) {
				retorno = false; // retorna falso si no existe alguna sltrmtr relacionada con record
				return retorno;
			}catch (NonUniqueResultException e) {
				retorno = true; // retorna verdader si  existe varios sltrmt  relacionados con record
				return retorno;
			} catch (Exception e) {
				retorno = true; // retorna verdader si  existe error
				return retorno;
			} 
		}
		return retorno;
	}
	
	/**MQ
	 * Busca si existe al menos una  entidad Sltrmt por  id del materia y la id de fichaEstudiante con estado del registro
	 * @param mtrId -id de la Materia a buscar
	 * @param fcesId -id de la FichaEstudiante a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idMateria y id Ficha estudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	@Override
	public boolean buscarXMtrIdXFcesIdXEstado(Integer mtrId, Integer fcesId, Integer estadoRegistro)  {
		boolean retorno=false;
		if ((mtrId != null) && (fcesId!=null)) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select sltrmt from SolicitudTerceraMatricula sltrmt ");
				sbsql.append(" where ");
				sbsql.append(" sltrmt.sltrmtMtrId =:mtrId ");
				sbsql.append(" and  sltrmt.sltrmtFcesId =:fcesId ");
				sbsql.append(" and  sltrmt.sltrmtEstadoRegistro =:estadoRegistro ");
				
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("mtrId", mtrId);
			    q.setParameter("fcesId", fcesId);
			    q.setParameter("estadoRegistro", estadoRegistro);
			    //System.out.println(sbsql.toString());
			    q.getSingleResult(); 
				retorno = true;
			} catch (NoResultException e) {
				retorno = false; // retorna falso si no existe alguna sltrmtr relacionada con record
				return retorno;
			}catch (NonUniqueResultException e) {
				retorno = true; // retorna verdader si  existe varios sltrmt  relacionados con record
				return retorno;
			} catch (Exception e) {
				retorno = true; // retorna verdader si  existe error
				return retorno;
			} 
		}
		return retorno;
	}
	/**
	 * Crea la entidad SolicitudTerceraMatricula y edita RecordEstudiante
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @param estadoSltrmtSolicitud  -  estado del la solicitud a guardarse en la tabla solicitudTerceraMatricula
	 * @param estadoRcesSolicitud - estado de SolicitudTerceraMatri en la tabla RecordEstudiante
	 * @param periodoActivo - en que se crea la solicitud.
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarSolicitudTerceraMatricula(List<MateriaDto> listMateria,  int estadoSltrmtSolicitud, PeriodoAcademico periodoActivo, Usuario usr) throws Exception, SolicitudTerceraMatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//*********************** RECORD ESTUDIANTIL ********************************************
			//********************** DE LAS MATERIAS SELECCIONADAS **********************************
			//***************************************************************************************
		  
			Boolean cargadoDocumentosTodos= false;
			
			for (MateriaDto item : listMateria) {
				if (item.getSltrmtDocumentoSolicitud() != null) {
					cargadoDocumentosTodos = true;
				} else {
					cargadoDocumentosTodos = false;
					break;
				}
			}
			
			if(cargadoDocumentosTodos==true){
			if(listMateria != null){
				for (MateriaDto item : listMateria) {

    				RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
				//	rcesAux.setRcesEstadoTerceraMatri(estadoRcesSolicitud);  // cambio el estadoTerceraMatri en Rces
    				Causal cslAux= em.find(Causal.class, item.getCslId());//Busco el causal
					PeriodoAcademico pracAux= em.find(PeriodoAcademico.class, periodoActivo.getPracId());//Busco periodo académico
					String extension = GeneralesUtilidades.obtenerExtension(item.getSltrmtDocumentoSolicitud());
					SolicitudTerceraMatricula sltrmtAux= new SolicitudTerceraMatricula(); //Creo el objeto
					  sltrmtAux.setSltrmtCausal(cslAux);  //seteo el objeto causal
					  if(rcesAux!=null){ //No existe rcesAux si la consulta de rces es del SAU
					  sltrmtAux.setSltrmtRecordEstudiante(rcesAux);  //seteo el objeto record
					  }
					  sltrmtAux.setSltrmtPeriodoAcademico(pracAux);   //seteo el periodo
     			      sltrmtAux.setSltrmtDocumentoSolicitud(SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_LABEL+"-"+periodoActivo.getPracId()+"-"+item.getMtrId()+"-"+usr.getUsrNick()+"."+extension); //seteo el nombre del documento solicitado
					  sltrmtAux.setSltrmtFechaSolicitud(new Timestamp(new Date().getTime())); //seteo la fecha de solicitud
					  sltrmtAux.setSltrmtEstado(estadoSltrmtSolicitud);  //seteo el campo estado de la solicitud
					  sltrmtAux.setSltrmtTipo(SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE); //seteo el tipo como solicitud
					  sltrmtAux.setSltrmtMtrId(item.getMtrId());
					  sltrmtAux.setSltrmtFcesId(item.getFcesId());
					  sltrmtAux.setSltrmtEstadoRegistro(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
				  	em.persist(sltrmtAux);

				}
			   session.getUserTransaction().commit();
			   retorno = true;
			   }
			}
			
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
			throw new SolicitudTerceraMatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Crea la entidad SolicitudTerceraMatricula y edita RecordEstudiante para el caso de apelaciones
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @param estadoSltrmtSolicitud  -  estado del la solicitud a guardarse en la tabla solicitudTerceraMatricula
	 * @param estadoRcesSolicitud - estado de SolicitudTerceraMatri en la tabla RecordEstudiante
	 * @param periodoActivo - en que se crea la solicitud.
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarApelacionTerceraMatricula(List<MateriaDto> listMateria,  int estadoSltrmtSolicitud, PeriodoAcademico periodoActivo , Usuario usr) throws Exception, SolicitudTerceraMatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//*********************** RECORD ESTUDIANTIL ********************************************
			//********************** DE LAS MATERIAS SELECCIONADAS **********************************
			//***************************************************************************************
		  
			Boolean cargadoDocumentosTodos= false;
			for (MateriaDto item : listMateria) {
				if (item.getSltrmtDocumentoSolicitud() != null) {
					cargadoDocumentosTodos = true;
				} else {
					cargadoDocumentosTodos = false;
					break;
				}
			}
			
			if(cargadoDocumentosTodos==true){
			if(listMateria != null){
				for (MateriaDto item : listMateria) {

    				RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
				
    				Causal cslAux= em.find(Causal.class, item.getCslId());//Busco el causal
					PeriodoAcademico pracAux= em.find(PeriodoAcademico.class, periodoActivo.getPracId());//Busco periodo académico
					SolicitudTerceraMatricula sltrmtNegada= em.find(SolicitudTerceraMatricula.class, item.getSltrmtId());
					String extension = GeneralesUtilidades.obtenerExtension(item.getSltrmtDocumentoSolicitud());
					
					SolicitudTerceraMatricula sltrmtAux= new SolicitudTerceraMatricula(); //Creo el objeto solicitud apelacion
					  sltrmtAux.setSltrmtCausal(cslAux);  //seteo el objeto causal
					if(rcesAux!=null){
					  sltrmtAux.setSltrmtRecordEstudiante(rcesAux);  //seteo el objeto record
					}
					  sltrmtAux.setSltrmtPeriodoAcademico(pracAux);   //seteo el periodo
					  sltrmtAux.setSltrmtSubId(sltrmtNegada);       //Seteo la solicitud negada por el director
					  sltrmtAux.setSltrmtDocumentoSolicitud(SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_LABEL+"-"+periodoActivo.getPracId()+"-"+item.getMtrId()+"-"+usr.getUsrNick()+"."+extension); //seteo el nombre del documento solicitado
					  sltrmtAux.setSltrmtFechaSolicitud(new Timestamp(new Date().getTime())); //seteo la fecha de solicitud
					  sltrmtAux.setSltrmtEstado(estadoSltrmtSolicitud);  //seteo el campo estado de la solicitud
					  sltrmtAux.setSltrmtTipo(SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_VALUE); //seteo el tipo como solicitud
					  sltrmtAux.setSltrmtMtrId(item.getMtrId());
					  sltrmtAux.setSltrmtFcesId(item.getFcesId());
					  sltrmtAux.setSltrmtEstadoRegistro(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
				  	em.persist(sltrmtAux);

				}
			   session.getUserTransaction().commit();
			   retorno = true;
			   }
			}
			
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
			throw new SolicitudTerceraMatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	
	/**
	 * edita  estadoTerceraMatri de RecordEstudiante y edita estado de SolicitudTerceraMatricula.
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @param estadoSltrmtSolicitud  -  estado del la solicitud a guardarse en la tabla solicitudTerceraMatricula
	 * @param estadoRcesSolicitud - estado de SolicitudTerceraMatri en la tabla RecordEstudiante
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarVerificacionSolicitudTerceraMatricula(List<SolicitudTerceraMatriculaDto> listMateria,  int estadoSltrmtSolicitud, String usuario) throws Exception, SolicitudTerceraMatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
	
			if(listMateria != null){
				for (SolicitudTerceraMatriculaDto item : listMateria) {
    			//	RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
				//	rcesAux.setRcesEstadoTerceraMatri(estadoRcesSolicitud); //Cambio el estado del campo estadoTerceramatricula de Rces
					SolicitudTerceraMatricula sltrmtAux= em.find(SolicitudTerceraMatricula.class, item.getSltrmtId());
					  sltrmtAux.setSltrmtFechaVerificacion(new Timestamp(new Date().getTime()));
					  sltrmtAux.setSltrmtEstado(estadoSltrmtSolicitud); //Cambio el estado de Solicitud Tercera Matricula.
					  sltrmtAux.setSltrmtObservacion(usuario);
				
				}
			}
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
			throw new SolicitudTerceraMatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**
	 * edita  estadoTerceraMatri de RecordEstudiante y edita estado de SolicitudTerceraMatricula.
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarAprobacionSolicitudTerceraMatricula(List<SolicitudTerceraMatriculaDto> listMateria, int fcinId, String usuario, String observacionFinal ) throws Exception, SolicitudTerceraMatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
		
			if(listMateria != null){
				for (SolicitudTerceraMatriculaDto item : listMateria) {

				      SolicitudTerceraMatricula sltrmtAux= em.find(SolicitudTerceraMatricula.class, item.getSltrmtId());
					  sltrmtAux.setSltrmtFechaRespSolicitud(new Timestamp(new Date().getTime())); //fecha en que se aprueba o niega la solicitud
					  String usuarioAnterior=sltrmtAux.getSltrmtObservacion();
					  sltrmtAux.setSltrmtObservacion("Resuelve solicitud: "+usuario+" "+" ,Verifica solicitud: "+usuarioAnterior);
					  sltrmtAux.setSltrmtObservacionFinal(observacionFinal);
				      sltrmtAux.setSltrmtEstado(item.getRespuestaSolicitud());
				}
				  
				
			}
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
			throw new SolicitudTerceraMatriculaException(e.getMessage());
		}
		return retorno;
	}

	
	/**
	 * edita  estadoTerceraMatri de RecordEstudiante y edita estado de SolicitudTerceraMatricula.
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarAprobacionApelacionTerceraMatricula(List<SolicitudTerceraMatriculaDto> listMateria, int fcinId,  String usuario, String observacionFinal ) throws Exception, SolicitudTerceraMatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			Boolean cargadoDocumentosTodos= false;
			for (SolicitudTerceraMatriculaDto item : listMateria) {
				if (item.getSltrmtDocumentoResolucion() != null) {
					cargadoDocumentosTodos = true;
				} else {
					cargadoDocumentosTodos = false;
					break;
				}
			}
			
			
			if(cargadoDocumentosTodos==true){
			if(listMateria != null){
				for (SolicitudTerceraMatriculaDto item : listMateria) {
			  				
					SolicitudTerceraMatricula sltrmtAux= em.find(SolicitudTerceraMatricula.class, item.getSltrmtId());
					  sltrmtAux.setSltrmtFechaRespSolicitud(new Timestamp(new Date().getTime())); //fecha en que se aprueba o niega la solicitud
					  String usuarioAnterior=sltrmtAux.getSltrmtObservacion();
					  sltrmtAux.setSltrmtObservacion("Resuelve solicitud: "+usuario+" "+" ,Verifica solicitud: "+usuarioAnterior);
					  sltrmtAux.setSltrmtObservacionFinal(observacionFinal);
					  sltrmtAux.setSltrmtEstado(item.getRespuestaSolicitud());
				
					//Guardar nombre del archivo de la resolucion					    					      
					   String extension = GeneralesUtilidades.obtenerExtension(item.getSltrmtDocumentoResolucion());
	     			   sltrmtAux.setSltrmtDocumentoResolucion(SolicitudTerceraMatriculaConstantes.RESOLUCION_APELACION_TERCERA_MATRICULA_LABEL+"-"+sltrmtAux.getSltrmtId()+"."+extension); //seteo el nombre del documento de resolucion
					  					  
				}
				
				
			}
			session.getUserTransaction().commit();
			retorno = true;
			
		}	
		
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
			throw new SolicitudTerceraMatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**
	 * Busca si existe al menos una  entidad Sltrmt por  id del materia y la id de fichaEstudiante
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param mtrId -id de la Materia a buscar
	 * @param fcesId -id de la FichaEstudiante a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idMateria y id Ficha estudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	@Override
	public SolicitudTerceraMatricula buscarSolicitudXMtrIdXFcesId(Integer mtrId, Integer fcesId) throws SolicitudTerceraMatriculaNoEncontradoException, SolicitudTerceraMatriculaValidacionException, SolicitudTerceraMatriculaException  {
		SolicitudTerceraMatricula retorno = null;
		
		if ((mtrId != null) && (fcesId!=null)) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select sltrmt from SolicitudTerceraMatricula sltrmt ");
				sbsql.append(" where sltrmt.sltrmtMtrId =:mtrId ");
				sbsql.append(" and sltrmt.sltrmtFcesId =:fcesId ");
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("mtrId", mtrId);
			    q.setParameter("fcesId", fcesId);
			    
				retorno = (SolicitudTerceraMatricula) q.getSingleResult();
			} catch (NoResultException e) {
				throw new SolicitudTerceraMatriculaNoEncontradoException(e.getMessage());
			} catch (NonUniqueResultException e) {
				throw new SolicitudTerceraMatriculaValidacionException(e.getMessage());
			} catch (Exception e) {
				throw new SolicitudTerceraMatriculaException(e.getMessage());
			} 
		}
		return retorno;
	}
	
	public SolicitudTerceraMatricula buscarSolicitudXMtrIdXFcesIdxEstado(Integer mtrId, Integer fcesId) throws SolicitudTerceraMatriculaNoEncontradoException, SolicitudTerceraMatriculaValidacionException, SolicitudTerceraMatriculaException  {
		SolicitudTerceraMatricula retorno = null;
		
		if ((mtrId != null) && (fcesId!=null)) { //verifico que el id del aula no sea nulo
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select sltrmt from SolicitudTerceraMatricula sltrmt ");
				sbsql.append(" where sltrmt.sltrmtMtrId =:mtrId ");
				sbsql.append(" and sltrmt.sltrmtFcesId =:fcesId ");
				sbsql.append(" and (sltrmt.sltrmtEstado = 4 ");sbsql.append(" or sltrmt.sltrmtEstado = 8) ");
				sbsql.append(" and sltrmt.sltrmtEstadoRegistro = ");sbsql.append(SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE);
		  		Query q = em.createQuery(sbsql.toString());
			    q.setParameter("mtrId", mtrId);
			    q.setParameter("fcesId", fcesId);
			    
				retorno = (SolicitudTerceraMatricula) q.getSingleResult();
			} catch (NoResultException e) {
				throw new SolicitudTerceraMatriculaNoEncontradoException("No se encontró solicitud aprobada de tercera matrícula en la materia solicitada.");
			} catch (NonUniqueResultException e) {
				throw new SolicitudTerceraMatriculaValidacionException("Se encontró varias solicitudes aprobadas de tercera matrícula en la materia solicitada." + fcesId);
			} catch (Exception e) {
				throw new SolicitudTerceraMatriculaException(e.getMessage());
			} 
		}
		return retorno;
	}
	
}
