package com.api.gestnotesapi.services;

import java.time.LocalDate;

public class AnonymatService{

    public String anonymatGenerator(String codeUE, int session, int n) {
        int annee = LocalDate.now().getYear();
        int year = annee-2000;
        return String.valueOf(codeUE + session + year + "_" + n);
    }
}
