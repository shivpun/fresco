package me.fresco.play.constant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.fresco.play.domain.Question;
import me.fresco.play.domain.QuizMessage;

public class MessageUtil {
	
	public static QuizMessage quizMessage(Map<String, Object> map) {
		QuizMessage quizMsg = new QuizMessage();
		quizMsg.setAssestId((Integer)map.get("id"));
		quizMsg.setMinimum((Integer)map.get("passing_marks"));
		Map<String, Object> sections = (Map<String, Object>)((List) map.get("sections")).get(0);
		ObjectMapper obj = new ObjectMapper();
		try {
			String q = obj.writeValueAsString(sections.get("questions"));
			List<Question> questions = obj.readValue(q, new TypeReference<List<Question>>(){});
			quizMsg.setQuestions(questions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quizMsg;
	}
	
}
