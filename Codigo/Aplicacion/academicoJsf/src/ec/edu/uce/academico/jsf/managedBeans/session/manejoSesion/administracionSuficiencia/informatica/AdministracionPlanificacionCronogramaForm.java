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
   
 ARCHIVO:     AdministracionPlanificacionCronogramaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración  del proceso Planificacion Cronogramas de las Suficiencias.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
22-OCT-2018				 FREDDY GUZMÁN						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSuficiencia.informatica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MallaPeriodoDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.ProcesoFlujoDto;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaPeriodoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaPeriodoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) AdministracionPlanificacionCronogramaForm.
 * Managed Bean que maneja las peticiones para la administración del proceso Planificacion Cronogramas de las Suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name="administracionPlanificacionCronogramaForm")
@SessionScoped
public class AdministracionPlanificacionCronogramaForm implements Serializable{

	private static final long serialVersionUID = -3934424476360393698L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	private Usuario apcfUsuario;
	private Integer apcfModalidadId;
	private Integer apcfEstadoId;
	private Integer apcfPeriodoId;
	private Integer apcfActivarModal;
	
	private PlanificacionCronogramaDto apcfPlanificacionCronogramaDto;
	private MallaPeriodoDto apcfMallaPeriodoDto;
	
	private List<ModalidadDto> apcfListModalidadDto;
	private List<PeriodoAcademicoDto> apcfListPeriodoAcademicoDto;
	private List<SelectItem> apcfListEstado;
	private List<PlanificacionCronogramaDto> apcfListPlanificacionCronogramaDto;
	private List<MallaPeriodoDto> apcfListMallaPeriodoDto;	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private MallaPeriodoServicio servMallaPeriodo;
	@EJB private ProcesoFlujoServicio servProcesoFlujo;
	@EJB private PlanificacionCronogramaServicio servPlanificacionCronograma;
	
	@EJB private MallaPeriodoDtoServicioJdbc servJdbcMallaPeriodoDto;
	@EJB private PlanificacionCronogramaDtoServicioJdbc servJdbcPlanificacionCronogramaDto;
	@EJB private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	public String irInicio(){
		vaciarVariables();
		return "irInicio"; 	
	}
	
	public String irListarPlanificacionInformatica(Usuario usuario) {
		apcfUsuario = usuario;
		apcfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apcfListModalidadDto = cargarModalidadesInformatica();
		apcfListPlanificacionCronogramaDto = cargarProcesosFlujoActivosInformatica();
		apcfActivarModal = GeneralesConstantes.APP_ID_BASE;
		return "irListarPlanificacionInformatica";
	}
	
