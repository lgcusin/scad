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
   
 ARCHIVO:     HiloDeEnvio.java	  
 DESCRIPCION: Clase que define los procesos a ser realizados para validar con la Dinardap a través
 del uso de hilos 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19-12-2017				Daniel Albuja                       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.hilos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
/**
 * Clase (hilos)HiloDeEnvio. Clase que define los procesos a ser realizados para validar con la Dinardap a través
 * través del uso de hilos 	
 * 
 * 
 * @author dalbuja.
 * @version 1.0
 */

public class HiloDeEnvio implements Runnable{
	private List<Calificacion> listaAux;
	
	private Connection con;	
	public HiloDeEnvio(List<Calificacion> listaAux, Connection con) {
		this.listaAux = listaAux;
		this.con = con;
	}

	public void run() {
		for (Calificacion item : listaAux) {
			corregirSupletorio(item);
		}
		
	}
	
	public synchronized  void corregirSupletorio(Calificacion item){
		try {
			if(item.getClfParamRecuperacion1()!=null){
				BigDecimal asistenciaSumaP1P2 = BigDecimal.ZERO;
				BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
				BigDecimal notEstudiante2 = new BigDecimal(item.getClfNota2());
				BigDecimal notEstudiante1 = new BigDecimal(item.getClfNota1());
				BigDecimal notaSuma = BigDecimal.ZERO;
				BigDecimal promedioAsistencia = BigDecimal.ZERO;
				
				notaSuma = notEstudiante1.setScale(2, RoundingMode.HALF_UP).add(notEstudiante2.setScale(2, RoundingMode.HALF_UP));
				asistenciaSumaP1P2 = new BigDecimal(item.getClfAsistencia1()).add(new BigDecimal(item.getClfAsistencia2()));
				asistenciaSumaDocente = new BigDecimal(item.getClfAsistenciaDocente1()).add(new BigDecimal(item.getClfAsistenciaDocente2()));
				promedioAsistencia=calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, asistenciaSumaP1P2.intValue(), asistenciaSumaDocente.intValue());
				item.setClfSumaP1P2(notaSuma.floatValue());
				item.setClfPromedioAsistencia(promedioAsistencia.floatValue());
				item.setClfAsistenciaTotal(asistenciaSumaP1P2.floatValue());
				item.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
				int com = notaSuma.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
				int comparador = promedioAsistencia.compareTo(new BigDecimal(80));
				// si el promedio de asistencia es mayor o igual a 80
				if(comparador == 1 || comparador == 0){
					if(com == 1 || com == 0){
						item.setClfPromedioAsistencia(promedioAsistencia.setScale(0, RoundingMode.HALF_UP).floatValue());
						item.setClfNotaFinalSemestre((notaSuma.setScale(0, RoundingMode.HALF_UP)).floatValue());
						PreparedStatement pstmt = null;
						StringBuilder sbSql = new StringBuilder();
						sbSql.append(" update Record_estudiante set rces_estado = ");sbSql.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						sbSql.append(" where rces_id = ? ");
						pstmt = con.prepareStatement(sbSql.toString());
						pstmt.setInt(1, item.getRecordEstudiante().getRcesId());
						pstmt.executeUpdate();
						try {
							pstmt.close();
						} catch (Exception e) {
						}
						item.setClfSupletorio(null);
						item.setClfParamRecuperacion1(null);
						item.setClfParamRecuperacion2(null);
					}else{
						int minNota = notaSuma.compareTo(new BigDecimal(8.8));
						if(minNota==-1){
							item.setClfNotaFinalSemestre((notaSuma.setScale(2, RoundingMode.HALF_UP)).floatValue());
							PreparedStatement pstmt = null;
							StringBuilder sbSql = new StringBuilder();
							sbSql.append(" update Record_estudiante set rces_estado = ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							sbSql.append(" where rces_id = ? ");
							pstmt = con.prepareStatement(sbSql.toString());
							pstmt.setInt(1, item.getRecordEstudiante().getRcesId());
							pstmt.executeUpdate();
							try {
								pstmt.close();
							} catch (Exception e) {
							}
							item.setClfSupletorio(null);
							item.setClfParamRecuperacion1(null);
							item.setClfParamRecuperacion2(null);
						}else{
							try {
								if(item.getClfParamRecuperacion1()>0){
										BigDecimal parametro1Aux  = BigDecimal.ZERO;
										parametro1Aux  = notaSuma.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.HALF_UP);
										item.setClfParamRecuperacion1(parametro1Aux.floatValue());
										BigDecimal parametro2Aux  = BigDecimal.ZERO;
										parametro2Aux  = new BigDecimal(item.getClfSupletorio()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2,RoundingMode.HALF_UP);
										item.setClfParamRecuperacion2(parametro2Aux.floatValue());
											
										BigDecimal sumaParametros = BigDecimal.ZERO;
										sumaParametros = parametro1Aux.setScale(2, RoundingMode.DOWN).add(parametro2Aux.setScale(2, RoundingMode.HALF_UP));
										item.setClfNotaFinalSemestre(sumaParametros.floatValue());
										
										int estadoRces = (new BigDecimal(item.getClfNotaFinalSemestre())).compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
										if(estadoRces == 1 || estadoRces ==0){
											try {
												item.setClfNotaFinalSemestre((sumaParametros.setScale(0, RoundingMode.HALF_UP)).floatValue());
												item.setClfPromedioAsistencia(promedioAsistencia.setScale(0, RoundingMode.HALF_UP).floatValue());
												PreparedStatement pstmt = null;
												StringBuilder sbSql = new StringBuilder();
												sbSql.append(" update Record_estudiante set rces_estado = ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
												sbSql.append(" where rces_id = ? ");
												pstmt = con.prepareStatement(sbSql.toString());
												pstmt.setInt(1, item.getRecordEstudiante().getRcesId());
												pstmt.executeUpdate();
												pstmt.close();
											} catch (Exception e) {
												e.printStackTrace();
											}
											
										}else{
											PreparedStatement pstmt = null;
											StringBuilder sbSql = new StringBuilder();
											sbSql.append(" update Record_estudiante set rces_estado = ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
											sbSql.append(" where rces_id = ? ");
											pstmt = con.prepareStatement(sbSql.toString());
											pstmt.setInt(1, item.getRecordEstudiante().getRcesId());
											pstmt.executeUpdate();
											try {
												pstmt.close();
											} catch (Exception e) {
											}
										}	
									
								}
							} catch (Exception e) {
								item.setClfSupletorio(null);
									item.setClfParamRecuperacion1(null);
									item.setClfParamRecuperacion2(null);
									item.setClfPromedioAsistencia(promedioAsistencia.setScale(0, RoundingMode.HALF_UP).floatValue());
									item.setClfNotaFinalSemestre(notaSuma.setScale(2, RoundingMode.HALF_UP).floatValue());
									PreparedStatement pstmt = null;
									StringBuilder sbSql = new StringBuilder();
									sbSql.append(" update Record_estudiante set rces_estado = ");sbSql.append(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
									sbSql.append(" where rces_id = ? ");
									pstmt = con.prepareStatement(sbSql.toString());
									pstmt.setInt(1, item.getRecordEstudiante().getRcesId());
									pstmt.executeUpdate();
									try {
										pstmt.close();
									} catch (Exception e1) {
									}
							}
						}
					}
					
				}else{
					item.setClfPromedioNotas((notaSuma.setScale(2, RoundingMode.HALF_UP)).floatValue());
					item.setClfNotaFinalSemestre((notaSuma.setScale(0, RoundingMode.HALF_UP)).floatValue());
					PreparedStatement pstmt = null;
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" update Record_estudiante set rces_estado = ");sbSql.append(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					sbSql.append(" where rces_id = ? ");
					pstmt = con.prepareStatement(sbSql.toString());
					pstmt.setInt(1, item.getRecordEstudiante().getRcesId());
					pstmt.executeUpdate();
					try {
						pstmt.close();
					} catch (Exception e) {
					}
					item.setClfSupletorio(null);
					item.setClfParamRecuperacion1(null);
					item.setClfParamRecuperacion2(null);
				}
				
			}
		
			PreparedStatement pstmt1 = null;
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" update calificacion set clf_nota_final_semestre="
					+item.getClfNotaFinalSemestre()+ ",clf_supletorio="+item.getClfSupletorio()
					+",clf_param_recuperacion1="+item.getClfParamRecuperacion1()+",clf_param_recuperacion2="+item.getClfParamRecuperacion2()
					+" ,clf_estado=-10 where clf_id= ");sbSql.append(item.getClfId());
			pstmt1 = con.prepareStatement(sbSql.toString());
			pstmt1.executeUpdate();
			if(item.getClfNotaFinalSemestre()>=28){
				PreparedStatement pstmt2 = null;
				StringBuilder sbSql2 = new StringBuilder();
				sbSql2.append(" update Record_estudiante set rces_estado = ");sbSql2.append(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				sbSql2.append(" where rces_id = ? ");
				pstmt2 = con.prepareStatement(sbSql2.toString());
				pstmt2.setInt(1, item.getRecordEstudiante().getRcesId());
				pstmt2.executeUpdate();
				try {
					pstmt2.close();
				} catch (Exception e) {
				}
			}
			try {
				pstmt1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		BigDecimal itemCost = BigDecimal.ZERO;
		itemCost = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje))
				.divide(new BigDecimal(asitenciaDoc), 0, RoundingMode.HALF_UP);
		return itemCost;
	}
	
}
