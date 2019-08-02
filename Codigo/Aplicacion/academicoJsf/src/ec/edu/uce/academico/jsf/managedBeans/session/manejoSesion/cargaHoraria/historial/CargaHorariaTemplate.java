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
10-JUN-2019		    	Freddy Guzmán 			      		Carga Horaria Template
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial;

import java.io.Serializable;

import ec.edu.uce.academico.ejb.utilidades.constantes.PuestoEnum;

/**
 * Clase (managed bean) CargaHorariaTemplate.
 * Managed Bean que permite generar los reportes de la carga horaria de los docentes.
 * @author fgguzman
 * @version 1.0
 */
public class CargaHorariaTemplate implements Serializable {

	private static final long serialVersionUID = -7885812696353297407L;
	
	// ------> ASIGNACIÓN MAXIMA HORAS - segun instructivo agosto 2018
	//---> TIEMPO COMPLETO 
	// NOMBRAMIENTOS
	protected final int RLLB_TMCM_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE = 20;
	protected final int RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE = 4;
	protected final int RLLB_TMCM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE = 4;
	protected final int RLLB_TMCM_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE = 3;
	protected final int RLLB_TMCM_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE = 3;
	// CONTRATOS
	protected final int RLLB_TMCM_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE = 24;
	protected final int RLLB_TMCM_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE = 4;
	protected final int RLLB_TMCM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE = 4;
	protected final int RLLB_TMCM_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE = 3;
	protected final int RLLB_TMCM_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE = 3;
	
	//---> MEDIO TIEMPO 
	// NOMBRAMIENTOS
	protected final int RLLB_MDTM_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE = 14;
	protected final int RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE = 2;
	protected final int RLLB_MDTM_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE = 2;
	protected final int RLLB_MDTM_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE = 2;
	protected final int RLLB_MDTM_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE = 2;
	// CONTRATOS
	protected final int RLLB_MDTM_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE = 16;
	protected final int RLLB_MDTM_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE = 2;
	protected final int RLLB_MDTM_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE = 2;
	protected final int RLLB_MDTM_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE = 2;
	protected final int RLLB_MDTM_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE = 2;

	//---> TIEMPO PARCIAL
	// NOMBRAMIENTOS
	protected final int RLLB_TMPR_NOMBRAMIENTO_MAX_IMPARTICION_CLASES_2018_2019_VALUE = 8;
	protected final int RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_CLASES_2018_2019_VALUE = 1;
	protected final int RLLB_TMPR_NOMBRAMIENTO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE = 1;
	protected final int RLLB_TMPR_NOMBRAMIENTO_MAX_TUTORIAS_2018_2019_VALUE = 1;
	protected final int RLLB_TMPR_NOMBRAMIENTO_MAX_EXPERIMENTACION_2018_2019_VALUE = 1;
	// CONTRATOS
	protected final int RLLB_TMPR_CONTRATO_MAX_IMPARTICION_CLASES_2018_2019_VALUE = 10;
	protected final int RLLB_TMPR_CONTRATO_MAX_PREPARACION_CLASES_2018_2019_VALUE = 1;
	protected final int RLLB_TMPR_CONTRATO_MAX_PREPARACION_EXAMENES_2018_2019_VALUE = 1;
	protected final int RLLB_TMPR_CONTRATO_MAX_TUTORIAS_2018_2019_VALUE = 1;
	protected final int RLLB_TMPR_CONTRATO_MAX_EXPERIMENTACION_2018_2019_VALUE = 1;

