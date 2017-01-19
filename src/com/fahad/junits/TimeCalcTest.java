package com.fahad.junits;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fahad.pojos.Talks;
import com.fahad.utility.TimeCalc;

/**
 * JUNITS to test the Utility class
 * 
 * @author fahadahmed
 *
 */
public class TimeCalcTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testGetNextScheduledTime() {
		Date input = new Date();
		input.setHours(9);
		input.setMinutes(00);

		Date expected = new Date();
		expected.setHours(10);
		expected.setMinutes(00);

		int duration = 60;

		Date actual = TimeCalc.getNextScheduledTime(input, duration);
		assertEquals(expected.getHours(), actual.getHours());
	}

	@Test
	public void testGetTotalTalksTime() {
		List<Talks> list = getList();
		int expected = 3;
		assertEquals(expected, TimeCalc.getTotalTalksTime(list));
	}

	public List<Talks> getList() {
		List<Talks> list = new ArrayList<Talks>();
		list.add(new Talks("a", 1));
		list.add(new Talks("b", 1));
		list.add(new Talks("c", 1));
		return list;
	}

}
