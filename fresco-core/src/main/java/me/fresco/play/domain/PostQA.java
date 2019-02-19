package me.fresco.play.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostQA implements Serializable {
	
	@JsonProperty(value = "progress_id")
	private Integer progressId;
	
	@JsonProperty(value = "content_id")
	private Integer contentId;
	
	@JsonProperty(value = "sections")
	private List<Secton> section = new ArrayList<Secton>();

	public Integer getProgressId() {
		return progressId;
	}

	public void setProgressId(Integer progressId) {
		this.progressId = progressId;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public List<Secton> getSection() {
		return section;
	}

	public void setSection(List<Secton> section) {
		this.section = section;
	}
	
	public void addSection(Secton secton) {
		getSection().add(secton);
	}
}
