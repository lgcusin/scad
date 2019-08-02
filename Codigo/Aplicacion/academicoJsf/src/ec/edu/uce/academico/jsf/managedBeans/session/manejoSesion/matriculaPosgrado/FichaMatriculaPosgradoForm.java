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
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matriculaPosgrado;

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
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteRegistroMatriculaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) FichaMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author darellano.
 * @version 1.0
 */

@ManagedBean(name = "fichaMatriculaPosgradoForm")
@SessionScoped
public class FichaMatriculaPosgradoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private PeriodoAcademico fmfPeriodoAcademicoBuscar;
	private CarreraDto fmfCarreraDtoBuscar;
	private List<PeriodoAcademico> fmfListPeriodoAcademico;
	private List<CarreraDto> fmfListCarreraDto;
	private List<FichaMatriculaDto> fmfListMatriculaDto;
	private Usuario fmfUsuario;
	private List<EstudianteJdbcDto> fmfListEstudianteDto; 
	private String fmfDesactivar = "false";
	private CarreraDto fmfCarreraEstudiante;
	private String pracDescripcion;
	
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
	CarreraDtoServicioJdbc servFmfCarreraDtoServicioJdbc;
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String iniciarListarMatricula(Usuario usuario) {
		try {
			//Guardamos el usuario en una variable
			fmfUsuario = usuario;
			//Inicializar la lista de periodos academicos
			fmfListPeriodoAcademico = servFmfPeriodoAcademico.listarTodosPosgradoActivo();
			//Inicializar la lista de matriculas del estudiante
			fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraPosgrado(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			//Instanciar los objetos de busqueda
			fmfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
			fmfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
			//Instancio la lista llamando al metodo get
			getFmfListCarreraDto();
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
					fmfListCarreraDto.add(carreraAgregar);
				}
			}
			
			
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} 
		
		return "irFcmtPosgrado";
	}


	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String iniciarReimpresionComprobante(Usuario usuario) {
		try {
			//Guardamos el usuario en una variable
			fmfUsuario = usuario;
			//Inicializar la lista de periodos academicos
			fmfListPeriodoAcademico = servFmfPeriodoAcademico.listarTodosPosgradoActivo();
			//Inicializar la lista de matriculas del estudiante
			fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraPosgrado(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			//Instanciar los objetos de busqueda
			fmfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
			fmfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
			//Instancio la lista llamando al metodo get
			getFmfListCarreraDto();
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
					fmfListCarreraDto.add(carreraAgregar);
				}
			}
			
			
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} 
		
		return "irReimpresionPosgrado";
	}
	
	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		try {
			//Busco las matriculas con los parámetro ingresados en el panel de búsqyeda
			fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraPosgrado(fmfPeriodoAcademicoBuscar.getPracId(), fmfUsuario.getUsrPersona().getPrsIdentificacion(), fmfCarreraDtoBuscar.getCrrId());
			if(fmfListMatriculaDto.size() == 0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.buscar.matriculas.no.encontrado.exception")));
				fmfListMatriculaDto = null;
			}
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.buscar.ficha.matricula.exception")));
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
		fmfListPeriodoAcademico = null;
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
	@SuppressWarnings("static-access")
	public String irVerMatricula(FichaMatriculaDto fichaMatriculaDto){
		try {
			ReporteRegistroMatriculaForm reporte = new ReporteRegistroMatriculaForm();
			//lleno la lista del objeto de estudiante (tiene algunas entidades)
			
			//fmfListEstudianteDto = servFmfEstudianteDto.buscarEstudianteXIdPersonaXIdMatricula(fmfUsuario.getUsrPersona().getPrsId(), fichaMatriculaDto.getFcmtId());
			
			fmfCarreraEstudiante = servFmfCarreraDtoServicioJdbc.buscarCarreraXIdentificacionXcrrId(fmfUsuario.getUsrPersona().getPrsIdentificacion(),fichaMatriculaDto.getCrrId());
			fmfListEstudianteDto = servFmfEstudianteDto.buscarEstudianteXIdPersonaXIdMatriculaPosgrado(fmfUsuario.getUsrPersona().getPrsId(),fichaMatriculaDto.getFcmtId(), fichaMatriculaDto.getPracId());
		
			String usrNick = fmfUsuario.getUsrNick();
			pracDescripcion = fichaMatriculaDto.getPracDescripcion();
			reporte.generarReporteRegistroMatricula(fmfListEstudianteDto, usrNick,fmfCarreraEstudiante,  pracDescripcion);
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
	}
	
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String regresarMatricula(){
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



	public CarreraDto getFmfCarreraEstudiante() {
		return fmfCarreraEstudiante;
	}



	public void setFmfCarreraEstudiante(CarreraDto fmfCarreraEstudiante) {
		this.fmfCarreraEstudiante = fmfCarreraEstudiante;
	}



	public String getPracDescripcion() {
		return pracDescripcion;
	}



	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}
	
	
	
}
