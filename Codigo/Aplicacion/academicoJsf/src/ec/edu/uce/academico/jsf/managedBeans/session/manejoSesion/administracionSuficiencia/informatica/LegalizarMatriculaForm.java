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
   
 ARCHIVO:     SoporteMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja temas de soporte para matriculas. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-MAY-2019 			Freddy Guzmán						Legalización de matriculas suficiencias.
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSuficiencia.informatica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobantePagoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaInformaticaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SysRecConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) LegalizarMatriculaForm. 
 * Managed Bean que administra temas relacionados a la legalización de matrículas de suficiencia en informática.
 * @author fgguzman
 * @version 1.0
 */
@SessionScoped
@ManagedBean(name = "legalizarMatriculaForm")
public class LegalizarMatriculaForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	private Usuario lmfUsuario;
	
	private String lmfPrimerApellido;
	private String lmfIdentificacion;
	private Integer lmfTipoBusqueda;
	private Integer lmfTipoUsuario;
	
	private PersonaDto lmfPersonaDto;
	
	private List<CarreraDto> lmfListCarreraDto;
	private List<ComprobantePagoDto> lmfListComprobantePagoDto;
	private List<PersonaDto> lmfListPersonaDto;
	
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private CarreraServicio servCarrera;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private ComprobantePagoServicioJdbc servComprobantePagoServicioJdbc;
	@EJB private ComprobantePagoServicio servComprobantePago;
	@EJB private SuficienciaInformaticaServicioJdbc servJdbcSuficienciaInformatica;

	// *******************************************************************/
	// **************** METODOS GENERALES DE NAVEGACIÓN  *****************/
	// *******************************************************************/

