package me.fresco.play.config;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.fresco.play.constant.FrescoConstant;
import me.fresco.play.constant.FrescoHeader;
import me.fresco.play.constant.FrescoRoutes;
import me.fresco.play.domain.Answers;
import me.fresco.play.domain.FrescoMessage;
import me.fresco.play.domain.Node;
import me.fresco.play.domain.PostQA;
import me.fresco.play.domain.PostResult;
import me.fresco.play.domain.QA;
import me.fresco.play.domain.Question;
import me.fresco.play.domain.QuizMessage;
import me.fresco.play.domain.Secton;
import me.fresco.play.domain.Task;
import me.fresco.play.util.JacksonUtil;

@Configuration
public class FrescoQuizChannel {

	private static final Logger LOGGER = LoggerFactory.getLogger(FrescoQuizChannel.class);

	@Autowired
	private FrescoDB frescoDB;

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

	@ServiceActivator(inputChannel = "fresco.quiz.insert", outputChannel = "fresco.quiz.verify.QA")
	public Message<QuizMessage> quizInsert(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		List<Question> questions = payload.getQuestions();
		for (Question question : questions) {
			frescoDB.question(question);
			frescoDB.answer(question.getAnswers());
			frescoDB.QA(question, payload.getAssestId(), payload.getContentId(), payload.getScore(), payload.getTotal(),
					payload.getMinimum());
		}
		System.out.println("quizInsert :" + message);
		return message;
	}


	@ServiceActivator(inputChannel = "fresco.quiz.verify.QA", outputChannel = "fresco.quiz.isfinal")
	public Message<QuizMessage> verifyDB(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		List<Question> questions = payload.getQuestions();
		for (Question question : questions) {
			List<Integer> correctId = frescoDB.correct(question);
			List<Integer> wrongId = frescoDB.wrong(question);
			boolean correct = false;
			if (!CollectionUtils.isEmpty(correctId)) {
				for (Integer ansId : correctId) {
					Answers answer = question.fetchAnswers(ansId);
					if (answer != null) {
						answer.setType(FrescoConstant.CORRECT);
						correct = true;
						break;
					}
				}
			}
			if (!correct && !CollectionUtils.isEmpty(wrongId)) {
				for (Integer ansId : wrongId) {
					Answers answer = question.fetchAnswers(ansId);
					if (answer != null) {
						answer.setType(FrescoConstant.WRONG);
					}
				}
			}
		}
		System.out.println("verifyDB :" + message);
		return message;
	}
	
	@ServiceActivator(inputChannel = "fresco.quiz.isfinal", outputChannel = "fresco.quiz.prepare.QA")
	public Message<QuizMessage> isFinalQuiz(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		FrescoMessage frescoMessage = payload.getFrescoMessage();
		Node activeNode = frescoMessage.getActiveNode();
		Task task = activeNode.nextTask();
		if((task==null || activeNode.getTasks().size()<2)) {
			List<QA> qas = frescoDB.findByFinalQC(frescoMessage.getActiveNode().getId());
		}
		return message;
		
	}

	@ServiceActivator(inputChannel = "fresco.quiz.prepare.QA", outputChannel = "fresco.quiz.wrong.QA")
	public Message<QuizMessage> prepareQA(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		FrescoMessage frescoMessage = payload.getFrescoMessage();
		Node activeNode = frescoMessage.getActiveNode();
		Task task = activeNode.nextTask();
		List<QA> qas = new ArrayList<QA>();
		List<QA> corrects = new ArrayList<QA>();
		List<QA> qaSections = new ArrayList<QA>();
		if((task==null || activeNode.getTasks().size()<2)) {
			qas = frescoDB.findByFinalQC(frescoMessage.getActiveNode().getId());
		}
		
		Secton secton = new Secton();
		PostQA postQA = new PostQA();
		postQA.setProgressId(payload.getProgressId());
		postQA.setContentId(payload.getContentId());
		secton.setSectionId(payload.getAssestId());
		postQA.addSection(secton);
		
		for (Question q : payload.getQuestions()) {
			QA qa = new QA();
			qa.setQuestions(q.getId());
			//secton.addQA(qa);
			qaSections.add(qa);
			Answers correct = q.correct();
			if (correct != null && correct.getId() != null) {
				corrects.add(qa);
				qa.addAnswer(correct.getId());
			}  else {
				List<Answers> attemp = q.attemp();
				if (CollectionUtils.isEmpty(attemp)) {
					System.out.println("EMPTY");
				}
				for (Answers ans : attemp) {
					qa.addAnswer(ans.getId());
					break;
				}
			}
		}
		for(QA qa:qaSections) {
			if(CollectionUtils.isEmpty(qas) && qas.contains(qa)) {
				secton.addQA(qa);
			}else if(CollectionUtils.isEmpty(qas) && !qas.contains(qa)) {
				boolean flag = false;
				for(QA qc:qas) {
					if(!corrects.contains(qc)) {
						flag = true;
						secton.addQA(qc);
						break;
					}
				}
				if(!flag) {
					secton.addQA(qa);
				}
			} else {
				secton.addQA(qa);
			} 
		}
		payload.setPostQA(postQA);
		System.out.println("prepareQA :" + message);
		return message;
	}

