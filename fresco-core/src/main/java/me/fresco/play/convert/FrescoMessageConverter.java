package me.fresco.play.convert;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class FrescoMessageConverter implements MessageConverter {

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		System.out.println("CONVERTER");
		Message message = session.createMessage();
		return null;
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		System.out.println("CONVERTER FROM");
		return null;
	}

}
