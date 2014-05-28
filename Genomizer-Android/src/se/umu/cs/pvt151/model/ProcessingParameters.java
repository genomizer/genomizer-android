package se.umu.cs.pvt151.model;

import java.util.ArrayList;

/**
 * A model-class intended to store information about
 * an arbitrary number of parameters.
 * 
 * @author Rickard dv12rhm
 *
 */
public class ProcessingParameters {
	
	//ArrayList of parameters (Strings)
	private ArrayList<String> parameters;
	
	
	/**
	 * Initializes a new ProcessingParameters object.
	 * 
	 */
	public ProcessingParameters() {
		parameters = new ArrayList<String>();
	}
	
	
	/**
	 * Appends a new parameter.
	 * 
	 * @param parameter
	 */
	public void addParameter(String parameter) {
		parameters.add(parameter);
	}
	
	
	/**
	 * Returns a parameter at specified index.
	 * 
	 * @param index
	 * @return a parameter
	 */
	public String getParameter(int index) {
		return parameters.get(index);
	}
	
	
	/**
	 * Returns the number of parameters stored.
	 * 
	 * @return
	 */
	public int size() {
		return parameters.size();
	}
}
