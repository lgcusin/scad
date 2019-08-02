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
   
 ARCHIVO:     HorarioAcademicoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la creación de un nuevo programa de posgrado.. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20-Jun-2018 		  Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Cronograma;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemico;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemicoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacionMalla;
import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;

/**
 * Clase (DTO) ProgramaPosgradoDto. DTO encargado de manejar los datos de la
 * creación de un nuevo programa de posgrado.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class ProgramaPosgradoDto implements Serializable {

	private static final long serialVersionUID = -5768676106415490425L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla PeriodoAcademico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;
	private Integer pracTipo;
	private String pracDetalleFechaCohorte;
	
	// Tabla NucleoProblemico
	private List<NucleoProblemico> listaNucleoProblemico;

	// Tabla NucleoProblemicoCarrera
	private List<NucleoProblemicoCarrera> listaNucleoProblemicoCarrera;
	
	// Tabla NucleoProblemicoCarrera
	private List<MateriaDto> listaMateriaDto;
	
	
	
	// Tabla Cronograma
	private int crnId;
	private PeriodoAcademico crnPeriodoAcademico;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;

	// Tabla Titulo
	private int ttlId;
	private String ttlDescripcion;
	private Integer ttlSexo;
	private Integer ttlEstado;
	private Integer ttlTipo;
	
	// Tabla ConfiguracionCarrera
	private ConfiguracionCarrera configuracionCarrera;
	private Integer cncrExiste;
	
	// Tabla CronogramaProcesoFlujo
	private int crprflId;
	private ProcesoFlujo crprProcesoFlujo;
	private Cronograma crprCronograma;
	
	
	// Tabla PlanificacionCronograma
	private Date plcrFechaInicioOrdinaria;
	private Date plcrFechaFinOrdinaria;
	private Date plcrFechaInicioExtra;
	private Date plcrFechaFinExtra;

	// Tabla Carrera
	private int crrId;
	private Dependencia crrDependencia;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrTipo;
	private Integer crrCupo;
	private BigDecimal crrArancel;
	private Integer crrEspeCodigo;
	private Integer crrProceso;
	private Integer crrNumMaxCreditos;
	
	//Dependencia
	private int dpnId;
	private Ubicacion dpnUbicacion;
	private Dependencia dpnDependencia;
	private String dpnDescripcion;
	private Integer dpnJerarquia;
	private Integer dpnEstado;
	private Integer dpnCampus;
	private String dpnUej;
	private String dpnCodSori;
	
	//MallaCurricular
	private Integer mlcrId;
	private Carrera mlcrCarrera;
	private TipoFormacionMalla mlcrTipoFormacionMalla;
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
	
	private Integer niveles;
	
	public ProgramaPosgradoDto() {

	}


	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
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




	public int getCrnId() {
		return crnId;
	}


	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}


	public PeriodoAcademico getCrnPeriodoAcademico() {
		return crnPeriodoAcademico;
	}


	public void setCrnPeriodoAcademico(PeriodoAcademico crnPeriodoAcademico) {
		this.crnPeriodoAcademico = crnPeriodoAcademico;
	}


	public Integer getCrnTipo() {
		return crnTipo;
	}


	public void setCrnTipo(Integer crnTipo) {
		this.crnTipo = crnTipo;
	}


	public String getCrnDescripcion() {
		return crnDescripcion;
	}


	public void setCrnDescripcion(String crnDescripcion) {
		this.crnDescripcion = crnDescripcion;
	}


	public Integer getCrnEstado() {
		return crnEstado;
	}


	public void setCrnEstado(Integer crnEstado) {
		this.crnEstado = crnEstado;
	}


	public int getTtlId() {
		return ttlId;
	}


	public void setTtlId(int ttlId) {
		this.ttlId = ttlId;
	}


	public String getTtlDescripcion() {
		return ttlDescripcion;
	}


	public void setTtlDescripcion(String ttlDescripcion) {
		this.ttlDescripcion = ttlDescripcion;
	}


	public Integer getTtlSexo() {
		return ttlSexo;
	}


	public void setTtlSexo(Integer ttlSexo) {
		this.ttlSexo = ttlSexo;
	}


	public Integer getTtlEstado() {
		return ttlEstado;
	}


	public void setTtlEstado(Integer ttlEstado) {
		this.ttlEstado = ttlEstado;
	}


	public Integer getTtlTipo() {
		return ttlTipo;
	}


	public void setTtlTipo(Integer ttlTipo) {
		this.ttlTipo = ttlTipo;
	}




	public int getCrprflId() {
		return crprflId;
	}


	public void setCrprflId(int crprflId) {
		this.crprflId = crprflId;
	}


	public ProcesoFlujo getCrprProcesoFlujo() {
		return crprProcesoFlujo;
	}


	public void setCrprProcesoFlujo(ProcesoFlujo crprProcesoFlujo) {
		this.crprProcesoFlujo = crprProcesoFlujo;
	}


	public Cronograma getCrprCronograma() {
		return crprCronograma;
	}


	public void setCrprCronograma(Cronograma crprCronograma) {
		this.crprCronograma = crprCronograma;
	}




	public int getCrrId() {
		return crrId;
	}


	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}


	public Dependencia getCrrDependencia() {
		return crrDependencia;
	}


	public void setCrrDependencia(Dependencia crrDependencia) {
		this.crrDependencia = crrDependencia;
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


	public Integer getCrrTipo() {
		return crrTipo;
	}


	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}


	public Integer getCrrCupo() {
		return crrCupo;
	}


	public void setCrrCupo(Integer crrCupo) {
		this.crrCupo = crrCupo;
	}


	public BigDecimal getCrrArancel() {
		return crrArancel;
	}


	public void setCrrArancel(BigDecimal crrArancel) {
		this.crrArancel = crrArancel;
	}


	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}


	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}


	public Integer getCrrProceso() {
		return crrProceso;
	}


	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}


	public Integer getCrrNumMaxCreditos() {
		return crrNumMaxCreditos;
	}


	public void setCrrNumMaxCreditos(Integer crrNumMaxCreditos) {
		this.crrNumMaxCreditos = crrNumMaxCreditos;
	}


	public int getDpnId() {
		return dpnId;
	}


	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}


	public Ubicacion getDpnUbicacion() {
		return dpnUbicacion;
	}


	public void setDpnUbicacion(Ubicacion dpnUbicacion) {
		this.dpnUbicacion = dpnUbicacion;
	}


	public Dependencia getDpnDependencia() {
		return dpnDependencia;
	}


	public void setDpnDependencia(Dependencia dpnDependencia) {
		this.dpnDependencia = dpnDependencia;
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


	public String getDpnUej() {
		return dpnUej;
	}


	public void setDpnUej(String dpnUej) {
		this.dpnUej = dpnUej;
	}


	public String getDpnCodSori() {
		return dpnCodSori;
	}


	public void setDpnCodSori(String dpnCodSori) {
		this.dpnCodSori = dpnCodSori;
	}


	public String getPracDetalleFechaCohorte() {
		return pracDetalleFechaCohorte;
	}


	public void setPracDetalleFechaCohorte(String pracDetalleFechaCohorte) {
		this.pracDetalleFechaCohorte = pracDetalleFechaCohorte;
	}


	public Integer getNiveles() {
		return niveles;
	}


	public void setNiveles(Integer niveles) {
		this.niveles = niveles;
	}


	public List<NucleoProblemico> getListaNucleoProblemico() {
		return listaNucleoProblemico;
	}


	public void setListaNucleoProblemico(List<NucleoProblemico> listaNucleoProblemico) {
		this.listaNucleoProblemico = listaNucleoProblemico;
	}


	public List<NucleoProblemicoCarrera> getListaNucleoProblemicoCarrera() {
		return listaNucleoProblemicoCarrera;
	}


	public void setListaNucleoProblemicoCarrera(List<NucleoProblemicoCarrera> listaNucleoProblemicoCarrera) {
		this.listaNucleoProblemicoCarrera = listaNucleoProblemicoCarrera;
	}


	public Integer getCncrExiste() {
		return cncrExiste;
	}


	public void setCncrExiste(Integer cncrExiste) {
		this.cncrExiste = cncrExiste;
	}


	public ConfiguracionCarrera getConfiguracionCarrera() {
		return configuracionCarrera;
	}


	public void setConfiguracionCarrera(ConfiguracionCarrera configuracionCarrera) {
		this.configuracionCarrera = configuracionCarrera;
	}


	public Date getPlcrFechaInicioOrdinaria() {
		return plcrFechaInicioOrdinaria;
	}


	public void setPlcrFechaInicioOrdinaria(Date plcrFechaInicioOrdinaria) {
		this.plcrFechaInicioOrdinaria = plcrFechaInicioOrdinaria;
	}


	public Date getPlcrFechaFinOrdinaria() {
		return plcrFechaFinOrdinaria;
	}


	public void setPlcrFechaFinOrdinaria(Date plcrFechaFinOrdinaria) {
		this.plcrFechaFinOrdinaria = plcrFechaFinOrdinaria;
	}


	public Date getPlcrFechaInicioExtra() {
		return plcrFechaInicioExtra;
	}


	public void setPlcrFechaInicioExtra(Date plcrFechaInicioExtra) {
		this.plcrFechaInicioExtra = plcrFechaInicioExtra;
	}


	public Date getPlcrFechaFinExtra() {
		return plcrFechaFinExtra;
	}


	public void setPlcrFechaFinExtra(Date plcrFechaFinExtra) {
		this.plcrFechaFinExtra = plcrFechaFinExtra;
	}


	public List<MateriaDto> getListaMateriaDto() {
		return listaMateriaDto;
	}


	public void setListaMateriaDto(List<MateriaDto> listaMateriaDto) {
		this.listaMateriaDto = listaMateriaDto;
	}


	public Carrera getMlcrCarrera() {
		return mlcrCarrera;
	}


	public void setMlcrCarrera(Carrera mlcrCarrera) {
		this.mlcrCarrera = mlcrCarrera;
	}


	public TipoFormacionMalla getMlcrTipoFormacionMalla() {
		return mlcrTipoFormacionMalla;
	}


	public void setMlcrTipoFormacionMalla(TipoFormacionMalla mlcrTipoFormacionMalla) {
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


	public Integer getMlcrId() {
		return mlcrId;
	}


	public void setMlcrId(Integer mlcrId) {
		this.mlcrId = mlcrId;
	}





	
	
	
	
	
}
