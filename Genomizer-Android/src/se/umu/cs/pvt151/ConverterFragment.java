/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.GenomeRelease;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Anders
 *
 */
public class ConverterFragment extends Fragment{
	
	private TextView txtBowtie;
	private TextView txtGeneVer;
	private TextView txtSamToGff;
	private TextView txtGffToSgr;
	private TextView txtSmoothing;
	private TextView txtStepSize;
	private TextView txtRatioCalc;
	private TextView txtRatio;
	private TextView txtRatioSmooth;
	private EditText editBowtie;
	private EditText editSmoothing;
	private EditText editStepSize;
	private EditText editRatio;
	private EditText editRatioSmooth;
	private ToggleButton toggleSamToGff;
	private ToggleButton toggleGffToSam;
	private ToggleButton toggleRatioCalc;
	private Spinner spinnerGeneVer;
	private Button convertButton;
	private String[] headers = new String[] {"Bowtie", "Geneome version", "SAM to GFF", "GFF to SGR", "Smoothing", "Stepsize", "Ratio calculation", "Ratio", "Smoothing"};
	private String[] hints = new String[] {"-a -m 1 --best -p 10 -v 2 -q -S", "10 1 5 0 0", "y 10", "single 4 0", "150 1 7 0 0"};
	private ArrayList<EditText> editTextList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert_params, container, false);
		
		setupTextViews(v);
		setupEditTexts(v);
		setupToggleButtons(v);
		setupButton(v);
		spinnerGeneVer = (Spinner) v.findViewById(R.id.spinner_convert_geneversion);
		new GetGeneReleaseTask().execute();

		return v;
	}

	

	/**
	 * @param v
	 */
	private void setupButton(View v) {
		convertButton = (Button) v.findViewById(R.id.btn_convert_convertbutton);
		convertButton.setText("Convert");
		convertButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
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
	}

	/**
	 * @param v
	 */
	private void setupToggleButtons(View v) {
		toggleSamToGff = (ToggleButton) v.findViewById(R.id.toggle_convert_samtogff);
		toggleSamToGff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		
		toggleGffToSam = (ToggleButton) v.findViewById(R.id.toggle_convert_gfftosgr);
		toggleGffToSam.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		
		toggleRatioCalc = (ToggleButton) v.findViewById(R.id.toggle_convert_ratiocalc);
		toggleRatioCalc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * @param v
	 */
	private void setupEditTexts(View v) {
		editTextList = new ArrayList<EditText>();
		
		editBowtie = (EditText) v.findViewById(R.id.edit_convert_bowtie);
		editBowtie.setHint(hints[0]);
		editBowtie.addTextChangedListener(new TextWatch(0));
		editTextList.add(editBowtie);
		
		editSmoothing = (EditText) v.findViewById(R.id.edit_convert_smoothing);
		editSmoothing.setHint(hints[1]);
		editBowtie.addTextChangedListener(new TextWatch(1));
		editTextList.add(editSmoothing);
		
		editStepSize = (EditText) v.findViewById(R.id.edit_convert_stepsize);
		editStepSize.setHint(hints[2]);
		editBowtie.addTextChangedListener(new TextWatch(2));
		editTextList.add(editStepSize);
		
		editRatio = (EditText) v.findViewById(R.id.edit_convert_ratio);
		editRatio.setHint(hints[3]);
		editBowtie.addTextChangedListener(new TextWatch(3));
		editTextList.add(editRatio);
		
		editRatioSmooth = (EditText) v.findViewById(R.id.edit_convert_ratiosmooth);
		editRatioSmooth.setHint(hints[4]);
		editBowtie.addTextChangedListener(new TextWatch(4));
		editTextList.add(editRatioSmooth);
		
		for (EditText e : editTextList) {
			e.setEnabled(false);
		}
		editTextList.get(0).setEnabled(true);
	}

	/**
	 * @param v
	 */
	private void setupTextViews(View v) {
		txtBowtie = (TextView) v.findViewById(R.id.lbl_convert_bowtie);
		txtBowtie.setText(headers[0]);
		
		txtGeneVer = (TextView) v.findViewById(R.id.lbl_convert_geneversion);
		txtGeneVer.setText(headers[1]);
		
		txtSamToGff = (TextView) v.findViewById(R.id.lbl_convert_samtogff);
		txtSamToGff.setText(headers[2]);
		
		txtGffToSgr = (TextView) v.findViewById(R.id.lbl_convert_gfftosgr);
		txtGffToSgr.setText(headers[3]);
		
		txtSmoothing = (TextView) v.findViewById(R.id.lbl_convert_smoothing);
		txtSmoothing.setText(headers[4]);
		
		txtStepSize = (TextView) v.findViewById(R.id.lbl_convert_stepsize);
		txtStepSize.setText(headers[5]);
		
		txtRatioCalc = (TextView) v.findViewById(R.id.lbl_convert_ratiocalc);
		txtRatioCalc.setText(headers[6]);
		
		txtRatio = (TextView) v.findViewById(R.id.lbl_convert_ratio);
		txtRatio.setText(headers[7]);
		
		txtRatioSmooth = (TextView) v.findViewById(R.id.lbl_convert_ratiosmooth);
		txtRatioSmooth.setText(headers[8]);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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
			Log.d("smurf", "s: " + s);
			if (editTextList.size() > id && s.length() == 0) {
				Log.d("smurf", "edittext: " + editTextList.size());
//				editTextList.get(id + 1).setEnabled(false);
			} else if (editTextList.size() > id) {
				Log.d("smurf", "edittext: " + editTextList.size());
//				editTextList.get(id + 1).setEnabled(true);
			}
		}
		
	}
	
	
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
