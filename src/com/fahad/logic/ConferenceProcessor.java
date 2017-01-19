package com.fahad.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fahad.exception.InvalidTalkException;
import com.fahad.pojos.Talks;
import com.fahad.utility.TimeCalc;

/**
 * Class to do actual processing of the logic.
 * 
 * @author fahadahmed
 *
 */
public class ConferenceProcessor {

	/**
	 * Method to read data from file. Path of File : D:\\input.txt
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public List<String> createListFromFile(File fil) throws IOException {
		List<String> talkList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fil));
			String line = reader.readLine();
			while (line != null) {
				talkList.add(line);
				line = reader.readLine();
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return talkList;
	}

	/**
	 * Method to return sorted list of all talks in file passed.
	 * 
	 * @param list
	 * @return
	 */
	public List<Talks> splittedTalks(List<String> list) {
		List<Talks> objTalkList = new ArrayList<Talks>();

		try {
			for (String talk : list) {
				int lastSpaceIndex = talk.lastIndexOf(" ");
				if (talk.endsWith("60min")) {
					objTalkList.add(new Talks(talk.substring(0, lastSpaceIndex), 60));
				} else if (talk.endsWith("45min")) {
					objTalkList.add(new Talks(talk.substring(0, lastSpaceIndex), 45));
				} else if (talk.endsWith("30min")) {
					objTalkList.add(new Talks(talk.substring(0, lastSpaceIndex), 30));
				} else if (talk.endsWith("lightning")) {
					objTalkList.add(new Talks(talk.substring(0, lastSpaceIndex), 5));
				} else {
					throw new InvalidTalkException("Invalid Time in list. Cannot be scheduled.");
				}
			}
		} catch (InvalidTalkException e) {
			e.printStackTrace();
		}
		return objTalkList;
	}

	/**
	 * Method to prepare the schedule of talks.
	 * 
	 * @param talks
	 * @return
	 * @throws InvalidTalkException
	 */
	public List<List<Talks>> getSchedule(List<Talks> talks) throws InvalidTalkException {
		List<Talks> talkTemp = new ArrayList<Talks>();
		int maxEveningLimit = 240;
		talkTemp.addAll(talks);

		int totalTalkDuration = TimeCalc.getTotalTalksTime(talkTemp);

		// using 6 since we have to schedule the talks between 9-12 and 1-4. We
		// have to schedule all talks before 4.
		int perDayTime = 6 * 60;
		int possibleDays = totalTalkDuration / perDayTime;

		// removing scheduled morning list from complete list
		List<List<Talks>> mornings = findCombination(talkTemp, possibleDays, true);
		for (List<Talks> list : mornings) {
			talkTemp.removeAll(list);
		}

		// removing scheduled evening list from complete list
		List<List<Talks>> evenings = findCombination(talkTemp, possibleDays, false);
		for (List<Talks> list : evenings) {
			talkTemp.removeAll(list);
		}

		// With the remaining time add talk for Scheduled Networking task.
		if (!talkTemp.isEmpty()) {
			List<Talks> allScheduledTalks = new ArrayList<Talks>();

			for (List<Talks> listEvening : evenings) {
				int totalSlotEvnTime = TimeCalc.getTotalTalksTime(listEvening);
				for (Talks talkss : talkTemp) {
					int dura = talkss.getDuration();
					if (dura + totalSlotEvnTime <= maxEveningLimit) {
						talkss.setSchduled(true);
						listEvening.add(talkss);
						allScheduledTalks.add(talkss);
					}
				}
				talkTemp.removeAll(allScheduledTalks);
				if (talkTemp.isEmpty()) {
					break;
				}
			}

		}

		// call method to print track lists
		return printList(mornings, evenings);
	}

