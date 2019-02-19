package me.fresco.play.constant;

public class FrescoQuery {
	
	public static final String SELECT_COUNT_Q = "SELECT count(1) from Q WHERE f_id=?";
	
	public static final String SELECT_COUNT_A = "SELECT count(1) from A WHERE f_id=?";
	
	public static final String SELECT_COUNT_QA = "SELECT count(1) from QA where fq_id=? and fa_id=?";
	
	public static final String SELECT_COUNT_QC = "SELECT count(1) from QC WHERE fq_id=?";
	
	public static final String SELECT_COUNT_QWW = "SELECT count(1) from QW WHERE fq_id=?";
	
	public static final String SELECT_COUNT_QW = "SELECT count(1) from QW WHERE fq_id=? and fa_id=?";
	
	public static final String SELECT_COUNT_AT = "SELECT count(1) from AT WHERE seq=?";
	
	public static final String SELECT_ANS_QC = "SELECT distinct fa_id from QC where fq_id=?";
	
	public static final String SELECT_ANS_QW = "SELECT distinct fa_id from QW where fq_id=?";
	
	public static final String SELECT_FINAL_ANS_QC = "select fq_id,fa_id from qc where fq_id in (select fq_id from qa where s_id=?)";
	
	public static final String SELECT_FINAL_ANS_QW = "select fq_id,fa_id from qw where fq_id in (select fq_id from qa where s_id=?)";
	
	public static final String INSERT_Q = "INSERT INTO Q (f_id, Q, create_date) VALUES (?,?, CURRENT_TIMESTAMP)";
	
	public static final String INSERT_A = "INSERT INTO A (f_id, A, create_date) VALUES (?,?, CURRENT_TIMESTAMP)";
	
	public static final String INSERT_QA = "INSERT INTO QA (fq_id, fa_id, n_id, s_id, g_id, c_id, score, total, minq, create_date) VALUES (?,?, ?,?, ?,?,?,?,?, CURRENT_TIMESTAMP)";
	
	public static final String INSERT_QC = "INSERT INTO QC (fq_id, fa_id, create_date) VALUES (?,?, CURRENT_TIMESTAMP)";
	
	public static final String INSERT_QW = "INSERT INTO QW (fq_id, fa_id, create_date) VALUES (?,?, CURRENT_TIMESTAMP)";
	
	public static final String DELETE_QW = "DELETE from QW WHERE fq_id=?";
	
	public static final String INSERT_AT = "INSERT INTO AT (fq_id, fa_id, nos_qa, score, total, minq, n_id, s_id, g_id, c_id, create_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
	
	public static final String SEQ_AT = "SELECT nextval('FRESCO_AT_SEQ')";
}
