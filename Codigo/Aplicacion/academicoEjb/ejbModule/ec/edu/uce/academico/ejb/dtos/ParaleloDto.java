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
   
 ARCHIVO:     ParaleloDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad ParaleloDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-03-2017 		  David Arellano   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Clase (DTO) ParaleloDto. DTO encargado de manejar los datos de la entidad
 * ParaleloDto.
 * 
 * @author darellano.
 * @version 1.0
 */
public class ParaleloDto implements Serializable {

	private static final long serialVersionUID = -3663263502299711591L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla paralelo
	private int prlId;
	private String prlCodigo;
	private String prlDescripcion;
	private Integer prlEstado;
	private Integer prlCupo;
	private Integer prlCarrera;
	private Integer prlPeriodoAcademico;
	private Timestamp prlFecha;
	private Timestamp prlInicioClase;
	private Timestamp prlFinClase;

	// Tabla mallaCurricularMateria
	private Integer mlcrmtId;
	private Integer mlcrmtNvlId;

	// Tabla mallaCurricularParalelo
	private Integer mlcrprId;
	private Integer mlcrprInscritos;
	private Integer mlcrprMatriculados;
	private Integer mlcrprCupo;
	private Integer mlcrprReservaRepetidos;
	private Integer mlcrprNivelacionCrrId;
	private Integer mlcrprModalidad;

	// Tabla periodoAcademico
	private Integer pracId;
	private String pracDescripcion;
	private Integer pracEstado;

	// Tabla Carrera
	private Integer crrId;
	private String crrDetalle;
	private String crrDescripcion;

	// Tabla Dependencia
	private String dpnDescripcion;

	// Tabla MATERIA
	private Integer mtrId;
	private String mtrDescripcion;
	private Integer mtrTimtId;
	private Integer mtrSubId;

	// Tabla Nivel
	private Integer nvlId;
	private String nvlDescripcion;

	// Tabla HORARIO ACADEMICO
	private int hracId;
	private String hracDescripcion;
	private Integer hracDia;
	private Integer hracEstado;
	private Integer hracMlcrprIdComp;

	// Tabla HORA CLASE AULA
	private int hoclalId;
	private Integer hoclalEstado;

	// Tabla HORA CLASE
	private int hoclId;
	private String hoclDescripcion;
	private Integer hoclEstado;
	private Integer hoclHoraInicio;
	private Integer hoclHoraFin;

	// Tabla AULA
	private int alaId;
	private String alaCodigo;
	private String alaDescripcion;
	private Integer alaEstado;

	private PersonaDto prlPersonaDto;
	private PeriodoAcademicoDto prlPeriodoAcademicoDto;
	private CarreraDto prlCarreraDto;
	private NivelDto prlNivelDto;
	private MallaCurricularParaleloDto prlMallaCurricularParaleloDto;
	private CargaHorariaDto prlCargaHorariaDto;
	private MateriaDto prlMateriaDto;
	private DependenciaDto prlDependenciaDto;

	// Tabla FichaDocente
	private int fcdcId;
	// PARA REPORTE NOTAS PRS
	private String prsIdentificacion;
	private String prsNombres;
	private String prsPrimerAPellido;
	private String prsSegundoApellido;

	public ParaleloDto() {

	}

