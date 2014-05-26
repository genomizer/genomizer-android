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
	
	public GeneFile() {
		
	}
	
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setGrVersion(String grVersion) {
		this.grVersion = grVersion;
	}
	
	public String getGrVersion() {
		return grVersion;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
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