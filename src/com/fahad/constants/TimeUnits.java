package com.fahad.constants;

public enum TimeUnits {
	MINUTES(1, "min"), LIGHTENING(5, "lightning");

	private int time;
	private String representations;

	private TimeUnits(int time, String representations) {
		this.time = time;
		this.representations = representations;
	}

}
