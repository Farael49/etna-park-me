package com.example.parkme;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import classes.GPSTracker;
import classes.ServerRequests;
import classes.User;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements OnMapReadyCallback {
	final int RADIUS = 1000;
	GPSTracker gps;
	LatLng lastLoc = new LatLng(48.813547, 2.392863);
	ArrayList<HashMap<String, Float>> spotsInRange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		gps = new GPSTracker(MapActivity.this);
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(GoogleMap map) {
		map.setMyLocationEnabled(true);
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			lastLoc = new LatLng(latitude, longitude);
			new GetSpots().execute(Double.toString(latitude),
					Double.toString(longitude));
			if (spotsInRange != null)
				for (HashMap<String, Float> spot : spotsInRange)
					map.addMarker(new MarkerOptions()
							.title("Parking Spot")
							.snippet("Disponible dans 5 minutes")
							.position(
									new LatLng(spot.get("user_lat"), spot
											.get("user_lng"))));

			// \n is for new line
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
		} else {
			// Can't get location.
			// GPS or network is not enabled.
			// Ask user to enable GPS/network in settings.
			gps.showSettingsAlert();
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 13));
		map.addMarker(new MarkerOptions().title("Parking Spot")
				.snippet("Disponible dans 5 minutes").position(lastLoc));

	}

	/**
	 * Background Async Task to add a spot
	 * */
	class GetSpots extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;
		private String actionStatus = "";
		private int RADIUS = 1000;
		ArrayList<HashMap<String, Float>> results;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MapActivity.this);
			pDialog.setMessage("Searching Spots..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			Float user_lat = Float.valueOf(args[0]);
			Float user_lng = Float.valueOf(args[1]);
			if (User.getInstance().isAuthenticated()) {
				spotsInRange = ServerRequests.getAllSpots(user_lat, user_lng,
						RADIUS);

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
		}

	}

}