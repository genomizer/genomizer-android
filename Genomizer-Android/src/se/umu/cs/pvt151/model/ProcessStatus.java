package se.umu.cs.pvt151.model;

public class ProcessStatus {
	
	private String experimentName;
	private String status;
	private String author;
	
	private long timeAdded;
	private long timeStarted;
	private long timeFinnished;
	
	private String[] outputFiles;
	
	
	public String getExperimentName() {
		return experimentName;
	}


	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public long getTimeAdded() {
		return timeAdded;
	}


	public void setTimeAdded(long timeAdded) {
		this.timeAdded = timeAdded;
	}


	public long getTimeStarted() {
		return timeStarted;
	}


	public void setTimeStarted(long timeStarted) {
		this.timeStarted = timeStarted;
	}


	public long getTimeFinnished() {
		return timeFinnished;
	}


	public void setTimeFinnished(long timeFinnished) {
		this.timeFinnished = timeFinnished;
	}


	public String[] getOutputFiles() {
		return outputFiles;
	}


	public void setOutputFiles(String[] outputFiles) {
		this.outputFiles = outputFiles;
	}


	public ProcessStatus() {
		
	}

}
