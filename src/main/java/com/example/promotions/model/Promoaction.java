package com.example.promotions.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "name"})
public class Promoaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;

    @OneToMany(mappedBy = "id")
    private List<Prize> prizes;

    @ManyToMany
    @JoinTable(
        name = "promo_x_participant",
        joinColumns = @JoinColumn(name = "promoaction_id"),
        inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private List<Participant> participants;
}
