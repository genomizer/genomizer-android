/**
 * 
 */
package se.umu.cs.pvt151.model;

import java.util.ArrayList;

import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Data container for conversionParameters for the Genomizer Android 
 * application.
 * 
 * @author Anders
 *
 */
public class ConversionParameter {	
	
	String name;
	String presentValue;
	String parameterType;
	int selectedSpinnerVal;
	boolean checked;
	boolean enabled;
	EditText editText;
	CheckBox checkBox;
	ArrayList<String> values;
	
	
	public ConversionParameter(String name) {
		this.name = name;
		checked = false;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the presentValue
	 */
	public String getPresentValue() {
		return presentValue;
	}

	/**
	 * @param presentValue the presentValue to set
	 */
	public void setPresentValue(String presentValue) {
		this.presentValue = presentValue;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the values
	 */
	public ArrayList<String> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	/**
	 * @return the parameterType
	 */
	public String getParameterType() {
		return parameterType;
	}

	/**
	 * @param parameterType the parameterType to set
	 */
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	/**
	 * @return the selectedSpinnerVal
	 */
	public int getSelectedSpinnerVal() {
		return selectedSpinnerVal;
	}

	/**
	 * @param selectedSpinnerVal the selectedSpinnerVal to set
	 */
	public void setSelectedSpinnerVal(int selectedSpinnerVal) {
		this.selectedSpinnerVal = selectedSpinnerVal;
	}
	
	@Override
	public String toString() {
		String s = "Parameter name: " + name + "\n";
		s += "Value: " + presentValue + "\n";
		s += "Checked: " + checked + "\n";
		
		return s;
	}

	/**
	 * @return the disabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the editText
	 */
	public EditText getEditText() {
		return editText;
	}

	/**
	 * @param editText the editText to set
	 */
	public void setEditText(EditText editText) {
		this.editText = editText;
	}

	/**
	 * @return the checkBox
	 */
	public CheckBox getCheckBox() {
		return checkBox;
	}

	/**
	 * @param checkBox the checkBox to set
	 */
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
	
}
