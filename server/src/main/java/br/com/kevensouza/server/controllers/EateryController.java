package br.com.kevensouza.server.controllers;

import br.com.kevensouza.server.models.EateryModel;
import br.com.kevensouza.server.models.UserModel;
import br.com.kevensouza.server.services.EateryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EateryController {
    private final EateryService eateryService;

    @PostMapping("/users/{userId}/eataries")
    public ResponseEntity<EateryModel> create(@PathVariable("userId") UUID userId,
                                              @RequestBody @Valid EateryModel body) {
        return eateryService.create(userId, body);
    }

    @PostMapping("/eateries/{eateryId}/host/{hostUserId}/add/{addUserId}")
    public ResponseEntity<UserModel> addUserToEatery(@PathVariable("eateryId") UUID eateryId,
                                                           @PathVariable("hostUserId") UUID hostUserId,
                                                           @PathVariable("addUserId") UUID addUserId) {
        return eateryService.addUserToEatery(eateryId, hostUserId, addUserId);
    }

    @GetMapping("/eateries")
    public ResponseEntity<List<EateryModel>> readAll() {
        return eateryService.readAll();
    }

    @GetMapping("/eataries/{eateryId}")
    public ResponseEntity<EateryModel> readById(@PathVariable("eateryId") UUID eateryId) {
        return eateryService.readById(eateryId);
    }

    @PatchMapping("/eateries/{eateryId}")
    public ResponseEntity<EateryModel> update(@PathVariable("eateryId") UUID eateryId, @RequestBody @Valid EateryModel body) {
        return eateryService.update(eateryId, body);
    }

    @PatchMapping("/eateries/picture/{eateryId}")
    public ResponseEntity<EateryModel> updatePicture(@PathVariable("eateryId") UUID eateryId, @RequestBody @Valid EateryModel body) {
        return eateryService.updatePicture(eateryId, body);
    }

    @DeleteMapping("/eateries/{eateryId}/host/{hostUserId}/remove/{removeUserId}")
    public ResponseEntity<Void> removeUserFromEatery(@PathVariable("eateryId") UUID eateryId,
                                                                @PathVariable("hostUserId") UUID hostUserId,
                                                                @PathVariable("removeUserId") UUID removeUserId) {
        return eateryService.removeUserFromEatery(eateryId, hostUserId, removeUserId);
    }


    @DeleteMapping("/users/{userId}/eateries/{eateryId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId,
                                       @PathVariable("eateryId") UUID eateryId) {
        return eateryService.delete(userId, eateryId);
    }
}
