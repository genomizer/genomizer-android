package se.umu.cs.pvt151;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkSpaceFragment extends Fragment {
	
	
	public void onCreate(Bundle onSavedInstanceState) {
		
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_work_space, parent, false);
		
		return v;
	}
}