	@ServiceActivator(inputChannel = "fresco.quiz.wrong.QA")
	public void checkWQA(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostQA postQA = payload.getPostQA();
		Secton secton = postQA.getSection().get(0);
		boolean isEmpty = false;
		QA empty = null;
		for (QA qa : secton.getQas()) {
			if (CollectionUtils.isEmpty(qa.getAnswers())) {
				empty = qa;
				isEmpty = true;
			}
			if (isEmpty) {
				break;
			}
		}
		if (isEmpty) {
			frescoDB.deleteQW(empty);
			if("web".equalsIgnoreCase(type)) {
				trigQuiz(message, "fresco.quiz.verify.QA");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.quiz.verify.QA", JacksonUtil.quizMessageToJSON(message));
			}
		} else {
			for (QA q : secton.getQas()) {
				if (payload.getNews() != null && payload.getNews().getQuestions().equals(q.getQuestions())) {
					QA aq = new QA();
					System.out.println(payload.getNews());
					aq.setQuestions(payload.getNews().getQuestions());
					aq.addAnswer(payload.getNews().nextAnswer());
					payload.setPrevs(aq);
					payload.setNews(q);
				}
			}
			payload.addPostQA(postQA);
			if("web".equalsIgnoreCase(type)) {
				trigQuiz(message, "fresco.quiz.postQA");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.quiz.postQA", JacksonUtil.quizMessageToJSON(message));
			}
		}
	}

	@ServiceActivator(inputChannel = "fresco.quiz.postQA", outputChannel = "fresco.quiz.continue")
	public Message<QuizMessage> postQA(Message<QuizMessage> message) throws IOException {
		QuizMessage payload = message.getPayload();
		PostQA postQA = payload.getPostQA();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		ObjectMapper mapper = new ObjectMapper();
		String values = mapper.writeValueAsString(postQA);
		map.add("data", values);
		HttpEntity<Object> httpEntity = FrescoHeader.headers(map, payload.getApiKey());

		ResponseEntity<Map> postResultEntity = null;
		try {
			postResultEntity = restTemplate.exchange(FrescoRoutes.FRESCO_POST_RESULT, HttpMethod.POST, httpEntity,
					Map.class);
		} catch (Exception e) {
			LOGGER.info("quizStart | EXCEPTION:" + e);
			payload.setPostQAs(new ArrayList<>());
			Message<FrescoMessage> frescoMsg = MessageBuilder.withPayload(payload.getFrescoMessage())
					.copyHeaders(message.getHeaders()).build();
			if("web".equalsIgnoreCase(type)) {
				trigFresco(frescoMsg, "fresco.play.next.node");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.mq.play.next.node", JacksonUtil.frescoMessageToJSON(frescoMsg));
			}
		}
		if (postResultEntity != null) {
			String response = mapper.writeValueAsString(postResultEntity.getBody().get("assessment"));
			PostResult postResult = mapper.readValue(response.getBytes(), PostResult.class);
			payload.setActive(postResult);
			System.out.println("postQA :" + response + "\nPOSTVALUE:" + values + "\nActiveNODE:"
					+ payload.getFrescoMessage().getActiveNode().getId());
			return message;
		}
		QuizMessage quizMessage = new QuizMessage();
		return MessageBuilder.withPayload(quizMessage).copyHeaders(message.getHeaders()).build();
	}

