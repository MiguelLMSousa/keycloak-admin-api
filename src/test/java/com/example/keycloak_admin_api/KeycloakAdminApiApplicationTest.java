package com.example.keycloak_admin_api;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class KeycloakAdminApiApplicationTest {

    @LocalServerPort
    private int port;

    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:26.2")
            .withRealmImportFile("/test-realm.json");

    @BeforeEach
    void setUpRestAssuredClient(){
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @DynamicPropertySource
    static void addCustomPropertiesToTest(DynamicPropertyRegistry registry){
        registry.add("keycloak.url", keycloakContainer::getAuthServerUrl);
    }

    @Test
    void sholdReturnAListOfAuthFlowsAndStatus200(){
        when().get("/authflows").then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void sholdReturnAndStatus201(){
        when().put("/authflows/8a3fa81c-ce3c-44e6-b821-05aa5ae4315f").then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void sholdReturnAndStatus400(){
        when().put("/authflows/notFound").then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void sholdReturnAndStatus204(){
        when().delete("/authflows").then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void sholdReturnAndStatus410(){
        when().delete("/authflows").then().statusCode(HttpStatus.GONE.value());
    }


}