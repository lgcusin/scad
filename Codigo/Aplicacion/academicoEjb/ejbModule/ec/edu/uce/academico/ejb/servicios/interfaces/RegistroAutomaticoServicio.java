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

 ARCHIVO:     RegistroAutomaticoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el proceso de registro automatico.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 19-05-2017          David Arellano                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.RegistroDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;

/**
 * Interface RegistroAutomaticoServicio
 * Interfase que define las operaciones sobre el proceso de registro automatico.
 * @author darellano
 * @version 1.0
 */
public interface RegistroAutomaticoServicio {
	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la matricula de un estudiante 
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	public void generarRegistro(List<RegistroDto> listRegistro) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;

	void generarRegistroPosgrado(List<RegistroDto> listRegistroDto, Integer pracId)
			throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;
	
	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la ficha de inscripcion de un estudiante a homologar
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	public String generarRegistroHomologacion(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;

	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la ficha de inscripcion de un estudiante nuevo a carrera
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	
	public String generarRegistroPregrado(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;
	
	/**
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion
	 * para generar la ficha de inscripcion de un estudiante nuevo a idiomas de a acuerdo a la nota del examen de ubicación
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	
	public String generarRegistroUbicacionIdiomas(List<RegistroDto> listRegistroDto, PlanificacionCronograma planificacionCronograma) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;
	
	
	/**MQ:
	 * Modifica las entidades : usuario, persona, usuario_rol,ficha_inscripcion  POSGRADO
	 * para generar la ficha de inscripcion de un estudiante a homologar desde cero  o crea solo ficha inscripcion si es cambio de carrera.
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	
	public String generarRegistroHomologacionPosgrado(List<RegistroDto> listRegistroDto) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;
	
	/**MQ
	 * Crea usuario, persona, usuario_rol,ficha_inscripcion si el estudiante no existe en el SIIU,
	 * crea solo ficha inscripcion como reingreso si el estudiante ya existe en el SIIU.
	 * @param listRegistro - lista de RegistroDto para insertar en las tablas de la BD
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */

	public String generarRegistroManual(RegistroHomologacionDto itemRegistro, Integer tipoIngresoNuevaFcin) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;
	
	/**
	 * Elimina ficha inscripcion sino tiene ficha estudiante o es un reingreso o reinicio se vuelve a colocar la ficha inscripcion anterior en la ficha Estudiante
	 * @param registroHomologacion - registro con la ficha inscripcion a eliminar.
	 * @param fcinAnterior - id de la ficha inscripcion anterior
	 * @param fichaEstudianteExistente - ficha estudiante existente.
	 * @throws RegistroAutomaticoValidacionException - Excepcion lanzada cuando no se encuentra una entidad para el registro 
	 * @throws RegistroAutomaticoException - Excepcion general
	 */
	public String eliminarFichaInscripcionHomologación(RegistroHomologacionDto registroHomologacion, FichaEstudiante fichaEstudianteExistente) throws RegistroAutomaticoValidacionException, RegistroAutomaticoException;
	
}
