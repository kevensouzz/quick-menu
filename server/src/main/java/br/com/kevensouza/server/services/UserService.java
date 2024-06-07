package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.EateryModel;
import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.models.enums.UserRole;
import br.com.kevensouza.server.repositories.EateryRepository;
import br.com.kevensouza.server.repositories.UserRepository;
import br.com.kevensouza.server.security.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Token tokenService;
    private final AuthenticationManager authenticationManager;
    private final EateryRepository eateryRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Token tokenService, AuthenticationManager authenticationManager, EateryRepository eateryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.eateryRepository = eateryRepository;
    }

    public ResponseEntity<Map<String, String>> register(UserModel body) {
        if (body.getEmail().isEmpty() ||
                body.getUsername().isEmpty() ||
                body.getPassword().isEmpty() ||
                body.getCpf().isEmpty()) {

            return ResponseEntity.badRequest().build();

        } else if (userRepository.existsByUsername(body.getUsername()) ||
                userRepository.existsByEmail(body.getEmail()) ||
                userRepository.existsByCpf(body.getCpf())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } else if (body.getUsername().length() > 16 ||
                body.getUsername().length() < 3 ||
                body.getPassword().length() < 8 ||
                body.getCpf().length() < 11 ||
                body.getCpf().length() > 14) {

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        } else if (body.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        body.setRole(UserRole.USER);
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(body);
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((UserModel) auth.getPrincipal());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> login(UserModel body) {
        if (
                !userRepository.existsByUsername(body.getUsername())
                        ||
                        !passwordEncoder.matches(body.getPassword(), userRepository.findByUsername(body.getUsername()).getPassword())
        ) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((UserModel) auth.getPrincipal());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Boolean> checkPassword(UUID userId, UserModel body) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(passwordEncoder.matches(body.getPassword(), user.getPassword()));
    }

    public ResponseEntity<List<UserModel>> readAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<UserModel> readById(UUID userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<UserModel> update(UUID userId, UserModel body) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userRepository.existsByUsername(body.getUsername()) ||
                userRepository.existsByEmail(body.getEmail()) ||
                userRepository.existsByCpf(body.getCpf())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } else if (body.getUsername() != null &&
                (body.getUsername().length() > 16 || body.getUsername().length() < 3) ||
                (body.getCpf() != null && body.getCpf().length() != 11)) {

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        }

        if (body.getUsername() != null) {
            user.setUsername(body.getUsername());
        }

        if (body.getEmail() != null) {
            user.setEmail(body.getEmail());
        }

        if (body.getCpf() != null) {
            user.setCpf(body.getCpf());
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<UserModel> updatePicture(UUID userId, UserModel body) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setPicture(body.getPicture());

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<UserModel> updatePassword(UUID userId, UserModel body) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        user.setPassword(passwordEncoder.encode(body.getPassword()));

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<UserModel> updateRole(UUID userId, UserModel body) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setRole(body.getRole());

        return ResponseEntity.ok(userRepository.save(user));
    }

    @Transactional
    public ResponseEntity<Void> delete(UUID userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        for (EateryModel eatery : user.getEateries()) {
            eatery.getUsers().remove(user);

            if (eatery.getUsers().isEmpty()) {
                eateryRepository.delete(eatery);
            } else {
                eateryRepository.save(eatery);
            }
        }

        user.getEateries().clear();
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}