//  IDA
	
	public String irBuscarEstudiantes(Usuario usuario) {
		String retorno = null;

		lmfUsuario = usuario;
		lmfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		lmfIdentificacion = new String();
		lmfPrimerApellido = new String();
		lmfListPersonaDto = null;


		List<Carrera> carreras = new ArrayList<>();
		List<RolFlujoCarrera> rolflujocarrera;
		try {
			
			rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				carreras.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));

			}
			for (Carrera carrera : carreras) {
				if (carrera.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE) {
					retorno = "irBuscarEstudiantesSuficiencias";
					break;
//				}else if (carrera.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE ||
//						carrera.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE ||
//						carrera.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE ||
//						carrera.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE ||
//						carrera.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
//					retorno = "irBuscarEstudiantesSuficiencias";
//					break;
				}
			}
			
			
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		if (retorno == null) {
			FacesUtil.mensajeError("Ud no tiene autorización para el uso de esta funcionalidad.");
		}
		
		return retorno;
	}
	
	
	public String irBuscarComprobantes(Usuario usuario) {
		lmfUsuario = usuario;
		lmfListComprobantePagoDto = null;
		return "irComprobantes";
	}
	
	
	public String irComprobantesPago(PersonaDto estudiante){
		String retorno = null;
		
		List<ComprobantePagoDto> comprobantes = cargarComprobantesPago(estudiante.getPrsIdentificacion(), estudiante.getPrsFichaMatriculaDto().getFcmtFechaMatricula());
		if (!comprobantes.isEmpty()) {
			lmfPersonaDto = estudiante;
			
			for (ComprobantePagoDto it : comprobantes) {
				it.setCmpaAcceso(Boolean.TRUE);//disable
				it.setCmpaTotalMatricula(estudiante.getPrsFichaMatriculaDto().getFcmtValorTotal());

				if (it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_COMPROBANTE_PAGADO_VALUE) 
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_RECIBO_CERRADO_VALUE)
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_RECIBO_PAGADO_VALUE)
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_VOUCHER_FACTURADA_VALUE)
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_VOUCHER_PREFACTURADA_VALUE)) {
					if (cambiarBigDecimalToString(estudiante.getPrsFichaMatriculaDto().getFcmtValorTotal(),1).equals(cambiarBigDecimalToString(it.getCmpaTotalAPagar(),1))) {
						it.setCmpaAcceso(Boolean.FALSE);
						it.setCmpaTotalMatricula(estudiante.getPrsFichaMatriculaDto().getFcmtValorTotal());
					}
					
				}
			}
			
			lmfListComprobantePagoDto = comprobantes;
			
			retorno = "irComprobantes";	
		}
		
		return retorno;
		
	}
	
	


	public String irInicio() {
		limpiarFormEstudiantes();
		lmfListComprobantePagoDto = null;
		return "irInicio";
	}
	
	public String irEstudiantes() {
		limpiarFormEstudiantes(); 
		return "irEstudiantes";
	}
	
	
	
	public void limpiarFormEstudiantes() {
		lmfIdentificacion = new String();
		lmfPrimerApellido = new String();
		lmfListPersonaDto = null;
		lmfListComprobantePagoDto = null;
		
	}
	
	

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/
	
	/**
	 * Método que permite actualizar el estado de la matricula, pasar inscrito a matriculado.
	 * @param comprobantePago - recibo o comprobante cobrados.
	 */
	public void legalizarMatricula(ComprobantePagoDto comprobantePago){

		if (!comprobantePago.getCmpaAcceso()) {

			try {
				Integer retorno = servComprobantePagoServicioJdbc.legalizarMatricula(lmfPersonaDto.getPrsIdentificacion(), lmfPersonaDto.getPrsCarreraDto().getCrrId(), lmfPersonaDto.getPrsPeriodoAcademicoDto().getPracId());
				if (!retorno.equals(0)) {
					comprobantePago.setCmpaId(lmfPersonaDto.getPrsFichaMatriculaDto().getCmpaId());
					comprobantePago.setCmpaAcceso(Boolean.TRUE);
					try {
						servComprobantePago.actualizarComprobantePago(comprobantePago);
						FacesUtil.mensajeInfo("Matrícula legalizada con éxito. Materias legalizas: " + retorno);							
					} catch (ComprobantePagoValidacionException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (ComprobantePagoNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					}

				}else {
					comprobantePago.setCmpaAcceso(Boolean.TRUE);
					FacesUtil.mensajeError("La matrícula ya fue legalizada.");							
				}
			} catch (RecordEstudianteValidacionException e) {
				FacesUtil.mensajeError("Comuníquese con el administrador del sistema, para solventar problemas al legalizar su matrícula.");
			} catch (RecordEstudianteException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

		}

	}
	
	
	
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscarEstudiantesSIIU() {
		
		if (!lmfTipoBusqueda.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			if (lmfTipoBusqueda.equals(GeneralesConstantes.APP_FIND_BY_IDENTIFICACION)) {
				lmfListPersonaDto = cargarEstudiantesInformatica(lmfIdentificacion);
			}else {
				lmfListPersonaDto = cargarEstudiantesInformatica(lmfPrimerApellido);	
			}
			
		}else{
			FacesUtil.mensajeInfo("Ingrese la cédula, pasaporte o primer apellido del estudiante para continuar.");
		}
		
	}
	
	private List<PersonaDto> cargarEstudiantesInformatica(String param) {
		List<PersonaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcSuficienciaInformatica.buscarEstudiantesPorParametro(param, lmfTipoBusqueda);
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}



	private List<ComprobantePagoDto> cargarComprobantesPago(String prsIdentificacion, Timestamp fechaMatricula) {
		List<ComprobantePagoDto> retorno = new ArrayList<>();
		
		try {
			retorno =  servComprobantePagoServicioJdbc.buscarComprobantesDePagoInformatica(prsIdentificacion, cambiarTimestampToString(fechaMatricula, "yyyy-MM-dd"));
		} catch (ComprobantePagoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ComprobantePagoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		
		return retorno;
	}
	
	
	
	/**
	 * Metodo que convierte timestamp en String.
	 * @param timestamp - fecha
	 * @param entrada - formato
	 * @return Java.Util.Date
	 */
	private String cambiarTimestampToString(Timestamp timestamp, String salida) {
		String retorno = "";
		SimpleDateFormat sdf = new SimpleDateFormat(salida);
		Date fecha = null;
		
		try {
			fecha = sdf.parse(timestamp.toString());
			retorno = sdf.format(fecha);
		} catch (ParseException e) {
		}
		
		return retorno;
	}
	
	
	
	public String getComprobanteEstadoLabel(int param){
		String estado =  " - ";
		
		switch (param) {
		case SysRecConstantes.ESDC_COMPROBANTE_EMITIDO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_EMITIDO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_ANULADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_ANULADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_ENVIADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_ENVIADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_FACTURADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_FACTURADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_PAGADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_PAGADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_CADUCADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_CADUCADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_PAGADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_PAGADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_ANULADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_ANULADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_CERRADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_CERRADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_FACTURADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_FACTURADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_DEVUELTO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_DEVUELTO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_DEPOSITADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_DEPOSITADO_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_PREFACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_PREFACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_FACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_FACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_ANULADA_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_ANULADA_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_POR_COBRAR_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_POR_COBRAR_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_PREFACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_PREFACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_FACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_FACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_ANULADA_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_ANULADA_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_POR_COBRAR_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_POR_COBRAR_LABEL;
			break;
		}
		
		return estado;
	}
	
	public String cambiarTimestampToString(Timestamp param){
		if (param != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date fecha;
			
			try {
				fecha = sdf.parse(param.toString());
			} catch (ParseException e) {
				return "";
			}
			
			return sdf.format(fecha);
		}else {
			return "";
		}
	}
	
	public static String cambiarBigDecimalToString(BigDecimal param, int simbolo){
		
		if (param != null && param.intValue() != 0) {
			if (simbolo == 0) {
				return String.format("%.2f", param) + " %";	
			}else if(param.intValue() == -1 ){
				return "";
			}else if(simbolo == 1 ){
				return String.format("%.2f", param);
			}else if (simbolo == 2) {
				return String.valueOf(param.intValue());
			}else if (simbolo == 3) {
				return String.format("$ %.2f", param);	
			}
		}
		
		return "";
	}
	
	public void busquedaPorIdentificacion(){
		
		if (lmfIdentificacion.length() > 0) {
			lmfPrimerApellido = new String();
			lmfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (lmfPrimerApellido.length() > 0) {
			lmfIdentificacion = new String();
			lmfTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}

	// ****************************************************************/
	// ******************* GETTERS Y SETTERS **************************/
	// ****************************************************************/

	public Usuario getLmfUsuario() {
		return lmfUsuario;
	}


	public void setLmfUsuario(Usuario lmfUsuario) {
		this.lmfUsuario = lmfUsuario;
	}


	public String getLmfIdentificacion() {
		return lmfIdentificacion;
	}


	public void setLmfIdentificacion(String lmfIdentificacion) {
		this.lmfIdentificacion = lmfIdentificacion;
	}


	public List<CarreraDto> getLmfListCarreraDto() {
		return lmfListCarreraDto;
	}


	public void setLmfListCarreraDto(List<CarreraDto> lmfListCarreraDto) {
		this.lmfListCarreraDto = lmfListCarreraDto;
	}


	public List<ComprobantePagoDto> getLmfListComprobantePagoDto() {
		return lmfListComprobantePagoDto;
	}


	public void setLmfListComprobantePagoDto(List<ComprobantePagoDto> lmfListComprobantePagoDto) {
		this.lmfListComprobantePagoDto = lmfListComprobantePagoDto;
	}




	public String getLmfPrimerApellido() {
		return lmfPrimerApellido;
	}


	public void setLmfPrimerApellido(String lmfPrimerApellido) {
		this.lmfPrimerApellido = lmfPrimerApellido;
	}


	public Integer getLmfTipoBusqueda() {
		return lmfTipoBusqueda;
	}


	public void setLmfTipoBusqueda(Integer lmfTipoBusqueda) {
		this.lmfTipoBusqueda = lmfTipoBusqueda;
	}


	public List<PersonaDto> getLmfListPersonaDto() {
		return lmfListPersonaDto;
	}


	public void setLmfListPersonaDto(List<PersonaDto> lmfListPersonaDto) {
		this.lmfListPersonaDto = lmfListPersonaDto;
	}


	public Integer getLmfTipoUsuario() {
		return lmfTipoUsuario;
	}


	public void setLmfTipoUsuario(Integer lmfTipoUsuario) {
		this.lmfTipoUsuario = lmfTipoUsuario;
	}


	public PersonaDto getLmfPersonaDto() {
		return lmfPersonaDto;
	}


	public void setLmfPersonaDto(PersonaDto lmfPersonaDto) {
		this.lmfPersonaDto = lmfPersonaDto;
	}

	
	

	
}
