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
   
 ARCHIVO:     AdministracionCronogramaPosgradoForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de la tabla Cronograma.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
06-Febrero-2018			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) AdministracionCronogramaPosgradoForm.
 * Managed Bean que maneja las peticiones para la administración de la tabla Cronograma para Posgrado.
 * @author jdalbuja.
 * @version 1.0
 */

@ManagedBean(name="administracionCronogramaPosgradoForm")
@SessionScoped
public class AdministracionCronogramaPosgradoForm implements Serializable{

	private static final long serialVersionUID = -5706899311603575398L;
	private Integer acfPeriodoId;
	private Integer acfCronogramaId;
	private String acfActivacionCrrId;
	private CronogramaDto acfCronogramaEditar;
	private List<CronogramaDto> acfListCronograma;
	private Usuario acfUsuarioBuscar;
	private Integer acfTipoUsuario;
	private String acfTipoCarrera;
	private List<PeriodoAcademicoDto> acfListPeriodoAcademico;
	private String acfPaginacionSt;
	private Integer acfValidadorClick;
	private List<SelectItem> acfListaCombos;
	private List<CarreraDto> acfListCarreraDto;
	
	@EJB private CronogramaDtoServicioJdbc servAcfCronogramaServicio;
	@EJB private UsuarioRolServicio servAcfUsuarioRolServicio;
	@EJB private PeriodoAcademicoDtoServicioJdbc servAcfPeriodoAcademicoServicio;
	@EJB private PlanificacionCronogramaServicio servAcfPlanificacionCronogramaServicio;
	@EJB private CarreraDtoServicioJdbc servAcfCarreraDtoServiciojdbc;
	
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	/**
	 * Incicializa las variables para la funcionalidad de administración de la tabla Cronograma
	 * @return navegacion al xhtml de listar Cronograma
	 */
	public String irAdministracionCronogramaPosgrado(Usuario usuario) {
		acfUsuarioBuscar = usuario;
		
		try {
			List<UsuarioRol> usro = servAcfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if( item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					acfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					acfTipoCarrera="programas";
				}
			}
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
		}
		
