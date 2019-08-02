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

 ARCHIVO:     DocenteJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad FichaDocente DTO JDBC. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 27-04-2017 			Dennis Collaguazo  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) DocenteJdbcDto.
 * DTO encargado de manejar los datos de la entidad FichaDocente DTO JDBC. 
 * @author dcollaguazo.
 * @version 1.0
 */
public class DocenteDatosDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	//Tabla Usuario
	private int usrId;
	private String usrNick;
	
	//Tabla Rol
	private int rolId;
	private String rolDescripcion;
	
	//Tabla Carrera
	private int crrId;
	private String crrDescripcion;

	public DocenteDatosDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public String getUsrNick() {
		return usrNick;
	}

	public void setUsrNick(String usrNick) {
		this.usrNick = usrNick;
	}

	public int getRolId() {
		return rolId;
	}

	public void setRolId(int rolId) {
		this.rolId = rolId;
	}

	public String getRolDescripcion() {
		return rolDescripcion;
	}

	public void setRolDescripcion(String rolDescripcion) {
		this.rolDescripcion = rolDescripcion;
	}

	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(tabulador + "usrId : " + usrId); 
		sb.append(tabulador + "usrNick : " + (usrNick==null? "NULL":usrNick));
		sb.append(tabulador + "rolId : " + rolId); 
		sb.append(tabulador + "rolDescripcion : " + (rolDescripcion==null? "NULL":rolDescripcion));
		sb.append(tabulador + "crrId : " + crrId); 
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion==null? "NULL":crrDescripcion));

		return sb.toString();
	}

	

	

	


}
