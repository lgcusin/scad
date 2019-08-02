package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 02/03/2017 8:53:22 by Hibernate Tools 3.5.0.Final

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "DEPENDENCIA", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name="Dependencia.findAll", query="SELECT d FROM Dependencia d"),
				@NamedQuery(name = "Dependencia.findPorJerarquia", query = "SELECT d FROM Dependencia d WHERE d.dpnJerarquia = :jerarquia") })
public class Dependencia implements java.io.Serializable {
	private static final long serialVersionUID = -4703078536222894663L;
	
	private int dpnId;
	private Ubicacion dpnUbicacion;
	private Dependencia dpnDependencia;
	private String dpnDescripcion;
	private Integer dpnJerarquia;
	private Integer dpnEstado;
	private Integer dpnCampus;
	private String dpnUej;
	private String dpnCodSori;
	private List<Dependencia> dpnDependencias = new ArrayList<Dependencia>(0);
	private List<Carrera> dpnCarreras = new ArrayList<Carrera>(0);
	private List<Edificio> dpnEdificios = new ArrayList<Edificio>(0);

	public Dependencia() {
	}

	public Dependencia(int dpnId) {
		this.dpnId = dpnId;
	}

	public Dependencia(int dpnId, Ubicacion dpnUbicacion, Dependencia dpnDependencia, String dpnDescripcion,
			Integer dpnJerarquia, Integer dpnEstado, Integer dpnCampus, String dpnUej, String dpnCodSori,
			List<Dependencia> dpnDependencias, List<Carrera> dpnCarreras, List<Edificio> dpnEdificios) {
		this.dpnId = dpnId;
		this.dpnUbicacion = dpnUbicacion;
		this.dpnDependencia = dpnDependencia;
		this.dpnDescripcion = dpnDescripcion;
		this.dpnJerarquia = dpnJerarquia;
		this.dpnEstado = dpnEstado;
		this.dpnCampus = dpnCampus;
		this.dpnUej = dpnUej;
		this.dpnCodSori = dpnCodSori;
		this.dpnDependencias = dpnDependencias;
		this.dpnCarreras = dpnCarreras;
		this.dpnEdificios = dpnEdificios;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "DPN_ID", unique = true, nullable = false, precision = 2, scale = 0)
	public int getDpnId() {
		return this.dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	@ManyToOne
	@JoinColumn(name = "UBC_ID")
	public Ubicacion getDpnUbicacion() {
		return this.dpnUbicacion;
	}

	public void setDpnUbicacion(Ubicacion dpnUbicacion) {
		this.dpnUbicacion = dpnUbicacion;
	}

	@ManyToOne
	@JoinColumn(name = "DPN_SUB_ID")
	public Dependencia getDpnDependencia() {
		return this.dpnDependencia;
	}

	public void setDpnDependencia(Dependencia dpnDependencia) {
		this.dpnDependencia = dpnDependencia;
	}

	@Column(name = "DPN_DESCRIPCION", length = 512)
	@Length(max = 512)
	public String getDpnDescripcion() {
		return this.dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	@Column(name = "DPN_JERARQUIA", precision = 2, scale = 0)
	public Integer getDpnJerarquia() {
		return this.dpnJerarquia;
	}

	public void setDpnJerarquia(Integer dpnJerarquia) {
		this.dpnJerarquia = dpnJerarquia;
	}

	@Column(name = "DPN_ESTADO", precision = 2, scale = 0)
	public Integer getDpnEstado() {
		return this.dpnEstado;
	}

	public void setDpnEstado(Integer dpnEstado) {
		this.dpnEstado = dpnEstado;
	}

	@Column(name = "DPN_CAMPUS", precision = 2, scale = 0)
	public Integer getDpnCampus() {
		return this.dpnCampus;
	}

	public void setDpnCampus(Integer dpnCampus) {
		this.dpnCampus = dpnCampus;
	}

	@OneToMany(mappedBy = "dpnDependencia")
	public List<Dependencia> getDpnDependencias() {
		return this.dpnDependencias;
	}

	public void setDpnDependencias(List<Dependencia> dpnDependencias) {
		this.dpnDependencias = dpnDependencias;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "crrDependencia")
	public List<Carrera> getDpnCarreras() {
		return this.dpnCarreras;
	}

	public void setDpnCarreras(List<Carrera> dpnCarreras) {
		this.dpnCarreras = dpnCarreras;
	}

	@OneToMany(mappedBy = "edfDependencia")
	public List<Edificio> getDpnEdificios() {
		return this.dpnEdificios;
	}

	public void setDpnEdificios(List<Edificio> dpnEdificios) {
		this.dpnEdificios = dpnEdificios;
	}

	@Column(name = "DPN_UEJ")
	public String getDpnUej() {
		return dpnUej;
	}

	public void setDpnUej(String dpnUej) {
		this.dpnUej = dpnUej;
	}

	@Column(name = "DPN_COD_SORI")
	public String getDpnCodSori() {
		return dpnCodSori;
	}

	public void setDpnCodSori(String dpnCodSori) {
		this.dpnCodSori = dpnCodSori;
	}

}
