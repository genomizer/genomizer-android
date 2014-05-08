package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Fragment which will display search options for the Genomizer app, displays
 * the current annotations used by the Genomizer database and presents them to 
 * the user for searching.
 * 
 * @author Anders Lundberg, dv12alg
 *
 */
public class SearchListFragment extends ListFragment {

	protected static final String ANNOTATION = "Annotation";
	protected static final String VALUE = "Value";
	protected static final String SEARCH_MAP = "searchMap";
	private ArrayList<String> mAnnotationNamesList;
	private Button searchButton;
	private ArrayList<String> mSpinnerList;
	private HashMap<String, String> mSearchList;
	private ArrayList<Annotation> mAnnotations;
	private boolean waitServerAnnotations = true;
	private ArrayList<SearchViewHolder> viewHolderList = new ArrayList<SearchViewHolder>();
	
	
	/**
	 * Defines search and textfield lists.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSearchList = new HashMap<String, String>();
		
	}
	
	/**
	 * Attaches an ArrayAdapter implementation on the current listview
	 * and a footer with a search button to conclude the search.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<String> adapter;
		View footer = generateFooter();
		populateAnnotation();
		while(waitServerAnnotations);
		waitServerAnnotations = true;
		generateSearchButton(footer);
		adapter = new SearchListAdapter(mAnnotationNamesList);
		adapter.setNotifyOnChange(true);
		setListAdapter(adapter);
	}

	/**
	 * Populates the annotation fields while there is no direct uplonk to the
	 * server, should be removed and replaced after the server can respond with
	 * actual annotations.
	 */
	private void populateAnnotation() {
		
			new Thread(new Runnable() {
				@Override
			
				public void run() {
					try {
						mAnnotations = ComHandler.getServerAnnotations();
						mAnnotationNamesList = new ArrayList<String>();
						
						for(Annotation annotation : mAnnotations) {
							mAnnotationNamesList.add(annotation.getName());	
						}
						waitServerAnnotations = false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}).start();

		
	}

	/**
	 * Generates a search button with a onClickListener attached to it.
	 * 
	 * @param footer the view where the button is placed
	 */
	private void generateSearchButton(View footer) {
		searchButton = (Button) footer
				.findViewById(R.id.btn_search_footer);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ExperimentListActivity.class);
				String key = null;
				String value = null;
				
				HashMap<String, String> search = new HashMap<String, String>();
				
				for (SearchViewHolder vh : viewHolderList) {
					if (vh.isChecked && vh.isDropDown) {
						key = vh.textView.getText().toString();
						for (Annotation annotation : mAnnotations) {
							if (annotation.getName().equals(key)) {
								value = annotation.getValue().get(vh.selectedPosition);
							}
						}
						
					} else if (vh.isChecked) {
						if (vh.freetext != null && vh.freetext.length() > 0) {
							key = vh.textView.getText().toString();
							value = vh.freetext;
							
						}
					}
					if(key != null & value != null) {
						search.put(key, value);
					}
					
				}
				
				Log.d("SEARCH", "Search: " + search.toString());
				
				intent.putExtra(SEARCH_MAP, search);			
				startActivity(intent);				

			}
		});
	}

	/**
	 * Generates footer view for the searchList Fragment.
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
	 * Static searchViewHolder class for keeping items in the searchList in 
	 * memory when scrolled out of the screen.
	 * 
	 * @author Anders Lundberg, dv12alg
	 *
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
	 * Will use the searchViewHolder as memory of the list scrolled out of view, 
	 * also attaches onCheckedChangedListener on each of the checkboxes used 
	 * for the search-fields.
	 * 
	 * @author Anders Lundberg, dv12alg
	 *
	 */
	private class SearchListAdapter extends ArrayAdapter<String> {

		public SearchListAdapter(ArrayList<String> annotationNames) {
			super(getActivity(), 0, annotationNames);
		}

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
				viewHolderList.add(viewHolder);
				viewHolder.convertView = convertView;
//				convertView.setTag(viewHolder);	

				
			} else {
				if(position >= viewHolderList.size()) return getView(position, null, parent);
				viewHolder = viewHolderList.get(position);
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
		 * @param position
		 * @param convertView
		 * @param viewHolder
		 * @param spinner
		 */
		private void makeSpinnerHolder(int position, View convertView,
				SearchViewHolder viewHolder, Spinner spinner) {
			viewHolder.textView = (TextView) convertView.findViewById(R.id.lbl_spinner_search);
			viewHolder.textView.setText(mAnnotations.get(position).getName());
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check_dropdown_search);
			viewHolder.checkBox.setTag(viewHolder);
			viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
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
		 * 
		 * @param position
		 * @param convertView
		 * @param viewHolder
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
	 * 
	 * @author 
	 *
	 */
	private class TheTextWatcher implements TextWatcher {

		private SearchViewHolder viewHolder;

		public TheTextWatcher(SearchViewHolder viewHolder) {
			this.viewHolder = viewHolder;
		}
		@Override
		public void afterTextChanged(Editable s) {
			viewHolder.freetext = s.toString();
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
		
	}

}
