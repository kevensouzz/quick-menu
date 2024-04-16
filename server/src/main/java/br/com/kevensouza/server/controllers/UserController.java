package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.models.UserRole;
import br.com.kevensouza.server.models.dtos.ResponseTokenDto;
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

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Object> Register(@RequestBody @Valid UserModel user) {
        if (user.getEmail() == null || user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There Are Mandatory Fields Not Filled In!");
        } else if (userRepository.findByUsername(user.getUsername()) != null || userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Some Of This Information Is Already Being Used!");
        } else if (user.getUsername().length() > 16 || user.getUsername().length() < 3 || user.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Username Must Be Between 3 And 16 Characters and Password Must Be Equal OrBigger Then 8 Characters!");
        } else if (user.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTokenDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> Login(@RequestBody @Valid UserModel user) {
        if (
                userRepository.findByUsername(user.getUsername()) == null
                        ||
                        !passwordEncoder.matches(user.getPassword(), userRepository.findByUsername(user.getUsername()).getPassword())
        ) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Username or Password!");
        } else if (user.getRole() != null || user.getEmail() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTokenDto(token));
    }

    @PostMapping("/checkPass/{id}")
    public ResponseEntity<Boolean> CheckPassword(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel userModel) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (userModel.getEmail() != null || userModel.getUsername() != null || userModel.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(passwordEncoder.matches(userModel.getPassword(), user.get().getPassword()));
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
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        user.get().add(linkTo(methodOn(UserController.class).List()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel userModel) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (userRepository.findByUsername(userModel.getUsername()) != null || userRepository.findByEmail(userModel.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Some Of This Information Is Already Being Used!");
        } else if (userModel.getUsername() != null && (userModel.getUsername().length() > 16 || userModel.getUsername().length() < 3)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Username Must Be Between 3 And 16 Characters!");
        } else if (userModel.getRole() != null || userModel.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userModel.getUsername() != null) {
            user.get().setUsername(userModel.getUsername());
        }

        if (userModel.getEmail() != null) {
            user.get().setEmail(userModel.getEmail());
        }

        userRepository.save(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("User Successfully Updated!");
    }

    @PatchMapping("/pass/{id}")
    public ResponseEntity<Object> UpdatePass(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel userModel) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (userModel.getEmail() != null || userModel.getUsername() != null || userModel.getRole() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (userModel.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password Must Be Equal Or Bigger Then 8 Characters!");
        }

        if (!passwordEncoder.matches(userModel.getPassword(), user.get().getPassword())) {
            user.get().setPassword(passwordEncoder.encode(userModel.getPassword()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It's Already The Actual Password!");
        }

        userRepository.save(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("User Password Successfully Updated");
    }

    @PatchMapping("/role/{id}")
    public ResponseEntity<Object> UpdateRole(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel userModel) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (userModel.getEmail() != null || userModel.getUsername() != null || userModel.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (user.get().getRole() != userModel.getRole()) {
            user.get().setRole(userModel.getRole());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It's Already The Actual Role!");
        }

        userRepository.save(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("User Role Successfully Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.delete(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("User Successfully Deleted!");
    }
}
