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
 24-04-2018			 Arturo Villafuerte                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula.pregrado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteRegistroMatriculaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) FichaMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "fichaMatriculaPreForm")
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
	
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	private @EJB 	FichaMatriculaDtoServicioJdbc servFmfFichaMatriculaDto;
	private @EJB 	PeriodoAcademicoServicio servFmfPeriodoAcademico;
	private @EJB 	EstudianteDtoServicioJdbc  servFmfEstudianteDto;
	private @EJB	PeriodoAcademicoDtoServicioJdbc servFmfPeridoAcademicoServicioJDB;
	private @EJB	CarreraDtoServicioJdbc servFmfCarreraDtoServicioJdbc;
	private @EJB	NivelServicio servFmfNivelServicio;
	private @EJB	MatriculaServicioJdbc servFmfMatriculaServicioJdbc;

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String iniciarListarMatricula(Usuario usuario) {
		
		fmfActivadorReporte = 0;
		fmfUsuario = usuario;
		
		
		try {
			List<FichaMatriculaDto> matriculas = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraFull(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			if (!matriculas.isEmpty()) {
				matriculas.sort(Comparator.comparing(FichaMatriculaDto::getCrrDescripcion).thenComparing(Comparator.comparing(FichaMatriculaDto::getPracId)).reversed());
				fmfListMatriculaDto = matriculas;
				
				fmfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
				fmfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
				
				fmfListCarreraDto = filtrarCarreras(fmfListMatriculaDto);
				fmfListPeriodoAcademicoDto = filtrarPeriodos(fmfListMatriculaDto);
			}
			 
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Matricula.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} 

		return "irMatriculaPregrado";
	}

	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		try {
			fmfListMatriculaDto = servFmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraFull(fmfPeriodoAcademicoBuscar.getPracId(), fmfUsuario.getUsrPersona().getPrsIdentificacion(), fmfCarreraDtoBuscar.getCrrId());
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
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		fmfListMatriculaDto = null;
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
		String retorno = null;
		
		try {
			
			fmfFichaMatriculaSeleccion = fichaMatriculaDto;
			fmfPracDescripcion = fichaMatriculaDto.getPracDescripcion();
			fmfPracId = fichaMatriculaDto.getPracId();
			fmfCarreraEstudiante = servFmfCarreraDtoServicioJdbc.buscarCarreraXIdentificacionXcrrId(fmfUsuario.getUsrPersona().getPrsIdentificacion(),fichaMatriculaDto.getCrrId());
			fmfListEstudianteDto = servFmfEstudianteDto.listaMateriasXfcmtXidentificacionFull(fmfUsuario.getUsrPersona().getPrsIdentificacion(),fichaMatriculaDto.getFcmtId());
			retorno = "irVerMatricula";		
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Estudiante homologado sin Asignaturas.");
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
		
	}
	
	//TODO: CAMBIAR EL METODO VINICIO
	public void nada(){
		fmfDesactivar = "true";
		fmfReporte = "GENERAR_MATRICULA";
		fmfJasper = "reporteRegistroMatricula";
		ReporteRegistroMatriculaForm.generarReporteRegistroMatriculaFull(fmfListEstudianteDto, fmfUsuario.getUsrNick(), fmfCarreraEstudiante, fmfPracDescripcion, obtenerDescripcionNivel(fmfFichaMatriculaSeleccion.getFcmtNivelUbicacion()), fmfFichaMatriculaSeleccion.getTpgrDescripcion());
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
			e.printStackTrace();
		} catch (ParaleloException e) {
			e.printStackTrace();
		}
	}
	
	public String obtenerDescripcionNivel(int nivelNumeral){
		String descripcion = null;
		try {
			Nivel nivel  = servFmfNivelServicio.buscarPorId(nivelNumeral);
			descripcion = nivel.getNvlDescripcion();
		} catch (NivelNoEncontradoException e) {
			e.printStackTrace();
		} catch (NivelException e) {
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
	private List<PeriodoAcademicoDto> filtrarPeriodos(List<FichaMatriculaDto> matriculas) {
		List<PeriodoAcademicoDto> periodos = new ArrayList<>();

		if (!matriculas.isEmpty()) {
			Map<Integer, FichaMatriculaDto> mapPeriodos =  new HashMap<Integer, FichaMatriculaDto>();
			for (FichaMatriculaDto matricula : matriculas) {
				mapPeriodos.put(matricula.getPracId(), matricula);
			}
			for (Entry<Integer, FichaMatriculaDto> item : mapPeriodos.entrySet()) {
				PeriodoAcademicoDto periodo = new PeriodoAcademicoDto();
				periodo.setPracId(item.getValue().getPracId());
				periodo.setPracDescripcion(item.getValue().getPracDescripcion()); 
				periodos.add(periodo);   
			}
		}

		return periodos;
	}



	private List<CarreraDto> filtrarCarreras(List<FichaMatriculaDto> matriculas) {
		List<CarreraDto> carreras = new ArrayList<>();

		if (!matriculas.isEmpty()) {
			Map<Integer, FichaMatriculaDto> mapCarreras =  new HashMap<Integer, FichaMatriculaDto>();
			for (FichaMatriculaDto matricula : matriculas) {
				mapCarreras.put(matricula.getCrrId(), matricula);
			}
			for (Entry<Integer, FichaMatriculaDto> item : mapCarreras.entrySet()) {
				CarreraDto carrera = new CarreraDto();
				carrera.setCrrId(item.getValue().getCrrId());
				carrera.setCrrDescripcion(item.getValue().getCrrDescripcion()); 
				carreras.add(carrera);   
			}
		}

		return carreras;
	}
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

	public FichaMatriculaDto getFmfFichaMatriculaSeleccion() {
		return fmfFichaMatriculaSeleccion;
	}

	public void setFmfFichaMatriculaSeleccion(FichaMatriculaDto fmfFichaMatriculaSeleccion) {
		this.fmfFichaMatriculaSeleccion = fmfFichaMatriculaSeleccion;
	}
	
	
}