	@ServiceActivator(inputChannel = "fresco.quiz.continue")
	// @Filter(inputChannel="fresco.quiz.continue",
	// outputChannel="fresco.quiz.postToDB", discardChannel="fresco.completed")
	public void isContinueQuiz(Message<QuizMessage> message) {
		System.out.println("isContinueQuiz:" + message);
		QuizMessage payload = message.getPayload();
		// return payload.getFrescoMessage()!=null;
		if (payload.getFrescoMessage() != null) {
			if("web".equalsIgnoreCase(type)) {
				trigQuiz(message, "fresco.quiz.postToDB");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.quiz.postToDB", JacksonUtil.quizMessageToJSON(message));
			}
		} else {
			MessageChannel channel = context.getBean("fresco.start", MessageChannel.class);
			Message<FrescoMessage> msg = context.getBean(Message.class);
			channel.send(msg);
		}
	}

	@Filter(inputChannel = "fresco.quiz.score.zero", outputChannel = "fresco.score.zero", discardChannel = "fresco.quiz.score.total")
	public boolean scoreZero(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostResult postResult = payload.getActive();
		return postResult != null && postResult.getScore() == 0;
	}

	@Filter(inputChannel = "fresco.quiz.score.total", outputChannel = "fresco.quiz.transform.frescoMsg", discardChannel = "fresco.quiz.score.min")
	public boolean scoreEqualToTotal(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostResult postResult = payload.getActive();
		return postResult != null && postResult.getScore() == postResult.getQuizMark();
	}

	@Filter(inputChannel = "fresco.quiz.score.min", outputChannel = "fresco.quiz.score.min.save", discardChannel = "fresco.quiz.score.prev.QA")
	public boolean scoreEqualToMin(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostResult postResult = payload.getActive();
		return postResult != null && postResult.getScore() >= postResult.getPassMark();
	}

	@ServiceActivator(inputChannel = "fresco.quiz.transform.frescoMsg", outputChannel = "fresco.quiz.next")
	public Message<FrescoMessage> freco(Message<QuizMessage> qMsg) {
		QuizMessage quizMessage = qMsg.getPayload();
		FrescoMessage frescoMessage = quizMessage.getFrescoMessage();
		Integer count = 0;
		Object obj = qMsg.getHeaders().get("quizCount");
		if (obj == null) {
			count = count + 1;
		} else {
			count = (Integer) obj + 1;
		}
		Message<FrescoMessage> message = MessageBuilder.withPayload(frescoMessage).copyHeaders(qMsg.getHeaders())
				.setHeader("quizCount", count).build();
		return message;
	}

	@ServiceActivator(inputChannel = "fresco.quiz.next") // , outputChannel="fresco.content")
	// @Filter(inputChannel="fresco.quiz.next", outputChannel="fresco.quiz.start",
	// discardChannel="fresco.next.content")
	public void routeNextQuiz(Message<FrescoMessage> fMsg) {
		Integer obj = (Integer) fMsg.getHeaders().get("quizCount");
		// return obj!=null && obj<=6;
		if (obj != null && obj <= 2) {
			if("web".equalsIgnoreCase(type)) {
				trigFresco(fMsg, "fresco.quiz.start");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.mq.quiz.start", JacksonUtil.frescoMessageToJSON(fMsg));
			}
		} else {
			FrescoMessage payload = fMsg.getPayload();
			Node node = payload.getActiveNode();
			node.getTasks().remove(node.nextTask());
			Message<FrescoMessage> message = MessageBuilder.withPayload(payload).copyHeaders(fMsg.getHeaders())
					.setHeader("quizCount", 0).build();
			if("web".equalsIgnoreCase(type)) {
				trigFresco(message, "fresco.contentId");
			} else {
				jmsTemplate.convertAndSend("fresco.jms.mq.content", JacksonUtil.frescoMessageToJSON(message));
			}
		}
	}

	@ServiceActivator(inputChannel = "fresco.next.content", outputChannel = "fresco.content")
	public Message<FrescoMessage> resetCounter(Message<FrescoMessage> fMsg) {
		FrescoMessage payload = fMsg.getPayload();
		Node node = payload.getActiveNode();
		node.getTasks().remove(node.nextTask());
		Message<FrescoMessage> message = MessageBuilder.withPayload(payload).copyHeaders(fMsg.getHeaders())
				.setHeader("quizCount", 0).build();
		return message;
	}

