package com.example.parkme;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TimePicker;

public class MyTimePicker extends TimePickerDialog {
	private int minHour = -1;
	private int minMinute = -1;

	private int maxHour = 25;
	private int maxMinute = 25;

	private int currentHour = 0;
	private int currentMinute = 0;

	public MyTimePicker(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
	}

	@Override
	public void updateTime(int hourOfDay, int minutOfHour) {
		// TODO Auto-generated method stub
		super.updateTime(hourOfDay, minutOfHour);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		boolean validTime = true;
		if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
			validTime = false;
		}
		if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
			validTime = false;
		}

		if (validTime) {
			currentHour = hourOfDay;
			currentMinute = minute;
		}

		updateTime(currentHour, currentMinute);
	}

	public void setMin(int hour, int minute) {
		minHour = hour;
		minMinute = minute;
	}

	public void setMax(int hour, int minute) {
		maxHour = hour;
		maxMinute = minute;
	}

}
