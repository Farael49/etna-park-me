package com.example.parkme;

import java.util.Calendar;

import com.example.parkme.MapActivity.GetSpots;
import com.google.android.gms.maps.model.LatLng;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import classes.GPSTracker;
import classes.ServerRequests;
import classes.User;
import classes.Utils;

public class AddSpotActivity extends FragmentActivity {
	Button btnSubmit;
	EditText etLat, etLng;
	TextView date, time;
	NumberPicker npTime;

	@Override
	public void onBackPressed() {
		User.getInstance().setAuthenticated(false);
		super.onBackPressed();
	}

	// exemple d'un datePicker
	public void showDatePickerDialog(View v) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog mDatePicker;

		mDatePicker = new DatePickerDialog(AddSpotActivity.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						date.setText(dayOfMonth + "/" + monthOfYear + "/"
								+ year);
					}
				}, year, month, day);
		mDatePicker.getDatePicker().setMinDate(
				Calendar.getInstance().getTimeInMillis() - 10000);
		mDatePicker.setTitle("Choisissez la date");
		mDatePicker.show();
	}

	// exemple d'un timePicker
	public void showTimePickerDialog(View v) {
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);

		MyTimePicker mTimePicker;
		mTimePicker = new MyTimePicker(AddSpotActivity.this,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,
							int selectedHour, int selectedMinute) {
						time.setText(selectedHour + ":" + selectedMinute);
					}

				}, hour, minute, true);
		mTimePicker.setMin(3, 0);

		mTimePicker.setTitle("Choisissez l'heure !");
		mTimePicker.show();
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
		npTime = (NumberPicker) findViewById(R.id.numberPicker);
		date = (TextView) findViewById(R.id.tvDate);
		time = (TextView) findViewById(R.id.tvTime);

		date.setText("00/00/00");
		time.setText("00:00");

		npTime.setMaxValue(9);
		npTime.setMinValue(0);
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
				String time = String.valueOf(npTime.getValue());
				if (lat.isEmpty() || lng.isEmpty()) {
					GPSTracker gps = new GPSTracker(AddSpotActivity.this);
					if (gps.canGetLocation()) {
						lat = Double.toString(gps.getLatitude());
						lng = Double.toString(gps.getLongitude());
					}
				}
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
		private String actionStatus = "";

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
			String password = null;
			AccountManager accountManager = AccountManager
					.get(getApplicationContext());
			Account[] accounts = accountManager
					.getAccountsByType("com.example.parkme");
			for (Account account : accounts) {
				if (account.name.equals(User.getInstance().getEmail())) {
					password = accountManager.getPassword(account);
					break;
				}
			}

			if (User.getInstance().isAuthenticated()) {
				boolean isRequestDone = ServerRequests.addSpot(lat, lng, time,
						User.getInstance().getEmail(), password);
				if (!isRequestDone)
					actionStatus = "An error occured";
				else
					actionStatus = "Spot added !";

			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			// Utils.showToastText(getApplicationContext(), actionStatus);
			if (User.getInstance().isAuthenticated()) {
				Intent i = new Intent(AddSpotActivity.this, MapActivity.class);
				startActivity(i);
			}
		}

	}

}
