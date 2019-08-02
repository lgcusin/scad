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
   
 ARCHIVO:     ListasCombosForm.java	  
 DESCRIPCION: Bean de peticion que maneja las listas para los combos en la aplicacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2016		Dennis Collaguazo			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.request.generales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.AulaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CorequisitoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.EdificioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.EtniaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.EvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FuncionTipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PrerequisitoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PuestoEnum;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ReferenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;


/**
 * Clase (request bean) ListasCombosForm.
 * Bean de peticion que maneja las listas para los combos en la aplicacion.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="listasCombosForm")
@RequestScoped
public class ListasCombosForm implements Serializable {
	private static final long serialVersionUID = -873375668506819062L;
	
	@EJB	private MateriaServicio servMateriaServicio;
	@EJB	private CarreraServicio servCarreraServicio;
	
	/**
	 * Genera los items para el combo categoria de la tabla puesto.
	 * @author fgguzman
	 * @return items para el combo categoria de la tabla puesto.
	 */
	public static List<SelectItem> getListCategoriaPuesto() {
		List<SelectItem>  retorno = new ArrayList<>();
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_PRINCIPAL.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_PRINCIPAL.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_AGREGADO.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_AGREGADO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_AUXILIAR.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_AUXILIAR.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_HONORARIO.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_HONORARIO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_INVITADO.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_INVITADO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_OCASIONAL.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_OCASIONAL.getLabel()));
		return retorno;
	}
	
	/**
	 * Obtiene el label de la categoria del puesto.
	 * @author fgguzman
	 * @param param - categoria del puesto.
	 * @return label del valor de la categoria puesto.
	 */
	public static String getLabelCategoriaPuesto(int param){
		String retorno = "";
		
		for (SelectItem item : getListCategoriaPuesto()) {
			if(((Integer)item.getValue()).intValue() == param){
				retorno = item.getLabel();
				break;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Genera items para el combo unidad de medida de asignaturas en procesos de carrera de pregrado.
	 * @author fgguzman
	 * @return items para el combo unidad de medida de asignaturas en procesos de carrera de pregrado.
	 */
	public static List<SelectItem> getListUnidadMedidaAsignaturaPregrado()	{
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE, MateriaConstantes.MATERIA_POR_HORAS_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE, MateriaConstantes.MATERIA_POR_CREDITOS_LABEL));
		return retorno;
		
	}
	
	
	/**
	 * Obtiente el label de la unidad de medida de las asignaturas de pregrado según el proceso de carrera al que pertenecen.
	 * @author fgguzman
	 * @param param - tipo de proceso de la carrera de pregrado.
	 * @return horas / creditos.
	 */
	public static String getLabelUnidadMedidaAsignaturaPregrado(int param){
		String retorno = "";
		
		for (SelectItem item : getListUnidadMedidaAsignaturaPregrado()) {
			if(((Integer)item.getValue()).intValue() == param){
				retorno = item.getLabel();
				break;
			}
		}
		
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo tipo de unidad de medida
	 * @return lista de items para el combo tipo de unidad de medida
	 */
	public List<SelectItem> getListaUnidadMedida()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MateriaConstantes.MATERIA_POR_CREDITOS_VALUE,MateriaConstantes.MATERIA_POR_CREDITOS_LABEL));
		retorno.add(new SelectItem(MateriaConstantes.MATERIA_POR_HORAS_VALUE,MateriaConstantes.MATERIA_POR_HORAS_LABEL));
		return retorno;
		
	}
	
	
	/**
	 * Obtiente el label de unidad de medida
	 * @param unidadMedida - valor de la unidad de Medida de la materia
	 * @return label unidad de Medida
	 */
	public String getLabelUnidadMedida(int unidadMedida){
		String retorno = null;
		for (SelectItem item : getListaUnidadMedida()) {
			if(((Integer)item.getValue()).intValue() == unidadMedida){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo identificación
	 * @return lista de items para el combo tipo identificación
	 */
	public List<SelectItem> getListaTipoIdentificacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE,PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE,PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del tipo documento
	 * @param tipo - tipo documento
	 * @return label del valor del tipo documento
	 */
	public String getLabelTipoDocumento(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoIdentificacion()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo sexo
	 * @return lista de items para el combo sexo
	 */
	public List<SelectItem> getListaSexo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PersonaConstantes.SEXO_HOMBRE_VALUE,PersonaConstantes.SEXO_HOMBRE_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.SEXO_MUJER_VALUE,PersonaConstantes.SEXO_MUJER_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo numero de matricula unicamente para la funcionalidad de carga del histórico de nivelación
	 * @return lista de items para el combo numero de matricula unicamente para la funcionalidad de carga del histórico de nivelación
	 */
	public List<SelectItem> getListaNumeroMatriculaNivelacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE ,DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE,DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo con opciones de SI y NO 
	 * @return lista de items para el combo con opciones de SI y NO 
	 */
	public List<SelectItem> getListaGeneralSiNo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(GeneralesConstantes.APP_SI_VALUE,GeneralesConstantes.APP_SI_LABEL));
		retorno.add(new SelectItem(GeneralesConstantes.APP_NO_VALUE,GeneralesConstantes.APP_NO_LABEL));
		return retorno;
	}
	
	public String getResultadoSiNo(Integer op)	{
		if(op==GeneralesConstantes.APP_SI_VALUE){
			return GeneralesConstantes.APP_SI_LABEL;
		}else{
			return GeneralesConstantes.APP_NO_LABEL;
		}
	}
	
	public String getResultadoNivelPosgrado(Integer op)	{
		switch (op) {
		case 101:
			return NivelConstantes.NIVEL_PRIMERO_LABEL;
		case 102:
			return NivelConstantes.NIVEL_SEGUNDO_LABEL;
		case 103:
			return NivelConstantes.NIVEL_TERCER_LABEL;
		case 104:
			return NivelConstantes.NIVEL_CUARTO_LABEL;
		case 105:
			return NivelConstantes.NIVEL_QUINTO_LABEL;
		case 106:
			return NivelConstantes.NIVEL_SEXTO_LABEL;
		case 107:
			return NivelConstantes.NIVEL_SEPTIMO_LABEL;
		case 108:
			return NivelConstantes.NIVEL_OCTAVO_LABEL;
		default:
			return null;
		}
	}
	
	/**
	 * Genera la lista de items para el combo de seleccion
	 * @return lista de items para el combo de seleccion
	 */
	public List<SelectItem> getTipoSeleccion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE ,GeneralesConstantes.APP_TIPO_SELECCION_TODOS_LABEL));
		retorno.add(new SelectItem(GeneralesConstantes.APP_TIPO_SELECCION_NADA_VALUE,GeneralesConstantes.APP_TIPO_SELECCION_NADA_LABEL));
		
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado de usuario
	 * @return lista de items para el combo estado de usuario
	 */
	public List<SelectItem> getListaEstadoUsuario()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(UsuarioConstantes.ESTADO_ACTIVO_VALUE, UsuarioConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(UsuarioConstantes.ESTADO_INACTIVO_VALUE, UsuarioConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiente el label del estado del usuario
	 * @param estado - estado del usuario a obtener
	 * @return label del valor del usuario
	 */
	public String getLabelEstadoUsuario(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoUsuario()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado de usuario rol
	 * @return lista de items para el combo estado de usuario rol
	 */
	public List<SelectItem> getListaEstadoUsuarioRol()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE, UsuarioRolConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE, UsuarioRolConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiente el label del estado del usuario rol
	 * @param estado - estado del usuario rol a obtener
	 * @return label del valor del usuario rol
	 */
	public String getLabelEstadoUsuarioRol(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoUsuarioRol()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	public String getLabelEstadoComprobante(int estado){
		if(estado==ComprobantePagoConstantes.ESTADO_EMITIDO_VALUE){
			return ComprobantePagoConstantes.ESTADO_EMITIDO_LABEL;
		}else if (estado==ComprobantePagoConstantes.ESTADO_ENVIADO_VALUE){
			return ComprobantePagoConstantes.ESTADO_ENVIADO_LABEL;
		}else{
			return ComprobantePagoConstantes.ESTADO_PAGADO_LABEL;
		}
		
	}
	
	/**
	 * Genera la lista de items para el combo de estado de la tabla matricula
	 * @return lista de items para el combo de estado de la tabla matricula 
	 */
	public List<SelectItem> getListaEstadoMatricula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MatriculaConstantes.ESTADO_MATRICULA_ACTIVO_VALUE,MatriculaConstantes.ESTADO_MATRICULA_ACTIVO_LABEL));
		retorno.add(new SelectItem(MatriculaConstantes.ESTADO_MATRICULA_INACTIVO_VALUE,MatriculaConstantes.ESTADO_MATRICULA_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el valor de estado de la tabla  Matricula segun su id
	 * @param tipoId - tipoId del estado de la tabla Matricula
	 * @return el label del estado de matricula.
	 */
	public String getEstadoMatricula(Integer tipoId){
		String retorno = null;
		if(tipoId != null){
			for (SelectItem item : getListaEstadoMatricula()) {
				if(((Integer)item.getValue()).intValue() == tipoId){
					retorno = item.getLabel();
				}
			}
		}else{
			retorno = "";
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de tipo matricula
	 * @return lista de items para el combo de tipo matricula 
	 */
	public List<SelectItem> getListaTipoMatricula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE,MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_LABEL));
		retorno.add(new SelectItem(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_VALUE,MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_LABEL));
		retorno.add(new SelectItem(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_VALUE,MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el valor de tipo Matricula segun su id
	 * @param tipoId - tipoId del tipo Matricula
	 * @return el label del tipo matricula.
	 */
	public String getTipoMatricula(Integer tipoId){
		String retorno = null;
		if(tipoId != null){
			for (SelectItem item : getListaTipoMatricula()) {
				if(((Integer)item.getValue()).intValue() == tipoId){
					retorno = item.getLabel();
				}
			}
		}else{
			retorno = "";
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de Modalidad matricula
	 * @return lista de items para el combo de Modalidad matricula 
	 */
	public List<SelectItem> getListaModalidadMatricula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MatriculaConstantes.TIPO_MODALIDAD_PRESENCIAL_VALUE,MatriculaConstantes.TIPO_MODALIDAD_PRESENCIAL_LABEL));
		retorno.add(new SelectItem(MatriculaConstantes.TIPO_MODALIDAD_SEMIPRESENCIAL_VALUE,MatriculaConstantes.TIPO_MODALIDAD_SEMIPRESENCIAL_LABEL));
		retorno.add(new SelectItem(MatriculaConstantes.TIPO_MODALIDAD_DISTANCIA_VALUE,MatriculaConstantes.TIPO_MODALIDAD_DISTANCIA_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el valor de Modalidad Matricula segun su id
	 * @param tipoId - tipoId de Modalidad Matricula
	 * @return el label de Modalidad matricula.
	 */
	public String getModalidadMatricula(Integer tipoId){
		String retorno = null;
		if(tipoId != null){
			for (SelectItem item : getListaModalidadMatricula()) {
				if(((Integer)item.getValue()).intValue() == tipoId){
					retorno = item.getLabel();
				}
			}
		}else{
			retorno = "";
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo de Modalidad matricula
	 * @return lista de items para el combo de Modalidad matricula 
	 */
	public List<SelectItem> getListaModalidad()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ModalidadConstantes.TIPO_MODALIDAD_PRESENCIAL_VALUE,ModalidadConstantes.TIPO_MODALIDAD_PRESENCIAL_LABEL));
		retorno.add(new SelectItem(ModalidadConstantes.TIPO_MODALIDAD_SEMIPRESENCIAL_VALUE,ModalidadConstantes.TIPO_MODALIDAD_SEMIPRESENCIAL_LABEL));
		retorno.add(new SelectItem(ModalidadConstantes.TIPO_MODALIDAD_DISTANCIA_VALUE,ModalidadConstantes.TIPO_MODALIDAD_DISTANCIA_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el valor de Modalidad Matricula segun su id
	 * @param tipoId - tipoId de Modalidad Matricula
	 * @return el label de Modalidad matricula.
	 */
	public String getModalidad(Integer tipoId){
		String retorno = null;
		if(tipoId != null){
			for (SelectItem item : getListaModalidad()) {
				if(((Integer)item.getValue()).intValue() == tipoId){
					retorno = item.getLabel();
				}
			}
		}else{
			retorno = "";
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de Vigencia Malla Curricilar
	 * @return lista de items para el combo de Vigencia Malla Curricilar 
	 */
	public List<SelectItem> getListaVigenciaMallaCurricular()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MallaCurricularConstantes.VIGENTE_MALLA_SI_VALUE,MallaCurricularConstantes.VIGENTE_MALLA_SI_LABEL));
		retorno.add(new SelectItem(MallaCurricularConstantes.VIGENTE_MALLA_NO_VALUE,MallaCurricularConstantes.VIGENTE_MALLA_NO_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de Vigencia Malla Curricilar
	 * @return lista de items para el combo de Vigencia Malla Curricilar 
	 */
	public List<SelectItem> getListaEstadoMallaCurricular()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE,MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_LABEL));
		retorno.add(new SelectItem(MallaCurricularConstantes.ESTADO_MALLA_INACTIVO_VALUE,MallaCurricularConstantes.ESTADO_MALLA_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de Vigencia Malla Curricilar
	 * @return lista de items para el combo de Vigencia Malla Curricilar 
	 */
	public List<SelectItem> getListaOrgAprendizajeMallaCurricular()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MallaCurricularConstantes.TIPO_ORG_APREN_1_VALUE,MallaCurricularConstantes.TIPO_ORG_APREN_1_LABEL));
		retorno.add(new SelectItem(MallaCurricularConstantes.TIPO_ORG_APREN_2_VALUE,MallaCurricularConstantes.TIPO_ORG_APREN_2_LABEL));
		retorno.add(new SelectItem(MallaCurricularConstantes.TIPO_ORG_APREN_3_VALUE,MallaCurricularConstantes.TIPO_ORG_APREN_3_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de Vigencia Malla Curricilar
	 * @return lista de items para el combo de Vigencia Malla Curricilar 
	 */
	public List<SelectItem> getListaTipoAprovaciónMallaCurricular()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MallaCurricularConstantes.POR_NIVEL_VALUE,MallaCurricularConstantes.POR_NIVEL_LABEL));
		retorno.add(new SelectItem(MallaCurricularConstantes.POR_MATERIA_VALUE,MallaCurricularConstantes.POR_MATERIA_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado de paralelo
	 * @return lista de items para el combo estado de paralelo
	 */
	public List<SelectItem> getListaEstadoParalelo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ParaleloConstantes.ESTADO_ACTIVO_VALUE, ParaleloConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(ParaleloConstantes.ESTADO_INACTIVO_VALUE, ParaleloConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del paralelo
	 * @param estado - estado del paralelo a obtener
	 * @return label del valor del paralelo
	 */
	public String getLabelEstadoParalelo(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoParalelo()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo ficha matricula el nivel
	 * @return lista de items para el combo de nivel de ficha matricula
	 */
	public List<SelectItem> getListaNivelFichaMatricula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(FichaMatriculaConstantes.PRIMER_NIVEL_VALUE, FichaMatriculaConstantes.PRIMER_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.SEGUNDO_NIVEL_VALUE, FichaMatriculaConstantes.SEGUNDO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.TERCERO_NIVEL_VALUE, FichaMatriculaConstantes.TERCERO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.CUARTO_NIVEL_VALUE, FichaMatriculaConstantes.CUARTO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.QUINTO_NIVEL_VALUE, FichaMatriculaConstantes.QUINTO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.SEXTO_NIVEL_VALUE, FichaMatriculaConstantes.SEXTO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.SEPTIMO_NIVEL_VALUE, FichaMatriculaConstantes.SEPTIMO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.OCTAVO_NIVEL_VALUE, FichaMatriculaConstantes.OCTAVO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.NOVENO_NIVEL_VALUE, FichaMatriculaConstantes.NOVENO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.DECIMO_NIVEL_VALUE, FichaMatriculaConstantes.DECIMO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.DECIMO_PRIMERO_NIVEL_VALUE, FichaMatriculaConstantes.DECIMO_PRIMERO_NIVEL_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.NIVELACION_VALUE, FichaMatriculaConstantes.NIVELACION_LABEL));
		retorno.add(new SelectItem(FichaMatriculaConstantes.APROBACION_VALUE, FichaMatriculaConstantes.APROBACION_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del nivel de ficha matricula
	 * @param estado - estado del nivel de ficha matricula
	 * @return label del valor del nivel
	 */
	public String getListaNivelFichaMatricula(int estado){
		String retorno = null;
		for (SelectItem item : getListaNivelFichaMatricula()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo detalle matrícula el número matrícula
	 * @return lista de items para el combo detalle matrícula el número matrícula
	 */
	public List<SelectItem> getListaNumeroMatriculaDetalleMatricula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE, DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE, DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE, DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label de numero de matricula de detalle matricula
	 * @param estado - estado del numero de matricula de detalle matricula
	 * @return label del valor del numero de matricula
	 */
	public String getListaNumeroMatriculaDetalleMatricula(int estado){
		String retorno = null;
		for (SelectItem item : getListaNumeroMatriculaDetalleMatricula()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado record estudiantil
	 * @return lista de items para el combo estado record estudiantil
	 */
	public static List<SelectItem> getListaEstadoRecorAcademico()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE, RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE, RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE, RecordEstudianteConstantes.ESTADO_APROBADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE, RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_MIGRADO_SAU_VALUE, RecordEstudianteConstantes.ESTADO_MIGRADO_SAU_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_CONVALIDADO_VALUE, RecordEstudianteConstantes.ESTADO_CONVALIDADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE, RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE, RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE, RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_VALUE, RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_FORTUITAS_LABEL));
		
			
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado record estudiantil
	 * @param estado - estado del record estudiantil
	 * @return label del valor del estado del record estudiantil
	 */
	public static String getListaEstadoRecorAcademico(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoRecorAcademico()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo tipo de aula
	 * @return lista de items para el combo tipo de aula
	 */
	public List<SelectItem> getListaTipoAula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(AulaConstantes.TIPO_AULA_VALUE, AulaConstantes.TIPO_AULA_LABEL));
		retorno.add(new SelectItem(AulaConstantes.TIPO_LABORATORIO_VALUE, AulaConstantes.TIPO_LABORATORIO_LABEL));
		
		return retorno;
	}
	
	/**
	 * Obtiene el label del tipo de aula
	 * @param estado - tipo de aula
	 * @return label del valor del tipo de aula
	 */
	public String getLabelTipoAula(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoAula()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}

	
	/**
	 * Genera la lista de items para el combo estado periodo academico
	 * @return lista de items para el combo estado periodo academico
	 */
	public List<SelectItem> getListEstadoPeriodoAcademico()	{
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_INACTIVO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_LABEL));
		return retorno;
	}
	 
	/**
	 * Obtiene el label del estado del periodo academico
	 * @param estado - estado del periodo academico
	 * @return label del valor del estado del periodo academico
	 */
	public String getLabelEstadoPeriodoAcademico(int param){
		String retorno = "";

		for (SelectItem item : getListEstadoPeriodoAcademico()) {
			if(((Integer)item.getValue()).intValue() == param){
				retorno = item.getLabel();
			}
		}

		return retorno;
	}

	/**
	 * Genera la lista de items para el combo estado materia
	 * @return lista de items para el combo estado materia
	 */
	public List<SelectItem> getListaEstadoMateria()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_ACTIVO_LABEL));
		retorno.add(new SelectItem(MateriaConstantes.ESTADO_MATERIA_INACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado materia
	 * @param estado - estado de la materia
	 * @return label del valor del estado de la materia
	 */
	public String getLabelEstadoMateria(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoMateria()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado materia
	 * @param estado - estado de la materia
	 * @return label del valor del estado de la materia
	 */
	public String getLabelFcinMatriculado(int estado){
		if(estado==0){
			return "MATRICULADO";
		}else{
			return "NO MATRICULADO";
		}
	}
	
	
	/**
	 * Genera la lista de items para el combo estado malla curricular materia
	 * @return lista de items para el combo estado malla curricular materia
	 */
	public List<SelectItem> getListaEstadoMallaCurricularMateria()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE, MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_LABEL));
		retorno.add(new SelectItem(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_INACTIVO_VALUE, MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado malla curricular materia
	 * @param estado - estado de la  malla curricular materia
	 * @return label del valor del estado de la  malla curricular materia
	 */
	public String getLabelEstadoMallaCurricularMateria(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoMallaCurricularMateria()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Genera la lista de items para el combo Relacion de Trabajo de la materia
	 * @return lista de items para el combo Relacion de Trabajo de la materia
	 */
	public List<SelectItem> getListaRelacionTrabajoMateria()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(MateriaConstantes.RELACIONTRABAJO_MATERIA_1_1_5_VALUE, MateriaConstantes.RELACIONTRABAJO_MATERIA_1_1_5_LABEL));
		retorno.add(new SelectItem(MateriaConstantes.RELACIONTRABAJO_MATERIA_1_2_VALUE, MateriaConstantes.RELACIONTRABAJO_MATERIA_1_2_LABEL));
		retorno.add(new SelectItem(MateriaConstantes.RELACIONTRABAJO_MATERIA_OTROS_VALUE, MateriaConstantes.RELACIONTRABAJO_MATERIA_OTROS_LABEL));
		
		return retorno;
	}
	
	/**
	 * Obtiene el label del Relacion de Trabajo de la materia
	 * @param estado - Relacion de Trabajo de la materia
	 * @return label del valor Relacion de Trabajo de la materia
	 */
	public String getLabelRelacionTrabajoMateria(int estado){
		String retorno = null;
		for (SelectItem item : getListaRelacionTrabajoMateria()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo estado  malla - periodo 
	 * @return lista de items para el combo estado malla - periodo 
	 */
	public List<SelectItem> getListaEstadoMallaPeriodo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE, MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_LABEL));
		retorno.add(new SelectItem(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_INACTIVO_VALUE, MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado de la malla - periodo
	 * @param estado - estado de la malla - periodo
	 * @return label del valor del estado de la malla - periodo
	 */
	public String getListaEstadoMallaPeriodo(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoMallaPeriodo()) {
			if(((Integer)item.getValue()).intValue() == tipo){
			}
		}
		return retorno;
	}

	
	/**
	 * Genera la lista de items para el combo estado  malla - periodo 
	 * @return lista de items para el combo estado malla - periodo 
	 */
	public List<SelectItem> getListaTipoNotaSemestre(){
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(RecordEstudianteConstantes.TIPO_NOTA_PRIMER_PARCIAL_VALUE, RecordEstudianteConstantes.TIPO_NOTA_PRIMER_PARCIAL_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.TIPO_NOTA_SEGUNDO_PARCIAL_VALUE, RecordEstudianteConstantes.TIPO_NOTA_SEGUNDO_PARCIAL_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.TIPO_NOTA_RECUPERACION_VALUE, RecordEstudianteConstantes.TIPO_NOTA_RECUPERACION_LABEL));
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo estado del prerequisito
	 * @return lista de items para el combo del prerequisito
	 */
	public List<SelectItem> getListaEstadoPrerrequisito()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PrerequisitoConstantes.ESTADO_PREREQUISITO_ACTIVO_VALUE, PrerequisitoConstantes.ESTADO_PREREQUISITO_ACTIVO_LABEL));
		retorno.add(new SelectItem(PrerequisitoConstantes.ESTADO_PREREQUISITO_INACTIVO_VALUE, PrerequisitoConstantes.ESTADO_PREREQUISITO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del prerrequisito
	 * @param estado - estado de la del prerrequisito
	 * @return label del valor del estado del prerrequisito
	 */
	public String getLabelEstadoPrerrequisito(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoPrerrequisito()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo estado del correquisito
	 * @return lista de items para el combo del prerequisito
	 */
	public List<SelectItem> getListaEstadoCorrequisito()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(CorequisitoConstantes.ESTADO_COREQUISITO_ACTIVO_VALUE, CorequisitoConstantes.ESTADO_COREQUISITO_ACTIVO_LABEL));
		retorno.add(new SelectItem(CorequisitoConstantes.ESTADO_COREQUISITO_INACTIVO_VALUE, CorequisitoConstantes.ESTADO_COREQUISITO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del correquisito
	 * @param estado - estado de la del correquisito
	 * @return label del valor del estado del correquisito
	 */
	public String getLabelEstadoCorrequisito(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoCorrequisito()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado del edificio
	 * @return lista de items para el combo del estado de edificio
	 */
	public List<SelectItem> getListaEstadoEdificio()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(EdificioConstantes.ESTADO_ACTIVO_VALUE, EdificioConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(EdificioConstantes.ESTADO_INACTIVO_VALUE, EdificioConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del edificio
	 * @param estado - estado de la del edificio
	 * @return label del valor del estado del edificio
	 */
	public String getLabelEstadoEdificio(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoCorrequisito()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado de aula
	 * @return lista de items para el combo estado de aula
	 */
	public List<SelectItem> getListaEstadoAula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(AulaConstantes.ESTADO_ACTIVO_VALUE, AulaConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(AulaConstantes.ESTADO_INACTIVO_VALUE, AulaConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del tipo de aula
	 * @param estado - tipo de aula
	 * @return label del valor del tipo de aula
	 */
	public String getLabelEstadoAula(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoAula()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo dia de la semana en horario academico
	 * @return lista de items para el combo estado de aula
	 */
	public List<SelectItem> getListaDiaHorarioAcademico()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_LUNES_VALUE, HorarioAcademicoConstantes.DIA_LUNES_LABEL));
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_MARTES_VALUE, HorarioAcademicoConstantes.DIA_MARTES_LABEL));
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE, HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL));
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_JUEVES_VALUE, HorarioAcademicoConstantes.DIA_JUEVES_LABEL));
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_VIERNES_VALUE, HorarioAcademicoConstantes.DIA_VIERNES_LABEL));
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_SABADO_VALUE, HorarioAcademicoConstantes.DIA_SABADO_LABEL));
		retorno.add(new SelectItem(HorarioAcademicoConstantes.DIA_DOMINGO_VALUE, HorarioAcademicoConstantes.DIA_DOMINGO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del dia de la semana en horario academico
	 * @param tipo - dia de la semana
	 * @return label del valor del tipo de aula
	 */
	public String getLabelDiaHorarioAcademico(int tipo){
		String retorno = null;
		for (SelectItem item : getListaDiaHorarioAcademico()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo número matrícula homologación.
	 * @return lista de items para el combo número matrícula homologación.
	 */
	public List<SelectItem> getListaNumeroMatriculaHomologacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE, DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE, DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL));
		
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo aprobado o negada la solicitud de retiro de matricula
	 * @return lista de items para el combo aprobado o negada la solicitud de retiro de matricula
	 */
	public List<SelectItem> getListaEstadoSolicitudRetiro()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(0, "APROBAR"));
		retorno.add(new SelectItem(1, "NEGAR"));
		
		return retorno;
	}
	
	/**
	 * Obtiene el label de número de matrícula de homologación
	 * @param estado - estado del número de matrícula de homologación
	 * @return label del valor del número de matrícula
	 */
	public String getLabelNumeroMatriculaHomologacion(int estado){
		String retorno = null;
		for (SelectItem item : getListaNumeroMatriculaHomologacion()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo discapacidad de persona.
	 * @return lista de items para el combo discapacidad de persona.
	 */
	public List<SelectItem> getListaPresentaDiscapacidad()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PersonaConstantes.DISCAPACIDAD_SI_VALUE, PersonaConstantes.DISCAPACIDAD_SI_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.DISCAPACIDAD_NO_VALUE, PersonaConstantes.DISCAPACIDAD_NO_LABEL));
		
		return retorno;
	}
	
	/**
	 * Obtiene el label si presenta o no discapacidad
	 * @param estado - estado de discapacidad
	 * @return label discapacidad
	 */
	public String getLabelPresentaDiscapacidad(int estado){
		String retorno = null;
		for (SelectItem item : getListaPresentaDiscapacidad()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo discapacidad de persona.
	 * @return lista de items para el combo discapacidad de persona.
	 */
	public List<SelectItem> getListaTieneCarnetConadis()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
	
		retorno.add(new SelectItem(PersonaConstantes.CARNET_CONADIS_SI_VALUE, PersonaConstantes.CARNET_CONADIS_SI_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.CARNET_CONADIS_NO_VALUE, PersonaConstantes.CARNET_CONADIS_NO_LABEL));
		
		return retorno;
	}
	
	/**
	 * Obtiene el label si presenta o no discapacidad
	 * @param estado - estado de discapacidad
	 * @return label discapacidad
	 */
	public String getLabelTieneCarnetConadis(int estado){
		String retorno = null;
		for (SelectItem item : getListaTieneCarnetConadis()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo tipo discapacidad de la persona.
	 * @return lista de items para el combo  tipo discapacidad de  la persona.
	 */
	public List<SelectItem> getListaTipoDiscapacidad()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PersonaConstantes.TIPO_DISCAPACIDAD_AUDITIVA_VALUE, PersonaConstantes.TIPO_DISCAPACIDAD_AUDITIVA_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.TIPO_DISCAPACIDAD_FISICA_VALUE, PersonaConstantes.TIPO_DISCAPACIDAD_FISICA_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.TIPO_DISCAPACIDAD_INTELECTUAL_VALUE, PersonaConstantes.TIPO_DISCAPACIDAD_INTELECTUAL_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.TIPO_DISCAPACIDAD_VISUAL_VALUE, PersonaConstantes.TIPO_DISCAPACIDAD_VISUAL_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del tipo de  discapacidad
	 * @param estado - estado del tipo de discapacidad
	 * @return label tipo de discapacidad
	 */
	public String getLabelTipoDiscapacidad(int estado){
		String retorno = null;
		for (SelectItem item : getListaTipoDiscapacidad()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo etnias.
	 * @return lista de items para el combo  etnias.
	 */
	public List<SelectItem> getListaEtnias()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_INDIGENA_VALUE, EtniaConstantes.ETNIA_INDIGENA_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_AFROECUATORIANO_VALUE, EtniaConstantes.ETNIA_AFROECUATORIANO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_NEGRO_VALUE, EtniaConstantes.ETNIA_NEGRO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_MULATO_VALUE, EtniaConstantes.ETNIA_MULATO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_MONTUBIO_VALUE, EtniaConstantes.ETNIA_MONTUBIO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_MESTIZO_VALUE, EtniaConstantes.ETNIA_MESTIZO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_BLANCO_VALUE, EtniaConstantes.ETNIA_BLANCO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_OTRO_VALUE, EtniaConstantes.ETNIA_OTRO_LABEL));
		retorno.add(new SelectItem(EtniaConstantes.ETNIA_DESCONOCIDO_VALUE, EtniaConstantes.ETNIA_DESCONOCIDO_LABEL));
		
		
		return retorno;
	}
	
	/**
	 * Obtiene el label de la etnia
	 * @param estado - estado de la etnia
	 * @return label de la etnia
	 */
	public String getLabelEtnias(int estado){
		String retorno = null;
		for (SelectItem item : getListaEtnias()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el estado civil.
	 * @return lista de items para el estado civil.
	 */
	public List<SelectItem> getListaEstadoCivil()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
				
		retorno.add(new SelectItem(PersonaConstantes.ESTADO_CIVIL_CASADO_VALUE, PersonaConstantes.ESTADO_CIVIL_CASADO_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.ESTADO_CIVIL_DIVORCIADO_VALUE, PersonaConstantes.ESTADO_CIVIL_DIVORCIADO_LABEL));
//		retorno.add(new SelectItem(PersonaConstantes.ESTADO_CIVIL_SEPARADO_VALUE, PersonaConstantes.ESTADO_CIVIL_SEPARADO_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.ESTADO_CIVIL_SOLTERO_VALUE, PersonaConstantes.ESTADO_CIVIL_SOLTERO_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.ESTADO_CIVIL_UNION_DE_HECHO_VALUE, PersonaConstantes.ESTADO_CIVIL_UNION_DE_HECHO_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.ESTADO_CIVIL_VIUDO_VALUE, PersonaConstantes.ESTADO_CIVIL_VIUDO_LABEL));
		
				
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado civil
	 * @param estado - estado del estado civil
	 * @return label de la etnia
	 */
	public String getLabelEstadoCivil(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoCivil()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	

		
		
		/**
		 * Genera la lista de items para el tipo de institucion académica
		 * @return lista de items para el tipo de institucion académica.
		 */
		public List<SelectItem> getListaTipoInstitucionAcademica()	{
			List<SelectItem> retorno = new ArrayList<SelectItem>();
			retorno = new ArrayList<SelectItem>();
			retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_FISCAL_VALUE, InstitucionAcademicaConstantes.TIPO_FISCAL_LABEL));
			retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_FISCOMISIONAL_VALUE, InstitucionAcademicaConstantes.TIPO_FISCOMISIONAL_LABEL));
			retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_PARTICULAR_VALUE, InstitucionAcademicaConstantes.TIPO_PARTICULAR_LABEL));
			retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_MUNICIPAL_VALUE, InstitucionAcademicaConstantes.TIPO_MUNICIPAL_LABEL));
			return retorno;
		}
		
		/**
		 * Obtiene el label del tipo de institucion académica
		 * @param estado - estado del tipo de institucion académica
		 * @return label del tipo de institucion académica
		 */
		public String getLabelTipoInstitucionAcademica(int estado){
			String retorno = null;
			for (SelectItem item : getListaTipoInstitucionAcademica()) {
				if(((Integer)item.getValue()).intValue() == estado){
					retorno = item.getLabel();
				}
			}
			return retorno;
		}
		
		/**
		 * Genera la lista de items para el nivel de institucion académica.
		 * @return lista de items para el tipo de institucion académica.
		 */
		public List<SelectItem> getListaNivelInstitucionAcademica()	{
			List<SelectItem> retorno = new ArrayList<SelectItem>();
			retorno = new ArrayList<SelectItem>();
			retorno.add(new SelectItem(InstitucionAcademicaConstantes.NIVEL_SECUNDARIA_VALUE, InstitucionAcademicaConstantes.NIVEL_SECUNDARIA_LABEL));
			retorno.add(new SelectItem(InstitucionAcademicaConstantes.NIVEL_UNIVERSIDAD_VALUE, InstitucionAcademicaConstantes.NIVEL_UNIVERSIDAD_LABEL));
			
			return retorno;
		}
		
		/**
		 * Obtiene el label del nivel de institucion académica
		 * @param estado - estado del nivel de institucion académica
		 * @return label del nivel de institucion académica
		 */
		public String getLabelNivelInstitucionAcademica(int estado){
			String retorno = null;
			for (SelectItem item : getListaNivelInstitucionAcademica()) {
				if(((Integer)item.getValue()).intValue() == estado){
					retorno = item.getLabel();
				}
			}
			return retorno;
		}
//	/**
//	 * Genera la lista de items para el combo sexo para los titulos
//	 * @return lista de items para el combo sexo para los titulos
//	 */
//	public List<SelectItem> getListaGeneroTitulo()	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		retorno.add(new SelectItem(TituloConstantes.GENERO_MASCULINO_VALUE,TituloConstantes.GENERO_MASCULINO_LABEL));
//		retorno.add(new SelectItem(TituloConstantes.GENERO_FEMENINO_VALUE,TituloConstantes.GENERO_FEMENINO_LABEL));
//		retorno.add(new SelectItem(TituloConstantes.GENERO_ESTANDAR_VALUE,TituloConstantes.GENERO_ESTANDAR_LABEL));
//		return retorno;
//	}
//	
//	/**
//	 * Obtiene el valor de genero titulo sugun su id
//	 * @param generoId - generoId del genero del titulo
//	 * @return el label del genero del titulo como string.
//	 */
//	public String getGeneroTitulo(Integer generoId){
//		String retorno = null;
//		if(generoId != null){
//			for (SelectItem item : getListaGeneroTitulo()) {
//				if(((Integer)item.getValue()).intValue() == generoId){
//					retorno = item.getLabel();
//				}
//			}
//		}else{
//			retorno = "";
//		}
//		return retorno;
//	}
//	
//	/**
//	 * Genera la lista de items para el combo Tipo duracion reconocimiento
//	 * @return lista de items para el combo Tipo duracion reconocimiento
//	 */
//	public List<SelectItem> getListaTipoDuracionReconocimiento()	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE,FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL));
//		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE,FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL));
//		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE,FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL));
//		return retorno;
//	}
//	
//	/**
//	 * Genera la lista de items para el combo Tipo colegio
//	 * @return lista de items para el combo Tipo colegio
//	 */
//	public List<SelectItem> getListaTipoColegio()	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_FISCAL_VALUE ,InstitucionAcademicaConstantes.TIPO_FISCAL_LABEL));
//		retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_FISCOMICIONAL_VALUE ,InstitucionAcademicaConstantes.TIPO_FISCOMICIONAL_LABEL ));
//		retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_MUNICIPAL_VALUE,InstitucionAcademicaConstantes.TIPO_MUNICIPAL_LABEL));
//		retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_PARTICULAR_VALUE,InstitucionAcademicaConstantes.TIPO_PARTICULAR_LABEL));
//		return retorno;
//	}
	
//	/**
//	 * Obtiene el valor de la variable tipo colegio
//	 * @param estado - id del tipo de colegio para obtener el label del tipo de colegio
//	 * @return el label de la variable id colegio.
//	 */
//	public String getLabelTipoColegio(int idTipoColegio){
//		String retorno = null;
//		for (SelectItem item : getListaTipoColegio()) {
//			if(((Integer)item.getValue()).intValue() == idTipoColegio){
//				retorno = item.getLabel();
//			}
//		}
//		return retorno;
//	}
	

	
//	/**
//	 * Genera la lista de items para los estado de tramite de titulo
//	 * @return lista de items para los estado de tramite de titulo
//	 */
//	public List<SelectItem> getListarEstadosProcesoTramite()	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_PENDIENTE_ENVIO_SECRETARIA_VALUE,TramiteTituloConstantes.ESTADO_PENDIENTE_ENVIO_SECRETARIA_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_PENDIENTE_VALIDACION_SECR_ABG_VALUE,TramiteTituloConstantes.ESTADO_PENDIENTE_VALIDACION_SECR_ABG_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_ACEPTADO_SECR_ABG_VALUE,TramiteTituloConstantes.ESTADO_ACEPTADO_SECR_ABG_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_RECHAZADO_SECR_ABG_VALUE,TramiteTituloConstantes.ESTADO_RECHAZADO_SECR_ABG_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SECR_ABG_VALUE,TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SECR_ABG_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_PENDIENTE_GENERAR_CSV_VALUE,TramiteTituloConstantes.ESTADO_PENDIENTE_GENERAR_CSV_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_PENDIENTE_VERIFICAR_CARGA_VALUE,TramiteTituloConstantes.ESTADO_PENDIENTE_VERIFICAR_CARGA_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_VERIFICADO_OK_VALUE,TramiteTituloConstantes.ESTADO_VERIFICADO_OK_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_VERIFICADO_ERROR_VALUE,TramiteTituloConstantes.ESTADO_VERIFICADO_ERROR_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SENESCYT_VALUE,TramiteTituloConstantes.ESTADO_PENDIENTE_CORRECION_RECHAZADO_SENESCYT_LABEL));
//		retorno.add(new SelectItem(TramiteTituloConstantes.ESTADO_TITULO_GENERADO_VALUE,TramiteTituloConstantes.ESTADO_TITULO_GENERADO_LABEL));
//		return retorno;
//	}
	
//	/**
//	 * Obtiente el label del estado del proceso trámite
//	 * @param estado - estado de tramite
//	 * @return label del valor del estafo del proceso trámite
//	 */
//	public String getLabelEstadoProcesoTramite(int estado){
//		String retorno = null;
//		for (SelectItem item : getListarEstadosProcesoTramite()) {
//			if(((Integer)item.getValue()).intValue() == estado){
//				retorno = item.getLabel();
//			}
//		}
//		return retorno;
//	}
	
	
	
	
//	/**
//	 * Genera la lista de items para los estado de tramite de titulo por ejecucion proceso
//	 * @return lista de items para los estado de tramite de titulo
//	 */
//	public List<SelectItem> getListarEstadosProcesoTramiteXProcesoRol(int procesoRol)	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		
//		for (Integer item : GeneralesUtilidades.listarEstadosProcesoTramiteXProcesoRol(procesoRol)) {
//			retorno.add(getSelectedItemEstadoProcesoTramite(item));
//		}
//		return retorno;
//	}
//	
//	/**
//	 * Obtiene el SelectItem de acuerdo al estado de tramite de titulo
//	 * @param estado - estado de tramite de titulo como entero
//	 * @return el label del estado de tramite de titulo como string.
//	 */
//	private SelectItem getSelectedItemEstadoProcesoTramite(Integer estado){
//		SelectItem retorno = null;
//		if(estado != null){
//			for (SelectItem item : getListarEstadosProcesoTramite()) {
//				if(((Integer)item.getValue()).intValue() == estado){
//					retorno = item;
//				}
//			}
//		}else{
//			retorno = null;
//		}
//		return retorno;
//	}
//	/**
//	 * Obtiene el valor de acuerdo al estado de tramite de titulo
//	 * @param estado - estado de tramite de titulo como entero
//	 * @return el label del estado de tramite de titulo como string.
//	 */
//	public String getEstadoProcesoTramite(Integer estado){
//		String retorno = null;
//		if(estado != null){
//			for (SelectItem item : getListarEstadosProcesoTramite()) {
//				if(((Integer)item.getValue()).intValue() == estado){
//					retorno = item.getLabel();
//				}
//			}
//		}else{
//			retorno = "";
//		}
//		return retorno;
//	}
	

	
//	/**
//	 * Genera la lista de items para el combo tipo de la carrera
//	 * @return lista de items para el combo tipo de la carrera
//	 */
//	public List<SelectItem> getListaTipoCarrera()	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		retorno.add(new SelectItem(DuracionConstantes.TIPO_CARRERA_ANIOS_VALUE,DuracionConstantes.TIPO_CARRERA_ANIOS_LABEL));
//		retorno.add(new SelectItem(DuracionConstantes.TIPO_CARRERA_SEMESTRES_VALUE,DuracionConstantes.TIPO_CARRERA_SEMESTRES_LABEL));
//		retorno.add(new SelectItem(DuracionConstantes.TIPO_CARRERA_CREDITOS_VALUE,DuracionConstantes.TIPO_CARRERA_CREDITOS_LABEL));
//		return retorno;
//	}
//	
//	/**
//	 * Obtiene el valor de tipo de carrera segun su id
//	 * @param tipoId - tipoId del tipo de carrera
//	 * @return el label del tipo de carrera como string.
//	 */
//	public String getTipoCarrera(Integer tipoId){
//		String retorno = null;
//		if(tipoId != null){
//			for (SelectItem item : getListaGeneroTitulo()) {
//				if(((Integer)item.getValue()).intValue() == tipoId){
//					retorno = item.getLabel();
//				}
//			}
//		}else{
//			retorno = "";
//		}
//		return retorno;
//	}
	
	/**
	 * Genera la lista de items para el combo tipo de Nivel (Grado)
	 * @return lista de items para el combo tipo de Nivel (Grado)
	 */
	public List<SelectItem> getListaTipoGrado()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.PRAC_PREGRADO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE, PeriodoAcademicoConstantes.PRAC_POSGRADO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiente el label del estado del tipo de Nivel (Grado)
	 * @param tipo - tipo de Nivel (Grado) a obtener
	 * @return label del valor del tipo de Nivel (Grado)
	 */
	public String getLabelTipoGrado(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoGrado()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo de solicitudes para los reportes
	 * @return lista de items para el combo tipo de solicitudes para los reportes
	 */
	public List<SelectItem> getListaTipoSolicitudes()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(1,"ANULACION DE MATERIAS"));
		retorno.add(new SelectItem(2,"TERCERAS MATRICULAS"));
		retorno.add(new SelectItem(3,"RECTIFICACION DE NOTAS"));
		retorno.add(new SelectItem(4,"NOTAS ATRASADAS"));
		return retorno;
	}
	
	/**
	 * Obtiente el label del tipo solicitudes para los reportes
	 * @param tipo - tipo de solicitudes para los reportes
	 * @return label del valor del tipo solicitudes para los reportes
	 */
	public String getLabelTipoSolicitudes(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoSolicitudes()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo de Nivel (Grado)
	 * @return lista de items para el combo tipo de Nivel (Grado)
	 */
	public List<SelectItem> getListaTipoFactura()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_PRIMERA_MATRICULA_VALUE,ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_PRIMERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE,ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_LABEL));
		retorno.add(new SelectItem(ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_TERCERA_MATRICULA_VALUE,ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_TERCERA_MATRICULA_LABEL));
		return retorno;
	}
	
	
//	/**
//	 * Genera la lista de items para el combo estado periodo academico
//	 * @return lista de items para el combo estado periodo academico
//	 */
//	public List<SelectItem> getListaTipoCronograma(int usuario)	{
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		retorno = new ArrayList<SelectItem>();
//		if(usuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()){
//			retorno.add(new SelectItem(CronogramaConstantes.TIPO_ACADEMICO_VALUE, CronogramaConstantes.TIPO_ACADEMICO_LABEL));
//			retorno.add(new SelectItem(CronogramaConstantes.TIPO_NIVELACION_VALUE, CronogramaConstantes.TIPO_NIVELACION_LABEL));
//			
//		}
//		if(usuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
//			retorno.add(new SelectItem(CronogramaConstantes.TIPO_POSGRADO_VALUE, CronogramaConstantes.TIPO_POSGRADO_LABEL));
//		}
//		return retorno;
//	}
//	
//	/**
//	 * Obtiene el label del estado del periodo academico
//	 * @param estado - estado del periodo academico
//	 * @return label del valor del estado del periodo academico
//	 */
//	public String getListaTipoCronograma(int tipo, int usuario){
//		String retorno = null;
//		for (SelectItem item : getListaTipoCronograma(usuario)) {
//			if(((Integer)item.getValue()).intValue() == tipo){
//				retorno = item.getLabel();
//			}
//		}
//		return retorno;
//	}
	
	/**
	 * Genera la lista de items para el combo tipo cronograma pregrado
	 * @return lista de items para el combo tipo cronograma pregrado
	 */
	public List<SelectItem> getListaTipoCronogramaPregrado()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
			retorno.add(new SelectItem(CronogramaConstantes.TIPO_ACADEMICO_VALUE, CronogramaConstantes.TIPO_ACADEMICO_LABEL));
			retorno.add(new SelectItem(CronogramaConstantes.TIPO_NIVELACION_VALUE, CronogramaConstantes.TIPO_NIVELACION_LABEL));
			retorno.add(new SelectItem(CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_VALUE, CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del tipo cronograma pregrado
	 * @param estado - estado del tipo cronograma pregrado
	 * @return label del valor del estado del tipo cronograma pregrado
	 */
	public String getLabelListaTipoCronogramaPregrado(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoCronogramaPregrado()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo cronograma posgrado
	 * @return lista de items para el combo tipo cronograma posgrado
	 */
	public List<SelectItem> getListaTipoCronogramaPosgrado()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(CronogramaConstantes.TIPO_POSGRADO_VALUE, CronogramaConstantes.TIPO_POSGRADO_LABEL));	
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del tipo cronograma posgrado
	 * @param estado - estado del tipo cronograma posgrado
	 * @return label del valor del estado del tipo cronograma posgrado
	 */
	public String getLabelListaTipoCronogramaPosgrado(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoCronogramaPosgrado()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado proceso flujo
	 * @return lista de items para el combo estado proceso flujo
	 */
	public List<SelectItem> getListaEstadoPlanificacionCronograma()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE, PlanificacionCronogramaConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(PlanificacionCronogramaConstantes.ESTADO_INACTIVO_VALUE, PlanificacionCronogramaConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del estado proceso flujo
	 * @param estado - estado del estado proceso flujo
	 * @return label del valor del estado del estado proceso flujo
	 */
	public String getLabelListaEstadoPlanificacionCronograma(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoPlanificacionCronograma()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado Evaluacion docente
	 * @return lista de items para el combo estado Evaluacion docente
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public List<SelectItem> getListaEstadoEvaluacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(EvaluacionConstantes.ESTADO_ACTIVO_VALUE, EvaluacionConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(EvaluacionConstantes.ESTADO_INACTIVO_VALUE, EvaluacionConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	 
	
	
	/**
	 * Genera la lista de items para el combo estado solicitudesTerceraMatricula 
	 * @return lista de items para el combo estado solicitudesTerceraMatricula 
	 */
	public List<SelectItem> getListaEstadoSolicitudTerceraMatriSltrmt()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();

		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL));

		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del estado estado solicitudesTerceraMatricula 
	 * @param tipo - tipo del estado solicitudesTerceraMatricula
	 * @return label del valor del estado solicitudesTerceraMatricula 
	 */
	public String getLabelListaEstadoSolicitudTerceraMatriSltrmt(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoSolicitudTerceraMatriSltrmt()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 * @return lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 */
	public List<SelectItem> getListaApruebaSolicitudTerceraMatriSltrmt()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL));
		
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 * @return lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 */
	public List<SelectItem> getListaApruebaApelacionTerceraMatriSltrmt()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL));
		
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo estado TipoIngresoHomologacion FCIN
	 * @return lista de items para el combo estado TipoIngresoHomologacion 
	 */
	public List<SelectItem> getListaTipoIngresoHomologacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_LABEL));
		
		return retorno;
	}
	
	
	/**
	 * Obtiene el label del estado del estado estado tipos de ingreso FCIN
	 * @param tipo - tipo del tipos de ingreso
	 * @return label del valor del tipos de ingreso
	 */
	public String getLabelTipoIngresoHomologacion(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoIngresoHomologacion()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	

	/**
	 * Genera la lista de items para el combo estado TipoIngresoHomologacion FCIN
	 * @return lista de items para el combo estado TipoIngresoHomologacion 
	 */
	public List<SelectItem> getListaEstadoIngresoHomologacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE, FichaInscripcionConstantes.ESTADO_HOMOLOGADO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE, FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_LABEL));
		
		return retorno;
	}
	
	
	/**
	 * Obtiene el label del estado del estado estado tipos de ingreso FCIN
	 * @param tipo - tipo del tipos de ingreso
	 * @return label del valor del tipos de ingreso
	 */
	public String getLabelEstadoIngresoHomologacion(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoIngresoHomologacion()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo  Tipouniversidad
	 * @return lista de items para el combo  Tipouniversidad 
	 */
	public List<SelectItem> getListaTipoUnivEstudPrev()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE, FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_LABEL));
		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PRIVADA_VALUE, FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PRIVADA_LABEL));
		
		return retorno;
	}
	
	
	/**
	 * Obtiene el label del estado del estado Tipouniversidad FCIN
	 * @param tipo - tipo del Tipouniversidad
	 * @return label del valor del Tipouniversidad
	 */
	public String getLabelTipoUnivEstudPrev(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoUnivEstudPrev()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	

	/**
	 * Genera la lista de items para el combo  Tipouniversidad
	 * @return lista de items para el combo  Tipouniversidad 
	 */
	public List<SelectItem> getListaEstadoEstudPrev()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(FichaEstudianteConstantes.ESTADO_ESTUD_PREV_TITULADO_VALUE, FichaEstudianteConstantes.ESTADO_ESTUD_PREV_TITULADO_LABEL));
		retorno.add(new SelectItem(FichaEstudianteConstantes.ESTADO_ESTUD_PREV_CURSANDO_VALUE, FichaEstudianteConstantes.ESTADO_ESTUD_PREV_CURSANDO_LABEL));
		
		return retorno;
	}
	
	
	/**
	 * Obtiene el label del estado del estado Tipouniversidad FCIN
	 * @param tipo - tipo del Tipouniversidad
	 * @return label del valor del Tipouniversidad
	 */
	public String getLabelEstadoEstudPrev(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoEstudPrev()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Genera la lista de items para el combo estado del rol
	 * @return lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 */
	public String getListaEstadoRolDescripcion(int rolEstado)	{
		if(rolEstado==RolConstantes.ROL_ADMIN_VALUE){
			return "ADMINISTRADOR";
		}else if(rolEstado==RolConstantes.ROL_ADMINDGA_VALUE){
			return "ADMINISTRADOR DGA";
		}else if(rolEstado==RolConstantes.ROL_ADMINDPP_VALUE){
			return "ADMINISTRADOR PSGRADO";
		}else if(rolEstado==RolConstantes.ROL_ADMINFACULTAD_VALUE){
			return "ADMINISTRADOR FACULTAD";
		}else if(rolEstado==RolConstantes.ROL_DIRCARRERA_VALUE){
			return "DIRECTOR DE CARRERA";
		}else if(rolEstado==RolConstantes.ROL_DIRPOSGRADO_VALUE){
			return "DIRECTOR DE POSGRADO";
		}else if(rolEstado==RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE){
			return "DIRECTOR DE PROGRAMA DE POSGRADO";
		}else if(rolEstado==RolConstantes.ROL_DOCENTE_VALUE){
			return "DOCENTE";
		}else if(rolEstado==RolConstantes.ROL_ESTUD_PREGRADO_VALUE){
			return "ESTUDIANTE DE PREGRADO";
		}else if(rolEstado==RolConstantes.ROL_ESTUD_VALUE){
			return "ESTUDIANTE";
		}else if(rolEstado==RolConstantes.ROL_ESTUDIANTEPOSGRADO_VALUE){
			return "ESTUDIANTE POSGRADO";
		}else if(rolEstado==RolConstantes.ROL_SECREABOGADO_VALUE){
			return "SECRETARIO ABOGADO";
		}else if(rolEstado==RolConstantes.ROL_SECRECARRERA_VALUE){
			return "SECRETARIA DE CARRERA";
		}else if(rolEstado==RolConstantes.ROL_ADMINNIVELACION_VALUE){
			return "ADMINISTRADOR DE NIVELACION";
		}else if(rolEstado==24){
			return "ADMINISTRADOR DE REPORTES PREGRADO";
		}else if(rolEstado==RolConstantes.ROL_DIRVINCULACIONSOCIEDAD_VALUE){
			return "DIRECTOR DE VINCULACION CON LA SOCIEDAD";
		}else if(rolEstado==RolConstantes.ROL_USUARIONIVELACION_VALUE){
			return "USUARIO NIVELACION";
		}else if(rolEstado==29){
			return "DECANO";
		}else if(rolEstado==30){
			return "SUBDECANO";
		}else if(rolEstado==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE){
			return "SECRETARIA DE SUFICIENCIA";
		}
		
		return null;
		
		
	}
	

	/**
	 * Genera la lista de items para el combo tipo de solicitud
	 * @return lista de items para el combo tipo de solicitud de tercera matricula
	 */
	public List<SelectItem> getListaTipoSolicitud()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_VALUE, SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.TIPO_APELACION_VALUE, SolicitudTerceraMatriculaConstantes.TIPO_APELACION_LABEL));

		return retorno;
		
	}
	
	
	/**
	 * Obtiente el label del tipo de solicitud de tercera matricula
	 * @param tipoSolicitud - valor de la  solicitud de tercera matricula
	 * @return label del tipo de solicitud de tercera matricula
	 */
	public String getLabelTipoSolicitud(int tipoSolicitud){
		String retorno = null;
		for (SelectItem item : getListaTipoSolicitud()) {
			if(((Integer)item.getValue()).intValue() == tipoSolicitud){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}

	/**
	 * Genera la lista de items para el combo modo de aprobacion de la asignatura
	 * @return lista de items para el combo  de modo de aprobacion de la asignatura 
	 */
	public List<SelectItem> getListaModoAprobacionNota1()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(RecordEstudianteConstantes.MODO_APROBACION_NORMAL_VALUE, RecordEstudianteConstantes.MODO_APROBACION_NORMAL_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.MODO_APROBACION_RECUPERACION_VALUE, RecordEstudianteConstantes.MODO_APROBACION_RECUPERACION_LABEL));
	
		return retorno;
		
	}
	
	
	/**
	 * Obtiente el label del modo de aprobación de la asignatura
	 * @param modoAprobacion - valor del modo de aprobacion de la asignatura
	 * @return label modo de aprobacion
	 */
	public String getListaModoAprobacionNota1(int modoAprobacion){
		String retorno = null;
		for (SelectItem item : getListaModoAprobacionNota1()) {
			if(((Integer)item.getValue()).intValue() == modoAprobacion){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo modo de aprobacion de la asignatura
	 * @return lista de items para el combo  de modo de aprobacion de la asignatura 
	 */
	public List<SelectItem> getListaModoAprobacionNota2()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
	
		retorno.add(new SelectItem(RecordEstudianteConstantes.MODO_APROBACION_RECONOCIMIENTO_VALUE, RecordEstudianteConstantes.MODO_APROBACION_RECONOCIMIENTO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.MODO_APROBACION_OTRA_COHORTE_VALUE, RecordEstudianteConstantes.MODO_APROBACION_OTRA_COHORTE_LABEL));
		
		return retorno;
		
	}
	
	
	/**
	 * Obtiente el label del modo de aprobación de la asignatura
	 * @param modoAprobacion - valor del modo de aprobacion de la asignatura
	 * @return label modo de aprobacion
	 */
	public String getListaModoAprobacionNota2(int modoAprobacion){
		String retorno = null;
		for (SelectItem item : getListaModoAprobacionNota2()) {
			if(((Integer)item.getValue()).intValue() == modoAprobacion){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado del record para Ubicación de nivel en Idiomas
	 * @return lista de items para el combo tipo de aula
	 */
	public List<SelectItem> getListaRecordAcademicoIdiomas()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE, RecordEstudianteConstantes.ESTADO_APROBADO_LABEL));
		
		
		return retorno;
	}
	
	/**
	 * Obtiene el label estado del record para Ubicación de nivel en Idiomas
	 * @param estado - estado del record
	 * @return label del valor del estado del record
	 */
	public String getLabelRecordAcademicoIdiomas(int tipo){
		String retorno = null;
		for (SelectItem item : getListaRecordAcademicoIdiomas()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}

	
	
	/**
	 * Genera la lista de items para el combo tipo de retiro y anulación
	 * @return lista de items para el combo tipo de retiro y anulación
	 */
	public List<SelectItem> getListaTipoRetiroAnulacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(DetalleMatriculaConstantes.TIPO_RETIRO_VOLUNTARIO_VALUE, DetalleMatriculaConstantes.TIPO_RETIRO_VOLUNTARIO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.TIPO_RETIRO_FORTUITO_VALUE, DetalleMatriculaConstantes.TIPO_RETIRO_FORTUITO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.TIPO_ANULACION_VALUE, DetalleMatriculaConstantes.TTIPO_ANULACION_LABEL));

		return retorno;
	}
	
	/**
	 * Obtiene el label  para tipo de retiro y anulación
	 * @param estado - estado del record
	 * @return label del tipo de retiro y anulación
	 */
	public String getLabelTipoRetiroAnulacion(int tipo){
		String retorno = null;
		for (SelectItem item : getListaRecordAcademicoIdiomas()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado del rol
	 * @return lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 */
	public String getListaDescripcionXRolDescripcion(String rolDescripcion)	{
		if(rolDescripcion==RolConstantes.ROL_ADMIN.substring(RolConstantes.ROL_ADMIN.indexOf("_")+1, RolConstantes.ROL_ADMIN.length())){
			return "ADMINISTRADOR";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ADMINDGA.substring(RolConstantes.ROL_ADMINDGA.indexOf("_")+1, RolConstantes.ROL_ADMINDGA.length()))){
			return "ADMINISTRADOR DGA";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ADMINDPP.substring(RolConstantes.ROL_ADMINDPP.indexOf("_")+1, RolConstantes.ROL_ADMINDPP.length()))){
			return "ADMINISTRADOR POSGRADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ADMINFACULTAD.substring(RolConstantes.ROL_ADMINFACULTAD.indexOf("_")+1, RolConstantes.ROL_ADMINFACULTAD.length()))){
			return "ADMINISTRADOR FACULTAD";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DIRCARRERA.substring(RolConstantes.ROL_DIRCARRERA.indexOf("_")+1, RolConstantes.ROL_DIRCARRERA.length()))){
			return "DIRECTOR DE CARRERA";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DIRPOSGRADO.substring(RolConstantes.ROL_DIRPOSGRADO.indexOf("_")+1, RolConstantes.ROL_DIRPOSGRADO.length()))){
			return "DIRECTOR DE POSGRADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DIRCARRERAPOSGRADO.substring(RolConstantes.ROL_DIRCARRERAPOSGRADO.indexOf("_")+1, RolConstantes.ROL_DIRCARRERAPOSGRADO.length()))){
			return "DIRECTOR DE PROGRAMA DE POSGRADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DOCENTE.substring(RolConstantes.ROL_DOCENTE.indexOf("_")+1, RolConstantes.ROL_DOCENTE.length()))){
			return "DOCENTE";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ESTUD_PREGRADO.substring(RolConstantes.ROL_ESTUD_PREGRADO.indexOf("_")+1, RolConstantes.ROL_ESTUD_PREGRADO.length()))){
			return "ESTUDIANTE DE PREGRADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ESTUD.substring(RolConstantes.ROL_ESTUD.indexOf("_")+1, RolConstantes.ROL_ESTUD.length()))){
			return "ESTUDIANTE";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ESTUDIANTEPOSGRADO.substring(RolConstantes.ROL_ESTUDIANTEPOSGRADO.indexOf("_")+1, RolConstantes.ROL_ESTUDIANTEPOSGRADO.length()))){
			return "ESTUDIANTE POSGRADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_SECREABOGADO.substring(RolConstantes.ROL_SECREABOGADO.indexOf("_")+1, RolConstantes.ROL_SECREABOGADO.length()))){
			return "SECRETARIO ABOGADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_SECRECARRERA.substring(RolConstantes.ROL_SECRECARRERA.indexOf("_")+1, RolConstantes.ROL_SECRECARRERA.length()))){
			return "SECRETARIA DE CARRERA";
		}else if(rolDescripcion.equals(RolConstantes.ROL_ADMINNIVELACION.substring(RolConstantes.ROL_ADMINNIVELACION.indexOf("_")+1, RolConstantes.ROL_ADMINNIVELACION.length()))){
			return "ADMINISTRADOR DE NIVELACION";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DIRVINCULACIONSOCIEDAD.substring(RolConstantes.ROL_DIRVINCULACIONSOCIEDAD.indexOf("_")+1, RolConstantes.ROL_DIRVINCULACIONSOCIEDAD.length()))){
			return "DIRECTOR DE VINCULACION CON LA SOCIEDAD";
		}else if(rolDescripcion.equals(RolConstantes.ROL_USUARIONIVELACION.substring(RolConstantes.ROL_USUARIONIVELACION.indexOf("_")+1, RolConstantes.ROL_USUARIONIVELACION.length()))){
			return "USUARIO NIVELACION";
		}else if(rolDescripcion.equals(RolConstantes.ROL_SECRESECREABOGADO.substring(RolConstantes.ROL_SECRESECREABOGADO.indexOf("_")+1, RolConstantes.ROL_SECRESECREABOGADO.length()))){
			return "SECRETARIO/A DEL SECRETARIO ABOGADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_SECRESUFICIENCIAS.substring(RolConstantes.ROL_SECRESUFICIENCIAS.indexOf("_")+1, RolConstantes.ROL_SECRESUFICIENCIAS.length()))){
			return "SECRETARIA DE SUFICIENCIA";
		}else if(rolDescripcion.equals(RolConstantes.ROL_CONSULTOREPORTES.substring(RolConstantes.ROL_CONSULTOREPORTES.indexOf("_")+1, RolConstantes.ROL_CONSULTOREPORTES.length()))){
			return "CONSULTOR";
		}else if(rolDescripcion.equals(RolConstantes.ROL_BD_SECREPOSGRADO.substring(RolConstantes.ROL_BD_SECREPOSGRADO.indexOf("_")+1, RolConstantes.ROL_BD_SECREPOSGRADO.length()))){
			return "SECRETARIA DE POSGRADO";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD.substring(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD.indexOf("_")+1, RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD.length()))){
			return "DIRECTOR DE ASEGURAMIENTO DE LA CALIDAD";
		}else if(rolDescripcion.equals(RolConstantes.ROL_DIRINVESTIGACION.substring(RolConstantes.ROL_DIRINVESTIGACION.indexOf("_")+1, RolConstantes.ROL_DIRINVESTIGACION.length()))){
			return "DIRECTOR DE INVESTIGACION";
		}else if(rolDescripcion.equals(RolConstantes.ROL_PRESCOMITEETICA.substring(RolConstantes.ROL_PRESCOMITEETICA.indexOf("_")+1, RolConstantes.ROL_PRESCOMITEETICA.length()))){
			return "PRESIDENTE DEL COMITE DE ETICA";
		}else{
			return rolDescripcion;
		} 
	}
	
	public String getDescripcionMateriaModular(Integer mtrId)	{
		Materia mtrAux = new Materia();
		if(mtrId!=GeneralesConstantes.APP_ID_BASE){
			try {
				mtrAux = servMateriaServicio.buscarPorId(mtrId);
				return mtrAux.getMtrDescripcion();
			} catch (MateriaNoEncontradoException e) {
				return "N/A";
			} catch (MateriaException e) {
				return "N/A";
			}catch (Exception e) {
				return "N/A";
			}	
		}
		return "N/A";
		
	}
	
	public String getDescripcionCarrera(int crrId){
		Carrera retorno;
		
		try {
			retorno=servCarreraServicio.buscarPorId(crrId);
			return retorno.getCrrDescripcion();
		} catch (Exception e) {
			
		}
		return " ";
	}
	
	

	/**
	 * Genera la lista de items para el combo estado del record para Ubicación de nivel en Idiomas
	 * @return lista de items para el combo tipo de aula
	 */
	public List<SelectItem> getListaEstadosTercerasMatriculas()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(GeneralesConstantes.APP_ID_BASE,GeneralesConstantes.APP_SELECCIONE_TODOS ));

		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL));
		//Constantes para identificar el estado de la apelación de tercera matricula.
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL));
		retorno.add(new SelectItem(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL));
		return retorno;
	}
	


	/**
	 * Genera la lista de items para el combo niveles de administración de posgrados
	 * @return lista de items para el combo tipo de aula
	 */
	public List<SelectItem> getListaNivelesPosgrado()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(1, "UN NIVEL"));
		retorno.add(new SelectItem(2, "DOS NIVELES"));
		retorno.add(new SelectItem(3, "TRES NIVELES"));
		retorno.add(new SelectItem(4, "CUATRO NIVELES"));
		retorno.add(new SelectItem(5, "CINCO NIVELES"));
		retorno.add(new SelectItem(6, "SEIS NIVELES"));
		retorno.add(new SelectItem(7, "SIETE NIVELES"));
		retorno.add(new SelectItem(8, "OCHO NIVELES"));
		retorno.add(new SelectItem(9, "NUEVE NIVELES"));
		retorno.add(new SelectItem(10, "DIEZ NIVELES"));
		
		return retorno;
	}
	

	
	/**
	 * Genera la lista de items para el combo estado del rol
	 * @return lista de items para el combo estado solicitudTerceraMatri de recordEstudiante
	 */
	public String getListaDescripcionEstadosTercerasMatriculas(Integer estado)	{
		if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL;
		}else if(estado==SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE){
			return SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL;
	    }else {
			return "SIN ESTADO";
		}
	}	
	
	public List<SelectItem> getListaEstadosMatriculaSAU()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(1,"MATRICULADO"));
		retorno.add(new SelectItem(2,"ANULADO"));
		retorno.add(new SelectItem(3,"RETIRADO"));
		retorno.add(new SelectItem(4,"PENDIENTE"));
		
		return retorno;
		
	}

	
	/**
	 * Genera la lista de items para el combo estado de rolflujocarrera
	 * @return lista de items para el combo estado de rolflujocarrera
	 */
	public List<SelectItem> getListaEstadoRoflcr()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
			retorno.add(new SelectItem(RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_LABEL));
			retorno.add(new SelectItem(RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE, RolFlujoCarreraConstantes.ESTADO_INACTIVO_LABEL));
			
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado de rolflujocarrera
	 * @param estado - estado del tipo rolflujocarrera
	 * @return label del valor del estado del tipo rolflujocarrera
	 */
	public String getLabelEstadoRoflcr(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoRoflcr()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado  detalle matricula para retiros y retiros fortuitos
	 * @return lista de items para el combo estado detalle matricula
	 */
	public List<SelectItem> getListaEstadoDetalleMatricula()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE, DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE, DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE, DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE, DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE, DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_ANULACION_MATRICULA_ACEPTADA_VALUE, DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_ANULACION_MATRICULA_ACEPTADA_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado detalle matricula
	 * @param estado - estado del detalle matricula
	 * @return label del valor del estado del detalle matricula
	 */
	public String getLabelEstadoDetalleMatricula(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoDetalleMatricula()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	

	/**
	 * Genera la lista de items para el combo estado nivel académico
	 * @return lista de items para el combo estado nivel académico
	 */
	public List<SelectItem> getListaNivelAcademico()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(CarreraConstantes.TIPO_PREGRADO_VALUE, CarreraConstantes.TIPO_PREGRADO_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.TIPO_POSGRADO_VALUE, CarreraConstantes.TIPO_POSGRADO_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.TIPO_NIVELEACION_VALUE, CarreraConstantes.TIPO_NIVELACION_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.TIPO_SUFICIENCIA_VALUE, CarreraConstantes.TIPO_SUFICIENCIA_LABEL));
		return retorno;
	}
	 
	/**
	 * Obtiene el label del estado nivel académico
	 * @param estado - estado del nivel académico
	 * @return label del valor del estado del nivel académico
	 */
	public String getLabelNivelAcademico(int estado){
		String retorno = null;
		for (SelectItem item : getListaNivelAcademico()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo Tipo inscrito de la ficha inscripción
	 * @return lista de items para el combo Tipo inscrito de la ficha inscripción
	 */
	public List<SelectItem> getListaTipoInscrito()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE, FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_LABEL));
		return retorno;
		
	}
	 
	/**
	 * Obtiene el label del Tipo inscrito de la ficha inscripción
	 * @param tipo - Tipo inscrito de la ficha inscripción
	 * @return label del valor del Tipo inscrito de la ficha inscripción
	 */
	public String getLabelTipoInscrito(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoInscrito()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado de la ficha inscripción
	 * @return lista de items para el combo estado de la ficha inscripción
	 */
	public List<SelectItem> getListaEstadoFichaInscripcion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(FichaInscripcionConstantes.ACTIVO_VALUE, FichaInscripcionConstantes.ACTIVO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.INACTIVO_VALUE, FichaInscripcionConstantes.INACTIVO_LABEL));
		
		return retorno;
		
	}
	 
	/**
	 * Obtiene el label del estado de la ficha inscripción
	 * @param estado - estado del de la ficha inscripción
	 * @return label del valor del estado de la ficha inscripción
	 */
	public String getLabelEstadoFichaInscripcion(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoFichaInscripcion()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo vigencia de la ficha inscripción
	 * @return lista de items para el combo vigencia de la ficha inscripción
	 */
	public List<SelectItem> getListaVigenciaFichaInscripcion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(FichaInscripcionConstantes.VIGENTE_VALUE, FichaInscripcionConstantes.VIGENTE_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.NO_VIGENTE_VALUE, FichaInscripcionConstantes.NO_VIGENTE_LABEL));
		
		return retorno;
		
	}
	 
	/**
	 * Obtiene el label del vigencia de la ficha inscripción
	 * @param estado - vigencia del de la ficha inscripción
	 * @return label del valor del vigencia de la ficha inscripción
	 */
	public String getLabelVigenciaFichaInscripcion(int estado){
		String retorno = null;
		for (SelectItem item : getListaVigenciaFichaInscripcion()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo de ingreso de la ficha inscripción
	 * @return lista de items para el combo vigencia de la ficha inscripción
	 */
	public List<SelectItem> getListaTipoIngresoFichaInscripcion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_LABEL));
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_LABEL));
	//	retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_LABEL)); // MQ: Pide producción retirar opción
	//	retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_LABEL)); //MQ: Pedido de retirar opción UCE-DA- 2019-1734-O 9 jul 2019
	 //	retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_LABEL)); // MQ: NO es necesario al 11 Feb 2019  
		retorno.add(new SelectItem(FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE, FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_LABEL));
		
		return retorno;
		
	}
	 
	/**
	 * Obtiene el label del tipo de ingreso de la ficha inscripción
	 * @param tipo - tipo de ingreso del de la ficha inscripción
	 * @return label del valor del tipo de ingreso de la ficha inscripción
	 */
	public String getLabelTipoIngresoFichaInscripcion(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoIngresoFichaInscripcion()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	

	
	/**
	 * Genera la lista de items para el combo tipo de anulación de una asignatura
	 * @return lista de items para el combo vigencia de la ficha inscripción
	 */
	public List<SelectItem> getListaTipoAnulacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE, DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL));
		retorno.add(new SelectItem(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_VALUE, DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL));
		return retorno;
		
	}
	 
	/**
	 * Obtiene el label del tipo de ingreso de la ficha inscripción
	 * @param tipo - tipo de ingreso del de la ficha inscripción
	 * @return label del valor del tipo de ingreso de la ficha inscripción
	 */
	public String getLabelTipoAnulacion(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoAnulacion()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo tipo de parentesco para beneficiario
	 * @return lista de items para el combo tipo de parentesco para beneficiario
	 */
	public List<SelectItem> getListaTipoParentescoBeneficiario()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_PADRES_VALUE, ReferenciaConstantes.PARENTESCO_PADRES_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_SUEGROS_VALUE, ReferenciaConstantes.PARENTESCO_SUEGROS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_HIJOS_VALUE, ReferenciaConstantes.PARENTESCO_HIJOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_YERNO_NUERA_VALUE, ReferenciaConstantes.PARENTESCO_YERNO_NUERA_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_ABUELOS_VALUE, ReferenciaConstantes.PARENTESCO_ABUELOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_HERMANOS_VALUE, ReferenciaConstantes.PARENTESCO_HERMANOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_CUNADO_VALUE, ReferenciaConstantes.PARENTESCO_CUNADO_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_NIETOS_VALUE, ReferenciaConstantes.PARENTESCO_NIETOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_BISABUELOS_VALUE, ReferenciaConstantes.PARENTESCO_BISABUELOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_TIOS_VALUE, ReferenciaConstantes.PARENTESCO_TIOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_SOBRINOS_VALUE, ReferenciaConstantes.PARENTESCO_SOBRINOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_BIZNIETOS_VALUE, ReferenciaConstantes.PARENTESCO_BIZNIETOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_PRIMOS_VALUE, ReferenciaConstantes.PARENTESCO_PRIMOS_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_CONYUGE_VALUE, ReferenciaConstantes.PARENTESCO_CONYUGE_LABEL));
		retorno.add(new SelectItem(ReferenciaConstantes.PARENTESCO_OTROS_VALUE, ReferenciaConstantes.PARENTESCO_OTROS_LABEL));
		
		return retorno;
		
	}
	 
	/**
	 * Obtiene el label del tipo de parentesco para beneficiario
	 * @param tipo - tipo de parentesco para beneficiario
	 * @return label del valor del tipo de parentesco para beneficiario
	 */
	public String getLabelTipoParentescoBeneficiario(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoParentescoBeneficiario()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	public List<SelectItem> getListaEstadoPeriodoAcademico()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_INACTIVO_LABEL));
		retorno.add(new SelectItem(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo de carrera
	 * @return lista de items para el combo tipo de carrera
	 */
	public List<SelectItem> getListaTipoCarrera()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(CarreraConstantes.TIPO_NIVELEACION_VALUE, CarreraConstantes.TIPO_NIVELACION_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.TIPO_PREGRADO_VALUE, CarreraConstantes.TIPO_PREGRADO_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.TIPO_POSGRADO_VALUE, CarreraConstantes.TIPO_POSGRADO_LABEL));
		retorno.add(new SelectItem(CarreraConstantes.TIPO_SUFICIENCIA_VALUE, CarreraConstantes.TIPO_SUFICIENCIA_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del tipo de carrera
	 * @param tipo - tipo de carrera
	 * @return label del valor del tipo de carrera
	 */
	public String getLabelTipoCarrera(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoCarrera()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo estado detalle puesto
	 * @return lista de items para el combo estado detalle puesto
	 */
	public List<SelectItem> getListaEstadoDetallePuesto()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		
		retorno.add(new SelectItem(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE, DetallePuestoConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(DetallePuestoConstantes.ESTADO_INACTIVO_VALUE, DetallePuestoConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el label del estado del estado detalle puesto
	 * @param estado - estado del estado detalle puesto
	 * @return label del valor del estado del estado detalle puesto
	 */
	public String getLabelListaEstadoDetallePuesto(int tipo){
		String retorno = null;
		for (SelectItem item : getListaEstadoDetallePuesto()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	/**
	 * Genera la lista de items para el combo tipo de seleccion para el indicador registrado
	 * @return lista de items para el combo tipo partida para contrato
	 */
	public List<SelectItem> getListaTipoSeleccion(){
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(TipoContenidoConstantes.TIPO_SELECCION_UNICO_VALUE, TipoContenidoConstantes.TIPO_SELECCION_UNICO_LABEL));
		retorno.add(new SelectItem(TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_VALUE, TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_LABEL));
		//retorno.add(new SelectItem(TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_TRES_VALUE, TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_TRES_LABEL));
		
		
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo tipo de seleccion para el indicador registrado
	 * @return lista de items para el combo tipo partida para contrato
	 */
	public List<SelectItem> getListaEstadoFuncionTipoEvaluacion(){
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(FuncionTipoEvaluacionConstantes.ESTADO_ACTIVO_VALUE, FuncionTipoEvaluacionConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(FuncionTipoEvaluacionConstantes.ESTADO_INACTIVO_VALUE, FuncionTipoEvaluacionConstantes.ESTADO_INACTIVO_LABEL));
	
		
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo categoria.
	 * @return items para el combo categoria.
	 */
	public List<SelectItem> getListCategoria() {
		List<SelectItem>  retorno = new ArrayList<>();
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_PRINCIPAL.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_PRINCIPAL.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_AGREGADO.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_AGREGADO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_AUXILIAR.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_AUXILIAR.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_HONORARIO.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_HONORARIO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_INVITADO.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_INVITADO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.PST_CATEGORIA_DOCENTE_OCASIONAL.getValue(), PuestoEnum.PST_CATEGORIA_DOCENTE_OCASIONAL.getLabel()));
		return retorno;
	}
	
	/**
	 * Obtiene el label de la categoria
	 * @param tipo - tipo categoria
	 * @return label del valor de la categoria
	 */
	public String getLabelCategoria(int tipo){
		String retorno = null;
		for (SelectItem item : getListCategoria()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo categoria.
	 * @return items para el combo categoria.
	 */
	public List<SelectItem> getListaNivelRangoGradual() {
		List<SelectItem>  retorno = new ArrayList<>();
		retorno.add(new SelectItem(PuestoEnum.NIVEL_RANGO_GRADUAL_UNO.getValue(), PuestoEnum.NIVEL_RANGO_GRADUAL_UNO.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.NIVEL_RANGO_GRADUAL_DOS.getValue(), PuestoEnum.NIVEL_RANGO_GRADUAL_DOS.getLabel()));
		retorno.add(new SelectItem(PuestoEnum.NIVEL_RANGO_GRADUAL_TRES.getValue(), PuestoEnum.NIVEL_RANGO_GRADUAL_TRES.getLabel()));
		return retorno;
	}
	
	/**
	 * Obtiene el label de la categoria
	 * @param tipo - tipo categoria
	 * @return label del valor de la categoria
	 */
	public String getLabelNivelRangoGradual(int tipo){
		String retorno = null;
		for (SelectItem item : getListaNivelRangoGradual()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
}

