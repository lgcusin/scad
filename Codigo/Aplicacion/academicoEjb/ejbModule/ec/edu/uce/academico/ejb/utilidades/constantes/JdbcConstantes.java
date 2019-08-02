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
   
 ARCHIVO:     JdbcConstantes.java	  
 DESCRIPCION: Clase de constantes de tablas y campos de la BD para el uso de JDBC.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20-03-2017			David Arellano				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;
import java.util.ResourceBundle;

public class JdbcConstantes {

	public static final String base = "desarrollo";
//	public static final String base = "produccion";
	
	//****************************************************************************
	//**************** ESQUEMA DESARROLLO*******************************************
	//****************************************************************************
	public static final String ESQUEMA_DESARROLLO;
	
	//****************************************************************************
	//**************** TABLA PLANIFICACION EVALUACION ****************************
	//****************************************************************************
	public static final String TABLA_HORARIO_FUNCION;
	public static final String HRFN_ID;
	public static final String HRFN_DESCRIPCION;
	public static final String HRFN_HORA_INICIO;
	public static final String HRFN_HORA_FIN;
	public static final String HRFN_ESTADO;
	public static final String HRFN_DIA;
	public static final String HRFN_HOCL_ID;
	public static final String HRFN_FUNCION;
	public static final String HRFN_ACTIVIDAD;
	public static final String HRFN_PRAC_ID;
	public static final String HRFN_DTPS_ID;
	
	//****************************************************************************
	//**************** TABLA PLANIFICACION EVALUACION ****************************
	//****************************************************************************
	public static final String TABLA_PLANIFICACION_EVALUACION;
	public static final String PLEV_ID;
	public static final String PLEV_FECHA_INICIO;
	public static final String PLEV_FECHA_FIN;
	public static final String PLEV_ESTADO;

	//****************************************************************************
	//**************** TABLA ACTIVIDAD_ESENCIAL*******************************************
	//****************************************************************************
	public static final String TABLA_ACTIVIDAD_ESENCIAL;
	public static final String ACES_ID;
	public static final String ACES_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA ACTIVIDAD_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_ACTIVIDAD_PUESTO;
	public static final String ACPS_ID;
	public static final String ACPS_ACES_ID;
	public static final String ACPS_PST_ID;

	//****************************************************************************
	//**************** TABLA APRUEBA_SOLICITUD*******************************************
	//****************************************************************************
	public static final String TABLA_APRUEBA_SOLICITUD;
	public static final String APSL_ID;
	public static final String APSL_PRTRCN_ID;
	public static final String APSL_FECHA_APROBACION;
	public static final String APSL_ESTADO;
	public static final String APSL_OBSERVACION;

	//****************************************************************************
	//**************** TABLA ARANCEL*******************************************
	//****************************************************************************
	public static final String TABLA_ARANCEL;
	public static final String ARN_ID;
	public static final String ARN_MDL_ID;
	public static final String ARN_TIGR_ID;
	public static final String ARN_DESCRIPCION;
	public static final String ARN_TIPO_MATRICULA;
	public static final String ARN_TIPO_NUM_MATRICULA;
	public static final String ARN_VALOR;
	public static final String ARN_TIPO;

	//****************************************************************************
	//**************** TABLA AREA_CONOCIMIENTO*******************************************
	//****************************************************************************
	public static final String TABLA_AREA_CONOCIMIENTO;
	public static final String ARCN_ID;
	public static final String ARCN_DESCRIPCION;
	public static final String ARCN_CODIGO;
	public static final String ARCN_JERARQUIA;
	public static final String ARCN_SUB_ID;

	//****************************************************************************
	//**************** TABLA ASIGNA_PRESUPUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_ASIGNA_PRESUPUESTO;
	public static final String ASPR_ID;
	public static final String ASPR_PRTRCN_ID;
	public static final String ASPR_PARTIDA_PRESUPUESTO;
	public static final String ASPR_ESTADO;
	public static final String ASPR_ARCHIVO_CERTIFICACION;

	//****************************************************************************
	//**************** TABLA AULA*******************************************
	//****************************************************************************
	public static final String TABLA_AULA;
	public static final String ALA_ID;
	public static final String ALA_EDF_ID;
	public static final String ALA_DESCRIPCION;
	public static final String ALA_CODIGO;
	public static final String ALA_TIPO;
	public static final String ALA_CAPACIDAD;
	public static final String ALA_PISO;
	public static final String ALA_ESTADO;
	
	//****************************************************************************
	//**************** TABLA CALIFICACION*******************************************
	//****************************************************************************
	public static final String TABLA_CALIFICACION;
	public static final String CLF_NOTA_FINAL_SEMESTRE;
	public static final String CLF_ESTADO;
	public static final String CLF_ASISTENCIA_DOCENTE1;
	public static final String CLF_ASISTENCIA_DOCENTE2;
	public static final String CLF_SUMA_P1_P2;
	public static final String CLF_ASISTENCIA_TOTAL_DOC;
	public static final String CLF_ID;
	public static final String CLF_RCES_ID;
	public static final String CLF_NOTA1;
	public static final String CLF_NOTA2;
	public static final String CLF_EXAMEN;
	public static final String CLF_SUPLETORIO;
	public static final String CLF_ASISTENCIA1;
	public static final String CLF_ASISTENCIA2;
	public static final String CLF_TOTAL_ASISTENCIA1;
	public static final String CLF_TOTAL_ASISTENCIA2;
	public static final String CLF_PROMEDIO_NOTAS;
	public static final String CLF_PROMEDIO_ASISTENCIA;
	public static final String CLF_ASISTENCIA_TOTAL;
	public static final String CLF_PARAM_RECUPERACION1;
	public static final String CLF_PARAM_RECUPERACION2;
	
	//****************************************************************************
	//**************** TABLA CALIFICACION_MODULO *********************************
	//****************************************************************************
	public static final String TABLA_CALIFICACION_MODULO;
	public static final String CLMD_NOTA_FINAL_SEMESTRE;
	public static final String CLMD_ESTADO;
	public static final String CLMD_ASISTENCIA_DOCENTE1;
	public static final String CLMD_ASISTENCIA_DOCENTE2;
	public static final String CLMD_SUMA_P1_P2;
	public static final String CLMD_ASISTENCIA_TOTAL_DOC;
	public static final String CLMD_ID;
	public static final String CLMD_RCES_ID;
	public static final String CLMD_NOTA1;
	public static final String CLMD_NOTA2;
	public static final String CLMD_EXAMEN;
	public static final String CLMD_SUPLETORIO;
	public static final String CLMD_ASISTENCIA1;
	public static final String CLMD_ASISTENCIA2;
	public static final String CLMD_TOTAL_ASISTENCIA1;
	public static final String CLMD_TOTAL_ASISTENCIA2;
	public static final String CLMD_PROMEDIO_NOTAS;
	public static final String CLMD_PROMEDIO_ASISTENCIA;
	public static final String CLMD_ASISTENCIA_TOTAL;
	public static final String CLMD_PARAM_RECUPERACION1;
	public static final String CLMD_PARAM_RECUPERACION2;
	public static final String MLCRPR_ID_MODULO;

	//****************************************************************************
	//**************** TABLA CAMPO_FORMACION*******************************************
	//****************************************************************************
	public static final String TABLA_CAMPO_FORMACION;
	public static final String CMFR_ID;
	public static final String CMFR_TIPO;
	public static final String CMFR_DESCRIPCION;
	public static final String CMFR_ESTADO;

	//****************************************************************************
	//**************** TABLA CANDIDATO_CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_CANDIDATO_CONTRATO;
	public static final String CNCN_ID;
	public static final String CNCN_PRS_ID;
	public static final String CNCN_PRTRCN_ID;
	public static final String CNCN_ESTADO;
	public static final String CNCN_OBS_RECTORADO;
	public static final String CNCN_TERCER_NIVEL;
	public static final String CNCN_CUARTO_NIVEL;
	public static final String CNCN_IMPEDIMENTO;
	public static final String CNCN_OBS_IMPEDIMENTO;
	public static final String CNCN_ARCHIVO_IMPEDIMENTO;
	public static final String CNCN_LABORA_OTRA_INST;
	public static final String CNCN_OBS_LABORA_OTRA_INST;
	public static final String CNCN_ARCHIVO_LABORA_INST;
	

	//****************************************************************************
	//**************** TABLA CARGA_HORARIA*******************************************
	//****************************************************************************
	public static final String TABLA_CARGA_HORARIA;
	public static final String CRHR_ID;
	public static final String CRHR_TIFNCRHR_ID;
	public static final String CRHR_PRAC_ID;
	public static final String CRHR_DTPS_ID;
	public static final String CRHR_MLCRPR_ID;
	public static final String CRHR_OBSERVACION;
	public static final String CRHR_ESTADO;
	public static final String CRHR_NUM_HORAS;
	public static final String CRHR_ESTADO_ESTADO;
	public static final String CRHR_CARRERA_ID;
	//****************************************************************************
	//**************** TABLA CARGA_HORARIA_CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_CARGA_HORARIA_CONTRATO;
	public static final String CRHRCN_ID;
	public static final String CRHRCN_PRTRCN_ID;
	public static final String CRHRCN_HORA_CLASE;
	public static final String CRHRCN_HORA_VINCULACION;
	public static final String CRHRCN_TUTORIA;
	public static final String CRHRCN_PREPARACION_CLASE;

	//****************************************************************************
	//**************** TABLA CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_CARRERA;
	public static final String CRR_TIPO;
	public static final String CRR_CUPO;
	public static final String CRR_ARANCEL;
	public static final String CRR_ESPE_CODIGO;
	public static final String CRR_ID;
	public static final String CRR_DPN_ID;
	public static final String CRR_DESCRIPCION;
	public static final String CRR_COD_SNIESE;
	public static final String CRR_DETALLE;
	public static final String CRR_FECHA_CREACION;
	public static final String CRR_RESOLUCION;
	public static final String CRR_PROCESO;
	public static final String CRR_REAJUSTE_MATRICULA;
	public static final String CRR_ID_ARANCEL;
	public static final String CRR_TIPO_EVALUACION;
	//****************************************************************************
	//**************** TABLA CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_CARRERA_AREA;
	public static final String CRAR_ID;
	public static final String CRAR_CRR_ID;
	public static final String CRAR_AREA_ID;
	
	// ****************************************************************************
	// **************** TABLA CAUSAL***********************************************
	// ****************************************************************************
	public static final String TABLA_CAUSAL;
	public static final String CSL_ID;
	public static final String CSL_TICS_ID;
	public static final String CSL_DESCRIPCION;
	public static final String CSL_CODIGO;
	public static final String CSL_ESTADO;
	
	
	//****************************************************************************
	//**************** TABLA CAUSAL_DETALLE_MATRICULA*******************************************
	//****************************************************************************
		public static final String TABLA_CAUSAL_DETALLE_MATRICULA;
		public static final String CSDTMT_ID;
		public static final String CSDTMT_CSL_ID;
		public static final String CSDTMT_DTMT_ID;
	
	//****************************************************************************
	//**************** TABLA COMPETENCIA*******************************************
	//****************************************************************************
	public static final String TABLA_COMPETENCIA;
	public static final String CMP_ID;
	public static final String CMP_DENOMINACION;
	public static final String CMP_DEFINICION;
	public static final String CMP_TIPO;

	//****************************************************************************
	//**************** TABLA COMPETENCIA_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_COMPETENCIA_PUESTO;
	public static final String CMPS_ID;
	public static final String CMPS_PST_ID;
	public static final String CMPS_NVCM_ID;

	//****************************************************************************
	//**************** TABLA COMPROBANTE_PAGO*******************************************
	//****************************************************************************
	public static final String TABLA_COMPROBANTE_PAGO;
	public static final String CMPA_ID;
	public static final String CMPA_FCMT_ID;
	public static final String CMPA_CODIGO;
	public static final String CMPA_ESTADO;
	public static final String CMPA_VALOR_TOTAL;
	public static final String CMPA_FECHA_EMISION;
	public static final String CMPA_FECHA_CADUCIDAD;
	public static final String CMPA_TIPO;
	public static final String CMPA_FECHA_PAGO;
	
	
	public static final String CMPA_NUM_COMPROBANTE;
	public static final String CMPA_NUM_COMP_SECUENCIAL;
	public static final String CMPA_DESCRIPCION;
	public static final String CMPA_TOTAL_PAGO;
	public static final String CMPA_TOTAL_FACULTAD;
	public static final String CMPA_PROC_SAU;
	public static final String CMPA_TIPO_UNIDAD;
	public static final String CMPA_VALOR_PAGADO;
	public static final String CMPA_CANTIDAD;
	public static final String CMPA_ID_ARANCEL;
	public static final String CMPA_PAI_CODIGO;
	public static final String CMPA_APLICA_GRATUIDAD;
	public static final String CMPA_MATR_TIPO;
	public static final String CMPA_MODALIDAD;
	public static final String CMPA_ESPE_CODIGO;
	public static final String CMPA_FECHA_CADUCA;
	public static final String CMPA_FECHA_ENVIO;
	public static final String CMPA_ESTADO_PAGO;
	
	//****************************************************************************
	//**************** TABLA CONFIGURACION_CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_CONFIGURACION_CARRERA;
	public static final String CNCR_ID;
	public static final String CNCR_CRR_ID;
	public static final String CNCR_VGN_ID;
	public static final String CNCR_MDL_ID;
	public static final String CNCR_TTL_ID;
	public static final String CNCR_UBC_ID;
	public static final String CNCR_TISE_ID;
	public static final String CNCR_TIFR_ID;
	public static final String CNCR_DRC_ID;

	//****************************************************************************
	//**************** TABLA CONOCIMIENTO*******************************************
	//****************************************************************************
	public static final String TABLA_CONOCIMIENTO;
	public static final String CNC_ID;
	public static final String CNC_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA CONOCIMIENTO_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_CONOCIMIENTO_PUESTO;
	public static final String CNPS_ID;
	public static final String CNPS_PST_ID;
	public static final String CNPS_CNC_ID;

	//****************************************************************************
	//**************** TABLA CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_CONTRATO;
	public static final String CNT_ID;
	public static final String CNT_TICN_ID;
	public static final String CNT_TIFN_ID;
	
	// ****************************************************************************
	// **************** TABLA CONTROL_PROCESO**************************************
	// ****************************************************************************
	public static final String TABLA_CONTROL_PROCESO;
	public static final String CNPR_ID;
	public static final String CNPR_TIPR_ID;
	public static final String CNPR_USRO_ID;
	public static final String CNPR_DETALLE_PROCESO;
	public static final String CNPR_TIPO_ACCION;
	public static final String CNPR_FECHA_ACCION;
	public static final String CNPR_OBSERVACION_ACCION;

	//****************************************************************************
	//**************** TABLA CONVALIDACION*******************************************
	//****************************************************************************
	public static final String TABLA_CONVALIDACION;
	public static final String CNV_ID;
	public static final String CNV_MTR_ID;
	public static final String CNV_MLCRMT_ID;

	//****************************************************************************
	//**************** TABLA COREQUISITO*******************************************
	//****************************************************************************
	public static final String TABLA_COREQUISITO;
	public static final String CRQ_ID;
	public static final String CRQ_MTR_ID;
	public static final String CRQ_MTR_COREQUISITO_ID;
	public static final String CRQ_CODIGO;
	public static final String CRQ_DESCRIPCION;
	public static final String CRQ_ESTADO;

	//****************************************************************************
	//**************** TABLA CRONOGRAMA*******************************************
	//****************************************************************************
	public static final String TABLA_CRONOGRAMA;
	public static final String CRN_ID;
	public static final String CRN_PRAC_ID;
	public static final String CRN_TIPO;
	public static final String CRN_DESCRIPCION;
	public static final String CRN_ESTADO;

	//****************************************************************************
	//**************** TABLA CRONOGRAMA_PROCESO_FLUJO*******************************************
	//****************************************************************************
	public static final String TABLA_CRONOGRAMA_PROCESO_FLUJO;
	public static final String CRPRFL_PRFL_ID;
	public static final String CRPRFL_CRN_ID;
	public static final String CRPRFL_ID;
	public static final String CRPRFL_ORDINAL;

	//****************************************************************************
	//**************** TABLA DEPENDENCIA*******************************************
	//****************************************************************************
	public static final String TABLA_DEPENDENCIA;
	public static final String DPN_ID;
	public static final String DPN_SUB_ID;
	public static final String DPN_UBC_ID;
	public static final String DPN_DESCRIPCION;
	public static final String DPN_JERARQUIA;
	public static final String DPN_ESTADO;
	public static final String DPN_CAMPUS;
	public static final String DPN_COD_SORI;
	public static final String DPN_UEJ;

	//****************************************************************************
	//**************** TABLA DESTREZA*******************************************
	//****************************************************************************
	public static final String TABLA_DESTREZA;
	public static final String DST_ID;
	public static final String DST_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA DESTREZA_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_DESTREZA_PUESTO;
	public static final String DSPS_ID;
	public static final String DSPS_DST_ID;
	public static final String DSPS_PST_ID;

	//****************************************************************************
	//**************** TABLA DETALLE_CARGA_HORARIA*******************************************
	//****************************************************************************
	public static final String TABLA_DETALLE_CARGA_HORARIA;
	public static final String DTCRHR_CARRERA_ID;
	public static final String DTCRHR_ID;
	public static final String DTCRHR_CRHR_ID;
	public static final String DTCRHR_FECHA_INICIO;
	public static final String DTCRHR_FECHA_FIN;
	public static final String DTCRHR_NUM_PROYECTO;
	public static final String DTCRHR_AREA;
	public static final String DTCRHR_SUBAREA;
	public static final String DTCRHR_UNIDAD_ACADEMICA;
	public static final String DTCRHR_ASIGNATURA_CLINICAS;
	public static final String DTCRHR_NUM_ALUMNOS_CLINICAS;
	public static final String DTCRHR_UNIVERSIDAD_DOCTORADO;
	public static final String DTCRHR_TITULO_DOCTORADO;
	public static final String DTCRHR_NIVEL_PROYECTO1;
	public static final String DTCRHR_PROYECTO1;
	public static final String DTCRHR_NIVEL_PROYECTO2;
	public static final String DTCRHR_PROYECTO2;
	public static final String DTCRHR_NIVEL_PROYECTO3;
	public static final String DTCRHR_PROYECTO3;
	public static final String DTCRHR_NIVEL_PROYECTO4;
	public static final String DTCRHR_PROYECTO4;
	public static final String DTCRHR_NIVEL_PROYECTO5;
	public static final String DTCRHR_PROYECTO5;
	public static final String DTCRHR_NIVEL_PROYECTO6;
	public static final String DTCRHR_PROYECTO6;
	public static final String DTCRHR_NIVEL_PROYECTO7;
	public static final String DTCRHR_PROYECTO7;
	public static final String DTCRHR_NIVEL_PROYECTO8;
	public static final String DTCRHR_PROYECTO8;
	public static final String DTCRHR_PROYECTO1_HORAS;
	public static final String DTCRHR_PROYECTO2_HORAS;
	public static final String DTCRHR_PROYECTO3_HORAS;
	public static final String DTCRHR_PROYECTO4_HORAS;
	public static final String DTCRHR_PROYECTO5_HORAS;
	public static final String DTCRHR_PROYECTO6_HORAS;
	public static final String DTCRHR_PROYECTO7_HORAS;
	public static final String DTCRHR_PROYECTO8_HORAS;
	public static final String DTCRHR_ESTADO;
	public static final String DTCRHR_ESTADO_ELIMINACION;
	public static final String DTCRHR_NUM_HORAS;
	public static final String DTCRHR_NOMBRE_PROYECTO;
	public static final String DTCRHR_FUNCION;
	public static final String DTCRHR_NUM_ALUMNOS_TITULACION;

	//****************************************************************************
	//**************** TABLA DETALLE_MATRICULA*******************************************
	//****************************************************************************
	public static final String TABLA_DETALLE_MATRICULA;
	public static final String DTMT_ID;
	public static final String DTMT_ARN_ID;
	public static final String DTMT_CMPA_ID;
	public static final String DTMT_MLCRPR_ID;
	public static final String DTMT_NUMERO;
	public static final String DTMT_ESTADO;
	public static final String DTMT_ARCHIVO_ESTUDIANTES;
	public static final String DTMT_ESTADO_HISTORICO;
	public static final String DTMT_ESTADO_CAMBIO;
	public static final String DTMT_OBSERVACION_HISTORICO;
	public static final String DTMT_OBSERVACION_CAMBIO;
	public static final String DTMT_VALOR_PARCIAL;
	public static final String DTMT_FECHA_HISTORICO;
	public static final String DTMT_FECHA_CAMBIO;
	public static final String DTMT_USUARIO;
	public static final String DTMT_MODIFICACION;
	public static final String DTMT_FECHA_SOLICITUD;
	public static final String DTMT_FECHA_RESPUESTA;
	public static final String DTMT_ESTADO_SOLICITUD;
	public static final String DTMT_ESTADO_RESPUESTA;
	public static final String DTMT_CSL_RETIRO_ID;
	public static final String DTMT_FECHA_VERIFICACION_RETIRO;
	public static final String DTMT_ARCHIVO_RESPUESTA;
	
	public static final String DTMT_TIPO_ANULACION;
	public static final String DTMT_FECHA_ANULACION;
	public static final String DTMT_OBSERVACION_ANULACION;
	public static final String DTMT_OBSERVACION_FINAL_RETIRO;
	public static final String DTMT_ARCHIVO_ANULACION;
	public static final String DTMT_REGISTRANTE_ANULACION;
	
	
	//****************************************************************************
	//**************** TABLA DETALLE_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_DETALLE_PUESTO;
	public static final String DTPS_ID;
	public static final String DTPS_PST_ID;
	public static final String DTPS_FCES_ID;
	public static final String DTPS_FCDC_ID;
	public static final String DTPS_FCEM_ID;
	public static final String DTPS_RLLB_ID;
	public static final String DTPS_TIDC_ID;
	public static final String DTPS_CRR_ID;
	public static final String DTPS_ESTADO;
	public static final String DTPS_ESTADO_CATEGORIA;
	public static final String DTPS_PRAC_ID;
	public static final String DTPS_TIPO_CARRERA;

	//****************************************************************************
	//**************** TABLA DETALLE_TRAMITE_CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_DETALLE_TRAMITE_CONTRATO;
	public static final String DTTRCN_ID;
	public static final String DTTRCN_DTPS_ID;
	public static final String DTTRCN_TRM_ID;
	public static final String DTTRCN_CNT_ID;
	public static final String DTTRCN_SUB_ID;
	public static final String DTTRCN_NUM_TRAMITE;
	public static final String DTTRCN_ESTADO_TRAMITE;
	public static final String DTTRCN_ESTADO_PROCESO;
	public static final String DTTRCN_OBSERVACION;
	public static final String DTTRCN_FECHA_INICIO;
	public static final String DTTRCN_FECHA_FIN;
	public static final String DTTRCN_ARCHIVO_RESOLUCION;

	//****************************************************************************
	//**************** TABLA DURACION*******************************************
	//****************************************************************************
	public static final String TABLA_DURACION;
	public static final String DRC_ID;
	public static final String DRC_TIPO;
	public static final String DRC_TIEMPO;
	public static final String DRC_TIPO_SNIESE;

	//****************************************************************************
	//**************** TABLA EDIFICIO*******************************************
	//****************************************************************************
	public static final String TABLA_EDIFICIO;
	public static final String EDF_ID;
	public static final String EDF_DPN_ID;
	public static final String EDF_DESCRIPCION;
	public static final String EDF_ESTADO;
	public static final String EDF_CODIGO;
	public static final String EDF_LOCALIZACION;

	//****************************************************************************
	//**************** TABLA ETNIA*******************************************
	//****************************************************************************
	public static final String TABLA_ETNIA;
	public static final String ETN_ID;
	public static final String ETN_CODIGO_SNIESE;
	public static final String ETN_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA EXCEDENTE*******************************************
	//****************************************************************************
	public static final String TABLA_EXCEDENTE;
	public static final String EXC_ID;
	public static final String EXC_DESCRIPCION;
	public static final String EXC_ESTADO;
	public static final String EXC_TIPO;

	//****************************************************************************
	//**************** TABLA EXPERIENCIA_LABORAL_CONTR*******************************************
	//****************************************************************************
	public static final String TABLA_EXPERIENCIA_LABORAL_CONTR;
	public static final String EXLBCN_CNCN_ID;
	public static final String EXLBCN_ID;
	public static final String EXLBCN_TIPO_INSTITUCION;
	public static final String EXLBCN_DESCRIPCION;
	public static final String EXLBCN_UNIDAD;
	public static final String EXLBCN_PUESTO;
	public static final String EXLBCN_FECHA_INGRESO;
	public static final String EXLBCN_FECHA_SALIDA;
	public static final String EXLBCN_MOTIVO_INGRESO;
	public static final String EXLBCN_MOTIVO_SALIDA;
	public static final String EXLBCN_TIPO;

	//****************************************************************************
	//**************** TABLA FICHA_DOCENTE*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_DOCENTE;
	public static final String FCDC_ESTADO;
	public static final String FCDC_ID;
	public static final String FCDC_PRS_ID;

	//****************************************************************************
	//**************** TABLA FICHA_EMPLEADO*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_EMPLEADO;
	public static final String FCEM_ID;
	public static final String FCEM_PRS_ID;

	//****************************************************************************
	//**************** TABLA FICHA_ESTUDIANTE*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_ESTUDIANTE;
	public static final String FCES_ID;
	public static final String FCES_FECHA_INICIO;
	public static final String FCES_FECHA_EGRESAMIENTO;
	public static final String FCES_FECHA_ACTA_GRADO;
	public static final String FCES_NUM_ACTA_GRADO;
	public static final String FCES_FECHA_REFRENDACION;
	public static final String FCES_NUM_REFRENDACION;
	public static final String FCES_CRR_ESTUD_PREVIOS;
	public static final String FCES_TIEMPO_ESTUD_REC;
	public static final String FCES_TIPO_DURAC_REC;
	public static final String FCES_TIPO_COLEGIO;
	public static final String FCES_TIPO_COLEGIO_SNIESE;
	public static final String FCES_NOTA_PROM_ACUMULADO;
	public static final String FCES_NOTA_TRAB_TITULACION;
	public static final String FCES_LINK_TESIS;
	public static final String FCES_REC_ESTUD_PREVIOS;
	public static final String FCES_REC_ESTUD_PREV_SNIESE;
	public static final String FCES_FECHA_CREACION;
	public static final String FCES_PRS_ID;
	public static final String FCES_INAC_ID;
	public static final String FCES_FCIN_ID;
	public static final String FCES_ESTADO_UNIVERSITARIO;
	public static final String FCES_ESTADO_MATRICULA;
	public static final String FCES_OBSERVACION;
	public static final String FCES_TITULO_BACHILLER;
	public static final String FCES_NOTA_GRADO_SECUNDARIA;
	public static final String FCES_INAC_TTL_ID;
	public static final String FCES_COLEGIO_ID;
	public static final String FCES_TIPO_UNIV_ESTUD_PREV;
	public static final String FCES_ESTADO_ESTUD_PREV;
	public static final String FCES_UBC_COLEGIO;
	public static final String FCES_FECHA_ACTUALIZACION_DATOS;
		
	public static final String FCES_UNIV_ESTUD_PREV_ID;
	public static final String FCES_TIT_ESTUD_PREV_ID;
	public static final String FCES_REG_TITULO_PREV;

	//****************************************************************************
	//**************** TABLA FICHA_INSCRIPCION*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_INSCRIPCION;
	public static final String FCIN_ID;
	public static final String FCIN_USRO_ID;
	public static final String FCIN_CNCR_ID;
	public static final String FCIN_PRAC_ID;
	public static final String FCIN_FECHA_INSCRIPCION;
	public static final String FCIN_OBSERVACION;
	public static final String FCIN_OBSERVACION_INGRESO;
	public static final String FCIN_TIPO;
	public static final String FCIN_TIPO_INGRESO;
	public static final String FCIN_ESTADO_INGRESO;
	public static final String FCIN_DOCUMENTO_INGRESO;
	public static final String FCIN_MATRICULADO;
	public static final String FCIN_ESTADO;
	public static final String FCIN_NIVEL_INGRESO;
	public static final String FCIN_ENCUESTA;
	public static final String FCIN_NOTA_ENES;
	public static final String FCIN_CARRERA;
	public static final String FCIN_CARRERA_SIIU;
	public static final String FCIN_CNCR_AREA;
	public static final String FCIN_FCIN_NIVELACION;
	public static final String FCIN_ANIO_ABANDONA_CARRERA;
	public static final String FCIN_CRR_ANTERIOR_ID;
	public static final String FCIN_CNCR_ID_NIVELACION;
	public static final String FCIN_FCIN_ANTERIOR_ID;
	public static final String FCIN_NOTA_UBICACION;
	public static final String FCIN_VIGENTE;
	public static final String FCIN_ESTADO_RETIRO;
	public static final String FCIN_REINICIO_ORIGEN;
	public static final String FCIN_APLICA_NOTA_ENES;
	public static final String FCIN_NOTA_CORTE_ID;
	
	//****************************************************************************
	//**************** TABLA FICHA_MATRICULA*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_MATRICULA;
	public static final String FCMT_PRAC_ID;
	public static final String FCMT_ID;
	public static final String FCMT_FCES_ID;
	public static final String FCMT_PLCR_ID;
	public static final String FCMT_NIVEL_UBICACION;
	public static final String FCMT_ESTADO;
	public static final String FCMT_FECHA_CONVALIDACION;
	public static final String FCMT_TIPO;
	public static final String FCMT_MODALIDAD;
	public static final String FCMT_VALOR_TOTAL;
	public static final String FCMT_FECHA_MATRICULA;
	public static final String FCMT_FECHA_ESTADO;

	//****************************************************************************
	//**************** TABLA FORMATO_CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_FORMATO_CONTRATO;
	public static final String FRCN_ID;
	public static final String FRCN_TICN_ID;
	public static final String FRCN_DESCRIPCON;
	public static final String FRCN_ESTADO;

	//****************************************************************************
	//**************** TABLA FUNCION*******************************************
	//****************************************************************************
	public static final String TABLA_FUNCION;
	public static final String FNC_ID;
	public static final String FNC_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA FUNCION_CARGA_HORARIA*******************************************
	//****************************************************************************
	public static final String TABLA_FUNCION_CARGA_HORARIA;
	public static final String FNCRHR_ID;
	public static final String FNCRHR_DESCRIPCION;
	public static final String FNCRHR_HORAS_ASIGNADAS;
	public static final String FNCRHR_HORAS_MINIMO;
	public static final String FNCRHR_HORAS_MAXIMO;

	//****************************************************************************
	//**************** TABLA FUNCION_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_FUNCION_PUESTO;
	public static final String FNPS_ID;
	public static final String FNPS_PST_ID;
	public static final String FNPS_FNC_ID;

	//****************************************************************************
	//**************** TABLA GRATUIDAD*******************************************
	//****************************************************************************
	public static final String TABLA_GRATUIDAD;
	public static final String GRT_ID;
	public static final String GRT_FCES_ID;
	public static final String GRT_TIGR_ID;
	public static final String GRT_FCMT_ID;
	public static final String GRT_ESTADO;

	//****************************************************************************
	//**************** TABLA GRUPO_OCUPACIONAL*******************************************
	//****************************************************************************
	public static final String TABLA_GRUPO_OCUPACIONAL;
	public static final String GROC_ID;
	public static final String GROC_DESCRIPCION;
	public static final String GROC_GRADO;
	public static final String GROC_RMU;
	public static final String GROC_RGM_ID;
	public static final String GROC_RMU_HORA;

	
	//****************************************************************************
	//**************** TABLA HORA_CLASE*******************************************
	//****************************************************************************
	public static final String TABLA_HISTORIAL_NOTA_CORTE;
	public static final String HSNOCR_ID;
	public static final String HSNOCR_FECHA_REGISTRO;
	public static final String HSNOCR_USR_ID;
	public static final String HSNOCR_USR_NICK;
	public static final String HSNOCR_TIPO_PROCESO;
	public static final String HSNOCR_NOTA_ANTERIOR;
		
		
	//****************************************************************************
	//**************** TABLA HORA_CLASE*******************************************
	//****************************************************************************
	public static final String TABLA_HORA_CLASE;
	public static final String HOCL_ID;
	public static final String HOCL_DESCRIPCION;
	public static final String HOCL_ESTADO;
	public static final String HOCL_HORA_INICIO;
	public static final String HOCL_HORA_FIN;

	//****************************************************************************
	//**************** TABLA HORA_CLASE_AULA*******************************************
	//****************************************************************************
	public static final String TABLA_HORA_CLASE_AULA;
	public static final String HOCLAL_ID;
	public static final String HOCLAL_HOCL_ID;
	public static final String HOCLAL_ALA_ID;
	public static final String HOCLAL_ESTADO;

	//****************************************************************************
	//**************** TABLA HORARIO_ACADEMICO*******************************************
	//****************************************************************************
	public static final String TABLA_HORARIO_ACADEMICO;
	public static final String HRAC_ID;
	public static final String HRAC_DESCRIPCION;
	public static final String HRAC_DIA;
	public static final String HRAC_HORA_INICIO;
	public static final String HRAC_HORA_FIN;
	public static final String HRAC_ESTADO;
	public static final String HRAC_HOCLAL_ID;
	public static final String HRAC_MLCRPR_ID;
	public static final String MLCRPR_ID_COMP;

	//****************************************************************************
	//**************** TABLA INFORME_TECNICO*******************************************
	//****************************************************************************
	public static final String TABLA_INFORME_TECNICO;
	public static final String INTC_ID;
	public static final String INTC_PRTRCN_ID;
	public static final String INTC_OBSERVACION;
	public static final String INTC_ESTADO;

	//****************************************************************************
	//**************** TABLA INSTITUCION_ACADEMICA*******************************************
	//****************************************************************************
	public static final String TABLA_INSTITUCION_ACADEMICA;
	public static final String INAC_ID;
	public static final String INAC_CODIGO_SNIESE;
	public static final String INAC_DESCRIPCION;
	public static final String INAC_NIVEL;
	public static final String INAC_TIPO;
	public static final String INAC_TIPO_SNIESE;
	public static final String INAC_UBC_ID;

	//****************************************************************************
	//**************** TABLA INSTRUCCION_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_INSTRUCCION_PUESTO;
	public static final String INPS_ID;
	public static final String INPS_NVIN_ID;
	public static final String INPS_PST_ID;
	public static final String INPS_ANIO;
	public static final String INPS_MES;

	//****************************************************************************
	//**************** TABLA ITINERARIO*******************************************
	//****************************************************************************
	public static final String TABLA_ITINERARIO;
	public static final String ITN_ID;
	public static final String ITN_CRR_ID;
	public static final String ITN_DESCRIPCION;
	public static final String ITN_ESTADO;
	public static final String ITN_OBSERVACION;

	//****************************************************************************
	//**************** TABLA ITINERARIO_MALLA_MATERIA*******************************************
	//****************************************************************************
	public static final String TABLA_ITINERARIO_MALLA_MATERIA;
	public static final String ITMLMT_ID;
	public static final String ITMLMT_ESTADO;
	public static final String ITMLMT_MLCRMT_ID;
	public static final String ITMLMT_ITN_ID;

	//****************************************************************************
	//**************** TABLA MALLA_CURRICULAR*******************************************
	//****************************************************************************
	public static final String TABLA_MALLA_CURRICULAR;
	public static final String MLCR_ID;
	public static final String MLCR_TIFRML_ID;
	public static final String MLCR_CRR_ID;
	public static final String MLCR_CODIGO;
	public static final String MLCR_DESCRIPCION;
	public static final String MLCR_ESTADO;
	public static final String MLCR_FECHA_INICIO;
	public static final String MLCR_FECHA_FIN;
	public static final String MLCR_TOTAL_HORAS;
	public static final String MLCR_TOTAL_MATERIAS;
	public static final String MLCR_TIPO_ORG_APRENDIZAJE;
	public static final String MLCR_VIGENTE;
	public static final String MLCR_TIPO_APROBACION;
	
	//****************************************************************************
	//************** TABLA MALLA_CURRICULAR_NIVEL ********************************
	//****************************************************************************
	public static final String TABLA_MALLA_CURRICULAR_NIVEL;
	public static final String MLCRNV_ID;
	public static final String MLCRNV_CREDITOS;
	public static final String MLCRNV_CREDITOS_ACUMULADO;

	//****************************************************************************
	//**************** TABLA MALLA_CURRICULAR_MATERIA*******************************************
	//****************************************************************************
	public static final String TABLA_MALLA_CURRICULAR_MATERIA;
	public static final String MLCRMT_ID;
	public static final String MLCRMT_MLCR_ID;
	public static final String MLCRMT_MTR_ID;
	public static final String MLCRMT_UNFR_ID;
	public static final String MLCRMT_NVL_ID;
	public static final String MLCRMT_NVL_SUB_ID;
	public static final String MLCRMT_ESTADO;

	//****************************************************************************
	//**************** TABLA MALLA_CURRICULAR_PARALELO*******************************************
	//****************************************************************************
	public static final String TABLA_MALLA_CURRICULAR_PARALELO;
	public static final String MLCRPR_ID;
	public static final String MLCRPR_PRL_ID;
	public static final String MLCRPR_MLCRMT_ID;
	public static final String MLCRPR_INSCRITOS;
	public static final String MLCRPR_CUPO;
	public static final String MLCRPR_RESERVA_REPETIDOS;
	public static final String MLCRPR_NIVELACION_CRR_ID;
	public static final String MLCRPR_MODALIDAD;
	
	//****************************************************************************
	//**************** TABLA MALLA_PERIODO*******************************************
	//****************************************************************************
	public static final String TABLA_MALLA_PERIODO;
	public static final String MLPR_ID;
	public static final String MLPR_MLCR_ID;
	public static final String MLPR_PRAC_ID;
	public static final String MLPR_ESTADO;

