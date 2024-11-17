package isep.labdsof.backend.config;

import isep.labdsof.backend.domain.dtos.GoogleUserInfoDto;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.MDC;

@RequiredArgsConstructor
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;
    private final UserRepository userRepository;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        final String finalToken = token;
        final GoogleUserInfoDto googleUserInfoDto = userInfoClient.get().uri(uriBuilder -> uriBuilder.path("/oauth2/v3/userinfo").queryParam("access_token", finalToken).build()).retrieve().bodyToMono(GoogleUserInfoDto.class).block();
        Map<String, Object> attributes = new HashMap<>();
        assert googleUserInfoDto != null;
        String name = googleUserInfoDto.getName();
        String email = googleUserInfoDto.getEmail();
        String picture = googleUserInfoDto.getPicture();
        attributes.put("name", name);
        attributes.put("email", email);
        attributes.put("picture", picture);
        final List<Role> roles = new ArrayList<>();
        final Optional<User> user = userRepository.findUserByEmail(email);
        user.ifPresent(u -> {
            roles.addAll(u.getRoles());
            MDC.put("userId", u.getId().toString());
        });
        if (user.isEmpty()) {
            attributes.put("isNewUser", Boolean.TRUE);
        } else {
            attributes.put("isWaitingForApprovalForEventWorker", user.get().isWaitingForApprovalForEventWorker());
            attributes.put("isWaitingForApprovalForEventManager", user.get().isWaitingForApprovalForEventManager());
        }
        attributes.put("roles", roles);

        final List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
        try {
            return new OAuth2IntrospectionAuthenticatedPrincipal(googleUserInfoDto.getName(), new HashMap<>(attributes), authorities);
        } finally {
            // Nullify the sensitive data after use
            if (token != null) {
                Arrays.fill(token.toCharArray(), '\0');
                token = null;
            }
            if (googleUserInfoDto.getEmail() != null) {
                Arrays.fill(googleUserInfoDto.getEmail().toCharArray(), '\0');
                googleUserInfoDto.setEmail(null);
            }
            if (googleUserInfoDto.getName() != null) {
                Arrays.fill(googleUserInfoDto.getName().toCharArray(), '\0');
                googleUserInfoDto.setName(null);
            }
            if (googleUserInfoDto.getPicture() != null) {
                Arrays.fill(googleUserInfoDto.getPicture().toCharArray(), '\0');
                googleUserInfoDto.setPicture(null);
            }
            if (name != null) {
                Arrays.fill(name.toCharArray(), '\0');
                name = null;
            }
            if (email != null) {
                Arrays.fill(email.toCharArray(), '\0');
                email = null;
            }
            if (picture != null) {
                Arrays.fill(picture.toCharArray(), '\0');
                picture = null;
            }
            attributes.clear();
            attributes = null;
        }
    }
}
