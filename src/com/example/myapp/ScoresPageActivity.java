package com.example.myapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ScoresPageActivity extends Activity {

	private TableLayout ll = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scores_page);
		
		populateScoreTable();
		
		Button launchMainActivityButton = (Button) findViewById(R.id.play); 
		launchMainActivityButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mainActivityIntent = new Intent(ScoresPageActivity.this, MainActivity.class);
				// Launch the Activity using the intent
				mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mainActivityIntent);
			}
		});

		Button launchScoresActivityButton = (Button) findViewById(R.id.scores); 
		launchScoresActivityButton.setEnabled(false);

		Button exitButton = (Button) findViewById(R.id.exit);
		exitButton.setText("Main Menu");
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startPageActivityIntent = new Intent(ScoresPageActivity.this, StartPageActivity.class);
				startPageActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// Launch the Activity using the intent
				startActivity(startPageActivityIntent);
			}
		});
	}
	
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	this.finish();
    }
    
	void populateScoreTable() {
		// get scores from sharedpreference file set in mainActivity.java
		ll = (TableLayout) findViewById(R.id.scoresTable);
		SharedPreferences scores = getSharedPreferences(MainActivity.PREFS_NAME, 0);

		//call to add title row on score table
		addRow(null, 0);
		
		try {
			final JSONArray jsonArray = new JSONArray(scores.getString("scores", "[]"));
			JSONObject jsonObj = null;

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = (JSONObject) jsonArray.get(i);
				
				addRow(jsonObj,i+1);
			}
		}
		catch (Exception e) {
	        e.printStackTrace();
	    }		
	}
	
    void addRow(JSONObject jsonObj, int index) {

		TableRow row= null;
		TextView tvT = null;
		TextView tvX = null;
		TextView tvO = null;
		
    	if(jsonObj == null) {
    		//add title row
			row= new TableRow(this);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(lp);
			tvT = new TextView(this);
			tvX = new TextView(this);
			tvO = new TextView(this);

			tvT.setText("TimeStamp");
			tvT.setGravity(Gravity.CENTER);
			tvT.setPadding(20, 10, 20, 10);
			tvX.setText("X Win Count");
			tvX.setGravity(Gravity.CENTER);
			tvX.setPadding(20, 10, 20, 10);
			tvO.setText("O Win Count");
			tvO.setGravity(Gravity.CENTER);
			tvO.setPadding(20, 10, 20, 10);
			
			row.addView(tvT);
			row.addView(tvX);
			row.addView(tvO);

			ll.addView(row, index);
    	}
    	else {
    		//add data row
			row= new TableRow(this);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(lp);
			tvT = new TextView(this);
			tvX = new TextView(this);
			tvO = new TextView(this);

			try {
				tvT.setText(jsonObj.getString("time"));
				tvT.setGravity(Gravity.CENTER);
				tvT.setPadding(20, 5, 20, 5);
				tvX.setText(String.valueOf(jsonObj.getInt("X")));
				tvX.setGravity(Gravity.CENTER);
				tvX.setPadding(20, 5, 20, 5);
				tvO.setText(String.valueOf(jsonObj.getInt("O")));
				tvO.setGravity(Gravity.CENTER);
				tvO.setPadding(20, 5, 20, 5);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			row.addView(tvT);
			row.addView(tvX);
			row.addView(tvO);

			// add these rows from second row onwards. First row is for headings.
			ll.addView(row, index);
    	}
    }
    
}
