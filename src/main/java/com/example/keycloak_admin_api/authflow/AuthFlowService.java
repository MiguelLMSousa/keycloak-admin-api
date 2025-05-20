package com.example.keycloak_admin_api.authflow;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.AuthenticationFlowRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthFlowService {

    @Value("${keycloak.realm}")
    private String realm;

    final String COPY_PREFIX = "Copy of";

    private final Keycloak keycloakService;

    public List<AuthenticationFlowRepresentation> listAuthFlows() {
        return keycloakService.realm(realm).flows().getFlows();
    }

    public AuthenticationFlowRepresentation cloneAuthFlow(String id) {
        AuthenticationFlowRepresentation flow = Optional.ofNullable(keycloakService.realm(realm).flows().getFlow(id)).orElseThrow(NotFoundException::new);
        Response response = keycloakService.realm(realm).flows().copy(flow.getAlias(), new HashMap<>());
        response.close();
        return response.getStatus() == HttpStatus.CREATED.value() ? flow: null;
    }

    public List<AuthenticationFlowRepresentation> deleteOldAuthFlows() {
        List<AuthenticationFlowRepresentation> allCopyFlows = keycloakService.realm(realm).flows().getFlows().stream().filter((element) -> {
            return Optional.ofNullable(element.getAlias()).isEmpty();
        }).toList();
        allCopyFlows.forEach((element) -> {
            keycloakService.realm(realm).flows().deleteFlow(element.getId());
        });
        return allCopyFlows;
    }

}
