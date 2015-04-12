package com.example.bobthebuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View; 
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MalibuCountDownTimer countDownTimer;
	private boolean timerHasStarted = false;
	private Button startB;
	private TextView text;
	
	private NotificationManager mNotificationManager;
	private int notificationID = 100;
	private int numMessages = 0;

	private final long startTime = 1000*60*30;
	private final long interval = 1000;
	
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "bobPrefs" ;
	public static final String BuiltScore = "builtKey"; 
	public static final String UnBuiltScore = "unBuiltKey";
	private String bobBuildScore = "0";
	private String bobUnBuildScore = "0";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startB = (Button) findViewById(R.id.startButton);

		text = (TextView) this.findViewById(R.id.timerValue);
		countDownTimer = new MalibuCountDownTimer(startTime, interval);
		text.setText(convertMilliToTimeString(startTime));
          
    
        //Code for returning on the application after screen unlocked
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        
        //Retrieve Stored Data
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(BuiltScore))
        {
        	bobBuildScore = sharedpreferences.getString(BuiltScore, "");
        }
        if (sharedpreferences.contains(UnBuiltScore))
        {
        	bobUnBuildScore = sharedpreferences.getString(UnBuiltScore, "");
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
       	Log.v("$$$$$$", "In Method: onPause()");
    
    }
    
    @Override
    public void onStop() {
    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        boolean screenOn;
        screenOn = pm.isScreenOn();
    	super.onStop();
    	if (screenOn) {
    	countDownTimer.cancel();
		timerHasStarted = false;
		text.setText(getText(R.string.timerInitVal));
		startB.setText(getText(R.string.startButtonLabel));
		// Store values between instances here
	       	Editor editor = sharedpreferences.edit();
	       bobUnBuildScore = Integer.toString((Integer.parseInt(bobUnBuildScore) + 1));
	        editor.putString(UnBuiltScore, bobUnBuildScore);
	        Log.v("$$$$$$", "unBuiltScore Value:"+bobUnBuildScore);
        // Commit to storage
	        editor.commit();
    	//tToast("onStop.");
		displayNotification();
    	Log.v("$$$$$$", "In Method: onStop()");
    	}
    }
    
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //tToast("onResume.");
        Log.v("$$$$$$", "In Method: onResume()");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(BuiltScore))
        {
           //tToast(sharedpreferences.getString(BuiltScore, ""));
        }
        if (sharedpreferences.contains(UnBuiltScore))
        {
           //tToast(sharedpreferences.getString(UnBuiltScore, ""));
        }
    }
        
    
    
	public void startTimer(View v)	{
		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
			startB.setText(R.string.duringBuildButtonLabel);
			//startB.setText(getText(R.string.duringBuildButtonLabel));
		}
		else {
			alertMessage();
		}
	}
    	
    // CountDownTimer class
 	public class MalibuCountDownTimer extends CountDownTimer {

 		public MalibuCountDownTimer(long startTime, long interval)	{
 						super(startTime, interval);
 		}

 		@Override
 		public void onFinish(){
 	        // Store values between instances here
 	       	Editor editor = sharedpreferences.edit();
 	       bobBuildScore = Integer.toString((Integer.parseInt(bobBuildScore) + 1));
 	        editor.putString(BuiltScore, bobBuildScore);
 	       Log.v("$$$$$$", "BuiltScore Value:"+bobBuildScore);     
 	        // Commit to storage
 	        editor.commit();
 			text.setText(getText(R.string.timeUpMessage));
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
 	
 	public void alertMessage() {
         DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                      switch (which) {
                      case DialogInterface.BUTTON_POSITIVE:
                             // Yes button clicked
                    	  		countDownTimer.cancel();
              					timerHasStarted = false;
              					text.setText(getText(R.string.timerInitVal));
              					startB.setText(getText(R.string.startButtonLabel));
              				// Store values between instances here
              		 	       	Editor editor = sharedpreferences.edit();
              		 	       	bobUnBuildScore = Integer.toString(Integer.parseInt(bobUnBuildScore)+1);
              		 	        editor.putString(UnBuiltScore, bobUnBuildScore);
              		 	        Log.v("$$$$$$", "UnBuiltScore Value:"+bobUnBuildScore);
           		 	        // Commit to storage
              		 	        editor.commit();            					
                             break;
                      case DialogInterface.BUTTON_NEGATIVE:
                             // No button clicked
                             // do nothing                             
                             break;
                      }
                }
         	};

         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("You sure want to kill the cute little Bob ?")
                      .setPositiveButton("Yes, destroy the building also!!", dialogClickListener)
                      .setNegativeButton("No, can think again!!", dialogClickListener).show();
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
    
    protected void displayNotification() {
        Log.v("Start", "notification");

        /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =  new NotificationCompat.Builder(this);	

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.woman);

        /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);
        
        /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);

        /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
           stackBuilder.getPendingIntent(
              0,
              PendingIntent.FLAG_UPDATE_CURRENT
           );
        //For canceling the notification on click 
        mBuilder.setAutoCancel(true);
        
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID, mBuilder.build());
        
     }
    
}
