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
   
 ARCHIVO:     RegistroHistoricoForm.java	  
 DESCRIPCION: Managed Bean que maneja el registro histórico de los estudiantes con 2da y 3er que entran a nivelación.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
05-04-2017			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.registro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoSistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoSistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.SistemaCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSistemaCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) RegistroHistoricoForm.
 * Managed Bean que maneja el registro histórico de los estudiantes con 2da y 3er que entran a nivelación.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name="registroHistoricoForm")
@SessionScoped
public class RegistroHistoricoForm implements Serializable{

	private static final long serialVersionUID = -397102404723196895L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	//Objetos
	private Usuario rhfUsuario;
	private PersonaDto rhfPersonaDtoBuscar;
	private CarreraDto rhfCarreraDtoBuscar;
	private PersonaDto rhfPersonaDtoEditar;
	private PeriodoAcademico rhfPeriodoAcademico;
	private MallaCurricular rhfMallaCurricular;
	private TipoSistemaCalificacion rhfTipoSistemaCalificacion;
	private SistemaCalificacion rhfSistemaCalificacion;
	private PlanificacionCronograma rhfPlanificacionCronograma;
	private PeriodoAcademico rhfPeriodoAcademicoActivo;
	private Integer rhfValidadorClic;
	private Integer rhfBloqueoModal;  // 1.- no   0.- si   para evitar que recarguen la página y se pueda generar la matricula una y otra vez
		
	//Listas de Objetos
	private List<PersonaDto> rhfListPersonaDto;
    private List<CarreraDto> rhfListCarreraDto;
    private List<MateriaDto> rhfListMateriaDto;
    private List<ParaleloDto> rhfListParaleloDto;
    
    
		
	
	
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
		
	}
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	//Para la busqueda
	
	@EJB 
	PersonaDtoServicioJdbc servRhfPersonaDto;
	@EJB 
	CarreraDtoServicioJdbc servRhfCarreraDto;
	@EJB 
	PeriodoAcademicoServicio servRhfPeriodoAcademico;
	@EJB 
	MallaCurricularServicio servRhfMallaCurricular;
	@EJB 
	MateriaDtoServicioJdbc servRhfMateriaDto; 
	@EJB 
	ParaleloDtoServicioJdbc servRhfParaleloDto;
	@EJB 
	TipoSistemaCalificacionServicio servRhfTipoSistemaCalificacion;
	@EJB 
	SistemaCalificacionServicio servRhfSistemaCalificacion;
	@EJB 
	PlanificacionCronogramaServicio servRhfPlanificacionCronograma;
	@EJB 
	MatriculaServicio servRhfMatriculaServicio;
	
	
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	//Para ir a Administracion del Paralelo
	
	
	/**
	 * Método que permite iniciar la administración del paralelo
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración Paralelo.
	 */
	
	public String irAlistarEstudiante(Usuario usuario) { 
		
		rhfUsuario= usuario;
		rhfBloqueoModal = 1;
		iniciarParametros();
		return "irAlistarEstudianteRegistroHistorico";
	}
	
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		FacesUtil.limpiarMensaje();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados y vuelve a llenar los periodos
	 */
	public void limpiar()  {
		iniciarParametros();
	}
	
	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
		try {
		
		//Inicio los objetos
		rhfPersonaDtoBuscar = new PersonaDto();
		rhfPersonaDtoBuscar.setPrsIdentificacion("");
		rhfCarreraDtoBuscar = new CarreraDto();
		rhfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
		
		//inicio la lista de Personas
		rhfListPersonaDto= null;
        rhfListCarreraDto= null;
        
		//seteo la carrera para que busque por todas
		rhfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		
		//busco el periodo academico activo
		rhfPeriodoAcademicoActivo = servRhfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		
        //lleno las carreras
//        rhfListCarreraDto=servRhfCarreraDto.listarXfacultad(DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE);
		rhfListCarreraDto=servRhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rhfUsuario.getUsrId(), RolConstantes.ROL_USUARIONIVELACION_VALUE , RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE ,  rhfPeriodoAcademicoActivo.getPracId());
		
        //lleno los estudiantes
        rhfListPersonaDto= servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(), RolConstantes.ROL_ESTUD_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE,FichaInscripcionConstantes.INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, rhfPersonaDtoBuscar.getPrsIdentificacion());
