package com.fahad.pojos;

@SuppressWarnings("rawtypes")
public class Talks implements Comparable {
	private String topicName;
	private int duration;
	private boolean schduled;
	private String schTime;

	public Talks(String topicName, int time) {
		this.topicName = topicName;
		this.duration = time;
	}

	public Talks(String topicName, int time, boolean schduled) {
		this.topicName = topicName;
		this.duration = time;
		this.schduled = schduled;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getSchTime() {
		return schTime;
	}

	public void setSchTime(String schTime) {
		this.schTime = schTime;
	}

	public boolean isSchduled() {
		return schduled;
	}

	public void setSchduled(boolean schduled) {
		this.schduled = schduled;
	}

	@Override
	public String toString() {
		return "Talks [topicName=" + topicName + ", time=" + schTime + "]";
	}

	@Override
	public int compareTo(Object obj) {
		Talks talk = (Talks) obj;
		if (this.duration > talk.duration)
			return -1;
		else if (this.duration < talk.duration)
			return 1;
		else
			return 0;
	}
}
