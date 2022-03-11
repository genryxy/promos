package com.example.promotions.repo;

import com.example.promotions.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepo extends JpaRepository<Participant, Long> {
}
