/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.ConversionParameter;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.ProcessingParameters;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Anders
 *
 */
public class ConvertFragment extends Fragment {
	
	private static final String PARAMETERS_RAW_PROFILE = "Parameters RAW -> Profile";
	private static final String RAW_CONVERSION = "raw";
	private static final String FILES = "files";
	private static final String CONVERSION_TYPE = "type";
	private ListView convertListView;
	private ArrayList<ConversionParameter> parameters;
	private ArrayList<String> convertFields;
//	private ArrayList<String> hintList;
//	private ArrayList<String> fieldTypeMap;
//	private ArrayList<Integer> spinnerSelection;
//	private HashMap <String, String> parameterMap;
//	private HashMap <String, Boolean> checkedFields;
//	private HashMap<String, String[]> valueList;
//	private HashMap<String, String> choosenConversionParameters;
	private Button convertButton;
	private String type;
	private ArrayList<GeneFile> files;
	private TextView convertLabel;
	public boolean taskCompleted;

	/**
	 * 
	 */
	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert, container, false);
		
		convertListView = (ListView) v.findViewById(R.id.listview_convert);
		convertLabel = (TextView) v.findViewById(R.id.lbl_convert_header);
		
		return v;
	}

	/**
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createLists();
		retrieveData();	
	}


	/**
	 * 
	 */
	private void retrieveData() {
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			type = (String) bundle.get(CONVERSION_TYPE);
			files = (ArrayList<GeneFile>) bundle.get(FILES);
		}
	}


	/**
	 * 
	 */
	private void createLists() {
		parameters = new ArrayList<ConversionParameter>();
		setupRawParameters();
		
		
//		convertFields = new ArrayList<String>();
//		hintList = new ArrayList<String>();
//		parameterMap = new HashMap<String, String>();
//		checkedFields = new HashMap<String, Boolean>();
//		fieldTypeMap = new ArrayList<String>();
//		valueList = new HashMap<String, String[]>();
//		choosenConversionParameters = new HashMap<String, String>();
	}
	
	/**
	 * 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (type != null && type.equals(RAW_CONVERSION)) {
			setupRawParameters();
		}

		createConvertButton();
		setupConvert();
	}

	/**
	 * 
	 */
	private void setupConvert() {
		
		
		
//		for (String s : convertFields) {
//			checkedFields.put(s, false);
//			parameterMap.put(s, null);
//		}
		
		convertListView.setAdapter(new ConvertAdapter(convertFields));
	}


	/**
	 * 
	 */
	private void createConvertButton() {
		convertButton = (Button) getActivity().findViewById(R.id.btn_convert_convert);
		convertButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Log.d("smurf",
//						"--------------------------------------\n"
//								+ "FieldNames:\n" + convertFields
//								+ "\n" + "CheckedList:\n"
//								+ checkedFields
//								+ "\n" + "Parameters:\n"
//								+ parameterMap
//								+ "\n-------------------------------------------------\n");
				ProcessingParameters parameters = new ProcessingParameters();
//				for (String s : convertFields) {
//					parameters.addParameter(parameterMap.get(s));
//				}
				
				new ConvertTask().execute(parameters);
				
				
			}

			
		});
	}
	
	
	/**
	 * 
	 */
	private void conversionCheck() {
		if (taskCompleted) {
			toastUser("Conversion started");
		} else {
			toastUser("Conversion malfunction");
		}
		taskCompleted = false;
	}
	
	protected void toastUser(String string) {
		Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();
		
	}

	/**
	 * 
	 */
	private void setupRawParameters() {
		int i = 0;
		
		ArrayList<String> yesNo = new ArrayList<String>();
		yesNo.add("Yes");
		yesNo.add("No");
		
		ArrayList<String> gene = new ArrayList<String>();
		gene.add("Smurf ver. 1.2.4");
		gene.add("Gargamel ver. 1.2.4");
		gene.add("Mufflon ver. 1.2.5");
		gene.add("Minotaur ver. 1.3.4");
		gene.add("Harpy ver. 2.3.4");
		gene.add("Smaug ver. 4.32.2");
		
		convertFields.add("Bowtie parameters");
		convertFields.add("Genome version");
		convertFields.add("SAM to GFF");
		convertFields.add("GFF to SGR");
		convertFields.add("Smoothing");
		convertFields.add("Step size");
		convertFields.add("Ratio Calculation");
		convertFields.add("Ratio");
		convertFields.add("Ratio smoothing");
		
		for (String s : convertFields) {
			ConversionParameter temp = new ConversionParameter(s);
			if (i == 1) {
				temp.setParameterType("spinner");
				temp.setValues(gene);
			} else if (i == 2 || i == 3) {
				temp.setParameterType("spinner");
				temp.setValues(yesNo);
			} else {
				temp.setParameterType("freetext");
			}
			parameters.add(temp);
			i++;
		}
		
//		for (int i = 0; i < 9; i++) {
//			choosenConversionParameters.put(convertFields.get(i), "");
//		}
//		
//		
//		
//		fieldTypeMap.add("freetext");
//		fieldTypeMap.add("spinner");
//		fieldTypeMap.add("spinner");
//		fieldTypeMap.add("spinner");
//		fieldTypeMap.add("freetext");
//		fieldTypeMap.add("freetext");
//		fieldTypeMap.add("freetext");
//		fieldTypeMap.add("freetext");
//		fieldTypeMap.add("freetext");
//		
//		hintList.add("1");
//		hintList.add("2");
//		hintList.add("3");
//		hintList.add("4");
//		hintList.add("5");
//		hintList.add("6");
//		hintList.add("7");
//		hintList.add("8");
//		hintList.add("9");
//		
//		
//		valueList.put("Genome version", new String[]{"d_melanogaster_fb5_22", "smurf ver.032615", "Gargamel ver. 2.832"});
//		valueList.put("SAM to GFF", new String[]{"Yes", "No"});
//		valueList.put("GFF to SGR", new String[]{"Yes", "No"});
		
		convertLabel.setText(PARAMETERS_RAW_PROFILE);
	}
	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private static class ConvertViewHolder {
		TextView title;
		EditText parameter;
		CheckBox checkBox;
		Spinner spinner;
		int spinnerSelection;
	}
	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private class ConvertAdapter extends ArrayAdapter<String> {

		/**
		 * 
		 * @param convertFields
		 */
		public ConvertAdapter(ArrayList<String> convertFields) {
			super(getActivity(), 0, convertFields);
		}
		
		/**
		 * 
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ConvertViewHolder viewHolder = null;
			ConvertTextWatch textWatcher = null;
			int resource;
			boolean fieldIsFreetext = parameters.get(position).getParameterType().equals("freetext");
			String temp;

			if (fieldIsFreetext) {
				resource = R.layout.conversion_item;
			} else {
				resource = R.layout.conversion_spinner_item;
			}
			
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(resource, null);
				
				viewHolder = new ConvertViewHolder();
				viewHolder.title = (TextView) convertView.findViewById(R.id.lbl_conversion_item);
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_conversion_item);
				viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int pos = (Integer) buttonView.getTag();
						parameters.get(pos).setChecked(isChecked);
					}
				});
				
				if (fieldIsFreetext) {
					textWatcher = new ConvertTextWatch();
					viewHolder.parameter = (EditText) convertView.findViewById(R.id.edit_conversion_item);
					viewHolder.parameter.addTextChangedListener(textWatcher);
					viewHolder.parameter.setTag(textWatcher);
				} else {
					viewHolder.spinner = (Spinner) convertView.findViewById(R.id.spinner_conversion_item);
					viewHolder.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							int pos = (Integer) parent.getTag();
							ConversionParameter c = parameters.get(pos);
							
							c.setSelectedSpinnerVal(position);
							c.setPresentValue(parent.getSelectedItem().toString());
							
									choosenConversionParameters.put(
											convertFields.get(pos),
											parent.getItemAtPosition(position)
													.toString());
							
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub
							
						}
						
					});
				}
				
				
				
				convertView.setTag(viewHolder);
				convertView.setTag(R.id.lbl_conversion_item, viewHolder.title);
				convertView.setTag(R.id.edit_conversion_item, viewHolder.parameter);
				convertView.setTag(R.id.checkbox_conversion_item, viewHolder.checkBox);
				
			}else {
				viewHolder = (ConvertViewHolder) convertView.getTag();
			}
			
			temp = convertFields.get(position);
			viewHolder.checkBox.setTag(position);
			viewHolder.spinner.setTag(position);
			textWatcher = (ConvertTextWatch) viewHolder.parameter.getTag();
			textWatcher.updatePosition(position);
			viewHolder.title.setText(temp);
			viewHolder.parameter.setHint(hintList.get(position));
			viewHolder.parameter.setText(parameterMap.get(temp));
			viewHolder.checkBox.setChecked(checkedFields.get(temp).booleanValue());
			viewHolder.spinner.setSelection(position);
			
			return convertView;
		}
	}
	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private class ConvertTextWatch implements TextWatcher {
		private int position;
		
		/**
		 * 
		 * @param position
		 */
		public void updatePosition(int position) {
			this.position = position;
		}
		
		/**
		 * 
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
		
		/**
		 * 
		 */
		@Override
		public void afterTextChanged(Editable s) {
			String key = convertFields.get(position);
			parameterMap.put(key, s.toString());
		}


		/**
		 * 
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
	}
	
	private class ConvertTask extends AsyncTask<ProcessingParameters, Void, Void> {
		
		@Override
		protected Void doInBackground(ProcessingParameters... params) {
			
			try {
				taskCompleted = ComHandler.rawToProfile(files.get(0), params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			conversionCheck();
		}
		
	}
	
}
