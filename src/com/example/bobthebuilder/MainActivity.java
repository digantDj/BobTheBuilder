package com.example.bobthebuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.View; 
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity  implements OnClickListener {

	private MalibuCountDownTimer countDownTimer;
	private boolean timerHasStarted = false;
	private Button startB;
	private TextView text;

	private final long startTime = 1000*60*30;
	private final long interval = 1000;
	
    private BroadcastReceiver mReceiver;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startB = (Button) this.findViewById(R.id.startButton);
		startB.setOnClickListener(this);

		text = (TextView) this.findViewById(R.id.timerValue);
		countDownTimer = new MalibuCountDownTimer(startTime, interval);
		text.setText(convertMilliToTimeString(startTime));
          
    
        //Code for returning on the application after screen unlocked
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        boolean screenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //screenOn = pm.isInteractive();
        	screenOn = pm.isScreenOn();
        } else {
            screenOn = pm.isScreenOn();
        }

        if (screenOn) {
            // Screen is still on, so do your thing here
        	Log.v("$$$$$$", "In Method: onPause()");
        }
        
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	countDownTimer.cancel();
		timerHasStarted = false;
		text.setText("30:00");
		startB.setText("Build");
    	tToast("onStop.");
    	Log.v("$$$$$$", "In Method: onStop()");
    }
    
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //tToast("onResume.");
        Log.v("$$$$$$", "In Method: onResume()");
    }
    
    
    @Override
	public void onClick(View v)	{
		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
			startB.setText("Kill Bob");
		}
		else {
			countDownTimer.cancel();
			timerHasStarted = false;
			text.setText("30:00");
			startB.setText("Build");
		}
	}
    	
    // CountDownTimer class
 	public class MalibuCountDownTimer extends CountDownTimer {

 		public MalibuCountDownTimer(long startTime, long interval)	{
 						super(startTime, interval);
 		}

 		@Override
 		public void onFinish(){
 						text.setText("Time's up!");
 		}

 		@Override
 		public void onTick(long millisUntilFinished) {
 						text.setText(convertMilliToTimeString(millisUntilFinished));
 		}
 	}

 	public String convertMilliToTimeString(long milliseconds) {
 			NumberFormat f = new DecimalFormat("00");
 			int seconds = (int) ((milliseconds / 1000) % 60);
			int minutes = (int) ((milliseconds / 1000) / 60);
 			String finalString = f.format(minutes) + ":" + f.format(seconds);
			return finalString;
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
    
    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }
}
