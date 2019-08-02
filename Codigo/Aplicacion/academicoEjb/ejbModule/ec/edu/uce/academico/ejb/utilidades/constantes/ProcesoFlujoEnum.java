package ec.edu.uce.academico.ejb.utilidades.constantes;

public enum ProcesoFlujoEnum {

	/// CONSTANTES PARA CAMPOS DE TABLA
	// PRFL_ESTADO
	ESTADO_ACTIVO(0, "ACTIVO"), 
	ESTADO_INACTIVO(1, "INACTIVO"),
	
	// PRFL_DESCRIPCION
	REAJUSTE_HORARIO_CLASES(28, "REAJUSTE DE HORARIO DE CLASES");

	private int value;
	private String label;

	private ProcesoFlujoEnum(int value, String label) {
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
