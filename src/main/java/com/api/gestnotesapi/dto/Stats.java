package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    private Double reussite;
    private Double echec;
    private Double eliminer;
}
