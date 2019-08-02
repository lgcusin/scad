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

 ARCHIVO:     MateriaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Materia.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 16-03-2017			David Arellano				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;


/**
 * Interface MateriaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Materia.
 * @author darellano
 * @version 1.0
 */
public interface MateriaDtoServicioJdbc {
	
	/**
	 * Método que permite buscar una materia modular por periodo academico.
	 * @author fgguzman
	 * @param modularId
	 * @param paraleloId
	 */
	MateriaDto buscarModularPorPeriodo(int modularId, int paraleloId) throws MateriaException, MateriaValidacionException, MateriaNoEncontradoException ;

	/**
	 * Realiza la busqueda de todas materias de una malla especifica de un nivel
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @param nivelId - id del nivel de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmallaXnivel(int mallaId, int nivelId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

	/**
	 * Método que realiza la busqueda por nivel y por carrera para listar las materias
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXnivelXcarrera(int nivelId, int carreraId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

	/**
	 * Método que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXperiodoXcarreraXnivelXparaleloXdocente(int periodoId, int carreraId, int nivelId, int paraleloId, int docenteId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	
	/**
	 * Método que realiza la busqueda por periodo academico activo por carrera por nivel y por docente para listar las materias
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXperiodoActivoXcarreraXnivelXparaleloXdocente(int carreraId, int nivelId,  int docenteId) throws MateriaDtoException, MateriaDtoNoEncontradoException;


	/**
	 * Método que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoXcarrera(int personaId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	
	/**MQ
	 * Método que realiza la busqueda de materias ficha_matricula y por estudiante, para consulta de notas de posgrado-SIN NUMERO MATRICULA
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXestudianteXmatriculaXperiodoXcarreraXPosgrado(int personaId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	
    /**
	 * Realiza la busqueda de las sub materias de una materia padre
	 * @return Lista las sub materias de una materia padre
	 * @param materiaId - id de la materia padre
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateriaPadre(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Método que realiza la busqueda por descripcion y por carrera para listar las materias
	 * @param descripcion - descripcion nombre de la materia a buscar
	 * @param idCarrera - idCarrera id de la carrera seleccionada para la busqueda
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXDescripcionXcarrera(String descripcion, int idCarrera) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas materias.
	 * @return Lista todas materias de de una facultad, carrera .
	 * @param dependenciaId - id de la dependencia de las materias a buscar.
	 * @param carreraId - id de la carrera de las materias a buscar.
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada.
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados.
	 */
	
