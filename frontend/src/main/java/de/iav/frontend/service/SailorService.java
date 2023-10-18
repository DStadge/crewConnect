package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.iav.frontend.exception.CustomJsonProcessingException;
import de.iav.frontend.exception.CustomStatusCodeException;
import de.iav.frontend.model.Sailor;
import de.iav.frontend.model.SailorWithoutId;
import de.iav.frontend.security.AuthService;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SailorService {
    public static final String COOKIE = "Cookie";
    public static final String JSESSIONID = "JSESSIONID=";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SAILOR_BASE_URL = "https://muc-java-23-1-dirk.capstone-project.de/api/crewconnect";
    //http://localhost:8080/api/crewconnect
    //https://muc-java-23-1-dirk.capstone-project.de/api/crewconnect
    private static final String HEADER_VAR = "application/json";

    private static SailorService instance;

    public SailorService() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static synchronized SailorService getInstance() {
        if (instance == null) {
            instance = new SailorService();
        }
        return instance;
    }

    private Sailor mapToSailor(String json) {
        try {
            return objectMapper.readValue(json, Sailor.class);
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException("Fehler beim oeffnen des Seglers ", e);
        }
    }

    public List<Sailor> getSailorList() {
        HttpRequest request = HttpRequest.newBuilder().header(COOKIE, JSESSIONID + AuthService.getInstance().sessionId())
                .GET()
                .uri(URI.create(SAILOR_BASE_URL + "/sailor"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToSailorList)
                .join();
    }

    private List<Sailor> mapToSailorList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException("Segler konnte nicht geoeffnet werden", e);
        }
    }

    public void addSailor(SailorWithoutId sailorToAdd) {
        try {
            String requestBody = objectMapper.writeValueAsString(sailorToAdd);
            HttpRequest request = HttpRequest.newBuilder().header(COOKIE, JSESSIONID + AuthService.getInstance().sessionId())
                    .uri(URI.create(SAILOR_BASE_URL + "/sailor"))
                    .header("Content-Type", HEADER_VAR)
                    .header("Accept", HEADER_VAR)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToSailor)
                    .join();
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException("Segler konnte nicht hinzugefuegt werden", e);
        }
    }

    public void updateSailorById(String id, Sailor sailorToAdd) {
        try {
            String requestBody = objectMapper.writeValueAsString(sailorToAdd);
            HttpRequest request = HttpRequest.newBuilder().header(COOKIE, JSESSIONID + AuthService.getInstance().sessionId())
                    .uri(URI.create(SAILOR_BASE_URL + "/" + id))
                    .header("Content-Type", HEADER_VAR)
                    .header("Accept", HEADER_VAR)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToSailor)
                    .join();
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException("Segler konnte nicht aktuakisiert werden ", e);
        }
    }

    public void deleteSailorById(String idToDelete, TableView<Sailor> listView) {
        HttpRequest request = HttpRequest.newBuilder().header(COOKIE, JSESSIONID + AuthService.getInstance().sessionId())
                .uri(URI.create(SAILOR_BASE_URL + "/" + idToDelete))
                .DELETE()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        Platform.runLater(() -> {
                            listView.getItems().removeIf(sailor -> sailor.sailorId().equals(idToDelete));
                            listView.refresh();
                        });
                    } else {
                        throw new CustomStatusCodeException("Fehler beim loeschen des Seglers mit der ID: " + idToDelete);
                    }
                })
                .join();
    }
}