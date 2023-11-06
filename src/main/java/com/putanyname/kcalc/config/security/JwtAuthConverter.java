package com.putanyname.kcalc.config.security;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${keycloak.clientId}")
    private String clientId;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var authorities = extractResourceAccessRoles(jwt);
        authorities.addAll(extractScopeAuthorities(jwt));

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private Set<GrantedAuthority> extractResourceAccessRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.<Map<String, Object>>getClaim("resource_access"))
                .map(resource -> ((Map<String, Object>) resource.get(clientId)))
                .map(resourceRoles -> ((Collection<String>) resourceRoles.get("roles")))
                .orElse(Collections.emptySet())
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractScopeAuthorities(Jwt jwt) {
        return jwtGrantedAuthoritiesConverter.convert(jwt);
    }

    private String getPrincipalClaimName(Jwt jwt) {
        return jwt.getClaim(JwtClaimNames.SUB);
    }
}
