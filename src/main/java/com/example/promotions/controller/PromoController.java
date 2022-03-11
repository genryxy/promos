package com.example.promotions.controller;

import com.example.promotions.model.Participant;
import com.example.promotions.model.Prize;
import com.example.promotions.model.PromoDao;
import com.example.promotions.model.Promoaction;
import com.example.promotions.model.Result;
import com.example.promotions.repo.PromoactionRepo;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromoController {

    private PromoactionRepo promoRepo;

    @Autowired
    public PromoController(final PromoactionRepo promoRepo) {
        this.promoRepo = promoRepo;
    }

    @GetMapping("promo")
    public ResponseEntity<String> getPromo() {
        List<PromoDao> promos = promoRepo.findAll().stream()
            .map(promo -> new PromoDao(promo.getId(), promo.getName(), promo.getDescription()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(new Gson().toJson(promos));
    }

    @GetMapping("promo/{id}")
    public ResponseEntity<String> getPromoById(@PathVariable(name = "id") Promoaction promo) {
        return ResponseEntity.ok(new Gson().toJson(promo));
    }

    @PutMapping("promo/{id}")
    public ResponseEntity<String> updatePromoById(@PathVariable(name = "id") Long id,
        @RequestBody String body) {
        Promoaction promo = new Gson().fromJson(body, Promoaction.class);
        if (StringUtils.isEmpty(promo.getName())) {
            return ResponseEntity.badRequest().body("Name could not be removed");
        }
        Optional<Promoaction> fromdb = promoRepo.findById(id);
        final Promoaction saved;
        if (fromdb.isPresent()) {
            fromdb.get().setName(promo.getName());
            fromdb.get().setDescription(promo.getDescription());
            saved = promoRepo.save(fromdb.get());
        } else {
            saved = promoRepo.save(promo);
        }
        return ResponseEntity.ok(new Gson().toJson(saved));
    }

    @DeleteMapping("promo/{id}")
    public ResponseEntity<HttpStatus> deletePromoById(@PathVariable(name = "id") Long promo) {
        if (promoRepo.findById(promo).isPresent()) {
            promoRepo.deleteById(promo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("promo")
    public ResponseEntity<Long> uploadPromo(@RequestBody String body) {
        Promoaction promo = new Gson().fromJson(body, Promoaction.class);
        Promoaction saved = promoRepo.save(promo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @PostMapping("/promo/{id}/raffle")
    public ResponseEntity<String> play(@PathVariable(name = "id") Long id) {
        Optional<Promoaction> promo = promoRepo.findById(id);
        if (promo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (promo.get().getParticipants().size() == 0 ||
            promo.get().getParticipants().size() != promo.get().getPrizes().size()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Number of prizes and participants should be equal and > 0");
        }
        List<Participant> participants = promo.get().getParticipants();
        List<Prize> prizes = promo.get().getPrizes();
        List<Result> res = new ArrayList<>();
        for (int i = 0; i < prizes.size(); i++) {
            res.add(new Result(participants.get(i), prizes.get(i)));
        }
        return ResponseEntity.ok(new Gson().toJson(res));
    }
}
