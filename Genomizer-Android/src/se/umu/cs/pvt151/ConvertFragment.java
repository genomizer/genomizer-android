/**
 * 
 */
package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.spec.PSource;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.ConversionParameter;
import se.umu.cs.pvt151.model.ConvertLogic;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.ProcessingParameters;
import android.R.integer;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Anders
 *
 */
public class ConvertFragment extends Fragment {

	private static final String PARAMETERS_RAW_PROFILE = "Parameters RAW -> Profile";
	private static final String RAW_CONVERSION = "raw";
	private static final String FILES = "files";
	private static final String CONVERSION_TYPE = "type";
	private ListView convertListView;
	private ArrayList<ConversionParameter> parameters;
	private ArrayList<String> convertFields;
	private ArrayList<ConvertViewHolder> viewHolderList;
	private Button convertButton;
	private String type;
	private ArrayList<GeneFile> files;
	private TextView convertLabel;
	public boolean taskCompleted;
	private ConvertAdapter convertAdapter;

	/**
	 * 
	 */
	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_convert, container, false);

		convertListView = (ListView) v.findViewById(R.id.listview_convert);
		convertLabel = (TextView) v.findViewById(R.id.lbl_convert_header);
		convertLabel.setText(PARAMETERS_RAW_PROFILE);


		return v;
	}

	/**
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createLists();
		retrieveData();	
	}


	/**
	 * 
	 */
	private void retrieveData() {
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			type = (String) bundle.get(CONVERSION_TYPE);
			files = (ArrayList<GeneFile>) bundle.get(FILES);
		}
	}


	/**
	 * 
	 */
	private void createLists() {
		parameters = new ArrayList<ConversionParameter>();
		convertFields = new ArrayList<String>();
		viewHolderList = new ArrayList<ConvertFragment.ConvertViewHolder>();
		setupRawParameters();


		//		hintList = new ArrayList<String>();
		//		parameterMap = new HashMap<String, String>();
		//		checkedFields = new HashMap<String, Boolean>();
		//		fieldTypeMap = new ArrayList<String>();
		//		valueList = new HashMap<String, String[]>();
		//		choosenConversionParameters = new HashMap<String, String>();
	}

	/**
	 * 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (type != null && type.equals(RAW_CONVERSION)) {
			setupRawParameters();
		}

		createConvertButton();
		setupConvert();
	}

	/**
	 * 
	 */
	private void setupConvert() {



		//		for (String s : convertFields) {
		//			checkedFields.put(s, false);
		//			parameterMap.put(s, null);
		//		}
		convertAdapter = new ConvertAdapter(convertFields);
		convertListView.setAdapter(convertAdapter);
	}


	/**
	 * 
	 */
	private void createConvertButton() {
		convertButton = (Button) getActivity().findViewById(R.id.btn_convert_convert);
		convertButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean gap = false;
				boolean paramGatherOk = true;
				int position = 0;
				ProcessingParameters processList = new ProcessingParameters();

				for (ConversionParameter conv : parameters) {
					if (!conv.isChecked()) {
						gap = true;
					} else if (gap) {
						toastUser(parameters.get(position - 1).getName() + " needs to be filled");
						paramGatherOk = false;
						break;
					} else if (conv.getPresentValue().length() < 1) {
						toastUser(conv.getName() +  ", must be filled");
						paramGatherOk = false;
						break;
					} else if (conv.getName().equals("Ratio Calculation") && conv.isChecked()) {
						boolean a = parameters.get(7).isChecked();
						boolean b = parameters.get(8).isChecked();
						if (!a || !b) {
							toastUser("Both " + parameters.get(7).getName()
									+ " and " + parameters.get(8).getName()
									+ " must be used if " + conv.getName()
									+ " is checked");
							paramGatherOk = false;
							break;
						}
					} else {
						processList.addParameter(conv.getPresentValue());
						Log.d("smurf", "Value added: " + conv.getPresentValue());
					}
					position++;
				}

				if (paramGatherOk) {
					for (int i = processList.size(); i < 9; i++) {
						Log.d("smurf", "Value added: " + "");
					}
					//					new ConvertTask().execute(processList);
				}

				Log.d("smurf", "============================================");
			}


		});
	}


	/**
	 * 
	 */
	private void conversionCheck() {
		if (taskCompleted) {
			toastUser("Conversion started");
		} else {
			toastUser("Conversion malfunction");
		}
		taskCompleted = false;
	}

	protected void toastUser(String string) {
		Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();

	}

	/**
	 * 
	 */
	private void setupRawParameters() {
		int i = 0;

		ArrayList<String> yesNo = new ArrayList<String>();
		yesNo.add("Yes");
		yesNo.add("No");

		ArrayList<String> gene = new ArrayList<String>();
		gene.add("Smurf ver. 1.2.4");
		gene.add("Gargamel ver. 1.2.4");
		gene.add("Mufflon ver. 1.2.5");
		gene.add("Minotaur ver. 1.3.4");
		gene.add("Harpy ver. 2.3.4");
		gene.add("Smaug ver. 4.32.2");

		convertFields.add("Bowtie parameters");
		convertFields.add("Genome version");
		convertFields.add("SAM to GFF");
		convertFields.add("GFF to SGR");
		convertFields.add("Smoothing");
		convertFields.add("Step size");
		convertFields.add("Ratio Calculation");
		convertFields.add("Ratio");
		convertFields.add("Ratio smoothing");

		for (String s : convertFields) {
			ConversionParameter temp = new ConversionParameter(s);
			if (i == 1) {
				temp.setParameterType("spinner");
				temp.setValues(gene);
			} else if (i == 2 || i == 3) {
				temp.setParameterType("spinner");
				temp.setValues(yesNo);
			} else {
				temp.setParameterType("freetext");
			}
			parameters.add(temp);
			i++;
		}		
	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private static class ConvertViewHolder {
		TextView title;
		EditText parameter;
		CheckBox checkBox;
		Spinner spinner;
		int spinnerSelection;
	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class ConvertAdapter extends ArrayAdapter<String> {

		/**
		 * 
		 * @param convertFields
		 */
		public ConvertAdapter(ArrayList<String> convertFields) {
			super(getActivity(), 0, convertFields);
		}

		/**
		 * 
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ConvertViewHolder viewHolder = null;
			ConvertTextWatch textWatcher = null;
			ArrayAdapter<String> spinAdapter;
			int resource;
			boolean fieldIsFreetext = parameters.get(position).getParameterType().equals("freetext");
			String temp;

			if (fieldIsFreetext) {
				resource = R.layout.conversion_item;
			} else {
				resource = R.layout.conversion_spinner_item;
			}

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(resource, null);

				viewHolder = new ConvertViewHolder();
				viewHolderList.add(viewHolder);
				viewHolder.title = (TextView) convertView.findViewById(R.id.lbl_conversion_item);
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_conversion_item);

				viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int pos = (Integer) buttonView.getTag();
						parameters.get(pos).setChecked(isChecked);
					}
				});

				if (fieldIsFreetext) {
					textWatcher = new ConvertTextWatch();
					viewHolder.parameter = (EditText) convertView.findViewById(R.id.edit_conversion_item);
					viewHolder.parameter.addTextChangedListener(textWatcher);
					viewHolder.parameter.setTag(textWatcher);
					parameters.get(position).setEditText(viewHolder.parameter);

				} else {
					viewHolder.spinner = (Spinner) convertView.findViewById(R.id.spinner_conversion_item);
					spinAdapter = new ArrayAdapter<String>(convertView.getContext(), android.R.layout.simple_spinner_item, parameters.get(position).getValues());
					spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					viewHolder.spinner.setAdapter(spinAdapter);
					viewHolder.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							int pos = (Integer) parent.getTag();
							ConversionParameter c = parameters.get(pos);

							c.setSelectedSpinnerVal(position);
							c.setPresentValue(parent.getSelectedItem().toString());

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}

					});
				}



				convertView.setTag(viewHolder);
				convertView.setTag(R.id.lbl_conversion_item, viewHolder.title);
				convertView.setTag(R.id.edit_conversion_item, viewHolder.parameter);
				convertView.setTag(R.id.checkbox_conversion_item, viewHolder.checkBox);

			}else {
				viewHolder = (ConvertViewHolder) convertView.getTag();
			}

			if (fieldIsFreetext) {
				textWatcher = (ConvertTextWatch) viewHolder.parameter.getTag();
				textWatcher.updatePosition(position);
				viewHolder.parameter.setHint("Nudge nudge, you know what i mean");
				viewHolder.parameter.setText(parameters.get(position).getPresentValue());

			} else {
				viewHolder.spinner.setTag(position);
				viewHolder.spinner.setSelection(parameters.get(position).getSelectedSpinnerVal());

			}

			viewHolder.checkBox.setTag(position);
			viewHolder.title.setText(parameters.get(position).getName());
			viewHolder.checkBox.setChecked(parameters.get(position).isChecked());
			return convertView;
		}
	}

	/**
	 * 
	 * @author Anders
	 *
	 */
	private class ConvertTextWatch implements TextWatcher {
		private int position;
		private int logicIndex = 0;
		private int lastCharIndex = 0;

		/**
		 * 
		 * @param position
		 */
		public void updatePosition(int position) {
			this.position = position;
		}

		/**
		 * 
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		/**
		 * 
		 */
		@Override
		public void afterTextChanged(Editable s) {
			int i;
			CharSequence subSequence;
			int lastCharPos = 0;
			char lastChar = 0;
			if (s.length() > 0) {
				lastCharPos = s.length() - 1;
				lastChar = s.charAt(lastCharPos);
			} 

			if (position == 4 && lastChar == ' ') {
				i = lastCharPos;
				while (i > 0) {
					if (s.charAt(i - 1) == ' ') {
						break;
					}
					i--;
				}
				subSequence = s.subSequence(i, lastCharPos);
				verify(logicIndex, subSequence.toString());
				Log.d("smurf", "SubString: " + subSequence.toString() + "\nLogicIndex: "  + logicIndex);
				
				if (lastCharIndex > lastCharPos) {
					logicIndex--;
				} else if (lastCharIndex < lastCharPos) {
					logicIndex++;
				}
				lastCharIndex = lastCharPos;
			}
			parameters.get(position).setPresentValue(s.toString());
		}


		private void verify(int logicIndex, String string) {
			ConvertLogic logic = new ConvertLogic();
			boolean verify = false;
			try {
				int test = Integer.parseInt(string);
				
				switch (logicIndex) {
				case 0:
					verify = logic.verifyWindowSize(Integer.parseInt(string));
					break;
				case 1:
					verify = logic.verifySmoothType(Integer.parseInt(string));
					break;
				case 2:
					verify = logic.verifyMinStep(Integer.parseInt(string));
					break;
				case 3:
					verify = logic.verifyPrintMean(Integer.parseInt(string));
					break;
				case 4:
					verify = logic.verifyPrintZero(Integer.parseInt(string));
					break;

				default:
					break;
				}
			} catch (NumberFormatException e) {
				userFeedback(logicIndex);
			}
			

			if (verify == false) {
				userFeedback(logicIndex);
			}

		}

		/**
		 * @param logicIndex
		 */
		private void userFeedback(int logicIndex) {
			switch (logicIndex) {
			case 1:
				toastUser("Window size must be over 0");
				break;
			case 2:
				toastUser("Smooth type can only be between 0 or 1");
				break;
			case 3:
				toastUser("Minimum step cant be negative");
				break;
			case 4:
				toastUser("Print mean can only be 0 or 1");
				break;
			case 5:
				toastUser("Print zeros can only be 0 or 1");
				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;


			default:
				break;
			}
		}

		/**
		 * 
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}
	}

	private class ConvertTask extends AsyncTask<ProcessingParameters, Void, Void> {

		@Override
		protected Void doInBackground(ProcessingParameters... params) {

			try {
				taskCompleted = ComHandler.rawToProfile(files.get(0), params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			conversionCheck();
		}

	}

}
