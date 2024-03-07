package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Object> Register(@RequestBody @Valid UserModel user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This Email is Already registered!");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), String.valueOf(10)));
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

//    @PostMapping("/login")
//    public ResponseEntity<Object> Login(@RequestBody @Valid UserModel user) {
//        if (userRepository.findByEmail(user.getEmail()).isEmpty() || BCrypt.checkpw(userRepository.findByEmail(user.getEmail()).get().getPassword(), String.valueOf(10))) {
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Email or Password!");
//        }
//    }

    @GetMapping
    public ResponseEntity<Object> List() {
        List<UserModel> users = userRepository.findAll();
        if (!users.isEmpty()) {
            for (UserModel user : users) {
                UUID id = user.getId();
                user.add(linkTo(methodOn(UserController.class).ListById(id)).withSelfRel());
            }
        }
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Users Were Found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> ListById(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        }
        user.get().add(linkTo(methodOn(UserController.class).List()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserModel userModel) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        } else if (userRepository.findByEmail(userModel.getEmail()).isPresent() && !Objects.equals(user.get().getEmail(), userModel.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Email is Already registered!");
        }
        userModel.setPassword(BCrypt.hashpw(userModel.getPassword(), String.valueOf(10)));
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        }
        userRepository.delete(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("User Successfully Deleted!");
    }
}
