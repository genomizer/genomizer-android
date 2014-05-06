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

public class File {

	private String id;
	private String type;
	private String name;
	private String uploadedBy;
	private String date;
	private String size;
	private String uRL;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getURL() {
		return uRL;
	}

	public void setURL(String uRL) {
		this.uRL = uRL;
	}



}