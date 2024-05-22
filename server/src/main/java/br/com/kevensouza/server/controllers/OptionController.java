package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.OptionModel;
import br.com.kevensouza.server.services.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("menus/{menuId}/options")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OptionController {
    private final OptionService optionService;

    @PostMapping
    public ResponseEntity<OptionModel> create(@PathVariable("menuId") UUID menuId, @RequestBody @Valid OptionModel option) {
        return optionService.create(menuId, option);
    }

    @PatchMapping("/{optionId}")
    public ResponseEntity<OptionModel> update(@PathVariable("optionId") UUID optionId, @RequestBody @Valid OptionModel body) {
        return optionService.update(optionId, body);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> delete(@PathVariable("menuId") UUID menuId, @PathVariable("optionId") UUID optionId) {
        return optionService.delete(menuId, optionId);
    }
}
