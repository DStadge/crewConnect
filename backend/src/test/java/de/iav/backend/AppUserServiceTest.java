package de.iav.backend;


import de.iav.backend.security.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.catchThrowable;

class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AppUserService appUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appUserService = new AppUserService(appUserRepository, passwordEncoder);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Mocken eines AppUser-Objekts
        AppUser appUser = new AppUser("1", "testuser", "test@iav.com", "password", AppUserRole.USER);

        // Definieren des erwarteten Verhaltens des UserRepository, wenn "test@example.com" übergeben wird
        when(appUserRepository.findByEmail("test@iav.com")).thenReturn(Optional.of(appUser));

        // Testen der loadUserByUsername-Methode
        UserDetails userDetails = appUserService.loadUserByUsername("test@iav.com");

        // Überprüfen, ob die zurückgegebene UserDetails korrekt ist
        assertThat(userDetails.getUsername()).isEqualTo(appUser.username());
        assertThat(userDetails.getPassword()).isEqualTo("password");
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_USER");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Definieren des erwarteten Verhaltens des UserRepository, wenn "test@example.com" übergeben wird
        when(appUserRepository.findByEmail("test@iav.com")).thenReturn(Optional.empty());

        // Testen der loadUserByUsername-Methode und Prüfen des Fehlermeldungs-Texts
        Throwable thrown = catchThrowable(() -> appUserService.loadUserByUsername("test@iav.com"));

        // Überprüfen, ob eine Ausnahme geworfen wurde
        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);

        // Überprüfen, ob die Fehlermeldung den erwarteten Text enthält (nach dem Entfernen von Leerzeichen)
        assertThat(thrown.getMessage().trim()).isEqualTo("Benutzer nicht gefunden!");
    }

/*
    @Test
    void testCreateUser_Success() {
        AppUserRequest appUserRequest = new AppUserRequest("newuser", "newuser@iav.com", "password");
        AppUser appUser = new AppUser("1", "newuser", "newuser@iav.com", "password", AppUserRole.USER);

        // Definieren des erwarteten Verhaltens des UserRepository für findByUsername und findByEmail
        when(appUserRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail("newuser@test.com")).thenReturn(Optional.empty());

        // Definieren des erwarteten Verhaltens des PasswordEncoder
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Definieren des erwarteten Verhaltens des UserRepository für save
        when(appUserRepository.save(Mockito.any(AppUser.class))).thenReturn(appUser);

        // Testen der createUser-Methode
        AppUserResponse appUserResponse = appUserService.createUser(appUserRequest);

        // Überprüfen, ob die Antwort korrekt ist
        assertThat(appUserResponse.id()).isEqualTo("1");
        assertThat(appUserResponse.username()).isEqualTo("newuser");
        assertThat(appUserResponse.email()).isEqualTo("newuser@iav.com");
        assertThat(appUserResponse.role()).isEqualTo(AppUserRole.USER.name());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        AppUserRequest appUserRequest = new AppUserRequest("existinguser", "existinguser@iav.com", "password");

        // Definieren des erwarteten Verhaltens des UserRepository für findByUsername
        when(appUserRepository.findByUsername("existinguser")).thenReturn(Optional.of(new AppUser()));

        // Testen der createUser-Methode mit einem bereits existierenden Benutzer
        assertThatThrownBy(() -> appUserService.createUser(appUserRequest))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessage("Benutzer ist schon angelegt!");

        // Stellen Sie sicher, dass der Benutzer nicht erstellt wurde
        verify(appUserRepository, never()).save(Mockito.any(AppUser.class));
    }*/
}