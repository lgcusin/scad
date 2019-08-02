package ec.edu.uce.academico.ejb.dtos;


import java.math.BigDecimal;

/**
 * Clase (Entity Bean) EstadisticasDocenteNotasDto. Dto que almacena los datos de estadísticas del docente con el paso de notas
 * @author dalbuja.
 * @version 1.0
 */
public class EstadisticasDocenteNotasDto implements java.io.Serializable {

	private static final long serialVersionUID = -6421158488030031838L;
	
	private Integer numEstudiantesMatriculados;
	private Integer numEstudiantesInscritos;
	private Integer numEstudianteConNota1;
	private Integer numEstudiantesSinNota1;
	private BigDecimal desviacionEstandar1;
	private BigDecimal media1;
	private BigDecimal sumatoriaNotas1;
	private Integer numEstudianteConNota2;
	private Integer numEstudiantesSinNota2;
	private BigDecimal desviacionEstandar2;
	private BigDecimal media2;
	private BigDecimal sumatoriaNotas2;
	private String prlCodigo;
	private String mtrDescripcion;
	private Integer mlcrprId;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String crrDescripcion;
	private String dpnDescripcion;
	private String pracDescripcion;
	private BigDecimal coeficiente1;
	private BigDecimal coeficiente2;
	
	
	public Integer getNumEstudiantesMatriculados() {
		return numEstudiantesMatriculados;
	}
	public void setNumEstudiantesMatriculados(Integer numEstudiantesMatriculados) {
		this.numEstudiantesMatriculados = numEstudiantesMatriculados;
	}
	public Integer getNumEstudiantesInscritos() {
		return numEstudiantesInscritos;
	}
	public void setNumEstudiantesInscritos(Integer numEstudiantesInscritos) {
		this.numEstudiantesInscritos = numEstudiantesInscritos;
	}
	public Integer getNumEstudianteConNota1() {
		return numEstudianteConNota1;
	}
	public void setNumEstudianteConNota1(Integer numEstudianteConNota1) {
		this.numEstudianteConNota1 = numEstudianteConNota1;
	}
	public Integer getNumEstudiantesSinNota1() {
		return numEstudiantesSinNota1;
	}
	public void setNumEstudiantesSinNota1(Integer numEstudiantesSinNota1) {
		this.numEstudiantesSinNota1 = numEstudiantesSinNota1;
	}
	public BigDecimal getDesviacionEstandar1() {
		return desviacionEstandar1;
	}
	public void setDesviacionEstandar1(BigDecimal desviacionEstandar1) {
		this.desviacionEstandar1 = desviacionEstandar1;
	}
	public BigDecimal getMedia1() {
		return media1;
	}
	public void setMedia1(BigDecimal media1) {
		this.media1 = media1;
	}
	public BigDecimal getSumatoriaNotas1() {
		return sumatoriaNotas1;
	}
	public void setSumatoriaNotas1(BigDecimal sumatoriaNotas1) {
		this.sumatoriaNotas1 = sumatoriaNotas1;
	}
	public Integer getNumEstudianteConNota2() {
		return numEstudianteConNota2;
	}
	public void setNumEstudianteConNota2(Integer numEstudianteConNota2) {
		this.numEstudianteConNota2 = numEstudianteConNota2;
	}
	public Integer getNumEstudiantesSinNota2() {
		return numEstudiantesSinNota2;
	}
	public void setNumEstudiantesSinNota2(Integer numEstudiantesSinNota2) {
		this.numEstudiantesSinNota2 = numEstudiantesSinNota2;
	}
	public BigDecimal getDesviacionEstandar2() {
		return desviacionEstandar2;
	}
	public void setDesviacionEstandar2(BigDecimal desviacionEstandar2) {
		this.desviacionEstandar2 = desviacionEstandar2;
	}
	public BigDecimal getMedia2() {
		return media2;
	}
	public void setMedia2(BigDecimal media2) {
		this.media2 = media2;
	}
	public BigDecimal getSumatoriaNotas2() {
		return sumatoriaNotas2;
	}
	public void setSumatoriaNotas2(BigDecimal sumatoriaNotas2) {
		this.sumatoriaNotas2 = sumatoriaNotas2;
	}
	public Integer getMlcrprId() {
		return mlcrprId;
	}
	public void setMlcrprId(Integer mlcrprId) {
		this.mlcrprId = mlcrprId;
	}
	public String getPrlCodigo() {
		return prlCodigo;
	}
	public void setPrlCodigo(String prlCodigo) {
		this.prlCodigo = prlCodigo;
	}
	public String getMtrDescripcion() {
		return mtrDescripcion;
	}
	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
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
	public String getPrsNombres() {
		return prsNombres;
	}
	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}
	public String getCrrDescripcion() {
		return crrDescripcion;
	}
	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}
	public String getDpnDescripcion() {
		return dpnDescripcion;
	}
	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}
	public String getPracDescripcion() {
		return pracDescripcion;
	}
	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}
	public BigDecimal getCoeficiente1() {
		return coeficiente1;
	}
	public void setCoeficiente1(BigDecimal coeficiente1) {
		this.coeficiente1 = coeficiente1;
	}
	public BigDecimal getCoeficiente2() {
		return coeficiente2;
	}
	public void setCoeficiente2(BigDecimal coeficiente2) {
		this.coeficiente2 = coeficiente2;
	}
	
	
	
	
	
}
