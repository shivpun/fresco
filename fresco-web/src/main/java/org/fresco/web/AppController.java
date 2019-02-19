package org.fresco.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Async
public class AppController {

	@RequestMapping(value = "/routeFresco", method = { RequestMethod.POST })
	public String routeFresco(@RequestBody String vals, @RequestParam("url") String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> httpEntity = new HttpEntity<String>(vals);
		System.out.println("FRESCO!!");
		// restTemplate.exchange("http://localhost:5050/trigFresco", HttpMethod.GET,
		// httpEntity, String.class);
		restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		return vals;
	}

	@RequestMapping(value = "/routeQuiz", method = { RequestMethod.POST })
	public String routeQuiz(@RequestBody String vals, @RequestParam("url") String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> httpEntity = new HttpEntity<String>(vals);
		System.out.println("routeQuiz!!");
		// restTemplate.exchange("http://localhost:5050/trigQuiz", HttpMethod.GET,
		// httpEntity, String.class);
		restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		return vals;
	}
}