	public String irListarPlanificacionIdiomas(Usuario usuario) {
		
		try {
			apcfUsuario = usuario;
			apcfEstadoId = GeneralesConstantes.APP_ID_BASE;
			apcfPeriodoId = GeneralesConstantes.APP_ID_BASE;
			apcfListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE},new Integer[]{PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE});
			apcfListPeriodoAcademicoDto.sort(Comparator.comparing(PeriodoAcademicoDto::getPracId).reversed());
			apcfListEstado = cargarEstadosIdiomas();
			apcfListPlanificacionCronogramaDto = cargarProcesosFlujoActivosIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			apcfActivarModal = GeneralesConstantes.APP_ID_BASE;
			return "irListarPlanificacionIdiomas";	
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}
	
	public String irListarPlanificacionCulturaFisica(Usuario usuario) {
		apcfUsuario = usuario;
		apcfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apcfListModalidadDto = cargarModalidadesInformatica();
		apcfListPlanificacionCronogramaDto = cargarProcesosFlujoActivosInformatica();
		apcfActivarModal = GeneralesConstantes.APP_ID_BASE;
		return "irListarPlanificacionCulturaFisica";
	}
	
	public String irNuevaPlanficacionCronograma(){
		iniciarVariablesFormNuevaPlanificacion();
		return "irNuevaPlanficacionCronograma";
	}

	public String irNuevaPlanficacionCronogramaIdiomas(){
		iniciarVariablesFormNuevaPlanificacionIdiomas();
		return "irNuevaPlanficacionCronogramaIdiomas";
	}
	
	public String irEditarPlanficacionCronograma(PlanificacionCronogramaDto proceso){
		apcfPlanificacionCronogramaDto = proceso;
		return "irEditarPlanficacionCronograma";
	}
	
	public String irEditarPlanficacionCronogramaIdiomas(PlanificacionCronogramaDto proceso){
		apcfPlanificacionCronogramaDto = proceso;
		return "irEditarPlanficacionCronogramaIdiomas";
	}
	
	
	public String irCronogramasInformatica(){
		limpiarFormCronograma();
		limpiarFormNuevaPlanificacion();
		return "irCronogramasInformatica"; 	
	}
	
	public String irCronogramasIdiomas(){
		limpiarFormCronograma();
		limpiarFormNuevaPlanificacion();
		return "irCronogramasIdiomas"; 	
	}
	
	public String crearPeriodoAcademico(){
		boolean retorno = false;
		
		try {
			List<MallaPeriodoDto> mallasPeriodo = cargarMallasPeriodoPorTipoPeriodo(2);
			if (mallasPeriodo != null && mallasPeriodo.size()>0) {
				apcfListMallaPeriodoDto = mallasPeriodo;	
			}else {
				mallasPeriodo = new ArrayList<>();
				MallaCurricularDto mlcrInformatica = new MallaCurricularDto();mlcrInformatica.setMlcrCrrId(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE);
				MallaPeriodoDto mlprInformatica = new MallaPeriodoDto();mlprInformatica.setMlprMallaCurricularDto(mlcrInformatica);
				mallasPeriodo.add(mlprInformatica);
				apcfListMallaPeriodoDto = mallasPeriodo;
			}

			retorno = servPlanificacionCronograma.crearNuevaPlanificacion(apcfPlanificacionCronogramaDto, apcfListPlanificacionCronogramaDto, apcfListMallaPeriodoDto);
			if (retorno) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Período académico registrado con éxito.");
				limpiarFormNuevaPlanificacion();
				limpiarFormCronograma();
				deshabilitarModalCrearNuevaPlanificacion();
			}
		
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irCronogramasInformatica";
	}

	public String crearPeriodoAcademicoIdiomas(){
		boolean retorno = false;
		
		try {
			List<MallaPeriodoDto> mallasPeriodo = cargarMallasPeriodoPorTipoPeriodo(1);
			if (mallasPeriodo != null && mallasPeriodo.size()>0) {
				apcfListMallaPeriodoDto = mallasPeriodo;	
			}else {
				mallasPeriodo = new ArrayList<>();
				
				MallaCurricularDto mlcrIngles = new MallaCurricularDto();mlcrIngles.setMlcrCrrId(CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE);
				MallaCurricularDto mlcrFrances = new MallaCurricularDto();mlcrFrances.setMlcrCrrId(CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE);
				MallaCurricularDto mlcrItaliano = new MallaCurricularDto();mlcrItaliano.setMlcrCrrId(CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE);
				MallaCurricularDto mlcrCoreano = new MallaCurricularDto();mlcrCoreano.setMlcrCrrId(CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE);
				MallaCurricularDto mlcrKichwa = new MallaCurricularDto();mlcrKichwa.setMlcrCrrId(CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE);
				
				MallaPeriodoDto mlprIngles = new MallaPeriodoDto();mlprIngles.setMlprMallaCurricularDto(mlcrIngles);
				MallaPeriodoDto mlprFrances = new MallaPeriodoDto();mlprFrances.setMlprMallaCurricularDto(mlcrFrances);
				MallaPeriodoDto mlprItaliano = new MallaPeriodoDto();mlprItaliano.setMlprMallaCurricularDto(mlcrItaliano);
				MallaPeriodoDto mlprCoreano = new MallaPeriodoDto();mlprCoreano.setMlprMallaCurricularDto(mlcrCoreano);
				MallaPeriodoDto mlprKichwa = new MallaPeriodoDto();mlprKichwa.setMlprMallaCurricularDto(mlcrKichwa);
				
				mallasPeriodo.add(mlprIngles);
				mallasPeriodo.add(mlprFrances);
				mallasPeriodo.add(mlprItaliano);
				mallasPeriodo.add(mlprCoreano);
				mallasPeriodo.add(mlprKichwa);
				apcfListMallaPeriodoDto = mallasPeriodo;
			}
			
			retorno = servPlanificacionCronograma.crearNuevaPlanificacion(apcfPlanificacionCronogramaDto, apcfListPlanificacionCronogramaDto, apcfListMallaPeriodoDto);
			if (retorno) {
				FacesUtil.mensajeInfo("Período académico registrado con éxito.");
				limpiarFormNuevaPlanificacion();
				limpiarFormCronograma();
				deshabilitarModalCrearNuevaPlanificacion();
			}
		
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irCronogramasIdiomas";
	}
	
	public String actualizarPlanificacion(){
		if (validarFechasPeriodoAcademico()) {
			if (apcfPlanificacionCronogramaDto.getPlcrFechaFinal().after(apcfPlanificacionCronogramaDto.getPlcrFechaInicial())) {
				try {
					servPlanificacionCronograma.editarPlanificacion(apcfPlanificacionCronogramaDto);
					FacesUtil.mensajeInfo("La actualización fue ejecutada con éxito.");
					limpiarFormNuevaPlanificacion();
					limpiarFormCronograma();
					return "irCronogramasInformatica";
				} catch (PlanificacionCronogramaException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				} catch (PlanificacionCronogramaValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				}
			}else {
				FacesUtil.mensajeInfo("La fecha final debe ser mayor a la inicial.");
				return null;
			}
		}else {
			return null;
		}
	}
	
	public String actualizarPlanificacionIdiomas(){
		if (validarFechasPeriodoAcademico()) {
			if (apcfPlanificacionCronogramaDto.getPlcrFechaFinal().after(apcfPlanificacionCronogramaDto.getPlcrFechaInicial())) {
				try {
					servPlanificacionCronograma.editarPlanificacion(apcfPlanificacionCronogramaDto);
					FacesUtil.mensajeInfo("La actualización fue ejecutada con éxito.");
					limpiarFormNuevaPlanificacion();
					limpiarFormCronograma();
					return "irCronogramasIdiomas";
				} catch (PlanificacionCronogramaException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				} catch (PlanificacionCronogramaValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				}
			}else {
				FacesUtil.mensajeInfo("La fecha final debe ser mayor a la inicial.");
				return null;
			}
		}else {
			return null;
		}
	}
	
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	

	public void limpiarFormCronograma(){
		apcfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apcfEstadoId = GeneralesConstantes.APP_ID_BASE;
		apcfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		apcfListPlanificacionCronogramaDto = null;
	}
	
	private void limpiarFormNuevaPlanificacion(){
		apcfPlanificacionCronogramaDto = null;
		apcfListPlanificacionCronogramaDto = null;
	}
	
	private void iniciarVariablesFormNuevaPlanificacion(){
		apcfListModalidadDto = cargarModalidadesInformatica();
		apcfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apcfActivarModal = GeneralesConstantes.APP_ID_BASE;

		apcfPlanificacionCronogramaDto = new PlanificacionCronogramaDto();
		apcfPlanificacionCronogramaDto.setPlcrPeriodoAcademicoDto(new PeriodoAcademicoDto());
		apcfPlanificacionCronogramaDto.setPlcrCronogramaDto(new CronogramaDto());
		apcfListPlanificacionCronogramaDto = null; 
	}

	private void iniciarVariablesFormNuevaPlanificacionIdiomas(){
		apcfActivarModal = GeneralesConstantes.APP_ID_BASE;

		apcfPlanificacionCronogramaDto = new PlanificacionCronogramaDto();
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		periodo.setPracFechaIncio(GeneralesUtilidades.getFechaActualSistema());
		periodo.setPracFechaFin(GeneralesUtilidades.getFechaActualSistema());
		periodo.setPracTipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
		periodo.setPracEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		
		CronogramaDto cronograma = new CronogramaDto();
		cronograma.setCrnDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " " + CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_LABEL);
		cronograma.setCrnTipo(CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE);
		cronograma.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);

		apcfPlanificacionCronogramaDto.setPlcrPeriodoAcademicoDto(periodo);
		apcfPlanificacionCronogramaDto.setPlcrCronogramaDto(cronograma);
		apcfListPlanificacionCronogramaDto = cargarProcesosFlujoPlantillaIdiomas(); 
	}
	
	private void vaciarVariables(){
		apcfUsuario = null;
		apcfModalidadId= null;
		apcfEstadoId= null;
		apcfActivarModal= null;
		apcfPlanificacionCronogramaDto= null;
		apcfListModalidadDto= null;
		apcfListEstado= null;
		apcfListPlanificacionCronogramaDto= null;

	}
	
	
	/**
	 * Método para buscar la entidad deseada con los parámetros de búsqueda ingresados
	 */
	public void buscarPlanificacion(){
		List<PlanificacionCronogramaDto> cronogramas = cargarProcesosFlujoActivosInformatica();

		if (apcfModalidadId != GeneralesConstantes.APP_ID_BASE) {
			List<PlanificacionCronogramaDto> aux = new ArrayList<>();	
			for (PlanificacionCronogramaDto item : cronogramas) {
				if (item.getPlcrPeriodoAcademicoDto().getPracTipo() == getPeriodoAcademicoTipo()) {
					aux.add(item);
				}
			}
			apcfListPlanificacionCronogramaDto = aux;
		}else {
			apcfListPlanificacionCronogramaDto = cronogramas;
		}
	}
	
	/**
	 * Método para buscar la entidad deseada con los parámetros de búsqueda ingresados
	 */
	public void buscarPlanificacionIdiomas(){
		apcfListPlanificacionCronogramaDto = null;
		if (apcfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			apcfListPlanificacionCronogramaDto = cargarProcesosFlujoPorPeriodoIdiomas(apcfPeriodoId);
		}else {
			apcfListPlanificacionCronogramaDto = cargarProcesosFlujoActivosIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		}
		
	}
	
	private List<MallaPeriodoDto> cargarMallasPeriodoPorTipoPeriodo(int tipoPeriodo){
		List<MallaPeriodoDto>  retorno = null;
		
		try {
			if (tipoPeriodo == 1) {
				retorno = servJdbcMallaPeriodoDto.buscarMallasPorTipoPeriodo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);	
			}else if (tipoPeriodo == 2) {
				retorno = servJdbcMallaPeriodoDto.buscarMallasPorTipoPeriodo(getPeriodoAcademicoTipo(), MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
			}
			
		} catch (MallaPeriodoException e) {
			FacesUtil.mensajeError(e.getMessage());
			retorno = null;
		} catch (MallaPeriodoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			retorno = null;
		}
		
		return retorno;
	}
	
