package de.iav.frontend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.exception.CustomJsonProcessingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class AuthService {

    private String sessionId;
    private String errorMessage;
    private static AuthService instance;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String BACKEND_AUTH_URL="https://muc-java-23-1-dirk.capstone-project.de/api/auth";
    //https://muc-java-23-1-dirk.capstone-project.de/api/auth
    //http://localhost:8080/api/auth

    private AuthService() {
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String sessionId() {
        return sessionId;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public boolean registerAppUser(AppUserRequest appUserRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(appUserRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.join().statusCode();

            if (statusCode == 201) {
                return true;
            } else {
                setErrorMessage("Registrierung fehlgeschlagen. / Email und/oder Benutzername schon eingetragen!");
                return false;
            }
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException("Benutzer kann nicht registriert werden!", e);

        }
    }

    public boolean login(String username, String password) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_AUTH_URL + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()))
                .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.join().statusCode();

        if (statusCode == 200) {
            String responseCookie = response.join().headers().firstValue("Set-Cookie").orElseThrow();
            String responseSessionId = responseCookie.substring(11, responseCookie.indexOf(";"));

            setSessionId(responseSessionId);

            return true;
        } else {
            setErrorMessage("Login fehlgeschlagen. Falscher Benutzername und / oder Passwort?");
            return false;
        }
    }

    public boolean logout() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_AUTH_URL + "/logout"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Cookie", "JSESSIONID=" + sessionId)
                .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.join().statusCode();

        if (statusCode == 200) {
            return true;
        } else {
            setErrorMessage("Logout fehlgeschlagen.");
            return false;
        }
    }
}