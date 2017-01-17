package com.fahad.pojos;

public class Talks implements Comparable {
	private String topicName;
	private int time;

	public Talks(String talkTopic, int time) {
		this.topicName = talkTopic;
		this.time = time;
	}

	public String getTopicName() {
		return topicName;
	}

	public int getTime() {
		return time;
	}


	@Override
	public String toString() {
		return "Talks [topicName=" + topicName + ", time=" + time + "]";
	}

	@Override
	public int compareTo(Object obj) {
		Talks talk = (Talks) obj;
		if (this.time > talk.time)
			return -1;
		else if (this.time < talk.time)
			return 1;
		else
			return 0;
	}
}
