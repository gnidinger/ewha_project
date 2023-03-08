package com.ewha.back.global.security.oAuth;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

	@GetMapping("/redirect")
	public ResponseEntity redirect(
		@RequestParam("access_token") String accessToken,
		@RequestParam("refresh_token") String refreshToken) throws URISyntaxException {

		// URI redirect = new URI("https://www.getabeer.co.kr");
		URI redirect = new URI("http://localhost:3000");

		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
			.maxAge(7 * 24 * 60 * 60)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(redirect);
		headers.set("Set-Cookie", cookie.toString());
		headers.set("Authorization", accessToken);

		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}
}
