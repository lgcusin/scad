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

 ARCHIVO:     MatriculaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones del proceso de matriculación. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 27-03-2017          David Arellano                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ec.edu.uce.academico.ejb.dtos.CausalDto;
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteMatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoProcesoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ControlProcesoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoGratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoProcesoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.CausalDetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.ControlProceso;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.TipoProceso;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol; 

/**
 * Clase (Bean)MatriculaServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MatriculaServicioImpl implements MatriculaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource
	private SessionContext session;
	@Resource
    private UserTransaction userTransaction;
	@EJB
	FichaEstudianteServicio servfichaEstudiante;
	@EJB
	MallaCurricularParaleloServicio servMallaCurricularParaleloServicio;
	@EJB
	MallaCurricularParaleloDtoServicioJdbc servMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	FichaInscripcionDtoServicioJdbc servFichainscripcionDtoServicoJdbc;
	@EJB
	FichaEstudianteServicio servFichaEstudiante;
	@EJB
	PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB
	TipoProcesoServicio servTipoProceso; 
	@EJB
	FichaMatriculaDtoServicioJdbc servFichaMatriculaJdbc;
	@EJB
	ComprobantePagoServicio servComprobantePago;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaId - id de la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param nivelUbicacion - nivel de ubicacion en el que cae el matriculado
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param mallaCurricular - entidad malla curricular en la que se le asigna para la matricula 
	 * @param estudianteNuevo - booleano que permite determinar si es un estudiante nuevo o no
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarMatricula(List<MateriaDto> listMaterias, int personaId, FichaInscripcionDto fichaInscripcion, int nivelUbicacion
									, int tipoMatricula, int tipoGratuidadId, BigDecimal valorMatricula, MallaCurricular mallaCurricular, Boolean estudianteNuevo
									, PlanificacionCronograma planificacionCronograma, PeriodoAcademico pracId) throws MatriculaValidacionException, MatriculaException, EstudianteMatriculaValidacionException{
//		Boolean retorno = false;
		String retorno = null;
		try {
			session.getUserTransaction().begin();
			retorno="N/A";
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaId);
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fichaInscripcion.getFcinId());
			if (fichaInscripcionAux.getFcinMatriculado() == FichaInscripcionConstantes.SI_MATRICULADO_VALUE) {
				try {
					session.getUserTransaction().rollback();
				} catch (IllegalStateException e1) {
				} catch (SecurityException e1) {
				} catch (SystemException e1) {
				}
				throw new EstudianteMatriculaValidacionException(
						"Su matrícula ha sido generada con éxito.");
			}
			fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);//Modificar ficha inscripcion
			PeriodoAcademico pracAux = servPeriodoAcademico.buscarXestado( PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			if(estudianteNuevo){ // si es estudiante nuevo
				FichaEstudiante fcesAux = null;
				try {
					fcesAux = servFichaEstudiante.buscarPorFcinIdNueva(fichaInscripcion.getFcinId());	
				} catch (Exception e) {
				}
				if(fcesAux!=null){
					try {
						session.getUserTransaction().rollback();
					} catch (IllegalStateException e1) {
					} catch (SecurityException e1) {
					} catch (SystemException e1) {
					}
					throw new EstudianteMatriculaValidacionException("Usted ya posee una matrícula activa en el perído académico actual, no puede continuar.");
				}
				//creacion del objeto Ficha Estudiante
				FichaEstudiante fichaEstudiante = new FichaEstudiante();
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
				
				
				
				em.persist(fichaEstudiante);
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
				fichaMatricula.setFcmtPracId(pracAux.getPracId());
				
				//TODO SI ES BECADO ??? MODIFICAR AQUI
//				if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
					fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
//				}else{
					fichaMatricula.setFcmtValorTotal(valorMatricula);
//				}
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				//creo comprobante pago 
				//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
				
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
				int i = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(i==0){
					comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
				}
				em.persist(comprobantePago);
				//creo un detalle_matricula y un record_estudiante por materia 
				//TODO: falta poner el arancel y el valor (determinar como se calcula los valores dependiendo de el numero de horas de la materia) en el detalle_matricula 
				int contadorAux = 0;
				StringBuilder sbError = new StringBuilder(); // en caso de error
				sbError.append("No existe cupos en las materias: "); // en caso de error
				for (MateriaDto itemMaterias : listMaterias) {
					contadorAux ++;
					//modifico la malla curricular paralelo
					Integer mallaParaleloAux = null;
					int contadorParalelo = 0;
					for (ParaleloDto itemParalelo : itemMaterias.getListParalelos()) {
						contadorParalelo++;
						if(itemParalelo.getPrlId() == itemMaterias.getPrlId()){
							mallaParaleloAux = itemParalelo.getMlcrprId();
						}else{
							if(itemMaterias.getListParalelos().size() == contadorParalelo && mallaParaleloAux == null){
								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.validacion.exception")));
							}
						}
					}
//					Paralelo paralelo = em.find(Paralelo.class, itemMaterias.getPrlId()); // busco el paralelo de esa materia
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mallaParaleloAux);
					if(mallaCurricularParalelo.getMlcrprReservaRepetidos()!=null){
						if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()-mallaCurricularParalelo.getMlcrprReservaRepetidos()){ // si existen cupos 
							mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
						}else{
							if(contadorAux == listMaterias.size()){
								sbError.append(itemMaterias.getMtrDescripcion());
								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
							}else{
								sbError.append(itemMaterias.getMtrDescripcion()+ ", ");
							}
						}	
					}else{
						if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){ // si existen cupos 
							mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
						}else{
							if(contadorAux == listMaterias.size()){
								sbError.append(itemMaterias.getMtrDescripcion());
								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
							}else{
								sbError.append(itemMaterias.getMtrDescripcion()+ ", ");
							}
						}
					}
						
					
					em.merge(mallaCurricularParalelo);
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
//					StringBuilder sb = new StringBuilder();
//					sb.append("SELECT MAX(dtmtId) from DetalleMatricula");
//					Query q = em.createQuery(sb.toString());
//					Integer numDtmt = (Integer) q.getSingleResult();
//					numDtmt++;
//					detallaMatricula.setDtmtId(numDtmt);
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					int j = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(j==0){
						//arancel nulo y seteo el valor parcial en cero
						detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					}else{
						detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					}
					em.persist(detallaMatricula);
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					 i = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(i==0){
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
						
					}else{
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);	
					}
					
					em.persist(recordEstudiante);
					
				}
				
//				//creo la gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);		
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
				
			}else{  /// ESTUDIANTE CON RECORD
				//busco la ficha estudiante
				//FichaEstudiante fichaEstudiante = servfichaEstudiante.buscarXpersonaId(personaId);
				FichaEstudiante fichaEstudiante = servfichaEstudiante.buscarPorFcinId(fichaInscripcion.getFcinId());
				

				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(0); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
//				if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
//					fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
//				}else{
					fichaMatricula.setFcmtValorTotal(valorMatricula);
//				}
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				fichaMatricula.setFcmtPracId(pracAux.getPracId());
//				StringBuilder sb = new StringBuilder();
//				sb.append("SELECT MAX(fcmtId) from FichaMatricula");
//				Query q = em.createQuery(sb.toString());
//				Integer numFcmt = (Integer) q.getSingleResult();
//				numFcmt=numFcmt+1;
//				fichaMatricula.setFcmtId(numFcmt);
				em.persist(fichaMatricula);
				//creo comprobante pago 
				//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE); //según el tipo de matricula
				// Detalle de fechas de comprobante
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Timestamp((new Date()).getTime()));
				comprobantePago.setCmpaFechaEmision(new Timestamp(cal.getTime().getTime()));
					cal.add(Calendar.DAY_OF_WEEK, 4);
				Timestamp ts = new Timestamp(cal.getTime().getTime());
				comprobantePago.setCmpaFechaCaduca(ts);
				
				Carrera carrera = new Carrera();
				carrera = em.find(Carrera.class,fichaInscripcion.getCrrId());
				
				
				comprobantePago.setCmpaDescripcion(ComprobantePagoConstantes.DESCRIPCION_MATRICULA_NIVELACION+" "+carrera.getCrrDependencia().getDpnDescripcion());
				StringBuilder sbComprobanteAux = new StringBuilder();
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnUej());
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnCodSori());
				
				
				Integer secuencial=new Integer(0);
				try {
					secuencial = servComprobantePago.buscarMaxId();
					if(secuencial != null){
						secuencial++;	
					}else{
						secuencial=1;
					}
				}catch (Exception e) {
					e.printStackTrace();
					secuencial=1;
				}
//				System.out.println(secuencial);
				String secuencialString = secuencial.toString();
				String auxNumComprobante= GeneralesUtilidades.completarCadenaDer(sbComprobanteAux.toString(), 
						(ComprobantePagoConstantes.NUM_MAX_CARACTERES_COMPROBANTE-secuencialString.length()), "0");
				StringBuilder sbComprobante = new StringBuilder();
				sbComprobante.append(auxNumComprobante);
				sbComprobante.append(secuencialString);
				comprobantePago.setCmpaNumComprobante(sbComprobante.toString());
				comprobantePago.setCmpaCodigo(sbComprobante.toString());
				retorno = sbComprobante.toString();
				comprobantePago.setCmpaNumCompSecuencial(secuencial);
					comprobantePago.setCmpaTotalPago(valorMatricula);
					comprobantePago.setCmpaValorPagado(valorMatricula);
					comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
				
				
				comprobantePago.setCmpaTotalFacultad(ComprobantePagoConstantes.TOTAL_FACULTAD_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaProcSau(ComprobantePagoConstantes.PROC_SAU_INSCRIPCION_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				
				comprobantePago.setCmpaTipoUnidad(ComprobantePagoConstantes.TIPO_UNIDAD_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
				
				comprobantePago.setCmpaCantidad(ComprobantePagoConstantes.CANTIDAD_MATRICULA_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				int comparador = valorMatricula.compareTo(ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE);
				if(comparador==0){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_SEGUNDA_MATRICULA_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
				
				}else if (comparador==1){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_TERCERA_MATRICULA_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
				}
				
					comprobantePago.setCmpaPaiCodigo(ComprobantePagoConstantes.ECUADOR_VALUE_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaAplicaGratuidad(ComprobantePagoConstantes.NO_APLICA_GRATUIDAD_INSCRIPCION_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaMatrTipo(ComprobantePagoConstantes.MATRICULA_TIPO_ORDINARIA_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaModalidad(ComprobantePagoConstantes.MODALIDAD_PRESENCIAL_NIVELACION_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaEspeCodigo(carrera.getCrrEspeCodigo());
				
				while (true){
					ComprobantePagoDto cmpa=null;
					try {
						cmpa = servComprobantePago.buscarXNumComprobantePago(retorno);
					} catch (Exception e) {
					}
					
					if(cmpa!=null){
						secuencial=new Integer(0);
						try {
							secuencial = servComprobantePago.buscarMaxId();
							if(secuencial != null){
								secuencial++;	
							}else{
								secuencial=1;
							}
						}catch (Exception e) {
							secuencial=1;
						}
//						System.out.println(secuencial);
						secuencialString = secuencial.toString();
						auxNumComprobante= GeneralesUtilidades.completarCadenaDer(sbComprobanteAux.toString(), 
								(ComprobantePagoConstantes.NUM_MAX_CARACTERES_COMPROBANTE-secuencialString.length()), "0");
						sbComprobante = new StringBuilder();
						sbComprobante.append(auxNumComprobante);
						sbComprobante.append(secuencialString);
						comprobantePago.setCmpaNumComprobante(sbComprobante.toString());
						comprobantePago.setCmpaCodigo(sbComprobante.toString());
						retorno = sbComprobante.toString();
					}else{
						em.persist(comprobantePago);
						break;
					}
				}

				int contadorAux = 0;
				StringBuilder sbError = new StringBuilder(); // en caso de error
				sbError.append("No existen cupos en las materias: "); // en caso de error
				for (MateriaDto itemMaterias : listMaterias) {
					contadorAux ++;
					//modifico la malla curricular paralelo
					Integer mallaParaleloAux = null;
					int contadorParalelo = 0;
					for (ParaleloDto itemParalelo : itemMaterias.getListParalelos()) {
						contadorParalelo++;
						if(itemParalelo.getPrlId() == itemMaterias.getPrlId()){
							mallaParaleloAux = itemParalelo.getMlcrprId();
						}else{
							if(itemMaterias.getListParalelos().size() == contadorParalelo && mallaParaleloAux == null){
								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.validacion.exception")));
							}
						}
					}
//					Paralelo paralelo = em.find(Paralelo.class, itemMaterias.getPrlId()); // busco el paralelo de esa materia
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mallaParaleloAux);
					try {
						if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){ // si existen cupos 
							mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
						}else{
							if(contadorAux == listMaterias.size()){
								sbError.append(itemMaterias.getMtrDescripcion());
								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
							}else{
								sbError.append(itemMaterias.getMtrDescripcion()+ ", ");
							}
								
						}	
					} catch (Exception e) {
						 	mallaCurricularParalelo.setMlcrprInscritos(1); // agrego un cupo en la materia
					}
					
					em.merge(mallaCurricularParalelo);
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
//					sb = new StringBuilder();
//					sb.append("SELECT MAX(dtmtId) from DetalleMatricula");
//					q = em.createQuery(sb.toString());
//					Integer numDtmt = (Integer) q.getSingleResult();
//					numDtmt=numDtmt+1;
//					detallaMatricula.setDtmtId(numDtmt);
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					int ll = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(ll==0){
						//arancel nulo y seteo el valor parcial en cero
						detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					}else{
						//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
					}
					em.persist(detallaMatricula);
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
					em.persist(recordEstudiante);
					
				}
				
				//creo la gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);		
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
			}
			
			
			session.getUserTransaction().commit();
			// TODO: cambiar para que el metodo no maneje la transaccion sino el contenedor
		} catch (MatriculaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new MatriculaValidacionException(e.getMessage());
		
		} catch (EstudianteMatriculaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new EstudianteMatriculaValidacionException(e.getMessage());
		}catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaId - id de la persona que se va a matricular
	 * @param fichaInscripcion - entidad ficha inscripcion de la cual se va a realizar la matricula
	 * @param nivelUbicacion - nivel de ubicacion en el que cae el matriculado
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param mallaCurricular - entidad malla curricular en la que se le asigna para la matricula 
	 * @param estudianteNuevo - booleano que permite determinar si es un estudiante nuevo o no
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarMatriculaPosgrado(List<MateriaDto> listMaterias, int personaId, FichaInscripcionDto fichaInscripcion, int nivelUbicacion
									, int tipoMatricula, int tipoGratuidadId, BigDecimal valorMatricula, MallaCurricular mallaCurricular, Boolean estudianteNuevo
									, PlanificacionCronograma planificacionCronograma) throws MatriculaValidacionException, MatriculaException{
		String retorno = null;
		try {
			session.getUserTransaction().begin();
			
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaId);
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fichaInscripcion.getFcinId());
			fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);//Modificar ficha inscripcion
			
			if(estudianteNuevo){ // si es estudiante nuevo
				//creacion del objeto Ficha Estudiante
				FichaEstudiante fichaEstudiante = new FichaEstudiante();
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
				em.persist(fichaEstudiante);
				
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
				fichaMatricula.setFcmtPracId(fichaInscripcionAux.getFcinPeriodoAcademico().getPracId());
				
				//TODO SI ES BECADO ??? MODIFICAR AQUI
					fichaMatricula.setFcmtValorTotal(valorMatricula);
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				//creo comprobante pago 
				//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PENDIENTE_GENERACION_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
				// Detalle de fechas de comprobante
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Timestamp((new Date()).getTime()));
				comprobantePago.setCmpaFechaEmision(new Timestamp(cal.getTime().getTime()));
				
				//Se permite el pago hasta tres meses solo en medicina
				
				
				if(fichaInscripcion.getCrrId()==109){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_ANATOMIA_PATOLOGICA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==110){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_ANESTESIOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==111){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_CIRUGIA_GENERAL_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==112){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_CIRUGIA_ONCOLOGICA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==113){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_DERMATOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==114){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_GASTROENTEROLOGIA_Y_ENDOSCOPIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==115){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_GINECOLOGIA_Y_OBSTETRICIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==116){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_IMAGENOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==117){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_MEDICINA_CRITICA_Y_TERAPIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==118){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_MEDICINA_DE_EMERGENCIAS_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==119){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_MEDICINA_INTERNA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==120){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_NEFROLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==121){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_NEUMOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==122){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_ONCOLOGIA_CLINICA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==123){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_ORTOPEDIA_Y_TRAUMATOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==124){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_OTORRINOLARINGOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==125){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_PATOLOGIA_CLINICA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==126){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_PEDIATRIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==127){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_RADIOTERAPIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==128){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_PSIQUIATRIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==129){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_UROLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==139){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_EDUCACION_INICIAL_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==240){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MAESTRIA_CONSTRUCCIONES_OBRAS_CIVILES_COMPROBANTE_CARGA_CSV_VALUE);
					comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PAGADO_EN_VENTANILLA_VALUE);
				}else if(fichaInscripcion.getCrrId()==241){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_OFTALMOLOGIA_COMPROBANTE_CARGA_CSV_VALUE);
					comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PAGADO_EN_VENTANILLA_VALUE);
				}else if(fichaInscripcion.getCrrId()==137){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_FORENSE_COMPROBANTE_CARGA_CSV_VALUE);
					comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PAGADO_EN_VENTANILLA_VALUE);
				}else if(fichaInscripcion.getCrrId()==136){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_MEDICINA_FAMILIAR_COMPROBANTE_CARGA_CSV_VALUE);
					comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PAGADO_EN_VENTANILLA_VALUE);
				}else{
					//Por matrículas extraordinarias
					if(fichaInscripcion.getCrrId()==199){
							comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ENTRENAMIENTO_DEPORTIVO_COMPROBANTE_CARGA_CSV_VALUE);
//						}	
					}else if(fichaInscripcion.getCrrId()==214){
							comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_EDUCACION_LINGUISTICA_COMPROBANTE_CARGA_CSV_VALUE);
//						}	
					}else if(fichaInscripcion.getCrrId()==218){
							comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_EDUCACION_MENCION_TIC_COMPROBANTE_CARGA_CSV_VALUE);
//						}	
					}else if(fichaInscripcion.getCrrId()==236){
							comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_PSICOLOGIA_INTERVENCION_ADICCIONES_COMPROBANTE_CARGA_CSV_VALUE);
//						}	
					}else if(fichaInscripcion.getCrrId()==243){
							comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_VETERINARIA_EPIDEMIOLOGIA_SALUD_VETERINARIA_CARGA_CSV_VALUE);
//						}	
					}else if(fichaInscripcion.getCrrId()==244){
							comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_INGENIERIA_MAESTRIA_SISTEMAS_MANUFACTURA_CARGA_CSV_VALUE);
//						}	
					}else{
						Carrera crrAux = em.find(Carrera.class, fichaInscripcion.getCrrId());
						comprobantePago.setCmpaIdArancel(crrAux.getCrrIdArancel());
					}
//					
//					cal.add(Calendar.MONTH, -3);
					
				}
//				cal.add(Calendar.MONTH, 3);
				cal.add(Calendar.DAY_OF_WEEK, 5);	
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE);
				
				Timestamp ts = new Timestamp(cal.getTime().getTime());
				comprobantePago.setCmpaFechaCaduca(ts);
				
				Carrera carrera = new Carrera();
				carrera = em.find(Carrera.class,fichaInscripcion.getCrrId());
				
				
				comprobantePago.setCmpaDescripcion(ComprobantePagoConstantes.DESCRIPCION_MATRICULA_POSGRADO+" "+carrera.getCrrDependencia().getDpnDescripcion());
				StringBuilder sbComprobanteAux = new StringBuilder();
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnUej());
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnCodSori());
				Integer secuencial=new Integer(0);
				try {
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT max(cmpaNumCompSecuencial) from ComprobantePago ");
					Query q = em.createQuery(sbSql.toString());
					secuencial = (Integer)q.getSingleResult();
					if(secuencial != null){
						secuencial++;	
					}else{
						secuencial=1;
					}
				}catch (Exception e) {
				}
				
				String secuencialString = secuencial.toString();
				String auxNumComprobante= GeneralesUtilidades.completarCadenaDer(sbComprobanteAux.toString(), 
						(ComprobantePagoConstantes.NUM_MAX_CARACTERES_COMPROBANTE-secuencialString.length()), "0");
				StringBuilder sbComprobante = new StringBuilder();
				sbComprobante.append(auxNumComprobante);
				sbComprobante.append(secuencialString);
				comprobantePago.setCmpaNumComprobante(sbComprobante.toString());
				comprobantePago.setCmpaCodigo(sbComprobante.toString());
				retorno = sbComprobante.toString();
				comprobantePago.setCmpaNumCompSecuencial(secuencial);
				switch (personaAux.getPrsIdentificacion()) {
				case "1718470105":
				case "1723585889":
				case "1720232139":
				case "0605598127":
				case "1003059803":
				case "1714005749":
				case "1714831276":
				case "1718567843":
				case "1313525196":
				case "1719302158":
				case "1757335813":
				case "0502030752":
				case "1311758419":
				case "1756157986":
				case "1711932341":
				case "0401189469":
				case "2100368345":
				case "1720559036":
				case "0603034380":
				case "1103876197":
				case "1722454962":
				case "1400755987":
				case "1003567516":
				case "1712637667":
				case "1720237211":
				case "1103727069":
				case "0705335693":
				case "1718895608":
				case "1717915142":
				case "0924196033":
				case "1804295812":
				case "0604088120":
				case "2300116353":
				case "1104577349":
				case "1720643426":
				case "1713848719":
				case "0302152608":
				case "1717855421":
				case "0603933268":
				case "1717227712":
				case "1718315623":
				case "0603865221":
				case "1721054326":
				case "1803698883":
				case "1721898730":
				case "1720213188":
				case "1804309902":
				case "1719296640":
				case "1716259120":
				case "0401140231":
				case "1712643004":
				case "1803731353":
				case "1709574030":
				case "1709691636":
				case "1722720974":
				case "0603337395":
				case "1723722383":
				case "1804129086":
				case "1804263554":
				case "0604409490":
				case "1715627475":
				case "1715351274":
				case "0401545389":
				case "1715953210":
				case "1804242939":
				case "1309189940":
				case "1720505385":
				case "1600484016":
				case "1103616700":
				case "1103817167":
				case "0704466267":
				case "0604318030":
				case "1717464323":
				case "1714471438":
				case "0201793361":
				case "1719254904":
				case "1104870751":
				case "0703924159":
				case "1717435216":
				case "1804605374":
				case "0301748018":
				case "1804044046":
				case "1312848052":
				case "1311401101":
				case "1803787918":
				case "0503513376":
				case "1003082060":
				case "0503143521":
				case "1003569165":
				case "1803113214":
				case "0201918315":
				case "1718412891":
				case "1309499927":
				case "1724114929":
				case "0401593462":
				case "1719500975":
				case "1803994563":
				case "1804611117":
				case "0201852985":
				case "0929377091":
				case "1205490327":
				case "0604672857":
				case "1725454621":
				case "1716865694":
				case "0603866708":
				case "1719376657":
				case "1720533734":
				case "0603564485":
				case "1720064847":
				case "1003559448":
				case "1721514238":
				case "0926263989":
				case "1713757852":
				case "1721880761":
				case "0106036395":
				case "0502074065":
				case "0703961623":
				case "0502332380":
				case "1725201873":
				case "1715645899":
				case "1713423794":
				case "1718524737":
				case "0705416105":
				case "1804274007":
				case "1718851007":
				case "1716992803":
				case "1717839359":
				case "0923566731":
				case "1718361007":
				case "1104658701":
				case "1803605953":
				case "1312781105":
				case "1804472858":
				case "1720748621":
				case "1311594137":
				case "0302306584":
				case "0603462433":
				case "0502966757":
				case "1723542773":
				case "1716386675":
				case "0705429009":
				case "1500625270":
				case "1804041620":
				case "1722246947":
				case "1719368670":
				case "1311040016":
				case "1804530325":
				case "1721711149":
				case "1105030827":
				case "1716084841":
				case "0104473772":
				case "1721071262":
				case "1719593673":
				case "1718317108":
				case "0502550908":
				case "1803743853":
				case "0922427018":
				case "1104350069":
				case "1711761237":
				case "1718401472":
				case "1720688454":
				case "1722487731":
				case "1717540569":
				case "1003821186":
				case "1312838939":
				case "1721827978":
				case "0104434535":
				case "1722057583":
				case "1719372631":
				case "0502578313":
				case "1717556219":
				case "1718475443":
				case "1720897980":
				case "1716768930":
				case "0604655456":
				case "1720990520":
				case "1804697546":
				case "0202047130":
				case "1722634787":
				case "1803023009":
				case "0913727780":
				case "1715510259":
				case "1718829052":
				case "1713001194":
				case "1103302418":
				case "1721888210":
				case "1804021077":
				case "0803039270":
				case "1802822443":
				case "1719693358":
				case "1803191699":
				case "0923400279":
				case "1718481664":
				case "0401609284":
				case "1722620786":
				case "1712022258":
				case "1715781835":
				case "1721096434":
				case "1722254388":
				case "1104446313":
				case "0401462387":
				case "1715782486":
				case "1723563944":
				case "1717555948":
				case "0801978982":
				case "1722653191":
				case "1002975181":
				case "1723867055":
				case "1721355434":
				case "0704941277":
				case "1719050963":
				case "1900529924":
					//ODONTOLOGIA BECADO
				case "1713915682":	
					comprobantePago.setCmpaTotalPago(new BigDecimal(0.0));
					comprobantePago.setCmpaValorPagado(new BigDecimal(0.0));
					comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_PAGADO_VALUE);
					break;
				default:
					comprobantePago.setCmpaTotalPago(valorMatricula);
					comprobantePago.setCmpaValorPagado(valorMatricula);
					break;
				}
				
				
				comprobantePago.setCmpaTotalFacultad(ComprobantePagoConstantes.TOTAL_FACULTAD_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaProcSau(ComprobantePagoConstantes.PROC_SAU_INSCRIPCION_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				
				comprobantePago.setCmpaTipoUnidad(ComprobantePagoConstantes.TIPO_UNIDAD_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				
				comprobantePago.setCmpaCantidad(ComprobantePagoConstantes.CANTIDAD_MATRICULA_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				
				// Carrera ortodoncia
				if(fichaInscripcion.getCrrId()==25){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_ENDODONCIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==26){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_ODONTOPEDIATRIA_COMPROBANTE_CARGA_CSV_VALUE);	
				}else if(fichaInscripcion.getCrrId()==27){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_ORTODONCIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==28){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_PERIODONCIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==29){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_REHABILITACION_ORAL_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==30){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_IMPLANTOLOGIA_ORAL_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==31){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_ESTETICA_Y_OPERATORIA_COMPROBANTE_CARGA_CSV_VALUE);
				}else if(fichaInscripcion.getCrrId()==32){
					comprobantePago.setCmpaIdArancel(ComprobantePagoConstantes.ID_ARANCEL_INSCRIPCION_POSTGRADO_ODONTOLOGIA_CIRUGIA_ORAL_COMPROBANTE_CARGA_CSV_VALUE);
				}
				
				if(personaAux.getPrsUbicacionNacimiento().getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
					comprobantePago.setCmpaPaiCodigo(ComprobantePagoConstantes.ECUADOR_VALUE_COMPROBANTE_CARGA_CSV_VALUE);
				}else{
					comprobantePago.setCmpaPaiCodigo(ComprobantePagoConstantes.NO_ECUADOR_VALUE_COMPROBANTE_CARGA_CSV_VALUE);
				}
				comprobantePago.setCmpaAplicaGratuidad(ComprobantePagoConstantes.NO_APLICA_GRATUIDAD_INSCRIPCION_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaMatrTipo(ComprobantePagoConstantes.MATRICULA_TIPO_ORDINARIA_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaModalidad(ComprobantePagoConstantes.MODALIDAD_PRESENCIAL_POSTGRADO_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaEspeCodigo(carrera.getCrrEspeCodigo());
				
				
				em.persist(comprobantePago);
				
				//creo un detalle_matricula y un record_estudiante por materia 
				//TODO: falta poner el arancel y el valor (determinar como se calcula los valores dependiendo de el numero de horas de la materia) en el detalle_matricula 
//				int contadorAux = 0;
				StringBuilder sbError = new StringBuilder(); // en caso de error
				sbError.append("No existe cupos en las materias: "); // en caso de error
				for (MateriaDto itemMaterias : listMaterias) {
//					contadorAux ++;
					//modifico la malla curricular paralelo
//					Integer mallaParaleloAux = null;
//					int contadorParalelo = 0;
//					for (ParaleloDto itemParalelo : itemMaterias.getListParalelos()) {
//						contadorParalelo++;
//						if(itemParalelo.getPrlId() == itemMaterias.getPrlId()){
//							mallaParaleloAux = itemParalelo.getMlcrprId();
//						}else{
//							if(itemMaterias.getListParalelos().size() == contadorParalelo && mallaParaleloAux == null){
//								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.validacion.exception")));
//							}
//						}
//					}sdfsdfsd
					MallaCurricularParalelo mlcrprAux=  servMallaCurricularParaleloServicio.buscarXParaleloIdXMateriaId( itemMaterias.getMtrId(),fichaInscripcionAux.getFcinPeriodoAcademico().getPracId());
//					Paralelo paralelo = em.find(Paralelo.class, mlcrprAux.getMlcrprParalelo().getPrlId()); // busco el paralelo de esa materia
					try {
						mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos()+1); // agrego un cupo en la materia	
					} catch (Exception e) {
						mlcrprAux.setMlcrprInscritos(1); // inicio el cupo en la materia
					}
					
					
					em.merge(mlcrprAux);
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mlcrprAux);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					int m = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(m==0){
						//arancel nulo y seteo el valor parcial en cero
						detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					}else{
						detallaMatricula.setDtmtValorParcial(valorMatricula);
					}
					em.persist(detallaMatricula);
					
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mlcrprAux);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					if(comprobantePago.getCmpaEstado()==ComprobantePagoConstantes.ESTADO_PAGADO_VALUE){
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);	
					}else{
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
					}
					
					em.persist(recordEstudiante);
					
					
				}
				
				//creo la gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.NO_APLICA_GRATUIDAD_VALUE);		
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
				
			}
			
			
			session.getUserTransaction().commit();
			// TODO: cambiar para que el metodo no maneje la transaccion sino el contenedor
		}  catch (Exception e) {
			e.printStackTrace();
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException("Error al generar la matrícula, por favor acérquese a la Dirección de Tecnologías.");
		}
		return retorno;
	}
	
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
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma) throws MatriculaValidacionException, MatriculaException{
//		Boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaDto.getPrsId());
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, personaDto.getFcinId());
			fichaInscripcionAux.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);//Modificar ficha inscripcion
			
			//creacion del objeto Ficha Estudiante
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
			fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
			fichaEstudiante.setFcesPersona(personaAux);
			fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
			em.persist(fichaEstudiante);
			
			//creacion de la ficha matricula
			FichaMatricula fichaMatricula = new FichaMatricula();
			fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
			fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.NIVELACION_VALUE);
			fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			fichaMatricula.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
//			fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
			if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
			}else{
				fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
			}
			fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
			fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
			em.persist(fichaMatricula);
				
			//creo comprobante pago 
			//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
			ComprobantePago comprobantePago = new ComprobantePago();
			comprobantePago.setCmpaFichaMatricula(fichaMatricula);
			comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
			comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
			comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
			int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
			if(o==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
			}
			em.persist(comprobantePago);
			
			//creo un detalle_matricula y un record_estudiante por materia 
			//TODO: falta poner el arancel y el valor (determinar como se calcula los valores dependiendo de el numero de horas de la materia) en el detalle_matricula 
			int contadorAux = 0;
			StringBuilder sbError = new StringBuilder(); // en caso de error
			sbError.append("No existe cupos en las materias: "); // en caso de error
			for (MateriaDto itemMaterias : listMaterias) {
				contadorAux ++;
				//modifico la malla curricular paralelo
				Integer mallaParaleloAux = null;
				int contadorParalelo = 0;
				for (ParaleloDto itemParalelo : itemMaterias.getListParalelos()) {
					contadorParalelo++;
					if(itemParalelo.getPrlId() == itemMaterias.getPrlId()){
						mallaParaleloAux = itemParalelo.getMlcrprId();
					}else{
						if(itemMaterias.getListParalelos().size() == contadorParalelo && mallaParaleloAux == null){
							throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.validacion.exception")));
						}
					}
				}
				Paralelo paralelo = em.find(Paralelo.class, itemMaterias.getPrlId()); // busco el paralelo de esa materia
				MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mallaParaleloAux);
				if(mallaCurricularParalelo.getMlcrprInscritos() < paralelo.getPrlCupo()){ // si existen cupos 
					mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
				}else{
					if(contadorAux == listMaterias.size()){
						sbError.append(itemMaterias.getMtrDescripcion());
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}else{
						sbError.append(itemMaterias.getMtrDescripcion()+ ", ");
					}
							
				}
				em.merge(mallaCurricularParalelo);
				
				
				
				//detalle matricula
				DetalleMatricula detalleMatricula = new DetalleMatricula();
				detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
				detalleMatricula.setDtmtComprobantePago(comprobantePago);
				detalleMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
				detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(r==0){
					//arancel nulo y seteo el valor parcial en cero
					detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
				}else{
					//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
				}
				em.persist(detalleMatricula);
					
				//record
				RecordEstudiante recordEstudiante = new RecordEstudiante();
				recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
				recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
				recordEstudiante.setRcesEstado(itemMaterias.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_APROBADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				em.persist(recordEstudiante);
					
				//registro de notas
				Calificacion calificacion = new Calificacion();
				calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
				calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
				calificacion.setClfPromedioNotas(new Float(itemMaterias.getNotaPromedio().toString()));
				calificacion.setClfPromedioAsistencia(new Float(itemMaterias.getNotaTres().toString()));
				calificacion.setRecordEstudiante(recordEstudiante);
				em.persist(calificacion);
			}
				
			//creo la gratuidad
			TipoGratuidad tipoGratuidad = new TipoGratuidad();
			tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);		
			Gratuidad gratuidad = new Gratuidad();
			gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
			gratuidad.setGrtFichaEstudiante(fichaEstudiante);
			gratuidad.setGrtTipoGratuidad(tipoGratuidad);
			gratuidad.setGrtFichaMatricula(fichaMatricula);
			em.persist(gratuidad);
				
			
			
			//cargar en una lista las materias de segunda matricula
			List<MateriaDto> listMateriaDtoAux = new ArrayList<MateriaDto>();
			for (MateriaDto itemMateria : listMaterias) {
				if(itemMateria.getNumMatricula() == DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE){
					listMateriaDtoAux.add(itemMateria);
				}
			}
			
			if(listMateriaDtoAux.size()!=0){
				//tiene materias con segunda matricula
				
				//busco la ficha estudiante
				FichaEstudiante fichaEstudianteSegunda = servfichaEstudiante.buscarXpersonaId(personaDto.getPrsId());
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatriculaSegunda = new FichaMatricula();
				fichaMatriculaSegunda.setFcmtFichaEstudiante(fichaEstudianteSegunda);
				fichaMatriculaSegunda.setFcmtNivelUbicacion(FichaMatriculaConstantes.NIVELACION_VALUE);
				fichaMatriculaSegunda.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatriculaSegunda.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
//				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
				if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
					fichaMatriculaSegunda.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
				}else{
					fichaMatriculaSegunda.setFcmtValorTotal(new BigDecimal(valorMatricula));
				}
				fichaMatriculaSegunda.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatriculaSegunda.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatriculaSegunda);
					
				//creo comprobante pago 
				//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
				ComprobantePago comprobantePagoSegunda = new ComprobantePago();
				comprobantePagoSegunda.setCmpaFichaMatricula(fichaMatriculaSegunda);
				comprobantePagoSegunda.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePagoSegunda.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePagoSegunda.setCmpaTipo(tipoMatricula); //según el tipo de matricula
				int s = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(s==0){
					comprobantePagoSegunda.setCmpaFechaPago(new Timestamp(new Date().getTime()));
				}
				em.persist(comprobantePagoSegunda);
				
				//creo un detalle_matricula y un record_estudiante por materia 
				//TODO: falta poner el arancel y el valor (determinar como se calcula los valores dependiendo de el numero de horas de la materia) en el detalle_matricula 
				int contadorAuxSegunda = 0;
				StringBuilder sbErrorSegunda = new StringBuilder(); // en caso de error
				sbErrorSegunda.append("No existe cupos en las materias: "); // en caso de error
				for (MateriaDto itemMateriasSegunda : listMateriaDtoAux) {
					contadorAuxSegunda ++;
					//modifico la malla curricular paralelo
					Integer mallaParaleloAux = null;
					int contadorParalelo = 0;
					for (ParaleloDto itemParalelo : itemMateriasSegunda.getListParalelos()) {
						contadorParalelo++;
						if(itemParalelo.getPrlId() == itemMateriasSegunda.getPrlId()){
							mallaParaleloAux = itemParalelo.getMlcrprId();
						}else{
							if(itemMateriasSegunda.getListParalelos().size() == contadorParalelo && mallaParaleloAux == null){
								throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.validacion.exception")));
							}
						}
					}
					Paralelo paralelo = em.find(Paralelo.class, itemMateriasSegunda.getPrlId()); // busco el paralelo de esa materia
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mallaParaleloAux);
					if(mallaCurricularParalelo.getMlcrprInscritos() < paralelo.getPrlCupo()){ // si existen cupos 
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
					}else{
						if(contadorAuxSegunda == listMaterias.size()){
							sbErrorSegunda.append(itemMateriasSegunda.getMtrDescripcion());
							throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
						}else{
							sbErrorSegunda.append(itemMateriasSegunda.getMtrDescripcion()+ ", ");
						}
								
					}
					em.merge(mallaCurricularParalelo);
					
					
					
					//detalle matricula
					DetalleMatricula detalleMatricula = new DetalleMatricula();
					detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detalleMatricula.setDtmtComprobantePago(comprobantePagoSegunda);
					detalleMatricula.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); 
					detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					int t = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(t==0){
						//arancel nulo y seteo el valor parcial en cero
						detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					}else{
						//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
					}
					em.persist(detalleMatricula);
						
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudianteSegunda);
					recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					em.persist(recordEstudiante);
					
					//registro de notas
					Calificacion calificacion = new Calificacion();
					calificacion.setClfNota1(new Float(0));
					calificacion.setClfNota2(new Float(0));
					calificacion.setClfPromedioNotas(new Float(0));
					calificacion.setClfPromedioAsistencia(new Float(0));
					calificacion.setRecordEstudiante(recordEstudiante);
					em.persist(calificacion);
						
				}
					
				//creo la gratuidad
				TipoGratuidad tipoGratuidadSegunda = new TipoGratuidad();
				tipoGratuidadSegunda = em.find(TipoGratuidad.class, tipoGratuidadId);		
				Gratuidad gratuidadSegunda = new Gratuidad();
				gratuidadSegunda.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidadSegunda.setGrtFichaEstudiante(fichaEstudiante);
				gratuidadSegunda.setGrtTipoGratuidad(tipoGratuidadSegunda);
				gratuidadSegunda.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidadSegunda);
			}
			
			session.getUserTransaction().commit();
			// TODO: cambiar para que el metodo no maneje la transaccion sino el contenedor
		} catch (MatriculaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new MatriculaValidacionException(e.getMessage());
		
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//		retorno = true;
	}
	
	
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
	public boolean generarSolicitudRetiroMatricula(List<EstudianteJdbcDto> listDetalleMatricula, int estadoDtmt, String archivoRetiro, int estadoSolicitadoDtmt, int estadoSolicitadoRces) throws Exception, MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//************ ACTUALIZAR EL DETALLE MATRÍCULA y RECORD ESTUDIANTIL *********************
			//********************** DE LAS MATERIAS SELECCIONADAS **********************************
			//***************************************************************************************
			String extension = GeneralesUtilidades.obtenerExtension(archivoRetiro);
			if(listDetalleMatricula != null){
				for (EstudianteJdbcDto item : listDetalleMatricula) {
					DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
					if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){
//						dtmtAux.setDtmtEstado(estadoDtmt); no se guarda nada por cambio de funcionalidad en el proceso
						dtmtAux.setDtmtArchivoEstudiantes(item.getDtmtId()+"."+extension);
						dtmtAux.setDtmtEstadoCambio(estadoSolicitadoDtmt); 
						dtmtAux.setDtmtObservacionCambio("Solicitud de retiro de matrícula generada por el estudiante");
						dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
						dtmtAux.setDtmtEstadoHistorico(estadoSolicitadoDtmt); 
						dtmtAux.setDtmtObservacionHistorico("Solicitud de retiro de matrícula generada por el estudiante");
						dtmtAux.setDtmtFechaHistorico(new Timestamp(new Date().getTime()));
//						rcesAux.setRcesEstado(estadoSolicitadoRces); no se guarda nada por cambio de funcionalidad en el proceso
						dtmtAux.setDtmtEstadoSolicitud(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE);
						dtmtAux.setDtmtFechaSolicitud(new Timestamp(new Date().getTime()));
					}
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
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**
	 * MQ:
	 * Método que sirve para generar la solicitud de retiro por situacion fortuita por parte del estudiante
	 * @param listDetalleMatricula - listDetalleMatricula, lista de detalle de matrículas que va ha solicitar
	 * @param archivoRetiro - archivoRetiro, nombre del path y del archivo de solicitud de retiro
	 * @param estadoSolicitadoDtmt - estadoSolicitadoDtmt, estado del detalle de matricula, para solicitud de retiro
	 * @throws Exception - Exception excepción general
	 * @throws MatriculaException - MatriculaException excepción general de matricula
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarSolicitudRetiroFortuito(List<EstudianteJdbcDto> listDetalleMatricula, String archivoRetiro) throws Exception, MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//************ ACTUALIZAR EL DETALLE MATRÍCULA y RECORD ESTUDIANTIL *********************
			//********************** DE LAS MATERIAS SELECCIONADAS **********************************
			//***************************************************************************************
			String extension = GeneralesUtilidades.obtenerExtension(archivoRetiro);
			if(listDetalleMatricula != null){
				for (EstudianteJdbcDto item : listDetalleMatricula) {
					DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
					if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){
						dtmtAux.setDtmtArchivoEstudiantes(DetalleMatriculaConstantes.DTMT_ARCHIVO_SOLICITUD_RETIRO_FORTUITO_LABEL+"-"+item.getDtmtId()+"."+extension);
						dtmtAux.setDtmtObservacionCambio("Solicitud de retiro por situación fortuita generada por el estudiante");
						dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
						dtmtAux.setDtmtObservacionHistorico("Solicitud de retiro por situación fortuita generada por el estudiante");
						dtmtAux.setDtmtFechaHistorico(new Timestamp(new Date().getTime()));
						dtmtAux.setDtmtEstadoSolicitud(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE);  
						dtmtAux.setDtmtFechaSolicitud(new Timestamp(new Date().getTime()));
						dtmtAux.setDtmtCslRetiroId(item.getCslId());
					}
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
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
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
	public boolean verificarSolicitudRetiroFortuito(List<EstudianteJdbcDto> listDetalleMatricula, Integer estadoSolicitud, Usuario usuario) throws Exception, MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//************ ACTUALIZAR EL DETALLE MATRÍCULA y RECORD ESTUDIANTIL *********************
			//********************** DE LAS MATERIAS SELECCIONADAS **********************************
			//***************************************************************************************
			if(listDetalleMatricula != null){
				for (EstudianteJdbcDto item : listDetalleMatricula) {
					DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
					RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
					if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){
						dtmtAux.setDtmtObservacionHistorico("Solicitud de retiro por situación fortuita verificada por:  "+ usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
						dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
						dtmtAux.setDtmtEstadoSolicitud(estadoSolicitud);  
						dtmtAux.setDtmtFechaVerificacionRetiro(new Timestamp(new Date().getTime()));
						
					}
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
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
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
	public boolean aprobarSolicitudRetiroFortuitoMatricula(List<EstudianteJdbcDto> listDetalleMatricula, boolean estadoRetiroTotal,  int idFichaMatricula, Usuario usuario, String observacionFinal) throws MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//************ ACTUALIZAR EL DETALLE MATRÍCULA y RECORD ESTUDIANTIL *********************
			//************** Y FICHA MATRICULA DE LAS MATERIAS SOLICITADAS **************************
			//***************************************************************************************
			if(listDetalleMatricula != null){ //si la lista no esta vacia
			
					
					FichaMatricula fcmtAux = em.find(FichaMatricula.class, idFichaMatricula); //busco la ficha matrícula
					
					 for(EstudianteJdbcDto item : listDetalleMatricula){
						DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
						MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, item.getMlcrprId()); //busco la malla curricular paralelo
						
						if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){ //si encontró el detalle matricula y el record estudiante
							if(item.getEstadoSolicitudRetiro() == 0){ //solicitud aprobada
								dtmtAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE); 
								dtmtAux.setDtmtEstadoCambio(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE); 
								dtmtAux.setDtmtObservacionCambio("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
								// dtmtAux.setDtmtObservacionHistorico(observacionFinal); //SE USO PARA GUARDAR TEMPORALMENTE HASTA CREAR EL CAMPO DtmtObservacionFinalRetiro
								dtmtAux.setDtmtObservacionFinalRetiro(observacionFinal);
								dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
								dtmtAux.setDtmtEstadoRespuesta(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);
								dtmtAux.setDtmtFechaRespuesta(new Timestamp(new Date().getTime()));
								
								String extension = GeneralesUtilidades.obtenerExtension(item.getDtmtArchivoRespuesta());
				     			dtmtAux.setDtmtArchivoRespuesta(DetalleMatriculaConstantes.DTMT_ARCHIVO_RESOLUCION_RETIRO_FORTUITO_LABEL + "-"	+ item.getDtmtId() + "." + extension); //seteo el nombre del documento de resolucion
							
				     			rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE); 
								rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
								
								
								if(mlcrprAux.getMlcrprInscritos() == null || mlcrprAux.getMlcrprInscritos() <= 0){
									mlcrprAux.setMlcrprInscritos(0);
								}else{
									mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos() - 1);
								}
							}
							if(item.getEstadoSolicitudRetiro() == 1){ //solicitud negada
								dtmtAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
								dtmtAux.setDtmtEstadoCambio(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);
								dtmtAux.setDtmtObservacionCambio("La solicitud de retiro de matrícula ha sido negada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
								//dtmtAux.setDtmtObservacionHistorico(observacionFinal); //SE USO PARA GUARDAR TEMPORALMENTE HASTA CREAR EL CAMPO DtmtObservacionFinalRetiro
								dtmtAux.setDtmtObservacionFinalRetiro(observacionFinal);
								dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
								dtmtAux.setDtmtEstadoRespuesta(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);
								dtmtAux.setDtmtFechaRespuesta(new Timestamp(new Date().getTime()));
								
								String extension = GeneralesUtilidades.obtenerExtension(item.getDtmtArchivoRespuesta());
				     			dtmtAux.setDtmtArchivoRespuesta(DetalleMatriculaConstantes.DTMT_ARCHIVO_RESOLUCION_RETIRO_FORTUITO_LABEL + "-"	+ item.getDtmtId() + "." + extension); //seteo el nombre del documento de resolucion
								
				     			rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
								rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido negada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
							}
							em.merge(mlcrprAux);
							em.merge(rcesAux);
							em.merge(dtmtAux);
						}
					}
					
					 if(estadoRetiroTotal){//caso en el que se retira de todas las asignaturas
						fcmtAux.setFcmtEstado(FichaMatriculaConstantes.ESTADO_INACTIVO_VALUE);
						fcmtAux.setFcmtFechaEstado(new Timestamp(new Date().getTime()));
					}
				
			}
			session.getUserTransaction().commit();
		//	session.getUserTransaction().rollback();
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
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
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
	public boolean guardarAnulacionMatricula(List<EstudianteJdbcDto> listDetalleMatricula, boolean estadoRetiroTotal, int idFichaMatricula, Usuario usuario, String observacionFinal) throws MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//************ ACTUALIZAR EL DETALLE MATRÍCULA y RECORD ESTUDIANTIL *********************
			//************** Y FICHA MATRICULA DE LAS MATERIAS SOLICITADAS **************************
			//***************************************************************************************
			if(listDetalleMatricula != null){ //si la lista no esta vacia
			
					
					FichaMatricula fcmtAux = em.find(FichaMatricula.class, idFichaMatricula); //busco la ficha matrícula
					
					 for(EstudianteJdbcDto item : listDetalleMatricula){
						DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
						MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, item.getMlcrprId()); //busco la malla curricular paralelo
						
						if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){ //si encontró el detalle matricula y el record estudiante
                            //	dtmtAux.setDtmtEstadoCambio(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE); 
							//  dtmtAux.setDtmtObservacionHistorico(observacionFinal); //SE USO PARA GUARDAR TEMPORALMENTE HASTA CREAR EL CAMPO DtmtObservacionFinalRetiro
							//	dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
							// dtmtAux.setDtmtObservacionCambio("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());

								dtmtAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE); 							
								dtmtAux.setDtmtObservacionAnulacion(observacionFinal);
								dtmtAux.setDtmtTipoAnulacion(item.getDtmtTipoAnulacion()); //Tipo de anulación
								dtmtAux.setDtmtFechaAnulacion(new Timestamp(new Date().getTime()));
								dtmtAux.setDtmtRegistranteAnulacion("La asignatura ha sido anulada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
							
								String extension = GeneralesUtilidades.obtenerExtension(item.getDtmtArchivoAnulacion());
				     			dtmtAux.setDtmtArchivoAnulacion(DetalleMatriculaConstantes.DTMT_ARCHIVO_RESOLUCION_ANULACION_LABEL + "-"+ item.getDtmtId() + "." + extension); //seteo el nombre del documento de resolucion
				     			
				     									
								if(item.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE){
									
									rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE); 
									rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());

									for (CausalDto causalDto : item.getListaCausalesDto()) {
										Causal causalAux= em.find(Causal.class, causalDto.getCslId());
										CausalDetalleMatricula csdtmtAux= new CausalDetalleMatricula();
										csdtmtAux.setCsdtmtDetalleMatricula(dtmtAux);  //Guardo el detalleMatricula
										csdtmtAux.setCsdtmtCausal(causalAux);   //Guardo el causal
										em.persist(csdtmtAux);
									}
									
								}else{
									
									rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_VALUE); 
									rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());

									
								}
								
								//Si la matricula se anula se debe disminuir el Número de inscritos
								if(mlcrprAux.getMlcrprInscritos() == null || mlcrprAux.getMlcrprInscritos() <= 0){
									mlcrprAux.setMlcrprInscritos(0);
								}else{
									mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos() - 1);
								}
					
					
							em.merge(mlcrprAux);
							em.merge(rcesAux);
							em.merge(dtmtAux);
						}
					}
					
					 if(estadoRetiroTotal){//caso en el que no se retira de todas las asignaturas
						fcmtAux.setFcmtEstado(FichaMatriculaConstantes.ESTADO_INACTIVO_VALUE);
						fcmtAux.setFcmtFechaEstado(new Timestamp(new Date().getTime()));
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
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
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
	public boolean aprobarSolicitudRetiroMatricula(List<EstudianteJdbcDto> listDetalleMatricula, boolean estadoRetiroTotal, int estadoDtmt, int dtmtEstadoCambio, int estadoRecord, int idFichaMatricula, int estadoFichaMatricula, Usuario usuario) throws MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			//***************************************************************************************
			//************ ACTUALIZAR EL DETALLE MATRÍCULA y RECORD ESTUDIANTIL *********************
			//************** Y FICHA MATRICULA DE LAS MATERIAS SOLICITADAS **************************
			//***************************************************************************************
			if(listDetalleMatricula != null){ //si la lista no esta vacia
				if(!estadoRetiroTotal){ //caso en el que se retira en varias materias
					for(EstudianteJdbcDto item : listDetalleMatricula){
						DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
						RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
						MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, item.getMlcrprId()); //busco la malla curricular paralelo
						
						if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){ //si encontró el detalle matricula y el record estudiante
							if(item.getEstadoSolicitudRetiro() == 0){ //solicitud aprobada
								dtmtAux.setDtmtEstado(estadoDtmt); 
								dtmtAux.setDtmtEstadoCambio(dtmtEstadoCambio); 
								dtmtAux.setDtmtObservacionCambio("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
								dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
								dtmtAux.setDtmtEstadoRespuesta(dtmtEstadoCambio);
								dtmtAux.setDtmtFechaRespuesta(new Timestamp(new Date().getTime()));
								rcesAux.setRcesEstado(estadoRecord); 
								rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
								if(mlcrprAux.getMlcrprInscritos() == null || mlcrprAux.getMlcrprInscritos() <= 0){
									mlcrprAux.setMlcrprInscritos(0);
								}else{
									mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos() - 1);
								}
							}
							if(item.getEstadoSolicitudRetiro() == 1){ //solicitud negada
								dtmtAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
								dtmtAux.setDtmtEstadoCambio(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);
								dtmtAux.setDtmtObservacionCambio("La solicitud de retiro de matrícula ha sido negada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
								dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
								dtmtAux.setDtmtEstadoRespuesta(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);
								dtmtAux.setDtmtFechaRespuesta(new Timestamp(new Date().getTime()));
								rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
								rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido negada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
							}
							em.merge(mlcrprAux);
							em.merge(rcesAux);
							em.merge(dtmtAux);
						}
					}
				}else{ //caso en el que se retira en todas las materias
					FichaMatricula fcmtAux = em.find(FichaMatricula.class, idFichaMatricula); //busco la ficha matrícula
					if(fcmtAux.getFcmtId() != 0){ //verifico que exista una ficha matrícula
						boolean retiroTodas = true;
						for(EstudianteJdbcDto item : listDetalleMatricula){
							DetalleMatricula dtmtAux = em.find(DetalleMatricula.class, item.getDtmtId()); //busco el detalle matricula
							RecordEstudiante rcesAux = em.find(RecordEstudiante.class, item.getRcesId()); //busco el record estudiante
							MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, item.getMlcrprId()); //busco la malla curricular paralelo
							if(dtmtAux.getDtmtId() != 0 && rcesAux.getRcesId() != 0){//verifico que el detalle matricula y el record estudiante exista
								if(item.getEstadoSolicitudRetiro() == 0){ //solicitud aprobada
									dtmtAux.setDtmtEstado(estadoDtmt); 
									dtmtAux.setDtmtEstadoCambio(dtmtEstadoCambio); 
									dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
									dtmtAux.setDtmtEstadoRespuesta(dtmtEstadoCambio);
									dtmtAux.setDtmtFechaRespuesta(new Timestamp(new Date().getTime()));
									rcesAux.setRcesEstado(estadoRecord); 
									rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido aprobado por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
									if(mlcrprAux.getMlcrprInscritos() == null || mlcrprAux.getMlcrprInscritos() <= 0){
										mlcrprAux.setMlcrprInscritos(0);
									}else{
										mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos() - 1);
									}
								}
								if(item.getEstadoSolicitudRetiro() == 1){ //solicitud negada
									dtmtAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
									dtmtAux.setDtmtEstadoCambio(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);
									dtmtAux.setDtmtObservacionCambio("La solicitud de retiro de matrícula ha sido negada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
									dtmtAux.setDtmtFechaCambio(new Timestamp(new Date().getTime()));
									dtmtAux.setDtmtEstadoRespuesta(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE);
									dtmtAux.setDtmtFechaRespuesta(new Timestamp(new Date().getTime()));
									rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
									rcesAux.setRcesObservacion("La solicitud de retiro de matrícula ha sido negada por: "+usuario.getUsrIdentificacion()+" "+usuario.getUsrPersona().getPrsPrimerApellido()+" "+usuario.getUsrPersona().getPrsSegundoApellido()+" "+usuario.getUsrPersona().getPrsNombres()+" "+usuario.getUsrNick());
									retiroTodas = false;
								}
								em.merge(mlcrprAux);
								em.merge(rcesAux);
								em.merge(dtmtAux);
							}
						}
						if(retiroTodas){
							fcmtAux.setFcmtEstado(estadoFichaMatricula);
							fcmtAux.setFcmtFechaEstado(new Timestamp(new Date().getTime()));
						}
					}
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
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void generarMatriculaPregrado(List<MateriaDto> listMaterias, FichaInscripcionDto fichaInscripcion, Boolean estudianteNuevo ,CronogramaActividadJdbcDto procesoFlujo, PlanificacionCronograma planificacionCronograma ,Integer recordEstudianteId , int nivelUbicacion, PeriodoAcademico pracId) throws MatriculaValidacionException, MatriculaException{
//		Boolean retorno = false;
		try {
			session.getUserTransaction().begin();
			
			//busco la persona
			Persona personaAux = em.find(Persona.class, fichaInscripcion.getPrsId());
			
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fichaInscripcion.getFcinId());
			fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);//Modificar ficha inscripcion
			
			if(estudianteNuevo){ // si es estudiante nuevo
				//creacion del objeto Ficha Estudiante
				FichaEstudiante fichaEstudiante = new FichaEstudiante();
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
				em.persist(fichaEstudiante);
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(0); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
				fichaMatricula.setFcmtPracId(pracId.getPracId());
				
				//TODO SI ES BECADO ??? MODIFICAR AQUI
//				if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
//				}else{
//					fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
//				}
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				//creo comprobante pago 
				//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE); //según el tipo de matricula
//				int i = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
//				if(i==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
//				}
				em.persist(comprobantePago);
				
				
				//creo un detalle_matricula y un record_estudiante por materia 
				StringBuilder sbError = new StringBuilder(); // en caso de error
				sbError.append("No existe cupos en las materias: "); // en caso de error
				for (MateriaDto itemMaterias : listMaterias) {
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMaterias.getMlcrmtId(), itemMaterias.getPrlId());
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprAux.getMlcrprId());
					if(mallaCurricularParalelo.getMlcrprInscritos() == null){
						mallaCurricularParalelo.setMlcrprInscritos(0);
					}
					if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){ // si existen cupos 
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
					}else{
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}
					em.merge(mallaCurricularParalelo);
					
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					em.persist(detallaMatricula);
					
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					em.persist(recordEstudiante);
				}
			}else{  /// ESTUDIANTE CON RECORD
				
				//busco la ficha estudiante
				FichaEstudiante fichaEstudiante = servfichaEstudiante.buscarXpersonaId(fichaInscripcion.getPrsId());
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(0); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
//				if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
				fichaMatricula.setFcmtPracId(pracId.getPracId());
//				}else{
//					fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
//				}
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				//creo comprobante pago 
				//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE); //según el tipo de matricula
//				int l = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
//				if(l==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
//				}
				em.persist(comprobantePago);
				//creo un detalle_matricula y un record_estudiante por materia 
				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
				for (MateriaDto itemMaterias : listMaterias) {
					try {
						mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMaterias.getMlcrmtId(), itemMaterias.getPrlId());
					} catch (Exception e) {
						session.getUserTransaction().rollback();
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}					
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprAux.getMlcrprId());
					//asdasdasdasd PTM2
					if(mallaCurricularParalelo.getMlcrprInscritos()==null){
						mallaCurricularParalelo.setMlcrprInscritos(0);
					}
					if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){ // si existen cupos 
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
					}else{
						session.getUserTransaction().rollback();
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}
					
					em.merge(mallaCurricularParalelo);
					
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					em.persist(detallaMatricula);
					
					//record  ESTA MAL........MQ
//					RecordEstudiante recordEstudiante = em.find(RecordEstudiante.class, recordEstudianteId);
//					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
//					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
//					recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
//					em.persist(recordEstudiante);
//					
					
					//record   
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					em.persist(recordEstudiante);
				}
			}
			
			
			session.getUserTransaction().commit();
		} catch (MatriculaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new MatriculaValidacionException(e.getMessage());
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		}
//		retorno = true;
	}
	
	
	public boolean agregarMateriasMatricula(int fcmtId, int cmpaId, BigDecimal dtmValorParcial, int fcesId, int rcesEstado, List<MateriaDto> listaMaterias) throws MatriculaException{
		boolean retorno = false;
		try {
			session.getUserTransaction().begin();

			ComprobantePago comprobanteAux = em.find(ComprobantePago.class, cmpaId);
			FichaEstudiante fichaEstuAux = em.find(FichaEstudiante.class, fcesId);

			if(listaMaterias.size() > 0 || listaMaterias != null){
				for (MateriaDto itemMateria : listaMaterias) {
					if(itemMateria.getPrlId() != null && itemMateria.getMtrId() != 0){
						MallaCurricularParaleloDto mlcrprDto = new MallaCurricularParaleloDto();
						mlcrprDto = servMallaCurricularParaleloDtoServicioJdbc.buscarCupoMlcrprXMateriaXParalelo(itemMateria.getPrlId().intValue(),itemMateria.getMtrId());

						//					for (ParaleloDto paralelo : itemMateria.getMtrListParalelo()) {
						MallaCurricularParalelo mlcrprAux = em.find(MallaCurricularParalelo.class, mlcrprDto.getMlcrprId());

						DetalleMatricula detalleAux = new DetalleMatricula();
						detalleAux.setDtmtComprobantePago(comprobanteAux);
						detalleAux.setDtmtMallaCurricularParalelo(mlcrprAux);
						detalleAux.setDtmtNumero(itemMateria.getNumMatricula());
						detalleAux.setDtmtValorParcial(dtmValorParcial);
						detalleAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
						em.persist(detalleAux);

						RecordEstudiante recordAux = new RecordEstudiante();
						recordAux.setRcesMallaCurricularParalelo(mlcrprAux);
						recordAux.setRcesFichaEstudiante(fichaEstuAux);
						recordAux.setRcesEstado(rcesEstado);
						em.persist(recordAux);

						if(mlcrprAux.getMlcrprInscritos() == null){
							mlcrprAux.setMlcrprInscritos(0);
						}
						mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos() + 1);
						em.merge(mlcrprAux);
						//						break;
						//					}
					}
				}
			}
			session.getUserTransaction().commit();
			retorno = true;
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**MQ
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
	public Boolean generarMatriculaHomologacion(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma , String archivoSubido
											, FichaEstudiante fces, PlanificacionCronogramaDto plcrDtoIntercambio, UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaDto.getPrsId());
			
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, personaDto.getFcinId());
			fichaInscripcionAux.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);//Modificar ficha inscripcion
			fichaInscripcionAux.setFcinVigente(FichaInscripcionConstantes.VIGENTE_VALUE);
			
			if(fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_VALUE){
				fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE);
				PeriodoAcademico periodoPosgradoAux= new PeriodoAcademico();
				periodoPosgradoAux= em.find(PeriodoAcademico.class, personaDto.getFcinPeriodoPosgradoId());
				fichaInscripcionAux.setFcinPeriodoAcademico(periodoPosgradoAux);
				
			}else if((fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
				||(fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE)){
				
				fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE);
				
			}else{
			fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE);
			}
			fichaInscripcionAux.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_REINGRESO_VALUE);
			
			//Si existe carrera anterior grabo y desactivo la ficha inscripción activa de la carrera anterior
			if(personaDto.getFcinCrrAnteriorId()!= GeneralesConstantes.APP_ID_BASE){
			
				fichaInscripcionAux.setFcinCrrAnteriorId(personaDto.getFcinCrrAnteriorId());
			 
			 //BUSCO LAS FICHAS INSCRIPCION ACTIVAS EN LA CARRERA ANTERIOR
			   List<FichaInscripcionDto> listaFichaInscripcionDtoAnteriorAux=servFichainscripcionDtoServicoJdbc.buscarXidentificacionXcarreraXEstado(personaDto.getPrsIdentificacion(), personaDto.getFcinCrrAnteriorId(),FichaInscripcionConstantes.ACTIVO_VALUE);
			
			   if(listaFichaInscripcionDtoAnteriorAux!=null){
			   for (FichaInscripcionDto fichaInscripcionDto : listaFichaInscripcionDtoAnteriorAux) {
						FichaInscripcion fichaInscripcionAnterior= em.find(FichaInscripcion.class, fichaInscripcionDto.getFcinId());
						   fichaInscripcionAnterior.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
						   fichaInscripcionAnterior.setFcinVigente(FichaInscripcionConstantes.NO_VIGENTE_VALUE);
			      }
			  }
			   
			}
						
			if(personaDto.getFcinEstadoIngreso()!=GeneralesConstantes.APP_ID_BASE){
			fichaInscripcionAux.setFcinEstadoIngreso(personaDto.getFcinEstadoIngreso());
			}
			if(personaDto.getFcinAnioAbandonaCarrera()!=null){
		    fichaInscripcionAux.setFcinAnioAbandonaCarrera(personaDto.getFcinAnioAbandonaCarrera());
			}
			if(archivoSubido!=null){
			String extension = GeneralesUtilidades.obtenerExtension(archivoSubido);
			fichaInscripcionAux.setFcinDocumentoIngreso(personaDto.getPrsIdentificacion()+"-"+personaDto.getFcinId()+"."+extension);
			}
			
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
			
			if(fces==null){ //NO EXISTE FICHAS ESTUDIANTE EN LA CARRERA
        	    //creacion del objeto Ficha Estudiante
			
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
				
				if(personaDto.getFcesUnivEstudPrevId()!=GeneralesConstantes.APP_ID_BASE){
					fichaEstudiante.setFcesUnivEstudPrevId(personaDto.getFcesUnivEstudPrevId());
				}
							
				if(personaDto.getFcesTipoUnivEstudprev()!=GeneralesConstantes.APP_ID_BASE){
				fichaEstudiante.setFcesTipoUnivEstudPrev(personaDto.getFcesTipoUnivEstudprev());
				}
				if(personaDto.getFcesEstadoEstudprev()!=GeneralesConstantes.APP_ID_BASE){
				fichaEstudiante.setFcesEstadoEstudPrev(personaDto.getFcesEstadoEstudprev());
				}
				
				if(personaDto.getFcesTitEstudPrevId()!=GeneralesConstantes.APP_ID_BASE){
					fichaEstudiante.setFcesTitEstudPrevId(personaDto.getFcesTitEstudPrevId());
					}
				
				if(personaDto.getFcesRegTituloPrev()!=null){
					fichaEstudiante.setFcesRegTituloPrev(personaDto.getFcesRegTituloPrev());
					}
				
				
				em.persist(fichaEstudiante);
			
			}else{ // YA EXISTE UNA FICHA ESTUDIANTE
				
				
				fichaEstudiante= em.find(FichaEstudiante.class, fces.getFcesId()); // buscamos la ficha estudiante existente para editar
						
				     if(personaDto.getFcesUnivEstudPrevId()!=GeneralesConstantes.APP_ID_BASE){
							fichaEstudiante.setFcesUnivEstudPrevId(personaDto.getFcesUnivEstudPrevId());
						}
									
						if(personaDto.getFcesTipoUnivEstudprev()!=GeneralesConstantes.APP_ID_BASE){
						fichaEstudiante.setFcesTipoUnivEstudPrev(personaDto.getFcesTipoUnivEstudprev());
						}
						if(personaDto.getFcesEstadoEstudprev()!=GeneralesConstantes.APP_ID_BASE){
						fichaEstudiante.setFcesEstadoEstudPrev(personaDto.getFcesEstadoEstudprev());
						}
						
						if(personaDto.getFcesTitEstudPrevId()!=GeneralesConstantes.APP_ID_BASE){
							fichaEstudiante.setFcesTitEstudPrevId(personaDto.getFcesTitEstudPrevId());
							}
						
						if(personaDto.getFcesRegTituloPrev()!=null){
							fichaEstudiante.setFcesRegTituloPrev(personaDto.getFcesRegTituloPrev());
						}
				
			}
		
	
		
				//creacion de la ficha matricula HOMOLOGACION
			FichaMatricula fichaMatricula = new FichaMatricula();
			fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
			//Verificar nivel
			fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.PRIMER_NIVEL_VALUE);
			fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			fichaMatricula.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
			if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
			}else{
				fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
			}
			
			fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
			
			
			
//			if(fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE){
//				//Se busca planificacion cronograma en el periodo actual, pues Intercambio crea la ficha matrícula en el periodo actual
//				PlanificacionCronograma plcrIntercambioAux= new PlanificacionCronograma();
//				plcrIntercambioAux= em.find(PlanificacionCronograma.class, plcrDtoIntercambio.getPlcrId());
//				
//				fichaMatricula.setFcmtPlanificacionCronograma(plcrIntercambioAux);
//				
//				PeriodoAcademico pracActivo=servPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//				fichaMatricula.setFcmtPracId(pracActivo.getPracId());
//			}else{
				// el resto de tipos de ingreso utiliza planificación cronograma de homologacion
			fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
			fichaMatricula.setFcmtPracId(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
	//		}
			ConfiguracionCarrera configuracionCarrera=	em.find(ConfiguracionCarrera.class, fichaInscripcionAux.getFcinConfiguracionCarrera().getCncrId());
			if(configuracionCarrera.getCncrModalidad()!=null){
			fichaMatricula.setFcmtModalidad(configuracionCarrera.getCncrModalidad().getMdlId());
			}
			
		    em.persist(fichaMatricula);
				
			//creo comprobante pago 
			//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
			ComprobantePago comprobantePago = new ComprobantePago();
			comprobantePago.setCmpaFichaMatricula(fichaMatricula);
			comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
			comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
			comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
			int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
			if(o==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
			}
		    em.persist(comprobantePago);
		    
		    
		
			
			//creo un detalle_matricula y un record_estudiante por materia 
			//TODO: falta poner el arancel y el valor (determinar como se calcula los valores dependiendo de el numero de horas de la materia) en el detalle_matricula 
			//int contadorAux = 0;
			StringBuilder sbError = new StringBuilder(); // en caso de error
			sbError.append("No existe cupos en las materias: "); // en caso de error
	
		
			
			if(listMaterias!=null){
			for (MateriaDto itemMaterias : listMaterias) {

				
				//BUSCO MALLA_CURRICULAR_MATERIA  POR EL ID DE MLCRMT QUE VIENE EN CADA MATERIA HOMOLOGADA
				MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
				
				//detalle matricula
				DetalleMatricula detalleMatricula = new DetalleMatricula();
				detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
				detalleMatricula.setDtmtComprobantePago(comprobantePago);
				detalleMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
				detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
				int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(r==0){
					//arancel nulo y seteo el valor parcial en cero
					detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
				}else{
					//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
				}
			   	em.persist(detalleMatricula);
					
				//record
				RecordEstudiante recordEstudiante = new RecordEstudiante();
				recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
				recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
				
				if((fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
					|| (fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE)){
					recordEstudiante.setRcesEstado(itemMaterias.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_APROBADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				}else{
				recordEstudiante.setRcesEstado(itemMaterias.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				}
			
			 	em.persist(recordEstudiante);
					
				//registro de notas
				Calificacion calificacion = new Calificacion();
				calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
				calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
				calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
				calificacion.setRecordEstudiante(recordEstudiante);
			 	em.persist(calificacion);
			 }
			
			} 	
			
			
			//creo la gratuidad
			TipoGratuidad tipoGratuidad = new TipoGratuidad();
			//tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);	
			tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
			Gratuidad gratuidad = new Gratuidad();
			gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
			gratuidad.setGrtFichaEstudiante(fichaEstudiante);
			gratuidad.setGrtTipoGratuidad(tipoGratuidad);
			gratuidad.setGrtFichaMatricula(fichaMatricula);
		  	em.persist(gratuidad);
			
			
			//creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.FCIN_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaInscripcionAux.getFcinId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaEstudiante.getFcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaMatricula.getFcmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(comprobantePago.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.GRT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(gratuidad.getGrtId());
				
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("actualizar-");
			observacionAux.append(JdbcConstantes.FCIN_ID);
			observacionAux.append("-");
			observacionAux.append(fichaInscripcionAux.getFcinId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCES_ID);
			observacionAux.append("-");
			observacionAux.append(fichaEstudiante.getFcesId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCMT_ID);
			observacionAux.append("-");
			observacionAux.append(fichaMatricula.getFcmtId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.CMPA_ID);
			observacionAux.append("-");
			observacionAux.append(comprobantePago.getCmpaId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.GRT_ID);
			observacionAux.append("-");
			observacionAux.append(gratuidad.getGrtId());
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	em.persist(controlProceso); 
	
		  	
		session.getUserTransaction().commit();
	
			retorno = true;
	
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//	
		return retorno;
	}
	
	
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
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma, UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaDto.getPrsId());
			
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, personaDto.getFcinId());
			fichaInscripcionAux.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);//Modificar ficha inscripcion
			fichaInscripcionAux.setFcinVigente(FichaInscripcionConstantes.VIGENTE_VALUE);
			fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE);
			fichaInscripcionAux.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_REINGRESO_VALUE);
							
			if(personaDto.getFcinEstadoIngreso()!=GeneralesConstantes.APP_ID_BASE){
			fichaInscripcionAux.setFcinEstadoIngreso(personaDto.getFcinEstadoIngreso());
			}
			 //creacion del objeto Ficha Estudiante
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
				em.persist(fichaEstudiante);
			
				//creacion de la ficha matricula HOMOLOGACION
			FichaMatricula fichaMatricula = new FichaMatricula();
			fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
			//Verificar nivel
			fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.PRIMER_NIVEL_VALUE);
			fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			fichaMatricula.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
			if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
			}else{
				fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
			}
			
			fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
			fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
			fichaMatricula.setFcmtPracId(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
	//		}
			ConfiguracionCarrera configuracionCarrera=	em.find(ConfiguracionCarrera.class, fichaInscripcionAux.getFcinConfiguracionCarrera().getCncrId());
			if(configuracionCarrera.getCncrModalidad()!=null){
			fichaMatricula.setFcmtModalidad(configuracionCarrera.getCncrModalidad().getMdlId());
			}
			
		    em.persist(fichaMatricula);
		    
		//	*****************************************************************	
		    
			//creo comprobante pago 
		    
			//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
			ComprobantePago comprobantePago = new ComprobantePago();
			comprobantePago.setCmpaFichaMatricula(fichaMatricula);
			comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
			comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
			comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
			int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
			if(o==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
			}
		    em.persist(comprobantePago);
			
			//creo un detalle_matricula y un record_estudiante por materia 
			//TODO: falta poner el arancel y el valor (determinar como se calcula los valores dependiendo de el numero de horas de la materia) en el detalle_matricula 
			//int contadorAux = 0;
			StringBuilder sbError = new StringBuilder(); // en caso de error
			sbError.append("No existe cupos en las materias: "); // en caso de error
	
			
			if(listMaterias!=null){
			for (MateriaDto itemMaterias : listMaterias) {

				
				//BUSCO MALLA_CURRICULAR_MATERIA  POR EL ID DE MLCRMT QUE VIENE EN CADA MATERIA HOMOLOGADA
				MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
				
				//detalle matricula
				DetalleMatricula detalleMatricula = new DetalleMatricula();
				detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
				detalleMatricula.setDtmtComprobantePago(comprobantePago);
				detalleMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
				detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
				int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(r==0){
					//arancel nulo y seteo el valor parcial en cero
					detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
				}else{
					//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
				}
			   	em.persist(detalleMatricula);
					
				//record
				RecordEstudiante recordEstudiante = new RecordEstudiante();
				recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
				recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
				//No se coloca Homologado por pedido de cultura Fisica
				recordEstudiante.setRcesEstado(itemMaterias.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_APROBADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE); 
			 	em.persist(recordEstudiante);
					
				//registro de notas
				Calificacion calificacion = new Calificacion();
				calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
				calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
				calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
				calificacion.setClfPromedioAsistencia(itemMaterias.getClfPromedioAsistencia());
				calificacion.setRecordEstudiante(recordEstudiante);
			 	em.persist(calificacion);
			 }
			
			} 	
			
			
			//creo la gratuidad
			TipoGratuidad tipoGratuidad = new TipoGratuidad();
			//tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);	
			tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
			Gratuidad gratuidad = new Gratuidad();
			gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
			gratuidad.setGrtFichaEstudiante(fichaEstudiante);
			gratuidad.setGrtTipoGratuidad(tipoGratuidad);
			gratuidad.setGrtFichaMatricula(fichaMatricula);
		  	em.persist(gratuidad);
		  	
			//creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.FCIN_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaInscripcionAux.getFcinId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaEstudiante.getFcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaMatricula.getFcmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(comprobantePago.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.GRT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(gratuidad.getGrtId());
				
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("actualizar-");
			observacionAux.append(JdbcConstantes.FCIN_ID);
			observacionAux.append("-");
			observacionAux.append(fichaInscripcionAux.getFcinId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCES_ID);
			observacionAux.append("-");
			observacionAux.append(fichaEstudiante.getFcesId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCMT_ID);
			observacionAux.append("-");
			observacionAux.append(fichaMatricula.getFcmtId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.CMPA_ID);
			observacionAux.append("-");
			observacionAux.append(comprobantePago.getCmpaId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.GRT_ID);
			observacionAux.append("-");
			observacionAux.append(gratuidad.getGrtId());
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	em.persist(controlProceso);
		 
	
		  	
		session.getUserTransaction().commit();
	
			retorno = true;
	
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//	
		return retorno;
	}
	
	
	
	
	/**MQ
	 * Ubicacion Idiomas  de forma manual
	 * Modifica las entidades : ficha_estudiante, ficha_matricula, comprobante_pago, detalle_matricula, record_estudiante, gratuidad, ficha_inscripcion, paralelo,calificacion
	 * para generar la matricula de un estudiante 
	 * @param listMaterias - lista de materias en las que el estudiante se va a matricular
	 * @param personaDto - la persona que se va a matricular
	 * @param tipoMatricula - tipo de matricula que se genera dependiendo del cronograma
	 * @param tipoGratuidadId - id de la gratuidad que se tiene que generar en la matricula
	 * @param valorMatricula - valor de la matricula
	 * @param planificacionCronograma - entidad planificacion cronograma que permite relacionar en que tiempo se ejecuto la matricula
	 * @param archivoSubido- Archivo informe de autorización para ubicar manualmente a un estudiante
	 * @param fces - Ficha estudiante del estudiante a ubicar en el nivel
	 * @param usuarioRol - del usuario que realiza la ubicación para auditoria del proceso
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarMatriculaUbicacion(List<MateriaDto> listMaterias, PersonaDto personaDto, int tipoMatricula, int tipoGratuidadId
											,Integer valorMatricula , PlanificacionCronograma planificacionCronograma , String archivoSubido
											, FichaEstudiante fces, UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaDto.getPrsId());
			
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, personaDto.getFcinId());
			fichaInscripcionAux.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);//Modificar fcin a activa
			fichaInscripcionAux.setFcinVigente(FichaInscripcionConstantes.VIGENTE_VALUE);//Modificar fcin a activa
			fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE); // Modificar  fcin tipo a INSCRITO_SUFICIENCIAS
			fichaInscripcionAux.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_REINGRESO_VALUE);
			
				
			if(personaDto.getFcinEstadoIngreso()!=GeneralesConstantes.APP_ID_BASE){ //Homologado o No homologado
			fichaInscripcionAux.setFcinEstadoIngreso(personaDto.getFcinEstadoIngreso());
			}
					
			if(archivoSubido!=null){
			String extension = GeneralesUtilidades.obtenerExtension(archivoSubido);
			fichaInscripcionAux.setFcinDocumentoIngreso(personaDto.getPrsIdentificacion()+"-"+personaDto.getFcinId()+"."+extension);
			}
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
			
			if(fces==null){ //NO EXISTE FICHAS ESTUDIANTE EN LA CARRERA
        	    //creacion del objeto Ficha Estudiante
			
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
														
				em.persist(fichaEstudiante);
			
			}else{ // YA EXISTE UNA FICHA ESTUDIANTE, USO POSIBLE, EXISTE CONTROL EN VERIFICAR CLICK
						
				fichaEstudiante= em.find(FichaEstudiante.class, fces.getFcesId()); // buscamos la ficha estudiante existente para editar
								     				
			}
		
			//creacion de la ficha matricula
			FichaMatricula fichaMatricula = new FichaMatricula();
			fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
			//Verificar nivel
			fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.PRIMER_NIVEL_VALUE);
			fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			fichaMatricula.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
			if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
			}else{
				fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
			}
			
			fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
			fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
			fichaMatricula.setFcmtPracId(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
			ConfiguracionCarrera configuracionCarrera=	em.find(ConfiguracionCarrera.class, fichaInscripcionAux.getFcinConfiguracionCarrera().getCncrId());
			
			if(configuracionCarrera.getCncrModalidad()!=null){
			fichaMatricula.setFcmtModalidad(configuracionCarrera.getCncrModalidad().getMdlId());
			}
						
		    em.persist(fichaMatricula);
				
			//creo comprobante pago 
			//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
			ComprobantePago comprobantePago = new ComprobantePago();
			comprobantePago.setCmpaFichaMatricula(fichaMatricula);
			comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
			comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
			comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
			int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
			if(o==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
			}
		    em.persist(comprobantePago);
			
			//creo un detalle_matricula y un record_estudiante por materia 
				
			if(listMaterias!=null){
	
			for (MateriaDto itemMaterias : listMaterias) {

				
				//BUSCO MALLA_CURRICULAR_MATERIA  POR EL ID DE MLCRMT QUE VIENE EN CADA MATERIA HOMOLOGADA
				MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
				
				//detalle matricula
				DetalleMatricula detalleMatricula = new DetalleMatricula();
				detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
				detalleMatricula.setDtmtComprobantePago(comprobantePago);
				detalleMatricula.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); // Si existe materia se aprueba con primera en Idiomas
				detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
				int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(r==0){
					//arancel nulo y seteo el valor parcial en cero
					detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
				}else{
					//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
				}
			   	em.persist(detalleMatricula);
					
				//record
				RecordEstudiante recordEstudiante = new RecordEstudiante();
				recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
				recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
				recordEstudiante.setRcesEstado(itemMaterias.getEstadoHomologacion());//estado aprobado en caso de Idiomas
			 	em.persist(recordEstudiante);
					
				//registro de notas
//				Calificacion calificacion = new Calificacion();
//				calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
//				calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
//				calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
//				calificacion.setRecordEstudiante(recordEstudiante);
//			 	em.persist(calificacion);
			}
		}		
			//creo la gratuidad
			TipoGratuidad tipoGratuidad = new TipoGratuidad();
			//tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);
			
			tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
			
			Gratuidad gratuidad = new Gratuidad();
			gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
			gratuidad.setGrtFichaEstudiante(fichaEstudiante);
			gratuidad.setGrtTipoGratuidad(tipoGratuidad);
			gratuidad.setGrtFichaMatricula(fichaMatricula);
		  	em.persist(gratuidad);
		  	
		  	//creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.FCIN_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaInscripcionAux.getFcinId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaEstudiante.getFcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaMatricula.getFcmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(comprobantePago.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.GRT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(gratuidad.getGrtId());
				
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("actualizar-");
			observacionAux.append(JdbcConstantes.FCIN_ID);
			observacionAux.append("-");
			observacionAux.append(fichaInscripcionAux.getFcinId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCES_ID);
			observacionAux.append("-");
			observacionAux.append(fichaEstudiante.getFcesId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCMT_ID);
			observacionAux.append("-");
			observacionAux.append(fichaMatricula.getFcmtId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.CMPA_ID);
			observacionAux.append("-");
			observacionAux.append(comprobantePago.getCmpaId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.GRT_ID);
			observacionAux.append("-");
			observacionAux.append(gratuidad.getGrtId());
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	em.persist(controlProceso);
		     
			session.getUserTransaction().commit();
		 	
			retorno = true;
			// TODO: cambiar para que el metodo no maneje la transaccion sino el contenedor
		
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//	
		return retorno;
	}
	
	
	
	/**MQ
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
											,Integer valorMatricula , PlanificacionCronograma planificacionCronogramaPosgrado , String archivoSubido
											, FichaEstudiante fces, UsuarioRol usuarioRol , boolean crearNuevaMatricula, List<MateriaDto> listaMateriaNivelPosgrado, Integer nivelActualPosgrado) throws MatriculaValidacionException, MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			//busco la persona
			Persona personaAux = em.find(Persona.class, personaDto.getPrsId());
			
			//busco la ficha Inscripcion
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, personaDto.getFcinId());
			fichaInscripcionAux.setFcinEstado(FichaInscripcionConstantes.ACTIVO_VALUE);//Modificar ficha inscripcion
			
			fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE);
			PeriodoAcademico periodoPosgradoAux= new PeriodoAcademico();
			periodoPosgradoAux= em.find(PeriodoAcademico.class, personaDto.getFcinPeriodoPosgradoId());
			
			fichaInscripcionAux.setFcinPeriodoAcademico(periodoPosgradoAux);
			fichaInscripcionAux.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_REINGRESO_VALUE);
								
			if(personaDto.getFcinEstadoIngreso()!=GeneralesConstantes.APP_ID_BASE){
			fichaInscripcionAux.setFcinEstadoIngreso(personaDto.getFcinEstadoIngreso());
			}
			
			if(archivoSubido!=null){
			String extension = GeneralesUtilidades.obtenerExtension(archivoSubido);
			fichaInscripcionAux.setFcinDocumentoIngreso(personaDto.getPrsIdentificacion()+"-"+personaDto.getFcinId()+"."+extension);
			}
			
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
			
			if(fces==null){ //NO EXISTE FICHAS ESTUDIANTE EN LA CARRERA
        	    //creacion del objeto Ficha Estudiante
				fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
				fichaEstudiante.setFcesPersona(personaAux);
				fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
				em.persist(fichaEstudiante);
			}
		
			//creacion de la ficha matricula
			FichaMatricula fichaMatricula = new FichaMatricula();
			fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
			//Verificar nivel EN POSGRADOS
			if(nivelActualPosgrado==FichaMatriculaConstantes.PRIMER_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_PRIMER_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.SEGUNDO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_SEGUNDO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.TERCERO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_TERCER_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.CUARTO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_CUARTO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.QUINTO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_QUINTO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.SEXTO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_SEXTO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.SEPTIMO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_SEPTIMO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.OCTAVO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_OCTAVO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.NOVENO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_NOVENO_NIVEL_VALUE);
			}else if(nivelActualPosgrado==FichaMatriculaConstantes.DECIMO_NIVEL_VALUE){
				fichaMatricula.setFcmtNivelUbicacion(FichaMatriculaConstantes.POSGRADO_DECIMO_NIVEL_VALUE);
			}
			
			fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			fichaMatricula.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO
            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
			if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
				fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
			}else{
				fichaMatricula.setFcmtValorTotal(new BigDecimal(valorMatricula));
			}
			
			fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
			fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronogramaPosgrado);
		    //FICHA MATRICULA EN PERIODO SELECCIONADO AL HOMOLOGAR
			fichaMatricula.setFcmtPracId(periodoPosgradoAux.getPracId());
		
			ConfiguracionCarrera configuracionCarrera=	em.find(ConfiguracionCarrera.class, fichaInscripcionAux.getFcinConfiguracionCarrera().getCncrId());
			if(configuracionCarrera.getCncrModalidad()!=null){
			fichaMatricula.setFcmtModalidad(configuracionCarrera.getCncrModalidad().getMdlId());
			}
			
		    em.persist(fichaMatricula);
				
			//creo comprobante pago 
			//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
			ComprobantePago comprobantePago = new ComprobantePago();
			comprobantePago.setCmpaFichaMatricula(fichaMatricula);
			comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
			comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
			comprobantePago.setCmpaTipo(tipoMatricula); //según el tipo de matricula
			int o = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
			if(o==0){
				comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
			}
		    em.persist(comprobantePago);
			
			//creo un detalle_matricula y un record_estudiante por materia 
					
			if(listMaterias!=null){
	
			for (MateriaDto itemMaterias : listMaterias) {
				
				//BUSCO MALLA_CURRICULAR_MATERIA  POR EL ID DE MLCRMT QUE VIENE EN CADA MATERIA HOMOLOGADA
				MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
				
				//detalle matricula
				DetalleMatricula detalleMatricula = new DetalleMatricula();
				detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
				detalleMatricula.setDtmtComprobantePago(comprobantePago);
				detalleMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
				detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
				int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(r==0){
					//arancel nulo y seteo el valor parcial en cero
					detalleMatricula.setDtmtValorParcial(BigDecimal.ZERO);
				}else{
					//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
				}
			   	em.persist(detalleMatricula);
					
				//record
				RecordEstudiante recordEstudiante = new RecordEstudiante();
				recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
				recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
				recordEstudiante.setRcesEstado(itemMaterias.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_APROBADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				if(itemMaterias.getRcesModoAprobacionNota1()!= GeneralesConstantes.APP_ID_BASE){
				recordEstudiante.setRcesModoAprobacion(itemMaterias.getRcesModoAprobacionNota1());	
				}
			 	em.persist(recordEstudiante);
					
				//registro de notas
				Calificacion calificacion = new Calificacion();
				calificacion.setClfNota1(new Float(itemMaterias.getNotaSuma().floatValue()));
				calificacion.setClfNota2(new Float(itemMaterias.getNotaSuma().floatValue()));
				calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
				calificacion.setClfPromedioAsistencia(itemMaterias.getClfPromedioAsistencia());
				calificacion.setRecordEstudiante(recordEstudiante);
			 	em.persist(calificacion);
			 	
			 	if(itemMaterias.getEsHomologadoReg2()){
			 		
			 		//detalle matricula
					DetalleMatricula detalleMatriculaReg2 = new DetalleMatricula();
					detalleMatriculaReg2.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detalleMatriculaReg2.setDtmtComprobantePago(comprobantePago);
					detalleMatriculaReg2.setDtmtNumero(itemMaterias.getNumMatriculaReg2()); // calculado en el mb
					detalleMatriculaReg2.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detalleMatriculaReg2.setDtmtValorPorMateria(BigDecimal.ZERO);
					int r2 = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(r2==0){
						//arancel nulo y seteo el valor parcial en cero
						detalleMatriculaReg2.setDtmtValorParcial(BigDecimal.ZERO);
					}else{
						//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
					}
				   	em.persist(detalleMatriculaReg2);
						
					//record
					RecordEstudiante recordEstudianteReg2 = new RecordEstudiante();
					recordEstudianteReg2.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudianteReg2.setRcesFichaEstudiante(fichaEstudiante);
					recordEstudianteReg2.setRcesObservacion(itemMaterias.getRcesObservacion());
					recordEstudianteReg2.setRcesEstado(itemMaterias.getAprobadoReg2()==true ? RecordEstudianteConstantes.ESTADO_APROBADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					if(itemMaterias.getRcesModoAprobacionNota2()!= GeneralesConstantes.APP_ID_BASE){
						recordEstudianteReg2.setRcesModoAprobacion(itemMaterias.getRcesModoAprobacionNota2());	
					}
				 	em.persist(recordEstudianteReg2);
						
					//registro de notas
					Calificacion calificacionReg2 = new Calificacion();
					calificacionReg2.setClfNota1(new Float(itemMaterias.getNotaSumaReg2().floatValue()));
					calificacionReg2.setClfNota2(new Float(itemMaterias.getNotaSumaReg2().floatValue()));
					calificacionReg2.setClfNotaFinalSemestre(itemMaterias.getNotaSumaReg2().floatValue());
					calificacionReg2.setClfPromedioAsistencia(itemMaterias.getClfPromedioAsistenciaReg2());
					calificacionReg2.setRecordEstudiante(recordEstudianteReg2);
				 	em.persist(calificacionReg2);
			 		
			 	}
			 	
			}
		}		
			//creo la gratuidad
			TipoGratuidad tipoGratuidad = new TipoGratuidad();
			//tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);		
			
			tipoGratuidad = em.find(TipoGratuidad.class, TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
			Gratuidad gratuidad = new Gratuidad();
			gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
			gratuidad.setGrtFichaEstudiante(fichaEstudiante);
			gratuidad.setGrtTipoGratuidad(tipoGratuidad);
			gratuidad.setGrtFichaMatricula(fichaMatricula);
		  	em.persist(gratuidad);
		  	
		  //creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.FCIN_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaInscripcionAux.getFcinId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaEstudiante.getFcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaMatricula.getFcmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(comprobantePago.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.GRT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(gratuidad.getGrtId());
				
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("actualizar-");
			observacionAux.append(JdbcConstantes.FCIN_ID);
			observacionAux.append("-");
			observacionAux.append(fichaInscripcionAux.getFcinId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCES_ID);
			observacionAux.append("-");
			observacionAux.append(fichaEstudiante.getFcesId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.FCMT_ID);
			observacionAux.append("-");
			observacionAux.append(fichaMatricula.getFcmtId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.CMPA_ID);
			observacionAux.append("-");
			observacionAux.append(comprobantePago.getCmpaId());
			observacionAux.append("; crear-");
			observacionAux.append(JdbcConstantes.GRT_ID);
			observacionAux.append("-");
			observacionAux.append(gratuidad.getGrtId());
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	em.persist(controlProceso);
		  	
		  	if(crearNuevaMatricula){//OPCION1: Si se debe crear siguiente matricula en nivel siguiente Con la misma Ficha Matricula
	  		
	  		if((listaMateriaNivelPosgrado!=null)&&(listaMateriaNivelPosgrado.size()>0)){// si aún existen materias en el siguiente nivel
	  			
	  	
				//creo un detalle_matricula con el mismo comprobante de pago y un record_estudiante por materia  con la misma fces
						
				for (MateriaDto itemMaterias2 : listaMateriaNivelPosgrado) {
					
					//BUSCO MALLA_CURRICULAR_PARALELO POR EL ID DE MLCRPR QUE VIENE EN CADA MATERIA PARA LA MATRICULA NUEVA
					MallaCurricularParalelo mallaCurricularParaleloNueva = em.find(MallaCurricularParalelo.class, itemMaterias2.getMlcrprId());
					
					//detalle matricula
					DetalleMatricula detalleMatriculaNueva = new DetalleMatricula();
					detalleMatriculaNueva.setDtmtMallaCurricularParalelo(mallaCurricularParaleloNueva);
					detalleMatriculaNueva.setDtmtComprobantePago(comprobantePago);
					detalleMatriculaNueva.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); // calculado en el mb
					detalleMatriculaNueva.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detalleMatriculaNueva.setDtmtValorPorMateria(BigDecimal.ZERO);
					int r = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
					if(r==0){
						//arancel nulo y seteo el valor parcial en cero
						detalleMatriculaNueva.setDtmtValorParcial(BigDecimal.ZERO);
					}else{
						//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
					}
				   	em.persist(detalleMatriculaNueva);
						
					//record
					RecordEstudiante recordEstudianteNuevo = new RecordEstudiante();
					recordEstudianteNuevo.setRcesMallaCurricularParalelo(mallaCurricularParaleloNueva);
					recordEstudianteNuevo.setRcesFichaEstudiante(fichaEstudiante);
					recordEstudianteNuevo.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
				 	em.persist(recordEstudianteNuevo);

				}
										  	
			 
	  		}
	  		
	  	}
		  	
//		  OPCION  2:
//		  	if(crearNuevaMatricula){//Si se debe crear siguiente matricula en nivel siguiente  OTRA MATRICULA
//		  		
//		  		if((listaMateriaNivelPosgrado!=null)&&(listaMateriaNivelPosgrado.size()>0)){// si aún existen materias en el siguiente nivel
//		  			
//		  		//creacion de la ficha matricula
//					FichaMatricula fichaMatriculaNueva = new FichaMatricula();
//					fichaMatriculaNueva.setFcmtFichaEstudiante(fichaEstudiante);
//					//Verificar nivel
//					fichaMatriculaNueva.setFcmtNivelUbicacion(nivelActualPosgrado+1); //Siguiente nivel
//					fichaMatriculaNueva.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
//					fichaMatriculaNueva.setFcmtTipo(tipoMatricula); // calculo según el cronograma  TODO: FALTA CALCULAR EN EL MANAGED BEAN ESTO : 0-Matricula Ordinaria
//		            //fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad()); // proviene de la configuracion carrera seleccionada
//					if(valorMatricula == GeneralesConstantes.APP_ID_BASE){
//						fichaMatriculaNueva.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
//					}else{
//						fichaMatriculaNueva.setFcmtValorTotal(new BigDecimal(valorMatricula));
//					}
//					
//					fichaMatriculaNueva.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
//					fichaMatriculaNueva.setFcmtPlanificacionCronograma(planificacionCronogramaPosgrado); 
//					
//				    //FICHA MATRICULA EN PERIODO SELECCIONADO AL HOMOLOGAR
//					fichaMatriculaNueva.setFcmtPracId(periodoPosgradoAux.getPracId());
//				
//					ConfiguracionCarrera configuracionCarrera2=	em.find(ConfiguracionCarrera.class, fichaInscripcionAux.getFcinConfiguracionCarrera().getCncrId());
//					if(configuracionCarrera2.getCncrModalidad()!=null){
//						fichaMatriculaNueva.setFcmtModalidad(configuracionCarrera2.getCncrModalidad().getMdlId());
//					}
//					
//				    em.persist(fichaMatriculaNueva);
//						
//					//creo comprobante pago 
//					//TODO: FALTA ASIGNAR EL CODIGO DEL COMPROBATE DE PAGO y el valor total (no se xq tiene un valor aqui y en la matricula) y fecha de caducidad
//					ComprobantePago comprobantePagoNuevo = new ComprobantePago();
//					comprobantePagoNuevo.setCmpaFichaMatricula(fichaMatriculaNueva);
//					comprobantePagoNuevo.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
//					comprobantePagoNuevo.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
//					comprobantePagoNuevo.setCmpaTipo(tipoMatricula); //según el tipo de matricula:  0- Matricula ordinaria
//					int o2 = fichaMatriculaNueva.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
//					if(o2==0){
//						comprobantePagoNuevo.setCmpaFechaPago(new Timestamp(new Date().getTime()));
//					}
//				    em.persist(comprobantePagoNuevo);
//					
//					//creo un detalle_matricula y un record_estudiante por materia 
//							
//					for (MateriaDto itemMaterias2 : listaMateriaNivelPosgrado) {
//						
//						//BUSCO MALLA_CURRICULAR_PARALELO POR EL ID DE MLCRPR QUE VIENE EN CADA MATERIA PARA LA MATRICULA NUEVA
//						MallaCurricularParalelo mallaCurricularParaleloNueva = em.find(MallaCurricularParalelo.class, itemMaterias2.getMlcrprId());
//						
//						
//						//detalle matricula
//						DetalleMatricula detalleMatriculaNueva = new DetalleMatricula();
//						detalleMatriculaNueva.setDtmtMallaCurricularParalelo(mallaCurricularParaleloNueva);
//						detalleMatriculaNueva.setDtmtComprobantePago(comprobantePagoNuevo);
//						detalleMatriculaNueva.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); // calculado en el mb
//						detalleMatriculaNueva.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
//						int r = fichaMatriculaNueva.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
//						if(r==0){
//							//arancel nulo y seteo el valor parcial en cero
//							detalleMatriculaNueva.setDtmtValorParcial(BigDecimal.ZERO);
//						}else{
//							//TODO: FALTA COMPLETAR EL CASO CUANDO TIENEN ARANCEL
//						}
//					   	em.persist(detalleMatriculaNueva);
//							
//						//record
//						RecordEstudiante recordEstudianteNuevo = new RecordEstudiante();
//						recordEstudianteNuevo.setRcesMallaCurricularParalelo(mallaCurricularParaleloNueva);
//						recordEstudianteNuevo.setRcesFichaEstudiante(fichaEstudiante);
//						recordEstudianteNuevo.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
//					 	em.persist(recordEstudianteNuevo);
//
//					}
//					
//					//creo la gratuidad
//					TipoGratuidad tipoGratuidadNueva = new TipoGratuidad();
//					tipoGratuidadNueva = em.find(TipoGratuidad.class, tipoGratuidadId);		
//					Gratuidad gratuidadNueva = new Gratuidad();
//					gratuidadNueva.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
//					gratuidadNueva.setGrtFichaEstudiante(fichaEstudiante);
//					gratuidadNueva.setGrtTipoGratuidad(tipoGratuidadNueva);
//					gratuidadNueva.setGrtFichaMatricula(fichaMatriculaNueva);
//				  	em.persist(gratuidadNueva);
//				  	
//				  //creo Control del proceso
//				  	ControlProceso controlProcesoNuevo = new ControlProceso();
//				  	UsuarioRol usuarioRolAux2= em.find(UsuarioRol.class, usuarioRol.getUsroId());
//				  	TipoProceso tipoProcesoAux2= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
//				  	controlProcesoNuevo.setCnprUsuarioRol(usuarioRolAux2);
//				  	controlProcesoNuevo.setCnprTipoProceso(tipoProcesoAux2);
//				  	controlProcesoNuevo.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
//				  	controlProcesoNuevo.setCnprFechaAccion(new Timestamp(new Date().getTime()));
//				  	StringBuilder detalleAux2= new StringBuilder();
//				  	
//				  	detalleAux2.append(JdbcConstantes.FCMT_ID);
//				  	detalleAux2.append(":");
//				  	detalleAux2.append(fichaMatriculaNueva.getFcmtId());
//				  	detalleAux2.append(";");
//				  	detalleAux2.append(JdbcConstantes.CMPA_ID);
//				  	detalleAux2.append(":");
//				  	detalleAux2.append(comprobantePagoNuevo.getCmpaId());
//				  	detalleAux2.append(";");
//				  	detalleAux2.append(JdbcConstantes.GRT_ID);
//				  	detalleAux2.append(":");
//				  	detalleAux2.append(gratuidadNueva.getGrtId());
//						
//				  	controlProcesoNuevo.setCnprDetalleProceso(detalleAux2.toString());
//				  	
//					StringBuilder observacionAux2= new StringBuilder();
//				
//					observacionAux2.append("crear-");
//					observacionAux2.append(JdbcConstantes.FCMT_ID);
//					observacionAux2.append("-");
//					observacionAux2.append(fichaMatriculaNueva.getFcmtId());
//					observacionAux2.append("; crear-");
//					observacionAux2.append(JdbcConstantes.CMPA_ID);
//					observacionAux2.append("-");
//					observacionAux2.append(comprobantePagoNuevo.getCmpaId());
//					observacionAux2.append("; crear-");
//					observacionAux2.append(JdbcConstantes.GRT_ID);
//					observacionAux2.append("-");
//					observacionAux2.append(gratuidadNueva.getGrtId());
//					controlProcesoNuevo.setCnprObservacionAccion(observacionAux2.toString());
//				  	em.persist(controlProcesoNuevo);
//		  		}
//		  		
//		  	}
		  	
		 	session.getUserTransaction().commit();
			
		retorno = true;
		
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//	
		return retorno;
	}


	/**
	 * Genera matricula de pregrado con aranceles, gratuidadt, etc. 
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @throws MatriculaValidacionException
	 * @throws MatriculaException
	 */
	@SuppressWarnings("unchecked")
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
												
			) throws MatriculaValidacionException, MatriculaException{
		
//		Boolean retorno = false;
		
		ComprobantePago retorno = null;
		
		try {
			session.getUserTransaction().begin();
			
			Persona personaAux = em.find(Persona.class, fichaInscripcion.getPrsId());
			
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fichaInscripcion.getFcinId());
			
			if(valorMatricula.equals(BigDecimal.ZERO)){
				fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);
			}else{
				fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
			}
			
			FichaEstudiante fichaEstudiante = null;
			// Estudiante nuevo
			if(estudianteNuevo){
				
				fichaEstudiante = em.find(FichaEstudiante.class, fichaInscripcion.getFcesId());
				if (fichaEstudiante == null) {
					//creacion del objeto Ficha Estudiante
					fichaEstudiante = new FichaEstudiante();
					fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
					fichaEstudiante.setFcesPersona(personaAux);
					fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
					em.persist(fichaEstudiante);	
				}
				
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				
				fichaMatricula.setFcmtTipo(obtenerTipoMatricula(procesoFlujo.getPrlfId())); 
				
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad());
				fichaMatricula.setFcmtPracId(pracId.getPracId());
				
				if(valorMatricula == new BigDecimal(GeneralesConstantes.APP_ID_BASE)){
					fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
				}else{
					fichaMatricula.setFcmtValorTotal(valorMatricula);
				}
				
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE); 
				int i = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(i==0){
					comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
				}
				em.persist(comprobantePago);
				retorno = comprobantePago;
				
				for (MateriaDto itemMaterias : listMaterias) {
					
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMaterias.getMlcrmtId(), itemMaterias.getPrlId());
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprAux.getMlcrprId());
					if(mallaCurricularParalelo.getMlcrprInscritos() == null){
						mallaCurricularParalelo.setMlcrprInscritos(0);
					}
					if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){ // si existen cupos 
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
					}else{
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}
					em.merge(mallaCurricularParalelo);
					
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtValorPorMateria(itemMaterias.getValorMatricula());
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					em.persist(detallaMatricula);
					
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					if(valorMatricula.equals(BigDecimal.ZERO)){
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					}else{
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
					}
					em.persist(recordEstudiante);
				}
				
				// Gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);	
				
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
				
			// Estudiante Existente
			}else{  
				
				fichaEstudiante = em.find(FichaEstudiante.class, fichaInscripcion.getFcesId());
				
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(obtenerTipoMatricula(procesoFlujo.getPrlfId()));
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad());
				fichaMatricula.setFcmtValorTotal(valorMatricula); 
				fichaMatricula.setFcmtPracId(pracId.getPracId());
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				
				ComprobantePago comprobantePago = null;
				
				Carrera carrera = new Carrera();
				carrera = em.find(Carrera.class,fichaInscripcion.getCrrId());
				StringBuilder sbComprobanteAux = new StringBuilder();
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnUej());
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnCodSori());
				 
				//Comprobante pago 
				comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaTipo(obtenerTipoMatricula(procesoFlujo.getPrlfId()));

				// Detalle de fechas de comprobante
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Timestamp((new Date()).getTime()));
				comprobantePago.setCmpaFechaEmision(new Timestamp(cal.getTime().getTime()));
				cal.add(Calendar.DAY_OF_WEEK, 4);
				Timestamp ts = new Timestamp(cal.getTime().getTime());


				if(valorMatricula.equals(BigDecimal.ZERO)){
					comprobantePago.setCmpaFechaCaduca(null);
				}else{
					comprobantePago.setCmpaFechaCaduca(ts);
				} 

				comprobantePago.setCmpaDescripcion(ComprobantePagoConstantes.DESCRIPCION_MATRICULA_PREGRADO+" "+carrera.getCrrDependencia().getDpnDescripcion());
				
				comprobantePago.setCmpaNumComprobante(null);
				comprobantePago.setCmpaTotalPago(valorMatricula); 
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EMITIDO_PREGRADO_VALUE);
				comprobantePago.setCmpaValorPagado(valorMatricula);
				comprobantePago.setCmpaIdArancel(null);

				if(procesoFlujo.getPrlfId() == 2){
					comprobantePago.setCmpaMatrTipo(ComprobantePagoConstantes.MATRICULA_TIPO_ORDINARIA_COMPROBANTE_CARGA_CSV_VALUE);
				}
				if(procesoFlujo.getPrlfId() == 3){
					comprobantePago.setCmpaMatrTipo(ComprobantePagoConstantes.MATRICULA_TIPO_EXTRAORDINARIA_COMPROBANTE_CARGA_CSV_VALUE);
				} 

				comprobantePago.setCmpaAplicaGratuidad(ComprobantePagoConstantes.INDEPENDIENTE_GRATUIDAD_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaCantidad(ComprobantePagoConstantes.CANTIDAD_MATRICULA_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaTipoUnidad(ComprobantePagoConstantes.TIPO_UNIDAD_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaProcSau(ComprobantePagoConstantes.PROC_SAU_INSCRIPCION_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaTotalFacultad(ComprobantePagoConstantes.TOTAL_FACULTAD_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaModalidad(fichaInscripcion.getCncrModalidad());
				comprobantePago.setCmpaPaiCodigo(ComprobantePagoConstantes.ECUADOR_VALUE_COMPROBANTE_CARGA_CSV_VALUE);

				comprobantePago.setCmpaEspeCodigo(carrera.getCrrEspeCodigo());
				em.persist(comprobantePago); 

				
				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
				for (MateriaDto itemMaterias : listMaterias) {
					
					try {
						mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMaterias.getMlcrmtId(), itemMaterias.getPrlId());
					} catch (Exception e) {
						session.getUserTransaction().rollback();
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}
					
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprAux.getMlcrprId());
					
					if(mallaCurricularParalelo.getMlcrprInscritos()==null){
						mallaCurricularParalelo.setMlcrprInscritos(0);
					}
					if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){  
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1);
					}
					em.merge(mallaCurricularParalelo);
					
					retorno = comprobantePago;
					
					// Detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtValorPorMateria(itemMaterias.getValorMatricula());
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula());
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					em.persist(detallaMatricula);
					
					// Record Estudiante   
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					if(valorMatricula.equals(BigDecimal.ZERO)){
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					}else{
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
					}
					
					em.persist(recordEstudiante);
				}
				
				//Tipo Gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);		
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
			}
			
			if(retorno == null){
				session.getUserTransaction().rollback();
			}else {
				List<FichaMatricula> matriculas = new ArrayList<>();
						
				try {
					Query q = em.createNamedQuery("FichaMatricula.findPorFcesIdPracId");
					q.setParameter("fcesId", fichaEstudiante.getFcesId());
					q.setParameter("periodoId", pracId.getPracId());
					matriculas = q.getResultList();
					if (matriculas.size()>1) {
						session.getUserTransaction().rollback();
					}else {
						session.getUserTransaction().commit();	
					}
				} catch (Exception e) {
					session.getUserTransaction().rollback();	
				}
			}
			
		} catch (MatriculaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new MatriculaValidacionException(e.getMessage());
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Genera matricula de pregrado con aranceles, gratuidadt, etc. 
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @throws MatriculaValidacionException
	 * @throws MatriculaException
	 */
	@SuppressWarnings("unchecked")
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
					
			) throws MatriculaValidacionException, MatriculaException{

		
		
		ComprobantePago retorno = null;
		
		try {
			session.getUserTransaction().begin();
			
			Persona personaAux = em.find(Persona.class, fichaInscripcion.getPrsId());
			
			FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fichaInscripcion.getFcinId());
			
			if(valorMatricula.equals(BigDecimal.ZERO)){
				fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.SI_MATRICULADO_VALUE);
			}else{
				fichaInscripcionAux.setFcinMatriculado(FichaInscripcionConstantes.NO_MATRICULADO_VALUE);
			}
			
			FichaEstudiante fichaEstudiante = null;
			// Estudiante nuevo
			if(estudianteNuevo){
				fichaEstudiante = em.find(FichaEstudiante.class, fichaInscripcion.getFcesId());

				if (fichaEstudiante == null) {
					//creacion del objeto Ficha Estudiante
					fichaEstudiante = new FichaEstudiante();
					fichaEstudiante.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
					fichaEstudiante.setFcesPersona(personaAux);
					fichaEstudiante.setFcesFichaInscripcion(fichaInscripcionAux);
					em.persist(fichaEstudiante);	
				}
				
				//creacion de la ficha matricula
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				
				fichaMatricula.setFcmtTipo(obtenerTipoMatriculaSuficiencias(procesoFlujo.getPrlfId())); 
				
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad());
				fichaMatricula.setFcmtPracId(pracId.getPracId());
				
				if(valorMatricula == new BigDecimal(GeneralesConstantes.APP_ID_BASE)){
					fichaMatricula.setFcmtValorTotal(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE)); 
				}else{
					fichaMatricula.setFcmtValorTotal(valorMatricula);
				}
				
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				
				ComprobantePago comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE);
				comprobantePago.setCmpaFechaEmision(new Timestamp(new Date().getTime()));
				comprobantePago.setCmpaTipo(ComprobantePagoConstantes.ESTADO_ACTIVO_VALUE); 
				int i = fichaMatricula.getFcmtValorTotal().compareTo(new BigDecimal(GeneralesConstantes.APP_CERO_VALUE));
				if(i==0){
					comprobantePago.setCmpaFechaPago(new Timestamp(new Date().getTime()));
				}
				em.persist(comprobantePago);
				retorno = comprobantePago;
				
				for (MateriaDto itemMaterias : listMaterias) {
					
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMaterias.getMlcrmtId(), itemMaterias.getPrlId());
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprAux.getMlcrprId());
					if(mallaCurricularParalelo.getMlcrprInscritos() == null){
						mallaCurricularParalelo.setMlcrprInscritos(0);
					}
					if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){ // si existen cupos 
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1); // agrego un cupo en la materia
					}else{
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}
					em.merge(mallaCurricularParalelo);
					
					//detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtValorPorMateria(itemMaterias.getValorMatricula());
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					em.persist(detallaMatricula);
					
					//record
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					if(valorMatricula.equals(BigDecimal.ZERO)){
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					}else{
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
					}
					em.persist(recordEstudiante);
				}
				
				// Gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);	
				
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
				
			// Estudiante Existente
			}else{  
				
				fichaEstudiante = em.find(FichaEstudiante.class, fichaInscripcion.getFcesId());
				
				FichaMatricula fichaMatricula = new FichaMatricula();
				fichaMatricula.setFcmtFichaEstudiante(fichaEstudiante);
				fichaMatricula.setFcmtNivelUbicacion(nivelUbicacion);
				fichaMatricula.setFcmtEstado(FichaMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				fichaMatricula.setFcmtTipo(obtenerTipoMatriculaSuficiencias(procesoFlujo.getPrlfId()));
				fichaMatricula.setFcmtModalidad(fichaInscripcion.getCncrModalidad());
				fichaMatricula.setFcmtValorTotal(valorMatricula); 
				fichaMatricula.setFcmtPracId(pracId.getPracId());
				fichaMatricula.setFcmtFechaMatricula(new Timestamp(new Date().getTime()));
				fichaMatricula.setFcmtPlanificacionCronograma(planificacionCronograma);
				em.persist(fichaMatricula);
				
				
				ComprobantePago comprobantePago = null;
				
				Carrera carrera = new Carrera();
				carrera = em.find(Carrera.class,fichaInscripcion.getCrrId());
				StringBuilder sbComprobanteAux = new StringBuilder();
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnUej());
				sbComprobanteAux.append(carrera.getCrrDependencia().getDpnCodSori());
				 
				//Comprobante pago 
				comprobantePago = new ComprobantePago();
				comprobantePago.setCmpaFichaMatricula(fichaMatricula);
				comprobantePago.setCmpaTipo(obtenerTipoMatriculaSuficiencias(procesoFlujo.getPrlfId()));

				// Detalle de fechas de comprobante
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Timestamp((new Date()).getTime()));
				comprobantePago.setCmpaFechaEmision(new Timestamp(cal.getTime().getTime()));
				cal.add(Calendar.DAY_OF_WEEK, 4);
				Timestamp ts = new Timestamp(cal.getTime().getTime());


				if(valorMatricula.equals(BigDecimal.ZERO)){
					comprobantePago.setCmpaFechaCaduca(null);
				}else{
					comprobantePago.setCmpaFechaCaduca(ts);
				} 

				comprobantePago.setCmpaDescripcion(ComprobantePagoConstantes.DESCRIPCION_MATRICULA_PREGRADO+" "+carrera.getCrrDependencia().getDpnDescripcion());
				
				comprobantePago.setCmpaNumComprobante(null);
				comprobantePago.setCmpaTotalPago(valorMatricula); 
				comprobantePago.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EMITIDO_PREGRADO_VALUE);
				comprobantePago.setCmpaValorPagado(valorMatricula);
				comprobantePago.setCmpaIdArancel(null);

				if(procesoFlujo.getPrlfId() == 2){
					comprobantePago.setCmpaMatrTipo(ComprobantePagoConstantes.MATRICULA_TIPO_ORDINARIA_COMPROBANTE_CARGA_CSV_VALUE);
				}
				if(procesoFlujo.getPrlfId() == 3){
					comprobantePago.setCmpaMatrTipo(ComprobantePagoConstantes.MATRICULA_TIPO_EXTRAORDINARIA_COMPROBANTE_CARGA_CSV_VALUE);
				} 

				comprobantePago.setCmpaAplicaGratuidad(ComprobantePagoConstantes.INDEPENDIENTE_GRATUIDAD_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaCantidad(ComprobantePagoConstantes.CANTIDAD_MATRICULA_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaTipoUnidad(ComprobantePagoConstantes.TIPO_UNIDAD_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaProcSau(ComprobantePagoConstantes.PROC_SAU_INSCRIPCION_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaTotalFacultad(ComprobantePagoConstantes.TOTAL_FACULTAD_COMPROBANTE_CARGA_CSV_VALUE);
				comprobantePago.setCmpaModalidad(fichaInscripcion.getCncrModalidad());
				comprobantePago.setCmpaPaiCodigo(ComprobantePagoConstantes.ECUADOR_VALUE_COMPROBANTE_CARGA_CSV_VALUE);

				comprobantePago.setCmpaEspeCodigo(carrera.getCrrEspeCodigo());
				em.persist(comprobantePago); 

				
				MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
				for (MateriaDto itemMaterias : listMaterias) {
					
					try {
						mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMaterias.getMlcrmtId(), itemMaterias.getPrlId());
					} catch (Exception e) {
						session.getUserTransaction().rollback();
						throw new MatriculaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.generarMatricula.general.validacion.exception")));
					}
					
					MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, mlcrprAux.getMlcrprId());
					
					if(mallaCurricularParalelo.getMlcrprInscritos()==null){
						mallaCurricularParalelo.setMlcrprInscritos(0);
					}
					if(mallaCurricularParalelo.getMlcrprInscritos() < mallaCurricularParalelo.getMlcrprCupo()){  
						mallaCurricularParalelo.setMlcrprInscritos(mallaCurricularParalelo.getMlcrprInscritos()+1);
					}
					em.merge(mallaCurricularParalelo);
					
					retorno = comprobantePago;
					
					// Detalle matricula
					DetalleMatricula detallaMatricula = new DetalleMatricula();
					detallaMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
					detallaMatricula.setDtmtComprobantePago(comprobantePago);
					detallaMatricula.setDtmtValorPorMateria(itemMaterias.getValorMatricula());
					detallaMatricula.setDtmtNumero(itemMaterias.getNumMatricula());
					detallaMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					detallaMatricula.setDtmtValorParcial(BigDecimal.ZERO);
					em.persist(detallaMatricula);
					
					// Record Estudiante   
					RecordEstudiante recordEstudiante = new RecordEstudiante();
					recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
					recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
					if(valorMatricula.equals(BigDecimal.ZERO)){
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					}else{
						recordEstudiante.setRcesEstado(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE);
					}
					
					em.persist(recordEstudiante);
				}
				
				//Tipo Gratuidad
				TipoGratuidad tipoGratuidad = new TipoGratuidad();
				tipoGratuidad = em.find(TipoGratuidad.class, tipoGratuidadId);		
				Gratuidad gratuidad = new Gratuidad();
				gratuidad.setGrtEstado(GratuidadConstantes.ESTADO_ACTIVO_VALUE);
				gratuidad.setGrtFichaEstudiante(fichaEstudiante);
				gratuidad.setGrtTipoGratuidad(tipoGratuidad);
				gratuidad.setGrtFichaMatricula(fichaMatricula);
				em.persist(gratuidad);
			}
			
			if(retorno == null){
				session.getUserTransaction().rollback();
			}else {
				List<FichaMatricula> matriculas = new ArrayList<>();
						
				try {
					Query q = em.createNamedQuery("FichaMatricula.findPorFcesIdPracId");
					q.setParameter("fcesId", fichaEstudiante.getFcesId());
					q.setParameter("periodoId", pracId.getPracId());
					matriculas = q.getResultList();
					if (matriculas.size()>1) {
						if (pracId.getPracTipo().equals(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE)) {
							session.getUserTransaction().commit();	
						}else {
							session.getUserTransaction().rollback();	
						}
					}else {
						session.getUserTransaction().commit();	
					}
				} catch (Exception e) {
					session.getUserTransaction().rollback();	
				}
			}
			

		} catch (MatriculaValidacionException e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			throw new MatriculaValidacionException(e.getMessage());
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Agrega materias cambiando estados y recalulando valores, gratuidad, etc. 
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @return true or false
	 * @throws MatriculaException
	 */
	public boolean agregarMateriasMatriculaFull(int fcmtId, int cmpaId, int fcesId, int rcesEstado, List<MateriaDto> listaMaterias, Nivel nivel, BigDecimal valorTotal, Gratuidad gratuidad, int tipoGratuidadId, Usuario usuario) throws MatriculaException{
		
		boolean retorno = false;
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		
		try {
			session.getUserTransaction().begin();

			ComprobantePago comprobanteAux = em.find(ComprobantePago.class, cmpaId);
			comprobanteAux.setCmpaNumComprobante(null);
			comprobanteAux.setCmpaEstado(ComprobantePagoConstantes.ESTADO_EDITADO_VALUE);
			comprobanteAux.setCmpaTotalPago(valorTotal);
			comprobanteAux.setCmpaFechaEmision(null);
			comprobanteAux.setCmpaFechaCaduca(null);
			
			em.merge(comprobanteAux);
			
			FichaMatricula fichaMatrAux = em.find(FichaMatricula.class, fcmtId);
			fichaMatrAux.setFcmtNivelUbicacion(nivel.getNvlId());
			fichaMatrAux.setFcmtValorTotal(valorTotal);
			em.merge(fichaMatrAux);
			
			Gratuidad gratuidadAux = em.find(Gratuidad.class, gratuidad.getGrtId());
			TipoGratuidad tipoGratuidadAux = em.find(TipoGratuidad.class, tipoGratuidadId);
			gratuidadAux.setGrtTipoGratuidad(tipoGratuidadAux);
			em.merge(gratuidadAux);
			
			FichaEstudiante fichaEstuAux = em.find(FichaEstudiante.class, fcesId);
			
			if(listaMaterias.size() > 0 || listaMaterias != null){
				for (MateriaDto itemMateria : listaMaterias) {
					if(itemMateria.getRcesId() == 0 && itemMateria.getDtmtId() == 0 ){
						if(itemMateria.getPrlId() != null  && itemMateria.getPrlId() != GeneralesConstantes.APP_ID_BASE && itemMateria.getMtrId() != 0){

							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(itemMateria.getMlcrmtId(), itemMateria.getPrlId());

							DetalleMatricula detalleAux = new DetalleMatricula();
							detalleAux.setDtmtComprobantePago(comprobanteAux);
							detalleAux.setDtmtMallaCurricularParalelo(mlcrprAux);
							detalleAux.setDtmtNumero(itemMateria.getNumMatricula());
							detalleAux.setDtmtValorPorMateria(itemMateria.getValorMatricula());
							detalleAux.setDtmtValorParcial(itemMateria.getValorMatricula());
							detalleAux.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
							detalleAux.setDtmtModificacion(fechaActual);
							detalleAux.setDtmtUsuario(usuario.getUsrNick());
							em.persist(detalleAux);

							RecordEstudiante recordAux = new RecordEstudiante();
							recordAux.setRcesMallaCurricularParalelo(mlcrprAux);
							recordAux.setRcesFichaEstudiante(fichaEstuAux);
							recordAux.setRcesEstado(rcesEstado);
							recordAux.setRcesModificacion(fechaActual);
							recordAux.setRcesUsuario(usuario.getUsrNick());
							em.persist(recordAux);

							if(mlcrprAux.getMlcrprInscritos() == null){
								mlcrprAux.setMlcrprInscritos(0);
							}
							mlcrprAux.setMlcrprInscritos(mlcrprAux.getMlcrprInscritos() + 1);
							em.merge(mlcrprAux);
							//						break;
							//					}
						}
					}else{
						DetalleMatricula detalleAux = em.find(DetalleMatricula.class, itemMateria.getDtmtId());
						RecordEstudiante recordAux = em.find(RecordEstudiante.class, itemMateria.getRcesId());
						
						if(detalleAux != null && recordAux != null ){
							detalleAux.setDtmtNumero(itemMateria.getNumMatricula());
							detalleAux.setDtmtValorParcial(itemMateria.getValorMatricula());
							detalleAux.setDtmtValorPorMateria(itemMateria.getValorMatricula());
							em.merge(detalleAux);
							recordAux.setRcesEstado(rcesEstado);
							em.merge(recordAux);
						}else{
							session.getUserTransaction().rollback();
						}
						
					}
				}
			}
			
			session.getUserTransaction().commit();
			retorno = true;
		} catch (Exception e) {
			try {
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
			} catch (SecurityException e1) {
			} catch (SystemException e1) {
			}
			//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		}
		return retorno;
	}
	
	
	
	/**
	 * Obtiene el tipo de matricula en la que se va ha matricular
	 * @param prflId .- Id de proceso flujo a buscar
	 */
	private int obtenerTipoMatricula(int prflId){
		Integer tipoFichaMatricula = null;
		
		switch (prflId) {
		//Matricula Ordinaria
		case 2:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE; 	
			break;
		//Matricula Extraordinaria
		case 3:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_VALUE;
			break;
		//Matricula Especial
		case 5:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_VALUE;
			break;
		//Error al detectar el tipo de matricula
		default:
			tipoFichaMatricula = null;
			break;
		}
		
		return tipoFichaMatricula;
	}
//	
//	/**
//	 * Carga detalle del reporte orden de cobro 
//	 * @param lista
//	 * @param gratuidad
//	 * @return
//	 */
//	private List<MateriaDto> cargarDetalle(List<MateriaDto> lista){
//		List<MateriaDto> retorno = null;
//
//		BigDecimal primeraValor = BigDecimal.ZERO;
//		Integer primeraCreditosHoras = 0;
//		BigDecimal segundaValor = BigDecimal.ZERO;
//		Integer segundaCreditosHoras = 0;
//		BigDecimal terceraValor = BigDecimal.ZERO;
//		Integer terceraCreditosHoras = 0;
//		
//		for(MateriaDto item : lista){
//			if(item.getNumMatricula() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
//				primeraValor = primeraValor.add(item.getValorMatricula());
//				if(item.getMtrCreditos() != 0 ){
//					primeraCreditosHoras = primeraCreditosHoras+item.getMtrCreditos();
//				}
//				if(item.getMtrHorasCien() != 0){
//					primeraCreditosHoras = primeraCreditosHoras+item.getMtrHorasCien();
//				}
//			}
//			if(item.getNumMatricula() == SAUConstantes.SEGUNDA_MATRICULA_VALUE){
//				segundaValor = segundaValor.add(item.getValorMatricula());
//				if(item.getMtrCreditos() != 0 ){
//					segundaCreditosHoras = segundaCreditosHoras+item.getMtrCreditos();
//				}
//				if(item.getMtrHorasCien() != 0){
//					segundaCreditosHoras = segundaCreditosHoras+item.getMtrHorasCien();
//				}
//			}
//			if(item.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE){
//				terceraValor = terceraValor.add(item.getValorMatricula());
//				if(item.getMtrCreditos() != 0 ){
//					terceraCreditosHoras = terceraCreditosHoras+item.getMtrCreditos();
//				}
//				if(item.getMtrHorasCien() != 0){
//					terceraCreditosHoras = terceraCreditosHoras+item.getMtrHorasCien();
//				}
//			}
//		}
//		
//		retorno = new ArrayList<>();
//		
//		if(!primeraValor.equals(BigDecimal.ZERO)){
//			MateriaDto detalle = new MateriaDto();
//			detalle.setNumMatricula(SAUConstantes.PRIMERA_MATRICULA_VALUE);
//			detalle.setMtrDescripcion("PRIMERA MATRICULA");
//			detalle.setValorMatricula(primeraValor);
//			detalle.setMtrCreditos(primeraCreditosHoras);
//			retorno.add(detalle);
//		}
//		if(!segundaValor.equals(BigDecimal.ZERO)){
//			MateriaDto detalle = new MateriaDto();
//			detalle.setNumMatricula(SAUConstantes.SEGUNDA_MATRICULA_VALUE);
//			detalle.setMtrDescripcion("SEGUNA MATRICULA");
//			detalle.setValorMatricula(segundaValor);
//			detalle.setMtrCreditos(segundaCreditosHoras);
//			retorno.add(detalle);
//		}
//		if(!terceraValor.equals(BigDecimal.ZERO)){
//			MateriaDto detalle = new MateriaDto();
//			detalle.setNumMatricula(SAUConstantes.TERCERA_MATRICULA_VALUE);
//			detalle.setMtrDescripcion("TERCERA MATRICULA");
//			detalle.setValorMatricula(terceraValor);
//			detalle.setMtrCreditos(terceraCreditosHoras);
//			retorno.add(detalle);
//		}
//		
//		if(terceraValor.equals(BigDecimal.ZERO) && segundaValor.equals(BigDecimal.ZERO) && primeraValor.equals(BigDecimal.ZERO)){
//			MateriaDto detalle = new MateriaDto();
//			detalle.setNumMatricula(0);
//			detalle.setMtrDescripcion("GRATUIDAD");
//			detalle.setValorMatricula(BigDecimal.ZERO);
//			retorno.add(detalle);
//		}
//		
//		return retorno;
//	}
	
	
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
public Boolean eliminarHomologacion(int fcesId, int fcinId, int fcmtId, int grtId, int cmpaId,  List<DetalleMatricula> listDetalleMatricula, List<RecordEstudiante> listRecordEstudiante, List<Calificacion> listCalificacion, UsuarioRol usuarioRol) throws  MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			
			//Borro detallesMatricula de homologacion si existe, por lo no homologados que pueden no tener detalle
		if((listDetalleMatricula!=null)&&(listDetalleMatricula.size()>0)){
			
			for (DetalleMatricula itemDetalleMatricula : listDetalleMatricula) {
				DetalleMatricula detalleAux= em.find(DetalleMatricula.class, itemDetalleMatricula.getDtmtId());
				em.remove(detalleAux);
			}
			
			
		}
		//Borro calificaciones de homologación  si existe
         if((listCalificacion!=null)&&(listCalificacion.size()>0)){
			
			for (Calificacion itemCalificacion : listCalificacion) {
				Calificacion calificacionAux= em.find(Calificacion.class, itemCalificacion.getClfId());
				em.remove(calificacionAux);
			}
			
			
		}
         //Borro lista de record academico de homologación si existe
         if((listRecordEstudiante!=null)&&(listRecordEstudiante.size()>0)){
 			
 			for (RecordEstudiante itemRecordEstudiante : listRecordEstudiante) {
 				RecordEstudiante recordEstudianteAux= em.find(RecordEstudiante.class, itemRecordEstudiante.getRcesId());
 				em.remove(recordEstudianteAux);
 			}
 			
 			
 		}
		
         ComprobantePago comprobanteAux =em.find(ComprobantePago.class, cmpaId);
         em.remove(comprobanteAux);
         
         Gratuidad gratuidadAux= em.find(Gratuidad.class, grtId);
         em.remove(gratuidadAux);
         
         FichaMatricula fichaMatriculaAux = em.find(FichaMatricula.class, fcmtId);
         em.remove(fichaMatriculaAux);
         
        
         
//		FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fcinId);
//		fichaInscripcionAux.setFcinTipo(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE);
//		fichaInscripcionAux.setFcinEstado(FichaInscripcionConstantes.INACTIVO_VALUE);
//		fichaInscripcionAux.setFcinEstadoRetiro(FichaInscripcionConstantes.ESTADO_RETIRO_INACTIVO_VALUE);
//		fichaInscripcionAux.setFcinEstadoIngreso(null);
//		fichaInscripcionAux.setFcinDocumentoIngreso(null);
//		fichaInscripcionAux.setFcinCrrAnteriorId(null);
		
		//BORRO FICHA ESTUDIANTE EN IDIOMAS  PORQUE NO TIENE SOLICITUDES DE TERCERA
	//	if((fichaInscripcionAux.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE)) {
			
			FichaEstudiante fichaEstudianteAux = em.find(FichaEstudiante.class, fcesId);
			em.remove(fichaEstudianteAux);
			
	//	}
		
			//BORRO FCIN, TIENE QUE VOLVER A CARGAR LA FICHA INSCRIPCION, pedido de Mary
		FichaInscripcion fichaInscripcionAux = em.find(FichaInscripcion.class, fcinId);
		em.remove(fichaInscripcionAux);
			
			
		  //creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_ELIMINAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.FCMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(fichaMatriculaAux.getFcmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(comprobanteAux.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.GRT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(gratuidadAux.getGrtId());
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("eliminar-");
			detalleAux.append(JdbcConstantes.FCMT_ID);
			observacionAux.append("-");
			detalleAux.append(fichaMatriculaAux.getFcmtId());
			observacionAux.append("; eliminar-");
			detalleAux.append(JdbcConstantes.CMPA_ID);
			observacionAux.append("-");
			detalleAux.append(comprobanteAux.getCmpaId());
			observacionAux.append("; eliminar-");
			observacionAux.append(JdbcConstantes.GRT_ID);
			observacionAux.append("-");
			observacionAux.append(gratuidadAux.getGrtId());
			
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	em.persist(controlProceso);
		  	
		session.getUserTransaction().commit();
		
		retorno = true;
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//	
		return retorno;
	}

	/**MQ
	 * Agregar registros a homologación las entidades : Se añade record, detalle_matricula y calificaciones a una homologación existente
	 * @param listMaterias - lista de materias que se va a agregar
	 * @param personaDto - la persona que se va a agregar asignaturas a la homologación
	 * @throws MatriculaValidacionException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarAgregarEnMatriculaHomologacion(List<MateriaDto> listMaterias, RegistroHomologacionDto personaDto,UsuarioRol usuarioRol) throws MatriculaValidacionException, MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			
			//Busco la ficha del estudiante
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
			fichaEstudiante= em.find(FichaEstudiante.class, personaDto.getFcesId()); // buscamos la ficha estudiante existente para editar

				
			//Busco el comprobante de pago de homologacion
			ComprobantePago comprobantePago = new ComprobantePago();
	        comprobantePago= em.find(ComprobantePago.class, personaDto.getCmpaId());
			
			if(listMaterias!=null){
			for (MateriaDto itemMaterias : listMaterias) {

				
				//BUSCO MALLA_CURRICULAR_PARALELO  POR EL ID DE MLCRPR_ID QUE VIENE EN CADA MATERIA HOMOLOGADA
				MallaCurricularParalelo mallaCurricularParalelo = em.find(MallaCurricularParalelo.class, itemMaterias.getMlcrprId());
				
				//Detalle matricula
				DetalleMatricula detalleMatricula = new DetalleMatricula();
				detalleMatricula.setDtmtMallaCurricularParalelo(mallaCurricularParalelo);
				detalleMatricula.setDtmtComprobantePago(comprobantePago);
				detalleMatricula.setDtmtNumero(itemMaterias.getNumMatricula()); // calculado en el mb
				detalleMatricula.setDtmtEstado(DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
				detalleMatricula.setDtmtValorPorMateria(BigDecimal.ZERO);
			   	em.persist(detalleMatricula);
					
				//Record Estudiante
				RecordEstudiante recordEstudiante = new RecordEstudiante();
				recordEstudiante.setRcesMallaCurricularParalelo(mallaCurricularParalelo);
				recordEstudiante.setRcesFichaEstudiante(fichaEstudiante);
				recordEstudiante.setRcesEstado(itemMaterias.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				
			
			 	em.persist(recordEstudiante);
					
				//registro de notas
				Calificacion calificacion = new Calificacion();
				calificacion.setClfNota1(new Float(itemMaterias.getNotaUno().toString()));
				calificacion.setClfNota2(new Float(itemMaterias.getNotaDos().toString()));
				calificacion.setClfNotaFinalSemestre(itemMaterias.getNotaSuma().floatValue());
				calificacion.setRecordEstudiante(recordEstudiante);
			 	em.persist(calificacion);
			 	
			 	
				//creo Control del proceso
			  	ControlProceso controlProceso = new ControlProceso();
			  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
			  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_HOMOLOGACION_LABEL);
			  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
			  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
			  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_INSERTAR_VALUE);
			  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
			  	
			  	StringBuilder detalleAux= new StringBuilder();
			  	detalleAux.append(JdbcConstantes.DTMT_ID);
			  	detalleAux.append(":");
			  	detalleAux.append(detalleMatricula.getDtmtId());
			  	detalleAux.append(";");
			  	detalleAux.append(JdbcConstantes.FCES_ID);
			  	detalleAux.append(":");
			  	detalleAux.append(fichaEstudiante.getFcesId());
			  	detalleAux.append(";");
			
			  	detalleAux.append(JdbcConstantes.CMPA_ID);
			  	detalleAux.append(":");
			  	detalleAux.append(comprobantePago.getCmpaId());
			  	detalleAux.append(";");
			  	detalleAux.append(JdbcConstantes.RCES_ID);
			  	detalleAux.append(":");
			  	detalleAux.append(recordEstudiante.getRcesId());
			  	detalleAux.append(";");
			  	detalleAux.append(JdbcConstantes.CLF_ID);
			  	detalleAux.append(":");
			  	detalleAux.append(calificacion.getClfId());
					
			  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
			  	
				StringBuilder observacionAux= new StringBuilder();
				observacionAux.append("actualizar-");
				observacionAux.append(JdbcConstantes.FCMT_ID);
				observacionAux.append("-");
				observacionAux.append(personaDto.getFcmtId());
				observacionAux.append("; crear-");
				observacionAux.append(JdbcConstantes.DTMT_ID);
				observacionAux.append("-");
				observacionAux.append(detalleMatricula.getDtmtId());
				observacionAux.append("; crear-");
				observacionAux.append(JdbcConstantes.RCES_ID);
				observacionAux.append("-");
				observacionAux.append(recordEstudiante.getRcesId());
				observacionAux.append("; crear-");
				observacionAux.append(JdbcConstantes.CLF_ID);
				observacionAux.append("-");
				observacionAux.append(calificacion.getClfId());
			  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
			  	
			  	em.persist(controlProceso); 
			 	
			 	
			 }
			
			} 	

		  	
		session.getUserTransaction().commit();
	
			retorno = true;
	
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
//			throw new MatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroFichaEsdudiante.buscar.por.crr.ttl.apellido.ide.no.encontrado.exception")));
			throw new MatriculaException(e.getMessage());
		} 
//	
		return retorno;
	}
	
	
	/**MQ
	 * Editar registros  homologación  : Se actualiza notas y número de matricula de una asignatura homologada
	 * 
	 * @param materiaEditada -  materia que va a ser editada
	 * @param usuarioRol - la persona que se va a editar la asignatura en la homologación
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarEditarEnMatriculaHomologacion(MateriaDto materiaEditada,UsuarioRol usuarioRol) throws MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			
			//Busco el recordEstudiante para editar el estado
			RecordEstudiante recordEstudiante = new RecordEstudiante();
			recordEstudiante= em.find(RecordEstudiante.class, materiaEditada.getRcesId());
			//recordEstudiante.setRcesEstado(materiaEditada.getEstadoHomologacion());
			recordEstudiante.setRcesEstado(materiaEditada.getAprobado()==true ? RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE:RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			
			
			//Busco la calificacion para editar la nota
			Calificacion calificacion = new Calificacion();
			calificacion= em.find(Calificacion.class, materiaEditada.getClfId());
			calificacion.setClfNota1(new Float(materiaEditada.getNotaUno().toString()));
			calificacion.setClfNota2(new Float(materiaEditada.getNotaDos().toString()));
			calificacion.setClfNotaFinalSemestre(materiaEditada.getNotaSuma().floatValue());
			
			
			//Busco el detalle Matricula para cambiar el número de matricula
			DetalleMatricula detalleMatricula = new DetalleMatricula();
			detalleMatricula= em.find(DetalleMatricula.class, materiaEditada.getDtmtId());
			detalleMatricula.setDtmtNumero(materiaEditada.getNumMatricula());
			
			//creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_EDICION_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_ACTUALIZAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.DTMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(detalleMatricula.getDtmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(materiaEditada.getFcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(materiaEditada.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.RCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(recordEstudiante.getRcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CLF_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(calificacion.getClfId());
				
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("actualizar-");
			observacionAux.append(JdbcConstantes.DTMT_ID);
			observacionAux.append("-");
			observacionAux.append(detalleMatricula.getDtmtId());
			observacionAux.append("; actualiza-");
			observacionAux.append(JdbcConstantes.RCES_ID);
			observacionAux.append("-");
			observacionAux.append(recordEstudiante.getRcesId());
			observacionAux.append("; actualizar-");
			observacionAux.append(JdbcConstantes.CLF_ID);
			observacionAux.append("-");
			observacionAux.append(calificacion.getClfId());
					
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	
		  	em.persist(controlProceso); 
		  	
		session.getUserTransaction().commit();
	
			retorno = true;
	
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new MatriculaException(e.getMessage());
		} 
		return retorno;
	}
	
	
	/**MQ
	 * Eliminar un registro de homologación  
	 * 
     * @param materiaEditada -  materia que va a ser editada
	 * @param usuarioRol - la persona que se va a editar la asignatura en la homologación
	 * @throws MatriculaException - Excepcion general
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean generarEliminarEnMatriculaHomologacion(MateriaDto materiaEliminada,UsuarioRol usuarioRol) throws MatriculaException{
		
		Boolean retorno= false;
		
		try {
			session.getUserTransaction().begin();
			
			//Busco la calificacion para editar la nota
			Calificacion calificacion = new Calificacion();
			calificacion= em.find(Calificacion.class, materiaEliminada.getClfId());
			
			em.remove(calificacion);
			
			//Busco el recordEstudiante para editar el estado
			RecordEstudiante recordEstudiante = new RecordEstudiante();
			recordEstudiante= em.find(RecordEstudiante.class, materiaEliminada.getRcesId());
			
			em.remove(recordEstudiante);
			
			
			//Busco el detalle Matricula para cambiar el número de matricula
			DetalleMatricula detalleMatricula = new DetalleMatricula();
			detalleMatricula= em.find(DetalleMatricula.class, materiaEliminada.getDtmtId());

			em.remove(detalleMatricula);
			
			//creo Control del proceso
		  	ControlProceso controlProceso = new ControlProceso();
		  	UsuarioRol usuarioRolAux= em.find(UsuarioRol.class, usuarioRol.getUsroId());
		  	TipoProceso tipoProcesoAux= servTipoProceso.buscarXDescripcion(TipoProcesoConstantes.TIPO_PROCESO_EDICION_HOMOLOGACION_LABEL);
		  	controlProceso.setCnprUsuarioRol(usuarioRolAux);
		  	controlProceso.setCnprTipoProceso(tipoProcesoAux);
		  	controlProceso.setCnprTipoAccion(ControlProcesoConstantes.TIPO_ACCION_ELIMINAR_VALUE);
		  	controlProceso.setCnprFechaAccion(new Timestamp(new Date().getTime()));
		  	
		  	StringBuilder detalleAux= new StringBuilder();
		  	detalleAux.append(JdbcConstantes.DTMT_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(detalleMatricula.getDtmtId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.FCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(materiaEliminada.getFcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CMPA_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(materiaEliminada.getCmpaId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.RCES_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(recordEstudiante.getRcesId());
		  	detalleAux.append(";");
		  	detalleAux.append(JdbcConstantes.CLF_ID);
		  	detalleAux.append(":");
		  	detalleAux.append(calificacion.getClfId());
				
		  	controlProceso.setCnprDetalleProceso(detalleAux.toString());
		  	
			StringBuilder observacionAux= new StringBuilder();
			observacionAux.append("eliminar-");
			observacionAux.append(JdbcConstantes.DTMT_ID);
			observacionAux.append("-");
			observacionAux.append(detalleMatricula.getDtmtId());
			observacionAux.append("; eliminar-");
			observacionAux.append(JdbcConstantes.RCES_ID);
			observacionAux.append("-");
			observacionAux.append(recordEstudiante.getRcesId());
			observacionAux.append("; eliminar-");
			observacionAux.append(JdbcConstantes.CLF_ID);
			observacionAux.append("-");
			observacionAux.append(calificacion.getClfId());
					
		  	controlProceso.setCnprObservacionAccion(observacionAux.toString());
		  	
		  	em.persist(controlProceso); 
		  	
		session.getUserTransaction().commit();
	
			retorno = true;
	
		
		} catch (Exception e) {
			try {
				e.printStackTrace();
				session.getUserTransaction().rollback();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			throw new MatriculaException(e.getMessage());
		} 
		return retorno;
	}
	
	private int obtenerTipoMatriculaSuficiencias(int prflId){
		Integer tipoFichaMatricula = null;
		
		switch (prflId) {
		//Matricula Ordinaria
		case 2:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE; 	
			break;
		//Matricula Extraordinaria
		case 3:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_VALUE;
			break;
		//Matricula Especial
		case 5:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_VALUE;
			break;
		case 25:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE;
			break;
		case 24:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE;
			break;
		case 23:
			tipoFichaMatricula = MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE;
			break;
		//Error al detectar el tipo de matricula
		default:
			tipoFichaMatricula = null;
			break;
		}
		
		return tipoFichaMatricula;
	}
	
	
}