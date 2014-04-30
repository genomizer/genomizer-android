package se.umu.cs.pvt151;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchListFragment extends ListFragment {
	
	private String[] values = new String[] { "Experiment Id", "Pubmed Id", "Type of data",
	        "Species", "Genome release", "Cell-line", "Development stage", "Sex",
	        "Tissue", "Antigen name", "Antigen symbol", "Antibody" };

	private ArrayList<String> annotationList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		annotationList = new ArrayList<String>();
		for (String s : values) {
			annotationList.add(s);
		}
		
		SearchListAdapter adapter = new SearchListAdapter(annotationList);
		adapter.setNotifyOnChange(true);
		setListAdapter(adapter);
		
		if (annotationList.isEmpty()) {
			Toast.makeText(getActivity(), "No stores to display, add store by using the menu",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	static class searchViewHolder {
		protected EditText annotation;
		protected CheckBox markCheckBox;
	}
	
	private class SearchListAdapter extends ArrayAdapter<String> {
		
		public SearchListAdapter(ArrayList<String> storeList) {
			super(getActivity(), 0, storeList);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			searchViewHolder viewHolder = null;
			
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.searchlist_field, null);
				
				viewHolder = new searchViewHolder();
				viewHolder.annotation = (EditText) convertView.findViewById(R.id.txtf_search_hint);
				viewHolder.markCheckBox = (CheckBox) convertView.findViewById(R.id.check_search_hint);
				
				viewHolder.markCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int getPosition = (Integer) buttonView.getTag();
					}
				});
				convertView.setTag(viewHolder);
				convertView.setTag(R.id.txtf_search_hint, viewHolder.annotation);
				convertView.setTag(R.id.check_search_hint, viewHolder.markCheckBox);
			} else {
				viewHolder = (searchViewHolder) convertView.getTag();
			}
			
			viewHolder.markCheckBox.setTag(position);
			viewHolder.annotation.setHint(annotationList.get(position).toString());
			
			return convertView;
		}
	}
	
	
}
