package se.umu.cs.pvt151;

import se.umu.cs.pvt151.com.ComHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class SettingsFragment extends ListFragment {

	private int prevActive = 0;
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    String[] values = new String[] { "http://scratchy.cs.umu.se:7000/", "http://genomizer.apiary-mock.com/",
	    		"http://scratchy.cs.umu.se:7050/", "http://scratchy.cs.umu.se:8050/", 
	    		"http://scratchy.cs.umu.se:60000/"};
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_single_choice, values);	        
	    
	    setListAdapter(adapter);
	    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    for(int i = 0; i < values.length; i++) {
	    	if(ComHandler.getServerURL().compareTo(values[i]) == 0) {
	    		getListView().setItemChecked(i, true);
	    		prevActive = i;
	    	}
	    }	    
	    
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  ((CheckedTextView) l.getChildAt(prevActive)).setChecked(false);
		  CheckedTextView item = ((CheckedTextView) l.getChildAt(position));		  
		  item.setChecked(true);		  
		  prevActive = position;
		  ComHandler.setServerURL((String) getListAdapter().getItem(position));
	  }
}