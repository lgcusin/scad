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
   
 ARCHIVO:     EstudianteNotasPosgradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la consulta de notas del estudiante posgrado. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 28-06-2018			 Marcelo Quishpe                         Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EstudianteNotasPosgradoForm. 
 * Bean de sesion que maneja la consulta de notas del estudiante posgrado.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "estudianteNotasPosgradoForm")
@SessionScoped
public class EstudianteNotasPosgradoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario enpfUsuario;
	private PeriodoAcademico enpfPeriodoAcademicoBuscar;
	private CarreraDto enpfCarreraDtoBuscar;
	private List<PeriodoAcademico> enpfListPeriodoAcademico;
	private List<CarreraDto> enpfListCarreraDto;
	private List<MateriaDto> enpfListMateriaDto;	
	private List<FichaMatriculaDto> enpfListMatriculaDto;
	private Persona enpfEstudiante;
	private PeriodoAcademico enpfPeriodo;
	private Carrera enpfCarrera;
	private boolean enpRenderNumMatricula;
	
	//private List<MateriaDto> enpfNotasSAu; 
	
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
	CarreraServicio  servCarreraServicio;
	@EJB
	MatriculaServicioJdbc servMatriculaDtoServicioJdbc; 

	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de lista de matriculaa
	 * @return navegacion al listar matricula
	 */
	public String irAlistarMatriculasPosgrado(Usuario usuario) {
		try {
			
			enpfUsuario = usuario;

			enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarreraPosgrado(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			
			if(enpfListMatriculaDto != null || enpfListMatriculaDto.size() > 0){

				enpfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
				enpfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);

				getEnpfListCarreraDto();
				for (FichaMatriculaDto itemMatricula : enpfListMatriculaDto) { 
					Boolean encontro = false;  
					for (CarreraDto itemCarrera : enpfListCarreraDto) {  
						if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ 
							encontro = true; 
						}
					} 
					if(encontro == false){ 
						CarreraDto carreraAgregar = new CarreraDto();
						carreraAgregar.setCrrId(itemMatricula.getCrrId());
						carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
						carreraAgregar.setCrrEspeCodigo(itemMatricula.getCrnEstado());
						enpfListCarreraDto.add(carreraAgregar);
					}
				}



				getEnpfListPeriodoAcademico();
				for (FichaMatriculaDto itemMatricula : enpfListMatriculaDto) { 
					Boolean encontro = false; 
					for (PeriodoAcademico itemPeriodoAcademico : enpfListPeriodoAcademico) { 
						if(itemMatricula.getPracId() == itemPeriodoAcademico.getPracId()){ 
							encontro = true; 
						}
					} 

					if(encontro == false){ 
						PeriodoAcademico periodoAgregar = new PeriodoAcademico();
						periodoAgregar.setPracId(itemMatricula.getPracId());  
						periodoAgregar.setPracDescripcion(itemMatricula.getPracDescripcion()); 
						enpfListPeriodoAcademico.add(periodoAgregar);
					}
				}
			}else{
				FacesUtil.mensajeError("Usted no posee registros de matricula en posgrado ");
				return null;
			}

			
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.iniciarListarMatricula.ficha.matricula.exception")));
			return null;
		} 
		
		return "irListarMatriculaEstudiantePosgrado";
	}


	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		try {

			enpfListMatriculaDto = new ArrayList<>();
//			if(enpfCarreraDtoBuscar.getCrrId() != GeneralesConstantes.APP_ID_BASE){
				
					enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarreraPosgrado(GeneralesConstantes.APP_ID_BASE, enpfUsuario.getUsrPersona().getPrsIdentificacion(), enpfCarreraDtoBuscar.getCrrId());
			
 
		    	if(enpfListMatriculaDto.size() == 0){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.buscar.matriculas.no.encontrado.exception")));
					enpfListMatriculaDto = null;
				}
//			}else{
//				enpfListMatriculaDto = servEnpfFichaMatriculaDto.ListarXPeriodoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, enpfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
//				
//			}
			
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteNotasPregrado.buscar.ficha.matricula.exception")));
		}  
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar() {
		enpfPeriodoAcademicoBuscar = new PeriodoAcademico(GeneralesConstantes.APP_ID_BASE);
		enpfCarreraDtoBuscar = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		enpfListMatriculaDto = null;
		buscar();
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
		enpfCarrera = null;
		enpfEstudiante = null;
		enpfPeriodo = null;
		return "irInicio";
	}
	
	/**
	 * Visualiza las notas generada del estudiante
	 * @return  Navegacion a la pagina de notas.
	 */
	public String irVerNotas(FichaMatriculaDto fichaMatriculaDto){
		enpRenderNumMatricula= true;
		try {
			enpfCarrera= servCarreraServicio.buscarPorId(fichaMatriculaDto.getCrrId());
		} catch (CarreraNoEncontradoException e3) {
			    FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e3.getMessage());
		} catch (CarreraException e3) {
			    FacesUtil.limpiarMensaje(); 
			    FacesUtil.mensajeError(e3.getMessage());
		}
		
		try {
			enpfPeriodo=servEnpfPeriodoAcademico.buscarPorId(fichaMatriculaDto.getPracId());
		} catch (PeriodoAcademicoNoEncontradoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		} catch (PeriodoAcademicoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		}
		
		try {
			enpfEstudiante= servPersonaServicio.buscarPorId(enpfUsuario.getUsrPersona().getPrsId());
		} catch (PersonaNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		} catch (PersonaException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		
		try {
		
			
			if(enpfCarrera != null && enpfCarrera.getCrrTipo()==CarreraConstantes.TIPO_POSGRADO_VALUE){
				enpfListMateriaDto = servEnpfMateriaDto.listarXestudianteXmatriculaXperiodoXcarreraXPosgrado(enpfUsuario.getUsrPersona().getPrsId(), fichaMatriculaDto.getFcmtId(), fichaMatriculaDto.getPracId());
			    enpRenderNumMatricula= false;
			}
			
			
		} catch (MateriaDtoException e) {
		} catch (MateriaDtoNoEncontradoException e) {
		}
		
				
		return "irVerNotasPosgrado";
	}
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String regresarMatricula(){
		enpfListMateriaDto = null;
		return "regresarListarMatricula";
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


	public boolean isEnpRenderNumMatricula() {
		return enpRenderNumMatricula;
	}


	public void setEnpRenderNumMatricula(boolean enpRenderNumMatricula) {
		this.enpRenderNumMatricula = enpRenderNumMatricula;
	}
	
}
