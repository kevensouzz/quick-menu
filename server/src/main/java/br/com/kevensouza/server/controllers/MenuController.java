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

    @PostMapping("/users/{userId}/menus")
    public ResponseEntity<MenuModel> create(@PathVariable("userId") UUID userId, @RequestBody @Valid MenuModel body) {
        return menuService.create(userId, body);
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuModel>> readAll() {
        return menuService.readAll();
    }

    @GetMapping("/menus/id/{menuId}")
    public ResponseEntity<MenuModel> readById(@PathVariable("menuId") UUID menuId) {
        return menuService.readById(menuId);
    }

    @GetMapping("/menus/code/{code}")
    public ResponseEntity<MenuModel> ReadByCode(@PathVariable("code") String code) {
        return menuService.readByCode(code);
    }

    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<MenuModel> update(@PathVariable("menuId") UUID menuId, @RequestBody @Valid MenuModel body) {
        return menuService.update(menuId, body);
    }

    @DeleteMapping("/users/{userId}/menus/{menuId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId, @PathVariable("menuId") UUID menuId) {
        return menuService.delete(userId, menuId);
    }
}
