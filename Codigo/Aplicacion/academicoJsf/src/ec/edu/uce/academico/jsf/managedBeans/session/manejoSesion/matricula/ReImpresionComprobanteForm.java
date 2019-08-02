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
   
 ARCHIVO:     ReImpresionComprobanteForm.java	  
 DESCRIPCION: Bean de sesion que maneja la reimpresión del comprobante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-09-2017		 Daniel Albuja			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;

import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) ReImpresionComprobanteForm.
 * Bean de sesion que maneja la reimpresión del comprobante
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="reImpresionComprobanteForm")
@SessionScoped
public class ReImpresionComprobanteForm implements Serializable {
	private static final long serialVersionUID = -7885786472838841602L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	
	private Persona pfPostulante;
	private DependenciaDto pfFacultad;
	private Carrera pfCarrera;
	private List<DependenciaDto> pfListFacultades;
	private List<Carrera> pfListCarreras;
	private List<UbicacionDto> pfListPaises;
	private List<UbicacionDto> pfListProvincias;
	private List<UbicacionDto> pfListCantones;
	private Persona pfPersonaPostulante;
	private Integer pfPaisResidenciaId;
	private Integer pfProvinciaResidenciaId;
	private boolean pfHabilitadorGuardar;
	private boolean pfHabilitadorFechaEgresamiento;
	private Integer pfHabilitadorNombreuniversidad;
	private Integer pfSeleccionEgresamiento;
	private List<PeriodoAcademicoDto> pfListPeriodoAcademicoBusq;
	private List<FichaInscripcionDto> fgmfListFichaInscripcionDto;
	private PeriodoAcademico pfPeriodoAcademico;
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){

	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	DependenciaDtoServicioJdbc servPfFacultadDtoJdbc;
	@EJB
	CarreraServicio servFgmfCarreraServicio;
	@EJB
	PersonaServicio servPfPersona;
	@EJB
	UbicacionDtoServicioJdbc servPfUbicacionDtoJdbc;
	@EJB
	PeriodoAcademicoServicio servPfperiodoAcademico;
	@EJB
	EstudianteDtoServicioJdbc servrcfEstudianteDtoServicioJdbc;
	@EJB
	UsuarioServicio servRcfUsuario;
	@EJB
	CarreraServicio servCarreraServicio;
	@EJB
	ComprobantePagoServicio servComprobantePagoServicio;
	@EJB
	PeriodoAcademicoDtoServicioJdbc servEfPeriodoAcademicoDtoServicioJdbc;
	@EJB 
	FichaInscripcionDtoServicioJdbc servFgmfFichaInscripcionDto;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	/**
	 * Maneja de inicio del bean
	 * @return - cadena de navegacion a la pagina de postulación
	 */
	public String iniciarReimpresion(Usuario usuario){
		pfPostulante = new Persona();
		pfPeriodoAcademico = new PeriodoAcademico();
		pfListPeriodoAcademicoBusq = new ArrayList<PeriodoAcademicoDto>();
		try {
			pfPostulante=servPfPersona.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			pfListPeriodoAcademicoBusq = servEfPeriodoAcademicoDtoServicioJdbc.listarPeriodosMatriculadoConComprobantePago(usuario.getUsrIdentificacion());
			try {
				fgmfListFichaInscripcionDto = servFgmfFichaInscripcionDto.buscarXidentificacionXestadoXMatriculado(usuario.getUsrIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
			} catch (FichaInscripcionDtoException e1) {
			} catch (FichaInscripcionDtoNoEncontradoException e1) {
			}
		} catch (PersonaNoEncontradoException | PersonaException | PeriodoAcademicoDtoJdbcException | PeriodoAcademicoDtoJdbcNoEncontradoException e) {
		}
		if(pfListPeriodoAcademicoBusq.size()==0){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Usted no posee comprobantes de pago generados en el sistema.");
			return null;
		}
		pfHabilitadorGuardar=true;
		pfSeleccionEgresamiento=null;
		pfHabilitadorFechaEgresamiento=true;
		pfHabilitadorGuardar=true;
		return "iniciarImpresion";
	}
	


	/**
	 * Maneja de cancelacion de registro
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarReimpresion(){
		pfPostulante = new Persona();
		return "irMenuPrincipal";
	}
	


			
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	
	/**
	* Genera el voucher de pago y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return void
	*/
	public void generarReporteOrdenCobro(){
		pfHabilitadorGuardar=true;
			ComprobantePago cmpaAux = new ComprobantePago();
			PeriodoAcademico pracAux;
			try {
				pracAux = servPfperiodoAcademico.buscarPorId(pfPeriodoAcademico.getPracId());
				for (PeriodoAcademicoDto item : pfListPeriodoAcademicoBusq) {
					if(item.getPracId()==pracAux.getPracId()){
						 try {
							cmpaAux = servComprobantePagoServicio.buscarXId(item.getCmpaId());
						} catch (ComprobantePagoNoEncontradoException e) {
							e.printStackTrace();
						} catch (ComprobantePagoException e) {
							e.printStackTrace();
						}		
					}
				}
			} catch (PeriodoAcademicoNoEncontradoException | PeriodoAcademicoException e1) {
				e1.printStackTrace();
			}
			StringBuilder pathGeneralImagenes = new StringBuilder();
			pathGeneralImagenes.append(FacesContext.getCurrentInstance()
					.getExternalContext().getRealPath("/"));
			pathGeneralImagenes.append("/academico/reportes/archivosJasper/matriculaPosgrado/codigoBarras.png");
				// Generamos el código de barras para el adjunto del voucher de pago
			try {
//				//Es el tipo de clase 
				Barcode128 code128 = new Barcode128();
				//Seteo el tipo de codigo
				code128.setCodeType(Barcode.CODE128);
				code128.setBarHeight(15f); 
				code128.setStartStopText(false);
				code128.setExtended(true);
				code128.setX(1f);
				//Setep el codigo
				code128.setCode(cmpaAux.getCmpaNumComprobante());
				//Guardo la imagen
				java.awt.Image rawImage = code128.createAwtImage(Color.BLACK, Color.WHITE);
				BufferedImage outImage = new BufferedImage(rawImage.getWidth(null), rawImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
				outImage.getGraphics().drawImage(rawImage, 0, 0, null);
				ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				ImageIO.write(outImage, "png", bytesOut);
				bytesOut.flush();
				byte[] data = bytesOut.toByteArray();
				
				BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
				File file = new File(pathGeneralImagenes.toString());
				if (file.exists()) {
					file.delete();     
				}
				file = new File(pathGeneralImagenes.toString());
				ImageIO.write(bi, "PNG", file);
			  }catch (java.io.IOException ex) {
				  ex.printStackTrace();
			  }
			try {
				List<Map<String, Object>> frmCrpCampos = null;
				 Map<String, Object> frmCrpParametros = null;
				 String frmCrpNombreReporte = null;
				// ****************************************************************//
				// ***************** GENERACION DE LA ORDEN DE COBRO  *************//
				// ****************************************************************//
				// ****************************************************************//
				frmCrpNombreReporte = "VoucherMatricula";
				frmCrpParametros = new HashMap<String, Object>();
				
				SimpleDateFormat formato = 
						new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
				String fecha = formato.format(cmpaAux.getCmpaFechaEmision());
				Calendar c = Calendar.getInstance();
				c.setTime(new Date()); 
				c.add(Calendar.DATE, 4); 
				String fechaCaducidad = formato.format(cmpaAux.getCmpaFechaCaduca());
				
				frmCrpParametros.put("facultad","NIVELACION");
				frmCrpParametros.put("fecha",fecha);
				frmCrpParametros.put("fechaCaducidad",fechaCaducidad);
				
				frmCrpParametros.put("numComprobante",cmpaAux.getCmpaNumComprobante());
				frmCrpCampos = new ArrayList<Map<String, Object>>();
				Map<String, Object> dato = null;
					dato = new HashMap<String, Object>();
					Carrera carreraAux = new Carrera();
					try {
						carreraAux = servFgmfCarreraServicio.buscarPorId(fgmfListFichaInscripcionDto.get(0).getCrrId());
					} catch (CarreraNoEncontradoException e) {
					} catch (Exception e) {
					}
					if(carreraAux.getCrrDetalle()!=null){
						frmCrpParametros.put("carrera", carreraAux.getCrrDetalle());	
					}else{
						frmCrpParametros.put("carrera", "NIVELACION");
					}
					
//					if(item.getPstDireccion()!=null){
						frmCrpParametros.put("identificacion", pfPostulante.getPrsIdentificacion());
//						/////////////////////////////////////////////////////////////////////////////////
//						frmCrpParametros.put("direccion", "NO HAY");
						frmCrpParametros.put("postulante", pfPostulante.getPrsPrimerApellido()+" "+pfPostulante.getPrsSegundoApellido()+" "+pfPostulante.getPrsNombres());
//					}else{
//						frmCrpParametros.put("postulante", "CONSUMIDOR FINAL");
//						frmCrpParametros.put("identificacion", "9999999999");
						frmCrpParametros.put("direccion", "S/N");
//					}
					frmCrpParametros.put("email", pfPostulante.getPrsMailPersonal());
					
					frmCrpParametros.put("telefono", " ");
					int comparador = cmpaAux.getCmpaTotalPago().compareTo(new BigDecimal(108));
					if(comparador==0){
						frmCrpParametros.put("textoNivelacion","MATRICULA SEGUNDA VEZ - NIVELACION");
					}else{
						frmCrpParametros.put("textoNivelacion","MATRICULA TERCERA VEZ - NIVELACION");
					}
					frmCrpParametros.put("valorPagar", cmpaAux.getCmpaTotalPago().toString()+".00");
					StringBuilder pathGeneralReportes = new StringBuilder();
					pathGeneralReportes.append(FacesContext.getCurrentInstance()
							.getExternalContext().getRealPath("/"));
					pathGeneralReportes.append("/academico/reportes/archivosJasper/matriculaPosgrado");
					frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/logoUce.png");
					frmCrpParametros.put("imagenCodigoBarras", pathGeneralReportes+"/codigoBarras.png");
					frmCrpCampos.add(dato);
				// Establecemos en el atributo de la sesión la lista de mapas de
				// datos frmCrpCampos y parámetros frmCrpParametros
				FacesContext context = FacesContext.getCurrentInstance();
				HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
				HttpSession httpSession = request.getSession(false);
				httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
				httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
				httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
				// ******************FIN DE GENERACION DE REPORTE ************//
				pfHabilitadorGuardar=false;
			} catch (Exception e) {
				e.printStackTrace();
				pfHabilitadorGuardar=true;
			}
			
	}

	//****************************************************************/
		//******************* GETTERS Y SETTERS **************************/
		//****************************************************************/
		
		

	public Persona getPfPostulante() {
		return pfPostulante;
	}



	public void setPfPostulante(Persona pfPostulante) {
		this.pfPostulante = pfPostulante;
	}



	public DependenciaDto getPfFacultad() {
		return pfFacultad;
	}



	public void setPfFacultad(DependenciaDto pfFacultad) {
		this.pfFacultad = pfFacultad;
	}



	public Carrera getPfCarrera() {
		return pfCarrera;
	}



	public void setPfCarrera(Carrera pfCarrera) {
		this.pfCarrera = pfCarrera;
	}



	public List<DependenciaDto> getPfListFacultades() {
		return pfListFacultades;
	}



	public void setPfListFacultades(List<DependenciaDto> pfListFacultades) {
		this.pfListFacultades = pfListFacultades;
	}



	public List<Carrera> getPfListCarreras() {
		return pfListCarreras;
	}



	public void setPfListCarreras(List<Carrera> pfListCarreras) {
		this.pfListCarreras = pfListCarreras;
	}



	public List<UbicacionDto> getPfListPaises() {
		return pfListPaises;
	}



	public void setPfListPaises(List<UbicacionDto> pfListPaises) {
		this.pfListPaises = pfListPaises;
	}



	public List<UbicacionDto> getPfListProvincias() {
		return pfListProvincias;
	}



	public void setPfListProvincias(List<UbicacionDto> pfListProvincias) {
		this.pfListProvincias = pfListProvincias;
	}



	public List<UbicacionDto> getPfListCantones() {
		return pfListCantones;
	}



	public void setPfListCantones(List<UbicacionDto> pfListCantones) {
		this.pfListCantones = pfListCantones;
	}



	public Persona getPfPersonaPostulante() {
		return pfPersonaPostulante;
	}



	public void setPfPersonaPostulante(Persona pfPersonaPostulante) {
		this.pfPersonaPostulante = pfPersonaPostulante;
	}



	public Integer getPfPaisResidenciaId() {
		return pfPaisResidenciaId;
	}



	public void setPfPaisResidenciaId(Integer pfPaisResidenciaId) {
		this.pfPaisResidenciaId = pfPaisResidenciaId;
	}



	public Integer getPfProvinciaResidenciaId() {
		return pfProvinciaResidenciaId;
	}



	public void setPfProvinciaResidenciaId(Integer pfProvinciaResidenciaId) {
		this.pfProvinciaResidenciaId = pfProvinciaResidenciaId;
	}



	public boolean isPfHabilitadorGuardar() {
		return pfHabilitadorGuardar;
	}



	public void setPfHabilitadorGuardar(boolean pfHabilitadorGuardar) {
		this.pfHabilitadorGuardar = pfHabilitadorGuardar;
	}



	public boolean isPfHabilitadorFechaEgresamiento() {
		return pfHabilitadorFechaEgresamiento;
	}



	public void setPfHabilitadorFechaEgresamiento(boolean pfHabilitadorFechaEgresamiento) {
		this.pfHabilitadorFechaEgresamiento = pfHabilitadorFechaEgresamiento;
	}



	public Integer getPfHabilitadorNombreuniversidad() {
		return pfHabilitadorNombreuniversidad;
	}



	public void setPfHabilitadorNombreuniversidad(Integer pfHabilitadorNombreuniversidad) {
		this.pfHabilitadorNombreuniversidad = pfHabilitadorNombreuniversidad;
	}



	public Integer getPfSeleccionEgresamiento() {
		return pfSeleccionEgresamiento;
	}



	public void setPfSeleccionEgresamiento(Integer pfSeleccionEgresamiento) {
		this.pfSeleccionEgresamiento = pfSeleccionEgresamiento;
	}



	public List<PeriodoAcademicoDto> getPfListPeriodoAcademicoBusq() {
		return pfListPeriodoAcademicoBusq;
	}



	public void setPfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> pfListPeriodoAcademicoBusq) {
		this.pfListPeriodoAcademicoBusq = pfListPeriodoAcademicoBusq;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public PeriodoAcademico getPfPeriodoAcademico() {
		return pfPeriodoAcademico;
	}



	public void setPfPeriodoAcademico(PeriodoAcademico pfPeriodoAcademico) {
		this.pfPeriodoAcademico = pfPeriodoAcademico;
	}



	public List<FichaInscripcionDto> getFgmfListFichaInscripcionDto() {
		return fgmfListFichaInscripcionDto;
	}



	public void setFgmfListFichaInscripcionDto(List<FichaInscripcionDto> fgmfListFichaInscripcionDto) {
		this.fgmfListFichaInscripcionDto = fgmfListFichaInscripcionDto;
	}



	
	
	

}
