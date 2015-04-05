package com.example.bobthebuilder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View; 
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;  


public class MainActivity extends Activity {

	// public var  
    private TextView timerValue;
    private BroadcastReceiver mReceiver;
    private Button timerButton;
    
    private long startTime = 0L;
    
    private Handler customHandler = new Handler();
    
    long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // findViewById = Finds a view that was identified by the id attribute  
        // from the XML that was processed in onCreate(Bundle).  
        // (EditText) = typecast  
        timerValue = (TextView) findViewById(R.id.timerValue);
        
    
        //Code for returning on the application after screen unlocked
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onDestroy()
    {
          super.onDestroy();
          Log.v("$$$$$$", "In Method: onDestroy()");
         
          if (mReceiver != null)
          {
                unregisterReceiver(mReceiver);
                mReceiver = null;
          }          
       
    }
    
    @Override
    public void  onSaveInstanceState(Bundle outState)
    {
          Log.v("$````$", "In Method: onSaveInstanceState()");
          //if necessary,set a flag to check whether we have to restore or not
          //handle necessary savings…
    }

    @Override
    public void onRestoreInstanceState(Bundle inState)
    {
          Log.v("$````$", "In Method: onRestoreInstanceState()");
          //if any saved state, restore from it…
    }
    
    
    
    /* 
     * Will be executed by clicking on the calculate button because we assigned 
     * "startTimer" to the "onClick" Property! 
     */  
    public void startTimer(View view) {  
    	
        // if the text field is empty show the message "enter a valid number"  
        if (!timerValue.getText().toString().equals("30:00")) {  
            // Toast = focused floating view that will be shown over the main  
            // application  
        	Log.v("$````$", "In Method: "+timerValue.getText());
            Toast.makeText(this, "Sorry!! Timer already started", Toast.LENGTH_LONG)  
                    .show();  
        } else {  
            //Start the timer
        	Log.v("$````$", "Starting Timer");
        	startTime = SystemClock.uptimeMillis();
			customHandler.postDelayed(updateTimerThread, 0);
              
        }  
    }
    
    private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			
			updatedTime = 30 - (timeSwapBuff - timeInMilliseconds);
			
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			timerValue.setText("" + mins + ":" + String.format("%02d", secs));
			//timerValue.setText(SystemClock.uptimeMillis()+ " + " + startTime);
			
			customHandler.postDelayed(this, 0);
		}

	};
  
    
}