//	private MallaPeriodoDto cargarMallaPeriodoPorTipoPeriodo(){
//		MallaPeriodoDto retorno = null;
//		
//		try {
//			retorno = servJdbcMallaPeriodoDto.buscarMallaPeriodo(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE, getPeriodoAcademicoTipo(), MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
//		} catch (MallaPeriodoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//			retorno = null;
//		} catch (MallaPeriodoNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//			retorno = null;
//		}
//		
//		return retorno;
//	}
	
	public void generarCodigoPeriodoInformatica(){
		apcfListPlanificacionCronogramaDto = new ArrayList<>();
		apcfPlanificacionCronogramaDto = new PlanificacionCronogramaDto();
		
		PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
		CronogramaDto cronograma = new CronogramaDto();
		PlanificacionCronogramaDto plcrMatricula = new PlanificacionCronogramaDto();ProcesoFlujoDto prflMatricula = new ProcesoFlujoDto();
		PlanificacionCronogramaDto plcrInicioClases = new PlanificacionCronogramaDto();ProcesoFlujoDto prflInicioClases = new ProcesoFlujoDto();
		PlanificacionCronogramaDto plcrCambioParalelo = new PlanificacionCronogramaDto();ProcesoFlujoDto prflCambioParalelo = new ProcesoFlujoDto();
		PlanificacionCronogramaDto plcrRegistroNotas = new PlanificacionCronogramaDto();ProcesoFlujoDto prflRegistroNotas = new ProcesoFlujoDto();

		
		if (apcfModalidadId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			switch (apcfModalidadId) {
			case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
				periodo.setPracTipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE);
				periodo.setPracDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " INFORMÁTICA - " + ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL);
				prflMatricula.setPrflId(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_REGULARES_VALUE);
				prflMatricula.setPrflDescripcion(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_REGULARES_LABEL);
				cronograma.setCrnDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " " + CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_LABEL);
				cronograma.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
				cronograma.setCrnTipo(CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_VALUE);
				break;
			case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
				periodo.setPracTipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE);
				periodo.setPracDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " INFORMÁTICA - " + ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL);
				prflMatricula.setPrflId(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_INTENSIVOS_VALUE);
				prflMatricula.setPrflDescripcion(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_INTENSIVOS_LABEL);
				cronograma.setCrnDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " " + CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_INTENSIVO_LABEL);
				cronograma.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
				cronograma.setCrnTipo(CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE);
				break;
			case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
				periodo.setPracTipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE);
				periodo.setPracDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " INFORMÁTICA - " + ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL);
				prflMatricula.setPrflId(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_VALUE);
				prflMatricula.setPrflDescripcion(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_LABEL);
				cronograma.setCrnDescripcion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "YYYY") + " " + CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_EXONERACION_LABEL);
				cronograma.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
				cronograma.setCrnTipo(CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE);
				break;
			default:
				break;
			}
			
			periodo.setPracFechaIncio(GeneralesUtilidades.getFechaActualSistema());
			periodo.setPracFechaFin(GeneralesUtilidades.getFechaActualSistema());
			periodo.setPracEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			apcfPlanificacionCronogramaDto.setPlcrPeriodoAcademicoDto(periodo);
			apcfPlanificacionCronogramaDto.setPlcrCronogramaDto(cronograma);
			
			prflCambioParalelo.setPrflId(ProcesoFlujoConstantes.PROCESO_FLUJO_REAJUSTE_VALUE);
			prflCambioParalelo.setPrflDescripcion(ProcesoFlujoConstantes.PRFL_INFORMATICA_CAMBIO_PARALELO_LABEL);
			plcrCambioParalelo.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
			plcrCambioParalelo.setPlcrProcesoFlujoDto(prflCambioParalelo);
			
			prflInicioClases.setPrflId(ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
			prflInicioClases.setPrflDescripcion(ProcesoFlujoConstantes.PROCESO_FLUJO_REAJUSTE_LABEL);
			plcrInicioClases.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
			plcrInicioClases.setPlcrProcesoFlujoDto(prflInicioClases);
			
			plcrMatricula.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
			plcrMatricula.setPlcrProcesoFlujoDto(prflMatricula);
			
			prflRegistroNotas.setPrflId(ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);
			prflRegistroNotas.setPrflDescripcion(ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_LABEL);
			plcrRegistroNotas.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
			plcrRegistroNotas.setPlcrProcesoFlujoDto(prflRegistroNotas);
			
			
			apcfListPlanificacionCronogramaDto.add(plcrInicioClases);
			apcfListPlanificacionCronogramaDto.add(plcrCambioParalelo);
			apcfListPlanificacionCronogramaDto.add(plcrMatricula);
			apcfListPlanificacionCronogramaDto.add(plcrRegistroNotas);

		}else {
			apcfListPlanificacionCronogramaDto = null;
			apcfPlanificacionCronogramaDto = null;
		}
		
	}
	
	public static String getNumberWithZeros(int number) {
		return String.format("%03d", number);
	}
	
	public void habilitarModalCrearNuevaPlanificacion(){
		if (validarCampoObligatoriosForm()) {
			apcfActivarModal = 1;	
		}
	}
	
	public void deshabilitarModalCrearNuevaPlanificacion(){
		apcfActivarModal = 0;
	}
	
	public void habilitarModalCrearNuevaPlanificacionIdiomas(){
		if (validarCampoObligatoriosIdiomasForm()) {
			apcfActivarModal = 1;	
		}
	}
	
	
	
	public String etiquetarPeriodoAcademicoEstados(Integer estado){
		String retorno = "  ";
		
		if (estado != null) {
			switch (estado) {
			case PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE:
				retorno = PeriodoAcademicoConstantes.ESTADO_ACTIVO_LABEL;
				break;
			case PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE:
				retorno = PeriodoAcademicoConstantes.ESTADO_INACTIVO_LABEL;
				break;
			case PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE:
				retorno = PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_LABEL;
				break;
			}
		}
		
		return retorno;
	}
	
	public String etiquetarPlanificacionCronoramaEstados(Integer estado){
		String retorno = "  ";
		
		if (estado != null) {
			switch (estado) {
			case PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE:
				retorno = PlanificacionCronogramaConstantes.ESTADO_ACTIVO_LABEL;
				break;
			case PlanificacionCronogramaConstantes.ESTADO_INACTIVO_VALUE:
				retorno = PeriodoAcademicoConstantes.ESTADO_INACTIVO_LABEL;
				break;
			}
		}
			
		return retorno;
	}
	
	private int getPeriodoAcademicoTipo(){
		Integer retorno = null;
		
		switch (apcfModalidadId) {
		case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
			retorno = PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
			retorno = PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
			retorno = PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE;
			break;
		}
		
		return retorno;
	}
	
	private List<ModalidadDto> cargarModalidadesInformatica(){
		List<ModalidadDto>  retorno = new ArrayList<>();
//		retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE, ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL));
		retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE, ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL));
		retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE, ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL));
		return retorno;
	}
	
	private List<SelectItem> cargarEstadosIdiomas(){
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,PeriodoAcademicoConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE,PeriodoAcademicoConstantes.ESTADO_INACTIVO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE,PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_LABEL));
		return retorno;
	}
	
	private List<PlanificacionCronogramaDto> cargarProcesosFlujoActivosInformatica(){
		List<PlanificacionCronogramaDto>  retorno = null;
		
		try {
			retorno = servJdbcPlanificacionCronogramaDto.buscarCronogramaProcesos(new Integer[]{PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE,PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE}, new Integer[]{CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE}, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró procesos activos en ninguna modalidad.");
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	private List<PlanificacionCronogramaDto> cargarProcesosFlujoActivosIdiomas(int pracEstado){
		List<PlanificacionCronogramaDto>  retorno = null;
		
		try {
			retorno = servJdbcPlanificacionCronogramaDto.buscarCronogramaProcesos(new Integer[]{PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE}, new Integer[]{CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE}, pracEstado);
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró resultados con los parámetros ingresados.");
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<PlanificacionCronogramaDto> cargarProcesosFlujoPorPeriodoIdiomas(int periodoId){
		List<PlanificacionCronogramaDto>  retorno = null;
		
		try {
			retorno = servJdbcPlanificacionCronogramaDto.buscarCronogramaProcesosPorPeriodo(periodoId);
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró resultados con los parámetros ingresados.");
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<PlanificacionCronogramaDto> cargarProcesosFlujoPlantillaIdiomas(){
		List<PlanificacionCronogramaDto>  retorno = null;
		
		try {
			List<ProcesoFlujo> procesos = servProcesoFlujo.listarTodos();
			if (procesos != null && procesos.size() > 0) {
				
				retorno = new ArrayList<>();
				
				for (ProcesoFlujo item : procesos) {
					if (item.getPrflId() == ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE
									  || item.getPrflId() == ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE
									  || item.getPrflId() == ProcesoFlujoConstantes.PROCESO_FLUJO_REAJUSTE_VALUE
									  || item.getPrflId() == ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE
									  || item.getPrflId() == ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE) {
						
						PlanificacionCronogramaDto planificacion = new PlanificacionCronogramaDto();
						ProcesoFlujoDto proceso = new ProcesoFlujoDto();
						proceso.setPrflId(item.getPrflId());
						proceso.setPrflDescripcion(item.getPrflDescripcion());
						planificacion.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
						planificacion.setPlcrProcesoFlujoDto(proceso);
						retorno.add(planificacion);
					}
				}
			}
			
		} catch (ProcesoFlujoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ProcesoFlujoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return  retorno;
	}
	
	private boolean validarCampoObligatoriosForm(){
		
		if (!validarModalidad()) {
			return false;
		}
		
		if (!validarFechasPeriodoAcademico()) {
			return false;
		}
		
		if (!validarFechasPlanificacion()) {
			return false;
		}
		
		return true;
	}

	
	private boolean validarCampoObligatoriosIdiomasForm(){
		
		if (!validarDescripcionPeriodoAcademico()) {
			return false;
		}
		
		if (!validarFechasPeriodoAcademico()) {
			return false;
		}
		
		if (!validarFechasPlanificacion()) {
			return false;
		}
		
		return true;
	}
	
	private boolean validarDescripcionPeriodoAcademico() {
		if (apcfPlanificacionCronogramaDto.getPlcrPeriodoAcademicoDto()== null || apcfPlanificacionCronogramaDto.getPlcrPeriodoAcademicoDto().getPracDescripcion().length() < 1) {
			FacesUtil.mensajeInfo("Ingrese la descripción del Período Académico.");
			return false;
		}
		return true;
	}
	

	private boolean validarModalidad() {
		if (apcfModalidadId.intValue() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeInfo("Seleccione una Modalidad para continuar.");
			return false;
		}
		return true;
	}
	
	private boolean validarFechasPeriodoAcademico() {
		if (apcfPlanificacionCronogramaDto.getPlcrPeriodoAcademicoDto().getPracFechaIncio() != null) {
			if (apcfPlanificacionCronogramaDto.getPlcrPeriodoAcademicoDto().getPracFechaFin() != null) {
				if (apcfPlanificacionCronogramaDto.getPlcrPeriodoAcademicoDto().getPracFechaFin().after(apcfPlanificacionCronogramaDto.getPlcrPeriodoAcademicoDto().getPracFechaIncio())) {
					
				}else {
					FacesUtil.mensajeInfo("La fecha inicial debe ser menor a la fecha final -> período académico.");
					return false;	
				}
			}else {
				FacesUtil.mensajeInfo("Ingrese la fecha final del período académico.");
				return false;
			}
		}else {
			FacesUtil.mensajeInfo("Ingrese la fecha inicial del período académico.");
			return false;
		}

		return true;
	}
	
	private boolean validarFechasPlanificacion(){
		
		for (PlanificacionCronogramaDto item : apcfListPlanificacionCronogramaDto) {
			if (item.getPlcrFechaInicial()!= null) {
				if (item.getPlcrFechaFinal() != null) {
					// comparar fechas - final despues de la inicial
					if (item.getPlcrFechaInicial().after(item.getPlcrFechaFinal())) {
						FacesUtil.mensajeInfo("La fecha inicial debe ser menor a la fecha final. Proceso " + item.getPlcrProcesoFlujoDto().getPrflDescripcion());
						return false;	
					}
				}else {
					FacesUtil.mensajeInfo("Ingrese la fecha final del proceso " + item.getPlcrProcesoFlujoDto().getPrflDescripcion());
					return false;	
				}
			}else {
				FacesUtil.mensajeInfo("Ingrese la fecha inicial del proceso " + item.getPlcrProcesoFlujoDto().getPrflDescripcion());
				return false;
			}
		}
		
		return true;
	}
	
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	

	public Usuario getApcfUsuario() {
		return apcfUsuario;
	}

	public void setApcfUsuario(Usuario apcfUsuario) {
		this.apcfUsuario = apcfUsuario;
	}

	public Integer getApcfModalidadId() {
		return apcfModalidadId;
	}

	public void setApcfModalidadId(Integer apcfModalidadId) {
		this.apcfModalidadId = apcfModalidadId;
	}

	public Integer getApcfEstadoId() {
		return apcfEstadoId;
	}

	public void setApcfEstadoId(Integer apcfEstadoId) {
		this.apcfEstadoId = apcfEstadoId;
	}

	public List<ModalidadDto> getApcfListModalidadDto() {
		return apcfListModalidadDto;
	}

	public void setApcfListModalidadDto(List<ModalidadDto> apcfListModalidadDto) {
		this.apcfListModalidadDto = apcfListModalidadDto;
	}

	public List<SelectItem> getApcfListEstado() {
		return apcfListEstado;
	}

	public void setApcfListEstado(List<SelectItem> apcfListEstado) {
		this.apcfListEstado = apcfListEstado;
	}


	public List<PlanificacionCronogramaDto> getApcfListPlanificacionCronogramaDto() {
		return apcfListPlanificacionCronogramaDto;
	}


	public void setApcfListPlanificacionCronogramaDto(List<PlanificacionCronogramaDto> apcfListPlanificacionCronogramaDto) {
		this.apcfListPlanificacionCronogramaDto = apcfListPlanificacionCronogramaDto;
	}


	public PlanificacionCronogramaDto getApcfPlanificacionCronogramaDto() {
		return apcfPlanificacionCronogramaDto;
	}


	public void setApcfPlanificacionCronogramaDto(PlanificacionCronogramaDto apcfPlanificacionCronogramaDto) {
		this.apcfPlanificacionCronogramaDto = apcfPlanificacionCronogramaDto;
	}


	public Integer getApcfActivarModal() {
		return apcfActivarModal;
	}


	public void setApcfActivarModal(Integer apcfActivarModal) {
		this.apcfActivarModal = apcfActivarModal;
	}


	public MallaPeriodoDto getApcfMallaPeriodoDto() {
		return apcfMallaPeriodoDto;
	}


	public void setApcfMallaPeriodoDto(MallaPeriodoDto apcfMallaPeriodoDto) {
		this.apcfMallaPeriodoDto = apcfMallaPeriodoDto;
	}

	public List<MallaPeriodoDto> getApcfListMallaPeriodoDto() {
		return apcfListMallaPeriodoDto;
	}

	public void setApcfListMallaPeriodoDto(List<MallaPeriodoDto> apcfListMallaPeriodoDto) {
		this.apcfListMallaPeriodoDto = apcfListMallaPeriodoDto;
	}

	public List<PeriodoAcademicoDto> getApcfListPeriodoAcademicoDto() {
		return apcfListPeriodoAcademicoDto;
	}

	public void setApcfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> apcfListPeriodoAcademicoDto) {
		this.apcfListPeriodoAcademicoDto = apcfListPeriodoAcademicoDto;
	}

	public Integer getApcfPeriodoId() {
		return apcfPeriodoId;
	}

	public void setApcfPeriodoId(Integer apcfPeriodoId) {
		this.apcfPeriodoId = apcfPeriodoId;
	}

	
	


	
}

