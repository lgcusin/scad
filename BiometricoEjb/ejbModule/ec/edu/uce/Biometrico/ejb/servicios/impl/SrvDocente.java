package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialException;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.biometrico.jpa.*;


/**
 * Session Bean implementation class DocenteBean
 * 
 * @author Wilson-DK
 *
 */
@Stateless
@Local
public class SrvDocente implements SrvDocenteLocal {

	@PersistenceContext
	private EntityManager em;

	public SrvDocente() {
		//
	}

	/**
	 * Lista los parametros de holgura para el registro del control academico en
	 * la facultad
	 * 
	 * @param parametro
	 *            Nombre de la holgura del horario academico
	 * @param integer
	 *            Posicion de la holgura
	 * @return
	 */
	@Override
	public List<FichaDocente> listarDocentesxParametroxFacultad(String param, Integer fcId) {
		param = param.toUpperCase();
		param = "%" + param + "%";
		List<FichaDocente> lstD = null;
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery(
					"select distinct fcdc,prs from DetallePuesto as dtps "
							+ "join dtps.dtpsFichaDocente as fcdc "
							+ "join fcdc.fcdcPersona as prs "
							+ "join dtps.dtpsCarrera as crr "
							+ "join crr.crrDependencia as dpn "
							+ "where (prs.prsPrimerApellido LIKE :param or prs.prsSegundoApellido LIKE :param or prs.prsNombres LIKE :param) and dpn.dpnId=:fcId "
							+ "order by prs.prsPrimerApellido, prs.prsSegundoApellido");
			query.setParameter("param", param).setParameter("fcId", fcId);
			lstD= new ArrayList<>();
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				FichaDocente fd = (FichaDocente) objArray[0];
				fd.setFcdcPersona((Persona) objArray[1]);
				lstD.add(fd);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar docentes por facultad");
			return lstD;
		}
		return lstD;
	}

	/**
	 * Metodo que obtiene los diferentes tipos de huellas de los dedos a
	 * registrar del docente
	 * 
	 * @return Tipos de huellas para selecccionar
	 */
	@Override
	public List<TipoHuella> listarTipoHuellas() {
		List<TipoHuella> lstTipoHuella = null;
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery("select tphl from TipoHuella as tphl where tphl.tphlDescripcionDedo <> 'INACTIVO' order by tphl.tphlId");
			lstTipoHuella = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// objArray = (Object[]) obj;
			// lstTipoHuella.add((TipoHuella) objArray[0]);
			// }
		} catch (Exception e) {
			System.out.println("Error al consultar el tipo de huella");
			return lstTipoHuella;
		}
		return lstTipoHuella;
	}

	/**
	 * Metodo que obtiene la lista de huellas del docente
	 * 
	 * @param fcdc_id
	 *            id del Docente
	 * @return huellas del docente
	 * @throws SQLException
	 * @throws IOException
	 */
	@Override
	public List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException {
		List<BufferedImage> lstI = null;
		try {
			List<HuellaDactilar> lsth = em.createNamedQuery("HuellaDactilar.findAllById", HuellaDactilar.class)
					.setParameter("idDcnt", fcdc_id).getResultList();
			for (HuellaDactilar hldc : lsth) {
				lstI.add(toBufferedImage(hldc.getHldPrimerHuella()));
				lstI.add(toBufferedImage(hldc.getHldSegundaHuella()));
			}
		} catch (Exception e) {
			System.out.println("Error al obtener huellas del docente");
			return lstI;
		}
		return lstI;
	}

	/**
	 * Transforma un campo Blob a Buffered
	 * 
	 * @param blb
	 *            Blob de base
	 * @return Bufferred transformado
	 * @throws SQLException
	 * @throws IOException
	 */
	public BufferedImage toBufferedImage(Blob blb) {
		Blob blob = blb;
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Metodo Obtiene una Huella Dactilar del docente x el tipo de huella del
	 * dedo
	 * 
	 * @param fdId
	 *            id del docente
	 * @param thid
	 *            de tipo de huella
	 * @return Huella Dactilar del docente
	 */
	public HuellaDactilar findHuella(Integer fdId, Integer thid) {
		HuellaDactilar hldc = null;
		try {
			Query query = em.createQuery(
					"select hldc from HuellaDactilar as hldc where hldc.hldcFichaDocente.fcdcId=:fdId and hldc.hldcTipoHuella.tphlId=:thid");
			query.setParameter("fdId", fdId).setParameter("thid", thid);
			List<HuellaDactilar> lstH = query.getResultList();
			if (lstH == null || !lstH.isEmpty()) {
				hldc = lstH.get(0);
			}
		} catch (Exception e) {
			System.out.println("Error al buscar huella"+e.getMessage());
			return hldc;
		}
		return hldc;
	}

	/**
	 * Metodo para guardar un nuevo registro de huella dactilar
	 * 
	 * @param bimg1
	 *            buffer de primera huella
	 * @param bimg2
	 *            buffer de segunda huella
	 * @param fcdc
	 *            id del docente
	 * @param tphl
	 *            id del tipo huella
	 * @throws IOException
	 * @throws SerialException
	 * @throws SQLException
	 */
	@Override
	public void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException {
		HuellaDactilar hldc;
		hldc = findHuella(fcdc.getFcdcId(), tphl.getTphlId());
		if (hldc != null) {
			for (int i = 0; i < 2; i++) {
				ByteArrayOutputStream baos = null;
				try {
					baos = new ByteArrayOutputStream();
					if (i == 0) {
						ImageIO.write(bimg1, "png", baos);
					}
					ImageIO.write(bimg2, "png", baos);
				} finally {
					try {
						baos.close();
					} catch (Exception e) {
					}
				}
				Blob huella = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
				if (i == 0) {
					hldc.setHldPrimerHuella(huella);
				}
				hldc.setHldSegundaHuella(huella);
			}
			hldc.setHldcFichaDocente(fcdc);
			hldc.setHldcTipoHuella(tphl);
			em.merge(hldc);
		} else {
			hldc = new HuellaDactilar();
			for (int i = 0; i < 2; i++) {
				ByteArrayOutputStream baos = null;
				try {
					baos = new ByteArrayOutputStream();
					if (i == 0) {
						ImageIO.write(bimg1, "png", baos);
					}
					ImageIO.write(bimg2, "png", baos);
				} finally {
					try {
						baos.close();
					} catch (Exception e) {
					}
				}
				Blob huella = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
				if (i == 0) {
					hldc.setHldPrimerHuella(huella);
				}
				hldc.setHldSegundaHuella(huella);
			}
			hldc.setHldcFichaDocente(fcdc);
			hldc.setHldcTipoHuella(tphl);
			em.persist(hldc);
		}
	}

	/**
	 * Metodo que obtiene asistencia en la hora de registro academico
	 * 
	 * @param id
	 *            id del docente
	 * @param inicio
	 *            fecha inicio del rango
	 * @param fin
	 *            fecha fin del rango
	 * @param crrId
	 *            id de la Carrera
	 * @return
	 */
	@Override
	public List<Asistencia> listarAsistencia(Integer fdId, Date inicio, Date fin, Integer crrId) {
		List<Asistencia> listAs = null;
		try {
			Object[] objArray;
			Query query;
			if (crrId != null) {
				query = em.createQuery(
						"select ass,hrac,mlcrpr,mlcrmt,mtr,crr,fd, p from Asistencia as ass "
								+ "join ass.assHorarioAcademico as hrac join ass.assFichaDocente as fd join fd.fcdcPersona as p "
								+ "join hrac.hracMallaCurricularParalelo as mlcrpr "
								+ "join mlcrpr.mlcrprMallaCurricularMateria as mlcrmt join mlcrmt.mlcrmtMateria as mtr join mtr.mtrCarrera as crr "
								+ "where ass.assFichaDocente.fcdcId=:fcdcId and ass.assFecha >= :fechaInicio and ass.assFecha <= :fechaFin and crr.crrId=:crrId"
								+ " order by ass.assFecha asc");
				query.setParameter("crrId", crrId);
			} else {
				query = em.createQuery("select ass,hrac,mlcrpr,prl,mlcrmt,mtr,hoclal,hocl, fd,p from Asistencia as ass "
						+ "join ass.assHorarioAcademico as hrac join ass.assFichaDocente as fd join fd.fcdcPersona as p "
						+ "join hrac.hracMallaCurricularParalelo as mlcrpr "
						+ "join mlcrpr.mlcrprMallaCurricularMateria as mlcrmt join mlcrpr.mlcrprParalelo as prl "
						+ "join mlcrmt.mlcrmtMateria as mtr join hrac.hracHoraClaseAula as hoclal join hoclal.hoclalHoraClase as hocl "
						+ "where ass.assFichaDocente.fcdcId=:fcdcId and ass.assFecha >= :fechaInicio and ass.assFecha <= :fechaFin order by ass.assFecha, mtr.mtrDescripcion, prl.prlDescripcion asc");
			}
			query.setParameter("fcdcId", fdId);
			query.setParameter("fechaInicio", inicio);
			query.setParameter("fechaFin", fin);
			listAs = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				listAs.add((Asistencia) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar Asistencias: " + e.getMessage());
			return listAs;
		}
		return listAs;
	}

	/**
	 * Permite buscar el horario asignado a la asistencia a justificar
	 * 
	 * @param assId
	 * @return
	 */
	@Override
	public HorarioAcademico findHorarioByAsistencia(Asistencia asis) {
		HorarioAcademico horario = null;
		try {
			Object[] arrayObj;
			List<HorarioAcademico> lstH = new ArrayList<>();
			Query query = em
					.createQuery("select hrac, hca, hc from HorarioAcademico as hrac join hrac.hracHoraClaseAula as hca join hca.hoclalHoraClase as hc "
							+ "where hrac.hracMallaCurricularParalelo.mlcrprId=:mlcrpr and hrac.hracDia=:dia");
			query.setParameter("mlcrpr", asis.getAssHorarioAcademico().getHracMallaCurricularParalelo().getMlcrprId())
					.setParameter("dia", asis.getAssFecha().getDay() - 1);
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstH.add((HorarioAcademico) arrayObj[0]);
			}
			horario = lstH.get(0);
			horario.setHracHoraFin(lstH.get(lstH.size() - 1).getHracHoraFin());
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
			return horario;
		}
		return horario;
	}

	/**
	 * Actualiza la asistencia justificada
	 * 
	 * @param asistencia
	 */
	@Override
	public void actualizarAsistencia(Asistencia asistencia) {
		if (asistencia != null) {
			em.merge(asistencia);
		}
	}

	/**
	 * Obtiene las actividades microcurricualres de la materia y docente
	 * 
	 * @param fcdcId
	 *            id de Docente
	 * @param mtrId
	 *            id de Materia
	 * @return
	 */
	@Override
	public List<Seguimiento> listarSeguimientosxDocenteMateria(Integer fcdcId, Integer mtrId) {
		List<Seguimiento> lstS = null;
		try {
			Object[] objArray;
			Query query = em
					.createQuery("select sgm, ass, cncr, uncr from Seguimiento as sgm "
							+ "join sgm.asistencia as ass "
							+ "join sgm.sgmContenidoCurricular as cncr "
							+ "join cncr.unidadCurricular as uncr "
							+ "where sgm.sgmMallaCurricularParalelo.mlcrprMallaCurricularMateria.mlcrmtMateria.mtrId=:mtrId "
							+ "and ass.assFichaDocente.fcdcId=:fcdcId order by sgm.sgmId asc");
			query.setParameter("fcdcId", fcdcId).setParameter("mtrId", mtrId);
			lstS= new ArrayList<>();
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				lstS.add((Seguimiento) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar seguimiento por docente y materia");
			return lstS;
		}
		return lstS;
	}

	/**
	 * Guarda opciones de registro movil o registro mediante login
	 * 
	 * @param selectDcnt
	 *            Docente a enrolar
	 * @param selectTp
	 *            Tipo de huella del dedo a registrar
	 * @param flagMovil
	 *            Estado de registro movil
	 * @param flagSinHuella
	 *            Estado de registro x login
	 */
	@Override
	public void guardarActualizarEstados(FichaDocente selectDcnt, TipoHuella selectTp, boolean flagMovil,
			boolean flagSinHuella) {
		HuellaDactilar hd = null;
		hd = findHuella(selectDcnt.getFcdcId(), selectTp.getTphlId());
		if (hd == null) {
			hd= new HuellaDactilar();
			hd.setHldcFichaDocente(selectDcnt);
			hd.setHldcTipoHuella(selectTp);
			if (selectTp.getTphlId() == 5 && flagSinHuella) {
				hd.setHldcCodigoAuxiliar(1);
			} else if (selectTp.getTphlId() == 5 && !flagSinHuella) {
				hd.setHldcCodigoAuxiliar(0);
			}
			em.persist(hd);
		} else {
			if (selectTp.getTphlId() == 5 && flagSinHuella) {
				hd.setHldcCodigoAuxiliar(1);
			} else if (selectTp.getTphlId() == 5 && !flagSinHuella) {
				hd.setHldcCodigoAuxiliar(0);
			} else if (selectTp.getTphlId() == 4 && !flagMovil) {
				hd.setHldcTipoHuella(em.find(TipoHuella.class, 0));
			}
			em.merge(hd);
		}
	}

	/**
	 * Obtiene estados de registro movil o registro movil
	 * 
	 * @param fcdcId
	 *            del Docente
	 * @return Lista de estados
	 */
	@Override
	public List<Boolean> listarestados(Integer fcdcId) {
		List<Boolean> lstE = new ArrayList<>(2);
		lstE.add(0, false);
		lstE.add(1, false);
		List<HuellaDactilar> lstHD = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em
					.createQuery("select hldc,tphl from HuellaDactilar as hldc join hldc.hldcTipoHuella as tphl "
							+ "where hldc.hldcFichaDocente.fcdcId=:fcdcId and hldc.hldcTipoHuella.tphlId not in (1,2,3)")
					.setParameter("fcdcId", fcdcId);
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstHD.add((HuellaDactilar) arrayObj[0]);
			}
			for (HuellaDactilar hd : lstHD) {
				if (hd.getHldcCodigoAuxiliar() == 1) {
					lstE.add(0, true);
				} else if (hd.getHldcTipoHuella().getTphlId() == 4) {
					lstE.add(1, true);
				} else if (hd.getHldcCodigoAuxiliar() != 1) {
					lstE.add(0, false);
				} else if (hd.getHldcTipoHuella().getTphlId() == 0) {
					lstE.add(1, false);
				}
			}

		} catch (Exception e) {
			System.out.println("Erros obtener estados docente: "+e.getMessage());
			return lstE;
		}
		return lstE;
	}

	/**
	 * Verifica si tiene permisos de registro mediante login
	 * 
	 * @param regDcnt
	 *            Docente a verificar
	 * @return estado del permiso
	 */
	@Override
	public boolean verificarLogin(FichaDocente regDcnt) {
		boolean estado = false;
		try {
			Query query = em.createQuery("select hldc.hldcCodigoAuxiliar from HuellaDactilar as hldc "
					+ "where hldc.hldcFichaDocente.fcdcId=:fdId and hldc.hldcCodigoAuxiliar=1");
			query.setParameter("fdId", regDcnt.getFcdcId());
			Integer codigo = (Integer) query.getSingleResult();
			if (codigo == 1) {
				estado = true;
			} else {
				estado = false;
			}
		} catch (Exception e) {
			return estado;
		}
		return estado;
	}

	/**
	 * Obtiene una lista de materias de una carrera al que pertenece el docente
	 * 
	 * @param fcdcId
	 *            del Docente
	 * @param crrId
	 *            de la Carrera
	 * @return materias x carrera del docente
	 */
	@Override
	public List<Materia> listarMateriasxCarrera(Integer fcdcId, Integer crrId) {
		List<Materia> lstM = null;
		try {
			Query query = em.createQuery(
					"select  distinct mtr from HorarioAcademico as hrac join hrac.hracMallaCurricularParalelo as mlcrpr "
							+ "join mlcrpr.mlcrprParalelo as prl join mlcrpr.mlcrprMallaCurricularMateria as mlcrmt "
							+ "join mlcrmt.mlcrmtMateria as mtr join mtr.mtrCarrera as crr join mlcrmt.mlcrmtNivel as nvl "
							+ "join hrac.hracHoraClaseAula as hoclal join hoclal.hoclalHoraClase as hocl join hoclal.hoclalAula as al "
							+ "where mlcrpr.mlcrprId in (select mlcrpr.mlcrprId from CargaHoraria as crhr inner join crhr.crhrMallaCurricularParalelo as mlcrpr "
							+ "join crhr.crhrPeriodoAcademico as prac where prac.pracEstado=0 and crhr.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId group by mlcrpr.mlcrprId) "
							+ "and hoclal.hoclalEstado=0 and crr.crrId=:crrId order by mtr.mtrId asc");
			query.setParameter("crrId", crrId);
			query.setParameter("fdId", fcdcId);
			lstM = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// lstM.add(em.find(Materia.class, obj));
			// }
		} catch (Exception e) {
			System.out.println("Error al consultar materias por semestre: " + e);
			return lstM;
		}
		return lstM;
	}

	/**
	 * Permite obtener las asistencias de los docentes por facultad y enviar al
	 * mail cada mes.
	 * 
	 * @param fclId
	 * @return
	 */
	@Override
	public List<Asistencia> getAsistenciasReporte(Integer fclId, Date inicio, Date fin) {
		List<Asistencia> asistenciaList = new ArrayList<>();
		try {
			Object[] objArray;
			Query query;
			if (fclId != null) {
				query = em.createQuery("select ass,fcdc,prs,hrac,mlcrpr,mlcrmt,mtr,crr,dpn from Asistencia as ass "
						+ "join ass.assFichaDocente as fcdc join fcdc.fcdcPersona as prs join ass.assHorarioAcademico as hrac "
						+ "join hrac.hracMallaCurricularParalelo as mlcrpr join mlcrpr.mlcrprMallaCurricularMateria as mlcrmt join mlcrmt.mlcrmtMateria as mtr "
						+ "join mtr.mtrCarrera as crr join crr.crrDependencia as dpn where dpn.dpnId=:fclId and ass.assFecha >= :fechaInicio and ass.assFecha <= :fechaFin");
				query.setParameter("fclId", fclId);
				query.setParameter("fechaInicio", inicio);
				query.setParameter("fechaFin", fin);
				for (Object obj : query.getResultList()) {
					objArray = (Object[]) obj;
					Asistencia ad = (Asistencia) objArray[0];
					FichaDocente fd = (FichaDocente) objArray[1];
					Persona p = (Persona) objArray[2];
					HorarioAcademico ha = (HorarioAcademico) objArray[3];
					MallaCurricularParalelo mcp = (MallaCurricularParalelo) objArray[4];
					MallaCurricularMateria mcm = (MallaCurricularMateria) objArray[5];
					Materia m = (Materia) objArray[6];
					Carrera crr = (Carrera) objArray[7];
					Dependencia dp = (Dependencia) objArray[8];
					fd.setFcdcPersona(p);
					ad.setAssFichaDocente(fd);
					crr.setCrrDependencia(dp);
					m.setMtrCarrera(crr);
					mcm.setMlcrmtMateria(m);
					mcp.setMlcrprMallaCurricularMateria(mcm);
					ha.setHracMallaCurricularParalelo(mcp);
					ad.setAssHorarioAcademico(ha);
					asistenciaList.add(ad);
				}
			}
		} catch (Exception e) {
			System.out.println("Error en el proceso getAsistenciasReporte" + e.getStackTrace());
			return asistenciaList;
		}
		return asistenciaList;
	}
}
