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
   
 ARCHIVO:     AdministracionPlanificacionCronogramaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración  del proceso Planificacion Cronogramas de las Suficiencias.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
22-OCT-2018				 FREDDY GUZMÁN						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSuficiencia.informatica;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.AulaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) AdministracionPlanificacionCronogramaForm.
 * Managed Bean que maneja las peticiones para la administración del proceso Planificacion Cronogramas de las Suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name="administracionSuficienciaParaleloForm")
@SessionScoped
public class AdministracionSuficienciaParaleloForm implements Serializable{

	private static final long serialVersionUID = -3934424476360393698L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	private Integer apfPeriodoId;
	private Integer apfCarreraId;
	private Integer apfNivelId;
	private Integer apfModalidadId;
	private Integer apfRepeticionesId;
	private Integer apfEstadoId;
	private Integer apfNuevoCupoId;
	private Integer apfDependenciaId;
	private Integer apfAreaId;
	private Integer apfActivarModal;
	private Integer apfDependenciaParaleloEspecialId;
	private Boolean apfDisabledModalidad;
	private Boolean apfRenderParaleloEspecial;
	
	private Date apfFechaIncioParaleloEspecial;
	private Date apfFechaFinParaleloEspecial;
	private Usuario apfUsuario;
	private AulaDto apfAulaDto;
	private ParaleloDto apfParaleloDto;
	private MateriaDto apfMateriaDto;
	private List<PeriodoAcademicoDto> apfListPeriodoAcademicoDto;
	private List<CarreraDto> apfListCarreraDto;
	private List<NivelDto> apfListNivelDto;
	private List<ModalidadDto> apfListModalidadDto;
	private List<ParaleloDto> apfListParaleloDto;
	private List<MateriaDto> apfListMateriaDto;
	private List<MateriaDto> apfListNivelCompletoMateriaDto;
	private List<AulaDto> apfListAulaDto;

	private List<Dependencia> apfListDependenciaParaleloEspecial;
	private List<Carrera> apfListCarreraParaleloEspecial;
	private List<Carrera> apfListArea;
	private List<Carrera> apfListCarrera;
	private List<SelectItem> apfListEstados;
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private CarreraServicio servCarrera;
	@EJB private ParaleloServicio servParalelo;
	@EJB private DependenciaServicio servDependencia;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private MallaCurricularParaleloServicio servMallaCurricularParalelo;
	@EJB private NivelServicio servNivel;
	
	@EJB private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB private NivelDtoServicioJdbc servJdbcNivelDto;
	@EJB private ParaleloDtoServicioJdbc servJdbcParaleloDto;
	@EJB private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB private AulaDtoServicioJdbc servJdbcAulaDto;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private HoraClaseDtoServicioJdbc servHoraClaseDto; 
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto;

	
	// ********************************************************************/
	// ******************* METODOS DE NAVEGACION **************************/
	// ********************************************************************/
	
	
	public String irInicio(){
		return "irInicio"; 	
	}
	
