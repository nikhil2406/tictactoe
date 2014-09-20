package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View.OnClickListener;

import com.example.myapp.Player;
import com.example.myapp.Player.symbols;

public class MainActivity extends Activity {
	public static String sessionTimeStamp = null;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static int mXWinCount = 0;
	public static int mOWinCount = 0;
	//keep track of how many chances are played (equal to button pressed) already in current game
	public static int chancesPlayed = 0;
	
	private Player[] players = new Player[2];

	private enum player_id {PLAYER_1, PLAYER_2};
	// let XX play first chance
	private player_id kiskiBari = player_id.PLAYER_1;
	
	//keep a global for storing all buttons in board layout
	private ArrayList<View> allButtonsOnBoard;

	
    /**
     * get current time stamp. used for storing scores according to time
     * @return
     */
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// get time stamp and save it.
		//Make sure that timestamp is collected only once per session
		if(sessionTimeStamp == null) {
			sessionTimeStamp = getCurrentTimeStamp();
		}
        
        // call player intitalizer
        players[0] = new Player(symbols.XX);
        players[1] = new Player(symbols.OO);
        // by default making second player a robot
        players[1].setRobot(true);
        
        // save all buttons of board layout for later use enable/disable
		final RelativeLayout board_layout = (RelativeLayout)findViewById(R.id.board_layout);
		//board_layout.setClickable(false);
		allButtonsOnBoard = board_layout.getTouchables();
        
        // row 0
        final ToggleButton button00 = (ToggleButton) findViewById(R.id.ToggleButton00);
        button00.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button00);
			}
		});

        final ToggleButton button01 = (ToggleButton) findViewById(R.id.ToggleButton01);
        button01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button01);
			}
		});

        final ToggleButton button02 = (ToggleButton) findViewById(R.id.ToggleButton02);
        button02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button02);
			}
		});

        
        // row 1
        final ToggleButton button10 = (ToggleButton) findViewById(R.id.ToggleButton10);
        button10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button10);
			}
		});

        final ToggleButton button11 = (ToggleButton) findViewById(R.id.ToggleButton11);
        button11.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button11);
			}
		});

        final ToggleButton button12 = (ToggleButton) findViewById(R.id.ToggleButton12);
        button12.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button12);
			}
		});

        // row 2
        final ToggleButton button20 = (ToggleButton) findViewById(R.id.ToggleButton20);
        button20.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button20);
			}
		});

        final ToggleButton button21 = (ToggleButton) findViewById(R.id.ToggleButton21);
        button21.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button21);
			}
		});

        final ToggleButton button22 = (ToggleButton) findViewById(R.id.ToggleButton22);
        button22.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processClickEvent(button22);
			}
		});

        //---- handling for buttons below the game board
        // Exit button will act to take control to start menu page
		Button exitButton = (Button) findViewById(R.id.exit);
		exitButton.setText("Main Menu");
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startPageActivityIntent = new Intent(MainActivity.this, StartPageActivity.class);
				startPageActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// Launch the Activity using the intent
				startActivity(startPageActivityIntent);
			}
		});
		
		// scores button will still open scores page
		Button launchScoresActivityButton = (Button) findViewById(R.id.scores); 
		launchScoresActivityButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent scoresActivityIntent = new Intent(MainActivity.this, ScoresPageActivity.class);
				// Launch the Activity using the intent
				startActivity(scoresActivityIntent);
			}
		});
		
		// play button would clear board layout to enable another game to begin in same activity
		Button resetGameButton = (Button) findViewById(R.id.play);
		resetGameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//reset chancesPlayed to zero
				chancesPlayed = 0;
				for(View touchable : allButtonsOnBoard) {
				    if( touchable instanceof ToggleButton )
				    {
				        ((ToggleButton)touchable).setClickable(true);
				    	((ToggleButton)touchable).setText(null);
				    	if (((ToggleButton)touchable).isChecked()) ((ToggleButton)touchable).toggle();
				    }
				}

			}
		});
		
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    
    // commented this so that from scores page pressing back button brings us to this page
    //in case scores was opened from play page
