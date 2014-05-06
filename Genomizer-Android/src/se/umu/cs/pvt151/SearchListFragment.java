package se.umu.cs.pvt151;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.ConnectionException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private ArrayList<String> mAnnotationList;
	private Button searchButton;
	private ArrayList<String> mSpinnerList;
	private HashMap<String, String> mSearchList;
	private ArrayList<Annotation> mAnnotations;
	private boolean fulingWait = true;

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
		generateSearchButton(footer);
		adapter = new SearchListAdapter(mAnnotationList);
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
						mAnnotationList = new ArrayList<String>();
						
						for(Annotation annotation : mAnnotations) {
							mAnnotationList.add(annotation.getName());	
						}
						fulingWait = false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ConnectionException e) {
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
				HashMap<Integer, String> annotations = new HashMap<Integer, String>();
				HashMap<Integer, String> value = new HashMap<Integer, String>();
				
				//TODO pass the searchlist to the experimentList fragment
				intent.putExtra(ANNOTATION, annotations);
				intent.putExtra(VALUE, value);
				Log.d("Experiment", "Search annotations: " + annotations.toString());
				Log.d("Experiment", "Search value: " + value.toString());
				
				startActivity(intent);
				
				Log.d("smurf", "Search: " + mSearchList.toString());

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
	static class searchViewHolder {
		protected TextView annotation;
		protected Spinner annotationValue;
		protected int selectedPosition;
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
			searchViewHolder viewHolder = null;
			ArrayAdapter<String> spinAdapter;
			Spinner spinner;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.searchlist_dropdown_field, null);
				final ArrayList<String> mSpinnerList = mAnnotations.get(position).getValue();
				
				
				spinAdapter = new ArrayAdapter<String>(convertView.getContext(), android.R.layout.simple_spinner_item, mSpinnerList);
				spinner = (Spinner) convertView.findViewById(R.id.spinner_search);
				spinner.setAdapter(spinAdapter);

				viewHolder = new searchViewHolder();
				viewHolder.annotation = (TextView) convertView.findViewById(R.id.lbl_spinner_search);
				viewHolder.annotationValue = (Spinner) convertView.findViewById(R.id.spinner_search);
				viewHolder.selectedPosition = 0;
				
				convertView.setTag(viewHolder);
								
				spinner.setTag(viewHolder);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						searchViewHolder viewHolder = (searchViewHolder) parent.getTag();
//						int annotationPosition = (Integer) parent.getTag();
						String annotation = viewHolder.annotation.getText().toString();
						String value = mSpinnerList.get(position);
						viewHolder.selectedPosition = position;
						Log.d("smurf", "Annotation: " + annotation + "\nValue: " + value + "\n-----------------");
						mSearchList.put(annotation, value);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}

					
				});
				
			} else {
				viewHolder = (searchViewHolder) convertView.getTag();
			}
			
			String annotationText = mAnnotationList.get(position);
			int selectionPos = viewHolder.selectedPosition;
			
			viewHolder.annotation.setText(annotationText);
			viewHolder.annotationValue.setSelection(selectionPos);
			
			return convertView;
		}

	}
	
	/**
	 * Add the selected search to searchList, with both annotation and the
	 * string input by user.
	 * 
	 * @param pos , position of the marked annotationField
	 */
	private void addToSearchList(int pos) {
//		String text = mTextFields.get(pos).getText().toString();
//		String[] search = null;
//		if (text.length() > 0) {
//			search = new String[2];
//			search[0] = mAnnotationList.get(pos);
//			search[1] = text;
//			mSearchList.add(search);
//		}
	}
	
	/**
	 * Removes the selected search from the searchList.
	 * 
	 * @param pos , position of the unchecked annotationField
	 */
	private void removeFromSearchList(int pos) {
//		for (String[] s : mSearchList) {
//			if (s[0].equals(mAnnotationList.get(pos))) {
//				mSearchList.remove(s);
//			}
//		}
	}
}