	public String irParalelosSuficiencias(Usuario usuario) {
		String retorno = null;
		
		try {

			apfUsuario = usuario;
			
			List<PeriodoAcademicoDto> periodos = servJdbcPeriodoAcademicoDto.buscarPeriodos(apfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			if (!periodos.isEmpty()) {
				Iterator<PeriodoAcademicoDto> iter = periodos.iterator();
				while(iter.hasNext()){
					PeriodoAcademicoDto cad = (PeriodoAcademicoDto) iter.next();
					if(cad.getPracTipo().equals(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE)){
						iter.remove();
					}
				}
				apfListPeriodoAcademicoDto = periodos;
			} 
			
			apfListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(apfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				apfListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			
			iniciarFormListarParalelos();
			retorno = "irParalelosSuficiencias";
			
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	public String irParalelosPregrado(Usuario usuario) {
		String retorno = null;
		
		try {

			apfUsuario = usuario;
			
			apfListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscarPeriodos(apfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			apfListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(apfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				apfListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			
			iniciarFormListarParalelos();
			retorno = "irParalelosPregrado";

		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	public String irParalelosNivelacion(Usuario usuario) {
		String retorno = null;
		
		try {

			apfUsuario = usuario;
			
			apfListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscarPeriodos(apfUsuario.getUsrId(), RolConstantes.ROL_SECRENIVELACION_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			apfListArea = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(apfUsuario.getUsrId(), RolConstantes.ROL_SECRENIVELACION_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				apfListArea.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			
			iniciarFormListarParalelos();
			retorno = "irParalelosNivelacion";
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	
	
	
	
	public String irNuevoParaleloSuficiencias() {
		iniciarFormNuevoParalelo();
		return "irNuevoParaleloSuficiencias";
	}
	
	public String irNuevoParaleloPregrado() {
		iniciarFormNuevoParalelo();
		return "irNuevoParaleloPregrado";
	}

	public String irNuevoParaleloNivelacion() {
		iniciarFormNuevoParalelo();
		return "irNuevoParaleloNivelacion";
	}

	
	
	
	
	public String irVerParaleloSuficiencias(ParaleloDto item){
		apfParaleloDto = item;
		List<MateriaDto> materias = cargarMateriasPorParalelo(item.getPrlId());
		if (!materias.isEmpty()) {
			materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			apfListMateriaDto = materias;			
		}
		return "irVerParaleloSuficiencias";
	}
	
	public String irVerParaleloPregrado(ParaleloDto item){
		apfParaleloDto = item;
		List<MateriaDto> materias = cargarMateriasPorParalelo(item.getPrlId());
		if (!materias.isEmpty()) {
			Iterator<MateriaDto> iter = materias.iterator();
			while (iter.hasNext()) {
				MateriaDto mat = (MateriaDto) iter.next();
				if (mat.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULO_VALUE)) {
					iter.remove();
				}
			}
			
			materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			apfListMateriaDto = materias;	
		}
		
		List<MateriaDto> materiasPorNivel = cargarMateriasPorCarreraNivel();
		if (!materiasPorNivel.isEmpty()) {
			for (MateriaDto materiaDto : materiasPorNivel) {
				materiaDto.setMtrCmbEstado(Boolean.FALSE);
			}
			apfListNivelCompletoMateriaDto = materiasPorNivel;
		}
		
		return "irVerParaleloPregrado";
	}
	
	public String irVerParaleloNivelacion(ParaleloDto item){
		apfParaleloDto = item;
		apfParaleloDto.setPrlCarreraDto(cargarCarreraDelArea(item.getMlcrprNivelacionCrrId()));
		List<MateriaDto> materias = cargarMateriasPorParalelo(item.getPrlId());
		if (!materias.isEmpty()) {
			materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			apfListMateriaDto = materias;	
		}
		
		List<MateriaDto> materiasPorNivel = cargarMateriasPorCarreraNivelNivelacion();
		if (!materiasPorNivel.isEmpty()) {
			for (MateriaDto materiaDto : materiasPorNivel) {
				materiaDto.setMtrCmbEstado(Boolean.FALSE);
			}
			apfListNivelCompletoMateriaDto = materiasPorNivel;
		}
		return "irVerParaleloNivelacion";
	}
	
	
	
	private CarreraDto cargarCarreraDelArea(Integer crrId) {
		CarreraDto carrera = null;
		
		try {
			carrera = servJdbcCarreraDto.buscarXId(crrId);
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return carrera;
	}

	public String irVerParaleloSuficiencias(){
		apfMateriaDto = null;
		apfListAulaDto =  null;
		apfAulaDto = null;
		
		apfListEstados = null;
		apfListModalidadDto = null;
		apfEstadoId = null;
		apfModalidadId = null;
		return "irVerParaleloSuficiencias";
	}
	
	public String irVerParaleloPregrado(){
		apfMateriaDto = null;
		apfListAulaDto =  null;
		apfAulaDto = null;
		
		apfListEstados = null;
		apfListModalidadDto = null;
		apfEstadoId = null;
		apfModalidadId = null;
		return "irVerParaleloPregrado";
	}
	
	public String irVerParaleloNivelacion(){
		apfMateriaDto = null;
		apfListAulaDto =  null;
		apfAulaDto = null;
		
		apfListEstados = null;
		apfListModalidadDto = null;
		apfEstadoId = null;
		apfModalidadId = null;
		return "irVerParaleloNivelacion";
	}
	
	public String irEditarParaleloNivelacion(){
		apfListEstados = cargarEstadosParalelos();
		
		cargarDependenciaPorCarrera(apfCarreraId);
		apfListModalidadDto = cargarModalidadesSuficiencias();
		
		apfEstadoId = apfParaleloDto.getPrlEstado();
		apfModalidadId = apfParaleloDto.getMlcrprModalidad();
		
		establecerEstadosEdicionMateriasPorParalelo();
		
		return "irEditarParaleloNivelacion";
	}
	
	
	public String irEditarParaleloSuficiencias(){
		apfListEstados = cargarEstadosParalelos();
		
		cargarDependenciaPorCarrera(apfCarreraId);
		apfListModalidadDto = cargarModalidadesSuficiencias();
		
		apfEstadoId = apfParaleloDto.getPrlEstado();
		apfModalidadId = apfParaleloDto.getMlcrprModalidad();

		return "irEditarParaleloSuficiencias";
	}
	

	
	public String irEditarParaleloPregrado(){
		apfListEstados = cargarEstadosParalelos();
		
		cargarDependenciaPorCarrera(apfCarreraId);
		apfListModalidadDto = cargarModalidadesSuficiencias();
		
		apfEstadoId = apfParaleloDto.getPrlEstado();
		apfModalidadId = apfParaleloDto.getMlcrprModalidad();
		
		establecerEstadosEdicionMateriasPorParalelo();
		
		return "irEditarParaleloPregrado";
	}
	
	
	
	public void eliminarMateriaDelParaleloPregrado(MateriaDto materia){
		List<HoraClaseDto>  retorno = new ArrayList<>();
		
		if (apfListMateriaDto.size()>1) {
			
			if (setearMallaCurricularParaleliId(materia) != 0) {

				if (!materia.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
					try{
						retorno = servHoraClaseDto.buscarHorarioAcademico(setearMallaCurricularParaleliId(materia));
					} catch (HoraClaseDtoException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (HoraClaseDtoNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					}
				}else {
					try {
						List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(materia.getMtrId());
						if (!modulos.isEmpty()) {
							List<HoraClaseDto> horario = null;
							for (MateriaDto modulo : modulos) {
								try{
									horario = servHoraClaseDto.buscarHorarioAcademicoPorPeriodoParaleloAsignatura(apfPeriodoId, apfParaleloDto.getPrlId(), modulo.getMtrId());
								}catch (Exception e) {
								}
							}

							if (horario != null && horario.size()>0) {
								retorno.addAll(horario);
							}
						}
					} catch (MallaCurricularMateriaException e) {
						FacesUtil.mensajeError("Verifique que los Módulos de la Asignatura se encuentren bien parametrizados.");
					} catch (MallaCurricularMateriaValidacionException e) {
						FacesUtil.mensajeError("Verifique que los Módulos de la Asignatura se encuentren bien parametrizados.");
					} catch (MallaCurricularMateriaNoEncontradoException e) {
						FacesUtil.mensajeError("Verifique que los Módulos de la Asignatura se encuentren bien parametrizados.");
					}
				}

				if (retorno.isEmpty()) {
					try {
						if (servParalelo.eliminarMateriaDelParalelo(materia, apfParaleloDto.getPrlId())) {
							List<MateriaDto> materias = cargarMateriasPorParalelo(apfParaleloDto.getPrlId());
							if (!materias.isEmpty()) {
								Iterator<MateriaDto> iter = materias.iterator();
								while (iter.hasNext()) {
									MateriaDto mat = (MateriaDto) iter.next();
									if (mat.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULO_VALUE)) {
										iter.remove();
									}
								}
								materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
								apfListMateriaDto = materias;
							}

							if (!apfListNivelCompletoMateriaDto.isEmpty()) {
								for (MateriaDto materiaDto : apfListNivelCompletoMateriaDto) {
									materiaDto.setMtrCmbEstado(Boolean.FALSE);
								}
							}

							establecerEstadosEdicionMateriasPorParalelo();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("La Asignatura fue eliminada del Paralelo con éxito");
						}else {
							FacesUtil.mensajeError("Error tipo SQL, verifique que las Asignaturas se encuentren bien parametrizadas.");
						}
					} catch (ParaleloException e1) {
						FacesUtil.mensajeError("Error tipo SQL, verifique que las Asignaturas se encuentren bien parametrizadas.");
					}			
				}else {
					FacesUtil.mensajeError("No se puede eliminar la Asignatura del Paralelo porque ya tiene Horario de Clases asignado.");
				}
			}else {
				FacesUtil.mensajeError("Error tipo SQL, verifique que las Asignaturas se encuentren bien parametrizadas.");
			}
			
		}else {
			FacesUtil.mensajeError("No puede eliminar todas las Asignaturas, si no desea hacer uso del Paralelo desactivelo.");
		}
		
	}
	

	private int setearMallaCurricularParaleliId(MateriaDto materia) {
		for (MateriaDto item : apfListMateriaDto) {
			if (item.getMlcrmtId() == materia.getMlcrmtId()) {
				return item.getMlcrprId();
			}
		}
		return 0;
	}

	public void agregarMateriaAlParaleloPregrado(MateriaDto materia){
		
		ParaleloDto paralelo = new ParaleloDto();
		paralelo.setPrlId(apfParaleloDto.getPrlId());
		paralelo.setMlcrprModalidad(apfModalidadId);

		try {
			if (servParalelo.agregarMateriaAparalelo(paralelo, materia)) {
				List<MateriaDto> materias = cargarMateriasPorParalelo(apfParaleloDto.getPrlId());
				if (!materias.isEmpty()) {
					Iterator<MateriaDto> iter = materias.iterator();
					while (iter.hasNext()) {
						MateriaDto mat = (MateriaDto) iter.next();
						if (mat.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULO_VALUE)) {
							iter.remove();
						}
					}
					materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
					apfListMateriaDto = materias;
				}
				establecerEstadosEdicionMateriasPorParalelo();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("La Asignatura fue agregada al Paralelo con éxito.");
			}
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

	}
	
	private void establecerEstadosEdicionMateriasPorParalelo() {
	
		if (!apfListNivelCompletoMateriaDto.isEmpty()) {
			if (!apfListMateriaDto.isEmpty()) {
				for (MateriaDto materiaPorNivel : apfListNivelCompletoMateriaDto) {
					for (MateriaDto materiaPorParalelo : apfListMateriaDto) {
						if (materiaPorNivel.getMtrCodigo().equals(materiaPorParalelo.getMtrCodigo())) {
							materiaPorNivel.setMtrCmbEstado(Boolean.TRUE);
						}
					}
				}
				apfListNivelCompletoMateriaDto.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			}
		}
	}

	
	
	
	public String irFormParalelosSuficiencias() {
		iniciarFormListarParalelos();
		return "volverParalelosSuficiencias";
	}
	
	public String irFormParalelosPregrado() {
		iniciarFormListarParalelos();
		return "volverParalelosPregrado";
	}
	
	
	public String irFormParalelosNivelacion() {
		iniciarFormListarParalelos();
		return "volverParalelosNivelacion";
	}
	
	
	
	
	public String irEditarCupoMateriaPorParaleloSuficiencias(MateriaDto item){
		
		try {
			apfMateriaDto = item;
			apfListAulaDto =  servJdbcAulaDto.buscarAulasPorMateria(item.getPrlId(), item.getMlcrmtId());
			if (apfListAulaDto != null && apfListAulaDto.size() >0) {
				apfAulaDto = apfListAulaDto.stream().min(Comparator.comparing(AulaDto::getAlaCapacidad)).get();
			}
			
		} catch (AulaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Es necesario asignar horario a la Asignatura para poder modificar el Cupo.");
			return null;
		}
		
		return "irEditarCupoMateriaSuficiencias";
	}
	
	public String irEditarCupoMateriaPorParaleloPregrado(MateriaDto item){
		String retorno = null;
		apfMateriaDto = item;

		if (item.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
			
			List<MateriaDto> materias = cargarModulosPorMateriaModularId(item.getMtrId());
			if (!materias.isEmpty()) {
				
				List<AulaDto> aulas = new ArrayList<>();
				for (MateriaDto materia : materias) {
					materia.setPrlId(item.getPrlId());
					List<AulaDto> aulasModular = cargarAulasPorMateria(materia);
					if (!aulasModular.isEmpty()) {
						aulas.addAll(aulasModular);
					}
				}

				if (!aulas.isEmpty()) {
					apfAulaDto = aulas.stream().min(Comparator.comparing(AulaDto::getAlaCapacidad)).get();
					apfListAulaDto = aulas;
					retorno ="irEditarCupoMateriaPregrado";
				}else {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Es necesario asignar horario a la materia para poder establecer el Cupo.");
				}				
				
			}

		}else {
			List<AulaDto> aulas = cargarAulasPorMateria(item);
			if (!aulas.isEmpty()) {
				apfAulaDto = aulas.stream().min(Comparator.comparing(AulaDto::getAlaCapacidad)).get();
				apfListAulaDto = aulas;
				retorno ="irEditarCupoMateriaPregrado";
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Es necesario asignar horario a la materia para poder establecer el Cupo.");
			}
		}
		
		return retorno;
	}
	
	private List<AulaDto> cargarAulasPorMateria(MateriaDto materia){
		List<AulaDto> aulas = new ArrayList<>();
		
		try {
			aulas =  servJdbcAulaDto.buscarAulasPorMateria(materia.getPrlId(), materia.getMlcrmtId());
		} catch (AulaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Es necesario asignar horario a la materia para poder establecer el Cupo.");
		}
		
		return aulas;
	}
	
	
	private List<MateriaDto> cargarModulosPorMateriaModularId(int modularMtrId) {
		List<MateriaDto> materias = new ArrayList<>();
		
		try {
			materias = servJdbcMallaCurricularMateriaDto.buscarModulos(modularMtrId);
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return materias;
	}

	public String irEditarCupoMateriaPorParaleloNivelacion(MateriaDto item){
		
		try {
			apfMateriaDto = item;
			apfListAulaDto =  servJdbcAulaDto.buscarAulasPorMateria(item.getPrlId(), item.getMlcrmtId());
			if (apfListAulaDto != null && apfListAulaDto.size() >0) {
				apfAulaDto = apfListAulaDto.stream().min(Comparator.comparing(AulaDto::getAlaCapacidad)).get();
			}
			
		} catch (AulaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (AulaDtoNoEncontradoException e) {
			apfAulaDto = new AulaDto();
//			FacesUtil.mensajeError("Es necesario asignar horario a la materia para poder establecer el Cupo.");
			return "irEditarCupoMateriaNivelacion";
		}
		
		return "irEditarCupoMateriaNivelacion";
	}
	
	



	public String irGuardarCambiosCupoPorMateriaSuficiencias(){

		try {
			servMallaCurricularParalelo.editarCupoPorMlcrprId(apfMateriaDto.getMlcrprId(), apfMateriaDto.getMlcrprCupo());
			FacesUtil.mensajeInfo("Cupo actualizado con éxito.");
			return "irVerParaleloSuficiencias";
		} catch (MallaCurricularParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("Error al actualizar el Nuevo Cupo.");
			return null;
		} catch (MallaCurricularParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}

	public String irGuardarCambiosCupoPorMateriaPregrado(){

		try {
			servMallaCurricularParalelo.editarCupoPorMlcrprId(apfMateriaDto.getMlcrprId(), apfMateriaDto.getMlcrprCupo());
			FacesUtil.mensajeInfo("Cupo actualizado con éxito.");
			return "irVerParaleloPregrado";
		} catch (MallaCurricularParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("Error al actualizar el Nuevo Cupo.");
			return null;
		} catch (MallaCurricularParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}

	public String irGuardarCambiosCupoPorMateriaNivelacion(){
		
		try {
			servMallaCurricularParalelo.editarCupoPorMlcrprId(apfMateriaDto.getMlcrprId(), apfMateriaDto.getMlcrprCupo());
			FacesUtil.mensajeInfo("Cupo actualizado con éxito.");
			return "irVerParaleloNivelacion";
		} catch (MallaCurricularParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("Error al actualizar el Nuevo Cupo.");
			return null;
		} catch (MallaCurricularParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}

	
	
	public String irGuardarCambiosParaleloPregrado(){
		
		try {
			apfParaleloDto.setMlcrprModalidad(apfModalidadId);
			apfParaleloDto.setPrlEstado(apfEstadoId);
			boolean retorno = servParalelo.editarParalelo(apfParaleloDto);
			if (retorno) {
				FacesUtil.mensajeInfo("El paralelo fue actualizado con éxito.");
			}
		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró el Paralelo solicitado.");
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return "irVerParaleloPregrado";
	}
	
	public String irGuardarCambiosParaleloSuficiencias(){
		
		try {
			apfParaleloDto.setMlcrprModalidad(apfModalidadId);
			apfParaleloDto.setPrlEstado(apfEstadoId);
			boolean retorno = servParalelo.editarParalelo(apfParaleloDto);
			if (retorno) {
				FacesUtil.mensajeInfo("El paralelo fue actualizado con éxito.");
			}
		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró el Paralelo solicitado.");
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return "irVerParalelosSuficiencias";
	}
	
	public String irGuardarCambiosParaleloNivelacion(){
		
		try {
			apfParaleloDto.setPrlEstado(apfEstadoId);
			boolean retorno = servParalelo.editarParalelo(apfParaleloDto);
			if (retorno) {
				FacesUtil.mensajeInfo("El paralelo fue actualizado con éxito.");
			}
		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró el Paralelo solicitado.");
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return "irVerParaleloNivelacion";
	}
	
	

	public String irGuardarParaleloPregrado(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					apfParaleloDto.setPracId(apfPeriodoId);
					apfParaleloDto.setCrrId(apfCarreraId);
					apfParaleloDto.setMlcrprModalidad(apfModalidadId);

					List<MateriaDto> materias = new ArrayList<>();
					for (MateriaDto item : apfListMateriaDto) {
						if (item.getMtrCmbEstado()) {
							materias.add(item);
						}
					}

					if (materias.size() > 0) {
						// controlar cuantos pide y cuantos crea.
						boolean retorno = false;
						for (int i = 0; i < apfRepeticionesId.intValue(); i++) {
							generarCodigoParalelosPregrado();
							try {
								retorno = servParalelo.crearParalelo(apfParaleloDto, materias);
							} catch (ParaleloException e) {
								FacesUtil.mensajeError(e.getMessage());
							}
						}

						if (retorno) {
							iniciarFormListarParalelos();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("El paralelo fue creado con éxito.");
							return "volverParalelosPregrado";
						} else {
							return null;
						}

					}else {
						FacesUtil.mensajeError("Seleccione por lo menos una materia para continuar.");
						return null;
					}
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					return null;
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				return null;
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
			return null;
		}

	}
	
	public String irGuardarParaleloNivelacion(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						apfParaleloDto.setPracId(apfPeriodoId);
						apfParaleloDto.setCrrId(apfAreaId);
						apfParaleloDto.setMlcrprModalidad(apfModalidadId);

						List<MateriaDto> materias = new ArrayList<>();
						for (MateriaDto item : apfListMateriaDto) {
							if (item.getMtrCmbEstado()) {
								materias.add(item);
							}
						}

						if (materias.size() > 0) {
							// controlar cuantos pide y cuantos crea.
							boolean retorno = false;
							for (int i = 0; i < apfRepeticionesId.intValue(); i++) {
								generarCodigoParalelosNivelacion();
								try {
									apfParaleloDto.setMlcrprNivelacionCrrId(apfCarreraId);
									retorno = servParalelo.crearParalelo(apfParaleloDto, materias);
								} catch (ParaleloException e) {
									FacesUtil.mensajeError(e.getMessage());
								}
							}

							if (retorno) {
								iniciarFormListarParalelos();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("El paralelo fue creado con éxito.");
								return "volverParalelosNivelacion";
							} else {
								return null;
							}

						}else {
							FacesUtil.mensajeError("Seleccione por lo menos una materia para continuar.");
							return null;
						}
					} else {
						FacesUtil.mensajeError("Seleccione un nivel para continuar.");
						return null;
					}
				}else {
					FacesUtil.mensajeError("Seleccione una carrera para continuar.");
					return null;
				}
			}else {
				FacesUtil.mensajeError("Seleccione un área para continuar.");
				return null;
			}
			
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
			return null;
		}

	}
	
	
	public String irGuardarParaleloSuficiencias(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (apfModalidadId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						apfParaleloDto.setPracId(apfPeriodoId);
						apfParaleloDto.setCrrId(apfCarreraId);
						apfParaleloDto.setMlcrprModalidad(apfModalidadId);

						List<MateriaDto> materias = new ArrayList<>();
						for (MateriaDto item : apfListMateriaDto) {
							if (item.getMtrCmbEstado()) {
								materias.add(item);
							}
						}
						
						if (materias.size() > 0) {
							// controlar cuantos pide y cuantos crea.
							boolean retorno = false;
							for (int i = 0; i < apfRepeticionesId.intValue(); i++) {
								generarCodigoParalelosInformatica();
								try {
									retorno = servParalelo.crearParalelo(apfParaleloDto, materias);
								} catch (ParaleloException e) {
									FacesUtil.mensajeError(e.getMessage());
								}
							}

							if (retorno) {
								iniciarFormListarParalelos();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("El paralelo fue creado con éxito.");
								return "irParalelosSuficiencias";
							} else {
								return null;
							}
							
						}else {
							FacesUtil.mensajeError("Seleccione por lo menos una materia para continuar.");
							return null;
						}
					}else {
						FacesUtil.mensajeError("Seleccione una modalidad para continuar.");
						return null;
					}
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					return null;
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				return null;
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
			return null;
		}

	}
	
	public String irGuardarParaleloSuficienciaInformatica(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (apfModalidadId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						
						apfParaleloDto.setPracId(apfPeriodoId);
						apfParaleloDto.setCrrId(apfCarreraId);
						apfParaleloDto.setMlcrprModalidad(apfModalidadId);

						List<MateriaDto> materias = new ArrayList<>();
						for (MateriaDto item : apfListMateriaDto) {
							if (item.getMtrCmbEstado()) {
								materias.add(item);
							}
						}
						
						if (materias.size() > 0) {
							// controlar cuantos pide y cuantos crea.
							boolean retorno = false;
							for (int i = 0; i < apfRepeticionesId.intValue(); i++) {
								generarCodigoParalelosInformatica();
								try {
									//atributos paralelo especial
									if (apfRenderParaleloEspecial) {
										apfParaleloDto.setMlcrprNivelacionCrrId(apfDependenciaParaleloEspecialId);
										apfParaleloDto.setPrlInicioClase(new Timestamp(apfFechaIncioParaleloEspecial.getTime()));
										apfParaleloDto.setPrlFinClase(new Timestamp(apfFechaFinParaleloEspecial.getTime()));
									}
									
									retorno = servParalelo.crearParalelo(apfParaleloDto, materias);
									vaciarModalParaleloEspecial();
									
								} catch (ParaleloException e) {
									FacesUtil.mensajeError(e.getMessage());
								}
							}

							if (retorno) {
								iniciarFormListarParalelos();
								apfRepeticionesId = Integer.valueOf(1);
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("El paralelo fue creado con éxito.");
								return "irParalelosSuficiencias";
							} else {
								return null;
							}
							
						}else {
							FacesUtil.mensajeError("Seleccione por lo menos una materia para continuar.");
							return null;
						}
					}else {
						FacesUtil.mensajeError("Seleccione una modalidad para continuar.");
						return null;
					}
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					return null;
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				return null;
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
			return null;
		}

	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	public void buscarCarrerasParaleloEspecial(){
		
		if (!apfDependenciaParaleloEspecialId.equals(GeneralesConstantes.APP_ID_BASE)) {
			apfListCarreraParaleloEspecial = cargarCarrerasPorDependencia();
			apfListCarreraParaleloEspecial.sort(Comparator.comparing(Carrera::getCrrDescripcion));
		}else {
			FacesUtil.mensajeInfo("Seleccione una facultad para continuar.");
		}
		
	}
	
	public void verificarFechasParaleloEspecial(){
		if (!apfDependenciaParaleloEspecialId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (apfFechaIncioParaleloEspecial != null) {
					if (apfFechaFinParaleloEspecial != null) {
						if (apfFechaFinParaleloEspecial.before(apfFechaIncioParaleloEspecial)) {
							FacesUtil.mensajeInfo("La fecha final debe ser mayor a la inicial.");
						}
					}else {
						FacesUtil.mensajeInfo("Seleccione la fecha fin para continuar.");
					}
				}else {
					FacesUtil.mensajeInfo("Seleccione la fecha de inicio para continuar.");
				}
		}else {
			FacesUtil.mensajeInfo("Seleccione una facultad para continuar.");
		}
	}
	
	
	public void asignarAtributosParaleloEspecial(){
		if (!apfDependenciaParaleloEspecialId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (apfFechaIncioParaleloEspecial != null) {
				if (apfFechaFinParaleloEspecial != null) {
					if (apfFechaFinParaleloEspecial.after(apfFechaIncioParaleloEspecial)) {
						//TODO: SETEAR PARAMETROS A PARALELO - MLCRPR
						apfRenderParaleloEspecial = true;						
					}else {
						apfRenderParaleloEspecial = false;						
						FacesUtil.mensajeInfo("La fecha final debe ser mayor a la inicial.");
					}
				}else {
					FacesUtil.mensajeInfo("Seleccione la fecha fin para continuar.");
				}
			}else {
				FacesUtil.mensajeInfo("Seleccione la fecha de inicio para continuar.");
			}
		}else {
			FacesUtil.mensajeInfo("Seleccione una facultad para continuar.");
		}
	}
	
	private List<Carrera> cargarCarrerasPorDependencia() {
		List<Carrera> retorno = new ArrayList<>();
		
		try {
			retorno = servCarrera.buscarCarreras(apfDependenciaParaleloEspecialId, CarreraConstantes.TIPO_PREGRADO_VALUE);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	public void vaciarModalParaleloEspecial(){
		iniciarModalParaleloEspecial();
		
	}
	
	public void buscarCarreras(){
		try {
			apfListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(apfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				apfListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
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
	}
	
	public void buscarCarrerasPregrado(){
		try {
			apfListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(apfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				apfListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
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
	}
	
	
	
	
	
	
	public void buscarAreasNivelacion(){
		
		limpiarCombosPeriodo();
		
		if (!apfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			FacesUtil.limpiarMensaje();
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	private void limpiarCombosPeriodo(){
		apfAreaId  = GeneralesConstantes.APP_ID_BASE;
		apfCarreraId = GeneralesConstantes.APP_ID_BASE;
		apfNivelId = GeneralesConstantes.APP_ID_BASE;
		apfListParaleloDto = null;
		apfListMateriaDto = null;
	}
	
	public void buscarCarrerasPorArea(){

		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			limpiarCombosArea();
			if (apfAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				
				try {
					 apfListCarreraDto = servJdbcCarreraDto.buscarCarrerasPorArea(apfAreaId);
				} catch (CarreraDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());	
				} catch (CarreraDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}

			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
			
	}
	
	private void limpiarCombosArea(){
		apfCarreraId = GeneralesConstantes.APP_ID_BASE;
		apfNivelId = GeneralesConstantes.APP_ID_BASE;
		apfListParaleloDto = null;
		apfListMateriaDto = null;
	}
	
	
	public void buscarNivelesNivelacion(){
		
		if (!apfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!apfAreaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				
				limpiarCombosCarrera();
				if (!apfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
					try {
						apfListNivelDto = servJdbcNivelDto.listarNivelXCarrera(apfAreaId);
					} catch (NivelDtoJdbcException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (NivelDtoJdbcNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					}
				}else {
					FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				}
				
			}else {
				limpiarCombosArea();
				FacesUtil.mensajeError("Seleccione un área para continuar.");
			}
		}else {
			limpiarCombosPeriodo();
			FacesUtil.mensajeError("Seleccione un período para continuar.");
		}
		
	}
	
	private void limpiarCombosCarrera(){
		apfNivelId = GeneralesConstantes.APP_ID_BASE;
		apfListNivelDto = null;
		apfListParaleloDto = null;
		apfListMateriaDto = null;
	}
	
	
	public void buscarNiveles(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				try {
					apfListNivelDto = servJdbcNivelDto.listarNivelXCarrera(apfCarreraId);
				} catch (NivelDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (NivelDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
		
	}
	
	public void buscarParalelosNivelacion(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						List<ParaleloDto> auxListParaleloDto = cargarParalelosPorPeriodoCarreraNivelNivelacion();
						auxListParaleloDto.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
						apfListParaleloDto = auxListParaleloDto;
					} else {
						apfListParaleloDto = null;
						FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					}
				}else {
					limpiarCombosCarrera();
					FacesUtil.mensajeError("Seleccione una área para continuar.");
				}
			}else {
				limpiarCombosArea();
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			limpiarCombosPeriodo();
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}

	}
	
	public void buscarParalelosPregrado(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						List<ParaleloDto> auxListParaleloDto = cargarParalelosPorPeriodoCarreraNivel();
						auxListParaleloDto.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
						apfListParaleloDto = auxListParaleloDto;
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
		
	}
	
	public void buscarParalelosSuficiencias(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						List<ParaleloDto> auxListParaleloDto = cargarParalelosPorPeriodoCarreraNivel();
						auxListParaleloDto.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
						apfListParaleloDto = auxListParaleloDto;
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
		
	}
	
	
	public void buscarMaterias(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						establecerModalidadCulturaFisica();
						cargarDependenciaPorCarrera(apfCarreraId);
						apfListModalidadDto = cargarModalidadesSuficiencias();
						List<MateriaDto> materias = cargarMateriasPorCarreraNivel();
						if (!materias.isEmpty()) {
							materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
							apfListMateriaDto = materias;	
						}
						
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	public void buscarMateriasInformatica(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						cargarDependenciaPorCarrera(apfCarreraId);
						apfListModalidadDto = cargarModalidadesSuficiencias();
						List<MateriaDto> materias = cargarMateriasPorCarreraNivel();
						if (!materias.isEmpty()) {
							apfListMateriaDto = materias;
							apfListMateriaDto.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
						}
						
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	
	public void buscarMateriasNivelacion(){
		if (apfPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (apfAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (apfCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (apfNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						establecerModalidadCulturaFisica();
						cargarDependenciaPorCarrera(apfAreaId);
						List<MateriaDto> materias = cargarMateriasPorCarreraNivelNivelacion();
						if (!materias.isEmpty()) {
							materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
							apfListMateriaDto = materias;	
						}
					} else {
						FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					}
				}else {
					FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione un área para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	
	private void establecerModalidadCulturaFisica() {
		
		try {
			PeriodoAcademico periodo = servPeriodoAcademico.buscarPorId(apfPeriodoId);
			if (periodo != null) {
				switch (periodo.getPracTipo()) {
				case PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE:
					apfDisabledModalidad = Boolean.TRUE;
					if (apfNivelId.equals(NivelConstantes.NIVEL_APROBACION_VALUE)) {
						apfModalidadId =  ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE;
					}else {
						apfModalidadId =  ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE;
					}
					break;
				default:
					apfModalidadId = GeneralesConstantes.APP_ID_BASE;
					apfDisabledModalidad = Boolean.FALSE;
					break;
				}
			}
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
	}

	public void limpiarFormParalelos(){
		iniciarFormListarParalelos();
	}
	
	public void limpiarFormParalelosNivelacion(){
		iniciarFormListarParalelos();
	}
	
	
	public void resetComboMateria(MateriaDto item){
		
	}
	
	
	
	
	public void crearParalelos(MateriaDto item){
		
	}
	
	public void actualizarCupoAula(){
		
	}
	
	
	public void establecerModalidad(){
		apfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apfDisabledModalidad = Boolean.FALSE;
		
		 try {
			PeriodoAcademico periodo = servPeriodoAcademico.buscarPorId(apfPeriodoId);
			if (periodo != null) {
				switch (periodo.getPracTipo()) {
				case PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE:
					apfModalidadId =  ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE;
					apfDisabledModalidad = Boolean.TRUE;
					break;
				case PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE:
					apfModalidadId = ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE;
					apfDisabledModalidad = Boolean.TRUE;
					break;
				case PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE:
					apfModalidadId = ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE;
					apfDisabledModalidad = Boolean.TRUE;
					break;
				}
			}
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public String etiquetarModalidadParalelo(int modalidad){
		String retorno = "";
		switch (modalidad) {
		case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_LABEL;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL;
			break;
		default:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL;
			break;
		}
		return retorno;
	}
	
	public String etiquetarTipoMateria(int timtId){
		String retorno = "";
		
		switch (timtId) {
		case TipoMateriaConstantes.TIPO_UNIDAD_ANALISIS_VALUE:
			retorno = TipoMateriaConstantes.TIPO_UNIDAD_ANALISIS_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_VALUE:
			retorno = TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_MODULAR_VALUE:
			retorno = TipoMateriaConstantes.TIPO_MODULAR_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_MODULO_VALUE:
			retorno = TipoMateriaConstantes.TIPO_MODULO_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_ASIGNATURA_VALUE:
			retorno = TipoMateriaConstantes.TIPO_ASIGNATURA_LABEL;
			break;
		}
		
		return retorno;
	}
	
	public String etiquetarEstadoParalelo(int estado){
		String retorno = "";
		switch (estado) {
		case ParaleloConstantes.ESTADO_ACTIVO_VALUE:
			retorno = ParaleloConstantes.ESTADO_ACTIVO_LABEL;
			break;
		case ParaleloConstantes.ESTADO_INACTIVO_VALUE:
			retorno = ParaleloConstantes.ESTADO_INACTIVO_LABEL;
			break;
		}
		return retorno;
		
	}
	
	
	public String etiquetarEstadoAula(int estado){
		String retorno = "";
		switch (estado) {
		case 0:
			retorno = AulaConstantes.ESTADO_ACTIVO_LABEL;
			break;
		case 1:
			retorno = AulaConstantes.ESTADO_INACTIVO_LABEL;
			break;
		}
		return retorno;
		
	}
	
	
	public String etiquetarTipoAula(int estado){
		String retorno = "";
		switch (estado) {
		case 0:
			retorno = AulaConstantes.TIPO_AULA_LABEL;
			break;
		case 1:
			retorno = AulaConstantes.TIPO_LABORATORIO_LABEL;
			break;
		}
		return retorno;
		
	}
	
	
	private List<MateriaDto> cargarMateriasPorCarreraNivel(){
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcMateriaDto.buscarMateriasPorCarreraNivel(apfCarreraId,apfNivelId);
		} catch (MallaCurricularDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Asignaturas con los parámetros solicitados.");
		}
		
		return retorno;
	}
	
	private List<MateriaDto> cargarMateriasPorCarreraNivelNivelacion(){
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcMateriaDto.buscarMateriasPorCarreraNivel(apfAreaId,apfNivelId);
		} catch (MallaCurricularDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Asignaturas con los parámetros solicitados.");
		}
		
		return retorno;
	}
	
	
	
	private List<ModalidadDto> cargarModalidadesSuficiencias(){
		List<ModalidadDto> retorno = new ArrayList<>();
		
		if(apfDependenciaId.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE)){
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE, ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL));
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE, ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL));
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE, ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL));
		}else if (apfDependenciaId.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE)){
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE, ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL));
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE, ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL));
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE, ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_LABEL));
		}else if (apfDependenciaId.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE)){
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE, ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL));
			retorno.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE, ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL));
		}

		return retorno;
	}
	
	private  void iniciarFormListarParalelos(){
		apfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		apfAreaId = GeneralesConstantes.APP_ID_BASE;
		apfCarreraId = GeneralesConstantes.APP_ID_BASE;
		apfNivelId = GeneralesConstantes.APP_ID_BASE;
		apfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apfListParaleloDto = null;
		apfListNivelDto = null;
		
		apfDependenciaId = null;
		apfRepeticionesId = null;
		apfParaleloDto = new ParaleloDto();
		apfListMateriaDto = null;
		apfListNivelDto = null;
		apfListModalidadDto = null;
		
		apfListNivelCompletoMateriaDto = null;
	}
	
	private  void iniciarFormNuevoParalelo(){
		apfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		apfAreaId = GeneralesConstantes.APP_ID_BASE;
		apfCarreraId = GeneralesConstantes.APP_ID_BASE;
		apfNivelId = GeneralesConstantes.APP_ID_BASE;
		apfModalidadId = GeneralesConstantes.APP_ID_BASE;
		apfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		
		apfRepeticionesId = Integer.valueOf(1);
		
		apfDisabledModalidad = Boolean.FALSE;

		
		apfParaleloDto = new ParaleloDto();
		
		apfListMateriaDto = null;
		apfListNivelDto = null;
		apfListModalidadDto = null;
		
		iniciarModalParaleloEspecial();
		
	}
	
	private void iniciarModalParaleloEspecial(){
		apfListDependenciaParaleloEspecial = cargarDependencias();
		apfListCarreraParaleloEspecial = null;
		
		apfDependenciaParaleloEspecialId = GeneralesConstantes.APP_ID_BASE;
		
		apfFechaIncioParaleloEspecial = null;
		apfFechaFinParaleloEspecial = null;
		
		apfRenderParaleloEspecial = Boolean.FALSE;
	}
	
	private List<Dependencia> cargarDependencias() {
		List<Dependencia> retorno = new ArrayList<>(); 
		
		try {
			retorno =  servDependencia.buscarDependencias(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private void cargarDependenciaPorCarrera(int carreraId){
		try {
			Dependencia dependencia = servDependencia.buscarFacultadXcrrId(carreraId);
			apfDependenciaId = dependencia.getDpnId();
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	private List<SelectItem> cargarEstadosParalelos(){
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(ParaleloConstantes.ESTADO_ACTIVO_VALUE, ParaleloConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(ParaleloConstantes.ESTADO_INACTIVO_VALUE, ParaleloConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	private void generarCodigoParalelosNivelacion(){

		StringBuilder sb = new StringBuilder();

		if (!apfAreaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!apfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if(!apfNivelId.equals(GeneralesConstantes.APP_ID_BASE)){
					try{
						Carrera area = servCarrera.buscarPorId(apfAreaId);
						String[] arreglo = area.getCrrDescripcion().split(" ");
						
						int contador = 0;
						for (int j = 0; j < arreglo.length; j++) {
							if (contador == 0) {
								sb.append(arreglo[j].substring(0, 1));
							}else if (contador == 1) {
								sb.append(arreglo[j]+"-");
							}else if ((contador > 1 && contador < 8 ) && arreglo[j].length() > 3 ) {
								sb.append(arreglo[j].substring(0, 1));
							}
							contador++;
						}
						sb.append("-");
						
						Carrera carrera = servCarrera.buscarPorId(apfCarreraId);
						String[] arregloNombre = carrera.getCrrDescripcion().split(" ");
						for (int j = 0; j < arregloNombre.length; j++) {
							if (arregloNombre[j].length() > 3) {
								sb.append(arregloNombre[j].substring(0, 1));
							}
						}
						sb.append("-");
						sb.append(getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivelNivelacion().size()));
					} catch (CarreraNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (CarreraException e) {
						FacesUtil.mensajeError(e.getMessage());
					}

				} 
			}
		}

		apfParaleloDto.setPrlCodigo(sb.toString());
		apfParaleloDto.setPrlDescripcion(sb.toString());
	}
	
	
	private void generarCodigoParalelosPregrado(){

		StringBuilder sb = new StringBuilder();

		if (!apfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if(!apfNivelId.equals(GeneralesConstantes.APP_ID_BASE)){
				try{
					Carrera carrera = servCarrera.buscarPorId(apfCarreraId);
					String[] arregloNombre = carrera.getCrrDescripcion().split(" ");
					for (int j = 0; j < arregloNombre.length; j++) {
						if(arregloNombre[j].length()>3){
							sb.append(arregloNombre[j].substring(0, 1));
						}
					}

					Nivel nivel = servNivel.buscarPorId(apfNivelId);
					sb.append(nivel.getNvlNumeral());
					sb.append("-");
					sb.append(getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
				} catch (CarreraNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (CarreraException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (NivelNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (NivelException e) {
					FacesUtil.mensajeError(e.getMessage());
				}

			} 
		}

		apfParaleloDto.setPrlCodigo(sb.toString());
		apfParaleloDto.setPrlDescripcion(sb.toString());
	}
	
	
	private void generarCodigoParalelosInformatica(){
		
			if(apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE)){

				switch (apfModalidadId) {

				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_REGULARES +getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_REGULARES +getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_INTENSIVOS+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_INTENSIVOS+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_EXONERACIONES+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_EXONERACIONES+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}

			}else if (apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE)) {
				switch (apfModalidadId) {
				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}
			}else if (apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE)) {
				switch (apfModalidadId) {
				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}
			}else if (apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE)) {
				switch (apfModalidadId) {
				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}
			}else if (apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE)) {
				switch (apfModalidadId) {
				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}
			}else if (apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE)) {
				switch (apfModalidadId) {
				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_INTENSIVOS+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_ONLINE+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}
			}else if (apfCarreraId.equals(CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
				switch (apfModalidadId) {
				case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_CULTURA_FISICA_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_CULTURA_FISICA_ACRONIMO_REGULARES+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
					apfParaleloDto.setPrlCodigo(ParaleloConstantes.PRL_SUFICIENCIA_CULTURA_FISICA_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					apfParaleloDto.setPrlDescripcion(ParaleloConstantes.PRL_SUFICIENCIA_CULTURA_FISICA_ACRONIMO_INTENSIVOS+apfNivelId+"-"+getNumberWithZeros(cargarParalelosPorPeriodoCarreraNivel().size()));
					break;
				default:
					break;
				}
			}


	}
	
	private List<ParaleloDto> cargarParalelosPorPeriodoCarreraNivel(){
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelos(apfPeriodoId,apfCarreraId,apfNivelId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<ParaleloDto> cargarParalelosPorPeriodoCarreraNivelNivelacion(){
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelosPorAreaCarrera(apfPeriodoId,apfAreaId, apfCarreraId, apfNivelId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	
	
	private List<MateriaDto> cargarMateriasPorParalelo(int paraleloId){
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcMateriaDto.buscarMateriasPorParalelo(paraleloId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Materias vinculadas al Paralelo seleccionado.");
		}
		
		return retorno;
	}
	
	
	
	
	private String getNumberWithZeros(int number) {
		return String.format("%03d", number + 1);
	}
	// ********************************************************************/
	// *********************** METODOS ENCAPSULACION **********************/
	// ********************************************************************/

	public Usuario getApfUsuario() {
		return apfUsuario;
	}

	public void setApfUsuario(Usuario apfUsuario) {
		this.apfUsuario = apfUsuario;
	}

	public Integer getApfPeriodoId() {
		return apfPeriodoId;
	}

	public void setApfPeriodoId(Integer apfPeriodoId) {
		this.apfPeriodoId = apfPeriodoId;
	}

	public Integer getApfCarreraId() {
		return apfCarreraId;
	}

	public void setApfCarreraId(Integer apfCarreraId) {
		this.apfCarreraId = apfCarreraId;
	}

	public Integer getApfNivelId() {
		return apfNivelId;
	}

	public void setApfNivelId(Integer apfNivelId) {
		this.apfNivelId = apfNivelId;
	}

	public List<PeriodoAcademicoDto> getApfListPeriodoAcademicoDto() {
		return apfListPeriodoAcademicoDto;
	}

	public void setApfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> apfListPeriodoAcademicoDto) {
		this.apfListPeriodoAcademicoDto = apfListPeriodoAcademicoDto;
	}

	public List<CarreraDto> getApfListCarreraDto() {
		return apfListCarreraDto;
	}

	public void setApfListCarreraDto(List<CarreraDto> apfListCarreraDto) {
		this.apfListCarreraDto = apfListCarreraDto;
	}

	public List<NivelDto> getApfListNivelDto() {
		return apfListNivelDto;
	}

	public void setApfListNivelDto(List<NivelDto> apfListNivelDto) {
		this.apfListNivelDto = apfListNivelDto;
	}

	public List<MateriaDto> getApfListMateriaDto() {
		return apfListMateriaDto;
	}

	public void setApfListMateriaDto(List<MateriaDto> apfListMateriaDto) {
		this.apfListMateriaDto = apfListMateriaDto;
	}

	public List<Carrera> getApfListCarrera() {
		return apfListCarrera;
	}

	public void setApfListCarrera(List<Carrera> apfListCarrera) {
		this.apfListCarrera = apfListCarrera;
	}

	public List<ParaleloDto> getApfListParaleloDto() {
		return apfListParaleloDto;
	}

	public void setApfListParaleloDto(List<ParaleloDto> apfListParaleloDto) {
		this.apfListParaleloDto = apfListParaleloDto;
	}

	public Integer getApfModalidadId() {
		return apfModalidadId;
	}

	public void setApfModalidadId(Integer apfModalidadId) {
		this.apfModalidadId = apfModalidadId;
	}

	public List<ModalidadDto> getApfListModalidadDto() {
		return apfListModalidadDto;
	}

	public void setApfListModalidadDto(List<ModalidadDto> apfListModalidadDto) {
		this.apfListModalidadDto = apfListModalidadDto;
	}

	public Integer getApfActivarModal() {
		return apfActivarModal;
	}

	public void setApfActivarModal(Integer apfActivarModal) {
		this.apfActivarModal = apfActivarModal;
	}

	public Integer getApfRepeticionesId() {
		return apfRepeticionesId;
	}

	public void setApfRepeticionesId(Integer apfRepeticionesId) {
		this.apfRepeticionesId = apfRepeticionesId;
	}

	public ParaleloDto getApfParaleloDto() {
		return apfParaleloDto;
	}

	public void setApfParaleloDto(ParaleloDto apfParaleloDto) {
		this.apfParaleloDto = apfParaleloDto;
	}

	public List<SelectItem> getApfListEstados() {
		return apfListEstados;
	}

	public void setApfListEstados(List<SelectItem> apfListEstados) {
		this.apfListEstados = apfListEstados;
	}

	public Integer getApfEstadoId() {
		return apfEstadoId;
	}

	public void setApfEstadoId(Integer apfEstadoId) {
		this.apfEstadoId = apfEstadoId;
	}

	public Integer getApfNuevoCupoId() {
		return apfNuevoCupoId;
	}

	public void setApfNuevoCupoId(Integer apfNuevoCupoId) {
		this.apfNuevoCupoId = apfNuevoCupoId;
	}

	public Integer getApfDependenciaId() {
		return apfDependenciaId;
	}

	public void setApfDependenciaId(Integer apfDependenciaId) {
		this.apfDependenciaId = apfDependenciaId;
	}

	public AulaDto getApfAulaDto() {
		return apfAulaDto;
	}

	public void setApfAulaDto(AulaDto apfAulaDto) {
		this.apfAulaDto = apfAulaDto;
	}

	public List<AulaDto> getApfListAulaDto() {
		return apfListAulaDto;
	}

	public void setApfListAulaDto(List<AulaDto> apfListAulaDto) {
		this.apfListAulaDto = apfListAulaDto;
	}

	public MateriaDto getApfMateriaDto() {
		return apfMateriaDto;
	}

	public void setApfMateriaDto(MateriaDto apfMateriaDto) {
		this.apfMateriaDto = apfMateriaDto;
	}

	public Boolean getApfDisabledModalidad() {
		return apfDisabledModalidad;
	}

	public void setApfDisabledModalidad(Boolean apfDisabledModalidad) {
		this.apfDisabledModalidad = apfDisabledModalidad;
	}

	public Integer getApfAreaId() {
		return apfAreaId;
	}

	public void setApfAreaId(Integer apfAreaId) {
		this.apfAreaId = apfAreaId;
	}

	public List<MateriaDto> getApfListNivelCompletoMateriaDto() {
		return apfListNivelCompletoMateriaDto;
	}

	public void setApfListNivelCompletoMateriaDto(List<MateriaDto> apfListNivelCompletoMateriaDto) {
		this.apfListNivelCompletoMateriaDto = apfListNivelCompletoMateriaDto;
	}

	public List<Carrera> getApfListArea() {
		return apfListArea;
	}

	public void setApfListArea(List<Carrera> apfListArea) {
		this.apfListArea = apfListArea;
	}

	public Integer getApfDependenciaParaleloEspecialId() {
		return apfDependenciaParaleloEspecialId;
	}

	public void setApfDependenciaParaleloEspecialId(Integer apfDependenciaParaleloEspecialId) {
		this.apfDependenciaParaleloEspecialId = apfDependenciaParaleloEspecialId;
	}


	public Date getApfFechaIncioParaleloEspecial() {
		return apfFechaIncioParaleloEspecial;
	}

	public void setApfFechaIncioParaleloEspecial(Date apfFechaIncioParaleloEspecial) {
		this.apfFechaIncioParaleloEspecial = apfFechaIncioParaleloEspecial;
	}

	public Date getApfFechaFinParaleloEspecial() {
		return apfFechaFinParaleloEspecial;
	}

	public void setApfFechaFinParaleloEspecial(Date apfFechaFinParaleloEspecial) {
		this.apfFechaFinParaleloEspecial = apfFechaFinParaleloEspecial;
	}

	public List<Dependencia> getApfListDependenciaParaleloEspecial() {
		return apfListDependenciaParaleloEspecial;
	}

	public void setApfListDependenciaParaleloEspecial(List<Dependencia> apfListDependenciaParaleloEspecial) {
		this.apfListDependenciaParaleloEspecial = apfListDependenciaParaleloEspecial;
	}

	public List<Carrera> getApfListCarreraParaleloEspecial() {
		return apfListCarreraParaleloEspecial;
	}

	public void setApfListCarreraParaleloEspecial(List<Carrera> apfListCarreraParaleloEspecial) {
		this.apfListCarreraParaleloEspecial = apfListCarreraParaleloEspecial;
	}

	public Boolean getApfRenderParaleloEspecial() {
		return apfRenderParaleloEspecial;
	}

	public void setApfRenderParaleloEspecial(Boolean apfRenderParaleloEspecial) {
		this.apfRenderParaleloEspecial = apfRenderParaleloEspecial;
	}

	


}

