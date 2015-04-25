package com.example.bobthebuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View; 
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MalibuCountDownTimer countDownTimer;
	private boolean timerHasStarted = false;
	private Button startB;
	private TextView timerText;
	private TextView statusText;
	private TextView buildNumberText;
	private TextView unBuildNumberText;
	private ImageView bobBubbleImage;
	private ImageView groundBubbleImage;
	private ImageView shovelImage;
	private ImageView truckImage;
	private TranslateAnimation animation;
	
	private NotificationManager mNotificationManager;
	private int notificationID = 100;
	private int numMessages = 0;

	private final long startTime = 1000*60*30;
	private final long interval = 1000;
	private boolean isInFront;
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "bobPrefs" ;
	public static final String BuiltScore = "builtKey"; 
	public static final String UnBuiltScore = "unBuiltKey";
	
	private String bobBuildScore = "0";
	private String bobUnBuildScore = "0";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
      //First time Splash Screen
        
        SharedPreferences ratePrefs = getSharedPreferences("First Update", 0);
        Log.v("SplashScreen Value","Splash Screen Value is "+ ratePrefs.getBoolean("FrstTime", false));
        if (!ratePrefs.getBoolean("FrstTime", false)) {
        	//Set flag for FirstTime
        Editor edit = ratePrefs.edit();
        edit.putBoolean("FrstTime", true);
        edit.commit();
        	//Launching the workflow screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intt;
     	       intt = new Intent(MainActivity.this, ScreenSlideActivity.class);
     	       startActivity(intt);
            }
        	}, 1000);
        
        }
        
        //Hiding Status Bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_main);
        
        startB = (Button) findViewById(R.id.startButton);
        statusText = (TextView) this.findViewById(R.id.statusTextView);
		timerText = (TextView) this.findViewById(R.id.timerValue);
		buildNumberText = (TextView) this.findViewById(R.id.buildNumberTextView);
		unBuildNumberText = (TextView) this.findViewById(R.id.unBuildNumberTextView);
		shovelImage = (ImageView) findViewById(R.id.shovelImageView);
		bobBubbleImage = (ImageView) findViewById(R.id.imageView1);
		groundBubbleImage = (ImageView) findViewById(R.id.imageView2);
		truckImage = (ImageView) findViewById(R.id.truckImageView);
		
		//Initialize Timer
		countDownTimer = new MalibuCountDownTimer(startTime, interval);
		timerText.setText(convertMilliToTimeString(startTime));
		
		//Initialize Images and hide whatever not required
        groundBubbleImage.setVisibility(View.INVISIBLE);
        truckImage.setVisibility(View.INVISIBLE);
        truckImage.setScaleX(-1);
		
        //Code for returning on the application after screen unlocked
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        
        //Retrieve Stored Data
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(BuiltScore))
        {
        	bobBuildScore = sharedpreferences.getString(BuiltScore, "");
        	buildNumberText.setText(bobBuildScore);
        }
        if (sharedpreferences.contains(UnBuiltScore))
        {
        	bobUnBuildScore = sharedpreferences.getString(UnBuiltScore, "");
        	unBuildNumberText.setText(bobUnBuildScore);
        }
        
        //Set Font for entire android project
        Typeface robotoThinFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto/Roboto-Thin.ttf");
        Typeface robotoCondensedFont = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed/RobotoCondensed-Regular.ttf");
        statusText.setTypeface(robotoCondensedFont);
        timerText.setTypeface(robotoThinFont);
        startB.setTypeface(robotoCondensedFont);
        
        //Animation shovel
        animation = new TranslateAnimation(0.0f, 0.0f,
          0.0f, 20.0f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(2);
        animation.setFillAfter(true);
        shovelImage.startAnimation(animation);
    }
    
    @Override
    public void onPause() {
        super.onPause();
       	Log.v("$$$$$$", "In Method: onPause()");
       	isInFront = false;
    }
    
    @Override
    public void onStop() {
    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    	isInFront = false;
        boolean screenOn;
        screenOn = pm.isScreenOn();
    	super.onStop();

    	if (screenOn && timerHasStarted) {
    	countDownTimer.cancel();
		timerHasStarted = false;
		timerText.setText(getText(R.string.timerInitVal));
		startB.setText(getText(R.string.startButtonLabel));
		statusText.setText(getText(R.string.startBuildingText));
		// Store values between instances here
	       	Editor editor = sharedpreferences.edit();
	       bobUnBuildScore = Integer.toString((Integer.parseInt(bobUnBuildScore) + 1));
	        editor.putString(UnBuiltScore, bobUnBuildScore);
	        Log.v("$$$$$$", "unBuiltScore Value:"+bobUnBuildScore);
        // Commit to storage
	        editor.commit();
    	//tToast("onStop.");
		displayNotification();
		groundBubbleImage.setVisibility(View.INVISIBLE);
		truckImage.setVisibility(View.INVISIBLE);
		truckImage.clearAnimation();
		bobBubbleImage.setVisibility(View.VISIBLE);
		shovelImage.setVisibility(View.VISIBLE);
    	Log.v("$$$$$$", "In Method: onStop()");
    	}
    }
    
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        truckImage.setScaleX(-1);
        //tToast("onResume.");
        isInFront = true;
        Log.v("$$$$$$", "In Method: onResume()");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(BuiltScore))
        {
        	bobBuildScore = sharedpreferences.getString(BuiltScore, "");
        	buildNumberText.setText(bobBuildScore);
           //tToast(sharedpreferences.getString(BuiltScore, ""));
        }
        if (sharedpreferences.contains(UnBuiltScore))
        {
        	bobUnBuildScore = sharedpreferences.getString(UnBuiltScore, "");
        	unBuildNumberText.setText(bobUnBuildScore);
           //tToast(sharedpreferences.getString(UnBuiltScore, ""));
        }

    }
        
    public void startTruckAnimation(final ImageView truck){
    	TranslateAnimation anim = new TranslateAnimation(0.0f, 70.0f, 0.0f, 0.0f);
    	anim.setDuration(1000);
    	anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(2);
        anim.setFillAfter(true);
    	anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

    	@Override
    	public void onAnimationStart(Animation animation) { }

    	@Override
    	public void onAnimationRepeat(Animation animation) { 
    		if(truck.getScaleX() == -1){
    			truck.setScaleX(1);
    		}
    		else{
    			truck.setScaleX(-1);
    		}
    		
    	}

    	@Override
    	public void onAnimationEnd(Animation animation)	{ }
    	
    	});

    	truck.startAnimation(anim);
    }
    
    
	public void startTimer(View v)	{
		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
			startB.setText(R.string.duringBuildButtonLabel);
			statusText.setText(R.string.duringBuildingText);
			shovelImage.clearAnimation();
			shovelImage.setVisibility(View.INVISIBLE);
			groundBubbleImage.setVisibility(View.VISIBLE);
			truckImage.setVisibility(View.VISIBLE);
			startTruckAnimation(truckImage);
			bobBubbleImage.setVisibility(View.INVISIBLE);
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
 	       buildNumberText.setText(bobBuildScore);
 	       Log.v("$$$$$$", "BuiltScore Value:"+bobBuildScore);
 	        // Commit to storage
 	        editor.commit();
 		   statusText.setText(getText(R.string.timeUpMessage));
 		   timerText.setText(getText(R.string.timerInitVal));
 		   startB.setText(R.string.startButtonLabel);
 		   groundBubbleImage.setVisibility(View.INVISIBLE);
 		   truckImage.setVisibility(View.INVISIBLE);
 		   truckImage.clearAnimation();
 		   bobBubbleImage.setVisibility(View.VISIBLE);
 		   shovelImage.setVisibility(View.VISIBLE);
 		   
 		   //If Screen off, then send a notification
 		   PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
 	       boolean screenOn;
 	       screenOn = pm.isScreenOn();
 	       if((!screenOn)||(!isInFront)){
 	    	   displaySuccessNotification();
 	       }
 	       else{
 	    	   Intent intent;
	 	       intent = new Intent(MainActivity.this, NotificationSuccessView.class);
	 	       startActivity(intent);
 	       }

 		}

 		@Override
 		public void onTick(long millisUntilFinished) {
 				String[] statuses = getResources().getStringArray(R.array.duringBuildingMessages_array);
 				
 				if(((int) (((millisUntilFinished / 1000) % 60)) == 0) ||((int) (((millisUntilFinished / 1000) % 60)) == 20) || ((int) (((millisUntilFinished / 1000) % 60)) == 40) ){
 					int idx = new Random().nextInt(statuses.length);
 					statusText.setText(statuses[idx]);
 				}
 				timerText.setText(convertMilliToTimeString(millisUntilFinished));
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
              					timerText.setText(getText(R.string.timerInitVal));
              					startB.setText(getText(R.string.startButtonLabel));
              					statusText.setText(getText(R.string.startBuildingText));
              				// Store values between instances here
              		 	       	Editor editor = sharedpreferences.edit();
              		 	       	bobUnBuildScore = Integer.toString(Integer.parseInt(bobUnBuildScore)+1);
              		 	        editor.putString(UnBuiltScore, bobUnBuildScore);
              		 	        unBuildNumberText.setText(bobUnBuildScore);
              		 	        Log.v("$$$$$$", "UnBuiltScore Value:"+bobUnBuildScore);
           		 	        // Commit to storage
              		 	        editor.commit();
              		 	        shovelImage.startAnimation(animation);
              		 	        groundBubbleImage.setVisibility(View.INVISIBLE);
              		 	        truckImage.setVisibility(View.INVISIBLE);
              		 	        truckImage.clearAnimation();
              		 	        bobBubbleImage.setVisibility(View.VISIBLE);
              		 	        shovelImage.setVisibility(View.VISIBLE);
              		 	    // Redirect to Failure Notification Screen
              		 	        Intent intent;
              		 	     	intent = new Intent(MainActivity.this, NotificationView.class);
              			    	startActivity(intent);
                             break;
                      case DialogInterface.BUTTON_NEGATIVE:
                    	  	//Do Nothing
                            break;
                      }
                }
         	};

         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("You sure want to kill the hard working Bob ?")
                      .setPositiveButton("Yes, destroy the building also!!", dialogClickListener)
                      .setNegativeButton("No, can think again!!", dialogClickListener).show();
 	}
 		/* Upper Menu Add Settings ... Dots Code
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
    */
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
    
    protected void displaySuccessNotification(){
    	Log.v("Notification", "Success notification");

        /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =  new NotificationCompat.Builder(this);	

        mBuilder.setContentTitle("Successfully Built a Building!!");
        mBuilder.setContentText("Now that you built your building. Share it with your friends");
        mBuilder.setTicker("Yaay!! your Building is ready.");
        mBuilder.setSmallIcon(R.drawable.success_bob);

        /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);
        
        /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, NotificationSuccessView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationSuccessView.class);

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
    
    protected void displayNotification() {
        Log.v("Notification", "Failure notification");

        /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =  new NotificationCompat.Builder(this);	

        mBuilder.setContentTitle("You killed Bob");
        mBuilder.setContentText("The building was destroyed as well!");
        mBuilder.setTicker("Oops! Poor Bob");
        mBuilder.setSmallIcon(R.drawable.dead_bob);

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
