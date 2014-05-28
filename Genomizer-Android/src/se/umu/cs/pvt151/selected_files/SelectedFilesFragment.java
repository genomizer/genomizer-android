package se.umu.cs.pvt151.selected_files;


import se.umu.cs.pvt151.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/**
 * This Fragment class is meant to be a view containing a number of file
 * lists for the user. Each file list is to be visualized in a tab.
 * The class uses a TabHost with implemented swipe-functionality.
 * New tabs must be added in the method initializeTabs and in the
 * TabsPagerAdapter class.
 * 
 * @author Rickard dv12rhm
 *
 */
public class SelectedFilesFragment extends Fragment implements OnTabChangeListener, OnPageChangeListener{

	private TabHost tabHost;
	private ViewPager viewPager;
	private LayoutInflater inflater = null;
	private ViewGroup parent = null;


	/**
	 * Called by the system when this fragment is created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	/**
	 * Inflates the view of this fragment.
	 * 
	 * @return The inflated view of the layout
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.parent = parent;
		View v = initView();

		return v;
	}


	/**
	 * This method initializes the viewPager and the TabsPagerAdapter.
	 * 
	 * @return View
	 */
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
	
	
	/**
	 * When this fragments activity is created, set current tab
	 * to the tab on index 0 so that the tabs view will be initialized.
	 * 
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tabHost.setCurrentTab(0);	
	}
	
	
	/**
	 * Creates and initializes the tabs for the TabHost.
	 * More tabs may be added by adding them here and in the
	 * TabsPagerAdapter.
	 * 
	 */
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
	}


	/**
	 * Unimplemented
	 */
	public void onPageScrollStateChanged(int e) {

	}


	/**
	 * Whenever the ViewPager is changed, notify the 
	 * TabHost.
	 * 
	 */
	public void onPageScrolled(int e1, float e2, int e3) {
		int pos = viewPager.getCurrentItem();
		tabHost.setCurrentTab(pos);
	}


	/**
	 * Unimplemented
	 */
	public void onPageSelected(int e) {

	}


	/**
	 * Whenever the tabs is changed, notify the 
	 * viewPager.
	 * 
	 */
	public void onTabChanged(String e) {
		int pos = tabHost.getCurrentTab();
		viewPager.setCurrentItem(pos);
	}
}
