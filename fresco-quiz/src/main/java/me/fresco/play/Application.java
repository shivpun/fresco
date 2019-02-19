package me.fresco.play;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import me.fresco.play.domain.FrescoMessage;

@EnableScheduling
@EnableIntegration
@EnableJms
@IntegrationComponentScan(basePackages = { "me.fresco.play" })
@SpringBootApplication(scanBasePackages = { "me.fresco.play" })
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		//JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		//Object obj = context.getBean("fresco.play.next.node");
		MessageChannel channel = context.getBean("fresco.start", MessageChannel.class);
		Message<FrescoMessage> message = context.getBean(Message.class);
		channel.send(message);
		//jmsTemplate.convertAndSend("fresco.jms.mq.progress.start", JacksonUtil.frescoMessageToJSON(message));
		System.out.println("FINSIHED !!");
	}

	@Bean
	public Message<FrescoMessage> frescoMessage(@Value(value = "${fresco.employeeId}") String employeeId,
			@Value(value = "${fresco.apiKey}") String apiKey, @Value(value = "${fresco.nodeId}") Integer nodeId) {
		FrescoMessage frescoMessage = new FrescoMessage();
		frescoMessage.setEmployeeId(employeeId);
		frescoMessage.setxApiKey(apiKey);
		frescoMessage.createActiveNode(nodeId);
		Message<FrescoMessage> message = MessageBuilder.withPayload(frescoMessage).build();
		return message;
	}
	
}
