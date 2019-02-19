package me.fresco.play.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import me.fresco.play.domain.FrescoMessage;
import me.fresco.play.domain.MessageFresco;
import me.fresco.play.domain.MessageQuiz;
import me.fresco.play.domain.QuizMessage;
import me.fresco.play.util.JacksonUtil;

@Configuration
public class ActiveMQConfiguration {
	
	@Autowired
	private ApplicationContext context;
	
	@JmsListener(destination = "fresco.jms.mq.play.next.node")
	public void receiveMessag2e(String message) {
		MessageFresco msg = JacksonUtil.jsonToFrescoMessage(message);
		Message<FrescoMessage> fMsg = JacksonUtil.createFrescoMessage(msg);
		System.out.println("receiveMessag2e"+msg);
		MessageChannel channel = context.getBean("fresco.play.next.node", MessageChannel.class);
		channel.send(fMsg);
	}
	
	@JmsListener(destination = "fresco.jms.mq.contentId")
	public void contentId(String message) {
		MessageFresco msg = JacksonUtil.jsonToFrescoMessage(message);
		Message<FrescoMessage> fMsg = JacksonUtil.createFrescoMessage(msg);
		System.out.println("contentId"+msg);
		MessageChannel channel = context.getBean("fresco.contentId", MessageChannel.class);
		channel.send(fMsg);
	}
	
	@JmsListener(destination = "fresco.jms.mq.quiz.start")
	public void routeNextQuiz(String message) {
		MessageFresco msg = JacksonUtil.jsonToFrescoMessage(message);
		Message<FrescoMessage> fMsg = JacksonUtil.createFrescoMessage(msg);
		System.out.println("routeNextQuiz"+msg);
		MessageChannel channel = context.getBean("fresco.quiz.start", MessageChannel.class);
		channel.send(fMsg);
	}
	
	@JmsListener(destination = "fresco.jms.mq.content")
	public void resetCounter(String message) {
		MessageFresco msg = JacksonUtil.jsonToFrescoMessage(message);
		Message<FrescoMessage> fMsg = JacksonUtil.createFrescoMessage(msg);
		System.out.println("routeNextQuiz"+msg);
		MessageChannel channel = context.getBean("fresco.content", MessageChannel.class);
		channel.send(fMsg);
	}
	
	@JmsListener(destination = "fresco.jms.quiz.verify.QA")
	public void verifyQA(String message) {
		MessageQuiz msg = JacksonUtil.jsonToQuizMessage(message);
		Message<QuizMessage> fMsg = JacksonUtil.createQuizMessage(msg);
		System.out.println("routeNextQuiz"+msg);
		MessageChannel channel = context.getBean("fresco.quiz.verify.QA", MessageChannel.class);
		channel.send(fMsg);
	}
	
	@JmsListener(destination = "fresco.jms.quiz.postQA")
	public void postQA(String message) {
		MessageQuiz msg = JacksonUtil.jsonToQuizMessage(message);
		Message<QuizMessage> fMsg = JacksonUtil.createQuizMessage(msg);
		System.out.println("routeNextQuiz"+msg);
		MessageChannel channel = context.getBean("fresco.quiz.postQA", MessageChannel.class);
		channel.send(fMsg);
	}

	@JmsListener(destination = "fresco.jms.quiz.postToDB")
	public void postQAToDB(String message) {
		MessageQuiz msg = JacksonUtil.jsonToQuizMessage(message);
		Message<QuizMessage> fMsg = JacksonUtil.createQuizMessage(msg);
		System.out.println("routeNextQuiz"+msg);
		MessageChannel channel = context.getBean("fresco.quiz.postToDB", MessageChannel.class);
		channel.send(fMsg);
	}
}
