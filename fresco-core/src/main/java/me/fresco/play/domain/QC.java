package me.fresco.play.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QC implements Serializable {
	
	@JsonProperty(value = "question_id")
	private Integer fq;
	
	@JsonProperty(value = "answer_id")
	private Integer fa;

	public Integer getFq() {
		return fq;
	}

	public void setFq(Integer fq) {
		this.fq = fq;
	}

	public Integer getFa() {
		return fa;
	}

	public void setFa(Integer fa) {
		this.fa = fa;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fq == null) ? 0 : fq.hashCode());
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
		QC other = (QC) obj;
		if (fq == null) {
			if (other.fq != null)
				return false;
		} else if (!fq.equals(other.fq))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QC [fq=" + fq + ", fa=" + fa + "]";
	}
}
