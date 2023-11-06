package com.putanyname.kcalc.config.keycloak;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakConfigurationProperties {
    @NotBlank
    private String serverUrl;
    @NotBlank
    private String realmName;
    @NotBlank
    private String clientId;
    @NotBlank
    private String redirectUrl;
    @NotBlank
    private String adminUsername;
    @NotBlank
    private String adminPassword;
}
