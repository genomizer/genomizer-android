/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import se.umu.cs.pvt151.com.ComHandler;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
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
	private ArrayList<String> convertFields;
	private ArrayList<String> hintList;
	private HashMap <String, String> parameterMap;
	private HashMap <String, Boolean> checkedFields;
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
		convertFields = new ArrayList<String>();
		hintList = new ArrayList<String>();
		parameterMap = new HashMap<String, String>();
		checkedFields = new HashMap<String, Boolean>();
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
		
		for (String s : convertFields) {
			checkedFields.put(s, false);
			parameterMap.put(s, null);
		}
		
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
				Log.d("smurf",
						"--------------------------------------\n"
								+ "FieldNames:\n" + convertFields
								+ "\n" + "CheckedList:\n"
								+ checkedFields
								+ "\n" + "Parameters:\n"
								+ parameterMap
								+ "\n-------------------------------------------------\n");
				ProcessingParameters parameters = new ProcessingParameters();
				for (String s : convertFields) {
					parameters.addParameter(parameterMap.get(s));
				}
				
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
		convertFields.add("Bowtie parameters");
		convertFields.add("Genome version");
		convertFields.add("SAM to GFF");
		convertFields.add("GFF to SGR");
		convertFields.add("Smoothing");
		convertFields.add("Step size");
		convertFields.add("Ratio Calculation");
		convertFields.add("Ratio");
		convertFields.add("Ratio smoothing");
		
		
		hintList.add("1");
		hintList.add("2");
		hintList.add("3");
		hintList.add("4");
		hintList.add("5");
		hintList.add("6");
		hintList.add("7");
		hintList.add("8");
		hintList.add("9");
		
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
			String temp;
			
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.conversion_item, null);
				
				viewHolder = new ConvertViewHolder();
				textWatcher = new ConvertTextWatch();
				viewHolder.title = (TextView) convertView.findViewById(R.id.lbl_conversion_item);
				viewHolder.parameter = (EditText) convertView.findViewById(R.id.edit_conversion_item);
				viewHolder.parameter.addTextChangedListener(textWatcher);
				viewHolder.parameter.setTag(textWatcher);
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_conversion_item);
				viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int pos = (Integer) buttonView.getTag();
						String key = convertFields.get(pos);
						checkedFields.put(key, isChecked);
					}
				});
				
				convertView.setTag(viewHolder);
				convertView.setTag(R.id.lbl_conversion_item, viewHolder.title);
				convertView.setTag(R.id.edit_conversion_item, viewHolder.parameter);
				convertView.setTag(R.id.checkbox_conversion_item, viewHolder.checkBox);
				
			}else {
				viewHolder = (ConvertViewHolder) convertView.getTag();
			}
			
			temp = convertFields.get(position);
			viewHolder.checkBox.setTag(position);
			textWatcher = (ConvertTextWatch) viewHolder.parameter.getTag();
			textWatcher.updatePosition(position);
			viewHolder.title.setText(temp);
			viewHolder.parameter.setHint(hintList.get(position));
			viewHolder.parameter.setText(parameterMap.get(temp));
			viewHolder.checkBox.setChecked(checkedFields.get(temp).booleanValue());
			
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
