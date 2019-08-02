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
   
 ARCHIVO:     FacesUtil.java	  
 DESCRIPCION: Clase encargada de obtener usuario y roles del facesContext, 
 			  asi como tabien de manejar los mensajes de error para el usuario. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.utilidades;

import java.util.Collection;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.security.core.context.SecurityContextHolder;

import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.spring.springSecurity.DetalleUsuario;
import ec.edu.uce.academico.jsf.spring.springSecurity.ProveedorPermisos;

/**
 * Clase FacesUtil.
 * Clase encargada de obtener usuario y roles del facesContext, asi como tabien de manejar los mensajes de error para el usuario.
 * @author dcollaguazo.
 * @version 1.0
 */
public class FacesUtil{
	
	/**
	 * Retorna el mensaje de informacion en el facesContext
	 * @param mensaje - mensaje de informacion
	 */
	public static void mensajeInfo(String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,mensaje,""));
    }
	
	/**
	 * Retorna el mensaje de error en el facesContext
	 * @param mensaje - mensaje de error
	 */
    public static void mensajeError(String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,""));
    }
    
    /**
	 * Retorna el mensaje de advertencia en el facesContext
	 * @param mensaje - mensaje de advertencia
	 */
    public static void mensajeWarn(String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,mensaje,""));
    }
    
    /**
  	 * Retorna la lista de roles del usuario logueado
  	 * @return lista de roles del usuario logueado
  	 */
    @SuppressWarnings("unchecked")
	public static Collection<ProveedorPermisos> obtenerPerfiles(){
    	DetalleUsuario aux = (DetalleUsuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return (Collection<ProveedorPermisos>)aux.getAuthorities();
    }
    
    /**
  	 * Retorna el usuario logueado
  	 * @return el usuario (Entity) logueado
  	 */
   	public static Usuario obtenerUsuario(){
   		DetalleUsuario aux = (DetalleUsuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       	return aux.getUsuario();
    }
   	
    /**
  	 * Elimina los mensajes encolados
  	 */
	public static void limpiarMensaje()    {
        Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
        while(msgIterator.hasNext()){
            msgIterator.next();
            msgIterator.remove();
        }
    }
	
	/**
	* Retorna el mensaje de advertencia en el facesContext
	* @param mensaje - mensaje de advertencia
	*/
	public static void mensajeError(String etiqueta, String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(etiqueta, new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,""));
    }
	
	/**
	 * Retorna el mensaje de informacion en el facesContext con etiqueta
	 * @param mensaje - mensaje de informacion
	 * @param etiqueta -etiqueta
	 */
	public static void mensajeInfo(String etiqueta,String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(etiqueta, new FacesMessage(FacesMessage.SEVERITY_INFO,mensaje,""));
    }
}
