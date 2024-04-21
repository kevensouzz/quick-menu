package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.SettingsModel;
import br.com.kevensouza.server.repositories.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
@CrossOrigin(origins = "http://localhost:4200")
public class SettingsController {
    private final SettingsRepository settingsRepository;

    @PatchMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") UUID id, @RequestBody SettingsModel body) {
        Optional<SettingsModel> settings = settingsRepository.findById(id);
        if (settings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (body.getBackgroundColor() != null) {
            settings.get().setBackgroundColor(body.getBackgroundColor());
        }

        if (body.getFontColor() != null) {
            settings.get().setFontColor(body.getFontColor());
        }

        if (body.getFontSize() != 0) {
            settings.get().setFontSize(body.getFontSize());
        }

        settingsRepository.save(settings.get());
        return ResponseEntity.status(HttpStatus.OK).body("successfully updated!");
    }
}
