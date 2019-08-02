package ec.edu.uce.Biometrico.jsf.managedBeans.biometrico;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Clase definida para la estructura del reporte pdf.
 * 
 * @author wilso
 *
 */
public class TemplatePDF {

	private File pdfFile;
	private Document document;
	private PdfWriter pdfWriter;
	private Paragraph paragraph;
	private Font fTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	private Font fSubTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private Font fEncabezado = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private Font fTexto = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	private Font fResaltado = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);
	private Font fDetalle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

	/**
	 * Metodo que permite crear el archivo pdf.
	 * 
	 * @param nombreMateria
	 */
	public void openDocument(String nombreMateria) {
		createFile(nombreMateria);
		try {
			document = new Document(PageSize.A4);
			pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();
		} catch (Exception e) {
			System.out.println("Error al crear PDF" + e.toString());
		}
	}

	/**
	 * Metodo que permite validar el fichero donde se almacena el reporte pdf.
	 * 
	 * @param nombreMateria
	 */
	private void createFile(String nombreMateria) {
		File directorioReportes = new File("c:\\SistemaBiometricoUCE");
		if (directorioReportes.exists() && directorioReportes.isDirectory()) {
			pdfFile = new File(directorioReportes, "Asistencia_Docente_" + nombreMateria + ".pdf");
		} else {
			directorioReportes.mkdirs();
			pdfFile = new File(directorioReportes, "Asistencia_Docente_" + nombreMateria + ".pdf");
		}
	}

	/**
	 * Metodo que permite cerrar el documento creado.
	 */
	public void closeDocument() {
		document.close();
	}

	/**
	 * Metodo que permite setear informacion del documento.
	 * 
	 * @param titulo
	 * @param materia
	 * @param docente
	 */
	public void addMetaData(String titulo, String materia, String docente) {
		document.addTitle(titulo);
		document.addSubject(materia);
		document.addAuthor(docente);
	}

	/**
	 * Metodo que permite ingresar los titulos y subtitulos del reporte.
	 * 
	 * @param titulo
	 * @param subTitulo
	 * @param fecha
	 */
	public void addTitulo(String titulo, String subTitulo, String fecha) {
		try {
			paragraph = new Paragraph("", fTitulo);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(30);
			document.add(paragraph);

			paragraph = new Paragraph(titulo, fTitulo);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph(subTitulo, fSubTitulo);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph("Fecha reporte: " + fecha, fResaltado);
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);
		} catch (Exception e) {
			System.out.println("Error al agregar titulos PDF" + e.toString());
		}
	}

	/**
	 * Metodo que permite agregar parrafos al documento pdf.
	 * 
	 * @param content
	 */
	public void addParrafo(String content) {
		try {
			paragraph = new Paragraph(content, fTexto);
			paragraph.setSpacingAfter(5);
			paragraph.setSpacingBefore(5);
			document.add(paragraph);
		} catch (Exception e) {
			System.out.println("Error al crear PDF" + e.toString());
		}
	}

	/**
	 * Metodo que permite ingresar un parrafo detalle para el reporte.
	 * 
	 * @param content
	 */
	public void addParrafoDetalle(String content) {
		try {
			paragraph = new Paragraph(content, fDetalle);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(5);
			paragraph.setSpacingBefore(5);
			document.add(paragraph);
		} catch (Exception e) {
			System.out.println("Error al crear PDF" + e.toString());
		}
	}

	/**
	 * Metodo que permite ingresar la tabla de datos para el reporte.
	 * 
	 * @param encabezado
	 * @param estudiantes
	 */
	public void createTable(String[] encabezado, ArrayList<String[]> estudiantes) {
		try {
			paragraph = new Paragraph();
			paragraph.setFont(fTexto);
			PdfPTable pdfPTable = new PdfPTable(encabezado.length);
			pdfPTable.setWidthPercentage(100);
			pdfPTable.setSpacingBefore(5);
			PdfPCell pdfPCell;
			int i = 0;
			while (i < encabezado.length) {
				pdfPCell = new PdfPCell(new Phrase(encabezado[i++], fEncabezado));
				pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPCell.setBackgroundColor(new BaseColor(0, 165, 245));
				pdfPTable.addCell(pdfPCell);
			}

			for (int indexRow = 0; indexRow < estudiantes.size(); indexRow++) {
				String[] row = estudiantes.get(indexRow);
				for (int indexColumn = 0; indexColumn < encabezado.length; indexColumn++) {
					pdfPCell = new PdfPCell(new Phrase(row[indexColumn], fTexto));
					pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfPTable.addCell(pdfPCell);
				}
			}
			paragraph.add(pdfPTable);
			document.add(paragraph);
		} catch (Exception e) {
			System.out.println("Error al crear PDF" + e.toString());
		}
	}

	/**
	 * Metodo que permite agregar imagen de la cabecera.
	 */
	public void addImageCabecera() {
		try {
			String pathImagen = System.getProperty("jboss.server.base.dir")
					+ "\\deployments\\BiometricoEar.ear\\BiometricoJsf.war\\img\\plantillaCabecera.png";
			Image image = Image.getInstance(pathImagen);
			image.setAbsolutePosition(0f, 790f);
			image.scaleToFit(826f, 60f);
			document.add(image);
		} catch (Exception e) {
			System.out.println("Error al agregar imagen" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que permite ingresar imagen para el pie de pagina.
	 */
	public void addImagePie() {
		try {
			String pathImagen = System.getProperty("jboss.server.base.dir")
					+ "\\deployments\\BiometricoEar.ear\\BiometricoJsf.war\\img\\plantillaPie.png";
			Image image = Image.getInstance(pathImagen);
			image.setAbsolutePosition(0f, 0f);
			image.scaleToFit(600f, 60f);
			document.add(image);
		} catch (Exception e) {
			System.out.println("Error al agregar imagen" + e);
			e.printStackTrace();
		}
	}
}
