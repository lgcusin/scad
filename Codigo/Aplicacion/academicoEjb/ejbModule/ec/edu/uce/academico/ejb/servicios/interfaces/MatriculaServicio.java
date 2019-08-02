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

 ARCHIVO:     MatriculaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el proceso de matriculación.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 27-03-2017          David Arellano                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteMatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

/**
 * Interface MatriculaServicio
 * Interfase que define las operaciones sobre el proceso de matriculación.
 * @author darellano
 * @version 1.0
 */
public interface MatriculaServicio {
	
	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaId - id de la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param nivelUbicacion - nivel de ubicacion en el que cae el matriculado
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param costoMatricula - valor de la matricula
	 * @param mallaCurricular - entidad malla curricular en la que se le asigna para la matricula 
	 * @param estudianteNuevo - booleano que permite determinar si es un estudiante nuevo o no
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 * @throws EstudianteMatriculaValidacionException 
	 */
	public String generarMatricula(List<MateriaDto> listMaterias, int personaId, FichaInscripcionDto fichaInscripcion, int nivelUbicacion
									, int tipoMatricula, int tipoGratuidadId, BigDecimal costoMatricula, MallaCurricular mallaCurricular, Boolean estudianteNuevo
									, PlanificacionCronograma planificacionCronograma, PeriodoAcademico pracId) throws MatriculaValidacionException, MatriculaException, EstudianteMatriculaValidacionException;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarMatriculaPosgrado(List<MateriaDto> listMaterias, int personaId, FichaInscripcionDto fichaInscripcion, int nivelUbicacion
									, int tipoMatricula, int tipoGratuidadId, BigDecimal valorMatricula, MallaCurricular mallaCurricular, Boolean estudianteNuevo
									, PlanificacionCronograma planificacionCronograma) throws MatriculaValidacionException, MatriculaException;
	
	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,calificacion
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaDto - la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void generarMatriculaHistorico(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
			,Integer valorMatricula , PlanificacionCronograma planificacionCronograma) throws MatriculaValidacionException, MatriculaException;

		
	/**
	 * Método que sirve para generar la solicitud de retiro de matrícula por parte del estudiante
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha solicitar
	 * @param estadoDtmt - estadoDtmt estado del detalle de matricula para poder habilitar la solicitud
	 * @param archivoRetiro - archivoRetiro, nombre del path y del archivo de solicitud de retiro
	 * @param estadoSolicitadoDtmt - estadoSolicitadoDtmt, estado del detalle de matricula, para solicitud de retiro
	 * @param estadoSolicitadoRces - estadoSolicitadoRces, estado del record estudiante, para inactivar el record de la materia solicitada
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException excepción general de matricula
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarSolicitudRetiroMatricula(List<EstudianteJdbcDto> listDetalleMatricula, int estadoDtmt, String archivoRetiro, int estadoSolicitadoDtmt, int estadoSolicitadoRces) throws Exception;

	/**
	 * Método que sirve para aprobar la solicitud de retiro de matriculas por parte de los estudiantes
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha retirarse
	 * @param estadoRetiroTotal - estadoRetiroTotal estado del retiro si va ha ser en todas la materias
	 * @param estadoDtmt - estadoDtmt estado del detalle de matricula para poder habilitar la aprobación
	 * @param dtmtEstadoCambio - dtmtEstadoCambio, estado del detalle de matrícula en el estado cambio, maneja las solicitues y aprobaciones
	 * @param estadoRecord - estadoRecord, estado del recor estudiante para verificar que estan en respectivo estado para aprobaccion
	 * @param idFichaMatricula - idFichaMatricula, id de la ficha matrícula, si es caso es retirar toda la matrícula
	 * @param estadoFichaMatricula - estadoFichaMatricula, estado de la ficha matricula, si el caso es retirar en todo
	 * @param usuario - usuario, datos del usuario que aprueba la solicitud de matrícula
	 * @return, retorna verdadero si se ejecuto la transacción caso contrario false
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean aprobarSolicitudRetiroMatricula(List<EstudianteJdbcDto> listDetalleMatricula, boolean estadoRetiroTotal, int estadoDtmt, int dtmtEstadoCambio, int estadoRecord, int idFichaMatricula, int estadoFichaMatricula, Usuario usuario) throws MatriculaException;
	
	
	public void generarMatriculaPregrado(List<MateriaDto> listMaterias, FichaInscripcionDto fcin,Boolean estudianteNuevo, CronogramaActividadJdbcDto procesoFlujo, PlanificacionCronograma planificacionCronograma, Integer recordEstudianteId, int nivelUbicacion, PeriodoAcademico pracId) throws MatriculaValidacionException, MatriculaException;
	
	public boolean agregarMateriasMatricula(int fcmtId, int cmpaId, BigDecimal dtmValorParcial, int fcesId, int rcesEstado, List<MateriaDto> listaMaterias) throws MatriculaException;
		
		
	/**
	 * Método que sirve para generar la solicitud de retiro de matrícula por parte del estudiante
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha solicitar
	 * @param estadoDtmt - estadoDtmt estado del detalle de matricula para poder habilitar la solicitud
	 * @param archivoRetiro - archivoRetiro, nombre del path y del archivo de solicitud de retiro
	 * @param estadoSolicitadoDtmt - estadoSolicitadoDtmt, estado del detalle de matricula, para solicitud de retiro
	 * @param estadoSolicitadoRces - estadoSolicitadoRces, estado del record estudiante, para inactivar el record de la materia solicitada
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException excepción general de matricula
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarMatriculaHomologacion(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
			,Integer valorMatricula , PlanificacionCronograma planificacionCronograma, String archivoSubido ,FichaEstudiante fces, PlanificacionCronogramaDto plcrDtoIntercambio,UsuarioRol usuarioRol ) throws MatriculaValidacionException, MatriculaException;
	
	/**MQ
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,calificacion
	 * para generar la matricula de un estudiante como registro de homologación de suficiencia de cultura física 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaDto - la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarMatriculaHomologacionSufCulturaFisica(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma, UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException;
	
	
	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,calificacion
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaDto - la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarMatriculaUbicacion(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma , String archivoSubido
											, FichaEstudiante fces, UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException;
	
	
	
	/**
	 * Genera matricula de pregrado con aranceles, gratuidadt, etc. 
	 * @author Arturo Villafuerte - ajvillafuerte.
	 * @param listMaterias
	 * @param fichaInscripcion
	 * @param estudianteNuevo
	 * @param procesoFlujo
	 * @param planificacionCronograma
	 * @param recordEstudianteId
	 * @param nivelUbicacion
	 * @param pracId
	 * @throws MatriculaValidacionException
	 * @throws MatriculaException
	 */
	public ComprobantePago generarMatriculaPregradoFull(
			
			List<MateriaDto> listMaterias, 
			FichaInscripcionDto fichaInscripcion, 
			Boolean estudianteNuevo,
			CronogramaActividadJdbcDto procesoFlujo, 
			PlanificacionCronograma planificacionCronograma,
			Integer recordEstudianteId, 
			int nivelUbicacion,
			PeriodoAcademico pracId,
			BigDecimal valorMatricula,
			int tipoGratuidadId,
			List<Arancel> listaAranceles
												
			) throws MatriculaValidacionException, MatriculaException;
	
	
	/**
	 * Genera matricula de pregrado con aranceles, gratuidadt, etc. 
	 * @author Arturo Villafuerte - ajvillafuerte.
	 * @param listMaterias
	 * @param fichaInscripcion
	 * @param estudianteNuevo
	 * @param procesoFlujo
	 * @param planificacionCronograma
	 * @param recordEstudianteId
	 * @param nivelUbicacion
	 * @param pracId
	 * @throws MatriculaValidacionException
	 * @throws MatriculaException
	 */
	public ComprobantePago generarMatriculaSuficiencias(
			
			List<MateriaDto> listMaterias, 
			FichaInscripcionDto fichaInscripcion, 
			Boolean estudianteNuevo,
			CronogramaActividadJdbcDto procesoFlujo, 
			PlanificacionCronograma planificacionCronograma,
			Integer recordEstudianteId, 
			int nivelUbicacion,
			PeriodoAcademico pracId,
			BigDecimal valorMatricula,
			int tipoGratuidadId,
			List<Arancel> listaAranceles
												
			) throws MatriculaValidacionException, MatriculaException;
	
