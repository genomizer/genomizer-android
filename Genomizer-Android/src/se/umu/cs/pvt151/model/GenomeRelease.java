package se.umu.cs.pvt151.model;

public class GenomeRelease {
	
	private String genomeVersion;
	private String specie;
	private String path;
	private String fileName;
	
	public GenomeRelease() {
		
	}

	public String getGenomeVersion() {
		return genomeVersion;
	}

	public void setGenomeVersion(String genomeVersion) {
		this.genomeVersion = genomeVersion;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
