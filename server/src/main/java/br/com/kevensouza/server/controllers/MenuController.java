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
@RequestMapping("/menus")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuModel> create(@RequestBody @Valid MenuModel body) {
        return menuService.create(body);
    }

    @GetMapping
    public ResponseEntity<List<MenuModel>> readAll() {
        return menuService.readAll();
    }

    @GetMapping("/id/{menuId}")
    public ResponseEntity<MenuModel> readById(@PathVariable("menuId") UUID menuId) {
        return menuService.readById(menuId);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<MenuModel> ReadByCode(@PathVariable("code") String code) {
        return menuService.readByCode(code);
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuModel> update(@PathVariable("menuId") UUID menuId, @RequestBody @Valid MenuModel body) {
        return menuService.update(menuId, body);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> delete(@PathVariable("menuId") UUID menuId) {
        return menuService.delete(menuId);
    }
}
