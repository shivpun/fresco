package me.fresco.play.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizMessage implements Serializable {
	
	@Override
	public String toString() {
		return "QuizMessage [apiKey=" + apiKey + ", minimum=" + minimum + ", total=" + total + ", score=" + score
				+ ", assestId=" + assestId + ", groupId=" + groupId + ", progressId=" + progressId + ", contentId="
				+ contentId + ", active=" + active + ", last=" + last + ", prevs=" + prevs + ", news=" + news
				+ ", postResults=" + postResults + ", postQA=" + postQA + ", postQAs=" + postQAs + ", questions="
				+ questions + ", frescoMessage=" + frescoMessage + "]";
	}

	private String apiKey;
	
	private Integer minimum;
	
	private Integer total;
	
	private Integer score;
	
	private Integer assestId;
	
	private Integer groupId;
	
	private Integer progressId;
	
	private Integer contentId;
	
	private PostResult active;
	
	private PostResult last;
	
	private QA prevs;
	
	private QA news;
	
	private List<QA> correct = new ArrayList<QA>();
	
	private List<QA> wrong = new ArrayList<QA>();
	
	private List<PostResult> postResults = new ArrayList<PostResult>();
	
	private PostQA postQA;
	
	private List<PostQA> postQAs = new ArrayList<PostQA>();
	
	private List<Question> questions = new ArrayList<Question>();
	
	private FrescoMessage frescoMessage;
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getAssestId() {
		return assestId;
	}

	public void setAssestId(Integer assestId) {
		this.assestId = assestId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

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

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public FrescoMessage getFrescoMessage() {
		return frescoMessage;
	}

	public void setFrescoMessage(FrescoMessage frescoMessage) {
		this.frescoMessage = frescoMessage;
	}

	public PostQA getPostQA() {
		return postQA;
	}

	public void setPostQA(PostQA postQA) {
		this.postQA = postQA;
	}

	public List<PostQA> getPostQAs() {
		return postQAs;
	}

	public void setPostQAs(List<PostQA> postQAs) {
		this.postQAs = postQAs;
	}
	
	public PostResult getActive() {
		return active;
	}

	public void setActive(PostResult active) {
		this.active = active;
	}

	public PostResult getLast() {
		return last;
	}

	public void setLast(PostResult last) {
		this.last = last;
	}

	public List<PostResult> getPostResults() {
		return postResults;
	}

	public void setPostResults(List<PostResult> postResults) {
		this.postResults = postResults;
	}
	
	public void addPostResults(PostResult result) {
		getPostResults().add(result);
	}

	public void addPostQA(PostQA postQA) {
		getPostQAs().add(postQA);
	}

	public QA getPrevs() {
		return prevs;
	}

	public void setPrevs(QA prevs) {
		this.prevs = prevs;
	}

	public QA getNews() {
		return news;
	}

	public void setNews(QA news) {
		this.news = news;
	}

	public List<QA> getCorrect() {
		return correct;
	}

	public void setCorrect(List<QA> correct) {
		this.correct = correct;
	}

	public List<QA> getWrong() {
		return wrong;
	}

	public void setWrong(List<QA> wrong) {
		this.wrong = wrong;
	}
}
