package classes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by Christophe on 16/12/2014.
 */
public class ServerRequests {

	private static final String ADD_SPOT = "OFFER_SPOT";
	private static final String AUTHENTICATE = "AUTHENTICATE";
	private static final String SUCCESS_TAG = "success";
	private static final String URL_CRUD_SCRIPT = "http://107.170.145.214/android_connect/php_script/crud.php";
	
	/** TODO 
	 * php script still not updated
	 * must add : 
	 * creating account
	 * updating/removing spot
	 * add constraints to the database */
	public static boolean createAccount(String email, String password){
		return false;
	}
	
	public static boolean updateSpot(String email){
		return false;
	}
	
	public static boolean removeSpot(String email){
		return false;
	}

	public static boolean authenticate(String email, String cryptedPwd) {
		boolean isAuthenticated = false;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", AUTHENTICATE));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", cryptedPwd));
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

		// parse json data
		try {
			JSONObject jObject = new JSONObject(result);
			if (jObject.getInt(SUCCESS_TAG) == 1) {
				User.getInstance().setEmail(email);
				User.getInstance().setAuthenticated(true);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return User.getInstance().isAuthenticated();
	}

	
	public static boolean addSpot(float lat, float lng, int time_rdy) {
		boolean success = false;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", ADD_SPOT));
		nameValuePairs.add(new BasicNameValuePair("lat", Float.toString(lat)));
		nameValuePairs.add(new BasicNameValuePair("lng", Float.toString(lng)));
		// nameValuePairs.add(new BasicNameValuePair("email", User.getEmail()));
		// nameValuePairs.add(new BasicNameValuePair("token",
		// user.getAuthToken()));
		nameValuePairs.add(new BasicNameValuePair("user_id", Integer
				.toString(1)));
		nameValuePairs.add(new BasicNameValuePair("time_when_ready", Integer
				.toString(time_rdy)));

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

		// parse json data
		try {
			JSONObject jObject = new JSONObject(result);
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
}
