package com.example.promotions.repo;

import com.example.promotions.model.Promoaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoactionRepo extends JpaRepository<Promoaction, Long> {
}
