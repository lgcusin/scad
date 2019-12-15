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
   
 ARCHIVO:     GeneralesConstantesForm.java	  
 DESCRIPCION: Bean de peticion que maneja las constantes generales. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017 			Dennis Collaguazo                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.Biometrico.jsf.general;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.Biometrico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;


/**
 * Clase (request bean) GeneralesConstantesForm.
 * Bean de peticion que maneja las constantes generales.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean
@RequestScoped
public class GeneralesConstantesForm {
	
	/**
	 * Retorna el valor de la constante de campo requerido
	 * @return el valor de la constante de campo requerido
	 */
	public String getMsgCampoRequerido() {
		return GeneralesConstantes.APP_MSG_CAMP_REQUERIDO;
	}
	
	/**
	 * Retorna la constante que representa a la etiqueta de la opcion de seleccionar
	 * @return la constante que representa a la etiqueta de la opcion de seleccionar
	 */
	public String getAppSeleccione() {
		return GeneralesConstantes.APP_SELECCIONE;
	}
	
	/**
	 * Retorna la constante que representa a la etiqueta de la opcion de seleccionar todos
	 * @return la constante que representa a la etiqueta de la opcion de seleccionar todos
	 */
	public String getAppSeleccioneTodos() {
		return GeneralesConstantes.APP_SELECCIONE_TODOS;
	}
	
	/**
	 * Retorna el valor de la de APP_ID_BASE de la entidad
	 * @return el valor de la de APP_ID_BASE de la entidad
	 */
	public Integer getAppIdBase() {
		return GeneralesConstantes.APP_ID_BASE;
	}
	/**
	 * Retorna el valor de la de APP_EDITAR
	 * @return el valor de la de APP_EDITAR
	 */
	public Integer getAppIdEditar() {
		return GeneralesConstantes.APP_EDITAR;
	}
	/**
	 * Retorna el valor de la de APP_NUEVO
	 * @return el valor de la de APP_NUEVO
	 */
	public Integer getAppIdNuevo() {
		return GeneralesConstantes.APP_NUEVO;
	}
	/**
	 * Retorna el formato fecha y hora
	 * @return el formato fecha y hora
	 */
	public String getAppFormatoFechaHora() {
		return GeneralesConstantes.APP_FORMATO_FECHA_HORA;
	}
	
	/**
	 * Retorna el formato fecha
	 * @return el formato fecha
	 */
	public String getAppFormatoFecha() {
		return GeneralesConstantes.APP_FORMATO_FECHA;
	}

	/**
	 * Retorna la fecha actual del sistema
	 * @return
	 */
	public Date getFechaDelSistema() {
		Date fecha = new Date();
		return fecha;
    }
	
	/**
	 * Retorna el valor de la aprobacion por nivel
	 * @return el valor de la aprobacion por nivel
	 */
	public Integer getAprobacionXnivel() {
		return GeneralesConstantes.APP_APROBACION_POR_NIVEL_VALUE;
	}
	
	/**
	 * Retorna el valor de la aprobacion por materia
	 * @return el valor de la aprobacion por materia
	 */
	public Integer getAprobacionXmateria() {
		return GeneralesConstantes.APP_APROBACION_POR_MATERIA_VALUE;
	}

	
	/**
	 * Valida la fecha en el componente de primefaces que no sea mayor a la actual
	 * @return
	 */
	public Date getFechaSistema() {
		return new Date();
    }
	
	
	/**
	 * Retorna la fecha con formato string
	 * @return la fecha con formato string
	 */
//	public String getFormatoFechaTimestamp(Timestamp tmFecha){
//		try {
//			if(tmFecha != null){
//				String tmFechaAux = GeneralesUtilidades.fechaFormatoTimeStamp(tmFecha);
//				if(tmFechaAux != null){
//					return tmFechaAux;
//				}else{
//					return "";
//				}
//			}else{
//				return "";
//			}
//		} catch (ParseException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Constantes.generales.formatoFechaTimestamp.transformar")));
//			return "";
//		}
//	}
	
	
	/**
	 * Retorna el label del estado del paralelo
	 * @param  el valor de la constante del estado del paralelo 
	 * @return el valor label de la constantes  del Estado del Paralelo: 0.- ACTIVO  1.-INACTIVO
	 */
//	public String traerEstadoParalelo(int estadoParalelo){
//		if(estadoParalelo == ParaleloConstantes.ESTADO_ACTIVO_VALUE){
//			return ParaleloConstantes.ESTADO_ACTIVO_LABEL;
//		}
//		
//		if(estadoParalelo == ParaleloConstantes.ESTADO_INACTIVO_VALUE){
//			return ParaleloConstantes.ESTADO_INACTIVO_LABEL;
//		}
//		return null;
//	}
	
	/**
	 * Retorna el label del numero de matricula
	 * @param  el valor de la constante del numero de matricula 
	 * @return el valor label de la constantes  del numero de matricula : 0.- PRIMERA  1.-SEGUNDA 2.- TERCERA
	 */
