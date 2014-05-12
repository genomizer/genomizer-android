package se.umu.cs.pvt151;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class WorkSpaceFragment extends Fragment {

	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
	private ListView listResults;

	private ArrayList<GeneFile> raw;
	private ArrayList<GeneFile> profile;
	private ArrayList<GeneFile> region;
	private ArrayList<GeneFile> results;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		raw = new ArrayList<GeneFile>();
		profile = new ArrayList<GeneFile>();
		region = new ArrayList<GeneFile>();
		results = new ArrayList<GeneFile>();
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_work_space, parent, false);
		
		TabHost tabs = (TabHost) v.findViewById(R.id.tabhost);
	    tabs.setup();
	    
	    tabs.addTab(tabs.newTabSpec("RAW"));

		listRaw = (ListView) v.findViewById(R.id.raw);
		listProfile = (ListView) v.findViewById(R.id.profile);
		listRegion = (ListView) v.findViewById(R.id.region);
		listResults = (ListView) v.findViewById(R.id.result);

		listRaw.setAdapter(new FileListAdapter(raw, "raw"));
		listProfile.setAdapter(new FileListAdapter(profile, "profile"));
		listRegion.setAdapter(new FileListAdapter(region, "region"));
		listResults.setAdapter(new FileListAdapter(results, "results"));

		return v;
	}


	/**
	 * Adapter used for listviews
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<GeneFile> {
		ArrayList<GeneFile> forShow = new ArrayList<GeneFile>();
		boolean[] selectedItem;
		String data;

		public FileListAdapter(ArrayList<GeneFile> files, String dataType) {
			super(getActivity(), 0, files);
			forShow = files;
			data = dataType;
			selectedItem = new boolean[files.size()];
			for(int i = 0; i<selectedItem.length; i++) {
				selectedItem[i] = false;
			}
		}


		public View getView(int position, View convertView, ViewGroup parent) {

			Context cont = getActivity();

			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_view_checkbox, null);
			}


			GeneFile file = getItem(position);

			if (file != null) {
				TextView textView = (TextView) view.findViewById(R.id.textView1);
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.textForBox);

				textView.setText(file.getName());

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						
					}
				});
			}

			return view;
		}
		
		
		public GeneFile getItem(int position) {
			return forShow.get(position);
		}
	}
}
