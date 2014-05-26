package se.umu.cs.pvt151.com;

public class GenomizerHttpPackage {
	
	private int code;
	
	private String body;
	
	
	/**
	 * Creates a new GenomizerHttpPackage object.
	 * 
	 * @param code
	 * @param body
	 */
	public GenomizerHttpPackage(int code, String body) {
		this.code = code;
		this.body = body;
	}


	/**
	 * Returns the code as an int.
	 * 
	 * @return code
	 */
	public int getCode() {
		return code;
	}


	/**
	 * Returns the body as a String.
	 * 
	 * @return body
	 */
	public String getBody() {
		return body;
	}
}
