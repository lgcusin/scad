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
   
 ARCHIVO:     Agregar_Tipo_Nivel_Apertura.java	  
 DESCRIPCION: Bean que genera los reportes para del Decano de una facultad. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-05-2018			 Daniel Ortiz                      		Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReportesConsultorServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) FichaMatriculaForm. 
 * Bean que muestra los reportes al Decano. 
 * @author dortiz
 * @version 1.0
 */
@ManagedBean(name = "reportesConsultorForm")
@SessionScoped
public class ReportesConsultorForm implements Serializable {

	private static final long serialVersionUID = 7472841396484757157L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private Usuario rcfUsuario;
	private PersonaDto rcfPersonaDto;
	private List<UsuarioRol> rcfListUsuarioRol;
	private PersonaDto rcfEstudianteBuscar;
	private PeriodoAcademico rcfPeriodoAcademicoBuscar;
	private Dependencia rcfDependenciaBuscar;
	private Carrera rcfCarreraBuscar;
	private List<PersonaDto> rcfListMatriculados;
	private List<SolicitudTerceraMatriculaDto> rcfListEstudiantesTercerasMatriculas;
	private List<RecordEstudianteDto> rcfListEstudiantesNotas;
	private List<PeriodoAcademico> rcfListPeriodoAcademico;
	private List<Dependencia> rcfListFacultades;
	private List<Carrera> rcfListCarreras;
	private Timestamp rcffecha_inicio;
	private Timestamp rcffecha_fin;
	private int rcftipo_solicitud;
	private String rcfActivacion;
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
	UsuarioRolServicio rcfUsuarioRolServicio;
	@EJB
	DependenciaServicio rcfDependenciaServicio;
	@EJB
	CarreraServicio rcfCarreraServicio;
	@EJB
	PeriodoAcademicoServicio rcfPeriodoAcademicoServicio;
	@EJB
	ReportesConsultorServicioJdbc rcfReportesConsultorServicioJdbc;
	@EJB
	UsuarioServicio rcfUsuarioServicio;
	@EJB
	PersonaServicio rcfPersonaServicio;
	//****************************************************************/
	//*********** METODOS GENERALES DE LA CLASE **********************/
	//****************************************************************/
	public String irReportesConsultor(Usuario usr){
		rcfUsuario=usr;
		try {
			
			rcfListUsuarioRol = rcfUsuarioRolServicio.buscarXUsuario(usr.getUsrId());
			rcfListFacultades = rcfDependenciaServicio.listarTodos();
			
			for(UsuarioRol item:rcfListUsuarioRol){
				if(item.getUsroRol().getRolId()==RolConstantes.ROL_DECANO_VALUE.intValue()){
					rcfPersonaDto.setRolId(RolConstantes.ROL_DECANO_VALUE);
					rcfPersonaDto.setRolDescripcion(RolConstantes.ROL_DECANO);
				}
			}
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} 
			
		return "";
	}

//	public String irReportesSolicitudesEstudiantes(Usuario usr){
//		rcfUsuario=usr;
//		rcfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
//		rcfEstudianteBuscar = new PersonaDto();
//		
//		return "irReporteSolicitudes";
//	}

	public String irReporteMatriculados(Usuario usr){
		rcfUsuario=usr;
		rcfActivacion = "false";
		rcfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		rcfDependenciaBuscar = new Dependencia(GeneralesConstantes.APP_ID_BASE);
		rcfCarreraBuscar = new Carrera(GeneralesConstantes.APP_ID_BASE);
		rcfEstudianteBuscar = new PersonaDto();
		
		
		return "irReporteMatriculados";
	}
	public String irReporteTercerasMatriculas(Usuario usr){
		rcfUsuario=usr;
		rcfActivacion = "false";
		rcfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		rcfDependenciaBuscar = new Dependencia(GeneralesConstantes.APP_ID_BASE);
		rcfCarreraBuscar = new Carrera(GeneralesConstantes.APP_ID_BASE);
		rcfEstudianteBuscar = new PersonaDto();
		buscarDependenciasPregrado();
		buscarPeriodoAcademicoPregrado();
		
		return "irReporteTercerasMatriculasGeneral";
	}
	public String irReporteEstudiantesNotas(Usuario usr){
		rcfUsuario=usr;
		rcfActivacion = "false";
		rcfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		rcfDependenciaBuscar = new Dependencia(GeneralesConstantes.APP_ID_BASE);
		rcfCarreraBuscar = new Carrera(GeneralesConstantes.APP_ID_BASE);
		rcfEstudianteBuscar = new PersonaDto();
		buscarDependenciasPregrado();
		buscarPeriodoAcademicoPregrado();
		
		return "irReporteEstudiantesNotas";
	}
	//Realiza la busqueda de las dependencias por tipo de grado 
	public void buscarDependeciasXTipoGrado(int pracTipo)
	{
		if(pracTipo==PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE){
			
		}else{
			buscarDependenciasPregrado();
			buscarPeriodoAcademicoPregrado();
		}
	}
	
