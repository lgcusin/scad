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
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matriculaPosgrado;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
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
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
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

@ManagedBean(name="reImpresionComprobantePosgradoForm")
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
	@EJB
	DependenciaServicio servPfFacultadServicio;
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
		switch (usuario.getUsrIdentificacion()) {
		case "1718470105":
		case "1723585889":
		case "1720232139":
		case "0605598127":
		case "1003059803":
		case "1714005749":
		case "1714831276":
		case "1718567843":
		case "1313525196":
		case "1719302158":
		case "1757335813":
		case "0502030752":
		case "1311758419":
		case "1756157986":
		case "1711932341":
		case "0401189469":
		case "2100368345":
		case "1720559036":
		case "1103876197":
		case "1722454962":
		case "1400755987":
		case "1003567516":
		case "1720237211":
		case "1103727069":
		case "0705335693":
		case "1718895608":
		case "1717915142":
		case "0924196033":
		case "1804295812":
		case "0604088120":
		case "2300116353":
		case "1104577349":
		case "1720643426":
		case "1713848719":
		case "0302152608":
		case "1717855421":
		case "0603933268":
		case "1718315623":
		case "0603865221":
		case "1721054326":
		case "1803698883":
		case "1721898730":
		case "1720213188":
		case "1804309902":
		case "1719296640":
		case "1716259120":
		case "0401140231":
		case "1712643004":
		case "1803731353":
		case "1709574030":
		case "1709691636":
		case "0603337395":
		case "1723722383":
		case "1804129086":
		case "1804263554":
		case "0604409490":
		case "1715627475":
		case "1715351274":
		case "0401545389":
		case "1715953210":
		case "1804242939":
		case "1309189940":
		case "1720505385":
		case "1600484016":
		case "1103616700":
		case "1103817167":
		case "0704466267":
		case "0604318030":
		case "1717464323":
		case "1714471438":
		case "0201793361":
		case "1719254904":
		case "1104870751":
		case "0703924159":
		case "1717435216":
		case "1804605374":
		case "0301748018":
		case "1804044046":
		case "1312848052":
		case "1311401101":
		case "1803787918":
		case "0503513376":
		case "1003082060":
		case "0503143521":
		case "1003569165":
		case "1803113214":
		case "0201918315":
		case "1718412891":
		case "1309499927":
		case "1724114929":
		case "0401593462":
		case "1719500975":
		case "1803994563":
		case "1804611117":
		case "0201852985":
		case "0929377091":
		case "1205490327":
		case "0604672857":
		case "1725454621":
		case "1716865694":
		case "0603866708":
		case "1719376657":
		case "1720533734":
		case "0603564485":
		case "1720064847":
		case "1003559448":
		case "1721514238":
		case "0926263989":
		case "1713757852":
		case "1721880761":
		case "0106036395":
		case "0502074065":
		case "0703961623":
		case "0502332380":
		case "1725201873":
		case "1715645899":
		case "1718524737":
		case "0705416105":
		case "1804274007":
		case "1718851007":
		case "1716992803":
		case "1717839359":
		case "0923566731":
		case "1718361007":
		case "1104658701":
		case "1803605953":
		case "1312781105":
		case "1804472858":
		case "1720748621":
		case "1311594137":
		case "0302306584":
		case "0603462433":
		case "0502966757":
		case "1716386675":
		case "0705429009":
		case "1500625270":
		case "1804041620":
		case "1722246947":
		case "1719368670":
		case "1311040016":
		case "1804530325":
		case "1721711149":
		case "1105030827":
		case "1716084841":
		case "0104473772":
		case "1721071262":
		case "1719593673":
		case "1718317108":
		case "0502550908":
		case "1803743853":
		case "0922427018":
		case "1104350069":
		case "1711761237":
		case "1718401472":
		case "1720688454":
		case "1722487731":
		case "1717540569":
		case "1003821186":
		case "1721827978":
		case "0104434535":
		case "1722057583":
		case "1719372631":
		case "0502578313":
		case "1717556219":
		case "1718475443":
		case "1720897980":
		case "1716768930":
		case "0604655456":
		case "1720990520":
		case "1804697546":
		case "0202047130":
		case "1722634787":
		case "1803023009":
		case "0913727780":
		case "1715510259":
		case "1718829052":
		case "1713001194":
		case "1103302418":
		case "1721888210":
		case "1804021077":
		case "0803039270":
		case "1802822443":
		case "1719693358":
		case "1803191699":
		case "0923400279":
		case "1718481664":
		case "0401609284":
		case "1722620786":
		case "1715781835":
		case "1721096434":
		case "1722254388":
		case "1715782486":
		case "1723563944":
		case "1717555948":
		case "0801978982":
		case "1722653191":
		case "1002975181":
		case "1723867055":
		case "1721355434":
		case "0704941277":
		case "1312838582":
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Usted no posee comprobantes de pago generados en el sistema.");
			return null;
		default:
		}
		pfPostulante = new Persona();
		pfPeriodoAcademico = new PeriodoAcademico();
		pfListPeriodoAcademicoBusq = new ArrayList<PeriodoAcademicoDto>();
		try {
			pfPostulante=servPfPersona.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			pfListPeriodoAcademicoBusq = servEfPeriodoAcademicoDtoServicioJdbc.listarPeriodosMatriculadoConComprobantePagoPosgrado(usuario.getUsrIdentificacion());
			try {
				fgmfListFichaInscripcionDto = servFgmfFichaInscripcionDto.buscarXidentificacionXestadoXMatriculadoPosgrado(usuario.getUsrIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
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
		return "iniciarImpresionPosgrado";
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
			
			
			if(cmpaAux.getCmpaEstado()!=ComprobantePagoConstantes.ESTADO_PENDIENTE_GENERACION_VALUE){
				StringBuilder pathGeneralImagenes = new StringBuilder();
				pathGeneralImagenes.append(FacesContext.getCurrentInstance()
						.getExternalContext().getRealPath("/"));
				pathGeneralImagenes.append("/academico/reportes/archivosJasper/matriculaPosgrado/codigoBarras.png");
					// Generamos el código de barras para el adjunto del voucher de pago
				try {
//					//Es el tipo de clase 
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
					String fechaCaducidad = formato.format(cmpaAux.getCmpaFechaCaduca());
					
					
					
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
						} catch (CarreraException e) {
						}
						frmCrpParametros.put("carrera", carreraAux.getCrrDetalle());
						Dependencia facultadAux = null;
						try {
							facultadAux = servPfFacultadServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
						} catch (DependenciaNoEncontradoException e) {
						}
						frmCrpParametros.put("facultad",facultadAux.getDpnDescripcion());
//						if(item.getPstDireccion()!=null){
							frmCrpParametros.put("identificacion", pfPostulante.getPrsIdentificacion());
//							/////////////////////////////////////////////////////////////////////////////////
//							frmCrpParametros.put("direccion", "NO HAY");
							frmCrpParametros.put("postulante", pfPostulante.getPrsNombres()+" "+pfPostulante.getPrsPrimerApellido()+" "+pfPostulante.getPrsSegundoApellido());
//						}else{
//							frmCrpParametros.put("postulante", "CONSUMIDOR FINAL");
//							frmCrpParametros.put("identificacion", "9999999999");
							frmCrpParametros.put("direccion", "S/N");
//						}
						frmCrpParametros.put("email", pfPostulante.getPrsMailPersonal());
						
						frmCrpParametros.put("telefono", " ");
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
			}else{
				cmpaAux = new ComprobantePago();
				pracAux = new PeriodoAcademico();
				try {
					pracAux = servPfperiodoAcademico.buscarPorId(pfPeriodoAcademico.getPracId());
					for (PeriodoAcademicoDto item : pfListPeriodoAcademicoBusq) {
						if(item.getPracId()==pracAux.getPracId()){
							 try {
								 servComprobantePagoServicio.generarComprobanteMedicina(item.getCmpaId());
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
//					//Es el tipo de clase 
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
					String fechaCaducidad = formato.format(cmpaAux.getCmpaFechaCaduca());
					
					
					
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
						} catch (CarreraException e) {
						}
						frmCrpParametros.put("carrera", carreraAux.getCrrDetalle());
						Dependencia facultadAux = null;
						try {
							facultadAux = servPfFacultadServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
						} catch (DependenciaNoEncontradoException e) {
						}
						frmCrpParametros.put("facultad",facultadAux.getDpnDescripcion());
//						if(item.getPstDireccion()!=null){
							frmCrpParametros.put("identificacion", pfPostulante.getPrsIdentificacion());
//							/////////////////////////////////////////////////////////////////////////////////
//							frmCrpParametros.put("direccion", "NO HAY");
							frmCrpParametros.put("postulante", pfPostulante.getPrsPrimerApellido()+" "+pfPostulante.getPrsSegundoApellido()+" "+pfPostulante.getPrsNombres());
//						}else{
//							frmCrpParametros.put("postulante", "CONSUMIDOR FINAL");
//							frmCrpParametros.put("identificacion", "9999999999");
							frmCrpParametros.put("direccion", "S/N");
//						}
						frmCrpParametros.put("email", pfPostulante.getPrsMailPersonal());
						
						frmCrpParametros.put("telefono", " ");
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