	public List<MateriaDto> listarMateriasPdpn_IdPcrr_Id(int  dependenciaId , int carreraId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

	/**
	 * Método que realiza la busqueda por Malla, Nivel, Descripción y tipo de busqueda(Prerrequisitos, Correquisitos)
	 * @param idMalla -idMalla a buscar
	 * @param idCarrera - idNivel para la busqueda
	 * @param descripcion - descripcion para la busqueda
	 * * @param tipoBusqueda - tipoBusqueda para la busqueda (Prerrequisitos, Correquisitos)
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasPIdMlcr_PIdNvl_PDescripcion(int IdMlcr, int IdNvl, String descripcion, int tipoBusqueda) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de la materias por Id Materia en mallas curriculares
	 * @return TRUE- Si existe la materia en una malla curricular, FALSE- sino existe
	 * @param materiaId - id de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean  buscarMateriaDtoPMlcrmt(int materiaId) throws MateriaDtoException;
	
	/**
	 * Método que realiza la busqueda por periodo academico por carrera por nivel para listar las materias
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlId(int pracId, int crrId, int nvlId, int prlId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Método que realiza la busqueda por periodo academico por carrera por nivel para listar las materias con modulares
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlIdModular(int pracId, int crrId, int nvlId, int prlId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Método que realiza la busqueda por periodo academico por carrera por nivel para listar las materias sin la materia a consultar
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @param mlcrprId - mlcrprId id del de la materia a ser excluida
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlIdSinMateria(int pracId, int crrId, int nvlId, int prlId, MateriaDto materiaDto) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel para listar las materias sin la materia a consultar
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionada para la busqueda
	 * @param nvlId - nvlId id del nivel seleccionado para la busqueda
	 * @param prlId - prlId id del paralelo seleccionado para la busqueda
	 * @param mlcrprId - mlcrprId id del de la materia a ser excluida
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarPpracIdPcrrIdPnvlIdPprlIdSinMateriaSinControlNumHorasCreditos(int pracId, int crrId, int nvlId, int prlId, MateriaDto materiaDto) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Método que realiza la busqueda de materias ficha_matricula y por estudiante.
	 * @param personaId - personaId id del persona del estudiante
	 * @param matriculaId - matriculaId id de la matricula seleccionado.	 
	 * @return - retorna la lista de materias por los parámetros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarNotasEstudianteNivelacion(String identificacionPrs) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas materias de una malla especifica 
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmalla(int mallaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**MQ:  28/03/2019
	 * Realiza la busqueda de todas materias de una malla especifica sin presentar materias hijas(modulos) para homologaciones
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXmallaSinAsignaturasModulo(int mallaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de todas materias de una carrera
	 * @return Lista todas materias de esa carrera
	 * @param crrId - id de la carrera a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	
	
	public List<MateriaDto> listarXmallaXnivelXpreXcoreq(Integer crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas materias de una carrera
	 * @return Lista todas materias de esa carrera
	 * @param crrId - id de la carrera a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasXpreXcoreq(Integer materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracActivoXprcacEnCierre(String personaIdentificacion, int cncrID) throws MateriaDtoException;
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracActivoXprcac(String personaIdentificacion, int cncrID) throws MateriaDtoException;
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public MateriaDto buscarRecordEstudiante(String personaIdentificacion) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	public List<MateriaDto> listarMateriasMallaHastaNivel(int mallaId, int numeralId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas materias de una malla especifica de un nivel
	 * @return Lista todas materias de esa malla
	 * @param mallaId - id de la malla a buscar
	 * @param nivelId - id del nivel de la materia a buscar
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarMateriasxCarrera(int crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracHomologacion(String personaIdentificacion, int cncrID) throws MateriaDtoException;
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionprcacEnCierre(String personaIdentificacion) throws MateriaDtoException;
	
	/**
	 * Realiza la busqueda del record academico por la identificacion 
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudianteXidentificacionXcarrera(String personaIdentificacion, Integer crrId) throws MateriaDtoException;
	
	
	
	/**MQ
	 * Realiza la busqueda del materias por Estado del record, numero de matricula, estadoMateria, Carrera
	 * @param prsId - prsId del estudiante a buscar
	 * @param dtmtNumMatriculaMateria - dtmtNumMatriculaMateria del estudiante a buscar
	 * @param rcesEstado - fcesId del estudiante a buscar
	 * @param crrId - crrId del estudiante a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> ListarMateriasXPrsIdXNumeroMatriculaXEstadoMateriaXCarrera(Integer prsId, Integer dtmtNumMatriculaMateria, int rcesEstado, int crrId) throws MateriaDtoException;

	

	/**MQ
	 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado, carrera  fichaInscripcion activa  en el periodo HOMOLOGACION
	 * @param prsId - prsId del estudiante a buscar
	 * @param dtmtNumMatriculaMateria - dtmtNumMatriculaMateria del estudiante a buscar
	 * @param rcesEstado - fcesId del estudiante a buscar
	 * @param crrId - crrId del estudiante a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
		public List<MateriaDto> ListarMateriasXPrsIdXNumeroMatriculaXEstadoMateriaXCarreraxPeriodoHomologacion(Integer prsId, Integer dtmtNumMatriculaMateria, Integer rcesEstado, Integer crrId) throws MateriaDtoException;
	
	
	
	/**MQ
	 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado y carrera
	 * @param prsId - fcesId del estudiante a buscar
	 * @param crrId - fcesId del estudiante a buscar
	 * @param rcesEstado - fcesId del estudiante a buscar
	 * @return Lista de MateriaDto que corresponde a la persona buscada
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
		public List<MateriaDto> ListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitud(Integer prsId, Integer crrId, int sltrmtEstadoSolicitud) throws MateriaDtoException;
	
		/**
		 * Realiza la busqueda del record academico por la identificacion 
		 * @author Arturo Villafuerte - ajvillafuerte
		 * @param personaIdentificacion - String de la indentificación de la persona a buscar
		 * @return Lista de MateriaDto que corresponde a la persona buscada
		 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 */
		public List<MateriaDto> buscarRecordEstudianteXidentificacionXpracActivoXprcacFull(String personaIdentificacion, int cncrID, int periodoInicioId) throws MateriaDtoException;
			
		/**
		 * Realiza la búsqueda de las materias de una malla curricular según carrera y estados de la materia.  
		 * @author ajvillafuerte
		 * @author fgguzman
		 * @return materias
		 * @param crrId - id de la carrera
		 * @param mtrEstados - estado de la materia
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
		 */
		public List<MateriaDto> listarMateriasxCarreraFull(int crrId, Integer[] mtrEstados) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
		/**MQ
		 * Realiza la busqueda del materias por Id de persona, numero de matricula,  con segunda matricula, estado y carrera
		 * @param prsId - fcesId del estudiante a buscar
		 * @param crrId - fcesId del estudiante a buscar
		 * @param rcesEstado - fcesId del estudiante a buscar
		 * @return Lista de MateriaDto que corresponde a la persona buscada
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 */
		public List<MateriaDto> ListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitudSIIUSAU(Integer prsId, Integer crrId, int sltrmtEstadoSolicitud) throws MateriaDtoException;

		/**
		 * Realiza la busqueda del record academico por la identificacion 
		 * @author Arturo Villafuerte - ajvillafuerte
		 * @param personaIdentificacion - String de la indentificación de la persona a buscar
		 * @return Lista de MateriaDto que corresponde a la persona buscada
		 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 */
		public List<MateriaDto> buscarRecordEstudianteAModificarXidentificacionXpracActivoXprcacFull(String personaIdentificacion, int periodoId, int carreaId) throws MateriaDtoException;
		
		public List<MateriaDto> listarMateriasActivasPosgrado(int carreraId, int nivelId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

		public List<MateriaDto> listarXestudianteXmatriculaXperiodoXcarreraFull(int personaId, int matriculaId, int periodoId,	int carreraId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		public List<MateriaDto> listarXmallaCurricularParalelo(Integer mlcrprId, Integer prsId, Integer periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

		public List<EstudianteJdbcDto> listarNotasXmallaCurricularParalelo(Integer mlcrprId, Integer prsId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		/**
		 * Realiza la búsqueda del record académico. 
		 * @author fgguzman
		 * @param prsIdentificacion - cédula o pasaporte
		 * @param rcesEstado - {-1-Inscrito, 0-Matriculado, 6-Retiro Solicitado}
		 * @param pracTipo - {0-Pregrado,1-Posgrado,2-Idiomas,3-Suf_Cult_Fisica,4-Suf_Intensivo_Cult_Fisica}
		 * @param pracEstado - {0-Activo,1-Inactivo,2-En cierre}
		 * @return MateriasDto de las carreras que se encuentre inscrito o matriculado.
		 * @throws MateriaDtoNoEncontradoException - Lanzada cuando no se encuentra ningun registro.
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada.
		 */
		List<MateriaDto> buscarRecordEstudiante(String prsIdentificacion,  List<String> rcesEstado, int pracTipo, int pracEstado) throws MateriaDtoException, MateriaDtoNoEncontradoException;

		/**
		 * Realiza la busqueda del record academico por identificacion y carrera 
		 * @author fgguzman
		 * @param identificacion - cedula o pasaporte del estudiante
		 * @param crrId - id de la carrera
		 * @return materias encontradas.
		 * @throws MateriaDtoNoEncontradoException - Lanzada cuando no se encuentra ningun registro.
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada.
		 */
		List<MateriaDto> buscarRecordEstudiante(String identificacion, int crrId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		/**
		 * Método que permite recuperar las materias vinculadas a una carrera y nivel especifico.
		 * @author fgguzman
		 * @param carreraId - id de la Carrera
		 * @param nivelId - id del nivel
		 * @return materias asociadas.
		 * @throws MallaCurricularDtoException
		 * @throws MallaCurricularDtoNoEncontradoException
		 */
		List<MateriaDto> buscarMateriasPorCarreraNivel(int carreraId, int nivelId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;

		/**
		 * Método que devuelve las materias que se encuentran vinculadas a un paralelo.
		 * @author fgguzman
		 * @param paraleloId - id del paralelo
		 * @return materias
		 * @throws ParaleloDtoException
		 * @throws ParaleloDtoNoEncontradoException
		 */
		List<MateriaDto> buscarMateriasPorParalelo(int paraleloId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
		
		
		public List<MateriaDto> buscarEstudianteMatriculadoXPeriodoXDependencia(int idPeriodo, int idDependencia, int tipoCarrera, int idCarrera) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		public PersonaDto buscarDocentesAsignadosCHyCompartidosXPeriodoXDependencia(int idPeriodo, int mlcrprId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		/**
		 * Realiza la busqueda de todas materias de una malla especifica de un nivel
		 * @author jdalbuja
		 * @return Lista todas materias de esa malla
		 * @param mallaId - id de la malla a buscar
		 * @param nivelId - id del nivel de la materia a buscar
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
		 */
		
		public List<MateriaDto> listarMateriasxCarreraPracEnCierre(int crrId)
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		public List<MateriaDto> listarMateriasNotasXPracActivoXCarreraXIdentificacion(String cedulaEstudiante,
				List<CarreraDto> carrerasDirector, int crrId, int mtrId)
				throws MateriaDtoException, MateriaDtoNoEncontradoException;


		public List<MateriaDto> listarMateriasNotasXPracEnCierreXCarreraXIdentificacion(String cedulaEstudiante,
				List<CarreraDto> carrerasDirector, int crrId, int mtrId)
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		public List<MateriaDto> listarEstudiantesParaRecalificacion(String cedulaEstudiante, List<CarreraDto> carrerasDirector,
				int crrId, int mtrId) throws MateriaDtoException, MateriaDtoNoEncontradoException;


		List<MateriaDto> listadoEstudiantesXmatriculaXperiodoEnCierreXcarreraXMateriaModular()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesRegistrosDuplicadosModulares()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesRegistrosEditadosModulares()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesSinNotaFinal() throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesSinNota1SinNota2()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesProblemasRecuperacion()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesRecalculoRecuperacion()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		List<MateriaDto> listarXrecordXmatriculaXperiodo (int recordId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		/**
		 * MQ:
		 * Metodo que realiza la busqueda de materias homologadas con notas por  personaId ficha_matricula y periodo
		 * @param personaId - personaId id del persona del estudiante
		 * @param matriculaId - matriculaId id de la matricula seleccionado.
		 * @param periodoId - periodoId id de la matricula seleccionado.	 
		 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
		 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
		 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
		 */
		public List<MateriaDto> listarHomologacionesXestudianteXmatriculaXperiodoXcarrera(int personaId, int matriculaId, int periodoId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
		
		/**
		 * Método que permite recuperar las materias con su docente segun paralelo ingresado.
		 * @author fgguzman
		 * @param paraleloId - id del paralelo.
		 * @return materias con su docente.
		 * @throws ParaleloDtoException
		 * @throws ParaleloDtoNoEncontradoException
		 */
		List<MateriaDto> buscarMateriasDocentePorParalelo(int paraleloId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
		
		/**
		 * Método que permite buscar parametros para flujo horario academico.
		 * @author fgguzman
		 * @param paraleloId - id del paralelo
		 * @param materiaId - id de la materia
		 * @return materia solicitada
		 * @throws ParaleloDtoValidacionException
		 * @throws ParaleloDtoException
		 * @throws ParaleloDtoNoEncontradoException
		 */
		MateriaDto buscarMateriaPorParaleloMateria(int paraleloId, int materiaId) throws ParaleloDtoValidacionException, ParaleloDtoException, ParaleloDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesRegistrosDuplicados()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoCorrecionEstado() throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<EstudianteJdbcDto> listarCalificacionesEstudianteNivelacion(String identificacionPrs)
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listadoEstudiantesXmatriculaXperiodoAnteriorXcarreraXMateriaModular()
				throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listarXestudianteXmatriculaXperiodoEIdXcarreraXMateriaModular(String personaCedula,
				Integer rcesId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

		void eliminarDuplicados(int clfId) throws MateriaDtoException, MateriaDtoNoEncontradoException;

		List<MateriaDto> listarXestudianteXmatriculaXperiodoEnCierreXcarreraXMateriaModular(String personaCedula,
				Integer rcesId, Integer pracId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
}
