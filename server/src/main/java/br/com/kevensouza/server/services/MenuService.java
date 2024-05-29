package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.EateryModel;
import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.repositories.EateryRepository;
import br.com.kevensouza.server.repositories.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    private final EateryRepository eateryRepository;
    private final MenuRepository menuRepository;

    public MenuService(EateryRepository eateryRepository, MenuRepository menuRepository) {
        this.eateryRepository = eateryRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public ResponseEntity<MenuModel> create(UUID eateryId, MenuModel body) {
        EateryModel eatery = eateryRepository.findById(eateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        MenuModel menuSaved = menuRepository.save(body);

        eatery.getMenus().add(menuSaved);
        eateryRepository.save(eatery);

        return ResponseEntity.status(HttpStatus.CREATED).body(menuSaved);
    }

    public ResponseEntity<List<MenuModel>> readAll() {
        return ResponseEntity.ok(menuRepository.findAll());
    }

    public ResponseEntity<MenuModel> readById(UUID menuId) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(menu);
    }

    public ResponseEntity<MenuModel> update(UUID menuId, MenuModel body) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getName() != null) {
            menu.setName(body.getName());
        }

        if (body.getDescription() != null) {
            menu.setDescription(body.getDescription());
        }

        return ResponseEntity.ok(menuRepository.save(menu));
    }

    public ResponseEntity<MenuModel> updatePicture(UUID menuId, MenuModel body) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        menu.setPicture(body.getPicture());

        return ResponseEntity.ok(menuRepository.save(menu));
    }

    @Transactional
    public ResponseEntity<Void> delete(UUID eateryId, UUID menuId) {
        EateryModel eatery = eateryRepository.findById(eateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        eatery.getMenus().remove(menu);
        eateryRepository.save(eatery);

        menuRepository.delete(menu);

        return ResponseEntity.noContent().build();
    }
}
