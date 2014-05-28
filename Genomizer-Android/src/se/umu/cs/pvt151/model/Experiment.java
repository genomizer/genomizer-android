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

	
	/**
	 * Gets the experiment name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Sets the experiment name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Gets the user which created the experiment.
	 * 
	 * @return
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	
	/**
	 * Sets the user which created the experiment.
	 * 
	 * @param created_by
	 */
	public void setCreatedBy(String created_by) {
		this.createdBy = created_by;
	}

	
	/**
	 * gets the files of the experiment.
	 * 
	 * @return files
	 */
	public List<GeneFile> getFiles() {
		return files;
	}

	
	/**
	 * Sets the files of the experiment.
	 * 
	 * @param files
	 */
	public void setFiles(List<GeneFile> files) {
		this.files = files;
	}

	
	/**
	 * Gets the annotations of the experiment.
	 * 
	 * @return annotations
	 */
	public List<Annotation> getAnnotations() {
		return annotations;
	}
	
	
	/**
	 * Sets the annotations of the experiment.
	 * 
	 * @param annotations
	 */
	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

}