//		rhfListPersonaDto = null;
        
        //validador click en 0
        rhfValidadorClic = new Integer(0);
        
//        rhfBloqueoModal = 1;
	   
//		} catch (PersonaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaDtoNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcException e) {
			
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			
			FacesUtil.mensajeError(e.getMessage());
			//e.printStackTrace();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
	}
	
	/**
	 * Método para buscar las personas con los parámetros ingresados en los filtros de busqueda
	 */
	public void buscar()  {
		//lleno los estudiantes
        try {
        	rhfListPersonaDto = null;
//        	if(rhfCarreraDtoBuscar.getCrrIdentificador() != GeneralesConstantes.APP_ID_BASE){
//        		rhfListPersonaDto= servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(), RolConstantes.ROL_ESTUD_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE,FichaInscripcionConstantes.INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, rhfPersonaDtoBuscar.getPrsIdentificacion());
//        	}else{
//        		FacesUtil.limpiarMensaje();
//        		FacesUtil.mensajeError("Debe seleccionar un área");
//        		
//        	}
        	
        	rhfListPersonaDto= servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(), RolConstantes.ROL_ESTUD_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE,FichaInscripcionConstantes.INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, rhfPersonaDtoBuscar.getPrsIdentificacion());
        	
        	
        	
		} catch (PersonaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
	}
	
	/**
	 * Método para inicar el registro del histórico del estudiante
	 * @return navegación a la pagina de registrar el historico del estudiante
	 */
	public String irRegistrarHistorico(PersonaDto rhfPersonaDto)  {
		rhfPersonaDtoEditar = rhfPersonaDto;
		rhfBloqueoModal = 1;
		try {
			
			//proceso para buscar la planificacion cronograma activa
			// creo la lista de proceso flujo correspondiente a la matricula
			List<Integer> listProcesoFlujoAux = new ArrayList<Integer>();
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			
			//busqueda del período academico anterior
			rhfPeriodoAcademico = servRhfPeriodoAcademico.buscarPorId(PeriodoAcademicoConstantes.PRAC_DIESISEIS_DIECISIETE_VALUE);
			
			//listo planificaciones cronograma de matriculas
			List<PlanificacionCronograma>listPlanificacionCronogramaAux;
			listPlanificacionCronogramaAux = servRhfPlanificacionCronograma.buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(rhfPeriodoAcademico.getPracId(),listProcesoFlujoAux, CronogramaConstantes.TIPO_NIVELACION_VALUE);
							
			//validacion de las fechas del cronograma
			int cont = 0;
			int contNumPlanificacion = 0;
			for (PlanificacionCronograma item : listPlanificacionCronogramaAux) {
				contNumPlanificacion++;
				if(item.getPlcrEstado() == PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE){
					rhfPlanificacionCronograma = item;
					cont++;
				}
				if(cont == 0 && contNumPlanificacion == listPlanificacionCronogramaAux.size()){
					//TODO: GENERAR EL MENSAJE
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.encuesta.validacion.exception")));
					FacesUtil.mensajeError("No existen planificacion cromograma activo, por favor comuniquese con el administrador del sistema");
					return null;
				}
			}
			
			//Buscar la malla curricular vigente y activa de la carrera 
			rhfMallaCurricular = servRhfMallaCurricular.buscarXcarreraXvigenciaXestado(rhfPersonaDtoEditar.getCrrId(), MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			//buscar las materias de nivelacion de esa malla
			rhfListMateriaDto = servRhfMateriaDto.listarXmallaXnivel(rhfMallaCurricular.getMlcrId(), NivelConstantes.NIVEL_NIVELACION_VALUE);
			
			for (MateriaDto itmMateria : rhfListMateriaDto) {
				//busco los paralelos de esa materia
				rhfListParaleloDto = servRhfParaleloDto.listarXmallaMateriaXperiodo(itmMateria.getMlcrmtId(), rhfPeriodoAcademico.getPracId());
				//guardo los paralelos de esa materia
				itmMateria.setListParalelos(rhfListParaleloDto);
				// seteo la variable de seleccion ya que los estudiantes nuevos cogen todas las materias obligatoriamente
				itmMateria.setIsSeleccionado(true); 
//				 seteo el numero de matricula a primera
//				itmMateria.setNumMatricula(GeneralesConstantes.APP_PRIMERA_MATRICULA);
				//asignar valores por defecto de las notas
				itmMateria.setNotaUno(new BigDecimal(0));
				itmMateria.setNotaDos(new BigDecimal(0));
				itmMateria.setNotaTres(new BigDecimal(0));
				itmMateria.setNotaPromedio(new BigDecimal(0));
				itmMateria.setAprobado(false);
			}
			
			//buscar el tipo de sistema de calificaciones por la descripcion : "NIVELACION"
			rhfTipoSistemaCalificacion = servRhfTipoSistemaCalificacion.buscarXDescripcion(TipoSistemaCalificacionConstantes.SISTEMA_CALIFICACION_NIVELACION_LABEL);
			//buscar el sistema de calificaciones por el  periodo academiico y por el tipo sistema de calificaciones
			rhfSistemaCalificacion = servRhfSistemaCalificacion.listarSistemaCalificacionXPracXtissclXEstado(rhfPeriodoAcademico.getPracId(), rhfTipoSistemaCalificacion.getTissclId(), SistemaCalificacionConstantes.INACTIVO_VALUE);
			
			//Asignación de paralelos
			ParaleloDto paraleloAux = null ;
			for (ParaleloDto paraleloDtoItem : rhfListParaleloDto) { // recorro los paralelos disponibles
				if(paraleloDtoItem.getMlcrprInscritos().intValue() < paraleloDtoItem.getPrlCupo().intValue()){ // si el cupo no esta lleno
					paraleloAux = paraleloDtoItem; // guardo el paralelo con cupo disponible
					break;
				}
			} 
			if(paraleloAux != null){ // si existio paralelo disponible
				for (MateriaDto materiasItem : rhfListMateriaDto) { // recorro la lista de materias 
					materiasItem.setPrlId(paraleloAux.getPrlId()); // asigno el paralelo a las materias
					materiasItem.setPrlDescripcion(paraleloAux.getPrlDescripcion());
					materiasItem.setMlcrprId(paraleloAux.getMlcrprId()); //
					
					//busco la malla curricular paralelo
					
					//asignar 1 solo cupo a ese paralelo 
				}
			}else{ // si no exite cupos disponibles en ningun paralelo
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatricula.paralelo.validacion.exception")));
				return null;
			}
			
			
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoSistemaCalificacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoSistemaCalificacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (SistemaCalificacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (SistemaCalificacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
        return "irRegistrarHistorico";
	}
	
	
	/**
	 * Método para inicar el registro del histórico del estudiante
	 * @return navegación a la pagina de registrar el historico del estudiante
	 */
	public String guardarHistorico()  {
		if(rhfBloqueoModal == 1){
			rhfBloqueoModal = 0;
			
			try {
				
//				Integer cont = 0;
//				for (MateriaDto itemNumMatricula : rhfListMateriaDto) {
//					if(!itemNumMatricula.getAprobado()&&itemNumMatricula.getNumMatricula()==1){
//						cont++;
//					}
//					if(cont>=1){
//						//itemNumMatricula.setAprobado(false);
//									
//						}
//					
//					
//				}
				
			servRhfMatriculaServicio.generarMatriculaHistorico(rhfListMateriaDto, rhfPersonaDtoEditar
						, 0
						, 3
						, GeneralesConstantes.APP_CERO_VALUE, rhfPlanificacionCronograma);
				
				iniciarParametros();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHistorico.guardar.hostorico.exitoso")));
				
			} catch (MatriculaValidacionException e) {
				//validador click en 0
		        rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeInfo(e.getMessage());
				return null;
			} catch (MatriculaException e) {
				//validador click en 0
		        rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeInfo(e.getMessage());
				return null;
			}
		}
	
		//validador click en 0
    	rhfValidadorClic = new Integer(0);
		return "irAlistarEstudianteRegistroHistorico";
	}
	
	/**
	 * Método para inicar el registro del histórico del estudiante
	 * @return navegación a la pagina de registrar el historico del estudiante
	 */
	public String cancelarHistorico()  {
		rhfPlanificacionCronograma = new PlanificacionCronograma();
		rhfPeriodoAcademico = new PeriodoAcademico();
		rhfMallaCurricular = new MallaCurricular();
		rhfListMateriaDto = null;
		rhfListParaleloDto = null;
//		rhfListPersonaDto = null;
		rhfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
		rhfTipoSistemaCalificacion = new TipoSistemaCalificacion();
		rhfSistemaCalificacion = new SistemaCalificacion();
		//validador click en 0
        rhfValidadorClic = new Integer(0);
        rhfBloqueoModal = 1;
		return "irAlistarEstudianteRegistroHistorico";
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
			
	/**
	 * verifica que haga click en el boton generar matricula
	 */
	public String verificarClickGenerarMatricula(){
		if(rhfBloqueoModal == 1){
			rhfValidadorClic = 1;
		}else{
			rhfValidadorClic = 0;
		}
		return null;
	}
	
	/**
	 * calcula el promedio de las tres notas
	 */
		public void calcularPromedio(MateriaDto materiaDto){
			BigDecimal notaSuma = materiaDto.getNotaUno().add(materiaDto.getNotaDos()); //hago la sumatoria de las dos notas
			BigDecimal notaPromedioSuma = notaSuma.divide(new BigDecimal(2), 2, RoundingMode.DOWN); // hago el promedio de las dos notas
			materiaDto.setNotaPromedio(notaPromedioSuma); //seteo la nota promedio suma
			int result = notaSuma.compareTo(new BigDecimal(rhfSistemaCalificacion.getSsclNotaMinimaAprobacion().toString())); 
			if(result >=  0){
				//aprobado por notas
				int resultAsistencia = materiaDto.getNotaTres().compareTo(new BigDecimal(rhfSistemaCalificacion.getSsclPorcentajeAprobacion().toString()));
				if(resultAsistencia >= 0){
					//aprobado por asistencia y notas
					materiaDto.setAprobado(true); // seteo el aprobado de la materia
				}else{
					//reprobado por asistencias 
					materiaDto.setAprobado(false); // seteo el reprobado de la materia
				}
			}else{
				//reprobado por notas
				materiaDto.setAprobado(false); // seteo el reprobado de la materia
			}
		}
		
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	
	public Usuario getRhfUsuario() {
		return rhfUsuario;
	}


	public void setRhfUsuario(Usuario rhfUsuario) {
		this.rhfUsuario = rhfUsuario;
	}


	public List<PersonaDto> getRhfListPersonaDto() {
		rhfListPersonaDto = rhfListPersonaDto==null?(new ArrayList<PersonaDto>()):rhfListPersonaDto;
		return rhfListPersonaDto;
	}


	public void setRhfListPersonaDto(List<PersonaDto> rhfListPersonaDto) {
		this.rhfListPersonaDto = rhfListPersonaDto;
	}


	public PersonaDto getRhfPersonaDtoBuscar() {
		return rhfPersonaDtoBuscar;
	}


	public void setRhfPersonaDtoBuscar(PersonaDto rhfPersonaDtoBuscar) {
		this.rhfPersonaDtoBuscar = rhfPersonaDtoBuscar;
	}


	public List<CarreraDto> getRhfListCarreraDto() {
		rhfListCarreraDto = rhfListCarreraDto==null?(new ArrayList<CarreraDto>()):rhfListCarreraDto;
		return rhfListCarreraDto;
	}


	public void setRhfListCarreraDto(List<CarreraDto> rhfListCarreraDto) {
		this.rhfListCarreraDto = rhfListCarreraDto;
	}


	public CarreraDto getRhfCarreraDtoBuscar() {
		return rhfCarreraDtoBuscar;
	}


	public void setRhfCarreraDtoBuscar(CarreraDto rhfCarreraDtoBuscar) {
		this.rhfCarreraDtoBuscar = rhfCarreraDtoBuscar;
	}


	public PeriodoAcademico getRhfPeriodoAcademico() {
		return rhfPeriodoAcademico;
	}


	public void setRhfPeriodoAcademico(PeriodoAcademico rhfPeriodoAcademico) {
		this.rhfPeriodoAcademico = rhfPeriodoAcademico;
	}


	public MallaCurricular getRhfMallaCurricular() {
		return rhfMallaCurricular;
	}


	public void setRhfMallaCurricular(MallaCurricular rhfMallaCurricular) {
		this.rhfMallaCurricular = rhfMallaCurricular;
	}


	public PersonaDto getRhfPersonaDtoEditar() {
		return rhfPersonaDtoEditar;
	}


	public void setRhfPersonaDtoEditar(PersonaDto rhfPersonaDtoEditar) {
		this.rhfPersonaDtoEditar = rhfPersonaDtoEditar;
	}


	public List<MateriaDto> getRhfListMateriaDto() {
		rhfListMateriaDto = rhfListMateriaDto==null?(new ArrayList<MateriaDto>()):rhfListMateriaDto;
		return rhfListMateriaDto;
	}


	public void setRhfListMateriaDto(List<MateriaDto> rhfListMateriaDto) {
		this.rhfListMateriaDto = rhfListMateriaDto;
	}


	public List<ParaleloDto> getRhfListParaleloDto() {
		rhfListParaleloDto = rhfListParaleloDto==null?(new ArrayList<ParaleloDto>()):rhfListParaleloDto;
		return rhfListParaleloDto;
	}


	public void setRhfListParaleloDto(List<ParaleloDto> rhfListParaleloDto) {
		this.rhfListParaleloDto = rhfListParaleloDto;
	}


	public TipoSistemaCalificacion getRhfTipoSistemaCalificacion() {
		return rhfTipoSistemaCalificacion;
	}


	public void setRhfTipoSistemaCalificacion(TipoSistemaCalificacion rhfTipoSistemaCalificacion) {
		this.rhfTipoSistemaCalificacion = rhfTipoSistemaCalificacion;
	}


	public SistemaCalificacion getRhfSistemaCalificacion() {
		return rhfSistemaCalificacion;
	}


	public void setRhfSistemaCalificacion(SistemaCalificacion rhfSistemaCalificacion) {
		this.rhfSistemaCalificacion = rhfSistemaCalificacion;
	}


	public PlanificacionCronograma getRhfPlanificacionCronograma() {
		return rhfPlanificacionCronograma;
	}


	public void setRhfPlanificacionCronograma(PlanificacionCronograma rhfPlanificacionCronograma) {
		this.rhfPlanificacionCronograma = rhfPlanificacionCronograma;
	}


	public PeriodoAcademico getRhfPeriodoAcademicoActivo() {
		return rhfPeriodoAcademicoActivo;
	}


	public void setRhfPeriodoAcademicoActivo(PeriodoAcademico rhfPeriodoAcademicoActivo) {
		this.rhfPeriodoAcademicoActivo = rhfPeriodoAcademicoActivo;
	}


	public Integer getRhfValidadorClic() {
		return rhfValidadorClic;
	}


	public void setRhfValidadorClic(Integer rhfValidadorClic) {
		this.rhfValidadorClic = rhfValidadorClic;
	}


	public Integer getRhfBloqueoModal() {
		return rhfBloqueoModal;
	}


	public void setRhfBloqueoModal(Integer rhfBloqueoModal) {
		this.rhfBloqueoModal = rhfBloqueoModal;
	}
	
	
}
	
	
	
	
	
	
	
	
	
	
