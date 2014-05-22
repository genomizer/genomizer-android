package se.umu.cs.pvt151;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Annotation;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Fragment which will display search options for the Genomizer app, retreives
 * the current annotations used by the Genomizer database and presents them to 
 * the user for searching.
 * 
 * @author Anders Lundberg, dv12alg
 * @author Erik Åberg, c11ean
 *
 */
public class SearchListFragment extends ListFragment {

	private static final String ANNOTATIONS_EXTRA = "Annotations";
	protected static final String SEARCH_MAP_EXTRA = "searchMap";
	protected static final String CONNECTION_ERROR = "Unable to connect to the remote server";
	protected static final String NO_SEARCH_VALUES = "No annotations choosen for search";
	
	private ArrayList<String> mAnnotationNamesList;
	private Button mSearchButton;
	private ArrayList<Annotation> mAnnotations;
	private ArrayList<SearchViewHolder> mViewHolderList = new ArrayList<SearchViewHolder>();
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * Starts the Annotation asyncTask to retrieve the annotations from
	 * the Genomizer database. 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new AnnotationsTask().execute();
	}
	
	public void onMenuItemPress(String string) {
		if(string.equals(getString(R.string.action_search_editPub))) {
			Intent intent = new Intent(getActivity(),
					SearchPubmedActivity.class);
			intent.putExtra("PubmedQuery", generateSearchString());
			intent.putExtra(ANNOTATIONS_EXTRA, mAnnotationNamesList);
			intent.putExtra(SEARCH_MAP_EXTRA, generateSearchMap());
			startActivity(intent);	
		}
	}
	
	/**
	 * Initializes a new SearchListAdapter for the listView in the fragment 
	 * containing the generated annotations from the database and
	 * setup a footer for the search button to generate a search string.
	 */
	private void setupListView() {
		ArrayAdapter<String> adapter;
		View footer = generateFooter();
		generateSearchButton(footer);
		adapter = new SearchListAdapter(mAnnotationNamesList);
		adapter.setNotifyOnChange(true);
		setListAdapter(adapter);
	}

	/**
	 * Generates a search button connected to a onClickListener, which will
	 * generate a hashmap with the selected values for  
	 * 
	 * @param footer the view where the button is placed
	 */
	private void generateSearchButton(View footer) {
		mSearchButton = (Button) footer.findViewById(R.id.btn_search_footer);
		mSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ExperimentListActivity.class);

				HashMap<String, String> search = generateSearchMap();
				
				if (search.isEmpty()) {
					toastMessage(NO_SEARCH_VALUES);
				} else {
					intent.putExtra(ANNOTATIONS_EXTRA, mAnnotationNamesList);
					intent.putExtra(SEARCH_MAP_EXTRA, search);
					startActivity(intent);
					
				}

			}
		});
	}
	
	private HashMap<String, String> generateSearchMap() {
		String key = null;
		String value = null;
		
		HashMap<String, String> search = new HashMap<String, String>();

		for (SearchViewHolder vh : mViewHolderList) {
			if (vh.isChecked && vh.isDropDown) {
				key = vh.textView.getText().toString();
				for (Annotation annotation : mAnnotations) {
					if (annotation.getName().equals(key)) {
						value = annotation.getValue().get(
								vh.selectedPosition);
					}
				}

			} else if (vh.isChecked) {
				if (vh.freetext != null && vh.freetext.length() > 0) {
					key = vh.textView.getText().toString();
					value = vh.freetext;

				}
			}
			if (key != null && value != null) {
				search.put(key, value);
			}
		}
		return search;
	}
	
	private String generateSearchString() {
		try {
			return ComHandler.generatePubmedQuery(generateSearchMap());
		} catch (UnsupportedEncodingException e) {		
			e.printStackTrace();
		}
		return "0o unsupportedEncoding";
	}

	/**
	 * Generates and returns a footer view for the searchList Fragment.
	 * 
	 * @return the View of the generated footer
	 */
	private View generateFooter() {
		ListView listView = getListView();
		View footer = getActivity().getLayoutInflater().inflate(
				R.layout.searchlist_footer, null);
		listView.addFooterView(footer);
		return footer;
	}
	
	/**
	 * Generates toast messages for the searchListFragment
	 * 
	 * @param text the string that needs to be displayed.
	 */
	private void toastMessage(String text) {
		Toast.makeText(getActivity().getApplicationContext(), text,
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Static searchViewHolder class for keeping items in the searchList in 
	 * memory when scrolled out of the screen.
	 * 
	 * @author Anders Lundberg, dv12alg
	 * @author Erik Åberg, c11eag
	 */
	static class SearchViewHolder {
		protected EditText editText;
		protected TextView textView;
		protected Spinner spinner;
		protected int position;
		protected int selectedPosition;
		protected String freetext;
		protected boolean isDropDown;
		protected CheckBox checkBox;
		protected boolean isChecked;
		protected View convertView;
	}
	
	/**
	 * Implementation of ArrayAdapter made for the genomizer app.
	 * Will use the searchViewHolder as memory of the list scrolled out of view.
	 * 
	 * @author Anders Lundberg, dv12alg 
	 * @author Erik Åberg, c11ean
	 */
	private class SearchListAdapter extends ArrayAdapter<String> {
		
		/**
		 * Creates a new SearchListAdapter, with the annotationlist passed into
		 * the adapter.
		 * 
		 * @param annotationNames List with annotations to be displayed in the
		 * adapter.
		 */
		public SearchListAdapter(ArrayList<String> annotationNames) {
			super(getActivity(), 0, annotationNames);
		}
		
		/**
		 * Creates new views for each annotation category that is set in the
		 * adapter to the listView. Creates either a freetext field or a
		 * dropdown menu, depending on which is specified for the annotation
		 * category. Stores the views information in a viewholder for a
		 * memory management when the view is out of screen.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SearchViewHolder viewHolder = null;
			ArrayAdapter<String> spinAdapter;
			Spinner spinner;

			if (convertView == null) {
				final ArrayList<String> mSpinnerList = mAnnotations.get(position).getValue();				
				
				if(mSpinnerList.size() == 1 && mSpinnerList.get(0).compareTo("freetext") == 0) {
					convertView = getActivity().getLayoutInflater().inflate(
							R.layout.searchlist_field, null);
					viewHolder = new SearchViewHolder();
					
					makeFreeTextHolder(position, convertView, viewHolder);
				} else {
					convertView = getActivity().getLayoutInflater().inflate(
							R.layout.searchlist_dropdown_field, null);
					
					spinAdapter = new ArrayAdapter<String>(convertView.getContext(), android.R.layout.simple_spinner_item, mSpinnerList);
					spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner = (Spinner) convertView.findViewById(R.id.spinner_search);
					
					spinner.setAdapter(spinAdapter);
					
					
					viewHolder = new SearchViewHolder();
					makeSpinnerHolder(position, convertView, viewHolder,
							spinner);
				}
				mViewHolderList.add(viewHolder);
				viewHolder.convertView = convertView;

				
			} else {
				if(position >= mViewHolderList.size()) return getView(position, null, parent);
				viewHolder = mViewHolderList.get(position);
				convertView = viewHolder.convertView;
				
				if(viewHolder.isDropDown) {				
					viewHolder.textView.setText(mAnnotationNamesList.get(viewHolder.position));
					viewHolder.spinner.setSelection(viewHolder.selectedPosition);
					viewHolder.checkBox.setChecked(viewHolder.isChecked);
				} else {
					viewHolder.editText.setText(viewHolder.freetext);
					viewHolder.editText.clearFocus();
					viewHolder.textView.setText(mAnnotationNamesList.get(viewHolder.position));
					viewHolder.checkBox.setChecked(viewHolder.isChecked);
				}
			}
			return convertView;
		}

		/**
		 * Gets the corresponding spinner from the layout and setup the
		 * viewholder for the spinner. Also sets onCheckedChangedListener for
		 * the checkBox connected to the layout.
		 * 
		 * @param position for the actual view
		 * @param convertView the view of the selection-field
		 * @param viewHolder the viewholder to create and setup for the view
		 * @param spinner the spinner that is a part of the selection-field
		 */
		private void makeSpinnerHolder(int position, View convertView,
				SearchViewHolder viewHolder, Spinner spinner) {
			viewHolder.textView = (TextView) convertView.findViewById(R.id.lbl_spinner_search);
			viewHolder.textView.setText(mAnnotations.get(position).getName());
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check_dropdown_search);
			viewHolder.checkBox.setTag(viewHolder);
			viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				/**
				 * Marks the Selection_field as marked or unmarked. depending
				 * on the users clicked choice.
				 */
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SearchViewHolder vh = (SearchViewHolder) buttonView.getTag();
					vh.isChecked = isChecked;
					
				}
			});
			viewHolder.spinner = (Spinner) convertView.findViewById(R.id.spinner_search);
			viewHolder.isDropDown = true;
			viewHolder.position = position;
			
			spinner.setTag(viewHolder);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,
						View view, int position, long id) {
					SearchViewHolder vh = (SearchViewHolder) parent.getTag();
					vh.selectedPosition = position;
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {							
				}
				
			});
		}

		/**
		 * Gets the corresponding textView from the layout and setup the
		 * viewholder for the textView. Also sets onCheckedChangedListener for
		 * the checkBox connected to the layout.
		 * 
		 * @param position the position for the actual view
		 * @param convertView the view of the layout inflated
		 * @param viewHolder the viewholder to create and setup for the view
		 */
		private void makeFreeTextHolder(int position, View convertView,
				SearchViewHolder viewHolder) {
			viewHolder.isDropDown = false;
			viewHolder.textView = (TextView) convertView.findViewById(R.id.lbl_field_search);
			viewHolder.textView.setText(mAnnotations.get(position).getName());
			viewHolder.position = position;
			
			viewHolder.editText = (EditText) convertView.findViewById(R.id.txtf_search_hint);
			viewHolder.editText.setHint(mAnnotations.get(position).getName());

			viewHolder.editText.addTextChangedListener(new TheTextWatcher(viewHolder));					
			
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check_search_hint);
			viewHolder.checkBox.setTag(viewHolder);
			viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SearchViewHolder vh = (SearchViewHolder) buttonView.getTag();
					vh.isChecked = isChecked;
					
				}
			});
			viewHolder.editText.clearFocus();
		}

	}
	
	/**
	 * Implementation of TextWatcher for the Genomizer android application, 
	 * collects user input from freetextfields. 
	 * 
	 * @author Anders Lundberg, dv12alg 
	 * @author Erik Åberg, c11ean
	 */
	private class TheTextWatcher implements TextWatcher {

		private SearchViewHolder viewHolder;
		
		/**
		 * Creates a new TheTextWatcher object.
		 * 
		 * @param viewHolder the viewHolder that contains the EditText that
		 * is connected with the textWatcher.
		 */
		public TheTextWatcher(SearchViewHolder viewHolder) {
			this.viewHolder = viewHolder;
		}
		
		/**
		 * After text is changed, updates the freeText field in the viewHolder
		 * connected to the specific field.
		 */
		@Override
		public void afterTextChanged(Editable s) {
			viewHolder.freetext = s.toString();	
		}
		
		/**
		 * Unimplemented
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}
		
		/**
		 * Unimplemented
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
		
	}
	
	/**
	 * AsyncTask for connecting to the server and generate annotations for the
	 * search view in the Genomizer android application.
	 * 
	 * @author Anders Lundberg, dv12alg 
	 * @author Erik Åberg, c11ean
	 *
	 */
	private class AnnotationsTask extends AsyncTask<Void, Void, Void> {
		
		private IOException except;

		/**
		 * Connects to the server, collects annotation data from the database
		 * and sets the retrieved values into corresponding lists.
		 */
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				mAnnotations = ComHandler.getServerAnnotations();
				mAnnotationNamesList = new ArrayList<String>();
				
				for(Annotation annotation : mAnnotations) {
					mAnnotationNamesList.add(annotation.getName());	
				}
				
			} catch (IOException e) {
				except = e;
				SingleFragmentActivity act = (SingleFragmentActivity) getActivity();
				act.relogin();
			}
			return null;
			
		}
		
		/**
		 * After the collection of data is complete setup the adapter with the
		 * information about annotations found in the database contacted.
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (except == null) {
				setupListView();
			} else {
				toastMessage(CONNECTION_ERROR);
				except.printStackTrace();
				except = null;
			}
			
		}
		
	}

}
