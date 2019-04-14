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

@ManagedBean(name = "administrarParametro")
@ViewScoped
public class AdministrarParametro {

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
	}

	public void limpiarFiltros() {
	}
}