	//****************************************************************************
	//**************** TABLA MATERIA*******************************************
	//****************************************************************************
	public static final String TABLA_MATERIA;
	public static final String MTR_UNIDAD_MEDIDA;
	public static final String MTR_CREDITOS;
	public static final String MTR_ID;
	public static final String MTR_CMFR_ID;
	public static final String MTR_TIMT_ID;
	public static final String MTR_SUB_ID;
	public static final String MTR_NCPRCR_ID;
	public static final String MTR_CRR_ID;
	public static final String MTR_CODIGO;
	public static final String MTR_CODIGO_SNIESE;
	public static final String MTR_DESCRIPCION;
	public static final String MTR_ESTADO;
	public static final String MTR_HORAS;
	public static final String MTR_INTEGRADORA_HORAS;
	public static final String MTR_PRE_PROFESIONAL_HORAS;
	public static final String MTR_HORAS_CIEN;
	public static final String MTR_RELACION_TRABAJO;
	public static final String MTR_HORAS_PRACTICAS;
	public static final String MTR_HORAS_TRAB_AUTONOMO;
	public static final String MTR_HORAS_PRAC_SEMA;
	public static final String MTR_HORAS_AUTONO_SEMA;
	
	//****************************************************************************
	//**************** TABLA MODALIDAD*******************************************
	//****************************************************************************
	public static final String TABLA_MODALIDAD;
	public static final String MDL_ID;
	public static final String MDL_DESCRIPCION;
	
	//****************************************************************************
	//**************** TABLA NOTA_CORTE*******************************************
	//****************************************************************************
	public static final String TABLA_NOTA_CORTE;
	public static final String NOCR_ID;
	public static final String NOCR_ESTADO;
	public static final String NOCR_NOTA;
	public static final String NOCR_PRAC_ID;
	public static final String NOCR_CRR_ID;
	
	//****************************************************************************
	//**************** TABLA NIVEL*******************************************
	//****************************************************************************
	public static final String TABLA_NIVEL;
	public static final String NVL_ID;
	public static final String NVL_DESCRIPCION;
	public static final String NVL_ESTADO;
	public static final String NVL_NUMERAL;
	
	//****************************************************************************
	//**************** TABLA NIVEL_APERTURA*******************************************
	//****************************************************************************
	public static final String TABLA_NIVEL_APERTURA;
	public static final String NVAP_ID;
	public static final String NVAP_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA NIVEL_COMPETENCIA*******************************************
	//****************************************************************************
	public static final String TABLA_NIVEL_COMPETENCIA;
	public static final String NVCM_ID;
	public static final String NVCM_CMP_ID;
	public static final String NVCM_DESCRIPCION;
	public static final String NVCM_COMPORTAMIENTO;

	//****************************************************************************
	//**************** TABLA NIVEL_FORMACION*******************************************
	//****************************************************************************
	public static final String TABLA_NIVEL_FORMACION;
	public static final String NVFR_ID;
	public static final String NVFR_RGAC_ID;
	public static final String NVFR_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA NIVEL_INSTRUCCION*******************************************
	//****************************************************************************
	public static final String TABLA_NIVEL_INSTRUCCION;
	public static final String NVIN_ID;
	public static final String NVIN_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA NUCLEO_PROBLEMICO*******************************************
	//****************************************************************************
	public static final String TABLA_NUCLEO_PROBLEMICO;
	public static final String NCPR_ID;
	public static final String NCPR_DESCRIPCION;
	public static final String NCPR_ESTADO;

	//****************************************************************************
	//**************** TABLA NUCLEO_PROBLEMICO_CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_NUCLEO_PROBLEMICO_CARRERA;
	public static final String NCPRCR_ID;
	public static final String NCPRCR_CRR_ID;
	public static final String NCPRCR_ESTADO;
	public static final String NCPRCR_NCPR_ID;

	//****************************************************************************
	//**************** TABLA PARALELO*******************************************
	//****************************************************************************
	public static final String TABLA_PARALELO;
	public static final String PRL_ID;
	public static final String PRL_CODIGO;
	public static final String PRL_DESCRIPCION;
	public static final String PRL_FECHA;
	public static final String PRL_INICIO_CLASE;
	public static final String PRL_FIN_CLASE;
	public static final String PRL_CUPO;
	public static final String PRL_ESTADO;
	public static final String PRL_CRR_ID;
	public static final String PRL_PRAC_ID;


	//****************************************************************************
	//**************** TABLA PERIODO_ACADEMICO*******************************************
	//****************************************************************************
	public static final String TABLA_PERIODO_ACADEMICO;
	public static final String PRAC_ID;
	public static final String PRAC_DESCRIPCION;
	public static final String PRAC_ESTADO;
	public static final String PRAC_FECHA_INCIO;
	public static final String PRAC_FECHA_FIN;
	public static final String PRAC_TIPO;

	//****************************************************************************
	//**************** TABLA PERSONA*******************************************
	//****************************************************************************
	public static final String TABLA_PERSONA;
	public static final String PRS_MAIL_INSTITUCIONAL;
	public static final String PRS_TELEFONO;
	public static final String PRS_FECHA_NACIMIENTO;
	public static final String PRS_ETN_ID;
	public static final String PRS_UBC_NACIMIENTO;
	public static final String PRS_UBC_RESIDENCIA;
	public static final String PRS_SECTOR_DOMICILIO;
	public static final String PRS_CALLE_PRINCIPAL;
	public static final String PRS_CALLE_SECUNDARIA;
	public static final String PRS_NUMERO_CASA;
	public static final String PRS_REFERENCIA_DOMICILIO;
	public static final String PRS_CELULAR;
	public static final String PRS_ID;
	public static final String PRS_TIPO_IDENTIFICACION;
	public static final String PRS_TIPO_IDENTIFICACION_SNIESE;
	public static final String PRS_IDENTIFICACION;
	public static final String PRS_PRIMER_APELLIDO;
	public static final String PRS_SEGUNDO_APELLIDO;
	public static final String PRS_NOMBRES;
	public static final String PRS_SEXO;
	public static final String PRS_SEXO_SNIESE;
	public static final String PRS_MAIL_PERSONAL;
	public static final String PRS_DISCAPACIDAD;
	public static final String PRS_TIPO_DISCAPACIDAD;
	public static final String PRS_PORCE_DISCAPACIDAD;
	public static final String PRS_CARNET_CONADIS;
	public static final String PRS_NUM_CARNET_CONADIS;
	public static final String PRS_ESTADO_CIVIL;
	public static final String PRS_FECHA_ACTUALIZACION_DATOS;
	public static final String PRS_FECHA_VINCULACION_SEGURO;
	public static final String PRS_FECHA_REGISTRO_SEGURO;
	public static final String PRS_ESTADO_SEGURO;
	public static final String PRS_FORMULARIO_SEGURO;
	public static final String PRS_FECHA_FORMULARIO_SEGURO;

	//****************************************************************************
	//**************** TABLA PLANIFICACION_CRONOGRAMA*******************************************
	//****************************************************************************
	public static final String TABLA_PLANIFICACION_CRONOGRAMA;
	public static final String PLCR_ID;
	public static final String PLCR_CRPRFL_ID;
	public static final String PLCR_ESTADO;
	public static final String PLCR_FECHA_INICIO;
	public static final String PLCR_FECHA_FIN;

	//****************************************************************************
	//**************** TABLA PORCENTAJE_GRATUIDAD*******************************************
	//****************************************************************************
	public static final String TABLA_PORCENTAJE_GRATUIDAD;
	public static final String PRGR_ID;
	public static final String PRGR_TIGR_ID;
	public static final String PRGR_DESCRIPCION;
	public static final String PRGR_VALOR;
	public static final String PRGR_ESTADO;

	//****************************************************************************
	//**************** TABLA PREREQUISITO*******************************************
	//****************************************************************************
	public static final String TABLA_PREREQUISITO;
	public static final String PRR_ID;
	public static final String PRR_MTR_ID;
	public static final String PRR_MTR_PREREQUISITO_ID;
	public static final String PRR_CODIGO;
	public static final String PRR_DESCRIPCION;
	public static final String PRR_ESTADO;

	//****************************************************************************
	//**************** TABLA PROCESO*******************************************
	//****************************************************************************
	public static final String TABLA_PROCESO;
	public static final String PRC_ID;
	public static final String PRC_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA PROCESO_CALIFICACION*******************************************
	//****************************************************************************
	public static final String TABLA_PROCESO_CALIFICACION;
	public static final String PRCL_ID;
	public static final String PRCL_CLF_ID;
	public static final String PRCL_FECHA;
	public static final String PRCL_TIPO_PROCESO;
	public static final String PRCL_OBSERVACION;
	public static final String PRCL_NOTA1;
	public static final String PRCL_NOTA2;
	public static final String PRCL_EXAMEN;
	public static final String PRCL_SUPLETORIO;
	public static final String PRCL_ASISTENCIA1;
	public static final String PRCL_ASISTENCIA2;
	public static final String PRCL_TOTAL_ASISTENCIA1;
	public static final String PRCL_TOTAL_ASISTENCIA2;
	public static final String PRCL_PROMEDIO_NOTAS;
	public static final String PRCL_PROMEDIO_ASISTENCIA;
	public static final String PRCL_ASISTENCIA_TOTAL;
	public static final String PRCL_PARAM_RECUPERACION1;
	public static final String PRCL_PARAM_RECUPERACION2;
	public static final String PRCL_NOTA_FINAL_SEMESTRE;
	public static final String PRCL_ASISTENCIA_DOCENTE1;
	public static final String PRCL_ASISTENCIA_DOCENTE2;
	public static final String PRCL_FECHA_NOTA2;
	public static final String PRCL_FECHA_RECUPERACION;
	public static final String PRCL_OBSERVACION2;
	public static final String PRCL_OBSERVACION3;
	public static final String PRCL_SUMA_P1_P2;
	public static final String PRCL_ASISTENCIA_TOTAL_DOC;

	//****************************************************************************
	//**************** TABLA PROCESO_ESTUDIANTE*******************************************
	//****************************************************************************
	public static final String TABLA_PROCESO_ESTUDIANTE;
	public static final String PRES_ID;
	public static final String PRES_FCES_ID;
	public static final String PRES_DESCRIPCION;
	public static final String PRES_TIPO;
	public static final String PRES_ESTADO;
	public static final String PRES_FECHA_EJECUCION;

	//****************************************************************************
	//**************** TABLA PROCESO_FLUJO*******************************************
	//****************************************************************************
	public static final String TABLA_PROCESO_FLUJO;
	public static final String PRFL_ID;
	public static final String PRFL_DESCRIPCION;
	public static final String PRFL_ESTADO;

	//****************************************************************************
	//**************** TABLA PROCESO_TRAMITE_CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_PROCESO_TRAMITE_CONTRATO;
	public static final String PRTRCN_ID;
	public static final String PRTRCN_DTTRCN_ID;
	public static final String PRTRCN_TIPO_PROCESO;
	public static final String PRTRCN_FECHA_EJECUCION;
	public static final String PRTRCN_RESULTADO_PROCESO;

	//****************************************************************************
	//**************** TABLA PROYECCION*******************************************
	//****************************************************************************
	public static final String TABLA_PROYECCION;
	public static final String PRY_ID;
	public static final String PRY_EXC_ID;
	public static final String PRY_MLCRMT_ID;
	public static final String PRY_PRAC_ID;
	public static final String PRY_NUM_NUEVO;
	public static final String PRY_NUM_REPETIDO;

	//****************************************************************************
	//**************** TABLA PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_PUESTO;
	public static final String PST_ID;
	public static final String PST_DENOMINACION;
	public static final String PST_ESTADO;
	public static final String PST_AMBITO;
	public static final String PST_CODIGO;
	public static final String PST_NIVEL;
	public static final String PST_MISION;
	public static final String PST_RESPONSABILIDAD;
	public static final String PST_DESCRIPCION_EXPERIENCIA;
	public static final String PST_INTERFAZ;
	public static final String PST_FECHA_CREACION;
	public static final String PST_NIVEL_RANGO_GRADUAL;
	public static final String PST_SER_ID;
	public static final String PST_GROC_ID;
	public static final String PST_TMDD_ID;
	public static final String PST_TIPS_ID;
	public static final String PST_CATEGORIA_DOCENTE;

	//****************************************************************************
	//**************** TABLA PUESTO_AREA_CONOCIMIENTO*******************************************
	//****************************************************************************
	public static final String TABLA_PUESTO_AREA_CONOCIMIENTO;
	public static final String PSARCN_ID;
	public static final String PSARCN_ARCN_ID;
	public static final String PSARCN_PST_ID;

	//****************************************************************************
	//**************** TABLA RECORD_ESTUDIANTE*******************************************
	//****************************************************************************
	public static final String TABLA_RECORD_ESTUDIANTE;
	public static final String RCES_ID;
	public static final String RCES_FCES_ID;
	public static final String RCES_MLCRPR_ID;
	public static final String RCES_ESTADO;
	public static final String RCES_OBSERVACION;
	public static final String RCES_INGRESO_NOTA;
	public static final String RCES_INGRESO_NOTA2;
	public static final String RCES_INGRESO_NOTA3;
	public static final String RCES_USUARIO;
	public static final String RCES_MODIFICACION;
	public static final String RCES_MODO_APROBACION;
	//****************************************************************************
	//**************** TABLA REGIMEN*******************************************
	//****************************************************************************
	public static final String TABLA_REGIMEN;
	public static final String RGM_ID;
	public static final String RGM_DESCRIPCION;
	public static final String RGM_CODIGO;

	//****************************************************************************
	//**************** TABLA REGIMEN_ACADEMICO*******************************************
	//****************************************************************************
	public static final String TABLA_REGIMEN_ACADEMICO;
	public static final String RGAC_ID;
	public static final String RGAC_DESCRIPCION;
	public static final String RGAC_ESTADO;

	//****************************************************************************
	//**************** TABLA RELACION_LABORAL*******************************************
	//****************************************************************************
	public static final String TABLA_RELACION_LABORAL;
	public static final String RLLB_ID;
	public static final String RLLB_DESCRIPCION;
	public static final String RLLB_ESTADO;

	//****************************************************************************
	//**************** TABLA REQUISITO*******************************************
	//****************************************************************************
	public static final String TABLA_REQUISITO;
	public static final String RQS_ID;
	public static final String RQS_PRTRCN_ID;
	public static final String RQS_PUBLICACIONES;
	public static final String RQS_EXPERICIA_DOCENTE;
	public static final String RQS_EXPERICIA_LABORAL;
	public static final String RQS_CAPACITACION;
	public static final String RQS_MECANIZADO_IESS;
	public static final String RQS_DECLARACION_BIENES;
	public static final String RQS_PLURIEMPLEO;
	public static final String RQS_NEPOTISMO;
	public static final String RQS_CERTIFICACION_BANCARIA;
	public static final String RQS_CERTIFICADO_AFILIADO_IESS;
	public static final String RQS_ESTADO;

	//****************************************************************************
	//**************** TABLA ROL*******************************************
	//****************************************************************************
	public static final String TABLA_ROL;
	public static final String ROL_ID;
	public static final String ROL_DESCRIPCION;
	public static final String ROL_TIPO;
	public static final String ROL_DETALLE;

	//****************************************************************************
	//**************** TABLA ROL_FLUJO_CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_ROL_FLUJO_CARRERA;
	public static final String ROFLCR_ID;
	public static final String ROFLCR_USRO_ID;
	public static final String ROFLCR_CRR_ID;
	public static final String ROFLCR_ESTADO;

	//****************************************************************************
	//**************** TABLA SERIE*******************************************
	//****************************************************************************
	public static final String TABLA_SERIE;
	public static final String SER_ID;
	public static final String SER_PRC_ID;
	public static final String SER_CODIGO;
	public static final String SER_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA SISTEMA_CALIFICACION*******************************************
	//****************************************************************************
	public static final String TABLA_SISTEMA_CALIFICACION;
	public static final String SSCL_ID;
	public static final String SSCL_PRAC_ID;
	public static final String SSCL_TISSCL_ID;
	public static final String SSCL_NOTA_MAXIMA;
	public static final String SSCL_NOTA_MINIMA_APROBACION;
	public static final String SSCL_NOTA_MINIMA_SUPLETORIO;
	public static final String SSCL_PORCENTAJE_APROBACION;
	public static final String SSCL_REDONDEO;
	public static final String SSCL_ESTADO;
	
	
	// ****************************************************************************
	// **************** TABLA SOLICITUD_TERCERA_MATRICULA*******************************************
	// ****************************************************************************
	public static final String TABLA_SOLICITUD_TERCERA_MATRICULA;
	public static final String SLTRMT_ID;
	public static final String SLTRMT_CSL_ID;
	public static final String SLTRMT_RCES_ID;
	public static final String SLTRMT_PRAC_ID;
	public static final String SLTRMT_ESTADO;
	public static final String SLTRMT_TIPO;
	public static final String SLTRMT_FECHA_SOLICITUD;
	public static final String SLTRMT_DOCUMENTO_SOLICITUD;
	public static final String SLTRMT_FECHA_VERIF_SOLICITUD;
	public static final String SLTRMT_FECHA_RESP_SOLICITUD;
	public static final String SLTRMT_DOCUMENTO_RESOLUCION;
	public static final String SLTRMT_OBSERVACION;
	public static final String SLTRMT_OBSERVACION_FINAL;
	public static final String SLTRMT_SUB_ID;
	public static final String SLTRMT_FCES_ID;
	public static final String SLTRMT_MTR_ID;
	public static final String SLTRMT_ESTADO_REGISTRO;

	//****************************************************************************
	//**************** TABLA TIEMPO_DEDICACION*******************************************
	//****************************************************************************
	public static final String TABLA_TIEMPO_DEDICACION;
	public static final String TMDD_ID;
	public static final String TMDD_DESCRIPCION;
	public static final String TMDD_HORAS;
	public static final String TMDD_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_APERTURA*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_APERTURA;
	public static final String TIAP_ID;
	public static final String TIAP_DESCRIPCION;
	public static final String TIAP_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_CARGA_HORARIA*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_CARGA_HORARIA;
	public static final String TICRHR_ID;
	public static final String TICRHR_DESCRIPCION;
	public static final String TICRHR_ESTADO;

	// ****************************************************************************
	// **************** TABLA TIPO CAUSAL******************************************
	// ****************************************************************************
	public static final String TABLA_TIPO_CAUSAL;
	public static final String TICS_ID;
	public static final String TICS_DESCRIPCION;
	public static final String TICS_ESTADO;
			
	
	
	//****************************************************************************
	//**************** TABLA TIPO_CONTRATO*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_CONTRATO;
	public static final String TICN_ID;
	public static final String TICN_TIEM_ID;
	public static final String TICN_DESCRIPCION;
	public static final String TICN_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_DOCUMENTO*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_DOCUMENTO;
	public static final String TIDC_ID;
	public static final String TIDC_SUB_ID;
	public static final String TIDC_DESCRIPCION;
	public static final String TIDC_NUMERO_DOCUMENTO;
	public static final String TIDC_FECHA_INICIO;
	public static final String TIDC_FECHA_FIN;
	public static final String TIDC_INGRESO_CONCURSO;

	//****************************************************************************
	//**************** TABLA TIPO_EMPLEADO*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_EMPLEADO;
	public static final String TIEM_ID;
	public static final String TIEM_DESCRIPCION;
	public static final String TIEM_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_FINANCIAMIENTO*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_FINANCIAMIENTO;
	public static final String TIFN_ID;
	public static final String TIFN_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA TIPO_FORMACION*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_FORMACION;
	public static final String TIFR_ID;
	public static final String TIFR_DESCRIPCION;
	public static final String TIFR_NVFR_ID;

	//****************************************************************************
	//**************** TABLA TIPO_FORMACION_MALLA*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_FORMACION_MALLA;
	public static final String TIFRML_ID;
	public static final String TIFRML_DESCRIPCION;
	public static final String TIFRML_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_FUNCION_CARGA_HORARIA*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_FUNCION_CARGA_HORARIA;
	public static final String TIFNCRHR_ID;
	public static final String TIFNCRHR_TICRHR_ID;
	public static final String TIFNCRHR_ESTADO;
	public static final String TIFNCRHR_FNCRHR_ID;

	//****************************************************************************
	//**************** TABLA TIPO_GRATUIDAD*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_GRATUIDAD;
	public static final String TIGR_ID;
	public static final String TIGR_DESCRIPCION;
	public static final String TIGR_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_MATERIA*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_MATERIA;
	public static final String TIMT_ID;
	public static final String TIMT_DESCRIPCION;
	public static final String TIMT_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_NIVEL_APERTURA*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_NIVEL_APERTURA;
	public static final String TINVAP_ID;
	public static final String TINVAP_NVAP_ID;
	public static final String TINVAP_TIAP_ID;
	public static final String TINVAP_PLCR_ID;
	public static final String TINVAP_FECHA_INICIO;
	public static final String TINVAP_FECHA_FIN;
	public static final String TINVAP_ESTADO;
	public static final String TINVAP_OBSERVACION;
	
	//****************************************************************************
	// **************** TABLA TIPO_PROCESO*******************************************
	// ****************************************************************************
	public static final String TABLA_TIPO_PROCESO;
	public static final String TIPR_ID;
	public static final String TIPR_DESCRIPCION;
	public static final String TIPR_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_PUESTO*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_PUESTO;
	public static final String TIPS_ID;
	public static final String TIPS_DESCRIPCION;
	public static final String TIPS_ESTADO;

	//****************************************************************************
	//**************** TABLA TIPO_SEDE*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_SEDE;
	public static final String TISE_ID;
	public static final String TISE_RGAC_ID;
	public static final String TISE_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA TIPO_SISTEMA_CALIFICACION*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_SISTEMA_CALIFICACION;
	public static final String TISSCL_ID;
	public static final String TISSCL_DESCRIPCION;
	public static final String TISSCL_ESTADO;
	public static final String TISSCL_OBSERVACION;

	//****************************************************************************
	//**************** TABLA TITULO*******************************************
	//****************************************************************************
	public static final String TABLA_TITULO;
	public static final String TTL_ID;
	public static final String TTL_DESCRIPCION;
	public static final String TTL_SEXO;
	public static final String TTL_ESTADO;
	public static final String TTL_TIPO;

	//****************************************************************************
	//**************** TABLA TRAMITE*******************************************
	//****************************************************************************
	public static final String TABLA_TRAMITE;
	public static final String TRM_ID;
	public static final String TRM_DESCRIPCION;
	public static final String TRM_ESTADO;

	//****************************************************************************
	//**************** TABLA TURNERO*******************************************
	//****************************************************************************
	public static final String TABLA_TURNERO;
	public static final String TRN_ID;
	public static final String TRN_PRTRCN_ID;
	public static final String TRN_USRO_ID;
	public static final String TRN_ESTADO;

	//****************************************************************************
	//**************** TABLA UBICACION*******************************************
	//****************************************************************************
	public static final String TABLA_UBICACION;
	public static final String UBC_ID;
	public static final String UBC_DESCRIPCION;
	public static final String UBC_JERARQUIA;
	public static final String UBC_GENTILICIO;
	public static final String UBC_COD_SNIESE;
	public static final String UBC_SUB_ID;

	//****************************************************************************
	//**************** TABLA UNIDAD_FORMACION*******************************************
	//****************************************************************************
	public static final String TABLA_UNIDAD_FORMACION;
	public static final String UNFR_ID;
	public static final String UNFR_DESCRIPCION;
	public static final String UNFR_ESTADO;

	//****************************************************************************
	//**************** TABLA USUARIO*******************************************
	//****************************************************************************
	public static final String TABLA_USUARIO;
	public static final String USR_ID;
	public static final String USR_IDENTIFICACION;
	public static final String USR_NICK;
	public static final String USR_PASSWORD;
	public static final String USR_FECHA_CREACION;
	public static final String USR_FECHA_CADUCIDAD;
	public static final String USR_FECHA_CAD_PASS;
	public static final String USR_ESTADO;
	public static final String USR_EST_SESION;
	public static final String USR_ACTIVE_DIRECTORY;
	public static final String USR_PRS_ID;

	//****************************************************************************
	//**************** TABLA USUARIO_ROL*******************************************
	//****************************************************************************
	public static final String TABLA_USUARIO_ROL;
	public static final String USRO_ID;
	public static final String USRO_ESTADO;
	public static final String USRO_USR_ID;
	public static final String USRO_ROL_ID;

	//****************************************************************************
	//**************** TABLA VIGENCIA*******************************************
	//****************************************************************************
	public static final String TABLA_VIGENCIA;
	public static final String VGN_ID;
	public static final String VGN_DESCRIPCION;
	 
	//****************************************************************************
	//**************** TABLA ASIGNACION_EVALUADOR*******************************************
	//****************************************************************************
	public static final String TABLA_ASIGNACION_EVALUADOR;
	public static final String ASEV_ID;
	public static final String ASEV_CRHR_ID;
	public static final String ASEV_EVEV_ID;
	public static final String ASEV_ESTADO;
	public static final String ASEV_USUARIO;
	public static final String ASEV_FECHA;
	public static final String ASEV_EVALUADOR_CRR_ID;

	//****************************************************************************
	//**************** TABLA CONTENIDO*******************************************
	//****************************************************************************
	public static final String TABLA_CONTENIDO;
	public static final String CNTN_ID;
	public static final String CNTN_TPCNFNTPEV_ID;
	public static final String CNTN_ASEV_ID;
	public static final String CNTN_EVA_ID;
	public static final String CNTN_DESCRIPCION;
	public static final String CNTN_SELECCION;
	public static final String CNTN_FECHA;
	public static final String CNTN_USUARIO;
	public static final String CNTN_SELECCION_INICIAL;
	public static final String CNTN_REGISTRO_APELACION;
	public static final String CNTN_USUARIO_APELACION;
	public static final String CNTN_OFICIO_APELACION;
	public static final String CNTN_ESTADO_APELACION;
	
	//****************************************************************************
	//**************** TABLA EVALUACION*******************************************
	//****************************************************************************
	public static final String TABLA_EVALUACION;
	public static final String EVL_EVA_ID;
	public static final String EVL_PRAC_ID;
	public static final String EVL_TPEV_ID;
	public static final String EVL_EVA_DESCRIPCION;
	public static final String EVL_EVA_USUARIO;
	public static final String EVL_EVA_FECHA;
	public static final String EVL_EVA_ESTADO;
	
	
	//****************************************************************************
	//**************** TABLA EVALUADOR_EVALUADO*******************************************
	//****************************************************************************
	public static final String TABLA_EVALUADOR_EVALUADO;
	public static final String EVEV_ID;
	public static final String EVEV_USRO_ID;
	public static final String EVEV_EVALUADOR;
	
	//****************************************************************************
	//**************** TABLA FUNCION_EVALUACION*******************************************
	//****************************************************************************
	public static final String TABLA_FUNCION_EVALUACION;
	public static final String FNEV_ID;
	public static final String FNEV_DESCRIPCION;
	public static final String FNEV_ESTADO;
	public static final String FNEV_FECHA;
	public static final String FNEV_USUARIO;
	
	//****************************************************************************
	//**************** TABLA FUNCION_TIPO_EVALUACION*******************************************
	//****************************************************************************
	public static final String TABLA_FUNCION_TIPO_EVALUACION;
	public static final String FNTIEV_FNCTPEV_ID;
	public static final String FNTIEV_TPEV_ID;
	public static final String FNTIEV_FNEV_ID;
	public static final String FNTIEV_FNCTPEV_USUARIO;
	public static final String FNTIEV_FNCTPEV_FECHA;
	public static final String FNTIEV_FNCTPEV_ESTADO;
	
	//****************************************************************************
	//**************** TABLA TIPO_EVALUACION*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_EVALUACION;
	public static final String TIEV_TPEV_ID;
	public static final String TIEV_TPEV_DESCRIPCION;
	public static final String TIEV_TPEV_INTRODUCCION;
	public static final String TIEV_TPEV_INSTRUCCIONES;
	public static final String TIEV_TPEV_FECHA;
	public static final String TIEV_TPEV_USUARIO;
	public static final String TIEV_TPEV_ESTADO;
	public static final String EVL_EVA_CRONOGRAMA_INICIO;
	public static final String EVL_EVA_CRONOGRAMA_FIN;
	public static final String EVL_EVA_PRFL_ID;

	//****************************************************************************
	//**************** TABLA TPCN_FUNCION_TPEV*******************************************
	//****************************************************************************
	public static final String TABLA_TPCN_FUNCION_TPEV;
	public static final String TPFNTP_TPCNFNTPEV_ID;
	public static final String TPFNTP_FNCTPEV_ID;
	public static final String TPFNTP_TPCN_ID;
	public static final String TPFNTP_TPCNFNTPEV_USUARIO;
	public static final String TPFNTP_TPCNFNTPEV_FECHA;
	public static final String TPFNTP_TPCNFNTPEV_ESTADO;
	
	//****************************************************************************
	//**************** TABLA TIPO_CONTENIDO*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_CONTENIDO;
	public static final String TICN_TPCN_ID;
	public static final String TICN_TPCN_DESCRIPCION;
	public static final String TICN_TPCN_ESTADO;
	public static final String TICN_TPCN_TIPO;
	public static final String TICN_TPCN_NUMERAL;
	public static final String TICN_TPCN_TIPO_COMPONENTE;
	public static final String TICN_TPCN_TIPO_SELECCION;
	public static final String TICN_TPCN_NUM_MAX;
	public static final String TICN_TPCN_FECHA;
	public static final String TICN_TPCN_USUARIO;
	public static final String TICN_TPCN_OBLIGATORIEDAD;
	public static final String TICN_TPCN_FK_TPCN_ID;
	
	//****************************************************************************
	//**************** TABLA CARRERA_INTERCAMBIO*******************************************
	//****************************************************************************
	public static final String TABLA_CARRERA_INTERCAMBIO;
	public static final String CRIN_ID;
	public static final String CRIN_CRR_ID;
	public static final String CRIN_FCIN_ID;
	public static final String CRIN_AUTORIZACION;
	public static final String CRIN_USER_REGISTRO;
	public static final String CRIN_FECHA_REGISTRO;
	public static final String CRIN_OBSERVACION;
	public static final String CRIN_ESTADO;
	
	//****************************************************************************
	//**************** TABLA MATERIA_INTERCAMBIO*******************************************
	//****************************************************************************
	public static final String TABLA_MATERIA_INTERCAMBIO;
	public static final String MTIN_ID;
	public static final String MTIN_CRIN_ID;
	public static final String MTIN_MTR_ID;
	public static final String MTIN_DESCRIPCION;
	public static final String MTIN_CODIGO;
	public static final String MTIN_ESTADO;
	
	//****************************************************************************
	//**************** TABLA REFERENCIA ******************************************
	//****************************************************************************
	public static final String TABLA_REFERENCIA;
	public static final String RFR_ID;
	public static final String RFR_CEDULA;
	public static final String RFR_NOMBRE;
	public static final String RFR_DIRECCION;
	public static final String RFR_PARENTESCO;
	public static final String RFR_PORCENTAJE;
	public static final String RFR_ESTADO;
	
