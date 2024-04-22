package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.models.dtos.ResponseTokenDto;
import br.com.kevensouza.server.models.enums.UserRole;
import br.com.kevensouza.server.repositories.UserRepository;
import br.com.kevensouza.server.security.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserRepository userRepository;
    private final Token tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Object> Register(@RequestBody @Valid UserModel body) {
        if (body.getEmail() == null || body.getUsername() == null || body.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There Are Mandatory Fields Not Filled In!");
        } else if (userRepository.findByUsername(body.getUsername()) != null || userRepository.findByEmail(body.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Some Of This Information Is Already Being Used!");
        } else if (body.getUsername().length() > 16 || body.getUsername().length() < 3 || body.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Username Must Be Between 3 And 16 Characters and Password Must Be Equal OrBigger Then 8 Characters!");
        } else if (body.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        body.setRole(UserRole.USER);
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(body);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTokenDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> Login(@RequestBody @Valid UserModel body) {
        if (
                userRepository.findByUsername(body.getUsername()) == null
                        ||
                        !passwordEncoder.matches(body.getPassword(), userRepository.findByUsername(body.getUsername()).getPassword())
        ) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Username or Password!");
        } else if (body.getRole() != null || body.getEmail() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTokenDto(token));
    }

    @PostMapping("/checkPass/{id}")
    public ResponseEntity<Boolean> CheckPassword(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel body) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(passwordEncoder.matches(body.getPassword(), user.getPassword()));
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> List() {
        List<UserModel> users = userRepository.findAll();
        if (!users.isEmpty()) {
            for (UserModel user : users) {
                UUID id = user.getUserId();
                user.add(linkTo(methodOn(UserController.class).ListById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> ListById(@PathVariable(value = "id") UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.add(linkTo(methodOn(UserController.class).List()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel body) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (userRepository.findByUsername(body.getUsername()) != null || userRepository.findByEmail(body.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Some Of This Information Is Already Being Used!");
        } else if (body.getUsername() != null && (body.getUsername().length() > 16 || body.getUsername().length() < 3)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Username Must Be Between 3 And 16 Characters!");
        } else if (body.getRole() != null || body.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (body.getUsername() != null) {
            user.setUsername(body.getUsername());
        }

        if (body.getEmail() != null) {
            user.setEmail(body.getEmail());
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User Successfully Updated!");
    }

    @PatchMapping("/pass/{id}")
    public ResponseEntity<Object> UpdatePass(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel body) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getEmail() != null || body.getUsername() != null || body.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (body.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password Must Be Equal Or Bigger Then 8 Characters!");
        }

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(body.getPassword()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It's Already The Actual Password!");
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User Password Successfully Updated");
    }

    @PatchMapping("/role/{id}")
    public ResponseEntity<Object> UpdateRole(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel body) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getEmail() != null || body.getUsername() != null || body.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (user.getRole() != body.getRole()) {
            user.setRole(body.getRole());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It's Already The Actual Role!");
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User Role Successfully Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body("User Successfully Deleted!");
    }
}
