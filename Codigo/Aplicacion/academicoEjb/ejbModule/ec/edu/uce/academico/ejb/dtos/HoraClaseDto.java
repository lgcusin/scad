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
   
 ARCHIVO:     HoraClaseDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad HoraClaseDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-SEPT-2017 		  Dennis Collaguazo   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) HoraClaseDto. DTO encargado de manejar los datos de la entidad
 * HoraClaseDto.
 * 
 * @author dcollaguazo.
 * @version 1.0
 */
public class HoraClaseDto implements Serializable {

	private static final long serialVersionUID = -5768676106415490425L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Aula
	private Integer alaId;
	private String alaCodigo;
	private String alaDescripcion;
	private String alaCapacidad;

	// Tabla Hora Clase
	private Integer hoclId;
	private String hoclDescripcion;
	private String hoclHoraInicio;
	private String hoclHoraFin;
	private Integer hoclPosicionX;
	private Integer hoclPosicionY;
	private Integer hoclDiaValue;
	private String hoclDiaLabel;
	private Boolean hoclCheckBox;
	private Boolean hoclDisableCheck;

	private HoraClaseDto hoclLunesHoraClaseDto;
	private HoraClaseDto hoclMartesHoraClaseDto;
	private HoraClaseDto hoclMiercolesHoraClaseDto;
	private HoraClaseDto hoclJuevesHoraClaseDto;
	private HoraClaseDto hoclViernesHoraClaseDto;
	private HoraClaseDto hoclSabadoHoraClaseDto;
	private HoraClaseDto hoclDomingoHoraClaseDto;
	// Tabla Hora Clase Aula
	private Integer hoclauId;
	private Integer hoclauEstado;

	// Tabla Horario Academico
	private Integer hracId;
	private Integer hracDia;
	private String hracFechaInicio;
	private String hracFechaFin;
	private Integer hracMlcrprId;
	private Integer hracMlcrprIdComp;
	private Integer hracHoraTipo;

	private ParaleloDto hracParaleloDto;
	private PeriodoAcademicoDto hracPeriodoAcademicoDto;
	private PersonaDto hoclPersonaDto;
	private CargaHorariaDto hoclCargaHorariaDto;
	private MateriaDto hoclMateriaDto;
	private CarreraDto hoclCarreraDto;
	private HorarioFuncionDto hoclHorarioFuncionDto;

	public HoraClaseDto() {

	}

