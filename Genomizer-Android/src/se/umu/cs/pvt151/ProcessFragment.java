package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Process;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ProcessFragment extends Fragment {

	private ListView processList;

	private ArrayList<Process> processes;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		processes = new ArrayList<Process>();
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_process, parent, false);

		processList = (ListView) v.findViewById(R.id.processList);

		return v;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ProcessTask().execute();
	}


	/**
	 * Adapter used for listviews
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
				view = inflater.inflate(R.layout.list_view_checkbox, null);
			}

			Process process = getItem(position);

			if (process != null) {
				TextView textView = (TextView) view.findViewById(R.id.textView1);
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.textForBox);

				checkBox.setTag(position);

//				textView.setText(process.getName());

				view.setTag(textView);
				view.setTag(checkBox);
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
	
	
	private class ProcessTask extends AsyncTask<Void, Void, Void> {
		
		private IOException except;

		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				processes = ComHandler.getProcesses();
				
			} catch (IOException e) {
				except = e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (except == null) {
				processList.setAdapter(new ProcessListAdapter(processes));
			} else {
				except.printStackTrace();
				except = null;
			}
			
		}
		
	}
}
