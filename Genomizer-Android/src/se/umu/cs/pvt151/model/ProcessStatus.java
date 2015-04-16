package se.umu.cs.pvt151.model;

/**
 * A model-class intended to store information about
 * the status of a process.
 * 
 * @author Rickard dv12rhm
 *
 */
public class ProcessStatus {

	private String experimentName;
	private String status;
	private String author;

	private long timeAdded;
	private long timeStarted;
	private long timeFinnished;

	private String[] outputFiles;
	
	/**
	 * Creates a new ProcessStatus object.
	 */
	public ProcessStatus() {

	}

	/**
	 * Returns the experiments name.
	 * 
	 * @return experimentName
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * Sets the experiments name
	 * 
	 * @param experimentName
	 */
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	/**
	 * Returns the process status.
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the process status
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the author.
	 * 
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author of the experiment.
	 * 
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Returns the time when the process
	 * was added.
	 * 
	 * @return timeAdded
	 */
	public long getTimeAdded() {
		return timeAdded;
	}

	/**
	 * Sets the time when the process
	 * was added.
	 * 
	 * @param timeAdded
	 */
	public void setTimeAdded(long timeAdded) {
		this.timeAdded = timeAdded;
	}

	/**
	 * Returns the time when the process
	 * was started.
	 * 
	 * @return timeStarted
	 */
	public long getTimeStarted() {
		return timeStarted;
	}

	/**
	 * Sets the time when the process
	 * was started.
	 * 
	 * @param timeStarted
	 */
	public void setTimeStarted(long timeStarted) {
		this.timeStarted = timeStarted;
	}

	/**
	 * Returns the time when the process
	 * was finished
	 * 
	 * @return Time when finished
	 */
	public long getTimeFinnished() {
		return timeFinnished;
	}

	/**
	 * Sets the time when the process
	 * was finished.
	 * 
	 * @param timeFinnished
	 */
	public void setTimeFinnished(long timeFinnished) {
		this.timeFinnished = timeFinnished;
	}

	/**
	 * Returns the output-files of the process.
	 * 
	 * @return outputFiles
	 */
	public String[] getOutputFiles() {
		return outputFiles;
	}

	/**
	 * Sets the output-files of the process.
	 * 
	 * @param outputFiles
	 */
	public void setOutputFiles(String[] outputFiles) {
		this.outputFiles = outputFiles;
	}
}