	public HoraClaseDto(Integer hoclPosicionX, Integer hoclPosicionY, Integer hoclauId) {
		super();
		this.hoclPosicionX = hoclPosicionX;
		this.hoclPosicionY = hoclPosicionY;
		this.hoclauId = hoclauId;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public Integer getAlaId() {
		return alaId;
	}

	public void setAlaId(Integer alaId) {
		this.alaId = alaId;
	}

	public String getAlaCodigo() {
		return alaCodigo;
	}

	public void setAlaCodigo(String alaCodigo) {
		this.alaCodigo = alaCodigo;
	}

	public String getAlaDescripcion() {
		return alaDescripcion;
	}

	public void setAlaDescripcion(String alaDescripcion) {
		this.alaDescripcion = alaDescripcion;
	}

	public String getAlaCapacidad() {
		return alaCapacidad;
	}

	public void setAlaCapacidad(String alaCapacidad) {
		this.alaCapacidad = alaCapacidad;
	}

	public Integer getHoclId() {
		return hoclId;
	}

	public void setHoclId(Integer hoclId) {
		this.hoclId = hoclId;
	}

	public String getHoclDescripcion() {
		return hoclDescripcion;
	}

	public void setHoclDescripcion(String hoclDescripcion) {
		this.hoclDescripcion = hoclDescripcion;
	}

	public String getHoclHoraInicio() {
		return hoclHoraInicio;
	}

	public void setHoclHoraInicio(String hoclHoraInicio) {
		this.hoclHoraInicio = hoclHoraInicio;
	}

	public String getHoclHoraFin() {
		return hoclHoraFin;
	}

	public void setHoclHoraFin(String hoclHoraFin) {
		this.hoclHoraFin = hoclHoraFin;
	}

	// public Integer getHoclal() {
	// return hoclal;
	// }
	// public void setHoclal(Integer hoclal) {
	// this.hoclal = hoclal;
	// }
	public Integer getHracId() {
		return hracId;
	}

	public void setHracId(Integer hracId) {
		this.hracId = hracId;
	}

	public Integer getHracDia() {
		return hracDia;
	}

	public void setHracDia(Integer hracDia) {
		this.hracDia = hracDia;
	}

	public String getHracFechaInicio() {
		return hracFechaInicio;
	}

	public void setHracFechaInicio(String hracFechaInicio) {
		this.hracFechaInicio = hracFechaInicio;
	}

	public String getHracFechaFin() {
		return hracFechaFin;
	}

	public void setHracFechaFin(String hracFechaFin) {
		this.hracFechaFin = hracFechaFin;
	}

	public Integer getHoclPosicionX() {
		return hoclPosicionX;
	}

	public void setHoclPosicionX(Integer hoclPosicionX) {
		this.hoclPosicionX = hoclPosicionX;
	}

	public Integer getHoclPosicionY() {
		return hoclPosicionY;
	}

	public void setHoclPosicionY(Integer hoclPosicionY) {
		this.hoclPosicionY = hoclPosicionY;
	}

	public Integer getHoclauId() {
		return hoclauId;
	}

	public void setHoclauId(Integer hoclauId) {
		this.hoclauId = hoclauId;
	}

	public Integer getHoclDiaValue() {
		return hoclDiaValue;
	}

	public void setHoclDiaValue(Integer hoclDiaValue) {
		this.hoclDiaValue = hoclDiaValue;
	}

	public String getHoclDiaLabel() {
		return hoclDiaLabel;
	}

	public void setHoclDiaLabel(String hoclDiaLabel) {
		this.hoclDiaLabel = hoclDiaLabel;
	}

	public HoraClaseDto getHoclLunesHoraClaseDto() {
		return hoclLunesHoraClaseDto;
	}

	public void setHoclLunesHoraClaseDto(HoraClaseDto hoclLunesHoraClaseDto) {
		this.hoclLunesHoraClaseDto = hoclLunesHoraClaseDto;
	}

	public HoraClaseDto getHoclMartesHoraClaseDto() {
		return hoclMartesHoraClaseDto;
	}

	public void setHoclMartesHoraClaseDto(HoraClaseDto hoclMartesHoraClaseDto) {
		this.hoclMartesHoraClaseDto = hoclMartesHoraClaseDto;
	}

	public HoraClaseDto getHoclMiercolesHoraClaseDto() {
		return hoclMiercolesHoraClaseDto;
	}

	public void setHoclMiercolesHoraClaseDto(HoraClaseDto hoclMiercolesHoraClaseDto) {
		this.hoclMiercolesHoraClaseDto = hoclMiercolesHoraClaseDto;
	}

	public HoraClaseDto getHoclJuevesHoraClaseDto() {
		return hoclJuevesHoraClaseDto;
	}

	public void setHoclJuevesHoraClaseDto(HoraClaseDto hoclJuevesHoraClaseDto) {
		this.hoclJuevesHoraClaseDto = hoclJuevesHoraClaseDto;
	}

	public HoraClaseDto getHoclViernesHoraClaseDto() {
		return hoclViernesHoraClaseDto;
	}

	public void setHoclViernesHoraClaseDto(HoraClaseDto hoclViernesHoraClaseDto) {
		this.hoclViernesHoraClaseDto = hoclViernesHoraClaseDto;
	}

	public HoraClaseDto getHoclSabadoHoraClaseDto() {
		return hoclSabadoHoraClaseDto;
	}

	public void setHoclSabadoHoraClaseDto(HoraClaseDto hoclSabadoHoraClaseDto) {
		this.hoclSabadoHoraClaseDto = hoclSabadoHoraClaseDto;
	}

	public HoraClaseDto getHoclDomingoHoraClaseDto() {
		return hoclDomingoHoraClaseDto;
	}

	public void setHoclDomingoHoraClaseDto(HoraClaseDto hoclDomingoHoraClaseDto) {
		this.hoclDomingoHoraClaseDto = hoclDomingoHoraClaseDto;
	}

	public Boolean getHoclCheckBox() {
		return hoclCheckBox;
	}

	public void setHoclCheckBox(Boolean hoclCheckBox) {
		this.hoclCheckBox = hoclCheckBox;
	}

	public Boolean getHoclDisableCheck() {
		return hoclDisableCheck;
	}

	public void setHoclDisableCheck(Boolean hoclDisableCheck) {
		this.hoclDisableCheck = hoclDisableCheck;
	}

	public ParaleloDto getHracParaleloDto() {
		return hracParaleloDto;
	}

	public void setHracParaleloDto(ParaleloDto hracParaleloDto) {
		this.hracParaleloDto = hracParaleloDto;
	}

	public Integer getHracMlcrprId() {
		return hracMlcrprId;
	}

	public void setHracMlcrprId(Integer hracMlcrprId) {
		this.hracMlcrprId = hracMlcrprId;
	}

	public Integer getHracMlcrprIdComp() {
		return hracMlcrprIdComp;
	}

	public void setHracMlcrprIdComp(Integer hracMlcrprIdComp) {
		this.hracMlcrprIdComp = hracMlcrprIdComp;
	}

	public PeriodoAcademicoDto getHracPeriodoAcademicoDto() {
		return hracPeriodoAcademicoDto;
	}

	public void setHracPeriodoAcademicoDto(PeriodoAcademicoDto hracPeriodoAcademicoDto) {
		this.hracPeriodoAcademicoDto = hracPeriodoAcademicoDto;
	}

	public PersonaDto getHoclPersonaDto() {
		return hoclPersonaDto;
	}

	public void setHoclPersonaDto(PersonaDto hoclPersonaDto) {
		this.hoclPersonaDto = hoclPersonaDto;
	}

	public Integer getHoclauEstado() {
		return hoclauEstado;
	}

	public void setHoclauEstado(Integer hoclauEstado) {
		this.hoclauEstado = hoclauEstado;
	}

	public Integer getHracHoraTipo() {
		return hracHoraTipo;
	}

	public void setHracHoraTipo(Integer hracHoraTipo) {
		this.hracHoraTipo = hracHoraTipo;
	}

	public CargaHorariaDto getHoclCargaHorariaDto() {
		return hoclCargaHorariaDto;
	}

	public void setHoclCargaHorariaDto(CargaHorariaDto hoclCargaHorariaDto) {
		this.hoclCargaHorariaDto = hoclCargaHorariaDto;
	}

	public MateriaDto getHoclMateriaDto() {
		return hoclMateriaDto;
	}

	public void setHoclMateriaDto(MateriaDto hoclMateriaDto) {
		this.hoclMateriaDto = hoclMateriaDto;
	}

	public CarreraDto getHoclCarreraDto() {
		return hoclCarreraDto;
	}

	public void setHoclCarreraDto(CarreraDto hoclCarreraDto) {
		this.hoclCarreraDto = hoclCarreraDto;
	}

	public HorarioFuncionDto getHoclHorarioFuncionDto() {
		return hoclHorarioFuncionDto;
	}

	public void setHoclHorarioFuncionDto(HorarioFuncionDto hoclHorarioFuncionDto) {
		this.hoclHorarioFuncionDto = hoclHorarioFuncionDto;
	}

}
