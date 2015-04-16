package se.umu.cs.pvt151.model;


/**
 * A model-class intended to store information about
 * a genome release.
 * 
 * @author Rickard dv12rhm
 *
 */
public class GenomeRelease {
	
	private String genomeVersion;
	private String specie;
	private String path;
	//TODO private ArrayList<String> files; ??
	
	
	/**
	 * Creates and initializes a new GenomeRelease
	 * object.
	 * 
	 */
	public GenomeRelease() {
		
	}

	
	/**
	 * Returns the genomeversion.
	 * 
	 * @return genomeversion
	 */
	public String getGenomeVersion() {
		return genomeVersion;
	}

	
	/**
	 * Sets the genomeversion.
	 * 
	 * @param genomeVersion
	 */
	public void setGenomeVersion(String genomeVersion) {
		this.genomeVersion = genomeVersion;
	}

	
	/**
	 * Returns the species.
	 * 
	 * @return species
	 */
	public String getSpecie() {
		return specie;
	}

	
	/**
	 * Sets the species.
	 * 
	 * @param specie
	 */
	public void setSpecie(String specie) {
		this.specie = specie;
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
}
