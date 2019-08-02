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
   
 ARCHIVO:     SysRecConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de procedencia SYSREC.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-agosto-2018		 Freddy Guzmán 							Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) SysRecConstantes.
 * Clase que maneja las constantes del sistema de recaudaciones SYSREC.
 * @author fgguzman v1
 * @version 1.0
 */
public class SysRecConstantes {
	
	//Constantes para la tabla estado documento
	public static final int ESDC_COMPROBANTE_EMITIDO_VALUE = 1;
	public static final int ESDC_COMPROBANTE_ANULADO_VALUE = 2;
	public static final int ESDC_COMPROBANTE_ENVIADO_VALUE = 3;
	public static final int ESDC_COMPROBANTE_FACTURADO_VALUE = 4;
	public static final int ESDC_COMPROBANTE_PAGADO_VALUE = 5;
	public static final int ESDC_COMPROBANTE_CADUCADO_VALUE = 6;
	public static final int ESDC_RECIBO_PAGADO_VALUE = 7;
	public static final int ESDC_RECIBO_ANULADO_VALUE = 8;
	public static final int ESDC_RECIBO_CERRADO_VALUE = 9;
	public static final int ESDC_RECIBO_FACTURADO_VALUE = 10;
	public static final int ESDC_RECIBO_DEVUELTO_VALUE = 11;
	public static final int ESDC_RECIBO_DEPOSITADO_VALUE = 12;
	public static final int ESDC_TRANSFERENCIA_PREFACTURADA_VALUE = 13;
	public static final int ESDC_TRANSFERENCIA_FACTURADA_VALUE = 14;
	public static final int ESDC_TRANSFERENCIA_ANULADA_VALUE = 15;
	public static final int ESDC_TRANSFERENCIA_POR_COBRAR_VALUE = 16;
	public static final int ESDC_VOUCHER_PREFACTURADA_VALUE = 17;
	public static final int ESDC_VOUCHER_FACTURADA_VALUE = 18;
	public static final int ESDC_VOUCHER_ANULADA_VALUE = 19;
	public static final int ESDC_VOUCHER_POR_COBRAR_VALUE = 20;

	public static final String ESDC_COMPROBANTE_EMITIDO_LABEL = "COMPROBANTE EMITIDO";
	public static final String ESDC_COMPROBANTE_ANULADO_LABEL = "COMPROBANTE ANULADO";
	public static final String ESDC_COMPROBANTE_ENVIADO_LABEL = "COMPROBANTE ENVIADO";
	public static final String ESDC_COMPROBANTE_FACTURADO_LABEL = "COMPROBANTE FACTURADO";
	public static final String ESDC_COMPROBANTE_PAGADO_LABEL = "COMPROBANTE PAGADO";
	public static final String ESDC_COMPROBANTE_CADUCADO_LABEL = "COMPROBANTE CADUCADO";
	public static final String ESDC_RECIBO_PAGADO_LABEL = "RECIBO PAGADO";
	public static final String ESDC_RECIBO_ANULADO_LABEL = "RECIBO ANULADO";
	public static final String ESDC_RECIBO_CERRADO_LABEL = "RECIBO CERRADO";
	public static final String ESDC_RECIBO_FACTURADO_LABEL = "RECIBO FACTURADO";
	public static final String ESDC_RECIBO_DEVUELTO_LABEL = "RECIBO DEVUELTO";
	public static final String ESDC_RECIBO_DEPOSITADO_LABEL = "RECIBO DEPOSITADO";
	public static final String ESDC_TRANSFERENCIA_PREFACTURADA_LABEL = "TRANSFERENCIA PREFACTURADA";
	public static final String ESDC_TRANSFERENCIA_FACTURADA_LABEL = "TRANSFERENCIA FACTURADA";
	public static final String ESDC_TRANSFERENCIA_ANULADA_LABEL = "TRANSFERENCIA ANULADA";
	public static final String ESDC_TRANSFERENCIA_POR_COBRAR_LABEL = "TRANSFERENCIA POR COBRAR";
	public static final String ESDC_VOUCHER_PREFACTURADA_LABEL = "VOUCHER PREFACTURADA";
	public static final String ESDC_VOUCHER_FACTURADA_LABEL = "VOUCHER FACTURADA";
	public static final String ESDC_VOUCHER_ANULADA_LABEL = "VOUCHER ANULADA";
	public static final String ESDC_VOUCHER_POR_COBRAR_LABEL = "VOUCHER POR COBRAR";
}
