package classes;

import java.util.List;

/**
 * Created by Christophe on 16/12/2014.
 */
public class User {
	private String email; // Used as an ID, not PID
	private static User instance;
	private boolean isAuthenticated;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	private List<Vehicle> vehicles; // List containing User's vehicles
	private String authToken;

	public User() {
	}

	public static User getInstance() {
		if (instance == null) {
			instance = new User();
			return instance;
		} else {
			return instance;
		}
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}