		iniciarParametros();
		return "irAdministracionCronogramaPosgrado";
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio
	 */
	public void iniciarParametros(){

		acfCronogramaId = GeneralesConstantes.APP_ID_BASE;
		acfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		
		acfCronogramaEditar = new CronogramaDto();
		ListasCombosForm lista = new ListasCombosForm();
		acfListCronograma = null;
		
		//inicio la lista de peridodos academicos
		if( acfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				
			try {
				acfListPeriodoAcademico = servAcfPeriodoAcademicoServicio.listarXEstadoPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				acfListaCombos = lista.getListaTipoCronogramaPregrado();
			} catch (PeriodoAcademicoDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}
		
		//inicio el string de paginacion 
		acfPaginacionSt = new String();
		//inicio el validador del click en 0
		acfValidadorClick = 0;
		
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio
	 */
	public String irInicio(){
		limpiar();
		return "irInicio"; 	
	}
	
	
	/**
	 * Método para limpiar los parámtros de busqueda ingresados
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	/**
	 * Método para buscar la entidad deseada con los parámetros de búsqueda ingresados
	 */
	public void buscar(Integer pracId, Integer crnTipo){
		//busca los procesos asociados al cronograma
		if(pracId != GeneralesConstantes.APP_ID_BASE){
				
				try {
					PeriodoAcademicoDto periodo = servAcfPeriodoAcademicoServicio.buscar(pracId);
					
					if (periodo != null) {
						if (periodo.getPracTipo().intValue() == PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE) {
								
									acfListCronograma = cargarProcesosCronograma(pracId, CronogramaConstantes.TIPO_POSGRADO_VALUE);	
									
						}
					}
					
				} catch (PeriodoAcademicoDtoJdbcException e) {
				} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
				}
				
		}else{
			FacesUtil.mensajeError("Seleccione un período académico.");
		}
	}
	
	/**
	 * Método que retorna las carreras segun el periodo seleccionado.
	 * @param crrTipo - tipo de cronograma.
	 * @return carreras con dependencias.
	 */
	public List<CarreraDto> cargarCarreras(int crrTipo){
		
		try {
			return servAcfCarreraDtoServiciojdbc.buscarCarreraDependencia(CronogramaConstantes.TIPO_ACADEMICO_VALUE);
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Método que permite cargar los procesos asignados a un periodo academico. 
	 * @param pracId - id del periodo academico.
	 * @param crnTipo - tipo de cronograma.
	 * @return los procesos asignados al cronograma.
	 */ 
	private List<CronogramaDto> cargarProcesosCronograma(int pracId, int crnTipo){
		List<CronogramaDto> cronograma = null;
		boolean ordinal = false;
		acfPeriodoId = pracId;
		acfCronogramaId = crnTipo;
		
		try {
			cronograma = servAcfCronogramaServicio.listarProcesosXPeriodoxTipo(pracId, crnTipo);
			if (cronograma != null && cronograma.size() > 0) {
				for (CronogramaDto it : cronograma) {
					if (it.getCrprflOrdinal() == null) {
						ordinal = true;
					}
				}

				if (!ordinal) {
					cronograma.sort(Comparator.comparing(CronogramaDto::getCrprflOrdinal));	
				}
			}
			return cronograma;
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return cronograma;
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Verifique que se hayan creado los procesos para el cronograma seleccionado.");
			return cronograma;
		}
	}
	
	/**
	 * Método para ir a la funcionalidad de ver un cronograma
	 * @return navegación al xhtml verCronograma
	 */
	public String irVerCronograma(CronogramaDto cronograma){
		
		try {
			
			acfActivacionCrrId = GeneralesConstantes.APP_SELECCIONE_TODOS;
			acfCronogramaEditar = servAcfCronogramaServicio.buscarPlanificacionCronogramaXPeriodoXTipoXPlanificacion(cronograma.getPracId(), cronograma.getCrnTipo(), cronograma.getPlcrId());
		
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return "irEditarCronogramaPosgrado";
	}
	
	/**
	 * Método para editar los cronogramas del periodo academico
	 */
	public String editarCronograma(){
		try {
			servAcfPlanificacionCronogramaServicio.editarCronograma(acfCronogramaEditar);
			acfListCronograma = cargarProcesosCronograma(acfPeriodoId, acfCronogramaId);
			
		} catch (PeriodoAcademicoValidacionException e) {
		} catch (PeriodoAcademicoException e) {
		} catch (PlanificacionCronogramaException e) {
		} 
		
		return "irAdministracionCronogramaPosgrado";
	}
	
	
	/**
	 * Método para cancelar la edicion o creacion de un periodo academico
	 * @return navegacion al xhtml de listar periodo academico
	 */
	public String cancelar(){
		limpiar();
		return "cancelar"; 	
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	
	
	/**
	 * setea el verificador del click a 0 para nuevas validaciones
	 */
	public void setearVerificadorClick(){
		acfValidadorClick = 0;
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	public List<CronogramaDto> getAcfListCronograma() {
		return acfListCronograma;
	}

	public void setAcfListCronograma(List<CronogramaDto> acfListCronograma) {
		this.acfListCronograma = acfListCronograma;
	}

	public Usuario getAcfUsuarioBuscar() {
		return acfUsuarioBuscar;
	}

	public void setAcfUsuarioBuscar(Usuario acfUsuarioBuscar) {
		this.acfUsuarioBuscar = acfUsuarioBuscar;
	}

	public Integer getAcfTipoUsuario() {
		return acfTipoUsuario;
	}

	public void setAcfTipoUsuario(Integer acfTipoUsuario) {
		this.acfTipoUsuario = acfTipoUsuario;
	}

	public String getAcfTipoCarrera() {
		return acfTipoCarrera;
	}

	public void setAcfTipoCarrera(String acfTipoCarrera) {
		this.acfTipoCarrera = acfTipoCarrera;
	}

	public List<PeriodoAcademicoDto> getAcfListPeriodoAcademico() {
		return acfListPeriodoAcademico;
	}

	public void setAcfListPeriodoAcademico(List<PeriodoAcademicoDto> acfListPeriodoAcademico) {
		this.acfListPeriodoAcademico = acfListPeriodoAcademico;
	}

	public String getAcfPaginacionSt() {
		return acfPaginacionSt;
	}

	public void setAcfPaginacionSt(String acfPaginacionSt) {
		this.acfPaginacionSt = acfPaginacionSt;
	}

	public Integer getAcfValidadorClick() {
		return acfValidadorClick;
	}

	public void setAcfValidadorClick(Integer acfValidadorClick) {
		this.acfValidadorClick = acfValidadorClick;
	}

	public List<SelectItem> getAcfListaCombos() {
		return acfListaCombos;
	}

	public void setAcfListaCombos(List<SelectItem> acfListaCombos) {
		this.acfListaCombos = acfListaCombos;
	}

	public CronogramaDto getAcfCronogramaEditar() {
		return acfCronogramaEditar;
	}

	public void setAcfCronogramaEditar(CronogramaDto acfCronogramaEditar) {
		this.acfCronogramaEditar = acfCronogramaEditar;
	}

	public Integer getAcfPeriodoId() {
		return acfPeriodoId;
	}

	public void setAcfPeriodoId(Integer acfPeriodoId) {
		this.acfPeriodoId = acfPeriodoId;
	}

	public Integer getAcfCronogramaId() {
		return acfCronogramaId;
	}

	public void setAcfCronogramaId(Integer acfCronogramaId) {
		this.acfCronogramaId = acfCronogramaId;
	}

	public String getAcfActivacionCrrId() {
		return acfActivacionCrrId;
	}

	public void setAcfActivacionCrrId(String acfActivacionCrrId) {
		this.acfActivacionCrrId = acfActivacionCrrId;
	}

	public List<CarreraDto> getAcfListCarreraDto() {
		return acfListCarreraDto;
	}

	public void setAcfListCarreraDto(List<CarreraDto> acfListCarreraDto) {
		this.acfListCarreraDto = acfListCarreraDto;
	}

	
}

