package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvHorarioLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Actividad;
import ec.edu.uce.biometrico.jpa.Bibliografia;
import ec.edu.uce.biometrico.jpa.Carrera;
import ec.edu.uce.biometrico.jpa.ContenidoCurricular;
import ec.edu.uce.biometrico.jpa.Dependencia;
import ec.edu.uce.biometrico.jpa.Herramienta;
import ec.edu.uce.biometrico.jpa.MallaCurricularMateria;
import ec.edu.uce.biometrico.jpa.Materia;
import ec.edu.uce.biometrico.jpa.Metodologia;
import ec.edu.uce.biometrico.jpa.Nivel;
import ec.edu.uce.biometrico.jpa.PeriodoAcademico;
import ec.edu.uce.biometrico.jpa.RecursoDidactico;
import ec.edu.uce.biometrico.jpa.Syllabo;
import ec.edu.uce.biometrico.jpa.UnidadCurricular;

@ManagedBean(name = "syllabus")
@SessionScoped
public class Syllabus implements Serializable {

	private static final long serialVersionUID = 3853955356123100328L;
	@EJB
	private SrvSeguimientoLocal srvSgm;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	@EJB
	private SrvHorarioLocal srvHor;
	private Login beanLogin;

	private Dependencia facultad;
	public Materia selectMtr;
	public MallaCurricularMateria mllCrrMateria;
	public Syllabo syl;
	public UnidadCurricular uc;
	public Carrera selectCrr;
	public Integer crrId;
	public List<Carrera> lstC;
	public List<Materia> lstM;
	public List<UnidadCurricular> lstUC;
	public List<ContenidoCurricular> lstCnt;
	public List<Actividad> lstAct;
	public List<Herramienta> lstHrr;
	public boolean dataSyllabo;
	public boolean modiSyllabo;
	public boolean crearSyllabo;

	public String tipAsignatura;
	public String prdAcademico;