/*    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	this.finish();
    }*/
    

    
    
    /**
     * create a jsonarray and store it in sharedpreference for use later.
     * Store last 10 results
     */
    public void fillScores() {
    	SharedPreferences scores = getSharedPreferences(PREFS_NAME, 0);
    	JSONArray jsonArray = null;
    	JSONObject jsonObj = new JSONObject();
    	boolean modifiedExistingEntry = false;
    	try {
			jsonObj.put("time", sessionTimeStamp);
			jsonObj.put("X", mXWinCount);
			jsonObj.put("O", mOWinCount);

    		jsonArray = new JSONArray(scores.getString("scores", "[]"));
    		int length = jsonArray.length();
    		Log.i(getLocalClassName(), "json length=" + String.valueOf(jsonArray.length()));

    		for (int i=0; i < jsonArray.length();i++) {
    			//Log.i(getLocalClassName(), String.valueOf(i) + "th json= " + jsonArray.get(i).toString());
    			Log.i(getLocalClassName(), "::" + sessionTimeStamp + "::" + ((JSONObject)(jsonArray.get(i))).getString("time")+"::");
    			if(sessionTimeStamp.equalsIgnoreCase(((JSONObject)(jsonArray.get(i))).getString("time"))) {
    				((JSONObject)(jsonArray.get(i))).put("X",mXWinCount);
    				((JSONObject)(jsonArray.get(i))).put("O",mOWinCount);
    				modifiedExistingEntry = true;
    				break;
    			}
    		}
    		
    		if(!modifiedExistingEntry) {
	    		if (length < 9) {
	    			jsonArray.put(jsonObj);
	    		}
	    		else {
	    			int i = 1;
	    			for (i=1;i < 9;i++) {
	    				jsonArray.put(i-1, (JSONObject) jsonArray.get(i));
	    			}
	    			jsonArray.put(i-1, jsonObj);
	    		}
    		}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
		SharedPreferences.Editor editor = scores.edit();
		editor.putString("scores", jsonArray.toString());
		editor.commit();
    	
    }

    void robotPlays() {
    	ToggleButton button = ((ToggleButton) findViewById(R.id.ToggleButton01));
    	button.setText(players[kiskiBari.ordinal()].getName());
		button.setClickable(false);
    }
    
    
    void processClickEvent(ToggleButton button) {
    	//increment chancesPlayed for every button click
    	chancesPlayed++;
		final TextView textView1 = (TextView) findViewById(R.id.textView1);
		
		if (kiskiBari == player_id.PLAYER_1) {
			button.setText(players[0].getName());
			button.setClickable(false);
			//after each click check if someone won
			if ( !gameOver(kiskiBari, button, textView1) ) {
		    	if (chancesPlayed == 9) {
		    		textView1.setText("Its a Draw !. Play Again.");
		    	}
		    	else {
					kiskiBari = player_id.PLAYER_2;
					textView1.setText( players[1].getName() + " to play");
					//TODO test code
					robotPlays();
		    	}
			}
			else { //player 1 won the game
				mXWinCount++;
				Toast.makeText(getApplicationContext(), "XX=" + mXWinCount + " OO=" + mOWinCount,
						   Toast.LENGTH_LONG).show();
				//disable all remaining buttons
				final RelativeLayout board_layout = (RelativeLayout)findViewById(R.id.board_layout);
				ArrayList<View> touchables = board_layout.getTouchables();
				for(View touchable : touchables){
				    if( touchable instanceof ToggleButton )
				        ((ToggleButton)touchable).setClickable(false);
				}
				//fill score on device to enable show results in scores page
				fillScores();
			}
		}
		else {
			button.setText(players[1].getName());
			button.setClickable(false);
			//after each click check if someone won

			if (!gameOver(kiskiBari, button, textView1)) {
		    	if (chancesPlayed == 9) {
		    		textView1.setText("Its a Draw !. Play Again.");
		    	}
		    	else {
					kiskiBari = player_id.PLAYER_1;
					textView1.setText( players[0].getName() + " to play");
		    	}
			}
			else {
				mOWinCount++;
				Toast.makeText(getApplicationContext(), "XX=" + mXWinCount + " OO=" + mOWinCount,
						   Toast.LENGTH_LONG).show();
				final RelativeLayout board_layout = (RelativeLayout)findViewById(R.id.board_layout);
				//board_layout.setClickable(false);
				ArrayList<View> touchables = board_layout.getTouchables();
				for(View touchable : touchables){
				    if( touchable instanceof ToggleButton )
				        ((ToggleButton)touchable).setClickable(false);
				}
				//fill score on device to enable show results in scores page
				fillScores();

			}
		}
		
		
		
	}

    boolean gameOver(player_id id, ToggleButton button, TextView textView1) {
		// check it after first 4 chances are over
    	if (chancesPlayed <= 4) {
    		Log.i(getLocalClassName(), "4 chances are not over yet. moving outof gameover()");
    		return false;
    	}

    	int button_id = button.getId();
    	boolean over = false;
    	CharSequence buttonText = ((ToggleButton) findViewById(button_id)).getText();
    	
    	switch (button_id) {
    	//column 1: 00, 10, 20
    	case R.id.ToggleButton00: {
    		
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton01)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton02)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton10)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton20)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton22)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    	case R.id.ToggleButton10: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton12)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton00)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton20)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    	case R.id.ToggleButton20: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton21)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton22)).getText() ) {
    			//textView1.setText(((ToggleButton) findViewById(button_id)).getText()+ " WINS");
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton10)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton00)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton02)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    	//column 2: 01, 11, 21
    	case R.id.ToggleButton01: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton00)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton02)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton21)).getText() ) {
    			over = true;
    		}
    		break;
    	}
    		
    	case R.id.ToggleButton11: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton10)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton12)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton01)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton21)).getText() ) {
    			over = true;
    		}
    		break;
    	}
    		
    	case R.id.ToggleButton21: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton20)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton22)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton01)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    		
    	//column 3: 02, 12, 22
    	case R.id.ToggleButton02: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton01)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton00)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton12)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton22)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton20)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    	case R.id.ToggleButton12: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton10)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton02)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton22)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    	case R.id.ToggleButton22: {
    		if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton21)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton20)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton12)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton02)).getText() ) {
    			over = true;
    		}
    		else if( buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton11)).getText() && 
    				buttonText == 
    				((ToggleButton) findViewById(R.id.ToggleButton00)).getText() ) {
    			over = true;
    		}
    		break;
    	}

    	}
    	
    	if (over) {
    		textView1.setText(((ToggleButton) findViewById(button_id)).getText()+ " WINS");
    	}
    	
    	return over;
    }
    
    
}
