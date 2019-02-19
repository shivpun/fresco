package me.fresco.play.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class FrescoMessage implements Serializable {
	
	private String employeeId;
	
	private String xApiKey;
	
	private List<Progress> progresses;
	
	private Node activeNode;
	
	private Task quizTask;
	
	private Content content;
	
	public FrescoMessage() {
		super();
	}

	public FrescoMessage(String employeeId, String xApiKey) {
		super();
		this.employeeId = employeeId;
		this.xApiKey = xApiKey;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getxApiKey() {
		return xApiKey;
	}

	public void setxApiKey(String xApiKey) {
		this.xApiKey = xApiKey;
	}
	
	public List<Progress> getProgresses() {
		return progresses;
	}

	public void setProgresses(List<Progress> progresses) {
		this.progresses = progresses;
	}

	public Node getActiveNode() {
		return activeNode;
	}

	public void setActiveNode(Node activeNode) {
		this.activeNode = activeNode;
	}

	public Task getQuizTask() {
		return quizTask;
	}

	public void setQuizTask(Task quizTask) {
		this.quizTask = quizTask;
	}
	
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public void createActiveNode(Integer nodeId) {
		this.activeNode = new Node();
		this.activeNode.setId(nodeId);
	}
	
	public Progress nextProgress() {
		if(!CollectionUtils.isEmpty(getProgresses())) {
			return getProgresses().get(0);
		}
		return null;
	}
	
	public FrescoMessage clone() {
		FrescoMessage frescoMessage = new FrescoMessage(this.employeeId, this.xApiKey);
		return frescoMessage;
	}

	@Override
	public String toString() {
		return "FrescoMessage [employeeId=" + employeeId + ", xApiKey=" + xApiKey + ", progresses=" + progresses
				+ ", activeNode=" + activeNode + ", quizTask=" + quizTask + "]";
	}
}
