package com.example.parkme;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import classes.ServerRequests;
import classes.User;

public class MainActivity extends Activity {
	Button btnLogin;
	TextView tvEmail, tvPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Buttons
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvEmail = (TextView) findViewById(R.id.email);
		tvPwd = (TextView) findViewById(R.id.password);

		// login click event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				/*
				 * Intent i = new Intent(getApplicationContext(),
				 * NewSpotActivity.class); startActivity(i);
				 */
				String email = tvEmail.getText().toString();
				String pwd = tvPwd.getText().toString();

				new CreateNewSpot().execute(email, pwd);
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
	 * Background Async Task to add new spot
	 * */
	class CreateNewSpot extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Adding Spot..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String password = args[1];
			AccountManager accountManager = AccountManager
					.get(getApplicationContext());
			Account[] accounts = accountManager
					.getAccountsByType("com.example.parkme");
			if (User.getInstance().isAuthenticated)
				ServerRequests.addSpot(15.5f, 25.2f, 150, User.getInstance());
			else {
				for (Account account : accounts) {
					if(ServerRequests.authenticate(account.name, accountManager.getPassword(account)))
						break;
				}

				Account account = new Account(args[0], "com.example.parkme");
				accountManager.addAccountExplicitly(account, password, null);
			}
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
