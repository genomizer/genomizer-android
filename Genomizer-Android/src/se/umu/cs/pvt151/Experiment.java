/**
 * 
 */
package se.umu.cs.pvt151;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to contain experiment info: such as files and annotations related to an
 * experiment.
 * 
 * @author erik c11ean
 * 
 */
public class Experiment {

	private String name;
	private String created_by;
	private List<File> files = new ArrayList<File>();
	private List<Annotation> annotations = new ArrayList<Annotation>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

}
