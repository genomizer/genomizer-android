package se.umu.cs.pvt151.com;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * This class main purpose is to anywhere in the
 * application be able to create and view new toasts and verify the connection.
 * 
 * @author erik c11ean
 *
 */
public class Genomizer extends Application {

	private static Context context;
	
	
	/**
	 * When this object is created get the applications context.
	 * 
	 */
    public void onCreate(){
        super.onCreate();
        Genomizer.context = getApplicationContext();
    }
    

    /**
     * Returns the context of the application
     * 
     * @return context
     */
    public static Context getAppContext() {
        return Genomizer.context;
    }
    
    
    /**
     * Creates and visualizes a toast with the parameter 
     * msg as text.
     * 
     * @param msg
     */
    public static void makeToast(final String msg) {    
    	Handler toastHandler = new Handler(context.getMainLooper());
    	Runnable toastRunnable = new Runnable() {
    	
			@Override
			public void run() {
				Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
				toast.setGravity( Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 180);
				toast.show();				
			}
    	};
    	toastHandler.post(toastRunnable);
    }
    
    
    
    
    /**
	 * Verify that the phone currently have an internet connection.
	 * @return true if online false otherwise
	 */
    public static boolean isOnline() {
    	ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
	    return connectManager.getActiveNetworkInfo() != null && 
	       connectManager.getActiveNetworkInfo().isConnected();
    }
}
