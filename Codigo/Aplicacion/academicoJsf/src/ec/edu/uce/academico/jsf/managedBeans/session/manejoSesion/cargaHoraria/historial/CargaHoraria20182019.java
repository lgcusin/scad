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
   
 ARCHIVO:     CargaHorariaTemplateForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para el reporte de horario por estudiante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
10-DIC-2018		    	Freddy Guzmán 			      		Carga Horaria Template
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ec.edu.uce.academico.ejb.dtos.CargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RelacionLaboralConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TiempoDedicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * Clase (managed bean) ReporteCargaHorariaForm.
 * Managed Bean que permite generar los reportes de la carga horaria de los docentes.
 * @author fgguzman
 * @version 1.0
 */
public class CargaHoraria20182019 extends CargaHorariaTemplate implements Serializable {
	private static final long serialVersionUID = -5422984994284974811L;

	/**
	 * Método que permite establecer las horas para cada componente de la carga horaria - imparticion clases.
	 * - Mostrar solamente las principales con sumatorias de compartidas.
	 * - Modulares contar con las principales
	 * @param docente - docente con relacion laboral y tiempo de dedicacion
	 * @param listImparticionClases - consolidado compartidas, modulares
	 * @return parte inicial componentes.
	 */
	public Integer[] cargarDatosImparticionClases(PersonaDto docente, List<CargaHorariaDto> listImparticionClases){
		Integer [] retorno = new Integer[7];
		Arrays.fill(retorno, new Integer(0));

		if (!listImparticionClases.isEmpty()) {
			retorno[0] = establecerHorasImparticionClases(docente, listImparticionClases);
			retorno[1] = establecerHorasPreparacionClases(docente, listImparticionClases);
			retorno[2] = establecerHorasPreparacionExamenes(docente, listImparticionClases);
			retorno[3] = establecerHorasTutoriasAcademicas(docente, listImparticionClases);
			retorno[4] = establecerHorasPracticasExperimentacion(docente, listImparticionClases);
			retorno[5] = establecerNumeroParalelos(listImparticionClases);
			retorno[6] = establecerNumeroMatriculados(listImparticionClases);
		}

		return retorno;
	}

