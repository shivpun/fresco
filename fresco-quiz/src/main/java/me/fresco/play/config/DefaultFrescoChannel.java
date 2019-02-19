package me.fresco.play.config;

import static me.fresco.play.constant.FrescoRoutes.FRESCO_CONTENT;
import static me.fresco.play.constant.FrescoRoutes.FRESCO_NODE;
import static me.fresco.play.constant.FrescoRoutes.FRESCO_PROGRESSES;
import static me.fresco.play.constant.FrescoRoutes.FRESCO_QUIZ_DETAIL;
import static me.fresco.play.constant.FrescoRoutes.FRESCO_TASK;
import static org.springframework.http.HttpMethod.GET;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestTemplate;

import me.fresco.play.constant.FrescoConstant;
import me.fresco.play.constant.FrescoHeader;
import me.fresco.play.constant.MessageUtil;
import me.fresco.play.domain.Content;
import me.fresco.play.domain.FrescoMessage;
import me.fresco.play.domain.Node;
import me.fresco.play.domain.Progress;
import me.fresco.play.domain.Progresses;
import me.fresco.play.domain.QuizMessage;
import me.fresco.play.domain.Task;
import me.fresco.play.util.JacksonUtil;

@Configuration
public class DefaultFrescoChannel {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFrescoChannel.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired(required=false)
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ApplicationContext context;
	
	@Value(value="${fresco.type}")
	private String type;
	
	@Value(value="${fresco.url.fresco}")
	private String fresco;
	
	@Value(value="${fresco.url.quiz}")
	private String quiz;
	
	@Value(value="${fresco.url.web}")
	private String webURL;

	@ServiceActivator(inputChannel = "fresco.start", outputChannel = "fresco.nodeId")
	public Message<FrescoMessage> progressess(Message<FrescoMessage> msg) {
		FrescoMessage payload = msg.getPayload();
		HttpEntity<String> httpEntity = FrescoHeader.headers(payload.getxApiKey());
		URI uri = URI.create(FRESCO_PROGRESSES);
		ResponseEntity<Progresses> progresses = restTemplate.exchange(uri, GET, httpEntity, Progresses.class);
		payload.setProgresses(progresses.getBody().getProgress());
		System.out.println("EXECUTING XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n\n\n\n NODEID:" + payload.getActiveNode() + " | STARTED !! REMAINING: "+ payload.getProgresses().size());
		return msg;
	}
	
	@ServiceActivator(inputChannel = "fresco.play.next.node", outputChannel = "fresco.nodeId")
	public Message<FrescoMessage> frescoNextNode(Message<FrescoMessage> message) {
		FrescoMessage payload = message.getPayload();
		//Collections.shuffle(payload.getProgresses());
		Progress progress = payload.nextProgress();
		payload.createActiveNode(progress.getNodeId());
		//payload.createActiveNode(progress.getNodeId());
		System.out.println("EXECUTING NODEID:"+payload.getActiveNode()+" | STARTED !! REMAINING: "+payload.getProgresses().size());
		payload.getProgresses().remove(progress);
		return message;
	}

	@ServiceActivator(inputChannel = "fresco.nodeId", outputChannel = "fresco.taskId")
	public Message<FrescoMessage> nodeId(Message<FrescoMessage> message) {
		FrescoMessage payload = message.getPayload();
		Node activeNode = payload.getActiveNode();
		HttpEntity<String> httpEntity = FrescoHeader.headers(payload.getxApiKey());
		URI uri = URI.create(String.format(FRESCO_NODE, activeNode.getId()));
		ResponseEntity<Node> nodes = restTemplate.exchange(uri, GET, httpEntity, Node.class);
		payload.setActiveNode(nodes.getBody());
		System.out.println("MESSA" + nodes);
		return message;
	}
	
	@ServiceActivator(inputChannel = "fresco.taskId", outputChannel = "fresco.contentId")
	public Message<FrescoMessage> taskId(Message<FrescoMessage> message) {
		FrescoMessage payload = message.getPayload();
		Node activeNode = payload.getActiveNode();
		List<Task> tasks = activeNode.getTasks();
		HttpEntity<String> httpEntity = FrescoHeader.headers(payload.getxApiKey());
		for (Task task : tasks) {
			URI uri = URI.create(String.format(FRESCO_TASK, task.getId()));
			ResponseEntity<Task> t = restTemplate.exchange(uri, GET, httpEntity, Task.class);
			activeNode.replace(t.getBody());

		}
		Message<FrescoMessage> fMsg = MessageBuilder.withPayload(payload).copyHeaders(message.getHeaders()).setHeader("quizCount", 0).build();
		return fMsg;
	}

	@Filter(inputChannel="fresco.contentId", outputChannel="fresco.transform.content", discardChannel="fresco.quiz.start")
	public boolean contentId(Message<FrescoMessage> message) {
		FrescoMessage payload = message.getPayload();
		Node node = payload.getActiveNode();
		Task task = node.nextTask();
		List<Content> contents = task.getContents();
		HttpEntity<String> httpEntity = FrescoHeader.headers(payload.getxApiKey());
		boolean contentFlag = true;
		List<Content> c1 = new ArrayList<Content>();
		for (Content c : contents) {
			URI uri = URI.create(String.format(FRESCO_CONTENT, new Object[] { node.getId(), task.getId(), c.getId() }));
			ResponseEntity<String> content = restTemplate.exchange(uri, GET, httpEntity, String.class);
			System.out.println("MESSA" + content);
			if (FrescoConstant.CONTENT_TYPE_QUIZ.equalsIgnoreCase(c.getContentType())) {
				System.out.println("QUIZ!!" + c);
				contentFlag = false;
			} else {
				c1.add(c);
			}
		}
		contents.removeAll(c1);
		return contentFlag;
	}
	
