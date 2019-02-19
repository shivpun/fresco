package me.fresco.play.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Content implements Serializable {
	
	@JsonProperty(value = "id")
	private Integer id;
	
	@JsonProperty(value = "content_type")
	private String contentType;
	
	@JsonProperty(value = "status")
	private String status;
	
	@JsonProperty(value = "content_group_id")
	private Integer groupId;
	
	@JsonProperty(value = "sequence_number")
	private Integer sequence;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "Content [id=" + id + ", contentType=" + contentType + ", status=" + status + ", groupId=" + groupId
				+ ", sequence=" + sequence + "]";
	}
}
