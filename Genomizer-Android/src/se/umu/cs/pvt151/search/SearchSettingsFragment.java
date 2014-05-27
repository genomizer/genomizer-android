package se.umu.cs.pvt151.search;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.layout;
import se.umu.cs.pvt151.search_result.ExperimentListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * SearchSettingsFragment
 * Fragment used to display available
 * annotations in a list with a
 * check box next to each annotation.
 * Used to check and store in internal storage.
 * Default settings is also possible, and then
 * the two first annotations is shown in the
 * search result view.
 * @author Cecilia Lindmark
 */
public class SearchSettingsFragment extends Fragment {
	
	private ArrayList<String> annotations = new ArrayList<String>();
	private ListView list;
	private ArrayList<Boolean> forBoxChecks = new ArrayList<Boolean>();
	private Button saveButton;
	private Button defaultButton;
	private ArrayList<String> newSettings = new ArrayList<String>();
	private String setting;
	private String file = "SearchSettings.txt";
	private HashMap<String, String> searchResults = new HashMap<String, 
			String>();
	private String file2 = "DefaultSettings.txt";
	
	/**
	 * onCreate
	 * Retreives right values
	 * from previous activity.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		annotations = getActivity().getIntent().getExtras().getStringArrayList(
				"Annotations");
		searchResults = (HashMap<String, String>) getActivity().getIntent()
				.getExtras().getSerializable("searchMap");
	}
	
	/**
	 * onCreateView
	 * Inflates the view.
	 * Sets listeners to button to
	 * save set annotations and to the
	 * button used to set default settings.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_settings, parent,
				false);
		list = (ListView) v.findViewById(R.id.listView1);
		fillData();
		list.setAdapter(new SearchSettingAdapter(annotations));
		
		saveButton = (Button) v.findViewById(R.id.save_srcbtn);
		defaultButton = (Button) v.findViewById(R.id.default_btn);
		
		saveButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Adding: " + newSettings.toString(), 
						Toast.LENGTH_SHORT).show();
				saveSettings();
				Intent intent = new Intent(getActivity(), 
						ExperimentListActivity.class);
				intent.putStringArrayListExtra("Annotations", annotations);
				intent.putExtra("searchMap", searchResults);
				saveDefaultOrNot("false");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);				
			}
			
		});
		
		defaultButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), 
						ExperimentListActivity.class);
				intent.putStringArrayListExtra("Annotations", annotations);
				intent.putExtra("searchMap", searchResults);
				saveDefaultOrNot("true");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
			
		});
		
		return v;
	}
	
	/**
	 * Method used to save set annotations
	 * settings in internal storage
	 */
	private void saveSettings() {
		setting = "";
		Context cont = getActivity();
		getActivity().deleteFile(file);
				
		if(!newSettings.isEmpty()) {
			for(int i = 0; i < newSettings.size(); i++) {
				setting = setting + newSettings.get(i) + ";";
			}
		}
		
		try {
			FileOutputStream fos = cont.getApplicationContext().openFileOutput(
					file, Context.MODE_APPEND);
			fos.write(setting.getBytes());
			fos.close();
		
		} catch (FileNotFoundException e) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Could not save info: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Could not save info: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Method used to save information
	 * if default settings should be used or not
	 * @param defaultOrNot true if defaults settings
	 * is selected, else false
	 */
	private void saveDefaultOrNot(String defaultOrNot) {
		Context cont = getActivity();
		getActivity().deleteFile(file2);
		
		try {
			FileOutputStream fos = cont.getApplicationContext().openFileOutput(
					file2, Context.MODE_APPEND);
			fos.write(defaultOrNot.getBytes());
			fos.close();
		
		} catch (FileNotFoundException e) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Could not save info: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Could not save info: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * Method used to set array with
	 * false values, used to handle check box
	 * checks on scroll. 
	 */
	private void fillData() {
		for(int i = 0; i < annotations.size(); i++) {
			forBoxChecks.add(false);
		}
	}
	
	/**
	 * Used to create holder for
	 * values used in getview
	 *
	 */
	static class listViewHolder {
		protected TextView annotationInfo;
		protected CheckBox annotationCheckBox;
		protected ArrayList<String> forBoxChecks;
	}
	
	/**
	 * Adapter used to handle selection of
	 * options in the list view. Sets onclick
	 * listener to list view to detect what happens when
	 * user checks check boxes. 
	 * @author Cecilia Lindmark
	 *
	 */
	private class SearchSettingAdapter extends ArrayAdapter<String> {
		ArrayList<String> forShow = new ArrayList<String>();
		boolean[] selectedItem;

		public SearchSettingAdapter(ArrayList<String> fileInfo) {
				super(getActivity(), 0, fileInfo);
				forShow = fileInfo;
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
			
			viewHolder.annotationInfo = (TextView) convertView.findViewById(
					R.id.textView1);
			viewHolder.annotationCheckBox = (CheckBox) convertView.findViewById(
					R.id.textForBox);
			final listViewHolder buttonHolder = viewHolder;
			viewHolder.annotationCheckBox.setOnClickListener(
					new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(v.isShown()) {
						int getPosition = (Integer) v.getTag();
						
						if(!forBoxChecks.isEmpty()) {
							//Adding values if checkbox is checked
							forBoxChecks.set(getPosition,((
									CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((
									CompoundButton) v).isChecked();
							
						} else {
							forBoxChecks.add(getPosition, ((
									CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((
									CompoundButton) v).isChecked();
						} 
					} 
					
					if(buttonHolder.annotationCheckBox.isChecked()) {
						newSettings.add(annotations.get(getPos));
					} else if(!buttonHolder.annotationCheckBox.isChecked()) {
						newSettings.remove(annotations.get(getPos));
					}
				}
				
			});
			
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.textView1, viewHolder.annotationInfo);
			convertView.setTag(R.id.textForBox, viewHolder.annotationCheckBox);
			
			viewHolder.annotationCheckBox.setTag(position);
			if(!forBoxChecks.isEmpty()) {
				viewHolder.annotationCheckBox.setChecked(
						forBoxChecks.get(position));
			}
			viewHolder.annotationInfo.setText(forShow.get(position));
		
			return convertView;
		}
	}
}
