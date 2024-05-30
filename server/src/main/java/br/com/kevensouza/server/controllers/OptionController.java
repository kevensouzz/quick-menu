package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.OptionModel;
import br.com.kevensouza.server.services.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OptionController {
    private final OptionService optionService;

    @PostMapping("/menus/{menuId}/options")
    public ResponseEntity<OptionModel> create(@PathVariable("menuId") UUID menuId, @RequestBody @Valid OptionModel body) {
        return optionService.create(menuId, body);
    }

    @GetMapping("/options")
    public ResponseEntity<List<OptionModel>> readAll() {
        return optionService.readAll();
    }

    @GetMapping("/options/{optionId}")
    public ResponseEntity<OptionModel> readById(@PathVariable UUID optionId) {
        return optionService.readById(optionId);
    }

    @PatchMapping("/options/{optionId}")
    public ResponseEntity<OptionModel> update(@PathVariable("optionId") UUID optionId, @RequestBody @Valid OptionModel body) {
        return optionService.update(optionId, body);
    }

    @PatchMapping("/options/picture/{optionId}")
    public ResponseEntity<OptionModel> updatePicture(@PathVariable("optionId") UUID optionId, @RequestBody @Valid OptionModel body) {
        return optionService.updatePicture(optionId, body);
    }

    @DeleteMapping("/menus/{menuId}/options/{optionId}")
    public ResponseEntity<Void> delete(@PathVariable("menuId") UUID menuId, @PathVariable("optionId") UUID optionId) {
        return optionService.delete(menuId, optionId);
    }
}
