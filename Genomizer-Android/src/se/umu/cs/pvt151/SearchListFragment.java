package se.umu.cs.pvt151;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Fragment which will display search options for the Genomizer app, displays
 * the current annotations used by the Genomizer database and presents them to 
 * the user for searching.
 * 
 * @author Anders Lundberg, dv12alg
 *
 */
public class SearchListFragment extends ListFragment {

	private ArrayList<String> mAnnotationList;
	private CopyOnWriteArrayList<String[]> mSearchList;
	private HashMap<Integer, EditText> mTextFields;

	/**
	 * Defines search and textfield lists.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSearchList = new CopyOnWriteArrayList<String[]>();
		mTextFields = new HashMap<Integer, EditText>();
		populateAnnotation();
	}
	
	/**
	 * Attaches an ArrayAdapter implementation on the current listview
	 * and a footer with a search button to conclude the search.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SearchListAdapter adapter;
		View footer = generateFooter();
		
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
		String[] values = new String[] { "Experiment Id", "Pubmed Id",
				"Type of data", "Species", "Genome release", "Cell-line",
				"Development stage", "Sex", "Tissue", "Antigen name",
				"Antigen symbol", "Antibody" };
		mAnnotationList = new ArrayList<String>();
		for (String s : values) {
			mAnnotationList.add(s);
		}
	}

	/**
	 * Generates a search button with a onClickListener attached to it.
	 * 
	 * @param footer the view where the button is placed
	 */
	private void generateSearchButton(View footer) {
		Button searchButton = (Button) footer
				.findViewById(R.id.btn_search_footer);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO should work with connection
				// try {
				// ComHandler.search(searchList);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (ConnectionException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				Intent i = new Intent(getActivity(),
						ExperimentListActivity.class);
				startActivity(i);

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
		protected EditText annotation;
		protected CheckBox markCheckBox;
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

		public SearchListAdapter(ArrayList<String> storeList) {
			super(getActivity(), 0, storeList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			searchViewHolder viewHolder = null;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.searchlist_field, null);

				viewHolder = new searchViewHolder();
				viewHolder.annotation = (EditText) convertView
						.findViewById(R.id.txtf_search_hint);
				mTextFields.put(position, viewHolder.annotation);
				viewHolder.markCheckBox = (CheckBox) convertView
						.findViewById(R.id.check_search_hint);

				viewHolder.markCheckBox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								int pos = (Integer) buttonView.getTag();

								if (isChecked) {
									addToSearchList(pos);
								} else {
									removeFromSearchList(pos);
								}
							}
						});
				convertView.setTag(viewHolder);
				convertView.setTag(R.id.txtf_search_hint, 
						viewHolder.annotation);
				convertView.setTag(R.id.check_search_hint,
						viewHolder.markCheckBox);
			} else {
				viewHolder = (searchViewHolder) convertView.getTag();
			}

			viewHolder.markCheckBox.setTag(position);
			viewHolder.annotation.setHint(mAnnotationList.get(position)
					.toString());

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
		String text = mTextFields.get(pos).getText().toString();
		String[] search = null;
		if (text.length() > 0) {
			search = new String[2];
			search[0] = mAnnotationList.get(pos);
			search[1] = text;
			mSearchList.add(search);
		}
	}
	
	/**
	 * Removes the selected search from the searchList.
	 * 
	 * @param pos , position of the unchecked annotationField
	 */
	private void removeFromSearchList(int pos) {
		for (String[] s : mSearchList) {
			if (s[0].equals(mAnnotationList.get(pos))) {
				mSearchList.remove(s);
			}
		}
	}
}