	/**
	 * Método que permite establecer las horas para cada componente de la carga horaria - funciones
	 * @param listFuncionesCargaHoraria - todas las funciones del periodo.
	 * @return parte final componentes
	 */
	public Integer[] cargarDatosFuncionesCargaHoraria(List<CargaHorariaDto> listFuncionesCargaHoraria) {
		Integer [] retorno = new Integer[12];
		Arrays.fill(retorno, new Integer(0));

		if (!listFuncionesCargaHoraria.isEmpty()) {
			int clinicasPasantias = 0;
			int titulacion = 0;
			int vinculacionSociedad = 0;
			int proyectoSemilla = 0;
			int investigacionAvanzada = 0;
			int actividadesInvestigacion = 0;
			int proyectosInvestigacion = 0;
			int direccion = 0;
			int gestion = 0;//NO APLICA
			int representaciones = 0;
			int actividadesVAP= 0;
			int coordinadores = 0;

			for (CargaHorariaDto item : listFuncionesCargaHoraria) {

				if (item.getFncahrId() == FNCAHR_ACTIVIDAD_SUPERVISOR_CLINICAS_VALUE) {
					clinicasPasantias = clinicasPasantias + item.getCahrNumHoras();
				}else if (item.getFncahrId() == FNCAHR_OTRAS_ACTIVIDADES_INVESTIGACION_VALUE ||
					      item.getFncahrId() == FNCAHR_MIEMBROS_COMITE_ETICA_EN_INVESTIGACION_DE_FACULTADES_VALUE) {
					actividadesInvestigacion = actividadesInvestigacion + item.getCahrNumHoras();	
				}else if (item.getTicahrId() == TICAHR_INVESTIGACION_AVANZADA_VALUE ) {
					if (item.getFncahrId() != FNCAHR_PROYECTOS_Y_CONVENIOS_CON_COOPERACION_INTERINSTITUCIONAL) {
						investigacionAvanzada =  investigacionAvanzada + item.getCahrNumHoras();	
					}
				}else if (item.getTicahrId() == TICAHR_PROYECTO_SEMILLA_VALUE) {
					proyectoSemilla = proyectoSemilla + item.getCahrNumHoras();
				}else if (item.getTicahrId() == TICAHR_VINCULACION_SOCIEDAD_VALUE ) {
					vinculacionSociedad = vinculacionSociedad + item.getCahrNumHoras();
				}else if (item.getTicahrId() == TICAHR_TITULACION_VALUE ) {
					titulacion = titulacion + item.getCahrNumHoras();
				}else if (item.getTicahrId() == TICAHR_EXAMEN_COMPLEXIVO_VALUE ) {
					// examenComplexivo = examenComplexivo + item.getCahrNumHoras();
				}else if (item.getFncahrId() == FNCAHR_PROYECTOS_Y_CONVENIOS_CON_COOPERACION_INTERINSTITUCIONAL) {
					proyectosInvestigacion = proyectosInvestigacion + item.getCahrNumHoras();	
				}else if (item.getTicahrId() == TICAHR_MIEMBRO_CONSEJO_VALUE ||
					      item.getTicahrId() == TICAHR_COMITE_SUBCOMITE_VALUE){
					representaciones = representaciones + item.getCahrNumHoras();
				}else if (item.getFncahrId() == FNCAHR_CONSEJO_AUTORIZADAS_VAP_VALUE ||
					      item.getFncahrId() == FNCAHR_MIEMBRO_DE_GESTION_VAP_VALUE){
					actividadesVAP = actividadesVAP + item.getCahrNumHoras();	
				}else if (item.getFncahrId() == FNCAHR_COORDINADOR_DE_TITULACION_VALUE ||
						  item.getFncahrId() == FNCAHR_COORDINADOR_DE_PROGRAMA_DE_POSGRADO_VALUE ||
						  item.getFncahrId() == FNCAHR_COORDINADOR_DE_SEGUIMIENTO_A_GRADUADOS_VALUE ||
						  item.getFncahrId() == FNCAHR_RESPONSABLE_DE_GESTION_DE_MODALIDAD_A_DISTANCIA_VALUE ||
						  item.getFncahrId() == FNCAHR_COORDINADOR_DE_CARRERA_A_COMISION_DE_FACULTAD_O_CALIDAD_VALUE ||
						  item.getFncahrId() == FNCAHR_COORDINADOR_DE_FACULTAD_DE_VINCULACION_O_CALIDAD_VALUE ||
						  item.getFncahrId() == FNCAHR_COORDINADOR_DE_AREA_ASIGNATURA_SEMESTRE_VALUE ||
						  item.getFncahrId() == FNCAHR_COORDINADOR_DE_DISENO_DE_CARRERAS_Y_PROGRAMAS_DE_POSGRADO_VALUE ) {
					coordinadores = coordinadores + item.getCahrNumHoras();
				}else if (item.getTicahrId() == TICAHR_DIRECCION_VALUE) {
					direccion = direccion + item.getCahrNumHoras();
				}

			}

			retorno[0] = clinicasPasantias;
			retorno[1] = vinculacionSociedad;
			retorno[2] = titulacion;
			retorno[3] = investigacionAvanzada;
			retorno[4] = proyectoSemilla;
			retorno[5] = actividadesInvestigacion;
			retorno[6] = proyectosInvestigacion;
			retorno[7] = direccion;
			retorno[8] = gestion;
			retorno[9] = representaciones;
			retorno[10] = actividadesVAP;
			retorno[11] = coordinadores;
		}

		return retorno;
	}
	
	/**
	 * Método que permite consolidar la informacion de las asignaturas con horario compartido.
	 * @param listHorarioGeneral - horarios con datos precargados de modulares.
	 * @return asignaturas principales por paralelo.
	 */
	public List<CargaHorariaDto> consolidarAsignaturasPrincipales(List<CargaHorariaDto> listHorarioGeneral) {
		List<CargaHorariaDto> retorno = new ArrayList<>();
		
		for (CargaHorariaDto item : listHorarioGeneral) {
			if (item.getCahrMateriaDto().getHracMlcrprIdComp() == 0) {
				retorno.add(item);	
			}
		}
		
		for (CargaHorariaDto item : retorno) {
			item.getCahrParaleloDto().setMlcrprMatriculados(contarMatriculadosParaleloPrincipal(item.getCahrMateriaDto().getHracMlcrprId(), listHorarioGeneral));
		}
		
		return retorno;
	}
	
