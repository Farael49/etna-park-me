package classes;

import java.util.Calendar;
import java.util.Date;

import android.location.Location;
import android.text.format.DateFormat;

public class PlaceLibre implements Place{
	private Location location;
	private DateFormat dateWhenReady;
	
	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return location;
	}
	
	public DateFormat getDate() {
		return dateWhenReady;
	}
	

 
}
