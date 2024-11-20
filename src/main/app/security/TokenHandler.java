package main.app.security;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenHandler {
	private String token = null;
	private final Long TOKENDURATIONMINUTES = (long) 1430;//token es valido durante 1440 minutos, checkear antes para tener changui
	private LocalDateTime tokenExpirationTime;
	@Autowired
	private RestTemplate restTemplate;
	
	//@Value("${baseURLAuth}")
	private String baseURLAuth = "http://localhost:8090/auth";
	
	private final String MAILPASSWORDJSON = "{\"email\":\"admin@admin.com\",\"password\":\"admin\"}";
	
	
	
	public TokenHandler(RestTemplate restTemplate) {
		super();

		this.restTemplate = restTemplate;
	}

	public String getToken() {
		if(token == null || tokenExpirado()) {
			
			RequestEntity request = RequestEntity
					.post(URI.create(baseURLAuth + "/login"))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.ALL)
					.body(MAILPASSWORDJSON);
			/*
			ParameterizedTypeReference<HashMap<String,String>> myBean =
			 new ParameterizedTypeReference<HashMap<String,String>>() {};
			ResponseEntity<HashMap<String,String>> response = restTemplate.exchange(request, myBean);
			*/
			ResponseEntity<String> response = restTemplate.exchange(request, String.class);
			token = response.getBody();
			tokenExpirationTime = LocalDateTime.now().plusMinutes(TOKENDURATIONMINUTES);
		}
		return token;
	}
	
	private boolean tokenExpirado() {
		return LocalDateTime.now().isAfter(tokenExpirationTime);
	}
	
	public HttpHeaders createMicroserviceHeader() {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());
	        return headers;
	}
}
