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
   
 ARCHIVO:     MallaCurricularNivelDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad MallaCurricularNivel. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-FEB-2019 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) MallaCurricularNivelDto. DTO encargado de manejar los datos de la
 * entidad MallaCurricularNivel de posgrado.
 * 
 * @author jdalbuja.
 * @version 1.0
 */
public class MallaCurricularNivelPosgradoDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	private int mlcrnvId;
	private Integer mlcrnvHoras;
	private Integer mlcrnvHorasAcumulado;
	private Integer mlcrnvHorasSql; // validación de campo
	private Integer mlcrnvHorasAcumuladoSql; // validación de campo
	private Integer mlcrnvMlcrId;
	private Integer mlcrnvNvlId;

	private MallaCurricularDto mlcrnvMallaCurricularDto;
	private CarreraDto mlcrnvCarreraDto;
	private NivelDto mlcrnvNivelDto;
	private MateriaDto mlcrnvMateriaDto;
	private PeriodoAcademicoDto mlcrnvPeriodoAcademicoDto;

	public MallaCurricularNivelPosgradoDto() {
	}

	public int getMlcrnvId() {
		return mlcrnvId;
	}

	public void setMlcrnvId(int mlcrnvId) {
		this.mlcrnvId = mlcrnvId;
	}

	public Integer getMlcrnvHoras() {
		return mlcrnvHoras;
	}

	public void setMlcrnvHoras(Integer mlcrnvHoras) {
		this.mlcrnvHoras = mlcrnvHoras;
	}

	public Integer getMlcrnvHorasAcumulado() {
		return mlcrnvHorasAcumulado;
	}

	public void setMlcrnvHorasAcumulado(Integer mlcrnvHorasAcumulado) {
		this.mlcrnvHorasAcumulado = mlcrnvHorasAcumulado;
	}

	public Integer getMlcrnvMlcrId() {
		return mlcrnvMlcrId;
	}

	public void setMlcrnvMlcrId(Integer mlcrnvMlcrId) {
		this.mlcrnvMlcrId = mlcrnvMlcrId;
	}

	public Integer getMlcrnvNvlId() {
		return mlcrnvNvlId;
	}

	public void setMlcrnvNvlId(Integer mlcrnvNvlId) {
		this.mlcrnvNvlId = mlcrnvNvlId;
	}

	public MallaCurricularDto getMlcrnvMallaCurricularDto() {
		return mlcrnvMallaCurricularDto;
	}

	public void setMlcrnvMallaCurricularDto(MallaCurricularDto mlcrnvMallaCurricularDto) {
		this.mlcrnvMallaCurricularDto = mlcrnvMallaCurricularDto;
	}

	public CarreraDto getMlcrnvCarreraDto() {
		return mlcrnvCarreraDto;
	}

	public void setMlcrnvCarreraDto(CarreraDto mlcrnvCarreraDto) {
		this.mlcrnvCarreraDto = mlcrnvCarreraDto;
	}

	public NivelDto getMlcrnvNivelDto() {
		return mlcrnvNivelDto;
	}

	public void setMlcrnvNivelDto(NivelDto mlcrnvNivelDto) {
		this.mlcrnvNivelDto = mlcrnvNivelDto;
	}

	public Integer getMlcrnvHorasSql() {
		return mlcrnvHorasSql;
	}

	public void setMlcrnvHorasSql(Integer mlcrnvHorasSql) {
		this.mlcrnvHorasSql = mlcrnvHorasSql;
	}

	public Integer getMlcrnvHorasAcumuladoSql() {
		return mlcrnvHorasAcumuladoSql;
	}

	public void setMlcrnvHorasAcumuladoSql(Integer mlcrnvHorasAcumuladoSql) {
		this.mlcrnvHorasAcumuladoSql = mlcrnvHorasAcumuladoSql;
	}
	
	public MateriaDto getMlcrnvMateriaDto() {
		return mlcrnvMateriaDto;
	}

	public void setMlcrnvMateriaDto(MateriaDto mlcrnvMateriaDto) {
		this.mlcrnvMateriaDto = mlcrnvMateriaDto;
	}

	public PeriodoAcademicoDto getMlcrnvPeriodoAcademicoDto() {
		return mlcrnvPeriodoAcademicoDto;
	}

	public void setMlcrnvPeriodoAcademicoDto(PeriodoAcademicoDto mlcrnvPeriodoAcademicoDto) {
		this.mlcrnvPeriodoAcademicoDto = mlcrnvPeriodoAcademicoDto;
	}

	public String toString() {
		return "MallaCurricularNivelDto [mlcrnvId=" + mlcrnvId + ", mlcrnvHoras=" + mlcrnvHoras
				+ ", mlcrnvHorasAcumulado=" + mlcrnvHorasAcumulado + ", mlcrnvMlcrId=" + mlcrnvMlcrId + ", mlcrnvNvlId="
				+ mlcrnvNvlId + "]";
	}

}
