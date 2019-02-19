package me.fresco.play.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value= {"percentile", "total_correct_answers", "miles", "miles_earned", "total_questions_attempted","failure_hash"})
public class PostResult implements Serializable {
	
	@JsonProperty(value = "user_score")
	private Integer score;
	
	@JsonProperty(value = "passing_marks")
	private Integer passMark;
	
	@JsonProperty(value = "quiz_marks")
	private Integer quizMark;
	
	@JsonProperty(value = "incorrect_questions")
	private List<QC> qcs;
	
	private String result;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getPassMark() {
		return passMark;
	}

	public void setPassMark(Integer passMark) {
		this.passMark = passMark;
	}

	public Integer getQuizMark() {
		return quizMark;
	}

	public void setQuizMark(Integer quizMark) {
		this.quizMark = quizMark;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<QC> getQcs() {
		return qcs;
	}

	public void setQcs(List<QC> qcs) {
		this.qcs = qcs;
	}

	@Override
	public String toString() {
		return "PostResult [score=" + score + ", passMark=" + passMark + ", quizMark=" + quizMark + ", qcs=" + qcs
				+ ", result=" + result + "]";
	}
	
	
}
