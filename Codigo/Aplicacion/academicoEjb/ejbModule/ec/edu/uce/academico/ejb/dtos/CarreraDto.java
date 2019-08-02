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
   
 ARCHIVO:     CarreraDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Carrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02-03-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ec.edu.uce.academico.jpa.entidades.publico.Nivel;

/**
 * Clase (DTO) CarreraDto. DTO encargado de manejar los datos de la entidad
 * Carrera.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class CarreraDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Carrera
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrDpnId;
	private Integer crrTipo;
	private Integer crrProceso;
	private Integer crrEspeCodigo;
	private Integer crrReajusteMatricula;
	private Integer crrIdentificador;// objeto creado para busqueda
	private Boolean crrAcceso;
	private Integer crrOrdinal;
	private Integer crrEvaluado;
	private Integer crrNoEvaluado;
	private Integer crrEstado;
	private Integer crrTipoEvaluacion;

	// Tabla Dependencia
	private int dpnId;
	private Integer dpnSubId;
	private Integer dpnUbcId;
	private String dpnDescripcion;
	private Integer dpnJerarquia;
	private Integer dpnEstado;
	private Integer dpnCampus;

	// malla_curricular
	private int mlcrId;
	private Integer mlcrCarrera;
	private Integer mlcrTipoFormacionMalla;
	private String mlcrCodigo;
	private String mlcrDescripcion;
	private Integer mlcrEstado;
	private Date mlcrFechaInicio;
	private Date mlcrFechaFin;
	private Integer mlcrTotalHoras;
	private Integer mlcrTotalMaterias;
	private Integer mlcrTipoOrgAprendizaje;
	private Integer mlcrVigente;
	private Integer mlcrTipoAprobacion;

	// malla_periodo
	private int mlprId;
	private Integer mlprMallaCurricular;
	private Integer mlprPeriodoAcademico;
	private Integer mlprEstado;

	// periodo_academico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;
	private Integer pracTipo;

	// malla_curricular_materia
	private int mlcrmtId;
	private Integer mlcrmtNivelPreRequisito;
	private Integer mlcrmtMallaCurricular;
	private Nivel mlcrmtNivel;
	private Integer mlcrmtUnidadFormacion;
	private Integer mlcrmtMateria;
	private Integer mlcrmtEstado;

	// materia
	private int mtrId;
	private Integer mtrCampoFormacion;
	private Integer mtrTipoMateria;
	private Integer mtrMateria;
	private Integer mtrCarrera;
	private Integer mtrNucleoProblemicoCarrera;
	private String mtrCodigo;
	private String mtrCodigoSniese;
	private String mtrDescripcion;
	private Integer mtrEstado;
	private Integer mtrHoras;
	private Integer mtrIntegradoraHoras;
	private Integer mtrPreProfesionalHoras;
	private Integer mtrHorasCien;
	private Integer mtrRelacionTrabajo;
	private Integer mtrUnidadMedida;
	private BigDecimal mtrCreditos;

	// persona
	private int prsId;
	private String prsIdentificacion;
	private String prsNombres;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsEmailInstitucional;

	// ficha_inscripcion
	private int fcinId;
	private Integer fcinCncrId;
	private Integer fcinPracId;
	
	//Nota corte
	
	private int nocrId;
	private Integer nocrEstado;
	private Float nocrNota;
	private Integer nocrPracId;
	boolean existeNotaCorte;  //permite verificar si se debe editar o crear nuevo
	

	private PersonaDto crrPersonaDto;
	private PeriodoAcademicoDto crrPeriodoAcademicoDto;
	private List<CarreraDto> crrListCarreraDto;

	public CarreraDto() {
	}

	public CarreraDto(Integer crrId) {
		this.crrId = crrId;
	}

	public CarreraDto(int crrId, String crrDescripcion) {
		super();
		this.crrId = crrId;
		this.crrDescripcion = crrDescripcion;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

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

	public String getCrrCodSniese() {
		return crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Date getCrrFechaCreacion() {
		return crrFechaCreacion;
	}

	public void setCrrFechaCreacion(Date crrFechaCreacion) {
		this.crrFechaCreacion = crrFechaCreacion;
	}

	public String getCrrResolucion() {
		return crrResolucion;
	}

	public void setCrrResolucion(String crrResolucion) {
		this.crrResolucion = crrResolucion;
	}

	public Integer getCrrDpnId() {
		return crrDpnId;
	}

	public void setCrrDpnId(Integer crrDpnId) {
		this.crrDpnId = crrDpnId;
	}

	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public Integer getDpnSubId() {
		return dpnSubId;
	}

	public void setDpnSubId(Integer dpnSubId) {
		this.dpnSubId = dpnSubId;
	}

	public Integer getDpnUbcId() {
		return dpnUbcId;
	}

	public void setDpnUbcId(Integer dpnUbcId) {
		this.dpnUbcId = dpnUbcId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Integer getDpnJerarquia() {
		return dpnJerarquia;
	}

	public void setDpnJerarquia(Integer dpnJerarquia) {
		this.dpnJerarquia = dpnJerarquia;
	}

	public Integer getDpnEstado() {
		return dpnEstado;
	}

	public void setDpnEstado(Integer dpnEstado) {
		this.dpnEstado = dpnEstado;
	}

	public Integer getDpnCampus() {
		return dpnCampus;
	}

	public void setDpnCampus(Integer dpnCampus) {
		this.dpnCampus = dpnCampus;
	}

	public Integer getCrrIdentificador() {
		return crrIdentificador;
	}

	public void setCrrIdentificador(Integer crrIdentificador) {
		this.crrIdentificador = crrIdentificador;
	}

	public int getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(int mlcrId) {
		this.mlcrId = mlcrId;
	}

	public Integer getMlcrCarrera() {
		return mlcrCarrera;
	}

	public void setMlcrCarrera(Integer mlcrCarrera) {
		this.mlcrCarrera = mlcrCarrera;
	}

	public Integer getMlcrTipoFormacionMalla() {
		return mlcrTipoFormacionMalla;
	}

	public void setMlcrTipoFormacionMalla(Integer mlcrTipoFormacionMalla) {
		this.mlcrTipoFormacionMalla = mlcrTipoFormacionMalla;
	}

	public String getMlcrCodigo() {
		return mlcrCodigo;
	}

	public void setMlcrCodigo(String mlcrCodigo) {
		this.mlcrCodigo = mlcrCodigo;
	}

	public String getMlcrDescripcion() {
		return mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
	}

	public Integer getMlcrEstado() {
		return mlcrEstado;
	}

	public void setMlcrEstado(Integer mlcrEstado) {
		this.mlcrEstado = mlcrEstado;
	}

	public Date getMlcrFechaInicio() {
		return mlcrFechaInicio;
	}

	public void setMlcrFechaInicio(Date mlcrFechaInicio) {
		this.mlcrFechaInicio = mlcrFechaInicio;
	}

	public Date getMlcrFechaFin() {
		return mlcrFechaFin;
	}

	public void setMlcrFechaFin(Date mlcrFechaFin) {
		this.mlcrFechaFin = mlcrFechaFin;
	}

	public Integer getMlcrTotalHoras() {
		return mlcrTotalHoras;
	}

	public void setMlcrTotalHoras(Integer mlcrTotalHoras) {
		this.mlcrTotalHoras = mlcrTotalHoras;
	}

	public Integer getMlcrTotalMaterias() {
		return mlcrTotalMaterias;
	}

	public void setMlcrTotalMaterias(Integer mlcrTotalMaterias) {
		this.mlcrTotalMaterias = mlcrTotalMaterias;
	}

	public Integer getMlcrTipoOrgAprendizaje() {
		return mlcrTipoOrgAprendizaje;
	}

	public void setMlcrTipoOrgAprendizaje(Integer mlcrTipoOrgAprendizaje) {
		this.mlcrTipoOrgAprendizaje = mlcrTipoOrgAprendizaje;
	}

	public Integer getMlcrVigente() {
		return mlcrVigente;
	}

	public void setMlcrVigente(Integer mlcrVigente) {
		this.mlcrVigente = mlcrVigente;
	}

	public Integer getMlcrTipoAprobacion() {
		return mlcrTipoAprobacion;
	}

	public void setMlcrTipoAprobacion(Integer mlcrTipoAprobacion) {
		this.mlcrTipoAprobacion = mlcrTipoAprobacion;
	}

	public int getMlprId() {
		return mlprId;
	}

	public void setMlprId(int mlprId) {
		this.mlprId = mlprId;
	}

	public Integer getMlprMallaCurricular() {
		return mlprMallaCurricular;
	}

	public void setMlprMallaCurricular(Integer mlprMallaCurricular) {
		this.mlprMallaCurricular = mlprMallaCurricular;
	}

	public Integer getMlprPeriodoAcademico() {
		return mlprPeriodoAcademico;
	}

	public void setMlprPeriodoAcademico(Integer mlprPeriodoAcademico) {
		this.mlprPeriodoAcademico = mlprPeriodoAcademico;
	}

	public Integer getMlprEstado() {
		return mlprEstado;
	}

	public void setMlprEstado(Integer mlprEstado) {
		this.mlprEstado = mlprEstado;
	}

	public int getPracId() {
		return pracId;
	}

	public void setPracId(int pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}

	public Date getPracFechaIncio() {
		return pracFechaIncio;
	}

	public void setPracFechaIncio(Date pracFechaIncio) {
		this.pracFechaIncio = pracFechaIncio;
	}

	public Date getPracFechaFin() {
		return pracFechaFin;
	}

	public void setPracFechaFin(Date pracFechaFin) {
		this.pracFechaFin = pracFechaFin;
	}

	public Integer getPracTipo() {
		return pracTipo;
	}

	public void setPracTipo(Integer pracTipo) {
		this.pracTipo = pracTipo;
	}

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Integer getMlcrmtNivelPreRequisito() {
		return mlcrmtNivelPreRequisito;
	}

	public void setMlcrmtNivelPreRequisito(Integer mlcrmtNivelPreRequisito) {
		this.mlcrmtNivelPreRequisito = mlcrmtNivelPreRequisito;
	}

	public Integer getMlcrmtMallaCurricular() {
		return mlcrmtMallaCurricular;
	}

	public void setMlcrmtMallaCurricular(Integer mlcrmtMallaCurricular) {
		this.mlcrmtMallaCurricular = mlcrmtMallaCurricular;
	}

	public Nivel getMlcrmtNivel() {
		return mlcrmtNivel;
	}

	public void setMlcrmtNivel(Nivel mlcrmtNivel) {
		this.mlcrmtNivel = mlcrmtNivel;
	}

	public Integer getMlcrmtUnidadFormacion() {
		return mlcrmtUnidadFormacion;
	}

	public void setMlcrmtUnidadFormacion(Integer mlcrmtUnidadFormacion) {
		this.mlcrmtUnidadFormacion = mlcrmtUnidadFormacion;
	}

	public Integer getMlcrmtMateria() {
		return mlcrmtMateria;
	}

	public void setMlcrmtMateria(Integer mlcrmtMateria) {
		this.mlcrmtMateria = mlcrmtMateria;
	}

	public Integer getMlcrmtEstado() {
		return mlcrmtEstado;
	}

	public void setMlcrmtEstado(Integer mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	public int getMtrId() {
		return mtrId;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
	}

	public Integer getMtrCampoFormacion() {
		return mtrCampoFormacion;
	}

	public void setMtrCampoFormacion(Integer mtrCampoFormacion) {
		this.mtrCampoFormacion = mtrCampoFormacion;
	}

	public Integer getMtrTipoMateria() {
		return mtrTipoMateria;
	}

	public void setMtrTipoMateria(Integer mtrTipoMateria) {
		this.mtrTipoMateria = mtrTipoMateria;
	}

	public Integer getMtrMateria() {
		return mtrMateria;
	}

	public void setMtrMateria(Integer mtrMateria) {
		this.mtrMateria = mtrMateria;
	}

	public Integer getMtrCarrera() {
		return mtrCarrera;
	}

	public void setMtrCarrera(Integer mtrCarrera) {
		this.mtrCarrera = mtrCarrera;
	}

	public Integer getMtrNucleoProblemicoCarrera() {
		return mtrNucleoProblemicoCarrera;
	}

	public void setMtrNucleoProblemicoCarrera(Integer mtrNucleoProblemicoCarrera) {
		this.mtrNucleoProblemicoCarrera = mtrNucleoProblemicoCarrera;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public String getMtrCodigoSniese() {
		return mtrCodigoSniese;
	}

	public void setMtrCodigoSniese(String mtrCodigoSniese) {
		this.mtrCodigoSniese = mtrCodigoSniese;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public Integer getMtrEstado() {
		return mtrEstado;
	}

	public void setMtrEstado(Integer mtrEstado) {
		this.mtrEstado = mtrEstado;
	}

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public Integer getMtrIntegradoraHoras() {
		return mtrIntegradoraHoras;
	}

	public void setMtrIntegradoraHoras(Integer mtrIntegradoraHoras) {
		this.mtrIntegradoraHoras = mtrIntegradoraHoras;
	}

	public Integer getMtrPreProfesionalHoras() {
		return mtrPreProfesionalHoras;
	}

	public void setMtrPreProfesionalHoras(Integer mtrPreProfesionalHoras) {
		this.mtrPreProfesionalHoras = mtrPreProfesionalHoras;
	}

	public Integer getMtrHorasCien() {
		return mtrHorasCien;
	}

	public void setMtrHorasCien(Integer mtrHorasCien) {
		this.mtrHorasCien = mtrHorasCien;
	}

	public Integer getMtrRelacionTrabajo() {
		return mtrRelacionTrabajo;
	}

	public void setMtrRelacionTrabajo(Integer mtrRelacionTrabajo) {
		this.mtrRelacionTrabajo = mtrRelacionTrabajo;
	}

	public Integer getMtrUnidadMedida() {
		return mtrUnidadMedida;
	}

	public void setMtrUnidadMedida(Integer mtrUnidadMedida) {
		this.mtrUnidadMedida = mtrUnidadMedida;
	}

	public BigDecimal getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(BigDecimal mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}

	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
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

	public String getPrsPrimerApellido() {
		return prsPrimerApellido;
	}

	public void setPrsPrimerApellido(String prsPrimerApellido) {
		this.prsPrimerApellido = prsPrimerApellido;
	}

	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}

	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}

	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinCncrId() {
		return fcinCncrId;
	}

	public void setFcinCncrId(Integer fcinCncrId) {
		this.fcinCncrId = fcinCncrId;
	}

	public Integer getFcinPracId() {
		return fcinPracId;
	}

	public void setFcinPracId(Integer fcinPracId) {
		this.fcinPracId = fcinPracId;
	}

	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	public Integer getCrrReajusteMatricula() {
		return crrReajusteMatricula;
	}

	public void setCrrReajusteMatricula(Integer crrReajusteMatricula) {
		this.crrReajusteMatricula = crrReajusteMatricula;
	}

	public Boolean getCrrAcceso() {
		return crrAcceso;
	}

	public void setCrrAcceso(Boolean crrAcceso) {
		this.crrAcceso = crrAcceso;
	}

	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	public Integer getCrrEstado() {
		return crrEstado;
	}

	public void setCrrEstado(Integer crrEstado) {
		this.crrEstado = crrEstado;
	}

	public Integer getCrrOrdinal() {
		return crrOrdinal;
	}

	public void setCrrOrdinal(Integer crrOrdinal) {
		this.crrOrdinal = crrOrdinal;
	}

	public Integer getCrrEvaluado() {
		return crrEvaluado;
	}

	public void setCrrEvaluado(Integer crrEvaluado) {
		this.crrEvaluado = crrEvaluado;
	}

	public Integer getCrrNoEvaluado() {
		return crrNoEvaluado;
	}

	public void setCrrNoEvaluado(Integer crrNoEvaluado) {
		this.crrNoEvaluado = crrNoEvaluado;
	}

	public String getPrsEmailInstitucional() {
		return prsEmailInstitucional;
	}

	public void setPrsEmailInstitucional(String prsEmailInstitucional) {
		this.prsEmailInstitucional = prsEmailInstitucional;
	}

	public List<CarreraDto> getCrrListCarreraDto() {
		return crrListCarreraDto;
	}

	public void setCrrListCarreraDto(List<CarreraDto> crrListCarreraDto) {
		this.crrListCarreraDto = crrListCarreraDto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + crrId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		CarreraDto other = (CarreraDto) obj;
		if (crrId != other.crrId)
			return false;
		return true;
	}

	public Integer getCrrTipoEvaluacion() {
		return crrTipoEvaluacion;
	}

	public void setCrrTipoEvaluacion(Integer crrTipoEvaluacion) {
		this.crrTipoEvaluacion = crrTipoEvaluacion;
	}

	public PersonaDto getCrrPersonaDto() {
		return crrPersonaDto;
	}

	public void setCrrPersonaDto(PersonaDto crrPersonaDto) {
		this.crrPersonaDto = crrPersonaDto;
	}

	public PeriodoAcademicoDto getCrrPeriodoAcademicoDto() {
		return crrPeriodoAcademicoDto;
	}

	public void setCrrPeriodoAcademicoDto(PeriodoAcademicoDto crrPeriodoAcademicoDto) {
		this.crrPeriodoAcademicoDto = crrPeriodoAcademicoDto;
	}

	
	
	public int getNocrId() {
		return nocrId;
	}

	public void setNocrId(int nocrId) {
		this.nocrId = nocrId;
	}

	public Integer getNocrEstado() {
		return nocrEstado;
	}

	public void setNocrEstado(Integer nocrEstado) {
		this.nocrEstado = nocrEstado;
	}

	public Float getNocrNota() {
		return nocrNota;
	}

	public void setNocrNota(Float nocrNota) {
		this.nocrNota = nocrNota;
	}

	public Integer getNocrPracId() {
		return nocrPracId;
	}

	public void setNocrPracId(Integer nocrPracId) {
		this.nocrPracId = nocrPracId;
	}
	
	

	public boolean isExisteNotaCorte() {
		return existeNotaCorte;
	}

	public void setExisteNotaCorte(boolean existeNotaCorte) {
		this.existeNotaCorte = existeNotaCorte;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrCodSniese : " + (crrCodSniese == null ? "NULL" : crrCodSniese));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrTipo : " + (crrTipo == null ? "NULL" : crrTipo));
		sb.append(tabulador + "crrDpnId : " + (crrDpnId == null ? "NULL" : crrDpnId));
		sb.append(tabulador + "crrEspeCodigo : " + (crrEspeCodigo == null ? "NULL" : crrEspeCodigo));
		sb.append(tabulador + "dpnUbcId : " + dpnUbcId);
		sb.append(tabulador + "dpnSubId : " + dpnSubId);
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));
		sb.append(tabulador + "dpnJerarquia : " + (dpnJerarquia == null ? "NULL" : dpnJerarquia));
		sb.append(tabulador + "dpnEstado : " + (dpnEstado == null ? "NULL" : dpnEstado));
		sb.append(tabulador + "dpnCampus : " + (dpnCampus == null ? "NULL" : dpnCampus));
		sb.append(tabulador + "crrIdentificador : " + (crrIdentificador == null ? "NULL" : crrIdentificador));

		sb.append(tabulador + "mlcrId: " + mlcrId);
		sb.append(tabulador + "mlcrCarrera: " + (mlcrCarrera == null ? "NULL" : mlcrCarrera));
		sb.append(tabulador + "mlcrTipoFormacionMalla: "
				+ (mlcrTipoFormacionMalla == null ? "NULL" : mlcrTipoFormacionMalla));
		sb.append(tabulador + "mlcrCodigo : " + (mlcrCodigo == null ? "NULL" : mlcrCodigo));
		sb.append(tabulador + "mlcrDescripcion : " + (mlcrDescripcion == null ? "NULL" : mlcrDescripcion));
		sb.append(tabulador + "mlcrEstado : " + (mlcrEstado == null ? "NULL" : mlcrEstado));
		sb.append(tabulador + "mlcrFechaInicio : " + (mlcrFechaInicio == null ? "NULL" : mlcrFechaInicio));
		sb.append(tabulador + "mlcrFechaFin : " + (mlcrFechaFin == null ? "NULL" : mlcrFechaFin));
		sb.append(tabulador + "mlcrTotalHoras : " + (mlcrTotalHoras == null ? "NULL" : mlcrTotalHoras));
		sb.append(tabulador + "mlcrTotalMaterias : " + (mlcrTotalMaterias == null ? "NULL" : mlcrTotalMaterias));
		sb.append(tabulador + "mlcrTipoOrgAprendizaje : "
				+ (mlcrTipoOrgAprendizaje == null ? "NULL" : mlcrTipoOrgAprendizaje));
		sb.append(tabulador + "mlcrVigente : " + (mlcrVigente == null ? "NULL" : mlcrVigente));
		sb.append(tabulador + "mlcrTipoAprobacion : " + (mlcrTipoAprobacion == null ? "NULL" : mlcrTipoAprobacion));

		sb.append(tabulador + "mlprId: " + mlprId);
		sb.append(tabulador + "mlprMallaCurricular : " + (mlprMallaCurricular == null ? "NULL" : mlprMallaCurricular));
		sb.append(tabulador + "mlprMallaCurricular : " + (mlprMallaCurricular == null ? "NULL" : mlprMallaCurricular));
		sb.append(tabulador + "mlprEstado : " + (mlprEstado == null ? "NULL" : mlprEstado));

		sb.append(tabulador + "pracId:" + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));
		sb.append(tabulador + "pracFechaIncio : " + (pracFechaIncio == null ? "NULL" : pracFechaIncio));
		sb.append(tabulador + "pracFechaFin : " + (pracFechaFin == null ? "NULL" : pracFechaFin));
		sb.append(tabulador + "pracTipo : " + (pracTipo == null ? "NULL" : pracTipo));

		sb.append(tabulador + "mlcrmtId: " + mlcrmtId);
		sb.append(tabulador + "mlcrmtNivelPreRequisito : "
				+ (mlcrmtNivelPreRequisito == null ? "NULL" : mlcrmtNivelPreRequisito));
		sb.append(tabulador + "mlcrmtMallaCurricular : "
				+ (mlcrmtMallaCurricular == null ? "NULL" : mlcrmtMallaCurricular));
		sb.append(tabulador + "mlcrmtNivel : " + (mlcrmtNivel == null ? "NULL" : mlcrmtNivel));
		sb.append(tabulador + "mlcrmtUnidadFormacion : "
				+ (mlcrmtUnidadFormacion == null ? "NULL" : mlcrmtUnidadFormacion));
		sb.append(tabulador + "mlcrmtMateria : " + (mlcrmtMateria == null ? "NULL" : mlcrmtMateria));
		sb.append(tabulador + "mlcrmtEstado :" + (mlcrmtEstado == null ? "NULL" : mlcrmtEstado));

		sb.append(tabulador + "mtrId: " + mtrId);
		sb.append(tabulador + "mtrCampoFormacion : " + (mtrCampoFormacion == null ? "NULL" : mtrCampoFormacion));
		sb.append(tabulador + "mtrTipoMateria : " + (mtrTipoMateria == null ? "NULL" : mtrTipoMateria));
		sb.append(tabulador + "mtrMateria : " + (mtrMateria == null ? "NULL" : mtrMateria));
		sb.append(tabulador + "mtrCarrera : " + (mtrCarrera == null ? "NULL" : mtrCarrera));
		sb.append(tabulador + "mtrNucleoProblemicoCarrera : "
				+ (mtrNucleoProblemicoCarrera == null ? "NULL" : mtrNucleoProblemicoCarrera));
		sb.append(tabulador + "mtrCodigo :" + (mtrCodigo == null ? "NULL" : mtrCodigo));

		sb.append(tabulador + "mtrCodigoSniese :" + (mtrCodigoSniese == null ? "NULL" : mtrCodigoSniese));
		sb.append(tabulador + "mtrDescripcion :" + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
		sb.append(tabulador + "mtrEstado :" + (mtrEstado == null ? "NULL" : mtrEstado));
		sb.append(tabulador + "mtrHoras :" + (mtrHoras == null ? "NULL" : mtrHoras));
		sb.append(tabulador + "mtrIntegradoraHoras :" + (mtrIntegradoraHoras == null ? "NULL" : mtrIntegradoraHoras));
		sb.append(tabulador + "mtrPreProfesionalHoras :"
				+ (mtrPreProfesionalHoras == null ? "NULL" : mtrPreProfesionalHoras));
		sb.append(tabulador + "mtrHorasCien :" + (mtrHorasCien == null ? "NULL" : mtrHorasCien));
		sb.append(tabulador + "mtrRelacionTrabajo :" + (mtrRelacionTrabajo == null ? "NULL" : mtrRelacionTrabajo));
		sb.append(tabulador + "mtrUnidadMedida :" + (mtrUnidadMedida == null ? "NULL" : mtrUnidadMedida));
		sb.append(tabulador + "mtrCreditos :" + (mtrCreditos == null ? "NULL" : mtrCreditos));

		sb.append(tabulador + "nocrId: " + nocrId);
		sb.append(tabulador + "nocrEstado : " + (nocrEstado == null ? "NULL" : nocrEstado));
		sb.append(tabulador + "nocrNota : " + (nocrNota == null ? "NULL" : nocrNota));
		sb.append(tabulador + "nocrPracId : " + (nocrPracId == null ? "NULL" : nocrPracId));
		sb.append(tabulador + "existeNotaCorte : " + existeNotaCorte);
		return sb.toString();
	}

}
