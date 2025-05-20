package com.example.keycloak_admin_api;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KeycloakAdminApiApplication {

	@Value("${keycloak.url}")
	private String url;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.username}")
	private String username;
	@Value("${keycloak.password}")
	private String password;
	@Value("${keycloak.clientId}")
	private String clientId;

	@Bean
	public Keycloak keycloak() {
		return KeycloakBuilder.builder().serverUrl(url).realm(realm).username(username).password(password).clientId(clientId).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(KeycloakAdminApiApplication.class, args);
	}

}
