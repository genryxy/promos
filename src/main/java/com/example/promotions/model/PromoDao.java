package com.example.promotions.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PromoDao implements Serializable {
    private Long id;
    private String name;
    private String description;
}
