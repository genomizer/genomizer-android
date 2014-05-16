/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.GenomeRelease;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Anders
 *
 */
public class ConverterFragment extends Fragment{

	private ArrayList<View> viewList;
	private ArrayList<View> enableList;
	private String[] headers = new String[] { "Bowtie", "Geneome version",
			"SAM to GFF", "GFF to SGR", "Smoothing", "Stepsize",
			"Ratio calculation", "Ratio", "Smoothing" };
	private String[] hints = new String[] { "-a -m 1 --best -p 10 -v 2 -q -S",
			"10 1 5 0 0", "y 10", "single 4 0", "150 1 7 0 0" };
	private Spinner spinnerGeneVer;
	private Button convertButton;
	private HashMap<String, Integer> viewMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert_params, container, false);

		spinnerGeneVer = (Spinner) v.findViewById(R.id.spinner_convert_geneversion);

		viewList = new ArrayList<View>();
		viewList = setupTextViews(v, viewList);
		viewList = setupEditTexts(v, viewList);
		viewList = setupToggleButtons(v, viewList);
		convertButton = setupButton(v);
		enableList = sortListForLayout(viewList);
		viewMap = mapWidgets();

		new GetGeneReleaseTask().execute();

		return v;
	}



	/**
	 * 
	 * @param v
	 * @param viewList2
	 * @return
	 */
	private ArrayList<View> setupTextViews(View v, ArrayList<View> viewList2) {
		TextView tw;

		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_bowtie));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_geneversion));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_samtogff));	
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_gfftosgr));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_smoothing));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_stepsize));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_ratiocalc));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_ratio));
		viewList2.add((TextView) v.findViewById(R.id.lbl_convert_ratiosmooth));

		for (int i = 0; i < headers.length; i++) {
			tw = (TextView) viewList2.get(i);
			tw.setText(headers[i]);
		}

		return viewList2;
	}



	/**
	 * @param v
	 * @param viewList2 
	 * @return 
	 */
	private ArrayList<View> setupEditTexts(View v, ArrayList<View> viewList2) {
		EditText et;
		int i = viewList2.size();
		int j = viewList2.size();

		viewList2.add((EditText) v.findViewById(R.id.edit_convert_bowtie));
		viewList2.add((EditText) v.findViewById(R.id.edit_convert_smoothing));
		viewList2.add((EditText) v.findViewById(R.id.edit_convert_stepsize));
		viewList2.add((EditText) v.findViewById(R.id.edit_convert_ratio));
		viewList2.add((EditText) v.findViewById(R.id.edit_convert_ratiosmooth));

		for (String s : hints) {
			et = (EditText) viewList2.get(i);
			et.setHint(s);
			et.addTextChangedListener(new TextWatch(i));
			if (i > j) {
				et.setEnabled(false);
			}
			i++;
		}
		return viewList2;
	}



	/**
	 * @param v
	 * @param viewList2 
	 * @return 
	 */
	private ArrayList<View> setupToggleButtons(View v, ArrayList<View> viewList2) {
		int i = viewList2.size();
		ToggleButton tb;

		viewList2.add((ToggleButton) v.findViewById(R.id.toggle_convert_samtogff));
		viewList2.add((ToggleButton) v.findViewById(R.id.toggle_convert_gfftosgr));
		viewList2.add((ToggleButton) v.findViewById(R.id.toggle_convert_ratiocalc));

		for (int j = i; j < viewList2.size(); j++) {
			tb = (ToggleButton) viewList2.get(j);
			tb.setOnCheckedChangeListener(new CheckerChange(j));
			tb.setEnabled(false);
		}

		return viewList2;
	}



	/**
	 * @param v
	 * @return 
	 */
	private Button setupButton(View v) {
		Button button = (Button) v.findViewById(R.id.btn_convert_convertbutton);
		button.setText("Convert");
		button.setOnClickListener(new buttonListener());

		return button;
	}

	private ArrayList<View> sortListForLayout(ArrayList<View> viewList2) {
		ArrayList<View> temp = new ArrayList<View>();
		
		temp.add(viewList2.get(0));
		temp.add(viewList2.get(9));
		temp.add(viewList2.get(1));
		temp.add(viewList2.get(17));
		temp.add(viewList2.get(2));
		temp.add(viewList2.get(14));
		temp.add(viewList2.get(3));
		temp.add(viewList2.get(15));
		temp.add(viewList2.get(4));
		temp.add(viewList2.get(10));
		temp.add(viewList2.get(5));
		temp.add(viewList2.get(11));
		temp.add(viewList2.get(6));
		temp.add(viewList2.get(16));
		temp.add(viewList2.get(7));
		temp.add(viewList2.get(12));
		temp.add(viewList2.get(8));
		temp.add(viewList2.get(13));
		return temp;
	}



	private HashMap<String, Integer> mapWidgets() {
		HashMap<String, Integer> temp = new HashMap<String, Integer>();

		temp.put("Bowtie header", 0);
		temp.put("GeneVersion header", 1);
		temp.put("SamToGff header", 2);
		temp.put("GffToSSgr", 3);
		temp.put("Smoothing header", 4);
		temp.put("StepSize header", 5);
		temp.put("RatioCalc header", 6);
		temp.put("Ratio header", 7);
		temp.put("RatioSmoothing header", 8);
		temp.put("Bowtie edit", 9);
		temp.put("Smoothing edit", 10);
		temp.put("StepSize edit", 11);
		temp.put("Ratio edit", 12);
		temp.put("RatioSmoothing edit", 13);
		temp.put("SamToGff toggle", 14);
		temp.put("GffToSgr toggle", 15);
		temp.put("RatioCalc toggle", 16);
		temp.put("GeneVer spinner", 17);

		return temp;
	}



	/**
	 * 
	 * @param geneRelList
	 */
	private void setupSpinner(ArrayList<GenomeRelease> geneRelList) {
		ArrayAdapter<String> adapter;
		ArrayList<String> geneList = new ArrayList<String>();

		geneList.add("");
		for (GenomeRelease genomeRelease : geneRelList) {
			geneList.add(genomeRelease.getGenomeVersion());
		}

		adapter = new ArrayAdapter<String>(getActivity()
				,
				android.R.layout.simple_spinner_item, geneList);
		spinnerGeneVer.setAdapter(adapter);
		spinnerGeneVer.setOnItemSelectedListener(new itemListener());
		spinnerGeneVer.setEnabled(false);
		viewList.add(spinnerGeneVer);
	}

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

			switch (id) {
			case 9:
				viewList.get(viewMap.get("GeneVer spinner")).setEnabled(enable);
				break;
			case 10:
				viewList.get(viewMap.get("StepSize edit")).setEnabled(enable);
				break;
			case 11:
				viewList.get(viewMap.get("RatioCalc toggle")).setEnabled(enable);
				break;
			case 12:
				
				break;
			case 13:

				break;

			default:
				break;
			}





			if (s.length() > 0 && id == 0) {

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

			switch (id) {
			case 14:
				viewList.get(viewMap.get("GffToSgr toggle")).setEnabled(isChecked);
				break;
			case 15:
				viewList.get(viewMap.get("Smoothing edit")).setEnabled(isChecked);
				break;
			case 16:
				viewList.get(viewMap.get("Ratio edit")).setEnabled(isChecked);
				viewList.get(viewMap.get("RatioSmoothing edit")).setEnabled(isChecked);
				break;

			default:
				break;
			}
			// TODO Auto-generated method stub

		}
	}

	private class itemListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			viewList.get(viewMap.get("SamToGff toggle")).setEnabled(id > 0);
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
			// TODO Auto-generated method stub
	
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

}
