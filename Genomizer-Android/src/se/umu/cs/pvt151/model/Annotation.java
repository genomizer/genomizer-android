package se.umu.cs.pvt151.model;

import java.util.ArrayList;

/**
 * Annotation representation class for the Genomizer Android Application,
 * used as a data container. 
 * 
 * @author Erik Åberg, c11ean
 *
 */
public class Annotation {
	
	private int id;
	private String name;
	
	private ArrayList<String> value;
	
	/**
	 * Creates a new Annotation object.
	 */
	public Annotation() {
		value = new ArrayList<String>();
	}
	
	/**
	 * Returns the annotation-id number.
	 * 
	 * @return id number
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id for the Annotation object.
	 * 
	 * @param i the id number to set for the object
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name for the Annotation object.
	 * 
	 * @return the name as a string
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Sets the name for the Annotation object.
	 * 
	 * @param name the name to set as a string
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a list of the values of the current Annotation object.
	 * 
	 * @return values in an ArrayList containing strings
	 */
	public ArrayList<String> getValue() {
		return value;
	}

	/**
	 * Sets the values for the current Annotation object
	 * 
	 * @param value the values to set as a ArrayList of strings
	 */
	public void setValue(ArrayList<String> value) {
		this.value = value;
	}
	
	/**
	 * Appends a value string to the valueList in the current Annotation object
	 * 
	 * @param newValue the string to append to the existing list in the object
	 */
	public void appendValue(String newValue) {
		value.add(newValue);
	}

	/**
	 * Checks if the ValueList in the Annotation object is empty or not.
	 * 
	 * 
	 * @return true if the list is empty, otherwise returns false.
	 */
	public boolean isFreeText() {
		return value.isEmpty();
	}
}
