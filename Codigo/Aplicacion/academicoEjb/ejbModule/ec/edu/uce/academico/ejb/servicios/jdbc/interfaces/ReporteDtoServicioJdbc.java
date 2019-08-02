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

 ARCHIVO:     EstudianteDtoServicioJdbc.java	  
 DESCRIPCION: Interface para los servicios de DTO del reporte. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 31-05-2018 		Fatima Tobar			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ReportesGeneralesDto;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;

/**
 * Interface EstudianteDtoServicioJdbc. Para los servicios del DTO de
 * reportes.
 * 
 * @author fktobar.
 * @version 1.0
 */
public interface ReporteDtoServicioJdbc {

	
		
	public List<ReportesGeneralesDto> buscarTotalMatriculadosxCarreraXNivel(int idPeriodo, int idCarrera, int idNiveles, List<Dependencia> listaDependencia)
			throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;

	
//public List<ReportesGeneralesDto> buscarTotalInscritosxCarreraXNivel(int idPeriodo,int idDependencia, int idCarrera, int idNiveles)
//		throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;


public List<ReportesGeneralesDto> buscarTotalSolicitudesTerceras(int idPeriodo, int idEstados, List<Dependencia> listaDependencia)
		throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;



public List<ReportesGeneralesDto> buscarRecordEstudianteSAU(int facultad, int especodigo, int periodoId, String identificacion,
		int matriculaCurso, int matriculaEstado)
		throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;


Integer buscarCrrIdporEspeCodigoSAU(int espe_codigo)
		throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;


Integer buscarDpnIdporCarreraidSAU(int crr_id)
		throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;





public List<ReportesGeneralesDto> buscarRecordEstudianteSIIU(int facultad, int codigocarrera, int periodoId,
		String identificacion) throws ReportesGeneralesDtoException, ReportesGeneralesDtoNoEncontradoException;






}
