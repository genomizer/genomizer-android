package se.umu.cs.pvt151.model;

import java.util.ArrayList;

public class Annotation {
	
	private int id;
	private String name;
	
	private ArrayList<String> value;
	
	
	public Annotation() {
		value = new ArrayList<String>();
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int i) {
		this.id = i;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<String> getValue() {
		return value;
	}


	public void setValue(ArrayList<String> value) {
		this.value = value;
	}
	
	
	public void appendValue(String newValue) {
		value.add(newValue);
	}

	
	public boolean isFreeText() {
		return value.size() == 0;
	}
	
	
}
