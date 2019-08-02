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
   
 ARCHIVO:     ActualizacionDocenteForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Postgrado de la calidad. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
20-10-2017		 Arturo Villafuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.docente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PuestoException;
import ec.edu.uce.academico.ejb.excepciones.PuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralException;
import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionException;
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RelacionLaboralServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TiempoDedicacionServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;
import ec.edu.uce.academico.jpa.entidades.publico.RelacionLaboral;
import ec.edu.uce.academico.jpa.entidades.publico.TiempoDedicacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) ActualizacionDocenteForm.java Bean de sesión que maneja
 * los atributos del formulario de Postgrado de la calidad.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "actualizacionDocenteForm")
@SessionScoped
public class ActualizacionDocenteForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private String adfPrimerApellido;
	private String adfIdentificacion;
	
	private Integer adfTipoBusqueda;
	private Integer adfRelacionLaboralId;
	private Integer adfCategoriaId;
	private Integer adfTiempoDedicacionId;
	private Integer adfRangoGradualId;
	private Integer adfDependenciaId;
	private Integer adfCarreraId;
	
	private Boolean adfRenderRangoGradual;
	
	private PersonaDatosDto adfPersonaDatosDto;
	
	private List<PersonaDatosDto> adfListPersonaDatosDto;
	private List<Dependencia> adfListDependencia;
	private List<Carrera> adfListCarrera;
	private List<RelacionLaboral> adfListRelacionLaboral;
	private List<TiempoDedicacion> adfListTiempoDedicacion;
	private List<Puesto> adfListCategoria;
	private List<Puesto> adfListNivelRangoGradual;
	
	
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB	private DependenciaServicio servDependencia;
	@EJB	private CarreraServicio servCarrera;
	@EJB	private PersonaDatosDtoServicioJdbc servJdbcPersonaDatos;
	@EJB	private PuestoServicio servPuesto;
	@EJB	private TiempoDedicacionServicio servTiempoDedicacion;
	@EJB	private DetallePuestoServicio servDetallePuesto;
	@EJB	private RelacionLaboralServicio servRelacionLaboral;  
	
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	public String irFormularioActualizacionDocente(Usuario usuario) {
		
		adfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		adfRelacionLaboralId = GeneralesConstantes.APP_ID_BASE;
		adfCategoriaId = GeneralesConstantes.APP_ID_BASE;
		adfTiempoDedicacionId = GeneralesConstantes.APP_ID_BASE;
		adfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		adfCarreraId = GeneralesConstantes.APP_ID_BASE;
		
		adfPrimerApellido = new String();
		adfIdentificacion = new String();

		adfListPersonaDatosDto = new ArrayList<>();
		adfListRelacionLaboral = new ArrayList<>();
		adfListTiempoDedicacion = new ArrayList<>();
		adfListCategoria = new ArrayList<>();
		adfListNivelRangoGradual = new ArrayList<>();
		adfListDependencia = new ArrayList<>();
		adfListCarrera = new ArrayList<>();

		adfRenderRangoGradual = false;
		
		adfPersonaDatosDto = null;
		
		return "irFormularioBuscarDocente";
	}

	
	public String irInicio() {
		
		adfPrimerApellido = null;
		adfIdentificacion = null;
		
		adfTipoBusqueda = null;
		adfRelacionLaboralId = null;
		adfCategoriaId = null;
		adfTiempoDedicacionId = null;
		adfRangoGradualId = null;
		adfDependenciaId = null;
		adfCarreraId = null;
		
		adfRenderRangoGradual = null;
		
		adfPersonaDatosDto = null;
		
		adfListPersonaDatosDto = null; 
		adfListDependencia = null;
		adfListCarrera = null;
		adfListRelacionLaboral = null;
		adfListTiempoDedicacion = null;
		adfListCategoria = null;
		adfListNivelRangoGradual = null;
		
		return "irInicio";
	}
	
	public String irFormBuscarDocentes() {
		limpiarFormActualizacionPuesto();
		return "irFormBuscarDocentes";
	}
	
	private void limpiarFormActualizacionPuesto(){
		adfRelacionLaboralId = GeneralesConstantes.APP_ID_BASE;
		adfCategoriaId = GeneralesConstantes.APP_ID_BASE;
		adfTiempoDedicacionId = GeneralesConstantes.APP_ID_BASE;
		adfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		adfCarreraId = GeneralesConstantes.APP_ID_BASE;
		
		adfListRelacionLaboral = new ArrayList<>();
		adfListTiempoDedicacion = new ArrayList<>();
		adfListCategoria = new ArrayList<>();
		adfListNivelRangoGradual = new ArrayList<>();
		adfListCarrera = new ArrayList<>();

		adfRenderRangoGradual = false;
	}
	
	
	public void buscarDocentesPorTipoDeBusqueda() {
		
		if (!adfTipoBusqueda.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			if (adfTipoBusqueda.equals(GeneralesConstantes.APP_FIND_BY_IDENTIFICACION)) {
				adfListPersonaDatosDto = cargarDetallesPuestoDocente(adfIdentificacion, adfTipoBusqueda);	
			}else {
				adfListPersonaDatosDto = cargarDetallesPuestoDocente(adfPrimerApellido, adfTipoBusqueda);
			}
			
			if (adfListPersonaDatosDto.isEmpty()) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del docente para continuar.");;
		}
		
	}
	
	public void limpiarFormDocentes() {
		
		adfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
				
		adfPrimerApellido = new String();
		adfIdentificacion = new String();

		adfListPersonaDatosDto = new ArrayList<>();
		adfRenderRangoGradual = false;
		
		adfPersonaDatosDto = null;
		
	}
	

	private List<PersonaDatosDto> cargarDetallesPuestoDocente(String param, int tipoBusqueda) {
		List<PersonaDatosDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcPersonaDatos.buscarDocentesPorFacultadPeriodoMultipleNivel(param, tipoBusqueda);
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
		}
		
		return retorno;
	}


	public String  irActualizarDetallePuesto(PersonaDatosDto docente){
		String retorno = "irActualizarDetallePuesto";
		
		adfPersonaDatosDto = docente;
		
		adfListRelacionLaboral = cargarListRelacionLaboral();
		adfListTiempoDedicacion = cargarListTiempoDedicacion();
		adfListDependencia = cargarDependenciasPorNivelAcademico(docente.getPrsCarreraDto().getCrrTipo());
		
		adfRelacionLaboralId = docente.getRllbId();
		adfTiempoDedicacionId = docente.getTmddId();
		adfListCategoria = cargarListCategoria(docente.getTmddId());
		adfCategoriaId = docente.getPstCategoria();
		adfListNivelRangoGradual = cargarListRangoGradual(adfTiempoDedicacionId, adfCategoriaId);
		
		if (!adfListNivelRangoGradual.isEmpty()) {
			adfRangoGradualId = docente.getPstNivelRangoGradual();
			adfRenderRangoGradual = true;
		}
		
		return retorno;
	}
	

	private List<Puesto> cargarListCategoria(Integer tiempoDedicacionId) {
		List<Puesto>  retorno = new ArrayList<>();

		try {
			retorno = servPuesto.buscarPorTiempoDedicacion(tiempoDedicacionId);
		} catch (PuestoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PuestoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	private List<RelacionLaboral> cargarListRelacionLaboral() {
		List<RelacionLaboral>  retorno = new ArrayList<>();

		try{
			retorno = servRelacionLaboral.listarTodos();
		} catch (RelacionLaboralNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RelacionLaboralException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		
		return retorno;
	}
	
	private List<TiempoDedicacion> cargarListTiempoDedicacion() {
		List<TiempoDedicacion>  retorno = new ArrayList<>();

		try{
			retorno = servTiempoDedicacion.listarTodos();
		} catch (TiempoDedicacionNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.iniciar.datos.tiempo.dedicacion.no.encontrado.exception")));
		} catch (TiempoDedicacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.iniciar.datos.tiempo.dedicacion.exception")));
		} 
		
		return retorno;
	}


	private List<Dependencia> cargarDependenciasPorNivelAcademico(Integer nivelId) {
		List<Dependencia> retorno = new ArrayList<>();
		
		try {
			retorno = servDependencia.buscarDependencias(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, nivelId);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	
	
	public void buscarCarreras(){
		if (!adfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			adfListCarrera = cargarCarrerasPorDependencia(adfDependenciaId, adfPersonaDatosDto.getPrsCarreraDto().getCrrTipo());
		}else {
			FacesUtil.mensajeInfo("Seleccione una dependencia para continuar.");
		}
		
	}
	
	public void  limpiarModalAgregarCarrera(){
		adfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		adfListCarrera = new ArrayList<>();
	}
	
	public void buscarCategorias(){
		adfCategoriaId = GeneralesConstantes.APP_ID_BASE;
		adfRangoGradualId = GeneralesConstantes.APP_ID_BASE;
		adfListCategoria = new ArrayList<>();
		adfListNivelRangoGradual = new ArrayList<>();
		adfRenderRangoGradual = false;
		
		if (!adfTiempoDedicacionId.equals(GeneralesConstantes.APP_ID_BASE)) {
			adfListCategoria = cargarListCategoria(adfTiempoDedicacionId);
		}else {
			FacesUtil.mensajeInfo("Seleccione una opción en tiempo de dedicación para continuar.");
		}
	}
	
	
	public void buscarRangoGradual(){
		adfListNivelRangoGradual = new ArrayList<>();
		adfRangoGradualId = GeneralesConstantes.APP_ID_BASE;
		adfRenderRangoGradual = false;
		
		if (!adfTiempoDedicacionId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!adfCategoriaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			
				adfListNivelRangoGradual = cargarListRangoGradual(adfTiempoDedicacionId, adfCategoriaId);
				FacesUtil.limpiarMensaje();
				if (!adfListNivelRangoGradual.isEmpty()) {
					adfRenderRangoGradual = true;
				}
				
			}else {
				FacesUtil.mensajeInfo("Seleccione una opción en categoria para continuar.");
			}
		}else {
			FacesUtil.mensajeInfo("Seleccione una opción en tiempo de dedicación para continuar.");
		}
	}
	
	private List<Puesto> cargarListRangoGradual(Integer tiempoDedicacionId, Integer categoriaId) {
		List<Puesto> retorno = new ArrayList<>();
		
		try {
			retorno = servPuesto.buscarPorTiempoDedicacionCategoria(tiempoDedicacionId, categoriaId);
		} catch (PuestoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PuestoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	
	
	public void establecerCarrera(Carrera carrera){
		adfPersonaDatosDto.setPrsCarreraDto(new CarreraDto(carrera.getCrrId(), carrera.getCrrDescripcion()));
		adfPersonaDatosDto.getPrsCarreraDto().setCrrTipo(carrera.getCrrTipo());
	}
	
	
	
	private List<Carrera> cargarCarrerasPorDependencia(Integer dependenciaId, Integer nivelId) {
		List<Carrera> retorno = new ArrayList<>();
		try {
			retorno = servCarrera.buscarCarreras(dependenciaId, nivelId);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}



	
	
	
	/**
	 * Actualiza los parametros del formulario
	 **/
	public String guardarCambios() {

		String retorno = "";
		DetallePuesto detalle = new DetallePuesto();
		Carrera carrera = null;
		RelacionLaboral relacionLaboral = null;
		Puesto puesto = null;
		
		if (!adfRelacionLaboralId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!adfTiempoDedicacionId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!adfCategoriaId.equals(GeneralesConstantes.APP_ID_BASE)) {
					
					boolean actualizar = false;
					if (adfListNivelRangoGradual.isEmpty()) {
						actualizar = true;
					}else {
						if (!adfRangoGradualId.equals(GeneralesConstantes.APP_ID_BASE)) {
							actualizar = true;
						}else {
							FacesUtil.mensajeError("Seleccione un opción en rango gradual para continuar.");	
						}
					}
					
					if (actualizar) {
						
						try { 

							carrera = servCarrera.buscarPorId(adfPersonaDatosDto.getPrsCarreraDto().getCrrId());
							relacionLaboral = servRelacionLaboral.buscarPorId(adfRelacionLaboralId);
							puesto = servPuesto.buscarPuesto(adfTiempoDedicacionId, adfCategoriaId, adfRangoGradualId);
						
							detalle.setDtpsId(adfPersonaDatosDto.getDtpsId());
							detalle.setDtpsCarrera(carrera);
							detalle.setDtpsRelacionLaboral(relacionLaboral);
							detalle.setDtpsPuesto(puesto);			

							if(puesto != null && relacionLaboral != null && carrera != null){
								if(servDetallePuesto.editar(detalle)){
									limpiarFormActualizacionPuesto();
									adfListPersonaDatosDto = cargarDetallesPuestoDocente(adfPersonaDatosDto.getPrsIdentificacion(), GeneralesConstantes.APP_FIND_BY_IDENTIFICACION);
									retorno = "irFormBuscarDocentes";
									FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.guardar.cambios.con.exito.validacion")));
								}else{
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.guardar.cambios.exception")));
								}
							} 
							
						} catch (PuestoNoEncontradoException e) {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.guardar.cambios.puesto.no.encontrado.exception")));
						} catch (PuestoException e) {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.guardar.cambios.puesto.exception")));
						} catch (DetallePuestoException e) {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.guardar.cambios.detalle.puesto.no.encontrado.exception")));
						} catch (DetallePuestoValidacionException e) {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionDocente.guardar.cambios.detalle.puesto.validacion.exception")));
						} catch (RelacionLaboralNoEncontradoException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (RelacionLaboralException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (CarreraNoEncontradoException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (CarreraException e) {
							FacesUtil.mensajeError(e.getMessage());
						}
					}
					
				}else {
					FacesUtil.mensajeError("Seleccione un opción en categoría para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione un opción en tiempo de dedicación para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un opción en relación laboral para continuar.");
		}
		return retorno;
	}

	 

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	/**
	 * Lista de Entidades Docente por categoria
	 **/
	public void listarCategoria() {
			 
	}
	
	public void busquedaPorIdentificacion(){
		
		if (adfIdentificacion.length() > 0) {
			adfPrimerApellido = new String();
			adfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (adfPrimerApellido.length() > 0) {
			adfIdentificacion = new String();
			adfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	 
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/


	public String getAdfPrimerApellido() {
		return adfPrimerApellido;
	}

	public void setAdfPrimerApellido(String adfPrimerApellido) {
		this.adfPrimerApellido = adfPrimerApellido;
	}

	public String getAdfIdentificacion() {
		return adfIdentificacion;
	}

	public void setAdfIdentificacion(String adfIdentificacion) {
		this.adfIdentificacion = adfIdentificacion;
	}

	public Integer getAdfTipoBusqueda() {
		return adfTipoBusqueda;
	}

	public void setAdfTipoBusqueda(Integer adfTipoBusqueda) {
		this.adfTipoBusqueda = adfTipoBusqueda;
	}

	public List<PersonaDatosDto> getAdfListPersonaDatosDto() {
		return adfListPersonaDatosDto;
	}

	public void setAdfListPersonaDatosDto(List<PersonaDatosDto> adfListPersonaDatosDto) {
		this.adfListPersonaDatosDto = adfListPersonaDatosDto;
	}


	public PersonaDatosDto getAdfPersonaDatosDto() {
		return adfPersonaDatosDto;
	}

	public void setAdfPersonaDatosDto(PersonaDatosDto adfPersonaDatosDto) {
		this.adfPersonaDatosDto = adfPersonaDatosDto;
	}


	public Integer getAdfRelacionLaboralId() {
		return adfRelacionLaboralId;
	}


	public void setAdfRelacionLaboralId(Integer adfRelacionLaboralId) {
		this.adfRelacionLaboralId = adfRelacionLaboralId;
	}


	public Integer getAdfCategoriaId() {
		return adfCategoriaId;
	}


	public void setAdfCategoriaId(Integer adfCategoriaId) {
		this.adfCategoriaId = adfCategoriaId;
	}


	public Integer getAdfTiempoDedicacionId() {
		return adfTiempoDedicacionId;
	}


	public void setAdfTiempoDedicacionId(Integer adfTiempoDedicacionId) {
		this.adfTiempoDedicacionId = adfTiempoDedicacionId;
	}


	public List<RelacionLaboral> getAdfListRelacionLaboral() {
		return adfListRelacionLaboral;
	}


	public void setAdfListRelacionLaboral(List<RelacionLaboral> adfListRelacionLaboral) {
		this.adfListRelacionLaboral = adfListRelacionLaboral;
	}


	public List<TiempoDedicacion> getAdfListTiempoDedicacion() {
		return adfListTiempoDedicacion;
	}


	public void setAdfListTiempoDedicacion(List<TiempoDedicacion> adfListTiempoDedicacion) {
		this.adfListTiempoDedicacion = adfListTiempoDedicacion;
	}


	public Integer getAdfRangoGradualId() {
		return adfRangoGradualId;
	}


	public void setAdfRangoGradualId(Integer adfRangoGradualId) {
		this.adfRangoGradualId = adfRangoGradualId;
	}


	public Integer getAdfDependenciaId() {
		return adfDependenciaId;
	}


	public void setAdfDependenciaId(Integer adfDependenciaId) {
		this.adfDependenciaId = adfDependenciaId;
	}


	public Integer getAdfCarreraId() {
		return adfCarreraId;
	}


	public void setAdfCarreraId(Integer adfCarreraId) {
		this.adfCarreraId = adfCarreraId;
	}


	public List<Dependencia> getAdfListDependencia() {
		return adfListDependencia;
	}


	public void setAdfListDependencia(List<Dependencia> adfListDependencia) {
		this.adfListDependencia = adfListDependencia;
	}


	public List<Carrera> getAdfListCarrera() {
		return adfListCarrera;
	}


	public void setAdfListCarrera(List<Carrera> adfListCarrera) {
		this.adfListCarrera = adfListCarrera;
	}


	public List<Puesto> getAdfListCategoria() {
		return adfListCategoria;
	}

	public void setAdfListCategoria(List<Puesto> adfListCategoria) {
		this.adfListCategoria = adfListCategoria;
	}

	public List<Puesto> getAdfListNivelRangoGradual() {
		return adfListNivelRangoGradual;
	}

	public void setAdfListNivelRangoGradual(List<Puesto> adfListNivelRangoGradual) {
		this.adfListNivelRangoGradual = adfListNivelRangoGradual;
	}

	public Boolean getAdfRenderRangoGradual() {
		return adfRenderRangoGradual;
	}

	public void setAdfRenderRangoGradual(Boolean adfRenderRangoGradual) {
		this.adfRenderRangoGradual = adfRenderRangoGradual;
	}
	
 
	
	
}