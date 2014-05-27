package se.umu.cs.pvt151.selected_files;


import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class SelectedFilesFragment extends Fragment implements OnTabChangeListener, OnPageChangeListener{

	private TabHost tabHost;
	private ViewPager viewPager;
	private LayoutInflater inflater = null;
	private ViewGroup parent = null;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.parent = parent;
		View v = initView();

		return v;
	}


	private View initView() {
		View v = inflater.inflate(R.layout.fragment_selected_files_swipe, parent, false);

		TabsPagerAdapter adapter = new TabsPagerAdapter(getActivity().getSupportFragmentManager());

		viewPager = (ViewPager) v.findViewById(R.id.viewpagerSwipe);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);

		tabHost = (TabHost) v.findViewById(R.id.tabhostSwipe);
		
		initializeTabs();
		
		tabHost.setCurrentTab(1);
		return v;
	}
	
	
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tabHost.setCurrentTab(0);	
	}
	
	
	private void initializeTabs() {
		tabHost.setOnTabChangedListener(this);
		tabHost.setup();

		TabHost.TabSpec spec = tabHost.newTabSpec("RAW");
		spec.setContent(R.id.viewpagerSwipe);
		spec.setIndicator("RAW");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("PROFILE");
		spec.setContent(R.id.viewpagerSwipe);
		spec.setIndicator("PROFILE");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("REGION");
		spec.setContent(R.id.viewpagerSwipe);
		spec.setIndicator("REGION");
		tabHost.addTab(spec);

		//Add new arbitrary tabs here
		
//		spec = tabHost.newTabSpec("RESULT");
//		spec.setContent(R.id.viewpagerSwipe);
//		spec.setIndicator("RESULT");
//		tabHost.addTab(spec);
	}


	@Override
	public void onPageScrollStateChanged(int e) {

	}


	@Override
	public void onPageScrolled(int e1, float e2, int e3) {
		int pos = viewPager.getCurrentItem();
		tabHost.setCurrentTab(pos);
	}


	@Override
	public void onPageSelected(int e) {

	}


	@Override
	public void onTabChanged(String e) {
		int pos = tabHost.getCurrentTab();
		viewPager.setCurrentItem(pos);
	}
}
