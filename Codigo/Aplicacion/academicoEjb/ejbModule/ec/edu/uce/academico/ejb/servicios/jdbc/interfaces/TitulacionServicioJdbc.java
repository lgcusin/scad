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
   
 ARCHIVO:     SuficienciaInformaticaServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la Suficiencia Informatica.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-ENE-2018			Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.sql.SQLException;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.TitulacionDto;
import ec.edu.uce.academico.ejb.excepciones.TitulacionException;
import ec.edu.uce.academico.ejb.excepciones.TitulacionNoEncontradoException;

/**
 * Interface SuficienciaInformaticaServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la Suficiencia Informatica.
 * @author fgguzman
 * @version 1.0
 */
public interface TitulacionServicioJdbc {

	public List<TitulacionDto> ListarTitulacionExamenComplexivo(int convId, int fclId, int crrId) throws TitulacionNoEncontradoException, TitulacionException;
	
	public List<TitulacionDto> listarConvocatoria() throws TitulacionNoEncontradoException,TitulacionException;
	
	public List<TitulacionDto> listarTodasFacultades() throws TitulacionNoEncontradoException,TitulacionException;
	
	public List<TitulacionDto> listarTodasCarreraXFacultad(int fclId) throws TitulacionNoEncontradoException,TitulacionException;
	
	public List<TitulacionDto> listarFacultadesxUsuario(int usrId) throws TitulacionNoEncontradoException,TitulacionException;
	public List<TitulacionDto> ListarTitulacionProyectoTitulacion(int convId, int fclId, int crrId) throws TitulacionNoEncontradoException, TitulacionException;
	
	
}
