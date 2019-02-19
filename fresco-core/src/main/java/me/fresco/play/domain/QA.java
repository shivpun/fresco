package me.fresco.play.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QA {
	
	@JsonProperty(value = "question_id")
	private Integer questions;
	
	@JsonProperty(value = "answer_ids")
	private List<Integer> answers = new ArrayList<Integer>();

	public Integer getQuestions() {
		return questions;
	}

	public void setQuestions(Integer questions) {
		this.questions = questions;
	}

	public List<Integer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Integer> answers) {
		this.answers = answers;
	}
	
	public Integer nextAnswer() {
		return getAnswers().get(0);
	}
	
	public void addAnswer(Integer answer) {
		getAnswers().add(answer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
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
		QA other = (QA) obj;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QA [questions=" + questions + ", answers=" + answers + "]";
	}
}
