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
   
 ARCHIVO:    SolicitudTerceraMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la solicitud del estudiante para habilitar la tercera matricula. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-01-2018			 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.habilitacionTerceraMatricula;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
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
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.primefaces.event.FileUploadEvent;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CausalException;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoCausalConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSolicitudTerceraForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (session bean) SolicitudTerceraMatriculaForm. 
 * Bean de sesion que maneja la solicitud del estudiante para habilitar la tercera matricula.. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "solicitudTerceraMatriculaForm")
@SessionScoped
public class SolicitudTerceraMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = 9210281719911332407L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario stmfUsuario;
    //LISTAR MATERIAS REPETIDAS CON SEGUNDA MATRIUCLA
	private List<FichaMatriculaDto> stmfListFichaMatriculaDtoBusq;
	private CarreraDto stmfCarreraDtoBuscar;
	private List<CarreraDto> stmfListCarreraDtoBusq;
	private List<MateriaDto> stmfListMateriaDto;
	//MODAL SUBIR ARCHIVO
	private List<Causal> stmfListCausal;
	private MateriaDto stmfMateriaSeleccionada;//Se usa para ingresar el causal y e archivo en el modal SubirArchivo
	private String stmfNombreArchivoAuxiliar;
	private String stmfNombreArchivoSubido;
	//VERIFICAR CRONOGRAMA
	private Integer stmfTipoCronograma;
	private Dependencia stmfDependencia;
	private CronogramaActividadJdbcDto stmfCronogramaActividad;
	//PARA VISUALIZACIÓN
	private Boolean stmfInactivaOpcionSolicitar;
    //VALIDACIONES EN GUARDADOS
	private Integer stmfValidadorSeleccion;
	private Integer stmfVerificadorActivaModal; // activa modal guardar
	private Integer stmfActivaModalCargarSolicitud; // activa modal cargar causal y solicitud
	private PeriodoAcademico stmfPeriodoActivo; 
	
	//REPORTE
	private List<MateriaDto> stmfLista;
	private int ecfActivadorReporte;
	private Persona stmfPersona = new Persona();
	private Boolean stmfActivaBotonReporte;
	private PersonaDto stmfDirCarrera;
	
	//ESTUDIANTE SAU
	private List<FichaInscripcionDto> stmfListaFichaInscripcionSau;
	private List<RecordEstudianteSAUDto> stmfListaRecordEstudianteSau;

	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar() {
	}

	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB 
	MateriaDtoServicioJdbc servStmfMateriaDtoServicio;
	@EJB
	FichaMatriculaDtoServicioJdbc servStmfFichaMatriculaDtoServicioJdbc;
	@EJB 
	CausalServicio servStmfCausalServicio;
	@EJB 
	DependenciaServicio servStmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servStmfCronogramaActividadDtoServicioJdbc;
	@EJB
	PeriodoAcademicoServicio servStmfPeriodoAcademicoServicio;
	@EJB
	SolicitudTerceraMatriculaServicio servStmfSolicitudTerceraMatriculaServicio;
	@EJB
	PersonaServicio servStmfPersonaServicio;
	
	//Servicios para Estudiantes SAU
	@EJB
	FichaInscripcionDtoServicioJdbc  servStmfFichaInscripcionServicio;
	@EJB
	MatriculaServicioJdbc servStmfRecordEstudianteSAU;
	@EJB
	CarreraServicio servStmfCarreraSAU;
	@EJB
	MateriaServicio servStmfMateriaSAU;
	@EJB
	FichaInscripcionDtoServicioJdbc servStmfFichaInscripcionDtoSAU;
	@EJB
	FichaEstudianteServicio servStmfFichaEstudianteSAU;
	@EJB	
	PersonaDtoServicioJdbc servStmfPersonaRol;
	@EJB
	SolicitudTerceraMatriculaDtoServicioJdbc servSolicitudesTerceraMatriculaDtoJdbc;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/

	/**
	 * Dirige la navegación hacia la página de listar de materias que el estudiante debe solicitar  autorización de tercera matricula
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar materias
	 */
	public String irVerListaMaterias(Usuario usuario) {
		stmfUsuario = usuario;
		ecfActivadorReporte = 0;
		String retorno = null;
	try {
//			//INICIO PARAMETROS
			iniciarParametros();
//			//BUSCA LA LISTA DE MATRÍCULAS DEL ESTUDIANTE
			//stmfListFichaMatriculaDtoBusq= servStmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, stmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			stmfListFichaMatriculaDtoBusq= servStmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraParaTercerasMatriculas(GeneralesConstantes.APP_ID_BASE, stmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			
			getStmfListCarreraDtoBusq();
//			//OBTENER LA LISTA DE CARRERAS QUE LOS ESTUDIANTES ESTAN MATRICULADOS
			
			if(stmfListFichaMatriculaDtoBusq!=null){
			for (FichaMatriculaDto itemMatricula : stmfListFichaMatriculaDtoBusq) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
				Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
				for (CarreraDto itemCarrera : stmfListCarreraDtoBusq) { // recorro la lista de carreras a las que el estudiante esta matriculado 
					if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
						encontro = true; // asigno el booleano a verdado
					}
				} //fin recorrido de lista auxiliar de carreras
				if(encontro == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
					CarreraDto carreraAgregar = new CarreraDto();
					carreraAgregar.setCrrId(itemMatricula.getCrrId());
					carreraAgregar.setCrrDetalle(itemMatricula.getCrrDetalle());
					carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
					carreraAgregar.setCrrEspeCodigo(itemMatricula.getCrrEspeCodigo());
					stmfListCarreraDtoBusq.add(carreraAgregar);
				}
			}

	      }
			
			//ESTUDIANTE SAU**********************************
			// BUSCO CARRERAS POR LA FICHA INSCRIPCION
	//		 if((stmfListCarreraDtoBusq==null)||(stmfListCarreraDtoBusq.size()==0)){
			
			stmfListaFichaInscripcionSau=servStmfFichaInscripcionServicio.listarFcinXIdentificacionXEstado(stmfUsuario.getUsrPersona().getPrsIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
				
			if(stmfListaFichaInscripcionSau!=null){
				for (FichaInscripcionDto itemInscripcion : stmfListaFichaInscripcionSau) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
					Boolean encontro2 = false; // boolenado para determinar si le encontro en la lista 
					for (CarreraDto itemCarrera : stmfListCarreraDtoBusq) { // recorro la lista de carreras a las que el estudiante esta matriculado 
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
						stmfListCarreraDtoBusq.add(carreraAgregar);
					}
				}

			}	//FIN  BUSCAR CARRERAS ESTUDIANTE SAU
				
				
	//		}
			
				stmfPersona=servStmfPersonaServicio.buscarPorId(stmfUsuario.getUsrPersona().getPrsId());
				
			
			
			//LISTA DE CAUSALES PARA TERCERA MATRICULA
			stmfListCausal=servStmfCausalServicio.listarxTipo(TipoCausalConstantes.TIPO_CAUSAL_TERCERA_MATRICULA_VALUE);
			retorno = "irVerListaMaterias";
		} catch (FichaMatriculaException e) {
		  FacesUtil.limpiarMensaje();
	      FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.irVerListaMaterias.ficha.matricula.exception")));
		} catch (CausalNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.irVerListaMaterias.causal.no.encontrado.exception")));
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.irVerListaMaterias.lista.ficha.inscripcion.exception")));
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.irVerListaMaterias.lista.ficha.inscripcion.no.encontrado.exception")));
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	
		return retorno;
	}

	
	/**
	 * Método que busca las materias repetidas con segunda matrícula
	 */
	public void buscarMaterias(){
		
		
		List<MateriaDto> listaMateriasSIIU= new ArrayList<MateriaDto>();
	//	List<MateriaDto> listaMateriasSIIUHomologacion= new ArrayList<MateriaDto>();
		List<MateriaDto> listaMateriasAux2= new ArrayList<MateriaDto>();
		List<RecordEstudianteSAUDto> listaMateriasSau= new ArrayList<>();
		List<RecordEstudianteSAUDto> listaMateriasSauFiltrada= new ArrayList<>();
		FichaInscripcionDto fichaInscripcionDtoAux= new FichaInscripcionDto();
		Carrera carreraAux = new Carrera();
		stmfListMateriaDto=new ArrayList<>();
		stmfActivaModalCargarSolicitud = 0;
				
			try{
				//Verifico que se haya seleccionado una carrera
				if(stmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					
					stmfDirCarrera=servStmfPersonaRol.buscarDirectorCarreraxidCarrera(stmfCarreraDtoBuscar.getCrrId());
				
//					boolean cronogramaAbierto = false;
//					if(stmfCarreraDtoBuscar.getCrrId()==82 ||stmfCarreraDtoBuscar.getCrrId()==157){ //cronograma abierto para Medicina y medicna rediseño
//						cronogramaAbierto = true;
//						
//					}else{
//						
//						cronogramaAbierto =verificarCronogramaSolicitarTerceraMatricula(); //otras carreras se rigen al cronograma
//						
//					}
					
					//Verifico que este habilitado el cronograma de tercera matricula
			 	if(verificarCronogramaSolicitarTerceraMatricula()){   //comentado temporalmente por casos de medicina  8 mar 2019
				 
			//		if(cronogramaAbierto){ //Se cambia temporalmente por el de arriba
						
				
				    		//Buscar la ficha inscripcion 
				     		try {
								fichaInscripcionDtoAux=servStmfFichaInscripcionDtoSAU.buscarFcinXidentificacionXcarrera(stmfUsuario.getUsrPersona().getPrsIdentificacion(), stmfCarreraDtoBuscar.getCrrId());
							} catch (FichaInscripcionDtoException e) {
								fichaInscripcionDtoAux= null;
								FacesUtil.limpiarMensaje();
					            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.ficha.inscripcion.exception")));

								e.printStackTrace();
							} catch (FichaInscripcionDtoNoEncontradoException e) {
								fichaInscripcionDtoAux= null;
								FacesUtil.limpiarMensaje();
					            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.ficha.inscripcion.no.encontrado.exception")));

							}
				    		
				    		
				     		//Lista de materias homologadas con segunda y reprobadas,  sirve si se cuenta las veces tomadas de la materia para sacar listaMateriasSIIU
				     	//	listaMateriasSIIUHomologacion=servStmfMateriaDtoServicio.ListarMateriasXPrsIdXNumeroMatriculaXEstadoMateriaXCarreraxPeriodoHomologacion(stmfUsuario.getUsrPersona().getPrsId(),  DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE, RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE, stmfCarreraDtoBuscar.getCrrId());
				     		
				     		//Buscamos si el record de la lista consultada tiene ya generada alguna solicitud.
//				    		if(listaMateriasSIIUHomologacion!=null){
//				    		for (MateriaDto materiaDtoHomologado : listaMateriasSIIUHomologacion) {
//								if(!servStmfSolicitudTerceraMatriculaServicio.buscarXMtrIdXFcesId(materiaDtoHomologado.getMtrId(), fichaInscripcionDtoAux.getFcesId())){//Si no se genero una solicitud
//									stmfListMateriaDto.add(materiaDtoHomologado);  	//Añadimos la materia a la lista de materias por solicitar tercera matricula
//								}
//							  }
//				    		}
				     		
				     		
				    		//lista de Materias estudiante SIIU, con segunda y reprobadas
				    		listaMateriasSIIU=servStmfMateriaDtoServicio.ListarMateriasXPrsIdXNumeroMatriculaXEstadoMateriaXCarrera(stmfUsuario.getUsrPersona().getPrsId(), DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE, RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE, stmfCarreraDtoBuscar.getCrrId());
				    	
				    		
				    	//busco que las materias no tengan solicitudes de 3ra generadas.
				    if((listaMateriasSIIU!=null)&&(listaMateriasSIIU.size()>0)){
				    		for (MateriaDto materiaDto : listaMateriasSIIU) {
								
				    //			if(!servStmfSolicitudTerceraMatriculaServicio.buscarXMtrIdXFcesId(materiaDto.getMtrId(), fichaInscripcionDtoAux.getFcesId())){//Si no se genero una solicitud
    						if(!servStmfSolicitudTerceraMatriculaServicio.buscarXMtrIdXFcesIdXEstado(materiaDto.getMtrId(), fichaInscripcionDtoAux.getFcesId(), SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE)){// busco solicitudes activa	
									stmfListMateriaDto.add(materiaDto);  	//Añadimos la materia a la lista de materias por solicitar tercera matricula
								}else{
									
//									
//									 // NUEVO  CAMBIO 20 ago 2018  de forma temporal se da otra oportunidad a los NEGADOS en 2da Instancia antes. HECHO YA POR BASE****************
//								    // Busco las solicitudes creadas por cada materia y que no se hayan aprobado nunca
//						    			List <SolicitudTerceraMatriculaDto> listaSolicitudesAux=servSolicitudesTerceraMatriculaDtoJdbc.listarSolicitudesxFcesIdXMtrId(materiaDto.getMtrId(), fichaInscripcionDtoAux.getFcesId()) ;
//						    	 if((listaSolicitudesAux!=null)&&(listaSolicitudesAux.size()>0)){ //if 2
//						    			boolean solicitudAprobado = false;
//			                        for (SolicitudTerceraMatriculaDto solicitudDto : listaSolicitudesAux) {
//										if((solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE)
//											||(solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE)){
//											solicitudAprobado= true;
//											break;								
//										}
//									}
//						    			
//			                        if(!solicitudAprobado){ //No esta aprobada
//			                        	boolean solicitudOtrosEstados =false;
//			                            for (SolicitudTerceraMatriculaDto solicitudDto : listaSolicitudesAux) {
//			    							if((solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE)
//			    								||	(solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE)
//			    								||(solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE)
//			    								||(solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE)){
//			    								solicitudOtrosEstados= true;
//			    								break;								
//			    							}
//			    						}
//			                        	
//			                        	if(!solicitudOtrosEstados){
//			                        	boolean apelacionNegada =false;
//			                            for (SolicitudTerceraMatriculaDto solicitudDto : listaSolicitudesAux) {
//			    							if((solicitudDto.getSltrmtEstado()==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE)
//			    								){
//			    								apelacionNegada= true;
//			    								break;								
//			    							}
//			    						}
//			                            if(apelacionNegada){
//			                            	stmfListMateriaDto.add(materiaDto);
//			                            }
//			                         
//			                        	}  
//			                            
//			                         }
//									}
									
								}
								
							}
				    	}
				    	
					    		
				    	//  SI NO EXISTE RECORD EN SIIU BUSCAMOS COMO ESTUDIANTE SAU
				    	
		 //   	if	((listaMateriasSIIU==null)||(listaMateriasSIIU.size()==0)){
				   	try {
				    		 //buscamos carrera por la carrera seleccionada
				    			carreraAux=servStmfCarreraSAU.buscarPorId(stmfCarreraDtoBuscar.getCrrId());
				    			
				    		
				    			if(carreraAux.getCrrEspeCodigo()!=null){ //Si se selecciono una carrera
								    //Se lista las materias en el SAU
				    				listaMateriasSau=servStmfRecordEstudianteSAU.buscarRecordAcademicoSAU(stmfUsuario.getUsrPersona().getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
				    			}
				    		
				    		} catch (RecordEstudianteNoEncontradoException e) {
								//e.printStackTrace();
				    			listaMateriasSau = new ArrayList<>() ;
							} catch (RecordEstudianteException e) {
								listaMateriasSau = new ArrayList<>() ;
								FacesUtil.limpiarMensaje();
					            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.recordEstudianteSau.exception")));
							} catch (CarreraNoEncontradoException e) {
								listaMateriasSau = new ArrayList<>() ;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.carrera.no.encontrada.exception")));
							} catch (CarreraException e) {
								listaMateriasSau = new ArrayList<>() ;
								FacesUtil.limpiarMensaje();
					            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.carrera.exception")));
							}
				     		
				     		//BUSCO MATERIAS REPETIDAS CON SEGUNDA MATRICULA
				     		if(listaMateriasSau!=null){
				     		listaMateriasSauFiltrada=materiasRepetidasConSegunda(listaMateriasSau);
				     		
				     		
				     		//SOLO REALIZA  SI EXISTE RECORD EN EL SAU
				     		if((listaMateriasSauFiltrada!=null)&&(listaMateriasSauFiltrada.size())!=0){
					    		//CREAR LA LISTA DE MATERIA DTO, EN CASO DE ESTUDIANTE SAU
				    		for (RecordEstudianteSAUDto recordEstudianteSAUDto : listaMateriasSauFiltrada) {
				    			MateriaDto materiaDto= new MateriaDto();
				    			Materia materiaSauAux= new Materia();
				    			Carrera carreraSauAux= new Carrera();
				    			//Busco Materia en Tabla
				    			
									try {  
										//Necesito del SAU:   codigoMateria
										carreraSauAux=servStmfCarreraSAU.buscarPorId(stmfCarreraDtoBuscar.getCrrId());
										materiaSauAux=servStmfMateriaSAU.buscarXCodigoXEspeCodigo(recordEstudianteSAUDto.getCodigoMateria(),carreraSauAux.getCrrEspeCodigo() );
									if((fichaInscripcionDtoAux!=null)&&(carreraSauAux!=null)&&(materiaSauAux!=null)){    
										//Creo el objeto MateriaDto
										materiaDto.setMtrId(materiaSauAux.getMtrId());
										materiaDto.setMtrDescripcion(materiaSauAux.getMtrDescripcion());
										materiaDto.setMtrCodigo(materiaSauAux.getMtrCodigo());
										materiaDto.setCrrId(carreraSauAux.getCrrId());
										materiaDto.setFcesId(fichaInscripcionDtoAux.getFcesId());
										
										//transformo el  estado y número de matricula entre SAU a  SIIU
										if(recordEstudianteSAUDto.getEstado()==SAUConstantes.MATERIA_APROBADA_VALUE){
											materiaDto.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
										}else if(recordEstudianteSAUDto.getEstado()==SAUConstantes.MATERIA_REPROBADA_VALUE) {
											materiaDto.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
											
										}else if(recordEstudianteSAUDto.getEstado()==SAUConstantes.MATERIA_HOMOLOGADA_VALUE){
											materiaDto.setRcesEstado(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE);
										}
										if(recordEstudianteSAUDto.getNumeroMtricula()==SAUConstantes.PRIMERA_MATRICULA_VALUE){
											materiaDto.setDtmtNumero(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
										}else if(recordEstudianteSAUDto.getNumeroMtricula()==SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
											materiaDto.setDtmtNumero(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE);
											
										}else if(recordEstudianteSAUDto.getNumeroMtricula()==SAUConstantes.TERCERA_MATRICULA_VALUE) {
											materiaDto.setDtmtNumero(DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE);
											
										}
										//lleno la lista de MateriaDto
					    		     	listaMateriasAux2.add(materiaDto);
					    		     	
									} 	
					    		     	
					    		     	
					    		    } catch (MateriaValidacionException e) {
										FacesUtil.limpiarMensaje();
										//FacesUtil.mensajeError(e.getMessage());
										FacesUtil.mensajeError("No se encontró asignaturas o se encuentra inactiva, en la que se requiere realizar solicitud de tercera matrícula");
									} catch (MateriaException e) {
										FacesUtil.limpiarMensaje();
										FacesUtil.mensajeError(e.getMessage());
									} catch (CarreraNoEncontradoException e) {
										FacesUtil.limpiarMensaje();
										FacesUtil.mensajeError(e.getMessage());
									} catch (CarreraException e) {
										FacesUtil.limpiarMensaje();
										FacesUtil.mensajeError(e.getMessage());
									}
							}
				    
				    		//ESTUDIANTE SAU:buscar si la materia ya tiene generada una solicitud
				    	  	
				    		if(listaMateriasAux2!=null){
				    		for (MateriaDto materiaDtoSau : listaMateriasAux2) {
                       //  if(!servStmfSolicitudTerceraMatriculaServicio.buscarXMtrIdXFcesId(materiaDtoSau.getMtrId(), fichaInscripcionDtoAux.getFcesId())){//busco que no exista solicitud de tercera
				    			if(!servStmfSolicitudTerceraMatriculaServicio.buscarXMtrIdXFcesIdXEstado(materiaDtoSau.getMtrId(), fichaInscripcionDtoAux.getFcesId(), SolicitudTerceraMatriculaConstantes.ESTADO_REGISTRO_ACTIVO_VALUE)){	//busco solo solicitudes activas
				    			stmfListMateriaDto.add(materiaDtoSau);  	//Añadimos la materia a la lsita de materias por solicitar tercera matricula
							     }
								
						    	}
				    		
				    		}
				    		
				  	     }	
				   		}
				     		
				    //	}  //FIN ESTUDIANTE SAU   if	
				    		
				    		//verificamos si la lista esta vacia, deshabilitamos el botón de guardar solicitudes
				    		if((stmfListMateriaDto.size()<=0)||(stmfListMateriaDto==null))	{
				    	     	stmfInactivaOpcionSolicitar=Boolean.TRUE;
				    	    }else{
				    	    	stmfInactivaOpcionSolicitar=Boolean.FALSE;
				    	    }
				    	}  //VerificarCronograma o cronogramaAbierto
				    	
				}else{
					FacesUtil.limpiarMensaje();
		            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.validacion.carrera.exception")));

				}
	    } catch (MateriaDtoException e) {
		FacesUtil.limpiarMensaje();
	    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.exception")));
        } catch (PersonaDtoException e1) {
        	FacesUtil.limpiarMensaje();
    	    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.director.carrera.exception")));
		} catch (PersonaDtoNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.buscar.materias.director.carrera.no.encontrado.exception")));
		} 
			
	}
	
	
	/**
	 * Método para buscar materias repetidas con segunda matricula. 
	 * @param listaSauRecibida.- el record del estudiante SAU
	 * @return retorna  las materias reprobadas con segunda matrícula, que no tengan una tercera matrícula. 
	 */
	public  List<RecordEstudianteSAUDto> materiasRepetidasConSegunda(List<RecordEstudianteSAUDto> listaSauRecibida){
		
		List<RecordEstudianteSAUDto> listaRetornada= new ArrayList<>();
		
		for (RecordEstudianteSAUDto itemMateriaRecibida : listaSauRecibida) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
			Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
			for (RecordEstudianteSAUDto itemMateriaSegunda : listaRetornada) { // recorro la lista de carreras a las que el estudiante esta matriculado 
				if(itemMateriaRecibida.getCodigoMateria().equals(itemMateriaSegunda.getCodigoMateria())){ // si encuentro la carrera de la matricula en la lista de carreas
					 encontro=true;
				}			
				
				
			} //fin recorrido de lista auxiliar de carreras
			if(encontro == false){ 
				if((itemMateriaRecibida.getNumeroMtricula()==SAUConstantes.SEGUNDA_MATRICULA_VALUE)&&(itemMateriaRecibida.getEstado()==SAUConstantes.MATERIA_REPROBADA_VALUE)){
				
					if(verificarTercera(itemMateriaRecibida,listaSauRecibida )==false){
						RecordEstudianteSAUDto materiaAgregar = new RecordEstudianteSAUDto();
						materiaAgregar.setCarreraId(itemMateriaRecibida.getCarreraId());
						materiaAgregar.setCodigoMateria(itemMateriaRecibida.getCodigoMateria());
						materiaAgregar.setEstado(itemMateriaRecibida.getEstado());
						materiaAgregar.setNumeroMtricula(itemMateriaRecibida.getNumeroMtricula());
						materiaAgregar.setPeriodoAcademico(itemMateriaRecibida.getPeriodoAcademico());
						materiaAgregar.setSemestre(itemMateriaRecibida.getSemestre());
						listaRetornada.add(materiaAgregar);
					}
					
				}
			}				
				
		}
		
		return listaRetornada;
	}
	
	/**
	 * Método para verificar si una materia tiene tercera matricula  
	 * @param materiaPorVerificarTercera la materia a buscar y listaSau.- el record del estudiante SAU
	 * @return retorna Verdadero si la materia esta con tercera matricula en la lista caso contrario retorna falso
	 */
	
	public boolean verificarTercera(RecordEstudianteSAUDto materiaPorVerificarTercera, List<RecordEstudianteSAUDto> listaSau){
		Boolean retorno = false;
		
		   for (RecordEstudianteSAUDto recordEstudianteSAUDto : listaSau) {
			
			   if((recordEstudianteSAUDto.getCodigoMateria().equals(materiaPorVerificarTercera.getCodigoMateria()))){
					   
				if ( (recordEstudianteSAUDto.getNumeroMtricula()==SAUConstantes.TERCERA_MATRICULA_VALUE)
						||(recordEstudianteSAUDto.getEstado()==SAUConstantes.MATERIA_APROBADA_VALUE)
						||(recordEstudianteSAUDto.getEstado()==SAUConstantes.MATERIA_HOMOLOGADA_VALUE)){
				   retorno= true;
				   break;
				}
			   }   
		}
		
				return retorno;
	}
	/**
	 * Método para guardar el archivo y la causal en el item
	 * @param materia -materia que se selecciona de la lista para cargar causal y la solicitud
	 */

	public void guardarSubirArchivo(MateriaDto materia) {
		Causal auxCausal = new Causal();
		auxCausal= null;
		
		if (materia.getCslId() != GeneralesConstantes.APP_ID_BASE) { //Verifica que se ha seleccionado una causal
			if (stmfNombreArchivoSubido != null) { //verifico que se haya cargado un archivo
					try {
						//Busco el causal
						auxCausal = servStmfCausalServicio.buscarPorId(materia.getCslId());
					} catch (CausalNoEncontradoException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("mensaje", "No se encontró el causal seleccionado por Id");//mensaje en ventana modal
					} catch (CausalException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("mensaje", "Error desconocido al buscar por Id");//mensaje en ventana modal
					} 

				if (auxCausal != null) {
					// Buscamos la materia en la lista de materias para guardar los valores
					for (MateriaDto itemMtr : stmfListMateriaDto) {
						if (itemMtr.getMtrId() == materia.getMtrId()) {
							itemMtr.setCslDescripcion(auxCausal.getCslDescripcion());
							itemMtr.setCslEstado(auxCausal.getCslEstado());
							itemMtr.setCslCodigo(auxCausal.getCslCodigo());
							// Guardamos el nombre del archivo en el objeto
							itemMtr.setSltrmtDocumentoSolicitud(stmfNombreArchivoSubido);
							break;
						}
					}
				}
				stmfActivaModalCargarSolicitud = 0;
				stmfNombreArchivoAuxiliar = null;
				stmfNombreArchivoSubido = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("mensaje", "Documento evidencia cargado con exito");//mensaje en ventana modal
			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje", "Debe seleccionar el documento evidencia");//mensaje en ventana modal
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje", "Debe seleccionar un causal");//mensaje en ventana modal
		}

	}
		
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		iniciarParametros();
		return "irInicio";
	}
	
	/**
	 * limpia el filtro de busqueda y panel de resultado
	 */
	public void limpiarFormListarMaterias()	{
		stmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		stmfListMateriaDto=null;
		stmfInactivaOpcionSolicitar=Boolean.TRUE;
	}
	
	
	/**
	 * llama a generar el reporte PDF
	 */
	public void llamarReporte(){
		Carrera crrAux= new Carrera();
		
		
		try {
			   crrAux = servStmfCarreraSAU.buscarPorId(stmfCarreraDtoBuscar.getCrrId());
				
			} catch (CarreraNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			} catch (CarreraException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			}
		if((stmfLista!=null)&&(stmfLista.size()>0)){
		
		ReporteSolicitudTerceraForm.generarReporteSolicitudTercera(stmfLista, stmfDependencia, crrAux, stmfPersona, stmfPeriodoActivo, stmfUsuario, stmfDirCarrera);
		ecfActivadorReporte = 1;
		
		}else{
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.llamar.reporte.validacion.lista.solicitudes.vacia.exception")));
			
		}
		
	
	}
	
	/**
	 * Método que guarda la solicitud de tercera matricula en la BDD
	 * @return retorna - la navegación de la página listar matriculas
	 */
	public void solicitarTerceraMatricula(){
		stmfPeriodoActivo= new PeriodoAcademico();
		Carrera crrAux2= new Carrera();
		//Busco el periodo academico activo
		try {
			stmfPeriodoActivo=servStmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		
	//VERIFICO QUE EXISTAN MATERIAS PARA RETIRO DE LA MATRICULA 
		if(stmfListMateriaDto!=null && stmfListMateriaDto.size()>0){
			try {
			//ASIGNO LAS MATERIAS SELECCIONADAS A OTRA LISTA PARA GUARDAR
				List<MateriaDto> listaSeleccionada = new ArrayList<>();
				for (MateriaDto item : stmfListMateriaDto) {
					if(item.getIsSeleccionado()){
						listaSeleccionada.add(item);
					}
				}
				//CAMBIO DE ESTADO A SOLICITUD DE RETIRO EN DETALLE MATRICULA CON LA LISTA DE MATERIAS
				List<MateriaDto> listaSeleccionadaGuarda = new ArrayList<>();
				List<MateriaDto> listaSeleccionadaNoGuarda = new ArrayList<>();
				String rutaNombre = null;
				String rutaTemporal = null;
				if(servStmfSolicitudTerceraMatriculaServicio.generarSolicitudTerceraMatricula(listaSeleccionada, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE, stmfPeriodoActivo, stmfUsuario)){
				//PARA CARGA DE ARCHIVO EN EL SERVIDOR
				
					for (MateriaDto item : listaSeleccionada) {
							if(item.getIsSeleccionado()){
								String extension = GeneralesUtilidades.obtenerExtension(item.getSltrmtDocumentoSolicitud());
								rutaNombre = SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_LABEL+"-"+stmfPeriodoActivo.getPracId()+"-"+item.getMtrId()+"-"+stmfUsuario.getUsrNick()+ "." + extension;
								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + item.getSltrmtDocumentoSolicitud();
								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA+ rutaNombre);
								
								listaSeleccionadaGuarda.add(item);
								
							}else{
								listaSeleccionadaNoGuarda.add(item);
							}
							
							
							item.setRutaPdf(rutaNombre);
													
							rutaNombre = null;
							rutaTemporal = null;
						}
					
					stmfListMateriaDto = listaSeleccionadaNoGuarda;
					//Llamar reporte
					stmfLista = listaSeleccionada;
					
					 
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.solicitar.tercera.matricula.validacion.exito")));
					
					
					
					crrAux2 = servStmfCarreraSAU.buscarPorId(stmfCarreraDtoBuscar.getCrrId());
					
					 
					try {
						//******************************************************************************
						  //************************* ACA INICIA EL ENVIO DE MAIL ************************
						//******************************************************************************
						Connection connection = null;
						Session session = null;
						MessageProducer producer = null;
						ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin","nio://10.20.1.64:61616");
						connection = connectionFactory.createConnection();
						connection.start();
						session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						Destination destino = session.createQueue("COLA_MAIL_DTO");
						// Creamos un productor
						producer = session.createProducer(destino);
						
						
						JasperReport jasperReport = null;
						JasperPrint jasperPrint;
						List<Map<String, Object>> frmRrmCampos = null;
						Map<String, Object> frmRrmParametros = null;
						
						String frmRrmNombreReporte = null;
						List<Causal> listaCausalaux = new ArrayList<>();
						// ****************************************************************//
						// ********* GENERACION DEL REPORTE DE REGISTRO MATRICULA *********//
						// ****************************************************************//
						// ****************************************************************//
						frmRrmNombreReporte = "solicitudTerceraMatricula";
						frmRrmParametros = new HashMap<String, Object>();
						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
						//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
						String fecha = formato.format(new Date());
						frmRrmParametros.put("fecha",fecha);
						//frmRrmParametros.put("facultad", stmfDependencia.getDpnDescripcion());
					
//						if(crrAux2.getCrrDescripcion()!=null){
//						frmRrmParametros.put("carrera", crrAux2.getCrrDescripcion());
//						}else{
//							frmRrmParametros.put("carrera", " ");	
//						}
						
						String nombres = null;
						if(stmfPersona!=null){
						 nombres=stmfPersona.getPrsNombres()+" "+stmfPersona.getPrsPrimerApellido().toUpperCase()+" "
								+(stmfPersona.getPrsSegundoApellido() == null?" ":stmfPersona.getPrsSegundoApellido());
						}else{
							nombres= " ";
							
						}
					//	frmRrmParametros.put("nombre",nombres );
						
//						if(stmfPersona.getPrsIdentificacion()!=null){
//						frmRrmParametros.put("identificacion", stmfPersona.getPrsIdentificacion());
//						}else{
//							frmRrmParametros.put("identificacion"," ");
//						}
						
						String dirCarrera= null;
						
						if(stmfDirCarrera!=null){
						  dirCarrera = stmfDirCarrera.getPrsNombres()+" "+stmfDirCarrera.getPrsPrimerApellido().toUpperCase()+" "
								+(stmfDirCarrera.getPrsSegundoApellido() == null?" ":stmfDirCarrera.getPrsSegundoApellido());
						}else{
							dirCarrera=" ";
							
						}
						
						StringBuilder sbTextoInicial = new StringBuilder();
						sbTextoInicial.append("Señor(a)");sbTextoInicial.append("\n");
						sbTextoInicial.append(dirCarrera);sbTextoInicial.append("\n");
						sbTextoInicial.append("DIRECTOR(A) DE LA CARRERA DE ");
						if(crrAux2.getCrrDescripcion()!=null){
							sbTextoInicial.append(crrAux2.getCrrDescripcion());sbTextoInicial.append("\n");
						}else{
							sbTextoInicial.append(" ");sbTextoInicial.append("\n");
						}
						sbTextoInicial.append("FACULTAD DE ");
						if(stmfDependencia.getDpnDescripcion()!=null){
							sbTextoInicial.append(stmfDependencia.getDpnDescripcion());sbTextoInicial.append("\n");
						}else{
							sbTextoInicial.append(" ");sbTextoInicial.append("\n");
						}
						sbTextoInicial.append("Presente.- ");sbTextoInicial.append("\n\n");
						sbTextoInicial.append("Señor(a) Director(a)");sbTextoInicial.append("\n");
						
						frmRrmParametros.put("textoInicial", sbTextoInicial.toString());
						
						
						
						StringBuilder sbTexto = new StringBuilder();
						sbTexto.append("Yo, "); 
						sbTexto.append(nombres);
						sbTexto.append(" con identificación No. ");
						sbTexto.append(stmfPersona.getPrsIdentificacion());
						sbTexto.append(" estudiante de la Carrera de ");
						if(crrAux2.getCrrDescripcion()!=null){
							sbTexto.append(crrAux2.getCrrDescripcion());
							}else{
								sbTexto.append(" ");
							}
						sbTexto.append(" de la Facultad de ");
						if(stmfDependencia.getDpnDescripcion()!=null){
							sbTexto.append(stmfDependencia.getDpnDescripcion());
							}else{
								sbTexto.append(" ");
							}
					
						sbTexto.append(" ,solicito a usted muy comedidamente me conceda la autorización respectiva para realizar la Tercera Matrícula en la(s) siguiente(s) asignatura(s): ");
						
						frmRrmParametros.put("texto", sbTexto.toString());
						StringBuilder sbPeriodo = new StringBuilder();
						StringBuilder sbCodigo = new StringBuilder();
						StringBuilder sbAsignatura = new StringBuilder();
						StringBuilder sbHora = new StringBuilder();
						StringBuilder sbCausal = new StringBuilder();
						StringBuilder sbEvidencia = new StringBuilder();
						
						for (MateriaDto item : listaSeleccionada) {
							if(item.getMtrDescripcion().length() <= 44){
								if(stmfPeriodoActivo.getPracDescripcion()!=null){
									sbPeriodo.append(stmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n");
								}else{
									sbPeriodo.append(" ");sbPeriodo.append("\n\n");
								}
																
								
								if(item.getMtrCodigo() != null){
									sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
								}else{
									sbCodigo.append(" ");sbCodigo.append("\n\n");
								}
								
								sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
								
								if(item.getMtrHoras() != null){
									sbHora.append(item.getMtrHoras());sbHora.append("\n\n");
								}else{
									sbHora.append(" ");sbHora.append("\n\n");
								}
								
								if(item.getCslCodigo()!=null){
									sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n");
									}else{
									 sbCausal.append(" ");sbCausal.append("\n\n");
									}
									
									if(item.getRutaPdf()!=null){
									sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n");
									}
							}
							if(item.getMtrDescripcion().length() > 44 && item.getMtrDescripcion().length() <= 88){
								if(stmfPeriodoActivo.getPracDescripcion()!=null){
									sbPeriodo.append(stmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n");
								}else{
									sbPeriodo.append(" ");sbPeriodo.append("\n\n\n");
								}
								
								if(item.getMtrCodigo() != null){
									sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
								}else{
									sbCodigo.append(" ");sbCodigo.append("\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
								if(item.getMtrHoras() != null){
									sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n");
								}else{
									sbHora.append(" ");sbHora.append("\n\n\n");
								}
								
								if(item.getCslCodigo()!=null){
								sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n");
								}else{
								 sbCausal.append(" ");sbCausal.append("\n\n\n");
								}
								
								if(item.getRutaPdf()!=null){
								sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n\n");
								}else{
									sbEvidencia.append(" ");sbEvidencia.append("\n\n\n");
								}
								
							}
							if(item.getMtrDescripcion().length() > 88 && item.getMtrDescripcion().length() <= 132){
								if(stmfPeriodoActivo.getPracDescripcion()!=null){
									sbPeriodo.append(stmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n");
								}else{
									sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n");
								}
								if(item.getMtrCodigo() != null){
									sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
								}else{
									sbCodigo.append("");sbCodigo.append("\n\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
								if(item.getMtrHoras() != null){
									sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n");
								}else{
									sbHora.append("");sbHora.append("\n\n\n\n");
								}
							
								   if(item.getCslCodigo()!=null){
									sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n");
									}else{
									 sbCausal.append(" ");sbCausal.append("\n\n\n\n");
									}
									
									if(item.getRutaPdf()!=null){
									sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n");
									}
							}
							if(item.getMtrDescripcion().length() > 132 && item.getMtrDescripcion().length() <= 176){
								if(stmfPeriodoActivo.getPracDescripcion()!=null){
									sbPeriodo.append(stmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n");
								}else{
									sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n");
								}
								
								if(item.getMtrCodigo() != null){
									sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
								}else{
									sbCodigo.append("");sbCodigo.append("\n\n\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
								if(item.getMtrHoras() != null){
									sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n");
								}else{
									sbHora.append("");sbHora.append("\n\n\n\n\n");
								}
								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n");
								}

								if (item.getRutaPdf() != null) {
									sbEvidencia.append(item.getRutaPdf());
									sbEvidencia.append("\n\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n\n");
								}
							}
							if(item.getMtrDescripcion().length() > 176 && item.getMtrDescripcion().length() <= 220){
								if(stmfPeriodoActivo.getPracDescripcion()!=null){
									sbPeriodo.append(stmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n");
							   }else{
									sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n");
								}
								
								if(item.getMtrCodigo() != null){
									sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
								}else{
									sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
								if(item.getMtrHoras() != null){
									sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n\n");
								}else{
									sbHora.append("");sbHora.append("\n\n\n\n\n\n");
								}
								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n\n");
								}

								if (item.getRutaPdf() != null) {
									sbEvidencia.append(item.getRutaPdf());
									sbEvidencia.append("\n\n\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n\n\n");
								}
							}
							if(item.getMtrDescripcion().length() > 220 && item.getMtrDescripcion().length() <= 264){
								if(stmfPeriodoActivo.getPracDescripcion()!=null){
									sbPeriodo.append(stmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n\n");
							   }else{
									sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n\n");
								}
								
								if(item.getMtrCodigo() != null){
									sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
								}else{
									sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
								if(item.getMtrHoras() != null){
									sbHora.append(item.getMtrHoras());sbHora.append("\n\n\n\n\n\n\n");
								}else{
									sbHora.append("");sbHora.append("\n\n\n\n\n\n\n");
								}
								
								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n\n\n");
								}

								if (item.getRutaPdf() != null) {
									sbEvidencia.append(item.getRutaPdf());
									sbEvidencia.append("\n\n\n\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n\n\n\n");
								}
							}
							frmRrmParametros.put("periodo", sbPeriodo.toString());
							frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
							frmRrmParametros.put("asignatura", sbAsignatura.toString());
							frmRrmParametros.put("numero", sbHora.toString());
							frmRrmParametros.put("causal", sbCausal.toString());
							frmRrmParametros.put("evidencia", sbEvidencia.toString());
							
							if(stmfPersona.getPrsSegundoApellido() != null){
								frmRrmParametros.put("estudiante", stmfPersona.getPrsNombres()+" "+stmfPersona.getPrsPrimerApellido()+" "+stmfPersona.getPrsSegundoApellido());
							}else{
								frmRrmParametros.put("estudiante", stmfPersona.getPrsNombres()+" "+stmfPersona.getPrsPrimerApellido()+" "+stmfPersona.getPrsSegundoApellido());
								
							}
							if(stmfUsuario.getUsrNick()!=null){
								frmRrmParametros.put("nick", stmfUsuario.getUsrNick());
							}else{
								frmRrmParametros.put("nick", " ");
							}
																			
							Boolean encontrado = false;
							Causal objetoCausal= new Causal();
							for (Causal causal : listaCausalaux) {
								if(causal.getCslCodigo().equals(item.getCslCodigo())){
									encontrado = true;
									break;
								}
							}
							
							if(encontrado ==false){
								objetoCausal.setCslCodigo(item.getCslCodigo());
								objetoCausal.setCslDescripcion(item.getCslDescripcion());
								listaCausalaux.add(objetoCausal);
							}
							
						}
						StringBuilder sbCslCodigo= new StringBuilder();
						StringBuilder sbCslDescripcion= new StringBuilder();
						for (Causal causal2 : listaCausalaux) {
						
					    if(causal2.getCslCodigo()!=null){
					    	sbCslCodigo.append(causal2.getCslCodigo());sbCslCodigo.append("\n");	
					    }else{
							sbCslCodigo.append(" ");sbCslCodigo.append("\n");
					    }
						
						if(causal2.getCslDescripcion()!=null){
							sbCslDescripcion.append(causal2.getCslDescripcion());sbCslDescripcion.append("\n");
						}else{
							sbCslDescripcion.append(" ");sbCslDescripcion.append("\n");
							
						}
							frmRrmParametros.put("cslCodigo", sbCslCodigo.toString());
							frmRrmParametros.put("cslDescripcion", sbCslDescripcion.toString());
						}
						
						
						
						StringBuilder pathGeneralReportes = new StringBuilder();
						pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSolicitudTercera");
						frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
						frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
						frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
						
						frmRrmCampos = new ArrayList<Map<String, Object>>();
						Map<String, Object> datoTercera = null;
						datoTercera = new HashMap<String, Object>();
						frmRrmCampos.add(datoTercera);
					
						jasperReport = (JasperReport) JRLoader.loadObject(new File(
								(pathGeneralReportes.toString() + "/solicitud.jasper")));
						jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
						
						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
									
						//lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(stmfUsuario.getUsrPersona().getPrsMailInstitucional());
						//path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo= new StringBuilder();
						formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
						String fechaCorreo = formato.format(new Date());
						String carreraCorreo= null;
						if(crrAux2.getCrrDescripcion()!=null){
							 carreraCorreo=crrAux2.getCrrDescripcion();
						}else{
							 carreraCorreo= " ";
						}
							
						sbCorreo= GeneralesUtilidades.generarAsuntoSolicitudTercera(GeneralesUtilidades.generaStringParaCorreo(fechaCorreo.toString()),
								nombres, GeneralesUtilidades.generaStringParaCorreo(carreraCorreo));
						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_TERCERA_MATRICULA,
											sbCorreo.toString()
											, "admin", "dt1c201s", true, arreglo, "solicitudTerceraMatricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
						String jsonSt = pmail.generarMail();
						Gson json = new Gson();
						MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
						// 	Iniciamos el envío de mensajes
						ObjectMessage message = session.createObjectMessage(mailDto);
						producer.send(message);
						
						// Establecemos en el atributo de la sesión la lista de mapas de
						// datos frmCrpCampos y parámetros frmCrpParametros
						FacesContext context = FacesContext.getCurrentInstance();
						HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
						HttpSession httpSession = request.getSession(false);
						httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
						httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
						httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
						
						ecfActivadorReporte = 1;
						
					} catch (Exception e) {
						e.printStackTrace();
					}
						//******************************************************************************
						//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
						//******************************************************************************
					
					stmfActivaBotonReporte=Boolean.FALSE;
					stmfInactivaOpcionSolicitar=Boolean.TRUE;
					listaSeleccionada = null;
					
				
				}else{
					stmfListMateriaDto = listaSeleccionadaGuarda;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.solicitar.tercera.matricula.validacion.sinExito")));
				
				}
				stmfVerificadorActivaModal = 0;

			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		}else{
			stmfVerificadorActivaModal = 0;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.solicitar.tercera.matricula.validacion.lista.materias.vacia")));
		}
	stmfVerificadorActivaModal = 0;

		} catch (PeriodoAcademicoNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		} catch (PeriodoAcademicoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
	
	
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros
	 */
	public void iniciarParametros() {
		stmfListFichaMatriculaDtoBusq = null;
		stmfCarreraDtoBuscar = new CarreraDto();
		stmfListCarreraDtoBusq = null;
		stmfListMateriaDto = null;
		stmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		stmfNombreArchivoAuxiliar = null;
		stmfNombreArchivoSubido = null;
		stmfListCausal= null;
		stmfInactivaOpcionSolicitar= Boolean.TRUE;
		stmfValidadorSeleccion = GeneralesConstantes.APP_ID_BASE;
		stmfActivaModalCargarSolicitud = 0;
		stmfVerificadorActivaModal = 0;
		stmfActivaBotonReporte=Boolean.TRUE;
		
		stmfDirCarrera= null;
		
	}
	
	//SELECCIONA TODOS LOS ITEMS DE LA LISTA
	/**
	 * Método que realiza la selección del check box de todos 
	 * o de ninguna materia para la solicitud de tercera matricula
	 */
	public void seleccionarTodosAgregarRetiro(){
		if(stmfListMateriaDto!= null && stmfListMateriaDto.size()>0){
			for (MateriaDto item : stmfListMateriaDto) {
				item.setIsSeleccionado(stmfValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
			}
		}
	}
	

	/**
	 * Verifica que el proceso de solicitar tercera matricula exista y se encuentre activo en la fecha actual
	 * @return retora True, si esta el proceso dentro de las fechas y false si no esta dentro de las fechas del cronograma
	 */
	public Boolean verificarCronogramaSolicitarTerceraMatricula() {
		Date fechaActual = new Date();
		Boolean retorno = false;
		// DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		try {
			stmfDependencia = servStmfDependencialServicio.buscarFacultadXcrrId(stmfCarreraDtoBuscar.getCrrId());
		} catch (DependenciaNoEncontradoException e1) {
			retorno = false;
			stmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		if (stmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			stmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			stmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			stmfCronogramaActividad = servStmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(stmfCarreraDtoBuscar.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, stmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_REGISTRO_SOLICITUDES_TERCERA_MATRICULA_VALUE,
							ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			retorno = false;
			stmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();			
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			retorno = false;
			stmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
		if (stmfCronogramaActividad != null) {
			if ((stmfCronogramaActividad.getPlcrFechaInicio() != null) && (stmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(stmfCronogramaActividad.getPlcrFechaInicio(),stmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
					retorno = true;
					stmfInactivaOpcionSolicitar=Boolean.FALSE;
				} else {
					retorno = false;
					stmfInactivaOpcionSolicitar=Boolean.TRUE;
					FacesUtil.limpiarMensaje();
			
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.verificar.cronograma.solicitud.tercera.matricula.validacion.no.habilitado")));
				}
			} else {
				retorno = false;
				stmfInactivaOpcionSolicitar=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
			    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.verificar.cronograma.solicitud.tercera.matricula.validacion.sin.fecha.cronograma")));
				}

		} else {
			retorno = false;
			stmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.verificar.cronograma.solicitud.tercera.matricula.cronograma.no.encontrado.exception")));
		}
		return retorno;
	}

	/**
	 * Método para activar el modal de carga de causal y solicitud
	 * @param materia - materia que se selecciona de la lista para cargar causal y la solicitud
	 */
	public void activaModalCargarSolicitud(MateriaDto materia) {
		stmfActivaModalCargarSolicitud = 1;
		stmfVerificadorActivaModal = 0;
		stmfMateriaSeleccionada = new MateriaDto();
		stmfMateriaSeleccionada = materia;
	}
	
	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event - event archivo oficio que presenta el estudiante para solicitar la autorización de tercera matriucl
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		stmfNombreArchivoSubido = archivo.getFile().getFileName();
		stmfNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir") + File.separator + stmfNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(), rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {

			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.handleFileUpload.carga.archivo.exception")));
		}
	}
			
	/**
	 * Método para verificar y activar el modal de guardar las solicitudes
	 */
	public String verificarClickSolicitarTerceraMatricula(){
		boolean seleccionadosTodos = false;
		boolean seleccionadoCausalTodos = false;
		boolean CargadoDocumentosTodos = false;
		
		
		
		//VERIFICO  QUE EXISTA EL DIRECTORIO PARA GUARDAR EL PDF	
        String pathDirGuardar =GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA;
			
        File directorio = new File(pathDirGuardar);
        boolean existeDirectorio= true;
			if(!directorio.exists()){
				
				existeDirectorio= false;
			}
			
		
			
		// VERIFICA QUE SE HA SELECCIONADO MATERIAS
		for (MateriaDto item : stmfListMateriaDto) {
			if (item.getIsSeleccionado()) {
				seleccionadosTodos = true;
			} else {
				seleccionadosTodos = false;
				break;
			}
		}
		// VERIFICA QUE TODAS LAS MATERIAS TENGAN CAUSALES
		for (MateriaDto item : stmfListMateriaDto) {
			if (item.getCslId() != GeneralesConstantes.APP_ID_BASE) {
				seleccionadoCausalTodos = true;
			} else {
				seleccionadoCausalTodos = false;
				break;
			}
		}
		// VERIFICA QUE SE HA SUBIDO O CARGADO EL ARCHIVO EN TODAS LAS MATERIAS
		
		for (MateriaDto item : stmfListMateriaDto) {
			if (item.getSltrmtDocumentoSolicitud() != null) {
				CargadoDocumentosTodos = true;
			} else {
				CargadoDocumentosTodos = false;
				break;
			}
		}
			
		if(existeDirectorio){
		if (seleccionadosTodos) { // verifica que ha seleccionado materias
			if (seleccionadoCausalTodos) { // verifica causales en todas las materias
				if (CargadoDocumentosTodos) {
					stmfVerificadorActivaModal = 1; //Se activa el modal para guardar las solicitudes
				  } else {
					stmfVerificadorActivaModal = 0;  //Se desactiva el modal para guardar las solicitudes
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.solicitud.en.todos.validacion.exception")));
				  }
			 } else {
				stmfVerificadorActivaModal = 0; //Se desactiva el modal para guardar las solicitudes
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.causal.en.todos.validacion.exception")));

			}
		  } else {
			stmfVerificadorActivaModal = 0;  //Se desactiva el modal para guardar las solicitudes
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.seleccionar.todas.materias.validacion.exception")));

			}
		
	}else{
		stmfVerificadorActivaModal = 0;  //Se desactiva el modal para guardar las solicitudes
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("No existe el directorio para guardar la evidencia para la solicitud de tercera matrícula.");
	
	}
		
	return null;
	}

	
	public void desactivaModalSolicitar(){
		stmfVerificadorActivaModal = 0;
	}
	
	/**
	 * Método para cerrar la ventana de subir archivo
	 */
	
	public void CancelarSubirArchivo(){
		stmfMateriaSeleccionada= null;
		stmfActivaModalCargarSolicitud=0;
			
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	

	public Usuario getStmfUsuario() {
		return stmfUsuario;
	}

	public void setStmfUsuario(Usuario stmfUsuario) {
		this.stmfUsuario = stmfUsuario;
	}

	public CarreraDto getStmfCarreraDtoBuscar() {
		return stmfCarreraDtoBuscar;
	}

	public void setStmfCarreraDtoBuscar(CarreraDto stmfCarreraDtoBuscar) {
		this.stmfCarreraDtoBuscar = stmfCarreraDtoBuscar;
	}

	public List<MateriaDto> getStmfListMateriaDto() {
		stmfListMateriaDto = stmfListMateriaDto==null?(new ArrayList<MateriaDto>()):stmfListMateriaDto;
		return stmfListMateriaDto;
	}

	public void setStmfListMateriaDto(List<MateriaDto> stmfListMateriaDto) {
		this.stmfListMateriaDto = stmfListMateriaDto;
	}

	public List<CarreraDto> getStmfListCarreraDtoBusq() {
		stmfListCarreraDtoBusq = stmfListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):stmfListCarreraDtoBusq;
		return stmfListCarreraDtoBusq;
	}

	public void setStmfListCarreraDtoBusq(List<CarreraDto> stmfListCarreraDtoBusq) {
		this.stmfListCarreraDtoBusq = stmfListCarreraDtoBusq;
	}

	public List<FichaMatriculaDto> getStmfListFichaMatriculaDtoBusq() {
		stmfListFichaMatriculaDtoBusq = stmfListFichaMatriculaDtoBusq==null?(new ArrayList<FichaMatriculaDto>()):stmfListFichaMatriculaDtoBusq;
		return stmfListFichaMatriculaDtoBusq;
	}

	public void setStmfListFichaMatriculaDtoBusq(List<FichaMatriculaDto> stmfListFichaMatriculaDtoBusq) {
		this.stmfListFichaMatriculaDtoBusq = stmfListFichaMatriculaDtoBusq;
	}


	public String getStmfNombreArchivoAuxiliar() {
		return stmfNombreArchivoAuxiliar;
	}


	public void setStmfNombreArchivoAuxiliar(String stmfNombreArchivoAuxiliar) {
		this.stmfNombreArchivoAuxiliar = stmfNombreArchivoAuxiliar;
	}


	public String getStmfNombreArchivoSubido() {
		return stmfNombreArchivoSubido;
	}


	public void setStmfNombreArchivoSubido(String stmfNombreArchivoSubido) {
		this.stmfNombreArchivoSubido = stmfNombreArchivoSubido;
	}


	public List<Causal> getStmfListCausal() {
		return stmfListCausal;
	}


	public void setStmfListCausal(List<Causal> stmfListCausal) {
		this.stmfListCausal = stmfListCausal;
	}


	public Integer getStmfTipoCronograma() {
		return stmfTipoCronograma;
	}


	public Dependencia getStmfDependencia() {
		return stmfDependencia;
	}


	public void setStmfDependencia(Dependencia stmfDependencia) {
		this.stmfDependencia = stmfDependencia;
	}


	public CronogramaActividadJdbcDto getStmfCronogramaActividad() {
		return stmfCronogramaActividad;
	}


	public void setStmfCronogramaActividad(CronogramaActividadJdbcDto stmfCronogramaActividad) {
		this.stmfCronogramaActividad = stmfCronogramaActividad;
	}


	public void setStmfTipoCronograma(Integer stmfTipoCronograma) {
		this.stmfTipoCronograma = stmfTipoCronograma;
	}


	public Integer getStmfValidadorSeleccion() {
		return stmfValidadorSeleccion;
	}


	public void setStmfValidadorSeleccion(Integer stmfValidadorSeleccion) {
		this.stmfValidadorSeleccion = stmfValidadorSeleccion;
	}


	public Integer getStmfVerificadorActivaModal() {
		return stmfVerificadorActivaModal;
	}


	public void setStmfVerificadorActivaModal(Integer stmfVerificadorActivaModal) {
		this.stmfVerificadorActivaModal = stmfVerificadorActivaModal;
	}


	public MateriaDto getStmfMateriaSeleccionada() {
		return stmfMateriaSeleccionada;
	}


	public void setStmfMateriaSeleccionada(MateriaDto stmfMateriaSeleccionada) {
		this.stmfMateriaSeleccionada = stmfMateriaSeleccionada;
	}


	public Integer getStmfActivaModalCargarSolicitud() {
		return stmfActivaModalCargarSolicitud;
	}


	public void setStmfActivaModalCargarSolicitud(Integer stmfActivaModalCargarSolicitud) {
		this.stmfActivaModalCargarSolicitud = stmfActivaModalCargarSolicitud;
	}


	public PeriodoAcademico getStmfPeriodoActivo() {
		return stmfPeriodoActivo;
	}


	public void setStmfPeriodoActivo(PeriodoAcademico stmfPeriodoActivo) {
		this.stmfPeriodoActivo = stmfPeriodoActivo;
	}


	public Boolean getStmfInactivaOpcionSolicitar() {
		return stmfInactivaOpcionSolicitar;
	}


	public void setStmfInactivaOpcionSolicitar(Boolean stmfInactivaOpcionSolicitar) {
		this.stmfInactivaOpcionSolicitar = stmfInactivaOpcionSolicitar;
	}


	public List<FichaInscripcionDto> getStmfListaFichaInscripcionSau() {
		stmfListaFichaInscripcionSau = stmfListaFichaInscripcionSau==null?(new ArrayList<FichaInscripcionDto>()):stmfListaFichaInscripcionSau;
		return stmfListaFichaInscripcionSau;
	}


	public void setStmfListaFichaInscripcionSau(List<FichaInscripcionDto> stmfListaFichaInscripcionSau) {
		this.stmfListaFichaInscripcionSau = stmfListaFichaInscripcionSau;
	}


	public List<RecordEstudianteSAUDto> getStmfListaRecordEstudianteSau() {
		stmfListaRecordEstudianteSau = stmfListaRecordEstudianteSau==null?(new ArrayList<RecordEstudianteSAUDto>()):stmfListaRecordEstudianteSau;
		return stmfListaRecordEstudianteSau;
	}


	public void setStmfListaRecordEstudianteSau(List<RecordEstudianteSAUDto> stmfListaRecordEstudianteSau) {
		this.stmfListaRecordEstudianteSau = stmfListaRecordEstudianteSau;
	}


	public List<MateriaDto> getStmfLista() {
		return stmfLista;
	}


	public void setStmfLista(List<MateriaDto> stmfLista) {
		this.stmfLista = stmfLista;
	}


	public int getEcfActivadorReporte() {
		return ecfActivadorReporte;
	}


	public void setEcfActivadorReporte(int ecfActivadorReporte) {
		this.ecfActivadorReporte = ecfActivadorReporte;
	}


	public Persona getStmfPersona() {
		return stmfPersona;
	}


	public void setStmfPersona(Persona stmfPersona) {
		this.stmfPersona = stmfPersona;
	}


	public Boolean getStmfActivaBotonReporte() {
		return stmfActivaBotonReporte;
	}


	public void setStmfActivaBotonReporte(Boolean stmfActivaBotonReporte) {
		this.stmfActivaBotonReporte = stmfActivaBotonReporte;
	}


	public PersonaDto getStmfDirCarrera() {
		return stmfDirCarrera;
	}


	public void setStmfDirCarrera(PersonaDto stmfDirCarrera) {
		this.stmfDirCarrera = stmfDirCarrera;
	}


	
	
	
	
	
	

	
	
	
}
