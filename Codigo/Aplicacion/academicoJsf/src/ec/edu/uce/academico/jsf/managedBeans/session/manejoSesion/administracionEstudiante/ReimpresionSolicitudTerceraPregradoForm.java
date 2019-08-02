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
   
 ARCHIVO:     EstudianteNotasPregradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la consulta de notas del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-06-2017			 Marcelo Quishpe                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionEstudiante;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.SolicitudTerceraMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteApelacionTerceraForm;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSolicitudTerceraForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EstudianteNotasPregradoForm. 
 * Bean de sesion que maneja la consulta de notas del estudiante.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "reimpresionSolicitudTerceraForm")
@SessionScoped
public class ReimpresionSolicitudTerceraPregradoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario enpfUsuario;
	//PARA BUSQUEDA
	private PeriodoAcademico enpfPeriodoAcademicoBuscar;
	private CarreraDto enpfCarreraDtoBuscar;
	private List<SolicitudTerceraMatriculaDto> enpfListSolicitudTerceraDto;
	private List<SolicitudTerceraMatriculaDto> enpfListSolicitudTerceraAnterioresDto;
	private SolicitudTerceraMatricula enpfSolicitudBuscar;
	

	//LISTAS PARA DESPLIEGUE DE INFORMACION
	private List<PeriodoAcademico> enpfListPeriodoAcademico;
	private List<CarreraDto> enpfListCarreraDto;
	private List<MateriaDto> enpfListMateriaDto;	
	private List<FichaMatriculaDto> enpfListMatriculaDto;
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private Persona enpfEstudiante;
	private PeriodoAcademico enpfPeriodo;
	private Carrera enpfCarrera;
	private Integer tipo;
	private PersonaDto enpfDecano;
	private Dependencia enpfDependencia;
	private PeriodoAcademico enpfPeriodoAcademico;
	
	//ESTUDIANTE SAU
	private List<FichaInscripcionDto> stmfListaFichaInscripcionSau;
	
	//Reporte
	private PersonaDto enpDirectorCarrera;
	private int ecfActivadorReporte;
	private String enpActivar;
	
	
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
	FichaMatriculaDtoServicioJdbc servEnpfFichaMatriculaDto;
	@EJB 
	PeriodoAcademicoServicio servEnpfPeriodoAcademico;	
	@EJB 
	EstudianteDtoServicioJdbc  servEnpfEstudianteDto;
	@EJB 
	MateriaDtoServicioJdbc  servEnpfMateriaDto;
	@EJB 
	PersonaServicio  servPersonaServicio;
	@EJB 
	CarreraServicio  servCarreraaServicio;
	@EJB
	CarreraDtoServicioJdbc servCarreraServicioJdbc;
	@EJB
	SolicitudTerceraMatriculaDtoServicioJdbc servSolicitudTerceraJdbc;
	@EJB
	FichaInscripcionDtoServicioJdbc  servStmfFichaInscripcionServicio;
	@EJB
	PersonaDtoServicioJdbc servPersonaJdbc;
	@EJB
	DependenciaServicio servDependencia;

	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String irAlistarSolicitudes(Usuario usuario) {
		try {
			ecfActivadorReporte = 0;
			enpActivar = "false";
			enpfUsuario = usuario;//Guardamos el usuario en una variable
			
			//BUSCO LAS MATRICULAS DEL ESTUDIANTE CON LOS PARAMETROS  DE BUSQUEDA POR DEFECTO, SIN PASAR POR LA CARRERA DE FICHA INSCRIPCION
		// enpfListMatriculaDto = servEnpfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			
			enpfPeriodoAcademico = servEnpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			enpfEstudiante = servPersonaServicio.buscarPorId(enpfUsuario.getUsrPersona().getPrsId());
			enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);

			//INSTANCIO LOS OBJETOS DE BUSQUEDA
//			enpfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
			enpfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
			//INSTANCIO LA LISTA, LLAMANDO AL METODO GET
			tipo = GeneralesConstantes.APP_ID_BASE;
			getEnpfListCarreraDto();
			//OBTENER LA LISTA DE CARRERAS A LAS QUE ESTUDIANTE ESTÁ MATRICULADO.
			for (FichaMatriculaDto itemMatricula : enpfListMatriculaDto) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
				Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
				for (CarreraDto itemCarrera : enpfListCarreraDto) { // recorro la lista de carreras a las que el estudiante esta matriculado 
					if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
						encontro = true; // asigno el booleano a verdado
					}
				} //fin recorrido de lista auxiliar de carreras
				if(encontro == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
					CarreraDto carreraAgregar = new CarreraDto();
					carreraAgregar.setCrrId(itemMatricula.getCrrId());
					carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
					enpfListCarreraDto.add(carreraAgregar);
				}
			}
			
			
			//ESTUDIANTE SAU**********************************
			// BUSCO CARRERAS POR LA FICHA INSCRIPCION
		//	if((enpfListCarreraDto==null)||(enpfListCarreraDto.size()==0)){
			
					stmfListaFichaInscripcionSau=servStmfFichaInscripcionServicio.listarFcinXIdentificacionXEstado(enpfUsuario.getUsrPersona().getPrsIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
				
			
				for (FichaInscripcionDto itemInscripcion : stmfListaFichaInscripcionSau) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
					Boolean encontro2 = false; // boolenado para determinar si le encontro en la lista 
					for (CarreraDto itemCarrera : enpfListCarreraDto) { // recorro la lista de carreras a las que el estudiante esta matriculado 
						if(itemInscripcion.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
							encontro2 = true; // asigno el booleano a verdado
						}
					} //fin recorrido de lista auxiliar de carreras
					if(encontro2 == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
						CarreraDto carreraAgregar = new CarreraDto();
						carreraAgregar.setCrrId(itemInscripcion.getCrrId());
						carreraAgregar.setCrrDetalle(itemInscripcion.getCrrDetalle());
						carreraAgregar.setCrrDescripcion(itemInscripcion.getCrrDescripcion());
						carreraAgregar.setCrrEspeCodigo(itemInscripcion.getCrrEspeCodigo());
						enpfListCarreraDto.add(carreraAgregar);
					}
				}

				//FIN  BUSCAR CARRERAS ESTUDIANTE SAU
				
		//	}
			
			
