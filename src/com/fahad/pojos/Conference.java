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

}
