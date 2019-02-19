package me.fresco.play.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class Answers implements Serializable {

	private static final long serialVersionUID = -2569972984949207163L;

	@JsonProperty(value = "id")
	private Integer id;

	@JsonProperty(value = "answer_no")
	private Integer answerNo;

	@JsonProperty(value = "answer")
	private String answer;

	@JsonIgnore
	private String type;

	@JsonIgnore
	private String is_true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnswerNo() {
		return answerNo;
	}

	public void setAnswerNo(Integer answerNo) {
		this.answerNo = answerNo;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIs_true() {
		return is_true;
	}

	public void setIs_true(String is_true) {
		this.is_true = is_true;
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
		Answers other = (Answers) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Answers [id=" + id + ", answerNo=" + answerNo + ", answer=" + answer + ", type=" + type + ", is_true="
				+ is_true + "]";
	}
}
