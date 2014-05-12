/**
 * 
 */
package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;

/**
 * @author Anders
 *
 */
public class ConvertActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		
		return new ConvertFragment();
	}

}
