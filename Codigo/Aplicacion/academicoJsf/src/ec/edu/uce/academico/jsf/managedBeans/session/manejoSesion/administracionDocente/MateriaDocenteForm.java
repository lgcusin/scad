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
   
 ARCHIVO:     MateriaDocenteForm.java	  
 DESCRIPCION: Bean de sesion que maneja las materias por docente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-MAYO-2017 			Dennis Collaguazo                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionDocente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) MateriaDocenteForm.
 * Managed Bean que administra las materias para la visualización por docentes.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="materiaDocenteForm")
@SessionScoped
public class MateriaDocenteForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario mdfUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto mdfDocente;
	
	//PARA BUSQUEDA
	private DocenteJdbcDto mdfMateriaBuscar;
	private List<PeriodoAcademicoDto> mdfListPeriodoAcademicoBusq;
	private List<DocenteJdbcDto> mdfNivelDocenteDtoBusq;
	private List<CarreraDto> mdfListCarreraBusq;
	private List<NivelDto> mdfListNivelBusq;
	private List<ParaleloDto> mdfListParaleloBusq;
	private List<MateriaDto> mdfListMateriaBusq;
	
	private List<DocenteJdbcDto> mdfListCarreraDocenteBusq;
	
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
			
	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servMdfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private CarreraDtoServicioJdbc servMdfCarreraDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servMdfDocenteDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servMdfNivelDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servMdfParaleloDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servMdfMateriaDtoServicioJdbc;
	
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR A LISTAR MATERIAS
	/**
	 * Dirige la navegacion hacia la pagina de listarMateria
	 */
	public String irAlistarMateria(Usuario usuario){
		mdfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			//LISTO LOS PERIODOS ACADEMICOS
//			mdfListPeriodoAcademicoBusq = servMdfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			mdfListPeriodoAcademicoBusq = servMdfPeriodoAcademicoDtoServicioJdbc.listarTodos();
			retorno = "irListarMateriaDocente";
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	/**
	 * Método que permite buscar la carrera por el periódo academico
	 * @param idPeriodo -  idPeriodo seleccionado para la busqueda
	 */
	public void llenarCarrera(int idPeriodo){
		idPeriodo = mdfMateriaBuscar.getPracId();
		mdfMateriaBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		mdfMateriaBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		mdfMateriaBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		mdfMateriaBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		mdfListCarreraBusq = null;
		mdfListCarreraDocenteBusq = null;
		mdfListNivelBusq = null;
		mdfListParaleloBusq = null;
		mdfListMateriaBusq = null;
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
//				//mdfListCarreraBusq = servMdfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(mdfUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				mdfListCarreraDocenteBusq =servMdfDocenteDtoServicioJdbc.buscarCarrerasDocenteXPrac(mdfUsuario.getUsrIdentificacion(),idPeriodo);
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.carrera.id.Periodo.validacion.exception")));
			}
//		} catch (CarreraDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (CarreraDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.carrera.id.Periodo.validacion.no.encontrado.exception")));
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.carrera.id.Periodo.validacion.no.encontrado.exception")));
		} 
	}
	
	
	
	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * @param idCarrera - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera){
		idCarrera = mdfMateriaBuscar.getCrrId();
		mdfMateriaBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		mdfMateriaBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		mdfMateriaBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		mdfListNivelBusq = null;
		mdfListParaleloBusq = null;
		mdfNivelDocenteDtoBusq = null;
		mdfListMateriaBusq = null;
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				//mdfDocente = servMdfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodo(mdfUsuario.getUsrId(), mdfMateriaBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE, mdfMateriaBuscar.getPracId());
				mdfDocente = servMdfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(mdfUsuario.getUsrId(), mdfMateriaBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE,  mdfMateriaBuscar.getPracId());
				//LISTO LOS NIVELES
				//mdfListNivelBusq = servMdfNivelDtoServicioJdbc.listarNivelXPeriodoXCarrera(mdfMateriaBuscar.getPracId(), mdfMateriaBuscar.getCrrId(), mdfDocente.getDtpsId());
				mdfNivelDocenteDtoBusq = servMdfDocenteDtoServicioJdbc.buscarNivelesDocenteXPrac(mdfMateriaBuscar.getCrrId(),mdfUsuario.getUsrIdentificacion(), mdfMateriaBuscar.getPracId());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.nivel.id.carrera.validacion.exception")));
			}
