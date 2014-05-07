package se.umu.cs.pvt151.com;

public class GenomizerHttpPackage {
	
	private int code;
	
	private String body;
	
	
	public GenomizerHttpPackage(int code, String body) {
		this.code = code;
		this.body = body;
	}


	public int getCode() {
		return code;
	}


	public String getBody() {
		return body;
	}
}
