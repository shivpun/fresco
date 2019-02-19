package me.fresco.play.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task implements Serializable {

	@JsonProperty(value = "id")
	private Integer id;

	@JsonProperty(value = "node_id")
	private Integer nodeId;

	@JsonProperty(value = "sequence_number")
	private Integer sequence;

	@JsonProperty(value = "status")
	private String status;

	@JsonProperty(value = "task_type")
	private String taskType;

	@JsonProperty(value = "contents")
	private List<Content> contents;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		if (contents != null) {
			this.contents = contents;
			this.contents.sort(Comparator.comparing(Content::getSequence));
		}
	}
	
	public Content nextContent() {
		if(!CollectionUtils.isEmpty(getContents())) {
			return getContents().get(0);
		}
		return null;
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
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", nodeId=" + nodeId + ", sequence=" + sequence + ", status=" + status + ", taskType="
				+ taskType + ", contents=" + contents + "]";
	}
}