	@Transformer(inputChannel = "fresco.transform.content", outputChannel = "fresco.content")
	public Message<FrescoMessage> transformContent(Message<FrescoMessage> message) {
		FrescoMessage payload = message.getPayload();
		Node node = payload.getActiveNode();
		node.getTasks().remove(node.nextTask());
		return message;
	}
	
	@ServiceActivator(inputChannel="fresco.content")
    //@Filter(inputChannel="fresco.content", outputChannel="fresco.contentId", discardChannel="fresco.completed")
	public void content(Message<FrescoMessage> message) {
		System.out.println("content:"+message);
		FrescoMessage payload = message.getPayload();
		Node node = payload.getActiveNode();
		//return node.nextTask()!=null;
		if("web".equalsIgnoreCase(type)) {
			trigFresco(message, "fresco.contentId");
		} else {
			if(node.nextTask()!=null) {
				jmsTemplate.convertAndSend("fresco.jms.mq.contentId", JacksonUtil.frescoMessageToJSON(message));
			}
		}
	}
	
	
	   @ServiceActivator(inputChannel = "fresco.completed")
		public void completed(Message<FrescoMessage> message) {
			System.out.println("COMPLETED :"+message);
			//((ConfigurableApplicationContext)context).stop();
			if("web".equalsIgnoreCase(type)) {
				trigFresco(message, "fresco.play.next.node");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.mq.play.next.node", JacksonUtil.frescoMessageToJSON(message));
			}
		}
	     
	    @ServiceActivator(inputChannel = "fresco.quiz.start", outputChannel="fresco.continue.quiz")
		public Message<QuizMessage> quizStart(Message<FrescoMessage> message) {
	    	FrescoMessage payload = message.getPayload();
			Node node = payload.getActiveNode();
			Task task = node.nextTask();
			Content content = task.nextContent();
			HttpEntity<String> httpEntity = FrescoHeader.headers(payload.getxApiKey());
			URI uri = URI.create(String.format(FRESCO_QUIZ_DETAIL, new Object[] { content.getId() }));
			ResponseEntity<Map> quizDetail = null;
			try {
				quizDetail = restTemplate.exchange(uri, GET, httpEntity, Map.class);
			} catch(Exception e) {
				LOGGER.info("quizStart | EXCEPTION:"+e);
				System.out.println("quizStart | EXCEPTION:"+e);
				if("web".equalsIgnoreCase(type)) {
					trigFresco(message, "fresco.play.next.node");
				} else {
					jmsTemplate.convertAndSend("fresco.jms.mq.play.next.node", JacksonUtil.frescoMessageToJSON(message));
				}
			}
			if(quizDetail!=null) {
				QuizMessage quizMessage = MessageUtil.quizMessage((Map<String, Object>) quizDetail.getBody().get("assessment"));
				quizMessage.setFrescoMessage(payload);
				quizMessage.setApiKey(payload.getxApiKey());
				quizMessage.setProgressId(node.getProgresses().getId());
				quizMessage.setGroupId(content.getGroupId());
				quizMessage.setContentId(content.getId());
				Message<QuizMessage> quizMsg = MessageBuilder.withPayload(quizMessage).copyHeaders(message.getHeaders()).build();
				System.out.println("Quiz:"+quizMessage);
				return quizMsg;
			}
			QuizMessage quizMessage = new QuizMessage();
			return MessageBuilder.withPayload(quizMessage).copyHeaders(message.getHeaders()).build();
		}
	    
	    @Filter(inputChannel="fresco.continue.quiz", outputChannel="fresco.quiz.insert")
		public boolean isContinueQuiz(Message<QuizMessage> message) {
			System.out.println("isContinueQuiz:"+message);
			QuizMessage payload = message.getPayload();
			if(payload.getFrescoMessage()==null) {
				MessageChannel channel = context.getBean("fresco.start", MessageChannel.class);
				Message<FrescoMessage> msg = context.getBean(Message.class);
				channel.send(msg);
			}
			return payload.getFrescoMessage()!=null;
		}
	    
	    public void trigFresco(Message<FrescoMessage> message, String channel) {
	    	HttpEntity<String> httpEntity = new HttpEntity<String>(JacksonUtil.frescoMessageToJSON(message, channel));
	    	restTemplate.exchange(webURL+"/routeFresco?url="+fresco, HttpMethod.POST, httpEntity, String.class);
	    }
	    
	    
	    public void trigQuiz(Message<QuizMessage> message, String channel) {
	    	HttpEntity<String> httpEntity = new HttpEntity<String>(JacksonUtil.quizMessageToJSON(message, channel));
	    	restTemplate.exchange(webURL+"/routeQuiz?url="+quiz, HttpMethod.POST, httpEntity, String.class);
	    }
}
