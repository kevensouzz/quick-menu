package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.MenuModel;
import br.com.kevensouza.server.models.OptionModel;
import br.com.kevensouza.server.repositories.MenuRepository;
import br.com.kevensouza.server.repositories.OptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class OptionService {
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;

    public OptionService(MenuRepository menuRepository, OptionRepository optionRepository) {
        this.menuRepository = menuRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public ResponseEntity<OptionModel> create(UUID menuId, OptionModel body) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getAvaliability() == null || body.getName().isEmpty() || body.getDescription().isEmpty() || body.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else if (menu.getOptions().stream().anyMatch(option -> option.getName().equalsIgnoreCase(body.getName()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        OptionModel optionSaved = optionRepository.save(body);

        menu.getOptions().add(optionSaved);
        menuRepository.save(menu);

        return ResponseEntity.status(HttpStatus.CREATED).body(optionSaved);
    }

    public ResponseEntity<List<OptionModel>> readAll() {
        return ResponseEntity.ok(optionRepository.findAll());
    }

    public ResponseEntity<OptionModel> readById(UUID optionId) {
        OptionModel option = optionRepository.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(option);
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

        return ResponseEntity.ok(optionRepository.save(option));
    }

    public ResponseEntity<OptionModel> updatePicture(UUID optionId, OptionModel body) {
        OptionModel option = optionRepository.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        option.setPicture(body.getPicture());

        return ResponseEntity.ok(optionRepository.save(option));
    }

    @Transactional
    public ResponseEntity<Void> delete(UUID menuId, UUID optionId) {
        MenuModel menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OptionModel option = optionRepository.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        menu.getOptions().remove(option);
        menuRepository.save(menu);

        optionRepository.delete(option);

        return ResponseEntity.noContent().build();
    }
}
