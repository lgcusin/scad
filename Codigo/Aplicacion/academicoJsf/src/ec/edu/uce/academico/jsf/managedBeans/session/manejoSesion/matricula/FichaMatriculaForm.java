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
   
 ARCHIVO:     FichaMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-03-2017			 David Arellano                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteRegistroMatriculaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) FichaMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author darellano.
 * @version 1.0
 */

@ManagedBean(name = "fichaMatriculaForm")
@SessionScoped
public class FichaMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private PeriodoAcademico fmfPeriodoAcademicoBuscar;
	private CarreraDto fmfCarreraDtoBuscar;
	private List<PeriodoAcademico> fmfListPeriodoAcademico;
	private List<PeriodoAcademicoDto> fmfListPeriodoAcademicoDto;
	private List<CarreraDto> fmfListCarreraDto;
	private List<FichaMatriculaDto> fmfListMatriculaDto;
	private Usuario fmfUsuario;
	private List<EstudianteJdbcDto> fmfListEstudianteDto; 
	private String fmfDesactivar = "false";
	private CarreraDto fmfCarreraEstudiante;
	private int fmfActivadorReporte; 
	private String fmfPracDescripcion;
	private int fmfPracId;
	private String fmfReporte;
	private String fmfJasper;
	private FichaMatriculaDto fmfFichaMatriculaSeleccion;
	private Carrera fmfCarreraSiiu;
	private boolean fmfTipoEstudiante;
	
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
	FichaMatriculaDtoServicioJdbc servFmfFichaMatriculaDto;
	@EJB 
	PeriodoAcademicoServicio servFmfPeriodoAcademico;
	@EJB 
	EstudianteDtoServicioJdbc  servFmfEstudianteDto;
	@EJB
	PeriodoAcademicoDtoServicioJdbc servFmfPeridoAcademicoServicioJDB;
	@EJB
	CarreraDtoServicioJdbc servFmfCarreraDtoServicioJdbc;
	@EJB
	NivelServicio servFmfNivelServicio;
	@EJB
	MatriculaServicioJdbc servFmfMatriculaServicioJdbc;
	@EJB
	FichaInscripcionServicio servFichaInscripcionServicio;
	@EJB
	FichaEstudianteServicio servFichaEstudianteServicio;
	@EJB
	CarreraServicio servCarreraServicio;
	@EJB
	UsuarioRolServicio servUsuarioRolServicio;
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String iniciarListarMatricula(Usuario usuario) {
		try {
			fmfActivadorReporte = 0;
			//Guardamos el usuario en una variable
			fmfUsuario = usuario;
			//Inicializar la lista de periodos academicos
//			try {
//				fmfListPeriodoAcademicoDto = servFmfPeridoAcademicoServicioJDB.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			} catch (PeriodoAcademicoDtoJdbcException e) {
//			} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
//			}
			
			
//			fmfListPeriodoAcademico = servFmfPeriodoAcademico.listarTodos();
			//Inicializar la lista de matriculas del estudiante
			UsuarioRol usroAux = new UsuarioRol();
			try {
				usroAux = servUsuarioRolServicio.buscarXUsuarioXrol(fmfUsuario.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
				if(usroAux.getUsroRol().getRolId()==RolConstantes.ROL_ESTUD_PREGRADO_VALUE){
					fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraFullNivelacion(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
				}
			} catch (UsuarioRolNoEncontradoException e) {
				try {
					fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);	
				} catch (Exception e2) {
				}
				
			} catch (UsuarioRolException e) {
			}
			
			//BUSCO FICHAS MATRICULA SIN PASAR POR FICHA INSCRIPCION
	//		fmfListMatriculaDto = servFmfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);

			
			//Instanciar los objetos de busqueda
			fmfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
			fmfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
			//Instancio la lista llamando al metodo get
			getFmfListCarreraDto();
			try {
				//Obtener la lista de carreras a las que el estudiante está inscrito
				for (FichaMatriculaDto itemMatricula : fmfListMatriculaDto) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
					Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
					for (CarreraDto itemCarrera : fmfListCarreraDto) { // recorro la lista de carreras a las que el estudiante esta matriculado 
						if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
							encontro = true; // asigno el booleano a verdado
						}
					} //fin recorrido de lista auxiliar de carreras
					if(encontro == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
						CarreraDto carreraAgregar = new CarreraDto();
						carreraAgregar.setCrrId(itemMatricula.getCrrId());
						carreraAgregar.setCrrDetalle(itemMatricula.getCrrDetalle());
						carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
						fmfListCarreraDto.add(carreraAgregar);
					}
				}
				
				
				//INSTANCIO (new) LA LISTA PERIODO ACADEMICO, LLAMANDO AL METODO PARA LLENARLO EN EL FOR
				getFmfListPeriodoAcademicoDto();
				//PERMITE OBTENER LA LISTA DE PERIODOS EN LOS QUE EL ESTUDIANTES ESTA MATRICULADO
				for (FichaMatriculaDto itemMatricula : fmfListMatriculaDto) { // recorro la lista de matriculas del estudiante, para comparar el periodo.
					Boolean encontro = false; // boolenado para determinar si le encontro en la lista de periodos
					for (PeriodoAcademicoDto itemPeriodoAcademico : fmfListPeriodoAcademicoDto) { // recorro la lista
						if(itemMatricula.getPracId() == itemPeriodoAcademico.getPracId()){ // si encuentro el periodo ya en la lista de periodos.
							encontro = true; // asigno el booleano a verdadero.
						}
					} //fin recorrido de lista auxiliar 
					if(encontro == false){ // si no econtró el periodo de la matricula en la lista de periodos agrego ese periodos a la lista.
						PeriodoAcademicoDto periodoAgregar = new PeriodoAcademicoDto();
						periodoAgregar.setPracId(itemMatricula.getPracId());  //Instancio el idPeriodo de la matricula en id periodoAgregar.
						periodoAgregar.setPracDescripcion(itemMatricula.getPracDescripcion()); //Instancio la descripcionPeriodo de la matricula en la descripcion del  periodoAgregar.
						fmfListPeriodoAcademicoDto.add(periodoAgregar);   //Agrego el objeto PeriodoAcademico a la lista de periodos.
					}
				}
			} catch (Exception e) {
				FacesUtil.mensajeError("Usted no posee matrículas registradas en el sistema académico.");
				return null;
			}
			
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} 
		
		return "irMatricula";
	}


	
	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		try {
			//Busco las matriculas con los parámetro ingresados en el panel de búsqyeda
			List<Rol> tipoRol = new ArrayList<>();
			tipoRol = servUsuarioRolServicio.buscarRolesXUsuario(fmfUsuario.getUsrId());
			
			for (Rol item : tipoRol) {
				if(item.getRolId() == RolConstantes.ROL_ESTUD_PREGRADO_VALUE){
					fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraFull(fmfPeriodoAcademicoBuscar.getPracId(), fmfUsuario.getUsrPersona().getPrsIdentificacion(), fmfCarreraDtoBuscar.getCrrId());
					break;
				}
				if(item.getRolId() == RolConstantes.ROL_ESTUD_VALUE){
					fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, fmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
					break;
				}
			}
			
			
//			fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraFull(fmfPeriodoAcademicoBuscar.getPracId(), fmfUsuario.getUsrPersona().getPrsIdentificacion(), fmfCarreraDtoBuscar.getCrrId());
//			
//	//		fmfListMatriculaDto = servFmfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(fmfPeriodoAcademicoBuscar.getPracId(), fmfUsuario.getUsrPersona().getPrsIdentificacion(), fmfCarreraDtoBuscar.getCrrId());

			
			if(fmfListMatriculaDto.size() == 0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.buscar.matriculas.no.encontrado.exception")));
				fmfListMatriculaDto = null;
			}
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.buscar.ficha.matricula.exception")));
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		fmfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		fmfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		fmfListMatriculaDto = null;
		buscar();
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		fmfListMatriculaDto = null;
//		fmfListPeriodoAcademico = null;
		fmfListPeriodoAcademicoDto= null;
		fmfListCarreraDto = null;
		fmfPeriodoAcademicoBuscar = null;
		fmfCarreraDtoBuscar = null;
		fmfUsuario = null;
		fmfListEstudianteDto = null;
		return "irInicio";
	}
	
	/**
	 * Visualiza la matricula generada del estudiante
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irVerMatricula(FichaMatriculaDto fichaMatriculaDto){
		try {
			
			fmfFichaMatriculaSeleccion = fichaMatriculaDto;
			//lleno la lista del objeto de estudiante (tiene algunas entidades)
			
			//fmfListEstudianteDto = servFmfEstudianteDto.buscarEstudianteXIdPersonaXIdMatricula(fmfUsuario.getUsrPersona().getPrsId(), fichaMatriculaDto.getFcmtId());
			
			fmfPracDescripcion = fichaMatriculaDto.getPracDescripcion();
			fmfPracId = fichaMatriculaDto.getPracId();
			fmfCarreraEstudiante = servFmfCarreraDtoServicioJdbc.buscarCarreraXIdentificacionXcrrId(fmfUsuario.getUsrPersona().getPrsIdentificacion(),fichaMatriculaDto.getCrrId());
			fmfListEstudianteDto = servFmfEstudianteDto.buscarEstudianteXIdPersonaXIdMatricula(fmfUsuario.getUsrPersona().getPrsId(),fichaMatriculaDto.getFcmtId(), fichaMatriculaDto.getPracId());
//			fmfListEstudianteDto = servFmfEstudianteDto.listaMateriasXfcmtXidentificacionFull(fmfUsuario.getUsrPersona().getPrsIdentificacion(),fichaMatriculaDto.getFcmtId());
			FichaEstudiante fcesAux;
			try {
				fcesAux = servFichaEstudianteServicio.buscarPorId(fichaMatriculaDto.getFcesId());
				FichaInscripcion fcinAux = servFichaInscripcionServicio.buscarXId(fcesAux.getFcesFichaInscripcion().getFcinId());
				if(fcinAux.getFcinTipo()==3){
					fmfTipoEstudiante=true;
				}else{
					fmfTipoEstudiante=false;
				}
				try {
					fmfCarreraSiiu = servCarreraServicio.buscarPorId(fcinAux.getFcinCarreraSiiu());
				} catch (CarreraNoEncontradoException e) {
				} catch (CarreraException e) {
				}
			} catch (FichaEstudianteNoEncontradoException | FichaEstudianteException e) {
			}
		} catch (EstudianteDtoJdbcException e) {
			// TODO Auto-generated catch block VINICIO
			e.printStackTrace();
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			// TODO Auto-generated catch block VINICIO
			e.printStackTrace();
		} catch (CarreraDtoJdbcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "irVerMatricula";
	}
	
	//TODO: CAMBIAR EL METODO VINICIO
	public void nada(){
		fmfDesactivar = "true";
		fmfReporte = "GENERAR_MATRICULA";
		if(fmfTipoEstudiante){
			fmfJasper = "reporteRegistroMatriculaNivelacion";
			ReporteRegistroMatriculaForm.generarReporteRegistroMatriculaNivelacion(fmfCarreraSiiu.getCrrDescripcion(), fmfListEstudianteDto, fmfUsuario.getUsrNick(), fmfCarreraEstudiante, fmfPracDescripcion, obtenerDescripcionNivel(fmfFichaMatriculaSeleccion.getFcmtNivelUbicacion()), fmfFichaMatriculaSeleccion.getTpgrDescripcion());
		}else{
			fmfJasper = "reporteRegistroMatricula";	
			ReporteRegistroMatriculaForm.generarReporteRegistroMatriculaFull(fmfListEstudianteDto, fmfUsuario.getUsrNick(), fmfCarreraEstudiante, fmfPracDescripcion, obtenerDescripcionNivel(fmfFichaMatriculaSeleccion.getFcmtNivelUbicacion()), fmfFichaMatriculaSeleccion.getTpgrDescripcion());
		}
		
		
		fmfActivadorReporte = 1;
	}
	
	
	public void generarordenCobro(){
		try {
			fmfDesactivar = "true";
			fmfReporte = "GENERAR_ORDEN_COBRO";
			fmfJasper = "reporteOrdenCobro";
			ComprobantePago comprobante = servFmfMatriculaServicioJdbc.buscarComprobantePago(fmfUsuario.getUsrIdentificacion(), fmfCarreraEstudiante.getCrrId(), fmfPracId );
			ReporteRegistroMatriculaForm.generarReporteOrdenCobroFull(fmfListEstudianteDto, fmfUsuario, fmfCarreraEstudiante, fmfPracDescripcion, obtenerDescripcionNivel(fmfFichaMatriculaSeleccion.getFcmtNivelUbicacion()), fmfFichaMatriculaSeleccion.getTpgrDescripcion(), comprobante);
			fmfActivadorReporte = 1;
		} catch (ParaleloNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParaleloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String obtenerDescripcionNivel(int nivelNumeral){
		String descripcion = null;
		try {
			Nivel nivel  = servFmfNivelServicio.listarNivelXNumeral(nivelNumeral);
			descripcion = nivel.getNvlDescripcion();
		} catch (NivelNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NivelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return descripcion;
	}
	
	
	public String obtenerGratuidad(int nivelNumeral){
		String descripcion = null;
		try {
			Nivel nivel  = servFmfNivelServicio.listarNivelXNumeral(nivelNumeral);
			descripcion = nivel.getNvlDescripcion();
		} catch (NivelNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NivelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return descripcion;
	}
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String regresarMatricula(){
		fmfActivadorReporte = 0;
		fmfDesactivar = "false";
		fmfListEstudianteDto = null;
		return "regrasarListarMatricula";
	}
	
	
	
	
	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	public PeriodoAcademico getFmfPeriodoAcademicoBuscar() {
		return fmfPeriodoAcademicoBuscar;
	}

	public void setFmfPeriodoAcademicoBuscar(PeriodoAcademico fmfPeriodoAcademicoBuscar) {
		this.fmfPeriodoAcademicoBuscar = fmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getFmfCarreraDtoBuscar() {
		return fmfCarreraDtoBuscar;
	}

	public void setFmfCarreraDtoBuscar(CarreraDto fmfCarreraDtoBuscar) {
		this.fmfCarreraDtoBuscar = fmfCarreraDtoBuscar;
	}

	public List<PeriodoAcademico> getFmfListPeriodoAcademico() {
		fmfListPeriodoAcademico = fmfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):fmfListPeriodoAcademico;
		return fmfListPeriodoAcademico;
	}

	public void setFmfListPeriodoAcademico(List<PeriodoAcademico> fmfListPeriodoAcademico) {
		this.fmfListPeriodoAcademico = fmfListPeriodoAcademico;
	}

	public List<CarreraDto> getFmfListCarreraDto() {
		fmfListCarreraDto = fmfListCarreraDto==null?(new ArrayList<CarreraDto>()):fmfListCarreraDto;
		return fmfListCarreraDto;
	}

	public void setFmfListCarreraDto(List<CarreraDto> fmfListCarreraDto) {
		this.fmfListCarreraDto = fmfListCarreraDto;
	}

	public List<FichaMatriculaDto> getFmfListMatriculaDto() {
		fmfListMatriculaDto = fmfListMatriculaDto==null?(new ArrayList<FichaMatriculaDto>()):fmfListMatriculaDto;
		return fmfListMatriculaDto;
	}

	public void setFmfListMatriculaDto(List<FichaMatriculaDto> fmfListMatriculaDto) {
		this.fmfListMatriculaDto = fmfListMatriculaDto;
	}

	public Usuario getFmfUsuario() {
		return fmfUsuario;
	}

	public void setFmfUsuario(Usuario fmfUsuario) {
		this.fmfUsuario = fmfUsuario;
	}

	public List<EstudianteJdbcDto> getFmfListEstudianteDto() {
		fmfListEstudianteDto = fmfListEstudianteDto==null?(new ArrayList<EstudianteJdbcDto>()):fmfListEstudianteDto;
		return fmfListEstudianteDto;
	}

	public void setFmfListEstudianteDto(List<EstudianteJdbcDto> fmfListEstudianteDto) {
		this.fmfListEstudianteDto = fmfListEstudianteDto;
	}

	public String getFmfDesactivar() {
		return fmfDesactivar;
	}

	public void setFmfDesactivar(String fmfDesactivar) {
		this.fmfDesactivar = fmfDesactivar;
	}

	public int getFmfActivadorReporte() {
		return fmfActivadorReporte;
	}

	public void setFmfActivadorReporte(int fmfActivadorReporte) {
		this.fmfActivadorReporte = fmfActivadorReporte;
	}

	public List<PeriodoAcademicoDto> getFmfListPeriodoAcademicoDto() {
		fmfListPeriodoAcademicoDto = fmfListPeriodoAcademicoDto==null?(new ArrayList<PeriodoAcademicoDto>()):fmfListPeriodoAcademicoDto;
		return fmfListPeriodoAcademicoDto;
	}

	public void setFmfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> fmfListPeriodoAcademicoDto) {
		this.fmfListPeriodoAcademicoDto = fmfListPeriodoAcademicoDto;
	}

	public CarreraDto getFmfCarreraEstudiante() {
		return fmfCarreraEstudiante;
	}

	public void setFmfCarreraEstudiante(CarreraDto fmfCarreraEstudiante) {
		this.fmfCarreraEstudiante = fmfCarreraEstudiante;
	}

	public String getPracDescripcion() {
		return fmfPracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.fmfPracDescripcion = pracDescripcion;
	}

	public String getFmfReporte() {
		return fmfReporte;
	}

	public void setFmfReporte(String fmfReporte) {
		this.fmfReporte = fmfReporte;
	}

	public String getFmfJasper() {
		return fmfJasper;
	}

	public void setFmfJasper(String fmfJasper) {
		this.fmfJasper = fmfJasper;
	}



	public Carrera getFmfCarreraSiiu() {
		return fmfCarreraSiiu;
	}



	public void setFmfCarreraSiiu(Carrera fmfCarreraSiiu) {
		this.fmfCarreraSiiu = fmfCarreraSiiu;
	}
	
	
}
