/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     ProgramaPosgradoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre los programas de posgrado. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 20/06/2018           	Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.ProgramaPosgradoDto;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProgramaPosgradoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.CampoFormacion;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Cronograma;
import ec.edu.uce.academico.jpa.entidades.publico.CronogramaProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.Duracion;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Modalidad;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemico;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemicoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacionMalla;
import ec.edu.uce.academico.jpa.entidades.publico.TipoMateria;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSede;
import ec.edu.uce.academico.jpa.entidades.publico.Titulo;
import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.academico.jpa.entidades.publico.Vigencia;

/**
 * Clase (Bean)ProgramaPosgradoServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ProgramaPosgradoServicioImpl implements ProgramaPosgradoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@EJB
	private ParaleloDtoServicioJdbc servParaleloDtoServicioJdbc;
	
	@Resource
	private SessionContext session;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void guardarConfiguracionPrograma(ProgramaPosgradoDto programa, boolean programaExiste){
		
		try {
			session.getUserTransaction().begin();
			PeriodoAcademico prac = new PeriodoAcademico();
			prac.setPracDescripcion(programa.getPracDescripcion());
			prac.setPracEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			prac.setPracFechaIncio(programa.getPracFechaIncio());
			prac.setPracFechaFin(programa.getPracFechaFin());
			prac.setPracTipo(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
			em.persist(prac);
			Cronograma crn = new Cronograma();
			crn.setCrnDescripcion(programa.getCrnDescripcion());
			crn.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
			crn.setCrnTipo(CronogramaConstantes.TIPO_POSGRADO_VALUE);
			crn.setCrnPeriodoAcademico(prac);
			em.persist(crn);
			
			CronogramaProcesoFlujo crprfl = new CronogramaProcesoFlujo();
			crprfl.setCrprCronograma(crn);
			crprfl.setCrprProcesoFlujo(em.find(ProcesoFlujo.class, 2));
			em.persist(crprfl);
			
			CronogramaProcesoFlujo crprfl1 = new CronogramaProcesoFlujo();
			crprfl1.setCrprCronograma(crn);
			crprfl1.setCrprProcesoFlujo(em.find(ProcesoFlujo.class, 3));
			em.persist(crprfl1);
			
			CronogramaProcesoFlujo crprfl2 = new CronogramaProcesoFlujo();
			crprfl2.setCrprCronograma(crn);
			crprfl2.setCrprProcesoFlujo(em.find(ProcesoFlujo.class, 5));
			em.persist(crprfl2);
			
			CronogramaProcesoFlujo crprfl3 = new CronogramaProcesoFlujo();
			crprfl3.setCrprCronograma(crn);
			crprfl3.setCrprProcesoFlujo(em.find(ProcesoFlujo.class, 6));
			em.persist(crprfl3);
			
			PlanificacionCronograma plcr = new PlanificacionCronograma();
			plcr.setPlcrCronogramaProcesoFlujo(crprfl);
			plcr.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
			plcr.setPlcrFechaInicio(new Timestamp(programa.getPlcrFechaInicioOrdinaria().getTime()));
			plcr.setPlcrFechaFin(new Timestamp(programa.getPlcrFechaFinOrdinaria().getTime()));
			em.persist(plcr);
			PlanificacionCronograma plcrExtra = new PlanificacionCronograma();
			plcrExtra.setPlcrCronogramaProcesoFlujo(crprfl1);
			plcrExtra.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
			plcrExtra.setPlcrFechaInicio(new Timestamp(programa.getPlcrFechaInicioExtra().getTime()));
			plcrExtra.setPlcrFechaFin(new Timestamp(programa.getPlcrFechaFinExtra().getTime()));
			em.persist(plcrExtra);
			if(!programaExiste){
				NucleoProblemico ncpr = new NucleoProblemico();
				ncpr.setNcprDescripcion(programa.getListaNucleoProblemico().get(0).getNcprDescripcion());
				ncpr.setNcprEstado(0);
				em.persist(ncpr);
				List<NucleoProblemicoCarrera> listaNcprcr = new ArrayList<NucleoProblemicoCarrera>();
				for (int i = 0; i<programa.getListaNucleoProblemico().size();i++) {
					NucleoProblemicoCarrera ncprcr = new NucleoProblemicoCarrera();
					ncprcr.setNcprcrNucleoProblemico(ncpr);
					ncprcr.setNcprcrCarrera(programa.getListaNucleoProblemicoCarrera().get(i).getNcprcrCarrera());
					ncprcr.setNcprcrEstado(0);
					em.persist(ncprcr);
					listaNcprcr.add(ncprcr);
				}
				MallaCurricular mlcrAux = new MallaCurricular();
				mlcrAux.setMlcrCarrera(programa.getMlcrCarrera());
				mlcrAux.setMlcrCodigo(programa.getMlcrCodigo());	
				mlcrAux.setMlcrDescripcion(programa.getMlcrDescripcion().toUpperCase());
				mlcrAux.setMlcrFechaInicio(programa.getMlcrFechaInicio());
				mlcrAux.setMlcrFechaFin(programa.getMlcrFechaFin());
				mlcrAux.setMlcrEstado(MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE);
				mlcrAux.setMlcrTipoAprobacion(1);
				mlcrAux.setMlcrVigente(0);
				mlcrAux.setMlcrTipoOrgAprendizaje(2);
				TipoFormacionMalla tpfrmlAux = new TipoFormacionMalla();
				tpfrmlAux = em.find(TipoFormacionMalla.class, 4);
				mlcrAux.setMlcrTipoFormacionMalla(tpfrmlAux);
				em.persist(mlcrAux);
				
				MallaPeriodo mlprAux = new MallaPeriodo();
				mlprAux.setMlprEstado(MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
				mlprAux.setMlprMallaCurricular(mlcrAux);
				mlprAux.setMlprPeriodoAcademico(prac);
				em.persist(mlprAux);
				
				
				for (int i = 0; i<programa.getListaMateriaDto().size();i++) {
					Materia mtrAux = new Materia();
					mtrAux.setMtrDescripcion(programa.getListaMateriaDto().get(i).getMtrDescripcion().toUpperCase());
					mtrAux.setMtrCodigo(programa.getListaMateriaDto().get(i).getMtrCodigo());
					CampoFormacion cmfr = em.find(CampoFormacion.class, 10);
					TipoMateria tpmt = em.find(TipoMateria.class, 12);
					mtrAux.setmtrCampoFormacion(cmfr);
					mtrAux.setMtrTipoMateria(tpmt);
					mtrAux.setMtrNucleoProblemicoCarrera(listaNcprcr.get(programa.getListaMateriaDto().get(i).getNvlId()-1));
					mtrAux.setMtrCarrera(programa.getMlcrCarrera());
					em.persist(mtrAux);
					Nivel nvl1 = new Nivel();
					nvl1=em.find(Nivel.class, programa.getListaMateriaDto().get(i).getNvlId());	
					MallaCurricularMateria mlcrmt = new MallaCurricularMateria();
					mlcrmt.setMlcrmtNivel(nvl1);
					mlcrmt.setMlcrmtMallaCurricular(mlcrAux);
					mlcrmt.setMlcrmtMateria(mtrAux);
					mlcrmt.setMlcrmtEstado(MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
					em.persist(mlcrmt);
					
					StringBuilder sb = new StringBuilder();
					sb.append("PG-");
					String[] arregloNombre = programa.getMlcrCarrera().getCrrDescripcion().split(" ");
					for (int j = 0; j < arregloNombre.length; j++) {
						if(arregloNombre[j].length()>3){
							sb.append(arregloNombre[j].substring(0, 1));
						}
					}
					sb.append(programa.getListaMateriaDto().get(0).getNvlId());
					sb.append("-");
					try {
						List<ParaleloDto> listaParalelos = new ArrayList<ParaleloDto>();
						listaParalelos = servParaleloDtoServicioJdbc.listarXNivelCarreraXperiodo(programa.getMlcrCarrera().getCrrId(),programa.getPracId(), programa.getListaMateriaDto().get(i).getNvlId());
						if(listaParalelos.size()==0){
							sb.append(1);
						}else{
							sb.append(listaParalelos.size()+1);
						}
					} catch (ParaleloDtoException | ParaleloDtoNoEncontradoException e) {
						sb.append(1);
					}
					Paralelo prlAux = null;
					try {
						StringBuffer sbsql = new StringBuffer();
						sbsql.append(" Select prl from Paralelo prl ");
						sbsql.append(" where prl.prlCodigo =:codigo ");
						Query q = em.createQuery(sbsql.toString());
						q.setParameter("codigo", sb.toString());
						prlAux = (Paralelo) q.getSingleResult();
					} catch (Exception e) {
					}
					if(prlAux!=null){
						//No es necesario crear
					}else{
						prlAux = new Paralelo();
						prlAux.setPrlCodigo(sb.toString());
						prlAux.setPrlDescripcion(sb.toString());
						prlAux.setPrlPeriodoAcademico(prac);
						prlAux.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
						prlAux.setPrlCarrera(programa.getMlcrCarrera());
						em.persist(prlAux);
					}
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmt);
					mlcrprAux.setMlcrprParalelo(prlAux);
					mlcrprAux.setMlcrprInscritos(0);
					mlcrprAux.setMlcrprCupo(100);
					em.persist(mlcrprAux);
				}
				Titulo ttl = new Titulo();
				if(programa.getTtlId()==GeneralesConstantes.APP_ID_BASE){
					ttl.setTtlDescripcion(programa.getTtlDescripcion().toUpperCase());
					ttl.setTtlTipo(4);
					em.persist(ttl);
				}else{
					ttl = em.find(Titulo.class, programa.getTtlId());
				}
				ConfiguracionCarrera cncr = null;
				try {
					cncr = new ConfiguracionCarrera();
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
					sbsql.append(" where cncr.cncrTitulo.ttlDescripcion =:titulo ");
					sbsql.append(" and cncr.cncrCarrera.crrId =:carrera ");
					Query q = em.createQuery(sbsql.toString());
					q.setParameter("titulo", programa.getTtlDescripcion().toUpperCase());
					q.setParameter("carrera", programa.getMlcrCarrera().getCrrId());
					cncr = (ConfiguracionCarrera) q.getSingleResult();
				} catch (Exception e) {
					cncr = new ConfiguracionCarrera();
					cncr.setCncrCarrera(programa.getMlcrCarrera());
					cncr.setCncrTitulo(ttl);
					Modalidad mdl = em.find(Modalidad.class, 1);
					cncr.setCncrModalidad(mdl);
					Vigencia vgn = em.find(Vigencia.class, 1);
					cncr.setCncrVigencia(vgn);
					Ubicacion ubc = em.find(Ubicacion.class, 450);
					cncr.setCncrUbicacion(ubc);
					TipoSede ts = em.find(TipoSede.class, 1);
					cncr.setCncrTipoSede(ts);
					Duracion drc = em.find(Duracion.class, 7);
					cncr.setCncrDuracion(drc);
					em.persist(cncr);
				}
	
			}else{
				MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
				mlcrmtAux = em.find(MallaCurricularMateria.class, programa.getListaMateriaDto().get(0).getMlcrmtId());
				MallaCurricular mlcrAux = em.find(MallaCurricular.class, programa.getMlcrId());
//				MallaCurricular mlcr = new MallaCurricular();
//				mlcr.setMlcrCarrera(mlcrAux.getMlcrCarrera());
//				mlcr.setMlcrCodigo(mlcrAux.getMlcrCodigo());
//				mlcr.setMlcrDescripcion(mlcrAux.getMlcrDescripcion());
//				mlcr.setMlcrEstado(mlcrAux.getMlcrEstado());
//				mlcr.setMlcrFechaFin(mlcrAux.getMlcrFechaFin());
//				mlcr.setMlcrFechaInicio(mlcrAux.getMlcrFechaInicio());
//				mlcr.setMlcrTipoAprobacion(mlcrAux.getMlcrTipoAprobacion());
//				em.persist(mlcr);
				
				MallaPeriodo mlpr = new MallaPeriodo();
				mlpr.setMlprEstado(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_ACTIVO_VALUE);
				mlpr.setMlprMallaCurricular(mlcrAux);
				mlpr.setMlprPeriodoAcademico(prac);
				em.persist(mlpr);
				
				
				StringBuilder sb = new StringBuilder();
				Carrera crrAux = em.find(Carrera.class, programa.getCrrId());
				boolean op = true;
				Paralelo prlAux = null;
				for (int i = 0; i<programa.getListaMateriaDto().size();i++) {
					MallaCurricularMateria mlcrmt = new MallaCurricularMateria();
					mlcrmt = em.find(MallaCurricularMateria.class, programa.getListaMateriaDto().get(i).getMlcrmtId());
//					MallaCurricularMateria mlcrmtAux1 = new MallaCurricularMateria();
//					mlcrmtAux1.setMlcrmtEstado(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
//					mlcrmtAux1.setMlcrmtNivel(mlcrmt.getMlcrmtNivel());
//					mlcrmtAux1.setMlcrmtMallaCurricular(mlcr);
//					mlcrmtAux1.setMlcrmtMateria(mlcrmt.getMlcrmtMateria());
//					mlcrmtAux1.setMlcrmtEstado(MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
//					em.persist(mlcrmtAux1);
					
					
					sb = new StringBuilder();
					sb.append("PG-");
					
					String[] arregloNombre = crrAux.getCrrDescripcion().split(" ");
					for (int j = 0; j < arregloNombre.length; j++) {
						if(arregloNombre[j].length()>3){
							sb.append(arregloNombre[j].substring(0, 1));
						}
					}
					sb.append(programa.getListaMateriaDto().get(0).getNvlId());
					sb.append("-");
					try {
						List<ParaleloDto> listaParalelos = new ArrayList<ParaleloDto>();
						listaParalelos = servParaleloDtoServicioJdbc.listarXNivelCarreraXperiodo(programa.getCrrId(),programa.getPracId(), programa.getListaMateriaDto().get(i).getNvlId());
						if(listaParalelos.size()==0){
							sb.append(1);
						}else{
							sb.append(listaParalelos.size()+1);
						}
					} catch (ParaleloDtoException | ParaleloDtoNoEncontradoException e) {
						sb.append(1);
					}
					
					
					
//					System.out.println(sb.toString());
//					
//					try {
//						StringBuffer sbsql = new StringBuffer();
//						sbsql.append(" Select prl from Paralelo prl ");
//						sbsql.append(" where prl.prlCodigo =:codigo ");
//						sbsql.append(" and prl.pracId =:preiodo ");
//						Query q = em.createQuery(sbsql.toString());
//						q.setParameter("codigo", sb.toString());
//						q.setParameter("preiodo", prac.getPracId());
//						prlAux = (Paralelo) q.getSingleResult();
//					} catch (Exception e) {
//					}
					if(op){
//						//AUMENTO MQ
//						if(prlAux.getPrlPeriodoAcademico().getPracId()==(prac.getPracId())){ 
//							//No es necesario crear
//						}else{ 
//						prlAux = new Paralelo();
//						prlAux.setPrlCodigo(sb.toString());
//						prlAux.setPrlDescripcion(sb.toString());
//						prlAux.setPrlPeriodoAcademico(prac);
//						prlAux.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
//						prlAux.setPrlCarrera(crrAux);
//						em.persist(prlAux);
//						} 
						//FIN AUMENTO MQ
												
						prlAux = new Paralelo();
						prlAux.setPrlCodigo(sb.toString());
						prlAux.setPrlDescripcion(sb.toString());
						prlAux.setPrlPeriodoAcademico(prac);
						prlAux.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
						//prlAux.setPrlCarrera(programa.getMlcrCarrera());
						prlAux.setPrlCarrera(crrAux); //MQ
						em.persist(prlAux);
						op=false;
					}
					
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux.setMlcrprMallaCurricularMateria(mlcrmt);
					mlcrprAux.setMlcrprParalelo(prlAux);
					mlcrprAux.setMlcrprInscritos(0);
					mlcrprAux.setMlcrprCupo(100);
					em.persist(mlcrprAux);
				}
			}
		session.getUserTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
} 
	
	
