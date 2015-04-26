package com.example.bobthebuilder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class NotificationSuccessView extends Activity {

	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "bobPrefs" ;
	public static final String BuiltScore = "builtKey"; 
	public static final String UnBuiltScore = "unBuiltKey";

	private String bobBuildScore = "0";
	private String bobUnBuildScore = "0";
	
	private TextView buildNumberText;
	private TextView unBuildNumberText;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState)
	   {
		   super.onCreate(savedInstanceState);
		   
	        //Hiding Status Bar
	        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	        getActionBar().hide();
		   
		   //Setting Notification View
		   setContentView(R.layout.notification_success);   
	        

		   // Initialize Google AdMob Adv
	        AdView mAdView = (AdView) findViewById(R.id.adView);
	        AdRequest adRequest = new AdRequest.Builder().build();
	        mAdView.loadAd(adRequest);
	        
		   
	        //Initialize TextBox elements
	        buildNumberText = (TextView) this.findViewById(R.id.buildNumberTextView);
			unBuildNumberText = (TextView) this.findViewById(R.id.unBuildNumberTextView);
	        
	        //Retrieve Values from Shared Preferences
	        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	        if (sharedpreferences.contains(BuiltScore))
	        {
	        	bobBuildScore = sharedpreferences.getString(BuiltScore, "");
	        	Log.v("SharePreferences value",sharedpreferences.getString(BuiltScore, ""));
	        	Log.v("BobBuildSCore Value",bobBuildScore);
	        	buildNumberText.setText(bobBuildScore);
	        }
	        if (sharedpreferences.contains(UnBuiltScore))
	        {
	        	bobUnBuildScore = sharedpreferences.getString(UnBuiltScore, "");
	        	unBuildNumberText.setText(bobUnBuildScore);
	        }
	      
	   }
	   
	   public void mainScreenRedirect(View v) {
		    Intent intent;
		    intent = new Intent(this, MainActivity.class);
		    startActivity(intent);
		}
}
