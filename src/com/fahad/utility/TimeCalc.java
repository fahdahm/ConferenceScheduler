package com.fahad.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fahad.pojos.Talks;

public class TimeCalc {

	public static String getNextScheduledTime(Date date, int duration) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma ");
		long time = date.getTime();
		long timeDuration = duration * 60 * 1000;
		long newTime = time + timeDuration;
		Date d = new Date();
		d.setTime(newTime);
		return dateFormat.format(d);
	}

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
