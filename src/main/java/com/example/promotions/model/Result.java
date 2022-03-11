package com.example.promotions.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"winner", "prize"})
public class Result implements Serializable {
    private Participant winner;
    private Prize prize;
}
