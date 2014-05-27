/**
 * 
 */
package se.umu.cs.pvt151.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.SingleFragmentActivity;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.layout;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.Genomizer;
import se.umu.cs.pvt151.model.ProcessingParameters;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

	private static final String CONVERSIONS_STARTED = " file-conversions started successfully";
	private static final String CONVERT = "CONVERT";
	private static final String RAW_TO_PROFILE = "raw";
	private static final String FILES = "files";
	private static final String TYPE = "type";

	private final String[] headers = new String[] { "Bowtie", "Geneome version",
			"SAM to GFF", "GFF to SGR", "Smoothing", "Stepsize",
			"Ratio calculation", "Ratio", "Smoothing" };
	private final String[] hints = new String[] { "-a -m 1 --best -p 10 -v 2 -q -S",
			"10 1 5 0 0", "y 10", "single 4 0", "150 1 7 0 0" };

	private Button convertButton;
	@SuppressWarnings("unused")
	private ArrayList<View> headerList;
	private ArrayList<View> viewList;
	private ArrayList<GeneFile> processList;
	private String processType;
	private ArrayList<String> processParameters;
	private ArrayList<GeneFile> failedConversions;
	private ProgressDialog progress;
	private int convertedFiles;
	private IOException convertException;
	private ProgressDialog loadScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	/**
	 * When created, retrieves the geneFiles and processingType for the
	 * conversion to be done. Setup the conversion view for the processingType
	 * intended for the geneFiles.
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
			showLoadScreen("Downloading genome releases");
			new GetGeneReleaseTask().execute();
		}

		return v;
	}
	
	/**
	 * When coming back to the search view, enables the search button again.
	 */
	@Override
	public void onResume() {
		super.onResume();
		convertButton.setEnabled(true);
	}

	/**
	 * Displays a loading screen for the user while data is downloaded from
	 * the server.
	 * 
	 * @param msg the message output to the user
	 */
	private void showLoadScreen(String msg) {
		loadScreen = new ProgressDialog(getActivity());
		loadScreen.setTitle("Loading");
		loadScreen.setMessage(msg);
		loadScreen.show();
	}


	/**
	 * Setup the headers for the different parameter-fields in the conversion
	 * page. 
	 * 
	 * @param v the view where the textFields used as headers is contained. 
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
				et.addTextChangedListener(new textListener(i));
				et.setOnClickListener(new textClickWatch(i));
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

		return tempList;
	}

	/**
	 * Setup the convert button for the conversion view. Attaches a 
	 * buttonListener to the button. 
	 * 
	 * @param v the view where the button are contained in.
	 * @return button for starting the conversion
	 */
	private Button setupButton(View v) {
		Button tempButton = (Button) v.findViewById(R.id.btn_convert_convertbutton);
		tempButton.setText(CONVERT);
		tempButton.setOnClickListener(new buttonListener());

		return tempButton;
	}

	/**
	 * Setup the genomeRealease spinner for the conversion view.
	 * 
	 * @param geneRelList the list with genomeReleases that is to be displayed
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
		sp.setEnabled(true);
	}
	
	/**
	 * Increments the number of files where the conversions has started 
	 * successfully. If the conversion start was failed the geneFile is stored
	 * in an arrayList for notification to the user about which files 
	 * that didn't succeed.
	 * 
	 * @param result true if the conversion started, otherwise false
	 * @param geneFile the geneFile that the boolean value correspond to.
	 */
	private void incrementConverted(boolean result, GeneFile geneFile) {
		if (result) {
			convertedFiles++;
		} else {
			failedConversions.add(geneFile);
		}
	}
	
	/**
	 * Summarize the conversion done, will display to the user which files
	 * that not started correct. And display a toast of the total number of
	 * files that was successfully started for conversion.
	 * Will also dismiss the current conversion view and startup a
	 * processFragment to display the conversions started.
	 */
	private void conversionSummary() {
		String message = "";
		AlertDialog.Builder alertBuilder;
		AlertDialog alert;
		Intent i;
		
		progress.dismiss();
		
		if (!failedConversions.isEmpty()) {

			for (GeneFile g : failedConversions) {
				message += g.getName() + "\n";
			}

			alertBuilder = new AlertDialog.Builder(getActivity());
			alertBuilder.setTitle("Conversions NOT started");
			alertBuilder.setMessage(message);
			alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Genomizer.makeToast(convertedFiles + CONVERSIONS_STARTED);
					startNext();
				}
			});
			
			alert = alertBuilder.create();
			alert.show();
		} else {
			Genomizer.makeToast(convertedFiles + CONVERSIONS_STARTED);
			startNext();
		}
		
			
	}


	/**
	 * 
	 */
	private void startNext() {
		Intent i;
		i = new Intent(getActivity(), ProcessActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		getActivity().overridePendingTransition(0,0);
		getActivity().finish();
	}

	/**
	 * OnCheckedChangeListener, customized for the Genomizer android 
	 * application to be used with the converterFragment on the different
	 * toggleButtons. 
	 * 
	 * @author Anders Lundberg, dv12alg.
	 *
	 */
	private class CheckerChange implements OnCheckedChangeListener {
		private int id;

		/**
		 * Creates a new CheckerChange object with a id set to it.
		 * 
		 * @param id the index number where the ToggleButtons can be found 
		 * in the viewList.
		 */
		public CheckerChange(int id) {
			this.id = id;
		}

		/**
		 * Manages action if the ToggleButton is changed. If the button is
		 * activated then the next ToggleButton or EditText in the 
		 * conversion view will be enabled for edit. If ToggleButton is set
		 * to off, all widgets after will be disabled.
		 * The two last fields are connected together for enabling/disabling.
		 */
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			ToggleButton tb;
			EditText et;

			if (isChecked && (id + 1) < viewList.size()) {
				viewList.get(id + 1).setEnabled(isChecked);
				if (viewList.get(id + 1) instanceof EditText) {
					et = (EditText) viewList.get(id + 1);
					et.performClick();
				}
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
	 * OnClickListener for the Genomizer Android application, to be used
	 * with the convertButton in the converterFragment.
	 * 
	 * @author Anders Lundberg, dv12alg
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

		

			if (processParameters.get(0).length() < 1) {
				Genomizer.makeToast(headers[0] + " must be filled out");
			} else {
				convertButton.setEnabled(false);
				failedConversions = new ArrayList<GeneFile>();
				convertedFiles = 0;
				convertException = null;
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

	}


	/**
	 * 
	 * @author Anders
	 *
	 */
	private class GetGeneReleaseTask extends AsyncTask<Void, Void, ArrayList<GenomeRelease>> {


		/**
		 * 
		 */
		@Override
		protected ArrayList<GenomeRelease> doInBackground(Void... params) {
			ArrayList<GenomeRelease> genomeList = null;
			SingleFragmentActivity act;

			try {
				genomeList = ComHandler.getGenomeReleases();
			} catch (IOException e) {
				act = (SingleFragmentActivity) getActivity();
				act.relogin();
			}

			return genomeList;
		}

		/**
		 * 
		 */
		@Override
		protected void onPostExecute(ArrayList<GenomeRelease> result) {
			super.onPostExecute(result);
			loadScreen.dismiss();
			if (result == null) {
				Genomizer.makeToast("Cant retreive Genome Releases from server");
				getActivity().finish();
			} else if (result.isEmpty()) {
				Genomizer.makeToast("No Gemome releases found on server");
				getActivity().finish();
			} else {
				setupSpinner(result);
			}
		}

	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class ConvertTask extends AsyncTask<GeneFile, Void, HashMap<Boolean, GeneFile>> {



		/**
		 * 
		 */
		@Override
		protected HashMap<Boolean, GeneFile> doInBackground(GeneFile... params) {
			GeneFile geneFile = params[0];
			HashMap<Boolean, GeneFile> map = new HashMap<Boolean, GeneFile>();

			ProcessingParameters parameters = new ProcessingParameters();
			boolean convertOk = false;
			String meta = "";
			String release = processParameters.get(1);

			for (int i = 0; i < processParameters.size(); i++) {
				if (i == 1) {
					parameters.addParameter("");
				} else {
					parameters.addParameter(processParameters.get(i));
				}
			}

			try {
				convertOk = ComHandler.rawToProfile(geneFile, parameters, meta, release);
				map.put(convertOk, geneFile);
			} catch (IOException e) {
				e.printStackTrace();
				convertException = e;
				convertButton.setEnabled(true);
			}

			return map;
		}


		/**
		 * 
		 */
		@Override
		protected void onPostExecute(HashMap<Boolean, GeneFile> result) {
			super.onPostExecute(result);
			boolean convert = result.containsKey(true);
			GeneFile geneFile = result.get(convert);

			if (convertException == null) {
				incrementConverted(convert, geneFile);
				progress.incrementProgressBy(1);

				if (progress.getProgress() == progress.getMax()) {
					conversionSummary();
				}

			} else {
				progress.dismiss();
				Genomizer.makeToast("Server not responding");
			}
		}
	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class textListener implements TextWatcher {

		private int index;
		private ToggleButton tb;

		public textListener(int index) {
			this.index = index;
		}

		/**
		 * Unimplemented
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		/**
		 * 
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (s.length() < 1) {
				if (viewList.size() > (index + 2)) {
					for (int i = index + 1; i < viewList.size(); i++) {
						viewList.get(i).setEnabled(false);
						if (viewList.get(i) instanceof ToggleButton) {
							tb = (ToggleButton) viewList.get(i);
							tb.setChecked(false);
						}
					}	
				}
			} else if (s.length() > 0) {
				viewList.get(index + 1).setEnabled(true);
				if (viewList.get(index + 1) instanceof EditText) {
					viewList.get(index + 1).performClick();
				}
			}
		}

		/**
		 * Unimplemented
		 */
		@Override
		public void afterTextChanged(Editable s) {


		}

	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class textClickWatch implements OnClickListener {

		private int index;
		private EditText et;

		public textClickWatch(int index) {
			this.index = index;
		}

		/**
		 * 
		 */
		@Override
		public void onClick(View v) {
			if (viewList.size() > (index + 1)) {
				et = (EditText) viewList.get(index);
				if (et.getText().length() > 0) {
					if (viewList.get(index + 1) instanceof EditText) {
						et = (EditText) viewList.get(index + 1);
						et.performClick();
					}
					viewList.get(index + 1).setEnabled(true);
				}
			}

		}

	}

}
