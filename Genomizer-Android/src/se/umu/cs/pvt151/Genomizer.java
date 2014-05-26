package se.umu.cs.pvt151;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class Genomizer extends Application {

	private static Context context;

    public void onCreate(){
        super.onCreate();
        Genomizer.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Genomizer.context;
    }
    
    public static void makeToast(final String msg) {    	
    	Handler toastHandler = new Handler(context.getMainLooper());
    	Runnable toastRunnable = new Runnable() {

			@Override
			public void run() {
				Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
				toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();				
			}
    		
    	};
    	toastHandler.post(toastRunnable);
    }
    
}
