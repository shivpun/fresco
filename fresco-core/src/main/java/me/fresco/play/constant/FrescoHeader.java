package me.fresco.play.constant;

import static me.fresco.play.constant.FrescoRoutes.FRESCO_HOST;
import static me.fresco.play.constant.FrescoRoutes.FRESCO_ORIGIN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class FrescoHeader {
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";

	public static final String GZIP_ACCEPT_ENCODING = "gzip, deflate, br";

	public static final String NO_CACHE = "no-cache";
	
	public static final String ACCESS_CONTROL_REQUEST_HEADER = "content-type,x-api-key";
	
	public static final String CONNECTION = "keep-alive";

	public static final String ACCEPT = "*/*";
	
	public static HttpEntity<String> headers(String apiKey) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("User-Agent", USER_AGENT);
		headers.add("Host", FRESCO_HOST);
		headers.add("Origin", FRESCO_ORIGIN);
		headers.add("Cache-Control", NO_CACHE);
		headers.add("Pragma", NO_CACHE);
		headers.add("Content-Type", APPLICATION_JSON_VALUE);
		headers.add("x-api-key", apiKey);
		headers.add("Connection", CONNECTION);
		headers.add("Accept", ACCEPT);
		return new HttpEntity<String>(headers);
	}
	
	public static HttpEntity<Object> headers(Object body, String apiKey) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("User-Agent", USER_AGENT);
		headers.add("Host", FRESCO_HOST);
		headers.add("Origin", FRESCO_ORIGIN);
		headers.add("Accept-Language", "en-US,en;q=0.9");
		headers.add("Cache-Control", NO_CACHE);
		headers.add("Pragma", NO_CACHE);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("x-api-key", apiKey);
		headers.add("Connection", CONNECTION);
		headers.add("Accept", ACCEPT);
		return new HttpEntity<Object>(body, headers);
	}
}
