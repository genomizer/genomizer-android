package se.umu.cs.pvt151;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class SearchSettingsFragment extends Fragment {
	
	private ArrayList<String> annotations = new ArrayList<String>();
	private ListView list;
	private ArrayList<Boolean> forBoxChecks = new ArrayList<Boolean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		annotations = getActivity().getIntent().getExtras().getStringArrayList("Annotations");
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_settings, parent, false);
		list = (ListView) v.findViewById(R.id.listView1);
		fillData();
		list.setAdapter(new SearchSettingAdapter(annotations, "hello"));
		
		return v;
	}
	
	/**
	 * Temp method to fill array with values
	 */
	private void fillData() {
		for(int i = 0; i < annotations.size(); i++) {
			forBoxChecks.add(false);
		}
	}
	
	static class listViewHolder {
		protected TextView annotationInfo;
		protected CheckBox annotationCheckBox;
		protected ArrayList<String> forBoxChecks;
	}
	
	private class SearchSettingAdapter extends ArrayAdapter<String> {
		//TODO: Use same adapter for all three listviews, or need to make 3 differents ones?
		ArrayList<String> forShow = new ArrayList<String>();
		boolean[] selectedItem;
		String data;

		public SearchSettingAdapter(ArrayList<String> fileInfo, String dataType) {
				super(getActivity(), 0, fileInfo);
				forShow = fileInfo;
				data = dataType;
				selectedItem = new boolean[fileInfo.size()];
				for(int i = 0; i<selectedItem.length; i++) {
					selectedItem[i] = false;
				}
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			listViewHolder viewHolder = null;
			final int getPos = position;
		
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_view_checkbox, null);
				viewHolder = new listViewHolder();
				
			} else {
				viewHolder = (listViewHolder) convertView.getTag();
				
			}
			
			viewHolder.annotationInfo = (TextView) convertView.findViewById(R.id.textView1);
			viewHolder.annotationCheckBox = (CheckBox) convertView.findViewById(R.id.textForBox);
			final listViewHolder buttonHolder = viewHolder;
			viewHolder.annotationCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(v.isShown()) {
						int getPosition = (Integer) v.getTag();
						
						if(!forBoxChecks.isEmpty()) {
							//Adding values if checkbox is checked
							forBoxChecks.set(getPosition,((CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((CompoundButton) v).isChecked();
							
						} else {
							forBoxChecks.add(getPosition, ((CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((CompoundButton) v).isChecked();
						} 
					} 
					
					if(buttonHolder.annotationCheckBox.isChecked()) {
						
					} else if(!buttonHolder.annotationCheckBox.isChecked()) {
						
					}
				}
				
			});
			
			
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.textView1, viewHolder.annotationInfo);
			convertView.setTag(R.id.textForBox, viewHolder.annotationCheckBox);
			
			viewHolder.annotationCheckBox.setTag(position);
			if(!forBoxChecks.isEmpty()) {
				viewHolder.annotationCheckBox.setChecked(forBoxChecks.get(position));
			}
			viewHolder.annotationInfo.setText(forShow.get(position));
		
			return convertView;
		}
	}
}
