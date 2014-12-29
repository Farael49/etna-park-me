package com.example.parkme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import classes.ServerRequests;
import classes.User;
import classes.Utils;

public class AddSpotActivity extends Activity {
	Button btnSubmit;
	EditText etLat, etLng, etTime;

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_spot);
		Utils.showToastText(this, "Bienvenue " + User.getInstance().getEmail());
		// Buttons
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		etLat = (EditText) findViewById(R.id.lat);
		etLng = (EditText) findViewById(R.id.lng);
		etTime = (EditText) findViewById(R.id.time);
		
		// login click event
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				/*
				 * Intent i = new Intent(getApplicationContext(),
				 * NewSpotActivity.class); startActivity(i);
				 */
				String lat = etLat.getText().toString();
				String lng = etLng.getText().toString();
				String time = etTime.getText().toString();

				new AddSpot().execute(lat, lng, time);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.requests, menu);
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

	/**
	 * Background Async Task to add a spot
	 * */
	class AddSpot extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddSpotActivity.this);
			pDialog.setMessage("Adding Spot..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			Float lat = Float.valueOf(args[0]);
			Float lng = Float.valueOf(args[1]);
			int time = Integer.valueOf(args[2]);
			
			if (User.getInstance().isAuthenticated())
				ServerRequests.addSpot(lat, lng, time);

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}

}
