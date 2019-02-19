package me.fresco.play.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.fresco.play.constant.FrescoConstant;

@JsonIgnoreProperties
public class Question implements Serializable {

	@JsonProperty(value = "id")
	private Integer id;

	@JsonProperty(value = "question_no")
	private Integer questionNo;

	@JsonProperty(value = "question")
	private String question;
	
	@JsonProperty(value = "question_type")
	private String questionType;
	
	@JsonProperty(value="marks")
	private Integer marks;
	
	@JsonIgnore
	private String feedback;
	
	@JsonIgnore
	private String lock_version;

	@JsonProperty(value = "answers")
	private List<Answers> answers = new ArrayList<Answers>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(Integer questionNo) {
		this.questionNo = questionNo;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Answers> getAnswers() {
		return answers;
	}
	
	public Answers correct() {
		Answers ans = null;
		for(Answers answer:getAnswers()) {
			if(FrescoConstant.CORRECT.equalsIgnoreCase(answer.getType())) {
				ans=answer;
				break;
			}
		}
		return ans;
	}
	
	public List<Answers> attemp() {
		List<Answers> answer = new ArrayList<Answers>();
		for(Answers ans:getAnswers()) {
			if(!FrescoConstant.WRONG.equalsIgnoreCase(ans.getType())) {
				answer.add(ans);
			}
		}
		return answer;
	}
	
	public Answers fetchAnswers(Integer answerId) {
		Answers o = new Answers();
		o.setId(answerId);
		int index = getAnswers().indexOf(o);
		if(index>-1) {
			return getAnswers().get(index);
		}
		return null;
	}

	public void setAnswers(List<Answers> answers) {
		this.answers = answers;
	}
	
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Integer getMarks() {
		return marks;
	}

	public void setMarks(Integer marks) {
		this.marks = marks;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getLock_version() {
		return lock_version;
	}

	public void setLock_version(String lock_version) {
		this.lock_version = lock_version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", questionNo=" + questionNo + ", question=" + question + ", questionType="
				+ questionType + ", answers=" + answers + "]";
	}

}