	//TIPOS DE CARGA HORARIA
	protected final int TICAHR_INVESTIGACION_AVANZADA_VALUE = 3;
	protected final int TICAHR_PROYECTO_SEMILLA_VALUE = 4;
	protected final int TICAHR_VINCULACION_SOCIEDAD_VALUE = 5;
	protected final int TICAHR_TITULACION_VALUE = 6;
	protected final int TICAHR_EXAMEN_COMPLEXIVO_VALUE = 7;
	protected final int TICAHR_COORDINADORES_VALUE = 8;
	protected final int TICAHR_DOCTORADOS_VALUE = 9;
	protected final int TICAHR_DIRECCION_Y_GESTION_VALUE = 10;
	protected final int TICAHR_MIEMBRO_CONSEJO_VALUE = 11;
	protected final int TICAHR_COMITE_SUBCOMITE_VALUE = 12;
	protected final int TICAHR_ASEGURAMIENTO_CALIDAD_VALUE = 13;
	protected final int TICAHR_POSTGRADO_VALUE = 16;
	protected final int TICAHR_DIRECCION_VALUE = 18;
//	protected static final int TICAHR_MIEMBROS_GESTION_VALUE = 19;

	// FUNCIONES CARGA HORARIA
	protected final int FNCAHR_ACTIVIDAD_SUPERVISOR_CLINICAS_VALUE = 30;
	protected final int FNCAHR_COORDINADOR_DE_TITULACION_VALUE  = 45;
	protected final int FNCAHR_MIEMBRO_DE_GESTION_VAP_VALUE = 75;
	protected final int FNCAHR_COORDINADOR_DE_PROGRAMA_DE_POSGRADO_VALUE  = 76;
	protected final int FNCAHR_COORDINADOR_DE_SEGUIMIENTO_A_GRADUADOS_VALUE  = 77;
	protected final int FNCAHR_RESPONSABLE_DE_GESTION_DE_MODALIDAD_A_DISTANCIA_VALUE  = 78;
	protected final int FNCAHR_CONSEJO_AUTORIZADAS_VAP_VALUE = 79;
	protected final int FNCAHR_COORDINADOR_DE_CARRERA_A_COMISION_DE_FACULTAD_O_CALIDAD_VALUE  = 80;
	protected final int FNCAHR_COORDINADOR_DE_FACULTAD_DE_VINCULACION_O_CALIDAD_VALUE  = 81;
	protected final int FNCAHR_COORDINADOR_DE_AREA_ASIGNATURA_SEMESTRE_VALUE  = 82;
	protected final int FNCAHR_COORDINADOR_DE_DISENO_DE_CARRERAS_Y_PROGRAMAS_DE_POSGRADO_VALUE  = 84;
	protected final int FNCAHR_PROYECTOS_Y_CONVENIOS_CON_COOPERACION_INTERINSTITUCIONAL = 91; // INVESTIGACION AVANZADA
	protected final int FNCAHR_MIEMBROS_COMITE_ETICA_EN_INVESTIGACION_DE_FACULTADES_VALUE = 94;
	protected final int FNCAHR_OTRAS_ACTIVIDADES_INVESTIGACION_VALUE = 95;
	
	public static String establecerCategoria(int categoria, int nivelRangoGradual){
		String retorno = "";
		
		if (PuestoEnum.PST_CATEGORIA_DOCENTE_PRINCIPAL.getValue() == categoria) {
			retorno = PuestoEnum.PST_CATEGORIA_DOCENTE_PRINCIPAL.getLabel() + " NIVEL " + nivelRangoGradual;
		}else if (PuestoEnum.PST_CATEGORIA_DOCENTE_AGREGADO.getValue() == categoria) {
			retorno = PuestoEnum.PST_CATEGORIA_DOCENTE_AGREGADO.getLabel() + " NIVEL " + nivelRangoGradual;
		}else if (PuestoEnum.PST_CATEGORIA_DOCENTE_AUXILIAR.getValue() == categoria) {
			retorno = PuestoEnum.PST_CATEGORIA_DOCENTE_AUXILIAR.getLabel() + " NIVEL " + nivelRangoGradual;
		}else {
			retorno = PuestoEnum.PST_CATEGORIA_DOCENTE_OCASIONAL.getLabel();
		}
		
		return retorno;
	}
}
