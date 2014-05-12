package se.umu.cs.pvt151.model;

import java.util.ArrayList;

/**
 * 
 * 
 * @author Rickard
 *
 */
public class ProcessingParameters {
	
	private ArrayList<String> parameters;
	
	public ProcessingParameters() {
		parameters = new ArrayList<String>();
	}
	
	
	public void addParameter(String parameter) {
		parameters.add(parameter);
	}
	
	
	public String getParameter(int index) {
		return parameters.get(index);
	}
	
	
	public int size() {
		return parameters.size();
	}
}
