package me.fresco.play.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Node implements Serializable {
	
	@Override
	public String toString() {
		return "Node [id=" + id + ", progresses=" + progresses + ", tasks=" + tasks + "]";
	}

	@JsonProperty(value = "id")
	private Integer id;
	
	@JsonProperty(value = "progress")
	private Progress progresses;
	
	@JsonProperty(value = "tasks")
	private List<Task> tasks = new ArrayList<Task>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Progress getProgresses() {
		return progresses;
	}

	public void setProgresses(Progress progresses) {
		this.progresses = progresses;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		if(tasks!=null) {
			this.tasks = tasks;
			this.tasks.sort(Comparator.comparing(Task::getSequence));
		}
	}
	
	public Task nextTask() {
		if(!CollectionUtils.isEmpty(getTasks())) {
			return getTasks().get(0);
		}
		return null;
	}
	
	public void replace(Task task) {
		if(!CollectionUtils.isEmpty(getTasks()) && task!=null && task.getId()!=null) {
			int index = getTasks().indexOf(task);
			if(index>-1) {
				tasks.set(index, task);
			}
		}
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
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
