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
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCertificadoNotasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) CertificadoNotasForm.
 * Managed Bean que administra la visualización de los certificados de notas PREGRADO y SUFICIENCIAS.
 * @author fgguzman.
 * @version 1.0
 */


@ManagedBean(name="certificadoNotasForm")
@SessionScoped
public class CertificadoNotasForm extends HistorialAcademicoForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//************************ VARIABLES *****************************/
	//****************************************************************/
	private Usuario cnfUsuario;
	private EstudianteJdbcDto cnfEstudianteJdbcDto;
	private CarreraDto cnfCarreraDto;

	private Integer cnfTipoBusqueda;
	private Integer cnfActivarReporte;
	private Integer cnfCarreraId;
	private String cnfIdentificacion;
	private String cnfPrimerApellido;
	private String cnfNombreArchivo ;
	private String cnfNombreReporte;
	
	private List<Carrera> cnfListCarrera;
	private List<RecordEstudianteDto> cnfListCarreraDto;
	private List<RecordEstudianteDto> cnfListPeriodoAcademicoDto;
	private List<RecordEstudianteDto> cnfListRecordEstudianteDto;
	private List<EstudianteJdbcDto> cnfListEstudianteJdbcDto;
	
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

			cnfUsuario = usuario;
			cnfListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXUsuarioXRoles(cnfUsuario.getUsrId(), new Integer[]{RolConstantes.ROL_SECREABOGADO_VALUE, RolConstantes.ROL_SECRECARRERA_VALUE, RolConstantes.ROL_SECRESUFICIENCIAS_VALUE});
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				cnfListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
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
		
		return "irListarEstudiantesCertificadoNotas";
	}
	
	public String irVerCarreras(EstudianteJdbcDto estudiante){
		cnfEstudianteJdbcDto = estudiante;
		
		cnfListRecordEstudianteDto = cargarHistorialAcademicoSAIUHomologado(cnfEstudianteJdbcDto.getPrsIdentificacion());
		if (cnfListRecordEstudianteDto != null && cnfListRecordEstudianteDto.size() >0) {
			cnfListCarreraDto =  permisosVisualizacionRecord(cargarCarrerasPorMatriculas());
			return "irVerCarrerasCertificadoNotas";
		}else {
			FacesUtil.mensajeInfo("El estudiante con identificación " + estudiante.getPrsIdentificacion() + " no tiene matriculas para mostrar.");
			return null;
		}
		
	}
	
	public String irVerMatriculas(RecordEstudianteDto carrera){
		cnfCarreraDto = carrera.getRcesCarreraDto();
		List<RecordEstudianteDto> auxCmfListPeriodoAcademicoDto = cargarMatriculasPorPeriodo(cnfListRecordEstudianteDto, cnfCarreraDto);
		if (auxCmfListPeriodoAcademicoDto != null && auxCmfListPeriodoAcademicoDto.size() >0 ) {
			auxCmfListPeriodoAcademicoDto.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracDescripcion));
			cnfListPeriodoAcademicoDto = auxCmfListPeriodoAcademicoDto;
			return "irVerMatriculasCertificadoNotas";	
		}else {
			return null;
		}
		
	}
	
	public String irInicio(){
		vaciarParametrosFormEstudiantes();
		vaciarParametrosFormCarreras();
		vaciarParametrosFormPeriodos();
		cnfListRecordEstudianteDto = null;
		return "irInicio";
	}
	
	public String irEstudiantes(){
		vaciarParametrosFormEstudiantes();
		return "irEstudiantes";
	}
	
	public String irCarreras(){
		cnfActivarReporte = 0;	
		return "irCarreras";
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	private void iniciarParametrosFormEstudiantes(){
		cnfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		cnfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		cnfIdentificacion = new String();
		cnfPrimerApellido = new String();
		cnfListEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormEstudiantes(){
		cnfActivarReporte = null;
		cnfTipoBusqueda = null;
		cnfIdentificacion = null;
		cnfPrimerApellido = null;
		cnfListEstudianteJdbcDto = null;
	}
	
	public void limpiarParametrosFormEstudiantes(){
		cnfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		cnfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		cnfIdentificacion = new String();
		cnfPrimerApellido = new String();
		cnfListEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormCarreras(){
		cnfListCarreraDto = null;
		cnfEstudianteJdbcDto = null;
	}
	
	private void vaciarParametrosFormPeriodos(){
		cnfListPeriodoAcademicoDto = null;
		cnfEstudianteJdbcDto = null;
		cnfActivarReporte = null;
	}
	
	/**
	 * Método que permite habilitar el acceso de visualizacion de las carreras que puede emitir los certificados.
	 */
	private List<RecordEstudianteDto> permisosVisualizacionRecord(List<RecordEstudianteDto> carreras) {
		for (Carrera secretaria : cnfListCarrera) {
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
		
		if (cnfTipoBusqueda.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			String param = null;
			if (cnfTipoBusqueda == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = cnfIdentificacion;
			}else {
				param = cnfPrimerApellido;
			}
			
			List<EstudianteJdbcDto>  estudiantes = cargarEstudianteSAIU(param, cnfListCarrera, cnfTipoBusqueda);
			if (estudiantes != null && estudiantes.size() > 0) {
				cnfListEstudianteJdbcDto = estudiantes;
			}else {
				cnfListEstudianteJdbcDto = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar con la búsqueda.");
		}

	}
	
	public void busquedaPorIdentificacion(){
		
		if (cnfIdentificacion.length() > 0) {
			cnfPrimerApellido = new String();
			cnfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (cnfPrimerApellido.length() > 0) {
			cnfIdentificacion = new String();
			cnfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	

	private List<RecordEstudianteDto> cargarCarrerasPorMatriculas() {
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		Map<Integer, RecordEstudianteDto> mapCarreras =  new HashMap<Integer, RecordEstudianteDto>();

		for (RecordEstudianteDto it : cnfListRecordEstudianteDto) {
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
		cnfNombreArchivo  = "certificadoNotas";
		cnfNombreReporte = "CERTIFICADOS_NOTAS";

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
		if (asignaturas.size() > 0) {
			ReporteCertificadoNotasForm.generarReporteCertificadoNotas(cnfUsuario, cnfEstudianteJdbcDto, matricula, carrera, secretario, asignaturas);
			cnfActivarReporte = 1;	
		}else {
			cnfActivarReporte = 0;
			FacesUtil.mensajeInfo("No se encontró registros para generar el Certificado.");			
		}
		
		

	}
	
	private List<RecordEstudianteDto> cargarAsignaturasPorPeriodo(RecordEstudianteDto periodo, CarreraDto carrera) {
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		for (RecordEstudianteDto item : cnfListRecordEstudianteDto) {
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
	public Usuario getCnfUsuario() {
		return cnfUsuario;
	}

	public void setCnfUsuario(Usuario cnfUsuario) {
		this.cnfUsuario = cnfUsuario;
	}

	public EstudianteJdbcDto getCnfEstudianteJdbcDto() {
		return cnfEstudianteJdbcDto;
	}

	public void setCnfEstudianteJdbcDto(EstudianteJdbcDto cnfEstudianteJdbcDto) {
		this.cnfEstudianteJdbcDto = cnfEstudianteJdbcDto;
	}

	public CarreraDto getCnfCarreraDto() {
		return cnfCarreraDto;
	}

	public void setCnfCarreraDto(CarreraDto cnfCarreraDto) {
		this.cnfCarreraDto = cnfCarreraDto;
	}

	public Integer getCnfTipoBusqueda() {
		return cnfTipoBusqueda;
	}

	public void setCnfTipoBusqueda(Integer cnfTipoBusqueda) {
		this.cnfTipoBusqueda = cnfTipoBusqueda;
	}

	public Integer getCnfActivarReporte() {
		return cnfActivarReporte;
	}

	public void setCnfActivarReporte(Integer cnfActivarReporte) {
		this.cnfActivarReporte = cnfActivarReporte;
	}

	public Integer getCnfCarreraId() {
		return cnfCarreraId;
	}

	public void setCnfCarreraId(Integer cnfCarreraId) {
		this.cnfCarreraId = cnfCarreraId;
	}

	public String getCnfIdentificacion() {
		return cnfIdentificacion;
	}

	public void setCnfIdentificacion(String cnfIdentificacion) {
		this.cnfIdentificacion = cnfIdentificacion;
	}

	public String getCnfPrimerApellido() {
		return cnfPrimerApellido;
	}

	public void setCnfPrimerApellido(String cnfPrimerApellido) {
		this.cnfPrimerApellido = cnfPrimerApellido;
	}

	public String getCnfNombreArchivo() {
		return cnfNombreArchivo;
	}

	public void setCnfNombreArchivo(String cnfNombreArchivo) {
		this.cnfNombreArchivo = cnfNombreArchivo;
	}

	public String getCnfNombreReporte() {
		return cnfNombreReporte;
	}

	public void setCnfNombreReporte(String cnfNombreReporte) {
		this.cnfNombreReporte = cnfNombreReporte;
	}

	public List<Carrera> getCnfListCarrera() {
		return cnfListCarrera;
	}

	public void setCnfListCarrera(List<Carrera> cnfListCarrera) {
		this.cnfListCarrera = cnfListCarrera;
	}

	public List<RecordEstudianteDto> getCnfListCarreraDto() {
		return cnfListCarreraDto;
	}

	public void setCnfListCarreraDto(List<RecordEstudianteDto> cnfListCarreraDto) {
		this.cnfListCarreraDto = cnfListCarreraDto;
	}

	public List<RecordEstudianteDto> getCnfListPeriodoAcademicoDto() {
		return cnfListPeriodoAcademicoDto;
	}

	public void setCnfListPeriodoAcademicoDto(List<RecordEstudianteDto> cnfListPeriodoAcademicoDto) {
		this.cnfListPeriodoAcademicoDto = cnfListPeriodoAcademicoDto;
	}

	public List<RecordEstudianteDto> getCnfListRecordEstudianteDto() {
		return cnfListRecordEstudianteDto;
	}

	public void setCnfListRecordEstudianteDto(List<RecordEstudianteDto> cnfListRecordEstudianteDto) {
		this.cnfListRecordEstudianteDto = cnfListRecordEstudianteDto;
	}

	public List<EstudianteJdbcDto> getCnfListEstudianteJdbcDto() {
		return cnfListEstudianteJdbcDto;
	}

	public void setCnfListEstudianteJdbcDto(List<EstudianteJdbcDto> cnfListEstudianteJdbcDto) {
		this.cnfListEstudianteJdbcDto = cnfListEstudianteJdbcDto;
	}

	public CarreraDtoServicioJdbc getServJdbcCarreraDto() {
		return servJdbcCarreraDto;
	}

	public void setServJdbcCarreraDto(CarreraDtoServicioJdbc servJdbcCarreraDto) {
		this.servJdbcCarreraDto = servJdbcCarreraDto;
	}

	public UsuarioRolServicio getServUsuarioRol() {
		return servUsuarioRol;
	}

	public void setServUsuarioRol(UsuarioRolServicio servUsuarioRol) {
		this.servUsuarioRol = servUsuarioRol;
	}

	public PersonaDtoServicioJdbc getServJdbcPersonaDto() {
		return servJdbcPersonaDto;
	}

	public void setServJdbcPersonaDto(PersonaDtoServicioJdbc servJdbcPersonaDto) {
		this.servJdbcPersonaDto = servJdbcPersonaDto;
	}

	public RolFlujoCarreraServicio getServRolFlujoCarrera() {
		return servRolFlujoCarrera;
	}

	public void setServRolFlujoCarrera(RolFlujoCarreraServicio servRolFlujoCarrera) {
		this.servRolFlujoCarrera = servRolFlujoCarrera;
	}

	public CarreraServicio getServCarrera() {
		return servCarrera;
	}

	public void setServCarrera(CarreraServicio servCarrera) {
		this.servCarrera = servCarrera;
	}

	public EstudianteDtoServicioJdbc getServJdbcEstudianteDto() {
		return servJdbcEstudianteDto;
	}

	public void setServJdbcEstudianteDto(EstudianteDtoServicioJdbc servJdbcEstudianteDto) {
		this.servJdbcEstudianteDto = servJdbcEstudianteDto;
	}

	public MatriculaServicioJdbc getServJdbcMatricula() {
		return servJdbcMatricula;
	}

	public void setServJdbcMatricula(MatriculaServicioJdbc servJdbcMatricula) {
		this.servJdbcMatricula = servJdbcMatricula;
	}
	

}
