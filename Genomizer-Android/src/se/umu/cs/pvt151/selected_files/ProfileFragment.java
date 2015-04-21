package se.umu.cs.pvt151.selected_files;

import java.util.ArrayList;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import android.app.AlertDialog;
import android.content.Context;
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
 * This fragment visualizes a list of GeneFiles.
 * The files may be marked and deleted by the user.
 * 
 * @author Rickard dv12rhm
 *
 */
public class ProfileFragment extends Fragment {

	private ListView listProfile;

	private ArrayList<GeneFile> profile;
	private ArrayList<GeneFile> selectedProfile;

	private FileListAdapter adapter;

	private Button removeButton;


	/**
	 * Called when the ProfileFragment is created.
	 * Gets the profile files from DataStorage.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		profile = DataStorage.getFileList("profile");
		selectedProfile = new ArrayList<GeneFile>();
	}


	/**
	 * Inflates the view of the fragment, including 
	 * the listview and buttons.
	 * 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile, parent, false);
		listProfile = (ListView) v.findViewById(R.id.profile);
		adapter = new FileListAdapter(profile, "profile");
		listProfile.setAdapter(adapter);
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
		selectedProfile = DataStorage.getFileList("profileSelected");
		adapter.notifyDataSetChanged();
		setButtonsStatus();
	}


	/**
	 * Implements a buttonlistener for the remove button.
	 * 
	 * @param v
	 */
	private void setRemoveButtonListener(View v) {
		removeButton = (Button) v.findViewById(R.id.remove_profile_button);

		removeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < selectedProfile.size(); i++) {
					adapter.remove(selectedProfile.get(i));
					profile.remove(selectedProfile.get(i));
				}
				selectedProfile.clear();
				adapter.notifyDataSetChanged();
				setButtonsStatus();
			}
		});
		setButtonsStatus();
	}


	/**
	 * Sets the status on the remove button based on
	 * if there are any marked checkboxes or not.
	 * 
	 */
	private void setButtonsStatus() {
		if (selectedProfile.size() > 0) {
			removeButton.setEnabled(true);
		} else {
			removeButton.setEnabled(false);
		}
	}

	/**
	 * Method used to create a dialog window with
	 * more information about a file.
	 * @param file that extra information will
	 * be received from. 
	 */
	private void displayExtraFileInfo(GeneFile file) {
		AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
		String moreInfo;
		//Information to be displayed in dialogue about the file
		moreInfo = "Exp id: " + file.getExpId() + "\n" + "Type: " 
				+ file.getType() + "\n" + "Author: " + file.getAuthor()
				+ "\n" + "Uploaded by: " + file.getUploadedBy() + "\n" 
				+ "Date: " + file.getDate() + "\n" + "GR Version: "
				+ file.getGrVersion() + "\n" + "Path: " + file.getPath();

		build.setTitle(file.getName());
		build.setMessage(moreInfo);
		build.setNeutralButton("OK", null);
		build.show();
	}
	
	
	
	
	/**
	 * Adapter used for listviews. Its purpose is to store and view
	 * GeneFile objects graphically.
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<GeneFile> {
		
		//The files to be visualized in the listview
		ArrayList<GeneFile> forShow = new ArrayList<GeneFile>();
		
		//TODO dataType används inte!
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
				//TODO privat klass istället för onCheckedChangeListener?
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						int position = (Integer) buttonView.getTag();

						if (isChecked) {
							if (!selectedProfile.contains(forShow.get(position))) {
								selectedProfile.add(forShow.get(position));
							}
						} else {
							selectedProfile.remove(forShow.get(position));
						}
						DataStorage.appendFileList("profileSelected", selectedProfile);
						setButtonsStatus();
					}
				});

				if (selectedProfile.contains(file)) {
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
	
}
