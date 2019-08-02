package ec.edu.uce.academico.ejb.dtos;

public class CargaHorariaDto implements java.io.Serializable {

	private static final long serialVersionUID = -4293101984087230351L;

	private int cahrId;
	private String cahrObservacion;
	private Integer cahrEstado;
	private Integer cahrEstadoEliminacion;
	private Integer cahrNumHoras;
	private Integer cahrCarreraId;
	private Integer cahrMlcrprId;
	private Integer cahrMlcrprIdPrincipal;

	// Tipo Carga Horaria
	private Integer ticahrId;
	private String ticahrDescripcion;
	private Integer ticahrEstado;

	// Funcion Carga Horaria
	private Integer fncahrId;
	private String fncahrDescripcion;
	private Integer fncahrHorasAsignadas;
	private Integer fncahrHorasMinimo;
	private Integer fncahrHorasMaximo;
	private Integer fncahrTiempoDedicacion;

	// Detalle Puesto
	private Integer dtpsId;
	private Integer dtpsEstado;

	// Tiempo de Dedicacion
	private Integer tmddId;
	private String tmddDescripcion;
	private String tmddCategoria;
	private Integer tmddHoras;
	private Integer tmddEstado;

	// Relacion Laboral
	private Integer rllbId;
	private String rllbDescripcion;
	private Integer rllbEstado;

	private MallaCurricularParaleloDto cahrMallaCurricularParaleloDto;
	private PeriodoAcademicoDto cahrPeriodoAcademicoDto;
	private DependenciaDto carhDependenciaDto;
	private CarreraDto cahrCarreraDto;
	private PersonaDto cahrPersonaDto;
	private MateriaDto cahrMateriaDto;
	private ParaleloDto cahrParaleloDto;

	public CargaHorariaDto() {
	}

	public int getCahrId() {
		return cahrId;
	}

	public void setCahrId(int cahrId) {
		this.cahrId = cahrId;
	}

	public String getCahrObservacion() {
		return cahrObservacion;
	}

	public void setCahrObservacion(String cahrObservacion) {
		this.cahrObservacion = cahrObservacion;
	}

	public Integer getCahrEstado() {
		return cahrEstado;
	}

	public void setCahrEstado(Integer cahrEstado) {
		this.cahrEstado = cahrEstado;
	}

	public Integer getCahrEstadoEliminacion() {
		return cahrEstadoEliminacion;
	}

	public void setCahrEstadoEliminacion(Integer cahrEstadoEliminacion) {
		this.cahrEstadoEliminacion = cahrEstadoEliminacion;
	}

	public Integer getCahrNumHoras() {
		return cahrNumHoras;
	}

	public void setCahrNumHoras(Integer cahrNumHoras) {
		this.cahrNumHoras = cahrNumHoras;
	}

	public Integer getCahrCarreraId() {
		return cahrCarreraId;
	}

	public void setCahrCarreraId(Integer cahrCarreraId) {
		this.cahrCarreraId = cahrCarreraId;
	}

	public Integer getTicahrId() {
		return ticahrId;
	}

	public void setTicahrId(Integer ticahrId) {
		this.ticahrId = ticahrId;
	}

	public String getTicahrDescripcion() {
		return ticahrDescripcion;
	}

	public void setTicahrDescripcion(String ticahrDescripcion) {
		this.ticahrDescripcion = ticahrDescripcion;
	}

	public Integer getTicahrEstado() {
		return ticahrEstado;
	}

	public void setTicahrEstado(Integer ticahrEstado) {
		this.ticahrEstado = ticahrEstado;
	}

	public Integer getFncahrId() {
		return fncahrId;
	}

	public void setFncahrId(Integer fncahrId) {
		this.fncahrId = fncahrId;
	}

	public String getFncahrDescripcion() {
		return fncahrDescripcion;
	}

	public void setFncahrDescripcion(String fncahrDescripcion) {
		this.fncahrDescripcion = fncahrDescripcion;
	}

	public Integer getFncahrHorasAsignadas() {
		return fncahrHorasAsignadas;
	}

	public void setFncahrHorasAsignadas(Integer fncahrHorasAsignadas) {
		this.fncahrHorasAsignadas = fncahrHorasAsignadas;
	}

	public Integer getFncahrHorasMinimo() {
		return fncahrHorasMinimo;
	}

	public void setFncahrHorasMinimo(Integer fncahrHorasMinimo) {
		this.fncahrHorasMinimo = fncahrHorasMinimo;
	}

	public Integer getFncahrHorasMaximo() {
		return fncahrHorasMaximo;
	}

	public void setFncahrHorasMaximo(Integer fncahrHorasMaximo) {
		this.fncahrHorasMaximo = fncahrHorasMaximo;
	}