//		} catch (NivelDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (NivelDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.detalle.puesto.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que permite buscar la lista de paralelos por el id del nivel
	 * @param idNivel - idNivel seleccionado para la busqueda
	 */
	public void llenarParalelo(int idNivel){
		idNivel = mdfMateriaBuscar.getNvlId();
		mdfMateriaBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		mdfMateriaBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		mdfListParaleloBusq = null;
		mdfListMateriaBusq = null;
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS PARALELOS
				mdfListParaleloBusq = servMdfParaleloDtoServicioJdbc.listarXperiodoXcarreraXnivelXdocente(mdfMateriaBuscar.getPracId(), mdfMateriaBuscar.getCrrId(), mdfMateriaBuscar.getNvlId(), mdfDocente.getDtpsId());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.llenar.paralelo.id.nivel.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que permite verificar que se encuentre seleccionado al menos un paralelo para la búsqueda
	 * @param idParalelo - idParalelo seleccionado para la verificación
	 */
	public void verificaParalelo(int idParalelo){
		idParalelo = mdfMateriaBuscar.getPrlId();
		mdfListMateriaBusq = null;
		if(idParalelo == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.verificar.paralelo.id.paralelo.validacion.exception")));
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
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		//LISTO LOS PERIODOS ACADEMICOS
		try {
//			mdfListPeriodoAcademicoBusq = servMdfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			mdfListPeriodoAcademicoBusq = servMdfPeriodoAcademicoDtoServicioJdbc.listarTodos();
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.limpiar.periodo.academico.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Busca las materias del docente según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		mdfListMateriaBusq = null;
		try {
			if(mdfMateriaBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.buscar.materia.periodo.validacion.exception")));
			}else if(mdfMateriaBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.buscar.materia.carrera.validacion.exception")));
			}else if(mdfMateriaBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.buscar.materia.nivel.validacion.exception")));
			}else if(mdfMateriaBuscar.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.buscar.materia.paralelo.validacion.exception")));
			} else{
				//BUSCO LA LISTA DE MATERIAS POR PERIODO, CARRERA, NIVEL, PARALELO
				mdfListMateriaBusq = servMdfMateriaDtoServicioJdbc.listarXperiodoXcarreraXnivelXparaleloXdocente(mdfMateriaBuscar.getPracId(), mdfMateriaBuscar.getCrrId(), mdfMateriaBuscar.getNvlId(), mdfMateriaBuscar.getPrlId(), mdfDocente.getDtpsId());
			}
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Materia.Docente.buscar.no.encontrado.exception")));
		}
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		//INICIALIZO EL DTO DE DOCENTE BUSCAR
		mdfMateriaBuscar = new DocenteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO ID
		mdfMateriaBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		mdfMateriaBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL ID
		mdfMateriaBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO ID
		mdfMateriaBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//ANULO LA LISTA DE PERIODOS ACADEMICOS
		mdfListPeriodoAcademicoBusq = null;
		//ANULO LA LISTA DE CARRERAS
//		mdfListCarreraBusq = null;
		mdfListCarreraDocenteBusq = null;
		//ANULO LA LISTA DE NIVEL
//		mdfListNivelBusq = null;
		mdfNivelDocenteDtoBusq = null;
		//ANULO LA LISTA DE PARALELOS
		mdfListParaleloBusq = null;
		//ANULO LA LISTA DE MATERIAS POR DOCENTE
		mdfListMateriaBusq = null;
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getMdfUsuario() {
		return mdfUsuario;
	}
	public void setMdfUsuario(Usuario mdfUsuario) {
		this.mdfUsuario = mdfUsuario;
	}
	public DocenteJdbcDto getMdfDocente() {
		return mdfDocente;
	}
	public void setMdfDocente(DocenteJdbcDto mdfDocente) {
		this.mdfDocente = mdfDocente;
	}
	public DocenteJdbcDto getMdfMateriaBuscar() {
		return mdfMateriaBuscar;
	}
	public void setMdfMateriaBuscar(DocenteJdbcDto mdfMateriaBuscar) {
		this.mdfMateriaBuscar = mdfMateriaBuscar;
	}
	public List<PeriodoAcademicoDto> getMdfListPeriodoAcademicoBusq() {
		mdfListPeriodoAcademicoBusq = mdfListPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademicoDto>()):mdfListPeriodoAcademicoBusq;
		return mdfListPeriodoAcademicoBusq;
	}
	public void setMdfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> mdfListPeriodoAcademicoBusq) {
		this.mdfListPeriodoAcademicoBusq = mdfListPeriodoAcademicoBusq;
	}
	public List<CarreraDto> getMdfListCarreraBusq() {
		mdfListCarreraBusq = mdfListCarreraBusq==null?(new ArrayList<CarreraDto>()):mdfListCarreraBusq;
		return mdfListCarreraBusq;
	}
	public void setMdfListCarreraBusq(List<CarreraDto> mdfListCarreraBusq) {
		this.mdfListCarreraBusq = mdfListCarreraBusq;
	}
	public List<NivelDto> getMdfListNivelBusq() {
		mdfListNivelBusq = mdfListNivelBusq==null?(new ArrayList<NivelDto>()):mdfListNivelBusq;
		return mdfListNivelBusq;
	}
	public void setMdfListNivelBusq(List<NivelDto> mdfListNivelBusq) {
		this.mdfListNivelBusq = mdfListNivelBusq;
	}
	public List<ParaleloDto> getMdfListParaleloBusq() {
		mdfListParaleloBusq = mdfListParaleloBusq==null?(new ArrayList<ParaleloDto>()):mdfListParaleloBusq;
		return mdfListParaleloBusq;
	}
	public void setMdfListParaleloBusq(List<ParaleloDto> mdfListParaleloBusq) {
		this.mdfListParaleloBusq = mdfListParaleloBusq;
	}
	public List<MateriaDto> getMdfListMateriaBusq() {
		mdfListMateriaBusq = mdfListMateriaBusq==null?(new ArrayList<MateriaDto>()):mdfListMateriaBusq;
		return mdfListMateriaBusq;
	}
	public void setMdfListMateriaBusq(List<MateriaDto> mdfListMateriaBusq) {
		this.mdfListMateriaBusq = mdfListMateriaBusq;
	}

	public List<DocenteJdbcDto> getMdfListCarreraDocenteBusq() {
		mdfListCarreraDocenteBusq = mdfListCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):mdfListCarreraDocenteBusq;
		return mdfListCarreraDocenteBusq;
	}

	public void setMdfListCarreraDocenteBusq(List<DocenteJdbcDto> mdfListCarreraDocenteBusq) {
		this.mdfListCarreraDocenteBusq = mdfListCarreraDocenteBusq;
	}

	public List<DocenteJdbcDto> getMdfNivelDocenteDtoBusq() {
		mdfNivelDocenteDtoBusq = mdfNivelDocenteDtoBusq==null?(new ArrayList<DocenteJdbcDto>()):mdfNivelDocenteDtoBusq;
		return mdfNivelDocenteDtoBusq;
	}

	public void setMdfNivelDocenteDtoBusq(List<DocenteJdbcDto> mdfNivelDocenteDtoBusq) {
		this.mdfNivelDocenteDtoBusq = mdfNivelDocenteDtoBusq;
	}
	
	
	
}
