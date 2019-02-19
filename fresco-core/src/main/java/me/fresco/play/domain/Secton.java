package me.fresco.play.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Secton implements Serializable {
	
	@JsonProperty(value = "section_id")
	private Integer sectionId;
	
	@JsonProperty(value = "questions")
	private List<QA> qas = new ArrayList<QA>();

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public List<QA> getQas() {
		return qas;
	}

	public void setQas(List<QA> qas) {
		this.qas = qas;
	}
	
	public void addQA(QA qa) {
		getQas().add(qa);
	}

	@Override
	public String toString() {
		return "Secton [sectionId=" + sectionId + ", qas=" + qas + "]";
	}
}
