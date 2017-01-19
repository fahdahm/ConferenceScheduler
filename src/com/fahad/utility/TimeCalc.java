package com.fahad.utility;

import java.util.Date;
import java.util.List;

import com.fahad.pojos.Talks;

/**
 * Utility Class.
 * 
 * @author fahaahme
 *
 */
public class TimeCalc {

	/**
	 * Method to get the next time after the duration has passed.
	 * 
	 * @param date
	 * @param duration
	 * @return
	 */
	public static Date getNextScheduledTime(Date date, int duration) {
		long time = date.getTime();
		long timeDuration = duration * 60 * 1000;
		long newTime = time + timeDuration;
		Date d = new Date();
		d.setTime(newTime);
		return d;

	}

	/**
	 * Method to get the total time for the passed list.
	 * 
	 * @param list
	 * @return
	 */
	public static int getTotalTalksTime(List<Talks> list) {
		int total = 0;
		if (list == null || list.isEmpty()) {
			return 0;
		}
		for (Talks t : list) {
			total = total + t.getDuration();
		}
		return total;
	}
}
