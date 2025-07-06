//KeycloakUtil: Handles interaction with Keycloak for user registration and token issuance

package com.foodcart.authservice.util;

import java.util.List;
import java.util.Optional;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcart.authservice.dto.AuthResDto;
import com.foodcart.authservice.dto.LoginReqDto;
import com.foodcart.authservice.dto.RegisterReqDto;
import com.foodcart.authservice.exception.UserAlreadyExistsException;
import com.foodcart.authservice.exception.UserCreationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to interact with Keycloak: - Create new users using Admin API -
 * Obtain access/refresh tokens using Password Grant
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakUtil {

	// Configuration properties from application.properties
	@Value("${keycloak.server-url}")
	private String serverUrl;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.client-id}")
	private String clientId;

	@Value("${keycloak.client-secret}")
	private String clientSecret;

	@Value("${keycloak.admin-username}")
	private String adminUsername;

	@Value("${keycloak.admin-password}")
	private String adminPassword;

	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Registers a new user in Keycloak using admin credentials.
	 * 
	 * @param req RegisterReqDto containing username/email/password
	 * @return true if user created successfully
	 */
	public boolean createUser(RegisterReqDto req) {

		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(serverUrl).realm("master") // Admin realm
				.grantType(OAuth2Constants.PASSWORD).clientId("admin-cli").username(adminUsername)
				.password(adminPassword).build();

		// Create user object
		UserRepresentation user = new UserRepresentation();
		user.setUsername(req.getUsername());
		user.setEmail(req.getEmail());
		user.setEnabled(true);

		// Set credentials
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(req.getPassword());
		credential.setTemporary(false);
		
		user.setCredentials(List.of(credential));

		// Make API call to create the user
		var response = keycloak.realm(realm).users().create(user);
		int status = 201;

		if (status == 201) {
			return true;
		} else if (status == 409) {
			throw new UserAlreadyExistsException(); // your custom exception
		} else {
			throw new UserCreationException("Unexpected error during user creation: " + status);
		}

	}

	/**
	 * Exchanges user credentials for access and refresh tokens.
	 * 
	 * @param req login request with username/password
	 * @return Optional of AuthResDto with token info
	 */
	public Optional<AuthResDto> getToken(LoginReqDto req) {
		try {
			String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", serverUrl, realm);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			// Compose form URL encoded body
			String body = 
					"grant_type=password" + 
					"&client_id=" + clientId + 
					"&client_secret=" + clientSecret + 
					"&username=" + req.getUsername() + 
					"&password=" + req.getPassword();

			HttpEntity<String> entity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();

			// Make POST request to token endpoint
			ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);

			log.info("tokenUrl : " + tokenUrl);
			log.info("body : " + body);

			if (response.getStatusCode().is2xxSuccessful()) {
				// Parse token response
				TokenResponse token = mapper.readValue(response.getBody(), TokenResponse.class);

				AuthResDto authResDto = AuthResDto.builder()
						.id("N/A") // You can replace this with actual user ID via key-cloak lookup if needed
						.username(req.getUsername())
						.accessToken(token.accessToken)
						.refreshToken(token.refreshToken)
						.roles(List.of()) // Token roles can be extracted later
						.message("Login successful")
						.build(); 

				return Optional.of(authResDto);
				
			}
		} catch (Exception e) {
			log.error("Failed to fetch token from Keycloak", e);
		}
		
		return Optional.empty();
	}

	/**
	 * Helper class for parsing token JSON
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class TokenResponse {
		@JsonAlias("access_token")
		public String accessToken;

		@JsonAlias("refresh_token")
		public String refreshToken;
	}
}