	//Realiza la busqueda de las dependencias pregrado
	public void buscarDependenciasPregrado(){
		int jerarquia_facultades=3;
	try {
		
		rcfListFacultades = rcfDependenciaServicio.listarFacultadesActivas(jerarquia_facultades);
		
		} catch (DependenciaNoEncontradoException e) {
		FacesUtil.mensajeError(e.getMessage());
		e.printStackTrace();
		}
	
	}
	
	//Realiza la busqueda de las carreras de la dependencia seleccionada
	public void buscarCarrerasXDependecia(int dpnId){
		
		try {
			
			rcfListCarreras = rcfCarreraServicio.listarCarrerasXFacultad(dpnId);
			
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	//Realiza la busqueda de los periodos academicos pregrado
	public void buscarPeriodoAcademicoPregrado(){
		rcfListPeriodoAcademico = rcfPeriodoAcademicoServicio.listarTodos();
		rcfListPeriodoAcademico.remove(6);
		
	}
	
	//Realiza la busqueda de los Estudiantes Matriculados por la Facultad,Carrera y Periodo Academico Seleccionado
	public void buscarMatriculados(){
		rcfListMatriculados = new ArrayList<PersonaDto>();
		int dpn=rcfDependenciaBuscar.getDpnId();
		int crr=rcfCarreraBuscar.getCrrId();
		int prac=rcfPeriodoAcademicoBuscar.getPracId();
		PeriodoAcademico pracAux = new PeriodoAcademico();
		Dependencia dpnAux = new Dependencia();
		Carrera crrAux = new Carrera();
		try {
			
			rcfListMatriculados = rcfReportesConsultorServicioJdbc.listarEstudiantesMatriculadosXFacultadXCarreraXPeriodoAcademico(dpn, crr, prac);
			
			pracAux = rcfPeriodoAcademicoServicio.buscarPorId(rcfPeriodoAcademicoBuscar.getPracId());
			dpnAux = rcfDependenciaServicio.buscarPorId(rcfDependenciaBuscar.getDpnId());
			crrAux = rcfCarreraServicio.buscarPorId(rcfCarreraBuscar.getCrrId());
			generarReporteMatriculados(rcfListMatriculados,pracAux,dpnAux,crrAux);
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	//Realiza la busqueda de los Estudiantes que realizaron solicitudes terceras matriculas por la Facultad,Carrera y Periodo Academico Seleccionado
	public void buscarTercerasMatriculas(){
		rcfListEstudiantesTercerasMatriculas = new ArrayList<SolicitudTerceraMatriculaDto>();
		PeriodoAcademico pracAux = new PeriodoAcademico();
		Dependencia dpnAux = new Dependencia();
		Carrera crrAux = new Carrera();
		int crr=rcfCarreraBuscar.getCrrId();
		int prac=rcfPeriodoAcademicoBuscar.getPracId();
		int validador=0;
		String observacion;
		String responsableAux="";
		String responsable1="";
		String responsable2="";
		Persona responsableResuelve;
		Persona responsableVerifica;
		try {
			
			rcfListEstudiantesTercerasMatriculas = rcfReportesConsultorServicioJdbc.listarEstudiantesTercerasMatriculasXCarreraXPeriodoAcademico(crr, prac);
			for(SolicitudTerceraMatriculaDto item:rcfListEstudiantesTercerasMatriculas){
				observacion = item.getSltrmtObservacion();
				for(int i = 0; i < observacion.length(); i++){
					if(observacion.charAt(i)==','){
						validador=0;
						responsable1=responsableAux;
						responsableAux="";
					}
					if(validador!= 0 && observacion.charAt(i)!=' '){
						responsableAux=responsableAux+observacion.charAt(i);
					}
					if(observacion.charAt(i)==':'){
						validador=1;
					}
				}
				responsable2=responsableAux;
				responsableResuelve = rcfPersonaServicio.buscarPorId(rcfUsuarioServicio.buscarPorNick(responsable1).getUsrPersona().getPrsId());
				responsableVerifica = rcfPersonaServicio.buscarPorId(rcfUsuarioServicio.buscarPorNick(responsable2).getUsrPersona().getPrsId());
				item.setSltrmtObservacion("Resuelve Solicitud: "+responsableResuelve.getPrsNombres()+" "+responsableResuelve.getPrsPrimerApellido()+" "+
				responsableResuelve.getPrsSegundoApellido()+", Verifica Solicitud: "+responsableVerifica.getPrsNombres()+" "+responsableVerifica.getPrsPrimerApellido()+
				" "+responsableVerifica.getPrsSegundoApellido());
				
				if(item.getSltrmtTipo()==SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_VALUE){
					switch(item.getSltrmtEstado()){
					case 0:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_LABEL);
						break;
					case 1:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_LABEL);
						break;
					case 2:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);
						break;
					case 3:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);
						break;
					case 4:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);
						break;
					}
				}else{
					switch(item.getSltrmtEstado()){
					case 5:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_LABEL);
						break;
					case 6:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);
						break;
					case 7:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);
						break;
					case 8:
						item.setSltrmtEstadoLabel(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);
						break;
					}	
				}
			}
			
		pracAux = rcfPeriodoAcademicoServicio.buscarPorId(rcfPeriodoAcademicoBuscar.getPracId());
		dpnAux = rcfDependenciaServicio.buscarPorId(rcfDependenciaBuscar.getDpnId());
		crrAux = rcfCarreraServicio.buscarPorId(rcfCarreraBuscar.getCrrId());	
		generarReporteTercerasMatriculas(rcfListEstudiantesTercerasMatriculas,pracAux,dpnAux,crrAux);
		
		} catch (SolicitudTerceraMatriculaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (SolicitudTerceraMatriculaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	//Realiza la busqueda de las notas de los Estudiantes por la Facultad,Carrera y Periodo Academico seleccionado
	public void buscarNotasEstudiantes(){
		PeriodoAcademico pracAux = new PeriodoAcademico();
		Dependencia dpnAux = new Dependencia();
		Carrera crrAux = new Carrera();
		int crr=rcfCarreraBuscar.getCrrId();
		int prac=rcfPeriodoAcademicoBuscar.getPracId();
		String nombreCompleto="";
		String Estado="";
		double Nota;
		try {
			
			rcfListEstudiantesNotas = rcfReportesConsultorServicioJdbc.listarNotasEstudiantesXCarreraXPeriodoAcademico(crr, prac);
			
			for(RecordEstudianteDto item:rcfListEstudiantesNotas){
				
				nombreCompleto = item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres();	
				item.setPrsNombreCompleto(nombreCompleto);
				
				if(item.getClfNotaFinal()==null){
//					Estado="Matriculado";
					Estado="Reprobado";	
				}else{
					Nota = item.getClfNotaFinal().doubleValue();
					if(Nota>=28.00){
						Estado="Aprobado";
					}else{
						Estado="Reprobado";				
					}
				}
				item.setClfEstadoLabel(Estado);
			}
			
			pracAux = rcfPeriodoAcademicoServicio.buscarPorId(rcfPeriodoAcademicoBuscar.getPracId());
			dpnAux = rcfDependenciaServicio.buscarPorId(rcfDependenciaBuscar.getDpnId());
			crrAux = rcfCarreraServicio.buscarPorId(rcfCarreraBuscar.getCrrId());
			generarReporteNotasEstudiantes(rcfListEstudiantesNotas, pracAux, dpnAux, crrAux);
			
		} catch (RecordEstudianteDtoException | RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}
				 
		
	}
	
	//Genera el reporte de las notas de los estudiantes
	public void generarReporteNotasEstudiantes(List<RecordEstudianteDto> ListaNotasEstudiantes, PeriodoAcademico prac,Dependencia dpn,Carrera crr){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ************************************************************************//
		// ********* GENERACION DEL REPORTE SOLICITUDES TERCERA MATRICULA *********//
		// ************************************************************************//
		// ************************************************************************//
		frmRrmNombreReporte = "reporteNotasEstudiantes";
		frmRrmParametros = new HashMap<String, Object>();
		
		frmRrmParametros.put("Facultad", dpn.getDpnDescripcion());
		frmRrmParametros.put("Carrera", crr.getCrrDescripcion());
		frmRrmParametros.put("Periodo", prac.getPracDescripcion());
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reportesConsultor/NotasEstudiantes");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
		
		for (RecordEstudianteDto item : ListaNotasEstudiantes) {
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("Cedula", item.getPrsIdentificacion());
			datoEstudiantes.put("Nombres", item.getPrsNombreCompleto());
			datoEstudiantes.put("Semestre", item.getNvlDescripcion());
			datoEstudiantes.put("Materia", item.getMtrDescripcion());
			datoEstudiantes.put("Hemisemestre_1", item.getClfNota1());
			datoEstudiantes.put("Asistencia_1", item.getClfAsistencia1());
			datoEstudiantes.put("Hemisemestre_2", item.getClfNota2());
			datoEstudiantes.put("Asistencia_2", item.getClfAsistencia2());
			datoEstudiantes.put("Recuperacion", item.getClfNotaRecuperacion());
			datoEstudiantes.put("Promedio", item.getClfNotaFinal());
			datoEstudiantes.put("Asistencia_Final", item.getClfAsistenciaFinal());
			datoEstudiantes.put("Estado", item.getClfEstadoLabel());
			
			frmRrmCampos.add(datoEstudiantes);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
	}
	
	//Genera el reporte de las solicitudes de terceras matriculas
	public void generarReporteTercerasMatriculas(List<SolicitudTerceraMatriculaDto> ListaTercerasMatriculas,PeriodoAcademico prac,Dependencia dpn,Carrera crr){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ************************************************************************//
		// ********* GENERACION DEL REPORTE SOLICITUDES TERCERA MATRICULA *********//
		// ************************************************************************//
		// ************************************************************************//
		frmRrmNombreReporte = "reporteTercerasMatriculas";
		frmRrmParametros = new HashMap<String, Object>();
		
		frmRrmParametros.put("Facultad", dpn.getDpnDescripcion());
		frmRrmParametros.put("Carrera", crr.getCrrDescripcion());
		frmRrmParametros.put("Periodo", prac.getPracDescripcion());
		frmRrmParametros.put("Proceso", "Tercera Matricula");
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reportesConsultor/Solicitudes/TercerasMatriculas");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
	
		for (SolicitudTerceraMatriculaDto item : ListaTercerasMatriculas) {
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("Numeral", item.getNumeral());
			datoEstudiantes.put("Cedula", item.getPrsIdentificacion());
			datoEstudiantes.put("Apellido_Paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("Apellido_Materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("Nombres", item.getPrsNombres());
			datoEstudiantes.put("Semestre", item.getNvlDescripcion());
			datoEstudiantes.put("CodigoMateria", item.getMtrId());
			datoEstudiantes.put("Materia", item.getMtrDescripcion());
			datoEstudiantes.put("Tipo", item.getSltrmtTipo()==0? "SOLICITUD":"APELACIÓN");
			datoEstudiantes.put("Estado", item.getSltrmtEstadoLabel());
			datoEstudiantes.put("FechaCreacion", item.getSltrmtFechaSolicitud());
			datoEstudiantes.put("FechaProceso", item.getSltrmtFechaVerificacion());
			datoEstudiantes.put("Responsable", item.getSltrmtObservacion());
			datoEstudiantes.put("Observacion", item.getSltrmtObservacionFinal());
			frmRrmCampos.add(datoEstudiantes);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
			
	}
	
	//Genera el reporte de los matriculados
	public void generarReporteMatriculados(List<PersonaDto> ListaMatriculados,PeriodoAcademico prac,Dependencia dpn,Carrera crr){
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE MATRICULADOS ***************//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteMatriculados";
		frmRrmParametros = new HashMap<String, Object>();
		
		frmRrmParametros.put("Facultad", dpn.getDpnDescripcion());
		frmRrmParametros.put("Carrera", crr.getCrrDescripcion());
		frmRrmParametros.put("Periodo", prac.getPracDescripcion());
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/reportesConsultor/Matriculados");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
		frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoEstudiantes = null;
	
		for (PersonaDto item : ListaMatriculados) {
			datoEstudiantes = new HashMap<String, Object>();
			datoEstudiantes.put("Numeral", item.getNumeral());
			datoEstudiantes.put("Cedula", item.getPrsIdentificacion());
			datoEstudiantes.put("Apellido_Paterno", item.getPrsPrimerApellido());
			datoEstudiantes.put("Apellido_Materno", item.getPrsSegundoApellido());
			datoEstudiantes.put("Nombres", item.getPrsNombres());
			datoEstudiantes.put("Semestre", item.getNvlDescripcion());
			datoEstudiantes.put("Correo_Personal", item.getPrsMailPersonal());
			datoEstudiantes.put("Correo_Institucional", item.getPrsMailInstitucional());
			frmRrmCampos.add(datoEstudiantes);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
	
	}
	
	//********************************************************************/
	//*********************** METODOS AUXILIARES *************************/
	//********************************************************************/
	public void limpiar(){
		rcfActivacion = "false";
		rcfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		rcfDependenciaBuscar = new Dependencia(GeneralesConstantes.APP_ID_BASE);
		rcfCarreraBuscar = new Carrera(GeneralesConstantes.APP_ID_BASE);
		rcfListFacultades = new ArrayList<Dependencia>();
		rcfListPeriodoAcademico = new ArrayList<PeriodoAcademico>();
		rcfListCarreras = new ArrayList<Carrera>();
		rcfListMatriculados = new ArrayList<PersonaDto>();
		rcfListEstudiantesTercerasMatriculas = new ArrayList<SolicitudTerceraMatriculaDto>();
		rcfListEstudiantesNotas = new ArrayList<RecordEstudianteDto>();
		buscarDependenciasPregrado();
		buscarPeriodoAcademicoPregrado();
	}
	
	public String irInicio(){
		
		rcfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		rcfDependenciaBuscar = new Dependencia(GeneralesConstantes.APP_ID_BASE);
		rcfCarreraBuscar = new Carrera(GeneralesConstantes.APP_ID_BASE);
		rcfEstudianteBuscar = new PersonaDto();
		rcfListFacultades = new ArrayList<Dependencia>();
		rcfListPeriodoAcademico = new ArrayList<PeriodoAcademico>();
		rcfListCarreras = new ArrayList<Carrera>();
		rcfListMatriculados = new ArrayList<PersonaDto>();
		rcfListEstudiantesTercerasMatriculas = new ArrayList<SolicitudTerceraMatriculaDto>();
		rcfListEstudiantesNotas = new ArrayList<RecordEstudianteDto>();
		
		return "irInicio";
	}
	//****************************************************************/
	//*********************** GETTERS Y SETTERS **********************/
	//****************************************************************/
	
	public Usuario getRcfUsuario() {
		return rcfUsuario;
	}

	public List<RecordEstudianteDto> getRcfListEstudiantesNotas() {
		return rcfListEstudiantesNotas;
	}

	public void setRcfListEstudiantesNotas(List<RecordEstudianteDto> rcfListEstudiantesNotas) {
		this.rcfListEstudiantesNotas = rcfListEstudiantesNotas;
	}

	public void setRcfUsuario(Usuario rcfUsuario) {
		this.rcfUsuario = rcfUsuario;
	}

	public PersonaDto getRcfPersonaDto() {
		return rcfPersonaDto;
	}

	public void setRcfPersonaDto(PersonaDto rcfPersonaDto) {
		this.rcfPersonaDto = rcfPersonaDto;
	}

	public List<UsuarioRol> getRcfListUsuarioRol() {
		return rcfListUsuarioRol;
	}

	public void setRcfListUsuarioRol(List<UsuarioRol> rcfListUsuarioRol) {
		this.rcfListUsuarioRol = rcfListUsuarioRol;
	}

	public PersonaDto getRcfEstudianteBuscar() {
		return rcfEstudianteBuscar;
	}

	public void setRcfEstudianteBuscar(PersonaDto rcfEstudianteBuscar) {
		this.rcfEstudianteBuscar = rcfEstudianteBuscar;
	}

	public PeriodoAcademico getRcfPeriodoAcademicoBuscar() {
		return rcfPeriodoAcademicoBuscar;
	}

	public void setRcfPeriodoAcademicoBuscar(PeriodoAcademico rcfPeriodoAcademicoBuscar) {
		this.rcfPeriodoAcademicoBuscar = rcfPeriodoAcademicoBuscar;
	}

	public Dependencia getRcfDependenciaBuscar() {
		return rcfDependenciaBuscar;
	}

	public void setRcfDependenciaBuscar(Dependencia rcfDependenciaBuscar) {
		this.rcfDependenciaBuscar = rcfDependenciaBuscar;
	}

	public Carrera getRcfCarreraBuscar() {
		return rcfCarreraBuscar;
	}

	public void setRcfCarreraBuscar(Carrera rcfCarreraBuscar) {
		this.rcfCarreraBuscar = rcfCarreraBuscar;
	}

	public List<PersonaDto> getRcfListMatriculados() {
		return rcfListMatriculados;
	}

	public void setRcfListMatriculados(List<PersonaDto> rcfListMatriculados) {
		this.rcfListMatriculados = rcfListMatriculados;
	}

	public List<SolicitudTerceraMatriculaDto> getRcfListEstudiantesTercerasMatriculas() {
		return rcfListEstudiantesTercerasMatriculas;
	}

	public void setRcfListEstudiantesTercerasMatriculas(
			List<SolicitudTerceraMatriculaDto> rcfListEstudiantesTercerasMatriculas) {
		this.rcfListEstudiantesTercerasMatriculas = rcfListEstudiantesTercerasMatriculas;
	}

	public List<PeriodoAcademico> getRcfListPeriodoAcademico() {
		return rcfListPeriodoAcademico;
	}

	public void setRcfListPeriodoAcademico(List<PeriodoAcademico> rcfListPeriodoAcademico) {
		this.rcfListPeriodoAcademico = rcfListPeriodoAcademico;
	}

	public List<Dependencia> getRcfListFacultades() {
		return rcfListFacultades;
	}

	public void setRcfListFacultades(List<Dependencia> rcfListFacultades) {
		this.rcfListFacultades = rcfListFacultades;
	}

	public List<Carrera> getRcfListCarreras() {
		return rcfListCarreras;
	}

	public void setRcfListCarreras(List<Carrera> rcfListCarreras) {
		this.rcfListCarreras = rcfListCarreras;
	}

	public Timestamp getRcffecha_inicio() {
		return rcffecha_inicio;
	}

	public void setRcffecha_inicio(Timestamp rcffecha_inicio) {
		this.rcffecha_inicio = rcffecha_inicio;
	}

	public Timestamp getRcffecha_fin() {
		return rcffecha_fin;
	}

	public void setRcffecha_fin(Timestamp rcffecha_fin) {
		this.rcffecha_fin = rcffecha_fin;
	}

	public int getRcftipo_solicitud() {
		return rcftipo_solicitud;
	}

	public void setRcftipo_solicitud(int rcftipo_solicitud) {
		this.rcftipo_solicitud = rcftipo_solicitud;
	}

	public String getRcfActivacion() {
		return rcfActivacion;
	}

	public void setRcfActivacion(String rcfActivacion) {
		this.rcfActivacion = rcfActivacion;
	}
	
	
		
}
