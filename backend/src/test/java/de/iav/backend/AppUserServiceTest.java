package de.iav.backend;


import de.iav.backend.security.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appUserService = new AppUserService(appUserRepository, passwordEncoder);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        AppUser appUser = new AppUser("1", "Paul", "paulpanzer@iav.com", "1234", AppUserRole.USER);

        when(appUserRepository.findByEmail("paulpanzer@iav.com")).thenReturn(Optional.of(appUser));

        UserDetails userDetails = appUserService.loadUserByUsername("paulpanzer@iav.com");

        assertThat(userDetails.getUsername()).isEqualTo(appUser.username());
        assertThat(userDetails.getPassword()).isEqualTo("1234");
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_USER");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(appUserRepository.findByEmail("paulpanzer@iav.com")).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> appUserService.loadUserByUsername("paulpanzer@iav.com"));

        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);

        assertThat(thrown.getMessage().trim()).isEqualTo("Benutzer nicht gefunden!");
    }


    @Test
    void testCreateUser_Success() {
        AppUserRequest appUserRequest = new AppUserRequest("Paul", "paulpanzer@iav.com", "1234");
        AppUser appUser = new AppUser("1", "Paul", "paulpanzer@iav.com", "1234", AppUserRole.USER);

        when(appUserRepository.findByUsername("Paul")).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail("paulpanzer@test.com")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234")).thenReturn("hashedPassword");

        when(appUserRepository.save(Mockito.any(AppUser.class))).thenReturn(appUser);

        AppUserResponse appUserResponse = appUserService.createUser(appUserRequest);

        assertThat(appUserResponse.id()).isEqualTo("1");
        assertThat(appUserResponse.username()).isEqualTo("Paul");
        assertThat(appUserResponse.email()).isEqualTo("paulpanzer@iav.com");
        assertThat(appUserResponse.role()).hasToString(AppUserRole.USER.name());
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        AppUserRequest appUserRequest = new AppUserRequest("existinguser", "paulpanzer@iav.de", "1234");

        when(appUserRepository.findByUsername("existinguser")).thenReturn(Optional.of(new AppUser("1", "Paul", "paulpanzer@iav.de", "1234", AppUserRole.USER)));

        when(appUserRepository.findByEmail("existinguser@example.com")).thenReturn(Optional.of(new AppUser("1", "Paul", "paulpanzer@iav.de", "1234", AppUserRole.USER)));

        assertThatThrownBy(() -> appUserService.createUser(appUserRequest))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessage("Benutzer ist schon angelegt!");

        verify(appUserRepository, never()).save(any(AppUser.class));
    }
}