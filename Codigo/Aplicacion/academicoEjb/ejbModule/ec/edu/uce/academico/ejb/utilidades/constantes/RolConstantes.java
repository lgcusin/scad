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
   
 ARCHIVO:     RolConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Rol 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) RolConstantes.
 * Clase que maneja las constantes de la entidad Rol.
 * @author dcollaguazo.
 * @version 1.0
 */
public class RolConstantes {
	//constantes de la entidad roles en la base de datos
	public static final String ROL_BD_ADMIN = "ADMINISTRADOR";
	public static final String ROL_BD_ESTUD = "ESTUDIANTE";
	public static final String ROL_BD_SOPORTE = "SOPORTE";
	public static final String ROL_BD_ADMINFACULTAD = "ADMINFACULTAD";
	public static final String ROL_BD_DIRCARRERA = "DIRCARRERA";
	public static final String ROL_BD_DOCENTE = "DOCENTE";
	public static final String ROL_BD_DIRRRHH = "DIRRRHH";
	public static final String ROL_BD_SECRECARRERA = "SECRECARRERA";
	public static final String ROL_BD_ADMINNIVELACION = "ADMINNIVELACION";
	public static final String ROL_BD_USUARIONIVELACION = "USUARIONIVELACION";
	public static final String ROL_BD_ADMINDGA = "ADMINDGA";
	public static final String ROL_BD_ADMINDPP = "ADMINDPP";
	public static final String ROL_BD_SECREPOSGRADO = "SECREPOSGRADO";
	public static final String ROL_BD_ESTUDIANTEPOSGRADO = "ESTUDIANTEPOSGRADO";
	public static final String ROL_BD_DECANO = "DECANO";
	public static final String ROL_BD_SUBDECANO = "SUBDECANO";
	
	public static final String ROL_BD_DIRASEGURAMIENTOCALIDAD = "DIRASEGCALIDAD";
	public static final String ROL_BD_DIRVINCULACIONSOCIEDAD = "DIRVINCULACIONSOC";
	public static final String ROL_BD_COORDINVESTIGACION = "COORDINVESTIGACION";
	public static final String ROL_BD_PRESCOMITEETICA = "PRESCOMITEETICA";
	public static final String ROL_BD_COORDGESTION = "COORDGESTION";
	public static final String ROL_BD_COORDASIGNACIONCARGAHORARIA = "COORDASIGCRGHORARIA";
	public static final String ROL_BD_DIRINVESTIGACION = "DIRINVESTIGACION";
	public static final String ROL_BD_DIRPOSGRADO = "DIRPOSGRADO";
	public static final String ROL_BD_ACTUALIZACIONDOCENTE = "ACTUALIZACIONDOCENTE";
	public static final String ROL_BD_EVALUADORDIRECTIVOS = "EVALUADORDIRECTIVOS";
	public static final String ROL_BD_EVALUADORPARESACADEMICOS = "EVALUADORPARESACADEMICOS";
	public static final String ROL_BD_COORDINADORAREA = "COORDINADORAREA"; 
	public static final String ROL_BD_ESTUD_PREGRADO = "ESTUDIANTEPREGRADO";
	public static final String ROL_BD_SECRESUFICIENCIAS = "SECRETARIO_SUFICIENCIAS";
	public static final String ROL_BD_SECRENIVELACION = "SECRETARIO_NIVELACION";
	
	public static final String ROL_BD_CONSULTOREPORTES = "CONSULTORREPORTES";
	
