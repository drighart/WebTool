package org.drdevelopment.webtool.rest.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemStatus {

	private String time;
	
	public SystemStatus() {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		time = dateTime.format(formatter);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
