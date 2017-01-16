package com.fahad.pojos;

import java.util.ArrayList;
import java.util.List;

public class TalkSlots {
	private List<Talks> talkList;
	private int remainingTime;
	private int startTime;
	private TalkSlots extraTime;

	public TalkSlots(int remainingTime, int startTime) {
		this.talkList = new ArrayList<Talks>();
		this.remainingTime = remainingTime;
		this.startTime = startTime;
	}

	public void addTalks(Talks t) {
		// check exception
		talkList.add(t);
		remainingTime = remainingTime - t.getTime();

	}

	public boolean canAccomodate(Talks t) {
		return remainingTime >= t.getTime();
	}

	public void addExtra(TalkSlots t) {
		this.extraTime = t;
	}

	// print the stack of the talk list

	public int addTalkListEvents(TalkSlots s) {
		// TODO
		return 0;
	}
}
