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
					throw new InvalidTalkException("Talk may not be scheduled");
				}
			}
		} catch (InvalidTalkException e) {
			e.printStackTrace();
		}
		Collections.sort(objTalkList);
		return objTalkList;
	}

	/**
	 * Prepare the schedule of talks.
	 * 
	 * @param talksList
	 * @return
	 * @throws InvalidTalkException
	 */
	public List<List<Talks>> getSchedule(List<Talks> talksList) throws InvalidTalkException {
		List<Talks> talkTemp = new ArrayList<Talks>();
		talkTemp.addAll(talksList);

		List<List<Talks>> mornings = finCombination(talkTemp, true);
		for (List<Talks> talkList : mornings) {
			talkTemp.removeAll(talkList);
		}

		List<List<Talks>> evenings = finCombination(talkTemp, false);
		for (List<Talks> talkList : evenings) {
			talkTemp.removeAll(talkList);
		}

		// if
		if (!talkTemp.isEmpty()) {
			throw new InvalidTalkException("EMpty Schedule");
		}

		// TODO
		// fill up the remaining minutes

		// TODO
		// If operation list is still not empty, its mean the conference can not
		// be scheduled with the provided data.

		// Print the track lists.
		return printList(mornings, evenings);
	}

	private List<List<Talks>> finCombination(List<Talks> talksListForOperation, boolean isMorning) {
		int minSessionTimeLimit = 180;
		int maxSessionTimeLimit = 240;

		if (isMorning)
			maxSessionTimeLimit = minSessionTimeLimit;

		int talkListSize = talksListForOperation.size();
		List<List<Talks>> possibleCombinationsOfTalks = new ArrayList<List<Talks>>();
		int possibleCombinationCount = 0;

		// Loop to get combination for total possible days.
		// Check one by one from each talk to get possible combination.
		for (int count = 0; count < talkListSize; count++) {
			int startPoint = count;
			int totalTime = 0;
			List<Talks> possibleCombinationList = new ArrayList<Talks>();

			// Loop to get possible combination.
			while (startPoint != talkListSize) {
				int currentCount = startPoint;
				startPoint++;
				Talks currentTalk = talksListForOperation.get(currentCount);
				if (currentTalk.isScheduled())
					continue;
				int talkTime = currentTalk.getTimeDuration();
				// If the current talk time is greater than maxSessionTimeLimit
				// or
				// sum of the current time and total of talk time added in list
				// is greater than maxSessionTimeLimit.
				// then continue.
				if (talkTime > maxSessionTimeLimit || talkTime + totalTime > maxSessionTimeLimit) {
					continue;
				}

				possibleCombinationList.add(currentTalk);
				totalTime += talkTime;

				// If total time is completed for this session than break this
				// loop.
				if (isMorning) {
					if (totalTime == maxSessionTimeLimit)
						break;
				} else if (totalTime >= minSessionTimeLimit)
					break;
			}

			// Valid session time for morning session is equal to
			// maxSessionTimeLimit.
			// Valid session time for evening session is less than or eqaul to
			// maxSessionTimeLimit and greater than or equal to
			// minSessionTimeLimit.
			boolean validSession = false;
			if (isMorning)
				validSession = (totalTime == maxSessionTimeLimit);
			else
				validSession = (totalTime >= minSessionTimeLimit && totalTime <= maxSessionTimeLimit);

			// If session is valid than add this session in the possible
			// combination list and set all added talk as scheduled.
			if (validSession) {
				possibleCombinationsOfTalks.add(possibleCombinationList);
				for (Talks talk : possibleCombinationList) {
					talk.setScheduled(true);
				}
				possibleCombinationCount++;
				if (possibleCombinationCount == totalPossibleDays)
					break;
			}
		}

		return possibleCombinationsOfTalks;
	}

	private List<List<Talks>> printList(List<List<Talks>> combForMornSessions, List<List<Talks>> combForEveSessions) {
		List<List<Talks>> scheduledTalksList = new ArrayList<List<Talks>>();
		int totalPossibleDays = combForMornSessions.size();

		// for loop to schedule event for all days.
		for (int dayCount = 0; dayCount < totalPossibleDays; dayCount++) {
			List<Talks> talkList = new ArrayList<Talks>();

			// Create a date and initialize start time 09:00 AM.
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma ");
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
				talk.setScheduledTime(scheduledTime);
				System.out.println(scheduledTime + talk.getTitle());
				scheduledTime = getNextScheduledTime(date, talk.getTimeDuration());
				talkList.add(talk);
			}

			// Scheduled Lunch Time for 60 mins.
			int lunchTimeDuration = 60;
			Talk lunchTalk = new Talks("Lunch", 60);
			lunchTalk.setScheduledTime(scheduledTime);
			talkList.add(lunchTalk);
			System.out.println(scheduledTime + "Lunch");

			// Evening Session - set the scheduled time in the talk and get the
			// next time using time duration of current talk.
			scheduledTime = getNextScheduledTime(date, lunchTimeDuration);
			List<Talks> eveSessionTalkList = combForEveSessions.get(dayCount);
			for (Talks talk : eveSessionTalkList) {
				talk.setScheduledTime(scheduledTime);
				talkList.add(talk);
				System.out.println(scheduledTime + talk.getTitle());
				scheduledTime = getNextScheduledTime(date, talk.getTimeDuration());
			}

			// Scheduled Networking Event at the end of session, Time duration
			// is just to initialize the Talk object.
			Talks networkingTalk = new Talks("Networking Event", 60);
			networkingTalk.setScheduledTime(scheduledTime);
			talkList.add(networkingTalk);
			System.out.println(scheduledTime + "Networking Event\n");
			scheduledTalksList.add(talkList);
		}

		return scheduledTalksList;
	}

}