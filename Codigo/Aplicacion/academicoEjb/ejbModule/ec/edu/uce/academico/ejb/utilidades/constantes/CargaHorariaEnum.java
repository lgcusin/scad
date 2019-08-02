package ec.edu.uce.academico.ejb.utilidades.constantes;

public enum CargaHorariaEnum {

	// CONSTANTES PARA CAMPOS DE TABLA
	// cahr_estado
	ESTADO_ACTIVO(0, "ACTIVO"), 
	ESTADO_INACTIVO(1, "INACTIVO"),
	// cahr_estado_eliminado
	ESTADO_ELIMINADO_ACTIVO(0, "ELIMINADO"), 
	ESTADO_ELIMINADO_INACTIVO(1, "NO ELIMINADO"),
	
	// CONSTANTES PARA ELABORACION DE PROCESOS
	CAT_DIRECCION_GESTION(1,"DIRECCIÓN Y GESTIÓN"),
	CAT_DOCENCIA(2, "DOCENCIA"),
	CAT_INVESTIGACION(3, "INVESTIGACIÓN"),
	
	//direccion y gestion
	SUB_DG_DIRECCION(1,"DIRECCIÓN"),
	SUB_DG_GESTION(2,"GESTIÓN"),
	SUB_DG_REPRESENTANTE(3,"REPRESENTANTE"),
	SUB_DG_COORDINADOR(4,"COORDINADOR"),
	
	//docencia
	SUB_DOC_IMPARTICION_CLASES(1, "IMPARTICIÓN DE CLASES"),
	SUB_DOC_PREPARACION_CLASES(2, "PREPARACIÓN DE CLASES"),
	SUB_DOC_PREPARACION_EXAMENES(3, "PREPARACIÓN DE EXÁMENES"),
	SUB_DOC_TUTORIAS(4, "TUTORIAS ACADÉMICAS"),
	SUB_DOC_PRACTICA_EXPERIMENTACION(5, "PRÁCTICAS DE EXPERIMENTACIÓN"),
	SUB_DOC_VINCULACION(6, "VINCULACIÓN"),
	SUB_DOC_TITULACION(7, "TITULACIÓN"),
	
	//investigacion
	SUB_INV_AVANZADA(1, "INVESTIGACIÓN AVANZADA"),
	SUB_INV_BASICA(2, "INVESTIGACIÓN BÁSICA"),
	SUB_INV_ACTIVIDADES(3, "OTRAS ACTIVIDADES DE INVESTIGACIÓN"),
	SUB_INV_PROYECTOS(4, "OTROS PROYECTOS DE INVESTIGACIÓN");

	private int value;
	private String label;

	private CargaHorariaEnum(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