//			//INSTANCIO (new) LA LISTA PERIODO ACADEMICO, LLAMANDO AL METODO PARA LLENARLO EN EL FOR
//			getEnpfListPeriodoAcademico();
//			//PERMITE OBTENER LA LISTA DE PERIODOS EN LOS QUE EL ESTUDIANTES ESTA MATRICULADO
//			for (FichaMatriculaDto itemMatricula : enpfListMatriculaDto) { // recorro la lista de matriculas del estudiante, para comparar el periodo.
//				Boolean encontro = false; // boolenado para determinar si le encontro en la lista de periodos
//				for (PeriodoAcademico itemPeriodoAcademico : enpfListPeriodoAcademico) { // recorro la lista
//					if(itemMatricula.getPracId() == itemPeriodoAcademico.getPracId()){ // si encuentro el periodo ya en la lista de periodos.
//						encontro = true; // asigno el booleano a verdadero.
//					}
//				} //fin recorrido de lista auxiliar 
//				if(encontro == false){ // si no econtró el periodo de la matricula en la lista de periodos agrego ese periodos a la lista.
//					PeriodoAcademico periodoAgregar = new PeriodoAcademico();
//					periodoAgregar.setPracId(itemMatricula.getPracId());  //Instancio el idPeriodo de la matricula en id periodoAgregar.
//					periodoAgregar.setPracDescripcion(itemMatricula.getPracDescripcion()); //Instancio la descripcionPeriodo de la matricula en la descripcion del  periodoAgregar.
//					enpfListPeriodoAcademico.add(periodoAgregar);   //Agrego el objeto PeriodoAcademico a la lista de periodos.
//				}
//			}
			
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			// TODO Auto-generated catch block
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
		
		return "irListarSolicitudes";
	}

	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(CarreraDto idCarrera){
		try {
			
			enpfListSolicitudTerceraDto= new ArrayList<>();
			enpfListSolicitudTerceraAnterioresDto=new ArrayList<>();
			enpfCarrera = servCarreraaServicio.buscarPorId(idCarrera.getCrrId());
			enpfDependencia = servDependencia.buscarFacultadXcrrId(idCarrera.getCrrId());
			//enpDirectorCarrera = servPersonaJdbc.buscarDirectorCarreraxidCarrera(idCarrera.getCrrId()); // Se verifica en el momento de generar el reporte por separado director y decano
			//enpfDecano = servPersonaJdbc.buscarPersonaXRolIdXFclId(RolConstantes.ROL_DECANO_VALUE, enpfDependencia.getDpnId()); // Se verifica en el momento de generar el reporte por separado director y decano
			
				enpfListSolicitudTerceraDto = servSolicitudTerceraJdbc.listarxIdentificacionxCarreraSolicitudApelacion(enpfUsuario.getUsrPersona().getPrsIdentificacion(), idCarrera.getCrrId(), tipo);
			   
				enpfListSolicitudTerceraAnterioresDto = servSolicitudTerceraJdbc.listarxIdentificacionxCarreraSolicitudApelacionAnteriores(enpfUsuario.getUsrPersona().getPrsIdentificacion(), idCarrera.getCrrId(), tipo);
			
			   if((enpfListSolicitudTerceraDto==null)||( enpfListSolicitudTerceraDto.size()<=0)){
				 FacesUtil.limpiarMensaje();
				 FacesUtil.mensajeError("No existe solicitudes en estado de solicitadas en este período");	//6 marzo del 2019
		      	 }
								
			
			
		} catch (SolicitudTerceraMatriculaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
//		catch (PersonaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaDtoNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			 FacesUtil.mensajeError("No existe información del Director de Carrera o Decano de la facultad");	
//		} 
		catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	public void imprimirReporte(){
		if((enpfListSolicitudTerceraDto!=null)&&( enpfListSolicitudTerceraDto.size()>0)){

		if(tipo == 0){
			
			try {
				enpDirectorCarrera = servPersonaJdbc.buscarDirectorCarreraxidCarrera(enpfCarrera.getCrrId());
				ecfActivadorReporte = 1;
				ReporteSolicitudTerceraForm.generarReporteReimpresion(enpfListSolicitudTerceraDto, enpDirectorCarrera, enpfUsuario);
				enpActivar = "true";
			} catch (PersonaDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDtoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				 FacesUtil.mensajeError("No existe información del Director de Carrera, acérquese a su carrera");	
			} 
			
		
		    }else{
		    try {
				enpfDecano = servPersonaJdbc.buscarPersonaXRolIdXFclId(RolConstantes.ROL_DECANO_VALUE, enpfDependencia.getDpnId());
				ecfActivadorReporte = 2;
				ReporteApelacionTerceraForm.generarReporteApelacionTerceraLista(enpfListSolicitudTerceraDto, enpfDependencia, enpfCarrera, enpfEstudiante, enpfPeriodoAcademico, enpfUsuario, enpfDecano);
				enpActivar = "true";
			} catch (PersonaDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDtoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				 FacesUtil.mensajeError("No existe información del Decano de su facultad, acérquese a su carrera");	
			} 
			
	    	}
		
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existe solicitudes generadas en este período");	
			
		}
		
	}
	
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar() {
//		enpfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		enpfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		enpfListSolicitudTerceraDto = new ArrayList<SolicitudTerceraMatriculaDto>();
		enpfListSolicitudTerceraAnterioresDto =new ArrayList<SolicitudTerceraMatriculaDto>();
//		enpfListCarreraDto = null;
		tipo = GeneralesConstantes.APP_ID_BASE;
		enpfDecano = new PersonaDto();
		enpfDependencia = new Dependencia();
		enpfPeriodoAcademico = new PeriodoAcademico();
		enpfCarrera = new Carrera();
//		enpfListMatriculaDto = null;
	}
	/**
	 * Limpia los parámetros ingresados  al cambiar carrera
	 */
	public void cambiarCarrera() {
		enpfListSolicitudTerceraDto = new ArrayList<SolicitudTerceraMatriculaDto>();
		enpfListSolicitudTerceraAnterioresDto = new ArrayList<SolicitudTerceraMatriculaDto>();
//		enpfListCarreraDto = null;
		tipo = GeneralesConstantes.APP_ID_BASE;
		enpfDecano = new PersonaDto();
		enpfDependencia = new Dependencia();
		enpfPeriodoAcademico = new PeriodoAcademico();
		enpfCarrera = new Carrera();
		enpActivar= "false";
//		enpfListMatriculaDto = null;
	}
	
	/**
	 * Limpia los parámetros ingresados  al cambiar tipo solicitud
	 */
	public void cambiarTipoSolicitud() {
		enpfListSolicitudTerceraDto = new ArrayList<SolicitudTerceraMatriculaDto>();
		enpfListSolicitudTerceraAnterioresDto = new ArrayList<SolicitudTerceraMatriculaDto>();
//		enpfListCarreraDto = null;
		enpfDecano = new PersonaDto();
		enpfDependencia = new Dependencia();
		enpfPeriodoAcademico = new PeriodoAcademico();
		enpfCarrera = new Carrera();
//		enpfListMatriculaDto = null;
		enpActivar= "false";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		enpfListMatriculaDto = null;
		enpfListPeriodoAcademico = null;
		enpfListCarreraDto = null;
		enpfPeriodoAcademicoBuscar = null;
		enpfCarreraDtoBuscar = null;
		enpfUsuario = null;
		enpfListMateriaDto = null;
		enpfCarrera = new Carrera();
		enpfEstudiante = null;
		enpfPeriodo = null;
		enpfListSolicitudTerceraDto = null;
		enpfListSolicitudTerceraAnterioresDto = null;
		enpfSolicitudBuscar =null;
		return "irInicio";
	}
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String regresarSolicitud(){
		enpfListSolicitudTerceraDto = null;
		enpfListSolicitudTerceraAnterioresDto = null;
		return "regresarListarSolicitudes";
	}


	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	public PeriodoAcademico getEnpfPeriodoAcademicoBuscar() {
		return enpfPeriodoAcademicoBuscar;
	}

	public void setEnpfPeriodoAcademicoBuscar(PeriodoAcademico enpfPeriodoAcademicoBuscar) {
		this.enpfPeriodoAcademicoBuscar = enpfPeriodoAcademicoBuscar;
	}

	public CarreraDto getEnpfCarreraDtoBuscar() {
		return enpfCarreraDtoBuscar;
	}

	public void setEnpfCarreraDtoBuscar(CarreraDto enpfCarreraDtoBuscar) {
		this.enpfCarreraDtoBuscar = enpfCarreraDtoBuscar;
	}

	public List<PeriodoAcademico> getEnpfListPeriodoAcademico() {
		enpfListPeriodoAcademico = enpfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):enpfListPeriodoAcademico;
		return enpfListPeriodoAcademico;
	}

	public void setEnpfListPeriodoAcademico(List<PeriodoAcademico> enpfListPeriodoAcademico) {
		this.enpfListPeriodoAcademico = enpfListPeriodoAcademico;
	}

	public List<CarreraDto> getEnpfListCarreraDto() {
		enpfListCarreraDto = enpfListCarreraDto==null?(new ArrayList<CarreraDto>()):enpfListCarreraDto;
		return enpfListCarreraDto;
	}

	public void setEnpfListCarreraDto(List<CarreraDto> enpfListCarreraDto) {
		this.enpfListCarreraDto = enpfListCarreraDto;
	}

	public List<FichaMatriculaDto> getEnpfListMatriculaDto() {
		enpfListMatriculaDto = enpfListMatriculaDto==null?(new ArrayList<FichaMatriculaDto>()):enpfListMatriculaDto;
		return enpfListMatriculaDto;
	}

	public void setEnpfListMatriculaDto(List<FichaMatriculaDto> enpfListMatriculaDto) {
		this.enpfListMatriculaDto = enpfListMatriculaDto;
	}

	public Usuario getEnpfUsuario() {
		return enpfUsuario;
	}

	public void setEnpfUsuario(Usuario enpfUsuario) {
		this.enpfUsuario = enpfUsuario;
	}

	public List<MateriaDto> getEnpfListMateriaDto() {
		enpfListMateriaDto = enpfListMateriaDto==null?(new ArrayList<MateriaDto>()):enpfListMateriaDto;
		return enpfListMateriaDto;
	}

	public void setEnpfListMateriaDto(List<MateriaDto> enpfListMateriaDto) {
		this.enpfListMateriaDto = enpfListMateriaDto;
	}
	
	public PeriodoAcademicoServicio getServEnpfPeriodoAcademico() {
		return servEnpfPeriodoAcademico;
	}

	public Persona getEnpfEstudiante() {
		return enpfEstudiante;
	}

	public void setEnpfEstudiante(Persona enpfEstudiante) {
		this.enpfEstudiante = enpfEstudiante;
	}

	public PeriodoAcademico getEnpfPeriodo() {
		return enpfPeriodo;
	}

	public void setEnpfPeriodo(PeriodoAcademico enpfPeriodo) {
		this.enpfPeriodo = enpfPeriodo;
	}

	public Carrera getEnpfCarrera() {
		return enpfCarrera;
	}

	public void setEnpfCarrera(Carrera enpfCarrera) {
		this.enpfCarrera = enpfCarrera;
	}


	public List<SolicitudTerceraMatriculaDto> getEnpfListSolicitudTerceraDto() {
		enpfListSolicitudTerceraDto = enpfListSolicitudTerceraDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):enpfListSolicitudTerceraDto;
		return enpfListSolicitudTerceraDto;
	}


	public void setEnpfListSolicitudTerceraDto(List<SolicitudTerceraMatriculaDto> enpfListSolicitudTerceraDto) {
		this.enpfListSolicitudTerceraDto = enpfListSolicitudTerceraDto;
	}


	public List<FichaInscripcionDto> getStmfListaFichaInscripcionSau() {
		return stmfListaFichaInscripcionSau;
	}


	public void setStmfListaFichaInscripcionSau(List<FichaInscripcionDto> stmfListaFichaInscripcionSau) {
		this.stmfListaFichaInscripcionSau = stmfListaFichaInscripcionSau;
	}


	public PersonaDto getEnpDirectorCarrera() {
		return enpDirectorCarrera;
	}


	public void setEnpDirectorCarrera(PersonaDto enpDirectorCarrera) {
		this.enpDirectorCarrera = enpDirectorCarrera;
	}


	public int getEcfActivadorReporte() {
		return ecfActivadorReporte;
	}


	public void setEcfActivadorReporte(int ecfActivadorReporte) {
		this.ecfActivadorReporte = ecfActivadorReporte;
	}


	public String getEnpActivar() {
		return enpActivar;
	}


	public void setEnpActivar(String enpActivar) {
		this.enpActivar = enpActivar;
	}


	public SolicitudTerceraMatricula getEnpfSolicitudBuscar() {
		return enpfSolicitudBuscar;
	}


	public void setEnpfSolicitudBuscar(SolicitudTerceraMatricula enpfSolicitudBuscar) {
		this.enpfSolicitudBuscar = enpfSolicitudBuscar;
	}


	public Integer getTipo() {
		return tipo;
	}


	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}


	public PersonaDto getEnpfDecano() {
		return enpfDecano;
	}


	public void setEnpfDecano(PersonaDto enpfDecano) {
		this.enpfDecano = enpfDecano;
	}


	public Dependencia getEnpfDependencia() {
		return enpfDependencia;
	}


	public void setEnpfDependencia(Dependencia enpfDependencia) {
		this.enpfDependencia = enpfDependencia;
	}


	public PeriodoAcademico getEnpfPeriodoAcademico() {
		return enpfPeriodoAcademico;
	}


	public void setEnpfPeriodoAcademico(PeriodoAcademico enpfPeriodoAcademico) {
		this.enpfPeriodoAcademico = enpfPeriodoAcademico;
	}
	
	public List<SolicitudTerceraMatriculaDto> getEnpfListSolicitudTerceraAnterioresDto() {
		enpfListSolicitudTerceraAnterioresDto = enpfListSolicitudTerceraAnterioresDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):enpfListSolicitudTerceraAnterioresDto;
		return enpfListSolicitudTerceraAnterioresDto;
	}


	public void setEnpfListSolicitudTerceraAnterioresDto(List<SolicitudTerceraMatriculaDto> enpfListSolicitudTerceraAnterioresDto) {
		
		this.enpfListSolicitudTerceraAnterioresDto = enpfListSolicitudTerceraAnterioresDto;
	}
	
	
	
	
}
