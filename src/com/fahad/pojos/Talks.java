package com.fahad.pojos;

import com.fahad.constants.TimeUnits;

public class Talks {
	private String topicName;
	private int time;
	private TimeUnits units;

	public Talks(String talkTopic, int time, TimeUnits units) {
		this.topicName = talkTopic;
		this.time = time;
		this.units = units;
	}

	public String getTopicName() {
		return topicName;
	}

	public int getTime() {
		return time;
	}

	public TimeUnits getUnits() {
		return units;
	}

	@Override
	public String toString() {
		return "Talks [topicName=" + topicName + ", time=" + time + ", units=" + units + "]";
	}

}