//	public String traerNumeroMatricula(int numMatricula){
//		if(numMatricula == DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE){
//			return DetalleMatriculaConstantes.PRIMERA_MATRICULA_LABEL;
//		}
//		
//		if(numMatricula == DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE){
//			return DetalleMatriculaConstantes.SEGUNDA_MATRICULA_LABEL;
//		}
//		
//		if(numMatricula == DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE){
//			return DetalleMatriculaConstantes.TERCERA_MATRICULA_LABEL;
//		}
//		return null;
//	}
	
	public static String[] separarNombres(String nombres){
		String[] listaArticulos = {"el", "la", "de", "los", "las","del"};
		
		String[] retorno = new String[2];
		if(nombres == null ){
			return null;
		}
		String asd = nombres.replaceAll(" +", " ").trim();
		
		String[] aux = asd.split("\\s+");
		if(aux.length == 1){
			retorno[0] = aux[0];
			retorno[1] = null;
		}else{
			int contNombres = 0;
			
			StringBuilder sb = new StringBuilder();
			boolean esArticulo = false;
			for (String item : aux) {
				if(contNombres == 0){
					//verifico si es articulo
					if(item.length() <=3){
						esArticulo = false;
						for (String itemArt: listaArticulos) {
							if(item.equalsIgnoreCase(itemArt)){//es articulo
								esArticulo = true;
								break;
							}
						}
						
						if(!esArticulo){
							sb.append(item);
							sb.append(" ");
							contNombres++;
							retorno[0]= sb.toString().replaceAll(" +", " ").trim();
							sb = new StringBuilder();
						}else{
							sb.append(item);
							sb.append(" ");
						}
					}else{
						sb.append(item);
						sb.append(" ");
						retorno[0]= sb.toString().replaceAll(" +", " ").trim();
						contNombres++;
						sb = new StringBuilder();
					}
				}else{
					sb.append(item);
					sb.append(" ");
				}
			}
			retorno[1]= sb.toString().replaceAll(" +", " ").trim();
		}
		return retorno;
	}
	
	public static String separaCaracteres(String cadena, int desde, int hasta){
		String retorno = null;
		if(cadena != null){
			retorno = cadena.substring(desde, hasta);
		}else{
			retorno = "";
		}
        return retorno;
	}
	
	/**
	 * Retorn el valor de la constantes LABEL de tipo de identificacion de la persona
	 * @return el valor de la constantes PersonaConstantes: 0.-CEDULA 1.-PASAPORTE/OTROS
	 */
//	public String traerTipoIdentificacion(int tipoBD){
//		if(tipoBD == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
//			return PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_LABEL;
//		}
//		
//		if(tipoBD == PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
//			return PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_LABEL;
//		}
//		return null;
//	}
	
	
	/**
	 * Retorna la constante correspondiente al Rol Decano de la Facultad.
	 * @return la constante correspondiente al Rol Decano de la Facultad.
	 */
//	public Integer getRolDecano() {
//		return RolConstantes.ROL_DECANO_VALUE;
//	}

	
	/**
	 * Retorna la constante correspondiente al Rol Director de Carrera.
	 * @return la constante correspondiente al Rol Director de Carrera.
	 */
//	public Integer getRolDirectorCarrera() {
//		return RolConstantes.ROL_DIRCARRERA_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Secretario de Carrera.
	 * @return la constante correspondiente al Rol Secretario de Carrera.
	 */
//	public Integer getRolSecretariaCarrera() {
//		return RolConstantes.ROL_SECRECARRERA_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Secretario de Suficiencias.
	 * @return la constante correspondiente al Rol Secretario de Suficiencias.
	 */
//	public Integer getRolSecretariaSuficiencias() {
//		return RolConstantes.ROL_SECRESUFICIENCIAS_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Secretario de Nivelacion.
	 * @return la constante correspondiente al Rol Secretario de Nivelacion.
	 */
//	public Integer getRolSecretariaNivelacion() {
//		return RolConstantes.ROL_SECRENIVELACION_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Estudiante Pregrado.
	 * @return la constante correspondiente al Rol Estudiante Pregrado.
	 */
//	public Integer getRolEstudiantePregrado() {
//		return RolConstantes.ROL_ESTUD_PREGRADO_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Soporte Tecnico.
	 * @return la constante correspondiente al Rol Soporte Tecnico.
	 */
//	public Integer getRolSoporteTecnico() {
//		return RolConstantes.ROL_SOPORTE_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Consultores.
	 * @return la constante correspondiente al Rol Consultores.
	 */
//	public Integer getRolConsultorReportes() {
//		return RolConstantes.ROL_CONSULTOREPORTES_VALUE;
//	}
	
	/**
	 * Retorna la constante correspondiente al Rol Secretaria Prosgrado.
	 * @return la constante correspondiente al Rol Secretaria Prosgrado.
	 */
//	public Integer getRolSecretariaPosgrado() {
//		return RolConstantes.ROL_SECREPOSGRADO_VALUE;
//	}
	
	/**
	 * Método que da formato a un BigDecimal y retorna un String.
	 * @author FREDDY - fgguzman 
	 * @param param - bigdecimal
	 * @param simbolo - adiciona simbolo.
	 * @return valor en formato string.
	 */
	public String cambiarBigDecimalToString(BigDecimal param, int simbolo){
		
		if (param != null && param.intValue() != 0) {
			if (simbolo == 0) {
				return " $. " + String.format("%.2f", param);	
			}
		}
		
		return " - ";
	}
	
}
