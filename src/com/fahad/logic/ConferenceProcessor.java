package com.fahad.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fahad.exception.InvalidTalkException;
import com.fahad.pojos.Talks;
import com.fahad.utility.TimeCalc;

public class ConferenceProcessor {

	/**
	 * Method to read data from file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public List<String> createListFromFile(File file) throws IOException {
		List<String> talkList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String strLine = reader.readLine();
			while (strLine != null) {
				talkList.add(strLine);
				strLine = reader.readLine();
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return talkList;
	}

	/**
	 * Method to return sorted list of all talks in file
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talks> sortedTalks(List<String> list) {
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
		Collections.sort(objTalkList);
		return objTalkList;
	}

	/**
	 * Main method to prepare the schedule of talks.
	 * 
	 * @param talksList
	 * @return
	 * @throws InvalidTalkException
	 */
	public List<List<Talks>> getSchedule(List<Talks> talks) throws InvalidTalkException {
		List<Talks> talkTemp = new ArrayList<Talks>();
		int maxEveningLimit = 240;
		talkTemp.addAll(talks);
		int totalTalkDuration = TimeCalc.getTotalTalksTime(talkTemp);

		// using 6 since we have to schedule the talks between 9-12 and 1-4
		int perDayTime = 6 * 60;
		int possibleDays = totalTalkDuration / perDayTime;

		// removing scheduled list from complete list
		List<List<Talks>> mornings = findCombination(talkTemp, possibleDays, true);
		for (List<Talks> list : mornings) {
			talkTemp.removeAll(list);
		}

		// removing scheduled list from complete list
		List<List<Talks>> evenings = findCombination(talkTemp, possibleDays, false);
		for (List<Talks> list : evenings) {
			talkTemp.removeAll(list);
		}

		// Add talks with the remaining minutes
		if (!talkTemp.isEmpty()) {
			List<Talks> scheduledTalks = new ArrayList<Talks>();
			for (List<Talks> listEvening : evenings) {
				int totalEvnTime = TimeCalc.getTotalTalksTime(listEvening);
				for (Talks talkss : talkTemp) {
					int dura = talkss.getDuration();
					if (dura + totalEvnTime <= maxEveningLimit) {
						listEvening.add(talkss);
						talkss.setSchduled(true);
						scheduledTalks.add(talkss);
					}
				}
				talkTemp.removeAll(scheduledTalks);
				if (talkTemp.isEmpty()) {
					break;
				}
			}
		}
		if (!talkTemp.isEmpty()) {
			throw new InvalidTalkException("Cannot schedule. Internal Error");
		}

		// Print the track lists.
		return printList(mornings, evenings);
	}

	/**
	 * Selecting the combination of Talks.
	 * 
	 * @param talkList
	 * @param daysPossible
	 * @param isMorning
	 * @return
	 */
	private List<List<Talks>> findCombination(List<Talks> talkList, int daysPossible, boolean isMorning) {
		int talkListSize = talkList.size();
		int minTime = 180;
		int maxTime = 240;

		if (isMorning) {
			maxTime = minTime;
		}

		List<List<Talks>> combinationOfTalks = new ArrayList<List<Talks>>();
		int combinationCount = 0;

		// Checking one by one from each talk to get possible combination.
		for (int count = 0; count < talkListSize; count++) {

			int startPoint = count;
			int totalTime = 0;

			List<Talks> possibleCombinationList = new ArrayList<Talks>();

			// Loop to get possible combination.
			while (startPoint != talkListSize) {
				int currentCount = startPoint;
				startPoint++;
				Talks currentTalk = talkList.get(currentCount);
				if (currentTalk.isSchduled())
					continue;
				int curentTime = currentTalk.getDuration();

				if (curentTime > maxTime || curentTime + totalTime > maxTime) {
					continue;
				}

				possibleCombinationList.add(currentTalk);
				totalTime = totalTime + curentTime;

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
				combinationOfTalks.add(possibleCombinationList);
				for (Talks talk : possibleCombinationList) {
					talk.setSchduled(true);
				}
				combinationCount++;
				if (combinationCount == daysPossible)
					break;
			}
		}

		return combinationOfTalks;
	}

	private List<List<Talks>> printList(List<List<Talks>> combForMornSessions, List<List<Talks>> combForEveSessions) {
		List<List<Talks>> scheduledTalksList = new ArrayList<List<Talks>>();
		int totalPossibleDays = combForMornSessions.size();

		// for loop to schedule event for all days.
		for (int dayCount = 0; dayCount < totalPossibleDays; dayCount++) {
			List<Talks> talkList = new ArrayList<Talks>();

			// Create a date and initialize start time 09:00 AM.
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
			date.setHours(9);
			date.setMinutes(0);
			date.setSeconds(0);

			int trackCount = dayCount + 1;
			String scheduledTime = dateFormat.format(date);

			System.out.println("Track " + trackCount + ":");

			// Morning Session - set the scheduled time in the talk and get the
			// next time using time duration of current talk.
			List<Talks> mornSessionTalkList = combForMornSessions.get(dayCount);
			for (Talks talk : mornSessionTalkList) {
				talk.setSchTime(scheduledTime);
				System.out.println(scheduledTime + talk.getTopicName());
				scheduledTime = TimeCalc.getNextScheduledTime(date, talk.getDuration());
				talkList.add(talk);
			}

			// Scheduled Lunch Time for 60 mins.
			int lunchTimeDuration = 60;
			Talks lunchTalk = new Talks("Lunch", 60);
			lunchTalk.setSchTime(scheduledTime);
			talkList.add(lunchTalk);
			System.out.println(scheduledTime + "Lunch");

			// Evening Session - set the scheduled time in the talk and get the
			// next time using time duration of current talk.
			scheduledTime = TimeCalc.getNextScheduledTime(date, lunchTimeDuration);
			List<Talks> eveSessionTalkList = combForEveSessions.get(dayCount);
			for (Talks talk : eveSessionTalkList) {
				talk.setSchTime(scheduledTime);
				talkList.add(talk);
				System.out.println(scheduledTime + talk.getTopicName());
				scheduledTime = TimeCalc.getNextScheduledTime(date, talk.getDuration());
			}

			// Scheduled Networking Event at the end of session, Time duration
			// is just to initialize the Talk object.
			Talks networkingTalk = new Talks("Networking Event", 60);
			networkingTalk.setSchTime(scheduledTime);
			talkList.add(networkingTalk);
			System.out.println(scheduledTime + "Networking Event\n");
			scheduledTalksList.add(talkList);
		}

		return scheduledTalksList;
	}

}