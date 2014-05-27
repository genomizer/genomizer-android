package se.umu.cs.pvt151.search;

import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.ExperimentListActivity;
import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.layout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SearchPubmedFragment extends Fragment {

	private EditText pubmedInput;
	private String origionalPubquery = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_pubmed, container, false);
		initViewObjects(view);
		return view;
	}

	private void initViewObjects(View view) {
		pubmedInput = (EditText) view.findViewById(R.id.search_pubmed_input_field);
		TextView pubmedText = (TextView) view.findViewById(R.id.search_pubmed_textview);
		pubmedText.setText("Optional Pubmed-style edit");
		origionalPubquery = getActivity().getIntent().getExtras().getString("PubmedQuery");		
		pubmedInput.setText(constructUserFriendlyQueryStringFromOriginalSearchQuery(origionalPubquery));
	}
	
	
	private String constructUserFriendlyQueryStringFromOriginalSearchQuery(String pubStr) {
		pubStr = pubStr.replace("%5B", "[");
		pubStr = pubStr.replace("%5D", "]");
		char[] chrs = new char[pubStr.length()];
		pubStr.getChars(0, pubStr.length(), chrs, 0);
		
		for(int i = 0; i < chrs.length; i++) {
			if(chrs[i] == '+') {
				if(chrs[i - 1] == ']') {
					chrs[i] = '\n';
					
				} else {
					if(checkIfStringIsSubstringOfCharArray(i - 3, i, chrs, "AND")) {
						chrs[i] = '\n';
					} else if(checkIfStringIsSubstringOfCharArray(i - 2, i, chrs, "OR")) {
						chrs[i] = '\n';
					} else if(checkIfStringIsSubstringOfCharArray(i - 3, i, chrs, "NOT")) {
						chrs[i] = '\n';						
					}
				}
			}
		}
		pubStr = String.valueOf(chrs);
		pubStr = pubStr.replace("+", " ");
				
		return pubStr;
	}
	
	private boolean checkIfStringIsSubstringOfCharArray(int start, int end, char[] chars, String str) {
		int k = 0;
		for(int i = start; i < end; i++) {
			if(str.charAt(k) == chars[i]) k++;
		}
		if(k == end - start) return true;
		return false;
	}
	
	private String recreateSearchQueryFromUserInput() {
		StringBuilder sb = new StringBuilder();
		String str = pubmedInput.getText().toString().replaceAll("\n", "");
		str = str.replace(" ", "+");
		
		boolean bracketMatching = false;
		for(int i = 0, j = 0; i < str.length(); i++) {
			if(str.charAt(i) == ']') {
				
				String subStr = str.substring(j, i + 1);
				subStr = subStr.replace("[", "%5B");
				subStr = subStr.replace("]", "%5D");
				sb.append(subStr);
				
				if(i == str.length() - 1) {
					break;
				}
				sb.append("+");
				bracketMatching = true;
				
				continue;
			}
			
			if(bracketMatching) {				
				if(str.substring(i, i +3).equals("AND")) {
					sb.append("AND+");
					j = i + 3;
					bracketMatching = false;
				} else if(str.substring(i, i + 2).equals("OR")) {
					sb.append("OR+");
					j = i + 2;
					bracketMatching = false;
				} else if(str.substring(i, i + 3).equals("NOT")) {
					sb.append("NOT+");
					j = i + 3;
					bracketMatching = false;
				}
				
			}
			
		}
		//TODO check for double ++ etc
		
		return sb.toString();
	}
	
	private void substituteLowerCaseConnectives() {
		String str = pubmedInput.getText().toString();
		str = str.replaceAll("or", "OR");
		str = str.replaceAll("and", "AND");
		str = str.replaceAll("not", "NOT");
		pubmedInput.setText(str);
	}
	
	public void initExperimentList() {
		
		ArrayList<String> annotation = getActivity().getIntent().getExtras().getStringArrayList("Annotations");
		@SuppressWarnings("unchecked")
		HashMap<String, String> searchResults = (HashMap<String, String>) getActivity().getIntent().getExtras().getSerializable("searchMap");
		Intent intent = new Intent(getActivity(),
				ExperimentListActivity.class);
		
		substituteLowerCaseConnectives();
		String editedPubstring = null;
		try {
		editedPubstring = recreateSearchQueryFromUserInput();
		} catch(StringIndexOutOfBoundsException e) {
			Toast.makeText(getActivity(), "Malformed pubmed Query! Did you remove logical connectives?", Toast.LENGTH_LONG).show();
			return;
		}
		Log.d("SearchPubmedFragment", "Original pubstr: " + origionalPubquery);
		Log.d("SearchPubmedFragment", "New pubstr: " + editedPubstring);
		Log.d("SearchPubmedFragment", "New vs Original pubstr (str compare): " + origionalPubquery.compareTo(editedPubstring));
		if(editedPubstring != null) {
			intent.putExtra("PubmedQuery", editedPubstring);
		} else {
			intent.putExtra("PubmedQuery", origionalPubquery);
		}
		intent.putExtra("Annotations", annotation);
		intent.putExtra("searchMap", searchResults);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		getActivity().overridePendingTransition(0,0);
		
		
		
	}
}