	/**
	 * @author Arturo
	 * @param fcmtId
	 * @param cmpaId
	 * @param dtmValorParcial
	 * @param fcesId
	 * @param rcesEstado
	 * @param listaMaterias
	 * @return
	 * @throws MatriculaException
	 */
	public boolean agregarMateriasMatriculaFull(int fcmtId, int cmpaId, int fcesId, int rcesEstado, List<MateriaDto> listaMaterias, Nivel nivel, BigDecimal valorTotal, Gratuidad gratuidad, int tipoGratuidadId, Usuario usuario) throws MatriculaException;


	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,calificacion
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaDto - la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @param fces - ficha estudiante
	 * @param UsuarioRol - usuarioRol del usuario que realiza el proceso
	 * @param crearNuevaMatricula- boolean que indica si se debe o no crear la siguiente matricula de posgrado luego de registrar homologacion
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarMatriculaRegHistoricoPosgrado(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma , String archivoSubido
											, FichaEstudiante fces, UsuarioRol usuarioRol , boolean crearNuevaMatricula, List<MateriaDto> listaMateriaNivelPosgrado,  Integer nivelActualPosgrado) throws MatriculaValidacionException, MatriculaException;
		
	
	
	
	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,calificacion
	 * para generar la matricula de un estudiante 
	 * @param fcesId - id de la ficha estudiante
	 * @param fcinId- id de la ficha inscripcion
	 * @param grtId - id de la gratuidad
	 * @param cmpaId - id del comprobante de pago
	 * @param listDetalleMatricula - lista de detalles matricula
	 * @param listRecordEstudiante - lista de record estudiante
	 * @param listCalificacion - lista de calificaciones
	 * @param usuarioRol - usuario rol de la persona que realiza el cambio
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public Boolean eliminarHomologacion(int fcesId, int fcinId, int fcmtId, int grtId, int cmpaId,  List<DetalleMatricula> listDetalleMatricula, List<RecordEstudiante> listRecordEstudiante, List<Calificacion> listCalificacion, UsuarioRol usuarioRol) throws MatriculaException;
	
	
	/**MQ:
	 * Agregar registros a homologación las entidades : Se añade record, detalle_matricula y calificaciones a una homologación existente
	 * @param listMaterias - lista de materias que se va a agregar
	 * @param personaDto - la persona que se va a agregar asignaturas a la homologación
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarAgregarEnMatriculaHomologacion(List<MateriaDto> listMaterias, RegistroHomologacionDto personaDto,UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException;
	
	
	/**MQ:
	 * Editar registros  homologación  : Se actualiza notas y número de matricula de una asignatura homologada
	 * 
	 * @param materiaEditada -  materia que va a ser editada
	 * @param usuarioRol - la persona que se va a editar la asignatura en la homologación
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarEditarEnMatriculaHomologacion(MateriaDto materiaEditada,UsuarioRol usuarioRol) throws  MatriculaException;
	

	/**MQ:
	 * Eliminar un registro de homologación  
	 * 
     * @param materiaEditada -  materia que va a ser editada
	 * @param usuarioRol - la persona que se va a editar la asignatura en la homologación
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarEliminarEnMatriculaHomologacion(MateriaDto materiaEditada,UsuarioRol usuarioRol) throws MatriculaException;
	
	
	
	/**MQ:
	 * Método que sirve para generar la solicitud de retiro por situacion fortuita por parte del estudiante
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha solicitar
	 * @param archivoRetiro - archivoRetiro, nombre del path y del archivo de solicitud de retiro
	 * @param estadoSolicitadoDtmt - estadoSolicitadoDtmt, estado del detalle de matricula, para solicitud de retiro
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException excepción general de matricula
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarSolicitudRetiroFortuito(List<EstudianteJdbcDto> listDetalleMatricula, String archivoRetiro) throws Exception, MatriculaException;
	
	/**
	 * MQ:
	 * Método que sirve para verificar la solicitud de retiro por situacion fortuita por parte de la secretaria de carrera
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha solicitar
	 * @param archivoRetiro - archivoRetiro, nombre del path y del archivo de solicitud de retiro
	 * @param estadoSolicitadoDtmt - estadoSolicitadoDtmt, estado del detalle de matricula, para solicitud de retiro
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException excepción general de matricula
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean verificarSolicitudRetiroFortuito(List<EstudianteJdbcDto> listDetalleMatricula, Integer estadoSolicitud,  Usuario usuario) throws Exception, MatriculaException;
	
	
	/**
	 * MQ: 20/12/2018
	 * Método que sirve para aprobar la solicitud de retiro de matriculas por parte de los estudiantes
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha retirarse
	 * @param estadoRetiroTotal - estadoRetiroTotal estado del retiro si va ha ser en todas la materias
	 * @param idFichaMatricula - idFichaMatricula, id de la ficha matrícula, si es caso es retirar toda la matrícula
	 * @param usuario - usuario, datos del usuario que aprueba la solicitud de matrícula
	 * @return, retorna verdadero si se ejecuto la transacción caso contrario false
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean aprobarSolicitudRetiroFortuitoMatricula(List<EstudianteJdbcDto> listDetalleMatricula, boolean estadoRetiroTotal,  int idFichaMatricula, Usuario usuario, String observacionFinal) throws MatriculaException;
	
	
	/**
	 * MQ: 22/01/2019
	 * Método que sirve para anular las matriculas por parte del secretario abogado
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha retirarse
	 * @param observacionFinal- Observación ingresada al momento de realizar la anulación de la matricula
	 * @param usuario - usuario, datos del usuario que aprueba la solicitud de matrícula
	 * @return, retorna verdadero si se ejecuto la transacción caso contrario false
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean guardarAnulacionMatricula(List<EstudianteJdbcDto> listDetalleMatricula,boolean estadoRetiroTotal,int idFichaMatricula, Usuario usuario, String observacionFinal) throws MatriculaException;
}
