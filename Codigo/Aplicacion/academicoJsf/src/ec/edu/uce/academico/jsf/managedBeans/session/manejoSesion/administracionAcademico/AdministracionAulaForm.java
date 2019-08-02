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
   
 ARCHIVO:     AdministracionAulaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de  Aula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
15-05-2017			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EdificioDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AulaException;
import ec.edu.uce.academico.ejb.excepciones.AulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AulaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioException;
import ec.edu.uce.academico.ejb.excepciones.EdificioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EdificioServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseAulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.Edificio;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) AdministracionAulaForm.
 * Managed Bean que maneja las peticiones para la administración de la tabla Aula.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name="administracionAulaForm")
@SessionScoped
public class AdministracionAulaForm implements Serializable{

	private static final long serialVersionUID = -397102404723196895L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	// OBJETOS
	private Usuario aafUsuario;
	private DependenciaDto aafDependenciaDtoBuscar;
	private EdificioDto aafEdificioDtoBuscar;
	private AulaDto aafAulaDto;
	// CREAR NUEVA AULA
	private AulaDto aafAulaDtoNuevo;
	private Edificio aafEdificio;
	private Aula aafAulaGrabada;
	// LISTAS DE OBJETO
	private List<AulaDto> aafListAulaDto;
	private List<DependenciaDto> aafListDependenciaDto;
	private List<EdificioDto> aafListEdificioDto;
	private List<HoraClase> aafListHoraClase;
	private List<HoraClaseAula>   aafListHoraClaseAulaEditar;
	// **********AUXILIARES*****************//
	private Integer aafValidadorClick; // Para verificar si se va a editar o crear nuevo objeto
	private AulaDto aafAulaDtoEditar; // Objeto a Editar
	// PARAMETROS PARA ELIMINAR AULAS
	private AulaDto aafAulaDtoEliminar;
	private List<HoraClaseAula> aafListHoraClaseAulaEliminar;
	private List<HorarioAcademico> aafListHorarioAcademico;
	
	
	
		
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
		
	}
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB 
	AulaDtoServicioJdbc servAafAulaDto;
	@EJB
	DependenciaDtoServicioJdbc servAafDependenciaDto;
	@EJB
	EdificioDtoServicioJdbc servAafEdificioDto;	
	@EJB
	AulaServicio servAafAulaServicio;
	@EJB
	EdificioServicio servAafEdificioServicio;
	@EJB
	HoraClaseAulaServicio servAafHoraClaseAulaServicio;
	@EJB
	HoraClaseServicio servAafHoraClaseServicio;
	@EJB
	HorarioAcademicoServicio servAafHorarioAcademicoServicio;
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	//IR A ADMINISTRAR AULA
	/**
	 * Método que permite iniciar la administración del aula
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración aula.
	 */
	
	public String irAdministracionAula(Usuario usuario) {
		aafUsuario = usuario; // Guardo el usuario que ingresa
		iniciarParametros(); // llamo al método iniciarParametros
		return "irAdministracionAula"; // Voy a la página de administración del aula
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados y paneles
	 * @return 
	 */
	public String limpiar() {
		iniciarParametros();
		return null;
	}
	
	/**
	 * Método para limpiar la lista de Aula cuando se cambia el edificio, y no se presiona buscar
	 * @return 
	 */
	public String limpiarAulas() {
		aafListAulaDto = new ArrayList<AulaDto>();
		return null;
	}
	
	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametros() {
		aafDependenciaDtoBuscar = new DependenciaDto();// Inicio el objeto Dependencia
		aafEdificioDtoBuscar = new EdificioDto(); // Inicio el objetos Edificio
		aafListAulaDto = null;// inicio la lista de Aula
		aafListDependenciaDto = new ArrayList<DependenciaDto>(); // inicio la lista de Dependencias
		aafListEdificioDto = new ArrayList<EdificioDto>(); // inicio la lista de Edificios
		aafDependenciaDtoBuscar.setDpnId(GeneralesConstantes.APP_ID_BASE);// Seteo en -99 el valor inicial IdDependencia del objeto Dependencia
		aafEdificioDtoBuscar.setEdfId(GeneralesConstantes.APP_ID_BASE);// Seteo en -99 el valor inicial IdEdificio del objeto Edificio
		aafValidadorClick = 0; // Seteo en cero el valor de click editar/nuevo
		llenarDependencia(); // Se llama al metodo para llenar las dependencias
	}
	
	
	 /**
	 * Método para llenar la lista de Dependencias
	 */
	public void llenarDependencia() {
		aafListDependenciaDto = null;
		aafDependenciaDtoBuscar.setDpnId(GeneralesConstantes.APP_ID_BASE);
		try {
			aafListDependenciaDto = servAafDependenciaDto.listarXUsuario(aafUsuario.getUsrId());
		} catch (DependenciaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	/**
	 * Método para llenar la lista de Edificio 
	 *
	 */
	public void llenarEdificio() {
		aafListEdificioDto = null;
		aafEdificioDtoBuscar.setEdfId(GeneralesConstantes.APP_ID_BASE);
		if (aafDependenciaDtoBuscar.getDpnId() != GeneralesConstantes.APP_ID_BASE) { // Si selecciona una Dependencia
			try {
				aafListEdificioDto = servAafEdificioDto.listarEdificiosPdpnId(aafDependenciaDtoBuscar.getDpnId());
			} catch (EdificioDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (EdificioDtoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.llenar.edificios.id.dependencia.no.encontrado.exception")));
			}
		} else { 
			// Sino selecciona una carrera, limpia los mensajes e inicia los parametros nuevamente, para que desliegue todas las aulas sin filtro
			FacesUtil.limpiarMensaje();// no es necesario enviar mensjae al usuario
			limpiar(); // se limpia, inicializa los objetos
		}
	}
	
	/**
	 * Método para llenar la Lista de Aulas 
	 */
	public void buscarAula() {
		aafListAulaDto = null;
		try {
			// Listo las aulas con los parametros ingresados, al presionar buscar
			aafListAulaDto = servAafAulaDto.listarXUsuarioXDependenciaXEdificio(aafUsuario.getUsrId(),aafDependenciaDtoBuscar.getDpnId(), aafEdificioDtoBuscar.getEdfId());
		} catch (AulaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
		

	/**
	 * Método que permite ver  o editar un aula
	 * @return navegación al xhtml ver Paralelo o editar Aula
	 * @param  El objeto aula y el tipo de transacción (ver o editar)
	 */
	public String verDatosAula(AulaDto aula, int transaccion) {
		aafAulaDto = null;
		aafAulaDto = aula;
		String retorno = null;
		// Pregunta si quiere ver o editar el aula de acuero a la transacción
		if (transaccion == 0) {
			retorno = "irVerDatosAula";
		} else if (transaccion == 1) {
			retorno = "irEditarAula";
		}
		return retorno;
	}
	
	/**
	 * Método para editar un Parametro
	 * @return navegación al xhtml listarParalelos
	 */
	public String editarAula() {
		aafAulaDtoEditar = new AulaDto();
		aafAulaDtoEditar = aafAulaDto;
		aafListHoraClaseAulaEditar=null;
		//LISTO HORAS CLASE AULA DE LA MATERIA. HA SER EDITADOS.
		try {
			aafListHoraClaseAulaEditar=servAafHoraClaseAulaServicio.ListarHoraClaseAulaPIdAla(aafAulaDtoEditar.getAlaId());
		} catch (HoraClaseAulaException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return "irAdministracionAula";
		} catch (HoraClaseAulaNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return "irAdministracionAula";
		}		
		
		
		try {
			servAafAulaServicio.editar(aafAulaDtoEditar,aafListHoraClaseAulaEditar );
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.editar.aula.validacion.exitoso")));
		} catch (AulaValidacionException e) {
			aafValidadorClick=0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (AulaException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			
		}
		return "irAdministracionAula";
	}
	
	/**
	 * Método para ir a generar el nuevo paralelo
	 * @return navagación al xhtml nuevo
	 */
	public String irNuevoAula() {
		// INICIALIZO LOS OBJETOS Y LISTAS
		aafAulaDtoNuevo = new AulaDto();
		aafListDependenciaDto = null;
		aafListEdificioDto = null;
		aafListHoraClase= null;
		llenarDependencia();
		return "irNuevoAula";
	}
	
	/**
	 * Método para crear y grabar un nueva aula
	 * @return navagación al xhtml AdministracionAula
	 */
	public String crearAula() {
		aafEdificio = null;
		aafAulaGrabada = null;
		aafListHoraClase= null;
		try {
			// busco el objeto edificio de acuerdo al item seleccionado en el
			// combo
			if (aafEdificioDtoBuscar != null) {
				aafEdificio = servAafEdificioServicio.buscarPorId(aafEdificioDtoBuscar.getEdfId());
				if (aafEdificio == null) {
					//envia el mensaje a Exception NoEncontrado
					throw new EdificioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministrarAula.crear.aula.buscar.edificio.validacion.exception")));
				} 
			}
		} catch (EdificioNoEncontradoException e1) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return "irAdministracionAula";
		} catch (EdificioException e1) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return "irAdministracionAula";
		}

		try {//LIST TODAS LAS HORAS CLASE PARA RELACIONARLAS CON LA AULA NUEVA.
			aafListHoraClase = servAafHoraClaseServicio.listarTodos();
		} catch (HoraClaseAulaNoEncontradoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return "irAdministracionAula";
		} catch (HoraClaseException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return "irAdministracionAula";
		}

		
		if ((aafListHoraClase != null)) {//Si existe lista de horas_clase
			try {
				// llamo al servicio que crea la nueva aula
				aafAulaDtoNuevo.setEdfId(aafEdificio.getEdfId());
				aafAulaGrabada = servAafAulaServicio.anadir(aafAulaDtoNuevo, aafEdificio , aafListHoraClase);
				iniciarParametros();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.crear.aula.validacion.exitoso")));
			} catch (AulaValidacionException e) {
				aafValidadorClick = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
				return null;				
			} catch (AulaException e) {
				iniciarParametros();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
				return "irAdministracionAula";
			}
                
//			if (aafAulaGrabada != null) { //Si se grabo correctamente la nueva aula
//				try { //GRABAR LA  RELACION DEL AULA CON TODAS LAS HORAS_CLASE EXISTENTES EN BDD.
//					if (servAafHoraClaseAulaServicio.grabar(aafAulaGrabada, aafListHoraClase)) {
//						iniciarParametros();
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.crear.aula.validacion.exitoso")));
//						
//					}
//				} catch (HoraClaseAulaException e) {
//					iniciarParametros();
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError(e.getMessage());
//					return "irAdministracionAula";
//				}
//
//			} else {//No se grabo el Aula correctamente
//				iniciarParametros();
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.crear.aula.validacion.sin.exito")));
//				return "irAdministracionAula";
//			}
		} else { //No existe horas_clase en la BDD.
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.crear.aula.validacion.sin.horas.clase")));
		}
		
		return "irAdministracionAula";
	}
	
	
	
	/**
	 * Método que elimina Aula
	 * navagación al xhtml AdministracionAula
	 */
	public String eliminarAula() {
		aafListHorarioAcademico = null;
		aafListHoraClaseAulaEliminar = new ArrayList<HoraClaseAula>();
	
		// LISTO HORAS CLASE AULA DEL AULA A ELIMINAR.
		try {
			aafListHoraClaseAulaEliminar = servAafHoraClaseAulaServicio.ListarHoraClaseAulaPIdAla(aafAulaDtoEliminar.getAlaId());
		} catch (HoraClaseAulaException e1) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return "irAdministracionAula";
		} catch (HoraClaseAulaNoEncontradoException e1) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return "irAdministracionAula";
		}
		
		// BUSCO SI ALGUNA HORA CLASE AULA, ESTA VINCULADA EN LA TABLA HORARIO ACADEMICO
		boolean encontrado = false;
		for (HoraClaseAula horaClaseAula : aafListHoraClaseAulaEliminar) {
			try {
				aafListHorarioAcademico = servAafHorarioAcademicoServicio.listarHorarioAcademicoXhoraClaseAulaId(horaClaseAula.getHoclalId());
				if (aafListHorarioAcademico != null) {
					encontrado = true;
					break;
				}
			} catch (HorarioAcademicoNoEncontradoException e) {
				encontrado = false;
			} catch (HorarioAcademicoException e) {
				iniciarParametros();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
				return "irAdministracionAula";
			}
		}

		try { // SI NO SE ENCONTRO HORA_CLASE_AULA RALACIONADO A HORARIO
				// ACADEMICO SE PROCEDE A BORRAR EL AULA.
			if (encontrado == false) {
				servAafAulaServicio.eliminarAula(aafAulaDtoEliminar.getAlaId(), aafListHoraClaseAulaEliminar);
				iniciarParametros();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.eliminar.aula.exitoso")));
			} else {// CASO CONTRARIO SE PRESENTE ERROR DE ELIMINACION.
				iniciarParametros();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionAula.eliminar.aula.ocupada")));
			}
		} catch (AulaValidacionException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaNoEncontradoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
				
		return "irAdministracionAula";
	}
	
	// ********************************************************************/
		// *********************** METODOS AUXILIARES *************************/
		// ********************************************************************/
			
		/**
		* verifica que haga click en el boton nuevo paralelo
		*/
		public String verificarClickNuevoAula(){
			aafValidadorClick= 1;
			return null;
		}
		
		/**
		* verifica que haga click en el boton editar paralelo
		*/
		public String verificarClickEditarAula(){
		aafValidadorClick= 2;
			return null;
		}
	
		/**
		 * verifica que haga click en el boton para eliminar AUla
		 */
		
		public String verificarClickEliminarAula(AulaDto aula){
			aafAulaDtoEliminar=aula;
			aafValidadorClick = 3;
			return null;
		}
		
		/**
	 * setea el verificador del click a 0 para nuevas validaciones
	 */
		public void setearVerificadorClick(){
			aafValidadorClick = 0;
		}
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	
		public Usuario getAafUsuario() {
			return aafUsuario;
		}

		public void setAafUsuario(Usuario aafUsuario) {
			this.aafUsuario = aafUsuario;
		}

		public DependenciaDto getAafDependenciaDtoBuscar() {
			return aafDependenciaDtoBuscar;
		}

		public void setAafDependenciaDtoBuscar(DependenciaDto aafDependenciaDtoBuscar) {
			this.aafDependenciaDtoBuscar = aafDependenciaDtoBuscar;
		}

		public EdificioDto getAafEdificioDtoBuscar() {
			return aafEdificioDtoBuscar;
		}

		public void setAafEdificioDtoBuscar(EdificioDto aafEdificioDtoBuscar) {
			this.aafEdificioDtoBuscar = aafEdificioDtoBuscar;
		}

		public List<EdificioDto> getAafListEdificioDto() {
			aafListEdificioDto = aafListEdificioDto==null?(new ArrayList<EdificioDto>()):aafListEdificioDto;
			return aafListEdificioDto;
		}

		public void setAafListEdificioDto(List<EdificioDto> aafListEdificioDto) {
			this.aafListEdificioDto = aafListEdificioDto;
		}

		public List<AulaDto> getAafListAulaDto() {
			aafListAulaDto = aafListAulaDto==null?(new ArrayList<AulaDto>()):aafListAulaDto;
			return aafListAulaDto;
		}

		public void setAafListAulaDto(List<AulaDto> aafListAulaDto) {
			this.aafListAulaDto = aafListAulaDto;
		}

		public List<DependenciaDto> getAafListDependenciaDto() {
			aafListDependenciaDto = aafListDependenciaDto==null?(new ArrayList<DependenciaDto>()):aafListDependenciaDto;
			return aafListDependenciaDto;
		}

		public void setAafListDependenciaDto(List<DependenciaDto> aafListDependenciaDto) {
			this.aafListDependenciaDto = aafListDependenciaDto;
		}

		public AulaDto getAafAulaDto() {
			return aafAulaDto;
		}

		public void setAafAulaDto(AulaDto aafAulaDto) {
			this.aafAulaDto = aafAulaDto;
		}

		public Integer getAafValidadorClick() {
			return aafValidadorClick;
		}

		public void setAafValidadorClick(Integer aafValidadorClick) {
			this.aafValidadorClick = aafValidadorClick;
		}

		public AulaDto getAafAulaDtoEditar() {
			return aafAulaDtoEditar;
		}

		public void setAafAulaDtoEditar(AulaDto aafAulaDtoEditar) {
			this.aafAulaDtoEditar = aafAulaDtoEditar;
		}
		
		public AulaDto getAafAulaDtoNuevo() {
			return aafAulaDtoNuevo;
		}

		public void setAafAulaDtoNuevo(AulaDto aafAulaDtoNuevo) {
			this.aafAulaDtoNuevo = aafAulaDtoNuevo;
		}

		public Edificio getAafEdificio() {
			return aafEdificio;
		}

		public void setAafEdificio(Edificio aafEdificio) {
			this.aafEdificio = aafEdificio;
		}

		
		public List<HoraClase> getAafListHoraClase() {
			aafListHoraClase = aafListHoraClase==null?(new ArrayList<HoraClase>()):aafListHoraClase;
			return aafListHoraClase;
		}

		public void setAafListHoraClase(List<HoraClase> aafListHoraClase) {
			this.aafListHoraClase = aafListHoraClase;
		}

		public Aula getAafAulaGrabada() {
			return aafAulaGrabada;
		}

		public void setAafAulaGrabada(Aula aafAulaGrabada) {
			this.aafAulaGrabada = aafAulaGrabada;
		}

		public AulaDto getAafAulaDtoEliminar() {
			return aafAulaDtoEliminar;
		}

		public void setAafAulaDtoEliminar(AulaDto aafAulaDtoEliminar) {
			this.aafAulaDtoEliminar = aafAulaDtoEliminar;
		}

		public List<HoraClaseAula> getAafListHoraClaseAulaEditar() {
			aafListHoraClaseAulaEditar = aafListHoraClaseAulaEditar==null?(new ArrayList<HoraClaseAula>()):aafListHoraClaseAulaEditar;
			return aafListHoraClaseAulaEditar;
		}

		public void setAafListHoraClaseAulaEditar(List<HoraClaseAula> aafListHoraClaseAulaEditar) {
			this.aafListHoraClaseAulaEditar = aafListHoraClaseAulaEditar;
		}

		public List<HoraClaseAula> getAafListHoraClaseAulaEliminar() {
			aafListHoraClaseAulaEliminar = aafListHoraClaseAulaEliminar==null?(new ArrayList<HoraClaseAula>()):aafListHoraClaseAulaEliminar;
			return aafListHoraClaseAulaEliminar;
		}

		public void setAafListHoraClaseAulaEliminar(List<HoraClaseAula> aafListHoraClaseAulaEliminar) {
			this.aafListHoraClaseAulaEliminar = aafListHoraClaseAulaEliminar;
		}

		public List<HorarioAcademico> getAafListHorarioAcademico() {
			aafListHorarioAcademico = aafListHorarioAcademico==null?(new ArrayList<HorarioAcademico>()):aafListHorarioAcademico;
			return aafListHorarioAcademico;
		}

		public void setAafListHorarioAcademico(List<HorarioAcademico> aafListHorarioAcademico) {
			this.aafListHorarioAcademico = aafListHorarioAcademico;
		}

	
		
		
		
		
		
}
	
	
	
	
	
	
	
	
	
	
