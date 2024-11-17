package isep.labdsof.backend.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import isep.labdsof.backend.domain.dtos.RegisterUserDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.domain.responses.TokenDto;
import isep.labdsof.backend.domain.responses.UrlDto;
import isep.labdsof.backend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Value("${frontend.url.public}")
    private String frontendUrlPublic;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientSecret}")
    private String clientSecret;

    private final OpaqueTokenIntrospector introspector;

    private final WebClient revocationClient;
    private final AuthService authService;

    @GetMapping("/url")
    public ResponseEntity<UrlDto> auth() {
        final String url = new GoogleAuthorizationCodeRequestUrl(clientId, frontendUrlPublic + "/login", Arrays.asList("email", "profile", "openid")).setAccessType("offline").setApprovalPrompt("force").build();
        return ResponseEntity.ok(new UrlDto(url));
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenDto> callback(final @RequestParam("code") String code) {
        final String token;
        final String refreshToken;
        final String name;
        final String email;
        final String picture;
        final boolean isNewUser;
        final boolean isEventWorker;
        final boolean isEventManager;
        final boolean isAdmin;
        final boolean isWaitingForApprovalForEventWorker;
        final boolean isWaitingForApprovalForEventManager;
        final String expireDate;
        try {
            final GoogleTokenResponse googleTokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(), clientId, clientSecret, code, frontendUrlPublic + "/login").execute();
            token = googleTokenResponse.getAccessToken();
            refreshToken = googleTokenResponse.getRefreshToken();
            expireDate = getExpireDate(googleTokenResponse);
            final OAuth2AuthenticatedPrincipal principal = introspector.introspect(token);
            name = principal.getAttribute("name");
            email = principal.getAttribute("email");
            picture = principal.getAttribute("picture");
            isNewUser = Boolean.TRUE.equals(principal.getAttribute("isNewUser"));
            isEventWorker = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EVENT_WORKER"));
            isEventManager = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EVENT_MANAGER"));
            isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
            isWaitingForApprovalForEventWorker = Boolean.TRUE.equals(principal.getAttribute("isWaitingForApprovalForEventWorker"));
            isWaitingForApprovalForEventManager = Boolean.TRUE.equals(principal.getAttribute("isWaitingForApprovalForEventManager"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Expire date: {}", expireDate);
        return ResponseEntity.ok(new TokenDto(token, expireDate, refreshToken, name, email, picture, isNewUser,
                isEventWorker, isEventManager, isAdmin, isWaitingForApprovalForEventWorker,
                isWaitingForApprovalForEventManager));
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        String token = null;
        final String expireDate;
        try {
            final GoogleTokenResponse googleTokenResponse = new GoogleRefreshTokenRequest(new NetHttpTransport(), new GsonFactory(), refreshToken, clientId, clientSecret).execute();
            token = googleTokenResponse.getAccessToken();
            expireDate = getExpireDate(googleTokenResponse);
            return ResponseEntity.ok(new TokenDto(token, expireDate));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } finally {
            // Nullify the sensitive data after use
            Arrays.fill(refreshToken.toCharArray(), '\0');
            refreshToken = null;
            if (token != null) {
                Arrays.fill(token.toCharArray(), '\0');
                token = null;
            }
        }
    }

    private static String getExpireDate(final GoogleTokenResponse googleTokenResponse) {
        final Long expiresInSeconds = googleTokenResponse.getExpiresInSeconds();
        final Calendar expireDate = Calendar.getInstance();
        expireDate.add(Calendar.SECOND, expiresInSeconds.intValue());
        return DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")).format(expireDate.toInstant());
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, @RequestParam("refreshToken") String refreshToken) {
        String accessToken = null;
        String authHeader = request.getHeader("Authorization");
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);  // Remove "Bearer " prefix
            } else {
                return ResponseEntity.badRequest().build();
            }

            revocationClient.post().header("Content-Type", "application/x-www-form-urlencoded").bodyValue("token=" + accessToken + "&token=" + refreshToken).retrieve().toBodilessEntity().block();

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            // Nullify the sensitive data after use
            if (refreshToken != null) {
                Arrays.fill(refreshToken.toCharArray(), '\0');
                refreshToken = null;
            }
            if (accessToken != null) {
                Arrays.fill(accessToken.toCharArray(), '\0');
                accessToken = null;
            }
        }
    }

    @GetMapping("/register")
    public ResponseEntity<StatusResponse> register(@RequestParam @NotNull final String email,
                                                   @RequestParam @NotNull final String name,
                                                   @RequestParam @DefaultValue("false") final boolean isEventWorker,
                                                   @RequestParam final String eventWorkerInfo,
                                                   @RequestParam @DefaultValue("false") final boolean isEventManager,
                                                   @RequestParam final String eventManagerInfo) {
        final RegisterUserDto registerStatus = authService.register(email, name, isEventWorker, eventWorkerInfo, isEventManager, eventManagerInfo);
        if (registerStatus.isFailedToCreateUser()) {
            return ResponseEntity.status(registerStatus.getErrorStatus())
                    .body(StatusResponse.builder()
                            .success(false)
                            .messages(registerStatus.getMessages())
                            .build());
        }
        if (registerStatus.isUserCreated()) {
            return ResponseEntity.status(isEventManager || isEventWorker ? 200 : 201).body(StatusResponse.builder()
                    .success(true)
                    .messages(registerStatus.getMessages())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
