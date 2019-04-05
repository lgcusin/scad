package helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

public class UploadHelper {
	private int limit = 1024000;
	private String types = "xls|xlsx";
	private String path = "/resources/files/";

	public UploadHelper() {

	}

	public String processUpload(Part fileupload) {
		String fileSaveData = "";
		try {
			if (fileupload.getSize() > 0) {
				String submittedFileName = getFileName(fileupload);
				if (checkFileType(submittedFileName)) {
					if (fileupload.getSize() > this.limit) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tamaño demasiado grande", null));
					} else {
						String currentFileName = submittedFileName;
						String extension = currentFileName.substring(currentFileName.lastIndexOf("."),
								currentFileName.length());
						Long nameRadom = Calendar.getInstance().getTimeInMillis();
						String newFileName = nameRadom + extension;
						fileSaveData = newFileName;
						String fileSavePath = FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath(this.path);
						try {
							byte[] filecontent = new byte[(int) fileupload.getSize()];
							InputStream in = fileupload.getInputStream();
							in.read(filecontent);
							File fileToCreate = new File(fileSavePath, newFileName);
							File folder = new File(fileSavePath);
							if (!folder.exists()) {
								folder.mkdirs();
							}
							FileOutputStream fileOutputStream = new FileOutputStream(fileToCreate);
							fileOutputStream.write(filecontent);
							fileOutputStream.flush();
							fileOutputStream.close();
							fileSaveData = newFileName;
						} catch (IOException e) {
							fileSaveData = "";
						}
					}
				} else {
					fileSaveData = "";
				}
			}
		} catch (Exception e) {
			fileSaveData = "";
		}
		return fileSaveData;
	}

	public String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String filename = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
				return filename.substring(filename.indexOf("/") + 1).substring(filename.indexOf("\\") + 1);
			}
		}
		return null;
	}

	private boolean checkFileType(String fileName) {
		if (fileName.length() > 0) {
			String[] parts = fileName.split("\\.");
			if (parts.length > 0) {
				String extension = parts[parts.length - 1];
				return this.types.contains(extension);
			}
		}
		return false;
	}

}
