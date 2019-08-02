/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     NotacorteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla NotaCorte.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
11-07-2019          Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteException;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NotaCorteValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.NotaCorte;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface NotaCorteServicio
 * Interfase que define las operaciones sobre la tabla NotaCorte.
 * @author lmquishpei
 * @version 1.0
 */
public interface NotaCorteServicio {
	/**
	 * Busca una entidad Nota corte por su id
	 * @param id - del Nota corte a a buscar
	 * @return Nota corte con el id solicitado
	 * @throws NotaCorteNoEncontradoException - Excepcion lanzada cuando no se encuentra una Nota corte con el id solicitado
	 * @throws NotaCorteException - Excepcion general
	 */
	public NotaCorte buscarPorId(Integer id) throws NotaCorteNoEncontradoException, NotaCorteException;

	/**
	 * Buscar Notas corte activas por Carrera y Periodo(corte)
	 * @param idCrr - idCarrrera de las Nota corte a buscar
	 * @param idPrac - idPeriodoAcademico de las Nota corte a buscar
	 * @return  Nota corte con los parametro indicados
	 * @throws NotaCorteException - Excepcion general
	 */

	public NotaCorte buscarActivoXCrrXPrac(Integer idCrr, Integer idPrac ) throws  NotaCorteException;
	
	/**
	 * Añadir o editar nota de corte de una carrera en la BD
	 * @return true o False - Si se completa correctamente el guardado en BDD
	 * @throws NotaCorteValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws NotaCorteException - Excepción general
	 */

	public boolean editarGuardarNotaCorte(CarreraDto carreraNotaCorte, Integer NocrPracId, Usuario usuario) throws  NotaCorteException;
	
	/**
	 * listar Notas corte activas en periodo distinto Periodo(corte)
	 * @param idPrac - idPeriodoAcademico de las Nota corte a buscar
	 */

	public List<NotaCorte> listarActivoXPrac(Integer idPrac) throws  NotaCorteException ;
	
	
		
	public void desactivarNotaCorteAnteriores(List<NotaCorte> listaNotaCorte ) throws  NotaCorteException;
	
	
}
