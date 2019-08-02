package ec.edu.uce.academico.ejb.utilidades.constantes;

public enum PuestoEnum {

	/// CONSTANTES PARA CAMPOS DE TABLA
	// PST_ESTADO
	ESTADO_ACTIVO(0, "ACTIVO"), 
	ESTADO_INACTIVO(1, "INACTIVO"),
	
	// PST_NIVEL_RANGO_GRADUAL
	NIVEL_RANGO_GRADUAL_UNO(1, "NIVEL 1"),
	NIVEL_RANGO_GRADUAL_DOS(2, "NIVEL 2"),
	NIVEL_RANGO_GRADUAL_TRES(3, "NIVEL 3"),
	
	// PST_CATEGORIA_DOCENTE
	PST_CATEGORIA_DOCENTE_PRINCIPAL(1, "PRINCIPAL"),
	PST_CATEGORIA_DOCENTE_AGREGADO(2, "AGREGADO"),
	PST_CATEGORIA_DOCENTE_AUXILIAR(3, "AUXILIAR"),
	PST_CATEGORIA_DOCENTE_HONORARIO(4, "HONORARIO"),
	PST_CATEGORIA_DOCENTE_INVITADO(5, "INVITADO"),
	PST_CATEGORIA_DOCENTE_OCASIONAL(6, "OCASIONAL");

	private int value;
	private String label;

	private PuestoEnum(int value, String label) {
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
