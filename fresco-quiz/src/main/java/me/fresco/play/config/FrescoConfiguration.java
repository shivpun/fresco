package me.fresco.play.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FrescoConfiguration {

	@Bean
	public DataSource dataSource(@Value(value = "${spring.datasource.url}") String url,

			@Value(value = "${spring.datasource.username}") String username,

			@Value(value = "${spring.datasource.password}") String password,

			@Value(value = "${spring.datasource.driver-class-name}") String driverClassName) {

		DataSource dataSource = DataSourceBuilder.create().url(url).username(username).password(password)
				.driverClassName(driverClassName).build();

		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean(name = "fresco.start")
	public MessageChannel frescoStart() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	@Bean(name = "fresco.play.next.node")
	public MessageChannel frescoNextNode() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	@Bean(name = "fresco.contentId")
	public MessageChannel contentId() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	@Bean(name = "fresco.quiz.start")
	public MessageChannel routeNextQuiz() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	
	@Bean(name = "fresco.content")
	public MessageChannel resetCounter() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	@Bean(name = "fresco.quiz.verify.QA")
	public MessageChannel verifyQA() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	@Bean(name = "fresco.quiz.postQA")
	public MessageChannel postQA() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
	
	@Bean(name = "fresco.quiz.postToDB")
	public MessageChannel postQAToDB() {
		DirectChannel channel = new DirectChannel();
		channel.setMaxSubscribers(Integer.MAX_VALUE);
		return channel;
	}
}
