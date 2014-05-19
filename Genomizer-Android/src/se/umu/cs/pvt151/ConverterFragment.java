/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessingParameters;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Anders
 *
 */
public class ConverterFragment extends Fragment{

	private static final String RAW_TO_PROFILE = "raw";
	private static final String FILES = "files";
	private static final String TYPE = "type";
	
	private final String[] headers = new String[] { "Bowtie", "Geneome version",
			"SAM to GFF", "GFF to SGR", "Smoothing", "Stepsize",
			"Ratio calculation", "Ratio", "Smoothing" };
	private final String[] hints = new String[] { "-a -m 1 --best -p 10 -v 2 -q -S",
			"10 1 5 0 0", "y 10", "single 4 0", "150 1 7 0 0" };
	
	private Button convertButton;
	private HashMap<String, Integer> viewMap;
	private ArrayList<View> headerList;
	private ArrayList<View> viewList;
	private ArrayList<GeneFile> processList;
	private String processType;
	private ArrayList<String> processParameters;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getActivity().getIntent().getExtras();
		
		if (b != null) {
			processType = (String) b.get(TYPE);
			processList = (ArrayList<GeneFile>) b.get(FILES);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert_params, container, false);

		if (true) {
			headerList = setupHeaders(v);
			viewList = setupWidgetsForLayout(v);
			convertButton = setupButton(v);
			new GetGeneReleaseTask().execute();
		}
		
		return v;
	}



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
		tw.setText("RAW -> PROFILE");
		
		return tempList;
	}

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
				et.addTextChangedListener(new TextWatch(i));
				et.setEnabled(false);
				etCount++;
				
			} else if (i == 2 || i == 3 || i == 6) {
				tb = (ToggleButton) tempList.get(i);
				tb.setOnCheckedChangeListener(new CheckerChange(i));
				tb.setEnabled(false);
				
			} 
			
		}
		
		tempList.get(0).setEnabled(true);
		
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

		geneList.add("");
		
		for (GenomeRelease genomeRelease : geneRelList) {
			geneList.add(genomeRelease.getGenomeVersion());
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, geneList);

		sp = (Spinner) viewList.get(1);
		
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new itemListener(1));
		sp.setEnabled(false);
	}

	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private class TextWatch implements TextWatcher {
		private int id;

		public TextWatch(int id) {
			this.id = id;
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			boolean enable = s.length() > 0;
			
			if (enable) {
				
				if ((id + 1) < viewList.size()) {
					viewList.get(id + 1).setEnabled(enable);
				}
				
			} else {
		
				for (int i = (id + 1); i < viewList.size(); i++) {
					viewList.get(i).setEnabled(enable);
				}
			}
				
		}

	}

	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private class CheckerChange implements OnCheckedChangeListener {
		private int id;

		public CheckerChange(int id) {
			this.id = id;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			
			if (isChecked && (id + 1) < viewList.size()) {
				viewList.get(id + 1).setEnabled(isChecked);
			} else {
				for (int i = (id + 1); i < viewList.size(); i++) {
					viewList.get(i).setEnabled(isChecked);
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
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			if (id == 0) {
				for (int i = (index + 1); i < viewList.size(); i++) {
					viewList.get(i).setEnabled(false);
				}
			} else {
				viewList.get(index + 1).setEnabled(true);
			}
			
		}

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
	
		@Override
		public void onClick(View v) {
			processParameters = new ArrayList<String>();
			EditText et;
			ToggleButton tb;
			Spinner sp;
			
			for (int i = 0; i < viewList.size(); i++) {
				
				if (viewList.get(i).isEnabled()) {
					
					if (i == 0 || i == 4 || i == 5 || i == 7 || i  == 8) {
						et = (EditText) viewList.get(i);
						processParameters.add(et.getText().toString());
					} else if (i == 2 || i == 3) {
						tb = (ToggleButton) viewList.get(i);
						processParameters.add("y");
					} else if (i == 1) {
						sp = (Spinner) viewList.get(i);
						processParameters.add(sp.getSelectedItem().toString());
					}
				} else {
					processParameters.add("");
				}
			}
			
			Log.d("smurf", "ProcessList:\n" + processParameters);
			
			new ConvertTask().execute();
	
		}
	
	}

	
	/**
	 * 
	 * @author Anders
	 *
	 */
	private class GetGeneReleaseTask extends AsyncTask<Void, Void, ArrayList<GenomeRelease>> {

		private ArrayList<GenomeRelease> genomeList;

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

		@Override
		protected void onPostExecute(ArrayList<GenomeRelease> result) {
			super.onPostExecute(result);
			setupSpinner(genomeList);
		}

	}
	
	
	private class ConvertTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ProcessingParameters parameters = new ProcessingParameters();
			String meta = "";
			String release = processParameters.get(1);
			
			processParameters.set(1, "");
			
			for (String s : processParameters) {
				parameters.addParameter(s);
			}
			
			try {
				ComHandler.rawToProfile(processList.get(0), parameters, meta, release);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
}
