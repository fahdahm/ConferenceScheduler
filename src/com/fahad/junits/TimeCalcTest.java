package com.fahad.junits;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import com.fahad.utility.TimeCalc;

public class TimeCalcTest {

	@Test
	public void testGetNextScheduledTime() {
		Date input = new Date();
		input.setHours(9);
		input.setMinutes(0);
		input.setSeconds(0);

		int duration = 60;

		Date expected = new Date();
		expected.setHours(10);
		expected.setMinutes(0);
		expected.setSeconds(0);

		Date actual = TimeCalc.getNextScheduledTime(input, duration);
		assertEquals(expected.getTime(), actual.getTime());
	}

	@Test
	public void testGetTotalTalksTime() {
		fail("Not yet implemented");
	}

}