	/**
	 * Method to print the tracks by day
	 * 
	 * @param morningSession
	 * @param combForEveSessions
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private List<List<Talks>> printList(List<List<Talks>> morningSession, List<List<Talks>> eveningSessions) {

		List<List<Talks>> scheduledTalksList = new ArrayList<List<Talks>>();
		int noOfDays = morningSession.size();

		// Loop to schedule event for all days.Starting with 1.
		for (int dayCount = 0; dayCount < noOfDays; dayCount++) {

			List<Talks> talkList = new ArrayList<Talks>();

			// Create a date and initialize start time 09:00 AM.
			Date date = new Date();
			date.setHours(9);
			date.setMinutes(0);
			date.setSeconds(0);
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

			int trackCount = dayCount + 1;
			String scheduledTime = dateFormat.format(date);

			System.out.println("Track " + trackCount + ":");

			// Morning Session
			List<Talks> mornList = morningSession.get(dayCount);
			for (Talks talk : mornList) {
				talk.setSchTime(scheduledTime);
				System.out.println(scheduledTime + " " + talk.getTopicName());
				date = TimeCalc.getNextScheduledTime(date, talk.getDuration());
				scheduledTime = dateFormat.format(date);
				talkList.add(talk);
			}

			// Lunch Time
			int lunchTime = 60;
			Talks lunchTalk = new Talks("Lunch", 60);
			lunchTalk.setSchTime(scheduledTime);
			scheduledTime = dateFormat.format(date);
			System.out.println(scheduledTime + " " + "Lunch");
			talkList.add(lunchTalk);

			// Evening Session
			date = TimeCalc.getNextScheduledTime(date, lunchTime);
			scheduledTime = dateFormat.format(date);
			List<Talks> evnList = eveningSessions.get(dayCount);
			for (Talks talk : evnList) {
				talk.setSchTime(scheduledTime);
				System.out.println(scheduledTime + " " + talk.getTopicName());
				date = TimeCalc.getNextScheduledTime(date, talk.getDuration());
				scheduledTime = dateFormat.format(date);
				talkList.add(talk);
			}

			// Scheduled Networking Event
			Talks networkingTalk = new Talks("Networking Event", 60);
			networkingTalk.setSchTime(scheduledTime);
			System.out.println(scheduledTime + " " + "Networking Event\n");
			talkList.add(networkingTalk);

			// creating the schedule list
			scheduledTalksList.add(talkList);
		}

		return scheduledTalksList;
	}

	/**
	 * Selecting the combination of Talks.
	 * 
	 * @param talkList
	 * @param possibleDays
	 * @param isMorning
	 * @return
	 */
	private List<List<Talks>> findCombination(List<Talks> talkList, int possibleDays, boolean isMorning) {
		int size = talkList.size();
		int minTime = 180;
		int maxTime = 240;

		if (isMorning) {
			maxTime = minTime;
		}

		List<List<Talks>> combinationOfTalks = new ArrayList<List<Talks>>();
		int combCount = 0;

		// Checking talks.
		for (int count = 0; count < size; count++) {

			int startPoint = count;
			int totalTime = 0;

			List<Talks> possibleList = new ArrayList<Talks>();

			// Loop to get possible combination.
			while (startPoint != size) {
				int currentCount = startPoint;
				startPoint++;
				Talks currentTalk = talkList.get(currentCount);
				if (currentTalk.isSchduled()) {
					continue;
				}

				int talkTime = currentTalk.getDuration();

				if (talkTime > maxTime || talkTime + totalTime > maxTime) {
					continue;
				}

				possibleList.add(currentTalk);
				totalTime = totalTime + talkTime;

				if (isMorning) {
					if (totalTime == maxTime)
						break;
				} else if (totalTime >= minTime)
					break;
			}

			boolean validSession = false;
			if (isMorning)
				validSession = (totalTime == maxTime);
			else
				validSession = (totalTime >= minTime && totalTime <= maxTime);

			if (validSession) {
				combinationOfTalks.add(possibleList);
				for (Talks talk : possibleList) {
					talk.setSchduled(true);
				}
				combCount++;
				if (combCount == possibleDays)
					break;
			}
		}
		return combinationOfTalks;
	}

}