	// public List<UnidadCurricular> lstUnidadCurriculars;
	// public List<ContenidoCurricular> lstContenidos;
	// public List<Actividad> lstActividads;
	// public List<Herramienta> lstherramientas;
	// public List<Metodologia> lstMetodologias;
	// public List<Bibliografia> lstBibliografias;
	// private List<RecursoDidactico> lstRecursosDidacticos;
	private PeriodoAcademico selectPrac;
	private Integer pracId;
	private boolean viewSyl;
	private List<PeriodoAcademico> lstP;
	private List<Syllabo> lstSyllabos;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		iniciar();
		if (beanLogin.isDocente()) {
			// lstC = srvSgm.listarAllCrrByFcdc(
			// beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaDocentes().get(0).getFcdcId());
			// lstM = srvSgm.listarAllMatByFcdc(
			// beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaDocentes().get(0).getFcdcId());
			lstP = srvSgm.listarPeriodos(0, 0);
			facultad = beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia();
			viewSyl = true;
		} else if (beanLogin.isAdminFacultad()) {
			facultad = beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia();
			lstP = srvEmp.listarPeriodos();
			viewSyl = false;
		}

	}

	public void iniciar() {
		pracId = null;
		crrId = null;
		selectPrac = new PeriodoAcademico();
		selectCrr = new Carrera();
		selectMtr = new Materia();
		selectPrac.setPracId(GeneralesConstantes.APP_ID_BASE);
		selectCrr.setCrrId(GeneralesConstantes.APP_ID_BASE);
		lstP = new ArrayList<>();
		lstC = new ArrayList<>();
		lstM = new ArrayList<>();
		lstSyllabos = new ArrayList<>();
		viewSyl = false;
	}

	public void listarCarreras(ValueChangeEvent event) {
		pracId = (Integer) event.getNewValue();
		selectCrr.setCrrId(GeneralesConstantes.APP_ID_BASE);
		lstM = null;
		if (event.getNewValue() != null && pracId != GeneralesConstantes.APP_ID_BASE) {
			if (viewSyl) {
				lstC = srvEmp.listarCarreras(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId());
				// srvSgm.listarAllCrrByFcdc(usuarioSy.getUsrPersona().getPrsFichaDocentes().get(0).getFcdcId());
			} else {
				lstC = srvEmp.listarCarrerasxFacultadPeriodo(facultad.getDpnId(), pracId);
			}
			if (lstC.isEmpty()) {
				FacesUtil.mensajeError("No existen carreras para el periodo.");
			} else {
				for (PeriodoAcademico prac : lstP) {
					if (prac.getPracId() == pracId) {
						selectPrac = prac;
					}
				}
			}
		}

	}

	public void carreraID(ValueChangeEvent event) {
		crrId = (Integer) event.getNewValue();
		lstM = null;
	}

	public void buscar() {
		if (pracId != null && pracId != GeneralesConstantes.APP_ID_BASE) {
			if (crrId != GeneralesConstantes.APP_ID_BASE) {
				lstM = new ArrayList<>();
				if (viewSyl) {
					List<Nivel> lstS = srvHor
							.listarSemestrexCarrera(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), crrId);
					for (Nivel nvl : lstS) {
						lstM.addAll(srvHor.listarMatBySem(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(),
								nvl.getNvlId(), crrId));
					}
				} else {
					lstM = srvSgm.listarMatByCrr(crrId);
				}
			} else {
				FacesUtil.mensajeError("Seleccione una carrera");
			}
		} else {
			FacesUtil.mensajeError("Seleccione un periodo.");
		}
	}

	public String limpiar() {
		pracId = null;
		crrId = null;
		selectPrac = new PeriodoAcademico();
		selectCrr = new Carrera();
		selectMtr = new Materia();
		selectPrac.setPracId(GeneralesConstantes.APP_ID_BASE);
		selectCrr.setCrrId(GeneralesConstantes.APP_ID_BASE);
		lstM = new ArrayList<>();
		return null;
	}

	public String verDetaSyllabus() {
		if (selectMtr != null) {
			mllCrrMateria = srvSgm.getMallaCurricularMateria(selectMtr.getMtrId());
			selectCrr = mllCrrMateria.getMlcrmtMateria().getMtrCarrera();
			syl = srvSgm.obtenerSyllabusxMateria(mllCrrMateria.getMlcrmtId());
			if (syl != null) {
				lstUC = srvSgm.listarUnidadCurricular(selectMtr.getMtrId());
				if (!lstUC.isEmpty()) {
					for (UnidadCurricular unidad : lstUC) {
						lstCnt = srvSgm.buscarContenidosCurriculares(unidad.getUncrId());
						for (ContenidoCurricular contenidoCurricular : lstCnt) {
							lstAct = srvSgm.listarActividades(contenidoCurricular.getCncrId());
							lstHrr = srvSgm.listarHerramientas(contenidoCurricular.getCncrId());
							contenidoCurricular.setActividads(lstAct);
							contenidoCurricular.setHerramientas(lstHrr);
						}
						unidad.setContenidos(lstCnt);
						unidad.setMetodologias(srvSgm.listarMetodologias(unidad.getUncrId()));
						unidad.setBibliografias(srvSgm.listarBibliografias(unidad.getUncrId()));
						unidad.setRecursosDidacticoses(srvSgm.listarRecursosDidacticos(unidad.getUncrId()));
					}
				}
				syl.setSylUnidadCurriculars(lstUC);
				dataSyllabo = true;
				return "detalleSyllabo";
			} else {
				// lstUC = new ArrayList<>();
				lstSyllabos = srvSgm.buscarAllSyllaboxPeriodo(mllCrrMateria);
				dataSyllabo = false;
				crearSyllabo = true;
				FacesUtil.mensajeError(
						"Agregue un syllabo o seleccione uno registrado, la materia no tiene uno disponible");
				return "crearSyllabo";
			}
		} else {
			FacesUtil.mensajeError("Seleccione una materia");
			return null;
		}
	}

	public String modificar() {
		modiSyllabo = true;
		dataSyllabo = false;
		crearSyllabo = false;
		return "editarSyllabo";
	}

	public String seleccionar() {
		lstUC = srvSgm.listarUnidadCurricular(syl.getSylMallaCurricularMateria().getMlcrmtMateria().getMtrId());
		if (!lstUC.isEmpty()) {
			for (UnidadCurricular unidad : lstUC) {
				lstCnt = srvSgm.buscarContenidosCurriculares(unidad.getUncrId());
				for (ContenidoCurricular contenidoCurricular : lstCnt) {
					lstAct = srvSgm.listarActividades(contenidoCurricular.getCncrId());
					lstHrr = srvSgm.listarHerramientas(contenidoCurricular.getCncrId());
					for (Actividad act : lstAct) {
						act.setActId(GeneralesConstantes.APP_ID_BASE);
					}
					contenidoCurricular.setActividads(lstAct);
					for (Herramienta hrr : lstHrr) {
						hrr.setHrrId(GeneralesConstantes.APP_ID_BASE);
					}
					contenidoCurricular.setHerramientas(lstHrr);
					contenidoCurricular.setCncrId(GeneralesConstantes.APP_ID_BASE);
				}
				unidad.setContenidos(lstCnt);
				List<Metodologia> lstMtd = srvSgm.listarMetodologias(unidad.getUncrId());
				for (Metodologia mtd : lstMtd) {
					mtd.setMtdId(GeneralesConstantes.APP_ID_BASE);
				}
				unidad.setMetodologias(lstMtd);
				List<RecursoDidactico> lstRcdd = srvSgm.listarRecursosDidacticos(unidad.getUncrId());
				for (RecursoDidactico rcdd : lstRcdd) {
					rcdd.setRcddId(GeneralesConstantes.APP_ID_BASE);
				}
				unidad.setRecursosDidacticoses(lstRcdd);
				List<Bibliografia> lstBbl = srvSgm.listarBibliografias(unidad.getUncrId());
				for (Bibliografia bbl : lstBbl) {
					bbl.setBblId(GeneralesConstantes.APP_ID_BASE);
				}
				unidad.setBibliografias(lstBbl);

				unidad.setUncrId(GeneralesConstantes.APP_ID_BASE);
			}
		}
		syl.setSylUnidadCurriculars(lstUC);
		modiSyllabo = true;
		dataSyllabo = false;
		crearSyllabo = false;
		return "editarSyllabo";
	}

	public String crearSyllabus() {
		modiSyllabo = true;
		crearSyllabo = false;
		syl = new Syllabo(GeneralesConstantes.APP_ID_BASE, mllCrrMateria, "", 0, 0, "", "", "", "", lstUC);
		return "editarSyllabo";
	}

	public String guardarSyllabus() {
		mllCrrMateria = srvSgm.getMallaCurricularMateria(selectMtr.getMtrId());
		if (!lstUC.isEmpty()) {
			syl.setSylId(mllCrrMateria.getMlcrmtId());
			syl.setSylMallaCurricularMateria(mllCrrMateria);
			srvSgm.guardarActualizarSyllabus(syl);
			// for (UnidadCurricular unidad : syl.getSylUnidadCurriculars()) {
			// uc = srvSgm.guardarActualizarUnidad(unidad);
			// if (uc != null) {
			// for (ContenidoCurricular contenidoCurricular :
			// unidad.getContenidos()) {
			// contenidoCurricular.setUnidadCurricular(uc);
			// ContenidoCurricular cc =
			// srvSgm.guardarActualizarContenido(contenidoCurricular);
			// if (cc != null) {
			// for (Actividad actividad : contenidoCurricular.getActividads()) {
			// actividad.setActContenidoCurricular(cc);
			// srvSgm.guardarActualizarActividad(actividad);
			// }
			// for (Herramienta herramienta :
			// contenidoCurricular.getHerramientas()) {
			// herramienta.setHrrContenidoCurricular(cc);
			// srvSgm.guardarActualizarHerramienta(herramienta);
			// }
			// }
			//
			// }
			// for (Metodologia metodologia : unidad.getMetodologias()) {
			// metodologia.setMtdUnidadCurricular(uc);
			// srvSgm.guardarActualizarMetodologia(metodologia);
			// }
			// for (Bibliografia bibliografia : unidad.getBibliografias()) {
			// bibliografia.setBblUnidadCurricular(uc);
			// srvSgm.guardarActualizarBibliografia(bibliografia);
			// }
			// for (RecursoDidactico recurso : unidad.getRecursosDidacticoses())
			// {
			// recurso.setRcddUnidadCurricular(uc);
			// srvSgm.guardarActualizarRecursoDidactico(recurso);
			// }
			// } else {
			// FacesUtil.mensajeError("Error al guardarla unidad, intentelo
			// nuevamente");
			// return null;
			// }
			// }
		} else {
			FacesUtil.mensajeError("No existe unidad curricular para guardar");
			return null;
		}
		modiSyllabo = false;
		dataSyllabo = true;
		syl = null;
		lstUC = null;
		return "syllabus";
	}

	public String regresar() {
		dataSyllabo = false;
		modiSyllabo = false;
		crearSyllabo = false;
		syl = null;
		lstUC = null;
		return "syllabus";
	}

	public String regresarPrincipal() {
		selectPrac = null;
		selectCrr = null;
		lstP = null;
		lstC = null;
		lstM = null;
		return "principal";
	}

	public String agregarUnCr() {
		// lstUnidadCurriculars = syl.getSylUnidadCurriculars();
		// lstContenidos = new ArrayList<>();
		// lstMetodologias = new ArrayList<>();
		// lstRecursosDidacticos = new ArrayList<>();
		// lstBibliografias = new ArrayList<>();
		uc = new UnidadCurricular(GeneralesConstantes.APP_ID_BASE);
		uc.setUncrHorasTeoricas(0);
		uc.setUncrHorasPresenciales(0);
		uc.setUncrHorasPracticas(0);
		uc.setUncrHorasVirtual(0);
		uc.setSyllabo(syl);
		// if (!lstUC.isEmpty()) {
		//
		// } else {
		// lstUnidadCurriculars.add(uc);
		// syl.setSylUnidadCurriculars(lstUnidadCurriculars);
		// }
		return "unidadCurricular";
	}

	public void onAddNewCnt() {
		ContenidoCurricular contenidoCurricular = new ContenidoCurricular(GeneralesConstantes.APP_ID_BASE);
		// lstActividads = new ArrayList<>();
		// lstherramientas = new ArrayList<>();
		contenidoCurricular.setUnidadCurricular(uc);
		// contenidoCurricular.setActividads(lstActividads);
		// contenidoCurricular.setHerramientas(lstherramientas);
		// lstContenidos.add(contenidoCurricular);
		// uc.setContenidos(lstContenidos);
		uc.getContenidos().add(contenidoCurricular);
	}

	public void onAddNewAct(ContenidoCurricular cnt) {
		Actividad actividad = new Actividad(GeneralesConstantes.APP_ID_BASE);
		actividad.setActContenidoCurricular(cnt);
		// lstActividads = cnt.getActividads();
		cnt.getActividads().add(actividad);
	}

	public void onAddNewHrr(ContenidoCurricular cnt) {
		Herramienta herramienta = new Herramienta(GeneralesConstantes.APP_ID_BASE);
		herramienta.setHrrContenidoCurricular(cnt);
		// lstherramientas = cnt.getHerramientas();
		cnt.getHerramientas().add(herramienta);
	}

	public void onAddNewMtd() {
		Metodologia mtd = new Metodologia(GeneralesConstantes.APP_ID_BASE);
		mtd.setMtdUnidadCurricular(uc);
		// lstMetodologias = uc.getMetodologias();
		uc.getMetodologias().add(mtd);
	}

	public void onAddNewBbl() {
		Bibliografia bbl = new Bibliografia(GeneralesConstantes.APP_ID_BASE);
		bbl.setBblUnidadCurricular(uc);
		// lstBibliografias = uc.getBibliografias();
		uc.getBibliografias().add(bbl);
	}

	public void onAddNewRcd() {
		RecursoDidactico rcd = new RecursoDidactico(GeneralesConstantes.APP_ID_BASE);
		rcd.setRcddUnidadCurricular(uc);
		// lstRecursosDidacticos = uc.getRecursosDidacticoses();
		uc.getRecursosDidacticoses().add(rcd);
	}

	public void eliminarContenido(ContenidoCurricular cnt) {
		uc.getContenidos().remove(cnt);
		// lstContenidos = uc.getContenidos();
	}

	public void eliminarActividad(ContenidoCurricular cnt, Actividad act) {
		cnt.getActividads().remove(act);
	}

	public void eliminarHerramienta(ContenidoCurricular cnt, Herramienta hrr) {
		cnt.getHerramientas().remove(hrr);
	}

	public void eliminarMetodologia(Metodologia mtd) {
		uc.getMetodologias().remove(mtd);
		// lstMetodologias = uc.getMetodologias();
	}

	public void eliminarBibliografia(Bibliografia bbl) {
		uc.getBibliografias().remove(bbl);
		// lstBibliografias = uc.getBibliografias();
	}

	public void eliminarRecurso(RecursoDidactico rcd) {
		uc.getRecursosDidacticoses().remove(rcd);
		// lstRecursosDidacticos = uc.getRecursosDidacticoses();
	}

	public String AddNewUnidadCurricular() {
		if (uc.getContenidos().isEmpty()) {
			FacesUtil.mensajeError("Debe contener uno o mas contenidos");
			return null;
		} else {
			for (ContenidoCurricular cnt : uc.getContenidos()) {
				if (cnt.getCncrDescripcion() == null || cnt.getCncrDescripcion().trim().isEmpty()) {
					FacesUtil.mensajeError("Los contenidos no deben ser vacios");
					return null;
				} else {
					if (cnt.getActividads().isEmpty()) {
						FacesUtil.mensajeError("Debe contener uno o mas actividades");
						return null;
					} else {
						for (Actividad act : cnt.getActividads()) {
							if (act.getActDescripcion() == null || act.getActDescripcion().trim().isEmpty()) {
								FacesUtil.mensajeError("Los contenidos de actividades no deben ser vacios");
								return null;
							}
						}
					}
					if (cnt.getHerramientas().isEmpty()) {
						FacesUtil.mensajeError("Debe contener uno o mas mecanismos");
						return null;
					} else {
						for (Herramienta hrr : cnt.getHerramientas()) {
							if (hrr.getHrrNombre() == null || hrr.getHrrNombre().trim().isEmpty()) {
								FacesUtil.mensajeError("Los contenidos  de mecanismos no deben ser vacios");
								return null;
							}
						}
					}
				}
			}
		}
		if (uc.getMetodologias().isEmpty()) {
			FacesUtil.mensajeError("Debe contener uno o mas metodologias");
			return null;
		} else {
			for (Metodologia mtd : uc.getMetodologias()) {
				if (mtd.getMtdDescripcion() == null || mtd.getMtdDescripcion().trim().isEmpty()) {
					FacesUtil.mensajeError("Los contenidos  de la metodologia no deben ser vacios");
					return null;
				}
			}
		}
		if (uc.getRecursosDidacticoses().isEmpty()) {
			FacesUtil.mensajeError("Debe contener uno o mas recursos");
			return null;
		} else {
			for (RecursoDidactico rcdd : uc.getRecursosDidacticoses()) {
				if (rcdd.getRcddDescripcion() == null || rcdd.getRcddDescripcion().trim().isEmpty()) {
					FacesUtil.mensajeError("Los contenidos  del recurso no deben ser vacios");
					return null;
				}
			}
		}
		if (uc.getBibliografias().isEmpty()) {
			FacesUtil.mensajeError("Debe contener uno o mas bibliografias");
			return null;
		} else {
			for (Bibliografia bbl : uc.getBibliografias()) {
				if (bbl.getBblObraFisica() == null || bbl.getBblObraFisica().trim().isEmpty()) {
					FacesUtil.mensajeError("El nombre de obra bibliografica no deben ser vacios");
					return null;
				}
			}
		}
		// Suma las horas totales de la Unidad
		uc.setUncrTotalHoras(uc.getUncrHorasPracticas() + uc.getUncrHorasTeoricas());

		if (uc.getUncrDescripcion() == null || uc.getUncrDescripcion().trim().isEmpty()) {
			int posicion = lstUC.size() + 1;
			uc.setUncrDescripcion("UNIDAD CURRICULAR No. " + posicion);
			lstUC.add(uc);
			// } else if (uc.getUncrDescripcion().trim().isEmpty()) {
		} else {
			lstUC.set(lstUC.indexOf(uc), uc);
		}
		syl.setSylUnidadCurriculars(lstUC);
		syl.setSylHorasClase(syl.getSylHorasClase() + uc.getUncrHorasPracticas() + uc.getUncrHorasTeoricas());
		syl.setSylHorasTutorias(syl.getSylHorasTutorias() + uc.getUncrHorasPresenciales() + uc.getUncrHorasVirtual());
		uc = null;
		return "editarSyllabo";
	}

	public String cancelUnidadCurricular() {
		syl.setSylHorasClase(syl.getSylHorasClase() + uc.getUncrHorasPracticas() + uc.getUncrHorasTeoricas());
		syl.setSylHorasTutorias(syl.getSylHorasTutorias() + uc.getUncrHorasPresenciales() + uc.getUncrHorasVirtual());
		uc = null;
		return "editarSyllabo";
	}

	public String modificarUnCr() {
		if (uc == null) {
			FacesUtil.mensajeError("Unidad curricular no selecionada");
			return null;
		} else {
			syl.setSylHorasClase(syl.getSylHorasClase() - (uc.getUncrHorasPracticas() + uc.getUncrHorasTeoricas()));
			syl.setSylHorasTutorias(
					syl.getSylHorasTutorias() - (uc.getUncrHorasPresenciales() + uc.getUncrHorasVirtual()));
			return "unidadCurricular";
		}

	}

	public void eliminarUnCr() {
		if (uc == null) {
			FacesUtil.mensajeError("Unidad curricular no selecionada");
		} else {
			try {
				int posicion = syl.getSylUnidadCurriculars().indexOf(uc);
				int pos = posicion;
				for (UnidadCurricular un : syl.getSylUnidadCurriculars()) {
					if (syl.getSylUnidadCurriculars().indexOf(un) > posicion) {
						pos = pos + 1;
						un.setUncrDescripcion("UNIDAD CURRICULAR No. " + pos);
					}
				}
				if (uc.getUncrId() == GeneralesConstantes.APP_ID_BASE) {
					syl.getSylUnidadCurriculars().remove(uc);
				} else {
					syl.getSylUnidadCurriculars().remove(uc);
					srvSgm.eliminarUnidad(uc);
				}
				lstUC.remove(uc);
				uc = null;
				FacesUtil.mensajeInfo("Unidad eliminada");
			} catch (Exception e) {
				FacesUtil.mensajeError("Ocurrio un problema al eliminar el registro: " + e.getMessage());
			}
		}
	}

	// Setters and getters

	public Dependencia getFacultad() {
		return facultad;
	}

	public PeriodoAcademico getSelectPrac() {
		return selectPrac;
	}

	public void setSelectPrac(PeriodoAcademico selectPrac) {
		this.selectPrac = selectPrac;
	}

	public Integer getPracId() {
		return pracId;
	}

	public void setPracId(Integer pracId) {
		this.pracId = pracId;
	}

	public List<PeriodoAcademico> getLstP() {
		return lstP;
	}

	public void setLstP(List<PeriodoAcademico> lstP) {
		this.lstP = lstP;
	}

	public boolean isViewSyl() {
		return viewSyl;
	}

	public void setViewSyl(boolean viewSyl) {
		this.viewSyl = viewSyl;
	}

	public void setFacultad(Dependencia facultad) {
		this.facultad = facultad;
	}

	public List<Materia> getLstM() {
		return lstM;
	}

	public Materia getSelectMtr() {
		return selectMtr;
	}

	public MallaCurricularMateria getMllCrrMateria() {
		return mllCrrMateria;
	}

	public void setMllCrrMateria(MallaCurricularMateria mllCrrMateria) {
		this.mllCrrMateria = mllCrrMateria;
	}

	public void setSelectMtr(Materia selectMtr) {
		this.selectMtr = selectMtr;
	}

	public void setLstM(List<Materia> lstM) {
		this.lstM = lstM;
	}

	public List<Carrera> getLstC() {
		return lstC;
	}

	public void setLstC(List<Carrera> lstC) {
		this.lstC = lstC;
	}

	public Carrera getSelectCrr() {
		return selectCrr;
	}

	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	// Getters Syllabus

	public boolean isModiSyllabo() {
		return modiSyllabo;
	}

	public void setModiSyllabo(boolean modiSyllabo) {
		this.modiSyllabo = modiSyllabo;
	}

	public boolean isCrearSyllabo() {
		return crearSyllabo;
	}

	public void setCrearSyllabo(boolean crearSyllabo) {
		this.crearSyllabo = crearSyllabo;
	}

	public Syllabo getSyl() {
		return syl;
	}

	public void setSyl(Syllabo syl) {
		this.syl = syl;
	}

	public List<UnidadCurricular> getLstUC() {
		return lstUC;
	}

	public void setLstUC(List<UnidadCurricular> lstUC) {
		this.lstUC = lstUC;
	}

	public boolean isDataSyllabo() {
		return dataSyllabo;
	}

	public void setDataSyllabo(boolean dataSyllabo) {
		this.dataSyllabo = dataSyllabo;
	}

	/** Metodos para agregar datos del fomulario del sillabo **/

	public UnidadCurricular getUc() {
		return uc;
	}

	public void setUc(UnidadCurricular uc) {
		this.uc = uc;
	}

	public String getTipAsignatura() {
		return tipAsignatura;
	}

	public void setTipAsignatura(String tipAsignatura) {
		this.tipAsignatura = tipAsignatura;
	}

	public String getPrdAcademico() {
		return prdAcademico;
	}

	public void setPrdAcademico(String prdAcademico) {
		this.prdAcademico = prdAcademico;
	}

	public List<Syllabo> getLstSyllabos() {
		return lstSyllabos;
	}

	public void setLstSyllabos(List<Syllabo> lstSyllabos) {
		this.lstSyllabos = lstSyllabos;
	}

	// public List<ContenidoCurricular> getLstContenidos() {
	// return lstContenidos;
	// }
	//
	// public void setLstContenidos(List<ContenidoCurricular> lstContenidos) {
	// this.lstContenidos = lstContenidos;
	// }
	//
	// public List<Actividad> getLstActividads() {
	// return lstActividads;
	// }
	//
	// public void setLstActividads(List<Actividad> lstActividads) {
	// this.lstActividads = lstActividads;
	// }
	//
	// public List<Herramienta> getLstherramientas() {
	// return lstherramientas;
	// }
	//
	// public void setLstherramientas(List<Herramienta> lstherramientas) {
	// this.lstherramientas = lstherramientas;
	// }
	//
	// public List<Metodologia> getLstMetodologias() {
	// return lstMetodologias;
	// }
	//
	// public void setLstMetodologias(List<Metodologia> lstMetodologias) {
	// this.lstMetodologias = lstMetodologias;
	// }
	//
	// public List<Bibliografia> getLstBibliografias() {
	// return lstBibliografias;
	// }
	//
	// public void setLstBibliografias(List<Bibliografia> lstBibliografias) {
	// this.lstBibliografias = lstBibliografias;
	// }
	//
	// public List<RecursoDidactico> getLstRecursosDidacticos() {
	// return lstRecursosDidacticos;
	// }
	//
	// public void setLstRecursosDidacticos(List<RecursoDidactico>
	// lstRecursosDidacticos) {
	// this.lstRecursosDidacticos = lstRecursosDidacticos;
	// }

}
