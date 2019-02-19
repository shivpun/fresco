package me.fresco.play.domain;

import java.io.Serializable;
import java.util.Map;

public class MessageFresco implements Serializable {
	
	private String channel;
	
	private FrescoMessage frescoMessage;
	
	private Map<String, Object> headers;

	public FrescoMessage getFrescoMessage() {
		return frescoMessage;
	}

	public void setFrescoMessage(FrescoMessage frescoMessage) {
		this.frescoMessage = frescoMessage;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
