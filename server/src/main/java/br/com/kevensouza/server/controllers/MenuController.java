package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.models.ConfigModel;
import br.com.kevensouza.server.repositories.MenuRepository;
import br.com.kevensouza.server.repositories.ConfigRepository;
import br.com.kevensouza.server.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final ConfigRepository configRepository;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Object> Create(@PathVariable(value = "userId") UUID userId, @RequestBody @Valid MenuModel body, ConfigModel config) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        body.setUser(user);

        if (body.getName().isEmpty() || body.getCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else if (!body.getCode().matches("[A-Z0-9]{6}")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("code must have 6 characters (numbers | uppercase letters).");
        } else if (menuRepository.findByCode(body.getCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("this code is already in use.");
        }

        config.setBackgroundColor("white");
        config.setFontColor("black");
        config.setFontSize(12);

        var menu = menuRepository.save(body);

        config.setMenu(menu);
        configRepository.save(config);

        return ResponseEntity.status(HttpStatus.CREATED).body("successfully created!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuModel>> ReadAll() {
        List<MenuModel> menus = menuRepository.findAll();
        if (!menus.isEmpty()) {
            for (MenuModel menu : menus) {
                UUID id = menu.getMenuId();
                menu.add(linkTo(methodOn(MenuController.class).ReadById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MenuModel> ReadById(@PathVariable(value = "id") UUID id) {
        var menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        menu.add(linkTo(methodOn(MenuController.class).ReadAll()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(menu);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<MenuModel> ReadByCode(@PathVariable(value = "code") String code) {
        var menu = menuRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        menu.add(linkTo(methodOn(MenuController.class).ReadAll()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(menu);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody @Valid MenuModel body) {
        var menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (menuRepository.findByCode(body.getCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (body.getName() != null) {
            menu.setName(body.getName());
        }

        if (body.getCode() != null) {
            menu.setCode(body.getCode());
        }

        menuRepository.save(menu);
        return ResponseEntity.status(HttpStatus.OK).body("successfully updated!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) {
        var menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        menuRepository.delete(menu);
        return ResponseEntity.status(HttpStatus.OK).body("successfully deleted!");
    }
}
