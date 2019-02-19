package me.fresco.play.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Progress implements Serializable {

	@JsonProperty(value = "id")
	private Integer id;

	@JsonProperty(value = "node_id")
	private Integer nodeId;

	@JsonProperty(value = "started_content_group_id")
	private Integer groupId;

	@JsonProperty(value = "started_task_id")
	private Integer taskId;

	@JsonProperty(value = "status")
	private String status;

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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		Progress other = (Progress) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Progress [id=" + id + ", nodeId=" + nodeId + ", groupId=" + groupId + ", taskId=" + taskId + ", status="
				+ status + "]";
	}
}
