package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.iav.frontend.exception.CustomJsonProcessingException;
import de.iav.frontend.exception.CustomStatusCodeException;
import de.iav.frontend.model.Boat;
import de.iav.frontend.security.AuthService;
import javafx.application.Platform;
import javafx.scene.control.TableView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BoatService {
    private static final String COOKIE = "Cookie";
    private static final String JSESSIONID = "JSESSIONID=";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BOAT_BASE_URL = "https://muc-java-23-1-dirk.capstone-project.de/api/crewconnect";
    private static final String HEADER_VAR = "application/json";

    private static BoatService instance;

    private List<Boat> boatList = new ArrayList<>();

    public BoatService() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static synchronized BoatService getInstance() {
        if (instance == null) {
            instance = new BoatService();
        }
        return instance;
    }

    public List<Boat> getBoatList() {
        HttpRequest request = HttpRequest.newBuilder().header(COOKIE, JSESSIONID + AuthService.getInstance().sessionId())
                .GET()
                .uri(URI.create(BOAT_BASE_URL + "/boat"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToBoatList)
                .join();
    }

    private List<Boat> mapToBoatList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException("Fehler beim Anzeigen der Bootsliste", e);
        }
    }

    public void saveBoat(Boat newBoat) {
        List<Boat> currentBoatList = getBoatList();
        if (currentBoatList == null) {
            currentBoatList = new ArrayList<>(); // Erstelle eine neue Liste, wenn die Rückgabe null ist
        }
        currentBoatList.add(newBoat);
        this.boatList = currentBoatList;
    }
}