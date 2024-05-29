package br.com.kevensouza.server.services;

import br.com.kevensouza.server.models.EateryModel;
import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.repositories.EateryRepository;
import br.com.kevensouza.server.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class EateryService {
    private final EateryRepository eateryRepository;
    private final UserRepository userRepository;

    public EateryService(EateryRepository eateryRepository, UserRepository userRepository) {
        this.eateryRepository = eateryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<EateryModel> create(UUID userId, EateryModel body) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (body.getName().isEmpty() ||
                body.getCnpj().isEmpty() ||
                body.getAddress().isEmpty() ||
                body.getPhone().isEmpty()) {

            return ResponseEntity.badRequest().build();

        } else if (eateryRepository.existsByName(body.getName())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } else if (body.getPhone().length() < 12 ||
                    body.getPhone().length() > 16 ||
                    body.getCnpj().length() != 14) {

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        }

        EateryModel eaterySaved = eateryRepository.save(body);

        user.getEateries().add(eaterySaved);
        userRepository.save(user);

        eaterySaved.getUsers().add(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(eaterySaved);
    }

    @Transactional
    public ResponseEntity<UserModel> addUserToEatery(UUID eateryId, UUID hostUserId, UUID addUserId) {
        EateryModel eatery = eateryRepository.findById(eateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserModel hostUser = userRepository.findById(hostUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserModel addUser = userRepository.findById(addUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!eatery.getUsers().contains(hostUser)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        eatery.getUsers().add(addUser);
        eateryRepository.save(eatery);

        addUser.getEateries().add(eatery);
        userRepository.save(addUser);

        return ResponseEntity.ok(addUser);
    }

    public ResponseEntity<List<EateryModel>> readAll() {
        return ResponseEntity.ok(eateryRepository.findAll());
    }

    public ResponseEntity<EateryModel> readById(UUID eateryId) {
        EateryModel eatery = eateryRepository.findById(eateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(eatery);
    }

    public ResponseEntity<EateryModel> update(UUID EateryId, EateryModel body) {
        EateryModel eatery = eateryRepository.findById(EateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (eateryRepository.existsByName(body.getName())
                || eateryRepository.existsByName(body.getName())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } else if ((body.getPhone() != null && (body.getPhone().length() < 12 || body.getPhone().length() > 16)) ||
                (body.getCnpj() != null && body.getCnpj().length() != 14)) {

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        }

        if (body.getDescription() != null) {
            eatery.setDescription(body.getDescription());
        }

        if (body.getName() != null) {
            eatery.setName(body.getName());
        }

        if (body.getAddress() != null) {
            eatery.setAddress(body.getAddress());
        }

        if (body.getPhone() != null) {
            eatery.setPhone(body.getPhone());
        }

        if (body.getCnpj() != null) {
            eatery.setCnpj(body.getCnpj());
        }

        return ResponseEntity.ok(eateryRepository.save(eatery));
    }

    public ResponseEntity<EateryModel> updatePicture(UUID eateryId, EateryModel body) {
        EateryModel eatery = eateryRepository.findById(eateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        eatery.setPicture(body.getPicture());

        return ResponseEntity.ok(eatery);
    }

    @Transactional
    public ResponseEntity<Void> removeUserFromEatery(UUID eateryId, UUID hostUserId, UUID removeUserId) {
        EateryModel eatery = eateryRepository.findById(eateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserModel hostUser = userRepository.findById(hostUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserModel removeUser = userRepository.findById(removeUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!eatery.getUsers().contains(hostUser)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        eatery.getUsers().remove(removeUser);
        eateryRepository.save(eatery);

        removeUser.getEateries().remove(eatery);
        userRepository.save(removeUser);

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> delete(UUID userId, UUID EateryId) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        EateryModel eatery = eateryRepository.findById(EateryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!eatery.getUsers().contains(user)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        for (UserModel userModel : eatery.getUsers()) {
            userModel.getEateries().remove(eatery);
            userRepository.save(userModel);
        }

        eatery.getUsers().clear();
        eateryRepository.delete(eatery);

        return ResponseEntity.noContent().build();
    }
}