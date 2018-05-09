package nwmissouri.edu.staticclasses;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by S521670 on 7/8/2015.
 */

/**
 * This class is used to write toast messages.
 */
public class ToastMessage {
    /**
     * Static method to write toast messages.
     * @param message
     * @param activity
     */
    public static void message(String message,Activity activity){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
