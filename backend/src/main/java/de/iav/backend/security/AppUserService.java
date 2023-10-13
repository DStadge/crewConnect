package de.iav.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService{

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden! "));
        return User.builder()
                .username(appUser.username())
                .password(appUser.password())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + appUser.role().name())))
                .build();
    }

    public AppUserResponse createUser(AppUserRequest appUserRequest) {
        if (
                appUserRepository.findByUsername(appUserRequest.username()).isPresent() ||
                        appUserRepository.findByEmail(appUserRequest.email()).isPresent()
        ) {
            throw new UserAlreadyExistException("Benutzer ist schon angelegt!");
        }

        AppUser userToSave = new AppUser(
                null,
                appUserRequest.username(),
                appUserRequest.email(),
                passwordEncoder.encode(appUserRequest.password()),
                AppUserRole.USER
        );
        AppUser savedUser = appUserRepository.save(userToSave);
        return new AppUserResponse(
                savedUser.id(),
                savedUser.username(),
                savedUser.email(),
                savedUser.role()
        );
    }
}