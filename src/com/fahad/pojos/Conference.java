package com.fahad.pojos;

import java.util.ArrayList;
import java.util.List;

public class Conference {
	private List<TalkSlots> tracks;

	public Conference() {
		tracks = new ArrayList<TalkSlots>();
	}

	public void addTalk(TalkSlots t) {
		tracks.add(t);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Conference Schedule: \n");
		for (int i = 0; i < tracks.size(); i++) {
			str.append("Track " + (i + 1) + ":\t");
			str.append(tracks.get(i) + "\n");
		}
		return str.toString();
	}

}
