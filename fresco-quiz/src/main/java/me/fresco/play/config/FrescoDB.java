package me.fresco.play.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.CollectionUtils;

import me.fresco.play.constant.FrescoConstant;
import me.fresco.play.constant.FrescoQuery;
import me.fresco.play.domain.Answers;
import me.fresco.play.domain.PostResult;
import me.fresco.play.domain.QA;
import me.fresco.play.domain.QC;
import me.fresco.play.domain.Question;

@Configuration
public class FrescoDB {
	
	@Value(value = "${spring.datasource.url}")
	private String url;
	
	@Value(value = "${spring.datasource.username}") 
	private String username;
	@Value(value = "${spring.datasource.password}") String password;
	@Value(value = "${spring.datasource.driver-class-name}") String driverClassName;
	
	public void question(Question question) {
		Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_Q, new Object[] {question.getQuestionNo()}, Integer.class);
		if(count<1) {
			jdbcTemplate().update(FrescoQuery.INSERT_Q, new Object[] {question.getQuestionNo(), question.getQuestion()});
		}
	}
	
	public void answer(List<Answers> answers) {
		if(!CollectionUtils.isEmpty(answers)) {
			for(Answers answer:answers) {
				Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_A, new Object[] {answer.getId()}, Integer.class);
				if(count<1) {
					jdbcTemplate().update(FrescoQuery.INSERT_A, new Object[] {answer.getId(), answer.getAnswer()});
				}
			}
		}
	}
	
	public void QA(Question question, Integer sectionId, Integer contentId, Integer score, Integer total, Integer minq) {
		List<Answers> answers = question.getAnswers();
		for(Answers answer:answers) {
			Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_QA, new Object[] {question.getId(), answer.getId()}, Integer.class);
			if(count<1) {
				jdbcTemplate().update(FrescoQuery.INSERT_QA, new Object[] {question.getId(), answer.getId(), sectionId, contentId, score, total, minq});
			}
		}
	}
	
	public void insertQW(List<QA> qas) {
		for(QA qa:qas) {
			Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_QW, new Object[] {qa.getQuestions(), qa.nextAnswer()}, Integer.class);
			if(count<1) {
				jdbcTemplate().update(FrescoQuery.INSERT_QW, new Object[] {qa.getQuestions(), qa.nextAnswer()});
			}
		}
	}
	
	public List<QA> findByFinalQC(Integer nodeId) {
		 return	jdbcTemplate().query(FrescoQuery.SELECT_FINAL_ANS_QC, new Object[] {nodeId}, new ResultSetExtractor<List<QA>>() {

				@Override
				public List<me.fresco.play.domain.QA> extractData(ResultSet rs)
						throws SQLException, DataAccessException {
					List<QA> qas = new ArrayList<QA>();
					while(rs.next()) {
						QA qa = new QA();
						qa.setQuestions(rs.getInt("fq_id"));
						qa.addAnswer(rs.getInt("fa_id"));
						qas.add(qa);
					}
					return qas;
				}
				
			});
	}
	
	public List<QA> findByFinalQW(Integer nodeId) {
		 return	jdbcTemplate().query(FrescoQuery.SELECT_FINAL_ANS_QW, new Object[] {nodeId}, new ResultSetExtractor<List<QA>>() {

				@Override
				public List<me.fresco.play.domain.QA> extractData(ResultSet rs)
						throws SQLException, DataAccessException {
					List<QA> qas = new ArrayList<QA>();
					while(rs.next()) {
						QA qa = new QA();
						qa.setQuestions(rs.getInt("fq_id"));
						qa.addAnswer(rs.getInt("fa_id"));
						qas.add(qa);
					}
					return qas;
				}
				
			});
	}
	
	public void deleteQW(QA qa) {
		jdbcTemplate().update(FrescoQuery.DELETE_QW, new Object[] {qa.getQuestions()});
	}
	
	public void insertQC(List<QA> qas) {
		for(QA qa:qas) {
			Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_QC, new Object[] {qa.getQuestions()}, Integer.class);
			if(count<1) {
				jdbcTemplate().update(FrescoQuery.INSERT_QC, new Object[] {qa.getQuestions(), qa.nextAnswer()});
			}
		}
	}
	
	public List<Integer> correct(Question question) {
		Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_QC, new Object[] {question.getId()}, Integer.class);
		if(count>0) {
			return jdbcTemplate().queryForList(FrescoQuery.SELECT_ANS_QC, new Object[] {question.getId()}, Integer.class);
		}
		return new ArrayList<Integer>();
	}
	
	public List<Integer> wrong(Question question) {
		Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_QWW, new Object[] {question.getId()}, Integer.class);
		if(count>0) {
			return jdbcTemplate().queryForList(FrescoQuery.SELECT_ANS_QW, new Object[] {question.getId()}, Integer.class);
		}
		return new ArrayList<Integer>();
	}
	
	public void AT(List<QA> qas, PostResult postResult, Integer contentId) {
		Integer count = jdbcTemplate().queryForObject(FrescoQuery.SELECT_COUNT_AT, new Object[] {contentId}, Integer.class);
		if(count<1) {
			for(QA qa:qas) {
				jdbcTemplate().update(FrescoQuery.INSERT_AT, new Object[] {qa.getQuestions(), qa.nextAnswer(), postResult.getScore(), postResult.getQuizMark(), postResult.getPassMark(), contentId});
			}
		}
	}
	
	public void saveMinScore(List<QA> qas, List<QC> iqcs, List<Question> quesList) {
		List<QA> correct = new ArrayList<QA>();
		List<QA> wrong = new ArrayList<QA>();
		for(QA qa:qas) {
			QC qc = new QC();
			qc.setFq(qa.getQuestions());
			Question q = new Question();
			q.setQuestionNo(qa.getQuestions());
			q.setId(qa.getQuestions());
			if(iqcs.contains(qc)) {
				int index = quesList.indexOf(q);
				Question question = quesList.get(index);
				Answers ans = question.fetchAnswers(qa.nextAnswer());
				ans.setType(FrescoConstant.WRONG);
				wrong.add(qa);
			} else {
				int index = quesList.indexOf(q);
				System.out.println("saveMinScore | CORRECT | ELSE:"+index);
				Question question = quesList.get(index);
				Answers ans = question.fetchAnswers(qa.nextAnswer());
				ans.setType(FrescoConstant.CORRECT);
				correct.add(qa);
			}
		}
		insertQW(wrong);
		insertQC(correct);
	}
	
	public DataSource dataSource() {
		/*
		 * DataSource dataSource =
		 * DataSourceBuilder.create().url(url).username(username).password(password)
		 * .driverClassName(driverClassName).build();
		 */
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
		dataSource.setDriverClassName(driverClassName);
		return dataSource;
	}

	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