	//constantes de la entidad roles en el sistema
	public static final String ROL_ADMIN = "ROLE_ADMINISTRADOR";
	public static final Integer ROL_ADMIN_VALUE = Integer.valueOf(1);
	public static final String ROL_ESTUD = "ROLE_ESTUDIANTE";
	public static final Integer ROL_ESTUD_VALUE = Integer.valueOf(7);
	public static final String ROL_SOPORTE = "ROLE_SOPORTE";
	public static final Integer ROL_SOPORTE_VALUE = Integer.valueOf(2);
	public static final String ROL_ADMINFACULTAD = "ROLE_ADMINFACULTAD";
	public static final Integer ROL_ADMINFACULTAD_VALUE = Integer.valueOf(3);
	public static final String ROL_DIRCARRERA = "ROLE_DIRCARRERA";
	public static final Integer ROL_DIRCARRERA_VALUE = Integer.valueOf(4);
	public static final String ROL_DOCENTE = "ROLE_DOCENTE";
	public static final Integer ROL_DOCENTE_VALUE = Integer.valueOf(5);
	public static final String ROL_DIRRRHH = "ROLE_DIRRRHH";
	public static final Integer ROL_DIRRRHH_VALUE = Integer.valueOf(6);
	public static final String ROL_SECRECARRERA = "ROLE_SECRECARRERA";
	public static final Integer ROL_SECRECARRERA_VALUE = Integer.valueOf(8);
	public static final String ROL_ADMINNIVELACION = "ROLE_ADMINNIVELACION";
	public static final Integer ROL_ADMINNIVELACION_VALUE = Integer.valueOf(9);
	public static final String ROL_USUARIONIVELACION = "ROLE_USUARIONIVELACION";
	public static final Integer ROL_USUARIONIVELACION_VALUE = Integer.valueOf(10);
	public static final String ROL_ADMINDGA = "ROLE_ADMINDGA";
	public static final Integer ROL_ADMINDGA_VALUE = Integer.valueOf(11);
	public static final String ROL_ADMINDPP = "ROLE_ADMINDPP";
	public static final Integer ROL_ADMINDPP_VALUE = Integer.valueOf(12);
	public static final String ROL_SECREPOSGRADO = "ROLE_SECREPOSGRADO";
	public static final Integer ROL_SECREPOSGRADO_VALUE = Integer.valueOf(13);
	public static final String ROL_ESTUDIANTEPOSGRADO = "ROLE_ESTUDIANTEPOSGRADO";
	public static final Integer ROL_ESTUDIANTEPOSGRADO_VALUE = Integer.valueOf(14);
	
	public static final String ROL_DIRASEGURAMIENTOCALIDAD = "ROLE_DIRASEGCALIDAD";
	public static final Integer ROL_DIRASEGURAMIENTOCALIDAD_VALUE = Integer.valueOf(15);
	public static final String ROL_DIRVINCULACIONSOCIEDAD = "ROLE_DIRVINCULACIONSOC";
	public static final Integer ROL_DIRVINCULACIONSOCIEDAD_VALUE = Integer.valueOf(16);
	public static final String ROL_COORDINVESTIGACION = "ROLE_COORDINVESTIGACION";
	public static final Integer ROL_COORDINVESTIGACION_VALUE = Integer.valueOf(17);
	public static final String ROL_PRESCOMITEETICA = "ROLE_PRESCOMITEETICA";
	public static final Integer ROL_PRESCOMITEETICA_VALUE = Integer.valueOf(18);
	public static final String ROL_COORDGESTION = "ROLE_COORDGESTION";
	public static final Integer ROL_COORDGESTION_VALUE = Integer.valueOf(19);
	public static final String ROL_COORDASIGNACIONCARGAHORARIA = "ROLE_COORDASIGCRGHORARIA";
	public static final Integer ROL_COORDASIGNACIONCARGAHORARIA_VALUE = Integer.valueOf(20);
	public static final String ROL_DIRINVESTIGACION = "ROLE_DIRINVESTIGACION";
	public static final Integer ROL_DIRINVESTIGACION_VALUE = Integer.valueOf(21);
	
	public static final String ROL_ESTUD_PREGRADO = "ROLE_ESTUDIANTEPREGRADO";
	public static final Integer ROL_ESTUD_PREGRADO_VALUE = Integer.valueOf(22);
	
