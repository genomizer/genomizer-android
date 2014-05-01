package se.umu.cs.pvt151;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchListFragment extends ListFragment {
	

	private ArrayList<String> annotationList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		populateAnnotation();
		
		
	}

	/**
	 * Populates the annotation fields while there is no direct uplonk to the
	 * server, should be removed and replaced after the server can respond with
	 * actual annotations.
	 */
	private void populateAnnotation() {
		String[] values = new String[] { "Experiment Id", "Pubmed Id",
				"Type of data", "Species", "Genome release", "Cell-line",
				"Development stage", "Sex", "Tissue", "Antigen name",
				"Antigen symbol", "Antibody" };
		annotationList = new ArrayList<String>();
		for (String s : values) {
			annotationList.add(s);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListView listView = getListView();
		View footer = getActivity().getLayoutInflater().inflate(
				R.layout.searchlist_footer, null);
		Button searchButton = (Button) footer.findViewById(R.id.btn_search_footer);
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO remove testprint
				Log.d("smurf", "Search....");
				
			}
		});
		
		
		listView.addFooterView(footer);
		Log.d("smurf", "added footer");
		
		SearchListAdapter adapter = new SearchListAdapter(annotationList);
		adapter.setNotifyOnChange(true);
		setListAdapter(adapter);

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
