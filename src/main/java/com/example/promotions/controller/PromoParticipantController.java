package com.example.promotions.controller;

import com.example.promotions.model.Participant;
import com.example.promotions.model.Promoaction;
import com.example.promotions.repo.ParticipantRepo;
import com.example.promotions.repo.PromoactionRepo;
import com.google.gson.Gson;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromoParticipantController {
    private PromoactionRepo promoRepo;

    private ParticipantRepo participantRepo;

    @Autowired
    public PromoParticipantController(final PromoactionRepo promoRepo, final ParticipantRepo participantRepo) {
        this.promoRepo = promoRepo;
        this.participantRepo = participantRepo;
    }

    @PostMapping(path = "promo/{id}/participant")
    public ResponseEntity<Long> addParticipant(@PathVariable(name = "id") Long id,
        @RequestBody String body) {
        Optional<Promoaction> promo = promoRepo.findById(id);
        if (promo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Participant participant = new Gson().fromJson(body, Participant.class);
        participant = participantRepo.save(participant);
        List<Participant> upd = promo.get().getParticipants();
        upd.add(participant);
        promo.get().setParticipants(upd);
        promoRepo.save(promo.get());
        return ResponseEntity.ok(participant.getId());
    }

    @DeleteMapping("promo/{promoId}/participant/{participantId}")
    public ResponseEntity<HttpStatus> deleteParticipantByIdFromPromo(
        @PathVariable(name = "promoId") Long promoId,
        @PathVariable(name = "participantId") Participant participant
    ) {
        Optional<Promoaction> promo = promoRepo.findById(promoId);
        if (promo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Participant> upd = promo.get().getParticipants();
        if (!upd.remove(participant)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        promo.get().setParticipants(upd);
        promoRepo.save(promo.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
