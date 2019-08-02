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
   
 ARCHIVO:     CargaHorariaServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla carga .
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 18-12-2018			Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;

/**
 * Interface CargaHorariaServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 * @author fgguzman
 * @version 1.0
 */
public interface CargaHorariaServicioJdbc {

	PersonaDto buscarInformacionDocente(String identificacion) throws PersonaNoEncontradoException, PersonaException;
	
	List<CargaHorariaDto> buscarImparticionClasesPorDocente(String identificacion, int periodo) throws CargaHorariaNoEncontradoException, CargaHorariaException;
	
	List<CargaHorariaDto> buscarFuncionesPorDocente(String identificacion, int periodo) throws CargaHorariaNoEncontradoException, CargaHorariaException;

	List<CargaHorariaDto> buscarFuncionesPorPeriodo(int periodo) throws CargaHorariaNoEncontradoException, CargaHorariaException;
	
	List<PersonaDto> buscarDocentes(int dependenciaId, int carreraId) throws PersonaNoEncontradoException, PersonaException;

}
