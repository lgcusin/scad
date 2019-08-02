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

 ARCHIVO:     ReportesConsultorServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de los reportes del consultor.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 04-06-2018		  Daniel Ortiz				       Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoNoEncontradoException;
/**
 * Interface ReportesConsultorServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de los reportes del consultor.
 * @author dortiz
 * @version 1.0
 */
public interface ReportesConsultorServicioJdbc {
	/**
	 * Lista los estudiantes matriculados por Facultad, Carrera y Periodo Academico.
	 * @return Lista de los estudiantes matriculados.
	 * @throws PersonaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PersonaDto> listarEstudiantesMatriculadosXFacultadXCarreraXPeriodoAcademico(int dpn,int crr,int prac) throws PersonaDtoException,PersonaDtoNoEncontradoException;
	/**
	 * Lista los estudiantes con solicitudes de terceras matriculas por Carrera y Periodo Academico.
	 * @return Lista de los estudiantes con terceras matriculas.
	 * @throws PersonaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarEstudiantesTercerasMatriculasXCarreraXPeriodoAcademico(int crr,int prac) throws SolicitudTerceraMatriculaDtoException,SolicitudTerceraMatriculaDtoNoEncontradoException;
	/**
	 * Lista las notas de los estudiantes por Carrera y Periodo Academico.
	 * @return Lista de las notas de los estudiantes.
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RecordEstudianteDto> listarNotasEstudiantesXCarreraXPeriodoAcademico(int crr,int prac) throws RecordEstudianteDtoException,RecordEstudianteDtoNoEncontradoException;
	/**
	 * Genera el record academico con las notas del estudiante por su Carrera y Cedula.
	 * @return Lista de las notas de los estudiantes.
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RecordEstudianteDto> generarRecordAcademicoXCarreraXCedula(int crr,String cedula) throws RecordEstudianteDtoException,RecordEstudianteDtoNoEncontradoException;

}
