package de.iav.backend;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.security.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private final AuthController authController;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public AuthControllerTest() {
        AppUserService appUserService = Mockito.mock(AppUserService.class);
        this.authController = new AuthController(appUserService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testHello() throws Exception {
        mockMvc.perform(get("/api/auth/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hier ist eine Nachricht ohne Authentifizierung, u.a. um zu Testen ob der Server läuft."));
    }

    @Test
    void testLoginAuthenticated() throws Exception {
        String username = "testuser";
        mockMvc.perform(post("/api/auth/login")
                        .principal(() -> username))
                .andExpect(status().isOk())
                .andExpect(content().string(username));
    }

    @Test
    void testLoginUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isOk())
                .andExpect(content().string("unbekannter Benutzer"));
    }

    @Test
    void testRegister() throws Exception {
        AppUserRequest appUserRequest = new AppUserRequest("newuser", "newuser@iav.com", "password");
        AppUserResponse appUserResponse = new AppUserResponse("newuser", "newuser", "newuser@iav.com", AppUserRole.USER);

        Mockito.when(authController.getAppUserService().createUser(appUserRequest)).thenReturn(appUserResponse);

        mockMvc.perform(post("/api/auth/register")
                        .content(objectMapper.writeValueAsString(appUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(appUserResponse.username()));
    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        AppUserRequest appUserRequest = new AppUserRequest("existinguser", "test@iav.de","1234");
        UserAlreadyExistException exception = new UserAlreadyExistException("Benutzer existiert bereits");

        Mockito.when(authController.getAppUserService().createUser(appUserRequest)).thenThrow(exception);

        mockMvc.perform(post("/api/auth/register")
                        .content(objectMapper.writeValueAsString(appUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(exception.getMessage()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        ResultActions resultActions = mockMvc.perform(post("/api/auth/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Du hast Dich erfolgreich ausgelogt!"));

        assert session.isInvalid();

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}