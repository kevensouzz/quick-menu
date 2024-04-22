package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.ConfigModel;
import br.com.kevensouza.server.repositories.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configs")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfigController {
    private final ConfigRepository configRepository;

    @PatchMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody ConfigModel body) {
        var settings = configRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getBackgroundColor() != null) {
            settings.setBackgroundColor(body.getBackgroundColor());
        }

        if (body.getFontColor() != null) {
            settings.setFontColor(body.getFontColor());
        }

        if (body.getFontSize() != 0) {
            settings.setFontSize(body.getFontSize());
        }

        configRepository.save(settings);
        return ResponseEntity.status(HttpStatus.OK).body("successfully updated!");
    }
}
