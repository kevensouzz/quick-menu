package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.repositories.MenuRepository;
import br.com.kevensouza.server.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @PostMapping("/new/{userId}")
    public ResponseEntity<Object> Create(@PathVariable(value = "userId") UUID userId, @RequestBody @Valid MenuModel body) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        body.setUser(user);

        if (body.getName().isEmpty() || body.getCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else if (!body.getCode().matches("[A-Z0-9]{6}")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("code must have 6 characters including numbers and letters.");
        } else if (menuRepository.findByCode(body.getCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("this code is already in use.");
        }

        menuRepository.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body("successfully created!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuModel>> List() {
        List<MenuModel> menus = menuRepository.findAll();
        if (!menus.isEmpty()) {
            for (MenuModel menu : menus) {
                UUID id = menu.getMenuId();
                menu.add(linkTo(methodOn(MenuController.class).ListById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuModel> ListById(@PathVariable(value = "id") UUID id) {
        Optional<MenuModel> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        menu.get().add(linkTo(methodOn(MenuController.class).List()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(menu.get());
    }
}
