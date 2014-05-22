/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessingParameters;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * ConvertFragment handles the different parameters of a conversion in the
 * Genomizer Android application. Will present the user with inputfields 
 * for all the types of parameters that is required for the conversion from
 * Raw to Profile -data.
 * 
 * @author Anders Lundberg, dv12alg
 *
 */
public class ConverterFragment extends Fragment{

	private static final String RAW_TO_PROFILE_PARAMETERS = "Raw to profile parameters";
	private static final String RAW_TO_PROFILE = "raw";
	private static final String FILES = "files";
	private static final String TYPE = "type";

	private final String[] headers = new String[] { "Bowtie", "Geneome version",
			"SAM to GFF", "GFF to SGR", "Smoothing", "Stepsize",
			"Ratio calculation", "Ratio", "Smoothing" };
	private final String[] hints = new String[] { "-a -m 1 --best -p 10 -v 2 -q -S",
			"10 1 5 0 0", "y 10", "single 4 0", "150 1 7 0 0" };

	@SuppressWarnings("unused")
	private Button convertButton;
	@SuppressWarnings("unused")
	private ArrayList<View> headerList;
	private ArrayList<View> viewList;
	private ArrayList<GeneFile> processList;
	private String processType;
	private ArrayList<String> processParameters;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	
	/**
	 * When created, retrieves 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert_params, container, false);
		Bundle b = getActivity().getIntent().getExtras();

		if (b != null) {
			processType = (String) b.get(TYPE);
			processList = (ArrayList<GeneFile>) b.get(FILES);
		}

		if (processType.equals(RAW_TO_PROFILE)) {
			headerList = setupHeaders(v);
			viewList = setupWidgetsForLayout(v);
			convertButton = setupButton(v);
			new GetGeneReleaseTask().execute();
		}

		return v;
	}


	/**
	 * Setup the headers for the different parameter-fields in the conversion
	 * page. 
	 * 
	 * @param v the view where the textfields used as headers is contained. 
	 * @return ArrayList<View> with all the textFields used as headers.
	 */
	private ArrayList<View> setupHeaders(View v) {
		ArrayList<View> tempList = new ArrayList<View>();
		TextView tw;

		tempList.add((TextView) v.findViewById(R.id.lbl_convert_bowtie));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_geneversion));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_samtogff));	
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_gfftosgr));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_smoothing));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_stepsize));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_ratiocalc));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_ratio));
		tempList.add((TextView) v.findViewById(R.id.lbl_convert_ratiosmooth));

		for (int i = 0; i < tempList.size(); i++) {
			tw = (TextView) tempList.get(i);
			tw.setText(headers[i]);
		}

		tw = (TextView) v.findViewById(R.id.lbl_convert_header);
		tw.setText(RAW_TO_PROFILE_PARAMETERS);

		return tempList;
	}
	
	/**
	 * Sets up the different widgets for the conversion parameter page
	 * presented for the user. Sets onTouchListeners on the editTextFields and
	 * onCheckedChangeListeners on the toggleButtons. Also disables all the
	 * widgets except for the first 2 that are mandatory for conversion. 
	 * 
	 * @param v the view where the widgets are contained in.
	 * @return ArrayList<View> with the different widgets in the order that 
	 * they appear on the screen
	 */
	private ArrayList<View> setupWidgetsForLayout(View v) {
		int etCount = 0;
		EditText et;
		ToggleButton tb;
		ArrayList<View> tempList = new ArrayList<View>();

		tempList.add((EditText) v.findViewById(R.id.edit_convert_bowtie));
		tempList.add((Spinner) v.findViewById(R.id.spinner_convert_geneversion));
		tempList.add((ToggleButton) v.findViewById(R.id.toggle_convert_samtogff));
		tempList.add((ToggleButton) v.findViewById(R.id.toggle_convert_gfftosgr));
		tempList.add((EditText) v.findViewById(R.id.edit_convert_smoothing));
		tempList.add((EditText) v.findViewById(R.id.edit_convert_stepsize));
		tempList.add((ToggleButton) v.findViewById(R.id.toggle_convert_ratiocalc));
		tempList.add((EditText) v.findViewById(R.id.edit_convert_ratio));	
		tempList.add((EditText) v.findViewById(R.id.edit_convert_ratiosmooth));

		for (int i = 0; i < tempList.size(); i++) {

			if (i == 0 || i == 4 || i == 5 || i == 7 || i  == 8) {
				et = (EditText) tempList.get(i);
				et.setText(hints[etCount]);
				et.setOnTouchListener(new TextSelector(i));
				if (i != 0) {
					et.setEnabled(false);
				}
				etCount++;

			} else if (i == 2 || i == 3 || i == 6) {
				tb = (ToggleButton) tempList.get(i);
				tb.setOnCheckedChangeListener(new CheckerChange(i));
				if (i != 2) {
					tb.setEnabled(false);
				}

			} 

		}

//		tempList.get(0).setEnabled(true);
//		tempList.get(2).setEnabled(true);

		return tempList;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	private Button setupButton(View v) {
		Button tempButton = (Button) v.findViewById(R.id.btn_convert_convertbutton);
		tempButton.setText("CONVERT");
		tempButton.setOnClickListener(new buttonListener());

		return tempButton;
	}

	/**
	 * 
	 * @param geneRelList
	 */
	private void setupSpinner(ArrayList<GenomeRelease> geneRelList) {
		Spinner sp;
		ArrayAdapter<String> adapter;
		ArrayList<String> geneList = new ArrayList<String>();

		for (GenomeRelease genomeRelease : geneRelList) {
			geneList.add(genomeRelease.getGenomeVersion());
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, geneList);

		sp = (Spinner) viewList.get(1);

		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new itemListener(1));
		sp.setEnabled(true);
	}

	/**
	 * 
	 * @param response
	 */
	private void toastUser(String response) {
		Toast.makeText(getActivity().getApplicationContext(), response,
				Toast.LENGTH_SHORT).show();

	}
	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private class TextSelector implements OnTouchListener {

		private int index;
		
		/**
		 * 
		 * @param index
		 */
		public TextSelector(int index) {
			this.index = index;
		}
		
		/**
		 * 
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int start;
			
			EditText et = (EditText) viewList.get(index);

			if (et.getText().length() > 0 && (index + 1) < viewList.size()) {
				viewList.get(index + 1).setEnabled(true);
			} else {
				if (index == 1) {
					start = index + 2;
				} else {
					start = index + 1;
				}
				
				for (int i = start; i < viewList.size(); i++) {
					viewList.get(i).setEnabled(false);
				}
			}

			return false;
		}

	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class CheckerChange implements OnCheckedChangeListener {
		private int id;
		
		/**
		 * 
		 * @param id
		 */
		public CheckerChange(int id) {
			this.id = id;
		}
		
		/**
		 * 
		 */
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			ToggleButton tb;

			if (isChecked && (id + 1) < viewList.size()) {
				viewList.get(id + 1).setEnabled(isChecked);
			} else {
				for (int i = (id + 1); i < viewList.size(); i++) {
					viewList.get(i).setEnabled(isChecked);
					if (viewList.get(i) instanceof ToggleButton) {
						tb = (ToggleButton) viewList.get(i);
						tb.setChecked(isChecked);
					}
				}
			}

			if (id == 6) {
				viewList.get(id + 2).setEnabled(isChecked);
			}
		}
	}


	/**
	 * 
	 * @author Anders
	 *
	 */
	private class itemListener implements OnItemSelectedListener {
		private int index;

		public itemListener(int id) {
			this.index = id;
		}
		
		/**
		 * 
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			
//			if (id == 0) {
//				for (int i = (index + 1); i < viewList.size(); i++) {
//					viewList.get(i).setEnabled(false);
//				}
//			} else {
//				viewList.get(index + 1).setEnabled(true);
//			}

		}
		
		/**
		 * 
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}


	/**
	 * 
	 * @author Anders
	 *
	 */
	private class buttonListener implements OnClickListener {
		
		

		/**
		 * 
		 */
		@Override
		public void onClick(View v) {
			processParameters = new ArrayList<String>();
			EditText et;
			Spinner sp;

			for (int i = 0; i < viewList.size(); i++) {
				if(i == 6) continue;
				if (viewList.get(i).isEnabled()) {

					if (i == 0 || i == 4 || i == 5 || i == 7 || i  == 8) {
						et = (EditText) viewList.get(i);
						processParameters.add(et.getText().toString());
					} else if (i == 2 || i == 3) {
						processParameters.add("y");
					} else if (i == 1) {
						sp = (Spinner) viewList.get(i);
						processParameters.add(sp.getSelectedItem().toString());
					}
				} else {
					processParameters.add("");
				}
			}
			
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Starting conversions");
			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress.setMax(processList.size());
			progress.show();
			
			for (int i = 0; i < processList.size(); i++) {
				new ConvertTask().execute(processList.get(i));
			}

		}

	}


	/**
	 * 
	 * @author Anders
	 *
	 */
	private class GetGeneReleaseTask extends AsyncTask<Void, Void, ArrayList<GenomeRelease>> {

		private ArrayList<GenomeRelease> genomeList;
		
		/**
		 * 
		 */
		@Override
		protected ArrayList<GenomeRelease> doInBackground(Void... params) {
			try {
				genomeList = ComHandler.getGenomeReleases();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
		
		/**
		 * 
		 */
		@Override
		protected void onPostExecute(ArrayList<GenomeRelease> result) {
			super.onPostExecute(result);
			if (genomeList == null || genomeList.isEmpty()) {
				toastUser("Cant retreive Genome Releases from server");
				getActivity().finish();
			} else {
				setupSpinner(genomeList);
			}
		}

	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class ConvertTask extends AsyncTask<GeneFile, Void, Boolean> {

		private boolean processResult = true;
		private IOException caughtException = null;
		private int failCount = 0;
		private int testInt = 0;
		private boolean convertOk;
		
		/**
		 * 
		 */
		@Override
		protected Boolean doInBackground(GeneFile... params) {
			GeneFile geneFile = params[0];
			ProcessingParameters parameters = new ProcessingParameters();
			boolean convertOk = false;
			String meta = "";
			String release = processParameters.get(1);

			processParameters.set(1, "");

			for (String s : processParameters) {
				parameters.addParameter(s);
			}

			try {
//				for (GeneFile geneFile : processList) {
					convertOk = ComHandler.rawToProfile(geneFile, parameters, meta, release);
//					if (!convertOk) {
//						processResult = false;
//						failCount ++;
//					}
//					testInt ++;
					Log.d("Convert", "ConvertFragment part1, ConvertOk: " + convertOk);
//				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
				caughtException = e;
			}

			return convertOk;
		}
		

		/**
		 * 
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			boolean convert = Boolean.valueOf(result);

			String response = "";
			if (caughtException == null) {
				Log.d("Convert", "ConvertFragment part2, convert: " + convert);
				if (convert) {
					progress.incrementProgressBy(1);
					if (progress.getProgress() == progress.getMax()) {
						progress.dismiss();
					}
					
					
				} else {
					if (processList.size() > 1) {
						response = failCount + " conversions failed to start";
					} else {
						response = "Conversion malfunction";					
					}
					
				}
			} else {
				progress.dismiss();
				response = "Server not responding";
			}
			if (response.length() > 0) {
				toastUser(response);
			}

		}

	}

}
