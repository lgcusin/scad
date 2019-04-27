/**
 * 
 */
package managedBeans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @author wilso
 *
 */

@ManagedBean(name = "registroFeriado")
@ViewScoped
public class RegistroFeriado {

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
	}

	public void limpiarFiltros() {
	}
}
