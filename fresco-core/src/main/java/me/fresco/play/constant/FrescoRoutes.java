package me.fresco.play.constant;

public class FrescoRoutes {
	
	public static final String FRESCO_API_V1 = "https://play-api.fresco.me/api/v1";
	
	public static final String FRESCO_CONTENT_API = "https://play.fresco.me/course";
	
	public static final String FRESCO_HOST = "play-api.fresco.me";
	
	public static final String FRESCO_ORIGIN = "https://play.fresco.me";
	
	public static final String FRESCO_PROGRESSES = FRESCO_API_V1+"/progresses.json";
	
	public static final String FRESCO_TASK = FRESCO_API_V1+"/tasks/%s.json";
	
	public static final String FRESCO_NODE = FRESCO_API_V1+"/nodes/%s.json";
	
	public static final String FRESCO_CONTENT = FRESCO_CONTENT_API+"/%s/progress/topic/%s/content/%s";
	
	public static final String FRESCO_LEXICONS_EXPLORE = FRESCO_API_V1+"/lexicons/explore_lexicons.json";
	
	public static final String FRESCO_LEXICONS_NODES = FRESCO_API_V1+"lexicons/%s/list_nodes.json";
	
	public static final String FRESCO_QUIZ_DETAIL = FRESCO_API_V1+"/assessments/get_quiz_details.json?id=%s";
	
	public static final String FRESCO_POST_RESULT = FRESCO_API_V1+"/assessments/post_result.json";
}
