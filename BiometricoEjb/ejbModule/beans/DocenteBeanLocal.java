package beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import model.FichaDocente;
import model.TipoHuella;

@Local
public interface DocenteBeanLocal {
	
	public FichaDocente getDocente(Integer id);

	public List<FichaDocente> listar(String parametro);

	public List<TipoHuella> listarDedos();

	public List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException;
}
