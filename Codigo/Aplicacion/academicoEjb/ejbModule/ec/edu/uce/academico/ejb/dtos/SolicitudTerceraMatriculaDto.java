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
   
 ARCHIVO:     SolicitudTerceraMatriculaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad SolicitudTerceraMatricula. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-02-2018 			Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Clase (DTO) SolicitudTerceraMatriculaDto. DTO encargado de manejar los datos de la entidad
 * SolicitudTerceraMatricula.
 * @author lmquishpei.
 * @version 1.0
 */
public class SolicitudTerceraMatriculaDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	//NUMERAL PARA REPORTES
	private int numeral;
	// Tabla Materia
	private int mtrId;
	private String mtrCodigo;
	private String mtrDescripcion;
	private int mtrHoras;
	// Tabla Carrera
	private int crrId;
	private String crrDescripcion;
	private String crrDetalle;
	private Integer crrDpnId;	
	// Malla curricular materia
	private int mlcrmtId;
	// Malla curricular paralelo
	private int mlcrprId;
	Boolean isSeleccionado;
	// numero de matricula
	private int numMatricula;
	// Tabla Periodo Academico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	// Tabla Persona
	private int prsId;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailInstitucional;
	// Tabla Ficha Estudiante
	private int fcesId;
	// Tabla Record Estudiante
	private int rcesId;
	private Integer rcesEstado;
	private Integer rcesIngresoNota;
	private Integer rcesEstadoTerceraMatri;
	//Tabla Causal
	private int cslId;
	private String cslDescripcion;
	private Integer cslEstado;
	private String cslCodigo;
	//Tabla Tipo Causal
	private int ticsId;
	private String ticsDescripcion;
	private Integer ticsEstado;
	//Tabla Solicitud Tercera Matricula
    private int sltrmtId;
	private Integer sltrmtEstado;
	private Integer sltrmtEstadoRegistro;
	private String sltrmtEstadoLabel;
	private Integer sltrmtTipo;
	private Timestamp sltrmtFechaSolicitud;
	private String sltrmtDocumentoSolicitud;
	private Timestamp sltrmtFechaVerificacion;
	private Timestamp sltrmtFechaRespSolicitud;
	//private Integer sltrmtEstadoApelacion;
	private Timestamp sltrmtFechaApelacion;
	//private String sltrmtDocumentoApelacion;
	private Timestamp sltrmtFechaResolucion;
	private String sltrmtDocumentoResolucion;
	private String sltrmtObservacion;
	private String sltrmtObservacionFinal;
	
	// Dependencia
	private int dpnId;
	private String dpnDescripcion;
	
	//Ficha Inscripcion
	private int fcinId;
	private Integer fcinTipo;
	private Integer fcinEstado;
	
	//Nivel
	private int nvlId;
	private String nvlDescripcion;

	//VARIABLES AUXILIARES
	
	private boolean visualizador;//para controlar disable
	private Integer respuestaSolicitud;
   
	public SolicitudTerceraMatriculaDto() {
		isSeleccionado = false;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	
	public int getMtrId() {
		return mtrId;
	}

	public String getSltrmtEstadoLabel() {
		return sltrmtEstadoLabel;
	}

	public void setSltrmtEstadoLabel(String sltrmtEstadoLabel) {
		this.sltrmtEstadoLabel = sltrmtEstadoLabel;
	}

	public Integer getSltrmtTipo() {
		return sltrmtTipo;
	}

	public void setSltrmtTipo(Integer sltrmtTipo) {
		this.sltrmtTipo = sltrmtTipo;
	}

	public int getNvlId() {
		return nvlId;
	}

	public int getNumeral() {
		return numeral;
	}

	public void setNumeral(int numeral) {
		this.numeral = numeral;
	}

	public void setNvlId(int nvlId) {
		this.nvlId = nvlId;
	}

	public String getNvlDescripcion() {
		return nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
	}
	
	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}
	
	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

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

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Integer getCrrDpnId() {
		return crrDpnId;
	}

	public void setCrrDpnId(Integer crrDpnId) {
		this.crrDpnId = crrDpnId;
	}

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Boolean getIsSeleccionado() {
		return isSeleccionado;
	}

	public void setIsSeleccionado(Boolean isSeleccionado) {
		this.isSeleccionado = isSeleccionado;
	}

	public int getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(int mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public int getNumMatricula() {
		return numMatricula;
	}

	public void setNumMatricula(int numMatricula) {
		this.numMatricula = numMatricula;
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

	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	public int getFcesId() {
		return fcesId;
	}

	public void setFcesId(int fcesId) {
		this.fcesId = fcesId;
	}

	public int getRcesId() {
		return rcesId;
	}

	public void setRcesId(int rcesId) {
		this.rcesId = rcesId;
	}

	public Integer getRcesEstado() {
		return rcesEstado;
	}

	public void setRcesEstado(Integer rcesEstado) {
		this.rcesEstado = rcesEstado;
	}

	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
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

	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}

	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}
	
	public Integer getRcesIngresoNota() {
		return rcesIngresoNota;
	}

	
	public void setRcesIngresoNota(Integer rcesIngresoNota) {
		this.rcesIngresoNota = rcesIngresoNota;
	}

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public int getCslId() {
		return cslId;
	}

	public void setCslId(int cslId) {
		this.cslId = cslId;
	}
	
	public String getCslDescripcion() {
		return cslDescripcion;
	}

	public void setCslDescripcion(String cslDescripcion) {
		this.cslDescripcion = cslDescripcion;
	}

	public Integer getCslEstado() {
		return cslEstado;
	}

	public void setCslEstado(Integer cslEstado) {
		this.cslEstado = cslEstado;
	}
	
	
	public String getCslCodigo() {
		return cslCodigo;
	}

	public void setCslCodigo(String cslCodigo) {
		this.cslCodigo = cslCodigo;
	}

	public int getTicsId() {
		return ticsId;
	}

	public void setTicsId(int ticsId) {
		this.ticsId = ticsId;
	}

	public String getTicsDescripcion() {
		return ticsDescripcion;
	}

	public void setTicsDescripcion(String ticsDescripcion) {
		this.ticsDescripcion = ticsDescripcion;
	}

	public Integer getTicsEstado() {
		return ticsEstado;
	}

	public void setTicsEstado(Integer ticsEstado) {
		this.ticsEstado = ticsEstado;
	}

	public int getSltrmtId() {
		return sltrmtId;
	}

	public void setSltrmtId(int sltrmtId) {
		this.sltrmtId = sltrmtId;
	}

	public Integer getSltrmtEstado() {
		return sltrmtEstado;
	}

	public void setSltrmtEstado(Integer sltrmtEstado) {
		this.sltrmtEstado = sltrmtEstado;
	}

	public Timestamp getSltrmtFechaSolicitud() {
		return sltrmtFechaSolicitud;
	}

	public void setSltrmtFechaSolicitud(Timestamp sltrmtFechaSolicitud) {
		this.sltrmtFechaSolicitud = sltrmtFechaSolicitud;
	}

	public String getSltrmtDocumentoSolicitud() {
		return sltrmtDocumentoSolicitud;
	}

	public void setSltrmtDocumentoSolicitud(String sltrmtDocumentoSolicitud) {
		this.sltrmtDocumentoSolicitud = sltrmtDocumentoSolicitud;
	}

	public Timestamp getSltrmtFechaResolucion() {
		return sltrmtFechaResolucion;
	}

	public void setSltrmtFechaResolucion(Timestamp sltrmtFechaResolucion) {
		this.sltrmtFechaResolucion = sltrmtFechaResolucion;
	}

	public String getSltrmtDocumentoResolucion() {
		return sltrmtDocumentoResolucion;
	}

	public void setSltrmtDocumentoResolucion(String sltrmtDocumentoResolucion) {
		this.sltrmtDocumentoResolucion = sltrmtDocumentoResolucion;
	}

	public String getSltrmtObservacion() {
		return sltrmtObservacion;
	}

	public void setSltrmtObservacion(String sltrmtObservacion) {
		this.sltrmtObservacion = sltrmtObservacion;
	}

	public Integer getRcesEstadoTerceraMatri() {
		return rcesEstadoTerceraMatri;
	}

	public void setRcesEstadoTerceraMatri(Integer rcesEstadoTerceraMatri) {
		this.rcesEstadoTerceraMatri = rcesEstadoTerceraMatri;
	}

	public Timestamp getSltrmtFechaVerificacion() {
		return sltrmtFechaVerificacion;
	}

	public void setSltrmtFechaVerificacion(Timestamp sltrmtFechaVerificacion) {
		this.sltrmtFechaVerificacion = sltrmtFechaVerificacion;
	}

	public Timestamp getSltrmtFechaRespSolicitud() {
		return sltrmtFechaRespSolicitud;
	}

	public void setSltrmtFechaRespSolicitud(Timestamp sltrmtFechaRespSolicitud) {
		this.sltrmtFechaRespSolicitud = sltrmtFechaRespSolicitud;
	}

	
	public Timestamp getSltrmtFechaApelacion() {
		return sltrmtFechaApelacion;
	}

	public void setSltrmtFechaApelacion(Timestamp sltrmtFechaApelacion) {
		this.sltrmtFechaApelacion = sltrmtFechaApelacion;
	}

	
	
	
	public boolean isVisualizador() {
		return visualizador;
	}

	public void setVisualizador(boolean visualizador) {
		this.visualizador = visualizador;
	}

	
	
	public Integer getRespuestaSolicitud() {
		return respuestaSolicitud;
	}

	public void setRespuestaSolicitud(Integer respuestaSolicitud) {
		this.respuestaSolicitud = respuestaSolicitud;
	}

	
	
	
	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinTipo() {
		return fcinTipo;
	}

	public void setFcinTipo(Integer fcinTipo) {
		this.fcinTipo = fcinTipo;
	}

	public Integer getFcinEstado() {
		return fcinEstado;
	}

	public void setFcinEstado(Integer fcinEstado) {
		this.fcinEstado = fcinEstado;
	}

	
	public int getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(int mtrHoras) {
		this.mtrHoras = mtrHoras;
	}
	
	

	public String getSltrmtObservacionFinal() {
		return sltrmtObservacionFinal;
	}

	public void setSltrmtObservacionFinal(String sltrmtObservacionFinal) {
		this.sltrmtObservacionFinal = sltrmtObservacionFinal;
	}
	

	public Integer getSltrmtEstadoRegistro() {
		return sltrmtEstadoRegistro;
	}

	public void setSltrmtEstadoRegistro(Integer sltrmtEstadoRegistro) {
		this.sltrmtEstadoRegistro = sltrmtEstadoRegistro;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("Materia");
		sb.append(tabulador + "mtrId : " + mtrId);
		sb.append(tabulador + "mtrCodigo : " + (mtrCodigo == null ? "NULL" : mtrCodigo));
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
	
		sb.append("Carrera");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrDpnId : " + (crrDpnId == null ? "NULL" : crrDpnId));

		sb.append("MallaCuricularMateria");
		sb.append(tabulador + "mlcrmtId : " + mlcrmtId);

		sb.append("MallaCuricularParalelo");
		sb.append(tabulador + "mlcrprId : " + mlcrprId);

		sb.append("Periodo Academico");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));

		sb.append("Persona");
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsIdentificacion: " + (prsIdentificacion == null ? "NULL" : prsIdentificacion));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido == null ? "NULL" : prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido == null ? "NULL" : prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres == null ? "NULL" : prsNombres));
		sb.append(
				tabulador + "prsMailInstitucional : " + (prsMailInstitucional == null ? "NULL" : prsMailInstitucional));

		sb.append("Ficha Estudiante");
		sb.append(tabulador + "fcesId : " + fcesId);

		sb.append("Ficha Matricula");
		sb.append("NumeroMatricula");
		sb.append(tabulador + "numMatricula : " + numMatricula);

		sb.append("Record Estudiante");
		sb.append(tabulador + "rcesId : " + rcesId);
		sb.append(tabulador + "rcesEstado : " + rcesEstado);
		sb.append(tabulador + "rcesIngresoNota : " + (rcesIngresoNota == null ? "NULL" : rcesIngresoNota));
		sb.append(tabulador + "rcesEstadoTerceraMatri : " + rcesEstadoTerceraMatri);
		
		sb.append("EstaSeleccionado");
		sb.append(tabulador + "isSeleccionado : " + isSeleccionado);
		
		sb.append("Dependencia");
		sb.append(tabulador + "dpnId : " + dpnId);
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));
		
		sb.append("Nivel");
		sb.append(tabulador + "nvlId : " + nvlId);
		sb.append(tabulador + "nvlDescripcion : " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));

		sb.append("Causal");
		sb.append(tabulador + "cslId : " + cslId);
		sb.append(tabulador + "cslDescripcion : " + (cslDescripcion == null ? "NULL" : cslDescripcion));
		sb.append(tabulador + "cslEstado : " + cslEstado);
		sb.append(tabulador + "cslCodigo : " + (cslCodigo == null ? "NULL" : cslCodigo));
		
		
		sb.append("Tipo Causal");
		sb.append(tabulador + "ticsId : " + ticsId);
		sb.append(tabulador + "ticsDescripcion : " + (ticsDescripcion == null ? "NULL" : ticsDescripcion));
		sb.append(tabulador + "ticsEstado : " + ticsEstado);
		
		sb.append(" Solicitud Tercera Matricula ");
		sb.append(tabulador + "sltrmtId : " + sltrmtId);
		sb.append(tabulador + "sltrmtEstado : " + (sltrmtEstado==null? "NULL":sltrmtEstado));
		sb.append(tabulador + "sltrmtTipo : " + (sltrmtTipo==null? "NULL":sltrmtTipo));
		sb.append(tabulador + "sltrmtFechaSolicitud : " + (sltrmtFechaSolicitud==null? "NULL":sltrmtFechaSolicitud));
		sb.append(tabulador + "sltrmtDocumentoSolicitud : " + (sltrmtDocumentoSolicitud==null? "NULL":sltrmtDocumentoSolicitud));
		sb.append(tabulador + "sltrmtFechaVerificacion : " + (sltrmtFechaVerificacion==null? "NULL":sltrmtFechaVerificacion));
		sb.append(tabulador + "sltrmtFechaRespSolicitud : " + (sltrmtFechaRespSolicitud==null? "NULL":sltrmtFechaRespSolicitud));
		sb.append(tabulador + "sltrmtFechaRespSolicitud : " + (sltrmtFechaRespSolicitud==null? "NULL":sltrmtFechaRespSolicitud));
		sb.append(tabulador + "sltrmtFechaApelacion : " + (sltrmtFechaApelacion==null? "NULL":sltrmtFechaApelacion));
		sb.append(tabulador + "sltrmtFechaResolucion : " + (sltrmtFechaResolucion==null? "NULL":sltrmtFechaResolucion));
		sb.append(tabulador + "sltrmtDocumentoResolucion : " + (sltrmtDocumentoResolucion==null? "NULL":sltrmtDocumentoResolucion));
		sb.append(tabulador + "sltrmtObservacion : " + (sltrmtObservacion==null? "NULL":sltrmtObservacion));
		sb.append(tabulador + "sltrmtObservacionFinal : " + (sltrmtObservacionFinal==null? "NULL":sltrmtObservacionFinal));
		sb.append(tabulador + "sltrmtEstadoRegistro : " + (sltrmtEstadoRegistro==null? "NULL":sltrmtEstadoRegistro));
	
		sb.append(tabulador + "visualizador : " + visualizador);
		sb.append(tabulador + "respuestaSolicitud : " + (respuestaSolicitud==null? "NULL":respuestaSolicitud));
		
		sb.append("Ficha Inscripcion");
		sb.append(tabulador + "fcinId : " + fcinId);
		sb.append(tabulador + "fcinTipo : " + (fcinTipo==null? "NULL":fcinTipo));
		sb.append(tabulador + "fcinEstado : " + (fcinEstado==null? "NULL":fcinEstado));
		
		return sb.toString();
		
		
		
	}

}
