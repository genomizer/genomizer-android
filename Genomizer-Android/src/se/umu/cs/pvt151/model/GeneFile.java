/**
 * 
 */
package se.umu.cs.pvt151.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Object to represent files within experiment.
 * @author erik c11ean
 *
 */

public class GeneFile implements Parcelable {
	
	private String fileId;
	private String expId;
	private String type;
	private String name;
	private String author;
	private String uploadedBy;
	private String isPrivate;
	private String path;
	private String url;
	private String date;
	private String grVersion;
	
	
	/**
	 * initializes a new GeneFile object.
	 */
	public GeneFile() {
		
	}
	
	
	/**
	 * Initializes a new GeneFile object.
	 * The in parameter is a parcel used to
	 * initialize the objects fields.
	 * 
	 * @param in
	 */
	private GeneFile(Parcel in) {
		fileId = in.readString();
		expId = in.readString();
		type = in.readString();
		name = in.readString();
		author = in.readString();
		uploadedBy = in.readString();
		isPrivate = in.readString();
		path = in.readString();
		url = in.readString();
		date = in.readString();
		grVersion = in.readString();
	}

	
	/**
	 * Gets the path.
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	
	/**
	 * Sets the path.
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	
	/**
	 * Gets the URL.
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	
	/**
	 * Sets the URL.
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	/**
	 * Gets the genomerelease version.
	 * 
	 * @param grVersion
	 */
	public void setGrVersion(String grVersion) {
		this.grVersion = grVersion;
	}
	
	
	/**
	 * Sets the genomerelease version.
	 * 
	 * @return genomerelease version
	 */
	public String getGrVersion() {
		return grVersion;
	}

	
	/**
	 * Gets the date.
	 * 
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	
	/**
	 * Sets the date.
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	
	/**
	 * Gets whether or not the file is private.
	 * 
	 * @return is private
	 */
	public String getIsPrivate() {
		return isPrivate;
	}

	
	/**
	 * Sets whether or not the file is private.
	 * 
	 * @param isPrivate
	 */
	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}

	
	/**
	 * Gets the file id.
	 * 
	 * @return file id
	 */
	public String getFileId() {
		return fileId;
	}

	
	/**
	 * Sets the file id.
	 * 
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	
	/**
	 * Gets the experiment id.
	 * 
	 * @return experiment id
	 */
	public String getExpId() {
		return expId;
	}

	
	/**
	 * Sets the experiment id.
	 * 
	 * @param expId
	 */
	public void setExpId(String expId) {
		this.expId = expId;
	}

	
	/**
	 * Gets the type.
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	
	/**
	 * Sets the type.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	
	/**
	 * Gets the name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Gets the author.
	 * 
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}

	
	/**
	 * Sets the author.
	 * 
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	
	/**
	 * Gets the user which uploaded the file.
	 * 
	 * @return user
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}

	
	/**
	 * Sets the user which uploaded the file.
	 * 
	 * @param uploadedBy
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	
	/**
	 * Returns 0. This method is called by the system.
	 */
	public int describeContents() {
		return 0;
	}

	
	/**
	 * Writes this object to a parcel.
	 * 
	 */
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(fileId);
		out.writeString(expId);
		out.writeString(type);
		out.writeString(name);
		out.writeString(author);
		out.writeString(uploadedBy);
		out.writeString(isPrivate);
		out.writeString(path);
		out.writeString(url);
		out.writeString(date);
		out.writeString(grVersion);
	}
	

	/**
	 * This object is used to create arrays of GeneFiles.
	 */
	public static final Parcelable.Creator<GeneFile> CREATOR
	= new Parcelable.Creator<GeneFile>() {
		public GeneFile createFromParcel(Parcel in) {
			return new GeneFile(in);
		}

		public GeneFile[] newArray(int size) {
			return new GeneFile[size];
		}
	};
}