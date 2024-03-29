package com.example.parkme;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import classes.ServerRequests;
import classes.User;
import classes.Utils;

public class MainActivity extends Activity {
	Button btnLogin, btnRegister;
	TextView tvEmail, tvPwd;
	int MIN_PWD_LENGTH, MIN_MAIL_LENGTH = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// try to log
		new SignInWithAccountManager().execute();
		// Buttons
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		tvEmail = (TextView) findViewById(R.id.email);
		tvPwd = (TextView) findViewById(R.id.password);

		// login click event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String email = tvEmail.getText().toString();
				String pwd = tvPwd.getText().toString();
				if (email.length() > MIN_MAIL_LENGTH && pwd.length() > MIN_PWD_LENGTH)
					new SignInManual().execute(email, pwd);
				else
					Utils.showToastText(getApplicationContext(),
							"Renseignez correctement vos identifiants.");
			}
		});
		
		// login click event
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String email = tvEmail.getText().toString();
				String pwd = tvPwd.getText().toString();
				if (email.length() > MIN_MAIL_LENGTH && pwd.length() > MIN_PWD_LENGTH)
					new RegisterUser().execute(email, pwd);
				else
					Utils.showToastText(getApplicationContext(),
							"Vos identifiants sont trop courts.");
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
	 * Background Async Task to sign in
	 * */
	class SignInManual extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Connecting ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String email = args[0];
			String password = args[1];

			if (ServerRequests.authenticate(email, password)) {
				AccountManager accountManager = AccountManager
						.get(getApplicationContext());
				Account account = new Account(email, "com.example.parkme");
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
			if (User.getInstance().isAuthenticated()) {
				Intent i = new Intent(MainActivity.this, AddSpotActivity.class);
				startActivity(i);
			}
		}

	}

	/**
	 * Background sync Task to login
	 * */
	class SignInWithAccountManager extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Logging, please wait.");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		private void loggingWithAccountManager(AccountManager accountManager,
				Account[] accounts) {
			for (Account account : accounts) {
				if (ServerRequests.authenticate(account.name,
						accountManager.getPassword(account))) {
					break;
				}
			}
		}

		protected String doInBackground(String... args) {
			AccountManager accountManager = AccountManager
					.get(getApplicationContext());
			Account[] accounts = accountManager
					.getAccountsByType("com.example.parkme");
			loggingWithAccountManager(accountManager, accounts);
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			if (User.getInstance().isAuthenticated()) {
				// goes next activity
				Log.e("auth_status", "test"
						+ User.getInstance().isAuthenticated());
				Log.i("PM", "Launching AddSpotActivity");
				Intent i = new Intent(MainActivity.this, AddSpotActivity.class);
				startActivity(i);
			} else {
				Utils.showToastText(getApplicationContext(), "Please enter your credentials");
			}
		}
	}
	
	/**
	 * Background Async Task to sign in
	 * */
	class RegisterUser extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Registering ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String email = args[0];
			String password = args[1];

			ServerRequests.registerUser(email, password);
			if (ServerRequests.authenticate(email, password)) {
				AccountManager accountManager = AccountManager
						.get(getApplicationContext());
				Account account = new Account(email, "com.example.parkme");
				accountManager.addAccountExplicitly(account, password, null);
			}

			return email;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String email) {
			// dismiss the dialog once done
			pDialog.dismiss();
			if (User.getInstance().isAuthenticated()) {
				Intent i = new Intent(MainActivity.this, AddSpotActivity.class);
				startActivity(i);
			}else
				Utils.showToastText(getApplicationContext(), "An error occured while adding the account " + email);
		}

	}
}
