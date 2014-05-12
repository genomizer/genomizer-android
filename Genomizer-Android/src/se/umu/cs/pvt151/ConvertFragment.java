/**
 * 
 */
package se.umu.cs.pvt151;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	private ListView convertListView;
	private ArrayList<String> convertFields;
	private HashMap <String, String> parameterMap;
	private HashMap <String, Boolean> checkedFields;
	private Button convertButton;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		convertListView = (ListView) getActivity().findViewById(R.id.listview_convert);
		convertFields = new ArrayList<String>();
		parameterMap = new HashMap<String, String>();
		checkedFields = new HashMap<String, Boolean>();
		
		//TODO test
		setupTest();
		
		setup();
	}
	
	
	private void setupTest() {
		convertFields.add("A");
		convertFields.add("B");
		convertFields.add("C");
		convertFields.add("D");
		convertFields.add("E");
		convertFields.add("F");
		convertFields.add("G");
		convertFields.add("H");
	}


	private void setup() {
		
		convertButton = (Button) getActivity().findViewById(R.id.btn_convert_convert);
		convertButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("smurf",
						"--------------------------------------\n"
								+ convertFields
								+ "\n"
								+ checkedFields
								+ "\n"
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
			String temp;
			
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.conversion_item, null);
				
				viewHolder = new ConvertViewHolder();
				viewHolder.title = (TextView) convertView.findViewById(R.id.lbl_conversion_item);
				viewHolder.parameter = (EditText) convertView.findViewById(R.id.edit_conversion_item);
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
			viewHolder.title.setText(temp);
			viewHolder.parameter.setText(parameterMap.get(temp));
			viewHolder.checkBox.setChecked(checkedFields.get(temp).booleanValue());
			
			return convertView;
		}

		
		
	}
}
