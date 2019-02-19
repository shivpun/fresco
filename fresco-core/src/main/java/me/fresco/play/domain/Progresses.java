package me.fresco.play.domain;

import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Progresses {
	
	@JsonProperty(value = "progresses")
	private List<Progress> progress;

	public List<Progress> getProgress() {
		return progress;
	}

	public void setProgress(List<Progress> progress) {
		if(progress!=null) {
			this.progress = progress;
			this.progress.sort(Comparator.comparing(Progress::getNodeId));
		}
	}

	@Override
	public String toString() {
		return "Progresses [progress=" + progress + "]";
	}
}
