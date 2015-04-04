package com.example.bobthebuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View; 
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;  
import android.widget.Toast;  


public class MainActivity extends Activity {

	// public var  
    private EditText text; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // findViewById = Finds a view that was identified by the id attribute  
        // from the XML that was processed in onCreate(Bundle).  
        // (EditText) = typecast  
        text = (EditText) findViewById(R.id.editText1);
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
    
    /* 
     * Will be executed by clicking on the calculate button because we assigned 
     * "Calculate" to the "onClick" Property! 
     */  
    public void Calculate(View view) {  
  
        RadioButton mileButton = (RadioButton) findViewById(R.id.radioButton1);  
        RadioButton kmhButton = (RadioButton) findViewById(R.id.radioButton2);  
        // if the text field is empty show the message "enter a valid number"  
        if (text.getText().length() == 0) {  
            // Toast = focused floating view that will be shown over the main  
            // application  
            Toast.makeText(this, "enter a valid number", Toast.LENGTH_LONG)  
                    .show();  
        } else {  
            //parse input Value from Text Field  
            double inputValue = Double.parseDouble(text.getText().toString());  
            // convert to...  
            if (mileButton.isChecked()) {  
                text.setText(String.valueOf(convertToMiles(inputValue)));  
                // uncheck "to miles" Button  
                mileButton.setChecked(false);  
                // check "to km/h" Button  
                kmhButton.setChecked(true);  
            } else { /* if kmhButton isChecked() */  
                text.setText(String.valueOf(convertToKmh(inputValue)));  
                // uncheck "to km/h" Button  
                kmhButton.setChecked(false);  
                // check "to miles" Button  
                mileButton.setChecked(true);  
            }  
        }  
    }  
  
    private double convertToMiles(double inputValue) {  
        // convert km/h to miles  
        return (inputValue * 1.609344);  
    }  
  
    private double convertToKmh(double inputValue) {  
        // convert miles to km/h  
        return (inputValue * 0.621372);  
    }  
    
}
