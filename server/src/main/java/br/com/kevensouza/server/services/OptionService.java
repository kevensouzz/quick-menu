package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.models.OptionModel;
import br.com.kevensouza.server.repositories.MenuRepository;
import br.com.kevensouza.server.repositories.OptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class OptionService {
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;

    public OptionService(MenuRepository menuRepository, OptionRepository optionRepository) {
        this.menuRepository = menuRepository;
        this.optionRepository = optionRepository;
    }

    public ResponseEntity<OptionModel> create(UUID menuId, OptionModel option) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (option.getAvaliability() == null || option.getName().isEmpty() || option.getDescription().isEmpty() || option.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        var optionSaved = optionRepository.save(option);

        menu.getOptions().add(optionSaved);
        menuRepository.save(menu);

        return ResponseEntity.status(HttpStatus.CREATED).body(optionSaved);
    }

    public ResponseEntity<OptionModel> update(UUID optionId, OptionModel body) {
        OptionModel option = optionRepository.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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
        return ResponseEntity.ok(option);
    }

    public ResponseEntity<Void> delete(UUID menuId, UUID optionId) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OptionModel option = optionRepository.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        menu.getOptions().remove(option);
        menuRepository.save(menu);
        return ResponseEntity.noContent().build();
    }
}
