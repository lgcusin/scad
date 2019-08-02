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

 ARCHIVO:     MallaCurricularNivelServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla MallaCurricular. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                               COMENTARIOS
 08-ENE-2019      			Freddy guzmán 						Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularNivelServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularNivelServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularNivel;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;

/**
 * Clase (Bean)MallaCurricularNivelServicioImpl.
 * Bean declarado como Stateless.
 * @author fgguzman
 * @version 1.0
 */

@Stateless
public class MallaCurricularNivelServicioImpl implements MallaCurricularNivelServicio{


	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@EJB private MallaCurricularNivelServicioJdbc servJdbcMallaCurricularNivel;
	

	@SuppressWarnings("unchecked")
	public void registrarCreditosPorNivel() throws MallaCurricularNivelValidacionException, MallaCurricularNivelException{

		try {
			List<Dependencia> dependencias = new ArrayList<>();
			Query sql1 = em.createNamedQuery("Dependencia.findPorJerarquia");
			sql1.setParameter("jerarquia", DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);

			dependencias = sql1.getResultList();
			if (!dependencias.isEmpty()) {
				for (Dependencia dependencia : dependencias) {
					try {
						List<Carrera> carreras = new ArrayList<>();
						Query sql2 = em.createNamedQuery("Carrera.findPorDependenciaTipoCrr");
						sql2.setParameter("dpnId", dependencia.getDpnId());
						sql2.setParameter("crrTipo", CarreraConstantes.TIPO_PREGRADO_VALUE);

						carreras = sql2.getResultList();
						if (!carreras.isEmpty()) {
							for (Carrera carrera : carreras) {
								try {
									List<MallaCurricularNivelDto> mallaPorNiveles = servJdbcMallaCurricularNivel.buscar(carrera.getCrrId());
									if (!mallaPorNiveles.isEmpty()) {
										for (MallaCurricularNivelDto item : mallaPorNiveles) {
											try {
												MallaCurricularNivel mallaPorNivel = null;
												Query sql3 = em.createNamedQuery("MallaCurricularNivel.findPorMallaCurricularNivel");
												sql3.setParameter("mallaCurricularId", item.getMlcrnvMlcrId());
												sql3.setParameter("nivelId", item.getMlcrnvNvlId());
												mallaPorNivel = (MallaCurricularNivel) sql3.getSingleResult();
												if (mallaPorNivel != null) {
													mallaPorNivel.setMlcrnvCreditos(item.getMlcrnvHoras());
													mallaPorNivel.setMlcrnvCreditosAcumulado(item.getMlcrnvHorasAcumulado());
													em.merge(mallaPorNivel);
													em.flush();
												}
											} catch (Exception e) {
												MallaCurricularNivel mlcrnv = new MallaCurricularNivel();
												Nivel nivel = em.find(Nivel.class, item.getMlcrnvNvlId());
												MallaCurricular malla = em.find(MallaCurricular.class, item.getMlcrnvMlcrId());

												mlcrnv.setMlcrnvCreditos(item.getMlcrnvHoras());
												mlcrnv.setMlcrnvCreditosAcumulado(item.getMlcrnvHorasAcumulado());
												mlcrnv.setMlcrnvNivel(nivel);
												mlcrnv.setMlcrnvMallaCurricular(malla);

												em.persist(mlcrnv);
												em.flush();
											}
										}
									}
								} catch (MallaCurricularNivelNoEncontradoException e) {
								} catch (MallaCurricularNivelException e) {
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			throw new MallaCurricularNivelException();
		}

	}


	@Override
	public void actualizarCreditosPorCarrera(int crrId) throws MallaCurricularNivelValidacionException, MallaCurricularNivelException {
		try {
			if (crrId != 0) {
				try {
					List<MallaCurricularNivelDto> mallaPorNiveles = servJdbcMallaCurricularNivel.buscar(crrId);
					if (!mallaPorNiveles.isEmpty()) {
						for (MallaCurricularNivelDto item : mallaPorNiveles) {

							try {
								MallaCurricularNivel mallaPorNivel = null;
								Query sql3 = em.createNamedQuery("MallaCurricularNivel.findPorMallaCurricularNivel");
								sql3.setParameter("mallaCurricularId", item.getMlcrnvMlcrId());
								sql3.setParameter("nivelId", item.getMlcrnvNvlId());
								mallaPorNivel = (MallaCurricularNivel) sql3.getSingleResult();
								if (mallaPorNivel != null) {
									mallaPorNivel.setMlcrnvCreditos(item.getMlcrnvHoras());
									mallaPorNivel.setMlcrnvCreditosAcumulado(item.getMlcrnvHorasAcumulado());
									em.merge(mallaPorNivel);
									em.flush();
								}
							} catch (NoResultException e) {
								MallaCurricularNivel mlcrnv = new MallaCurricularNivel();
								Nivel nivel = em.find(Nivel.class, item.getMlcrnvNvlId());
								MallaCurricular malla = em.find(MallaCurricular.class, item.getMlcrnvMlcrId());

								mlcrnv.setMlcrnvCreditos(item.getMlcrnvHoras());
								mlcrnv.setMlcrnvCreditosAcumulado(item.getMlcrnvHorasAcumulado());
								mlcrnv.setMlcrnvNivel(nivel);
								mlcrnv.setMlcrnvMallaCurricular(malla);

								em.persist(mlcrnv);
								em.flush();
							} catch (NonUniqueResultException e) {
							} catch (Exception e){
							}
						}
					}
				} catch (MallaCurricularNivelNoEncontradoException e) {
				} catch (MallaCurricularNivelException e) {
				}
			}else {
				throw new MallaCurricularValidacionException();
			}
		} catch (Exception e) {
			throw new MallaCurricularNivelException();
		}
	}


	@Override
	public MallaCurricularNivel buscarPorMallaCurricularNivel(int mlcrId, int nvlId) throws MallaCurricularNivelNoEncontradoException, MallaCurricularNivelValidacionException, MallaCurricularNivelException {
		MallaCurricularNivel retorno = null;

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT m FROM MallaCurricularNivel m ");
		sbsql.append(" WHERE m.mlcrnvMallaCurricular.mlcrId = :mallaCurricularId ");
		sbsql.append(" AND m.mlcrnvNivel.nvlId = :nivelId ");
		
		try {
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("mallaCurricularId", mlcrId);
			q.setParameter("nivelId", nvlId);
			retorno = (MallaCurricularNivel) q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new MallaCurricularNivelNoEncontradoException("No se encontró la Malla Curricular Nivel solicitada.");
		}catch (NonUniqueResultException e) {
			throw new MallaCurricularNivelValidacionException("Se encontró más de un resultado con los parámetros solicitados.");
		} catch (Exception e) {
			throw new MallaCurricularNivelException("Error desconocido al buscar la Malla Curricular Nivel.");
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<MallaCurricularNivel> buscarCreditosPorNivel(int carreraId) throws MallaCurricularNivelNoEncontradoException, MallaCurricularNivelValidacionException, MallaCurricularNivelException {
		List<MallaCurricularNivel> retorno = new ArrayList<>();

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT mlcrnv FROM MallaCurricularNivel mlcrnv, MallaCurricular mlcr, Carrera crr ");
		sbsql.append(" WHERE  mlcr.mlcrId = mlcrnv.mlcrnvMallaCurricular.mlcrId ");
		sbsql.append(" AND crr.crrId = mlcr.mlcrCarrera.crrId  ");
		sbsql.append(" AND crr.crrId = :carreraId  ");
		
		try {
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("carreraId", carreraId);
			retorno = q.getResultList();
			
		} catch (NoResultException e) {
			throw new MallaCurricularNivelNoEncontradoException("No se encontró registros en la  tabla MallaCurricularNivel para la carrera solicitada.");
		} catch (Exception e) {
			throw new MallaCurricularNivelException("Error desconocido al buscar MallaCurricularNivel por Carrera.");
		}
		
		return retorno;
	}

	
	
}
