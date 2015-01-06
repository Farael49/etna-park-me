package classes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by Christophe on 16/12/2014.
 */
public class ServerRequests {

	private static final String ADD_SPOT = "OFFER_SPOT";
	private static final String AUTHENTICATE = "AUTHENTICATE";
	private static final String REGISTER = "REGISTER";
	private static final String GET_ALL_SPOTS = "GET_ALL_SPOTS";
	private static final String SUCCESS_TAG = "success";
	private static final String RESULT_TAG = "result";
	private static final int WRONG_ID = -1;
	private static final String URL_CRUD_SCRIPT = "http://107.170.145.214/android_connect/php_script/crud.php";

	/**
	 * TODO php script still not updated must add : creating account
	 * updating/removing spot add constraints to the database
	 */
	public static boolean updateSpot(String email) {
		return false;
	}

	public static boolean removeSpot(String email) {
		return false;
	}

	public static ArrayList<HashMap<String, String>> getAllSpots(Float lat,
			Float lng, int radius) {
		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", GET_ALL_SPOTS));
		nameValuePairs.add(new BasicNameValuePair("user_lat", Float
				.toString(lat)));
		nameValuePairs.add(new BasicNameValuePair("user_lng", Float
				.toString(lng)));
		nameValuePairs.add(new BasicNameValuePair("radius", Integer
				.toString(radius)));

		// parse json data
		try {
			JSONObject jObject = doPhpAction(nameValuePairs);
			if (jObject.getInt(SUCCESS_TAG) == 1) {
				// products found
				// Getting Array of spots
				JSONArray spots = jObject.getJSONArray("spots");

				// looping through All Products
				for (int i = 0; i < spots.length(); i++) {
					JSONObject c = spots.getJSONObject(i);

					// Storing each json item in variable
					Float tmp_lat = Float.valueOf(c.getString("lat"));
					Float tmp_lng = Float.valueOf(c.getString("lng"));
					Float tmp_id = Float.valueOf(c.getString("id"));

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put("spot_lat", Float.toString(tmp_lat));
					map.put("spot_lng", Float.toString(tmp_lng));
					map.put("spot_id", Float.toString(tmp_id));


					// adding HashList to ArrayList
					results.add(map);
				}
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			User.getInstance().setAuthenticated(false);

		}
		return results;
	}

	public static boolean authenticate(String email, String cryptedPwd) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", AUTHENTICATE));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", cryptedPwd));

		// parse json data
		try {
			JSONObject jObject = doPhpAction(nameValuePairs);
			if (jObject.getInt(SUCCESS_TAG) == 1) {
				User.getInstance().setEmail(email);
				User.getInstance().setAuthenticated(true);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			User.getInstance().setAuthenticated(false);

		}
		return User.getInstance().isAuthenticated();
	}

	/**
	 * 
	 * @param lat
	 * @param lng
	 * @param time_rdy
	 * @param username
	 *            is the user's email
	 * @param password_am
	 *            is the password stored in the account manager of Android and
	 *            found with the actual email used as username
	 * @return
	 */
	public static boolean addSpot(float lat, float lng, int time_rdy,
			String username, String password_am) {
		boolean success = false;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", ADD_SPOT));
		nameValuePairs.add(new BasicNameValuePair("lat", Float.toString(lat)));
		nameValuePairs.add(new BasicNameValuePair("lng", Float.toString(lng)));
		nameValuePairs.add(new BasicNameValuePair("email", username));
		nameValuePairs.add(new BasicNameValuePair("password", password_am));
		nameValuePairs.add(new BasicNameValuePair("time_when_ready", Integer
				.toString(time_rdy)));

		// parse json data
		try {
			JSONObject jObject = doPhpAction(nameValuePairs);
			/*
			 * for (int i = 0; i < jObject.length(); i++) Log.e(jObject.get)
			 */
			if (jObject.getInt(SUCCESS_TAG) == 1) {
				success = true;
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return success;

	}

	public static boolean registerUser(String username, String password) {
		boolean success = false;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", REGISTER));
		nameValuePairs.add(new BasicNameValuePair("email", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		try {
			JSONObject jObject = doPhpAction(nameValuePairs);
			if (jObject.getInt(RESULT_TAG) == 1)
				success = true;
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return success;
	}

	private static int getUserId(String username, String password) {
		int result = WRONG_ID;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", AUTHENTICATE));
		nameValuePairs.add(new BasicNameValuePair("email", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		doPhpAction(nameValuePairs);
		try {
			JSONObject jObject = doPhpAction(nameValuePairs);
			result = jObject.getInt(RESULT_TAG);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return result;
	}

	private static JSONObject doPhpAction(
			ArrayList<NameValuePair> nameValuePairs) {
		String result = "";
		InputStream is = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL_CRUD_SCRIPT);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
			Log.e("test", result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		JSONObject jObject = null;
		// parse json data
		try {
			jObject = new JSONObject(result);

		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());

		}
		return jObject;
	}
}
