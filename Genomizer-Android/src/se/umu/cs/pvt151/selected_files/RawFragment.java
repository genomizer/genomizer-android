package se.umu.cs.pvt151.selected_files;

import java.util.ArrayList;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.processing.ConverterActivity;
import android.app.AlertDialog;
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

/**
 * This fragment is visualizes a list of GeneFiles.
 * The files may be marked and converted/deleted by the user.
 * 
 * @author Rickard dv12rhm
 *
 */
public class RawFragment extends Fragment {

	private ListView listRaw;

	private ArrayList<GeneFile> raw;
	private ArrayList<GeneFile> selectedRaw;

	private FileListAdapter adapter;

	private Button convertRawButton;
	private Button removeRawButton;


	/**
	 * Called when the RawFragment is created.
	 * Gets the raw files from DataStorage.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		raw = DataStorage.getFileList("raw");
		selectedRaw = new ArrayList<GeneFile>();
	}


	/**
	 * Inflates the view of the fragment, including 
	 * the listview and buttons.
	 * 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_raw, parent, false);
		listRaw = (ListView) v.findViewById(R.id.raw);
		adapter = new FileListAdapter(raw, "raw");
		listRaw.setAdapter(adapter);
		setConvertButtonListener(v);
		setRemoveButtonListener(v);

		return v;
	}


	/**
	 * This method is called when the fragments activity is created.
	 * The selected files is loaded from DataStorage.
	 * 
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		selectedRaw = DataStorage.getFileList("rawSelected");
		adapter.notifyDataSetChanged();
		setButtonsStatus();
	}


	/**
	 * Implements a buttonlistener for the convert button.
	 * 
	 * @param v
	 */
	private void setConvertButtonListener(View v) {
		convertRawButton = (Button) v.findViewById(R.id.convert_raw_button);

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
	}


	/**
	 * Implements a buttonlistener for the remove button.
	 * 
	 * @param v
	 */
	private void setRemoveButtonListener(View v) {
		removeRawButton = (Button) v.findViewById(R.id.remove_raw_button);

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


	/**
	 * Sets the buttons status based on if there is any marked files
	 * or not.
	 * 
	 */
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
	 * Adapter used for listviews. Its purpose is to store and view
	 * GeneFile objects graphically.
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<GeneFile> {

		//The files to be visualized in the listview
		ArrayList<GeneFile> forShow = new ArrayList<GeneFile>();
		

		public FileListAdapter(ArrayList<GeneFile> files, String dataType) {
			super(getActivity(), 0, files);
			forShow = files;
		}


		/**
		 * Returns the view of an object in the listview at specified position.
		 * This method is called by the system to build and visualize the
		 * listview.
		 * 
		 */
		public View getView(int position, View view, ViewGroup parent) {
			Context cont = getActivity();
			
			final int pos = position;

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
				
				//When a checkbox is clicked
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int position = (Integer) buttonView.getTag();

						if (isChecked) {
							if (!selectedRaw.contains(forShow.get(position))) {
								selectedRaw.add(forShow.get(position));
							}
						} else {
							selectedRaw.remove(forShow.get(position));
						}
						DataStorage.appendFileList("rawSelected", selectedRaw);
						setButtonsStatus();
					}
				});

				if (selectedRaw.contains(file)) {
					if (!checkBox.isChecked()) {
						checkBox.toggle();
					}
				} else { 
					if (checkBox.isChecked()) {
						checkBox.toggle();
					}
				}
				
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						displayExtraFileInfo(forShow.get(pos));
					}
				});
				
				//Set the view tags
				view.setTag(textView);
				view.setTag(checkBox);
			}
			return view;
		}

		
		public boolean hasStableIds() {
			return true;
		}
		

		/**
		 * Gets the object at specified position.
		 * 
		 * @return GeneFile
		 */
		public GeneFile getItem(int position) {
			return forShow.get(position);
		}
	}


	/**
	 * Method used to create a dialog window with
	 * more information about a file when text view is
	 * clicked
	 * @param file that extra information will
	 * be received from. 
	 */
	private void displayExtraFileInfo(GeneFile file) {
		AlertDialog.Builder build = new AlertDialog.Builder(getActivity());		
		build.setTitle(file.getName());
		build.setMessage(file.getFileInfo());
		build.setNeutralButton("OK", null);
		build.show();
	}
}
