package me.fresco.play.util;

import java.io.IOException;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.fresco.play.domain.FrescoMessage;
import me.fresco.play.domain.MessageFresco;
import me.fresco.play.domain.MessageQuiz;
import me.fresco.play.domain.QuizMessage;

public class JacksonUtil {
	
	public static String frescoMessageToJSON(MessageFresco frescoMessage) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(frescoMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String quizMessageToJSON(Message<QuizMessage> message) {
		MessageQuiz fresco = createQuizMessage(message);
		return quizMessageToJSON(fresco);
	}
	
	
	public static String quizMessageToJSON(Message<QuizMessage> message, String channel) {
		MessageQuiz fresco = createQuizMessage(message);
		fresco.setChannel(channel);
		return quizMessageToJSON(fresco);
	}
	public static MessageQuiz jsonToQuizMessage(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json.getBytes(), MessageQuiz.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Message<QuizMessage> createQuizMessage(String json) {
		MessageQuiz quiz = JacksonUtil.jsonToQuizMessage(json);
		return MessageBuilder.withPayload(quiz.getQuizMessage()).copyHeaders(quiz.getHeaders()).build();
	}
	
	public static Message<QuizMessage> createQuizMessage(MessageQuiz quiz) {
		return MessageBuilder.withPayload(quiz.getQuizMessage()).copyHeaders(quiz.getHeaders()).build();
	}
	
	public static MessageQuiz createQuizMessage(Message<QuizMessage> message) {
		MessageQuiz fresco = new MessageQuiz();
		fresco.setQuizMessage(message.getPayload());
		fresco.setHeaders(message.getHeaders());
		return fresco;
	}
	
	
	public static String quizMessageToJSON(MessageQuiz frescoMessage) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(frescoMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String frescoMessageToJSON(Message<FrescoMessage> message) {
		MessageFresco fresco = createFrescoMessage(message);
		return frescoMessageToJSON(fresco);
	}
	
	public static String frescoMessageToJSON(Message<FrescoMessage> message, String channel) {
		MessageFresco fresco = createFrescoMessage(message);
		fresco.setChannel(channel);
		return frescoMessageToJSON(fresco);
	}
	
	public static MessageFresco jsonToFrescoMessage(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json.getBytes(), MessageFresco.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Message<FrescoMessage> createFrescoMessage(String json) {
		MessageFresco fresco = JacksonUtil.jsonToFrescoMessage(json);
		return MessageBuilder.withPayload(fresco.getFrescoMessage()).copyHeaders(fresco.getHeaders()).build();
	}
	
	public static Message<FrescoMessage> createFrescoMessage(MessageFresco fresco) {
		return MessageBuilder.withPayload(fresco.getFrescoMessage()).copyHeaders(fresco.getHeaders()).build();
	}
	
	public static MessageFresco createFrescoMessage(Message<FrescoMessage> message) {
		MessageFresco fresco = new MessageFresco();
		fresco.setFrescoMessage(message.getPayload());
		fresco.setHeaders(message.getHeaders());
		return fresco;
	}
}
