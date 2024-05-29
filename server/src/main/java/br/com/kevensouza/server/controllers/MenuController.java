package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.services.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/eateries/{eateryId}/menus")
    public ResponseEntity<MenuModel> create(@PathVariable("eateryId") UUID eateryId, @RequestBody @Valid MenuModel body) {
        return menuService.create(eateryId, body);
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuModel>> readAll() {
        return menuService.readAll();
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuModel> readById(@PathVariable("menuId") UUID menuId) {
        return menuService.readById(menuId);
    }

    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<MenuModel> update(@PathVariable("menuId") UUID menuId, @RequestBody @Valid MenuModel body) {
        return menuService.update(menuId, body);
    }

    @PatchMapping("/menus/picture/{menuId}")
    public ResponseEntity<MenuModel> updatePicture(@PathVariable("menuId") UUID menuId, @RequestBody @Valid MenuModel body) {
        return menuService.updatePicture(menuId, body);
    }

    @DeleteMapping("/eateries/{eateryId}/menus/{menuId}")
    public ResponseEntity<Void> delete(@PathVariable("eateryId") UUID eateryId, @PathVariable("menuId") UUID menuId) {
        return menuService.delete(eateryId, menuId);
    }
}