	static{
		if(base.equals("desarrollo")){
			ResourceBundle rb = ResourceBundle.getBundle("META-INF.configuracion.tablasJdbc");
			ESQUEMA_DESARROLLO = rb.getString("esquema.desarrollo");
			
			TABLA_HORARIO_FUNCION = rb.getString("desarrollo.horario.funcion");
			HRFN_ID=rb.getString("desarrollo.horario.funcion.hrfn.id");
			HRFN_DESCRIPCION=rb.getString("desarrollo.horario.funcion.hrfn.descripcion");
			HRFN_HORA_INICIO=rb.getString("desarrollo.horario.funcion.hrfn.hora.inicio");
			HRFN_HORA_FIN=rb.getString("desarrollo.horario.funcion.hrfn.hora.fin");
			HRFN_ESTADO=rb.getString("desarrollo.horario.funcion.hrfn.estado");
			HRFN_DIA=rb.getString("desarrollo.horario.funcion.hrfn.dia");
			HRFN_HOCL_ID=rb.getString("desarrollo.horario.funcion.hocl.id");
			HRFN_FUNCION=rb.getString("desarrollo.horario.funcion.hrfn.funcion");
			HRFN_ACTIVIDAD=rb.getString("desarrollo.horario.funcion.hrfn.actividad");
			HRFN_PRAC_ID=rb.getString("desarrollo.horario.funcion.hrfn.prac.id");
			HRFN_DTPS_ID=rb.getString("desarrollo.horario.funcion.hrfn.dtps.id");
			
			TABLA_PLANIFICACION_EVALUACION = rb.getString("desarrollo.planificacion.evaluacion");
			PLEV_ID=rb.getString("desarrollo.planificacion.evaluacion.plev.id");
			PLEV_FECHA_INICIO=rb.getString("desarrollo.planificacion.evaluacion.plev.fecha.inicio");
			PLEV_FECHA_FIN=rb.getString("desarrollo.planificacion.evaluacion.plev.fecha.fin");
			PLEV_ESTADO=rb.getString("desarrollo.planificacion.evaluacion.plev.estado");
			
			TABLA_ACTIVIDAD_ESENCIAL = rb.getString("desarrollo.actividad.esencial");
			ACES_ID = rb.getString("desarrollo.actividad.esencial.aces.id");
			ACES_DESCRIPCION = rb.getString("desarrollo.actividad.esencial.aces.descripcion");
			TABLA_ACTIVIDAD_PUESTO = rb.getString("desarrollo.actividad.puesto");
			ACPS_ID = rb.getString("desarrollo.actividad.puesto.acps.id");
			ACPS_ACES_ID = rb.getString("desarrollo.actividad.puesto.aces.id");
			ACPS_PST_ID = rb.getString("desarrollo.actividad.puesto.pst.id");
			TABLA_APRUEBA_SOLICITUD = rb.getString("desarrollo.aprueba.solicitud");
			APSL_ID = rb.getString("desarrollo.aprueba.solicitud.apsl.id");
			APSL_PRTRCN_ID = rb.getString("desarrollo.aprueba.solicitud.prtrcn.id");
			APSL_FECHA_APROBACION = rb.getString("desarrollo.aprueba.solicitud.apsl.fecha.aprobacion");
			APSL_ESTADO = rb.getString("desarrollo.aprueba.solicitud.apsl.estado");
			APSL_OBSERVACION = rb.getString("desarrollo.aprueba.solicitud.apsl.observacion");
			TABLA_ARANCEL = rb.getString("desarrollo.arancel");
			ARN_ID = rb.getString("desarrollo.arancel.arn.id");
			ARN_MDL_ID = rb.getString("desarrollo.arancel.mdl.id");
			ARN_TIGR_ID = rb.getString("desarrollo.arancel.tigr.id");
			ARN_DESCRIPCION = rb.getString("desarrollo.arancel.arn.descripcion");
			ARN_TIPO_MATRICULA = rb.getString("desarrollo.arancel.arn.tipo.matricula");
			ARN_TIPO_NUM_MATRICULA = rb.getString("desarrollo.arancel.arn.tipo.num.matricula");
			ARN_VALOR = rb.getString("desarrollo.arancel.arn.valor");
			ARN_TIPO = rb.getString("desarrollo.arancel.arn.tipo");
			TABLA_AREA_CONOCIMIENTO = rb.getString("desarrollo.area.conocimiento");
			ARCN_ID = rb.getString("desarrollo.area.conocimiento.arcn.id");
			ARCN_DESCRIPCION = rb.getString("desarrollo.area.conocimiento.arcn.descripcion");
			ARCN_CODIGO = rb.getString("desarrollo.area.conocimiento.arcn.codigo");
			ARCN_JERARQUIA = rb.getString("desarrollo.area.conocimiento.arcn.jerarquia");
			ARCN_SUB_ID = rb.getString("desarrollo.area.conocimiento.arcn.sub.id");
			TABLA_ASIGNA_PRESUPUESTO = rb.getString("desarrollo.asigna.presupuesto");
			ASPR_ID = rb.getString("desarrollo.asigna.presupuesto.aspr.id");
			ASPR_PRTRCN_ID = rb.getString("desarrollo.asigna.presupuesto.prtrcn.id");
			ASPR_PARTIDA_PRESUPUESTO = rb.getString("desarrollo.asigna.presupuesto.aspr.partida.presupuesto");
			ASPR_ESTADO = rb.getString("desarrollo.asigna.presupuesto.aspr.estado");
			ASPR_ARCHIVO_CERTIFICACION = rb.getString("desarrollo.asigna.presupuesto.aspr.archivo.certificacion");
			TABLA_AULA = rb.getString("desarrollo.aula");
			ALA_ID = rb.getString("desarrollo.aula.ala.id");
			ALA_EDF_ID = rb.getString("desarrollo.aula.edf.id");
			ALA_DESCRIPCION = rb.getString("desarrollo.aula.ala.descripcion");
			ALA_CODIGO = rb.getString("desarrollo.aula.ala.codigo");
			ALA_TIPO = rb.getString("desarrollo.aula.ala.tipo");
			ALA_CAPACIDAD = rb.getString("desarrollo.aula.ala.capacidad");
			ALA_PISO = rb.getString("desarrollo.aula.ala.piso");
			ALA_ESTADO = rb.getString("desarrollo.aula.ala.estado");
			TABLA_CALIFICACION = rb.getString("desarrollo.calificacion");
			CLF_NOTA_FINAL_SEMESTRE = rb.getString("desarrollo.calificacion.clf.nota.final.semestre");
			CLF_ESTADO = rb.getString("desarrollo.calificacion.clf.estado");
			CLF_ASISTENCIA_DOCENTE1 = rb.getString("desarrollo.calificacion.clf.asistencia.docente1");
			CLF_ASISTENCIA_DOCENTE2 = rb.getString("desarrollo.calificacion.clf.asistencia.docente2");
			CLF_SUMA_P1_P2 = rb.getString("desarrollo.calificacion.clf.suma.p1.p2");
			CLF_ASISTENCIA_TOTAL_DOC = rb.getString("desarrollo.calificacion.clf.asistencia.total.doc");
			CLF_ID = rb.getString("desarrollo.calificacion.clf.id");
			CLF_RCES_ID = rb.getString("desarrollo.calificacion.rces.id");
			CLF_NOTA1 = rb.getString("desarrollo.calificacion.clf.nota1");
			CLF_NOTA2 = rb.getString("desarrollo.calificacion.clf.nota2");
			CLF_EXAMEN = rb.getString("desarrollo.calificacion.clf.examen");
			CLF_SUPLETORIO = rb.getString("desarrollo.calificacion.clf.supletorio");
			CLF_ASISTENCIA1 = rb.getString("desarrollo.calificacion.clf.asistencia1");
			CLF_ASISTENCIA2 = rb.getString("desarrollo.calificacion.clf.asistencia2");
			CLF_TOTAL_ASISTENCIA1 = rb.getString("desarrollo.calificacion.clf.total.asistencia1");
			CLF_TOTAL_ASISTENCIA2 = rb.getString("desarrollo.calificacion.clf.total.asistencia2");
			CLF_PROMEDIO_NOTAS = rb.getString("desarrollo.calificacion.clf.promedio.notas");
			CLF_PROMEDIO_ASISTENCIA = rb.getString("desarrollo.calificacion.clf.promedio.asistencia");
			CLF_ASISTENCIA_TOTAL = rb.getString("desarrollo.calificacion.clf.asistencia.total");
			CLF_PARAM_RECUPERACION1 = rb.getString("desarrollo.calificacion.clf.param.recuperacion1");
			CLF_PARAM_RECUPERACION2 = rb.getString("desarrollo.calificacion.clf.param.recuperacion2");
			
			TABLA_CALIFICACION_MODULO = rb.getString("desarrollo.calificacion.modulo");
			CLMD_NOTA_FINAL_SEMESTRE = rb.getString("desarrollo.calificacion.modulo.clmd.nota.final.semestre");
			CLMD_ESTADO = rb.getString("desarrollo.calificacion.modulo.clmd.estado");
			CLMD_ASISTENCIA_DOCENTE1 = rb.getString("desarrollo.calificacion.modulo.clmd.asistencia.docente1");
			CLMD_ASISTENCIA_DOCENTE2 = rb.getString("desarrollo.calificacion.modulo.clmd.asistencia.docente2");
			CLMD_SUMA_P1_P2 = rb.getString("desarrollo.calificacion.modulo.clmd.suma.p1.p2");
			CLMD_ASISTENCIA_TOTAL_DOC = rb.getString("desarrollo.calificacion.modulo.clmd.asistencia.total.doc");
			CLMD_ID = rb.getString("desarrollo.calificacion.modulo.clmd.id");
			CLMD_RCES_ID = rb.getString("desarrollo.calificacion.modulo.rces.id");
			CLMD_NOTA1 = rb.getString("desarrollo.calificacion.modulo.clmd.nota1");
			CLMD_NOTA2 = rb.getString("desarrollo.calificacion.modulo.clmd.nota2");
			CLMD_EXAMEN = rb.getString("desarrollo.calificacion.modulo.clmd.examen");
			CLMD_SUPLETORIO = rb.getString("desarrollo.calificacion.modulo.clmd.supletorio");
			CLMD_ASISTENCIA1 = rb.getString("desarrollo.calificacion.modulo.clmd.asistencia1");
			CLMD_ASISTENCIA2 = rb.getString("desarrollo.calificacion.modulo.clmd.asistencia2");
			CLMD_TOTAL_ASISTENCIA1 = rb.getString("desarrollo.calificacion.modulo.clmd.total.asistencia1");
			CLMD_TOTAL_ASISTENCIA2 = rb.getString("desarrollo.calificacion.modulo.clmd.total.asistencia2");
			CLMD_PROMEDIO_NOTAS = rb.getString("desarrollo.calificacion.modulo.clmd.promedio.notas");
			CLMD_PROMEDIO_ASISTENCIA = rb.getString("desarrollo.calificacion.modulo.clmd.promedio.asistencia");
			CLMD_ASISTENCIA_TOTAL = rb.getString("desarrollo.calificacion.modulo.clmd.asistencia.total");
			CLMD_PARAM_RECUPERACION1 = rb.getString("desarrollo.calificacion.modulo.clmd.param.recuperacion1");
			CLMD_PARAM_RECUPERACION2 = rb.getString("desarrollo.calificacion.modulo.clmd.param.recuperacion2");
			MLCRPR_ID_MODULO = rb.getString("desarrollo.calificacion.modulo.mlcrpr.id.modulo");
			TABLA_CAMPO_FORMACION = rb.getString("desarrollo.campo.formacion");
			CMFR_ID = rb.getString("desarrollo.campo.formacion.cmfr.id");
			CMFR_TIPO = rb.getString("desarrollo.campo.formacion.cmfr.tipo");
			CMFR_DESCRIPCION = rb.getString("desarrollo.campo.formacion.cmfr.descripcion");
			CMFR_ESTADO = rb.getString("desarrollo.campo.formacion.cmfr.estado");
			TABLA_CANDIDATO_CONTRATO = rb.getString("desarrollo.candidato.contrato");
			CNCN_ID = rb.getString("desarrollo.candidato.contrato.cncn.id");
			CNCN_PRS_ID = rb.getString("desarrollo.candidato.contrato.prs.id");
			CNCN_PRTRCN_ID = rb.getString("desarrollo.candidato.contrato.prtrcn.id");
			CNCN_ESTADO = rb.getString("desarrollo.candidato.contrato.cncn.estado");
			CNCN_OBS_RECTORADO = rb.getString("desarrollo.candidato.contrato.cncn.obs.rectorado");
			CNCN_TERCER_NIVEL = rb.getString("desarrollo.candidato.contrato.cncn.tercer.nivel");
			CNCN_CUARTO_NIVEL = rb.getString("desarrollo.candidato.contrato.cncn.cuarto.nivel");
			CNCN_IMPEDIMENTO = rb.getString("desarrollo.candidato.contrato.cncn.impedimento");
			CNCN_OBS_IMPEDIMENTO = rb.getString("desarrollo.candidato.contrato.cncn.obs.impedimento");
			CNCN_ARCHIVO_IMPEDIMENTO = rb.getString("desarrollo.candidato.contrato.cncn.archivo.impedimento");
			CNCN_LABORA_OTRA_INST = rb.getString("desarrollo.candidato.contrato.cncn.labora.otra.inst");
			CNCN_OBS_LABORA_OTRA_INST = rb.getString("desarrollo.candidato.contrato.cncn.obs.labora.otra.inst");
			CNCN_ARCHIVO_LABORA_INST = rb.getString("desarrollo.candidato.contrato.cncn.archivo.labora.inst");
			TABLA_CARGA_HORARIA = rb.getString("desarrollo.carga.horaria");
			CRHR_ID = rb.getString("desarrollo.carga.horaria.crhr.id");
			CRHR_TIFNCRHR_ID = rb.getString("desarrollo.carga.horaria.tifncrhr.id");
			CRHR_PRAC_ID = rb.getString("desarrollo.carga.horaria.prac.id");
			CRHR_DTPS_ID = rb.getString("desarrollo.carga.horaria.dtps.id");
			CRHR_MLCRPR_ID = rb.getString("desarrollo.carga.horaria.mlcrpr.id");
			CRHR_OBSERVACION = rb.getString("desarrollo.carga.horaria.crhr.observacion");
			CRHR_ESTADO = rb.getString("desarrollo.carga.horaria.crhr.estado");
			CRHR_NUM_HORAS = rb.getString("desarrollo.carga.horaria.crhr.num.horas");
			CRHR_ESTADO_ESTADO = rb.getString("desarrollo.carga.horaria.crhr.estado.eliminacion");
			CRHR_CARRERA_ID = rb.getString("desarrollo.carga.horaria.crhr.carrera.id");
			TABLA_CARGA_HORARIA_CONTRATO = rb.getString("desarrollo.carga.horaria.contrato");
			CRHRCN_ID = rb.getString("desarrollo.carga.horaria.contrato.crhrcn.id");
			CRHRCN_PRTRCN_ID = rb.getString("desarrollo.carga.horaria.contrato.prtrcn.id");
			CRHRCN_HORA_CLASE = rb.getString("desarrollo.carga.horaria.contrato.crhrcn.hora.clase");
			CRHRCN_HORA_VINCULACION = rb.getString("desarrollo.carga.horaria.contrato.crhrcn.hora.vinculacion");
			CRHRCN_TUTORIA = rb.getString("desarrollo.carga.horaria.contrato.crhrcn.tutoria");
			CRHRCN_PREPARACION_CLASE = rb.getString("desarrollo.carga.horaria.contrato.crhrcn.preparacion.clase");
			
			TABLA_CARRERA = rb.getString("desarrollo.carrera");
			CRR_TIPO = rb.getString("desarrollo.carrera.crr.tipo");
			CRR_CUPO = rb.getString("desarrollo.carrera.crr.cupo");
			CRR_ARANCEL = rb.getString("desarrollo.carrera.crr.arancel");
			CRR_ESPE_CODIGO = rb.getString("desarrollo.carrera.crr.espe.codigo");
			CRR_ID = rb.getString("desarrollo.carrera.crr.id");
			CRR_DPN_ID = rb.getString("desarrollo.carrera.dpn.id");
			CRR_DESCRIPCION = rb.getString("desarrollo.carrera.crr.descripcion");
			CRR_COD_SNIESE = rb.getString("desarrollo.carrera.crr.cod.sniese");
			CRR_DETALLE = rb.getString("desarrollo.carrera.crr.detalle");
			CRR_FECHA_CREACION = rb.getString("desarrollo.carrera.crr.fecha.creacion");
			CRR_RESOLUCION = rb.getString("desarrollo.carrera.crr.resolucion");
			CRR_PROCESO = rb.getString("desarrollo.carrera.crr.proceso");
			CRR_REAJUSTE_MATRICULA = rb.getString("desarrollo.carrera.crr.reajuste.matricula");
			CRR_ID_ARANCEL = rb.getString("desarrollo.carrera.crr.id.arancel");
			CRR_TIPO_EVALUACION = rb.getString("desarrollo.carrera.crr.tipo.evaluacion");
			
			TABLA_CARRERA_AREA= rb.getString("desarrollo.carrera.area");
			CRAR_ID= rb.getString("desarrollo.carrera.area.crar.id");
			CRAR_CRR_ID= rb.getString("desarrollo.carrera.area.crar.crr.id");
			CRAR_AREA_ID= rb.getString("desarrollo.carrera.area.crar.area.id");
			
			TABLA_CAUSAL_DETALLE_MATRICULA = rb.getString("desarrollo.causal.detalle.matricula");
			CSDTMT_ID = rb.getString("desarrollo.causal.detalle.matricula.csdtmt.id");
			CSDTMT_CSL_ID = rb.getString("desarrollo.causal.detalle.matricula.csl.id");
			CSDTMT_DTMT_ID = rb.getString("desarrollo.causal.detalle.matricula.dtmt.id");
			
			TABLA_CAUSAL = rb.getString("desarrollo.causal");
			CSL_ID = rb.getString("desarrollo.causal.csl.id");
			CSL_TICS_ID = rb.getString("desarrollo.causal.tics.id");
			CSL_DESCRIPCION = rb.getString("desarrollo.causal.csl.descripcion");
			CSL_CODIGO = rb.getString("desarrollo.causal.csl.codigo");
			CSL_ESTADO = rb.getString("desarrollo.causal.csl.estado");
			
			TABLA_COMPETENCIA = rb.getString("desarrollo.competencia");
			CMP_ID = rb.getString("desarrollo.competencia.cmp.id");
			CMP_DENOMINACION = rb.getString("desarrollo.competencia.cmp.denominacion");
			CMP_DEFINICION = rb.getString("desarrollo.competencia.cmp.definicion");
			CMP_TIPO = rb.getString("desarrollo.competencia.cmp.tipo");
			TABLA_COMPETENCIA_PUESTO = rb.getString("desarrollo.competencia.puesto");
			CMPS_ID = rb.getString("desarrollo.competencia.puesto.cmps.id");
			CMPS_PST_ID = rb.getString("desarrollo.competencia.puesto.pst.id");
			CMPS_NVCM_ID = rb.getString("desarrollo.competencia.puesto.nvcm.id");
			TABLA_COMPROBANTE_PAGO = rb.getString("desarrollo.comprobante.pago");
			CMPA_ID = rb.getString("desarrollo.comprobante.pago.cmpa.id");
			CMPA_FCMT_ID = rb.getString("desarrollo.comprobante.pago.fcmt.id");
			CMPA_CODIGO = rb.getString("desarrollo.comprobante.pago.cmpa.codigo");
			CMPA_ESTADO = rb.getString("desarrollo.comprobante.pago.cmpa.estado");
			CMPA_VALOR_TOTAL = rb.getString("desarrollo.comprobante.pago.cmpa.valor.total");
			CMPA_FECHA_EMISION = rb.getString("desarrollo.comprobante.pago.cmpa.fecha.emision");
			CMPA_FECHA_CADUCIDAD = rb.getString("desarrollo.comprobante.pago.cmpa.fecha.caducidad");
			CMPA_TIPO = rb.getString("desarrollo.comprobante.pago.cmpa.tipo");
			CMPA_FECHA_PAGO = rb.getString("desarrollo.comprobante.pago.cmpa.fecha.pago");
			CMPA_NUM_COMPROBANTE= rb.getString("desarrollo.comprobante.pago.cmpa.num.comprobante");
			CMPA_NUM_COMP_SECUENCIAL= rb.getString("desarrollo.comprobante.pago.cmpa.num.comp.secuencial");
			CMPA_DESCRIPCION= rb.getString("desarrollo.comprobante.pago.cmpa.descripcion");
			CMPA_TOTAL_PAGO= rb.getString("desarrollo.comprobante.pago.cmpa.total.pago");
			CMPA_TOTAL_FACULTAD= rb.getString("desarrollo.comprobante.pago.cmpa.total.facultad");
			CMPA_PROC_SAU= rb.getString("desarrollo.comprobante.pago.cmpa.proc.sau");
			CMPA_TIPO_UNIDAD= rb.getString("desarrollo.comprobante.pago.cmpa.tipo.unidad");
			CMPA_VALOR_PAGADO= rb.getString("desarrollo.comprobante.pago.cmpa.valor.pagado");
			CMPA_CANTIDAD= rb.getString("desarrollo.comprobante.pago.cmpa.cantidad");
			CMPA_ID_ARANCEL= rb.getString("desarrollo.comprobante.pago.cmpa.id.arancel");
			CMPA_PAI_CODIGO= rb.getString("desarrollo.comprobante.pago.cmpa.pai.codigo");
			CMPA_APLICA_GRATUIDAD= rb.getString("desarrollo.comprobante.pago.cmpa.aplica.gratuidad");
			CMPA_MATR_TIPO= rb.getString("desarrollo.comprobante.pago.cmpa.matr.tipo");
			CMPA_MODALIDAD= rb.getString("desarrollo.comprobante.pago.cmpa.modalidad");
			CMPA_ESPE_CODIGO= rb.getString("desarrollo.comprobante.pago.cmpa.espe.codigo");
			CMPA_FECHA_CADUCA= rb.getString("desarrollo.comprobante.pago.cmpa.fecha.caduca");
			CMPA_FECHA_ENVIO = rb.getString("desarrollo.comprobante.pago.cmpa.fecha.envio");
			CMPA_ESTADO_PAGO = rb.getString("desarrollo.comprobante.pago.cmpa.estado.pago");
			TABLA_CONFIGURACION_CARRERA = rb.getString("desarrollo.configuracion.carrera");
			CNCR_ID = rb.getString("desarrollo.configuracion.carrera.cncr.id");
			CNCR_CRR_ID = rb.getString("desarrollo.configuracion.carrera.crr.id");
			CNCR_VGN_ID = rb.getString("desarrollo.configuracion.carrera.vgn.id");
			CNCR_MDL_ID = rb.getString("desarrollo.configuracion.carrera.mdl.id");
			CNCR_TTL_ID = rb.getString("desarrollo.configuracion.carrera.ttl.id");
			CNCR_UBC_ID = rb.getString("desarrollo.configuracion.carrera.ubc.id");
			CNCR_TISE_ID = rb.getString("desarrollo.configuracion.carrera.tise.id");
			CNCR_TIFR_ID = rb.getString("desarrollo.configuracion.carrera.tifr.id");
			CNCR_DRC_ID = rb.getString("desarrollo.configuracion.carrera.drc.id");
			
			TABLA_CONOCIMIENTO = rb.getString("desarrollo.conocimiento");
			CNC_ID = rb.getString("desarrollo.conocimiento.cnc.id");
			CNC_DESCRIPCION = rb.getString("desarrollo.conocimiento.cnc.descripcion");
			TABLA_CONOCIMIENTO_PUESTO = rb.getString("desarrollo.conocimiento.puesto");
			CNPS_ID = rb.getString("desarrollo.conocimiento.puesto.cnps.id");
			CNPS_PST_ID = rb.getString("desarrollo.conocimiento.puesto.pst.id");
			CNPS_CNC_ID = rb.getString("desarrollo.conocimiento.puesto.cnc.id");
			TABLA_CONTRATO = rb.getString("desarrollo.contrato");
			CNT_ID = rb.getString("desarrollo.contrato.cnt.id");
			CNT_TICN_ID = rb.getString("desarrollo.contrato.ticn.id");
			CNT_TIFN_ID = rb.getString("desarrollo.contrato.tifn.id");
		    TABLA_CONTROL_PROCESO=rb.getString("desarrollo.control.proceso");
		    CNPR_ID= rb.getString("desarrollo.control.proceso.cnpr.id");
			CNPR_TIPR_ID=rb.getString("desarrollo.control.proceso.tipr_id");
			CNPR_USRO_ID=rb.getString("desarrollo.control.proceso.usro_id");
			CNPR_DETALLE_PROCESO=rb.getString("desarrollo.control.proceso.cnpr.detalle.proceso");
			CNPR_TIPO_ACCION=rb.getString("desarrollo.control.proceso.cnpr.tipo.accion");
			CNPR_FECHA_ACCION=rb.getString("desarrollo.control.proceso.cnpr.fecha.accion");
			CNPR_OBSERVACION_ACCION=rb.getString("desarrollo.control.proceso.cnpr.observacion.accion");
			TABLA_CONVALIDACION = rb.getString("desarrollo.convalidacion");
			CNV_ID = rb.getString("desarrollo.convalidacion.cnv.id");
			CNV_MTR_ID = rb.getString("desarrollo.convalidacion.mtr.id");
			CNV_MLCRMT_ID = rb.getString("desarrollo.convalidacion.mlcrmt.id");
			TABLA_COREQUISITO = rb.getString("desarrollo.corequisito");
			CRQ_ID = rb.getString("desarrollo.corequisito.crq.id");
			CRQ_MTR_ID = rb.getString("desarrollo.corequisito.mtr.id");
			CRQ_MTR_COREQUISITO_ID = rb.getString("desarrollo.corequisito.mtr.corequisito.id");
			CRQ_CODIGO = rb.getString("desarrollo.corequisito.crq.codigo");
			CRQ_DESCRIPCION = rb.getString("desarrollo.corequisito.crq.descripcion");
			CRQ_ESTADO = rb.getString("desarrollo.corequisito.crq.estado");
			TABLA_CRONOGRAMA = rb.getString("desarrollo.cronograma");
			CRN_ID = rb.getString("desarrollo.cronograma.crn.id");
			CRN_PRAC_ID = rb.getString("desarrollo.cronograma.prac.id");
			CRN_TIPO = rb.getString("desarrollo.cronograma.crn.tipo");
			CRN_DESCRIPCION = rb.getString("desarrollo.cronograma.crn.descripcion");
			CRN_ESTADO = rb.getString("desarrollo.cronograma.crn.estado");
			TABLA_CRONOGRAMA_PROCESO_FLUJO = rb.getString("desarrollo.cronograma.proceso.flujo");
			CRPRFL_PRFL_ID = rb.getString("desarrollo.cronograma.proceso.flujo.prfl.id");
			CRPRFL_CRN_ID = rb.getString("desarrollo.cronograma.proceso.flujo.crn.id");
			CRPRFL_ID = rb.getString("desarrollo.cronograma.proceso.flujo.crprfl.id");
			CRPRFL_ORDINAL = rb.getString("desarrollo.cronograma.proceso.flujo.crprfl.ordinal");
			TABLA_DEPENDENCIA = rb.getString("desarrollo.dependencia");
			DPN_ID = rb.getString("desarrollo.dependencia.dpn.id");
			DPN_SUB_ID = rb.getString("desarrollo.dependencia.dpn.sub.id");
			DPN_UBC_ID = rb.getString("desarrollo.dependencia.ubc.id");
			DPN_DESCRIPCION = rb.getString("desarrollo.dependencia.dpn.descripcion");
			DPN_JERARQUIA = rb.getString("desarrollo.dependencia.dpn.jerarquia");
			DPN_ESTADO = rb.getString("desarrollo.dependencia.dpn.estado");
			DPN_CAMPUS = rb.getString("desarrollo.dependencia.dpn.campus");
			DPN_COD_SORI = rb.getString("desarrollo.dependencia.dpn.cod.sori");
			DPN_UEJ = rb.getString("desarrollo.dependencia.dpn.uej");
			TABLA_DESTREZA = rb.getString("desarrollo.destreza");
			DST_ID = rb.getString("desarrollo.destreza.dst.id");
			DST_DESCRIPCION = rb.getString("desarrollo.destreza.dst.descripcion");
			TABLA_DESTREZA_PUESTO = rb.getString("desarrollo.destreza.puesto");
			DSPS_ID = rb.getString("desarrollo.destreza.puesto.dsps.id");
			DSPS_DST_ID = rb.getString("desarrollo.destreza.puesto.dst.id");
			DSPS_PST_ID = rb.getString("desarrollo.destreza.puesto.pst.id");
			TABLA_DETALLE_CARGA_HORARIA = rb.getString("desarrollo.detalle.carga.horaria");
			DTCRHR_CARRERA_ID = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.carrera.id");
			DTCRHR_ID = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.id");
			DTCRHR_CRHR_ID = rb.getString("desarrollo.detalle.carga.horaria.crhr.id");
			DTCRHR_FECHA_INICIO = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.fecha.inicio");
			DTCRHR_FECHA_FIN = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.fecha.fin");
			DTCRHR_NUM_PROYECTO = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.num.proyecto");
			DTCRHR_AREA = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.area");
			DTCRHR_SUBAREA = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.subarea");
			DTCRHR_UNIDAD_ACADEMICA = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.unidad.academica");
			DTCRHR_ASIGNATURA_CLINICAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.asignatura.clinicas");
			DTCRHR_NUM_ALUMNOS_CLINICAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.num.alumnos.clinicas");
			DTCRHR_UNIVERSIDAD_DOCTORADO = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.universidad.doctorado");
			DTCRHR_TITULO_DOCTORADO = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.titulo.doctorado");
			DTCRHR_NIVEL_PROYECTO1 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto1");
			DTCRHR_PROYECTO1 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto1");
			DTCRHR_PROYECTO1_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto1.horas");
			DTCRHR_NIVEL_PROYECTO2 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto2");
			DTCRHR_PROYECTO2 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto2");
			DTCRHR_PROYECTO2_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto2.horas");
			DTCRHR_NIVEL_PROYECTO3 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto3");
			DTCRHR_PROYECTO3 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto3");
			DTCRHR_PROYECTO3_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto3.horas");
			DTCRHR_NIVEL_PROYECTO4 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto4");
			DTCRHR_PROYECTO4 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto4");
			DTCRHR_PROYECTO4_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto4.horas");
			DTCRHR_NIVEL_PROYECTO5 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto5");
			DTCRHR_PROYECTO5 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto5");
			DTCRHR_PROYECTO5_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto5.horas");
			DTCRHR_NIVEL_PROYECTO6 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto6");
			DTCRHR_PROYECTO6 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto6");
			DTCRHR_PROYECTO6_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto6.horas");
			DTCRHR_NIVEL_PROYECTO7 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto7");
			DTCRHR_PROYECTO7 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto7");
			DTCRHR_PROYECTO7_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto7.horas");
			DTCRHR_NIVEL_PROYECTO8 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nivel.proyecto8");
			DTCRHR_PROYECTO8 = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto8");
			DTCRHR_PROYECTO8_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.proyecto8.horas");
			DTCRHR_ESTADO = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.estado");
			DTCRHR_ESTADO_ELIMINACION = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.estado.eliminacion");
			DTCRHR_NUM_HORAS = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.num.horas");
			DTCRHR_NOMBRE_PROYECTO = rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.nombre.proyecto");
			DTCRHR_FUNCION= rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.funcion");
			DTCRHR_NUM_ALUMNOS_TITULACION= rb.getString("desarrollo.detalle.carga.horaria.dtcrhr.num.alumnos.titulacion");
			TABLA_DETALLE_MATRICULA = rb.getString("desarrollo.detalle.matricula");
			DTMT_ID = rb.getString("desarrollo.detalle.matricula.dtmt.id");
			DTMT_ARN_ID = rb.getString("desarrollo.detalle.matricula.arn.id");
			DTMT_CMPA_ID = rb.getString("desarrollo.detalle.matricula.cmpa.id");
			DTMT_MLCRPR_ID = rb.getString("desarrollo.detalle.matricula.mlcrpr.id");
			DTMT_NUMERO = rb.getString("desarrollo.detalle.matricula.dtmt.numero");
			DTMT_ESTADO = rb.getString("desarrollo.detalle.matricula.dtmt.estado");
			DTMT_ARCHIVO_ESTUDIANTES = rb.getString("desarrollo.detalle.matricula.dtmt.archivo.estudiantes");
			DTMT_ESTADO_HISTORICO = rb.getString("desarrollo.detalle.matricula.dtmt.estado.historico");
			DTMT_ESTADO_CAMBIO = rb.getString("desarrollo.detalle.matricula.dtmt.estado.cambio");
			DTMT_OBSERVACION_HISTORICO = rb.getString("desarrollo.detalle.matricula.dtmt.observacion.historico");
			DTMT_OBSERVACION_CAMBIO = rb.getString("desarrollo.detalle.matricula.dtmt.observacion.cambio");
			DTMT_VALOR_PARCIAL = rb.getString("desarrollo.detalle.matricula.dtmt.valor.parcial");
			DTMT_FECHA_HISTORICO = rb.getString("desarrollo.detalle.matricula.dtmt.fecha.historico");
			DTMT_FECHA_CAMBIO = rb.getString("desarrollo.detalle.matricula.dtmt.fecha.cambio");
			DTMT_USUARIO = rb.getString("desarrollo.detalle.matricula.dtmt.usuario");
			DTMT_MODIFICACION = rb.getString("desarrollo.detalle.matricula.dtmt.modificacion");
			DTMT_FECHA_SOLICITUD = rb.getString("desarrollo.detalle.matricula.dtmt.fecha.solicitud");
			DTMT_FECHA_RESPUESTA = rb.getString("desarrollo.detalle.matricula.dtmt.fecha.respuesta");
			DTMT_ESTADO_SOLICITUD = rb.getString("desarrollo.detalle.matricula.dtmt.estado.solicitud");
			DTMT_ESTADO_RESPUESTA = rb.getString("desarrollo.detalle.matricula.dtmt.estado.respuesta");
			DTMT_CSL_RETIRO_ID = rb.getString("desarrollo.detalle.matricula.dtmt.csl.retiro.id");
			DTMT_FECHA_VERIFICACION_RETIRO = rb.getString("desarrollo.detalle.matricula.dtmt.fecha.verificacion.retiro");
			DTMT_ARCHIVO_RESPUESTA = rb.getString("desarrollo.detalle.matricula.dtmt.archivo.respuesta");
			DTMT_TIPO_ANULACION = rb.getString("desarrollo.detalle.matricula.dtmt.tipo.anulacion");
			DTMT_FECHA_ANULACION = rb.getString("desarrollo.detalle.matricula.dtmt.fecha.anulacion");
			DTMT_OBSERVACION_ANULACION = rb.getString("desarrollo.detalle.matricula.dtmt.observacion.anulacion");
			DTMT_OBSERVACION_FINAL_RETIRO = rb.getString("desarrollo.detalle.matricula.dtmt.observacion.final.retiro");
			DTMT_ARCHIVO_ANULACION = rb.getString("desarrollo.detalle.matricula.dtmt.archivo.anulacion");
			DTMT_REGISTRANTE_ANULACION = rb.getString("desarrollo.detalle.matricula.dtmt.registrante.anulacion");
			
			TABLA_DETALLE_PUESTO = rb.getString("desarrollo.detalle.puesto");
			DTPS_ID = rb.getString("desarrollo.detalle.puesto.dtps.id");
			DTPS_PST_ID = rb.getString("desarrollo.detalle.puesto.pst.id");
			DTPS_FCES_ID = rb.getString("desarrollo.detalle.puesto.fces.id");
			DTPS_FCDC_ID = rb.getString("desarrollo.detalle.puesto.fcdc.id");
			DTPS_FCEM_ID = rb.getString("desarrollo.detalle.puesto.fcem.id");
			DTPS_RLLB_ID = rb.getString("desarrollo.detalle.puesto.rllb.id");
			DTPS_TIDC_ID = rb.getString("desarrollo.detalle.puesto.tidc.id");
			DTPS_CRR_ID = rb.getString("desarrollo.detalle.puesto.crr.id");
			DTPS_ESTADO = rb.getString("desarrollo.detalle.puesto.dtps.estado");
			DTPS_ESTADO_CATEGORIA = rb.getString("desarrollo.detalle.puesto.dtps.estado.categoria");
			DTPS_PRAC_ID = rb.getString("desarrollo.detalle.puesto.dtps.prac.id");
			DTPS_TIPO_CARRERA = rb.getString("desarrollo.detalle.puesto.dtps.tipo.carrera");
			
			TABLA_DETALLE_TRAMITE_CONTRATO = rb.getString("desarrollo.detalle.tramite.contrato");
			DTTRCN_ID = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.id");
			DTTRCN_DTPS_ID = rb.getString("desarrollo.detalle.tramite.contrato.dtps.id");
			DTTRCN_TRM_ID = rb.getString("desarrollo.detalle.tramite.contrato.trm.id");
			DTTRCN_CNT_ID = rb.getString("desarrollo.detalle.tramite.contrato.cnt.id");
			DTTRCN_SUB_ID = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.sub.id");
			DTTRCN_NUM_TRAMITE = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.num.tramite");
			DTTRCN_ESTADO_TRAMITE = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.estado.tramite");
			DTTRCN_ESTADO_PROCESO = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.estado.proceso");
			DTTRCN_OBSERVACION = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.observacion");
			DTTRCN_FECHA_INICIO = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.fecha.inicio");
			DTTRCN_FECHA_FIN = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.fecha.fin");
			DTTRCN_ARCHIVO_RESOLUCION = rb.getString("desarrollo.detalle.tramite.contrato.dttrcn.archivo.resolucion");
			TABLA_DURACION = rb.getString("desarrollo.duracion");
			DRC_ID = rb.getString("desarrollo.duracion.drc.id");
			DRC_TIPO = rb.getString("desarrollo.duracion.drc.tipo");
			DRC_TIEMPO = rb.getString("desarrollo.duracion.drc.tiempo");
			DRC_TIPO_SNIESE = rb.getString("desarrollo.duracion.drc.tipo.sniese");
			TABLA_EDIFICIO = rb.getString("desarrollo.edificio");
			EDF_ID = rb.getString("desarrollo.edificio.edf.id");
			EDF_DPN_ID = rb.getString("desarrollo.edificio.dpn.id");
			EDF_DESCRIPCION = rb.getString("desarrollo.edificio.edf.descripcion");
			EDF_ESTADO = rb.getString("desarrollo.edificio.edf.estado");
			EDF_CODIGO = rb.getString("desarrollo.edificio.edf.codigo");
			EDF_LOCALIZACION = rb.getString("desarrollo.edificio.edf.localizacion");
			TABLA_ETNIA = rb.getString("desarrollo.etnia");
			ETN_ID = rb.getString("desarrollo.etnia.etn.id");
			ETN_CODIGO_SNIESE = rb.getString("desarrollo.etnia.etn.codigo.sniese");
			ETN_DESCRIPCION = rb.getString("desarrollo.etnia.etn.descripcion");
			TABLA_EXCEDENTE = rb.getString("desarrollo.excedente");
			EXC_ID = rb.getString("desarrollo.excedente.exc.id");
			EXC_DESCRIPCION = rb.getString("desarrollo.excedente.exc.descripcion");
			EXC_ESTADO = rb.getString("desarrollo.excedente.exc.estado");
			EXC_TIPO = rb.getString("desarrollo.excedente.exc.tipo");
			TABLA_EXPERIENCIA_LABORAL_CONTR = rb.getString("desarrollo.experiencia.laboral.contr");
			EXLBCN_CNCN_ID = rb.getString("desarrollo.experiencia.laboral.contr.cncn.id");
			EXLBCN_ID = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.id");
			EXLBCN_TIPO_INSTITUCION = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.tipo.institucion");
			EXLBCN_DESCRIPCION = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.descripcion");
			EXLBCN_UNIDAD = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.unidad");
			EXLBCN_PUESTO = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.puesto");
			EXLBCN_FECHA_INGRESO = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.fecha.ingreso");
			EXLBCN_FECHA_SALIDA = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.fecha.salida");
			EXLBCN_MOTIVO_INGRESO = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.motivo.ingreso");
			EXLBCN_MOTIVO_SALIDA = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.motivo.salida");
			EXLBCN_TIPO = rb.getString("desarrollo.experiencia.laboral.contr.exlbcn.tipo");
			TABLA_FICHA_DOCENTE = rb.getString("desarrollo.ficha.docente");
			FCDC_ESTADO = rb.getString("desarrollo.ficha.docente.fcdc.estado");
			FCDC_ID = rb.getString("desarrollo.ficha.docente.fcdc.id");
			FCDC_PRS_ID = rb.getString("desarrollo.ficha.docente.prs.id");
			TABLA_FICHA_EMPLEADO = rb.getString("desarrollo.ficha.empleado");
			FCEM_ID = rb.getString("desarrollo.ficha.empleado.fcem.id");
			FCEM_PRS_ID = rb.getString("desarrollo.ficha.empleado.prs.id");
			TABLA_FICHA_ESTUDIANTE = rb.getString("desarrollo.ficha.estudiante");
			FCES_ID = rb.getString("desarrollo.ficha.estudiante.fces.id");
			FCES_FECHA_INICIO = rb.getString("desarrollo.ficha.estudiante.fces.fecha.inicio");
			FCES_FECHA_EGRESAMIENTO = rb.getString("desarrollo.ficha.estudiante.fces.fecha.egresamiento");
			FCES_FECHA_ACTA_GRADO = rb.getString("desarrollo.ficha.estudiante.fces.fecha.acta.grado");
			FCES_NUM_ACTA_GRADO = rb.getString("desarrollo.ficha.estudiante.fces.num.acta.grado");
			FCES_FECHA_REFRENDACION = rb.getString("desarrollo.ficha.estudiante.fces.fecha.refrendacion");
			FCES_NUM_REFRENDACION = rb.getString("desarrollo.ficha.estudiante.fces.num.refrendacion");
			FCES_CRR_ESTUD_PREVIOS = rb.getString("desarrollo.ficha.estudiante.fces.crr.estud.previos");
			FCES_TIEMPO_ESTUD_REC = rb.getString("desarrollo.ficha.estudiante.fces.tiempo.estud.rec");
			FCES_TIPO_DURAC_REC = rb.getString("desarrollo.ficha.estudiante.fces.tipo.durac.rec");
			FCES_TIPO_COLEGIO = rb.getString("desarrollo.ficha.estudiante.fces.tipo.colegio");
			FCES_TIPO_COLEGIO_SNIESE = rb.getString("desarrollo.ficha.estudiante.fces.tipo.colegio.sniese");
			FCES_NOTA_PROM_ACUMULADO = rb.getString("desarrollo.ficha.estudiante.fces.nota.prom.acumulado");
			FCES_NOTA_TRAB_TITULACION = rb.getString("desarrollo.ficha.estudiante.fces.nota.trab.titulacion");
			FCES_LINK_TESIS = rb.getString("desarrollo.ficha.estudiante.fces.link.tesis");
			FCES_REC_ESTUD_PREVIOS = rb.getString("desarrollo.ficha.estudiante.fces.rec.estud.previos");
			FCES_REC_ESTUD_PREV_SNIESE = rb.getString("desarrollo.ficha.estudiante.fces.rec.estud.prev.sniese");
			FCES_FECHA_CREACION = rb.getString("desarrollo.ficha.estudiante.fces.fecha.creacion");
			FCES_PRS_ID = rb.getString("desarrollo.ficha.estudiante.prs.id");
			FCES_INAC_ID = rb.getString("desarrollo.ficha.estudiante.inac.id");
			FCES_FCIN_ID = rb.getString("desarrollo.ficha.estudiante.fcin.id");
			FCES_ESTADO_UNIVERSITARIO = rb.getString("desarrollo.ficha.estudiante.fces.estado.universitario");
			FCES_ESTADO_MATRICULA = rb.getString("desarrollo.ficha.estudiante.fces.estado.matricula");
			FCES_OBSERVACION = rb.getString("desarrollo.ficha.estudiante.fces.observacion");
			FCES_TITULO_BACHILLER = rb.getString("desarrollo.ficha.estudiante.fces.titulo.bachiller");
			FCES_NOTA_GRADO_SECUNDARIA = rb.getString("desarrollo.ficha.estudiante.fces.nota.grado.secundaria");
			FCES_INAC_TTL_ID = rb.getString("desarrollo.ficha.estudiante.fces.inac.ttl.id");
			FCES_COLEGIO_ID = rb.getString("desarrollo.ficha.estudiante.fces.colegio.id");
			FCES_TIPO_UNIV_ESTUD_PREV = rb.getString("desarrollo.ficha.estudiante.fces.tipo.univ.estud.prev");
			FCES_ESTADO_ESTUD_PREV = rb.getString("desarrollo.ficha.estudiante.fces.estado.estud.prev");
			FCES_UBC_COLEGIO = rb.getString("desarrollo.ficha.estudiante.fces.ubc.colegio");
			FCES_FECHA_ACTUALIZACION_DATOS = rb.getString("desarrollo.ficha.estudiante.fces.fecha.actualizacion.datos");
			FCES_UNIV_ESTUD_PREV_ID= rb.getString("desarrollo.ficha.estudiante.fces.univ.estud.prev.id");
			FCES_TIT_ESTUD_PREV_ID = rb.getString("desarrollo.ficha.estudiante.fces.tit.estud.pre.id");
			FCES_REG_TITULO_PREV = rb.getString("desarrollo.ficha.estudiante.fces.reg.titulo.prev");
					
			TABLA_FICHA_INSCRIPCION = rb.getString("desarrollo.ficha.inscripcion");
			FCIN_ID = rb.getString("desarrollo.ficha.inscripcion.fcin.id");
			FCIN_USRO_ID = rb.getString("desarrollo.ficha.inscripcion.usro.id");
			FCIN_CNCR_ID = rb.getString("desarrollo.ficha.inscripcion.cncr.id");
			FCIN_PRAC_ID = rb.getString("desarrollo.ficha.inscripcion.prac.id");
			FCIN_FECHA_INSCRIPCION = rb.getString("desarrollo.ficha.inscripcion.fcin.fecha.inscripcion");
			FCIN_OBSERVACION = rb.getString("desarrollo.ficha.inscripcion.fcin.observacion");
			FCIN_OBSERVACION_INGRESO = rb.getString("desarrollo.ficha.inscripcion.fcin.observacion.ingreso");
			FCIN_TIPO = rb.getString("desarrollo.ficha.inscripcion.fcin.tipo");
			FCIN_TIPO_INGRESO = rb.getString("desarrollo.ficha.inscripcion.fcin.tipo.ingreso");
			FCIN_ANIO_ABANDONA_CARRERA = rb.getString("desarrollo.ficha.inscripcion.fcin.anio.abandona.carrera");
			FCIN_CRR_ANTERIOR_ID=rb.getString("desarrollo.ficha.inscripcion.fcin.crr.anterior.id");
			FCIN_ESTADO_INGRESO = rb.getString("desarrollo.ficha.inscripcion.fcin.estado.ingreso");
			FCIN_DOCUMENTO_INGRESO = rb.getString("desarrollo.ficha.inscripcion.fcin.documento.ingreso");
			FCIN_MATRICULADO = rb.getString("desarrollo.ficha.inscripcion.fcin.matriculado");
			FCIN_ESTADO = rb.getString("desarrollo.ficha.inscripcion.fcin.estado");
			FCIN_NIVEL_INGRESO = rb.getString("desarrollo.ficha.inscripcion.fcin.nivel.ingreso");
			FCIN_ENCUESTA = rb.getString("desarrollo.ficha.inscripcion.fcin.encuesta");
			FCIN_NOTA_ENES = rb.getString("desarrollo.ficha.inscripcion.fcin.nota.enes");
			FCIN_CARRERA = rb.getString("desarrollo.ficha.inscripcion.fcin.carrera");
			FCIN_CARRERA_SIIU = rb.getString("desarrollo.ficha.inscripcion.fcin.carrera.siiu");
			FCIN_CNCR_AREA = rb.getString("desarrollo.ficha.inscripcion.fcin.cncr.area");
			FCIN_FCIN_NIVELACION = rb.getString("desarrollo.ficha.inscripcion.fcin.fcin.nivelacion");
			FCIN_CNCR_ID_NIVELACION = rb.getString("desarrollo.ficha.inscripcion.fcin.cncr.id.nivelacion");
			FCIN_FCIN_ANTERIOR_ID=rb.getString("desarrollo.ficha.inscripcion.fcin.fcin.anterior.id");	
			FCIN_NOTA_UBICACION=rb.getString("desarrollo.ficha.inscripcion.fcin.nota.ubicacion");
			FCIN_ESTADO_RETIRO=rb.getString("desarrollo.ficha.inscripcion.fcin.estado.retiro");
			FCIN_REINICIO_ORIGEN=rb.getString("desarrollo.ficha.inscripcion.fcin.reinicio.origen");
			FCIN_VIGENTE=rb.getString("desarrollo.ficha.inscripcion.fcin.vigente");
			FCIN_APLICA_NOTA_ENES=rb.getString("desarrollo.ficha.inscripcion.fcin.aplica.nota.enes");
			FCIN_NOTA_CORTE_ID=rb.getString("desarrollo.ficha.inscripcion.fcin.nota.corte.id");			
			TABLA_FICHA_MATRICULA = rb.getString("desarrollo.ficha.matricula");
			FCMT_PRAC_ID = rb.getString("desarrollo.ficha.matricula.fcmt.prac.id");
			FCMT_ID = rb.getString("desarrollo.ficha.matricula.fcmt.id");
			FCMT_FCES_ID = rb.getString("desarrollo.ficha.matricula.fces.id");
			FCMT_PLCR_ID = rb.getString("desarrollo.ficha.matricula.plcr.id");
			FCMT_NIVEL_UBICACION = rb.getString("desarrollo.ficha.matricula.fcmt.nivel.ubicacion");
			FCMT_ESTADO = rb.getString("desarrollo.ficha.matricula.fcmt.estado");
			FCMT_FECHA_CONVALIDACION = rb.getString("desarrollo.ficha.matricula.fcmt.fecha.convalidacion");
			FCMT_TIPO = rb.getString("desarrollo.ficha.matricula.fcmt.tipo");
			FCMT_MODALIDAD = rb.getString("desarrollo.ficha.matricula.fcmt.modalidad");
			FCMT_VALOR_TOTAL = rb.getString("desarrollo.ficha.matricula.fcmt.valor.total");
			FCMT_FECHA_MATRICULA = rb.getString("desarrollo.ficha.matricula.fcmt.fecha.matricula");
			FCMT_FECHA_ESTADO = rb.getString("desarrollo.ficha.matricula.fcmt.fecha.estado");
			TABLA_FORMATO_CONTRATO = rb.getString("desarrollo.formato.contrato");
			FRCN_ID = rb.getString("desarrollo.formato.contrato.frcn.id");
			FRCN_TICN_ID = rb.getString("desarrollo.formato.contrato.ticn.id");
			FRCN_DESCRIPCON = rb.getString("desarrollo.formato.contrato.frcn.descripcon");
			FRCN_ESTADO = rb.getString("desarrollo.formato.contrato.frcn.estado");
			TABLA_FUNCION = rb.getString("desarrollo.funcion");
			FNC_ID = rb.getString("desarrollo.funcion.fnc.id");
			FNC_DESCRIPCION = rb.getString("desarrollo.funcion.fnc.descripcion");
			TABLA_FUNCION_CARGA_HORARIA = rb.getString("desarrollo.funcion.carga.horaria");
			FNCRHR_ID = rb.getString("desarrollo.funcion.carga.horaria.fncrhr.id");
			FNCRHR_DESCRIPCION = rb.getString("desarrollo.funcion.carga.horaria.fncrhr.descripcion");
			FNCRHR_HORAS_ASIGNADAS = rb.getString("desarrollo.funcion.carga.horaria.fncrhr.horas.asignadas");
			FNCRHR_HORAS_MINIMO = rb.getString("desarrollo.funcion.carga.horaria.fncrhr.horas.minimo");
			FNCRHR_HORAS_MAXIMO = rb.getString("desarrollo.funcion.carga.horaria.fncrhr.horas.maximo");
			TABLA_FUNCION_PUESTO = rb.getString("desarrollo.funcion.puesto");
			FNPS_ID = rb.getString("desarrollo.funcion.puesto.fnps.id");
			FNPS_PST_ID = rb.getString("desarrollo.funcion.puesto.pst.id");
			FNPS_FNC_ID = rb.getString("desarrollo.funcion.puesto.fnc.id");
			TABLA_GRATUIDAD = rb.getString("desarrollo.gratuidad");
			GRT_ID = rb.getString("desarrollo.gratuidad.grt.id");
			GRT_FCES_ID = rb.getString("desarrollo.gratuidad.fces.id");
			GRT_TIGR_ID = rb.getString("desarrollo.gratuidad.tigr.id");
			GRT_FCMT_ID = rb.getString("desarrollo.gratuidad.fcmt.id");
			GRT_ESTADO = rb.getString("desarrollo.gratuidad.grt.estado");
			TABLA_GRUPO_OCUPACIONAL = rb.getString("desarrollo.grupo.ocupacional");
			GROC_ID = rb.getString("desarrollo.grupo.ocupacional.groc.id");
			GROC_DESCRIPCION = rb.getString("desarrollo.grupo.ocupacional.groc.descripcion");
			GROC_GRADO = rb.getString("desarrollo.grupo.ocupacional.groc.grado");
			GROC_RMU = rb.getString("desarrollo.grupo.ocupacional.groc.rmu");
			GROC_RGM_ID = rb.getString("desarrollo.grupo.ocupacional.rgm.id");
			GROC_RMU_HORA = rb.getString("desarrollo.grupo.ocupacional.groc.rmu.hora");
			TABLA_HISTORIAL_NOTA_CORTE = rb.getString("desarrollo.historial.nota.corte");
			HSNOCR_ID = rb.getString("desarrollo.historial.nota.corte.hsnocr.id");
			HSNOCR_FECHA_REGISTRO = rb.getString("desarrollo.historial.nota.corte.hsnocr.fecha.registro");
			HSNOCR_USR_ID = rb.getString("desarrollo.historial.nota.corte.hsnocr.usr.id");
			HSNOCR_USR_NICK = rb.getString("desarrollo.historial.nota.corte.hsnocr.usr.nick");
			HSNOCR_TIPO_PROCESO = rb.getString("desarrollo.historial.nota.corte.hsnocr.tipo.proceso");
			HSNOCR_NOTA_ANTERIOR = rb.getString("desarrollo.historial.nota.corte.hsnocr.nota.anterior");
			TABLA_HORA_CLASE = rb.getString("desarrollo.hora.clase");
			HOCL_ID = rb.getString("desarrollo.hora.clase.hocl.id");
			HOCL_DESCRIPCION = rb.getString("desarrollo.hora.clase.hocl.descripcion");
			HOCL_ESTADO = rb.getString("desarrollo.hora.clase.hocl.estado");
			HOCL_HORA_INICIO = rb.getString("desarrollo.hora.clase.hocl.hora.inicio");
			HOCL_HORA_FIN = rb.getString("desarrollo.hora.clase.hocl.hora.fin");
			TABLA_HORA_CLASE_AULA = rb.getString("desarrollo.hora.clase.aula");
			HOCLAL_ID = rb.getString("desarrollo.hora.clase.aula.hoclal.id");
			HOCLAL_HOCL_ID = rb.getString("desarrollo.hora.clase.aula.hocl.id");
			HOCLAL_ALA_ID = rb.getString("desarrollo.hora.clase.aula.ala.id");
			HOCLAL_ESTADO = rb.getString("desarrollo.hora.clase.aula.hoclal.estado");
			TABLA_HORARIO_ACADEMICO = rb.getString("desarrollo.horario.academico");
			HRAC_ID = rb.getString("desarrollo.horario.academico.hrac.id");
			HRAC_DESCRIPCION = rb.getString("desarrollo.horario.academico.hrac.descripcion");
			HRAC_DIA = rb.getString("desarrollo.horario.academico.hrac.dia");
			HRAC_HORA_INICIO = rb.getString("desarrollo.horario.academico.hrac.hora.inicio");
			HRAC_HORA_FIN = rb.getString("desarrollo.horario.academico.hrac.hora.fin");
			HRAC_ESTADO = rb.getString("desarrollo.horario.academico.hrac.estado");
			HRAC_HOCLAL_ID = rb.getString("desarrollo.horario.academico.hoclal.id");
			HRAC_MLCRPR_ID = rb.getString("desarrollo.horario.academico.mlcrpr.id");
			MLCRPR_ID_COMP = rb.getString("desarrollo.horario.academico.mlcrpr.id.comp");
			TABLA_INFORME_TECNICO = rb.getString("desarrollo.informe.tecnico");
			INTC_ID = rb.getString("desarrollo.informe.tecnico.intc.id");
			INTC_PRTRCN_ID = rb.getString("desarrollo.informe.tecnico.prtrcn.id");
			INTC_OBSERVACION = rb.getString("desarrollo.informe.tecnico.intc.observacion");
			INTC_ESTADO = rb.getString("desarrollo.informe.tecnico.intc.estado");
			TABLA_INSTITUCION_ACADEMICA = rb.getString("desarrollo.institucion.academica");
			INAC_ID = rb.getString("desarrollo.institucion.academica.inac.id");
			INAC_CODIGO_SNIESE = rb.getString("desarrollo.institucion.academica.inac.codigo.sniese");
			INAC_DESCRIPCION = rb.getString("desarrollo.institucion.academica.inac.descripcion");
			INAC_NIVEL = rb.getString("desarrollo.institucion.academica.inac.nivel");
			INAC_TIPO = rb.getString("desarrollo.institucion.academica.inac.tipo");
			INAC_TIPO_SNIESE = rb.getString("desarrollo.institucion.academica.inac.tipo.sniese");
			INAC_UBC_ID = rb.getString("desarrollo.institucion.academica.ubc.id");
			TABLA_INSTRUCCION_PUESTO = rb.getString("desarrollo.instruccion.puesto");
			INPS_ID = rb.getString("desarrollo.instruccion.puesto.inps.id");
			INPS_NVIN_ID = rb.getString("desarrollo.instruccion.puesto.nvin.id");
			INPS_PST_ID = rb.getString("desarrollo.instruccion.puesto.pst.id");
			INPS_ANIO = rb.getString("desarrollo.instruccion.puesto.inps.anio");
			INPS_MES = rb.getString("desarrollo.instruccion.puesto.inps.mes");
			TABLA_ITINERARIO = rb.getString("desarrollo.itinerario");
			ITN_ID = rb.getString("desarrollo.itinerario.itn.id");
			ITN_CRR_ID = rb.getString("desarrollo.itinerario.crr.id");
			ITN_DESCRIPCION = rb.getString("desarrollo.itinerario.itn.descripcion");
			ITN_ESTADO = rb.getString("desarrollo.itinerario.itn.estado");
			ITN_OBSERVACION = rb.getString("desarrollo.itinerario.itn.observacion");
			TABLA_ITINERARIO_MALLA_MATERIA = rb.getString("desarrollo.itinerario.malla.materia");
			ITMLMT_ID = rb.getString("desarrollo.itinerario.malla.materia.itmlmt.id");
			ITMLMT_ESTADO = rb.getString("desarrollo.itinerario.malla.materia.itmlmt.estado");
			ITMLMT_MLCRMT_ID = rb.getString("desarrollo.itinerario.malla.materia.mlcrmt.id");
			ITMLMT_ITN_ID = rb.getString("desarrollo.itinerario.malla.materia.itn.id");
			TABLA_MALLA_CURRICULAR = rb.getString("desarrollo.malla.curricular");
			MLCR_ID = rb.getString("desarrollo.malla.curricular.mlcr.id");
			MLCR_TIFRML_ID = rb.getString("desarrollo.malla.curricular.tifrml.id");
			MLCR_CRR_ID = rb.getString("desarrollo.malla.curricular.crr.id");
			MLCR_CODIGO = rb.getString("desarrollo.malla.curricular.mlcr.codigo");
			MLCR_DESCRIPCION = rb.getString("desarrollo.malla.curricular.mlcr.descripcion");
			MLCR_ESTADO = rb.getString("desarrollo.malla.curricular.mlcr.estado");
			MLCR_FECHA_INICIO = rb.getString("desarrollo.malla.curricular.mlcr.fecha.inicio");
			MLCR_FECHA_FIN = rb.getString("desarrollo.malla.curricular.mlcr.fecha.fin");
			MLCR_TOTAL_HORAS = rb.getString("desarrollo.malla.curricular.mlcr.total.horas");
			MLCR_TOTAL_MATERIAS = rb.getString("desarrollo.malla.curricular.mlcr.total.materias");
			MLCR_TIPO_ORG_APRENDIZAJE = rb.getString("desarrollo.malla.curricular.mlcr.tipo.org.aprendizaje");
			MLCR_VIGENTE = rb.getString("desarrollo.malla.curricular.mlcr.vigente");
			MLCR_TIPO_APROBACION = rb.getString("desarrollo.malla.curricular.mlcr.tipo.aprobacion");
			
			TABLA_MALLA_CURRICULAR_NIVEL = rb.getString("desarrollo.malla.curricular.nivel");
			MLCRNV_ID = rb.getString("desarrollo.malla.curricular.nivel.mlcrnv.id");
			MLCRNV_CREDITOS = rb.getString("desarrollo.malla.curricular.nivel.mlcrnv.creditos");
			MLCRNV_CREDITOS_ACUMULADO = rb.getString("desarrollo.malla.curricular.nivel.mlcrnv.creditos.acumulado");
			
			TABLA_MALLA_CURRICULAR_MATERIA = rb.getString("desarrollo.malla.curricular.materia");
			MLCRMT_ID = rb.getString("desarrollo.malla.curricular.materia.mlcrmt.id");
			MLCRMT_MLCR_ID = rb.getString("desarrollo.malla.curricular.materia.mlcr.id");
			MLCRMT_MTR_ID = rb.getString("desarrollo.malla.curricular.materia.mtr.id");
			MLCRMT_UNFR_ID = rb.getString("desarrollo.malla.curricular.materia.unfr.id");
			MLCRMT_NVL_ID = rb.getString("desarrollo.malla.curricular.materia.nvl.id");
			MLCRMT_NVL_SUB_ID = rb.getString("desarrollo.malla.curricular.materia.nvl.sub.id");
			MLCRMT_ESTADO = rb.getString("desarrollo.malla.curricular.materia.mlcrmt.estado");
			
			TABLA_MALLA_CURRICULAR_PARALELO = rb.getString("desarrollo.malla.curricular.paralelo");
			MLCRPR_ID = rb.getString("desarrollo.malla.curricular.paralelo.mlcrpr.id");
			MLCRPR_PRL_ID = rb.getString("desarrollo.malla.curricular.paralelo.prl.id");
			MLCRPR_MLCRMT_ID = rb.getString("desarrollo.malla.curricular.paralelo.mlcrmt.id");
			MLCRPR_INSCRITOS = rb.getString("desarrollo.malla.curricular.paralelo.mlcrpr.inscritos");
			MLCRPR_CUPO = rb.getString("desarrollo.malla.curricular.paralelo.mlcrpr.cupo");
			MLCRPR_RESERVA_REPETIDOS = rb.getString("desarrollo.malla.curricular.paralelo.mlcrpr.reserva.repetidos");
			MLCRPR_NIVELACION_CRR_ID = rb.getString("desarrollo.malla.curricular.paralelo.mlcrpr.nivelacion.crr.id");
			MLCRPR_MODALIDAD = rb.getString("desarrollo.malla.curricular.paralelo.mlcrpr.modalidad");
			
			TABLA_MALLA_PERIODO = rb.getString("desarrollo.malla.periodo");
			MLPR_ID = rb.getString("desarrollo.malla.periodo.mlpr.id");
			MLPR_MLCR_ID = rb.getString("desarrollo.malla.periodo.mlcr.id");
			MLPR_PRAC_ID = rb.getString("desarrollo.malla.periodo.prac.id");
			MLPR_ESTADO = rb.getString("desarrollo.malla.periodo.mlpr.estado");
			
			TABLA_MATERIA = rb.getString("desarrollo.materia");
			MTR_UNIDAD_MEDIDA = rb.getString("desarrollo.materia.mtr.unidad.medida");
			MTR_CREDITOS = rb.getString("desarrollo.materia.mtr.creditos");
			MTR_ID = rb.getString("desarrollo.materia.mtr.id");
			MTR_CMFR_ID = rb.getString("desarrollo.materia.cmfr.id");
			MTR_TIMT_ID = rb.getString("desarrollo.materia.timt.id");
			MTR_SUB_ID = rb.getString("desarrollo.materia.mtr.sub.id");
			MTR_NCPRCR_ID = rb.getString("desarrollo.materia.ncprcr.id");
			MTR_CRR_ID = rb.getString("desarrollo.materia.crr.id");
			MTR_CODIGO = rb.getString("desarrollo.materia.mtr.codigo");
			MTR_CODIGO_SNIESE = rb.getString("desarrollo.materia.mtr.codigo.sniese");
			MTR_DESCRIPCION = rb.getString("desarrollo.materia.mtr.descripcion");
			MTR_ESTADO = rb.getString("desarrollo.materia.mtr.estado");
			MTR_HORAS = rb.getString("desarrollo.materia.mtr.horas");
			MTR_INTEGRADORA_HORAS = rb.getString("desarrollo.materia.mtr.integradora.horas");
			MTR_PRE_PROFESIONAL_HORAS = rb.getString("desarrollo.materia.mtr.pre.profesional.horas");
			MTR_HORAS_CIEN = rb.getString("desarrollo.materia.mtr.horas.cien");
			MTR_RELACION_TRABAJO = rb.getString("desarrollo.materia.mtr.relacion.trabajo");
			MTR_HORAS_PRACTICAS = rb.getString("desarrollo.materia.mtr.horas.practicas");
			MTR_HORAS_TRAB_AUTONOMO = rb.getString("desarrollo.materia.mtr.horas.trab.autonomo");
			MTR_HORAS_PRAC_SEMA = rb.getString("desarrollo.materia.mtr.horas.prac.sema");
			MTR_HORAS_AUTONO_SEMA = rb.getString("desarrollo.materia.mtr.horas.autono.sema");
			TABLA_MODALIDAD = rb.getString("desarrollo.modalidad");
			MDL_ID = rb.getString("desarrollo.modalidad.mdl.id");
			MDL_DESCRIPCION = rb.getString("desarrollo.modalidad.mdl.descripcion");
			TABLA_NOTA_CORTE = rb.getString("desarrollo.nota.corte");
			NOCR_ID = rb.getString("desarrollo.nota.corte.nocr.id");
			NOCR_ESTADO = rb.getString("desarrollo.nota.corte.nocr.estado");
			NOCR_NOTA = rb.getString("desarrollo.nota.corte.nocr.nota");
			NOCR_PRAC_ID = rb.getString("desarrollo.nota.corte.nocr.prac.id");
			NOCR_CRR_ID = rb.getString("desarrollo.nota.corte.nocr.crr.id");
			TABLA_NIVEL = rb.getString("desarrollo.nivel");
			NVL_ID = rb.getString("desarrollo.nivel.nvl.id");
			NVL_DESCRIPCION = rb.getString("desarrollo.nivel.nvl.descripcion");
			NVL_ESTADO = rb.getString("desarrollo.nivel.nvl.estado");
			NVL_NUMERAL = rb.getString("desarrollo.nivel.nvl.numeral");
			TABLA_NIVEL_APERTURA = rb.getString("desarrollo.nivel.apertura");
			NVAP_ID = rb.getString("desarrollo.nivel.apertura.nvap.id");
			NVAP_DESCRIPCION = rb.getString("desarrollo.nivel.apertura.nvap.descripcion");
			TABLA_NIVEL_COMPETENCIA = rb.getString("desarrollo.nivel.competencia");
			NVCM_ID = rb.getString("desarrollo.nivel.competencia.nvcm.id");
			NVCM_CMP_ID = rb.getString("desarrollo.nivel.competencia.cmp.id");
			NVCM_DESCRIPCION = rb.getString("desarrollo.nivel.competencia.nvcm.descripcion");
			NVCM_COMPORTAMIENTO = rb.getString("desarrollo.nivel.competencia.nvcm.comportamiento");
			TABLA_NIVEL_FORMACION = rb.getString("desarrollo.nivel.formacion");
			NVFR_ID = rb.getString("desarrollo.nivel.formacion.nvfr.id");
			NVFR_RGAC_ID = rb.getString("desarrollo.nivel.formacion.rgac.id");
			NVFR_DESCRIPCION = rb.getString("desarrollo.nivel.formacion.nvfr.descripcion");
			TABLA_NIVEL_INSTRUCCION = rb.getString("desarrollo.nivel.instruccion");
			NVIN_ID = rb.getString("desarrollo.nivel.instruccion.nvin.id");
			NVIN_DESCRIPCION = rb.getString("desarrollo.nivel.instruccion.nvin.descripcion");
			TABLA_NUCLEO_PROBLEMICO = rb.getString("desarrollo.nucleo.problemico");
			NCPR_ID = rb.getString("desarrollo.nucleo.problemico.ncpr.id");
			NCPR_DESCRIPCION = rb.getString("desarrollo.nucleo.problemico.ncpr.descripcion");
			NCPR_ESTADO = rb.getString("desarrollo.nucleo.problemico.ncpr.estado");
			TABLA_NUCLEO_PROBLEMICO_CARRERA = rb.getString("desarrollo.nucleo.problemico.carrera");
			NCPRCR_ID = rb.getString("desarrollo.nucleo.problemico.carrera.ncprcr.id");
			NCPRCR_CRR_ID = rb.getString("desarrollo.nucleo.problemico.carrera.crr.id");
			NCPRCR_ESTADO = rb.getString("desarrollo.nucleo.problemico.carrera.ncprcr.estado");
			NCPRCR_NCPR_ID = rb.getString("desarrollo.nucleo.problemico.carrera.ncpr.id");
			TABLA_PARALELO = rb.getString("desarrollo.paralelo");
			PRL_ID = rb.getString("desarrollo.paralelo.prl.id");
			PRL_CRR_ID = rb.getString("desarrollo.paralelo.crr.id");
			PRL_PRAC_ID = rb.getString("desarrollo.paralelo.prac.id");
			PRL_CODIGO = rb.getString("desarrollo.paralelo.prl.codigo");
			PRL_DESCRIPCION = rb.getString("desarrollo.paralelo.prl.descripcion");
			PRL_ESTADO = rb.getString("desarrollo.paralelo.prl.estado");
			PRL_CUPO = rb.getString("desarrollo.paralelo.prl.cupo");
			PRL_FECHA = rb.getString("desarrollo.paralelo.prl.fecha");
			PRL_INICIO_CLASE = rb.getString("desarrollo.paralelo.prl.inicio.clase");
			PRL_FIN_CLASE = rb.getString("desarrollo.paralelo.prl.fin.clase");
			TABLA_PERIODO_ACADEMICO = rb.getString("desarrollo.periodo.academico");
			PRAC_ID = rb.getString("desarrollo.periodo.academico.prac.id");
			PRAC_DESCRIPCION = rb.getString("desarrollo.periodo.academico.prac.descripcion");
			PRAC_ESTADO = rb.getString("desarrollo.periodo.academico.prac.estado");
			PRAC_FECHA_INCIO = rb.getString("desarrollo.periodo.academico.prac.fecha.incio");
			PRAC_FECHA_FIN = rb.getString("desarrollo.periodo.academico.prac.fecha.fin");
			PRAC_TIPO = rb.getString("desarrollo.periodo.academico.prac.tipo");
			TABLA_PERSONA = rb.getString("desarrollo.persona");
			PRS_MAIL_INSTITUCIONAL = rb.getString("desarrollo.persona.prs.mail.institucional");
			PRS_TELEFONO = rb.getString("desarrollo.persona.prs.telefono");
			PRS_FECHA_NACIMIENTO = rb.getString("desarrollo.persona.prs.fecha.nacimiento");
			PRS_ETN_ID = rb.getString("desarrollo.persona.etn.id");
			PRS_UBC_NACIMIENTO = rb.getString("desarrollo.persona.ubc.nacimiento");
			PRS_UBC_RESIDENCIA = rb.getString("desarrollo.persona.ubc.residencia");
			PRS_SECTOR_DOMICILIO = rb.getString("desarrollo.persona.prs.sector.domicilio");
			PRS_CALLE_PRINCIPAL = rb.getString("desarrollo.persona.prs.calle.principal");
			PRS_CALLE_SECUNDARIA = rb.getString("desarrollo.persona.prs.calle.secundaria");
			PRS_NUMERO_CASA = rb.getString("desarrollo.persona.prs.numero.casa");
			PRS_REFERENCIA_DOMICILIO = rb.getString("desarrollo.persona.prs.referencia.domicilio");
			PRS_CELULAR = rb.getString("desarrollo.persona.prs.celular");
			PRS_ID = rb.getString("desarrollo.persona.prs.id");
			PRS_TIPO_IDENTIFICACION = rb.getString("desarrollo.persona.prs.tipo.identificacion");
			PRS_TIPO_IDENTIFICACION_SNIESE = rb.getString("desarrollo.persona.prs.tipo.identificacion.sniese");
			PRS_IDENTIFICACION = rb.getString("desarrollo.persona.prs.identificacion");
			PRS_PRIMER_APELLIDO = rb.getString("desarrollo.persona.prs.primer.apellido");
			PRS_SEGUNDO_APELLIDO = rb.getString("desarrollo.persona.prs.segundo.apellido");
			PRS_NOMBRES = rb.getString("desarrollo.persona.prs.nombres");
			PRS_SEXO = rb.getString("desarrollo.persona.prs.sexo");
			PRS_SEXO_SNIESE = rb.getString("desarrollo.persona.prs.sexo.sniese");
			PRS_MAIL_PERSONAL = rb.getString("desarrollo.persona.prs.mail.personal");
			PRS_DISCAPACIDAD = rb.getString("desarrollo.persona.prs.discapacidad");
			PRS_TIPO_DISCAPACIDAD = rb.getString("desarrollo.persona.prs.tipo.discapacidad");
			PRS_PORCE_DISCAPACIDAD = rb.getString("desarrollo.persona.prs.porce.discapacidad");
			PRS_CARNET_CONADIS = rb.getString("desarrollo.persona.prs.carnet.conadis");
			PRS_NUM_CARNET_CONADIS = rb.getString("desarrollo.persona.prs.num.carnet.conadis");
			PRS_ESTADO_CIVIL = rb.getString("desarrollo.persona.prs.estado.civil");
			PRS_FECHA_ACTUALIZACION_DATOS = rb.getString("desarrollo.persona.prs.fecha.actualizacion.datos");
			PRS_FECHA_VINCULACION_SEGURO = rb.getString("desarrollo.persona.prs.fecha.vinculacion.seguro");
			PRS_FECHA_REGISTRO_SEGURO = rb.getString("desarrollo.persona.prs.fecha.registro.seguro");
			PRS_ESTADO_SEGURO = rb.getString("desarrollo.persona.prs.estado.seguro");
			PRS_FORMULARIO_SEGURO = rb.getString("desarrollo.persona.prs.formulario.seguro");
			PRS_FECHA_FORMULARIO_SEGURO = rb.getString("desarrollo.persona.prs.fecha.formulario.seguro");
			
			TABLA_PLANIFICACION_CRONOGRAMA = rb.getString("desarrollo.planificacion.cronograma");
			PLCR_ID = rb.getString("desarrollo.planificacion.cronograma.plcr.id");
			PLCR_CRPRFL_ID = rb.getString("desarrollo.planificacion.cronograma.crprfl.id");
			PLCR_ESTADO = rb.getString("desarrollo.planificacion.cronograma.plcr.estado");
			PLCR_FECHA_INICIO = rb.getString("desarrollo.planificacion.cronograma.plcr.fecha.inicio");
			PLCR_FECHA_FIN = rb.getString("desarrollo.planificacion.cronograma.plcr.fecha.fin");
			TABLA_PORCENTAJE_GRATUIDAD = rb.getString("desarrollo.porcentaje.gratuidad");
			PRGR_ID = rb.getString("desarrollo.porcentaje.gratuidad.prgr.id");
			PRGR_TIGR_ID = rb.getString("desarrollo.porcentaje.gratuidad.tigr.id");
			PRGR_DESCRIPCION = rb.getString("desarrollo.porcentaje.gratuidad.prgr.descripcion");
			PRGR_VALOR = rb.getString("desarrollo.porcentaje.gratuidad.prgr.valor");
			PRGR_ESTADO = rb.getString("desarrollo.porcentaje.gratuidad.prgr.estado");
			TABLA_PREREQUISITO = rb.getString("desarrollo.prerequisito");
			PRR_ID = rb.getString("desarrollo.prerequisito.prr.id");
			PRR_MTR_ID = rb.getString("desarrollo.prerequisito.mtr.id");
			PRR_MTR_PREREQUISITO_ID = rb.getString("desarrollo.prerequisito.mtr.prerequisito.id");
			PRR_CODIGO = rb.getString("desarrollo.prerequisito.prr.codigo");
			PRR_DESCRIPCION = rb.getString("desarrollo.prerequisito.prr.descripcion");
			PRR_ESTADO = rb.getString("desarrollo.prerequisito.prr.estado");
			TABLA_PROCESO = rb.getString("desarrollo.proceso");
			PRC_ID = rb.getString("desarrollo.proceso.prc.id");
			PRC_DESCRIPCION = rb.getString("desarrollo.proceso.prc.descripcion");
			TABLA_PROCESO_CALIFICACION = rb.getString("desarrollo.proceso.calificacion");
			PRCL_ID = rb.getString("desarrollo.proceso.calificacion.prcl.id");
			PRCL_CLF_ID = rb.getString("desarrollo.proceso.calificacion.clf.id");
			PRCL_FECHA = rb.getString("desarrollo.proceso.calificacion.prcl.fecha");
			PRCL_TIPO_PROCESO = rb.getString("desarrollo.proceso.calificacion.prcl.tipo.proceso");
			PRCL_OBSERVACION = rb.getString("desarrollo.proceso.calificacion.prcl.observacion");
			PRCL_NOTA1 = rb.getString("desarrollo.proceso.calificacion.prcl.nota1");
			PRCL_NOTA2 = rb.getString("desarrollo.proceso.calificacion.prcl.nota2");
			PRCL_EXAMEN = rb.getString("desarrollo.proceso.calificacion.prcl.examen");
			PRCL_SUPLETORIO = rb.getString("desarrollo.proceso.calificacion.prcl.supletorio");
			PRCL_ASISTENCIA1 = rb.getString("desarrollo.proceso.calificacion.prcl.asistencia1");
			PRCL_ASISTENCIA2 = rb.getString("desarrollo.proceso.calificacion.prcl.asistencia2");
			PRCL_TOTAL_ASISTENCIA1 = rb.getString("desarrollo.proceso.calificacion.prcl.total.asistencia1");
			PRCL_TOTAL_ASISTENCIA2 = rb.getString("desarrollo.proceso.calificacion.prcl.total.asistencia2");
			PRCL_PROMEDIO_NOTAS = rb.getString("desarrollo.proceso.calificacion.prcl.promedio.notas");
			PRCL_PROMEDIO_ASISTENCIA = rb.getString("desarrollo.proceso.calificacion.prcl.promedio.asistencia");
			PRCL_ASISTENCIA_TOTAL = rb.getString("desarrollo.proceso.calificacion.prcl.asistencia.total");
			PRCL_PARAM_RECUPERACION1 = rb.getString("desarrollo.proceso.calificacion.prcl.param.recuperacion1");
			PRCL_PARAM_RECUPERACION2 = rb.getString("desarrollo.proceso.calificacion.prcl.param.recuperacion2");
			PRCL_NOTA_FINAL_SEMESTRE = rb.getString("desarrollo.proceso.calificacion.prcl.nota.final.semestre");
			PRCL_ASISTENCIA_DOCENTE1 = rb.getString("desarrollo.proceso.calificacion.prcl.asistencia.docente1");
			PRCL_ASISTENCIA_DOCENTE2 = rb.getString("desarrollo.proceso.calificacion.prcl.asistencia.docente2");
			PRCL_FECHA_NOTA2 = rb.getString("desarrollo.proceso.calificacion.prcl.fecha.nota2");
			PRCL_FECHA_RECUPERACION = rb.getString("desarrollo.proceso.calificacion.prcl.fecha.recuperacion");
			PRCL_OBSERVACION2 = rb.getString("desarrollo.proceso.calificacion.prcl.observacion2");
			PRCL_OBSERVACION3 = rb.getString("desarrollo.proceso.calificacion.prcl.observacion3");
			PRCL_SUMA_P1_P2 = rb.getString("desarrollo.proceso.calificacion.prcl.suma.p1.p2");
			PRCL_ASISTENCIA_TOTAL_DOC = rb.getString("desarrollo.proceso.calificacion.prcl.asistencia.total.doc");
			TABLA_PROCESO_ESTUDIANTE = rb.getString("desarrollo.proceso.estudiante");
			PRES_ID = rb.getString("desarrollo.proceso.estudiante.pres.id");
			PRES_FCES_ID = rb.getString("desarrollo.proceso.estudiante.fces.id");
			PRES_DESCRIPCION = rb.getString("desarrollo.proceso.estudiante.pres.descripcion");
			PRES_TIPO = rb.getString("desarrollo.proceso.estudiante.pres.tipo");
			PRES_ESTADO = rb.getString("desarrollo.proceso.estudiante.pres.estado");
			PRES_FECHA_EJECUCION = rb.getString("desarrollo.proceso.estudiante.pres.fecha.ejecucion");
			TABLA_PROCESO_FLUJO = rb.getString("desarrollo.proceso.flujo");
			PRFL_ID = rb.getString("desarrollo.proceso.flujo.prfl.id");
			PRFL_DESCRIPCION = rb.getString("desarrollo.proceso.flujo.prfl.descripcion");
			PRFL_ESTADO = rb.getString("desarrollo.proceso.flujo.prfl.estado");
			TABLA_PROCESO_TRAMITE_CONTRATO = rb.getString("desarrollo.proceso.tramite.contrato");
			PRTRCN_ID = rb.getString("desarrollo.proceso.tramite.contrato.prtrcn.id");
			PRTRCN_DTTRCN_ID = rb.getString("desarrollo.proceso.tramite.contrato.dttrcn.id");
			PRTRCN_TIPO_PROCESO = rb.getString("desarrollo.proceso.tramite.contrato.prtrcn.tipo.proceso");
			PRTRCN_FECHA_EJECUCION = rb.getString("desarrollo.proceso.tramite.contrato.prtrcn.fecha.ejecucion");
			PRTRCN_RESULTADO_PROCESO = rb.getString("desarrollo.proceso.tramite.contrato.prtrcn.resultado.proceso");
			TABLA_PROYECCION = rb.getString("desarrollo.proyeccion");
			PRY_ID = rb.getString("desarrollo.proyeccion.pry.id");
			PRY_EXC_ID = rb.getString("desarrollo.proyeccion.exc.id");
			PRY_MLCRMT_ID = rb.getString("desarrollo.proyeccion.mlcrmt.id");
			PRY_PRAC_ID = rb.getString("desarrollo.proyeccion.prac.id");
			PRY_NUM_NUEVO = rb.getString("desarrollo.proyeccion.pry.num.nuevo");
			PRY_NUM_REPETIDO = rb.getString("desarrollo.proyeccion.pry.num.repetido");
			TABLA_PUESTO = rb.getString("desarrollo.puesto");
			PST_ID = rb.getString("desarrollo.puesto.pst.id");
			PST_DENOMINACION = rb.getString("desarrollo.puesto.pst.denominacion");
			PST_ESTADO = rb.getString("desarrollo.puesto.pst.estado");
			PST_AMBITO = rb.getString("desarrollo.puesto.pst.ambito");
			PST_CODIGO = rb.getString("desarrollo.puesto.pst.codigo");
			PST_NIVEL = rb.getString("desarrollo.puesto.pst.nivel");
			PST_MISION = rb.getString("desarrollo.puesto.pst.mision");
			PST_RESPONSABILIDAD = rb.getString("desarrollo.puesto.pst.responsabilidad");
			PST_DESCRIPCION_EXPERIENCIA = rb.getString("desarrollo.puesto.pst.descripcion.experiencia");
			PST_INTERFAZ = rb.getString("desarrollo.puesto.pst.interfaz");
			PST_FECHA_CREACION = rb.getString("desarrollo.puesto.pst.fecha.creacion");
			PST_NIVEL_RANGO_GRADUAL = rb.getString("desarrollo.puesto.pst.nivel.rango.gradual");
			PST_SER_ID = rb.getString("desarrollo.puesto.ser.id");
			PST_GROC_ID = rb.getString("desarrollo.puesto.groc.id");
			PST_TMDD_ID = rb.getString("desarrollo.puesto.tmdd.id");
			PST_TIPS_ID = rb.getString("desarrollo.puesto.tips.id");
			PST_CATEGORIA_DOCENTE = rb.getString("desarrollo.puesto.pst.categoria.docente");
			TABLA_PUESTO_AREA_CONOCIMIENTO = rb.getString("desarrollo.puesto.area.conocimiento");
			PSARCN_ID = rb.getString("desarrollo.puesto.area.conocimiento.psarcn.id");
			PSARCN_ARCN_ID = rb.getString("desarrollo.puesto.area.conocimiento.arcn.id");
			PSARCN_PST_ID = rb.getString("desarrollo.puesto.area.conocimiento.pst.id");
			TABLA_RECORD_ESTUDIANTE = rb.getString("desarrollo.record.estudiante");
			RCES_ID = rb.getString("desarrollo.record.estudiante.rces.id");
			RCES_FCES_ID = rb.getString("desarrollo.record.estudiante.fces.id");
			RCES_MLCRPR_ID = rb.getString("desarrollo.record.estudiante.mlcrpr.id");
			RCES_ESTADO = rb.getString("desarrollo.record.estudiante.rces.estado");
			RCES_OBSERVACION = rb.getString("desarrollo.record.estudiante.rces.observacion");
			RCES_INGRESO_NOTA = rb.getString("desarrollo.record.estudiante.rces.ingreso.nota");
			RCES_INGRESO_NOTA2 = rb.getString("desarrollo.record.estudiante.rces.ingreso.nota2");
			RCES_INGRESO_NOTA3 = rb.getString("desarrollo.record.estudiante.rces.ingreso.nota3");
			RCES_USUARIO = rb.getString("desarrollo.record.estudiante.rces.usuario");
			RCES_MODIFICACION = rb.getString("desarrollo.record.estudiante.rces.modificacion");
			RCES_MODO_APROBACION =rb.getString("desarrollo.record.estudiante.rces.modo.aprobacion");
			
			TABLA_REGIMEN = rb.getString("desarrollo.regimen");
			RGM_ID = rb.getString("desarrollo.regimen.rgm.id");
			RGM_DESCRIPCION = rb.getString("desarrollo.regimen.rgm.descripcion");
			RGM_CODIGO = rb.getString("desarrollo.regimen.rgm.codigo");
			TABLA_REGIMEN_ACADEMICO = rb.getString("desarrollo.regimen.academico");
			RGAC_ID = rb.getString("desarrollo.regimen.academico.rgac.id");
			RGAC_DESCRIPCION = rb.getString("desarrollo.regimen.academico.rgac.descripcion");
			RGAC_ESTADO = rb.getString("desarrollo.regimen.academico.rgac.estado");
			TABLA_RELACION_LABORAL = rb.getString("desarrollo.relacion.laboral");
			RLLB_ID = rb.getString("desarrollo.relacion.laboral.rllb.id");
			RLLB_DESCRIPCION = rb.getString("desarrollo.relacion.laboral.rllb.descripcion");
			RLLB_ESTADO = rb.getString("desarrollo.relacion.laboral.rllb.estado");
			TABLA_REQUISITO = rb.getString("desarrollo.requisito");
			RQS_ID = rb.getString("desarrollo.requisito.rqs.id");
			RQS_PRTRCN_ID = rb.getString("desarrollo.requisito.prtrcn.id");
			RQS_PUBLICACIONES = rb.getString("desarrollo.requisito.rqs.publicaciones");
			RQS_EXPERICIA_DOCENTE = rb.getString("desarrollo.requisito.rqs.expericia.docente");
			RQS_EXPERICIA_LABORAL = rb.getString("desarrollo.requisito.rqs.expericia.laboral");
			RQS_CAPACITACION = rb.getString("desarrollo.requisito.rqs.capacitacion");
			RQS_MECANIZADO_IESS = rb.getString("desarrollo.requisito.rqs.mecanizado.iess");
			RQS_DECLARACION_BIENES = rb.getString("desarrollo.requisito.rqs.declaracion.bienes");
			RQS_PLURIEMPLEO = rb.getString("desarrollo.requisito.rqs.pluriempleo");
			RQS_NEPOTISMO = rb.getString("desarrollo.requisito.rqs.nepotismo");
			RQS_CERTIFICACION_BANCARIA = rb.getString("desarrollo.requisito.rqs.certificacion.bancaria");
			RQS_CERTIFICADO_AFILIADO_IESS = rb.getString("desarrollo.requisito.rqs.certificado.afiliado.iess");
			RQS_ESTADO = rb.getString("desarrollo.requisito.rqs.estado");
			TABLA_ROL = rb.getString("desarrollo.rol");
			ROL_ID = rb.getString("desarrollo.rol.rol.id");
			ROL_DESCRIPCION = rb.getString("desarrollo.rol.rol.descripcion");
			ROL_TIPO = rb.getString("desarrollo.rol.rol.tipo");
			ROL_DETALLE = rb.getString("desarrollo.rol.rol.detalle");
			TABLA_ROL_FLUJO_CARRERA = rb.getString("desarrollo.rol.flujo.carrera");
			ROFLCR_ID = rb.getString("desarrollo.rol.flujo.carrera.roflcr.id");
			ROFLCR_USRO_ID = rb.getString("desarrollo.rol.flujo.carrera.usro.id");
			ROFLCR_CRR_ID = rb.getString("desarrollo.rol.flujo.carrera.crr.id");
			ROFLCR_ESTADO = rb.getString("desarrollo.rol.flujo.carrera.roflcr.estado");
			TABLA_SERIE = rb.getString("desarrollo.serie");
			SER_ID = rb.getString("desarrollo.serie.ser.id");
			SER_PRC_ID = rb.getString("desarrollo.serie.prc.id");
			SER_CODIGO = rb.getString("desarrollo.serie.ser.codigo");
			SER_DESCRIPCION = rb.getString("desarrollo.serie.ser.descripcion");
			TABLA_SISTEMA_CALIFICACION = rb.getString("desarrollo.sistema.calificacion");
			SSCL_ID = rb.getString("desarrollo.sistema.calificacion.sscl.id");
			SSCL_PRAC_ID = rb.getString("desarrollo.sistema.calificacion.prac.id");
			SSCL_TISSCL_ID = rb.getString("desarrollo.sistema.calificacion.tisscl.id");
			SSCL_NOTA_MAXIMA = rb.getString("desarrollo.sistema.calificacion.sscl.nota.maxima");
			SSCL_NOTA_MINIMA_APROBACION = rb.getString("desarrollo.sistema.calificacion.sscl.nota.minima.aprobacion");
			SSCL_NOTA_MINIMA_SUPLETORIO = rb.getString("desarrollo.sistema.calificacion.sscl.nota.minima.supletorio");
			SSCL_PORCENTAJE_APROBACION = rb.getString("desarrollo.sistema.calificacion.sscl.porcentaje.aprobacion");
			SSCL_REDONDEO = rb.getString("desarrollo.sistema.calificacion.sscl.redondeo");
			SSCL_ESTADO = rb.getString("desarrollo.sistema.calificacion.sscl.estado");
			TABLA_SOLICITUD_TERCERA_MATRICULA = rb.getString("desarrollo.solicitud.tercera.matricula");
			SLTRMT_ID = rb.getString("desarrollo.sltrmt.id");
			SLTRMT_CSL_ID = rb.getString("desarrollo.csl.id");
			SLTRMT_RCES_ID = rb.getString("desarrollo.rces.id");
			SLTRMT_PRAC_ID = rb.getString("desarrollo.prac.id");
			SLTRMT_ESTADO = rb.getString("desarrollo.sltrmt.estado");
			SLTRMT_TIPO = rb.getString("desarrollo.sltrmt.tipo");
			SLTRMT_FECHA_SOLICITUD = rb.getString("desarrollo.sltrmt.fecha.solicitud");
			SLTRMT_DOCUMENTO_SOLICITUD = rb.getString("desarrollo.sltrmt.documento.solicitud");
			SLTRMT_FECHA_VERIF_SOLICITUD=rb.getString("desarrollo.sltrmt.fecha.verif.solicitud");
			SLTRMT_FECHA_RESP_SOLICITUD= rb.getString("desarrollo.sltrmt.fecha.resp.solicitud");
			SLTRMT_DOCUMENTO_RESOLUCION = rb.getString("desarrollo.sltrmt.documento.resolucion");
			SLTRMT_OBSERVACION = rb.getString("desarrollo.sltrmt.observacion");		
			SLTRMT_OBSERVACION_FINAL = rb.getString("desarrollo.sltrmt.observacion.final");
			SLTRMT_SUB_ID = rb.getString("desarrollo.sltrmt.sub.id");	
			SLTRMT_FCES_ID = rb.getString("desarrollo.sltrmt.fces.id");
			SLTRMT_MTR_ID = rb.getString("desarrollo.sltrmt.mtr.id");
			SLTRMT_ESTADO_REGISTRO = rb.getString("desarrollo.sltrmt.estado.registro");
			TABLA_TIEMPO_DEDICACION = rb.getString("desarrollo.tiempo.dedicacion");
			TMDD_ID = rb.getString("desarrollo.tiempo.dedicacion.tmdd.id");
			TMDD_DESCRIPCION = rb.getString("desarrollo.tiempo.dedicacion.tmdd.descripcion");
			TMDD_HORAS = rb.getString("desarrollo.tiempo.dedicacion.tmdd.horas");
			TMDD_ESTADO = rb.getString("desarrollo.tiempo.dedicacion.tmdd.estado");
			TABLA_TIPO_APERTURA = rb.getString("desarrollo.tipo.apertura");
			TIAP_ID = rb.getString("desarrollo.tipo.apertura.tiap.id");
			TIAP_DESCRIPCION = rb.getString("desarrollo.tipo.apertura.tiap.descripcion");
			TIAP_ESTADO = rb.getString("desarrollo.tipo.apertura.tiap.estado");
			TABLA_TIPO_CARGA_HORARIA = rb.getString("desarrollo.tipo.carga.horaria");
			TICRHR_ID = rb.getString("desarrollo.tipo.carga.horaria.ticrhr.id");
			TICRHR_DESCRIPCION = rb.getString("desarrollo.tipo.carga.horaria.ticrhr.descripcion");
			TICRHR_ESTADO = rb.getString("desarrollo.tipo.carga.horaria.ticrhr.estado");
			TABLA_TIPO_CAUSAL = rb.getString("desarrollo.tipo.causal");
			TICS_ID = rb.getString("desarrollo.tipo.causal.tics.id");
			TICS_DESCRIPCION = rb.getString("desarrollo.tipo.causal.tics.descripcion");
			TICS_ESTADO = rb.getString("desarrollo.tipo.causal.tics.estado");
			TABLA_TIPO_CONTRATO = rb.getString("desarrollo.tipo.contrato");
			TICN_ID = rb.getString("desarrollo.tipo.contrato.ticn.id");
			TICN_TIEM_ID = rb.getString("desarrollo.tipo.contrato.tiem.id");
			TICN_DESCRIPCION = rb.getString("desarrollo.tipo.contrato.ticn.descripcion");
			TICN_ESTADO = rb.getString("desarrollo.tipo.contrato.ticn.estado");
			TABLA_TIPO_DOCUMENTO = rb.getString("desarrollo.tipo.documento");
			TIDC_ID = rb.getString("desarrollo.tipo.documento.tidc.id");
			TIDC_SUB_ID = rb.getString("desarrollo.tipo.documento.tidc.sub.id");
			TIDC_DESCRIPCION = rb.getString("desarrollo.tipo.documento.tidc.descripcion");
			TIDC_NUMERO_DOCUMENTO = rb.getString("desarrollo.tipo.documento.tidc.numero.documento");
			TIDC_FECHA_INICIO = rb.getString("desarrollo.tipo.documento.tidc.fecha.inicio");
			TIDC_FECHA_FIN = rb.getString("desarrollo.tipo.documento.tidc.fecha.fin");
			TIDC_INGRESO_CONCURSO = rb.getString("desarrollo.tipo.documento.tidc.ingreso.concurso");
			TABLA_TIPO_EMPLEADO = rb.getString("desarrollo.tipo.empleado");
			TIEM_ID = rb.getString("desarrollo.tipo.empleado.tiem.id");
			TIEM_DESCRIPCION = rb.getString("desarrollo.tipo.empleado.tiem.descripcion");
			TIEM_ESTADO = rb.getString("desarrollo.tipo.empleado.tiem.estado");
			TABLA_TIPO_FINANCIAMIENTO = rb.getString("desarrollo.tipo.financiamiento");
			TIFN_ID = rb.getString("desarrollo.tipo.financiamiento.tifn.id");
			TIFN_DESCRIPCION = rb.getString("desarrollo.tipo.financiamiento.tifn.descripcion");
			TABLA_TIPO_FORMACION = rb.getString("desarrollo.tipo.formacion");
			TIFR_ID = rb.getString("desarrollo.tipo.formacion.tifr.id");
			TIFR_DESCRIPCION = rb.getString("desarrollo.tipo.formacion.tifr.descripcion");
			TIFR_NVFR_ID = rb.getString("desarrollo.tipo.formacion.nvfr.id");
			TABLA_TIPO_FORMACION_MALLA = rb.getString("desarrollo.tipo.formacion.malla");
			TIFRML_ID = rb.getString("desarrollo.tipo.formacion.malla.tifrml.id");
			TIFRML_DESCRIPCION = rb.getString("desarrollo.tipo.formacion.malla.tifrml.descripcion");
			TIFRML_ESTADO = rb.getString("desarrollo.tipo.formacion.malla.tifrml.estado");
			TABLA_TIPO_FUNCION_CARGA_HORARIA = rb.getString("desarrollo.tipo.funcion.carga.horaria");
			TIFNCRHR_ID = rb.getString("desarrollo.tipo.funcion.carga.horaria.tifncrhr.id");
			TIFNCRHR_TICRHR_ID = rb.getString("desarrollo.tipo.funcion.carga.horaria.ticrhr.id");
			TIFNCRHR_ESTADO = rb.getString("desarrollo.tipo.funcion.carga.horaria.tifncrhr.estado");
			TIFNCRHR_FNCRHR_ID = rb.getString("desarrollo.tipo.funcion.carga.horaria.fncrhr.id");
			TABLA_TIPO_GRATUIDAD = rb.getString("desarrollo.tipo.gratuidad");
			TIGR_ID = rb.getString("desarrollo.tipo.gratuidad.tigr.id");
			TIGR_DESCRIPCION = rb.getString("desarrollo.tipo.gratuidad.tigr.descripcion");
			TIGR_ESTADO = rb.getString("desarrollo.tipo.gratuidad.tigr.estado");
			TABLA_TIPO_MATERIA = rb.getString("desarrollo.tipo.materia");
			TIMT_ID = rb.getString("desarrollo.tipo.materia.timt.id");
			TIMT_DESCRIPCION = rb.getString("desarrollo.tipo.materia.timt.descripcion");
			TIMT_ESTADO = rb.getString("desarrollo.tipo.materia.timt.estado");
			TABLA_TIPO_NIVEL_APERTURA = rb.getString("desarrollo.tipo.nivel.apertura");
			TINVAP_ID = rb.getString("desarrollo.tipo.nivel.apertura.tinvap.id");
			TINVAP_NVAP_ID = rb.getString("desarrollo.tipo.nivel.apertura.nvap.id");
			TINVAP_TIAP_ID = rb.getString("desarrollo.tipo.nivel.apertura.tiap.id");
			TINVAP_PLCR_ID = rb.getString("desarrollo.tipo.nivel.apertura.plcr.id");
			TINVAP_FECHA_INICIO = rb.getString("desarrollo.tipo.nivel.apertura.tinvap.fecha.inicio");
			TINVAP_FECHA_FIN = rb.getString("desarrollo.tipo.nivel.apertura.tinvap.fecha.fin");
			TINVAP_ESTADO = rb.getString("desarrollo.tipo.nivel.apertura.tinvap.estado");
			TINVAP_OBSERVACION = rb.getString("desarrollo.tipo.nivel.apertura.tinvap.observacion");
			TABLA_TIPO_PROCESO = rb.getString("desarrollo.tipo.proceso");
			TIPR_ID = rb.getString("desarrollo.tipo.proceso.tipr.id");
			TIPR_DESCRIPCION = rb.getString("desarrollo.tipo.proceso.tipr.descripcion");
			TIPR_ESTADO = rb.getString("desarrollo.tipo.proceso.tipr.estado");
			TABLA_TIPO_PUESTO = rb.getString("desarrollo.tipo.puesto");
			TIPS_ID = rb.getString("desarrollo.tipo.puesto.tips.id");
			TIPS_DESCRIPCION = rb.getString("desarrollo.tipo.puesto.tips.descripcion");
			TIPS_ESTADO = rb.getString("desarrollo.tipo.puesto.tips.estado");
			TABLA_TIPO_SEDE = rb.getString("desarrollo.tipo.sede");
			TISE_ID = rb.getString("desarrollo.tipo.sede.tise.id");
			TISE_RGAC_ID = rb.getString("desarrollo.tipo.sede.rgac.id");
			TISE_DESCRIPCION = rb.getString("desarrollo.tipo.sede.tise.descripcion");
			TABLA_TIPO_SISTEMA_CALIFICACION = rb.getString("desarrollo.tipo.sistema.calificacion");
			TISSCL_ID = rb.getString("desarrollo.tipo.sistema.calificacion.tisscl.id");
			TISSCL_DESCRIPCION = rb.getString("desarrollo.tipo.sistema.calificacion.tisscl.descripcion");
			TISSCL_ESTADO = rb.getString("desarrollo.tipo.sistema.calificacion.tisscl.estado");
			TISSCL_OBSERVACION = rb.getString("desarrollo.tipo.sistema.calificacion.tisscl.observacion");
			TABLA_TITULO = rb.getString("desarrollo.titulo");
			TTL_ID = rb.getString("desarrollo.titulo.ttl.id");
			TTL_DESCRIPCION = rb.getString("desarrollo.titulo.ttl.descripcion");
			TTL_SEXO = rb.getString("desarrollo.titulo.ttl.sexo");
			TTL_ESTADO = rb.getString("desarrollo.titulo.ttl.estado");
			TTL_TIPO = rb.getString("desarrollo.titulo.ttl.tipo");
			TABLA_TRAMITE = rb.getString("desarrollo.tramite");
			TRM_ID = rb.getString("desarrollo.tramite.trm.id");
			TRM_DESCRIPCION = rb.getString("desarrollo.tramite.trm.descripcion");
			TRM_ESTADO = rb.getString("desarrollo.tramite.trm.estado");
			TABLA_TURNERO = rb.getString("desarrollo.turnero");
			TRN_ID = rb.getString("desarrollo.turnero.trn.id");
			TRN_PRTRCN_ID = rb.getString("desarrollo.turnero.prtrcn.id");
			TRN_USRO_ID = rb.getString("desarrollo.turnero.usro.id");
			TRN_ESTADO = rb.getString("desarrollo.turnero.trn.estado");
			TABLA_UBICACION = rb.getString("desarrollo.ubicacion");
			UBC_ID = rb.getString("desarrollo.ubicacion.ubc.id");
			UBC_DESCRIPCION = rb.getString("desarrollo.ubicacion.ubc.descripcion");
			UBC_JERARQUIA = rb.getString("desarrollo.ubicacion.ubc.jerarquia");
			UBC_GENTILICIO = rb.getString("desarrollo.ubicacion.ubc.gentilicio");
			UBC_COD_SNIESE = rb.getString("desarrollo.ubicacion.ubc.cod.sniese");
			UBC_SUB_ID = rb.getString("desarrollo.ubicacion.ubc.sub.id");
			TABLA_UNIDAD_FORMACION = rb.getString("desarrollo.unidad.formacion");
			UNFR_ID = rb.getString("desarrollo.unidad.formacion.unfr.id");
			UNFR_DESCRIPCION = rb.getString("desarrollo.unidad.formacion.unfr.descripcion");
			UNFR_ESTADO = rb.getString("desarrollo.unidad.formacion.unfr.estado");
			TABLA_USUARIO = rb.getString("desarrollo.usuario");
			USR_ID = rb.getString("desarrollo.usuario.usr.id");
			USR_IDENTIFICACION = rb.getString("desarrollo.usuario.usr.identificacion");
			USR_NICK = rb.getString("desarrollo.usuario.usr.nick");
			USR_PASSWORD = rb.getString("desarrollo.usuario.usr.password");
			USR_FECHA_CREACION = rb.getString("desarrollo.usuario.usr.fecha.creacion");
			USR_FECHA_CADUCIDAD = rb.getString("desarrollo.usuario.usr.fecha.caducidad");
			USR_FECHA_CAD_PASS = rb.getString("desarrollo.usuario.usr.fecha.cad.pass");
			USR_ESTADO = rb.getString("desarrollo.usuario.usr.estado");
			USR_EST_SESION = rb.getString("desarrollo.usuario.usr.est.sesion");
			USR_ACTIVE_DIRECTORY = rb.getString("desarrollo.usuario.usr.active.directory");
			USR_PRS_ID = rb.getString("desarrollo.usuario.prs.id");
			TABLA_USUARIO_ROL = rb.getString("desarrollo.usuario.rol");
			USRO_ID = rb.getString("desarrollo.usuario.rol.usro.id");
			USRO_ESTADO = rb.getString("desarrollo.usuario.rol.usro.estado");
			USRO_USR_ID = rb.getString("desarrollo.usuario.rol.usr.id");
			USRO_ROL_ID = rb.getString("desarrollo.usuario.rol.rol.id");
			TABLA_VIGENCIA = rb.getString("desarrollo.vigencia");
			VGN_ID = rb.getString("desarrollo.vigencia.vgn.id");
			VGN_DESCRIPCION = rb.getString("desarrollo.vigencia.vgn.descripcion");
			
			TABLA_ASIGNACION_EVALUADOR = rb.getString("desarrollo.asignacion.evaluador");
			ASEV_ID = rb.getString("desarrollo.asignacion.evaluador.asev.id");
			ASEV_CRHR_ID = rb.getString("desarrollo.asignacion.evaluador.crhr.id");
			ASEV_EVEV_ID = rb.getString("desarrollo.asignacion.evaluador.evev.id");
			ASEV_ESTADO = rb.getString("desarrollo.asignacion.evaluador.asev.estado");
			ASEV_USUARIO = rb.getString("desarrollo.asignacion.evaluador.asev.usuario");
			ASEV_FECHA = rb.getString("desarrollo.asignacion.evaluador.asev.fecha");
			ASEV_EVALUADOR_CRR_ID = rb.getString("desarrollo.asignacion.evaluador.asev.evaluador.crr.id");

			TABLA_CONTENIDO = rb.getString("desarrollo.contenido");
			CNTN_ID = rb.getString("desarrollo.contenido.cnt.id");
			CNTN_TPCNFNTPEV_ID = rb.getString("desarrollo.contenido.tpcnfntpev.id");
			CNTN_ASEV_ID = rb.getString("desarrollo.contenido.asev.id");
			CNTN_EVA_ID = rb.getString("desarrollo.contenido.eva.id");
			CNTN_DESCRIPCION = rb.getString("desarrollo.contenido.cnt.descripcion");
			CNTN_SELECCION = rb.getString("desarrollo.contenido.cnt.seleccion");
			CNTN_FECHA = rb.getString("desarrollo.contenido.cnt.fecha");
			CNTN_USUARIO = rb.getString("desarrollo.contenido.cnt.usuario");
			CNTN_SELECCION_INICIAL=rb.getString("desarrollo.contenido.cnt.seleccion.inicial");
			CNTN_REGISTRO_APELACION=rb.getString("desarrollo.contenido.cnt.registro.apelacion");
			CNTN_USUARIO_APELACION=rb.getString("desarrollo.contenido.cnt.usuario.apelacion");
			CNTN_OFICIO_APELACION=rb.getString("desarrollo.contenido.cnt.oficio.apelacion");
			CNTN_ESTADO_APELACION=rb.getString("desarrollo.contenido.cnt.estado.apelacion");
			
			TABLA_EVALUACION = rb.getString("desarrollo.evaluacion");
			EVL_EVA_ID = rb.getString("desarrollo.evaluacion.eva.id");
			EVL_PRAC_ID = rb.getString("desarrollo.evaluacion.prac.id");
			EVL_TPEV_ID = rb.getString("desarrollo.evaluacion.tpev.id");
			EVL_EVA_DESCRIPCION = rb.getString("desarrollo.evaluacion.eva.descripcion");
			EVL_EVA_USUARIO = rb.getString("desarrollo.evaluacion.eva.usuario");
			EVL_EVA_FECHA = rb.getString("desarrollo.evaluacion.eva.fecha");
			EVL_EVA_ESTADO = rb.getString("desarrollo.evaluacion.eva.estado");
			EVL_EVA_CRONOGRAMA_INICIO = rb.getString("desarrollo.evaluacion.eva.cronograma.inicio");
			EVL_EVA_CRONOGRAMA_FIN = rb.getString("desarrollo.evaluacion.eva.cronograma.fin");
			EVL_EVA_PRFL_ID = rb.getString("desarrollo.evaluacion.eva.prfl.id");
			
			TABLA_EVALUADOR_EVALUADO = rb.getString("desarrollo.evaluador.evaluado");
			EVEV_ID = rb.getString("desarrollo.evaluador.evaluado.evev.id");
			EVEV_USRO_ID = rb.getString("desarrollo.evaluador.evaluado.usro.id");
			EVEV_EVALUADOR = rb.getString("desarrollo.evaluador.evaluado.evev.evaluador");

			TABLA_FUNCION_EVALUACION = rb.getString("desarrollo.funcion.evaluacion");
			FNEV_ID = rb.getString("desarrollo.funcion.evaluacion.fnev.id");
			FNEV_DESCRIPCION = rb.getString("desarrollo.funcion.evaluacion.fnev.descripcion");
			FNEV_ESTADO = rb.getString("desarrollo.funcion.evaluacion.fnev.estado");
			FNEV_FECHA = rb.getString("desarrollo.funcion.evaluacion.fnev.fecha");
			FNEV_USUARIO = rb.getString("desarrollo.funcion.evaluacion.fnev.usuario");
			
			TABLA_FUNCION_TIPO_EVALUACION = rb.getString("desarrollo.funcion.tipo.evaluacion");
			FNTIEV_FNCTPEV_ID = rb.getString("desarrollo.funcion.tipo.evaluacion.fnctpev.id");
			FNTIEV_TPEV_ID = rb.getString("desarrollo.funcion.tipo.evaluacion.tpev.id");
			FNTIEV_FNEV_ID = rb.getString("desarrollo.funcion.tipo.evaluacion.fnev.id");
			FNTIEV_FNCTPEV_USUARIO = rb.getString("desarrollo.funcion.tipo.evaluacion.fnctpev.usuario");
			FNTIEV_FNCTPEV_FECHA = rb.getString("desarrollo.funcion.tipo.evaluacion.fnctpev.fecha");
			FNTIEV_FNCTPEV_ESTADO = rb.getString("desarrollo.funcion.tipo.evaluacion.fnctpev.estado");
			
			TABLA_TIPO_CONTENIDO = rb.getString("desarrollo.tipo.contenido");
			TICN_TPCN_ID = rb.getString("desarrollo.tipo.contenido.tpcn.id");
			TICN_TPCN_DESCRIPCION = rb.getString("desarrollo.tipo.contenido.tpcn.descripcion");
			TICN_TPCN_ESTADO = rb.getString("desarrollo.tipo.contenido.tpcn.estado");
			TICN_TPCN_TIPO = rb.getString("desarrollo.tipo.contenido.tpcn.tipo");
			TICN_TPCN_NUMERAL = rb.getString("desarrollo.tipo.contenido.tpcn.numeral");
			TICN_TPCN_TIPO_COMPONENTE = rb.getString("desarrollo.tipo.contenido.tpcn.tipo.componente");
			TICN_TPCN_TIPO_SELECCION = rb.getString("desarrollo.tipo.contenido.tpcn.tipo.seleccion");
			TICN_TPCN_NUM_MAX = rb.getString("desarrollo.tipo.contenido.tpcn.num.max");
			TICN_TPCN_FECHA = rb.getString("desarrollo.tipo.contenido.tpcn.fecha");
			TICN_TPCN_USUARIO = rb.getString("desarrollo.tipo.contenido.tpcn.usuario");
			TICN_TPCN_OBLIGATORIEDAD = rb.getString("desarrollo.tipo.contenido.tpcn.obligatoriedad");
			TICN_TPCN_FK_TPCN_ID = rb.getString("desarrollo.tipo.contenido.tpcn.fk.tpcn.id");

			TABLA_TIPO_EVALUACION = rb.getString("desarrollo.tipo.evaluacion");
			TIEV_TPEV_ID = rb.getString("desarrollo.tipo.evaluacion.tpev.id");
			TIEV_TPEV_DESCRIPCION = rb.getString("desarrollo.tipo.evaluacion.tpev.descripcion");
			TIEV_TPEV_INTRODUCCION = rb.getString("desarrollo.tipo.evaluacion.tpev.introduccion");
			TIEV_TPEV_INSTRUCCIONES = rb.getString("desarrollo.tipo.evaluacion.tpev.instrucciones");
			TIEV_TPEV_FECHA = rb.getString("desarrollo.tipo.evaluacion.tpev.fecha");
			TIEV_TPEV_USUARIO = rb.getString("desarrollo.tipo.evaluacion.tpev.usuario");
			TIEV_TPEV_ESTADO = rb.getString("desarrollo.tipo.evaluacion.tpev.estado");
			
			TABLA_TPCN_FUNCION_TPEV = rb.getString("desarrollo.tpcn.funcion.tpev");
			TPFNTP_TPCNFNTPEV_ID = rb.getString("desarrollo.tpcn.funcion.tpev.tpcnfntpev.id");
			TPFNTP_FNCTPEV_ID = rb.getString("desarrollo.tpcn.funcion.tpev.fnctpev.id");
			TPFNTP_TPCN_ID = rb.getString("desarrollo.tpcn.funcion.tpev.tpcn.id");
			TPFNTP_TPCNFNTPEV_USUARIO = rb.getString("desarrollo.tpcn.funcion.tpev.tpcnfntpev.usuario");
			TPFNTP_TPCNFNTPEV_FECHA = rb.getString("desarrollo.tpcn.funcion.tpev.tpcnfntpev.fecha");
			TPFNTP_TPCNFNTPEV_ESTADO = rb.getString("desarrollo.tpcn.funcion.tpev.tpcnfntpev.estado");
			
			TABLA_CARRERA_INTERCAMBIO = rb.getString("desarrollo.carrera.intercambio");
			CRIN_ID = rb.getString("desarrollo.carrera.intercambio.crin.id");
			CRIN_CRR_ID = rb.getString("desarrollo.carrera.intercambio.crin.crr");
			CRIN_FCIN_ID = rb.getString("desarrollo.carrera.intercambio.crin.fcin");
			CRIN_AUTORIZACION = rb.getString("desarrollo.carrera.intercambio.crin.autorizacion");
			CRIN_USER_REGISTRO = rb.getString("desarrollo.carrera.intercambio.crin.user.registro");
			CRIN_FECHA_REGISTRO = rb.getString("desarrollo.carrera.intercambio.crin.fecha.registro");
			CRIN_OBSERVACION = rb.getString("desarrollo.carrera.intercambio.crin.observacion");
			CRIN_ESTADO = rb.getString("desarrollo.carrera.intercambio.crin.estado");
			
			
			TABLA_MATERIA_INTERCAMBIO = rb.getString("desarrollo.materia.intercambio");
			MTIN_ID = rb.getString("desarrollo.materia.intercambio.mtin.id");
			MTIN_CRIN_ID = rb.getString("desarrollo.materia.intercambio.mtin.crin");
			MTIN_MTR_ID = rb.getString("desarrollo.materia.intercambio.mtin.mtr");
			MTIN_DESCRIPCION = rb.getString("desarrollo.materia.intercambio.mtin.descripcion");
			MTIN_CODIGO = rb.getString("desarrollo.materia.intercambio.mtin.codigo");
			MTIN_ESTADO = rb.getString("desarrollo.materia.intercambio.mtin.estado");
			
			TABLA_REFERENCIA = rb.getString("desarrollo.referencia");
			RFR_ID = rb.getString("desarrollo.referencia.rfr.id");
			RFR_CEDULA = rb.getString("desarrollo.referencia.rfr.cedula");
			RFR_NOMBRE = rb.getString("desarrollo.referencia.rfr.nombre");
			RFR_DIRECCION = rb.getString("desarrollo.referencia.rfr.direccion");
			RFR_PARENTESCO = rb.getString("desarrollo.referencia.rfr.parentesco");
			RFR_PORCENTAJE = rb.getString("desarrollo.referencia.rfr.porcentaje");
			RFR_ESTADO = rb.getString("desarrollo.referencia.rfr.estado");
			
			
		}else{
			
			/***************PARA PRODUCCION**************************************/
			/********************************************************************/
			/********************************************************************/
			/********************************************************************/
			ResourceBundle rb = ResourceBundle.getBundle("META-INF.configuracion.tablasJdbcProduccion");
			ESQUEMA_DESARROLLO = rb.getString("esquema.academico.produccion");
			
			TABLA_HORARIO_FUNCION = rb.getString("academico.produccion.horario.funcion");
			HRFN_ID=rb.getString("academico.produccion.horario.funcion.hrfn.id");
			HRFN_DESCRIPCION=rb.getString("academico.produccion.horario.funcion.hrfn.descripcion");
			HRFN_HORA_INICIO=rb.getString("academico.produccion.horario.funcion.hrfn.hora.inicio");
			HRFN_HORA_FIN=rb.getString("academico.produccion.horario.funcion.hrfn.hora.fin");
			HRFN_ESTADO=rb.getString("academico.produccion.horario.funcion.hrfn.estado");
			HRFN_DIA=rb.getString("academico.produccion.horario.funcion.hrfn.dia");
			HRFN_HOCL_ID=rb.getString("academico.produccion.horario.funcion.hocl.id");
			HRFN_FUNCION=rb.getString("academico.produccion.horario.funcion.hrfn.funcion");
			HRFN_ACTIVIDAD=rb.getString("academico.produccion.horario.funcion.hrfn.actividad");
			HRFN_PRAC_ID=rb.getString("academico.produccion.horario.funcion.hrfn.prac.id");
			HRFN_DTPS_ID=rb.getString("academico.produccion.horario.funcion.hrfn.dtps.id");
			
			TABLA_PLANIFICACION_EVALUACION = rb.getString("academico.produccion.planificacion.evaluacion");
			PLEV_ID=rb.getString("academico.produccion.planificacion.evaluacion.plev.id");
			PLEV_FECHA_INICIO=rb.getString("academico.produccion.planificacion.evaluacion.plev.fecha.inicio");
			PLEV_FECHA_FIN=rb.getString("academico.produccion.planificacion.evaluacion.plev.fecha.fin");
			PLEV_ESTADO=rb.getString("academico.produccion.planificacion.evaluacion.plev.estado");
			TABLA_ACTIVIDAD_ESENCIAL = rb.getString("academico.produccion.actividad.esencial");
			ACES_ID = rb.getString("academico.produccion.actividad.esencial.aces.id");
			ACES_DESCRIPCION = rb.getString("academico.produccion.actividad.esencial.aces.descripcion");
			TABLA_ACTIVIDAD_PUESTO = rb.getString("academico.produccion.actividad.puesto");
			ACPS_ID = rb.getString("academico.produccion.actividad.puesto.acps.id");
			ACPS_ACES_ID = rb.getString("academico.produccion.actividad.puesto.aces.id");
			ACPS_PST_ID = rb.getString("academico.produccion.actividad.puesto.pst.id");
			TABLA_APRUEBA_SOLICITUD = rb.getString("academico.produccion.aprueba.solicitud");
			APSL_ID = rb.getString("academico.produccion.aprueba.solicitud.apsl.id");
			APSL_PRTRCN_ID = rb.getString("academico.produccion.aprueba.solicitud.prtrcn.id");
			APSL_FECHA_APROBACION = rb.getString("academico.produccion.aprueba.solicitud.apsl.fecha.aprobacion");
			APSL_ESTADO = rb.getString("academico.produccion.aprueba.solicitud.apsl.estado");
			APSL_OBSERVACION = rb.getString("academico.produccion.aprueba.solicitud.apsl.observacion");
			TABLA_ARANCEL = rb.getString("academico.produccion.arancel");
			ARN_ID = rb.getString("academico.produccion.arancel.arn.id");
			ARN_MDL_ID = rb.getString("academico.produccion.arancel.mdl.id");
			ARN_TIGR_ID = rb.getString("academico.produccion.arancel.tigr.id");
			ARN_DESCRIPCION = rb.getString("academico.produccion.arancel.arn.descripcion");
			ARN_TIPO_MATRICULA = rb.getString("academico.produccion.arancel.arn.tipo.matricula");
			ARN_TIPO_NUM_MATRICULA = rb.getString("academico.produccion.arancel.arn.tipo.num.matricula");
			ARN_VALOR = rb.getString("academico.produccion.arancel.arn.valor");
			ARN_TIPO = rb.getString("academico.produccion.arancel.arn.tipo");
			TABLA_AREA_CONOCIMIENTO = rb.getString("academico.produccion.area.conocimiento");
			ARCN_ID = rb.getString("academico.produccion.area.conocimiento.arcn.id");
			ARCN_DESCRIPCION = rb.getString("academico.produccion.area.conocimiento.arcn.descripcion");
			ARCN_CODIGO = rb.getString("academico.produccion.area.conocimiento.arcn.codigo");
			ARCN_JERARQUIA = rb.getString("academico.produccion.area.conocimiento.arcn.jerarquia");
			ARCN_SUB_ID = rb.getString("academico.produccion.area.conocimiento.arcn.sub.id");
			TABLA_ASIGNA_PRESUPUESTO = rb.getString("academico.produccion.asigna.presupuesto");
			ASPR_ID = rb.getString("academico.produccion.asigna.presupuesto.aspr.id");
			ASPR_PRTRCN_ID = rb.getString("academico.produccion.asigna.presupuesto.prtrcn.id");
			ASPR_PARTIDA_PRESUPUESTO = rb.getString("academico.produccion.asigna.presupuesto.aspr.partida.presupuesto");
			ASPR_ESTADO = rb.getString("academico.produccion.asigna.presupuesto.aspr.estado");
			ASPR_ARCHIVO_CERTIFICACION = rb.getString("academico.produccion.asigna.presupuesto.aspr.archivo.certificacion");
			TABLA_AULA = rb.getString("academico.produccion.aula");
			ALA_ID = rb.getString("academico.produccion.aula.ala.id");
			ALA_EDF_ID = rb.getString("academico.produccion.aula.edf.id");
			ALA_DESCRIPCION = rb.getString("academico.produccion.aula.ala.descripcion");
			ALA_CODIGO = rb.getString("academico.produccion.aula.ala.codigo");
			ALA_TIPO = rb.getString("academico.produccion.aula.ala.tipo");
			ALA_CAPACIDAD = rb.getString("academico.produccion.aula.ala.capacidad");
			ALA_PISO = rb.getString("academico.produccion.aula.ala.piso");
			ALA_ESTADO = rb.getString("academico.produccion.aula.ala.estado");
			TABLA_CALIFICACION = rb.getString("academico.produccion.calificacion");
			CLF_NOTA_FINAL_SEMESTRE = rb.getString("academico.produccion.calificacion.clf.nota.final.semestre");
			CLF_ESTADO = rb.getString("academico.produccion.calificacion.clf.estado");
			CLF_ASISTENCIA_DOCENTE1 = rb.getString("academico.produccion.calificacion.clf.asistencia.docente1");
			CLF_ASISTENCIA_DOCENTE2 = rb.getString("academico.produccion.calificacion.clf.asistencia.docente2");
			CLF_SUMA_P1_P2 = rb.getString("academico.produccion.calificacion.clf.suma.p1.p2");
			CLF_ASISTENCIA_TOTAL_DOC = rb.getString("academico.produccion.calificacion.clf.asistencia.total.doc");
			CLF_ID = rb.getString("academico.produccion.calificacion.clf.id");
			CLF_RCES_ID = rb.getString("academico.produccion.calificacion.rces.id");
			CLF_NOTA1 = rb.getString("academico.produccion.calificacion.clf.nota1");
			CLF_NOTA2 = rb.getString("academico.produccion.calificacion.clf.nota2");
			CLF_EXAMEN = rb.getString("academico.produccion.calificacion.clf.examen");
			CLF_SUPLETORIO = rb.getString("academico.produccion.calificacion.clf.supletorio");
			CLF_ASISTENCIA1 = rb.getString("academico.produccion.calificacion.clf.asistencia1");
			CLF_ASISTENCIA2 = rb.getString("academico.produccion.calificacion.clf.asistencia2");
			CLF_TOTAL_ASISTENCIA1 = rb.getString("academico.produccion.calificacion.clf.total.asistencia1");
			CLF_TOTAL_ASISTENCIA2 = rb.getString("academico.produccion.calificacion.clf.total.asistencia2");
			CLF_PROMEDIO_NOTAS = rb.getString("academico.produccion.calificacion.clf.promedio.notas");
			CLF_PROMEDIO_ASISTENCIA = rb.getString("academico.produccion.calificacion.clf.promedio.asistencia");
			CLF_ASISTENCIA_TOTAL = rb.getString("academico.produccion.calificacion.clf.asistencia.total");
			CLF_PARAM_RECUPERACION1 = rb.getString("academico.produccion.calificacion.clf.param.recuperacion1");
			CLF_PARAM_RECUPERACION2 = rb.getString("academico.produccion.calificacion.clf.param.recuperacion2");
			TABLA_CALIFICACION_MODULO = rb.getString("academico.produccion.calificacion.modulo");
			CLMD_NOTA_FINAL_SEMESTRE = rb.getString("academico.produccion.calificacion.modulo.clmd.nota.final.semestre");
			CLMD_ESTADO = rb.getString("academico.produccion.calificacion.modulo.clmd.estado");
			CLMD_ASISTENCIA_DOCENTE1 = rb.getString("academico.produccion.calificacion.modulo.clmd.asistencia.docente1");
			CLMD_ASISTENCIA_DOCENTE2 = rb.getString("academico.produccion.calificacion.modulo.clmd.asistencia.docente2");
			CLMD_SUMA_P1_P2 = rb.getString("academico.produccion.calificacion.modulo.clmd.suma.p1.p2");
			CLMD_ASISTENCIA_TOTAL_DOC = rb.getString("academico.produccion.calificacion.modulo.clmd.asistencia.total.doc");
			CLMD_ID = rb.getString("academico.produccion.calificacion.modulo.clmd.id");
			CLMD_RCES_ID = rb.getString("academico.produccion.calificacion.modulo.rces.id");
			CLMD_NOTA1 = rb.getString("academico.produccion.calificacion.modulo.clmd.nota1");
			CLMD_NOTA2 = rb.getString("academico.produccion.calificacion.modulo.clmd.nota2");
			CLMD_EXAMEN = rb.getString("academico.produccion.calificacion.modulo.clmd.examen");
			CLMD_SUPLETORIO = rb.getString("academico.produccion.calificacion.modulo.clmd.supletorio");
			CLMD_ASISTENCIA1 = rb.getString("academico.produccion.calificacion.modulo.clmd.asistencia1");
			CLMD_ASISTENCIA2 = rb.getString("academico.produccion.calificacion.modulo.clmd.asistencia2");
			CLMD_TOTAL_ASISTENCIA1 = rb.getString("academico.produccion.calificacion.modulo.clmd.total.asistencia1");
			CLMD_TOTAL_ASISTENCIA2 = rb.getString("academico.produccion.calificacion.modulo.clmd.total.asistencia2");
			CLMD_PROMEDIO_NOTAS = rb.getString("academico.produccion.calificacion.modulo.clmd.promedio.notas");
			CLMD_PROMEDIO_ASISTENCIA = rb.getString("academico.produccion.calificacion.modulo.clmd.promedio.asistencia");
			CLMD_ASISTENCIA_TOTAL = rb.getString("academico.produccion.calificacion.modulo.clmd.asistencia.total");
			CLMD_PARAM_RECUPERACION1 = rb.getString("academico.produccion.calificacion.modulo.clmd.param.recuperacion1");
			CLMD_PARAM_RECUPERACION2 = rb.getString("academico.produccion.calificacion.modulo.clmd.param.recuperacion2");
			MLCRPR_ID_MODULO = rb.getString("academico.produccion.calificacion.modulo.mlcrpr.id.modulo");
			TABLA_CAMPO_FORMACION = rb.getString("academico.produccion.campo.formacion");
			CMFR_ID = rb.getString("academico.produccion.campo.formacion.cmfr.id");
			CMFR_TIPO = rb.getString("academico.produccion.campo.formacion.cmfr.tipo");
			CMFR_DESCRIPCION = rb.getString("academico.produccion.campo.formacion.cmfr.descripcion");
			CMFR_ESTADO = rb.getString("academico.produccion.campo.formacion.cmfr.estado");
			TABLA_CANDIDATO_CONTRATO = rb.getString("academico.produccion.candidato.contrato");
			CNCN_ID = rb.getString("academico.produccion.candidato.contrato.cncn.id");
			CNCN_PRS_ID = rb.getString("academico.produccion.candidato.contrato.prs.id");
			CNCN_PRTRCN_ID = rb.getString("academico.produccion.candidato.contrato.prtrcn.id");
			CNCN_ESTADO = rb.getString("academico.produccion.candidato.contrato.cncn.estado");
			CNCN_OBS_RECTORADO = rb.getString("academico.produccion.candidato.contrato.cncn.obs.rectorado");
			CNCN_TERCER_NIVEL = rb.getString("academico.produccion.candidato.contrato.cncn.tercer.nivel");
			CNCN_CUARTO_NIVEL = rb.getString("academico.produccion.candidato.contrato.cncn.cuarto.nivel");
			CNCN_IMPEDIMENTO = rb.getString("academico.produccion.candidato.contrato.cncn.impedimento");
			CNCN_OBS_IMPEDIMENTO = rb.getString("academico.produccion.candidato.contrato.cncn.obs.impedimento");
			CNCN_ARCHIVO_IMPEDIMENTO = rb.getString("academico.produccion.candidato.contrato.cncn.archivo.impedimento");
			CNCN_LABORA_OTRA_INST = rb.getString("academico.produccion.candidato.contrato.cncn.labora.otra.inst");
			CNCN_OBS_LABORA_OTRA_INST = rb.getString("academico.produccion.candidato.contrato.cncn.obs.labora.otra.inst");
			CNCN_ARCHIVO_LABORA_INST = rb.getString("academico.produccion.candidato.contrato.cncn.archivo.labora.inst");
			TABLA_CARGA_HORARIA = rb.getString("academico.produccion.carga.horaria");
			CRHR_ID = rb.getString("academico.produccion.carga.horaria.crhr.id");
			CRHR_TIFNCRHR_ID = rb.getString("academico.produccion.carga.horaria.tifncrhr.id");
			CRHR_PRAC_ID = rb.getString("academico.produccion.carga.horaria.prac.id");
			CRHR_DTPS_ID = rb.getString("academico.produccion.carga.horaria.dtps.id");
			CRHR_MLCRPR_ID = rb.getString("academico.produccion.carga.horaria.mlcrpr.id");
			CRHR_OBSERVACION = rb.getString("academico.produccion.carga.horaria.crhr.observacion");
			CRHR_ESTADO = rb.getString("academico.produccion.carga.horaria.crhr.estado");
			CRHR_NUM_HORAS = rb.getString("academico.produccion.carga.horaria.crhr.num.horas");
			CRHR_ESTADO_ESTADO = rb.getString("academico.produccion.carga.horaria.crhr.estado.eliminacion");
			CRHR_CARRERA_ID = rb.getString("academico.produccion.carga.horaria.crhr.carrera.id");
			TABLA_CARGA_HORARIA_CONTRATO = rb.getString("academico.produccion.carga.horaria.contrato");
			CRHRCN_ID = rb.getString("academico.produccion.carga.horaria.contrato.crhrcn.id");
			CRHRCN_PRTRCN_ID = rb.getString("academico.produccion.carga.horaria.contrato.prtrcn.id");
			CRHRCN_HORA_CLASE = rb.getString("academico.produccion.carga.horaria.contrato.crhrcn.hora.clase");
			CRHRCN_HORA_VINCULACION = rb.getString("academico.produccion.carga.horaria.contrato.crhrcn.hora.vinculacion");
			CRHRCN_TUTORIA = rb.getString("academico.produccion.carga.horaria.contrato.crhrcn.tutoria");
			CRHRCN_PREPARACION_CLASE = rb.getString("academico.produccion.carga.horaria.contrato.crhrcn.preparacion.clase");
			TABLA_CARRERA = rb.getString("academico.produccion.carrera");
			CRR_TIPO = rb.getString("academico.produccion.carrera.crr.tipo");
			CRR_CUPO = rb.getString("academico.produccion.carrera.crr.cupo");
			CRR_ARANCEL = rb.getString("academico.produccion.carrera.crr.arancel");
			CRR_ESPE_CODIGO = rb.getString("academico.produccion.carrera.crr.espe.codigo");
			CRR_ID = rb.getString("academico.produccion.carrera.crr.id");
			CRR_DPN_ID = rb.getString("academico.produccion.carrera.dpn.id");
			CRR_DESCRIPCION = rb.getString("academico.produccion.carrera.crr.descripcion");
			CRR_COD_SNIESE = rb.getString("academico.produccion.carrera.crr.cod.sniese");
			CRR_DETALLE = rb.getString("academico.produccion.carrera.crr.detalle");
			CRR_FECHA_CREACION = rb.getString("academico.produccion.carrera.crr.fecha.creacion");
			CRR_RESOLUCION = rb.getString("academico.produccion.carrera.crr.resolucion");
			CRR_PROCESO = rb.getString("academico.produccion.carrera.crr.proceso");
			CRR_REAJUSTE_MATRICULA = rb.getString("academico.produccion.carrera.crr.reajuste.matricula");
			CRR_ID_ARANCEL = rb.getString("academico.produccion.carrera.crr.id.arancel");
			CRR_TIPO_EVALUACION = rb.getString("academico.produccion.carrera.crr.tipo.evaluacion");
			
			TABLA_CARRERA_AREA= rb.getString("academico.produccion.carrera.area");
			CRAR_ID= rb.getString("academico.produccion.carrera.area.crar.id");
			CRAR_CRR_ID= rb.getString("academico.produccion.carrera.area.crar.crr.id");
			CRAR_AREA_ID= rb.getString("academico.produccion.carrera.area.crar.area.id");
			
			TABLA_CAUSAL = rb.getString("academico.produccion.causal");
			CSL_ID = rb.getString("academico.produccion.causal.csl.id");
			CSL_TICS_ID = rb.getString("academico.produccion.causal.tics.id");
			CSL_DESCRIPCION = rb.getString("academico.produccion.causal.csl.descripcion");
			CSL_CODIGO = rb.getString("academico.produccion.causal.csl.codigo");
			CSL_ESTADO = rb.getString("academico.produccion.causal.csl.estado");
			TABLA_COMPETENCIA = rb.getString("academico.produccion.competencia");
			
			TABLA_CAUSAL_DETALLE_MATRICULA = rb.getString("academico.produccion.causal.detalle.matricula");
			CSDTMT_ID = rb.getString("academico.produccion.causal.detalle.matricula.csdtmt.id");
			CSDTMT_CSL_ID = rb.getString("academico.produccion.causal.detalle.matricula.csl.id");
			CSDTMT_DTMT_ID = rb.getString("academico.produccion.causal.detalle.matricula.dtmt.id");
			
			CMP_ID = rb.getString("academico.produccion.competencia.cmp.id");
			CMP_DENOMINACION = rb.getString("academico.produccion.competencia.cmp.denominacion");
			CMP_DEFINICION = rb.getString("academico.produccion.competencia.cmp.definicion");
			CMP_TIPO = rb.getString("academico.produccion.competencia.cmp.tipo");
			TABLA_COMPETENCIA_PUESTO = rb.getString("academico.produccion.competencia.puesto");
			CMPS_ID = rb.getString("academico.produccion.competencia.puesto.cmps.id");
			CMPS_PST_ID = rb.getString("academico.produccion.competencia.puesto.pst.id");
			CMPS_NVCM_ID = rb.getString("academico.produccion.competencia.puesto.nvcm.id");
			TABLA_COMPROBANTE_PAGO = rb.getString("academico.produccion.comprobante.pago");
			CMPA_ID = rb.getString("academico.produccion.comprobante.pago.cmpa.id");
			CMPA_FCMT_ID = rb.getString("academico.produccion.comprobante.pago.fcmt.id");
			CMPA_CODIGO = rb.getString("academico.produccion.comprobante.pago.cmpa.codigo");
			CMPA_ESTADO = rb.getString("academico.produccion.comprobante.pago.cmpa.estado");
			CMPA_VALOR_TOTAL = rb.getString("academico.produccion.comprobante.pago.cmpa.valor.total");
			CMPA_FECHA_EMISION = rb.getString("academico.produccion.comprobante.pago.cmpa.fecha.emision");
			CMPA_FECHA_CADUCIDAD = rb.getString("academico.produccion.comprobante.pago.cmpa.fecha.caducidad");
			CMPA_TIPO = rb.getString("academico.produccion.comprobante.pago.cmpa.tipo");
			CMPA_FECHA_PAGO = rb.getString("academico.produccion.comprobante.pago.cmpa.fecha.pago");
			CMPA_NUM_COMPROBANTE= rb.getString("academico.produccion.comprobante.pago.cmpa.num.comprobante");
			CMPA_NUM_COMP_SECUENCIAL= rb.getString("academico.produccion.comprobante.pago.cmpa.num.comp.secuencial");
			CMPA_DESCRIPCION= rb.getString("academico.produccion.comprobante.pago.cmpa.descripcion");
			CMPA_TOTAL_PAGO= rb.getString("academico.produccion.comprobante.pago.cmpa.total.pago");
			CMPA_TOTAL_FACULTAD= rb.getString("academico.produccion.comprobante.pago.cmpa.total.facultad");
			CMPA_PROC_SAU= rb.getString("academico.produccion.comprobante.pago.cmpa.proc.sau");
			CMPA_TIPO_UNIDAD= rb.getString("academico.produccion.comprobante.pago.cmpa.tipo.unidad");
			CMPA_VALOR_PAGADO= rb.getString("academico.produccion.comprobante.pago.cmpa.valor.pagado");
			CMPA_CANTIDAD= rb.getString("academico.produccion.comprobante.pago.cmpa.cantidad");
			CMPA_ID_ARANCEL= rb.getString("academico.produccion.comprobante.pago.cmpa.id.arancel");
			CMPA_PAI_CODIGO= rb.getString("academico.produccion.comprobante.pago.cmpa.pai.codigo");
			CMPA_APLICA_GRATUIDAD= rb.getString("academico.produccion.comprobante.pago.cmpa.aplica.gratuidad");
			CMPA_MATR_TIPO= rb.getString("academico.produccion.comprobante.pago.cmpa.matr.tipo");
			CMPA_MODALIDAD= rb.getString("academico.produccion.comprobante.pago.cmpa.modalidad");
			CMPA_ESPE_CODIGO= rb.getString("academico.produccion.comprobante.pago.cmpa.espe.codigo");
			CMPA_FECHA_CADUCA= rb.getString("academico.produccion.comprobante.pago.cmpa.fecha.caduca");
			CMPA_FECHA_ENVIO = rb.getString("academico.produccion.comprobante.pago.cmpa.fecha.envio");
			CMPA_ESTADO_PAGO = rb.getString("academico.produccion.comprobante.pago.cmpa.estado.pago");
			TABLA_CONFIGURACION_CARRERA = rb.getString("academico.produccion.configuracion.carrera");
			CNCR_ID = rb.getString("academico.produccion.configuracion.carrera.cncr.id");
			CNCR_CRR_ID = rb.getString("academico.produccion.configuracion.carrera.crr.id");
			CNCR_VGN_ID = rb.getString("academico.produccion.configuracion.carrera.vgn.id");
			CNCR_MDL_ID = rb.getString("academico.produccion.configuracion.carrera.mdl.id");
			CNCR_TTL_ID = rb.getString("academico.produccion.configuracion.carrera.ttl.id");
			CNCR_UBC_ID = rb.getString("academico.produccion.configuracion.carrera.ubc.id");
			CNCR_TISE_ID = rb.getString("academico.produccion.configuracion.carrera.tise.id");
			CNCR_TIFR_ID = rb.getString("academico.produccion.configuracion.carrera.tifr.id");
			CNCR_DRC_ID = rb.getString("academico.produccion.configuracion.carrera.drc.id");
			TABLA_CONOCIMIENTO = rb.getString("academico.produccion.conocimiento");
			CNC_ID = rb.getString("academico.produccion.conocimiento.cnc.id");
			CNC_DESCRIPCION = rb.getString("academico.produccion.conocimiento.cnc.descripcion");
			TABLA_CONOCIMIENTO_PUESTO = rb.getString("academico.produccion.conocimiento.puesto");
			CNPS_ID = rb.getString("academico.produccion.conocimiento.puesto.cnps.id");
			CNPS_PST_ID = rb.getString("academico.produccion.conocimiento.puesto.pst.id");
			CNPS_CNC_ID = rb.getString("academico.produccion.conocimiento.puesto.cnc.id");
			TABLA_CONTRATO = rb.getString("academico.produccion.contrato");
			CNT_ID = rb.getString("academico.produccion.contrato.cnt.id");
			CNT_TICN_ID = rb.getString("academico.produccion.contrato.ticn.id");
			CNT_TIFN_ID = rb.getString("academico.produccion.contrato.tifn.id");
			TABLA_CONTROL_PROCESO=rb.getString("academico.produccion.control.proceso");
		    CNPR_ID= rb.getString("academico.produccion.control.proceso.cnpr.id");
			CNPR_TIPR_ID=rb.getString("academico.produccion.control.proceso.tipr_id");
			CNPR_USRO_ID=rb.getString("academico.produccion.control.proceso.usro_id");
			CNPR_DETALLE_PROCESO=rb.getString("academico.produccion.control.proceso.cnpr.detalle.proceso");
			CNPR_TIPO_ACCION=rb.getString("academico.produccion.control.proceso.cnpr.tipo.accion");
			CNPR_FECHA_ACCION=rb.getString("academico.produccion.control.proceso.cnpr.fecha.accion");
			CNPR_OBSERVACION_ACCION=rb.getString("academico.produccion.control.proceso.cnpr.observacion.accion");
			TABLA_CONVALIDACION = rb.getString("academico.produccion.convalidacion");
			CNV_ID = rb.getString("academico.produccion.convalidacion.cnv.id");
			CNV_MTR_ID = rb.getString("academico.produccion.convalidacion.mtr.id");
			CNV_MLCRMT_ID = rb.getString("academico.produccion.convalidacion.mlcrmt.id");
			TABLA_COREQUISITO = rb.getString("academico.produccion.corequisito");
			CRQ_ID = rb.getString("academico.produccion.corequisito.crq.id");
			CRQ_MTR_ID = rb.getString("academico.produccion.corequisito.mtr.id");
			CRQ_MTR_COREQUISITO_ID = rb.getString("academico.produccion.corequisito.mtr.corequisito.id");
			CRQ_CODIGO = rb.getString("academico.produccion.corequisito.crq.codigo");
			CRQ_DESCRIPCION = rb.getString("academico.produccion.corequisito.crq.descripcion");
			CRQ_ESTADO = rb.getString("academico.produccion.corequisito.crq.estado");
			TABLA_CRONOGRAMA = rb.getString("academico.produccion.cronograma");
			CRN_ID = rb.getString("academico.produccion.cronograma.crn.id");
			CRN_PRAC_ID = rb.getString("academico.produccion.cronograma.prac.id");
			CRN_TIPO = rb.getString("academico.produccion.cronograma.crn.tipo");
			CRN_DESCRIPCION = rb.getString("academico.produccion.cronograma.crn.descripcion");
			CRN_ESTADO = rb.getString("academico.produccion.cronograma.crn.estado");
			TABLA_CRONOGRAMA_PROCESO_FLUJO = rb.getString("academico.produccion.cronograma.proceso.flujo");
			CRPRFL_PRFL_ID = rb.getString("academico.produccion.cronograma.proceso.flujo.prfl.id");
			CRPRFL_CRN_ID = rb.getString("academico.produccion.cronograma.proceso.flujo.crn.id");
			CRPRFL_ID = rb.getString("academico.produccion.cronograma.proceso.flujo.crprfl.id");
			CRPRFL_ORDINAL = rb.getString("academico.produccion.cronograma.proceso.flujo.crprfl.ordinal");
			TABLA_DEPENDENCIA = rb.getString("academico.produccion.dependencia");
			DPN_ID = rb.getString("academico.produccion.dependencia.dpn.id");
			DPN_SUB_ID = rb.getString("academico.produccion.dependencia.dpn.sub.id");
			DPN_UBC_ID = rb.getString("academico.produccion.dependencia.ubc.id");
			DPN_DESCRIPCION = rb.getString("academico.produccion.dependencia.dpn.descripcion");
			DPN_JERARQUIA = rb.getString("academico.produccion.dependencia.dpn.jerarquia");
			DPN_ESTADO = rb.getString("academico.produccion.dependencia.dpn.estado");
			DPN_CAMPUS = rb.getString("academico.produccion.dependencia.dpn.campus");
			DPN_COD_SORI = rb.getString("academico.produccion.dependencia.dpn.cod.sori");
			DPN_UEJ = rb.getString("academico.produccion.dependencia.dpn.uej");
			TABLA_DESTREZA = rb.getString("academico.produccion.destreza");
			DST_ID = rb.getString("academico.produccion.destreza.dst.id");
			DST_DESCRIPCION = rb.getString("academico.produccion.destreza.dst.descripcion");
			TABLA_DESTREZA_PUESTO = rb.getString("academico.produccion.destreza.puesto");
			DSPS_ID = rb.getString("academico.produccion.destreza.puesto.dsps.id");
			DSPS_DST_ID = rb.getString("academico.produccion.destreza.puesto.dst.id");
			DSPS_PST_ID = rb.getString("academico.produccion.destreza.puesto.pst.id");
			TABLA_DETALLE_CARGA_HORARIA = rb.getString("academico.produccion.detalle.carga.horaria");
			DTCRHR_CARRERA_ID = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.carrera.id");
			DTCRHR_ID = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.id");
			DTCRHR_CRHR_ID = rb.getString("academico.produccion.detalle.carga.horaria.crhr.id");
			DTCRHR_FECHA_INICIO = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.fecha.inicio");
			DTCRHR_FECHA_FIN = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.fecha.fin");
			DTCRHR_NUM_PROYECTO = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.num.proyecto");
			DTCRHR_AREA = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.area");
			DTCRHR_SUBAREA = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.subarea");
			DTCRHR_UNIDAD_ACADEMICA = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.unidad.academica");
			DTCRHR_ASIGNATURA_CLINICAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.asignatura.clinicas");
			DTCRHR_NUM_ALUMNOS_CLINICAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.num.alumnos.clinicas");
			DTCRHR_UNIVERSIDAD_DOCTORADO = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.universidad.doctorado");
			DTCRHR_TITULO_DOCTORADO = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.titulo.doctorado");
			DTCRHR_NIVEL_PROYECTO1 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto1");
			DTCRHR_PROYECTO1 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto1");
			DTCRHR_PROYECTO1_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto1.horas");
			DTCRHR_NIVEL_PROYECTO2 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto2");
			DTCRHR_PROYECTO2 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto2");
			DTCRHR_PROYECTO2_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto2.horas");
			DTCRHR_NIVEL_PROYECTO3 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto3");
			DTCRHR_PROYECTO3 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto3");
			DTCRHR_PROYECTO3_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto3.horas");
			DTCRHR_NIVEL_PROYECTO4 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto4");
			DTCRHR_PROYECTO4 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto4");
			DTCRHR_PROYECTO4_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto4.horas");
			DTCRHR_NIVEL_PROYECTO5 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto5");
			DTCRHR_PROYECTO5 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto5");
			DTCRHR_PROYECTO5_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto5.horas");
			DTCRHR_NIVEL_PROYECTO6 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto6");
			DTCRHR_PROYECTO6 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto6");
			DTCRHR_PROYECTO6_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto6.horas");
			DTCRHR_NIVEL_PROYECTO7 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto7");
			DTCRHR_PROYECTO7 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto7");
			DTCRHR_PROYECTO7_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto7.horas");
			DTCRHR_NIVEL_PROYECTO8 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nivel.proyecto8");
			DTCRHR_PROYECTO8 = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto8");
			DTCRHR_PROYECTO8_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.proyecto8.horas");
			DTCRHR_ESTADO = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.estado");
			DTCRHR_ESTADO_ELIMINACION = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.estado.eliminacion");
			DTCRHR_NUM_HORAS = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.num.horas");
			DTCRHR_NOMBRE_PROYECTO = rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.nombre.proyecto");
			DTCRHR_FUNCION= rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.funcion");
			DTCRHR_NUM_ALUMNOS_TITULACION= rb.getString("academico.produccion.detalle.carga.horaria.dtcrhr.num.alumnos.titulacion");
			TABLA_DETALLE_MATRICULA = rb.getString("academico.produccion.detalle.matricula");
			DTMT_ID = rb.getString("academico.produccion.detalle.matricula.dtmt.id");
			DTMT_ARN_ID = rb.getString("academico.produccion.detalle.matricula.arn.id");
			DTMT_CMPA_ID = rb.getString("academico.produccion.detalle.matricula.cmpa.id");
			DTMT_MLCRPR_ID = rb.getString("academico.produccion.detalle.matricula.mlcrpr.id");
			DTMT_NUMERO = rb.getString("academico.produccion.detalle.matricula.dtmt.numero");
			DTMT_ESTADO = rb.getString("academico.produccion.detalle.matricula.dtmt.estado");
			DTMT_ARCHIVO_ESTUDIANTES = rb.getString("academico.produccion.detalle.matricula.dtmt.archivo.estudiantes");
			DTMT_ESTADO_HISTORICO = rb.getString("academico.produccion.detalle.matricula.dtmt.estado.historico");
			DTMT_ESTADO_CAMBIO = rb.getString("academico.produccion.detalle.matricula.dtmt.estado.cambio");
			DTMT_OBSERVACION_HISTORICO = rb.getString("academico.produccion.detalle.matricula.dtmt.observacion.historico");
			DTMT_OBSERVACION_CAMBIO = rb.getString("academico.produccion.detalle.matricula.dtmt.observacion.cambio");
			DTMT_VALOR_PARCIAL = rb.getString("academico.produccion.detalle.matricula.dtmt.valor.parcial");
			DTMT_FECHA_HISTORICO = rb.getString("academico.produccion.detalle.matricula.dtmt.fecha.historico");
			DTMT_FECHA_CAMBIO = rb.getString("academico.produccion.detalle.matricula.dtmt.fecha.cambio");
			DTMT_USUARIO = rb.getString("academico.produccion.detalle.matricula.dtmt.usuario");
			DTMT_MODIFICACION = rb.getString("academico.produccion.detalle.matricula.dtmt.modificacion");
			DTMT_FECHA_SOLICITUD = rb.getString("academico.produccion.detalle.matricula.dtmt.fecha.solicitud");
			DTMT_FECHA_RESPUESTA = rb.getString("academico.produccion.detalle.matricula.dtmt.fecha.respuesta");
			DTMT_ESTADO_SOLICITUD = rb.getString("academico.produccion.detalle.matricula.dtmt.estado.solicitud");
			DTMT_ESTADO_RESPUESTA = rb.getString("academico.produccion.detalle.matricula.dtmt.estado.respuesta");
			DTMT_CSL_RETIRO_ID= rb.getString("academico.produccion.detalle.matricula.dtmt.csl.retiro.id");
			DTMT_FECHA_VERIFICACION_RETIRO= rb.getString("academico.produccion.detalle.matricula.dtmt.fecha.verificacion.retiro");
			DTMT_ARCHIVO_RESPUESTA = rb.getString("academico.produccion.detalle.matricula.dtmt.archivo.respuesta");
			DTMT_TIPO_ANULACION = rb.getString("academico.produccion.detalle.matricula.dtmt.tipo.anulacion");
			DTMT_FECHA_ANULACION = rb.getString("academico.produccion.detalle.matricula.dtmt.fecha.anulacion");
			DTMT_OBSERVACION_ANULACION = rb.getString("academico.produccion.detalle.matricula.dtmt.observacion.anulacion");
			DTMT_OBSERVACION_FINAL_RETIRO = rb.getString("academico.produccion.detalle.matricula.dtmt.observacion.final.retiro");
			DTMT_ARCHIVO_ANULACION = rb.getString("academico.produccion.detalle.matricula.dtmt.archivo.anulacion");
			DTMT_REGISTRANTE_ANULACION = rb.getString("academico.produccion.detalle.matricula.dtmt.registrante.anulacion");
			
			TABLA_DETALLE_PUESTO = rb.getString("academico.produccion.detalle.puesto");
			DTPS_ID = rb.getString("academico.produccion.detalle.puesto.dtps.id");
			DTPS_PST_ID = rb.getString("academico.produccion.detalle.puesto.pst.id");
			DTPS_FCES_ID = rb.getString("academico.produccion.detalle.puesto.fces.id");
			DTPS_FCDC_ID = rb.getString("academico.produccion.detalle.puesto.fcdc.id");
			DTPS_FCEM_ID = rb.getString("academico.produccion.detalle.puesto.fcem.id");
			DTPS_RLLB_ID = rb.getString("academico.produccion.detalle.puesto.rllb.id");
			DTPS_TIDC_ID = rb.getString("academico.produccion.detalle.puesto.tidc.id");
			DTPS_CRR_ID = rb.getString("academico.produccion.detalle.puesto.crr.id");
			DTPS_ESTADO = rb.getString("academico.produccion.detalle.puesto.dtps.estado");
			DTPS_ESTADO_CATEGORIA = rb.getString("academico.produccion.detalle.puesto.dtps.estado.categoria");
			DTPS_PRAC_ID = rb.getString("academico.produccion.detalle.puesto.dtps.prac.id");
			DTPS_TIPO_CARRERA = rb.getString("academico.produccion.detalle.puesto.dtps.tipo.carrera");
			
			TABLA_DETALLE_TRAMITE_CONTRATO = rb.getString("academico.produccion.detalle.tramite.contrato");
			DTTRCN_ID = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.id");
			DTTRCN_DTPS_ID = rb.getString("academico.produccion.detalle.tramite.contrato.dtps.id");
			DTTRCN_TRM_ID = rb.getString("academico.produccion.detalle.tramite.contrato.trm.id");
			DTTRCN_CNT_ID = rb.getString("academico.produccion.detalle.tramite.contrato.cnt.id");
			DTTRCN_SUB_ID = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.sub.id");
			DTTRCN_NUM_TRAMITE = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.num.tramite");
			DTTRCN_ESTADO_TRAMITE = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.estado.tramite");
			DTTRCN_ESTADO_PROCESO = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.estado.proceso");
			DTTRCN_OBSERVACION = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.observacion");
			DTTRCN_FECHA_INICIO = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.fecha.inicio");
			DTTRCN_FECHA_FIN = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.fecha.fin");
			DTTRCN_ARCHIVO_RESOLUCION = rb.getString("academico.produccion.detalle.tramite.contrato.dttrcn.archivo.resolucion");
			TABLA_DURACION = rb.getString("academico.produccion.duracion");
			DRC_ID = rb.getString("academico.produccion.duracion.drc.id");
			DRC_TIPO = rb.getString("academico.produccion.duracion.drc.tipo");
			DRC_TIEMPO = rb.getString("academico.produccion.duracion.drc.tiempo");
			DRC_TIPO_SNIESE = rb.getString("academico.produccion.duracion.drc.tipo.sniese");
			TABLA_EDIFICIO = rb.getString("academico.produccion.edificio");
			EDF_ID = rb.getString("academico.produccion.edificio.edf.id");
			EDF_DPN_ID = rb.getString("academico.produccion.edificio.dpn.id");
			EDF_DESCRIPCION = rb.getString("academico.produccion.edificio.edf.descripcion");
			EDF_ESTADO = rb.getString("academico.produccion.edificio.edf.estado");
			EDF_CODIGO = rb.getString("academico.produccion.edificio.edf.codigo");
			EDF_LOCALIZACION = rb.getString("academico.produccion.edificio.edf.localizacion");
			TABLA_ETNIA = rb.getString("academico.produccion.etnia");
			ETN_ID = rb.getString("academico.produccion.etnia.etn.id");
			ETN_CODIGO_SNIESE = rb.getString("academico.produccion.etnia.etn.codigo.sniese");
			ETN_DESCRIPCION = rb.getString("academico.produccion.etnia.etn.descripcion");
			TABLA_EXCEDENTE = rb.getString("academico.produccion.excedente");
			EXC_ID = rb.getString("academico.produccion.excedente.exc.id");
			EXC_DESCRIPCION = rb.getString("academico.produccion.excedente.exc.descripcion");
			EXC_ESTADO = rb.getString("academico.produccion.excedente.exc.estado");
			EXC_TIPO = rb.getString("academico.produccion.excedente.exc.tipo");
			TABLA_EXPERIENCIA_LABORAL_CONTR = rb.getString("academico.produccion.experiencia.laboral.contr");
			EXLBCN_CNCN_ID = rb.getString("academico.produccion.experiencia.laboral.contr.cncn.id");
			EXLBCN_ID = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.id");
			EXLBCN_TIPO_INSTITUCION = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.tipo.institucion");
			EXLBCN_DESCRIPCION = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.descripcion");
			EXLBCN_UNIDAD = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.unidad");
			EXLBCN_PUESTO = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.puesto");
			EXLBCN_FECHA_INGRESO = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.fecha.ingreso");
			EXLBCN_FECHA_SALIDA = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.fecha.salida");
			EXLBCN_MOTIVO_INGRESO = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.motivo.ingreso");
			EXLBCN_MOTIVO_SALIDA = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.motivo.salida");
			EXLBCN_TIPO = rb.getString("academico.produccion.experiencia.laboral.contr.exlbcn.tipo");
			TABLA_FICHA_DOCENTE = rb.getString("academico.produccion.ficha.docente");
			FCDC_ESTADO = rb.getString("academico.produccion.ficha.docente.fcdc.estado");
			FCDC_ID = rb.getString("academico.produccion.ficha.docente.fcdc.id");
			FCDC_PRS_ID = rb.getString("academico.produccion.ficha.docente.prs.id");
			TABLA_FICHA_EMPLEADO = rb.getString("academico.produccion.ficha.empleado");
			FCEM_ID = rb.getString("academico.produccion.ficha.empleado.fcem.id");
			FCEM_PRS_ID = rb.getString("academico.produccion.ficha.empleado.prs.id");
			TABLA_FICHA_ESTUDIANTE = rb.getString("academico.produccion.ficha.estudiante");
			FCES_ID = rb.getString("academico.produccion.ficha.estudiante.fces.id");
			FCES_FECHA_INICIO = rb.getString("academico.produccion.ficha.estudiante.fces.fecha.inicio");
			FCES_FECHA_EGRESAMIENTO = rb.getString("academico.produccion.ficha.estudiante.fces.fecha.egresamiento");
			FCES_FECHA_ACTA_GRADO = rb.getString("academico.produccion.ficha.estudiante.fces.fecha.acta.grado");
			FCES_NUM_ACTA_GRADO = rb.getString("academico.produccion.ficha.estudiante.fces.num.acta.grado");
			FCES_FECHA_REFRENDACION = rb.getString("academico.produccion.ficha.estudiante.fces.fecha.refrendacion");
			FCES_NUM_REFRENDACION = rb.getString("academico.produccion.ficha.estudiante.fces.num.refrendacion");
			FCES_CRR_ESTUD_PREVIOS = rb.getString("academico.produccion.ficha.estudiante.fces.crr.estud.previos");
			FCES_TIEMPO_ESTUD_REC = rb.getString("academico.produccion.ficha.estudiante.fces.tiempo.estud.rec");
			FCES_TIPO_DURAC_REC = rb.getString("academico.produccion.ficha.estudiante.fces.tipo.durac.rec");
			FCES_TIPO_COLEGIO = rb.getString("academico.produccion.ficha.estudiante.fces.tipo.colegio");
			FCES_TIPO_COLEGIO_SNIESE = rb.getString("academico.produccion.ficha.estudiante.fces.tipo.colegio.sniese");
			FCES_NOTA_PROM_ACUMULADO = rb.getString("academico.produccion.ficha.estudiante.fces.nota.prom.acumulado");
			FCES_NOTA_TRAB_TITULACION = rb.getString("academico.produccion.ficha.estudiante.fces.nota.trab.titulacion");
			FCES_LINK_TESIS = rb.getString("academico.produccion.ficha.estudiante.fces.link.tesis");
			FCES_REC_ESTUD_PREVIOS = rb.getString("academico.produccion.ficha.estudiante.fces.rec.estud.previos");
			FCES_REC_ESTUD_PREV_SNIESE = rb.getString("academico.produccion.ficha.estudiante.fces.rec.estud.prev.sniese");
			FCES_FECHA_CREACION = rb.getString("academico.produccion.ficha.estudiante.fces.fecha.creacion");
			FCES_PRS_ID = rb.getString("academico.produccion.ficha.estudiante.prs.id");
			FCES_INAC_ID = rb.getString("academico.produccion.ficha.estudiante.inac.id");
			FCES_FCIN_ID = rb.getString("academico.produccion.ficha.estudiante.fcin.id");
			FCES_ESTADO_UNIVERSITARIO = rb.getString("academico.produccion.ficha.estudiante.fces.estado.universitario");
			FCES_ESTADO_MATRICULA = rb.getString("academico.produccion.ficha.estudiante.fces.estado.matricula");
			FCES_OBSERVACION = rb.getString("academico.produccion.ficha.estudiante.fces.observacion");
			FCES_TITULO_BACHILLER = rb.getString("academico.produccion.ficha.estudiante.fces.titulo.bachiller");
			FCES_NOTA_GRADO_SECUNDARIA = rb.getString("academico.produccion.ficha.estudiante.fces.nota.grado.secundaria");
			FCES_INAC_TTL_ID = rb.getString("academico.produccion.ficha.estudiante.fces.inac.ttl.id");
			FCES_COLEGIO_ID = rb.getString("academico.produccion.ficha.estudiante.fces.colegio.id");
			FCES_TIPO_UNIV_ESTUD_PREV = rb.getString("academico.produccion.ficha.estudiante.fces.tipo.univ.estud.prev");
			FCES_ESTADO_ESTUD_PREV = rb.getString("academico.produccion.ficha.estudiante.fces.estado.estud.prev");
			FCES_UBC_COLEGIO = rb.getString("academico.produccion.ficha.estudiante.fces.ubc.colegio");
			FCES_FECHA_ACTUALIZACION_DATOS = rb.getString("academico.produccion.ficha.estudiante.fces.fecha.actualizacion.datos");
			FCES_UNIV_ESTUD_PREV_ID= rb.getString("academico.produccion.ficha.estudiante.fces.univ.estud.prev.id");
			FCES_TIT_ESTUD_PREV_ID = rb.getString("academico.produccion.ficha.estudiante.fces.tit.estud.pre.id");
			FCES_REG_TITULO_PREV = rb.getString("academico.produccion.ficha.estudiante.fces.reg.titulo.prev");
			TABLA_FICHA_INSCRIPCION = rb.getString("academico.produccion.ficha.inscripcion");
			FCIN_ID = rb.getString("academico.produccion.ficha.inscripcion.fcin.id");
			FCIN_USRO_ID = rb.getString("academico.produccion.ficha.inscripcion.usro.id");
			FCIN_CNCR_ID = rb.getString("academico.produccion.ficha.inscripcion.cncr.id");
			FCIN_PRAC_ID = rb.getString("academico.produccion.ficha.inscripcion.prac.id");
			FCIN_FECHA_INSCRIPCION = rb.getString("academico.produccion.ficha.inscripcion.fcin.fecha.inscripcion");
			FCIN_OBSERVACION = rb.getString("academico.produccion.ficha.inscripcion.fcin.observacion");
			FCIN_OBSERVACION_INGRESO = rb.getString("academico.produccion.ficha.inscripcion.fcin.observacion.ingreso");
			FCIN_TIPO = rb.getString("academico.produccion.ficha.inscripcion.fcin.tipo");
			FCIN_TIPO_INGRESO = rb.getString("academico.produccion.ficha.inscripcion.fcin.tipo.ingreso");
			FCIN_ESTADO_INGRESO = rb.getString("academico.produccion.ficha.inscripcion.fcin.estado.ingreso");
			FCIN_DOCUMENTO_INGRESO = rb.getString("academico.produccion.ficha.inscripcion.fcin.documento.ingreso");
			FCIN_MATRICULADO = rb.getString("academico.produccion.ficha.inscripcion.fcin.matriculado");
			FCIN_ESTADO = rb.getString("academico.produccion.ficha.inscripcion.fcin.estado");
			FCIN_NIVEL_INGRESO = rb.getString("academico.produccion.ficha.inscripcion.fcin.nivel.ingreso");
			FCIN_ENCUESTA = rb.getString("academico.produccion.ficha.inscripcion.fcin.encuesta");
			FCIN_NOTA_ENES = rb.getString("academico.produccion.ficha.inscripcion.fcin.nota.enes");
			FCIN_CARRERA = rb.getString("academico.produccion.ficha.inscripcion.fcin.carrera");
			FCIN_CARRERA_SIIU = rb.getString("academico.produccion.ficha.inscripcion.fcin.carrera.siiu");
			FCIN_CNCR_AREA = rb.getString("academico.produccion.ficha.inscripcion.fcin.cncr.area");
			FCIN_FCIN_NIVELACION = rb.getString("academico.produccion.ficha.inscripcion.fcin.fcin.nivelacion");
			FCIN_ANIO_ABANDONA_CARRERA = rb.getString("academico.produccion.ficha.inscripcion.fcin.anio.abandona.carrera");
			FCIN_CRR_ANTERIOR_ID=rb.getString("academico.produccion.ficha.inscripcion.fcin.crr.anterior.id");
			FCIN_CNCR_ID_NIVELACION=rb.getString("academico.produccion.ficha.inscripcion.fcin.cncr.id.nivelacion");
			FCIN_FCIN_ANTERIOR_ID=rb.getString("academico.produccion.ficha.inscripcion.fcin.fcin.anterior.id");
			FCIN_NOTA_UBICACION=rb.getString("academico.produccion.ficha.inscripcion.fcin.nota.ubicacion");
			FCIN_VIGENTE=rb.getString("academico.produccion.ficha.inscripcion.fcin.vigente");
			FCIN_APLICA_NOTA_ENES=rb.getString("academico.produccion.ficha.inscripcion.fcin.aplica.nota.enes");
			FCIN_NOTA_CORTE_ID=rb.getString("academico.produccion.ficha.inscripcion.fcin.nota.corte.id");		
			FCIN_ESTADO_RETIRO=rb.getString("academico.produccion.ficha.inscripcion.fcin.estado.retiro");
			FCIN_REINICIO_ORIGEN=rb.getString("academico.produccion.ficha.inscripcion.fcin.reinicio.origen");
			TABLA_FICHA_MATRICULA = rb.getString("academico.produccion.ficha.matricula");
			FCMT_PRAC_ID = rb.getString("academico.produccion.ficha.matricula.fcmt.prac.id");
			FCMT_ID = rb.getString("academico.produccion.ficha.matricula.fcmt.id");
			FCMT_FCES_ID = rb.getString("academico.produccion.ficha.matricula.fces.id");
			FCMT_PLCR_ID = rb.getString("academico.produccion.ficha.matricula.plcr.id");
			FCMT_NIVEL_UBICACION = rb.getString("academico.produccion.ficha.matricula.fcmt.nivel.ubicacion");
			FCMT_ESTADO = rb.getString("academico.produccion.ficha.matricula.fcmt.estado");
			FCMT_FECHA_CONVALIDACION = rb.getString("academico.produccion.ficha.matricula.fcmt.fecha.convalidacion");
			FCMT_TIPO = rb.getString("academico.produccion.ficha.matricula.fcmt.tipo");
			FCMT_MODALIDAD = rb.getString("academico.produccion.ficha.matricula.fcmt.modalidad");
			FCMT_VALOR_TOTAL = rb.getString("academico.produccion.ficha.matricula.fcmt.valor.total");
			FCMT_FECHA_MATRICULA = rb.getString("academico.produccion.ficha.matricula.fcmt.fecha.matricula");
			FCMT_FECHA_ESTADO = rb.getString("academico.produccion.ficha.matricula.fcmt.fecha.estado");
			TABLA_FORMATO_CONTRATO = rb.getString("academico.produccion.formato.contrato");
			FRCN_ID = rb.getString("academico.produccion.formato.contrato.frcn.id");
			FRCN_TICN_ID = rb.getString("academico.produccion.formato.contrato.ticn.id");
			FRCN_DESCRIPCON = rb.getString("academico.produccion.formato.contrato.frcn.descripcon");
			FRCN_ESTADO = rb.getString("academico.produccion.formato.contrato.frcn.estado");
			TABLA_FUNCION = rb.getString("academico.produccion.funcion");
			FNC_ID = rb.getString("academico.produccion.funcion.fnc.id");
			FNC_DESCRIPCION = rb.getString("academico.produccion.funcion.fnc.descripcion");
			TABLA_FUNCION_CARGA_HORARIA = rb.getString("academico.produccion.funcion.carga.horaria");
			FNCRHR_ID = rb.getString("academico.produccion.funcion.carga.horaria.fncrhr.id");
			FNCRHR_DESCRIPCION = rb.getString("academico.produccion.funcion.carga.horaria.fncrhr.descripcion");
			FNCRHR_HORAS_ASIGNADAS = rb.getString("academico.produccion.funcion.carga.horaria.fncrhr.horas.asignadas");
			FNCRHR_HORAS_MINIMO = rb.getString("academico.produccion.funcion.carga.horaria.fncrhr.horas.minimo");
			FNCRHR_HORAS_MAXIMO = rb.getString("academico.produccion.funcion.carga.horaria.fncrhr.horas.maximo");
			TABLA_FUNCION_PUESTO = rb.getString("academico.produccion.funcion.puesto");
			FNPS_ID = rb.getString("academico.produccion.funcion.puesto.fnps.id");
			FNPS_PST_ID = rb.getString("academico.produccion.funcion.puesto.pst.id");
			FNPS_FNC_ID = rb.getString("academico.produccion.funcion.puesto.fnc.id");
			TABLA_GRATUIDAD = rb.getString("academico.produccion.gratuidad");
			GRT_ID = rb.getString("academico.produccion.gratuidad.grt.id");
			GRT_FCES_ID = rb.getString("academico.produccion.gratuidad.fces.id");
			GRT_TIGR_ID = rb.getString("academico.produccion.gratuidad.tigr.id");
			GRT_FCMT_ID = rb.getString("academico.produccion.gratuidad.fcmt.id");
			GRT_ESTADO = rb.getString("academico.produccion.gratuidad.grt.estado");
			TABLA_GRUPO_OCUPACIONAL = rb.getString("academico.produccion.grupo.ocupacional");
			GROC_ID = rb.getString("academico.produccion.grupo.ocupacional.groc.id");
			GROC_DESCRIPCION = rb.getString("academico.produccion.grupo.ocupacional.groc.descripcion");
			GROC_GRADO = rb.getString("academico.produccion.grupo.ocupacional.groc.grado");
			GROC_RMU = rb.getString("academico.produccion.grupo.ocupacional.groc.rmu");
			GROC_RGM_ID = rb.getString("academico.produccion.grupo.ocupacional.rgm.id");
			GROC_RMU_HORA = rb.getString("academico.produccion.grupo.ocupacional.groc.rmu.hora");
			TABLA_HISTORIAL_NOTA_CORTE = rb.getString("academico.produccion.historial.nota.corte");
			HSNOCR_ID = rb.getString("academico.produccion.historial.nota.corte.hsnocr.id");
			HSNOCR_FECHA_REGISTRO = rb.getString("academico.produccion.historial.nota.corte.hsnocr.fecha.registro");
			HSNOCR_USR_ID = rb.getString("academico.produccion.historial.nota.corte.hsnocr.usr.id");
			HSNOCR_USR_NICK = rb.getString("academico.produccion.historial.nota.corte.hsnocr.usr.nick");
			HSNOCR_TIPO_PROCESO = rb.getString("academico.produccion.historial.nota.corte.hsnocr.tipo.proceso");
			HSNOCR_NOTA_ANTERIOR = rb.getString("academico.produccion.historial.nota.corte.hsnocr.nota.anterior");
			TABLA_HORA_CLASE = rb.getString("academico.produccion.hora.clase");
			HOCL_ID = rb.getString("academico.produccion.hora.clase.hocl.id");
			HOCL_DESCRIPCION = rb.getString("academico.produccion.hora.clase.hocl.descripcion");
			HOCL_ESTADO = rb.getString("academico.produccion.hora.clase.hocl.estado");
			HOCL_HORA_INICIO = rb.getString("academico.produccion.hora.clase.hocl.hora.inicio");
			HOCL_HORA_FIN = rb.getString("academico.produccion.hora.clase.hocl.hora.fin");
			TABLA_HORA_CLASE_AULA = rb.getString("academico.produccion.hora.clase.aula");
			HOCLAL_ID = rb.getString("academico.produccion.hora.clase.aula.hoclal.id");
			HOCLAL_HOCL_ID = rb.getString("academico.produccion.hora.clase.aula.hocl.id");
			HOCLAL_ALA_ID = rb.getString("academico.produccion.hora.clase.aula.ala.id");
			HOCLAL_ESTADO = rb.getString("academico.produccion.hora.clase.aula.hoclal.estado");
			TABLA_HORARIO_ACADEMICO = rb.getString("academico.produccion.horario.academico");
			HRAC_ID = rb.getString("academico.produccion.horario.academico.hrac.id");
			HRAC_DESCRIPCION = rb.getString("academico.produccion.horario.academico.hrac.descripcion");
			HRAC_DIA = rb.getString("academico.produccion.horario.academico.hrac.dia");
			HRAC_HORA_INICIO = rb.getString("academico.produccion.horario.academico.hrac.hora.inicio");
			HRAC_HORA_FIN = rb.getString("academico.produccion.horario.academico.hrac.hora.fin");
			HRAC_ESTADO = rb.getString("academico.produccion.horario.academico.hrac.estado");
			HRAC_HOCLAL_ID = rb.getString("academico.produccion.horario.academico.hoclal.id");
			HRAC_MLCRPR_ID = rb.getString("academico.produccion.horario.academico.mlcrpr.id");
			MLCRPR_ID_COMP = rb.getString("academico.produccion.horario.academico.mlcrpr.id.comp");
			TABLA_INFORME_TECNICO = rb.getString("academico.produccion.informe.tecnico");
			INTC_ID = rb.getString("academico.produccion.informe.tecnico.intc.id");
			INTC_PRTRCN_ID = rb.getString("academico.produccion.informe.tecnico.prtrcn.id");
			INTC_OBSERVACION = rb.getString("academico.produccion.informe.tecnico.intc.observacion");
			INTC_ESTADO = rb.getString("academico.produccion.informe.tecnico.intc.estado");
			TABLA_INSTITUCION_ACADEMICA = rb.getString("academico.produccion.institucion.academica");
			INAC_ID = rb.getString("academico.produccion.institucion.academica.inac.id");
			INAC_CODIGO_SNIESE = rb.getString("academico.produccion.institucion.academica.inac.codigo.sniese");
			INAC_DESCRIPCION = rb.getString("academico.produccion.institucion.academica.inac.descripcion");
			INAC_NIVEL = rb.getString("academico.produccion.institucion.academica.inac.nivel");
			INAC_TIPO = rb.getString("academico.produccion.institucion.academica.inac.tipo");
			INAC_TIPO_SNIESE = rb.getString("academico.produccion.institucion.academica.inac.tipo.sniese");
			INAC_UBC_ID = rb.getString("academico.produccion.institucion.academica.ubc.id");
			TABLA_INSTRUCCION_PUESTO = rb.getString("academico.produccion.instruccion.puesto");
			INPS_ID = rb.getString("academico.produccion.instruccion.puesto.inps.id");
			INPS_NVIN_ID = rb.getString("academico.produccion.instruccion.puesto.nvin.id");
			INPS_PST_ID = rb.getString("academico.produccion.instruccion.puesto.pst.id");
			INPS_ANIO = rb.getString("academico.produccion.instruccion.puesto.inps.anio");
			INPS_MES = rb.getString("academico.produccion.instruccion.puesto.inps.mes");
			TABLA_ITINERARIO = rb.getString("academico.produccion.itinerario");
			ITN_ID = rb.getString("academico.produccion.itinerario.itn.id");
			ITN_CRR_ID = rb.getString("academico.produccion.itinerario.crr.id");
			ITN_DESCRIPCION = rb.getString("academico.produccion.itinerario.itn.descripcion");
			ITN_ESTADO = rb.getString("academico.produccion.itinerario.itn.estado");
			ITN_OBSERVACION = rb.getString("academico.produccion.itinerario.itn.observacion");
			TABLA_ITINERARIO_MALLA_MATERIA = rb.getString("academico.produccion.itinerario.malla.materia");
			ITMLMT_ID = rb.getString("academico.produccion.itinerario.malla.materia.itmlmt.id");
			ITMLMT_ESTADO = rb.getString("academico.produccion.itinerario.malla.materia.itmlmt.estado");
			ITMLMT_MLCRMT_ID = rb.getString("academico.produccion.itinerario.malla.materia.mlcrmt.id");
			ITMLMT_ITN_ID = rb.getString("academico.produccion.itinerario.malla.materia.itn.id");
			
			TABLA_MALLA_CURRICULAR = rb.getString("academico.produccion.malla.curricular");
			MLCR_ID = rb.getString("academico.produccion.malla.curricular.mlcr.id");
			MLCR_TIFRML_ID = rb.getString("academico.produccion.malla.curricular.tifrml.id");
			MLCR_CRR_ID = rb.getString("academico.produccion.malla.curricular.crr.id");
			MLCR_CODIGO = rb.getString("academico.produccion.malla.curricular.mlcr.codigo");
			MLCR_DESCRIPCION = rb.getString("academico.produccion.malla.curricular.mlcr.descripcion");
			MLCR_ESTADO = rb.getString("academico.produccion.malla.curricular.mlcr.estado");
			MLCR_FECHA_INICIO = rb.getString("academico.produccion.malla.curricular.mlcr.fecha.inicio");
			MLCR_FECHA_FIN = rb.getString("academico.produccion.malla.curricular.mlcr.fecha.fin");
			MLCR_TOTAL_HORAS = rb.getString("academico.produccion.malla.curricular.mlcr.total.horas");
			MLCR_TOTAL_MATERIAS = rb.getString("academico.produccion.malla.curricular.mlcr.total.materias");
			MLCR_TIPO_ORG_APRENDIZAJE = rb.getString("academico.produccion.malla.curricular.mlcr.tipo.org.aprendizaje");
			MLCR_VIGENTE = rb.getString("academico.produccion.malla.curricular.mlcr.vigente");
			MLCR_TIPO_APROBACION = rb.getString("academico.produccion.malla.curricular.mlcr.tipo.aprobacion");
			TABLA_MALLA_CURRICULAR_MATERIA = rb.getString("academico.produccion.malla.curricular.materia");
			MLCRMT_ID = rb.getString("academico.produccion.malla.curricular.materia.mlcrmt.id");
			MLCRMT_MLCR_ID = rb.getString("academico.produccion.malla.curricular.materia.mlcr.id");
			MLCRMT_MTR_ID = rb.getString("academico.produccion.malla.curricular.materia.mtr.id");
			MLCRMT_UNFR_ID = rb.getString("academico.produccion.malla.curricular.materia.unfr.id");
			MLCRMT_NVL_ID = rb.getString("academico.produccion.malla.curricular.materia.nvl.id");
			MLCRMT_NVL_SUB_ID = rb.getString("academico.produccion.malla.curricular.materia.nvl.sub.id");
			MLCRMT_ESTADO = rb.getString("academico.produccion.malla.curricular.materia.mlcrmt.estado");
			
			TABLA_MALLA_CURRICULAR_NIVEL = rb.getString("academico.produccion.malla.curricular.nivel");
			MLCRNV_ID = rb.getString("academico.produccion.malla.curricular.nivel.mlcrnv.id");
			MLCRNV_CREDITOS = rb.getString("academico.produccion.malla.curricular.nivel.mlcrnv.creditos");
			MLCRNV_CREDITOS_ACUMULADO = rb.getString("academico.produccion.malla.curricular.nivel.mlcrnv.creditos.acumulado");
			
			TABLA_MALLA_CURRICULAR_PARALELO = rb.getString("academico.produccion.malla.curricular.paralelo");
			MLCRPR_ID = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrpr.id");
			MLCRPR_PRL_ID = rb.getString("academico.produccion.malla.curricular.paralelo.prl.id");
			MLCRPR_MLCRMT_ID = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrmt.id");
			MLCRPR_INSCRITOS = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrpr.inscritos");
			MLCRPR_CUPO = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrpr.cupo");
			MLCRPR_MODALIDAD = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrpr.modalidad");

			TABLA_MALLA_PERIODO = rb.getString("academico.produccion.malla.periodo");
			MLCRPR_RESERVA_REPETIDOS = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrpr.reserva.repetidos");
			MLCRPR_NIVELACION_CRR_ID = rb.getString("academico.produccion.malla.curricular.paralelo.mlcrpr.nivelacion.crr.id");
			MLPR_ID = rb.getString("academico.produccion.malla.periodo.mlpr.id");
			MLPR_MLCR_ID = rb.getString("academico.produccion.malla.periodo.mlcr.id");
			MLPR_PRAC_ID = rb.getString("academico.produccion.malla.periodo.prac.id");
			MLPR_ESTADO = rb.getString("academico.produccion.malla.periodo.mlpr.estado");
			
			TABLA_MATERIA = rb.getString("academico.produccion.materia");
			MTR_UNIDAD_MEDIDA = rb.getString("academico.produccion.materia.mtr.unidad.medida");
			MTR_CREDITOS = rb.getString("academico.produccion.materia.mtr.creditos");
			MTR_ID = rb.getString("academico.produccion.materia.mtr.id");
			MTR_CMFR_ID = rb.getString("academico.produccion.materia.cmfr.id");
			MTR_TIMT_ID = rb.getString("academico.produccion.materia.timt.id");
			MTR_SUB_ID = rb.getString("academico.produccion.materia.mtr.sub.id");
			MTR_NCPRCR_ID = rb.getString("academico.produccion.materia.ncprcr.id");
			MTR_CRR_ID = rb.getString("academico.produccion.materia.crr.id");
			MTR_CODIGO = rb.getString("academico.produccion.materia.mtr.codigo");
			MTR_CODIGO_SNIESE = rb.getString("academico.produccion.materia.mtr.codigo.sniese");
			MTR_DESCRIPCION = rb.getString("academico.produccion.materia.mtr.descripcion");
			MTR_ESTADO = rb.getString("academico.produccion.materia.mtr.estado");
			MTR_HORAS = rb.getString("academico.produccion.materia.mtr.horas");
			MTR_INTEGRADORA_HORAS = rb.getString("academico.produccion.materia.mtr.integradora.horas");
			MTR_PRE_PROFESIONAL_HORAS = rb.getString("academico.produccion.materia.mtr.pre.profesional.horas");
			MTR_HORAS_CIEN = rb.getString("academico.produccion.materia.mtr.horas.cien");
			MTR_RELACION_TRABAJO = rb.getString("academico.produccion.materia.mtr.relacion.trabajo");
			MTR_HORAS_PRACTICAS = rb.getString("academico.produccion.materia.mtr.horas.practicas");
			MTR_HORAS_TRAB_AUTONOMO = rb.getString("academico.produccion.materia.mtr.horas.trab.autonomo");
			MTR_HORAS_PRAC_SEMA = rb.getString("academico.produccion.materia.mtr.horas.prac.sema");
			MTR_HORAS_AUTONO_SEMA = rb.getString("academico.produccion.materia.mtr.horas.autono.sema");
			TABLA_MODALIDAD = rb.getString("academico.produccion.modalidad");
			MDL_ID = rb.getString("academico.produccion.modalidad.mdl.id");
			MDL_DESCRIPCION = rb.getString("academico.produccion.modalidad.mdl.descripcion");
			TABLA_NOTA_CORTE = rb.getString("academico.produccion.nota.corte");
			NOCR_ID = rb.getString("academico.produccion.nota.corte.nocr.id");
			NOCR_ESTADO = rb.getString("academico.produccion.nota.corte.nocr.estado");
			NOCR_NOTA = rb.getString("academico.produccion.nota.corte.nocr.nota");
			NOCR_PRAC_ID = rb.getString("academico.produccion.nota.corte.nocr.prac.id");
			NOCR_CRR_ID = rb.getString("academico.produccion.nota.corte.nocr.crr.id");
			TABLA_NIVEL = rb.getString("academico.produccion.nivel");
			NVL_ID = rb.getString("academico.produccion.nivel.nvl.id");
			NVL_DESCRIPCION = rb.getString("academico.produccion.nivel.nvl.descripcion");
			NVL_ESTADO = rb.getString("academico.produccion.nivel.nvl.estado");
			NVL_NUMERAL = rb.getString("academico.produccion.nivel.nvl.numeral");
			TABLA_NIVEL_APERTURA = rb.getString("academico.produccion.nivel.apertura");
			NVAP_ID = rb.getString("academico.produccion.nivel.apertura.nvap.id");
			NVAP_DESCRIPCION = rb.getString("academico.produccion.nivel.apertura.nvap.descripcion");
			TABLA_NIVEL_COMPETENCIA = rb.getString("academico.produccion.nivel.competencia");
			NVCM_ID = rb.getString("academico.produccion.nivel.competencia.nvcm.id");
			NVCM_CMP_ID = rb.getString("academico.produccion.nivel.competencia.cmp.id");
			NVCM_DESCRIPCION = rb.getString("academico.produccion.nivel.competencia.nvcm.descripcion");
			NVCM_COMPORTAMIENTO = rb.getString("academico.produccion.nivel.competencia.nvcm.comportamiento");
			TABLA_NIVEL_FORMACION = rb.getString("academico.produccion.nivel.formacion");
			NVFR_ID = rb.getString("academico.produccion.nivel.formacion.nvfr.id");
			NVFR_RGAC_ID = rb.getString("academico.produccion.nivel.formacion.rgac.id");
			NVFR_DESCRIPCION = rb.getString("academico.produccion.nivel.formacion.nvfr.descripcion");
			TABLA_NIVEL_INSTRUCCION = rb.getString("academico.produccion.nivel.instruccion");
			NVIN_ID = rb.getString("academico.produccion.nivel.instruccion.nvin.id");
			NVIN_DESCRIPCION = rb.getString("academico.produccion.nivel.instruccion.nvin.descripcion");
			TABLA_NUCLEO_PROBLEMICO = rb.getString("academico.produccion.nucleo.problemico");
			NCPR_ID = rb.getString("academico.produccion.nucleo.problemico.ncpr.id");
			NCPR_DESCRIPCION = rb.getString("academico.produccion.nucleo.problemico.ncpr.descripcion");
			NCPR_ESTADO = rb.getString("academico.produccion.nucleo.problemico.ncpr.estado");
			TABLA_NUCLEO_PROBLEMICO_CARRERA = rb.getString("academico.produccion.nucleo.problemico.carrera");
			NCPRCR_ID = rb.getString("academico.produccion.nucleo.problemico.carrera.ncprcr.id");
			NCPRCR_CRR_ID = rb.getString("academico.produccion.nucleo.problemico.carrera.crr.id");
			NCPRCR_ESTADO = rb.getString("academico.produccion.nucleo.problemico.carrera.ncprcr.estado");
			NCPRCR_NCPR_ID = rb.getString("academico.produccion.nucleo.problemico.carrera.ncpr.id");
			TABLA_PARALELO = rb.getString("academico.produccion.paralelo");
			PRL_ID = rb.getString("academico.produccion.paralelo.prl.id");
			PRL_CRR_ID = rb.getString("academico.produccion.paralelo.crr.id");
			PRL_PRAC_ID = rb.getString("academico.produccion.paralelo.prac.id");
			PRL_CODIGO = rb.getString("academico.produccion.paralelo.prl.codigo");
			PRL_DESCRIPCION = rb.getString("academico.produccion.paralelo.prl.descripcion");
			PRL_ESTADO = rb.getString("academico.produccion.paralelo.prl.estado");
			PRL_CUPO = rb.getString("academico.produccion.paralelo.prl.cupo");
			PRL_FECHA = rb.getString("academico.produccion.paralelo.prl.fecha");
			PRL_INICIO_CLASE = rb.getString("academico.produccion.paralelo.prl.inicio.clase");
			PRL_FIN_CLASE = rb.getString("academico.produccion.paralelo.prl.fin.clase");
			TABLA_PERIODO_ACADEMICO = rb.getString("academico.produccion.periodo.academico");
			PRAC_ID = rb.getString("academico.produccion.periodo.academico.prac.id");
			PRAC_DESCRIPCION = rb.getString("academico.produccion.periodo.academico.prac.descripcion");
			PRAC_ESTADO = rb.getString("academico.produccion.periodo.academico.prac.estado");
			PRAC_FECHA_INCIO = rb.getString("academico.produccion.periodo.academico.prac.fecha.incio");
			PRAC_FECHA_FIN = rb.getString("academico.produccion.periodo.academico.prac.fecha.fin");
			PRAC_TIPO = rb.getString("academico.produccion.periodo.academico.prac.tipo");
			TABLA_PERSONA = rb.getString("academico.produccion.persona");
			PRS_MAIL_INSTITUCIONAL = rb.getString("academico.produccion.persona.prs.mail.institucional");
			PRS_TELEFONO = rb.getString("academico.produccion.persona.prs.telefono");
			PRS_FECHA_NACIMIENTO = rb.getString("academico.produccion.persona.prs.fecha.nacimiento");
			PRS_ETN_ID = rb.getString("academico.produccion.persona.etn.id");
			PRS_UBC_NACIMIENTO = rb.getString("academico.produccion.persona.ubc.nacimiento");
			PRS_UBC_RESIDENCIA = rb.getString("academico.produccion.persona.ubc.residencia");
			PRS_SECTOR_DOMICILIO = rb.getString("academico.produccion.persona.prs.sector.domicilio");
			PRS_CALLE_PRINCIPAL = rb.getString("academico.produccion.persona.prs.calle.principal");
			PRS_CALLE_SECUNDARIA = rb.getString("academico.produccion.persona.prs.calle.secundaria");
			PRS_NUMERO_CASA = rb.getString("academico.produccion.persona.prs.numero.casa");
			PRS_REFERENCIA_DOMICILIO = rb.getString("academico.produccion.persona.prs.referencia.domicilio");
			PRS_CELULAR = rb.getString("academico.produccion.persona.prs.celular");
			PRS_ID = rb.getString("academico.produccion.persona.prs.id");
			PRS_TIPO_IDENTIFICACION = rb.getString("academico.produccion.persona.prs.tipo.identificacion");
			PRS_TIPO_IDENTIFICACION_SNIESE = rb.getString("academico.produccion.persona.prs.tipo.identificacion.sniese");
			PRS_IDENTIFICACION = rb.getString("academico.produccion.persona.prs.identificacion");
			PRS_PRIMER_APELLIDO = rb.getString("academico.produccion.persona.prs.primer.apellido");
			PRS_SEGUNDO_APELLIDO = rb.getString("academico.produccion.persona.prs.segundo.apellido");
			PRS_NOMBRES = rb.getString("academico.produccion.persona.prs.nombres");
			PRS_SEXO = rb.getString("academico.produccion.persona.prs.sexo");
			PRS_SEXO_SNIESE = rb.getString("academico.produccion.persona.prs.sexo.sniese");
			PRS_MAIL_PERSONAL = rb.getString("academico.produccion.persona.prs.mail.personal");
			PRS_DISCAPACIDAD = rb.getString("academico.produccion.persona.prs.discapacidad");
			PRS_TIPO_DISCAPACIDAD = rb.getString("academico.produccion.persona.prs.tipo.discapacidad");
			PRS_PORCE_DISCAPACIDAD = rb.getString("academico.produccion.persona.prs.porce.discapacidad");
			PRS_CARNET_CONADIS = rb.getString("academico.produccion.persona.prs.carnet.conadis");
			PRS_NUM_CARNET_CONADIS = rb.getString("academico.produccion.persona.prs.num.carnet.conadis");
			PRS_ESTADO_CIVIL = rb.getString("academico.produccion.persona.prs.estado.civil");
			PRS_FECHA_ACTUALIZACION_DATOS = rb.getString("academico.produccion.persona.prs.fecha.actualizacion.datos");
			PRS_FECHA_VINCULACION_SEGURO = rb.getString("academico.produccion.persona.prs.fecha.vinculacion.seguro");
			PRS_FECHA_REGISTRO_SEGURO = rb.getString("academico.produccion.persona.prs.fecha.registro.seguro");
			PRS_ESTADO_SEGURO = rb.getString("academico.produccion.persona.prs.estado.seguro");
			PRS_FORMULARIO_SEGURO = rb.getString("academico.produccion.persona.prs.formulario.seguro");
			PRS_FECHA_FORMULARIO_SEGURO = rb.getString("academico.produccion.persona.prs.fecha.formulario.seguro");
			
			TABLA_PLANIFICACION_CRONOGRAMA = rb.getString("academico.produccion.planificacion.cronograma");
			PLCR_ID = rb.getString("academico.produccion.planificacion.cronograma.plcr.id");
			PLCR_CRPRFL_ID = rb.getString("academico.produccion.planificacion.cronograma.crprfl.id");
			PLCR_ESTADO = rb.getString("academico.produccion.planificacion.cronograma.plcr.estado");
			PLCR_FECHA_INICIO = rb.getString("academico.produccion.planificacion.cronograma.plcr.fecha.inicio");
			PLCR_FECHA_FIN = rb.getString("academico.produccion.planificacion.cronograma.plcr.fecha.fin");
			TABLA_PORCENTAJE_GRATUIDAD = rb.getString("academico.produccion.porcentaje.gratuidad");
			PRGR_ID = rb.getString("academico.produccion.porcentaje.gratuidad.prgr.id");
			PRGR_TIGR_ID = rb.getString("academico.produccion.porcentaje.gratuidad.tigr.id");
			PRGR_DESCRIPCION = rb.getString("academico.produccion.porcentaje.gratuidad.prgr.descripcion");
			PRGR_VALOR = rb.getString("academico.produccion.porcentaje.gratuidad.prgr.valor");
			PRGR_ESTADO = rb.getString("academico.produccion.porcentaje.gratuidad.prgr.estado");
			TABLA_PREREQUISITO = rb.getString("academico.produccion.prerequisito");
			PRR_ID = rb.getString("academico.produccion.prerequisito.prr.id");
			PRR_MTR_ID = rb.getString("academico.produccion.prerequisito.mtr.id");
			PRR_MTR_PREREQUISITO_ID = rb.getString("academico.produccion.prerequisito.mtr.prerequisito.id");
			PRR_CODIGO = rb.getString("academico.produccion.prerequisito.prr.codigo");
			PRR_DESCRIPCION = rb.getString("academico.produccion.prerequisito.prr.descripcion");
			PRR_ESTADO = rb.getString("academico.produccion.prerequisito.prr.estado");
			TABLA_PROCESO = rb.getString("academico.produccion.proceso");
			PRC_ID = rb.getString("academico.produccion.proceso.prc.id");
			PRC_DESCRIPCION = rb.getString("academico.produccion.proceso.prc.descripcion");
			TABLA_PROCESO_CALIFICACION = rb.getString("academico.produccion.proceso.calificacion");
			PRCL_ID = rb.getString("academico.produccion.proceso.calificacion.prcl.id");
			PRCL_CLF_ID = rb.getString("academico.produccion.proceso.calificacion.clf.id");
			PRCL_FECHA = rb.getString("academico.produccion.proceso.calificacion.prcl.fecha");
			PRCL_TIPO_PROCESO = rb.getString("academico.produccion.proceso.calificacion.prcl.tipo.proceso");
			PRCL_OBSERVACION = rb.getString("academico.produccion.proceso.calificacion.prcl.observacion");
			PRCL_NOTA1 = rb.getString("academico.produccion.proceso.calificacion.prcl.nota1");
			PRCL_NOTA2 = rb.getString("academico.produccion.proceso.calificacion.prcl.nota2");
			PRCL_EXAMEN = rb.getString("academico.produccion.proceso.calificacion.prcl.examen");
			PRCL_SUPLETORIO = rb.getString("academico.produccion.proceso.calificacion.prcl.supletorio");
			PRCL_ASISTENCIA1 = rb.getString("academico.produccion.proceso.calificacion.prcl.asistencia1");
			PRCL_ASISTENCIA2 = rb.getString("academico.produccion.proceso.calificacion.prcl.asistencia2");
			PRCL_TOTAL_ASISTENCIA1 = rb.getString("academico.produccion.proceso.calificacion.prcl.total.asistencia1");
			PRCL_TOTAL_ASISTENCIA2 = rb.getString("academico.produccion.proceso.calificacion.prcl.total.asistencia2");
			PRCL_PROMEDIO_NOTAS = rb.getString("academico.produccion.proceso.calificacion.prcl.promedio.notas");
			PRCL_PROMEDIO_ASISTENCIA = rb.getString("academico.produccion.proceso.calificacion.prcl.promedio.asistencia");
			PRCL_ASISTENCIA_TOTAL = rb.getString("academico.produccion.proceso.calificacion.prcl.asistencia.total");
			PRCL_PARAM_RECUPERACION1 = rb.getString("academico.produccion.proceso.calificacion.prcl.param.recuperacion1");
			PRCL_PARAM_RECUPERACION2 = rb.getString("academico.produccion.proceso.calificacion.prcl.param.recuperacion2");
			PRCL_NOTA_FINAL_SEMESTRE = rb.getString("academico.produccion.proceso.calificacion.prcl.nota.final.semestre");
			PRCL_ASISTENCIA_DOCENTE1 = rb.getString("academico.produccion.proceso.calificacion.prcl.asistencia.docente1");
			PRCL_ASISTENCIA_DOCENTE2 = rb.getString("academico.produccion.proceso.calificacion.prcl.asistencia.docente2");
			PRCL_FECHA_NOTA2 = rb.getString("academico.produccion.proceso.calificacion.prcl.fecha.nota2");
			PRCL_FECHA_RECUPERACION = rb.getString("academico.produccion.proceso.calificacion.prcl.fecha.recuperacion");
			PRCL_OBSERVACION2 = rb.getString("academico.produccion.proceso.calificacion.prcl.observacion2");
			PRCL_OBSERVACION3 = rb.getString("academico.produccion.proceso.calificacion.prcl.observacion3");
			PRCL_SUMA_P1_P2 = rb.getString("academico.produccion.proceso.calificacion.prcl.suma.p1.p2");
			PRCL_ASISTENCIA_TOTAL_DOC = rb.getString("academico.produccion.proceso.calificacion.prcl.asistencia.total.doc");
			TABLA_PROCESO_ESTUDIANTE = rb.getString("academico.produccion.proceso.estudiante");
			PRES_ID = rb.getString("academico.produccion.proceso.estudiante.pres.id");
			PRES_FCES_ID = rb.getString("academico.produccion.proceso.estudiante.fces.id");
			PRES_DESCRIPCION = rb.getString("academico.produccion.proceso.estudiante.pres.descripcion");
			PRES_TIPO = rb.getString("academico.produccion.proceso.estudiante.pres.tipo");
			PRES_ESTADO = rb.getString("academico.produccion.proceso.estudiante.pres.estado");
			PRES_FECHA_EJECUCION = rb.getString("academico.produccion.proceso.estudiante.pres.fecha.ejecucion");
			TABLA_PROCESO_FLUJO = rb.getString("academico.produccion.proceso.flujo");
			PRFL_ID = rb.getString("academico.produccion.proceso.flujo.prfl.id");
			PRFL_DESCRIPCION = rb.getString("academico.produccion.proceso.flujo.prfl.descripcion");
			PRFL_ESTADO = rb.getString("academico.produccion.proceso.flujo.prfl.estado");
			TABLA_PROCESO_TRAMITE_CONTRATO = rb.getString("academico.produccion.proceso.tramite.contrato");
			PRTRCN_ID = rb.getString("academico.produccion.proceso.tramite.contrato.prtrcn.id");
			PRTRCN_DTTRCN_ID = rb.getString("academico.produccion.proceso.tramite.contrato.dttrcn.id");
			PRTRCN_TIPO_PROCESO = rb.getString("academico.produccion.proceso.tramite.contrato.prtrcn.tipo.proceso");
			PRTRCN_FECHA_EJECUCION = rb.getString("academico.produccion.proceso.tramite.contrato.prtrcn.fecha.ejecucion");
			PRTRCN_RESULTADO_PROCESO = rb.getString("academico.produccion.proceso.tramite.contrato.prtrcn.resultado.proceso");
			TABLA_PROYECCION = rb.getString("academico.produccion.proyeccion");
			PRY_ID = rb.getString("academico.produccion.proyeccion.pry.id");
			PRY_EXC_ID = rb.getString("academico.produccion.proyeccion.exc.id");
			PRY_MLCRMT_ID = rb.getString("academico.produccion.proyeccion.mlcrmt.id");
			PRY_PRAC_ID = rb.getString("academico.produccion.proyeccion.prac.id");
			PRY_NUM_NUEVO = rb.getString("academico.produccion.proyeccion.pry.num.nuevo");
			PRY_NUM_REPETIDO = rb.getString("academico.produccion.proyeccion.pry.num.repetido");
			TABLA_PUESTO = rb.getString("academico.produccion.puesto");
			PST_ID = rb.getString("academico.produccion.puesto.pst.id");
			PST_DENOMINACION = rb.getString("academico.produccion.puesto.pst.denominacion");
			PST_ESTADO = rb.getString("academico.produccion.puesto.pst.estado");
			PST_AMBITO = rb.getString("academico.produccion.puesto.pst.ambito");
			PST_CODIGO = rb.getString("academico.produccion.puesto.pst.codigo");
			PST_NIVEL = rb.getString("academico.produccion.puesto.pst.nivel");
			PST_MISION = rb.getString("academico.produccion.puesto.pst.mision");
			PST_RESPONSABILIDAD = rb.getString("academico.produccion.puesto.pst.responsabilidad");
			PST_DESCRIPCION_EXPERIENCIA = rb.getString("academico.produccion.puesto.pst.descripcion.experiencia");
			PST_INTERFAZ = rb.getString("academico.produccion.puesto.pst.interfaz");
			PST_FECHA_CREACION = rb.getString("academico.produccion.puesto.pst.fecha.creacion");
			PST_NIVEL_RANGO_GRADUAL = rb.getString("academico.produccion.puesto.pst.nivel.rango.gradual");
			PST_SER_ID = rb.getString("academico.produccion.puesto.ser.id");
			PST_GROC_ID = rb.getString("academico.produccion.puesto.groc.id");
			PST_TMDD_ID = rb.getString("academico.produccion.puesto.tmdd.id");
			PST_TIPS_ID = rb.getString("academico.produccion.puesto.tips.id");
			PST_CATEGORIA_DOCENTE = rb.getString("academico.produccion.puesto.pst.categoria.docente");
			TABLA_PUESTO_AREA_CONOCIMIENTO = rb.getString("academico.produccion.puesto.area.conocimiento");
			PSARCN_ID = rb.getString("academico.produccion.puesto.area.conocimiento.psarcn.id");
			PSARCN_ARCN_ID = rb.getString("academico.produccion.puesto.area.conocimiento.arcn.id");
			PSARCN_PST_ID = rb.getString("academico.produccion.puesto.area.conocimiento.pst.id");
			TABLA_RECORD_ESTUDIANTE = rb.getString("academico.produccion.record.estudiante");
			RCES_ID = rb.getString("academico.produccion.record.estudiante.rces.id");
			RCES_FCES_ID = rb.getString("academico.produccion.record.estudiante.fces.id");
			RCES_MLCRPR_ID = rb.getString("academico.produccion.record.estudiante.mlcrpr.id");
			RCES_ESTADO = rb.getString("academico.produccion.record.estudiante.rces.estado");
			RCES_OBSERVACION = rb.getString("academico.produccion.record.estudiante.rces.observacion");
			RCES_INGRESO_NOTA = rb.getString("academico.produccion.record.estudiante.rces.ingreso.nota");
			RCES_INGRESO_NOTA2 = rb.getString("academico.produccion.record.estudiante.rces.ingreso.nota2");
			RCES_INGRESO_NOTA3 = rb.getString("academico.produccion.record.estudiante.rces.ingreso.nota3");
			RCES_USUARIO = rb.getString("academico.produccion.record.estudiante.rces.usuario");
			RCES_MODIFICACION = rb.getString("academico.produccion.record.estudiante.rces.modificacion");
			RCES_MODO_APROBACION =rb.getString("academico.produccion.record.estudiante.rces.modo.aprobacion");
			
			TABLA_REGIMEN = rb.getString("academico.produccion.regimen");
			RGM_ID = rb.getString("academico.produccion.regimen.rgm.id");
			RGM_DESCRIPCION = rb.getString("academico.produccion.regimen.rgm.descripcion");
			RGM_CODIGO = rb.getString("academico.produccion.regimen.rgm.codigo");
			TABLA_REGIMEN_ACADEMICO = rb.getString("academico.produccion.regimen.academico");
			RGAC_ID = rb.getString("academico.produccion.regimen.academico.rgac.id");
			RGAC_DESCRIPCION = rb.getString("academico.produccion.regimen.academico.rgac.descripcion");
			RGAC_ESTADO = rb.getString("academico.produccion.regimen.academico.rgac.estado");
			TABLA_RELACION_LABORAL = rb.getString("academico.produccion.relacion.laboral");
			RLLB_ID = rb.getString("academico.produccion.relacion.laboral.rllb.id");
			RLLB_DESCRIPCION = rb.getString("academico.produccion.relacion.laboral.rllb.descripcion");
			RLLB_ESTADO = rb.getString("academico.produccion.relacion.laboral.rllb.estado");
			TABLA_REQUISITO = rb.getString("academico.produccion.requisito");
			RQS_ID = rb.getString("academico.produccion.requisito.rqs.id");
			RQS_PRTRCN_ID = rb.getString("academico.produccion.requisito.prtrcn.id");
			RQS_PUBLICACIONES = rb.getString("academico.produccion.requisito.rqs.publicaciones");
			RQS_EXPERICIA_DOCENTE = rb.getString("academico.produccion.requisito.rqs.expericia.docente");
			RQS_EXPERICIA_LABORAL = rb.getString("academico.produccion.requisito.rqs.expericia.laboral");
			RQS_CAPACITACION = rb.getString("academico.produccion.requisito.rqs.capacitacion");
			RQS_MECANIZADO_IESS = rb.getString("academico.produccion.requisito.rqs.mecanizado.iess");
			RQS_DECLARACION_BIENES = rb.getString("academico.produccion.requisito.rqs.declaracion.bienes");
			RQS_PLURIEMPLEO = rb.getString("academico.produccion.requisito.rqs.pluriempleo");
			RQS_NEPOTISMO = rb.getString("academico.produccion.requisito.rqs.nepotismo");
			RQS_CERTIFICACION_BANCARIA = rb.getString("academico.produccion.requisito.rqs.certificacion.bancaria");
			RQS_CERTIFICADO_AFILIADO_IESS = rb.getString("academico.produccion.requisito.rqs.certificado.afiliado.iess");
			RQS_ESTADO = rb.getString("academico.produccion.requisito.rqs.estado");
			TABLA_ROL = rb.getString("academico.produccion.rol");
			ROL_ID = rb.getString("academico.produccion.rol.rol.id");
			ROL_DESCRIPCION = rb.getString("academico.produccion.rol.rol.descripcion");
			ROL_TIPO = rb.getString("academico.produccion.rol.rol.tipo");
			ROL_DETALLE = rb.getString("academico.produccion.rol.rol.detalle");
			TABLA_ROL_FLUJO_CARRERA = rb.getString("academico.produccion.rol.flujo.carrera");
			ROFLCR_ID = rb.getString("academico.produccion.rol.flujo.carrera.roflcr.id");
			ROFLCR_USRO_ID = rb.getString("academico.produccion.rol.flujo.carrera.usro.id");
			ROFLCR_CRR_ID = rb.getString("academico.produccion.rol.flujo.carrera.crr.id");
			ROFLCR_ESTADO = rb.getString("academico.produccion.rol.flujo.carrera.roflcr.estado");
			TABLA_SERIE = rb.getString("academico.produccion.serie");
			SER_ID = rb.getString("academico.produccion.serie.ser.id");
			SER_PRC_ID = rb.getString("academico.produccion.serie.prc.id");
			SER_CODIGO = rb.getString("academico.produccion.serie.ser.codigo");
			SER_DESCRIPCION = rb.getString("academico.produccion.serie.ser.descripcion");
			TABLA_SISTEMA_CALIFICACION = rb.getString("academico.produccion.sistema.calificacion");
			SSCL_ID = rb.getString("academico.produccion.sistema.calificacion.sscl.id");
			SSCL_PRAC_ID = rb.getString("academico.produccion.sistema.calificacion.prac.id");
			SSCL_TISSCL_ID = rb.getString("academico.produccion.sistema.calificacion.tisscl.id");
			SSCL_NOTA_MAXIMA = rb.getString("academico.produccion.sistema.calificacion.sscl.nota.maxima");
			SSCL_NOTA_MINIMA_APROBACION = rb.getString("academico.produccion.sistema.calificacion.sscl.nota.minima.aprobacion");
			SSCL_NOTA_MINIMA_SUPLETORIO = rb.getString("academico.produccion.sistema.calificacion.sscl.nota.minima.supletorio");
			SSCL_PORCENTAJE_APROBACION = rb.getString("academico.produccion.sistema.calificacion.sscl.porcentaje.aprobacion");
			SSCL_REDONDEO = rb.getString("academico.produccion.sistema.calificacion.sscl.redondeo");
			SSCL_ESTADO = rb.getString("academico.produccion.sistema.calificacion.sscl.estado");
			TABLA_SOLICITUD_TERCERA_MATRICULA = rb.getString("academico.produccion.solicitud.tercera.matricula");
			SLTRMT_ID = rb.getString("academico.produccion.sltrmt.id");
			SLTRMT_CSL_ID = rb.getString("academico.produccion.csl.id");
			SLTRMT_RCES_ID = rb.getString("academico.produccion.rces.id");
			SLTRMT_PRAC_ID = rb.getString("academico.produccion.prac.id");
			SLTRMT_ESTADO = rb.getString("academico.produccion.sltrmt.estado");
			SLTRMT_TIPO = rb.getString("academico.produccion.sltrmt.tipo");
			SLTRMT_FECHA_SOLICITUD = rb.getString("academico.produccion.sltrmt.fecha.solicitud");
			SLTRMT_DOCUMENTO_SOLICITUD = rb.getString("academico.produccion.sltrmt.documento.solicitud");
			SLTRMT_FECHA_VERIF_SOLICITUD=rb.getString("academico.produccion.sltrmt.fecha.verif.solicitud");
			SLTRMT_FECHA_RESP_SOLICITUD= rb.getString("academico.produccion.sltrmt.fecha.resp.solicitud");
			SLTRMT_DOCUMENTO_RESOLUCION = rb.getString("academico.produccion.sltrmt.documento.resolucion");
			SLTRMT_OBSERVACION = rb.getString("academico.produccion.sltrmt.observacion");
			SLTRMT_OBSERVACION_FINAL = rb.getString("academico.produccion.sltrmt.observacion.final");
			SLTRMT_SUB_ID = rb.getString("academico.produccion.sltrmt.sub.id");
			SLTRMT_FCES_ID = rb.getString("academico.produccion.sltrmt.fces.id");
			SLTRMT_MTR_ID = rb.getString("academico.produccion.sltrmt.mtr.id");
			SLTRMT_ESTADO_REGISTRO = rb.getString("academico.produccion.sltrmt.estado.registro");
			TABLA_TIEMPO_DEDICACION = rb.getString("academico.produccion.tiempo.dedicacion");
			TMDD_ID = rb.getString("academico.produccion.tiempo.dedicacion.tmdd.id");
			TMDD_DESCRIPCION = rb.getString("academico.produccion.tiempo.dedicacion.tmdd.descripcion");
			TMDD_HORAS = rb.getString("academico.produccion.tiempo.dedicacion.tmdd.horas");
			TMDD_ESTADO = rb.getString("academico.produccion.tiempo.dedicacion.tmdd.estado");
			TABLA_TIPO_APERTURA = rb.getString("academico.produccion.tipo.apertura");
			TIAP_ID = rb.getString("academico.produccion.tipo.apertura.tiap.id");
			TIAP_DESCRIPCION = rb.getString("academico.produccion.tipo.apertura.tiap.descripcion");
			TIAP_ESTADO = rb.getString("academico.produccion.tipo.apertura.tiap.estado");
			TABLA_TIPO_CARGA_HORARIA = rb.getString("academico.produccion.tipo.carga.horaria");
			TICRHR_ID = rb.getString("academico.produccion.tipo.carga.horaria.ticrhr.id");
			TICRHR_DESCRIPCION = rb.getString("academico.produccion.tipo.carga.horaria.ticrhr.descripcion");
			TICRHR_ESTADO = rb.getString("academico.produccion.tipo.carga.horaria.ticrhr.estado");
			TABLA_TIPO_CAUSAL = rb.getString("academico.produccion.tipo.causal");
			TICS_ID = rb.getString("academico.produccion.tipo.causal.tics.id");
			TICS_DESCRIPCION = rb.getString("academico.produccion.tipo.causal.tics.descripcion");
			TICS_ESTADO = rb.getString("academico.produccion.tipo.causal.tics.estado");
			TABLA_TIPO_CONTRATO = rb.getString("academico.produccion.tipo.contrato");
			TICN_ID = rb.getString("academico.produccion.tipo.contrato.ticn.id");
			TICN_TIEM_ID = rb.getString("academico.produccion.tipo.contrato.tiem.id");
			TICN_DESCRIPCION = rb.getString("academico.produccion.tipo.contrato.ticn.descripcion");
			TICN_ESTADO = rb.getString("academico.produccion.tipo.contrato.ticn.estado");
			TABLA_TIPO_DOCUMENTO = rb.getString("academico.produccion.tipo.documento");
			TIDC_ID = rb.getString("academico.produccion.tipo.documento.tidc.id");
			TIDC_SUB_ID = rb.getString("academico.produccion.tipo.documento.tidc.sub.id");
			TIDC_DESCRIPCION = rb.getString("academico.produccion.tipo.documento.tidc.descripcion");
			TIDC_NUMERO_DOCUMENTO = rb.getString("academico.produccion.tipo.documento.tidc.numero.documento");
			TIDC_FECHA_INICIO = rb.getString("academico.produccion.tipo.documento.tidc.fecha.inicio");
			TIDC_FECHA_FIN = rb.getString("academico.produccion.tipo.documento.tidc.fecha.fin");
			TIDC_INGRESO_CONCURSO = rb.getString("academico.produccion.tipo.documento.tidc.ingreso.concurso");
			TABLA_TIPO_EMPLEADO = rb.getString("academico.produccion.tipo.empleado");
			TIEM_ID = rb.getString("academico.produccion.tipo.empleado.tiem.id");
			TIEM_DESCRIPCION = rb.getString("academico.produccion.tipo.empleado.tiem.descripcion");
			TIEM_ESTADO = rb.getString("academico.produccion.tipo.empleado.tiem.estado");
			TABLA_TIPO_FINANCIAMIENTO = rb.getString("academico.produccion.tipo.financiamiento");
			TIFN_ID = rb.getString("academico.produccion.tipo.financiamiento.tifn.id");
			TIFN_DESCRIPCION = rb.getString("academico.produccion.tipo.financiamiento.tifn.descripcion");
			TABLA_TIPO_FORMACION = rb.getString("academico.produccion.tipo.formacion");
			TIFR_ID = rb.getString("academico.produccion.tipo.formacion.tifr.id");
			TIFR_DESCRIPCION = rb.getString("academico.produccion.tipo.formacion.tifr.descripcion");
			TIFR_NVFR_ID = rb.getString("academico.produccion.tipo.formacion.nvfr.id");
			TABLA_TIPO_FORMACION_MALLA = rb.getString("academico.produccion.tipo.formacion.malla");
			TIFRML_ID = rb.getString("academico.produccion.tipo.formacion.malla.tifrml.id");
			TIFRML_DESCRIPCION = rb.getString("academico.produccion.tipo.formacion.malla.tifrml.descripcion");
			TIFRML_ESTADO = rb.getString("academico.produccion.tipo.formacion.malla.tifrml.estado");
			TABLA_TIPO_FUNCION_CARGA_HORARIA = rb.getString("academico.produccion.tipo.funcion.carga.horaria");
			TIFNCRHR_ID = rb.getString("academico.produccion.tipo.funcion.carga.horaria.tifncrhr.id");
			TIFNCRHR_TICRHR_ID = rb.getString("academico.produccion.tipo.funcion.carga.horaria.ticrhr.id");
			TIFNCRHR_ESTADO = rb.getString("academico.produccion.tipo.funcion.carga.horaria.tifncrhr.estado");
			TIFNCRHR_FNCRHR_ID = rb.getString("academico.produccion.tipo.funcion.carga.horaria.fncrhr.id");
			TABLA_TIPO_GRATUIDAD = rb.getString("academico.produccion.tipo.gratuidad");
			TIGR_ID = rb.getString("academico.produccion.tipo.gratuidad.tigr.id");
			TIGR_DESCRIPCION = rb.getString("academico.produccion.tipo.gratuidad.tigr.descripcion");
			TIGR_ESTADO = rb.getString("academico.produccion.tipo.gratuidad.tigr.estado");
			TABLA_TIPO_MATERIA = rb.getString("academico.produccion.tipo.materia");
			TIMT_ID = rb.getString("academico.produccion.tipo.materia.timt.id");
			TIMT_DESCRIPCION = rb.getString("academico.produccion.tipo.materia.timt.descripcion");
			TIMT_ESTADO = rb.getString("academico.produccion.tipo.materia.timt.estado");
			TABLA_TIPO_NIVEL_APERTURA = rb.getString("academico.produccion.tipo.nivel.apertura");
			TINVAP_ID = rb.getString("academico.produccion.tipo.nivel.apertura.tinvap.id");
			TINVAP_NVAP_ID = rb.getString("academico.produccion.tipo.nivel.apertura.nvap.id");
			TINVAP_TIAP_ID = rb.getString("academico.produccion.tipo.nivel.apertura.tiap.id");
			TINVAP_PLCR_ID = rb.getString("academico.produccion.tipo.nivel.apertura.plcr.id");
			TINVAP_FECHA_INICIO = rb.getString("academico.produccion.tipo.nivel.apertura.tinvap.fecha.inicio");
			TINVAP_FECHA_FIN = rb.getString("academico.produccion.tipo.nivel.apertura.tinvap.fecha.fin");
			TINVAP_ESTADO = rb.getString("academico.produccion.tipo.nivel.apertura.tinvap.estado");
			TINVAP_OBSERVACION = rb.getString("academico.produccion.tipo.nivel.apertura.tinvap.observacion");
			TABLA_TIPO_PUESTO = rb.getString("academico.produccion.tipo.puesto");
			TIPS_ID = rb.getString("academico.produccion.tipo.puesto.tips.id");
			TIPS_DESCRIPCION = rb.getString("academico.produccion.tipo.puesto.tips.descripcion");
			TIPS_ESTADO = rb.getString("academico.produccion.tipo.puesto.tips.estado");
			TABLA_TIPO_PROCESO = rb.getString("academico.produccion.tipo.proceso");
			TIPR_ID = rb.getString("academico.produccion.tipo.proceso.tipr.id");
			TIPR_DESCRIPCION = rb.getString("academico.produccion.tipo.proceso.tipr.descripcion");
			TIPR_ESTADO = rb.getString("academico.produccion.tipo.proceso.tipr.estado");
			TABLA_TIPO_SEDE = rb.getString("academico.produccion.tipo.sede");
			TISE_ID = rb.getString("academico.produccion.tipo.sede.tise.id");
			TISE_RGAC_ID = rb.getString("academico.produccion.tipo.sede.rgac.id");
			TISE_DESCRIPCION = rb.getString("academico.produccion.tipo.sede.tise.descripcion");
			TABLA_TIPO_SISTEMA_CALIFICACION = rb.getString("academico.produccion.tipo.sistema.calificacion");
			TISSCL_ID = rb.getString("academico.produccion.tipo.sistema.calificacion.tisscl.id");
			TISSCL_DESCRIPCION = rb.getString("academico.produccion.tipo.sistema.calificacion.tisscl.descripcion");
			TISSCL_ESTADO = rb.getString("academico.produccion.tipo.sistema.calificacion.tisscl.estado");
			TISSCL_OBSERVACION = rb.getString("academico.produccion.tipo.sistema.calificacion.tisscl.observacion");
			TABLA_TITULO = rb.getString("academico.produccion.titulo");
			TTL_ID = rb.getString("academico.produccion.titulo.ttl.id");
			TTL_DESCRIPCION = rb.getString("academico.produccion.titulo.ttl.descripcion");
			TTL_SEXO = rb.getString("academico.produccion.titulo.ttl.sexo");
			TTL_ESTADO = rb.getString("academico.produccion.titulo.ttl.estado");
			TTL_TIPO = rb.getString("academico.produccion.titulo.ttl.tipo");
			TABLA_TRAMITE = rb.getString("academico.produccion.tramite");
			TRM_ID = rb.getString("academico.produccion.tramite.trm.id");
			TRM_DESCRIPCION = rb.getString("academico.produccion.tramite.trm.descripcion");
			TRM_ESTADO = rb.getString("academico.produccion.tramite.trm.estado");
			TABLA_TURNERO = rb.getString("academico.produccion.turnero");
			TRN_ID = rb.getString("academico.produccion.turnero.trn.id");
			TRN_PRTRCN_ID = rb.getString("academico.produccion.turnero.prtrcn.id");
			TRN_USRO_ID = rb.getString("academico.produccion.turnero.usro.id");
			TRN_ESTADO = rb.getString("academico.produccion.turnero.trn.estado");
			TABLA_UBICACION = rb.getString("academico.produccion.ubicacion");
			UBC_ID = rb.getString("academico.produccion.ubicacion.ubc.id");
			UBC_DESCRIPCION = rb.getString("academico.produccion.ubicacion.ubc.descripcion");
			UBC_JERARQUIA = rb.getString("academico.produccion.ubicacion.ubc.jerarquia");
			UBC_GENTILICIO = rb.getString("academico.produccion.ubicacion.ubc.gentilicio");
			UBC_COD_SNIESE = rb.getString("academico.produccion.ubicacion.ubc.cod.sniese");
			UBC_SUB_ID = rb.getString("academico.produccion.ubicacion.ubc.sub.id");
			TABLA_UNIDAD_FORMACION = rb.getString("academico.produccion.unidad.formacion");
			UNFR_ID = rb.getString("academico.produccion.unidad.formacion.unfr.id");
			UNFR_DESCRIPCION = rb.getString("academico.produccion.unidad.formacion.unfr.descripcion");
			UNFR_ESTADO = rb.getString("academico.produccion.unidad.formacion.unfr.estado");
			TABLA_USUARIO = rb.getString("academico.produccion.usuario");
			USR_ID = rb.getString("academico.produccion.usuario.usr.id");
			USR_IDENTIFICACION = rb.getString("academico.produccion.usuario.usr.identificacion");
			USR_NICK = rb.getString("academico.produccion.usuario.usr.nick");
			USR_PASSWORD = rb.getString("academico.produccion.usuario.usr.password");
			USR_FECHA_CREACION = rb.getString("academico.produccion.usuario.usr.fecha.creacion");
			USR_FECHA_CADUCIDAD = rb.getString("academico.produccion.usuario.usr.fecha.caducidad");
			USR_FECHA_CAD_PASS = rb.getString("academico.produccion.usuario.usr.fecha.cad.pass");
			USR_ESTADO = rb.getString("academico.produccion.usuario.usr.estado");
			USR_EST_SESION = rb.getString("academico.produccion.usuario.usr.est.sesion");
			USR_ACTIVE_DIRECTORY = rb.getString("academico.produccion.usuario.usr.active.directory");
			USR_PRS_ID = rb.getString("academico.produccion.usuario.prs.id");
			TABLA_USUARIO_ROL = rb.getString("academico.produccion.usuario.rol");
			USRO_ID = rb.getString("academico.produccion.usuario.rol.usro.id");
			USRO_ESTADO = rb.getString("academico.produccion.usuario.rol.usro.estado");
			USRO_USR_ID = rb.getString("academico.produccion.usuario.rol.usr.id");
			USRO_ROL_ID = rb.getString("academico.produccion.usuario.rol.rol.id");
			TABLA_VIGENCIA = rb.getString("academico.produccion.vigencia");
			VGN_ID = rb.getString("academico.produccion.vigencia.vgn.id");
			VGN_DESCRIPCION = rb.getString("academico.produccion.vigencia.vgn.descripcion");
			
			TABLA_ASIGNACION_EVALUADOR = rb.getString("academico.produccion.asignacion.evaluador");
			ASEV_ID = rb.getString("academico.produccion.asignacion.evaluador.asev.id");
			ASEV_CRHR_ID = rb.getString("academico.produccion.asignacion.evaluador.crhr.id");
			ASEV_EVEV_ID = rb.getString("academico.produccion.asignacion.evaluador.evev.id");
			ASEV_ESTADO = rb.getString("academico.produccion.asignacion.evaluador.asev.estado");
			ASEV_USUARIO = rb.getString("academico.produccion.asignacion.evaluador.asev.usuario");
			ASEV_FECHA = rb.getString("academico.produccion.asignacion.evaluador.asev.fecha");
			ASEV_EVALUADOR_CRR_ID = rb.getString("academico.produccion.asignacion.evaluador.asev.evaluador.crr.id");

			TABLA_CONTENIDO = rb.getString("academico.produccion.contenido");
			CNTN_ID = rb.getString("academico.produccion.contenido.cnt.id");
			CNTN_TPCNFNTPEV_ID = rb.getString("academico.produccion.contenido.tpcnfntpev.id");
			CNTN_ASEV_ID = rb.getString("academico.produccion.contenido.asev.id");
			CNTN_EVA_ID = rb.getString("academico.produccion.contenido.eva.id");
			CNTN_DESCRIPCION = rb.getString("academico.produccion.contenido.cnt.descripcion");
			CNTN_SELECCION = rb.getString("academico.produccion.contenido.cnt.seleccion");
			CNTN_FECHA = rb.getString("academico.produccion.contenido.cnt.fecha");
			CNTN_USUARIO = rb.getString("academico.produccion.contenido.cnt.usuario");
			CNTN_SELECCION_INICIAL=rb.getString("academico.produccion.contenido.cnt.seleccion.inicial");
			CNTN_REGISTRO_APELACION=rb.getString("academico.produccion.contenido.cnt.registro.apelacion");
			CNTN_USUARIO_APELACION=rb.getString("academico.produccion.contenido.cnt.usuario.apelacion");
			CNTN_OFICIO_APELACION=rb.getString("academico.produccion.contenido.cnt.oficio.apelacion");
			CNTN_ESTADO_APELACION=rb.getString("academico.produccion.contenido.cnt.estado.apelacion");
			
			TABLA_EVALUACION = rb.getString("academico.produccion.evaluacion");
			EVL_EVA_ID = rb.getString("academico.produccion.evaluacion.eva.id");
			EVL_PRAC_ID = rb.getString("academico.produccion.evaluacion.prac.id");
			EVL_TPEV_ID = rb.getString("academico.produccion.evaluacion.tpev.id");
			EVL_EVA_DESCRIPCION = rb.getString("academico.produccion.evaluacion.eva.descripcion");
			EVL_EVA_USUARIO = rb.getString("academico.produccion.evaluacion.eva.usuario");
			EVL_EVA_FECHA = rb.getString("academico.produccion.evaluacion.eva.fecha");
			EVL_EVA_ESTADO = rb.getString("academico.produccion.evaluacion.eva.estado");
			EVL_EVA_CRONOGRAMA_INICIO = rb.getString("academico.produccion.evaluacion.eva.cronograma.inicio");
			EVL_EVA_CRONOGRAMA_FIN = rb.getString("academico.produccion.evaluacion.eva.cronograma.fin");
			EVL_EVA_PRFL_ID = rb.getString("academico.produccion.evaluacion.eva.prfl.id");
			
			TABLA_EVALUADOR_EVALUADO = rb.getString("academico.produccion.evaluador.evaluado");
			EVEV_ID = rb.getString("academico.produccion.evaluador.evaluado.evev.id");
			EVEV_USRO_ID = rb.getString("academico.produccion.evaluador.evaluado.usro.id");
			EVEV_EVALUADOR = rb.getString("academico.produccion.evaluador.evaluado.evev.evaluador");

			TABLA_FUNCION_EVALUACION = rb.getString("academico.produccion.funcion.evaluacion");
			FNEV_ID = rb.getString("academico.produccion.funcion.evaluacion.fnev.id");
			FNEV_DESCRIPCION = rb.getString("academico.produccion.funcion.evaluacion.fnev.descripcion");
			FNEV_ESTADO = rb.getString("academico.produccion.funcion.evaluacion.fnev.estado");
			FNEV_FECHA = rb.getString("academico.produccion.funcion.evaluacion.fnev.fecha");
			FNEV_USUARIO = rb.getString("academico.produccion.funcion.evaluacion.fnev.usuario");
			
			TABLA_FUNCION_TIPO_EVALUACION = rb.getString("academico.produccion.funcion.tipo.evaluacion");
			FNTIEV_FNCTPEV_ID = rb.getString("academico.produccion.funcion.tipo.evaluacion.fnctpev.id");
			FNTIEV_TPEV_ID = rb.getString("academico.produccion.funcion.tipo.evaluacion.tpev.id");
			FNTIEV_FNEV_ID = rb.getString("academico.produccion.funcion.tipo.evaluacion.fnev.id");
			FNTIEV_FNCTPEV_USUARIO = rb.getString("academico.produccion.funcion.tipo.evaluacion.fnctpev.usuario");
			FNTIEV_FNCTPEV_FECHA = rb.getString("academico.produccion.funcion.tipo.evaluacion.fnctpev.fecha");
			FNTIEV_FNCTPEV_ESTADO = rb.getString("academico.produccion.funcion.tipo.evaluacion.fnctpev.estado");
			
			TABLA_TIPO_CONTENIDO = rb.getString("academico.produccion.tipo.contenido");
			TICN_TPCN_ID = rb.getString("academico.produccion.tipo.contenido.tpcn.id");
			TICN_TPCN_DESCRIPCION = rb.getString("academico.produccion.tipo.contenido.tpcn.descripcion");
			TICN_TPCN_ESTADO = rb.getString("academico.produccion.tipo.contenido.tpcn.estado");
			TICN_TPCN_TIPO = rb.getString("academico.produccion.tipo.contenido.tpcn.tipo");
			TICN_TPCN_NUMERAL = rb.getString("academico.produccion.tipo.contenido.tpcn.numeral");
			TICN_TPCN_TIPO_COMPONENTE = rb.getString("academico.produccion.tipo.contenido.tpcn.tipo.componente");
			TICN_TPCN_TIPO_SELECCION = rb.getString("academico.produccion.tipo.contenido.tpcn.tipo.seleccion");
			TICN_TPCN_NUM_MAX = rb.getString("academico.produccion.tipo.contenido.tpcn.num.max");
			TICN_TPCN_FECHA = rb.getString("academico.produccion.tipo.contenido.tpcn.fecha");
			TICN_TPCN_USUARIO = rb.getString("academico.produccion.tipo.contenido.tpcn.usuario");
			TICN_TPCN_OBLIGATORIEDAD = rb.getString("academico.produccion.tipo.contenido.tpcn.obligatoriedad");
			TICN_TPCN_FK_TPCN_ID = rb.getString("academico.produccion.tipo.contenido.tpcn.fk.tpcn.id");

			TABLA_TIPO_EVALUACION = rb.getString("academico.produccion.tipo.evaluacion");
			TIEV_TPEV_ID = rb.getString("academico.produccion.tipo.evaluacion.tpev.id");
			TIEV_TPEV_DESCRIPCION = rb.getString("academico.produccion.tipo.evaluacion.tpev.descripcion");
			TIEV_TPEV_INTRODUCCION = rb.getString("academico.produccion.tipo.evaluacion.tpev.introduccion");
			TIEV_TPEV_INSTRUCCIONES = rb.getString("academico.produccion.tipo.evaluacion.tpev.instrucciones");
			TIEV_TPEV_FECHA = rb.getString("academico.produccion.tipo.evaluacion.tpev.fecha");
			TIEV_TPEV_USUARIO = rb.getString("academico.produccion.tipo.evaluacion.tpev.usuario");
			TIEV_TPEV_ESTADO = rb.getString("academico.produccion.tipo.evaluacion.tpev.estado");
			
			TABLA_TPCN_FUNCION_TPEV = rb.getString("academico.produccion.tpcn.funcion.tpev");
			TPFNTP_TPCNFNTPEV_ID = rb.getString("academico.produccion.tpcn.funcion.tpev.tpcnfntpev.id");
			TPFNTP_FNCTPEV_ID = rb.getString("academico.produccion.tpcn.funcion.tpev.fnctpev.id");
			TPFNTP_TPCN_ID = rb.getString("academico.produccion.tpcn.funcion.tpev.tpcn.id");
			TPFNTP_TPCNFNTPEV_USUARIO = rb.getString("academico.produccion.tpcn.funcion.tpev.tpcnfntpev.usuario");
			TPFNTP_TPCNFNTPEV_FECHA = rb.getString("academico.produccion.tpcn.funcion.tpev.tpcnfntpev.fecha");
			TPFNTP_TPCNFNTPEV_ESTADO = rb.getString("academico.produccion.tpcn.funcion.tpev.tpcnfntpev.estado");
			
			TABLA_CARRERA_INTERCAMBIO = rb.getString("academico.produccion.carrera.intercambio");
			CRIN_ID = rb.getString("academico.produccion.carrera.intercambio.crin.id");
			CRIN_CRR_ID = rb.getString("academico.produccion.carrera.intercambio.crin.crr");
			CRIN_FCIN_ID = rb.getString("academico.produccion.carrera.intercambio.crin.fcin");
			CRIN_AUTORIZACION = rb.getString("academico.produccion.carrera.intercambio.crin.autorizacion");
			CRIN_USER_REGISTRO = rb.getString("academico.produccion.carrera.intercambio.crin.user.registro");
			CRIN_FECHA_REGISTRO = rb.getString("academico.produccion.carrera.intercambio.crin.fecha.registro");
			CRIN_OBSERVACION = rb.getString("academico.produccion.carrera.intercambio.crin.observacion");
			CRIN_ESTADO = rb.getString("academico.produccion.carrera.intercambio.crin.estado");
			
			TABLA_MATERIA_INTERCAMBIO = rb.getString("academico.produccion.materia.intercambio");
			MTIN_ID = rb.getString("academico.produccion.materia.intercambio.mtin.id");
			MTIN_CRIN_ID = rb.getString("academico.produccion.materia.intercambio.mtin.crin");
			MTIN_MTR_ID = rb.getString("academico.produccion.materia.intercambio.mtin.mtr");
			MTIN_DESCRIPCION = rb.getString("academico.produccion.materia.intercambio.mtin.descripcion");
			MTIN_CODIGO = rb.getString("academico.produccion.materia.intercambio.mtin.codigo");
			MTIN_ESTADO = rb.getString("academico.produccion.materia.intercambio.mtin.estado");
			
			TABLA_REFERENCIA = rb.getString("academico.produccion.referencia");
			RFR_ID = rb.getString("academico.produccion.referencia.rfr.id");
			RFR_CEDULA = rb.getString("academico.produccion.referencia.rfr.cedula");
			RFR_NOMBRE = rb.getString("academico.produccion.referencia.rfr.nombre");
			RFR_DIRECCION = rb.getString("academico.produccion.referencia.rfr.direccion");
			RFR_PARENTESCO = rb.getString("academico.produccion.referencia.rfr.parentesco");
			RFR_PORCENTAJE = rb.getString("academico.produccion.referencia.rfr.porcentaje");
			RFR_ESTADO = rb.getString("academico.produccion.referencia.rfr.estado");
		}
		
	}
}