	public ParaleloDto(int prlId, String prlDescripcion, Integer prlCarrera) {
		super();
		this.prlId = prlId;
		this.prlDescripcion = prlDescripcion;
		this.prlCarrera = prlCarrera;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getPrlId() {
		return prlId;
	}

	public void setPrlId(int prlId) {
		this.prlId = prlId;
	}

	public String getPrlCodigo() {
		return prlCodigo;
	}

	public void setPrlCodigo(String prlCodigo) {
		this.prlCodigo = prlCodigo;
	}

	public String getPrlDescripcion() {
		return prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
	}

	public Integer getPrlEstado() {
		return prlEstado;
	}

	public void setPrlEstado(Integer prlEstado) {
		this.prlEstado = prlEstado;
	}

	public Integer getPrlCarrera() {
		return prlCarrera;
	}

	public void setPrlCarrera(Integer prlCarrera) {
		this.prlCarrera = prlCarrera;
	}

	public Integer getPrlPeriodoAcademico() {
		return prlPeriodoAcademico;
	}

	public void setPrlPeriodoAcademico(Integer prlPeriodoAcademico) {
		this.prlPeriodoAcademico = prlPeriodoAcademico;
	}

	public Integer getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(Integer mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Integer getMlcrmtNvlId() {
		return mlcrmtNvlId;
	}

	public void setMlcrmtNvlId(Integer mlcrmtNvlId) {
		this.mlcrmtNvlId = mlcrmtNvlId;
	}

	public Integer getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(Integer mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public Integer getPrlCupo() {
		return prlCupo;
	}

	public void setPrlCupo(Integer prlCupo) {
		this.prlCupo = prlCupo;
	}

	public Integer getMlcrprInscritos() {
		return mlcrprInscritos;
	}

	public void setMlcrprInscritos(Integer mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

	public Integer getPracId() {
		return pracId;
	}

	public void setPracId(Integer pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}

	public Integer getMtrId() {
		return mtrId;
	}

	public void setMtrId(Integer mtrId) {
		this.mtrId = mtrId;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public Integer getMtrTimtId() {
		return mtrTimtId;
	}

	public void setMtrTimtId(Integer mtrTimtId) {
		this.mtrTimtId = mtrTimtId;
	}

	public Integer getMtrSubId() {
		return mtrSubId;
	}

	public void setMtrSubId(Integer mtrSubId) {
		this.mtrSubId = mtrSubId;
	}

	public String getNvlDescripcion() {
		return nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

	public Integer getMlcrprCupo() {
		return mlcrprCupo;
	}

	public void setMlcrprCupo(Integer mlcrprCupo) {
		this.mlcrprCupo = mlcrprCupo;
	}

	public Integer getNvlId() {
		return nvlId;
	}

	public void setNvlId(Integer nvlId) {
		this.nvlId = nvlId;
	}

	public int getHracId() {
		return hracId;
	}

	public void setHracId(int hracId) {
		this.hracId = hracId;
	}

	public String getHracDescripcion() {
		return hracDescripcion;
	}

	public void setHracDescripcion(String hracDescripcion) {
		this.hracDescripcion = hracDescripcion;
	}

	public Integer getHracDia() {
		return hracDia;
	}

	public void setHracDia(Integer hracDia) {
		this.hracDia = hracDia;
	}

	public Integer getHracEstado() {
		return hracEstado;
	}

	public void setHracEstado(Integer hracEstado) {
		this.hracEstado = hracEstado;
	}

	public Integer getHracMlcrprIdComp() {
		return hracMlcrprIdComp;
	}

	public void setHracMlcrprIdComp(Integer hracMlcrprIdComp) {
		this.hracMlcrprIdComp = hracMlcrprIdComp;
	}

	public int getHoclalId() {
		return hoclalId;
	}

	public void setHoclalId(int hoclalId) {
		this.hoclalId = hoclalId;
	}

	public Integer getHoclalEstado() {
		return hoclalEstado;
	}

	public void setHoclalEstado(Integer hoclalEstado) {
		this.hoclalEstado = hoclalEstado;
	}

	public int getHoclId() {
		return hoclId;
	}

	public void setHoclId(int hoclId) {
		this.hoclId = hoclId;
	}

	public String getHoclDescripcion() {
		return hoclDescripcion;
	}

	public void setHoclDescripcion(String hoclDescripcion) {
		this.hoclDescripcion = hoclDescripcion;
	}

	public Integer getHoclEstado() {
		return hoclEstado;
	}

	public void setHoclEstado(Integer hoclEstado) {
		this.hoclEstado = hoclEstado;
	}

	public Integer getHoclHoraInicio() {
		return hoclHoraInicio;
	}

	public void setHoclHoraInicio(Integer hoclHoraInicio) {
		this.hoclHoraInicio = hoclHoraInicio;
	}

	public Integer getHoclHoraFin() {
		return hoclHoraFin;
	}

	public void setHoclHoraFin(Integer hoclHoraFin) {
		this.hoclHoraFin = hoclHoraFin;
	}

	public int getAlaId() {
		return alaId;
	}

	public void setAlaId(int alaId) {
		this.alaId = alaId;
	}

	public String getAlaDescripcion() {
		return alaDescripcion;
	}

	public void setAlaDescripcion(String alaDescripcion) {
		this.alaDescripcion = alaDescripcion;
	}

	public Integer getAlaEstado() {
		return alaEstado;
	}

	public void setAlaEstado(Integer alaEstado) {
		this.alaEstado = alaEstado;
	}

	public Integer getMlcrprReservaRepetidos() {
		return mlcrprReservaRepetidos;
	}

	public void setMlcrprReservaRepetidos(Integer mlcrprReservaRepetidos) {
		this.mlcrprReservaRepetidos = mlcrprReservaRepetidos;
	}

	public Integer getMlcrprNivelacionCrrId() {
		return mlcrprNivelacionCrrId;
	}

	public void setMlcrprNivelacionCrrId(Integer mlcrprNivelacionCrrId) {
		this.mlcrprNivelacionCrrId = mlcrprNivelacionCrrId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Integer getMlcrprModalidad() {
		return mlcrprModalidad;
	}

	public void setMlcrprModalidad(Integer mlcrprModalidad) {
		this.mlcrprModalidad = mlcrprModalidad;
	}

	public Integer getMlcrprMatriculados() {
		return mlcrprMatriculados;
	}

	public void setMlcrprMatriculados(Integer mlcrprMatriculados) {
		this.mlcrprMatriculados = mlcrprMatriculados;
	}

	public Timestamp getPrlFecha() {
		return prlFecha;
	}

	public void setPrlFecha(Timestamp prlFecha) {
		this.prlFecha = prlFecha;
	}

	public PersonaDto getPrlPersonaDto() {
		return prlPersonaDto;
	}

	public void setPrlPersonaDto(PersonaDto prlPersonaDto) {
		this.prlPersonaDto = prlPersonaDto;
	}

	public String getAlaCodigo() {
		return alaCodigo;
	}

	public void setAlaCodigo(String alaCodigo) {
		this.alaCodigo = alaCodigo;
	}

	public PeriodoAcademicoDto getPrlPeriodoAcademicoDto() {
		return prlPeriodoAcademicoDto;
	}

	public void setPrlPeriodoAcademicoDto(PeriodoAcademicoDto prlPeriodoAcademicoDto) {
		this.prlPeriodoAcademicoDto = prlPeriodoAcademicoDto;
	}

	public CarreraDto getPrlCarreraDto() {
		return prlCarreraDto;
	}

	public void setPrlCarreraDto(CarreraDto prlCarreraDto) {
		this.prlCarreraDto = prlCarreraDto;
	}

	public NivelDto getPrlNivelDto() {
		return prlNivelDto;
	}

	public void setPrlNivelDto(NivelDto prlNivelDto) {
		this.prlNivelDto = prlNivelDto;
	}

	public MallaCurricularParaleloDto getPrlMallaCurricularParaleloDto() {
		return prlMallaCurricularParaleloDto;
	}

	public void setPrlMallaCurricularParaleloDto(MallaCurricularParaleloDto prlMallaCurricularParaleloDto) {
		this.prlMallaCurricularParaleloDto = prlMallaCurricularParaleloDto;
	}

	public CargaHorariaDto getPrlCargaHorariaDto() {
		return prlCargaHorariaDto;
	}

	public void setPrlCargaHorariaDto(CargaHorariaDto prlCargaHorariaDto) {
		this.prlCargaHorariaDto = prlCargaHorariaDto;
	}

	public int getFcdcId() {
		return fcdcId;
	}

	public void setFcdcId(int fcdcId) {
		this.fcdcId = fcdcId;
	}

	public Timestamp getPrlInicioClase() {
		return prlInicioClase;
	}

	public void setPrlInicioClase(Timestamp prlInicioClase) {
		this.prlInicioClase = prlInicioClase;
	}

	public Timestamp getPrlFinClase() {
		return prlFinClase;
	}

	public void setPrlFinClase(Timestamp prlFinClase) {
		this.prlFinClase = prlFinClase;
	}

	public MateriaDto getPrlMateriaDto() {
		return prlMateriaDto;
	}

	public void setPrlMateriaDto(MateriaDto prlMateriaDto) {
		this.prlMateriaDto = prlMateriaDto;
	}

	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}

	public String getPrsNombres() {
		return prsNombres;
	}

	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}

	public String getPrsPrimerAPellido() {
		return prsPrimerAPellido;
	}

	public void setPrsPrimerAPellido(String prsPrimerAPellido) {
		this.prsPrimerAPellido = prsPrimerAPellido;
	}

	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}

	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}

	public DependenciaDto getPrlDependenciaDto() {
		return prlDependenciaDto;
	}

	public void setPrlDependenciaDto(DependenciaDto prlDependenciaDto) {
		this.prlDependenciaDto = prlDependenciaDto;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();

		sb.append(" Paralelo ");
		sb.append(tabulador + "prlId : " + prlId);
		sb.append(tabulador + "prlCodigo : " + (prlCodigo == null ? "NULL" : prlCodigo));
		sb.append(tabulador + "prlDescripcion : " + (prlDescripcion == null ? "NULL" : prlDescripcion));
		sb.append(tabulador + "prlEstado : " + (prlEstado == null ? "NULL" : prlEstado));
		sb.append(tabulador + "prlCupo : " + (prlCupo == null ? "NULL" : prlCupo));
		sb.append(tabulador + "prlCarrera : " + (prlCarrera == null ? "NULL" : prlCarrera));
		sb.append(tabulador + "prlPeriodoAcademico : " + (prlPeriodoAcademico == null ? "NULL" : prlPeriodoAcademico));

		sb.append(" MallaCurricularMateria ");
		sb.append(tabulador + "mlcrmtId : " + mlcrmtId);
		sb.append(tabulador + "mlcrmtNvlId : " + mlcrmtNvlId);

		sb.append(" MallaCurricularParalelo ");
		sb.append(tabulador + "mlcrprId : " + mlcrprId);
		sb.append(tabulador + "mlcrprInscritos : " + (mlcrprInscritos == null ? "NULL" : mlcrprInscritos));

		sb.append(" PeriodoAcademico");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));

		sb.append(" Carrera");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));

