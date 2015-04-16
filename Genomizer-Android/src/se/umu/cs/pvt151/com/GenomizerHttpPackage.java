package se.umu.cs.pvt151.com;

/**
 * This class is a model for a response from the server.
 * Its purpose is only to store response code and
 * response body.
 * 
 * @author Rickard dv12rhm
 *
 */
public class GenomizerHttpPackage {
	
	private int code; // HTTP response code
	
	private String body; //Formatted as a JSONString
	
	
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
