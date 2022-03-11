package com.example.promotions.repo;

import com.example.promotions.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrizeRepo extends JpaRepository<Prize, Long> {
}
