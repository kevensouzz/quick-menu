package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid UserModel body) {
        return userService.register(body);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserModel body) {
        return userService.login(body);
    }

    @PostMapping("/check-password/{userId}")
    public ResponseEntity<Boolean> checkPassword(@PathVariable("userId") UUID userId, @RequestBody @Valid UserModel body) {
        return userService.checkPassword(userId, body);
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> readAll() {
        return userService.readAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> readById(@PathVariable("userId") UUID userId) {
        return userService.readById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserModel> update(@PathVariable("userId") UUID userId, @RequestBody @Valid UserModel body) {
        return userService.update(userId, body);
    }

    @PatchMapping("/password/{userId}")
    public ResponseEntity<UserModel> updatePassword(@PathVariable("userId") UUID userId, @RequestBody @Valid UserModel body) {
        return userService.updatePassword(userId, body);
    }

    @PatchMapping("/role/{userId}")
    public ResponseEntity<UserModel> updateRole(@PathVariable("userId") UUID userId, @RequestBody @Valid UserModel body) {
        return userService.updateRole(userId, body);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
        return userService.delete(userId);
    }
}
