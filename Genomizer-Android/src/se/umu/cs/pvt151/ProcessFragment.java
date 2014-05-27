package se.umu.cs.pvt151;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Process;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProcessFragment extends Fragment {

	private static final String DOWNLOADING_PROCESSING_INFORMATION = "Downloading processing information";
	private ListView processList;
	private Button refreshButton;
	private ArrayList<Process> processes;
	private ProgressDialog loadScreen;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		processes = new ArrayList<Process>();
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_process, parent, false);
		processList = (ListView) v.findViewById(R.id.processList);
		refreshButton = (Button) v.findViewById(R.id.process_button);
		
		refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processes.clear();
				showLoadScreen(DOWNLOADING_PROCESSING_INFORMATION);
				new ProcessTask().execute();
			}
		});
		return v;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showLoadScreen(DOWNLOADING_PROCESSING_INFORMATION);
		new ProcessTask().execute();
	}


	/**
	 * Adapter used for listviews. Creates a list of Process objects.
	 *
	 */
	private class ProcessListAdapter extends ArrayAdapter<Process> {
		ArrayList<Process> forShow = new ArrayList<Process>();
		boolean[] selectedItem;

		public ProcessListAdapter(ArrayList<Process> processes) {
			super(getActivity(), 0, processes);
			forShow = processes;

			if (processes != null) {
				selectedItem = new boolean[processes.size()];
				for(int i = 0; i<selectedItem.length; i++) {
					selectedItem[i] = false;
				}
			}
		}


		public View getView(int position, View view, ViewGroup parent) {
			Context cont = getActivity();
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_view_process, null);
			}
			Process process = getItem(position);

			if (process != null) {
				TextView expNameView = (TextView) view.findViewById(R.id.expNameTextView);
				TextView authorView = (TextView) view.findViewById(R.id.authorTextView);
				TextView timeAddedView = (TextView) view.findViewById(R.id.timeAddedTextView);
				TextView timeStartedView = (TextView) view.findViewById(R.id.timeStartedTextView);
				TextView timeFinishedView = (TextView) view.findViewById(R.id.timeFinishedTextView);
				TextView statusView = (TextView) view.findViewById(R.id.statusTextView);

				expNameView.setText(process.getExperimentName());
				authorView.setText(process.getAuthor());
				timeAddedView.setText(timeInterpreter(process.getTimeAdded()));
				timeStartedView.setText(timeInterpreter(process.getTimeStarted()));
				timeFinishedView.setText(timeInterpreter(process.getTimeFinnished()));
				
				statusView.setText(process.getStatus());
				setStatusColor(statusView);

				view.setTag(expNameView);
				view.setTag(authorView);
				view.setTag(timeAddedView);
				view.setTag(timeStartedView);
				view.setTag(timeFinishedView);
				view.setTag(statusView);
			}
			return view;
		}


		@Override
		public boolean hasStableIds() {
			return true;
		}


		public Process getItem(int position) {
			return forShow.get(position);
		}
	}


	/**
	 * This class extends AsyncTask, its only purpose is to get the status of
	 * any number of processes from a server and save it in an ArrayList.
	 * 
	 * @author Rickard
	 *
	 */
	private class ProcessTask extends AsyncTask<Void, Void, Void> {

		private IOException exception;

		@Override
		protected Void doInBackground(Void... params) {

			try {				
				processes = ComHandler.getProcesses();

			} catch (IOException e) {
				exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			loadScreen.dismiss();
			if (exception == null) {
				processList.setAdapter(new ProcessListAdapter(processes));
			} else {
				SingleFragmentActivity act = (SingleFragmentActivity) getActivity();
				act.relogin();
				exception = null;
			}	
		}
	}

	
	/**
	 * Converts a long variable from seconds 
	 * counted from 1 january 1970 to a date and returns it
	 * as a String.
	 * 
	 * @param seconds
	 * @return
	 */
	private String timeInterpreter(long seconds) {
		if(seconds == 0) {
			return "Pending...";
		}
		
		Date date = new Date(seconds);		
        return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, 
        		SimpleDateFormat.SHORT, Locale.ENGLISH).format(date);
	}
	
	
	/**
	 * Sets the color of the current process status.
	 * 
	 * @param statusView
	 */
	private void setStatusColor(TextView statusView) {
		String status = (String) statusView.getText();
		
		if (((String)status).contains("Crashed")) {
			statusView.setTextColor(Color.RED);
		} else if (status.contains("Finished")) {
			statusView.setTextColor(Color.GREEN);
		} else if (status.contains("Started")) {
			statusView.setTextColor(Color.CYAN);
		} else if (status.contains("Waiting")) {
			statusView.setTextColor(Color.BLUE);
		}
	}
	
	/**
	 * Displays a loading screen for the user, while downloading data from the
	 * server. Must be manually dismissed when data transfer is done.
	 * 
	 * @param msg the message to display to the user
	 */
	private void showLoadScreen(String msg) {
		loadScreen = new ProgressDialog(getActivity());
		loadScreen.setTitle("Loading");
		loadScreen.setMessage(msg);
		loadScreen.show();
	}
}