		sb.append(" Dependencia");
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));

		sb.append(" Materia");
		sb.append(tabulador + "mtrId : " + mtrId);
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
		sb.append(tabulador + "mtrTimtId : " + (mtrTimtId == null ? "NULL" : mtrTimtId));
		sb.append(tabulador + "mtrSubId : " + (mtrSubId == null ? "NULL" : mtrSubId));

		sb.append(" Nivel");
		sb.append(tabulador + "nvlDescripcion : " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));

		sb.append(" HorarioAcademico ");
		sb.append(tabulador + "hracId : " + hracId);
		sb.append(tabulador + "hracDescripcion : " + (hracDescripcion == null ? "NULL" : hracDescripcion));
		sb.append(tabulador + "hracDia : " + (hracDia == null ? "NULL" : hracDia));
		sb.append(tabulador + "hracEstado : " + (hracEstado == null ? "NULL" : hracEstado));
		sb.append(tabulador + "hracMlcrprIdComp : " + (hracMlcrprIdComp == null ? "NULL" : hracMlcrprIdComp));

		sb.append(" HoraClaseAula ");
		sb.append(tabulador + "hoclalId : " + hoclalId);
		sb.append(tabulador + "hoclalEstado : " + (hoclalEstado == null ? "NULL" : hoclalEstado));

		sb.append(" HoraClase ");
		sb.append(tabulador + "hoclId : " + hoclId);
		sb.append(tabulador + "hoclDescripcion : " + (hoclDescripcion == null ? "NULL" : hoclDescripcion));
		sb.append(tabulador + "hoclEstado : " + (hoclEstado == null ? "NULL" : hoclEstado));
		sb.append(tabulador + "hoclHoraInicio : " + (hoclHoraInicio == null ? "NULL" : hoclHoraInicio));
		sb.append(tabulador + "hoclHoraFin : " + (hoclHoraFin == null ? "NULL" : hoclHoraFin));

		sb.append(" Aula ");
		sb.append(tabulador + "alaId : " + alaId);
		sb.append(tabulador + "alaDescripcion : " + (alaDescripcion == null ? "NULL" : alaDescripcion));
		sb.append(tabulador + "alaEstado : " + (alaEstado == null ? "NULL" : alaEstado));

		return sb.toString();
	}

}
