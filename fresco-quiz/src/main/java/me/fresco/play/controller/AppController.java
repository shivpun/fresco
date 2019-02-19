package me.fresco.play.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.fresco.play.domain.FrescoMessage;
import me.fresco.play.domain.MessageFresco;
import me.fresco.play.domain.MessageQuiz;
import me.fresco.play.domain.QuizMessage;
import me.fresco.play.util.JacksonUtil;

@RestController
public class AppController {
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping(value="/trigFresco", method= {RequestMethod.POST})
	public String trigFresco(@RequestBody String vals) {
		MessageFresco msg = JacksonUtil.jsonToFrescoMessage(vals);
		Message<FrescoMessage> fMsg = JacksonUtil.createFrescoMessage(msg);
		System.out.println("receiveMessag2e"+msg);
		MessageChannel channel = context.getBean(msg.getChannel(), MessageChannel.class);
		channel.send(fMsg);
		return vals;
	}
	
	@RequestMapping(value="/trigQuiz", method= {RequestMethod.POST})
	public String trigQuizMsg(@RequestBody String vals) {
		MessageQuiz msg = JacksonUtil.jsonToQuizMessage(vals);
		Message<QuizMessage> fMsg = JacksonUtil.createQuizMessage(msg);
		System.out.println("routeNextQuiz"+msg);
		MessageChannel channel = context.getBean(msg.getChannel(), MessageChannel.class);
		channel.send(fMsg);
		return vals;
	}
	
	
}
