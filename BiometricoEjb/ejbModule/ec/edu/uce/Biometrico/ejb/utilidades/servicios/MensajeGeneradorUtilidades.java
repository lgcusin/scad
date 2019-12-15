/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     MensajeGeneradorUtilidades.java	  
 DESCRIPCIÓN: Clase encargada de gestionar los mensajes a traves de un archivo de configuracion 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo			       Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.Biometrico.ejb.utilidades.servicios;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
* Clase MensajeGeneradorUtilidades.java
* Encargada de gestionar los mensajes a traves de un archivo de configuracion 
* @author dcollaguazo
* @version 1.0 
*/
public class MensajeGeneradorUtilidades implements Serializable{

	private static final long serialVersionUID = 8692441354775221668L;

	private String clave;
	private Object[] valores;
	
	public static final HashMap<String, String> mensajes;
	
	static{
		mensajes = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("META-INF.configuracion.mensajes");
		Enumeration<String> enumClaves =  rb.getKeys();
		while(enumClaves.hasMoreElements()){
			String claveAux = (String) enumClaves.nextElement(); 
			mensajes.put(claveAux, rb.getString(claveAux));
		}
	}
	
	public MensajeGeneradorUtilidades(String clave) {
		this(clave, null);
	}
	
	public MensajeGeneradorUtilidades(String clave, Object obj) {
		this(clave, new Object[] { obj });
	}
	
	public MensajeGeneradorUtilidades(String clave, Object obj0, Object obj1) {
		this(clave, new Object[] { obj0, obj1 });
	}
	
	public MensajeGeneradorUtilidades(String clave, Object obj0, Object obj1, Object obj2) {
		this(clave, new Object[] { obj0, obj1, obj2 });
	}
	
	public MensajeGeneradorUtilidades(String clave, Object obj0, Object obj1, Object obj2, Object obj3) {
		this(clave, new Object[] { obj0, obj1, obj2, obj3 });
	}
	
	public MensajeGeneradorUtilidades(String clave, Object[] valores) {
        this.clave = clave;
        this.valores = valores;
    }

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Object[] getValores() {
		return valores;
	}

	public void setValores(Object[] valores) {
		this.valores = valores;
	}
	
	public static String getMsj(MensajeGeneradorUtilidades log) {
		String mensajeGenerico = null;
		if(mensajes.containsKey(log.getClave())){
			mensajeGenerico = mensajes.get(log.getClave());
			if(log.getValores() !=null && log.getValores().length>0){
				for (int i=0 ; i<log.getValores().length;i++ ) {
					String corte = "@"+i;					
					mensajeGenerico = mensajeGenerico.replaceAll(corte, (log.getValores()[i]).toString());
				}
			}
		}else{
			mensajeGenerico = "No existe el mensaje";
		}
		return mensajeGenerico;
	}
		
}
