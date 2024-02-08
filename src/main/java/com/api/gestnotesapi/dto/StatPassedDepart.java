package com.api.gestnotesapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatPassedDepart {
    private String code;
    private String annee;
    private double stat;

    public StatPassedDepart(String code, String annee, double stat) {
        this.code = code;
        this.annee = annee;
        this.stat = stat;
    }
}
