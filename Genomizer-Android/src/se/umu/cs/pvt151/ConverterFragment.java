/**
 * 
 */
package se.umu.cs.pvt151;

import java.util.ArrayList;

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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert_params, container, false);
		
		setupTextViews(v);
		setupEditTexts(v);
		setupToggleButtons(v);
		
//		GetGeneReleaseTask.execute();
//		setupSpinner(v);
		setupButton(v);
		
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
	 * @param v
	 */
	private void setupSpinner(View v) {
		spinnerGeneVer = (Spinner) v.findViewById(R.id.spinner_convert_geneversion);
		ArrayList<String> geneList = new ArrayList<String>();
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, geneList);
		spinnerGeneVer.setAdapter(adapter);
	}

	/**
	 * @param v
	 */
	private void setupToggleButtons(View v) {
		toggleSamToGff = (ToggleButton) v.findViewById(R.id.toggle_convert_samtogff);
		toggleGffToSam = (ToggleButton) v.findViewById(R.id.toggle_convert_gfftosgr);
		toggleRatioCalc = (ToggleButton) v.findViewById(R.id.toggle_convert_ratiocalc);
	}

	/**
	 * @param v
	 */
	private void setupEditTexts(View v) {
		editBowtie = (EditText) v.findViewById(R.id.edit_convert_bowtie);
		editBowtie.setHint(hints[0]);
		editSmoothing = (EditText) v.findViewById(R.id.edit_convert_smoothing);
		editSmoothing.setHint(hints[1]);
		editStepSize = (EditText) v.findViewById(R.id.edit_convert_stepsize);
		editStepSize.setHint(hints[2]);
		editRatio = (EditText) v.findViewById(R.id.edit_convert_ratio);
		editRatio.setHint(hints[3]);
		editRatioSmooth = (EditText) v.findViewById(R.id.edit_convert_ratiosmooth);
		editRatioSmooth.setHint(hints[4]);
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class SpinnListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class GetGeneReleaseTask extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
