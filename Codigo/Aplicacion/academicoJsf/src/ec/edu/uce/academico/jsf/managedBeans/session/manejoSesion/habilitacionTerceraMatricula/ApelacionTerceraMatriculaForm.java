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
   
 ARCHIVO:    ApelacionTerceraMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la apelación del estudiante para habilitar la tercera matricula. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
16-02-2018			 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.habilitacionTerceraMatricula;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoCausalConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteApelacionTerceraForm;
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

@ManagedBean(name = "apelacionTerceraMatriculaForm")
@SessionScoped
public class ApelacionTerceraMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = 9210281719911332407L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario atmfUsuario;
    //LISTAR MATERIAS REPETIDAS CON SEGUNDA MATRIUCLA
	private List<FichaMatriculaDto> atmfListFichaMatriculaDtoBusq;
	private CarreraDto atmfCarreraDtoBuscar;
	private List<CarreraDto> atmfListCarreraDtoBusq;
	private List<MateriaDto> atmfListMateriaDto;
	//MODAL SUBIR ARCHIVO
	private List<Causal> atmfListCausal;
	private MateriaDto atmfMateriaSeleccionada;//Se usa para ingresar el causal y e archivo en el modal SubirArchivo
	private String atmfNombreArchivoAuxiliar;
	private String atmfNombreArchivoSubido;
	//VERIFICAR CRONOGRAMA
	private Integer atmfTipoCronograma;
	private Dependencia atmfDependencia;
	private CronogramaActividadJdbcDto atmfCronogramaActividad;
	//PARA VISUALIZACIÓN
	private Boolean atmfInactivaOpcionSolicitar;
	private int ecfActivadorReporte;
	private Boolean atmfActivaBotonReporte;
		
	//VALIDACIONES EN GUARDADOS
	private Integer atmfValidadorSeleccion;
	private Integer atmfVerificadorActivaModal; // activa modal guardar
	private Integer atmfActivaModalCargarSolicitud; // activa modal cargar causal y solicitud
	private PeriodoAcademico atmfPeriodoActivo; 
	
	//SAU
	private List<FichaInscripcionDto> atmfListaFichaInscripcionSau;
	
	//REPORTE
	
	private PersonaDto atmfDecanoFcl ;
	private Persona atmfPersona;
	private List<MateriaDto> atmfLista;
	
	
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
	MateriaDtoServicioJdbc servAtmfMateriaDtoServicio;
	@EJB
	FichaMatriculaDtoServicioJdbc servAtmfFichaMatriculaDtoServicioJdbc;
	@EJB 
	CausalServicio servAtmfCausalServicio;
	@EJB 
	DependenciaServicio servAtmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servAtmfCronogramaActividadDtoServicioJdbc;
	@EJB
	PeriodoAcademicoServicio servAtmfPeriodoAcademicoServicio;
	@EJB
	SolicitudTerceraMatriculaServicio servAtmfSolicitudTerceraMatriculaServicio;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servAtmfSolicitudTerceraMatriculaDtoServicioJdbc;
	//Servicios para Estudiantes SAU
	@EJB
	FichaInscripcionDtoServicioJdbc  servStmfFichaInscripcionServicio;
	//Reporte
	@EJB	
	PersonaDtoServicioJdbc servStmfPersonaRol;
	@EJB
	PersonaServicio servStmfPersonaServicio;
	@EJB
	CarreraServicio servStmfCarrera;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/

	/**
	 * Dirige la navegación hacia la página de listar de materias que el estudiante debe solicitar  autorización de tercera matricula
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar materias
	 */
	public String irVerApelacionListaMaterias(Usuario usuario) {
		atmfUsuario = usuario;
		String retorno = null;
	try {
//			//INICIO PARAMETROS
			iniciarParametros();
//			//BUSCA LA LISTA DE MATRÍCULAS DEL ESTUDIANTE
			//atmfListFichaMatriculaDtoBusq= servAtmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, atmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			atmfListFichaMatriculaDtoBusq= servAtmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraParaTercerasMatriculas(GeneralesConstantes.APP_ID_BASE, atmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			
			getAtmfListCarreraDtoBusq();
//			//OBTENER LA LISTA DE CARRERAS QUE LOS ESTUDIANTES ESTAN MATRICULADOS
			
			if(atmfListFichaMatriculaDtoBusq!=null){
			for (FichaMatriculaDto itemMatricula : atmfListFichaMatriculaDtoBusq) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
				Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
				for (CarreraDto itemCarrera : atmfListCarreraDtoBusq) { // recorro la lista de carreras a las que el estudiante esta matriculado 
					if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
						encontro = true; // asigno el booleano a verdado
					}
				} //fin recorrido de lista auxiliar de carreras
				if(encontro == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
					CarreraDto carreraAgregar = new CarreraDto();
					carreraAgregar.setCrrId(itemMatricula.getCrrId());
					carreraAgregar.setCrrDetalle(itemMatricula.getCrrDetalle());
					carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
					atmfListCarreraDtoBusq.add(carreraAgregar);
				}
			}
			
			}
			
			
			//ESTUDIANTE SAU**********************************
			// BUSCO CARRERAS POR LA FICHA INSCRIPCION
		//	if((atmfListCarreraDtoBusq==null)||(atmfListCarreraDtoBusq.size()==0)){
				
					atmfListaFichaInscripcionSau=servStmfFichaInscripcionServicio.listarFcinXIdentificacionXEstado(atmfUsuario.getUsrPersona().getPrsIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
				
				if(atmfListaFichaInscripcionSau!=null){
				for (FichaInscripcionDto itemInscripcion : atmfListaFichaInscripcionSau) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
					Boolean encontro2 = false; // boolenado para determinar si le encontro en la lista 
					for (CarreraDto itemCarrera : atmfListCarreraDtoBusq) { // recorro la lista de carreras a las que el estudiante esta matriculado 
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
						atmfListCarreraDtoBusq.add(carreraAgregar);
					}
				}
			}
				
				
				//FIN  BUSCAR CARRERAS ESTUDIANTE SAU
	//		}
			
			atmfPersona=servStmfPersonaServicio.buscarPorId(atmfUsuario.getUsrPersona().getPrsId());
			
			//LISTA DE CAUSALES PARA TERCERA MATRICULA
			atmfListCausal=servAtmfCausalServicio.listarxTipo(TipoCausalConstantes.TIPO_CAUSAL_TERCERA_MATRICULA_VALUE);
			retorno = "irVerApelacionListaMaterias";
		} catch (FichaMatriculaException e) {
		  FacesUtil.limpiarMensaje();
	      FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.irVerListaMaterias.ficha.matricula.exception")));
		} catch (CausalNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.irVerListaMaterias.causal.no.encontrado.exception")));
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.irVerListaMaterias.lista.ficha.inscripcion.exception")));
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.irVerListaMaterias.lista.ficha.inscripcion.no.encontrado.exception")));
		} 
		return retorno;
	}

	
	/**
	 * Método que busca las materias repetidas con segunda matrícula
	 */
	public void buscarMaterias(){
		
		List<MateriaDto>  listaMateriasAux= new ArrayList<>();
		atmfListMateriaDto=new ArrayList<>();
		atmfActivaModalCargarSolicitud = 0;
		ecfActivadorReporte = 0;
			try{
				//Verifico que se haya seleccionado una carrera
				if(atmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
				
					  //Busco Dependencia
						atmfDependencia = servAtmfDependencialServicio.buscarFacultadXcrrId(atmfCarreraDtoBuscar.getCrrId());
					
				     	atmfDecanoFcl=servStmfPersonaRol.buscarPersonaXRolIdXFclId(RolConstantes.ROL_DECANO_VALUE, atmfDependencia.getDpnId());
				    	
//				     	boolean cronogramaAbierto = false;
//						if(atmfCarreraDtoBuscar.getCrrId()==82 ||atmfCarreraDtoBuscar.getCrrId()==157){ //cronograma abierto para Medicina y medicna rediseño
//							cronogramaAbierto = true;
//							
//						}else{
//							
//							cronogramaAbierto =verificarCronogramaSolicitarTerceraMatricula(); //otras carreras se rigen al cronograma
//							
//						}
				     	
				     	//Verifico que este habilitado el cronograma de tercera matricula
				    	if(verificarCronogramaSolicitarTerceraMatricula()){
				 
				//		if(cronogramaAbierto){
						
			          //BUSCO MATERIAS CON SOLCITUD NEGADA.
                     listaMateriasAux=servAtmfMateriaDtoServicio.ListarMateriasXPrsIdXCrrIdXRcesEstadoSolicitudSIIUSAU(atmfUsuario.getUsrPersona().getPrsId(), atmfCarreraDtoBuscar.getCrrId(), SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE);

				    	//Verifico si de la lista anterior no se ha realizado la apelacion.	
				    		for (MateriaDto Materia : listaMateriasAux) {
								if(!servAtmfSolicitudTerceraMatriculaServicio.buscarPorSltrmtId(Materia.getSltrmtId())){
									atmfListMateriaDto.add(Materia);
								}				
							}
				    		
				    		//Verificamos si la lista esta vacia para bloquear la solicitud.
				    		
				    		if(atmfListMateriaDto.size()<=0)	{
				    	     	atmfInactivaOpcionSolicitar=Boolean.TRUE;
				    	    }else{
				    	    	atmfInactivaOpcionSolicitar=Boolean.FALSE;
				    	    }
				    	} 
				}else{
					FacesUtil.limpiarMensaje();
		            FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.buscar.materias.validacion.carrera.exception")));

				}
	    } catch (MateriaDtoException e) {
		FacesUtil.limpiarMensaje();
	    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.buscar.materias.exception")));
        } catch (PersonaDtoException e) {
        	FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			
		    FacesUtil.mensajeError("Apelacion.tercera.matricula.buscar.materias.decano.no.encontrado.exception");
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError("Apelacion.tercera.matricula.buscar.materias.facultad.no.encontrado.exception");
		}
				
	}
	
	
	/**
	 * Método para guardar el archivo y la causal en el item
	 * @param materia -materia que se selecciona de la lista para cargar causal y la solicitud
	 */

	public void guardarSubirArchivo(MateriaDto materia) {
		Causal auxCausal = new Causal();
		auxCausal= null;
		
		if (materia.getCslId() != GeneralesConstantes.APP_ID_BASE) { //Verifica que se ha seleccionado una causal
			if (atmfNombreArchivoSubido != null) { //verifico que se haya cargado un archivo
					try {
						//Busco el causal
						auxCausal = servAtmfCausalServicio.buscarPorId(materia.getCslId());
					} catch (CausalNoEncontradoException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("mensaje", "No se encontró el causal seleccionado por Id");// Mensaje en ventana modal
					} catch (CausalException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("mensaje", "Error desconocido al buscar por Id");// Mensaje en ventana modal
					} 

				if (auxCausal != null) {
					// Buscamos la materia en la lista de materias para guardar los valores
					for (MateriaDto itemMtr : atmfListMateriaDto) {
						if (itemMtr.getMtrId() == materia.getMtrId()) {
							itemMtr.setCslDescripcion(auxCausal.getCslDescripcion());
							itemMtr.setCslEstado(auxCausal.getCslEstado());
							itemMtr.setCslCodigo(auxCausal.getCslCodigo());
							// Guardamos el nombre del archivo en el objeto
							itemMtr.setSltrmtDocumentoSolicitud(atmfNombreArchivoSubido);
							break;
						}
					}
				}
				atmfActivaModalCargarSolicitud = 0;
				atmfNombreArchivoAuxiliar = null;
				atmfNombreArchivoSubido = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("mensaje", "Documento evidencia cargado con éxito");// Mensaje en ventana modal
			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje", "Debe seleccionar el documento evidencia");// Mensaje en ventana modal
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje", "Debe seleccionar un causal");// Mensaje en ventana modal
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
		atmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		atmfListMateriaDto=null;
		atmfInactivaOpcionSolicitar=Boolean.TRUE;
	}
	
	/**
	 * Método que guarda la solicitud de tercera matricula en la BDD
	 * @return retorna - la navegación de la página listar matriculas
	 */
	public void apelarSolicitudTerceraMatricula(){
		atmfPeriodoActivo= new PeriodoAcademico();
		
		Carrera crrAux2= new Carrera();
		
		
		
	//VERIFICO QUE EXISTAN MATERIAS PARA RETIRO DE LA MATRICULA 
		if(atmfListMateriaDto!=null && atmfListMateriaDto.size()>0){
			try {
				
				
				//Busco el periodo academico activo
				atmfPeriodoActivo=servAtmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				
			//ASIGNO LAS MATERIAS SELECCIONADAS A OTRA LISTA PARA GUARDAR
				List<MateriaDto> listaSeleccionada = new ArrayList<>();
				for (MateriaDto item : atmfListMateriaDto) {
					if(item.getIsSeleccionado()){
						listaSeleccionada.add(item);
					}
				}
				//CAMBIO DE ESTADO A SOLICITUD DE RETIRO EN DETALLE MATRICULA CON LA LISTA DE MATERIAS
				List<MateriaDto> listaSeleccionadaGuarda = new ArrayList<>();
				List<MateriaDto> listaSeleccionadaNoGuarda = new ArrayList<>();
				String rutaNombre = null;
				String rutaTemporal = null;
				if(servAtmfSolicitudTerceraMatriculaServicio.generarApelacionTerceraMatricula(listaSeleccionada, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE, atmfPeriodoActivo, atmfUsuario)){
				//PARA CARGA DE ARCHIVO EN EL SERVIDOR
				for (MateriaDto item : listaSeleccionada) {
							if(item.getIsSeleccionado()){
								String extension = GeneralesUtilidades.obtenerExtension(item.getSltrmtDocumentoSolicitud());
							                                                                                              
								rutaNombre =SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_LABEL+"-"+atmfPeriodoActivo.getPracId()+"-"+item.getMtrId()+"-"+atmfUsuario.getUsrNick()+ "." + extension;
								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + item.getSltrmtDocumentoSolicitud();
								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_APELACION_TERCERA_MATRICULA+ rutaNombre);
								listaSeleccionadaGuarda.add(item);
							}else{
								listaSeleccionadaNoGuarda.add(item);
							}
							
							item.setRutaPdf(rutaNombre);
							
							rutaNombre = null;
							rutaTemporal = null;
							
						}
				
				
				crrAux2 = servStmfCarrera.buscarPorId(atmfCarreraDtoBuscar.getCrrId());
				
				 
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
					frmRrmNombreReporte = "apelacionTerceraMatricula";
					frmRrmParametros = new HashMap<String, Object>();
					SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
					//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
					String fecha = formato.format(new Date());
					frmRrmParametros.put("fecha",fecha);
					//frmRrmParametros.put("facultad", stmfDependencia.getDpnDescripcion());
				
//					if(crrAux2.getCrrDescripcion()!=null){
//					frmRrmParametros.put("carrera", crrAux2.getCrrDescripcion());
//					}else{
//						frmRrmParametros.put("carrera", " ");	
//					}
					
					String nombres = null;
					if(atmfPersona!=null){
					 nombres=atmfPersona.getPrsNombres()+" "+atmfPersona.getPrsPrimerApellido().toUpperCase()+" "
							+(atmfPersona.getPrsSegundoApellido() == null?" ":atmfPersona.getPrsSegundoApellido());
					}else{
						nombres= " ";
						
					}
				//	frmRrmParametros.put("nombre",nombres );
					
//					if(stmfPersona.getPrsIdentificacion()!=null){
//					frmRrmParametros.put("identificacion", stmfPersona.getPrsIdentificacion());
//					}else{
//						frmRrmParametros.put("identificacion"," ");
//					}
					
					String decanofcl= null;
					
					if(atmfDecanoFcl!=null){
						decanofcl = atmfDecanoFcl.getPrsNombres()+" "+atmfDecanoFcl.getPrsPrimerApellido().toUpperCase()+" "
							+(atmfDecanoFcl.getPrsSegundoApellido() == null?" ":atmfDecanoFcl.getPrsSegundoApellido());
					}else{
						decanofcl=" ";
						
					}
					
					StringBuilder sbTextoInicial = new StringBuilder();
					sbTextoInicial.append("Señor(a)");sbTextoInicial.append("\n");
					sbTextoInicial.append(decanofcl);sbTextoInicial.append("\n");
					sbTextoInicial.append("DECANO(A) DE LA FACULTAD DE ");
					if(atmfDependencia.getDpnDescripcion()!=null){
						sbTextoInicial.append(atmfDependencia.getDpnDescripcion());sbTextoInicial.append("\n");
					}else{
						sbTextoInicial.append(" ");sbTextoInicial.append("\n");
					}
					sbTextoInicial.append("Presente.- ");sbTextoInicial.append("\n\n");
					sbTextoInicial.append("De mis consideraciones:");sbTextoInicial.append("\n");
					
					frmRrmParametros.put("textoInicial", sbTextoInicial.toString());
					
					
					
					StringBuilder sbTexto = new StringBuilder();
					sbTexto.append("Yo, "); 
					sbTexto.append(nombres);
					sbTexto.append(" con identificación No. ");
					sbTexto.append(atmfPersona.getPrsIdentificacion());
					sbTexto.append(" estudiante de la Carrera de ");
					if(crrAux2.getCrrDescripcion()!=null){
						sbTexto.append(crrAux2.getCrrDescripcion());
						}else{
							sbTexto.append(" ");
						}
					sbTexto.append(" de la Facultad de ");
					if(atmfDependencia.getDpnDescripcion()!=null){
						sbTexto.append(atmfDependencia.getDpnDescripcion());
						}else{
							sbTexto.append(" ");
						}
				
					sbTexto.append(", solicito a usted muy comedidamente me conceda la autorización respectiva para realizar la solicitud de apelación de Tercera Matrícula en la(s) siguiente(s) asignatura(s): ");
					
					frmRrmParametros.put("texto", sbTexto.toString());
					StringBuilder sbPeriodo = new StringBuilder();
					StringBuilder sbCodigo = new StringBuilder();
					StringBuilder sbAsignatura = new StringBuilder();
					StringBuilder sbHora = new StringBuilder();
					StringBuilder sbCausal = new StringBuilder();
					StringBuilder sbEvidencia = new StringBuilder();
					
					for (MateriaDto item : listaSeleccionada) {
						if(item.getMtrDescripcion().length() <= 44){
							if(atmfPeriodoActivo.getPracDescripcion()!=null){
								sbPeriodo.append(atmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n");
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
							if(atmfPeriodoActivo.getPracDescripcion()!=null){
								sbPeriodo.append(atmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n");
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
							if(atmfPeriodoActivo.getPracDescripcion()!=null){
								sbPeriodo.append(atmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n");
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
							if(atmfPeriodoActivo.getPracDescripcion()!=null){
								sbPeriodo.append(atmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n");
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
							if(atmfPeriodoActivo.getPracDescripcion()!=null){
								sbPeriodo.append(atmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n");
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
							if(atmfPeriodoActivo.getPracDescripcion()!=null){
								sbPeriodo.append(atmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n\n");
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
						//DATOS DEL ESTUDIANTE
						if(atmfPersona.getPrsSegundoApellido() != null){
							frmRrmParametros.put("estudiante", atmfPersona.getPrsNombres()+" "+atmfPersona.getPrsPrimerApellido()+" "+atmfPersona.getPrsSegundoApellido());
						}else{
							frmRrmParametros.put("estudiante", atmfPersona.getPrsNombres()+" "+atmfPersona.getPrsPrimerApellido()+" "+atmfPersona.getPrsSegundoApellido());
							
						}
						if(atmfUsuario.getUsrNick()!=null){
							frmRrmParametros.put("nick", atmfUsuario.getUsrNick());
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
					pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteApelacionTercera");
					frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
					frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
					frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
					
					frmRrmCampos = new ArrayList<Map<String, Object>>();
					Map<String, Object> datoTercera = null;
					datoTercera = new HashMap<String, Object>();
					frmRrmCampos.add(datoTercera);
				
					jasperReport = (JasperReport) JRLoader.loadObject(new File(
							(pathGeneralReportes.toString() + "/apelacion.jasper")));
					jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
					
					byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
								
					//lista de correos a los que se enviara el mail
					List<String> correosTo = new ArrayList<>();
					correosTo.add(atmfUsuario.getUsrPersona().getPrsMailInstitucional());
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
						
					sbCorreo= GeneralesUtilidades.generarAsuntoApelacionTercera(GeneralesUtilidades.generaStringParaCorreo(fechaCorreo.toString()),
							nombres, GeneralesUtilidades.generaStringParaCorreo(carreraCorreo));
					pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_APELACION_TERCERA_MATRICULA,
										sbCorreo.toString()
										, "admin", "dt1c201s", true, arreglo, "apelacionTerceraMatricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
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
				
				atmfActivaBotonReporte=Boolean.FALSE;
				atmfInactivaOpcionSolicitar=Boolean.TRUE;
				
					//BUSCO LAS MATERIAS MATRICULADAS QUE TIENE EL ESTUDIANTE
					atmfListMateriaDto = listaSeleccionadaNoGuarda;
									
					//Llamar reporte
					atmfLista = listaSeleccionada;
					
					
					listaSeleccionada = null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.validacion.exito")));
					
				}else{
					atmfListMateriaDto = listaSeleccionadaGuarda;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.validacion.sinExito")));
				
				}
				atmfVerificadorActivaModal = 0;

			} catch (PeriodoAcademicoNoEncontradoException e1) {
	          	FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.periodoAcademico.no.encontrado.exception")));
			} catch (PeriodoAcademicoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.periodoAcademico.exception")));
			} catch (FileNotFoundException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.archivo.no.encontrado.exception")));
			} catch (SolicitudTerceraMatriculaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.guardar.exception")));
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.carrera.no.encontrado.exception")));
			} catch (CarreraException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} 
		}else{
			atmfVerificadorActivaModal = 0;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.solicitar.tercera.matricula.validacion.lista.materias.vacia")));
		}
	atmfVerificadorActivaModal = 0;

	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros
	 */
	public void iniciarParametros() {
		atmfListFichaMatriculaDtoBusq = null;
		atmfCarreraDtoBuscar = new CarreraDto();
		atmfListCarreraDtoBusq = null;
		atmfListMateriaDto = null;
		atmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		atmfNombreArchivoAuxiliar = null;
		atmfNombreArchivoSubido = null;
		atmfListCausal= null;
		atmfInactivaOpcionSolicitar= Boolean.TRUE;
		atmfValidadorSeleccion = GeneralesConstantes.APP_ID_BASE;
		atmfActivaModalCargarSolicitud = 0;
		atmfVerificadorActivaModal = 0;
		atmfActivaBotonReporte=Boolean.TRUE;
		ecfActivadorReporte = 0;
		atmfPersona= new Persona();
		atmfDecanoFcl= new PersonaDto();
		atmfLista= new ArrayList<>();
	}
	
	//SELECCIONA TODOS LOS ITEMS DE LA LISTA
	/**
	 * Método que realiza la selección del check box de todos 
	 * o de ninguna materia para la solicitud de tercera matricula
	 */
	public void seleccionarTodosAgregarRetiro(){
		if(atmfListMateriaDto!= null && atmfListMateriaDto.size()>0){
			for (MateriaDto item : atmfListMateriaDto) {
				item.setIsSeleccionado(atmfValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
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
		
		if (atmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			atmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			atmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			atmfCronogramaActividad = servAtmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(atmfCarreraDtoBuscar.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, atmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_REGISTRO_APELACIONES_TERCERA_MATRICULA_VALUE,
							ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			retorno = false;
			atmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();			
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			retorno = false;
			atmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
		if (atmfCronogramaActividad != null) {
			if ((atmfCronogramaActividad.getPlcrFechaInicio() != null) && (atmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(atmfCronogramaActividad.getPlcrFechaInicio(),atmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
					retorno = true;
					atmfInactivaOpcionSolicitar=Boolean.FALSE;
				} else {
					retorno = false;
					atmfInactivaOpcionSolicitar=Boolean.TRUE;
					FacesUtil.limpiarMensaje();
			
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.verificar.cronograma.apelacion.tercera.matricula.validacion.no.habilitado")));
				}
			} else {
				retorno = false;
				atmfInactivaOpcionSolicitar=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
			    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.verificar.cronograma.apelacion.tercera.matricula.validacion.sin.fecha.cronograma")));
				}

		} else {
			retorno = false;
			atmfInactivaOpcionSolicitar=Boolean.TRUE;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.verificar.cronograma.apelacion.tercera.matricula.cronograma.no.encontrado.exception")));
		}
		return retorno;
	}

	/**
	 * Método para activar el modal de carga de causal y solicitud
	 * @param materia - materia que se selecciona de la lista para cargar causal y la solicitud
	 */
	public void activaModalCargarSolicitud(MateriaDto materia) {
		atmfActivaModalCargarSolicitud = 1;
		atmfVerificadorActivaModal = 0;
		atmfMateriaSeleccionada = new MateriaDto();
		atmfMateriaSeleccionada = materia;
	}
	
	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event - event archivo oficio que presenta el estudiante para solicitar la autorización de tercera matriucl
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		atmfNombreArchivoSubido = archivo.getFile().getFileName();
		atmfNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir") + File.separator + atmfNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(), rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {

			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.handleFileUpload.carga.archivo.exception")));
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
        String pathDirGuardar =GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_APELACION_TERCERA_MATRICULA;
			
        File directorio = new File(pathDirGuardar);
        boolean existeDirectorio= true;
			if(!directorio.exists()){
				
				existeDirectorio= false;
			}
			
		
		
		// VERIFICA QUE SE HA SELECCIONADO MATERIAS
		for (MateriaDto item : atmfListMateriaDto) {
			if (item.getIsSeleccionado()) {
				seleccionadosTodos = true;
			} else {
				seleccionadosTodos = false;
				break;
			}
		}
		// VERIFICA QUE TODAS LAS MATERIAS TENGAN CAUSALES
		for (MateriaDto item : atmfListMateriaDto) {
			if (item.getCslId() != GeneralesConstantes.APP_ID_BASE) {
				seleccionadoCausalTodos = true;
			} else {
				seleccionadoCausalTodos = false;
				break;
			}
		}
		// VERIFICA QUE SE HA SUBIDO O CARGADO EL ARCHIVO EN TODAS LAS MATERIAS
		
		for (MateriaDto item : atmfListMateriaDto) {
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
					atmfVerificadorActivaModal = 1; //Se activa el modal para guardar las solicitudes
				} else {
					atmfVerificadorActivaModal = 0;  //Se desactiva el modal para guardar las solicitudes
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.apelacion.en.todos.validacion.exception")));

				}
			} else {
				atmfVerificadorActivaModal = 0; //Se desactiva el modal para guardar las solicitudes
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.causal.en.todos.validacion.exception")));

			}
		} else {
			atmfVerificadorActivaModal = 0;  //Se desactiva el modal para guardar las solicitudes
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.seleccionar.todas.materias.validacion.exception")));

			}
		
	}else{
		atmfVerificadorActivaModal = 0;
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("No existe el directorio para guardar la evidencia para la solicitud de apelación de tercera matrícula.");
	
	}
	return null;
	}

	
	/**
	 * Método para cerrar la ventana de subir archivo
	 */
	
	public void CancelarSubirArchivo(){
		atmfMateriaSeleccionada= null;
		atmfActivaModalCargarSolicitud=0;
			
	}

	
	/*
	 * llama a generar el reporte PDF
	 */
	public void llamarReporte(){
		atmfActivaModalCargarSolicitud = 0;
		Carrera crrAux= new Carrera();
		
		try {
			   crrAux = servStmfCarrera.buscarPorId(atmfCarreraDtoBuscar.getCrrId());
			} catch (CarreraNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			} catch (CarreraException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			}
		if((atmfLista!=null)&&(atmfLista.size()>0)){
		
		ReporteApelacionTerceraForm.generarReporteApelacionTercera(atmfLista, atmfDependencia, crrAux, atmfPersona, atmfPeriodoActivo, atmfUsuario, atmfDecanoFcl);
		ecfActivadorReporte = 1;
		
		}else{
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Apelacion.tercera.matricula.llamar.reporte.validacion.lista.vacia.exception")));
		}
	}
	
	public void desactivaModalSolicitar(){
		atmfVerificadorActivaModal = 0;
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	

	public Usuario getAtmfUsuario() {
		return atmfUsuario;
	}

	public void setAtmfUsuario(Usuario atmfUsuario) {
		this.atmfUsuario = atmfUsuario;
	}

	public CarreraDto getAtmfCarreraDtoBuscar() {
		return atmfCarreraDtoBuscar;
	}

	public void setAtmfCarreraDtoBuscar(CarreraDto atmfCarreraDtoBuscar) {
		this.atmfCarreraDtoBuscar = atmfCarreraDtoBuscar;
	}

	public List<MateriaDto> getAtmfListMateriaDto() {
		atmfListMateriaDto = atmfListMateriaDto==null?(new ArrayList<MateriaDto>()):atmfListMateriaDto;
		return atmfListMateriaDto;
	}

	public void setAtmfListMateriaDto(List<MateriaDto> atmfListMateriaDto) {
		this.atmfListMateriaDto = atmfListMateriaDto;
	}

	public List<CarreraDto> getAtmfListCarreraDtoBusq() {
		atmfListCarreraDtoBusq = atmfListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):atmfListCarreraDtoBusq;
		return atmfListCarreraDtoBusq;
	}

	public void setAtmfListCarreraDtoBusq(List<CarreraDto> atmfListCarreraDtoBusq) {
		this.atmfListCarreraDtoBusq = atmfListCarreraDtoBusq;
	}

	public List<FichaMatriculaDto> getAtmfListFichaMatriculaDtoBusq() {
		atmfListFichaMatriculaDtoBusq = atmfListFichaMatriculaDtoBusq==null?(new ArrayList<FichaMatriculaDto>()):atmfListFichaMatriculaDtoBusq;
		return atmfListFichaMatriculaDtoBusq;
	}

	public void setAtmfListFichaMatriculaDtoBusq(List<FichaMatriculaDto> atmfListFichaMatriculaDtoBusq) {
		this.atmfListFichaMatriculaDtoBusq = atmfListFichaMatriculaDtoBusq;
	}


	public String getAtmfNombreArchivoAuxiliar() {
		return atmfNombreArchivoAuxiliar;
	}


	public void setAtmfNombreArchivoAuxiliar(String atmfNombreArchivoAuxiliar) {
		this.atmfNombreArchivoAuxiliar = atmfNombreArchivoAuxiliar;
	}


	public String getAtmfNombreArchivoSubido() {
		return atmfNombreArchivoSubido;
	}


	public void setAtmfNombreArchivoSubido(String atmfNombreArchivoSubido) {
		this.atmfNombreArchivoSubido = atmfNombreArchivoSubido;
	}


	public List<Causal> getAtmfListCausal() {
		atmfListCausal = atmfListCausal==null?(new ArrayList<Causal>()):atmfListCausal;
		return atmfListCausal;
	}


	public void setAtmfListCausal(List<Causal> atmfListCausal) {
		this.atmfListCausal = atmfListCausal;
	}


	public Integer getAtmfTipoCronograma() {
		return atmfTipoCronograma;
	}


	public Dependencia getAtmfDependencia() {
		return atmfDependencia;
	}


	public void setAtmfDependencia(Dependencia atmfDependencia) {
		this.atmfDependencia = atmfDependencia;
	}


	public CronogramaActividadJdbcDto getAtmfCronogramaActividad() {
		return atmfCronogramaActividad;
	}


	public void setAtmfCronogramaActividad(CronogramaActividadJdbcDto atmfCronogramaActividad) {
		this.atmfCronogramaActividad = atmfCronogramaActividad;
	}


	public void setAtmfTipoCronograma(Integer atmfTipoCronograma) {
		this.atmfTipoCronograma = atmfTipoCronograma;
	}


	public Integer getAtmfValidadorSeleccion() {
		return atmfValidadorSeleccion;
	}


	public void setAtmfValidadorSeleccion(Integer atmfValidadorSeleccion) {
		this.atmfValidadorSeleccion = atmfValidadorSeleccion;
	}


	public Integer getAtmfVerificadorActivaModal() {
		return atmfVerificadorActivaModal;
	}


	public void setAtmfVerificadorActivaModal(Integer atmfVerificadorActivaModal) {
		this.atmfVerificadorActivaModal = atmfVerificadorActivaModal;
	}


	public MateriaDto getAtmfMateriaSeleccionada() {
		return atmfMateriaSeleccionada;
	}


	public void setAtmfMateriaSeleccionada(MateriaDto atmfMateriaSeleccionada) {
		this.atmfMateriaSeleccionada = atmfMateriaSeleccionada;
	}


	public Integer getAtmfActivaModalCargarSolicitud() {
		return atmfActivaModalCargarSolicitud;
	}


	public void setAtmfActivaModalCargarSolicitud(Integer atmfActivaModalCargarSolicitud) {
		this.atmfActivaModalCargarSolicitud = atmfActivaModalCargarSolicitud;
	}


	public PeriodoAcademico getAtmfPeriodoActivo() {
		return atmfPeriodoActivo;
	}


	public void setAtmfPeriodoActivo(PeriodoAcademico atmfPeriodoActivo) {
		this.atmfPeriodoActivo = atmfPeriodoActivo;
	}


	public Boolean getAtmfInactivaOpcionSolicitar() {
		return atmfInactivaOpcionSolicitar;
	}


	public void setAtmfInactivaOpcionSolicitar(Boolean atmfInactivaOpcionSolicitar) {
		this.atmfInactivaOpcionSolicitar = atmfInactivaOpcionSolicitar;
	}


	public List<FichaInscripcionDto> getAtmfListaFichaInscripcionSau() {
		atmfListaFichaInscripcionSau = atmfListaFichaInscripcionSau==null?(new ArrayList<FichaInscripcionDto>()):atmfListaFichaInscripcionSau;
		
		return atmfListaFichaInscripcionSau;
	}


	public void setAtmfListaFichaInscripcionSau(List<FichaInscripcionDto> atmfListaFichaInscripcionSau) {
		this.atmfListaFichaInscripcionSau = atmfListaFichaInscripcionSau;
	}



	public Persona getAtmfPersona() {
		return atmfPersona;
	}


	public void setAtmfPersona(Persona atmfPersona) {
		this.atmfPersona = atmfPersona;
	}


	public Boolean getAtmfActivaBotonReporte() {
		return atmfActivaBotonReporte;
	}


	public void setAtmfActivaBotonReporte(Boolean atmfActivaBotonReporte) {
		this.atmfActivaBotonReporte = atmfActivaBotonReporte;
	}


	public int getEcfActivadorReporte() {
		return ecfActivadorReporte;
	}


	public void setEcfActivadorReporte(int ecfActivadorReporte) {
		this.ecfActivadorReporte = ecfActivadorReporte;
	}


	public List<MateriaDto> getAtmfLista() {
		atmfLista = atmfLista==null?(new ArrayList<MateriaDto>()):atmfLista;

		return atmfLista;
	}


	public void setAtmfLista(List<MateriaDto> atmfLista) {
		this.atmfLista = atmfLista;
	}


	public PersonaDto getAtmfDecanoFcl() {
		return atmfDecanoFcl;
	}


	public void setAtmfDecanoFcl(PersonaDto atmfDecanoFcl) {
		this.atmfDecanoFcl = atmfDecanoFcl;
	}

	
}
