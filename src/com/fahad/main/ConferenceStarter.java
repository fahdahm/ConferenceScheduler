package com.fahad.main;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.fahad.exception.InvalidTalkException;
import com.fahad.logic.ConferenceProcessor;
import com.fahad.pojos.Talks;

public class ConferenceStarter {

	public static void main(String[] args) {
		String fileName = "D:\\input.txt";
		File file = new File(fileName);
		ConferenceProcessor cf = new ConferenceProcessor();

		// Read test file
		List<String> rawList = cf.createListFromFile(file);

		// Sort the list of Talk
		List<Talks> sortedList = cf.sortedTalks(rawList);

		// Find the schdule of List
		try {
			List<List<Talks>> finalList = cf.getSchedule(sortedList);
		} catch (InvalidTalkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
