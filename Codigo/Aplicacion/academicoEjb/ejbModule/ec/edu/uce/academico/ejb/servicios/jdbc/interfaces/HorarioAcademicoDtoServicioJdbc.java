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

 ARCHIVO:     HorarioAcademicoDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla HorarioAcademicoDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 21-SEPT-2017		Dennis Collaguazo 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;


/**
 * Interface HorarioAcademicoDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla HorarioAcademicoDto.
 * @author dcollaguazo
 * @version 1.0
 */
public interface HorarioAcademicoDtoServicioJdbc {
	
	/**
	 * Método que busca el horario academico del estudiante según carrera y periodo academico.
	 * @author fgguzman - FREDDY 
	 * @param crrId - id de la Carrera.
	 * @param pracId - id del Periodo Academico.
	 * @param identificacion - cedula o pasaporte.
	 * @return horario de clases por carrera.
	 * @throws HorarioAcademicoDtoException
	 * @throws HorarioAcademicoDtoNoEncontradoException
	 */
	List<HorarioAcademicoDto> buscarHorarioModulo(int paraleloId, int mlcrmtId, int periodoId)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;

	
	/**
	 * Método que busca el horario academico del estudiante según carrera y periodo academico.
	 * @author fgguzman - FREDDY 
	 * @param crrId - id de la Carrera.
	 * @param pracId - id del Periodo Academico.
	 * @param identificacion - cedula o pasaporte.
	 * @return horario de clases por carrera.
	 * @throws HorarioAcademicoDtoException
	 * @throws HorarioAcademicoDtoNoEncontradoException
	 */
	List<HorarioAcademicoDto> buscarHorarioEstudiante(int crrId, int pracId, String identificacion)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;

	/**
	 * Método que busca el horario academico del estudiante según carrera y periodo academico.
	 * @author fgguzman - FREDDY 
	 * @param crrId - id de la Carrera.
	 * @param pracId - id del Periodo Academico.
	 * @param identificacion - cedula o pasaporte.
	 * @return horario de clases por carrera.
	 * @throws HorarioAcademicoDtoException
	 * @throws HorarioAcademicoDtoNoEncontradoException
	 */
	List<HorarioAcademicoDto> buscarHorarioDocente(String identificacion)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;


	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioPpracIdPprlIdPmtrId(int pracId, int prlId, int mtrId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXprlId(int pracId, int prlId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;

	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXListMlcrpr(int pracId, List<EstudianteJdbcDto> listaMaterias) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXListMlcrprComp(int pracId, List<DocenteJdbcDto> listaMaterias) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;
	
		
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXcrrId(int pracId, int crrId, List<CarreraDto> listCarreras, List<MallaCurricularParaleloDto> listaMallaCurricularParalelo) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXpracIdXprlIdXmlcrprId(int pracId, int mlcrprId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;
		
	/**
	 * Realiza la busqueda de todas los horarios academicos
	 * @param pracId - pracId id de la malla curricular 
	 * @param dtpsId - dtpsId id del detalle puesto del docente
	 * @return Lista todas las mallas curricularers materia por periodo, detalle puesto
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioXPracIdXDtpsId(int pracId, int dtpsId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;

	/**
	 * Realiza la busqueda de todas los horarios academicos buscados por paralelo Id
	 * @param prlId - prlId , estado de la malla curricular
	 * @return Lista de paralelos encontrados
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	
	public List<HorarioAcademicoDto> listarHorarioPprlIdPMtrId( int prlId, int mtrId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas los horarios academicos buscados
	 * @param pracId - pracId id de la malla curricular 
	 * @param prlId - prlId , estado de la malla curricular
	 * @param mtrId - mtrId estado de la malla curricular materia
	 * @param hracDia - hracDia dia en el que hay que consultar
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws HorarioAcademicoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HorarioAcademicoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<HorarioAcademicoDto> listarHorarioPpracIdPprlIdPmtrIdPhracDia(int pracId, int prlId, int mtrId, int hracDia) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;
	

	/**
	 * Listar horarios por parametros de busqueda
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param prlId .- Paralelo id a buscar
	 * @param mlcrmtId .- MallaCurricularMateria Id a buscar
	 * @param pracId .- PeriodoAcademico id a buscar  
	 * @return Lista de horarios
	 * @throws HorarioAcademicoDtoException
	 * @throws HorarioAcademicoDtoNoEncontradoException
	 */
	public List<HorarioAcademicoDto> buscarHorarioFull(int prlId, int mlcrmtId, int pracId)throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException;
	
	/**
	 * Método que permite buscar si un aula-docente han sido compartidos.
	 * @author fgguzman 
	 * @param mlcrprId - id a verificar si es compartido
	 * @return horarios compartidos
	 * @throws HorarioAcademicoDtoException
	 * @throws HorarioAcademicoDtoNoEncontradoException
	 */
	List<HorarioAcademicoDto> buscarParaleloCompartido(int mlcrprId) throws HorarioAcademicoDtoException, HorarioAcademicoDtoNoEncontradoException ;

}
