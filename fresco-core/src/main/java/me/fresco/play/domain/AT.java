package me.fresco.play.domain;

public class AT {
	
	private Integer fq;
	
	private Integer fa;
	
	private Integer score;
	
	private Integer total;
	
	private Integer minq;
	
	private Integer seq;

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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getMinq() {
		return minq;
	}

	public void setMinq(Integer minq) {
		this.minq = minq;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "AT [fq=" + fq + ", fa=" + fa + ", score=" + score + ", total=" + total + ", minq=" + minq + ", seq="
				+ seq + "]";
	}
}
