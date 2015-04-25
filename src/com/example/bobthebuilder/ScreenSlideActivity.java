package com.example.bobthebuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ScreenSlideActivity extends Activity {

	ViewPager myPager;
	Button goButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);

		// Set View pager
		ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageArra);
		myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(0);
		
		//Hide Go Button On Start
		goButton = new Button(this);
		goButton = (Button) findViewById(R.id.goButton);
		goButton.setVisibility(android.view.View.INVISIBLE);
		
		//Set Listener
		myPager.setOnPageChangeListener(new OnPageChangeListener() {
		    public void onPageScrollStateChanged(int state) {}
		    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

		    public void onPageSelected(int position) {
		        // Check if this is the page you want.
		    	if(position==3){
		    		goButton.setVisibility(android.view.View.VISIBLE);
		    	}
		    	else{
		    		goButton.setVisibility(android.view.View.INVISIBLE);
		    	}
		    }
		});
	}
	


	
	
	

	private int imageArra[] = { R.drawable.walkthrough1, R.drawable.walkthrough2,R.drawable.walkthrough3,R.drawable.walkthrough4 };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void startMainActivity(View v)	{
			Intent intent;
	       intent = new Intent(ScreenSlideActivity.this, MainActivity.class);
	       startActivity(intent);
	}
}