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
   
 ARCHIVO:     ConsultaBean.java	  
 DESCRIPCION: Clase que ejecuta la recepción del objeto de la consulta
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 18-12-2016			Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.hilos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
/**
 * Clase (bean)ConsultaBean. Clase que ejecuta la recepción del objeto del
 * consulta
 * 
 * 
 * @author dalbuja.
 * @version 1.0
 */

public class ConsultaBean extends Thread{
	Connection con;
	
	private List<Thread> listaHilos;
	List<Calificacion> listaEstudiantes = new ArrayList<Calificacion>();
	
	public ConsultaBean(List<Calificacion> rdfListEstdiantesEditar, Connection con){
		this.listaEstudiantes = rdfListEstdiantesEditar;
		this.con=con;
	}
	

	@Override
	public void run() {
		try {
			
			HiloDeEnvio hiloEnvio;
			List<Calificacion> listaEnvio = new ArrayList<Calificacion>();
			List<List<Calificacion>> lista = new ArrayList<List<Calificacion>>();
			
			listaHilos = new ArrayList<Thread>();
			int sumador = 0;
			for (Iterator<Calificacion> i = listaEstudiantes.iterator(); i.hasNext();) {
				listaEnvio.add(i.next());
				sumador++;
				if(sumador==500){
					lista.add(listaEnvio);	
					listaEnvio = new ArrayList<Calificacion>();		
				}else{
					if(!i.hasNext()){
						lista.add(listaEnvio);
					}
				}
			} 
			
			for(List<Calificacion> itemListaHilos : lista) {
					hiloEnvio = new HiloDeEnvio(itemListaHilos, con);
					listaHilos.add(new Thread(hiloEnvio));
			}
			
			for( Thread t : listaHilos ) { 
			      t.start();
		    }
			
			for( Thread t : listaHilos ) { 
			      t.join();
			}
			
		} catch (InterruptedException e) {
		}
		try {
			con.close();
		} catch (SQLException e) {
		}
	}
	

	
}