	public Integer getFncahrTiempoDedicacion() {
		return fncahrTiempoDedicacion;
	}

	public void setFncahrTiempoDedicacion(Integer fncahrTiempoDedicacion) {
		this.fncahrTiempoDedicacion = fncahrTiempoDedicacion;
	}

	public Integer getDtpsId() {
		return dtpsId;
	}

	public void setDtpsId(Integer dtpsId) {
		this.dtpsId = dtpsId;
	}

	public Integer getDtpsEstado() {
		return dtpsEstado;
	}

	public void setDtpsEstado(Integer dtpsEstado) {
		this.dtpsEstado = dtpsEstado;
	}

	public MallaCurricularParaleloDto getCahrMallaCurricularParaleloDto() {
		return cahrMallaCurricularParaleloDto;
	}

	public void setCahrMallaCurricularParaleloDto(MallaCurricularParaleloDto cahrMallaCurricularParaleloDto) {
		this.cahrMallaCurricularParaleloDto = cahrMallaCurricularParaleloDto;
	}

	public PeriodoAcademicoDto getCahrPeriodoAcademicoDto() {
		return cahrPeriodoAcademicoDto;
	}

	public void setCahrPeriodoAcademicoDto(PeriodoAcademicoDto cahrPeriodoAcademicoDto) {
		this.cahrPeriodoAcademicoDto = cahrPeriodoAcademicoDto;
	}

	public DependenciaDto getCarhDependenciaDto() {
		return carhDependenciaDto;
	}

	public void setCarhDependenciaDto(DependenciaDto carhDependenciaDto) {
		this.carhDependenciaDto = carhDependenciaDto;
	}

	public CarreraDto getCahrCarreraDto() {
		return cahrCarreraDto;
	}

	public void setCahrCarreraDto(CarreraDto cahrCarreraDto) {
		this.cahrCarreraDto = cahrCarreraDto;
	}

	public PersonaDto getCahrPersonaDto() {
		return cahrPersonaDto;
	}

	public void setCahrPersonaDto(PersonaDto cahrPersonaDto) {
		this.cahrPersonaDto = cahrPersonaDto;
	}

	public MateriaDto getCahrMateriaDto() {
		return cahrMateriaDto;
	}

	public void setCahrMateriaDto(MateriaDto cahrMateriaDto) {
		this.cahrMateriaDto = cahrMateriaDto;
	}

	public ParaleloDto getCahrParaleloDto() {
		return cahrParaleloDto;
	}

	public void setCahrParaleloDto(ParaleloDto cahrParaleloDto) {
		this.cahrParaleloDto = cahrParaleloDto;
	}

	public Integer getTmddId() {
		return tmddId;
	}

	public void setTmddId(Integer tmddId) {
		this.tmddId = tmddId;
	}

	public String getTmddDescripcion() {
		return tmddDescripcion;
	}

	public void setTmddDescripcion(String tmddDescripcion) {
		this.tmddDescripcion = tmddDescripcion;
	}

	public Integer getTmddHoras() {
		return tmddHoras;
	}

	public void setTmddHoras(Integer tmddHoras) {
		this.tmddHoras = tmddHoras;
	}

	public Integer getTmddEstado() {
		return tmddEstado;
	}

	public void setTmddEstado(Integer tmddEstado) {
		this.tmddEstado = tmddEstado;
	}

	public Integer getRllbId() {
		return rllbId;
	}

	public void setRllbId(Integer rllbId) {
		this.rllbId = rllbId;
	}

	public String getRllbDescripcion() {
		return rllbDescripcion;
	}

	public void setRllbDescripcion(String rllbDescripcion) {
		this.rllbDescripcion = rllbDescripcion;
	}

	public Integer getRllbEstado() {
		return rllbEstado;
	}

	public void setRllbEstado(Integer rllbEstado) {
		this.rllbEstado = rllbEstado;
	}

	public String getTmddCategoria() {
		return tmddCategoria;
	}

	public void setTmddCategoria(String tmddCategoria) {
		this.tmddCategoria = tmddCategoria;
	}

	public Integer getCahrMlcrprId() {
		return cahrMlcrprId;
	}

	public void setCahrMlcrprId(Integer cahrMlcrprId) {
		this.cahrMlcrprId = cahrMlcrprId;
	}

	public Integer getCahrMlcrprIdPrincipal() {
		return cahrMlcrprIdPrincipal;
	}

	public void setCahrMlcrprIdPrincipal(Integer cahrMlcrprIdPrincipal) {
		this.cahrMlcrprIdPrincipal = cahrMlcrprIdPrincipal;
	}

}