	private Integer contarMatriculadosParaleloPrincipal(Integer mlcrprId, List<CargaHorariaDto> imparitcionClases) {
		Integer retorno = new Integer(0);

		boolean esCompartido = false;
		for (CargaHorariaDto cargaHoraria : imparitcionClases) {

			if (cargaHoraria.getCahrMateriaDto().getHracMlcrprIdComp() != null  && cargaHoraria.getCahrMateriaDto().getHracMlcrprIdComp().equals(mlcrprId)) {
				retorno = retorno + cargaHoraria.getCahrParaleloDto().getMlcrprMatriculados();
				esCompartido = true;
			}
			
			if (cargaHoraria.getCahrMateriaDto().getHracMlcrprId().equals(mlcrprId)) {
				retorno = retorno + cargaHoraria.getCahrParaleloDto().getMlcrprMatriculados();
			}
			
			if (esCompartido) {
				for (CargaHorariaDto item : imparitcionClases) {
					if (item.getCahrMateriaDto().getHracMlcrprId().equals(mlcrprId)) {
						item.getCahrParaleloDto().setPrlDescripcion(item.getCahrParaleloDto().getPrlDescripcion()+"(*)");
						esCompartido = false;
					}
				}
			}

		}

		return retorno;
	}
	
	/**
	 * Método que permite establecer la dependencia a la que pertenece el docente.
	 * @return dependencia en la que mas carga horaria tiene.
	 */
	public DependenciaDto establecerDependencia(List<CargaHorariaDto> materiasEncontradas) {
		DependenciaDto retorno = new DependenciaDto();
		
		if (!materiasEncontradas.isEmpty()) {
			Map<DependenciaDto, Integer> ocurrencias = materiasEncontradas.stream().collect(Collectors.groupingBy(CargaHorariaDto::getCarhDependenciaDto, Collectors.summingInt(CargaHorariaDto::getCahrNumHoras)));
			if(!ocurrencias.isEmpty()){
				int maxvalue = Collections.max(ocurrencias.values());
				for (Entry<DependenciaDto, Integer> entry : ocurrencias.entrySet()) {  
					if (entry.getValue()==maxvalue) {
						retorno = entry.getKey();
						break;
					}
				}   
			}
		}
		
		return retorno;
	}
	
	
	/**
	 * Método que permite sumar las horas de imparticion de clases.
	 * - CONDICIONES POR PARALELO
	 * Solo horas de docencia rediseño + Creditos en diseño. 
	 * Limite al global en funcion al tiempo de dedicacion. 
	 */
	private int establecerHorasImparticionClases(PersonaDto docente, List<CargaHorariaDto> listImparticionClases){
		int retorno = 0; 
		
		int horas = listImparticionClases.stream().mapToInt(it-> it.getCahrMateriaDto().getMtrHorasPorSemana()).sum();
		if(docente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_NOMBRAMIENTO_VALUE.intValue()){

			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = horas > RLLB_TMCM_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE ? RLLB_TMCM_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = horas > RLLB_MDTM_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE ? RLLB_MDTM_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE:horas;
				break;
			}

		}else{ // CONTRATOS
			
			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = horas > RLLB_TMCM_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE ? RLLB_TMCM_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = horas > RLLB_MDTM_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE ? RLLB_MDTM_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE ? RLLB_TMPR_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE:horas;
				break;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite sumar las horas de preparacion de clases.
	 * - CONDICIONES POR ASIGNATURA
	 * Suma horas docencia + creditos en diseño
	 * Limite al global en funcion al tiempo de dedicacion. 
	 */
	private int establecerHorasPreparacionClases(PersonaDto docente, List<CargaHorariaDto> listImparticionClases){
		int retorno = 0; 

		Map<String, CargaHorariaDto> mapMaterias =  new HashMap<String, CargaHorariaDto>();
		for (CargaHorariaDto item : listImparticionClases) {
			mapMaterias.put(GeneralesUtilidades.quitarEspaciosEnBlanco(item.getCahrMateriaDto().getMtrDescripcion()), item);
		}

		int horas = mapMaterias.entrySet().stream().mapToInt(it-> it.getValue().getCahrMateriaDto().getMtrHorasPorSemana()).sum();
		if(docente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_NOMBRAMIENTO_VALUE.intValue()){

			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE ? RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE ? RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE:horas;
				break;
			}

		}else{ // CONTRATOS
			
			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMCM_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE ? RLLB_TMCM_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_MDTM_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE ? RLLB_MDTM_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(2)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE ? RLLB_TMPR_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE:horas;
				break;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite sumar las horas de preparacion de examenes.
	 * - CONDICIONES POR ESTUDIANTES
	 * Minimo una hora.
	 * Aplica 2 horas por cada 40 estudiantes. 
	 * Aplica inmediato superior 2.5 -> 3 
	 * Limite al global en funcion al tiempo de dedicacion. 
	 */
	private int establecerHorasPreparacionExamenes(PersonaDto docente, List<CargaHorariaDto> listImparticionClases){
		
		int retorno = 1; 

		int horas = listImparticionClases.stream().mapToInt(it-> it.getCahrParaleloDto().getMlcrprMatriculados()).sum();
		if(docente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_NOMBRAMIENTO_VALUE.intValue()){

			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue())) == 0 ? retorno : Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue())) == 0 ? retorno : Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
//				retorno = horas > RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:horas;
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue())) == 0 ? retorno : Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			}

		}else{ // CONTRATOS
			
			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMCM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMCM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue())) == 0 ? 1 : Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_TMCM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMCM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_MDTM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_MDTM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue())) == 0 ? 1 : Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue() > RLLB_MDTM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_MDTM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:Math.round(new BigDecimal(String.valueOf(horas)).divide(new BigDecimal(String.valueOf(20)),new MathContext(3, RoundingMode.HALF_UP)).doubleValue()));
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE ? RLLB_TMPR_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE:horas;
				break;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite sumar las horas de tutorias academicas.
	 * - CONDICIONES POR ASIGNATURA
	 * Solo aplica a asignaturas de diseño.
	 */
	private int establecerHorasTutoriasAcademicas(PersonaDto docente, List<CargaHorariaDto> listImparticionClases){
		int retorno = 0; 

		Map<String, CargaHorariaDto> mapMaterias =  new HashMap<String, CargaHorariaDto>();
		for (CargaHorariaDto item : listImparticionClases) {
			if (item.getCahrCarreraDto().getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
				mapMaterias.put(GeneralesUtilidades.quitarEspaciosEnBlanco(item.getCahrMateriaDto().getMtrDescripcion()), item);
			}
		}

		int horas = mapMaterias.size();
		if(docente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_NOMBRAMIENTO_VALUE.intValue()){

			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = horas > RLLB_TMCM_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE ? RLLB_TMCM_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = horas > RLLB_MDTM_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE ? RLLB_MDTM_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE:horas;
				break;
			}

		}else{ // CONTRATOS
			
			switch (docente.getTmddId()) {
			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
				retorno = horas > RLLB_TMCM_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE ? RLLB_TMCM_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
				retorno = horas > RLLB_MDTM_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE ? RLLB_MDTM_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE:horas;
				break;
			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
				retorno = horas > RLLB_TMPR_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE ? RLLB_TMPR_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE:horas;
				break;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite sumar las horas de practicas de experimentación.
	 * - CONDICIONES POR PARALELO
	 * PAE aplica solo a rediseños en paralelos principales, no compartidos.
	 */
	private int establecerHorasPracticasExperimentacion(PersonaDto docente, List<CargaHorariaDto> listImparticionClases){
		int retorno = 0; 

//		int horas = listImparticionClases.stream().mapToInt(it-> it.getCahrMateriaDto().getMtrHorasPorSemanaPAE()).sum();
//		if(docente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_NOMBRAMIENTO_VALUE.intValue()){
//
//			switch (docente.getTmddId()) {
//			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
//				retorno = horas > RLLB_TMCM_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE ? RLLB_TMCM_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE:horas;
//				break;
//			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
//				retorno = horas > RLLB_MDTM_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE ? RLLB_MDTM_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE:horas;
//				break;
//			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
//				retorno = horas > RLLB_TMPR_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE ? RLLB_TMPR_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE:horas;
//				break;
//			}
//
//		}else{ // CONTRATOS
//			
//			switch (docente.getTmddId()) {
//			case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
//				retorno = horas > RLLB_TMCM_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE ? RLLB_TMCM_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE:horas;
//				break;
//			case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
//				retorno = horas > RLLB_MDTM_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE ? RLLB_MDTM_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE:horas;
//				break;
//			case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
//				retorno = horas > RLLB_TMPR_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE ? RLLB_TMPR_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE:horas;
//				break;
//			}
//			
//		}

		return retorno;
	}
	
	/**
	 * Método que permite establecer la cantidad e alumnos que se encuentran matriculados en los paralelos.
	 * @param listImparticionClases - horario consolidado.
	 * @return cantidad de estudiantes matriculados.
	 */
	private Integer establecerNumeroMatriculados(List<CargaHorariaDto> listImparticionClases) {
		return listImparticionClases.stream().mapToInt(it-> it.getCahrParaleloDto().getMlcrprMatriculados()).sum();
	}

	/**
	 * Metodo que permite establecer el numero de paralelos que tiene a cargo el docente.
	 * - solo aplica principales.
	 * @param listImparticionClases - horario consolidado.
	 * @return cantidad de paralelos.
	 */
	private Integer establecerNumeroParalelos(List<CargaHorariaDto> listImparticionClases) {
		return listImparticionClases.size();
	}
	
}
