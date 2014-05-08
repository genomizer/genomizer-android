/**
 * 
 */
package se.umu.cs.pvt151;

/**
 * Object to represent files within experiment.
 * @author erik c11ean
 *
 */
import java.util.Map;

public class GeneFile {

	private String fileId;
	private String expId;
	private String type;
	private String name;
	private String author;
	private String uploadedBy;
	private String metadata;
	private String isPrivate;
	private String path;


	public String getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public String getExpId() {
		return expId;
	}

	public void setExpId(String expId) {
		this.expId = expId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getUploadedBy() {
		return uploadedBy;
	}
	
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

}