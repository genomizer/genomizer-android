/**
 * 
 */
package se.umu.cs.pvt151.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to contain experiment info: such as files and annotations related to an
 * experiment.
 * 
 * @author erik c11ean
 * 
 */
public class Experiment {

	private String name;
	private String createdBy;
	private List<GeneFile> files = new ArrayList<GeneFile>();
	private List<Annotation> annotations = new ArrayList<Annotation>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String created_by) {
		this.createdBy = created_by;
	}

	public List<GeneFile> getFiles() {
		return files;
	}

	public void setFiles(List<GeneFile> files) {
		this.files = files;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}
	
	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

}
