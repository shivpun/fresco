package me.fresco.play;

import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import me.fresco.play.domain.FrescoMessage;

@EnableIntegration
@EnableScheduling
@SpringBootApplication(scanBasePackages = { "me.fresco.play" })
public class ApplicationLocal {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLocal.class);
	
	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		MessageChannel channel = context.getBean("fresco.start", MessageChannel.class);
		Message<FrescoMessage> message = context.getBean(Message.class);
		channel.send(message);
		System.out.println("FINSIHED !!");
	}
	
	
	@Bean
	public Message<FrescoMessage> frescoMessage(@Value(value="${fresco.employeeId}") String employeeId, @Value(value="${fresco.apiKey}") String apiKey, @Value(value="${fresco.nodeId}") Integer nodeId) {
		FrescoMessage frescoMessage = new FrescoMessage();
		frescoMessage.setEmployeeId(employeeId);
		frescoMessage.setxApiKey(apiKey);
		frescoMessage.createActiveNode(nodeId);
		Message<FrescoMessage> message = MessageBuilder.withPayload(frescoMessage).build();
		return message;
	}
	
	
	@Scheduled(fixedDelay = 3*MILLIS_PER_MINUTE)
	public void schedule() {
		((ConfigurableApplicationContext)context).registerShutdownHook();
		LOGGER.info("SCHEDULEDD !!");
		me.fresco.play.ApplicationLocal.main(new String[0]);
	}
}
