/**
 * 
 */
package se.umu.cs.pvt151;

import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.model.GeneFile;
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

/**
 * @author Anders
 *
 */
public class ConvertFragment extends Fragment {
	
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


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		convertFields = new ArrayList<String>();
		hintList = new ArrayList<String>();
		parameterMap = new HashMap<String, String>();
		checkedFields = new HashMap<String, Boolean>();
		
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			type = (String) bundle.get(CONVERSION_TYPE);
			files = (ArrayList<GeneFile>) bundle.get(FILES);
		}	
	}
	
	

	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert, container, false);
		
		convertListView = (ListView) v.findViewById(R.id.listview_convert);
		
		
		return v;
	};
	
	private void setupTest() {
		convertFields.add("A");
		convertFields.add("B");
		convertFields.add("C");
		convertFields.add("D");
		convertFields.add("E");
		convertFields.add("F");
		convertFields.add("G");
		convertFields.add("H");
		convertFields.add("I");
		convertFields.add("J");
		convertFields.add("K");
		convertFields.add("L");
		
	}


	private void setup() {
		
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
				
			}
		});
		
		for (String s : convertFields) {
			checkedFields.put(s, false);
			parameterMap.put(s, null);
		}
		
		convertListView.setAdapter(new ConvertAdapter(convertFields));
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// TODO test
//		setupTest();
		if (type != null && type.equals(RAW_CONVERSION)) {
			setupRawParameters();
		}
		setup();
	}
	
	private void setupRawParameters() {
		convertFields.add("Bowtie parameters");
		convertFields.add("Genome index reference");
		convertFields.add("Smoothing");
		convertFields.add("Step size");
		
		hintList.add("-a -m l --best -p 10 -v 2 -q -S");
		hintList.add("d_melanogaster_fb5_22");
		hintList.add("10 1 5 0 1");
		hintList.add("y standard, 10 standard");
	}

	private static class ConvertViewHolder {
		TextView title;
		EditText parameter;
		CheckBox checkBox;
	}
	
	private class ConvertAdapter extends ArrayAdapter<String> {


		public ConvertAdapter(ArrayList<String> convertFields) {
			super(getActivity(), 0, convertFields);
		}
		
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
	
	private class ConvertTextWatch implements TextWatcher {
		
		private int position;
		private int test = 0;

		public ConvertTextWatch() {
			
		}
		
		public void updatePosition(int position) {
			this.position = position;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			String key = convertFields.get(position);
			parameterMap.put(key, s.toString());
			Log.d("smurf", "Test: " + test);
			test++;
		}
		
	}
}
