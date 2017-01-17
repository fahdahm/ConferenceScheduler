package com.fahad.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.fahad.logic.ConferenceProcessor;
import com.fahad.pojos.Conference;

public class ConferenceStarter {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Supply the input file to schedule");
		}
		File file = new File(args[0]);

		List<String> list = getTalkListFromFile(file);
		Conference conference = new ConferenceProcessor().schdule(list);
		conference.toString();
	}

	public static List<String> getTalkListFromFile(File file) {
		List<String> talkList = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String strLine = reader.readLine();
			while (strLine != null) {
				talkList.add(strLine);
				strLine = reader.readLine();
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		} finally {
		}
		return talkList;
	}

}
