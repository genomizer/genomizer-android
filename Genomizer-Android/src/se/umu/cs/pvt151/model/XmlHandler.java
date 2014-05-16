package se.umu.cs.pvt151.model;

import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XmlHandler {
	
	private XmlSerializer serializer = Xml.newSerializer();
	private StringWriter writer = new StringWriter();
	
	
	public XmlHandler() {
		
	}
	
	
	public void createXml() {
		
	}
	
	
	public boolean XmlFileExists() {
		return false;
	}
	
	
	public void appendFile(GeneFile file) {
		
	}
	
	
	public void removeFile(GeneFile file) {
		
	}
	
	
	public void hasFile(GeneFile file) {
		
	}
}
