package de.iav.backend.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;

    public AppUserService getAppUserService() {
        return appUserService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hier ist eine Nachricht ohne Authentifizierung, u.a. um zu Testen ob der Server läuft.";
    }

    @PostMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return principal.getName();
        }
        return "unbekannter Benutzer";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse register(@RequestBody AppUserRequest appUserRequest) {
        return appUserService.createUser(appUserRequest);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        Map<String, String> body = Map.of("error", e.getMessage(), "timestamp", Instant.now().toString());
        return ResponseEntity.badRequest().body(body);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "Du hast Dich erfolgreich ausgelogt!";
    }
}