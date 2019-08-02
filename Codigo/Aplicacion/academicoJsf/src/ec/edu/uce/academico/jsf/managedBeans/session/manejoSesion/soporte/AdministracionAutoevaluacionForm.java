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
   
 ARCHIVO:     AdministracionPeriodoAcademicoForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de la tabla PeriodoAcademico.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
5-NOV-2017			Vinicio Rosales                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) AdministracionAutoevaluacionForm.
 * Managed Bean que maneja las peticiones para la administración de la generacion del reporte.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name="administracionAutoevaluacionForm")
@SessionScoped
public class AdministracionAutoevaluacionForm implements Serializable{

	private static final long serialVersionUID = -5706899311603575398L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	//para buscar
	private Usuario aafUsuario;
	private List<CarreraDto> aafListAutoevaluacionCarreraDto;
	private List<CarreraDto> aafListReporteCarreraDto;
	private List<PeriodoAcademico> aafListPeriodoAcademico;
	private Integer aafValidadorClic;
	private Integer aafPeriodoId;

	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private ContenidoEvaluacionDtoServicioJdbc servJdbcContenidoEvaluacionDto;
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	/**
	 * Incicializa las variables para la funcionalidad de administración de la generacion del reporte
	 * @return navegacion al xhtml de listar Autoevaluacion
	 */
	public String irAdministracionAutoevaluacion(Usuario usuario) {
		aafUsuario = usuario;
		iniciarParametros();
		return "irListarAutoevaluacion";
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio
	 */
	public void iniciarParametros(){
		aafPeriodoId = GeneralesConstantes.APP_ID_BASE;
		aafListPeriodoAcademico = cargarPeriodosAcademicos();
		aafListAutoevaluacionCarreraDto = null;
		bloquearModal();
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio
	 */
	public String irInicio(){
		limpiar();
		return "irInicio"; 	
	}
	
	
	/**
	 * Método para limpiar los parámtros de busqueda ingresados
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	/**
	 * Método para buscar la entidad deseada con los parámetros de búsqueda ingresados
	 */
	public void buscar(){
		
		if(aafPeriodoId != GeneralesConstantes.APP_ID_BASE){
			try {
				List<CarreraDto> docentesAutoevaluar = servJdbcCarreraDto.listarXDocentesHorasClaseXPeriodo(aafPeriodoId);
				if (docentesAutoevaluar != null && docentesAutoevaluar.size() > 0) {
					for (CarreraDto item : docentesAutoevaluar) {
						item.setCrrEstado(verificarAutoevaluacion(item.getPrsIdentificacion()));
					}
					docentesAutoevaluar.sort(Comparator.comparing(CarreraDto::getDpnDescripcion).thenComparing(Comparator.comparing(CarreraDto::getCrrDescripcion)));
					aafListAutoevaluacionCarreraDto = docentesAutoevaluar;
				}
				
				List<CarreraDto> carreras = agruparPorCarreras(docentesAutoevaluar);
				aafListReporteCarreraDto = cargarEstadistico(carreras);
			
			} catch (DetallePuestoDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		}else{
			FacesUtil.mensajeError("Debe seleccionar el período académico");
		}
		
	}
	
	private List<CarreraDto> cargarEstadistico(List<CarreraDto> carreras) {
		List<CarreraDto> retorno = new ArrayList<>();	
		Map<String, CarreraDto> mapCarreras= new HashMap<String, CarreraDto>();
		for (CarreraDto item : carreras) {
			mapCarreras.put(item.getCrrDescripcion(), item);
		}
		for (Entry<String, CarreraDto> item : mapCarreras.entrySet()) {
			retorno.add(item.getValue());
		}
		
		retorno.sort(Comparator.comparing(CarreraDto::getDpnDescripcion).thenComparing(CarreraDto::getCrrDescripcion));
		
		return retorno;
	}

	private List<CarreraDto> agruparPorCarreras(List<CarreraDto> autoEvaluados) {
		List<List<CarreraDto>> carreras = new ArrayList<>();		
		List<CarreraDto> retorno = new ArrayList<>();	
		
		Map<Integer, List<CarreraDto>> mapCarreras = autoEvaluados.stream().collect(Collectors.groupingBy(CarreraDto::getCrrId));
		for (Entry<Integer, List<CarreraDto>> carreraDocentes : mapCarreras.entrySet()) {
			carreras.add(carreraDocentes.getValue());
		}
		carreras.addAll(mapCarreras.values());
		
		
		for (List<CarreraDto> auxCarreras : carreras) {
			int evaluados = 0;
			int totalEvaluados = 0;
			
			auxCarreras.get(auxCarreras.size() - 1).setCrrEvaluado(evaluados);
			auxCarreras.get(auxCarreras.size() - 1).setCrrNoEvaluado(totalEvaluados);
			
			for (CarreraDto auxDocente : auxCarreras) {
				if (auxDocente.getCrrEstado().intValue() == 1) {
					evaluados = evaluados  + 1;
				}
				totalEvaluados = totalEvaluados + 1;
				auxDocente.setCrrEvaluado(evaluados);
				auxDocente.setCrrNoEvaluado(totalEvaluados);
			}
			
		}
		
		for (List<CarreraDto> auxCarreras : carreras) {
			CarreraDto carrera = new CarreraDto();
			carrera.setDpnDescripcion(auxCarreras.get(auxCarreras.size()-1).getDpnDescripcion());
			carrera.setCrrDescripcion(auxCarreras.get(auxCarreras.size()-1).getCrrDescripcion());
			carrera.setCrrEvaluado(auxCarreras.get(auxCarreras.size()-1).getCrrEvaluado());
			carrera.setCrrNoEvaluado(auxCarreras.get(auxCarreras.size()-1).getCrrNoEvaluado());
			retorno.add(carrera);
		}
		
		
		return retorno;
	}
	
	private int verificarAutoevaluacion(String identificacion){
		try {
			return servJdbcContenidoEvaluacionDto.buscarEvaluacionDocente(identificacion, 5);
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			return 0;
		} catch (ContenidoEvaluacionDtoException e) {
			return 0;
		}
	}
	
	public String etiquetaEstadoEvaluacion(int estado){
		String retorno = "";
		
		switch (estado) {
		case 0:
			retorno = "NO EVALUADO";
			break;
		case 1:
			retorno = "EVALUADO";
			break;
		}
		
		return retorno;
	}
	
	/**
	 * Método para cancelar la edicion o creacion de un periodo academico
	 * @return navegacion al xhtml de listar periodo academico
	 */
	public String cancelar(){
		limpiar();
		return "cancelar"; 	
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla
	 */
	public void verificarClickImprimir(){
		if(aafListReporteCarreraDto.size() > 0){
			PersonaDto directora = new PersonaDto();
			directora.setPrsPrimerApellido("Carrillo");
			directora.setPrsSegundoApellido("Chico");
			directora.setPrsNombres("María Elena");
			directora.setPrsCargo("DIRECTORA DE TECNOLOGIAS");
			ReporteEvaluacionDocenteForm.generarReporteGeneralAutoevaluacion(aafListReporteCarreraDto, aafUsuario, directora, "100", "100");
			habilitaModalImpresion();
		}else{
			FacesUtil.mensajeError("No existe información para presentar");
			bloquearModal();
		}
	}
	
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla
	 */
	public void verificarClickImprimirXls(){
		if(aafListReporteCarreraDto.size() > 0){
			PersonaDto directora = new PersonaDto();
			directora.setPrsPrimerApellido("Carrillo");
			directora.setPrsSegundoApellido("Chico");
			directora.setPrsNombres("María Elena");
			directora.setPrsCargo("DIRECTORA DE TECNOLOGIAS");
			ReporteEvaluacionDocenteForm.generarReporteGeneralAutoevaluacionXls(aafListAutoevaluacionCarreraDto, aafUsuario, directora, "100", "100");
			habilitaModalImpresionXls();
		}else{
			FacesUtil.mensajeError("No existe información para presentar");
			bloquearModal();
		}
	}
	
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloquearModal(){
		aafValidadorClic = 0;
	}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		aafValidadorClic = 1;
	}
	
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresionXls(){
		aafValidadorClic = 2;
	}
	
	
	private List<PeriodoAcademico> cargarPeriodosAcademicos(){
		try {
			return  servPeriodoAcademico.buscarPracEstadoEvaluacionXestadoPracXtipoPrac(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}  catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	

	// ********************************************************************/
	// *********************** ENCAPSULAMIENTO ****************************/
	// ********************************************************************/
	

	public Usuario getAafUsuario() {
		return aafUsuario;
	}

	public void setAafUsuario(Usuario aafUsuario) {
		this.aafUsuario = aafUsuario;
	}

	public List<CarreraDto> getAafListAutoevaluacionCarreraDto() {
		return aafListAutoevaluacionCarreraDto;
	}

	public void setAafListAutoevaluacionCarreraDto(List<CarreraDto> aafListAutoevaluacionCarreraDto) {
		this.aafListAutoevaluacionCarreraDto = aafListAutoevaluacionCarreraDto;
	}

	public List<PeriodoAcademico> getAafListPeriodoAcademico() {
		return aafListPeriodoAcademico;
	}

	public void setAafListPeriodoAcademico(List<PeriodoAcademico> aafListPeriodoAcademico) {
		this.aafListPeriodoAcademico = aafListPeriodoAcademico;
	}

	public Integer getAafValidadorClic() {
		return aafValidadorClic;
	}

	public void setAafValidadorClic(Integer aafValidadorClic) {
		this.aafValidadorClic = aafValidadorClic;
	}

	public Integer getAafPeriodoId() {
		return aafPeriodoId;
	}

	public void setAafPeriodoId(Integer aafPeriodoId) {
		this.aafPeriodoId = aafPeriodoId;
	}

	public List<CarreraDto> getAafListReporteCarreraDto() {
		return aafListReporteCarreraDto;
	}

	public void setAafListReporteCarreraDto(List<CarreraDto> aafListReporteCarreraDto) {
		this.aafListReporteCarreraDto = aafListReporteCarreraDto;
	}
	
	

	
}

