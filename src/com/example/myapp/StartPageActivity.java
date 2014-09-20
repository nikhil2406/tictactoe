package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class StartPageActivity extends Activity {
	
	private DialogFragment mDialog;
	private static final int ALERTTAG = 0, PROGRESSTAG = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.start_page);
		setContentView(R.layout.start_page_main);
		
/*		ImageView welcomeImage = (ImageView) findViewById(R.id.welcomeImage);
		welcomeImage.setImageResource(R.drawable.welcome_image);
*/		
		Button launchMainActivityButton = (Button) findViewById(R.id.play); 
		launchMainActivityButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent mainActivityIntent = new Intent(StartPageActivity.this, MainActivity.class);
				// Launch the Activity using the intent
				startActivity(mainActivityIntent);
			}
		});

		Button launchScoresActivityButton = (Button) findViewById(R.id.scores); 
		launchScoresActivityButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent scoresActivityIntent = new Intent(StartPageActivity.this, ScoresPageActivity.class);
				// Launch the Activity using the intent
				startActivity(scoresActivityIntent);
			}
		});

		Button exitButton = (Button) findViewById(R.id.exit); 
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.sessionTimeStamp = null;
				MainActivity.mXWinCount = 0;
				MainActivity.mOWinCount = 0;
				showDialogFragment(ALERTTAG);
				//StartPageActivity.this.finish();
			}
		});

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@SuppressLint("NewApi") protected void continueShutdown(boolean shouldContinue) {
		if (shouldContinue) {
			finishShutdown();
		} else {
			mDialog.dismiss();
		}
	}
	
	private void finishShutdown() {
		finish();
	}
	
	@SuppressLint("NewApi") void showDialogFragment(int dialogID) {
		switch (dialogID) {
		case ALERTTAG:
			mDialog = AlertDialogFragment.newInstance();
			mDialog.show(getFragmentManager(), "Alert");
			break;
/*		case PROGRESSTAG:
			mDialog = ProgressDialogFragment.newInstance();
			mDialog.show(getFragmentManager(), "Shutdown");
			break;*/
		}
	}

}


class AlertDialogFragment extends DialogFragment {

	public static AlertDialogFragment newInstance() {
		return new AlertDialogFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setMessage("Do you really want to exit?")
				.setCancelable(false)
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								((StartPageActivity) getActivity())
										.continueShutdown(false);
							}
						})
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									final DialogInterface dialog, int id) {
								((StartPageActivity) getActivity())
										.continueShutdown(true);
							}
						}).create();
	}
}




