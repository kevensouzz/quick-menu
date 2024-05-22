package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.models.enums.UserRole;
import br.com.kevensouza.server.repositories.UserRepository;
import br.com.kevensouza.server.security.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Token tokenService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Token tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<String> register(UserModel body) {
        if (body.getEmail() == null || body.getUsername() == null || body.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (userRepository.findByUsername(body.getUsername()) != null || userRepository.findByEmail(body.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else if (body.getUsername().length() > 16 || body.getUsername().length() < 3 || body.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        } else if (body.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        body.setRole(UserRole.USER);
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(body);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<String> login(UserModel body) {
        if (
                userRepository.findByUsername(body.getUsername()) == null
                        ||
                        !passwordEncoder.matches(body.getPassword(), userRepository.findByUsername(body.getUsername()).getPassword())
        ) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<Boolean> checkPassword(UUID userId, UserModel body) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(passwordEncoder.matches(body.getPassword(), user.getPassword()));
    }

    public ResponseEntity<List<UserModel>> readAll() {
        List<UserModel> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<UserModel> readById(UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<UserModel> update(UUID userId, UserModel body) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userRepository.findByUsername(body.getUsername()) != null || userRepository.findByEmail(body.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else if (body.getUsername() != null && (body.getUsername().length() > 16 || body.getUsername().length() < 3)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        if (body.getUsername() != null) {
            user.setUsername(body.getUsername());
        }

        if (body.getEmail() != null) {
            user.setEmail(body.getEmail());
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<UserModel> updatePassword(UUID userId, UserModel body) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(body.getPassword()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<UserModel> updateRole(UUID userId, UserModel body) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getRole() != body.getRole()) {
            user.setRole(body.getRole());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Void> delete(UUID userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
