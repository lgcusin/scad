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
   
 ARCHIVO:     AdministracionNotaCorte.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de  notas de corte.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
11-07-2019			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.NotaCorteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.NotaCorte;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) AdministracionNotaCorte.
 * Managed Bean que maneja las peticiones para la administración de notas de corte.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name="administracionNotaCorteForm")
@SessionScoped
public class AdministracionNotaCorte implements Serializable{

	private static final long serialVersionUID = -397102404723196895L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	// OBJETOS
	private Usuario ancfUsuario;
	private PeriodoAcademico ancfPeriodoAcademicoCorte;
	
	// LISTAS DE OBJETO
	private List<CarreraDto> ancfListCarreraNotaCorte;
	private List<CarreraDto> ancfListCarreraNotaCorteAntes; //Guardar Notas de corte antes de realizar la actualizacion.
	
	// **********AUXILIARES*****************//
	private Integer ancfValidadorGuardarClick;  
	
		
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
		
	}
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB 
	PeriodoAcademicoServicio servAncfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servAncfCarreraDto;
	@EJB
	NotaCorteServicio servAncfNotaCorte;
    @EJB
    FichaInscripcionDtoServicioJdbc servAncfFichaInscripcionJdbcDto;
	
		
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	/**
	 * Método que permite iniciar la administración de las notas de corte
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración las notas de corte.
	 */
	public String irAdministracionNotaCorte(Usuario usuario) {
		ancfUsuario = usuario; 
		iniciarParametros(); // llamo al método iniciarParametro
	
		try {
	        //Buscar si existe periodo en cierre
			ancfPeriodoAcademicoCorte = servAncfPeriodoAcademico.buscarPeriodoEnCierre(); //no tiene exception, devuelve null
		
		
		if(ancfPeriodoAcademicoCorte==null){// Sino existe periodo en cierre, busco periodo activo
			ancfPeriodoAcademicoCorte = servAncfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		}
			
		//Lista de Carreras de rediseño
			//ancfListCarreraNotaCorte = servAncfCarreraDto.listarCarreraActivasRediseno();
		
		//Lista de Carerras de pregrado(Diseño y rediseño)
		ancfListCarreraNotaCorte = servAncfCarreraDto.listarCarrerasPregrado();
		
		
		if(ancfListCarreraNotaCorte!=null && ancfListCarreraNotaCorte.size()>0){
				
			for (CarreraDto carreraDto : ancfListCarreraNotaCorte) {
				try {
					
					//busco si existe notas de corte activas por carrera en el periodo validado anteriormente
					NotaCorte notaCorteAux = servAncfNotaCorte.buscarActivoXCrrXPrac(carreraDto.getCrrId(), ancfPeriodoAcademicoCorte.getPracId());
				   //Si existe nota de corte en la carrera agrega a la lista de carreras
					if (notaCorteAux!=null){
						carreraDto.setNocrNota(notaCorteAux.getNocrNota());
						carreraDto.setNocrId(notaCorteAux.getNocrId());
						carreraDto.setNocrEstado(notaCorteAux.getNocrEstado());
						carreraDto.setNocrPracId(notaCorteAux.getNocrPracId());
						
						//Guardo nota de corte antes si existe ya una nota de corte
						CarreraDto carreraAntes = new CarreraDto();
						carreraAntes.setCrrId(carreraDto.getCrrId());
						carreraAntes.setCrrDescripcion(carreraDto.getCrrDescripcion());
						carreraAntes.setCrrDetalle(carreraDto.getCrrDetalle());
						carreraAntes.setDpnId(carreraDto.getDpnId());
						carreraAntes.setDpnDescripcion(carreraDto.getDpnDescripcion());
						carreraAntes.setNocrNota(notaCorteAux.getNocrNota());
						carreraAntes.setNocrId(notaCorteAux.getNocrId());
						carreraAntes.setNocrEstado(notaCorteAux.getNocrEstado());
						carreraAntes.setNocrPracId(notaCorteAux.getNocrPracId());
						
						ancfListCarreraNotaCorteAntes.add(carreraAntes); //Solo lista con carreras con notas de corte
				
					}
				
				} catch (NotaCorteException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				}
			}
			
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.ir.administracion.nota.corte.listaCarrera.validacion.exception")));
			}

		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.ir.administracion.nota.corte.carrera.exception")));
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.ir.administracion.nota.corte.carrera.no.encontrado.exception")));
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.ir.administracion.nota.corte.periodo.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.ir.administracion.nota.corte.periodo.exception")));
		}
		
		return "irAdministracionNotaCorte"; // Voy a la página de administración
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados y paneles
	 * @return 
	 */
	public String limpiar() {
		iniciarParametros();
		return null;
	}
	

	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametros() {
	 ancfPeriodoAcademicoCorte= null;
	 ancfListCarreraNotaCorte= new ArrayList<>();
	 ancfListCarreraNotaCorteAntes = new ArrayList<>();
	 ancfValidadorGuardarClick= 0;
	
	}
	
	
 /**
  * Método para editar o crear Nota Corte en BDD
 */
	public String editarGuardarNotaCorte() {
	
		String retorno = null;
		//DESACTIVAR NOTAS CORTES ACTIVAS DE PERIODOS ANTERIORES SI EXISTEN.
	try {
			List<NotaCorte> listaNotaCorteDesactivar = new ArrayList<>();
		     //listo Notas de corte avtivos con periodos distintos del actual
			 listaNotaCorteDesactivar =servAncfNotaCorte.listarActivoXPrac(ancfPeriodoAcademicoCorte.getPracId());
		
		      if(listaNotaCorteDesactivar!=null&&listaNotaCorteDesactivar.size()>0){
				servAncfNotaCorte.desactivarNotaCorteAnteriores(listaNotaCorteDesactivar); //método para desactivar notas de corte anteriores
		       }
		 
			
		//Guardar cambios o nuevas notas de corte
		boolean guardadoCorrecto= false;
		ancfValidadorGuardarClick = 0;
		for (CarreraDto carreraDto : ancfListCarreraNotaCorte) {
						guardadoCorrecto=servAncfNotaCorte.editarGuardarNotaCorte(carreraDto, ancfPeriodoAcademicoCorte.getPracId(), ancfUsuario);
				if(!guardadoCorrecto){
					break;
					
				}
			
			
		}
		
		if(guardadoCorrecto){
				
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.editarGuardarNotaCorte.validacion.exito")));
		limpiar();
		retorno= "irInicio";
			
		}else{
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.editarGuardarNotaCorte.validacion.sin.exito")));
    
			
		}
		
		} catch (NotaCorteException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
		return retorno;
	
	}
	
	
	/**
	  * Método para verificar antes de guardar información
	 */
		public void verificaEditarGuardar() {
			
			//Verifico que no se grabe mientras todas las carreras no tengan notas de corte
			boolean existeUno = false;
			for (CarreraDto carreraDto : ancfListCarreraNotaCorte) {
				if(carreraDto.getNocrNota()!=null){
					existeUno = true;
					break;
				}
			}
			
			
			
	if(existeUno){ //Si se registro al menos una nota de corte
			List<FichaInscripcionDto> listaFichasInscripcion = new ArrayList<>();
			try {
				
		           if(ancfListCarreraNotaCorteAntes!=null&& ancfListCarreraNotaCorteAntes.size()>0)	{	//Si existio alguna nota de corte anterior, esta editando
		            	Integer contador = 0;
			
		        	for (CarreraDto carreraDto : ancfListCarreraNotaCorte) { //Lista de notas nuevas y editadas
				        boolean encontrado = false;
			        	contador++;
				
			           for (CarreraDto carreraDtoAntes : ancfListCarreraNotaCorteAntes) {  //lista de  notas de corte anteriores
					
				        if(carreraDto.getNocrNota()!=null){ //si se registro una valor de nota nueva
						
				            	BigDecimal notaNuevaAux= new BigDecimal(carreraDto.getNocrNota());
					            BigDecimal notaAntesAux= new BigDecimal(carreraDtoAntes.getNocrNota());
					
					        if(carreraDto.getCrrId() == carreraDtoAntes.getCrrId()  // Si es la misma carrera  y el valor es diferenteal valor anterior de corte que esta editando
					          && ((notaAntesAux.compareTo(notaNuevaAux)>0)||(notaAntesAux.compareTo(notaNuevaAux)<0))){
																		
					            	 listaFichasInscripcion =	servAncfFichaInscripcionJdbcDto.listarXnotaCorte(carreraDtoAntes.getNocrId()); //busco si existe fcin registrados en el id de nota corte
						
					    	         if((listaFichasInscripcion!=null)&&(listaFichasInscripcion.size()>0)){ //Existe alguna fcin que se comparo con la nota de corte de antes.
							
						              	encontrado = true;  //encontre al menos alguien
							             break;
						              }
						
						
					          }
					
				          }
				
			            } //for carrreaDto Antes
				
				         if(encontrado){
					
					      ancfValidadorGuardarClick = 2; //Tengo que presentar otro modal con advertencia de que ya existe personas registradas bajo la nota corte anterior.
					     break;
					
				          }
				
				          if((!encontrado)&&(contador == ancfListCarreraNotaCorte.size())){//termine de buscar y no encontre personas registradas con ninguna de las notas de corte anterior.
					        ancfValidadorGuardarClick = 1; //habilito modal normal, para guardar
				           }
				
			           } //fin For carreraDto
			
	            	}else{
			
			                 ancfValidadorGuardarClick = 1; //No existian notas de corte registradas en este periodo, se procede a guardar porque son notas nuevas.
			
		                  }
			
			} catch (FichaInscripcionDtoException e) {
				FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.verificaEditarGuardar.fichaInscripcion.Exception")));
			}
	}else{
				
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionNotaCorte.verificaEditarGuardar.validacion.notaCorte.anterior")));
		
	}

}
	

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

		public Usuario getAncfUsuario() {
			return ancfUsuario;
		}

		public void setAncfUsuario(Usuario ancfUsuario) {
			this.ancfUsuario = ancfUsuario;
		}

		public PeriodoAcademico getAncfPeriodoAcademicoCorte() {
			return ancfPeriodoAcademicoCorte;
		}

		public void setAncfPeriodoAcademicoCorte(PeriodoAcademico ancfPeriodoAcademicoCorte) {
			this.ancfPeriodoAcademicoCorte = ancfPeriodoAcademicoCorte;
		}

		public List<CarreraDto> getAncfListCarreraNotaCorte() {
			ancfListCarreraNotaCorte = ancfListCarreraNotaCorte==null?(new ArrayList<CarreraDto>()):ancfListCarreraNotaCorte;	
			return ancfListCarreraNotaCorte;
		}

		public void setAncfListCarreraNotaCorte(List<CarreraDto> ancfListCarreraNotaCorte) {
			this.ancfListCarreraNotaCorte = ancfListCarreraNotaCorte;
		}

		public Integer getAncfValidadorGuardarClick() {
			return ancfValidadorGuardarClick;
		}

		public void setAncfValidadorGuardarClick(Integer ancfValidadorGuardarClick) {
			this.ancfValidadorGuardarClick = ancfValidadorGuardarClick;
		}

		public List<CarreraDto> getAncfListCarreraNotaCorteAntes() {
			ancfListCarreraNotaCorteAntes = ancfListCarreraNotaCorteAntes==null?(new ArrayList<CarreraDto>()):ancfListCarreraNotaCorteAntes;	
			return ancfListCarreraNotaCorteAntes;
		}

		public void setAncfListCarreraNotaCorteAntes(List<CarreraDto> ancfListCarreraNotaCorteAntes) {
			this.ancfListCarreraNotaCorteAntes = ancfListCarreraNotaCorteAntes;
		}

		
}
