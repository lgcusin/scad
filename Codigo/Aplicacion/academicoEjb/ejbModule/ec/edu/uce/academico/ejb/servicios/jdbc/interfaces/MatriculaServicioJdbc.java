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

 ARCHIVO:     MatriculaServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la Matricula del SAIU.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 23Feb2018			 Freddy Guzmán				          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MatriculaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;

/**
 * Interface MatriculaServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la Matricula del SAIU.
 * @author fgguzman
 * @version 1.0
 */
public interface MatriculaServicioJdbc {
	
	/**
	 * Método que permite buscar la cantidad de cupos y matriculados en una Materia y Paralelo X.
	 * @author fgguzman // -- v1
	 * @author Arturo Villafuerte - ajvilafuerte // -- v2
	 * @param mallaCurricularParaleloId - parametro con la malla curricular paralelo ID.
	 * @param paraleloId - parametro con el paralelo. 
	 * @return cantidad de cupos disponibles.
	 * @throws MateriaException
	 * @throws MateriaNoEncontradoException
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloException
	 */
	List<Integer> buscarCuposAndMatriculados(int mallaCurricularParaleloId, int paraleloId, int periodoId) throws ParaleloNoEncontradoException, ParaleloException;
	

	/**
	 * Busca las del sistema SAU 
	 * @author Arturo Villafuerte - ajvilafuerte
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carreraId - id de la carrera.
	 * @param periodoId - id del periodo.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	ComprobantePago buscarComprobantePago(String identificacion, int carreraid,  int periodoId) throws ParaleloNoEncontradoException, ParaleloException ;
	
	/**
	 * Método que permite listar todas las materias en la que el estudiante se encuentra matriculado o inscrito en un periodo activo actual.
	 * @author fgguzman 
	 * @param prsIdentificacion - cedula o pasaporte.
	 * @param estados - activo, inactivo, en cierre.
	 * @param pracTipos - Nivelacion, Pregrado, Postgrado, Suficiencias
	 * @return matricula con carreras y materias inscritas o matriculadas en periodos activos.
	 * @throws MatriculaNoEncontradoException
	 * @throws MatriculaException
	 */
	List<MatriculaDto> buscarMatriculasActivas(String prsIdentificacion, List<String> estados, List<String> pracTipos) throws MatriculaNoEncontradoException, MatriculaException;
	
	/**
	 * Método que permite listar todos los estudiantes de inscritos en un periodo academico.
	 * @author fgguzman 
	 * @param periodoId - id del periodo academico
	 * @return estudiantes que no pagaron su matricula y terminaron inscritos en el periodo ingresado.
	 * @throws MatriculaNoEncontradoException
	 * @throws MatriculaException
	 */
	List<MatriculaDto> buscarEstudiantesInscritos(int periodoId) throws MatriculaNoEncontradoException, MatriculaException;
	
	
	/**
	 * Método que permite recuperar el record academico del estudiante del SIIU.
	 * @author fgguzman 
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carrerasId - id de las carreras.
	 * @param pracTipo - tipo de periodo academico.
	 * @return record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarRecordEstudianteSIIU(String identificacion, List<String> carrerasId, int pracTipo) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite recuperar el record academico del estudiante del SIIU. 
	 * {Pregrado, Idiomas, Cultura Fisica, Informatica}
	 * {Estados: aprobado, reprobado, homologado}
	 * @author fgguzman 
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param estadosMateria - 0-> Activas , 1->Inactivas
	 * @return record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarRecordEstudianteSIIU(String identificacion, Integer[] estadosMateria) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite recuperar el record academico del estudiante del SIIU.
	 * @author fgguzman //--v1
	 * @author Arturo Villafuerte - ajvillafuerte //--v2
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carreraId - id de la carrera.
	 * @param periodoId - id del periodo.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarRecordAcademicoSIIU(String identificacion, int carreraId, int periodoInicioId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	
	/**
	 * Método que permite recuperar el record academico del estudiante del SAU.
	 * @author fgguzman // -- v1
	 * @author Arturo Villafuerte - ajvilafuerte // -- v2
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carreraId - id de la carrera.
	 * @param periodoId - id del periodo.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteSAUDto> buscarRecordAcademicoSAU(String identificacion, int espeCodigo,  int periodoId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Busca las del sistema SAU 
	 * @author Arturo Villafuerte - ajvilafuerte // -- v2
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param carreraId - id de la carrera.
	 * @param periodoId - id del periodo.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteSAUDto> buscarNotasSAU(String identificacion, int espeCodigo,  int periodoId)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite recuperar el record academico del estudiante del SAU, para el hisorial académico.
	 * {Carreras, Idiomas, Cultura Fisica}
	 * {Estados aprovado, reprobado, convalidado}
	 * @author fgguzman 
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param estadosMateria - 0-> Inactivas , 1->Activas
	 * @return record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarRecordAcademicoSAU(String identificacion, Integer[] estadosMateria) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite recuperar el record academico de la Suficiencia en Cultura Fisica del estudiante del SAU.
	 * @author fgguzman 
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @return lista record academico.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarRecordAcademicoSuficienciaCulturaFisicaSAU(String identificacion)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException ;

	/**
	 * Método que permite buscar si el estudiante se matriculo o no en un periodo academico.
	 * @author fgguzman
	 * @param prsIdentificacion - cedula o pasaporte del estudiante
	 * @param fcinId - id de la Ficha Inscripcion
	 * @param periodoId - id del periodo
	 * @return matricula del estudiante
	 * @throws MatriculaNoEncontradoException
	 * @throws MatriculaException
	 */
	List<MatriculaDto> buscarEstudianteMatriculado(String prsIdentificacion, int fcinId, int periodoId) throws MatriculaNoEncontradoException, MatriculaException;
	
	/**
	 * Método que permite buscar estudiantes de pregrado con ficha inscripcion activa.
	 * @return estudiantes inscritos en pregrado
	 * @throws MatriculaNoEncontradoException
	 * @throws MatriculaException
	 */
	List<MatriculaDto> buscarEstudiantesPregrado() throws MatriculaNoEncontradoException, MatriculaException;
	
	/**
	 * Método que permite listar los estudiantes matriculados en un paralelo. Exoneraciones Informatica -> hasta refactorizar.
	 * @param paraleloId - id del paralelo
	 * @return estudiantes matriculados en el paralelo
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarEstudiantesMatriculados(int paraleloId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException; 
	
	
	/**
	 * 22 mar 2019
	 * Método que permite buscar El record del estudiante en el SAU, incluyendo materia anuladas la matricula, para utilizar en movilidad estudiantil.
	 * @author lmquishpei
	 * @param identificacion - identificación del estudiante. 
	 * @param espeCodigo - codigo de carrera. 
	 * @param periodoId - hasta el periodo . 
	 * @return record de estudiante en SAU.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	public List<RecordEstudianteSAUDto> buscarRecordAcademicoSAUconAnulados(String identificacion, int espeCodigo,  int periodoId)	throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
}

