/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     HorarioAcademicoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla HorarioAcademico.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 18-SEP-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;

/**
 * Interface HorarioAcademicoServicio
 * Interfase que define las operaciones sobre la tabla Edificio.
 * @author lmquishpei
 * @version 1.0
 */
public interface HorarioAcademicoServicio {

	/**
	 * Lista todas los HorariosAcademicos existentes en la BD  con idHoraClaseAula
	 * @return lista de todas los HorariosAcademicos existentes en la BD con idHoraClaseAula
	 */
	public List <HorarioAcademico> listarHorarioAcademicoXhoraClaseAulaId(int horaClaseAulaId) throws HorarioAcademicoNoEncontradoException , HorarioAcademicoException;

	/**
	 * Método que sirve para editar el horario academico
	 * @param entidad - entidad, entidad horario academico ha ser editada
	 * @return, retorna verdadero si se ejecuto la edición
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean editar(HorarioAcademico entidad) throws HorarioAcademicoException, Exception;

	/**
	 * Método que sirve para eliminar un horario academico
	 * @param hracId - hracId, id del horario academico a eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int hracId) throws HorarioAcademicoException, HorarioAcademicoValidacionException, HorarioAcademicoNoEncontradoException;

	/**
	 * Método que sirve para agregar un horario academico
	 * @param entidad - entidad, entidad horario academico
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean nuevo(HorarioAcademico entidad) throws HorarioAcademicoException, Exception;
	
	/**
	 * Método que sirve para compartir un horario academico
	 * @param entidadAsignar - entidadAsignar, entidad horario academico a ser asignada
	 * @param listHorarioComp - listHorarioComp, lista de horarios academicos a compartir
	 * @param docente - docente, información docente a compartir
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws HorarioAcademicoException - HorarioAcademicoException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean compartir(HorarioAcademicoDto entidadAsignar, List<HorarioAcademicoDto> listHorarioComp , DocenteJdbcDto docente) throws HorarioAcademicoException, Exception;
	

	/**
	 * Método que permite guardar en la tabla Horario Académico.
	 * @author fgguzman
	 * @param entidad - HorarioAcademico
	 * @return HorarioAcademico
	 * @throws HorarioAcademicoException
	 * @throws HorarioAcademicoValidacionException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	HorarioAcademico guardar(HorarioAcademico entidad) throws HorarioAcademicoException,HorarioAcademicoValidacionException;
	
	/**
	 * Método que permite compratir el Horario de Clases de una Asignatura del Paralelo A al Paralelo B de la misma Carrera o diferentes.
	 * @author fgguzman
	 * @param mlcrprIdPrincipal - id de la MlCrPrId de la Asignatura Principal
	 * @param mlcrprIdCompartida - id de la MlCrPrId de la Asignatura a la que se va a Compartir el Horario de Clases.
	 * @return true si todo fue exito.
	 * @throws HorarioAcademicoNoEncontradoException
	 * @throws HorarioAcademicoException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	boolean compartir(int mlcrprIdPrincipal, int mlcrprIdCompartida) throws CargaHorariaNoEncontradoException, CargaHorariaValidacionException, CargaHorariaException, HorarioAcademicoNoEncontradoException, HorarioAcademicoException;
}
