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
   
 ARCHIVO:     CertificadoMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-NOV-2018 			 Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.certificado;

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
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCertificadoMatriculaForm;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) CertificadoMatriculaForm.
 * Managed Bean que administra la visualización de los certificados de matricula PREGRADO y SUFICIENCIAS.
 * @author fgguzman.
 * @version 1.0
 */


@ManagedBean(name="certificadoMatriculaForm")
@SessionScoped
public class CertificadoMatriculaForm extends HistorialAcademicoForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	//****************************************************************/
	//************************ VARIABLES *****************************/
	//****************************************************************/
	private Usuario cmfUsuario;
	private EstudianteJdbcDto cmfEstudianteJdbcDto;
	private CarreraDto cmfCarreraDto;

	private Integer cmfTipoBusqueda;
	private Integer cmfActivarReporte;
	private Integer cmfCarreraId;
	private String cmfIdentificacion;
	private String cmfPrimerApellido;
	private String cmfNombreArchivo ;
	private String cmfNombreReporte;
	
	private List<Carrera> cmfListCarrera;
	private List<RecordEstudianteDto> cmfListCarreraDto;
	private List<RecordEstudianteDto> cmfListPeriodoAcademicoDto;
	private List<RecordEstudianteDto> cmfListRecordEstudianteDto;
	private List<EstudianteJdbcDto> cmfListEstudianteJdbcDto;
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private CarreraServicio servCarrera;
	@EJB private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB private MatriculaServicioJdbc servJdbcMatricula;

	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	

	public String irListarEstudiantes(Usuario usuario){

		try {

			cmfUsuario = usuario;
			cmfListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXUsuarioXRoles(cmfUsuario.getUsrId(), new Integer[]{RolConstantes.ROL_SECREABOGADO_VALUE, RolConstantes.ROL_SECRECARRERA_VALUE, RolConstantes.ROL_SECRESUFICIENCIAS_VALUE});
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				cmfListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			
			iniciarParametrosFormEstudiantes();
			
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
		return "irListarEstudiantesCertificadoMatricula";
	}
	
	public String irVerCarreras(EstudianteJdbcDto estudiante){
		cmfEstudianteJdbcDto = estudiante;
		
		 cmfListRecordEstudianteDto = cargarHistorialMatriculasSAIU(cmfEstudianteJdbcDto.getPrsIdentificacion());
		if (cmfListRecordEstudianteDto != null && cmfListRecordEstudianteDto.size() >0) {
			cmfListCarreraDto =  permisosVisualizacionRecord(cargarCarrerasPorMatriculas());
			return "irVerCarrerasCertificadoMatricula";
		}else {
			FacesUtil.mensajeInfo("El estudiante con identificación " + estudiante.getPrsIdentificacion() + " no tiene matriculas para mostrar.");
			return null;
		}
		
	}
	
	public String irVerMatriculas(RecordEstudianteDto carrera){
		cmfCarreraDto = carrera.getRcesCarreraDto();
		List<RecordEstudianteDto> auxCmfListPeriodoAcademicoDto = cargarMatriculasPorPeriodo(cmfListRecordEstudianteDto, cmfCarreraDto);
		if (auxCmfListPeriodoAcademicoDto != null && auxCmfListPeriodoAcademicoDto.size() >0 ) {
			auxCmfListPeriodoAcademicoDto.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracDescripcion));
			cmfListPeriodoAcademicoDto = auxCmfListPeriodoAcademicoDto;
			return "irVerMatriculasCertificadoMatricula";	
		}else {
			return null;
		}
		
	}
	
	public String irInicio(){
		vaciarParametrosFormEstudiantes();
		vaciarParametrosFormCarreras();
		vaciarParametrosFormPeriodos();
		cmfListRecordEstudianteDto = null;
		return "irInicio";
	}
	
	public String irEstudiantes(){
		vaciarParametrosFormEstudiantes();
		return "irEstudiantes";
	}
	
	public String irCarreras(){
		cmfActivarReporte = 0;	
		return "irCarreras";
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	private void iniciarParametrosFormEstudiantes(){
		cmfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		cmfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		cmfIdentificacion = new String();
		cmfPrimerApellido = new String();
		cmfListEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormEstudiantes(){
		cmfActivarReporte = null;
		cmfTipoBusqueda = null;
		cmfIdentificacion = null;
		cmfPrimerApellido = null;
		cmfListEstudianteJdbcDto = null;
	}
	
	public void limpiarParametrosFormEstudiantes(){
		cmfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		cmfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		cmfIdentificacion = new String();
		cmfPrimerApellido = new String();
		cmfListEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormCarreras(){
		cmfListCarreraDto = null;
		cmfEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormPeriodos(){
		cmfListPeriodoAcademicoDto = null;
		cmfEstudianteJdbcDto = null;
		cmfActivarReporte = null;
	}
	
	/**
	 * Método que permite habilitar el acceso de visualizacion de las carreras que puede emitir los certificados.
	 */
	private List<RecordEstudianteDto> permisosVisualizacionRecord(List<RecordEstudianteDto> carreras) {
		for (Carrera secretaria : cmfListCarrera) {
			for (RecordEstudianteDto estudiante : carreras) {
				if (secretaria.getCrrId()==estudiante.getRcesCarreraDto().getCrrId()) {
					estudiante.getRcesCarreraDto().setCrrAcceso(Boolean.TRUE);
				}
			}
		}
		return carreras;
	} 

	/**
	 * Método que permite realizar la búsqueda de estudiantes.
	 */
	public void buscarEstudiante(){
		
		if (cmfTipoBusqueda.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			String param = null;
			if (cmfTipoBusqueda == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = cmfIdentificacion;
			}else {
				param = cmfPrimerApellido;
			}
			
			List<EstudianteJdbcDto>  estudiantes = cargarEstudianteSAIU(param, cmfListCarrera, cmfTipoBusqueda);
			if (estudiantes != null && estudiantes.size() > 0) {
				cmfListEstudianteJdbcDto = estudiantes;
			}else {
				cmfListEstudianteJdbcDto = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar con la búsqueda.");
		}

	}
	
	public void busquedaPorIdentificacion(){
		
		if (cmfIdentificacion.length() > 0) {
			cmfPrimerApellido = new String();
			cmfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (cmfPrimerApellido.length() > 0) {
			cmfIdentificacion = new String();
			cmfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	

	private List<RecordEstudianteDto> cargarCarrerasPorMatriculas() {
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		Map<Integer, RecordEstudianteDto> mapCarreras =  new HashMap<Integer, RecordEstudianteDto>();

		for (RecordEstudianteDto it : cmfListRecordEstudianteDto) {
			mapCarreras.put(it.getRcesCarreraDto().getCrrId(), it);
		}
		for (Entry<Integer, RecordEstudianteDto> carrera : mapCarreras.entrySet()) {
			retorno.add(carrera.getValue());
		}
		return retorno;
	}
	
	private List<RecordEstudianteDto> cargarMatriculasPorPeriodo(List<RecordEstudianteDto> matriculas, CarreraDto carrera) {
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		Map<Integer, RecordEstudianteDto> mapPeriodos =  new HashMap<Integer, RecordEstudianteDto>();

		for (RecordEstudianteDto it : matriculas) {
			if (it.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
				mapPeriodos.put(it.getRcesPeriodoAcademicoDto().getPracId(), it);
			}
		}
		for (Entry<Integer, RecordEstudianteDto> periodo : mapPeriodos.entrySet()) {
			retorno.add(periodo.getValue());				
		}

		return retorno;
	}

	public void generarCertificadoMatricula(RecordEstudianteDto periodo) {
		cmfNombreArchivo  = "ReporteAprobacionCulturaFisica";
		cmfNombreReporte = "CERTIFICADOS_MATRICULA";

		PersonaDto secretario =  cargarSecretarioAbogado(periodo);

		CarreraDto carrera = new CarreraDto();
		carrera.setDpnId(periodo.getRcesDependenciaDto().getDpnId());
		carrera.setDpnDescripcion(periodo.getRcesDependenciaDto().getDpnDescripcion());
		carrera.setCrrId(periodo.getRcesCarreraDto().getCrrId());
		carrera.setCrrDescripcion(periodo.getRcesCarreraDto().getCrrDescripcion());

		FichaMatriculaDto matricula = new FichaMatriculaDto();
		matricula.setFcmtId(periodo.getRcesFichaMatriculaDto().getFcmtId());
		matricula.setFcmtModalidadLabel(periodo.getRcesFichaMatriculaDto().getFcmtModalidadLabel());
		matricula.setPracDescripcion(periodo.getRcesPeriodoAcademicoDto().getPracDescripcion());
		matricula.setNvlDescripcion(periodo.getRcesFichaMatriculaDto().getNvlDescripcion());
		
		List<RecordEstudianteDto> asignaturas = cargarAsignaturasPorPeriodo(periodo, carrera);
		int contador = 0; 
		for (RecordEstudianteDto item : asignaturas) {
			if (item.getRcesMateriaDto().getMtrEstado().intValue() == RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE) {
				contador = contador +1;
			}
		}
		
		if (contador == asignaturas.size()) {
			cmfActivarReporte = 0;
			FacesUtil.mensajeInfo("El estudiante no legalizó la matrícula en este período académico, por ello no se puede generar el Certificado.");
		}else {
			ReporteCertificadoMatriculaForm.generarReporteCretificadoMatricula(cmfUsuario, cmfEstudianteJdbcDto, matricula, carrera, secretario, asignaturas);
			cmfActivarReporte = 1;	
		}
		
		

	}
	
	private List<RecordEstudianteDto> cargarAsignaturasPorPeriodo(RecordEstudianteDto periodo, CarreraDto carrera) {
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		for (RecordEstudianteDto item : cmfListRecordEstudianteDto) {
			if (item.getRcesPeriodoAcademicoDto().getPracId() == periodo.getRcesPeriodoAcademicoDto().getPracId()
					&& item.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
				retorno.add(item);
			}
		}
		
		return retorno;
	}
	

	/**
	 * Mètodo que permite cargar datos del secretario abogado de la facultada
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarSecretarioAbogado(RecordEstudianteDto periodo){

		PersonaDto secretario  = null;
		try {
			secretario = servJdbcPersonaDto.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE, periodo.getRcesDependenciaDto().getDpnId());
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró Secretario Abogado.");
		}	

		return secretario;
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getCmfUsuario() {
		return cmfUsuario;
	}

	public void setCmfUsuario(Usuario cmfUsuario) {
		this.cmfUsuario = cmfUsuario;
	}

	public Integer getCmfActivarReporte() {
		return cmfActivarReporte;
	}

	public void setCmfActivarReporte(Integer cmfActivarReporte) {
		this.cmfActivarReporte = cmfActivarReporte;
	}

	public Integer getCmfCarreraId() {
		return cmfCarreraId;
	}

	public void setCmfCarreraId(Integer cmfCarreraId) {
		this.cmfCarreraId = cmfCarreraId;
	}

	public String getCmfIdentificacion() {
		return cmfIdentificacion;
	}

	public void setCmfIdentificacion(String cmfIdentificacion) {
		this.cmfIdentificacion = cmfIdentificacion;
	}

	public String getCmfPrimerApellido() {
		return cmfPrimerApellido;
	}

	public void setCmfPrimerApellido(String cmfPrimerApellido) {
		this.cmfPrimerApellido = cmfPrimerApellido;
	}

	public Integer getCmfTipoBusqueda() {
		return cmfTipoBusqueda;
	}

	public void setCmfTipoBusqueda(Integer cmfTipoBusqueda) {
		this.cmfTipoBusqueda = cmfTipoBusqueda;
	}

	public List<Carrera> getCmfListCarrera() {
		return cmfListCarrera;
	}

	public void setCmfListCarrera(List<Carrera> cmfListCarrera) {
		this.cmfListCarrera = cmfListCarrera;
	}

	public CarreraDto getCmfCarreraDto() {
		return cmfCarreraDto;
	}

	public void setCmfCarreraDto(CarreraDto cmfCarreraDto) {
		this.cmfCarreraDto = cmfCarreraDto;
	}

	public String getCmfNombreArchivo() {
		return cmfNombreArchivo;
	}

	public void setCmfNombreArchivo(String cmfNombreArchivo) {
		this.cmfNombreArchivo = cmfNombreArchivo;
	}

	public String getCmfNombreReporte() {
		return cmfNombreReporte;
	}

	public void setCmfNombreReporte(String cmfNombreReporte) {
		this.cmfNombreReporte = cmfNombreReporte;
	}

	public List<RecordEstudianteDto> getCmfListRecordEstudianteDto() {
		return cmfListRecordEstudianteDto;
	}

	public void setCmfListRecordEstudianteDto(List<RecordEstudianteDto> cmfListRecordEstudianteDto) {
		this.cmfListRecordEstudianteDto = cmfListRecordEstudianteDto;
	}

	public List<RecordEstudianteDto> getCmfListCarreraDto() {
		return cmfListCarreraDto;
	}

	public void setCmfListCarreraDto(List<RecordEstudianteDto> cmfListCarreraDto) {
		this.cmfListCarreraDto = cmfListCarreraDto;
	}

	public List<RecordEstudianteDto> getCmfListPeriodoAcademicoDto() {
		return cmfListPeriodoAcademicoDto;
	}

	public void setCmfListPeriodoAcademicoDto(List<RecordEstudianteDto> cmfListPeriodoAcademicoDto) {
		this.cmfListPeriodoAcademicoDto = cmfListPeriodoAcademicoDto;
	}

	public List<EstudianteJdbcDto> getCmfListEstudianteJdbcDto() {
		return cmfListEstudianteJdbcDto;
	}

	public void setCmfListEstudianteJdbcDto(List<EstudianteJdbcDto> cmfListEstudianteJdbcDto) {
		this.cmfListEstudianteJdbcDto = cmfListEstudianteJdbcDto;
	}

	public EstudianteJdbcDto getCmfEstudianteJdbcDto() {
		return cmfEstudianteJdbcDto;
	}

	public void setCmfEstudianteJdbcDto(EstudianteJdbcDto cmfEstudianteJdbcDto) {
		this.cmfEstudianteJdbcDto = cmfEstudianteJdbcDto;
	}

	


}
