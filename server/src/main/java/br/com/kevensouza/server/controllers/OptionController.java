package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.OptionModel;
import br.com.kevensouza.server.repositories.MenuRepository;
import br.com.kevensouza.server.repositories.OptionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/options")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OptionController {
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;

    @PostMapping("/new/{menuId}")
    public ResponseEntity<Object> Create(@PathVariable(value = "menuId") UUID menuId, @RequestBody @Valid OptionModel body) {
        var menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        body.setMenu(menu);

        if (body.getAvaliability() == null || body.getName().isEmpty() || body.getDescription().isEmpty() || body.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        optionRepository.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body("successfully created!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody @Valid OptionModel body) {
        var option = optionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getAvaliability() != null) {
            option.setAvaliability(body.getAvaliability());
        }

        if (body.getDescription() != null) {
            option.setDescription(body.getDescription());
        }

        if (body.getName() != null) {
            option.setName(body.getName());
        }

        if (body.getPrice() != null) {
            option.setPrice(body.getPrice());
        }

        if (body.getPicture() != null) {
            option.setPicture(body.getPicture());
        }

        optionRepository.save(option);
        return ResponseEntity.status(HttpStatus.OK).body("successfully updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) {
        var option = optionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        optionRepository.delete(option);
        return ResponseEntity.status(HttpStatus.OK).body("sucessfully deleted!");
    }
}
