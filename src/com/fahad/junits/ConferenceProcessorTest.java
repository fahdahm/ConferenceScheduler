package com.fahad.junits;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fahad.exception.InvalidTalkException;
import com.fahad.logic.ConferenceProcessor;
import com.fahad.pojos.Talks;

/**
 * JUNITS for testing ConferenceProcessor class.
 * 
 * @author fahadahmed
 *
 */
public class ConferenceProcessorTest {

	ConferenceProcessor cp = new ConferenceProcessor();

	@Test
	public void testCreateListFromFile() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/input.txt");
		File file = new File(url.toURI());
		assertNotNull(cp.createListFromFile(file));
	}

	@Test
	public void testSortedTalks() {
		List<String> list = new ArrayList<String>();
		list.add("Writing Fast Tests Against Enterprise Rails 60min");
		list.add("Overdoing it in Python 45min");
		list.add("Lua for the Masses 30min");
		assertNotNull(cp.splittedTalks(list));
	}

	@Test
	public void testGetSchedule() throws InvalidTalkException {
		List<Talks> list = new ArrayList<Talks>();
		list.add(new Talks("TestTopic", 60));
		list.add(new Talks("TestTopic", 45));
		list.add(new Talks("TestTopic", 30));
		list.add(new Talks("TestTopic", 45));
		list.add(new Talks("TestTopic", 5));
		list.add(new Talks("TestTopic", 45));
		list.add(new Talks("TestTopic", 60));
		list.add(new Talks("TestTopic", 45));
		list.add(new Talks("TestTopic", 60));
		list.add(new Talks("TestTopic", 45));
		assertNotNull(cp.getSchedule(list));
	}
}
