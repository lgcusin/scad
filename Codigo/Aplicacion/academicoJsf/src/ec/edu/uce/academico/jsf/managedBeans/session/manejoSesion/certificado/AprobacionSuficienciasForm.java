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
   
 ARCHIVO:     AprobacionSuficienciasForm.java	  
 DESCRIPCION: Bean de sesion que maneja la sesion para la aprobacion de suficiencias. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20AGO2018			 Freddy Guzmán                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.certificado;

import java.io.Serializable;
import java.math.BigDecimal;
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
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaIdiomasServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCertificadoSuficienciasForm;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSuficienciaInformaticaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) AprobacionSuficienciasForm. Bean de sesion que maneja la
 * sesion para los certificados de aprobacion de suficiencias.
 * 
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "aprobacionSuficienciasForm")
@SessionScoped
public class AprobacionSuficienciasForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;
	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	private Usuario asfUsuario;
	private String asfPrimerApellido;
	private String asfIdentificacion;
	private Integer asfTipoUsuario;
	private Integer asfTipoBusqueda;
	private Integer asfAcceso; // boton reporte
	private BigDecimal asfPromedioGeneral;
	private Persona asfPersona;
	private CarreraDto asfCarreraDto;
	private List<Persona> asfListPersona;
	private List<CarreraDto> asfListSecretariaCarreraDto;
	private List<RecordEstudianteDto> asfListRecordEstudianteDto;
	private int asfActivarReporte;
	private String asfNombreReporte;
	private String asfNombreArchivo;
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB private MatriculaServicioJdbc servJdbcErafMatricula;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private PersonaServicio servPersona;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB private CarreraServicio servCarrera;
	@EJB private UsuarioRolServicio servUsuarioRolServicio;
	@EJB private MatriculaServicioJdbc servMatriculaServicioJdbc;
	@EJB private SuficienciaIdiomasServicioJdbc servJdbcSuficienciaIdiomas;

	// ****************************************************************/
	// ******************* METODOS DE NAVEGACION **********************/
	// ****************************************************************/
	
	/**
	 * Metodo que permite retornar a la pagina principal.
	 * @return pagina de inicio.
	 */
	public String irInicio() {
		limpiarFormNotas();
		asfIdentificacion = null;
		asfPrimerApellido = null;
		asfListPersona = null;
		return "irInicio";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiante(Usuario usuario) {
		
		asfUsuario = usuario;
		asfActivarReporte = 0;
		asfPrimerApellido = new String();
		asfIdentificacion = new String();

		List<UsuarioRol> usro = null;
		
		try {
			usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

		if (usro != null && usro.size() > 0) {
			for (UsuarioRol item : usro) {
				if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()) {
					asfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE;
					cargarCarrerasAsignadasASecretaria(item);
					return "irListarEstudianteSuficiencia";
				} 
			}
		}
		
		return null;
	}
	
	public String irNotasEstudiante(Persona persona){
		asfPersona = persona;
		String retorno = null;

		if (asfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
			List<RecordEstudianteDto> record = buscarRecordAcademicoCulturaFisica();
			if (record != null && record.size() > 0) {
				FacesUtil.limpiarMensaje();
				record.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracId));
				asfListRecordEstudianteDto = record;
				retorno = "irNotasEstudianteSuficienciaCulturaFisica";
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Es estudiante con identificación " + persona.getPrsIdentificacion() + " aún no dispone de un récord académico.");
			}
		}else if (asfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
			List<RecordEstudianteDto> record = buscarRecordAcademicoIdiomasSIIU();
			if (record != null && record.size() > 0) {
				FacesUtil.limpiarMensaje();
				record.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracId));
				asfListRecordEstudianteDto = record;
				retorno = "irNotasEstudianteSuficienciaIdiomas";
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Es estudiante con identificación " + persona.getPrsIdentificacion() + " aún no dispone de un récord académico.");
			}
		}else if (asfTipoUsuario ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
			List<RecordEstudianteDto> record = buscarRecordAcademicoInformatica();
			if (record != null && record.size() > 0) {
				FacesUtil.limpiarMensaje();
				record.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracId));
				asfListRecordEstudianteDto = record;
				retorno = "irNotasEstudianteSuficienciaInformatica";
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Es estudiante con identificación " + persona.getPrsIdentificacion() + " aún no dispone de un récord académico.");
			}
		}
		
		return retorno;

	}
	
	public String irEstudiantes(){
		limpiarFormEstudiantes();
		limpiarFormNotas();
		return "irEstudiantesSuficiencia";
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiarFormEstudiantes() {
		asfIdentificacion = new String();
		asfPrimerApellido = new String();
		asfListPersona = null;
	}
	
	private void limpiarFormNotas(){
		asfListRecordEstudianteDto = null;
		asfActivarReporte = 0;
		asfNombreReporte = null;
		asfNombreArchivo = null;
	}
	
	// ****************************************************************/
	// *********************** METODOS GENERALES **********************/
	// ****************************************************************/
	
	
	public void generarReporteCertificado() {
		asfActivarReporte = 0;
		asfNombreArchivo  = new String();
		asfNombreReporte = new String();
		
		if (asfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
			asfNombreArchivo  = "ReporteAprobacionCulturaFisica";
			asfNombreReporte = "CERTIFICADO_SUFICIENCIA_CULTURA_FISICA";
			
			List<RecordEstudianteDto> aprobadas = new ArrayList<>();
			if (asfListRecordEstudianteDto != null && asfListRecordEstudianteDto.size() > 0 ) {
				for (RecordEstudianteDto it : asfListRecordEstudianteDto) {
					if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)) {
						aprobadas.add(it);
					}
				}
				
				if (!aprobadas.isEmpty()) {
					
					PersonaDto secretario =  cargarSecretarioAbogado();
					PersonaDto decano =  cargarDecano();
					ReporteCertificadoSuficienciasForm.generarReporteSuficienciaCulturaFisica(asfUsuario, asfPersona, asfCarreraDto, decano, secretario, aprobadas);
					
					if (aprobadas.size() > 1) {
						asfActivarReporte = 1;
					}else {
						for (RecordEstudianteDto item : aprobadas) {
							if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_APROBACION_VALUE)) {
								asfActivarReporte = 1;
								break;
							}
						}
					}
					
				}else {
					FacesUtil.mensajeInfo("No es posible generar el Certificado ya que el estudiante no cumple con los requisitos.");
				}
			
			}else {
				FacesUtil.mensajeInfo("No existen datos en lista para mostrar.");
			}
		}else if (asfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
			asfNombreArchivo  = "ReporteAprobacionIdiomas";
			asfNombreReporte = "CERTIFICADO_SUFICIENCIA_IDIOMAS";
			
			List<RecordEstudianteDto> aprobadas = new ArrayList<>();
			if (asfListRecordEstudianteDto != null && asfListRecordEstudianteDto.size() > 0 ) {
				for (RecordEstudianteDto it : asfListRecordEstudianteDto) {
					if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) ||
							it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) {
						aprobadas.add(it);
					}
				}
				
				List<RecordEstudianteDto> aprobacion = new ArrayList<>();
				try {
					aprobacion = servJdbcSuficienciaIdiomas.buscarAprobacionIdiomasSIIU(asfPersona.getPrsIdentificacion());
				} catch (RecordEstudianteNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (RecordEstudianteException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
				if (!aprobadas.isEmpty() && !aprobacion.isEmpty()) {
//					PersonaDto decano =  cargarDirectorCarrera();
					PersonaDto decano =  new PersonaDto();
					decano.setPrsPrimerApellido("SANGUÑA");
					decano.setPrsSegundoApellido("LOACHAMIN");
					decano.setPrsNombres("MSc.EDISON SANTIAGO");
					decano.setPrsSexo(0);
					
					CarreraDto carrera = new CarreraDto();
					carrera.setCrrId(aprobacion.get(aprobacion.size()-1).getRcesCarreraDto().getCrrId());
					carrera.setCrrDescripcion(aprobacion.get(aprobacion.size()-1).getRcesCarreraDto().getCrrDescripcion());
					
					ReporteCertificadoSuficienciasForm.generarReporteSuficienciaIdiomas(asfUsuario, asfPersona, carrera, decano, null, aprobadas);
					asfActivarReporte = 1;	
				}else {
					FacesUtil.mensajeInfo("No es posible generar el Certificado ya que el estudiante no cumple con los requisitos.");
				}
			
			}else {
				FacesUtil.mensajeInfo("No existen datos en lista para mostrar.");
			}
			
		}else if (asfTipoUsuario ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
			asfNombreArchivo  = "ReporteAprobacionInformatica";
			asfNombreReporte = "CERTIFICADO_SUFICIENCIA_INFORMATICA";
			
			if (asfListRecordEstudianteDto != null && asfListRecordEstudianteDto.size() > 0 ) {
					
					RecordEstudianteDto aprobacion = null;
					for (RecordEstudianteDto item : asfListRecordEstudianteDto) {
						if (item.getRcesMateriaDto().getMtrEstadoLabel().equals("APROBADO")) {
							aprobacion = item;	
							break;
						}
					}
					
					if (aprobacion != null) {
						try {
							aprobacion.getRcesPeriodoAcademicoDto().setPracTipo(servPeriodoAcademico.buscarPorId(aprobacion.getRcesPeriodoAcademicoDto().getPracId()).getPracTipo());
						} catch (PeriodoAcademicoNoEncontradoException e) {
						} catch (PeriodoAcademicoException e) {
						}
						
						PersonaDto secretario =  cargarSecretarioAbogado();
						ReporteSuficienciaInformaticaForm.generarCertificadoAprobacion(asfUsuario, asfPersona, secretario, aprobacion);
						asfActivarReporte = 1;	
					}else {
						FacesUtil.mensajeInfo("No es posible generar el Certificado ya que el estudiante no cumple con los requisitos.");
					}
					
					
			}else {
				FacesUtil.mensajeInfo("No es posible generar el Certificado ya que el estudiante no cumple con los requisitos.");
			}
		}
	}
	
	private void cargarCarrerasAsignadasASecretaria(UsuarioRol usro) {
		
		try {
			List<CarreraDto> carreras = servJdbcCarreraDto.buscarCarreras(usro.getUsroId());
			if (carreras != null && carreras.size() > 0) {
				asfListSecretariaCarreraDto = null;
				asfListSecretariaCarreraDto = carreras;
				
				for (CarreraDto it : carreras) {
					if (it.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						asfTipoUsuario = DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE;
						asfCarreraDto = it;
						break;
					}else if (it.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
						asfTipoUsuario = DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE;
						asfCarreraDto = it;
						break;
					}else if (it.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
						asfTipoUsuario =  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE;
						asfCarreraDto = it;
						break;
					}
				}
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
		}
		
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscarEstudiantesSIIU() {

		if (asfIdentificacion.length() > 0 || asfPrimerApellido.length() > 0) {

			List<Persona> personas = new ArrayList<>();
			List<String> carreras = new ArrayList<>();
			carreras.add(String.valueOf(GeneralesConstantes.APP_ID_BASE));

			if (asfTipoBusqueda.equals(GeneralesConstantes.APP_FIND_BY_IDENTIFICACION)) {
				List<EstudianteJdbcDto> estudiantes = cargarEstudiantesSIIU(asfIdentificacion, carreras, asfTipoBusqueda);
				if (estudiantes != null && estudiantes.size() > 0) {
					for (EstudianteJdbcDto it : estudiantes) {
						Persona p = new Persona();
						p.setPrsId(it.getPrsId());
						p.setPrsIdentificacion(it.getPrsIdentificacion());
						p.setPrsPrimerApellido(it.getPrsPrimerApellido());
						p.setPrsSegundoApellido(it.getPrsSegundoApellido());
						p.setPrsNombres(it.getPrsNombres());
						p.setPrsSexo(it.getPrsSexo());
						personas.add(p);
					}
				}else {
					asfListPersona = null;
					FacesUtil.mensajeInfo("No se encontró resultados con la identificación ingresada. Intente con búsqueda avanzada.");	
				}
			}else {
				List<EstudianteJdbcDto> estudiantes = cargarEstudiantesSIIU(asfPrimerApellido, carreras, asfTipoBusqueda);
				if (estudiantes != null && estudiantes.size() > 0) {
					for (EstudianteJdbcDto it : estudiantes) {
						Persona p = new Persona();
						p.setPrsId(it.getPrsId());
						p.setPrsIdentificacion(it.getPrsIdentificacion());
						p.setPrsPrimerApellido(it.getPrsPrimerApellido());
						p.setPrsSegundoApellido(it.getPrsSegundoApellido());
						p.setPrsNombres(it.getPrsNombres());
						p.setPrsSexo(it.getPrsSexo());
						personas.add(p);
					}
				}else {
					asfListPersona = null;
					FacesUtil.mensajeInfo("No se encontró resultados con el apellido ingresado. Intente con búsqueda avanzada.");
				}
			}

			if (personas.size() > 0) {
				List<Persona> aux = new ArrayList<>();
				Map<String, Persona> mapCarreras =  new HashMap<String, Persona>();

				for (Persona it : personas) {
					mapCarreras.put(it.getPrsIdentificacion(),it);
				}
				for (Entry<String, Persona> it : mapCarreras.entrySet()) {
					aux.add(it.getValue());
				}
				aux.sort(Comparator.comparing(Persona::getPrsPrimerApellido).thenComparing(Persona:: getPrsSegundoApellido).thenComparing(Persona:: getPrsNombres));
				asfListPersona = null;
				asfListPersona = aux;
			}else {
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados. Intente con búsqueda avanzada.");	
			}

		} else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar.");
		}

	}
	
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscarEstudiantesSAU() {

		if (asfIdentificacion.length() > 0 || asfPrimerApellido.length() > 0) {

			List<Persona> personas = new ArrayList<>();
			List<String> carreras = new ArrayList<>();
			carreras.add(String.valueOf(GeneralesConstantes.APP_ID_BASE));

			if (asfTipoBusqueda.equals(GeneralesConstantes.APP_FIND_BY_IDENTIFICACION)) {
				List<EstudianteJdbcDto> estudiantes = cargarEstudiantesSAU(asfIdentificacion, carreras, asfTipoBusqueda);
				if (estudiantes != null && estudiantes.size() > 0) {
					for (EstudianteJdbcDto it : estudiantes) {
						Persona p = new Persona();
						p.setPrsId(it.getPrsId());
						p.setPrsIdentificacion(it.getPrsIdentificacion());
						p.setPrsPrimerApellido(it.getPrsPrimerApellido());
						p.setPrsSegundoApellido(it.getPrsSegundoApellido());
						p.setPrsNombres(it.getPrsNombres());
						p.setPrsSexo(it.getPrsSexo());
						personas.add(p);
					}
				}else {
					asfListPersona = null;
					FacesUtil.mensajeInfo("No se encontró resultados con la identificación ingresada.");
				}
			}else {
				List<EstudianteJdbcDto> estudiantes = cargarEstudiantesSAU(asfPrimerApellido, carreras, asfTipoBusqueda);
				if (estudiantes != null && estudiantes.size() > 0) {
					for (EstudianteJdbcDto it : estudiantes) {
						Persona p = new Persona();
						p.setPrsId(it.getPrsId());
						p.setPrsIdentificacion(it.getPrsIdentificacion());
						p.setPrsPrimerApellido(it.getPrsPrimerApellido());
						p.setPrsSegundoApellido(it.getPrsSegundoApellido());
						p.setPrsNombres(it.getPrsNombres());
						p.setPrsSexo(it.getPrsSexo());
						personas.add(p);
					}
				}else {
					asfListPersona = null;
					FacesUtil.mensajeInfo("No se encontró resultados con el apellido ingresado.");
				}
			}

			if (personas.size() > 0) {
				List<Persona> aux = new ArrayList<>();
				Map<String, Persona> mapCarreras =  new HashMap<String, Persona>();

				for (Persona it : personas) {
					mapCarreras.put(it.getPrsIdentificacion(),it);
				}
				for (Entry<String, Persona> it : mapCarreras.entrySet()) {
					aux.add(it.getValue());
				}
				
				aux.sort(Comparator.comparing(Persona::getPrsPrimerApellido).thenComparing(Persona:: getPrsSegundoApellido).thenComparing(Persona:: getPrsNombres));
				asfListPersona = null;
				asfListPersona = aux;
			}else {
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");	
			}

		} else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar.");
		}

	}
	
	
	private List<EstudianteJdbcDto> cargarEstudiantesSIIU(String param, List<String> carreras, int tipoBusqueda){
		try {
			return servJdbcEstudianteDto.buscarEstudiantePorCarrerasSIIU(param, carreras, tipoBusqueda);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	
	private List<EstudianteJdbcDto> cargarEstudiantesSAU(String param, List<String> carreras, int tipoBusqueda){
		try {
			return servJdbcEstudianteDto.buscarEstudiantePorCarrerasSAU(param, carreras, tipoBusqueda);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Método que permite unir los records academicos del estudiante.
	 * @return record academico consolidado.
	 */
	private List<RecordEstudianteDto> buscarRecordAcademicoInformatica(){
		List<String> carreras = new ArrayList<>();
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE));
		
		try {
			return servMatriculaServicioJdbc.buscarRecordEstudianteSIIU(asfPersona.getPrsIdentificacion(), carreras, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	
	/**
	 * Método que permite unir los records academicos del estudiante.
	 * @return record academico consolidado.
	 */
	private List<RecordEstudianteDto> buscarRecordAcademicoIdiomasSIIU(){
		List<String> carreras = new ArrayList<>();
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE));
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE));
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE));
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE));
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE));
		
		try {
			return servMatriculaServicioJdbc.buscarRecordEstudianteSIIU(asfPersona.getPrsIdentificacion(), carreras, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	/**
	 * Método que permite unir los records academicos del estudiante.
	 * @return record academico consolidado.
	 */
	private List<RecordEstudianteDto> buscarRecordAcademicoCulturaFisica(){
		List<RecordEstudianteDto> record = new ArrayList<>();
		
		List<RecordEstudianteDto> recordSAU = cargarRecordAcademicoCulturaFisicaSAU();
		if (recordSAU != null && recordSAU.size() > 0) {
			
			for (RecordEstudianteDto it : recordSAU) {
				try {
					Carrera entidad = servCarrera.buscarCarreraXEspeCodigo(setearCarreraId(it.getRcesCarreraDto().getCrrId()));
					it.getRcesCarreraDto().setCrrId(entidad.getCrrId());
					it.getRcesCarreraDto().setCrrDescripcion(entidad.getCrrDescripcion());
					it.getRcesCarreraDto().setDpnId(entidad.getCrrDependencia().getDpnId());
				} catch (CarreraNoEncontradoException e) {
					it.getRcesCarreraDto().setCrrId(-1);
					it.getRcesCarreraDto().setCrrDescripcion("CARRERA NO REGISTRADA EN SIIU");
				} catch (CarreraException e) {
				}
			}
			
			record.addAll(recordSAU);
		}

		List<RecordEstudianteDto> recordSIIU = cargarRecordAcademicoCulturaFisicaSIIU();
		if (recordSIIU != null && recordSIIU.size() > 0) {
			record.addAll(recordSIIU);
		}

		if (record.size() > 0) {
			return record;
		}else {
			return null;
		}
	}
	
	private int setearCarreraId(int carreraId) {

		if (carreraId == SAUConstantes.INGLES_APROBACION_NIVELES 
				|| carreraId == SAUConstantes.INGLES_APROBACION_CERTIFICADO
				|| carreraId == SAUConstantes.INGLES_APROBACION_IDIOMA_ADICIONAL
				|| carreraId == SAUConstantes.INGLES_APROBACION_SUFICIENCIA 
				|| carreraId == SAUConstantes.INGLES_APROBACION_A1
				|| carreraId == SAUConstantes.INGLES_APROBACION_A2
				|| carreraId ==  SAUConstantes.INGLES_APROBACION_INTENSIVO1
				|| carreraId ==  SAUConstantes.INGLES_APROBACION_INTENSIVO2
				|| carreraId == SAUConstantes.INGLES_APROBACION_ONLINE){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}else if (carreraId == SAUConstantes.FRANCES_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.FRANCES_APROBACION_A1
				|| carreraId == SAUConstantes.FRANCES_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_FRANCES;
		}else if (carreraId == SAUConstantes.ITALIANO_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.ITALIANO_APROBACION_A1
				|| carreraId == SAUConstantes.ITALIANO_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_ITALIANO;
		}else if (carreraId == SAUConstantes.COREANO_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.COREANO_APROBACION_A1
				|| carreraId == SAUConstantes.COREANO_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_COREANO;
		}else if (carreraId == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA1
				|| carreraId == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_KICHWA;
		}else if (carreraId == SAUConstantes.AFR_DEFENSA_PERSONAL
				|| carreraId == SAUConstantes.AFR_ACOND_FISICO
				|| carreraId == SAUConstantes.AFR_FUTBOL
				|| carreraId == SAUConstantes.AFR_AEREOBICOS
				|| carreraId == SAUConstantes.AFR_GIMNASIA_PESAS
				|| carreraId == SAUConstantes.AFR_BASQUETBALL
				|| carreraId == SAUConstantes.AFR_VOLEYBALL
				|| carreraId == SAUConstantes.AFR_TENIS
				|| carreraId == SAUConstantes.AFR_BAILE
				|| carreraId == SAUConstantes.AFR_BAILE_TROPICAL
				|| carreraId == SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_CULTURA_FISICA;
			// no se sabe en que idoma cayeron
		}else if (carreraId == SAUConstantes.CHINO_MANDARIN_APROBACION
				|| carreraId == SAUConstantes.APROBADO_A1A2
				|| carreraId == SAUConstantes.APROBADO_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}

		return carreraId;
	}
	
	
	private List<RecordEstudianteDto> cargarRecordAcademicoCulturaFisicaSAU (){ 

		try {
			return servMatriculaServicioJdbc.buscarRecordAcademicoSuficienciaCulturaFisicaSAU(asfPersona.getPrsIdentificacion());
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}

	private List<RecordEstudianteDto> cargarRecordAcademicoCulturaFisicaSIIU (){ 
		List<String> carreras = new ArrayList<>();
		carreras.add(String.valueOf(CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE));
		
		try {
			return servMatriculaServicioJdbc.buscarRecordEstudianteSIIU(asfPersona.getPrsIdentificacion(), carreras, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	public void busquedaPorIdentificacion(){
		
		if (asfIdentificacion.length() > 0) {
			asfPrimerApellido = new String();
			asfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (asfPrimerApellido.length() > 0) {
			asfIdentificacion = new String();
			asfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	
	
	/**
	 * Mètodo que permite cargar datos del secretario abogado de la facultada
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarSecretarioAbogado(){
		
		PersonaDto secretario  = null;
		
		if (asfCarreraDto != null) {
			try {
				secretario = servJdbcPersonaDto.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE, asfCarreraDto.getDpnId());
			} catch (PersonaDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDtoNoEncontradoException e) {
				FacesUtil.mensajeInfo("No se encontró Secretario Abogado.");
			}	

		}
		
		return secretario;
	}
	
	
	/**
	 * Mètodo que permite cargar datos del secretario abogado de la facultada
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarDecano(){

		PersonaDto decano  = null;

		if (asfCarreraDto != null) {

			try {
				decano = servJdbcPersonaDto.buscarPersonaXRolIdXFclId(RolConstantes.ROL_DECANO_VALUE, asfCarreraDto.getDpnId());
			} catch (PersonaDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDtoNoEncontradoException e) {
				FacesUtil.mensajeInfo("No se encontró Decano en la Facultad " + asfCarreraDto.getDpnDescripcion());
			}	

		}

		return decano;
	}
	
	/**
	 * Mètodo que permite cargar datos del secretario abogado de la facultada
	 * @return entidad PersonaDto.
	 */
//	private PersonaDto cargarDirectorCarrera(){
//		PersonaDto director  = null;
//
//		try {
//			director = servJdbcPersonaDto.buscarDirectorCarreraxidCarrera(CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE);
//		} catch (PersonaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaDtoNoEncontradoException e) {
//			FacesUtil.mensajeInfo("No se encontró Decano en la Facultad " + asfCarreraDto.getDpnDescripcion());
//		}	
//
//		return director;
//	}
	
	/**
	 * Método que da formato de dos decimales a un BigDecimal.
	 * @param param - bigdecimal
	 * @param simbolo - adiciona simbolo.
	 * @return valor en formato string.
	 */
	public static String getBigDecimal(BigDecimal param, int simbolo){
		
		if (param != null && param.intValue() != 0) {
			if (simbolo == 0) {
				return String.format("%.2f", param) + " %";	
			}else if(param.intValue() == -1 ){
				return "";
			}else if(simbolo == 1 ){
				return String.format("%.2f", param);
			}else if (simbolo == 2) {
				return String.valueOf(param.intValue());
			}
		}
		
		return "";
	}

	// ****************************************************************/
	// *********************    ENCAPSULACION    **********************/
	// ****************************************************************/
	
	public Usuario getAsfUsuario() {
		return asfUsuario;
	}

	public void setAsfUsuario(Usuario asfUsuario) {
		this.asfUsuario = asfUsuario;
	}

	public String getAsfPrimerApellido() {
		return asfPrimerApellido;
	}

	public void setAsfPrimerApellido(String asfPrimerApellido) {
		this.asfPrimerApellido = asfPrimerApellido;
	}

	public String getAsfIdentificacion() {
		return asfIdentificacion;
	}

	public void setAsfIdentificacion(String asfIdentificacion) {
		this.asfIdentificacion = asfIdentificacion;
	}


	public Integer getAsfTipoUsuario() {
		return asfTipoUsuario;
	}

	public void setAsfTipoUsuario(Integer asfTipoUsuario) {
		this.asfTipoUsuario = asfTipoUsuario;
	}

	public Integer getAsfTipoBusqueda() {
		return asfTipoBusqueda;
	}

	public void setAsfTipoBusqueda(Integer asfTipoBusqueda) {
		this.asfTipoBusqueda = asfTipoBusqueda;
	}

	public Integer getAsfAcceso() {
		return asfAcceso;
	}

	public void setAsfAcceso(Integer asfAcceso) {
		this.asfAcceso = asfAcceso;
	}

	public BigDecimal getAsfPromedioGeneral() {
		return asfPromedioGeneral;
	}

	public void setAsfPromedioGeneral(BigDecimal asfPromedioGeneral) {
		this.asfPromedioGeneral = asfPromedioGeneral;
	}

	public Persona getAsfPersona() {
		return asfPersona;
	}

	public void setAsfPersona(Persona asfPersona) {
		this.asfPersona = asfPersona;
	}

	public List<Persona> getAsfListPersona() {
		return asfListPersona;
	}

	public void setAsfListPersona(List<Persona> asfListPersona) {
		this.asfListPersona = asfListPersona;
	}

	public List<CarreraDto> getAsfListSecretariaCarreraDto() {
		return asfListSecretariaCarreraDto;
	}

	public void setAsfListSecretariaCarreraDto(List<CarreraDto> asfListSecretariaCarreraDto) {
		this.asfListSecretariaCarreraDto = asfListSecretariaCarreraDto;
	}

	public List<RecordEstudianteDto> getAsfListRecordEstudianteDto() {
		return asfListRecordEstudianteDto;
	}

	public void setAsfListRecordEstudianteDto(List<RecordEstudianteDto> asfListRecordEstudianteDto) {
		this.asfListRecordEstudianteDto = asfListRecordEstudianteDto;
	}

	public int getAsfActivarReporte() {
		return asfActivarReporte;
	}

	public void setAsfActivarReporte(int asfActivarReporte) {
		this.asfActivarReporte = asfActivarReporte;
	}

	public String getAsfNombreReporte() {
		return asfNombreReporte;
	}

	public void setAsfNombreReporte(String asfNombreReporte) {
		this.asfNombreReporte = asfNombreReporte;
	}

	public String getAsfNombreArchivo() {
		return asfNombreArchivo;
	}

	public void setAsfNombreArchivo(String asfNombreArchivo) {
		this.asfNombreArchivo = asfNombreArchivo;
	}

	public CarreraServicio getServCarrera() {
		return servCarrera;
	}

	public void setServCarrera(CarreraServicio servCarrera) {
		this.servCarrera = servCarrera;
	}

	public CarreraDto getAsfCarreraDto() {
		return asfCarreraDto;
	}

	public void setAsfCarreraDto(CarreraDto asfCarreraDto) {
		this.asfCarreraDto = asfCarreraDto;
	}

	
}
