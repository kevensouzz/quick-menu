package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.repositories.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public ResponseEntity<MenuModel> create(MenuModel body) {
        if (body.getName().isEmpty() || body.getCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else if (!body.getCode().matches("[A-Z0-9]{6}")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else if (menuRepository.findByCode(body.getCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        menuRepository.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public ResponseEntity<List<MenuModel>> readAll() {
        List<MenuModel> menus = menuRepository.findAll();
        return ResponseEntity.ok(menus);
    }

    public ResponseEntity<MenuModel> readById(UUID menuId) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(menu);
    }

    public ResponseEntity<MenuModel> readByCode(String code) {
        MenuModel menu = menuRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(menu);
    }

    public ResponseEntity<MenuModel> update(UUID menuId, MenuModel body) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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
        return ResponseEntity.ok(menu);
    }

    public ResponseEntity<Void> delete(UUID menuId) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        menuRepository.delete(menu);
        return ResponseEntity.noContent().build();
    }
}
