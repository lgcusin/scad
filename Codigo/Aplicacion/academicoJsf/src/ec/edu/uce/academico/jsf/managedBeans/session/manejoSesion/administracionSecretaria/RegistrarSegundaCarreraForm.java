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
   
 ARCHIVO:    RegistrarSegundaCarreraForm.java	  
 DESCRIPCION: Managed Bean que maneja el asignar como segunda carrera a un estudiante
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
06-AGO-2018			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSecretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) RegistrarSegundaCarreraForm. Managed Bean que maneja el asignar como segunda carrera a un estudiante
 * 
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "registrarSegundaCarreraForm")
@SessionScoped
public class RegistrarSegundaCarreraForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// Objetos
	// PANTALLA LISTAR ESTUDIANTES
	private Usuario rscfUsuario;
	private PersonaDto rscfPersonaDtoBuscar; // Guardar la identificación
	private CarreraDto rscfCarreraDtoBuscar; // Guardar la carrera
	private List<CarreraDto> rscfListCarreraDto; // Combo a seleccionar carreras
	private List<EstudianteJdbcDto> rscfListaEstudiantes; //estudiante resultado de la busqueda
	private EstudianteJdbcDto rscfEstudianteEditar; 
	private EstudianteJdbcDto rscfEstudianteGuardar;
	private Integer rscfValidadorClic; 
	private boolean rscfBloqueaCamposAdicionales; 
	private PeriodoAcademico rscfPeriodoAcademicoActivo;
	private List<InstitucionAcademicaDto> rscfListUniversidadesDto;
	private List<TituloDto> rscfListTitulosUniversidadDto;
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {

	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	// Para la busqueda

	@EJB
	CarreraDtoServicioJdbc servRscfCarreraDto;
	@EJB
	PeriodoAcademicoServicio servRscfPeriodoAcademico;
	@EJB
	EstudianteDtoServicioJdbc servRscfEstudiante;
	@EJB
	InstitucionAcademicaDtoServicioJdbc servRscfInstitucionesAcademicasDto;
	@EJB
	TituloDtoServicioJdbc servRscfTitulosDtoUniversitarios;
	@EJB
	FichaEstudianteServicio servRscfFichaEstudianteServicio;


	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al
	 * iniciar la funcionalidad
	 * 
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		return "irInicio";
	}

	/**
	 * Método que permite iniciar el registro de segunda carrera al estudiante
	 * @param usuario   - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de busqueda de estudiante
	 */

	public String irAlistarEstudSegundaCarrera(Usuario usuario) {
		rscfUsuario = usuario;
		iniciarParametros();
		return "irAlistarEstSegundaCarrera";
	}


	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
		try {

	//		rscfListPersonas = new ArrayList<>();
			// Inicio los objetos
			
			rscfPersonaDtoBuscar = new PersonaDto();
			rscfPersonaDtoBuscar.setPrsIdentificacion("");
			rscfCarreraDtoBuscar = new CarreraDto();
			// seteo la carrera para que busque por todas
			
			rscfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			// inicio la lista de Personas
			
			rscfListCarreraDto = null;
			rscfListaEstudiantes= null;
			
			// busco el periodo academico activo
			rscfPeriodoAcademicoActivo = servRscfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			// lleno las carreras/
			// rscfListCarreraDto=servRscfCarreraDto.listarXfacultad(DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE);
			rscfListCarreraDto = servRscfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPregrado(
					rscfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE,
					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rscfPeriodoAcademicoActivo.getPracId());
		
			rscfValidadorClic = new Integer(0);
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.iniciar.parametros.periodo.academico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}

	}

	/**
	 * Método para buscar las personas con los parámetros ingresados en los
	 * filtros de busqueda
	 */
	public void buscar() {
		// buscar elestudiante por Identificación y carrera
	
		rscfListaEstudiantes= new ArrayList<>(); 
			
			try {
				if((rscfPersonaDtoBuscar.getPrsIdentificacion()!=null)&&(rscfPersonaDtoBuscar.getPrsIdentificacion().trim().length() >=1)){
					rscfListaEstudiantes = servRscfEstudiante.buscarEstudianteXIdentificacionXCarrera(rscfPersonaDtoBuscar.getPrsIdentificacion().trim(), rscfCarreraDtoBuscar.getCrrId());
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.buscar.sin.identificacion.validacion.exception")));
				}
				
			} catch (EstudianteDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (EstudianteDtoJdbcNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		
	}

	
	/**
	 * Método que permite ir a registrar los datos de las segunda carrera
	 * @return Navegacion a la pagina xhtml registrar segunda carrera.
	 */

	public String irRegistrarSegundaCarrera(EstudianteJdbcDto estudiante) {
		String retorno =null;
		rscfEstudianteEditar= null;
		rscfEstudianteEditar= estudiante;
		rscfBloqueaCamposAdicionales= true;
		try {
		if(rscfEstudianteEditar!=null){
			rscfListUniversidadesDto= servRscfInstitucionesAcademicasDto.listarXNivel(InstitucionAcademicaConstantes.NIVEL_UNIVERSIDAD_VALUE);
			rscfListTitulosUniversidadDto=servRscfTitulosDtoUniversitarios.listarXTipo(TituloConstantes.TIPO_TERCER_NIVEL_VALUE); 
			
		retorno= "irRegistrarSegundaCarrera";
		}else{
			FacesUtil.limpiarMensaje();
    		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.ir.registrar.segunda.carrera.validacion.exception")));

		}
		
		} catch (InstitucionAcademicaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (InstitucionAcademicaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TituloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TituloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		
		
		return retorno;
	}

	
	
	/**
	 * Método para verificar que la información este completa antes de guardar el registro
	 */
	public String irVerificarRegistroSegundaCarrera(EstudianteJdbcDto estudiante) {
		rscfEstudianteGuardar = null;
		rscfEstudianteGuardar=estudiante;

		boolean datosCompletos = false;
		boolean datosCompletosTitulado = true;
		if((rscfEstudianteGuardar.getFcesTipoUnivEstudPrev()==GeneralesConstantes.APP_ID_BASE)
				||(rscfEstudianteGuardar.getFcesEstadoEstudPrev()==GeneralesConstantes.APP_ID_BASE)
				||(rscfEstudianteGuardar.getFcesUnivEstudPrevId()==GeneralesConstantes.APP_ID_BASE)
				){
			datosCompletos= false;
		}else {
			datosCompletos= true;
		}
		
		if(rscfEstudianteGuardar.getFcesEstadoEstudPrev()==FichaEstudianteConstantes.ESTADO_ESTUD_PREV_TITULADO_VALUE){
			if((rscfEstudianteGuardar.getFcesTitEstudPrevId()==GeneralesConstantes.APP_ID_BASE)||(rscfEstudianteGuardar.getFcesRegTituloPrev()==null)
					||(rscfEstudianteGuardar.getFcesRegTituloPrev().equals(" "))||(rscfEstudianteGuardar.getFcesRegTituloPrev().trim().length()==0)){
				datosCompletosTitulado= false;
				
			}
			
		}
		
		
	if(datosCompletos){
		if(datosCompletosTitulado){
		rscfValidadorClic=1; //Se activa la ventana modal
		}else{
			rscfValidadorClic=0;
			FacesUtil.limpiarMensaje();
    		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.ir.verificar.registro.segunda.carrera.titulado.validacion.exception")));
		}
		
	}else{
		rscfValidadorClic=0;
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.ir.verificar.registro.segunda.carrera.titulado.validacion.exception")));
		FacesUtil.mensajeError("Debe ingresar toda la información solicitada");
	}
		return null;
	
	}

	/**
	 * Método para inicar el registro del notas de la materias homologadas del estudiante en BDD.
	 * @return navegación a la pagina xhtml. Listar estudiantes a homologar.
	 */
	public String guardarRegistroSegundaCarrera() {
	
			rscfValidadorClic = new Integer(0);
			
		if(servRscfFichaEstudianteServicio.editarFcesPorSegundaCarrera(rscfEstudianteGuardar)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Registro  grabado exitosamente");
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Se produjo un error al eliminar el registro");
				}
						
			iniciarParametros();
		return "irAlistarEstSegundaCarrera";
	}


	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	/**
	 * Método para limpiar
	 */
	public void limpiar(){
		iniciarParametros();
		
	}
	
	/**
	 * Método para desactivar el Modal guardar registro
	 */
		public void desactivaModalGuardarRegistroSegundaCarrera(){
			rscfValidadorClic = 0;
		}
		
		
		/**
		 * Método para cancelar el el ingresi de información de segunda carrera
		 * @return navegación a la pagina de listar estudiantes a homologar.
		 */
		public String cancelarRegistro() {
			iniciarParametros();
			return "irAlistarEstSegundaCarrera";
		}	
	
		
		/**
		 * Método para habilitar o deshabilitar ingreso de titulo y registro Senescyt
		*/
		public void habilitaListasAdicionales(Integer estadoEstudios) {
			
			if(estadoEstudios==FichaEstudianteConstantes.ESTADO_ESTUD_PREV_TITULADO_VALUE){
				rscfEstudianteEditar.setFcesTitEstudPrevId(GeneralesConstantes.APP_ID_BASE);
				rscfEstudianteEditar.setFcesRegTituloPrev(null);
				rscfBloqueaCamposAdicionales=false;
				
			}else if(estadoEstudios==FichaEstudianteConstantes.ESTADO_ESTUD_PREV_CURSANDO_VALUE){
				rscfEstudianteEditar.setFcesTitEstudPrevId(GeneralesConstantes.APP_ID_BASE);
				rscfEstudianteEditar.setFcesRegTituloPrev(null);
				rscfBloqueaCamposAdicionales=true;
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("debe seleccionar un estado de estudios previos");
								
			}
			
		}	
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getRscfUsuario() {
		return rscfUsuario;
	}

	public void setRscfUsuario(Usuario rscfUsuario) {
		this.rscfUsuario = rscfUsuario;
	}

	public PersonaDto getRscfPersonaDtoBuscar() {
		return rscfPersonaDtoBuscar;
	}

	public void setRscfPersonaDtoBuscar(PersonaDto rscfPersonaDtoBuscar) {
		this.rscfPersonaDtoBuscar = rscfPersonaDtoBuscar;
	}

	public List<CarreraDto> getRscfListCarreraDto() {
		rscfListCarreraDto = rscfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : rscfListCarreraDto;
		return rscfListCarreraDto;
	}

	public void setRscfListCarreraDto(List<CarreraDto> rscfListCarreraDto) {
		this.rscfListCarreraDto = rscfListCarreraDto;
	}

	public CarreraDto getRscfCarreraDtoBuscar() {
		return rscfCarreraDtoBuscar;
	}

	public void setRscfCarreraDtoBuscar(CarreraDto rscfCarreraDtoBuscar) {
		this.rscfCarreraDtoBuscar = rscfCarreraDtoBuscar;
	}

	public Integer getRscfValidadorClic() {
		return rscfValidadorClic;
	}

	public void setRscfValidadorClic(Integer rscfValidadorClic) {
		this.rscfValidadorClic = rscfValidadorClic;
	}

	
	public PeriodoAcademico getRscfPeriodoAcademicoActivo() {
		return rscfPeriodoAcademicoActivo;
	}

	public void setRscfPeriodoAcademicoActivo(PeriodoAcademico rscfPeriodoAcademicoActivo) {
		this.rscfPeriodoAcademicoActivo = rscfPeriodoAcademicoActivo;
	}

	
	public List<EstudianteJdbcDto> getRscfListaEstudiantes() {
		rscfListaEstudiantes = rscfListaEstudiantes == null ? (new ArrayList<EstudianteJdbcDto>()) : rscfListaEstudiantes;
		return rscfListaEstudiantes;
	}

	public void setRscfListaEstudiantes(List<EstudianteJdbcDto> rscfListaEstudiantes) {
		this.rscfListaEstudiantes = rscfListaEstudiantes;
	}

	public EstudianteJdbcDto getRscfEstudianteEditar() {
		return rscfEstudianteEditar;
	}

	public void setRscfEstudianteEditar(EstudianteJdbcDto rscfEstudianteEditar) {
		this.rscfEstudianteEditar = rscfEstudianteEditar;
	}

	public List<InstitucionAcademicaDto> getRscfListUniversidadesDto() {
		rscfListUniversidadesDto = rscfListUniversidadesDto == null ? (new ArrayList<InstitucionAcademicaDto>()) : rscfListUniversidadesDto;
		return rscfListUniversidadesDto;
	}

	public void setRscfListUniversidadesDto(List<InstitucionAcademicaDto> rscfListUniversidadesDto) {
		this.rscfListUniversidadesDto = rscfListUniversidadesDto;
	}

	public List<TituloDto> getRscfListTitulosUniversidadDto() {
		rscfListTitulosUniversidadDto = rscfListTitulosUniversidadDto == null ? (new ArrayList<TituloDto>()) : rscfListTitulosUniversidadDto;
		return rscfListTitulosUniversidadDto;
	}

	public void setRscfListTitulosUniversidadDto(List<TituloDto> rscfListTitulosUniversidadDto) {
		this.rscfListTitulosUniversidadDto = rscfListTitulosUniversidadDto;
	}

	public EstudianteJdbcDto getRscfEstudianteGuardar() {
		return rscfEstudianteGuardar;
	}

	public void setRscfEstudianteGuardar(EstudianteJdbcDto rscfEstudianteGuardar) {
		this.rscfEstudianteGuardar = rscfEstudianteGuardar;
	}

	public boolean isRscfBloqueaCamposAdicionales() {
		return rscfBloqueaCamposAdicionales;
	}

	public void setRscfBloqueaCamposAdicionales(boolean rscfBloqueaCamposAdicionales) {
		this.rscfBloqueaCamposAdicionales = rscfBloqueaCamposAdicionales;
	}
	
}
