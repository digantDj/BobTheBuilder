/**
 * 
 */
package com.example.bobthebuilder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author digantDj
 *
 */
public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
          if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
          {    
                Log.v("$$$$$$", "In Method:  ACTION_SCREEN_OFF");
                // onPause() will be called.
          }
          else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
          {
                Log.v("$$$$$$", "In Method:  ACTION_SCREEN_ON");
//onResume() will be called.

//Better check for whether the screen was already locked
//if locked, do not take any resuming action in onResume()

                //Suggest you, not to take any resuming action here.       
          }
          else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT))
          {
                Log.v("$$$$$$", "In Method:  ACTION_USER_PRESENT");
//Handle resuming events
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
          }

    }
}