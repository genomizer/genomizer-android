package se.umu.cs.pvt151;

import java.util.ArrayList;

import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class RawFragment extends Fragment {

	private ListView listRaw;

	private ArrayList<GeneFile> raw;
	private ArrayList<GeneFile> selectedRaw;

	private FileListAdapter adapter;

	private Button convertRawButton;
	private Button removeRawButton;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		raw = DataStorage.getFileList("raw");
		selectedRaw = new ArrayList<GeneFile>();
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_raw, parent, false);
		listRaw = (ListView) v.findViewById(R.id.raw);
		adapter = new FileListAdapter(raw, "raw");
		listRaw.setAdapter(adapter);
		setButtonListeners(v);

		return v;
	}


	private void setButtonListeners(View v) {
		convertRawButton = (Button) v.findViewById(R.id.convert_raw_button);
		removeRawButton = (Button) v.findViewById(R.id.remove_raw_button);

		convertRawButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectedRaw.size() > 0) {
					Intent intent = new Intent(getActivity(),
							ConverterActivity.class);

					intent.putExtra("type", "raw");
					intent.putExtra("files", selectedRaw);

					startActivity(intent);
				} 
			}
		});

		removeRawButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < selectedRaw.size(); i++) {
					GeneFile file = selectedRaw.get(i);
					adapter.remove(file);
					raw.remove(file);
				}
				selectedRaw.clear();
				adapter.notifyDataSetChanged();
				setButtonsStatus();
			}
		});
		setButtonsStatus();
	}
	
	
	private void setButtonsStatus() {
		if (selectedRaw.size() > 0) {
			convertRawButton.setEnabled(true);
			removeRawButton.setEnabled(true);
		} else {
			convertRawButton.setEnabled(false);
			removeRawButton.setEnabled(false);
		}
	}


	/**
	 * Adapter used for listviews
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<GeneFile> {
		ArrayList<GeneFile> forShow = new ArrayList<GeneFile>();
		boolean[] selectedItem;

		public FileListAdapter(ArrayList<GeneFile> files, String dataType) {
			super(getActivity(), 0, files);
			forShow = files;
			if (files != null) {
				selectedItem = new boolean[files.size()];
				for(int i = 0; i<selectedItem.length; i++) {
					selectedItem[i] = false;
				}
			}
		}


		public View getView(int position, View view, ViewGroup parent) {
			Context cont = getActivity();

			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_view_checkbox, null);
			}

			GeneFile file = getItem(position);

			if (file != null) {
				TextView textView = (TextView) view.findViewById(R.id.textView1);
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.textForBox);

				checkBox.setTag(position);
				textView.setText(file.getName());

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						int position = (Integer) buttonView.getTag();

						if (isChecked) {
							selectedRaw.add(forShow.get(position));
						} else {
							selectedRaw.remove(forShow.get(position));
						}

						setButtonsStatus();
					}
				});
				
				if (!selectedRaw.contains(file)) {
					if (checkBox.isChecked()) {
						checkBox.toggle();
					}
				}

				view.setTag(textView);
				view.setTag(checkBox);
			}
			return view;
		}


		@Override
		public boolean hasStableIds() {
			return true;
		}


		public GeneFile getItem(int position) {
			return forShow.get(position);
		}
	}
}
