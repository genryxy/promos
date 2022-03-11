package com.example.promotions.controller;

import com.example.promotions.model.Participant;
import com.example.promotions.model.Prize;
import com.example.promotions.model.Promoaction;
import com.example.promotions.repo.ParticipantRepo;
import com.example.promotions.repo.PrizeRepo;
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
public class PromoPrizeController {
    private PromoactionRepo promoRepo;

    private PrizeRepo prizeRepo;

    @Autowired
    public PromoPrizeController(final PromoactionRepo promoRepo, final PrizeRepo prizeRepo) {
        this.promoRepo = promoRepo;
        this.prizeRepo = prizeRepo;
    }

    @PostMapping(path = "promo/{id}/prize")
    public ResponseEntity<Long> addPrize(@PathVariable(name = "id") Long id,
        @RequestBody String body) {
        Optional<Promoaction> promo = promoRepo.findById(id);
        if (promo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Prize prize = new Gson().fromJson(body, Prize.class);
        prize = prizeRepo.save(prize);
        List<Prize> upd = promo.get().getPrizes();
        upd.add(prize);
        promo.get().setPrizes(upd);
        promoRepo.save(promo.get());
        return ResponseEntity.ok(prize.getId());
    }

    @DeleteMapping("promo/{promoId}/prize/{prizeId}")
    public ResponseEntity<HttpStatus> deletePrizeByIdFromPromo(
        @PathVariable(name = "promoId") Long promoId,
        @PathVariable(name = "prizeId") Prize prize
    ) {
        Optional<Promoaction> promo = promoRepo.findById(promoId);
        if (promo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Prize> upd = promo.get().getPrizes();
        if (!upd.remove(prize)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        promo.get().setPrizes(upd);
        promoRepo.save(promo.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