	@ServiceActivator(inputChannel = "fresco.quiz.score.min.save", outputChannel = "fresco.quiz.score.prev.QA")
	public Message<QuizMessage> updateMinScore(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostResult active = payload.getActive();
		PostQA activePostQA = payload.getPostQA();
		List<QA> qas = activePostQA.getSection().get(0).getQas();
		frescoDB.saveMinScore(qas, active.getQcs(), payload.getQuestions());
		return message;
	}

	@Filter(inputChannel = "fresco.quiz.score.prev.QA", outputChannel = "fresco.quiz.score.prev.notExist", discardChannel = "fresco.quiz.score.prev.Exist")
	public boolean prevExist(Message<QuizMessage> qMsg) {
		QuizMessage payload = qMsg.getPayload();
		return payload.getLast() == null;
	}

	@Filter(inputChannel = "fresco.quiz.score.prev.Exist", outputChannel = "fresco.quiz.score.prev.Equal", discardChannel = "fresco.quiz.score.prev.greater")
	public boolean isPrevEqual(Message<QuizMessage> message) {
		QuizMessage quizMessage = message.getPayload();
		PostResult last = quizMessage.getLast();
		PostResult active = quizMessage.getActive();
		return last.getScore() == active.getScore();
	}

	@ServiceActivator(inputChannel = "fresco.quiz.score.prev.Equal", outputChannel="check")
	// @ServiceActivator(inputChannel="fresco.quiz.score.prev.Equal",
	// outputChannel="fresco.quiz.verify.QA")
	public Message<QuizMessage> prevEqual(Message<QuizMessage> message) {
		QuizMessage quizMessage = message.getPayload();
		List<QA> qas = new ArrayList<QA>();
		if(quizMessage.getNews()!=null) {
		qas.add(quizMessage.getNews());
		}
		if(quizMessage.getPrevs()!=null) {
		qas.add(quizMessage.getPrevs());
		}
		frescoDB.insertQW(qas);
		// quizMessage.setNews(null);
		quizMessage.setPrevs(null);
		 return message;
		//jmsTemplate.convertAndSend("fresco.jms.quiz.verify.QA", JacksonUtil.quizMessageToJSON(message));
	}

	@Filter(inputChannel = "fresco.quiz.score.prev.greater", outputChannel = "fresco.quiz.score.prev.greater.right", discardChannel = "fresco.quiz.score.prev.less")
	public boolean isPrevGreater(Message<QuizMessage> message) {
		QuizMessage quizMessage = message.getPayload();
		PostResult last = quizMessage.getLast();
		PostResult active = quizMessage.getActive();
		return last.getScore() < active.getScore();
	}

	@ServiceActivator(inputChannel = "fresco.quiz.score.prev.greater.right", outputChannel="check")
	// @ServiceActivator(inputChannel="fresco.quiz.score.prev.greater.right",
	// outputChannel="fresco.quiz.verify.QA")
	public Message<QuizMessage> prevGreater(Message<QuizMessage> message) {
		QuizMessage quizMessage = message.getPayload();
		List<QA> qasC = new ArrayList<QA>();
		if(quizMessage.getNews()!=null) {
		qasC.add(quizMessage.getNews());
		}
		List<QA> qasW = new ArrayList<QA>();
		if(quizMessage.getPrevs()!=null) {
		qasW.add(quizMessage.getPrevs());
		}
		try {
			frescoDB.insertQC(qasC);
			//frescoDB.insertQW(qasW);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("EXCEPTIOB");
		}
		// quizMessage.setNews(null);
		quizMessage.setPrevs(null);
		return message;
		
	}
	
