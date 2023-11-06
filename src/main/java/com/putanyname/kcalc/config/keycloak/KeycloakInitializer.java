package com.putanyname.kcalc.config.keycloak;


import com.putanyname.kcalc.config.security.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakInitializer implements CommandLineRunner {

    private final Keycloak keycloakAdmin;
    private final KeycloakConfigurationProperties keycloakProperties;

    @Override
    public void run(String... args) {
        log.info("Initializing '{}' realm in Keycloak ...", keycloakProperties.getRealmName());

        var isRealmAlreadyExist = keycloakAdmin.realms()
                .findAll()
                .stream()
                .anyMatch(r -> r.getRealm().equals(keycloakProperties.getRealmName()));

        if (!isRealmAlreadyExist) {
            createRealm();
        }
    }

    private void createRealm() {
        // Client
        var clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(keycloakProperties.getClientId());
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setRedirectUris(List.of(keycloakProperties.getRedirectUrl()));
        clientRepresentation.setDefaultRoles(new String[]{Roles.USER.toString()});

        // Realm
        var realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(keycloakProperties.getRealmName());
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);
        realmRepresentation.setClients(List.of(clientRepresentation));


        realmRepresentation.setUsers(List.of(createAdmin()));

        // Create Realm
        keycloakAdmin.realms().create(realmRepresentation);
    }

    private UserRepresentation createAdmin() {
        var credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(keycloakProperties.getAdminPassword());

        var adminRoles = Arrays.stream(Roles.values())
                .map(Object::toString)
                .toList();
        // User
        var userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(keycloakProperties.getAdminUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        userRepresentation.setClientRoles(Map.of(keycloakProperties.getClientId(), adminRoles));

        return userRepresentation;
    }


}