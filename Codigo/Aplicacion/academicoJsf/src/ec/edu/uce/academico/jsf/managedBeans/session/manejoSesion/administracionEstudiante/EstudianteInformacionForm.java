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
   
 ARCHIVO:     EstudianteInformacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja la consulta de informacion del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-12-2017			 Marcelo Quishpe                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionEstudiante;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EtniaException;
import ec.edu.uce.academico.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.ReferenciaException;
import ec.edu.uce.academico.ejb.excepciones.ReferenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ReferenciaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Etnia;
import ec.edu.uce.academico.jpa.entidades.publico.Referencia;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EstudianteInformacionForm. 
 * Bean de sesion que maneja la consulta de Informacion del estudiante.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "estudianteInformacionForm")
@SessionScoped
public class EstudianteInformacionForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario eifUsuario;
	//OBJETOS PARA VER INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto eifEstudianteVer;
	private UbicacionDto eifCantonNacimientoVer;
	private UbicacionDto eifProvinciaNacimientoVer;
	private UbicacionDto eifPaisNacimientoVer;
	private UbicacionDto eifParroquiaResidenciaVer;
	private UbicacionDto eifCantonResidenciaVer;
	private UbicacionDto eifProvinciaResidenciaVer;
	private UbicacionDto eifPaisResidenciaVer;
	private List<Referencia> eifListaReferenciaVer;
	//LISTAS PARA EDITAR INFORMACIÓN DEL ESTUDIANTE
	private List<UbicacionDto> eifListaPaisesNac;
	private List<UbicacionDto> eifListaPaisesRes;
	private List<UbicacionDto> eifListaPaisesInac;
	private List<UbicacionDto> eifListaCantonesNac;
	private List<UbicacionDto> eifListaProvinciasNac;
	private List<UbicacionDto> eifListaParroquiasRes;
	private List<UbicacionDto> eifListaCantonesRes;
	private List<UbicacionDto> eifListaProvinciasRes;
	//OBJETOS PARA EDITAR INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto eifEstudianteEditar;
	private UbicacionDto eifCantonNacimientoEditar;
	private UbicacionDto eifProvinciaNacimientoEditar;
	private UbicacionDto eifPaisNacimientoEditar;
	private UbicacionDto eifParroquiaResidenciaEditar;
	private UbicacionDto eifCantonResidenciaEditar;
	private UbicacionDto eifProvinciaResidenciaEditar;
	private UbicacionDto eifPaisResidenciaEditar;
	private List<Referencia> eifListaReferenciaEditar;
	private List<Etnia> eifListaEtnias;
	//VARIABLES AUXILIARES
	private Integer eifClickModalEditarEstudiante;
	private Boolean eifDesactivarUbicacionRes;
	private Integer eifValidadorClick;
	private boolean eifHabilitarBoton;
	//PARA CARGA DE ARCHIVO
	private String eifNombreArchivoAuxiliar;
	private String eifNombreArchivoSubido;
	private Integer eifVisualizadorBotones;
	//PARA DESCARGAR ARCHIVO
	private String eifArchivoSelSt;
	private Integer eifVisualizadorLink;
	
	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {
	
	}
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB 
	private EstudianteDtoServicioJdbc  servEifEstudianteDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servEifUbicacionDtoServicioJdbc;
	@EJB
	private PersonaServicio servEifPersonaServicio;
	@EJB
	private ReferenciaServicio servEifReferenciaServicio;
	@EJB
	private EtniaServicio servEifEtniaServicio;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Método  que permite ver la informacion del estudiante
	 * @return navegacion a xhtml verDatosEstudiante
	 */
	public String irVerDatosEstudiante(Usuario usuario) {
		iniciarParametrosVer();// Inicializa los objetos antes de ver la información
		eifUsuario = usuario;// Guardamos el usuario en una variable
		llenarVerDatosEstudiante();
		return "irVerDatosEstudiante";

	}

	/**
	 * Método  que permite llenar las la informacion del estudiante, en las variables
	 * 
	 */
	public void llenarVerDatosEstudiante(){
		try {
			//DATOS DE ESTUDIANTE 
			eifEstudianteVer = servEifEstudianteDtoServicioJdbc.buscarEstudianteXIdPersona(eifUsuario.getUsrPersona().getPrsId());
			if (eifEstudianteVer != null) {
				// Pais, provincia, cantón de nacimiento
				try {
					eifCantonNacimientoVer = servEifUbicacionDtoServicioJdbc.buscarXId(eifEstudianteVer.getUbcPaisId());
					// Verifico que el dato almacenado sea de una cantón
					if (eifCantonNacimientoVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
						try {
							eifProvinciaNacimientoVer = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifCantonNacimientoVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifProvinciaNacimientoVer.setUbcDescripcion("N/A");
						}

						try {
							eifPaisNacimientoVer = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifProvinciaNacimientoVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifPaisNacimientoVer.setUbcDescripcion("N/A");

						}

					} else { // Si el dato guardado NO es un cantón, coloco N/A
						// provincia y canton, coloco el Id en pais.
						eifPaisNacimientoVer = eifCantonNacimientoVer;
						eifCantonNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifCantonNacimientoVer.setUbcDescripcion("N/A");
						eifProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifProvinciaNacimientoVer.setUbcDescripcion("N/A");

					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					eifCantonNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifCantonNacimientoVer.setUbcDescripcion("N/A");
					eifProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifProvinciaNacimientoVer.setUbcDescripcion("N/A");
					eifPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifPaisNacimientoVer.setUbcDescripcion("N/A");
				}

				// Pais, provincia, cantón de residencia
				try {

					eifParroquiaResidenciaVer = servEifUbicacionDtoServicioJdbc.buscarXId(eifEstudianteVer.getUbcParroquiaId());
					// Verifico que el dato almacenado sea de una parroquia
					if (eifParroquiaResidenciaVer
							.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE) {
						try {// busco el Canton con la parroquia de residencia
							eifCantonResidenciaVer = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifParroquiaResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifCantonResidenciaVer.setUbcDescripcion("N/A");
						}

						try { // Busco la provincia con el canton de residencia
							eifProvinciaResidenciaVer = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifCantonResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifProvinciaResidenciaVer.setUbcDescripcion("N/A");
						}

						try {// Busco el pais con la provincia de residencia
							eifPaisResidenciaVer = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifProvinciaResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {

							eifPaisResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifPaisResidenciaVer.setUbcDescripcion("N/A");
						}
					} else {// Si el dato guardado NO es un cantón, coloco N/A
						// en Pais, provincia, canton y parroquia
						eifPaisResidenciaVer = eifParroquiaResidenciaVer;
						eifProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifProvinciaResidenciaVer.setUbcDescripcion("N/A");
						eifCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifCantonResidenciaVer.setUbcDescripcion("N/A");
						eifParroquiaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifParroquiaResidenciaVer.setUbcDescripcion("N/A");
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					eifPaisResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifPaisResidenciaVer.setUbcDescripcion("N/A");
					eifProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifProvinciaResidenciaVer.setUbcDescripcion("N/A");
					eifCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifCantonResidenciaVer.setUbcDescripcion("N/A");
					eifParroquiaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifParroquiaResidenciaVer.setUbcDescripcion("N/A");
				}
				//PARA VER REGISTROS DE REFERENCIA
				eifListaReferenciaVer = servEifReferenciaServicio.buscarXPersona(eifEstudianteVer.getPrsId());
				if(eifListaReferenciaVer.size() > 0){
					eifHabilitarBoton = false;
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.validacion.exception")));
			}

		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.no.encontrado.exception")));
		}  catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.ubicacion.exception")));
		} catch (ReferenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
		} catch (ReferenciaException e) {
			FacesUtil.limpiarMensaje();
		}

	}

	/**
	 * Redirecciona a la pagina de ver datos completos del estudiante
	 * @return Navegacion a la página de visualización de datos del estudiante.
	 */
	public String editarDatosEstudiante() {
		String retorno = null;
		iniciarParametrosEditar();
		try {
			eifListaEtnias=servEifEtniaServicio.listarTodos();
		} catch (EtniaNoEncontradoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		} catch (EtniaException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		}
		//Listo los paises para el combo de pais de nacimiento
		try {
			eifListaPaisesNac= servEifUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
		} catch (UbicacionDtoJdbcException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.nacimiento.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.nacimiento.no.encontrado.exception")));
		}
		//Listo los paises para el combo de pais de residencia
		try {
			eifListaPaisesRes= servEifUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
		} catch (UbicacionDtoJdbcException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.residencia.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.residencia.no.encontrado.exception")));
		}
		try {
			//DATOS DE ESTUDIANTE 
			eifEstudianteEditar = servEifEstudianteDtoServicioJdbc.buscarEstudianteXIdPersona(eifUsuario.getUsrPersona().getPrsId());
			if(eifEstudianteEditar != null){
				//PAIS, PROVINCIA, CANTON DE NACIMIENTO
				try {
					eifCantonNacimientoEditar = servEifUbicacionDtoServicioJdbc.buscarXId(eifEstudianteEditar.getUbcPaisId());
					//Verifico que el dato almacenado sea de una cantón
					if (eifCantonNacimientoEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
						try {//Busco la provincia por el canton de nacimiento
							eifProvinciaNacimientoEditar = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifCantonNacimientoEditar.getUbcId());
							//Creo la lista de cantones por la provincia guardada en la BDD
							eifListaCantonesNac=   servEifUbicacionDtoServicioJdbc.listaCatonXProvincia(eifProvinciaNacimientoEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifProvinciaNacimientoEditar.setUbcDescripcion("N/A");
							eifListaCantonesNac= null;
						}
						try {//Busco el pais por la provincia de nacimiento
							eifPaisNacimientoEditar = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifProvinciaNacimientoEditar.getUbcId());
							//Creo la lista de Provincias por el pais guardado en la BDD.
							eifListaProvinciasNac= servEifUbicacionDtoServicioJdbc.listaProvinciaXPais(eifPaisNacimientoEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifPaisNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifPaisNacimientoEditar.setUbcDescripcion("N/A");
							eifListaProvinciasNac=null;
						}
					} else { //Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						eifPaisNacimientoEditar=eifCantonNacimientoEditar;
						eifCantonNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifCantonNacimientoEditar.setUbcDescripcion("N/A");
						eifProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifProvinciaNacimientoEditar.setUbcDescripcion("N/A");
						eifListaCantonesNac= null;
						//Busco la lista de Provincias si el pais es ecuador
						if(eifPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
							eifListaProvinciasNac=servEifUbicacionDtoServicioJdbc.listaProvinciaXPais(eifPaisNacimientoEditar.getUbcId());
						}else{
							eifListaProvinciasNac= null;
						}
					}
				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					eifCantonNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifCantonNacimientoEditar.setUbcDescripcion("N/A");
					eifProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifProvinciaNacimientoEditar.setUbcDescripcion("N/A");
					eifPaisNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifPaisNacimientoEditar.setUbcDescripcion("N/A");
					eifListaCantonesNac= null;
					eifListaProvinciasNac=null;
				}
				// PAIS, PROVINCIA, CANTON Y PARROQUIA DE RESIDENCIA
				try {
					eifParroquiaResidenciaEditar = servEifUbicacionDtoServicioJdbc.buscarXId(eifEstudianteEditar.getUbcParroquiaId());
					//Verifico que el dato almacenado sea de una parroquia
					if (eifParroquiaResidenciaEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE) {
						try {//Busco el canton por la parroquia de residencia
							eifCantonResidenciaEditar = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifParroquiaResidenciaEditar.getUbcId());
							//Creo la Lista de Parroquias por el canton guardado en la BDD.
							eifListaParroquiasRes=servEifUbicacionDtoServicioJdbc.listaParroquiasXCanton(eifCantonResidenciaEditar.getUbcId());	
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifCantonResidenciaEditar.setUbcDescripcion("N/A");
							eifListaParroquiasRes= null;
						}
						try {//Busco la provincia por el canton de residencia
							eifProvinciaResidenciaEditar = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifCantonResidenciaEditar.getUbcId());
							//Creo la Lista de Cantones por la provincia guardada en la BDD
							eifListaCantonesRes=servEifUbicacionDtoServicioJdbc.listaCatonXProvincia(eifProvinciaResidenciaEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifProvinciaResidenciaEditar.setUbcDescripcion("N/A");
							eifListaCantonesRes= null;
						}
						try {//Busco el pais por la provincia de residencia
							eifPaisResidenciaEditar = servEifUbicacionDtoServicioJdbc.buscarPadreXId(eifProvinciaResidenciaEditar.getUbcId());
							//Creo la Lista de Provincias por Pais guardado en la BDD
							eifListaProvinciasRes= servEifUbicacionDtoServicioJdbc.listaProvinciaXPais(eifPaisResidenciaEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							eifPaisResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							eifPaisResidenciaEditar.setUbcDescripcion("N/A");
						}
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia, canton y parroquia
						eifPaisResidenciaEditar=eifParroquiaResidenciaEditar;
						eifProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifProvinciaResidenciaEditar.setUbcDescripcion("N/A");
						eifCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifCantonResidenciaEditar.setUbcDescripcion("N/A");
						eifParroquiaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						eifParroquiaResidenciaEditar.setUbcDescripcion("N/A");
						eifListaCantonesRes= null;
						eifListaParroquiasRes= null;
						//Si el dato guardado  es Ecuador busco la lista de provincias.
						if(eifPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
							eifListaProvinciasRes=servEifUbicacionDtoServicioJdbc.listaProvinciaXPais(eifPaisResidenciaEditar.getUbcId());;
						}else{
							eifListaProvinciasRes= null;
						}
					}
				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					eifPaisResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifPaisResidenciaEditar.setUbcDescripcion("N/A");
					eifProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifProvinciaResidenciaEditar.setUbcDescripcion("N/A");
					eifCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifCantonResidenciaEditar.setUbcDescripcion("N/A");
					eifParroquiaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					eifParroquiaResidenciaEditar.setUbcDescripcion("N/A");
					eifListaProvinciasRes= null;
					eifListaCantonesRes= null;
					eifListaParroquiasRes= null;
				}
				//CONTROL DE BLOQUEO DE VISTA DE ELEMENTOS, ANTES DE PRESENTAR LOS DATOS
				if (eifPaisResidenciaEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {
					eifDesactivarUbicacionRes = Boolean.TRUE;
				}
				// FIN CONTROL
				//PARA OBTENER BENEFICIARIOS (REFERENCIAS)
				//				eifListaReferenciaEditar = servEifReferenciaServicio.buscarXPersona(eifEstudianteEditar.getPrsId());
				eifListaReferenciaEditar = eifListaReferenciaVer;
				if(eifListaReferenciaEditar.size() <= 0){
					Referencia referencia = new Referencia();
					eifListaReferenciaEditar.add(referencia);
				}

			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.validacion.exception")));
			}
			retorno = "irEditarDatosEstudiante";
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.no.encontrado.exception")));
		}  catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.ubicacion.exception")));
		} 
		return retorno;
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/

	//INICIAR PARÁMETROS DE VER ESTUDIANTE
	public void iniciarParametrosVer(){
		eifEstudianteVer = null;
		eifEstudianteVer = null;
		eifCantonNacimientoVer = null;
		eifProvinciaNacimientoVer = null;
		eifPaisNacimientoVer = null;
		eifParroquiaResidenciaVer = null;
		eifCantonResidenciaVer = null;
		eifProvinciaResidenciaVer = null;
		eifPaisResidenciaVer = null;
		eifHabilitarBoton = true;
		bloqueaModal();
		ClickCerrarModalEditarEstudiante();
		eifNombreArchivoAuxiliar = null;
		eifNombreArchivoSubido = null;
		eifVisualizadorBotones = GeneralesConstantes.APP_ID_BASE;
	}

	//INICIAR PARÁMETROS EDITAR ESTUDIANTE
	public void iniciarParametrosEditar(){
		eifListaPaisesNac= new ArrayList<UbicacionDto>();
		eifListaPaisesRes=new ArrayList<UbicacionDto>();
		eifListaCantonesNac= new ArrayList<UbicacionDto>();
		eifListaProvinciasNac= new ArrayList<UbicacionDto>();
		eifListaParroquiasRes= new ArrayList<UbicacionDto>();
		eifListaCantonesRes= new ArrayList<UbicacionDto>();
		eifListaProvinciasRes= new ArrayList<UbicacionDto>();

		eifEstudianteEditar= null;
		eifCantonNacimientoEditar= null;
		eifProvinciaNacimientoEditar= null;
		eifPaisNacimientoEditar= null;
		eifParroquiaResidenciaEditar= null;
		eifCantonResidenciaEditar= null;
		eifProvinciaResidenciaEditar= null;
		eifPaisResidenciaEditar= null;
		ClickCerrarModalEditarEstudiante();

	}
	/**
	 * Método que permite buscar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
//	public void	llenarProvinciasNacEditar(int idPais ){
//		eifListaProvinciasNac=null;
//		eifProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//		//eifListaCantonesNac=null;
//		eifCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//	    //Actualizar el valor del gentilicio, de acuerdo al pais seleccionado
//		UbicacionDto paisSeleccionadAux;
//		try {
//			paisSeleccionadAux = servEifUbicacionDtoServicioJdbc.buscarXId(idPais);
//			eifPaisNacimientoEditar=paisSeleccionadAux;
//		} catch (UbicacionDtoJdbcException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.pais.exception")));
//		} catch (UbicacionDtoJdbcNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.pais.no.encontrado.exception")));
//		}
//				
//		//LLenar la lista de Provincias.
//		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
//			try {
//				eifListaProvinciasNac = servEifUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
//			} catch (UbicacionDtoJdbcException e) {
//				
//				eifListaProvinciasNac=null;
//				eifListaCantonesNac=null;
//				eifProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//				eifCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.provincia.exception")));
//				
//			} catch (UbicacionDtoJdbcNoEncontradoException e) {
//				
//				eifListaProvinciasNac=null;
//				eifListaCantonesNac=null;
//				eifProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//				eifCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.provincia.no.encotrado.exception")));
//				
//			}
//		} else {
//			eifListaProvinciasNac=null;
//			eifListaCantonesNac=null;
//			eifProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//			eifCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//			
//		}
//	}
	
	/**
	 * Método que permite buscar la lista de cantones por el id del provincia
	 * @param idProvincia - idProvincia seleccionada para la búsqueda
	 */
//	public void	llenarCantonesNacEditar(int idProvincia){
//		eifListaCantonesNac=null;
//		eifCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
//		//Llena la lista de Cantones de nacimiento
//		try {
//			eifListaCantonesNac=servEifUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
//		} catch (UbicacionDtoJdbcException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.nac.editar.ubicacion.exception")));
//			
//		} catch (UbicacionDtoJdbcNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.nac.editar.ubicacion.no.encontrado.exception")));
//		}
//	
//	}
	
	/**
	 * Método que permite buscsar la lista de provincias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarProvinciasResEditar(int idPais ){
		eifListaProvinciasRes=null;
		eifProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//eifListaCantonesRes=null;
		eifCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//eifListaParroquiasRes=null;
		eifParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//LLenar la lista de Provincias.
		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
			try {
				eifListaProvinciasRes = servEifUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
				eifDesactivarUbicacionRes=Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				eifListaProvinciasRes=null;
				eifListaCantonesRes=null;
				eifListaParroquiasRes=null;
				eifProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				eifCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				eifParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				eifDesactivarUbicacionRes=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.res.editar.ubicacion.provincia.exception")));

			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				eifListaProvinciasRes=null;
				eifListaCantonesRes=null;
				eifListaParroquiasRes=null;
				eifProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				eifCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				eifParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				eifDesactivarUbicacionRes=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.res.editar.ubicacion.provincia.no.encontrado.exception")));
			}
		} else {
			eifListaProvinciasRes=null;
			eifListaCantonesRes=null;
			eifListaParroquiasRes=null;
			eifProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			eifCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			eifParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			eifDesactivarUbicacionRes=Boolean.TRUE;

		}

	}
	
	/**
	 * Método que permite buscsar la lista de cantones por el id de provincia
	 * @param idProvincia - idProvincia seleccionada para la búsqueda
	 */
	public void	llenarCantonesResEditar(int idProvincia){
		eifListaCantonesRes=null;
		eifCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		eifListaParroquiasRes=null;
		eifParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);

		//Llena la lista de Cantones de nacimiento
		try {
			eifListaCantonesRes=servEifUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.res.editar.ubicacion.canton.exception")));

		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.res.editar.ubicacion.canton.no.encontrado.exception")));
		}

	}
	
	/**
	 * Método que permite buscsar la lista de parroquias por el id del canton
	 * @param idCanton - idCanton seleccionada para la búsqueda
	 */
	public void	llenarParroquiasResEditar(int idCanton){
		eifListaParroquiasRes=null;
		eifParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);

		try {
			eifListaParroquiasRes=servEifUbicacionDtoServicioJdbc.listaParroquiasXCanton(idCanton);
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.parroquias.res.editar.ubicacion.parroquia.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.parroquias.res.editar.ubicacion.parroquia.no.encontrado.exception")));
		}

	}
	
	
	/**
	 * Método para verificar campos llenos antes de activar modal editar estudiante
	 */
	public void VerificaModalEditarEstudiante() {
		boolean validaReferencia = false;
		float porcentaje = 0;
		for (Referencia item : eifListaReferenciaEditar) {
			if(item.getRfrPorcentaje() == null || item.getRfrPorcentaje() == 0 || item.getRfrParentesco() == null || item.getRfrParentesco() == 0 || item.getRfrParentesco() == GeneralesConstantes.APP_ID_BASE ){
				validaReferencia = true;
			}else {
				validaReferencia = false;
			}
			if(item.getRfrCedula() == null || item.getRfrNombre() == null || item.getRfrDireccion() == null || validaReferencia ){
				validaReferencia = true;
			}else{
				porcentaje = porcentaje + item.getRfrPorcentaje();
			}
		}
		if(eifEstudianteEditar.getEtnId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar la etnia");
		} else if(eifEstudianteEditar.getPrsSexo() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar el sexo");
		} else if(eifEstudianteEditar.getPrsEstadoCivil() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar el estado civil");
		} else if(eifEstudianteEditar.getPrsMailPersonal().equals("")){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar el mail personal");
		} else if(eifEstudianteEditar.getPrsTelefono().equals("")){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar el número de teléfono");
		} else if(eifEstudianteEditar.getPrsCelular().equals("")){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar el número celular");
		} else if (eifPaisResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar el pais de residencia del estudiante");
		} else if((eifProvinciaResidenciaEditar.getUbcId()==GeneralesConstantes.APP_ID_BASE)&&(eifPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar la provincia de residencia del estudiante");
		} else if ((eifCantonResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (eifPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar el canton de residencia del estudiante");
		} else if ((eifParroquiaResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (eifPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe seleccionar la parroquia de residencia del estudiante");
		} else if(validaReferencia){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar todos los datos de los beneficiaros para el seguro de vida");
		} else if(porcentaje != 100){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("La suma de todos los porcentajes debe ser igual a 100");
		} else {
			// habilita Modal Modificar
			ClickModalEditarEstudiante();
		}
	}
	
	/**
	 * Método para actualizar la información del estudiante en la BDD
	 * @return navegación al xhtml ver DatosEstudiante
	 */
	public String guardarEstudianteEditado() {
		String retorno=null;
		//Si el valor del	pais seleccionado como residencia es diferente de ecuador, guardo el valor  del paisen el campo de Parroquia de residencia de la BDD
		if(eifPaisResidenciaEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			eifParroquiaResidenciaEditar=eifPaisResidenciaEditar;
		}
		//LLama al método actualizar estudiante, con los parametros 	
		try {
			eifListaReferenciaVer = servEifReferenciaServicio.buscarXPersona(eifEstudianteVer.getPrsId());
			if(servEifPersonaServicio.actualizarEstudianteXEstudiante(eifEstudianteEditar, eifParroquiaResidenciaEditar, eifListaReferenciaEditar, eifListaReferenciaVer)){
				ClickCerrarModalEditarEstudiante();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.guardar.estudiante.editado.validacion.exitoso")));
			}else{ // caso que no se ejecute la actualización
				ClickCerrarModalEditarEstudiante();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.guardar.estudiante.editado.validacion.sin.exitoso")));
			}
			llenarVerDatosEstudiante();
			eifHabilitarBoton = true;
			bloqueaModal();
			retorno="irVerDatosEstudiante";
		} catch (PersonaException e) {
			ClickCerrarModalEditarEstudiante();
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.guardar.estudiante.editado.persona.exception")));
		} catch (ReferenciaNoEncontradoException e) {
			e.printStackTrace();
			ClickCerrarModalEditarEstudiante();
			FacesUtil.limpiarMensaje();
		} catch (ReferenciaException e) {
			e.printStackTrace();
			ClickCerrarModalEditarEstudiante();
			FacesUtil.limpiarMensaje();
		}
		return retorno;
	}

	/**
	 * Método para activar Modal modificar estudiante
	 */
	public void ClickModalEditarEstudiante() {
		eifClickModalEditarEstudiante = 1; // habilita modal modificar
	}

	/**
	 * Método para desactivar Modal modificar estudiante
	 */
	public void ClickCerrarModalEditarEstudiante() {
		eifClickModalEditarEstudiante = 0; // cerrer modal modificar
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		iniciarParametrosVer();
		return "irInicio";
	}
	
	public void agregarBeneficiario(){
        Referencia ref = createReferencia(eifListaReferenciaEditar.size()).get(0);
        eifListaReferenciaEditar.add(ref);
	}
	
	public List<Referencia> createReferencia(int size) {
        List<Referencia> list = new ArrayList<Referencia>();
        for(int i = 0 ; i < size ; i++) {
            list.add(new Referencia());
        }
        return list;
    }
	
	public void elimiarRegistroEdit(Referencia refEliminar){
		if(eifListaReferenciaEditar.size() > 1){
			List<Referencia> listAux = new ArrayList<>();
			for (Referencia item : eifListaReferenciaEditar) {
				if(item != refEliminar){
					listAux.add(item);
				}
			}
			eifListaReferenciaEditar = null;
			eifListaReferenciaEditar = listAux;
		}else{
			FacesUtil.mensajeError("No puede eliminar todos los registros para la información del seguro de vida");
		}
	}
	
	public void verificarClickImprimir(){
		if(eifListaReferenciaVer.size() > 0 && eifEstudianteVer != null){
			generarReporteSeguroVida(eifEstudianteVer, eifListaReferenciaVer);
			habilitaModalImpresion();
		}else{
			bloqueaModal();
			FacesUtil.mensajeError("Debe ingresar toda la información");
		}
		eifHabilitarBoton = true;
	}
	
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		eifValidadorClick = 1;
	}
	
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalCargaArchivo(){
		eifValidadorClick = 2;
	}
	
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		eifValidadorClick = 0;
	}
	
	/**
	 * Método que genera el reporte
	 */
	public void generarReporteSeguroVida(EstudianteJdbcDto estudiante, List<Referencia> eifListaReferenciaVer){
		try {
			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			ListasCombosForm listasCombos = new ListasCombosForm();
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE HORARIO *********//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "reporteSeguroVida";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			SimpleDateFormat formatoAlterno = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha","Quito, "+fecha);
			frmRrmParametros.put("nombre",estudiante.getPrsPrimerApellido()+" "+estudiante.getPrsSegundoApellido()+" "+estudiante.getPrsNombres());
			frmRrmParametros.put("direccion",estudiante.getPrsCallePrincipal()+" "+estudiante.getPrsCalleSecundaria()+" "+estudiante.getPrsNumeroCasa()+" "+estudiante.getPrsReferenciaDomicilio());
			if(estudiante.getPrsEstadoCivil() == GeneralesConstantes.APP_ID_BASE){
				frmRrmParametros.put("estadocivil"," ");
			}else{
				frmRrmParametros.put("estadocivil",listasCombos.getLabelEstadoCivil(estudiante.getPrsEstadoCivil()));
			}
			frmRrmParametros.put("ocupacion","ESTUDIANTIL");
			frmRrmParametros.put("fechavinculacion",estudiante.getPrsFechaVinculacionSeguro().toString());
			if(estudiante.getPrsFechaNacimiento() != null && eifCantonNacimientoVer.getUbcDescripcion() != null){
				String fechaNacimiento = formatoAlterno.format(estudiante.getPrsFechaNacimiento());
				frmRrmParametros.put("lugarnacimiento",eifCantonNacimientoVer.getUbcDescripcion()+", "+fechaNacimiento);
			}else{
				frmRrmParametros.put("lugarnacimiento"," ");
			}
			
			frmRrmParametros.put("cedula",estudiante.getPrsIdentificacion());

			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> beneficiario = null;

			for (Referencia item : eifListaReferenciaVer) {
				beneficiario = new HashMap<String, Object>();
				beneficiario.put("nombrebeneficiario", item.getRfrNombre());
				beneficiario.put("direccionbeneficiario", item.getRfrDireccion());
				beneficiario.put("parentesco", listasCombos.getLabelTipoParentescoBeneficiario(item.getRfrParentesco()));
				beneficiario.put("porcentaje", item.getRfrPorcentaje().toString());
				frmRrmCampos.add(beneficiario);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSeguroVida");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/segurosucre.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");


			//			frmRrmCampos.add(datoHorario);
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
			bloqueaModal();
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	}
	
	//CARGA DE ARCHIVO
	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event - event archivo formulario de beneficiarios para seguro de vida
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		eifNombreArchivoSubido = archivo.getFile().getFileName();
		eifNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + eifNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(),	rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {
			FacesUtil.mensajeError("No puede cargar el archivo");
		}
	}
	
//	public void upload() {
//        if(file != null) {
//            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton agregar el formulario de seguro de vida 
	 * @return retora null para para cualquier cosa pero setea a 6 la variable eifValidadorClic
	 * estado para poder agregra el formulario de seguro de vida
	 */
	public void verificarClickAgregarFormulario(){
		bloqueaModal();
		String rutaNombre = null;
		String rutaTemporal = null;
//		String archivoGuardado = null;
		String extension = GeneralesUtilidades.obtenerExtension(eifNombreArchivoSubido);
		if(eifNombreArchivoSubido != null){ //verifica que ha subido el archivo
			rutaNombre = eifEstudianteVer.getPrsIdentificacion()+ "." + extension;
//			archivoGuardado = rutaNombre;
			rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + eifNombreArchivoSubido;
			try {
				//actualizar el campo en la tabla persona de la bd
				if(servEifPersonaServicio.actualizarFormularioSeguroPersona(eifEstudianteVer)){
					GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_FORMULARIO_SEGURO_VIDA+ rutaNombre);
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Formulario cargado con éxito");
				}
			} catch (FileNotFoundException e) {
				e.getMessage();
			} catch (PersonaException e) {
				e.getMessage();
			}
		} else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe realizar la carga del formulario del seguro de vida");
		}
	}
	
	public boolean verificarDatosIngresados(){
		boolean retorno;
		if(eifListaReferenciaVer.size() > 0 && eifEstudianteVer != null){
			retorno = false;
		}else{
			retorno = true;
		}
		return retorno;
	}
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(){
		eifArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_FORMULARIO_SEGURO_VIDA+eifEstudianteVer.getPrsFormularioSeguro()+".pdf";
		if(eifArchivoSelSt != null){
			String nombre = "Seguro de vida "+eifEstudianteVer.getPrsFormularioSeguro();
			try{
//				File f=new File(GeneralesConstantes.APP_PATH_ARCHIVOS_TEMPORAL+materiaSeleccionada.getDtmtArchivoEstudiantes());
//				if(f.exists())
//				{
//				    f.delete();
//				}
//				f.createNewFile(); 
//				FileObject destn = (FileObject) VFS.getManager().resolveFile(f.getAbsolutePath());
//				UserAuthenticator auth = new StaticUserAuthenticator("", "produ", "12345.a");
//				FileSystemOptions opts = new FileSystemOptions();
//
//				DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
//				FileObject fo = (FileObject) VFS.getManager().resolveFile(armfArchivoSelSt,opts);
//				destn.copyFrom( fo,Selectors.SELECT_SELF);
//				destn.close();
//				return new DefaultStreamedContent(destn.getContent().getInputStream(),GeneralesUtilidades.obtenerContentType(armfArchivoSelSt),nombre);
				
//					FileInputStream  fis = new FileInputStream(vstmfArchivoSelSt);
					URL oracle = new URL("file:"+eifArchivoSelSt);
//					 URL urlObject = new URL("/");
					    URLConnection urlConnection = oracle.openConnection();
					    InputStream inputStream = urlConnection.getInputStream();
					return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(eifArchivoSelSt),nombre);
				
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
				eifArchivoSelSt = null;
				FacesUtil.mensajeError("Error al descargar el formulario de seguro de vida. Inténtelo nuevamente.");
				return null;
			} catch (Exception e) {
			}
		}else{
			FacesUtil.mensajeError("Error al descargar el formulario de seguro de vida, no se encontró el archivo, comuniquese con el administrador del sistema.");
			return null;
		}
		return null;
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas 
	 */
	public boolean deshabilitar(){
		boolean retorno = false;
		try {
			EstudianteJdbcDto consulta = new EstudianteJdbcDto();
			consulta = servEifEstudianteDtoServicioJdbc.buscarEstudianteXIdPersona(eifUsuario.getUsrPersona().getPrsId());
			eifEstudianteVer = consulta;
			if(consulta.getPrsFormularioSeguro() != null){
				retorno = true;
			}
		} catch (EstudianteDtoJdbcException | EstudianteDtoJdbcNoEncontradoException e) {
			e.getMessage();
		}
		return retorno;
	}

	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	public Usuario getEifUsuario() {
		return eifUsuario;
	}

	public void setEifUsuario(Usuario eifUsuario) {
		this.eifUsuario = eifUsuario;
	}

	public EstudianteJdbcDto getEifEstudianteVer() {
		return eifEstudianteVer;
	}

	public void setEifEstudianteVer(EstudianteJdbcDto eifEstudianteVer) {
		this.eifEstudianteVer = eifEstudianteVer;
	}


	public UbicacionDto getEifCantonNacimientoVer() {
		return eifCantonNacimientoVer;
	}


	public void setEifCantonNacimientoVer(UbicacionDto eifCantonNacimientoVer) {
		this.eifCantonNacimientoVer = eifCantonNacimientoVer;
	}


	public UbicacionDto getEifProvinciaNacimientoVer() {
		return eifProvinciaNacimientoVer;
	}


	public void setEifProvinciaNacimientoVer(UbicacionDto eifProvinciaNacimientoVer) {
		this.eifProvinciaNacimientoVer = eifProvinciaNacimientoVer;
	}


	public UbicacionDto getEifPaisNacimientoVer() {
		return eifPaisNacimientoVer;
	}


	public void setEifPaisNacimientoVer(UbicacionDto eifPaisNacimientoVer) {
		this.eifPaisNacimientoVer = eifPaisNacimientoVer;
	}


	public UbicacionDto getEifParroquiaResidenciaVer() {
		return eifParroquiaResidenciaVer;
	}


	public void setEifParroquiaResidenciaVer(UbicacionDto eifParroquiaResidenciaVer) {
		this.eifParroquiaResidenciaVer = eifParroquiaResidenciaVer;
	}


	public UbicacionDto getEifCantonResidenciaVer() {
		return eifCantonResidenciaVer;
	}


	public void setEifCantonResidenciaVer(UbicacionDto eifCantonResidenciaVer) {
		this.eifCantonResidenciaVer = eifCantonResidenciaVer;
	}


	public UbicacionDto getEifProvinciaResidenciaVer() {
		return eifProvinciaResidenciaVer;
	}


	public void setEifProvinciaResidenciaVer(UbicacionDto eifProvinciaResidenciaVer) {
		this.eifProvinciaResidenciaVer = eifProvinciaResidenciaVer;
	}


	public UbicacionDto getEifPaisResidenciaVer() {
		return eifPaisResidenciaVer;
	}


	public void setEifPaisResidenciaVer(UbicacionDto eifPaisResidenciaVer) {
		this.eifPaisResidenciaVer = eifPaisResidenciaVer;
	}

	public List<Referencia> getEifListaReferenciaVer() {
		eifListaReferenciaVer = eifListaReferenciaVer == null ? (new ArrayList<Referencia>()) : eifListaReferenciaVer;
		return eifListaReferenciaVer;
	}

	public void setEifListaReferenciaVer(List<Referencia> eifListaReferenciaVer) {
		this.eifListaReferenciaVer = eifListaReferenciaVer;
	}

	public EstudianteJdbcDto getEifEstudianteEditar() {
		return eifEstudianteEditar;
	}


	public void setEifEstudianteEditar(EstudianteJdbcDto eifEstudianteEditar) {
		this.eifEstudianteEditar = eifEstudianteEditar;
	}


	public UbicacionDto getEifCantonNacimientoEditar() {
		return eifCantonNacimientoEditar;
	}


	public void setEifCantonNacimientoEditar(UbicacionDto eifCantonNacimientoEditar) {
		this.eifCantonNacimientoEditar = eifCantonNacimientoEditar;
	}


	public UbicacionDto getEifProvinciaNacimientoEditar() {
		return eifProvinciaNacimientoEditar;
	}


	public void setEifProvinciaNacimientoEditar(UbicacionDto eifProvinciaNacimientoEditar) {
		this.eifProvinciaNacimientoEditar = eifProvinciaNacimientoEditar;
	}


	public UbicacionDto getEifPaisNacimientoEditar() {
		return eifPaisNacimientoEditar;
	}


	public void setEifPaisNacimientoEditar(UbicacionDto eifPaisNacimientoEditar) {
		this.eifPaisNacimientoEditar = eifPaisNacimientoEditar;
	}


	public UbicacionDto getEifParroquiaResidenciaEditar() {
		return eifParroquiaResidenciaEditar;
	}


	public void setEifParroquiaResidenciaEditar(UbicacionDto eifParroquiaResidenciaEditar) {
		this.eifParroquiaResidenciaEditar = eifParroquiaResidenciaEditar;
	}


	public UbicacionDto getEifCantonResidenciaEditar() {
		return eifCantonResidenciaEditar;
	}


	public void setEifCantonResidenciaEditar(UbicacionDto eifCantonResidenciaEditar) {
		this.eifCantonResidenciaEditar = eifCantonResidenciaEditar;
	}


	public UbicacionDto getEifPaisResidenciaEditar() {
		return eifPaisResidenciaEditar;
	}


	public void setEifPaisResidenciaEditar(UbicacionDto eifPaisResidenciaEditar) {
		this.eifPaisResidenciaEditar = eifPaisResidenciaEditar;
	}

	
	
	public List<UbicacionDto> getEifListaPaisesNac() {
		eifListaPaisesNac = eifListaPaisesNac == null ? (new ArrayList<UbicacionDto>()) : eifListaPaisesNac;
		return eifListaPaisesNac;
	}


	public void setEifListaPaisesNac(List<UbicacionDto> eifListaPaisesNac) {
		this.eifListaPaisesNac = eifListaPaisesNac;
	}


	public List<UbicacionDto> getEifListaPaisesRes() {
		eifListaPaisesRes = eifListaPaisesRes == null ? (new ArrayList<UbicacionDto>()) : eifListaPaisesRes;
		return eifListaPaisesRes;
	}


	public void setEifListaPaisesRes(List<UbicacionDto> eifListaPaisesRes) {
		this.eifListaPaisesRes = eifListaPaisesRes;
	}


	public List<UbicacionDto> getEifListaPaisesInac() {
		eifListaPaisesInac = eifListaPaisesInac == null ? (new ArrayList<UbicacionDto>()) : eifListaPaisesInac;
		return eifListaPaisesInac;
	}


	public void setEifListaPaisesInac(List<UbicacionDto> eifListaPaisesInac) {
		this.eifListaPaisesInac = eifListaPaisesInac;
	}


	public List<UbicacionDto> getEifListaCantonesNac() {
		eifListaCantonesNac = eifListaCantonesNac == null ? (new ArrayList<UbicacionDto>()) : eifListaCantonesNac;
		return eifListaCantonesNac;
	}


	public void setEifListaCantonesNac(List<UbicacionDto> eifListaCantonesNac) {
		this.eifListaCantonesNac = eifListaCantonesNac;
	}


	public List<UbicacionDto> getEifListaProvinciasNac() {
		eifListaProvinciasNac = eifListaProvinciasNac == null ? (new ArrayList<UbicacionDto>()) : eifListaProvinciasNac;
		return eifListaProvinciasNac;
	}


	public void setEifListaProvinciasNac(List<UbicacionDto> eifListaProvinciasNac) {
		this.eifListaProvinciasNac = eifListaProvinciasNac;
	}


	public List<UbicacionDto> getEifListaParroquiasRes() {
		eifListaParroquiasRes = eifListaParroquiasRes == null ? (new ArrayList<UbicacionDto>()) : eifListaParroquiasRes;
		return eifListaParroquiasRes;
	}


	public void setEifListaParroquiasRes(List<UbicacionDto> eifListaParroquiasRes) {
		this.eifListaParroquiasRes = eifListaParroquiasRes;
	}


	public List<UbicacionDto> getEifListaCantonesRes() {
		eifListaCantonesRes = eifListaCantonesRes == null ? (new ArrayList<UbicacionDto>()) : eifListaCantonesRes;
		return eifListaCantonesRes;
	}


	public void setEifListaCantonesRes(List<UbicacionDto> eifListaCantonesRes) {
		this.eifListaCantonesRes = eifListaCantonesRes;
	}


	public List<UbicacionDto> getEifListaProvinciasRes() {
		eifListaProvinciasRes = eifListaProvinciasRes == null ? (new ArrayList<UbicacionDto>()) : eifListaProvinciasRes;
		return eifListaProvinciasRes;
	}


	public void setEifListaProvinciasRes(List<UbicacionDto> eifListaProvinciasRes) {
		this.eifListaProvinciasRes = eifListaProvinciasRes;
	}


	public UbicacionDto getEifProvinciaResidenciaEditar() {
		return eifProvinciaResidenciaEditar;
	}


	public void setEifProvinciaResidenciaEditar(UbicacionDto eifProvinciaResidenciaEditar) {
		this.eifProvinciaResidenciaEditar = eifProvinciaResidenciaEditar;
	}


	public Integer getEifClickModalEditarEstudiante() {
		return eifClickModalEditarEstudiante;
	}


	public void setEifClickModalEditarEstudiante(Integer eifClickModalEditarEstudiante) {
		this.eifClickModalEditarEstudiante = eifClickModalEditarEstudiante;
	}


	public Boolean getEifDesactivarUbicacionRes() {
		return eifDesactivarUbicacionRes;
	}


	public void setEifDesactivarUbicacionRes(Boolean eifDesactivarUbicacionRes) {
		this.eifDesactivarUbicacionRes = eifDesactivarUbicacionRes;
	}

	public List<Referencia> getEifListaReferenciaEditar() {
		eifListaReferenciaEditar = eifListaReferenciaEditar == null ? (new ArrayList<Referencia>()) : eifListaReferenciaEditar;
		return eifListaReferenciaEditar;
	}

	public void setEifListaReferenciaEditar(List<Referencia> eifListaReferenciaEditar) {
		this.eifListaReferenciaEditar = eifListaReferenciaEditar;
	}

	public List<Etnia> getEifListaEtnias() {
		eifListaEtnias = eifListaEtnias == null ? (new ArrayList<Etnia>()) : eifListaEtnias;
		return eifListaEtnias;
	}

	public void setEifListaEtnias(List<Etnia> eifListaEtnias) {
		this.eifListaEtnias = eifListaEtnias;
	}

	public Integer getEifValidadorClick() {
		return eifValidadorClick;
	}

	public void setEifValidadorClick(Integer eifValidadorClick) {
		this.eifValidadorClick = eifValidadorClick;
	}

	public boolean isEifHabilitarBoton() {
		return eifHabilitarBoton;
	}

	public void setEifHabilitarBoton(boolean eifHabilitarBoton) {
		this.eifHabilitarBoton = eifHabilitarBoton;
	}

	public String getEifNombreArchivoAuxiliar() {
		return eifNombreArchivoAuxiliar;
	}

	public void setEifNombreArchivoAuxiliar(String eifNombreArchivoAuxiliar) {
		this.eifNombreArchivoAuxiliar = eifNombreArchivoAuxiliar;
	}

	public String getEifNombreArchivoSubido() {
		return eifNombreArchivoSubido;
	}

	public void setEifNombreArchivoSubido(String eifNombreArchivoSubido) {
		this.eifNombreArchivoSubido = eifNombreArchivoSubido;
	}

	public Integer getEifVisualizadorBotones() {
		return eifVisualizadorBotones;
	}

	public void setEifVisualizadorBotones(Integer eifVisualizadorBotones) {
		this.eifVisualizadorBotones = eifVisualizadorBotones;
	}

	public String getEifArchivoSelSt() {
		return eifArchivoSelSt;
	}

	public void setEifArchivoSelSt(String eifArchivoSelSt) {
		this.eifArchivoSelSt = eifArchivoSelSt;
	}

	public Integer getEifVisualizadorLink() {
		return eifVisualizadorLink;
	}

	public void setEifVisualizadorLink(Integer eifVisualizadorLink) {
		this.eifVisualizadorLink = eifVisualizadorLink;
	}

	
}
