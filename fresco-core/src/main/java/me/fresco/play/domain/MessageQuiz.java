package me.fresco.play.domain;

import java.util.Map;

public class MessageQuiz {
	
	private String channel;
	
	private QuizMessage quizMessage;

	private Map<String, Object> headers;
	
	public QuizMessage getQuizMessage() {
		return quizMessage;
	}

	public void setQuizMessage(QuizMessage quizMessage) {
		this.quizMessage = quizMessage;
	}

	@Override
	public String toString() {
		return "MessageQuiz [quizMessage=" + quizMessage + ", headers=" + headers + "]";
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