	public static final String ROL_DIRPOSGRADO = "ROLE_DIRPOSGRADO";
	public static final Integer ROL_DIRPOSGRADO_VALUE = Integer.valueOf(23);
	public static final String ROL_DIRCARRERAPOSGRADO = "ROLE_DIRCARRERAPOSGRADO";
	public static final Integer ROL_DIRCARRERAPOSGRADO_VALUE = Integer.valueOf(31); 
	public static final String ROL_SECREABOGADO = "ROLE_SECREABOGADO";
	public static final Integer ROL_SECREABOGADO_VALUE = Integer.valueOf(32);  
	public static final String ROL_ACTUALIZACIONDOCENTE = "ROLE_ACTUALIZACIONDOCENTE";
	public static final Integer ROL_ACTUALIZACIONDOCENTE_VALUE = Integer.valueOf(33); 
	public static final String ROL_EVALUADORDIRECTIVOS = "ROLE_EVALUADORDIRECTIVOS";
	public static final Integer ROL_EVALUADORDIRECTIVOS_VALUE = Integer.valueOf(34);
	public static final String ROL_EVALUADORPARESACADEMICOS = "ROLE_EVALUADORPARESACADEMICOS";
	public static final Integer ROL_EVALUADORPARESACADEMICOS_VALUE = Integer.valueOf(35);
	public static final String ROL_COORDINADORAREA = "ROLE_COORDINADORAREA";
	public static final Integer ROL_COORDINADORAREA_VALUE = Integer.valueOf(36); 
	public static final String ROL_SECRESECREABOGADO = "ROLE_SECRESECREABOGADO";
	public static final Integer ROL_SECRESECREABOGADO_VALUE = Integer.valueOf(37);
	public static final Integer ROL_SUBDECANO_VALUE = Integer.valueOf(30); 
	public static final String ROL_SUBDECANO = "ROLE_SUBDECANO";
	public static final Integer ROL_DECANO_VALUE = Integer.valueOf(29); 
	public static final String ROL_DECANO = "ROLE_DECANO";
	
	public static final Integer ROL_ADMINSUFACTIVIDADFISICA_VALUE = Integer.valueOf(45); 
	public static final String ROL_ADMINSUFACTIVIDADFISICA = "ROLE_ADMIN_SUF_ACTIVIDAD_FISICA";
	public static final Integer ROL_ADMINSUFIDIOMAS_VALUE = Integer.valueOf(44); 
	public static final String ROL_ADMINSUFIDIOMAS = "ROLE_ADMIN_SUF_IDIOMAS";
	public static final Integer ROL_ADMINSUFINFORMATICA_VALUE = Integer.valueOf(43); 
	public static final String ROL_ADMINSUFINFORMATICA = "ROLE_ADMIN_SUF_INFORMATICA";
	public static final Integer ROL_SECRESUFICIENCIAS_VALUE = Integer.valueOf(38); 
	public static final String ROL_SECRESUFICIENCIAS = "ROLE_SECRETARIO_SUFICIENCIAS";
	public static final Integer ROL_SECRENIVELACION_VALUE = Integer.valueOf(42); 
	public static final String ROL_SECRENIVELACION = "ROLE_SECRETARIO_NIVELACION";
	public static final Integer ROL_CONSULTOREPORTES_VALUE = Integer.valueOf(40); 
	public static final String ROL_CONSULTOREPORTES = "ROLE_CONSULTORREPORTES";
	public static final Integer ROL_INFORMATICOFACULTAD_VALUE = Integer.valueOf(39); 
	public static final String ROL_INFORMATICOFACULTAD = "ROLE_INFORMATICO_FACULTAD";
	public static final Integer ROL_MOVILIDADESTUDIANTIL_VALUE = Integer.valueOf(41); 
	public static final String ROL_MOVILIDADESTUDIANTIL = "ROLE_MOVILIDAD_ESTUDIANTIL";
	public static final String ROL_ADMINREPORTEPREGRADO = "ROLE_ADMINREPORTEPREGRADO";
	public static final Integer ROL_ADMINREPORTEPREGRADO_VALUE = Integer.valueOf(24);
	public static final String ROL_ADMINSEGCALIDADPREGRADOO = "ROLE_ADMINSEGCALIDADPREGRADO";
	public static final Integer ROL_ADMINSEGCALIDADPREGRADO_VALUE = Integer.valueOf(25);
	public static final String ROL_ADMINREPORTEPOSGRADO = "ROLE_ADMINREPORTEPOSGRADO";
	public static final Integer ROL_ADMINREPORTEPOSGRADO_VALUE = Integer.valueOf(26);
	public static final String ROL_ADMINSEGCALIDADPOSGRADO = "ROLE_ADMINSEGCALIDADPOSGRADO";
	public static final Integer ROL_ADMINSEGCALIDADPOSGRADO_VALUE = Integer.valueOf(27);
	public static final String ROL_ADMINFACULTADPOSGRADO = "ROLE_ADMINFACULTADPOSGRADO";
	public static final Integer ROL_ADMINFACULTADPOSGRADO_VALUE = Integer.valueOf(28);
	
	
}