	@ServiceActivator(inputChannel="check")
	public void checkWC(Message<QuizMessage> message) {
		QuizMessage quizMessage = message.getPayload();
		Secton secton = quizMessage.getPostQA().getSection().get(0);
		List<QA> qas = secton.getQas();
		for(Question question:quizMessage.getQuestions()) {
			QA news = quizMessage.getNews();
			if(news!=null && news.getQuestions().equals(question.getId())) {
				int count = 0, ansize=question.getAnswers().size();
				for(Answers anws:question.getAnswers()) {
					if(FrescoConstant.WRONG.equals(anws.getType())) {
						count = count + 1;
					}
					if(count==(ansize-1)) {
						quizMessage.setPrevs(null);
						quizMessage.setNews(null);
					}
				}
			}
		}
		if("web".equalsIgnoreCase(type)) {
			trigQuiz(message, "fresco.quiz.verify.QA");
		} else {
			jmsTemplate.convertAndSend("fresco.jms.quiz.verify.QA", JacksonUtil.quizMessageToJSON(message));
		}
	}

	@ServiceActivator(inputChannel = "fresco.quiz.score.prev.less", outputChannel="check")
	// @ServiceActivator(inputChannel="fresco.quiz.score.prev.less",
	// outputChannel="fresco.quiz.verify.QA")
	public Message<QuizMessage> prevLess(Message<QuizMessage> message) {
		QuizMessage quizMessage = message.getPayload();

		List<QA> qasC = new ArrayList<QA>();
		if(quizMessage.getPrevs()!=null) {
		qasC.add(quizMessage.getPrevs());
		}

		List<QA> qasW = new ArrayList<QA>();
		if(quizMessage.getNews()!=null) {
		qasW.add(quizMessage.getNews());
		}

		//frescoDB.insertQW(qasW);
		frescoDB.insertQC(qasC);
		// quizMessage.setNews(null);
		quizMessage.setPrevs(null);
		 return message;
		//jmsTemplate.convertAndSend("fresco.jms.quiz.verify.QA", JacksonUtil.quizMessageToJSON(message));
	}

	@ServiceActivator(inputChannel = "fresco.quiz.score.prev.notExist")
	// @ServiceActivator(inputChannel="fresco.quiz.score.prev.notExist",
	// outputChannel="fresco.quiz.postQA")
	public void updateNext(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		payload.setLast(payload.getActive());
		PostQA postQA = payload.getPostQA();
		List<QA> qas = postQA.getSection().get(0).getQas();
		boolean flag = false;
		for (Question qs : payload.getQuestions()) {
			Answers correctAns = qs.correct();
			List<Answers> attempAns = qs.attemp();
			QA qa = new QA();
			qa.setQuestions(qs.getId());
			int index = qas.indexOf(qa);
			if (correctAns == null && index > -1) {
				QA ansId = qas.get(index);
				for (Answers ans : attempAns) {
					if (!ansId.nextAnswer().equals(ans.getId())) {
						qa.addAnswer(ans.getId());
						payload.setPrevs(ansId);
						payload.setNews(qa);
						System.out.println("updateNext | NEWS:" + payload.getNews() + " | OLD:" + payload.getPrevs());
						qas.remove(index);
						qas.add(qa);
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
		System.out.println("updateNext :" + postQA);
		// return message;
		if("web".equalsIgnoreCase(type)) {
			trigQuiz(message, "fresco.quiz.verify.QA");
		} else {
			jmsTemplate.convertAndSend("fresco.jms.quiz.verify.QA", JacksonUtil.quizMessageToJSON(message));
		}
	}

	@ServiceActivator(inputChannel = "fresco.score.zero")
	// @ServiceActivator(inputChannel="fresco.score.zero",
	// outputChannel="fresco.quiz.verify.QA")
	public void updateScoreZero(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostResult active = payload.getActive();
		PostQA activePostQA = payload.getPostQA();
		List<QA> qas = activePostQA.getSection().get(0).getQas();
		frescoDB.insertQW(qas);
		if("web".equalsIgnoreCase(type)) {
			trigQuiz(message, "fresco.quiz.verify.QA");
		} else {
			jmsTemplate.convertAndSend("fresco.jms.quiz.verify.QA", JacksonUtil.quizMessageToJSON(message));
		}
		// return message;
	}

	@ServiceActivator(inputChannel = "fresco.quiz.postToDB", outputChannel = "fresco.quiz.score.zero")
	public Message<QuizMessage> postQAToDB(Message<QuizMessage> message) {
		QuizMessage payload = message.getPayload();
		PostResult active = payload.getActive();
		PostQA activePostQA = payload.getPostQA();
		List<QA> qas = activePostQA.getSection().get(0).getQas();
		frescoDB.AT(qas, active, payload.getContentId());
		return message;
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
